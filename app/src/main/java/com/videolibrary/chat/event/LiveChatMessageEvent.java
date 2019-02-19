package com.videolibrary.chat.event;

/**
 * Created with Android Studio.
 * Title:LiveChatMessageEvent
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:邓明佳
 * Date:2016/8/16
 * Time:15:16
 * Version 1.0
 */
public class LiveChatMessageEvent {
    Event event;
    public Object object;

    public enum Event {
        SEND_SUCESS,
        SEND_FAIL,
        FORBIN_TALK, REC

    }

    public LiveChatMessageEvent(Event event, Object object) {
        this.event = event;
        this.object = object;
    }

    public Event getEvent() {
        return event;
    }

    public Object getObject() {
        return object;
    }
}
