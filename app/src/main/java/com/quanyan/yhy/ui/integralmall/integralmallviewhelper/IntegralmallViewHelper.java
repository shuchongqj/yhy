package com.quanyan.yhy.ui.integralmall.integralmallviewhelper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.ItemType;
import com.quanyan.yhy.ui.adapter.base.BaseAdapterHelper;
import com.quanyan.yhy.ui.adapter.base.QuickAdapter;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.videolibrary.itemhandle.LiveListItemHelper;
import com.yhy.common.beans.net.model.master.MerchantInfo;
import com.yhy.common.beans.net.model.trip.ShortItem;
import com.yhy.common.utils.SPUtils;
import com.yhy.router.YhyRouter;
import com.yhy.service.IUserService;

import java.util.List;

/**
 * Created with Android Studio.
 * Title:AboutAndFeedController
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:wjm
 * Date:16/621
 * Version 1.3.0
 */
public class IntegralmallViewHelper {
    private static IntegralmallViewHelper instance = new IntegralmallViewHelper();
    @Autowired
    IUserService userService;

    private IntegralmallViewHelper() {
        YhyRouter.getInstance().inject(this);
    }

    public static IntegralmallViewHelper getInstance(){
        return instance;
    }
    /***
     * 兑奖记录
     *
     * @param context
     * @param datas
     * @return
     */
    public static QuickAdapter<MerchantInfo> setAdapter(Context context, List<MerchantInfo> datas) {
        QuickAdapter<MerchantInfo> adapter = new QuickAdapter<MerchantInfo>(context, R.layout.item_awarding_records, datas) {
            @Override
            protected void convert(BaseAdapterHelper helper, MerchantInfo item) {
//                helper.setImageUrl(R.id.iv_eat_product_bg, item.icon, 500, 240, R.mipmap.icon_default_215_150);
//                helper.setText(R.id.tv_eat_product_name, item.name.trim());
//                //helper.setText(R.id.tv_eat_product_price, String.format(context.getString(R.string.eat_label_price), String.valueOf(item.avgprice/100)));
//                helper.setText(R.id.tv_eat_product_address, item.city);
//                helper.setText(R.id.tv_eat_product_price, String.format(context.getString(R.string.eat_label_price), StringUtil.converRMb2YunNoFlag(item.avgprice)));
            }
        };
        return adapter;
    }

    /**
     * 绑定景区推荐列表的视图
     *
     * @param context
     * @param helper
     * @param item
     */
    public static void handleMasterAdviceListItem(final Activity context, BaseAdapterHelper helper, final ShortItem item) {
        helper.setFlags(R.id.tv_master_consult_service_dis_unit, Paint.STRIKE_THRU_TEXT_FLAG);
        helper.setImageUrl(R.id.image_consult, item.mainPicUrl, (int)(346 * 1.5f), (int)(264 * 1.5f), R.mipmap.icon_default_215_150);
        helper.setText(R.id.tv_master_consult_service_title, item.title);//资深云南旅游达人咨询服务
        helper.setText(R.id.tv_master_consult_service_adress, TextUtils.isEmpty(item.destinations) ? "无" : item.destinations);//服务地区：云南、四川、广西

        helper.setText(R.id.tv_master_consult_integral, item.price/10 + "");//0

        if (item.price == 0) {//是否限时免费
            helper.setVisible(R.id.image_free, true);
        } else {
            helper.setVisible(R.id.image_free, false);
        }
        helper.setText(R.id.tv_master_consult_time, "积分/" + item.consultTime / 60 + "分钟");
        helper.setText(R.id.tv_master_consult_service_dis_unit, item.originalPrice/10 + "积分");//1200积分
        //达人是否在线
        if (!TextUtils.isEmpty(item.onlineStatus)) {
            if (item.onlineStatus.equals("ONLINE")) {
                helper.setTextColor(R.id.btn_start_master_consult_service, context.getResources().getColor(R.color.white));
                helper.setBackgroundRes(R.id.btn_start_master_consult_service, R.drawable.btn_orange_selector);
                helper.setText(R.id.btn_start_master_consult_service,context.getString(R.string.consult_now));
            } else if (item.onlineStatus.equals("NOTONLINE")) {
                helper.setBackgroundRes(R.id.btn_start_master_consult_service, R.drawable.shape_tv_integral_rob_no_date);
                helper.setTextColor(R.id.btn_start_master_consult_service, context.getResources().getColor(R.color.white));
                helper.setText(R.id.btn_start_master_consult_service,context.getString(R.string.label_btn_offline));
            }
        } else {
            helper.setBackgroundRes(R.id.btn_start_master_consult_service, R.drawable.shape_tv_integral_rob_no_date);
            helper.setTextColor(R.id.btn_start_master_consult_service, context.getResources().getColor(R.color.white));
        }

        helper.setOnClickListener(R.id.btn_start_master_consult_service, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(item.onlineStatus)) {
                    if (item.onlineStatus.equals("ONLINE")) {
                        NavUtils.gotoMasterConsultActivity(context, item.id,item.price,item.consultTime);
                    } else if (item.onlineStatus.equals("NOTONLINE")) {
//                        DialogUtil.showMessageDialog(context, null, context.getString(R.string.dialog_advice_no_fromsale), context.getString(R.string.dialog_advice_bt), null, null, null, null, null).show();
                    }
                } else {
//                    DialogUtil.showMessageDialog(context, null, context.getString(R.string.dialog_advice_no_fromsale), context.getString(R.string.dialog_advice_bt), null, null, null, null, null).show();
                }
            }
        });
        if(item.userInfo!=null&& item.userInfo.id!=0){
            if (instance.userService.getLoginUserId() == item.userInfo.id) {
//                helper.setVisible(R.id.btn_start_master_consult_service, false);
                helper.setVisibleOrInVisible(R.id.btn_start_master_consult_service,false);
            } else {
                helper.setVisibleOrInVisible(R.id.btn_start_master_consult_service,true);
//                helper.setVisible(R.id.btn_start_master_consult_service, true);
            }
        }

    }

    /**
     * 绑定景区推荐列表的视图
     *
     * @param context
     * @param helper
     * @param item
     */
    public static void handleIntegralmallListItem(final Activity context, BaseAdapterHelper helper, final ShortItem item) {
        long discountPrice = 0;
        if(item.payInfo != null){
            discountPrice = item.payInfo.maxPoint;
        }

        if (item.stockNum == 0) {
            helper.setVisible(R.id.iv_integralmall_logo_soldout, true);
            helper.setImageUrl(R.id.iv_integralmall_logo_pic, item.mainPicUrl, 0, 0, R.mipmap.icon_default_215_150);
            helper.setText(R.id.iv_integralmall_title, item.title.trim());
//            helper.setText(R.id.iv_integralmall_pic, StringUtil.pointToYuan(item.price/10 - (SPUtils.getScore(context) > discountPrice ? discountPrice : SPUtils.getScore(context))));
//            helper.setText(R.id.iv_integralmall_pic, StringUtil.pointToYuan(item.price));
            helper.setText(R.id.iv_integralmall_pic, item.maxPrice > item.price ? StringUtil.pointToYuan(item.price) + "起" : StringUtil.pointToYuan(item.price));
            if (item.originalPrice > 0) {
//                findViewById(R.id.commodity_integralmal_market_pirce_layout).setVisibility(View.VISIBLE);
                helper.setVisible(R.id.commodity_integralmal_market_pirce, true);

                //积分商城的原价
                TextView integralmalPrice = helper.getView(R.id.commodity_integralmal_market_pirce);
                integralmalPrice.setText(context.getString(R.string.money_symbol)
                        + StringUtil.convertPriceNoSymbolExceptLastZero(item.originalPrice));
                integralmalPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            }else {
                helper.setVisible(R.id.commodity_integralmal_market_pirce, false);

            }
            if (item.payInfo != null) {
                // 积分抵扣
//                helper.setText(R.id.iv_integralmall_deductible_pic, StringUtil.convertScoreToDiscount(context, item.payInfo.maxPoint));
//                helper.setText(R.id.iv_integralmall_deductible_pic, StringUtil.pointToYuanOne(item.payInfo.maxPoint*10));
                helper.setText(R.id.iv_integralmall_deductible_pic, item.payInfo.maxPoint > item.payInfo.minPoint ? StringUtil.pointToYuanOne(item.payInfo.minPoint*10) + "-" + StringUtil.pointToYuanOne(item.payInfo.maxPoint*10) :
                        StringUtil.pointToYuanOne(item.payInfo.minPoint*10));
            }

            helper.setTextColor(R.id.iv_integralmall_piclabel, context.getResources().getColor(R.color.tv_color_gray9));
            helper.setTextColor(R.id.iv_integralmall_pic, context.getResources().getColor(R.color.tv_color_gray9));
            helper.setTextColor(R.id.iv_integralmall_deductible, context.getResources().getColor(R.color.tv_color_gray9));
            helper.setTextColor(R.id.iv_integralmall_piclabeltwo, context.getResources().getColor(R.color.tv_color_gray9));
            helper.setTextColor(R.id.iv_integralmall_deductible_pic, context.getResources().getColor(R.color.tv_color_gray9));
            helper.setBackgroundRes(R.id.tv_integralmall_rob, R.drawable.shape_tv_integral_rob_no_date);
            helper.setText(R.id.tv_integralmall_rob,context.getString(R.string.label_sales_status_sold_out) );
        } else {
            helper.setVisible(R.id.iv_integralmall_logo_soldout, false);
            helper.setText(R.id.tv_integralmall_rob, context.getString(R.string.label_sales_status_buy) );
            helper.setImageUrl(R.id.iv_integralmall_logo_pic, item.mainPicUrl, 0, 0, R.mipmap.icon_default_215_150);
            helper.setText(R.id.iv_integralmall_title, item.title.trim());
//            helper.setText(R.id.iv_integralmall_pic, StringUtil.pointToYuan(item.price/10 - (SPUtils.getScore(context) > discountPrice ? discountPrice : SPUtils.getScore(context))));
//            helper.setText(R.id.iv_integralmall_pic, StringUtil.pointToYuan(item.price));
            helper.setText(R.id.iv_integralmall_pic, item.maxPrice > item.price ? StringUtil.pointToYuan(item.price) + "起" : StringUtil.pointToYuan(item.price));

            if (item.originalPrice > 0) {
//                findViewById(R.id.commodity_integralmal_market_pirce_layout).setVisibility(View.VISIBLE);
                helper.setVisible(R.id.commodity_integralmal_market_pirce, true);

                //积分商城的原价
                TextView integralmalPrice = helper.getView(R.id.commodity_integralmal_market_pirce);
                integralmalPrice.setText(context.getString(R.string.money_symbol)
                        + StringUtil.convertPriceNoSymbolExceptLastZero(item.originalPrice));
                integralmalPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            }else {
                helper.setVisible(R.id.commodity_integralmal_market_pirce, false);

            }
            if (item.payInfo != null) {
                // 积分抵扣
//                helper.setText(R.id.iv_integralmall_deductible_pic, StringUtil.convertScoreToDiscount(context, item.payInfo.maxPoint));
//                helper.setText(R.id.iv_integralmall_deductible_pic, StringUtil.pointToYuanOne(item.payInfo.maxPoint*10));
                helper.setText(R.id.iv_integralmall_deductible_pic, item.payInfo.maxPoint > item.payInfo.minPoint ? StringUtil.pointToYuanOne(item.payInfo.minPoint*10) + "-" + StringUtil.pointToYuanOne(item.payInfo.maxPoint*10) :
                        StringUtil.pointToYuanOne(item.payInfo.minPoint*10));

            }
//            helper.setText(R.id.iv_integralmall_pic, StringUtil.convertPriceNoSymbolExceptLastZero(item.price - discountPrice));
//            if (item.payInfo != null) {
//                // 积分抵扣
//                helper.setText(R.id.iv_integralmall_deductible_pic, StringUtil.convertScoreToDiscount(context, item.payInfo.maxPoint));
//            }
            helper.setTextColor(R.id.iv_integralmall_pic, context.getResources().getColor(R.color.main));
            helper.setTextColor(R.id.iv_integralmall_deductible, context.getResources().getColor(R.color.main));
            helper.setTextColor(R.id.iv_integralmall_deductible_pic, context.getResources().getColor(R.color.main));
            helper.setTextColor(R.id.iv_integralmall_piclabeltwo, context.getResources().getColor(R.color.main));
            helper.setTextColor(R.id.iv_integralmall_piclabel, context.getResources().getColor(R.color.main));
            helper.setBackgroundRes(R.id.tv_integralmall_rob, R.drawable.shape_tv_integral_rob_main);
        }
        //已售
        helper.setText(R.id.iv_integralmall_sale, StringUtil.formatSales(context, item.sales));
        helper.setOnClickListener(R.id.tv_integralmall_rob, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavUtils.gotoProductDetail(context,
                        ItemType.POINT_MALL,
                        item.id,
                        item.title);
            }
        });


    }
}
