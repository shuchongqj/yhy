package com.mogujie.tt.imservice.event;

/**
 * Created with Android Studio.
 * Title:ConsultEvent
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:邓明佳
 * Date:2016/7/1
 * Time:16:50
 * Version 1.0
 */
public class ConsultEvent {
    public Event event;
    public Object object;

    public ConsultEvent(Event event){
        this.event = event;
    }
    public ConsultEvent(Event event,Object obj){
        this.event = event;
        this.object = obj;
    }
    public enum Event {
        SESSION_CONSULT_OK,
        PROCESS_STATE_UPDATE, CONSULT_QUEUE_UPDATE,CONSULT_CONTROL,PROCESS_STATE_UPDATE_KO
    }
}
