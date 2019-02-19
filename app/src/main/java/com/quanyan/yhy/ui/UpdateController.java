package com.quanyan.yhy.ui;

import android.content.Context;
import android.os.Handler;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.quanyan.base.BaseController;
import com.quanyan.yhy.net.NetManager;
import com.quanyan.yhy.net.lsn.OnResponseListener;
import com.yhy.common.beans.net.model.rc.OnlineUpgrade;
import com.yhy.common.beans.net.model.user.UserStatusInfoList;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.SPUtils;
import com.yhy.router.YhyRouter;
import com.yhy.service.IUserService;

/**
 * Created with Android Studio.
 * Title:KickoutAcitivity
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:邓明佳
 * Date:2016/5/3
 * Time:16:46
 * Version 1.0
 */
public class UpdateController extends BaseController {
    public static final int GET_UPDATE_OK = 1;
    public static final int GET_UPDATE_KO = 2;

    @Autowired
    IUserService userService;
    public UpdateController(Context context, Handler handler) {
        super(context, handler);
        YhyRouter.getInstance().inject(this);
    }

    public void doGetUpdateData(final Context context) {
        NetManager.getInstance(context).doGetOnlineUpgrade(new OnResponseListener<OnlineUpgrade>() {
            @Override
            public void onComplete(boolean isOK, OnlineUpgrade upgradeInfo,
                                   int errorCode, String errorMsg) {
//                upgradeInfo= new OnlineUpgrade();
//                upgradeInfo.downloadUrl="www.baidu.com";
//                upgradeInfo.version="2";
//                upgradeInfo.versionName="2.0";
//                upgradeInfo.forceUpgrade=true;
//                upgradeInfo.forceDesc="强制升级";
                if (isOK && upgradeInfo != null) {
                    sendMessage(GET_UPDATE_OK, upgradeInfo);
                }
            }


            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(GET_UPDATE_KO, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 更新状态
     * @param context
     * @param status
     */
    public void doEditUserStatus(Context context,int status){
        NetManager.getInstance(context).doEditUserStatus(userService.getLoginUserId(),status,new OnResponseListener<Boolean>() {
            @Override
            public void onComplete(boolean isOK, Boolean result, int errorCode, String errorMsg) {
                if(isOK){
                    sendMessage(ValueConstants.MSG_EDIT_USER_STATUS_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_EDIT_USER_STATUS_KO, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_EDIT_USER_STATUS_KO, errorCode, 0, errorMessage);
            }
        });
    }

}
