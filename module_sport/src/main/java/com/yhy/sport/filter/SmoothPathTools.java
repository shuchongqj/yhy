package com.yhy.sport.filter;

import android.support.annotation.Nullable;

import com.amap.api.location.AMapLocation;

import java.util.List;

/**
 * @Description: 平滑处理实现类
 * @Created by zhaolei.yang 2018-07-07 17:19
 */
public class SmoothPathTools {

    private SmoothPathTools() {
        //Do Nothing;
    }

    /**
     * 平滑处理，包括降噪，滤波和抽稀操作
     *
     * @param amapList
     * @return
     */
    @Nullable
    public static List<AMapLocation> process(List<AMapLocation> amapList) {

        int size;
        if (amapList == null || (size = amapList.size()) == 0)
            return null;

        if (size <= 2)
            return amapList;

        NoiseFilter nf = new NoiseFilter();
        List<AMapLocation> noiseFilterList = nf.reduceNoiseAMap(amapList);

        KalmanFilter kf = new KalmanFilter();
        List<AMapLocation> kalmanFilterList = kf.processAMap(noiseFilterList);

        return ReducerFilter.reducerAMap(kalmanFilterList);
    }
}
