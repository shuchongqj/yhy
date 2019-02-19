package com.quanyan.yhy.ui.nineclub.bean;


import com.yhy.common.utils.JSONUtils;

/**
 * Created with Android Studio.
 * Title:JourneyDays
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:baojie
 * Date:2016-4-6
 * Time:9:25
 * Version 1.0
 */

public class JourneyDays {
    //最小天数大于0有效
    private int minDays;
    //最大天数，大于0且大于等于最小天数有效
    private int maxDays;

    public int getMinDays() {
        return minDays;
    }

    public void setMinDays(int minDays) {
        this.minDays = minDays;
    }

    public int getMaxDays() {
        return maxDays;
    }

    public void setMaxDays(int maxDays) {
        this.maxDays = maxDays;
    }

    /**
     * 转化为Json字符串
     * @return
     */
    public String toJsonString(){
        return JSONUtils.toJson(this);
    }
}
