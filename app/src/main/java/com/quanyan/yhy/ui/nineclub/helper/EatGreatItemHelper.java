package com.quanyan.yhy.ui.nineclub.helper;

import android.content.Context;

import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.R;
import com.quanyan.yhy.ui.adapter.base.BaseAdapterHelper;
import com.quanyan.yhy.ui.adapter.base.QuickAdapter;
import com.yhy.common.beans.net.model.master.MerchantInfo;

import java.util.List;

/**
 * Created with Android Studio.
 * Title:EatGreatItemHelper
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:baojie
 * Date:2016-3-17
 * Time:14:11
 * Version 1.0
 */

public class EatGreatItemHelper {
    public static QuickAdapter<MerchantInfo> setAdapter(Context context, List<MerchantInfo> datas) {
        QuickAdapter<MerchantInfo> adapter = new QuickAdapter<MerchantInfo>(context, R.layout.item_eat_product, datas) {
            @Override
            protected void convert(BaseAdapterHelper helper, MerchantInfo item) {
                helper.setFrescoImageUrl(R.id.iv_eat_product_bg, item.icon, 500, 240, R.mipmap.icon_default_215_150);
                helper.setText(R.id.tv_eat_product_name, item.name.trim());
                //helper.setText(R.id.tv_eat_product_price, String.format(context.getString(R.string.eat_label_price), String.valueOf(item.avgprice/100)));
                helper.setText(R.id.tv_eat_product_address, item.city);
                helper.setText(R.id.tv_eat_product_price, String.format(context.getString(R.string.eat_label_price), StringUtil.converRMb2YunNoFlag(item.avgprice)));
            }
        };
        return adapter;
    }
}
