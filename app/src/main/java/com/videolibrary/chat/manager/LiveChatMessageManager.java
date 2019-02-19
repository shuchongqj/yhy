package com.videolibrary.chat.manager;

import android.text.TextUtils;

import com.google.protobuf.ByteString;
import com.google.protobuf.CodedInputStream;
import com.mogujie.tt.imservice.callback.Packetlistener;
import com.mogujie.tt.utils.Logger;
import com.videolibrary.chat.entity.EntityUtil;
import com.videolibrary.chat.entity.LiveChatMessageEntity;
import com.videolibrary.chat.entity.LiveChatNotifyMessage;
import com.videolibrary.chat.event.LiveChatMessageEvent;
import com.videolibrary.chat.event.LiveChatPersonCountEvent;
import com.videolibrary.chat.event.LiveReStartEvent;
import com.videolibrary.chat.event.LiveStatusEvent;
import com.videolibrary.chat.protobuf.IMBaseDefine;
import com.videolibrary.chat.protobuf.IMLive;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import de.greenrobot.event.EventBus;

/**
 * Created with Android Studio.
 * Title:LiveChatMessageManager
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:邓明佳
 * Date:2016/8/16
 * Time:11:24
 * Version 1.0
 */
public class LiveChatMessageManager extends LiveChatManager {
    private Logger logger = Logger.getLogger(LiveChatMessageManager.class);

    private static LiveChatMessageManager inst = new LiveChatMessageManager();

    public static LiveChatMessageManager instance() {
        return inst;
    }

    @Override
    public void doOnStart() {

    }

    @Override
    public void doOnStop() {

    }

    public void sendMessage(final LiveChatMessageEntity entity) {
        IMLive.IMGroupMsgData msgData = IMLive.IMGroupMsgData.newBuilder().setFromUserId(entity.getFromId())
                .setToGroupId(entity.getToId())
                .setUserName(entity.getFromName() == null ? "" : entity.getFromName())
                .setUserImage(entity.getFromPic() == null ? "" : entity.getFromPic())
                .setMsgType(EntityUtil.getMsgTypeForSend(entity.getMsgType()))
                .setMsgData(ByteString.copyFrom(entity.getSendContent())).build();
        int sid = IMBaseDefine.ServiceID.SID_GROUP_MSG_VALUE;
        int cid = IMBaseDefine.GroupMsgCmdID.CID_GROUP_MSG_DATA_VALUE;
        LiveChatSocketManager.instance().sendRequest(msgData, sid, cid, new Packetlistener() {
            @Override
            public void onSuccess(Object response) {
                try {
                    IMLive.IMGroupMsgDataAck ack = IMLive.IMGroupMsgDataAck.parseFrom((CodedInputStream) response);
                    IMBaseDefine.ResultType type = ack.getResultInfo().getResultCode();
                    if (type == IMBaseDefine.ResultType.REFUSE_REASON_NONE) {
                        triggerEvent(new LiveChatMessageEvent(LiveChatMessageEvent.Event.SEND_SUCESS, entity));
                    } else if (type == IMBaseDefine.ResultType.REFUSE_REASON_FORBID_TALK) {
                        triggerEvent(new LiveChatMessageEvent(LiveChatMessageEvent.Event.FORBIN_TALK, entity));
                    } else {
                        triggerEvent(new LiveChatMessageEvent(LiveChatMessageEvent.Event.SEND_FAIL, entity));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFaild() {
                triggerEvent(new LiveChatMessageEvent(LiveChatMessageEvent.Event.SEND_FAIL, entity));
            }

            @Override
            public void onTimeout() {
                triggerEvent(new LiveChatMessageEvent(LiveChatMessageEvent.Event.SEND_FAIL, entity));
            }
        });
    }

    private void triggerEvent(Object event) {
        EventBus.getDefault().post(event);
    }

    public void packetDisPatch(int commandId, CodedInputStream codedInputStream) {
        switch (commandId) {
            case IMBaseDefine.GroupMsgCmdID.CID_GROUP_MSG_DATA_VALUE:
                onRecMsg(codedInputStream);
                break;
        }
    }

    private void onRecMsg(CodedInputStream codedInputStream) {
        try {
            IMLive.IMGroupMsgData msgData = IMLive.IMGroupMsgData.parseFrom(codedInputStream);
            LiveChatMessageEntity entity = EntityUtil.getEntityFromPB(msgData);
            if (entity != null) {
                triggerEvent(new LiveChatMessageEvent(LiveChatMessageEvent.Event.REC, entity));
            }
        } catch (IOException e) {
            logger.e("onRecMsg error parse error");
        }
    }

    public void packetDisPatchNotify(int commandId, CodedInputStream codedInputStream) {
        try {
            LiveChatNotifyMessage message;
            switch (commandId) {
                case IMBaseDefine.UserNotifyCmdID.CID_USER_NOTIFY_IN_OUT_VALUE:
                    IMLive.IMGroupUserNotifyInOut out = IMLive.IMGroupUserNotifyInOut.parseFrom(codedInputStream);
                    StringBuilder sb = new StringBuilder();
                    String userName = out.getUserInfo(0).getUserName();
                    long userId = out.getUserInfo(0).getUserId();
                    sb.append(userName);
                    if (out.getUserAction() == IMBaseDefine.UserAction.USER_ACTION_LOGIN) {//进入房间
                        EventBus.getDefault().post(new LiveChatPersonCountEvent(out, LiveChatPersonCountEvent.Event.LOGIN));
                        sb.append(" 来了");
                    } else if (out.getUserAction() == IMBaseDefine.UserAction.USER_ACTION_LOGOUT) {//退出房间
                        EventBus.getDefault().post(new LiveChatPersonCountEvent(out, LiveChatPersonCountEvent.Event.LOGOUT));
                        return;
                    }
                    if (userId < 0 || userId == LiveChatLoginManager.instance().getCurrentUserId()) {
                        return;
                    }
                    message = LiveChatNotifyMessage.createMessageByLocal(userId,userName,sb.toString());
                    triggerEvent(new LiveChatMessageEvent(LiveChatMessageEvent.Event.REC, message));
                    break;
                case IMBaseDefine.UserNotifyCmdID.CID_USER_NOTIFY_OTHERS_REQ_VALUE:
                    IMLive.IMGroupUserNotifyOthersReq req = IMLive.IMGroupUserNotifyOthersReq.parseFrom(codedInputStream);
                    String content = req.getOtherNotify().toStringUtf8();
                    try {
//                        "msgNotifyContent": "逆光少年 关注了主播",
//                                "msgNotifyType": "FOLLOW_MSG",
//                                "topic": "LIVE"
                        JSONObject jsonObject = new JSONObject(content);
                        String topic = jsonObject.optString("topic");
                        if ("LIVE".equals(topic)) {
                            String msgNotifyType = jsonObject.optString("msgNotifyType");
                            String msgContent = jsonObject.optString("msgNotifyContent");
                            if (msgNotifyType.equals("ANCHOR_LEAVE")) {
//                                triggerEvent(LiveStatusEvent.ANCHOR_LEAVE);
                            } else if (msgNotifyType.equals("LIVE_UPDATE_STATUS")) {
                                if ("3".equals(msgContent)) {
                                    triggerEvent(LiveStatusEvent.LIVE_MASTER_FINISH);
                                } else if ("4".equals(msgContent)) {
                                    triggerEvent(LiveStatusEvent.LIVE_FINISH);
                                } else if ("6".equals(msgContent)) {
                                    triggerEvent(LiveStatusEvent.LIVE_INVALID);
                                }
                                break;
                            } else if (msgNotifyType.equals("LIVE_START")) {
                                JSONObject jsonObject1 = new JSONObject(msgContent);
                                long liveId = jsonObject1.getLong("id");
                                long ancherId = jsonObject1.getLong("userId");
                                triggerEvent(new LiveReStartEvent(liveId, ancherId));
                                break;
                            }else if(msgNotifyType.equals("DELETE_LIVE")){
                                triggerEvent(LiveStatusEvent.DELETE_LIVE);
                                break;
                            }else if(msgNotifyType.equals("LIVE_OFF_SHELVE")){
                                triggerEvent(LiveStatusEvent.LIVE_OFF_SHELVE);
                                break;
                            }
                            if (!TextUtils.isEmpty(msgContent)) {
                                message = LiveChatNotifyMessage.createMessageByLocal(msgContent);
                                triggerEvent(new LiveChatMessageEvent(LiveChatMessageEvent.Event.REC, message));
                            }
                        }
                    } catch (JSONException e) {
                        return;
                    }
                    break;
            }
        } catch (IOException e) {
            logger.e("packetDisPatchNotify error parse error");
        }
    }
}
