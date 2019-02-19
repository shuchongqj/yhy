package com.quanyan.yhy.ui.common;

import android.content.Context;
import android.os.Handler;

import com.quanyan.base.BaseController;
import com.quanyan.yhy.net.NetManager;
import com.quanyan.yhy.net.lsn.OnResponseListener;
import com.yhy.common.constants.ValueConstants;

/**
 * Created with Android Studio.
 * Title:WebController
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:徐学东
 * Date:16/6/16
 * Time:下午5:55
 * Version 1.1.0
 */
public class WebController extends BaseController{
    public WebController(Context context, Handler handler) {
        super(context, handler);
    }

    /**
     * 获取WTK
     */
    public void doGetWapLoginToken(final Context context){
        NetManager.getInstance(context).doGetWapLoginToken(new OnResponseListener<String>() {
            @Override
            public void onComplete(boolean isOK, String result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(ValueConstants.MSG_GET_WTK_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_GET_WTK_KO, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_GET_WTK_KO, errorCode, 0, errorMessage);
            }
        });
    }
}
