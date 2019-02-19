package com.yhy.common.beans.net.model.mine;

import java.io.Serializable;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:MyClubActivityBean
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:baojie
 * Date:2015-11-5
 * Time:20:28
 * Version 1.0
 */

public class MyClubActivityBean implements Serializable {
    private static final long serialVersionUID = -9183761052660521841L;
    private String id;
    private String date;
    private String count;
    private int type;
    private String pay;
    private List<MyClubActivityOrderDetailBean> modb;


    public MyClubActivityBean() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }

    public List<MyClubActivityOrderDetailBean> getModb() {
        return modb;
    }

    public void setModb(List<MyClubActivityOrderDetailBean> modb) {
        this.modb = modb;
    }
}
