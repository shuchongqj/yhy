package com.quanyan.yhy.ui;

import com.yhy.common.base.NoLeakHandler;

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
public abstract class BaseController1 {

    protected void sendMessage(NoLeakHandler handler, int what, Object obj) {
        sendMessage(handler, what, 0, 0, obj);
    }

    protected void sendMessage(NoLeakHandler handler,int what) {
        sendMessage(handler,what, null);
    }

    protected void sendMessage(NoLeakHandler handler,int what, int arg1, int arg2) {
        sendMessage(handler,what, arg1, arg2, null);
    }

    protected void sendMessage(NoLeakHandler handler, int what, int arg1, int arg2, Object obj) {
        if (handler != null) {
            handler.obtainMessage(what, arg1, arg2, obj).sendToTarget();
        }
    }
}
