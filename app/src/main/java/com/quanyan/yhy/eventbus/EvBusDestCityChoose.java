package com.quanyan.yhy.eventbus;

/**
 * Created with Android Studio.
 * Title:AboutAndFeedController
 * Description:直播消息对象
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:zhaoxiaopo
 * Date:2/14/16
 * Time:10:47
 * Version 1.0
 */
public class EvBusDestCityChoose {

    private String cityCode;
    private String cityName;

    public EvBusDestCityChoose(String cityCode, String cityName){
        this.cityCode = cityCode;
        this.cityName = cityName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public String getCityName() {
        return cityName;
    }
}
