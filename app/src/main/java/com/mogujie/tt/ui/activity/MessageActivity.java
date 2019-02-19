
package com.mogujie.tt.ui.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.harwkin.nb.camera.ImageUtils;
import com.harwkin.nb.camera.callback.SelectMoreListener;
import com.harwkin.nb.camera.options.CameraOptions;
import com.harwkin.nb.camera.pop.CameraPop;
import com.harwkin.nb.camera.pop.CameraPopListener;
import com.harwkin.nb.camera.type.OpenType;
import com.mogujie.tt.DB.sp.SystemConfigSp;
import com.mogujie.tt.imservice.event.ConsultEvent;
import com.mogujie.tt.imservice.event.MessageEvent;
import com.mogujie.tt.imservice.event.PriorityEvent;
import com.mogujie.tt.imservice.event.SessionEvent;
import com.mogujie.tt.imservice.event.SwitchP2PEvent;
import com.mogujie.tt.imservice.event.UserInfoEvent;
import com.mogujie.tt.imservice.manager.IMLoginManager;
import com.mogujie.tt.imservice.service.IMService;
import com.mogujie.tt.imservice.support.IMServiceConnector;
import com.mogujie.tt.protobuf.helper.EntityChangeEngine;
import com.mogujie.tt.ui.adapter.MessageAdapter;
import com.mogujie.tt.ui.helper.AudioPlayerHandler;
import com.mogujie.tt.ui.helper.AudioRecordHandler;
import com.mogujie.tt.ui.widget.CustomEditView;
import com.mogujie.tt.ui.widget.IMGoodsLinkView;
import com.mogujie.tt.ui.widget.MGProgressbar;
import com.mogujie.tt.utils.Logger;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.util.StringUtil;
import com.quanyan.base.view.BaseNavView;
import com.quanyan.yhy.BaseApplication;

import com.quanyan.yhy.R;
import com.quanyan.yhy.common.AnalyDataValue;
import com.quanyan.yhy.eventbus.EvBusImage;
import com.quanyan.yhy.ui.base.utils.DialogUtil;
import com.quanyan.yhy.ui.base.utils.TCEventHelper;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.consult.ConsultUserReplyDialogActivity;
import com.quanyan.yhy.ui.consult.controller.ConsultController;
import com.quanyan.yhy.ui.consult.view.MessageConsultBottomView;
import com.quanyan.yhy.ui.consult.view.MessageConsultComment;
import com.quanyan.yhy.ui.consult.view.MessageConsultTimerView;
import com.yhy.common.beans.album.MediaItem;
import com.yhy.common.beans.im.entity.AudioMessage;
import com.yhy.common.beans.im.entity.ConsultControlEntity;
import com.yhy.common.beans.im.entity.ImageMessage;
import com.yhy.common.beans.im.entity.MessageEntity;
import com.yhy.common.beans.im.entity.PeerEntity;
import com.yhy.common.beans.im.entity.ProductCardMessage;
import com.yhy.common.beans.im.entity.TextMessage;
import com.yhy.common.beans.im.entity.UnreadEntity;
import com.yhy.common.beans.im.entity.UserEntity;
import com.yhy.common.beans.net.model.ProductCardModel;
import com.yhy.common.beans.net.model.tm.AcceptProcessOrderResult;
import com.yhy.common.beans.net.model.tm.CancelProcessResult;
import com.yhy.common.beans.net.model.tm.ConsultState;
import com.yhy.common.beans.net.model.tm.FinishProcessResult;
import com.yhy.common.beans.net.model.tm.ProcessState;
import com.yhy.common.beans.net.model.trip.LineDetail;
import com.yhy.common.beans.net.model.trip.ShortItem;
import com.yhy.common.constants.ConsultContants;
import com.yhy.common.constants.DBConstant;
import com.yhy.common.constants.HandlerConstant;
import com.yhy.common.constants.IntentConstant;
import com.yhy.common.constants.MessageConstant;
import com.yhy.common.constants.SysConstant;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.AndroidUtils;
import com.yhy.common.utils.CommonUtil;
import com.yhy.common.utils.FileUtil;
import com.yhy.common.utils.SPUtils;
import com.yhy.service.IUserService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import de.greenrobot.event.EventBus;

/**
 * @author Nana
 * @Description 主消息界面
 * @date 2014-7-15
 * <p>
 */
public class MessageActivity extends BaseActivity
        implements
        PullToRefreshBase.OnRefreshListener2<ListView>,
        View.OnClickListener,
        OnTouchListener,
        TextWatcher,
        SensorEventListener {

    private static Handler uiHandler = null;// 处理语音

    private CameraPop mCameraPop;
    private PullToRefreshListView lvPTR = null;
    private CustomEditView messageEdt = null;
    private TextView sendBtn = null;
    private Button recordAudioBtn = null;
    private ImageView keyboardInputImg = null;
    private ImageView keyboardInputImgEmo = null;
    private ImageView soundVolumeImg = null;


    private ImageView audioInputImg = null;
    private ImageView addPhotoBtn = null;
    //    private ImageView addEmoBtn = null;
    //    private LinearLayout emoLayout = null;
//    private EmoGridView emoGridView = null;
//    private YayaEmoGridView yayaEmoGridView = null;
//    private RadioGroup emoRadioGroup = null;
    private String audioSavePath = null;
    private InputMethodManager inputManager = null;
    private AudioRecordHandler audioRecorderInstance = null;
    private TextView textView_new_msg_tip = null;

    private MessageAdapter adapter = null;
    private Thread audioRecorderThread = null;
    private Dialog soundVolumeDialog = null;
//    private View soundVolumeLayout;
//        private View addOthersPanelView = null;

    MGProgressbar progressbar = null;

    //private boolean audioReday = false; 语音先关的
    private SensorManager sensorManager = null;
    private Sensor sensor = null;


    private String takePhotoSavePath = "";
    private Logger logger = Logger.getLogger(MessageActivity.class);
    private IMService imService;
    private UserEntity loginUser;
    private PeerEntity peerEntity = new PeerEntity();

    // 当前的session
    private String currentSessionKey;
    private int historyTimes = 0;

    //键盘布局相关参数
    int rootBottom = Integer.MIN_VALUE, keyboardHeight = 0;
    switchInputMethodReceiver receiver;
    private String currentInputMethod;
    private IMGoodsLinkView goodsLinkView;
    private ProductCardModel goodsLinkEntity;
    private View layoutOrderId;
    private View btnSendOrderId;
    private long orderId = 0;
    //商品详情
    private LineDetail mLineDetail;
    private InputMode inputMode = InputMode.DEFAULT;
    private MessageConsultTimerView messageConsultTimerView;
    /**
     * 全局Toast
     */
    private Toast mToast;
    private int peerType;
    private long peerId;
    private long serviceId;
    //评论视图
    private View mViewBottom;
    private MessageConsultComment mConsultComment;
    private MessageConsultBottomView mConsultBottom;
    private TextView mQueue;
    ConsultController consultController;
    boolean isNeedShowComment = true;
    private BroadcastReceiver mNetBroadCast;//监听网络变化
    private boolean isNeedFinishAcWhenComments = false;

    @Autowired
    IUserService userService;
    enum InputMode {
        AUDIO, DEFAULT, EMO;
    }

    public void showToast(int resId) {
        String text = getResources().getString(resId);
        if (mToast == null) {
            mToast = Toast.makeText(MessageActivity.this, text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.show();
    }

    public void cancelToast() {
        if (mToast != null) {
            mToast.cancel();
        }
    }

    @Override
    public void onBackPressed() {
        BaseApplication.gifRunning = false;
        cancelToast();
        super.onBackPressed();
    }

    /**
     * end 全局Toast
     */
    private IMServiceConnector imServiceConnector = new IMServiceConnector() {
        @Override
        public void onIMServiceConnected() {
            logger.d("message_activity#onIMServiceConnected");
            imService = imServiceConnector.getIMService();
            if (imService == null) {
                ToastUtil.showToast(MessageActivity.this, "获取服务失败");
                finish();
                return;
            }
            if (!EventBus.getDefault().isRegistered(MessageActivity.this)) {
                EventBus.getDefault().register(MessageActivity.this, SysConstant.MESSAGE_EVENTBUS_PRIORITY);
            }
            initData();
        }

        @Override
        public void onServiceDisconnected() {
            if (EventBus.getDefault().isRegistered(MessageActivity.this)) {
                EventBus.getDefault().unregister(MessageActivity.this);
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        logger.d("message_activity#onCreate:%s", this);
        currentSessionKey = getIntent().getStringExtra(IntentConstant.KEY_SESSION_KEY);
        mLineDetail = (LineDetail) getIntent().getSerializableExtra(SPUtils.EXTRA_DATA);
        if (mLineDetail != null && mLineDetail.userInfo != null) {
            currentSessionKey = EntityChangeEngine.getSessionKey((int) mLineDetail.userInfo.userId, DBConstant.SESSION_TYPE_SINGLE, serviceId);
        }
        registerNetBroadCast();
        initSoftInputMethod();
        initEmo();
        initAudioHandler();
        initAudioSensor();
        super.onCreate(savedInstanceState);
    }

    private void registerNetBroadCast() {
        if (mNetBroadCast == null) {
            mNetBroadCast = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
                        if (!onNetStateChanged) {
                            onNetStateChanged = true;
                            return;
                        }
                        if (AndroidUtils.isNetWorkAvalible(MessageActivity.this)) {
                            if (peerType == DBConstant.SESSION_TYPE_CONSULT) {
                                getConsultInfo();
                            }
                        }
                    }
                }
            };
            IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver(mNetBroadCast, intentFilter);
        }
    }

    private void unRegisterNetBroadCast() {
        if (mNetBroadCast != null) {
            unregisterReceiver(mNetBroadCast);
            mNetBroadCast = null;
        }
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.tt_activity_message, null);
    }

    private BaseNavView mBaseNavView;

    @Override
    public View onLoadNavView() {
        mBaseNavView = new BaseNavView(this);
        return mBaseNavView;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        initView();
        imServiceConnector.connect(this);
        logger.d("message_activity#register im service and eventBus");
    }


    // 触发条件,imservice链接成功，或者newIntent
    private void initData() {
        isNeedShowComment = true;
        isNeedFinishAcWhenComments = false;
        processState = null;
        if (consultController == null) consultController = new ConsultController(this, mHandler);
        String[] sessionInfo = EntityChangeEngine.spiltSessionKey(currentSessionKey);
        peerType = Integer.parseInt(sessionInfo[0]);
        peerId = Long.parseLong(sessionInfo[1]);
        serviceId = Long.parseLong(sessionInfo[2]);
        historyTimes = 0;
        adapter.clearItem();
//        adapter.clearImageMessageList();
        initUserEntity();
        // 头像、历史消息加载、取消通知
        setTitleByUser();
        reqHistoryMsg();
        adapter.setImService(imService, loginUser);
        imService.getUnReadMsgManager().readUnreadSession(currentSessionKey);
        imService.getNotificationManager().cancelSessionNotifications(String.valueOf(peerType));
        refreshConsult();
        reqPeerContactInfo();
        if (peerType == DBConstant.SESSION_TYPE_CONSULT) {
            requestConsultItems();
            getConsultInfo();
        }
    }

    public void getConsultInfo() {
        imService.getConsultManager().getProcessState(loginUser.getPeerId(), peerId, serviceId);
    }

    private void requestConsultItems() {
        List<Long> lists = new ArrayList<>();
        lists.add(serviceId);
        imService.getConsultManager().getItemListByItemIds(lists);
    }

    private MessageConsultTimerView.OnTimeOverListner onTimeOverListner = new MessageConsultTimerView.OnTimeOverListner() {
        @Override
        public void onFinish() {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (MessageActivity.this == null || isFinishing()) {
                        return;
                    }
                    if (processState != null && processState.processOrder != null && ("CONSULT_IN_CHAT").equals(processState.processOrder.processOrderStatus)) {
                        getConsultInfo();
                    }
                }
            }, 5000);
        }
    };

    // 服务流程状态 NOT_AVAILABLE:未生效 WAITING_PAY:待付款 CANCEL:取消 FINISH:结束 RATED:已评价 CONSULT_IN_QUEUE:排队中 CONSULT_IN_CHAT:咨询中
    private synchronized void refreshConsult() {
        mBaseNavView.setRightText(null);
        mBaseNavView.setRightTextClick(null);
        if (cancelConsultDialog != null && cancelConsultDialog.isShowing())
            cancelConsultDialog.dismiss();
        if (peerType == DBConstant.SESSION_TYPE_SINGLE) {
            mViewBottom.setVisibility(View.VISIBLE);
            messageConsultTimerView.setVisibility(View.GONE);
            mConsultComment.setVisibility(View.GONE);
            mQueue.setVisibility(View.GONE);
            mConsultBottom.setVisibility(View.GONE);
        } else if (peerType == DBConstant.SESSION_TYPE_CONSULT) {
//            messageConsultTimerView.setOnTimeOverListner(onTimeOverListner);
            if (processState == null) {
                mViewBottom.setVisibility(View.GONE);
                messageConsultTimerView.setVisibility(View.GONE);
                mConsultComment.setVisibility(View.GONE);
                mQueue.setVisibility(View.GONE);
                mConsultBottom.setVisibility(View.GONE);
            } else {
                String processStatus = processState.processOrder.processOrderStatus;
                boolean isSeller = isSeller();

                if (isSeller) {
                    if (processState.consultInfo.sellerQueueLength > 0) {
                        mQueue.setText(getString(R.string.consult_queue_tip, processState.consultInfo.sellerQueueLength));
                        mQueue.setVisibility(View.VISIBLE);
                    } else {
                        mQueue.setVisibility(View.GONE);
                    }
                    if (processStatus.equals("CONSULT_IN_CHAT")) {
                        messageConsultTimerView.setVisibility(View.VISIBLE);
                        messageConsultTimerView.setData(processState.consultInfo);
                        mViewBottom.setVisibility(View.VISIBLE);
                        mConsultComment.setVisibility(View.GONE);
                    } else if (processStatus.equals("FINISH") || processStatus.equals("RATED")) {
                        messageConsultTimerView.setVisibility(View.GONE);
                        mViewBottom.setVisibility(View.GONE);
                        mConsultComment.setVisibility(View.GONE);
                        mCameraPop.dismiss();
                    } else if (processStatus.equals("CONSULT_IN_QUEUE")) {
                        messageConsultTimerView.setVisibility(View.GONE);
                        mViewBottom.setVisibility(View.GONE);
                        mConsultComment.setVisibility(View.GONE);
                    }
                } else {
                    mQueue.setVisibility(View.GONE);
                    if (processStatus.equals("CONSULT_IN_CHAT")) {
                        messageConsultTimerView.setVisibility(View.VISIBLE);
                        messageConsultTimerView.setData(processState.consultInfo);
                        mViewBottom.setVisibility(View.VISIBLE);
                        mConsultComment.setVisibility(View.GONE);
                        mBaseNavView.setRightText(getString(R.string.finish_consult));
                        mBaseNavView.setRightTextClick(finishConsultListener);
                    } else if (processStatus.equals("CONSULT_IN_QUEUE")) {
                        messageConsultTimerView.setVisibility(View.GONE);
                        mViewBottom.setVisibility(View.GONE);
                        mConsultComment.setVisibility(View.GONE);
                        mBaseNavView.setRightText(getString(R.string.cancel_consult));
                        mBaseNavView.setRightTextClick(cancelConsultListener);
                    } else if (processStatus.equals("FINISH") || processStatus.equals("RATED")) {
                        messageConsultTimerView.setVisibility(View.GONE);
                        mViewBottom.setVisibility(View.GONE);
                        mConsultComment.setVisibility(View.GONE);
                        mCameraPop.dismiss();
                    }
                }
                Map<Long, ShortItem> map = imService.getConsultManager().getItemMap();
                ShortItem item = map.get(serviceId);
                mConsultBottom.setData(processState, item, isNeedShowComment, isSeller);
            }

//       imService.getConsultManager().get
        }
    }

    Dialog cancelConsultDialog;
    private View.OnClickListener cancelConsultListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            cancelConsultDialog = DialogUtil.showMessageDialog(MessageActivity.this, null, getString(R.string.cancel_consult_dialog_message), getString(R.string.keep_waitting), getString(R.string.confirm_cancel), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancelConsultDialog.dismiss();
                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancelConsultDialog.dismiss();
                    showLoadingView(null);
                    consultController.cancelProcessOrder(MessageActivity.this, processState.processOrder.processOrderId);
                }
            });
            cancelConsultDialog.show();
        }
    };

    Dialog finishConsultDialog;
    private View.OnClickListener finishConsultListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finishConsultDialog = DialogUtil.showMessageDialog(MessageActivity.this, null, getString(R.string.finish_consult_dialog_message), getString(R.string.label_btn_ok), getString(R.string.label_btn_cancel), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finishConsultDialog.dismiss();
                    showLoadingView(null);
                    consultController.finishConsult(MessageActivity.this, processState.processOrder.itemId, processState.processOrder.processOrderId);
                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finishConsultDialog.dismiss();
                }
            });
            finishConsultDialog.show();
        }
    };

    /**
     * 是否为卖家
     */
    private boolean isSeller() {
        if (processState == null || loginUser == null) return false;
        if (processState.processOrder.consultUserInfo.userId == loginUser.getPeerId()) {
            return true;
        }
        return false;
    }

    private boolean isBuyer() {
        if (processState == null || loginUser == null) return false;
        if (processState.processOrder.buyerInfo.userId == loginUser.getPeerId()) {
            return true;
        }
        return false;
    }


    private void reqPeerContactInfo() {
        List<Long> ids = new ArrayList<>();
        ids.add(peerEntity.getPeerId());
        imService.getContactManager().reqUserInfoById(ids);
    }

    private void initUserEntity() {
        loginUser = imService.getLoginManager().getLoginInfo();
        if (loginUser == null) {
            loginUser = new UserEntity();
            int timeNow = (int) (System.currentTimeMillis() / 1000);
            String nickname = SPUtils.getNickName(this);
            loginUser.setAvatar(ImageUtils.getImageFullUrl(SPUtils.getUserIcon(this)));
            loginUser.setCreated(timeNow);
            loginUser.setPinyinName(nickname);
            loginUser.setGender(1);
            loginUser.setMainName(nickname);
            loginUser.setUpdated(timeNow);
            loginUser.setPeerId(userService.getLoginUserId());
            loginUser.setOptions((int) SPUtils.getRoleType(this));
            loginUser.setIsVip(SPUtils.isVip(this));
        }
        peerEntity = imService.getSessionManager().findPeerEntity(currentSessionKey);
        if (peerEntity == null) {
            peerEntity = new PeerEntity();
            peerEntity.setPeerId(peerId);
            peerEntity.setMainName("未知");
        }
    }

    private void initSoftInputMethod() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        receiver = new switchInputMethodReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.INPUT_METHOD_CHANGED");
        registerReceiver(receiver, filter);

        SystemConfigSp.instance().init(this);
        currentInputMethod = Settings.Secure.getString(MessageActivity.this.getContentResolver(), Settings.Secure.DEFAULT_INPUT_METHOD);
        keyboardHeight = SystemConfigSp.instance().getIntConfig(currentInputMethod);
    }

    /**
     * 本身位于Message页面，点击通知栏其他session的消息
     */
    @Override
    protected void onNewIntent(Intent intent) {
        logger.d("message_activity#onNewIntent:%s", this);
        super.onNewIntent(intent);
        setIntent(intent);
        stopSwitchP2PTask();
        historyTimes = 0;
        retryTimes = 0;
        if (intent == null) {
            return;
        }
        String newSessionKey = getIntent().getStringExtra(IntentConstant.KEY_SESSION_KEY);
        if (newSessionKey == null) {
            return;
        }
        logger.d("chat#newSessionInfo:%s", newSessionKey);
        if (!newSessionKey.equals(currentSessionKey) || peerType == DBConstant.SESSION_TYPE_CONSULT) {
            currentSessionKey = newSessionKey;
            if (imService == null) {
                imServiceConnector.connect(this);
            } else {
                initData();
            }
        }

        Serializable goodsEntity = getIntent().getSerializableExtra(IntentConstant.EXTRA_GOODS_LINK);
        goodsLinkView = (IMGoodsLinkView) findViewById(R.id.goods_link_view);
        if (goodsEntity != null) {
            goodsLinkEntity = (ProductCardModel) goodsEntity;
            goodsLinkView.setData(goodsLinkEntity);
            goodsLinkView.setOnSendListener(this);
            goodsLinkView.setVisibility(View.VISIBLE);
        } else {
            goodsLinkView.setVisibility(View.GONE);
        }

        orderId = getIntent().getLongExtra(IntentConstant.EXTRA_SEND_ORDER_ID, 0);
        if (orderId > 0) {
            layoutOrderId.setVisibility(View.VISIBLE);
        } else {
            layoutOrderId.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        logger.d("message_activity#onresume:%s", this);
        super.onResume();
        BaseApplication.gifRunning = true;
        historyTimes = 0;
        // not the first time
        if (imService != null && peerEntity != null) {
            // 处理session的未读信息
            handleUnreadMsgs();
        }
    }

    @Override
    protected void onDestroy() {
        logger.d("message_activity#onDestroy:%s", this);
        historyTimes = 0;
        stopSwitchP2PTask();
        if (loginUser != null && peerEntity != null) {
            imService.getSwitchServiceManager().sendP2PWritingMsg(loginUser.getPeerId(), peerEntity.getPeerId(), false, peerType, serviceId);
        }
        imServiceConnector.disconnect(this);
        adapter.clearItem();
        sensorManager.unregisterListener(this, sensor);
        saveImageLoadStatus();
        unRegisterNetBroadCast();
        unregisterReceiver(receiver);
        if (EventBus.getDefault().isRegistered(MessageActivity.this)) {
            EventBus.getDefault().unregister(MessageActivity.this);
        }
        super.onDestroy();
    }

    private void saveImageLoadStatus() {
        ArrayList<Object> msgObjectList = adapter.getMessageObjectList();
        List<MessageEntity> entitys = new ArrayList<>();
        for (int i = msgObjectList.size() - 1; i >= 0; --i) {
            Object item = msgObjectList.get(i);
            if (item instanceof ImageMessage) {
                ImageMessage image = (ImageMessage) item;
                if (image.getStatus() == MessageConstant.MSG_SUCCESS && image.getLoadStatus() == MessageConstant.IMAGE_LOADED_FAILURE) {
                    entitys.add(image);
                }
            }
        }
        if (entitys.isEmpty()) return;
        imService.getDbInterface().batchInsertOrUpdateMessage(entitys);
    }

    /**
     * 设定聊天名称
     * 1. 如果是user类型， 点击触发UserProfile
     * 2. 如果是群组，检测自己是不是还在群中
     */

    private void setTitleByUser() {
        if (peerEntity != null) {
            mBaseNavView.setTitleText(peerEntity.getMainName());
        }
//        mBaseNavView.setRightImg(R.mipmap.arrow_back_gray);
//        mBaseNavView.setRightImgClick(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                actFinish();
//            }
//        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (RESULT_OK != resultCode)
            return;
        mCameraPop.forResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void handleImagePickData(List<MediaItem> list) {
        ArrayList<ImageMessage> listMsg = new ArrayList<>();
        ArrayList<MediaItem> itemList = (ArrayList<MediaItem>) list;
        for (MediaItem item : itemList) {
            ImageMessage imageMessage = ImageMessage.buildForSend(item, loginUser, peerEntity, peerType, serviceId);
            listMsg.add(imageMessage);
            pushList(imageMessage);
        }
        imService.getMessageManager().sendImages(listMsg);
    }

    /**
     * 背景: 1.EventBus的cancelEventDelivery的只能在postThread中运行，而且没有办法绕过这一点
     * 2. onEvent(A a)  onEventMainThread(A a) 这个两个是没有办法共存的
     * 解决: 抽离出那些需要优先级的event，在onEvent通过handler调用主线程，
     * 然后cancelEventDelivery
     * <p>
     * todo  need find good solution
     */
    public void onEvent(PriorityEvent event) {
        switch (event.event) {
            case MSG_RECEIVED_MESSAGE: {
                MessageEntity entity = (MessageEntity) event.object;
                /**正式当前的会话*/
                if (currentSessionKey.equals(entity.getSessionKey())) {
                    Message message = Message.obtain();
                    message.what = HandlerConstant.MSG_RECEIVED_MESSAGE;
                    message.obj = entity;
                    uiHandler.sendMessage(message);
                    EventBus.getDefault().cancelEventDelivery(event);
                }
            }
            break;
        }
    }

    public void onEvent(EvBusImage event) {
        int pos = adapter.getMessageObjectList().size();
        if (adapter.getMessageObjectList().get(pos - 1) instanceof ImageMessage) {
            ImageMessage message = (ImageMessage) adapter.getMessageObjectList().get(pos - 1);
            if (message.getUrl().equals(event.path)) {
                scrollToBottomListItem();
            }
        }
    }

    public void onEventMainThread(UserInfoEvent event) {
        switch (event) {
            case USER_INFO_UPDATE:
                peerEntity = imService.getSessionManager().findPeerEntity(currentSessionKey);
                // 头像、历史消息加载、取消通知
                setTitleByUser();
        }
    }

    ProcessState processState;
    int retryTimes = 0;

    public void onEventMainThread(SessionEvent event) {
        if (event == SessionEvent.RESPONSE_SESSION_OK) {
            historyTimes = 0;
            adapter.clearItem();
            reqHistoryMsg();
        }
    }

    public void onEventMainThread(ConsultEvent event) {
        switch (event.event) {
            case PROCESS_STATE_UPDATE:
                try {
                    ProcessState state = (ProcessState) event.object;
                    if (state.processOrderItem.itemId == serviceId) {
                        if (state.processOrder.processOrderStatus.equals("WAITING_PAY") && state.processOrder.actualTotalFee == 0) {
                            reloadProcess();
                            return;
                        }
                        if (state.processOrder.buyerInfo.userId == loginUser.getPeerId() && "CONSULT_IN_QUEUE".equals(state.processOrder.processOrderStatus) && state.consultInfo.userQueuePosition == -1) {
                            reloadProcess();
                            return;
                        }
                        String processStatus = state.processOrder.processOrderStatus;
                        if (processStatus.equals("CONSULT_IN_CHAT")) {
                            isNeedShowComment = false;
                            isNeedFinishAcWhenComments = true;
                        }
                        hideLoadingView();
                        processState = state;
                        refreshConsult();
                    }
                } catch (Exception e) {
                    reloadProcess();
                }
                break;
            case PROCESS_STATE_UPDATE_KO:
                ToastUtil.showToast(this, StringUtil.handlerErrorCode(this, (Integer) event.object));
                break;
            case CONSULT_CONTROL:
                ConsultControlEntity controlEntity = (ConsultControlEntity) event.object;
                String tags = controlEntity.getTags();
                if (controlEntity.getItemId() != serviceId || peerType != DBConstant.SESSION_TYPE_CONSULT) {
                    if (tags.equals(ConsultContants.START)) {
                        ConsultUserReplyDialogActivity.gotoConsultUserReplyDialogActivity(this, controlEntity.getItemId(), controlEntity.getSellerId());
                    }
                } else {
                    if (isSeller()) {
                        if (tags.equals(ConsultContants.OVER) || tags.equals(ConsultContants.START) || tags.equals(ConsultContants.CANCEL)) {
                            getConsultInfo();
                        }
                    } else if (isBuyer()) {
                        //离开队伍
                        if (tags.equals(ConsultContants.OVER) || tags.equals(ConsultContants.START)) {
                            getConsultInfo();
                        }
                    } else {
                        if (processState == null || processState.consultInfo == null) return;
                        if (tags.equals(ConsultContants.DEQUEUE)) {
                            //离开队伍
                            processState.consultInfo.userQueuePosition++;
                        } else if (tags.equals(ConsultContants.ENQUEUE)) {
                            processState.consultInfo.userQueuePosition--;
                        }
                        refreshConsult();
                    }
                }
                break;
            case CONSULT_QUEUE_UPDATE:
                if (peerType != DBConstant.SESSION_TYPE_CONSULT) return;
                ConsultState consultInfo = (ConsultState) event.object;
                if (consultInfo.consultInfo.sellerQueueLength > 0) {
                    processState.consultInfo = consultInfo.consultInfo;
                    mQueue.setText(getString(R.string.consult_queue_tip, processState.consultInfo.sellerQueueLength));
                    mQueue.setVisibility(View.VISIBLE);
                } else {
                    mQueue.setVisibility(View.GONE);
                }
                break;
            case SESSION_CONSULT_OK:
                if (peerType != DBConstant.SESSION_TYPE_CONSULT) return;
                refreshConsult();
                break;
        }
    }

    private void reloadProcess() {
        if (retryTimes > 5) {
            this.finish();
            return;
        }
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                retryTimes++;
                imService.getConsultManager().getProcessState(loginUser.getPeerId(), peerId, serviceId);
            }
        }, 1000);
    }

    //开始输入
    public void onEventMainThread(SwitchP2PEvent event) {
        if (loginUser == null || peerEntity == null) return;
        if (event.fromId == peerEntity.getPeerId() && event.toId == loginUser.getPeerId() && event.itemId == serviceId && event.sesstionType == peerType) {
            if (event.event == SwitchP2PEvent.Event.WRITING) {
                mBaseNavView.setTitleText("对方正在输入…");
                startSwitchP2PTask();
//            } else if (event.event == SwitchP2PEvent.Event.STOP_WRITING) {
//                setTitleByUser();
            }
        }
    }

    //停止显示正在输入计时
    private void stopSwitchP2PTask() {
        if (switchP2PTimer != null) {
            switchP2PTimer.cancel();
            switchP2PTimer = null;
        }
    }

    //开始显示正在输入计时
    private synchronized void startSwitchP2PTask() {
        stopSwitchP2PTask();
        if (switchP2PTimer == null) {
            switchP2PTimer = new Timer();
            switchP2PTimer.schedule(new SwitchP2PTask(), 1000, 1000);
        }
    }

    Timer switchP2PTimer;

    private class SwitchP2PTask extends TimerTask {
        private int sendStopWritingTime = 5;

        @Override
        public void run() {
            sendStopWritingTime--;
            if (sendStopWritingTime <= 0) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setTitleByUser();
                    }
                });
                stopSwitchP2PTask();
            }
        }
    }

    public void onEventMainThread(MessageEvent event) {
        MessageEvent.Event type = event.getEvent();
        MessageEntity entity = event.getMessageEntity();
        switch (type) {
            case ACK_SEND_MESSAGE_OK: {
                onMsgAck(event.getMessageEntity());
            }
            break;

            case ACK_SEND_MESSAGE_FAILURE:
                // 失败情况下新添提醒
                if (!AndroidUtils.isNetWorkAvalible(this)) {
                    showToast(R.string.message_send_failed);
                }
            case ACK_SEND_MESSAGE_TIME_OUT: {
                onMsgUnAckTimeoutOrFailure(event.getMessageEntity());
            }
            break;

            case HANDLER_IMAGE_UPLOAD_FAILD: {

                logger.d("pic#onUploadImageFaild");
                ImageMessage imageMessage = (ImageMessage) event.getMessageEntity();
                adapter.updateItemState(imageMessage);
                if (!AndroidUtils.isNetWorkAvalible(this)) {
                    showToast(R.string.message_send_failed);
                } else if (!FileUtil.isFileExist(imageMessage.getPath()) && !FileUtil.isFileExist(imageMessage.getThumbnailUrl())) {
                    showToast(R.string.image_path_unavaluable);
                }
            }
            break;

            case HANDLER_IMAGE_UPLOAD_SUCCESS: {
                ImageMessage imageMessage = (ImageMessage) event.getMessageEntity();
                adapter.updateItemState(imageMessage);
            }
            break;

            case HISTORY_MSG_OBTAIN: {
                if (historyTimes == 1) {
                    adapter.clearItem();
                    reqHistoryMsg();
                }
            }
            break;
        }
    }

    /**
     * audio状态的语音还在使用这个
     */
    protected void initAudioHandler() {
        uiHandler = new Handler() {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case HandlerConstant.HANDLER_RECORD_FINISHED:
                        onRecordVoiceEnd((Float) msg.obj);
                        break;

                    // 录音结束
                    case HandlerConstant.HANDLER_STOP_PLAY:
                        // 其他地方处理了
                        //adapter.stopVoicePlayAnim((String) msg.obj);
                        break;

                    case HandlerConstant.RECEIVE_MAX_VOLUME:
                        onReceiveMaxVolume((Integer) msg.obj);
                        break;

                    case HandlerConstant.RECORD_AUDIO_TOO_LONG:
                        doFinishRecordAudio();
                        break;

                    case HandlerConstant.MSG_RECEIVED_MESSAGE:
                        MessageEntity entity = (MessageEntity) msg.obj;
                        ArrayList<Object> list = adapter.getMessageObjectList();
                        if (list != null && !list.isEmpty()) {
                            Object object = list.get(list.size() - 1);
                            if (object instanceof MessageEntity) {
                                if (((MessageEntity) object).getMsgId() == entity.getMsgId()) {
                                    return;
                                }

                            }
                        }
                        onMsgRecv(entity);
                        break;

                    default:
                        break;
                }
            }
        };
    }

    /**
     * [备注] DB保存，与session的更新manager已经做了
     *
     * @param messageEntity
     */
    private void onMsgAck(MessageEntity messageEntity) {
        logger.d("message_activity#onMsgAck");
        int msgId = messageEntity.getMsgId();
        logger.d("chat#onMsgAck, msgId:%d", msgId);

        /**到底采用哪种ID呐??*/
        long localId = messageEntity.getId();
        adapter.updateItemState(messageEntity);
//        scrollToBottomListItem();
    }


    private void handleUnreadMsgs() {
        logger.d("messageacitivity#handleUnreadMsgs sessionId:%s", currentSessionKey);
        // 清除未读消息
        UnreadEntity unreadEntity = imService.getUnReadMsgManager().findUnread(currentSessionKey);
        if (null == unreadEntity) {
            return;
        }
        int unReadCnt = unreadEntity.getUnReadCnt();
        if (unReadCnt > 0) {
            imService.getNotificationManager().cancelSessionNotifications(String.valueOf(peerType));
            adapter.notifyDataSetChanged();
            scrollToBottomListItem();
        }
    }


    // 肯定是在当前的session内

    private void onMsgRecv(MessageEntity entity) {
        logger.d("message_activity#onMsgRecv");

        imService.getUnReadMsgManager().ackReadMsg(entity);
        logger.d("chat#start pushList");
        pushList(entity);
        ListView lv = lvPTR.getRefreshableView();
        if (lv != null) {

            if (lv.getLastVisiblePosition() < adapter.getCount()) {
                textView_new_msg_tip.setVisibility(View.VISIBLE);
            } else {
                scrollToBottomListItem();
            }
        }
    }


    private void onMsgUnAckTimeoutOrFailure(MessageEntity messageEntity) {
        logger.d("chat#onMsgUnAckTimeoutOrFailure, msgId:%s", messageEntity.getMsgId());
        // msgId 应该还是为0
        adapter.updateItemState(messageEntity);
    }

    /**
     * @Description 初始化AudioManager，用于访问控制音量和钤声模式
     */
    private void initAudioSensor() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void initEmo() {
//        Emoparser.getInstance(MessageActivity.this);
        BaseApplication.gifRunning = true;
    }

    /**
     * @Description 初始化界面控件
     * 有点庞大 todo
     */
    private void initView() {
        // 列表控件(开源PTR)
        mViewBottom = findViewById(R.id.tt_layout_bottom);
        lvPTR = (PullToRefreshListView) this.findViewById(R.id.message_list);
        textView_new_msg_tip = (TextView) findViewById(R.id.tt_new_msg_tip);
        lvPTR.getRefreshableView().addHeaderView(LayoutInflater.from(this).inflate(R.layout.tt_messagelist_header, lvPTR.getRefreshableView(), false));
        lvPTR.getRefreshableView().setCacheColorHint(Color.WHITE);
        lvPTR.getRefreshableView().setSelector(new ColorDrawable(Color.WHITE));
        lvPTR.getRefreshableView().setOnTouchListener(lvPTROnTouchListener);
        adapter = new MessageAdapter(this);
        lvPTR.setAdapter(adapter);
        lvPTR.setOnRefreshListener(this);
//        lvPTR.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(), true, true) {
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//                switch (scrollState) {
//                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
//                        if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
//                            textView_new_msg_tip.setVisibility(View.GONE);
//                        }
//                        break;
//                }
//            }
//        });
        lvPTR.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                            textView_new_msg_tip.setVisibility(View.GONE);
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        textView_new_msg_tip.setOnClickListener(this);

        // 界面底部输入框布局
        sendBtn = (TextView) this.findViewById(R.id.send_message_btn);
        recordAudioBtn = (Button) this.findViewById(R.id.record_voice_btn);
        audioInputImg = (ImageView) this.findViewById(R.id.voice_btn);
        messageEdt = (CustomEditView) this.findViewById(R.id.message_text);
        LayoutParams messageEdtParam = (LayoutParams) messageEdt.getLayoutParams();
        messageEdtParam.addRule(RelativeLayout.LEFT_OF, R.id.show_add_photo_btn);
        messageEdtParam.addRule(RelativeLayout.RIGHT_OF, R.id.voice_btn);

        keyboardInputImg = (ImageView) this.findViewById(R.id.show_keyboard_btn);
        keyboardInputImgEmo = (ImageView) this.findViewById(R.id.show_keyboard_btn_emo);
        addPhotoBtn = (ImageView) this.findViewById(R.id.show_add_photo_btn);
//        addEmoBtn = (ImageView) this.findViewById(R.id.show_emo_btn);
        messageEdt.setOnFocusChangeListener(msgEditOnFocusChangeListener);
        messageEdt.setOnClickListener(this);
        messageEdt.addTextChangedListener(this);
        messageEdt.setOnEditorActionListener(etEditorActionListener);
        addPhotoBtn.setOnClickListener(this);
//        addEmoBtn.setOnClickListener(this);
        keyboardInputImg.setOnClickListener(this);
        keyboardInputImgEmo.setOnClickListener(this);
        audioInputImg.setOnClickListener(this);
        recordAudioBtn.setOnTouchListener(this);
        sendBtn.setOnClickListener(this);
        initSoundVolumeDlg();
        initCameraPop();
        layoutOrderId = findViewById(R.id.rl_order_id);
        findViewById(R.id.tv_order_id).setOnClickListener(this);
        //OTHER_PANEL_VIEW
//        addOthersPanelView = findViewById(R.id.add_others_panel);
//        LayoutParams params = (LayoutParams) addOthersPanelView.getLayoutParams();
//        if (keyboardHeight > 0) {
//            params.height = keyboardHeight;
//            addOthersPanelView.setLayoutParams(params);
//        }
//        View takePhotoBtn = findViewById(R.id.take_photo_btn);
//        View takeCameraBtn = findViewById(R.id.take_camera_btn);
//        takePhotoBtn.setOnClickListener(this);
//        takeCameraBtn.setOnClickListener(this);

        //EMO_LAYOUT
//        emoLayout = (LinearLayout) findViewById(R.id.emo_layout);
//        LayoutParams paramEmoLayout = (LayoutParams) emoLayout.getLayoutParams();
//        if (keyboardHeight > 0) {
//            paramEmoLayout.height = keyboardHeight;
//            emoLayout.setLayoutParams(paramEmoLayout);
//        }
//        emoGridView = (EmoGridView) findViewById(R.id.emo_gridview);
//        yayaEmoGridView = (YayaEmoGridView) findViewById(R.id.yaya_emo_gridview);
//        emoRadioGroup = (RadioGroup) findViewById(R.id.emo_tab_group);
//        emoGridView.setOnEmoGridViewItemClick(onEmoGridViewItemClick);
//        emoGridView.setAdapter();
//        yayaEmoGridView.setOnEmoGridViewItemClick(yayaOnEmoGridViewItemClick);
//        yayaEmoGridView.setAdapter();
//        emoRadioGroup.setOnCheckedChangeListener(emoOnCheckedChangeListener);


        //LOADING
        View view = LayoutInflater.from(MessageActivity.this)
                .inflate(R.layout.tt_progress_ly, null);
        progressbar = (MGProgressbar) view.findViewById(R.id.tt_progress);
        LayoutParams pgParms = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        pgParms.bottomMargin = 50;
        addContentView(view, pgParms);

        //ROOT_LAYOUT_LISTENER
        getContentParentView().getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);

        //顶部商品卡布局
        Serializable goodsEntity = getIntent().getSerializableExtra(IntentConstant.EXTRA_GOODS_LINK);
        goodsLinkView = (IMGoodsLinkView) findViewById(R.id.goods_link_view);
        if (goodsEntity != null) {
            goodsLinkEntity = (ProductCardModel) goodsEntity;
            goodsLinkView.setData(goodsLinkEntity);
            goodsLinkView.setOnSendListener(this);
            goodsLinkView.setVisibility(View.VISIBLE);
        } else {
            goodsLinkView.setVisibility(View.GONE);
        }

        orderId = getIntent().getLongExtra(IntentConstant.EXTRA_SEND_ORDER_ID, 0);
        if (orderId > 0) {
            layoutOrderId.setVisibility(View.VISIBLE);
        } else {
            layoutOrderId.setVisibility(View.GONE);
        }

        messageConsultTimerView = (MessageConsultTimerView) findViewById(R.id.message_consult_timer_view);
        messageConsultTimerView.setOnTimeOverListner(onTimeOverListner);
        mConsultComment = (MessageConsultComment) findViewById(R.id.view_comment);
        mQueue = (TextView) findViewById(R.id.tv_queue);
        mConsultBottom = (MessageConsultBottomView) findViewById(R.id.consult_bottom);
        mConsultBottom.setConsultListener(consultListener);
    }

    private boolean needFinishAcWhenComments = false;
    private MessageConsultBottomView.ConsultListener consultListener = new MessageConsultBottomView.ConsultListener() {
        @Override
        public void showComment() {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            mConsultComment.showComment(MessageActivity.this, processState.processOrderItem.bizOrder.bizOrderId, isNeedFinishAcWhenComments);
            mConsultComment.setVisibility(View.VISIBLE);
        }

        @Override
        public void accpetOrder() {
            showLoadingView(null);
            TCEventHelper.onEvent(MessageActivity.this, AnalyDataValue.IM_CONSULTING_ACCEPT);
            consultController.acceptProcessOrder(MessageActivity.this);
        }
    };


    private void initCameraPop() {
        mCameraPop = new CameraPop(this, new CameraPopListener() {
            @Override
            public void onCamreaClick(CameraOptions options) {
//                PackageManager pkm = getPackageManager();
//                boolean has_permission = (PackageManager.PERMISSION_GRANTED == pkm.checkPermission("android.permission.CAMERA", "com.quanyan.yhy"));
//                if (!has_permission) {
//                    ToastUtil.showToast(MessageActivity.this, R.string.camera_permission_not_open);
//                    return;
//                }
                options.setOpenType(OpenType.OPEN_CAMERA);
                mCameraPop.process();
            }

            @Override
            public void onPickClick(CameraOptions options) {
                options.setOpenType(OpenType.OPENN_USER_ALBUM).setMaxSelect(9);
                mCameraPop.process();
            }

            @Override
            public void onDelClick() {

            }

            @Override
            public void onVideoClick() {

            }

            /*@Override
            public void onVideoDraftClick() {

            }*/
        }, new SelectMoreListener() {
            @Override
            public void onSelectedMoreListener(List<MediaItem> pathList) {
                handleImagePickData(pathList);
            }
        });
    }

    /**
     * 软键盘sendlistener
     */
    private TextView.OnEditorActionListener etEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEND) {

                logger.d("message_activity#send btn clicked");

                String content = messageEdt.getText().toString();
                logger.d("message_activity#chat content:%s", content);
                if (content.trim().equals("")) {
                    Toast.makeText(MessageActivity.this,
                            getResources().getString(R.string.message_null), Toast.LENGTH_LONG).show();
                    return true;
                }
                TextMessage textMessage = TextMessage.buildForSend(content, loginUser, peerEntity, peerType, serviceId);
                imService.getMessageManager().sendText(textMessage);
                messageEdt.setText("");
                pushList(textMessage);
                scrollToBottomListItem();

                return true;
            }
            return false;
        }
    };

    /**
     * @Description 初始化音量对话框
     */
    private void initSoundVolumeDlg() {
        soundVolumeDialog = new Dialog(this, R.style.SoundVolumeStyle);
        soundVolumeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        soundVolumeDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        soundVolumeDialog.setContentView(R.layout.tt_sound_volume_dialog);
        soundVolumeDialog.setCanceledOnTouchOutside(false);
        soundVolumeImg = (ImageView) soundVolumeDialog.findViewById(R.id.sound_volume_img);
//        soundVolumeLayout = soundVolumeDialog.findViewById(R.id.sound_volume_bk);
    }

    /**
     * 1.初始化请求历史消息
     * 2.本地消息不全，也会触发
     */
    private void reqHistoryMsg() {
        historyTimes++;
        List<MessageEntity> msgList = imService.getMessageManager().loadHistoryMsg(historyTimes, currentSessionKey);
        pushList(msgList);
        scrollToBottomListItem();
    }

    /**
     * @param msg
     */
    public void pushList(MessageEntity msg) {
        logger.d("chat#pushList msgInfo:%s", msg);
        adapter.addItem(msg);
    }

    public void pushList(List<MessageEntity> entityList) {
        logger.d("chat#pushList list:%d", entityList.size());
        adapter.loadHistoryList(entityList);
    }


    /**
     * @Description 录音超时(60s)，发消息调用该方法
     */
    public void doFinishRecordAudio() {
        try {
            if (audioRecorderInstance.isRecording()) {
                audioRecorderInstance.setRecording(false);
            }
            if (soundVolumeDialog.isShowing()) {
                soundVolumeDialog.dismiss();
            }

            recordAudioBtn.setBackgroundResource(R.drawable.tt_pannel_btn_voiceforward_normal);

            audioRecorderInstance.setRecordTime(SysConstant.MAX_SOUND_RECORD_TIME);
            onRecordVoiceEnd(SysConstant.MAX_SOUND_RECORD_TIME);
        } catch (Exception e) {
        }
    }

    /**
     * @param voiceValue
     * @Description 根据分贝值设置录音时的音量动画
     */
    private void onReceiveMaxVolume(int voiceValue) {
        if (y1 - y2 >= 180 || !audioRecorderInstance.isRecording()) {
            return;
        }
        if (voiceValue < 200.0) {
            soundVolumeImg.setImageResource(R.mipmap.tt_sound_volume_01);
        } else if (voiceValue > 200.0 && voiceValue < 1200) {
            soundVolumeImg.setImageResource(R.mipmap.tt_sound_volume_02);
        } else if (voiceValue > 1200 && voiceValue < 10000) {
            soundVolumeImg.setImageResource(R.mipmap.tt_sound_volume_03);
        } else if (voiceValue > 10000) {
            soundVolumeImg.setImageResource(R.mipmap.tt_sound_volume_04);
        }
    }


    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
    }

    /**
     * @param data
     * @Description 处理拍照后的数据
     * 应该是从某个 activity回来的
     */
    private void handleTakePhotoData(Intent data) {
        ImageMessage imageMessage = ImageMessage.buildForSend(takePhotoSavePath, loginUser, peerEntity, peerType, serviceId);
        List<ImageMessage> sendList = new ArrayList<>(1);
        sendList.add(imageMessage);
        imService.getMessageManager().sendImages(sendList);
        // 格式有些问题
        pushList(imageMessage);
        messageEdt.clearFocus();//消除焦点
    }

    /**
     * @param audioLen
     * @Description 录音结束后处理录音数据
     */
    private void onRecordVoiceEnd(float audioLen) {
        if (audioRecorderInstance.isHasRecord()) {
            logger.d("message_activity#chat#audio#onRecordVoiceEnd audioLen:%f", audioLen);
            AudioMessage audioMessage = AudioMessage.buildForSend(audioLen, audioSavePath, loginUser, peerEntity, peerType, serviceId);
            imService.getMessageManager().sendVoice(audioMessage);
            pushList(audioMessage);
        } else {
            ToastUtil.showToast(this, R.string.audio_recorded_fail);
        }
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
    }

    @Override
    public void onPullDownToRefresh(
            final PullToRefreshBase<ListView> refreshView) {
        // 获取消息
        refreshView.postDelayed(new Runnable() {
            @Override
            public void run() {
                ListView mlist = lvPTR.getRefreshableView();
                int preSum = mlist.getCount();
                MessageEntity messageEntity = adapter.getTopMsgEntity();
                if (messageEntity != null) {
                    List<MessageEntity> historyMsgInfo = imService.getMessageManager().loadHistoryMsg(messageEntity, historyTimes);
                    if (historyMsgInfo.size() > 0) {
                        historyTimes++;
                        adapter.loadHistoryList(historyMsgInfo);
                    }
                }

                int afterSum = mlist.getCount();
                mlist.setSelection(afterSum - preSum);
                /**展示位置为这次消息的最末尾*/
                //mlist.setSelection(size);
                // 展示顶部
//                if (!(mlist).isStackFromBottom()) {
//                    mlist.setStackFromBottom(true);
//                }
//                mlist.setStackFromBottom(false);
                refreshView.onRefreshComplete();
            }
        }, 200);
    }


    @Override
    public void onClick(View v) {
        final int id = v.getId();
        switch (id) {
            case R.id.tv_send:
                MessageEntity message = null;
                message = ProductCardMessage.buildForSend(goodsLinkEntity, loginUser, peerEntity, peerType, serviceId);
                imService.getMessageManager().sendProductCard((ProductCardMessage) message);
                pushList(message);
                scrollToBottomListItem();
                break;
            case R.id.tv_order_id: {
                message = TextMessage.buildForSend(getString(R.string.order_code) + " : " + orderId, loginUser, peerEntity, peerType, serviceId);
                imService.getMessageManager().sendText((TextMessage) message);
                pushList(message);
                scrollToBottomListItem();
            }
            break;
            case R.id.show_add_photo_btn: {
                changeInputModeDefault(false);
                mCameraPop.showMenu(messageEdt);
                scrollToBottomListItem();
            }
            break;
//            case R.id.take_photo_btn: {
//                if (albumList.size() < 1) {
//                    Toast.makeText(MessageActivity.this,
//                            getResources().getString(R.string.not_found_album), Toast.LENGTH_LONG)
//                            .show();
//                    return;
//                }
//                // 选择图片的时候要将session的整个回话 传过来
//                Intent intent = new Intent(MessageActivity.this, PickPhotoActivity.class);
//                intent.putExtra(IntentConstant.KEY_SESSION_KEY, currentSessionKey);
//                startActivityForResult(intent, SysConstant.ALBUM_BACK_DATA);
//
//                MessageActivity.this.overridePendingTransition(R.anim.tt_album_enter, R.anim.tt_stay);
//                //addOthersPanelView.setVisibility(View.GONE);
//                messageEdt.clearFocus();//切记清除焦点
//                scrollToBottomListItem();
//            }
//            break;
//            case R.id.take_camera_btn: {
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                takePhotoSavePath = CommonUtil.getImageSavePath(String.valueOf(System
//                        .currentTimeMillis())
//                        + ".jpg");
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(takePhotoSavePath)));
//                startActivityForResult(intent, SysConstant.CAMERA_WITH_DATA);
//                //addOthersPanelView.setVisibility(View.GONE);
//                messageEdt.clearFocus();//切记清除焦点
//                scrollToBottomListItem();
//            }
//            break;
            case R.id.show_emo_btn: {
                changeInputModeEmo();
                inputManager.hideSoftInputFromWindow(messageEdt.getWindowToken(), 0);
            }
            break;
            case R.id.send_message_btn: {
                logger.d("message_activity#send btn clicked");

                String content = messageEdt.getText().toString();
                logger.d("message_activity#chat content:%s", content);
                if (content.trim().equals("")) {
                    Toast.makeText(MessageActivity.this,
                            getResources().getString(R.string.message_null), Toast.LENGTH_LONG).show();
                    return;
                }
                TextMessage textMessage = TextMessage.buildForSend(content, loginUser, peerEntity, peerType, serviceId);
                imService.getMessageManager().sendText(textMessage);
                messageEdt.setText("");
                pushList(textMessage);
                scrollToBottomListItem();
            }
            break;
            case R.id.voice_btn: {
                changeInputModeAudio();
            }
            break;
            case R.id.show_keyboard_btn_emo:
            case R.id.show_keyboard_btn: {
                changeInputModeDefault(true);
                messageEdt.requestFocus();
                inputManager.toggleSoftInputFromWindow(messageEdt.getWindowToken(), 1, 0);

            }
            break;
            case R.id.message_text:
                break;
            case R.id.tt_new_msg_tip: {
                scrollToBottomListItem();
                textView_new_msg_tip.setVisibility(View.GONE);
            }
            break;
        }
    }

    private float y1, y2, x1, x2;

    // 主要是录制语音的
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int id = v.getId();
        scrollToBottomListItem();
        if (id == R.id.record_voice_btn) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {

                if (AudioPlayerHandler.getInstance().isPlaying())
                    AudioPlayerHandler.getInstance().stopPlayer();
                y1 = event.getY();
                recordAudioBtn.setBackgroundResource(R.drawable.tt_pannel_btn_voiceforward_pressed);
                recordAudioBtn.setText(MessageActivity.this.getResources().getString(
                        R.string.release_to_send_voice));

                soundVolumeImg.setImageResource(R.mipmap.tt_sound_volume_01);
                soundVolumeDialog.show();
                audioSavePath = CommonUtil
                        .getAudioSavePath(IMLoginManager.instance().getLoginId());

                // 这个callback很蛋疼，发送消息从MotionEvent.ACTION_UP 判断
                audioRecorderInstance = new AudioRecordHandler(audioSavePath);

                audioRecorderThread = new Thread(audioRecorderInstance);
                audioRecorderInstance.setRecording(true);
                logger.d("message_activity#audio#audio record thread starts");
                audioRecorderThread.start();
            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                y2 = event.getY();
                if (y1 - y2 > 180) {
                    soundVolumeImg.setImageResource(R.mipmap.tt_sound_volume_cancel_bk);
                } else {
                    soundVolumeImg.setImageResource(R.mipmap.tt_sound_volume_01);
                }
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                y2 = event.getY();
                if (audioRecorderInstance.isRecording()) {
                    audioRecorderInstance.setRecording(false);
                }
                if (soundVolumeDialog.isShowing()) {
                    soundVolumeDialog.dismiss();
                }
                recordAudioBtn.setBackgroundResource(R.drawable.tt_pannel_btn_voiceforward_normal);
                recordAudioBtn.setText(MessageActivity.this.getResources().getString(
                        R.string.tip_for_voice_forward));
                if (y1 - y2 <= 180) {
                    if (audioRecorderInstance.getRecordTime() >= 1) {
                        if (audioRecorderInstance.getRecordTime() < SysConstant.MAX_SOUND_RECORD_TIME) {
                            Message msg = uiHandler.obtainMessage();
                            msg.what = HandlerConstant.HANDLER_RECORD_FINISHED;
                            msg.obj = audioRecorderInstance.getRecordTime();
                            uiHandler.sendMessage(msg);
                        }
                    } else {
                        soundVolumeImg.setImageResource(R.mipmap.tt_sound_volume_short_tip_bk);
                        soundVolumeDialog.show();
                        Timer timer = new Timer();
                        timer.schedule(new TimerTask() {
                            public void run() {
                                if (soundVolumeDialog.isShowing())
                                    soundVolumeDialog.dismiss();
                                this.cancel();
                            }
                        }, 700);
                    }
                }
            }
        }
        return false;
    }

    @Override
    protected void onStop() {
        logger.d("message_activity#onStop:%s", this);

        if (null != adapter) {
            adapter.hidePopup();
        }

        AudioPlayerHandler.getInstance().clear();
        super.onStop();
    }

    @Override
    protected void onStart() {
        logger.d("message_activity#onStart:%s", this);
        super.onStart();
    }


    @Override
    public void afterTextChanged(Editable s) {
        if (TextUtils.isEmpty(s.toString())) return;
        if (peerEntity == null && loginUser == null || imService == null) return;
        imService.getSwitchServiceManager().sendP2PWritingMsg(loginUser.getPeerId(), peerEntity.getPeerId(), true, peerType, serviceId);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() > 0) {
            LayoutParams messageEdtParam = (LayoutParams) messageEdt.getLayoutParams();
            messageEdtParam.addRule(RelativeLayout.LEFT_OF, R.id.send_message_btn);

            sendBtn.setVisibility(View.VISIBLE);
            addPhotoBtn.setVisibility(View.GONE);
//            sendBtn.setVisibility(View.VISIBLE);
//            LayoutParams param = (LayoutParams) messageEdt
//                    .getLayoutParams();
//            param.addRule(RelativeLayout.LEFT_OF, R.id.show_emo_btn);
//            addPhotoBtn.setVisibility(View.GONE);


        } else {
//            addPhotoBtn.setVisibility(View.VISIBLE);
//            LayoutParams param = (LayoutParams) messageEdt
//                    .getLayoutParams();
//            param.addRule(RelativeLayout.LEFT_OF, R.id.show_emo_btn);
//            sendBtn.setVisibility(View.GONE);

            LayoutParams messageEdtParam = (LayoutParams) messageEdt.getLayoutParams();
            messageEdtParam.addRule(RelativeLayout.LEFT_OF, R.id.show_add_photo_btn);

            addPhotoBtn.setVisibility(View.VISIBLE);
            sendBtn.setVisibility(View.GONE);
        }
    }

    private void changeInputModeDefault(boolean showInput) {
        inputMode = InputMode.DEFAULT;
        recordAudioBtn.setVisibility(View.GONE);
        keyboardInputImg.setVisibility(View.GONE);
        messageEdt.setVisibility(View.VISIBLE);
        audioInputImg.setVisibility(View.VISIBLE);
//        addEmoBtn.setVisibility(View.VISIBLE);
        keyboardInputImgEmo.setVisibility(View.GONE);
//        if (showInput) {
//            if (emoLayout.getVisibility() == View.VISIBLE) {
//                this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
//            } else {
//                this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//            }
//        } else {
//            emoLayout.setVisibility(View.GONE);
//            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//        }

//        scrollToBottomListItem();
    }

    private void changeInputModeAudio() {
        inputMode = InputMode.AUDIO;
        inputManager.hideSoftInputFromWindow(messageEdt.getWindowToken(), 0);
        messageEdt.setVisibility(View.GONE);
        audioInputImg.setVisibility(View.GONE);
        recordAudioBtn.setVisibility(View.VISIBLE);
        keyboardInputImg.setVisibility(View.VISIBLE);
//        emoLayout.setVisibility(View.GONE);
        messageEdt.setText("");
    }

    private void changeInputModeEmo() {
        /**yingmu 调整成键盘输出*/
        inputMode = InputMode.EMO;
        recordAudioBtn.setVisibility(View.GONE);
        keyboardInputImg.setVisibility(View.GONE);
        messageEdt.setVisibility(View.VISIBLE);
        audioInputImg.setVisibility(View.VISIBLE);
//        addEmoBtn.setVisibility(View.INVISIBLE);
        keyboardInputImgEmo.setVisibility(View.VISIBLE);
//        emoLayout.setVisibility(View.VISIBLE);
//        yayaEmoGridView.setVisibility(View.VISIBLE);
//        emoRadioGroup.check(R.id.tab1);
//        emoGridView.setVisibility(View.GONE);
        messageEdt.clearFocus();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
    }


    /**
     * @Description 滑动到列表底部
     */
    private void scrollToBottomListItem() {
        logger.d("message_activity#scrollToBottomListItem");

        // todo eric, why use the last one index + 2 can real scroll to the
        // bottom?
        final ListView lv = lvPTR.getRefreshableView();
        if (lv != null) {
            lv.post(new Runnable() {
                @Override
                public void run() {
                    lv.setSelection(lv.getBottom());
                }
            });
        }
        textView_new_msg_tip.setVisibility(View.GONE);
    }

    @Override
    protected void onPause() {
        logger.d("message_activity#onPause:%s", this);
        super.onPause();
    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {
    }

    @Override
    public void onSensorChanged(SensorEvent arg0) {
        try {
            if (!AudioPlayerHandler.getInstance().isPlaying()) {
                return;
            }
            float range = arg0.values[0];
            if (null != sensor && range == sensor.getMaximumRange()) {
                // 屏幕恢复亮度
                AudioPlayerHandler.getInstance().setAudioMode(AudioManager.MODE_NORMAL, this);
            } else {
                // 屏幕变黑
                AudioPlayerHandler.getInstance().setAudioMode(AudioManager.MODE_IN_CALL, this);
            }
        } catch (Exception e) {
            logger.error(e);
        }
    }

    public static Handler getUiHandler() {
        return uiHandler;
    }

    private void actFinish() {
        inputManager.hideSoftInputFromWindow(messageEdt.getWindowToken(), 0);
//        IMStackManager.getStackManager().popTopActivitys(MainActivity.class);
        BaseApplication.gifRunning = false;
        MessageActivity.this.finish();
    }

//    private RadioGroup.OnCheckedChangeListener emoOnCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
//        @Override
//        public void onCheckedChanged(RadioGroup radioGroup, int id) {
//            switch (id) {
//                case R.id.tab2:
//                    if (emoGridView.getVisibility() != View.VISIBLE) {
//                        yayaEmoGridView.setVisibility(View.GONE);
//                        emoGridView.setVisibility(View.VISIBLE);
//                    }
//                    break;
//                case R.id.tab1:
//                    if (yayaEmoGridView.getVisibility() != View.VISIBLE) {
//                        emoGridView.setVisibility(View.GONE);
//                        yayaEmoGridView.setVisibility(View.VISIBLE);
//                    }
//                    break;
//            }
//        }
//    };

//    private YayaEmoGridView.OnEmoGridViewItemClick yayaOnEmoGridViewItemClick = new YayaEmoGridView.OnEmoGridViewItemClick() {
//        @Override
//        public void onItemClick(int facesPos, int viewIndex) {
//            int resId = Emoparser.getInstance(MessageActivity.this).getYayaResIdList()[facesPos];
//            logger.d("message_activity#yayaEmoGridView be clicked");
//
//            String content = Emoparser.getInstance(MessageActivity.this).getYayaIdPhraseMap()
//                    .get(resId);
//            if (content.equals("")) {
//                Toast.makeText(MessageActivity.this,
//                        getResources().getString(R.string.message_null), Toast.LENGTH_LONG).show();
//                return;
//            }
//
//            TextMessage textMessage = TextMessage.buildForSend(content, loginUser, peerEntity);
//            imService.getMessageManager().sendText(textMessage);
//            pushList(textMessage);
//            scrollToBottomListItem();
//        }
//    };

//    private OnEmoGridViewItemClick onEmoGridViewItemClick = new OnEmoGridViewItemClick() {
//        @Override
//        public void onItemClick(int facesPos, int viewIndex) {
//            int deleteId = (++viewIndex) * (SysConstant.pageSize - 1);
//            if (deleteId > Emoparser.getInstance(MessageActivity.this).getResIdList().length) {
//                deleteId = Emoparser.getInstance(MessageActivity.this).getResIdList().length;
//            }
//            if (deleteId == facesPos) {
//                String msgContent = messageEdt.getText().toString();
//                if (msgContent.isEmpty())
//                    return;
//                if (msgContent.contains("["))
//                    msgContent = msgContent.substring(0, msgContent.lastIndexOf("["));
//                messageEdt.setText(msgContent);
//            } else {
//                int resId = Emoparser.getInstance(MessageActivity.this).getResIdList()[facesPos];
//                String pharse = Emoparser.getInstance(MessageActivity.this).getIdPhraseMap()
//                        .get(resId);
//                int startIndex = messageEdt.getSelectionStart();
//                Editable edit = messageEdt.getEditableText();
//                if (startIndex < 0 || startIndex >= edit.length()) {
//                    if (null != pharse) {
//                        edit.append(pharse);
//                    }
//                } else {
//                    if (null != pharse) {
//                        edit.insert(startIndex, pharse);
//                    }
//                }
//            }
//            Editable edtable = messageEdt.getText();
//            int position = edtable.length();
//            Selection.setSelection(edtable, position);
//        }
//    };

    private OnTouchListener lvPTROnTouchListener = new OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (inputMode == InputMode.AUDIO) {
                    return false;
                }
                changeInputModeDefault(false);
                messageEdt.clearFocus();
                inputManager.hideSoftInputFromWindow(messageEdt.getWindowToken(), 0);
            }
            return false;
        }
    };

    private View.OnFocusChangeListener msgEditOnFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                changeInputModeDefault(true);
            }
        }
    };


    private ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            Rect r = new Rect();
            getContentParentView().getGlobalVisibleRect(r);
            // 进入Activity时会布局，第一次调用onGlobalLayout，先记录开始软键盘没有弹出时底部的位置
            if (rootBottom == Integer.MIN_VALUE) {
                rootBottom = r.bottom;
                return;
            }
            // adjustResize，软键盘弹出后高度会变小
            if (r.bottom < rootBottom) {
                //按照键盘高度设置表情框和发送图片按钮框的高度
                keyboardHeight = rootBottom - r.bottom;
                SystemConfigSp.instance().init(MessageActivity.this);
                SystemConfigSp.instance().setIntConfig(currentInputMethod, keyboardHeight);
//                LayoutParams params = (LayoutParams) addOthersPanelView.getLayoutParams();
//                params.height = keyboardHeight;
//                LayoutParams params1 = (LayoutParams) emoLayout.getLayoutParams();
//                params1.height = keyboardHeight;
            } else {
//                if (inputMode == InputMode.DEFAULT) {
//                    if (emoLayout.getVisibility() == View.VISIBLE) {
//                        mUIHandler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                MessageActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//                                emoLayout.setVisibility(View.GONE);
//                            }
//                        }, 500);
//                    }
//                }
            }
        }
    };

    private class switchInputMethodReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.INPUT_METHOD_CHANGED")) {
                currentInputMethod = Settings.Secure.getString(MessageActivity.this.getContentResolver(), Settings.Secure.DEFAULT_INPUT_METHOD);
                SystemConfigSp.instance().setStrConfig(SystemConfigSp.SysCfgDimension.DEFAULTINPUTMETHOD, currentInputMethod);
                int height = SystemConfigSp.instance().getIntConfig(currentInputMethod);
                if (keyboardHeight != height) {
                    keyboardHeight = height;
//                    addOthersPanelView.setVisibility(View.GONE);
//                    emoLayout.setVisibility(View.GONE);
                    MessageActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                    messageEdt.requestFocus();
//                    if (keyboardHeight != 0 && addOthersPanelView.getLayoutParams().height != keyboardHeight) {
//                        LayoutParams params = (LayoutParams) addOthersPanelView.getLayoutParams();
//                        params.height = keyboardHeight;
//                    }
//                    if (keyboardHeight != 0 && emoLayout.getLayoutParams().height != keyboardHeight) {
//                        LayoutParams params = (LayoutParams) emoLayout.getLayoutParams();
//                        params.height = keyboardHeight;
//                    }
                } else {
//                    addOthersPanelView.setVisibility(View.VISIBLE);
//                    emoLayout.setVisibility(View.VISIBLE);
//                    MessageActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
//                    messageEdt.requestFocus();
                }
            }
        }

    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        hideLoadingView();
        int what = msg.what;
        if (what == ValueConstants.CANCEL_CONSULT_ORDER_OK) {
            CancelProcessResult result = (CancelProcessResult) msg.obj;
            if (result.success) {
                finish();
            } else {
                ToastUtil.showToast(this, "取消失败");
            }
        } else if (what == ValueConstants.CANCEL_CONSULT_ORDER_KO) {
            ToastUtil.showToast(this, StringUtil.handlerErrorCode(this, msg.arg1));
        } else if (what == ValueConstants.FINISH_CONSULT_ORDER_OK) {
            FinishProcessResult result = (FinishProcessResult) msg.obj;
            if (result.success) {
                showLoadingView(null);
            } else {
                ToastUtil.showToast(this, "结束失败");
            }
        } else if (what == ValueConstants.FINISH_CONSULT_ORDER_KO) {
            ToastUtil.showToast(this, StringUtil.handlerErrorCode(this, msg.arg1));
        } else if (what == ValueConstants.ACCEPT_PROCESS_ORDER_OK) {
            AcceptProcessOrderResult result = (AcceptProcessOrderResult) msg.obj;
            if (result.success) {
                showLoadingView(null);
                getConsultInfo();
            } else {
                if (!StringUtil.isEmpty(result.message)) {
                    ToastUtil.showToast(this, result.message);
                }
            }
        } else if (what == ValueConstants.ACCEPT_PROCESS_ORDER_KO) {
            ToastUtil.showToast(this, StringUtil.handlerErrorCode(this, msg.arg1));
        }
    }

    boolean onNetStateChanged = false;
}
