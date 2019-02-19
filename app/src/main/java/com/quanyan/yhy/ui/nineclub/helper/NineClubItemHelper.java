package com.quanyan.yhy.ui.nineclub.helper;

import android.content.Context;

import com.quanyan.yhy.R;
import com.quanyan.yhy.ui.adapter.base.BaseAdapterHelper;
import com.quanyan.yhy.ui.adapter.base.QuickAdapter;
import com.yhy.common.beans.net.model.RCShowcase;

import java.util.List;

/**
 * Created with Android Studio.
 * Title:NineClubItemHelper
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:baojie
 * Date:2016-3-8
 * Time:17:13
 * Version 1.0
 */

public class NineClubItemHelper {
    public static QuickAdapter<RCShowcase> setAdapter(Context context, List<RCShowcase> datas) {
        QuickAdapter<RCShowcase> adapter = new QuickAdapter<RCShowcase>(context, R.layout.item_list_nineclub, datas) {
            @Override
            protected void convert(BaseAdapterHelper helper, RCShowcase item) {
                helper.setImageUrl(R.id.iv_background, item.imgUrl, 750, 360, R.mipmap.ic_default_list_big);
                //helper.setText(R.id.tv_desc, item.title);
            }
        };

        return adapter;
    }
}
