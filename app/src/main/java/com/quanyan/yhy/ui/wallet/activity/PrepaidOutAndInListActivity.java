package com.quanyan.yhy.ui.wallet.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;

import com.quanyan.base.BaseListViewActivity;
import com.quanyan.base.baseenum.IActionTitleBar;
import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.R;
import com.quanyan.yhy.ui.adapter.base.BaseAdapterHelper;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.order.OrderTopView;
import com.quanyan.yhy.ui.wallet.Controller.WalletController;
import com.quanyan.yhy.ui.wallet.WalletUtils;
import com.yhy.common.beans.net.model.paycore.Bill;
import com.yhy.common.beans.net.model.paycore.BillList;
import com.yhy.common.constants.ValueConstants;

import java.util.List;

/**
 * Created with Android Studio.
 * Title:***Acitivity
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:田海波
 * Date:2016/6/11
 * Time:16:46
 * Version 2.0.1
 */
public class PrepaidOutAndInListActivity extends BaseListViewActivity<Bill> {
    private OrderTopView mOrderTopView;
    WalletController mController;

    //private boolean mHasNodata = false;
    public static void gotoPrepaidOutAndInListActivity(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, PrepaidOutAndInListActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void fetchData(int pageIndex) {
        startSearchList(false, pageIndex);
    }

    private void showNoDataPage() {
        showErrorView(getBaseDropListView().getListViewViewParent(), IActionTitleBar.ErrorType.EMPTYVIEW, getString(R.string.label_nodata_wonderful_play_list), getString(R.string.label_nodata_wallet_outin_message), "", null);
    }

    @Override
    public void dispatchMessage(Message msg) {
        hideLoadingView();
        hideErrorView(getBaseDropListView().getListViewViewParent());
        switch (msg.what) {
            case ValueConstants.PAY_PAGEQUERYUBILL_SUCCESS://列表尺寸
                BillList mBillList = (BillList) msg.obj;
                handleScenicList(mBillList);
                break;
            case ValueConstants.PAY_PAGEQUERYUBILL_ERROR:
                if (getBaseDropListView().getQuickAdapter().getCount() == 0) {
                    showNetErrorView(getBaseDropListView().getListViewViewParent(), msg.arg1);
                } else {
                    ToastUtil.showToast(this, StringUtil.handlerErrorCode(getApplicationContext(), msg.arg1));
                }
                break;
        }
        getBaseDropListView().getPullRefreshView().onRefreshComplete();
    }

    /**
     *
     */
    private void handleScenicList(BillList mBillList) {
        if (isRefresh()) {

            if (mBillList.billList != null) {
                getBaseDropListView().getQuickAdapter().replaceAll(mBillList.billList);
            } else {
                getBaseDropListView().getQuickAdapter().clear();
                showNoDataPage();
            }
          /*  if (getBaseDropListView().getQuickAdapter().getCount() == 0) {
                mHasNodata = true;
                getBaseDropListView().getQuickAdapter().add(new Bill());
            } else {
                mHasNodata = false;
            }*/
        } else {
            //   mHasNodata = false;
            if (mBillList.billList != null) {
                getBaseDropListView().getQuickAdapter().addAll(mBillList.billList);
            } else {
                ToastUtil.showToast(this, getString(R.string.no_more));
            }
        }
    }

    private void startSearchList(boolean isLoading, final int pageIndex) {
        if (pageIndex == 1) {
            showLoadingView(getString(R.string.loading_text));
            mBaseDropListView.getListView().setSelection(0);
        }

        mController.doGetPageQueryUserBill(pageIndex, ValueConstants.PAGESIZE, PrepaidOutAndInListActivity.this);


    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        showLoadingView(getString(R.string.loading_text));
        mController = new WalletController(this, mHandler);
        //getBaseDropListView().getPullRefreshView().setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        manualRefresh();
        mOrderTopView.setBackClickListener(new OrderTopView.BackClickListener() {
            @Override
            public void clickBack() {
                finish();
            }
        });
    }

    /**
     * 手动调用刷新操作
     */
    protected void manualRefresh() {
        if (mBaseDropListView != null) {
            mBaseDropListView.getListView().setSelection(0);
            onPullDownToRefresh(mBaseDropListView.getPullRefreshView());
        }
    }


    @Override
    public void convertItem(BaseAdapterHelper helper, Bill item) {
        helper.setText(R.id.tv_income_expence_type, WalletUtils.getIncomeExpenceType(item));
        helper.setText(R.id.tv_income_expence_date, WalletUtils.getFormatData(item.transTime));
        long account = WalletUtils.getIncomeExpenceAccount(item);
        helper.setTextColor(R.id.tv_income_expence_balance, account > 0 ? getResources().getColor(R.color.neu_fa4619) : getResources().getColor(R.color.neu_333333));
        helper.setText(R.id.tv_income_expence_balance, account > 0 ? "+" + StringUtil.formatWalletMoneyNoFlg(account) : "" + StringUtil.formatWalletMoneyNoFlg(account));
    }

    @Override
    public int setItemLayout() {
        return R.layout.prepaid_in_out_list_item_view;
    }


    @Override
    public List<String> setTabStrings() {
        return null;
    }


    @Override
    public List<View> setPopViews() {
        return null;
    }

    @Override
    public int getPageSize() {
        return ValueConstants.PAGESIZE;
    }

    @Override
    public void onItemClick(AdapterView parent, View view, int position, long id) {
        NavUtils.gotoDetailAccountActivity(PrepaidOutAndInListActivity.this, (Bill) getAdapter().getItem(position - 1));
    }

    @Override
    public View onLoadNavView() {
        mOrderTopView = new OrderTopView(PrepaidOutAndInListActivity.this);
        mOrderTopView.setOrderTopTitle("零钱明细");
        return mOrderTopView;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }
}