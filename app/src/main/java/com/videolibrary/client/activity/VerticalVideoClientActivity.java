package com.videolibrary.client.activity;

import android.app.Activity;
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
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.smart.sdk.api.resp.Api_SNSCENTER_SnsClosedViewTopNResult;
import com.smart.sdk.api.resp.Api_SNSCENTER_SnsHasNoEndBatchResult;
import com.smart.sdk.api.resp.Api_SNSCENTER_SnsLiveRecordResult;
import com.smart.sdk.api.resp.Api_SNSCENTER_UserInfo;
import com.tencent.ijk.media.player.IjkMediaPlayer;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.videolibrary.adapter.ChatListAdapter;
import com.videolibrary.chat.entity.LiveChatNotifyMessage;
import com.videolibrary.chat.entity.LiveChatTextMessage;
import com.videolibrary.chat.event.LiveChatMessageEvent;
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
import com.videolibrary.widget.VerticalVideoRootView;
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
import com.ymanalyseslibrary.secon.YmAnalyticsEvent;

import org.akita.util.AndroidUtil;
import org.akita.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;

import static com.videolibrary.controller.LiveController.MSG_GET_HASH_NO_END_LIVE_RESULT_OK;
import static com.videolibrary.controller.LiveController.MSG_GET_LIVE_OVER_RESULT_OK;

/**
 * Created with Android Studio.
 * Title:VideoActivty
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:赵晓坡
 * Date:16/8/3
 * Time:17:10
 * Version 1.1.0
 */
public class VerticalVideoClientActivity extends FragmentActivity implements NoLeakHandler.HandlerCallback,
        /*VideoRootView.ImeActionSend,*/ VerticalVideoRootView.ViewClick, VerticalVideoRootView.IMediaError, SlidingTabLayout.TabClick {

    private NoLeakHandler mHandler;

    private VerticalVideoRootView mVerticalVideoRootView;
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
     * 关于竖屏直播观看端一些Ui的初始化和设置
     */
    private Button btn_live_chart_open;//打开发送消息界面
    private RelativeLayout ll_chart_box;//发送消息界面
    private EditText et_chat_message;//发送消息EditText
    private RecyclerView rv_chat;
    private ChatListAdapter v_live_client_adapter;
    private ImageView iv_anchor_head;
    private TextView tv_anchor_name;
    private TextView tv_anchor_follows;
    private Button bt_follow_anchor;
    private ImageButton bt_close;
    private ImageButton bt_share;

    private TalentInfo talentInfo;
    private TextView send_message;
    private boolean isFirstGetLiveRecord = true;

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

        setContentView(R.layout.ac_vertical_video_client_play);
        ((BaseApplication) getApplication()).addActivity(this);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

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
                        if (mVerticalVideoRootView != null) {
                            mVerticalVideoRootView.pausePlayCallback();
                        }
                        break;
                }
            }
        }, PhoneStateListener.LISTEN_CALL_STATE);

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

        mVerticalVideoRootView = (VerticalVideoRootView) findViewById(R.id.ac_vertical_video_client_video_view);
        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.ac_video_client_sliding_tab);
        mFollowLayout = (LinearLayout) findViewById(R.id.ac_video_client_follow_layout);
        mViewPager = (ViewPager) findViewById(R.id.ac_video_client_viewpager);

        showLoadingView(getString(R.string.loading));
        if (mRoomId == -1) {
            //回放，不需要传roomId
            //事件统计
            Analysis.pushEvent(this, AnEvent.PLAY_PAGE_PLAYBACK);
            initialView();
            fetchData();
        } else {
            //直播需要根据roomId查询当前直播liveId
            fetchLiveRoomLivingInfo();
        }
        if (isLive) {
            initLiveViewConfig();
        }
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (mVerticalVideoRootView != null) {
                mVerticalVideoRootView.pausePlayCallback();
            }
        }
    };

    public void onEventMainThread(LiveChatMessageEvent event) {
        switch (event.getEvent()) {
            case SEND_SUCESS:
            case REC:
                if (event.object instanceof LiveChatTextMessage) {
                    LiveChatTextMessage message = (LiveChatTextMessage) event.object;
                    v_live_client_adapter.addMessage(message);
                }

                if (event.object instanceof LiveChatNotifyMessage) {
                    LiveChatNotifyMessage message = (LiveChatNotifyMessage) event.object;
                    v_live_client_adapter.addMessage(message);
                }
                rv_chat.scrollToPosition(v_live_client_adapter.getItemCount()-1);
                break;
            case FORBIN_TALK:
                /*showForbinTalkDialog();*/
                break;
        }
    }

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
                        //登录进来重新请求主播状态,重新连接IM 连接
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
        mVerticalVideoRootView.show(msg, isSelf);
    }

    private boolean isPaused = false;

    @Override
    protected void onResume() {
        super.onResume();
        HarwkinLogUtil.info(getClass().getSimpleName(), "Activity resume time : " + DateUtil.convert2String(System.currentTimeMillis(), "HH:mm:ss.SSS"));
        YmAnalyticsEvent.onResume(this);

        if (mVerticalVideoRootView != null) {
            mVerticalVideoRootView.showDelayTitle();
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
        if (mVerticalVideoRootView != null) {
            if (isPaused) {
                isPaused = false;
                mVerticalVideoRootView.resumePlayCallback();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mVerticalVideoRootView != null) {
            isPaused = true;
            mVerticalVideoRootView.pausePlayCallback();
        }
    }

    @Override
    protected void onDestroy() {
        if (mHandler != null) {
            mHandler.setValid(false);
            mHandler.removeCallbacksAndMessages(null);
        }
        if (mVerticalVideoRootView != null) {
            mVerticalVideoRootView.stopPlayback();
        }
        IjkMediaPlayer.native_profileEnd();
        if (isNetBroadRegisted) {
            unregisterReceiver(mNetBroadCast);
        }
        unregisterReceiver(mReceiver);
        ((BaseApplication) getApplication()).removeActivity(this);
        if (EventBus.getDefault().isRegistered(VerticalVideoClientActivity.this)) {
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
            mVerticalVideoRootView.setIMediaError(this);
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
            findViewById(R.id.ac_video_client_play_layout).setVisibility(View.VISIBLE);
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
        mVerticalVideoRootView.setViewClick(this);
        if (isLive && mFragments.get(0) instanceof VerticalVideoRootView.ImeActionSend) {
            mVerticalVideoRootView.setImeActionSend((VerticalVideoRootView.ImeActionSend) mFragments.get(0));
        }
        mVerticalVideoRootView.setMediaPath(new VideoTypeInfo(isLive ? VideoTypeInfo.VideoType.RTMP : VideoTypeInfo.VideoType.MP4,
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
                if (mVerticalVideoRootView != null) {
                    mVerticalVideoRootView.resetPlay();
                    mVerticalVideoRootView.setNetworkDisconnect(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (NetWorkUtil.isNetworkAvailable(getApplicationContext())) {
                                mVerticalVideoRootView.removeNetUnAvailableView();
                                mVerticalVideoRootView.startPlay(mLiveUrl);
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
                    if (!StringUtil.isEmpty(mLiveUrl) && mVerticalVideoRootView != null) {
                        mVerticalVideoRootView.startPlay(mLiveUrl);
                    }
                }
                break;
        }
    }

    Dialog liveNetDialog;

    private void showNetDialog(String notic) {
        liveNetDialog = DialogUtil.showMessageDialog(VerticalVideoClientActivity.this, null, notic, "继续观看", "退出观看", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!StringUtil.isEmpty(mLiveUrl) && mVerticalVideoRootView != null) {
                    mVerticalVideoRootView.startPlay(mLiveUrl);
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
                if (mVerticalVideoRootView != null) {
                    mVerticalVideoRootView.stopPlayback();
                    mVerticalVideoRootView.setVideoDelete();
                }
                break;
            case LIVE_OFF_SHELVE:
                if (mVerticalVideoRootView != null) {
                    mVerticalVideoRootView.stopPlayback();
                    mVerticalVideoRootView.setVideoDelete();
                }
                showSoldOutDialog();
                break;
        }
    }

    /**
     * 直播结束,显示直播结束页面
     */
    private void getRecentLive() {
            LiveController.getInstance().getLiveOverResult(this,mHandler,mRoomId);
    }

    public void onEventMainThread(LiveChatPersonCountEvent event) {
        switch (event.event) {
            case LOGIN:
            case LOGOUT:
                if (event.notifyInOut == null) return;
                if (mVerticalVideoRootView != null) {
                    mVerticalVideoRootView.setClientOnLineNumTv(getString(R.string.live_online_person_count, event.notifyInOut.getCurrentUsersCount()));
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
        this.talentInfo = talentInfo;

        //初始化主播头像和房间数据
        initLiveData();
        setFollowButtonStatus();

        if (!userService.isLogin()){
            return;
        }
        if (talentInfo != null) {
            if ("NONE".equals(talentInfo.attentionType)) {
                mFollowLayout.setBackgroundColor(getResources().getColor(R.color.ac_title_bg_color));
                mFollowLayout.setEnabled(true);
                mVerticalVideoRootView.setFolowState(false);
            } else {
                mVerticalVideoRootView.setFolowState(true);
                ((TextView) mFollowLayout.getChildAt(0)).setText(getString(R.string.label_follow_over));
                ((TextView) mFollowLayout.getChildAt(0)).setTextColor(Color.WHITE);
                mFollowLayout.setBackgroundColor(getResources().getColor(R.color.neu_999999));
                mFollowLayout.setEnabled(false);
                mFollowLayout.invalidate();

            }
        }
    }

    /**
     * 隐藏聊天的listview
     */
    public void hiddenChatListView(){
        if (rv_chat != null){
            rv_chat.setVisibility(View.INVISIBLE);
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
    public static final int FADE_OUT = 0x34;
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
                            Log.e(VerticalVideoClientActivity.class.getSimpleName(),"拿到结果"+"url = "+noEndBatchResult.pullStreamUrlRtmp);
                            mHandler.removeMessages(MSG_DO_GET_HAS_NO_END_BATCH);
                            mVerticalVideoRootView.resetPlay();
                            mVerticalVideoRootView.startPlay(noEndBatchResult.pullStreamUrlRtmp);
                            mHandler.sendEmptyMessageDelayed(MSG_WHAT_DELAY_TASK,5 * 1000);
                        }else {
                            Log.e(VerticalVideoClientActivity.class.getSimpleName(),"没有拿到结果"+"url ，继续10秒后重连");
                            mHandler.removeMessages(MSG_DO_GET_HAS_NO_END_BATCH);
                            mHandler.sendEmptyMessageDelayed(MSG_DO_GET_HAS_NO_END_BATCH,10 * 1000);
                        }
                    }else {
                        getRecentLive();
                    }
                }else {
                    Log.e(VerticalVideoClientActivity.class.getSimpleName(),"没有拿到结果"+"url ，继续10秒后重连");
                    mHandler.removeMessages(MSG_DO_GET_HAS_NO_END_BATCH);
                    mHandler.sendEmptyMessageDelayed(MSG_DO_GET_HAS_NO_END_BATCH,10 * 1000);
                }
                break;
            case MSG_DO_GET_HAS_NO_END_BATCH:
                Log.e(VerticalVideoClientActivity.class.getSimpleName(),"开始请求接口，是否有未完成的batch");
                LiveController.getInstance().getHasNoEndBatch(this,mHandler,mUserId);
                break;
            case FADE_OUT:
                showTopLayout(false);
                break;
            case MSG_GET_LIVE_OVER_RESULT_OK:
                Api_SNSCENTER_SnsClosedViewTopNResult result = (Api_SNSCENTER_SnsClosedViewTopNResult) msg.obj;
                showLiveOver(result);
                break;
            case FINISH:
                finish();
                break;
            case ValueConstants.MSG_GET_MASTER_DETAIL_KO:
                //fail to fetch the master detail info
                break;
            case LiveController.MSG_LIVE_FOLLOW_OK:
                // TODO: 8/30/16 关注成功
                FollowAnchorResult followAnchorResult = (FollowAnchorResult) msg.obj;
                if (followAnchorResult != null) {
                    if (LiveTypeConstants.FOLLOW_STATE_SUCCESS.equals(followAnchorResult.followStatus)) {

                            mVerticalVideoRootView.setFolowState(true);
                            ((TextView) mFollowLayout.getChildAt(0)).setText(getString(R.string.label_follow_over));
                            ((TextView) mFollowLayout.getChildAt(0)).setTextColor(Color.WHITE);
                            mFollowLayout.setBackgroundColor(getResources().getColor(R.color.neu_999999));
                            mFollowLayout.setEnabled(false);
                            mFollowLayout.invalidate();

                            //竖屏看直播情况下关注成功
                            bt_follow_anchor.setBackgroundResource(R.drawable.live_btn_following);
                            bt_follow_anchor.setEnabled(false);
                            tv_anchor_follows.setText((talentInfo.fansCount+1)+"");

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
                if (!isLive) {
                    mVerticalVideoRootView.setClientLiveVisible();
                }
                LiveRoomLivingRecordResult liveRoomLivingRecordResult = (LiveRoomLivingRecordResult) msg.obj;
                if (liveRoomLivingRecordResult != null) {
                    mLiveId = liveRoomLivingRecordResult.liveId;
                    if (mLiveId <= 0) {
                        if (mVerticalVideoRootView != null) {
                            mVerticalVideoRootView.setVideoLiveOver();
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
                    if (mVerticalVideoRootView != null) {
                        mVerticalVideoRootView.setVideoLiveOver();
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
                    Log.e(VerticalVideoClientActivity.class.getSimpleName(),"live status = "+ mLiveRecordResult.liveStatus);
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
                            v_live_client_adapter.addAffiche(mLiveRecordResult.liveConfig);//添加公告(竖屏直播中消息list添加)
                            isAddConfig = true;
                        }
                        mVerticalVideoRootView.setClientLiveVisible();
                        mVerticalVideoRootView.setClientOnLineNumTv("在线:" + mLiveRecordResult.onlineCount);
                        mVerticalVideoRootView.setClientRoomNum("房号:" + mLiveRecordResult.roomId);
                        mVerticalVideoRootView.setLandscapeTitleTv(mLiveRecordResult.liveTitle);
                        findViewById(R.id.vertical_live_client_controller).setVisibility(View.VISIBLE);

                        if (LiveTypeConstants.LIVE_ING.equals(mLiveRecordResult.liveStatus)) {
                            if (!TextUtils.isEmpty(mLiveRecordResult.pullStreamUrlRtmp)) {
                                mLiveUrl = "";
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

                        mVerticalVideoRootView.setClientPlayVisible();
                        mVerticalVideoRootView.setLandscapeTitleTv(mLiveRecordResult.liveTitle);

                        mVerticalVideoRootView.calculateReplayBackLabelTextPosition();
                        if (LiveTypeConstants.DELETE_LIVE.equals(mLiveRecordResult.status)) {
                            mVerticalVideoRootView.stopPlayback();
                            mVerticalVideoRootView.setVideoDelete();
//                            ToastUtil.showToast(this, getString(R.string.label_video_delete_by_anchor));
                        } else if (LiveTypeConstants.OFF_SHELVE_LIVE.equals(mLiveRecordResult.status)) {
                            mVerticalVideoRootView.stopPlayback();
                            mVerticalVideoRootView.setVideoDelete();
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
                                if (mVerticalVideoRootView != null) {
                                    mVerticalVideoRootView.setVideoLiveOver();
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
                    mVerticalVideoRootView.setNetworkDisconnect(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mVerticalVideoRootView.removeNetUnAvailableView();
                            //fetchData();
                        }
                    });
                }
                break;
        }
    }

    /**
     * 显示直播结束的画面
     * @param result
     */
    private void showLiveOver(final Api_SNSCENTER_SnsClosedViewTopNResult result) {
        if (result == null || result.list == null)return;
        findViewById(R.id.fl_live_over).setVisibility(View.VISIBLE);
        if (result.snsUserInfoDTO != null) {
            //主播头像
            ImageView anchor_head = findViewById(R.id.iv_anchor_head);
            ImageLoadManager.loadCircleImage(CommonUtil.getImageFullUrl(result.snsUserInfoDTO.avatar), anchor_head);
            //主播id
            TextView anchor_name = findViewById(R.id.tv_anchor);
            anchor_name.setText(result.snsUserInfoDTO.nickname);
            ImageButton ib_finish = findViewById(R.id.ib_finish);
            ib_finish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }


        for (int i = 0; i < result.list.size(); i++) {
            switch (i){
                case 0:
                    if (result.list.get(i).liveStatus != null && result.list.get(i).liveStatus.equals(LiveTypeConstants.LIVE_ING)) {
                        TextView live_first_item_tvVideoType = findViewById(R.id.live_first_item_tvVideoType);
                        live_first_item_tvVideoType.setText("直播");
                        live_first_item_tvVideoType.setBackgroundResource(R.drawable.commant_tag_video_red);
                    } else if (result.list.get(i).liveStatus != null && result.list.get(i).liveStatus.equals(LiveTypeConstants.LIVE_REPLAY)) {
                        TextView live_first_item_tvVideoType = findViewById(R.id.live_first_item_tvVideoType);
                        live_first_item_tvVideoType.setText("视频");
                        live_first_item_tvVideoType.setBackgroundResource(R.drawable.commant_tag_video_blue);
                    }
                    //第一个视频的封面
                    RoundImageView live_first_item_ivCover =  findViewById(R.id.live_first_item_ivCover);
                    live_first_item_ivCover.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int screenType;
                            if (result.list.get(0).liveScreenType.equals("VERTICAL")){
                                screenType = 1;
                            }else {
                                screenType = 0;
                            }
                            if (LiveTypeConstants.LIVE_ING.equals(result.list.get(0).liveStatus)) {
                                IntentUtil.startVideoClientActivity(result.list.get(0).liveId,
                                        userService.getLoginUserId(), true, screenType);
                            } else if (LiveTypeConstants.LIVE_REPLAY.equals(result.list.get(0).liveStatus)) {
                                IntentUtil.startVideoClientActivity(result.list.get(0).liveId,
                                        userService.getLoginUserId(), false, screenType);
                            }
                        }
                    });
                    ImageLoadManager.loadImage(result.list.get(i).liveCover,0,live_first_item_ivCover);
                    //第一个视频的观看人数
                    TextView live_first_item_tvAudienceNum = findViewById(R.id.live_first_item_tvAudienceNum);
                    live_first_item_tvAudienceNum.setText(String.valueOf(result.list.get(i).viewCount));
                    //第一个视频名字
                    TextView tv_first_video = findViewById(R.id.tv_first_video);
                    tv_first_video.setText(result.list.get(i).liveTitle);
                    break;
                case 1:
                    findViewById(R.id.rl_second_video).setVisibility(View.VISIBLE);
                    if (result.list.get(i).liveStatus != null && result.list.get(i).liveStatus.equals(LiveTypeConstants.LIVE_ING)) {
                        TextView live_second_item_tvVideoType = findViewById(R.id.live_second_item_tvVideoType);
                        live_second_item_tvVideoType.setText("直播");
                        live_second_item_tvVideoType.setBackgroundResource(R.drawable.commant_tag_video_red);
                    } else if (result.list.get(i).liveStatus != null && result.list.get(i).liveStatus.equals(LiveTypeConstants.LIVE_REPLAY)) {
                        TextView live_second_item_tvVideoType = findViewById(R.id.live_second_item_tvVideoType);
                        live_second_item_tvVideoType.setText("视频");
                        live_second_item_tvVideoType.setBackgroundResource(R.drawable.commant_tag_video_blue);
                    }
                    //第二个视频的封面
                    RoundImageView live_second_item_ivCover =  findViewById(R.id.live_second_item_ivCover);
                    live_second_item_ivCover.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int screenType;
                            if (result.list.get(1).liveScreenType.equals("VERTICAL")){
                                screenType = 1;
                            }else {
                                screenType = 0;
                            }
                            if (LiveTypeConstants.LIVE_ING.equals(result.list.get(1).liveStatus)) {
                                IntentUtil.startVideoClientActivity(result.list.get(1).liveId,
                                        userService.getLoginUserId(), true, screenType);
                            } else if (LiveTypeConstants.LIVE_REPLAY.equals(result.list.get(1).liveStatus)) {
                                IntentUtil.startVideoClientActivity(result.list.get(1).liveId,
                                        userService.getLoginUserId(), false, screenType);
                            }
                        }
                    });
                    ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(result.list.get(i).liveCover),0,live_second_item_ivCover);
                    //第二个视频的观看人数
                    TextView live_second_item_tvAudienceNum = findViewById(R.id.live_second_item_tvAudienceNum);
                    live_second_item_tvAudienceNum.setText(String.valueOf(result.list.get(i).viewCount));
                    //第二个视频名字
                    TextView tv_second_live_name = findViewById(R.id.tv_second_live_name);
                    tv_second_live_name.setText(result.list.get(i).liveTitle);
                    break;
                case 2:
                    findViewById(R.id.rl_third_video).setVisibility(View.VISIBLE);
                    if (result.list.get(i).liveStatus != null && result.list.get(i).liveStatus.equals(LiveTypeConstants.LIVE_ING)) {
                        TextView live_third_item_tvVideoType = findViewById(R.id.live_third_item_tvVideoType);
                        live_third_item_tvVideoType.setText("直播");
                        live_third_item_tvVideoType.setBackgroundResource(R.drawable.commant_tag_video_red);
                    } else if (result.list.get(i).liveStatus != null && result.list.get(i).liveStatus.equals(LiveTypeConstants.LIVE_REPLAY)) {
                        TextView live_second_item_tvVideoType = findViewById(R.id.live_second_item_tvVideoType);
                        live_second_item_tvVideoType.setText("视频");
                        live_second_item_tvVideoType.setBackgroundResource(R.drawable.commant_tag_video_blue);
                    }
                    //第三个视频的封面
                    RoundImageView live_third_item_ivCover =  findViewById(R.id.live_third_item_ivCover);
                    live_third_item_ivCover.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int screenType;
                            if (result.list.get(2).liveScreenType.equals("VERTICAL")){
                                screenType = 1;
                            }else {
                                screenType = 0;
                            }
                            if (LiveTypeConstants.LIVE_ING.equals(result.list.get(2).liveStatus)) {
                                IntentUtil.startVideoClientActivity(result.list.get(2).liveId,
                                        userService.getLoginUserId(), true, screenType);
                            } else if (LiveTypeConstants.LIVE_REPLAY.equals(result.list.get(2).liveStatus)) {
                                IntentUtil.startVideoClientActivity(result.list.get(2).liveId,
                                        userService.getLoginUserId(), false, screenType);
                            }
                        }
                    });
                    ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(result.list.get(i).liveCover),0,live_third_item_ivCover);
                    //第三个视频的观看人数
                    TextView live_third_item_tvAudienceNum = findViewById(R.id.live_third_item_tvAudienceNum);
                    live_third_item_tvAudienceNum.setText(String.valueOf(result.list.get(i).viewCount));
                    //第三个视频名字
                    TextView tv_third_live_name = findViewById(R.id.tv_third_live_name);
                    tv_third_live_name.setText(result.list.get(i).liveTitle);
                    break;
            }
        }
    }

    /**
     * 设置竖屏直播情况下的关注按钮状态
     * @param
     */
    public void setFollowButtonStatus() {

        if (!userService.isLogin()){
            bt_follow_anchor.setBackgroundResource(R.drawable.live_btn_follow);
            return;
        }

        if ("NONE".equals(talentInfo.attentionType)) {
            bt_follow_anchor.setBackgroundResource(R.drawable.live_btn_follow);
        } else {
            bt_follow_anchor.setBackgroundResource(R.drawable.live_btn_following);
            bt_follow_anchor.setEnabled(false);
        }

    }

    /**
     * 显示或者隐藏顶部的layout
     * @param show
     */
    public void showTopLayout(boolean show){

        if (show){
            if (isLive){
                btn_live_chart_open.setVisibility(View.VISIBLE);
            }
            findViewById(R.id.rl_top_layout).setVisibility(View.VISIBLE);
        }else {
            if (isLive){
                btn_live_chart_open.setVisibility(View.INVISIBLE);
            }
            findViewById(R.id.rl_top_layout).setVisibility(View.INVISIBLE);
        }

    }

    /**
     * 初始化主播相关的数据
     */
    private void initLiveData() {
        iv_anchor_head = (ImageView) findViewById(R.id.iv_anchor_header);
        tv_anchor_name = (TextView) findViewById(R.id.tv_anchor_name);
        tv_anchor_follows = (TextView) findViewById(R.id.tv_room_number);
        bt_follow_anchor = (Button) findViewById(R.id.btn_follow_anchor);
        bt_close = (ImageButton) findViewById(R.id.ib_close_live);
        bt_share = (ImageButton) findViewById(R.id.ib_share);

        //if (mLiveRecordResult.userInfo.currentUserFollowStatus)
        bt_follow_anchor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userService.isLogin()) {
                    follow(v);
                }else {
                    NavUtils.gotoLoginActivity(VerticalVideoClientActivity.this);
                }
            }
        });

        bt_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //事件统计
                Analysis.pushEvent(VerticalVideoClientActivity.this, AnEvent.PLAY_PAGE_RETURN);
                finish();
            }
        });

        bt_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share(v);
            }
        });

        if (!TextUtils.isEmpty((talentInfo.fansCount+""))){
            tv_anchor_follows.setText(talentInfo.fansCount+"");
        }

        if (mLiveRecordResult == null){
            return;
        }

        Api_SNSCENTER_UserInfo userInfo = mLiveRecordResult.userInfo;

        if (userInfo == null){
            return;
        }

        if (!TextUtils.isEmpty(mLiveRecordResult.userInfo.avatar)){
            ImageLoadManager.loadCircleImage(CommonUtil.getImageFullUrl(mLiveRecordResult.userInfo.avatar),iv_anchor_head);
        }

        if (!TextUtils.isEmpty(mLiveRecordResult.userInfo.nickname)){
            tv_anchor_name.setText(mLiveRecordResult.userInfo.nickname);
        }

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
            String shareTitle = "[直播]" + mVerticalVideoRootView.getLiveTitle();
            String videoUrl = SPUtils.getShareDefaultUrl(this, SysConfigType.URL_LIVE_SHARE_LINK) + mLiveId;
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
        if (v.isEnabled()){
            LiveController.getInstance().followAnchor(this, mHandler, mLiveId, mUserId);
            return;
        }

        if (userService.getLoginUserId() == mUserId) {
            return;
        }

        if (!mFollowLayout.isEnabled()) {
            // TODO: 16/9/21 已关注状态
        }else {
            LiveController.getInstance().followAnchor(this, mHandler, mLiveId, mUserId);
        }

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
            String shareTitle = "[直播]" + mVerticalVideoRootView.getLiveTitle();
            String shareContent;
            String videoUrl = "";
            /*if (EnvConfig.ENV_TEST.equals(SPUtils.getEnv(getApplicationContext()))) {
                videoUrl = "http://zb.test.yingheying.com/#!/live/" + mLiveId;
            } else if (EnvConfig.ENV_PRE_ONLINE.equals(SPUtils.getEnv(getApplicationContext()))) {
                videoUrl = "http://zb.pre.yingheying.com/#!/live/" + mLiveId;
            } else if (EnvConfig.ENV_ONLINE.equals(SPUtils.getEnv(getApplicationContext()))) {
                videoUrl = "http://zb.yingheying.com/#!/live/" + mLiveId;
            }*/
//            if (EnvConfig.ENV_ONLINE.equals(SPUtils.getEnv(VideoClientActivity.this))) {
//                // 正式
//                videoUrl = "http://mtest.yingheying.com/wxlive/#!/live/" + mLiveId;
//            } else {
//                // 测试
//                videoUrl = "http://mtest.yingheying.com/wxlive/#!/live/" + mLiveId;
//
//            }
//            videoUrl = ContextHelper.getLiveShareUrl() + mLiveId;
            videoUrl = SPUtils.getShareDefaultUrl(VerticalVideoClientActivity.this, SysConfigType.URL_LIVE_SHARE_LINK) + mLiveId;

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
        web.setThumb(new UMImage(VerticalVideoClientActivity.this, logoBm));  //缩略图
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
            ShareAction shareAction = new ShareAction(VerticalVideoClientActivity.this);
            shareAction
                    .setCallback(umShareListener)
                    .setPlatform(SHARE_MEDIA.SINA)
                    .withMedia(new UMImage(VerticalVideoClientActivity.this, logoBm))
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
        TCEventHelper.onEvent(VerticalVideoClientActivity.this, AnalyDataValue.LIVE_DETAIL_SHARE_TYPE_CLICK, map);
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
            //fetchData();
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


    /**
     * 竖屏直播观看的部分交互控件
     */
    public void initLiveViewConfig() {

        btn_live_chart_open = (Button) findViewById(R.id.btn_live_chart_open);
        send_message = (TextView) findViewById(R.id.send_message);
        ll_chart_box = (RelativeLayout) findViewById(R.id.ll_chart_box);
        et_chat_message = (EditText) findViewById(R.id.et_chat_message);
        rv_chat = (RecyclerView) findViewById(R.id.rv_chart);
        LinearLayoutManager manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        rv_chat.setLayoutManager(manager);
        v_live_client_adapter = new ChatListAdapter(this,new ArrayList());
        rv_chat.setAdapter(v_live_client_adapter);
        et_chat_message.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                et_chat_message.getWindowVisibleDisplayFrame(rect);
                int rootInvisibleHeight = et_chat_message.getRootView().getHeight() - rect.bottom;
                if (rootInvisibleHeight <= 100) {
                    //软键盘隐藏啦
                    ll_chart_box.setVisibility(View.GONE);
                } else {
                    ////软键盘弹出啦
                    ll_chart_box.setVisibility(View.VISIBLE);
                    et_chat_message.setFocusable(true);
                    et_chat_message.setFocusableInTouchMode(true);
                    et_chat_message.requestFocus();
                }
            }
        });

        btn_live_chart_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //点击左下角...按钮弹出输入框,获取焦点
                ll_chart_box.setVisibility(View.VISIBLE);
                et_chat_message.setFocusable(true);
                et_chat_message.setFocusableInTouchMode(true);
                et_chat_message.requestFocus();
                openKeybord(et_chat_message,VerticalVideoClientActivity.this);
            }
        });

        send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_chat_message != null && et_chat_message.getText().length()>0) {
                    sendMessage(et_chat_message.getText().toString());
                    closeKeybord(et_chat_message,VerticalVideoClientActivity.this);
                }
            }
        });

        //注册软键盘上的发送按钮
        et_chat_message.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    boolean isOK = false;
                    //目前输入框需要支持多行输入，此时enter键的内容不会更改，且按下时actionId为0；
                    // 注意不同的手机可能有兼容性问题，此时只监听enter键的按下
                    //当actionId == XX_SEND
                    //或者event.getKeyCode == ENTER 且 event.getAction == ACTION_DOWN时也触发
                    //注意，这是一定要判断event != null。因为在某些输入法上会返回null。
                    if (actionId == EditorInfo.IME_ACTION_SEND
                            || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
                        //发送
                        if (et_chat_message != null && et_chat_message.getText().length()>0) {
                            sendMessage(et_chat_message.getText().toString());
                            closeKeybord(et_chat_message,VerticalVideoClientActivity.this);
                        }
                        isOK = true;
                    }
                    return isOK;
                }
            });
        }

    //发送消息
    private void sendMessage(String strings) {
        if (!userService.isLogin()) {
            NavUtils.gotoLoginActivity(this);
            return;
        }
        if (TextUtils.isEmpty(strings.trim())) {
            ToastUtil.showToast(this, "内容为空");
        } else {
            boolean isLogin = userService.isLogin();
            long fromId = 0;
            String fromName = null;
            String fromPic = null;
            if (isLogin) {
                fromId = userService.getLoginUserId();
                fromName = SPUtils.getNickName(this);
                fromPic = SPUtils.getUserIcon(this);
            } else {
                if (getChatService() != null) {
                    fromId = getChatService().liveChatLoginManager.getCurrentUserId();
                }
            }
            if (getChatService() != null) {
                LiveChatTextMessage message = new LiveChatTextMessage(fromId, mLiveId, fromName, fromPic, strings);
                getChatService().liveChatMessageManager.sendMessage(message);
            }
            et_chat_message.setText("");
            AndroidUtil.hideIME(this, true);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
       /* View v = getCurrentFocus();
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {

            if (isSoftInputShow(this) && isShouldHideKeyboard(v,ev)){
                closeKeybord(et_chat_message,this);
            }

        }*/
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {

        if (send_message != null) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击发送的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }

        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }

        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return true;
    }


    /**
     * 打开软键盘
     *
     * @param mEditText
     * @param mContext
     */
    public void openKeybord(EditText mEditText, Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN);
        /*imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY);*/
        imm.toggleSoftInput(0,
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 关闭软键盘
     *
     * @param mEditText
     * @param mContext
     */
    public void closeKeybord(EditText mEditText, Context mContext) {
        if (isLive) {
            InputMethodManager imm = (InputMethodManager) mContext
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
            }
        }
    }

    /**
     * 判断当前软键盘是否打开
     *
     * @param activity
     */
    public boolean isSoftInputShow(Activity activity) {

        // 虚拟键盘隐藏 判断view是否为空
        View view = activity.getWindow().peekDecorView();
        if (view != null) {
            // 隐藏虚拟键盘
            InputMethodManager inputmanger = (InputMethodManager) activity
                    .getSystemService(Activity.INPUT_METHOD_SERVICE);
//       inputmanger.hideSoftInputFromWindow(view.getWindowToken(),0);

            return inputmanger.isActive() && activity.getWindow().getCurrentFocus() != null;
        }
        return false;
    }
}
