package com.quanyan.yhy.ui.order;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.util.DateUtil;
import com.quanyan.base.util.MD5Utils;
import com.quanyan.base.util.StringUtil;
import com.quanyan.base.view.customview.pulltorefresh.PullToRefreshBase;
import com.quanyan.base.view.customview.pulltorefresh.PullToRefreshScrollView;
import com.quanyan.yhy.BaseApplication;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.AnalyDataValue;
import com.quanyan.yhy.common.ItemType;
import com.quanyan.yhy.common.PayStatus;
import com.quanyan.yhy.common.WeiXinPayResult;
import com.quanyan.yhy.eventbus.GuangDaPaySuccessEvBus;
import com.quanyan.yhy.pay.PayController;
import com.quanyan.yhy.pay.alipay.PayResult;
import com.quanyan.yhy.service.controller.OrderController;
import com.quanyan.yhy.ui.base.utils.DialogUtil;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.base.utils.TCEventHelper;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.scenichoteldetail.PayTypeUtil;
import com.quanyan.yhy.ui.wallet.WalletUtils;
import com.quanyan.yhy.ui.wallet.view.WalletDialog;
import com.quanyan.yhy.ui.wallet.view.gridpasswordview.GridPasswordView;
import com.quanyan.yhy.wxapi.WeichatPayTask;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.yhy.common.beans.net.model.param.WebParams;
import com.yhy.common.beans.net.model.paycore.CebCloudPayInfo;
import com.yhy.common.beans.net.model.paycore.EleAccountInfo;
import com.yhy.common.beans.net.model.paycore.ElePursePayParam;
import com.yhy.common.beans.net.model.paycore.PayCoreBaseResult;
import com.yhy.common.beans.net.model.paycore.PcPayStatusInfo;
import com.yhy.common.beans.net.model.paycore.WxPayInfo;
import com.yhy.common.beans.net.model.tm.TmCreateOrderResultTO;
import com.yhy.common.beans.net.model.tm.TmDetailOrder;
import com.yhy.common.beans.net.model.tm.TmLogisticsOrder;
import com.yhy.common.beans.net.model.tm.TmMainOrder;
import com.yhy.common.beans.net.model.tm.TmOrderDetail;
import com.yhy.common.beans.net.model.tm.TmPayInfo;
import com.yhy.common.beans.net.model.tm.TmPayStatusInfo;
import com.yhy.common.beans.net.model.trip.HotelItem;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.types.AppDebug;
import com.yhy.common.utils.CommonUtil;
import com.yhy.common.utils.SPUtils;
import com.yhy.imageloader.ImageLoadManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Created with Android Studio.
 * Title:OrderConfigActivity
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-3-3
 * Time:9:57
 * Version 1.0
 * Description:活动订单确认界面
 */
public class OrderConfigActivity extends BaseActivity {
    private static final int PAYSUCCESS = 0x12345;

    protected boolean isPayComplete = false;//是否支付过

    @ViewInject(R.id.view_order_bottom)
    private OrderBottomTabView view_order_bottom;

    @ViewInject(R.id.view_orderpay)
    private OrderPayView mView_orderpay;

    @ViewInject(R.id.include_order_config)
    private View mInclude_order_config;

    @ViewInject(R.id.ll_orderconfig_message)
    private LinearLayout mLl_orderconfig_message;

    private List<OrderConfig> orderConfigs;

    private LayoutInflater mInflater;

    @ViewInject(R.id.ll_orderconfig_updown)
    private RelativeLayout mLl_orderconfig_updown;

    @ViewInject(R.id.tv_orderconfig_totalprice)
    private TextView mTotalPaView;

    @ViewInject(R.id.tv_orderconfig_original_totalprice)
    private TextView mOriginalTotalPaView;

    @ViewInject(R.id.ll_original_content)
    private LinearLayout mOriginalContent;

    @ViewInject(R.id.tv_orderconfig_updown)
    private TextView mTv_orderconfig_updown;
    @ViewInject(R.id.pull_image)
    private ImageView pull_image;

    @ViewInject(R.id.ptrolv_order_config)
    private PullToRefreshScrollView mLvOrderConfig;

    //订单详情
    private TmCreateOrderResultTO mCreateOrderResult;
    private String mItemType;

    @ViewInject(R.id.tv_orderconfig_tips)
    private TextView mOrderTipsView;

    @ViewInject(R.id.in_address)
    private View mAdress;

    /**
     * 跳转到收银台页面
     *
     * @param context
     */
    private PayController payController;


    private Dialog mNoticeDialog;

    private long mStartTimeMill;
    private long mEndTimeMill;

    private OrderTopView mOrderTopView;

    private String scenicName;

    private OrderController mOrderController;
    private long mOrderId;

    private boolean isAuth = false;
    private String type;
    private long accountBalance = 0;
    WeichatPayTask mWeichatPayTask;
    private Dialog mWalletDialog;
    private long actualTotalFee = 0;
    private Dialog mPayDialog;
    EleAccountInfo mEleAccountInfo;
    private boolean isPerson = false;
    private boolean isCompany = false;

    public static void gotoActivitysOrderConfigActivity(Context context, HotelItem hotelItem, long startTimeMill, long endTimeMill) {
        Intent intent = new Intent(context, OrderConfigActivity.class);
        intent.putExtra("hotelItem", hotelItem);
        context.startActivity(intent);
    }

    public static void gotoActivitysOrderConfigActivity(Context context, String type, TmCreateOrderResultTO result, String payType, long startTimeMill, long endTimeMill) {
        Intent intent = new Intent(context, OrderConfigActivity.class);
        if (result != null) {
            intent.putExtra(SPUtils.EXTRA_DATA, result);
        }
        if (!StringUtil.isEmpty(type)) {
            intent.putExtra(SPUtils.EXTRA_TYPE, type);
        }
        intent.putExtra("payType", payType);
        intent.putExtra("startTime", startTimeMill);
        intent.putExtra("endTime", endTimeMill);
        context.startActivity(intent);
    }


    /**
     * 跳转到收银台页面
     *
     * @param context
     */
    public static void gotoActivitysOrderConfigActivity(Context context, String type, TmCreateOrderResultTO result, long startTimeMill, long endTimeMill, String scenicName) {
        Intent intent = new Intent(context, OrderConfigActivity.class);
        if (result != null) {
            intent.putExtra(SPUtils.EXTRA_DATA, result);
        }
        if (!StringUtil.isEmpty(type)) {
            intent.putExtra(SPUtils.EXTRA_TYPE, type);
        }
        if (!StringUtil.isEmpty(scenicName)) {
            intent.putExtra("scenicName", scenicName);
        }
        intent.putExtra("startTime", startTimeMill);
        intent.putExtra("endTime", endTimeMill);
        context.startActivity(intent);
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.activity_activitysorderconfig, null);
    }

    @Override
    public View onLoadNavView() {
        mOrderTopView = new OrderTopView(this);
        mOrderTopView.setOrderTopTitle(getResources().getString(R.string.order_config_title));
        return mOrderTopView;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ViewUtils.inject(this);

        if (mWeichatPayTask == null) {
            mWeichatPayTask = new WeichatPayTask(this);
        }
        mLvOrderConfig.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mCreateOrderResult = (TmCreateOrderResultTO) getIntent().getSerializableExtra(SPUtils.EXTRA_DATA);
        mItemType = getIntent().getStringExtra(SPUtils.EXTRA_TYPE);
        if (mItemType.equals(ItemType.SPOTS)) {
            scenicName = getIntent().getStringExtra("scenicName");
        }
        mStartTimeMill = getIntent().getLongExtra("startTime", -1);
        mEndTimeMill = getIntent().getLongExtra("endTime", -1);
        payController = new PayController(this, mHandler);
        mOrderController = new OrderController(this, mHandler);

        String tips = getString(R.string.dlg_msg_cancel_pay_for_order);
        if (ItemType.POINT_MALL.equals(mItemType) || ItemType.NORMAL.equals(mItemType)) {
            tips = getString(R.string.dlg_msg_cancel_pay_for_point_order);
        }
        mNoticeDialog = DialogUtil.showMessageDialog(this,
                "提示",
                tips,
                getString(R.string.dlg_btn_label_cancel),
                getString(R.string.dlg_btn_label_payfor), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mNoticeDialog.dismiss();
                        OrderConfigActivity.this.finish();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mNoticeDialog.dismiss();
                    }
                });
        mNoticeDialog.setCanceledOnTouchOutside(true);
        mInflater = LayoutInflater.from(this);
        view_order_bottom.setSubmitText(getResources().getString(R.string.order_config_submit_text));
        mInclude_order_config.setVisibility(View.GONE);
        mTv_orderconfig_updown.setText(getResources().getString(R.string.order_arrow_down));

        if (mCreateOrderResult != null && mCreateOrderResult.mainOrder != null && mCreateOrderResult.mainOrder.bizOrder != null) {
            mOrderId = mCreateOrderResult.mainOrder.bizOrder.bizOrderId;
            //doNetDetail();
            actualTotalFee = mCreateOrderResult.mainOrder.bizOrder.actualTotalFee;
        }

        if (mCreateOrderResult != null) {
            setConfigMessage(mCreateOrderResult.mainOrder);
            setConfigView();
            setInfo(mCreateOrderResult.mainOrder, mCreateOrderResult.logisticsOrder);
        }

        EventBus.getDefault().register(this);

        initClick();
    }

    private void doNetDetail() {
        if (mOrderId != 0) {
            mOrderController.doGetOrderDetails(this, mOrderId);
        }
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        mLvOrderConfig.onRefreshComplete();
        switch (msg.what) {
            //支付宝处理结果
            case ValueConstants.SDK_PAY_FLAG: {
                PayResult payResult = new PayResult((String) msg.obj);
                // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                String resultStatus = payResult.getResultStatus();
                // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                if (TextUtils.equals(resultStatus, PayStatus.PAYOK)) {//成功
                    showLoadingView("正在确认支付结果，请稍候...");
                    mHandler.sendEmptyMessageDelayed(PAYSUCCESS, 2000);
                } else {
                    // 判断resultStatus 为非“9000”则代表可能支付失败
                    // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                    if (TextUtils.equals(resultStatus, PayStatus.PAYING)) {//正在处理中
//                        Toast.makeText(OrderConfigActivity.this, getString(R.string.pay_ing),
//                                Toast.LENGTH_SHORT).show();
//                        if (mCreateOrderResult.mainOrder.bizOrder.orderType.equals(ValueConstants.ORDER_TYPE_NORMAL) ||
//                                mCreateOrderResult.mainOrder.bizOrder.orderType.equals(ItemType.POINT_MALL)) {
//                            NavUtils.gotoMyOrderAllListActivity(this);
//                        } else {
//                            NavUtils.gotoOrderListActivity(OrderConfigActivity.this, getType(mItemType), OrderController.CHILD_TYPE_PAID);
//                        }
                        NavUtils.gotoMyOrderAllListActivity(this);
                        finish();
                    } else {//支付失败
                        // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
//                        Toast.makeText(OrderConfigActivity.this, getString(R.string.pay_failed),
//                                Toast.LENGTH_SHORT).show();
//                        if (mCreateOrderResult.mainOrder.bizOrder.orderType.equals(ValueConstants.ORDER_TYPE_NORMAL) ||
//                                mCreateOrderResult.mainOrder.bizOrder.orderType.equals(ItemType.POINT_MALL)) {
//                            NavUtils.gotoMyOrderAllListActivity(this);
//                        } else {
//                            NavUtils.gotoOrderListActivity(OrderConfigActivity.this, getType(mItemType), OrderController.CHILD_TYPE_PREPARE);
//                        }
                        NavUtils.gotoMyOrderAllListActivity(this);
                        finish();
                    }
                }
                break;
            }

            case ValueConstants.MSG_GET_PAY_WEIXIN_OK://微信
                WxPayInfo mTmWxPayInfo = (WxPayInfo) msg.obj;
                if (mTmWxPayInfo.success) {
                    mWeichatPayTask.payByWeiChat(mTmWxPayInfo);
                } else {
                    ToastUtil.showToast(OrderConfigActivity.this, mTmWxPayInfo.errorMsg);
                }

                break;
            case ValueConstants.MSG_GET_PAY_WEIXIN_KO:
                ToastUtil.showToast(this, StringUtil.handlerErrorCode(this, msg.arg1));
                break;
            case ValueConstants.MSG_GET_PAY_WEIXINSTATUS_OK://服务器返回状态成功
                TmPayStatusInfo wxPayStatuInfo = (TmPayStatusInfo) msg.obj;
                handleWxPayStatu(wxPayStatuInfo);
                break;
            case ValueConstants.MSG_CLUB_DETAIL_INFO_OK://成功
                TmPayInfo payInfo = (TmPayInfo) msg.obj;
                handleData(payInfo);
                break;
            case ValueConstants.MSG_CLUB_DETAIL_INFO_KO:
                ToastUtil.showToast(this, StringUtil.handlerErrorCode(this, msg.arg1));
                break;
            case PAYSUCCESS:
                hideLoadingView();
                isPayComplete = true;
                NavUtils.gotoMyOrderAllListActivity(this);
                finish();
                break;
            case ValueConstants.GET_ORDER_DETAILS_SUCCESS:
                if (msg.obj instanceof TmOrderDetail) {
                    TmOrderDetail tmOrderDetail = (TmOrderDetail) msg.obj;
                    if (tmOrderDetail != null) {
                        handleDetailData(tmOrderDetail);
                    }
                }

                break;
            case ValueConstants.GET_ORDER_DETAILS_FAIL:
                ToastUtil.showToast(this, StringUtil.handlerErrorCode(this, msg.arg1));
                break;

            case ValueConstants.PAY_GETELEACCOUNTINFO_SUCCESS:
                EleAccountInfo info = (EleAccountInfo) msg.obj;
                handMsg(info);
                break;
            case ValueConstants.PAY_GETELEACCOUNTINFO_ERROR:
//                ToastUtil.showToast(WalletActivity.this, StringUtil.handlerErrorCode(WalletActivity.this, msg.arg1));
                break;

            case ValueConstants.PAY_ElePursePay_SUCCESS://钱包支付成功
                hideLoadingView();
                PayCoreBaseResult payCoreBaseResult = (PayCoreBaseResult) msg.obj;
                if (payCoreBaseResult != null) {
                    if (payCoreBaseResult.success) {
                        showLoadingView("正在确认支付结果，请稍候...");
                        mHandler.sendEmptyMessageDelayed(PAYSUCCESS, 2000);
                    } else {
                        if (payCoreBaseResult.errorCode.equals(WalletUtils.PAY_PWD_ERROR)) {//密码错误
                            showPassErrorDialog("支付密码错误，请重试");
                        } else if (payCoreBaseResult.errorCode.equals(WalletUtils.PAY_PWD_MORE_THAN_MAXIMUM_RETRIES)) {//超过最大输入次数
                            showSurPassErrorDialog("支付密码输入错误过多账户已被锁定，请点击忘记密码进行找回或10分钟后重试");
                        } else {
                            showLoadingView("正在确认支付结果，请稍候...");
                            mHandler.sendEmptyMessageDelayed(PAYSUCCESS, 2000);
                        }
                    }
                } else {
                    showLoadingView("正在确认支付结果，请稍候...");
                    mHandler.sendEmptyMessageDelayed(PAYSUCCESS, 2000);
                }
                break;
            case ValueConstants.PAY_ElePursePay_ERROR:////钱包支付失败
//                ToastUtil.showToast(OrderConfigActivity.this, StringUtil.handlerErrorCode(OrderConfigActivity.this, msg.arg1));
                hideLoadingView();
                showLoadingView("正在确认支付结果，请稍候...");
                mHandler.sendEmptyMessageDelayed(PAYSUCCESS, 2000);
                break;
            case ValueConstants.PAY_GetCebCloudPayInfo_SUCCESS://获取光大银行快捷支付URL
                hideLoadingView();
                CebCloudPayInfo cebCloudPayInfo = (CebCloudPayInfo) msg.obj;
                if (cebCloudPayInfo != null) {
                    if (!TextUtils.isEmpty(cebCloudPayInfo.payInfo)) {
                        WebParams params = new WebParams();
                        params.setUrl(cebCloudPayInfo.payInfo);
                        params.setTitle("支付");
                        NavUtils.openBrowser(OrderConfigActivity.this, params);
                    } else {
//                        ToastUtil.showToast(OrderConfigActivity.this, "支付失败");
                        showLoadingView("正在确认支付结果，请稍候...");
                        mHandler.sendEmptyMessageDelayed(PAYSUCCESS, 2000);
                    }
                } else {
//                    ToastUtil.showToast(OrderConfigActivity.this, "支付失败");
                    showLoadingView("正在确认支付结果，请稍候...");
                    mHandler.sendEmptyMessageDelayed(PAYSUCCESS, 2000);
                }
                break;
            case ValueConstants.PAY_GetCebCloudPayInfo_ERROR:
                hideLoadingView();
                ToastUtil.showToast(OrderConfigActivity.this, StringUtil.handlerErrorCode(OrderConfigActivity.this, msg.arg1));
                break;
            case ValueConstants.PAY_GetPayStatusInfo_SUCCESS:
                hideLoadingView();
                PcPayStatusInfo payStatus = (PcPayStatusInfo) msg.obj;
//                if (payStatus != null) {
//                    if (payStatus.payStatus == 10000 && payStatus.success) {
//                        ToastUtil.showToast(OrderConfigActivity.this, "支付成功");
//                    } else {
//                        ToastUtil.showToast(OrderConfigActivity.this, "支付失败");
//                    }
//                } else {
//                    ToastUtil.showToast(OrderConfigActivity.this, "支付失败");
//                }
                showLoadingView("正在确认支付结果，请稍候...");
                mHandler.sendEmptyMessageDelayed(PAYSUCCESS, 2000);
                break;
            case ValueConstants.PAY_GetPayStatusInfo_ERROR:
//                ToastUtil.showToast(OrderConfigActivity.this, StringUtil.handlerErrorCode(OrderConfigActivity.this, msg.arg1));
                hideLoadingView();
                showLoadingView("正在确认支付结果，请稍候...");
                mHandler.sendEmptyMessageDelayed(PAYSUCCESS, 2000);
                break;
            case 0x110:
                ElePursePayParam params = (ElePursePayParam) msg.obj;
                payController.doElePursePay(params, OrderConfigActivity.this);
                if (mPayDialog != null && mPayDialog.isShowing()) {
                    mPayDialog.dismiss();
                }
                showLoadingView("正在支付，请稍候...");
                break;
        }
    }

    //刷新的数据处理
    private void handleDetailData(TmOrderDetail tmOrderDetail) {
        setConfigMessage(tmOrderDetail.mainOrder);
        setConfigView();
        setInfo(tmOrderDetail.mainOrder, tmOrderDetail.logisticsOrder);
    }

    private void handleData(TmPayInfo payInfo) {
        if (payInfo == null) {
            ToastUtil.showToast(this, getString(R.string.pay_backnull));
            return;
        }
        if (StringUtil.isEmpty(payInfo.payInfo)) {
            ToastUtil.showToast(OrderConfigActivity.this, getString(R.string.pay_dataerror));
            return;
        }
        alipay(payInfo.payInfo);
    }

    //start======================支付宝支付相关代码============//
     /*
     * 支付宝支付
     *
     * @param payInfo
     */
    private void alipay(final String payInfo) {
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(OrderConfigActivity.this);
                // 调用支付接口，获取支付结果
//                String result = alipay.pay(payInfo);
//
//                Message msg = new Message();
//                msg.what = ValueConstants.SDK_PAY_FLAG;
//                msg.obj = result;
//                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    private void setInfo(final TmMainOrder tmMainOrder, TmLogisticsOrder tmLogisticsOrder) {
        if (tmMainOrder.detailOrders != null) {
            TmDetailOrder detailOrder = tmMainOrder.detailOrders.get(0);
            String info1 = String.format(getString(R.string.label_buy_amount), tmMainOrder.bizOrder.buyAmount);

            if (ItemType.FREE_LINE.equals(mItemType) || ItemType.TOUR_LINE.equals(mItemType) ||
                    ItemType.FREE_LINE_ABOARD.equals(mItemType) || ItemType.TOUR_LINE_ABOARD.equals(mItemType)) {
                ((TextView) findViewById(R.id.sa_tv_good_name)).setText(detailOrder.itemTitle);
                String info2 = String.format(getString(R.string.label_buy_content_thumbmail), detailOrder.packageType);
                String info3 = String.format(getString(R.string.label_buy_time), DateUtil.convert2String(tmMainOrder.departTime, "yyyy-MM-dd"));
                ((TextView) findViewById(R.id.sa_tv_good_info_1)).setText(info1 + "    " + info3);
                ((TextView) findViewById(R.id.sa_tv_good_info_2)).setText(info2);
            } else if (ItemType.CITY_ACTIVITY.equals(mItemType)) {
                ((TextView) findViewById(R.id.sa_tv_good_name)).setText(detailOrder.itemTitle);
                String info2 = String.format(getString(R.string.label_buy_content_thumbmail), detailOrder.activityContent);
                String info3 = String.format(getString(R.string.label_buy_time), detailOrder.activityTime);
                ((TextView) findViewById(R.id.sa_tv_good_info_1)).setText(info1 + "    " + info3);
                ((TextView) findViewById(R.id.sa_tv_good_info_2)).setText(info2);
            } else if (ItemType.HOTEL.equals(mItemType)) {
                ((TextView) findViewById(R.id.sa_tv_good_name)).setText(tmMainOrder.hotelTitle);
                //ONLINE:在线付 OFFLINE:线下付
                if (PayTypeUtil.ONLINE_PAY.equals(tmMainOrder.payType)) {
                    mView_orderpay.setVisibility(View.VISIBLE);
                    mOrderTipsView.setText(getResources().getString(R.string.label_hint_cancel_order));
                } else if (PayTypeUtil.OFFLINE_PAY.equals(tmMainOrder.payType)) {
                    mOrderTopView.setOrderTopTitle(getResources().getString(R.string.order_config_offline_title));
                    mView_orderpay.setVisibility(View.GONE);
                    mOrderTipsView.setText(getResources().getString(R.string.hotel_order_tips));
                    view_order_bottom.setSubmitText("返回首页");
                    view_order_bottom.showleftImg("订单详情");
                    view_order_bottom.setOnLeftClick(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // TODO: 16/5/28 查看订单详情
                            NavUtils.gotoOrderDetailsActivity(OrderConfigActivity.this, getType(mItemType), tmMainOrder.bizOrder.bizOrderId);
                        }
                    });
                    view_order_bottom.setonRighClick(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // TODO: 16/5/28 返回首页
                            ((BaseApplication) getApplicationContext()).gotoGonaActivity();
                        }
                    });
                }
                String info2 = tmMainOrder.roomTitle + ":  " + tmMainOrder.roomAmount + "间";
                String info3 = String.format(getString(R.string.hotel_order_time), DateUtil.convert2String(mStartTimeMill, "yyyy.MM.dd") + " - " + DateUtil.convert2String(mEndTimeMill, "yyyy.MM.dd"));
                ((TextView) findViewById(R.id.sa_tv_good_info_1)).setText(info2);
                ((TextView) findViewById(R.id.sa_tv_good_info_2)).setText(info3);
            } else if (ItemType.SPOTS.equals(mItemType)) {
                ((TextView) findViewById(R.id.sa_tv_good_name)).setText(scenicName + "(" + tmMainOrder.ticketTitle + ")");
                String info2 = String.format(getString(R.string.scenic_order_buycount), detailOrder.bizOrder.buyAmount) + "    " + String.format(getString(R.string.scenic_order_entertime), DateUtil.convert2String(tmMainOrder.scenicEnterTime, "yyyy-MM-dd"));
//                String info3 = String.format(getString(R.string.scenic_order_combo), detailOrder.packageType);
                ((TextView) findViewById(R.id.sa_tv_good_info_1)).setText(info2);
                ((TextView) findViewById(R.id.sa_tv_good_info_2)).setText("");
            } else if (ItemType.POINT_MALL.equals(mItemType) || ItemType.NORMAL.equals(mItemType)) {//积分商城
                mOrderTipsView.setText(getResources().getString(R.string.label_hint_cancel_point_order));
                ((TextView) findViewById(R.id.sa_tv_good_name)).setText(detailOrder.itemTitle);
                ((TextView) findViewById(R.id.sa_tv_good_info_1)).setText("");
                ((TextView) findViewById(R.id.sa_tv_good_info_3)).setVisibility(View.VISIBLE);
                ((TextView) findViewById(R.id.sa_tv_good_info_3)).setText(StringUtil.converRMb2YunWithFlag(OrderConfigActivity.this, detailOrder.itemPrice));
                ((TextView) findViewById(R.id.sa_tv_good_info_2)).setText("数量：" + detailOrder.bizOrder.buyAmount);
                if (tmLogisticsOrder != null) {
                    mAdress.setVisibility(View.VISIBLE);
                    ((TextView) findViewById(R.id.tv_rcadress_tel)).setText(tmLogisticsOrder.mobilePhone);
                    ((TextView) findViewById(R.id.tv_rcadress_name)).setText(tmLogisticsOrder.fullName);
                    String province = "";
                    String city = "";
                    String area = "";
                    //详细地址
                    if (!StringUtil.isEmpty(tmLogisticsOrder.address)) {
                        if (!StringUtil.isEmpty(tmLogisticsOrder.prov)) {
                            province = tmLogisticsOrder.prov;
                        }
                        if (!StringUtil.isEmpty(tmLogisticsOrder.city) && !tmLogisticsOrder.city.equals(tmLogisticsOrder.prov)) {
                            city = tmLogisticsOrder.city;
                        } else {
                            city = "";
                        }
                        if (!StringUtil.isEmpty(tmLogisticsOrder.area)) {
                            area = tmLogisticsOrder.area;
                        }
                        String newAddress = province + city + area + tmLogisticsOrder.address;
                        ((TextView) findViewById(R.id.tv_rcadress_adress)).setText(newAddress);
                    }
                }
            }

//            BaseImgView.loadimg(((ImageView) findViewById(R.id.sa_iv_good)), detailOrder.itemPic,
//                    R.mipmap.icon_default_150_150,
//                    R.mipmap.icon_default_150_150,
//                    R.mipmap.icon_default_150_150,
//                    ImageScaleType.EXACTLY,
//                    150,
//                    150,
//                    0);
            ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(detailOrder.itemPic), R.mipmap.icon_default_150_150, 150, 150, (ImageView) findViewById(R.id.sa_iv_good));

        }


        ((TextView) findViewById(R.id.tv_orderconfig_totalprice)).setText(
                StringUtil.converRMb2YunWithFlag(OrderConfigActivity.this, tmMainOrder.totalFee)
        );
        if (!ItemType.HOTEL.equals(mItemType)) {
            view_order_bottom.setBottomPrice(StringUtil.converRMb2YunWithFlag(OrderConfigActivity.this, tmMainOrder.totalFee));
        }

    }


//    private void initView() {
//
//    }

    private void initClick() {

        mOrderTopView.setBackClickListener(new OrderTopView.BackClickListener() {
            @Override
            public void clickBack() {
                back();
            }
        });

        view_order_bottom.setSubmitClickListener(new OrderBottomTabView.SubmitClick() {
            @Override
            public void submit() {
                //Just for monkey test disable pay
                if (AppDebug.IS_MONKEY_TEST) {
                    return;
                }

                if (mCreateOrderResult.mainOrder.bizOrder.actualTotalFee <= 0) {
                    ToastUtil.showToast(OrderConfigActivity.this, getResources().getString(R.string.pay_failed));
                    return;
                }


                switch (mView_orderpay.getSelectedIndex()) {
                    case 1:
                        //alipay
                        Map<String, String> stringMap1 = new HashMap<String, String>();
                        stringMap1.put(AnalyDataValue.KEY_PAY_WAY, getString(R.string.pay_eventzfb));
                        stringMap1.put(AnalyDataValue.KEY_ORDER_ID, String.valueOf(mCreateOrderResult.mainOrder.bizOrder.bizOrderId));
                        TCEventHelper.onEvent(OrderConfigActivity.this, AnalyDataValue.CONFIRM_PAYMENT, stringMap1);
                        payController.getPayInfo(OrderConfigActivity.this, mCreateOrderResult.mainOrder.bizOrder.bizOrderId, PayStatus.PAYTYPE_ZFB);
                        break;
               /*     case 1:


                        payController.doGetWxBatchPayInfo(OrderConfigActivity.this, new long[]{mCreateOrderResult.mainOrder.bizOrder.bizOrderId});
                        break;*/
                    case 0://钱包支付
//                        Map<String, String> stringMap2 = new HashMap<String, String>();
//                        stringMap2.put(AnalyDataValue.KEY_PAY_WAY, getString(R.string.pay_eventzfb));
//                        stringMap2.put(AnalyDataValue.KEY_ORDER_ID, String.valueOf(mCreateOrderResult.mainOrder.bizOrder.bizOrderId));
//                        TCEventHelper.onEvent(OrderConfigActivity.this, AnalyDataValue.CONFIRM_PAYMENT, stringMap2);


                        // 测试要求改
                        getUserPayMsg();
                       /* if (isPerson) {
                            if (!isAuth) {
                                mWalletDialog = WalletDialog.showIdentificationDialog(OrderConfigActivity.this, type);
                            } else {
                                if (accountBalance < actualTotalFee) {
                                    showNoBalanceDialog("钱包余额不足，请充值后再支付");
                                } else {
                                    mPayDialog = showOrderPayDialog(OrderConfigActivity.this, actualTotalFee, mOrderId);
                                }
                            }
                        } else {
                            if (isCompany) {
                                ToastUtil.showToast(OrderConfigActivity.this, "您的帐号暂不支持该功能");
                            } else {
                                ToastUtil.showToast(OrderConfigActivity.this, "您的帐号暂不支持该功能");
                            }
                        }*/
                        break;

                    case 2:
                       /* if (isPerson) {
                            //请求光大银行的链接
                            payController.doGetCebCloudPayInfo(mOrderId, null, WalletUtils.APP, OrderConfigActivity.this);
                            showLoadingView("");
                            if (!isAuth) {

                                mWalletDialog = WalletDialog.showIdentificationDialog(OrderConfigActivity.this, type);
                            } else {
                                //请求光大银行的链接
                                payController.doGetCebCloudPayInfo(mOrderId, null, WalletUtils.APP, OrderConfigActivity.this);
                                showLoadingView("");
                            }
                        } else {
                            if (isCompany) {
                                ToastUtil.showToast(OrderConfigActivity.this, "您的帐号暂不支持该功能");
                            } else {
                                ToastUtil.showToast(OrderConfigActivity.this, "您的帐号暂不支持该功能");
                            }
                        }*/

                        //请求光大银行的链接
                        payController.doGetCebCloudPayInfo(mOrderId, null, WalletUtils.APP, OrderConfigActivity.this);
                        showLoadingView("");
                        break;
                }


            }
        });

        mLl_orderconfig_updown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInclude_order_config.getVisibility() == View.GONE) {
                    mInclude_order_config.setVisibility(View.VISIBLE);
                    pull_image.setImageResource(R.mipmap.arrow_up_icon);
                    mTv_orderconfig_updown.setText(getResources().getString(R.string.order_arrow_up));
                } else {
                    mInclude_order_config.setVisibility(View.GONE);
                    pull_image.setImageResource(R.mipmap.arrow_down_icon);
                    mTv_orderconfig_updown.setText(getResources().getString(R.string.order_arrow_down));
                }
            }
        });

        mLvOrderConfig.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                doNetDetail();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {

            }
        });

    }

    private void setConfigView() {
        mLl_orderconfig_message.removeAllViews();
        for (int i = 0; i < orderConfigs.size(); i++) {
            View view = mInflater.inflate(R.layout.hotelorerconfig_tour_item_layout, null);
            TextView name = (TextView) view.findViewById(R.id.order_addtourist_layout_title);
            TextView code = (TextView) view.findViewById(R.id.order_addtourist_layout_code);
            TextView send = (TextView) view.findViewById(R.id.order_addtourist_layout_second_name);
            if (mItemType.equals(ItemType.POINT_MALL) && i == 0) {
                if (!TextUtils.isEmpty(orderConfigs.get(i).getTitle())) {
                    name.setText(orderConfigs.get(i).getTitle() + "：");
                }
            } else {
                if (!TextUtils.isEmpty(orderConfigs.get(i).getTitle())) {
                    name.setText(orderConfigs.get(i).getTitle());
                }
            }

            if (mItemType.equals(ItemType.POINT_MALL) && i == 0) {
                if (!TextUtils.isEmpty(orderConfigs.get(i).getContent())) {
                    send.setText(orderConfigs.get(i).getContent());
                }
            } else {
                if (!TextUtils.isEmpty(orderConfigs.get(i).getContent())) {
                    code.setText(orderConfigs.get(i).getContent());
                }
            }

            if (!TextUtils.isEmpty(orderConfigs.get(i).getTitle())) {
                mLl_orderconfig_message.addView(view);
            }

        }
    }

    private void setConfigMessage(TmMainOrder tmMainOrder) {
        if (orderConfigs == null) {
            orderConfigs = new ArrayList<OrderConfig>();
        } else {
            orderConfigs.clear();
        }
        if (tmMainOrder != null && tmMainOrder.merchantInfo != null) {
            OrderConfig o1 = new OrderConfig();
            o1.setTitle("商户");
            if (tmMainOrder.merchantInfo.name != null) {
                o1.setContent(tmMainOrder.merchantInfo.name);
            } else {
                o1.setContent("");
            }
            orderConfigs.add(o1);
        }

        if (tmMainOrder != null && tmMainOrder.detailOrders != null && tmMainOrder.detailOrders.size() != 0) {
            for (int i = 0; i < tmMainOrder.detailOrders.size(); i++) {
                OrderConfig o2 = new OrderConfig();
                if (ItemType.FREE_LINE.equals(mItemType) || ItemType.TOUR_LINE.equals(mItemType) ||
                        ItemType.FREE_LINE_ABOARD.equals(mItemType) || ItemType.TOUR_LINE_ABOARD.equals(mItemType)) {
                    if (tmMainOrder.detailOrders.get(i).bizOrder != null) {
                        if (tmMainOrder.detailOrders.get(i).itemPrice > 0) {
                            if (!TextUtils.isEmpty(tmMainOrder.detailOrders.get(i).personType)) {
                                o2.setTitle(tmMainOrder.detailOrders.get(i).personType);
                            }
                            o2.setContent(StringUtil.converRMb2YunWithFlag(OrderConfigActivity.this, tmMainOrder.detailOrders.get(i).itemPrice) + " x " + tmMainOrder.detailOrders.get(i).bizOrder.buyAmount);
                        }
                    } else {
                        if (!TextUtils.isEmpty(tmMainOrder.detailOrders.get(i).personType)) {
                            o2.setTitle(tmMainOrder.detailOrders.get(i).personType);
                            o2.setContent(StringUtil.converRMb2YunWithFlag(OrderConfigActivity.this, tmMainOrder.detailOrders.get(i).itemPrice));
                        }
                    }

                } else if (ItemType.CITY_ACTIVITY.equals(mItemType)) {
                    o2.setTitle(getResources().getString(R.string.activity_order_number));

                    if (tmMainOrder.detailOrders.get(i).bizOrder != null) {
                        o2.setContent(StringUtil.converRMb2YunWithFlag(OrderConfigActivity.this, tmMainOrder.detailOrders.get(i).itemPrice) + " x " + tmMainOrder.detailOrders.get(i).bizOrder.buyAmount);
                    } else {
                        o2.setContent(StringUtil.converRMb2YunWithFlag(OrderConfigActivity.this, tmMainOrder.detailOrders.get(i).itemPrice));
                    }
                } else if (ItemType.SPOTS.equals(mItemType)) {
                    if (!TextUtils.isEmpty(tmMainOrder.detailOrders.get(i).itemTitle)) {
                        o2.setTitle(tmMainOrder.detailOrders.get(i).itemTitle + "(" + tmMainOrder.ticketTitle + ")");
                    }
                    if (tmMainOrder.detailOrders.get(i).bizOrder != null) {
                        o2.setContent(StringUtil.converRMb2YunWithFlag(OrderConfigActivity.this, tmMainOrder.detailOrders.get(i).itemPrice) + " x " + tmMainOrder.detailOrders.get(i).bizOrder.buyAmount);
                    } else {
                        o2.setContent(StringUtil.converRMb2YunWithFlag(OrderConfigActivity.this, tmMainOrder.detailOrders.get(i).itemPrice));
                    }
                } else if (ItemType.HOTEL.equals(mItemType)) {
                    if (!TextUtils.isEmpty(tmMainOrder.detailOrders.get(i).itemTitle)) {
                        o2.setTitle(tmMainOrder.detailOrders.get(i).itemTitle);
                    }

                    if (tmMainOrder.detailOrders.get(i).bizOrder != null) {
                        o2.setContent(StringUtil.converRMb2YunWithFlag(OrderConfigActivity.this, tmMainOrder.detailOrders.get(i).itemPrice) + " x " + tmMainOrder.detailOrders.get(i).bizOrder.buyAmount);
                    } else {
                        o2.setContent(StringUtil.converRMb2YunWithFlag(OrderConfigActivity.this, tmMainOrder.detailOrders.get(i).itemPrice));
                    }
                } else if (ItemType.POINT_MALL.equals(mItemType) || ItemType.NORMAL.equals(mItemType)) {
                    o2.setTitle("订单金额");

                    if (tmMainOrder.detailOrders.get(i).bizOrder != null) {
                        o2.setContent(StringUtil.converRMb2YunWithFlag(OrderConfigActivity.this, tmMainOrder.detailOrders.get(i).itemPrice) + " x " + tmMainOrder.detailOrders.get(i).bizOrder.buyAmount);
                    } else {
                        o2.setContent(StringUtil.converRMb2YunWithFlag(OrderConfigActivity.this, tmMainOrder.detailOrders.get(i).itemPrice));
                    }
                }

//                if (tmMainOrder.detailOrders.get(i).bizOrder != null) {
//                    o2.setContent(StringUtil.converRMb2YunWithFlag(OrderConfigActivity.this, tmMainOrder.detailOrders.get(i).itemPrice) + " x " + tmMainOrder.detailOrders.get(i).bizOrder.buyAmount);
//                } else {
//                    o2.setContent(StringUtil.converRMb2YunWithFlag(OrderConfigActivity.this, tmMainOrder.detailOrders.get(i).itemPrice));
//                }
                orderConfigs.add(o2);
            }
        }

        if (tmMainOrder.voucherDiscountFee > 0) {
            OrderConfig o = new OrderConfig();
            o.setTitle("优惠券");
            o.setContent("-" + StringUtil.converRMb2YunWithFlag(OrderConfigActivity.this, tmMainOrder.voucherDiscountFee));
            orderConfigs.add(o);
        }

        if (tmMainOrder.usePoint > 0) {
            OrderConfig o = new OrderConfig();
            o.setTitle("积分");
            o.setContent("-" + StringUtil.convertPriceNoSymbolExceptLastZeroWithFlag(OrderConfigActivity.this, tmMainOrder.usePoint));
            orderConfigs.add(o);
        }

        if (tmMainOrder != null && tmMainOrder.bizOrder != null) {
            view_order_bottom.setBottomPrice(StringUtil.converRMb2YunWithFlag(OrderConfigActivity.this, tmMainOrder.bizOrder.actualTotalFee));
            actualTotalFee = tmMainOrder.bizOrder.actualTotalFee;
            mTotalPaView.setText(StringUtil.converRMb2YunWithFlag(OrderConfigActivity.this, tmMainOrder.bizOrder.actualTotalFee));
            //改价前的价格
            mOriginalContent.setVisibility(View.GONE);
            if (tmMainOrder.bizOrder.originalActualTotalFee != tmMainOrder.bizOrder.actualTotalFee) {
                if (mOriginalContent.getVisibility() == View.GONE) {
                    mOriginalContent.setVisibility(View.VISIBLE);
                }
                mOriginalTotalPaView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                mOriginalTotalPaView.setText("改价前：" + StringUtil.converRMb2YunWithFlag(OrderConfigActivity.this, tmMainOrder.bizOrder.originalActualTotalFee));
            }
        }
    }

    class OrderConfig {
        private String title;
        private String content;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

    /**
     * 退出Activity
     */
    private void back() {
        if (!isPayComplete) {
            if (mCreateOrderResult != null) {
                if (ItemType.HOTEL.equals(mItemType)) {
                    if (PayTypeUtil.OFFLINE_PAY.equals(mCreateOrderResult.mainOrder.payType)) {
                        finish();
                    } else {
                        if (mNoticeDialog.isShowing()) {
                            mNoticeDialog.dismiss();
                        } else {
                            mNoticeDialog.show();
                        }
                    }
                } else {
                    if (mNoticeDialog.isShowing()) {
                        mNoticeDialog.dismiss();
                    } else {
                        mNoticeDialog.show();
                    }
                }
            } else {
                finish();
            }
        } else {
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            back();
        }
        return false;
    }


    private String getType(String pType) {
        if (ItemType.CITY_ACTIVITY.equals(pType)) {//城市活动
            return ValueConstants.ORDER_TYPE_CITY_ACTIVITY;
        } else if (ItemType.FREE_LINE.equals(pType) || ItemType.TOUR_LINE.equals(pType) ||
                ItemType.FREE_LINE_ABOARD.equals(pType) || ItemType.TOUR_LINE_ABOARD.equals(pType)) {//线路
            return ValueConstants.ORDER_TYPE_TRAVEL;
        } else if (ItemType.HOTEL.equals(pType)) {
            return ValueConstants.ORDER_TYPE_HOTEL;
        } else if (ItemType.SPOTS.equals(pType)) {
            return ValueConstants.ORDER_TYPE_SCENIC;
        } else {
            return null;
        }
    }

    //请求接口获取个人钱包信息
    private void getUserPayMsg() {
        payController.doGetEleAccountInfo(this);
    }

    //获取订单支付状态
    private void doGetPayStatusInfo(long bizID) {
        payController.doGetPayStatusInfo(bizID, this);
    }

    public boolean isUploadIdcard(EleAccountInfo info) {
        if ("NOT_UPLOAD".equals(info.idCardPhotoStatus)) {
            if ("YES".equals(info.isForcedUploadPhoto)) {
                showDialog(info, true);
                return false;
            } /*else {
                if (PrefUtil.getIsFirstStart(OrderConfigActivity.this)) {
                    showDialog(info, false);
                    PrefUtil.putIsFirstStart(OrderConfigActivity.this, false);
                }
            }*/

        }
        return true;
    }

    public void showDialog(final EleAccountInfo info, final boolean isFocus) {
        WalletDialog.showUptateIdentityDialog(this, info.uploadMsg, isFocus, new WalletDialog.IdentifyInterface() {
            @Override
            public void OnIdentifyConfirm() {
                NavUtils.gotoIdCardUploadActivity(OrderConfigActivity.this, info.userName);
            }

            @Override
            public void OnIdentifyCancel() {
        /*        if (isFocus) {
                    finish();
                }*/
            }
        });

    }

    private void handMsg(EleAccountInfo info) {

        if (info == null) {
            isPerson = true;
            isAuth = false;
            type = WalletUtils.OPEN_ELE_ACCOUNT;
        } else {
            if (!TextUtils.isEmpty(info.accountType)) {
                if (info.accountType.equals(WalletUtils.PERSON)) {
                    isPerson = true;
                    if (!TextUtils.isEmpty(info.status)) {
                        if (info.status.equals(WalletUtils.TACK_EFFECT)) {
                            if (info.existPayPwd) {
                                isAuth = true;
                            } else {
                                type = WalletUtils.SETUP_PAY_PWD;
                                isAuth = false;
                            }
                        } else {
                            type = WalletUtils.OPEN_ELE_ACCOUNT;
                            isAuth = false;
                        }
                        accountBalance = info.accountBalance;
                    } else {
                        type = WalletUtils.OPEN_ELE_ACCOUNT;
                        isAuth = false;
                    }
                } else if (info.accountType.equals(WalletUtils.COMPANY)) {
                    isCompany = true;
                } else {

                }
            }
        }

        if (isPerson) {
            if (!isAuth) {
                NavUtils.gotoRealNameAuthActivity(OrderConfigActivity.this, type,"");
             //   mWalletDialog = WalletDialog.showIdentificationDialog(OrderConfigActivity.this, type);
            } else {

                if (info != null) {
                    if(isUploadIdcard(info)){
                        if (accountBalance < actualTotalFee) {
                            showNoBalanceDialog("钱包余额不足，请充值后再支付");
                        } else {
                            mPayDialog = showOrderPayDialog(OrderConfigActivity.this, actualTotalFee, mOrderId);
                        }
                    }
                }

            }
        } else {
            if (isCompany) {
                ToastUtil.showToast(OrderConfigActivity.this, "企业用户暂不支持“钱包支付”，请选择其他支付方式");
            } else {
                ToastUtil.showToast(OrderConfigActivity.this, "您的帐号暂不支持该功能");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mWalletDialog != null && mWalletDialog.isShowing()) {
            mWalletDialog.dismiss();
        }
        // getUserPayMsg();
    }


    public void onEvent(GuangDaPaySuccessEvBus bus) {
        //TODO 光大银行支付完成后处理
        doGetPayStatusInfo(mOrderId);
        showLoadingView("正在确认支付状态，请稍候");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        mWeichatPayTask.destroyWx();
    }

    /**
     * 支付弹框
     *
     * @param context
     * @param sub
     * @param bizOrderId
     * @return
     */
    private Dialog showOrderPayDialog(final Context context, final long sub, final long bizOrderId) {
        LayoutInflater mLayoutInflater = LayoutInflater.from(context);
        View view = mLayoutInflater.inflate(R.layout.pay_pass_dialog, null);
        TextView tv_subtitle = (TextView) view.findViewById(R.id.tv_subtitle);
        tv_subtitle.setText("");
        final Dialog dialog = new Dialog(context, R.style.kangzai_dialog);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);

        final GridPasswordView mCustomUi = (GridPasswordView) view.findViewById(R.id.gpv_customUi);
        TextView mMoney = (TextView) view.findViewById(R.id.tv_money);
        mMoney.setText(StringUtil.formatWalletMoneyNoFlg(sub));
        mCustomUi.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
            @Override
            public void onTextChanged(String psw) {
                if (!TextUtils.isEmpty(psw) && psw.length() == 6) {
                    WalletUtils.hideSoft(context, mCustomUi.getWindowToken());
                    ElePursePayParam payParam = new ElePursePayParam();
                    payParam.bizOrderId = bizOrderId;
                    payParam.payPwd = MD5Utils.toMD5(psw);
                    payParam.sourceType = WalletUtils.APP;

                    Message msg = Message.obtain();
                    msg.what = 0x110;
                    msg.obj = payParam;
                    mHandler.sendMessageDelayed(msg, 500);
                }
            }

            @Override
            public void onInputFinish(String psw) {

            }
        });
        dialog.show();
        return dialog;
    }
    private String payStatus = PayStatus.PAYERROR;
    private int serverCount = 0;
    private static final int MAX_COUNT = 3;
    private static final int SEND_TIME = 1000;
    //处理服务器返回的微信支付状态


    private void finishActivity() {
        Intent intent = getIntent();
        intent.putExtra(SPUtils.EXTRA_DATA, payStatus);
        setResult(Activity.RESULT_OK, intent);
        finish();
        OrderConfigActivity.this.overridePendingTransition(R.anim.push_up_in2, R.anim.push_up_out2);
    }

    private void handleWxPayStatu(TmPayStatusInfo wxPayStatuInfo) {
        if (wxPayStatuInfo != null && wxPayStatuInfo.payStatus == PayStatus.PAYSERVEROK) {
            hideLoadingView();
            Toast.makeText(OrderConfigActivity.this, getString(R.string.pay_success), Toast.LENGTH_SHORT).show();
            payStatus = PayStatus.PAYOK;
            finishActivity();
        } else {
            //三次向服务器轮询获取结果
            if (serverCount < MAX_COUNT) {
                try {
                    Thread.sleep(SEND_TIME);
                    payController.getPayStatusInfo(OrderConfigActivity.this, mOrderId);
                    serverCount++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                payStatus = PayStatus.PAYING;
                hideLoadingView();
                finishActivity();
            }
        }

    }


    public void onEventMainThread(WeiXinPayResult result) {

        BaseResp resp = result.getResp();

        //微信支付处理code
        switch (resp.errCode) {

            case 0://微信支付成功
                showLoadingView("");
                payController.getPayStatusInfo(OrderConfigActivity.this, mOrderId);//请求服务器判断是否成功
                serverCount++;

                break;
            case -1://支付失败
                Toast.makeText(OrderConfigActivity.this, getString(R.string.pay_failed), Toast.LENGTH_SHORT).show();
                payStatus = PayStatus.PAYERROR;
                NavUtils.gotoMyOrderAllListActivity(this);
                finish();
                break;
            case -2://用户取消
                Toast.makeText(OrderConfigActivity.this, getString(R.string.pay_failed), Toast.LENGTH_SHORT).show();
                payStatus = PayStatus.PAYERROR;

                NavUtils.gotoMyOrderAllListActivity(this);
                finish();
                break;

            default:
                break;
        }


    }
    /**
     * 显示输入密码错误弹框
     */

    private Dialog mErrorDilog;

    private void showPassErrorDialog(String msg) {
        if (mErrorDilog == null) {
            mErrorDilog = DialogUtil.showMessageDialog(this,
                    null,
                    msg,
                    "重试",
                    "忘记密码",
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mErrorDilog != null) {
                                mErrorDilog.dismiss();
                            }
                            mPayDialog = showOrderPayDialog(OrderConfigActivity.this, actualTotalFee, mOrderId);
                        }
                    },
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mErrorDilog != null) {
                                mErrorDilog.dismiss();
                            }
                            NavUtils.gotoForgetPasSelectCardActivity(OrderConfigActivity.this);
                        }
                    });
        }
        mErrorDilog.show();
    }

    private Dialog mSurErrorDialog;

    private void showSurPassErrorDialog(String msg) {
        if (mSurErrorDialog == null) {
            mSurErrorDialog = DialogUtil.showMessageDialog(this,
                    null,
                    msg,
                    "取消",
                    "忘记密码",
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mSurErrorDialog != null) {
                                mSurErrorDialog.dismiss();
                            }
                        }
                    },
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mSurErrorDialog != null) {
                                mSurErrorDialog.dismiss();
                            }
                            NavUtils.gotoForgetPasSelectCardActivity(OrderConfigActivity.this);
                        }
                    });
        }
        mSurErrorDialog.show();
    }

    private Dialog mNoBalanceDialog;

    private void showNoBalanceDialog(String msg) {
        if (mNoBalanceDialog == null) {
            mNoBalanceDialog = DialogUtil.showMessageDialog(this,
                    null,
                    msg,
                    "充值",
                    "取消",
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mNoBalanceDialog != null) {
                                mNoBalanceDialog.dismiss();
                            }
                            NavUtils.gotoWalletActivity(OrderConfigActivity.this);
                        }
                    },
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mNoBalanceDialog != null) {
                                mNoBalanceDialog.dismiss();
                            }
                        }
                    });
        }
        mNoBalanceDialog.show();
    }

}
