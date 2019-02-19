package com.newyhy.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.newyhy.adapter.circle.DynamicAdapter;
import com.newyhy.network.CircleNetController;
import com.newyhy.network.NetHandler;
import com.newyhy.views.YhyRecyclerDivider;
import com.quanyan.base.util.StringUtil;
import com.quanyan.base.view.BaseNavView;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.AnalyDataValue;
import com.quanyan.yhy.common.ShieldType;
import com.yhy.common.eventbus.event.EvBusBlack;
import com.yhy.common.eventbus.event.EvBusCircleChangeFollow;
import com.yhy.common.eventbus.event.EvBusCircleChangePraise;
import com.yhy.common.eventbus.event.EvBusCircleDelete;
import com.quanyan.yhy.eventbus.EvBusUserLoginState;
import com.quanyan.yhy.libanalysis.AnEvent;
import com.quanyan.yhy.libanalysis.Analysis;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.base.utils.TCEventHelper;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yhy.common.base.BaseNewActivity;
import com.yhy.common.beans.net.model.discover.TopicInfoResult;
import com.yhy.common.beans.net.model.discover.UgcInfoResult;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.CommonUtil;
import com.yhy.imageloader.ImageLoadManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

import static com.newyhy.network.BaseNetController.MSG_ERROR;
import static com.newyhy.network.CircleNetController.MSG_TOPIC_DETAIL_OK;
import static com.newyhy.network.CircleNetController.MSG_UGC_LIST_NULL;
import static com.newyhy.network.CircleNetController.MSG_UGC_LIST_RECEIVE;

/**
 * 新的话题详情列表页面
 * Created by Jiervs on 2018/6/1.
 */

public class NewTopicDetailActivity extends BaseNewActivity implements NetHandler.NetHandlerCallback,OnRefreshListener,OnLoadMoreListener {

    private SmartRefreshLayout refreshLayout;
    private Context mContext = this;
    private RecyclerView rv_ugc;
    private LinearLayout error_view;
    private DynamicAdapter adapter;
    private LinearLayoutManager mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
    //TitleBar
    private BaseNavView nav;
    //topicHeader
    private View headerView;
    private ImageView iv_background;
    private ImageView iv_back;
    private TextView tv_topic;
    private TextView tv_join_num;
    private TextView tv_read_num;
    private TextView tv_describe;
    private Button btn_join;
    //data
    private int pageIndex;
    private int type;//0:主页 1:关注 2:热门 3:话题
    private String topicTitle;
    private long topicId;
    private ArrayList<UgcInfoResult> mList;
    //handler
    private NetHandler handler;
    private CircleNetController controller;

    HashMap<String, String> extraMap = new HashMap<>();

    public static void gotoNewTopicDetailsActivity(Context context, String topicName, long topicId) {
        Intent intent = new Intent(context, NewTopicDetailActivity.class);
        if (!StringUtil.isEmpty(topicName)) {
            intent.putExtra("topicTitle", topicName);
        }
        intent.putExtra("topicId", topicId);
        context.startActivity(intent);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.new_topic_detail_activity;
    }

    @Override
    protected void initVars() {
        super.initVars();
        extraMap.put(Analysis.TAG, tag);

    }

    @SuppressLint("InflateParams")
    @Override
    protected void initView() {
        super.initView();
        handler = new NetHandler(this);
        controller = new CircleNetController(this,handler);
        type = getIntent().getIntExtra("type",0);
        topicTitle =  getIntent().getStringExtra("topicTitle");
        topicId = getIntent().getLongExtra("topicId",0);
        EventBus.getDefault().register(this);

        nav = findViewById(R.id.nav);

        refreshLayout = findViewById(R.id.refreshLayout);
        rv_ugc = findViewById(R.id.rv_ugc);
        btn_join = findViewById(R.id.btn_join);
        error_view = findViewById(R.id.error_view);//errorView
        mList = new ArrayList<>();

        headerView = LayoutInflater.from(this).inflate(R.layout.topic_detail_header_view,null);
        iv_background = headerView.findViewById(R.id.iv_background);
        iv_back = headerView.findViewById(R.id.iv_back);
        iv_back.setOnClickListener(v -> finish());
        tv_topic = headerView.findViewById(R.id.tv_topic);
        tv_join_num = headerView.findViewById(R.id.tv_join_num);
        tv_read_num = headerView.findViewById(R.id.tv_read_num);
        tv_describe = headerView.findViewById(R.id.tv_describe);
        initController();
        adapter.addHeaderView(headerView);
        initClickListener();
        onRefresh(refreshLayout);
        mImmersionBar.fitsSystemWindows(false).transparentBar().statusBarDarkFont(true).init();
    }

    @Override
    protected void initData() {
        super.initData();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /***************************************************         Override          ************************************************************/

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        if (topicTitle != null) {
            controller.getTopicUGCPageList(this,topicTitle,pageIndex,10);
        }
        refreshLayout.finishLoadMore(1000);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        pageIndex = 1;
        if (topicTitle != null) {
            controller.getTopicInfo(this,topicTitle);
            controller.getTopicUGCPageList(this,topicTitle,pageIndex,10);
        }
        refreshLayout.finishRefresh(1000);
    }

    @Override
    public void handleMessage(Message msg) {
        if(isFinishing()){
            return;
        }
        switch (msg.what) {

            case MSG_TOPIC_DETAIL_OK://获取话题详情
                TopicInfoResult topicInfoResult = (TopicInfoResult) msg.obj;
                if (topicInfoResult != null) {
                    //处理话题头
                    ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(topicInfoResult.pics),R.mipmap.icon_default_750_420,iv_background);
                    tv_topic.setText(topicInfoResult.title);
                    nav.setTitleText(topicInfoResult.title);
                    tv_join_num.setText("参与" + topicInfoResult.talkNum);
                    tv_read_num.setText("阅读" + topicInfoResult.redNum);
                    if (!TextUtils.isEmpty(topicInfoResult.content)) {
                        tv_describe.setText(topicInfoResult.content);
                        tv_describe.setVisibility(View.VISIBLE);
                    }else {
                        tv_describe.setVisibility(View.GONE);
                    }
                } else {
                    ToastUtil.showToast(this, getResources().getString(R.string.topic_details_forbid));
                    finish();
                }
                break;

            case MSG_UGC_LIST_RECEIVE://成功收到List
                error_view.setVisibility(View.GONE);
                receiveList(msg);
                break;

            case MSG_UGC_LIST_NULL://List为null
                if (pageIndex == 1) {
                    mList.clear();
                    adapter.notifyDataSetChanged();
                    handlerErrorView("未能获取数据","刷新试试",noData);
                }
                break;

            case MSG_ERROR://请求失败
                ToastUtil.showToast(this,"请求失败");
                break;
        }
    }

    //接收List
    public void receiveList(Message msg) {
        if (pageIndex == 1) mList.clear();
        if (null != msg.obj) {
            List<UgcInfoResult> ugcInfoList = (List<UgcInfoResult>) msg.obj;
            mList.addAll(ugcInfoList);
            pageIndex += 1;
            adapter.notifyDataSetChanged();
        }
    }

    /****************************************************        UiMethod         *************************************************************/
    public void initController(){
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);
        rv_ugc.setLayoutManager(mLayoutManager);
        adapter = new DynamicAdapter(NewTopicDetailActivity.this,mList);
        adapter.extraMap = extraMap;
        rv_ugc.addItemDecoration(new YhyRecyclerDivider(getResources(), R.color.gray_E, R.dimen.yhy_size_6px, R.dimen.yhy_size_1px, LinearLayoutManager.VERTICAL));
        rv_ugc.setAdapter(adapter);
        rv_ugc.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView rv, int dx, int dy) {
                super.onScrolled(rv, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) rv_ugc.getLayoutManager();
                int position = layoutManager.findFirstVisibleItemPosition();
                if (position == 0) {
                    View firstVisibleChildView = layoutManager.findViewByPosition(0);
                    if (Math.abs(firstVisibleChildView.getTop()) > getResources().getDimension(R.dimen.yhy_size_112px)) {//112数值是HeaderView 的高度减去NavBar的高度
                        if (tv_topic != null) {
                            nav.setTitleText(tv_topic.getText().toString());
                        }
                        nav.setBackgroundResource(R.color.white);
                        nav.setLeftImage(R.mipmap.arrow_back_gray);
                        nav.setVisibility(View.VISIBLE);
                        mImmersionBar.fitsSystemWindows(true).statusBarColor(R.color.White).statusBarDarkFont(true).init();
                    } else {
                        nav.setVisibility(View.GONE);
                        mImmersionBar.fitsSystemWindows(false).transparentBar().statusBarDarkFont(true).init();
                    }
                } else {
                    if (tv_topic != null) {
                        nav.setTitleText(tv_topic.getText().toString());
                    }
                    nav.setBackgroundResource(R.color.white);
                    nav.setLeftImage(R.mipmap.arrow_back_gray);
                    nav.setVisibility(View.VISIBLE);
                    mImmersionBar.fitsSystemWindows(true).statusBarColor(R.color.White).statusBarDarkFont(true).init();
                }
            }
        });

    }

    /**
     * ErrorView的点击监听
     */
    private View.OnClickListener noData;
    public void initClickListener(){

        //重新刷新
        noData = v -> {

        };

        //参与话题
        btn_join.setOnClickListener(v -> {
            //统计事件
            Analysis.pushEvent(mContext, AnEvent.JOIN_TOPIC);
            Map<String, String> map = new HashMap<>();
            map.put(AnalyDataValue.KEY_ID, topicId + "");
            map.put(AnalyDataValue.KEY_NAME, topicTitle);
            TCEventHelper.onEvent(NewTopicDetailActivity.this, AnalyDataValue.TOPIC_PARTAKE, map);
            NavUtils.gotoAddLiveActivity(NewTopicDetailActivity.this, topicTitle);
        });

    }

    /**
     * 根据情况显示需要的ErrorView
     */
    public void handlerErrorView(String tips, String advice, View.OnClickListener listener){
        ((ImageView) error_view.findViewById(R.id.iv_error)).setImageResource(R.mipmap.default_page_lists);
        ((TextView)error_view.findViewById(R.id.tv_tips)).setText(tips);
        ((TextView)error_view.findViewById(R.id.tv_advice)).setText(advice);
        error_view.setOnClickListener(listener);
        error_view.setVisibility(View.VISIBLE);
    }

    /****************************************************        OnEvent         *************************************************************/
    /**
     *Login State
     */
    public void onEvent(EvBusUserLoginState state) {
        if (state.getUserLoginState() == 0) {
            onRefresh(refreshLayout);
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
}
