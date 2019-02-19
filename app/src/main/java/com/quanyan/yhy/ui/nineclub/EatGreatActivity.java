package com.quanyan.yhy.ui.nineclub;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

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
import com.quanyan.yhy.common.MerchantType;
import com.quanyan.yhy.ui.adapter.base.QuickAdapter;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.base.utils.TCEventHelper;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.nineclub.controller.BuyMustController;
import com.quanyan.yhy.ui.nineclub.helper.EatGreatItemHelper;
import com.yhy.common.beans.net.model.club.PageInfo;
import com.yhy.common.beans.net.model.master.MerchantInfo;
import com.yhy.common.beans.net.model.master.MerchantList;
import com.yhy.common.beans.net.model.master.MerchantQuery;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.AndroidUtils;
import com.yhy.common.utils.SPUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with Android Studio.
 * Title:EatGreatActivity
 * Description:美食列表界面
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:baojie
 * Date:2016-3-12
 * Time:12:05
 * Version 1.0
 */
public class EatGreatActivity extends BaseActivity {

    @ViewInject(R.id.pull_to_refresh_listview)
    private PullToRefreshListView mPullToRefreshListView;

    @ViewInject(R.id.base_pullrefresh_listview_parent_layout)
    private RelativeLayout mParentLayout;

    private ListView mListView;

    private BuyMustController mController;
    private QuickAdapter<MerchantInfo> mAdapter;

    protected int mPageIndex = 1;

    protected boolean isRefresh = true;
    private static int PAGESIZE = 15;
    private String mEatType;
    private boolean mHasNext = true;


    public static void gotoEatGreatActivity(Context context, String type) {
        Intent intent = new Intent(context, EatGreatActivity.class);
        intent.putExtra(SPUtils.EXTRA_TYPE, type);
        context.startActivity(intent);
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.base_pull_refresh_layout_listview, null);
    }

    private BaseNavView mBaseNavView;

    @Override
    public View onLoadNavView() {
        mBaseNavView = new BaseNavView(this);
        mBaseNavView.setTitleText(getString(R.string.eatgreat_title));
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
        mEatType = getIntent().getStringExtra(SPUtils.EXTRA_TYPE);
        initPlist();

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

        MerchantQuery query = new MerchantQuery();
        query.merchantType = MerchantType.EAT;

        PageInfo pageInfo = new PageInfo();
        pageInfo.pageNo = pageIndex;
        pageInfo.pageSize = PAGESIZE;
        query.pageInfo = pageInfo;

        mController.doGetEatGreatList(EatGreatActivity.this, query);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        hideLoadingView();
        hideErrorView(null);
        //刷新完成
        mPullToRefreshListView.onRefreshComplete();
        switch (msg.what) {
            case ValueConstants.MSG_EATGREAT_LIST_OK:
                MerchantList merchant = (MerchantList) msg.obj;
                if(merchant != null){
                    mHasNext = merchant.hasNext;
                    handlePageData(merchant.merchantList);
                }
                break;
            case ValueConstants.MSG_EATGREAT_LIST_KO:
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

    private void handlePageData(List<MerchantInfo> value) {

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
                    //调到必买详情页
                    MerchantInfo merchantInfo = mAdapter.getItem(position - headerCount);
                    NavUtils.gotoEatGreatDetailActivity(EatGreatActivity.this, merchantInfo.sellerId);
                    Map<String, String> map = new HashMap<String, String>();
                    map.put(AnalyDataValue.KEY_PID, merchantInfo.sellerId + "");
                    map.put(AnalyDataValue.KEY_PNAME, merchantInfo.name);
                    TCEventHelper.onEvent(EatGreatActivity.this, AnalyDataValue.TC_ID_DELICIOUS_FOOR_DETAIL, map);
                }
            }
        });

    }

    private void initPlist() {
        mPullToRefreshListView.setScrollingWhileRefreshingEnabled(!mPullToRefreshListView.isScrollingWhileRefreshingEnabled());
        mPullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        mListView = mPullToRefreshListView.getRefreshableView();
        mListView.setHeaderDividersEnabled(false);
        mListView.setDividerHeight(0);
        mAdapter = EatGreatItemHelper.setAdapter(this, new ArrayList<MerchantInfo>());
        mListView.setAdapter(mAdapter);
    }

}
