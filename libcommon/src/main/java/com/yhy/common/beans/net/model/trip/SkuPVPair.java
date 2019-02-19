package com.yhy.common.beans.net.model.trip;


import com.yhy.common.beans.net.model.tm.ItemSkuVO;

import java.io.Serializable;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:SkuPVPair
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2015-12-16
 * Time:16:05
 * Version 1.0
 * Description:
 */
public class SkuPVPair implements Serializable{

    private static final long serialVersionUID = 6410684479988413982L;
    private String name;
    private long pid;
    private List<ItemSkuVO> itemList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    public List<ItemSkuVO> getItemList() {
        return itemList;
    }

    public void setItemList(List<ItemSkuVO> itemList) {
        this.itemList = itemList;
    }
}
