package com.quanyan.yhy.ui.signed.activity;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

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
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.integralmall.integralmallcontroller.IntegralmallController;
import com.quanyan.yhy.ui.signed.signedviewhelper.signedViewHelper;
import com.yhy.common.beans.net.model.point.PointDetailQueryResult;
import com.yhy.common.beans.net.model.point.PointsDetail;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.AndroidUtils;

import java.util.List;

/**
 * Created with Android Studio.
 * Title:EatGreatActivity
 * Description:积分明细
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:wjm
 * Date:2016-6-22
 * Time:12:05
 * Version 1.3.0
 */
public class SignedDetailsActivity extends BaseFragment {
    private PullToRefreshListView mPullToRefreshListView;
    private RelativeLayout mParentLayout;
    private ListView mListView;
    private QuickAdapter<PointsDetail> mAdapter;
    protected int mPageIndex = 1;
    protected boolean isRefresh = true;
    private static int PAGESIZE = 15;
    private boolean mHasNext = true;
    IntegralmallController integralmallController;

//    private BaseNavView mBaseNavView;
//    @Override
//    public View onLoadNavView() {
//        mBaseNavView = new BaseNavView(getActivity());
//        mBaseNavView.setTitleText(getString(R.string.label_signeddetails_title));
//        return mBaseNavView;
//    }

//    @Override
//    public boolean isTopCover() {
//        return false;
//    }

    @Override
    public View onLoadContentView() {
        return View.inflate(getActivity(), R.layout.base_pull_refresh_layout_listview, null);
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mPullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.pull_to_refresh_listview);
        mParentLayout = (RelativeLayout) view.findViewById(R.id.base_pullrefresh_listview_parent_layout);
        integralmallController = new IntegralmallController(getActivity(), mHandler);
        initPlist();
        initEvent();
        //访问网络
        showLoadingView(getString(R.string.loading_text));
    }
    int pageIndex=1;
    private void doNetDatas(boolean isRefreshs) {
        if (isRefreshs) {
            isRefresh=true;
            pageIndex=1;
        } else {
            isRefresh=false;
            pageIndex++;
        }
//        System.out.println("wjm====pageIndex"+pageIndex);
        integralmallController.doPointDetailQuery(getActivity(), pageIndex, PAGESIZE);
    }
    @Override
    public void onResume() {
        super.onResume();
        doNetDatas(true);

    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        hideLoadingView();
        hideErrorView(null);
        //刷新完成
        mPullToRefreshListView.onRefreshComplete();
        switch (msg.what) {
            case ValueConstants.MSG_INTEGRALMALL_POINTDETAIL_OK:
                PointDetailQueryResult mPointDetailQueryResult = (PointDetailQueryResult) msg.obj;
                handlePageData(mPointDetailQueryResult.list);
                break;
            case ValueConstants.MSG_INTEGRALMALL_POINTDETAIL_KO:
                if (isRefresh) {
                    if (mAdapter.getCount() == 0) {
                        mAdapter.clear();
                        showErrorView(null, ErrorCode.NETWORK_UNAVAILABLE == msg.arg1 ?
                                IActionTitleBar.ErrorType.NETUNAVAILABLE : IActionTitleBar.ErrorType.ERRORNET, "", "", "", new ErrorViewClick() {
                            @Override
                            public void onClick(View view) {
                                doNetDatas(true);
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
                ToastUtil.showToast(getActivity(), R.string.scenic_hasnodata);
                break;

        }

    }

    private void handlePageData(List<PointsDetail> value) {

        if (isRefresh) {
            if (value != null && value.size() > 0) {
                mAdapter.replaceAll(value);
//                pageIndex++;
            } else {
                mHasNext = false; //测试
                mAdapter.clear();
                showNoDataPage();
            }
        } else {
            if (value != null) {
//                pageIndex++;
                mAdapter.addAll(value);
            }else{
//                mHandler.sendEmptyMessageDelayed(ValueConstants.MSG_HAS_NO_DATA, 1000);
                mHasNext = false; //测试
            }
        }

    }

    private void showNoDataPage() {
        showErrorView(mParentLayout, IActionTitleBar.ErrorType.EMPTYVIEW, getString(R.string.label_nodata_wonderful_play_list), getString(R.string.label_nodata_wonderfulplay_list_message), "", null);
    }

    private void initEvent() {
        mPullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                doNetDatas(true);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (mHasNext) {
//                    int i=(mAdapter.getCount() / PAGESIZE) + 1;

                    doNetDatas(false);
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
//                    MerchantInfo merchantInfo = mAdapter.getItem(position - headerCount);
                }
            }
        });

    }

    private void initPlist() {
        mPullToRefreshListView.setScrollingWhileRefreshingEnabled(!mPullToRefreshListView.isScrollingWhileRefreshingEnabled());
        mPullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        mListView = mPullToRefreshListView.getRefreshableView();
        mListView.setHeaderDividersEnabled(false);
        mListView.setDividerHeight(0);
        mAdapter = signedViewHelper.setSignedDetailsAdapter(getActivity());
        mListView.setAdapter(mAdapter);
    }
}
