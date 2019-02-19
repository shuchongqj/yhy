package com.newyhy.views.itemview;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.newyhy.activity.VideoPlayer;
import com.newyhy.beans.CircleCrawlInfo;
import com.newyhy.beans.CircleLiveInfoResult;
import com.newyhy.config.CircleBizType;
import com.newyhy.config.CircleShareChannel;
import com.newyhy.utils.DateUtil;
import com.newyhy.utils.ShareUtils;
import com.newyhy.views.YhyListVideoView;
import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.SysConfigType;
import com.yhy.common.eventbus.event.EvBusCircleChangeFollow;
import com.yhy.common.eventbus.event.EvBusCircleChangePraise;
import com.yhy.common.eventbus.event.EvBusCircleDelete;
import com.quanyan.yhy.libanalysis.AnEvent;
import com.quanyan.yhy.libanalysis.Analysis;
import com.quanyan.yhy.net.NetManager;
import com.quanyan.yhy.net.lsn.OnResponseListener;
import com.quanyan.yhy.ui.base.utils.DialogUtil;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.videolibrary.utils.IntentUtil;
import com.yhy.common.beans.net.model.comment.ComSupportInfo;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.CommonUtil;
import com.yhy.common.utils.SPUtils;
import com.yhy.location.LocationManager;
import com.yhy.network.YhyCallback;
import com.yhy.network.YhyCode;
import com.yhy.network.api.activitycenter.ActivityCenterApi;
import com.yhy.network.req.activitycenter.PlayReq;
import com.yhy.network.req.activitycenter.ShareReq;
import com.yhy.network.resp.Response;
import com.yhy.network.resp.activitycenter.PlayResp;
import com.yhy.network.resp.activitycenter.ShareResp;
import com.yhy.router.YhyRouter;
import com.yhy.service.IUserService;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import de.greenrobot.event.EventBus;

import static com.newyhy.activity.VideoPlayer.YhyState.YHY_LOAD;
import static com.newyhy.activity.VideoPlayer.YhyState.YHY_PLAYING;

/**
 * CircleLiveLayout 圈子中视频 Item 的布局
 * Created by Jiervs on 2018/6/20.
 */

public class CircleStandardVideoLayout extends LinearLayout {

    private TextView tv_origin;
    private TextView tv_time;
    private LinearLayout ll_comment;
    private TextView tv_comment_num;
    private LinearLayout ll_more;
    private LinearLayout ll_follow;
    private ImageView iv_follow_state;
    private TextView tv_follow_state;
    private TextView tv_saw_number;
    private LinearLayout ll_video_play;
    private RelativeLayout rl_video_pause;
    private ImageView iv_wx_circle;
    private ImageView iv_wx;
    private ImageView iv_fire;
    public YhyListVideoView video_player;

    //data
    private long id;
    private long liveId;
    private long roomId;
    private int liveScreenType;
    private String nickName;
    private long userId;
    private String title;
    private String origin;
    private long createdTime;
    private int commentNum;
    private int supportNum;
    private String isSupport;
    private int followType;
    private String videoPicUrl;
    private ArrayList<String> picList;
    private String videoUrl;
    private int saw_number;

    // fenxaing
    private String shareTitle;
    private String shareUrl;
    private String shareImg;
    private String userName;
    private String shareContent;

    @Autowired
    IUserService userService;

    //recycler的位置
    public int position;
    public HashMap<String, String> extraMap = new HashMap<>();

    public CircleStandardVideoLayout(Context context) {
        super(context);
        initViews(context);
    }

    public CircleStandardVideoLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public CircleStandardVideoLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    public void initViews(Context context) {
        YhyRouter.getInstance().inject(this);
        LayoutInflater.from(context).inflate(R.layout.circle_standard_video_layout, this);
        tv_origin = findViewById(R.id.tv_origin);
        tv_time = findViewById(R.id.tv_time);
        video_player = findViewById(R.id.video_player);
        ll_comment = findViewById(R.id.ll_comment);
        tv_comment_num = findViewById(R.id.tv_comment_num);
        ll_more = findViewById(R.id.ll_more);
        ll_follow = findViewById(R.id.ll_follow);
        iv_follow_state = findViewById(R.id.iv_follow_state);
        tv_follow_state = findViewById(R.id.tv_follow_state);
        iv_wx_circle = findViewById(R.id.iv_wx_circle);
        iv_wx = findViewById(R.id.iv_wx);
        ll_video_play = findViewById(R.id.ll_video_play);
        rl_video_pause = findViewById(R.id.rl_video_pause);
        tv_saw_number = findViewById(R.id.tv_saw_number);
        iv_fire = findViewById(R.id.iv_fire);
    }

    //转换数据t
    public <T> void covertData(T t) {
        if (t instanceof CircleLiveInfoResult) {//CircleLiveInfoResult
            CircleLiveInfoResult data = (CircleLiveInfoResult) t;
            if (null != data.userInfo) {
                origin = data.userInfo.nickname;
                nickName = data.userInfo.nickname;
                userId = data.userInfo.userId;
            }
            //createdTime = data.gmtCreated;
            id = data.id;
            liveId = data.liveId;
            roomId = data.roomId;
            title = data.liveTitle;
            commentNum = data.commentNum;
            supportNum = data.supportNum;
            isSupport = data.isSupport;
            followType = data.type;
            videoPicUrl = data.liveCover;
            saw_number = data.viewCount;

//            isPlaying = data.isPlaying;
            if (data.replayUrl != null && data.replayUrl.size() > 0)
                videoUrl = data.replayUrl.get(0);

            if ("HORIZONTAL".equals(data.liveScreenType)) {
                liveScreenType = 0;
            } else {
                liveScreenType = 1;
            }

            shareTitle = "[视频]" + title;
            shareUrl = SPUtils.getShareDefaultUrl(getContext(), SysConfigType.URL_LIVE_SHARE_LINK) + liveId;
            shareImg = videoPicUrl;
            userName = nickName;
            shareContent = "好的东东就要一起分享!（" + userName + "）的视频,快来一起看吧!";
        }

        if (t instanceof CircleCrawlInfo) {//CircleCrawlInfo
            CircleCrawlInfo data = (CircleCrawlInfo) t;

        }
    }

    public <T> void setData(Context context, T data, boolean isPlaying) {
        covertData(data);

        //发布来源
        tv_origin.setText(origin);
        //发布时间
        try {
            if (createdTime > 0) {
                String time = DateUtil.getCreateAt(createdTime);
                tv_time.setText(time);
            } else {
                tv_time.setText("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (createdTime > 0) {
            tv_time.setVisibility(VISIBLE);
        } else {
            tv_time.setVisibility(GONE);
        }
        // 关注
        if (userService.isLoginUser(userId)) {
            iv_follow_state.setVisibility(GONE);
            tv_follow_state.setVisibility(GONE);
        } else {
            tv_follow_state.setVisibility(VISIBLE);
            iv_follow_state.setVisibility(followType == 0 ? VISIBLE : GONE);
            tv_follow_state.setText(followType == 0 ? "关注" : "已关注");
        }
        //评论数
        tv_comment_num.setText((commentNum > 0 ? (commentNum > 999 ? "999+" : commentNum) : 0) + "");

        //更多
        ll_more.setVisibility(View.VISIBLE);

        //video
        video_player.setUrlAndPlayer(videoUrl, CommonUtil.getImageFullUrl(videoPicUrl), title, liveId);
        video_player.setPlayStateListener2(new YhyListVideoView.PlayStateListener() {
            @Override
            public void onPlay() {
            }

            @Override
            public void onPause() {
            }

            @Override
            public void onEnd() {
            }

            @Override
            public void onClickPlay(String url) {
                // 埋点
                if (extraMap != null) {
                    Analysis.pushEvent(context, AnEvent.ListVideoPlay,
                            new Analysis.Builder().
                                    setLat(LocationManager.getInstance().getStorage().getLast_lat()).
                                    setLng(LocationManager.getInstance().getStorage().getLast_lng()).
                                    setId(String.valueOf(liveId)).
                                    setTab(extraMap.get(Analysis.TAB)).
                                    setPosition(position).
                                    setPlayMode("手动"));
                }

                doPlay();
                if (listener != null) listener.onlyPlay();
            }

            @Override
            public void onSeek(int progress) {

            }

            @Override
            public void onTraceSeek(int offset) {
                // 埋点
                Analysis.pushEvent(context, AnEvent.PageVideoBar,
                        new Analysis.Builder().
                                setLat(LocationManager.getInstance().getStorage().getLast_lat()).
                                setLng(LocationManager.getInstance().getStorage().getLast_lng()).
                                setId(String.valueOf(liveId)).
                                setList(true).
                                setDragtime(offset));
            }
        });

        video_player.position = position;

        if (isPlaying) {
            tv_saw_number.setVisibility(GONE);
            iv_fire.setVisibility(GONE);
            VideoPlayer.getInstance().attach(this.video_player);
            if (VideoPlayer.getInstance().getCurrentState() == YHY_PLAYING || VideoPlayer.getInstance().getCurrentState() == YHY_LOAD) {
                ll_video_play.setVisibility(VISIBLE);
                rl_video_pause.setVisibility(GONE);
            } else {
                ll_video_play.setVisibility(GONE);
                rl_video_pause.setVisibility(VISIBLE);
            }
        } else {
            ll_video_play.setVisibility(GONE);
            rl_video_pause.setVisibility(VISIBLE);
            tv_saw_number.setVisibility(VISIBLE);
            iv_fire.setVisibility(VISIBLE);
            VideoPlayer.getInstance().deattach(this.video_player);
        }


        //观看或在线人数
        String num = null;
        if (saw_number >= 9990000) {
            (tv_saw_number).setText("999+万");
        } else if (saw_number >= 10000) {
            num = (new DecimalFormat("#.##").format(saw_number / 10000.0f));
            (tv_saw_number).setText(num + "万");
        } else {
            (tv_saw_number).setText(saw_number + "");
        }


        /*******************************************************     Logic Method      *****************************************************************/

        //关注
        ll_follow.setOnClickListener(v -> {
            if (followType == 0) {
                doFollow(context, userId);
            } else {
                doCancelFollow(context, userId);
            }
        });

        //评论
        ll_comment.setOnClickListener(v -> {
            if (userService.isLogin()) {
                IntentUtil.startVideoClientActivity(liveId, userId, false, liveScreenType);
            } else {
                NavUtils.gotoLoginActivity(context);
            }
        });

        //分享微信朋友圈
        iv_wx_circle.setOnClickListener(v -> {
            doShare(context, CircleShareChannel.WeMoment);
            ShareUtils.doShare(SHARE_MEDIA.WEIXIN_CIRCLE, (Activity) context, shareTitle, shareContent, videoUrl, shareImg);

        });

        //分享微信
        iv_wx.setOnClickListener(v -> {
            doShare(context, CircleShareChannel.Wechat);

            ShareUtils.doShare(SHARE_MEDIA.WEIXIN, (Activity) context, shareTitle, shareContent, videoUrl, shareImg);
        });

        //更多
        ll_more.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CircleStandardVideoLayout.this.showMoreMenu(context);
            }
        });

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 埋点
                if (extraMap != null) {
                    Analysis.pushEvent(context, AnEvent.ListVideoClick,
                            new Analysis.Builder().
                                    setLat(LocationManager.getInstance().getStorage().getLast_lat()).
                                    setLng(LocationManager.getInstance().getStorage().getLast_lng()).
                                    setId(String.valueOf(liveId)).
                                    setTab(extraMap.get(Analysis.TAB)).
                                    setPosition(position));
                    Analysis.pushEvent(context, AnEvent.PageVideoOpen,
                            new Analysis.Builder().
                                    setLat(LocationManager.getInstance().getStorage().getLast_lat()).
                                    setLng(LocationManager.getInstance().getStorage().getLast_lng()).
                                    setId(String.valueOf(liveId)).
                                    setLastPage(extraMap.get(Analysis.TAG)));
                }
                doPlay();
                IntentUtil.startVideoClientActivity(liveId, userId, false, liveScreenType);
            }
        });
    }

    /***********************************************************     Something      ************************************************************************/

    public void resetVideo(boolean stop) {
    }

    public void setOnlyPlay(OnlyPlayListener listener) {
        this.listener = listener;
    }

    public OnlyPlayListener listener;

    public interface OnlyPlayListener {
        void onlyPlay();
    }

    public void setShare(boolean isVisible) {
        if (isVisible) {
            ll_video_play.setVisibility(VISIBLE);
            rl_video_pause.setVisibility(GONE);
        } else {
            ll_video_play.setVisibility(GONE);
            rl_video_pause.setVisibility(VISIBLE);
        }
    }


    public void setVideoMatchActivity(Activity activity) {
        if (video_player != null)
            video_player.setFullScreenActivity(activity);
    }

    /***********************************************************     Api      ************************************************************************/

    /**
     * 关注
     */
    public void doFollow(Context context, long userId) {
        Analysis.pushEvent(context, AnEvent.PageFollew,
                new Analysis.Builder().
                        setLat(LocationManager.getInstance().getStorage().getLast_lat()).
                        setLng(LocationManager.getInstance().getStorage().getLast_lng()).
                        setId(String.valueOf(liveId)).
                        setType("视频").
                        setList(true));

        if (userService.isLogin()) {

            NetManager.getInstance(context).doAddFollower(userId, new OnResponseListener<Boolean>() {
                @Override
                public void onComplete(boolean isOK, Boolean result, int errorCode, String errorMsg) {
                    if (isOK && result) {
                        followType = 1;
                        EventBus.getDefault().post(new EvBusCircleChangeFollow(userId, followType));
                    } else {
                        ToastUtil.showToast(context, StringUtil.handlerErrorCode(context, errorCode));
                    }
                }

                @Override
                public void onInternError(int errorCode, String errorMessage) {
                    ToastUtil.showToast(context, StringUtil.handlerErrorCode(context, errorCode));
                }
            });
        } else {
            NavUtils.gotoLoginActivity(context);
        }
    }

    /**
     * 取消关注
     */
    public void doCancelFollow(Context context, long userId) {
        if (userService.isLogin()) {

            NetManager.getInstance(context).doRemoveFollower(userId, new OnResponseListener<Boolean>() {
                @Override
                public void onComplete(boolean isOK, Boolean result, int errorCode, String errorMsg) {
                    if (isOK && result) {
                        followType = 0;
                        EventBus.getDefault().post(new EvBusCircleChangeFollow(userId, followType));
                    } else {
                        ToastUtil.showToast(context, StringUtil.handlerErrorCode(context, errorCode));
                    }
                }

                @Override
                public void onInternError(int errorCode, String errorMessage) {
                    ToastUtil.showToast(context, StringUtil.handlerErrorCode(context, errorCode));
                }
            });
        } else {
            NavUtils.gotoLoginActivity(context);
        }
    }

    /**
     * 点赞或取消点赞
     */
    public void doPraise(Context context, long id, String isSupport) {
        if (userService.isLogin()) {
            doPraiseOrUnPraise(context, id, ValueConstants.TYPE_PRAISE_WONDERFULL_VIDEO, ValueConstants.TYPE_AVAILABLE.equals(isSupport) ? ValueConstants.UNPRAISE : ValueConstants.PRAISE);
        } else {
            NavUtils.gotoLoginActivity(context);
        }

    }

    private void doPraiseOrUnPraise(Context context, long outId, String outType, int type) {
        // 埋点
        Analysis.pushEvent(context, AnEvent.PageLike,
                new Analysis.Builder().
                        setLat(LocationManager.getInstance().getStorage().getLast_lat()).
                        setLng(LocationManager.getInstance().getStorage().getLast_lng()).
                        setId(String.valueOf(liveId)).
                        setType("视频").
                        setList(true));

        if (userService.isLogin()) {
            NetManager.getInstance(context).doPrasizeForum(outId, outType, type, new OnResponseListener<ComSupportInfo>() {
                @Override
                public void onComplete(boolean isOK, ComSupportInfo result, int errorCode, String errorMsg) {
                    if (isOK) {
                        if (ValueConstants.TYPE_DELETED.equals(result.isSupport)) {
                            supportNum = supportNum - 1 > 0 ? supportNum - 1 : 0;
                        } else {
                            supportNum = supportNum + 1;
                        }
                        isSupport = result.isSupport;
                        EventBus.getDefault().post(new EvBusCircleChangePraise(outId, result.isSupport));

                    } else {
                        ToastUtil.showToast(context, StringUtil.handlerErrorCode(context, errorCode));

                    }
                }

                @Override
                public void onInternError(int errorCode, String errorMessage) {
                    ToastUtil.showToast(context, StringUtil.handlerErrorCode(context, errorCode));

                }
            });
        } else {
            NavUtils.gotoLoginActivity(context);
        }
    }


    /**
     * 更多菜单
     *
     * @param context
     */
    public void showMoreMenu(Context context) {
        ShareUtils.ShareExtraInfo shareExtraInfo = new ShareUtils.ShareExtraInfo();
        shareExtraInfo.needDoShare = true;
        shareExtraInfo.bizId = liveId;
        shareExtraInfo.bizType = CircleBizType.SNS_VEDIO;
        ShareUtils.showShareBoard((Activity) context, shareTitle, shareContent, shareUrl, shareImg, false, true,
                () -> NavUtils.gotoComplaintList(context, title,
                        picList != null && picList.size() > 0 ? new ArrayList<>(picList) : new ArrayList<>(), id),
                null, null,
//              followType != 0 ? (ShareUtils.OnCancleFollowListener) () -> showCancelDialog(context) : null,
                userId == userService.getLoginUserId() ? (ShareUtils.OnDeleteListener) () -> doDelete(context) : null,
                () -> CircleStandardVideoLayout.this.doPraise(CircleStandardVideoLayout.this.getContext(), liveId, isSupport), supportNum, isSupport, shareExtraInfo);
    }

//    private Dialog mCancelDialog;    //取消关注
//
//    private void showCancelDialog(Context context) {
//        if (mCancelDialog == null) {
//            mCancelDialog = DialogUtil.showMessageDialog(context, "是否不再关注此人？", "",
//                    context.getString(R.string.label_btn_ok), context.getString(R.string.cancel), v -> {
//                        doCancelFollow(context, userId);
//                        mCancelDialog.dismiss();
//                    }, v -> mCancelDialog.dismiss());
//        }
//        mCancelDialog.show();
//    }

    private Dialog mDialog;          //删除本条动态对话框

    private void doDelete(Context context) {
        if (mDialog == null) {
            mDialog = DialogUtil.showMessageDialog(context, context.getString(R.string.del_photo), context.getString(R.string.delete_notice),
                    context.getString(R.string.label_btn_ok), context.getString(R.string.label_btn_cancel), v1 -> {
                        deleteSubject(context, id);
                        mDialog.dismiss();
                    }, v1 -> mDialog.dismiss());
        }
        mDialog.show();
    }

    public void deleteSubject(Context context, long subjectId) {
        NetManager.getInstance(context).doDelUGC(subjectId, new OnResponseListener<Boolean>() {
            @Override
            public void onComplete(boolean isOK, Boolean result, int errorCode, String errorMsg) {
                if (isOK && result) {
                    EventBus.getDefault().post(new EvBusCircleDelete(id));
                } else {
                    ToastUtil.showToast(context, StringUtil.handlerErrorCode(context, errorCode));
                }
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                ToastUtil.showToast(context, StringUtil.handlerErrorCode(context, errorCode));
            }
        });
    }


    /**
     * 点击事件上传服务器（获取积分）
     */
    public void doPlay() {
        YhyCallback<Response<PlayResp>> callback = new YhyCallback<Response<PlayResp>>() {
            @Override
            public void onSuccess(Response<PlayResp> data) {
            }

            @Override
            public void onFail(YhyCode code, Exception exception) {
            }
        };
        new ActivityCenterApi().play(new PlayReq(new PlayReq.Companion.P(liveId, CircleBizType.SNS_VEDIO)), callback).execAsync();
    }

    /**
     * 点击事件上传服务器（获取积分）
     */
    public void doShare(Context context, String channel) {
        // 埋点
        Analysis.pushEvent(context, AnEvent.PageShare,
                new Analysis.Builder().
                        setLat(LocationManager.getInstance().getStorage().getLast_lat()).
                        setLng(LocationManager.getInstance().getStorage().getLast_lng()).
                        setId(String.valueOf(liveId)).
                        setType("视频").
                        setList(true));


        YhyCallback<Response<ShareResp>> callback = new YhyCallback<Response<ShareResp>>() {
            @Override
            public void onSuccess(Response<ShareResp> data) {
            }

            @Override
            public void onFail(YhyCode code, Exception exception) {
            }
        };
        new ActivityCenterApi().share(new ShareReq(new ShareReq.Companion.P(liveId, CircleBizType.SNS_VEDIO, channel)), callback).execAsync();
    }
}
