package com.quanyan.yhy.ui.common;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.quanyan.base.BaseActivity;
import com.quanyan.yhy.R;

/**
 * Created with Android Studio.
 * Title:WeiXinPublicCodeActivity
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:baojie
 * Date:2015-11-24
 * Time:19:28
 * Version 1.0
 */

public class WeiXinPublicCodeActivity extends BaseActivity {

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            WeiXinPublicCodeActivity.this.finish();
            WeiXinPublicCodeActivity.this.overridePendingTransition(R.anim.push_up_in2, R.anim.push_up_out2);
        }
        return true;
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.ac_weixinpubliccode, null);
    }

    @Override
    public View onLoadNavView() {
        return null;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }
}
