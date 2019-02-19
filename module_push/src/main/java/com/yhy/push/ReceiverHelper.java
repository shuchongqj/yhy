package com.yhy.push;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.lidroid.xutils.util.LogUtils;
import com.quanyan.yhy.libanalysis.Analysis;
import com.quncao.lark.R;
import com.yhy.common.beans.im.NotificationMessageEntity;
import com.yhy.common.constants.NotificationConstants;
import com.yhy.common.eventbus.event.NotificationEvent;
import com.yhy.common.utils.SPUtils;
import com.yhy.router.YhyRouter;
import com.yhy.service.IUserService;

import org.jboss.netty.util.internal.StringUtil;
import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;

public class ReceiverHelper {
    private static final ReceiverHelper ourInstance = new ReceiverHelper();

    private String lastExtra = "";

    @Autowired
    IUserService userService;
    public static ReceiverHelper getInstance() {
        return ourInstance;
    }

    private ReceiverHelper() {
        YhyRouter.getInstance().inject(this);
    }

    public void processThrougthPass(Context context, String extras){
        try {
            if (("" + lastExtra).equals(extras)){
                return ;
            }
            lastExtra = extras;
            //解析数据
            JSONObject jsonObject = new JSONObject(extras);
            String title = jsonObject.optString("title", "");
            String message = jsonObject.optString("content", "");
            String version = jsonObject.optString(NotificationConstants.VERSION);
            if (!TextUtils.isEmpty(version) && NotificationConstants.VERSION_2.equals(version)) {
                //如果通知version是2.0 叶斌版本
                NotificationMessageEntity entity = new NotificationMessageEntity(extras, message, title);
                if (userService.isLogin()) {
                    NotificationEvent notificationEvent = new NotificationEvent(NotificationEvent.Event.RECEIVE, entity);
                    EventBus.getDefault().post(notificationEvent);
//                    showInNotificationBar(context, entity, title, message, null);
                } else {
//                    showInNotificationBar(context, entity, title, message, null);
                }
            } else {
                //老版本
                int bizType = jsonObject.optInt(NotificationConstants.KEY_BIZ_TYPE);
                int bizSubType = jsonObject.optInt(NotificationConstants.KEY_BIZ_SUB_TYPE);
                if (bizType == NotificationConstants.BIZ_TYPE_TRANSACTION && bizSubType == NotificationConstants.LOGIN_FIRST_BEGIN_SUB_TYPE) {
                    NavUtilsProxy.gotoFirstLoginDialogActivity(context, message);
                } else if (bizType == NotificationConstants.BIZ_TYPE_TRANSACTION || bizType == NotificationConstants.BIZ_TYPE_INTERACTION) {
                    NotificationMessageEntity entity = null;
                    entity = new NotificationMessageEntity(message, extras);
                    entity.setCreateTime(System.currentTimeMillis());
//                    showInNotificationBar(context, entity, "", "", null);
                    if (userService.isLogin()) {
                        NotificationEvent notificationEvent = new NotificationEvent(NotificationEvent.Event.RECEIVE, entity);
                        EventBus.getDefault().post(notificationEvent);
                    }
                }
            }
        } catch (JSONException e) {
            LogUtils.d("unknown jpush reciver");
        }
    }

    public void processHuaWeiThrougthPass(Context context, String extras){
        try {
            if (("" + lastExtra).equals(extras)){
                return ;
            }
            lastExtra = extras;
            //解析数据
            JSONObject jsonObject = new JSONObject(extras);
            String title = jsonObject.optString("title", "");
            String message = jsonObject.optString("content", "");
            jsonObject = new JSONObject(message);
            String version = jsonObject.optString(NotificationConstants.VERSION);
            if (!TextUtils.isEmpty(version) && NotificationConstants.VERSION_2.equals(version)) {
                //如果通知version是2.0 叶斌版本
                NotificationMessageEntity entity = new NotificationMessageEntity(jsonObject.toString(), message, title);
                Analysis.notificationReceive(context, entity.getMessage());
                if (userService.isLogin()) {
                    NotificationEvent event = new NotificationEvent(NotificationEvent.Event.RECEIVE, entity);
                    EventBus.getDefault().post(event);
//                    showInNotificationBar(context, entity, title, entity.getMessage(), null);
                } else {
                    showInNotificationBar(context, entity, title, entity.getMessage(), null);
                }
            } else {
                //老版本
                int bizType = jsonObject.optInt(NotificationConstants.KEY_BIZ_TYPE);
                int bizSubType = jsonObject.optInt(NotificationConstants.KEY_BIZ_SUB_TYPE);
                if (bizType == NotificationConstants.BIZ_TYPE_TRANSACTION && bizSubType == NotificationConstants.LOGIN_FIRST_BEGIN_SUB_TYPE) {
                    NavUtilsProxy.gotoFirstLoginDialogActivity(context, message);
                } else if (bizType == NotificationConstants.BIZ_TYPE_TRANSACTION
                        || bizType == NotificationConstants.BIZ_TYPE_INTERACTION
                        ) {
                    NotificationMessageEntity entity = new NotificationMessageEntity(jsonObject.optString("content"), jsonObject.toString());
                    entity.setCreateTime(System.currentTimeMillis());
//                    showInNotificationBar(context, entity, entity.getTitle(), entity.getMessage(), null);
                    if (userService.isLogin()) {
                        NotificationEvent notificationEvent = new NotificationEvent(NotificationEvent.Event.RECEIVE, entity);
                        EventBus.getDefault().post(notificationEvent);
                    }
                }
            }
        } catch (JSONException e) {
            LogUtils.d("unknown jpush reciver");
        }
    }

    public void dispatchNotificationEvent(Context context, String title, String message, String extras){
        try {
            if (("" + lastExtra).equals(extras)){
                return ;
            }
            lastExtra = extras;
            //解析数据
            JSONObject jsonObject = new JSONObject(extras);
            try {
                String intentUrl = jsonObject.getString("intent_uri");
                Uri uri = Uri.parse(intentUrl);
                jsonObject = new JSONObject(uri.getQueryParameter("extra"));

            }catch (Exception e){
                e.printStackTrace();
            }

            String version = jsonObject.optString(NotificationConstants.VERSION);
            if (!TextUtils.isEmpty(version) && NotificationConstants.VERSION_2.equals(version)) {
                //如果通知version是2.0 叶斌版本
                NotificationMessageEntity entity = new NotificationMessageEntity(jsonObject.toString(), message, title);
                if (!entity.isNewVersion()) {
                    return;
                }
                if (userService.isLogin()) {
                    NotificationEvent event = new NotificationEvent(NotificationEvent.Event.RECEIVE, entity);
                    event.setNeedNotification(false);
                    Analysis.notificationReceive(context, entity.getMessage());
                    EventBus.getDefault().post(event);
//                    showInNotificationBar(context, entity, title, entity.getMessage(), null);
                } else {
//                    showInNotificationBar(context, entity, title, entity.getMessage(), null);
                }

            }
            else {
                //老版本
                int bizType = jsonObject.optInt(NotificationConstants.KEY_BIZ_TYPE);
                int bizSubType = jsonObject.optInt(NotificationConstants.KEY_BIZ_SUB_TYPE);
                if (bizType == NotificationConstants.BIZ_TYPE_TRANSACTION && bizSubType == NotificationConstants.LOGIN_FIRST_BEGIN_SUB_TYPE) {
//                    NavUtils.gotoFirstLoginDialogActivity(context, message);
                } else if (bizType == NotificationConstants.BIZ_TYPE_TRANSACTION || bizType == NotificationConstants.BIZ_TYPE_INTERACTION) {
                    NotificationMessageEntity entity = null;
                    entity = new NotificationMessageEntity(message, extras);
                    entity.setCreateTime(System.currentTimeMillis());
                    if (userService.isLogin()) {
                        NotificationEvent notificationEvent = new NotificationEvent(NotificationEvent.Event.RECEIVE, entity);
                        EventBus.getDefault().post(notificationEvent);
                    }
                }
            }

        } catch (Exception e) {
            LogUtils.d("unknown jpush reciver");
        }
    }

    public boolean processNotification(Context context, String title, String message, String extras, Runnable onStartActivity){
        try {
            if (("" + lastExtra).equals(extras)){
                return false;
            }
            lastExtra = extras;
            //解析数据
            JSONObject jsonObject = new JSONObject(extras);
            String version = jsonObject.optString(NotificationConstants.VERSION);
            if (!TextUtils.isEmpty(version) && NotificationConstants.VERSION_2.equals(version)) {
                //如果通知version是2.0 叶斌版本
                NotificationMessageEntity entity = new NotificationMessageEntity(jsonObject.toString(), message, title);
                if (!entity.isNewVersion()) {
                    return false;
                }
                Analysis.notificationClick(context, entity.getMessage());
                Intent intent = NativeUtilsProxy.getIntent(entity.getNtfOperationCode(), entity.getNtfOperationVaule(), context, false);

                if (intent != null) {
                    if (onStartActivity != null){
                        onStartActivity.run();
                    }
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    return true;
                }

//                context.startActivity(intent);
            }
            else {
                //老版本
                int bizType = jsonObject.optInt(NotificationConstants.KEY_BIZ_TYPE);
                int bizSubType = jsonObject.optInt(NotificationConstants.KEY_BIZ_SUB_TYPE);
                if (bizType == NotificationConstants.BIZ_TYPE_TRANSACTION && bizSubType == NotificationConstants.LOGIN_FIRST_BEGIN_SUB_TYPE) {
                    NavUtilsProxy.gotoFirstLoginDialogActivity(context, message);
                } else if (bizType == NotificationConstants.BIZ_TYPE_TRANSACTION || bizType == NotificationConstants.BIZ_TYPE_INTERACTION) {
                    NotificationMessageEntity entity = null;
                    entity = new NotificationMessageEntity(message, extras);
                    entity.setCreateTime(System.currentTimeMillis());
                    //showInNotificationBar(context, entity);
//                    if (SPUtils.isLogin(context)) {
//
//                    }
                }
            }
            return false;

        } catch (Exception e) {
            LogUtils.d("unknown jpush reciver");
        }
        return false;
    }

    /**
     * 显示通知栏
     */
    private void showInNotificationBar(Context context, NotificationMessageEntity entity, String title, String message, Integer notifyId) {
        System.err.println("push======showInNotificationBar" + title + " " + message);
        NotificationManager notifyMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notifyMgr == null) return;
        if (entity == null) return;
        if (!entity.isNewVersion()) {
            return;
        }
        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(message)) return;

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle(title);
        builder.setContentText(message);

        builder.setSmallIcon(R.mipmap.app_launch_icon);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.app_launch_icon));
        builder.setTicker(message);
        builder.setWhen(System.currentTimeMillis());
        builder.setAutoCancel(true);


        Intent intent = NativeUtilsProxy.getIntent(entity.getNtfOperationCode(), entity.getNtfOperationVaule(), context, false);

        if (intent != null) {
            Intent clickIntent = new Intent(context, ReceiveNotificationActivity.class);
            clickIntent.putExtra("clickIntent", intent);
            clickIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pendingIntent;
            if (notifyId != null){
                notifyMgr.cancel(notifyId);
                pendingIntent = PendingIntent.getActivity(context, notifyId, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            }else{
                pendingIntent = PendingIntent.getActivity(context, (int) entity.getOutId(), clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            }
            builder.setContentIntent(pendingIntent);
        }

        Notification notification = builder.build();
        if (notifyId != null){
            notifyMgr.notify(notifyId, notification);
        }else{
            notifyMgr.notify((int) entity.getOutId(), notification);
        }

    }
}