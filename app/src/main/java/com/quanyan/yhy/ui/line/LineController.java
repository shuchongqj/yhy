package com.quanyan.yhy.ui.line;

import android.content.Context;
import android.os.Handler;

import com.quanyan.base.BaseController;
import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.common.ItemType;
import com.quanyan.yhy.common.MerchantType;
import com.quanyan.yhy.common.QueryType;
import com.quanyan.yhy.common.ResourceType;
import com.quanyan.yhy.net.NetManager;
import com.quanyan.yhy.net.lsn.OnResponseListener;
import com.quanyan.yhy.ui.nineclub.bean.JourneyDays;
import com.yhy.common.beans.net.model.common.BoothList;
import com.yhy.common.beans.net.model.master.QueryTerm;
import com.yhy.common.beans.net.model.master.QueryTermsDTO;
import com.yhy.common.beans.net.model.trip.ShortItemsResult;
import com.yhy.common.constants.ValueConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:AboutAndFeedController
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:zhaoxiaopo
 * Date:16/3/9
 * Time:15:59
 * Version 1.0
 */
public class LineController extends BaseController {

    public LineController(Context context, Handler handler) {
        super(context, handler);
    }

    public void fetchLineCommmentList() {
        sendMessage(1);
    }

    /**
     * 获取跟团游首页的资源位
     */
    public void doGetPackageTourResourceList(final Context context) {
        List<String> codes = new ArrayList<>();
        codes.add(ResourceType.QUANYAN_GROUP_TRAVELL_AD_ROTATION);
        codes.add(ResourceType.RESOURCE_CODE_G);

        NetManager.getInstance(context).doGetMultiBooths(codes, new OnResponseListener<BoothList>() {

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_GET_PACKAGE_TOUR_HOME_PAGE_RESOURCE_LIST_KO, errorCode, 0, errorMessage);
            }

            @Override
            public void onComplete(boolean isOK, BoothList result, int errorCode, String errorMsg) {
                if (isOK) {
                    if (result == null) {
                        result = new BoothList();
                    }
                    sendMessage(ValueConstants.MSG_GET_PACKAGE_TOUR_HOME_PAGE_RESOURCE_LIST_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_GET_PACKAGE_TOUR_HOME_PAGE_RESOURCE_LIST_KO, errorCode, 0, errorMsg);
            }
        });
    }

    /**
     * 获取自由行首页的资源位
     */
    public void doGetFreeWalkResourceList(final Context context) {
        List<String> codes = new ArrayList<>();
        codes.add(ResourceType.QUANYAN_FREE_TRAVELL_AD_ROTATION);
        codes.add(ResourceType.RESOURCE_CODE_I);

        NetManager.getInstance(context).doGetMultiBooths(codes, new OnResponseListener<BoothList>() {

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_GET_FREE_WALK_HOME_PAGE_RESOURCE_LIST_KO, errorCode, 0, errorMessage);
            }

            @Override
            public void onComplete(boolean isOK, BoothList result, int errorCode, String errorMsg) {
                if (isOK) {
                    if (result == null) {
                        result = new BoothList();
                    }
                    sendMessage(ValueConstants.MSG_GET_FREE_WALK_HOME_PAGE_RESOURCE_LIST_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_GET_FREE_WALK_HOME_PAGE_RESOURCE_LIST_KO, errorCode, 0, errorMsg);
            }
        });
    }

    /**
     * 获取同城活动首页资源位
     */
    public void doGetCityActivityResourceList(final Context context) {
        List<String> codes = new ArrayList<>();
        codes.add(ResourceType.QUANYAN_ACTIVITY_AD_ROTATION);
        codes.add(ResourceType.RESOURCE_CODE_K);

        NetManager.getInstance(context).doGetMultiBooths(codes, new OnResponseListener<BoothList>() {

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_GET_CITY_ACTIVITY_HOME_PAGE_RESOURCE_LIST_KO, errorCode, 0, errorMessage);
            }

            @Override
            public void onComplete(boolean isOK, BoothList result, int errorCode, String errorMsg) {
                if (isOK) {
                    if (result == null) {
                        result = new BoothList();
                    }
                    sendMessage(ValueConstants.MSG_GET_CITY_ACTIVITY_HOME_PAGE_RESOURCE_LIST_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_GET_CITY_ACTIVITY_HOME_PAGE_RESOURCE_LIST_KO, errorCode, 0, errorMsg);
            }
        });
    }

    /**
     * 获取周边玩乐首页资源位
     */
    public void doGetArroundFunResourceList(final Context context) {
        List<String> codes = new ArrayList<>();
        codes.add(ResourceType.QUANYAN_NEARBY_ENJOY_AD_ROTATION);
        codes.add(ResourceType.RESOURCE_CODE_N);
        codes.add(ResourceType.RESOURCE_CODE_O);

        NetManager.getInstance(context).doGetMultiBooths(codes, new OnResponseListener<BoothList>() {

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_GET_ARROUND_FUN_HOME_PAGE_RESOURCE_LIST_KO, errorCode, 0, errorMessage);
            }

            @Override
            public void onComplete(boolean isOK, BoothList result, int errorCode, String errorMsg) {
                if (isOK) {
                    if (result == null) {
                        result = new BoothList();
                    }
                    sendMessage(ValueConstants.MSG_GET_ARROUND_FUN_HOME_PAGE_RESOURCE_LIST_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_GET_ARROUND_FUN_HOME_PAGE_RESOURCE_LIST_KO, errorCode, 0, errorMsg);
            }
        });
    }

    /**
     * 线路首页推荐列表数据
     *
     * @param pageIndex
     * @param pageSize
     */
    public void doGetLineRecomandList(final Context context, double lat, double lon, String type, String cityCode, final int pageIndex, final int pageSize) {
        QueryTermsDTO param = new QueryTermsDTO();
        param.pageSize = pageSize;
        param.pageNo = pageIndex;
        if(lat > 0) {
            param.latitude = lat;
        }
        if(lon > 0) {
            param.longitude = lon;
        }
        List<QueryTerm> terms = new ArrayList<>();
        if (ItemType.ARROUND_FUN.equals(type)) {
            QueryTerm t1 = new QueryTerm();
            t1.type = QueryType.ITEM_TYPE;
            t1.value = ItemType.FREE_LINE + "," +ItemType.TOUR_LINE + "," + ItemType.FREE_LINE_ABOARD + "," +ItemType.TOUR_LINE_ABOARD;
            terms.add(t1);

            JourneyDays days = new JourneyDays();
            days.setMinDays(0);
            days.setMaxDays(2);
            QueryTerm t3 = new QueryTerm();
            t3.type = QueryType.DAYS;
            t3.value = days.toJsonString();
            terms.add(t3);
        } else {
            QueryTerm t1 = new QueryTerm();
            t1.type = QueryType.ITEM_TYPE;
            String itemType;
            if (ItemType.TOUR_LINE.equals(type)) {
                itemType = type + "," + ItemType.TOUR_LINE_ABOARD;
            } else if (ItemType.FREE_LINE.equals(type)) {
                itemType = type + "," + ItemType.FREE_LINE_ABOARD;
            }else{
                itemType = type;
            }
            t1.value = itemType;
            terms.add(t1);
        }
        
        if (!StringUtil.isEmpty(cityCode)) {
            QueryTerm t2 = new QueryTerm();
            if (ItemType.TOUR_LINE.equals(type) || ItemType.FREE_LINE.equals(type) ||
                    ItemType.TOUR_LINE_ABOARD.equals(type) || ItemType.FREE_LINE_ABOARD.equals(type)) {
                t2.type = QueryType.START_CITY;
            } else {
                t2.type = QueryType.DEST_CITY;
            }
            t2.value = cityCode;
            terms.add(t2);
        }

        QueryTerm ts = new QueryTerm();
        ts.type = QueryType.SELLER_TYPE;
        ts.value = MerchantType.MERCHANT;
        terms.add(ts);

        param.queryTerms = terms;

        NetManager.getInstance(context).doGetItemList(param, new OnResponseListener<ShortItemsResult>() {
            @Override
            public void onComplete(boolean isOK, ShortItemsResult result, int errorCode, String errorMsg) {
                if (isOK) {
                    if (result == null) {
                        result = new ShortItemsResult();
                    }
                    sendMessage(ValueConstants.MSG_LINE_HOME_PAGE_RECOMMAND_LIST_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_LINE_HOME_PAGE_RECOMMAND_LIST_KO, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_LINE_HOME_PAGE_RECOMMAND_LIST_KO, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 获取线路筛选列表
     */
    public void doGetLineFilter(final Context context, final String type) {
        NetManager.getInstance(context).doGetQueryFilter(ItemType.getFilterTypeByItemType(type), new OnResponseListener<QueryTerm>() {
            @Override
            public void onComplete(boolean isOK, QueryTerm result, int errorCode, String errorMsg) {
                if (isOK) {
                    if (result == null) {
                        result = new QueryTerm();
                    }
                    sendMessage(ValueConstants.MSG_GET_LINE_FILTER_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_GET_LINE_FILTER_ERROR, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_GET_LINE_FILTER_ERROR, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 达人商品搜索
     *
     * @param params
     */
    public void doSearchLineListSearch(final Context context, QueryTermsDTO params) {
        NetManager.getInstance(context).doGetItemList(params, new OnResponseListener<ShortItemsResult>() {
            @Override
            public void onComplete(boolean isOK, ShortItemsResult result, int errorCode, String errorMsg) {
                if (isOK) {
                    if (result == null) {
                        result = new ShortItemsResult();
                    }
                    sendMessage(ValueConstants.MSG_MASTER_SEARCH_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_MASTER_SEARCH_KO, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_MASTER_SEARCH_KO, errorCode, 0, errorMessage);
            }
        });
    }

}
