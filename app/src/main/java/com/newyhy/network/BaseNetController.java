package com.newyhy.network;

import android.content.Context;

/**
 * Network Controller
 * Created by Jiervs on 2018/4/17.
 */

public class BaseNetController {

    public static final int MSG_ERROR = 0x9999;//获取数据失败

    protected NetHandler mHandler;

    public BaseNetController(Context context, NetHandler handler) {
        mHandler = handler;
    }

    protected void sendMessage(int what, Object obj) {
        sendMessage(what, 0, 0, obj);
    }

    protected void sendMessage(int what) {
        sendMessage(what, null);
    }

    protected void sendMessage(int what, int arg1, int arg2) {
        sendMessage(what, arg1, arg2, null);
    }

    protected void sendMessage(int what, int arg1, int arg2, Object obj) {
        if (mHandler != null) {
            mHandler.obtainMessage(what, arg1, arg2, obj).sendToTarget();
        }
    }
}
