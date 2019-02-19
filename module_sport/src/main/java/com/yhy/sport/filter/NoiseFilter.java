package com.yhy.sport.filter;

import android.location.Location;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.yhy.sport.util.Const;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 噪声过滤器，当前点和前一个点对比，如果距离超过设定的阈值则返回false
 * @Created by zhaolei.yang 2018-07-07 14:27
 */
public class NoiseFilter {

    private AMapLocation mLastLocation;

    private final GPSState mGPSState;
    private final SportType mSortType;

    public NoiseFilter() {
        this(new GPSState(), new SportType());
    }

    public NoiseFilter(GPSState gpsState, SportType type) {
        mGPSState = gpsState;
        mSortType = type;
    }

    /**
     * 判断大的噪点
     *
     * @param wrapper
     * @return
     */
    public boolean isValidate(LocationWrapper wrapper) {
        AMapLocation location;
        if (wrapper == null || (location = wrapper.getLocation()) == null) {
            return false;
        }

        if (mLastLocation == null) {
            mLastLocation = location;
        } else {
            /**
             * 1. 前一个GPS时间戳和当前GPS时间戳 > 1s 认为不是当前的点位 去除
             */
//            Log.i(Const.TAG, "1###定位时间戳和当前时间戳 " + Math.abs(curTime - locTime));
//            if (Math.abs(location.getTime() - mLastLocation.getTime()) > 1000) {
//                return false;
//            }

            /**
             * 2. GPS信号丢失 去除
             */
            if (mGPSState != null) {
                Log.i(Const.TAG, "2###GPS信号 " + mGPSState.obtainState().getMsg());
                if (mGPSState.obtainState().getCode() == GPSState.State.GPS_LOSS.getCode()) {
                    return false;
                }
            }

            /**
             * 3上一次获取点位时间戳与当前点位时间戳差值 <= 0.1s  认为在原地不动  去除
             */
            Log.i(Const.TAG, "3###时间差 " + Math.abs(mLastLocation.getTime() - location.getTime()));
            if (Math.abs(mLastLocation.getTime() - location.getTime()) <= 100) {
                return false;
            }

            /**
             * 4. 上一次获取点位位置与当前点位位置差值 <= 0.1m 认为原地不动 去除
             */
            LatLng pre = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            LatLng cur = new LatLng(location.getLatitude(), location.getLongitude());
            float distance = AMapUtils.calculateLineDistance(pre, cur);
            double ds = FilterHelper.calculateDistance(pre, cur);
            Log.i(Const.TAG, "4###定位的位置与前一次比较 从Amap中获取 " + distance);
            Log.i(Const.TAG, "4###定位的位置与前一次比较 自己计算 " + ds);
            if (distance <= 0.1) {
                return false;
            }

            /**
             * 7. 根据运动类型判断速度策略

             行走 --最小速度 minLimitSpeed = 0.3 m/s
             --最大速度 max(当前计算速度, gps返回的当前速度) > 5.5 m/s  -> // 估算人类竞走最快纪录 5.5 m/s

             跑步 --最小速度 minLimitSpeed = 0.5 m/s
             --最大速度 max(当前计算速度, gps返回的当前速度) > 100 / 9.58 m/s -> // 目前最新百米赛跑的世界纪录是9.58 秒

             骑行 --最小速度 minLimitSpeed = 0.8 m/s
             --最大速度 max(当前计算速度, gps返回的当前速度) > 56 m/s -> // 公路自行车世界纪录 202km/h = 56 m/s
             */
            double speed = distance * 1000 / Math.abs(mLastLocation.getTime() - location.getTime());
            Log.i(Const.TAG, "7###当前速度 计算值 " + speed);
            switch (mSortType.getType()) {
                case WALK:
                    if (speed < 0.3 || speed > 5.5) {
                        return false;
                    }

                case RUN:
                    if (speed < 0.5 || speed > 9.58) {
                        return false;
                    }

                case RIDE:
                    if (speed < 0.8 || speed > 56) {
                        return false;
                    }

                default:
                    break;
            }

            mLastLocation = location;
            return true;
        }

        return true;
    }

    /**
     * 降噪，高德地图数据列表处理
     * 阈值默认：Const.sNOISE_THRESHOLD
     *
     * @param inPoints
     * @return
     */
    public List<AMapLocation> reduceNoiseAMap(List<AMapLocation> inPoints) {
        return reduceNoiseAMap(inPoints, Const.NOISE_THRESHOLD);
    }

    /**
     * 降噪，高德地图数据列表处理，指定阈值
     *
     * @param inPoints
     * @param threshHold
     * @return
     */
    public List<AMapLocation> reduceNoiseAMap(List<AMapLocation> inPoints, float threshHold) {
        if (inPoints == null) {
            return null;
        }
        if (inPoints.size() <= 2) {
            return inPoints;
        }
        List<AMapLocation> ret = new ArrayList<>();
        for (int i = 0; i < inPoints.size(); i++) {
            AMapLocation pre = FilterHelper.getLastAMap(ret);
            AMapLocation cur = inPoints.get(i);
            if (pre == null || i == inPoints.size() - 1) {
                ret.add(cur);
                continue;
            }
            AMapLocation next = inPoints.get(i + 1);
            double distance = FilterHelper.calculateDistanceFromPoint(cur, pre, next);
            if (distance < threshHold) {
                ret.add(cur);
            }
        }
        return ret;
    }

    /**
     * 降噪，系统位置数据列表处理
     * 指定阈值
     *
     * @param inPoints
     * @param threshHold
     * @return
     */
    public List<Location> reduceNoiseLoc(List<Location> inPoints, float threshHold) {
        if (inPoints == null) {
            return null;
        }
        if (inPoints.size() <= 2) {
            return inPoints;
        }
        List<Location> ret = new ArrayList<>();
        for (int i = 0; i < inPoints.size(); i++) {
            Location pre = FilterHelper.getLastLoc(ret);
            Location cur = inPoints.get(i);
            if (pre == null || i == inPoints.size() - 1) {
                ret.add(cur);
                continue;
            }
            Location next = inPoints.get(i + 1);
            double distance = FilterHelper.calculateDistanceFromPoint(cur, pre, next);
            if (distance < threshHold) {
                ret.add(cur);
            }
        }
        return ret;
    }


    /**
     * 降噪，地理位置数据列表处理
     * 指定阈值
     *
     * @param inPoints
     * @param threshHold
     * @return
     */
    public List<LatLng> reduceNoiseLat(List<LatLng> inPoints, float threshHold) {
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
            if (distance < threshHold) {
                ret.add(cur);
            }
        }
        return ret;
    }
}
