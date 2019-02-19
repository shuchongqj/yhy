package com.quanyan.yhy.common;

/**
 * Created with Android Studio.
 * Title:ConsultingState
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:徐学东
 * Date:16/8/11
 * Time:上午11:57
 * Version 1.1.0
 */
public class ConsultingState {
    //未生效
    public static final int NOT_AVAILABLE = 0;
    //待付款
    public static final int WAITING_PAY = 0;
    //取消
    public static final int CANCEL = 5;
    //结束
    public static final int FINISH = 3;
    //已评价
    public static final int RATED = 4;
    //排队中
    public static final int CONSULT_IN_QUEUE = 1;
    //咨询中
    public static final int CONSULT_IN_CHAT = 2;

    //未生效
    public static final String STR_NOT_AVAILABLE = "NOT_AVAILABLE";
    //待付款
    public static final String STR_WAITING_PAY = "WAITING_PAY";
    //取消
    public static final String STR_CANCEL = "CANCEL";
    //结束
    public static final String STR_FINISH = "FINISH";
    //已评价
    public static final String STR_RATED = "RATED";
    //排队中
    public static final String STR_CONSULT_IN_QUEUE = "CONSULT_IN_QUEUE";
    //咨询中
    public static final String STR_CONSULT_IN_CHAT = "CONSULT_IN_CHAT";

}
