package com.mogujie.tt.imservice.manager;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.text.TextUtils;

import com.mogujie.tt.DB.sp.ConfigurationSp;
import com.mogujie.tt.imservice.event.UnreadEvent;
import com.mogujie.tt.ui.activity.ChatAcitivity;
import com.mogujie.tt.utils.IMUIHelper;
import com.mogujie.tt.utils.Logger;
import com.quanyan.yhy.R;
import com.quanyan.yhy.ui.base.utils.NativeUtils;
import com.yhy.common.beans.im.NotificationMessageEntity;
import com.yhy.common.beans.im.entity.UnreadEntity;
import com.yhy.common.constants.DBConstant;
import com.yhy.common.constants.IntentConstant;
import com.yhy.common.constants.IntentConstants;
import com.yhy.common.constants.NotificationConstants;
import com.yhy.common.constants.SysConstant;
import com.yhy.common.eventbus.event.NotificationEvent;
import com.yhy.common.utils.CommonUtil;
import com.yhy.push.ReceiveNotificationActivity;

import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * 伪推送; app退出之后就不会收到推送的信息
 * 通知栏新消息通知
 * a.每个session 只显示一条
 * b.每个msg 信息都显示
 * 配置依赖与 configure
 */
public class IMNotificationManager extends IMManager {

    private Logger logger = Logger.getLogger(IMNotificationManager.class);
    private static IMNotificationManager inst = new IMNotificationManager();
    //单人会话未读数
    private Map<Long, Integer> unReadCountSingle = new HashMap<>();
    private Map<Long, Integer> unReadCountConsult = new HashMap<>();
    //通知未读数
    private int unReadCountNotification = 0;

    public static IMNotificationManager instance() {
        return inst;
    }

    private ConfigurationSp configurationSp;

    private IMNotificationManager() {
    }

    @Override
    public void doOnStart() {
        cancelAllNotifications();
    }

    public void onLoginSuccess() {
        long loginId = IMLoginManager.instance().getLoginId();
        configurationSp = ConfigurationSp.instance(ctx, loginId);
        if (!EventBus.getDefault().isRegistered(inst)) {
            EventBus.getDefault().register(inst);
        }
    }

    public void reset() {
        EventBus.getDefault().unregister(this);
        cancelAllNotifications();
    }


    public void onEventMainThread(UnreadEvent event) {
        switch (event.event) {
            case UNREAD_MSG_RECEIVED:
                UnreadEntity unreadEntity = event.entity;
                handleMsgRecv(unreadEntity);
                break;
        }
    }

    private void handleMsgRecv(UnreadEntity entity) {
        logger.d("notification#recv unhandled message");
        long peerId = entity.getPeerId();
        int sessionType = entity.getSessionType();
        logger.d("notification#msg no one handled, peerId:%d, sessionType:%d", peerId, sessionType);
        System.err.println("xg--- onPushMsg --> " + " 222");

//        if (sessionType == DBConstant.SESSION_TYPE_NOTIFICATION) {
//            return;
//        }

        if (CommonUtil.isTopActivy(ctx, ChatAcitivity.class.getName())) {
            PowerManager pm = (PowerManager) ctx.getSystemService(Context.POWER_SERVICE);
            if (pm.isScreenOn()) {
                return;
            }
        }

        //判断是否设定了免打扰
        if (entity.isForbidden()) {
            logger.d("notification#GROUP_STATUS_SHIELD");
            return;
        }

        //PC端是否登陆 取消 【暂时先关闭】
//        if(IMLoginManager.instance().isPcOnline()){
//            logger.d("notification#isPcOnline");
//            return;
//        }

        // 全局开关
        boolean globallyOnOff = configurationSp.getCfg(SysConstant.SETTING_GLOBAL, ConfigurationSp.CfgDimension.NOTIFICATION);
        if (!globallyOnOff) {
            logger.d("notification#shouldGloballyShowNotification is false, return");
            return;
        }

        // 单独的设置
//        boolean singleOnOff = configurationSp.getCfg(entity.getSessionKey(), ConfigurationSp.CfgDimension.NOTIFICATION);
//        if (singleOnOff) {
//            logger.d("notification#shouldShowNotificationBySession is false, return");
//            return;
//        }

        //if the message is a multi login message which send from another terminal,not need notificate to status bar
        // 判断是否是自己的消息
        if (IMLoginManager.instance().getLoginId() != peerId) {
            showNotification(entity);
        }
    }


    public void cancelAllNotifications() {
        logger.d("notification#cancelAllNotifications");
        if (null == ctx) {
            return;
        }
        NotificationManager notifyMgr = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notifyMgr == null) {
            return;
        }
        notifyMgr.cancelAll();
        unReadCountSingle.clear();
        unReadCountConsult.clear();
        unReadCountNotification = 0;
    }


    /**
     * 在通知栏中删除特定回话的状态
     */
    public void cancelSessionNotifications(String sessionType) {
        logger.d("notification#cancelSessionNotifications");
        if (ctx == null) {
            return;
        }
        try {
            NotificationManager notifyMgr = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
            if (null == notifyMgr) {
                return;
            }
            int notificationId = getSessionNotificationId(sessionType);
            notifyMgr.cancel(notificationId);
            if (Long.parseLong(sessionType) == DBConstant.SESSION_TYPE_SINGLE) {
                unReadCountSingle.clear();
            } else if (Long.parseLong(sessionType) == DBConstant.SESSION_TYPE_NOTIFICATION) {
                unReadCountNotification = 0;
            } else if (Long.parseLong(sessionType) == DBConstant.SESSION_TYPE_CONSULT) {
                unReadCountConsult.clear();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    private void showNotification(final UnreadEntity unreadEntity) {
        // todo eric need to set the exact size of the big icon
        // 服务端有些特定的支持 尺寸是不是要调整一下 todo 100*100  下面的就可以不要了
//        ImageSize targetSize = new ImageSize(80, 80);
        long peerId = unreadEntity.getPeerId();
        int sessionType = unreadEntity.getSessionType();
//        String avatarUrl = "";
        String title = "";
        String content = unreadEntity.getLatestMsgData();

        if (unreadEntity.getSessionType() == DBConstant.SESSION_TYPE_SINGLE) {
//            UserEntity contact = IMContactManager.instance().findContact(peerId);
//            String userName = contact == null ? "User_" + peerId : contact.getMainName();

            if (unReadCountSingle.containsKey(peerId)) {
                int unReadCount = unReadCountSingle.get(peerId) + 1;
                unReadCountSingle.put(peerId, unReadCount);
            } else {
                unReadCountSingle.put(peerId, 1);
            }

            int contactCount = unReadCountSingle.size();
            int messageCount = 0;
            for (Map.Entry entry : unReadCountSingle.entrySet()) {
                messageCount = messageCount + (int) entry.getValue();
            }

            if (contactCount > 1) {
                title = ctx.getString(R.string.noti_receiver_muli_message, contactCount, messageCount);
            } else {
                title = ctx.getString(R.string.noti_receiver_single_message);
            }
            content = unreadEntity.getLatestMsgData();

        } else if (unreadEntity.getSessionType() == DBConstant.SESSION_TYPE_NOTIFICATION) {
            unReadCountNotification++;
            title = unreadEntity.getTitle();
//            if (unReadCountNotification > 1) {
//                title = ctx.getString(R.string.noti_receiver_muli_notification, unReadCountNotification);
//            } else {
//                title = ctx.getString(R.string.noti_receiver_single_notification);
//            }
        } else if (unreadEntity.getSessionType() == DBConstant.SESSION_TYPE_CONSULT) {
            if (unReadCountConsult.containsKey(peerId)) {
                int unReadCount = unReadCountConsult.get(peerId) + 1;
                unReadCountConsult.put(peerId, unReadCount);
            } else {
                unReadCountConsult.put(peerId, 1);
            }

            int contactCount = unReadCountConsult.size();
            int messageCount = 0;
            for (Map.Entry entry : unReadCountConsult.entrySet()) {
                messageCount = messageCount + (int) entry.getValue();
            }

            if (contactCount > 1) {
                title = ctx.getString(R.string.noti_receiver_muli_message, contactCount, messageCount);
            } else {
                title = ctx.getString(R.string.noti_receiver_single_message);
            }
            content = unreadEntity.getLatestMsgData();
        }
        //获取头像
//        avatarUrl = IMUIHelper.getRealAvatarUrl(avatarUrl);
//        final String ticker = content;
        //利用sessiontype 获取notificationid 用来做同种消息类型合并
        int notificationId;
        if (unreadEntity.getSessionType() == DBConstant.SESSION_TYPE_NOTIFICATION) {
            notificationId = getSessionNotificationId(String.valueOf(unreadEntity.getLaststMsgId()));
        } else {
            notificationId = getSessionNotificationId(String.valueOf(unreadEntity.getSessionType()));
        }
        Intent intent;
        if (!TextUtils.isEmpty(unreadEntity.getNtfCode())) {
            intent = NativeUtils.getIntent(unreadEntity.getNtfCode(), unreadEntity.getNtfVaule(), ctx, true);
        } else {
            intent = new Intent(ctx, ChatAcitivity.class);
            if (unreadEntity.getSessionType() == DBConstant.SESSION_TYPE_NOTIFICATION) {
                intent.putExtra(IntentConstants.EXTRA_BIZ_TYPE, unreadEntity.getPeerId());
            } else if (unreadEntity.getSessionType() == DBConstant.SESSION_TYPE_SINGLE) {
                if (unReadCountSingle.size() == 1) {
                    intent.putExtra(IntentConstant.KEY_SESSION_KEY, unreadEntity.getSessionKey());
                }
            } else if (unreadEntity.getSessionType() == DBConstant.SESSION_TYPE_CONSULT) {
                if (unReadCountConsult.size() == 1) {
                    intent.putExtra(IntentConstant.KEY_SESSION_KEY, unreadEntity.getSessionKey());
                }
            }
        }


//        Bitmap defaultBitmap = BitmapFactory.decodeResource(ctx.getResources(), IMUIHelper.getDefaultAvatarResId(unreadEntity.getSessionType()));

        if (unreadEntity.isNeedNotify()){
            showInNotificationBar(title, content, null, notificationId, intent);
        }
    }

    private void showInNotificationBar(String title, String ticker, Bitmap iconBitmap, int notificationId, Intent intent) {
        logger.d("notification#showInNotificationBar title:%s ticker:%s", title, ticker);
        System.err.println("push======showInNotificationBar2 " + title + " " + ticker);

        NotificationManager notifyMgr = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notifyMgr == null) {
            return;
        }

        Builder builder = new NotificationCompat.Builder(ctx);
        if (!TextUtils.isEmpty(title)){
            builder.setContentTitle(title);
        }

        if (!TextUtils.isEmpty(ticker)){
            builder.setContentText(ticker);
        }

        builder.setSmallIcon(R.mipmap.app_launch_icon);
        builder.setLargeIcon(BitmapFactory.decodeResource(ctx.getResources(), com.quncao.lark.R.mipmap.app_launch_icon));
        builder.setTicker(ticker);
        builder.setWhen(System.currentTimeMillis());
        builder.setAutoCancel(true);

        // sound
        if (configurationSp.getCfg(SysConstant.SETTING_GLOBAL, ConfigurationSp.CfgDimension.SOUND)) {
            builder.setDefaults(Notification.DEFAULT_SOUND);
        } else {
            logger.d("notification#setting is not using sound");
        }
        if (iconBitmap != null) {
            logger.d("notification#fetch icon from network ok");
            builder.setLargeIcon(iconBitmap);
        } else {
            // do nothint ?
        }
        // if MessageActivity is in the background, the system would bring it to
        // the front
        if (intent != null) {
            Intent clickIntent = new Intent(ctx, ReceiveNotificationActivity.class);
            clickIntent.putExtra("clickIntent", intent);
            clickIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(ctx, notificationId, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);
        }
        Notification notification = builder.build();
        notifyMgr.notify(notificationId, notification);
    }

    // come from
    // http://www.partow.net/programming/hashfunctions/index.html#BKDRHashFunction
    private long hashBKDR(String str) {
        long seed = 131; // 31 131 1313 13131 131313 etc..
        long hash = 0;

        for (int i = 0; i < str.length(); i++) {
            hash = (hash * seed) + str.charAt(i);
        }
        return hash;
    }

    /* End Of BKDR Hash Function */
    public int getSessionNotificationId(String sessionType) {
        logger.d("notification#getSessionNotificationId sessionTag:%s", sessionType);
        int hashedNotificationId = (int) hashBKDR(sessionType);
        logger.d("notification#hashedNotificationId:%d", hashedNotificationId);
        return hashedNotificationId;
    }
}
