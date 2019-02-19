package com.quanyan.yhy.ui.travel.controller;

import android.content.Context;
import android.os.Handler;

import com.quanyan.base.BaseController;
import com.quanyan.yhy.net.NetManager;
import com.quanyan.yhy.net.lsn.OnResponseListener;
import com.yhy.common.beans.net.model.item.CityActivityDetail;
import com.yhy.common.beans.net.model.shop.MerchantItem;
import com.yhy.common.beans.net.model.tm.SkuListWithStartDate;
import com.yhy.common.beans.net.model.tm.TmCreateOrderContext;
import com.yhy.common.beans.net.model.tm.TmCreateOrderParam;
import com.yhy.common.beans.net.model.tm.TmCreateOrderResultTO;
import com.yhy.common.beans.net.model.tm.TmUserRoute;
import com.yhy.common.beans.net.model.tm.VoucherTemplateResultList;
import com.yhy.common.beans.net.model.trip.ItemVO;
import com.yhy.common.beans.net.model.trip.LineDetail;
import com.yhy.common.beans.net.model.trip.LineItemDetail;
import com.yhy.common.constants.ValueConstants;

/**
 * Created by zhaoxp on 2015-11-10.
 */
public class TravelController extends BaseController {

    public TravelController(Context context, Handler handler) {
        super(context, handler);
    }

    /**
     * 达人详情页
     *
     * @param id
     */
    public void doGetCityActivityDetail(final Context context, final long id) {
        NetManager.getInstance(context).doGetCityActivityDetail(id, new OnResponseListener<CityActivityDetail>() {
            @Override
            public void onComplete(boolean isOK, CityActivityDetail result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(ValueConstants.MSG_LINE_DETAIL_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_LINE_DETAIL_ERROR, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_LINE_DETAIL_ERROR, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 必买商品详情
     *
     * @param id
     */
    public void doGetNormalProductDetail(final Context context, final long id) {
        NetManager.getInstance(context).doGetNormItemDetail(id, new OnResponseListener<MerchantItem>() {
            @Override
            public void onComplete(boolean isOK, MerchantItem result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(ValueConstants.MSG_LINE_DETAIL_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_LINE_DETAIL_ERROR, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_LINE_DETAIL_ERROR, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 获取线路详情（跟团，自由行）
     *
     * @param id
     */
    public void doGetLineDetail(final Context context, final long id) {
        NetManager.getInstance(context).doGetLineDetailByItemId(id, new OnResponseListener<LineItemDetail>() {
            @Override
            public void onComplete(boolean isOK, LineItemDetail result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(ValueConstants.MSG_LINE_DETAIL_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_LINE_DETAIL_ERROR, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_LINE_DETAIL_ERROR, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 获取我的行程列表
     */
    public void doGetTravelHomeData(final int pageIndex, int pageSize) {
//        NetManager.getInstance(context).doGetUserRouteList(pageIndex, pageSize, new OnResponseListener<TmUserRouteList>() {
//            @Override
//            public void onComplete(boolean isOK, TmUserRouteList result, int errorCode, String errorMsg) {
//                if (isOK) {
//                    if (result == null) {
//                        result = new TmUserRouteList();
//                    } else {
//                        if (pageIndex == 1) {
//                            new CacheManager(context, mUiHandler).saveMyTripPageData(result);
//                        }
//                    }
//                    sendMessage(ValueConstants.MSG_TRIP_DATA_OK, result);
//                    return;
//                }
//                sendMessage(ValueConstants.MSG_TRIP_DATA_ERROR, errorCode, 0, errorMsg);
//            }
//
//            @Override
//            public void onInternError(int errorCode, String errorMessage) {
//                sendMessage(ValueConstants.MSG_TRIP_DATA_ERROR, errorCode, 0, errorMessage);
//            }
//        });
    }

    /**
     * 获取我的行程详情
     *
     * @param lineId
     */
    public void doGetUserTripDetail(final Context context, final long lineId) {
        NetManager.getInstance(context).doGetUserRouteDetail(lineId, new OnResponseListener<TmUserRoute>() {
            @Override
            public void onComplete(boolean isOK, TmUserRoute result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(ValueConstants.MSG_TRIP_DATA_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_TRIP_DATA_ERROR, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_TRIP_DATA_ERROR, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 获取行程推荐列表
     */
    public void doGetRecommendRouteList() {
//        NetManager.getInstance(context).doGetRecommandTravelList(new OnResponseListener<MyTripContent>() {
//            @Override
//            public void onComplete(boolean isOK, MyTripContent result, int errorCode, String errorMsg) {
//                if (isOK) {
//                    if (result == null) {
//                        result = new MyTripContent();
//                    } else {
//                        new CacheManager(context, mUiHandler).saveTripPageData(result);
//                    }
//                    sendMessage(ValueConstants.MSG_TRIP_DATA_RECOMMEND_OK, result);
//                    return;
//                }
//                sendMessage(ValueConstants.MSG_TRIP_DATA_RECOMMEND_ERROR, errorCode, 0, errorMsg);
//            }
//
//            @Override
//            public void onInternError(int errorCode, String errorMessage) {
//                sendMessage(ValueConstants.MSG_TRIP_DATA_RECOMMEND_ERROR, errorCode, 0, errorMessage);
//            }
//        });
    }

    /**
     * 线路详情
     *
     * @param lineId
     */
    public void doGetRouteDetail(final Context context, long lineId) {
        NetManager.getInstance(context).doGetLinesDetail(lineId, new OnResponseListener<LineDetail>() {
            @Override
            public void onComplete(boolean isOK, LineDetail result, int errorCode, String errorMsg) {
                if (isOK) {
                    if (result == null) {
                        result = new LineDetail();
                    }
                    sendMessage(ValueConstants.MSG_LINE_DETAIL_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_LINE_DETAIL_ERROR, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_LINE_DETAIL_ERROR, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 获取出发日期
     *
     * @param itemId
     */
    public void doGetDepartureDateList(final Context context, long itemId) {
        NetManager.getInstance(context).doGetItem(itemId, new OnResponseListener<ItemVO>() {
            @Override
            public void onComplete(boolean isOK, ItemVO result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(ValueConstants.MSG_GET_DEPARTURE_DATE_DATA_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_GET_DEPARTURE_DATE_DATA_KO, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_GET_DEPARTURE_DATE_DATA_KO, errorCode, 0, errorMessage);

            }
        });
    }

    /**
     * 获取订单上下文
     *
     * @param id
     */
    public void getCreateOrderContext(final Context context, long id) {
        NetManager.getInstance(context).doGetCreateOrderContext(id, new OnResponseListener<TmCreateOrderContext>() {
            @Override
            public void onComplete(boolean isOK, TmCreateOrderContext result, int errorCode, String errorMsg) {
                if (isOK) {
                    if (result == null) {
                        result = new TmCreateOrderContext();
                    }
                    sendMessage(ValueConstants.MSG_GET_ORDER_CONTEXT_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_GET_ORDER_CONTEXT_KO, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_GET_ORDER_CONTEXT_KO, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 提交订单
     *
     * @param param
     */
    public void doLineCreateOrder(final Context context, TmCreateOrderParam param) {
        NetManager.getInstance(context).doCreateOrder(param, new OnResponseListener<TmCreateOrderResultTO>() {
            @Override
            public void onComplete(boolean isOK, TmCreateOrderResultTO result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(ValueConstants.MSG_CREATE_ORDER_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_CREATE_ORDER_KO, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_CREATE_ORDER_KO, errorCode, 0, errorMessage);
            }
        });
    }


    public void doGetLineSkuDateList(final Context context, long item_id) {
        NetManager.getInstance(context).doGetLineSkuDateList(item_id, item_id, new OnResponseListener<SkuListWithStartDate>() {
            @Override
            public void onComplete(boolean isOK, SkuListWithStartDate result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(ValueConstants.MSG_GET_SKU_CONTEXT_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_GET_SKU_CONTEXT_ERROR, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_GET_SKU_CONTEXT_ERROR, errorCode, 0, errorMessage);
            }
        });
    }
	/**
     * 获取商品优惠券列表
     *
     * @param context
     * @param itemId
     */
    public void doGetCouponSellerList(Context context, int pageSize, int pageNo, final long itemId) {
        NetManager.getInstance(context).doQueryItemVoucherList(pageSize, pageNo, itemId, new OnResponseListener<VoucherTemplateResultList>() {
            @Override
            public void onComplete(boolean isOK, VoucherTemplateResultList result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(ValueConstants.COUPON_INFO_SELLER_SUCCESS, 0, 0, result);
                    return;
                }
                sendMessage(ValueConstants.COUPON_INFO_SELLER_FAIL, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.COUPON_INFO_SELLER_FAIL, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 达人咨询商品详情
     * @param id
     * @param context
     */
    public void doGetMasterConsultDetail(final long id, final Context context){
        NetManager.getInstance(context).doGetItemAndSellerInfo(id, new OnResponseListener<MerchantItem>() {
            @Override
            public void onComplete(boolean isOK, MerchantItem result, int errorCode, String errorMsg) {
                if(isOK){
                    sendMessage(ValueConstants.MSG_LINE_DETAIL_OK, 0, 0, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_LINE_DETAIL_ERROR, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_LINE_DETAIL_ERROR, errorCode, 0, errorMessage);
            }
        });
    }
}
