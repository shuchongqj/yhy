package com.quanyan.yhy.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.mogujie.tt.imservice.event.LoginEvent;
import com.newyhy.activity.NewPushStreamHorizontalActivity;
import com.newyhy.activity.NewPushStreamVerticalActivity;
import com.quanyan.base.util.LocalUtils;
import com.quanyan.yhy.BaseApplication;
import com.quanyan.yhy.R;
import com.quanyan.yhy.ui.base.utils.NavUtils;

import de.greenrobot.event.EventBus;

/**
 * Created with Android Studio.
 * Title:KickoutAcitivity
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:邓明佳
 * Date:2016/5/3
 * Time:16:46
 * Version 1.0
 */
public class KickoutAcitivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.height = LinearLayout.LayoutParams.MATCH_PARENT;
        layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(layoutParams);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kickout);

        Activity ac = ((BaseApplication) getApplication()).getActivity(NewPushStreamVerticalActivity.class.getSimpleName());
        if (ac != null) {
            NewPushStreamVerticalActivity pushStreamActivity = (NewPushStreamVerticalActivity) ac;
            pushStreamActivity.onKickout();
        }
        Activity horizontal_ac = ((BaseApplication) getApplication()).getActivity(NewPushStreamHorizontalActivity.class.getSimpleName());
        if (ac != null) {
            NewPushStreamHorizontalActivity pushStreamActivity = (NewPushStreamHorizontalActivity) horizontal_ac;
            pushStreamActivity.onKickout();
        }

        findViewById(R.id.relogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocalUtils.JumpToLogin(KickoutAcitivity.this);
                NavUtils.gotoLoginActivity(KickoutAcitivity.this);
                KickoutAcitivity.this.finish();
            }
        });
        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocalUtils.JumpToLogin(KickoutAcitivity.this);
                KickoutAcitivity.this.finish();
            }
        });
        EventBus.getDefault().post(LoginEvent.LOGIN_OUT);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.anim_not_change, R.anim.anim_pop_out);
    }

    @Override
    public void onBackPressed() {
    }
}
