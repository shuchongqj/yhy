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
import com.quanyan.base.view.BaseNavView;
import com.quanyan.base.view.customview.pulltorefresh.PullToRefreshBase;
import com.quanyan.base.view.customview.pulltorefresh.PullToRefreshListView;
import com.quanyan.base.yminterface.ErrorViewClick;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.AnalyDataValue;
import com.quanyan.yhy.common.ErrorCode;
import com.quanyan.yhy.common.ItemType;
import com.quanyan.yhy.common.QueryType;
import com.quanyan.yhy.ui.adapter.base.QuickAdapter;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.base.utils.TCEventHelper;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.nineclub.controller.BuyMustController;
import com.quanyan.yhy.ui.nineclub.helper.BuyMustItemHelper;
import com.yhy.common.beans.net.model.master.QueryTerm;
import com.yhy.common.beans.net.model.master.QueryTermsDTO;
import com.yhy.common.beans.net.model.trip.ShortItem;
import com.yhy.common.beans.net.model.trip.ShortItemsResult;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.AndroidUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:BuyMustListActivity
 * Description:必买商品列表
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:baojie
 * Date:2016-3-14
 * Time:14:22
 * Version 1.0
 */
public class BuyMustListActivity extends BaseActivity {

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
    private String mBuyType;
    private boolean mHasNext = true;


    /**
     * 开启必买列表界面
     *
     * @param context
     *
     */
    public static void gotoBuyMustListActivity(Context context) {
        Intent intent = new Intent(context, BuyMustListActivity.class);
        context.startActivity(intent);
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.ac_buymustlist, null);
    }

    private BaseNavView mBaseNavView;

    @Override
    public View onLoadNavView() {
        mBaseNavView = new BaseNavView(this);
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

        List<QueryTerm> ts = new ArrayList<>();
        QueryTerm t1 = new QueryTerm();
        t1.type = QueryType.ITEM_TYPE;
        t1.value = ItemType.NORMAL;

        ts.add(t1);
        params.queryTerms = ts;
        mController.doGetBuyMustList(BuyMustListActivity.this, params);
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
                    ShortItem item = mAdapter.getItem(position - headerCount);
                    if (item.userInfo.merchantId != 0)
                        NavUtils.gotoProductDetail(BuyMustListActivity.this, ItemType.NORMAL, item.id,
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
        switch (msg.what) {
            case ValueConstants.MSG_BUYMUST_LIST_OK:
                ShortItemsResult shortItemsResult = (ShortItemsResult) msg.obj;
                if (shortItemsResult != null) {
                    mHasNext = shortItemsResult.hasNext;
                    handleFirstPageData(shortItemsResult.shortItemList);
                }
                break;
            case ValueConstants.MSG_BUYMUST_LIST_KO:
                if (isRefresh) {
                    if (mAdapter.getCount() == 0) {
                        mAdapter.clear();
                        showErrorView(null, ErrorCode.NETWORK_UNAVAILABLE == msg.arg1 ?
                                IActionTitleBar.ErrorType.NETUNAVAILABLE : IActionTitleBar.ErrorType.ERRORNET, "", "", "", new ErrorViewClick() {
                            @Override
                            public void onClick(View view) {
                                doNetDatas(1);
                            }
                        });
                    } else {
                        AndroidUtils.showToastCenter(this, StringUtil.handlerErrorCode(this, msg.arg1));
                    }
                } else {
                    AndroidUtils.showToastCenter(this, StringUtil.handlerErrorCode(this, msg.arg1));
                }
                break;
            case ValueConstants.MSG_HAS_NO_DATA:
                ToastUtil.showToast(this, getString(R.string.scenic_hasnodata));
                break;
        }
    }

    /**
     * 处理必买列表
     *
     * @param value
     */
    private void handleFirstPageData(List<ShortItem> value) {



        /*if (value == null || value.size() < PAGESIZE) {
            mPullToRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        } else {
            mPullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        }*/
        if (isRefresh) {
            if (value != null && value.size() > 0) {
                mAdapter.replaceAll(value);
            } else {
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
        mAdapter = BuyMustItemHelper.setAdapter(this, new ArrayList<ShortItem>());

        mListView.setAdapter(mAdapter);
    }

    private void initTitle() {
        mBaseNavView.setTitleText(getString(R.string.buymust_title));

        mBaseNavView.setRightImg(R.mipmap.ic_master_nav_search);
        mBaseNavView.setRightImgClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击标题的搜索框处理
                NavUtils.gotoSearchActivity(BuyMustListActivity.this,
                        ItemType.NORMAL,
                        null,
                        getString(R.string.title_guide_buy),
                        -1);
                TCEventHelper.onEvent(BuyMustListActivity.this, AnalyDataValue.TC_ID_SEARCH, AnalyDataValue.SEARCH_CLICK_MUST_BUY);
            }
        });

        /*mTitleLayout.setVisibility(View.VISIBLE);
        mSearchBox.setText(getString(R.string.text_top_search_buymust_hint));
        mIvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mTitleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击标题的搜索框处理
                NavUtils.gotoMasterSearchActivity(BuyMustListActivity.this, MasterSearchActivity.TYPE_LINE);
            }
        });

        mIvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/
    }
}
