package com.quanyan.yhy.ui.club.clubcontroller;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.harwkin.nb.camera.PhotoUtil;
import com.quanyan.base.BaseController;
import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.R;
import com.yhy.common.eventbus.event.EvBusBlack;
import com.quanyan.yhy.net.NetManager;
import com.quanyan.yhy.net.lsn.OnResponseListener;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.smart.sdk.api.resp.Api_SNSCENTER_UgcInfo;
import com.yhy.common.beans.net.model.club.ClubInfoList;
import com.yhy.common.beans.net.model.club.ClubMemberInfo;
import com.yhy.common.beans.net.model.club.SnsActiveMemberPageInfo;
import com.yhy.common.beans.net.model.club.SnsActivePageInfoList;
import com.yhy.common.beans.net.model.club.SubjectDetail;
import com.yhy.common.beans.net.model.club.SubjectDynamic;
import com.yhy.common.beans.net.model.club.SubjectInfoList;
import com.yhy.common.beans.net.model.comment.ComSupportInfo;
import com.yhy.common.beans.net.model.comment.ComTagInfoList;
import com.yhy.common.beans.net.model.comment.CommentInfo;
import com.yhy.common.beans.net.model.comment.CommetDetailInfo;
import com.yhy.common.beans.net.model.rc.ComplaintInfo;
import com.yhy.common.beans.net.model.rc.ComplaintOptionInfoList;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.types.AppDebug;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaoxp on 2015-10-27.
 */
public class ClubController extends BaseController {

	public ClubController(Context context, Handler handler) {
		super(context, handler);
	}

	/**
	 * 图片压缩
	 * @param file
     */
	public void compressionImage(final Context context, String[] file) {
		List<String> files = new ArrayList<String>();
		for (String str : file) {
			if (!AppDebug.ORG_BMP_UPLOAD) {
				try {
					int i = str.lastIndexOf(".");
					if (i > 0) {
						String format = str.substring(i, str.length());
						if (!TextUtils.isEmpty(format) && format.equals(".gif")) {
							files.add(PhotoUtil.depositInDiskGif(context, str, format));
						} else {
							files.add(PhotoUtil.depositInDiskBitmap(context, PhotoUtil.getPhoto(str)));
						}
					} else {
						files.add(PhotoUtil.depositInDiskBitmap(context, PhotoUtil.getPhoto(str)));
					}
				} catch (Exception e) {
					sendMessage(ValueConstants.MSG_UPLOADIMAGE_KO, context.getString(R.string.picture_upload_failed));
					return;
				}
			} else {
				files.add(str);
			}
		}
		uploadImage(context, files);
	}

	/**
	 * 图片不压缩上传
	 * @param file
	 */
	public void toUploadImage(final Context context, String[] file) {
		List<String> files = new ArrayList<String>();
		for (String str : file) {
			files.add(str);
		}
		uploadImage(context, files);
	}

	/**
	 * 批量上传图片
	 *
	 * @param file
	 */
	public void uploadImage(final Context context, final List<String> file) {
		sendMessage(ValueConstants.MSG_SHOW_DIALOG, context.getString(R.string.toast_uploading_image));
		NetManager.getInstance(context).doUploadImages(file,
				new OnResponseListener<List<String>>() {

					@Override
					public void onInternError(int errorCode, String errorMessage) {
						sendMessage(ValueConstants.MSG_HIDELOADING_DIALOG);
						ToastUtil.showToast(context, StringUtil.handlerErrorCode(context, errorCode));
					}


					@Override
					public void onComplete(boolean isOK, List<String> uploadFileName, int errorCode,
					                       String errorMsg) {
						sendMessage(ValueConstants.MSG_HIDELOADING_DIALOG);
						if (isOK) {
							if (uploadFileName != null
									&& uploadFileName.size() > 0) {
								sendMessage(ValueConstants.MSG_UPLOADIMAGE_OK, uploadFileName);
							} else {
								ToastUtil.showToast(context, StringUtil.handlerErrorCode(context, errorCode));
							}
						} else {
							ToastUtil.showToast(context, StringUtil.handlerErrorCode(context, errorCode));
						}
					}
				});
	}

	/**
	 * 上传视频文件
	 * @param file
     */
	public void uploadVideos(final Context context, final List<String> file) {
		sendMessage(ValueConstants.MSG_SHOW_DIALOG, context.getString(R.string.toast_uploading_video));
		NetManager.getInstance(context).doUploadImages(file,
				new OnResponseListener<List<String>>() {

					@Override
					public void onInternError(int errorCode, String errorMessage) {
						sendMessage(ValueConstants.MSG_HIDELOADING_DIALOG);
						ToastUtil.showToast(context, StringUtil.handlerErrorCode(context, errorCode));
					}


					@Override
					public void onComplete(boolean isOK, List<String> uploadFileName, int errorCode,
										   String errorMsg) {
						sendMessage(ValueConstants.MSG_HIDELOADING_DIALOG);
						if (isOK) {
							if (uploadFileName != null
									&& uploadFileName.size() > 0) {
								sendMessage(ValueConstants.MSG_UPLOAD_VIDEO_OK, uploadFileName);
							} else {
								ToastUtil.showToast(context, StringUtil.handlerErrorCode(context, errorCode));
							}
						} else {
							ToastUtil.showToast(context, StringUtil.handlerErrorCode(context, errorCode));
						}
					}
				});
	}

	/**
	 * 上传视频和视频缩略图
	 * @param videoPath
	 * @param videoThumbPath
     */
	public void uploadVideoAndImage(final Context context, String videoPath,String videoThumbPath) {
		List<String> videos = new ArrayList<>();
		videos.add(videoPath);
		videos.add(videoThumbPath);
		sendMessage(ValueConstants.MSG_SHOW_DIALOG, context.getString(R.string.toast_uploading_video));
		NetManager.getInstance(context).doUploadVideos(videos,
				new OnResponseListener<List<String>>() {

					@Override
					public void onInternError(int errorCode, String errorMessage) {
						sendMessage(ValueConstants.MSG_HIDELOADING_DIALOG);
						ToastUtil.showToast(context, StringUtil.handlerErrorCode(context, errorCode));
					}


					@Override
					public void onComplete(boolean isOK, List<String> uploadFileName, int errorCode,
										   String errorMsg) {
						sendMessage(ValueConstants.MSG_HIDELOADING_DIALOG);
						if (isOK) {
							if (uploadFileName != null
									&& uploadFileName.size() > 0) {
								sendMessage(ValueConstants.MSG_UPLOAD_VIDEO_OK, uploadFileName);
							} else {
								ToastUtil.showToast(context, StringUtil.handlerErrorCode(context, errorCode));
							}
						} else {
							ToastUtil.showToast(context, StringUtil.handlerErrorCode(context, errorCode));
						}
					}
				});
	}

	public void sendUploadImage() {
		sendMessage(ValueConstants.MSG_UPLOADIMAGE_OK, null);
	}

	/**
	 * 上传直播
	 *
	 * @param info
	 */
	public void doPublishNewSubjectLive(final Context context, final Api_SNSCENTER_UgcInfo info) {
		sendMessage(ValueConstants.MSG_SHOW_DIALOG, "");
		NetManager.getInstance(context).doAddUGC(info, new OnResponseListener<Boolean>() {
			@Override
			public void onComplete(boolean isOK, Boolean result, int errorCode, String errorMsg) {
				sendMessage(ValueConstants.MSG_HIDELOADING_DIALOG);
				if (isOK) {
//							if (result != null) {
					sendMessage(ValueConstants.MSG_PUBLISH_LIVE_OK, result);
					return;
//							}
				}
				ToastUtil.showToast(context, StringUtil.handlerErrorCode(context, errorCode));
			}

			@Override
			public void onInternError(int errorCode, String errorMessage) {
				sendMessage(ValueConstants.MSG_HIDELOADING_DIALOG);
				ToastUtil.showToast(context, StringUtil.handlerErrorCode(context, errorCode));
			}
		});
	}

	/**
	 * 发表动态
	 *
	 * @param info
	 */
	public void doPublishNewSubject(final Context context, final SubjectDynamic info) {
		sendMessage(ValueConstants.MSG_SHOW_DIALOG, "");
		NetManager.getInstance(context).doPublishNewSubject(info,
				new OnResponseListener<SubjectDetail>() {

					@Override
					public void onInternError(int errorCode, String errorMessage) {
						sendMessage(ValueConstants.MSG_HIDELOADING_DIALOG);
						ToastUtil.showToast(context, StringUtil.handlerErrorCode(context, errorCode));
					}

					@Override
					public void onComplete(boolean isOK, SubjectDetail result, int errorCode, String errorMsg) {
						sendMessage(ValueConstants.MSG_HIDELOADING_DIALOG);
						if (isOK) {
							if (result != null) {
								sendMessage(ValueConstants.MSG_PUBLISH_LIVE_OK, result);
								return;
							}
						}
						ToastUtil.showToast(context, StringUtil.handlerErrorCode(context, errorCode));
					}
				});
	}

	/**
	 * 加入俱乐部
	 *
	 * @param clubId 俱乐部ID
	 */
	public void doJoinTheCLub(final Context context, long clubId) {
		NetManager.getInstance(context).doJoinClubMember(clubId, new OnResponseListener<ClubMemberInfo>() {
			@Override
			public void onComplete(boolean isOK, ClubMemberInfo result, int errorCode, String errorMsg) {
				if (isOK) {
					sendMessage(ValueConstants.MSG_CLUB_DETAIL_JOIN_OK, result);
					return;
				}
				sendMessage(ValueConstants.MSG_CLUB_DETAIL_JOIN_KO, errorCode, 0, errorMsg);
			}

			@Override
			public void onInternError(int errorCode, String errorMessage) {
				sendMessage(ValueConstants.MSG_CLUB_DETAIL_JOIN_KO, errorCode, 0, errorMessage);
			}
		});
	}

	/**
	 * 退出s俱乐部
	 *
	 * @param clubId 俱乐部ID
	 */
	public void doExitTheClub(final Context context, long clubId) {
		NetManager.getInstance(context).doExitClubMember(clubId, new OnResponseListener<ClubMemberInfo>() {
			@Override
			public void onComplete(boolean isOK, ClubMemberInfo result, int errorCode, String errorMsg) {
				if (isOK) {
					sendMessage(ValueConstants.MSG_CLUB_DETAIL_EXIT_OK, result);
					return;
				}
				sendMessage(ValueConstants.MSG_CLUB_DETAIL_EXIT_KO, errorCode, 0, errorMsg);
			}

			@Override
			public void onInternError(int errorCode, String errorMessage) {
				sendMessage(ValueConstants.MSG_CLUB_DETAIL_EXIT_KO, errorCode, 0, errorMessage);
			}
		});
	}

	/**
	 * 俱乐部列表搜索
	 *
	 * @param searchName
	 * @param pageIndex
	 * @param pageSize
	 */
	public void doSearchClubList(final Context context, String searchName, int pageIndex, int pageSize) {
		NetManager.getInstance(context).doGetClubInfoListPage(pageIndex, pageSize, searchName, new OnResponseListener<ClubInfoList>() {
			@Override
			public void onComplete(boolean isOK, ClubInfoList result, int errorCode, String errorMsg) {
				if (isOK) {
					sendMessage(ValueConstants.MSG_CLUB_SEARCH_LIST_OK, result);
					return;
				}
				sendMessage(ValueConstants.MSG_CLUB_SEARCH_LIST_ERROR, errorCode, 0, errorMsg);
			}

			@Override
			public void onInternError(int errorCode, String errorMessage) {
				sendMessage(ValueConstants.MSG_CLUB_SEARCH_LIST_ERROR, errorCode, 0, errorMessage);
			}
		});
	}

	/**
	 * 活动列表搜索
	 *
	 * @param searchName
	 * @param pageIndex
	 * @param pageSize
	 */
	public void doSearchActiveList(final Context context, String searchName, int pageIndex, int pageSize) {
		NetManager.getInstance(context).doGetActiveLstByTitlePage(pageIndex, pageSize, searchName, new OnResponseListener<SnsActivePageInfoList>() {
			@Override
			public void onComplete(boolean isOK, SnsActivePageInfoList result, int errorCode, String errorMsg) {
				if (isOK) {
					sendMessage(ValueConstants.MSG_ACTIVE_SEARCH_LIST_OK, result);
					return;
				}
				sendMessage(ValueConstants.MSG_ACTIVE_SEARCH_LIST_ERROR, errorCode, 0, errorMsg);
			}

			@Override
			public void onInternError(int errorCode, String errorMessage) {
				sendMessage(ValueConstants.MSG_ACTIVE_SEARCH_LIST_ERROR, errorCode, 0, errorMessage);
			}
		});
	}

	/**
	 * 活动详情--更多活动成员列表
	 *
	 * @param id        活动ID
	 * @param pageIndex 页数
	 * @param pageSize  也大小
	 */
	public void doGetActiveMemberList(final Context context, long id, int pageIndex, int pageSize) {
		NetManager.getInstance(context).doGetActiveJoinMemberList(pageIndex, pageSize, id, new OnResponseListener<SnsActiveMemberPageInfo>() {
			@Override
			public void onComplete(boolean isOK, SnsActiveMemberPageInfo result, int errorCode, String errorMsg) {
				if (isOK) {
					sendMessage(ValueConstants.MSG_ACTIVE_DETAIL_MORE_MEMBER_OK, result);
					return;
				}
				sendMessage(ValueConstants.MSG_ACTIVE_DETAIL_MORE_MEMBER_ERROR, errorCode, 0, errorMsg);
			}

			@Override
			public void onInternError(int errorCode, String errorMessage) {
				sendMessage(ValueConstants.MSG_ACTIVE_DETAIL_MORE_MEMBER_ERROR, errorCode, 0, errorMessage);
			}
		});
	}

	/**
	 * 根据用户ID获取动态列表
	 *
	 * @param userID
	 * @param pagetIndex
	 * @param pageSize
	 */
	public void doGetDynamicListByUserID(final Context context, long userID, int pagetIndex, int pageSize) {
		NetManager.getInstance(context).doGetSubjectList(userID, pagetIndex, pageSize, new OnResponseListener<SubjectInfoList>() {
			@Override
			public void onComplete(boolean isOK, SubjectInfoList result, int errorCode, String errorMsg) {
				if (isOK) {
					sendMessage(ValueConstants.MSG_LIST_OK, result);
					return;
				}
				sendMessage(ValueConstants.MSG_LIST_ERROR, errorCode, 0, errorMsg);
			}

			@Override
			public void onInternError(int errorCode, String errorMessage) {
				sendMessage(ValueConstants.MSG_LIST_ERROR, errorCode, 0, errorMessage);
			}
		});
	}

	/**
	 * 标签列表
	 *
	 * @param outType   LIVESUPTAG 直播    ACTIVETYTAG 活动  CLUBTAG 俱乐部
	 * @param pageIndex
	 * @param pageSize
	 */
	public void doGetTagInfoPageByOutType(final Context context, String outType, int pageIndex, int pageSize) {
		NetManager.getInstance(context).doGetTagInfoPageByOutType(outType, pageIndex, pageSize, new OnResponseListener<ComTagInfoList>() {
			@Override
			public void onComplete(boolean isOK, ComTagInfoList result, int errorCode, String errorMsg) {
				if (isOK) {
					sendMessage(ValueConstants.MSG_LABELS_OK, result);
					return;
				}
				sendMessage(ValueConstants.MSG_LABELS_ERROR, errorCode, 0, errorMsg);
			}

			@Override
			public void onInternError(int errorCode, String errorMessage) {
				sendMessage(ValueConstants.MSG_LABELS_ERROR, errorCode, 0, errorMessage);
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
	public void doAddNewPraiseToComment(final Context context, long outId, String outType, int type) {
		NetManager.getInstance(context).doPrasizeForum(outId, outType, type, new OnResponseListener<ComSupportInfo>() {
			@Override
			public void onComplete(boolean isOK, ComSupportInfo result, int errorCode, String errorMsg) {
				if (isOK) {
					sendMessage(ValueConstants.MSG_PRAISE_OK, result);
					return;
				}
				sendMessage(ValueConstants.MSG_PRAISE_ERROR, errorCode, 0, errorMsg);
			}

			@Override
			public void onInternError(int errorCode, String errorMessage) {
				sendMessage(ValueConstants.MSG_PRAISE_ERROR, errorCode, 0, errorMessage);
			}
		});
	}

	/**
	 * 评论
	 *
	 * @param info
	 */
	public void doPostComment(final Context context, final CommetDetailInfo info) {
		NetManager.getInstance(context).doPostComment(info, new OnResponseListener<CommentInfo>() {
			@Override
			public void onComplete(boolean isOK, CommentInfo result, int errorCode, String errorMsg) {
				if (isOK) {
					sendMessage(ValueConstants.MSG_COMMENT_OK, result);
					return;
				}
				sendMessage(ValueConstants.MSG_COMMENT_ERROR, errorCode, 0, errorMsg);
			}

			@Override
			public void onInternError(int errorCode, String errorMessage) {
				sendMessage(ValueConstants.MSG_COMMENT_ERROR, errorCode, 0, errorMessage);
			}
		});
	}

	/**
	 * 删除我的直播
	 *
	 * @param subjectId
	 */
	public void delSubjectLiveInfo(final Context context, final long subjectId) {
		NetManager.getInstance(context).doDeleteLive(subjectId, new OnResponseListener<Boolean>() {
			@Override
			public void onComplete(boolean isOK, Boolean result, int errorCode, String errorMsg) {
				if (isOK) {
					sendMessage(ValueConstants.MSG_DELETE_LIVE_OK, result);
					return;
				}
				sendMessage(ValueConstants.MSG_DELETE_LIVE_ERROR, errorCode, 0, errorMsg);
			}

			@Override
			public void onInternError(int errorCode, String errorMessage) {
				sendMessage(ValueConstants.MSG_DELETE_LIVE_ERROR, errorCode, 0, errorMessage);
			}
		});
	}

	/**
	 * 删除我的动态
	 *
	 * @param subjectId
	 */
	public void delSubjectInfo(final Context context, final long subjectId) {
//		NetManager.getInstance(context).doDeleteForum(subjectId, new OnResponseListener<Boolean>() {
//			@Override
//			public void onComplete(boolean isOK, Boolean result, int errorCode, String errorMsg) {
//				if (isOK) {
//					sendMessage(ValueConstants.MSG_DELETE_DYNAMIC_OK, result);
//					return;
//				}
//				sendMessage(ValueConstants.MSG_DELETE_DYNAMIC_ERROR, errorCode, 0, errorMsg);
//			}
//
//			@Override
//			public void onInternError(int errorCode, String errorMessage) {
//				sendMessage(ValueConstants.MSG_DELETE_DYNAMIC_ERROR, errorCode, 0, errorMessage);
//			}
//		});
		NetManager.getInstance(context).doDelUGC(subjectId, new OnResponseListener<Boolean>() {
			@Override
			public void onComplete(boolean isOK, Boolean result, int errorCode, String errorMsg) {
				if (isOK) {
					sendMessage(ValueConstants.MSG_DELETE_DYNAMIC_OK, result);
					return;
				}
				sendMessage(ValueConstants.MSG_DELETE_DYNAMIC_ERROR, errorCode, 0, errorMsg);
			}

			@Override
			public void onInternError(int errorCode, String errorMessage) {
				sendMessage(ValueConstants.MSG_DELETE_DYNAMIC_ERROR, errorCode, 0, errorMessage);
			}
		});
	}

	/**
	 * 获取投诉选项
	 * @throws JSONException
	 */
	public void doGetComplaintOptions(final Context context)  {
		NetManager.getInstance(context).doGetComplaintOptions(new OnResponseListener<ComplaintOptionInfoList>() {
			@Override
			public void onComplete(boolean isOK, ComplaintOptionInfoList result, int errorCode, String errorMsg) {
				if (isOK) {
					if(result == null){
						result = new ComplaintOptionInfoList();
					}
					sendMessage(ValueConstants.MSG_GET_COMPLAINT_LIST_OK, result);
					return;
				}
				sendMessage(ValueConstants.MSG_GET_COMPLAINT_LIST_KO, errorCode, 0, errorMsg);
			}

			@Override
			public void onInternError(int errorCode, String errorMessage) {
				sendMessage(ValueConstants.MSG_GET_COMPLAINT_LIST_KO, errorCode, 0, errorMessage);
			}
		});
	}

	/**
	 * 投诉
	 * @param complaintInfo
	 * @throws JSONException
	 */
	public void doSubmitComplaint(final Context context, ComplaintInfo complaintInfo) {
		NetManager.getInstance(context).doSubmitComplaint(complaintInfo, new OnResponseListener<Boolean>() {
			@Override
			public void onComplete(boolean isOK, Boolean result, int errorCode, String errorMsg) {
				if (isOK) {
					sendMessage(ValueConstants.MSG_SUBMIT_COMPLAINT_OK, result);
					return;
				}
				sendMessage(ValueConstants.MSG_SUBMIT_COMPLAINT_KO, errorCode, 0, errorMsg);
			}

			@Override
			public void onInternError(int errorCode, String errorMessage) {
				sendMessage(ValueConstants.MSG_SUBMIT_COMPLAINT_KO, errorCode, 0, errorMessage);
			}
		});
	}

	/**
	 * 屏蔽人或者动态
	 * @param type
	 * @param id
     */
	public void doBlackUserOrDynamic(final Context context, String type,long id) {
		NetManager.getInstance(context).doAddUserShield(type, id, new OnResponseListener<Boolean>() {
			@Override
			public void onComplete(boolean isOK, Boolean result, int errorCode, String errorMsg) {
				if (isOK) {
					EvBusBlack black = new EvBusBlack();
					black.id = id;
					black.type = type;
					sendMessage(ValueConstants.MSG_BLACK_OK,black);
					return;
				}
				sendMessage(ValueConstants.MSG_BLACK_KO, errorCode, 0, errorMsg);
			}

			@Override
			public void onInternError(int errorCode, String errorMessage) {
				sendMessage(ValueConstants.MSG_BLACK_KO, errorCode, 0, errorMessage);
			}
		});
	}
	/**
	 * 关注
	 */
	public void doAddAttention(final Context context, long userId) {
//		NetManager.getInstance(context).doPrasizeForum(outId, outType, type, new OnResponseListener<ComSupportInfo>() {
//			@Override
//			public void onComplete(boolean isOK, ComSupportInfo result, int errorCode, String errorMsg) {
//				if (isOK) {
//					sendMessage(ValueConstants.MSG_ATTENTION_OK, result);
//					return;
//				}
//				sendMessage(ValueConstants.MSG_ATTENTION_KO, errorCode, 0, errorMsg);
//			}
//
//			@Override
//			public void onInternError(int errorCode, String errorMessage) {
//				sendMessage(ValueConstants.MSG_ATTENTION_KO, errorCode, 0, errorMessage);
//			}
//		});
		NetManager.getInstance(context).doAddFollower(userId, new OnResponseListener<Boolean>() {
			@Override
			public void onComplete(boolean isOK, Boolean result, int errorCode, String errorMsg) {
				if (isOK) {
					sendMessage(ValueConstants.MSG_ATTENTION_OK, result);
					return;
				}
				sendMessage(ValueConstants.MSG_ATTENTION_KO, errorCode, 0, errorMsg);
			}

			@Override
			public void onInternError(int errorCode, String errorMessage) {
					sendMessage(ValueConstants.MSG_ATTENTION_KO, errorCode, 0, errorMessage);
			}
		});
	}

	/**
	 * 取消关注
	 * @param context
	 * @param outId
	 * @param outType
     * @param type
     */
	public void doCancelAttention(final Context context, long outId, String outType, int type) {
		NetManager.getInstance(context).doPrasizeForum(outId, outType, type, new OnResponseListener<ComSupportInfo>() {
			@Override
			public void onComplete(boolean isOK, ComSupportInfo result, int errorCode, String errorMsg) {
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
}
