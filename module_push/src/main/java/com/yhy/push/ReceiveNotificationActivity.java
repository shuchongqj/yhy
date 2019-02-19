package com.yhy.push;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.yhy.common.base.YHYBaseApplication;
import com.yhy.router.YhyRouter;
import com.yhy.router.types.MainActivityFromType;


public class ReceiveNotificationActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Intent intent = getIntent();
        if (intent == null){
            finish();
            return;
        }

        Intent i = intent.getParcelableExtra("clickIntent");
        if (i != null){
            startActivity(i);
            finish();
            return;
        }

        Uri uri = intent.getData();
        if (uri != null) {
            String p1 = uri.getQueryParameter("extra");

            ReceiverHelper.getInstance().processNotification(this, "", "", p1, new Runnable() {
                @Override
                public void run() {
                    YhyRouter.getInstance().startMainActivity(ReceiveNotificationActivity.this, MainActivityFromType.START_FROM_NOTIFICATION);
//                    if (GonaApplication.homeMainTabActivity == null){
//                        startActivity(new Intent(ReceiveNotificationActivity.this, HomeMainTabActivity.class));
//                    }
                }
            });

        }

        finish();
    }
}
