package com.newyhy.fragment.circle;

import android.graphics.Rect;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.newyhy.adapter.circle.CoffeeVideoAdapter;
import com.newyhy.beans.CircleCoffeeVideoList;
import com.newyhy.contract.CircleCoffeeVideoContract;
import com.newyhy.contract.presenter.CircleCoffeeVideoPresenter;
import com.newyhy.views.YhyRecyclerDivider;
import com.quanyan.yhy.R;
import com.quanyan.yhy.eventbus.EvBusUserLoginState;
import com.quanyan.yhy.libanalysis.AnEvent;
import com.quanyan.yhy.libanalysis.Analysis;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yhy.common.base.BaseLazyLoadFragment;
import com.yhy.common.beans.net.model.discover.UgcInfoResult;
import com.yhy.common.eventbus.event.EvBusCircleTabRefresh;
import com.yhy.common.utils.AndroidUtils;
import com.yhy.location.LocationManager;
import com.yhy.router.RouterPath;
import com.yhy.router.YhyRouter;

import java.util.ArrayList;
import java.util.HashMap;

import de.greenrobot.event.EventBus;

/**
 * 圈子短视频
 * Created by Jiervs on 2018/6/15.
 */
@Route(path = RouterPath.FRAGMENT_CIRCLE_COFFEEVIDEO)
public class CircleCoffeeVideoFragment extends BaseLazyLoadFragment implements OnRefreshListener, OnLoadMoreListener, CircleCoffeeVideoContract.View {

    private SmartRefreshLayout refreshLayout;
    private RecyclerView rv_coffee_video;
    private LinearLayout error_view;
    private CoffeeVideoAdapter adapter;
    //data
    private ArrayList<UgcInfoResult> list = new ArrayList<>();
    private int pageIndex = 1;
    private boolean hasNext = true;
    //presenter
    private CircleCoffeeVideoPresenter mPresenter;
    private boolean firstAutoRefresh = true;

    @Autowired(name = "extraMap")
    HashMap<String, String> extraMap;

    @Override
    protected void initVars() {
        super.initVars();
        if (!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this);
        YhyRouter.getInstance().inject(this);
        hasNext = true;
        extraMap.put(Analysis.TAG, tag);

    }

    @Override
    protected void initView() {
        super.initView();
        refreshLayout = mRootView.findViewById(R.id.refreshLayout);
        rv_coffee_video = mRootView.findViewById(R.id.rv_coffee_video);
        error_view = mRootView.findViewById(R.id.error_view);
        adapter = new CoffeeVideoAdapter(getActivity(), this, list);
        adapter.extraMap = extraMap;
        rv_coffee_video.setAdapter(adapter);
        rv_coffee_video.setLayoutManager(new GridLayoutManager(getContext(), 2));
        int margin = getResources().getDimensionPixelSize(R.dimen.yhy_size_1px);
        rv_coffee_video.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int position = parent.getChildAdapterPosition(view);
                if (position % 2 == 0) {
                    outRect.right = margin;
                } else {
                    outRect.left = margin;
                }
                outRect.bottom = margin;
            }
        });
        if (list.size() > 0) {
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
        return R.layout.circle_coffee_video_fragment;
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
            mPresenter.getNetData(pageIndex, 10);
        } else {
            if (list.size() == 0) showErrorView(R.mipmap.default_page_video, "暂无内容", "");
        }
        refreshLayout.finishLoadMore(3000);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        pageIndex = 1;
        hasNext = true;
        if (mPresenter != null) {
            mPresenter.getNetData(pageIndex, 10);
        } else {
            ((ImageView) error_view.findViewById(R.id.iv_error)).setImageResource(R.mipmap.default_page_video);
            ((TextView) error_view.findViewById(R.id.tv_tips)).setText(getString(R.string.circle_no_data));
        }
        refreshLayout.finishRefresh(3000);
    }

    /****************************************************************        Contract.View          **************************************************************/
    //以下方法中对View进行操作时最好进行 viewsReady 判断，防止网络回调数据时，Fragment的View已经被销毁而造成View空指针
    @Override
    public void setPresenter(CircleCoffeeVideoPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showData(CircleCoffeeVideoList result) {

        hasNext = result.hasNext;
        if (result.ugcInfoResultList != null && result.ugcInfoResultList.size() > 0) {
            // 刷新埋点
            if (pageIndex == 1) {
                if (firstAutoRefresh) {
                    Analysis.pushEvent(mActivity, AnEvent.ListRefresh,
                            new Analysis.Builder().
                                    setLat(LocationManager.getInstance().getStorage().getLast_lat()).
                                    setLng(LocationManager.getInstance().getStorage().getLast_lng()).
                                    setMode(1).
                                    setTab(extraMap.get(Analysis.TAB)).
                                    setQuantity(result.ugcInfoResultList.size()));
                    firstAutoRefresh = false;
                } else {
                    Analysis.pushEvent(mActivity, AnEvent.ListRefresh,
                            new Analysis.Builder().
                                    setLat(LocationManager.getInstance().getStorage().getLast_lat()).
                                    setLng(LocationManager.getInstance().getStorage().getLast_lng()).
                                    setMode(2).
                                    setTab(extraMap.get(Analysis.TAB)).
                                    setQuantity(result.ugcInfoResultList.size()));
                }
            }

            if (pageIndex == 1) list.clear();
            pageIndex++;
            list.addAll(result.ugcInfoResultList);
            adapter.notifyDataSetChanged();
            if (viewsReady) error_view.setVisibility(View.GONE);


        } else {
            if (list.size() == 0) showErrorView(R.mipmap.default_page_video, "暂无内容", "");
        }
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
    }

    @Override
    public void showErrorView(int resId, String tips, String advice) {
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
            if (list.size() > 0) rv_coffee_video.scrollToPosition(0);
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
