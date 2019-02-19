package com.videolibrary.chat.event;

import com.videolibrary.chat.protobuf.IMLive;

/**
 * Created with Android Studio.
 * Title:LiveChatPersonCountEvent
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:邓明佳
 * Date:2016/9/6
 * Time:10:37
 * Version 1.0
 */
public class LiveChatPersonCountEvent {
    public IMLive.IMGroupUserNotifyInOut notifyInOut;
    public Event event;
    public LiveChatPersonCountEvent(IMLive.IMGroupUserNotifyInOut out, Event event) {
        notifyInOut = out;
        this.event = event;
    }

    public enum Event{
        LOGIN,LOGOUT
    }
}
