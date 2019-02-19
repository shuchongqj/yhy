package com.ymanalyseslibrary;

/**
 * Created with Android Studio.
 * Title:AboutAndFeedController
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:zhaoxiaopo
 * Date:1/26/16
 * Time:15:07
 * Version 1.0
 */
public class AnalyticsConfig {
    /**
     * 缓存的最大长度，超过就上传
     */
    public static final int MAXLENTH = 100 * 1024;
    public static final boolean CATCH_EXCEPTION = true;

    /**
     * 上传数据的URL
     */
    //public static String URL = "http://test.data.yingheying.com/newlogwithgzip.jsp";
    /**
     * 界面的开关
     */
    public static final boolean ACTIVITY_DURATION_OPEN = true;
    public static long kContinueSessionMillis = 300000L;
    //超过该值写入文件
    public static long SAVE_LIMIT = 2048;
}
