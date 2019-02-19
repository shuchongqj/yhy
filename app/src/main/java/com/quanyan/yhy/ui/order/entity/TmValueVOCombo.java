package com.quanyan.yhy.ui.order.entity;

import com.yhy.common.beans.net.model.tm.ItemSkuVO;
import com.yhy.common.beans.net.model.tm.TmValueVO;

import java.util.List;

/**
 * Created with Android Studio.
 * Title:TmValueVOCombo
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-4-19
 * Time:10:44
 * Version 1.0
 * Description:
 */
public class TmValueVOCombo {

    private TmValueVO tmValueVO;
    private boolean isClick;
    private List<ItemSkuVO> skuList;

    public List<ItemSkuVO> getSkuList() {
        return skuList;
    }

    public void setSkuList(List<ItemSkuVO> skuList) {
        this.skuList = skuList;
    }

    public TmValueVO getTmValueVO() {
        return tmValueVO;
    }

    public void setTmValueVO(TmValueVO tmValueVO) {
        this.tmValueVO = tmValueVO;
    }

    public boolean isClick() {
        return isClick;
    }

    public void setIsClick(boolean isClick) {
        this.isClick = isClick;
    }
}
