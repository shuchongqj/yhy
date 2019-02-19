package com.videolibrary.chat.manager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PowerManager;

import com.mogujie.tt.imservice.callback.Packetlistener;
import com.mogujie.tt.utils.Logger;
import com.videolibrary.chat.event.LiveChatLoginEvent;
import com.videolibrary.chat.event.LiveChatSocketEvent;
import com.videolibrary.chat.protobuf.IMBaseDefine;
import com.videolibrary.chat.protobuf.IMLive;

import de.greenrobot.event.EventBus;

public class LiveChatHeartBeatManager extends LiveChatManager {

    private static LiveChatHeartBeatManager inst = new LiveChatHeartBeatManager();

    public static LiveChatHeartBeatManager instance() {
        return inst;
    }

    private Logger logger = Logger.getLogger(LiveChatHeartBeatManager.class);
    private final int HEARTBEAT_INTERVAL = 2 * 60 * 1000;
//    private final int HEARTBEAT_INTERVAL = 5000;
    private final String ACTION_SENDING_HEARTBEAT = "com.videolibrary.chat.manager.LiveChatHeartBeatManager";
    private PendingIntent pendingIntent;
    private boolean isRegistered = false;
    @Override
    public void doOnStart() {
        if (!EventBus.getDefault().isRegistered(inst)) {
            EventBus.getDefault().register(inst);
        }
    }

    @Override
    public void doOnStop() {
        logger.d("LiveChatHeartBeatManager#onStop");
        try {
            if (isRegistered) {
                getContext().unregisterReceiver(imReceiver);
                isRegistered = false;
            }
            cancelHeartbeatTimer();
            if (EventBus.getDefault().isRegistered(inst)) {
                EventBus.getDefault().unregister(inst);
            }
        } catch (Exception e) {
            logger.e("LiveChatHeartBeatManager#stop error:%s", e.getCause());
        }
    }

    public void onEventMainThread(LiveChatSocketEvent event){
        switch (event){
            case FAIL:
                onLoginFail();
                break;
        }
    }

    public void onEventMainThread(LiveChatLoginEvent event){
        switch (event){
            case FAIL:
                onLoginFail();
                break;
            case SUCESS:
                onLoginSuccess();
                break;
        }
    }

    public void onLoginSuccess() {
        logger.e("LiveChatHeartBeatManager#onLoginOk");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_SENDING_HEARTBEAT);
        logger.d("LiveChatHeartBeatManager#register actions");
        getContext().registerReceiver(imReceiver, intentFilter);
        isRegistered = true;
        //获取AlarmManager系统服务
        scheduleHeartbeat(HEARTBEAT_INTERVAL);
    }

    public void onLoginFail() {
        logger.w("LiveChatHeartBeatManager#onLoginFail");
        cancelHeartbeatTimer();
    }

    private void cancelHeartbeatTimer() {
        logger.w("LiveChatHeartBeatManager#cancelHeartbeatTimer");
        if (pendingIntent == null) {
            logger.w("LiveChatHeartBeatManager#pi is null");
            return;
        }
        AlarmManager am = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        am.cancel(pendingIntent);
    }


    private void scheduleHeartbeat(int seconds) {
        logger.d("LiveChatHeartBeatManager#scheduleHeartbeat every %d seconds", seconds);
        if (pendingIntent == null) {
            logger.w("LiveChatHeartBeatManager#fill in pendingintent");
            Intent intent = new Intent(ACTION_SENDING_HEARTBEAT);
            pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, 0);
            if (pendingIntent == null) {
                logger.w("LiveChatHeartBeatManager#scheduleHeartbeat#pi is null");
                return;
            }
        }

        AlarmManager am = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + seconds, seconds, pendingIntent);
    }


    /**
     * --------------------boradcast-广播相关-----------------------------
     */
    private BroadcastReceiver imReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            logger.w("LiveChatHeartBeatManager#im#receive action:%s", action);
            if (action.equals(ACTION_SENDING_HEARTBEAT)) {
                sendHeartBeatPacket();
            }
        }
    };

    public void sendHeartBeatPacket() {
        logger.d("LiveChatHeartBeatManager#reqSendHeartbeat");
        PowerManager pm = (PowerManager) getContext().getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "livechat_heartBeat_wakelock");
        wl.acquire();
        try {
            final long timeOut = 2 * 1000;
            IMLive.IMGroupOtherMsgHeart imGroupOtherMsgHeart = IMLive.IMGroupOtherMsgHeart.newBuilder().build();
            int sid = IMBaseDefine.ServiceID.SID_OTHER_MSG_VALUE;
            int cid = IMBaseDefine.GroupOtherMsgCmdID.CID_GROUP_OTHER_MSG_HEART_VALUE;
            LiveChatSocketManager.instance().sendRequest(imGroupOtherMsgHeart, sid, cid, new Packetlistener(timeOut) {
                @Override
                public void onSuccess(Object response) {
                    logger.d("LiveChatHeartBeatManager heartbeat#心跳成功，链接保活");
                }

                @Override
                public void onFaild() {
                    logger.w("LiveChatHeartBeatManager heartbeat#心跳包发送失败");
                    LiveChatSocketManager.instance().onMsgServerDisconn();
                }

                @Override
                public void onTimeout() {
                    logger.w("LiveChatHeartBeatManager heartbeat#心跳包发送超时");
                    LiveChatSocketManager.instance().onMsgServerDisconn();
                }
            });
            logger.d("LiveChatHeartBeatManager#send packet to server");
        } finally {
            wl.release();
        }
    }
}
