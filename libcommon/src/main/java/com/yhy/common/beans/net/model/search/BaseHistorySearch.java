package com.yhy.common.beans.net.model.search;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:BaseHistorySearch
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:徐学东
 * Date:16/3/8
 * Time:下午8:06
 * Version 1.0
 */
public class BaseHistorySearch implements Serializable{

    private static final long serialVersionUID = -3876201147836586136L;
    BaseHistorySearch(){}
    public BaseHistorySearch(String text) {
        this.text = text;
    }

    public String text;
}
