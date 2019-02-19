package com.videolibrary.metadata;

/**
 * Created with Android Studio.
 * Title:LiveTypeConstans
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:赵晓坡
 * Date:9/13/16
 * Time:18:58
 * Version 1.1.0
 */
public class LiveTypeConstants {
    /**
     * 达人创建直播
     */
    public static final String CREATE_LIVE = "CREATE_LIVE";
    /**
     * 正在直播
     */
    public static final String LIVE_ING = "START_LIVE";
    /**
     * 直播正在关闭
     */
    public static final String CLOSING_LIVE = "CLOSING_LIVE";
    /**
     * 结束直播
     */
    public static final String LIVE_OVER = "END_LIVE";
    /**
     * 直播在回放状态
     */
    public static final String LIVE_REPLAY = "REPLAY_LIVE";
    /**
     * 无效直播,一定时间内未推流
     */
    public static final String LIVE_INVALID = "INVALID_LIVE";

    /**
     * 直播记录状态 删除
     */
    public static final String DELETE_LIVE = "DELETE_LIVE";
    /**
     * 直播记录状态 下架
     */
    public static final String OFF_SHELVE_LIVE = "OFF_SHELVE_LIVE";
    /**
     * 直播记录状态 正常
     */
    public static final String NORMAL_LIVE = "NORMAL_LIVE";



    public static final String FOLLOW_STATE_SUCCESS = "FOLLOW_SUCCESS";
    public static final String FOLLOW_STATE_FAILED = "FOLLOW_FAILED";
    public static final String FOLLOW_STATE_ALREADY = "HAS_FOLLOW";

}
