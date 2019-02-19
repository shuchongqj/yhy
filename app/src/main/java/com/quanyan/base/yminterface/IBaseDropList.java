package com.quanyan.base.yminterface;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.quanyan.base.view.customview.dropdownview.DropDownMenu;
import com.quanyan.base.view.customview.pulltorefresh.PullToRefreshBase;
import com.quanyan.yhy.ui.adapter.base.BaseAdapterHelper;

import java.util.List;

/**
 * Created with Android Studio.
 * Title:AboutAndFeedController
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:zhaoxiaopo
 * Date:16/4/29
 * Time:15:06
 * Version 1.0
 */
public interface IBaseDropList<T, V extends ListView> {

    /**
     * 是否需要自己定义Adapter
     * @return if you need to custom the Adapter, return true, or false
     */
    boolean isNeedCustomAdater();

    /**
     * 设置自定义的Adapter，必须继承自{@link BaseAdapter}
     * @return
     */
    BaseAdapter setCustomAdapter();

    /**
     * 处理列表布局的item方法
     *
     * @param helper 提供处理布局内容的工具类
     * @param item item数据对象
     */
    void convertItem(BaseAdapterHelper helper, T item);
    /**
     * 设置列表布局
     *
     * @return 布局资源ID
     */
    int setItemLayout();

    /**
     * 若有筛选列表，则返回标题内容布局列表，数量需要和 {@link #setPopViews()}的数量一致
     * @return
     */
    @Nullable
    List<String> setTabStrings();

    /**
     * 若有筛选列表，则返回标题内容列表
     * @return
     */
    @Nullable
    List<View> setPopViews();

    /**
     * 若有需要可再下拉选项上方添加其他头部布局
     *
     * @param dropViewParent {@link DropDownMenu}的父布局
     */
    void addViewAboveDrop(LinearLayout dropViewParent);

    void onItemClick(AdapterView<?> parent, View view, int position, long id);

    void onPullDownToRefresh(PullToRefreshBase<V> refreshView);

    void onPullUpToRefresh(PullToRefreshBase<V> refreshView);

    void onScrollStateChanged(AbsListView view, int scrollState);

    void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount);
}
