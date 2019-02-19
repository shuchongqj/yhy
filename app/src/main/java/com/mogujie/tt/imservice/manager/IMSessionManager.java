
package com.mogujie.tt.imservice.manager;


import android.text.TextUtils;

import com.mogujie.tt.DB.DBInterface;
import com.mogujie.tt.DB.sp.ConfigurationSp;
import com.mogujie.tt.imservice.event.SessionEvent;
import com.mogujie.tt.protobuf.IMBaseDefine;
import com.mogujie.tt.protobuf.IMBuddy;
import com.mogujie.tt.protobuf.helper.EntityChangeEngine;
import com.mogujie.tt.protobuf.helper.Java2ProtoBuf;
import com.mogujie.tt.protobuf.helper.ProtoBuf2JavaBean;
import com.mogujie.tt.utils.Logger;
import com.quanyan.yhy.database.DBManager;
import com.yhy.common.beans.im.NotificationMessageEntity;
import com.yhy.common.beans.im.entity.MessageEntity;
import com.yhy.common.beans.im.entity.PeerEntity;
import com.yhy.common.beans.im.entity.RecentInfo;
import com.yhy.common.beans.im.entity.SessionEntity;
import com.yhy.common.beans.im.entity.UnreadEntity;
import com.yhy.common.beans.im.entity.UserEntity;
import com.yhy.common.beans.net.model.trip.ShortItem;
import com.yhy.common.constants.DBConstant;
import com.yhy.common.constants.MessageConstant;
import com.yhy.common.constants.NotificationConstants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import de.greenrobot.event.EventBus;

/**
 * app显示首页
 * 最近联系人列表
 */
public class IMSessionManager extends IMManager {
    private Logger logger = Logger.getLogger(IMSessionManager.class);
    private static IMSessionManager inst = new IMSessionManager();

    public static IMSessionManager instance() {
        return inst;
    }

    private IMSocketManager imSocketManager = IMSocketManager.instance();
    private IMLoginManager imLoginManager = IMLoginManager.instance();
    private DBInterface dbInterface = DBInterface.instance();

    // key = sessionKey -->  sessionType_peerId
    private Map<String, SessionEntity> sessionMap = new ConcurrentHashMap<>();
    //SessionManager 状态字段
    private boolean sessionListReady = false;

    @Override
    public void doOnStart() {
    }

    @Override
    public void reset() {
        sessionListReady = false;
        sessionMap.clear();
    }

    /**
     * 实现自身的事件驱动
     *
     * @param event
     */
    public void triggerEvent(SessionEvent event) {
        switch (event) {
            case RECENT_SESSION_LIST_SUCCESS:
                sessionListReady = true;
                break;
        }
        EventBus.getDefault().post(event);
    }

    public void onNormalLoginOk() {
        logger.d("recent#onLogin Successful");
        onLocalLoginOk();
        onLocalNetOk();
    }

    public void onLocalLoginOk() {
        logger.i("session#loadFromDb");
        List<SessionEntity> sessionInfoList = dbInterface.loadAllSession();
        for (SessionEntity sessionInfo : sessionInfoList) {
            if (sessionInfo.getStatus() == MessageConstant.MSG_SENDING) {
                sessionInfo.setStatus(MessageConstant.MSG_FAILURE);
            }
            sessionMap.put(sessionInfo.getSessionKey(), sessionInfo);
        }
        addLocalNotificaitonSession(NotificationConstants.BIZ_TYPE_TRANSACTION);
        addLocalNotificaitonSession(NotificationConstants.BIZ_TYPE_INTERACTION);

        triggerEvent(SessionEvent.RECENT_SESSION_LIST_SUCCESS);
    }

    private void addLocalNotificaitonSession(int bizType) {
        String sessionKey = EntityChangeEngine.getSessionKey(bizType, DBConstant.SESSION_TYPE_NOTIFICATION, 0);
        if (sessionMap.get(sessionKey) == null) {
            NotificationMessageEntity entity = DBManager.getInstance(ctx).getLastNotification(bizType);
            if (entity != null) {
                entity.sessionType = DBConstant.SESSION_TYPE_NOTIFICATION;
                if (bizType == NotificationConstants.BIZ_TYPE_TRANSACTION) {
                    entity.msgType = DBConstant.MSG_TYPE_NOTIFICATION;
                } else if (bizType == NotificationConstants.BIZ_TYPE_INTERACTION) {
                    entity.msgType = DBConstant.MSG_TYPE_INTERACTION;
                }
                updateSession(entity);
            }
        }
    }

    public void onLocalNetOk() {
        int latestUpdateTime = dbInterface.getSessionLastTime();
        logger.d("session#更新时间:%d", latestUpdateTime);
        reqGetRecentContacts(latestUpdateTime);
    }


    /**----------------------------分割线--------------------------------*/

    /**
     * 请求最近回话
     */
    private void reqGetRecentContacts(int latestUpdateTime) {
        logger.i("session#reqGetRecentContacts");
        long loginId = IMLoginManager.instance().getLoginId();
        IMBuddy.IMRecentContactSessionReq recentContactSessionReq = IMBuddy.IMRecentContactSessionReq
                .newBuilder()
                .setLatestUpdateTime(latestUpdateTime)
                .setUserId(loginId)
                .build();
        int sid = IMBaseDefine.ServiceID.SID_BUDDY_LIST_VALUE;
        int cid = IMBaseDefine.BuddyListCmdID.CID_BUDDY_LIST_RECENT_CONTACT_SESSION_REQUEST_VALUE;
        imSocketManager.sendRequest(recentContactSessionReq, sid, cid);
    }

    /**
     * 最近回话返回
     * 与本地的进行merge
     *
     * @param recentContactSessionRsp
     */
    public void onRepRecentContacts(IMBuddy.IMRecentContactSessionRsp recentContactSessionRsp) {
        logger.i("session#onRepRecentContacts");
        long userId = recentContactSessionRsp.getUserId();
        List<IMBaseDefine.ContactSessionInfo> contactSessionInfoList = recentContactSessionRsp.getContactSessionListList();
        logger.i("contact#user:%d  cnt:%d", userId, contactSessionInfoList.size());
        /**更新最近联系人列表*/

        ArrayList<SessionEntity> needDb = new ArrayList<>();
        for (IMBaseDefine.ContactSessionInfo sessionInfo : contactSessionInfoList) {
            // 返回的没有主键Id
            if (sessionInfo.getSessionId() == 0)
                continue;
            SessionEntity sessionEntity = ProtoBuf2JavaBean.getSessionEntity(sessionInfo);
            if (sessionEntity == null) continue;
            //并没有按照时间来排序
            sessionMap.put(sessionEntity.getSessionKey(), sessionEntity);
            needDb.add(sessionEntity);
        }
        logger.d("session#onRepRecentContacts is ready,now broadcast");

        //将最新的session信息保存在DB中
        dbInterface.batchInsertOrUpdateSession(needDb);
        if (needDb.size() > 0) {
            triggerEvent(SessionEvent.RESPONSE_SESSION_OK);
            triggerEvent(SessionEvent.RECENT_SESSION_LIST_UPDATE);
            IMContactManager.instance().checkAllContactInfoUpdate();
            IMConsultManager.instance().checkAllConsultInfoUpdate();
        }
    }

    /**
     * 请求删除会话
     */
    public void reqRemoveSession(RecentInfo recentInfo, boolean needSendRemoveReq) {
        logger.i("session#reqRemoveSession");

        long loginId = imLoginManager.getLoginId();
        String sessionKey = recentInfo.getSessionKey();
        /**直接本地先删除,清楚未读消息*/
        if (sessionMap.containsKey(sessionKey)) {
            sessionMap.remove(sessionKey);
            IMUnreadMsgManager.instance().readUnreadSession(sessionKey, needSendRemoveReq);
            dbInterface.deleteSession(sessionKey);
            ConfigurationSp.instance(ctx, loginId).setSessionTop(sessionKey, false);
            triggerEvent(SessionEvent.RECENT_SESSION_LIST_UPDATE);
        }
        if (!needSendRemoveReq) return;
        IMBaseDefine.ExtInfo extInfo = null;
        if (Java2ProtoBuf.isNewSessionType(recentInfo.getSessionType())) {
            extInfo = IMBaseDefine.ExtInfo.newBuilder().setSessionType(Java2ProtoBuf.getProtoSessionTypeNew(recentInfo.getSessionType())).build();
        }
        IMBuddy.IMRemoveSessionReq removeSessionReq = null;
        IMBuddy.IMRemoveSessionReq.Builder builder = IMBuddy.IMRemoveSessionReq
                .newBuilder()
                .setUserId(loginId)
                .setSessionId(recentInfo.getPeerId())
                .setSessionType(Java2ProtoBuf.getProtoSessionType(recentInfo.getSessionType())).setMsgItem(recentInfo.getServiceId());
        if (extInfo == null) {
            removeSessionReq = builder.build();
        } else {
            removeSessionReq = builder.setExtInfo(extInfo).build();
        }
        int sid = IMBaseDefine.ServiceID.SID_BUDDY_LIST_VALUE;
        int cid = IMBaseDefine.BuddyListCmdID.CID_BUDDY_LIST_REMOVE_SESSION_REQ_VALUE;
        imSocketManager.sendRequest(removeSessionReq, sid, cid);
    }

    /**
     * 删除会话返回
     */
    public void onRepRemoveSession(IMBuddy.IMRemoveSessionRsp removeSessionRsp) {
        logger.i("session#onRepRemoveSession");
        int resultCode = removeSessionRsp.getResultCode();
        if (0 != resultCode) {
            logger.e("session#removeSession failed");
            return;
        }
    }


    /**
     * 1.自己发送消息
     * 2.收到消息
     *
     * @param msg
     */
    public void updateSession(MessageEntity msg) {
        logger.d("recent#updateSession msg:%s", msg);
        if (msg == null) {
            logger.d("recent#updateSession is end,cause by msg is null");
            return;
        }
        long loginId = imLoginManager.getLoginId();
        boolean isSend = msg.isSend(loginId);
        // 因为多端同步的问题
        long peerId = msg.getPeerId(isSend);
        long serviceId = msg.getServiceId();
        SessionEntity sessionEntity = sessionMap.get(msg.getSessionKey());
        if (sessionEntity == null) {
            logger.d("session#updateSession#not found msgSessionEntity");
            sessionEntity = EntityChangeEngine.getSessionEntity(msg);
            sessionEntity.setPeerId(peerId);
            sessionEntity.buildSessionKey();
        } else {
            logger.d("session#updateSession#msgSessionEntity already in Map");
            sessionEntity.setUpdated(msg.getUpdated());
            sessionEntity.setLatestMsgData(msg.getMessageDisplay());
            sessionEntity.setTalkId(msg.getFromId());
            //todo check if msgid is null/0
            sessionEntity.setLatestMsgId(msg.getMsgId());
            sessionEntity.setLatestMsgType(msg.getMsgType());
            sessionEntity.setStatus(msg.getStatus());
        }

        /**DB 先更新*/
        ArrayList<SessionEntity> needDb = new ArrayList<>(1);
        needDb.add(sessionEntity);
        dbInterface.batchInsertOrUpdateSession(needDb);

        sessionMap.put(sessionEntity.getSessionKey(), sessionEntity);
        triggerEvent(SessionEvent.RECENT_SESSION_LIST_UPDATE);

        UserEntity entity = IMContactManager.instance().findContact(peerId);
        if (entity == null) {
            List<Long> ids = new ArrayList<>();
            ids.add(peerId);
            IMContactManager.instance().reqUserInfoById(ids);
        }
        if (serviceId > 0 && IMConsultManager.instance().getItemMap().get(serviceId) == null) {
            List<Long> ids = new ArrayList<>();
            ids.add(serviceId);
            IMConsultManager.instance().getItemListByItemIds(ids);
        }
    }

    /**
     * 1.自己发送消息
     * 2.收到消息
     *
     * @param msg
     */
    public void updateSession(NotificationMessageEntity msg) {
        logger.d("recent#updateSession msg:%s", msg);
        if (msg == null) {
            logger.d("recent#updateSession is end,cause by msg is null");
            return;
        }
        SessionEntity sessionEntity = sessionMap.get(msg.getSessionKey());
        if (sessionEntity == null) {
            logger.d("session#updateSession#not found msgSessionEntity");
            sessionEntity = EntityChangeEngine.getSessionEntity(msg);
            sessionEntity.setPeerId(msg.getPeerId());
            sessionEntity.buildSessionKey();
        } else {
            logger.d("session#updateSession#msgSessionEntity already in Map");
            sessionEntity.setUpdated((int) (msg.getCreateTime() / 1000));
            sessionEntity.setLatestMsgData(msg.getMessage());
            sessionEntity.setTalkId(0);
            //todo check if msgid is null/0
            sessionEntity.setLatestMsgId((int) msg.getMessageId());
            sessionEntity.setLatestMsgType(msg.getMsgType());
            sessionEntity.setStatus(MessageConstant.MSG_SUCCESS);
        }

        /**DB 先更新*/
        ArrayList<SessionEntity> needDb = new ArrayList<>(1);
        needDb.add(sessionEntity);
        dbInterface.batchInsertOrUpdateSession(needDb);
        sessionMap.put(sessionEntity.getSessionKey(), sessionEntity);
        triggerEvent(SessionEvent.RECENT_SESSION_LIST_UPDATE);
    }


    public List<SessionEntity> getRecentSessionList() {
        List<SessionEntity> recentInfoList = new ArrayList<>(sessionMap.values());
        return recentInfoList;
    }

    private static void sort(List<RecentInfo> data) {
        Collections.sort(data, new Comparator<RecentInfo>() {
            public int compare(RecentInfo o1, RecentInfo o2) {
                Integer a = o1.getUpdateTime();
                Integer b = o2.getUpdateTime();

                boolean isTopA = o1.isTop();
                boolean isTopB = o2.isTop();

                if (isTopA == isTopB) {
                    // 升序
                    //return a.compareTo(b);
                    // 降序
                    return b.compareTo(a);
                } else {
                    if (isTopA) {
                        return -1;
                    } else {
                        return 1;
                    }
                }

            }
        });
    }

    // 获取最近联系人列表，RecentInfo 是sessionEntity unreadEntity user/group 等等实体的封装
    // todo every time it has to sort, kind of inefficient, change it
    public List<RecentInfo> getRecentListInfo() {
        /**整理topList*/
        List<RecentInfo> recentSessionList = new ArrayList<>();
        long loginId = IMLoginManager.instance().getLoginId();

        List<SessionEntity> sessionList = getRecentSessionList();
        Map<Long, UserEntity> userMap = IMContactManager.instance().getUserMap();
        Map<String, UnreadEntity> unreadMsgMap = IMUnreadMsgManager.instance().getUnreadMsgMap();
        Map<Long, ShortItem> itemMap = IMConsultManager.instance().getItemMap();
        HashSet<String> topList = ConfigurationSp.instance(ctx, loginId).getSessionTopList();


        for (SessionEntity recentSession : sessionList) {
            int sessionType = recentSession.getPeerType();
            long peerId = recentSession.getPeerId();
            String sessionKey = recentSession.getSessionKey();
            long serviceId = recentSession.getServiceId();
            if (peerId == IMLoginManager.instance().getLoginInfo().getPeerId()) {
                continue;
            }
            UnreadEntity unreadEntity = unreadMsgMap.get(sessionKey);
            if (sessionType == DBConstant.SESSION_TYPE_SINGLE || sessionType == DBConstant.SESSION_TYPE_CONSULT) {
                UserEntity userEntity = userMap.get(peerId);
                ShortItem shortItem = itemMap.get(serviceId);
                RecentInfo recentInfo = new RecentInfo(recentSession, userEntity, unreadEntity, shortItem);
                if (topList != null && topList.contains(sessionKey)) {
                    recentInfo.setTop(true);
                }
                recentSessionList.add(recentInfo);
            } else if (sessionType == DBConstant.SESSION_TYPE_NOTIFICATION) {
                RecentInfo recentInfo = new RecentInfo(recentSession, unreadEntity);
                if (topList != null && topList.contains(sessionKey)) {
                    recentInfo.setTop(true);
                }
                recentSessionList.add(recentInfo);
            }
        }
        sort(recentSessionList);
        return recentSessionList;
    }


    public SessionEntity findSession(String sessionKey) {
        if (sessionMap.size() <= 0 || TextUtils.isEmpty(sessionKey)) {
            return null;
        }
        if (sessionMap.containsKey(sessionKey)) {
            return sessionMap.get(sessionKey);
        }
        return null;
    }

    public PeerEntity findPeerEntity(String sessionKey) {
        if (TextUtils.isEmpty(sessionKey)) {
            return null;
        }
        // 拆分
        PeerEntity peerEntity;
        String[] sessionInfo = EntityChangeEngine.spiltSessionKey(sessionKey);
        int peerType = Integer.parseInt(sessionInfo[0]);
        long peerId = Long.parseLong(sessionInfo[1]);
        switch (peerType) {
            case DBConstant.SESSION_TYPE_SINGLE:
            case DBConstant.SESSION_TYPE_CONSULT: {
                peerEntity = IMContactManager.instance().findContact(peerId);
            }
            break;
            default:
                throw new IllegalArgumentException("findPeerEntity#peerType is illegal,cause by " + peerType);
        }
        return peerEntity;
    }

    /**
     * ------------------------实体的get set-----------------------------
     */
    public boolean isSessionListReady() {
        return sessionListReady;
    }

    public void setSessionListReady(boolean sessionListReady) {
        this.sessionListReady = sessionListReady;
    }
}
