package com.newyhy.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.newyhy.config.CircleBizType;
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
import com.quanyan.yhy.ui.club.clubcontroller.ClubController;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.login.LoginActivity;
import com.smart.sdk.api.resp.Api_SNSCENTER_SnsClosedViewTopNResult;
import com.smart.sdk.api.resp.Api_SNSCENTER_SnsHasNoEndBatchResult;
import com.smart.sdk.api.resp.Api_SNSCENTER_SnsLiveRecordResult;
import com.umeng.analytics.MobclickAgent;
import com.videolibrary.adapter.ChatListAdapter;
import com.videolibrary.chat.entity.LiveChatNotifyMessage;
import com.videolibrary.chat.entity.LiveChatTextMessage;
import com.videolibrary.chat.event.LiveChatMessageEvent;
import com.videolibrary.chat.event.LiveChatPersonCountEvent;
import com.videolibrary.chat.event.LiveStatusEvent;
import com.videolibrary.chat.service.LiveChatService;
import com.videolibrary.chat.service.LiveChatServiceConnector;
import com.videolibrary.client.activity.VerticalVideoClientActivity;
import com.videolibrary.controller.LiveController;
import com.videolibrary.core.NetBroadCast;
import com.videolibrary.metadata.LiveTypeConstants;
import com.videolibrary.metadata.NetStateBean;
import com.videolibrary.utils.IntentUtil;
import com.videolibrary.utils.NetWorkUtil;
import com.yhy.common.base.BaseNewActivity;
import com.yhy.common.base.NoLeakHandler;
import com.yhy.common.beans.net.model.comment.ComSupportInfo;
import com.yhy.common.beans.net.model.master.TalentInfo;
import com.yhy.common.beans.net.model.msg.FollowAnchorResult;
import com.yhy.common.constants.IntentConstants;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.eventbus.event.EvBusVideoPraiseChange;
import com.yhy.common.listener.OnMultiClickListener;
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

import static com.newyhy.views.TXVideoView.TX_LIVE_PLAYER;
import static com.newyhy.views.TXVideoView.VERTICAL_LIVE;
import static com.tencent.rtmp.TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN;
import static com.videolibrary.controller.LiveController.MSG_GET_HASH_NO_END_LIVE_RESULT_OK;
import static com.videolibrary.controller.LiveController.MSG_GET_LIVE_OVER_RESULT_OK;
import static com.videolibrary.controller.LiveController.MSG_GET_LIVE_RECORD_OK;
import static com.videolibrary.controller.LiveController.getInstance;
import static com.yhy.common.constants.ValueConstants.MSG_GET_MASTER_DETAIL_OK;
import static com.yhy.common.constants.ValueConstants.TYPE_AVAILABLE;

@Route(path = RouterPath.ACTIVITY_VERTICAL_LIVE)
public class VerticalLiveActivity extends BaseNewActivity implements View.OnClickListener,
        NoLeakHandler.HandlerCallback {

    private int mLastDiff;
    private InputMsgDialog inputMsgDialog;
    private TXVideoView videoView;
    boolean isPlaying = false;
    private long mRoomId = -1;

    private ImageView iv_anchor_head, iv_zan;
    private TextView tv_anchor_name, tv_follow_num, tv_follow, tv_online_count;
    private RelativeLayout rl_tittle, rl_online_num;
    private RecyclerView rv_chat;
    private ChatListAdapter v_live_client_adapter;
    private String mLiveUrl;
    private boolean isNetBroadRegisted = false;
    private NetBroadCast mNetBroadCast;
    private long fansCount = 0;
    private String liveTittle = "";

    @Autowired(name = "liveId")
    long mLiveId;
    @Autowired(name = "anchorId")
    long mAnchorUserId;
    @Autowired
    IUserService userService;
    private ClubController mClubController;
    private long ugcId;
    private boolean isSupport;
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
    protected int setLayoutId() {
        return R.layout.ac_vertical_live;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //事件统计
        Analysis.pushEvent(this, AnEvent.PLAY_PAGE_PLAYBACK);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initVars() {
        super.initVars();
        mClubController = new ClubController(this, mHandler);
    }

    @Override
    protected void initView() {
        super.initView();
        videoView = findViewById(R.id.video_view);
        videoView.initPlayer(TX_LIVE_PLAYER, VERTICAL_LIVE, RENDER_MODE_FULL_FILL_SCREEN);

        iv_anchor_head = findViewById(R.id.iv_anchor_head);
        tv_anchor_name = findViewById(R.id.tv_anchor_name);
        tv_follow_num = findViewById(R.id.tv_follow_count);
        tv_follow = findViewById(R.id.btn_follow);
        tv_online_count = findViewById(R.id.tv_online_count);
        rl_tittle = findViewById(R.id.rl_tittle);
        rl_online_num = findViewById(R.id.rl_online_num);
        rv_chat = findViewById(R.id.rv_chat);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_chat.setLayoutManager(manager);
        v_live_client_adapter = new ChatListAdapter(this, new ArrayList());
        rv_chat.setAdapter(v_live_client_adapter);
        iv_zan = findViewById(R.id.ic_zan);
    }

    @Override
    protected void setListener() {
        super.setListener();
        videoView.setTXPlayerStatusListener(new TXVideoView.TXPlayerStatusListener() {
            @Override
            public void onStatusChange(int currentState) {
            }

            @Override
            public void showController(boolean show) {
                AlphaAnimationUtils.alphaAnimation(rl_online_num, show ? 1.0f : 0.0f, show ? 0.0f : 1.0f, 300, !show);
                AlphaAnimationUtils.alphaAnimation(rl_tittle, show ? 1.0f : 0.0f, show ? 0.0f : 1.0f, 300, !show);
                AlphaAnimationUtils.alphaAnimation(findViewById(R.id.bottom_shadow), show ? 1.0f : 0.0f, show ? 0.0f : 1.0f, 300, !show);
                AlphaAnimationUtils.alphaAnimation(findViewById(R.id.rl_say_something), show ? 1.0f : 0.0f, show ? 0.0f : 1.0f, 300, !show);
            }

            @Override
            public void fullScreenButtonClick() {

            }

            @Override
            public void onSeekBarTracking(int trackProgress) {
                
            }

            @Override
            public void onCenterButtonClick(boolean isPlay) {
                
            }
        });
        iv_anchor_head.setOnClickListener(this);
        tv_follow.setOnClickListener(this);
        findViewById(R.id.iv_share).setOnClickListener(this);
        iv_zan.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                Analysis.pushEvent(VerticalLiveActivity.this, AnEvent.PageLike,
                        new Analysis.Builder().
                                setLat(LocationManager.getInstance().getStorage().getLast_lat()).
                                setLng(LocationManager.getInstance().getStorage().getLast_lng()).
                                setId(String.valueOf(mLiveId)).
                                setType("直播").
                                setList(false));

                if (userService.isLogin()) {
                    String outType = ValueConstants.TYPE_PRAISE_LIVESUP;
                    mClubController.doAddNewPraiseToComment(VerticalLiveActivity.this, ugcId, outType, isSupport ? ValueConstants.UNPRAISE : ValueConstants.PRAISE);
                } else {
                    NavUtils.gotoLoginActivity(VerticalLiveActivity.this);
                }
            }
        });
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

        //连接上im
        if (mChatService == null) {
            mConnector.connect(this);
        }

        mNetBroadCast = new NetBroadCast();

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
    protected void initData() {
        super.initData();
        fetchData();
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

    /**
     * 根据liveId查询当前直播状态
     */
    private void fetchData() {
        LiveController.getInstance().getLiveRecord(this, mHandler, mLiveId);
    }

    /**
     * 直播结束,显示直播结束页面
     */
    private void getRecentLive() {
        LiveController.getInstance().getLiveOverResult(this, mHandler, mRoomId);
    }

    private Api_SNSCENTER_SnsLiveRecordResult mLiveRecordResult;
    private boolean isFirstGetLiveRecord = true;
    private final int MSG_WHAT_DELAY_TASK = 0x11120;
    private static final int MSG_DO_GET_HAS_NO_END_BATCH = 0x11119;

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case ValueConstants.MSG_PRAISE_OK:
                ComSupportInfo comSupportInfo = (ComSupportInfo) msg.obj;
                isSupport = !isSupport;
                iv_zan.setImageResource(isSupport ? R.drawable.ic_zaned : R.drawable.ic_un_zan);
                EventBus.getDefault().post(new EvBusVideoPraiseChange(ugcId,mLiveId,isSupport));
                break;
            case MSG_DO_GET_HAS_NO_END_BATCH:
                Log.e(VerticalVideoClientActivity.class.getSimpleName(), "开始请求接口，是否有未完成的batch");
                LiveController.getInstance().getHasNoEndBatch(this, mHandler, mAnchorUserId);
                break;
            case LiveController.MSG_LIVE_FOLLOW_OK:
                // TODO: 8/30/16 关注成功
                FollowAnchorResult followAnchorResult = (FollowAnchorResult) msg.obj;
                if (followAnchorResult != null) {
                    if (LiveTypeConstants.FOLLOW_STATE_SUCCESS.equals(followAnchorResult.followStatus)) {
                        //竖屏看直播情况下关注成功
                        tv_follow.setBackground(getResources().getDrawable(R.drawable.bg_followed));
                        tv_follow.setEnabled(false);
                        tv_follow_num.setText((fansCount + 1) + "");
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
                    liveTittle = mLiveRecordResult.liveTitle;
                    ugcId = mLiveRecordResult.ugcId;
                    isSupport = TYPE_AVAILABLE.equals(mLiveRecordResult.isSupport);
                    iv_zan.setImageResource(isSupport ? R.drawable.ic_zaned : R.drawable.ic_un_zan);
                    //添加直播公告
                    if (!isAddConfig && mLiveRecordResult.liveConfig != null && mLiveRecordResult.liveConfig.length() > 0) {
                        v_live_client_adapter.addAffiche(mLiveRecordResult.liveConfig);//添加公告(竖屏直播中消息list添加)
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
        tv_anchor_name.setText(talentInfo.nickName);
        tv_follow_num.setText("粉丝 " + talentInfo.fansCount);
        fansCount = talentInfo.fansCount;
        tv_online_count.setText(mLiveRecordResult.viewCount + "");
        if (mAnchorUserId == userService.getLoginUserId()) {
            tv_follow.setBackground(getResources().getDrawable(R.drawable.un_follow_bg));
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.et_comment:
                if (inputMsgDialog == null) {
                    inputMsgDialog = new InputMsgDialog(this, R.style.Theme_Light_Dialog);
                    inputMsgDialog.setOnShowListener(dialog -> mHandler.postDelayed(() -> inputMsgDialog.showSoftInput(), 100));
                    inputMsgDialog.setSendMsgClickCallBack(text -> {
                        sendMessage(text);
                        inputMsgDialog.dismiss();
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
//                    ToastUtil.showToast(v.getContext(), "关注");
                LiveController.getInstance().followAnchor(v.getContext(), mHandler, mLiveId, mAnchorUserId);
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

    //发送消息
    private void sendMessage(String strings) {
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
            if (getChatService() != null) {
                LiveChatTextMessage message = new LiveChatTextMessage(fromId, mLiveId, fromName, fromPic, strings);
                getChatService().liveChatMessageManager.sendMessage(message);
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
                    v_live_client_adapter.addMessage(message);
                }

                if (event.object instanceof LiveChatNotifyMessage) {
                    LiveChatNotifyMessage message = (LiveChatNotifyMessage) event.object;
                    v_live_client_adapter.addMessage(message);
                }
                rv_chat.scrollToPosition(v_live_client_adapter.getItemCount() - 1);
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
                if (event.notifyInOut == null) return;
                //tv_online_count.setText(event.notifyInOut.getCurrentUsersCount()+"");
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case IntentConstants.LOGIN_RESULT:
                if (resultCode == -1) {
                    fetchMasterData();
                    if (mConnector != null) {
                        mConnector.disconnect(this);
                        mConnector.connect(this);
                    }
                }
                break;
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

    public void onEventMainThread(LiveStatusEvent event) {
        switch (event) {
            case LIVE_FINISH:
                break;
            case LIVE_INVALID:
                break;
            case LIVE_MASTER_FINISH:
                break;
            case DELETE_LIVE:
                if (videoView != null) {
                    videoView.pausePlay();
                }
                showSoldOutDialog(getString(R.string.label_video_deleted));
                break;
            case LIVE_OFF_SHELVE:
                if (videoView != null) {
                    videoView.pausePlay();
                }
                showSoldOutDialog(getString(R.string.label_video_sold_out));
                break;
        }
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
     * 显示直播结束的画面
     *
     * @param result
     */
    private void showLiveOver(final Api_SNSCENTER_SnsClosedViewTopNResult result) {
        if (result == null || result.list == null) return;
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
            switch (i) {
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
                    RoundImageView live_first_item_ivCover = findViewById(R.id.live_first_item_ivCover);
                    live_first_item_ivCover.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int screenType;
                            if (result.list.get(0).liveScreenType.equals("VERTICAL")) {
                                screenType = 1;
                            } else {
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
                    ImageLoadManager.loadImage(result.list.get(i).liveCover, 0, live_first_item_ivCover);
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
                    RoundImageView live_second_item_ivCover = findViewById(R.id.live_second_item_ivCover);
                    live_second_item_ivCover.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int screenType;
                            if (result.list.get(1).liveScreenType.equals("VERTICAL")) {
                                screenType = 1;
                            } else {
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
                    ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(result.list.get(i).liveCover), 0, live_second_item_ivCover);
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
                    RoundImageView live_third_item_ivCover = findViewById(R.id.live_third_item_ivCover);
                    live_third_item_ivCover.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int screenType;
                            if (result.list.get(2).liveScreenType.equals("VERTICAL")) {
                                screenType = 1;
                            } else {
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
                    ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(result.list.get(i).liveCover), 0, live_third_item_ivCover);
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

}
