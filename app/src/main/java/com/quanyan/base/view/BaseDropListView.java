package com.quanyan.base.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.quanyan.base.baseenum.IActionTitleBar;
import com.quanyan.base.view.customview.dropdownview.DropDownMenu;
import com.quanyan.base.view.customview.pulltorefresh.PullToRefreshBase;
import com.quanyan.base.view.customview.pulltorefresh.PullToRefreshObservableListView;
import com.quanyan.base.yminterface.ErrorViewClick;
import com.quanyan.base.yminterface.IBaseDropList;
import com.quanyan.yhy.R;
import com.quanyan.yhy.ui.adapter.base.BaseAdapterHelper;
import com.quanyan.yhy.ui.adapter.base.QuickAdapter;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:AboutAndFeedController
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:zhaoxiaopo
 * Date:16/4/29
 * Time:15:04
 * Version 1.0
 */
public class BaseDropListView<T> extends LinearLayout implements AbsListView.OnScrollListener,
        AdapterView.OnItemClickListener, PullToRefreshBase.OnRefreshListener2<ObservableListView> {

    private IBaseDropList mIBaseDropList;

    private LinearLayout mDropViewParent;
    private LinearLayout mListViewViewParent;
    private DropDownMenu mDropDownMenu;
    private PullToRefreshObservableListView mPullToRefreshListView;
    private ObservableListView mListView;
    private BaseAdapter mQuickAdapter;

    protected List<T> mDatas = new ArrayList<>();

    public BaseDropListView(Context context) {
        super(context);
        initView(context);
    }

    public BaseDropListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public BaseDropListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BaseDropListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    /**
     * <li>设置接口信息，这对Fragment的设置，因为Fragment初始化布局的时候出入的时Activity的Context对象，</li>
     * <li>所以调用此接口初始化布局</li>
     * @param IBaseDropList
     */
    public void setIBaseDropList(IBaseDropList IBaseDropList) {
        mIBaseDropList = IBaseDropList;
        initOthers();
    }

    /**
     * 布局的初始化
     * @param context
     */
    private void initView(Context context) {
        setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        setBackgroundColor(getResources().getColor(R.color.neu_f4f4f4));
        View.inflate(context, R.layout.base_drop_down_layout, this);

        if (context instanceof IBaseDropList) {
            mIBaseDropList = (IBaseDropList) context;
            initOthers();
        }

    }

    /**
     * 私有方法，布局内的基本对象引用的初始化
     */
    private void initOthers(){
        mDropDownMenu = (DropDownMenu) findViewById(R.id.dropDownMenu);
        mDropViewParent = (LinearLayout) findViewById(R.id.base_drop_down_parent_layout);
        if(mIBaseDropList != null) {
            mIBaseDropList.addViewAboveDrop(mDropViewParent);
        }
        View contentView = View.inflate(
                getContext(), R.layout.base_pull_refresh_observablelistview_linear_sticky, null
        );
        mListViewViewParent = (LinearLayout) contentView.findViewById(R.id.base_pullrefresh_listview_parent_layout);
        mPullToRefreshListView = (PullToRefreshObservableListView) contentView.findViewById(R.id.id_stickynavlayout_innerscrollview);
        mListView = mPullToRefreshListView.getRefreshableView();

        mPullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
//        mPullToRefreshListView.setScrollingWhileRefreshingEnabled(!mPullToRefreshListView.isScrollingWhileRefreshingEnabled());

        if (mIBaseDropList != null) {
            if(!mIBaseDropList.isNeedCustomAdater()) {
                mQuickAdapter = new QuickAdapter<T>(getContext(), mIBaseDropList.setItemLayout(), mDatas) {
                    @Override
                    protected void convert(BaseAdapterHelper helper, T item) {
                        mIBaseDropList.convertItem(helper, item);
                    }
                };
                mListView.setAdapter(mQuickAdapter);
            }else{
                if(mIBaseDropList.setCustomAdapter() != null){
                    mQuickAdapter = mIBaseDropList.setCustomAdapter();
                    mListView.setAdapter(mQuickAdapter);
                }else{
                    ToastUtil.showToast(getContext(), "please set adapter");
                }
            }
            mDropDownMenu.setDropDownMenu(mIBaseDropList.setTabStrings(), mIBaseDropList.setPopViews(), contentView);
        }

        mPullToRefreshListView.setOnRefreshListener(this);
        mListView.setOnItemClickListener(this);
        mListView.setOnScrollListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mIBaseDropList != null) {
            mIBaseDropList.onItemClick(parent, view, position, id);
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ObservableListView> refreshView) {
        if (mIBaseDropList != null) {
            mIBaseDropList.onPullDownToRefresh(refreshView);
        }
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ObservableListView> refreshView) {
        if (mIBaseDropList != null) {
            mIBaseDropList.onPullUpToRefresh(refreshView);
        }
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
        if (mIBaseDropList != null) {
            mIBaseDropList.onScrollStateChanged(view, scrollState);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (mIBaseDropList != null) {
            mIBaseDropList.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }

    /**
     * 返回下拉刷新的布局
     * @return {@link PullToRefreshObservableListView}
     */
    public PullToRefreshObservableListView getPullRefreshView() {
        return mPullToRefreshListView;
    }

    public ListView getListView(){
        return mListView;
    }
    /**
     * 返回列表的适配器
     * @return {@link QuickAdapter}
     */
    public QuickAdapter<T> getQuickAdapter() {
        return (QuickAdapter<T>) mQuickAdapter;
    }

    /**
     * 返回列表的适配器
     * @return {@link QuickAdapter}
     */
    public BaseAdapter getCustomAdapter() {
        return mQuickAdapter;
    }

    /**
     * 返回下拉筛选列表的对象，进行筛选条件的设置
     * @return {@link DropDownMenu}
     */
    public DropDownMenu getDropDownMenu() {
        return mDropDownMenu;
    }

    /**
     * 返回下拉筛选列表的父布局引用，可用此对象在列表上方添加其他布局信息
     * @return {@link LinearLayout}
     */
    public LinearLayout getDropViewParent() {
        return mDropViewParent;
    }

    /**
     * <li>返回下拉刷新列表的父布局引用，此对象用来设置网络错误页面布局(一个参数)，避免错误页面将整个布局全部覆盖</li>
     * <li>{@link BaseView#showErrorView(ViewGroup, IActionTitleBar.ErrorType, String, String, String, ErrorViewClick)}</li>
     * @return {@link LinearLayout}
     */
    public LinearLayout getListViewViewParent() {
        return mListViewViewParent;
    }
}
