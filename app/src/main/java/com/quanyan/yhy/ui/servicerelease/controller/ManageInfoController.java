package com.quanyan.yhy.ui.servicerelease.controller;

import android.content.Context;
import android.os.Handler;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.quanyan.base.BaseController;
import com.quanyan.yhy.net.NetManager;
import com.quanyan.yhy.net.lsn.OnResponseListener;
import com.smart.sdk.api.resp.Api_TRADEMANAGER_ProcessQueryParam;
import com.yhy.common.beans.net.model.paycore.EleAccountInfo;
import com.yhy.common.beans.net.model.tm.CancelProcessResult;
import com.yhy.common.beans.net.model.tm.ConsultCategoryInfoList;
import com.yhy.common.beans.net.model.tm.ItemApiResult;
import com.yhy.common.beans.net.model.tm.ItemQueryParam;
import com.yhy.common.beans.net.model.tm.ProcessOrder;
import com.yhy.common.beans.net.model.tm.ProcessOrderList;
import com.yhy.common.beans.net.model.tm.PublishServiceDO;
import com.yhy.common.beans.net.model.user.UserStatusInfoList;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.SPUtils;
import com.yhy.router.YhyRouter;
import com.yhy.service.IUserService;


/**
 * Created with Android Studio.
 * Title:***Acitivity
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:王东旭
 * Date:2016/6/1
 * Time:16:46
 * Version 2.0
 */
public class ManageInfoController extends BaseController {
    NetManager mNetManager;
    Context context;

    @Autowired
    IUserService userService;

    public ManageInfoController(Context context, Handler handler) {
        super(context, handler);
        this.context = context;
        mNetManager = NetManager.getInstance(context);
        YhyRouter.getInstance().inject(this);
    }

    public void doSingleInfoSelect() {
        mUiHandler.sendEmptyMessage(1);
    }

    public void doInfoSelect() {
        mUiHandler.sendEmptyMessage(1);
    }

    /**
     * 获取达人出售的订单列表
     */
    public void doGetExpertOrderListSell(Api_TRADEMANAGER_ProcessQueryParam params) {
        mNetManager.doGetProcessOrderList(params, new OnResponseListener<ProcessOrderList>() {


            @Override
            public void onComplete(boolean isOK, ProcessOrderList result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(ValueConstants.GET_ORDER_LIST_SUCCESS, 0, errorCode, result);
                    return;
                }
                sendMessage(ValueConstants.GET_ORDER_LIST_FAIL, 0, errorCode, errorMsg);

            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.GET_ORDER_LIST_FAIL, 0, errorCode, errorMessage);
            }

        });
    }

    public void doGetExpertOrderDetail(long processOrderId) {
        mNetManager.doGetProcessOrderDetail(processOrderId, new OnResponseListener<ProcessOrder>() {


            @Override
            public void onComplete(boolean isOK, ProcessOrder result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(ValueConstants.GET_ORDER_DETAILS_SUCCESS, 0, errorCode, result);
                    return;
                }
                sendMessage(ValueConstants.GET_ORDER_DETAILS_FAIL, 0, errorCode, errorMsg);

            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.GET_ORDER_DETAILS_FAIL, 0, errorCode, errorMessage);

            }
        });
    }

    /**
     * 发布服务
     * @param context
     * @param param
     */
    public void doPublishService(final Context context, PublishServiceDO param) {
        NetManager.getInstance(context).doPublishService(param, new OnResponseListener<Boolean>() {
            @Override
            public void onComplete(boolean isOK, Boolean result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(ValueConstants.MSG_RELEASE_SERVICE_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_RELEASE_SERVICE_KO, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_RELEASE_SERVICE_KO, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 获取咨询商品属性
     * @param context
     */
    public void doGetConsultItemProperties(final Context context) {
        NetManager.getInstance(context).doGetConsultItemProperties(new OnResponseListener<ConsultCategoryInfoList>() {
            @Override
            public void onComplete(boolean isOK, ConsultCategoryInfoList result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(ValueConstants.MSG_GETCONSULT_ITEMPROPERTIES_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_GETCONSULT_ITEMPROPERTIES_KO, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_GETCONSULT_ITEMPROPERTIES_KO, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 服务管理列表接口
     *
     * @param
     * @param itemQueryParam
     */
    public void doServiceManageListRequest(ItemQueryParam itemQueryParam) {
        NetManager.getInstance(context).doGetGoodsManagementInfo(itemQueryParam, new OnResponseListener<ItemApiResult>() {
            @Override
            public void onComplete(boolean isOK, ItemApiResult result, int errorCode, String errorMsg) {
                if (isOK) {

                    sendMessage(ValueConstants.MSG_SERVICE_MANAGE_SUCCESS, errorCode, 0, result);
                    return;

                }
                sendMessage(ValueConstants.MSG_SERVICE_MANAGE_FAIL, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_SERVICE_MANAGE_FAIL, errorCode, 0, errorMessage);
            }
        });
    }

    public void doSendONorOFFLineRequest(ItemQueryParam params) {
        NetManager.getInstance(context).doGetUpdateState(params, new OnResponseListener<Boolean>() {
            @Override
            public void onComplete(boolean isOK, Boolean result, int errorCode, String errorMsg) {


                if (isOK) {
                    sendMessage(ValueConstants.MSG_SERVICE_UPDATE_SUCCESS, errorCode, 0, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_SERVICE_UPDATE_FAIL, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_SERVICE_UPDATE_FAIL, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 服务白名单
     */
    public void doCheckWhiteList() {
        NetManager.getInstance(context).doCheckWhiteList(new OnResponseListener<Boolean>() {
            @Override
            public void onComplete(boolean isOK, Boolean result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(ValueConstants.MSG_RELEASE_SERVICE_WHITE_LIST_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_RELEASE_SERVICE_WHITE_LIST_KO, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_RELEASE_SERVICE_WHITE_LIST_KO, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 更新状态
     *
     * @param context
     * @param status
     */
    public void doEditUserStatus(Context context, int status) {
        NetManager.getInstance(context).doEditUserStatus(userService.getLoginUserId(), status, new OnResponseListener<Boolean>() {
            @Override
            public void onComplete(boolean isOK, Boolean result, int errorCode, String errorMsg) {
                if (isOK) {
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

    /**
     * 获取用户状态
     *
     * @param context
     */
    public void doGetUserStatus(Context context) {
        NetManager.getInstance(context).doGetBatchUserStatus(new long[]{userService.getLoginUserId()}, new OnResponseListener<UserStatusInfoList>() {
            @Override
            public void onComplete(boolean isOK, UserStatusInfoList result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(ValueConstants.MSG_BATCH_GET_USER_STATUS_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_BATCH_GET_USER_STATUS_KO, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_BATCH_GET_USER_STATUS_KO, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 查询发布的服务
     *
     * @param params
     */
    public void doGetPublishItemInfo(ItemQueryParam params) {
        NetManager.getInstance(context).doGetPublishItemInfo(params, new OnResponseListener<PublishServiceDO>() {
            @Override
            public void onComplete(boolean isOK, PublishServiceDO result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(ValueConstants.MSG_SELECT_SERVICE_DETAIL_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_SELECT_SERVICE_DETAIL_KO, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_SELECT_SERVICE_DETAIL_KO, errorCode, 0, errorMessage);
            }
        });
    }

    /*
    * 服务管理详情接口
    * */
    public void doManageDetail(ItemQueryParam params, long id) {
        NetManager.getInstance(context).doGetGoodsDetailInfo(params, id, new OnResponseListener<ItemApiResult>() {

            @Override
            public void onComplete(boolean isOK, ItemApiResult result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(ValueConstants.GET_MANAGE_DETAIL_SUCCESS, result);
                    return;
                }
                sendMessage(ValueConstants.GET_MANAGE_DETAIL_FAIL, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.GET_MANAGE_DETAIL_FAIL, errorCode, 0, errorMessage);
            }

        });
    }

    public void cancelOrderNewInfo(long processId) {
        NetManager.getInstance(context).doCancelProcessOrder(processId, new OnResponseListener<CancelProcessResult>() {
            @Override
            public void onComplete(boolean isOK, CancelProcessResult result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(ValueConstants.CANCLE_ORDER_SUCCESS, result);
                    return;
                }
                sendMessage(ValueConstants.CANCLE_ORDER_FAIL, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.CANCLE_ORDER_FAIL, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 得到用户电子钱包的信息
     *
     * @param context
     */
    public void doGetEleAccountInfo(final Context context) {
        NetManager.getInstance(context).doGetEleAccountInfo(new OnResponseListener<EleAccountInfo>() {
            @Override
            public void onComplete(boolean isOK, EleAccountInfo result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(ValueConstants.PAY_GETELEACCOUNTINFO_SUCCESS, result);
                    return;
                }
                sendMessage(ValueConstants.PAY_GETELEACCOUNTINFO_ERROR, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.PAY_GETELEACCOUNTINFO_ERROR, errorCode, 0, errorMessage);
            }
        });
    }
}
