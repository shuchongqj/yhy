package com.videolibrary.chat.entity;

import com.quanyan.yhy.R;
import com.videolibrary.chat.Constants;

/**
 * Created with Android Studio.
 * Title:TextMessage
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:邓明佳
 * Date:2016/8/16
 * Time:15:39
 * Version 1.0
 */
public class LiveChatNotifyMessage extends LiveChatMessageEntity {
    int textColorResId;

    public LiveChatNotifyMessage(long fromId, long toId, String fromName, String fromPic, int msgType, String messageContent, int textColorResId) {
        super(fromId, toId, fromName, fromPic, msgType, messageContent);
        this.textColorResId = textColorResId;
    }

    @Override
    public byte[] getSendContent() {
        return null;
    }

    public static LiveChatNotifyMessage createMessageByLocal(long fromId, String fromName,String message) {
        return new LiveChatNotifyMessage(fromId, 0, fromName, null, Constants.MSG_TYPE_NOTIFY, message, R.color.orange_ying);
    }

    public static LiveChatNotifyMessage createMessageByLocal(String message) {
        return new LiveChatNotifyMessage(0, 0, null, null, Constants.MSG_TYPE_NOTIFY, message, R.color.orange_ying);
    }

    public static LiveChatNotifyMessage createMessageByLocal(String message, int textColorResId) {
        return new LiveChatNotifyMessage(0, 0, null, null, Constants.MSG_TYPE_NOTIFY, message, R.color.orange_ying);
    }
}
