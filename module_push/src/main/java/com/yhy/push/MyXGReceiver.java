package com.yhy.push;

import android.content.Context;
import android.net.Uri;

import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;

/**
 * 自定义接收器
 * <p/>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyXGReceiver extends XGPushBaseReceiver {
    private static final String TAG = "JPush";

    @Override
    public void onRegisterResult(Context context, int i, XGPushRegisterResult xgPushRegisterResult) {

    }

    @Override
    public void onUnregisterResult(Context context, int i) {

    }

    @Override
    public void onSetTagResult(Context context, int i, String s) {

    }

    @Override
    public void onDeleteTagResult(Context context, int i, String s) {

    }

    @Override
    public void onTextMessage(Context context, XGPushTextMessage xgPushTextMessage) {
        System.err.println("xg--- onTextMessage --> " +
                xgPushTextMessage.getTitle() +
                "1 " + xgPushTextMessage.getContent() +
                "2 " + xgPushTextMessage.getCustomContent());

        ReceiverHelper.getInstance().processThrougthPass(context,
                xgPushTextMessage.getContent());
    }

    @Override
    public void onNotifactionClickedResult(Context context, XGPushClickedResult xgPushClickedResult) {
        System.out.println("xg--- onNotifactionClickedResult --> " +
                xgPushClickedResult.getTitle() +
                " " + xgPushClickedResult.getContent() +
                " " + xgPushClickedResult.getCustomContent() +
                " " + xgPushClickedResult.getActivityName() +
                " " + xgPushClickedResult.getActionType());
//        ReceiverHelper.getInstance().processNotification(context,
//                xgPushClickedResult.getTitle(),
//                xgPushClickedResult.getContent(),
//                xgPushClickedResult.getCustomContent());

    }

    @Override
    public void onNotifactionShowedResult(Context context, XGPushShowedResult xgPushShowedResult) {

        try {
            System.out.println("xg--- onNotifactionClickedResult --> " +
                    xgPushShowedResult.getTitle() +
                    " " + xgPushShowedResult.getContent() +
                    " " + xgPushShowedResult.getCustomContent());
            String intentUrl = xgPushShowedResult.getActivity();
            Uri uri = Uri.parse(intentUrl);
            ReceiverHelper.getInstance().dispatchNotificationEvent(context,
                    xgPushShowedResult.getTitle(),
                    xgPushShowedResult.getContent(),
                    uri.getQueryParameter("extra"));
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
