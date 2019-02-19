package com.quanyan.yhy.ui.nineclub;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.baseenum.IActionTitleBar;
import com.quanyan.base.util.LocalUtils;
import com.quanyan.base.util.StringUtil;
import com.quanyan.base.view.BaseNavTitleView;
import com.quanyan.base.view.customview.pulltorefresh.PullToRefreshBase;
import com.quanyan.base.view.customview.pulltorefresh.PullToRefreshListView;
import com.quanyan.base.yminterface.ErrorViewClick;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.ErrorCode;
import com.quanyan.yhy.common.ItemType;
import com.quanyan.yhy.common.QueryType;
import com.quanyan.yhy.ui.adapter.base.BaseAdapterHelper;
import com.quanyan.yhy.ui.adapter.base.QuickAdapter;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.master.helper.MasterViewHelper;
import com.quanyan.yhy.ui.nineclub.bean.JourneyDays;
import com.quanyan.yhy.ui.nineclub.controller.BuyMustController;
import com.quanyan.yhy.ui.nineclub.helper.BuyMustItemHelper;
import com.yhy.common.beans.net.model.master.QueryTerm;
import com.yhy.common.beans.net.model.master.QueryTermsDTO;
import com.yhy.common.beans.net.model.trip.ShortItem;
import com.yhy.common.beans.net.model.trip.ShortItemsResult;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.AndroidUtils;
import com.yhy.common.utils.JSONUtils;
import com.yhy.common.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:BuyMustListActivity
 * Description:9club单个主题列表
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:baojie
 * Date:2016-3-14
 * Time:14:22
 * Version 1.0
 */
public class NineClubDetailListActivity extends BaseActivity {

    @ViewInject(R.id.fl_parent)
    private FrameLayout mParentLayout;

    /*@ViewInject(R.id.tv_cancle)
    private TextView mIvCancle;

    @ViewInject(R.id.top_bar_search_layout)
    private LinearLayout mTitleLayout;

    @ViewInject(R.id.tv_search_box)
    private TextView mSearchBox;*/

    /*@ViewInject(R.id.indicator)
    private TabPageIndicator mIndicator;

    @ViewInject(R.id.pager)
    private ViewPager mPager;*/

    @ViewInject(R.id.pull_to_refresh_listview)
    private PullToRefreshListView mPullToRefreshListView;
    private ListView mListView;
    private QuickAdapter<ShortItem> mAdapter;
    private BuyMustController mController;

    protected boolean isRefresh = true;

    protected int mPageIndex = 1;
    private static int PAGESIZE = 10;
    private String mPageTitle;//标题名字
    private String mPageType;//专辑类型
    private String mDataId;//id
    private String mNetType = "";//访问网络商品类型
    private boolean mHasNext = true;



    /**
     * 开启9club单个主题列表
     * @param context
     * @param id
     * @param pageType
     * @param title
     */
    public static void gotoNineClubDetailListActivity(Context context, String id, String pageType, String title) {
        Intent intent = new Intent(context, NineClubDetailListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(SPUtils.EXTRA_TYPE, pageType);
        bundle.putString(SPUtils.EXTRA_ID, id);
        bundle.putString(SPUtils.EXTRA_TITLE, title);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.ac_buymustlist, null);
    }

    private BaseNavTitleView mBaseNavView;

    @Override
    public View onLoadNavView() {
        mBaseNavView = new BaseNavTitleView(this);
        return mBaseNavView;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ViewUtils.inject(this);

        mController = new BuyMustController(this, mHandler);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            mPageType = bundle.getString(SPUtils.EXTRA_TYPE);
            mDataId = bundle.getString(SPUtils.EXTRA_ID);
            mPageTitle = bundle.getString(SPUtils.EXTRA_TITLE);
            mNetType = mPageType;
        }

        //初始化头部信息
        initTitle();
        //PullToRefreshListView初始化
        initPList();
        //事件处理
        initEvent();
        //访问网络
        showLoadingView(getString(R.string.loading_text));
        doNetDatas(mPageIndex);
    }

    private void doNetDatas(int pageIndex) {
        if (1 == pageIndex) {
            isRefresh = true;
        } else {
            isRefresh = false;
        }

        QueryTermsDTO params = new QueryTermsDTO();
        params.pageNo = pageIndex;
        params.pageSize = PAGESIZE;

        if(!StringUtil.isEmpty(SPUtils.getExtraCurrentLon(getApplicationContext()))) {
            params.longitude = Double.parseDouble(SPUtils.getExtraCurrentLon(getApplicationContext()));
        }
        if(!StringUtil.isEmpty(SPUtils.getExtraCurrentLat(getApplicationContext()))) {
            params.latitude = Double.parseDouble(SPUtils.getExtraCurrentLat(getApplicationContext()));
        }
        List<QueryTerm> ts = new ArrayList<>();

        //封装数据
        envelopData(ts);

        params.queryTerms = ts;
        mController.doGetBuyMustList(NineClubDetailListActivity.this,params);

    }

    private void envelopData(List<QueryTerm> ts) {

        if(ItemType.ARROUND_FUN.equals(mPageType)){
            QueryTerm t3 = new QueryTerm();
            t3.type = QueryType.DAYS;
            JourneyDays journeyDays = new JourneyDays();
            journeyDays.setMinDays(0);
            journeyDays.setMaxDays(2);
            t3.value = JSONUtils.toJson(journeyDays);
            ts.add(t3);
        }

        QueryTerm t1 = new QueryTerm();
        t1.type = QueryType.ITEM_TYPE;
        if(ItemType.ARROUND_FUN.equals(mNetType)){
            mNetType = ItemType.FREE_LINE + "," + ItemType.TOUR_LINE + "," + ItemType.FREE_LINE_ABOARD + "," + ItemType.TOUR_LINE_ABOARD;
        }
        t1.value = mNetType;
        ts.add(t1);

        if(!StringUtil.isEmpty(mDataId)){
            QueryTerm t2 = new QueryTerm();
            t2.type = QueryType.SUBJECT;
            t2.value = mDataId;
            ts.add(t2);
        }

    }

    private void initEvent() {

        mPullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                doNetDatas(1);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if(mHasNext){
                    doNetDatas((mAdapter.getCount() / PAGESIZE) + 1);
                }else {
                    mHandler.sendEmptyMessageDelayed(ValueConstants.MSG_HAS_NO_DATA, 1000);
                }
            }
        });

        mPullToRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int headerCount = mListView.getHeaderViewsCount();
                if (position >= headerCount) {
                    ShortItem item = (ShortItem) mAdapter.getItem(position - headerCount);
                    NavUtils.gotoProductDetail(NineClubDetailListActivity.this, item.itemType, item.id,
                            item.title);
                }
            }
        });
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        hideLoadingView();
        hideErrorView(null);
        //刷新完成
        mPullToRefreshListView.onRefreshComplete();
        switch (msg.what){
            case ValueConstants.MSG_BUYMUST_LIST_OK:
                ShortItemsResult shortItemsResult = (ShortItemsResult) msg.obj;
                if (shortItemsResult != null) {
                    mHasNext = shortItemsResult.hasNext;
                    handleFirstPageData(shortItemsResult.shortItemList);
                }
                break;
            case ValueConstants.MSG_BUYMUST_LIST_KO:
                if(isRefresh) {
                    if(mAdapter.getCount() == 0){
                        mAdapter.clear();
                        showErrorView(null, ErrorCode.NETWORK_UNAVAILABLE == msg.arg1 ?
                                IActionTitleBar.ErrorType.NETUNAVAILABLE : IActionTitleBar.ErrorType.ERRORNET, "", "", "", new ErrorViewClick() {
                            @Override
                            public void onClick(View view) {
                                doNetDatas(1);
                            }
                        });
                    }else {
                        AndroidUtils.showToastCenter(this, StringUtil.handlerErrorCode(this, msg.arg1));
                    }
                }else {
                    AndroidUtils.showToastCenter(this, StringUtil.handlerErrorCode(this, msg.arg1));
                }
                break;
            case ValueConstants.MSG_HAS_NO_DATA:
                ToastUtil.showToast(this, getString(R.string.scenic_hasnodata));
                break;
        }
    }

    private void handleFirstPageData(List<ShortItem> value) {

        /*if (value == null || value.size() < PAGESIZE) {
            mPullToRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        } else {
            mPullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        }*/
        if (isRefresh) {
            if (value != null && value.size() > 0) {
                mAdapter.replaceAll(value);
            }else {
                mAdapter.clear();
                //没有数据的显示
                showNoDataPage();
            }
        } else {
            if (value != null) {
                mAdapter.addAll(value);
            }
        }
    }

    private void showNoDataPage() {
        showErrorView(mParentLayout, IActionTitleBar.ErrorType.EMPTYVIEW, getString(R.string.label_nodata_wonderful_play_list), getString(R.string.label_nodata_wonderfulplay_list_message), "", null);
    }

    private void initPList() {
        mPullToRefreshListView.setScrollingWhileRefreshingEnabled(!mPullToRefreshListView.isScrollingWhileRefreshingEnabled());
        mPullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        mListView = mPullToRefreshListView.getRefreshableView();

        mListView.setHeaderDividersEnabled(false);
        mListView.setDividerHeight(0);

        //必买
        if(ItemType.NORMAL.equals(mPageType)){
            mAdapter = new QuickAdapter<ShortItem>(this, R.layout.item_buymustadapter, new ArrayList<ShortItem>()) {
                @Override
                protected void convert(BaseAdapterHelper helper, ShortItem item) {
                    BuyMustItemHelper.handlerBuyMustData(NineClubDetailListActivity.this, helper, item);
                }
            };
        }else {//其他商品
            mAdapter = new QuickAdapter<ShortItem>(this, R.layout.item_home_recommend, new ArrayList<ShortItem>()) {
                @Override
                protected void convert(BaseAdapterHelper helper, ShortItem item) {
                    MasterViewHelper.handleLineItem(NineClubDetailListActivity.this, helper, item, "");
                }
            };
        }

        mListView.setAdapter(mAdapter);

    }


    private void initTitle() {

        if(mPageTitle != null){
            mBaseNavView.setTitleText(mPageTitle);
        }

        /*setRightImageView(R.mipmap.master_top_right_search_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击标题的搜索框处理
                NavUtils.gotoMasterSearchActivity(NineClubDetailListActivity.this, MasterSearchActivity.TYPE_MUST_BUY);
            }
        });*/

    }


}
