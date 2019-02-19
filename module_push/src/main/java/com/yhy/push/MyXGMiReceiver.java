package com.yhy.push;

import android.content.Context;
import android.text.TextUtils;

import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageReceiver;
import com.yhy.common.utils.JSONUtils;

/**
 * 自定义接收器
 * <p/>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyXGMiReceiver extends PushMessageReceiver {
    private static final String TAG = "JPush";



    public void onReceivePassThroughMessage(Context var1, MiPushMessage var2) {
        try {
            ReceiverHelper.getInstance().processThrougthPass(var1,
                    var2.getContent());
        }catch (Exception e){
            e.printStackTrace();
        }

//        System.out.println();
//        NotificationMessageEntity notificationMessageEntity = new NotificationMessageEntity();
//        showInNotificationBar(var1, notificationMessageEntity, var2.getTitle(), var2.getDescription());
    }

    public void onNotificationMessageClicked(Context var1, MiPushMessage var2) {
//        System.out.println("xg--- onNotificationMessageClicked --> " +
//                var2.getAlias() +
//                " " + var2.getCategory() +
//                " " + var2.getContent() +
//                " " + var2.getDescription() +
//                " " + var2.getTitle() +
//                " " + var2.getTopic() +
//                " " + var2.getUserAccount() +
//                " " + var2.getExtra() +
//                " " + var2.getPassThrough());
//        try {
//            ReceiverHelper.getInstance().processNotification(var1,
//                    var2.getTitle(),
//                    var2.getContent(),
//                    JSON.toJSONString(var2.getExtra()));
//        }catch (Exception e){
//            e.printStackTrace();
//        }

    }

    public void onNotificationMessageArrived(Context var1, MiPushMessage var2) {
        try {
            ReceiverHelper.getInstance().dispatchNotificationEvent(var1,
                    var2.getTitle(),
                    (TextUtils.isEmpty(var2.getDescription()) ? var2.getContent() : var2.getDescription()),
                    JSONUtils.toJson(var2.getExtra()));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void onReceiveRegisterResult(Context var1, MiPushCommandMessage var2) {
    }

    public void onCommandResult(Context var1, MiPushCommandMessage var2) {
//        System.out.println("xg--- onCommandResult --> " +
//                var2.getCommand() +
//                " " + var2.getCategory() +
//                " " + var2.getReason() +
//                " " + var2.getCommandArguments() +
//                " " + var2.getResultCode());
    }

}
