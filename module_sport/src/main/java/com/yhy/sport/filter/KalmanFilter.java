package com.yhy.sport.filter;

import android.location.Location;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.model.LatLng;
import com.yhy.sport.model.RawLocation;
import com.yhy.sport.util.Const;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:卡尔曼滤波封装类
 * @Created by zhaolei.yang 2018-07-07 15:15
 */
public class KalmanFilter {

    private Kalman mKalman;
    private AMapLocation mLastLocation;
    private final int mIntensity; // Kalman预设强度值

    public KalmanFilter() {
        this(Const.KALMAN_INTENSITY);
    }

    KalmanFilter(int intensity) {
        mIntensity = intensity;
        mKalman = new Kalman();
    }

    /**
     * 处理高德地图数据点列表
     * 强度默认为：Const.sKALMAN_INTENSITY
     *
     * @param originList
     * @return
     */
    public List<AMapLocation> processAMap(List<AMapLocation> originList) {
        return processAMap(originList, Const.KALMAN_INTENSITY);
    }

    /**
     * 处理高德地图数据点列表
     * 强度可以指定 1 ~ 5 之间
     *
     * @param originList
     * @param intensity
     * @return
     */
    public List<AMapLocation> processAMap(List<AMapLocation> originList, int intensity) {
        List<AMapLocation> kalmanFilterList = new ArrayList<>();
        if (originList == null || originList.size() <= 2)
            return kalmanFilterList;

        mKalman.initial();//初始化滤波参数
        AMapLocation latLoc;
        AMapLocation lastLoc = originList.get(0);
        kalmanFilterList.add(lastLoc);

        for (int i = 1; i < originList.size(); i++) {
            AMapLocation curLoc = originList.get(i);
            latLoc = kalmanFilterPointAMap(lastLoc, curLoc, intensity);
            if (latLoc != null) {
                kalmanFilterList.add(latLoc);
                lastLoc = latLoc;
            }
        }

        return kalmanFilterList;
    }

    /**
     * 处理系统Location数据点列表
     * 默认强度为：Const.sKALMAN_INTENSITY
     *
     * @param originList
     * @return
     */
    public List<Location> processLoc(List<Location> originList) {
        return processLoc(originList, Const.KALMAN_INTENSITY);
    }

    /**
     * 处理系统Location数据点列表
     * 强度可以指定
     *
     * @param originList
     * @param intensity
     * @return
     */
    public List<Location> processLoc(List<Location> originList, int intensity) {
        List<Location> kalmanFilterList = new ArrayList<>();
        if (originList == null || originList.size() <= 2)
            return kalmanFilterList;

        mKalman.initial();//初始化滤波参数
        Location latLoc;
        Location lastLoc = originList.get(0);
        kalmanFilterList.add(lastLoc);

        for (int i = 1; i < originList.size(); i++) {
            Location curLoc = originList.get(i);
            latLoc = kalmanFilterPointLoc(lastLoc, curLoc, intensity);
            if (latLoc != null) {
                kalmanFilterList.add(latLoc);
                lastLoc = latLoc;
            }
        }

        return kalmanFilterList;
    }

    /**
     * 处理地理位置精简信息，只保留经纬度数据列表
     * 默认强度为：Const.sKALMAN_INTENSITY
     *
     * @param originList
     * @return
     */
    public List<LatLng> processLat(List<LatLng> originList) {
        return processLat(originList, Const.KALMAN_INTENSITY);
    }

    /**
     * 精简数据列表，只保留经纬度信息
     *
     * @param originList 原始轨迹list,list.size大于2
     * @param intensity  滤波强度（1—5）
     * @return
     */
    public List<LatLng> processLat(List<LatLng> originList, int intensity) {
        List<LatLng> kalmanFilterList = new ArrayList<>();
        if (originList == null || originList.size() <= 2)
            return kalmanFilterList;

        mKalman.initial();//初始化滤波参数
        LatLng latLng;
        LatLng lastLoc = originList.get(0);
        kalmanFilterList.add(lastLoc);

        for (int i = 1; i < originList.size(); i++) {
            LatLng curLoc = originList.get(i);
            latLng = kalmanFilterPointLat(lastLoc, curLoc, intensity);
            if (latLng != null) {
                kalmanFilterList.add(latLng);
                lastLoc = latLng;
            }
        }

        return kalmanFilterList;
    }

    /**
     * 针对单点滤波
     *
     * @param wrapper
     * @return
     */
    public AMapLocation process(LocationWrapper wrapper) {
        AMapLocation location;
        AMapLocation filterLocation = null;
        if (wrapper == null || (location = wrapper.getLocation()) == null)
            return null;

        if (mLastLocation == null) {
            mLastLocation = location;
        } else {
            filterLocation = kalmanFilterPointAMap(mLastLocation, location, mIntensity);
            mLastLocation = location;
        }

        return filterLocation;
    }

    /**
     * 单点滤波 AMapLocation
     *
     * @param lastLoc   上次定位点坐标
     * @param curLoc    本次定位点坐标
     * @param intensity 滤波强度（1—5）
     * @return 滤波后本次定位点坐标值
     */
    private AMapLocation kalmanFilterPointAMap(AMapLocation lastLoc, AMapLocation curLoc, int intensity) {
        LatLng lastLng = new LatLng(lastLoc.getLatitude(), lastLoc.getLongitude());
        LatLng curLng = new LatLng(curLoc.getLatitude(), curLoc.getLongitude());

        LatLng filtLng = kalmanFilterPointLat(lastLng, curLng, intensity);

        curLoc.setLatitude(filtLng.latitude);
        curLoc.setLongitude(filtLng.longitude);
        curLoc.setProvider(Const.KLMAN_FILTER_PROVIDER);

        return curLoc;
    }

    private Location kalmanFilterPointLoc(Location lastLoc, Location curLoc, int intensity) {
        LatLng lastLng = new LatLng(lastLoc.getLatitude(), lastLoc.getLongitude());
        LatLng curLng = new LatLng(curLoc.getLatitude(), curLoc.getLongitude());

        LatLng filtLng = kalmanFilterPointLat(lastLng, curLng, intensity);

        curLoc.setLatitude(filtLng.latitude);
        curLoc.setLongitude(filtLng.longitude);

        return curLoc;
    }

    /**
     * 单点滤波
     *
     * @param lastLoc   上次定位点坐标
     * @param curLoc    本次定位点坐标
     * @param intensity 滤波强度（1—5）
     * @return 滤波后本次定位点坐标值
     */
    private LatLng kalmanFilterPointLat(LatLng lastLoc, LatLng curLoc, int intensity) {
        if (mKalman.getPdelt_x() == 0 || mKalman.getPdelt_y() == 0) {
            mKalman.initial();
        }

        LatLng kalmanLatlng = null;
        if (lastLoc == null || curLoc == null) {
            return kalmanLatlng;
        }
        if (intensity < 1) {
            intensity = 1;
        } else if (intensity > 5) {
            intensity = 5;
        }
        for (int j = 0; j < intensity; j++) {
            kalmanLatlng = mKalman.kalmanFilter(lastLoc.longitude, curLoc.longitude, lastLoc.latitude, curLoc.latitude);
            curLoc = kalmanLatlng;
        }
        return kalmanLatlng;
    }
}
