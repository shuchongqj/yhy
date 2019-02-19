package com.yhy.common.eventbus.event;

/**
 * Created by yangboxue on 2018/6/28.
 */

public class EvBusCircleChangePraise {
    public long id;
    public String isSupport;

    public EvBusCircleChangePraise(long id, String isSupport) {
        this.id = id;
        this.isSupport = isSupport;
    }
}
