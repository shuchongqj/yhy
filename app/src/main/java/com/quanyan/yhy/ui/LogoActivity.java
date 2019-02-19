package com.quanyan.yhy.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.harwkin.nb.camera.ImageUtils;
import com.mogujie.tt.DB.DBInterface;
import com.mogujie.tt.protobuf.helper.ProtoBuf2JavaBean;
import com.newyhy.activity.HomeMainTabActivity;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.util.HarwkinLogUtil;
import com.quanyan.base.util.ScreenSize;
import com.quanyan.base.util.StringUtil;
import com.quanyan.pedometer.newpedometer.StepService;
import com.quanyan.pedometer.utils.PreferencesUtils;

import com.quanyan.yhy.R;
import com.quanyan.yhy.common.AnalyDataValue;
import com.quanyan.yhy.common.ItemType;
import com.quanyan.yhy.common.SysConfigType;
import com.quanyan.yhy.database.DBManager;
import com.quanyan.yhy.net.NetManager;
import com.quanyan.yhy.net.lsn.OnResponseListener;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.base.utils.TCEventHelper;
import com.quanyan.yhy.ui.common.calendar.ScreenUtil;
import com.smart.sdk.api.request.ApiCode;
import com.yhy.common.base.YHYBaseApplication;
import com.yhy.common.beans.net.model.AppDefaultConfig;
import com.yhy.common.beans.net.model.RCShowcase;
import com.yhy.common.beans.net.model.pedometer.PedometerUserInfo;
import com.yhy.common.beans.net.model.user.DestinationList;
import com.yhy.common.beans.net.model.user.UserInfo;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.CommonUtil;
import com.yhy.common.utils.SPUtils;
import com.yhy.imageloader.ImageLoadManager;
import com.yhy.router.YhyRouter;
import com.yhy.service.IUserService;

import org.json.JSONException;


@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class LogoActivity extends BaseActivity {
    // 此页面最多展示时间
    private static final int MAX_WAIT_TIME = 3000;
    private static final int MSG_SETTEXT = 2;
    private RelativeLayout rlAd;
    private ImageView mAdImage;
    private ImageView mIvLogo;
    private RCShowcase mAdModel;

    private TextView mSkipText;
    private int seconds = 3;

    @Autowired
    IUserService userService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        YhyRouter.getInstance().inject(this);
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_SETTEXT:
                seconds--;
                if (seconds == 0) {
                    finish();
                } else {
                    mSkipText.setText("跳过 " + seconds);
                    mHandler.postDelayed(mRunnable, 1000);
                }
                break;
        }
    }


    @Override
    public void finish() {
        if (SPUtils.isAppFirstStart(getApplicationContext())) {
            SPUtils.setAppFirstStart(getApplicationContext());
            NavUtils.gotoGuideActivty(this);
            super.finish();
            return;
        }
        setResult(Activity.RESULT_OK);
//        super.finish();
        Intent intent = new Intent(LogoActivity.this, HomeMainTabActivity.class);
        startActivity(intent);
        super.finish();
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    /**
     * 获取系统配置项目
     */
    private void initSystemConfig() {
//        if(!SPUtils.isNeedUpdateSystemConfig(this)){
//            return ;
//        }
        NetManager.getInstance(getApplicationContext()).doInitApp(new OnResponseListener<AppDefaultConfig>() {
            @Override
            public void onComplete(boolean isOK, AppDefaultConfig result, int errorCode, String errorMsg) {
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

                    if (result.getCityList() != null && (DestinationList) result.getCityList().get(ItemType.LINE) != null) {
                        DBManager.getInstance(getApplicationContext()).updateDestCities(ItemType.LINE, (DestinationList) result.getCityList().get(ItemType.LINE));
                    } else {
                        updateAll = false;
                    }

                    if (result.getCityList() != null && (DestinationList) result.getCityList().get(ItemType.HOTEL) != null) {
                        DBManager.getInstance(getApplicationContext()).updateDestCities(ItemType.HOTEL, (DestinationList) result.getCityList().get(ItemType.HOTEL));
                    } else {
                        updateAll = false;
                    }

                    if (result.getCityList() != null && (DestinationList) result.getCityList().get(ItemType.SCENIC) != null) {
                        DBManager.getInstance(getApplicationContext()).updateDestCities(ItemType.SCENIC, (DestinationList) result.getCityList().get(ItemType.SCENIC));
                    } else {
                        updateAll = false;
                    }

//                    ((YHYApplication) getApplication()).loadDestCities();

                    //TODO 更新广告信息
                    if (result.getAdBooth() != null) {
                        DBManager.getInstance(getApplicationContext()).updateADInfo(result.getAdBooth());

                    } else {
                        updateAll = false;
                    }

//                    // 首页弹窗
//                    if (result.getmAdMultiBooth() != null) {
//                        DBManager.getInstance(getApplicationContext()).updateADMultiInfo(result.getmAdMultiBooth());
//
//                    }

                    //TODO 更新达人TAB标题
                    if (result.getAdBooth() != null) {
                        DBManager.getInstance(getApplicationContext()).updateMasterTabInfo(result.getMasterBooth());
                    } else {
                        updateAll = false;
                    }

                    if (updateAll) {
                        SPUtils.setLastUpdateScTime(getApplicationContext(), System.currentTimeMillis());
                    }

//                    initAd();
                } else {
//                    initAd();
                }
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
//                initAd();
            }
        });
    }


    @Override
    protected void onDestroy() {
        mHandler.removeCallbacksAndMessages(this);
        super.onDestroy();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        rlAd = (RelativeLayout) findViewById(R.id.rl_ad);
        mAdImage = (ImageView) findViewById(R.id.iv_ad);
        mSkipText = (TextView) findViewById(R.id.ac_logo_skip);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mSkipText.getLayoutParams();
            layoutParams.topMargin += ScreenSize.getStatusBarHeight(getApplicationContext());
            mSkipText.setLayoutParams(layoutParams);
        }
        mSkipText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHandler.removeCallbacks(mRunnable);
                if (SPUtils.isAppFirstStart(getApplicationContext())) {
                    SPUtils.setAppFirstStart(getApplicationContext());
                    NavUtils.gotoGuideActivty(v.getContext());
                    finish();
                    return;
                } else {
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });
        mIvLogo = ((ImageView) findViewById(R.id.splash_bg));
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inSampleSize = 720;
//        BitmapFactory.decodeResource(getResources(), R.mipmap.ic_splash, );
//        ((ImageView)findViewById(R.id.splash_bg)).setImageResource(R.mipmap.ic_splash);
        //重新设置Logo的高度居中
        int width = ScreenUtil.getScreenWidth(this);
        int height = ScreenUtil.getScreenHeight(this);
        int pHeight = (int) (width / ValueConstants.SCALE_SPLASH);
        int leftHeight = height - pHeight - getResources().getDimensionPixelSize(R.dimen.dd_dimen_30px) - 100;
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) mIvLogo.getLayoutParams();
        rlp.bottomMargin = leftHeight / 2;
        mIvLogo.setLayoutParams(rlp);

        setTitleBarBackground(Color.TRANSPARENT);
        getUserProfile();
        initSystemConfig();
        initAd();
        mHandler.postDelayed(mRunnable, 1000);

    }

    /**
     * 初始化广告页
     */
    private void initAd() {
        String ad = SPUtils.getString(getApplicationContext(), SysConfigType.AD);
        if (ad != null) {
            try {
                mAdModel = RCShowcase.deserialize(ad);
                if (mAdModel == null || TextUtils.isEmpty(mAdModel.imgUrl)) {
                    return;
                }
                rlAd.setVisibility(View.VISIBLE);
                mAdImage.setVisibility(View.VISIBLE);
                mIvLogo.setVisibility(View.GONE);


//                Booth booth = result.getAdBooth();
//                if (booth == null || booth.showcases == null || booth.showcases.size() == 0) {
//                    return;
//                }
//                mAdModel = booth.showcases.get(0);
                String imgurl = ImageUtils.getImageFullUrl(mAdModel.imgUrl);
                HarwkinLogUtil.info("context name : " + imgurl);
                int width = ScreenUtil.getScreenWidth(LogoActivity.this);
                int height = ScreenUtil.getScreenHeight(LogoActivity.this);
                RelativeLayout.LayoutParams rl = (RelativeLayout.LayoutParams) mAdImage.getLayoutParams();
                rl.height = height;
//                mAdImage.setLayoutParams(rl);
//                mAdImage.setImageURI(imgurl);
                ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(imgurl), R.mipmap.about_us_logo_pedometr_share, mAdImage);

//                String imgurl = ImageUtils.getImageFullUrl(mAdModel.imgUrl);
//                HarwkinLogUtil.info("context name : " + imgurl);
//
//                int width = ScreenUtil.getScreenWidth(this);
//                int height = ScreenUtil.getScreenHeight(this);
//                int pHeight = (int) (width / ValueConstants.SCALE_SPLASH);
//
//                RelativeLayout.LayoutParams rl = (RelativeLayout.LayoutParams) mAdImage.getLayoutParams();
////                rl.height = (int) (com.mogujie.tt.utils.ScreenUtil.instance(this).getScreenHeightExcludeStatusBar() * 0.8f);
////                rl.height = pHeight;
//                rl.height = height;
//                mAdImage.setLayoutParams(rl);
//                BaseImgView.loadimg(mAdImage,
//                        imgurl,
//                        R.drawable.transparent,
//                        R.drawable.transparent,
//                        R.drawable.transparent,
//                        ImageScaleType.EXACTLY,
//                        width,
//                        pHeight,
//                        0);

                mAdImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    //事件统计
                    TCEventHelper.onEvent(LogoActivity.this, AnalyDataValue.LOGO_IMAGE_CLICK);
                    NavUtils.depatchAllJump(LogoActivity.this, mAdModel, -1);
                    setResult(Activity.RESULT_OK);
                    finish();
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

//        mHandler.postDelayed(mRunnable, 1000);
//        mHandler.sendEmptyMessageDelayed(MSG_FINISH, MAX_WAIT_TIME);
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            mHandler.sendEmptyMessage(MSG_SETTEXT);
        }
    };

    /**
     * 获取用户信息
     */
    private void getUserProfile() {
        if (userService.isLogin()) {
            NetManager.getInstance(this).doGetUserProfile(userService.getLoginUserId(), new OnResponseListener<UserInfo>() {
                @Override
                public void onComplete(boolean isOK, UserInfo result, int errorCode, String errorMsg) {
                    if (isOK && result != null) {
                        DBInterface.instance().initDbHelp(getApplicationContext(),  result.id);
                        DBInterface.instance().insertOrUpdateUser(ProtoBuf2JavaBean.getUserEntity(result));

                        DBManager.getInstance(getApplicationContext()).saveUserInfo(result);
                    }
                }

                @Override
                public void onInternError(int errorCode, String errorMessage) {

                }
            });
        }
        try {
            NetManager.getInstance(this).doGetPedometerUserInfo(new OnResponseListener<PedometerUserInfo>() {
                @Override
                public void onComplete(boolean isOK, PedometerUserInfo result, int errorCode, String errorMsg) {
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
//                            PreferencesUtils.putInt(getApplicationContext(), StepService.THRESHOLD_VALUE, result.trackConfig.minSyncStep);
//                            StepService.getInstance().setThreshold_value(result.trackConfig.minSyncStep);
                            if (!StringUtil.isEmpty(result.trackConfig.shareText)) {
                                PreferencesUtils.putString(getApplicationContext(), StepService.PEDOMETER_SAHRE_TEXT, result.trackConfig.shareText);
                            }
                        }
                        if (result.pointConfig != null) {
                            PreferencesUtils.putString(getApplicationContext(), StepService.PEDOMETER_POINT_COPY, result.pointConfig.toString);
                        }
                    }
                }

                @Override
                public void onInternError(int errorCode, String errorMessage) {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.ac_logo, null);
    }

    @Override
    public View onLoadNavView() {
        return null;
    }

    @Override
    public boolean isTopCover() {
        return true;
    }
}