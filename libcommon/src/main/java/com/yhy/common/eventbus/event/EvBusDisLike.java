package com.yhy.common.eventbus.event;

/**
 * 不喜欢 DisLike Event
 * Created by Jiervs on 2018/7/2.
 */

public class EvBusDisLike {

    long id;
    boolean isArticle;

    public EvBusDisLike(long id,boolean isArticle) {
        this.id = id;
        this.isArticle = isArticle;
    }

    public long getId() {
        return id;
    }
    public boolean isArticle() {
        return isArticle;
    }
}
