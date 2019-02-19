package com.quanyan.yhy.ui.signed.activity;

import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quanyan.base.BaseFragment;
import com.quanyan.base.baseenum.IActionTitleBar;
import com.quanyan.base.util.LocalUtils;
import com.quanyan.base.util.StringUtil;
import com.quanyan.base.view.customview.pulltorefresh.PullToRefreshBase;
import com.quanyan.base.view.customview.pulltorefresh.PullToRefreshListView;
import com.quanyan.base.yminterface.ErrorViewClick;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.ErrorCode;
import com.quanyan.yhy.ui.adapter.base.QuickAdapter;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.integralmall.integralmallcontroller.IntegralmallController;
import com.quanyan.yhy.ui.signed.signedviewhelper.signedViewHelper;
import com.yhy.common.beans.net.model.param.WebParams;
import com.yhy.common.beans.net.model.tm.DailyTaskQueryResult;
import com.yhy.common.beans.net.model.tm.Task;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.AndroidUtils;

import java.util.List;

/**
 * Created with Android Studio.
 * Title:EatGreatActivity
 * Description:任务列表
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:wjm
 * Date:2016-6-22
 * Time:12:05
 * Version 1.3.0
 */
public class SignedActivity extends BaseFragment implements View.OnClickListener {
    private ListView mListView;
    IntegralmallController integralmallController;
    private QuickAdapter<Task> mAdapter;
    protected int mPageIndex = 1;
    protected boolean isRefresh = true;
    private static int PAGESIZE = 15;
    private boolean mHasNext = true;
    private PullToRefreshListView mPullToRefreshListView;
    private RelativeLayout mParentLayout;
    private TextView tvSignedIntegralrule;

    @Override
    public View onLoadContentView() {
        return View.inflate(getActivity(), R.layout.activity_signed, null);
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mPullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.pull_to_refresh_listview);
        mParentLayout = (RelativeLayout) view.findViewById(R.id.base_parent_layout);
        //初始化页面数据
        integralmallController = new IntegralmallController(getActivity(), mHandler);
        initPlist();
        initEvent();
    }

    @Override
    public void onResume() {
        super.onResume();
        doNetDatas(mPageIndex);

    }
    private void doNetDatas(int pageIndex) {
        if (1 == pageIndex) {
            isRefresh = true;
        } else {
            isRefresh = false;
        }

        integralmallController.doDailyTaskQuery(getActivity(), pageIndex, PAGESIZE);
    }
    DailyTaskQueryResult task;
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        hideLoadingView();
        hideErrorView(null);
        //刷新完成
        mPullToRefreshListView.onRefreshComplete();
        switch (msg.what) {
            case ValueConstants.MSG_INTEGRALMALL_DAILYTASK_OK:
                 task = (DailyTaskQueryResult) msg.obj;
//                    mHasNext = task.hasNext;
                    handlePageData(task.list);

                break;
            case ValueConstants.MSG_INTEGRALMALL_DAILYTASK_KO:
                if (isRefresh) {
                    if (mAdapter.getCount() == 0) {
                        mAdapter.clear();
                        showErrorView(null, ErrorCode.NETWORK_UNAVAILABLE == msg.arg1 ?
                                        IActionTitleBar.ErrorType.NETUNAVAILABLE : IActionTitleBar.ErrorType.ERRORNET, "", "", "", new ErrorViewClick() {
                                    @Override
                                    public void onClick(View view) {
                                        doNetDatas(1);
                            }
                        });
                    } else {
                        AndroidUtils.showToastCenter(getActivity(), StringUtil.handlerErrorCode(getActivity(), msg.arg1));
                    }
                } else {
                    AndroidUtils.showToastCenter(getActivity(), StringUtil.handlerErrorCode(getActivity(), msg.arg1));
                }
                break;
            case ValueConstants.MSG_HAS_NO_DATA:
                ToastUtil.showToast(getActivity(), getString(R.string.scenic_hasnodata));
                break;
        }

    }

    private void handlePageData(List<Task> value) {

        if (isRefresh) {
            if (value != null && value.size() > 0) {
                mAdapter.replaceAll(value);
            } else {
                mHasNext = false; //测试
                mAdapter.clear();
                showNoDataPage();
            }
        } else {
            if (value != null && value.size() > 0) {
                mAdapter.addAll(value);
            }else{
                mHasNext = false; //测试
            }

        }
    }

    private void showNoDataPage() {
        showErrorView(null, IActionTitleBar.ErrorType.EMPTYVIEW, getString(R.string.label_nodata_wonderful_play_list), getString(R.string.label_nodata_wonderfulplay_list_message), "", null);
    }

    private void initEvent() {
        mPullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                doNetDatas(1);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (mHasNext) {
                    doNetDatas((mAdapter.getCount() / PAGESIZE) + 1);
                } else {
                    mHandler.sendEmptyMessageDelayed(ValueConstants.MSG_HAS_NO_DATA, 1000);
                }
            }
        });

        mPullToRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int headerCount = mListView.getHeaderViewsCount();
                if (position >= headerCount) {
                    Task task = mAdapter.getItem(position - headerCount);
                    NavUtils.gotoTaskDescriptionActivity(getActivity(), task.name, task.explanation);

                }
            }
        });

    }

    private void initPlist() {
        mPullToRefreshListView.setScrollingWhileRefreshingEnabled(!mPullToRefreshListView.isScrollingWhileRefreshingEnabled());
        mPullToRefreshListView.setMode(PullToRefreshBase.Mode.DISABLED);
        mListView = mPullToRefreshListView.getRefreshableView();
        View   signedView = LayoutInflater.from(getActivity()).inflate(R.layout.activity_signed_listview_footerview,
                null);
        tvSignedIntegralrule = (TextView) signedView.findViewById(R.id.tv_signed_integralrule);
        tvSignedIntegralrule.setOnClickListener(this);
        mListView.addFooterView(signedView);
        mListView.setHeaderDividersEnabled(false);
        mListView.setDividerHeight(0);

        mAdapter = signedViewHelper.setSignedDAdapter(getActivity());
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_signed_integralrule://积分规则
                if(task!=null){
                    WebParams wp = new WebParams();
                    wp.setUrl( task.pointsRulesUrl);
                    NavUtils.openBrowser(getActivity(), wp);
                }
                break;
        }
    }
}
