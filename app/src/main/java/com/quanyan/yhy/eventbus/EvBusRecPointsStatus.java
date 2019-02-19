package com.quanyan.yhy.eventbus;

/**
 * Created with Android Studio.
 * Title:AboutAndFeedController
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:zhaoxiaopo
 * Date:16/4/27
 * Time:21:18
 * Version 1.0
 */
public class EvBusRecPointsStatus {
    public EvBusRecPointsStatus(String status) {
        this.mStatus = status;
    }

    /**
     * 1领取成功 2已经领取 3领取失败
     */
    private String mStatus;

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }
}
