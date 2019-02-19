package com.newyhy.fragment.circle;

import android.os.Bundle;
import android.os.Message;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.newyhy.adapter.circle.CircleDetailPraiseAdapter;
import com.quanyan.yhy.R;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.discovery.controller.DiscoverController;
import com.quanyan.yhy.view.NetWorkErrorView;
import com.yhy.common.base.BaseNewFragment;
import com.yhy.common.beans.net.model.comment.SupportUserInfoList;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.SPUtils;
import com.yhy.router.YhyRouter;

import java.util.ArrayList;

/**
 * Created by yangboxue on 2018/6/27.
 */

public class CircleDetailPrasieFragment extends BaseNewFragment {

    private NestedScrollView error_view_contain;
//    private NetWorkErrorView error_view;
    private RecyclerView mListView;
    private CircleDetailPraiseAdapter mAdapter;

    protected DiscoverController mController;
    private long mOutId = -1;
    private int pageIndex;

    public static CircleDetailPrasieFragment getInstance(long subjectId) {
        CircleDetailPrasieFragment fragment = new CircleDetailPrasieFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(SPUtils.EXTRA_ID, subjectId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_circle_detail_praise;
    }

    @Override
    protected void initVars() {
        super.initVars();
        YhyRouter.getInstance().inject(this);
        mController = new DiscoverController(getActivity(), mHandler);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mOutId = bundle.getLong(SPUtils.EXTRA_ID, -1);
        }
    }

    @Override
    protected void initView() {
        super.initView();
        error_view_contain = mRootView.findViewById(R.id.error_view_contain);//errorView
//        error_view = mRootView.findViewById(R.id.error_view);//errorView

        mListView = mRootView.findViewById(R.id.rv_praise);
        mAdapter = new CircleDetailPraiseAdapter(mActivity, new ArrayList<>());
        mListView.setLayoutManager(new LinearLayoutManager(mActivity));
        mListView.setAdapter(mAdapter);
    }

    @Override
    protected void setListener() {
        super.setListener();
        mAdapter.setOnItemClickListener((baseQuickAdapter, view, i) -> NavUtils.gotoMasterHomepage(getActivity(), mAdapter.getItem(i).userId));
        mAdapter.setOnLoadMoreListener(() -> {
            pageIndex++;
            getData(pageIndex);
        }, mListView);
    }

    @Override
    protected void initData() {
        super.initData();
        pageIndex = 1;
        getData(pageIndex);
    }

    private void getData(int pageIndex) {
        if (-1 != mOutId) {
            mController.doGetLiveDetailAppraisePeople(getActivity(), mOutId, ValueConstants.TYPE_PRAISE_LIVESUP, pageIndex, ValueConstants.PAGESIZE);
        } else {
            ToastUtil.showToast(getActivity(), getString(R.string.error_params));
        }
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case DiscoverController.MSG_LIVE_DETAIL_COMMENT_OK: {
                SupportUserInfoList supportUserInfoList = (SupportUserInfoList) msg.obj;
                handleResult(supportUserInfoList);
                break;
            }
            case DiscoverController.MSG_LIVE_DETAIL_COMMENT_ERROR: {
//                if (isRefresh) {
//                    showErrorView(null, ErrorCode.NETWORK_UNAVAILABLE == msg.arg1 ?
//                            IActionTitleBar.ErrorType.NETUNAVAILABLE : IActionTitleBar.ErrorType.ERRORNET, "", "", "", new ErrorViewClick() {
//                        @Override
//                        public void onClick(View view) {
//                            updateData(false);
//                        }
//                    });
//                }
                break;
            }
        }
    }

    private void handleResult(SupportUserInfoList result) {
        if (result.supportUserInfoList != null && result.supportUserInfoList.size() > 0) {
            if (pageIndex == 1) {
                error_view_contain.setVisibility(View.GONE);
                mListView.setVisibility(View.VISIBLE);
                mAdapter.setNewData(result.supportUserInfoList);
            } else {
                mAdapter.addData(result.supportUserInfoList);
            }
            mAdapter.notifyDataSetChanged();
            mAdapter.loadMoreComplete();
            if (result.hasNext){
                mAdapter.loadMoreComplete();
            }else {
                mAdapter.loadMoreEnd(true);
            }
        } else {
            if (pageIndex == 1) {
                showNoDataPage();
            } else {
                ToastUtil.showToast(getActivity(), getString(R.string.scenic_hasnodata));
                mAdapter.loadMoreEnd(true);

            }
        }
    }

    /**
     * 刷新数据
     */
    public void updateData() {
        pageIndex = 1;
        getData(pageIndex);
    }

    private void showNoDataPage() {
        if (getActivity() == null) {
            return;
        }

        error_view_contain.setVisibility(View.VISIBLE);
        mListView.setVisibility(View.GONE);

//        error_view.show(getString(R.string.label_nodata_priase), "  ", "", null);
    }


    public void smoothScrollToTop() {
        mListView.smoothScrollToPosition(0);
    }
}
