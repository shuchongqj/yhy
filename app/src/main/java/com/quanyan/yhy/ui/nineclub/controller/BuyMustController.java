package com.quanyan.yhy.ui.nineclub.controller;

import android.content.Context;
import android.os.Handler;

import com.quanyan.base.BaseController;
import com.quanyan.yhy.net.NetManager;
import com.quanyan.yhy.net.lsn.OnResponseListener;
import com.yhy.common.beans.net.model.club.PageInfo;
import com.yhy.common.beans.net.model.common.Booth;
import com.yhy.common.beans.net.model.master.Merchant;
import com.yhy.common.beans.net.model.master.MerchantList;
import com.yhy.common.beans.net.model.master.MerchantQuery;
import com.yhy.common.beans.net.model.master.QueryTermsDTO;
import com.yhy.common.beans.net.model.rc.CityList;
import com.yhy.common.beans.net.model.trip.ShortItemsResult;
import com.yhy.common.beans.net.model.user.DestinationList;
import com.yhy.common.constants.ValueConstants;

/**
 * Created with Android Studio.
 * Title:BuyMustController
 * Description:访问服务器接口类
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:baojie
 * Date:2016-3-14
 * Time:19:10
 * Version 1.0
 */

public class BuyMustController extends BaseController{

    public BuyMustController(Context context, Handler handler) {
        super(context, handler);
    }



    /**
     * 必买列表数据
     * @param param
     */
    public void doGetBuyMustList(final Context context, QueryTermsDTO param){
        NetManager.getInstance(context).doGetItemList(param, new OnResponseListener<ShortItemsResult>() {
            @Override
            public void onComplete(boolean isOK, ShortItemsResult result, int errorCode, String errorMsg) {
                if (isOK) {
                    if (result == null) {
                        result = new ShortItemsResult();
                    }
                    sendMessage(ValueConstants.MSG_BUYMUST_LIST_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_BUYMUST_LIST_KO, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_BUYMUST_LIST_KO, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 9club列表数据
     * @param boothCode
     * @param pageInfo
     */
    public void doGetNineClubList(final Context context, String boothCode, PageInfo pageInfo){
        NetManager.getInstance(context).doGetPageBooth(boothCode, pageInfo, new OnResponseListener<Booth>() {
            @Override
            public void onComplete(boolean isOK, Booth result, int errorCode, String errorMsg) {
                if (isOK) {
                    if (result == null) {
                        result = new Booth();
                    }
                    sendMessage(ValueConstants.MSG_NINECLUB_LIST_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_NINECLUB_LIST_KO, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_NINECLUB_LIST_KO, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 美食列表数据
     * @param query
     */
    public void doGetEatGreatList(final Context context, MerchantQuery query){
        NetManager.getInstance(context).doQueryMerchantList(query, new OnResponseListener<MerchantList>() {
            @Override
            public void onComplete(boolean isOK, MerchantList result, int errorCode, String errorMsg) {
                if (isOK) {
                    if (result == null) {
                        result = new MerchantList();
                    }
                    sendMessage(ValueConstants.MSG_EATGREAT_LIST_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_EATGREAT_LIST_KO, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_EATGREAT_LIST_KO, errorCode, 0, errorMessage);
            }
        });
    }


    /**
     * 美食店铺详情
     * @param eatId
     */
    public void doGetEatGreatDetail(final Context context, long eatId){
        NetManager.getInstance(context).doQueryMerchantInfo(eatId, new OnResponseListener<Merchant>() {

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_EATGREAT_DETAIL_KO, errorCode, 0, errorMessage);
            }

            @Override
            public void onComplete(boolean isOK, Merchant result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(ValueConstants.MSG_EATGREAT_DETAIL_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_EATGREAT_DETAIL_KO, errorCode, 0, errorMsg);
            }
        });
    }

    /**
     * 获取目的地城市列表
     */
    public void doGetDestListNew(final Context context){
        NetManager.getInstance(context).doGetDestListNew(new OnResponseListener<CityList>() {

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_DESTCITY_NEW_KO, errorCode, 0, errorMessage);
            }

            @Override
            public void onComplete(boolean isOK, CityList result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(ValueConstants.MSG_DESTCITY_NEW_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_DESTCITY_NEW_KO, errorCode, 0, errorMsg);
            }
        });
    }

    //条件查询目的地
    public void doQueryDestinationTree(Context context,String type){
        NetManager.getInstance(context).doQueryDestinationTree(type, new OnResponseListener<DestinationList>() {

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_GET_ABROAD_DESTINATION_KO, errorCode, 0, errorMessage);
            }

            @Override
            public void onComplete(boolean isOK, DestinationList result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(ValueConstants.MSG_GET_ABROAD_DESTINATION_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_GET_ABROAD_DESTINATION_KO, errorCode, 0, errorMsg);
            }
        });
    }

    /**
     * 获取目的地头部4个球推荐信息
     * @param boothCode
     */
    public void doGetBoothDestTop(final Context context, String boothCode){
        NetManager.getInstance(context).doGetBooth(boothCode, new OnResponseListener<Booth>() {

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_DESTCITY_TOP_NEW_KO, errorCode, 0, errorMessage);
            }

            @Override
            public void onComplete(boolean isOK, Booth result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(ValueConstants.MSG_DESTCITY_TOP_NEW_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_DESTCITY_TOP_NEW_KO, errorCode, 0, errorMsg);
            }
        });
    }

}
