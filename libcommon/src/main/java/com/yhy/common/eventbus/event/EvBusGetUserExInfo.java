package com.yhy.common.eventbus.event;

import com.yhy.common.beans.user.User;

/**
 * 获取用户额外信息时的事件
 */
public class EvBusGetUserExInfo {

    private User user;

    public EvBusGetUserExInfo(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
