package com.quanyan.yhy.ui.servicerelease;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.quanyan.base.BaseActivity;
import com.quanyan.base.util.StringUtil;
import com.quanyan.base.view.BaseNavView;
import com.quanyan.base.view.customview.tabscrollindicator.SlidingTabLayout;
import com.quanyan.yhy.R;
import com.quanyan.yhy.ui.adapter.DetailViewPagerAdapter;
import com.yhy.common.utils.SPUtils;

import java.util.ArrayList;

/**
 * Created with Android Studio.
 * Title:***Acitivity
 * Description:  我发布的服务
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:王东旭
 * Date:2016/6/1
 * Time:16:46
 * Version 2.0
 */
public class ManageServiceInfoAcitvity extends BaseActivity implements SlidingTabLayout.TabClick {

    /**
     * 已发布的列表类型
     */
    public static final int STATE_MASTER_SERVICEMANAGE_PUBLISH_OVER = 2;
    /**
     * 发布中的列表类型
     */
    public static final int STATE_MASTER_SERVICEMANAGE_PUBLISH_STANDBY = 3;

    private SlidingTabLayout mSlidingTabLayout;
    private ViewPager mViewPager;

    private DetailViewPagerAdapter mDetailViewPagerAdapter;

    private ArrayList<Fragment> mBaseListViewFragments;

    private String[] mTabStringArray;
    private int mIndex;

    public static void gotoManageServiceInfoActivity(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, ManageServiceInfoAcitvity.class);
        context.startActivity(intent);
    }

    public static void gotoManageServiceInfoActivity(Context context, int index) {
        Intent intent = new Intent();
        intent.setClass(context, ManageServiceInfoAcitvity.class);
        intent.putExtra(SPUtils.EXTRA_DATA, index);
        context.startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for(int i = 0; i < mBaseListViewFragments.size(); i++){
            mBaseListViewFragments.get(i).onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mIndex = getIntent().getIntExtra(SPUtils.EXTRA_DATA, -1);

        mTabStringArray = getResources().getStringArray(R.array.service_state);
        mBaseListViewFragments = new ArrayList<>();

        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.ac_manage_service_info_sliding_tab);
        mViewPager = (ViewPager) findViewById(R.id.ac_manage_service_info_view_pager);

        mBaseListViewFragments.add(ManageInfoFragment.newInstance(STATE_MASTER_SERVICEMANAGE_PUBLISH_OVER));
        mBaseListViewFragments.add(ManageInfoFragment.newInstance(STATE_MASTER_SERVICEMANAGE_PUBLISH_STANDBY));
        mDetailViewPagerAdapter = new DetailViewPagerAdapter(getSupportFragmentManager(), mBaseListViewFragments, (ArrayList<String>) StringUtil.stringsToList(mTabStringArray));
        mViewPager.setAdapter(mDetailViewPagerAdapter);

        //tab属性设置
        mSlidingTabLayout.setDistributeEvenly(true);
        mSlidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.ac_title_bg_color));
        mSlidingTabLayout.setTabViewTextSizeSp(15);
        mSlidingTabLayout.setOnTabClickListener(this);
        mSlidingTabLayout.setViewPager(mViewPager);

        if (mIndex != -1) {
            mViewPager.setCurrentItem(mIndex);
        }
    }

    @Override
    public View onLoadContentView() {
        View view = View.inflate(this, R.layout.activity_manage_service_list, null);
        return view;
    }

    @Override
    public View onLoadNavView() {
        BaseNavView mBaseNavView = new BaseNavView(this);
        mBaseNavView.setTitleText(R.string.label_manage_service);
        return mBaseNavView;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }

    @Override
    public void onTabClick(View view, int position) {

    }
}
