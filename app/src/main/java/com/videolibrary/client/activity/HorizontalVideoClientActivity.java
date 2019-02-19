package com.videolibrary.client.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.lidroid.xutils.util.LogUtils;
import com.newyhy.utils.ShareUtils;
import com.newyhy.views.RoundImageView;
import com.quanyan.base.util.DateUtil;
import com.quanyan.base.util.HarwkinLogUtil;
import com.quanyan.base.util.StringUtil;
import com.quanyan.base.view.LoadingDialog;
import com.quanyan.base.view.customview.tabscrollindicator.SlidingTabLayout;
import com.quanyan.yhy.BaseApplication;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.AnalyDataValue;
import com.quanyan.yhy.common.ErrorCode;
import com.quanyan.yhy.common.SysConfigType;
import com.quanyan.yhy.libanalysis.AnEvent;
import com.quanyan.yhy.libanalysis.Analysis;
import com.quanyan.yhy.ui.adapter.DetailViewPagerAdapter;
import com.quanyan.yhy.ui.base.utils.DialogUtil;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.base.utils.PageNameUtils;
import com.quanyan.yhy.ui.base.utils.TCEventHelper;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.login.LoginActivity;
import com.quanyan.yhy.util.MobileUtil;
import com.smart.sdk.api.resp.Api_SNSCENTER_SnsClosedViewTopNDto;
import com.smart.sdk.api.resp.Api_SNSCENTER_SnsClosedViewTopNResult;
import com.smart.sdk.api.resp.Api_SNSCENTER_SnsHasNoEndBatchResult;
import com.smart.sdk.api.resp.Api_SNSCENTER_SnsLiveRecordResult;
import com.tencent.ijk.media.player.IjkMediaPlayer;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.videolibrary.chat.event.LiveChatPersonCountEvent;
import com.videolibrary.chat.event.LiveStatusEvent;
import com.videolibrary.chat.service.LiveChatService;
import com.videolibrary.chat.service.LiveChatServiceConnector;
import com.videolibrary.client.fragment.VideoPlayChatFragment;
import com.videolibrary.client.fragment.VideoPlayHomeFragment;
import com.videolibrary.controller.LiveController;
import com.videolibrary.core.NetBroadCast;
import com.videolibrary.core.VideoTypeInfo;
import com.videolibrary.metadata.LiveTypeConstants;
import com.videolibrary.metadata.NetStateBean;
import com.videolibrary.puhser.view.PopView;
import com.videolibrary.utils.IntentUtil;
import com.videolibrary.utils.NetWorkUtil;
import com.videolibrary.widget.HorizontalVideoRootView;
import com.yhy.common.base.NoLeakHandler;
import com.yhy.common.beans.net.model.master.TalentInfo;
import com.yhy.common.beans.net.model.msg.FollowAnchorResult;
import com.yhy.common.beans.net.model.msg.LiveRoomLivingRecordResult;
import com.yhy.common.constants.IntentConstants;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.AndroidUtils;
import com.yhy.common.utils.CommonUtil;
import com.yhy.common.utils.SPUtils;
import com.yhy.imageloader.ImageLoadManager;
import com.yhy.router.YhyRouter;
import com.yhy.service.IUserService;
import com.yixia.camera.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;

import static android.view.View.VISIBLE;
import static com.videolibrary.controller.LiveController.MSG_GET_HASH_NO_END_LIVE_RESULT_OK;
import static com.videolibrary.controller.LiveController.MSG_GET_LIVE_OVER_RESULT_OK;

/**
 * Created with Android Studio.
 * Title:VideoActivty
 * Description:   观看端
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:赵晓坡
 * Date:16/8/3
 * Time:17:10
 * Version 1.1.0
 */
public class HorizontalVideoClientActivity extends FragmentActivity implements NoLeakHandler.HandlerCallback,
        /*VideoRootView.ImeActionSend,*/ HorizontalVideoRootView.ViewClick, HorizontalVideoRootView.IMediaError, SlidingTabLayout.TabClick {

    private NoLeakHandler mHandler;

    private HorizontalVideoRootView mHorizontalVideoRootView;
    private SlidingTabLayout mSlidingTabLayout;
    private LinearLayout mFollowLayout;
    private ViewPager mViewPager;
    private DetailViewPagerAdapter mAdapter;
    private ArrayList<Fragment> mFragments;
    private ArrayList<String> mPagerTitles;

    private boolean isLive = false;
    private long mLiveId = -1;
    private long mUserId = -1;
    private long mRoomId = -1;
//    private String mRtmpUrl;

    private NetBroadCast mNetBroadCast;

    private Api_SNSCENTER_SnsLiveRecordResult mLiveRecordResult;

    private boolean isFirstGetLiveRecord = true;
    /**
     * 加载状态条
     */
    protected LoadingDialog mLoadingDialog;
    private String mLiveUrl;

    private boolean isNetBroadRegisted = false;

    private Dialog mSoldOutDialog;
    private int mOrientation = LinearLayoutManager.HORIZONTAL;
    /**
     * 是否添加了公告信息
     */
    private boolean isAddConfig = false;

    /**
     * 是否显示直播结束页面
     */
    private boolean shouldShowLiveFinish = false;

    @Autowired
    IUserService userService;

    @Override
    public void onBackPressed() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            try {
                int rotation = Settings.System.getInt(getContentResolver(), Settings.System.ACCELEROMETER_ROTATION);
                LogUtils.d("the system rotation value -->> " + rotation);
                if (rotation == 1) {
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
                        }
                    }, 2000);

                }
            } catch (Settings.SettingNotFoundException e) {
                LogUtils.e("the settings is not supported.");
            }
        } else {
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        YhyRouter.getInstance().inject(this);
        //事件统计
        Analysis.pushEvent(this, AnEvent.PLAY_PAGE);
        // init player
//        IjkMediaPlayer.loadLibrariesOnce(null);
//        IjkMediaPlayer.native_profileBegin("libijkplayer.so");

        setContentView(R.layout.ac_horizontal_video_client_play);
        ((BaseApplication) getApplication()).addActivity(this);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

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
                            if (mHorizontalVideoRootView != null) {
                                mHorizontalVideoRootView.pausePlayCallback();
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

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
//            mRtmpUrl = bundle.getString(IntentUtil.BUNDLE_RTMP_URL);
            mLiveId = bundle.getLong(IntentUtil.BUNDLE_LIVEID, -1);
            mUserId = bundle.getLong(IntentUtil.BUNDLE_ANCHORID, -1);
            isLive = bundle.getBoolean(IntentUtil.BUNDLE_IS_LIVE, false);
            mRoomId = bundle.getLong(IntentUtil.BUNDLE_ROOM_ID, -1);
        }

        mHandler = new NoLeakHandler(this);
        mFragments = new ArrayList<>();
        mPagerTitles = new ArrayList<>();

        mHorizontalVideoRootView = (HorizontalVideoRootView) findViewById(R.id.ac_video_client_video_view);
        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.ac_video_client_sliding_tab);
        mFollowLayout = (LinearLayout) findViewById(R.id.ac_video_client_follow_layout);
        mViewPager = (ViewPager) findViewById(R.id.ac_video_client_viewpager);

        showLoadingView(getString(R.string.loading));
        if (mRoomId == -1) {
            //事件统计
            Analysis.pushEvent(this, AnEvent.PLAY_PAGE_PLAYBACK);
            initialView();
            fetchData();
        } else {
            //直播需要根据roomId查询当前直播liveId
            fetchLiveRoomLivingInfo();
        }
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (mHorizontalVideoRootView != null) {
                mHorizontalVideoRootView.pausePlayCallback();
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case IntentConstants.LOGIN_RESULT:
                if (resultCode == -1) {
                    VideoPlayHomeFragment videoPlayHomeFragment = null;
                    if (isLive) {
                        videoPlayHomeFragment = (VideoPlayHomeFragment) mFragments.get(1);
                    } else {
                        videoPlayHomeFragment = (VideoPlayHomeFragment) mFragments.get(0);
                    }
                    if (videoPlayHomeFragment != null) {
                        videoPlayHomeFragment.fetchMasterData();
                        if (mConnector != null) {
                            mConnector.disconnect(this);
                            mConnector.connect(this);
                        }
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

    public void showBarrageMessage(String msg, boolean isSelf) {
        mHorizontalVideoRootView.show(msg, isSelf);
    }

    private boolean isPaused = false;

    @Override
    protected void onResume() {
        super.onResume();
        HarwkinLogUtil.info(getClass().getSimpleName(), "Activity resume time : " + DateUtil.convert2String(System.currentTimeMillis(), "HH:mm:ss.SSS"));
        if (mHorizontalVideoRootView != null) {
            mHorizontalVideoRootView.showDelayTitle();
        }
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
        if (mHorizontalVideoRootView != null) {
            if (isPaused) {
                isPaused = false;
                mHorizontalVideoRootView.resumePlayCallback();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mHorizontalVideoRootView != null) {
            isPaused = true;
            mHorizontalVideoRootView.pausePlayCallback();
        }
    }

    @Override
    protected void onDestroy() {
        if (mHandler != null) {
            mHandler.setValid(false);
            mHandler.removeCallbacksAndMessages(null);
        }
        if (mHorizontalVideoRootView != null) {
            mHorizontalVideoRootView.stopPlayback();
        }
        IjkMediaPlayer.native_profileEnd();
        if (isNetBroadRegisted) {
            unregisterReceiver(mNetBroadCast);
        }
        unregisterReceiver(mReceiver);
        ((BaseApplication) getApplication()).removeActivity(this);
        if (EventBus.getDefault().isRegistered(HorizontalVideoClientActivity.this)) {
            EventBus.getDefault().unregister(this);
        }
        if (isConnector) mConnector.disconnect(this);
        super.onDestroy();
    }

    private void fetchLiveRoomLivingInfo() {
        LiveController.getInstance().doGetLiveRoomLivingRecord(this, mHandler, mRoomId);
    }

    private void fetchData() {
        LiveController.getInstance().getLiveRecord(this, mHandler, mLiveId);
    }

    private void initialView() {
        if (isLive) {
            mFragments.add(VideoPlayChatFragment.getInstance(mLiveId));
            mHorizontalVideoRootView.setIMediaError(this);
        }
        mFragments.add(VideoPlayHomeFragment.getInstance(mUserId));
        if (isLive) {
            String[] strings = getResources().getStringArray(R.array.video_client_tabs);
            mPagerTitles.addAll(StringUtil.stringsToList(strings));
        } else {
            String[] strings = getResources().getStringArray(R.array.video_client_tabs1);
            mPagerTitles.addAll(StringUtil.stringsToList(strings));
        }
        mAdapter = new DetailViewPagerAdapter(getSupportFragmentManager(), mFragments, mPagerTitles);
        mViewPager.setAdapter(mAdapter);

        //tab属性设置
        if (!isLive) {
            findViewById(R.id.ac_video_client_play_layout).setVisibility(VISIBLE);
            mSlidingTabLayout.setSelectedIndicatorColors(Color.parseColor("#f0f0f0"));
//            mSlidingTabLayout.setSelectedIndicatorColors(Color.WHITE);
            mSlidingTabLayout.setTextColor(getResources().getColorStateList(R.color.tv_live_slide_tab_one));
        } else {
            mSlidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.ac_title_bg_color));
        }
        mSlidingTabLayout.setDistributeEvenly(true);
        mSlidingTabLayout.setTabViewTextSizeSp(15);
        mSlidingTabLayout.setViewPager(mViewPager);
        mSlidingTabLayout.setOnTabClickListener(this);


        mFollowLayout.setOnClickListener(mOnClickListener);
        mHorizontalVideoRootView.setViewClick(this);
        if (isLive && mFragments.get(0) instanceof HorizontalVideoRootView.ImeActionSend) {
            mHorizontalVideoRootView.setImeActionSend((HorizontalVideoRootView.ImeActionSend) mFragments.get(0));
        }
        mHorizontalVideoRootView.setMediaPath(new VideoTypeInfo(isLive ? VideoTypeInfo.VideoType.RTMP : VideoTypeInfo.VideoType.MP4,
                "", true), mOrientation);

        judgeIsSelf();

        mNetBroadCast = new NetBroadCast();
        //registNet();
        EventBus.getDefault().register(this);

        if (mChatService == null) {
            mConnector.connect(this);
        }
    }

    /**
     * 是否是自己
     */
    private void judgeIsSelf() {
        if (userService.isLogin() && userService.isLoginUser(mUserId)) {
            //主播自己不能关注自己
            ((TextView) mFollowLayout.getChildAt(0)).setTextColor(Color.WHITE);
            mFollowLayout.setBackgroundColor(getResources().getColor(R.color.neu_999999));
            mFollowLayout.invalidate();
            mFollowLayout.setEnabled(false);
        }
    }

    boolean isPlaying = false;

    /**
     * 网络状态变化
     *
     * @param netStateBean
     */
    public void onEvent(NetStateBean netStateBean) {
        switch (netStateBean.getNetState()) {
            case NetWorkUtil.NETWORK_NONE:
                //ToastUtil.showToast(this, "当前没有网络, 请连接后再试");
                if (mHorizontalVideoRootView != null) {
                    mHorizontalVideoRootView.resetPlay();
                    mHorizontalVideoRootView.setNetworkDisconnect(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (NetWorkUtil.isNetworkAvailable(getApplicationContext())) {
                                mHorizontalVideoRootView.removeNetUnAvailableView();
                                mHorizontalVideoRootView.startPlay(mLiveUrl);
                            }
                        }
                    });
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
                if (isLive) {
                    showNetDialog(getString(R.string.net_look_info_live));
                } else {
                    showNetDialog(getString(R.string.net_look_info_play));
                }
                break;
            case NetWorkUtil.NETWORK_WIFI:
                //ToastUtil.showToast(this, "已连接至WIFI");
                if (!isPlaying) {
                    isPlaying = true;
                    if (!StringUtil.isEmpty(mLiveUrl) && mHorizontalVideoRootView != null) {
                        mHorizontalVideoRootView.startPlay(mLiveUrl);
                    }
                }
                break;
        }
    }

    Dialog liveNetDialog;

    private void showNetDialog(String notic) {
        liveNetDialog = DialogUtil.showMessageDialog(HorizontalVideoClientActivity.this, null, notic, "继续观看", "退出观看", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!StringUtil.isEmpty(mLiveUrl) && mHorizontalVideoRootView != null) {
                    mHorizontalVideoRootView.startPlay(mLiveUrl);
                }
                liveNetDialog.dismiss();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                liveNetDialog.dismiss();
            }
        });
        liveNetDialog.show();
    }

    LiveChatService mChatService;
    private boolean isConnector = false;
    LiveChatServiceConnector mConnector = new LiveChatServiceConnector() {
        @Override
        public void onIMServiceConnected(LiveChatService chatService) {
            isConnector = true;
            if (chatService == null || mLiveId == 0) {
                return;
            }
            mChatService = chatService;
            mChatService.liveChatLoginManager.login(mLiveId);
        }

        @Override
        public void onServiceDisconnected() {
        }
    };

    public LiveChatService getChatService() {
        return mChatService;
    }

    public void onEventMainThread(LiveStatusEvent event) {
        switch (event) {
            case LIVE_FINISH:
                break;
            case LIVE_INVALID:
                break;
            case LIVE_MASTER_FINISH:
                break;
            case DELETE_LIVE:
                if (mHorizontalVideoRootView != null) {
                    mHorizontalVideoRootView.stopPlayback();
                    mHorizontalVideoRootView.setVideoDelete();
                }
                break;
            case LIVE_OFF_SHELVE:
                if (mHorizontalVideoRootView != null) {
                    mHorizontalVideoRootView.stopPlayback();
                    mHorizontalVideoRootView.setVideoDelete();
                }
                showSoldOutDialog();
                break;
        }
    }

    private void getRecentLive() {
        LiveController.getInstance().getLiveOverResult(this, mHandler, mRoomId);
    }

    public void onEventMainThread(LiveChatPersonCountEvent event) {
        switch (event.event) {
            case LOGIN:
            case LOGOUT:
                if (event.notifyInOut == null) return;
                if (mHorizontalVideoRootView != null) {
                    mHorizontalVideoRootView.setClientOnLineNumTv(getString(R.string.live_online_person_count, event.notifyInOut.getCurrentUsersCount()));
                }
                break;
        }
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ac_video_client_follow_layout:
                    // TODO: 8/29/16 关注
                    TCEventHelper.onEvent(v.getContext(), AnalyDataValue.LIVE_DETAIL_ATTENTION_CLICK);
                    if (!userService.isLogin()) {
                        NavUtils.gotoLoginActivity(v.getContext());
                        return;
                    }
//                    ToastUtil.showToast(v.getContext(), "关注");
                    LiveController.getInstance().followAnchor(v.getContext(), mHandler, mLiveId, mUserId);
                    break;
            }
        }
    };

    public void setFollowState(TalentInfo talentInfo) {

        if (!userService.isLogin()) {
            return;
        }

        if (talentInfo != null) {
            if ("NONE".equals(talentInfo.attentionType)) {
                mFollowLayout.setBackgroundColor(getResources().getColor(R.color.ac_title_bg_color));
                mFollowLayout.setEnabled(true);
                mHorizontalVideoRootView.setFolowState(false);
            } else {
                mHorizontalVideoRootView.setFolowState(true);
                ((TextView) mFollowLayout.getChildAt(0)).setText(getString(R.string.label_follow_over));
                ((TextView) mFollowLayout.getChildAt(0)).setTextColor(Color.WHITE);
                mFollowLayout.setBackgroundColor(getResources().getColor(R.color.neu_999999));
                mFollowLayout.setEnabled(false);
                mFollowLayout.invalidate();

            }
        }
    }

    /**
     * 视频下架的提示
     */
    private void showSoldOutDialog() {
        if (mSoldOutDialog == null) {
            mSoldOutDialog = DialogUtil.showMessageDialog(this, null, getString(R.string.label_video_sold_out),
                    getString(R.string.label_btn_ok), null, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mSoldOutDialog.dismiss();
                        }
                    }, null);
        }
        mSoldOutDialog.show();
    }

    public static final int FINISH = 0x33;
    private static final int MSG_DO_GET_HAS_NO_END_BATCH = 0x11119;
    private final int MSG_WHAT_DELAY_TASK = 0x11120;
    private long batchID;


    @Override
    public void handleMessage(Message msg) {
        hideLoadingView();
        switch (msg.what) {
            case MSG_WHAT_DELAY_TASK:
                LiveController.getInstance().getLiveRecord(this,mHandler,mLiveId);
                break;
            case MSG_GET_HASH_NO_END_LIVE_RESULT_OK:
                Api_SNSCENTER_SnsHasNoEndBatchResult noEndBatchResult = (Api_SNSCENTER_SnsHasNoEndBatchResult) msg.obj;
                if (noEndBatchResult != null){
                    mLiveId = noEndBatchResult.liveId;
                    if (noEndBatchResult.hasNoEndBatch){
                        if (!TextUtils.isEmpty(noEndBatchResult.pullStreamUrlRtmp)){
                            Log.e("EZEZ","拿到结果"+"url = "+noEndBatchResult.pullStreamUrlRtmp);
                            mHandler.removeMessages(MSG_DO_GET_HAS_NO_END_BATCH);
                            mHorizontalVideoRootView.resetPlay();
                            mHorizontalVideoRootView.startPlay(noEndBatchResult.pullStreamUrlRtmp);
                            mHandler.sendEmptyMessageDelayed(MSG_WHAT_DELAY_TASK,5 * 1000);
                        }else {
                            Log.e("EZEZ","没有拿到结果"+"url ，继续10秒后重连");
                            mHandler.removeMessages(MSG_DO_GET_HAS_NO_END_BATCH);
                            mHandler.sendEmptyMessageDelayed(MSG_DO_GET_HAS_NO_END_BATCH,10 * 1000);
                        }
                    }else {
                        mHorizontalVideoRootView.stopPlayback();
                        mHorizontalVideoRootView.setVideoLiveOver();
                        getRecentLive();
                    }
                }else {
                    Log.e("EZEZ","没有拿到结果"+"url ，继续10秒后重连");
                    mHandler.removeMessages(MSG_DO_GET_HAS_NO_END_BATCH);
                    mHandler.sendEmptyMessageDelayed(MSG_DO_GET_HAS_NO_END_BATCH,10 * 1000);
                }
                break;
            case MSG_DO_GET_HAS_NO_END_BATCH:
                Log.e("EZEZ","开始请求接口，是否有未完成的batch");
                LiveController.getInstance().getHasNoEndBatch(this,mHandler,mUserId);
                break;

            case FINISH:
                finish();
                break;
            case ValueConstants.MSG_GET_MASTER_DETAIL_KO:
                //fail to fetch the master detail info
                break;
            case MSG_GET_LIVE_OVER_RESULT_OK:
                Api_SNSCENTER_SnsClosedViewTopNResult result = (Api_SNSCENTER_SnsClosedViewTopNResult) msg.obj;
                showLiveOver(result);
                break;
            case LiveController.MSG_LIVE_FOLLOW_OK:
                // TODO: 8/30/16 关注成功
                FollowAnchorResult followAnchorResult = (FollowAnchorResult) msg.obj;
                if (followAnchorResult != null) {
                    if (LiveTypeConstants.FOLLOW_STATE_SUCCESS.equals(followAnchorResult.followStatus)) {
                        mHorizontalVideoRootView.setFolowState(true);
                        ((TextView) mFollowLayout.getChildAt(0)).setText(getString(R.string.label_follow_over));
                        ((TextView) mFollowLayout.getChildAt(0)).setTextColor(Color.WHITE);
                        mFollowLayout.setBackgroundColor(getResources().getColor(R.color.neu_999999));
                        mFollowLayout.setEnabled(false);
                        mFollowLayout.invalidate();
                    } else {
                        ToastUtil.showToast(this, "关注失败");
                    }
                }
                break;
            case LiveController.MSG_LIVE_FOLLOW_ERROR:
                // TODO: 8/30/16 关注失败
                ToastUtil.showToast(this, StringUtil.handlerErrorCode(getApplicationContext(), msg.arg1));
                break;
            case LiveController.MSG_LIVE_ROOM_LIVEING_RECORD_OK:
                mHorizontalVideoRootView.setClientLiveVisible();
                LiveRoomLivingRecordResult liveRoomLivingRecordResult = (LiveRoomLivingRecordResult) msg.obj;
                if (liveRoomLivingRecordResult != null) {
                    mLiveId = liveRoomLivingRecordResult.liveId;
                    if (mLiveId <= 0) {
                        if (mHorizontalVideoRootView != null) {
                            mHorizontalVideoRootView.setVideoLiveOver();
                            getRecentLive();
                        }
                        ToastUtil.showToast(this, "直播已结束");
                        mHandler.sendEmptyMessageDelayed(FINISH, 2000);
//                        finish();
                        return;
                    }
                    initialView();
                    fetchData();
                } else {
                    ToastUtil.showToast(this, "直播已结束");
//                    finish();
                    mHandler.sendEmptyMessageDelayed(FINISH, 2000);
                    if (mHorizontalVideoRootView != null) {
                        mHorizontalVideoRootView.setVideoLiveOver();
                        getRecentLive();
                    }
                }
                break;
            case LiveController.MSG_LIVE_ROOM_LIVEING_RECORD_ERROR:
                ToastUtil.showToast(this, "服务器异常");
//                finish();
                break;
            case LiveController.MSG_GET_LIVE_RECORD_OK:
                mLiveRecordResult = (Api_SNSCENTER_SnsLiveRecordResult) msg.obj;
                if (mLiveRecordResult == null){
                    mHandler.removeMessages(MSG_WHAT_DELAY_TASK);
                    mHandler.sendEmptyMessageDelayed(MSG_WHAT_DELAY_TASK,5 * 1000);
                    return;
                }
                mRoomId = mLiveRecordResult.roomId;
                if (isLive){
                    if ("END".equals(mLiveRecordResult.batchEnd)){
                        getRecentLive();
                        return;
                    }
                }
                if (!isFirstGetLiveRecord){
                    Log.e("EZEZ","live status = "+ mLiveRecordResult.liveStatus);
                    if (!LiveTypeConstants.LIVE_ING.equals(mLiveRecordResult.liveStatus)){
                        LiveController.getInstance().getHasNoEndBatch(this,mHandler,mUserId);
                    }else {
                        mHandler.removeMessages(MSG_WHAT_DELAY_TASK);
                        mHandler.sendEmptyMessageDelayed(MSG_WHAT_DELAY_TASK,5 * 1000);
                    }
                }

                if (isFirstGetLiveRecord) {
                    if (mLiveRecordResult.userInfo != null) {
                        mUserId = mLiveRecordResult.userInfo.userId;
                    }
                    if (isLive) {
                        ((VideoPlayHomeFragment) mFragments.get(1)).setLiveRoomTitle(mLiveRecordResult.liveTitle);
                    } else {
                        ((VideoPlayHomeFragment) mFragments.get(0)).setLiveRoomTitle(mLiveRecordResult.liveTitle);
                    }
                    if (mUserId <= 0 && mLiveRecordResult != null && mLiveRecordResult.userInfo != null) {
                        mUserId = mLiveRecordResult.userInfo.userId;
                    }
                    judgeIsSelf();
                    if (isLive) {
                        ((VideoPlayHomeFragment) mFragments.get(1)).resetUserId(mUserId);
                    } else {
                        ((VideoPlayHomeFragment) mFragments.get(0)).resetUserId(mUserId);
                    }
                    if (isLive) {
                        mLiveId = mLiveRecordResult.liveId;
                        if (!isAddConfig && mLiveRecordResult.liveConfig != null && mLiveRecordResult.liveConfig.length() > 0) {
                            ((VideoPlayChatFragment) mFragments.get(0)).mLiveChatListView.addImConfig(mLiveRecordResult.liveConfig);
                            isAddConfig = true;
                        }
                        mHorizontalVideoRootView.setClientLiveVisible();
                        mHorizontalVideoRootView.setClientOnLineNumTv("在线:" + mLiveRecordResult.onlineCount);
                        mHorizontalVideoRootView.setClientRoomNum("房号:" + mLiveRecordResult.roomId);
                        mHorizontalVideoRootView.setLandscapeTitleTv(mLiveRecordResult.liveTitle);
                        if (LiveTypeConstants.LIVE_ING.equals(mLiveRecordResult.liveStatus)) {
                            if (!TextUtils.isEmpty(mLiveRecordResult.pullStreamUrlRtmp)) {
                                mLiveUrl = "";
//                                if (mLiveRecordResult.pullStreamUrl.startsWith("rtmp://")) {
//                                    mLiveUrl = mLiveRecordResult.pullStreamUrl;
//                                } else {
//                                    mLiveUrl = "rtmp://".concat(mLiveRecordResult.pullStreamUrl);
//                                }
                                //修改URL by shenwenjie
                                mLiveUrl = mLiveRecordResult.pullStreamUrlRtmp;
                                registNet();
                                isFirstGetLiveRecord = false;
                                mHandler.sendEmptyMessageDelayed(MSG_WHAT_DELAY_TASK,5 * 1000);
                            }
                        } else {
                            // 直播已经结束
                            isFirstGetLiveRecord = false;
                            LiveController.getInstance().getHasNoEndBatch(this,mHandler,mUserId);
                        }
                    } else {
                        mHorizontalVideoRootView.setClientPlayVisible();
                        mHorizontalVideoRootView.setLandscapeTitleTv(mLiveRecordResult.liveTitle);

                        mHorizontalVideoRootView.calculateReplayBackLabelTextPosition();
                        if (LiveTypeConstants.DELETE_LIVE.equals(mLiveRecordResult.status)) {
                            mHorizontalVideoRootView.stopPlayback();
                            mHorizontalVideoRootView.setVideoDelete();
//                            ToastUtil.showToast(this, getString(R.string.label_video_delete_by_anchor));
                        } else if (LiveTypeConstants.OFF_SHELVE_LIVE.equals(mLiveRecordResult.status)) {
                            mHorizontalVideoRootView.stopPlayback();
                            mHorizontalVideoRootView.setVideoDelete();
                            showSoldOutDialog();
                        } else {
//                            mVideoRootView.setClientPlayVisible();
                            if (mLiveRecordResult.replayUrls != null && mLiveRecordResult.replayUrls.size() > 0) {
                                mLiveUrl = "";
                                if (mLiveRecordResult.replayUrls.get(0).startsWith("http://")) {
                                    mLiveUrl = mLiveRecordResult.replayUrls.get(0);
                                } else {
                                    mLiveUrl = "http://".concat(mLiveRecordResult.replayUrls.get(0));
                                }
                                registNet();
                            } else {
                                if (mHorizontalVideoRootView != null) {
                                    mHorizontalVideoRootView.setVideoLiveOver();
                                    getRecentLive();
                                }
                            }
                        }
                    }
                }
                batchID = mLiveRecordResult.batchId;
                break;
            case LiveController.MSG_GET_LIVE_RECORD_ERROR:
                if (ErrorCode.NETWORK_UNAVAILABLE == msg.arg1) {
                    mHorizontalVideoRootView.setNetworkDisconnect(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mHorizontalVideoRootView.removeNetUnAvailableView();
                        }
                    });
                }
                break;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (shouldShowLiveFinish && getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            findViewById(R.id.fl_horizaontal_live_finish).setVisibility(View.VISIBLE);
        }
    }

    private void showLiveOver(Api_SNSCENTER_SnsClosedViewTopNResult result) {
        if (result == null) return;
        shouldShowLiveFinish = true;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //当前是全屏
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            findViewById(R.id.fl_horizaontal_live_finish).setVisibility(View.VISIBLE);
        }
        final Api_SNSCENTER_SnsClosedViewTopNDto data = result.list.get(0);
        ImageButton iv_finish = findViewById(R.id.ib_finish);
        iv_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (data.liveStatus != null && data.liveStatus.equals(LiveTypeConstants.LIVE_ING)) {
            TextView live_first_item_tvVideoType = findViewById(R.id.live_small_item_tvVideoType);
            live_first_item_tvVideoType.setText("直播");
            live_first_item_tvVideoType.setBackgroundResource(R.drawable.commant_tag_video_red);
        } else if (data.liveStatus != null && data.liveStatus.equals(LiveTypeConstants.LIVE_REPLAY)) {
            TextView live_first_item_tvVideoType = findViewById(R.id.live_small_item_tvVideoType);
            live_first_item_tvVideoType.setText("视频");
            live_first_item_tvVideoType.setBackgroundResource(R.drawable.commant_tag_video_blue);
        }

        RoundImageView live_small_item_ivCover = findViewById(R.id.live_small_item_ivCover);
        live_small_item_ivCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int screenType;
                if (data.liveScreenType.equals("VERTICAL")) {
                    screenType = 1;
                } else {
                    screenType = 0;
                }
                if (LiveTypeConstants.LIVE_ING.equals(data.liveStatus)) {
                    IntentUtil.startVideoClientActivity(data.liveId,
                            userService.getLoginUserId(), true, screenType);
                } else if (LiveTypeConstants.LIVE_REPLAY.equals(data.liveStatus)) {
                    IntentUtil.startVideoClientActivity(data.liveId,
                            userService.getLoginUserId(), false, screenType);
                }
            }
        });

        ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(data.liveCover), 0, live_small_item_ivCover);
        //第一个视频的观看人数
        TextView live_small_item_tvAudienceNum = findViewById(R.id.live_small_item_tvAudienceNum);
        live_small_item_tvAudienceNum.setText(String.valueOf(data.viewCount));
        //第一个视频名字
        TextView tv_horizontal_live_name = findViewById(R.id.tv_horizontal_live_name);
        tv_horizontal_live_name.setText(data.liveTitle);
    }

    /**
     * 退出
     *
     * @param v
     */
    @Override
    public void back(View v) {

    }

    private PopView mPopView;

    /**
     * 分享
     *
     * @param v
     */
    @Override
    public void share(View v) {
//        ToastUtil.showToast(this, "分享");

        if (!userService.isLogin()) {
            LoginActivity.gotoLoginActivity(this);
        } else {
//            View contentView = View.inflate(this, R.layout.video_play_share_content, null);
//            contentView.findViewById(R.id.push_pop_share_wechat).setOnClickListener(mShareClick);
//            contentView.findViewById(R.id.push_pop_share_wechat_circle).setOnClickListener(mShareClick);
//            contentView.findViewById(R.id.push_pop_share_qq_layout).setOnClickListener(mShareClick);
//            contentView.findViewById(R.id.push_pop_share_sina_layout).setOnClickListener(mShareClick);
//            contentView.findViewById(R.id.push_pop_share_parent_viewgroup).setOnClickListener(mShareClick);
//            mPopView = new PopView(this, contentView, R.style.videoPlaysharepopanim, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//            mPopView.showPopViewBottom(v);
            String shareTitle = "[直播]" + mHorizontalVideoRootView.getLiveTitle();
            String videoUrl = SPUtils.getShareDefaultUrl(HorizontalVideoClientActivity.this, SysConfigType.URL_LIVE_SHARE_LINK) + batchID;
            String shareContent;
            String shareImg = "";

            /*if (EnvConfig.ENV_TEST.equals(SPUtils.getEnv(getApplicationContext()))) {
                videoUrl = "http://zb.test.yingheying.com/#!/live/" + mLiveId;
            } else if (EnvConfig.ENV_PRE_ONLINE.equals(SPUtils.getEnv(getApplicationContext()))) {
                videoUrl = "http://zb.pre.yingheying.com/#!/live/" + mLiveId;
            } else if (EnvConfig.ENV_ONLINE.equals(SPUtils.getEnv(getApplicationContext()))) {
                videoUrl = "http://zb.yingheying.com/#!/live/" + mLiveId;
            }*/
//            videoUrl = "http://mtest.yingheying.com/wxlive/#!/live/" + mLiveId;
//            videoUrl = ContextHelper.getLiveShareUrl() + mLiveId;
            String userName = "";
            if (mLiveRecordResult != null && mLiveRecordResult.userInfo != null) {
                userName = mLiveRecordResult.userInfo.nickname;
                if (isLive) {
                    shareImg = mLiveRecordResult.userInfo.avatar;
                } else {
                    shareImg = mLiveRecordResult.liveCover;
                }
            }
            if (isLive) {     //  直播的分享内容是   主播头像和SPUtils.getNickName(this) + "  正在鹰和鹰直播，快来围观！"
//                shareContent = "运动何必出门在外（" + userName + "）带你看遍千山万水!";
                shareContent = userName + "  正在鹰和鹰直播，快来围观！";
            } else {          //  精彩视频（回放）分享的内容是  封面
                shareContent = "好的东东就要一起分享!我正在欣赏（" + userName + "）的运动视频,快来一起看吧!";
            }
            ShareUtils.showShareBoard(this, shareTitle, shareContent, videoUrl, shareImg);
        }

    }

    /**
     * 喜欢
     *
     * @param v
     */
    @Override
    public void follow(View v) {
        //事件统计
        Analysis.pushEvent(this, AnEvent.PLAY_PAGE_ATTENTION);
        if (!userService.isLogin()) {
            NavUtils.gotoLoginActivity(v.getContext());
            return;
        }
        if (!mFollowLayout.isEnabled()) {
            // TODO: 16/9/21 已关注状态
            return;
        }
        if (userService.getLoginUserId() == mUserId) {
            return;
        }
        LiveController.getInstance().followAnchor(this, mHandler, mLiveId, mUserId);
//        ToastUtil.showToast(this, "关注");
    }

    /**
     * 客户端聊天消息的发送
     *
     * @param message 消息内容
     *//*
    @Override
    public void sendMessage(String message) {
        ToastUtil.showToast(this, "发送消息");
    }*/

    /**
     * 显示状态加载条
     *
     * @param msg 提示信息
     */
    public void showLoadingView(String msg) {
        if (mLoadingDialog == null) {
            mLoadingDialog = DialogUtil.showLoadingDialog(this, msg, true);
        }

        mLoadingDialog.setDlgMessage(msg);

        if (!mLoadingDialog.isShowing()) {
            mLoadingDialog.show();
        }
    }

    /**
     * 隐藏状态加载提示
     */
    public void hideLoadingView() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    private View.OnClickListener mShareClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.push_pop_share_parent_viewgroup) {
                if (mPopView != null && mPopView.isShowing()) {
                    mPopView.dismiss();
                }
                return;
            }
            String shareTitle = "[直播]" + mHorizontalVideoRootView.getLiveTitle();
            String shareContent;
            String videoUrl = "";
            /*if (EnvConfig.ENV_TEST.equals(SPUtils.getEnv(getApplicationContext()))) {
                videoUrl = "http://zb.test.yingheying.com/#!/live/" + mLiveId;
            } else if (EnvConfig.ENV_PRE_ONLINE.equals(SPUtils.getEnv(getApplicationContext()))) {
                videoUrl = "http://zb.pre.yingheying.com/#!/live/" + mLiveId;
            } else if (EnvConfig.ENV_ONLINE.equals(SPUtils.getEnv(getApplicationContext()))) {
                videoUrl = "http://zb.yingheying.com/#!/live/" + mLiveId;
            }*/
//            videoUrl = "http://mtest.yingheying.com/wxlive/#!/live/" + mLiveId;
//            videoUrl = ContextHelper.getLiveShareUrl() + mLiveId;
            videoUrl = SPUtils.getShareDefaultUrl(HorizontalVideoClientActivity.this, SysConfigType.URL_LIVE_SHARE_LINK) + mLiveId;
            String userName = "";
            if (mLiveRecordResult != null && mLiveRecordResult.userInfo != null) {
                userName = mLiveRecordResult.userInfo.nickname;
            }
            if (isLive) {
                shareContent = "运动何必出门在外（" + userName + "）带你看遍千山万水!";
            } else {
                shareContent = "好的东东就要一起分享!我正在欣赏（" + userName + "）的运动视频,快来一起看吧!";
            }
            mPopView.dismiss();
            switch (v.getId()) {
                case R.id.push_pop_share_wechat:
                    jumpToShare(shareTitle, shareContent, videoUrl, 0);
                    break;
                case R.id.push_pop_share_wechat_circle:
                    jumpToShare(shareTitle, shareContent, videoUrl, 1);
                    break;
                case R.id.push_pop_share_qq_layout:
                    jumpToShare(shareTitle, shareContent, videoUrl, 2);
                    break;
                case R.id.push_pop_share_sina_layout:
                    jumpToShare(shareTitle, shareContent, videoUrl, 3);
                    break;
            }


        }
    };

    UMShareAPI umShareAPI;

    /**
     * @param position wechat-->>0,  wechatcircle-->>1,  qq-->>2,  sina-->>3
     */
    private void jumpToShare(String shareTitle, String shareContent, String shareWebPage, int position) {
        tcEvent(position);
        if (position == -1) {
            return;
        }
        if (!AndroidUtils.isNetWorkAvalible(this)) {
            ToastUtil.showToast(this, getString(R.string.error_label_popup_wlyc));
            return;
        }
        if (umShareAPI == null) {
            umShareAPI = UMShareAPI.get(this);
        }
        UMImage urlImage = new UMImage(this, R.mipmap.ic_app_launcher);

        // TODO: 2018/3/10
        // 修改了 新的友盟分享
        Bitmap logoBm = BitmapFactory.decodeResource(getResources(), R.mipmap.app_logo_share);
        UMWeb web = new UMWeb(shareWebPage);
        web.setTitle(shareTitle);//标题
        web.setThumb(new UMImage(HorizontalVideoClientActivity.this, logoBm));  //缩略图
        web.setDescription(!StringUtil.isEmpty(shareContent) ? shareContent : "来云南,选");//描述

        if (position == 0) {
            if (!MobileUtil.isWeixinAvilible(this)) {
                ToastUtil.showToast(this, getString(R.string.wx_isinstall));
                return;
            }
            //微信分享
            new ShareAction(this).setPlatform(SHARE_MEDIA.WEIXIN)
                    .setCallback(umShareListener)
//                    .withTitle(shareTitle)
//                    .withText(shareContent)
//                    .withTargetUrl(shareWebPage)
                    .withMedia(web)
                    .share();
        } else if (position == 1) {
            if (!MobileUtil.isWeixinAvilible(this)) {
                ToastUtil.showToast(this, getString(R.string.wx_isinstall));
                return;
            }
            // 设置朋友圈分享的内容
            ShareAction sa = new ShareAction(this);
            sa.setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE);
            if (umShareListener != null) {
                sa.setCallback(umShareListener);
            }
            if (!StringUtil.isEmpty(shareTitle)) {
//                sa.withTitle(shareTitle + "\n" + shareContent);
            }
            if (!StringUtil.isEmpty(shareContent)) {
//                sa.withText(shareContent);
            }
            if (!StringUtil.isEmpty(shareWebPage)) {
//                sa.withTargetUrl(shareWebPage);
            }
            if (urlImage != null) {
//                sa.withMedia(urlImage);
            }
            sa.withMedia(web);
            sa.share();
        } else if (position == 2) {
            if (!umShareAPI.isInstall(this, SHARE_MEDIA.QQ)) {
                ToastUtil.showToast(this, getString(R.string.qq_isinstall));
                return;
            }
            //QQ分享
            new ShareAction(this).setPlatform(SHARE_MEDIA.QQ)
                    .setCallback(umShareListener)
//                    .withTitle(shareTitle)
//                    .withText(shareContent)
//                    .withTargetUrl(shareWebPage)
                    .withMedia(web)
                    .share();
        } else if (position == 3) {
            //新浪微博
            ShareAction shareAction = new ShareAction(HorizontalVideoClientActivity.this);
            shareAction
                    .setCallback(umShareListener)
                    .setPlatform(SHARE_MEDIA.SINA)
                    .withMedia(new UMImage(HorizontalVideoClientActivity.this, logoBm))
                    .withText(shareTitle + shareContent + shareWebPage);
            shareAction.share();
//            ShareAction sa = new ShareAction(this);
//            sa.setPlatform(SHARE_MEDIA.SINA);
//            if (umShareListener != null) {
//                sa.setCallback(umShareListener);
//            }
//            if (!StringUtil.isEmpty(shareTitle)) {
////                sa.withTitle(shareTitle);
//            }
//            if (!StringUtil.isEmpty(shareContent)) {
//                sa.withText(shareContent);
//            } else {
//                sa.withText("来云南,选");
//            }
//            if (!StringUtil.isEmpty(shareWebPage)) {
////                sa.withTargetUrl(shareWebPage);
//            }
//            if (urlImage != null) {
//                sa.withMedia(urlImage);
//            }
//            sa.share();
        }
    }

    private void tcEvent(int position) {
        Map<String, String> map = new HashMap<>();
        map.put(AnalyDataValue.KEY_LIVE_ID, mLiveId + "");
        switch (position) {
            case 0:
                map.put(AnalyDataValue.KEY_TYPE, AnalyDataValue.WEIXIN);
                break;
            case 1:
                map.put(AnalyDataValue.KEY_TYPE, AnalyDataValue.WEIXIN_CICRLE);
                break;
            case 2:
                map.put(AnalyDataValue.KEY_TYPE, AnalyDataValue.QQ);
                break;
            case 3:
                map.put(AnalyDataValue.KEY_TYPE, AnalyDataValue.WEIBO);
                break;
        }
        TCEventHelper.onEvent(HorizontalVideoClientActivity.this, AnalyDataValue.LIVE_DETAIL_SHARE_TYPE_CLICK, map);
    }

    UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {

        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {

        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {

        }
    };

    @Override
    public boolean onError() {
        if (NetWorkUtil.isNetworkAvailable(getApplicationContext())) {
        }
        return true;
    }

    @Override
    public void onTabClick(View view, int position) {
        switch (position) {
            case 0:
                break;
            case 1:
                if (isLive) {
                    TCEventHelper.onEvent(this, AnalyDataValue.LIVE_DETAIL_ANCHOR_TAB);
                }
                break;
        }
    }

}
