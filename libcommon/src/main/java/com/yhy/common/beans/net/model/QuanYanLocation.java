package com.yhy.common.beans.net.model;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:Location
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:徐学东
 * Date:16/4/29
 * Time:上午10:08
 * Version 1.0
 */
public class QuanYanLocation implements Serializable{

    private static final long serialVersionUID = 3022340989754626538L;

    public QuanYanLocation(double lat, double lng) {
        this.lng = lng;
        this.lat = lat;
    }

    public double getLng() {

        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double lng;

    public double lat;
}
