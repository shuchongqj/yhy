package com.quanyan.yhy.ui.tab.homepage.order;

import android.app.Dialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.quanyan.base.BaseActivity;
import com.quanyan.base.view.BaseNavView;

/**
 * Created with Android Studio.
 * Title:BaseOrderInfoActivtiy
 * Description:订单填基类，处理相同的操作，取消订单，订单提示
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:zhaoxp
 * Date:2015-12-12
 * Time:12:45
 * Version 1.0
 */
public class BaseOrderInfoActivtiy extends BaseActivity {
	protected boolean isPayComplete = false;//是否支付过
	private Dialog mOrderCancelDialog;

	@Override
	public View onLoadContentView() {
		return null;
	}

	private BaseNavView mBaseNavView;
	@Override
	public View onLoadNavView() {
		mBaseNavView = new BaseNavView(this);
		mBaseNavView.setLeftClick(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!isPayComplete) {
					mOrderCancelDialog = DialogOrder.cancelOrder(BaseOrderInfoActivtiy.this);
				}else{
					finish();
				}
			}
		});
		return mBaseNavView;
	}

	@Override
	public boolean isTopCover() {
		return false;
	}

	@Override
	protected void initView(Bundle savedInstanceState) {

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(KeyEvent.KEYCODE_BACK == keyCode){
			if(!isPayComplete) {
				mOrderCancelDialog = DialogOrder.cancelOrder(BaseOrderInfoActivtiy.this);
			}else{
				finish();
			}
		}
		return true;
	}

	@Override
	protected void onStop() {
		super.onStop();
		if(mOrderCancelDialog != null) {
			mOrderCancelDialog.dismiss();
		}
	}
}
