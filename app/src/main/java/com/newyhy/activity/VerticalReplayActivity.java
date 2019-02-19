package com.newyhy.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.androidkun.xtablayout.XTabLayout;
import com.newyhy.adapter.live.VideoCommentAndZanAdapter;
import com.newyhy.config.CircleBizType;
import com.newyhy.fragment.live.CommentFragment;
import com.newyhy.fragment.live.ZanListFragment;
import com.newyhy.utils.ShareUtils;
import com.newyhy.utils.live.AlphaAnimationUtils;
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
import com.yhy.network.resp.Response;
import com.yhy.network.resp.snscenter.GetShortVideoDetailResp;
import com.yhy.router.RouterPath;
import com.yhy.service.IUserService;
import com.ymanalyseslibrary.secon.YmAnalyticsEvent;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

import static com.newyhy.views.TXVideoView.Pause;
import static com.newyhy.views.TXVideoView.Playing;
import static com.newyhy.views.TXVideoView.TX_VOD_PLAYER;
import static com.newyhy.views.TXVideoView.VERTICAL_VOD;
import static com.tencent.rtmp.TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION;
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

@Route(path = RouterPath.ACTIVITY_VERTICAL_REPLAY)
public class VerticalReplayActivity extends BaseNewActivity implements View.OnClickListener {

    private RelativeLayout rl_comment;

    private XTabLayout tab_comment_zan;
    private ViewPager vp_comment_zan;
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private String[] tab;
    private CommentFragment commentFragment;
    private ZanListFragment zanListFragment;

    private int mLastDiff;
    private InputMsgDialog inputMsgDialog;
    private ImageView iv_anchor_head, ivZan;
    private TextView tv_anchor_name, tv_follow_num, tv_follow, tv_view_count;
    private TXVideoView videoView;
    private NetBroadCast mNetBroadCast;
    boolean isPlaying = false;
    private boolean isNetBroadRegisted = false;
    private UgcInfoResult result;

    private ClubController mClubController;
    private boolean isSupport = false;
    private boolean isOpenInput = false;

    @Autowired(name = "liveId")
    long mLiveId;
    @Autowired(name = "anchorId")
    long mAnchorUserId;
    @Autowired(name = "ugcId")
    long ugcId;
    @Autowired
    IUserService userService;
    private long fansCount = 0;
    private String liveTittle = "";

    private Api_SNSCENTER_SnsLiveRecordResult mLiveRecordResult;

    private String commenOutType;
    private String praiseOutType;

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
        return R.layout.ac_vertical_replay;
    }

    @Override
    protected void initVars() {
        super.initVars();
        mClubController = new ClubController(this, mHandler);

    }

    @Override
    protected void initView() {
        super.initView();
        RelativeLayout et_comment = findViewById(R.id.et_comment);
        findViewById(R.id.iv_show_comment).setOnClickListener(this);
        rl_comment = findViewById(R.id.fl_comment_zan);
        et_comment.setOnClickListener(this);
        tab_comment_zan = findViewById(R.id.tab_comment_zan);
        vp_comment_zan = findViewById(R.id.vp_comment_zan);
        tv_view_count = findViewById(R.id.tv_view_count);

        ivZan = findViewById(R.id.iv_zan);
        iv_anchor_head = findViewById(R.id.iv_anchor_head);
        tv_anchor_name = findViewById(R.id.tv_anchor_name);
        tv_follow_num = findViewById(R.id.tv_follow_count);
        tv_follow = findViewById(R.id.btn_follow);

        videoView = findViewById(R.id.vertical_replay_video_view);
        videoView.setBottomMarginMore();
        videoView.setTXPlayerStatusListener(new TXVideoView.TXPlayerStatusListener() {
            @Override
            public void onStatusChange(int currentState) {

            }

            @Override
            public void showController(boolean show) {
                if (!isOpenInput) {
                    AlphaAnimationUtils.alphaAnimation(findViewById(R.id.vertical_tittle), show ? 0.0f : 1.0f, show ? 1.0f : 0.0f, 300, show);
                    AlphaAnimationUtils.alphaAnimation(findViewById(R.id.bottom_shadow), show ? 0.0f : 1.0f, show ? 1.0f : 0.0f, 300, show);
                    AlphaAnimationUtils.alphaAnimation(findViewById(R.id.rl_say_something), show ? 0.0f : 1.0f, show ? 1.0f : 0.0f, 300, show);
                }
            }

            @Override
            public void fullScreenButtonClick() {

            }

            @Override
            public void onSeekBarTracking(int trackProgress) {
                String eventCode = "";
                if (mLiveId > 0) {
                    if (mLiveRecordResult == null) return;

                    if (mLiveRecordResult.outType == 1) {
                        eventCode = AnEvent.PageVideoBar;
                    } else {
                        eventCode = AnEvent.PageVideoBar;
                    }
                } else {
                    eventCode = AnEvent.PageSvideobar;

                }
                // 埋点
                Analysis.pushEvent(VerticalReplayActivity.this, eventCode,
                        new Analysis.Builder().
                                setLat(LocationManager.getInstance().getStorage().getLast_lat()).
                                setLng(LocationManager.getInstance().getStorage().getLast_lng()).
                                setId(String.valueOf(mLiveId > 0 ? mLiveId : ugcId)).
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
                    Analysis.pushEvent(VerticalReplayActivity.this, eventCode,
                            new Analysis.Builder().
                                    setLat(LocationManager.getInstance().getStorage().getLast_lat()).
                                    setLng(LocationManager.getInstance().getStorage().getLast_lng()).
                                    setId(String.valueOf(mLiveId)).
                                    setPlayPause("播放"));

                } else {
                    // 埋点
                    Analysis.pushEvent(VerticalReplayActivity.this, eventCode,
                            new Analysis.Builder().
                                    setLat(LocationManager.getInstance().getStorage().getLast_lat()).
                                    setLng(LocationManager.getInstance().getStorage().getLast_lng()).
                                    setId(String.valueOf(mLiveId)).
                                    setPlayPause("暂停"));
                }
            }
        });
        if (ugcId > 0) {
            videoView.initPlayer(TX_VOD_PLAYER, VERTICAL_VOD, RENDER_MODE_ADJUST_RESOLUTION);
        } else {
            videoView.initPlayer(TX_VOD_PLAYER, VERTICAL_VOD, RENDER_MODE_FULL_FILL_SCREEN);
        }

    }

    @Override
    protected void initData() {
        super.initData();

        //事件统计
        Analysis.pushEvent(this, AnEvent.PLAY_PAGE_PLAYBACK);

        fetchData();
    }

    private void initFragments() {

        tab = new String[2];
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

    private String mLiveUrl;

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
                //请求主播信息
                fetchMasterData();
                liveTittle = mLiveRecordResult.liveTitle;
                tv_view_count.setText(mLiveRecordResult.viewCount + "人浏览");
                if (mLiveRecordResult.outType == 1) {
                    praiseOutType = TYPE_PRAISE_LIVESUP;
                    commenOutType = TYPE_COMMENT_LIVESUP;
                    ugcId = mLiveRecordResult.ugcId;
                } else {
                    praiseOutType = TYPE_PRAISE_WONDERFULL_VIDEO;
                    commenOutType = TYPE_COMMENT_WONDERFULL_VIDEO;
                    ugcId = mLiveId;
                }

                isSupport = TYPE_AVAILABLE.equals(mLiveRecordResult.isSupport);
                ivZan.setImageResource(isSupport ? R.drawable.ic_zaned : R.drawable.ic_un_zan);
                initFragments();
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
                EventBus.getDefault().post(new EvBusVideoPraiseChange(ugcId,mLiveId,isSupport));
                updataViews(1);
                break;
            case ValueConstants.MSG_PRAISE_ERROR:
                ToastUtil.showToast(this, StringUtil.handlerErrorCode(this, msg.arg1));
                break;
            case MSG_LIVE_DETAIL_OK:
                //通过ugcId获得详情
                result = (UgcInfoResult) msg.obj;
                if (result == null) return;
                if (5 == result.shortVideoType) {
                    mLiveUrl = ContextHelper.getVodUrl() + result.videoUrl;
                } else if (4 == result.shortVideoType) {
                    mLiveUrl = ContextHelper.getImageUrl() + result.videoUrl;
                }
                doAddViewCount(new GetShortVideoDetail.Companion.P(ugcId));
                videoView.setAutoRestart(true);
                registNet();
                if (result.userInfo != null) {
                    mAnchorUserId = result.userInfo.userId;
                    isSupport = TYPE_AVAILABLE.equals(result.isSupport);
                    ivZan.setImageResource(isSupport ? R.drawable.ic_zaned : R.drawable.ic_un_zan);
                }
                tv_view_count.setText(result.viewNum + "人浏览");
                praiseOutType = TYPE_PRAISE_LIVESUP;
                commenOutType = TYPE_COMMENT_LIVESUP;
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
     * 获得主播信息
     */
    public void fetchMasterData() {
        if (mAnchorUserId > 0) {
            getInstance().getMasterDetail(this, mHandler, mAnchorUserId);
        }
    }

    /**
     * 设置主播信息
     *
     * @param talentInfo
     */
    private void setAnchorData(TalentInfo talentInfo) {
        ImageLoadManager.loadCircleImage(CommonUtil.getImageFullUrl(talentInfo.avatar),
                R.mipmap.head_protriat_default, iv_anchor_head);
        tv_anchor_name.setText(talentInfo.nickName);
        tv_follow_num.setText("粉丝 " + talentInfo.fansCount);
        fansCount = talentInfo.fansCount;
        if (mAnchorUserId == userService.getLoginUserId()) {
            tv_follow.setBackground(getResources().getDrawable(R.drawable.bg_followed));
            tv_follow.setEnabled(false);
            return;
        }
        if (!userService.isLogin()) {
            tv_follow.setBackground(getResources().getDrawable(R.drawable.un_follow_bg));
            return;
        }
        if ("NONE".equals(talentInfo.attentionType)) {
            tv_follow.setBackground(getResources().getDrawable(R.drawable.un_follow_bg));
        } else {
            tv_follow.setBackground(getResources().getDrawable(R.drawable.bg_followed));
            tv_follow.setEnabled(false);
            tv_follow.setText("已关注");
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

    private void registNet() {
        isNetBroadRegisted = true;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mNetBroadCast, intentFilter);
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

    private Dialog mSoldOutDialog;

    /**
     * 视频下架的提示
     */
    private void showSoldOutDialog(String notice) {
        if (mSoldOutDialog == null) {
            mSoldOutDialog = DialogUtil.showMessageDialog(this, null, notice,
                    getString(R.string.label_btn_ok), null, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mSoldOutDialog.dismiss();
                            finish();
                        }
                    }, null);
        }
        mSoldOutDialog.show();
    }

    @SuppressLint("WrongViewCast")
    @Override
    protected void setListener() {
        super.setListener();
        ivZan.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                if (mLiveId > 0) {
                    if (mLiveRecordResult == null) return;

                    if (mLiveRecordResult.outType == 1) {
                        // 埋点
                        Analysis.pushEvent(VerticalReplayActivity.this, AnEvent.PageLike,
                                new Analysis.Builder().
                                        setLat(LocationManager.getInstance().getStorage().getLast_lat()).
                                        setLng(LocationManager.getInstance().getStorage().getLast_lng()).
                                        setId(String.valueOf(mLiveId)).
                                        setType("直播").
                                        setList(false));

                    } else {
                        // 埋点
                        Analysis.pushEvent(VerticalReplayActivity.this, AnEvent.PageLike,
                                new Analysis.Builder().
                                        setLat(LocationManager.getInstance().getStorage().getLast_lat()).
                                        setLng(LocationManager.getInstance().getStorage().getLast_lng()).
                                        setId(String.valueOf(mLiveId)).
                                        setType("视频").
                                        setList(false));

                    }

                } else {
                    // 埋点
                    Analysis.pushEvent(VerticalReplayActivity.this, AnEvent.PageLike,
                            new Analysis.Builder().
                                    setLat(LocationManager.getInstance().getStorage().getLast_lat()).
                                    setLng(LocationManager.getInstance().getStorage().getLast_lng()).
                                    setId(String.valueOf(ugcId)).
                                    setType("小视频").
                                    setList(false));
                }
                if (userService.isLogin()) {
                    mClubController.doAddNewPraiseToComment(VerticalReplayActivity.this, ugcId, praiseOutType, isSupport ? ValueConstants.UNPRAISE : ValueConstants.PRAISE);
                } else {
                    NavUtils.gotoLoginActivity(VerticalReplayActivity.this);
                }
            }
        });
        iv_anchor_head.setOnClickListener(this);
        tv_follow.setOnClickListener(this);
        findViewById(R.id.iv_share).setOnClickListener(this);
        findViewById(R.id.et_comment).setOnClickListener(this);
        findViewById(R.id.iv_back).setOnClickListener(this);

        findViewById(R.id.et_comment).getViewTreeObserver().addOnGlobalLayoutListener(() -> {
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

        mNetBroadCast = new NetBroadCast();
        EventBus.getDefault().register(this);

        //来电时停止播放
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
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

        IntentFilter filter = new IntentFilter();
        filter.addAction("com.android.deskclock.ALARM_ALERT");
        filter.addAction("com.android.deskclock.ALARM_DONE");
        registerReceiver(mReceiver, filter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_show_comment:
                isOpenInput = !isOpenInput;
                if (rl_comment.getVisibility() == View.GONE) {
                    rl_comment.setVisibility(View.VISIBLE);
                    findViewById(R.id.rl_say_something).setBackgroundColor(getResources().getColor(R.color.live_background));
                } else {
                    rl_comment.setVisibility(View.GONE);
                    findViewById(R.id.rl_say_something).setBackgroundColor(getResources().getColor(R.color.transparent));
                }
                break;
            case R.id.et_comment:
                if (inputMsgDialog == null) {
                    inputMsgDialog = new InputMsgDialog(this, R.style.Theme_Light_Dialog);
                    inputMsgDialog.setOnShowListener(dialog -> mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            inputMsgDialog.showSoftInput();
                        }
                    }, 100));
                    inputMsgDialog.setSendMsgClickCallBack(text -> {
                        doPostComment(text);
                    });
                    inputMsgDialog.show();

                } else {
                    if (!inputMsgDialog.isShowing()) {
                        inputMsgDialog.show();
                    }
                }
                break;
            case R.id.iv_anchor_head:
                //主播头像点击
                NavUtils.gotoMasterHomepage(this, mAnchorUserId);
                break;
            case R.id.btn_follow:
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

                } else {
                    // 埋点
                    Analysis.pushEvent(this, AnEvent.PageFollew,
                            new Analysis.Builder().
                                    setLat(LocationManager.getInstance().getStorage().getLast_lat()).
                                    setLng(LocationManager.getInstance().getStorage().getLast_lng()).
                                    setId(String.valueOf(ugcId)).
                                    setType("小视频").
                                    setList(false));
                }

                doFollow(this, mAnchorUserId);
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_share:
                share();
                break;
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
                        //竖屏看直播情况下关注成功
                        tv_follow.setBackground(getResources().getDrawable(R.drawable.bg_followed));
                        tv_follow.setEnabled(false);
                        tv_follow_num.setText((fansCount + 1) + "");

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

        } else {
            Analysis.pushEvent(this, AnEvent.PageComment,
                    new Analysis.Builder().
                            setLat(LocationManager.getInstance().getStorage().getLast_lat()).
                            setLng(LocationManager.getInstance().getStorage().getLast_lng()).
                            setId(String.valueOf(ugcId)).
                            setType("小视频"));
        }

        if (userService.isLogin()) {
            if (!TextUtils.isEmpty(content)) {
                boolean flag = content.contentEquals("\r\n");
                if (!flag && TextUtils.isEmpty(content.trim())) {
                    ToastUtil.showToast(VerticalReplayActivity.this, getString(R.string.notice_is_all_space));
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
                mClubController.doPostComment(VerticalReplayActivity.this, commetDetailInfo);
            } else {
                ToastUtil.showToast(VerticalReplayActivity.this, "评论不能为空哦");
            }
        } else {
            NavUtils.gotoLoginActivity(VerticalReplayActivity.this);
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

        } else {
            // 埋点
            Analysis.pushEvent(this, AnEvent.PageShare,
                    new Analysis.Builder().
                            setLat(LocationManager.getInstance().getStorage().getLast_lat()).
                            setLng(LocationManager.getInstance().getStorage().getLast_lng()).
                            setId(String.valueOf(ugcId)).
                            setType("小视频").
                            setList(false));
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
            }else {
                bizType = CircleBizType.SNS_VEDIO_SHORT;
            }
            ShareUtils.ShareExtraInfo shareExtraInfo = new ShareUtils.ShareExtraInfo();
            shareExtraInfo.bizType = bizType;
            shareExtraInfo.bizId = mLiveId > 0 ? mLiveId : ugcId;
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
//        YmAnalyticsEvent.onPause(this);
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

        } else {
            // 埋点
            Analysis.pushEvent(this, AnEvent.PageSvideoClose,
                    new Analysis.Builder().
                            setLat(LocationManager.getInstance().getStorage().getLast_lat()).
                            setLng(LocationManager.getInstance().getStorage().getLast_lng()).
                            setId(String.valueOf(ugcId)).
                            setFinished(videoView.isPlayedFinish()));
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

    public void setCommentNum(int commentNum) {
        tab_comment_zan.getTabAt(0).setText("评论 " + commentNum);
    }

    public void setZanNum(int count) {
        tab_comment_zan.getTabAt(1).setText("点赞 " + count);
    }
}
