package com.quanyan.yhy.net;

import android.content.Context;
import android.os.Handler;

import com.google.gson.Gson;
import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.common.ErrorCode;
import com.quanyan.yhy.net.lsn.OnResponseListener;
import com.smart.sdk.api.request.ApiCode;
import com.smart.sdk.api.request.Cart_DeleteCart;
import com.smart.sdk.api.request.Cart_GetCartInfoList;
import com.smart.sdk.api.request.Cart_SaveToCart;
import com.smart.sdk.api.request.Cart_SelectCart;
import com.smart.sdk.api.request.Cart_SelectCartAmount;
import com.smart.sdk.api.request.Cart_UpdateCartAmount;
import com.smart.sdk.api.request.Items_GetConsultItemList;
import com.smart.sdk.api.request.Items_GetFastConsultItem;
import com.smart.sdk.api.request.Items_GetItem;
import com.smart.sdk.api.request.Items_GetItemAndSellerInfo;
import com.smart.sdk.api.request.Items_GetItemListByItemIds;
import com.smart.sdk.api.request.Points_PointDetailQuery;
import com.smart.sdk.api.request.Points_TotalPointQuery;
import com.smart.sdk.api.request.SellerAdmin_GetConsultItemProperties;
import com.smart.sdk.api.request.SellerAdmin_GetItemList;
import com.smart.sdk.api.request.SellerAdmin_GetPublishItemInfo;
import com.smart.sdk.api.request.SellerAdmin_PublishService;
import com.smart.sdk.api.request.SellerAdmin_UpdateState;
import com.smart.sdk.api.request.Task_DailyTaskQuery;
import com.smart.sdk.api.request.Task_IsSignIn;
import com.smart.sdk.api.request.Task_ShareDailySteps;
import com.smart.sdk.api.request.Trademanager_AcceptProcessOrder;
import com.smart.sdk.api.request.Trademanager_BuyerCloseOrder;
import com.smart.sdk.api.request.Trademanager_BuyerCloseOrderWithReason;
import com.smart.sdk.api.request.Trademanager_BuyerConfirmGoodsDeliveried;
import com.smart.sdk.api.request.Trademanager_CancelProcessOrder;
import com.smart.sdk.api.request.Trademanager_CreateBatchOrder;
import com.smart.sdk.api.request.Trademanager_CreateOrder;
import com.smart.sdk.api.request.Trademanager_CreateProcessOrder;
import com.smart.sdk.api.request.Trademanager_FinishConsult;
import com.smart.sdk.api.request.Trademanager_GenerateVoucher;
import com.smart.sdk.api.request.Trademanager_GetCloseOrderReasonList;
import com.smart.sdk.api.request.Trademanager_GetConsultInfoForSeller;
import com.smart.sdk.api.request.Trademanager_GetCreateOrderContext;
import com.smart.sdk.api.request.Trademanager_GetCreateOrderContextForPointMall;
import com.smart.sdk.api.request.Trademanager_GetLoginPayInfo;
import com.smart.sdk.api.request.Trademanager_GetOrderPrice;
import com.smart.sdk.api.request.Trademanager_GetPayInfo;
import com.smart.sdk.api.request.Trademanager_GetPayStatusInfo;
import com.smart.sdk.api.request.Trademanager_GetProcessOrderDetail;
import com.smart.sdk.api.request.Trademanager_GetProcessOrderList;
import com.smart.sdk.api.request.Trademanager_GetProcessState;
import com.smart.sdk.api.request.Trademanager_GetUserAssets;
import com.smart.sdk.api.request.Trademanager_GetUserRoute;
import com.smart.sdk.api.request.Trademanager_GetWxPayInfo;
import com.smart.sdk.api.request.Trademanager_PageQueryBizOrderForBuyer;
import com.smart.sdk.api.request.Trademanager_PageQueryUserRoute;
import com.smart.sdk.api.request.Trademanager_PostRate;
import com.smart.sdk.api.request.Trademanager_QueryBizOrderForBuyer;
import com.smart.sdk.api.request.Trademanager_QueryBizOrderInfoForBuyer;
import com.smart.sdk.api.request.Trademanager_QueryItemVoucherList;
import com.smart.sdk.api.request.Trademanager_QueryMyVoucherList;
import com.smart.sdk.api.request.Trademanager_QueryOrderVoucherList;
import com.smart.sdk.api.request.Trademanager_QueryPackageBuyOrder;
import com.smart.sdk.api.request.Trademanager_QueryRateBuyOrder;
import com.smart.sdk.api.request.Trademanager_QuerySellerAndConsultState;
import com.smart.sdk.api.request.Trademanager_QuerySellerVoucherList;
import com.smart.sdk.api.request.Trademanager_QueryUserRecentSportsOrder;
import com.smart.sdk.api.resp.Api_CART_CreateCartInfo;
import com.smart.sdk.api.resp.Api_CART_DeleteCartInfo;
import com.smart.sdk.api.resp.Api_CART_SelectCartInfo;
import com.smart.sdk.api.resp.Api_CART_UpdateAmountCartInfo;
import com.smart.sdk.api.resp.Api_ITEMS_ConsultContent;
import com.smart.sdk.api.resp.Api_ITEMS_QueryTermsDTO;
import com.smart.sdk.api.resp.Api_SELLERADMIN_ItemQueryParam;
import com.smart.sdk.api.resp.Api_SELLERADMIN_PublishServiceDO;
import com.smart.sdk.api.resp.Api_TRADEMANAGER_AcceptProcessOrderParam;
import com.smart.sdk.api.resp.Api_TRADEMANAGER_CancelProcessParam;
import com.smart.sdk.api.resp.Api_TRADEMANAGER_CloseOrderParam;
import com.smart.sdk.api.resp.Api_TRADEMANAGER_CreateBatchOrderParam;
import com.smart.sdk.api.resp.Api_TRADEMANAGER_CreateOrderContextParamForPointMall;
import com.smart.sdk.api.resp.Api_TRADEMANAGER_CreateOrderParam;
import com.smart.sdk.api.resp.Api_TRADEMANAGER_CreateProcessOrderParam;
import com.smart.sdk.api.resp.Api_TRADEMANAGER_DetailOrder;
import com.smart.sdk.api.resp.Api_TRADEMANAGER_FinishProcessParam;
import com.smart.sdk.api.resp.Api_TRADEMANAGER_OrderPriceQueryDTO;
import com.smart.sdk.api.resp.Api_TRADEMANAGER_PostRateParam;
import com.smart.sdk.api.resp.Api_TRADEMANAGER_ProcessQueryParam;
import com.smart.sdk.api.resp.Api_TRADEMANAGER_ProcessStateQuery;
import com.smart.sdk.api.resp.Api_TRADEMANAGER_QueryConsultStateParam;
import com.smart.sdk.api.resp.Api_TRADEMANAGER_QueryItemVoucherDTO;
import com.smart.sdk.api.resp.Api_TRADEMANAGER_QueryMyVoucherDTO;
import com.smart.sdk.api.resp.Api_TRADEMANAGER_QueryOrderVoucherDTO;
import com.smart.sdk.api.resp.Api_TRADEMANAGER_QuerySellerAndConsultStateParam;
import com.smart.sdk.api.resp.Api_TRADEMANAGER_QuerySellerVoucherDTO;
import com.smart.sdk.api.resp.Api_TRADEMANAGER_UserBindVoucherDTO;
import com.smart.sdk.client.ApiContext;
import com.smart.sdk.client.BaseRequest;
import com.yhy.common.beans.net.model.master.QueryTermsDTO;
import com.yhy.common.beans.net.model.point.PointDetailQueryResult;
import com.yhy.common.beans.net.model.shop.MerchantItem;
import com.yhy.common.beans.net.model.tm.AcceptProcessOrderResult;
import com.yhy.common.beans.net.model.tm.CancelProcessResult;
import com.yhy.common.beans.net.model.tm.CartAmountResult;
import com.yhy.common.beans.net.model.tm.CartInfoListResult;
import com.yhy.common.beans.net.model.tm.ConsultCategoryInfoList;
import com.yhy.common.beans.net.model.tm.ConsultContent;
import com.yhy.common.beans.net.model.tm.ConsultState;
import com.yhy.common.beans.net.model.tm.CreateBatchOrderParam;
import com.yhy.common.beans.net.model.tm.CreateCartInfo;
import com.yhy.common.beans.net.model.tm.CreateOrderContextForPointMall;
import com.yhy.common.beans.net.model.tm.CreateOrderContextParamForPointMall;
import com.yhy.common.beans.net.model.tm.CreateOrderResultTOList;
import com.yhy.common.beans.net.model.tm.CreateProcessOrderResultTO;
import com.yhy.common.beans.net.model.tm.DailyTaskQueryResult;
import com.yhy.common.beans.net.model.tm.DeleteCartInfo;
import com.yhy.common.beans.net.model.tm.FinishProcessResult;
import com.yhy.common.beans.net.model.tm.ItemApiResult;
import com.yhy.common.beans.net.model.tm.ItemQueryParam;
import com.yhy.common.beans.net.model.tm.LgExpressInfo;
import com.yhy.common.beans.net.model.tm.OrderDetailResult;
import com.yhy.common.beans.net.model.tm.OrderPriceQueryDTO;
import com.yhy.common.beans.net.model.tm.OrderVoucherResult;
import com.yhy.common.beans.net.model.tm.PackageDetailResult;
import com.yhy.common.beans.net.model.tm.PostRateParam;
import com.yhy.common.beans.net.model.tm.ProcessOrder;
import com.yhy.common.beans.net.model.tm.ProcessOrderList;
import com.yhy.common.beans.net.model.tm.ProcessState;
import com.yhy.common.beans.net.model.tm.PublishServiceDO;
import com.yhy.common.beans.net.model.tm.SelectCartInfo;
import com.yhy.common.beans.net.model.tm.SellerAndConsultStateResult;
import com.yhy.common.beans.net.model.tm.TmCloseOrderReasonItemList;
import com.yhy.common.beans.net.model.tm.TmConsultInfo;
import com.yhy.common.beans.net.model.tm.TmCreateOrderContext;
import com.yhy.common.beans.net.model.tm.TmCreateOrderParam;
import com.yhy.common.beans.net.model.tm.TmCreateOrderResultTO;
import com.yhy.common.beans.net.model.tm.TmOrderDetail;
import com.yhy.common.beans.net.model.tm.TmOrderList;
import com.yhy.common.beans.net.model.tm.TmPayInfo;
import com.yhy.common.beans.net.model.tm.TmPayStatusInfo;
import com.yhy.common.beans.net.model.tm.TmUserRoute;
import com.yhy.common.beans.net.model.tm.TmUserRouteList;
import com.yhy.common.beans.net.model.tm.TmWxPayInfo;
import com.yhy.common.beans.net.model.tm.UpdateAmountCartInfo;
import com.yhy.common.beans.net.model.tm.VoucherResultList;
import com.yhy.common.beans.net.model.tm.VoucherTemplateResultList;
import com.yhy.common.beans.net.model.trip.ShortItemsResult;
import com.yhy.common.beans.net.model.user.UserAssets;
import com.yhy.common.utils.SPUtils;

import org.json.JSONException;

import java.util.List;

/**
 * 订单相关
 */
public class TradeManagerNetManager extends BaseNetManager {
    private static TradeManagerNetManager mInstance;

    public TradeManagerNetManager(Context context, ApiContext apiContext, Handler handler) {
        mContext = context;
        mApiContext = apiContext;
        mHandler = handler;
    }


    public synchronized static TradeManagerNetManager getInstance(Context context, ApiContext apiContext, Handler handler) {
        if (mInstance == null) {
            mInstance = new TradeManagerNetManager(context, apiContext, handler);
        }

        return mInstance;
    }

    /**
     * 释放单例
     */
    public synchronized void release() {
        if (mInstance != null) {
            mInstance = null;
        }
    }

    /**
     * 获取最接近开始的活动或运动子订单
     *
     */
    public void doGetUserRecentSportsOrder(final OnResponseListener<Api_TRADEMANAGER_DetailOrder> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        final Trademanager_QueryUserRecentSportsOrder req = new Trademanager_QueryUserRecentSportsOrder();
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                if (lsn != null) {
                    Api_TRADEMANAGER_DetailOrder order = null;
                    try {
                        Gson g = new Gson();
                        order = g.fromJson(req.getResponse().serialize().toString(),Api_TRADEMANAGER_DetailOrder.class);

                    } catch (Exception e) {
                        if (lsn != null){
                            lsn.onComplete(false,null,ErrorCode.DATA_FORMAT_ERROR,e.getMessage());
                        }
                        e.printStackTrace();
                    }
                    lsn.onComplete(true,order, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);

            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }

    /**
     * 关闭订单
     *
     * @param itemId
     * @param lsn
     */
    public void doBuyerCloseOrder(long itemId, String reason, final OnResponseListener<Boolean> lsn) {
        if (!checkSubmitStatus(lsn) || reason == null || itemId <= 0) {
            return;
        }
        final Trademanager_BuyerCloseOrder req = new Trademanager_BuyerCloseOrder(itemId, reason);
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                if (lsn != null) {
                    lsn.onComplete(true, req.getResponse().value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);

            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }

    /**
     * 创建订单
     *
     * @param param
     * @param lsn
     * @throws JSONException
     */
    public void doCreateOrder(TmCreateOrderParam param, final OnResponseListener<TmCreateOrderResultTO> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn) || param == null) {
            return;
        }
        Api_TRADEMANAGER_CreateOrderParam createOrderParam = Api_TRADEMANAGER_CreateOrderParam.deserialize(param.serialize());
        final Trademanager_CreateOrder req = new Trademanager_CreateOrder(createOrderParam);
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                TmCreateOrderResultTO value = null;
                if (req.getResponse() != null) {
                    try {
                        value = TmCreateOrderResultTO.deserialize(req.getResponse().serialize());
                    } catch (JSONException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    }
                }
                if (lsn != null) {
                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);

            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }


    /**
     * 批量创建订单
     *
     * @param param
     * @param lsn
     * @throws JSONException
     */
    public void doCreateBatchOrder(CreateBatchOrderParam param, final OnResponseListener<CreateOrderResultTOList> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn) || param == null) {
            return;
        }
        Api_TRADEMANAGER_CreateBatchOrderParam createBatchOrderParam = Api_TRADEMANAGER_CreateBatchOrderParam.deserialize(param.serialize());
        final Trademanager_CreateBatchOrder req = new Trademanager_CreateBatchOrder(createBatchOrderParam);
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                CreateOrderResultTOList value = null;
                if (req.getResponse() != null) {
                    try {
                        value = CreateOrderResultTOList.deserialize(req.getResponse().serialize());
                    } catch (JSONException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    }
                }
                if (lsn != null) {
                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);

            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }

    /**
     * 获取下单页所需上下文
     *
     * @param id
     * @param lsn
     * @throws JSONException
     */
    public void doGetCreateOrderContext(long id, final OnResponseListener<TmCreateOrderContext> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        final Trademanager_GetCreateOrderContext req = new Trademanager_GetCreateOrderContext(id);
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                TmCreateOrderContext value = null;
                if (req.getResponse() != null) {
                    try {
                        value = TmCreateOrderContext.deserialize(req.getResponse().serialize());
                    } catch (JSONException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    }
                }
                if (lsn != null) {
                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);

            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }

    /**
     * 积分商城获取下单页所需上下文
     *
     * @param paramForPointMall
     * @param lsn
     * @throws JSONException
     */
    public void doGetCreateOrderContextForPointMall(CreateOrderContextParamForPointMall paramForPointMall, final OnResponseListener<CreateOrderContextForPointMall> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        final Trademanager_GetCreateOrderContextForPointMall req = new Trademanager_GetCreateOrderContextForPointMall(Api_TRADEMANAGER_CreateOrderContextParamForPointMall.deserialize(paramForPointMall.serialize()));
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                CreateOrderContextForPointMall value = null;
                if (req.getResponse() != null) {
                    try {
                        value = CreateOrderContextForPointMall.deserialize(req.getResponse().serialize());
                    } catch (JSONException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    }
                }
                if (lsn != null) {
                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);

            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }

    /**
     * 通过订单id查询包裹信息
     *
     * @param id
     * @param lsn
     */
    public void doQueryPackageBuyOrder(long id, final OnResponseListener<PackageDetailResult> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        final Trademanager_QueryPackageBuyOrder req = new Trademanager_QueryPackageBuyOrder(id);
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                PackageDetailResult value = null;
                if (req.getResponse() != null) {
                    try {
                        value = PackageDetailResult.deserialize(req.getResponse().serialize());
                    } catch (JSONException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    }
                }
                if (lsn != null) {
                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);

            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }

    /**
     * 通过订单id查询评价列表
     *
     * @param id
     * @param lsn
     */
    public void doQueryRateBuyOrder(long id, final OnResponseListener<OrderDetailResult> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        final Trademanager_QueryRateBuyOrder req = new Trademanager_QueryRateBuyOrder(id);
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                OrderDetailResult value = null;
                if (req.getResponse() != null) {
                    try {
                        value = OrderDetailResult.deserialize(req.getResponse().serialize());
                    } catch (JSONException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    }
                }
                if (lsn != null) {
                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);

            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }


    /**
     * 获取用户行程详情
     *
     * @param lineId
     * @param lsn
     */
    public void doGetUserRouteDetail(final long lineId, final OnResponseListener<TmUserRoute> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        final Trademanager_GetUserRoute req = new Trademanager_GetUserRoute(lineId);
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                TmUserRoute value = null;
                if (req.getResponse() != null) {
                    try {
                        value = TmUserRoute.deserialize(req.getResponse().serialize());
                    } catch (JSONException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    }
                }
                if (lsn != null) {
                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }

            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);

            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }

    /**
     * 分页查询交易订单，买家维度
     *
     * @param typeCode
     * @param statusCode 状态code 全部：ALL，代付款：WAITING_PAY，已完成：FINISH，已关闭：CLOSED，处理中：PENDING，待发货：WAITING_DELIVERY，待出行：WAITING_DEPART，待收货：SHIPPING，已取消：CANCEL，已退款：REFUNDED
     * @param pageIndex
     * @param pageSize
     * @param lsn
     * @throws JSONException
     */
    public void doGetOrderList(String typeCode,
                               String statusCode,
                               int pageIndex,
                               int pageSize,
                               final OnResponseListener<TmOrderList> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        final Trademanager_PageQueryBizOrderForBuyer req = new Trademanager_PageQueryBizOrderForBuyer();
//        if (!StringUtil.isEmpty(typeCode)) {
        req.setTypeCode(typeCode);
//        }

        if (!StringUtil.isEmpty(statusCode)) {
            req.setStatusCode(statusCode);
        }

        if (pageIndex > 0) {
            req.setPageNo(pageIndex);
        }

        if (pageSize > 0) {
            req.setPageSize(pageSize);
        }
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                TmOrderList value = null;
                if (req.getResponse() != null) {
                    try {
                        value = TmOrderList.deserialize(req.getResponse().serialize());
                    } catch (JSONException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    }
                }
                if (lsn != null) {
                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);

            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }

    /**
     * 获取我的行程列表
     *
     * @param pageIndex
     * @param pageSize
     * @param lsn
     * @throws JSONException
     */
    public void doGetUserRouteList(int pageIndex,
                                   int pageSize,
                                   final OnResponseListener<TmUserRouteList> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        final Trademanager_PageQueryUserRoute req = new Trademanager_PageQueryUserRoute();

        if (pageIndex > 0) {
            req.setPageNo(pageIndex);
        }

        if (pageSize > 0) {
            req.setPageSize(pageSize);
        }
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                TmUserRouteList value = null;
                if (req.getResponse() != null) {
                    try {
                        value = TmUserRouteList.deserialize(req.getResponse().serialize());
                    } catch (JSONException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    }
                }
                if (lsn != null) {
                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);

            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }

    /**
     * 订单详情
     *
     * @param id
     * @param lsn
     * @throws JSONException
     */
    public void doGetOrderDetail(long id, final OnResponseListener<TmOrderDetail> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        final Trademanager_QueryBizOrderForBuyer req = new Trademanager_QueryBizOrderForBuyer(id);
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                TmOrderDetail value = null;
                if (req.getResponse() != null) {
                    try {
                        value = TmOrderDetail.deserialize(req.getResponse().serialize());
                    } catch (JSONException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    }
                }
                if (lsn != null) {
                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);

            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }

    /**
     * 同时获取订单详情和ItemType
     *
     * @param id
     * @param lsn
     * @throws JSONException
     */
    public void doGetOrderDetailAndItemType(final long id, final OnResponseListener<TmOrderDetail> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        Runnable requestThread = new Runnable() {
            @Override
            public void run() {
                Trademanager_QueryBizOrderForBuyer req = new Trademanager_QueryBizOrderForBuyer(id);

                try {
                    sendRequest(mContext, mApiContext, new BaseRequest[]{req});
                    TmOrderDetail value = null;
                    if (req.getReturnCode() == ApiCode.SUCCESS && req.getResponse() != null) {
                        value = TmOrderDetail.deserialize(req.getResponse().serialize());
                        if (value != null && value.mainOrder != null && value.mainOrder.detailOrders != null && value.mainOrder.detailOrders.size() > 0) {
                            Items_GetItem itemReq = new Items_GetItem(value.mainOrder.detailOrders.get(0).itemId);
                            sendRequest(mContext, mApiContext, new BaseRequest[]{itemReq});
                            if (itemReq.getReturnCode() == ApiCode.SUCCESS && itemReq.getResponse() != null) {
                                value.itemType = itemReq.getResponse().itemType;
                            }
                        }
                    }

                    if (lsn != null) {
                        lsn.onComplete(true, value, ErrorCode.STATUS_OK, null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    handlerRequestException(e, lsn);
                }
            }
        };
        NetThreadManager.getInstance().execute(requestThread);
    }

    /**
     * 买家确认收货(普通商品)
     *
     * @param bizOrderId
     * @param lsn
     */
    public void doBuyerConfirmGoodsDeliveried(long bizOrderId, final OnResponseListener<Boolean> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        final Trademanager_BuyerConfirmGoodsDeliveried req = new Trademanager_BuyerConfirmGoodsDeliveried(bizOrderId);
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                if (lsn != null) {
                    lsn.onComplete(true, req.getResponse().value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);

            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }

    /**
     * 获取关闭订单理由列表
     *
     * @param orderBizType
     * @param lsn
     */
    public void doGetCloseOrderReasonList(String orderBizType, final OnResponseListener<TmCloseOrderReasonItemList> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        final Trademanager_GetCloseOrderReasonList req = new Trademanager_GetCloseOrderReasonList(orderBizType);
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                TmCloseOrderReasonItemList value = null;
                if (req.getResponse() != null) {
                    try {
                        value = TmCloseOrderReasonItemList.deserialize(req.getResponse().serialize());
                    } catch (JSONException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    }
                }
                if (lsn != null) {
                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);

            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }

    /**
     * 取消订单参数包装
     *
     * @param bizOrderId
     * @param closeReasonId
     * @param lsn
     */
    public void doBuyerCloseOrderWithReason(long bizOrderId, int closeReasonId, final OnResponseListener<Boolean> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        Api_TRADEMANAGER_CloseOrderParam closeOrderParam = new Api_TRADEMANAGER_CloseOrderParam();
        closeOrderParam.bizOrderId = bizOrderId;
        closeOrderParam.closeReasonId = closeReasonId;

        final Trademanager_BuyerCloseOrderWithReason req = new Trademanager_BuyerCloseOrderWithReason(closeOrderParam);
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                if (lsn != null) {
                    lsn.onComplete(true, req.getResponse().value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);

            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }

    /**
     * 获取支付的信息
     *
     * @param bizOrderId 订单ID
     * @param payChannel 支付渠道:PAY_ALI_SDK：支付宝SDK支付
     * @param lsn
     */
    public void doGetPayInfo(long bizOrderId, String payChannel, final OnResponseListener<TmPayInfo> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        final Trademanager_GetPayInfo req = new Trademanager_GetPayInfo(bizOrderId, payChannel);
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                TmPayInfo value = null;
                if (req.getResponse() != null) {
                    try {
                        value = TmPayInfo.deserialize(req.getResponse().serialize());
                    } catch (JSONException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    }
                }
                if (lsn != null) {
                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);

            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }

    /**
     * 获取支付的信息
     *
     * @param bizOrderId 订单ID
     * @param payChannel 支付渠道:PAY_ALI_SDK：支付宝SDK支付
     * @param lsn
     */
    public void doGetPayInfoV2(long bizOrderId, String payChannel, final OnResponseListener<TmPayInfo> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        final Trademanager_GetLoginPayInfo req = new Trademanager_GetLoginPayInfo(bizOrderId, payChannel);
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                TmPayInfo value = null;
                if (req.getResponse() != null) {
                    try {
                        value = TmPayInfo.deserialize(req.getResponse().serialize());
                    } catch (JSONException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    }
                }
                if (lsn != null) {
                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);

            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }

    /**
     * 获取微信支付的信息
     *
     * @param bizOrderId     订单ID
     * @param payChannel     支付渠道:PAY_WX：微信SDK支付
     * @param spbillCreateIp 用户端ip
     * @param lsn
     */
    public void doGetWxPayInfo(long bizOrderId,
                               String payChannel,
                               String spbillCreateIp,
                               final OnResponseListener<TmWxPayInfo> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        final Trademanager_GetWxPayInfo req = new Trademanager_GetWxPayInfo(bizOrderId, payChannel, spbillCreateIp);
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                TmWxPayInfo value = null;
                if (req.getResponse() != null) {
                    try {
                        value = TmWxPayInfo.deserialize(req.getResponse().serialize());
                    } catch (JSONException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    }
                }
                if (lsn != null) {
                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);

            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }

    /**
     * 获取支付状态信息
     *
     * @param bizOrderId 订单ID
     * @param lsn
     */
    public void doGetPayStatusInfo(long bizOrderId,
                                   final OnResponseListener<TmPayStatusInfo> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        final Trademanager_GetPayStatusInfo req = new Trademanager_GetPayStatusInfo(bizOrderId);
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                TmPayStatusInfo value = null;
                if (req.getResponse() != null) {
                    try {
                        value = TmPayStatusInfo.deserialize(req.getResponse().serialize());
                    } catch (JSONException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    }
                }
                if (lsn != null) {
                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);

            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }

    /**
     * 获取商品详情+店铺信息
     *
     * @param itemId
     * @param lsn
     */
    public void doGetItemAndSellerInfo(long itemId,
                                       final OnResponseListener<MerchantItem> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        final Items_GetItemAndSellerInfo req = new Items_GetItemAndSellerInfo(itemId);
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                MerchantItem value = null;
                if (req.getResponse() != null) {
                    try {
                        value = MerchantItem.deserialize(req.getResponse().serialize());
                    } catch (JSONException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    }
                }
                if (lsn != null) {
                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);

            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }

    /**
     * 提交评价
     *
     * @param param
     * @param lsn
     */
    public void doPostRate(PostRateParam param,
                           final OnResponseListener<Boolean> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn) || param == null) {
            return;
        }
        final Trademanager_PostRate req = new Trademanager_PostRate(Api_TRADEMANAGER_PostRateParam.deserialize(param.serialize()));
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                if (lsn != null) {
                    lsn.onComplete(true, req.getResponse().value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);

            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }

    /**
     * 获取拆单结果及订单金额
     *
     * @param param
     * @param lsn
     * @throws JSONException
     */
    public void doGetOrderPrice(OrderPriceQueryDTO param,
                                final OnResponseListener<TmCreateOrderContext> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn) || param == null) {
            return;
        }
        final Trademanager_GetOrderPrice req = new Trademanager_GetOrderPrice(Api_TRADEMANAGER_OrderPriceQueryDTO.deserialize(param.serialize()));
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                TmCreateOrderContext value = null;
                if (req.getResponse() != null) {
                    try {
                        value = TmCreateOrderContext.deserialize(req.getResponse().serialize());
                    } catch (JSONException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    }
                }
                if (lsn != null) {
                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);

            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }

    /**
     * 领取优惠券
     *
     * @param voucherTemplateId
     * @param lsn
     */
    public void doGenerateVoucher(long voucherTemplateId, final OnResponseListener<Boolean> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        final Api_TRADEMANAGER_UserBindVoucherDTO userBindVoucherDTO = new Api_TRADEMANAGER_UserBindVoucherDTO();
        userBindVoucherDTO.voucherTemplateId = voucherTemplateId;
        final Trademanager_GenerateVoucher req = new Trademanager_GenerateVoucher(userBindVoucherDTO);

        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                if (lsn != null) {
                    //优惠券+1
                    if (req.getResponse().value) {
                        SPUtils.setCouponCount(mContext, (SPUtils.getCouponCount(mContext) + 1));
                    }
                    lsn.onComplete(true, req.getResponse().value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);
            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }

    /**
     * 查询商品优惠券
     *
     * @param itemId
     * @param lsn
     * @throws JSONException
     */
    public void doQueryItemVoucherList(int pageSize, int pageNo, long itemId, final OnResponseListener<VoucherTemplateResultList> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        final Api_TRADEMANAGER_QueryItemVoucherDTO queryItemVoucherDTO = new Api_TRADEMANAGER_QueryItemVoucherDTO();
        queryItemVoucherDTO.itemId = itemId;
        queryItemVoucherDTO.pageSize = pageSize;
        queryItemVoucherDTO.pageNo = pageNo;

        final Trademanager_QueryItemVoucherList req = new Trademanager_QueryItemVoucherList(queryItemVoucherDTO);
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                VoucherTemplateResultList value = null;
                if (req.getResponse() != null) {
                    try {
                        value = VoucherTemplateResultList.deserialize(req.getResponse().serialize());
                    } catch (JSONException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    }
                }
                if (lsn != null) {
                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);

            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }

    /**
     * 查询我的优惠券
     *
     * @param status
     * @param pageSize
     * @param pageNo
     * @param lsn
     * @throws JSONException
     */
    public void doQueryMyVoucherList(String status, int pageSize, int pageNo, final OnResponseListener<VoucherResultList> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        final Api_TRADEMANAGER_QueryMyVoucherDTO queryMyVoucherDTO = new Api_TRADEMANAGER_QueryMyVoucherDTO();
        queryMyVoucherDTO.status = status;
        queryMyVoucherDTO.pageNo = pageNo;
        queryMyVoucherDTO.pageSize = pageSize;

        final Trademanager_QueryMyVoucherList req = new Trademanager_QueryMyVoucherList(queryMyVoucherDTO);
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                VoucherResultList value = null;
                if (req.getResponse() != null) {
                    try {
                        value = VoucherResultList.deserialize(req.getResponse().serialize());
                    } catch (JSONException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    }
                }
                if (lsn != null) {
                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);

            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }

    /**
     * 查询订单优惠券
     *
     * @param queryOrderVoucherDTO
     * @param lsn
     */
    public void doQueryOrderVoucherList(Api_TRADEMANAGER_QueryOrderVoucherDTO queryOrderVoucherDTO, final OnResponseListener<OrderVoucherResult> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn) || queryOrderVoucherDTO == null) {
            return;
        }

        final Trademanager_QueryOrderVoucherList req = new Trademanager_QueryOrderVoucherList(queryOrderVoucherDTO);

        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                OrderVoucherResult value = null;
                if (req.getResponse() != null) {
                    try {
                        value = OrderVoucherResult.deserialize(req.getResponse().serialize());
                    } catch (JSONException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    }
                }
                if (lsn != null) {
                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);

            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }

    /**
     * 查询店铺优惠券
     *
     * @param sellerId
     * @param lsn
     * @throws JSONException
     */
    public void doQuerySellerVoucherList(long sellerId, final OnResponseListener<VoucherTemplateResultList> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        final Api_TRADEMANAGER_QuerySellerVoucherDTO queryOrderVoucherDTO = new Api_TRADEMANAGER_QuerySellerVoucherDTO();
        queryOrderVoucherDTO.sellerId = sellerId;

        final Trademanager_QuerySellerVoucherList req = new Trademanager_QuerySellerVoucherList(queryOrderVoucherDTO);

        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                VoucherTemplateResultList value = null;
                if (req.getResponse() != null) {
                    try {
                        value = VoucherTemplateResultList.deserialize(req.getResponse().serialize());
                    } catch (JSONException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    }
                }
                if (lsn != null) {
                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);

            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }

    /**
     * 获取用户各种数量
     *
     * @param lsn
     * @throws JSONException
     */
    public void doGetUserAssets(final OnResponseListener<UserAssets> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        final Trademanager_GetUserAssets req = new Trademanager_GetUserAssets();

        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                UserAssets value = null;
                if (req.getResponse() != null) {
                    try {
                        value = UserAssets.deserialize(req.getResponse().serialize());
                    } catch (JSONException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    }
                }
                if (lsn != null) {
                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);

            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }

    /**
     * 积分明细查询
     *
     * @param pageNo
     * @param pageSize
     * @param lsn
     * @throws JSONException
     */
    public void doPointDetailQuery(int pageNo, int pageSize, final OnResponseListener<PointDetailQueryResult> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        final Points_PointDetailQuery req = new Points_PointDetailQuery(pageNo, pageSize);

        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                PointDetailQueryResult value = null;
                if (req.getResponse() != null) {
                    try {
                        value = PointDetailQueryResult.deserialize(req.getResponse().serialize());
                    } catch (JSONException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    }
                }
                if (lsn != null) {
                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);

            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }

    /**
     * 总积分查询
     *
     * @param lsn
     * @throws JSONException
     */
    public void doTotalPointQuery(final OnResponseListener<Long> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        final Points_TotalPointQuery req = new Points_TotalPointQuery();

        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                if (lsn != null) {
                    lsn.onComplete(true, req.getResponse().value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);

            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }

    /**
     * 每日任务清单查询
     *
     * @param pageNo
     * @param pageSize
     * @param lsn
     */
    public void doDailyTaskQuery(int pageNo, int pageSize, final OnResponseListener<DailyTaskQueryResult> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        final Task_DailyTaskQuery req = new Task_DailyTaskQuery(pageNo, pageSize);

        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                DailyTaskQueryResult value = null;
                if (req.getResponse() != null) {
                    try {
                        value = DailyTaskQueryResult.deserialize(req.getResponse().serialize());
                    } catch (JSONException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    }
                }
                if (lsn != null) {
                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);

            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }

    /**
     * 分享每日步数
     *
     * @param lsn
     * @throws JSONException
     */
    public void doShareDailySteps(final OnResponseListener<Long> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        final Task_ShareDailySteps req = new Task_ShareDailySteps();

        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                if (lsn != null) {
                    lsn.onComplete(true, req.getResponse().amount, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);

            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }


    /**
     * 验证今天是否已签到
     *
     * @param lsn
     */
    public void doIsSignIn(final OnResponseListener<Boolean> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        final Task_IsSignIn req = new Task_IsSignIn();

        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                if (lsn != null) {
                    lsn.onComplete(true, req.getResponse().value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);
            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }


    /**
     * 发布商品
     *
     * @param lsn
     */
    public void doPublishService(PublishServiceDO publishServiceDO, final OnResponseListener<Boolean> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn) || publishServiceDO == null) {
            return;
        }
        final SellerAdmin_PublishService req = new SellerAdmin_PublishService(Api_SELLERADMIN_PublishServiceDO.deserialize(publishServiceDO.serialize()));
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                if (lsn != null) {
                    lsn.onComplete(true, req.getResponse().value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);
            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }

    /**
     * 获取咨询商品属性
     *
     * @param lsn
     * @throws JSONException
     */
    public void doGetConsultItemProperties(final OnResponseListener<ConsultCategoryInfoList> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        final SellerAdmin_GetConsultItemProperties req = new SellerAdmin_GetConsultItemProperties();
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                ConsultCategoryInfoList value = null;
                if (req.getResponse() != null) {
                    try {
                        value = ConsultCategoryInfoList.deserialize(req.getResponse().serialize());
                    } catch (JSONException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    }
                }
                if (lsn != null) {
                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);
            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }

    /**
     * 获取商品管理列表
     *
     * @param params
     * @param lsn
     * @throws JSONException
     */
    public void doGetGoodsManagementInfo(ItemQueryParam params, final OnResponseListener<ItemApiResult> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn) || params == null) {
            return;
        }
        final SellerAdmin_GetItemList req = new SellerAdmin_GetItemList(Api_SELLERADMIN_ItemQueryParam.deserialize(params.serialize()));
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                ItemApiResult value = null;
                if (req.getResponse() != null) {
                    try {
                        value = ItemApiResult.deserialize(req.getResponse().serialize());
                    } catch (JSONException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    }
                }
                if (lsn != null) {
                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);

            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }

    /**
     * 获取商品详情页面信息
     *
     * @param lsn
     */
    public void doGetGoodsDetailInfo(ItemQueryParam params, final OnResponseListener<ItemApiResult> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

//        Api_SELLERADMIN_ItemDetail itemDetail = new Api_SELLERADMIN_ItemDetail();
//        itemDetail.goodsManagement = Api_SELLERADMIN_ItemManagement.deserialize(goodsManagement.serialize());
//        itemDetail.id = id;

//        final SellerAdmin_GetItemDetailInfo req = new SellerAdmin_GetItemDetailInfo(Api_SELLERADMIN_ItemQueryParam.deserialize(params.serialize()));
//        new RequestExecutor() {
//
//            @Override
//            protected boolean prepare() {
//                return checkSubmitStatus(lsn);
//            }
//
//            @Override
//            protected void postExecute(BaseRequest<?> request) {
//                ItemApiResult value = null;
//                if (req.getResponse() != null) {
//                    try {
//                        value = ItemApiResult.deserialize(req.getResponse().serialize());
//                    } catch (JSONException e) {
//                        if (lsn != null) {
//                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
//                        }
//                        e.printStackTrace();
//                    }
//                }
//                if (lsn != null) {
//                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
//                }
//            }
//
//            @Override
//            protected void handleException(BaseRequest<?> request) {
//                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);
//
//            }
//
//            @Override
//            protected void handleRequestException(Exception exception) {
//                handlerRequestException(exception, lsn);
//            }
//        }.execute(req);
    }


    /**
     * 发布服务白名单
     *
     * @param lsn
     * @throws JSONException
     */
    public void doCheckWhiteList(final OnResponseListener<Boolean> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
//        final SellerAdmin_CheckWhiteList req = new SellerAdmin_CheckWhiteList();
//        new RequestExecutor() {
//
//            @Override
//            protected boolean prepare() {
//                return checkSubmitStatus(lsn);
//            }
//
//            @Override
//            protected void postExecute(BaseRequest<?> request) {
//                if (lsn != null) {
//                    lsn.onComplete(true, req.getResponse().value, ErrorCode.STATUS_OK, req.getReturnMessage());
//                }
//            }
//
//            @Override
//            protected void handleException(BaseRequest<?> request) {
//                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);
//
//            }
//
//            @Override
//            protected void handleRequestException(Exception exception) {
//                handlerRequestException(exception, lsn);
//            }
//        }.execute(req);
    }

    /**
     * 更新商品状态
     *
     * @param params
     * @param lsn
     * @throws JSONException
     */
    public void doGetUpdateState(ItemQueryParam params, final OnResponseListener<Boolean> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn) || params == null) {
            return;
        }
        final SellerAdmin_UpdateState req = new SellerAdmin_UpdateState(Api_SELLERADMIN_ItemQueryParam.deserialize(params.serialize()));
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                if (lsn != null) {
                    lsn.onComplete(true, req.getResponse().value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);

            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }

    /**
     * 分页查询流程订单列表
     *
     * @param params
     * @param lsn
     * @throws JSONException
     */
    public void doGetProcessOrderList(Api_TRADEMANAGER_ProcessQueryParam params, final OnResponseListener<ProcessOrderList> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn) || params == null) {
            return;
        }
        final Trademanager_GetProcessOrderList req = new Trademanager_GetProcessOrderList(params);
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                ProcessOrderList value = null;
                if (req.getResponse() != null) {
                    try {
                        value = ProcessOrderList.deserialize(req.getResponse().serialize());
                    } catch (JSONException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    }
                }
                if (lsn != null) {
                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);

            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }

    /**
     * 获取流程订单详情
     *
     * @param processOrderId
     * @param lsn
     * @throws JSONException
     */
    public void doGetProcessOrderDetail(long processOrderId, final OnResponseListener<ProcessOrder> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        final Trademanager_GetProcessOrderDetail req = new Trademanager_GetProcessOrderDetail(processOrderId);
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                ProcessOrder value = null;
                if (req.getResponse() != null) {
                    try {
                        value = ProcessOrder.deserialize(req.getResponse().serialize());
                    } catch (JSONException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    }
                }
                if (lsn != null) {
                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);

            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }

    /**
     * 达人接单
     *
     * @param buyerId
     * @param processType
     * @param lsn
     * @throws JSONException
     */
    public void doAcceptProcessOrder(long buyerId, String processType, final OnResponseListener<AcceptProcessOrderResult> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        Api_TRADEMANAGER_AcceptProcessOrderParam param = new Api_TRADEMANAGER_AcceptProcessOrderParam();
        param.buyerId = buyerId;
        param.processType = processType;

        final Trademanager_AcceptProcessOrder req = new Trademanager_AcceptProcessOrder(param);
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                AcceptProcessOrderResult value = null;
                if (req.getResponse() != null) {
                    try {
                        value = AcceptProcessOrderResult.deserialize(req.getResponse().serialize());
                    } catch (JSONException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    }
                }
                if (lsn != null) {
                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);

            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }

    /**
     * 查询用户能否下咨询单
     *
     * @param itemId
     * @param lsn
     * @throws JSONException
     */
    public void doQuerySellerAndConsultState(long itemId, final OnResponseListener<SellerAndConsultStateResult> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        Api_TRADEMANAGER_QuerySellerAndConsultStateParam param = new Api_TRADEMANAGER_QuerySellerAndConsultStateParam();
        param.itemId = itemId;

        final Trademanager_QuerySellerAndConsultState req = new Trademanager_QuerySellerAndConsultState(param);
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                SellerAndConsultStateResult value = null;
                if (req.getResponse() != null) {
                    try {
                        value = SellerAndConsultStateResult.deserialize(req.getResponse().serialize());
                    } catch (JSONException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    }
                }
                if (lsn != null) {
                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);

            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }

    /**
     * 达人查询咨询状态
     *
     * @param params
     * @param lsn
     * @throws JSONException
     */
    public void doGetConsultInfoForSeller(List<String> params, final OnResponseListener<ConsultState> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        Api_TRADEMANAGER_QueryConsultStateParam queryConsultStateParam = new Api_TRADEMANAGER_QueryConsultStateParam();
        queryConsultStateParam.processOrderStatuses = params;

        final Trademanager_GetConsultInfoForSeller req = new Trademanager_GetConsultInfoForSeller(queryConsultStateParam);
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                ConsultState value = null;
                if (req.getResponse() != null) {
                    try {
                        value = ConsultState.deserialize(req.getResponse().serialize());
                    } catch (JSONException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    }
                }
                if (lsn != null) {
                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);

            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }


    /**
     * 创建流程订单
     *
     * @param params
     * @param lsn
     * @throws JSONException
     */
    public void doCreateProcessOrder(Api_TRADEMANAGER_CreateProcessOrderParam params, final OnResponseListener<CreateProcessOrderResultTO> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn) || params == null) {
            return;
        }

        final Trademanager_CreateProcessOrder req = new Trademanager_CreateProcessOrder(params);
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                CreateProcessOrderResultTO value = null;
                if (req.getResponse() != null) {
                    try {
                        value = CreateProcessOrderResultTO.deserialize(req.getResponse().serialize());
                    } catch (JSONException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    }
                }
                if (lsn != null) {
                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);

            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }

    /**
     * 用户结束流程
     *
     * @param itemId
     * @param processOrderId
     * @param lsn
     * @throws JSONException
     */
    public void doFinishConsult(long itemId, long processOrderId, final OnResponseListener<FinishProcessResult> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        Api_TRADEMANAGER_FinishProcessParam params = new Api_TRADEMANAGER_FinishProcessParam();
        params.itemId = itemId;
        params.processOrderId = processOrderId;

        final Trademanager_FinishConsult req = new Trademanager_FinishConsult(params);
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                FinishProcessResult value = null;
                if (req.getResponse() != null) {
                    try {
                        value = FinishProcessResult.deserialize(req.getResponse().serialize());
                    } catch (JSONException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    }
                }
                if (lsn != null) {
                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);

            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }

    /**
     * 用户取消流程
     *
     * @param processOrderId
     * @param lsn
     * @throws JSONException
     */
    public void doCancelProcessOrder(long processOrderId, final OnResponseListener<CancelProcessResult> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        Api_TRADEMANAGER_CancelProcessParam params = new Api_TRADEMANAGER_CancelProcessParam();
        params.processOrderId = processOrderId;

        final Trademanager_CancelProcessOrder req = new Trademanager_CancelProcessOrder(params);
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                CancelProcessResult value = null;
                if (req.getResponse() != null) {
                    try {
                        value = CancelProcessResult.deserialize(req.getResponse().serialize());
                    } catch (JSONException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    }
                }
                if (lsn != null) {
                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);

            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }

    /**
     * 根据商品ids批量查询商品
     *
     * @param itemIds
     * @param lsn
     * @throws JSONException
     */
    public void doGetItemListByItemIds(long[] itemIds, final OnResponseListener<ShortItemsResult> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        final Items_GetItemListByItemIds req = new Items_GetItemListByItemIds(itemIds);
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                ShortItemsResult value = null;
                if (req.getResponse() != null) {
                    try {
                        value = ShortItemsResult.deserialize(req.getResponse().serialize());
                    } catch (JSONException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    }
                }
                if (lsn != null) {
                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);

            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }

    /**
     * 获取流程状态（恢复现场）
     *
     * @param params
     * @param lsn
     * @throws JSONException
     */
    public void doGetProcessState(Api_TRADEMANAGER_ProcessStateQuery params, final OnResponseListener<ProcessState> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn) || params == null) {
            return;
        }
        final Trademanager_GetProcessState req = new Trademanager_GetProcessState(params);
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                ProcessState value = null;
                if (req.getResponse() != null) {
                    try {
                        value = ProcessState.deserialize(req.getResponse().serialize());
                    } catch (JSONException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    }
                }
                if (lsn != null) {
                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);

            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }

    /**
     * 获取达人咨询商品列表
     *
     * @param params
     * @param lsn
     * @throws JSONException
     */
    public void doGetConsultItemList(QueryTermsDTO params, final OnResponseListener<ShortItemsResult> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn) || params == null) {
            return;
        }
        final Items_GetConsultItemList req = new Items_GetConsultItemList(Api_ITEMS_QueryTermsDTO.deserialize(params.serialize()));
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                ShortItemsResult value = null;
                if (req.getResponse() != null) {
                    try {
                        value = ShortItemsResult.deserialize(req.getResponse().serialize());
                    } catch (JSONException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    }
                }
                if (lsn != null) {
                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);

            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }


    /**
     * 查询商品
     *
     * @param lsn
     * @throws JSONException
     */
    public void doGetPublishItemInfo(ItemQueryParam params, final OnResponseListener<PublishServiceDO> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn) || params == null) {
            return;
        }
        final SellerAdmin_GetPublishItemInfo req = new SellerAdmin_GetPublishItemInfo(Api_SELLERADMIN_ItemQueryParam.deserialize(params.serialize()));
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                PublishServiceDO value = null;
                if (req.getResponse() != null) {
                    try {
                        value = PublishServiceDO.deserialize(req.getResponse().serialize());
                    } catch (JSONException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    }
                }
                if (lsn != null) {
                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);

            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }

    /**
     * 更新商品状态
     *
     * @param params
     * @param lsn
     * @throws JSONException
     */
    public void doUpdateState(ItemQueryParam params, final OnResponseListener<Boolean> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        final SellerAdmin_UpdateState req = new SellerAdmin_UpdateState(Api_SELLERADMIN_ItemQueryParam.deserialize(params.serialize()));
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                if (lsn != null) {
                    lsn.onComplete(true, req.getResponse().value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);

            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }

    /**
     * 获取快速咨询商品
     *
     * @param params
     * @param lsn
     * @throws JSONException
     */
    public void doGetFastConsultItem(ConsultContent params, final OnResponseListener<TmConsultInfo> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        final Items_GetFastConsultItem req = new Items_GetFastConsultItem(Api_ITEMS_ConsultContent.deserialize(params.serialize()));
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                TmConsultInfo value = null;
                if (req.getResponse() != null) {
                    try {
                        value = TmConsultInfo.deserialize(req.getResponse().serialize());
                    } catch (JSONException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    }
                }
                if (lsn != null) {
                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);

            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }

    /**
     * 获取物流详情
     *
     * @param logisticsId
     * @param lsn
     * @throws JSONException
     */
    public void doGetLogisticsList(long logisticsId, final OnResponseListener<LgExpressInfo> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
//        final Lgcenter_GetExpressInfo req = new Lgcenter_GetExpressInfo(logisticsId);
//        new RequestExecutor() {
//
//            @Override
//            protected boolean prepare() {
//                return checkSubmitStatus(lsn);
//            }
//
//            @Override
//            protected void postExecute(BaseRequest<?> request) {
//                LgExpressInfo value = null;
//                if (req.getResponse() != null) {
//                    try {
//                        value = LgExpressInfo.deserialize(req.getResponse().serialize());
//                    } catch (JSONException e) {
//                        if (lsn != null) {
//                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
//                        }
//                        e.printStackTrace();
//                    }
//                }
//                if (lsn != null) {
//                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
//                }
//            }
//
//            @Override
//            protected void handleException(BaseRequest<?> request) {
//                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);
//
//            }
//
//            @Override
//            protected void handleRequestException(Exception exception) {
//                handlerRequestException(exception, lsn);
//            }
//        }.execute(req);
    }

    /**
     * 批量删除购物车
     *
     * @param deleteCartInfo
     * @param lsn
     * @throws JSONException
     */
    public void doDeleteCart(DeleteCartInfo deleteCartInfo, final OnResponseListener<Boolean> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        final Cart_DeleteCart req = new Cart_DeleteCart(Api_CART_DeleteCartInfo.deserialize(deleteCartInfo.serialize()));
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                if (lsn != null) {
                    lsn.onComplete(true, req.getResponse().value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);

            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }

    /**
     * 获取购物车列表
     *
     * @param lsn
     * @throws JSONException
     */
    public void doGetCartInfoList(final OnResponseListener<CartInfoListResult> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        final Cart_GetCartInfoList req = new Cart_GetCartInfoList();
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                CartInfoListResult value = null;
                if (req.getResponse() != null) {
                    try {
                        value = CartInfoListResult.deserialize(req.getResponse().serialize());
                    } catch (JSONException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    }
                }
                if (lsn != null) {
                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);

            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }

    /**
     * 加入购物车
     * @param createCartInfo
     * @param lsn
     * @throws JSONException
     */
    public void doSaveToCart(CreateCartInfo createCartInfo, final OnResponseListener<Boolean> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        final Cart_SaveToCart req = new Cart_SaveToCart(Api_CART_CreateCartInfo.deserialize(createCartInfo.serialize()));
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                if (lsn != null) {
                    lsn.onComplete(true, req.getResponse().value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);

            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }


    /**
     * 勾选购物车
     *
     * @param selectCartInfo
     * @param lsn
     * @throws JSONException
     */
    public void doSelectCart(SelectCartInfo selectCartInfo, final OnResponseListener<Boolean> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        final Cart_SelectCart req = new Cart_SelectCart(Api_CART_SelectCartInfo.deserialize(selectCartInfo.serialize()));
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                if (lsn != null) {
                    lsn.onComplete(true, req.getResponse().value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);

            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }

    /**
     * 购物车数量
     *
     * @param lsn
     * @throws JSONException
     */
    public void doSelectCartAmount(final OnResponseListener<CartAmountResult> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        final Cart_SelectCartAmount req = new Cart_SelectCartAmount();
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                CartAmountResult value = null;
                if (req.getResponse() != null) {
                    try {
                        value = CartAmountResult.deserialize(req.getResponse().serialize());
                    } catch (JSONException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    }
                }
                if (lsn != null) {
                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);

            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }

    /**
     * 修改商品数量
     *
     * @param updateAmountCartInfo
     * @param lsn
     * @throws JSONException
     */
    public void doUpdateCartAmount(UpdateAmountCartInfo updateAmountCartInfo, final OnResponseListener<Boolean> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        final Cart_UpdateCartAmount req = new Cart_UpdateCartAmount(Api_CART_UpdateAmountCartInfo.deserialize(updateAmountCartInfo.serialize()));
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                if (lsn != null) {
                    lsn.onComplete(true, req.getResponse().value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);

            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }

    /**
     * 通过买家id和订单id查询交易订单(包含子订单)
     * @param bizOrderId
     * @param lsn
     * @throws JSONException
     */
    public void doQueryBizOrderInfoForBuyer(long bizOrderId, final OnResponseListener<OrderDetailResult> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        final Trademanager_QueryBizOrderInfoForBuyer req = new Trademanager_QueryBizOrderInfoForBuyer(bizOrderId);
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                OrderDetailResult value = null;
                if (req.getResponse() != null) {
                    try {
                        value = OrderDetailResult.deserialize(req.getResponse().serialize());
                    } catch (JSONException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    }
                }
                if (lsn != null) {
                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);

            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }

}

