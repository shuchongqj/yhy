package com.quanyan.yhy.ui.master.controller;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.view.View;

import com.quanyan.base.BaseController;
import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.FilterType;
import com.quanyan.yhy.common.ResourceType;
import com.quanyan.yhy.net.NetManager;
import com.quanyan.yhy.net.lsn.OnResponseListener;
import com.quanyan.yhy.ui.base.utils.DialogUtil;
import com.yhy.common.beans.net.model.common.Booth;
import com.yhy.common.beans.net.model.common.BoothList;
import com.yhy.common.beans.net.model.item.BoothItemsResult;
import com.yhy.common.beans.net.model.item.CodeQueryDTO;
import com.yhy.common.beans.net.model.master.QueryTerm;
import com.yhy.common.beans.net.model.master.QueryTermsDTO;
import com.yhy.common.beans.net.model.master.TalentInfo;
import com.yhy.common.beans.net.model.master.TalentInfoList;
import com.yhy.common.beans.net.model.master.TalentQuery;
import com.yhy.common.beans.net.model.trip.ShortItemsResult;
import com.yhy.common.beans.net.model.user.DestinationList;
import com.yhy.common.beans.net.model.user.FollowerPageListResult;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:ShopHomePageActivity
 * Description:达人数据控制器
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:徐学东
 * Date:16/3/8
 * Time:下午1:18
 * Version 1.0
 */
public class MasterController extends BaseController {

    public MasterController(Context context, Handler handler) {
        super(context, handler);
    }

    /**
     * 获取线路筛选列表
     */
    public void getFilterList(final Context context) {
        NetManager.getInstance(context).doGetQueryFilter(FilterType.QUANYAN_MASTER_QUERY_TERN, new OnResponseListener<QueryTerm>() {
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
     * 获取达人列表
     *
     * @param params
     */
    public void doSearchMasterList(final Context context, TalentQuery params) {
        NetManager.getInstance(context).doQueryTalentList(params, new OnResponseListener<TalentInfoList>() {
            @Override
            public void onComplete(boolean isOK, TalentInfoList result, int errorCode, String errorMsg) {
                if (isOK) {
                    if (result == null) {
                        result = new TalentInfoList();
                    }
                    sendMessage(ValueConstants.MSG_GET_MASTER_LIST_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_GET_MASTER_LIST_KO, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_GET_MASTER_LIST_KO, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 获取达人详情
     *
     * @param id
     */
    public void getMasterDetail(final Context context, long id) {
        NetManager.getInstance(context).doGetTalentDetail(id, new OnResponseListener<TalentInfo>() {
            @Override
            public void onComplete(boolean isOK, TalentInfo result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(ValueConstants.MSG_GET_MASTER_DETAIL_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_GET_MASTER_DETAIL_KO, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_GET_MASTER_DETAIL_KO, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 获取热门搜索词
     */
    public void doGetHotSearchList(final Context context, String code) {
        NetManager.getInstance(context).doGetBooth(code, new OnResponseListener<Booth>() {
            @Override
            public void onComplete(boolean isOK, Booth result, int errorCode, String errorMsg) {
                if (isOK) {
                    if (result == null) {
                        result = new Booth();
                    }
                    sendMessage(ValueConstants.MSG_MASTER_HOT_SEARCH_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_MASTER_HOT_SEARCH_KO, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_MASTER_HOT_SEARCH_KO, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 达人搜索
     */
    public void doMasterListSearch(final Context context, TalentQuery query) {
        NetManager.getInstance(context).doQueryTalentList(query, new OnResponseListener<TalentInfoList>() {
            @Override
            public void onComplete(boolean isOK, TalentInfoList result, int errorCode, String errorMsg) {
                if (isOK) {
                    if (result == null) {
                        result = new TalentInfoList();
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

    /**
     * 达人商品搜索
     *
     * @param params
     */
    public void doMasterLineListSearch(final Context context, QueryTermsDTO params) {
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

    /**
     * 达人店铺职工商品列表数据
     *
     * @param param
     */
    public void doGetMasterShopList(final Context context, QueryTermsDTO param) {
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
     * 获取关注列表
     */
    public void doGetAttentionList(Context context, long userId, int pageIndex) {
        NetManager.getInstance(context).doGetFollowerList(userId, pageIndex, 20, new OnResponseListener<FollowerPageListResult>() {
            @Override
            public void onComplete(boolean isOK, FollowerPageListResult result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(ValueConstants.MSG_GET_ATTENTION_LIST_OK, result);
                } else {
                    sendMessage(ValueConstants.MSG_GET_ATTENTION_LIST_KO, errorCode, 0, errorMsg);
                }
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_GET_ATTENTION_LIST_KO, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 获取粉丝列表
     *
     * @param context
     * @param userId
     * @param pageIndex
     */
    public void doGetFansList(Context context, long userId, int pageIndex) {
        NetManager.getInstance(context).doGetFansList(userId, pageIndex, 20, new OnResponseListener<FollowerPageListResult>() {
            @Override
            public void onComplete(boolean isOK, FollowerPageListResult result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(ValueConstants.MSG_GET_ATTENTION_LIST_OK, result);
                } else {
                    sendMessage(ValueConstants.MSG_GET_ATTENTION_LIST_KO, errorCode, 0, errorMsg);
                }
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_GET_ATTENTION_LIST_KO, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 关注
     *
     * @param userId
     */
    public void doAttention(Context context, final long userId) {
        NetManager.getInstance(context).doAddFollower(userId, new OnResponseListener<Boolean>() {
            @Override
            public void onComplete(boolean isOK, Boolean result, int errorCode, String errorMsg) {
                if (isOK && result) {
                    sendMessage(ValueConstants.MSG_ATTENTION_OK, userId);
                } else {
                    sendMessage(ValueConstants.MSG_ATTENTION_KO, errorCode, 0, errorMsg);
                }
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_ATTENTION_KO, errorCode, 0, errorMessage);
            }
        });
    }

    Dialog cancelAttentionDialog;

    /**
     * 取消关注
     */
    public void cancelAttention(Context context, long id, DialogInterface.OnDismissListener listener) {
        final Context mContext = context;
        final long userId = id;
        cancelAttentionDialog = DialogUtil.showMessageDialog(context, null, context.getString(R.string.lable_cancel_attention_message), context.getString(R.string.affirm), context.getString(R.string.cancel), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelAttentionDialog.dismiss();
                NetManager.getInstance(mContext).doRemoveFollower(userId, new OnResponseListener<Boolean>() {
                    @Override
                    public void onComplete(boolean isOK, Boolean result, int errorCode, String errorMessage) {
                        if (isOK && result) {
                            sendMessage(ValueConstants.MSG_CANCEL_ATTENTION_OK, userId);
                        } else {
                            sendMessage(ValueConstants.MSG_CANCEL_ATTENTION_KO, errorCode, 0, errorMessage);
                        }
                    }

                    @Override
                    public void onInternError(int errorCode, String errorMessage) {
                        sendMessage(ValueConstants.MSG_CANCEL_ATTENTION_KO, errorCode, 0, errorMessage);
                    }
                });
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelAttentionDialog.dismiss();
            }
        });
        cancelAttentionDialog.setOnDismissListener(listener);
        cancelAttentionDialog.show();
    }

    public void cancelAttention(Context context, long id) {
        cancelAttention(context, id, null);
    }

    /**
     * 获取达人咨询商品列表
     */
    public void getConsultItemList(Context context, QueryTermsDTO userId) {
        NetManager.getInstance(context).doGetConsultItemList(userId, new OnResponseListener<ShortItemsResult>() {
            @Override
            public void onComplete(boolean isOK, ShortItemsResult result, int errorCode, String errorMsg) {
                if (isOK) {
                    if (result == null) {
                        result = new ShortItemsResult();
                    }
                    sendMessage(ValueConstants.MSG_INTEGRALMALL_LIST_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_INTEGRALMALL_LIST_KO, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_INTEGRALMALL_LIST_KO, errorCode, 0, errorMessage);
            }
        });
    }


    /**
     * 获取达人咨询筛选列表
     */
    public void doGetHotelFilter(final Context context, final String type) {
        NetManager.getInstance(context).doGetQueryFilter(type, new OnResponseListener<QueryTerm>() {
            @Override
            public void onComplete(boolean isOK, QueryTerm result, int errorCode, String errorMsg) {
                if (isOK) {
                    if (result == null) {
                        result = new QueryTerm();
                    }
                    sendMessage(ValueConstants.MSG_HOTEL_FILTER_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_HOTEL_FILTER_KO, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_HOTEL_FILTER_KO, errorCode, 0, errorMessage);
            }
        });
    }

    //条件查询目的地
    public void doQueryDestinationTree(final Context context, String type) {
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
     * 获取达人线路列表
     *
     * @param params
     */
    public void doLineMasterList(final Context context, QueryTermsDTO params) {
        NetManager.getInstance(context).doGetItemList(params, new OnResponseListener<ShortItemsResult>() {
            @Override
            public void onComplete(boolean isOK, ShortItemsResult result, int errorCode, String errorMsg) {
                if (isOK) {
                    if (result == null) {
                        result = new ShortItemsResult();
                    }
                    sendMessage(ValueConstants.MSG_GET_MASTER_PRODUCTS_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_GET_MASTER_PRODUCTS_KO, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_GET_MASTER_PRODUCTS_KO, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 获取达人线路首页配置位
     */
    public void doGetLineMasterBooth(final Context context) {
        NetManager.getInstance(context).doGetBooth(ResourceType.QUANYAN_LINE_TALENT_LIST_HEADER, new OnResponseListener<Booth>() {

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_GET_MASTER_PRODUCTS_AD_KO, errorCode, 0, errorMessage);
            }

            @Override
            public void onComplete(boolean isOK, Booth result, int errorCode, String errorMsg) {
                if (isOK) {
                    if (result == null) {
                        result = new Booth();
                    }
                    sendMessage(ValueConstants.MSG_GET_MASTER_PRODUCTS_AD_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_GET_MASTER_PRODUCTS_AD_KO, errorCode, 0, errorMsg);
            }
        });
    }

    /**
     * 获取达人咨询首页配置数据
     * @param context
     */
    public void doGetMasterConsultHomeData(final Context context) {
        List<String> codes = new ArrayList<>();
        codes.add(ResourceType.QUANYAN_CONSULTING_HOME_AD);
        NetManager.getInstance(context).doGetMultiBooths(codes, new OnResponseListener<BoothList>() {

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_GET_FIRST_PAGE_LIST_KO, errorCode, 0, errorMessage);
            }

            @Override
            public void onComplete(boolean isOK, BoothList result, int errorCode, String errorMsg) {
                if (isOK) {
                    if (result == null) {
                        result = new BoothList();
                    }
                    sendMessage(ValueConstants.MSG_GET_FIRST_PAGE_LIST_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_GET_FIRST_PAGE_LIST_KO, errorCode, 0, errorMsg);
            }
        });
    }

    /**
     * 根据展位码获取达人咨询商品列表
     * @param context
     */
    public void doGetMasterConsultHomeCodeData(final int pageIndex ,int pageSize,final Context context) {
        CodeQueryDTO queryDTO = new CodeQueryDTO();
        queryDTO.boothCode = ResourceType.QUANYAN_HOME_CONSULTING_TALENT_INDEX;
        queryDTO.pageNo = pageIndex;
        queryDTO.pageSize = pageSize;
        if(!StringUtil.isEmpty(SPUtils.getExtraCurrentLon(context))) {
            queryDTO.longitude = Double.parseDouble(SPUtils.getExtraCurrentLon(context));
        }
        if(!StringUtil.isEmpty(SPUtils.getExtraCurrentLat(context))) {
            queryDTO.latitude = Double.parseDouble(SPUtils.getExtraCurrentLat(context));
        }
        NetManager.getInstance(context).doGetThemeItemListByBoothCode(queryDTO, new OnResponseListener<BoothItemsResult>() {

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_MASTER_SEARCH_KO, errorCode, 0, errorMessage);
            }

            @Override
            public void onComplete(boolean isOK, BoothItemsResult result, int errorCode, String errorMsg) {
                if (isOK) {
                    if (result == null) {
                        result = new BoothItemsResult();
                    }
                    sendMessage(ValueConstants.MSG_MASTER_SEARCH_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_MASTER_SEARCH_KO, errorCode, 0, errorMsg);
            }
        });
    }

}
