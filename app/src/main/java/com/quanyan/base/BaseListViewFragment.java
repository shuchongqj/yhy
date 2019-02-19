package com.quanyan.base;

import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.quanyan.base.view.BaseDropListView;
import com.quanyan.base.view.customview.dropdownview.DropDownMenu;
import com.quanyan.base.view.customview.pulltorefresh.PullToRefreshBase;
import com.quanyan.base.view.customview.pulltorefresh.PullToRefreshObservableListView;
import com.quanyan.base.yminterface.IBaseDropList;
import com.quanyan.yhy.ui.adapter.base.QuickAdapter;
import com.yhy.common.constants.Constants;
import com.yhy.common.constants.ValueConstants;

/**
 * Created with Android Studio.
 * Title:AboutAndFeedController
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:zhaoxiaopo
 * Date:16/4/28
 * Time:17:08
 * Version 1.0
 */
public abstract class BaseListViewFragment<T> extends BaseFragment implements IBaseDropList<T, ObservableListView> {
    protected BaseDropListView mBaseDropListView;
    private boolean isRefresh = true;//当前是否处于刷新状态
    private boolean haxNext = true;//列表是否有下一页
    private int mPageIndex = 1;//当前页数

    @Override
    public View onLoadContentView() {
        mBaseDropListView = new BaseDropListView(getActivity());
        mBaseDropListView.setIBaseDropList(this);
        return mBaseDropListView;
    }

    public BaseDropListView getBaseDropListView() {
        return mBaseDropListView;
    }

    public PullToRefreshObservableListView getPullListView() {
        if (mBaseDropListView != null) {
            return mBaseDropListView.getPullRefreshView();
        }
        return null;
    }

    public void setPageIndex(int pageIndex){
        mPageIndex = pageIndex;
    }
    
    /**
     * 获取当前是否处于刷新操作的状态
     *
     * @return boolean
     */
    public boolean isRefresh() {
        return isRefresh;
    }

    /**
     * 获取是否还有下一页的判断
     *
     * @return boolean
     */
    public boolean isHaxNext() {
        return haxNext;
    }

    /**
     * 设置是否还有下一页
     *
     * @param haxNext true ： 有下一页，false ：没有下一页
     */
    public void setHaxNext(boolean haxNext) {
        this.haxNext = haxNext;
    }

    /**
     * 获取当前的页数
     *
     * @return 当前页数
     */
    public int getPageIndex() {
        return mPageIndex;
    }

    /**
     * 获取一页加载的数据数量 默认20，如果有修改子类重写此方法
     */
    public int getPageSize() {
        return Constants.PAGE_SIZE_DEFAULT;
    }

    /**
     * 获取listview设置的Adapter，用于设置数据
     *
     * @return {@link QuickAdapter}，没有则返回NULL
     */
    public QuickAdapter<T> getAdapter() {
        if (mBaseDropListView != null) {
            return mBaseDropListView.getQuickAdapter();
        }
        return null;
    }

    /**
     * 获取listview设置的Adapter，用于设置数据
     *
     * @return extends {@link BaseAdapter}，没有则返回NULL
     */
    public BaseAdapter getCustomAdapter(){
        if (mBaseDropListView != null) {
            return mBaseDropListView.getCustomAdapter();
        }
        return null;
    }

    /**
     * 是否需要自己定义Adapter
     *
     * @return if you need to custom the Adapter, return true, or false
     */
    @Override
    public boolean isNeedCustomAdater() {
        return false;
    }

    /**
     * @return
     */
    @Override
    public BaseAdapter setCustomAdapter() {
        return null;
    }

    /**
     * 若需要在筛选条件上方添加其他布局信息，就重写该方法
     *
     * @param dropViewParent <li>{@link DropDownMenu}的父布局, 使用时注意将View添加到第一个位置</li>
     *                       <li>使用{@link ViewGroup#addView(View, int)}, 索引值从0开始</li>
     */
    @Override
    public void addViewAboveDrop(LinearLayout dropViewParent) {

    }

    /**
     * 获取网路数据需要时直接重写该方法
     *
     * @param pageIndex 当前页数，子类不可改变的数值
     */
    public abstract void fetchData(final int pageIndex);

    /**
     * 手动调用刷新操作
     */
    protected void manualRefresh() {
        if (mBaseDropListView != null) {
            mBaseDropListView.getListView().setSelection(0);
            onPullDownToRefresh(mBaseDropListView.getPullRefreshView());
        }
    }

    /**
     * 下拉刷新的回调接口
     *
     * @param refreshView 刷新的ListView对象
     */
    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ObservableListView> refreshView) {
        isRefresh = true;
        mPageIndex = 1;
        fetchData(mPageIndex);
    }

    /**
     * 加载更多的回调接口
     *
     * @param refreshView 刷新的ListView对象
     */
    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ObservableListView> refreshView) {
        isRefresh = false;
        if (haxNext) {
//            haxNext = false;
            mPageIndex++;
            fetchData(mPageIndex);
        } else {
            Message.obtain(mHandler, ValueConstants.MSG_HAS_NO_DATA).sendToTarget();
        }
    }

    /**
     * LisView滑动状态的回调接口
     *
     * @param view        滑动的ListView
     * @param scrollState 当前滑动的状态{@link android.widget.AbsListView.OnScrollListener}
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    /**
     * ListView滑动的距离信息，当前可见的Item数量
     *
     * @param view             ListView
     * @param firstVisibleItem 第一个可见的Item
     * @param visibleItemCount 可见Item的总数
     * @param totalItemCount   listview中的item总数
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    @Override
    public void handleMessage(Message msg) {
        dispatchMessage(msg);
        refreshCurrentPage();
    }

    /**
     * 子类调用dispatchMessage处理数据后 刷新当前page
     */
    private void refreshCurrentPage() {
        int count = 0;
        if (mBaseDropListView != null) {
            QuickAdapter adapter = mBaseDropListView.getQuickAdapter();
            count = adapter.getCount();
        }
        int pageSize = getPageSize();
        if (count == 0) {
            mPageIndex = 1;
        } else if (count % pageSize == 0) {
            mPageIndex = count / pageSize;
        } else {
            mPageIndex = (count / pageSize) + 1;
        }
    }

    /**
     * 处理 请求网络数据后返回的Message
     *
     * @param msg
     */
    public abstract void dispatchMessage(Message msg);
}
