package com.newyhy.fragment.circle;

import android.content.Intent;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.newyhy.adapter.circle.FollowAdapter;
import com.newyhy.contract.CircleFollowContract;
import com.newyhy.contract.presenter.CircleFollowPresenter;
import com.newyhy.views.YhyRecyclerDivider;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.ShieldType;
import com.yhy.common.eventbus.event.EvBusBlack;
import com.yhy.common.eventbus.event.EvBusCircleChangeFollow;
import com.yhy.common.eventbus.event.EvBusCircleChangePraise;
import com.yhy.common.eventbus.event.EvBusCircleDelete;
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
import com.yhy.common.beans.net.model.discover.UgcInfoResultList;
import com.yhy.common.constants.IntentConstants;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.eventbus.event.EvBusCircleTabRefresh;
import com.yhy.common.eventbus.event.EvBusVideoCommentChange;
import com.yhy.common.eventbus.event.EvBusVideoPraiseChange;
import com.yhy.location.LocationManager;
import com.yhy.router.RouterPath;
import com.yhy.router.YhyRouter;
import com.yhy.service.IUserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import de.greenrobot.event.EventBus;

/**
 * 圈子关注
 * Created by Jiervs on 2018/6/15.
 */

@Route(path = RouterPath.FRAGMENT_CIRCLE_FOLLOW)
public class CircleFollowFragment extends BaseLazyLoadFragment implements OnRefreshListener, OnLoadMoreListener, CircleFollowContract.View {

    private SmartRefreshLayout refreshLayout;
    private RecyclerView rv_follow;
    private FollowAdapter adapter;
    private LinearLayout error_view;
    //date
    private ArrayList<UgcInfoResult> list = new ArrayList<>();
    private int pageIndex = 1;
    private boolean hasNext = true;
    //presenter
    private CircleFollowPresenter mPresenter;
    private boolean firstAutoRefresh = true;

    @Autowired
    IUserService userService;

    @Autowired(name = "extraMap")
    HashMap<String, String> extraMap;

    @Override
    protected int setLayoutId() {
        return R.layout.circle_follow_fragment;
    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        if (isVisible && adapter != null) {
            //更新界面数据，如果数据还在下载中，就显示加载框
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onFragmentFirstVisible() {
        if (viewsReady && userService.isLogin()) {
            refreshLayout.autoRefresh();
        }
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
        rv_follow = mRootView.findViewById(R.id.rv_follow);
        adapter = new FollowAdapter(getActivity(), list);
        adapter.extraMap = extraMap;

        rv_follow.setAdapter(adapter);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rv_follow.setLayoutManager(mLayoutManager);
        rv_follow.addItemDecoration(new YhyRecyclerDivider(getResources(), R.color.gray_E, R.dimen.yhy_size_6px, R.dimen.yhy_size_1px, LinearLayoutManager.VERTICAL));
        rv_follow.setItemAnimator(new DefaultItemAnimator());
        error_view = mRootView.findViewById(R.id.error_view);
        if (list.size()>0) {
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
        enableRefreshAndLoad(false);
    }

    @Override
    protected void initData() {
        super.initData();
        if (!userService.isLogin()) {
            enableRefreshAndLoad(false);
            showErrorView(R.mipmap.error_view,"未登录，请先登录", "登录",v -> {
                if(!userService.isLogin())
                    YhyRouter.getInstance().startLoginActivity(v.getContext(), null, Intent.FLAG_ACTIVITY_CLEAR_TOP, IntentConstants.LOGIN_RESULT);
            });
        } else{
            enableRefreshAndLoad(true);
        }
    }


    /****************************************************************        ViewListener          **************************************************************/

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        if (mPresenter != null && hasNext) {
            mPresenter.getNetData(pageIndex, 10);
        } else {
            if (pageIndex == 1) showErrorView(R.mipmap.default_page_lists,"暂无内容","",null);
        }
        refreshLayout.finishLoadMore(3000);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        if (userService.isLogin()) {
            pageIndex = 1;
            hasNext = true;
            if (mPresenter != null) {
                mPresenter.getNetData(pageIndex, 10);
            } else{
                showErrorView(R.mipmap.default_page_lists,"暂无内容","",null);
            }
        }
        refreshLayout.finishRefresh(3000);
    }

    /****************************************************************        Contract.View          **************************************************************/
    //以下方法中对View进行操作时最好进行 viewsReady 判断，防止网络回调数据时，Fragment的View已经被销毁而造成View空指针
    @Override
    public void setPresenter(CircleFollowPresenter presenter) {
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
            if (viewsReady) error_view.setVisibility(View.GONE);
        } else {
            if (pageIndex == 1) showErrorView(R.mipmap.default_page_lists,"暂无内容","",null);
        }
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
    }

    @Override
    public void showErrorView(int resId,String tips, String advice, View.OnClickListener listener) {
        if (viewsReady) {
            if (pageIndex == 1) {
                list.clear();
                adapter.notifyDataSetChanged();
                error_view.setVisibility(View.VISIBLE);
                ((ImageView) error_view.findViewById(R.id.iv_error)).setImageResource(resId);
                ((TextView) error_view.findViewById(R.id.tv_tips)).setText(tips);
                ((TextView) error_view.findViewById(R.id.tv_advice)).setText(advice);
                if ("登录".equals(((TextView) error_view.findViewById(R.id.tv_advice)).getText())) {
                    error_view.findViewById(R.id.tv_advice).setBackgroundResource(R.drawable.shape_button_red);
                } else {
                    error_view.findViewById(R.id.tv_advice).setBackgroundResource(R.drawable.transparent);
                }
            } else {
                ToastUtil.showToast(getContext(), tips);
            }
            error_view.setOnClickListener(listener);
            refreshLayout.finishRefresh();
            refreshLayout.finishLoadMore();
        }
    }

    public void enableRefreshAndLoad(boolean enable) {
        refreshLayout.setEnableRefresh(enable);
        refreshLayout.setEnableLoadMore(enable);
    }


    /****************************************************************        Handler          **************************************************************/
    @Override
    public void handleMessage(Message msg) {

    }

    /*****************************************************************        OnEvent         *************************************************************/
    //Login State
    public void onEvent(EvBusUserLoginState state) {
        if (state.getUserLoginState() == 0) {
            enableRefreshAndLoad(true);
            onRefresh(refreshLayout);
        } else {
            if (!userService.isLogin()) {
                pageIndex = 1;
                list.clear();
                adapter.notifyDataSetChanged();
                showErrorView(R.mipmap.error_view,"未登录，请先登录", "登录", v -> {
                    if(!userService.isLogin())
                    YhyRouter.getInstance().startLoginActivity(v.getContext(), null, Intent.FLAG_ACTIVITY_CLEAR_TOP, IntentConstants.LOGIN_RESULT);
                });
            }
            enableRefreshAndLoad(false);
        }
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
        ArrayList<UgcInfoResult> keepList = new ArrayList<>();
        if (state.type == 0) {
            for (UgcInfoResult result : list) {
                if (result.userInfo.userId != state.userId) {
                    keepList.add(result);
                }
            }
        }
        list.clear();
        list.addAll(keepList);
        adapter.notifyDataSetChanged();

    }

    //删除动态
    public void onEvent(EvBusCircleDelete state) {
        for (int i=0; i<list.size(); i++) {
            if (list.get(i).id == state.id) {
                list.remove(i);
                adapter.notifyItemRemoved(i);
                break;
            }
        }
    }

    public void onEvent(EvBusCircleTabRefresh refresh) {
        if (isFragmentVisible() && viewsReady) {
            if (list.size()>0)rv_follow.scrollToPosition(0);
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
            for (int i = 0; i < list.size();i++) {
                if (list.get(i).userInfo.id == black.id) {
                    list.remove(i);
                    adapter.notifyItemRemoved(i);
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
                    adapter.notifyItemChanged(i);
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
                    adapter.notifyItemChanged(i);
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
                    list.get(i).isSupport = change.isPraise ? "AVAILABLE":"DELETED";
                    int offset = change.isPraise? 1: -1;
                    list.get(i).supportNum =+ offset;
                    adapter.notifyItemChanged(i);
                    break;
                }
            }
        } else {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).id > 0 && list.get(i).id == change.ugcId) {
                    list.get(i).isSupport = change.isPraise ? "AVAILABLE":"DELETED";
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
        if (mPresenter != null) {
            mPresenter.release();
        }
        mPresenter = null;
    }
}
