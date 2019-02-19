package com.quanyan.base;

import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.FrameLayout;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.quanyan.base.baseenum.IActionTitleBar;
import com.quanyan.base.util.DateUtil;
import com.quanyan.base.util.HarwkinLogUtil;
import com.quanyan.base.util.ScreenSize;
import com.quanyan.base.view.BaseView;
import com.quanyan.base.yminterface.ErrorViewClick;
import com.quanyan.base.yminterface.IBaseView;
import com.quanyan.yhy.R;
import com.yhy.common.base.NoLeakHandler;
import com.yhy.router.YhyRouter;
import com.yhy.service.IUserService;

import java.lang.reflect.Field;

import de.greenrobot.event.EventBus;

/**
 * Created with Android Studio.
 * Title:AboutAndFeedController
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:zhaoxiaopo
 * Date:16/4/27
 * Time:16:38
 * Version 1.0
 */
public abstract class BaseFragment extends Fragment implements IBaseView,NoLeakHandler.HandlerCallback,AbsListView.OnScrollListener {

    private BaseView mBaseView;
    protected NoLeakHandler mHandler;
    protected boolean isDataInitial = false;//是否从网络中获取到数据
    private boolean isTopCover;

    /**
     * 初始化{@link NoLeakHandler}对象
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        YhyRouter.getInstance().inject(this);
        HarwkinLogUtil.info(getClass().getSimpleName(),"Fragment create time : " + DateUtil.convert2String(System.currentTimeMillis(), "HH:mm:ss.SSS"));

        //沉浸式状态栏的设置
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && isUseImmersiveStyle()) {
            Window window = getActivity().getWindow();
// Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
// Translucent navigation bar
//            window.setFlags(
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        mHandler = new NoLeakHandler(this);
    }

    /**
     * 提供父布局容器，初始化{@link BaseView}，对外提供{@link #initView(View, Bundle)}方法用于设置其他布局信息
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return View
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        HarwkinLogUtil.info(getClass().getSimpleName(),"Fragment createView time : " + DateUtil.convert2String(System.currentTimeMillis(), "HH:mm:ss.SSS"));

        mBaseView = new BaseView(getActivity());
        mBaseView.setIBaseView(this);

        isTopCover = isTopCover();
        if(!isTopCover){
            mBaseView.setDisallowTopCover(true);

        }
        //判断Build的版本，设置导航栏样式
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && isUseImmersiveStyle()) {
            mBaseView.setTopPadding(ScreenSize.getStatusBarHeight(getActivity().getApplicationContext()));
        }

        initView(mBaseView, savedInstanceState);
        return mBaseView;
    }

    /**
     * 是否使用4.4  API 19 以上的沉浸式样式
     *
     * @return 默认为true（重载方法可以重置）
     */
    @Override
    public boolean isUseImmersiveStyle() {
        return true;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        HarwkinLogUtil.info(getClass().getSimpleName(), "Fragment resume time : " + DateUtil.convert2String(System.currentTimeMillis(), "HH:mm:ss.SSS"));
    }

    @Override
    public void onPause() {
        super.onPause();
        HarwkinLogUtil.info(getClass().getSimpleName(),"Fragment pause time : " + DateUtil.convert2String(System.currentTimeMillis(), "HH:mm:ss.SSS"));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        HarwkinLogUtil.info(getClass().getSimpleName(),"Fragment destroyView time : " + DateUtil.convert2String(System.currentTimeMillis(), "HH:mm:ss.SSS"));

    }

    /**
     * 当Fragment结束后，需要将Handle中信息清空，同时设置标志位，禁止Handler再分发message
     */
    @Override
    public void onDetach() {
        super.onDetach();
        if(mHandler != null) {
            mHandler.setValid(false);
            mHandler.removeCallbacksAndMessages(null);
        }
        Field childFragmentManager = null;
        try {
            childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        HarwkinLogUtil.info(getClass().getSimpleName(),"Fragment destroy time : " + DateUtil.convert2String(System.currentTimeMillis(), "HH:mm:ss.SSS"));

        if (mHandler != null) {
            mHandler.setValid(true);
        }

        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState) {
            case SCROLL_STATE_IDLE:
//                ImageLoader.getInstance().resume();
                break;
            case SCROLL_STATE_TOUCH_SCROLL:
            case SCROLL_STATE_FLING:
//                ImageLoader.getInstance().pause();
                break;
        }
    }

    protected FrameLayout getNavParentView(){
        if(mBaseView != null) {
            return mBaseView.getNavParentView();
        }
        return null;
    }

    protected FrameLayout getContentParentView(){
        if(mBaseView != null) {
            return mBaseView.getContentParentView();
        }
        return null;
    }

    @Override
    public View onLoadContentView() {
        return null;
    }

    /**
     * 初始化布局对象
     *
     * @param view 跟布局对象
     * @param savedInstanceState
     */
    protected abstract void initView(View view, Bundle savedInstanceState);
    /**
     * 设置titleBar的背景颜色
     * @param colorRes
     */
    protected void setTitleBarBackground(int colorRes){
        if(mBaseView != null){
            mBaseView.setTitleBarBackground(colorRes);
        }
    }

    protected void setTitleBarBackGround(int alpha, int r, int g, int b){
        if(mBaseView != null){
            mBaseView.setTitleBarBackGround(alpha, r, g, b);
        }
    }

    /**
     * {@link Fragment}中默认不带导航栏，需要设置导航栏的时候重载该方法
     * @return NUll
     */
    @Deprecated
    @Override
    public View onLoadNavView() {
        return null;
    }

    /**
     * 默认处理Fragment中不需要导航栏
     * @return true
     */
    @Override
    public boolean isTopCover() {
        return true;
    }

    /**
     * 处理通过Handle发送的数据
     * @param msg {@link Message}对象
     */
    @Override
    public void handleMessage(Message msg) {

    }

    /**
     * 显示状态加载条
     * @param msg 提示信息
     */
    protected void showLoadingView(String msg){
        if(mBaseView != null){
            mBaseView.showLoadingView(msg);
        }
    }

    /**
     * 隐藏状态加载提示
     */
    protected void hideLoadingView(){
        if(mBaseView != null && isAdded()){
            mBaseView.hideLoadingView();
        }
    }
    /**
     * 显示错误页面
     *
     * @param errorViewParent 可以根据需要设置要显示错误页面的父布局，默认使用{@link #mBaseView}填充
     * @param type 错误类型由 {@link IActionTitleBar.ErrorType}定义
     * @param fistString 错误提示信息
     * @param secondString 错误提示内容
     * @param buttonText 是否有重新加载的按钮，如没有可以为空或者”“
     * @param errorViewClick 若显示重新加载的按钮，则传入响应点击事件
     */
    protected void showErrorView(@Nullable ViewGroup errorViewParent, IActionTitleBar.ErrorType type, String fistString,
                                 String secondString, String buttonText, ErrorViewClick errorViewClick) {
        if(mBaseView != null){
            mBaseView.showErrorView(errorViewParent, type, fistString, secondString, buttonText, errorViewClick);
        }
    }


    protected void setButtonColorRed() {
        if(mBaseView != null){
            mBaseView.mNetWorkErrorView.reload.setTextColor( getResources().getColor(R.color.main));
            mBaseView.mNetWorkErrorView.reload.setBackgroundResource(R.drawable.shape_tv_follow);
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
        if(mBaseView != null){
            mBaseView.hideErrorView(errorViewParent);
        }
    }
}
