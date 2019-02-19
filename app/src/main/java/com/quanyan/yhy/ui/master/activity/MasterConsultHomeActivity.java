package com.quanyan.yhy.ui.master.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.util.StringUtil;
import com.quanyan.base.view.customview.NoScrollGridView;
import com.quanyan.base.view.customview.imgpager.ImgPagerView;
import com.quanyan.base.view.customview.pulltorefresh.PullToRefreshBase;
import com.quanyan.base.view.customview.pulltorefresh.PullToRefreshObserableScrollView;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.AnalyDataValue;
import com.quanyan.yhy.common.ResourceType;
import com.quanyan.yhy.ui.adapter.base.BaseAdapterHelper;
import com.quanyan.yhy.ui.adapter.base.QuickAdapter;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.base.utils.TCEventHelper;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.master.controller.MasterController;
import com.quanyan.yhy.ui.master.helper.MasterViewHelper;
import com.quanyan.yhy.ui.order.OrderTopView;
import com.yhy.common.beans.net.model.common.Booth;
import com.yhy.common.beans.net.model.common.BoothList;
import com.yhy.common.beans.net.model.item.BoothItem;
import com.yhy.common.beans.net.model.item.BoothItemsResult;
import com.yhy.common.beans.net.model.trip.ShortItem;
import com.yhy.common.constants.ValueConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:MasterConsultHomeActivity
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-7-20
 * Time:14:18
 * Version 1.0
 * Description:
 */
public class MasterConsultHomeActivity extends BaseActivity {

    private OrderTopView mOrderTopView;

    @ViewInject(R.id.ps_master_scroll_view)
    private PullToRefreshObserableScrollView mPullToRefreshObserableScrollView;
    @ViewInject(R.id.ll_master_view)
    private LinearLayout mMasterView;

    @ViewInject(R.id.iv_pager)
    private ImgPagerView mImgPager;
    @ViewInject(R.id.ll_master_consult)
    private LinearLayout mMasterConsultLayout;
    @ViewInject(R.id.rl_more_consult)
    private RelativeLayout mMoreConsultLayout;
    @ViewInject(R.id.iv_service_flow)
    private ImageView mServiceFlow;

    private MasterController mMasterController;

    private LayoutInflater mInflater;

    public static void gotoMasterConsultHomeActivity(Context context) {
        Intent intent = new Intent(context, MasterConsultHomeActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ViewUtils.inject(this);
        mMasterController = new MasterController(this, mHandler);
        mInflater = LayoutInflater.from(this);
//        mImgPager.setScale(this, (float) ValueConstants.SCALE_VALUE_BANNER);
        mServiceFlow.setImageResource(R.mipmap.consult_flow_image);
        fetchData();
        initClick();
    }

    private void initClick() {
        mOrderTopView.setBackClickListener(new OrderTopView.BackClickListener() {
            @Override
            public void clickBack() {
                MasterConsultHomeActivity.this.finish();
            }
        });

        mPullToRefreshObserableScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ObservableScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ObservableScrollView> refreshView) {
                fetchData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ObservableScrollView> refreshView) {

            }
        });

        mMoreConsultLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TCEventHelper.onEvent(MasterConsultHomeActivity.this, AnalyDataValue.CONSULTING_HOME_CONSULTING_SERVICE_LIST);
                NavUtils.gotoMasterAdviceListActivity(MasterConsultHomeActivity.this);
            }
        });

    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.activity_masterconsulthome, null);
    }

    @Override
    public View onLoadNavView() {
        mOrderTopView = new OrderTopView(this);
        mOrderTopView.setOrderTopTitle(getResources().getString(R.string.master_consult_home_title));
        return mOrderTopView;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        hideLoadingView();
        hideErrorView(null);
        mPullToRefreshObserableScrollView.onRefreshComplete();
        switch (msg.what) {
            case ValueConstants.MSG_MASTER_SEARCH_OK://获取咨询达人配置数据
                handleConsultService(msg.obj);
                break;
            case ValueConstants.MSG_MASTER_SEARCH_KO:
                ToastUtil.showToast(this, StringUtil.handlerErrorCode(this, msg.arg1));
                break;
            case ValueConstants.MSG_GET_FIRST_PAGE_LIST_OK://获取咨询达人广告位数据
                handBannerView(msg.obj);
                break;
            case ValueConstants.MSG_GET_FIRST_PAGE_LIST_KO:
                ToastUtil.showToast(this, StringUtil.handlerErrorCode(this, msg.arg1));
                break;
        }
    }

    private void handBannerView(Object data) {
        if (data != null && data instanceof BoothList) {
            //广告位
            List<Booth> value = ((BoothList) data).value;
            if (value != null && value.size() != 0) {
                for (int i = 0; i < ((BoothList) data).value.size(); i++) {
                    if ((ResourceType.QUANYAN_CONSULTING_HOME_AD).equals(((BoothList) data).value.get(i).code)) {
                        Booth banner = ((BoothList) data).value.get(i);
                        if (banner != null && banner.showcases != null && banner.showcases.size() > 0) {
                            mImgPager.setBannerList(banner.showcases);
                        }
                    }
                }
            }
        }
    }

    private void handleConsultService(Object data) {
        if (data != null && data instanceof BoothItemsResult) {
            BoothItemsResult result = (BoothItemsResult) data;
            if (result != null && result.boothItemList != null && result.boothItemList.size() != 0) {
                int size = result.boothItemList.size();
                int childCount = mMasterConsultLayout.getChildCount();
                if(childCount > size){
                    int removeCount = childCount - size;
                    mMasterConsultLayout.removeViews(size, removeCount);
                }

                for(int i = 0; i < childCount && i < size; i ++){
                    setData(mMasterConsultLayout.getChildAt(i), result.boothItemList.get(i));
                }

                for(int i = childCount; i < size; i++){
                    View v = mInflater.inflate(R.layout.view_masterconsult_wkt_item, null);
                    v.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    setData(v, result.boothItemList.get(i));
                    mMasterConsultLayout.addView(v);
                }
            }
        }
    }

    private void setData(View v, BoothItem boothItem) {
        TextView title = (TextView) v.findViewById(R.id.tv_master_consult);
        TextView consultNum = (TextView) v.findViewById(R.id.view_master_consult_people_num);
        final NoScrollGridView mNoScrollGridView = (NoScrollGridView) v.findViewById(R.id.nsg_dest_list1);
        if (TextUtils.isEmpty(boothItem.themeTitle)) {
            title.setText("暂时没有标题");
        } else {
            title.setText(boothItem.themeTitle);
        }
        if (boothItem.consultCount >= 0) {
            consultNum.setText("" + boothItem.consultCount);
        } else {
            consultNum.setText("0");
        }
        QuickAdapter mMasterConsultAdapter = new QuickAdapter<ShortItem>(MasterConsultHomeActivity.this, R.layout.view_item_masterhome_consult, new ArrayList<ShortItem>()) {
            @Override
            protected void convert(BaseAdapterHelper helper, ShortItem item) {
                MasterViewHelper.handleMasterAdviceListItem(MasterConsultHomeActivity.this, helper, item);
            }
        };
        mNoScrollGridView.setAdapter(mMasterConsultAdapter);
        mMasterConsultAdapter.replaceAll(boothItem.shortItemList);
    }


    private void fetchData() {
        showLoadingView("");
        mMasterController.doGetMasterConsultHomeData(this);
        mMasterController.doGetMasterConsultHomeCodeData(1, 5, this);
    }
}
