package com.quanyan.yhy.ui.discovery.controller;

import android.content.Context;
import android.os.Handler;

import com.quanyan.base.BaseController;
import com.quanyan.yhy.net.CacheManager;
import com.quanyan.yhy.net.NetManager;
import com.quanyan.yhy.net.lsn.OnResponseListener;
import com.yhy.common.beans.net.model.comment.CommentInfoList;
import com.yhy.common.beans.net.model.comment.SupportUserInfoList;
import com.yhy.common.beans.net.model.discover.TopicDetailResult;
import com.yhy.common.beans.net.model.discover.TopicInfoResult;
import com.yhy.common.beans.net.model.discover.TopicInfoResultList;
import com.yhy.common.beans.net.model.discover.TravelSpecialInfo;
import com.yhy.common.beans.net.model.discover.TravelSpecialInfoList;
import com.yhy.common.beans.net.model.discover.UgcInfoResult;
import com.yhy.common.beans.net.model.discover.UgcInfoResultList;
import com.yhy.common.constants.ValueConstants;

/**
 * Created with Android Studio.
 * Title:DiscoverController
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:和平必胜、正义必胜、人民必胜
 * Author:炎黄子孙
 * Date:15/11/5
 * Time:下午7:48
 * Version 1.0
 */
public class DiscoverController extends BaseController {
    public static final int MSG_OK = 0x4001;
    public static final int MSG_ERROR = 0x4002;

    public static final int MSG_LIVE_LIST_OK = 0x2003;
    public static final int MSG_LIVE_LIST_KO = 0x2004;

    public static final int MSG_LIVE_DETAIL_COMMENT_OK = 0x3001;
    public static final int MSG_LIVE_DETAIL_COMMENT_ERROR = 0x3002;

    public DiscoverController(Context context, Handler handler) {
        super(context, handler);
    }

    /**
     * 广场（热门）列表
     * @param context
     * @param pageIndex
     * @param pageSize
     */
    public void doGetUGCPageListAll(final Context context, final int pageIndex, int pageSize) {
        NetManager.getInstance(context).doGetAreUgcPageList(pageIndex, pageSize, new OnResponseListener<UgcInfoResultList>() {
            @Override
            public void onComplete(boolean isOK, UgcInfoResultList result, int errorCode, String errorMsg) {
                if (isOK) {
                    if (result == null) {
                        result = new UgcInfoResultList();
                    } else {
                        if (pageIndex == 1) {
                            new CacheManager(context, mUiHandler).saveLiveListData(result);
                        }
                    }
                    sendMessage(MSG_LIVE_LIST_OK, result);
                    return;
                }
                sendMessage(MSG_LIVE_LIST_KO, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(MSG_LIVE_LIST_KO, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 关注或广场动态列表
     * @param type 查询类型 1,全部,2,关注
     */
    public void doGetUGCPageList(final Context context, long tagId, final int pageIndex, int pageSize, int type) {
//		NetManager.getInstance(context).doGetLiveListByTagId(tagId, pageIndex, pageSize, new OnResponseListener<SubjectInfoList>() {
//
//			@Override
//			public void onInternError(int errorCode, String errorMessage) {
//				sendMessage(MSG_LIVE_LIST_KO, errorCode, 0, errorMessage);
//			}
//
//			@Override
//			public void onComplete(boolean isOK, SubjectInfoList result, int errorCode, String errorMsg) {
//				if (isOK) {
//					if(result == null){
//						result = new SubjectInfoList();
//					}else{
//						if(pageIndex == 1) {
//							new CacheManager(context, mUiHandler).saveLiveListData(result);
//						}
//					}
//					sendMessage(MSG_LIVE_LIST_OK, result);
//					return;
//				}
//				sendMessage(MSG_LIVE_LIST_KO, errorCode, 0, errorMsg);
//			}
//		});
        NetManager.getInstance(context).doGetUGCPageList(pageIndex, pageSize, type, new OnResponseListener<UgcInfoResultList>() {
            @Override
            public void onComplete(boolean isOK, UgcInfoResultList result, int errorCode, String errorMsg) {
                if (isOK) {
                    if (result == null) {
                        result = new UgcInfoResultList();
                    } else {
                        if (pageIndex == 1) {
                            new CacheManager(context, mUiHandler).saveLiveListData(result);
                        }
                    }
                    sendMessage(MSG_LIVE_LIST_OK, result);
                    return;
                }
                sendMessage(MSG_LIVE_LIST_KO, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(MSG_LIVE_LIST_KO, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 获取UGC列表(根据用户ID查询)
     */
    public void doGetUGCListByUserId(final Context context, long userId, final int pageIndex, int pageSize) {
//		NetManager.getInstance(context).doGetUGCListByUserId(userId, pageIndex, pageSize, new OnResponseListener<SubjectInfoList>() {
//
//			@Override
//			public void onInternError(int errorCode, String errorMessage) {
//				sendMessage(MSG_LIVE_LIST_KO, errorCode, 0, errorMessage);
//			}
//
//			@Override
//			public void onComplete(boolean isOK, SubjectInfoList result, int errorCode, String errorMsg) {
//				if (isOK) {
//					if(result == null){
//						result = new SubjectInfoList();
//					}
//					sendMessage(MSG_LIVE_LIST_OK, result);
//					return;
//				}
//				sendMessage(MSG_LIVE_LIST_KO, errorCode, 0, errorMsg);
//			}
//		});
        NetManager.getInstance(context).doGetUserUGCPageList(pageIndex, pageSize, userId, new OnResponseListener<UgcInfoResultList>() {
            @Override
            public void onComplete(boolean isOK, UgcInfoResultList result, int errorCode, String errorMsg) {
                if (isOK) {
                    if (result == null) {
                        result = new UgcInfoResultList();
                    }
                    sendMessage(MSG_LIVE_LIST_OK, result);
                    return;
                }
                sendMessage(MSG_LIVE_LIST_KO, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(MSG_LIVE_LIST_KO, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 获取直播详情数据
     *
     * @param liveId
     */
    public void doGetLiveDetail(final Context context, final long liveId) {
//		NetManager.getInstance(context).doGetLiveDetail(liveId, new OnResponseListener<SubjectInfo>() {
//
//			@Override
//			public void onInternError(int errorCode, String errorMessage) {
//				sendMessage(ValueConstants.MSG_LIVE_DEATIL_ERROR, errorCode, 0, errorMessage);
//			}
//
//			@Override
//			public void onComplete(boolean isOK, SubjectInfo result, int errorCode, String errorMsg) {
//				if (isOK) {
//					sendMessage(ValueConstants.MSG_LIVE_DETAIL_OK, result);
//					return;
//				}
//				sendMessage(ValueConstants.MSG_LIVE_DEATIL_ERROR, errorCode, 0, errorMsg);
//			}
//		});
        NetManager.getInstance(context).doGetUGCDetail(liveId, new OnResponseListener<UgcInfoResult>() {
            @Override
            public void onComplete(boolean isOK, UgcInfoResult result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(ValueConstants.MSG_LIVE_DETAIL_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_LIVE_DEATIL_ERROR, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_LIVE_DEATIL_ERROR, errorCode, 0, errorMessage);
            }
        });
    }

//	/**
//	 * 获取动态详情
//	 * @param liveId
//	 */
//	public void doGetDaynamicDetail(final Context context, final long liveId){
//		NetManager.getInstance(context).deGetDynamicDetail(liveId, new OnResponseListener<SubjectInfo>() {
//
//			@Override
//			public void onInternError(int errorCode, String errorMessage) {
//				sendMessage(ValueConstants.MSG_LIVE_DEATIL_ERROR, errorCode, 0, errorMessage);
//			}
//
//			@Override
//			public void onComplete(boolean isOK, SubjectInfo result, int errorCode, String errorMsg) {
//				if (isOK) {
//					sendMessage(ValueConstants.MSG_LIVE_DETAIL_OK, result);
//					return;
//				}
//				sendMessage(ValueConstants.MSG_LIVE_DEATIL_ERROR, errorCode, 0, errorMsg);
//			}
//		});
//	}

    /**
     * 获取直播详情评论数据
     */
    public void doGetLiveDetailComment(final Context context, long subjectId, String outType, int pageIndex, int pageSize) {
        NetManager.getInstance(context).doGetLiveDetailCommment(subjectId, outType, pageIndex, pageSize, new OnResponseListener<CommentInfoList>() {

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(MSG_LIVE_DETAIL_COMMENT_ERROR, errorCode, 0, errorMessage);
            }

            @Override
            public void onComplete(boolean isOK, CommentInfoList result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(MSG_LIVE_DETAIL_COMMENT_OK, result);
                    return;
                }
                sendMessage(MSG_LIVE_DETAIL_COMMENT_ERROR, errorCode, 0, errorMsg);
            }
        });
    }

    /**
     * 获取直播详情评论点赞列表
     */
    public void doGetLiveDetailAppraisePeople(final Context context, long outId, String outType, int pageIndex, int pageSize) {
        NetManager.getInstance(context).doGetLiveDetailAppraisePeople(outId, outType, pageIndex, pageSize, new OnResponseListener<SupportUserInfoList>() {

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(MSG_LIVE_DETAIL_COMMENT_ERROR, errorCode, 0, errorMessage);
            }

            @Override
            public void onComplete(boolean isOK, SupportUserInfoList result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(MSG_LIVE_DETAIL_COMMENT_OK, result);
                    return;
                }
                sendMessage(MSG_LIVE_DETAIL_COMMENT_ERROR, errorCode, 0, errorMsg);
            }
        });
    }

    /**
     * 获取发布直播--搜索标签，默认数据列表
     */
    public void doGetLiveAddTopicLabels(final Context context, final String outType, final int pageIndex, final int pageSize) {
        NetManager.getInstance(context).doSearchHotTopicPageList(pageIndex, pageSize, outType, new OnResponseListener<TopicInfoResultList>() {
            @Override
            public void onComplete(boolean isOK, TopicInfoResultList result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(MSG_OK, result);
                    return;
                }
                sendMessage(MSG_ERROR, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(MSG_ERROR, errorCode, 0, errorMessage);
            }
        });
//        NetManager.getInstance(context).doGetLiveAddTopicLabels(outType, pageIndex, pageSize, new OnResponseListener<ComUserAndTagInfoList>() {
//
//            @Override
//            public void onInternError(int errorCode, String errorMessage) {
//                sendMessage(MSG_ERROR, errorCode, 0, errorMessage);
//            }
//
//            @Override
//            public void onComplete(boolean isOK, ComUserAndTagInfoList result, int errorCode, String errorMsg) {
//                if (isOK) {
//                    sendMessage(MSG_OK, result);
//                    return;
//                }
//                sendMessage(MSG_ERROR, errorCode, 0, errorMsg);
//            }
//        });
    }

    /**
     * 获取发布直播--搜索标签，搜索数据
     */
    public void doGetLiveAddTopicLabelsSearch(final Context context, final String searchContent, final int pageIndex, final int pageSize) {
        NetManager.getInstance(context).doSearchTopicTitlePageList(pageIndex, pageSize, searchContent, new OnResponseListener<TopicInfoResultList>() {
            @Override
            public void onComplete(boolean isOK, TopicInfoResultList result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(ValueConstants.MSG_TAG_SEARCH_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_TAG_SEARCH_ERROR, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_TAG_SEARCH_ERROR, errorCode, 0, errorMessage);
            }
        });
//        NetManager.getInstance(context).doFindTagList(searchContent, pageIndex, pageSize, new OnResponseListener<ComTagInfoList>() {
//
//            @Override
//            public void onInternError(int errorCode, String errorMessage) {
//                sendMessage(ValueConstants.MSG_TAG_SEARCH_ERROR, errorCode, 0, errorMessage);
//            }
//
//            @Override
//            public void onComplete(boolean isOK, ComTagInfoList result, int errorCode, String errorMsg) {
//                if (isOK) {
//                    sendMessage(ValueConstants.MSG_TAG_SEARCH_OK, result);
//                    return;
//                }
//                sendMessage(ValueConstants.MSG_TAG_SEARCH_ERROR, errorCode, 0, errorMsg);
//            }
//        });
    }

    /**
     * 获取游记列表
     *
     * @param pageIndex
     * @param pageSize
     */
    public void doGetTravelSpecialListPage(final Context context, final int pageIndex, int pageSize) {
        NetManager.getInstance(context).doGetTravelSpecialListPage(pageIndex, pageSize, new OnResponseListener<TravelSpecialInfoList>() {
            @Override
            public void onComplete(boolean isOK, TravelSpecialInfoList result, int errorCode, String errorMsg) {
                if (isOK) {
                    if (result == null) {
                        result = new TravelSpecialInfoList();
                    } else {
                        if (pageIndex == 1) {
                            new CacheManager(context, mUiHandler).saveTravelNotesData(result);
                        }
                    }
                    sendMessage(ValueConstants.MSG_TRAVELNOTES_LIST_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_TRAVELNOTES_LIST_ERROR, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_TRAVELNOTES_LIST_ERROR, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 获取游记详情
     *
     * @param travelNotesId
     */
    public void doGetTravelSpecialDetail(final Context context, final long travelNotesId) {
        NetManager.getInstance(context).doGetTravelSpecialDetail(travelNotesId, new OnResponseListener<TravelSpecialInfo>() {
            @Override
            public void onComplete(boolean isOK, TravelSpecialInfo result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(ValueConstants.MSG_TRAVELNOTES_DETAIL_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_TRAVELNOTES_DETAIL_ERROR, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_TRAVELNOTES_DETAIL_ERROR, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 删除品论
     *
     * @param id
     */
    public void doDelComment(final Context context, long id, String type) {
        NetManager.getInstance(context).doDeleteComment(id, type, new OnResponseListener<Boolean>() {
            @Override
            public void onComplete(boolean isOK, Boolean result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(ValueConstants.MSG_DELETE_COMMENT, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_DELETE_COMMENT_ERROR, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_DELETE_COMMENT_ERROR, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 删除自己发表的直播下的评论
     *
     * @param reployId  评论id
     * @param subjectId 帖子的UID
     */
    public void doDelComment(final Context context, long reployId,
                             long subjectId) {
        NetManager.getInstance(context).doDeleteComment(reployId, subjectId, new OnResponseListener<Boolean>() {
            @Override
            public void onComplete(boolean isOK, Boolean result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(ValueConstants.MSG_DELETE_COMMENT, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_DELETE_COMMENT_ERROR, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_DELETE_COMMENT_ERROR, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 取消关注
     *
     * @param context
     * @param outId
     */
    public void doCancelAttention(final Context context, long outId) {
//		NetManager.getInstance(context).doPrasizeForum(outId, outType, type, new OnResponseListener<ComSupportInfo>() {
//			@Override
//			public void onComplete(boolean isOK, ComSupportInfo result, int errorCode, String errorMsg) {
//				if (isOK) {
//					sendMessage(ValueConstants.MSG_CANCEL_ATTENTION_OK, result);
//					return;
//				}
//				sendMessage(ValueConstants.MSG_CANCEL_ATTENTION_KO, errorCode, 0, errorMsg);
//			}
//
//			@Override
//			public void onInternError(int errorCode, String errorMessage) {
//				sendMessage(ValueConstants.MSG_CANCEL_ATTENTION_KO, errorCode, 0, errorMessage);
//			}
//		});
        NetManager.getInstance(context).doRemoveFollower(outId, new OnResponseListener<Boolean>() {
            @Override
            public void onComplete(boolean isOK, Boolean result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(ValueConstants.MSG_CANCEL_ATTENTION_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_CANCEL_ATTENTION_KO, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_CANCEL_ATTENTION_KO, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 获取话题详情
     *
     * @param context
     */
    public void doGetTopicInfo(final Context context, String topicTitle) {
        NetManager.getInstance(context).doGetTopicInfo(topicTitle, new OnResponseListener<TopicInfoResult>() {
            @Override
            public void onComplete(boolean isOK, TopicInfoResult result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(ValueConstants.TOPIC_DETAIL_OK, result);
                    return;
                }
                sendMessage(ValueConstants.TOPIC_DETAIL_ERROR, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.TOPIC_DETAIL_ERROR, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 获取话题详情的UGC列表
     *
     * @param context
     * @param topicId
     * @param pageNo
     * @param pageSize
     */
    public void doGetTopicUGCPageList(final Context context, String topicTitle, int pageNo, int pageSize) {
        NetManager.getInstance(context).doGetTopicUGCPageList(topicTitle, pageNo, pageSize, new OnResponseListener<TopicDetailResult>() {
            @Override
            public void onComplete(boolean isOK, TopicDetailResult result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(ValueConstants.TOPIC_DETAIL_LIST_OK, result);
                    return;
                }
                sendMessage(ValueConstants.TOPIC_DETAIL_LIST_ERROR, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.TOPIC_DETAIL_LIST_ERROR, errorCode, 0, errorMessage);
            }
        });
    }
}
