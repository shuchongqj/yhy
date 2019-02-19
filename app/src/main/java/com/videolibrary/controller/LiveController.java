package com.videolibrary.controller;

import android.content.Context;

import com.quanyan.yhy.net.NetManager;
import com.quanyan.yhy.net.lsn.OnResponseListener;
import com.quanyan.yhy.ui.BaseController1;
import com.smart.sdk.api.resp.Api_LIVE_LiveRoomHasOrNot;
import com.smart.sdk.api.resp.Api_SNSCENTER_SnsCloseLiveResult;
import com.smart.sdk.api.resp.Api_SNSCENTER_SnsClosedViewTopNQuery;
import com.smart.sdk.api.resp.Api_SNSCENTER_SnsClosedViewTopNResult;
import com.smart.sdk.api.resp.Api_SNSCENTER_SnsCreateLiveResult;
import com.smart.sdk.api.resp.Api_SNSCENTER_SnsHasNoEndBatchResult;
import com.smart.sdk.api.resp.Api_SNSCENTER_SnsLiveRecordResult;
import com.smart.sdk.api.resp.Api_SNSCENTER_SnsReplayListQuery;
import com.smart.sdk.api.resp.Api_SNSCENTER_SnsReplayListResult;
import com.yhy.common.base.NoLeakHandler;
import com.yhy.common.beans.net.model.common.Booth;
import com.yhy.common.beans.net.model.master.TalentInfo;
import com.yhy.common.beans.net.model.msg.CreateLiveResult;
import com.yhy.common.beans.net.model.msg.DeleteLiveListInfo;
import com.yhy.common.beans.net.model.msg.DisableParam;
import com.yhy.common.beans.net.model.msg.DisableResult;
import com.yhy.common.beans.net.model.msg.FollowAnchorParam;
import com.yhy.common.beans.net.model.msg.FollowAnchorResult;
import com.yhy.common.beans.net.model.msg.LiveRecordAPIPageQuery;
import com.yhy.common.beans.net.model.msg.LiveRecordAPIPageResult;
import com.yhy.common.beans.net.model.msg.LiveRecordInfo;
import com.yhy.common.beans.net.model.msg.LiveRecordListUserIdQuery;
import com.yhy.common.beans.net.model.msg.LiveRoomLivingRecordResult;
import com.yhy.common.beans.net.model.msg.LiveRoomResult;
import com.yhy.common.beans.net.model.msg.OtherMsgParam;
import com.yhy.common.beans.net.model.msg.OtherMsgResult;
import com.yhy.common.constants.ValueConstants;

import java.util.List;
import java.util.UUID;

/**
 * Created with Android Studio.
 * Title:LiveController
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:邓明佳
 * Date:2016/8/17
 * Time:9:39
 * Version 1.0
 */
public class LiveController extends BaseController1 {

    public static final int MSG_LIVE_LIST_OK = 0x10201;
    public static final int MSG_LIVE_LIST_ERROR = 0x10202;

    public static final int MSG_GET_LIVE_RECORD_OK = 0x10203;
    public static final int MSG_GET_LIVE_RECORD_ERROR = 0x10204;

    public static final int MSG_GET_USER_LIVE_STATUS_OK = 0x10205;
    public static final int MSG_GET_USER_LIVE_STATUS_ERROR = 0x10206;

    public static final int MSG_UPDATE_LIVE_STATUS_OK = 0x10207;
    public static final int MSG_UPDATE_LIVE_STATUS_ERROR = 0x10208;

    public static final int MSG_LIVE_CREATE_OK = 0x10209;
    public static final int MSG_LIVE_CREATE_ERROR = 0x10210;

    public static final int MSG_LIVE_FOLLOW_OK = 0x10211;
    public static final int MSG_LIVE_FOLLOW_ERROR = 0x10212;

    public static final int MSG_LIVE_HAS_NOENDLIVE_OK = 0x10213;
    public static final int MSG_LIVE_HAS_NOENDLIVE_ERROR = 0x10214;

    public static final int MSG_LIVE_ROOM_INFO_OK = 0x10215;
    public static final int MSG_LIVE_ROOM_INFO_ERROR = 0x10216;

    public static final int MSG_UPDATE_LIVE_ROOM_OK = 0x10217;
    public static final int MSG_UPDATE_LIVE_ROOM_ERROR = 0x10218;

    public static final int MSG_DISABLE_SEND_MSG_OK = 0x10219;
    public static final int MSG_DISABLE_SEND_MSG_ERROR = 0x10220;

    public static final int MSG_CLOSE_LIVE_OK = 0x10221;
    public static final int MSG_CLOSE_LIVE_ERROR = 0x10222;

    public static final int MSG_DELTE_REPLAY_OK = 0x10223;
    public static final int MSG_DELTE_REPLAY_ERROR = 0x10224;

    public static final int MSG_LIVE_BANNER_OK = 0x10225;
    public static final int MSG_LIVE_BANNER_ERROR = 0x10226;

    public static final int MSG_LIVE_ROOM_LIVEING_RECORD_OK = 0x10227;
    public static final int MSG_LIVE_ROOM_LIVEING_RECORD_ERROR = 0x10228;

    public static final int MSG_LIVE_HAS_ROOM_OK = 0x10229;
    public static final int MSG_LIVE_HAS_ROOM_ERROR = 0x10230;

    public static final int MSG_GET_LIVE_OVER_RESULT_OK = 0x10231;
    public static final int MSG_GET_LIVE_OVER_RESULT_ERROR = 0x10232;

    public static final int MSG_GET_HASH_NO_END_LIVE_RESULT_OK = 0x10233;
    public static final int MSG_GET_HASH_NO_END_LIVE_RESULT_ERROR = 0x10234;

    private LiveController() {
    }

    public static LiveController sInstance;

    public static synchronized LiveController getInstance() {
        if (sInstance == null) {
            sInstance = new LiveController();
        }
        return sInstance;
    }

    /**
     * 发起直播
     */
    public void createLive(Context context, final NoLeakHandler handler, LiveRecordInfo info) {
        NetManager.getInstance(context).doCreateLive(info, new OnResponseListener<Api_SNSCENTER_SnsCreateLiveResult>() {
            @Override
            public void onComplete(boolean isOK, Api_SNSCENTER_SnsCreateLiveResult result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(handler, MSG_LIVE_CREATE_OK, 0, 0, result);
                    return;
                }
                sendMessage(handler, MSG_LIVE_CREATE_ERROR, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(handler, MSG_LIVE_CREATE_ERROR, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 获取直播列表
     *
     * @param context
     * @param liveStatus       直播状态{@link com.videolibrary.metadata.LiveTypeConstants#LIVE_ING},
     *                         {@link com.videolibrary.metadata.LiveTypeConstants#LIVE_REPLAY}
     * @param locationCityCode 直播城市code, 0:火星 -1/null:全部，其他省级code
     * @param pageNo           页码
     * @param pageSize         每页大小
     */
    public void getLiveList(Context context, final NoLeakHandler handler, final List<String> liveStatus,
                            final String locationCityCode, final int pageNo, final int pageSize) {
        LiveRecordAPIPageQuery liveRecordAPIPageQuery = new LiveRecordAPIPageQuery();
        liveRecordAPIPageQuery.liveStatus = liveStatus;
        liveRecordAPIPageQuery.locationCityCode = locationCityCode;
        liveRecordAPIPageQuery.pageNo = pageNo;
        liveRecordAPIPageQuery.pageSize = pageSize;
        NetManager.getInstance(context).doGetLiveList(liveRecordAPIPageQuery, new OnResponseListener<LiveRecordAPIPageResult>() {
            @Override
            public void onComplete(boolean isOK, LiveRecordAPIPageResult result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(handler, MSG_LIVE_LIST_OK, 0, 0, result);
                    return;
                }

                sendMessage(handler, MSG_LIVE_LIST_ERROR, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(handler, MSG_LIVE_LIST_ERROR, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 获取用户直播列表
     *
     * @param context
     * @param handler
     * @param userId     达人ID（根据达人ID查找）
     * @param liveStatus 直播状态:直播/回放（根据播放状态查找，默认不填，查找所有状态)
     * @param pageNo     页码
     * @param pageSize   每页大小
     */
    public void getLivelistByUserId(Context context, final NoLeakHandler handler, final long userId, final List<String> liveStatus,
                                    final List<String> liveRecordStatus, final int pageNo, final int pageSize) {
        LiveRecordListUserIdQuery api_live_liveRecordListUserIdQuery = new LiveRecordListUserIdQuery();
        api_live_liveRecordListUserIdQuery.userId = userId;
        api_live_liveRecordListUserIdQuery.liveStatus = liveStatus;
        api_live_liveRecordListUserIdQuery.pageNo = pageNo;
        api_live_liveRecordListUserIdQuery.pageSize = pageSize;
        api_live_liveRecordListUserIdQuery.liveRecordStatus = liveRecordStatus;
        NetManager.getInstance(context).doGetLiveListByUserId(api_live_liveRecordListUserIdQuery, new OnResponseListener<LiveRecordAPIPageResult>() {
            @Override
            public void onComplete(boolean isOK, LiveRecordAPIPageResult result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(handler, MSG_LIVE_LIST_OK, 0, 0, result);
                    return;
                }

                sendMessage(handler, MSG_LIVE_LIST_ERROR, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(handler, MSG_LIVE_LIST_ERROR, errorCode, 0, errorMessage);
            }
        });

    }

    /**
     * 新的用户回放列表
     * @param context
     * @param handler
     * @param userID
     * @param pageNo
     * @param pageSize
     */
    public void getLiveListByUserId(Context context,final NoLeakHandler handler,long userID,int pageNo,int pageSize){
        Api_SNSCENTER_SnsReplayListQuery api_snscenter_snsReplayListQuery = new Api_SNSCENTER_SnsReplayListQuery();
        api_snscenter_snsReplayListQuery.traceId = UUID.randomUUID().toString();
        api_snscenter_snsReplayListQuery.userId = userID;
        api_snscenter_snsReplayListQuery.pageNo = pageNo;
        api_snscenter_snsReplayListQuery.pageSize = pageSize;
        NetManager.getInstance(context).doGetLiveListByUserID(api_snscenter_snsReplayListQuery, new OnResponseListener<Api_SNSCENTER_SnsReplayListResult>() {
            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(handler, MSG_LIVE_LIST_ERROR, errorCode, 0, errorMessage);
            }

            @Override
            public void onComplete(boolean isOK, Api_SNSCENTER_SnsReplayListResult result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(handler, MSG_LIVE_LIST_OK, 0, 0, result);
                    return;
                }

                sendMessage(handler, MSG_LIVE_LIST_ERROR, errorCode, 0, errorMsg);
            }
        });
    }

    public void getLiveRoomInfo(Context context, long userId, final NoLeakHandler handler) {
        NetManager.getInstance(context).doGetLiveRoomInfo(userId, new OnResponseListener<LiveRoomResult>() {
            @Override
            public void onComplete(boolean isOK, LiveRoomResult result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(handler, MSG_LIVE_ROOM_INFO_OK, result);
                    return;
                }
                sendMessage(handler, MSG_LIVE_ROOM_INFO_ERROR, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(handler, MSG_LIVE_ROOM_INFO_ERROR, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 获取直播项
     *
     * @param context
     * @param livecenterId 直播ID
     */
    public void getLiveRecord(Context context, final NoLeakHandler handler, final long livecenterId) {
        NetManager.getInstance(context).doGetLiveRecord(livecenterId, new OnResponseListener<Api_SNSCENTER_SnsLiveRecordResult>() {
            @Override
            public void onComplete(boolean isOK, Api_SNSCENTER_SnsLiveRecordResult result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(handler, MSG_GET_LIVE_RECORD_OK, 0, 0, result);
                    return;
                }
                sendMessage(handler, MSG_GET_LIVE_RECORD_ERROR, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(handler, MSG_GET_LIVE_RECORD_ERROR, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 获取用户直播状态
     *
     * @param context
     * @param userId
     */
    /*public void getUserLiveStatus(Context context, final NoLeakHandler handler,final long userId){
        mUiHandler = handler;
        NetManager.getInstance(context).doGetUserLiveStatus(userId, new OnResponseListener<Boolean>() {
            @Override
            public void onComplete(boolean isOK, Boolean result, int errorCode, String errorMsg) {
                if(isOK){
                    sendMessage(MSG_GET_USER_LIVE_STATUS_OK, 0,0,result);
                    return;
                }
                sInstance.sendMessage(MSG_GET_USER_LIVE_STATUS_ERROR, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(MSG_GET_USER_LIVE_STATUS_ERROR, errorCode, 0, errorMessage);
            }
        });
    }*/

    /**
     * 保存、关闭直播
     *
     * @param context
     * @param liveRecordId 直播记录ID,必填
     * @param userId       主要用来校验
     * @param liveTitle    修改标题
     * @param liveCategory 修改分类
     * @param liveCover    修改封面
     * @param liveStatus   修改直播状态
     * @param hasReplay    修改是否生成回放
     * @param viewCount    修改观看人数
     * @param cityCode     / 修改城市code
     * @param cityName     修改城市名称
     */
    public void updateLive(Context context, final NoLeakHandler handler, final long liveRecordId, final long userId, final String liveTitle,
                           final long liveCategory, final String liveCover, final int liveStatus, final int hasReplay,
                           final int viewCount, final String cityCode, final String cityName) {
//        mUiHandler = handler;
//        UpdateLiveRecordInfo updateLiveRecordInfo = new UpdateLiveRecordInfo();
//        updateLiveRecordInfo.liveRecordId = liveRecordId;
//        updateLiveRecordInfo.userId = userId;
//        updateLiveRecordInfo.liveTitle = liveTitle;
//        updateLiveRecordInfo.liveCategory = liveCategory;
//        updateLiveRecordInfo.liveCover = liveCover;
//        updateLiveRecordInfo.liveStatus = liveStatus;
//        updateLiveRecordInfo.hasReplay = hasReplay;
//        updateLiveRecordInfo.viewCount = viewCount;
//        updateLiveRecordInfo.locationCityCode = cityCode;
//        updateLiveRecordInfo.locationCityName = cityName;
//        NetManager.getInstance(context).doGetUserLiveStatus(updateLiveRecordInfo, new OnResponseListener<Boolean>() {
//            @Override
//            public void onComplete(boolean isOK, Boolean result, int errorCode, String errorMsg) {
//                if(isOK){
//                    sendMessage(MSG_UPDATE_LIVE_STATUS_OK, 0,0,result);
//                    return;
//                }
//                sendMessage(MSG_UPDATE_LIVE_STATUS_ERROR, errorCode, 0, errorMsg);
//            }
//
//            @Override
//            public void onInternError(int errorCode, String errorMessage) {
//                sendMessage(MSG_UPDATE_LIVE_STATUS_ERROR, errorCode, 0, errorMessage);
//            }
//        });
    }

    /**
     * 关注主播
     *
     * @param context
     * @param handler
     * @param liveId   // 直播ID
     * @param anchorId 关注的主播id
     */
    public void followAnchor(Context context, final NoLeakHandler handler, final long liveId, final long anchorId) {
        FollowAnchorParam followAnchorParam = new FollowAnchorParam();
        followAnchorParam.liveId = liveId;
        followAnchorParam.anchorId = anchorId;
        NetManager.getInstance(context).doFollowAnchor(followAnchorParam, new OnResponseListener<FollowAnchorResult>() {
            @Override
            public void onComplete(boolean isOK, FollowAnchorResult result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(handler, MSG_LIVE_FOLLOW_OK, 0, 0, result);
                    return;
                }
                sendMessage(handler, MSG_LIVE_FOLLOW_ERROR, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(handler, MSG_LIVE_FOLLOW_ERROR, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 获取达人详情
     *
     * @param id
     */
    public void getMasterDetail(final Context context, final NoLeakHandler handler, final long id) {
        NetManager.getInstance(context).doGetTalentDetail(id, new OnResponseListener<TalentInfo>() {
            @Override
            public void onComplete(boolean isOK, TalentInfo result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(handler, ValueConstants.MSG_GET_MASTER_DETAIL_OK, result);
                    return;
                }
                sendMessage(handler, ValueConstants.MSG_GET_MASTER_DETAIL_KO, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(handler, ValueConstants.MSG_GET_MASTER_DETAIL_KO, errorCode, 0, errorMessage);
            }
        });
    }

    public void closeLive(Context context, final NoLeakHandler handler, long liveId) {
        NetManager.getInstance(context).doCloseLive(liveId, new OnResponseListener<Api_SNSCENTER_SnsCloseLiveResult>() {
            @Override
            public void onComplete(boolean isOK, Api_SNSCENTER_SnsCloseLiveResult result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(handler, MSG_CLOSE_LIVE_OK, result);
                    return;
                }
                sendMessage(handler, MSG_CLOSE_LIVE_ERROR, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(handler, MSG_CLOSE_LIVE_ERROR, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 是否含有未结束直播
     */
    public void hasNoEndLive(Context context, final NoLeakHandler handler) {
        NetManager.getInstance(context).hasNoEndLive(new OnResponseListener<CreateLiveResult>() {
            @Override
            public void onComplete(boolean isOK, CreateLiveResult result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(handler, MSG_LIVE_HAS_NOENDLIVE_OK, result);
                    return;
                }
                sendMessage(handler, MSG_LIVE_HAS_NOENDLIVE_ERROR, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(handler, MSG_LIVE_HAS_NOENDLIVE_ERROR, errorCode, 0, errorMessage);
            }
        });
    }

//    /**
//     * 获取达人详情
//     */
//    public void updateLiveRoom(final Context context, final NoLeakHandler handler, final UpdateLiveRoom liveRoom) {
//        NetManager.getInstance(context).doUpdateLiveRoom(liveRoom, new OnResponseListener<Boolean>() {
//            @Override
//            public void onComplete(boolean isOK, Boolean result, int errorCode, String errorMsg) {
//                if (isOK && result) {
//                    sendMessage(handler,MSG_UPDATE_LIVE_ROOM_OK, liveRoom);
//                    return;
//                }
//                sendMessage(handler,MSG_UPDATE_LIVE_ROOM_ERROR, errorCode, 0, errorMsg);
//            }
//
//            @Override
//            public void onInternError(int errorCode, String errorMessage) {
//                sendMessage(handler,MSG_UPDATE_LIVE_ROOM_ERROR, errorCode, 0, errorMessage);
//            }
//        });
//    }

    /**
     * 获取达人详情
     */
    public void disableSendMsg(final Context context, final NoLeakHandler handler, final DisableParam param) {
        NetManager.getInstance(context).doDisableSendMsg(param, new OnResponseListener<DisableResult>() {
            @Override
            public void onComplete(boolean isOK, DisableResult result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(handler, MSG_DISABLE_SEND_MSG_OK, result);
                    return;
                }
                sendMessage(handler, MSG_DISABLE_SEND_MSG_ERROR, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(handler, MSG_DISABLE_SEND_MSG_ERROR, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 批量删除回放
     *
     * @param context
     * @param handler
     * @param liveIds 回放ID List
     */
    public void deleteReplay(final Context context, final NoLeakHandler handler, long[] liveIds) {
        DeleteLiveListInfo deleteLiveListInfo = new DeleteLiveListInfo();
        deleteLiveListInfo.idArray = liveIds;
        NetManager.getInstance(context).doDeleteLiveReplay(deleteLiveListInfo, new OnResponseListener<Boolean>() {
            @Override
            public void onComplete(boolean isOK, Boolean result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(handler, MSG_DELTE_REPLAY_OK, result);
                    return;
                }
                sendMessage(handler, MSG_DELTE_REPLAY_ERROR, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(handler, MSG_DELTE_REPLAY_ERROR, errorCode, 0, errorMessage);
            }
        });
    }

    public void sendOtherMsg(Context context, final NoLeakHandler mHandler, OtherMsgParam param) {
        NetManager.getInstance(context).doSendOtherMsg(param, new OnResponseListener<OtherMsgResult>() {
            @Override
            public void onComplete(boolean isOK, OtherMsgResult result, int errorCode, String errorMsg) {

            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {

            }
        });
    }

    /**
     * 获取直播列表的广告位
     */
    public void doGetLiveListBanner(final Context context, final NoLeakHandler mHandler, String code) {
        NetManager.getInstance(context).doGetBooth(code, new OnResponseListener<Booth>() {
            @Override
            public void onComplete(boolean isOK, Booth result, int errorCode, String errorMsg) {
                if (isOK) {
                    if (result == null) {
                        result = new Booth();
                    }
                    sendMessage(mHandler, MSG_LIVE_BANNER_OK, result);
                    return;
                }
                sendMessage(mHandler, MSG_LIVE_BANNER_ERROR, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(mHandler, MSG_LIVE_BANNER_ERROR, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 获取直播间正在直播记录
     *
     * @param context
     * @param handler
     * @param roomId 直播间的id
     */
    public void doGetLiveRoomLivingRecord(final Context context, final NoLeakHandler handler, final long roomId){
        NetManager.getInstance(context).doGetLiveRoomLivingRecord(roomId, new OnResponseListener<LiveRoomLivingRecordResult>() {
            @Override
            public void onComplete(boolean isOK, LiveRoomLivingRecordResult result, int errorCode, String errorMsg) {
                if(isOK){
                    if(result == null){
                        result = new LiveRoomLivingRecordResult();
                    }
                    sendMessage(handler, MSG_LIVE_ROOM_LIVEING_RECORD_OK, result);
                    return;
                }
                sendMessage(handler, MSG_LIVE_ROOM_LIVEING_RECORD_ERROR, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(handler, MSG_LIVE_ROOM_LIVEING_RECORD_ERROR, errorCode, 0, errorMessage);

            }
        });
    }

    public void doGetHasLiveRoomOrNot(final Context context, final NoLeakHandler handler, final long userID){
        NetManager.getInstance(context).doGetHasLiveRoomOrNot(userID, new OnResponseListener<Api_LIVE_LiveRoomHasOrNot>() {
            @Override
            public void onComplete(boolean isOK, Api_LIVE_LiveRoomHasOrNot result, int errorCode, String errorMsg) {
                if (isOK){
                    if (result == null){
                        result.hasRoomOrNot = false;
                    }
                    sendMessage(handler,MSG_LIVE_HAS_ROOM_OK,result);
                    return;
                }
                sendMessage(handler,MSG_LIVE_HAS_ROOM_ERROR,errorCode,0,errorMsg);

            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(handler,MSG_LIVE_HAS_ROOM_ERROR,errorCode,0,errorMessage);
            }
        });
    }

    public void getLiveOverResult(final Context context,final NoLeakHandler handler, long roomId) {
        Api_SNSCENTER_SnsClosedViewTopNQuery api_snscenter_snsClosedViewTopNQuery = new Api_SNSCENTER_SnsClosedViewTopNQuery();
        api_snscenter_snsClosedViewTopNQuery.traceId = UUID.randomUUID().toString();
        api_snscenter_snsClosedViewTopNQuery.roomId = roomId;
        NetManager.getInstance(context).doGetLiveOverResult(api_snscenter_snsClosedViewTopNQuery, new OnResponseListener<Api_SNSCENTER_SnsClosedViewTopNResult>() {
            @Override
            public void onComplete(boolean isOK, Api_SNSCENTER_SnsClosedViewTopNResult result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(handler, MSG_GET_LIVE_OVER_RESULT_OK, 0, 0, result);
                    return;
                }

                sendMessage(handler, MSG_GET_LIVE_OVER_RESULT_ERROR, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(handler, MSG_GET_LIVE_OVER_RESULT_ERROR, errorCode, 0, errorMessage);
            }
        });
    }

    public void getHasNoEndBatch(final Context context, final NoLeakHandler mHandler, long uid) {
        NetManager.getInstance(context).doGetHasNoEndBatch(uid, new OnResponseListener<Api_SNSCENTER_SnsHasNoEndBatchResult>() {
            @Override
            public void onComplete(boolean isOK, Api_SNSCENTER_SnsHasNoEndBatchResult result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(mHandler, MSG_GET_HASH_NO_END_LIVE_RESULT_OK, 0, 0, result);
                    return;
                }

                sendMessage(mHandler, MSG_GET_HASH_NO_END_LIVE_RESULT_ERROR, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(mHandler, MSG_GET_HASH_NO_END_LIVE_RESULT_ERROR, errorCode, 0, errorMessage);
            }
        });
    }
}
