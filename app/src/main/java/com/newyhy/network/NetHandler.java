package com.newyhy.network;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * Created by Jiervs on 2018/4/17.
 */

public class NetHandler extends Handler {

    private WeakReference<NetHandlerCallback> mCallback;

    public NetHandler(NetHandlerCallback callback) {
        mCallback = new WeakReference<>(callback);
    }

    @Override
    public void handleMessage(Message msg) {
        if (mCallback!=null && mCallback.get() != null) {
            mCallback.get().handleMessage(msg);
        }
    }

    public interface NetHandlerCallback {
        void handleMessage(Message msg);
    }
}
