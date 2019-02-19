package com.mogujie.tt.imservice.manager;

import com.mogujie.tt.DB.DBInterface;
import com.mogujie.tt.imservice.event.UserInfoEvent;
import com.mogujie.tt.protobuf.IMBaseDefine;
import com.mogujie.tt.protobuf.IMBuddy;
import com.mogujie.tt.protobuf.helper.ProtoBuf2JavaBean;
import com.mogujie.tt.utils.Logger;
import com.quanyan.yhy.net.NetManager;
import com.quanyan.yhy.net.lsn.OnResponseListener;
import com.smart.sdk.api.resp.Api_USER_UserInfo;
import com.smart.sdk.api.resp.Api_USER_UserInfo_ArrayResp;
import com.yhy.common.beans.im.entity.SessionEntity;
import com.yhy.common.beans.im.entity.UserEntity;
import com.yhy.common.utils.pinyin.PinYin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import de.greenrobot.event.EventBus;

/**
 * 负责用户信息的请求
 * 为回话页面以及联系人页面提供服务
 * <p/>
 * 联系人信息管理
 * 普通用户的version  有总版本
 * 群组没有总version的概念， 每个群有version
 * 具体请参见 服务端具体的pd协议
 */
public class IMContactManager extends IMManager {
    private Logger logger = Logger.getLogger(IMContactManager.class);

    // 单例
    private static IMContactManager inst = new IMContactManager();

    public static IMContactManager instance() {
        return inst;
    }

    private IMSocketManager imSocketManager = IMSocketManager.instance();
    private DBInterface dbInterface = DBInterface.instance();

    // 自身状态字段
    private boolean userDataReady = false;
    private Map<Long, UserEntity> userMap = new ConcurrentHashMap<>();


    @Override
    public void doOnStart() {
    }

    /**
     * 登陆成功触发
     * auto自动登陆
     */
    public void onNormalLoginOk() {
        onLocalLoginOk();
//        onLocalNetOk();
    }

    /**
     * 加载本地DB的状态
     * 不管是离线还是在线登陆，loadFromDb 要运行的
     */
    public void onLocalLoginOk() {
        logger.d("contact#loadAllUserInfo");

        List<UserEntity> userlist = dbInterface.loadAllUsers();
        logger.d("contact#loadAllUserInfo dbsuccess");

        for (UserEntity userInfo : userlist) {
            // todo DB的状态不包含拼音的，这个样每次都要加载啊
            PinYin.getPinYin(userInfo.getMainName(), userInfo.getPinyinElement());
            userMap.put(userInfo.getPeerId(), userInfo);
        }

        triggerEvent(UserInfoEvent.USER_INFO_OK);
    }

//    /**
//     * 网络链接成功，登陆之后请求
//     */
//    public void onLocalNetOk(){
//        // 用户信息
//        int updateTime = dbInterface.getUserInfoLastTime();
//        logger.d("contact#loadAllUserInfo req-updateTime:%d", updateTime);
//        reqGetAllUsers(updateTime);
//    }

    public void reqUserInfoById(List<Long> ids) {
        NetManager.getInstance(ctx).doGetUserInfoById(ids, new OnResponseListener<Api_USER_UserInfo_ArrayResp>() {
            @Override
            public void onComplete(boolean isOK, Api_USER_UserInfo_ArrayResp result, int errorCode, String errorMsg) {
                if (isOK && result != null) {
                    List<Api_USER_UserInfo> userInfos = result.value;
                    ArrayList<UserEntity> needDb = new ArrayList<>();
                    for (Api_USER_UserInfo info : userInfos) {
                        UserEntity entity = ProtoBuf2JavaBean.getUserEntity(info);
                        userMap.put(entity.getPeerId(), entity);
                        needDb.add(entity);
                    }
                    dbInterface.batchInsertOrUpdateUser(needDb);
                    triggerEvent(UserInfoEvent.USER_INFO_UPDATE);
                }
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {

            }
        });
    }

    /**
     * 更新sessionlist后，检查是否已经有个人信息 如果没有加载个人信息list
     */
    public void checkAllContactInfoUpdate() {
        List<SessionEntity> sessionEntityList = IMSessionManager.instance().getRecentSessionList();
        if (sessionEntityList.size() == 0) {
            return;
        }
        List<Long> ids = new ArrayList<>();
        for (SessionEntity entity : sessionEntityList) {
            long peerId = entity.getPeerId();
            if (userMap.get(peerId) == null)
                ids.add(peerId);
        }

        if (ids.size() > 0) {
            reqUserInfoById(ids);
        }
    }


    @Override
    public void reset() {
        userDataReady = false;
        userMap.clear();
    }


    /**
     * @param event
     */
    public void triggerEvent(UserInfoEvent event) {
        //先更新自身的状态
        switch (event) {
            case USER_INFO_OK:
                userDataReady = true;
                break;
        }
        EventBus.getDefault().postSticky(event);
    }

    /**
     * -----------------------事件驱动---end---------
     */

//    private void reqGetAllUsers(int lastUpdateTime) {
//        logger.i("contact#reqGetAllUsers");
//        int userId = IMLoginManager.instance().getLoginId();
//
//        IMBuddy.IMAllUserReq imAllUserReq = IMBuddy.IMAllUserReq.newBuilder()
//                .setUserId(userId)
//                .setLatestUpdateTime(lastUpdateTime).build();
//        int sid = IMBaseDefine.ServiceID.SID_BUDDY_LIST_VALUE;
//        int cid = IMBaseDefine.BuddyListCmdID.CID_BUDDY_LIST_ALL_USER_REQUEST_VALUE;
//        imSocketManager.sendRequest(imAllUserReq, sid, cid);
//    }

    /**
     * yingmu change id from string to int
     *
     * @param imAllUserRsp 1.请求所有用户的信息,总的版本号version
     *                     2.匹配总的版本号，返回可能存在变更的
     *                     3.选取存在变更的，请求用户详细信息
     *                     4.更新DB，保存globalVersion 以及用户的信息
     */
    public void onRepAllUsers(IMBuddy.IMAllUserRsp imAllUserRsp) {
        logger.i("contact#onRepAllUsers");
        long userId = imAllUserRsp.getUserId();
        int lastTime = imAllUserRsp.getLatestUpdateTime();
        // lastTime 需要保存嘛? 不保存了

        int count = imAllUserRsp.getUserListCount();
        logger.i("contact#user cnt:%d", count);
        if (count <= 0) {
            return;
        }

        long loginId = IMLoginManager.instance().getLoginId();
        if (userId != loginId) {
            logger.e("[fatal error] userId not equels loginId ,cause by onRepAllUsers");
            return;
        }

        List<IMBaseDefine.UserInfo> changeList = imAllUserRsp.getUserListList();
        ArrayList<UserEntity> needDb = new ArrayList<>();
        for (IMBaseDefine.UserInfo userInfo : changeList) {
//            UserEntity entity =  ProtoBuf2JavaBean.getUserEntity(userInfo);
            UserEntity entity = null;
            userMap.put(entity.getPeerId(), entity);
            needDb.add(entity);
        }

        dbInterface.batchInsertOrUpdateUser(needDb);
        triggerEvent(UserInfoEvent.USER_INFO_UPDATE);
    }

    public UserEntity findContact(long buddyId) {
        if (buddyId > 0 && userMap.containsKey(buddyId)) {
            return userMap.get(buddyId);
        }
        return null;
    }

    /**
     * 请求用户详细信息
     *
     * @param userIds
     */
    public void reqGetDetaillUsers(ArrayList<Long> userIds) {
        logger.i("contact#contact#reqGetDetaillUsers");
        if (null == userIds || userIds.size() <= 0) {
            logger.i("contact#contact#reqGetDetaillUsers return,cause by null or empty");
            return;
        }
        long loginId = IMLoginManager.instance().getLoginId();
        IMBuddy.IMUsersInfoReq imUsersInfoReq = IMBuddy.IMUsersInfoReq.newBuilder()
                .setUserId(loginId)
                .addAllUserIdList(userIds)
                .build();

        int sid = IMBaseDefine.ServiceID.SID_BUDDY_LIST_VALUE;
        int cid = IMBaseDefine.BuddyListCmdID.CID_BUDDY_LIST_USER_INFO_REQUEST_VALUE;
        imSocketManager.sendRequest(imUsersInfoReq, sid, cid);
    }

    /**
     * 获取用户详细的信息
     *
     * @param imUsersInfoRsp
     */
    public void onRepDetailUsers(IMBuddy.IMUsersInfoRsp imUsersInfoRsp) {
        long loginId = imUsersInfoRsp.getUserId();
        boolean needEvent = false;
        List<IMBaseDefine.UserInfo> userInfoList = imUsersInfoRsp.getUserInfoListList();

        ArrayList<UserEntity> dbNeed = new ArrayList<>();
        for (IMBaseDefine.UserInfo userInfo : userInfoList) {
//            UserEntity userEntity = ProtoBuf2JavaBean.getUserEntity(userInfo);
            UserEntity userEntity = null;
            long userId = userEntity.getPeerId();
            if (userMap.containsKey(userId) && userMap.get(userId).equals(userEntity)) {
                //没有必要通知更新
            } else {
                needEvent = true;
                userMap.put(userEntity.getPeerId(), userEntity);
                dbNeed.add(userEntity);
                if (userInfo.getUserId() == loginId) {
                    IMLoginManager.instance().setLoginInfo(userEntity);
                }
            }
        }
        // 负责userMap
        dbInterface.batchInsertOrUpdateUser(dbNeed);

        // 判断有没有必要进行推送
        if (needEvent) {
            triggerEvent(UserInfoEvent.USER_INFO_UPDATE);
        }
    }

//    public List<UserEntity> getContactSortedList() {
//        // todo eric efficiency
//        List<UserEntity> contactList = new ArrayList<>(userMap.values());
//        Collections.sort(contactList, new Comparator<UserEntity>() {
//            @Override
//            public int compare(UserEntity entity1, UserEntity entity2) {
//                if (entity2.getPinyinElement().pinyin.startsWith("#")) {
//                    return -1;
//                } else if (entity1.getPinyinElement().pinyin.startsWith("#")) {
//                    // todo eric guess: latter is > 0
//                    return 1;
//                } else {
//                    if (entity1.getPinyinElement().pinyin == null) {
//                        PinYin.getPinYin(entity1.getMainName(), entity1.getPinyinElement());
//                    }
//                    if (entity2.getPinyinElement().pinyin == null) {
//                        PinYin.getPinYin(entity2.getMainName(), entity2.getPinyinElement());
//                    }
//                    return entity1.getPinyinElement().pinyin.compareToIgnoreCase(entity2.getPinyinElement().pinyin);
//                }
//            }
//        });
//        return contactList;
//    }

    /**------------------------部门相关的协议 end------------------------------*/

    /**
     * -----------------------实体 get set 定义-----------------------------------
     */

    public Map<Long, UserEntity> getUserMap() {
        return userMap;
    }

    public boolean isUserDataReady() {
        return userDataReady;
    }

}
