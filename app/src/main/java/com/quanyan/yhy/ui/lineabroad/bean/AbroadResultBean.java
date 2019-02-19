package com.quanyan.yhy.ui.lineabroad.bean;


import java.util.List;

/**
 * Created with Android Studio.
 * Title:AbroadResultBean
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-6-2
 * Time:16:49
 * Version 1.1.0
 */

public class AbroadResultBean {
    private String index;
    private List<AbroadAreaBean> lists;

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public List<AbroadAreaBean> getLists() {
        return lists;
    }

    public void setLists(List<AbroadAreaBean> lists) {
        this.lists = lists;
    }
}
