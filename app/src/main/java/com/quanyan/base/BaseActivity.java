package com.quanyan.base;

import android.content.BroadcastReceiver;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.quanyan.base.baseenum.IActionTitleBar;
import com.quanyan.base.util.DateUtil;
import com.quanyan.base.util.HarwkinLogUtil;
import com.yhy.common.utils.NetworkUtil;
import com.quanyan.base.util.ScreenSize;
import com.quanyan.base.view.BaseView;
import com.quanyan.base.yminterface.ErrorViewClick;
import com.quanyan.base.yminterface.IBaseView;
import com.quanyan.yhy.BaseApplication;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.ErrorCode;
import com.quanyan.yhy.ui.base.utils.PageNameUtils;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;
import com.yhy.common.base.NoLeakHandler;
import com.yhy.router.YhyRouter;

import de.greenrobot.event.EventBus;

/**
 * Created with Android Studio.
 * Title:AboutAndFeedController
 * Description:
 * <li>所有Activity的基类,遇到android:windowSoftInputMode="adjustResize"无效的情况，</li>
 * <li>在4.4以后加入的沉浸式状态栏，会导致windowSoftInputMode的adjustResize属性失效，在这种情况下</li>
 * <li>请在跟布局中添加 android:fitsSystemWindows="true", 但是设置这个属性后会导致界面下沉一个statusbar的高度，所以需要针对</li>
 * <li>这种情况进行处理，在{@link com.quanyan.base.view.BaseView}中有相应的处理结果</li>
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:zhaoxiaopo
 * Date:16/4/27
 * Time:15:09
 * Version 1.0
 */
public class BaseActivity extends FragmentActivity implements IBaseView, NoLeakHandler.HandlerCallback {

    private BaseView mBaseView;
    protected NoLeakHandler mHandler;

    /**
     * 设置导航栏是否遮盖内容布局
     */
    private boolean isTopCover = false;

    private boolean isRegisted = false;
    private BroadcastReceiver mNetBroadCast;//监听网络变化
    private SystemBarTintManager mSystemBarTintManager;

    /**
     * Activity的创建，初始化布局信息，设定沉浸式状态栏的标志，以及顶部导航栏是否需要覆盖内容布局的标志设置；
     * <li>初始化跟布局对象{@link BaseView}，内部提供导航栏和内容布局的父容器</li>
     * <li>初始化{@link NoLeakHandler}对象，</li>
     * <l>提供方法{@link #initView(Bundle)}用于设置其他布局信息</l>
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        YhyRouter.getInstance().inject(this);
        doBeforeSetContentView();
        HarwkinLogUtil.info(getClass().getSimpleName(), "Activity create time : " + DateUtil.convert2String(System.currentTimeMillis(), "HH:mm:ss.SSS"));
        //沉浸式状态栏的设置
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && isUseImmersiveStyle()) {
            Window window = getWindow();
// Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
// Translucent navigation bar
//            window.setFlags(
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            mSystemBarTintManager = new SystemBarTintManager(this);
            mSystemBarTintManager.setStatusBarTintEnabled(true);
            mSystemBarTintManager.setNavigationBarTintEnabled(true);

            mSystemBarTintManager.setTintColor(getResources().getColor(R.color.transparent_black_85));
        }
        mBaseView = new BaseView(this);
        mBaseView.setIBaseView(this);
        isTopCover = isTopCover();
        if (!isTopCover) {
            mBaseView.setDisallowTopCover(true);

        }
        //判断Build的版本，设置导航栏样式
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && isUseImmersiveStyle()) {
            mBaseView.setTopPadding(ScreenSize.getStatusBarHeight(getApplicationContext()));
        }
        setTitleBarBackground(Color.WHITE);
        setContentView(mBaseView);

        mHandler = new NoLeakHandler(this);

        ((BaseApplication) getApplication()).addActivity(this);

        initView(savedInstanceState);

        mNetBroadCast = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
                    if (NetworkUtil.isNetworkAvailable(getApplicationContext())) {
                        onNetStateChanged();
                    } else {
                        onNetStateChanged(false);
                    }
                }
            }
        };
    }

    protected void doBeforeSetContentView(){

    }


    @Override
    public View onLoadContentView() {
        return null;
    }

    @Override
    public View onLoadNavView() {
        return null;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }

    /**
     * 是否使用4.4  API 19 以上的沉浸式样式
     *
     * @return 默认为true(可以强制设置不使用，子类需重载该方法，return false;)
     */
    @Override
    public boolean isUseImmersiveStyle() {
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        HarwkinLogUtil.info(getClass().getSimpleName(), "Activity resume time : " + DateUtil.convert2String(System.currentTimeMillis(), "HH:mm:ss.SSS"));
        MobclickAgent.onPageStart(PageNameUtils.getChineseName(this));
        MobclickAgent.onResume(this);
//        YmAnalyticsEvent.onResume(this);
//        registerNoActiveDevideReceiver();
        if (!isRegisted) {
            IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver(mNetBroadCast, intentFilter);
            isRegisted = true;
        }
    }

    /**
     * 注册过期设备广播监听
     */
//    private void registerNoActiveDevideReceiver() {
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
//        filter.addAction(ValueConstants.BROADCASTRECEIVER_ALL_TAST_COMPLETE);
//        registerReceiver(mBroadcastReceiver, filter);
//    }

    /**
     * 广播
     */
//    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
//                if (NetworkUtil.isNetworkAvailable(getApplicationContext())) {
//                    onNetStateChanged();
//                }
//            }else if(ValueConstants.BROADCASTRECEIVER_ALL_TAST_COMPLETE.equals(intent.getAction())){
//                CreditNotification c= (CreditNotification)intent.getExtras().getSerializable(SPUtils.EXTRA_DATA);
////                String fraction=intent.getStringExtra("fraction");
////                String content=intent.getStringExtra("content");
//
//                if(c!=null){
//                    DialogUtil.showMessageDialogTask(BaseActivity.this, "+" + c.credit, c.description, 0);
//                }else{
//                }
//
//            }
//        }
//    };
    @Override
    protected void onPause() {
        super.onPause();
        HarwkinLogUtil.info(getClass().getSimpleName(), "Activity pause time : " + DateUtil.convert2String(System.currentTimeMillis(), "HH:mm:ss.SSS"));
//        unregisterReceiver(mBroadcastReceiver);
        MobclickAgent.onPageEnd(PageNameUtils.getChineseName(this));
        MobclickAgent.onPause(this);
//        YmAnalyticsEvent.onPause(this);
    }

    /**
     * 当Activity被回收之后，需要将Handle中信息清空，同时设置标志位，禁止Handler再分发message
     */
    @Override
    protected void onDestroy() {
        HarwkinLogUtil.info(getClass().getSimpleName(), "Activity destroy time : " + DateUtil.convert2String(System.currentTimeMillis(), "HH:mm:ss.SSS"));

        if (mHandler != null) {
            mHandler.setValid(false);
            mHandler.removeCallbacksAndMessages(null);
        }

        try {
            if (isRegisted) {
                unregisterReceiver(mNetBroadCast);
            }
        } catch (IllegalArgumentException e) {

        }
        ((BaseApplication) getApplication()).removeActivity(this);
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
        if (mBaseView != null){
            mBaseView.hideLoadingView();
        }
        super.onDestroy();
    }

    protected FrameLayout getNavParentView() {
        if (mBaseView != null) {
            return mBaseView.getNavParentView();
        }
        return null;
    }

    protected FrameLayout getContentParentView() {
        if (mBaseView != null) {
            return mBaseView.getContentParentView();
        }
        return null;
    }

    /**
     * 设置内容布局是否在导航栏下方
     *
     * @param flag true：内容布局在导航栏之下，相当于RelativeLayout.BELOW, false：导航栏将覆盖在内容布局的上方
     */
    protected void setDisallowTopCover(boolean flag) {
        if (mBaseView != null) {
            mBaseView.setDisallowTopCover(flag);
        }
    }

    /**
     * 网络变化（从无网转到有网）
     */
    protected void onNetStateChanged() {
        // 重新加载数据
    }

    /**
     * 网络切换
     *
     * @param hasNet
     */
    protected void onNetStateChanged(boolean hasNet) {
        // 重新加载数据
    }

    /**
     * 进行其他内容的初始化操作
     *
     * @param savedInstanceState
     */
    protected void initView(Bundle savedInstanceState){}

    /**
     * 设置titleBar的背景颜色
     *
     * @param colorRes
     */
    protected void setTitleBarBackground(int colorRes) {
        if (mBaseView != null) {
            mBaseView.setTitleBarBackground(colorRes);
        }
    }

    protected void setTitleBarBackGround(int alpha, int r, int g, int b) {
        if (mBaseView != null) {
            mBaseView.setTitleBarBackGround(alpha, r, g, b);
        }
    }

    /**
     * 设置状态栏的背景色
     *
     * @param resColor 资源文件ID
     */
    public void setSystemBarTintManagerColor(int resColor) {
        if (mSystemBarTintManager != null) {
            mSystemBarTintManager.setTintColor(resColor);
        }
    }

    /**
     * 显示状态加载条
     *
     * @param msg 提示信息
     */
    protected void showLoadingView(String msg) {
        if (mBaseView != null) {
            mBaseView.showLoadingView(msg);
        }
    }

    /**
     * 显示状态加载条
     *
     * @param msg 提示信息
     */
    protected void showLoadingView(String msg, int color) {
        if (mBaseView != null) {
            mBaseView.showLoadingView(msg, color);
        }
    }

    /**
     * 隐藏状态加载提示
     */
    public void hideLoadingView() {
        if (mBaseView != null) {
            mBaseView.post(() -> {
                if (mBaseView != null){
                    mBaseView.hideLoadingView();
                }

            });
        }
    }

    /**
     * 显示错误页面
     *
     * @param errorViewParent 可以根据需要设置要显示错误页面的父布局，默认使用{@link #mBaseView}填充
     * @param type            错误类型由 {@link IActionTitleBar.ErrorType}定义
     * @param fistString      错误提示信息
     * @param secondString    错误提示内容
     * @param buttonText      是否有重新加载的按钮，如没有可以为空或者”“
     * @param errorViewClick  若显示重新加载的按钮，则传入响应点击事件
     */
    protected void showErrorView(@Nullable ViewGroup errorViewParent, IActionTitleBar.ErrorType type, String fistString,
                                 String secondString, String buttonText, ErrorViewClick errorViewClick) {
        if (mBaseView != null) {
            mBaseView.showErrorView(errorViewParent, type, fistString, secondString, buttonText, errorViewClick);
        }
    }

    /**
     * 隐藏错误页面
     *
     * @param errorViewParent 可以根据需要设置要显示错误页面的父布局，
     *                        <li>需要和{@link #showErrorView(ViewGroup, IActionTitleBar.ErrorType, String, String, String, ErrorViewClick)}</li>
     *                        <li>中的{@link ViewGroup}保持一直</li>
     *                        <li>，默认使用{@link #mBaseView}填充</li>
     */
    protected void hideErrorView(@Nullable ViewGroup errorViewParent) {
        if (mBaseView != null) {
            mBaseView.hideErrorView(errorViewParent);
        }
    }

    /**
     * 处理通过Handle发送的数据
     *
     * @param msg {@link Message}信息
     */
    @Override
    public void handleMessage(Message msg) {
    }

    /**
     * @param errorCode 参考 ErrorCode
     * @param listener
     * @Description 显示错误页面
     * @author xiezhidong@pajk.cn
     */

    public void showErrorPage(int errorCode, View.OnClickListener listener) {
        switch (errorCode) {
            case ErrorCode.STATUS_IO_EXCEPTION:
                break;
            case ErrorCode.STATUS_NETWORK_EXCEPTION:
                break;
            case ErrorCode.CONNECTTION_TIME_OUT:
                break;
            case ErrorCode.DEVICE_TOKEN_MISSING:
                ToastUtil.showToast(this, "DEVICE_TOKEN_MISSING");
                break;

            case ErrorCode.NETWORK_UNAVAILABLE:
                break;

            case ErrorCode.NOT_LOGIN:
                ToastUtil.showToast(this, "NOT_LOGIN");
                break;
            default:
                break;
        }
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level >= ComponentCallbacks2.TRIM_MEMORY_MODERATE) {
//            BaseImgView.imageLoader.clearMemoryCache();
        }
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
