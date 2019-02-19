package com.newyhy.fragment.circle;

import android.content.res.Configuration;
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
import com.newyhy.activity.VideoPlayer;
import com.newyhy.activity.VideoSupportActivity;
import com.newyhy.adapter.circle.StandardVideoAdapter;
import com.newyhy.beans.CircleLiveInfoResult;
import com.newyhy.beans.CircleLiveList;
import com.newyhy.contract.CircleStandardVideoContract;
import com.newyhy.contract.presenter.CircleStandardVideoPresenter;
import com.quanyan.yhy.R;
import com.yhy.common.eventbus.event.EvBusCircleChangeFollow;
import com.quanyan.yhy.eventbus.EvBusUserLoginState;
import com.quanyan.yhy.libanalysis.AnEvent;
import com.quanyan.yhy.libanalysis.Analysis;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yhy.common.base.BaseLazyLoadFragment;
import com.yhy.common.eventbus.event.EvBusCircleTabRefresh;
import com.yhy.common.eventbus.event.EvBusPosition;
import com.yhy.location.LocationManager;
import com.yhy.router.RouterPath;
import com.yhy.router.YhyRouter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 圈子视频
 * Created by Jiervs on 2018/6/15.
 */

@Route(path = RouterPath.FRAGMENT_CIRCLE_STANDARDVIDEO)
public class CircleStandardVideoFragment extends BaseLazyLoadFragment implements OnRefreshListener, OnLoadMoreListener, CircleStandardVideoContract.View {
    private SmartRefreshLayout refreshLayout;
    private RecyclerView rv_standard_video;
    private LinearLayout error_view;
    private StandardVideoAdapter adapter;
    //data
    private ArrayList<CircleLiveInfoResult> list = new ArrayList<>();
    private int pageIndex = 1;
    private boolean hasNext = true;
    //presenter
    private CircleStandardVideoPresenter mPresenter;
    private LinearLayoutManager mLayoutManager;
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
        rv_standard_video = mRootView.findViewById(R.id.rv_standard_video);
        error_view = mRootView.findViewById(R.id.error_view);
        adapter = new StandardVideoAdapter(getActivity(), this, VideoSupportActivity.getPlayer(), list);
        adapter.extraMap = extraMap;

        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rv_standard_video.setLayoutManager(mLayoutManager);
        rv_standard_video.setAdapter(adapter);
        rv_standard_video.setItemAnimator(new DefaultItemAnimator() {
            @Override
            public boolean canReuseUpdatedViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, @NonNull List<Object> payloads) {
                return true;
            }
        });
        VideoPlayer.getInstance().init(adapter);
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

        rv_standard_video.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {

            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {
                int position = rv_standard_video.getChildAdapterPosition(view);
                if (position >= 0 && VideoPlayer.getInstance().getPosition() != -1) {
                    if (VideoPlayer.getInstance().getPosition() == position) {

                        if (getActivity() != null && getActivity() instanceof VideoSupportActivity) {
                            if (((VideoSupportActivity) getActivity()).isFullScreen) {

                            } else {
                                VideoPlayer.getInstance().setPosition(-1);
                                VideoPlayer.getInstance().stop();
                                VideoPlayer.getInstance().deattach(null);
                            }
                        } else {
                            VideoPlayer.getInstance().setPosition(-1);
                            VideoPlayer.getInstance().stop();
                            VideoPlayer.getInstance().deattach(null);
                        }
                    }
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyItemChanged(position);
                    }
                });
            }
        });
    }

    @Override
    protected int setLayoutId() {
        return R.layout.circle_standard_video_fragment;
    }

    /***********************************************************        LogicMethod           *************************************************************/
    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        if (!isVisible) {
            stopPlayer();
        }
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
            if (list.size() == 0) showErrorView(R.mipmap.default_page_video,"暂无内容","");
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
            if (list.size() == 0) showErrorView(R.mipmap.default_page_video,"暂无内容","");
        }
        refreshLayout.finishRefresh(3000);
    }

    /****************************************************************        Contract.View          **************************************************************/
    //以下方法中对View进行操作时最好进行 viewsReady 判断，防止网络回调数据时，Fragment的View已经被销毁而造成View空指针
    @Override
    public void setPresenter(CircleStandardVideoPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showData(CircleLiveList result) {

        hasNext = result.hasNext;
        if (result.list != null && result.list.size() > 0) {
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

            if (pageIndex == 1) {
                list.clear();
                VideoPlayer.getInstance().setPosition(-1);
                VideoPlayer.getInstance().stop();
                VideoPlayer.getInstance().deattach(null);
            }
            pageIndex++;
            list.addAll(result.list);
            adapter.notifyDataSetChanged();
            if (viewsReady) {
                error_view.setVisibility(View.GONE);
                refreshLayout.finishRefresh();
                refreshLayout.finishLoadMore();
            }
        } else {
            if (list.size() == 0) showErrorView(R.mipmap.default_page_video,"暂无内容","");
        }
    }

    @Override
    public void showErrorView(int resId , String tips, String advice) {
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

    //follow State
    public void onEvent(EvBusCircleChangeFollow state) {
        for (CircleLiveInfoResult result : list) {
            if (result.userInfo.userId == state.userId) {
                result.type = state.type;
            }
        }
        adapter.notifyDataSetChanged();

    }

    public void onEvent(EvBusPosition ev) {
        int position = ev.getPosition();
        if (position >= 0 && position <= list.size()) {
            adapter.notifyItemChanged(position, "");
        }
    }

    public void onEvent(EvBusCircleTabRefresh refresh) {
        if (isFragmentVisible() && viewsReady) {
            if (list.size()>0)rv_standard_video.scrollToPosition(0);
            refreshLayout.autoRefresh();
        }
    }

    /***********************************************************        LifeCycle           *************************************************************/
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.release();
            mPresenter = null;
        }
        VideoPlayer.getInstance().release();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        stopPlayer();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public void stopPlayer() {
        int lastPosition = VideoPlayer.getInstance().getPosition();
        if (lastPosition != -1) {
            VideoPlayer.getInstance().setPosition(-1);
            VideoPlayer.getInstance().stop();
            VideoPlayer.getInstance().deattach(null);
            adapter.notifyItemChanged(lastPosition);
        }
    }

    @Override
    protected boolean onBackPressed() {
        if (getActivity() instanceof VideoSupportActivity) {
            VideoSupportActivity activity = (VideoSupportActivity) getActivity();
            if (activity != null && activity.isFullScreen) {
                activity.exitFullScreen();
                return true;
            }
        }
        return super.onBackPressed();
    }
}
