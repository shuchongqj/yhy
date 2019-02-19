package com.yhy.common.eventbus.event;

/**
 * EvBusNewsCommentChange
 * Created by yangboxue on 2018/7/6.
 */

public class EvBusNewsCommentChange {

    public long ugcid;
    public boolean isAdd;

    public EvBusNewsCommentChange(long ugcid, boolean isAdd) {

        this.ugcid = ugcid;
        this.isAdd = isAdd;
    }
}
