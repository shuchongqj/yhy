package com.newyhy.fragment.live;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.newyhy.activity.HorizontalReplayActivity;
import com.newyhy.activity.VerticalReplayActivity;
import com.newyhy.adapter.live.ZanListAdapter;
import com.quanyan.yhy.R;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.discovery.controller.DiscoverController;
import com.quanyan.yhy.view.NetWorkErrorView;
import com.videolibrary.chat.event.LiveChatMessageEvent;
import com.yhy.common.base.BaseNewFragment;
import com.yhy.common.base.NoLeakHandler;
import com.yhy.common.beans.net.model.comment.SupportUserInfoList;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class ZanListFragment extends BaseNewFragment implements NoLeakHandler.HandlerCallback{

    private NestedScrollView error_view_contain;
    private NetWorkErrorView error_view;
    private RecyclerView mListView;
    private ZanListAdapter mAdapter;

    protected DiscoverController mController;
    private long mOutId = -1;
    private int pageIndex;
    private String praiseType;


    @Override
    protected int setLayoutId() {
        return R.layout.zan_list_fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setUgcID(long mOutId, String praiseType){
        this.mOutId = mOutId;
        mHandler = new NoLeakHandler(this);
        mController = new DiscoverController(getActivity(), mHandler);
        this.praiseType = praiseType;
    }

    @Override
    protected void initView() {
        super.initView();
        error_view_contain = mRootView.findViewById(R.id.error_view_contain);//errorView
        error_view = mRootView.findViewById(R.id.error_view);//errorView

        mListView = mRootView.findViewById(R.id.rc_zan);
        mAdapter = new ZanListAdapter(mActivity, new ArrayList<>());
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pageIndex = 1;
        getData(pageIndex);
    }

    @Override
    protected void initData() {
        super.initData();
    }

    private void getData(int pageIndex) {
        if (-1 != mOutId) {
            mController.doGetLiveDetailAppraisePeople(getContext(), mOutId, praiseType, pageIndex, ValueConstants.PAGESIZE);
        } else {
            ToastUtil.showToast(getContext(), getString(R.string.error_params));
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
        }
    }

    private void handleResult(SupportUserInfoList result) {
        if (mAdapter == null || error_view_contain == null) return;
        if (result.supportUserInfoList != null && result.supportUserInfoList.size() > 0) {
            if (getActivity() instanceof HorizontalReplayActivity){
                ((HorizontalReplayActivity) getActivity()).setZanNum(result.count);
            }

            if (getActivity() instanceof VerticalReplayActivity){
                ((VerticalReplayActivity) getActivity()).setZanNum(result.count);
            }
            if (pageIndex == 1 && error_view_contain != null) {
                error_view_contain.setVisibility(View.GONE);
                mListView.setVisibility(View.VISIBLE);
                mAdapter.setNewData(result.supportUserInfoList);
            } else {
                mAdapter.addData(result.supportUserInfoList);
            }
            mAdapter.notifyDataSetChanged();
            mAdapter.loadMoreComplete();
            if (result.hasNext) {
                mAdapter.loadMoreComplete();
            } else {
                mAdapter.loadMoreEnd(true);
            }
        } else {
            if (pageIndex == 1) {
                showNoDataPage();
                if (getActivity() instanceof HorizontalReplayActivity){
                    ((HorizontalReplayActivity) getActivity()).setZanNum(0);
                }

                if (getActivity() instanceof VerticalReplayActivity){
                    ((VerticalReplayActivity) getActivity()).setZanNum(0);
                }
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
    }

    public void smoothScrollToTop() {
        mListView.smoothScrollToPosition(0);
    }

}
