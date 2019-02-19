package com.yhy.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 自定义接收器
 * <p/>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyXXReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush";

    @Override
    public void onReceive(Context context, Intent intent) {

        if ("com.xxx.notification".equals(intent.getAction())){

        }

    }
}
