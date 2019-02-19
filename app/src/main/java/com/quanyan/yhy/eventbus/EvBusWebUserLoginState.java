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
public class EvBusWebUserLoginState {
    //登录通知
    public static final int LOGIN_STATE = 0;
    //退出通知
    public static final int LOGOUT_STATE = 1;
    public EvBusWebUserLoginState(int mUserLoginState) {
        this.mUserLoginState = mUserLoginState;
    }

    /**
     * 用户登录状态，0：登录通知，1：退出通知
     */
    private int mUserLoginState;

    public int getUserLoginState() {
        return mUserLoginState;
    }

    public void setUserLoginState(int userLoginState) {
        mUserLoginState = userLoginState;
    }
}
