package com.yhy.common.beans.net.model;

import java.io.Serializable;

/**
 * Created by zhaoxp on 2015-11-2.
 */
public class AddLiveLocationBean implements Serializable {
    private static final long serialVersionUID = 3195625248666817688L;
    private int id;
    private String title;
    private String address;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
