package com.yhy.common.beans.im.entity;

/**
 * Created with Android Studio.
 * Title:NotifyMessage
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:邓明佳
 * Date:2016/7/13
 * Time:11:40
 * Version 1.0
 */

import java.io.Serializable;


/**
 * @author : yingmu on 14-12-31.
 * @email : yingmu@mogujie.com.
 */
public class NotifyMessage extends MessageEntity implements Serializable {

    public NotifyMessage(){}

    public NotifyMessage(MessageEntity entity) {
        /**父类的id*/
        id = entity.getId();
        msgId = entity.getMsgId();
        fromId = entity.getFromId();
        toId = entity.getToId();
        sessionKey = entity.getSessionKey();
        content = entity.getContent();
        msgType = entity.getMsgType();
        displayType = entity.getDisplayType();
        status = entity.getStatus();
        created = entity.getCreated();
        updated = entity.getUpdated();
        serviceId = entity.getServiceId();
    }

    public static NotifyMessage parseFromDB(MessageEntity entity) {
        NotifyMessage textMessage = new NotifyMessage(entity);
        return textMessage;
    }

    /**
     * Not-null value.
     * DB的时候需要
     */
    @Override
    public String getContent() {
        return content;
    }

    @Override
    public byte[] getSendContent() {
        return null;
    }
}
