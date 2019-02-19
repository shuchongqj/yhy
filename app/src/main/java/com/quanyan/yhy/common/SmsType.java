package com.quanyan.yhy.common;

/**
 * Created with Android Studio.
 * Title:SmsType
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:徐学东
 * Date:16/4/21
 * Time:下午12:15
 * Version 1.0
 */
public class SmsType {
    /**
     * 注册
     */
    public static final String REGISTER = "REGISTER";
    /**
     * 登录
     */
    public static final String LOGIN = "LOGIN";
    /**
     * 忘记密码
     */
    public static final String RETRIVE_PASSWORD = "RETRIVE_PASSWORD";
    /**
     * 登录状态下忘记密码
     */
    public static final String RETRIVE_PASSWORD_BY_LOGIN = "RETRIVE_PASSWORD_BY_LOGIN";
    /**
     * 登录状态下更改手机号码
     */
    public static final String  CHANGE_PHONE= "CHANGE_PHONE";
    /**
     * 绑定手机号码
     */
    public static final String BIND_MOBILE_PHONE = "BIND_MOBILE_PHONE";
    //QQ登录
    public static final String THIRD_PARTY_TYPE_QQ = "QQ";

    //微博登录
    public static final String THIRD_PARTY_TYPE_WEIBO = "WEIBO";

    //微信登录
    public static final String THIRD_PARTY_TYPE_WX = "WEIXIN_UNIONID";
}
