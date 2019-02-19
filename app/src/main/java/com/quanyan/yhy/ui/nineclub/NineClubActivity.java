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
import com.quanyan.yhy.common.ErrorCode;
import com.quanyan.yhy.common.ResourceType;
import com.quanyan.yhy.ui.adapter.base.QuickAdapter;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.nineclub.controller.BuyMustController;
import com.quanyan.yhy.ui.nineclub.helper.NineClubItemHelper;
import com.yhy.common.beans.net.model.RCShowcase;
import com.yhy.common.beans.net.model.club.PageInfo;
import com.yhy.common.beans.net.model.common.Booth;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.AndroidUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:NineClubActivity
 * Description:9休club列表界面
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:baojie
 * Date:2016-3-8
 * Time:15:34
 * Version 1.0
 */

public class NineClubActivity extends BaseActivity {

    @ViewInject(R.id.fl_parent)
    private FrameLayout mParentLayout;

    @ViewInject(R.id.pull_to_refresh_listview)
    private PullToRefreshListView mPullToRefreshListView;
    private ListView mListView;
    private QuickAdapter<RCShowcase> mAdapter;

    private boolean isRefresh = true;
    protected int mPageIndex = 1;
    private static int PAGESIZE = 5;

    private BuyMustController mController;
    private List<RCShowcase> mShowcasesList;


    public static void gotoNineClubActivity(Context context) {
        Intent intent = new Intent(context, NineClubActivity.class);
        context.startActivity(intent);
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this,R.layout.ac_nineclub, null);
    }

    private BaseNavView mBaseNavView;

    @Override
    public View onLoadNavView() {
        mBaseNavView = new BaseNavView(this);
        mBaseNavView.setTitleText(getString(R.string.nineclub_title));
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

        initPList();

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

        String boothCode = ResourceType.QUANYAN_CLUB_PAGE_HEADLINE;
        PageInfo pageInfo = new PageInfo();
        pageInfo.pageNo = pageIndex;
        pageInfo.pageSize = PAGESIZE;

        mController.doGetNineClubList(NineClubActivity.this, boothCode, pageInfo);
    }

    private void initEvent() {
        mPullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                doNetDatas(1);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if(mShowcasesList != null && mShowcasesList.size() >= PAGESIZE){
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
                    RCShowcase item = mAdapter.getItem(position - headerCount);
                    //跳转对应的专辑列表
                    NavUtils.depatchAllJump(NineClubActivity.this, item, position);
                }
            }
        });
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        hideLoadingView();
        hideErrorView(null);
        mPullToRefreshListView.onRefreshComplete();
        switch (msg.what) {
            case ValueConstants.MSG_NINECLUB_LIST_OK:
                Booth booth = (Booth) msg.obj;
                if (booth != null) {
                    mShowcasesList = booth.showcases;
                    handleFirstPageData(mShowcasesList);
                }
                break;
            case ValueConstants.MSG_NINECLUB_LIST_KO:
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

    private void handleFirstPageData(List<RCShowcase> value) {
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


    private void initPList() {
        mPullToRefreshListView.setScrollingWhileRefreshingEnabled(!mPullToRefreshListView.isScrollingWhileRefreshingEnabled());
        mPullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        mAdapter = NineClubItemHelper.setAdapter(this, new ArrayList<RCShowcase>());
        mListView = mPullToRefreshListView.getRefreshableView();
        //addHeadBanner();
        mListView.setAdapter(mAdapter);
    }


}
