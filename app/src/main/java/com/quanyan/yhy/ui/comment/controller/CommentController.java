package com.quanyan.yhy.ui.comment.controller;

import android.content.Context;
import android.os.Handler;

import com.quanyan.base.BaseController;
import com.quanyan.yhy.net.NetManager;
import com.quanyan.yhy.net.lsn.OnResponseListener;
import com.yhy.common.beans.net.model.comment.DimensionList;
import com.yhy.common.beans.net.model.comment.RateCaseList;
import com.yhy.common.beans.net.model.comment.RateInfoList;
import com.yhy.common.beans.net.model.comment.RateInfoQuery;
import com.yhy.common.beans.net.model.common.PictureTextListQuery;
import com.yhy.common.beans.net.model.common.person.PictureTextListResult;
import com.yhy.common.beans.net.model.tm.PostRateParam;
import com.yhy.common.beans.net.model.trip.HotelFacilityResult;
import com.yhy.common.beans.net.model.trip.QueryFacilitiesDTO;
import com.yhy.common.beans.net.model.user.UserAssets;
import com.yhy.common.constants.ValueConstants;

/**
 * Created with Android Studio.
 * Title:CommentController
 * Description:评价网络调用接口
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-5-12
 * Time:9:48
 * Version 1.1.0
 */


public class CommentController extends BaseController {
    public CommentController(Context context, Handler handler) {
        super(context, handler);
    }

    /**
     * 填写评价init
     * @param type
     */
    public void doGetRateDimensionList(final Context context, String type){
        NetManager.getInstance(context).doGetRateDimensionList(type, new OnResponseListener<DimensionList>() {

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_COMMENT_WRITE_INIT_KO, errorCode, 0, errorMessage);
            }

            @Override
            public void onComplete(boolean isOK, DimensionList result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(ValueConstants.MSG_COMMENT_WRITE_INIT_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_COMMENT_WRITE_INIT_KO, errorCode, 0, errorMsg);
            }
        });
    }

    /**
     * 提交评价
     * @param param
     */
    public void doPostRate(final Context context, PostRateParam param){
        NetManager.getInstance(context).doPostRate(param, new OnResponseListener<Boolean>() {
            @Override
            public void onComplete(boolean isOK, Boolean result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(ValueConstants.MSG_COMMENT_WRITE_SUBMIT_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_COMMENT_WRITE_SUBMIT_KO, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_COMMENT_WRITE_SUBMIT_KO, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 获取评价列表头部信息
     * @param
     */
    public void doGetRateCaseList(final Context context, long id, String type) {
        NetManager.getInstance(context).doGetRateCaseList(id, type, new OnResponseListener<RateCaseList>() {
            @Override
            public void onComplete(boolean isOK, RateCaseList result, int errorCode, String errorMsg) {
                if (isOK) {
                    if (result == null) {
                        result = new RateCaseList();
                    }
                    sendMessage(ValueConstants.MSG_COMMENT_LIST_HEADER_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_COMMENT_LIST_HEADER_KO, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_COMMENT_LIST_HEADER_KO, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 获取评价列表接口
     * @param
     */
    public void doGetRateInfoPageList(final Context context, RateInfoQuery param) {
        NetManager.getInstance(context).doGetRateInfoPageList(param, new OnResponseListener<RateInfoList>() {
            @Override
            public void onComplete(boolean isOK, RateInfoList result, int errorCode, String errorMsg) {
                if (isOK) {
                    if (result == null) {
                        result = new RateInfoList();
                    }
                    sendMessage(ValueConstants.MSG_COMMENT_LIST_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_COMMENT_LIST_KO, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_COMMENT_LIST_KO, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 获取酒店服务设施
     * @param params
     */
    public void doGetHotelFacilities(final Context context, QueryFacilitiesDTO params) {

        NetManager.getInstance(context).doGetHotelFacilities(params, new OnResponseListener<HotelFacilityResult>() {
            @Override
            public void onComplete(boolean isOK, HotelFacilityResult result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(ValueConstants.MSG_GET_HOTEL_FACILITY_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_GET_HOTEL_FACILITY_KO, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_GET_HOTEL_FACILITY_KO, errorCode, 0, errorMessage);
            }
        });
    }


    /**
     * 我的界面数据更新
     * @param context
     *
     */
    public void doGetUserAssets(Context context) {

        NetManager.getInstance(context).doGetUserAssets(new OnResponseListener<UserAssets>() {
            @Override
            public void onComplete(boolean isOK, UserAssets result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(ValueConstants.MSG_GET_MINE_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_GET_MINE_KO, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_GET_MINE_KO, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 添加图文详情--达人主页
     * @param context
     * @param params
     */
    public void doAddPictureText(Context context, PictureTextListQuery params){
        NetManager.getInstance(context).doAddPictureText(params, new OnResponseListener<Boolean>() {
            @Override
            public void onComplete(boolean isOK, Boolean result, int errorCode, String errorMsg) {
                if(isOK){
                    sendMessage(ValueConstants.MSG_MINE_HOME_PAGE_ADD_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_MINE_HOME_PAGE_ADD_KO, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_MINE_HOME_PAGE_ADD_KO, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 查询图文详情--达人主页
     * @param context
     * @param id
     * @param type
     */
    public void doGetPictureTextInfo(Context context, long id, String type){
        NetManager.getInstance(context).doGetPictureTextInfo(id, type, new OnResponseListener<PictureTextListResult>() {
            @Override
            public void onComplete(boolean isOK, PictureTextListResult result, int errorCode, String errorMsg) {
                if(isOK){
                    sendMessage(ValueConstants.MSG_MINE_HOME_PAGE_SELECT_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_MINE_HOME_PAGE_SELECT_KO, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_MINE_HOME_PAGE_SELECT_KO, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 修改图文详情--达人主页
     * @param context
     * @param params
     */
    public void doUpdatePictureText(Context context, PictureTextListQuery params){
        NetManager.getInstance(context).doUpdatePictureText(params, new OnResponseListener<Boolean>() {
            @Override
            public void onComplete(boolean isOK, Boolean result, int errorCode, String errorMsg) {
                if(isOK){
                    sendMessage(ValueConstants.MSG_MINE_HOME_PAGE_EDIT_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_MINE_HOME_PAGE_EDIT_KO, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_MINE_HOME_PAGE_EDIT_KO, errorCode, 0, errorMessage);
            }
        });
    }

}
