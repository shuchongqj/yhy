package com.quanyan.yhy.ui.common;

import android.content.Context;
import android.os.Handler;

import com.quanyan.base.BaseController;
import com.quanyan.yhy.net.NetManager;
import com.quanyan.yhy.net.lsn.OnResponseListener;
import com.yhy.common.beans.net.model.common.CrashInfoList;
import com.yhy.common.constants.ValueConstants;

/**
 * Created with Android Studio.
 * Title:AboutAndFeedController
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:baojie
 * Date:2015-11-23
 * Time:19:52
 * Version 1.0
 */

public class AboutAndFeedController extends BaseController {


    public AboutAndFeedController(Context context, Handler handler) {
        super(context, handler);
    }


    /**
     * 意见反馈
     * @param feedback
     * @param contract
     */
    public void doFeedback(final Context context, String feedback, String contract){
        NetManager.getInstance(context).doFeedback(feedback, contract, new OnResponseListener<Boolean>() {
            @Override
            public void onComplete(boolean isOK, Boolean result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(ValueConstants.MSG_FEEDBACK_OK);
                    return;
                }
                sendMessage(ValueConstants.MSG_FEEDBACK_KO, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_FEEDBACK_KO, errorCode, 0, errorMessage);
            }
        });
    }

    public void doAddCrash(final Context context, CrashInfoList crashInfo){
        NetManager.getInstance(context).doAddCrash(crashInfo, new OnResponseListener<Boolean>() {
            @Override
            public void onComplete(boolean isOK, Boolean result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(ValueConstants.MSG_FEEDBACK_OK);
                    return;
                }
                sendMessage(ValueConstants.MSG_FEEDBACK_KO, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_FEEDBACK_KO, errorCode, 0, errorMessage);
            }
        });
    }
}
