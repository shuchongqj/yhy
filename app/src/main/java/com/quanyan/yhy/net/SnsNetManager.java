package com.quanyan.yhy.net;

import android.content.Context;
import android.os.Handler;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.ErrorCode;
import com.quanyan.yhy.net.lsn.OnResponseListener;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.smart.sdk.api.request.ApiCode;
import com.smart.sdk.api.request.Comcenter_AddNewCommentToSubject;
import com.smart.sdk.api.request.Comcenter_AddNewPraiseToComment;
import com.smart.sdk.api.request.Comcenter_DelReploy;
import com.smart.sdk.api.request.Comcenter_EditPraiseToComment;
import com.smart.sdk.api.request.Comcenter_FindSupportList;
import com.smart.sdk.api.request.Comcenter_FindTagList;
import com.smart.sdk.api.request.Comcenter_GetProductRateInfoPageList;
import com.smart.sdk.api.request.Comcenter_GetRateCaseList;
import com.smart.sdk.api.request.Comcenter_GetRateCountByOutId;
import com.smart.sdk.api.request.Comcenter_GetRateDimensionList;
import com.smart.sdk.api.request.Comcenter_GetRateInfoPageList;
import com.smart.sdk.api.request.Comcenter_GetTagInfoPageByOutType;
import com.smart.sdk.api.request.Comcenter_GetTagInfoPageByOutTypeAndUserId;
import com.smart.sdk.api.request.Comcenter_PageGetComments;
import com.smart.sdk.api.request.Competition_GetArrangeCampaignSquare;
import com.smart.sdk.api.request.Items_GetThemeItemListByBoothCode;
import com.smart.sdk.api.request.Live_DeleteLiveList;
import com.smart.sdk.api.request.Live_DisableSendMsg;
import com.smart.sdk.api.request.Live_FollowAnchor;
import com.smart.sdk.api.request.Live_GetLiveCategoryList;
import com.smart.sdk.api.request.Live_GetLiveList;
import com.smart.sdk.api.request.Live_GetLiveListByUserId;
import com.smart.sdk.api.request.Live_GetLiveRoomInfo;
import com.smart.sdk.api.request.Live_GetLiveRoomLivingRecord;
import com.smart.sdk.api.request.Live_HasLiveRoomOrNot;
import com.smart.sdk.api.request.Live_HasNoEndLive;
import com.smart.sdk.api.request.Live_SendNotifyMsg;
import com.smart.sdk.api.request.Live_UserGetIMAddress;
import com.smart.sdk.api.request.Live_VisitorGetIMAddress;
import com.smart.sdk.api.request.Resourcecenter_GetArticleListByBoothCode;
import com.smart.sdk.api.request.Resourcecenter_GetDiscoverPageContent;
import com.smart.sdk.api.request.Resourcecenter_GetSurroundPageContent;
import com.smart.sdk.api.request.Snscenter_AddFollower;
import com.smart.sdk.api.request.Snscenter_AddUGC;
import com.smart.sdk.api.request.Snscenter_AddUserShield;
import com.smart.sdk.api.request.Snscenter_CloseLive;
import com.smart.sdk.api.request.Snscenter_CreateLive;
import com.smart.sdk.api.request.Snscenter_DelComment;
import com.smart.sdk.api.request.Snscenter_DelSubjectInfo;
import com.smart.sdk.api.request.Snscenter_DelSubjectLiveInfo;
import com.smart.sdk.api.request.Snscenter_DelUGC;
import com.smart.sdk.api.request.Snscenter_Dislike;
import com.smart.sdk.api.request.Snscenter_ExitClubMember;
import com.smart.sdk.api.request.Snscenter_GetActiveByOutUserIdPage;
import com.smart.sdk.api.request.Snscenter_GetActivityListByOutIdPage;
import com.smart.sdk.api.request.Snscenter_GetActivityListByTitlePage;
import com.smart.sdk.api.request.Snscenter_GetActivityMemberList;
import com.smart.sdk.api.request.Snscenter_GetAllActivityListPage;
import com.smart.sdk.api.request.Snscenter_GetAllLiveListPage;
import com.smart.sdk.api.request.Snscenter_GetAreUgcPageList;
import com.smart.sdk.api.request.Snscenter_GetClubActivityDetail;
import com.smart.sdk.api.request.Snscenter_GetClubInfo;
import com.smart.sdk.api.request.Snscenter_GetClubInfoListByClubNamePage;
import com.smart.sdk.api.request.Snscenter_GetClubListPageByTagId;
import com.smart.sdk.api.request.Snscenter_GetFansList;
import com.smart.sdk.api.request.Snscenter_GetFollowerList;
import com.smart.sdk.api.request.Snscenter_GetGuideTagInfo;
import com.smart.sdk.api.request.Snscenter_GetLiveRecord;
import com.smart.sdk.api.request.Snscenter_GetMemberActivityDetail;
import com.smart.sdk.api.request.Snscenter_GetMyClubInfoList;
import com.smart.sdk.api.request.Snscenter_GetMySubjectList;
import com.smart.sdk.api.request.Snscenter_GetRecommendPageList;
import com.smart.sdk.api.request.Snscenter_GetSubjectDynamicDetail;
import com.smart.sdk.api.request.Snscenter_GetSubjectListByCludId;
import com.smart.sdk.api.request.Snscenter_GetSubjectLiveDetailById;
import com.smart.sdk.api.request.Snscenter_GetTagInfoListByType;
import com.smart.sdk.api.request.Snscenter_GetTopicInfo;
import com.smart.sdk.api.request.Snscenter_GetTopicPageList;
import com.smart.sdk.api.request.Snscenter_GetTopicUGCPageList;
import com.smart.sdk.api.request.Snscenter_GetTravelSpecialDetail;
import com.smart.sdk.api.request.Snscenter_GetTravelSpecialListPage;
import com.smart.sdk.api.request.Snscenter_GetUGCDetail;
import com.smart.sdk.api.request.Snscenter_GetUGCPageList;
import com.smart.sdk.api.request.Snscenter_GetUserReplayList;
import com.smart.sdk.api.request.Snscenter_GetUserUGCPageList;
import com.smart.sdk.api.request.Snscenter_GetUserVideoPageList;
import com.smart.sdk.api.request.Snscenter_HasNoEndBatch;
import com.smart.sdk.api.request.Snscenter_JoinClubMember;
import com.smart.sdk.api.request.Snscenter_PublishNewDynamicSubject;
import com.smart.sdk.api.request.Snscenter_PublishNewSubjectLive;
import com.smart.sdk.api.request.Snscenter_QueryGuidanceRecord;
import com.smart.sdk.api.request.Snscenter_QueryHomeLivePageList;
import com.smart.sdk.api.request.Snscenter_QueryUserSnsCountInfo;
import com.smart.sdk.api.request.Snscenter_RemoveFollower;
import com.smart.sdk.api.request.Snscenter_SaveUserCorrelation;
import com.smart.sdk.api.request.Snscenter_SearchHotTopicPageList;
import com.smart.sdk.api.request.Snscenter_SearchPageList;
import com.smart.sdk.api.request.Snscenter_SearchTopicTitlePageList;
import com.smart.sdk.api.request.Snscenter_SnsClosedViewTopN;
import com.smart.sdk.api.request.Train_AsyncCallback;
import com.smart.sdk.api.request.User_EditUserStatus;
import com.smart.sdk.api.request.User_GetBatchUserStatus;
import com.smart.sdk.api.resp.Api_BoolResp;
import com.smart.sdk.api.resp.Api_COMCENTER_ComSupportInfo;
import com.smart.sdk.api.resp.Api_COMCENTER_CommetDetailInfo;
import com.smart.sdk.api.resp.Api_COMCENTER_PageInfo;
import com.smart.sdk.api.resp.Api_COMCENTER_ProductRateInfoQuery;
import com.smart.sdk.api.resp.Api_COMCENTER_RateCountQuery;
import com.smart.sdk.api.resp.Api_COMCENTER_RateInfoQuery;
import com.smart.sdk.api.resp.Api_COMPETITION_CampaignsVoResult;
import com.smart.sdk.api.resp.Api_COMPETITION_QueryCampaignParam;
import com.smart.sdk.api.resp.Api_ITEMS_CodeQueryDTO;
import com.smart.sdk.api.resp.Api_LIVE_DeleteLiveListInfo;
import com.smart.sdk.api.resp.Api_LIVE_DisableParam;
import com.smart.sdk.api.resp.Api_LIVE_FollowParam;
import com.smart.sdk.api.resp.Api_LIVE_LiveRecordAPIPageQuery;
import com.smart.sdk.api.resp.Api_LIVE_LiveRecordListUserIdQuery;
import com.smart.sdk.api.resp.Api_LIVE_LiveRoomHasOrNot;
import com.smart.sdk.api.resp.Api_LIVE_MsgServerAddrParam;
import com.smart.sdk.api.resp.Api_LIVE_OtherMsgParam;
import com.smart.sdk.api.resp.Api_SNSCENTER_DisLikeArgs;
import com.smart.sdk.api.resp.Api_SNSCENTER_FollowPageQueryInfo;
import com.smart.sdk.api.resp.Api_SNSCENTER_GuideTagResult;
import com.smart.sdk.api.resp.Api_SNSCENTER_GuideTagResultList;
import com.smart.sdk.api.resp.Api_SNSCENTER_PageInfo;
import com.smart.sdk.api.resp.Api_SNSCENTER_RecommendPageQuery;
import com.smart.sdk.api.resp.Api_SNSCENTER_SearchPageQuery;
import com.smart.sdk.api.resp.Api_SNSCENTER_SnsCloseLiveResult;
import com.smart.sdk.api.resp.Api_SNSCENTER_SnsClosedViewTopNQuery;
import com.smart.sdk.api.resp.Api_SNSCENTER_SnsClosedViewTopNResult;
import com.smart.sdk.api.resp.Api_SNSCENTER_SnsCreateLiveRequest;
import com.smart.sdk.api.resp.Api_SNSCENTER_SnsCreateLiveResult;
import com.smart.sdk.api.resp.Api_SNSCENTER_SnsHasNoEndBatchResult;
import com.smart.sdk.api.resp.Api_SNSCENTER_SnsHomeLivePageQuery;
import com.smart.sdk.api.resp.Api_SNSCENTER_SnsLiveRecordResult;
import com.smart.sdk.api.resp.Api_SNSCENTER_SnsReplayListQuery;
import com.smart.sdk.api.resp.Api_SNSCENTER_SnsReplayListResult;
import com.smart.sdk.api.resp.Api_SNSCENTER_SubjectDynamic;
import com.smart.sdk.api.resp.Api_SNSCENTER_SubjectLive;
import com.smart.sdk.api.resp.Api_SNSCENTER_TagResultList;
import com.smart.sdk.api.resp.Api_SNSCENTER_TopicPageListQuery;
import com.smart.sdk.api.resp.Api_SNSCENTER_TopicPageQuery;
import com.smart.sdk.api.resp.Api_SNSCENTER_TopicSugPageListQuery;
import com.smart.sdk.api.resp.Api_SNSCENTER_TopicUgcPageQuery;
import com.smart.sdk.api.resp.Api_SNSCENTER_UgcInfo;
import com.smart.sdk.api.resp.Api_SNSCENTER_UserCorrelation;
import com.smart.sdk.api.resp.Api_TRAIN_AsyncCallbackParam;
import com.smart.sdk.api.resp.Api_TRAIN_ResponseDTO;
import com.smart.sdk.api.resp.Api_USER_UserBatchQuery;
import com.smart.sdk.client.ApiContext;
import com.smart.sdk.client.BaseRequest;
import com.yhy.common.beans.net.model.club.ClubInfoList;
import com.yhy.common.beans.net.model.club.ClubMemberInfo;
import com.yhy.common.beans.net.model.club.ClubMemberInfoList;
import com.yhy.common.beans.net.model.club.SnsActiveMemberPageInfo;
import com.yhy.common.beans.net.model.club.SnsActivePageInfo;
import com.yhy.common.beans.net.model.club.SnsActivePageInfoList;
import com.yhy.common.beans.net.model.club.SnsRimInfoList;
import com.yhy.common.beans.net.model.club.SubjectDetail;
import com.yhy.common.beans.net.model.club.SubjectDynamic;
import com.yhy.common.beans.net.model.club.SubjectInfo;
import com.yhy.common.beans.net.model.club.SubjectInfoList;
import com.yhy.common.beans.net.model.club.SubjectLive;
import com.yhy.common.beans.net.model.comment.ComSupportInfo;
import com.yhy.common.beans.net.model.comment.ComTagInfoList;
import com.yhy.common.beans.net.model.comment.CommentInfo;
import com.yhy.common.beans.net.model.comment.CommentInfoList;
import com.yhy.common.beans.net.model.comment.CommetDetailInfo;
import com.yhy.common.beans.net.model.comment.DimensionList;
import com.yhy.common.beans.net.model.comment.ProductRateInfoList;
import com.yhy.common.beans.net.model.comment.ProductRateInfoQuery;
import com.yhy.common.beans.net.model.comment.RateCaseList;
import com.yhy.common.beans.net.model.comment.RateCountInfo;
import com.yhy.common.beans.net.model.comment.RateCountQuery;
import com.yhy.common.beans.net.model.comment.RateInfoList;
import com.yhy.common.beans.net.model.comment.RateInfoQuery;
import com.yhy.common.beans.net.model.comment.SupportUserInfoList;
import com.yhy.common.beans.net.model.discover.ComUserAndTagInfoList;
import com.yhy.common.beans.net.model.discover.TopicDetailResult;
import com.yhy.common.beans.net.model.discover.TopicInfoResult;
import com.yhy.common.beans.net.model.discover.TopicInfoResultList;
import com.yhy.common.beans.net.model.discover.TravelSpecialInfo;
import com.yhy.common.beans.net.model.discover.TravelSpecialInfoList;
import com.yhy.common.beans.net.model.discover.UgcInfoResult;
import com.yhy.common.beans.net.model.discover.UgcInfoResultList;
import com.yhy.common.beans.net.model.item.BoothItemsResult;
import com.yhy.common.beans.net.model.item.CodeQueryDTO;
import com.yhy.common.beans.net.model.msg.CreateLiveResult;
import com.yhy.common.beans.net.model.msg.DeleteLiveListInfo;
import com.yhy.common.beans.net.model.msg.DisableParam;
import com.yhy.common.beans.net.model.msg.DisableResult;
import com.yhy.common.beans.net.model.msg.FollowAnchorParam;
import com.yhy.common.beans.net.model.msg.FollowAnchorResult;
import com.yhy.common.beans.net.model.msg.LiveCategoryResult;
import com.yhy.common.beans.net.model.msg.LiveMsgServerAddrParam;
import com.yhy.common.beans.net.model.msg.LiveMsgServerAddrResult;
import com.yhy.common.beans.net.model.msg.LiveRecordAPIPageQuery;
import com.yhy.common.beans.net.model.msg.LiveRecordAPIPageResult;
import com.yhy.common.beans.net.model.msg.LiveRecordInfo;
import com.yhy.common.beans.net.model.msg.LiveRecordListUserIdQuery;
import com.yhy.common.beans.net.model.msg.LiveRoomLivingRecordResult;
import com.yhy.common.beans.net.model.msg.LiveRoomResult;
import com.yhy.common.beans.net.model.msg.OtherMsgParam;
import com.yhy.common.beans.net.model.msg.OtherMsgResult;
import com.yhy.common.beans.net.model.rc.ArticleRecommendInfo;
import com.yhy.common.beans.net.model.rc.DiscoverPageContent;
import com.yhy.common.beans.net.model.rc.SurroundPageContent;
import com.yhy.common.beans.net.model.user.FollowRetInfo;
import com.yhy.common.beans.net.model.user.FollowerPageListResult;
import com.yhy.common.beans.net.model.user.SnsCountInfo;
import com.yhy.common.beans.net.model.user.UserStatusInfoList;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.SPUtils;
import com.yhy.router.YhyRouter;
import com.yhy.service.IUserService;
import com.yhy.user.UserService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class SnsNetManager extends BaseNetManager {
    private static SnsNetManager mInstance;

    @Autowired
    IUserService userService;

    public SnsNetManager(Context context, ApiContext apiContext, Handler handler) {
        mContext = context;
        mApiContext = apiContext;
        mHandler = handler;
        YhyRouter.getInstance().inject(this);
    }


    public synchronized static SnsNetManager getInstance(Context context, ApiContext apiContext, Handler handler) {
        if (mInstance == null) {
            mInstance = new SnsNetManager(context, apiContext, handler);
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
    //****************************************俱乐部 BEGIN ********************************//
    //TODO

    /**
     * 周边首页
     *
     * @param codes
     * @param lsn
     */
    public void doGetArroundPageList(List<String> codes,
                                     final OnResponseListener<SurroundPageContent> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        final Resourcecenter_GetSurroundPageContent req = new Resourcecenter_GetSurroundPageContent(codes);
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                SurroundPageContent value = null;
                if (req.getResponse() != null) {
                    try {
                        value = SurroundPageContent.deserialize(req.getResponse().serialize());
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
     * 发现首页
     *
     * @param lsn
     */
    public void doGetDiscoverPageList(List<String> codes, final OnResponseListener<DiscoverPageContent> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        if (codes == null) {
            codes = new ArrayList<>();
        }

        final Resourcecenter_GetDiscoverPageContent req = new Resourcecenter_GetDiscoverPageContent(codes);
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                DiscoverPageContent value = null;
                if (req.getResponse() != null) {
                    try {
                        value = DiscoverPageContent.deserialize(req.getResponse().serialize());
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
     * 标签列表
     *
     * @param outType
     * @param pageIndex
     * @param pageSize
     * @param lsn
     */
    public void doGetTagInfoPageByOutType(final String outType, final int pageIndex, final int pageSize, final OnResponseListener<ComTagInfoList> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        final Api_COMCENTER_PageInfo pageInfo = new Api_COMCENTER_PageInfo();
        pageInfo.pageNo = pageIndex;
        pageInfo.pageSize = pageSize;
        Runnable requestThread = new Runnable() {
            @Override
            public void run() {
                try {
                    final Comcenter_GetTagInfoPageByOutType req = new Comcenter_GetTagInfoPageByOutType(outType, pageInfo);
                    sendRequest(mContext, mApiContext, req);
                    if (req.getReturnCode() == ApiCode.SUCCESS) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                ComTagInfoList value = null;
                                if (req.getResponse() != null) {
                                    try {
                                        value = ComTagInfoList.deserialize(req.getResponse().serialize());
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
                        });
                    } else {
                        handlerException(req.getReturnCode(), req.getReturnMessage(), lsn);
                    }
                } catch (final Exception e) {
                    handlerRequestException(e, lsn);
                }
            }
        };
        NetThreadManager.getInstance().execute(requestThread);
    }

    /**
     * 分页获取活动参与的用户列表
     *
     * @param pageIndex
     * @param pageSize
     * @param id
     * @param lsn
     */
    public void doGetActiveJoinMemberList(final int pageIndex, final int pageSize, final long id, final OnResponseListener<SnsActiveMemberPageInfo> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        final Api_SNSCENTER_PageInfo pageInfo = new Api_SNSCENTER_PageInfo();
        pageInfo.pageNo = pageIndex;
        pageInfo.pageSize = pageSize;
        Runnable requestThread = new Runnable() {
            @Override
            public void run() {
                try {
                    final Snscenter_GetActivityMemberList req = new Snscenter_GetActivityMemberList(id, pageInfo);
                    sendRequest(mContext, mApiContext, req);
                    if (req.getReturnCode() == ApiCode.SUCCESS) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                SnsActiveMemberPageInfo value = null;
                                if (req.getResponse() != null) {
                                    try {
                                        value = SnsActiveMemberPageInfo.deserialize(req.getResponse().serialize());
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
                        });
                    } else {
                        handlerException(req.getReturnCode(), req.getReturnMessage(), lsn);
                    }
                } catch (final Exception e) {
                    handlerRequestException(e, lsn);
                }
            }
        };
        NetThreadManager.getInstance().execute(requestThread);
    }

    /**
     * 对动态、直播点赞或者取消点赞
     *
     * @param outId
     * @param outType
     * @param type    0 点赞 1取消点赞
     * @param lsn
     */
    public void doPrasizeForum(final long outId, final String outType, final int type, final OnResponseListener<ComSupportInfo> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        final BaseRequest req;
        if (type == ValueConstants.PRAISE) {
            req = new Comcenter_AddNewPraiseToComment(outId,
                    outType);

        } else if (type == ValueConstants.UNPRAISE) {
            req = new Comcenter_EditPraiseToComment(outId,
                    outType);
        } else {
            ToastUtil.showToast(mContext, mContext.getString(R.string.error_params));
            return;
        }
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                ComSupportInfo value = null;
                if (req.getResponse() != null) {
                    try {
                        value = ComSupportInfo.deserialize(((Api_COMCENTER_ComSupportInfo) req.getResponse()).serialize());
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
     * 对帖子、直播进行评论
     *
     * @param lsn
     */
    public void doPostComment(CommetDetailInfo info, final OnResponseListener<CommentInfo> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        Api_COMCENTER_CommetDetailInfo i = null;
        try {
            i = Api_COMCENTER_CommetDetailInfo.deserialize(info.serialize().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final Comcenter_AddNewCommentToSubject req = new Comcenter_AddNewCommentToSubject(i);

        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                CommentInfo value = null;
                if (req.getResponse() != null) {
                    try {
                        value = CommentInfo.deserialize(req.getResponse().serialize());
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
     * 删除自己发表的帖子
     *
     * @param forumId
     * @param lsn
     */
    public void doDeleteForum(final long forumId, final OnResponseListener<Boolean> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        Runnable requestThread = new Runnable() {
            @Override
            public void run() {
                try {
                    final Snscenter_DelSubjectInfo req = new Snscenter_DelSubjectInfo(forumId);
                    sendRequest(mContext, mApiContext, req);
                    if (req.getReturnCode() == ApiCode.SUCCESS) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (lsn != null) {
                                    lsn.onComplete(true, req.getResponse().value, ErrorCode.STATUS_OK, req.getReturnMessage());
                                }
                            }
                        });
                    } else {
                        handlerException(req.getReturnCode(), req.getReturnMessage(), lsn);
                    }
                } catch (final Exception e) {
                    handlerRequestException(e, lsn);
                }
            }
        };
        NetThreadManager.getInstance().execute(requestThread);
    }

    /**
     * 删除自己发表的直播
     *
     * @param forumId
     * @param lsn
     */
    public void doDeleteLive(final long forumId, final OnResponseListener<Boolean> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        Runnable requestThread = new Runnable() {
            @Override
            public void run() {
                try {
                    final Snscenter_DelSubjectLiveInfo req = new Snscenter_DelSubjectLiveInfo(forumId);
                    sendRequest(mContext, mApiContext, req);
                    if (req.getReturnCode() == ApiCode.SUCCESS) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (lsn != null) {
                                    lsn.onComplete(true, req.getResponse().value, ErrorCode.STATUS_OK, req.getReturnMessage());
                                }
                            }
                        });
                    } else {
                        handlerException(req.getReturnCode(), req.getReturnMessage(), lsn);
                    }
                } catch (final Exception e) {
                    handlerRequestException(e, lsn);
                }
            }
        };
        NetThreadManager.getInstance().execute(requestThread);
    }


    /**
     * 删除自己发表的评论
     *
     * @param commentId
     * @param type      类型 DYNAMICCOM动态 LIVECOM直播
     * @param lsn
     */
    public void doDeleteComment(final long commentId, String type, final OnResponseListener<Boolean> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        final Comcenter_DelReploy req = new Comcenter_DelReploy(commentId, type);

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
     * 删除自己发表的直播下的评论
     *
     * @param reployId  评论id
     * @param subjectId 帖子的UID
     * @param lsn
     */
    public void doDeleteComment(final long reployId, long subjectId, final OnResponseListener<Boolean> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        final Snscenter_DelComment req = new Snscenter_DelComment(reployId, subjectId);

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
     * zhangzx@yingheying.com
     * The sample code of the new Network API call design
     */
    public void doGetActiveByOutUserIdPageDemo(final int pageIndex,
                                               final int pageSize,
                                               final long clubId,
                                               final OnResponseListener<ClubMemberInfoList> lsn) {
        if (checkSubmitStatus(lsn)) {
            return;
        }
        final Api_SNSCENTER_PageInfo pageInfo = new Api_SNSCENTER_PageInfo();
        pageInfo.pageNo = pageIndex;
        pageInfo.pageSize = pageSize;
        final Snscenter_GetActiveByOutUserIdPage req = new Snscenter_GetActiveByOutUserIdPage(pageInfo, clubId);

        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                ClubMemberInfoList value = null;
                if (req.getResponse() != null) {
                    try {
                        value = ClubMemberInfoList.deserialize(req.getResponse().serialize());
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
     * 分页获取俱乐部的成员列表
     *
     * @param pageIndex
     * @param pageSize
     * @param clubId
     * @param lsn
     */
    public void doGetActiveByOutUserIdPage(final int pageIndex, final int pageSize, final long clubId, final OnResponseListener<ClubMemberInfoList> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        final Api_SNSCENTER_PageInfo pageInfo = new Api_SNSCENTER_PageInfo();
        pageInfo.pageNo = pageIndex;
        pageInfo.pageSize = pageSize;
        Runnable requestThread = new Runnable() {
            @Override
            public void run() {
                try {
                    final Snscenter_GetActiveByOutUserIdPage req = new Snscenter_GetActiveByOutUserIdPage(pageInfo, clubId);
                    sendRequest(mContext, mApiContext, req);
                    if (req.getReturnCode() == ApiCode.SUCCESS) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                ClubMemberInfoList value = null;
                                if (req.getResponse() != null) {
                                    try {
                                        value = ClubMemberInfoList.deserialize(req.getResponse().serialize());
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
                        });
                    } else {
                        handlerException(req.getReturnCode(), req.getReturnMessage(), lsn);
                    }
                } catch (final Exception e) {
                    handlerRequestException(e, lsn);
                }
            }
        };
        NetThreadManager.getInstance().execute(requestThread);
    }

    /**
     * 根据活动id获取活动详情
     *
     * @param id
     * @param lsn
     */
    public void doGetActiveDetail(final long id, final boolean isVip, final OnResponseListener<SnsActivePageInfo> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        Runnable requestThread = new Runnable() {
            @Override
            public void run() {
                try {
                    if (isVip) {
                        final Snscenter_GetMemberActivityDetail req = new Snscenter_GetMemberActivityDetail(id);
                        sendRequest(mContext, mApiContext, req);
                        if (req.getReturnCode() == ApiCode.SUCCESS) {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    SnsActivePageInfo value = null;
                                    if (req.getResponse() != null) {
                                        try {
                                            value = SnsActivePageInfo.deserialize(req.getResponse().serialize());
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
                            });
                        } else {
                            handlerException(req.getReturnCode(), req.getReturnMessage(), lsn);
                        }
                    } else {
                        final Snscenter_GetClubActivityDetail req = new Snscenter_GetClubActivityDetail(id);
                        sendRequest(mContext, mApiContext, req);
                        if (req.getReturnCode() == ApiCode.SUCCESS) {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    SnsActivePageInfo value = null;
                                    if (req.getResponse() != null) {
                                        try {
                                            value = SnsActivePageInfo.deserialize(req.getResponse().serialize());
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
                            });
                        } else {
                            handlerException(req.getReturnCode(), req.getReturnMessage(), lsn);
                        }
                    }

                } catch (final Exception e) {
                    handlerRequestException(e, lsn);
                }
            }
        };
        NetThreadManager.getInstance().execute(requestThread);
    }

    /**
     * 分页获取俱乐部的活动列表
     *
     * @param pageIndex
     * @param pageSize
     * @param clubId
     * @param lsn
     */
    public void doGetActiveListByOutIdPage(final int pageIndex, final int pageSize, final long clubId, final OnResponseListener<SnsRimInfoList> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        final Api_SNSCENTER_PageInfo pageInfo = new Api_SNSCENTER_PageInfo();
        pageInfo.pageNo = pageIndex;
        pageInfo.pageSize = pageSize;
        Runnable requestThread = new Runnable() {
            @Override
            public void run() {
                try {
                    final Snscenter_GetActivityListByOutIdPage req = new Snscenter_GetActivityListByOutIdPage(pageInfo, clubId);
                    sendRequest(mContext, mApiContext, req);
                    if (req.getReturnCode() == ApiCode.SUCCESS) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                SnsRimInfoList value = null;
                                if (req.getResponse() != null) {
                                    try {
                                        value = SnsRimInfoList.deserialize(req.getResponse().serialize());
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
                        });
                    } else {
                        handlerException(req.getReturnCode(), req.getReturnMessage(), lsn);
                    }
                } catch (final Exception e) {
                    handlerRequestException(e, lsn);
                }
            }
        };
        NetThreadManager.getInstance().execute(requestThread);
    }

    /**
     * 分页获取俱乐部的动态列表
     *
     * @param pageIndex
     * @param pageSize
     * @param clubId
     * @param lsn
     */
    public void doGetSubjectListByClubId(final int pageIndex, final int pageSize, final long clubId, final OnResponseListener<SubjectInfoList> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        final Api_SNSCENTER_PageInfo pageInfo = new Api_SNSCENTER_PageInfo();
        pageInfo.pageNo = pageIndex;
        pageInfo.pageSize = pageSize;
        Runnable requestThread = new Runnable() {
            @Override
            public void run() {
                try {
                    final Snscenter_GetSubjectListByCludId req = new Snscenter_GetSubjectListByCludId(pageInfo, clubId);
                    sendRequest(mContext, mApiContext, req);
                    if (req.getReturnCode() == ApiCode.SUCCESS) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                SubjectInfoList value = null;
                                if (req.getResponse() != null) {
                                    try {
                                        value = SubjectInfoList.deserialize(req.getResponse().serialize());
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
                        });
                    } else {
                        handlerException(req.getReturnCode(), req.getReturnMessage(), lsn);
                    }
                } catch (final Exception e) {
                    handlerRequestException(e, lsn);
                }
            }
        };
        NetThreadManager.getInstance().execute(requestThread);
    }

    /**
     * 活动列表
     *
     * @param tagId
     * @param type      类型 LIVESUPTAG:直播 ACTIVETYTAG:活动 CLUBTAG:俱乐部 MEMBERTAG:会员
     * @param pageIndex
     * @param pageSize
     * @param lsn
     */
    public void doGetActiveListPage(final long tagId,
                                    final String type,
                                    final int pageIndex,
                                    final int pageSize,
                                    final OnResponseListener<SnsActivePageInfoList> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        final Api_SNSCENTER_PageInfo pageInfo = new Api_SNSCENTER_PageInfo();
        pageInfo.pageNo = pageIndex;
        pageInfo.pageSize = pageSize;
        Runnable requestThread = new Runnable() {
            @Override
            public void run() {
                try {
                    final Snscenter_GetAllActivityListPage req = new Snscenter_GetAllActivityListPage(pageInfo, tagId, type);
                    sendRequest(mContext, mApiContext, req);
                    if (req.getReturnCode() == ApiCode.SUCCESS) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                SnsActivePageInfoList value = null;
                                if (req.getResponse() != null) {
                                    try {
                                        value = SnsActivePageInfoList.deserialize(req.getResponse().serialize());
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
                        });
                    } else {
                        handlerException(req.getReturnCode(), req.getReturnMessage(), lsn);
                    }
                } catch (final Exception e) {
                    handlerRequestException(e, lsn);
                }
            }
        };
        NetThreadManager.getInstance().execute(requestThread);
    }

    /**
     * 首页活动列表以及首页搜索：按标题搜索
     *
     * @param pageIndex
     * @param pageSize
     * @param title
     * @param lsn
     */
    public void doGetActiveListByTitlePage(final int pageIndex, final int pageSize, final String title, final OnResponseListener<SnsActivePageInfoList> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        final Api_SNSCENTER_PageInfo pageInfo = new Api_SNSCENTER_PageInfo();
        pageInfo.pageNo = pageIndex;
        pageInfo.pageSize = pageSize;
        Runnable requestThread = new Runnable() {
            @Override
            public void run() {
                try {
                    final Snscenter_GetActivityListByTitlePage req = new Snscenter_GetActivityListByTitlePage(pageInfo, title);
                    sendRequest(mContext, mApiContext, req);
                    if (req.getReturnCode() == ApiCode.SUCCESS) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                SnsActivePageInfoList value = null;
                                if (req.getResponse() != null) {
                                    try {
                                        value = SnsActivePageInfoList.deserialize(req.getResponse().serialize());
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
                        });
                    } else {
                        handlerException(req.getReturnCode(), req.getReturnMessage(), lsn);
                    }
                } catch (final Exception e) {
                    handlerRequestException(e, lsn);
                }
            }
        };
        NetThreadManager.getInstance().execute(requestThread);
    }

    /**
     * 获取俱乐部详情
     *
     * @param clubId
     * @param lsn
     */
    public void doGetClubDetail(final long clubId, final OnResponseListener<SnsActivePageInfoList> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        Runnable requestThread = new Runnable() {
            @Override
            public void run() {
                try {
                    final Snscenter_GetClubInfo req = new Snscenter_GetClubInfo(clubId);
                    sendRequest(mContext, mApiContext, req);
                    if (req.getReturnCode() == ApiCode.SUCCESS) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                SnsActivePageInfoList value = null;
                                if (req.getResponse() != null) {
                                    try {
                                        value = SnsActivePageInfoList.deserialize(req.getResponse().serialize());
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
                        });
                    } else {
                        handlerException(req.getReturnCode(), req.getReturnMessage(), lsn);
                    }
                } catch (final Exception e) {
                    handlerRequestException(e, lsn);
                }
            }
        };
        NetThreadManager.getInstance().execute(requestThread);
    }

    /**
     * 首页俱乐部列表以及首页搜索： 按名称
     *
     * @param pageIndex
     * @param pageSize
     * @param clubName
     * @param lsn
     */
    public void doGetClubInfoListPage(final int pageIndex, final int pageSize, final String clubName, final OnResponseListener<ClubInfoList> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        final Api_SNSCENTER_PageInfo pageInfo = new Api_SNSCENTER_PageInfo();
        pageInfo.pageNo = pageIndex;
        pageInfo.pageSize = pageSize;
        Runnable requestThread = new Runnable() {
            @Override
            public void run() {
                try {
                    final Snscenter_GetClubInfoListByClubNamePage req = new Snscenter_GetClubInfoListByClubNamePage(pageInfo, clubName);
                    sendRequest(mContext, mApiContext, req);
                    if (req.getReturnCode() == ApiCode.SUCCESS) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                ClubInfoList value = null;
                                if (req.getResponse() != null) {
                                    try {
                                        value = ClubInfoList.deserialize(req.getResponse().serialize());
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
                        });
                    } else {
                        handlerException(req.getReturnCode(), req.getReturnMessage(), lsn);
                    }
                } catch (final Exception e) {
                    handlerRequestException(e, lsn);
                }
            }
        };
        NetThreadManager.getInstance().execute(requestThread);
    }

    /**
     * 加入俱乐部
     *
     * @param clubId
     * @param lsn
     */
    public void doJoinClubMember(final long clubId, final OnResponseListener<ClubMemberInfo> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        Runnable requestThread = new Runnable() {
            @Override
            public void run() {
                try {
                    final Snscenter_JoinClubMember req = new Snscenter_JoinClubMember(clubId);
                    sendRequest(mContext, mApiContext, req);
                    if (req.getReturnCode() == ApiCode.SUCCESS) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                ClubMemberInfo value = null;
                                if (req.getResponse() != null) {
                                    try {
                                        value = ClubMemberInfo.deserialize(req.getResponse().serialize());
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
                        });
                    } else {
                        handlerException(req.getReturnCode(), req.getReturnMessage(), lsn);
                    }
                } catch (final Exception e) {
                    handlerRequestException(e, lsn);
                }
            }
        };
        NetThreadManager.getInstance().execute(requestThread);
    }

    /**
     * 退出俱乐部
     *
     * @param clubId
     * @param lsn
     */
    public void doExitClubMember(final long clubId, final OnResponseListener<ClubMemberInfo> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        Runnable requestThread = new Runnable() {
            @Override
            public void run() {
                try {
                    final Snscenter_ExitClubMember req = new Snscenter_ExitClubMember(clubId);
                    sendRequest(mContext, mApiContext, req);
                    if (req.getReturnCode() == ApiCode.SUCCESS) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                ClubMemberInfo value = null;
                                if (req.getResponse() != null) {
                                    try {
                                        value = ClubMemberInfo.deserialize(req.getResponse().serialize());
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
                        });
                    } else {
                        handlerException(req.getReturnCode(), req.getReturnMessage(), lsn);
                    }
                } catch (final Exception e) {
                    handlerRequestException(e, lsn);
                }
            }
        };
        NetThreadManager.getInstance().execute(requestThread);
    }

    /**
     * 发表新的动态
     *
     * @param subjectInfo
     * @param lsn
     */
    public void doPublishNewSubject(final SubjectDynamic subjectInfo, final OnResponseListener<SubjectDetail> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        if (subjectInfo == null) {
            return;
        }
        Runnable requestThread = new Runnable() {
            @Override
            public void run() {
                try {
                    final Snscenter_PublishNewDynamicSubject req = new Snscenter_PublishNewDynamicSubject(
                            Api_SNSCENTER_SubjectDynamic.deserialize(subjectInfo.serialize().toString()));
                    sendRequest(mContext, mApiContext, req);
                    if (req.getReturnCode() == ApiCode.SUCCESS) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                SubjectDetail value = null;
                                if (req.getResponse() != null) {
                                    try {
                                        value = SubjectDetail.deserialize(req.getResponse().serialize());
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
                        });
                    } else {
                        handlerException(req.getReturnCode(), req.getReturnMessage(), lsn);
                    }
                } catch (final Exception e) {
                    handlerRequestException(e, lsn);
                }
            }
        };
        NetThreadManager.getInstance().execute(requestThread);
    }

    /**
     * 发表新的直播
     *
     * @param subjectInfo
     * @param lsn
     */
    public void doPublishNewSubjectLive(final SubjectLive subjectInfo, final OnResponseListener<SubjectDetail> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        if (subjectInfo == null) {
            return;
        }
        Runnable requestThread = new Runnable() {
            @Override
            public void run() {
                try {
                    final Snscenter_PublishNewSubjectLive req = new Snscenter_PublishNewSubjectLive(
                            Api_SNSCENTER_SubjectLive.deserialize(subjectInfo.serialize()));
                    sendRequest(mContext, mApiContext, req);
                    if (req.getReturnCode() == ApiCode.SUCCESS) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                SubjectDetail value = null;
                                if (req.getResponse() != null) {
                                    try {
                                        value = SubjectDetail.deserialize(req.getResponse().serialize());
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
                        });
                    } else {
                        handlerException(req.getReturnCode(), req.getReturnMessage(), lsn);
                    }
                } catch (final Exception e) {
                    handlerRequestException(e, lsn);
                }
            }
        };
        NetThreadManager.getInstance().execute(requestThread);
    }

    /**
     * 分页获取直播（根据标签ID查询）
     *
     * @param tagID
     * @param pageIndex
     * @param pageSize
     * @param lsn
     */
    public void doGetLiveListByTagId(final long tagID, final int pageIndex, final int pageSize, final OnResponseListener<SubjectInfoList> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        final Api_SNSCENTER_PageInfo pageInfo = new Api_SNSCENTER_PageInfo();
        pageInfo.pageNo = pageIndex;
        pageInfo.pageSize = pageSize;
        Runnable requestThread = new Runnable() {
            @Override
            public void run() {
                try {
                    final Snscenter_GetAllLiveListPage req = new Snscenter_GetAllLiveListPage(pageInfo, tagID);
                    sendRequest(mContext, mApiContext, req);
                    if (req.getReturnCode() == ApiCode.SUCCESS) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                SubjectInfoList value = null;
                                if (req.getResponse() != null) {
                                    try {
                                        value = SubjectInfoList.deserialize(req.getResponse().serialize());
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
                        });
                    } else {
                        handlerException(req.getReturnCode(), req.getReturnMessage(), lsn);
                    }
                } catch (final Exception e) {
                    handlerRequestException(e, lsn);
                }
            }
        };
        NetThreadManager.getInstance().execute(requestThread);
    }

    /**
     * 获取我的直播列表
     *
     * @param lsn
     */
    public void doGetLiveListByUserID(final LiveRecordListUserIdQuery api_live_liveRecordListUserIdQuery, final OnResponseListener<LiveRecordAPIPageResult> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        Runnable requestThread = new Runnable() {
            @Override
            public void run() {
                try {
                    final Live_GetLiveListByUserId req = new Live_GetLiveListByUserId(Api_LIVE_LiveRecordListUserIdQuery.deserialize(api_live_liveRecordListUserIdQuery.serialize()));
                    sendRequest(mContext, mApiContext, req);
                    if (req.getReturnCode() == ApiCode.SUCCESS) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                LiveRecordAPIPageResult value = null;
                                if (req.getResponse() != null) {
                                    try {
                                        value = LiveRecordAPIPageResult.deserialize(req.getResponse().serialize());
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
                        });
                    } else {
                        handlerException(req.getReturnCode(), req.getReturnMessage(), lsn);
                    }
                } catch (final Exception e) {
                    handlerRequestException(e, lsn);
                }
            }
        };
        NetThreadManager.getInstance().execute(requestThread);
    }

    /**
     * 获取我的的动态
     *
     * @param pageIndex
     * @param pageSize
     * @param userId
     * @param lsn
     */
    public void doGetSubjectList(final int pageIndex, final int pageSize, final long userId, final OnResponseListener<SubjectInfoList> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        final Api_SNSCENTER_PageInfo pageInfo = new Api_SNSCENTER_PageInfo();
        pageInfo.pageNo = pageIndex;
        pageInfo.pageSize = pageSize;
        Runnable requestThread = new Runnable() {
            @Override
            public void run() {
                try {
                    final Snscenter_GetMySubjectList req = new Snscenter_GetMySubjectList(pageInfo, userId);
                    sendRequest(mContext, mApiContext, req);
                    if (req.getReturnCode() == ApiCode.SUCCESS) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                SubjectInfoList value = null;
                                if (req.getResponse() != null) {
                                    try {
                                        value = SubjectInfoList.deserialize(req.getResponse().serialize());
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
                        });
                    } else {
                        handlerException(req.getReturnCode(), req.getReturnMessage(), lsn);
                    }
                } catch (final Exception e) {
                    handlerRequestException(e, lsn);
                }
            }
        };
        NetThreadManager.getInstance().execute(requestThread);
    }

    /**
     * 获取我的俱乐部列表(根据用户ID)
     *
     * @param pageIndex
     * @param pageSize
     * @param lsn
     */
    public void doGetClubList(final long userId, final int pageIndex, final int pageSize, final OnResponseListener<ClubInfoList> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        final Api_SNSCENTER_PageInfo pageInfo = new Api_SNSCENTER_PageInfo();
        pageInfo.pageNo = pageIndex;
        pageInfo.pageSize = pageSize;

        Runnable requestThread = new Runnable() {
            @Override
            public void run() {
                try {
                    final Snscenter_GetMyClubInfoList req = new Snscenter_GetMyClubInfoList(pageInfo, userId);
                    sendRequest(mContext, mApiContext, req);
                    if (req.getReturnCode() == ApiCode.SUCCESS) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                ClubInfoList value = null;
                                if (req.getResponse() != null) {
                                    try {
                                        value = ClubInfoList.deserialize(req.getResponse().serialize());
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
                        });
                    } else {
                        handlerException(req.getReturnCode(), req.getReturnMessage(), lsn);
                    }
                } catch (final Exception e) {
                    handlerRequestException(e, lsn);
                }
            }
        };
        NetThreadManager.getInstance().execute(requestThread);
    }

    /**
     * 根据类型获取俱乐部列表
     *
     * @param type
     * @param pageIndex
     * @param pageSize
     * @param lsn
     */
    public void doGetClubListByType(final long type, final int pageIndex, final int pageSize, final OnResponseListener<ClubInfoList> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        final Api_SNSCENTER_PageInfo pageInfo = new Api_SNSCENTER_PageInfo();
        pageInfo.pageNo = pageIndex;
        pageInfo.pageSize = pageSize;

        Runnable requestThread = new Runnable() {
            @Override
            public void run() {
                try {
                    final Snscenter_GetClubListPageByTagId req = new Snscenter_GetClubListPageByTagId(pageInfo, type);
                    sendRequest(mContext, mApiContext, req);
                    if (req.getReturnCode() == ApiCode.SUCCESS) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                ClubInfoList value = null;
                                if (req.getResponse() != null) {
                                    try {
                                        value = ClubInfoList.deserialize(req.getResponse().serialize());
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
                        });
                    } else {
                        handlerException(req.getReturnCode(), req.getReturnMessage(), lsn);
                    }
                } catch (final Exception e) {
                    handlerRequestException(e, lsn);
                }
            }
        };
        NetThreadManager.getInstance().execute(requestThread);
    }

    /**
     * 获取直播详情
     *
     * @param liveId
     * @param lsn
     */
    public void doGetLiveDetail(final long liveId, final OnResponseListener<SubjectInfo> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        Runnable requestThread = new Runnable() {
            @Override
            public void run() {
                try {
                    final Snscenter_GetSubjectLiveDetailById req = new Snscenter_GetSubjectLiveDetailById(liveId);
                    sendRequest(mContext, mApiContext, req);
                    if (req.getReturnCode() == ApiCode.SUCCESS) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                SubjectInfo value = null;
                                if (req.getResponse() != null) {
                                    try {
                                        value = SubjectInfo.deserialize(req.getResponse().serialize());
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
                        });
                    } else {
                        handlerException(req.getReturnCode(), req.getReturnMessage(), lsn);
                    }
                } catch (final Exception e) {
                    handlerRequestException(e, lsn);
                }
            }
        };
        NetThreadManager.getInstance().execute(requestThread);
    }

    /**
     * 获取动态详情
     *
     * @param liveId
     * @param lsn
     */
    public void doGetDynamicDetail(final long liveId, final OnResponseListener<SubjectInfo> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        Runnable requestThread = new Runnable() {
            @Override
            public void run() {
                try {
                    final Snscenter_GetSubjectDynamicDetail req = new Snscenter_GetSubjectDynamicDetail(liveId);
                    sendRequest(mContext, mApiContext, req);
                    if (req.getReturnCode() == ApiCode.SUCCESS) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                SubjectInfo value = null;
                                if (req.getResponse() != null) {
                                    try {
                                        value = SubjectInfo.deserialize(req.getResponse().serialize());
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
                        });
                    } else {
                        handlerException(req.getReturnCode(), req.getReturnMessage(), lsn);
                    }
                } catch (final Exception e) {
                    handlerRequestException(e, lsn);
                }
            }
        };
        NetThreadManager.getInstance().execute(requestThread);
    }

    /**
     * 标签列表包含我的标签
     *
     * @param outType
     * @param pageIndex
     * @param pageSize
     * @param lsn
     */
    public void doSearchTag(final String outType, final int pageIndex, final int pageSize, final OnResponseListener<ComUserAndTagInfoList> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        final Api_COMCENTER_PageInfo pageInfo = new Api_COMCENTER_PageInfo();
        pageInfo.pageNo = pageIndex;
        pageInfo.pageSize = pageSize;
        Runnable requestThread = new Runnable() {
            @Override
            public void run() {
                try {
                    final Comcenter_GetTagInfoPageByOutTypeAndUserId req = new Comcenter_GetTagInfoPageByOutTypeAndUserId(outType, pageInfo);
                    sendRequest(mContext, mApiContext, req);
                    if (req.getReturnCode() == ApiCode.SUCCESS) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                ComUserAndTagInfoList value = null;
                                if (req.getResponse() != null) {
                                    try {
                                        value = ComUserAndTagInfoList.deserialize(req.getResponse().serialize());
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
                        });
                    } else {
                        handlerException(req.getReturnCode(), req.getReturnMessage(), lsn);
                    }
                } catch (Exception e) {
                    handlerRequestException(e, lsn);
                }
            }
        };
        NetThreadManager.getInstance().execute(requestThread);
    }

    /**
     * 根据输入内容搜索标签
     *
     * @param searchValue
     * @param pageIndex
     * @param pageSize
     * @param lsn
     */
    public void doSearchTagByName(final String searchValue, final int pageIndex, final int pageSize, final OnResponseListener<ComTagInfoList> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        final Api_COMCENTER_PageInfo pageInfo = new Api_COMCENTER_PageInfo();
        pageInfo.pageNo = pageIndex;
        pageInfo.pageSize = pageSize;
        Runnable requestThread = new Runnable() {
            @Override
            public void run() {
                try {
                    final Comcenter_FindTagList req = new Comcenter_FindTagList(searchValue, pageInfo);
                    sendRequest(mContext, mApiContext, req);
                    if (req.getReturnCode() == ApiCode.SUCCESS) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                ComTagInfoList value = null;
                                if (req.getResponse() != null) {
                                    try {
                                        value = ComTagInfoList.deserialize(req.getResponse().serialize());
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
                        });
                    } else {
                        handlerException(req.getReturnCode(), req.getReturnMessage(), lsn);
                    }
                } catch (Exception e) {
                    handlerRequestException(e, lsn);
                }
            }
        };
        NetThreadManager.getInstance().execute(requestThread);
    }


    /**
     * 分页获取某个话题的评论
     *
     * @param subjectId 话题id
     * @param outType   类型
     * @param pageIndex
     * @param pageSize
     * @param lsn
     */
    public void doGetCommentList(final long subjectId, final String outType, final int pageIndex, final int pageSize, final OnResponseListener<CommentInfoList> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        final Api_COMCENTER_PageInfo pageInfo = new Api_COMCENTER_PageInfo();
        pageInfo.pageNo = pageIndex;
        pageInfo.pageSize = pageSize;

        Runnable requestThread = new Runnable() {
            @Override
            public void run() {
                try {
                    final Comcenter_PageGetComments req = new Comcenter_PageGetComments(subjectId, outType, pageInfo);
                    sendRequest(mContext, mApiContext, req);
                    if (req.getReturnCode() == ApiCode.SUCCESS) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                CommentInfoList value = null;
                                if (req.getResponse() != null) {
                                    try {
                                        value = CommentInfoList.deserialize(req.getResponse().serialize());
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
                        });
                    } else {
                        handlerException(req.getReturnCode(), req.getReturnMessage(), lsn);
                    }
                } catch (final Exception e) {
                    handlerRequestException(e, lsn);
                }
            }
        };
        NetThreadManager.getInstance().execute(requestThread);
    }

    /**
     * 查询点赞信息列表
     *
     * @param outId     内容id
     * @param outType   类型
     * @param pageIndex
     * @param pageSize
     * @param lsn
     */
    public void doGetApraiseList(final long outId, final String outType, final int pageIndex, final int pageSize, final OnResponseListener<SupportUserInfoList> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        final Api_COMCENTER_PageInfo pageInfo = new Api_COMCENTER_PageInfo();
        pageInfo.pageNo = pageIndex;
        pageInfo.pageSize = pageSize;
        Runnable requestThread = new Runnable() {
            @Override
            public void run() {
                try {
                    final Comcenter_FindSupportList req = new Comcenter_FindSupportList(outId, outType, pageInfo);
                    sendRequest(mContext, mApiContext, req);
                    if (req.getReturnCode() == ApiCode.SUCCESS) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                SupportUserInfoList value = null;
                                if (req.getResponse() != null) {
                                    try {
                                        value = SupportUserInfoList.deserialize(req.getResponse().serialize());
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
                        });
                    } else {
                        handlerException(req.getReturnCode(), req.getReturnMessage(), lsn);
                    }
                } catch (final Exception e) {
                    handlerRequestException(e, lsn);
                }
            }
        };
        NetThreadManager.getInstance().execute(requestThread);
    }

    /**
     * 查询游记列表
     *
     * @param pageIndex
     * @param pageSize
     * @param lsn
     */
    public void doGetTravelSpecialListPage(final int pageIndex, final int pageSize, final OnResponseListener<TravelSpecialInfoList> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        final Api_SNSCENTER_PageInfo pageInfo = new Api_SNSCENTER_PageInfo();
        pageInfo.pageNo = pageIndex;
        pageInfo.pageSize = pageSize;
        Runnable requestThread = new Runnable() {
            @Override
            public void run() {
                try {
                    final Snscenter_GetTravelSpecialListPage req = new Snscenter_GetTravelSpecialListPage(pageInfo);
                    sendRequest(mContext, mApiContext, req);
                    if (req.getReturnCode() == ApiCode.SUCCESS) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                TravelSpecialInfoList value = null;
                                if (req.getResponse() != null) {
                                    try {
                                        value = TravelSpecialInfoList.deserialize(req.getResponse().serialize());
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
                        });
                    } else {
                        handlerException(req.getReturnCode(), req.getReturnMessage(), lsn);
                    }
                } catch (final Exception e) {
                    handlerRequestException(e, lsn);
                }
            }
        };
        NetThreadManager.getInstance().execute(requestThread);
    }

    /**
     * 获取游记详情
     *
     * @param travalNotesId
     * @param lsn
     */
    public void doGetTravelSpecialDetail(final long travalNotesId, final OnResponseListener<TravelSpecialInfo> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        Runnable requestThread = new Runnable() {
            @Override
            public void run() {
                try {
                    final Snscenter_GetTravelSpecialDetail req = new Snscenter_GetTravelSpecialDetail(travalNotesId);
                    sendRequest(mContext, mApiContext, req);
                    if (req.getReturnCode() == ApiCode.SUCCESS) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                TravelSpecialInfo value = null;
                                if (req.getResponse() != null) {
                                    try {
                                        value = TravelSpecialInfo.deserialize(req.getResponse().serialize());
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
                        });
                    } else {
                        handlerException(req.getReturnCode(), req.getReturnMessage(), lsn);
                    }
                } catch (final Exception e) {
                    handlerRequestException(e, lsn);
                }
            }
        };
        NetThreadManager.getInstance().execute(requestThread);
    }

    /**
     * 屏蔽人或者动态
     *
     * @param shieldType
     * @param id
     * @param lsn
     */
    public void doAddUserShield(final String shieldType, final long id, final OnResponseListener<Boolean> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        Runnable requestThread = new Runnable() {
            @Override
            public void run() {
                try {
                    final Snscenter_AddUserShield req = new Snscenter_AddUserShield(shieldType, id);
                    sendRequest(mContext, mApiContext, req);
                    if (req.getReturnCode() == ApiCode.SUCCESS) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (lsn != null) {
                                    lsn.onComplete(true, req.getResponse().value, ErrorCode.STATUS_OK, req.getReturnMessage());
                                }
                            }
                        });
                    } else {
                        handlerException(req.getReturnCode(), req.getReturnMessage(), lsn);
                    }
                } catch (final Exception e) {
                    handlerRequestException(e, lsn);
                }
            }
        };
        NetThreadManager.getInstance().execute(requestThread);
    }

    /**
     * 查询评价信息列表
     *
     * @param param
     * @param lsn
     */
    public void doGetRateInfoPageList(RateInfoQuery param,
                                      final OnResponseListener<RateInfoList> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn) || param == null) {
            return;
        }

        final Comcenter_GetRateInfoPageList req = new Comcenter_GetRateInfoPageList(Api_COMCENTER_RateInfoQuery.deserialize(param.serialize()));

        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                RateInfoList value = null;
                if (req.getResponse() != null) {
                    try {
                        value = RateInfoList.deserialize(req.getResponse().serialize());
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
     * 查询评价维度
     *
     * @param outType 外部类型： HOTEL: 酒店，VIEW：景区，LINE：线路，LOCAL：同城活动，PRODUCT：实物商品
     * @param lsn
     * @throws JSONException
     */
    public void doGetRateDimensionList(String outType,
                                       final OnResponseListener<DimensionList> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn) || outType == null) {
            return;
        }

        final Comcenter_GetRateDimensionList req = new Comcenter_GetRateDimensionList(
                outType);

        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                DimensionList value = null;
                if (req.getResponse() != null) {
                    try {
                        value = DimensionList.deserialize(req.getResponse().serialize());
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
     * 查询评价数量
     *
     * @param param
     * @param lsn
     * @throws JSONException
     */
    public void doGetRateCountByOutId(RateCountQuery param,
                                      final OnResponseListener<RateCountInfo> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn) || param == null) {
            return;
        }

        final Comcenter_GetRateCountByOutId req = new Comcenter_GetRateCountByOutId(Api_COMCENTER_RateCountQuery.deserialize(param.serialize()));

        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                RateCountInfo value = null;
                if (req.getResponse() != null) {
                    try {
                        value = RateCountInfo.deserialize(req.getResponse().serialize());
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
     * 查询评价条件
     *
     * @param id
     * @param outType 外部类型： HOTEL: 酒店，VIEW：景区，LINE：线路，LOCAL：同城活动，PRODUCT：实物商品
     * @param lsn
     * @throws JSONException
     */
    public void doGetRateCaseList(long id,
                                  String outType,
                                  final OnResponseListener<RateCaseList> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        final Comcenter_GetRateCaseList req = new Comcenter_GetRateCaseList(id, outType);

        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                RateCaseList value = null;
                if (req.getResponse() != null) {
                    try {
                        value = RateCaseList.deserialize(req.getResponse().serialize());
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
     * 查询实物商品评价信息列表
     *
     * @param param
     * @param lsn
     * @throws JSONException
     */
    public void doGetProductRateInfoPageList(ProductRateInfoQuery param,
                                             final OnResponseListener<ProductRateInfoList> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        final Comcenter_GetProductRateInfoPageList req = new Comcenter_GetProductRateInfoPageList(
                Api_COMCENTER_ProductRateInfoQuery.deserialize(param.serialize()));

        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                ProductRateInfoList value = null;
                if (req.getResponse() != null) {
                    try {
                        value = ProductRateInfoList.deserialize(req.getResponse().serialize());
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
     * 取消关注
     *
     * @param followedUserId
     * @param lsn
     * @throws JSONException
     */
    public void doRemoveFollower(long followedUserId, final OnResponseListener<Boolean> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        final Snscenter_RemoveFollower req = new Snscenter_RemoveFollower(followedUserId);

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
     * 添加关注
     *
     * @param followedUserId
     * @param lsn
     * @throws JSONException
     */
    public void doAddFollower(long followedUserId, final OnResponseListener<Boolean> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        final Snscenter_AddFollower req = new Snscenter_AddFollower(followedUserId);

        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                FollowRetInfo value = null;
                if (req.getResponse() != null) {
                    try {
                        value = FollowRetInfo.deserialize(req.getResponse().serialize());
                    } catch (JSONException e) {
                        value = new FollowRetInfo();
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    }
                } else {
                    value = new FollowRetInfo();
                }
                if (lsn != null) {
                    if (ValueConstants.FOLLOW_SUCCESS.equals(value.type)) {
                        lsn.onComplete(true, true, ErrorCode.STATUS_OK, req.getReturnMessage());
                    } else {
                        lsn.onComplete(true, false, ErrorCode.STATUS_OK, req.getReturnMessage());
                    }
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
     * 获取UGC详情
     *
     * @param ugcId
     * @param lsn
     * @throws JSONException
     */
    public void doGetUGCDetail(long ugcId, final OnResponseListener<UgcInfoResult> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        final Snscenter_GetUGCDetail req = new Snscenter_GetUGCDetail(ugcId);

        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                UgcInfoResult value = null;
                if (req.getResponse() != null) {
                    try {
                        value = UgcInfoResult.deserialize(req.getResponse().serialize());
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
     * 获取UGC列表
     *
     * @param pageIndex
     * @param pageSize
     * @param type      查询类型 1,全部,2,关注
     * @param lsn
     * @throws JSONException
     */
    public void doGetUGCPageList(final int pageIndex, final int pageSize, int type, final OnResponseListener<UgcInfoResultList> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        final Api_SNSCENTER_PageInfo pageInfo = new Api_SNSCENTER_PageInfo();
        pageInfo.pageNo = pageIndex;
        pageInfo.traceId = UUID.randomUUID().toString();
        pageInfo.pageSize = pageSize;

        Api_SNSCENTER_TopicPageListQuery topicPageListQuery = new Api_SNSCENTER_TopicPageListQuery();
        topicPageListQuery.pageInfo = pageInfo;
        topicPageListQuery.type = type;

        final Snscenter_GetUGCPageList req = new Snscenter_GetUGCPageList(topicPageListQuery);

        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                UgcInfoResultList value = null;
                if (req.getResponse() != null) {
                    try {
                        value = UgcInfoResultList.deserialize(req.getResponse().serialize());
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
     * 查询用户的UGC列表
     *
     * @param pageIndex
     * @param pageSize
     * @param createId
     * @param lsn
     * @throws JSONException
     */
    public void doGetUserUGCPageList(final int pageIndex, final int pageSize, long createId, final OnResponseListener<UgcInfoResultList> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        final Api_SNSCENTER_PageInfo pageInfo = new Api_SNSCENTER_PageInfo();
        pageInfo.pageNo = pageIndex;
        pageInfo.pageSize = pageSize;
        final Snscenter_GetUserUGCPageList req = new Snscenter_GetUserUGCPageList(pageInfo, createId);

        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                UgcInfoResultList value = null;
                if (req.getResponse() != null) {
                    try {
                        value = UgcInfoResultList.deserialize(req.getResponse().serialize());
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
     * 查询我的粉丝、关注、ugc数量
     *
     * @param theUserId
     * @param lsn
     * @throws JSONException
     */
    public void doQueryUserSnsCountInfo(long theUserId, final OnResponseListener<SnsCountInfo> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        final Snscenter_QueryUserSnsCountInfo req = new Snscenter_QueryUserSnsCountInfo(theUserId);

        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                SnsCountInfo value = null;
                if (req.getResponse() != null) {
                    try {
                        value = SnsCountInfo.deserialize(req.getResponse().serialize());
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
     * 模糊查询话题标题
     *
     * @param pageIndex
     * @param pageSize
     * @param titleLike
     * @param lsn
     * @throws JSONException
     */
    public void doSearchTopicTitlePageList(final int pageIndex, final int pageSize, String titleLike, final OnResponseListener<TopicInfoResultList> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        final Api_SNSCENTER_PageInfo pageInfo = new Api_SNSCENTER_PageInfo();
        pageInfo.pageNo = pageIndex;
        pageInfo.pageSize = pageSize;

        final Api_SNSCENTER_TopicPageQuery topicPageQuery = new Api_SNSCENTER_TopicPageQuery();
        topicPageQuery.pageInfo = pageInfo;
        topicPageQuery.titleLike = titleLike;

        final Snscenter_SearchTopicTitlePageList req = new Snscenter_SearchTopicTitlePageList(topicPageQuery);

        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                TopicInfoResultList value = null;
                if (req.getResponse() != null) {
                    try {
                        value = TopicInfoResultList.deserialize(req.getResponse().serialize());
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
     * 查看话题详情
     *
     * @param lsn
     * @throws JSONException
     */
    public void doGetTopicInfo(String topicTitle, final OnResponseListener<TopicInfoResult> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        final Snscenter_GetTopicInfo req = new Snscenter_GetTopicInfo(topicTitle);

        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                TopicInfoResult value = null;
                if (req.getResponse() != null) {
                    try {
                        value = TopicInfoResult.deserialize(req.getResponse().serialize());
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
     * 查看话题列表
     *
     * @param pageNo
     * @param pageSize
     * @param type     1.推荐 2.全部
     * @param startNum 页码
     * @param lsn
     * @throws JSONException
     */
    public void doGetTopicPageList(int pageNo, int pageSize, int type, long startNum, final OnResponseListener<TopicInfoResultList> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        final Api_SNSCENTER_PageInfo pageInfo = new Api_SNSCENTER_PageInfo();
        pageInfo.pageNo = pageNo;
        pageInfo.pageSize = pageSize;

        Api_SNSCENTER_TopicSugPageListQuery topicSugPageListQuery = new Api_SNSCENTER_TopicSugPageListQuery();
        topicSugPageListQuery.pageInfo = pageInfo;
        topicSugPageListQuery.startNum = startNum;
        topicSugPageListQuery.type = type;

        final Snscenter_GetTopicPageList req = new Snscenter_GetTopicPageList(topicSugPageListQuery);

        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                TopicInfoResultList value = null;
                if (req.getResponse() != null) {
                    try {
                        value = TopicInfoResultList.deserialize(req.getResponse().serialize());
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
     * 发布UGC
     *
     * @param ugcInfo
     * @param lsn
     * @throws JSONException
     */
    public void doAddUGC(Api_SNSCENTER_UgcInfo ugcInfo, final OnResponseListener<Boolean> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        final Snscenter_AddUGC req = new Snscenter_AddUGC(ugcInfo);

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
     * 删除UGC
     *
     * @param ugcId
     * @param lsn
     * @throws JSONException
     */
    public void doDelUGC(long ugcId, final OnResponseListener<Boolean> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        final Snscenter_DelUGC req = new Snscenter_DelUGC(ugcId);

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
     * 查询指定用户的粉丝列表
     *
     * @param userId
     * @param pageNo
     * @param pageSize
     * @param lsn
     * @throws JSONException
     */
    public void doGetFansList(long userId, int pageNo, int pageSize, final OnResponseListener<FollowerPageListResult> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        final Api_SNSCENTER_PageInfo pageInfo = new Api_SNSCENTER_PageInfo();
        pageInfo.pageNo = pageNo;
        pageInfo.pageSize = pageSize;

        Api_SNSCENTER_FollowPageQueryInfo fansPageQueryInfo = new Api_SNSCENTER_FollowPageQueryInfo();
        fansPageQueryInfo.pageInfo = pageInfo;
        fansPageQueryInfo.userId = userId;

        final Snscenter_GetFansList req = new Snscenter_GetFansList(fansPageQueryInfo);

        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                FollowerPageListResult value = null;
                if (req.getResponse() != null) {
                    try {
                        value = FollowerPageListResult.deserialize(req.getResponse().serialize());
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
     * 查询指定用户的关注列表
     *
     * @param userId
     * @param pageNo
     * @param pageSize
     * @param lsn
     * @throws JSONException
     */
    public void doGetFollowerList(long userId, int pageNo, int pageSize, final OnResponseListener<FollowerPageListResult> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        final Api_SNSCENTER_PageInfo pageInfo = new Api_SNSCENTER_PageInfo();
        pageInfo.pageNo = pageNo;
        pageInfo.pageSize = pageSize;

        Api_SNSCENTER_FollowPageQueryInfo fansPageQueryInfo = new Api_SNSCENTER_FollowPageQueryInfo();
        fansPageQueryInfo.pageInfo = pageInfo;
        fansPageQueryInfo.userId = userId;

        final Snscenter_GetFollowerList req = new Snscenter_GetFollowerList(fansPageQueryInfo);

        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                FollowerPageListResult value = null;
                if (req.getResponse() != null) {
                    try {
                        value = FollowerPageListResult.deserialize(req.getResponse().serialize());
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
     * 查询话题下的UGC列表
     *
     * @param pageNo
     * @param pageSize
     * @param lsn
     * @throws JSONException
     */
    public void doGetTopicUGCPageList(String topicTitle, int pageNo, int pageSize, final OnResponseListener<TopicDetailResult> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        Api_SNSCENTER_TopicUgcPageQuery topicUgcPageQuery = new Api_SNSCENTER_TopicUgcPageQuery();
        topicUgcPageQuery.pageNo = pageNo;
        topicUgcPageQuery.pageSize = pageSize;
        topicUgcPageQuery.topicTitle = topicTitle;

        final Snscenter_GetTopicUGCPageList req = new Snscenter_GetTopicUGCPageList(topicUgcPageQuery);

        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                TopicDetailResult value = null;
                if (req.getResponse() != null) {
                    try {
                        value = TopicDetailResult.deserialize(req.getResponse().serialize());
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
     * 查询热门话题
     *
     * @param pageIndex
     * @param pageSize
     * @param titleLike
     * @param lsn
     * @throws JSONException
     */
    public void doSearchHotTopicPageList(final int pageIndex, final int pageSize, String titleLike, final OnResponseListener<TopicInfoResultList> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        final Api_SNSCENTER_PageInfo pageInfo = new Api_SNSCENTER_PageInfo();
        pageInfo.pageNo = pageIndex;
        pageInfo.pageSize = pageSize;

        final Api_SNSCENTER_TopicPageQuery topicPageQuery = new Api_SNSCENTER_TopicPageQuery();
        topicPageQuery.pageInfo = pageInfo;
        topicPageQuery.titleLike = titleLike;

        final Snscenter_SearchHotTopicPageList req = new Snscenter_SearchHotTopicPageList(topicPageQuery);

        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                TopicInfoResultList value = null;
                if (req.getResponse() != null) {
                    try {
                        value = TopicInfoResultList.deserialize(req.getResponse().serialize());
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
     * 查询达人广场
     *
     * @param pageIndex
     * @param pageSize
     * @param lsn
     * @throws JSONException
     */
    public void doGetAreUgcPageList(final int pageIndex, final int pageSize, final OnResponseListener<UgcInfoResultList> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        final Api_SNSCENTER_PageInfo pageInfo = new Api_SNSCENTER_PageInfo();
        pageInfo.traceId = String.valueOf(userService.getLoginUserId());
        pageInfo.pageNo = pageIndex;
        pageInfo.pageSize = pageSize;

        final Snscenter_GetAreUgcPageList req = new Snscenter_GetAreUgcPageList(pageInfo);

        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                UgcInfoResultList value = null;
                if (req.getResponse() != null) {
                    try {
                        value = UgcInfoResultList.deserialize(req.getResponse().serialize());
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
     * 修改用户在线和隐身状态
     *
     * @param userId
     * @param status
     * @param lsn
     * @throws JSONException
     */
    public void doEditUserStatus(final long userId, final int status, final OnResponseListener<Boolean> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        final User_EditUserStatus req = new User_EditUserStatus(userId);
        req.setStatus(status);

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
     * 查询用户在线和隐身状态
     *
     * @param userIds
     * @param lsn
     * @throws JSONException
     */
    public void doGetBatchUserStatus(final long[] userIds, final OnResponseListener<UserStatusInfoList> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        final Api_USER_UserBatchQuery user_userBatchQuery = new Api_USER_UserBatchQuery();
        user_userBatchQuery.userIds = userIds;

        final User_GetBatchUserStatus req = new User_GetBatchUserStatus(user_userBatchQuery);

        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                UserStatusInfoList value = null;
                if (req.getResponse() != null) {
                    try {
                        value = UserStatusInfoList.deserialize(req.getResponse().serialize());
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
     * 根据文章id获取文章信息
     *
     * @param boothCode
     * @param lsn
     * @throws JSONException
     */
    public void doGetArticleListByBoothCode(final String boothCode, final OnResponseListener<ArticleRecommendInfo> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        final Resourcecenter_GetArticleListByBoothCode req = new Resourcecenter_GetArticleListByBoothCode(boothCode);

        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                ArticleRecommendInfo value = null;
                if (req.getResponse() != null) {
                    try {
                        value = ArticleRecommendInfo.deserialize(req.getResponse().serialize());
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
     * 根据展位码获取达人咨询商品列表
     *
     * @param params
     * @param lsn
     * @throws JSONException
     */
    public void doGetThemeItemListByBoothCode(CodeQueryDTO params, final OnResponseListener<BoothItemsResult> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        final Items_GetThemeItemListByBoothCode req = new Items_GetThemeItemListByBoothCode(Api_ITEMS_CodeQueryDTO.deserialize(params.serialize()));

        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                BoothItemsResult value = null;
                if (req.getResponse() != null) {
                    try {
                        value = BoothItemsResult.deserialize(req.getResponse().serialize());
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
     * 发起直播
     *
     * @param params
     * @param lsn
     * @throws JSONException
     */
    public void doCreateLive(LiveRecordInfo params, final OnResponseListener<Api_SNSCENTER_SnsCreateLiveResult> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        final Snscenter_CreateLive req = new Snscenter_CreateLive(Api_SNSCENTER_SnsCreateLiveRequest.deserialize(params.serialize()));
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                Api_SNSCENTER_SnsCreateLiveResult value = null;
                if (req.getResponse() != null) {
                    try {
                        value = Api_SNSCENTER_SnsCreateLiveResult.deserialize(req.getResponse().serialize());
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
     * 获取直播类型列表
     *
     * @param lsn
     * @throws JSONException
     */
    public void doGetLiveCategoryList(final OnResponseListener<LiveCategoryResult> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        final Live_GetLiveCategoryList req = new Live_GetLiveCategoryList();

        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                LiveCategoryResult value = null;
                if (req.getResponse() != null) {
                    try {
                        value = LiveCategoryResult.deserialize(req.getResponse().serialize());
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
     * 获取直播列表
     *
     * @param params
     * @param lsn
     * @throws JSONException
     */
    public void doGetLiveList(LiveRecordAPIPageQuery params, final OnResponseListener<LiveRecordAPIPageResult> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        final Live_GetLiveList req = new Live_GetLiveList(Api_LIVE_LiveRecordAPIPageQuery.deserialize(params.serialize()));

        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                LiveRecordAPIPageResult value = null;
                if (req.getResponse() != null) {
                    try {
                        value = LiveRecordAPIPageResult.deserialize(req.getResponse().serialize());
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
     * 获取直播项
     *
     * @param livecenterId
     * @param lsn
     * @throws JSONException
     */
    public void doGetLiveRecord(long livecenterId, final OnResponseListener<Api_SNSCENTER_SnsLiveRecordResult> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        final Snscenter_GetLiveRecord req = new Snscenter_GetLiveRecord(livecenterId);

        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                Api_SNSCENTER_SnsLiveRecordResult value = null;
                if (req.getResponse() != null) {
                    try {
                        value = Api_SNSCENTER_SnsLiveRecordResult.deserialize(req.getResponse().serialize());
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
     * 获取直播间信息
     *
     * @param lsn
     * @throws JSONException
     */
    public void doGetLiveRoomInfo(long userId, final OnResponseListener<LiveRoomResult> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        final Live_GetLiveRoomInfo req = new Live_GetLiveRoomInfo(userId);

        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                LiveRoomResult value = null;
                if (req.getResponse() != null) {
                    try {
                        value = LiveRoomResult.deserialize(req.getResponse().serialize());
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
     * 获取msg_server地址
     *
     * @param liveMsgServerAddrParam
     * @param lsn
     * @throws JSONException
     */
    public void doGetMsgServerAddress(LiveMsgServerAddrParam liveMsgServerAddrParam, final OnResponseListener<LiveMsgServerAddrResult> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        final Live_UserGetIMAddress req = new Live_UserGetIMAddress(Api_LIVE_MsgServerAddrParam.deserialize(liveMsgServerAddrParam.serialize()));

        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                LiveMsgServerAddrResult value = null;
                if (req.getResponse() != null) {
                    try {
                        value = LiveMsgServerAddrResult.deserialize(req.getResponse().serialize());
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
     * 获取msg_server地址
     *
     * @param liveMsgServerAddrParam
     * @param lsn
     * @throws JSONException
     */
    public void doGetMsgServerAddressByVisitor(LiveMsgServerAddrParam liveMsgServerAddrParam, final OnResponseListener<LiveMsgServerAddrResult> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        final Live_VisitorGetIMAddress req = new Live_VisitorGetIMAddress(Api_LIVE_MsgServerAddrParam.deserialize(liveMsgServerAddrParam.serialize()));

        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                LiveMsgServerAddrResult value = null;
                if (req.getResponse() != null) {
                    try {
                        value = LiveMsgServerAddrResult.deserialize(req.getResponse().serialize());
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
     * 直播禁言
     *
     * @param params
     * @param lsn
     * @throws JSONException
     */
    public void doDisableSendMsg(DisableParam params, final OnResponseListener<DisableResult> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        final Live_DisableSendMsg req = new Live_DisableSendMsg(Api_LIVE_DisableParam.deserialize(params.serialize()));

        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                DisableResult value = null;
                if (req.getResponse() != null) {
                    try {
                        value = DisableResult.deserialize(req.getResponse().serialize());
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
     * 关注主播
     *
     * @param params
     * @param lsn
     * @throws JSONException
     */
    public void doFollowAnchor(FollowAnchorParam params, final OnResponseListener<FollowAnchorResult> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        final Live_FollowAnchor req = new Live_FollowAnchor(Api_LIVE_FollowParam.deserialize(params.serialize()));

        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                FollowAnchorResult value = null;
                if (req.getResponse() != null) {
                    try {
                        value = FollowAnchorResult.deserialize(req.getResponse().serialize());
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
     * 发送通知消息，如主播离开/主播回来/分享直播等
     *
     * @param params
     * @param lsn
     * @throws JSONException
     */
    public void doSendOtherMsg(OtherMsgParam params, final OnResponseListener<OtherMsgResult> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        final Live_SendNotifyMsg req = new Live_SendNotifyMsg(Api_LIVE_OtherMsgParam.deserialize(params.serialize()));

        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                OtherMsgResult value = null;
                if (req.getResponse() != null) {
                    try {
                        value = OtherMsgResult.deserialize(req.getResponse().serialize());
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
     * 关闭直播
     *
     * @param lsn
     * @throws JSONException
     */
    public void doCloseLive(long liveId, final OnResponseListener<Api_SNSCENTER_SnsCloseLiveResult> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        final Snscenter_CloseLive req = new Snscenter_CloseLive(liveId);

        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                Api_SNSCENTER_SnsCloseLiveResult closeResult = null;
                try {
                    closeResult = Api_SNSCENTER_SnsCloseLiveResult.deserialize(req.getResponse().serialize());
                } catch (JSONException e) {
                    if (lsn != null) {
                        lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                    }
                    e.printStackTrace();
                }
                if (lsn != null) {
                    lsn.onComplete(true, closeResult, ErrorCode.STATUS_OK, req.getReturnMessage());
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
     * 是否含有未结束直播
     *
     * @param lsn
     * @throws JSONException
     */
    public void hasNoEndLive(final OnResponseListener<CreateLiveResult> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        final Live_HasNoEndLive req = new Live_HasNoEndLive();

        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                CreateLiveResult value = null;
                if (req.getResponse() != null) {
                    try {
                        value = CreateLiveResult.deserialize(req.getResponse().serialize());
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
     * 修改直播间
     *
     * @param updateLiveRoom
     * @param lsn
     * @throws JSONException
     */
//    public void doUpdateLiveRoom(UpdateLiveRoom updateLiveRoom, final OnResponseListener<Boolean> lsn) throws JSONException {
//        if (!checkSubmitStatus(lsn)) {
//            return;
//        }
//
//        final Live_UpdateLiveRoom req = new Live_UpdateLiveRoom(Api_LIVE_UpdateLiveRoom.deserialize(updateLiveRoom.serialize()));
//
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
//            }
//
//            @Override
//            protected void handleRequestException(Exception exception) {
//                handlerRequestException(exception, lsn);
//            }
//        }.execute(req);
//    }

    /**
     * 批量删除回放
     *
     * @param liveIdList
     * @param lsn
     * @throws JSONException
     */
    public void doDeleteLiveReplay(DeleteLiveListInfo liveIdList, final OnResponseListener<Boolean> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        final Live_DeleteLiveList req = new Live_DeleteLiveList(Api_LIVE_DeleteLiveListInfo.deserialize(liveIdList.serialize()));

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
     * 获取直播间正在直播记录
     *
     * @param roomId
     * @param lsn
     */
    public void doGetLiveRoomLivingRecord(final long roomId, final OnResponseListener<LiveRoomLivingRecordResult> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        final Live_GetLiveRoomLivingRecord req = new Live_GetLiveRoomLivingRecord(roomId);

        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                LiveRoomLivingRecordResult value = null;
                if (req.getResponse() != null) {
                    try {
                        value = LiveRoomLivingRecordResult.deserialize(req.getResponse().serialize());
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
     * 判断用户是否有直播权限
     *
     * @param userID
     * @param lsn
     */
    public void doGetHasLiveRoomOrNot(final long userID, final OnResponseListener<Api_LIVE_LiveRoomHasOrNot> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        final Live_HasLiveRoomOrNot req = new Live_HasLiveRoomOrNot(userID);

        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {

                Api_LIVE_LiveRoomHasOrNot value = null;
                if (req.getResponse() != null) {
                    try {
                        value = Api_LIVE_LiveRoomHasOrNot.deserialize(req.getResponse().serialize());
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
     * 请求直播结束的推荐视频
     *
     * @param api_snscenter_snsClosedViewTopNQuery
     * @param lsn
     * @throws JSONException
     */
    public void doGetLiveOverResult(Api_SNSCENTER_SnsClosedViewTopNQuery api_snscenter_snsClosedViewTopNQuery,
                                    final OnResponseListener<Api_SNSCENTER_SnsClosedViewTopNResult> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        final Snscenter_SnsClosedViewTopN req = new Snscenter_SnsClosedViewTopN(api_snscenter_snsClosedViewTopNQuery);

        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                Api_SNSCENTER_SnsClosedViewTopNResult closeResult = null;
                try {
                    closeResult = Api_SNSCENTER_SnsClosedViewTopNResult.deserialize(req.getResponse().serialize());
                } catch (JSONException e) {
                    if (lsn != null) {
                        lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                    }
                    e.printStackTrace();
                }
                if (lsn != null) {
                    lsn.onComplete(true, closeResult, ErrorCode.STATUS_OK, req.getReturnMessage());
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

    public void doGetHasNoEndBatch(long uid, final OnResponseListener<Api_SNSCENTER_SnsHasNoEndBatchResult> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        final Snscenter_HasNoEndBatch req = new Snscenter_HasNoEndBatch(uid);

        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                Api_SNSCENTER_SnsHasNoEndBatchResult value = null;
                if (req.getResponse() != null) {
                    try {
                        value = Api_SNSCENTER_SnsHasNoEndBatchResult.deserialize(req.getResponse().serialize());
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
     * 上传培训视频回调
     *
     * @param lsn
     * @throws JSONException
     */
    public void trainAsyncCallback(Api_TRAIN_AsyncCallbackParam param, final OnResponseListener<Api_TRAIN_ResponseDTO> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        final Train_AsyncCallback req = new Train_AsyncCallback(param);

        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                if (lsn != null) {
                    lsn.onComplete(true, req.getResponse(), ErrorCode.STATUS_OK, req.getReturnMessage());
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
     * 获取运动兴趣数据
     *
     * @param lsn
     * @throws JSONException
     */
    public void doGetGuideTagInfo(final OnResponseListener<Api_SNSCENTER_GuideTagResultList> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        Snscenter_GetGuideTagInfo request = new Snscenter_GetGuideTagInfo();
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> req) {

                Api_SNSCENTER_GuideTagResultList api_snscenter_guideTagResultList = null;
                if (request.getResponse() != null) {
                    try {
                        String responseString = request.getResponse().serialize().toString();
                        api_snscenter_guideTagResultList = new Gson().fromJson(responseString, new TypeToken<Api_SNSCENTER_GuideTagResultList>() {
                        }.getType());
                    } catch (JSONException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    } catch (JsonSyntaxException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    }
                }
                if (lsn != null) {
                    lsn.onComplete(true, api_snscenter_guideTagResultList, ErrorCode.STATUS_OK, req.getReturnMessage());
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
        }.execute(request);
    }

    /**
     * 查询圈子 直播 或者 视频列表
     *
     * @param pageIndex
     * @param pageSize
     * @param lsn
     * @throws JSONException
     */
    public void doGetHomeLivePageList(long uuid, final int sportHobby, final int pageIndex, final int pageSize, String recordType, String liveScreenType, final OnResponseListener<String> lsn) throws JSONException {

        if (!checkSubmitStatus(lsn)) {
            return;
        }

        final Api_SNSCENTER_SnsHomeLivePageQuery query = new Api_SNSCENTER_SnsHomeLivePageQuery();
        query.pageNo = pageIndex;
        query.pageSize = pageSize;
        query.recordType = recordType;
        query.liveScreenType = liveScreenType;
        query.traceId = String.valueOf(uuid);
        query.liveCategoryCode = sportHobby;
        ArrayList<String> liveRecordStatusList = new ArrayList<>();
        liveRecordStatusList.add("NORMAL_LIVE");
        query.liveRecordStatusList = liveRecordStatusList;

        ArrayList<String> liveStatusList = new ArrayList<>();
        liveStatusList.add("START_LIVE");
        liveStatusList.add("REPLAY_LIVE");
        query.liveStatusList = liveStatusList;

        final Snscenter_QueryHomeLivePageList req = new Snscenter_QueryHomeLivePageList(query);

        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                String value = null;
                if (req.getResponse() != null) {
                    try {
                        value = req.getResponse().serialize().toString();
                    } catch (JSONException e) {
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
     * 查询圈子 用户 视频列表
     *
     * @param pageIndex
     * @param pageSize
     * @param lsn
     * @throws JSONException
     */
    public void doGetUserVideoList(long uuid, final int pageIndex, final int pageSize, final OnResponseListener<String> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        final Api_SNSCENTER_PageInfo query = new Api_SNSCENTER_PageInfo();
        query.traceId = String.valueOf(uuid);
        query.pageNo = pageIndex;
        query.pageSize = pageSize;

        final Snscenter_GetUserVideoPageList req = new Snscenter_GetUserVideoPageList(query);

        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                String value = null;
                if (req.getResponse() != null) {
                    try {
                        value = req.getResponse().serialize().toString();
                    } catch (JSONException e) {
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
     * 查询圈子 推荐 列表
     *
     * @param pageIndex
     * @param pageSize
     * @param lsn
     * @throws JSONException
     */
    public void getSearchPageList(long uuid, String title, final int pageIndex, final int pageSize, final OnResponseListener<String> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        final Api_SNSCENTER_SearchPageQuery query = new Api_SNSCENTER_SearchPageQuery();
        query.title = title;
        query.traceId = String.valueOf(uuid);
        query.pageNo = pageIndex;
        query.pageSize = pageSize;

        final Snscenter_SearchPageList req = new Snscenter_SearchPageList(query);

        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                String value = null;
                if (req.getResponse() != null) {
                    try {
                        value = req.getResponse().serialize().toString();
                    } catch (JSONException e) {
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
     * 查询圈子 推荐 或者 动态Tab 列表
     *
     * @param pageIndex
     * @param pageSize
     * @param lsn
     * @throws JSONException
     */
    public void getRecommendPageList(long uuid, final int pageIndex, final int pageSize, List<String> tagList, int queryType, final OnResponseListener<String> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        final Api_SNSCENTER_RecommendPageQuery query = new Api_SNSCENTER_RecommendPageQuery();
        query.traceId = String.valueOf(uuid);
        query.pageNo = pageIndex;
        query.pageSize = pageSize;
        query.tagList = tagList;
        query.queryType = queryType;

        final Snscenter_GetRecommendPageList req = new Snscenter_GetRecommendPageList(query);

        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                String value = null;
                if (req.getResponse() != null) {
                    try {
                        value = req.getResponse().serialize().toString();
                    } catch (JSONException e) {
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
     * 圈子 推荐 或者 动态Tab 不喜欢删除接口
     *
     * @param lsn
     * @throws JSONException
     */
    public void doDislike(Api_SNSCENTER_DisLikeArgs args, final OnResponseListener<Boolean> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        final Snscenter_Dislike req = new Snscenter_Dislike(args);

        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                Boolean value = false;
                if (req.getResponse() != null) {
                    try {
                        value = req.getResponse().serialize().getBoolean("value");
                    } catch (JSONException e) {
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
     * 约战广场
     *
     * @param lsn
     * @throws JSONException
     */
    public void getCampaignData(Api_COMPETITION_QueryCampaignParam param, final OnResponseListener<Api_COMPETITION_CampaignsVoResult> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        final Competition_GetArrangeCampaignSquare req = new Competition_GetArrangeCampaignSquare(param);

        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                if (lsn != null) {
                    lsn.onComplete(true, req.getResponse(), ErrorCode.STATUS_OK, req.getReturnMessage());
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
     * 获取用户回放列表
     *
     * @param snsReplayListQuery
     * @param lsn
     */
    public void doGetLiveListByUserId(Api_SNSCENTER_SnsReplayListQuery snsReplayListQuery, OnResponseListener<Api_SNSCENTER_SnsReplayListResult> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        Snscenter_GetUserReplayList req = new Snscenter_GetUserReplayList(snsReplayListQuery);
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                if (lsn != null) {
                    lsn.onComplete(true, req.getResponse(), ErrorCode.STATUS_OK, req.getReturnMessage());
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

