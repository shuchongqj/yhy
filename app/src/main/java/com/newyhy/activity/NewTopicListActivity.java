package com.newyhy.activity;

import android.content.Context;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Adapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.newyhy.network.CircleNetController;
import com.newyhy.network.NetHandler;
import com.quanyan.yhy.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yhy.common.base.BaseNewFragment;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * 话题列表 Fragment
 * Created by Jiervs on 2018/5/29.
 */

public class NewTopicListActivity extends BaseNewFragment implements NetHandler.NetHandlerCallback, OnRefreshListener,OnLoadMoreListener {

    private SmartRefreshLayout refreshLayout;
    private Context mContext;
    private RecyclerView rv_topic;
    private Adapter adapter;
    private LinearLayout error_view;
    private LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
    //data
    private int pageIndex;
    private ArrayList mList;
    //handler
    private NetHandler handler;
    private CircleNetController controller;

    /****************************************************        Constructor         *************************************************************/

    /****************************************************        LifeCycle         *************************************************************/
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /****************************************************        override         *************************************************************/
    @Override
    protected void initView() {
        super.initView();
        handler = new NetHandler(this);
        controller = new CircleNetController(getContext(),handler);
        EventBus.getDefault().register(this);

        refreshLayout = mRootView.findViewById(R.id.refreshLayout);
        rv_topic = mRootView.findViewById(R.id.rv_topic);
        error_view = mRootView.findViewById(R.id.error_view);//errorView
        mList = new ArrayList<>();
        initController();
        initClickListener();
        onRefresh(refreshLayout);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.new_topic_list_fragment;
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {

    }


    /****************************************************        UiMethod         *************************************************************/

    public void initController(){
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);
        rv_topic.setLayoutManager(mLayoutManager);
        //adapter = new Adapter();
        }

    /**
     * 根据情况显示需要的ErrorView
     */
    public void handlerErrorView(String tips, String advice, View.OnClickListener listener){
        ((TextView)error_view.findViewById(R.id.tv_tips)).setText(tips);
        ((TextView)error_view.findViewById(R.id.tv_advice)).setText(advice);
        error_view.setOnClickListener(listener);
        error_view.setVisibility(View.VISIBLE);
    }

    /**
     * ErrorView的点击监听
     */
    private View.OnClickListener toHot,noData;
    public void initClickListener(){
        toHot = new View.OnClickListener() {//转热门
            @Override
            public void onClick(View v) {

            }
        };

        noData = new View.OnClickListener() {//重新刷新
            @Override
            public void onClick(View v) {

            }
        };
    }

    public void activityRefresh(){
        onRefresh(refreshLayout);
    }

    @Override
    public void handleMessage(Message msg) {

    }

}
