package com.yhy.push;

import android.content.Context;
import android.content.Intent;

import com.meizu.cloud.pushsdk.MzPushMessageReceiver;
import com.meizu.cloud.pushsdk.platform.message.PushSwitchStatus;
import com.meizu.cloud.pushsdk.platform.message.RegisterStatus;
import com.meizu.cloud.pushsdk.platform.message.SubAliasStatus;
import com.meizu.cloud.pushsdk.platform.message.SubTagsStatus;
import com.meizu.cloud.pushsdk.platform.message.UnRegisterStatus;

/**
 * 自定义接收器
 * <p/>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyXGMZReceiver extends MzPushMessageReceiver {
    private static final String TAG = "JPush";


    @Override
    public void onNotificationArrived(Context context, String s, String s1, String s2) {
        super.onNotificationArrived(context, s, s1, s2);
        ReceiverHelper.getInstance().dispatchNotificationEvent(context,
                s,
                s1,
                s2);
    }

    @Override
    public void onNotificationClicked(Context context, String s, String s1, String s2) {
        super.onNotificationClicked(context, s, s1, s2);

    }

    @Override
    public void onNotifyMessageArrived(Context context, String s) {
        super.onNotifyMessageArrived(context, s);
    }

    @Override
    public void onRegister(Context context, String s) {

    }

    @Override
    public void onMessage(Context context, String s) {
//        System.out.println("xg--- onMessage --> " +
//                s);
    }

    @Override
    public void onUnRegister(Context context, boolean b) {

    }

    @Override
    public void onPushStatus(Context context, PushSwitchStatus pushSwitchStatus) {

    }

    @Override
    public void onRegisterStatus(Context context, RegisterStatus registerStatus) {

    }

    @Override
    public void onUnRegisterStatus(Context context, UnRegisterStatus unRegisterStatus) {

    }

    @Override
    public void onSubTagsStatus(Context context, SubTagsStatus subTagsStatus) {

    }

    @Override
    public void onSubAliasStatus(Context context, SubAliasStatus subAliasStatus) {

    }

    @Override
    public void onReceive(Context context, Intent intent) {


        super.onReceive(context, intent);
    }
}
