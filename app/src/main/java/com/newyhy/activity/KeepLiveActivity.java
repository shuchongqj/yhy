package com.newyhy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.newyhy.utils.android_system.SystemUtils;
import com.newyhy.utils.step.ScreenManager;

/**
 * Created by yangboxue on 2018/5/9.
 */

public class KeepLiveActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        window.setGravity(Gravity.LEFT | Gravity.TOP);
        WindowManager.LayoutParams params = window.getAttributes();

        params.x = 0;
        params.y = 0;
        params.width = 1;
        params.height = 1;
        window.setAttributes(params);

        ScreenManager.getScreenManagerInstance(this).setSingleActivity(this);

    }

    @Override
    protected void onDestroy() {
//        if(Contants.DEBUG)
        if (!SystemUtils.isAppaLive(this, getPackageName())) {
            Intent intentAlive = new Intent(this, HomeMainTabActivity.class);
            intentAlive.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intentAlive);
        }
        super.onDestroy();
    }
}
