package com.quanyan.yhy.ui.nineclub.helper;

import android.content.Context;
import android.view.View;

import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.MerchantType;
import com.quanyan.yhy.ui.adapter.base.BaseAdapterHelper;
import com.quanyan.yhy.ui.adapter.base.QuickAdapter;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.yhy.common.beans.net.model.trip.ShortItem;

import java.util.List;

/**
 * Created with Android Studio.
 * Title:BuyMustItemHelper
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:baojie
 * Date:2016-3-14
 * Time:16:03
 * Version 1.0
 */

public class BuyMustItemHelper {
    public static QuickAdapter<ShortItem> setAdapter(Context context, List<ShortItem> datas) {
        QuickAdapter<ShortItem> adapter = new QuickAdapter<ShortItem>(context, R.layout.item_buymustadapter, datas) {
            @Override
            protected void convert(BaseAdapterHelper helper, ShortItem item) {
                handlerBuyMustData(context, helper, item);
            }
        };
        return adapter;
    }

    public static void handlerBuyMustData(final Context context, final BaseAdapterHelper helper, final ShortItem item) {
        helper.setFrescoImageUrl(R.id.item_buymust_img, item.mainPicUrl, 750, 360, R.mipmap.ic_default_list_big);
        helper.setText(R.id.item_buymust_title, item.title)
                .setText(R.id.item_buymust_price, StringUtil.convertPriceNoSymbolExceptLastZero(item.price))
                .setText(R.id.tv_buymust_sales, StringUtil.formatSales(context, item.sales));

        if (item.userInfo != null) {
            helper.setHeaderIcon(R.id.iv_buymust_shop_head, item.userInfo.avatar, item.userInfo.sellerType,
                    context.getResources().getInteger(R.integer.list_user_head_radius),
                    context.getResources().getInteger(R.integer.list_user_head_radius));
//            helper.setImageUrlRound(R.id.iv_buymust_shop_head, ImageUtils.getImageFullUrl(item.userInfo.avatar), 50, 50, R.mipmap.icon_default_128_128);
            helper.setText(R.id.tv_buymust_shop_title, item.userInfo.nickname);

            helper.setOnClickListener(R.id.iv_buymust_shop_head, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //跳转商铺详情
                    if (item.userInfo != null) {
                        long userId = item.userInfo.userId == 0 ? item.userInfo.id : item.userInfo.userId;
                        if(MerchantType.MERCHANT.equals(item.userInfo.sellerType)){
                            NavUtils.gotoMustBuyShopHomePageActivity(context,item.userInfo.nickname, userId);
                        }else if(MerchantType.TALENT.equals(item.userInfo.sellerType)){
                            NavUtils.gotoMasterHomepage(context,userId);
                        }
                    }
                }
            });

            helper.setOnClickListener(R.id.tv_buymust_shop_title, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.userInfo != null) {
                        long userId = item.userInfo.userId == 0 ? item.userInfo.id : item.userInfo.userId;
                        if(MerchantType.MERCHANT.equals(item.userInfo.sellerType)){
                            NavUtils.gotoMustBuyShopHomePageActivity(context,item.userInfo.nickname, userId);
                        }else if(MerchantType.TALENT.equals(item.userInfo.sellerType)){
                            NavUtils.gotoMasterHomepage(context,userId);
                        }
                    }
                }
            });
        }else {
            helper.setVisible(R.id.ll_shopll, false);
        }
    }
}
