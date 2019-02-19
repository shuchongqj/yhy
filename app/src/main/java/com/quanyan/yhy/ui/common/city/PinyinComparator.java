package com.quanyan.yhy.ui.common.city;

import com.yhy.common.beans.city.bean.AddressBean;

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

public class PinyinComparator implements Comparator<Object> {
    @Override
    public int compare(Object lhs, Object rhs) {
        AddressBean addressBeans1 = (AddressBean) lhs;
        AddressBean addressBeans2 = (AddressBean) rhs;
        String str1 = addressBeans1.getPinyin();
        String str2 = addressBeans2.getPinyin();
        return str1.compareTo(str2);
    }
}
