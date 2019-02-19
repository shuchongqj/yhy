package com.quanyan.yhy.ui.spcart;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.ItemType;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.common.address.model.DeleteMyAdress;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.order.controller.OrderController;
import com.quanyan.yhy.ui.spcart.adapter.SPCartOderAdapter;
import com.quanyan.yhy.ui.spcart.controller.SPCartController;
import com.quanyan.yhy.ui.spcart.dialog.SPCartDialog;
import com.quanyan.yhy.ui.spcart.view.SpCartTopBarView;
import com.quanyan.yhy.ui.spcart.view.SpcartOrderBottomView;
import com.quanyan.yhy.ui.spcart.view.SpcartOrderHeadView;
import com.quanyan.yhy.ui.tab.homepage.order.DialogOrder;
import com.smart.sdk.api.request.ApiCode;
import com.yhy.common.beans.net.model.common.address.MyAddressContentInfo;
import com.yhy.common.beans.net.model.tm.CreateBatchOrderParam;
import com.yhy.common.beans.net.model.tm.CreateOrderContextForPointMall;
import com.yhy.common.beans.net.model.tm.CreateOrderContextParamForPointMall;
import com.yhy.common.beans.net.model.tm.CreateOrderResultTOList;
import com.yhy.common.beans.net.model.tm.ItemParamForCreateOrder;
import com.yhy.common.beans.net.model.tm.LeaveMassage;
import com.yhy.common.beans.net.model.tm.Shop;
import com.yhy.common.beans.net.model.tm.Voucher;
import com.yhy.common.beans.net.model.tm.VoucherResult;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:SPCartOrderActivity
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-10-26
 * Time:10:37
 * Version 1.0
 * Description:
 */
public class SPCartOrderActivity extends BaseActivity {

    private static final String DATA = "data";
    private static final int CHOOSECOUPON = 0x11;
    private static final int SELECTADRESS = 0x10;
    private static final int ALLPOINT = 0x1234;

    @ViewInject(R.id.order_bottom)
    private SpcartOrderBottomView mSpcartOrderBottomView;

    private SpCartTopBarView mTopBarView;
    @ViewInject(R.id.expand)
    private ExpandableListView expandableListView;
    private SpcartOrderHeadView headView;
    private SPCartOderAdapter mAdapter;
    private SPCartController mSpcartController;
    private OrderController mController;
    private long mMimeTotalPoint = 0;
    private CreateOrderContextParamForPointMall mCreateOrderContextParamForPointMall;
    private CreateOrderContextForPointMall mCreateOrderContextForPointMall;

    private long addressId;

    private int mGroupPostion;
    private TextView mCouponTextView;

    private List<Shop> orderList;
    private long originalTotalFee;
    private long allPoint;

    private Dialog mOrderCancelDialog;
    protected boolean isPayComplete = false;//是否支付过


    public static void gotoSPCartOrderActivity(Context context, CreateOrderContextParamForPointMall mCreateOrderContextParamForPointMall) {
        Intent intent = new Intent(context, SPCartOrderActivity.class);
        intent.putExtra(DATA, mCreateOrderContextParamForPointMall);
        context.startActivity(intent);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ViewUtils.inject(this);
        mCreateOrderContextParamForPointMall = (CreateOrderContextParamForPointMall) getIntent().getSerializableExtra(DATA);
        orderList = new ArrayList<>();
        mSpcartController = new SPCartController(this, mHandler);
        mController = new OrderController(this, mHandler);
        headView = new SpcartOrderHeadView(this);
        expandableListView.setGroupIndicator(null);
        expandableListView.addHeaderView(headView);
        mAdapter = new SPCartOderAdapter(this, expandableListView, orderList);
        expandableListView.setAdapter(mAdapter);

        showLoadingView("正在加载,请稍候...");
        mSpcartController.doGetCreateOrderContextForPointMall(this, mCreateOrderContextParamForPointMall);

        initClick();
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.activity_spcartorder, null);
    }

    @Override
    public View onLoadNavView() {
        mTopBarView = new SpCartTopBarView(this);
        mTopBarView.setTitle("订单填写");
        mTopBarView.setEditBtnGone();
        mTopBarView.setmNoticeBtnGone();
        return mTopBarView;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }

    private void initClick() {
        mTopBarView.setBackClickListener(new SpCartTopBarView.BackClickListener() {
            @Override
            public void back() {
//                if (!isPayComplete) {
                mOrderCancelDialog = DialogOrder.cancelOrder(SPCartOrderActivity.this);
//                } else {
//                    finish();
//                }
            }
        });

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });

        mSpcartOrderBottomView.setSpcartOrderBottomViewClickListener(new SpcartOrderBottomView.SpcartOrderBottomViewClick() {
            @Override
            public void gotoDeal() {
                configParams();
            }
        });

        mAdapter.setSPCartOrderAdapterClickListener(new SPCartOderAdapter.SPCartOrderAdapterClick() {
            @Override
            public void gotoChooseCoupon(long sellerId, long totalPrice, int groupPostion, TextView couponTextView) {
                mGroupPostion = groupPostion;
                mCouponTextView = couponTextView;
                NavUtils.gotoOrderCouponActivity(SPCartOrderActivity.this, sellerId, totalPrice, CHOOSECOUPON);
            }
        });

        headView.setSpcartOrderHeadViewClickListener(new SpcartOrderHeadView.SpcartOrderHeadViewClick() {
            @Override
            public void noAddressClick() {
                NavUtils.gotoAddressListActivity(SPCartOrderActivity.this, SELECTADRESS, SPCartOrderActivity.class.getSimpleName());
            }

            @Override
            public void arrowAddressClick() {
                NavUtils.gotoAddressListActivity(SPCartOrderActivity.this, SELECTADRESS, SPCartOrderActivity.class.getSimpleName());
            }

            @Override
            public void swithCheck(boolean isChecked) {
                setBottomAllPriceView();
            }
        });
    }


    // 提交订单
    private void configParams() {
        if (addressId <= 0) {
            ToastUtil.showToast(this, "请选择收货地址");
            return;
        }
        showLoadingView("提交订单中...");
        doCreateOrder();
    }

    private void doCreateOrder() {
        mSpcartController.doCreateBatchOrder(this, getCreateBatchOrderParam(orderList));
    }

    /**
     * 组装提交订单的参数
     *
     * @param orderList
     * @return
     */
    private CreateBatchOrderParam getCreateBatchOrderParam(List<Shop> orderList) {
        if (orderList == null || orderList.size() == 0) {
            return null;
        }
        CreateBatchOrderParam params = new CreateBatchOrderParam();
        List<ItemParamForCreateOrder> mItemParamForCreateOrderList = new ArrayList<>();
        List<LeaveMassage> mLeaveMassageList = new ArrayList<>();
        List<Voucher> mVoucherList = new ArrayList<>();
        for (int i = 0; i < orderList.size(); i++) {
            if (orderList.get(i).voucherResult != null) {
                Voucher mVoucher = new Voucher();
                mVoucher.voucherId = orderList.get(i).voucherResult.id;
                mVoucher.sellerId = orderList.get(i).sellerId;
                mVoucherList.add(mVoucher);
            }
            if (!TextUtils.isEmpty(orderList.get(i).mLeaveMessage)) {
                LeaveMassage mLeaveMassage = new LeaveMassage();
                mLeaveMassage.leaveMassage = orderList.get(i).mLeaveMessage;
                mLeaveMassage.sellerId = orderList.get(i).sellerId;
                mLeaveMassageList.add(mLeaveMassage);
            }

            if (orderList.get(i).orderItemList != null && orderList.get(i).orderItemList.size() != 0) {
                for (int t = 0; t < orderList.get(i).orderItemList.size(); t++) {
                    ItemParamForCreateOrder mItemParamForCreateOrder = new ItemParamForCreateOrder();
                    mItemParamForCreateOrder.itemId = orderList.get(i).orderItemList.get(t).itemId;
                    mItemParamForCreateOrder.buyAmount = orderList.get(i).orderItemList.get(t).buyAmount;
                    mItemParamForCreateOrder.sellerId = orderList.get(i).sellerId;
                    mItemParamForCreateOrder.itemType = ItemType.NORMAL;
                    mItemParamForCreateOrderList.add(mItemParamForCreateOrder);
                }
            }
        }
        params.itemParamList = mItemParamForCreateOrderList;
        params.voucherList = mVoucherList;
        params.leaveMassageList = mLeaveMassageList;
        if (headView.getSwitchChecked()) {
            params.usePoint = setDisCount(mMimeTotalPoint, originalTotalFee, allPoint, getAllListVoucher(orderList));
        } else {
            params.usePoint = 0;
        }
        params.addressId = addressId;
        params.itemType = ItemType.NORMAL;
        return params;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case ValueConstants.SPCART_CreateOrderContextForPointMall_OK:
                //TODO 获取购物车下单上下文接口成功返回结果
                hideLoadingView();
                mCreateOrderContextForPointMall = (CreateOrderContextForPointMall) msg.obj;
                if (mCreateOrderContextForPointMall != null) {
                    mController.doTotalPointQuery(this);
                } else {
                    ToastUtil.showToast(SPCartOrderActivity.this, "获取数据失败");
                }
                break;
            case ValueConstants.SPCART_CreateOrderContextForPointMall_ERROR:
                hideLoadingView();
                if (msg.arg1 == ApiCode.SOLD_OUT_6000101 || msg.arg1 == ApiCode.ITEM_NOT_AVAILABLE_6000022) {
                    showSoldOuDialog();
                } else {
                    ToastUtil.showToast(SPCartOrderActivity.this, "获取数据失败");
                }
                break;
            case ValueConstants.POINT_TOTAL_OK:
                //TODO 获取个人积分成功返回结果
                mMimeTotalPoint = (Long) msg.obj;
                handleData(mCreateOrderContextForPointMall);
                break;
            case ValueConstants.POINT_TOTAL_ERROR:
                headView.setMimeUsePoint("" + mMimeTotalPoint);
                break;
            case ValueConstants.SPCART_CreateBatchOrder_OK:
                //TODO 提交订单成功返回的结果
                CreateOrderResultTOList mCreateOrderResultTOList = (CreateOrderResultTOList) msg.obj;
                handleOrderData(mCreateOrderResultTOList);
                break;
            case ValueConstants.SPCART_CreateBatchOrder_ERROR:
                hideLoadingView();
                if (msg.arg1 == ApiCode.SOLD_OUT_6000101 || msg.arg1 == ApiCode.ITEM_NOT_AVAILABLE_6000022) {
                    showSoldOuDialog();
                } else {
                    ToastUtil.showToast(SPCartOrderActivity.this, StringUtil.handlerErrorCode(getApplicationContext(), msg.arg1));
                }
                break;
            case ALLPOINT:
                hideLoadingView();
//                Toast.makeText(SPCartOrderActivity.this, "下单成功", Toast.LENGTH_SHORT).show();
                NavUtils.gotoMyOrderAllListActivity(SPCartOrderActivity.this);
                finish();
                break;
        }
    }

    private void handleData(CreateOrderContextForPointMall mCreateOrderContextForPointMall) {
        if (mCreateOrderContextForPointMall == null) {
            return;
        }

        if (mCreateOrderContextForPointMall.shopList != null && mCreateOrderContextForPointMall.shopList.size() != 0) {
            orderList.addAll(mCreateOrderContextForPointMall.shopList);
            mAdapter.notifyDataSetChanged();
        } else {
            orderList.clear();
            mAdapter.notifyDataSetChanged();
        }

        if (mCreateOrderContextForPointMall.defaultAddress != null) {
            addressId = mCreateOrderContextForPointMall.defaultAddress.addressId;
            headView.setAddress(mCreateOrderContextForPointMall.defaultAddress);
        }

        mSpcartOrderBottomView.setAllPrice(mCreateOrderContextForPointMall.originalTotalFee);
        mSpcartOrderBottomView.setDiscountPrice(0);

        originalTotalFee = mCreateOrderContextForPointMall.originalTotalFee;
        allPoint = mCreateOrderContextForPointMall.allPoint;
        headView.setMimeUsePoint("" + mMimeTotalPoint);
        long discount = setDisCount(mMimeTotalPoint, originalTotalFee, allPoint, 0);
        headView.setIntegralTips(String.format(getResources().getString(R.string.point_order_tips), String.valueOf(discount)));
//        headView.setIntegralMoney(StringUtil.convertPriceNoSymbolExceptLastZeroWithFlag(SPCartOrderActivity.this,
//                setDisCount(mMimeTotalPoint, originalTotalFee, allPoint, 0)));
        headView.setIntegralMoney(StringUtil.pointToYuanOne(discount*10));

        if (setDisCount(mMimeTotalPoint, mCreateOrderContextForPointMall.originalTotalFee, mCreateOrderContextForPointMall.allPoint, 0) <= 0) {
            headView.setSwitchCheckBoxChecked(false);
            headView.setSwitchCheckBoxClickable(false);
        } else {
            headView.setSwitchCheckBoxChecked(true);
            headView.setSwitchCheckBoxClickable(true);
        }

//        if (headView.getSwitchChecked()) {
//            mSpcartOrderBottomView.setAllPrice(originalTotalFee - setDisCount(mMimeTotalPoint, originalTotalFee, allPoint, 0));
//        } else {
//            mSpcartOrderBottomView.setAllPrice(originalTotalFee);
//        }
        setBottomAllPriceView();

        //处理无库存和已下家商品
        if (mCreateOrderContextForPointMall.invalidOrderItemList != null && mCreateOrderContextForPointMall.invalidOrderItemList.size() != 0) {
            showSoldOuDialog();
        }
    }

    // 提交订单成功处理返回的数据
    private void handleOrderData(CreateOrderResultTOList result) {
        if (result == null) {
            hideLoadingView();
            if (isAllPointPay()) {
                Toast.makeText(SPCartOrderActivity.this, "兑换失败", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(SPCartOrderActivity.this, getResources().getString(R.string.line_order_tips_failure), Toast.LENGTH_SHORT).show();
            }
            return;
        }

        if (isOrderSuccess(result)) {//下单成功
            if (isAllPointPay()) {
//                mHandler.sendEmptyMessageDelayed(ALLPOINT, 2000);
                // TODO: 2018/3/1
                String orderIds = "";
                if (result.mainOrderList != null && result.mainOrderList.size() > 0){
                    for (int i = 0 ; i < result.mainOrderList.size(); i++){
                        if (i == 0){
                            orderIds = String.valueOf(result.mainOrderList.get(i).bizOrder.bizOrderId);
                        }else {
                            orderIds = orderIds + "," + String.valueOf(result.mainOrderList.get(i).bizOrder.bizOrderId);
                        }
                    }
                }
                String url = SPUtils.getPonitPayUrl(this).trim();
                if (url != null && !url.isEmpty()) {
                    url = url.replaceAll(":orderId", orderIds);
                    NavUtils.startWebview(this, "", url, 0);
                    finish();
                }
            } else {
                hideLoadingView();

                // TODO: 2018/3/1
                String orderIds = "";
                if (result.mainOrderList != null && result.mainOrderList.size() > 0){
                    for (int i = 0 ; i < result.mainOrderList.size(); i++){
                        if (i == 0){
                            orderIds = String.valueOf(result.mainOrderList.get(i).bizOrder.bizOrderId);
                        }else {
                            orderIds = orderIds + "," + String.valueOf(result.mainOrderList.get(i).bizOrder.bizOrderId);
                        }
                    }
                }
                String url = SPUtils.getPonitPayUrl(this).trim();
                if (url != null && !url.isEmpty()) {
                    url = url.replaceAll(":orderId", orderIds);
                    NavUtils.startWebview(this, "", url, 0);
                    finish();
                }
//                NavUtils.gotoSPCartPayActivity(SPCartOrderActivity.this, getAllOrderIdS(result), totalPrice);
//                finish();
            }
        } else {
            hideLoadingView();
            Toast.makeText(SPCartOrderActivity.this, getResources().getString(R.string.line_order_tips_failure), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 判断下单是否成功
     *
     * @param result
     * @return
     */
    private boolean isOrderSuccess(CreateOrderResultTOList result) {
        if (result == null) {
            return false;
        }

        if (result.mainOrderList == null || result.mainOrderList.size() == 0) {
            return false;
        }

        boolean isSuccess = true;
//        for (int i = 0; i < result.mainOrderList.size(); i++) {
//            if (!result.value.get(i).success) {
//                isSuccess = false;
//                break;
//            }
//        }
        return isSuccess;
    }

//    /**
//     * 获取所有的orderId
//     *
//     * @param result
//     * @return
//     */
//    private long[] getAllOrderIdS(CreateOrderResultTOList result) {
//        if (result == null) {
//            return null;
//        }
//
//        if (result.value == null || result.value.size() == 0) {
//            return null;
//        }
//
//        List<Long> mBizOrderIdList = new ArrayList<>();
//        for (int i = 0; i < result.value.size(); i++) {
//            if (result.value.get(i).mainOrder.detailOrders != null && result.value.get(i).mainOrder.detailOrders.size() != 0) {
//                for (int t = 0; t < result.value.get(i).mainOrder.detailOrders.size(); t++) {
//                    mBizOrderIdList.add(result.value.get(i).mainOrder.detailOrders.get(t).bizOrder.bizOrderId);
//                }
//            }
//        }
//
//        long[] ids = new long[mBizOrderIdList.size()];
//        for (int i = 0; i < mBizOrderIdList.size(); i++) {
//            ids[i] = mBizOrderIdList.get(i);
//        }
//        return ids;
//    }

    /**
     * 获取所有的orderId
     *
     * @param result
     * @return
     */
    private long[] getAllOrderIdS(CreateOrderResultTOList result) {
        if (result == null) {
            return null;
        }

//        if (result.value == null || result.value.size() == 0) {
//            return null;
//        }
        if (result.mainOrderList == null || result.mainOrderList.size() == 0) {
            return null;
        }

        List<Long> mBizOrderIdList = new ArrayList<>();
        for (int i = 0; i < result.mainOrderList.size(); i++) {
            if (result.mainOrderList.get(i).bizOrder != null) {
                mBizOrderIdList.add(result.mainOrderList.get(i).bizOrder.bizOrderId);
            }
        }

        long[] ids = new long[mBizOrderIdList.size()];
        for (int i = 0; i < mBizOrderIdList.size(); i++) {
            ids[i] = mBizOrderIdList.get(i);
        }
        return ids;
    }

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
                            headView.setMyInfoAddress(myAddress);
                        }
                    }
                    break;
                case CHOOSECOUPON:
                    if (data != null) {
                        VoucherResult voucherResult = (VoucherResult) data.getSerializableExtra(SPUtils.EXTRA_DATA);
                        mCouponTextView.setText("-" + StringUtil.converRMb2YunWithFlag(SPCartOrderActivity.this, voucherResult.value));
                        orderList.get(mGroupPostion).voucherResult = voucherResult;
                        headView.setIntegralTips(String.format(getResources().getString(R.string.point_order_tips),
                                setDisCount(mMimeTotalPoint, originalTotalFee, allPoint, getAllListVoucher(orderList))));
                        headView.setIntegralMoney(StringUtil.convertPriceNoSymbolExceptLastZeroWithFlag(SPCartOrderActivity.this,
                                setDisCount(mMimeTotalPoint, originalTotalFee, allPoint, getAllListVoucher(orderList))));
                        if (setDisCount(mMimeTotalPoint, originalTotalFee, allPoint, getAllListVoucher(orderList)) <= 0) {
                            headView.setSwitchCheckBoxChecked(false);
                            headView.setSwitchCheckBoxClickable(false);
                        } else {
                            headView.setSwitchCheckBoxClickable(true);
                        }
                        setBottomAllPriceView();
                    }
                    break;
            }
        } else if (resultCode == RESULT_CANCELED) {
            switch (requestCode) {
                case CHOOSECOUPON:
                    VoucherResult voucherResult = null;
                    orderList.get(mGroupPostion).voucherResult = voucherResult;
                    mCouponTextView.setText("");
                    headView.setIntegralTips(String.format(getResources().getString(R.string.point_order_tips),
                            setDisCount(mMimeTotalPoint, originalTotalFee, allPoint, getAllListVoucher(orderList))));
                    headView.setIntegralMoney(StringUtil.convertPriceNoSymbolExceptLastZeroWithFlag(SPCartOrderActivity.this,
                            setDisCount(mMimeTotalPoint, originalTotalFee, allPoint, getAllListVoucher(orderList))));
                    if (setDisCount(mMimeTotalPoint, originalTotalFee, allPoint, getAllListVoucher(orderList)) <= 0) {
                        headView.setSwitchCheckBoxChecked(false);
                        headView.setSwitchCheckBoxClickable(false);
                    } else {
                        headView.setSwitchCheckBoxClickable(true);
                    }
                    setBottomAllPriceView();
                    break;
                case SELECTADRESS:
                    if (data != null) {
                        DeleteMyAdress mDeleteAdress = (DeleteMyAdress) data.getSerializableExtra(SPUtils.EXTRA_DATA);
                        if (mDeleteAdress != null) {
                            deleteAdress(mDeleteAdress);
                        }
                    }
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
            headView.setNoAddressLayoutVisibility(true);
            addressId = -1;
        }
    }

    /**
     * 获取总的使用的优惠券
     *
     * @param orderList
     * @return
     */
    private long getAllListVoucher(List<Shop> orderList) {
        if (orderList == null || orderList.size() == 0) {
            return 0;
        }
        long totalPoint = 0;
        for (int i = 0; i < orderList.size(); i++) {
            if (orderList.get(i).voucherResult != null) {
                totalPoint += orderList.get(i).voucherResult.value;
            }
        }

        return totalPoint;
    }

    /**
     * 计算可用积分
     *
     * @param mMimeTotalPoint 我的总积分
     * @param totalPrice      所有商品总价
     * @param allMaxUserPoint 所有可用积分
     * @param discoutPrice    优惠券总价
     * @return
     */
    private long setDisCount(long mMimeTotalPoint, long totalPrice, long allMaxUserPoint, long discoutPrice) {
        long minTotalPoint = allMaxUserPoint >= mMimeTotalPoint ? mMimeTotalPoint : allMaxUserPoint;
        long a = (totalPrice - discoutPrice) >= minTotalPoint ? minTotalPoint : (totalPrice - discoutPrice);
        return a <= 0 ? 0 : a;
    }

    /**
     * 设置底部价格显示
     */

    long totalPrice = 0;

    private void setBottomAllPriceView() {
        if (headView.getSwitchChecked()) {   //    使用了积分抵扣
            if (getAllListVoucher(orderList) <= 0) {
                long discount = setDisCount(mMimeTotalPoint, originalTotalFee, allPoint, 0);
                totalPrice = (originalTotalFee - discount*10) <= 0 ? 0 : (originalTotalFee - discount*10);
                mSpcartOrderBottomView.setAllPrice(totalPrice);
                mSpcartOrderBottomView.setDiscountPrice(discount*10);
            } else {
                long discount = setDisCount(mMimeTotalPoint, originalTotalFee, allPoint, getAllListVoucher(orderList));
                totalPrice = (originalTotalFee -  discount*10 - getAllListVoucher(orderList) <= 0) ? 0 : (originalTotalFee - discount*10 - getAllListVoucher(orderList));

//                totalPrice = (originalTotalFee - setDisCount(mMimeTotalPoint, originalTotalFee,
//                        allPoint, getAllListVoucher(orderList)) - getAllListVoucher(orderList) <= 0) ? 0
//                        : (originalTotalFee - setDisCount(mMimeTotalPoint, originalTotalFee, allPoint,
//                        getAllListVoucher(orderList)) - getAllListVoucher(orderList));
//                mSpcartOrderBottomView.setAllPrice(totalPrice);
                mSpcartOrderBottomView.setDiscountPrice(discount*10
                        + getAllListVoucher(orderList));
            }
        } else {
            if (getAllListVoucher(orderList) <= 0) {
                totalPrice = originalTotalFee;
                mSpcartOrderBottomView.setAllPrice(originalTotalFee);
                mSpcartOrderBottomView.setDiscountPrice(0);
            } else {
                totalPrice = (originalTotalFee - getAllListVoucher(orderList)) <= 0 ? 0 : (originalTotalFee - getAllListVoucher(orderList));
                mSpcartOrderBottomView.setAllPrice(totalPrice);
                mSpcartOrderBottomView.setDiscountPrice(getAllListVoucher(orderList));
            }
        }
    }


    /**
     * 判断是否是全积分兑换
     *
     * @return
     */
    private boolean isAllPointPay() {
        if (headView.getSwitchChecked()) {
            if (getAllListVoucher(orderList) <= 0) {
                return (originalTotalFee/10 - setDisCount(mMimeTotalPoint, originalTotalFee, allPoint, 0)) <= 0 ? true : false;
            } else {
                return (originalTotalFee/10 - setDisCount(mMimeTotalPoint, originalTotalFee, allPoint, getAllListVoucher(orderList))
                        - getAllListVoucher(orderList) <= 0) ? true : false;
            }
        } else {
            if (getAllListVoucher(orderList) <= 0) {
                return originalTotalFee/10 <= 0 ? true : false;
            } else {
                return (originalTotalFee/10 - getAllListVoucher(orderList)) <= 0 ? true : false;
            }
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
//            if (!isPayComplete) {
            mOrderCancelDialog = DialogOrder.cancelOrder(SPCartOrderActivity.this);
//            } else {
//                finish();
//            }
        }
        return true;
    }

    private Dialog mSoldOutDialog;

    private void showSoldOuDialog() {
        if (mSoldOutDialog == null) {
            mSoldOutDialog = SPCartDialog.showSoldOutDialog(SPCartOrderActivity.this, "您选择的商品中存在库存不足或已下架的商品,请您返回购物车重新选择商品进行下单!",
                    "返回购物车",
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mSoldOutDialog.dismiss();
                            SPCartOrderActivity.this.finish();
                        }
                    });
        }
        mSoldOutDialog.show();
    }
}
