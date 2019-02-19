package com.quanyan.yhy.eventbus;

/**
 * Created with Android Studio.
 * Title:AboutAndFeedController
 * Description:直播消息对象
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:zhaoxiaopo
 * Date:2/14/16
 * Time:10:47
 * Version 1.0
 */
public class EvBusUGCInfoAttention {

    private long userId;

    private boolean isFollow;

    public EvBusUGCInfoAttention(long userId, boolean isFollow){
        this.userId = userId;
        this.isFollow = isFollow;
    }

    public long getUserId() {
        return userId;
    }

    public boolean isFollow() {
        return isFollow;
    }
}
