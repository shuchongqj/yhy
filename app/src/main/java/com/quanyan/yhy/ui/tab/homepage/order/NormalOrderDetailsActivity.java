package com.quanyan.yhy.ui.tab.homepage.order;

import android.os.Message;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quanyan.base.baseenum.IActionTitleBar;
import com.quanyan.base.util.StringUtil;
import com.quanyan.base.view.customview.NoScrollGridView;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.AnalyDataValue;
import com.quanyan.yhy.common.ItemType;
import com.quanyan.yhy.common.ResourceType;
import com.quanyan.yhy.eventbus.EvBusNativeGoBack;
import com.quanyan.yhy.service.controller.OrderController;
import com.quanyan.yhy.ui.adapter.base.BaseAdapterHelper;
import com.quanyan.yhy.ui.adapter.base.QuickAdapter;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.base.utils.TCEventHelper;
import com.quanyan.yhy.ui.integralmall.integralmallviewhelper.IntegralmallViewHelper;
import com.quanyan.yhy.ui.views.OrderDetailsItemView;
import com.yhy.common.beans.net.model.item.CodeQueryDTO;
import com.yhy.common.beans.net.model.tm.OrderDetailResult;
import com.yhy.common.beans.net.model.tm.PackageResult;
import com.yhy.common.beans.net.model.tm.TmMainOrder;
import com.yhy.common.beans.net.model.trip.ShortItem;
import com.yhy.common.beans.net.model.trip.ShortItemsResult;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.SPUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;

import static com.quanyan.yhy.R.id.layout_error_view;

/**
 * Created with Android Studio.
 * Title:NormalOrderDetailsActivity
 * Description:   积分商品订单详情
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:邓明佳
 * Date:2016/7/6
 * Time:9:41
 * Version 1.0
 */
public class NormalOrderDetailsActivity extends MyBaseOrderDetailsActivity {

    private OrderDetailsItemView mPrice;
    private OrderDetailsItemView mPriceDiscount;
    private OrderDetailsItemView mPricePoint;
    private OrderDetailsItemView mPriceShould;
    private TextView mTvMobile;
    private TextView mTvName;
    private TextView mTvAdress;
    private TextView tvTrackingNumber;
    private TextView tvExpress;
    private RelativeLayout rlLookLogistical;

    private OrderDetailResult mOrderDetail;
    private LinearLayout mLLRecommend;
    private NoScrollGridView mGridView;
    private LinearLayout mErrorView;
    private QuickAdapter mPointAdapter;
    private TextView mTVLoadMore;
    private boolean isPost;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_normal_order_details;
    }

    @Override
    protected void initChildView() {
        mTvMobile = (TextView) findViewById(R.id.tv_mobile);
        mTvName = (TextView) findViewById(R.id.tv_name);
        mTvAdress = (TextView) findViewById(R.id.tv_address);
        tvTrackingNumber = (TextView) findViewById(R.id.tv_tracking_number);
        tvExpress = (TextView) findViewById(R.id.tv_express);
        mLLRecommend = (LinearLayout) findViewById(R.id.ll_recommend);
        mGridView = (NoScrollGridView) findViewById(R.id.base_pullrefresh_gridview);
        mErrorView = (LinearLayout) findViewById(layout_error_view);
        mTVLoadMore = (TextView) findViewById(R.id.tv_load_more);
        mPrice = (OrderDetailsItemView) findViewById(R.id.tv_order_price);
        mPriceDiscount = (OrderDetailsItemView) findViewById(R.id.tv_order_discount);
        mPricePoint = (OrderDetailsItemView) findViewById(R.id.tv_order_price_point);
        mPriceShould = (OrderDetailsItemView) findViewById(R.id.tv_order_price_should);
        mPriceShould.mright.setTextSize(14);
        mPriceShould.mright.setTextColor(getResources().getColor(R.color.main));


        rlLookLogistical = (RelativeLayout) findViewById(R.id.rl_look_logistical);
        rlLookLogistical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            /*    if (mOrderDetail == null || mOrderDetail.mainOrder == null || mOrderDetail.mainOrder.bizOrder == null || mOrderDetail.logisticsOrder == null || StringUtil.isEmpty(mOrderDetail.logisticsOrder.expressNo) ||

                        StringUtil.isEmpty(mOrderDetail.logisticsOrder.expressCompany) || mOrderDetail.mainOrder.bizOrder.bizOrderId <= 0) {
                    return;
                }*/

                NavUtils.gotoLogisticsActivity(NormalOrderDetailsActivity.this, mOrderDetail.mainOrder.bizOrder.bizOrderId);
            }
        });

        //猜你喜欢
        mLLRecommend.setVisibility(View.GONE);
        mPointAdapter = new QuickAdapter<ShortItem>(NormalOrderDetailsActivity.this, R.layout.integralmall_home_fictitious_item_view, new ArrayList<ShortItem>()) {
            @Override
            protected void convert(BaseAdapterHelper helper, ShortItem item) {
                IntegralmallViewHelper.handleIntegralmallListItem(NormalOrderDetailsActivity.this, helper, item);
            }
        };
        mGridView.setAdapter(mPointAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ShortItem shortItem = (ShortItem) mPointAdapter.getItem(position);

                tcEvent(shortItem);

                NavUtils.gotoProductDetail(NormalOrderDetailsActivity.this,
                        ItemType.POINT_MALL,
                        shortItem.id,
                        shortItem.title);
            }
        });
        //查看更多
        mTVLoadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavUtils.gotoIntegralmallHomeActivity(NormalOrderDetailsActivity.this);
            }
        });
    }

    private void tcEvent(ShortItem shortItem) {
        Map<String, String> map = new HashMap<String, String>();
        map.put(AnalyDataValue.KEY_PID, shortItem.id + "");
        map.put(AnalyDataValue.KEY_TITLE, shortItem.title);
        TCEventHelper.onEvent(NormalOrderDetailsActivity.this, AnalyDataValue.POINT_ORDER_DETAIL_RECOMMEND, map);
    }

    @Override
    protected void reloadChildData(OrderDetailResult detail) {
        mOrderDetail = detail;

        mTvName.setText(detail.mainOrder.logisticsOrder.fullName);
        mTvAdress.setText(detail.mainOrder.logisticsOrder.prov + detail.mainOrder.logisticsOrder.city + detail.mainOrder.logisticsOrder.area + detail.mainOrder.logisticsOrder.address);

        mTvMobile.setText(detail.mainOrder.logisticsOrder.mobilePhone);
        mPrice.setMessage(getString(R.string.order_amount), StringUtil.converRMb2YunWithFlag(NormalOrderDetailsActivity.this, detail.mainOrder.originalTotalFee));
        if (detail.mainOrder.voucherDiscountFee <= 0) {
            mPriceDiscount.setVisibility(View.GONE);
        } else {
            mPriceDiscount.setVisibility(View.VISIBLE);
            mPriceDiscount.setMessage(getString(R.string.coupon), "-" + StringUtil.converRMb2YunWithFlag(NormalOrderDetailsActivity.this, detail.mainOrder.voucherDiscountFee));
        }

        if (detail.mainOrder.usePoint <= 0) {
            mPricePoint.setVisibility(View.GONE);
        } else {
            mPricePoint.setVisibility(View.VISIBLE);
//            mPricePoint.setMessage(getString(R.string.point), "-" + StringUtil.converRMb2YunWithFlag(NormalOrderDetailsActivity.this, detail.mainOrder.usePoint));
            mPricePoint.setMessage(getString(R.string.point), "-" + StringUtil.pointToYuanOne(detail.mainOrder.usePoint * 10));

        }

        mPriceShould.setMessage(getString(R.string.amount_payable), StringUtil.converRMb2YunWithFlag(NormalOrderDetailsActivity.this, detail.mainOrder.totalFee));

        TmMainOrder bean = detail.mainOrder;
        String trackingNumber = getString(R.string.txt_no_data);
        String express = getString(R.string.txt_no_data);

        if (bean != null) {
            if (OrderController.getLogistState(bean)) {
                PackageResult logisticsOrder = bean.packageInfo;
                if (logisticsOrder != null) {
                    if (logisticsOrder.expressNo != null)
                        trackingNumber = logisticsOrder.expressNo;
                    if (logisticsOrder.expressCompany != null)
                        express = logisticsOrder.expressCompany;
                }
                rlLookLogistical.setVisibility(View.VISIBLE);
                getFormatContent(tvTrackingNumber, getString(R.string.tracking_number), trackingNumber);
                getFormatContent(tvExpress, getString(R.string.express), express);
            }
        }
        if (bean != null) {
            String orderType = bean.bizOrder.orderType;
            String orderStatus = bean.bizOrder.orderStatus;
            if (bean.detailOrders != null && bean.detailOrders.size() > 0) {
                // 积分，必买订单
                if (ValueConstants.ORDER_TYPE_NORMAL.equals(orderType) || ValueConstants.ORDER_TYPE_POINT.equals(orderType)) {
                    //  已收货，已完成
                    if (ValueConstants.ORDER_STATUS_FINISH.equals(orderStatus) || ValueConstants.ORDER_STATUS_RATED.equals(orderStatus) || ValueConstants.ORDER_STATUS_NOT_RATE.equals(orderStatus)) {

                        PackageResult logisticsOrder = bean.packageInfo;
                        if (logisticsOrder != null) {
                            if (logisticsOrder.expressNo != null)
                                trackingNumber = logisticsOrder.expressNo;
                            if (logisticsOrder.expressCompany != null)
                                express = logisticsOrder.expressCompany;
                        }
                        rlLookLogistical.setVisibility(View.VISIBLE);
                        getFormatContent(tvTrackingNumber, getString(R.string.tracking_number), trackingNumber);
                        getFormatContent(tvExpress, getString(R.string.express), express);
                    }//  已发货
                    else if (ValueConstants.ORDER_STATUS_SHIPPING.equals(orderStatus)) {
                        PackageResult logisticsOrder = bean.packageInfo;
                        if (logisticsOrder != null) {
                            if (logisticsOrder.expressNo != null)
                                trackingNumber = logisticsOrder.expressNo;
                            if (logisticsOrder.expressCompany != null)
                                express = logisticsOrder.expressCompany;
                        }
                        rlLookLogistical.setVisibility(View.VISIBLE);
                        getFormatContent(tvTrackingNumber, getString(R.string.tracking_number), trackingNumber);
                        getFormatContent(tvExpress, getString(R.string.express), express);
                    }

                }


            }


            //积分订单-猜你喜欢
            if (ValueConstants.ORDER_TYPE_NORMAL.equals(orderType)) {
                CodeQueryDTO codeQueryDTO = new CodeQueryDTO();
                codeQueryDTO.boothCode = ResourceType.QUANYAN_ORDER_DETAIL_POINT_PRODUCTS_RECOMMAND;
                codeQueryDTO.pageNo = 1;
                codeQueryDTO.pageSize = ValueConstants.PAGESIZE;
                if (!StringUtil.isEmpty(SPUtils.getExtraCurrentLat(NormalOrderDetailsActivity.this))) {
                    codeQueryDTO.latitude = Double.parseDouble(SPUtils.getExtraCurrentLat(NormalOrderDetailsActivity.this));
                }
                if (!StringUtil.isEmpty(SPUtils.getExtraCurrentLon(NormalOrderDetailsActivity.this))) {
                    codeQueryDTO.longitude = Double.parseDouble(SPUtils.getExtraCurrentLon(NormalOrderDetailsActivity.this));
                }
                controller.doGetRecommendCode(NormalOrderDetailsActivity.this, codeQueryDTO);
            }

        }


    }

    public void getFormatContent(TextView textView, String label, String content) {
        if (textView != null) {
            textView.setText(Html.fromHtml("" + label + "<font color=\"#333333\">" + content + "</font>"));
        }


    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case ValueConstants.POINT_ORDER_DETAIL_LIKE_RECOMMEND_OK:
                ShortItemsResult result = (ShortItemsResult) msg.obj;
                if (result != null) {
                    reloadLikeRecommend(result);
                } else {
                    mLLRecommend.setVisibility(View.GONE);
                }
                break;
            case ValueConstants.POINT_ORDER_DETAIL_LIKE_RECOMMEND_KO:
                mLLRecommend.setVisibility(View.GONE);
                break;
        }
    }

    private void reloadLikeRecommend(ShortItemsResult result) {
        if (result.shortItemList != null && result.shortItemList.size() > 0) {
            mLLRecommend.setVisibility(View.VISIBLE);
            mPointAdapter.replaceAll(result.shortItemList);
        } else {
            mLLRecommend.setVisibility(View.GONE);
        }
    }

    private void showNoDataPageView() {
        mErrorView.setVisibility(View.VISIBLE);
        mTVLoadMore.setVisibility(View.GONE);
        showErrorView(mErrorView,
                IActionTitleBar.ErrorType.EMPTYVIEW, getString(R.string.error_text_signed_nodata), " ", "", null);
    }

    @Override
    public void finish() {
        super.finish();
        if (!isPost) {
            isPost = true;
            EventBus.getDefault().post(new EvBusNativeGoBack());
        }
    }
}
