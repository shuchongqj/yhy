package com.videolibrary.chat.event;

/**
 * Created with Android Studio.
 * Title:LiveReStartEvent
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:邓明佳
 * Date:2016/10/9
 * Time:13:52
 * Version 1.0
 */
public class LiveReStartEvent {
    public long liveId;
    public long ancherId;
    public LiveReStartEvent(long liveId,long ancherId){
        this.liveId = liveId;
        this.ancherId = ancherId;
    }
}
