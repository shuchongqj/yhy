package com.yhy.common.beans.net.model.trip;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:ScenicCity
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2015-11-24
 * Time:15:19
 * Version 1.0
 * Description:
 */
public class ScenicCity implements Serializable{

    private static final long serialVersionUID = 6198380268773920746L;
    private boolean isSelect;

    private CityInfo cityInfo;

    public boolean isSelect() {
        return isSelect;
    }

    public void setIsSelect(boolean isSelect) {
        this.isSelect = isSelect;
    }

    public CityInfo getCityInfo() {
        if(cityInfo == null){
            return new CityInfo();
        }
        return cityInfo;
    }

    public void setCityInfo(CityInfo cityInfo) {
        this.cityInfo = cityInfo;
    }
}
