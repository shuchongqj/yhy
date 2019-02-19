package com.quanyan.yhy.ui.common.tourist;

import android.content.Context;
import android.os.Handler;

import com.quanyan.base.BaseController;
import com.quanyan.yhy.net.NetManager;
import com.quanyan.yhy.net.lsn.OnResponseListener;
import com.yhy.common.beans.net.model.common.person.UserContact;
import com.yhy.common.beans.net.model.common.person.UserContact_ArrayResp;
import com.yhy.common.constants.ValueConstants;

/**
 * Created with Android Studio.
 * Title:TouristController
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-6-13
 * Time:14:24
 * Version 1.1.0
 */

public class TouristController extends BaseController {

    public TouristController(Context context, Handler handler) {
        super(context, handler);
    }

    /**
     * 添加证件
     *
     * @param userContact
     */
    public void doAddCertificate(final Context context, UserContact userContact) {
        NetManager.getInstance(context).doAddCertificate(userContact, new OnResponseListener<Boolean>() {
            @Override
            public void onComplete(boolean isOK, Boolean result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(ValueConstants.MSG_NEW_ADD_CODE_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_NEW_ADD_CODE_KO, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_NEW_ADD_CODE_KO, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 删除证件
     *
     * @param contactId
     * @param type
     */
    public void doDeleteCertificate(final Context context, long contactId, String type) {
        NetManager.getInstance(context).doDeleteCertificate(contactId, type, new OnResponseListener<Boolean>() {
            @Override
            public void onComplete(boolean isOK, Boolean result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(ValueConstants.MSG_NEW_DELETE_CODE_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_NEW_DELETE_CODE_KO, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_NEW_DELETE_CODE_KO, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 添加用户联系人
     *
     * @param userContact
     */
    public void doAddOrUpdateVisitorInfo(final Context context, UserContact userContact) {
        NetManager.getInstance(context).doAddOrUpdateVisitorInfo(userContact, new OnResponseListener<UserContact>() {
            @Override
            public void onComplete(boolean isOK, UserContact result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(ValueConstants.MSG_NEW_ADD_TOURIST_OK);
                    return;
                }
                sendMessage(ValueConstants.MSG_NEW_ADD_TOURIST_KO, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_NEW_ADD_TOURIST_KO, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 删除用户联系人
     *
     * @param userContact
     */
    public void doDeleteVisitor(final Context context, UserContact userContact) {
        NetManager.getInstance(context).doDeleteVisitor(userContact, new OnResponseListener<Boolean>() {
            @Override
            public void onComplete(boolean isOK, Boolean result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(ValueConstants.MSG_NEW_DELETE_TOURIST_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_NEW_DELETE_TOURIST_KO, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_NEW_DELETE_TOURIST_KO, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 修改用户联系人
     *
     * @param userContact
     */
    public void doUpdateVisitorInfo(final Context context, UserContact userContact) {
        NetManager.getInstance(context).doUpdateVisitorInfo(userContact, new OnResponseListener<Boolean>() {
            @Override
            public void onComplete(boolean isOK, Boolean result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(ValueConstants.MSG_NEW_EDIT_TOURIST_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_NEW_EDIT_TOURIST_KO, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_NEW_EDIT_TOURIST_KO, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 获取用户联系人列表
     *
     * @param userId
     */
    public void doUpdateVisitorInfo(final Context context, long userId) {
        NetManager.getInstance(context).doGetVisitorList(userId, new OnResponseListener<UserContact_ArrayResp>() {
            @Override
            public void onComplete(boolean isOK, UserContact_ArrayResp result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(ValueConstants.MSG_GET_NEW_TOURIST_LIST_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_GET_NEW_TOURIST_LIST_KO, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_GET_NEW_TOURIST_LIST_KO, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 修改证件号
     *
     * @param mUserContact
     */
    public void doUpdateCertificate(final Context context, UserContact mUserContact) {
        NetManager.getInstance(context).doUpdateCertificate(mUserContact, new OnResponseListener<Boolean>() {
            @Override
            public void onComplete(boolean isOK, Boolean result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(ValueConstants.MSG_GET_NEW_TOURISTCODE_UPDATE_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_GET_NEW_TOURISTCODE_UPDATE_KO, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_GET_NEW_TOURISTCODE_UPDATE_KO, errorCode, 0, errorMessage);
            }
        });
    }

}
