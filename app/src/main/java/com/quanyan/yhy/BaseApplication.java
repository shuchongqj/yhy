package com.quanyan.yhy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;

import com.harwkin.nb.camera.FileUtil;
import com.lidroid.xutils.util.LogUtils;
import com.meituan.android.walle.ChannelInfo;
import com.meituan.android.walle.WalleChannelReader;
import com.mogujie.tt.ui.activity.ChatAcitivity;
import com.mogujie.tt.ui.activity.MessageActivity;
import com.mogujie.tt.ui.activity.MessageNotificationSettingActivity;
import com.mogujie.tt.ui.activity.NotificationListActivity;
import com.newyhy.activity.HomeMainTabActivity;
import com.newyhy.activity.NewPushStreamHorizontalActivity;
import com.newyhy.activity.NewPushStreamVerticalActivity;
import com.newyhy.service.InitializeService;
import com.quanyan.pedometer.newpedometer.StepService;
import com.quanyan.yhy.common.DirConstants;
import com.quanyan.yhy.libanalysis.AnCache;
import com.quanyan.yhy.libanalysis.AnEvent;
import com.quanyan.yhy.libanalysis.Analysis;
import com.quanyan.yhy.libanalysis.AnalysisModuleApplication;
import com.quanyan.yhy.net.utils.EnvSettings;
import com.quanyan.yhy.pay.PayActivity;
import com.quanyan.yhy.ui.consult.ConsultMasterReciveDialogActivity;
import com.quanyan.yhy.ui.consult.ConsultUserReplyDialogActivity;
import com.quanyan.yhy.ui.consult.MasterConsultActivity;
import com.quanyan.yhy.ui.consult.QuickConsultActivity;
import com.quanyan.yhy.ui.coupon.CouponActivity;
import com.quanyan.yhy.ui.integralmall.activity.MyOrderListActivity;
import com.quanyan.yhy.ui.master.activity.AttentionListActivity;
import com.quanyan.yhy.ui.mine.activity.UserInfoUpdateActivity;
import com.quanyan.yhy.ui.order.LineOrderActivity;
import com.quanyan.yhy.ui.order.OrderConfigActivity;
import com.quanyan.yhy.ui.order.PointOrderActivity;
import com.quanyan.yhy.ui.servicerelease.ExpertOrderDetailActivity;
import com.quanyan.yhy.ui.servicerelease.ExpertOrderListActivity;
import com.quanyan.yhy.ui.servicerelease.ManageServiceInfoAcitvity;
import com.quanyan.yhy.ui.servicerelease.PictureAndTextActivity;
import com.quanyan.yhy.ui.servicerelease.ReleaseServiceActivity;
import com.quanyan.yhy.ui.signed.activity.IntegralActivity;
import com.quanyan.yhy.ui.tab.homepage.order.NormalOrderDetailsActivity;
import com.quanyan.yhy.ui.tab.homepage.order.TravelOrderDetailsActivity;
import com.quanyan.yhy.ui.wallet.activity.BindCardActivity;
import com.quanyan.yhy.ui.wallet.activity.BindCardInforActivity;
import com.quanyan.yhy.ui.wallet.activity.BindCradVCodeActivity;
import com.quanyan.yhy.ui.wallet.activity.DetailAccountActivity;
import com.quanyan.yhy.ui.wallet.activity.ForgetPasBindCardActivity;
import com.quanyan.yhy.ui.wallet.activity.ForgetPasSelectCardActivity;
import com.quanyan.yhy.ui.wallet.activity.PayPassWordManagerActivity;
import com.quanyan.yhy.ui.wallet.activity.PrepaidOutAndInListActivity;
import com.quanyan.yhy.ui.wallet.activity.RealNameAuthActivity;
import com.quanyan.yhy.ui.wallet.activity.RechargeActivity;
import com.quanyan.yhy.ui.wallet.activity.SelectCardActivity;
import com.quanyan.yhy.ui.wallet.activity.SettingPayPassActivity;
import com.quanyan.yhy.ui.wallet.activity.UpdatePassWordActivity;
import com.quanyan.yhy.ui.wallet.activity.VerifyPassWordActivity;
import com.quanyan.yhy.ui.wallet.activity.WalletActivity;
import com.quanyan.yhy.ui.wallet.activity.WithDrawActivity;
import com.quanyan.yhy.ui.wallet.activity.WithDrawDetailsActivity;
import com.videolibrary.puhser.activity.PublishLiveActivity;
import com.yhy.cityselect.CitySelectModuleApplication;
import com.yhy.common.base.ModuleApplication;
import com.yhy.common.base.YHYBaseApplication;
import com.yhy.common.base.YhyEnvironment;
import com.yhy.common.types.AppDebug;
import com.yhy.common.utils.JSONUtils;
import com.yhy.imageloader.ImageLoaderModuleApplication;
import com.yhy.location.LocationManager;
import com.yhy.location.LocationModuleApplication;
import com.yhy.network.NetworkModuleApplication;
import com.yhy.network.req.log.LogUploadReq;
import com.yhy.push.PushModuleApplication;
import com.yhy.router.RouterApplication;
import com.yhy.topic.TopicModuleApplication;
import com.yixia.camera.VCamera;

import java.io.File;
import java.util.List;


public class BaseApplication extends YHYBaseApplication {
    static{
        AppDebug.init(BuildConfig.ENV_TYPE);



        registModule(new RouterApplication());
        registModule(new NetworkModuleApplication());
        registModule(new AnalysisModuleApplication());
        registModule(new LocationModuleApplication());
        registModule(new PushModuleApplication());
        registModule(new ImageLoaderModuleApplication());
        registModule(new TopicModuleApplication());
        registModule(new CitySelectModuleApplication());
//        registModule(new SportModuleApplication());
    }

    public static boolean gifRunning = true;

    @Override
    public void onCreate() {
        if (isMainProcess(this)){
            initWorkDirs();

            // 配置读取
            EnvSettings.read(getApplicationContext(), getAssets());

            super.onCreate();
            if (AppDebug.DEVELOPER_MODE) {
                StrictMode.VmPolicy.Builder builder1 = new StrictMode.VmPolicy.Builder();
                builder1.detectActivityLeaks()
                        .penaltyDropBox()
                        .penaltyDeath();
                StrictMode.setVmPolicy(builder1.build());
            }
            startService(new Intent(this, StepService.class));

            registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks);

            File dcim = new File(DirConstants.DIR_VIDEOS);
            if (!dcim.exists()) {
                if (!dcim.mkdirs()) {
                    Log.e("TAG", "Directory not created");
                }
            }
            VCamera.setVideoCachePath(dcim +File.separator);
            VCamera.setDebugMode(true);
            VCamera.initialize(this);

            InitializeService.start(this);
        }else {
            super.onCreate();
        }

    }

    @Override
    public YhyEnvironment getYhyEnvironment() {
        String channel = "Yhy";
        ChannelInfo channelInfo = WalleChannelReader.getChannelInfo(this);
        if (channelInfo != null) {
            channel = channelInfo.getChannel();
        }
        return new YhyEnvironment(BuildConfig.ENV_TYPE, channel);
    }


    /**
     * 踢出时候需要被关闭的界面
     */
    public static String[] ACTIVITY_CLOSE_PAGES = new String[]{
            PublishLiveActivity.class.getSimpleName(),
            NewPushStreamVerticalActivity.class.getSimpleName(),
            NewPushStreamHorizontalActivity.class.getSimpleName(),
            PayActivity.class.getSimpleName(),
            ChatAcitivity.class.getSimpleName(),
            NotificationListActivity.class.getSimpleName(),
            MessageActivity.class.getSimpleName(),
            TravelOrderDetailsActivity.class.getSimpleName(),
            MessageNotificationSettingActivity.class.getSimpleName(),
            LineOrderActivity.class.getSimpleName(),
            OrderConfigActivity.class.getSimpleName(),
            UserInfoUpdateActivity.class.getSimpleName(),
            NormalOrderDetailsActivity.class.getSimpleName(),
            QuickConsultActivity.class.getSimpleName(),
            MasterConsultActivity.class.getSimpleName(),
            ConsultMasterReciveDialogActivity.class.getSimpleName(),
            ConsultUserReplyDialogActivity.class.getSimpleName(),
            ReleaseServiceActivity.class.getSimpleName(),
            PictureAndTextActivity.class.getSimpleName(),
            ExpertOrderListActivity.class.getSimpleName(),
            MyOrderListActivity.class.getSimpleName(),
            ManageServiceInfoAcitvity.class.getSimpleName(),
            ExpertOrderDetailActivity.class.getSimpleName(),
            AttentionListActivity.class.getSimpleName(),
            PointOrderActivity.class.getSimpleName(),
            CouponActivity.class.getSimpleName(),
            IntegralActivity.class.getSimpleName(),
            BindCardActivity.class.getSimpleName(),
            BindCardInforActivity.class.getSimpleName(),
            BindCradVCodeActivity.class.getSimpleName(),
            DetailAccountActivity.class.getSimpleName(),
            ForgetPasBindCardActivity.class.getSimpleName(),
            ForgetPasSelectCardActivity.class.getSimpleName(),
            PayPassWordManagerActivity.class.getSimpleName(),
            PrepaidOutAndInListActivity.class.getSimpleName(),
            RealNameAuthActivity.class.getSimpleName(),
            RechargeActivity.class.getSimpleName(),
            SelectCardActivity.class.getSimpleName(),
            SettingPayPassActivity.class.getSimpleName(),
            UpdatePassWordActivity.class.getSimpleName(),
            VerifyPassWordActivity.class.getSimpleName(),
            WalletActivity.class.getSimpleName(),
            WithDrawActivity.class.getSimpleName(),
            WithDrawDetailsActivity.class.getSimpleName()
    };

    /**
     * 退出需要登录后才能显示的页面
     */
    public void exitNeedLoginActivity() {
        Activity activity;
        for (int i = (activityList.size() - 1); i >= 0; i--) {
            activity = activityList.get(i);
            if (activity instanceof AttentionListActivity) {
                AttentionListActivity.AttenttionType attentionType = ((AttentionListActivity) activity).getAttenttionType();
                if (attentionType == AttentionListActivity.AttenttionType.MY_FANS || attentionType == AttentionListActivity.AttenttionType.MY_ATTENTION) {
                    activityList.remove(activity);
                    activity.finish();
                    continue;
                }
            }
            for (String str : ACTIVITY_CLOSE_PAGES) {
                if (activity != null && activity.getClass().getSimpleName().equals(str)) {
                    activityList.remove(activity);
                    activity.finish();
                    break;
                }
            }
        }
    }

    /**
     * 踢出时候需要被关闭的界面
     */
    public static String GONAACTIVIT = HomeMainTabActivity.class.getSimpleName();

    public void gotoGonaActivity() {
        Activity activity;
        for (int i = (activityList.size() - 1); i >= 0; i--) {
            activity = activityList.get(i);
            if (activity != null && !activity.getClass().getSimpleName().equals(GONAACTIVIT)) {
                activity.finish();
            }
        }
        activityList.clear();
    }



    public int count = 0;

    private ActivityLifecycleCallbacks mActivityLifecycleCallbacks = new ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        }

        @Override
        public void onActivityStarted(Activity activity) {
            LogUtils.v(activity + "onActivityStarted");
            if (count == 0) {
                LogUtils.v(">>>>>>>>>>>>>>>>>>>切到前台  lifecycle");
                // 埋点
                Analysis.pushEvent(getApplicationContext(), AnEvent.AppOpen,
                        new Analysis.Builder().
                                setLat(LocationManager.getInstance().getStorage().getLast_lat()).
                                setLng(LocationManager.getInstance().getStorage().getLast_lng()));

                // 每次启动进入前台都去把本地已存的上传
                String logsJson = AnCache.getAnCacheCircleLogs(getApplicationContext());
                if (!TextUtils.isEmpty(logsJson)) {
                    List<LogUploadReq.Log> cachedlogs = JSONUtils.convertToArrayList(logsJson, LogUploadReq.Log.class);
                    if (cachedlogs != null && cachedlogs.size() > 0) {
                        Analysis.upLoadLogs(getApplicationContext(), cachedlogs, false);

                    }
                }

                // 每次启动进入前台都去把本地失败的缓存上传
                String dailedLogsJson = AnCache.getAnCacheCircleFailedLogs(getApplicationContext());
                if (!TextUtils.isEmpty(dailedLogsJson)) {
                    List<LogUploadReq.Log> failedLogs = JSONUtils.convertToArrayList(dailedLogsJson, LogUploadReq.Log.class);
                    if (failedLogs != null && failedLogs.size() > 0) {
                        Analysis.upLoadLogs(getApplicationContext(), failedLogs, true);

                    }
                }
            }
            count++;
        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {
            LogUtils.v(activity + "onActivityStopped");
            count--;
            if (count == 0) {
                LogUtils.v(">>>>>>>>>>>>>>>>>>>切到后台  lifecycle");
                // 埋点
                Analysis.pushEvent(getApplicationContext(), AnEvent.AppClose,
                        new Analysis.Builder().
                                setLat(LocationManager.getInstance().getStorage().getLast_lat()).
                                setLng(LocationManager.getInstance().getStorage().getLast_lng()));


            }
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    };

    public static String[] WalletActivitys = new String[]{
            SelectCardActivity.class.getSimpleName(), BindCardActivity.class.getSimpleName(), BindCardInforActivity.class.getSimpleName()
    };

    public void exitBindCard() {
        Activity activity;
        for (int i = (activityList.size() - 1); i >= 0; i--) {
            activity = activityList.get(i);
            for (String str : WalletActivitys) {
                if (activity != null && activity.getClass().getSimpleName().equals(str)) {
                    activityList.remove(activity);
                    activity.finish();
                    break;
                }
            }
        }
    }

    public static String[] WalletPasActivity = new String[]{
            BindCardActivity.class.getSimpleName(), ForgetPasBindCardActivity.class.getSimpleName()
    };

    public void exitForBindCard() {
        Activity activity;
        for (int i = (activityList.size() - 1); i >= 0; i--) {
            activity = activityList.get(i);
            for (String str : WalletPasActivity) {
                if (activity != null && activity.getClass().getSimpleName().equals(str)) {
                    activityList.remove(activity);
                    activity.finish();
                    break;
                }
            }
        }
    }

    public Activity getActivity(String simpleName) {
        for (int i = 0; i < activityList.size(); i++) {
            if (activityList.get(i).getClass().getSimpleName().equals(simpleName)) {
                return activityList.get(i);
            }
        }
        return null;
    }

    //初始化文件夹
    private void initWorkDirs() {
        FileUtil.mkdirs(DirConstants.DIR_WORK);
        FileUtil.mkdirs(DirConstants.DIR_PIC_THUMB);
        FileUtil.mkdirs(DirConstants.DIR_PIC_SHARE);
        FileUtil.mkdirs(DirConstants.DIR_LOGS);
        FileUtil.mkdirs(DirConstants.DIR_CACHE_BIG_PIC);
        FileUtil.mkdirs(DirConstants.DIR_CACHE_SMALL_PIC);
        FileUtil.mkdirs(DirConstants.DIR_VIDEOS);
        FileUtil.mkdirs(DirConstants.DIR_VIDEOS_CACHE);
        FileUtil.mkdirs(com.yhy.common.DirConstants.DIR_THEMES);
    }
}
