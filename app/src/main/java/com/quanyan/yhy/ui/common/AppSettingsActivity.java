package com.quanyan.yhy.ui.common;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Process;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.harwkin.nb.camera.FileUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.mogujie.tt.imservice.event.LoginEvent;
import com.newyhy.utils.SharedPreferenceUtil;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.util.LocalUtils;
import com.quanyan.base.util.StringUtil;
import com.quanyan.base.view.BaseNavView;
import com.quanyan.yhy.BaseApplication;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.DirConstants;
import com.quanyan.yhy.eventbus.EvBusChangePhone;
import com.quanyan.yhy.libanalysis.AnEvent;
import com.quanyan.yhy.libanalysis.Analysis;
import com.quanyan.yhy.net.NetManager;
import com.quanyan.yhy.net.lsn.OnResponseListener;
import com.quanyan.yhy.net.utils.EnvConfig;
import com.quanyan.yhy.ui.UpdateAcitivity;
import com.quanyan.yhy.ui.UpdateController;
import com.quanyan.yhy.ui.base.utils.DialogUtil;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.login.LoginActivity;
import com.quanyan.yhy.ui.views.AppSettingItem;
import com.yhy.common.base.YHYBaseApplication;
import com.yhy.common.beans.net.model.rc.OnlineUpgrade;
import com.yhy.common.constants.IntentConstant;
import com.yhy.common.constants.IntentConstants;
import com.yhy.common.types.AppDebug;
import com.yhy.common.utils.SPUtils;
import com.yhy.router.YhyRouter;
import com.yhy.service.IUserService;
import com.yixia.camera.util.Log;
import com.ymanalyseslibrary.AnalysesConstants;
import com.ymanalyseslibrary.tool.AnalyticUtils;

import java.io.File;

import de.greenrobot.event.EventBus;

import static com.umeng.commonsdk.stateless.UMSLEnvelopeBuild.mContext;

public class AppSettingsActivity extends BaseActivity implements View.OnClickListener {
    private static final int MSG_CLEAR_CACHE_OK = 0x1001;
    private static final int MSG_CLEAR_CACHE_KO = 0x1002;

    public static final int SETTING_RESULT = 100;

    //    @ViewInject(R.id.settings_common_visitors)
//    private AppSettingItem settings_common_visitors;
    @ViewInject(R.id.settings_common_addresses)
    private AppSettingItem settings_common_addresses;
    @ViewInject(R.id.settings_im_switch)
    private AppSettingItem settings_im_switch;
    @ViewInject(R.id.settings_tel)
    private AppSettingItem settings_tel;
    @ViewInject(R.id.settings_feedback)
    private AppSettingItem settings_feedback;
    @ViewInject(R.id.settings_about_us)
    private AppSettingItem settings_about_us;
    //手动升级
    @ViewInject(R.id.settings_upgrade)
    private AppSettingItem mSettingUpgrade;

    //密码管理
    //
    @ViewInject(R.id.settings_password)
    private AppSettingItem mSettingPassword;

    @ViewInject(R.id.settings_prasize_app)
    private AppSettingItem settings_prasize_app;
    @ViewInject(R.id.settings_clear_cache)
    private AppSettingItem settings_clear_cache;
    @ViewInject(R.id.settings_logout)
    private TextView settings_logout;
    @ViewInject(R.id.mine_information)
    private AppSettingItem mine_information;
    //环境切换
    @ViewInject(R.id.settings_switch_env)
    private TextView mTvSwitchEnv;

    @ViewInject(R.id.settings_imei)
    private TextView mTvImei;


    @ViewInject(R.id.settings_custom_service)
    private AppSettingItem settings_custom_service;

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.ac_app_settings, null);
    }

    private BaseNavView mBaseNavView;

    @Autowired
    IUserService userService;

    public IUserService getUserService() {
        return userService;
    }

    @Override
    public View onLoadNavView() {
        mBaseNavView = new BaseNavView(this);
        mBaseNavView.setTitleText(getString(R.string.title_settings));
        return mBaseNavView;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ViewUtils.inject(this);
        EventBus.getDefault().register(this);

        initViews();
    }

    @Override
    protected void onResume() {
        initDatas();
        super.onResume();
    }

    @Override
    public void handleMessage(Message msg) {
        hideLoadingView();
        switch (msg.what) {
            case MSG_CLEAR_CACHE_OK:
                hideLoadingView();
                settings_clear_cache.setSummary("0KB");
                toastShow();
                break;
            case MSG_CLEAR_CACHE_KO:
                hideLoadingView();
                ToastUtil.showToast(AppSettingsActivity.this, getString(R.string.toast_clear_cache_ko));
                break;
            case UpdateController.GET_UPDATE_OK:
                OnlineUpgrade onlineUpgrade = (OnlineUpgrade) msg.obj;
                handleUpgradeApp(onlineUpgrade, msg.arg1 != 0);
                break;
            case UpdateController.GET_UPDATE_KO:
                String strMsg = (String) msg.obj;
                if (StringUtil.isEmpty(strMsg)) {
                    ToastUtil.showToast(this, StringUtil.handlerErrorCode(this, msg.arg1));
                } else {
                    ToastUtil.showToast(this, strMsg);
                }
                break;
        }
    }

    private void toastShow() {
        CustomToastDialog myToast = new CustomToastDialog(this);
        myToast.setMessage(getString(R.string.toast_clear_cache_ok));
        myToast.showTime(3000);
        myToast.show();
    }

    /**
     * 处理升级
     */
    private void handleUpgradeApp(OnlineUpgrade onlineUpgrade, boolean justCheck) {
        int serviceVersion;
        try {
            serviceVersion = Integer.parseInt(onlineUpgrade.version);
        } catch (Exception e) {
            mSettingUpgrade.setSummary(getString(R.string.label_version_is_lastest));
//            SPUtils.save(SPUtils.TYPE_DEFAULT, this, SPUtils.KEY_VERSION_UPGRADE_MSG, getString(R.string.label_version_is_lastest));
            if (!justCheck){
                ToastUtil.showToast(this, R.string.label_version_is_lastest);
            }
            return;
        }
        if (serviceVersion > LocalUtils.getAppVersionCode(this)) {
            if (justCheck){
                mSettingUpgrade.setSummary(getString(R.string.label_find_new_version));
//            SPUtils.save(SPUtils.TYPE_DEFAULT, this, SPUtils.KEY_VERSION_UPGRADE_MSG, getString(R.string.label_version_is_lastest));
            }else {
                Intent intent = new Intent(AppSettingsActivity.this, UpdateAcitivity.class);
                intent.putExtra(IntentConstant.EXTRA_ONLINE_UPGRADE, onlineUpgrade);
                startActivity(intent);
            }

        } else {
            mSettingUpgrade.setSummary(getString(R.string.label_version_is_lastest));
//            SPUtils.save(SPUtils.TYPE_DEFAULT, this, SPUtils.KEY_VERSION_UPGRADE_MSG, getString(R.string.label_version_is_lastest));
            if (!justCheck){
                ToastUtil.showToast(this, R.string.label_toast_version_lastest);
            }
        }
    }


    private void initViews() {
        mine_information.setOnClickListener(this);

//        settings_common_visitors.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //TODO 常用旅客
//                NavUtils.gotoCommonUseTouristActivity(AppSettingsActivity.this, -1, TouristType.MIMETOURIST, null);
//            }
//        });

        settings_common_addresses.setOnClickListener(this);

        mSettingPassword.setOnClickListener(this);
        settings_im_switch.setOnClickListener(this);
        settings_tel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 电话
                LocalUtils.call(AppSettingsActivity.this, SPUtils.getServicePhone(AppSettingsActivity.this));
            }
        });

        settings_feedback.setOnClickListener(this);

        settings_about_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //统计事件
                Analysis.pushEvent(mContext, AnEvent.ABOUT_US);
                NavUtils.startWebview(AppSettingsActivity.this, "", SPUtils.getAboutUs(AppSettingsActivity.this), 0);
            }
        });
        //手动升级
        mSettingUpgrade.setOnClickListener(v -> {
            //TODO 手动升级
            //统计事件
            Analysis.pushEvent(mContext, AnEvent.VERSION_UPDATING);
            checkUpgrade(false);
        });
        checkUpgrade(true);

        mSettingUpgrade.showGoView(false);

        settings_clear_cache.setSummary(LocalUtils.getCacheSize(this));
        settings_clear_cache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //统计事件
                Analysis.pushEvent(mContext, AnEvent.CLEAR_CACHE);
                //TODO 清除缓存
                showLoadingView(getString(R.string.toast_clearing_cache));
                LocalUtils.clearCache(getApplicationContext(), new OnResponseListener<Boolean>() {
                    @Override
                    public void onComplete(boolean isOK, Boolean result, int errorCode, String errorMsg) {
                        if (isOK) {
                            Message.obtain(mHandler, MSG_CLEAR_CACHE_OK).sendToTarget();
                        } else {
                            Message.obtain(mHandler, MSG_CLEAR_CACHE_KO).sendToTarget();
                        }
                    }

                    @Override
                    public void onInternError(int errorCode, String errorMessage) {
                        Message.obtain(mHandler, MSG_CLEAR_CACHE_KO).sendToTarget();
                    }
                });
            }
        });
        settings_clear_cache.showGoView(false);

        settings_custom_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 我的客服
                //统计事件
                Analysis.pushEvent(mContext, AnEvent.CALL_CENTER);
                NavUtils.gotoServiceCenterActivity(AppSettingsActivity.this);
            }
        });


        settings_prasize_app.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //TODO 给应用评分
                //统计事件
                Analysis.pushEvent(mContext, AnEvent.FIVE_STAR);
                doPrasizeApp();
            }
        });

        settings_logout = findViewById(R.id.settings_logout);

        settings_logout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //统计事件
                Analysis.pushEvent(mContext, AnEvent.LOG_OUT);
                if (getUserService().isLogin()) {
                    doCheckLogoutDlg();
                } else {
                    NavUtils.gotoLoginActivity(AppSettingsActivity.this);
                }
            }
        });
        //当前测试开发环境
//        if (!AppDebug.IS_ONLINE) {
//            mTvSwitchEnv.setVisibility(View.VISIBLE);
//            mTvImei.setVisibility(View.VISIBLE);
            mTvSwitchEnv.setVisibility(View.GONE);
            mTvImei.setVisibility(View.GONE);
//            mTvImei.setText(AnalyticUtils.getDeviceIMEI(AppSettingsActivity.this));
//            mTvSwitchEnv.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    showSwitchEnvDialog();
//                }
//            });
//            if (EnvConfig.ENV_ONLINE.equals(SPUtils.getEnv(this))) {
//                //TODO 切换到线上环境
//                mTvSwitchEnv.setText(R.string.label_btn_switch_online);
//            } else if (EnvConfig.ENV_PRE_ONLINE.equals(SPUtils.getEnv(this))) {
//                //TODO 切换到预发环境
//                mTvSwitchEnv.setText(R.string.label_btn_switch_pre);
//            } else if (EnvConfig.ENV_TEST.equals(SPUtils.getEnv(this))) {
//                //TODO 切换到测试环境
//                mTvSwitchEnv.setText(R.string.label_btn_switch_test);
//            }
//        } else {
//            mTvSwitchEnv.setVisibility(View.GONE);
//            mTvImei.setVisibility(View.GONE);
//        }
    }

    /**
     * 检查升级
     */
    private void checkUpgrade(final boolean justCheck) {
        if (!justCheck){
            showLoadingView("");
        }

        NetManager.getInstance(this).doGetOnlineUpgrade(new OnResponseListener<OnlineUpgrade>() {
            @Override
            public void onComplete(boolean isOK, OnlineUpgrade upgradeInfo,
                                   int errorCode, String errorMsg) {
                if (isOK && upgradeInfo != null) {
                    Message message = Message.obtain(mHandler, UpdateController.GET_UPDATE_OK, upgradeInfo);
                    message.arg1 = justCheck ? 1 : 0;
                    message.sendToTarget();
                } else {
                    Message.obtain(mHandler, UpdateController.GET_UPDATE_KO, errorCode, 0, getString(R.string.label_toast_version_lastest)).sendToTarget();
                }
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                Message.obtain(mHandler, UpdateController.GET_UPDATE_KO, errorCode, 0, errorMessage).sendToTarget();
            }
        });
    }

    /**
     * 环境切换
     */
    private void showSwitchEnvDialog() {
        new AlertDialog.Builder(this)
                .setItems(R.array.env_switch_env_array, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doSwitchEnv(which);
                    }
                })
                .create()
                .show();
    }

    /**
     * 环境切换
     */
    private void doSwitchEnv(int which) {
        LocalUtils.clearLocalCache(this);
        //删除打点日志
        FileUtil.deleteFile(new File(DirConstants.DIR_LOGS, AnalysesConstants.ANALYSES_CACHE_FILE_NAME));
        switch (which) {
            case 0:
                SPUtils.setEnv(this, EnvConfig.ENV_ONLINE);
                break;
            case 1:
                SPUtils.setEnv(this, EnvConfig.ENV_PRE_ONLINE);
                break;
            case 2:
                SPUtils.setEnv(this, EnvConfig.ENV_TEST);
                break;
        }
        SPUtils.setLastUpdateScTime(this, 0);
        SPUtils.setLastUpdateWtkTime(this, 0);
        ((BaseApplication) getApplication()).exitAllActivity();
        finish();
        Process.killProcess(Process.myPid());
    }

    private void initDatas() {
//        settings_common_visitors.initItem(-1, R.string.title_common_visitors);
        settings_common_addresses.initItem(-1, R.string.title_common_addresses);
        settings_im_switch.initItem(-1, R.string.label_settings_im_switch);
        settings_tel.initItem(-1, R.string.label_settings_tel);
        settings_feedback.initItem(-1, R.string.label_settings_feedback);
        settings_feedback.setSummary(getString(R.string.label_sum_lets_to_be_better));
        settings_about_us.initItem(-1, R.string.label_settings_about_us);
        mSettingUpgrade.initItem(-1, R.string.label_settings_app_upgrade);
        settings_prasize_app.initItem(-1, R.string.label_settings_pround);
        settings_clear_cache.initItem(-1, R.string.label_settings_clear_cache);
        mine_information.initItem(-1, R.string.title_mine_information);
        mSettingPassword.initItem(-1, R.string.label_settings_account_security);
        settings_custom_service.initItem(-1, R.string.label_settings_custom_service);
        refreshLoginButtonStatus();
    }

    //意见反馈
    private void gotoFeedback() {
        NavUtils.gotoFeedbackAcivity(this);
    }

    /**
     * 弹出退出登录的确认对话框
     */
    Dialog mLogoutDlg;

    private void doCheckLogoutDlg() {
        if (mLogoutDlg == null) {
            mLogoutDlg = DialogUtil.showMessageDialog(this,
                    null,
                    getString(R.string.label_dlg_msg_sure_logout),
                    getString(R.string.label_btn_ok),
                    getString(R.string.label_btn_cancel),
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mLogoutDlg.dismiss();
                            doLogout();
                        }
                    },
                    null);
        }
        mLogoutDlg.show();
    }

    /**
     * 退出账号
     */
    private void doLogout() {
        showLoadingView(getString(R.string.dlg_msg_logout));
        NetManager.getInstance(this).doLogout(new OnResponseListener<Boolean>() {
            @Override
            public void onInternError(int errorCode,
                                      String errorMessage) {
                hideLoadingView();
                logout();
            }

            @Override
            public void onComplete(boolean isOK,
                                   Boolean isLogoutOk,
                                   int errorCode,
                                   String errorMsg) {
                hideLoadingView();
                logout();
            }
        });

    }

    private void logout(){
        LocalUtils.JumpToLogin(YHYBaseApplication.getInstance());
        NetManager.getInstance(YHYBaseApplication.getInstance()).changeLogoutStatus();
        NetManager.getInstance(YHYBaseApplication.getInstance()).release();

        refreshLoginButtonStatus();
        SharedPreferenceUtil.saveInterestSkip(false);//重置偏好数据
        //发送注销 通知im服务器
        EventBus.getDefault().post(LoginEvent.LOGIN_OUT);
    }

    /**
     * 更新按钮的登录状态
     */
    private void refreshLoginButtonStatus() {
        if (isFinishing()){
            return;
        }
        if (getUserService().isLogin()) {
            settings_logout.setText(R.string.label_btn_logout);
        } else {
            settings_logout.setText(R.string.label_btn_login);
        }
    }

    /**
     * 给程序员加油
     */
    private void doPrasizeApp() {
        NavUtils.gotoMarketSupport(this);
    }


    //返回更新我的主界面 by shenwenjie
    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        super.onBackPressed();
    }

    public void onEvent(EvBusChangePhone event) {
        finish();
    }

    @Override
    public void onClick(View v) {
        if (!isLoggedIn()) {    //去登录
            YhyRouter.getInstance().startLoginActivity(this, null, Intent.FLAG_ACTIVITY_CLEAR_TOP, IntentConstants.LOGIN_RESULT);
            return;
        }
        switch (v.getId()) {
            case R.id.mine_information:
                //TODO 个人资料
                //统计事件
                Analysis.pushEvent(mContext, AnEvent.PERSONAL_DATA);
                NavUtils.gotoUserInfoEditActivity(AppSettingsActivity.this);
                break;
            case R.id.settings_common_addresses:
                //TODO 常用地址
                //统计事件
                Analysis.pushEvent(mContext, AnEvent.DELIVERY_ADDRESS);
                NavUtils.startWebview(AppSettingsActivity.this, "", SPUtils.getURL_RECEIVE_ADDRESS(AppSettingsActivity.this), 0);
                break;
            case R.id.settings_password:
                //TODO 密码管理
                //统计事件
                Analysis.pushEvent(mContext, AnEvent.PASSWORD_MANAGEMENT);
                NavUtils.gotoPassWordManagerActivity(AppSettingsActivity.this);
                break;
            case R.id.settings_im_switch:
                //TODO 新消息提醒
                //统计事件
                Analysis.pushEvent(mContext, AnEvent.NEW_MESSAGE);
                NavUtils.gotoMessageNotificationSettingActivity(AppSettingsActivity.this);
                break;
            case R.id.settings_feedback:
                //TODO 反馈
                //统计事件
                Analysis.pushEvent(mContext, AnEvent.FEEDBACK);
                gotoFeedback();
                break;
        }
    }

    private boolean isLoggedIn() {
        if (userService.isLogin()) {
            return true;
        } else {
            YhyRouter.getInstance().startLoginActivity(this, null, Intent.FLAG_ACTIVITY_CLEAR_TOP, IntentConstants.LOGIN_RESULT);

            return false;
        }
    }

}
