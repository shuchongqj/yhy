package com.quanyan.pedometer;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.lidroid.xutils.util.LogUtils;
import com.quanyan.base.BaseFragment;
import com.quanyan.base.util.LocalUtils;
import com.quanyan.base.util.StringUtil;
import com.quanyan.pedometer.core.Constants;
import com.quanyan.pedometer.newpedometer.PedometerUtil;
import com.quanyan.pedometer.newpedometer.StepDBManager;
import com.quanyan.pedometer.newpedometer.StepService;
import com.quanyan.pedometer.newpedometer.WalkDataDaily;
import com.quanyan.pedometer.utils.PreferencesUtils;
import com.quanyan.pedometer.utils.TimeUtil;
import com.quanyan.yhy.R;
import com.quanyan.yhy.eventbus.EvBusStartFund;
import com.quanyan.yhy.eventbus.EvBusUserLoginState;
import com.quanyan.yhy.ui.adapter.DetailViewPagerAdapter;
import com.quanyan.yhy.ui.base.utils.DialogUtil;
import com.quanyan.yhy.ui.base.utils.NativeUtils;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.common.WebController;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.login.LoginActivity;
import com.quanyan.yhy.ui.shop.ShopHomePageActivity;
import com.quanyan.yhy.util.DomainUtils;
import com.yhy.common.beans.net.model.pedometer.PedometerHistoryResult;
import com.yhy.common.beans.net.model.pedometer.PedometerHistoryResultList;
import com.yhy.common.beans.net.model.pedometer.StepParam;
import com.yhy.common.beans.net.model.pedometer.StepResult;
import com.yhy.common.constants.IntentConstants;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.AndroidUtils;
import com.yhy.common.utils.SPUtils;
import com.yhy.network.api.user.UserApi;
import com.yhy.service.IUserService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

public class StepFragment2 extends BaseFragment implements OnClickListener {

    @Autowired
    IUserService userService;

    private StepController mStepController;
    private TextView mDistanceText;
    private TextView mCaloriesText;
    private TextView mFatText;

    private TextView mTodayPoints;
    private TextView mPointsDouble;

    private ImageView mPreviousImg;
    private ImageView mNextImg;
    private LinearLayout mPreviousLayout;
    private LinearLayout mNextLayout;
    private ViewPager mViewPager;
    private WebView mWebView;
//    private RelativeLayout mFLWebView;

    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private List<WalkDataDaily> mWalkDataInfoList = new ArrayList<>();
    private StepDBManager.SaveAndLoadWalkDataInfoListTask mSaveWalkDataTask;
    private StepDBManager.LoadWalkDataInfoListTask mLoadWalkDataTask;

//    private final int MAX_PAGE_SIZE = 31;

    public final static int MSG_GET_SERVER_DATA_OK = 0x101;
    public final static int MSG_GET_SERVER_DATA_FAIL = 0x202;
    public final static int MSG_GET_DB_DATA_OK = 0x303;
    public final static int MSG_GET_INIT_OK = 0x304;

    public static final int MSG_GET_POINT_INFO_OK = 0x404;
    public static final int MSG_GET_POINT_INFO_ERROR = 0x505;

    @Override
    protected void initView(View view, Bundle bundle) {
        EventBus.getDefault().register(this);
        mStepController = new StepController(getActivity(), mHandler);

        mDistanceText = (TextView) view.findViewById(R.id.tvDistance);
        mCaloriesText = (TextView) view.findViewById(R.id.tvCalorie);
        mFatText = (TextView) view.findViewById(R.id.tvFat);

        mTodayPoints = (TextView) view.findViewById(R.id.step_fg_point_text);
        mPointsDouble = (TextView) view.findViewById(R.id.step_fg_point_double_text);

        mPreviousImg = (ImageView) view.findViewById(R.id.fg_step_previous_img);
        mNextImg = (ImageView) view.findViewById(R.id.fg_step_next_img);

        mPreviousLayout = (LinearLayout) view.findViewById(R.id.fg_step_previous_img_layout);
        mNextLayout = (LinearLayout) view.findViewById(R.id.fg_step_next_img_layout);
        mViewPager = (ViewPager) view.findViewById(R.id.fg_step_gradient_view_pager);
        mWebView = (WebView) view.findViewById(R.id.fg_step_webview);
//        mFLWebView = (RelativeLayout) view.findViewById(R.id.ll_step_webview);

        mPreviousLayout.setOnClickListener(this);
        mNextLayout.setOnClickListener(this);
        view.findViewById(R.id.ic_pedo_notice_layout).setOnClickListener(this);

        ((TextView) view.findViewById(R.id.step_fg_point_notice)).setText(PreferencesUtils.getString(getActivity().getApplicationContext(), StepService.PEDOMETER_POINT_COPY, ""));
        mPointsDouble.setSelected(true);
        mPointsDouble.setText("未翻倍");
        mPointsDouble.setTextColor(Color.WHITE);

        mViewPager.addOnPageChangeListener(mPageListener);
    }

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.ACTION_UPLOAD_STEP_DATA_OK);
        filter.addAction(Constants.ACTION_UPLOAD_STEP_DATA_KO);
        filter.addAction(Constants.ACTION_OVER_DAY);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mReceiver, filter);
        PedometerUtil.getInstance().bindService(getActivity());
        PedometerUtil.getInstance().addListener(mOnStepListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        PedometerUtil.getInstance().removeListener(mOnStepListener);
        PedometerUtil.getInstance().unBindService(getActivity());
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mReceiver);
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(getActivity(), R.layout.fragment_step, null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case IntentConstants.LOGIN_RESULT:
                if (Activity.RESULT_OK == resultCode) {
                    loadData();
                    StepParam stepParam = new StepParam();
                    stepParam.stepCounts = (int) StepService.getInstance().getSteps();
                    stepParam.stepDate = System.currentTimeMillis();
                    getPointByStep(stepParam);
                }
                break;
        }
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (mSaveWalkDataTask != null
                && mSaveWalkDataTask.getStatus() != AsyncTask.Status.FINISHED) {
            mSaveWalkDataTask.cancel(true);
        }
        if (mLoadWalkDataTask != null
                && mLoadWalkDataTask.getStatus() != AsyncTask.Status.FINISHED) {
            mLoadWalkDataTask.cancel(true);
        }
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        loadData();
    }

    private OnPageChangeListener mPageListener = new OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            // TODO Auto-generated method stub
            if (getActivity() != null) {
                if (position == mWalkDataInfoList.size() - 1) {
                    ((PedoActivity) getActivity()).setTitleText(getString(R.string.title_pedometer));
                } else {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    ((PedoActivity) getActivity()).setTitleText(simpleDateFormat.format(new Date(mWalkDataInfoList.get(position).synTime)));
                }
            }

            if (position == mWalkDataInfoList.size() - 1) {
                mNextImg.setSelected(true);
                mNextLayout.setEnabled(false);
                mNextLayout.invalidate();
            } else if (position == 0) {
                mPreviousImg.setSelected(true);
                mPreviousLayout.setEnabled(false);
                mPreviousLayout.invalidate();
            } else {
                mPreviousImg.setSelected(false);
                mNextLayout.setEnabled(true);
                mNextLayout.invalidate();
                mNextImg.setSelected(false);
                mPreviousLayout.setEnabled(true);
                mPreviousLayout.invalidate();
            }

            WalkDataDaily walkDataDaily = mWalkDataInfoList.get(position);
            setWalkDataInfo(walkDataDaily.distance, walkDataDaily.calories);

            ((StepItemFragment) mFragments.get(position)).setCurrentStepCount(walkDataDaily.stepCount);
            ((StepItemFragment) mFragments.get(position)).updateUI();
        }

        @Override
        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageScrollStateChanged(int state) {
            // TODO Auto-generated method stub

        }
    };

    @Override
    public void onClick(View v) {
        if (TimeUtil.isFastDoubleClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.ic_pedo_notice_layout:
                ToastUtil.showToast(getActivity(), "你说这是什么???");
                cropView();
                break;
            case R.id.fg_step_previous_img_layout:
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
                break;
            case R.id.fg_step_next_img_layout:
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                break;
        }
    }

    private boolean isFirst = true;

    private PedometerUtil.OnStepListener mOnStepListener = new PedometerUtil.OnStepListener() {
        @Override
        public void onStep(long step, double speed, double calories, long time,
                           double distance) {
            LogUtils.d("step=" + step + ",speed=" + speed + ",calories="
                    + calories + ",distance=" + distance);
            if (isFirst) {
                isFirst = false;
                StepParam stepParam = new StepParam();
                stepParam.stepCounts = (int) step;
                stepParam.stepDate = System.currentTimeMillis();
                getPointByStep(stepParam);
            }

            if (mWalkDataInfoList.size() > 0 && mWalkDataInfoList.size() <= 31) {
                WalkDataDaily todayInfo = mWalkDataInfoList.get(mWalkDataInfoList.size() - 1);
                setWalkData(todayInfo, step, calories, distance);
                if (mViewPager.getCurrentItem() == mWalkDataInfoList.size() - 1) {
                    setWalkDataInfo(distance, calories);
                    ((StepItemFragment) mFragments.get(mWalkDataInfoList.size() - 1)).setCurrentStepCount(step);
                    ((StepItemFragment) mFragments.get(mWalkDataInfoList.size() - 1)).updateUI();
                }
            } else if (mWalkDataInfoList.size() == 1) {
                WalkDataDaily todayInfo = mWalkDataInfoList.get(0);
                setWalkData(todayInfo, step, calories, distance);
                setWalkDataInfo(distance, calories);
                ((StepItemFragment) mFragments.get(0)).setCurrentStepCount(step);
                ((StepItemFragment) mFragments.get(0)).updateUI();
            }

        }
    };

    private void initData() {
        WalkDataDaily walkDataDaily = new WalkDataDaily();
        walkDataDaily.targetStepCount = PedometerUtil.getTargetStep(getActivity());
        setWalkData(walkDataDaily, StepService.getInstance().getSteps(), StepService.getInstance().getCalories(),
                StepService.getInstance().getDistance());
        if (mWalkDataInfoList.size() < 31) {
            mWalkDataInfoList.add(walkDataDaily);
        } else if (mWalkDataInfoList.size() == 31) {
//                WalkDataDaily initDayValue = mWalkDataInfoList.get(mWalkDataInfoList.size() - 1);
//                if (TimeUtils.isToday(initDayValue.synTime)) {
//                    long lastSynTime = PreferencesUtils.getLong(getActivity().getApplicationContext(), StepService.LASTSYNTIME, -1);
//                    if (lastSynTime == -1) {
//                        PreferencesUtils.putLong(getActivity().getApplicationContext(), StepService.LASTSYNTIME, 0);
//                        StepService.getInstance().setValues(initDayValue.stepCount, initDayValue.distance, initDayValue.calories);
//                    }
//                }
            mWalkDataInfoList.set(mWalkDataInfoList.size() - 1, walkDataDaily);
        }
        for (int i = mFragments.size(); i < mWalkDataInfoList.size(); i++) {
            mFragments.add(new StepItemFragment());
        }
        mViewPager.setAdapter(new DetailViewPagerAdapter(getChildFragmentManager(), mFragments, null));
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mViewPager.setCurrentItem(mWalkDataInfoList.size() - 1);
            }
        }, 500);

        if (mWalkDataInfoList.size() == 1) {
            setWalkDataInfo(walkDataDaily.distance, walkDataDaily.calories);

            ((StepItemFragment) mFragments.get(0)).setCurrentStepCount(walkDataDaily.stepCount);
            ((StepItemFragment) mFragments.get(0)).updateUI();
        }
    }

    private void setWalkDataInfo(double distance, double calories) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        mDistanceText.setText(decimalFormat.format(distance) + "");
        mCaloriesText.setText(decimalFormat.format(calories) + "");
        mFatText.setText(decimalFormat.format(calories / 9) + "");
    }

    private void setWalkData(WalkDataDaily data, long step, double calories,
                             double distance) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        long timeFormat = (cal.getTimeInMillis() / 1000) * 1000;

        data.stepCount = step;
        data.calories = calories;
        data.distance = distance;
        data.synTime = timeFormat;
    }

    private void loadData() {
        showLoadingView("");
        mStepController.getHistoryPedometerInfo(getActivity());
    }

    public void reLoadPointByStep(){
        StepParam stepParam = new StepParam();
        stepParam.stepCounts = (int) StepService.getInstance().getSteps();
        stepParam.stepDate = System.currentTimeMillis();
        getPointByStep(stepParam);
    }

    private void getPointByStep(StepParam stepParam) {
        mStepController.doGetCurrentPointByStep(getActivity(), stepParam);
    }

    @Override
    public void handleMessage(Message msg) {
//        hideErrorView(mFLWebView);
        String errorMsg;
        switch (msg.what) {
            case MSG_GET_SERVER_DATA_OK:
                LogUtils.e("MSG_GET_SERVER_DATA_OK");
                PedometerHistoryResultList data = (PedometerHistoryResultList) msg.obj;
                List<PedometerHistoryResult> dataList = null;
                if (data != null && data.pedometerHistoryResultList != null && data.pedometerHistoryResultList.size() > 0) {
                    dataList = data.pedometerHistoryResultList;
                    StepDBManager.getDefaultDbUtils(getActivity());
                    mSaveWalkDataTask = new StepDBManager.SaveAndLoadWalkDataInfoListTask(StepDBManager.DataChange2(dataList),
                            new StepDBManager.LoadSuccessDailyData() {

                                @Override
                                public void success(List<WalkDataDaily> walkDataDailies) {
                                    if (mHandler != null) {
                                        Message.obtain(mHandler, MSG_GET_DB_DATA_OK, walkDataDailies)
                                                .sendToTarget();
                                    }
                                }
                            });
                    mSaveWalkDataTask.execute();
                } else {
                    Message.obtain(mHandler, MSG_GET_SERVER_DATA_FAIL, "").sendToTarget();
                }
                break;
            case MSG_GET_SERVER_DATA_FAIL:
                LogUtils.e("MSG_GET_SERVER_DATA_FAIL");
                errorMsg = (String) msg.obj;
                if (!TextUtils.isEmpty(errorMsg)) {
                    ToastUtil.showToast(getActivity(), errorMsg);
                }
                StepDBManager.getDefaultDbUtils(getActivity());
                mLoadWalkDataTask = new StepDBManager.LoadWalkDataInfoListTask(
                        new StepDBManager.LoadSuccessDailyData() {

                            @Override
                            public void success(List<WalkDataDaily> walkDataDailies) {
                                if (mHandler != null) {
                                    Message.obtain(mHandler, MSG_GET_DB_DATA_OK, walkDataDailies)
                                            .sendToTarget();
                                }
                            }

                        });
                mLoadWalkDataTask.execute();
                break;
            case MSG_GET_DB_DATA_OK:
                List<WalkDataDaily> dbData = (List<WalkDataDaily>) msg.obj;
                LogUtils.i("database data : " + dbData);
                LogUtils.i("database data size : " + dbData.size());
//                if (SPUtils.isLogin(getActivity().getApplicationContext())) {
//                    if (dbData != null && dbData.size() > 0) {
//                        mWalkDataInfoList.clear();
//                        mWalkDataInfoList.addAll(dbData);
//                        WalkDataDaily walkDataDaily = dbData.get(dbData.size() - 1);
//                        if (TimeUtils.isToday(walkDataDaily.synTime)) {
//                            new StepDBManager.UpdateLoginUserInfo(getActivity().getApplicationContext(), walkDataDaily.stepCount,
//                                    walkDataDaily.distance, walkDataDaily.calories,
//                                    new StepDBManager.ResetNewPersonValue() {
//                                        @Override
//                                        public void loadSuccess(long stepCount, double distance, double calories) {
//                                            StepService.getInstance().resetValue(stepCount, distance, calories);
//                                            if (mHandler != null) {
//                                                Message.obtain(mHandler, MSG_GET_INIT_OK).sendToTarget();
//                                            }
//                                        }
//                                    }).execute();
//                        } else {
//                            if (mHandler != null) {
//                                Message.obtain(mHandler, MSG_GET_INIT_OK).sendToTarget();
//                            }
//                        }
//                    }
//                } else {
                    mWalkDataInfoList.clear();
                    mWalkDataInfoList.addAll(dbData);
                    initData();
                    hideLoadingView();
//                }
                initWebData();
                break;
            case MSG_GET_INIT_OK:
                initData();
                hideLoadingView();
                break;
            case MSG_SHOW_ERROR_PAGE:
                mWebView.setVisibility(View.GONE);
                ToastUtil.showToast(getActivity(), getString(R.string.error_net_msg_text));
//                showErrorView(mFLWebView, IActionTitleBar.ErrorType.ERRORNET, "", "", "", new ErrorViewClick() {
//                    @Override
//                    public void onClick(View view) {
//                        mWebView.setVisibility(View.VISIBLE);
//                        reloadUrlForErrorPage();
//                        showLoadingView(getResources().getString(R.string.scenic_loading_notice));
//                    }
//                });
                break;
            case NativeUtils.MSG_SCHEME_URL:
                if (msg.obj instanceof String) {
                    Uri uri = Uri.parse((String) msg.obj);
                    NativeUtils.parseNativeData(uri, getActivity());
                }
                break;
            case ValueConstants.MSG_GET_WTK_OK:
                String wtk = (String) msg.obj;
                if (!StringUtil.isEmpty(wtk)) {
                    SPUtils.setWebUserToken(getActivity(), wtk, -1);
                    SPUtils.setLastUpdateWtkTime(getActivity(), System.currentTimeMillis());
                    new UserApi().saveWebToken(wtk);
                    loadCookieUrl();
                } else {
                    reloadUrlForErrorPage();
                }
                break;
            case ValueConstants.MSG_GET_WTK_KO:
                reloadUrlForErrorPage();
                break;
            case MSG_GET_POINT_INFO_OK:
                StepResult stepResult = (StepResult) msg.obj;
                if (stepResult != null) {
                    if (stepResult.doublePoint) {
                        mPointsDouble.setSelected(false);
                        mPointsDouble.setText("已翻倍");
                        mPointsDouble.setTextColor(Color.WHITE);
                    }
                    mTodayPoints.setText(stepResult.point + "");
                }
                break;
            case MSG_GET_POINT_INFO_ERROR:
                break;

            case StepController.MSG_GET_START_POINT_OK:
                DialogUtil.showRevPointsDialog(getActivity(), R.mipmap.ic_rev_points_start_fund, ShopHomePageActivity.TIME_INTERVAL_COUPON_DISPLAY, true);
                loadCookieUrl();
                break;
            case StepController.MSG_GET_START_POINIT_KO:
                ToastUtil.showToast(getActivity(), getActivity().getString(R.string.toast_pedometer_get_start_point_ko));
                break;
            case StepController.MSG_GET_YESTERDAY_POINT_OK:
                break;
            case StepController.MSG_GET_YESTERDAY_POINT_KO:
                break;
        }
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            LogUtils.e("mReceiver ACTION" + action);
//            Constants.ACTION_UPLOAD_STEP_DATA_KO.equals(action)
//            if (Constants.ACTION_UPLOAD_STEP_DATA_OK.equals(action)) {
//                if (SPUtils.isLogin(getActivity().getApplicationContext())) {
//                    loadData();
//                }
//            }else
            if (Constants.ACTION_OVER_DAY.equals(action)) {
                loadData();
            }
        }
    };

    /*****************************WebView Settings Start***********************************/
    /**
     * @Description 加载数据
     */
    //默认计步器的网页
    public static final int MSG_SHOW_ERROR_PAGE = 0x1001;
    private String mUrl;
    private WebController mWebController;

    private void initWebData() {
        mUrl = SPUtils.getPedometerUrl(getActivity());
        mWebController = new WebController(getActivity(), mHandler);

        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setBlockNetworkImage(false);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setPluginState(WebSettings.PluginState.ON);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setBuiltInZoomControls(true);
        settings.setSupportZoom(false);
        mWebView.getSettings().setDomStorageEnabled(false);
        mWebView.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
        mWebView.getSettings().setAllowFileAccess(false);
        mWebView.getSettings().setAppCacheEnabled(false);
        mWebView.setWebViewClient(new MyWebViewClient());
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            settings.setDisplayZoomControls(false);
        }
        //设置UA
        String ua = mWebView.getSettings().getUserAgentString();
        mWebView.getSettings().setUserAgentString(ua +
                ";QuanYanLvXing_"+ AndroidUtils.getVersionCode(getActivity()) +
                ";Channal_" + LocalUtils.getChannel(getActivity()));

        // 不使用缓存，只从网络获取数据
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (NativeUtils.parseUrl(url)) {
                    Message.obtain(mHandler, NativeUtils.MSG_SCHEME_URL, url).sendToTarget();
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                hideLoadingView();
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                Message.obtain(mHandler, MSG_SHOW_ERROR_PAGE).sendToTarget();
            }
        });

        loadCookieUrl();
    }

    /**
     * 重现加载网页
     */
    private void reloadUrlForErrorPage() {
        mWebView.loadUrl(mUrl);
    }

    /**
     * 加载URL
     */
    private void loadCookieUrl() {
        if (userService.isLogin() && SPUtils.isNeedUpdateWtk(getActivity())) {
            mWebController.doGetWapLoginToken(getActivity());
            return;
        }
        if (!TextUtils.isEmpty(mUrl)) {
            syncCookie();
            mWebView.loadUrl(mUrl);
        }
    }

    /**
     * 与html进行 JS交互
     */
    class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }
    }

    /**
     * 设置cookier
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected void syncCookie() {
        CookieManager cookieManager = CookieManager.getInstance();
        CookieSyncManager cookieSyncManager = CookieSyncManager
                .createInstance(getActivity());
        cookieSyncManager.sync();

        ParmMap cookiesMap = getDefalutCookier(getActivity());
        if (cookiesMap != null && cookiesMap.parmsMap.size() > 0) {
            cookieManager.setAcceptCookie(true);
            for (String key : cookiesMap.parmsMap.keySet()) {
                if (!TextUtils.isEmpty(cookiesMap.get(key))) {
                    cookieManager.setCookie(mUrl, cookiesMap.get(key));
                }
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.flush();
        }

        CookieSyncManager.getInstance().sync();
    }

    /**
     * 获取默认种的Cookie
     *
     * @param context
     * @return
     */
    private ParmMap getDefalutCookier(Context context) {
        ParmMap cookies = new ParmMap();
        if(!StringUtil.isEmpty(SPUtils.getWebUserToken(context))) {
            cookies.put("_wtk", String.format("_wtk=%s;path=/;domain=%s",
                    SPUtils.getWebUserToken(context),
                    DomainUtils.getDomain()));
        }
        if(userService.getLoginUserId() > 0) {
            cookies.put("_uid", String.format("_uid=%s;path=/;domain=%s",
                    userService.getLoginUserId(),
                    DomainUtils.getDomain()));
        }
        return cookies;
    }

    public static final class ParmMap implements Parcelable {
        public Map<String, String> parmsMap = new HashMap<String, String>();

        public void put(String key, String value) {
            parmsMap.put(key, value);
        }

        public String get(String key) {
            return parmsMap.get(key);
        }

        public void remove(String key) {
            parmsMap.remove(key);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeMap(parmsMap);
        }

        public static final Creator<ParmMap> CREATOR = new Creator<ParmMap>() {
            @Override
            public ParmMap createFromParcel(Parcel source) {
                ParmMap p = new ParmMap();
                p.parmsMap = source.readHashMap(HashMap.class.getClassLoader());
                return p;
            }

            @Override
            public ParmMap[] newArray(int size) {
                return new ParmMap[size];
            }
        };
    }

    /*****************************
     * WebView Settings End
     ***********************************/

    private void cropView() {

        //获取webview缩放率
        float scale = mWebView.getScale();
        //得到缩放后webview内容的高度
        int webViewHeight = (int) (mWebView.getContentHeight() * scale);
        Log.i("content height", webViewHeight + "");
        final Bitmap bitmap = Bitmap.createBitmap(mWebView.getWidth(), webViewHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        //绘制
        mWebView.draw(canvas);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    File file = new File(Environment.getExternalStorageDirectory() + File.separator + "cache_" + System.currentTimeMillis() + "cache.jpg");
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    Log.i("file", file.getAbsolutePath());
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fileOutputStream);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 用户登录退出通知
     *
     * @param evBusUserLoginState
     */
    public void onEvent(EvBusUserLoginState evBusUserLoginState) {
        loadCookieUrl();
    }

    public void onEvent(EvBusStartFund evBusStartFund) {
        if (!userService.isLogin()) {
            NavUtils.gotoLoginActivity(getActivity());
            return;
        }
        if (StepService.getInstance().getSteps() < 5) {
            DialogUtil.showRevPointsDialog(getActivity(), R.mipmap.ic_rev_points_no_reach_5, ShopHomePageActivity.TIME_INTERVAL_COUPON_DISPLAY, false);
        } else {
            mStepController.getStartPoint(getActivity());
        }
    }

}
