package com.quanyan.yhy.ui.common.city;

import android.content.Context;
import android.text.TextUtils;

import com.quanyan.yhy.R;
import com.quanyan.yhy.ui.adapter.base.BaseAdapterHelper;
import com.quanyan.yhy.ui.adapter.base.QuickAdapter;
import com.yhy.common.beans.city.bean.AddressBean;

import java.util.List;

/**
 * Created with Android Studio.
 * Title:CityAdapterHelper
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:baojie
 * Date:2016-3-9
 * Time:10:56
 * Version 1.0
 */

public class CityAdapterHelper {
    public static QuickAdapter<AddressBean> setAdapter(Context context, List<AddressBean> datas) {

        QuickAdapter<AddressBean> adapter = new QuickAdapter<AddressBean>(context, R.layout.item_destselcet_city, datas) {
            @Override
            protected void convert(BaseAdapterHelper helper, AddressBean item) {
                handleItem(context, helper, item);
            }
        };


        return adapter;
    }


    private static void handleItem(Context context, BaseAdapterHelper helper, AddressBean item) {

        helper.setText(R.id.tv_item, getName(item.getName()));
        if (helper.getPosition() == 0) {
            helper.setBackgroundRes(R.id.iv_item, R.drawable.shape_oval_one);
        } else if (helper.getPosition() == 1) {
            helper.setBackgroundRes(R.id.iv_item, R.drawable.shape_oval_two);
        } else if (helper.getPosition() == 2) {
            helper.setBackgroundRes(R.id.iv_item, R.drawable.shape_oval_three);
        } else if (helper.getPosition() == 3) {
            helper.setBackgroundRes(R.id.iv_item, R.drawable.shape_oval_four);
        }

    }


    private static String getName(String str){
        if(TextUtils.isEmpty(str)){
           return "";
        }else {
            if(str.length() >= 4){
                return str.substring(0, 3) + "...";
            }else{
                return str;
            }
        }


    }
}
