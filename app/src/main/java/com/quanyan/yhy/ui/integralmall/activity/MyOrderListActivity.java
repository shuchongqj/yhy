package com.quanyan.yhy.ui.integralmall.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.view.BaseNavView;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.PayStatus;
import com.quanyan.yhy.common.UpdateOrderState;
import com.quanyan.yhy.service.controller.OrderController;
import com.quanyan.yhy.ui.base.utils.PopupUtil;
import com.quanyan.yhy.ui.discovery.view.SimpleViewPagerIndicator;
import com.quanyan.yhy.ui.integralmall.integralmallinterface.OrderFilterInterface;
import com.quanyan.yhy.ui.order.entity.FilterCondition;
import com.quanyan.yhy.ui.servicerelease.ExpertOrderListActivity;
import com.quanyan.yhy.ui.tab.homepage.order.MyOrderListFragment;
import com.yhy.common.constants.IntentConstants;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.SPUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created with Android Studio.
 * Title:EatGreatActivity
 * Description:全部订单
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:tianhaibo
 * Date:2016-9-25
 * Time:12:05
 * Version 1.3.0
 */
public class MyOrderListActivity extends BaseActivity implements SimpleViewPagerIndicator.ViewPagerIndicatorClick {
    @ViewInject(R.id.viewpager)
    private ViewPager viewPager;
    @ViewInject(R.id.order_tab_bar)
    private SimpleViewPagerIndicator orderTabbar;
    @ViewInject(R.id.frame_layout_order_list)
    BaseNavView mBaseNavView;
    MyOrderListFragment[] mMyOrderListFragmentArrays;
    private String[] tabStringArray;
    private String ORDER_FILTER_STATE = "down";
    private String LAST_ORDER_STATUS = "";
    private String CURRENT_ORDER_STATUS = "";

    public static void gotoMyOrderAllListActivity(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, MyOrderListActivity.class);
        context.startActivity(intent);
    }

    public void refreshAllOrder() {
        if (mMyOrderListFragmentArrays == null || mMyOrderListFragmentArrays.length == 0)
            return;
        for (MyOrderListFragment fragment : mMyOrderListFragmentArrays) {
            fragment.sendGetOrderListTask(true);
        }
    }

    @Override
    public View onLoadNavView() {
        if (mBaseNavView == null) {
            mBaseNavView = new BaseNavView(this);
        }
        mBaseNavView.setTitleText(getString(R.string.title_expert_buy_order));
        mBaseNavView.setImageFilter(R.mipmap.arrow_order_down);

        return mBaseNavView;
    }

    @Override
    public View onLoadContentView() {
        View view = View.inflate(this, R.layout.activity_myorder_list, null);
        return view;
    }

    public void onEventMainThread(UpdateOrderState mUpdateOrderState) {
        refreshAllOrder();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PopupUtil.destoryOrderFilterPopup();
        EventBus.getDefault().unregister(this);
    }


    @Override
    protected void initView(Bundle savedInstanceState) {
        ViewUtils.inject(this);
        EventBus.getDefault().register(this);
        init();
        mBaseNavView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getPopuSate().equals("down")) {
                    showOrderFilterPopup();
                } else {
                    dismissOrderFilterPopup();
                }

            }
        });
    }

    public void updateArrowState() {
        if (ORDER_FILTER_STATE.equals("down")) {
            mBaseNavView.setImageFilter(R.mipmap.arrow_order_up);
            ORDER_FILTER_STATE = "up";
        } else {
            mBaseNavView.setImageFilter(R.mipmap.arrow_order_down);
            ORDER_FILTER_STATE = "down";
        }
    }

    public void showOrderFilterPopup() {

        PopupUtil.showOrderFilterPopup(MyOrderListActivity.this, getFiterData(), new OrderFilterInterface() {
            @Override
            public void OnFilterClick(int index, String conId, String text) {
                CURRENT_ORDER_STATUS = conId;
                //  mMyOrderListFragmentArrays[currenTabIndex].refreshData(conId);
                mBaseNavView.setTitleText(text.equals("全部") ? "我购买的" : text);
                for (int i = 0; i < mMyOrderListFragmentArrays.length; i++) {
                    mMyOrderListFragmentArrays[i].refreshData(CURRENT_ORDER_STATUS);
                }
            }

            @Override
            public void OnPopupDismiss() {
                updateArrowState();
            }
        }, mBaseNavView);
        mBaseNavView.setImageFilter(R.mipmap.arrow_order_up);
        ORDER_FILTER_STATE = "up";
    }

    public String getPopuSate() {
        return ORDER_FILTER_STATE;
    }

    public void dismissOrderFilterPopup() {
        PopupUtil.dismissOrderFilterPopup();
        mBaseNavView.setImageFilter(R.mipmap.arrow_order_down);
        ORDER_FILTER_STATE = "down";
    }

    @Override
    public boolean isTopCover() {
        return false;
    }
    Integer orderType;
    private boolean initOrderType() {
        if (orderType == null) {
            orderType = getIntent().getIntExtra(OrderController.PARAMS_ORDER_TYPE, 0);
            return true;
        }
        return false;
    }

    private void init() {
        if (initOrderType()) {
            tabStringArray = getResources().getStringArray(R.array.order_my_tab_activity);
            orderTabbar.setTitles(Arrays.asList(tabStringArray));
            orderTabbar.setTabClickListener(this);
        }
        if (mMyOrderListFragmentArrays == null) {
            mMyOrderListFragmentArrays = new MyOrderListFragment[4];
            mMyOrderListFragmentArrays[0] = MyOrderListFragment.newInstance(ValueConstants.KEY_ORDER_STATUS_ALL);
            mMyOrderListFragmentArrays[1] = MyOrderListFragment.newInstance(ValueConstants.KEY_ORDER_STATUS_UNPAY);
            mMyOrderListFragmentArrays[2] = MyOrderListFragment.newInstance(ValueConstants.KEY_ORDER_STATUS_VALID);
            mMyOrderListFragmentArrays[3] = MyOrderListFragment.newInstance(ValueConstants.KEY_ORDER_STATUS_UNCOMMENT);
            viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
                @Override
                public Fragment getItem(int position) {
                    return mMyOrderListFragmentArrays[position];
                }

                @Override
                public int getCount() {
                    return mMyOrderListFragmentArrays.length;
                }
            });
            viewPager.setOnPageChangeListener(MOnPageChangeListener);
            viewPager.setOffscreenPageLimit(5);
        }

    }

    private int currenTabIndex = 0;
    ViewPager.OnPageChangeListener MOnPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            orderTabbar.scroll(position, positionOffset);
        }

        @Override
        public void onPageSelected(int position) {
            orderTabbar.getTabTitleViews().get(currenTabIndex).setSelected(false);
            orderTabbar.getTabTitleViews().get(position).setSelected(true);
            currenTabIndex = position;
            updateOrderStatus();

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }

    };

    @Override
    public void tabClick(int position) {
        viewPager.setCurrentItem(position);
        currenTabIndex = position;
         updateOrderStatus();

    }

    public void updateOrderStatus() {
        mMyOrderListFragmentArrays[currenTabIndex].refreshData(CURRENT_ORDER_STATUS);
       /* if (!CURRENT_ORDER_STATUS.equals(LAST_ORDER_STATUS)) {

            for (int i = 0; i < mMyOrderListFragmentArrays.length; i++) {
                mMyOrderListFragmentArrays[i].refreshData(CURRENT_ORDER_STATUS);
            }
            LAST_ORDER_STATUS = CURRENT_ORDER_STATUS;
        }*/
    }

    public List<FilterCondition> getFiterData() {
        Resources res = getResources();
        String[] filter = res.getStringArray(R.array.order_filter);
        List<FilterCondition> list = new ArrayList();
        for (String item : filter) {
            FilterCondition mfilter = new FilterCondition();
            if (item.contains("|")) {
                mfilter.setConName(item.split("\\|")[0]);
                mfilter.setConId(item.split("\\|")[1]);
                list.add(mfilter);
            }
        }
        return list;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == ExpertOrderListActivity.REQ_CODE_REFRESH_ORDER_LIST) {
                if ((PayStatus.PAYOK).equals(data.getStringExtra(SPUtils.EXTRA_DATA)) || (PayStatus.PAYING).equals(data.getStringExtra(SPUtils.EXTRA_DATA))) {
                     refreshAllOrder();
                }
            } else if (requestCode == IntentConstants.REQUEST_CODE_COMMENT) {
                refreshAllOrder();
            }
        }
    }
}
