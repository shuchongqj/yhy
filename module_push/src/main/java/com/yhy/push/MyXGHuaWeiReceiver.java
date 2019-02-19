package com.yhy.push;

import android.content.Context;
import android.os.Bundle;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.huawei.hms.support.api.push.PushReceiver;
import com.yhy.common.utils.JSONUtils;

import org.json.JSONObject;

/**
 * 自定义接收器
 * <p/>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyXGHuaWeiReceiver extends PushReceiver {
    private static final String TAG = "JPush";

    @Override
    public void onToken(Context context, String token) {
        super.onToken(context, token);
        PushModuleApplication.registerToken(token);
    }

    @Override
    public void onPushMsg(Context context, byte[] bytes, String s) {
        super.onPushMsg(context, bytes, s);
//        ToastUtil.showToast(context, "xg--- onPushMsg --> " + " " + new String(bytes) + " " + s);

        try {
            System.err.println("xg--- onPushMsg --> " + " " + new String(bytes));

            String content = new String(bytes);
//            ReceiverHelper.getInstance().processThrougthPass(context, jsonObject.get("content").getAsString());
            ReceiverHelper.getInstance().processHuaWeiThrougthPass(context,  new JSONObject(content).toString());
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public boolean onPushMsg(Context context, byte[] bytes, Bundle bundle) {
        return super.onPushMsg(context, bytes, bundle);
    }

    @Override
    public void onEvent(Context context, Event event, Bundle bundle) {
        super.onEvent(context, event, bundle);


//        if (Event.NOTIFICATION_OPENED.equals(event)){
//            try {
//                String extra = bundle.getString("pushMsg");
//
//                JsonArray jsonArray = JSONUtils.parseJsonArray(extra);
//
////                JSONObject jsonObject = new JSONObject();
////                for (int i = 0; i < jsonArray.size();i++){
////                    JSONObject object = jsonArray.getJSONObject(i);
////                    Set<Map.Entry<String, Object>> entries =  object.entrySet();
////                    for (Map.Entry<String, Object> e : entries){
////                        jsonObject.put(e.getKey(), e.getValue());
////                    }
////
////                }
//
//                ReceiverHelper.getInstance().dispatchNotificationEvent(context, "", "", jsonArray.get(0).getAsString());
//
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//
//        }
    }

}
