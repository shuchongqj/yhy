package com.quanyan.yhy.ui.servicerelease;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.view.BaseNavView;
import com.quanyan.yhy.R;
import com.quanyan.yhy.service.controller.OrderController;
import com.quanyan.yhy.ui.discovery.view.SimpleViewPagerIndicator;
import com.yhy.common.utils.SPUtils;

import java.util.Arrays;

/**
 * Created with Android Studio.
 * Title:***Acitivity
 * Description:
 * <li>售出的订单列表</li>
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:王东旭
 * Date:2016/6/1
 * Time:16:46
 * Version 2.0
 */
public class ExpertOrderListActivity extends BaseActivity implements SimpleViewPagerIndicator.ViewPagerIndicatorClick {

    @ViewInject(R.id.viewpager)
    private ViewPager viewPager;

    @ViewInject(R.id.order_tab_bar)
    private SimpleViewPagerIndicator orderTabbar;
    @ViewInject(R.id.frame_layout_order_list)
    FrameLayout mFramelayoutorderlist;

    BaseNavView mBaseNavView;
    ExpertOrderListFragment[] mExpertOrderListFragmentArrays;

    private String[] tabStringArray;

    // type = 6 购买的订单 只需要一页 可能原因达人售出的订单多吧
    //type = 0 1 2 3 4 5 达人出售的订单
    public static void gotoExpertOrderListActivity(Context context, int type) {
        Intent intent = new Intent();
        intent.setClass(context, ExpertOrderListActivity.class);
        intent.putExtra(SPUtils.EXTRA_TYPE, type);
        context.startActivity(intent);
    }

    @Override
    public View onLoadNavView() {
        if (mBaseNavView == null) {
            mBaseNavView = new BaseNavView(this);
        }

        mBaseNavView.setTitleText(R.string.title_expert_sell_order);

        return mBaseNavView;
    }

    @Override
    public View onLoadContentView() {
        View view = View.inflate(this, R.layout.activity_expert_order_list, null);
        return view;
    }

    int type = 0;

    public boolean checkPageType() {
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            type = bundle.getInt(SPUtils.EXTRA_TYPE);
        }
        if (type == 6) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ViewUtils.inject(this);
        init();
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
            tabStringArray = getResources().getStringArray(R.array.order_tab_sell_activity);
            orderTabbar.setTitles(Arrays.asList(tabStringArray));
            orderTabbar.setTabClickListener(this);
        }

        if (mExpertOrderListFragmentArrays == null) {
            mExpertOrderListFragmentArrays = new ExpertOrderListFragment[5];
            for (int i = 0; i < 5; i++) {
                mExpertOrderListFragmentArrays[i] = new ExpertOrderListFragment();
                Bundle mBundle = new Bundle();
                mBundle.putInt(OrderController.PARAMS_ORDER_TYPE, i);
                mExpertOrderListFragmentArrays[i].setArguments(mBundle);
            }
            viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
                @Override
                public Fragment getItem(int position) {
                    return mExpertOrderListFragmentArrays[position];
                }

                @Override
                public int getCount() {
                    return mExpertOrderListFragmentArrays.length;
                }
            });
            viewPager.setOnPageChangeListener(MOnPageChangeListener);
            viewPager.setOffscreenPageLimit(5);
        }
      /*  viewPager.setCurrentItem(getIntent().getIntExtra(OrderController.PARAMS_ORDER_CHECK_TYPE, OrderController.CHILD_TYPE_ALL));*/
    }

    private int currenTabIndex = 0;
    ViewPager.OnPageChangeListener MOnPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            orderTabbar.scroll(position, positionOffset);
        }

        @Override
        public void onPageSelected(int position) {
            //通知顶部tabbar变化
            orderTabbar.getTabTitleViews().get(currenTabIndex).setSelected(false);
            orderTabbar.getTabTitleViews().get(position).setSelected(true);
            currenTabIndex = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }

    };

    @Override
    public void tabClick(int position) {
        viewPager.setCurrentItem(position);
    }
    //刷新订单列表
    public final static int REQ_CODE_REFRESH_ORDER_LIST = 0x12;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQ_CODE_REFRESH_ORDER_LIST && resultCode == Activity.RESULT_OK){
            for (int i = 0; i < 5; i++) {
                mExpertOrderListFragmentArrays[i].reloadData();
            }
        }

    }
}
