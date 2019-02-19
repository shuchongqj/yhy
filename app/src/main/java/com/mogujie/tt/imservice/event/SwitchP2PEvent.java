package com.mogujie.tt.imservice.event;

/**
 * Created with Android Studio.
 * Title:SwitchP2PEvent
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:邓明佳
 * Date:2016/9/21
 * Time:11:36
 * Version 1.0
 */
public class SwitchP2PEvent {
    public long fromId;
    public long toId;
    public long itemId;
    public int sesstionType;
    public Event event;

    public SwitchP2PEvent(long fromId, long toId,int sesstionType,long itemId, Event event) {
        this.fromId = fromId;
        this.toId = toId;
        this.sesstionType = sesstionType;
        this.itemId = itemId;
        this.event = event;
    }

    public enum Event {
        WRITING, STOP_WRITING
    }
}
