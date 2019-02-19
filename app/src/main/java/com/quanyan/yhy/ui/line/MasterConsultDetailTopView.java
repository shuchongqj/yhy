package com.quanyan.yhy.ui.line;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Paint;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.quanyan.base.view.customview.imgpager.ImgPagerView;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.AnalyDataValue;
import com.quanyan.yhy.ui.base.utils.TCEventHelper;
import com.quanyan.yhy.view.CommodityBottomView;
import com.yhy.common.beans.net.model.shop.MerchantItem;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.CommonUtil;
import com.yhy.common.utils.SPUtils;
import com.yhy.imageloader.ImageLoadManager;
import com.yhy.router.YhyRouter;
import com.yhy.service.IUserService;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with Android Studio.
 * Title:MasterConsultDetailTopView
 * Description: 达人咨询头部布局
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:赵晓坡
 * Date:16/7/20
 * Time:13:54
 * Version 1.1.0
 */
public class MasterConsultDetailTopView extends LinearLayout {

    private CommodityBottomView.ExchangeData mExchangeData;

    @Autowired
    IUserService userService;

    private ImgPagerView mImgPagerView;
    public MasterConsultDetailTopView(Context context) {
        super(context);
        initView(context);
    }

    public MasterConsultDetailTopView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public MasterConsultDetailTopView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MasterConsultDetailTopView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    public void setExchangeData(CommodityBottomView.ExchangeData exchangeData) {
        mExchangeData = exchangeData;
    }

    private void initView(Context context) {
        YhyRouter.getInstance().inject(this);
        View.inflate(context, R.layout.view_master_consult_detail, this);
        mImgPagerView = (ImgPagerView) findViewById(R.id.master_consult_detail_pager_view);
        mImgPagerView.setScale(context, (float) ValueConstants.SCALE_PRODUCT_HEADER_IMG);
        /**
         * 商铺头像的点击事件
         */
//        findViewById(R.id.master_consult_detail_merchant_layout).setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mExchangeData == null) {
//                    throw new NullPointerException("please use #setExchangeData(ExchangeData)# to initialize" +
//                            " the params and implement the interface #ExchangeData#");
//                }
//                Object object = mExchangeData.getData();
//                if (object != null) {
//                    if (object instanceof MerchantItem) {
//                        MerchantItem merchantItem = (MerchantItem) object;
//                        if (merchantItem.userInfo == null) {
//                            ToastUtil.showToast(getContext(), "数据错误");
//                            return;
//                        }
//                        long merchantId = ((MerchantItem) object).userInfo.userId == 0 ? ((MerchantItem) object).userInfo.id : ((MerchantItem) object).userInfo.userId;
//                        if(merchantItem.userInfo != null) {
//                            NavUtils.gotoPersonalPage(getContext(), merchantId, merchantItem.userInfo.nickname, merchantItem.userInfo.options);
//                        }
//                    }
//                }
//            }
//        });
    }

    public void bindViewData(MerchantItem merchantItem) {
        if (merchantItem.itemVO != null) {
            if (merchantItem.itemVO.picUrls != null && !merchantItem.itemVO.picUrls.isEmpty()) {
                mImgPagerView.setImgs(merchantItem.itemVO.picUrls);
            }

            ((TextView) findViewById(R.id.master_consult_detail_title)).setText(
                    TextUtils.isEmpty(merchantItem.itemVO.title) ? "" : merchantItem.itemVO.title
            );
            if (merchantItem.itemVO.marketPrice == 0) {
                // TODO: 16/7/29 限免
                ((TextView) findViewById(R.id.master_consult_detail_points)).setText("限免");
                TextView textView = (TextView) findViewById(R.id.master_consult_detail_points_sub);
                textView.setText(String.format(getContext().getString(R.string.label_points_format), merchantItem.itemVO.originalPrice/10)
                        + "/" + String.format(getContext().getString(R.string.label_time_minutes_format), merchantItem.itemVO.consultTime / 60));
                textView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                TextView textView = (TextView) findViewById(R.id.master_consult_detail_points_original);
                textView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                textView.setText(String.format(getContext().getString(R.string.label_consult_original_price_format), merchantItem.itemVO.originalPrice/10));
                ((TextView) findViewById(R.id.master_consult_detail_points)).setText(String.format(getContext().getString(R.string.label_points_format), merchantItem.itemVO.marketPrice/10));
                ((TextView) findViewById(R.id.master_consult_detail_points_sub)).setText("/"+String.format(getContext().getString(R.string.label_time_minutes_format), merchantItem.itemVO.consultTime / 60));
            }

            ((TextView)findViewById(R.id.master_consult_detail_serve_area)).setText(String.format(getContext().getString(R.string.label_consulting_service_area),
                    TextUtils.isEmpty(merchantItem.itemVO.destinations) ? "无" : merchantItem.itemVO.destinations));

            if (merchantItem.userInfo != null && userService.getLoginUserId() == (merchantItem.userInfo.userId == 0?merchantItem.userInfo.id : merchantItem.userInfo.userId)) {
                if (merchantItem.itemVO.sales > 999) {
                    ((TextView) findViewById(R.id.master_consult_detail_sales)).setText("已售999+笔");
                } else {
                    ((TextView) findViewById(R.id.master_consult_detail_sales)).setText(String.format(getContext().getString(R.string.label_sales), merchantItem.itemVO.sales));
                }
            } else {
                if (merchantItem.itemVO.showSales > 999) {
                    ((TextView) findViewById(R.id.master_consult_detail_sales)).setText("已售999+笔");
                } else {
                    ((TextView) findViewById(R.id.master_consult_detail_sales)).setText(String.format(getContext().getString(R.string.label_sales), merchantItem.itemVO.showSales));
                }
            }

        }
        if (merchantItem.userInfo != null) {
            ((TextView) findViewById(R.id.master_consult_detail_merchant_name)).setText(
                    TextUtils.isEmpty(merchantItem.userInfo.nickname) ? "" : merchantItem.userInfo.nickname
            );
            if(!TextUtils.isEmpty(merchantItem.userInfo.avatar)) {
//                BaseImgView.loadimg((ImageView) findViewById(R.id.master_consult_detail_merchant_img), merchantItem.userInfo.avatar,
//                        R.mipmap.icon_default_avatar,
//                        R.mipmap.icon_default_avatar,
//                        R.mipmap.icon_default_avatar,
//                        ImageScaleType.EXACTLY,
//                        180, 180, 180);
                ImageLoadManager.loadCircleImage(CommonUtil.getImageFullUrl(merchantItem.userInfo.avatar), R.mipmap.icon_default_avatar,180, 180, (ImageView) findViewById(R.id.master_consult_detail_merchant_img));

            }else{
                ((ImageView)findViewById(R.id.master_consult_detail_merchant_img)).setImageResource(R.mipmap.icon_default_avatar);
            }
        }

        //详情打点
        if(merchantItem.itemVO != null && merchantItem.userInfo != null){
            Map<String, String> map = new HashMap<>();
            map.put(AnalyDataValue.KEY_MID, merchantItem.userInfo.userId + "");
            map.put(AnalyDataValue.KEY_MNAME, merchantItem.userInfo.nickname);
            map.put(AnalyDataValue.KEY_PID, merchantItem.itemVO.id + "");
            map.put(AnalyDataValue.KEY_PNAME, merchantItem.itemVO.title);
            TCEventHelper.onEvent(getContext(), AnalyDataValue.VIEW_CONSULTING_SERVICE_DETAIL, map);
        }

    }
}
