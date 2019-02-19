package com.yhy.common.eventbus.event;

import com.yhy.common.beans.user.User;

/**
 * 获取用户信息的事件
 */
public class EvBusGetUserInfo {

    private User user;

    public EvBusGetUserInfo(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
