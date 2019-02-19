package com.yhy.common.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;

import de.greenrobot.event.EventBus;

public abstract class BaseNewFragment extends Fragment implements NoLeakHandler.HandlerCallback{
    protected Activity mActivity;
    protected View mRootView;
    protected boolean viewsReady = false;//控件是否初始化完成
    protected NoLeakHandler mHandler;
    public String tag  = getClass().getSimpleName();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ARouter.getInstance().inject(this);
        mHandler = new NoLeakHandler(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(setLayoutId(), container, false);
        initVars();
        initView();
        viewsReady = true;
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setListener();
        initData();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        viewsReady = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("ezez","======onDestroy = true");
        if(mHandler != null) {
            mHandler.setValid(false);
            mHandler.removeCallbacksAndMessages(null);
        }

        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    /**
     * Sets layout id.
     *
     * @return the layout id
     */
    protected abstract int setLayoutId();

    /**
     * 初始化参数
     */
    protected void initVars() {

    }

    /**
     * view与数据绑定
     */
    protected void initView() {

    }

    /**
     * 设置监听
     */
    protected void setListener() {

    }

    /**
     * 初始化数据
     */
    protected void initData() {

    }

    /**
     * 找到activity的控件
     *
     * @param <T> the type parameter
     * @param id  the id
     * @return the t
     */
    @SuppressWarnings("unchecked")
    protected <T extends View> T findActivityViewById(@IdRes int id) {
        return (T) mActivity.findViewById(id);
    }

    /**
     * 找到activity的控件
     *
     * @param <T> the type parameter
     * @param id  the id
     * @return the t
     */
    @SuppressWarnings("unchecked")
    protected <T extends View> T findFragmentViewById(@IdRes int id) {
        return (T) mRootView.findViewById(id);
    }

    /**
     * 处理返回键。
     * @return true表示吃掉返回事件，将不再继续传递，false则会继续传递直到ativity结束
     */
    protected boolean onBackPressed(){
        return false;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
