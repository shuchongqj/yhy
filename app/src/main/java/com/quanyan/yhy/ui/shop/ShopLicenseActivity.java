package com.quanyan.yhy.ui.shop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.quanyan.base.BaseActivity;
import com.quanyan.base.baseenum.IActionTitleBar;
import com.quanyan.base.util.StringUtil;
import com.quanyan.base.view.BaseNavView;
import com.quanyan.base.yminterface.ErrorViewClick;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.ErrorCode;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.shop.controller.ShopController;
import com.yhy.common.beans.net.model.master.Qualification;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.CommonUtil;
import com.yhy.common.utils.SPUtils;
import com.yhy.imageloader.ImageLoadManager;

/**
 * Created with Android Studio.
 * Title:ShopLicenseActivity
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-8-23
 * Time:15:35
 * Version 1.1.0
 */

public class ShopLicenseActivity extends BaseActivity {

    private BaseNavView mBaseNavView;
    private TextView mTVTitleName;
    private ImageView mIVLicenseImg;
    private long mSellerId;
    private ShopController mController;
    private String mSellName;

    public static void gotoShopLicenseActivity(Context context, long sellerId, String sellName) {
        Intent intent = new Intent(context, ShopLicenseActivity.class);
        intent.putExtra(SPUtils.EXTRA_ID, sellerId);
        intent.putExtra(SPUtils.EXTRA_NAME, sellName);
        context.startActivity(intent);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mController = new ShopController(this, mHandler);
        mSellerId = getIntent().getLongExtra(SPUtils.EXTRA_ID, -1);
        mSellName = getIntent().getStringExtra(SPUtils.EXTRA_NAME);
        mTVTitleName = (TextView) findViewById(R.id.tv_title_name);
        mIVLicenseImg = (ImageView) findViewById(R.id.iv_license_img);

        if(!StringUtil.isEmpty(mSellName)){
            mTVTitleName.setText(mSellName);
        }

        if(mSellerId != -1){
            doNetService();
        }else {
            ToastUtil.showToast(this, getString(R.string.error_params));
        }
    }

    private void doNetService() {
        showLoadingView("");
        mController.doQueryMerchantQualification(this, mSellerId);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        hideLoadingView();
        hideErrorView(null);
        switch (msg.what){
            case ValueConstants.MSG_QUERY_SHOP_CARD_OK:
                Qualification value = (Qualification) msg.obj;
                if(value != null){
                    handleData(value);
                }
                break;
            case ValueConstants.MSG_QUERY_SHOP_CARD_KO:
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

    private void handleData(Qualification value) {
        if (!StringUtil.isEmpty(value.saleLicensePic)) {
            ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(value.saleLicensePic), R.mipmap.icon_default_310_180, mIVLicenseImg);

        } else {
            mIVLicenseImg.setImageResource(R.mipmap.icon_default_310_180);
        }
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.ac_shop_license, null);
    }

    @Override
    public View onLoadNavView() {
        mBaseNavView = new BaseNavView(this);
        mBaseNavView.setTitleText(R.string.shop_picture);
        return mBaseNavView;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }
}
