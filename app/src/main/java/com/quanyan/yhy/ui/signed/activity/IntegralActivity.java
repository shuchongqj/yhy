package com.quanyan.yhy.ui.signed.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.view.BaseNavView;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.AnalyDataValue;
import com.quanyan.yhy.service.controller.OrderController;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.base.utils.TCEventHelper;
import com.quanyan.yhy.ui.discovery.view.SimpleViewPagerIndicator;
import com.quanyan.yhy.ui.integralmall.integralmallcontroller.IntegralmallController;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.SPUtils;
import com.yhy.service.IUserService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created with Android Studio.
 * Title:EatGreatActivity
 * Description:积分
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:wjm
 * Date:2016-6-24
 * Time:12:05
 * Version 1.3.0
 */
public class IntegralActivity extends BaseActivity implements SimpleViewPagerIndicator.ViewPagerIndicatorClick, View.OnClickListener {
    IntegralmallController integralmallController;
    @ViewInject(R.id.viewpager)
    private ViewPager viewPager;
    @ViewInject(R.id.order_tab_bar)
    private SimpleViewPagerIndicator orderTabbar;
    @ViewInject(R.id.ll_integral_mall)
    private LinearLayout mIntegralMall;

    private String[] tabStringArray;
    private int currenTabIndex = 0;

    @ViewInject(R.id.tv_integral_num)
    private TextView tvIntegralNum;

    @Autowired
    IUserService userService;

    public IUserService getUserService() {
        return userService;
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.activity_integral_list, null);
    }

    private BaseNavView mBaseNavView;

    @Override
    public View onLoadNavView() {
        mBaseNavView = new BaseNavView(this);
        mBaseNavView.setTitleText(getString(R.string.label_signed_title));
        return mBaseNavView;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        integralmallController = new IntegralmallController(this, mHandler);

        ViewUtils.inject(this);
        init();
    }

    private void initTabStringArray() {
        int resId = R.array.integral_tab_travel;
        tabStringArray = getResources().getStringArray(resId);
    }


    /**
     * 初始化数据
     */
    private void init() {
        showLoadingView(getString(R.string.loading_text));
        mIntegralMall.setOnClickListener(this);
        initTabStringArray();
        orderTabbar.setTitles(Arrays.asList(tabStringArray));
        orderTabbar.setTabClickListener(this);

        List<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(new SignedActivity());
        fragments.add(new SignedDetailsActivity());
        FragAdapter adapter = new FragAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(MOnPageChangeListener);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setCurrentItem(getIntent().getIntExtra(OrderController.PARAMS_ORDER_CHECK_TYPE, OrderController.CHILD_TYPE_ALL));
    }
    @Override
    public void onResume() {
        super.onResume();
        if (getUserService().isLogin()) {
            integralmallController. doTotalPointQuery(IntegralActivity.this);
        }else{
            tvIntegralNum.setText("——");
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_integral_mall://跳转到积分商城
                TCEventHelper.onEvent(IntegralActivity.this, AnalyDataValue.INTEGRAL_EXCHANGE);
                NavUtils.gotoIntegralmallHomeActivity(this);
                break;
        }
    }

    public class FragAdapter extends FragmentPagerAdapter {
        private List<Fragment> ViewPager;
        public FragAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            ViewPager = fragments;
        }

        @Override
        public Fragment getItem(int arg0) {
            return ViewPager.get(arg0);
        }

        @Override
        public int getCount() {
            return ViewPager.size();
        }

    }


    @Override
    public void tabClick(int position) {
        TCEventHelper.onEvent(IntegralActivity.this, AnalyDataValue.INTEGRAL_TASK_TAB_CHANGE, position + 1 + "");
        viewPager.setCurrentItem(position);
    }

    /**
     * viewpager 变化监听
     */
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
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }

    };


    @Override
    public void handleMessage(Message msg) {
        hideLoadingView();
        switch (msg.what) {
            case ValueConstants.MSG_INTEGRALMALL_TOTALPOINT_OK://总积分查询
                if (msg.obj != null) {
                    Long num = (Long) msg.obj;
                    tvIntegralNum.setText(num+"");
                    SPUtils.setScore(this,num);
                }
                break;
            case ValueConstants.MSG_INTEGRALMALL_TOTALPOINT_KO:
                tvIntegralNum.setText("——");
                break;
        }
    }


}
