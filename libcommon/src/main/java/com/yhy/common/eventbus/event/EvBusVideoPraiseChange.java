package com.yhy.common.eventbus.event;

public class EvBusVideoPraiseChange {
    public long ugcId;
    public long liveId;
    public boolean isPraise;

    /**
     * true 增加 false 删除
     * @param ugcId
     * @param liveId
     * @param isPraise
     */
    public EvBusVideoPraiseChange(long ugcId,long liveId, boolean isPraise) {
        this.ugcId = ugcId;
        this.liveId = liveId;
        this.isPraise = isPraise;
    }
}
