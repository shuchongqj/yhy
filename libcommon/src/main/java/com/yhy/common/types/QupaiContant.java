package com.yhy.common.types;


public class QupaiContant {
    /**
     * 默认最大时长
     */
    public static int DEFAULT_DURATION_MAX_LIMIT = 20;
    /**
     * 默认最大时长
     */
    public static int DEFAULT_DURATION_LIMIT_MIN = 5;
    /**
     * 默认码率
     */
    public static int DEFAULT_BITRATE = 2000 * 1024;
    /**
     * 默认CRF参数
     */
    public static int DEFAULT_VIDEO_RATE_CRF = 6;

    /**
     * VideoPreset
     */
    public static String DEFAULT_VIDEO_Preset = "faster";
    /**
     * VideoLevel
     */
    public static int DEFAULT_VIDEO_LEVEL = 30;

    /**
     * VideoTune
     */
    public static String DEFAULT_VIDEO_TUNE = "zerolatency";
    /**
     * movflags_KEY
     */
    public static String DEFAULT_VIDEO_MOV_FLAGS_KEY = "movflags";

    /**
     * movflags_VALUE
     */
    public static String DEFAULT_VIDEO_MOV_FLAGS_VALUE = "+faststart";

    public static String APPKEY_TEST = "207e45d42c2e9d9";// 204955a834a7c36
    public static String APPSECRET_TEST = "006848de64be45f8807344e78b0ab1ff";// 55e409dc27c04a87be124a2939348b58

    public static String APPKEY_ONLINE = "2083c761e523a32";// 204955a834a7c36
    public static String APPSECRET_ONLINE = "72a3d7f738414710819961a13ee53e14";// 55e409dc27c04a87be124a2939348b58

    public static String tags = "tags";
    public static String description = "description";
    public static String space = "yingheying"; //存储目录 建议使用uid cid之类的信息
    //视频宽度
    public static int DEFAULT_VIDEO_WIDTH = 480;
    //视频高度
    public static int DEFAULT_VIDEO_HEIGHT = 360;
}
