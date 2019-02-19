package com.videolibrary.chat.entity;

import com.videolibrary.chat.Constants;
import com.videolibrary.chat.protobuf.IMBaseDefine;
import com.videolibrary.chat.protobuf.IMLive;

import static com.videolibrary.chat.protobuf.IMBaseDefine.MsgType.MSG_TYPE_GROUP_TEXT;

/**
 * Created with Android Studio.
 * Title:EntityUtil
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:赵晓坡
 * Date:16/8/19
 * Time:17:38
 * Version 1.1.0
 */
public class EntityUtil {
    public static IMBaseDefine.MsgType getMsgTypeForSend(int msgType) {
        switch (msgType) {
            case Constants.MSG_TYPE_TEXT:
                return MSG_TYPE_GROUP_TEXT;
            default:
                throw new IllegalArgumentException("msgType is illegal,cause by #getMsgTypeForSend#" + msgType);
        }
    }

    public static int getMsgTypeForEntity(IMBaseDefine.MsgType msgType) {
        switch (msgType) {
            case MSG_TYPE_GROUP_TEXT:
                return Constants.MSG_TYPE_TEXT;
            default:
                return Constants.MSG_TYPE_UNKNOWN;
        }
    }

    public static LiveChatMessageEntity getEntityFromPB(IMLive.IMGroupMsgData msgData) {
        IMBaseDefine.MsgType msgType = msgData.getMsgType();
        switch (msgType) {
            case MSG_TYPE_GROUP_TEXT:
                return LiveChatTextMessage.parseFrom(msgData);
            default:
                return null;
        }
    }
}

