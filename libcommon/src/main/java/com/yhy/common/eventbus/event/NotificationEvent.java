package com.yhy.common.eventbus.event;

import com.yhy.common.beans.im.NotificationMessageEntity;

/**
 * Created with Android Studio.
 * Title:NotificationEvent
 * Description:通知消息事件
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:邓明佳
 * Date:2016/3/3
 * Time:17:02
 * Version 1.0
 */
public class NotificationEvent {
    public Event event;
    public NotificationMessageEntity entity;
    public boolean overrideUnRead;
    private boolean needNotification = true;
    public int bizType;

    public NotificationEvent(Event event, NotificationMessageEntity entity) {
        this.event = event;
        this.entity = entity;
    }

    public NotificationEvent(Event event, int bizType) {
        this.bizType = bizType;
        this.event = event;
    }

    public boolean isNeedNotification() {
        return needNotification;
    }

    public void setNeedNotification(boolean needNotification) {
        this.needNotification = needNotification;
        if (entity != null){
            entity.needNotification = needNotification;
        }

    }

    public enum Event {
        //接受新消息
        RECEIVE, UNREAD_CLEAR,
    }
}
