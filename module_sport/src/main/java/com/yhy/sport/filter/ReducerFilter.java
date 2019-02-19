package com.yhy.sport.filter;

import android.location.Location;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.model.LatLng;
import com.yhy.sport.util.Const;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 抽稀算法工具类，针对已经生成的数据列表
 * @Created by zhaolei.yang 2018-07-07 14:50
 */
public class ReducerFilter {

    private ReducerFilter() {
        //Do Nothing;
    }

    /**
     * 抽稀对外方法，形参为Location列表, 默认阈值为10
     *
     * @param inPoints
     * @return
     */
    public static List<AMapLocation> reducerAMap(List<AMapLocation> inPoints) {
        return reducerAMap(inPoints, Const.REDUCER_THRESHOLD);
    }

    /**
     * 抽稀对外方法，形参为Location列表, 阈值可定义
     *
     * @param inPoints
     * @param threshold
     * @return
     */
    public static List<AMapLocation> reducerAMap(List<AMapLocation> inPoints, float threshold) {
        int size;
        if (inPoints == null || (size = inPoints.size()) == 0)
            return null;

        if (size <= 2)
            return inPoints;

        List<AMapLocation> ret = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            AMapLocation pre = FilterHelper.getLastAMap(ret);
            AMapLocation cur = inPoints.get(i);
            if (pre == null || i == inPoints.size() - 1) {
                ret.add(cur);
                continue;
            }
            AMapLocation next = inPoints.get(i + 1);
            double distance = FilterHelper.calculateDistanceFromPoint(cur, pre, next);
            if (distance > threshold) {
                ret.add(cur);
            }
        }
        return ret;
    }

    /**
     * 抽稀对外方法，形参为Location列表, 默认阈值为10
     *
     * @param inPoints
     * @return
     */
    public static List<Location> reducerLoc(List<Location> inPoints) {
        return reducerLoc(inPoints, Const.REDUCER_THRESHOLD);
    }

    /**
     * 抽稀对外方法，形参为Location列表, 阈值可定义
     *
     * @param inPoints
     * @param threshold
     * @return
     */
    public static List<Location> reducerLoc(List<Location> inPoints, float threshold) {
        int size;
        if (inPoints == null || (size = inPoints.size()) == 0)
            return null;

        if (size <= 2)
            return inPoints;

        List<Location> ret = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Location pre = FilterHelper.getLastLoc(ret);
            Location cur = inPoints.get(i);
            if (pre == null || i == inPoints.size() - 1) {
                ret.add(cur);
                continue;
            }
            Location next = inPoints.get(i + 1);
            double distance = FilterHelper.calculateDistanceFromPoint(cur, pre, next);
            if (distance > threshold) {
                ret.add(cur);
            }
        }
        return ret;
    }

    /**
     * 抽稀对外方法，形参为LatLng列表, 默认阈值为10
     *
     * @param inPoints
     * @return
     */
    public static List<LatLng> reducerLat(List<LatLng> inPoints) {
        return reducerLat(inPoints, Const.REDUCER_THRESHOLD);
    }

    /**
     * 抽稀对外方法，形参为LatLng列表, 阈值可定义
     *
     * @param inPoints
     * @param threshHold
     * @return
     */
    public static List<LatLng> reducerLat(List<LatLng> inPoints,
                                          float threshHold) {
        if (inPoints == null) {
            return null;
        }
        if (inPoints.size() <= 2) {
            return inPoints;
        }
        List<LatLng> ret = new ArrayList<>();
        for (int i = 0; i < inPoints.size(); i++) {
            LatLng pre = FilterHelper.getLastLat(ret);
            LatLng cur = inPoints.get(i);
            if (pre == null || i == inPoints.size() - 1) {
                ret.add(cur);
                continue;
            }
            LatLng next = inPoints.get(i + 1);
            double distance = FilterHelper.calculateDistanceFromPoint(cur, pre, next);
            if (distance > threshHold) {
                ret.add(cur);
            }
        }
        return ret;
    }
}
