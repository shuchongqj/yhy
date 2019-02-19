package com.newyhy.fragment.circle;

import android.os.Bundle;
import android.os.Message;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.newyhy.adapter.TaEvaluateAdapter;
import com.newyhy.adapter.circle.ArticleAdapter;
import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.MerchantType;
import com.quanyan.yhy.common.QueryType;
import com.quanyan.yhy.net.NetManager;
import com.quanyan.yhy.net.lsn.OnResponseListener;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.view.NetWorkErrorView;
import com.yhy.common.base.BaseNewFragment;
import com.yhy.common.beans.net.model.master.QueryTerm;
import com.yhy.common.beans.net.model.master.QueryTermsDTO;
import com.yhy.common.beans.net.model.trip.ShortItem;
import com.yhy.common.beans.net.model.trip.ShortItemsResult;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.SPUtils;
import com.yhy.service.IUserService;

import java.util.ArrayList;
import java.util.List;

/**
 * 文章列表fragment
 * <p>
 * Created by yangboxue on 2018/6/25.
 */

public class CircleArticleFragment extends BaseNewFragment {

    private NestedScrollView error_view_contain;
    private NetWorkErrorView error_view;
    private RecyclerView mListView;
    private ArticleAdapter mAdapter;

    private long mTalentId;
    private long mOrgId;

    private int pageIndex;

    @Autowired
    IUserService userService;

    @Override
    protected int setLayoutId() {
        return R.layout.circle_article_fragment;
    }

    @Override
    protected void initVars() {
        super.initVars();
        mTalentId = getArguments().getLong(SPUtils.EXTRA_ID);
        mOrgId = getArguments().getLong("mOrgId");

    }

    @Override
    protected void initView() {
        super.initView();
//        refreshLayout = mRootView.findViewById(R.id.refreshLayout);
//        refreshLayout.setEnableRefresh(false);

        error_view_contain = mRootView.findViewById(R.id.error_view_contain);//errorView
        error_view = mRootView.findViewById(R.id.error_view);//errorView

        mListView = mRootView.findViewById(R.id.rv_article);
        mAdapter = new ArticleAdapter(mActivity, new ArrayList<>());
        mListView.setLayoutManager(new LinearLayoutManager(mActivity));
        mListView.setAdapter(mAdapter);

    }

    @Override
    protected void setListener() {
        super.setListener();

//        refreshLayout.setOnLoadMoreListener(this);

        mAdapter.setOnItemClickListener((baseQuickAdapter, view, i) -> {
//            ShortItem data = mAdapter.getItem(i);
//            if (!StringUtil.isEmpty(data.itemType)) {
//                NavUtils.gotoProductDetail(getActivity(), data.itemType, data.id, data.title);
//            } else {
//                ToastUtil.showToast(getActivity(), R.string.label_toast_no_config_item_type);
//            }
        });

        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                pageIndex++;
                getData(pageIndex);
            }
        }, mListView);
    }

    @Override
    protected void initData() {
        super.initData();
        pageIndex = 1;
        getData(pageIndex);
    }

    private void getData(int pageIndex) {
        if (mOrgId <= 0) {
            showNoDataPage();
            return;
        }

        QueryTermsDTO params = new QueryTermsDTO();
        params.pageSize = ValueConstants.PAGESIZE;
        params.pageNo = pageIndex;

        String latitude = SPUtils.getExtraCurrentLat(getActivity());
        String longtitude = SPUtils.getExtraCurrentLon(getActivity());
        double lat = TextUtils.isEmpty(latitude) ? 0 : Double.parseDouble(latitude);
        double lon = TextUtils.isEmpty(longtitude) ? 0 : Double.parseDouble(longtitude);
        if (lat > 0) {
            params.latitude = lat;
        }
        if (lon > 0) {
            params.longitude = lon;
        }

        List<QueryTerm> ts = new ArrayList<>();
        QueryTerm t1 = new QueryTerm();
        t1.type = QueryType.SELLER_ID;
//        t1.value = String.valueOf(mTalentId);
        t1.value = String.valueOf(mOrgId);
        ts.add(t1);

        QueryTerm t2 = new QueryTerm();
        t2.type = QueryType.SELLER_TYPE;
        t2.value = MerchantType.TALENT;
        ts.add(t2);

        params.queryTerms = ts;

        NetManager.getInstance(mActivity).doGetItemList(params, new OnResponseListener<ShortItemsResult>() {
            @Override
            public void onComplete(boolean isOK, ShortItemsResult result, int errorCode, String errorMsg) {
                if (isOK) {
                    handleResult(result);
                } else {
                    ToastUtil.showToast(getActivity(), StringUtil.handlerErrorCode(getActivity(), errorCode));
                }
//                refreshLayout.finishLoadMore();
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
//                refreshLayout.finishLoadMore();
                ToastUtil.showToast(getActivity(), StringUtil.handlerErrorCode(getActivity(), errorCode));
            }
        });
    }

    private void handleResult(ShortItemsResult result) {
        if (result.shortItemList != null && result.shortItemList.size() > 0) {
            if (pageIndex == 1) {
                error_view_contain.setVisibility(View.GONE);
                mListView.setVisibility(View.VISIBLE);
//                mAdapter.setNewData(result.shortItemList);
            } else {
//                mAdapter.addData(result.shortItemList);
            }
            mAdapter.notifyDataSetChanged();
            mAdapter.loadMoreComplete();

//            UgcInfoResultList resultList = (UgcInfoResultList) msg.obj;
//            if (resultList.ugcInfoList == null || resultList.ugcInfoList.size() == 0) return;
//            mList.addAll(resultList.ugcInfoList);
//            pageIndex += 1;
//            adapter.notifyDataSetChanged();
        } else {
            if (pageIndex == 1){
                showNoDataPage();
            }else {
                ToastUtil.showToast(getActivity(), getString(R.string.scenic_hasnodata));
                mAdapter.loadMoreEnd(true);

            }
        }
    }

    private void showNoDataPage() {
        if (getActivity() == null) {
            return;
        }

        error_view_contain.setVisibility(View.VISIBLE);
        mListView.setVisibility(View.GONE);

        if (mTalentId > 0 && userService.getLoginUserId() == mTalentId) {
            error_view.show(getString(R.string.label_hint_evaluate_nothing), "  ", "", null);
//            showErrorView(null, IActionTitleBar.ErrorType.EMPTYVIEW, getString(R.string.label_hint_evaluate_nothing), "  ", "", null);
        } else {
            error_view.show(getString(R.string.label_hint_evaluate_title_nothing), "  ", "", null);
//            showErrorView(null, IActionTitleBar.ErrorType.EMPTYVIEW, getString(R.string.label_hint_evaluate_title_nothing), "  ", "", null);
        }
    }

    /**
     * 刷新数据
     */
    public void updateData() {
        pageIndex = 1;
        getData(pageIndex);
    }

    @Override
    public void handleMessage(Message msg) {

    }
}
