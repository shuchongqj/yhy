package com.quanyan.yhy.net;

import android.content.Context;
import android.os.Handler;

import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.common.ErrorCode;
import com.quanyan.yhy.common.ItemType;
import com.quanyan.yhy.common.QueryType;
import com.quanyan.yhy.net.lsn.OnResponseListener;
import com.smart.sdk.api.request.ApiCode;
import com.smart.sdk.api.request.Items_AddListenCount;
import com.smart.sdk.api.request.Items_GetCityActivityDetail;
import com.smart.sdk.api.request.Items_GetEntityShopDetail;
import com.smart.sdk.api.request.Items_GetGuideDetail;
import com.smart.sdk.api.request.Items_GetGuideTips;
import com.smart.sdk.api.request.Items_GetHotelDetail;
import com.smart.sdk.api.request.Items_GetHotelFacilities;
import com.smart.sdk.api.request.Items_GetHotelFilter;
import com.smart.sdk.api.request.Items_GetHotelList;
import com.smart.sdk.api.request.Items_GetHotelListByCode;
import com.smart.sdk.api.request.Items_GetItem;
import com.smart.sdk.api.request.Items_GetItemAndSellerInfo;
import com.smart.sdk.api.request.Items_GetItemList;
import com.smart.sdk.api.request.Items_GetItemListByCode;
import com.smart.sdk.api.request.Items_GetItems;
import com.smart.sdk.api.request.Items_GetItemsByResourceId;
import com.smart.sdk.api.request.Items_GetItemsForPointMall;
import com.smart.sdk.api.request.Items_GetLineDetail;
import com.smart.sdk.api.request.Items_GetLineDetailByItemId;
import com.smart.sdk.api.request.Items_GetLineFilter;
import com.smart.sdk.api.request.Items_GetLines;
import com.smart.sdk.api.request.Items_GetNearbyGuideList;
import com.smart.sdk.api.request.Items_GetNewScenicDetail;
import com.smart.sdk.api.request.Items_GetPictures;
import com.smart.sdk.api.request.Items_GetRoomList;
import com.smart.sdk.api.request.Items_GetScenicFilter;
import com.smart.sdk.api.request.Items_GetScenicList;
import com.smart.sdk.api.request.Items_GetSugGuideList;
import com.smart.sdk.api.request.Items_KeywordSearch;
import com.smart.sdk.api.request.Items_SearchGuideList;
import com.smart.sdk.api.request.Membercenter_GetTalentDetail;
import com.smart.sdk.api.request.Membercenter_QueryMerchantInfo;
import com.smart.sdk.api.request.Resourcecenter_GetDestCityPageContent;
import com.smart.sdk.api.request.Resourcecenter_GetDestProvincePageContent;
import com.smart.sdk.api.request.Resourcecenter_GetDestinationByCode;
import com.smart.sdk.api.request.Resourcecenter_GetIntroduceInfo;
import com.smart.sdk.api.resp.Api_ITEMS_CodeQueryDTO;
import com.smart.sdk.api.resp.Api_ITEMS_GuideSearchReq;
import com.smart.sdk.api.resp.Api_ITEMS_KeywordSearchDTO;
import com.smart.sdk.api.resp.Api_ITEMS_NearGuideInfo;
import com.smart.sdk.api.resp.Api_ITEMS_QueryFacilitiesDTO;
import com.smart.sdk.api.resp.Api_ITEMS_QueryItemDTO;
import com.smart.sdk.api.resp.Api_ITEMS_QueryLineDTO;
import com.smart.sdk.api.resp.Api_ITEMS_QueryPictureDTO;
import com.smart.sdk.api.resp.Api_ITEMS_QueryPointMallItemDTO;
import com.smart.sdk.api.resp.Api_ITEMS_QueryRoomDTO;
import com.smart.sdk.api.resp.Api_ITEMS_QueryTerm;
import com.smart.sdk.api.resp.Api_ITEMS_QueryTermsDTO;
import com.smart.sdk.api.resp.Api_RESOURCECENTER_DestinationCodeName;
import com.smart.sdk.client.ApiContext;
import com.smart.sdk.client.BaseRequest;
import com.yhy.common.beans.net.model.guide.GuideAttractionFocusInfo;
import com.yhy.common.beans.net.model.guide.GuideScenicInfoList;
import com.yhy.common.beans.net.model.guide.GuideSearchInfo;
import com.yhy.common.beans.net.model.guide.GuideSearchReq;
import com.yhy.common.beans.net.model.guide.GuideTipsInfo;
import com.yhy.common.beans.net.model.guide.NearGuideInfo;
import com.yhy.common.beans.net.model.item.CityActivityDetail;
import com.yhy.common.beans.net.model.item.CodeQueryDTO;
import com.yhy.common.beans.net.model.master.Merchant;
import com.yhy.common.beans.net.model.master.MerchantDesc;
import com.yhy.common.beans.net.model.master.MerchantList;
import com.yhy.common.beans.net.model.master.MerchantQuery;
import com.yhy.common.beans.net.model.master.Qualification;
import com.yhy.common.beans.net.model.master.QueryTermsDTO;
import com.yhy.common.beans.net.model.master.TalentInfo;
import com.yhy.common.beans.net.model.master.TalentInfoList;
import com.yhy.common.beans.net.model.master.TalentQuery;
import com.yhy.common.beans.net.model.rc.DestCityPageContent;
import com.yhy.common.beans.net.model.rc.IntroduceInfo;
import com.yhy.common.beans.net.model.rc.MyTripContent;
import com.yhy.common.beans.net.model.shop.MerchantItem;
import com.yhy.common.beans.net.model.trip.EntityShopDetail;
import com.yhy.common.beans.net.model.trip.HotelDetail;
import com.yhy.common.beans.net.model.trip.HotelFacilityResult;
import com.yhy.common.beans.net.model.trip.HotelFilter;
import com.yhy.common.beans.net.model.trip.HotelInfoResult;
import com.yhy.common.beans.net.model.trip.ItemVO;
import com.yhy.common.beans.net.model.trip.ItemVOResult;
import com.yhy.common.beans.net.model.trip.KeywordSearchDTO;
import com.yhy.common.beans.net.model.trip.LineDetail;
import com.yhy.common.beans.net.model.trip.LineFilter;
import com.yhy.common.beans.net.model.trip.LineInfoResult;
import com.yhy.common.beans.net.model.trip.LineItemDetail;
import com.yhy.common.beans.net.model.trip.PictureVOResult;
import com.yhy.common.beans.net.model.trip.QueryFacilitiesDTO;
import com.yhy.common.beans.net.model.trip.QueryItemDTO;
import com.yhy.common.beans.net.model.trip.QueryLineDTO;
import com.yhy.common.beans.net.model.trip.QueryPictureDTO;
import com.yhy.common.beans.net.model.trip.QueryRoomDTO;
import com.yhy.common.beans.net.model.trip.RoomsResult;
import com.yhy.common.beans.net.model.trip.ScenicDetail;
import com.yhy.common.beans.net.model.trip.ScenicFilter;
import com.yhy.common.beans.net.model.trip.ScenicInfoResult;
import com.yhy.common.beans.net.model.trip.SearchResultList;
import com.yhy.common.beans.net.model.trip.ShortItemsResult;
import com.yhy.common.beans.net.model.user.TravelKa;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.SPUtils;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * 线路、景区、酒店、伴手礼相关接口聚集地
 */
public class TravelNetManager extends BaseNetManager {
    private static TravelNetManager mInstance;

    public TravelNetManager(Context context, ApiContext apiContext, Handler handler) {
        mContext = context;
        mApiContext = apiContext;
        mHandler = handler;
    }

    public synchronized static TravelNetManager getInstance(Context context, ApiContext apiContext, Handler handler) {
        if (mInstance == null) {
            mInstance = new TravelNetManager(context, apiContext, handler);
        }
        return mInstance;
    }

    /**
     * 释放单例
     */
    public synchronized void release(){
        if(mInstance != null){
            mInstance = null;
        }
    }

    /**
     * 根据类型、出发地、目的地分页筛选线路列表
     *
     * @param params
     * @param lsn
     */
    public void doGetTravelList(final QueryLineDTO params, final OnResponseListener<LineInfoResult> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn) || params == null) {
            return;
        }

        final Items_GetLines req = new Items_GetLines(Api_ITEMS_QueryLineDTO.deserialize(params.serialize()));
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                LineInfoResult value = null;
                if (req.getResponse() != null) {
                    try {
                        value = LineInfoResult.deserialize(req.getResponse().serialize());
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
     * 省目的地分页筛选线路列表
     *
     * @param params
     * @param lsn
     */
    public void GetDestProvincePageContent(final QueryLineDTO params, final OnResponseListener<MyTripContent> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn) || params == null) {
            return;
        }

        final Resourcecenter_GetDestProvincePageContent req = new Resourcecenter_GetDestProvincePageContent(params.destCode+"");
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                MyTripContent value = null;
                if (req.getResponse() != null) {
                    try {
                        value = MyTripContent.deserialize(req.getResponse().serialize());
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
     * 获取线路详情
     *
     * @param lineId
     * @param lsn
     */
    public void doGetLinesDetail(final long lineId, final OnResponseListener<LineDetail> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        final Items_GetLineDetail req = new Items_GetLineDetail(lineId);
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                LineDetail value = null;
                if (req.getResponse() != null) {
                    try {
                        value = LineDetail.deserialize(req.getResponse().serialize());
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
     * 通过资源ID，获取资源关联的商品列表
     *
     * @param resourceId
     * @param resourceType
     * @param lsn
     */
    public void doGetItemsByResourceId(final long resourceId,
                                       final String resourceType,
                                       final OnResponseListener<ItemVOResult> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        final Items_GetItemsByResourceId req = new Items_GetItemsByResourceId(resourceId, resourceType);
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                ItemVOResult value = null;
                if (req.getResponse() != null) {
                    try {
                        value = ItemVOResult.deserialize(req.getResponse().serialize());
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
     * 获取商家详情
     *
     * @param merchantId
     * @param type       商家类型 HOTEL:酒店 RESTARUANT:饭店 SCENIC:景区
     * @param lsn
     */
    public void doGetMerchantDetail(final long merchantId, final String type, final OnResponseListener<EntityShopDetail> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        final Items_GetEntityShopDetail req = new Items_GetEntityShopDetail(merchantId, type);
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                EntityShopDetail value = null;
                if (req.getResponse() != null) {
                    try {
                        value = EntityShopDetail.deserialize(req.getResponse().serialize());
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
     * 获取大咖个人主页信息
     *
     * @param bigshotId
     * @param lsn
     */
    public void doGetBigShotDetail(final long bigshotId, final OnResponseListener<TravelKa> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
//        final Membercenter_GetTravelKaDetail req = new Membercenter_GetTravelKaDetail(bigshotId);
//        new RequestExecutor() {
//
//            @Override
//            protected boolean prepare() {
//                return checkSubmitStatus(lsn);
//            }
//
//            @Override
//            protected void postExecute(BaseRequest<?> request) {
//                TravelKa value = null;
//                if (req.getResponse() != null) {
//                    try {
//                        value = TravelKa.deserialize(req.getResponse().serialize());
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
     * 根据城市ID获取目的地市的推荐内容
     * @param cityCode
     * @param lsn
     */
    public void doGetDestinationCityDetail(final String cityCode,List<String> codes, final OnResponseListener<DestCityPageContent> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        final Resourcecenter_GetDestCityPageContent req = new Resourcecenter_GetDestCityPageContent(
                 cityCode,codes);

        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                DestCityPageContent value = null;
                if (req.getResponse() != null) {
                    try {
                        value = DestCityPageContent.deserialize(req.getResponse().serialize());
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
     * 获取省市的介绍页 获取概况 民俗 消费 获取贴士 亮点 必买推荐
     *
     *
     * @param code
     * @param lsn
     */
    public void doGetDestinationIntroductionInfo(final String destCode,
                                                 final String code,
                                                 final OnResponseListener<IntroduceInfo> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        final Resourcecenter_GetIntroduceInfo req = new Resourcecenter_GetIntroduceInfo(destCode,code);

        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                IntroduceInfo value = null;
                if (req.getResponse() != null) {
                    try {
                        value = IntroduceInfo.deserialize(req.getResponse().serialize());
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
     * 获取景区主题和区域
     *
     * @param lsn
     */
    public void doGetScenicSpotThemes(final OnResponseListener<ScenicFilter> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        final Items_GetScenicFilter req = new Items_GetScenicFilter();
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                ScenicFilter value = null;
                if (req.getResponse() != null) {
                    try {
                        value = ScenicFilter.deserialize(req.getResponse().serialize());
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
     * 获取景区列表
     *
     * @param params
     * @param lsn
     */
    public void doGetScenicList(QueryTermsDTO params,
                                final OnResponseListener<ScenicInfoResult> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn) || params == null) {
            return;
        }
        Api_ITEMS_QueryTermsDTO queryScenicDTO = Api_ITEMS_QueryTermsDTO.deserialize(params.serialize());
        final Items_GetScenicList req = new Items_GetScenicList(queryScenicDTO);
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                ScenicInfoResult value = null;
                if (req.getResponse() != null) {
                    try {
                        value = ScenicInfoResult.deserialize(req.getResponse().serialize());
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
     * 获取周边景点列表
     */
    public void doGetArroundScenicList(final String cityCode,
                                        final OnResponseListener<ScenicInfoResult> lsn) throws JSONException{
        if (!checkSubmitStatus(lsn) || StringUtil.isEmpty(cityCode)) {
            return;
        }

        Runnable requestThread = new Runnable() {
            @Override
            public void run() {
                try {
                    Resourcecenter_GetDestinationByCode destReq = new Resourcecenter_GetDestinationByCode(Integer.parseInt(cityCode), ItemType.SCENIC);
                    sendRequest(mContext, mApiContext, destReq);

                    if(destReq.getReturnCode() == ApiCode.SUCCESS && destReq.getResponse() != null && destReq.getResponse().circumCities != null){
                        StringBuffer sb = new StringBuffer();
                        for(Api_RESOURCECENTER_DestinationCodeName value:destReq.getResponse().circumCities){
                            sb.append(value.code).append(",");
                        }
                        if(sb.toString().length() > 0){
                            Api_ITEMS_QueryTermsDTO params = new Api_ITEMS_QueryTermsDTO();
                            List<Api_ITEMS_QueryTerm> terms = new ArrayList<>();

                            Api_ITEMS_QueryTerm term = new Api_ITEMS_QueryTerm();
                            term.type = QueryType.DEST_CITY;
                            term.value = sb.toString().substring(0,sb.toString().length() - 1);
                            terms.add(term);

                            params.queryTerms = terms;
                            params.pageNo = 1;
                            params.pageSize = ValueConstants.PAGESIZE_FIVE;

                            if (!StringUtil.isEmpty(SPUtils.getExtraCurrentLat(mContext))) {
                                params.latitude = Double.parseDouble(SPUtils.getExtraCurrentLat(mContext));
                            }
                            if (!StringUtil.isEmpty(SPUtils.getExtraCurrentLon(mContext))) {
                                params.longitude = Double.parseDouble(SPUtils.getExtraCurrentLon(mContext));
                            }

                            Items_GetScenicList scenicReq = new Items_GetScenicList(params);
                            sendRequest(mContext, mApiContext, scenicReq);

                            ScenicInfoResult value = null;
                            if (scenicReq.getReturnCode() == ApiCode.SUCCESS && scenicReq.getResponse() != null) {
                                try {
                                    value = ScenicInfoResult.deserialize(scenicReq.getResponse().serialize());
                                    if (lsn != null) {
                                        lsn.onComplete(true, value, ErrorCode.STATUS_OK, scenicReq.getReturnMessage());
                                    }
                                } catch (JSONException e) {
                                    if (lsn != null) {
                                        lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                                    }
                                    e.printStackTrace();
                                }
                            } else{
                                lsn.onComplete(false, null, ErrorCode.STATUS_NETWORK_EXCEPTION, scenicReq.getReturnMessage());
                            }
                        }else{
                            if (lsn != null) {
                                lsn.onComplete(false, null, ErrorCode.STATUS_NETWORK_EXCEPTION, destReq.getReturnMessage());
                            }
                        }
                    }else{
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.STATUS_NETWORK_EXCEPTION, destReq.getReturnMessage());
                        }
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
     * 获取酒店列表
     * @param params
     * @param lsn
     * @throws JSONException
     */
    public void doGetHotelList(QueryTermsDTO params,
                               final OnResponseListener<HotelInfoResult> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn) || params == null) {
            return;
        }
        final Items_GetHotelList req = new Items_GetHotelList(Api_ITEMS_QueryTermsDTO.deserialize(params.serialize()));
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                HotelInfoResult value = null;
                if (req.getResponse() != null) {
                    try {
                        value = HotelInfoResult.deserialize(req.getResponse().serialize());
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
     * 获取房型列表
     * @param params
     * @param lsn
     * @throws JSONException
     */
    public void doGetRoomList(QueryRoomDTO params,
                               final OnResponseListener<RoomsResult> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn) || params == null) {
            return;
        }
        final Items_GetRoomList req = new Items_GetRoomList(Api_ITEMS_QueryRoomDTO.deserialize(params.serialize()));
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                RoomsResult value = null;
                if (req.getResponse() != null) {
                    try {
                        value = RoomsResult.deserialize(req.getResponse().serialize());
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
     * 获取酒店设施列表
     * @param params
     * @param lsn
     * @throws JSONException
     */
    public void doGetHotelFacilities(QueryFacilitiesDTO params,
                              final OnResponseListener<HotelFacilityResult> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn) || params == null) {
            return;
        }
        final Items_GetHotelFacilities req = new Items_GetHotelFacilities(Api_ITEMS_QueryFacilitiesDTO.deserialize(params.serialize()));
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                HotelFacilityResult value = null;
                if (req.getResponse() != null) {
                    try {
                        value = HotelFacilityResult.deserialize(req.getResponse().serialize());
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
     * 获取景区详情
     *
     * @param id
     * @param lsn
     */
    @Deprecated
    public void doScenicSpotDetail(final long id,
                                   final OnResponseListener<ScenicDetail> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        final Items_GetNewScenicDetail req = new Items_GetNewScenicDetail(id);
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                ScenicDetail value = null;
                if (req.getResponse() != null) {
                    try {
                        value = ScenicDetail.deserialize(req.getResponse().serialize());
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
     *
     * @param id
     * @param lsn
     */
    public void doScenicDetail(final long id,
                               final OnResponseListener<ScenicDetail> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        final Items_GetNewScenicDetail req = new Items_GetNewScenicDetail(id);
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                ScenicDetail value = null;
                if (req.getResponse() != null) {
                    try {
                        value = ScenicDetail.deserialize(req.getResponse().serialize());
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
     * 根据筛选条件筛选酒店
     *
     * @param params
     * @param lsn
     */
//    public void doGetHotelList(QueryHotelDTO params,
//                               final OnResponseListener<HotelInfoResult> lsn) throws JSONException {
//        if (!checkSubmitStatus(lsn) || params == null) {
//            return;
//        }
//
//        Api_ITEMS_QueryHotelDTO queryScenicDTO = Api_ITEMS_QueryHotelDTO.deserialize(params.serialize());
//        final Items_GetHotels req = new Items_GetHotels(queryScenicDTO);
//        new RequestExecutor() {
//
//            @Override
//            protected boolean prepare() {
//                return checkSubmitStatus(lsn);
//            }
//
//            @Override
//            protected void postExecute(BaseRequest<?> request) {
//                HotelInfoResult value = null;
//                if (req.getResponse() != null) {
//                    try {
//                        value = HotelInfoResult.deserialize(req.getResponse().serialize());
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
//    }

    /**
     * 获取酒店详情
     *
     * @param id
     * @param lsn
     */
    public void doGetHotelDetail(final long id,
                                 final OnResponseListener<HotelDetail> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        final Items_GetHotelDetail req = new Items_GetHotelDetail(id);
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                HotelDetail value = null;
                if (req.getResponse() != null) {
                    try {
                        value = HotelDetail.deserialize(req.getResponse().serialize());
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
     * 获取伴手礼类型
     *
     * @param lsn
     */
    public void doGetHandCeremonyTypes(final OnResponseListener<Boolean> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        Runnable requestThread = new Runnable() {
            @Override
            public void run() {

            }
        };
        NetThreadManager.getInstance().execute(requestThread);
    }

    /**
     * 获取商品详细信息，用于下单页面
     *
     * @param itemId 商品ID
     * @param lsn
     */
    public void doGetItem(long itemId, final OnResponseListener<ItemVO> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        final Items_GetItem req = new Items_GetItem(itemId);
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                ItemVO value = null;
                if (req.getResponse() != null) {
                    try {
                        value = ItemVO.deserialize(req.getResponse().serialize());
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
     * 通过商品类型，获取商品列表
     * @param param
     * @param lsn
     */
    public void doGetItems(QueryItemDTO param, final OnResponseListener<ItemVOResult> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        Api_ITEMS_QueryItemDTO itemDto = Api_ITEMS_QueryItemDTO.deserialize(param.serialize());
        final Items_GetItems req = new Items_GetItems(itemDto);
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                ItemVOResult value = null;
                if (req.getResponse() != null) {
                    try {
                        value = ItemVOResult.deserialize(req.getResponse().serialize());
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
     * 获取线路筛选列表
     * @param lsn
     */
    public void doGetLineFilter(final OnResponseListener<LineFilter> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        final Items_GetLineFilter req = new Items_GetLineFilter();
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                LineFilter value = null;
                if (req.getResponse() != null) {
                    try {
                        value = LineFilter.deserialize(req.getResponse().serialize());
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
     * 获取酒店筛选列表
     * @param lsn
     */
    public void doGetHotelFilter(final OnResponseListener<HotelFilter> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        final Items_GetHotelFilter req = new Items_GetHotelFilter();
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                HotelFilter value = null;
                if (req.getResponse() != null) {
                    try {
                        value = HotelFilter.deserialize(req.getResponse().serialize());
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
     * 根据关键字获取搜索结果
     *
     * @param params
     * @param lsn
     * @throws JSONException
     */
    public void doGetItemskeywordSearch(final KeywordSearchDTO params,
                                        final OnResponseListener<SearchResultList> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn) || params == null) {
            return;
        }

        Api_ITEMS_KeywordSearchDTO queryScenicDTO = Api_ITEMS_KeywordSearchDTO.deserialize(params.serialize());
        final Items_KeywordSearch req = new Items_KeywordSearch(queryScenicDTO);
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                SearchResultList value = null;
                if (req.getResponse() != null) {
                    try {
                        value = SearchResultList.deserialize(req.getResponse().serialize());
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
     * 获取图片列表
     * @param queryPictureDTO
     * @param lsn
     * @throws JSONException
     */
    public void doGetQueryPicture(QueryPictureDTO queryPictureDTO, final OnResponseListener<PictureVOResult> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)||queryPictureDTO == null) {
            return;
        }
        Api_ITEMS_QueryPictureDTO queryScenicDTO = Api_ITEMS_QueryPictureDTO.deserialize(queryPictureDTO.serialize());
        final Items_GetPictures req = new Items_GetPictures(queryScenicDTO);
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                PictureVOResult value = null;
                if (req.getResponse() != null) {
                    try {
                        value = PictureVOResult.deserialize(req.getResponse().serialize());
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
     * 达人页获取达人基本信息
     * @param talentId
     * @param lsn
     * @throws JSONException
     */
    public void doGetTalentDetail(long talentId, final OnResponseListener<TalentInfo> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        final Membercenter_GetTalentDetail req = new Membercenter_GetTalentDetail(talentId);
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                TalentInfo value = null;
                if (req.getResponse() != null) {
                    try {
                        value = TalentInfo.deserialize(req.getResponse().serialize());
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
     * 获取店铺信息
     * @param merchantId
     * @param lsn
     * @throws JSONException
     */
    public void doQueryMerchantInfo(long merchantId, final OnResponseListener<Merchant> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        final Membercenter_QueryMerchantInfo req = new Membercenter_QueryMerchantInfo(merchantId);
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                Merchant value = null;
                if (req.getResponse() != null) {
                    try {
                        value = Merchant.deserialize(req.getResponse().serialize());
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
     * 服务类型获取达人列表
     * @param query
     * @param lsn
     * @throws JSONException
     */
    public void doQueryTalentList(TalentQuery query, final OnResponseListener<TalentInfoList> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn) || query == null) {
            return;
        }
//        final Membercenter_QueryTalentList req = new Membercenter_QueryTalentList(Api_MEMBERCENTER_TalentQuery.deserialize(query.serialize()));
//        new RequestExecutor() {
//
//            @Override
//            protected boolean prepare() {
//                return checkSubmitStatus(lsn);
//            }
//
//            @Override
//            protected void postExecute(BaseRequest<?> request) {
//                TalentInfoList value = null;
//                if (req.getResponse() != null) {
//                    try {
//                        value = TalentInfoList.deserialize(req.getResponse().serialize());
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
//
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
     * 获取店铺列表
     * @param query
     * @param lsn
     * @throws JSONException
     */
    public void doQueryMerchantList(MerchantQuery query, final OnResponseListener<MerchantList> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn) || query == null) {
            return;
        }
//        final Membercenter_QueryMerchantList req = new Membercenter_QueryMerchantList(Api_MEMBERCENTER_MerchantQuery.deserialize(query.serialize()));
//        new RequestExecutor() {
//
//            @Override
//            protected boolean prepare() {
//                return checkSubmitStatus(lsn);
//            }
//
//            @Override
//            protected void postExecute(BaseRequest<?> request) {
//                MerchantList value = null;
//                if (req.getResponse() != null) {
//                    try {
//                        value = MerchantList.deserialize(req.getResponse().serialize());
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
//
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
     * 获取商品列表详情
     * @param param
     * @param lsn
     * @throws JSONException
     */
    public void doGetItemList(QueryTermsDTO param, final OnResponseListener<ShortItemsResult> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn) || param == null) {
            return;
        }
        final Items_GetItemList req = new Items_GetItemList(Api_ITEMS_QueryTermsDTO.deserialize(param.serialize()));
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
     * 获取普通商品详情
     * @param itemId
     * @param lsn
     * @throws JSONException
     */
    public void doGetNormItemDetail(long itemId, final OnResponseListener<MerchantItem> lsn) throws JSONException {
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
     * 根据展位码获取商品列表
     * @param code
     * @param lsn
     * @throws JSONException
     */
    public void doGetItemListByCode(CodeQueryDTO code, final OnResponseListener<ShortItemsResult> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn) || code == null) {
            return;
        }
        final Items_GetItemListByCode req = new Items_GetItemListByCode(Api_ITEMS_CodeQueryDTO.deserialize(code.serialize()));
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
     * 获取同城活动详情
     * @param itemId
     * @param lsn
     * @throws JSONException
     */
    public void doGetCityActivityDetail(long itemId, final OnResponseListener<CityActivityDetail> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn) ) {
            return;
        }
        final Items_GetCityActivityDetail req = new Items_GetCityActivityDetail(itemId);
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                CityActivityDetail value = null;
                if (req.getResponse() != null) {
                    try {
                        value = CityActivityDetail.deserialize(req.getResponse().serialize());
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
     * 获取线路商品详情
     * @param itemId
     * @param lsn
     * @throws JSONException
     */
    public void doGetLineDetailByItemId(long itemId,final OnResponseListener<LineItemDetail> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn) ) {
            return;
        }
        final Items_GetLineDetailByItemId req = new Items_GetLineDetailByItemId(itemId);
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                LineItemDetail value = null;
                if (req.getResponse() != null) {
                    try {
                        value = LineItemDetail.deserialize(req.getResponse().serialize());
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
     * 获取酒店推荐列表
     * @param param
     * @param lsn
     * @throws JSONException
     */
    public void doGetHotelListByCode(CodeQueryDTO param,final OnResponseListener<HotelInfoResult> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn) || param == null) {
            return;
        }
        final Items_GetHotelListByCode req = new Items_GetHotelListByCode(Api_ITEMS_CodeQueryDTO.deserialize(param.serialize()));
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                HotelInfoResult value = null;
                if (req.getResponse() != null) {
                    try {
                        value = HotelInfoResult.deserialize(req.getResponse().serialize());
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
     * 增加收听人数
     * @param guideId
     * @param attractionId
     * @param focusId
     * @param delta
     * @param lsn
     * @throws JSONException
     */
    public void doAddListenCount(long guideId, long attractionId, long focusId, long delta, final OnResponseListener<Boolean> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        final Items_AddListenCount req = new Items_AddListenCount(guideId, delta);
        req.setAttractionId(attractionId);
        req.setFocusId(focusId);
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
     * 获取导览详情
     * @param scenicId
     * @param lsn
     * @throws JSONException
     */
    public void doGetGuideDetail(long scenicId, final OnResponseListener<GuideAttractionFocusInfo> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        final Items_GetGuideDetail req = new Items_GetGuideDetail(scenicId);
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                GuideAttractionFocusInfo value = null;
                if (req.getResponse() != null) {
                    try {
                        value = GuideAttractionFocusInfo.deserialize(req.getResponse().serialize());
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
     * 获取实用锦囊
     * @param guideId
     * @param lsn
     * @throws JSONException
     */
    public void doGetGuideTips(long guideId,final OnResponseListener<GuideTipsInfo> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        final Items_GetGuideTips req = new Items_GetGuideTips(guideId);
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                GuideTipsInfo value = null;
                if (req.getResponse() != null) {
                    try {
                        value = GuideTipsInfo.deserialize(req.getResponse().serialize());
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
     * 获取附近的导览列表
     * @param lsn
     * @throws JSONException
     */
    public void doGetNearbyGuideList(NearGuideInfo nearGuideInfo, final OnResponseListener<GuideScenicInfoList> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        final Items_GetNearbyGuideList req = new Items_GetNearbyGuideList(Api_ITEMS_NearGuideInfo.deserialize(nearGuideInfo.serialize()));
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                GuideScenicInfoList value = null;
                if (req.getResponse() != null) {
                    try {
                        value = GuideScenicInfoList.deserialize(req.getResponse().serialize());
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
     * 获取推荐的导览列表
     * @param lsn
     * @throws JSONException
     */
    public void doGetSugGuideList(final OnResponseListener<GuideScenicInfoList> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        final Items_GetSugGuideList req = new Items_GetSugGuideList();
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                GuideScenicInfoList value = null;
                if (req.getResponse() != null) {
                    try {
                        value = GuideScenicInfoList.deserialize(req.getResponse().serialize());
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
     * 搜索导览列表
     * @param searchReq
     * @param lsn
     * @throws JSONException
     */
    public void doSearchGuideList(GuideSearchReq searchReq, final OnResponseListener<GuideSearchInfo> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        final Items_SearchGuideList req = new Items_SearchGuideList(Api_ITEMS_GuideSearchReq.deserialize(searchReq.serialize()));
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                GuideSearchInfo value = null;
                if (req.getResponse() != null) {
                    try {
                        value = GuideSearchInfo.deserialize(req.getResponse().serialize());
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
     * 查询店铺简介信息
     * @param sellerId
     * @param lsn
     * @throws JSONException
     */
    public void doQueryMerchantDesc(long sellerId, final OnResponseListener<MerchantDesc> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
//        final SellerAdmin_QueryMerchantDesc req = new SellerAdmin_QueryMerchantDesc(sellerId);
//        new RequestExecutor() {
//
//            @Override
//            protected boolean prepare() {
//                return checkSubmitStatus(lsn);
//            }
//
//            @Override
//            protected void postExecute(BaseRequest<?> request) {
//                MerchantDesc value = null;
//                if (req.getResponse() != null) {
//                    try {
//                        value = MerchantDesc.deserialize(req.getResponse().serialize());
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
//
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
     * 查询店铺资质信息
     * @param sellerId
     * @param lsn
     * @throws JSONException
     */
    public void doQueryMerchantQualification(long sellerId, final OnResponseListener<Qualification> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
//        final SellerAdmin_QueryMerchantQualification req = new SellerAdmin_QueryMerchantQualification(sellerId);
//        new RequestExecutor() {
//
//            @Override
//            protected boolean prepare() {
//                return checkSubmitStatus(lsn);
//            }
//
//            @Override
//            protected void postExecute(BaseRequest<?> request) {
//                Qualification value = null;
//                if (req.getResponse() != null) {
//                    try {
//                        value = Qualification.deserialize(req.getResponse().serialize());
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
//
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
     * 运动页面分页获取商品列表
     * @param code
     * @param lsn
     * @throws JSONException
     */
    public void doGetItemListForPointMall(CodeQueryDTO code, final OnResponseListener<ShortItemsResult> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn) || code == null) {
            return;
        }
//        final Items_GetItemListByCode req = new Items_GetItemListByCode(Api_ITEMS_CodeQueryDTO.deserialize(code.serialize()));
        final Items_GetItemsForPointMall req = new Items_GetItemsForPointMall(Api_ITEMS_QueryPointMallItemDTO.deserialize(code.serialize()));
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
}

