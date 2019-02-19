package com.quanyan.yhy.ui.lineabroad.controller;

import android.content.Context;
import android.os.Handler;

import com.quanyan.base.BaseController;
import com.quanyan.yhy.net.NetManager;
import com.quanyan.yhy.net.lsn.OnResponseListener;
import com.yhy.common.beans.net.model.user.DestinationList;
import com.yhy.common.constants.ValueConstants;

/**
 * Created with Android Studio.
 * Title:AbroadController
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-6-1
 * Time:21:09
 * Version 1.1.0
 */

public class AbroadController extends BaseController {

    public AbroadController(Context context, Handler handler) {
        super(context, handler);
    }

    //条件查询目的地
    public void doQueryDestinationTree(final Context context, String type){
        NetManager.getInstance(context).doQueryDestinationTree(type, new OnResponseListener<DestinationList>() {

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_GET_ABROAD_DESTINATION_KO, errorCode, 0, errorMessage);
            }

            @Override
            public void onComplete(boolean isOK, DestinationList result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(ValueConstants.MSG_GET_ABROAD_DESTINATION_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_GET_ABROAD_DESTINATION_KO, errorCode, 0, errorMsg);
            }
        });
    }


}
