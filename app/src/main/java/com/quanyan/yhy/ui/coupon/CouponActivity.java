package com.quanyan.yhy.ui.coupon;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.view.BaseNavView;
import com.quanyan.yhy.R;
import com.quanyan.yhy.ui.coupon.fragment.CouponInfoListFragment;
import com.quanyan.yhy.ui.discovery.view.SimpleViewPagerIndicator;
import com.ymanalyseslibrary.log.LogUtil;

import java.util.Arrays;


/**
 * Created with Android Studio.
 * Title:***Acitivity
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:王东旭
 * Date:2016/6/28
 * Time:16:46
 * Version 2.0
 */
public class CouponActivity extends BaseActivity implements View.OnClickListener, SimpleViewPagerIndicator.ViewPagerIndicatorClick{
    @ViewInject(R.id.viewpager)
    private ViewPager mViewPager;
    /**
     * 顶部自定义tabbar负责切换viewpager
     */
    @ViewInject(R.id.coupon_tab_bar)
    private SimpleViewPagerIndicator mCouponTabbar;
    @ViewInject(R.id.frame_layout_merchant)
    private FrameLayout mFrameLayoutMerchant;

    private int mTabNumber = 5;

    private CouponInfoListFragment[] mCouponInfoListFragment;

    private String[] mTabStringArray;
    private int mCurrenTabIndex = 0;

    private  boolean mIsRightIn;


    private CouponInfoListFragment mCouponInfoFragment;
    /*
    * param type 1我的优惠券 2商品优惠券
    * */
    public static void gotoCouponAcitvity(Context context, long itemVOid ,int type){
        Intent intent=new Intent();
        intent.setClass(context,CouponActivity.class);
        intent.putExtra("itemVOid",itemVOid);
        intent.putExtra("mIsRightIn",true);
        intent.putExtra("type",type);
        context.startActivity(intent);
    }
    long itemVOid;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);
      
        if(checkFromPageType() == 1){
            initData();
        }else if(checkFromPageType() == 2){
            itemVOid=getIntent().getExtras().getLong("itemVOid");
            initMerchantCouponData();
        }
        addListener();
    }

    private void addListener() {
        LinearLayout mBaseNavViewLeftLayout= (LinearLayout) mNavView.findViewById(R.id.base_nav_view_left_layout);
        mBaseNavViewLeftLayout.setOnClickListener(this);
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.activity_coupon_info_list, null);
    }
    BaseNavView mNavView;
    Bundle bundle;
    private Bundle getBundle(){
        bundle= getIntent().getExtras();
        if(bundle!=null){
            return bundle;
        }else {
            exceptionExit();
        }
        return null;
    }
    private boolean checkIsRightIn(){
        mIsRightIn= getBundle().getBoolean("mIsRightIn");
        if(mIsRightIn){
            return mIsRightIn;
        }else {
            exceptionExit();
        }
        return mIsRightIn;
    }
    private int checkFromPageType(){
        int type=getBundle().getInt("type");
        return type;
    }


    private void initMerchantCouponData(){
        mCouponTabbar.setVisibility(View.GONE);
        mViewPager.setVisibility(View.GONE);
        mFrameLayoutMerchant.setVisibility(View.VISIBLE);
        mCouponInfoFragment=new CouponInfoListFragment();
        Bundle mBundle=new Bundle();
        mBundle.putString("status",getStatus(3));
        mBundle.putLong("itemVOid",itemVOid);

        mCouponInfoFragment.setArguments(mBundle);
        getSupportFragmentManager().beginTransaction().add(R.id.frame_layout_merchant,mCouponInfoFragment).commit();
    }
    private void initData() {//我的代金券
        initTabStringArray();
        mCouponTabbar.setTitles(Arrays.asList(mTabStringArray));
        mCouponTabbar.setTabClickListener(this);
        if(mCouponInfoListFragment==null){
            mCouponInfoListFragment=new CouponInfoListFragment[3];

            for (int i = 0;i < 3;i++){
                mCouponInfoListFragment[i] = new CouponInfoListFragment();
                Bundle mBundle=new Bundle();
                mBundle.putString("status",getStatus(i));
                mCouponInfoListFragment[i].setArguments(mBundle);
            }
            mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
                @Override
                public Fragment getItem(int position) {
                    return mCouponInfoListFragment[position];
                }

                @Override
                public int getCount() {
                    return mCouponInfoListFragment.length;
                }
            });
            mViewPager.setOnPageChangeListener(mOnPageChangeListener);
            mViewPager.setOffscreenPageLimit(3);
        }
        mViewPager.setCurrentItem(0);
    }
    public String getStatus(int i){
        String status="";
        if(i==0){
            status="ACTIVE";
        }else if(i==1){
            status="USED";
        }else if(i==2){
            status="OVER_DUE";
        }else{
            status="SELL";
        }
        return status;
    }
    /**
     * viewpager 变化监听
     */
    ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            mCouponTabbar.scroll(position, positionOffset);
        }

        @Override
        public void onPageSelected(int position) {
            //通知顶部tabbar变化
            mCouponTabbar.getTabTitleViews().get(mCurrenTabIndex).setSelected(false);
            mCouponTabbar.getTabTitleViews().get(position).setSelected(true);
            mCurrenTabIndex = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }

    };
    public void initTabStringArray(){
        int resId = -1;
        resId = R.array.coupon_tab_activity;
        mTabStringArray= getResources().getStringArray(resId);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.base_nav_view_left_layout:

                finish();
                break;
        }
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        checkIsRightIn();

    }


    @Override
    public View onLoadNavView() {
        mNavView = new BaseNavView(this);
        if(checkFromPageType() == 1){
            //TODO 我的优惠券
            mNavView.setTitleText(getString(R.string.label_title_my_coupon));
        }else if(checkFromPageType() == 2){
            mNavView.setTitleText(getString(R.string.label_title_coupon));
        }
        return mNavView;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
    }
    public void exceptionExit(){
        LogUtil.e("coupon","exceptionExitMethod");
        finish();
    }

    @Override
    public void tabClick(int position) {
        mViewPager.setCurrentItem(position);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
