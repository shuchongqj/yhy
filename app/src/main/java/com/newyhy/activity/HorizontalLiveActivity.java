package com.newyhy.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.androidkun.xtablayout.XTabLayout;
import com.newyhy.adapter.live.LiveChatAndAnchorInfoAdapter;
import com.newyhy.config.CircleBizType;
import com.newyhy.fragment.live.AnchorInfoFragment;
import com.newyhy.fragment.live.LiveChatListFragment;
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
import com.quanyan.yhy.ui.base.utils.DialogUtil;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.base.utils.PageNameUtils;
import com.quanyan.yhy.ui.base.utils.TCEventHelper;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.login.LoginActivity;
import com.smart.sdk.api.resp.Api_SNSCENTER_SnsClosedViewTopNDto;
import com.smart.sdk.api.resp.Api_SNSCENTER_SnsClosedViewTopNResult;
import com.smart.sdk.api.resp.Api_SNSCENTER_SnsHasNoEndBatchResult;
import com.smart.sdk.api.resp.Api_SNSCENTER_SnsLiveRecordResult;
import com.smart.sdk.api.resp.Api_SNSCENTER_SnsReplayListResult;
import com.umeng.analytics.MobclickAgent;
import com.videolibrary.chat.entity.LiveChatNotifyMessage;
import com.videolibrary.chat.entity.LiveChatTextMessage;
import com.videolibrary.chat.event.LiveChatMessageEvent;
import com.videolibrary.chat.event.LiveChatPersonCountEvent;
import com.videolibrary.chat.service.LiveChatService;
import com.videolibrary.chat.service.LiveChatServiceConnector;
import com.videolibrary.client.activity.VerticalVideoClientActivity;
import com.videolibrary.controller.LiveController;
import com.videolibrary.core.NetBroadCast;
import com.videolibrary.metadata.LiveTypeConstants;
import com.videolibrary.metadata.NetStateBean;
import com.videolibrary.utils.IntentUtil;
import com.videolibrary.utils.NetWorkUtil;
import com.videolibrary.widget.BarrageViewParent;
import com.yhy.common.base.BaseNewActivity;
import com.yhy.common.beans.net.model.master.TalentInfo;
import com.yhy.common.beans.net.model.msg.FollowAnchorResult;
import com.yhy.common.utils.CommonUtil;
import com.yhy.common.utils.SPUtils;
import com.yhy.imageloader.ImageLoadManager;
import com.yhy.location.LocationManager;
import com.yhy.router.RouterPath;
import com.yhy.service.IUserService;
import com.ymanalyseslibrary.secon.YmAnalyticsEvent;

import org.akita.util.Log;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

import static com.newyhy.views.TXVideoView.HORIZONTAL_LIVE;
import static com.tencent.rtmp.TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN;
import static com.videolibrary.controller.LiveController.MSG_GET_HASH_NO_END_LIVE_RESULT_OK;
import static com.videolibrary.controller.LiveController.MSG_GET_LIVE_OVER_RESULT_OK;
import static com.videolibrary.controller.LiveController.MSG_GET_LIVE_RECORD_OK;
import static com.videolibrary.controller.LiveController.MSG_LIVE_LIST_OK;
import static com.videolibrary.controller.LiveController.getInstance;
import static com.yhy.common.constants.ValueConstants.MSG_GET_MASTER_DETAIL_OK;
import static com.yhy.common.constants.ValueConstants.TYPE_AVAILABLE;

@Route(path = RouterPath.ACTIVITY_HORIZONTAL_LIVE)
public class HorizontalLiveActivity extends BaseNewActivity implements View.OnClickListener {

    private LiveChatListFragment liveChatListFragment;
    private AnchorInfoFragment anchorInfoFragment;
    private ArrayList<Fragment> fragments = new ArrayList<>();

    private TXVideoView videoView;
    private BarrageViewParent barrage_view;
    private ImageView iv_anchor_head, top_iv_anchor_head, ib_finish, ib_share;
    private TextView tv_follow, tv_anchor_name, tv_publish_time, top_tv_tittle, top_tv_anchor_name,
            top_tv_follow_count, top_btn_follow, tv_online_count;
    private NetBroadCast mNetBroadCast;
    boolean isPlaying = false;

    @Autowired(name = "liveId")
    long mLiveId;
    @Autowired(name = "anchorId")
    long mAnchorUserId;
    @Autowired
    IUserService userService;
    private long mRoomId = -1;
    private String liveTittle = "";
    private String mLiveUrl;
    private boolean isNetBroadRegisted = false;
    private long fansCount = 0;
    private boolean isFullScreen = false;
    private long batchId;

    /**
     * 是否添加了公告信息
     */
    private boolean isAddConfig = false;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (videoView != null) {
                videoView.pausePlay();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //事件统计
        Analysis.pushEvent(this, AnEvent.PLAY_PAGE_PLAYBACK);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.ac_horizontal_live;
    }

    @Override
    protected void initVars() {
        super.initVars();
    }

    @Override
    protected void initView() {
        super.initView();
        XTabLayout tab_chat_anchor = findViewById(R.id.tab_chat_anchor);
        ViewPager vp_chat_anchor = findViewById(R.id.vp_chat_anchor);
        liveChatListFragment = new LiveChatListFragment();
        anchorInfoFragment = new AnchorInfoFragment();
        fragments.add(liveChatListFragment);
        fragments.add(anchorInfoFragment);
        String[] tab = new String[]{"       聊天       ", "       主播       "};
        vp_chat_anchor.setAdapter(new LiveChatAndAnchorInfoAdapter(
                getSupportFragmentManager(), fragments, tab));
        tab_chat_anchor.setupWithViewPager(vp_chat_anchor);

        videoView = findViewById(R.id.horizontal_live_videoview);
        videoView.initPlayer(TXVideoView.TX_LIVE_PLAYER, HORIZONTAL_LIVE, RENDER_MODE_FULL_FILL_SCREEN);
        barrage_view = findViewById(R.id.barrage_view);
        ib_finish = findViewById(R.id.ib_finish);
        ib_share = findViewById(R.id.ib_share);
        tv_follow = findViewById(R.id.tv_follow);
        iv_anchor_head = findViewById(R.id.iv_anchor_head);
        tv_anchor_name = findViewById(R.id.tv_anchor_name);
        tv_publish_time = findViewById(R.id.tv_publish_time);
        top_tv_tittle = findViewById(R.id.top_tv_tittle);
        top_iv_anchor_head = findViewById(R.id.top_iv_anchor_head);
        top_tv_anchor_name = findViewById(R.id.top_tv_anchor_name);
        top_tv_follow_count = findViewById(R.id.top_tv_follow_count);
        top_btn_follow = findViewById(R.id.top_btn_follow);
        tv_online_count = findViewById(R.id.tv_online_count);
    }

    @Override
    protected void setListener() {
        super.setListener();
        ib_share.setOnClickListener(this);
        ib_finish.setOnClickListener(this);
        tv_follow.setOnClickListener(this);
        top_btn_follow.setOnClickListener(this);
        iv_anchor_head.setOnClickListener(this);
        top_iv_anchor_head.setOnClickListener(this);
        findViewById(R.id.bottom_et_comment).setOnClickListener(this);
        videoView.needShowFullScreenButton(true);
        videoView.setTXPlayerStatusListener(new TXVideoView.TXPlayerStatusListener() {
            @Override
            public void onStatusChange(int currentState) {

            }

            @Override
            public void showController(boolean show) {
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    //当前是全屏
                    AlphaAnimationUtils.alphaAnimation(ib_finish, show ? 1.0f : 0.0f, show ? 0.0f : 1.0f, 300, !show);
                    AlphaAnimationUtils.alphaAnimation(ib_share, show ? 1.0f : 0.0f, show ? 0.0f : 1.0f, 300, !show);
                    AlphaAnimationUtils.alphaAnimation(findViewById(R.id.rl_video_tittle), show ? 1.0f : 0.0f, show ? 0.0f : 1.0f, 300, !show);
                    AlphaAnimationUtils.alphaAnimation(findViewById(R.id.view_shadow), show ? 1.0f : 0.0f, show ? 0.0f : 1.0f, 300, !show);
                    AlphaAnimationUtils.alphaAnimation(findViewById(R.id.view_input_shadow), show ? 1.0f : 0.0f, show ? 0.0f : 1.0f, 300, !show);
                    AlphaAnimationUtils.alphaAnimation(findViewById(R.id.rl_say_something), show ? 1.0f : 0.0f, show ? 0.0f : 1.0f, 300, !show);
                } else {
                    //当前是半屏
                    AlphaAnimationUtils.alphaAnimation(ib_finish, show ? 1.0f : 0.0f, show ? 0.0f : 1.0f, 300, !show);
                    AlphaAnimationUtils.alphaAnimation(ib_share, show ? 1.0f : 0.0f, show ? 0.0f : 1.0f, 300, !show);
                    AlphaAnimationUtils.alphaAnimation(findViewById(R.id.rl_online_num), show ? 1.0f : 0.0f, show ? 0.0f : 1.0f, 300, !show);
                    AlphaAnimationUtils.alphaAnimation(findViewById(R.id.view_input_shadow), show ? 1.0f : 0.0f, show ? 0.0f : 1.0f, 300, !show);
                    videoView.doFullScreenButtonAnimation(show);

                }
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

            }

            @Override
            public void onCenterButtonClick(boolean isPlay) {

            }
        });

        //连接上im
        if (mChatService == null) {
            mConnector.connect(this);
        }

        mNetBroadCast = new NetBroadCast();
        EventBus.getDefault().register(this);

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

        fetchData();
    }

    /**
     * 根据liveId查询当前直播状态
     */
    private void fetchData() {
        LiveController.getInstance().getLiveRecord(this, mHandler, mLiveId);
    }

    /**
     * 直播间聊天服务
     */
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

    //发送消息
    public void sendMessage(String strings) {
        // 埋点
        Analysis.pushEvent(this, AnEvent.PageComment,
                new Analysis.Builder().
                        setLat(LocationManager.getInstance().getStorage().getLast_lat()).
                        setLng(LocationManager.getInstance().getStorage().getLast_lng()).
                        setId(String.valueOf(mLiveId)).
                        setType("直播"));

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
            if (mChatService != null) {
                LiveChatTextMessage message = new LiveChatTextMessage(fromId, mLiveId, fromName, fromPic, strings);
                mChatService.liveChatMessageManager.sendMessage(message);
            }
        }
    }

    /**
     * IM消息事件
     *
     * @param event
     */
    public void onEventMainThread(LiveChatMessageEvent event) {
        switch (event.getEvent()) {
            case SEND_SUCESS:
            case REC:
                if (event.object instanceof LiveChatTextMessage) {
                    LiveChatTextMessage message = (LiveChatTextMessage) event.object;
                    liveChatListFragment.addMessage(message);
                    if (isFullScreen && message.getFromId() != userService.getLoginUserId()) {
                        barrage_view.show(message.getMessageContent(), false);
                    }
                }

                if (event.object instanceof LiveChatNotifyMessage) {
                    LiveChatNotifyMessage message = (LiveChatNotifyMessage) event.object;
                    if (isFullScreen && message.getFromId() != userService.getLoginUserId()) {
                        barrage_view.show(message.getMessageContent(), false);
                    }
                    liveChatListFragment.addMessage(message);
                }
                break;
            case FORBIN_TALK:
                break;
        }
    }

    /**
     * 有观众来了
     *
     * @param event
     */
    public void onEventMainThread(LiveChatPersonCountEvent event) {
        switch (event.event) {
            case LOGIN:
            case LOGOUT:
                /*if (event.notifyInOut == null) return;
                tv_online_count.setText(event.notifyInOut.getCurrentUsersCount()+"");*/
                //tv_online_count.setText(event.notifyInOut.getCurrentUsersCount()+"");
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

    private InputMsgDialog inputMsgDialog;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bottom_et_comment:
                if (inputMsgDialog == null) {
                    inputMsgDialog = new InputMsgDialog(this, R.style.Theme_Light_Dialog);
                    inputMsgDialog.setOnShowListener(dialog -> mHandler.postDelayed(() -> inputMsgDialog.showSoftInput(), 200));
                    inputMsgDialog.setSendMsgClickCallBack(text -> {
                        sendMessage(text);
                        barrage_view.show(text, true);
                    });
                    inputMsgDialog.show();

                } else {
                    if (!inputMsgDialog.isShowing()) {
                        inputMsgDialog.show();
                    }
                }
                break;
            case R.id.tv_follow:
                TCEventHelper.onEvent(v.getContext(), AnalyDataValue.LIVE_DETAIL_ATTENTION_CLICK);
                Analysis.pushEvent(this, AnEvent.PageFollew,
                        new Analysis.Builder().
                                setLat(LocationManager.getInstance().getStorage().getLast_lat()).
                                setLng(LocationManager.getInstance().getStorage().getLast_lng()).
                                setId(String.valueOf(mLiveId)).
                                setType("直播").
                                setList(false));

                if (!userService.isLogin()) {
                    NavUtils.gotoLoginActivity(v.getContext());
                    return;
                }
                LiveController.getInstance().followAnchor(v.getContext(), mHandler, mLiveId, mAnchorUserId);
                break;
            case R.id.top_btn_follow:
                TCEventHelper.onEvent(v.getContext(), AnalyDataValue.LIVE_DETAIL_ATTENTION_CLICK);
                if (!userService.isLogin()) {
                    NavUtils.gotoLoginActivity(v.getContext());
                    return;
                }
                LiveController.getInstance().followAnchor(v.getContext(), mHandler, mLiveId, mAnchorUserId);
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
            case R.id.top_iv_anchor_head:
                NavUtils.gotoMasterHomepage(this, mAnchorUserId);
                break;
            case R.id.iv_anchor_head:
                NavUtils.gotoMasterHomepage(this, mAnchorUserId);
                break;
        }
    }

    /**
     * 分享
     */
    private void share() {
        // 埋点
        Analysis.pushEvent(this, AnEvent.PageShare,
                new Analysis.Builder().
                        setLat(LocationManager.getInstance().getStorage().getLast_lat()).
                        setLng(LocationManager.getInstance().getStorage().getLast_lng()).
                        setId(String.valueOf(mLiveId)).
                        setType("直播").
                        setList(false));

        if (!userService.isLogin()) {
            LoginActivity.gotoLoginActivity(this);
        } else {
            String shareTitle = "[直播]" + liveTittle;
            String videoUrl = SPUtils.getShareDefaultUrl(this, SysConfigType.URL_LIVE_SHARE_LINK) + batchId;
            String shareContent;
            String shareImg = "";
            String userName = "";
            if (mLiveRecordResult != null && mLiveRecordResult.userInfo != null) {
                userName = mLiveRecordResult.userInfo.nickname;
                shareImg = mLiveRecordResult.userInfo.avatar;
            }
            shareContent = userName + "  正在鹰和鹰直播，快来围观！";

            // 积分
            ShareUtils.ShareExtraInfo shareExtraInfo = new ShareUtils.ShareExtraInfo();
            shareExtraInfo.bizType = CircleBizType.SNS_LIVE;
            shareExtraInfo.bizId = mLiveId;
            shareExtraInfo.needDoShare = true;
            ShareUtils.showShareBoard(this, shareTitle, shareContent, videoUrl, shareImg, shareExtraInfo);

        }
    }

    private Api_SNSCENTER_SnsLiveRecordResult mLiveRecordResult;
    private boolean isFirstGetLiveRecord = true;
    private final int MSG_WHAT_DELAY_TASK = 0x11120;
    private static final int MSG_DO_GET_HAS_NO_END_BATCH = 0x11119;

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case MSG_DO_GET_HAS_NO_END_BATCH:
                Log.e(VerticalVideoClientActivity.class.getSimpleName(), "开始请求接口，是否有未完成的batch");
                LiveController.getInstance().getHasNoEndBatch(this, mHandler, mAnchorUserId);
                break;
            case LiveController.MSG_LIVE_FOLLOW_OK:
                // TODO: 8/30/16 关注成功
                FollowAnchorResult followAnchorResult = (FollowAnchorResult) msg.obj;
                if (followAnchorResult != null) {
                    if (LiveTypeConstants.FOLLOW_STATE_SUCCESS.equals(followAnchorResult.followStatus)) {
                        tv_follow.setBackground(getResources().getDrawable(R.drawable.bg_horizontal_followed));
                        tv_follow.setTextColor(getResources().getColor(R.color.unselect));
                        tv_follow.setEnabled(false);
                        top_btn_follow.setBackground(getResources().getDrawable(R.drawable.bg_followed));
                        top_btn_follow.setEnabled(false);
                        top_tv_follow_count.setText((fansCount + 1) + "");
                    } else {
                        ToastUtil.showToast(this, "关注失败");
                    }
                }
                break;
            case LiveController.MSG_LIVE_FOLLOW_ERROR:
                // TODO: 8/30/16 关注失败
                ToastUtil.showToast(this, StringUtil.handlerErrorCode(getApplicationContext(), msg.arg1));
                break;
            case LiveController.MSG_LIVE_ROOM_LIVEING_RECORD_ERROR:
                ToastUtil.showToast(this, "服务器异常");
//                finish();
                break;
            case MSG_GET_MASTER_DETAIL_OK:
                //the master detail info
                TalentInfo talentInfo = (TalentInfo) msg.obj;
                if (talentInfo == null) return;
                setAnchorData(talentInfo);
                break;
            case MSG_WHAT_DELAY_TASK:
                fetchData();
                break;
            case MSG_GET_LIVE_OVER_RESULT_OK:
                Api_SNSCENTER_SnsClosedViewTopNResult result = (Api_SNSCENTER_SnsClosedViewTopNResult) msg.obj;
                showLiveOver(result);
                break;
            case MSG_LIVE_LIST_OK:
                Api_SNSCENTER_SnsReplayListResult replayListResult = (Api_SNSCENTER_SnsReplayListResult) msg.obj;
                if (replayListResult == null) return;
                if (replayListResult.list == null) return;
                anchorInfoFragment.setReplayData(mAnchorUserId,mLiveId);
                break;
            case MSG_GET_HASH_NO_END_LIVE_RESULT_OK:
                Api_SNSCENTER_SnsHasNoEndBatchResult noEndBatchResult = (Api_SNSCENTER_SnsHasNoEndBatchResult) msg.obj;
                if (noEndBatchResult != null) {
                    mLiveId = noEndBatchResult.liveId;
                    if (noEndBatchResult.hasNoEndBatch) {
                        if (!TextUtils.isEmpty(noEndBatchResult.pullStreamUrlFlv)) {
                            Log.e("EZEZ", "拿到结果" + "url = " + noEndBatchResult.pullStreamUrlFlv);
                            mHandler.removeMessages(MSG_DO_GET_HAS_NO_END_BATCH);
                            videoView.starPlay(noEndBatchResult.pullStreamUrlFlv);
                            mHandler.sendEmptyMessageDelayed(MSG_WHAT_DELAY_TASK, 5 * 1000);
                        } else {
                            Log.e("EZEZ", "没有拿到结果" + "url ，继续10秒后重连");
                            mHandler.removeMessages(MSG_DO_GET_HAS_NO_END_BATCH);
                            mHandler.sendEmptyMessageDelayed(MSG_DO_GET_HAS_NO_END_BATCH, 10 * 1000);
                        }
                    } else {
                        getRecentLive();
                    }
                } else {
                    Log.e("EZEZ", "没有拿到结果" + "url ，继续10秒后重连");
                    mHandler.removeMessages(MSG_DO_GET_HAS_NO_END_BATCH);
                    mHandler.sendEmptyMessageDelayed(MSG_DO_GET_HAS_NO_END_BATCH, 10 * 1000);
                }
                break;
            case MSG_GET_LIVE_RECORD_OK:
                mLiveRecordResult = (Api_SNSCENTER_SnsLiveRecordResult) msg.obj;
                if (mLiveRecordResult == null) return;

                tv_online_count.setText(mLiveRecordResult.viewCount + "");
                mRoomId = mLiveRecordResult.roomId;
                mAnchorUserId = mLiveRecordResult.userInfo.userId;
                try {
                    String time = com.newyhy.utils.DateUtil.getCreateAt(mLiveRecordResult.startDate);
                    tv_publish_time.setText(time);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if ("END".equals(mLiveRecordResult.batchEnd)) {
                    //请求主播信息
                    fetchMasterData();
                    getRecentLive();
                    return;
                }

                if (!isFirstGetLiveRecord) {
                    Log.e("EZEZ", "live status = " + mLiveRecordResult.liveStatus);
                    if (!LiveTypeConstants.LIVE_ING.equals(mLiveRecordResult.liveStatus)) {
                        LiveController.getInstance().getHasNoEndBatch(this, mHandler, mAnchorUserId);
                    } else {
                        mHandler.removeMessages(MSG_WHAT_DELAY_TASK);
                        mHandler.sendEmptyMessageDelayed(MSG_WHAT_DELAY_TASK, 5 * 1000);
                    }
                }

                if (isFirstGetLiveRecord) {
                    //请求主播信息
                    fetchMasterData();
                    batchId = mLiveRecordResult.batchId;
                    anchorInfoFragment.setReplayData(mAnchorUserId,mLiveId);
                    liveTittle = mLiveRecordResult.liveTitle;
                    liveChatListFragment.setZanStatus(TYPE_AVAILABLE.equals(mLiveRecordResult.isSupport), mLiveRecordResult.ugcId);
                    top_tv_tittle.setText(liveTittle);
                    /*ugcId = mLiveRecordResult.ugcId;
                    isSupport = TYPE_AVAILABLE.equals(mLiveRecordResult.isSupport);
                    iv_zan.setImageResource(isSupport ? R.drawable.ic_zaned : R.drawable.ic_un_zan);*/
                    //添加直播公告
                    if (!isAddConfig && mLiveRecordResult.liveConfig != null && mLiveRecordResult.liveConfig.length() > 0) {
                        liveChatListFragment.addAffiche(mLiveRecordResult.liveConfig);//添加公告(竖屏直播中消息list添加)
                        isAddConfig = true;
                    }
                    if (LiveTypeConstants.LIVE_ING.equals(mLiveRecordResult.liveStatus)) {
                        if (!TextUtils.isEmpty(mLiveRecordResult.pullStreamUrlFlv)) {
                            mLiveUrl = "";
                            mLiveUrl = mLiveRecordResult.pullStreamUrlFlv;
                            registNet();
                            isFirstGetLiveRecord = false;
                            mHandler.sendEmptyMessageDelayed(MSG_WHAT_DELAY_TASK, 5 * 1000);
                        }
                    } else {
                        // 直播已经结束
                        isFirstGetLiveRecord = false;
                        LiveController.getInstance().getHasNoEndBatch(this, mHandler, mAnchorUserId);
                    }
                }
        }

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
     * 直播结束,显示直播结束页面
     */
    private void getRecentLive() {
        LiveController.getInstance().getLiveOverResult(this, mHandler, mRoomId);
    }

    /**
     * 是否显示直播结束页面
     */
    private boolean shouldShowLiveFinish = false;

    /**
     * 显示直播结束页面
     *
     * @param result
     */
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
        ImageView iv_finish = findViewById(R.id.ib_finish);
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
                // 埋点
                Analysis.pushEvent(HorizontalLiveActivity.this, AnEvent.PageVideoRecommend,
                        new Analysis.Builder().
                                setLat(LocationManager.getInstance().getStorage().getLast_lat()).
                                setLng(LocationManager.getInstance().getStorage().getLast_lng()).
                                setId(String.valueOf(data.liveId)));

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

    private void registNet() {
        isNetBroadRegisted = true;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mNetBroadCast, intentFilter);
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
        top_tv_follow_count.setText("粉丝 " + talentInfo.fansCount);
        fansCount = talentInfo.fansCount;
        if (mAnchorUserId == userService.getLoginUserId()) {
            tv_follow.setBackground(getResources().getDrawable(R.drawable.un_follow_bg));
            top_btn_follow.setBackground(getResources().getDrawable(R.drawable.un_follow_bg));
            tv_follow.setEnabled(false);
            top_btn_follow.setEnabled(false);
            return;
        }
        if (!userService.isLogin()) {
            tv_follow.setBackground(getResources().getDrawable(R.drawable.un_follow_bg));
            top_btn_follow.setBackground(getResources().getDrawable(R.drawable.un_follow_bg));
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
        // 埋点
        Analysis.pushEvent(this, AnEvent.PageLiveClose,
                new Analysis.Builder().
                        setLat(LocationManager.getInstance().getStorage().getLast_lat()).
                        setLng(LocationManager.getInstance().getStorage().getLast_lng()).
                        setId(String.valueOf(mLiveId)).
                        setLiveState(true));

        if (mHandler != null) {
            mHandler.setValid(false);
            mHandler.removeCallbacksAndMessages(null);
        }
        if (mHandler != null) {
            mHandler.removeMessages(MSG_DO_GET_HAS_NO_END_BATCH);
            mHandler.removeMessages(MSG_WHAT_DELAY_TASK);
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
        if (isConnector) mConnector.disconnect(this);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            isFullScreen = !isFullScreen;
            //当前是全屏
            findViewById(R.id.rl_video_tittle).setVisibility(View.VISIBLE);
            ViewGroup.LayoutParams layoutParams = findViewById(R.id.rl_video_view).getLayoutParams();
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            findViewById(R.id.rl_video_view).setLayoutParams(layoutParams);
            findViewById(R.id.rl_say_something).setVisibility(View.VISIBLE);
            findViewById(R.id.view_shadow).setVisibility(View.VISIBLE);
            barrage_view.setVisibility(View.VISIBLE);
            findViewById(R.id.rl_online_num).setVisibility(View.INVISIBLE);
            findViewById(R.id.full_screen_gap_line).setVisibility(View.VISIBLE);
            findViewById(R.id.view_input_shadow).setVisibility(View.VISIBLE);
            findViewById(R.id.rl_say_something).setVisibility(View.VISIBLE);
            videoView.needShowFullScreenButton(false);
        } else {
            //当前是半屏
            isFullScreen = !isFullScreen;
            findViewById(R.id.rl_video_tittle).setVisibility(View.GONE);
            ViewGroup.LayoutParams layoutParams = findViewById(R.id.rl_video_view).getLayoutParams();
            layoutParams.height = (int) getResources().getDimension(R.dimen.yhy_size_233px);
            findViewById(R.id.rl_video_view).setLayoutParams(layoutParams);
            findViewById(R.id.rl_say_something).setVisibility(View.GONE);
            findViewById(R.id.view_shadow).setVisibility(View.GONE);
            barrage_view.setVisibility(View.GONE);
            findViewById(R.id.rl_online_num).setVisibility(View.VISIBLE);
            findViewById(R.id.full_screen_gap_line).setVisibility(View.GONE);
            findViewById(R.id.view_input_shadow).setVisibility(View.GONE);
            findViewById(R.id.rl_say_something).setVisibility(View.GONE);
            videoView.needShowFullScreenButton(true);
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
