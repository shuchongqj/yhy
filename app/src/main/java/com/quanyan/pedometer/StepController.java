package com.quanyan.pedometer;

import android.content.Context;
import android.os.Handler;

import com.quanyan.base.BaseController;
import com.quanyan.yhy.net.NetManager;
import com.quanyan.yhy.net.lsn.OnResponseListener;
import com.yhy.common.beans.net.model.pedometer.PedometerHistoryResultList;
import com.yhy.common.beans.net.model.pedometer.StepParam;
import com.yhy.common.beans.net.model.pedometer.StepResult;
import com.yhy.common.beans.net.model.track.ReceivePointResult;

/**
 * Created with Android Studio.
 * Title:StepController
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:赵晓坡
 * Date:16/7/20
 * Time:17:07
 * Version 1.1.0
 */
public class StepController extends BaseController {

    public static final int MSG_GET_START_POINT_OK = 0x80001;
    public static final int MSG_GET_START_POINIT_KO = 0x80002;

    public static final int MSG_GET_YESTERDAY_POINT_OK = 0x80003;
    public static final int MSG_GET_YESTERDAY_POINT_KO = 0x80004;


    public StepController(Context context, Handler handler) {
        super(context, handler);
    }

    public void getStartPoint(Context context){
        NetManager.getInstance(context).doGetStepStartPoint(new OnResponseListener<ReceivePointResult>() {
            @Override
            public void onComplete(boolean isOK, ReceivePointResult result, int errorCode, String errorMsg) {
                if(isOK){
                    sendMessage(MSG_GET_START_POINT_OK, result);
                    return;
                }
                sendMessage(MSG_GET_START_POINIT_KO, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(MSG_GET_START_POINIT_KO, errorCode, 0, errorMessage);
            }
        });
    }

    public void getYesterdayPoint(Context context){
        NetManager.getInstance(context).doGetStepYesterdayPoint(new OnResponseListener<ReceivePointResult>() {
            @Override
            public void onComplete(boolean isOK, ReceivePointResult result, int errorCode, String errorMsg) {
                if(isOK){
                    sendMessage(MSG_GET_YESTERDAY_POINT_OK, result);
                    return;
                }
                sendMessage(MSG_GET_YESTERDAY_POINT_KO, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(MSG_GET_YESTERDAY_POINT_KO, errorCode, 0, errorMessage);
            }
        });
    }

    public void getHistoryPedometerInfo(Context context){
        NetManager.getInstance(context).doGetHistoryPedometerInfo(new OnResponseListener<PedometerHistoryResultList>() {
            @Override
            public void onComplete(boolean isOK, PedometerHistoryResultList result, int errorCode, String errorMsg) {
                if (isOK && result != null) {
                    sendMessage(StepFragment2.MSG_GET_SERVER_DATA_OK, result);
                    return;
                }
                sendMessage(StepFragment2.MSG_GET_SERVER_DATA_FAIL, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(StepFragment2.MSG_GET_SERVER_DATA_FAIL, errorCode, 0, errorMessage);
            }
        });
    }

    public void doGetCurrentPointByStep(Context context, StepParam stepParam){
        NetManager.getInstance(context).doGetCurrentPointByStep(stepParam, new OnResponseListener<StepResult>() {
            @Override
            public void onComplete(boolean isOK, StepResult result, int errorCode, String errorMsg) {
                if (isOK && result != null && result.success) {
                    sendMessage(StepFragment2.MSG_GET_POINT_INFO_OK, result);
                    return;
                }
                sendMessage(StepFragment2.MSG_GET_POINT_INFO_ERROR, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(StepFragment2.MSG_GET_POINT_INFO_ERROR, errorCode, 0, errorMessage);
            }
        });
    }

}
