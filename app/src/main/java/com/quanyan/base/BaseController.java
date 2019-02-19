package com.quanyan.base;

import android.content.Context;
import android.os.Handler;

/**
 * Created with Android Studio.
 * Title:BaseController
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:和平必胜、正义必胜、人民必胜
 * Author:炎黄子孙
 * Date:15/10/19
 * Time:下午4:39
 * Version 1.0
 */
public abstract class BaseController {
    protected Handler mUiHandler;

    public BaseController(Context context, Handler handler) {
        mUiHandler = handler;
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
        if (mUiHandler != null) {
            mUiHandler.obtainMessage(what, arg1, arg2, obj).sendToTarget();
        }
    }
}
