package com.newyhy.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.androidkun.xtablayout.XTabLayout;
import com.newyhy.adapter.live.UserAllVideosAdapter;
import com.newyhy.config.CircleBizType;
import com.newyhy.fragment.live.CommentFragment;
import com.newyhy.fragment.live.ZanListFragment;
import com.newyhy.adapter.live.VideoCommentAndZanAdapter;
import com.newyhy.utils.ShareUtils;
import com.newyhy.utils.live.AlphaAnimationUtils;
import com.newyhy.views.RoundImageView;
import com.newyhy.views.TXVideoView;
import com.newyhy.views.dialog.InputMsgDialog;
import com.quanyan.base.util.DateUtil;
import com.quanyan.base.util.HarwkinLogUtil;
import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.BaseApplication;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.AnalyDataValue;
import com.quanyan.yhy.common.SysConfigType;
import com.quanyan.yhy.libanalysis.AnEvent;
import com.quanyan.yhy.libanalysis.Analysis;
import com.quanyan.yhy.net.NetManager;
import com.quanyan.yhy.net.lsn.OnResponseListener;
import com.quanyan.yhy.ui.base.utils.DialogUtil;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.base.utils.PageNameUtils;
import com.quanyan.yhy.ui.base.utils.TCEventHelper;
import com.quanyan.yhy.ui.club.clubcontroller.ClubController;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.discovery.controller.DiscoverController;
import com.quanyan.yhy.ui.login.LoginActivity;
import com.smart.sdk.api.resp.Api_SNSCENTER_SnsLiveRecordResult;
import com.umeng.analytics.MobclickAgent;
import com.videolibrary.controller.LiveController;
import com.videolibrary.core.NetBroadCast;
import com.videolibrary.metadata.LiveTypeConstants;
import com.videolibrary.metadata.NetStateBean;
import com.videolibrary.utils.IntentUtil;
import com.videolibrary.utils.NetWorkUtil;
import com.yhy.common.base.BaseNewActivity;
import com.yhy.common.beans.net.model.comment.ComSupportInfo;
import com.yhy.common.beans.net.model.comment.CommetDetailInfo;
import com.yhy.common.beans.net.model.discover.UgcInfoResult;
import com.yhy.common.beans.net.model.master.TalentInfo;
import com.yhy.common.config.ContextHelper;
import com.yhy.common.constants.IntentConstants;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.eventbus.event.EvBusVideoCommentChange;
import com.yhy.common.eventbus.event.EvBusVideoPraiseChange;
import com.yhy.common.listener.OnMultiClickListener;
import com.yhy.common.utils.CommonUtil;
import com.yhy.common.utils.SPUtils;
import com.yhy.imageloader.ImageLoadManager;
import com.yhy.location.LocationManager;
import com.yhy.network.YhyCallback;
import com.yhy.network.YhyCode;
import com.yhy.network.api.snscenter.SnsCenterApi;
import com.yhy.network.req.snscenter.GetShortVideoDetail;
import com.yhy.network.req.snscenter.GetUserReplayReq;
import com.yhy.network.req.snscenter.GetUserSuperbListReq;
import com.yhy.network.resp.Response;
import com.yhy.network.resp.snscenter.GetReplayByUserIdResp;
import com.yhy.network.resp.snscenter.GetShortVideoDetailResp;
import com.yhy.network.resp.snscenter.GetUserSuperbListResp;
import com.yhy.router.RouterPath;
import com.yhy.service.IUserService;
import com.ymanalyseslibrary.secon.YmAnalyticsEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.greenrobot.event.EventBus;

import static com.newyhy.views.TXVideoView.Pause;
import static com.newyhy.views.TXVideoView.Playing;
import static com.tencent.rtmp.TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN;
import static com.videolibrary.controller.LiveController.getInstance;
import static com.yhy.common.constants.ValueConstants.MSG_GET_MASTER_DETAIL_OK;
import static com.yhy.common.constants.ValueConstants.MSG_LIVE_DEATIL_ERROR;
import static com.yhy.common.constants.ValueConstants.MSG_LIVE_DETAIL_OK;
import static com.yhy.common.constants.ValueConstants.TYPE_AVAILABLE;
import static com.yhy.common.constants.ValueConstants.TYPE_COMMENT_LIVESUP;
import static com.yhy.common.constants.ValueConstants.TYPE_COMMENT_WONDERFULL_VIDEO;
import static com.yhy.common.constants.ValueConstants.TYPE_PRAISE_LIVESUP;
import static com.yhy.common.constants.ValueConstants.TYPE_PRAISE_WONDERFULL_VIDEO;

@Route(path = RouterPath.ACTIVITY_HORIZONTAL_REPLAY)
public class HorizontalReplayActivity extends BaseNewActivity implements View.OnClickListener {

    private AppBarLayout appBarLayout;
    private RelativeLayout et_comment;
    private XTabLayout tab_comment_zan;
    private ViewPager vp_comment_zan;

    private ArrayList<Fragment> fragments = new ArrayList<>();
    private CommentFragment commentFragment;
    private ZanListFragment zanListFragment;
    private InputMsgDialog inputMsgDialog;
    private int mLastDiff;

    boolean isPlaying = false;
    private boolean isNetBroadRegisted = false;
    private TXVideoView videoView;

    private NetBroadCast mNetBroadCast;
    private TextView tv_anchor_name, tv_publish_time, tv_follow, top_tv_anchor_name,
            top_tv_follow_count, top_btn_follow, top_tv_tittle, tv_view_video_num, tv_show_more;
    private ImageView iv_anchor_head, top_iv_anchor_head, ivZan, iv_arrow_down, ib_finish, ib_share;
    private RecyclerView rl_all_video;
    private List<GetUserSuperbListResp.LiveRecordResult> list = new ArrayList<>();
    private UserAllVideosAdapter adapter;

    @Autowired(name = "liveId")
    long mLiveId;
    @Autowired(name = "anchorId")
    long mAnchorUserId;
    @Autowired(name = "ugcId")
    long ugcId;
    @Autowired
    IUserService userService;
    private ClubController mClubController;
    private boolean isSupport = false;    //   先模拟一下点赞，还没有参数
    private int pageIndex;
    private String praiseOutType;
    private String commenOutType;
    private UgcInfoResult result;
    private int outType;
    private List<String> notInLivesId = new ArrayList<>();

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (videoView != null) {
                videoView.pausePlay();
            }
        }
    };

    @Override
    protected int setLayoutId() {
        return R.layout.ac_horizontal_replay;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initVars() {
        super.initVars();
        mClubController = new ClubController(this, mHandler);

    }

    @Override
    protected void initView() {
        super.initView();
        appBarLayout = findViewById(R.id.appBarLayout);
        et_comment = findViewById(R.id.et_comment);
        et_comment.setOnClickListener(this);
        findViewById(R.id.iv_show_comment).setOnClickListener(this);
        ivZan = findViewById(R.id.iv_zan);
        tab_comment_zan = findViewById(R.id.tab_comment_zan);
        vp_comment_zan = findViewById(R.id.vp_comment_zan);

        videoView = findViewById(R.id.horizontal_video_view);
        videoView.initPlayer(TXVideoView.TX_VOD_PLAYER, TXVideoView.HORIZONTAL_VOD, RENDER_MODE_FULL_FILL_SCREEN);
        tv_anchor_name = findViewById(R.id.tv_name);
        tv_publish_time = findViewById(R.id.tv_publish_time);
        iv_anchor_head = findViewById(R.id.ib_anchor_head);
        tv_follow = findViewById(R.id.tv_follow);
        top_iv_anchor_head = findViewById(R.id.top_iv_anchor_head);
        top_tv_anchor_name = findViewById(R.id.top_tv_anchor_name);
        top_tv_follow_count = findViewById(R.id.top_tv_follow_count);
        top_btn_follow = findViewById(R.id.top_btn_follow);
        top_tv_tittle = findViewById(R.id.top_tv_tittle);
        ib_finish = findViewById(R.id.ib_finish);
        ib_share = findViewById(R.id.ib_share);
        tv_view_video_num = findViewById(R.id.tv_view_video_num);
        iv_arrow_down = findViewById(R.id.iv_arrow_down);
        rl_all_video = findViewById(R.id.rc_all_video);
        tv_show_more = findViewById(R.id.tv_show_more);
    }

    @Override
    protected void setListener() {
        super.setListener();
        mNetBroadCast = new NetBroadCast();
        EventBus.getDefault().register(this);

        ivZan.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                if (mLiveId > 0) {
                    if (mLiveRecordResult == null) return;

                    if (mLiveRecordResult.outType == 1) {
                        // 埋点
                        Analysis.pushEvent(HorizontalReplayActivity.this, AnEvent.PageLike,
                                new Analysis.Builder().
                                        setLat(LocationManager.getInstance().getStorage().getLast_lat()).
                                        setLng(LocationManager.getInstance().getStorage().getLast_lng()).
                                        setId(String.valueOf(mLiveId)).
                                        setType("直播").
                                        setList(false));

                    } else {
                        // 埋点
                        Analysis.pushEvent(HorizontalReplayActivity.this, AnEvent.PageLike,
                                new Analysis.Builder().
                                        setLat(LocationManager.getInstance().getStorage().getLast_lat()).
                                        setLng(LocationManager.getInstance().getStorage().getLast_lng()).
                                        setId(String.valueOf(mLiveId)).
                                        setType("视频").
                                        setList(false));

                    }

                } else {
                    // 埋点
                    Analysis.pushEvent(HorizontalReplayActivity.this, AnEvent.PageLike,
                            new Analysis.Builder().
                                    setLat(LocationManager.getInstance().getStorage().getLast_lat()).
                                    setLng(LocationManager.getInstance().getStorage().getLast_lng()).
                                    setId(String.valueOf(ugcId)).
                                    setType("小视频").
                                    setList(false));
                }
                if (userService.isLogin()) {
                    mClubController.doAddNewPraiseToComment(HorizontalReplayActivity.this, ugcId, praiseOutType, isSupport ? ValueConstants.UNPRAISE : ValueConstants.PRAISE);
                } else {
                    NavUtils.gotoLoginActivity(HorizontalReplayActivity.this);
                }
            }
        });
        ib_finish.setOnClickListener(this);
        ib_share.setOnClickListener(this);
        tv_follow.setOnClickListener(this);
        top_btn_follow.setOnClickListener(this);
        iv_anchor_head.setOnClickListener(this);
        top_iv_anchor_head.setOnClickListener(this);
        iv_arrow_down.setOnClickListener(this);
        iv_anchor_head.setOnClickListener(this);
        top_iv_anchor_head.setOnClickListener(this);
        tv_show_more.setOnClickListener(this);
        videoView.needShowFullScreenButton(true);
        videoView.setTXPlayerStatusListener(new TXVideoView.TXPlayerStatusListener() {
            @Override
            public void onStatusChange(int currentState) {

            }

            @Override
            public void showController(boolean show) {
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    //当前是全屏
                    AlphaAnimationUtils.alphaAnimation(findViewById(R.id.rl_video_tittle), show ? 0.0f : 1.0f, show ? 1.0f : 0.0f, 300, show);
                    AlphaAnimationUtils.alphaAnimation(findViewById(R.id.view_shadow), show ? 0.0f : 1.0f, show ? 1.0f : 0.0f, 300, show);
                }
                AlphaAnimationUtils.alphaAnimation(ib_finish, show ? 0.0f : 1.0f, show ? 1.0f : 0.0f, 300, show);
                AlphaAnimationUtils.alphaAnimation(ib_share, show ? 0.0f : 1.0f, show ? 1.0f : 0.0f, 300, show);
            }

            @Override
            public void fullScreenButtonClick() {
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    //当前是全屏
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                } else {
                    //当前是半屏
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }
            }

            @Override
            public void onSeekBarTracking(int trackProgress) {
                // 埋点
                Analysis.pushEvent(HorizontalReplayActivity.this, AnEvent.PageVideoBar,
                        new Analysis.Builder().
                                setLat(LocationManager.getInstance().getStorage().getLast_lat()).
                                setLng(LocationManager.getInstance().getStorage().getLast_lng()).
                                setId(String.valueOf(mLiveId)).
                                setList(false).
                                setDragtime(trackProgress));
            }

            @Override
            public void onCenterButtonClick(boolean isPlay) {
                String eventCode = "";
                if (mLiveId > 0) {
                    if (mLiveRecordResult.outType != 1) {
                        eventCode = AnEvent.PageVideoPlay;
                    }
                } else {
                    eventCode = AnEvent.PageSvideoPlay;
                }
                if (!isPlay) {
                    // 埋点
                    Analysis.pushEvent(HorizontalReplayActivity.this, eventCode,
                            new Analysis.Builder().
                                    setLat(LocationManager.getInstance().getStorage().getLast_lat()).
                                    setLng(LocationManager.getInstance().getStorage().getLast_lng()).
                                    setId(String.valueOf(mLiveId)).
                                    setPlayPause("播放"));

                } else {
                    // 埋点
                    Analysis.pushEvent(HorizontalReplayActivity.this, eventCode,
                            new Analysis.Builder().
                                    setLat(LocationManager.getInstance().getStorage().getLast_lat()).
                                    setLng(LocationManager.getInstance().getStorage().getLast_lng()).
                                    setId(String.valueOf(mLiveId)).
                                    setPlayPause("暂停"));
                }
            }
        });

        adapter = new UserAllVideosAdapter(R.layout.recommend_video_item, list);
        adapter.setOnLoadMoreListener(() -> {
            pageIndex++;
            if (outType == 1) {
                fetchReplayLive(new GetUserReplayReq.Companion.P(UUID.randomUUID().toString(), mAnchorUserId
                        , pageIndex, 8, notInLivesId));
            } else {
                fetchReplayLive(new GetUserSuperbListReq.Companion.P(UUID.randomUUID().toString(), mAnchorUserId
                        , pageIndex, 8,notInLivesId));
            }
        }, rl_all_video);
        rl_all_video.setLayoutManager(new GridLayoutManager(this, 2));
        int marginTop = getResources().getDimensionPixelSize(R.dimen.yhy_size_19px);
        int marginLeft = getResources().getDimensionPixelSize(R.dimen.yhy_size_16px);
        int marginRight = getResources().getDimensionPixelSize(R.dimen.yhy_size_5px);
        rl_all_video.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int position = parent.getChildAdapterPosition(view);
                if (position % 2 == 0) {
                    outRect.right = marginRight;
                    outRect.left = marginLeft;
                } else {
                    outRect.left = marginRight;
                    outRect.right = marginLeft;
                }
                outRect.top = marginTop;
            }
        });
        rl_all_video.setAdapter(adapter);


        et_comment.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            Rect r = new Rect();
            //获取当前界面可视部分
            getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
            //获取屏幕的高度
            int screenHeight = getWindow().getDecorView().getRootView().getHeight();
            //此处就是用来获取键盘的高度的， 在键盘没有弹出的时候 此高度为0 键盘弹出的时候为一个正数
            int heightDifference = screenHeight - r.bottom;

            if (heightDifference <= 0 && mLastDiff > 0) {
                //软键盘收起状态
                if (inputMsgDialog != null && inputMsgDialog.isShowing()) {
                    inputMsgDialog.dismiss();
                }
            }
            mLastDiff = heightDifference;

        });

        //来电时停止播放
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            telephonyManager.listen(new PhoneStateListener() {
                @Override
                public void onCallStateChanged(int state, String incomingNumber) {
                    super.onCallStateChanged(state, incomingNumber);
                    switch (state) {
                        case TelephonyManager.CALL_STATE_IDLE:
                            break;
                        case TelephonyManager.CALL_STATE_OFFHOOK:
                            break;
                        case TelephonyManager.CALL_STATE_RINGING:
                            if (videoView != null) {
                                videoView.pausePlay();
                            }
                            break;
                    }
                }
            }, PhoneStateListener.LISTEN_CALL_STATE);
        }

        IntentFilter filter = new IntentFilter();
        filter.addAction("com.android.deskclock.ALARM_ALERT");
        filter.addAction("com.android.deskclock.ALARM_DONE");
        registerReceiver(mReceiver, filter);
    }

    @Override
    protected void initData() {
        super.initData();
        pageIndex = 1;
        Bundle bundleExtra = getIntent().getExtras();
        if (bundleExtra != null){
            if (bundleExtra.getLong(IntentUtil.BUNDLE_LIVEID) > 0){
                mLiveId = bundleExtra.getLong(IntentUtil.BUNDLE_LIVEID);
            }
        }
        fetchData();
    }

    /**
     * 请求live详情
     */
    private void fetchData() {
        if (mLiveId == 0 && ugcId != 0) {
            DiscoverController discoverController = new DiscoverController(this, mHandler);
            discoverController.doGetLiveDetail(this, ugcId);

        } else {
            LiveController.getInstance().getLiveRecord(this, mHandler, mLiveId);
        }
    }

    private void initFragments() {

        String[] tab = new String[2];
        tab[0] = "  评论 0 ";
        tab[1] = "  点赞 0 ";

        commentFragment = CommentFragment.getInstance(ugcId, commenOutType);
        zanListFragment = new ZanListFragment();
        zanListFragment.setUgcID(ugcId, praiseOutType);
        fragments.add(commentFragment);
        fragments.add(zanListFragment);

        VideoCommentAndZanAdapter videoCommentAndZanAdapter =
                new VideoCommentAndZanAdapter(getSupportFragmentManager(), fragments, tab);
        vp_comment_zan.setAdapter(videoCommentAndZanAdapter);
        tab_comment_zan.setupWithViewPager(vp_comment_zan);
    }


    /**
     * 获得主播信息
     */
    public void fetchMasterData() {
        if (mAnchorUserId > 0) {
            getInstance().getMasterDetail(this, mHandler, mAnchorUserId);
        }
    }

    private String mLiveUrl;
    private Api_SNSCENTER_SnsLiveRecordResult mLiveRecordResult;
    private long fansCount = 0;
    private String liveTittle = "";

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case LiveController.MSG_GET_LIVE_RECORD_OK:
                mLiveRecordResult = (Api_SNSCENTER_SnsLiveRecordResult) msg.obj;
                if (mLiveRecordResult == null) return;
                if (mLiveRecordResult.userInfo != null) {
                    mAnchorUserId = mLiveRecordResult.userInfo.userId;
                }
                notInLivesId.add(String.valueOf(mLiveId));
                isSupport = TYPE_AVAILABLE.equals(mLiveRecordResult.isSupport);
                ivZan.setImageResource(isSupport ? R.drawable.ic_zaned : R.drawable.ic_un_zan);
                //请求主播信息
                fetchMasterData();
                liveTittle = mLiveRecordResult.liveTitle;
                if (mLiveRecordResult.outType == 1) {
                    praiseOutType = TYPE_PRAISE_LIVESUP;
                    commenOutType = TYPE_COMMENT_LIVESUP;
                    ugcId = mLiveRecordResult.ugcId;
                    //查询推荐回放
                    fetchReplayLive(new GetUserReplayReq.Companion.P(UUID.randomUUID().toString(), mAnchorUserId
                            , 1, 8, notInLivesId));
                } else {
                    praiseOutType = TYPE_PRAISE_WONDERFULL_VIDEO;
                    commenOutType = TYPE_COMMENT_WONDERFULL_VIDEO;
                    ugcId = mLiveId;
                    //查询推荐视频
                    fetchReplayLive(new GetUserSuperbListReq.Companion.P(UUID.randomUUID().toString(), mAnchorUserId
                            , 1, 8,notInLivesId));
                }
                outType = mLiveRecordResult.outType;
                initFragments();
                top_tv_tittle.setText(liveTittle);
                tv_view_video_num.setText(mLiveRecordResult.viewCount + "人浏览");
                try {
                    String time = com.newyhy.utils.DateUtil.getCreateAt(mLiveRecordResult.startDate);
                    tv_publish_time.setText(time);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (LiveTypeConstants.DELETE_LIVE.equals(mLiveRecordResult.status)) {
                    showSoldOutDialog(getString(R.string.label_video_deleted));
                } else if (LiveTypeConstants.OFF_SHELVE_LIVE.equals(mLiveRecordResult.status)) {
                    showSoldOutDialog(getString(R.string.label_video_sold_out));
                } else {
                    if (mLiveRecordResult.replayUrls != null && mLiveRecordResult.replayUrls.size() > 0) {
                        mLiveUrl = "";
                        if (mLiveRecordResult.replayUrls.get(0).startsWith("http://")) {
                            mLiveUrl = mLiveRecordResult.replayUrls.get(0);
                        } else {
                            mLiveUrl = "http://".concat(mLiveRecordResult.replayUrls.get(0));
                        }
                        registNet();
                    }
                }
                break;
            case LiveController.MSG_GET_LIVE_RECORD_ERROR:
                Toast.makeText(this, "网络异常!", Toast.LENGTH_SHORT).show();
                break;
            case MSG_GET_MASTER_DETAIL_OK:
                //the master detail info
                TalentInfo talentInfo = (TalentInfo) msg.obj;
                if (talentInfo == null) return;
                setAnchorData(talentInfo);
                break;
            case ValueConstants.MSG_COMMENT_OK:
                //评论成功
                updataViews(0);
                EventBus.getDefault().post(new EvBusVideoCommentChange(ugcId,mLiveId,true));
                break;
            case ValueConstants.MSG_COMMENT_ERROR:
                ToastUtil.showToast(this, StringUtil.handlerErrorCode(this, msg.arg1));
                break;
            case ValueConstants.MSG_PRAISE_OK:
                ComSupportInfo comSupportInfo = (ComSupportInfo) msg.obj;
                isSupport = !isSupport;
                ivZan.setImageResource(isSupport ? R.drawable.ic_zaned : R.drawable.ic_un_zan);
                updataViews(1);
                EventBus.getDefault().post(new EvBusVideoPraiseChange(mLiveId,ugcId,isSupport));
                break;
            case ValueConstants.MSG_PRAISE_ERROR:
                ToastUtil.showToast(HorizontalReplayActivity.this, StringUtil.handlerErrorCode(this, msg.arg1));
                break;
            case MSG_LIVE_DETAIL_OK:
                //通过ugcId获得详情
                result = (UgcInfoResult) msg.obj;
                if (result == null) return;
                mLiveUrl = ContextHelper.getVodUrl() + result.videoUrl;
                registNet();
                doAddViewCount(new GetShortVideoDetail.Companion.P(ugcId));
                praiseOutType = TYPE_PRAISE_LIVESUP;
                commenOutType = TYPE_COMMENT_LIVESUP;
                if (result.userInfo != null) {
                    mAnchorUserId = result.userInfo.userId;
                    isSupport = TYPE_AVAILABLE.equals(result.isSupport);
                    ivZan.setImageResource(isSupport ? R.drawable.ic_zaned : R.drawable.ic_un_zan);
                    tv_view_video_num.setText(result.viewNum + "人浏览");
                }

                fetchMasterData();
                initFragments();
                break;
            case MSG_LIVE_DEATIL_ERROR:
                ToastUtil.showToast(this, StringUtil.handlerErrorCode(this, msg.arg1));
                break;
        }
    }

    private void doAddViewCount(GetShortVideoDetail.Companion.P p) {
        YhyCallback<Response<GetShortVideoDetailResp>> yhyCallback = new YhyCallback<Response<GetShortVideoDetailResp>>() {
            @Override
            public void onSuccess(Response<GetShortVideoDetailResp> data) {

            }

            @Override
            public void onFail(YhyCode code, Exception exception) {

            }
        };
        new SnsCenterApi().getShortVideoDetail(new GetShortVideoDetail(p), yhyCallback).execAsync();
    }

    /**
     * 显示回放的video
     *
     * @param replayListResult
     */
    private void setReplayData(List<GetUserSuperbListResp.LiveRecordResult> replayListResult) {
        for (int i = 0; i < replayListResult.size(); i++) {
            GetUserSuperbListResp.LiveRecordResult api_snscenter_liveRecordDTO = replayListResult.get(i);
            switch (i) {
                case 0:
                    findViewById(R.id.rv_1).setVisibility(View.VISIBLE);
                    RoundImageView roundImageView1 = findViewById(R.id.iv_cover_1);
                    TextView tv_anchor_name_1 = findViewById(R.id.tv_anchor_name_1);
                    TextView live_first_item_tvAudienceNum = findViewById(R.id.live_first_item_tvAudienceNum);
                    TextView tv_video_name_1 = findViewById(R.id.tv_video_name_1);
                    ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(api_snscenter_liveRecordDTO.liveCover),
                            roundImageView1);
                    tv_anchor_name_1.setText(api_snscenter_liveRecordDTO.nickname);
                    live_first_item_tvAudienceNum.setText(api_snscenter_liveRecordDTO.viewCount + "");
                    tv_video_name_1.setVisibility(View.VISIBLE);
                    tv_video_name_1.setText(api_snscenter_liveRecordDTO.liveTitle);
                    roundImageView1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // 埋点
                            Analysis.pushEvent(mContext, AnEvent.PageVideoRecommend,
                                    new Analysis.Builder().
                                            setLat(LocationManager.getInstance().getStorage().getLast_lat()).
                                            setLng(LocationManager.getInstance().getStorage().getLast_lng()).
                                            setId(String.valueOf(api_snscenter_liveRecordDTO.liveId)));

                            IntentUtil.startVideoClientActivity(api_snscenter_liveRecordDTO.liveId,
                                    mAnchorUserId, !"REPLAY_LIVE".equals(api_snscenter_liveRecordDTO.liveStatus),
                                    "HORIZONTAL".equals(api_snscenter_liveRecordDTO.liveScreenType) ? 0 : 1);
                        }
                    });
                    break;
                case 1:
                    findViewById(R.id.rv_2).setVisibility(View.VISIBLE);
                    RoundImageView roundImageView2 = findViewById(R.id.iv_cover_2);
                    TextView tv_anchor_name_2 = findViewById(R.id.tv_anchor_name_2);
                    TextView live_second_item_tvAudienceNum = findViewById(R.id.live_second_item_tvAudienceNum);
                    TextView tv_video_name_2 = findViewById(R.id.tv_video_name_2);
                    ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(api_snscenter_liveRecordDTO.liveCover),
                            roundImageView2);
                    tv_anchor_name_2.setText(api_snscenter_liveRecordDTO.nickname);
                    live_second_item_tvAudienceNum.setText(api_snscenter_liveRecordDTO.viewCount + "");
                    tv_video_name_2.setVisibility(View.VISIBLE);
                    tv_video_name_2.setText(api_snscenter_liveRecordDTO.liveTitle);
                    roundImageView2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // 埋点
                            Analysis.pushEvent(mContext, AnEvent.PageVideoRecommend,
                                    new Analysis.Builder().
                                            setLat(LocationManager.getInstance().getStorage().getLast_lat()).
                                            setLng(LocationManager.getInstance().getStorage().getLast_lng()).
                                            setId(String.valueOf(api_snscenter_liveRecordDTO.liveId)));

                            IntentUtil.startVideoClientActivity(api_snscenter_liveRecordDTO.liveId,
                                    mAnchorUserId, !"REPLAY_LIVE".equals(api_snscenter_liveRecordDTO.liveStatus),
                                    "HORIZONTAL".equals(api_snscenter_liveRecordDTO.liveScreenType) ? 0 : 1);
                        }
                    });
                    break;
                case 2:
                    findViewById(R.id.rv_3).setVisibility(View.VISIBLE);
                    RoundImageView roundImageView3 = findViewById(R.id.iv_cover_3);
                    TextView tv_anchor_name_3 = findViewById(R.id.tv_anchor_name_3);
                    TextView live_third_item_tvAudienceNum = findViewById(R.id.live_third_item_tvAudienceNum);
                    TextView tv_video_name_3 = findViewById(R.id.tv_video_name_3);
                    ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(api_snscenter_liveRecordDTO.liveCover),
                            roundImageView3);
                    tv_anchor_name_3.setText(api_snscenter_liveRecordDTO.nickname);
                    live_third_item_tvAudienceNum.setText(api_snscenter_liveRecordDTO.viewCount + "");
                    tv_video_name_3.setVisibility(View.VISIBLE);
                    tv_video_name_3.setText(api_snscenter_liveRecordDTO.liveTitle);
                    roundImageView3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // 埋点
                            Analysis.pushEvent(mContext, AnEvent.PageVideoRecommend,
                                    new Analysis.Builder().
                                            setLat(LocationManager.getInstance().getStorage().getLast_lat()).
                                            setLng(LocationManager.getInstance().getStorage().getLast_lng()).
                                            setId(String.valueOf(api_snscenter_liveRecordDTO.liveId)));

                            IntentUtil.startVideoClientActivity(api_snscenter_liveRecordDTO.liveId,
                                    mAnchorUserId, !"REPLAY_LIVE".equals(api_snscenter_liveRecordDTO.liveStatus),
                                    "HORIZONTAL".equals(api_snscenter_liveRecordDTO.liveScreenType) ? 0 : 1);
                        }
                    });
                    break;
                case 3:
                    findViewById(R.id.rv_4).setVisibility(View.VISIBLE);
                    RoundImageView roundImageView4 = findViewById(R.id.iv_cover_4);
                    TextView tv_anchor_name_4 = findViewById(R.id.tv_anchor_name_4);
                    TextView live_fourth_item_tvAudienceNum = findViewById(R.id.live_fourth_item_tvAudienceNum);
                    TextView tv_video_name_4 = findViewById(R.id.tv_video_name_4);
                    ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(api_snscenter_liveRecordDTO.liveCover),
                            roundImageView4);
                    tv_anchor_name_4.setText(api_snscenter_liveRecordDTO.nickname);
                    live_fourth_item_tvAudienceNum.setText(api_snscenter_liveRecordDTO.viewCount + "");
                    tv_video_name_4.setVisibility(View.VISIBLE);
                    tv_video_name_4.setText(api_snscenter_liveRecordDTO.liveTitle);
                    roundImageView4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // 埋点
                            Analysis.pushEvent(mContext, AnEvent.PageVideoRecommend,
                                    new Analysis.Builder().
                                            setLat(LocationManager.getInstance().getStorage().getLast_lat()).
                                            setLng(LocationManager.getInstance().getStorage().getLast_lng()).
                                            setId(String.valueOf(api_snscenter_liveRecordDTO.liveId)));

                            IntentUtil.startVideoClientActivity(api_snscenter_liveRecordDTO.liveId,
                                    mAnchorUserId, !"REPLAY_LIVE".equals(api_snscenter_liveRecordDTO.liveStatus),
                                    "HORIZONTAL".equals(api_snscenter_liveRecordDTO.liveScreenType) ? 0 : 1);
                        }
                    });
                    break;
            }
        }
    }

    /**
     * 关注
     */
    public void doFollow(Context context, long userId) {
        if (userService.isLogin()) {

            NetManager.getInstance(context).doAddFollower(userId, new OnResponseListener<Boolean>() {
                @Override
                public void onComplete(boolean isOK, Boolean result, int errorCode, String errorMsg) {
                    if (isOK && result) {
                        top_tv_follow_count.setText("粉丝 " + (fansCount + 1));
                        tv_follow.setBackground(getResources().getDrawable(R.drawable.bg_horizontal_followed));
                        tv_follow.setTextColor(getResources().getColor(R.color.unselect));
                        tv_follow.setText("已关注");
                        tv_follow.setEnabled(false);
                        top_btn_follow.setBackground(getResources().getDrawable(R.drawable.bg_followed));
                        top_btn_follow.setEnabled(false);
                        top_btn_follow.setText("已关注");

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
     * 查询推荐视频
     */
    private void fetchReplayLive(GetUserSuperbListReq.Companion.P p) {
        YhyCallback<Response<GetUserSuperbListResp>> yhyCallback = new YhyCallback<Response<GetUserSuperbListResp>>() {
            @Override
            public void onSuccess(Response<GetUserSuperbListResp> data) {
                if (data == null || isFinishing()) return;
                if (data.getContent().list == null) return;
                if (pageIndex == 1) {
                    setReplayData(data.getContent().list);
                }
                list.addAll(data.getContent().list);
                adapter.notifyDataSetChanged();
                if (data.getContent().hasNext) {
                    adapter.loadMoreComplete();
                } else {
                    adapter.loadMoreEnd(true);
                }
            }

            @Override
            public void onFail(YhyCode code, Exception exception) {

            }
        };
        new SnsCenterApi().getUserSuperbList(new GetUserSuperbListReq(p), yhyCallback).execAsync();
    }

    /**
     * 查询主播回放
     */
    private void fetchReplayLive(GetUserReplayReq.Companion.P p) {
        YhyCallback<Response<GetReplayByUserIdResp>> yhyCallback = new YhyCallback<Response<GetReplayByUserIdResp>>() {
            @Override
            public void onSuccess(Response<GetReplayByUserIdResp> data) {
                if (data == null || isFinishing()) return;
                if (data.getContent().list == null) return;
                if (pageIndex == 1) {
                    setReplayData(data.getContent().list);
                }
                list.addAll(data.getContent().list);
                adapter.notifyDataSetChanged();
                if (data.getContent().hasNext) {
                    adapter.loadMoreComplete();
                } else {
                    adapter.loadMoreEnd(true);
                }
            }

            @Override
            public void onFail(YhyCode code, Exception exception) {

            }
        };
        new SnsCenterApi().getUserReplayList(new GetUserReplayReq(p), yhyCallback).execAsync();
    }

    /**
     * 设置主播信息
     *
     * @param talentInfo
     */
    private void setAnchorData(TalentInfo talentInfo) {
        ImageLoadManager.loadCircleImage(CommonUtil.getImageFullUrl(talentInfo.avatar),
                R.mipmap.head_protriat_default, iv_anchor_head);
        ImageLoadManager.loadCircleImage(CommonUtil.getImageFullUrl(talentInfo.avatar),
                R.mipmap.head_protriat_default, top_iv_anchor_head);
        tv_anchor_name.setText(talentInfo.nickName);
        top_tv_anchor_name.setText(talentInfo.nickName);
        fansCount = talentInfo.fansCount;
        top_tv_follow_count.setText("粉丝 " + talentInfo.fansCount);
        if (mAnchorUserId == userService.getLoginUserId()) {
            tv_follow.setBackground(getResources().getDrawable(R.drawable.bg_horizontal_followed));
            top_btn_follow.setBackground(getResources().getDrawable(R.drawable.bg_followed));
            tv_follow.setEnabled(false);
            top_btn_follow.setEnabled(false);
            return;
        }
        if (!userService.isLogin()) {
            tv_follow.setBackground(getResources().getDrawable(R.drawable.un_follow_bg));
            return;
        }
        if ("NONE".equals(talentInfo.attentionType)) {
            tv_follow.setBackground(getResources().getDrawable(R.drawable.un_follow_bg));
            top_btn_follow.setBackground(getResources().getDrawable(R.drawable.un_follow_bg));
        } else {
            tv_follow.setBackground(getResources().getDrawable(R.drawable.bg_horizontal_followed));
            tv_follow.setTextColor(getResources().getColor(R.color.unselect));
            tv_follow.setEnabled(false);
            tv_follow.setText("已关注");
            top_btn_follow.setBackground(getResources().getDrawable(R.drawable.bg_followed));
            top_btn_follow.setEnabled(false);
            top_btn_follow.setText("已关注");
        }
    }

    private Dialog mSoldOutDialog;

    /**
     * 视频下架的提示
     */
    private void showSoldOutDialog(String notice) {
        if (mSoldOutDialog == null) {
            mSoldOutDialog = DialogUtil.showMessageDialog(this, null, notice,
                    getString(R.string.label_btn_ok),
                    null, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mSoldOutDialog.dismiss();
                            finish();
                        }
                    }, null);
        }
        mSoldOutDialog.show();
    }

    private void registNet() {
        isNetBroadRegisted = true;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mNetBroadCast, intentFilter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_show_comment:
                if (Math.abs(appBarLayout.getY()) == appBarLayout.getTotalScrollRange()) {
                    commentFragment.smoothScrollToTop();
                    zanListFragment.smoothScrollToTop();
                    vp_comment_zan.setCurrentItem(0);
                    appBarLayout.setExpanded(true, true);
                } else {
                    appBarLayout.setExpanded(false, true);
                }
                break;
            case R.id.et_comment:
                if (inputMsgDialog == null) {
                    inputMsgDialog = new InputMsgDialog(this, R.style.Theme_Light_Dialog);
                    inputMsgDialog.setOnShowListener(dialog ->
                            mHandler.postDelayed(() -> inputMsgDialog.showSoftInput(), 100));
                    inputMsgDialog.setSendMsgClickCallBack(this::doPostComment);
                    inputMsgDialog.show();

                } else {
                    if (!inputMsgDialog.isShowing()) {
                        inputMsgDialog.show();
                    }
                }
                break;
            case R.id.tv_publish_comment:

                break;
            case R.id.ib_anchor_head:
                //主播头像点击
                NavUtils.gotoMasterHomepage(this, mAnchorUserId);
                break;
            case R.id.top_iv_anchor_head:
                NavUtils.gotoMasterHomepage(this, mAnchorUserId);
                break;
            case R.id.tv_follow:
                TCEventHelper.onEvent(v.getContext(), AnalyDataValue.LIVE_DETAIL_ATTENTION_CLICK);
                if (mLiveId > 0) {
                    if (mLiveRecordResult == null) return;

                    if (mLiveRecordResult.outType == 1) {
                        // 埋点
                        Analysis.pushEvent(this, AnEvent.PageFollew,
                                new Analysis.Builder().
                                        setLat(LocationManager.getInstance().getStorage().getLast_lat()).
                                        setLng(LocationManager.getInstance().getStorage().getLast_lng()).
                                        setId(String.valueOf(mLiveId)).
                                        setType("直播").
                                        setList(false));

                    } else {
                        // 埋点
                        Analysis.pushEvent(this, AnEvent.PageFollew,
                                new Analysis.Builder().
                                        setLat(LocationManager.getInstance().getStorage().getLast_lat()).
                                        setLng(LocationManager.getInstance().getStorage().getLast_lng()).
                                        setId(String.valueOf(mLiveId)).
                                        setType("视频").
                                        setList(false));

                    }

                }
                doFollow(this, mAnchorUserId);
                break;
            case R.id.top_btn_follow:
                TCEventHelper.onEvent(v.getContext(), AnalyDataValue.LIVE_DETAIL_ATTENTION_CLICK);
                doFollow(this, mAnchorUserId);
                break;
            case R.id.ib_finish:
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    //当前是全屏
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                } else {
                    //当前是半屏
                    finish();
                }
                break;
            case R.id.ib_share:
                share();
                break;
            case R.id.tv_show_more:
                findViewById(R.id.fl_all_video).setVisibility(View.VISIBLE);
                break;
            case R.id.iv_arrow_down:
                findViewById(R.id.fl_all_video).setVisibility(View.GONE);
                break;
        }
    }

    /**
     * 发表评论
     */
    private void doPostComment(String content) {
        // 埋点
        if (mLiveId > 0) {
            if (mLiveRecordResult == null) return;

            if (mLiveRecordResult.outType == 1) {
                Analysis.pushEvent(this, AnEvent.PageComment,
                        new Analysis.Builder().
                                setLat(LocationManager.getInstance().getStorage().getLast_lat()).
                                setLng(LocationManager.getInstance().getStorage().getLast_lng()).
                                setId(String.valueOf(mLiveId)).
                                setType("直播"));
            } else {
                Analysis.pushEvent(this, AnEvent.PageComment,
                        new Analysis.Builder().
                                setLat(LocationManager.getInstance().getStorage().getLast_lat()).
                                setLng(LocationManager.getInstance().getStorage().getLast_lng()).
                                setId(String.valueOf(mLiveId)).
                                setType("视频"));
            }

        }


        if (userService.isLogin()) {
            if (!TextUtils.isEmpty(content)) {
                boolean flag = content.contentEquals("\r\n");
                if (!flag && TextUtils.isEmpty(content.trim())) {
                    ToastUtil.showToast(HorizontalReplayActivity.this, getString(R.string.notice_is_all_space));
                    return;
                }
                CommetDetailInfo commetDetailInfo = new CommetDetailInfo();
                commetDetailInfo.outType = commenOutType;
                commetDetailInfo.userId = userService.getLoginUserId();
                commetDetailInfo.textContent = content;
                commetDetailInfo.outId = ugcId;
//                if (mCommentInfo != null && mCommentInfo.createUserInfo != null) {
//                    commetDetailInfo.replyToUserId = mCommentInfo.createUserInfo.userId;
//                } else {
//                    HarwkinLogUtil.error("直播详情    评论的用户对象为---》null");
//                }
                mClubController.doPostComment(HorizontalReplayActivity.this, commetDetailInfo);
            } else {
                ToastUtil.showToast(HorizontalReplayActivity.this, "评论不能为空哦");
            }
        } else {
            NavUtils.gotoLoginActivity(HorizontalReplayActivity.this);
        }

    }

    /**
     * 点赞，评论成功后刷新列表
     *
     * @param pos
     */
    private void updataViews(int pos) {
        switch (pos) {
            case 0:
                if (0 != vp_comment_zan.getCurrentItem()) {
                    vp_comment_zan.setCurrentItem(0);
                }
                commentFragment.updateData();
                break;
            case 1:
                if (1 != vp_comment_zan.getCurrentItem()) {
                    vp_comment_zan.setCurrentItem(1);
                }
                zanListFragment.updateData();
                break;
        }
    }

    /**
     * 网络状态变化
     *
     * @param netStateBean
     */
    public void onEvent(NetStateBean netStateBean) {
        switch (netStateBean.getNetState()) {
            case NetWorkUtil.NETWORK_NONE:
                //ToastUtil.showToast(this, "当前没有网络, 请连接后再试");
                if (videoView != null) {
                    videoView.pausePlay();
                }
                break;
            case NetWorkUtil.NETWORK_2G:
                //ToastUtil.showToast(this, "当前使用的是2G网络");

            case NetWorkUtil.NETWORK_3G:
                //ToastUtil.showToast(this, "当前使用的时3G网络");

            case NetWorkUtil.NETWORK_4G:
                //ToastUtil.showToast(this, "当前使用的4G网络");

            case NetWorkUtil.NETWORK_MOBILE:
                //ToastUtil.showToast(this, "您正在使用移动网络");
                showNetDialog(getString(R.string.net_look_info_live));

                break;
            case NetWorkUtil.NETWORK_WIFI:
                //ToastUtil.showToast(this, "已连接至WIFI");
                if (!isPlaying) {
                    isPlaying = true;
                    if (!StringUtil.isEmpty(mLiveUrl) && videoView != null) {
                        videoView.starPlay(mLiveUrl);
                    }
                }
                break;
        }
    }

    Dialog liveNetDialog;

    private void showNetDialog(String notic) {
        liveNetDialog = DialogUtil.showMessageDialog(this, null, notic, "继续观看", "退出观看", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!StringUtil.isEmpty(mLiveUrl) && videoView != null) {
                    videoView.starPlay(mLiveUrl);
                }
                liveNetDialog.dismiss();
            }
        }, v -> {
            finish();
            liveNetDialog.dismiss();
        });
        liveNetDialog.show();
    }

    /**
     * 分享
     */
    public void share() {
        // 埋点
        if (mLiveId > 0) {
            if (mLiveRecordResult == null) return;

            if (mLiveRecordResult.outType == 1) {
                // 埋点
                Analysis.pushEvent(this, AnEvent.PageShare,
                        new Analysis.Builder().
                                setLat(LocationManager.getInstance().getStorage().getLast_lat()).
                                setLng(LocationManager.getInstance().getStorage().getLast_lng()).
                                setId(String.valueOf(mLiveId)).
                                setType("直播").
                                setList(false));
            } else {
                // 埋点
                Analysis.pushEvent(this, AnEvent.PageShare,
                        new Analysis.Builder().
                                setLat(LocationManager.getInstance().getStorage().getLast_lat()).
                                setLng(LocationManager.getInstance().getStorage().getLast_lng()).
                                setId(String.valueOf(mLiveId)).
                                setType("视频").
                                setList(false));
            }

        }

        if (!userService.isLogin()) {
            LoginActivity.gotoLoginActivity(this);
        } else {
            String shareTitle = "";
            String videoUrl = "";
            String shareContent = "";
            String shareImg = "";
            String userName = "";
            if (mLiveRecordResult != null && mLiveRecordResult.userInfo != null) {
                if (mLiveRecordResult.outType == 1) {
                    shareTitle = mLiveRecordResult.liveTitle;
                    videoUrl = SPUtils.getShareDefaultUrl(this, SysConfigType.URL_LIVE_SHARE_LINK) + mLiveId;
                    userName = mLiveRecordResult.userInfo.nickname;
                    shareImg = mLiveRecordResult.liveCover;
                    //精彩视频（回放）分享的内容是  封面
                    shareContent = "好的东东就要一起分享!我正在欣赏（" + userName + "）的运动视频,快来一起看吧!";
                } else {
                    shareTitle = "[" + mLiveRecordResult.liveTitle + "]" + "这个视频好棒,快来围观";
                    videoUrl = SPUtils.getShareDefaultUrl(this, SysConfigType.URL_LIVE_SHARE_LINK) + mLiveId;
                    shareImg = mLiveRecordResult.liveCover;
                    userName = mLiveRecordResult.userInfo.nickname;
                    //精彩视频（回放）分享的内容是  封面
                    shareContent = "这个视频有点意思~";
                }
            }

            if (result != null) {
                videoUrl = SPUtils.getURL_SHORT_VIDEO(this) + ugcId;
                if (result.userInfo != null) {
                    userName = result.userInfo.nickname;
                    shareImg = result.videoPicUrl;
                    shareContent = "这个视频有点意思~";
                    shareTitle = "[" + userName + "]" + "这个视频好棒,快来围观";
                }
            }


            // 积分系统
            String bizType = "";
            if (mLiveId > 0) {
                if (mLiveRecordResult != null) {
                    if (mLiveRecordResult.outType == 1) {
                        bizType = CircleBizType.SNS_LIVE;
                    } else {
                        bizType = CircleBizType.SNS_VEDIO;
                    }
                }
            }
            ShareUtils.ShareExtraInfo shareExtraInfo = new ShareUtils.ShareExtraInfo();
            shareExtraInfo.bizType = bizType;
            shareExtraInfo.bizId = mLiveId;
            shareExtraInfo.needDoShare = true;
            ShareUtils.showShareBoard(this, shareTitle, shareContent, videoUrl, shareImg, shareExtraInfo);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case IntentConstants.LOGIN_RESULT:
                if (resultCode == -1) {
                    fetchMasterData();
                }
                break;
        }
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // 埋点
            Analysis.pushEvent(mContext, AnEvent.PageVideoFull,
                    new Analysis.Builder().
                            setLat(LocationManager.getInstance().getStorage().getLast_lat()).
                            setLng(LocationManager.getInstance().getStorage().getLast_lng()).
                            setId(String.valueOf(mLiveId)).setList(false).setFullResize("全屏"));

            //当前是全屏
            findViewById(R.id.rl_video_tittle).setVisibility(View.VISIBLE);
            ViewGroup.LayoutParams layoutParams = findViewById(R.id.rl_video_view).getLayoutParams();
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            findViewById(R.id.rl_video_view).setLayoutParams(layoutParams);
            findViewById(R.id.rl_say_something).setVisibility(View.GONE);
            findViewById(R.id.view_shadow).setVisibility(View.VISIBLE);
            ib_share.setVisibility(View.VISIBLE);
            ib_finish.setVisibility(View.VISIBLE);
            videoView.setFullScreenButtonStatus(true);
            videoView.setPlayerButtonBigger(true);
        } else {
            // 埋点
            Analysis.pushEvent(mContext, AnEvent.PageVideoFull,
                    new Analysis.Builder().
                            setLat(LocationManager.getInstance().getStorage().getLast_lat()).
                            setLng(LocationManager.getInstance().getStorage().getLast_lng()).
                            setId(String.valueOf(mLiveId)).setList(false).setFullResize("恢复"));

            //当前是半屏
            findViewById(R.id.rl_video_tittle).setVisibility(View.GONE);
            ViewGroup.LayoutParams layoutParams = findViewById(R.id.rl_video_view).getLayoutParams();
            layoutParams.height = (int) getResources().getDimension(R.dimen.yhy_size_233px);
            findViewById(R.id.rl_video_view).setLayoutParams(layoutParams);
            findViewById(R.id.rl_say_something).setVisibility(View.VISIBLE);
            findViewById(R.id.view_shadow).setVisibility(View.GONE);
            videoView.setFullScreenButtonStatus(false);
            videoView.setPlayerButtonBigger(false);
        }
    }

    private boolean isPaused = false;

    @Override
    protected void onResume() {
        super.onResume();
        HarwkinLogUtil.info(getClass().getSimpleName(), "Activity resume time : " + DateUtil.convert2String(System.currentTimeMillis(), "HH:mm:ss.SSS"));
        YmAnalyticsEvent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        HarwkinLogUtil.info(getClass().getSimpleName(), "Activity pause time : " + DateUtil.convert2String(System.currentTimeMillis(), "HH:mm:ss.SSS"));
        MobclickAgent.onPageEnd(PageNameUtils.getChineseName(this));
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (videoView != null) {
            if (isPaused) {
                isPaused = false;
                videoView.resumePlay();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (videoView != null) {
            isPaused = true;
            videoView.pausePlay();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLiveId > 0) {
            if (mLiveRecordResult == null) return;

            if (mLiveRecordResult.outType == 1) {
                // 埋点
                Analysis.pushEvent(this, AnEvent.PageLiveClose,
                        new Analysis.Builder().
                                setLat(LocationManager.getInstance().getStorage().getLast_lat()).
                                setLng(LocationManager.getInstance().getStorage().getLast_lng()).
                                setId(String.valueOf(mLiveId)).
                                setLiveState(false));
            } else {
                // 埋点
                Analysis.pushEvent(this, AnEvent.PageVideoClose,
                        new Analysis.Builder().
                                setLat(LocationManager.getInstance().getStorage().getLast_lat()).
                                setLng(LocationManager.getInstance().getStorage().getLast_lng()).
                                setId(String.valueOf(mLiveId)).
                                setFinished(videoView.isPlayedFinish()));
            }

        }

        if (mHandler != null) {
            mHandler.setValid(false);
            mHandler.removeCallbacksAndMessages(null);
        }

        if (videoView != null) {
            videoView.destroyVideoVideo();
        }
        if (isNetBroadRegisted) {
            unregisterReceiver(mNetBroadCast);
        }
        unregisterReceiver(mReceiver);
        ((BaseApplication) getApplication()).removeActivity(this);
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }

    }

    /**
     * 设置评论数量
     *
     * @param commentNum
     */
    public void setCommentNum(int commentNum) {
        if (tab_comment_zan.getTabCount() > 0) {
            tab_comment_zan.getTabAt(0).setText("评论 " + commentNum);
        }
    }

    public void setZanNum(int count) {
        if (tab_comment_zan.getTabCount() > 0) {
            tab_comment_zan.getTabAt(1).setText("点赞 " + count);
        }
    }

    @Override
    public void onNewBackPressed() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //当前是全屏
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            //当前是半屏
            super.onNewBackPressed();
        }
    }
}
