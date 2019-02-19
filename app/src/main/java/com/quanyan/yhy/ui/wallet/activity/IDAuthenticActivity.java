package com.quanyan.yhy.ui.wallet.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.quanyan.base.BaseActivity;
import com.quanyan.base.view.BaseNavView;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.UpdateIDCardState;
import com.quanyan.yhy.ui.wallet.fragment.IDAuthenticFailFragment;
import com.quanyan.yhy.ui.wallet.fragment.IDAuthenticSuccessFragment;
import com.yhy.common.utils.SPUtils;

import de.greenrobot.event.EventBus;

public class IDAuthenticActivity extends BaseActivity {

    private BaseNavView mBaseNavView;
    private String title;
    boolean authResult;
    @Override
    protected void initView(Bundle savedInstanceState) {
         authResult = this.getIntent().getBooleanExtra(SPUtils.EXTRA_AUTH_RESULT, false);
        String userName = this.getIntent().getStringExtra(SPUtils.EXTRA_USER_NAME);
        String idCard = this.getIntent().getStringExtra(SPUtils.EXTRA_IDCARD);
        String validDate = this.getIntent().getStringExtra(SPUtils.EXTRA_VALIDDATE);

        IDAuthenticSuccessFragment mIDAuthenticSuccessFragment = IDAuthenticSuccessFragment.getInstance(userName, idCard, validDate);
        IDAuthenticFailFragment mIDAuthenticFailFragment = IDAuthenticFailFragment.getInstance();
        if (authResult) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_idauthen, mIDAuthenticSuccessFragment).commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_idauthen, mIDAuthenticFailFragment).commit();
        }
    }


    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.activity_idauthentic, null);
    }

    @Override
    public View onLoadNavView() {
        mBaseNavView = new BaseNavView(this);
        boolean authResult = IDAuthenticActivity.this.getIntent().getBooleanExtra(SPUtils.EXTRA_AUTH_RESULT, false);
        if (authResult) {
            title = "认证成功";
        } else {
            title = "认证失败";
        }
        mBaseNavView.setTitleText(title);
        mBaseNavView.setLeftClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOperator();
            }
        });
        return mBaseNavView;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            showOperator();
        }
        return false;
    }
    public  void  showOperator(){
        if (!authResult) {
            EventBus.getDefault().post(new UpdateIDCardState());
        }
        finish();
    }
}
