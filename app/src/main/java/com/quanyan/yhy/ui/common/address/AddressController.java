package com.quanyan.yhy.ui.common.address;

import android.content.Context;
import android.os.Handler;

import com.quanyan.base.BaseController;
import com.quanyan.yhy.net.NetManager;
import com.quanyan.yhy.net.lsn.OnResponseListener;
import com.yhy.common.beans.net.model.common.address.MyAddressContentInfo;
import com.yhy.common.beans.net.model.common.address.MyAddress_ArrayResp;
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
public class AddressController extends BaseController{


    public AddressController(Context context, Handler handler) {
        super(context, handler);
    }

    /**
     * 获取地址列表
     * @param userId
     */
    public void doGetAddressList(final Context context, Long userId){
        NetManager.getInstance(context).doGetAddressList(userId, new OnResponseListener<MyAddress_ArrayResp>() {
            @Override
            public void onComplete(boolean isOK, MyAddress_ArrayResp result, int errorCode, String errorMsg) {
                if (isOK) {
                    if (result == null) {
                        result = new MyAddress_ArrayResp();
                    }
                    sendMessage(ValueConstants.MSG_GET_ADDRESS_LIST_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_GET_ADDRESS_LIST_KO, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_GET_ADDRESS_LIST_KO, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 新增地址
     * @param addressContentInfo
     */
    public void doAddOrUpdateAddressInfo(final Context context, MyAddressContentInfo addressContentInfo){
        NetManager.getInstance(context).doAddOrUpdateAddressInfo(addressContentInfo, new OnResponseListener<MyAddressContentInfo>() {
            @Override
            public void onComplete(boolean isOK, MyAddressContentInfo result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(ValueConstants.MSG_ADD_UPDATE_ADDRESS_INFO_OK);
                    return;
                }
                sendMessage(ValueConstants.MSG_ADD_UPDATE_ADDRESS_INFO_KO, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_ADD_UPDATE_ADDRESS_INFO_KO, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 修改地址
     * @param mAddressInfo
     */
    public void doUpdateAddressInfo(final Context context, MyAddressContentInfo mAddressInfo) {
        NetManager.getInstance(context).doUpdateAddressInfo(mAddressInfo, new OnResponseListener<Boolean>() {
            @Override
            public void onComplete(boolean isOK, Boolean result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(ValueConstants.MSG_UPDATE_ADDRESS_OK);
                    return;
                }
                sendMessage(ValueConstants.MSG_UPDATE_ADDRESS_KO, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_UPDATE_ADDRESS_KO, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 删除地址
     * @param addressId
     */
    public void doDeleteAddress(final Context context, Long addressId){
        NetManager.getInstance(context).doDeleteAddress(addressId, new OnResponseListener<Boolean>() {
            @Override
            public void onComplete(boolean isOK, Boolean result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(ValueConstants.MSG_DELETE_ADDRESS_OK);
                    return;
                }
                sendMessage(ValueConstants.MSG_DELETE_ADDRESS_KO, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_DELETE_ADDRESS_KO, errorCode, 0, errorMessage);
            }
        });
    }

}
