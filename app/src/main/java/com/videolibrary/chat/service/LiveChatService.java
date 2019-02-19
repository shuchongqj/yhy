package com.videolibrary.chat.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.mogujie.tt.utils.Logger;
import com.videolibrary.chat.manager.LiveChatHeartBeatManager;
import com.videolibrary.chat.manager.LiveChatLoginManager;
import com.videolibrary.chat.manager.LiveChatMessageManager;
import com.videolibrary.chat.manager.LiveChatSocketManager;
import com.videolibrary.chat.manager.LiveReconnectManager;

/**
 * 直播聊天服务器
 */
public class LiveChatService extends Service {
    private Logger logger = Logger.getLogger(LiveChatService.class);

    public LiveChatLoginManager liveChatLoginManager = LiveChatLoginManager.instance();
    public LiveChatHeartBeatManager liveChatHeartBeatManager = LiveChatHeartBeatManager.instance();
    public LiveChatSocketManager liveChatSocketManager = LiveChatSocketManager.instance();
    public LiveReconnectManager liveReconnectManager = LiveReconnectManager.instance();
    public LiveChatMessageManager liveChatMessageManager = LiveChatMessageManager.instance();

    /**
     * binder
     */
    private LiveChatServiceBinder binder = new LiveChatServiceBinder();


    public class LiveChatServiceBinder extends Binder {
        public LiveChatService getService() {
            return LiveChatService.this;
        }
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return binder;
    }

    @Override
    public void onCreate() {
        logger.i("LiveChatService onCreate");
        super.onCreate();
        liveChatLoginManager.onStartIMManager(this);
        liveChatHeartBeatManager.onStartIMManager(this);
        liveChatSocketManager.onStartIMManager(this);
        liveReconnectManager.onStartIMManager(this);
        liveChatMessageManager.onStartIMManager(this);
//        startForeground((int) System.currentTimeMillis(), new Notification());
    }

    @Override
    public void onDestroy() {
        logger.i("LiveChatService onDestroy");
//        EventBus.getDefault().unregister(this);
        liveChatHeartBeatManager.doOnStop();
        liveReconnectManager.doOnStop();
        liveChatSocketManager.doOnStop();
        liveChatLoginManager.doOnStop();
        super.onDestroy();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        logger.d("LiveChatService#onTaskRemoved");
        this.stopSelf();
    }

}
