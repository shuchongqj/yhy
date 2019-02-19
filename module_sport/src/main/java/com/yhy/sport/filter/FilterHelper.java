package com.yhy.sport.filter;

import android.location.Location;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;

import java.util.List;

/**
 * @Description:滤波辅助类
 * @Created by zhaolei.yang 2018-07-07 17:31
 */
public class FilterHelper {

    private FilterHelper() {
        //Do Nothing;
    }

    public static AMapLocation getLastAMap(List<AMapLocation> locations) {
        if (locations == null || locations.size() == 0) {
            return null;
        }
        final int locListSize = locations.size();
        AMapLocation lastLocation = locations.get(locListSize - 1);
        return lastLocation;
    }

    public static Location getLastLoc(List<Location> locations) {
        if (locations == null || locations.size() == 0) {
            return null;
        }
        final int locListSize = locations.size();
        Location lastLocation = locations.get(locListSize - 1);
        return lastLocation;
    }

    public static LatLng getLastLat(List<LatLng> oneGraspList) {
        if (oneGraspList == null || oneGraspList.size() == 0) {
            return null;
        }
        int locListSize = oneGraspList.size();
        LatLng lastLocation = oneGraspList.get(locListSize - 1);
        return lastLocation;
    }

    /**
     * 计算当前点到线的垂线距离
     *
     * @param p         当前点
     * @param lineBegin 线的起点
     * @param lineEnd   线的终点
     */
    public static double calculateDistanceFromPoint(LatLng p, LatLng lineBegin,
                                                    LatLng lineEnd) {
        double A = p.longitude - lineBegin.longitude;
        double B = p.latitude - lineBegin.latitude;
        double C = lineEnd.longitude - lineBegin.longitude;
        double D = lineEnd.latitude - lineBegin.latitude;

        double dot = A * C + B * D;
        double len_sq = C * C + D * D;
        double param = dot / len_sq;

        double xx, yy;

        if (param < 0 || (lineBegin.longitude == lineEnd.longitude
                && lineBegin.latitude == lineEnd.latitude)) {
            xx = lineBegin.longitude;
            yy = lineBegin.latitude;
//            return -1;
        } else if (param > 1) {
            xx = lineEnd.longitude;
            yy = lineEnd.latitude;
//            return -1;
        } else {
            xx = lineBegin.longitude + param * C;
            yy = lineBegin.latitude + param * D;
        }
        return AMapUtils.calculateLineDistance(p, new LatLng(yy, xx));
    }

    /**
     * 计算两个坐标点间的距离，单位米
     *
     * @param cur
     * @param pre
     * @return
     */
    public static double calculateDistance(LatLng cur, LatLng pre) {
        if (cur == null || pre == null)
            return 0;

        double lon1 = (Math.PI / 180) * pre.longitude;
        double lon2 = (Math.PI / 180) * cur.longitude;
        double lat1 = (Math.PI / 180) * pre.latitude;
        double lat2 = (Math.PI / 180) * cur.latitude;

        // 地球半径
        double R = 6371;

        // 两点间距离 km
        double d = Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1))
                * R;

        return d * 1000;
    }

    public static double calculateDistanceFromPoint(Location p, Location lineBegin,
                                                    Location lineEnd) {
        LatLng latLngP = new LatLng(p.getLatitude(), p.getLongitude());
        LatLng latLngBegin = new LatLng(lineBegin.getLatitude(), lineBegin.getLongitude());
        LatLng latLngEnd = new LatLng(lineEnd.getLatitude(), lineEnd.getLongitude());

        return calculateDistanceFromPoint(latLngP, latLngBegin, latLngEnd);
    }
}
