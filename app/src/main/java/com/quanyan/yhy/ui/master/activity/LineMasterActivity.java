package com.quanyan.yhy.ui.master.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.quanyan.base.BaseListViewActivity;
import com.quanyan.base.baseenum.IActionTitleBar;
import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.ItemType;
import com.quanyan.yhy.common.MerchantType;
import com.quanyan.yhy.common.QueryType;
import com.quanyan.yhy.ui.adapter.base.BaseAdapterHelper;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.master.controller.MasterController;
import com.quanyan.yhy.ui.master.helper.MasterViewHelper;
import com.quanyan.yhy.ui.master.view.MasterLineHeadView;
import com.quanyan.yhy.ui.order.OrderTopView;
import com.yhy.common.beans.net.model.common.Booth;
import com.yhy.common.beans.net.model.master.QueryTerm;
import com.yhy.common.beans.net.model.master.QueryTermsDTO;
import com.yhy.common.beans.net.model.trip.ShortItem;
import com.yhy.common.beans.net.model.trip.ShortItemsResult;
import com.yhy.common.constants.ValueConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:LineMasterActivity
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-7-26
 * Time:17:13
 * Version 1.0
 * Description:
 */
public class LineMasterActivity extends BaseListViewActivity<ShortItem> {

    private MasterLineHeadView mMasterLineHead;
    private OrderTopView mTopView;
    private MasterController mController;
    private Boolean mHasNodata = false;

    public static void gotoLineMasterActivity(Activity context) {
        Intent intent = new Intent(context, LineMasterActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void fetchData(int pageIndex) {
        if (pageIndex == 1) {
            mController.doGetLineMasterBooth(this);
        }
        QueryTermsDTO params = new QueryTermsDTO();

        List<QueryTerm> ts = new ArrayList<>();
        QueryTerm t2 = new QueryTerm();
        t2.type = QueryType.SELLER_TYPE;
        t2.value = MerchantType.TALENT;
        ts.add(t2);

        QueryTerm t1 = new QueryTerm();
        t1.type = QueryType.ITEM_TYPE;
        t1.value = ItemType.FREE_LINE + "," + ItemType.TOUR_LINE + "," + ItemType.FREE_LINE_ABOARD + "," + ItemType.TOUR_LINE_ABOARD + "," + ItemType.CITY_ACTIVITY;
        ts.add(t1);

        params.queryTerms = ts;
        params.pageNo = getPageIndex();
        params.pageSize = ValueConstants.PAGESIZE;
        mController.doLineMasterList(this, params);
    }

    @Override
    public void dispatchMessage(Message msg) {
        hideLoadingView();
        hideErrorView(getBaseDropListView().getListViewViewParent());
        getBaseDropListView().getPullRefreshView().onRefreshComplete();
        switch (msg.what) {
            case ValueConstants.MSG_GET_MASTER_PRODUCTS_AD_OK:
                Booth booth = (Booth) msg.obj;
                handleBooth(booth);
                break;
            case ValueConstants.MSG_GET_MASTER_PRODUCTS_OK:
                ShortItemsResult shortItemsResult = (ShortItemsResult) msg.obj;
                handleLineList(shortItemsResult);
                break;
            case ValueConstants.MSG_GET_MASTER_PRODUCTS_AD_KO:
                break;
            case ValueConstants.MSG_GET_MASTER_PRODUCTS_KO:
                if (getBaseDropListView().getQuickAdapter().getCount() == 0) {
                    showNetErrorView(getBaseDropListView().getListViewViewParent(), msg.arg1);
                } else {
                    ToastUtil.showToast(this, StringUtil.handlerErrorCode(getApplicationContext(), msg.arg1));
                }
                break;
            case ValueConstants.MSG_HAS_NO_DATA:
                ToastUtil.showToast(this, getResources().getString(R.string.scenic_hasnodata));
                break;
        }
    }

    /**
     * 处理首页资源位
     *
     * @param booth
     */
    private void handleBooth(Booth booth) {
        if (booth == null || booth.showcases == null || booth.showcases.size() == 0) {
            return;
        }
        mMasterLineHead.handleData(booth.showcases.get(0));
    }

    /**
     * 处理线路搜索结果
     *
     * @param result
     */
    private void handleLineList(ShortItemsResult result) {
        if (result != null) {
            if (isRefresh()) {
                if (result.shortItemList != null) {
                    getBaseDropListView().getQuickAdapter().replaceAll(result.shortItemList);
                } else {
                    getBaseDropListView().getQuickAdapter().clear();
                }
                if (getBaseDropListView().getQuickAdapter().getCount() == 0) {
                    mHasNodata = true;
                    getBaseDropListView().getQuickAdapter().add(new ShortItem());
                }else{
                    mHasNodata = false;
                }
            } else {
                mHasNodata = false;
                if (result.shortItemList != null) {
                    getBaseDropListView().getQuickAdapter().addAll(result.shortItemList);
                } else {
                    ToastUtil.showToast(this, getString(R.string.no_more));
                }
            }
        }
    }

    /**
     * 展示无数据的提示
     */
    private void showNoDataPageView() {
        showErrorView(getBaseDropListView().getListViewViewParent(), IActionTitleBar.ErrorType.EMPTYVIEW, getString(R.string.label_no_products_searched), " ", "", null);
    }


    @Override
    protected void initView(Bundle savedInstanceState) {
        mTopView.setOrderTopTitle(getString(R.string.label_line_master_title));
        mController = new MasterController(this, mHandler);
        initHeadView(getBaseDropListView().getListView());
        initClick();
        manualRefresh();
    }

    @Override
    public void convertItem(BaseAdapterHelper helper, ShortItem item) {
        if(mHasNodata){
            helper.setVisible(R.id.item_recommend_nodata_layout, true)
                    .setVisible(R.id.item_recommend_layout, false)
                    .setText(R.id.item_recommend_nodata_text, getString(R.string.label_nodata_wonderfulplay_list_message));
        }else {
            helper.setVisible(R.id.item_recommend_nodata_layout, false)
                    .setVisible(R.id.item_recommend_layout, true);
            MasterViewHelper.handleLineMasterItem(this, helper, item);
        }
    }

    @Override
    public int setItemLayout() {
        return R.layout.item_home_recommend;
    }

    @Nullable
    @Override
    public List<String> setTabStrings() {
        return null;
    }

    @Nullable
    @Override
    public List<View> setPopViews() {
        return null;
    }

    @Override
    public void onItemClick(AdapterView parent, View view, int position, long id) {
        if (position < mBaseDropListView.getListView().getHeaderViewsCount()) {
            return;
        }
        ShortItem item = ((ShortItem) getAdapter().getItem(position - mBaseDropListView.getListView().getHeaderViewsCount()));
        NavUtils.gotoProductDetail(LineMasterActivity.this,
                item.itemType,
                item.id,
                item.title);
    }

    @Override
    public View onLoadNavView() {
        mTopView = new OrderTopView(this);
        return mTopView;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }

    private void initHeadView(ListView listView) {
        mMasterLineHead = new MasterLineHeadView(this);
        listView.addHeaderView(mMasterLineHead);
    }

    private void initClick() {
        mTopView.setBackClickListener(new OrderTopView.BackClickListener() {
            @Override
            public void clickBack() {
                LineMasterActivity.this.finish();
            }
        });
    }
}
