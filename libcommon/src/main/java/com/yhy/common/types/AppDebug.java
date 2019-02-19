package com.yhy.common.types;


public class AppDebug {

    public static boolean IS_ONLINE = false;

    public static void init(int envType){
        switch (envType) {
            case EnvType.DEVELOP:
            case EnvType.TEST:
                IS_ONLINE = false;
                break;
            case EnvType.RELEASE:
                IS_ONLINE = true;
                break;
        }
    }
    //调试监控开关，代码检测
    public static boolean DEVELOPER_MODE = IS_ONLINE;
    //网络日志开关
    public static boolean NET_DEBUG = IS_ONLINE;
    //本地调试开关
    public static boolean DEBUG_LOG = IS_ONLINE;
    //是否禁止支付
    public static boolean IS_MONKEY_TEST = IS_ONLINE;
    //经纬度和服务器给的是否一致 true一致，false 不一致
    public static final boolean GPS_REVESER = false;
    //图片加载的日志
    public static final boolean IMAGE_LOAD_DEBUG = false;
    //是否原图上传
    public static boolean ORG_BMP_UPLOAD = IS_ONLINE;
    //数据采集的开关
    public static boolean DC_SUBMIT = IS_ONLINE;
}
