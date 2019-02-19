package com.videolibrary.chat.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.mogujie.tt.utils.Logger;

/**
 * IMService绑定
 *
 * @modify yingmu
 * 1. 供上层使用【activity】
 * 同层次的manager没有必要使用。
 */
public abstract class LiveChatServiceConnector {
    protected static Logger logger = Logger.getLogger(LiveChatServiceConnector.class);

    public abstract void onIMServiceConnected(LiveChatService chatService);

    public abstract void onServiceDisconnected();

    private LiveChatService liveChatService;

    private ServiceConnection imServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            logger.i("onService(liveChatService)Disconnected");
            LiveChatServiceConnector.this.onServiceDisconnected();
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            logger.i("im#onService(liveChatService)Connected");

            if (liveChatService == null) {
                LiveChatService.LiveChatServiceBinder binder = (LiveChatService.LiveChatServiceBinder) service;
                liveChatService = binder.getService();

                if (liveChatService == null) {
                    logger.e("et liveChatService failed");
                    return;
                }
                logger.d("get liveChatService ok");
            }
            LiveChatServiceConnector.this.onIMServiceConnected(liveChatService);
        }
    };

    public void connect(Context ctx) {
        Intent intent = new Intent();
        intent.setClass(ctx, LiveChatService.class);
        ctx.bindService(intent, imServiceConnection, Context.BIND_AUTO_CREATE);
    }

    public void disconnect(Context ctx) {
        ctx.unbindService(imServiceConnection);
    }

}
