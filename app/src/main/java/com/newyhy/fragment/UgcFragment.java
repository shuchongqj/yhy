package com.newyhy.fragment;

import android.content.Context;
import android.os.Bundle;
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
import com.newyhy.adapter.circle.DynamicAdapter;
import com.newyhy.network.CircleNetController;
import com.newyhy.network.NetHandler;
import com.newyhy.views.YhyRecyclerDivider;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.ShieldType;
import com.quanyan.yhy.libanalysis.Analysis;
import com.yhy.common.eventbus.event.EvBusBlack;
import com.yhy.common.eventbus.event.EvBusCircleChangeFollow;
import com.yhy.common.eventbus.event.EvBusCircleChangePraise;
import com.yhy.common.eventbus.event.EvBusCircleDelete;
import com.quanyan.yhy.eventbus.EvBusUserLoginState;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yhy.common.base.BaseNewFragment;
import com.yhy.common.beans.net.model.discover.UgcInfoResult;
import com.yhy.common.beans.net.model.discover.UgcInfoResultList;
import com.yhy.common.constants.ValueConstants;
import com.yhy.service.IUserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import de.greenrobot.event.EventBus;

import static com.newyhy.network.BaseNetController.MSG_ERROR;
import static com.newyhy.network.CircleNetController.MSG_UGC_LIST_NULL;
import static com.newyhy.network.CircleNetController.MSG_UGC_LIST_RECEIVE;

/**
 * New version UgcFragment
 * Created by Jiervs on 2018/4/17.
 */

public class UgcFragment extends BaseNewFragment implements NetHandler.NetHandlerCallback, OnRefreshListener, OnLoadMoreListener {

    private SmartRefreshLayout refreshLayout;
    private Context mContext;
    private RecyclerView rv_ugc;
    private LinearLayout error_view;
    private DynamicAdapter adapter;
    //data
    private int pageIndex;
    private int type;//0:主页 1:关注 2:热门 3:话题 4:我的动态
    private long userTargetId;
    private ArrayList<UgcInfoResult> mList;
    //handler
    private NetHandler handler;
    private CircleNetController controller;
    private int followType = -1;

    @Autowired
    IUserService userService;

    HashMap<String, String> extraMap = new HashMap<>();

    /****************************************************        Constructor         *************************************************************/

    public static UgcFragment newInstance(int type) {
        Bundle args = new Bundle();
        UgcFragment fragment = new UgcFragment();
        args.putInt("type", type);
        fragment.setArguments(args);
        return fragment;
    }

    public static UgcFragment newInstance(int type, long targetId) {
        Bundle args = new Bundle();
        UgcFragment fragment = new UgcFragment();
        args.putInt("type", type);
        args.putLong("targetId", targetId);
        fragment.setArguments(args);
        return fragment;
    }


    /****************************************************        LifeCycle         *************************************************************/
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!userService.isLogin() && type == 1) {
            mList.clear();
            adapter.notifyDataSetChanged();
            handlerErrorView("未登录，您可以去热门看看", "热门看看", toHot);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    protected int setLayoutId() {
        return R.layout.ugc_fragment;
    }

    @Override
    protected void initVars() {
        super.initVars();
        extraMap.put(Analysis.TAG, tag);

    }

    @Override
    protected void initView() {
        super.initView();
        handler = new NetHandler(this);
        controller = new CircleNetController(getContext(), handler);
        type = getArguments().getInt("type", 0);
        userTargetId = getArguments().getLong("targetId", 0);
        EventBus.getDefault().register(this);

        refreshLayout = mRootView.findViewById(R.id.refreshLayout);
        rv_ugc = mRootView.findViewById(R.id.rv_ugc);
        error_view = mRootView.findViewById(R.id.error_view);//errorView
        mList = new ArrayList<>();
        initController();
        initClickListener();
        onRefresh(refreshLayout);
    }

    /****************************************************        UiMethod         *************************************************************/
    public void initController() {
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);
        if (type == 0) refreshLayout.setEnableRefresh(false);
        rv_ugc.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        adapter = new DynamicAdapter(getActivity(), mList);
        adapter.extraMap = extraMap;
        rv_ugc.addItemDecoration(new YhyRecyclerDivider(getResources(), R.color.gray_E, R.dimen.yhy_size_6px, R.dimen.yhy_size_1px, LinearLayoutManager.VERTICAL));
        rv_ugc.setAdapter(adapter);
        ((DefaultItemAnimator) rv_ugc.getItemAnimator()).setSupportsChangeAnimations(false);
    }

    /**
     * ErrorView的点击监听
     */
    private View.OnClickListener toHot, noData;

    public void initClickListener() {
        //转热门
        toHot = v -> {

        };

        //重新刷新
        noData = v -> {

        };
    }

    /**
     * 根据情况显示需要的ErrorView
     */
    public void handlerErrorView(String tips, String advice, View.OnClickListener listener) {
        ((ImageView) error_view.findViewById(R.id.iv_error)).setImageResource(R.mipmap.default_page_lists);
        ((TextView) error_view.findViewById(R.id.tv_tips)).setText(tips);
        ((TextView) error_view.findViewById(R.id.tv_advice)).setText(advice);
        error_view.setOnClickListener(listener);
        error_view.setVisibility(View.VISIBLE);
    }

    public void activityRefresh(){
        if (mList != null && mList.size()>0) {
            rv_ugc.scrollToPosition(0);
        }
        refreshLayout.autoRefresh();
    }

    /****************************************************        Override         *************************************************************/
    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        pageIndex = 1;
        if (getArguments() != null) {
            type = getArguments().getInt("type", 0);
            userTargetId = getArguments().getLong("targetId", 0);
        }
        switch (type) {
            case 1://关注
                if (userService.isLogin()) {
                    error_view.setVisibility(View.GONE);
                    controller.getUGCPageList(getContext(), pageIndex, 10, 2);
                } else {
                    handlerErrorView("未登录，您可以去热门看看", "热门看看", toHot);
                }
                break;
            case 2://热门
                controller.getUGCPageListAll(getContext(), pageIndex, 10);
                break;

            default://根据userId查询ugc列表
                if (userTargetId > 0)
                    controller.getUGCPageListByUserId(getContext(), pageIndex, 10, userTargetId);
                break;
        }
        refreshLayout.finishRefresh(1000);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        pageIndex++;
        switch (type) {
            case 1://关注
                if (userService.isLogin()) {
                    error_view.setVisibility(View.GONE);
                    controller.getUGCPageList(getContext(), pageIndex, 10, 2);
                } else {
                    handlerErrorView("未登录，您可以去热门看看", "热门看看", toHot);
                }
                break;
            case 2://热门
                controller.getUGCPageListAll(getContext(), pageIndex, 10);
                break;
            default://根据userId查询ugc列表
                if (userTargetId > 0)
                    controller.getUGCPageListByUserId(getContext(), pageIndex, 10, userTargetId);
                break;
        }
        refreshLayout.finishLoadMore(1000);
    }


    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_UGC_LIST_RECEIVE://成功收到List
                error_view.setVisibility(View.GONE);
                receiveList(msg);
                break;

            case MSG_UGC_LIST_NULL://List为null
                if (pageIndex == 1) {
                    mList.clear();
                    adapter.notifyDataSetChanged();
                    handlerErrorView("未能获取数据", "刷新试试", noData);
                }
                break;

            case MSG_ERROR://请求失败
                ToastUtil.showToast(getActivity(),"请求失败");
                break;
        }
    }

    //接收List
    public void receiveList(Message msg) {
        if (pageIndex == 1) mList.clear();
        if (null != msg.obj) {
            UgcInfoResultList resultList = (UgcInfoResultList) msg.obj;
            if (resultList.ugcInfoList == null || resultList.ugcInfoList.size() == 0) return;
            if (followType == 1){
                for (UgcInfoResult ugcInfoResult : resultList.ugcInfoList) {
                    ugcInfoResult.type = 1;
                }
            }
            mList.addAll(resultList.ugcInfoList);
            pageIndex += 1;
            adapter.notifyDataSetChanged();
        }
    }

    public void setFollowStatus(String attentionType){

        if ("FOLLOW".equals(attentionType)) {
            followType = 1;
            if (mList == null) return;
            for (int i = 0; i < mList.size(); i++) {
                mList.get(i).type = 1;
            }
        }
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    /****************************************************        OnEvent         *************************************************************/
    /**
     * Login State
     */
    public void onEvent(EvBusUserLoginState state) {
        if (state.getUserLoginState() == 0) {
            onRefresh(refreshLayout);
        } else {
            if (!userService.isLogin() && type == 1) {
                mList.clear();
                adapter.notifyDataSetChanged();
                handlerErrorView("未登录，您可以去热门看看", "热门看看", toHot);
            }
        }
    }

    //praise State
    public void onEvent(EvBusCircleChangePraise state) {
        for (UgcInfoResult result : mList) {
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
        for (UgcInfoResult result : mList) {
            if (result.userInfo.userId == state.userId) {
                result.type = state.type;
            }
        }
        adapter.notifyDataSetChanged();

    }

    //删除动态
    public void onEvent(EvBusCircleDelete state) {
        for (int i=0; i<mList.size(); i++) {
            if (mList.get(i).id == state.id) {
                mList.remove(i);
                adapter.notifyItemRemoved(i);
                break;
            }
        }
    }

    //屏蔽
    public void onEvent(EvBusBlack black) {
        if (black.type.equals(ShieldType.USER_SUBJECT)) {//屏蔽用户
            Iterator iterator = mList.iterator();
            while (iterator.hasNext()) {
                UgcInfoResult result = (UgcInfoResult) iterator.next();
                if (result.userInfo.userId == black.id) {
                    iterator.remove();
                }
            }
            adapter.notifyDataSetChanged();
        }

        if (black.type.equals(ShieldType.SUBJECT)) {//屏蔽动态
            for (int i = 0; i < mList.size();i++) {
                if (mList.get(i).id == black.id) {
                    mList.remove(i);
                    adapter.notifyItemRemoved(i);
                    break;
                }
            }
        }
    }
}
