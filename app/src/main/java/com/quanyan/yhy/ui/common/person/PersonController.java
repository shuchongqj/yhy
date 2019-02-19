package com.quanyan.yhy.ui.common.person;

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
 * Title:mPersonController
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:和平必胜、正义必胜、人民必胜
 * Author:炎黄子孙
 * Date:15/10/31
 * Time:上午10:22
 * Version 1.0
 */
public class PersonController extends BaseController{

    public PersonController(Context context, Handler handler) {
        super(context, handler);
    }

    /**
     * 获取游客列表
     *
     */
    public void doGetVisitorList(final Context context, Long userId){
        NetManager.getInstance(context).doGetVisitorList(userId, new OnResponseListener<UserContact_ArrayResp>() {
            @Override
            public void onComplete(boolean isOK, UserContact_ArrayResp result, int errorCode, String errorMsg) {
                if (isOK) {
                    if(result == null){
                        result = new UserContact_ArrayResp();
                    }
                    sendMessage(ValueConstants.MSG_GET_VISITIOR_LIST_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_GET_VISITIOR_LIST_KO, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_GET_VISITIOR_LIST_KO, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 新增游客
     * @param userContact
     */
    public void doAddOrUpdateVisitorInfo(final Context context, UserContact userContact){
        NetManager.getInstance(context).doAddOrUpdateVisitorInfo(userContact, new OnResponseListener<UserContact>() {
            @Override
            public void onComplete(boolean isOK, UserContact result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(ValueConstants.MSG_ADD_UPDATE_VISITOR_INFO_OK);
                    return;
                }
                sendMessage(ValueConstants.MSG_ADD_UPDATE_VISITOR_INFO_KO, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_ADD_UPDATE_VISITOR_INFO_KO, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 修改游客
     * @param userContact
     */
    public void doUpdateVisitorInfo(final Context context, UserContact userContact){
        NetManager.getInstance(context).doUpdateVisitorInfo(userContact, new OnResponseListener<Boolean>() {
            @Override
            public void onComplete(boolean isOK, Boolean result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(ValueConstants.MSG_UPDATE_VISITOR_OK);
                    return;
                }
                sendMessage(ValueConstants.MSG_UPDATE_VISITOR_KO, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_UPDATE_VISITOR_KO, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 删除游客
     * @param userContact
     */
    public void doDeleteVisitor(final Context context, UserContact userContact){
        NetManager.getInstance(context).doDeleteVisitor(userContact, new OnResponseListener<Boolean>() {
            @Override
            public void onComplete(boolean isOK, Boolean result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(ValueConstants.MSG_DELETE_VISITOR_OK);
                    return;
                }
                sendMessage(ValueConstants.MSG_DELETE_VISITOR_KO, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_DELETE_VISITOR_KO, errorCode, 0, errorMessage);
            }
        });
    }


    /**
     * 获取联系人列表
     * @param pageIndex
     * @param pageSize
     *//*
    public void doGetContactList(int pageIndex,int pageSize){
        NetManager.getInstance(context).doGetContactList(pageIndex, pageSize, new OnResponseListener<ArrayList<ContactInfo>>() {
            @Override
            public void onComplete(boolean isOK, ArrayList<ContactInfo> result, int errorCode, String errorMsg) {
                if (isOK) {
                    if (result == null) {
                        result = new ArrayList<ContactInfo>();
                    }
                    sendMessage(MSG_GET_CONTACT_LIST_OK, result);
                    return;
                }
                sendMessage(MSG_GET_CONTACT_LIST_KO, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(MSG_GET_CONTACT_LIST_KO, errorCode, 0, errorMessage);
            }
        });
    }

    *//**
     * 新增或更新联系人
     * @param contactInfo
     *//*
    public void doAddOrUpdateContactInfo(ContactInfo contactInfo){
        NetManager.getInstance(context).doAddOrUpdateContactInfo(contactInfo, new OnResponseListener<Boolean>() {
            @Override
            public void onComplete(boolean isOK, Boolean result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(MSG_ADD_UPDATE_CONTACT_INFO_OK);
                    return;
                }
                sendMessage(MSG_ADD_UPDATE_CONTACT_INFO_KO, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(MSG_ADD_UPDATE_CONTACT_INFO_KO, errorCode, 0, errorMessage);
            }
        });
    }

    *//**
     * 删除联系人
     * @param userContact
     *//*
    public void doDeleteContact(UserContact userContact){
        ToastUtil.showToast(context, "长按删除");
        NetManager.getInstance(context).doDeleteContact(userContact, new OnResponseListener<Boolean>() {
            @Override
            public void onComplete(boolean isOK, Boolean result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(MSG_DELETE_CONTACT_OK);
                    return;
                }
                sendMessage(MSG_DELETE_CONTACT_KO, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(MSG_DELETE_CONTACT_KO, errorCode, 0, errorMessage);
            }
        });
    }*/
}
