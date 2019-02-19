package com.newyhy.fragment.circle;

import android.graphics.Rect;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.newyhy.adapter.circle.LiveAdapter;
import com.newyhy.beans.CircleLiveInfoResult;
import com.newyhy.beans.CircleLiveList;
import com.newyhy.contract.CircleLiveContract;
import com.newyhy.contract.presenter.CircleLivePresenter;
import com.quanyan.yhy.R;
import com.quanyan.yhy.eventbus.EvBusUserLoginState;
import com.quanyan.yhy.libanalysis.AnEvent;
import com.quanyan.yhy.libanalysis.Analysis;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yhy.common.base.BaseLazyLoadFragment;
import com.yhy.common.eventbus.event.EvBusCircleTabRefresh;
import com.yhy.common.utils.AndroidUtils;
import com.yhy.location.LocationManager;
import com.yhy.router.RouterPath;
import com.yhy.router.YhyRouter;

import java.util.ArrayList;
import java.util.HashMap;

import de.greenrobot.event.EventBus;

/**
 * 圈子直播
 * Created by Jiervs on 2018/6/15.
 */

@Route(path = RouterPath.FRAGMENT_CIRCLE_LIVE)
public class CircleLiveFragment extends BaseLazyLoadFragment implements OnRefreshListener, OnLoadMoreListener, CircleLiveContract.View {

    private SmartRefreshLayout refreshLayout;
    private RecyclerView rv_live;
    private LinearLayout error_view;
    private LiveAdapter adapter;
    //data
    private ArrayList<CircleLiveInfoResult> list = new ArrayList<>();
    private int pageIndex = 1;
    private boolean hasNext = true;
    //presenter
    private CircleLivePresenter mPresenter;
    private boolean firstAutoRefresh = true;

    @Autowired(name = "extraMap")
    HashMap<String, String> extraMap;

    @Override
    protected void initVars() {
        super.initVars();
        if (!EventBus.getDefault().isRegistered(this))  EventBus.getDefault().register(this);
        YhyRouter.getInstance().inject(this);
        hasNext = true;
        extraMap.put(Analysis.TAG, tag);

    }

    @Override
    protected void initView() {
        super.initView();
        refreshLayout = mRootView.findViewById(R.id.refreshLayout);
        rv_live = mRootView.findViewById(R.id.rv_live);
        error_view = mRootView.findViewById(R.id.error_view);
        adapter = new LiveAdapter(getActivity(),this,list);
        adapter.extraMap = extraMap;

        rv_live.setAdapter(adapter);
        rv_live.setLayoutManager(new GridLayoutManager(getContext(), 2));
        int marginLeft = AndroidUtils.dip2px(getContext(),15);//getResources().getDimensionPixelSize(R.dimen.yhy_size_16px);
        int marginTop = AndroidUtils.dip2px(getContext(),13);//getResources().getDimensionPixelSize(R.dimen.yhy_size_16px);
        int halfMarginLeft = marginLeft / 4;
        rv_live.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int position = parent.getChildAdapterPosition(view);
                if (position % 2 == 0){
                    outRect.left = marginLeft;
                    outRect.right = halfMarginLeft;
                }else {
                    outRect.left = halfMarginLeft;
                    outRect.right = marginLeft;
                }
                outRect.top = marginTop;
            }
        });
        if (list.size()>0) {
            error_view.setVisibility(View.GONE);
        } else {
            ((ImageView) error_view.findViewById(R.id.iv_error)).setImageResource(R.mipmap.default_page_video);
            ((TextView) error_view.findViewById(R.id.tv_tips)).setText(getString(R.string.circle_no_data));
        }
    }

    @Override
    protected void setListener() {
        super.setListener();
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.circle_live_fragment;
    }

    /***********************************************************        LogicMethod           *************************************************************/
    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {

    }

    @Override
    protected void onFragmentFirstVisible() {
        if (viewsReady) {
            refreshLayout.autoRefresh();
        }
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        if (mPresenter != null && hasNext) {
            mPresenter.getNetData(pageIndex,10);
        }else {
            if (pageIndex == 1) showErrorView(R.mipmap.default_page_video,"暂无内容","");
        }
        refreshLayout.finishLoadMore(3000);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        pageIndex = 1;
        hasNext = true;
        if (mPresenter != null) {
            mPresenter.getNetData(pageIndex,10);
        }else {
            showErrorView(R.mipmap.default_page_video,"暂无内容","");
        }
        refreshLayout.finishRefresh(3000);
    }

    /****************************************************************        Contract.View          **************************************************************/
    //以下方法中对View进行操作时最好进行 viewsReady 判断，防止网络回调数据时，Fragment的View已经被销毁而造成View空指针
    @Override
    public void setPresenter(CircleLivePresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showData(CircleLiveList result) {
        hasNext = result.hasNext;
        if (result.list != null && result.list.size() >0) {
            // 刷新埋点
            if (pageIndex == 1) {
                if (firstAutoRefresh) {
                    Analysis.pushEvent(mActivity, AnEvent.ListRefresh,
                            new Analysis.Builder().
                                    setLat(LocationManager.getInstance().getStorage().getLast_lat()).
                                    setLng(LocationManager.getInstance().getStorage().getLast_lng()).
                                    setMode(1).
                                    setTab(extraMap.get(Analysis.TAB)).
                                    setQuantity(result.list.size()));
                    firstAutoRefresh = false;
                } else {
                    Analysis.pushEvent(mActivity, AnEvent.ListRefresh,
                            new Analysis.Builder().
                                    setLat(LocationManager.getInstance().getStorage().getLast_lat()).
                                    setLng(LocationManager.getInstance().getStorage().getLast_lng()).
                                    setMode(2).
                                    setTab(extraMap.get(Analysis.TAB)).
                                    setQuantity(result.list.size()));
                }
            }

            if (pageIndex == 1) list.clear();
            pageIndex ++;
            list.addAll(result.list);
            adapter.notifyDataSetChanged();
            if (viewsReady) error_view.setVisibility(View.GONE);
        } else {
            if (pageIndex == 1) showErrorView(R.mipmap.default_page_video,"暂无内容","");
        }
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
    }

    @Override
    public void showErrorView(int resId,String tips, String advice) {
        if (viewsReady) {
            if (pageIndex == 1) {
                list.clear();
                adapter.notifyDataSetChanged();
                error_view.setVisibility(View.VISIBLE);
                ((ImageView) error_view.findViewById(R.id.iv_error)).setImageResource(resId);
                ((TextView) error_view.findViewById(R.id.tv_tips)).setText(tips);
                ((TextView) error_view.findViewById(R.id.tv_advice)).setText(advice);
            }
            refreshLayout.finishRefresh();
            refreshLayout.finishLoadMore();
        }
    }

    /***********************************************************        Handler Message           *************************************************************/
    @Override
    public void handleMessage(Message msg) {

    }

    /*****************************************************************        OnEvent         *************************************************************/
    //Login State
    public void onEvent(EvBusUserLoginState state) {

    }

    public void onEvent(EvBusCircleTabRefresh refresh) {
        if (isFragmentVisible() && viewsReady) {
            if (list.size()>0)rv_live.scrollToPosition(0);
            refreshLayout.autoRefresh();
        }
    }

    /***********************************************************        LifeCycle           *************************************************************/
    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.release();
        mPresenter = null;
    }
}
