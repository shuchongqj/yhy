package com.quanyan.yhy.ui.spcart.controller;

import android.content.Context;
import android.os.Handler;

import com.quanyan.base.BaseController;
import com.quanyan.yhy.net.NetManager;
import com.quanyan.yhy.net.lsn.OnResponseListener;
import com.yhy.common.beans.net.model.paycore.AliBatchPayParam;
import com.yhy.common.beans.net.model.paycore.AliPayInfo;
import com.yhy.common.beans.net.model.paycore.CebCloudBatchPayParam;
import com.yhy.common.beans.net.model.paycore.CebCloudPayInfo;
import com.yhy.common.beans.net.model.paycore.ElePurseBatchPayParam;
import com.yhy.common.beans.net.model.paycore.PayCoreBaseResult;
import com.yhy.common.beans.net.model.tm.CartAmountResult;
import com.yhy.common.beans.net.model.tm.CartInfoListResult;
import com.yhy.common.beans.net.model.tm.CreateBatchOrderParam;
import com.yhy.common.beans.net.model.tm.CreateCartInfo;
import com.yhy.common.beans.net.model.tm.CreateOrderContextForPointMall;
import com.yhy.common.beans.net.model.tm.CreateOrderContextParamForPointMall;
import com.yhy.common.beans.net.model.tm.CreateOrderResultTOList;
import com.yhy.common.beans.net.model.tm.DeleteCartInfo;
import com.yhy.common.beans.net.model.tm.SelectCartInfo;
import com.yhy.common.beans.net.model.tm.UpdateAmountCartInfo;
import com.yhy.common.constants.ValueConstants;

/**
 * Created with Android Studio.
 * Title:SPCartController
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-10-27
 * Time:10:53
 * Version 1.0
 * Description:
 */
public class SPCartController extends BaseController {

    public SPCartController(Context context, Handler handler) {
        super(context, handler);
    }


    /**
     * 加入购物车
     *
     * @param createCartInfo
     * @param context
     */
    public void doSaveToSpcart(final CreateCartInfo createCartInfo, final Context context) {
        NetManager.getInstance(context).doSaveToCart(createCartInfo, new OnResponseListener<Boolean>() {
            @Override
            public void onComplete(boolean isOK, Boolean result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(ValueConstants.SPCART_SaveToCart_OK, 0, 0, result);
                    return;
                }
                sendMessage(ValueConstants.SPCART_SaveToCart_ERROR, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.SPCART_SaveToCart_ERROR, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 购物车数量
     *
     * @param context
     */
    public void doSelectCartAmount(final Context context) {
        NetManager.getInstance(context).doSelectCartAmount(new OnResponseListener<CartAmountResult>() {
            @Override
            public void onComplete(boolean isOK, CartAmountResult result, int errorCode, String errorMsg) {
                if (isOK) {
                    if (result == null) {
                        result = new CartAmountResult();
                    }
                    sendMessage(ValueConstants.SPCART_SelectCartAmount_OK, 0, 0, result);
                    return;
                }
                sendMessage(ValueConstants.SPCART_SelectCartAmount_ERROR, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.SPCART_SelectCartAmount_ERROR, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 批量删除购物车
     *
     * @param context
     * @param info
     */
    public void doDeleteCart(final Context context, final DeleteCartInfo info) {
        NetManager.getInstance(context).doDeleteCart(info, new OnResponseListener<Boolean>() {
            @Override
            public void onComplete(boolean isOK, Boolean result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(ValueConstants.SPCART_DeleteCart_OK, 0, 0, result);
                    return;
                }
                sendMessage(ValueConstants.SPCART_DeleteCart_ERROR, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.SPCART_DeleteCart_ERROR, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 获取购物车列表
     *
     * @param context
     */
    public void doGetCartInfoList(final Context context) {
        NetManager.getInstance(context).doGetCartInfoList(new OnResponseListener<CartInfoListResult>() {
            @Override
            public void onComplete(boolean isOK, CartInfoListResult result, int errorCode, String errorMsg) {
                if (isOK) {
                    if (result == null) {
                        result = new CartInfoListResult();
                    }
                    sendMessage(ValueConstants.SPCART_GetCartInfoList_OK, 0, 0, result);
                    return;
                }
                sendMessage(ValueConstants.SPCART_GetCartInfoList_ERROR, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.SPCART_GetCartInfoList_ERROR, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 勾选购物车
     *
     * @param context
     * @param info
     */
    public void doSelectCart(final Context context, final SelectCartInfo info) {
        NetManager.getInstance(context).doSelectCart(info, new OnResponseListener<Boolean>() {
            @Override
            public void onComplete(boolean isOK, Boolean result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(ValueConstants.SPCART_SelectCart_OK, 0, 0, result);
                    return;
                }
                sendMessage(ValueConstants.SPCART_SelectCart_ERROR, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.SPCART_SelectCart_ERROR, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 修改商品数量
     *
     * @param context
     * @param info
     */
    public void doUpdateCartAmount(final Context context, final UpdateAmountCartInfo info) {
        NetManager.getInstance(context).doUpdateCartAmount(info, new OnResponseListener<Boolean>() {
            @Override
            public void onComplete(boolean isOK, Boolean result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(ValueConstants.SPCART_UpdateCartAmount_OK, 0, 0, result);
                    return;
                }
                sendMessage(ValueConstants.SPCART_UpdateCartAmount_ERROR, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.SPCART_UpdateCartAmount_ERROR, errorCode, 0, errorMessage);
            }
        });
    }


    /**
     * 获取购物车下单界面上下文
     *
     * @param context
     */
    public void doGetCreateOrderContextForPointMall(final Context context, final CreateOrderContextParamForPointMall paramForPointMall) {
        NetManager.getInstance(context).doGetCreateOrderContextForPointMall(paramForPointMall, new OnResponseListener<CreateOrderContextForPointMall>() {
            @Override
            public void onComplete(boolean isOK, CreateOrderContextForPointMall result, int errorCode, String errorMsg) {
                if (isOK) {
                    if (result == null) {
                        result = new CreateOrderContextForPointMall();
                    }
                    sendMessage(ValueConstants.SPCART_CreateOrderContextForPointMall_OK, 0, 0, result);
                    return;
                }
                sendMessage(ValueConstants.SPCART_CreateOrderContextForPointMall_ERROR, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.SPCART_CreateOrderContextForPointMall_ERROR, errorCode, 0, errorMessage);
            }
        });
    }


    /**
     * 购物车下单
     *
     * @param context
     */
    public void doCreateBatchOrder(final Context context, final CreateBatchOrderParam param) {
        NetManager.getInstance(context).doCreateBatchOrder(param, new OnResponseListener<CreateOrderResultTOList>() {
            @Override
            public void onComplete(boolean isOK, CreateOrderResultTOList result, int errorCode, String errorMsg) {
                if (isOK) {
                    if (result == null) {
                        result = new CreateOrderResultTOList();
                    }
                    sendMessage(ValueConstants.SPCART_CreateBatchOrder_OK, 0, 0, result);
                    return;
                }
                sendMessage(ValueConstants.SPCART_CreateBatchOrder_ERROR, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.SPCART_CreateBatchOrder_ERROR, errorCode, 0, errorMessage);
            }
        });


//        NetManager.getInstance(context).docre
    }


    /**
     * 支付宝批量支付
     *
     * @param context
     * @param aliBatchPayParam
     */
    public void doGetAliBatchPayInfo(final Context context, final AliBatchPayParam aliBatchPayParam) {
        NetManager.getInstance(context).doGetAliBatchPayInfo(aliBatchPayParam, new OnResponseListener<AliPayInfo>() {
            @Override
            public void onComplete(boolean isOK, AliPayInfo result, int errorCode, String errorMsg) {
                if (isOK) {
                    if (result == null) {
                        result = new AliPayInfo();
                    }
                    sendMessage(ValueConstants.SPCART_GetAliBatchPayInfo_OK, 0, 0, result);
                    return;
                }
                sendMessage(ValueConstants.SPCART_GetAliBatchPayInfo_ERROR, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.SPCART_GetAliBatchPayInfo_ERROR, errorCode, 0, errorMessage);
            }
        });
    }


    /**
     * 钱包批量支付
     *
     * @param context
     * @param elePurseBatchPayParam
     */
    public void doElePurseBatchPay(final Context context, final ElePurseBatchPayParam elePurseBatchPayParam) {
        NetManager.getInstance(context).doElePurseBatchPay(elePurseBatchPayParam, new OnResponseListener<PayCoreBaseResult>() {
            @Override
            public void onComplete(boolean isOK, PayCoreBaseResult result, int errorCode, String errorMsg) {
                if (isOK) {
                    if (result == null) {
                        result = new PayCoreBaseResult();
                    }
                    sendMessage(ValueConstants.SPCART_ElePurseBatchPay_OK, 0, 0, result);
                    return;
                }
                sendMessage(ValueConstants.SPCART_ElePurseBatchPay_ERROR, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.SPCART_ElePurseBatchPay_ERROR, errorCode, 0, errorMessage);
            }
        });
    }


    /**
     * 光大批量支付
     *
     * @param context
     * @param cebCloudBatchPayParam
     */
    public void doGetCebCloudBatchPayInfo(final Context context, final CebCloudBatchPayParam cebCloudBatchPayParam) {
        NetManager.getInstance(context).doGetCebCloudBatchPayInfo(cebCloudBatchPayParam, new OnResponseListener<CebCloudPayInfo>() {
            @Override
            public void onComplete(boolean isOK, CebCloudPayInfo result, int errorCode, String errorMsg) {
                if (isOK) {
                    if (result == null) {
                        result = new CebCloudPayInfo();
                    }
                    sendMessage(ValueConstants.SPCART_GetCebCloudBatchPayInfo_OK, 0, 0, result);
                    return;
                }
                sendMessage(ValueConstants.SPCART_GetCebCloudBatchPayInfo_ERROR, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.SPCART_GetCebCloudBatchPayInfo_ERROR, errorCode, 0, errorMessage);
            }
        });
    }
}
