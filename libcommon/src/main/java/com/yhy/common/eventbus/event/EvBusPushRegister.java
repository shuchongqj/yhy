package com.yhy.common.eventbus.event;

/**
 * 推送注册成功后发出
 */
public class EvBusPushRegister {

    private String token;

    public EvBusPushRegister(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
