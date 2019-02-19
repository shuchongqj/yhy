package com.yhy.common.utils;

import android.text.TextUtils;

import com.yhy.common.beans.im.NotificationMessageEntity;
import com.yhy.common.beans.im.entity.MessageEntity;
import com.yhy.common.beans.im.entity.SessionEntity;
import com.yhy.common.constants.MessageConstant;

/**
 * @author : yingmu on 15-1-5.
 * @email : yingmu@mogujie.com.
 */
public class EntityChangeEngine {

    public static SessionEntity getSessionEntity(MessageEntity msg) {
        SessionEntity sessionEntity = new SessionEntity();

        // [图文消息] [图片] [语音]
        sessionEntity.setLatestMsgData(msg.getMessageDisplay());
        sessionEntity.setUpdated(msg.getUpdated());
        sessionEntity.setCreated(msg.getUpdated());
        sessionEntity.setLatestMsgId(msg.getMsgId());
        //sessionEntity.setPeerId(msg.getFromId());
        sessionEntity.setTalkId(msg.getFromId());
        sessionEntity.setPeerType(msg.getSessionType());
        sessionEntity.setLatestMsgType(msg.getMsgType());
        sessionEntity.setStatus(msg.getStatus());
        sessionEntity.setServiceId(msg.getServiceId());
        return sessionEntity;
    }

    // todo enum
    // 组建与解析统一地方，方便以后维护
    public static String getSessionKey(long peerId, int sessionType, long serviceId) {
        String sessionKey = sessionType + "_" + peerId + "_" + serviceId;
        return sessionKey;
    }

    public static String[] spiltSessionKey(String sessionKey) {
        if (TextUtils.isEmpty(sessionKey)) {
            throw new IllegalArgumentException("spiltSessionKey error,cause by empty sessionKey");
        }
        String[] sessionInfo = sessionKey.split("_", 3);
        return sessionInfo;
    }

    public static SessionEntity getSessionEntity(NotificationMessageEntity msg) {
        SessionEntity sessionEntity = new SessionEntity();

        // [图文消息] [图片] [语音]
        sessionEntity.setLatestMsgData(msg.getMessage());
        sessionEntity.setUpdated((int) (msg.getCreateTime() / 1000));
        sessionEntity.setCreated((int) (msg.getCreateTime() / 1000));
        sessionEntity.setLatestMsgId((int) msg.getMessageId());
        //sessionEntity.setPeerId(msg.getFromId());
        sessionEntity.setTalkId(0);
        sessionEntity.setPeerType(msg.getSessionType());
        sessionEntity.setLatestMsgType(msg.getMsgType());
        sessionEntity.setStatus(MessageConstant.MSG_SUCCESS);
        sessionEntity.setServiceId(0);
        return sessionEntity;
    }
}
