package com.quanyan.yhy.ui.shop.helper;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.harwkin.nb.camera.ImageUtils;
import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.CouponStatus;
import com.quanyan.yhy.ui.adapter.base.BaseAdapterHelper;
import com.quanyan.yhy.ui.adapter.base.QuickAdapter;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.common.calendar.ScreenUtil;
import com.quanyan.yhy.ui.master.helper.MasterViewHelper;
import com.yhy.common.beans.net.model.shop.ShopProductHorInfo;
import com.yhy.common.beans.net.model.tm.ExpressDetailInfo;
import com.yhy.common.beans.net.model.tm.LgExpressLineInfo;
import com.yhy.common.beans.net.model.tm.VoucherTemplateResult;
import com.yhy.common.beans.net.model.trip.ShortItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:ShopHomePageViewHelper
 * Description:店铺相关视图数据绑定工具
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:徐学东
 * Date:16/3/2
 * Time:下午2:42
 * Version 1.0
 */
public class ShopViewHelper {
    /**
     * 处理店铺横向商品列表页展示
     * @param context
     * @param helper
     * @param item
     */
    public static void handleShopProductHorListItem(final Context context,
                                                    BaseAdapterHelper helper,
                                                    ShopProductHorInfo item) {
        final ShortItem info1 = item.info1;
        final ShortItem info2 = item.info2;
        int imageWidth = ScreenUtil.getScreenWidth(context) / 2;
        LinearLayout l1 =  helper.getView(R.id.ll_product_1);
        LinearLayout l2 =  helper.getView(R.id.ll_product_2);
        if(info1 != null) {
            l1.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams lp1 = (LinearLayout.LayoutParams) helper.getView(R.id.iv_shop_product_bg_1).getLayoutParams();
            lp1.width = imageWidth;
            lp1.height = imageWidth;
            helper.getView(R.id.iv_shop_product_bg_1).setLayoutParams(lp1);

            helper.setImageUrl(R.id.iv_shop_product_bg_1, ImageUtils.getImageFullUrl(info1.mainPicUrl), imageWidth,
                    imageWidth, R.mipmap.icon_default_215_215);
            helper.setText(R.id.tv_shop_product_price_1, StringUtil.converRMb2YunStrNoDot(info1.price));
            helper.setText(R.id.tv_shop_product_name_1, info1.title);
            helper.setText(R.id.tv_shop_product_sales_1, StringUtil.formatSales(context,info1.stockNum));
            helper.setOnClickListener(R.id.ll_product_1, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavUtils.gotoProductDetail(context,info1.outType,info1.id,info1.title);
                }
            });
        }else{
            l1.setVisibility(View.INVISIBLE);
        }
        if(info2 != null) {
            l2.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams lp2 = (LinearLayout.LayoutParams) helper.getView(R.id.iv_shop_product_bg_2).getLayoutParams();
            lp2.width = imageWidth;
            lp2.height = imageWidth;
            helper.getView(R.id.iv_shop_product_bg_2).setLayoutParams(lp2);

            helper.setImageUrl(R.id.iv_shop_product_bg_2, ImageUtils.getImageFullUrl(info2.mainPicUrl), imageWidth
                    , imageWidth, R.mipmap.icon_default_215_215);
            helper.setText(R.id.tv_shop_product_price_2, StringUtil.converRMb2YunStrNoDot(info2.price));
            helper.setText(R.id.tv_shop_product_name_2, info2.title);
            helper.setText(R.id.tv_shop_product_sales_2, StringUtil.formatSales(context,info2.stockNum));
            helper.setOnClickListener(R.id.ll_product_2, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavUtils.gotoProductDetail(context,info2.outType,info2.id,info2.title);
                }
            });
        }else{
            l2.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 处理店铺商品列表页展示
     * @param activity
     * @param datas
     */
    public static QuickAdapter<ShortItem> doSetShopProductVerListAdapter(final Activity activity,
                                                 List<ShortItem> datas) {
        QuickAdapter<ShortItem> adapter = null;
        adapter = new QuickAdapter<ShortItem>(activity, R.layout.item_shop_product_ver, datas) {
            @Override
            protected void convert(BaseAdapterHelper helper, ShortItem item) {
               MasterViewHelper.handleShopProductsItem(activity,helper,item,"");
            }
        };
        return adapter;
    }
    /**
     * 处理店铺商品列表页展示
     * @param context
     * @param datas
     */
    public static QuickAdapter<ShopProductHorInfo> doSetShopProductHorListAdapter(final Context context,
                                                                                  List<ShortItem> datas) {
        QuickAdapter<ShopProductHorInfo> adapter = null;
        adapter = new QuickAdapter<ShopProductHorInfo>(context, R.layout.item_shop_product_hor, transferItemsInfoList(datas)) {
            @Override
            protected void convert(BaseAdapterHelper helper, ShopProductHorInfo item) {
                handleShopProductHorListItem(context, helper, item);
            }
        };
        return adapter;
    }

    /**
     * 转化两列商品展示的对象
     * @param orgList
     * @return
     */
    public static List<ShopProductHorInfo> transferItemsInfoList(List<ShortItem> orgList){
        List<ShopProductHorInfo> lastList = new ArrayList<>();
        if(orgList == null || orgList.size() == 0){
            return lastList;
        }
        for(int j = 0; j < orgList.size() ; j = j + 2){
            ShopProductHorInfo info = new ShopProductHorInfo();
            info.info1 = orgList.get(j);
            if((j+1) < orgList.size()){
                info.info2 = orgList.get(j+1);
            }
            lastList.add(info);
        }
        return lastList;
    }

    /**
     * 处理优惠券
     * @param context
     * @param helper
     * @param item
     */
    public static void handleCouponListItem(final Context context,
                                            BaseAdapterHelper helper,
                                            VoucherTemplateResult item) {
        if(CouponStatus.ACTIVE.equals(item.status)){
            helper.setVisible(R.id.iv_coupon_status,false);
            helper.getView().setBackgroundResource(R.mipmap.ic_shop_coupon_normal);
        }else{
            helper.setVisible(R.id.iv_coupon_status,true);
            helper.getView().setBackgroundResource(R.mipmap.ic_shop_coupon_disable);
        }
        if(item.value >= 100){
            helper.setText(R.id.tv_price_ints,String.valueOf(item.value / 100));
            helper.setText(R.id.tv_price_decimal,StringUtil.formatCouponPrice(item.value % 100) + "元");
        }else{
            helper.setText(R.id.tv_price_ints,"0");
            helper.setText(R.id.tv_price_decimal,StringUtil.formatCouponPrice(item.value % 100) + "元");
        }

        if(item.requirement > 0){
            helper.setText(R.id.tv_available_condition,
                    String.format(context.getResources().getString(R.string.label_coupon_reqirement), StringUtil.convertPriceNoSymbolExceptLastZero(item.requirement)));
        }
    }

    public static void handleCounponListView(final Context context, View view, VoucherTemplateResult item){
        if(CouponStatus.ACTIVE.equals(item.status)){
            view.findViewById(R.id.iv_coupon_status).setVisibility(View.GONE);
            view.setBackgroundResource(R.mipmap.ic_shop_coupon_normal);
        }else{
            view.findViewById(R.id.iv_coupon_status).setVisibility(View.VISIBLE);
            view.setBackgroundResource(R.mipmap.ic_shop_coupon_disable);
        }
        if(item.value >= 100){
            ((TextView)view.findViewById(R.id.tv_price_ints)).setText(String.valueOf(item.value / 100));
            ((TextView)view.findViewById(R.id.tv_price_decimal)).setText(StringUtil.formatCouponPrice(item.value % 100) + "元");
        }else{
            ((TextView)view.findViewById(R.id.tv_price_ints)).setText("0");
            ((TextView)view.findViewById(R.id.tv_price_decimal)).setText(StringUtil.formatCouponPrice(item.value % 100) + "元");
        }

        if(item.requirement > 0){
            ((TextView)view.findViewById(R.id.tv_available_condition)).setText(
                    String.format(context.getResources().getString(R.string.label_coupon_reqirement), StringUtil.convertPriceNoSymbolExceptLastZero(item.requirement)));
        }
    }

    /**
     * 处理物流信息
     * @param context
     * @param helper
     * @param item
     */
    public static void handleLogisticsItem(final Context context, BaseAdapterHelper helper, LgExpressLineInfo item){
        int pos = helper.getPosition();
        if(pos == 0){
            helper.setTextColor(R.id.tvDeliveryStatus, context.getResources().getColor(R.color.neu_fb9b00));
            helper.setTextColor(R.id.tvTime, context.getResources().getColor(R.color.neu_fb9b00));
            helper.getView(R.id.v_line_top).setVisibility(View.INVISIBLE);
//            helper.setBackgroundColor(R.id.v_line_top,context.getResources().getColor(R.color.neu_fb9b00));
//            helper.setBackgroundColor(R.id.v_line_bottom,context.getResources().getColor(R.color.neu_fb9b00));
            helper.setImageResource(R.id.iv_line_dot, R.mipmap.ic_lg_order_current_state);
        }else{
            helper.setTextColor(R.id.tvDeliveryStatus, context.getResources().getColor(R.color.neu_999999));
            helper.setTextColor(R.id.tvTime, context.getResources().getColor(R.color.neu_999999));
            helper.getView(R.id.v_line_top).setVisibility(View.VISIBLE);
//            helper.setBackgroundColor(R.id.v_line_top,context.getResources().getColor(R.color.neu_999999));
//            helper.setBackgroundColor(R.id.v_line_bottom,context.getResources().getColor(R.color.neu_999999));
            helper.setImageResource(R.id.iv_line_dot, R.mipmap.ic_lg_order_past_state);
        }

        if (!StringUtil.isEmpty(item.context)) {
            helper.setText(R.id.tvDeliveryStatus, item.context);
        } else {
            helper.setText(R.id.tvDeliveryStatus, "");
        }

        if (!StringUtil.isEmpty(item.time)) {
            helper.setText(R.id.tvTime, item.time);
        } else {
            helper.setText(R.id.tvTime, "");
        }
    }

    /**
     * 处理物流信息
     * @param context
     * @param helper
     * @param item
     */
    public static void handleLogisticsItem(final Context context, BaseAdapterHelper helper, ExpressDetailInfo item){
        int pos = helper.getPosition();
        if(pos == 0){
            helper.setTextColor(R.id.tvDeliveryStatus, context.getResources().getColor(R.color.neu_fb9b00));
            helper.setTextColor(R.id.tvTime, context.getResources().getColor(R.color.neu_fb9b00));
            helper.getView(R.id.v_line_top).setVisibility(View.INVISIBLE);
//            helper.setBackgroundColor(R.id.v_line_top,context.getResources().getColor(R.color.neu_fb9b00));
//            helper.setBackgroundColor(R.id.v_line_bottom,context.getResources().getColor(R.color.neu_fb9b00));
            helper.setImageResource(R.id.iv_line_dot,R.mipmap.ic_lg_order_current_state);
        }else{
            helper.setTextColor(R.id.tvDeliveryStatus, context.getResources().getColor(R.color.neu_999999));
            helper.setTextColor(R.id.tvTime, context.getResources().getColor(R.color.neu_999999));
            helper.getView(R.id.v_line_top).setVisibility(View.VISIBLE);
//            helper.setBackgroundColor(R.id.v_line_top,context.getResources().getColor(R.color.neu_999999));
//            helper.setBackgroundColor(R.id.v_line_bottom,context.getResources().getColor(R.color.neu_999999));
            helper.setImageResource(R.id.iv_line_dot,R.mipmap.ic_lg_order_past_state);
        }

        if (!StringUtil.isEmpty(item.context)) {
            helper.setText(R.id.tvDeliveryStatus, item.context);
        } else {
            helper.setText(R.id.tvDeliveryStatus, "");
        }

        if (!StringUtil.isEmpty(item.time)) {
            helper.setText(R.id.tvTime, item.time);
        } else {
            helper.setText(R.id.tvTime, "");
        }
    }
}
