package com.quanyan.base.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.newyhy.manager.ErrorManager;
import com.newyhy.model.ErrorInfo;
import com.quanyan.base.baseenum.IActionTitleBar;
import com.quanyan.base.util.HarwkinLogUtil;
import com.quanyan.base.util.ScreenSize;
import com.quanyan.base.yminterface.ErrorViewClick;
import com.quanyan.base.yminterface.IBaseView;
import com.quanyan.yhy.R;
import com.quanyan.yhy.ui.base.utils.DialogUtil;
import com.quanyan.yhy.view.NetWorkErrorView;

/**
 * Created with Android Studio.
 * Title:AboutAndFeedController
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:zhaoxiaopo
 * Date:16/4/25
 * Time:13:52
 * Version 1.0
 */
public class BaseView extends RelativeLayout {

    private IBaseView mIBaseView;
    /**
     * 导航栏的父布局
     */
    private FrameLayout mNavParentView;
    /**
     * 内容的父布局
     */
    private FrameLayout mContentParentView;

    /**
     * 网络错误页面
     */
    public NetWorkErrorView mNetWorkErrorView;
    /**
     * 加载状态条
     */
    protected LoadingDialog mLoadingDialog;

    public BaseView(Context context) {
        super(context);
        initView(context);
    }

    public BaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public BaseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BaseView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    @Override
    protected final boolean fitSystemWindows(Rect insets) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && mIBaseView.isUseImmersiveStyle()) {
            // Intentionally do not modify the bottom inset. For some reason,
            // if the bottom inset is modified, window resizing stops working.
            // Please Figure out why by yourself.
            insets.left = 0;
            insets.top = 0;
            insets.right = 0;
        }

        return super.fitSystemWindows(insets);
    }

    /**
     * 解决沉浸式状态栏下，android:windowSoftInputMode="adjustResize"失效的问题。
     * <li>参考：http://www.jianshu.com/p/773f6e6ab972</li>
     * <li>参考：http://stackoverflow.com/questions/21092888/windowsoftinputmode-adjustresize-not-working-with-translucent-action-navbar</li>
     *
     * @param insets
     * @return
     */
    @Override
    public WindowInsets onApplyWindowInsets(WindowInsets insets) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH && mIBaseView.isUseImmersiveStyle()) {
            return super.onApplyWindowInsets(insets.replaceSystemWindowInsets(0, 0, 0,
                    insets.getSystemWindowInsetBottom()));
        } else {
            return insets;
        }
    }

    /**
     * 加载页面布局的接口，不设置此接口将无法绘制content布局和导航栏
     *
     * @param iBaseView 接口实例对象
     */
    public void setIBaseView(IBaseView iBaseView) {
        mIBaseView = iBaseView;
        loadView();
    }

    /**
     * 初始化布局对象
     *
     * @param context 上下文
     */
    private void initView(Context context) {
        setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        View.inflate(context, R.layout.ac_base, this);
        setFocusable(true);
        setFocusableInTouchMode(true);
        mNavParentView = (FrameLayout) findViewById(R.id.ac_base_nav_title_view);
        mContentParentView = (FrameLayout) findViewById(R.id.ac_base_content_view);

        /*有问题，统一使用setIBaseView设置*/
//        if(context instanceof IBaseView) {
//            mIBaseView = (IBaseView) context;
//            loadView();
//        }
    }

    /**
     * 加载导航栏布局和内容布局
     */
    private void loadView() {
        View contentView = mIBaseView.onLoadContentView();
        View navView = mIBaseView.onLoadNavView();
        if (contentView != null) {
            mContentParentView.addView(contentView,
                    new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT));
        }
        if (navView != null) {
            mNavParentView.addView(navView,
                    new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ScreenSize.convertDIP2PX(getContext().getApplicationContext(), 48)));
        }
    }

    public FrameLayout getNavParentView() {
        return mNavParentView;
    }

    public FrameLayout getContentParentView() {
        return mContentParentView;
    }

    /**
     * 设置titleBar的背景颜色
     *
     * @param colorRes
     */
    public void setTitleBarBackground(int colorRes) {
        mNavParentView.setBackgroundColor(colorRes);
    }

    public void setTitleBarBackGround(int alpha, int r, int g, int b) {
        mNavParentView.setBackgroundColor(Color.argb(alpha, r, g, b));
    }

    private boolean isViewAdded = false;

    /**
     * 显示错误页面
     *
     * @param errorViewParent 可以根据需要设置要显示错误页面的父布局，默认使用{@link #mContentParentView}填充
     * @param type            错误类型由 {@link IActionTitleBar.ErrorType}定义
     * @param fistString      错误提示信息
     * @param secondString    错误提示内容
     * @param buttonText      是否有重新加载的按钮，如没有可以为空或者”“
     * @param errorViewClick  若显示重新加载的按钮，则传入响应点击事件
     */
    public void showErrorView(@Nullable ViewGroup errorViewParent, IActionTitleBar.ErrorType type, String fistString,
                              String secondString, String buttonText, ErrorViewClick errorViewClick) {
        if (mNetWorkErrorView == null) {
            mNetWorkErrorView = new NetWorkErrorView(getContext().getApplicationContext());
        }

        if (errorViewParent != null) {
            if (!isViewAdded) {
                errorViewParent.addView(mNetWorkErrorView, 0);
                isViewAdded = true;
            }
        } else {
            if (!isViewAdded) {
                mContentParentView.addView(mNetWorkErrorView);
                isViewAdded = true;
            }
        }
        if (type != null) {
            ErrorInfo eInfo = ErrorManager.getInstance().getErrorDefaultMap().get(type);
            if (IActionTitleBar.ErrorType.ERRORTOP == type) {
                findViewById(R.id.ac_base_net_error_header_notice).setVisibility(View.VISIBLE);
            } else {
                mNetWorkErrorView.show(eInfo.errorIconRes,
                        TextUtils.isEmpty(fistString) ? getContext().getString(eInfo.errorTextRes) : fistString,
                        TextUtils.isEmpty(secondString) ? getContext().getString(eInfo.errorMessageRes) : secondString,
                        buttonText,
                        errorViewClick);
            }
        }
    }

    /**
     * 隐藏错误页面
     *
     * @param errorViewParent 可以根据需要设置要显示错误页面的父布局，
     *                        <li>需要和{@link #showErrorView(ViewGroup, IActionTitleBar.ErrorType, String, String, String, ErrorViewClick)}</li>
     *                        <li>中的{@link ViewGroup}保持一直</li>
     *                        <li>，默认使用{@link #mContentParentView}填充</li>
     */
    public void hideErrorView(@Nullable ViewGroup errorViewParent) {
        if (mNetWorkErrorView != null) {
            if (errorViewParent != null) {
                errorViewParent.removeView(mNetWorkErrorView);
            } else {
                mContentParentView.removeView(mNetWorkErrorView);
            }
            mNetWorkErrorView = null;
            isViewAdded = false;
        }
    }

    /**
     * 显示状态加载条
     *
     * @param msg 提示信息
     */
    public void showLoadingView(String msg) {
        if (mLoadingDialog == null) {
            if (getContext() instanceof Activity) {
                mLoadingDialog = DialogUtil.showLoadingDialog((Activity) getContext(), msg, true);
            } else {
                HarwkinLogUtil.info("please use Activity's context");
                return;
            }
        }

        mLoadingDialog.setDlgMessage(msg);

        if (getContext() instanceof Activity) {
            Activity ac = (Activity) getContext();
            if (ac == null || ac.isFinishing()) {
                return;
            }
        }
        if (!mLoadingDialog.isShowing()) {
            mLoadingDialog.show();
        }
    }

    public void showLoadingView(String msg, int color) {
        if (mLoadingDialog == null) {
            if (getContext() instanceof Activity) {
                mLoadingDialog = DialogUtil.showLoadingDialog((Activity) getContext(), msg, true);
            } else {
                HarwkinLogUtil.info("please use Activity's context");
                return;
            }
        }
        if (color != -1) {
            mLoadingDialog.setDlgMsgColor(getContext(), color);
        }
        mLoadingDialog.setDlgMessage(msg);
        if (getContext() instanceof Activity) {
            Activity ac = (Activity) getContext();
            if (ac == null || ac.isFinishing()) {
                return;
            }
        }
        if (!mLoadingDialog.isShowing()) {
            mLoadingDialog.show();
        }
    }


    /**
     * 隐藏状态加载提示
     */
    public void hideLoadingView() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            try {
                mLoadingDialog.dismiss();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 沉浸式导航栏的内容高度设置
     *
     * @param topPadding
     */
    public void setTopPadding(int topPadding) {
        if (mNavParentView != null) {
            mNavParentView.setPadding(0, topPadding, 0, 0);
        }
    }

    /**
     * 若导航栏需要覆盖内容布局，则调用此方法
     */
    public void setDisallowTopCover(boolean flag) {
        if (mContentParentView != null) {
            if (flag) {
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mContentParentView.getLayoutParams();
                layoutParams.addRule(RelativeLayout.BELOW, R.id.ac_base_nav_title_view);
                mContentParentView.setLayoutParams(layoutParams);
            } else {
                RelativeLayout.LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                mContentParentView.setLayoutParams(layoutParams);
            }
        }
    }

}
