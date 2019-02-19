package com.quanyan.yhy.ui.order;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.AnalyDataValue;
import com.quanyan.yhy.common.ItemType;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.base.utils.TCEventHelper;
import com.quanyan.yhy.ui.common.address.model.DeleteMyAdress;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.order.controller.OrderController;
import com.quanyan.yhy.ui.tab.homepage.order.DialogOrder;
import com.yhy.common.beans.net.model.common.address.MyAddressContentInfo;
import com.yhy.common.beans.net.model.tm.TmCreateOrderContext;
import com.yhy.common.beans.net.model.tm.TmCreateOrderParam;
import com.yhy.common.beans.net.model.tm.TmCreateOrderResultTO;
import com.yhy.common.beans.net.model.tm.VoucherResult;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.CommonUtil;
import com.yhy.common.utils.SPUtils;
import com.yhy.imageloader.ImageLoadManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with Android Studio.
 * Title:PointOrderActivity
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-7-1
 * Time:10:37
 * Version 1.0
 * Description:
 */
public class PointOrderActivity extends BaseActivity {

    private static final String ITEMID = "ITEMID";
    private static final String ITEMTYPE = "ITEMTYPE";
    private static final int MAX = 99;
    private static final int Min = 1;
    private static final int SELECTADRESS = 0x10;
    private static final int CHOOSECOUPON = 0x11;
    private static final int ALLPOINT = 0x1234;

    private OrderTopView mOrderTopView;

    @ViewInject(R.id.view_order_bottom)
    private HotelOrderBottomTabView mOrderBottomTab;
    @ViewInject(R.id.rl_noadress)
    private RelativeLayout mNoAdressLayout;
    @ViewInject(R.id.rl_hasadress)
    private RelativeLayout mHasAdressLayout;
    @ViewInject(R.id.tv_rcadress_name)
    private TextView mRcAdressName;
    @ViewInject(R.id.tv_rcadress_tips)
    private TextView mRcAdressTips;
    @ViewInject(R.id.tv_rcadress_tel)
    private TextView mRcAdressTel;
    @ViewInject(R.id.tv_rcadress_adress)
    private TextView mRcAdress;
    @ViewInject(R.id.sa_iv_good)
    private ImageView mGoodImage;
    @ViewInject(R.id.sa_tv_good_name)
    private TextView mGoodName;
    @ViewInject(R.id.sa_tv_good_currentprice)
    private TextView mGoodPrice;
    @ViewInject(R.id.tv_label_last)
    private TextView mTvPriceLabel;
    @ViewInject(R.id.nc_num_select)
    private NumberChooseView mNumberChoose;
    @ViewInject(R.id.scenicorder_entertime_tv)
    private TextView mIntegral;
    @ViewInject(R.id.tv_integral_tips)
    private TextView mIntegralTips;
    @ViewInject(R.id.tv_integral_money)
    private TextView mIntegralMoney;
    @ViewInject(R.id.cb_switch)
    private CheckBox mSwitchBox;
    @ViewInject(R.id.rl_coupon_layout)
    private RelativeLayout mCouponLayout;
    @ViewInject(R.id.scenic_othersrq_et)
    private EditText mOthersrq;
    @ViewInject(R.id.tv_coupon)
    private TextView mCoupon;
    @ViewInject(R.id.ll_integral)
    private LinearLayout mIntegralLayout;

    private long itemId;
    private String itemType;
    private OrderController mController;

    private long sellerId = -1;
    private VoucherResult voucherResult;
    private long addressId = -1;

    private long mMimeTotalPoint = 0;

    private long maxUsePoint = 0;
    private int currentBuyNum = 1;
    private long unitPrice = 0;

    private String mItemType;
    private TmCreateOrderContext tmCreateOrderContext;
    protected boolean isPayComplete = false;//是否支付过
    private Dialog mOrderCancelDialog;

    public static void gotoPointOrderActivity(Context context, long itemId, String itemType) {
        Intent intent = new Intent(context, PointOrderActivity.class);
        intent.putExtra(ITEMID, itemId);
        intent.putExtra(ITEMTYPE, itemType);
        context.startActivity(intent);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ViewUtils.inject(this);
        itemType = getIntent().getStringExtra(ITEMTYPE);
        itemId = getIntent().getLongExtra(ITEMID, -1);
        mController = new OrderController(this, mHandler);
        mNumberChoose.initCheckValue(MAX, Min, currentBuyNum);
        mOrderBottomTab.setSubmitText(getResources().getString(R.string.order_submit_text));
        mOrderBottomTab.setBottomPrice(StringUtil.converRMb2YunWithFlag(PointOrderActivity.this, 0));
        mOrderBottomTab.setDetailsLayoutGone();

        if (itemType.equals(ItemType.NORMAL)) {
            mIntegralLayout.setVisibility(View.GONE);
        }

//        if (itemType.equals(ItemType.POINT_MALL)) {
//        mCouponLayout.setVisibility(View.GONE);
//        }

        mTvPriceLabel.setVisibility(View.GONE);

        initClick();

        doGetContextMsg();
    }

    private void initClick() {
        mNoAdressLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavUtils.gotoAddressListActivity(PointOrderActivity.this, SELECTADRESS, PointOrderActivity.class.getSimpleName());
            }
        });

        mHasAdressLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavUtils.gotoAddressListActivity(PointOrderActivity.this, SELECTADRESS, PointOrderActivity.class.getSimpleName());
            }
        });

        //选择优惠券
        mCouponLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long totalPrice = 0;
                if (mSwitchBox.isChecked()) {
                    if (voucherResult == null) {
                        totalPrice = unitPrice * currentBuyNum - setDisCount(mMimeTotalPoint, maxUsePoint, currentBuyNum, unitPrice, 0);
                    } else {
                        totalPrice = unitPrice * currentBuyNum - setDisCount(mMimeTotalPoint, maxUsePoint, currentBuyNum, unitPrice, voucherResult.value);
                    }
                } else {
                    totalPrice = unitPrice * currentBuyNum;
                }
                NavUtils.gotoOrderCouponActivity(PointOrderActivity.this, sellerId, totalPrice, CHOOSECOUPON);
            }
        });

        mNumberChoose.setNumberChooseListener(new NumberChooseView.NumberClickListener() {
            @Override
            public void onReduce(int num) {
                currentBuyNum = num;
                if (voucherResult != null) {
//                    mIntegralTips.setText(String.format(getResources().getString(R.string.point_order_tips), setDisCount(mMimeTotalPoint, maxUsePoint, currentBuyNum, unitPrice, voucherResult.value)));
//                    mIntegralMoney.setText(StringUtil.convertPriceNoSymbolExceptLastZeroWithFlag(PointOrderActivity.this, setDisCount(mMimeTotalPoint, maxUsePoint, currentBuyNum, unitPrice, voucherResult.value)));
//                    if (setDisCount(mMimeTotalPoint, maxUsePoint, currentBuyNum, unitPrice, voucherResult.value) <= 0) {
//                        mSwitchBox.setChecked(false);
//                        mSwitchBox.setClickable(false);
//                    } else {
//                        mSwitchBox.setClickable(true);
//                    }
                    mCoupon.setText("");
                    voucherResult = null;
//                    mIntegralTips.setText(String.format(getResources().getString(R.string.point_order_tips), setDisCount(mMimeTotalPoint, maxUsePoint, currentBuyNum, unitPrice, 0)));
//                    mIntegralMoney.setText(StringUtil.convertPriceNoSymbolExceptLastZeroWithFlag(PointOrderActivity.this, setDisCount(mMimeTotalPoint, maxUsePoint, currentBuyNum, unitPrice, 0)));

                    long discountPoint = setDisCount(mMimeTotalPoint, maxUsePoint, currentBuyNum, unitPrice, 0);
                    mIntegralTips.setText(String.format(getResources().getString(R.string.point_order_tips), String.valueOf(discountPoint)));
                    mIntegralMoney.setText(StringUtil.pointToYuanOne(discountPoint*10));
//                    mIntegralMoney.setText(StringUtil.convertScoreToDiscount(PointOrderActivity.this, maxUsePoint));
                    if (setDisCount(mMimeTotalPoint, maxUsePoint, currentBuyNum, unitPrice, 0) <= 0) {
                        mSwitchBox.setChecked(false);
                        mSwitchBox.setClickable(false);
                    } else {
                        mSwitchBox.setClickable(true);
                    }
                    mOrderBottomTab.setBottomPrice(StringUtil.converRMb2YunWithFlag(PointOrderActivity.this, unitPrice * currentBuyNum));
//                    mOrderBottomTab.setDiscountLayout(0);
                } else {
                    long discountPoint = setDisCount(mMimeTotalPoint, maxUsePoint, currentBuyNum, unitPrice, 0);
                    mIntegralTips.setText(String.format(getResources().getString(R.string.point_order_tips), String.valueOf(discountPoint)));
                    mIntegralMoney.setText(StringUtil.pointToYuanOne(discountPoint*10));
//                    mIntegralMoney.setText(StringUtil.convertScoreToDiscount(PointOrderActivity.this, maxUsePoint));
                    if (setDisCount(mMimeTotalPoint, maxUsePoint, currentBuyNum, unitPrice, 0) <= 0) {
                        mSwitchBox.setChecked(false);
                        mSwitchBox.setClickable(false);
                    } else {
                        mSwitchBox.setClickable(true);
                    }
                }

                if (itemType.equals(ItemType.POINT_MALL)) {
                    if (mSwitchBox.isChecked()) {
                        mOrderBottomTab.setDiscountLayout(setDisCount(mMimeTotalPoint, maxUsePoint, currentBuyNum, unitPrice, 0));
                    } else {
                        mOrderBottomTab.setDiscountLayout(0);
                    }
                } else {
                    mOrderBottomTab.setDiscountLayout(0);
                }
                setBottomPrice();
            }

            @Override
            public void onIncrease(int num) {
                currentBuyNum = num;
                if (voucherResult != null) {
                    voucherResult = null;
//                    mIntegralTips.setText(String.format(getResources().getString(R.string.point_order_tips), setDisCount(mMimeTotalPoint, maxUsePoint, currentBuyNum, unitPrice, voucherResult.value)));
//                    mIntegralMoney.setText(StringUtil.convertPriceNoSymbolExceptLastZeroWithFlag(PointOrderActivity.this, setDisCount(mMimeTotalPoint, maxUsePoint, currentBuyNum, unitPrice, voucherResult.value)));
//                    if (setDisCount(mMimeTotalPoint, maxUsePoint, currentBuyNum, unitPrice, voucherResult.value) <= 0) {
//                        mSwitchBox.setChecked(false);
//                        mSwitchBox.setClickable(false);
//                    } else {
//                        mSwitchBox.setClickable(true);
//                    }
                    mCoupon.setText("");
                    if (setDisCount(mMimeTotalPoint, maxUsePoint, currentBuyNum, unitPrice, 0) <= 0) {
                        mSwitchBox.setChecked(false);
                        mSwitchBox.setClickable(false);
                    } else {
                        mSwitchBox.setClickable(true);
                    }

                    long discountPoint = setDisCount(mMimeTotalPoint, maxUsePoint, currentBuyNum, unitPrice, 0);
                    mIntegralTips.setText(String.format(getResources().getString(R.string.point_order_tips), String.valueOf(discountPoint)));
                    mIntegralMoney.setText(StringUtil.pointToYuanOne(discountPoint*10));
//                    mIntegralTips.setText(String.format(getResources().getString(R.string.point_order_tips), setDisCount(mMimeTotalPoint, maxUsePoint, currentBuyNum, unitPrice, 0)));
//                    mIntegralMoney.setText(StringUtil.convertPriceNoSymbolExceptLastZeroWithFlag(PointOrderActivity.this, setDisCount(mMimeTotalPoint, maxUsePoint, currentBuyNum, unitPrice, 0)));
                    mOrderBottomTab.setBottomPrice(StringUtil.converRMb2YunWithFlag(PointOrderActivity.this, unitPrice * currentBuyNum));
//                    mOrderBottomTab.setDiscountLayout(0);
                } else {
                    if (setDisCount(mMimeTotalPoint, maxUsePoint, currentBuyNum, unitPrice, 0) <= 0) {
                        mSwitchBox.setChecked(false);
                        mSwitchBox.setClickable(false);
                    } else {
                        mSwitchBox.setClickable(true);
                    }
//                    mIntegralTips.setText(String.format(getResources().getString(R.string.point_order_tips), setDisCount(mMimeTotalPoint, maxUsePoint, currentBuyNum, unitPrice, 0)));
//                    mIntegralMoney.setText(StringUtil.convertPriceNoSymbolExceptLastZeroWithFlag(PointOrderActivity.this, setDisCount(mMimeTotalPoint, maxUsePoint, currentBuyNum, unitPrice, 0)));

                    long discountPoint = setDisCount(mMimeTotalPoint, maxUsePoint, currentBuyNum, unitPrice, 0);
                    mIntegralTips.setText(String.format(getResources().getString(R.string.point_order_tips), String.valueOf(discountPoint)));
                    mIntegralMoney.setText(StringUtil.pointToYuanOne(discountPoint*10));
                }

                if (itemType.equals(ItemType.POINT_MALL)) {
                    if (mSwitchBox.isChecked()) {
                        mOrderBottomTab.setDiscountLayout(setDisCount(mMimeTotalPoint, maxUsePoint, currentBuyNum, unitPrice, 0));
                    } else {
                        mOrderBottomTab.setDiscountLayout(0);
                    }
                } else {
                    mOrderBottomTab.setDiscountLayout(0);
                }
                setBottomPrice();
            }

            @Override
            public void onReduce() {

            }

            @Override
            public void onIncrease() {

            }
        });

        mOrderBottomTab.setSubmitClickListener(new HotelOrderBottomTabView.SubmitClick() {
            @Override
            public void submit() {
                tcEvent();
                configParams();
            }

            @Override
            public void gotoSalesDetails() {

            }
        });

        mSwitchBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (itemType.equals(ItemType.POINT_MALL)) {
                    if (isChecked) {
                        if (voucherResult != null) {
                            mOrderBottomTab.setDiscountLayout(setDisCount(mMimeTotalPoint, maxUsePoint, currentBuyNum, unitPrice, voucherResult.value) + voucherResult.value);
                        } else {
                            mOrderBottomTab.setDiscountLayout(setDisCount(mMimeTotalPoint, maxUsePoint, currentBuyNum, unitPrice, 0));
                        }
                    } else {
                        if (voucherResult != null) {
                            mOrderBottomTab.setDiscountLayout(voucherResult.value);
                        } else {
                            mOrderBottomTab.setDiscountLayout(0);
                        }
                    }
                }
                setBottomPrice();
            }
        });

        mOrderTopView.setBackClickListener(new OrderTopView.BackClickListener() {
            @Override
            public void clickBack() {
                if (!isPayComplete) {
                    mOrderCancelDialog = DialogOrder.cancelOrder(PointOrderActivity.this);
                } else {
                    finish();
                }
            }
        });
    }

    private void tcEvent() {
        Map<String, String> params = new HashMap<String, String>();
        params.put(AnalyDataValue.KEY_PTYPE, AnalyDataValue.getType(itemType));
        if (tmCreateOrderContext != null && tmCreateOrderContext.itemInfo != null) {
            params.put(AnalyDataValue.KEY_PNAME, tmCreateOrderContext.itemInfo.title);
        }
        params.put(AnalyDataValue.KEY_PID, itemId + "");
        TCEventHelper.onEvent(PointOrderActivity.this, AnalyDataValue.TC_ID_SUBMIT_ORDER, params);
    }

    private void setBottomPrice() {
        if (mSwitchBox.isChecked()) {
            if (voucherResult != null) {
                                long bottomPrice = (unitPrice * currentBuyNum - setDisCount(mMimeTotalPoint, maxUsePoint, currentBuyNum, unitPrice, voucherResult.value)*10 - voucherResult.value) <= 0 ? 0 : (unitPrice * currentBuyNum - setDisCount(mMimeTotalPoint, maxUsePoint, currentBuyNum, unitPrice, voucherResult.value)*10 - voucherResult.value);
                mOrderBottomTab.setBottomPrice(StringUtil.pointToYuan(bottomPrice));

//                long bottomPrice = (unitPrice * currentBuyNum/10 - setDisCount(mMimeTotalPoint, maxUsePoint, currentBuyNum, unitPrice, voucherResult.value) - voucherResult.value) <= 0 ? 0 : (unitPrice * currentBuyNum/10 - setDisCount(mMimeTotalPoint, maxUsePoint, currentBuyNum, unitPrice, voucherResult.value) - voucherResult.value);
//                mOrderBottomTab.setBottomPrice(StringUtil.converRMb2YunWithFlag(PointOrderActivity.this, bottomPrice));
            } else {
                long bottomPrice = (unitPrice * currentBuyNum - setDisCount(mMimeTotalPoint, maxUsePoint, currentBuyNum, unitPrice, 0)*10) <= 0 ? 0 : (unitPrice * currentBuyNum - setDisCount(mMimeTotalPoint, maxUsePoint, currentBuyNum, unitPrice, 0)*10);
//                mOrderBottomTab.setBottomPrice(StringUtil.converRMb2YunWithFlag(PointOrderActivity.this, bottomPrice));
                mOrderBottomTab.setBottomPrice(StringUtil.pointToYuan(bottomPrice));

            }
        } else {
            if (voucherResult != null) {
                long bottomPrice = (unitPrice * currentBuyNum - voucherResult.value) <= 0 ? 0 : (unitPrice * currentBuyNum - voucherResult.value);
//                mOrderBottomTab.setBottomPrice(StringUtil.converRMb2YunWithFlag(PointOrderActivity.this, bottomPrice));
                mOrderBottomTab.setBottomPrice(StringUtil.pointToYuan(bottomPrice));

            } else {
                long bottomPrice = unitPrice * currentBuyNum;
                mOrderBottomTab.setBottomPrice(StringUtil.pointToYuan(bottomPrice));

//                mOrderBottomTab.setBottomPrice(StringUtil.converRMb2YunWithFlag(PointOrderActivity.this, unitPrice * currentBuyNum));
            }
        }
    }

    /**
     * 判断是否是全积分
     *
     * @return
     */
    private boolean isAllPointPay() {
        if (mSwitchBox.isChecked()) {
            if (voucherResult != null) {
                return (unitPrice * currentBuyNum - setDisCount(mMimeTotalPoint, maxUsePoint, currentBuyNum, unitPrice, voucherResult.value) - voucherResult.value) <= 0 ? true : false;
            } else {
                return (unitPrice * currentBuyNum - setDisCount(mMimeTotalPoint, maxUsePoint, currentBuyNum, unitPrice, 0)) <= 0 ? true : false;
            }
        } else {
            if (voucherResult != null) {
                return (unitPrice * currentBuyNum - voucherResult.value) <= 0 ? true : false;
            } else {
                return (unitPrice * currentBuyNum) <= 0 ? true : false;
            }
        }
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.activity_pointorder, null);
    }

    @Override
    public View onLoadNavView() {
        mOrderTopView = new OrderTopView(this);
        mOrderTopView.setOrderTopTitle(getResources().getString(R.string.order_title));
        return mOrderTopView;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }

    private void doGetContextMsg() {
        if (itemId <= 0) {
            return;
        }
        mController.getCreateOrderContext(this, itemId);
    }

    private void configParams() {
        if (addressId <= 0) {
            ToastUtil.showToast(this, "请填写收货地址");
            return;
        }

        if (currentBuyNum <= 0) {
            ToastUtil.showToast(this, "请选择购买数量");
            return;
        }
        doCreateOrder();
    }

    private void doCreateOrder() {
        String remark = mOthersrq.getText().toString();
        TmCreateOrderParam param = new TmCreateOrderParam();
        param.itemId = itemId;
        param.otherInfo = remark;
        param.addressId = addressId;
        param.itemType = mItemType;
        param.buyAmount = currentBuyNum;
        if (voucherResult != null) {
            param.voucherId = voucherResult.id;
        }
        if (mSwitchBox.isChecked()) {
            if (voucherResult != null) {
                param.usePoint = setDisCount(mMimeTotalPoint, maxUsePoint, currentBuyNum, unitPrice, voucherResult.value);
            } else {
                param.usePoint = setDisCount(mMimeTotalPoint, maxUsePoint, currentBuyNum, unitPrice, 0);
            }
        }

        mController.doCreateOrder(PointOrderActivity.this, param);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case ValueConstants.MSG_CLUB_DETAIL_INFO_OK:
                tmCreateOrderContext = (TmCreateOrderContext) msg.obj;
                if (tmCreateOrderContext != null) {
                    mController.doTotalPointQuery(this);
                } else {

                }
                break;
            case ValueConstants.MSG_CLUB_DETAIL_INFO_KO:

                break;
            case ValueConstants.POINT_TOTAL_OK:
                mMimeTotalPoint = (Long) msg.obj;
                mIntegral.setText(mMimeTotalPoint + "");
                setContentView(tmCreateOrderContext);
                break;
            case ValueConstants.POINT_TOTAL_ERROR:
                mIntegral.setText(mMimeTotalPoint + "");
                break;
            case ValueConstants.MSG_CREATE_ORDER_OK:
                handleOrderData((TmCreateOrderResultTO) msg.obj);
                break;
            case ValueConstants.MSG_CREATE_ORDER_KO:
                ToastUtil.showToast(this, StringUtil.handlerErrorCode(getApplicationContext(), msg.arg1));
                break;

            case ALLPOINT:
                hideLoadingView();
                if (mSwitchBox.isChecked()) {
                    if (voucherResult != null) {
                        SPUtils.setScore(PointOrderActivity.this, SPUtils.getScore(PointOrderActivity.this) - setDisCount(mMimeTotalPoint, maxUsePoint, currentBuyNum, unitPrice, voucherResult.value));
                    } else {
                        SPUtils.setScore(PointOrderActivity.this, SPUtils.getScore(PointOrderActivity.this) - setDisCount(mMimeTotalPoint, maxUsePoint, currentBuyNum, unitPrice, 0));
                    }
                }
                Toast.makeText(PointOrderActivity.this, "兑换成功", Toast.LENGTH_SHORT).show();
                NavUtils.gotoMyOrderAllListActivity(PointOrderActivity.this);
                finish();
                break;
        }
    }

    /**
     * 处理提交订单成功
     *
     * @param result
     */
    private void handleOrderData(final TmCreateOrderResultTO result) {
        isPayComplete = true;
        if (result == null) {
            if (mItemType.equals(ItemType.POINT_MALL)) {
                if (isAllPointPay()) {
                    Toast.makeText(PointOrderActivity.this, "兑换失败", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PointOrderActivity.this, getResources().getString(R.string.line_order_tips_failure), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(PointOrderActivity.this, getResources().getString(R.string.line_order_tips_failure), Toast.LENGTH_SHORT).show();
            }
            return;
        }
        if (result.success) {
            // 提交订单成功
            // 延迟跳转

            if (mItemType.equals(ItemType.POINT_MALL) && isAllPointPay()) {
//                showLoadingView("提交订单中...");
//                mHandler.sendEmptyMessageDelayed(ALLPOINT, 2000);
                // TODO: 2018/3/1
                String url = SPUtils.getPonitPayUrl(this).trim();
                if (url != null && !url.isEmpty()) {
                    url = url.replaceAll(":orderId", String.valueOf(result.mainOrder.bizOrder.bizOrderId));
                    NavUtils.startWebview(this, "", url, 0);
                    finish();
                }
            } else {
                if (mItemType.equals(ItemType.POINT_MALL)) {
                    if (mSwitchBox.isChecked()) {
                        if (voucherResult != null) {
                            SPUtils.setScore(PointOrderActivity.this, SPUtils.getScore(PointOrderActivity.this) - setDisCount(mMimeTotalPoint, maxUsePoint, currentBuyNum, unitPrice, voucherResult.value));
                        } else {
                            SPUtils.setScore(PointOrderActivity.this, SPUtils.getScore(PointOrderActivity.this) - setDisCount(mMimeTotalPoint, maxUsePoint, currentBuyNum, unitPrice, 0));
                        }
                    }
                }

                // TODO: 2018/3/1
                String url = SPUtils.getPonitPayUrl(this).trim();
                if (url != null && !url.isEmpty()) {
                    url = url.replaceAll(":orderId", String.valueOf(result.mainOrder.bizOrder.bizOrderId));
                    NavUtils.startWebview(this, "", url, 0);
                    finish();
                }
//                NavUtils.gotoOrderConfigActivity(PointOrderActivity.this, mItemType, result, 0, 0, null);
//                finish();
            }
        } else {
            if (mItemType.equals(ItemType.POINT_MALL)) {
                if (isAllPointPay()) {
                    Toast.makeText(PointOrderActivity.this, "兑换失败", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PointOrderActivity.this, getResources().getString(R.string.line_order_tips_failure), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(PointOrderActivity.this, getResources().getString(R.string.line_order_tips_failure), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setContentView(TmCreateOrderContext tmCreateOrderContext) {
        if (tmCreateOrderContext == null) {
            return;
        }
        if (tmCreateOrderContext.itemInfo == null) {
            return;
        }
        if (tmCreateOrderContext.defaultAddress != null) {
            addressId = tmCreateOrderContext.defaultAddress.addressId;
            mNoAdressLayout.setVisibility(View.GONE);
            mHasAdressLayout.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(tmCreateOrderContext.defaultAddress.recipientName)) {
                mRcAdressName.setText(tmCreateOrderContext.defaultAddress.recipientName);
            } else {
                mRcAdressName.setText("");
            }
            if (!TextUtils.isEmpty(tmCreateOrderContext.defaultAddress.recipientPhone)) {
                mRcAdressTel.setText(tmCreateOrderContext.defaultAddress.recipientPhone);
            } else {
                mRcAdressTel.setText("");
            }

            //详细地址
            if (!StringUtil.isEmpty(tmCreateOrderContext.defaultAddress.detailAddress)) {
                if (!StringUtil.isEmpty(tmCreateOrderContext.defaultAddress.province)) {
                    province = tmCreateOrderContext.defaultAddress.province;
                }
                if (!StringUtil.isEmpty(tmCreateOrderContext.defaultAddress.city) && !tmCreateOrderContext.defaultAddress.city.equals(tmCreateOrderContext.defaultAddress.province)) {
                    city = tmCreateOrderContext.defaultAddress.city;
                } else {
                    city = "";
                }
                if (!StringUtil.isEmpty(tmCreateOrderContext.defaultAddress.area)) {
                    area = tmCreateOrderContext.defaultAddress.area;
                }
                String newAddress = province + city + area + tmCreateOrderContext.defaultAddress.detailAddress;
                mRcAdress.setText(newAddress);
            }
            if (tmCreateOrderContext.defaultAddress.isDefault) {
                mRcAdressTips.setText("默认");
            } else {
                mRcAdressTips.setText("");
            }
        } else {
            mNoAdressLayout.setVisibility(View.VISIBLE);
            mHasAdressLayout.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(tmCreateOrderContext.itemInfo.title)) {
            mGoodName.setText(tmCreateOrderContext.itemInfo.title);
        } else {
            mGoodName.setText("");
        }

        sellerId = tmCreateOrderContext.itemInfo.sellerId;

        maxUsePoint = tmCreateOrderContext.itemInfo.maxUsePoint;

        if (!TextUtils.isEmpty(tmCreateOrderContext.itemInfo.itemPic)) {
//            BaseImgView.loadimg(mGoodImage,
//                    ImageUtils.getImageFullUrl(tmCreateOrderContext.itemInfo.itemPic),
//                    R.mipmap.icon_default_215_215,
//                    R.mipmap.icon_default_215_215,
//                    R.mipmap.icon_default_215_215,
//                    ImageScaleType.EXACTLY,
//                    -1,
//                    -1,
//                    0);
            ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(tmCreateOrderContext.itemInfo.itemPic), R.mipmap.icon_default_215_215, mGoodImage);
        } else {
            mGoodImage.setImageResource(R.mipmap.icon_default_215_215);
        }

        mItemType = tmCreateOrderContext.itemInfo.itemType;
        unitPrice = tmCreateOrderContext.itemInfo.marketPrice;

        mGoodPrice.setText(StringUtil.converRMb2YunWithFlag(PointOrderActivity.this, tmCreateOrderContext.itemInfo.marketPrice));

//        mOrderBottomTab.setBottomPrice(StringUtil.converRMb2YunWithFlag(tmCreateOrderContext.itemInfo.marketPrice));
//        mIntegralTips.setText(String.format(getResources().getString(R.string.point_order_tips), setDisCount(mMimeTotalPoint, maxUsePoint, currentBuyNum, unitPrice, 0)));
        // 可用积分抵扣额度
        mIntegralTips.setText(String.format(getResources().getString(R.string.point_order_tips), String.valueOf(maxUsePoint > mMimeTotalPoint ? mMimeTotalPoint : maxUsePoint)));
        mIntegralMoney.setText(StringUtil.convertScoreToDiscount(PointOrderActivity.this, maxUsePoint));

        if (setDisCount(mMimeTotalPoint, maxUsePoint, currentBuyNum, unitPrice, 0) <= 0) {
            mSwitchBox.setChecked(false);
            mSwitchBox.setClickable(false);
        } else {
            mSwitchBox.setClickable(true);
            mSwitchBox.setChecked(true);
        }

        if (itemType.equals(ItemType.POINT_MALL)) {
            if (mSwitchBox.isChecked()) {
                mOrderBottomTab.setDiscountLayout(setDisCount(mMimeTotalPoint, maxUsePoint, currentBuyNum, unitPrice, 0));
            } else {
                mOrderBottomTab.setDiscountLayout(0);
            }
        } else {
            mOrderBottomTab.setDiscountLayout(0);
        }

        setBottomPrice();
    }

    /**
     * 全部可用积分
     * @param totalPoint   123456789
     * @param maxUsePoint  101
     * @param buyCount     1
     * @param unitPrice    101
     * @param discoutPrice 98
     * @return
     */
    private long setDisCount(long totalPoint, long maxUsePoint, int buyCount, long unitPrice, long discoutPrice) {
        long totalPrice = unitPrice * buyCount;
        long allMaxUserPoint = maxUsePoint * buyCount;
        long minTotalPoint = allMaxUserPoint >= totalPoint ? totalPoint : allMaxUserPoint;
        long a = (totalPrice - discoutPrice) >= minTotalPoint ? minTotalPoint : (totalPrice - discoutPrice);
        return a <= 0 ? 0 : a;
    }


    private String province = "";
    private String city = "";
    private String area = "";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SELECTADRESS://选择收货地址
                    if (data != null) {
                        MyAddressContentInfo myAddress = (MyAddressContentInfo) data.getSerializableExtra(SPUtils.EXTRA_DATA);
                        if (myAddress != null) {
                            addressId = myAddress.id;
                            if (mNoAdressLayout != null && mNoAdressLayout.getVisibility() == View.VISIBLE) {
                                mNoAdressLayout.setVisibility(View.GONE);
                            }
                            if (mHasAdressLayout != null && mHasAdressLayout.getVisibility() == View.GONE) {
                                mHasAdressLayout.setVisibility(View.VISIBLE);
                            }
                            if (!TextUtils.isEmpty(myAddress.recipientsName)) {
                                mRcAdressName.setText(myAddress.recipientsName);
                            } else {
                                mRcAdressName.setText("");
                            }
                            if (!TextUtils.isEmpty(myAddress.recipientsPhone)) {
                                mRcAdressTel.setText(myAddress.recipientsPhone);
                            } else {
                                mRcAdressTel.setText("");
                            }

                            //详细地址
                            if (!StringUtil.isEmpty(myAddress.detailAddress)) {
                                if (!StringUtil.isEmpty(myAddress.province)) {
                                    province = myAddress.province;
                                }
                                if (!StringUtil.isEmpty(myAddress.city) && !myAddress.city.equals(myAddress.province)) {
                                    city = myAddress.city;
                                } else {
                                    city = "";
                                }
                                if (!StringUtil.isEmpty(myAddress.area)) {
                                    area = myAddress.area;
                                }
                                String newAddress = province + city + area + myAddress.detailAddress;
                                mRcAdress.setText(newAddress);
                            }
                            if (myAddress.isDefault.equals("1")) {
                                mRcAdressTips.setText("默认");
                            } else {
                                mRcAdressTips.setText("");
                            }
                        }
                    }
                    break;
                case CHOOSECOUPON:
                    if (data != null) {
                        voucherResult = (VoucherResult) data.getSerializableExtra(SPUtils.EXTRA_DATA);
                        mCoupon.setText("-" + StringUtil.converRMb2YunWithFlag(PointOrderActivity.this, voucherResult.value));

                        long discountPoint = setDisCount(mMimeTotalPoint, maxUsePoint, currentBuyNum, unitPrice, voucherResult.value);
                        mIntegralTips.setText(String.format(getResources().getString(R.string.point_order_tips), StringUtil.pointToYuan(discountPoint)));
                        mIntegralMoney.setText(StringUtil.pointToYuan(discountPoint*10));
//                        mIntegralTips.setText(String.format(getResources().getString(R.string.point_order_tips), setDisCount(mMimeTotalPoint, maxUsePoint, currentBuyNum, unitPrice, voucherResult.value)));
//                        mIntegralMoney.setText(StringUtil.convertPriceNoSymbolWithFlag(PointOrderActivity.this, setDisCount(mMimeTotalPoint, maxUsePoint, currentBuyNum, unitPrice, voucherResult.value)));
                        if (setDisCount(mMimeTotalPoint, maxUsePoint, currentBuyNum, unitPrice, voucherResult.value) <= 0) {
                            mSwitchBox.setChecked(false);
                            mSwitchBox.setClickable(false);
                        } else {
                            mSwitchBox.setClickable(true);
                        }
                        setBottomPrice();

                        if (mSwitchBox.isChecked()) {
                            mOrderBottomTab.setDiscountLayout(voucherResult.value + setDisCount(mMimeTotalPoint, maxUsePoint, currentBuyNum, unitPrice, voucherResult.value));
                        } else {
                            mOrderBottomTab.setDiscountLayout(voucherResult.value);
                        }
                    }
                    break;
            }
        } else if (resultCode == RESULT_CANCELED) {
            switch (requestCode) {
                case SELECTADRESS:
                    if (data != null) {
                        DeleteMyAdress mDeleteAdress = (DeleteMyAdress) data.getSerializableExtra(SPUtils.EXTRA_DATA);
                        if (mDeleteAdress != null) {
                            deleteAdress(mDeleteAdress);
                        }
                    }
                    break;
                case CHOOSECOUPON:
                    voucherResult = null;
                    mCoupon.setText("");
                    setBottomPrice();
                    if (itemType.equals(ItemType.POINT_MALL)) {
                        if (mSwitchBox.isChecked()) {
                            mOrderBottomTab.setDiscountLayout(setDisCount(mMimeTotalPoint, maxUsePoint, currentBuyNum, unitPrice, 0));
                        } else {
                            mOrderBottomTab.setDiscountLayout(0);
                        }
                    } else {
                        mOrderBottomTab.setDiscountLayout(0);
                    }
//                    mIntegralTips.setText(String.format(getResources().getString(R.string.point_order_tips), setDisCount(mMimeTotalPoint, maxUsePoint, currentBuyNum, unitPrice, 0)));
//                    mIntegralMoney.setText(StringUtil.convertPriceNoSymbolExceptLastZeroWithFlag(PointOrderActivity.this, setDisCount(mMimeTotalPoint, maxUsePoint, currentBuyNum, unitPrice, 0)));

                    long discountPoint = setDisCount(mMimeTotalPoint, maxUsePoint, currentBuyNum, unitPrice, voucherResult.value);
                    mIntegralTips.setText(String.format(getResources().getString(R.string.point_order_tips), StringUtil.pointToYuan(discountPoint)));
                    mIntegralMoney.setText(StringUtil.pointToYuan(discountPoint*10));
                    break;
            }
        }
    }

    /**
     * 删除地址
     *
     * @param mDeleteAdress
     */
    private void deleteAdress(DeleteMyAdress mDeleteAdress) {
        if (mDeleteAdress == null) {
            return;
        }
        if (mDeleteAdress.deleMyAdress == null || mDeleteAdress.deleMyAdress.size() == 0) {
            return;
        }

        if (addressId == -1) {
            return;
        }

        boolean isDelete = false;

        for (int i = 0; i < mDeleteAdress.deleMyAdress.size(); i++) {
            if (addressId == mDeleteAdress.deleMyAdress.get(i).id) {
                isDelete = true;
                break;
            }
        }

        if (isDelete) {
            mNoAdressLayout.setVisibility(View.VISIBLE);
            mHasAdressLayout.setVisibility(View.GONE);
            addressId = -1;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            if (!isPayComplete) {
                mOrderCancelDialog = DialogOrder.cancelOrder(PointOrderActivity.this);
            } else {
                finish();
            }
        }
        return true;
    }


}
