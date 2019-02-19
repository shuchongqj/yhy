package com.newyhy.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.harwkin.nb.camera.FileUtil;
import com.mogujie.tt.imservice.service.IMService;
import com.newyhy.manager.ErrorManager;
import com.quanyan.base.util.LocalUtils;
import com.quanyan.yhy.common.DirConstants;
import com.quanyan.yhy.net.NetManager;
import com.quanyan.yhy.net.lsn.OnResponseListener;
import com.quanyan.yhy.net.utils.EnvConfig;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.onlineconfig.OnlineConfigAgent;
import com.umeng.socialize.PlatformConfig;
import com.yhy.common.base.YHYBaseApplication;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.types.AppDebug;
import com.yhy.common.utils.SPUtils;
import com.yhy.network.YhyCallback;
import com.yhy.network.YhyCode;
import com.yhy.network.api.ApiImp;
import com.yhy.network.req.device.RegisterReq;
import com.yhy.network.resp.Response;
import com.yhy.network.resp.device.RegisterResp;


public class InitializeService extends IntentService {

    private static final String ACTION_INIT_WHEN_APP_CREATE = "com.yhy.quanyan.service.action.INIT";

    public InitializeService() {
        super("InitializeService");
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, InitializeService.class);
        intent.setAction(ACTION_INIT_WHEN_APP_CREATE);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_INIT_WHEN_APP_CREATE.equals(action)) {
                performInit();
            }
        }
    }

    private void performInit() {
//        initWorkDirs();
        initUmengShare();
//        initJPush();
        OnlineConfigAgent.getInstance().updateOnlineConfig(this);
        initBugly();
        startIMService();
    }

    /**
     * 启动IM服务
     */
    private void startIMService() {
        Intent intent = new Intent();
        intent.setClass(this, IMService.class);
        startService(intent);
    }

    /**
     * bugly
     */
    private void initBugly() {
        if (YHYBaseApplication.getInstance().getYhyEnvironment().isOnline()) {
            // 正式
            CrashReport.initCrashReport(getApplicationContext(), "ee35bafd9f", false);
        } else {
            // 测试
            CrashReport.initCrashReport(getApplicationContext(), "5f5d36eb7e", false);

        }
    }



    /**
     * 初始化友盟分享
     */
    private void initUmengShare() {
        PlatformConfig.setWeixin(ValueConstants.WX_APPID, ValueConstants.WX_APPSECRET);
        PlatformConfig.setQQZone(ValueConstants.QQ_APPID, ValueConstants.QQ_APPKEY);
        PlatformConfig.setSinaWeibo(ValueConstants.Sina_APPID, ValueConstants.Sina_APPSECRET, "http://sns.whalecloud.com");
    }
}
