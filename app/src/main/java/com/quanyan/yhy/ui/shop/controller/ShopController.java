package com.quanyan.yhy.ui.shop.controller;

import android.content.Context;
import android.os.Handler;

import com.quanyan.base.BaseController;
import com.quanyan.yhy.net.NetManager;
import com.quanyan.yhy.net.lsn.OnResponseListener;
import com.yhy.common.beans.net.model.master.Merchant;
import com.yhy.common.beans.net.model.master.MerchantDesc;
import com.yhy.common.beans.net.model.master.Qualification;
import com.yhy.common.beans.net.model.master.QueryTermsDTO;
import com.yhy.common.beans.net.model.tm.LgExpressInfo;
import com.yhy.common.beans.net.model.tm.PackageDetailResult;
import com.yhy.common.beans.net.model.tm.VoucherTemplateResultList;
import com.yhy.common.beans.net.model.trip.ShortItemsResult;
import com.yhy.common.constants.ValueConstants;

/**
 * Created with Android Studio.
 * Title:ShopController
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:徐学东
 * Date:16/3/2
 * Time:下午3:15
 * Version 1.0
 */
public class ShopController extends BaseController {
    public ShopController(Context context, Handler handler) {
        super(context, handler);
    }

    /**
     * 获取店铺详情
     */
    public void doGetShopDetailInfo(final Context context, long shoppid) {
        NetManager.getInstance(context).doQueryMerchantInfo(shoppid, new OnResponseListener<Merchant>() {
            @Override
            public void onComplete(boolean isOK, Merchant result, int errorCode, String errorMsg) {
                if (isOK) {
                    if (result == null) {
                        result = new Merchant();
                    }
                    sendMessage(ValueConstants.MSG_GET_SHOP_DETAIL_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_GET_SHOP_DETAIL_KO, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_GET_SHOP_DETAIL_KO, errorCode, 0, errorMessage);

            }
        });
    }

    /**
     * 分页获取店铺商品列表
     *
     * @param param
     */
    public void doGetShopProductsList(final Context context, QueryTermsDTO param) {
        NetManager.getInstance(context).doGetItemList(param, new OnResponseListener<ShortItemsResult>() {

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_GET_SHOP_PRODUCTS_LIST_KO, errorCode, 0, errorMessage);
            }

            @Override
            public void onComplete(boolean isOK, ShortItemsResult result, int errorCode, String errorMsg) {
                if (isOK) {
                    if (result == null) {
                        result = new ShortItemsResult();
                    }
                    sendMessage(ValueConstants.MSG_GET_SHOP_PRODUCTS_LIST_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_GET_SHOP_PRODUCTS_LIST_KO, errorCode, 0, errorMsg);
            }
        });
    }

    /**
     * 获取店铺的优惠券列表
     *
     * @param sellerId
     */
    public void doQuerySellerVoucherList(final Context context, long sellerId) {
        NetManager.getInstance(context).doQuerySellerVoucherList(sellerId, new OnResponseListener<VoucherTemplateResultList>() {

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_GET_SHOP_COUPON_LIST_KO, errorCode, 0, errorMessage);
            }

            @Override
            public void onComplete(boolean isOK, VoucherTemplateResultList result, int errorCode, String errorMsg) {
                if (isOK) {
                    if (result == null) {
                        result = new VoucherTemplateResultList();
                    }
                    sendMessage(ValueConstants.MSG_GET_SHOP_COUPON_LIST_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_GET_SHOP_COUPON_LIST_KO, errorCode, 0, errorMsg);
            }
        });
    }

    /**
     * 领取优惠券
     *
     * @param context
     * @param voucherTemplateId
     */
    public void doGenerateVoucher(final Context context, long voucherTemplateId) {
        NetManager.getInstance(context).doGenerateVoucher(voucherTemplateId, new OnResponseListener<Boolean>() {

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_REC_SHOP_COUPON_KO, errorCode, 0, errorMessage);
            }

            @Override
            public void onComplete(boolean isOK, Boolean result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(ValueConstants.MSG_REC_SHOP_COUPON_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_REC_SHOP_COUPON_KO, errorCode, 0, errorMsg);
            }
        });
    }


    /**
     * 查询店铺简介信息
     *
     * @param context
     * @param sellerId
     */
    public void doQueryMerchantDesc(final Context context, long sellerId) {
        NetManager.getInstance(context).doQueryMerchantDesc(sellerId, new OnResponseListener<MerchantDesc>() {

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_QUERY_SHOP_DESC_KO, errorCode, 0, errorMessage);
            }

            @Override
            public void onComplete(boolean isOK, MerchantDesc result, int errorCode, String errorMsg) {
                if (isOK) {
                    if (result == null) {
                        result = new MerchantDesc();
                    }
                    sendMessage(ValueConstants.MSG_QUERY_SHOP_DESC_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_QUERY_SHOP_DESC_KO, errorCode, 0, errorMsg);
            }
        });
    }

    /**
     * 查询店铺资质信息
     *
     * @param context
     * @param sellerId
     */
    public void doQueryMerchantQualification(final Context context, long sellerId) {
        NetManager.getInstance(context).doQueryMerchantQualification(sellerId, new OnResponseListener<Qualification>() {

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_QUERY_SHOP_CARD_KO, errorCode, 0, errorMessage);
            }

            @Override
            public void onComplete(boolean isOK, Qualification result, int errorCode, String errorMsg) {
                if (isOK) {
                    if (result == null) {
                        result = new Qualification();
                    }
                    sendMessage(ValueConstants.MSG_QUERY_SHOP_CARD_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_QUERY_SHOP_CARD_KO, errorCode, 0, errorMsg);
            }
        });
    }

    /**
     * 获取物流详情
     *
     * @param context
     * @param logisticsId
     */
    public void doGetLogisticsList(final Context context, long logisticsId) {
//        LgExpressInfo result = null;
//        if (result == null) {
//            result = new LgExpressInfo();
//        }
//
//        List<LgExpressLineInfo> list = new ArrayList<LgExpressLineInfo>();
//        LgExpressLineInfo lgOrder = new LgExpressLineInfo();
//        lgOrder.context = "【北京市】在北京朝阳区望京一公司进行签收扫描，快件已被 已签收 签收，感谢使用韵达快递，期待再次为您服务";
//        lgOrder.time = "16年9月8日9:10:09";
//        list.add(lgOrder);
//        list.add(lgOrder);
//        list.add(lgOrder);
//        list.add(lgOrder);
//        list.add(lgOrder);
//
//        result.expressLineInfoList = list;
//        sendMessage(ValueConstants.MSG_GET_LOGISTICS_LIST_OK, result);

        NetManager.getInstance(context).doGetLogisticsList(logisticsId, new OnResponseListener<LgExpressInfo>() {

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_GET_LOGISTICS_LIST_KO, errorCode, 0, errorMessage);
            }

            @Override
            public void onComplete(boolean isOK, LgExpressInfo result, int errorCode, String errorMsg) {
                if (isOK) {
                    if (result == null) {
                        result = new LgExpressInfo();
                    }
                    sendMessage(ValueConstants.MSG_GET_LOGISTICS_LIST_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_GET_LOGISTICS_LIST_KO, errorCode, 0, errorMsg);
            }
        });
    }
    /**
     * 获取物流包裹详情
     *
     * @param context
     * @param orderId
     */
    public void doQueryPackageBuyOrder(final Context context, long orderId){
        NetManager.getInstance(context).doQueryPackageBuyOrder(orderId, new OnResponseListener<PackageDetailResult>() {
            @Override
            public void onComplete(boolean isOK, PackageDetailResult result, int errorCode, String errorMsg) {
                if (isOK) {
                    if (result == null) {
                        result = new PackageDetailResult();
                    }
                    sendMessage(ValueConstants.MSG_GET_ORDER_PACKET_LIST_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_GET_ORDER_PACKET_LIST_KO, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_GET_ORDER_PACKET_LIST_KO, errorCode, 0, errorMessage);
            }
        });

    }
}
