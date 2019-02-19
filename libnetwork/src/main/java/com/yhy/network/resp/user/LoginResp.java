package com.yhy.network.resp.user;


public class LoginResp {
    private String	token;	// 登录凭据
    private long	expire;	// 过期时间
    private long	uid;	// 用户id
    private long	lockTime;	// 用户锁定时间
    private String	mobile;	// 手机号
    private boolean	isGuide;	// 导游身份

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getExpire() {
        return expire;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getLockTime() {
        return lockTime;
    }

    public void setLockTime(long lockTime) {
        this.lockTime = lockTime;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public boolean isGuide() {
        return isGuide;
    }

    public void setGuide(boolean guide) {
        isGuide = guide;
    }
}
