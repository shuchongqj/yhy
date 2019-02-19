package com.quanyan.yhy.ui.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.newyhy.utils.DisplayUtils;
import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.R;
import com.yhy.common.beans.net.model.trip.ShortItem;
import com.yhy.common.utils.CommonUtil;
import com.yhy.imageloader.ImageLoadManager;

import java.util.List;

/**
 * Created by yangboxue on 2018/4/19.
 */

public class MallAdapter extends BaseQuickAdapter<ShortItem, BaseViewHolder> {
    private Activity context;
    private int imgWidth;

    public MallAdapter(Activity context, int layoutResId, @Nullable List<ShortItem> data) {
        super(layoutResId, data);
        this.context = context;
        imgWidth = (DisplayUtils.getScreenWidth(context) - DisplayUtils.dp2px(context, 41)) / 2;
    }

    @Override
    protected void convert(BaseViewHolder helper, final ShortItem item) {
        ImageView ivGoods = helper.getView(R.id.iv_integralmall_logo_pic);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) ivGoods.getLayoutParams();
        lp.height = imgWidth;
        ivGoods.setLayoutParams(lp);
        if (item.stockNum == 0) {
            helper.setVisible(R.id.iv_integralmall_logo_soldout, true);
            helper.setVisible(R.id.iv_integralmall_logo_hot, false);
            setImageUrl(ivGoods, item.mainPicUrl, 0, 0, R.mipmap.icon_default_215_150);
//            helper.setText(R.id.iv_integralmall_title, item.title.trim());
//            helper.setText(R.id.iv_integralmall_pic, StringUtil.pointToYuan(item.price/10 - (SPUtils.getScore(context) > discountPrice ? discountPrice : SPUtils.getScore(context))));
//            helper.setText(R.id.iv_integralmall_pic, StringUtil.pointToYuan(item.price));
            helper.setText(R.id.iv_integralmall_pic, StringUtil.convertPriceNoSymbolExceptLastZero(item.price));
//            helper.setText(R.id.iv_integralmall_pic, StringUtil.pointToYuan(item.price));
//            helper.setVisible(R.id.tv_qi, item.maxPrice > item.price ? true : false);
            helper.getView(R.id.tv_qi).setVisibility(item.maxPrice > item.price ? View.VISIBLE : View.GONE);

            if (item.originalPrice > 0) {
//                findViewById(R.id.commodity_integralmal_market_pirce_layout).setVisibility(View.VISIBLE);
                helper.setVisible(R.id.commodity_integralmal_market_pirce, true);

                //积分商城的原价
                TextView integralmalPrice = helper.getView(R.id.commodity_integralmal_market_pirce);
                integralmalPrice.setText(context.getString(R.string.money_symbol)
                        + StringUtil.convertPriceNoSymbolExceptLastZero(item.originalPrice));
                integralmalPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                helper.setVisible(R.id.commodity_integralmal_market_pirce, false);

            }
            if (item.payInfo != null) {
                // 积分抵扣
//                helper.setText(R.id.iv_integralmall_deductible_pic, StringUtil.convertScoreToDiscount(context, item.payInfo.maxPoint));
//                helper.setText(R.id.iv_integralmall_deductible_pic, StringUtil.pointToYuanOne(item.payInfo.maxPoint*10));
                if (item.payInfo.maxPoint > item.payInfo.minPoint) {
                    helper.setVisible(R.id.layout_integralmall_deduction, true);
                } else {
                    if (item.payInfo.maxPoint > 0) {
                        helper.setVisible(R.id.layout_integralmall_deduction, true);
                    } else {
                        helper.setVisible(R.id.layout_integralmall_deduction, false);
                    }

                }
                helper.setText(R.id.iv_integralmall_deductible_pic, item.payInfo.maxPoint > item.payInfo.minPoint ? StringUtil.pointToYuanOne(item.payInfo.minPoint * 10) + "-" + StringUtil.pointToYuanOne(item.payInfo.maxPoint * 10) :
                        StringUtil.pointToYuanOne(item.payInfo.minPoint * 10));
            }

            helper.setTextColor(R.id.iv_integralmall_piclabel, context.getResources().getColor(R.color.tv_color_gray9));
            helper.setTextColor(R.id.iv_integralmall_pic, context.getResources().getColor(R.color.tv_color_gray9));
            helper.setTextColor(R.id.iv_integralmall_deductible, context.getResources().getColor(R.color.tv_color_gray9));
            helper.setTextColor(R.id.iv_integralmall_piclabeltwo, context.getResources().getColor(R.color.tv_color_gray9));
            helper.setTextColor(R.id.iv_integralmall_deductible_pic, context.getResources().getColor(R.color.tv_color_gray9));
            helper.setBackgroundRes(R.id.tv_integralmall_rob, R.drawable.shape_tv_integral_rob_no_date);
            helper.setText(R.id.tv_integralmall_rob, context.getString(R.string.label_sales_status_sold_out));
        } else {
            helper.setVisible(R.id.iv_integralmall_logo_soldout, false);
            // 添加热卖标记
            helper.setVisible(R.id.iv_integralmall_logo_hot, item.hotSales ? true : false);

            helper.setText(R.id.tv_integralmall_rob, context.getString(R.string.label_sales_status_buy));
            setImageUrl(ivGoods, item.mainPicUrl, 0, 0, R.mipmap.icon_default_215_150);

//            helper.setText(R.id.iv_integralmall_pic, StringUtil.pointToYuan(item.price/10 - (SPUtils.getScore(context) > discountPrice ? discountPrice : SPUtils.getScore(context))));
//            helper.setText(R.id.iv_integralmall_pic, StringUtil.pointToYuan(item.price));
//            helper.setText(R.id.iv_integralmall_pic, StringUtil.pointToYuan(item.price));
            helper.setText(R.id.iv_integralmall_pic, StringUtil.convertPriceNoSymbolExceptLastZero(item.price));
            helper.getView(R.id.tv_qi).setVisibility(item.maxPrice > item.price ? View.VISIBLE : View.GONE);

            if (item.originalPrice > 0) {
//                findViewById(R.id.commodity_integralmal_market_pirce_layout).setVisibility(View.VISIBLE);
                helper.setVisible(R.id.commodity_integralmal_market_pirce, true);

                //积分商城的原价
                TextView integralmalPrice = helper.getView(R.id.commodity_integralmal_market_pirce);
                integralmalPrice.setText(context.getString(R.string.money_symbol)
                        + StringUtil.convertPriceNoSymbolExceptLastZero(item.originalPrice));
                integralmalPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                helper.setVisible(R.id.commodity_integralmal_market_pirce, false);

            }
            if (item.payInfo != null) {
                // 积分抵扣
//                helper.setText(R.id.iv_integralmall_deductible_pic, StringUtil.convertScoreToDiscount(context, item.payInfo.maxPoint));
//                helper.setText(R.id.iv_integralmall_deductible_pic, StringUtil.pointToYuanOne(item.payInfo.maxPoint*10));
                if (item.payInfo.maxPoint > item.payInfo.minPoint) {
                    helper.setVisible(R.id.layout_integralmall_deduction, true);
                } else {
                    if (item.payInfo.maxPoint > 0) {
                        helper.setVisible(R.id.layout_integralmall_deduction, true);
                    } else {
                        helper.setVisible(R.id.layout_integralmall_deduction, false);
                    }

                }
                helper.setText(R.id.iv_integralmall_deductible_pic, item.payInfo.maxPoint > item.payInfo.minPoint ? StringUtil.pointToYuanOne(item.payInfo.minPoint * 10) + "-" + StringUtil.pointToYuanOne(item.payInfo.maxPoint * 10) :
                        StringUtil.pointToYuanOne(item.payInfo.minPoint * 10));

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

        // 标题
        if ("GLOBAL_MALL".equals(item.itemType)) {
            SpannableStringBuilder span = new SpannableStringBuilder(" 全球购 " + item.title.trim());
            span.setSpan(new ForegroundColorSpan(Color.TRANSPARENT), 1, 5,
                    Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            helper.setText(R.id.iv_integralmall_title, span);
            helper.setVisible(R.id.tv_global, true);

        } else {
            helper.setVisible(R.id.tv_global, false);
            helper.setText(R.id.iv_integralmall_title, item.title.trim());

        }

        //已售
        helper.setText(R.id.iv_integralmall_sale, StringUtil.formatSales(context, item.sales));
        helper.setVisible(R.id.iv_integralmall_sale, false);
//        helper.setOnClickListener(R.id.tv_integralmall_rob, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                NavUtils.gotoProductDetail(context,
//                        ItemType.POINT_MALL,
//                        item.id,
//                        item.title);
//            }
//        });

    }

    private void setImageUrl(ImageView view, String imageUrl, int width, int height, int defaultPic) {
//        ImageView view = retrieveView(viewId);
        if (!TextUtils.isEmpty(imageUrl)) {

//            BaseImgView.loadimg(view,
//                    imageUrl,
//                    defaultPic,
//                    defaultPic,
//                    defaultPic,
//                    ImageScaleType.EXACTLY,
//                    width,
//                    height,
//                    0);
//            Glide.with(context).load(imageUrl).into(view);
            ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(imageUrl), defaultPic, width, height, view);
        } else {
            view.setImageResource(defaultPic);
        }
    }

}
