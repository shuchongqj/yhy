package com.newyhy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.mogujie.tt.DB.DBInterface;
import com.mogujie.tt.protobuf.helper.ProtoBuf2JavaBean;
import com.mogujie.tt.ui.activity.ChatAcitivity;
import com.newyhy.utils.app_utils.AppInfoUtils;
import com.yhy.common.utils.NetworkUtil;
import com.quanyan.base.util.StringUtil;
import com.quanyan.pedometer.newpedometer.StepService;
import com.quanyan.pedometer.utils.PreferencesUtils;

import com.quanyan.yhy.R;
import com.quanyan.yhy.database.DBManager;
import com.quanyan.yhy.libanalysis.AnEvent;
import com.quanyan.yhy.libanalysis.Analysis;
import com.quanyan.yhy.net.NetManager;
import com.quanyan.yhy.net.lsn.OnResponseListener;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.common.city.utils.PrefUtil;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.yhy.common.base.BaseNewActivity;
import com.yhy.common.beans.net.model.AppDefaultConfig;
import com.yhy.common.beans.net.model.pedometer.PedometerUserInfo;
import com.yhy.common.beans.net.model.user.UserInfo;
import com.yhy.common.constants.IntentConstants;
import com.yhy.common.utils.AndroidUtils;
import com.yhy.common.utils.SPUtils;
import com.yhy.location.LocationManager;
import com.yhy.service.IUserService;

import org.json.JSONException;

public class SplashActivity extends BaseNewActivity {

    // 启动页面，最多停留三秒，如果没网直接进入下个界面，如有有网弱网最多三秒自动进入下个页面
    private boolean isRegister;
    private boolean isSystemConfig;
    private boolean isPedometer;
    private boolean fromOut;

    @Autowired
    IUserService userService;

    @Override
    protected int setLayoutId() {
        return 0;
    }

    @Override
    public void handleMessage(Message msg) {
        mHandler.postDelayed(mRunnable, 3000);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }

        Analysis.pushEvent(this, AnEvent.AppOpen,
                new Analysis.Builder().
                        setLat(LocationManager.getInstance().getStorage().getLast_lat()).
                        setLng(LocationManager.getInstance().getStorage().getLast_lng()));



    }

    @Override
    protected void initData() {
        super.initData();
        PrefUtil.putIsFirstStart(SplashActivity.this, true);

        String action = getIntent().getAction();
        if (Intent.ACTION_VIEW.equals(action)) {
            fromOut = true;
        }

        if (NetworkUtil.isNetworkAvailable(this)) {
            mHandler.postDelayed(mRunnable, 1000);
            init();
        } else {
            ToastUtil.showToast(this, "当前网络未连接");
            startToPage();
        }
    }

    private Runnable mRunnable = () -> startToPage();

    private void startToPage() {
        int mStartType = getIntent().getIntExtra(SPUtils.EXTRA_TYPE, -1);
        if (mStartType == IntentConstants.START_FROM_NOTIFICATION) {
            Intent intent = getIntent();
            intent.putExtra("isFromGonaActivity", true);
            intent.setClass(this, ChatAcitivity.class);
            startActivity(intent);
        }
        finishSplash();
    }

    private void init() {
        initSystemConfig();
        getUserProfile();
    }

    /**
     * 获取系统配置项目
     */
    private void initSystemConfig() {
        NetManager.getInstance(getApplicationContext()).doInitApp(new OnResponseListener<AppDefaultConfig>() {
            @Override
            public void onComplete(boolean isOK, AppDefaultConfig result, int errorCode, String errorMsg) {
//                Logger.e("doInitApp" + "获取成功");
                isSystemConfig = true;
                boolean updateAll = true;
                if (isOK && result != null) {
                    if (result.getSystemConfig() != null) {
                        if (result.getSystemConfig().systemConfigList != null && result.getSystemConfig().systemConfigList.size() > 0) {
                            DBManager.getInstance(getApplicationContext()).updateSysConfigs(result.getSystemConfig().systemConfigList);
                        } else {
                            updateAll = false;
                        }
                    } else {
                        updateAll = false;
                    }

                    if (result.getComIconList() != null && result.getComIconList().comIconInfoList.size() > 0) {
                        DBManager.getInstance(getApplicationContext()).updateComIcons(result.getComIconList());
                    } else {
                        updateAll = false;
                    }

                    //TODO 更新广告信息
                    if (result.getAdBooth() != null) {
                        DBManager.getInstance(getApplicationContext()).updateADInfo(result.getAdBooth());

                    } else {
                        updateAll = false;
                    }

                    // 首页弹窗
                    if (result.getmAdMultiBooth() != null) {
                        DBManager.getInstance(getApplicationContext()).updateADMultiInfo(result.getmAdMultiBooth());

                    }

                    //TODO 更新达人TAB标题
                    if (result.getAdBooth() != null) {
                        DBManager.getInstance(getApplicationContext()).updateMasterTabInfo(result.getMasterBooth());
                    } else {
                        updateAll = false;
                    }

                    if (updateAll) {
                        SPUtils.setLastUpdateScTime(getApplicationContext(), System.currentTimeMillis());
                    }
                }
                netRequestFinish();
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
//                Logger.e("doInitApp" + "接口失败" + errorMessage + errorCode);
                isSystemConfig = true;
                netRequestFinish();
            }
        });
    }

    /**
     * 获取用户信息
     */
    private void getUserProfile() {
        if (userService.isLogin()) {
            NetManager.getInstance(this).doGetUserProfile(userService.getLoginUserId(), new OnResponseListener<UserInfo>() {
                @Override
                public void onComplete(boolean isOK, UserInfo result, int errorCode, String errorMsg) {
                    if (isOK && result != null) {
                        DBInterface.instance().initDbHelp(getApplicationContext(), result.id);
                        DBInterface.instance().insertOrUpdateUser(ProtoBuf2JavaBean.getUserEntity(result));

                        DBManager.getInstance(getApplicationContext()).saveUserInfo(result);
                    }
                    netRequestFinish();
                }

                @Override
                public void onInternError(int errorCode, String errorMessage) {
//                    Logger.e("doGetUserProfile" + "接口失败" + errorMessage + errorCode);
                    netRequestFinish();
                }
            });
        }
        try {
//            Logger.e("doGetPedometerUserInfo" + "开始获取");
            NetManager.getInstance(this).doGetPedometerUserInfo(new OnResponseListener<PedometerUserInfo>() {
                @Override
                public void onComplete(boolean isOK, PedometerUserInfo result, int errorCode, String errorMsg) {
//                    Logger.e("doGetPedometerUserInfo" + "获取成功");
                    isPedometer = true;
                    if (isOK && result != null) {
                        PreferencesUtils.putInt(getApplicationContext(), StepService.PEDOMETER_USER_INFO_AGE, result.age);
                        PreferencesUtils.putInt(getApplicationContext(), StepService.PEDOMETER_USER_INFO_HEIGHT, result.height);
                        PreferencesUtils.putInt(getApplicationContext(), StepService.PEDOMETER_USER_INFO_WEIGHT, result.weight);
                        PreferencesUtils.putLong(getApplicationContext(), StepService.LASTSYNTIME, result.syncDate);
                        if (result.trackConfig != null) {
                            PreferencesUtils.putString(getApplicationContext(), StepService.PEDOMETER_SHARE_WEIBO_TOPIC_NAME, result.trackConfig.weiboTopicName);
                            PreferencesUtils.putString(getApplicationContext(), StepService.PEDOMETER_SHARE_MASTER_CIRCLE_TOPIC_NAME, result.trackConfig.topicName);
                            PreferencesUtils.putString(getApplicationContext(), StepService.PEDOMETER_SAHRE_URL_QR_CODE, result.trackConfig.qrCodeUrl);
                            PreferencesUtils.putInt(getApplicationContext(), StepService.PEDOMETER_VIEW_MAX_STEPS, result.trackConfig.maxStepCircle);
                            if (!StringUtil.isEmpty(result.trackConfig.shareText)) {
                                PreferencesUtils.putString(getApplicationContext(), StepService.PEDOMETER_SAHRE_TEXT, result.trackConfig.shareText);
                            }
                        }
                        if (result.pointConfig != null) {
                            PreferencesUtils.putString(getApplicationContext(), StepService.PEDOMETER_POINT_COPY, result.pointConfig.toString);
                        }
                    }

                    netRequestFinish();
                }

                @Override
                public void onInternError(int errorCode, String errorMessage) {
                    isPedometer = true;
                    netRequestFinish();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void netRequestFinish() {
        if (isRegister && isSystemConfig && isPedometer) {
            startToPage();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            isRegister = true;
        } else {
            finishSplash();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRunnable != null) {
            mHandler.removeCallbacks(mRunnable);
        }
    }

    private boolean needFinish = true;//标记值，finishSplash()函数 直走一次

    private void finishSplash() {
        if (fromOut) {     // 从外部打开app
            Intent intent = new Intent(this, HomeMainTabActivity.class);
            intent.setData(getIntent().getData());
            intent.setAction(Intent.ACTION_VIEW);
            startActivity(intent);
//            NativeUtils.parseNativeData(getIntent(), this);
            finish();
            return;
        }

        if (needFinish) {
            String new_version = AndroidUtils.getVersion(this);
            String old_version = PreferencesUtils.getString(this, AppInfoUtils.Current_Version);
            // 大版本更新跳入引导页面
            if (AppInfoUtils.compareVersion(new_version, old_version) == AppInfoUtils.UPDATE_LARGE_VERSION) {
                NavUtils.gotoGuideActivty(SplashActivity.this);
            } else {
                //解决跳转白屏
                Intent intentIndex = new Intent(this, HomeMainTabActivity.class);
                Intent intentAdvertisement = new Intent(this, NewAdActivity.class);
                Intent[] intents = new Intent[2];
                intents[0] = intentIndex;
                intents[1] = intentAdvertisement;
                startActivities(intents);
                overridePendingTransition(R.anim.anim_pop_in, R.anim.anim_pop_out);
            }
            needFinish = false;
            finish();
            overridePendingTransition(R.anim.anim_pop_in, R.anim.anim_pop_out);
        }
    }
}