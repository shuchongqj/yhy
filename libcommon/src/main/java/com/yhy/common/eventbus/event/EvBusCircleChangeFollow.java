package com.yhy.common.eventbus.event;

/**
 * Created by yangboxue on 2018/6/28.
 */

public class EvBusCircleChangeFollow {

    public long userId;
    public int type;

    public EvBusCircleChangeFollow(long userId, int type) {
        this.userId = userId;
        this.type = type;
    }
}
