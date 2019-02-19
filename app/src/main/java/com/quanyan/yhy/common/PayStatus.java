package com.quanyan.yhy.common;

/**
 * Created with Android Studio.
 * Title:PayStatus
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2015-12-3
 * Time:19:10
 * Version 1.0
 * Description:
 */
public class PayStatus {

    public static final String PAYOK = "9000";//支付成功

    public static final String PAYING = "8000";//支付结果确认中

    public static final String PAYERROR = "7000";//支付失败

    public static final int PAYSERVEROK = 10000;//服务器返回支付成功

    public static final String PAYTYPE_ZFB = "PAY_ALI_SDK";//类型支付宝
    public static final String PAYTYPE_WX = "PAY_WX";//类型微信
    public static final String SOURCE_TYPE = "APP";//类型微信

}
