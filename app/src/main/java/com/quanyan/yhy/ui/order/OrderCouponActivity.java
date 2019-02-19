package com.quanyan.yhy.ui.order;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.baseenum.IActionTitleBar;
import com.quanyan.base.view.customview.pulltorefresh.PullToRefreshBase;
import com.quanyan.base.view.customview.pulltorefresh.PullToRefreshObservableListView;
import com.quanyan.base.yminterface.ErrorViewClick;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.AnalyDataValue;
import com.quanyan.yhy.common.CouponStatus;
import com.quanyan.yhy.common.ErrorCode;
import com.quanyan.yhy.ui.base.utils.TCEventHelper;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.coupon.adapter.CouponInfoListAdapter;
import com.quanyan.yhy.ui.order.controller.OrderController;
import com.smart.sdk.api.resp.Api_TRADEMANAGER_QueryOrderVoucherDTO;
import com.smart.sdk.api.resp.Api_TRADEMANAGER_QuerySellerVoucher;
import com.yhy.common.beans.net.model.tm.OrderVoucherResult;
import com.yhy.common.beans.net.model.tm.VoucherResult;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.SPUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with Android Studio.
 * Title:OrderCouponActivity
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-7-7
 * Time:15:23
 * Version 1.0
 * Description:
 */
public class OrderCouponActivity extends BaseActivity {

    private static final String SELLERID = "SELLERID";
    private static final String TOTALPRICE = "TOTALPRICE";
    private static final int PAGESIZE = 10;

    private OrderTopView mOrderTopView;
    @ViewInject(R.id.id_stickynavlayout_innerscrollview)
    private PullToRefreshObservableListView mPullListView;
    private ListView mListView;

    @ViewInject(R.id.tv_order_submit)
    private Button mButton;
    @ViewInject(R.id.rl_bottom)
    private RelativeLayout mRelBottom;
    private CouponInfoListAdapter mCouponInfoListAdapter;
    public List<VoucherResult> mList;

    private long sellerId;
    private long totalPrice;

    private OrderController mController;

    private int pageNo = 1;

    public static void gotoOrderCouponActivity(Activity context, long sellerId, long totalPrice, int requestCode) {
        Intent intent = new Intent(context, OrderCouponActivity.class);
        intent.putExtra(SELLERID, sellerId);
        intent.putExtra(TOTALPRICE, totalPrice);
        context.startActivityForResult(intent, requestCode);
    }


    @Override
    protected void initView(Bundle savedInstanceState) {
        ViewUtils.inject(this);
        mController = new OrderController(this, mHandler);
        sellerId = getIntent().getLongExtra(SELLERID, -1);
        totalPrice = getIntent().getLongExtra(TOTALPRICE, -1);
        mListView = mPullListView.getRefreshableView();
//        mPullListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mCouponInfoListAdapter = new CouponInfoListAdapter(this, mList, 1);
        mListView.setAdapter(mCouponInfoListAdapter);
        doGetCoupon();
        initClick();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            Intent intent = new Intent();
            setResult(0x10, intent);
            OrderCouponActivity.this.finish();
        }
        return true;
    }

    private void initClick() {
        mOrderTopView.setBackClickListener(new OrderTopView.BackClickListener() {
            @Override
            public void clickBack() {
                Intent intent = new Intent();
                setResult(0x10, intent);
                OrderCouponActivity.this.finish();
            }
        });

        mPullListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ObservableListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ObservableListView> refreshView) {
                pageNo = 1;
                doGetCoupon();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ObservableListView> refreshView) {
                pageNo++;
                doGetCoupon();
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tcEvent((VoucherResult) mCouponInfoListAdapter.getItem(position - 1));
                if (((VoucherResult) mCouponInfoListAdapter.getItem(position - 1)).status.equals(CouponStatus.ACTIVE)) {
                    Intent intent = new Intent();
                    intent.putExtra(SPUtils.EXTRA_DATA, (VoucherResult) mCouponInfoListAdapter.getItem(position - 1));
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                } else {
//                    ToastUtil.showToast(OrderCouponActivity.this, "该优惠券不可用");
                }
            }
        });

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(RESULT_CANCELED, intent);
                OrderCouponActivity.this.finish();
            }
        });
    }

    private void tcEvent(VoucherResult voucherResult) {
        if (voucherResult != null) {
            Map<String, String> map = new HashMap();
            map.put(AnalyDataValue.KEY_CID, voucherResult.id + "");
            map.put(AnalyDataValue.KEY_CNAME, voucherResult.title);
            if (voucherResult.sellerResult != null) {
                map.put(AnalyDataValue.KEY_SELLER_NAME, voucherResult.sellerResult.merchantName);
            }
            map.put(AnalyDataValue.KEY_FULL_PRICE, voucherResult.requirement + "");
            map.put(AnalyDataValue.KEY_REDUCE_PRICE, voucherResult.value + "");
            TCEventHelper.onEvent(this, AnalyDataValue.MINE_COUPON_LIST_ITEM, map);
        }

    }

    /**
     * 请求订单优惠券接口
     */
    private void doGetCoupon() {
        if (sellerId <= 0) {
            return;
        }
        List<Api_TRADEMANAGER_QuerySellerVoucher> list = new ArrayList<Api_TRADEMANAGER_QuerySellerVoucher>();
        Api_TRADEMANAGER_QuerySellerVoucher querySellerVoucher = new Api_TRADEMANAGER_QuerySellerVoucher();
        querySellerVoucher.sellerId = sellerId;
        querySellerVoucher.totalPrice = totalPrice;
        list.add(querySellerVoucher);
        Api_TRADEMANAGER_QueryOrderVoucherDTO queryOrderVoucherDTO = new Api_TRADEMANAGER_QueryOrderVoucherDTO();
        queryOrderVoucherDTO.querySellerVoucher = list;
        queryOrderVoucherDTO.pageSize = PAGESIZE;
        queryOrderVoucherDTO.pageNo = pageNo;
        mController.doQueryOrderVoucherList(this, queryOrderVoucherDTO);
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.activity_ordercoupon, null);
    }

    @Override
    public View onLoadNavView() {
        mOrderTopView = new OrderTopView(this);
        mOrderTopView.setOrderTopTitle("优惠券");
        return mOrderTopView;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        mPullListView.onRefreshComplete();
        hideErrorView(null);
        switch (msg.what) {
            case ValueConstants.MSG_CREATE_ORDER_OK:
                OrderVoucherResult result = (OrderVoucherResult) msg.obj;
                if (result != null) {
                    if (result.sellerVouchers != null && result.sellerVouchers.size() != 0) {
                        if (result.sellerVouchers.get(0).sellerVoucherList != null && result.sellerVouchers.get(0).sellerVoucherList.size() != 0) {
                            mList = result.sellerVouchers.get(0).sellerVoucherList;
                            if (pageNo == 1) {
                                mCouponInfoListAdapter.setData(mList, true);
                            } else {
                                mCouponInfoListAdapter.setData(mList, false);
                            }
                            mRelBottom.setVisibility(View.VISIBLE);
                        } else {
                            if (pageNo == 1) {
                                if (mList != null) {
                                    mList.clear();
                                    mCouponInfoListAdapter.setData(mList, true);
                                }
                                showNoDataPage();
                            } else {
                                ToastUtil.showToast(OrderCouponActivity.this, R.string.scenic_hasnodata);
                            }
                        }
                    } else {
                        if (pageNo == 1) {
                            if (mList != null) {
                                mList.clear();
                                mCouponInfoListAdapter.setData(mList, true);
                            }
                            showNoDataPage();
                        } else {
                            ToastUtil.showToast(OrderCouponActivity.this, R.string.scenic_hasnodata);
                        }
                    }
                } else {
                    if (pageNo == 1) {
                        if (mList != null) {
                            mList.clear();
                            mCouponInfoListAdapter.setData(mList, true);
                        }
                        showNoDataPage();
                    } else {
                        ToastUtil.showToast(OrderCouponActivity.this, R.string.scenic_hasnodata);
                    }
                }
                break;
            case ValueConstants.MSG_CREATE_ORDER_KO:
                if (pageNo == 1) {
                    showNetErrorView(msg.arg1);
                } else {
                    ToastUtil.showToast(OrderCouponActivity.this, msg.arg1);
                }
                break;
        }
    }

    private void showNoDataPage() {
        showErrorView(null, IActionTitleBar.ErrorType.EMPTYVIEW, getString(R.string.empty_coupon), " ", "", null);
    }

    private void showNetErrorView(int arg1) {
        showErrorView(null, ErrorCode.NETWORK_UNAVAILABLE == arg1 ?
                IActionTitleBar.ErrorType.NETUNAVAILABLE : IActionTitleBar.ErrorType.ERRORNET, "", "", "", new ErrorViewClick() {
            @Override
            public void onClick(View view) {
                doGetCoupon();
            }
        });
    }

}
