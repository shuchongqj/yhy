package com.quanyan.yhy.ui.spcart.helper;

import android.text.TextUtils;
import android.widget.ImageView;

import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.R;
import com.quanyan.yhy.ui.adapter.base.BaseAdapterHelper;
import com.quanyan.yhy.util.TimeUtil;
import com.yhy.common.beans.net.model.tm.VoucherTemplateResult;
import com.yhy.common.utils.CommonUtil;
import com.yhy.imageloader.ImageLoadManager;

/**
 * Created with Android Studio.
 * Title:SpCartHelper
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-10-19
 * Time:15:19
 * Version 1.0
 * Description:
 */
public class SpCartHelper {

    public static void handleSpCartCouponView(final BaseAdapterHelper helper, VoucherTemplateResult itemData) {
        if (itemData == null) {
            return;
        }
        ImageView imageView = helper.getView(R.id.iv_logo);
        if (itemData.sellerResult != null && !TextUtils.isEmpty(itemData.sellerResult.logo)) {
            ImageLoadManager.loadCircleImage(CommonUtil.getImageFullUrl(itemData.sellerResult.logo), R.mipmap.ic_shop_default_logo, 75, 75, imageView);

        } else {
            imageView.setImageResource(R.mipmap.ic_shop_default_logo);
        }
        if (!TextUtils.isEmpty(itemData.title)) {
            helper.setText(R.id.tv_title, itemData.title);
        } else {
            helper.setText(R.id.tv_title, "");
        }
        if (itemData.sellerResult != null && !TextUtils.isEmpty(itemData.sellerResult.merchantName)) {
            helper.setText(R.id.tv_merchant_name, "" + itemData.sellerResult.merchantName);
        } else {
            helper.setText(R.id.tv_merchant_name, "");
        }
        helper.setText(R.id.tv_value, "" + StringUtil.converRMb2YunNoFlag(itemData.value));
        helper.setText(R.id.tv_use_requirement, "满" + StringUtil.converRMb2YunNoFlag(itemData.requirement) + "元使用");
        if (itemData.startTime != 0 && itemData.endTime != 0) {
            helper.setText(R.id.tv_validate_date, TimeUtil.convertLong2String(itemData.startTime, "yyyy-MM-dd") + " 至 " + TimeUtil.convertLong2String(itemData.endTime, "yyyy-MM-dd"));
        }
//        if (!CouponStatus.ACTIVE.equals(itemData.status)) {
//            helper.setTextColor(R.id.tv_value, R.color.coupon_company_name);
//            helper.setBackgroundRes(R.id.lin_item_bg, R.drawable.bg_coupon_item_grey);
//            helper.setBackgroundRes(R.id.iv_split_line,R.drawable.item_line_check_grey);
//        } else {
//            helper.setTextColor(R.id.tv_value, R.color.tv_item_bottom_bg);
//            helper.setBackgroundRes(R.id.lin_item_bg, R.drawable.bg_coupon_item_white);
//            helper.setBackgroundRes(R.id.iv_split_line,R.drawable.item_line_check_orange);
//        }
    }

}
