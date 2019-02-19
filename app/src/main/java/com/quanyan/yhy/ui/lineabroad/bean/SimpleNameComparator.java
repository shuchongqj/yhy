package com.quanyan.yhy.ui.lineabroad.bean;

import java.util.Comparator;

/**
 * Created with Android Studio.
 * Title:PinyinComparator
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:baojie
 * Date:2016-4-13
 * Time:14:44
 * Version 1.0
 */

public class SimpleNameComparator implements Comparator<Object> {
    @Override
    public int compare(Object lhs, Object rhs) {
        AbroadAreaBean addressBeans1 = (AbroadAreaBean) lhs;
        AbroadAreaBean addressBeans2 = (AbroadAreaBean) rhs;
        String str1 = addressBeans1.getSimpleName();
        String str2 = addressBeans2.getSimpleName();
        return str1.compareTo(str2);
    }
}
