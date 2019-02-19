package com.yhy.common.beans.net.model;

import com.yhy.common.beans.net.model.rc.BaseInfo;

/**
 * Created with Android Studio.
 * Title:BaseInfoModel
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:朱道顺
 * Date:2015/12/10
 * Time:20:00
 * Version 1.0
 */
public class BaseInfoModel {
    public boolean isExpand = false;
    private BaseInfo baseInfo;

    public BaseInfo getBaseInfo() {
        return baseInfo;
    }

    public void setBaseInfo(BaseInfo baseInfo) {
        this.baseInfo = baseInfo;
    }
}
