package com.yhy.sport.util;

/**
 * @Description: 地图相关的辅助类
 * @Created by zhaolei.yang 2018-07-10 15:12
 */
public class MapUtil {

    private MapUtil() {
        //Do Nothing;
    }

    public static long nano2milli(long nano) {
        return (long) (nano / 1e6);
    }


    // 公里计算公式
    public static String getDistanceByStep(long steps) {
        return String.format("%.2f", steps * 0.6f / 1000);
    }

    // 千卡路里计算公式
    public static String getCalorieByStep(long steps) {
        return String.format("%.1f", steps * 0.6f * 60 * 1.036f / 1000);
    }
}
