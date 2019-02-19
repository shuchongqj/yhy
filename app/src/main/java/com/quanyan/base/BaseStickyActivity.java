package com.quanyan.base;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.quanyan.base.util.HarwkinLogUtil;
import com.quanyan.base.util.ScreenSize;
import com.quanyan.base.view.customview.pulltorefresh.PullToRefreshBase;
import com.quanyan.base.view.customview.pulltorefresh.PullToRefreshSticyView;
import com.quanyan.base.view.customview.stickyview.StickyNavLayout;
import com.quanyan.base.view.customview.tabscrollindicator.SlidingTabLayout;
import com.quanyan.yhy.R;
import com.quanyan.yhy.ui.adapter.DetailViewPagerAdapter;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;

import java.util.ArrayList;

/**
 * Created with Android Studio.
 * Title:AboutAndFeedController
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:zhaoxiaopo
 * Date:16/5/3
 * Time:09:53
 * Version 1.0
 */
public abstract class BaseStickyActivity extends BaseActivity implements
        PullToRefreshBase.OnRefreshListener<StickyNavLayout>, SlidingTabLayout.TabClick {

    /**
     * 下拉刷新的ListView
     */
    protected PullToRefreshSticyView mPullToRefreshSticyView;

    /**
     * 粘性布局的父容器
     */
    protected StickyNavLayout mStickyNavLayout;

    /**
     * StickyView的顶部布局的父容器
     */
    private LinearLayout mTopView;
    /**
     * ViewPager标题的TAB布局
     */
    private SlidingTabLayout mSlidingTabLayout;
    /**
     * ViewPager的引用
     */
    protected ViewPager mContentViewPager;

    /**
     * ViewPager标题数组
     */
    private DetailViewPagerAdapter mAdapter;
    /**
     * Fragment的引用
     */
    protected ArrayList<Fragment> mFragments;

    /**
     * 导航栏布局的父容器
     */
    private FrameLayout mTopLayout;
    /**
     * 底部布局的父容器
     */
    private LinearLayout mBottomLayout;
    private LinearLayout mRightPanelLayout;

    /**
     * 当前页数，默认为1
     */
    private int mPageIndex = 1;
    /**
     * 刷新状态
     */
    private boolean isRefresh = true;

    /**
     * 高度信息，用于设置ViewPager的高度
     */
    private int mTopViewHeight, mSlidingViewHeight, mBottomViewHeight;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void initView(Bundle savedInstanceState) {
        mPullToRefreshSticyView = (PullToRefreshSticyView) findViewById(R.id.base_sticky_refresh_layout);
        mTopView = (LinearLayout) findViewById(R.id.id_stickynavlayout_topview);
        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.id_stickynavlayout_indicator);
        mContentViewPager = (ViewPager) findViewById(R.id.id_stickynavlayout_viewpager);
        mBottomLayout = (LinearLayout) findViewById(R.id.base_sticky_bottom_layout);
        mTopLayout = (FrameLayout) findViewById(R.id.ac_base_nav_title_view);
        mRightPanelLayout = (LinearLayout) findViewById(R.id.base_sticky_right_panel);
        mStickyNavLayout = mPullToRefreshSticyView.getRefreshableView();

        if(addTopView() == null) {
            ToastUtil.showToast(this, getString(R.string.error_data_exception));
            finish();
            return;
//            throw new IllegalArgumentException("为添加顶部布局, 请传入正确的类型定义");
        }
        mTopView.addView(addTopView());

        mPullToRefreshSticyView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mPullToRefreshSticyView.setScrollingWhileRefreshingEnabled(!mPullToRefreshSticyView.isScrollingWhileRefreshingEnabled());
        mPullToRefreshSticyView.setOnRefreshListener(this);

        mFragments = setFragments();

        if(mFragments == null){
            mFragments = new ArrayList<>();
            HarwkinLogUtil.error("please set fragments");
        }
        mAdapter = new DetailViewPagerAdapter(getSupportFragmentManager(), mFragments, null);

        mAdapter.setTitles(setPagerTitles());
        mContentViewPager.setAdapter(mAdapter);
        mContentViewPager.setCurrentItem(0);

        //tab属性设置
        mSlidingTabLayout.setDistributeEvenly(true);
        mSlidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.ac_title_bg_color));
        mSlidingTabLayout.setTabViewTextSizeSp(15);
        mSlidingTabLayout.setOnTabClickListener(this);
        mSlidingTabLayout.setViewPager(mContentViewPager);

        resetViewHeight();

        mStickyNavLayout.requestLayout();

        initOthers();
    }

    protected abstract void initOthers();

    /**
     *
     * @return
     */
    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.ac_base_sticky, null);
    }

    /**
     * 若布局发生变化，则调用该方法重置ViewPager的高度，否则会引起内容显示不全的问题
     */
    public void resetViewHeight(){
        mTopLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mTopLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                if(isTopCover()) {
                    mStickyNavLayout.setTopHeight(mTopLayout.getMeasuredHeight());
                    mStickyNavLayout.requestLayout();
                }
                mTopViewHeight = mTopLayout.getMeasuredHeight();
                setViewPagerHeight();
            }
        });

        mBottomLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mBottomLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                mBottomViewHeight = mBottomLayout.getMeasuredHeight();
                setViewPagerHeight();
            }
        });

        mSlidingTabLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mSlidingTabLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                mSlidingViewHeight = mSlidingTabLayout.getMeasuredHeight();
                setViewPagerHeight();
            }
        });
    }

    /**
     * 设置ViewPager的高度
     */
    private void setViewPagerHeight(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mContentViewPager.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ScreenSize.getScreenHeightIncludeStatusBar(getApplicationContext())
                            - mTopViewHeight
                            - mSlidingViewHeight
                            - mBottomViewHeight));
        }else{
            mContentViewPager.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ScreenSize.getScreenHeightExcludeStatusBar(getApplicationContext())
                            - mTopViewHeight
                            - mSlidingViewHeight
                            - mBottomViewHeight));
        }
    }

    /**
     * 线路显示右边的panel列表
     * @param panelView 被添加的RightPanelView
     */
    protected void addRightPanel(View panelView){
        mRightPanelLayout.addView(panelView);
    }

    /**
     * 添加底部布局
     * @param bottomView 底部布局的父容器对象，需要添加底部布局的时候使用该对象添加
     */
    protected void addBottomView(View bottomView){
        mBottomLayout.addView(bottomView);
        mBottomLayout.requestLayout();
    }

    protected void resetTopView(){
        mTopView.removeAllViews();
        mTopView.addView(addTopView());
    }

    public DetailViewPagerAdapter getAdapter() {
        return mAdapter;
    }

    public SlidingTabLayout getSlidingTabLayout() {
        return mSlidingTabLayout;
    }

    public StickyNavLayout getStickyNavLayout() {
        return mStickyNavLayout;
    }

    public PullToRefreshSticyView getPullToRefreshSticyView() {
        return mPullToRefreshSticyView;
    }

    /**
     * 调用onPullRefresh刷新列表时，更新TAB的数据
     */
    protected void updateTabData(){};

    /**
     * 下拉刷新，重新获取数据，重置{@link #isRefresh}和{@link #mPageIndex}
     * @param refreshView 执行刷新操作的View
     */
    @Override
    public void onRefresh(PullToRefreshBase<StickyNavLayout> refreshView) {
        isRefresh = true;
        mPageIndex = 1;
        fetchData(mPageIndex);
    }

    /**
     * 手动调用刷新操作
     */
    protected void manualRefresh(){
        onRefresh(mPullToRefreshSticyView);
    }

    /**
     * 添加{@link SlidingTabLayout}布局上方的布局信息
     *
     * @return 顶部View布局的引用
     */
    protected abstract View addTopView();

    /**
     * 设置ViewPager的标题内容
     * @return 包含标题的List数组
     */
    protected abstract ArrayList<String> setPagerTitles();

    /**
     * 设置所需要的ViewPager信息，数量需要和{@link #setPagerTitles()}返回的一致
     * @return Fragment数组
     */
    protected abstract ArrayList<Fragment> setFragments();

    /**
     * 从网络获取数据
     * @param pageIndex 当前页数，在该方法内部子类不需要改变pageIndex的值，否则会引起页数不一致
     */
    public abstract void fetchData(final int pageIndex);
}
