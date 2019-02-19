package com.newyhy.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.newyhy.utils.ShareUtils;
import com.newyhy.utils.UpLoadProgressListener;
import com.newyhy.utils.live.LiveHelper;
import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.AnalyDataValue;
import com.quanyan.yhy.ui.base.utils.DialogUtil;
import com.quanyan.yhy.ui.base.utils.TCEventHelper;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.util.MobileUtil;
import com.smart.sdk.api.resp.Api_SNSCENTER_SnsCloseLiveResult;
import com.smart.sdk.api.resp.Api_SNSCENTER_SnsCreateLiveResult;
import com.smart.sdk.api.resp.Api_SNSCENTER_SnsLiveRecordResult;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.ui.TXCloudVideoView;
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
import com.videolibrary.chat.service.LiveChatService;
import com.videolibrary.chat.service.LiveChatServiceConnector;
import com.videolibrary.controller.LiveController;
import com.videolibrary.metadata.LiveTypeConstants;
import com.videolibrary.puhser.view.PopView;
import com.videolibrary.utils.IntentUtil;
import com.videolibrary.utils.NetWorkUtil;
import com.yhy.common.base.NoLeakHandler;
import com.yhy.common.beans.net.model.msg.LiveRecordInfo;
import com.yhy.common.beans.net.model.msg.OtherMsgParam;
import com.yhy.common.config.ContextHelper;
import com.yhy.common.constants.HandlerConstant;
import com.yhy.common.types.SysConfigType;
import com.yhy.common.utils.AndroidUtils;
import com.yhy.common.utils.CommonUtil;
import com.yhy.common.utils.SPUtils;
import com.yhy.router.YhyRouter;
import com.yhy.service.IUserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;

import static com.tencent.rtmp.TXLiveConstants.VIDEO_QUALITY_HIGH_DEFINITION;
import static com.tencent.rtmp.TXLiveConstants.VIDEO_QUALITY_STANDARD_DEFINITION;
import static com.tencent.rtmp.TXLiveConstants.VIDEO_QUALITY_SUPER_DEFINITION;
import static com.umeng.socialize.utils.ContextUtil.getContext;
public class NewPushStreamVerticalActivity extends AppCompatActivity implements
        NoLeakHandler.HandlerCallback,View.OnClickListener{

    private TextView btnPublish;
    private LiveHelper mPublisher;
    private NoLeakHandler mHandler;
    private LiveController mLiveController;
    private long liveId;

    private RecyclerView rv_chat;
    private boolean isFolded = false;
    private ChatListAdapter v_im_adapter;

    private String title;

    private LiveRecordInfo info;

    private boolean isStartPush = false;

    private long batchID;

    /**
     * 直播清晰度
     */
    private int defintionIndex;
    private UpLoadProgressListener upLoadProgressListener;
    private Dialog mPermissionDialog;
    private long startLiveTime = 0;

    private TXCloudVideoView mPreviewSurface;
    private boolean isKickout = false;
    private PopView mPopView;

    @Autowired
    IUserService userService;
    /**
     * 是否是重新推流
     */
    private boolean openFlash = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        YhyRouter.getInstance().inject(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.new_push_stream_vertical_activity);

        requestPermissions();

        // response screen rotation event
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        initView();

        shareLayoutConfig();

        initTopBarView(false);

        //初始化传过来的数据
        initData();

        //初始化推流器
        initPusher();

    }

    private void initView() {
        rv_chat = findViewById(R.id.rv_chart);
        LinearLayoutManager manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        rv_chat.setLayoutManager(manager);
        v_im_adapter = new ChatListAdapter(this,new ArrayList());
        rv_chat.setAdapter(v_im_adapter);
    }

    private void initTopBarView(boolean isPushing) {
        if (isPushing){
            findViewById(R.id.ib_back).setVisibility(View.GONE);

            findViewById(R.id.ib_close_chat_list).setVisibility(View.VISIBLE);
            findViewById(R.id.ib_close_chat_list).setOnClickListener(this);

            findViewById(R.id.ib_share).setVisibility(View.VISIBLE);
            findViewById(R.id.ib_share).setOnClickListener(this);

            findViewById(R.id.ll_share_layout).setVisibility(View.GONE);
            btnPublish.setVisibility(View.GONE);

            findViewById(R.id.iv_finish_live).setVisibility(View.VISIBLE);
            findViewById(R.id.iv_finish_live).setOnClickListener(this);

            final TextView tv_send_speed = findViewById(R.id.tv_send_speed);
            tv_send_speed.setVisibility(View.VISIBLE);
            upLoadProgressListener = new UpLoadProgressListener(getContext());
            upLoadProgressListener.setRXListener(new UpLoadProgressListener.RXListener() {
                @Override
                public void getSpeed(final int speed) {
                    Log.e("EZEZ","当前推流速度 = "+ speed);
                    if (speed >= 1000){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tv_send_speed.setText(String.valueOf(((float) speed) / 1000f) + "M/s");
                            }
                        });
                    }else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tv_send_speed.setText(String.valueOf(speed) + "Kb/s");
                            }
                        });
                    }
                }

            });
            upLoadProgressListener.start();

        }else {
            findViewById(R.id.ib_back).setVisibility(View.VISIBLE);
            findViewById(R.id.ib_back).setOnClickListener(this);

            findViewById(R.id.swCam).setVisibility(View.VISIBLE);
            findViewById(R.id.swCam).setOnClickListener(this);

            findViewById(R.id.ib_flash_light).setVisibility(View.VISIBLE);
            findViewById(R.id.ib_flash_light).setOnClickListener(this);
        }
    }

    /**
     * 初始化推流器
     */
    private void initPusher() {
        btnPublish =  findViewById(R.id.publish);
        btnPublish.setOnClickListener(this);
        mPreviewSurface = findViewById(R.id.glsurfaceview_camera);
        mPublisher = LiveHelper.getLiveHelper();
        mPublisher.initPublisher(this);
        mPublisher.initLiveConfig();
        mPublisher.setAnchorOrientation(0);
        mPublisher.setPushOrientation(TXLiveConstants.VIDEO_ANGLE_HOME_DOWN);
        mPublisher.setMirror(true);
        switch (defintionIndex){
            //标清
            case (0):
                mPublisher.setLiveQuality(VIDEO_QUALITY_STANDARD_DEFINITION);
                break;
                //高清
            case (1):
                mPublisher.setLiveQuality(VIDEO_QUALITY_HIGH_DEFINITION);
                break;
                //超清
            case (2):
                mPublisher.setLiveQuality(VIDEO_QUALITY_SUPER_DEFINITION);
                break;
        }
        mPublisher.startCameraPreview(mPreviewSurface);
    }

    /**
     * 创建直播
     */
    private void createLive() {

        mLiveController = LiveController.getInstance();

        mLiveController.createLive(this, mHandler, info);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPreviewSurface.onResume();
        //mPublisher.resumePush();
        mHandler.removeMessages(HandlerConstant.NEED_STOP_PUSH);

    }

    @Override
    protected void onPause() {
        super.onPause();
        mPreviewSurface.onPause();
        //mPublisher.pausePush();
        mHandler.sendEmptyMessageDelayed(HandlerConstant.NEED_STOP_PUSH,90 * 1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isConnector) {//断开IM
            mConnector.disconnect(this);
            isConnector = false;
        }
        mPreviewSurface.onDestroy();
        mPublisher.stopRtmpPublish();
        mHandler.removeMessages(MSG_WHAT_DELAY_TASK);
    }

    /**
     * 初始化直播间数据
     */
    private void initData() {
        mHandler = new NoLeakHandler(this);
        mLiveController = LiveController.getInstance();

        info = new LiveRecordInfo();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.getLong(IntentUtil.BUNDLE_BATCH_ID,0) == 0) {
                info.liveCategory = bundle.getInt(IntentUtil.BUNDLE_CATEGORY_CODE, 0);
                info.roomId = bundle.getLong(IntentUtil.BUNDLE_ROOM_ID, 0);
                info.liveTitle = bundle.getString(IntentUtil.BUNDLE_TITLE);
                info.liveScreenType = bundle.getInt(IntentUtil.BUNDLE_LIVE_SCREEN_TYPE);
                info.locationCityCode = bundle.getString(IntentUtil.BUNDLE_CITYCODE);
                info.locationCityName = bundle.getString(IntentUtil.BUNDLE_CITYNAME);
                info.liveNotice = bundle.getString(IntentUtil.BUNDLE_NOTICE);
            }else {
                info.batchId = bundle.getLong(IntentUtil.BUNDLE_BATCH_ID,0);
            }
        }

        defintionIndex = SPUtils.getInt(this, SPUtils.EXTRA_LIVE_DEFINTION_INDEX, 1);

        liveId = bundle.getLong(IntentUtil.BUNDLE_LIVEID, 0);
        title = bundle.getString(IntentUtil.BUNDLE_TITLE);

    }

    /**
     * 获取推流状态,恢复现场
     */
    private void fetchLiveState() {
        if (liveId > 0) {
            mLiveController.getLiveRecord(this, mHandler, liveId);
        }
    }

    /***************************************************************     IM     ********************************************************************/

    private LiveChatService mChatService;
    private boolean isConnector = false;
    LiveChatServiceConnector mConnector = new LiveChatServiceConnector() {
        @Override
        public void onIMServiceConnected(LiveChatService chatService) {
            isConnector = true;
            if (chatService == null || liveId == 0) {
                ToastUtil.showToast(NewPushStreamVerticalActivity.this, "create live error");
                finish();
                return;
            }

            mChatService = chatService;
            EventBus.getDefault().register(NewPushStreamVerticalActivity.this);
            mChatService.liveChatLoginManager.login(liveId);

        }

        @Override
        public void onServiceDisconnected() {
            isConnector = false;
            if (EventBus.getDefault().isRegistered(NewPushStreamVerticalActivity.this)) {
                EventBus.getDefault().unregister(NewPushStreamVerticalActivity.this);
            }
        }
    };

    private void initService() {
        mConnector.connect(this);
    }

    public void onEventMainThread(LiveChatMessageEvent event) {
        switch (event.getEvent()) {
            case SEND_SUCESS:
            case REC:
                if (event.object instanceof LiveChatTextMessage) {
                    LiveChatTextMessage message = (LiveChatTextMessage) event.object;
                    v_im_adapter.addMessage(message);
                }

                if (event.object instanceof LiveChatNotifyMessage) {
                    LiveChatNotifyMessage message = (LiveChatNotifyMessage) event.object;
                    v_im_adapter.addMessage(message);
                }
                rv_chat.scrollToPosition(v_im_adapter.getItemCount()-1);
                break;
            case FORBIN_TALK:
                /*showForbinTalkDialog();*/
                break;
        }
    }

    public void onEventMainThread(LiveChatPersonCountEvent event) {
        switch (event.event) {
            case LOGIN:
            case LOGOUT:
                if (event.notifyInOut == null) return;
                setOnlineCount((int) event.notifyInOut.getCurrentUsersCount());
                break;
        }
    }

    /*************************************************************       Share Model      ******************************************************************/
    private int behindStartLiveShareIndex = -1;
    /**
     * 配置分享按钮
     */
    private void shareLayoutConfig() {
        final ImageView behind_start_live_share_wx = findViewById(R.id.behind_start_live_share_wx);
        final ImageView behind_start_live_share_wx_friends = findViewById(R.id.behind_start_live_share_wx_friends);
        final ImageView behind_start_live_share_qq = findViewById(R.id.behind_start_live_share_qq);
        final ImageView behind_start_live_share_wb = findViewById(R.id.behind_start_live_share_wb);

        behind_start_live_share_wx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                behindStartLiveShareIndex = 0;
                behind_start_live_share_wx.setSelected(!behind_start_live_share_wx.isSelected());
                behind_start_live_share_wx_friends.setSelected(false);
                behind_start_live_share_qq.setSelected(false);
                behind_start_live_share_wb.setSelected(false);

                if (!behind_start_live_share_wx.isSelected()) behindStartLiveShareIndex = -1;
            }
        });

        behind_start_live_share_wx_friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                behindStartLiveShareIndex = 1;
                behind_start_live_share_wx.setSelected(false);
                behind_start_live_share_wx_friends.setSelected(!behind_start_live_share_wx_friends.isSelected());
                behind_start_live_share_qq.setSelected(false);
                behind_start_live_share_wb.setSelected(false);

                if (!behind_start_live_share_wx_friends.isSelected()) behindStartLiveShareIndex = -1;
            }
        });

        behind_start_live_share_qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                behindStartLiveShareIndex = 2;
                behind_start_live_share_wx.setSelected(false);
                behind_start_live_share_wx_friends.setSelected(false);
                behind_start_live_share_qq.setSelected(!behind_start_live_share_qq.isSelected());
                behind_start_live_share_wb.setSelected(false);

                if (!behind_start_live_share_qq.isSelected()) behindStartLiveShareIndex = -1;
            }
        });

        behind_start_live_share_wb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                behindStartLiveShareIndex = 3;
                behind_start_live_share_wx.setSelected(false);
                behind_start_live_share_wx_friends.setSelected(false);
                behind_start_live_share_qq.setSelected(false);
                behind_start_live_share_wb.setSelected(!behind_start_live_share_wb.isSelected());

                if (!behind_start_live_share_wb.isSelected()) behindStartLiveShareIndex = -1;
            }
        });
    }

    UMShareAPI umShareAPI;
    private int jumpToShare(String shareTitle, String shareContent, int position) {
        if (position == -1) {
            return 0;
        }
        if (!AndroidUtils.isNetWorkAvalible(this)) {
            ToastUtil.showToast(this, getString(R.string.error_label_popup_wlyc));
            return -1;
        }
        if (umShareAPI == null) {
            umShareAPI = UMShareAPI.get(this);
        }
        String shareWebPage = getLiveShareUrl();
//        Bitmap logoBm = BitmapFactory.decodeResource(getResources(), R.mipmap.app_logo_share);
        UMWeb web = new UMWeb(shareWebPage);
        web.setTitle(shareTitle);//标题
        web.setThumb(new UMImage(NewPushStreamVerticalActivity.this, CommonUtil.getImageFullUrl(SPUtils.getUserIcon(this))));  //缩略图
        web.setDescription(!StringUtil.isEmpty(shareContent) ? shareContent : "");//描述

        if (position == 0) {
            if (!MobileUtil.isWeixinAvilible(this)) {
                ToastUtil.showToast(this, getString(R.string.wx_isinstall));
                return 0;
            }
            //微信分享
            new ShareAction(this).setPlatform(SHARE_MEDIA.WEIXIN)
                    .setCallback(umShareListener)
                    .withMedia(web)
                    .share();
        } else if (position == 1) {
            if (!MobileUtil.isWeixinAvilible(this)) {
                ToastUtil.showToast(this, getString(R.string.wx_isinstall));
                return 0;
            }
            // 设置朋友圈分享的内容
            ShareAction sa = new ShareAction(this);
            sa.setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE);
            if (umShareListener != null) {
                sa.setCallback(umShareListener);
            }

            sa.withMedia(web);
            sa.share();
        } else if (position == 2) {
            if (!umShareAPI.isInstall(this, SHARE_MEDIA.QQ)) {
                ToastUtil.showToast(this, getString(R.string.qq_isinstall));
                return 0;
            }
            //QQ分享
            new ShareAction(this).setPlatform(SHARE_MEDIA.QQ)
                    .setCallback(umShareListener)
                    .withMedia(web)
                    .share();
        } else if (position == 3) {
            //新浪微博
            ShareAction shareAction = new ShareAction(NewPushStreamVerticalActivity.this);
            shareAction
                    .setCallback(umShareListener)
                    .setPlatform(SHARE_MEDIA.SINA)
                    .withMedia(new UMImage(NewPushStreamVerticalActivity.this, CommonUtil.getImageFullUrl(SPUtils.getUserIcon(this))))
                    .withText(shareTitle + shareContent + shareWebPage);
            shareAction.share();
        }
        return 1;
    }

    private void tcEvent(int sharePosition, int i) {
        Map<String, String> map = new HashMap<>();
        if (i == 3) {
            map.put(AnalyDataValue.KEY_LIVE_ID, liveId + "");
        }
        switch (sharePosition) {
            case 0:
                map.put(AnalyDataValue.KEY_TYPE, AnalyDataValue.WEIXIN);
                if (i == 1) {
                    TCEventHelper.onEvent(getApplicationContext(), AnalyDataValue.LIVE_READY_SHARE_CLICK, AnalyDataValue.WEIXIN);
                } else if (i == 2) {
                    TCEventHelper.onEvent(getApplicationContext(), AnalyDataValue.LIVE_FINISH_SHARE_TYPE_CLICK, AnalyDataValue.WEIXIN);
                } else {
                    TCEventHelper.onEvent(getApplicationContext(), AnalyDataValue.LIVE_DETAIL_SHARE_TYPE_CLICK, map);
                }

                break;
            case 1:
                map.put(AnalyDataValue.KEY_TYPE, AnalyDataValue.WEIXIN_CICRLE);
                if (i == 1) {
                    TCEventHelper.onEvent(getApplicationContext(), AnalyDataValue.LIVE_READY_SHARE_CLICK, AnalyDataValue.WEIXIN_CICRLE);
                } else if (i == 2) {
                    TCEventHelper.onEvent(getApplicationContext(), AnalyDataValue.LIVE_FINISH_SHARE_TYPE_CLICK, AnalyDataValue.WEIXIN_CICRLE);
                } else {
                    TCEventHelper.onEvent(getApplicationContext(), AnalyDataValue.LIVE_DETAIL_SHARE_TYPE_CLICK, map);
                }

                break;
            case 2:
                map.put(AnalyDataValue.KEY_TYPE, AnalyDataValue.QQ);
                if (i == 1) {
                    TCEventHelper.onEvent(getApplicationContext(), AnalyDataValue.LIVE_READY_SHARE_CLICK, AnalyDataValue.QQ);
                } else if (i == 2) {
                    TCEventHelper.onEvent(getApplicationContext(), AnalyDataValue.LIVE_FINISH_SHARE_TYPE_CLICK, AnalyDataValue.QQ);
                } else {
                    TCEventHelper.onEvent(getApplicationContext(), AnalyDataValue.LIVE_DETAIL_SHARE_TYPE_CLICK, map);
                }

                break;
            case 3:
                map.put(AnalyDataValue.KEY_TYPE, AnalyDataValue.WEIBO);
                if (i == 1) {
                    TCEventHelper.onEvent(getApplicationContext(), AnalyDataValue.LIVE_READY_SHARE_CLICK, AnalyDataValue.WEIBO);
                } else if (i == 2) {
                    TCEventHelper.onEvent(getApplicationContext(), AnalyDataValue.LIVE_FINISH_SHARE_TYPE_CLICK, AnalyDataValue.WEIBO);
                } else {
                    TCEventHelper.onEvent(getApplicationContext(), AnalyDataValue.LIVE_DETAIL_SHARE_TYPE_CLICK, map);
                }

                break;
        }
    }

    private boolean shareFlagOther = false;

    UMShareListener umShareListener = new UMShareListener() {

        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {
            if (shareFlagOther) {
                shareFlagOther = true;
                shareWeiboAction();
                if (behindStartLiveShareIndex != 3) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
            }else{
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            if (shareFlagOther) {
                shareFlagOther = true;
                shareWeiboAction();
                if (behindStartLiveShareIndex != 3) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
            }else{
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            if (shareFlagOther) {
                shareFlagOther = true;
                shareWeiboAction();
                if (behindStartLiveShareIndex != 3) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
            }else{
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        }
    };

    /**
     * 分享
     * @param v
     */
    public void share(View v) {
        ShareUtils.showShareBoard(this, "[直播] 来我直播间一起high！", SPUtils.getNickName(this) +
                "  正在鹰和鹰直播，快来围观！", getLiveShareUrl(), SPUtils.getUserIcon(this));
    }

    private void shareWeiboAction() {
        if (behindStartLiveShareIndex != 3) {
            return;
        }
        String title = "[直播] 来我直播间一起high！";
        String content = SPUtils.getNickName(this) + "  正在鹰和鹰直播，快来围观！";
        tcEvent(3, 1);
        jumpToShare(title, content, 3);
    }

    private String getLiveShareUrl() {
        return SPUtils.getShareDefaultUrl(this, SysConfigType.URL_LIVE_SHARE_LINK) + batchID;
    }


    /*************************************************************       Handler Callback      ******************************************************************/

    private int reCreateLiveTime = 0;
    private final int MSG_WHAT_DELAY_TASK = 0x11118;
    private final int MSG_WHAT_RE_CREATE_LIVE= 0x11119;

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case HandlerConstant.NEED_STOP_PUSH:
                if (mPublisher != null){
                    mPublisher.stopRtmp();
                }
                break;
            case LiveController.MSG_GET_LIVE_RECORD_OK:
                // TODO: 16/10/8 恢复现场
                Api_SNSCENTER_SnsLiveRecordResult liveRecordResult = (Api_SNSCENTER_SnsLiveRecordResult) msg.obj;
                if (liveRecordResult == null){
                    mHandler.removeMessages(MSG_WHAT_DELAY_TASK);
                    mHandler.sendEmptyMessageDelayed(MSG_WHAT_DELAY_TASK, 10 * 1000);
                    return;
                }
                Log.e("EZEZ","得到直播状态1 ＝ "+liveRecordResult.liveStatus);
                Log.e("EZEZ","得到直播状态2 ＝ "+liveRecordResult.status);
                if (!LiveTypeConstants.LIVE_ING.equals(liveRecordResult.liveStatus) ||
                        !LiveTypeConstants.NORMAL_LIVE.equals(liveRecordResult.status)) {

                    //stop push , send msg create live
                    if (reCreateLiveTime == 0) {
                        Log.e("EZEZ"," fa xian duan le ,fa songxiaoxi ");
                        mPublisher.stopRtmp();
                        mHandler.sendEmptyMessageDelayed(MSG_WHAT_RE_CREATE_LIVE, 1000);
                    }

                    Log.e("EZEZ","reCreateLiveTime ＝ "+reCreateLiveTime);

                } else {
                    mHandler.removeMessages(MSG_WHAT_DELAY_TASK);
                    mHandler.sendEmptyMessageDelayed(MSG_WHAT_DELAY_TASK, 10 * 1000);
                }
                break;
            case LiveController.MSG_GET_LIVE_RECORD_ERROR:
                mHandler.removeMessages(MSG_WHAT_DELAY_TASK);
                mHandler.sendEmptyMessageDelayed(MSG_WHAT_DELAY_TASK, 10 * 1000);
                break;
            case MSG_WHAT_DELAY_TASK:
                // TODO: 16/10/8 定时任务, 查询推流状态
                fetchLiveState();
                break;
            case MSG_WHAT_RE_CREATE_LIVE:
                //TODO 定时任务，重新链接直播
                reCreateLiveTime ++ ;
                if (reCreateLiveTime <= 5) {
                    Log.e("EZEZ","di "+ reCreateLiveTime +"ci kaishi create live ");
                    createLive();
                }else {
                    ToastUtil.showToast(this, "您的网络貌似已中断！");
                    finish();
                }
                break;
            case LiveController.MSG_CLOSE_LIVE_OK:
                mHandler.removeMessages(MSG_WHAT_DELAY_TASK);
                mPublisher.stopRtmpPublish();
                upLoadProgressListener.stop();
                if (msg.obj == null) {

                } else {
                    Api_SNSCENTER_SnsCloseLiveResult result = (Api_SNSCENTER_SnsCloseLiveResult) msg.obj;
                    if (result.status) {
                        isStartPush = false;
                        if (mConnector != null) {
                            mConnector.disconnect(this);
                            isConnector = false;
                        }
                        //显示直播结束页面
                        showCloseView(result.viewCount);
                    } else {
                        ToastUtil.showToast(this, "close live error");
                    }
                }
                break;
            case LiveController.MSG_LIVE_CREATE_ERROR:
                // TODO: 16/10/8 定时任务, recreate live
                Log.e("EZEZ","qingqiu create live error");
                mHandler.removeMessages(MSG_WHAT_RE_CREATE_LIVE);
                mHandler.sendEmptyMessageDelayed(MSG_WHAT_RE_CREATE_LIVE,5 * 1000);
                break;
            case LiveController.MSG_LIVE_CREATE_OK:
                if (msg.obj == null) {
                    ToastUtil.showToast(this, "create live error");
                    finish();
                } else {
                    Api_SNSCENTER_SnsCreateLiveResult result = (Api_SNSCENTER_SnsCreateLiveResult) msg.obj;
                    liveId = result.liveId;

                    if (TextUtils.isEmpty(result.pushStreamUrl)) {
                        ToastUtil.showToast(this, "推流地址错误");
                        finish();
                        return;
                    }

                    if (reCreateLiveTime == 0) {

                        isStartPush = true;
                        //记录开始直播的时间
                        startLiveTime = System.currentTimeMillis();

                        //链接IM,添加直播间公告
                        initService();

                        setOnlineCount(result.onlineCount);

                        rv_chat.setVisibility(View.VISIBLE);

                        if (info != null){
                            info.batchId = result.batchId;
                        }

                        if (null != result.liveConfig && result.liveConfig.length() > 0) {
                            v_im_adapter.addAffiche(result.liveConfig);
                        }

                        if (info != null){
                            info.batchId = result.batchId;
                            batchID = result.batchId;
                        }

                        //如果选择了开播分享,需要跳转分享
                        String title = "[直播] 来我直播间一起high！";
                        String content = SPUtils.getNickName(this) + "  正在鹰和鹰直播，快来围观！";
                        tcEvent(behindStartLiveShareIndex, 1);
                        int code = jumpToShare(title, content, behindStartLiveShareIndex);
                        if (code <= 0) {
                            //只有微博分享
                            shareFlagOther = true;
                            shareWeiboAction();
                        }
                    }else {
                        reCreateLiveTime = 0;
                        mHandler.removeMessages(MSG_WHAT_RE_CREATE_LIVE);
                    }
                    mPublisher.startRtmpPublish(result.pushStreamUrl);
                    mHandler.sendEmptyMessageDelayed(MSG_WHAT_DELAY_TASK, 10 * 1000);
                    break;
                }
        }
    }

    /**
     * 显示结束直播画面
     * @param viewCount
     */
    private void showCloseView(int viewCount) {
        //((TextView) findViewById(R.id.ac_push_stream_person_count)).setText(String.valueOf(result.viewCount));
        mPublisher.stopRtmpPublish();
        mPreviewSurface.setVisibility(View.GONE);
        findViewById(R.id.rl_live_finish).setVisibility(View.VISIBLE);
        findViewById(R.id.ac_push_stream_close).setOnClickListener(this);
        findViewById(R.id.ac_play_back_share_wx).setOnClickListener(this);
        findViewById(R.id.ac_play_back_share_wx_friends).setOnClickListener(this);
        findViewById(R.id.ac_play_back_share_qq).setOnClickListener(this);
        findViewById(R.id.ac_play_back_share_wb).setOnClickListener(this);
        TextView tv_view_count = findViewById(R.id.tv_live_view_count);
        tv_view_count.setText(viewCount+"");
        //计算直播时间是否小于三分钟
        if ((System.currentTimeMillis() - startLiveTime) >= 1000 * 60 * 3){
            findViewById(R.id.rl_live_finish_share).setVisibility(View.VISIBLE);
        }else {
            findViewById(R.id.tv_live_time_lower_3).setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.stream_push_pop_share_parent_viewgroup:
                if (mPopView != null && mPopView.isShowing()) {
                    mPopView.dismiss();
                }
                break;
            case R.id.ib_back:
                finish();
                break;
            case R.id.ac_push_stream_close:
                finish();
                break;
            case R.id.swCam:
                mPublisher.switchCamera();
                break;
            case R.id.ib_flash_light:

                if (mPublisher != null) {
                    if (openFlash) {
                        ((ImageView) findViewById(R.id.ib_flash_light)).setImageResource(R.mipmap.ic_live_flash_mode_open);
                        mPublisher.turnOnFlash(true);
                    } else {
                        ((ImageView) findViewById(R.id.ib_flash_light)).setImageResource(R.mipmap.ic_live_flash_mode_close);
                        mPublisher.turnOnFlash(false);
                    }
                    openFlash = !openFlash;
                }
                break;
            case R.id.ib_close_chat_list://折叠聊天框
                if (isFolded) {
                    ViewGroup.LayoutParams params = rv_chat.getLayoutParams();
                    params.height = (int) getResources().getDimension(R.dimen.dd_dimen_440px);
                    ((ImageView) findViewById(R.id.ib_close_chat_list)).setImageResource(R.mipmap.ic_live_push_im_open);
                    rv_chat.setLayoutParams(params);
                }else {
                    ViewGroup.LayoutParams params = rv_chat.getLayoutParams();
                    params.height = (int) getResources().getDimension(R.dimen.dd_dimen_100px);
                    ((ImageView) findViewById(R.id.ib_close_chat_list)).setImageResource(R.mipmap.ic_live_push_im_close);
                    rv_chat.setLayoutParams(params);
                }
                isFolded = !isFolded;
                break;
            case R.id.ib_share:
                share(v);
                break;
            case R.id.iv_finish_live:
                //关闭直播
                if (isStartPush) {
                    if (closeLiveDialog != null && closeLiveDialog.isShowing()) {
                        closeLiveDialog.dismiss();
                        closeLiveDialog = null;
                    } else {
                        if (userService.isLogin()){
                            showClosePushDialog();
                        }else {
                            finish();
                        }

                    }
                }else {
                    finish();
                }
                break;
            case R.id.publish:
                //初始化顶部layout
                initTopBarView(true);
                judgeTheNetworkType();

                break;
            case R.id.ac_play_back_share_wx:
                tcEvent(0, 2);
                jumpToShare("[直播]" + title, SPUtils.getNickName(this) + "  正在鹰和鹰直播，快来围观！", 0);
                SPUtils.save(this, SPUtils.EXTRA_LIVE_SHARE_POSITION, 0);
                break;
            case R.id.ac_play_back_share_wx_friends:
                tcEvent(1, 2);
                jumpToShare("[直播]" + title, SPUtils.getNickName(this) + "  正在鹰和鹰直播，快来围观！", 1);
                SPUtils.save(this, SPUtils.EXTRA_LIVE_SHARE_POSITION, 1);
                break;
            case R.id.ac_play_back_share_qq:
                tcEvent(2, 2);
                jumpToShare("[直播]" + title, SPUtils.getNickName(this) + "  正在鹰和鹰直播，快来围观！", 2);
                SPUtils.save(this, SPUtils.EXTRA_LIVE_SHARE_POSITION, 2);
                break;
            case R.id.ac_play_back_share_wb:
                tcEvent(3, 2);
                jumpToShare("[直播]" + title, SPUtils.getNickName(this) + "  正在鹰和鹰直播，快来围观！", 3);
                SPUtils.save(this, SPUtils.EXTRA_LIVE_SHARE_POSITION, 3);
                break;

        }
    }

    /**
     * 申请权限
     */
    private void requestPermissions() {
        if (Build.VERSION.SDK_INT>22){
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this,
                    Manifest.permission.RECORD_AUDIO)!= PackageManager.PERMISSION_GRANTED){
                //先判断有没有权限 ，没有就在这里进行权限的申请
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO}, 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                    //这里已经获取到了摄像头的权限，想干嘛干嘛了可以
                    judgeTheNetworkType();
                }else {
                    //这里是拒绝给APP摄像头权限，给个提示什么的说明一下都可以。
                    if (mPermissionDialog == null) {
                        mPermissionDialog = DialogUtil.showMessageDialog(this, null,
                                "打开摄像头失败！请在\"设置\"中确认是否授权使用摄像头", "确认", null,
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        finish();
                                        mPermissionDialog.dismiss();
                                    }
                                }, null);
                    }
                    mPermissionDialog.show();
                }
                break;
            default:
                break;
        }
    }



    /**
     * 设置在线人数
     * @param onlineCount
     */
    private void setOnlineCount(int onlineCount) {
        TextView tv_online = findViewById(R.id.tv_online);
        tv_online.setVisibility(View.VISIBLE);
        tv_online.setText(onlineCount+"在线");
    }

    /**
     * 结束直播提示dialog
     */
    Dialog closeLiveDialog;
    private void showClosePushDialog() {
        closeLiveDialog = DialogUtil.showMessageDialog(this, "您确定要结束直播么?", "", "继续直播", "结束直播", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (closeLiveDialog != null && closeLiveDialog.isShowing()) {
                    closeLiveDialog.dismiss();
                }
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mVideoRootView.stopPusher();
                if (isKickout){
                    finish();
                    return;
                }
                mLiveController.closeLive(NewPushStreamVerticalActivity.this, mHandler, liveId);
                if (closeLiveDialog != null && closeLiveDialog.isShowing()) {
                    closeLiveDialog.dismiss();
                }
            }
        });
        closeLiveDialog.show();
    }

    @Override
    public void onBackPressed() {
        if (isStartPush) {
            if (closeLiveDialog != null && closeLiveDialog.isShowing()) {
                closeLiveDialog.dismiss();
                closeLiveDialog = null;
            } else {
                if (userService.isLogin()){
                    showClosePushDialog();
                }else {
                    finish();
                }

            }
        } else {
            super.onBackPressed();
        }
    }

    private Dialog mNoNetworkDialog;
    private Dialog mNotWifiDialog;
    /**
     * 判断当前网络类型
     */
    private void judgeTheNetworkType() {
        switch (NetWorkUtil.getNetworkState(this)) {
            case NetWorkUtil.NETWORK_NONE:
                if (mNoNetworkDialog == null) {
                    mNoNetworkDialog = DialogUtil.showMessageDialog(this, null,
                            "当前没有网络，请检查后再试", "确认", "", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mNoNetworkDialog.dismiss();
                                }
                            }, null);
                }
                mNoNetworkDialog.show();
                break;
            case NetWorkUtil.NETWORK_2G:
            case NetWorkUtil.NETWORK_3G:
            case NetWorkUtil.NETWORK_4G:
            case NetWorkUtil.NETWORK_MOBILE:
                if (mNotWifiDialog == null) {
                    mNotWifiDialog = DialogUtil.showMessageDialog(this, "提示",
                            "您当前没有使用WIFI，是否继续？", "确认", "取消", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mNotWifiDialog.dismiss();
                                    createLive();
                                }
                            }, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mNotWifiDialog.dismiss();
                                }
                            });
                }
                mNotWifiDialog.show();
                break;
            case NetWorkUtil.NETWORK_WIFI:
                createLive();
                break;
        }
    }

    /**
     * 被挤下线
     */
    public void onKickout() {
        isKickout = true;
        if (mPublisher != null){
            mPublisher.stopRtmpPublish();
        }
        if (isConnector) {
            mConnector.disconnect(this);
            isConnector = false;
        }
        mHandler.removeMessages(MSG_WHAT_DELAY_TASK);
    }

    /**
     * 主播发送离开消息
     * @param msgNotifyType
     */
    private void sendBackLeaveMsg(String msgNotifyType) {
        OtherMsgParam param = new OtherMsgParam();
        param.liveId = liveId;
        param.msgNotifyType = msgNotifyType;
//        param.nickName = SPUtils.getNickName(this);
        LiveController.getInstance().sendOtherMsg(this, mHandler, param);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isStartPush) {
            sendBackLeaveMsg("COME_BACK");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isStartPush) {
            sendBackLeaveMsg("TAKE_A_BREAK");
        }
    }
}
