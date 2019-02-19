package com.quanyan.yhy.ui.tab.homepage.order;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.baseenum.IActionTitleBar;
import com.quanyan.base.util.LocalUtils;
import com.quanyan.base.util.StringUtil;
import com.quanyan.base.view.BaseNavView;
import com.quanyan.base.view.customview.NoScrollListView;
import com.quanyan.base.view.customview.pulltorefresh.PullToRefreshBase;
import com.quanyan.base.view.customview.pulltorefresh.PullToRefreshScrollView;
import com.quanyan.base.yminterface.ErrorViewClick;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.AnalyDataValue;
import com.quanyan.yhy.common.ErrorCode;
import com.quanyan.yhy.common.ItemType;
import com.quanyan.yhy.common.PayStatus;
import com.quanyan.yhy.common.UpdateOrderState;
import com.quanyan.yhy.service.controller.OrderController;
import com.quanyan.yhy.ui.adapter.base.BaseAdapterHelper;
import com.quanyan.yhy.ui.adapter.base.QuickAdapter;
import com.quanyan.yhy.ui.base.utils.DialogUtil;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.base.utils.TCEventHelper;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.servicerelease.ExpertOrderListActivity;
import com.quanyan.yhy.ui.views.OrderDetailsItemView;
import com.yhy.common.beans.net.model.tm.OrderDetailResult;
import com.yhy.common.beans.net.model.tm.TmButtonStatus;
import com.yhy.common.beans.net.model.tm.TmDetailOrder;
import com.yhy.common.constants.IntentConstants;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.CommonUtil;
import com.yhy.common.utils.SPUtils;
import com.yhy.imageloader.ImageLoadManager;
import com.yhy.service.IUserService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Created by 邓明佳 on 2015/11/14.
 * 订单详情积累
 */
public abstract class MyBaseOrderDetailsActivity extends BaseActivity implements PullToRefreshBase.OnRefreshListener2, View.OnClickListener {

    /**
     * 订单详情itemLayout
     */
    public PullToRefreshScrollView scrollView;

    //订单顶部 状态
    public TextView mOrderTime;
    public TextView mOrderCode;
    public TextView mOrderStatus;

    public TextView mCloseReason;
    public TextView mCloseTime;
    public View divider;


    public TextView mMerchant;
    public TextView mContactMerchant;
    public ImageView mIconMerchant;

    //listview adapter 显示商品子订单
    private QuickAdapter<TmDetailOrder> goodsAdapter;
    private NoScrollListView noScrollListView;

    //    private LinearLayout mCustomService;
//    private LinearLayout mHotLine;
    private TextView mPayTime;
    private TextView mDeliveryTime;
    private TextView mFinishTime;

    private TextView mRefund;

    //付款部分
    public TextView mTvBottom;

    public OrderController controller;
    //订单号
    private long orderCode;
    //订单详情 bean对象
    private OrderDetailResult orderDetail;
    //时间format用于时间展示
    public SimpleDateFormat dateFormat;
    public SimpleDateFormat dateFormatNoHH;

    private Dialog mCancelDlg;

    @Autowired
    IUserService userService;

    @Override
    public View onLoadContentView() {
        return View.inflate(this, getLayoutResId(), null);
    }

    private BaseNavView mBaseNavView;

    @Override
    public View onLoadNavView() {
        mBaseNavView = new BaseNavView(this);
        mBaseNavView.setTitleText(R.string.order_details);
        mBaseNavView.setLeftClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new UpdateOrderState());
                finish();
            }
        });
        return mBaseNavView;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            EventBus.getDefault().post(new UpdateOrderState());

            finish();
        }
        return false;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initView();
        initDate();
    }

    @Override
    protected void onResume() {
//        refreshBottomStatus();
        super.onResume();
    }

    //初始化数据
    private void initDate() {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        dateFormatNoHH = new SimpleDateFormat("yyyy-MM-dd");
        orderCode = getIntent().getLongExtra(OrderController.PARAMS_ORDER_CODE, -1);
        controller = new OrderController(this, mHandler);
        getOrderDetails();
    }

    //初始化视图
    private void initView() {
        initChildView();

        mOrderTime = (TextView) findViewById(R.id.tv_order_time);
        mOrderCode = (TextView) findViewById(R.id.tv_order_code);
        mOrderStatus = (TextView) findViewById(R.id.tv_order_status);
        mCloseReason = (TextView) findViewById(R.id.tv_close_reason);
        mCloseTime = (TextView) findViewById(R.id.tv_close_time);
        divider = findViewById(R.id.divider);

        scrollView = (PullToRefreshScrollView) findViewById(R.id.base_scroll_view);
        scrollView.setMode(PullToRefreshBase.Mode.DISABLED);
        scrollView.setOnRefreshListener(this);
        //底部付款
        mTvBottom = (TextView) findViewById(R.id.tv_bottom);

        mMerchant = (TextView) findViewById(R.id.tv_merchant);
        mContactMerchant = (TextView) findViewById(R.id.tv_contact_merchant);
        mIconMerchant = (ImageView) findViewById(R.id.image_merchant);

        noScrollListView = (NoScrollListView) findViewById(R.id.base_listview);
        noScrollListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        noScrollListView.setBackgroundColor(getResources().getColor(R.color.neu_f4f4f4));

//        findViewById(R.id.ll_custom_service).setOnClickListener(this);
//        findViewById(R.id.ll_hotline).setOnClickListener(this);
        findViewById(R.id.ll_merhchant_im).setOnClickListener(this);
        findViewById(R.id.ll_merhchant_hotline).setOnClickListener(this);
        mPayTime = (TextView) findViewById(R.id.tv_pay_time);
        mDeliveryTime = (TextView) findViewById(R.id.tv_delivery_time);
        mFinishTime = (TextView) findViewById(R.id.tv_finish_time);
        mRefund = (TextView) findViewById(R.id.tv_refund);

    }

    protected abstract int getLayoutResId();

    protected abstract void initChildView();

    protected abstract void reloadChildData(OrderDetailResult detail);

    //获取订单详情网络请求
    public void getOrderDetails() {
        showLoadingView(null);
        controller.doGetOrderDetails(MyBaseOrderDetailsActivity.this, orderCode);
    }

    /**
     * 跳转订单详情页
     *
     * @param context
     * @param orderType 订单类型 OrderController。PARENT_TYPE
     * @param orderCode 订单号
     */
    public static void gotoOrderDetailsActivity(Context context, String orderType, long orderCode) {
        Intent intent = getGotoOrderDetailsIntent(context, orderType, orderCode);
        if (intent == null) {
//            ToastUtil.showToast(context, "未识别订单类型");
            return;
        }
        context.startActivity(intent);
    }

    public static Intent getGotoOrderDetailsIntent(Context context, String orderType, long orderCode) {
        if (StringUtil.isEmpty(orderType)) {
            return null;
        }
        Intent intent = null;
        if (ValueConstants.ORDER_TYPE_LOCAL.equals(orderType) || ValueConstants.ORDER_TYPE_TRAVEL.equals(orderType) || orderType.equals(ValueConstants.ORDER_TYPE_TOUR_LINE) || orderType.equals(ValueConstants.ORDER_TYPE_FREE_LINE) || orderType.equals(ValueConstants.ORDER_TYPE_TOUR_LINE_ABOARD) || orderType.equals(ValueConstants.ORDER_TYPE_FREE_LINE_ABOARD) || orderType.equals(ValueConstants.ORDER_TYPE_ACTIVITY) || orderType.equals(ValueConstants.ORDER_TYPE_CITY_ACTIVITY)) {
            intent = new Intent(context, TravelOrderDetailsActivity.class);
        } else if (orderType.equals(ValueConstants.ORDER_TYPE_NORMAL) || orderType.equals(ValueConstants.ORDER_TYPE_POINT)) {
            intent = new Intent(context, NormalOrderDetailsActivity.class);
        }
        if (intent == null) return null;
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(OrderController.PARAMS_ORDER_CODE, orderCode);
        return intent;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        int what = msg.what;
        hideLoadingView();
        hideErrorView(null);
        if (what == ValueConstants.GET_ORDER_DETAILS_SUCCESS) {
            //获取订单详情成功
            scrollView.setVisibility(View.VISIBLE);
            scrollView.onRefreshComplete();
            OrderDetailResult detail = (OrderDetailResult) msg.obj;
            reloadData(detail);
        } else if (what == ValueConstants.GET_ORDER_DETAILS_FAIL) {
            //获取订单详情失败
            scrollView.setVisibility(View.GONE);
            scrollView.onRefreshComplete();
            showErrorView(null, ErrorCode.NETWORK_UNAVAILABLE == msg.arg1 ?
                    IActionTitleBar.ErrorType.NETUNAVAILABLE : IActionTitleBar.ErrorType.ERRORNET, "", "", "", new ErrorViewClick() {
                @Override
                public void onClick(View view) {
                    getOrderDetails();
                }
            });

        } else if (what == ValueConstants.CANCLE_ORDER_SUCCESS) {
            //取消订单成功
            if (orderDetail != null && orderDetail.mainOrder != null) {
                SPUtils.setScore(this, SPUtils.getScore(this) + orderDetail.mainOrder.usePoint);
            }
            getOrderDetails();
            EventBus.getDefault().post(new UpdateOrderState());
        } else if (what == ValueConstants.CANCLE_ORDER_FAIL) {
            //取消订单失败
            ToastUtil.showToast(this, StringUtil.handlerErrorCode(this, msg.arg1));
        } else if (what == ValueConstants.SHOW_LOADING_VIEW) {
            //显示加载view
            showLoadingView((String) msg.obj);
        } else if (what == ValueConstants.CONFIRM_RECIVER_SUCCESS) {
            //确认收货成功
            //从新获取订单详情
            getOrderDetails();
            EventBus.getDefault().post(new UpdateOrderState());
        } else if (what == ValueConstants.CONFIRM_RECIVER_FAIL) {
            //确认收货失败
            ToastUtil.showToast(this, StringUtil.handlerErrorCode(this, msg.arg1));
        }
    }

    /**
     * 根据服务器返回的数据加载到页面
     *
     * @param detail bean对象
     */
    private void reloadData(final OrderDetailResult detail) {
        if (detail == null) return;
        orderDetail = detail;
        reloadChildData(detail);
        //清空所有view 重新加载

        //下单时间
        Long createTime = detail.mainOrder.bizOrder.createTime;
        mOrderTime.setText(getString(R.string.order_create_time) + " : " + dateFormat.format(createTime == null ? 0 : createTime));
        //订单号
        mOrderCode.setText(getString(R.string.order_code) + " : " + detail.mainOrder.bizOrder.bizOrderId);
        //订单状态

        // mOrderStatus.setText(OrderController.getOrderStatusString(detail.mainOrder.bizOrder.orderStatus, detail.mainOrder.bizOrder.orderType, this));

        List<TmDetailOrder> detailOrderList = detail.mainOrder.detailOrders;


        if (ValueConstants.ORDER_TYPE_NORMAL.equals(detail.mainOrder.bizOrder.orderStatus) || detail.mainOrder.bizOrder.orderStatus.equals(ValueConstants.ORDER_TYPE_POINT)) {
            if (ValueConstants.ORDER_STATUS_WAITING_DELIVERY.equals(detail.mainOrder.bizOrder.orderStatus)) { //主订单已付款
                mOrderStatus.setText(OrderController.getOrderStatus(detailOrderList, this));
            } else {
                mOrderStatus.setText(OrderController.getOrderStatusString(detail.mainOrder.bizOrder.orderStatus, detail.mainOrder.bizOrder.orderType, this));
            }
        } else {
            mOrderStatus.setText(OrderController.getOrderStatusString(detail.mainOrder.bizOrder.orderStatus, detail.mainOrder.bizOrder.orderType, this));
        }


        if (ValueConstants.ORDER_STATUS_CANCEL.equals(detail.mainOrder.bizOrder.orderStatus)) {
            mCloseTime.setText(getString(R.string.order_close_time, getTimeString(detail.mainOrder.completionTime)));
            mCloseReason.setText(getString(R.string.order_close_reason, !TextUtils.isEmpty(detail.mainOrder.closeReason) ? detail.mainOrder.closeReason : getString(R.string.cancel_reason_outime)));
            mCloseTime.setVisibility(View.VISIBLE);
            mCloseReason.setVisibility(View.VISIBLE);
            divider.setVisibility(View.VISIBLE);
        } else {
            mCloseTime.setVisibility(View.GONE);
            mCloseReason.setVisibility(View.GONE);
            divider.setVisibility(View.GONE);
        }

        if (detail.mainOrder.orgInfo != null) {
            mMerchant.setText(detail.mainOrder.orgInfo.name);
        }

        if (detail.mainOrder.merchantInfo != null) {
            String headIconUrl = detail.mainOrder.merchantInfo.logo;
            if (StringUtil.isEmpty(headIconUrl)) {
                if (detail.mainOrder.merchantInfo.userInfo != null) {
                    headIconUrl = detail.mainOrder.merchantInfo.userInfo.imgUrl;
                }
            }
//            BaseImgView.loadimg(mIconMerchant,
//                    headIconUrl,
//                    R.mipmap.ic_shop_default_logo,
//                    R.mipmap.ic_shop_default_logo,
//                    R.mipmap.ic_shop_default_logo,
//                    null,
//                    150,
//                    150,
//                    90);
            ImageLoadManager.loadCircleImage(CommonUtil.getImageFullUrl(headIconUrl), R.mipmap.ic_shop_default_logo, 150, 150, mIconMerchant);

//            mMerchant.setText(detail.mainOrder.merchantInfo.name);
            mMerchant.setText(detail.mainOrder.orgInfo.name);

            mIconMerchant.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (detail.mainOrder.merchantInfo.userInfo == null) {
                        return;
                    }
                    NavUtils.gotoPersonalPage(MyBaseOrderDetailsActivity.this,
                            detail.mainOrder.merchantInfo.userInfo.userId,
                            detail.mainOrder.merchantInfo.userInfo.name,
                            detail.mainOrder.merchantInfo.userInfo.options);
                }
            });
            mMerchant.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (detail.mainOrder.merchantInfo.userInfo == null) {
                        return;
                    }
                    NavUtils.gotoPersonalPage(MyBaseOrderDetailsActivity.this,
                            detail.mainOrder.merchantInfo.userInfo.userId,
                            detail.mainOrder.merchantInfo.userInfo.name,
                            detail.mainOrder.merchantInfo.userInfo.options);
                }
            });
//            refreshBottomStatus();
            mContactMerchant.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (detail.mainOrder != null &&
                            detail.mainOrder.merchantInfo != null &&
                            detail.mainOrder.merchantInfo.userInfo != null &&
                            detail.mainOrder.merchantInfo.userInfo.userId == 0) {
                        ToastUtil.showToast(MyBaseOrderDetailsActivity.this, "商户id为0");
                        return;
                    }
                    tcEvent(detail);
                    NavUtils.gotoMessageActivity(MyBaseOrderDetailsActivity.this,
                            (int) detail.mainOrder.merchantInfo.userInfo.userId,
                            detail.mainOrder.bizOrder.bizOrderId);
                }
            });
        } else {
            mIconMerchant.setImageResource(R.mipmap.ic_shop_default_logo);
            mContactMerchant.setOnClickListener(null);
        }

        if (goodsAdapter == null) {
            noScrollListView.setAdapter(goodsAdapter = new QuickAdapter<TmDetailOrder>(this, R.layout.item_order_goods) {
                @Override
                protected void convert(BaseAdapterHelper helper, final TmDetailOrder item) {
                    helper.setImageUrl(R.id.image, item.itemPic, 300, 300, R.mipmap.icon_default_150_150);
                    final String orderType = item.bizOrder.orderType;
                    helper.getView(R.id.item_order_goods_right_layout).setVisibility(View.GONE);
                    if (orderType.equals(ValueConstants.ORDER_TYPE_TOUR_LINE) || orderType.equals(ValueConstants.ORDER_TYPE_FREE_LINE) || orderType.equals(ValueConstants.ORDER_TYPE_TOUR_LINE_ABOARD) || orderType.equals(ValueConstants.ORDER_TYPE_FREE_LINE_ABOARD)) {
                        helper.setText(R.id.tv_order_title, item.itemTitle);
                        helper.setText(R.id.tv_order_message, getString(R.string.depart_time) + ": " + getTimeString(detail.mainOrder.departTime, dateFormatNoHH));
                        helper.getView(R.id.tv_order_message).setVisibility(View.VISIBLE);
                        helper.setText(R.id.tv_order_sku, getString(R.string.package_type) + ": " + item.packageType);
                    } else if (orderType.equals(ValueConstants.ORDER_TYPE_ACTIVITY) || orderType.equals(ValueConstants.ORDER_TYPE_CITY_ACTIVITY)) {
                        helper.setText(R.id.tv_order_title, item.itemTitle);
                        helper.setText(R.id.tv_order_message, getString(R.string.depart_time) + ": " + item.activityTime);
                        helper.getView(R.id.tv_order_message).setVisibility(View.VISIBLE);
                        helper.setText(R.id.tv_order_sku, getString(R.string.package_type) + ": " + item.activityContent);
                    } else if (orderType.equals(ValueConstants.ORDER_TYPE_SPOTS)) {
                        helper.setText(R.id.tv_order_title, detail.mainOrder.scenicTitle + "(" + detail.mainOrder.ticketTitle + ")");
                        helper.setText(R.id.tv_order_message, getString(R.string.admission_time) + ": " + getTimeString(detail.mainOrder.scenicEnterTime, dateFormatNoHH));
                        helper.getView(R.id.tv_order_message).setVisibility(View.VISIBLE);

                    } else if (orderType.equals(ValueConstants.ORDER_TYPE_HOTEL) || orderType.equals(ValueConstants.ORDER_TYPE_HOTEL_OFFLINE)) {
                        helper.setText(R.id.tv_order_title, detail.mainOrder.hotelTitle);
                        helper.setText(R.id.tv_order_message, detail.mainOrder.roomTitle);
                        helper.setText(R.id.tv_order_sku, item.itemTitle);
                    } else if (orderType.equals(ValueConstants.ORDER_TYPE_NORMAL)) {
                        helper.getView(R.id.item_order_goods_right_layout).setVisibility(View.VISIBLE);
                        helper.setText(R.id.tv_order_title, item.itemTitle);
                        if (!TextUtils.isEmpty(item.packageType)) {
                            helper.setText(R.id.tv_order_message, getString(R.string.package_type) + ": " + item.packageType);
                            helper.getView(R.id.tv_order_message).setVisibility(View.VISIBLE);
                        } else {
                            helper.getView(R.id.tv_order_message).setVisibility(View.INVISIBLE);
                        }
                        helper.getView(R.id.tv_order_count_content).setVisibility(View.VISIBLE);
                        helper.getView(R.id.tv_order_total_price_content).setVisibility(View.VISIBLE);
                        helper.getView(R.id.tv_order_count).setVisibility(View.VISIBLE);
                        helper.setText(R.id.tv_order_count_content, "" + item.bizOrder.buyAmount);
                        helper.setText(R.id.tv_order_total_price_content, StringUtil.converRMb2YunWithFlag(context, item.itemPrice));

                    } else if (orderType.equals(ValueConstants.ORDER_TYPE_POINT_MALL)){
                        // 标题
                        helper.setText(R.id.tv_order_title, item.itemTitle);
                        helper.getView(R.id.item_order_goods_right_layout).setVisibility(View.VISIBLE);

                        // 总价
                        helper.getView(R.id.tv_order_total_price_content).setVisibility(View.VISIBLE);
                        helper.setText(R.id.tv_order_total_price_content, StringUtil.pointToYuan(item.bizOrder.originalActualTotalFee));
                        // 总数
                        helper.getView(R.id.lin_goods_num).setVisibility(View.VISIBLE);
                        helper.setText(R.id.tv_order_count_content, String.valueOf(item.bizOrder.buyAmount));

                    } else {
                        helper.getView(R.id.tv_order_count_content).setVisibility(View.GONE);
                        helper.getView(R.id.tv_order_total_price_content).setVisibility(View.GONE);
                        helper.getView(R.id.tv_order_count).setVisibility(View.GONE);
                    }

//                    helper.getView(R.id.iv_arrow).setVisibility(View.GONE);
//                    helper.getView(R.id.tv_order_sku).setVisibility(TextUtils.isEmpty(item.skuTitle) ? View.GONE : View.VISIBLE);
//                    helper.setText(R.id.tv_order_sku, item.skuTitle);
//        } else {
//            helper.setText(R.id.tv_order_message, context.getString(R.string.depart_time_order_list) + format2.format(bean.departTime));
//            helper.getView(R.id.tv_order_sku).setVisibility(View.GONE);
//        }
//        helper.setText(R.id.tv_price, StringUtil.converRMb2YunWithFlag(item.itemPrice));
//        helper.setText(R.id.tv_count, "× " + item.bizOrder.buyAmount);
//        if (orderType == OrderController.PARENT_TYPE_TRAVEL) {
//            helper.getView(R.id.ll_price).setVisibility(View.GONE);
//        } else {
//            helper.getView(R.id.ll_price).setVisibility(View.VISIBLE);
//        }
                    //TODO 新增商品的跳转
                    helper.setOnClickListener(R.id.ll_goods, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String orderType = item.bizOrder.orderType;
                            NavUtils.gotoProductDetail(context, orderType, item.itemId, item.itemTitle);
                        }
                    });
                }
            });
        }
        //当订单类型为旅行 listview中商品信息只显示一个，其他显示全部
//        if (orderType == OrderController.PARENT_TYPE_TRAVEL) {
        List<TmDetailOrder> goods = new ArrayList<>();
        goods.addAll(detail.mainOrder.detailOrders);
        // goods.add(detail.mainOrder.detailOrders.get(0));
        goodsAdapter.replaceAll(goods);
//        } else {
//            goodsAdapter.replaceAll(detail.mainOrder.detailOrders);
//        }

        if (detail.mainOrder.bizOrder.payTime > 0) {
            mPayTime.setText(getString(R.string.paid_time) + " :" + getTimeString(detail.mainOrder.bizOrder.payTime));
            mPayTime.setVisibility(View.VISIBLE);
        } else {
            mPayTime.setVisibility(View.GONE);
        }

//        if (detail.mainOrder.deliveryTime > 0) {
//            mDeliveryTime.setText(getString(R.string.delivery_time) + " :" + getTimeString(detail.mainOrder.deliveryTime));
//            mDeliveryTime.setVisibility(View.VISIBLE);
//        } else {
//            mDeliveryTime.setVisibility(View.GONE);
//        }
        if (detail.mainOrder.completionTime > 0 && !ValueConstants.ORDER_STATUS_CANCEL.equals(detail.mainOrder.bizOrder.orderStatus)) {
            mFinishTime.setText(getString(R.string.finished_time) + " :" + getTimeString(detail.mainOrder.completionTime));
            mFinishTime.setVisibility(View.VISIBLE);
        } else {
            mFinishTime.setVisibility(View.GONE);
        }

//        if (detail.logisticsOrder.consignTime > 0 && !ValueConstants.ORDER_TYPE_HOTEL_OFFLINE.equals(detail.mainOrder.bizOrder.orderType)) {
//            mDeliveryTime.setText(getString(R.string.delivery_time) + " :" + getTimeString(detail.logisticsOrder.consignTime));
//            mDeliveryTime.setVisibility(View.VISIBLE);
//        } else {
//            mDeliveryTime.setVisibility(View.GONE);
//        }
        //TODO 只有实物商品才会有发货时间
        if ((ValueConstants.ORDER_TYPE_POINT.equals(detail.mainOrder.bizOrder.orderType) || ValueConstants.ORDER_TYPE_POINT_MALL.equals(detail.mainOrder.bizOrder.orderType) ||
                ValueConstants.ORDER_TYPE_NORMAL.equals(detail.mainOrder.bizOrder.orderType)) && detail.logisticsOrder.consignTime > 0) {
            mDeliveryTime.setText(getString(R.string.delivery_time) + " :" + getTimeString(detail.logisticsOrder.consignTime));
            mDeliveryTime.setVisibility(View.VISIBLE);
        } else {
            mDeliveryTime.setVisibility(View.GONE);
        }

        TmButtonStatus buttonStatus = detail.mainOrder.buttonStatus;
        mTvBottom.setVisibility(View.GONE);
        mRefund.setVisibility(View.GONE);
        if (buttonStatus.payButton) {
            mTvBottom.setText(R.string.pay_now);
            mTvBottom.setOnClickListener(payOrderListener);
            mTvBottom.setVisibility(View.VISIBLE);
            if (buttonStatus.cancelButton) {
                mRefund.setText(R.string.cancel_order);
                mRefund.setOnClickListener(cancelOrderListener);
                mRefund.setVisibility(View.VISIBLE);
            }
        } else if (buttonStatus.buyerConfirmOrder) {
            mTvBottom.setText(R.string.confirm_receipt);
            mTvBottom.setOnClickListener(confrimOrderListener);
            mTvBottom.setVisibility(View.VISIBLE);
            if (buttonStatus.refundButton) {
                mRefund.setText(R.string.request_refund);
                mRefund.setOnClickListener(refundOrderListener);
                mRefund.setVisibility(View.VISIBLE);
            }
        } else if (buttonStatus.buyerConfirmOrder) {
            mTvBottom.setText(R.string.confirm_receipt);
            mTvBottom.setOnClickListener(confrimOrderListener);
            mTvBottom.setVisibility(View.VISIBLE);
            if (buttonStatus.cancelButton) {
                mRefund.setText(R.string.cancel_order);
                mRefund.setOnClickListener(cancelOrderListener);
                mRefund.setVisibility(View.VISIBLE);
            }
        } else if (buttonStatus.refundButton) {
            mRefund.setText(R.string.request_refund);
            mRefund.setOnClickListener(refundOrderListener);
            mRefund.setVisibility(View.VISIBLE);
//            mTvBottom.setText(R.string.request_refund);
//            mTvBottom.setOnClickListener(refundOrderListener);
//            mTvBottom.setVisibility(View.VISIBLE);
//            if (buttonStatus.cancelButton) {
//                mRefund.setText(R.string.cancel_order);
//                mRefund.setOnClickListener(cancelOrderListener);
//                mRefund.setVisibility(View.VISIBLE);
//            }
        } else if (buttonStatus.rateButton) {
            // TODO: 2018/3/17  评论功能下期做
//            mTvBottom.setVisibility(View.VISIBLE);
            mTvBottom.setVisibility(View.GONE);
            mTvBottom.setText(R.string.order_comment);
            mTvBottom.setOnClickListener(commentOrderListener);
            if (buttonStatus.cancelButton) {
                mRefund.setText(R.string.cancel_order);
                mRefund.setOnClickListener(cancelOrderListener);
                mRefund.setVisibility(View.VISIBLE);
            }
        } else if (buttonStatus.cancelButton) {
            mRefund.setText(R.string.cancel_order);
            mRefund.setOnClickListener(cancelOrderListener);
            mRefund.setVisibility(View.VISIBLE);
        }
    }

    private void tcEvent(OrderDetailResult detail) {
        Map<String, String> map = new HashMap<>();
        map.put(AnalyDataValue.KEY_UID, detail.mainOrder.merchantInfo.userInfo.userId + "");
        map.put(AnalyDataValue.KEY_BID, detail.mainOrder.bizOrder.bizOrderId + "");
        TCEventHelper.onEvent(MyBaseOrderDetailsActivity.this, AnalyDataValue.ORDER_DETAIL_CONNECTION_SELLER, map);
    }

    private View.OnClickListener commentOrderListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (orderDetail == null) return;
            if (ValueConstants.ORDER_TYPE_NORMAL.equals(orderDetail.mainOrder.bizOrder.orderType) || orderDetail.mainOrder.bizOrder.orderType.equals(ValueConstants.ORDER_TYPE_POINT)) {
                NavUtils.gotoGoodsCommentListActivity(MyBaseOrderDetailsActivity.this, orderCode, userService.getLoginUserId(), OrderController.getOrderTypeStringForComment(orderDetail.mainOrder.bizOrder.orderType), IntentConstants.REQUEST_CODE_COMMENT);
            } else {
                NavUtils.gotoWriteCommentAcitivty(MyBaseOrderDetailsActivity.this, orderCode, userService.getLoginUserId(), OrderController.getOrderTypeStringForComment(orderDetail.mainOrder.bizOrder.orderType), IntentConstants.REQUEST_CODE_COMMENT);
            }
            // NavUtils.gotoWriteCommentAcitivty(MyBaseOrderDetailsActivity.this, orderCode, SPUtils.getUid(MyBaseOrderDetailsActivity.this), OrderController.getOrderTypeStringForComment(orderDetail.mainOrder.bizOrder.orderType), IntentConstants.REQUEST_CODE_COMMENT);
        }
    };
    Dialog confirmDialog;
    private View.OnClickListener confrimOrderListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (orderDetail == null) return;
            confirmDialog = DialogUtil.showMessageDialog(MyBaseOrderDetailsActivity.this, null, getString(R.string.label_confirm_order), getString(R.string.label_btn_ok), getString(R.string.label_btn_cancel), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    confirmDialog.dismiss();
                    controller.confirmOrder(MyBaseOrderDetailsActivity.this, orderDetail.mainOrder.bizOrder.bizOrderId);
                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    confirmDialog.dismiss();
                }
            });

            confirmDialog.show();

        }
    };

    private View.OnClickListener refundOrderListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (orderDetail == null) return;
            LocalUtils.call(MyBaseOrderDetailsActivity.this, orderDetail.mainOrder.servicePhone);
        }
    };


    private View.OnClickListener payOrderListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (orderDetail == null) return;

            String url = SPUtils.getPonitPayUrl(MyBaseOrderDetailsActivity.this).trim();
            if (url != null && !url.isEmpty()) {
                url = url.replaceAll(":orderId", String.valueOf(orderDetail.mainOrder.bizOrder.bizOrderId));
                NavUtils.startWebview(MyBaseOrderDetailsActivity.this, "", url, 0);
            }

//            NavUtils.gotoPayActivity(MyBaseOrderDetailsActivity.this, new long[]{orderDetail.mainOrder.bizOrder.bizOrderId}, String.valueOf(orderDetail.mainOrder.bizOrder.actualTotalFee),
//                    ExpertOrderListActivity.REQ_CODE_REFRESH_ORDER_LIST);


        }
    };

    private View.OnClickListener cancelOrderListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (orderDetail == null) return;
//            controller.showCancelOrderDialog(MyBaseOrderDetailsActivity.this, orderDetail.mainOrder.bizOrder.bizOrderId, orderDetail.mainOrder.bizOrder.orderType);
            if (mCancelDlg == null) {
                mCancelDlg = DialogUtil.showMessageDialog(MyBaseOrderDetailsActivity.this,
                        null,
                        getString(R.string.cancel_order_confirm),
                        getString(R.string.label_btn_ok),
                        getString(R.string.label_btn_cancel),
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mCancelDlg.dismiss();
                                controller.showCancelOrderDialog(MyBaseOrderDetailsActivity.this, orderDetail.mainOrder.bizOrder.bizOrderId, orderDetail.mainOrder.bizOrder.orderType);

                            }
                        },
                        null);
            }
            mCancelDlg.show();
        }
    };


//    private View.OnClickListener refundListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            if (orderDetail == null) return;
//            showLoadingView("确认收货");
//            controller.confirmOrder(orderDetail.mainOrder.bizOrder.bizOrderId);
//        }
//    };

    /**
     * 根据时间戳获取时间字符串
     */
    public String getTimeString(long timeMillons) {
        return getTimeString(timeMillons, dateFormat);
    }

    /**
     * 根据时间戳获取时间字符串
     */
    public String getTimeString(long timeMillons, SimpleDateFormat format) {
        return format.format(new Date(timeMillons));
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        getOrderDetails();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ExpertOrderListActivity.REQ_CODE_REFRESH_ORDER_LIST:
                    if (data != null) {
                        String payStates = data.getStringExtra(SPUtils.EXTRA_DATA);
                        if (!TextUtils.isEmpty(payStates)) {
                            if (payStates.equals(PayStatus.PAYOK) || payStates.equals(PayStatus.PAYING)) {
                                //付款成功 直接从新拉详情数据
                                getOrderDetails();
                            }
                        }
                    }
                    break;
                case IntentConstants.REQUEST_CODE_COMMENT:
                    getOrderDetails();
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.ll_merhchant_im) {
            //TODO 联系商家
//            if (orderDetail != null) {
//                if (orderDetail.mainOrder != null && orderDetail.mainOrder.merchantInfo != null &&
//                        orderDetail.mainOrder.merchantInfo.userInfo != null &&
//                        orderDetail.mainOrder.merchantInfo.userInfo.userId == 0) {
//                    ToastUtil.showToast(MyBaseOrderDetailsActivity.this, "商户id为0");
//                    return;
//                }
//                tcEvent(orderDetail);
//                NavUtils.gotoMessageActivity(MyBaseOrderDetailsActivity.this,
//                        (int) orderDetail.mainOrder.merchantInfo.userInfo.userId,
//                        orderDetail.mainOrder.bizOrder.bizOrderId);
//            }
//            NavUtils.gotoMessageActivity(MyBaseOrderDetailsActivity.this, SPUtils.getServiceUID(this));
            NavUtils.gotoMessageActivity(MyBaseOrderDetailsActivity.this,
                    SPUtils.getServiceUID(this),
                    orderDetail.mainOrder.bizOrder.bizOrderId);
//            NavUtils.gotoMessageActivity(MyBaseOrderDetailsActivity.this, 1002);

        } else if (id == R.id.ll_merhchant_hotline) {
//            if (orderDetail == null || TextUtils.isEmpty(orderDetail.mainOrder.servicePhone))
//                return;
//            tcEvent(2);
//            if (StringUtil.isEmpty(orderDetail.mainOrder.merchantInfo.serviceTel)) {
//                LocalUtils.call(MyBaseOrderDetailsActivity.this, SPUtils.getServicePhone(this));
//                return;
//            }
//            LocalUtils.call(MyBaseOrderDetailsActivity.this, orderDetail.mainOrder.merchantInfo.serviceTel);
            LocalUtils.call(MyBaseOrderDetailsActivity.this, SPUtils.getServicePhone(this));

        }/*else if (id == R.id.ll_custom_service) {
            if (SPUtils.getServiceUID(this) > 0) {
                tcEvent(1);
                NavUtils.gotoMessageActivity(MyBaseOrderDetailsActivity.this, (int) SPUtils.getServiceUID(this));
            } else {
                ToastUtil.showToast(this, getString(R.string.label_toast_no_config_service_uid));
            }
        } else if (id == R.id.ll_hotline) {
            if (orderDetail == null || TextUtils.isEmpty(orderDetail.mainOrder.servicePhone))
                return;
            tcEvent(2);
            LocalUtils.call(MyBaseOrderDetailsActivity.this, SPUtils.getServicePhone(this));
        }*/
    }

    private void tcEvent(int i) {
        Map map = new HashMap();
        map.put(AnalyDataValue.KEY_BID, orderDetail.mainOrder.bizOrder.bizOrderId + "");
        switch (i) {
            case 1:
                map.put(AnalyDataValue.KEY_TYPE, "1");
                TCEventHelper.onEvent(this, AnalyDataValue.ORDER_DETAIL_SERVICE, map);
                break;
            case 2:
                map.put(AnalyDataValue.KEY_TYPE, "2");
                TCEventHelper.onEvent(this, AnalyDataValue.ORDER_DETAIL_SERVICE, map);
                break;
        }

    }

    /**
     * 添加skulist的view
     *
     * @param layout
     * @param detailOrders
     */
    public void addSkuData(LinearLayout layout, List<TmDetailOrder> detailOrders) {
        if (detailOrders == null) return;
        for (int i = 0; i < detailOrders.size(); i++) {
            TmDetailOrder order = detailOrders.get(i);
            String orderType = order.bizOrder.orderType;
            String key = getString(R.string.activity_order_number);
            if (orderType.equals(ValueConstants.ORDER_TYPE_TOUR_LINE) || orderType.equals(ValueConstants.ORDER_TYPE_FREE_LINE) || orderType.equals(ValueConstants.ORDER_TYPE_TOUR_LINE_ABOARD) || orderType.equals(ValueConstants.ORDER_TYPE_FREE_LINE_ABOARD))
                key = order.personType;
            layout.addView(createOrderDetailsItemView(key, StringUtil.converRMb2YunWithFlag(MyBaseOrderDetailsActivity.this, order.itemPrice) + " × " + order.bizOrder.buyAmount));

        }
    }

    /**
     * 创建订单详情 item
     *
     * @param key   显示键值
     * @param value 显示内容
     * @return OrderDetailsItemView
     */
    public OrderDetailsItemView createOrderDetailsItemView(String key, String value) {
        return new OrderDetailsItemView(this, key, value);
    }
}
