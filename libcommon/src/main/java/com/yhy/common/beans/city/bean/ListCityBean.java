package com.yhy.common.beans.city.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:ListCityBean
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:baojie
 * Date:2016-3-10
 * Time:15:41
 * Version 1.0
 */

public class ListCityBean implements Serializable {
    private static final long serialVersionUID = 3801697956231964944L;
    private List<AddressBean> datas;

    public List<AddressBean> getDatas() {
        return datas;
    }

    public void setDatas(List<AddressBean> datas) {
        this.datas = datas;
    }
}
