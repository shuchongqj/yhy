package com.newyhy.network;

import android.content.Context;

import com.quanyan.yhy.net.NetManager;
import com.quanyan.yhy.net.lsn.OnResponseListener;
import com.yhy.common.beans.net.model.comment.ComSupportInfo;
import com.yhy.common.beans.net.model.discover.TopicDetailResult;
import com.yhy.common.beans.net.model.discover.TopicInfoResult;
import com.yhy.common.beans.net.model.discover.UgcInfoResultList;
import com.yhy.common.constants.ValueConstants;

/**
 * 圈子 网络请求 Controller
 * Created by Jiervs on 2018/4/17.
 */

public class CircleNetController extends BaseNetController {

    public static final int MSG_UGC_LIST_RECEIVE = 0x1000;//收到UGCList
    public static final int MSG_UGC_LIST_NULL = 0x1001;//收到UGCList为空
    public static final int MSG_PRAISE_OK = 0x1002;//点赞成功
    public static final int MSG_FOLLOW_OK = 0x1003;//关注成功
    public static final int MSG_UNFOLLOW_OK = 0x1004;//取消关注成功
    public static final int MSG_TOPIC_DETAIL_OK = 0x1005;//获取话题详情

    public CircleNetController(Context context, NetHandler handler) {
        super(context, handler);
    }

    /**
     * Ugc列表
     *
     * @param type 查询类型 2,关注
     */
    public void getUGCPageList(final Context context, final int pageIndex, int pageSize, int type) {
        NetManager.getInstance(context).doGetUGCPageList(pageIndex, pageSize, type, new OnResponseListener<UgcInfoResultList>() {
            @Override
            public void onComplete(boolean isOK, UgcInfoResultList result, int errorCode, String errorMsg) {
                if (isOK) {
                    if (result != null && result.ugcInfoList != null && result.ugcInfoList.size() > 0) {
                        //if (pageIndex == 0) new CacheManager(context, mHandler).saveLiveListData(result);
                        sendMessage(MSG_UGC_LIST_RECEIVE, result);
                    } else {
                        sendMessage(MSG_UGC_LIST_NULL);
                    }
                }
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(MSG_ERROR, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 广场（热门）列表
     *
     * @param context
     * @param pageIndex
     * @param pageSize
     */
    public void getUGCPageListAll(final Context context, final int pageIndex, int pageSize) {
        NetManager.getInstance(context).doGetAreUgcPageList(pageIndex, pageSize, new OnResponseListener<UgcInfoResultList>() {
            @Override
            public void onComplete(boolean isOK, UgcInfoResultList result, int errorCode, String errorMsg) {
                if (isOK) {
                    if (result != null && result.ugcInfoList != null && result.ugcInfoList.size() > 0) {
                        //if (pageIndex == 0) new CacheManager(context, mHandler).saveLiveListData(result);
                        sendMessage(MSG_UGC_LIST_RECEIVE, result);
                    } else {
                        sendMessage(MSG_UGC_LIST_NULL);
                    }
                }
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(MSG_ERROR, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 获取用户 Ugc 列表
     *
     * @param context
     * @param pageIndex
     * @param pageSize
     */
    public void getUGCPageListByUserId(final Context context, final int pageIndex, int pageSize, long userId) {
        NetManager.getInstance(context).doGetUserUGCPageList(pageIndex, pageSize, userId, new OnResponseListener<UgcInfoResultList>() {
            @Override
            public void onComplete(boolean isOK, UgcInfoResultList result, int errorCode, String errorMsg) {
                if (isOK) {
                    if (result != null && result.ugcInfoList != null && result.ugcInfoList.size() > 0) {
                        //if (pageIndex == 0) new CacheManager(context, mHandler).saveLiveListData(result);
                        sendMessage(MSG_UGC_LIST_RECEIVE, result);
                    } else {
                        sendMessage(MSG_UGC_LIST_NULL);
                    }
                }
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(MSG_ERROR, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 获取话题详情
     *
     * @param context
     */
    public void getTopicInfo(final Context context, String topicTitle) {
        NetManager.getInstance(context).doGetTopicInfo(topicTitle, new OnResponseListener<TopicInfoResult>() {
            @Override
            public void onComplete(boolean isOK, TopicInfoResult result, int errorCode, String errorMessage) {
                if (isOK) {
                    sendMessage(MSG_TOPIC_DETAIL_OK, result);
                    return;
                }
                sendMessage(MSG_ERROR, errorCode, 0, errorMessage);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(MSG_ERROR, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 获取话题详情的 ugc list
     *
     * @param context
     */
    public void getTopicUGCPageList(final Context context, String topicTitle, int pageNo, int pageSize) {
        NetManager.getInstance(context).doGetTopicUGCPageList(topicTitle, pageNo, pageSize, new OnResponseListener<TopicDetailResult>() {
            @Override
            public void onComplete(boolean isOK, TopicDetailResult result, int errorCode, String errorMsg) {
                if (isOK) {
                    if (result != null && result.ugcInfoList != null && result.ugcInfoList.size() > 0) {
                        //if (pageIndex == 0) new CacheManager(context, mHandler).saveLiveListData(result);
                        sendMessage(MSG_UGC_LIST_RECEIVE, result.ugcInfoList);
                    } else {
                        sendMessage(MSG_UGC_LIST_NULL);
                    }
                }
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(MSG_ERROR, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 点赞
     *
     * @param outId   内容id
     * @param outType 类型： LIVESUP 直播 DYNAMICSUP 动态 ACTIVESUP 活动
     * @param type
     * @see ValueConstants#PRAISE
     * @see ValueConstants#UNPRAISE
     */
    public void doPraiseToComment(final Context context, long outId, String outType, int type) {
        NetManager.getInstance(context).doPrasizeForum(outId, outType, type, new OnResponseListener<ComSupportInfo>() {
            @Override
            public void onComplete(boolean isOK, ComSupportInfo result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(MSG_PRAISE_OK, result);
                    return;
                }
                sendMessage(MSG_ERROR, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(MSG_ERROR, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 关注
     */
    public void doFollow(final Context context, final long userId) {
        NetManager.getInstance(context).doAddFollower(userId, new OnResponseListener<Boolean>() {
            @Override
            public void onComplete(boolean isOK, Boolean result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(MSG_FOLLOW_OK, userId);
                    return;
                }
                sendMessage(MSG_ERROR, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(MSG_ERROR, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 取消关注
     *
     * @param context
     * @param followedUserId
     */
    public void doUnFollow(final Context context, final long followedUserId) {

        NetManager.getInstance(context).doRemoveFollower(followedUserId, new OnResponseListener<Boolean>() {
            @Override
            public void onComplete(boolean isOK, Boolean result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(MSG_UNFOLLOW_OK, followedUserId);
                    return;
                }
                sendMessage(MSG_ERROR, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(MSG_ERROR, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 视频播放次数加 1
     */
    public void doShortVideoPlayAddOne(final Context context, final long ugcId) {

        NetManager.getInstance(context).getShortVideoDetail(new OnResponseListener<Long>() {
            @Override
            public void onComplete(boolean isOK, Long result, int errorCode, String errorMsg) {

            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {

            }
        }, ugcId);
    }
}
