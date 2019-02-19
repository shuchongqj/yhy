package com.quanyan.yhy.ui.shop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.quanyan.base.BaseActivity;
import com.quanyan.base.baseenum.IActionTitleBar;
import com.quanyan.base.util.StringUtil;
import com.quanyan.base.view.BaseNavView;
import com.quanyan.base.yminterface.ErrorViewClick;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.ErrorCode;
import com.quanyan.yhy.ui.shop.controller.ShopController;
import com.yhy.common.beans.net.model.master.MerchantDesc;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.SPUtils;

/**
 * Created with Android Studio.
 * Title:ShopSimpleIntroduceActivity
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-8-23
 * Time:15:14
 * Version 1.1.0
 */

public class ShopSimpleIntroduceActivity extends BaseActivity {

    private BaseNavView mBaseNavView;
    private TextView mTVShow;
    private long mSellerId;
    private ShopController mController;

    public static void gotoShopSimpleIntroduceActivity(Context context, long sellerId) {
        Intent intent = new Intent(context, ShopSimpleIntroduceActivity.class);
        intent.putExtra(SPUtils.EXTRA_ID, sellerId);
        context.startActivity(intent);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mController = new ShopController(this, mHandler);
        mSellerId = getIntent().getLongExtra(SPUtils.EXTRA_ID, -1);
        mTVShow = (TextView) findViewById(R.id.tv_info);
        if(mSellerId != -1){
            doNetService();
        }
    }

    private void doNetService() {
        showLoadingView("");
        mController.doQueryMerchantDesc(this, mSellerId);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        hideLoadingView();
        hideErrorView(null);
        switch (msg.what){
            case ValueConstants.MSG_QUERY_SHOP_DESC_OK:
                MerchantDesc value = (MerchantDesc) msg.obj;
                if(value != null){
                    handleData(value);
                }
                break;
            case ValueConstants.MSG_QUERY_SHOP_DESC_KO:
                showErrorView(null, ErrorCode.NETWORK_UNAVAILABLE == msg.arg1 ?
                        IActionTitleBar.ErrorType.NETUNAVAILABLE : IActionTitleBar.ErrorType.ERRORNET, "", getString(R.string.error_view_network_loaderror_content), "", new ErrorViewClick() {
                    @Override
                    public void onClick(View view) {
                        doNetService();
                    }
                });
                break;
        }
    }

    private void handleData(MerchantDesc value) {
        if(!StringUtil.isEmpty(value.shopDesc)){
            mTVShow.setText(value.shopDesc);
        }else {
            showNoDataPage();
        }

    }

    private void showNoDataPage() {
        showErrorView(null, IActionTitleBar.ErrorType.EMPTYVIEW, getString(R.string.label_shop_info_null), " ", "", null);
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.ac_shop_simple_introduce, null);
    }

    @Override
    public View onLoadNavView() {
        mBaseNavView = new BaseNavView(this);
        mBaseNavView.setTitleText(R.string.shop_simple_introduce);
        return mBaseNavView;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }
}
