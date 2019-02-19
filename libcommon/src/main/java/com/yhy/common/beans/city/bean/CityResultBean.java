package com.yhy.common.beans.city.bean;

import java.util.List;

/**
 * Created with Android Studio.
 * Title:CityResultBean
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:baojie
 * Date:2016-3-9
 * Time:18:45
 * Version 1.0
 */

public class CityResultBean {
    private String index;
    private List<AddressBean> lists;

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public List<AddressBean> getLists() {
        return lists;
    }

    public void setLists(List<AddressBean> lists) {
        this.lists = lists;
    }
}
