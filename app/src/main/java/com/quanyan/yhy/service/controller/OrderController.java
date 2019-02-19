package com.quanyan.yhy.service.controller;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.quanyan.base.BaseController;
import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.AnalyDataValue;
import com.quanyan.yhy.net.NetManager;
import com.quanyan.yhy.net.lsn.OnResponseListener;
import com.quanyan.yhy.ui.base.utils.DialogUtil;
import com.quanyan.yhy.ui.base.utils.TCEventHelper;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.tab.homepage.order.CancelOrderView;
import com.yhy.common.beans.net.model.item.CodeQueryDTO;
import com.yhy.common.beans.net.model.tm.OrderDetailResult;
import com.yhy.common.beans.net.model.tm.TmCloseOrderReasonItemList;
import com.yhy.common.beans.net.model.tm.TmDetailOrder;
import com.yhy.common.beans.net.model.tm.TmMainOrder;
import com.yhy.common.beans.net.model.tm.TmOrderList;
import com.yhy.common.beans.net.model.trip.ShortItemsResult;
import com.yhy.common.constants.ValueConstants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dengmingjia on 2015/11/5.
 * 我的订单列表Controller
 */
public class OrderController extends BaseController {
    public static final String PARAMS_ORDER_TYPE = "orderType";
    public static final String PARAMS_ORDER_CODE = "orderCode";
    public static final String PARAMS_ORDER_CHECK_TYPE = "checkType";

    //订单子类型
    /**
     * 类型 所有订单
     */
    public static final int CHILD_TYPE_ALL = 0;
    /**
     * 类型 准备付款
     */
    public static final int CHILD_TYPE_PREPARE = 1;
    /**
     * 类型 已经付款
     */
    public static final int CHILD_TYPE_PAID = 2;
    public static final int CHILD_TYPE_SHIPPING = 3;
    /**
     * 类型 订单完成
     */
    public static final int CHILD_DONE = 4;
    private Dialog cancelOrderDialog;

    public OrderController(Context context, Handler handler) {
        super(context, handler);
    }

    /**
     * 获取订单列表
     *
     * @param type      订单类型
     * @param pageSize  分页每页条数
     * @param pageIndex 分页页数
     */
    public void doGetOrderList(final Context context, String orderType, final int typeStatus, int pageSize, final int pageIndex) {
        String statusType = getOrderStatus(typeStatus);
        NetManager.getInstance(context).doGetOrderList(orderType, statusType, pageIndex, pageSize, new OnResponseListener<TmOrderList>() {
            @Override
            public void onComplete(boolean isOK, TmOrderList result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(ValueConstants.GET_ORDER_LIST_SUCCESS, typeStatus, pageIndex, result);
                    return;
                }
                sendMessage(ValueConstants.GET_ORDER_LIST_FAIL, typeStatus, errorCode, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.GET_ORDER_LIST_FAIL, typeStatus, errorCode, errorMessage);
            }
        });
    }

    /**
     * 根据orderType index 获取服务器需要的字符串
     */
    public static String getOrderTypeStringForComment(String orderType) {
        String type = null;
        if (orderType.equals(ValueConstants.ORDER_TYPE_TOUR_LINE) || orderType.equals(ValueConstants.ORDER_TYPE_FREE_LINE) || orderType.equals(ValueConstants.ORDER_TYPE_TOUR_LINE_ABOARD) || orderType.equals(ValueConstants.ORDER_TYPE_FREE_LINE_ABOARD)) {
            type = ValueConstants.ORDER_TYPE_LINE;
        } else if (orderType.equals(ValueConstants.ORDER_TYPE_HOTEL) || orderType.equals(ValueConstants.ORDER_TYPE_HOTEL_OFFLINE)) {
            type = ValueConstants.ORDER_TYPE_HOTEL;
        } else if (orderType.equals(ValueConstants.ORDER_TYPE_SPOTS)) {
            type = ValueConstants.ORDER_TYPE_SCENIC;
//        } else if (orderParentType == PARENT_TYPE_HANDCEREMONY) {
//            type = ValueConstants.ORDER_TYPE_GIFT;
        } else if (orderType.equals(ValueConstants.ORDER_TYPE_ACTIVITY) || orderType.equals(ValueConstants.ORDER_TYPE_CITY_ACTIVITY)) {
            type = ValueConstants.ORDER_TYPE_LOCAL;
        } else if (orderType.equals(ValueConstants.ORDER_TYPE_NORMAL)) {
            type = ValueConstants.ORDER_TYPE_POINT;
        }
        return type;
    }

    /**
     * 获取订单的子分类
     *
     * @param childType
     * @return
     */
    private String getOrderStatus(int childType) {
        String type = null;
        if (childType == ValueConstants.KEY_ORDER_STATUS_ALL) {
            type = ValueConstants.ORDER_STATUS_ALL;
        } else if (childType == ValueConstants.KEY_ORDER_STATUS_UNPAY) {
            type = ValueConstants.ORDER_STATUS_WAITING_PAY;
        } else if (childType == ValueConstants.KEY_ORDER_STATUS_VALID) {
            type = ValueConstants.ORDER_STATUS_VALID;
        } else if (childType == ValueConstants.KEY_ORDER_STATUS_UNCOMMENT) {
            type = ValueConstants.ORDER_STATUS_UNCOMMENT;
        }
        return type;
    }

    /**
     * 获取订单详情
     *
     * @param orderCode 订单code
     */
    public void doGetOrderDetails(final Context context, long orderCode) {
        NetManager.getInstance(context).doQueryBizOrderInfoForBuyer(orderCode, new OnResponseListener<OrderDetailResult>() {
            @Override
            public void onComplete(boolean isOK, OrderDetailResult result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(ValueConstants.GET_ORDER_DETAILS_SUCCESS, result);
                    return;
                }
                sendMessage(ValueConstants.GET_ORDER_DETAILS_FAIL, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.GET_ORDER_DETAILS_FAIL, errorCode, 0, errorMessage);
            }
        });


      /*  NetManager.getInstance(context).doGetOrderDetailAndItemType(orderCode, new OnResponseListener<TmOrderDetail>() {
            @Override
            public void onComplete(boolean isOK, TmOrderDetail result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(ValueConstants.GET_ORDER_DETAILS_SUCCESS, result);
                    return;
                }
                sendMessage(ValueConstants.GET_ORDER_DETAILS_FAIL, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.GET_ORDER_DETAILS_FAIL, errorCode, 0, errorMessage);
            }
        });
*/

    }

    /**
     * 获取猜你喜欢商品
     *
     * @param context
     * @param boothCode
     */
    public void doGetRecommendCode(final Context context, CodeQueryDTO boothCode) {
        NetManager.getInstance(context).doGetItemListByCode(boothCode, new OnResponseListener<ShortItemsResult>() {

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.POINT_ORDER_DETAIL_LIKE_RECOMMEND_KO, errorCode, 0, errorMessage);
            }

            @Override
            public void onComplete(boolean isOK, ShortItemsResult result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(ValueConstants.POINT_ORDER_DETAIL_LIKE_RECOMMEND_OK, result);
                    return;
                }
                sendMessage(ValueConstants.POINT_ORDER_DETAIL_LIKE_RECOMMEND_KO, errorCode, 0, errorMsg);
            }
        });
    }

    /**
     * 根据服务器订单码返回界面显示
     *
     * @param orderStatus
     * @param context
     * @return
     */

    public static String getOrderStatusString(String orderStatus, String orderType, Context context) {
        int resid = -1;
        if (ValueConstants.ORDER_STATUS_WAITING_PAY.equals(orderStatus)) {
            resid = R.string.order_status_waiting_pay;
        } else if (ValueConstants.ORDER_STATUS_WAITING_DELIVERY.equals(orderStatus)) {
            if (ValueConstants.ORDER_TYPE_NORMAL.equals(orderType) || ValueConstants.ORDER_TYPE_POINT_MALL.equals(orderType) ) {
                resid = R.string.waitting_delivery;
            } else {
                resid = R.string.order_status_uncheck;
            }
        } else if (ValueConstants.ORDER_STATUS_SHIPPING.equals(orderStatus)) {
            if (orderType.equals(ValueConstants.ORDER_TYPE_SPOTS)) {
                resid = R.string.order_status_waitting_use;
            } else if (orderType.equals(ValueConstants.ORDER_TYPE_HOTEL) || orderType.equals(ValueConstants.ORDER_TYPE_HOTEL_OFFLINE)) {
                resid = R.string.order_status_appointment_success;
            } else if (orderType.equals(ValueConstants.ORDER_TYPE_ACTIVITY) || orderType.equals(ValueConstants.ORDER_TYPE_CITY_ACTIVITY) || orderType.equals(ValueConstants.ORDER_TYPE_TOUR_LINE) || orderType.equals(ValueConstants.ORDER_TYPE_FREE_LINE) || orderType.equals(ValueConstants.ORDER_TYPE_TOUR_LINE_ABOARD) || orderType.equals(ValueConstants.ORDER_TYPE_FREE_LINE_ABOARD)) {
                resid = R.string.order_status_waitting_go;
            } else if (orderType.equals(ValueConstants.ORDER_TYPE_NORMAL)) {
                resid = R.string.waitting_receiver;
            }
        } else if (ValueConstants.ORDER_STATUS_FINISH.equals(orderStatus) || ValueConstants.ORDER_STATUS_RATED.equals(orderStatus) || ValueConstants.ORDER_STATUS_NOT_RATE.equals(orderStatus)) {
            resid = R.string.order_status_finish;
        } else if (ValueConstants.ORDER_STATUS_CANCEL.equals(orderStatus)) {
            resid = R.string.order_status_cancel;
        } else if (ValueConstants.ORDER_STATUS_REFUNDED.equals(orderStatus)) {
            resid = R.string.order_status_refunded;
        } else if (ValueConstants.ORDER_STATUS_CLOSED.equals(orderStatus)) {
            resid = R.string.order_status_closed;
        } else if (ValueConstants.ORDER_STATUS_CONFIRMED_CLOSE.equals(orderStatus)) {
            resid = R.string.order_status_confirmed_close;
        } else if (ValueConstants.ORDER_STATUS_CONFIRMED.equals(orderStatus)) {
            resid = R.string.order_status_confirmed;
        }
        if (resid == -1)
            return null;
        return context.getString(resid);
    }
    public static String getOrderStatus(List<TmDetailOrder> detailOrders, Context context) {


        for (int i = 0; i < detailOrders.size(); i++) {
            String orderStatus = detailOrders.get(i).bizOrder.orderStatus;
            String orderType = detailOrders.get(i).bizOrder.orderType;
            if (ValueConstants.ORDER_STATUS_SHIPPING.equals(orderStatus)) {
                if (orderType.equals(ValueConstants.ORDER_TYPE_NORMAL)) {
                    return context.getString(R.string.waitting_receiver);
                }
            }
        }
        return context.getString(R.string.waitting_delivery);
    }
    public static boolean getLogistState(TmMainOrder mainOrder) {
        List<TmDetailOrder> detailOrders = mainOrder.detailOrders;
        if (detailOrders != null && detailOrders.size() > 0) {//有子订单
            for (int i = 0; i < detailOrders.size(); i++) {
                String orderStatus = detailOrders.get(i).bizOrder.orderStatus;
                if (ValueConstants.ORDER_STATUS_SHIPPING.equals(orderStatus) || ValueConstants.ORDER_STATUS_FINISH.equals(orderStatus) || ValueConstants.ORDER_STATUS_RATED.equals(orderStatus) || ValueConstants.ORDER_STATUS_NOT_RATE.equals(orderStatus)) {
                    return true;
                }
            }
        } else {// 无子订单
            String orderStatus = mainOrder.bizOrder.orderStatus;
            if (ValueConstants.ORDER_STATUS_SHIPPING.equals(orderStatus) || ValueConstants.ORDER_STATUS_FINISH.equals(orderStatus) || ValueConstants.ORDER_STATUS_RATED.equals(orderStatus) || ValueConstants.ORDER_STATUS_NOT_RATE.equals(orderStatus)) {
                return true;
            }
        }


        return false;
    }
    public  static boolean isGoodsRate(TmMainOrder bean) {
        List<TmDetailOrder> detailOrders = bean.detailOrders;
        if (detailOrders != null && detailOrders.size() > 0) {
            for (int i = 0; i < detailOrders.size(); i++) {
                String orderStatus = detailOrders.get(i).bizOrder.orderStatus;
                if (ValueConstants.ORDER_STATUS_NOT_RATE.equals(orderStatus))
                    return true;
            }
        } else {
            return bean.buttonStatus.rateButton;
        }
        return false;
    }

    /**
     * 取消订单
     */
    public void cancelOrder(final Context context, long orderId, int reasonId) {
        sendShowLoadingView("取消订单");
        NetManager.getInstance(context).doBuyerCloseOrderWithReason(orderId, reasonId, new OnResponseListener<Boolean>() {
            @Override
            public void onComplete(boolean isOK, Boolean result, int errorCode, String errorMsg) {
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
     * 订单确认收货
     *
     * @param orderId
     */
    public void confirmOrder(final Context context, long orderId) {
        NetManager.getInstance(context).doBuyerConfirmGoodsDeliveried(orderId, new OnResponseListener<Boolean>() {
            @Override
            public void onComplete(boolean isOK, Boolean result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(ValueConstants.CONFIRM_RECIVER_SUCCESS, result);
                    return;
                }
                sendMessage(ValueConstants.CONFIRM_RECIVER_FAIL, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.CONFIRM_RECIVER_FAIL, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 通知页面提出loadingdialog
     */
    public void sendShowLoadingView(String message) {
        sendMessage(ValueConstants.SHOW_LOADING_VIEW, message);
    }

    /**
     * 取消订单的提醒框
     */
    public void showCancelOrderDialog(final Context context, final long orderId, String orderType) {
        NetManager.getInstance(context).doGetCloseOrderReasonList(orderType, new OnResponseListener<TmCloseOrderReasonItemList>() {
            @Override
            public void onComplete(boolean isOK, TmCloseOrderReasonItemList result, int errorCode, String errorMsg) {
                if (result != null && result.value != null && result.value.size() > 0) {
                    cancelOrderDialog = DialogUtil.getMenuDialog((Activity) context, new CancelOrderView(context, new CancelOrderView.CancelOrderClickListener() {
                        @Override
                        public void onCancel() {
                            cancelOrderDialog.dismiss();
                        }

                        @Override
                        public void onSubmit(long reasonId, String reasonText) {
                            cancelOrder(context, orderId, (int) reasonId);
                            cancelOrderDialog.dismiss();
                            //打点
                            Map<String, String> stringMap = new HashMap<String, String>();
                            stringMap.put(AnalyDataValue.KEY_REASON, reasonText);
                            stringMap.put(AnalyDataValue.KEY_ORDER_ID, String.valueOf(orderId));
                            TCEventHelper.onEvent(context, AnalyDataValue.CANCEL_ORDER, stringMap);

                        }
                    }, result.value));
                    cancelOrderDialog.setCanceledOnTouchOutside(true);
                    cancelOrderDialog.show();
                }
            }

            @Override
            public void onInternError(final int errorCode, String errorMessage) {
                mUiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showToast(context, StringUtil.handlerErrorCode(context, errorCode));
                    }
                });
            }
        });
    }

    public static String getLatestArriveTime(String string) {
        if (TextUtils.isEmpty(string)) return null;
        String temp = null;
        String[] arr = string.split(":");
        int a = 0;
        try {
            a = Integer.parseInt(arr[0]);
        } catch (Exception e) {
            e.printStackTrace();
            return string;
        }
        if (a == 24) {
            temp = "次日00:00前";
        } else if (a < 24) {
            temp = string + "前";
        } else if (a > 24) {
            int d = a - 24;
            if (d < 10) {
                temp = "次日0" + d + ":00前";
            } else {
                temp = "次日" + d + ":00前";
            }
        }
        return temp;
    }


    /**
     * 获取商品评价列表
     *
     * @param context
     * @param orderId
     */
    public void doQueryRateBuyOrder(final Context context, long orderId) {
        NetManager.getInstance(context).doQueryRateBuyOrder(orderId, new OnResponseListener<OrderDetailResult>() {
            @Override
            public void onComplete(boolean isOK, OrderDetailResult result, int errorCode, String errorMsg) {
                if (isOK) {
                    if (result == null) {
                        result = new OrderDetailResult();
                    }
                    sendMessage(ValueConstants.MSG_GET_GOOD_RATE_LIST_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_GET_GOOD_RATE_LIST_KO, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_GET_GOOD_RATE_LIST_KO, errorCode, 0, errorMessage);
            }
        });

    }

}
