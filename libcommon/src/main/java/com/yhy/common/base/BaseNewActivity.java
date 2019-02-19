package com.yhy.common.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;
import com.gyf.barlibrary.ImmersionBar;
import com.yhy.common.R;
import com.yhy.common.utils.NetworkUtil;

import java.util.List;
import java.util.function.Consumer;

import de.greenrobot.event.EventBus;

public abstract class BaseNewActivity extends AppCompatActivity implements NoLeakHandler.HandlerCallback {
    protected ImmersionBar mImmersionBar;
    private InputMethodManager imm;
    protected NoLeakHandler mHandler;
    protected LoadingDialog mLoadingDialog;
    protected Context mContext;
    private BroadcastReceiver mNetBroadCast;//监听网络变化
    private boolean isRegisted = false;
    public String tag  = getClass().getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ARouter.getInstance().inject(this);
        mContext = this;
        if (setLayoutId() > 0) {
            setContentView(setLayoutId());
        }
        if (isImmersionBarEnabled()) {
            initImmersionBar();
        }
        mHandler = new NoLeakHandler(this);
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
        YHYBaseApplication.getInstance().addActivity(this);
        initVars();
        initView();
        setListener();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isRegisted) {
            IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver(mNetBroadCast, intentFilter);
            isRegisted = true;
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
     * 是否可以使用沉浸式
     * Is immersion bar enabled boolean.
     *
     * @return the boolean
     */
    protected boolean isImmersionBarEnabled() {
        return true;
    }

    protected void initImmersionBar() {
        //在BaseActivity里初始化
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.init();
    }

    protected abstract int setLayoutId();

    /**
     * 初始化参数
     */
    protected void initVars() {

    }

    protected void initData() {
    }

    protected void initView() {
    }

    protected void setListener() {
    }

    public void finish() {
        super.finish();
        hideSoftKeyBoard();
    }

    public void showLoadingView(String msg) {
        if (mLoadingDialog == null) {
            mLoadingDialog = showLoadingDialog(this, msg, true);
        }

        mLoadingDialog.setDlgMessage(msg);

        if (this.isFinishing()) {
            return;
        }
        if (!mLoadingDialog.isShowing()) {
            mLoadingDialog.show();
        }
    }

    public void showLoadingView(String msg, int color) {
        if (mLoadingDialog == null) {
            mLoadingDialog = showLoadingDialog(this, msg, true);
        }
        if (color != -1) {
            mLoadingDialog.setDlgMsgColor(this, color);
        }
        mLoadingDialog.setDlgMessage(msg);
        if (this.isFinishing()) {
            return;
        }
        if (!mLoadingDialog.isShowing()) {
            mLoadingDialog.show();
        }
    }

    public void hideLoadingView() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            YHYBaseApplication.getInstance().removeActivity(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (isRegisted) {
                unregisterReceiver(mNetBroadCast);
            }
        } catch (IllegalArgumentException e) {

        }
        this.imm = null;
        if (mImmersionBar != null)
            mImmersionBar.destroy();  //在BaseActivity里销毁
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }

    public void hideSoftKeyBoard() {
        View localView = getCurrentFocus();
        if (this.imm == null) {
            this.imm = ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE));
        }
        if ((localView != null) && (this.imm != null)) {
            this.imm.hideSoftInputFromWindow(localView.getWindowToken(), 2);
        }
    }

    @Override
    public void handleMessage(Message msg) {

    }

    /**
     * 获取加载等待对话框
     *
     * @param context
     * @param message
     * @param isCancelable
     * @return
     */
    private static LoadingDialog showLoadingDialog(Context context, String message, boolean isCancelable) {
        LoadingDialog dialog = new LoadingDialog(context, R.style.loading_dialog);
        //沉浸式状态栏的设置
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = dialog.getWindow();
// Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
// Translucent navigation bar
//            window.setFlags(
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        dialog.setCancelable(isCancelable);
        dialog.setDlgMessage(message);
//		InterestMultiPageDialog.getWindow().setType((WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
        return dialog;
    }

    /**
     * 重写返回事件。拦截返回事件传递给fragment。由fragment先处理
     */
    @Override
    public final void onBackPressed() {

        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragments) {
            if (fragment instanceof BaseNewFragment) {
                if (((BaseNewFragment) fragment).onBackPressed()) {
                    return;
                }
            }
        }

        onNewBackPressed();
    }

    protected void onNewBackPressed() {
        super.onBackPressed();
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

}
