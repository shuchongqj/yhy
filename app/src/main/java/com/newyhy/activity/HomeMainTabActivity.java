package com.newyhy.activity;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PointF;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.harwkin.nb.camera.CameraHandler;
import com.harwkin.nb.camera.CameraManager;
import com.harwkin.nb.camera.callback.SelectMoreListener;
import com.harwkin.nb.camera.options.CameraOptions;
import com.harwkin.nb.camera.type.OpenType;
import com.lidroid.xutils.util.LogUtils;
import com.mogujie.tt.imservice.event.LoginEvent;
import com.mogujie.tt.imservice.event.UnreadEvent;
import com.mogujie.tt.imservice.service.IMService;
import com.mogujie.tt.imservice.support.IMServiceConnector;
import com.newyhy.boom.BuilderManager;
import com.newyhy.boom.MyBoomMenuButton;
import com.newyhy.cache.circle.SPCache;
import com.newyhy.fragment.NewHomeCourtFragment;
import com.newyhy.fragment.circle.NewCircleFragment;
import com.newyhy.views.ActivityPopupWindow;
import com.newyhy.views.VirtualKey;
import com.quanyan.base.util.LocalUtils;
import com.quanyan.yhy.ui.common.WebViewActivity;
import com.yhy.common.utils.NetworkUtil;
import com.quanyan.base.util.StringUtil;
import com.quanyan.pedometer.newpedometer.StepService;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.AnalyDataValue;
import com.quanyan.yhy.common.LiveType;
import com.quanyan.yhy.common.UserStatus;
import com.quanyan.yhy.eventbus.EvBusMessageCount;
import com.quanyan.yhy.eventbus.EvBusWhenSelectFragment;
import com.quanyan.yhy.libanalysis.AnEvent;
import com.quanyan.yhy.libanalysis.Analysis;
import com.quanyan.yhy.net.NetManager;
import com.quanyan.yhy.net.lsn.OnResponseListener;
import com.quanyan.yhy.ui.UpdateAcitivity;
import com.quanyan.yhy.ui.UpdateController;
import com.quanyan.yhy.ui.base.utils.DialogUtil;
import com.quanyan.yhy.ui.base.utils.NativeUtils;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.personal.PersonalFragment;
import com.quanyan.yhy.ui.shortvideo.MediaRecorderActivity;
import com.quanyan.yhy.ui.sport.SportFragment;
import com.smart.sdk.api.resp.Api_LIVE_LiveRoomHasOrNot;
import com.smart.sdk.api.resp.Api_RESOURCECENTER_PublishBootResult;
import com.tencent.rtmp.TXVodPlayer;
import com.videolibrary.controller.LiveController;
import com.videolibrary.utils.IntentUtil;
import com.yhy.boombutton.BoomButtons.BoomButton;
import com.yhy.boombutton.BoomButtons.ButtonPlaceAlignmentEnum;
import com.yhy.boombutton.OnBoomListener;
import com.yhy.boombutton.Util;
import com.yhy.cityselect.entity.CityIndexBean;
import com.yhy.cityselect.entity.CityListBean;
import com.yhy.cityselect.cache.CityCache;
import com.yhy.common.beans.album.MediaItem;
import com.yhy.common.beans.net.model.common.BoothList;
import com.yhy.common.beans.net.model.rc.OnlineUpgrade;
import com.yhy.common.beans.net.model.user.UserStatusInfoList;
import com.yhy.common.constants.IntentConstant;
import com.yhy.common.constants.IntentConstants;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.eventbus.event.EvBusCircleTabRefresh;
import com.yhy.common.utils.AndroidUtils;
import com.yhy.common.utils.JSONUtils;
import com.yhy.common.utils.PermissionUtils;
import com.yhy.common.utils.SPUtils;
import com.yhy.location.EvBusLocation;
import com.yhy.location.LocationManager;
import com.yhy.network.YhyCallback;
import com.yhy.network.YhyCaller;
import com.yhy.network.YhyCode;
import com.yhy.network.api.outplace.OutPlaceApi;
import com.yhy.network.api.snscenter.SnsCenterApi;
import com.yhy.network.req.outplace.GetOutPlaceCityListReq;
import com.yhy.network.req.snscenter.GetTagInfoListByTypeReq;
import com.yhy.network.req.snscenter.QueryGuidanceRecordReq;
import com.yhy.network.resp.Response;
import com.yhy.network.resp.outplace.GetOutPlaceCityListResp;
import com.yhy.network.resp.snscenter.GetTagInfoListByTypeResp;
import com.yhy.network.resp.snscenter.QueryGuidanceRecordResp;
import com.yhy.router.RouterPath;
import com.yhy.router.YhyRouter;
import com.yhy.service.IUserService;
import com.ymanalyseslibrary.secon.YmAnalyticsEvent;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

@Route(path = RouterPath.ACTIVITY_MAIN)
public class HomeMainTabActivity extends VideoSupportActivity implements View.OnClickListener {

    private NewHomeCourtFragment mHomeCourtFragment;
    private SportFragment mSportFragment;
    private Fragment newCircleFragment;
    private PersonalFragment mPersonalFragment;

    private LinearLayout llHome;
    private LinearLayout llSport;
    private LinearLayout llCircle;
    private LinearLayout llPerson;
    private MyBoomMenuButton boomMenuButton;
    public static int imUnreadCount = 0;
    private int mClickBackCount = 0;

    private static final int MSG_SHOW_BOMBOX = 0x1002;
    private IMService imService;
    private ImageView imgUnRead;
    private UpdateController updateController;
    private LiveController mLiveController;
    private long userID;
    private VirtualKey root;

//    private YhyListVideoView videoView;
//    public boolean isFullScreen;

    private YhyCaller getTagInfoListByTypeCall;
    private YhyCaller queryGuidanceRecordCall;
//    private YhyCaller getOutplaceCityListCall;

    private WebView webview;

    @Autowired
    IUserService userService;

    public static boolean hasSelectedInterest = true;
    private IMServiceConnector imServiceConnector = new IMServiceConnector() {

        @Override
        public void onServiceDisconnected() {
            if (EventBus.getDefault().isRegistered(HomeMainTabActivity.this)) {
                EventBus.getDefault().unregister(HomeMainTabActivity.this);
            }
        }

        @Override
        public void onIMServiceConnected() {
            LogUtils.d("chatfragment#recent#onIMServiceConnected");
            imService = imServiceConnector.getIMService();
            if (imService == null) {
                return;
            }
            // 依赖联系人回话、未读消息、用户的信息三者的状态
            refreshUnreadCount(SPUtils.getIntTextChanged(HomeMainTabActivity.this));
            if (!EventBus.getDefault().isRegistered(HomeMainTabActivity.this)) {
                EventBus.getDefault().register(HomeMainTabActivity.this);
            }
        }
    };

    private Dialog mCancelDialog;
    private boolean locationPermissionWhenOnCreate;
    private FrameLayout full_screen_container;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        startService(new Intent(this, StepService.class));
//        // 记步服务保活措施
//        keepServiceLive();

        locationPermissionWhenOnCreate = PermissionUtils.checkLocationPermission(this);

        if (!locationPermissionWhenOnCreate) {
            EventBus.getDefault().post(new EvBusLocation(4001));
            //弹出设置权限的dialog
            if (mCancelDialog == null) {

                mCancelDialog = DialogUtil.showMessageDialog(this, "开启位置服务,获取精准定位", "",
                        getString(R.string.go_setting), getString(R.string.cancel), v -> {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                            mCancelDialog.dismiss();
                        }, v -> mCancelDialog.dismiss());
            }
            mCancelDialog.show();
        }
        queryInterestRecord();
        getCircleTab();
        player = new TXVodPlayer(this);

        FrameLayout fastWebView = findViewById(R.id.fastWebView);
        fastWebView.addView(webview = WebViewActivity.createFastWebView(this));
    }

    private void queryInterestRecord() {
        YhyCallback<Response<QueryGuidanceRecordResp>> callback = new YhyCallback<Response<QueryGuidanceRecordResp>>() {
            @Override
            public void onSuccess(Response<QueryGuidanceRecordResp> data) {
                hasSelectedInterest = data.getContent().getValue();
            }

            @Override
            public void onFail(YhyCode code, Exception exception) {

            }
        };
        queryGuidanceRecordCall = new SnsCenterApi().queryGuidanceRecord(new QueryGuidanceRecordReq(), callback).execAsync();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        NativeUtils.parseNativeData(intent, this);
        if (intent.getBooleanExtra(SPUtils.EXTRA_GONA_DISCOVER_TYPE, false)) {
            // 选择圈子
            selectedFragment(2);
            tabSelected(llCircle);
//            mImmersionBar.fitsSystemWindows(false).transparentStatusBar().flymeOSStatusBarFontColor("#000000").statusBarDarkFont(true).init();
            mImmersionBar.fitsSystemWindows(true).statusBarColor(R.color.White).statusBarDarkFont(true).init();
        }
    }

    @Override
    protected void initData() {
        mLiveController = LiveController.getInstance();
    }

    @Override
    protected void setListener() {
        super.setListener();
        llHome.setOnClickListener(this);
        llSport.setOnClickListener(this);
        llCircle.setOnClickListener(this);
        llPerson.setOnClickListener(this);
    }

    @Override
    protected void initView() {
        super.initView();
        llHome = findViewById(R.id.ll_home_court);
        llSport = findViewById(R.id.ll_sport);
        llCircle = findViewById(R.id.ll_circle);
        llPerson = findViewById(R.id.ll_person);
        boomMenuButton = findViewById(R.id.main_tab_icon);
        imgUnRead = findViewById(R.id.dot_person);

        // 默认主场
        selectedFragment(0);
        mImmersionBar.fitsSystemWindows(false).statusBarColor(R.color.white).statusBarDarkFont(true).init();
        tabSelected(llHome);

        // 8个按钮添加进去
        for (int i = 0; i < 8; i++)
            boomMenuButton.addBuilder(BuilderManager.getTextOutsideCircleButtonBuilder());

        // 离底边位置
        boomMenuButton.setButtonBottomMargin(400);
        boomMenuButton.setButtonPlaceAlignmentEnum(ButtonPlaceAlignmentEnum.Bottom);

        // 对8个按钮进行排版布局
        float w = Util.dp2px(70);
        float h = Util.dp2px(84);
        float h_0_5 = h / 2;
        float w_1_5 = w * 1.5f;

        float hm = boomMenuButton.getButtonHorizontalMargin();
        float vm = boomMenuButton.getButtonVerticalMargin();
        float vm_0_5 = vm / 2;
        float hm_1_5 = hm * 1.5f;

        boomMenuButton.getCustomButtonPlacePositions().add(new PointF(-w_1_5 - hm_1_5, -h_0_5 - vm_0_5));
        boomMenuButton.getCustomButtonPlacePositions().add(new PointF(-w / 2 - hm / 2, -h_0_5 - vm_0_5));
        boomMenuButton.getCustomButtonPlacePositions().add(new PointF(w / 2 + hm / 2, -h_0_5 - vm_0_5));
        boomMenuButton.getCustomButtonPlacePositions().add(new PointF(w_1_5 + hm_1_5, -h_0_5 - vm_0_5));

        boomMenuButton.getCustomButtonPlacePositions().add(new PointF(-w_1_5 - hm_1_5, +h_0_5 + vm_0_5));
        boomMenuButton.getCustomButtonPlacePositions().add(new PointF(-w / 2 - hm / 2, +h_0_5 + vm_0_5));
        boomMenuButton.getCustomButtonPlacePositions().add(new PointF(w / 2 + hm / 2, +h_0_5 + vm_0_5));
        boomMenuButton.getCustomButtonPlacePositions().add(new PointF(w_1_5 + hm_1_5, +h_0_5 + vm_0_5));

        boomMenuButton.setOnBoomListener(new OnBoomListener() {
            @Override
            public void onClicked(int index, BoomButton boomButton) {
                if (userService.isLogin()) {
                    publishBehavior(index);
                } else {
                    NavUtils.gotoLoginActivity(HomeMainTabActivity.this);
                }
            }

            @Override
            public void onBackgroundClick() {
            }

            @Override
            public void onBoomWillHide() {

            }

            @Override
            public void onBoomDidHide() {
            }

            @Override
            public void onBoomWillShow() {
                GetMainPublishBootInfo(LocationManager.getInstance().getStorage().getManual_cityName().replace("市", ""), LocationManager.getInstance().getStorage().getManual_cityCode());
            }

            @Override
            public void onBoomDidShow() {
                //统计事件
                Analysis.pushEvent(mContext, AnEvent.SYMBOL);

                Analysis.pushEvent(HomeMainTabActivity.this, AnEvent.PublishOpen,
                        new Analysis.Builder().
                                setLat(LocationManager.getInstance().getStorage().getLast_lat()).
                                setLng(LocationManager.getInstance().getStorage().getLast_lng()));

            }
        });


        NativeUtils.parseNativeData(getIntent(), this);
        updateController = new UpdateController(this, mHandler);

        registerUpdateBroadCast();
        imServiceConnector.connect(this);
        //小红点
        refreshUnreadCount(SPUtils.getIntTextChanged(HomeMainTabActivity.this));
        if (SPUtils.isNeedOpenBombox(this)) {
            mHandler.sendEmptyMessageDelayed(MSG_SHOW_BOMBOX, 8000);
        }
        // 首页弹窗广告
        getHomePopup();

        root = findViewById(R.id.activity_content);
        root.setonLayoutKeyChange(b -> {
        });

//        full_screen_container = findViewById(R.id.full_screen_container);
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
    }

    @Override
    protected int setLayoutId() {
        return R.layout.ac_main_tab;
    }

    Dialog rebootServiceDlg;

    private void showServiceRestartDlg() {
        if (!NetworkUtil.isNetworkAvailable(this)) {
            return;
        }
        if (rebootServiceDlg == null) {
            rebootServiceDlg = DialogUtil.showMessageDialog(this,
                    null,
                    getString(R.string.label_dlg_msg_start_service),
                    getString(R.string.label_btn_start_service),
                    getString(R.string.label_btn_start_service_later),
                    v -> {
                        rebootServiceDlg.dismiss();
                        showLoadingView("");
                        updateController.doEditUserStatus(HomeMainTabActivity.this, UserStatus.ONLINE);
                    }, v -> rebootServiceDlg.dismiss());
        }
        rebootServiceDlg.show();
    }

    IntentFilter intentFilter = null;

    private void registerUpdateBroadCast() {
        if (intentFilter == null) {
            intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver(updateReceiver, intentFilter);
        }
    }

    private void unRegisterUpdateBroadCast() {
        if (intentFilter != null) {
            unregisterReceiver(updateReceiver);
            intentFilter = null;
        }
    }

    BroadcastReceiver updateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
                if (!NetworkUtil.isNetworkAvailable(HomeMainTabActivity.this)) {
                    return;
                }
                updateController.doGetUpdateData(HomeMainTabActivity.this);
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_home_court:
                selectedFragment(0);
                if (llHome.isSelected()) EventBus.getDefault().post(new EvBusWhenSelectFragment(0));
                tabSelected(llHome);
                mImmersionBar.fitsSystemWindows(false).transparentStatusBar().statusBarDarkFont(true).init();
                break;
            case R.id.ll_sport:
                selectedFragment(1);
                if (llSport.isSelected())
                    EventBus.getDefault().post(new EvBusWhenSelectFragment(1));
                tabSelected(llSport);
//                mImmersionBar.fitsSystemWindows(false).flymeOSStatusBarFontColor("#000000").init();
                mImmersionBar.fitsSystemWindows(false).transparentStatusBar().statusBarDarkFont(true).init();
                break;
            case R.id.ll_circle:
                selectedFragment(2);
                if (llCircle.isSelected())
                    EventBus.getDefault().post(new EvBusCircleTabRefresh());
                tabSelected(llCircle);
                mImmersionBar.fitsSystemWindows(true).statusBarColor(R.color.White).statusBarDarkFont(true).init();
                break;
            case R.id.ll_person:
                selectedFragment(3);
                if (llPerson.isSelected())
                    EventBus.getDefault().post(new EvBusWhenSelectFragment(3));
                tabSelected(llPerson);
                mImmersionBar.fitsSystemWindows(false).transparentStatusBar().statusBarDarkFont(true).init();
                break;
        }
    }

    /**
     * 切换到某个页面
     *
     * @param position
     */
    public synchronized void selectedFragment(int position) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideFragment(transaction);
        switch (position) {
            case 0:
                //统计事件
                Analysis.pushEvent(mContext, AnEvent.HOME);
                if (mHomeCourtFragment == null) {
                    mHomeCourtFragment = new NewHomeCourtFragment();
                    transaction.add(R.id.content, mHomeCourtFragment);
                } else
                    transaction.show(mHomeCourtFragment);
                break;
            case 1:
                //统计事件
                Analysis.pushEvent(mContext, AnEvent.ACTIVITY);
                if (mSportFragment == null) {
                    mSportFragment = new SportFragment();
                    transaction.add(R.id.content, mSportFragment);
                } else
                    transaction.show(mSportFragment);
                break;
            case 2:
                //统计事件
                Analysis.pushEvent(mContext, AnEvent.CIRCLE);
                if (newCircleFragment == null) {
                    newCircleFragment = YhyRouter.getInstance().makeCircleFragment();
                    transaction.add(R.id.content, newCircleFragment);
                } else
                    transaction.show(newCircleFragment);
                break;
            case 3:
                //统计事件
                Analysis.pushEvent(mContext, AnEvent.MINE);
                if (mPersonalFragment == null) {
                    mPersonalFragment = new PersonalFragment();
                    transaction.add(R.id.content, mPersonalFragment);
                } else
                    transaction.show(mPersonalFragment);
                break;
        }
        transaction.commit();
    }

    /**
     * 隐藏其他Fragment
     *
     * @param transaction
     */
    private void hideFragment(FragmentTransaction transaction) {
        if (mHomeCourtFragment != null)
            transaction.hide(mHomeCourtFragment);
        if (mSportFragment != null)
            transaction.hide(mSportFragment);
        if (newCircleFragment != null)
            transaction.hide(newCircleFragment);
        if (mPersonalFragment != null)
            transaction.hide(mPersonalFragment);
    }

    /**
     * 设置当前点击状态
     *
     * @param linearLayout 当前被点击view
     */
    public void tabSelected(View linearLayout) {
        llHome.setSelected(false);
        llSport.setSelected(false);
        llCircle.setSelected(false);
        llPerson.setSelected(false);
        linearLayout.setSelected(true);
    }

    /**
     * 刷新消息未读数
     */
    private void refreshUnreadCount(Boolean isClick) {
        if (imService != null) {
            imUnreadCount = imService.getUnReadMsgManager().getTotalUnreadCount();

            EventBus.getDefault().post(new EvBusMessageCount(imUnreadCount));
            showUnread(imUnreadCount > 0 || isClick);

            if (mHomeCourtFragment != null && mHomeCourtFragment.isAdded()) {
                mHomeCourtFragment.updateIMMessageCount(imUnreadCount);
            }

            if (mPersonalFragment != null && mPersonalFragment.isAdded()) {
                mPersonalFragment.updateIMMessageCount(imUnreadCount);
            }

            if (mSportFragment != null && mSportFragment.isAdded()) {
                mSportFragment.updateIMMessageCount(imUnreadCount);
            }

            if (newCircleFragment != null && newCircleFragment.isAdded()) {
                ((NewCircleFragment)newCircleFragment).updateIMMessageCount(imUnreadCount);
            }

        } else {
            showUnread(isClick);
        }
    }

    public void showUnread(boolean show) {
        imgUnRead.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    public void onEventMainThread(Boolean isClick) {
        showUnread(isClick);
    }

    public void onEvent(LoginEvent event) {
        switch (event) {
            case LOGIN_UT_OK:
                imService.getLoginManager().login(userService.getLoginUserId());
                break;
        }
    }

    private void getHomePopup() {
        NetManager.getInstance(getApplicationContext()).getHomePopup(new OnResponseListener<BoothList>() {
            @Override
            public void onComplete(boolean isOK, BoothList boothlist, int errorCode, String errorMsg) {
                // 广告弹窗页
                if (boothlist == null || boothlist.value == null || boothlist.value.size() == 0 || boothlist.value.get(0) == null || boothlist.value.get(0).showcases == null || boothlist.value.get(0).showcases.size() == 0) {
                    return;
                }
                // 24小时内只弹一次
                long lastTime = SPUtils.getLong(HomeMainTabActivity.this, "home_popup");
                long currentTime = System.currentTimeMillis();
                long interval = currentTime - lastTime;
                if (interval > 24 * 60 * 60 * 1000) {
                    ActivityPopupWindow activityPopupWindow = new ActivityPopupWindow(HomeMainTabActivity.this, boothlist.value.get(0).showcases.get(0));
                    activityPopupWindow.showOrDismiss(boomMenuButton);
                    SPUtils.save(HomeMainTabActivity.this, "home_popup", currentTime);
                }

            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {

            }
        });
    }

    // 未读消息
    public void onEventMainThread(UnreadEvent event) {
        switch (event.event) {
            case SESSION_READED_UNREAD_MSG:
            case UNREAD_MSG_LIST_OK:
            case UNREAD_MSG_RECEIVED:
                refreshUnreadCount(SPUtils.getIntTextChanged(HomeMainTabActivity.this));
                break;
        }
    }

    boolean isFirstCheck = true;

    @Override
    public void handleMessage(Message msg) {
        hideLoadingView();
        switch (msg.what) {
            case UpdateController.GET_UPDATE_OK:
                OnlineUpgrade onlineUpgrade = (OnlineUpgrade) msg.obj;
                if (!isFirstCheck && !onlineUpgrade.forceUpgrade) return;
                int serviceVersion;
                try {
                    serviceVersion = Integer.parseInt(onlineUpgrade.version);
                } catch (Exception e) {
                    return;
                }
                if (serviceVersion > LocalUtils.getAppVersionCode(this)) {
                    ActivityManager am = (ActivityManager) HomeMainTabActivity.this
                            .getSystemService(Context.ACTIVITY_SERVICE);
                    ComponentName cName = am.getRunningTasks(1).size() > 0 ? am
                            .getRunningTasks(1).get(0).baseActivity : null;
                    if (cName != null && cName.getClassName().equals(HomeMainTabActivity.class.getName())) {
                        if (am.getRunningTasks(1).get(0).topActivity.getClassName().equals(UpdateAcitivity.class.getName()))
                            return;
                        if (onlineUpgrade.forceUpgrade || isFirstCheck) {
                            Intent intent = new Intent(HomeMainTabActivity.this, UpdateAcitivity.class);
                            intent.putExtra(IntentConstant.EXTRA_ONLINE_UPGRADE, onlineUpgrade);
                            startActivity(intent);
                            isFirstCheck = false;
                        }
                    }
                }
                break;
            case ValueConstants.MSG_EDIT_USER_STATUS_OK:
                SPUtils.setManualUpdateServiceStatus(this, true);
                ToastUtil.showToast(this, getString(R.string.label_service_switch_on_toast));
                break;
            case ValueConstants.MSG_EDIT_USER_STATUS_KO:
                AndroidUtils.showToast(this, StringUtil.handlerErrorCode(this, msg.arg1));
                break;
            case ValueConstants.MSG_BATCH_GET_USER_STATUS_OK:
                UserStatusInfoList result = (UserStatusInfoList) msg.obj;
                if (result != null && result.value != null && result.value.size() > 0) {
                    boolean isServiceStarted = (result.value.get(0).status == UserStatus.ONLINE);
                    if (!isServiceStarted) {
                        showServiceRestartDlg();
                    }
                }
                break;
            case ValueConstants.MSG_BATCH_GET_USER_STATUS_KO:
                break;
            case MSG_SHOW_BOMBOX:
                SPUtils.setLastBomboxOpenTime(this, System.currentTimeMillis());
                NavUtils.gotoBomboxActivity(this);
                break;
            case LiveController.MSG_LIVE_HAS_ROOM_OK:
                Api_LIVE_LiveRoomHasOrNot hasLiveRoom = (Api_LIVE_LiveRoomHasOrNot) msg.obj;
                if (hasLiveRoom.hasRoomOrNot) {
                    IntentUtil.startPublishActivity(this);
                } else if (null != hasLiveRoom.msg && hasLiveRoom.msg.length() > 0) {
                    NavUtils.startWebview(this, "", hasLiveRoom.msg, 0);
                } else {
                    ToastUtil.showToast(this, "您没有直播权限!");
                }
                break;
            case LiveController.MSG_LIVE_HAS_ROOM_ERROR:
                ToastUtil.showToast(this, "请求直播权限失败!");
        }
    }

    private Runnable r_exit_confirm_reset_counter = () -> mClickBackCount = 0;

    /***********************************************************      Lifecycle        ************************************************************/

    @Override
    protected void onResume() {
        super.onResume();
        //每次都要重新获得一下位置
        boolean locationPermissionWhenOnResume = PermissionUtils.checkLocationPermission(this);
        if (locationPermissionWhenOnResume) {
            if (!locationPermissionWhenOnCreate) {
                locationPermissionWhenOnCreate = true;
                LocationManager.getInstance().startLocation(this);
            }
        } else {
            locationPermissionWhenOnCreate = false;
        }
        userID = userService.getLoginUserId();
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideLoadingView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (StepService.getInstance() != null) {
            StepService.getInstance().saveData();
        }
        if (webview != null){
            try{
                webview.destroy();
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        imServiceConnector.disconnect(this);
        unRegisterUpdateBroadCast();
        YmAnalyticsEvent.onEvent(this, AnalyDataValue.APP_OUT);
        YmAnalyticsEvent.onDestroy(this);

        if (getTagInfoListByTypeCall != null) {
            getTagInfoListByTypeCall.cancel();
            getTagInfoListByTypeCall = null;
        }

        if (queryGuidanceRecordCall != null) {
            queryGuidanceRecordCall.cancel();
            queryGuidanceRecordCall = null;
        }

//        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CameraManager.OPEN_CAMERA_CODE && resultCode == -1) {
            showLoadingView("图片处理中");
            mCameraHandler.forResult(requestCode, resultCode, data);
        } else if (requestCode == CameraManager.OPEN_USER_ALBUM && resultCode == -1) {
            mCameraHandler.forResult(requestCode, resultCode, data);
        } else if (requestCode == 2 && resultCode == -1) {
            String topic = data.getStringExtra(SPUtils.EXTRA_ADD_LIVE_LABEL);
            NavUtils.gotoAddLiveActivity(this, topic);
        } else if (requestCode == CameraManager.OPEN_USER_ALBUM && resultCode == CameraManager.GET_VIDEO) {
            NavUtils.gotoAddLiveActivity(this, (MediaItem) data.getParcelableExtra(SPUtils.EXTRA_VIDEO));
        } else if (IntentConstants.LOGIN_RESULT == requestCode && resultCode == -1) {
            initData();
        }
    }

    /***********************************************************      Logic Method        ************************************************************/

    CameraHandler mCameraHandler = new CameraHandler(this,
            (SelectMoreListener) pathList -> NavUtils.gotoAddLiveAcitivty(HomeMainTabActivity.this, LiveType.ADD_PICTURE, null, (ArrayList<MediaItem>) pathList));

    /**
     * buttom的点击
     *
     * @param index
     */
    public void publishBehavior(int index) {
        switch (index) {
            case 0://文字
                //统计事件
                Analysis.pushEvent(mContext, AnEvent.SYMBOL_TEXT_RELEASED);
                NavUtils.gotoAddLiveActivity(this);
                break;
            case 1://拍摄
                //统计事件
                Analysis.pushEvent(mContext, AnEvent.SYMBOL_FILM_RELEASED);
                try {
                    if (LocalUtils.isAlertMaxStorage()) {
                        ToastUtil.showToast(this, getString(R.string.label_toast_sdcard_unavailable));
                        return;
                    }
                    CameraOptions options = mCameraHandler.getOptions();
                    options.setOpenType(OpenType.OPEN_CAMERA);
                    mCameraHandler.process();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 2://相册
                //统计事件
                Analysis.pushEvent(mContext, AnEvent.SYMBOL_PHOTO_RELEASED);
                try {
                    CameraOptions options = mCameraHandler.getOptions();
                    options.setOpenType(OpenType.OPENN_USER_ALBUM);
                    options.setMaxSelect(ValueConstants.MAX_SELECT_PICTURE);
                    mCameraHandler.processWithMedia();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 3://小视频
                //统计事件
                Analysis.pushEvent(mContext, AnEvent.SYMBOL_VIDEO_RELEASED);
                Intent intent = new Intent(this, MediaRecorderActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(SPUtils.EXTRA_TYPE, 1);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case 4://直播
                //统计事件
                Analysis.pushEvent(mContext, AnEvent.SYMBOL_LIVE_RELEASED);
                //请求网络,判断是否有直播权限
                if (userService.isLogin()) {
                    mLiveController.doGetHasLiveRoomOrNot(this, mHandler, userService.getLoginUserId());
                } else {
                    NavUtils.gotoLoginActivity(this);
                }
                break;
            case 5://话题
                //统计事件
                Analysis.pushEvent(mContext, AnEvent.SYMBOL_TOPIC_RELEASED);
                NavUtils.gotoAddTopic(this, 2);
                break;
            case 6://俱乐部
                //统计事件
                Analysis.pushEvent(mContext, AnEvent.SYMBOL_CLUB_RELEASED);
                NavUtils.startWebview(this, "俱乐部", SPUtils.addClub(this), 0);
                break;
            case 7://活动
                //统计事件
                Analysis.pushEvent(mContext, AnEvent.SYMBOL_ACTIVITY_RELEASED);
                NavUtils.startWebview(this, "活动", SPUtils.getAddClubAct(this).trim() + "?list=true", 0);
                break;
        }
    }


    /***********************************************************      Network Api        ************************************************************/
    /**
     * 获取 发布页面的一些信息
     *
     * @param cityName
     * @param cityCode
     */
    private void GetMainPublishBootInfo(String cityName, String cityCode) {

        NetManager.getInstance(this).doGetMainPublishBootInfo(cityName, cityCode, new OnResponseListener<Api_RESOURCECENTER_PublishBootResult>() {
            @Override
            public void onComplete(boolean isOK, Api_RESOURCECENTER_PublishBootResult result, int errorCode, String errorMsg) {
                if (!isFinishing()) {
                    if (boomMenuButton.getMyBackgroundView() != null) {
                        boomMenuButton.getMyBackgroundView().setPublishInfo(result);
                    }
                }
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {

            }
        });
    }

    /**
     * 获取圈子标签页
     */
    public void getCircleTab() {
        //新的network第一个测试调用
        YhyCallback<Response<GetTagInfoListByTypeResp>> callback = new YhyCallback<Response<GetTagInfoListByTypeResp>>() {
            @Override
            public void onSuccess(Response<GetTagInfoListByTypeResp> data) {
                SPCache.saveCircleTabCache(HomeMainTabActivity.this, JSONUtils.toJson(data.getContent().getTagResultList()));
            }

            @Override
            public void onFail(YhyCode code, Exception exception) {
                //如果没有拉取到数据则，按照默认本地Tab默认
            }
        };
        getTagInfoListByTypeCall = new SnsCenterApi().
                getTagInfoListByType(new GetTagInfoListByTypeReq(3), callback).
                execAsync();
    }

    //某些系统做了恢复保存会导致一些Fragment状态的异常
    @SuppressLint("MissingSuperCall")
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //super.onSaveInstanceState(outState);
    }

//    //player
//    public static TXVodPlayer player;
//
//    public static TXVodPlayer getPlayer() {
//        return player;
//    }

//    public void fullScreen(String videoUrl, String videoPicUrl, String title) {
//        videoView = findViewById(R.id.fullscreen_video);
//        videoView.setUrlAndPlayer(videoUrl, videoPicUrl, title);
//        videoView.setFullScreenActivity(this);
//        videoView.isFullScreen = true;
//        VideoPlayer.getInstance().attach(videoView);
//        full_screen_container.setVisibility(View.VISIBLE);
//        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//        }
//        mImmersionBar.fitsSystemWindows(false).statusBarColor(R.color.Black).statusBarDarkFont(true).init();
//    }
//
//    public void exitFullScreen() {
////        videoView.resetAllState();
//        isFullScreen = false;
//        VideoPlayer.getInstance().deattach(videoView);
//        VideoPlayer.getInstance().reAttach();
//        full_screen_container.setVisibility(View.GONE);
//        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        }
//        mImmersionBar.fitsSystemWindows(true).statusBarColor(R.color.White).statusBarDarkFont(true).init();
//    }

    @Override
    public void onNewBackPressed() {

        if (mClickBackCount == 0) {
            AndroidUtils.showToast(this, R.string.back_twice_exit);
            mHandler.removeCallbacks(r_exit_confirm_reset_counter);
            mHandler.postDelayed(r_exit_confirm_reset_counter, 3 * 1000);
            mClickBackCount++;
        } else {
            Analysis.pushEvent(this, AnEvent.AppClose,
                    new Analysis.Builder().
                            setLat(LocationManager.getInstance().getStorage().getLast_lat()).
                            setLng(LocationManager.getInstance().getStorage().getLast_lng()));

            super.onNewBackPressed();
        }
    }
}
