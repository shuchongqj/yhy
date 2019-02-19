package com.quanyan.yhy.ui.comment;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.quanyan.base.BaseListViewFragment;
import com.quanyan.base.baseenum.IActionTitleBar;
import com.quanyan.base.view.customview.pulltorefresh.PullToRefreshBase;
import com.quanyan.base.yminterface.ErrorViewClick;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.AnalyDataValue;
import com.quanyan.yhy.common.CommentType;
import com.quanyan.yhy.common.ErrorCode;
import com.quanyan.yhy.ui.adapter.base.BaseAdapterHelper;
import com.quanyan.yhy.ui.base.utils.TCEventHelper;
import com.quanyan.yhy.ui.comment.bean.CommentTabBean;
import com.quanyan.yhy.ui.comment.controller.CommentController;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.line.lineinterface.IUpdateTab;
import com.quanyan.yhy.view.LabelLayout;
import com.yhy.common.beans.net.model.comment.RateCaseInfo;
import com.yhy.common.beans.net.model.comment.RateCaseList;
import com.yhy.common.beans.net.model.comment.RateInfo;
import com.yhy.common.beans.net.model.comment.RateInfoList;
import com.yhy.common.beans.net.model.comment.RateInfoQuery;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.SPUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created with Android Studio.
 * Title:FullCommentFragment
 * Description:评价列表界面
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-5-13
 * Time:14:38
 * Version 1.1.0
 */


public class FullCommentFragment extends BaseListViewFragment<RateInfo> implements IUpdateTab {
    //全部评价和有图button集
    private LabelLayout mCommentHeader;
    private CommentController mController;
    private ArrayList<CommentTabBean> mTabList;
    private int mCurrentTabPos = 0;
    private long mSearchId;
    private String mOrderType;
    private int mCheckId;//测试字段
    private RateInfoQuery mRateInfoQuery;
    private boolean flag = false;
    protected int mPageIndex = 1;
    private boolean isFirst = true;

    protected boolean isRefresh = true;
    private String mPageType;

    public static FullCommentFragment getInstance(long lineId, String type, String pageType, boolean flag) {
        FullCommentFragment fragment = new FullCommentFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(SPUtils.EXTRA_ID, lineId);
        bundle.putString(SPUtils.EXTRA_TYPE, type);
        bundle.putString(SPUtils.EXTRA_SOURCE, pageType);
        bundle.putBoolean(SPUtils.EXTRA_DATA, flag);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mController = new CommentController(getActivity(), mHandler);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mSearchId = bundle.getLong(SPUtils.EXTRA_ID, -1);
            mOrderType = bundle.getString(SPUtils.EXTRA_TYPE);
            mPageType = bundle.getString(SPUtils.EXTRA_SOURCE);
            flag = bundle.getBoolean(SPUtils.EXTRA_DATA);
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ObservableListView> refreshView) {
        mController.doGetRateCaseList(getActivity(), mSearchId, mOrderType);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ObservableListView> refreshView) {
        if (hasMore) {
            doNetList((getAdapter().getCount() / ValueConstants.PAGESIZE) + 1);
        } else {
            mHandler.sendEmptyMessageDelayed(ValueConstants.MSG_HAS_NO_DATA, 1000);
        }
    }

    /**
     * 处理网络请求
     *
     * @param pageIndex 当前页数，子类不可改变的数值
     */
    @Override
    public void fetchData(int pageIndex) {
        /*showLoadingView(getString(R.string.loading_text));
        mRateInfoQuery.pageNo = pageIndex;
        mRateInfoQuery.pageSize = ValueConstants.PAGESIZE;
        //System.out.println("ApiContext" + mRateInfoQuery.condition);
        mController.doGetRateInfoPageList(mRateInfoQuery);*/
    }

    @Override
    public int getPageSize() {
        return ValueConstants.PAGESIZE;
    }

    private boolean hasMore = false;//是否还有下一页
    private boolean isOnBottomLoading = false;
    private RelativeLayout footerLayout;

    /**
     * 处理handlermessage的数据
     *
     * @param msg
     */
    @Override
    public void dispatchMessage(Message msg) {
        if (getActivity() == null) {
            return;
        }
        if (flag) {
            //重置到底部加载的状态
            isOnBottomLoading = false;
            if (!hasMore) {
                if (getPullListView().getRefreshableView() != null && footerLayout != null) {
                    getPullListView().getRefreshableView().removeFooterView(footerLayout);
                }
            }
        }

        hideLoadingView();
        getPullListView().onRefreshComplete();
        switch (msg.what) {
            case ValueConstants.MSG_COMMENT_LIST_HEADER_OK:
                hideErrorView(mBaseDropListView.getDropViewParent());
                RateCaseList result = (RateCaseList) msg.obj;
                if (result != null) {
                    bindHeaderView(result.rateCaseList);
                }
                break;
            case ValueConstants.MSG_COMMENT_LIST_HEADER_KO:
                showErrorView(mBaseDropListView.getDropViewParent(), ErrorCode.NETWORK_UNAVAILABLE == msg.arg1 ?
                        IActionTitleBar.ErrorType.NETUNAVAILABLE : IActionTitleBar.ErrorType.ERRORNET, "", "", "", new ErrorViewClick() {
                    @Override
                    public void onClick(View view) {
                        if (mSearchId != -1 && mOrderType != null) {
                            mController.doGetRateCaseList(getActivity(), mSearchId, mOrderType);
                        }
                        //refreshManual();
                    }
                });
                //ToastUtil.showToast(getActivity(), StringUtil.handlerErrorCode(getActivity(), msg.arg1));
                break;
            case ValueConstants.MSG_COMMENT_LIST_OK:
                hideErrorView(mBaseDropListView.getListViewViewParent());
//                hideErrorView(null);
                RateInfoList resultRate = (RateInfoList) msg.obj;
                if (resultRate != null) {
                    hasMore = resultRate.hasNext;
                    bindListData(resultRate.rateInfoList);
                }
                break;
            case ValueConstants.MSG_COMMENT_LIST_KO:
                showErrorView(mBaseDropListView.getListViewViewParent(), ErrorCode.NETWORK_UNAVAILABLE == msg.arg1 ?
                        IActionTitleBar.ErrorType.NETUNAVAILABLE : IActionTitleBar.ErrorType.ERRORNET, "", "", "", new ErrorViewClick() {
                    @Override
                    public void onClick(View view) {
                        //refreshManual();
                    }
                });
                break;
            case ValueConstants.MSG_HAS_NO_DATA:
                ToastUtil.showToast(getActivity(), getString(R.string.scenic_hasnodata));
                break;
        }
    }

    /**
     * 处理列表返回的数据
     *
     * @param value
     */
    private void bindListData(List<RateInfo> value) {

        /*if (isRefresh) {
            if (value != null && value.size() > 0) {
                getAdapter().replaceAll(value);
            } else {
                getAdapter().clear();
                showNoDataPage();
            }
        } else {
            if (value != null) {
                getAdapter().addAll(value);
            }
        }*/

        if (isRefresh) {
            if (value != null && value.size() > 0) {
                getAdapter().replaceAll(value);
            } else {
                getAdapter().clear();
                showNoDataPage();
            }
        } else {
            if (value != null) {
                getAdapter().addAll(value);
            } else {
                ToastUtil.showToast(getActivity(), getString(R.string.no_more));
            }
        }
    }

    //空数据显示
    private void showNoDataPage() {
        //showErrorView(null, IActionTitleBar.ErrorType.EMPTYVIEW, getString(R.string.label_hint_no_comments), " ", "", null);
        showErrorView(mBaseDropListView.getListViewViewParent(), IActionTitleBar.ErrorType.EMPTYVIEW, getString(R.string.label_hint_no_comments), " ", "", null);
    }

    /**
     * 评价头部数据处理
     *
     * @param list
     */
    private void bindHeaderView(List<RateCaseInfo> list) {
        if (list == null || list.size() <= 0) {
            return;
        }

        if (mTabList != null) {
            for (int i = 0; i < mCommentHeader.getChildCount(); i++) {
                TextView childAt = (TextView) mCommentHeader.getChildAt(i);
                childAt.setText(list.get(i).name + " " + String.format(getResources().getString(R.string.full_comment_count), list.get(i).count));
            }
        } else {
            mTabList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
//              View view = View.inflate(getActivity(), R.layout.label_comment_header, null);
//              TextView textView = (TextView) view.findViewById(R.id.tv_header);
                TextView textView = new TextView(getActivity().getApplicationContext());
                textView.setTextColor(getResources().getColor(R.color.neu_666666));
                textView.setGravity(Gravity.CENTER);
                textView.setTextSize(12);
                textView.setPadding(10, 8, 10, 8);
                LabelLayout.LayoutParams layoutParams = new LabelLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.rightMargin = 20;
//            textView.setLayoutParams(layoutParams);
                CommentTabBean tabBean = new CommentTabBean();
                tabBean.setId(i);
                if (isFirst) {//第一次进入
                    isFirst = false;
                    if (i == 0) {
                        tabBean.setIsChecked(true);
                    } else {
                        tabBean.setIsChecked(false);
                    }
                }
                tabBean.setCode(list.get(i).code);
                tabBean.setTextView(textView);
                mTabList.add(tabBean);
                textView.setOnClickListener(new TabClick(mTabList, i));
                textView.setText(list.get(i).name + " " + String.format(getResources().getString(R.string.full_comment_count), list.get(i).count));
                mCommentHeader.addView(textView, layoutParams);
            }
        }


        if (mTabList != null && mTabList.size() > 0) {
            refreshTextView();
        }
    }

    //设置textView的字体背景，网络请求数据
    private void refreshTextView() {
        for (int i = 0; i < mTabList.size(); i++) {
            if (mTabList.get(i).isChecked()) {
                mTabList.get(i).getTextView().setTextColor(getResources().getColor(R.color.ac_title_bg_color));
                mTabList.get(i).getTextView().setBackgroundResource(R.drawable.tab_comment_checked);
                //封装请求字段，访问网络
                if (mRateInfoQuery == null) {
                    mRateInfoQuery = new RateInfoQuery();
                    mRateInfoQuery.outId = mSearchId;
                    if (CommentType.LOCAL.equals(mOrderType) || CommentType.LINE.equals(mOrderType) || CommentType.POINT.equals(mOrderType) || CommentType.CONSULT.equals(mOrderType)) {
                        mRateInfoQuery.outType = CommentType.ITEM;
                    } else {
                        mRateInfoQuery.outType = mOrderType;
                    }
                }
                mRateInfoQuery.condition = mTabList.get(i).code;
                //访问列表数据
                doNetList(1);
                //refreshManual();
            } else {
                mTabList.get(i).getTextView().setTextColor(getResources().getColor(R.color.neu_666666));
                mTabList.get(i).getTextView().setBackgroundResource(R.drawable.tab_comment_unchecked);
            }
        }
    }

    private void doNetList(int pageIndex) {
        if (1 == pageIndex) {
            isRefresh = true;
        } else {
            isRefresh = false;
        }
        mRateInfoQuery.pageNo = pageIndex;
        mRateInfoQuery.pageSize = ValueConstants.PAGESIZE;
//        System.out.println("wjm===" + mRateInfoQuery.outType);
        mController.doGetRateInfoPageList(getActivity(), mRateInfoQuery);
    }

    //手动刷新
    private void refreshManual() {
        manualRefresh();
        getPullListView().getRefreshableView().setSelection(0);
    }

    @Override
    public void updateTabContent() {
        if (mSearchId != -1 && mOrderType != null && mController != null) {
            mController.doGetRateCaseList(getActivity(), mSearchId, mOrderType);
        }
    }

    private class TabClick implements View.OnClickListener {

        private ArrayList<CommentTabBean> mTabList;
        private int clickNum;

        public TabClick(ArrayList<CommentTabBean> mTabList, int clickNum) {
            this.mTabList = mTabList;
            this.clickNum = clickNum;
        }

        @Override
        public void onClick(View v) {
            if (mCurrentTabPos == clickNum) {
                return;
            }
            mCurrentTabPos = clickNum;
            for (int i = 0; i < mTabList.size(); i++) {
                if (mCurrentTabPos == mTabList.get(i).getId()) {
                    mTabList.get(i).setIsChecked(true);
                } else {
                    mTabList.get(i).setIsChecked(false);
                }
            }
            //打点
            Map<String, String> map = new HashMap<>();
            map.put(AnalyDataValue.KEY_ID, String.valueOf(mSearchId));
            map.put(AnalyDataValue.KEY_TYPE, AnalyDataValue.getType(mPageType));
            map.put(AnalyDataValue.KEY_FILTER_ID, String.valueOf(mTabList.get(mCurrentTabPos).getId()));
            map.put(AnalyDataValue.KEY_FILTER_NAME, mTabList.get(mCurrentTabPos).code);
            TCEventHelper.onEvent(getActivity(), AnalyDataValue.COMMENTS_FILTER, map);
            refreshTextView();
        }
    }

    /**
     * 在putorefresh上面加头
     */
    @Override
    public void addViewAboveDrop(LinearLayout dropViewParent) {
        super.addViewAboveDrop(dropViewParent);
        View inflate = View.inflate(getActivity(), R.layout.header_comment_label, null);
        mCommentHeader = (LabelLayout) inflate.findViewById(R.id.lb_comment_header);
        dropViewParent.addView(inflate, 0);
        if (mOrderType.equals(CommentType.CONSULT)) {
            inflate.setVisibility(View.GONE);
        }

    }

    /**
     * 添加头布局等
     *
     * @param view               跟布局对象
     * @param savedInstanceState
     */
    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        //评价列表头部信息获取
        if (flag) {
            getPullListView().setMode(PullToRefreshBase.Mode.DISABLED);
            getPullListView().getRefreshableView().setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    if (hasMore) {
                        if (firstVisibleItem > 0 && totalItemCount > 0 && (firstVisibleItem + visibleItemCount == totalItemCount)) {
                            if (!isOnBottomLoading) {
                                isOnBottomLoading = true;
                                // TODO: 16/3/3 获取更多数据
                                onPullUpToRefresh(getPullListView());
                            }
                        }
                    }
                }
            });
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            footerLayout = (RelativeLayout) inflater.inflate(R.layout.footer_loading_more, getPullListView().getRefreshableView(), false);
            getPullListView().getRefreshableView().addFooterView(footerLayout);
        }
        if (mSearchId != -1 && mOrderType != null) {
            mController.doGetRateCaseList(getActivity(), mSearchId, mOrderType);
        }

        if (mRateInfoQuery == null) {
            mRateInfoQuery = new RateInfoQuery();
            mRateInfoQuery.outId = mSearchId;
            if (CommentType.LOCAL.equals(mOrderType) || CommentType.LINE.equals(mOrderType) || CommentType.POINT.equals(mOrderType) || CommentType.CONSULT.equals(mOrderType)) {
                mRateInfoQuery.outType = CommentType.ITEM;
            } else {
                mRateInfoQuery.outType = mOrderType;
            }
        }
    }

    /**
     * 处理item的数据
     *
     * @param helper 提供处理布局内容的工具类
     * @param item   item数据对象
     */
    @Override
    public void convertItem(BaseAdapterHelper helper, RateInfo item) {
        CommentItemHelper.handleItem(getActivity(), helper, item, mPageType, mSearchId);
    }

    /**
     * item的布局
     *
     * @return
     */
    @Override
    public int setItemLayout() {
        return R.layout.item_commentlist;
    }

    @Nullable
    @Override
    public List<String> setTabStrings() {
        return null;
    }

    @Nullable
    @Override
    public List<View> setPopViews() {
        return null;
    }

    @Override
    public void onItemClick(AdapterView parent, View view, int position, long id) {

    }
}
