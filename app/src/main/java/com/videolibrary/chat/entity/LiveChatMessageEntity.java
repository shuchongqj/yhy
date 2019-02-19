package com.videolibrary.chat.entity;

/**
 * Created with Android Studio.
 * Title:LiveChatMessageEntity
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:邓明佳
 * Date:2016/8/16
 * Time:11:20
 * Version 1.0
 */
public abstract class LiveChatMessageEntity {
    protected long fromId;
    protected long toId;
    protected String fromName;
    protected String fromPic;
    protected int msgType;
    protected String messageContent;

    public LiveChatMessageEntity(long fromId, long toId, String fromName, String fromPic, int msgType, String messageContent) {
        this.fromId = fromId;
        this.toId = toId;
        this.fromName = fromName;
        this.fromPic = fromPic;
        this.msgType = msgType;
        this.messageContent = messageContent;
    }

    public abstract byte[] getSendContent();

    public long getFromId() {
        return fromId;
    }

    public void setFromId(long fromId) {
        this.fromId = fromId;
    }

    public long getToId() {
        return toId;
    }

    public void setToId(long toId) {
        this.toId = toId;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getFromPic() {
        return fromPic;
    }

    public void setFromPic(String fromPic) {
        this.fromPic = fromPic;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

}
