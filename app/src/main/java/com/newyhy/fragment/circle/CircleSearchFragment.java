package com.newyhy.fragment.circle;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.newyhy.adapter.circle.RecommendAdapter;
import com.newyhy.contract.CircleSearchContract;
import com.newyhy.contract.presenter.CircleSearchPresenter;
import com.newyhy.views.YhyRecyclerDivider;
import com.quanyan.yhy.R;
import com.yhy.common.eventbus.event.EvBusNewsCommentChange;
import com.quanyan.yhy.eventbus.EvBusUserLoginState;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnMultiPurposeListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yhy.common.base.BaseLazyLoadFragment;
import com.yhy.common.eventbus.event.EvBusDisLike;
import com.yhy.common.eventbus.event.EvBusVideoCommentChange;
import com.yhy.network.resp.snscenter.GetRecommendPageListResp;
import com.yhy.network.resp.snscenter.SearchPageListResp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import de.greenrobot.event.EventBus;

/**
 * 圈子推荐
 * Created by Jiervs on 2018/6/15.
 */

public class CircleSearchFragment extends BaseLazyLoadFragment implements OnRefreshListener, OnLoadMoreListener,CircleSearchContract.View {

    private SmartRefreshLayout refreshLayout;
    private RecyclerView rv_search;
    private RecommendAdapter adapter;
    private LinearLayout error_view;
    private RelativeLayout rl_parent;
    private TextView tab_anchor;
    private TextView tab_anchor_shadow;
    //date
    private List<GetRecommendPageListResp.RecommendResult> list = new ArrayList<>();
    private int pageIndex = 1;
    private boolean hasNext = true;
    private boolean showTips;
    private String title;
    //presenter
    private CircleSearchPresenter mPresenter;
    //var
    private Runnable hideTipsRunnable = () -> {
        setVisibilityWithAnimation(View.GONE,tab_anchor);
        setVisibilityWithAnimation(View.GONE,tab_anchor_shadow);
    };

    private LinearLayoutManager mLayoutManager;
    private ValueAnimator valueAnimator;

    @Override
    protected int setLayoutId() {
        return R.layout.circle_search_fragment;
    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        if (isVisible && adapter != null) {
            adapter.notifyDataSetChanged(); //刷新新闻时间
        } else {
            if (valueAnimator != null ) {
                valueAnimator.cancel();
            }
            if (tab_anchor_shadow != null) {
                tab_anchor_shadow.setVisibility(View.GONE);
            }
            if (tab_anchor != null) {
                tab_anchor_shadow.getLayoutParams().height = 0;
                tab_anchor_shadow.requestLayout();
            }
        }
    }

    @Override
    protected void onFragmentFirstVisible() {
    }

    @Override
    protected void initVars() {
        super.initVars();
        if (!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this);
        hasNext = true;
    }

    @Override
    protected void initView() {
        super.initView();
        refreshLayout = mRootView.findViewById(R.id.refreshLayout);
        rv_search = mRootView.findViewById(R.id.rv_search);
        adapter = new RecommendAdapter(getActivity(), list);
        rv_search.setAdapter(adapter);
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rv_search.setLayoutManager(mLayoutManager);
        rv_search.addItemDecoration(new YhyRecyclerDivider(getResources(), R.color.gray_E, R.dimen.yhy_size_1px, R.dimen.yhy_size_15px, LinearLayoutManager.VERTICAL));
        rv_search.setItemAnimator(new DefaultItemAnimator());
        error_view = mRootView.findViewById(R.id.error_view);
        tab_anchor = mRootView.findViewById(R.id.tab_anchor);
        tab_anchor_shadow = mRootView.findViewById(R.id.tab_anchor_shadow);
        rl_parent = mRootView.findViewById(R.id.rl_parent);
        if (list.size() > 0) error_view.setVisibility(View.GONE);
        refreshLayout.setReboundDuration(0);
        ((ImageView) error_view.findViewById(R.id.iv_error)).setImageResource(R.mipmap.default_page_search);
        ((TextView) error_view.findViewById(R.id.tv_tips)).setText(getString(R.string.circle_no_data));
    }

    @Override
    protected void setListener() {
        super.setListener();
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);
        refreshLayout.setOnMultiPurposeListener(new OnMultiPurposeListener() {
            @Override
            public void onHeaderMoving(RefreshHeader header, boolean isDragging, float percent, int offset, int headerHeight, int maxDragHeight) {

            }

            @Override
            public void onHeaderReleased(RefreshHeader header, int headerHeight, int maxDragHeight) {

            }

            @Override
            public void onHeaderStartAnimator(RefreshHeader header, int headerHeight, int maxDragHeight) {

            }

            @Override
            public void onHeaderFinish(RefreshHeader header, boolean success) {

            }

            @Override
            public void onFooterMoving(RefreshFooter footer, boolean isDragging, float percent, int offset, int footerHeight, int maxDragHeight) {

            }

            @Override
            public void onFooterReleased(RefreshFooter footer, int footerHeight, int maxDragHeight) {

            }

            @Override
            public void onFooterStartAnimator(RefreshFooter footer, int footerHeight, int maxDragHeight) {

            }

            @Override
            public void onFooterFinish(RefreshFooter footer, boolean success) {

            }

            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {

            }

            @Override
            public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
                if (newState == RefreshState.None && showTips){
                    setVisibilityWithAnimation(View.VISIBLE,tab_anchor);
                    setVisibilityWithAnimation(View.VISIBLE,tab_anchor_shadow);
                    mHandler.postDelayed(hideTipsRunnable,2000);
                    showTips = false;
                }
            }
        });
    }

    /***********************************************************        LogicMethod           *************************************************************/

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        if (mPresenter != null && hasNext) {
            mPresenter.getNetData(UUID.randomUUID().toString(),title, pageIndex,10);
        } else {
            if (list.size() == 0)showErrorView(R.mipmap.default_page_search,"暂无内容","");
        }
        refreshLayout.finishLoadMore(3000);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        adapter.notifyDataSetChanged();//为了刷新时间
        pageIndex = 1;
        hasNext = true;
        if (mPresenter != null) {
            mPresenter.getNetData(UUID.randomUUID().toString(),title, pageIndex,10);
        }
        refreshLayout.finishRefresh(3000);
    }

    public void search(String title) {
        this.title = title;
        adapter.setSearchKey(title);
        if (viewsReady) onRefresh(refreshLayout);
    }

    /****************************************************************        Contract.View          **************************************************************/
    //以下方法中对View进行操作时最好进行 viewsReady 判断，防止网络回调数据时，Fragment的View已经被销毁而造成View空指针
    @Override
    public void setPresenter(CircleSearchPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showData(SearchPageListResp resp) {
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
        hasNext = resp.hasNext;
        if (pageIndex == 1) {
            list.clear();
            adapter.notifyDataSetChanged();
        }
        if (resp.list != null && resp.list.size() > 0) {
            dataSort(resp.list,resp.opType);
            pageIndex ++ ;
            if (viewsReady) error_view.setVisibility(View.GONE);
        } else {
            showTopTip(getString(R.string.circle_no_update));
        }
    }

    @Override
    public void showErrorView(int resId ,String tips, String advice) {
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

    @Override
    public void showTopTip(String tip) {
        showTips = true;
        mHandler.removeCallbacks(hideTipsRunnable);
        tab_anchor.setText(tip);
        tab_anchor_shadow.setText(tip);
    }


    private void dataSort(List<GetRecommendPageListResp.RecommendResult> newList,int opType){
        ArrayList<GetRecommendPageListResp.RecommendResult> localTop = new ArrayList<>();
        ArrayList<GetRecommendPageListResp.RecommendResult> localNormal = new ArrayList<>();

        for (GetRecommendPageListResp.RecommendResult result : list) {
            if (result.top != 1) {
                localNormal.add(result);
            } else {
                localTop.add(result);
            }
        }

        ArrayList<GetRecommendPageListResp.RecommendResult> netTop = new ArrayList<>();
        ArrayList<GetRecommendPageListResp.RecommendResult> netNormal = new ArrayList<>();

        for (GetRecommendPageListResp.RecommendResult result : newList) {
            if (result.top != 1) {
                netNormal.add(result);
            } else {
                netTop.add(result);
            }
        }

        //置顶数据去重，迭代器remove避免并发修改异常,去重可以用TreeSet优化
        Iterator iterator1 = netTop.iterator();
        while (iterator1.hasNext()) {
            GetRecommendPageListResp.RecommendResult next = (GetRecommendPageListResp.RecommendResult) iterator1.next();
            if (localTop.indexOf(next) != -1){
                iterator1.remove();
            }
        }

        //一般数据去重
        Iterator iterator2 = netNormal.iterator();
        while (iterator2.hasNext()) {
            GetRecommendPageListResp.RecommendResult next = (GetRecommendPageListResp.RecommendResult) iterator2.next();
            if (localNormal.indexOf(next) != -1){
                iterator2.remove();
            }
        }

        if (opType == 1 || opType == 3) {
            localTop.addAll(netTop);//如果是第一页或者是加载更多，list直接添加
            localNormal.addAll(netNormal);
        } else {
            localTop.addAll(0, netTop);
            localNormal.addAll(0, netNormal);
        }

        int originSize = list.size();//改变前的Size
        list.clear();
        list.addAll(localTop);
        list.addAll(localNormal);
        adapter.notifyDataSetChanged();
        int newSize = list.size();//改变后的Size

        if (opType == 2 || opType == 3) {
            if (newSize - originSize > 0) {
                showTopTip(String.format(getContext().getResources().getString(R.string.circle_refresh_tip),newSize - originSize));
            }else {
                showTopTip(getString(R.string.circle_no_update));
            }
        }
    }

    private void setVisibilityWithAnimation(int state, TextView tv){
        //属性动画对象
        if(state == View.VISIBLE){
            //隐藏view，高度从height变为0
            if (tv.getId() == R.id.tab_anchor_shadow) {
                tv.setVisibility(View.VISIBLE);
                ObjectAnimator animator = ObjectAnimator.ofFloat(tv, "scaleX", 0f, 1f);
                animator.setDuration(300);
                animator.start();
            } else {
                //显示view，高度从0变到height值
                ObjectAnimator animator = ObjectAnimator.ofFloat(tv, "scaleX", 0f, 1f);
                animator.setDuration(300);
                animator.start();
                tv.setVisibility(View.VISIBLE);
                tv.getLayoutParams().height = (int) getResources().getDimension(R.dimen.yhy_size_36px);
                tv.requestLayout();
            }
        }else{
            //隐藏view，高度从height变为0
            if (tv.getId() == R.id.tab_anchor_shadow) {
                tv.setVisibility(View.GONE);
            } else {
                valueAnimator = ValueAnimator.ofInt((int) getResources().getDimension(R.dimen.yhy_size_36px),0);
                valueAnimator.addUpdateListener(valueAnimator -> {
                    //获取当前的height值
                    int h =(Integer)valueAnimator.getAnimatedValue();
                    //动态更新view的高度
                    tv.getLayoutParams().height = h;
                    tv.requestLayout();
                });
                valueAnimator.setDuration(300);
                valueAnimator.start();
            }
        }
    }


    /****************************************************************        Handler          **************************************************************/
    @Override
    public void handleMessage(Message msg) {

    }

    /*****************************************************************         OnEvent         *************************************************************/
    //Login State
    public void onEvent(EvBusUserLoginState state) {

    }

    //disLike
    public void onEvent(EvBusDisLike disLike) {
        if (disLike.isArticle()) {
            if (disLike.getId() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).ugcId == disLike.getId()) {
                        list.remove(i);
                        adapter.notifyItemRemoved(i);
                        break;
                    }
                }
            }
        } else {
            if (disLike.getId() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).videoId == disLike.getId()) {
                        list.remove(i);
                        adapter.notifyItemRemoved(i);
                        break;
                    }
                }
            }
        }

        if (isFragmentVisible()) {
            mHandler.removeCallbacks(hideTipsRunnable);
            tab_anchor.setText(R.string.circle_dislike);
            tab_anchor_shadow.setText(R.string.circle_dislike);
            if (mLayoutManager.findFirstVisibleItemPosition() == 0) {
                setVisibilityWithAnimation(View.VISIBLE,tab_anchor);
                setVisibilityWithAnimation(View.VISIBLE,tab_anchor_shadow);
            } else {
                setVisibilityWithAnimation(View.VISIBLE,tab_anchor_shadow);
            }
            mHandler.postDelayed(hideTipsRunnable,2000);
        }
    }

    //News Comment Change
    public void onEvent(EvBusNewsCommentChange evBusNewsCommentChange) {
        for (GetRecommendPageListResp.RecommendResult result : list) {
            if (result.ugcId == evBusNewsCommentChange.ugcid) {
                if (evBusNewsCommentChange.isAdd) {
                    result.commentNum++;
                } else {
                    result.commentNum--;
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    //  Video Comment Change
    public void onEvent(EvBusVideoCommentChange change) {
        long id = 0;
        if (change.ugcId > 0) id = change.ugcId;
        if (change.liveId > 0) id = change.liveId;

        if (id > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).videoId > 0 && list.get(i).videoId == id) {
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

    /***********************************************************         LifeCycle           *************************************************************/
    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.release();
        mPresenter = null;
    }
}
