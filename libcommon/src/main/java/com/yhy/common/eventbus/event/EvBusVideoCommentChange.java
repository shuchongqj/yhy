package com.yhy.common.eventbus.event;

/**
 * Created by Jiervs on 2018/7/26.
 */

public class EvBusVideoCommentChange {
    public long ugcId;
    public long liveId;
    public boolean isAdd;

    /**
     * true 增加 false 删除
     * @param ugcId
     * @param liveId
     * @param isAdd
     */
    public EvBusVideoCommentChange(long ugcId,long liveId, boolean isAdd) {
        this.ugcId = ugcId;
        this.liveId = liveId;
        this.isAdd = isAdd;
    }
}
