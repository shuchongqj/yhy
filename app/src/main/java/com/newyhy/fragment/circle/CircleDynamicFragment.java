package com.newyhy.fragment.circle;

import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.newyhy.adapter.circle.DynamicAdapter;
import com.newyhy.contract.CircleDynamicContract;
import com.newyhy.contract.presenter.CircleDynamicPresenter;
import com.newyhy.views.YhyRecyclerDivider;
import com.newyhy.views.itemview.CircleTopicHeaderView;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.ShieldType;
import com.yhy.common.eventbus.event.EvBusBlack;
import com.yhy.common.eventbus.event.EvBusCircleChangeFollow;
import com.yhy.common.eventbus.event.EvBusCircleChangePraise;
import com.yhy.common.eventbus.event.EvBusCircleDelete;
import com.quanyan.yhy.eventbus.EvBusUserLoginState;
import com.quanyan.yhy.libanalysis.AnEvent;
import com.quanyan.yhy.libanalysis.Analysis;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yhy.common.base.BaseLazyLoadFragment;
import com.yhy.common.beans.net.model.discover.UgcInfoResult;
import com.yhy.common.beans.net.model.discover.UgcInfoResultList;
import com.yhy.common.beans.topic.Topic;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.eventbus.event.EvBusCircleTabRefresh;
import com.yhy.common.eventbus.event.EvBusVideoCommentChange;
import com.yhy.common.eventbus.event.EvBusVideoPraiseChange;
import com.yhy.location.LocationManager;
import com.yhy.router.RouterPath;
import com.yhy.router.YhyRouter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 圈子动态
 * Created by Jiervs on 2018/6/15.
 */

@Route(path = RouterPath.FRAGMENT_CIRCLE_DYNAMIC)
public class CircleDynamicFragment extends BaseLazyLoadFragment implements OnRefreshListener, OnLoadMoreListener, CircleDynamicContract.View {

    private SmartRefreshLayout refreshLayout;
    private RecyclerView rv_dynamic;
    private DynamicAdapter adapter;
    private LinearLayout error_view;
    private CircleTopicHeaderView topicView;
    //date
    private ArrayList<UgcInfoResult> list = new ArrayList<>();
    private ArrayList<Topic> topicList = new ArrayList<>();
    private int pageIndex = 1;
    private boolean hasNext = true;
    //present
    private CircleDynamicPresenter mPresenter;
    private boolean firstAutoRefresh = true;

    @Autowired(name = "extraMap")
    HashMap<String, String> extraMap;

    @Override
    protected int setLayoutId() {
        return R.layout.circle_dynamic_fragment;
    }

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
        rv_dynamic = mRootView.findViewById(R.id.rv_dynamic);
        adapter = new DynamicAdapter(getActivity(), list);
        adapter.extraMap = extraMap;
        rv_dynamic.setAdapter(adapter);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rv_dynamic.setLayoutManager(mLayoutManager);
        rv_dynamic.addItemDecoration(new YhyRecyclerDivider(getResources(), R.color.gray_E, R.dimen.yhy_size_6px, R.dimen.yhy_size_1px, LinearLayoutManager.VERTICAL));
        //topicView
        topicView = new CircleTopicHeaderView(getContext());
        topicView.setData(topicList);
        adapter.addHeaderView(topicView);
        //errorView
        error_view = mRootView.findViewById(R.id.error_view);
        if (list.size() > 0) {
            error_view.setVisibility(View.GONE);
        } else {
            ((ImageView) error_view.findViewById(R.id.iv_error)).setImageResource(R.mipmap.default_dynamic);
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
    protected void onFragmentVisibleChange(boolean isVisible) {
        if (isVisible && adapter != null) adapter.notifyDataSetChanged(); //刷新动态时间
    }

    @Override
    protected void onFragmentFirstVisible() {
        if (viewsReady) {
            refreshLayout.autoRefresh();
        }
    }


    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        if (mPresenter != null) {
            mPresenter.getNetData(pageIndex, 10);
        } else {
            if (list.size() == 0) showErrorView(R.mipmap.default_dynamic, "暂无内容", "");
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
            if (list.size() == 0) showErrorView(R.mipmap.default_dynamic, "暂无内容", "");
        }
    }

    /****************************************************************        Contract.View          **************************************************************/
    //以下方法中对View进行操作时最好进行 viewsReady 判断，防止网络回调数据时，Fragment的View已经被销毁而造成View空指针
    @Override
    public void setPresenter(CircleDynamicPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showData(UgcInfoResultList result) {
        hasNext = result.hasNext;
        if (result.ugcInfoList != null && result.ugcInfoList.size() > 0) {
            // 刷新埋点
            if (pageIndex == 1) {
                if (firstAutoRefresh) {
                    Analysis.pushEvent(mActivity, AnEvent.ListRefresh,
                            new Analysis.Builder().
                                    setLat(LocationManager.getInstance().getStorage().getLast_lat()).
                                    setLng(LocationManager.getInstance().getStorage().getLast_lng()).
                                    setMode(1).
                                    setTab(extraMap.get(Analysis.TAB)).
                                    setQuantity(result.ugcInfoList.size()));
                    firstAutoRefresh = false;
                } else {
                    Analysis.pushEvent(mActivity, AnEvent.ListRefresh,
                            new Analysis.Builder().
                                    setLat(LocationManager.getInstance().getStorage().getLast_lat()).
                                    setLng(LocationManager.getInstance().getStorage().getLast_lng()).
                                    setMode(2).
                                    setTab(extraMap.get(Analysis.TAB)).
                                    setQuantity(result.ugcInfoList.size()));
                }
            }

            if (pageIndex == 1) list.clear();
            pageIndex++;
            list.addAll(result.ugcInfoList);
            adapter.notifyDataSetChanged();
            if (viewsReady) {
                error_view.setVisibility(View.GONE);
            }
        } else {
            if (list.size() == 0) showErrorView(R.mipmap.default_dynamic, "暂无内容", "");
        }
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
    }

    @Override
    public void showTopic(List<Topic> result) {
        topicView.setData(result);
        topicList = (ArrayList<Topic>) result;
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

    //praise State
    public void onEvent(EvBusCircleChangePraise state) {
        for (UgcInfoResult result : list) {
            if (result.id == state.id) {
                result.isSupport = state.isSupport;
                if (ValueConstants.TYPE_AVAILABLE.equals(state.isSupport)) {//点赞数加1
                    result.supportNum += 1;
                }
                if (ValueConstants.TYPE_DELETED.equals(state.isSupport)) {//点赞数减1
                    result.supportNum -= 1;
                }
            }
        }
        adapter.notifyDataSetChanged();

    }

    //follow State
    public void onEvent(EvBusCircleChangeFollow state) {
        for (UgcInfoResult result : list) {
            if (result.userInfo.userId == state.userId) {
                result.type = state.type;
            }
        }
        adapter.notifyDataSetChanged();
    }

    //删除动态
    public void onEvent(EvBusCircleDelete state) {
        for (UgcInfoResult result : list) {
            if (result.id == state.id) {
                list.remove(result);
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void onEvent(EvBusCircleTabRefresh refresh) {
        if (isFragmentVisible() && viewsReady) {
            if (list.size() > 0) rv_dynamic.scrollToPosition(0);
            refreshLayout.autoRefresh();
        }
    }

    //屏蔽
    public void onEvent(EvBusBlack black) {
        if (black.type.equals(ShieldType.USER_SUBJECT)) {//屏蔽用户
            Iterator iterator = list.iterator();
            while (iterator.hasNext()) {
                UgcInfoResult result = (UgcInfoResult) iterator.next();
                if (result.userInfo.userId == black.id) {
                    iterator.remove();
                }
            }
            adapter.notifyDataSetChanged();
        }

        if (black.type.equals(ShieldType.SUBJECT)) {//屏蔽动态
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).id == black.id) {
                    list.remove(i);
                    if (i != 0) {
                        adapter.notifyItemRemoved(i);
                    } else {
                        adapter.notifyDataSetChanged();
                    }
                    break;
                }
            }
        }
    }

    //  Video Comment Change
    public void onEvent(EvBusVideoCommentChange change) {
        if (change.liveId > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).id > 0 && list.get(i).liveId == change.liveId) {
                    if (change.isAdd) {
                        list.get(i).commentNum ++;
                    } else {
                        // TODO: 2018/7/26
                    }
                    adapter.notifyItemChanged(i+1);
                    break;
                }
            }
        } else {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).id > 0 && list.get(i).id == change.ugcId) {
                    if (change.isAdd) {
                        list.get(i).commentNum ++;
                    } else {
                        // TODO: 2018/7/26
                    }
                    adapter.notifyItemChanged(i+1);
                    break;
                }
            }
        }
    }

    //  Video Praise Change
    public void onEvent(EvBusVideoPraiseChange change) {
        if (change.liveId > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).id > 0 && list.get(i).liveId == change.liveId) {
                    list.get(i).isSupport = change.isPraise ? "AVAILABLE" : "DELETED";
                    int offset = change.isPraise? 1: -1;
                    list.get(i).supportNum =+ offset;
                    adapter.notifyItemChanged(i);
                    break;
                }
            }
        } else {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).id > 0 && list.get(i).id == change.ugcId) {
                    list.get(i).isSupport = change.isPraise ? "AVAILABLE" : "DELETED";
                    int offset = change.isPraise? 1: -1;
                    list.get(i).supportNum =+ offset;
                    adapter.notifyItemChanged(i);
                    break;
                }
            }
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
