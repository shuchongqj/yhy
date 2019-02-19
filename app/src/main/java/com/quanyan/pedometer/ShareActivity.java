package com.quanyan.pedometer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.quanyan.base.BaseActivity;
import com.quanyan.yhy.R;
import com.yhy.common.beans.net.model.shop.MerchantItem;
import com.yhy.common.beans.net.model.user.UserInfo;
import com.yhy.common.beans.user.User;
import com.yhy.common.utils.SPUtils;

/**
 * Created with Android Studio.
 * Title:ShareActivity
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:徐学东
 * Date:16/7/7
 * Time:下午7:12
 * Version 1.1.0
 */
public class ShareActivity extends BaseActivity{
    public final static int SHARE_STEPS = 1;
    public final static int INVITE_FRIENDS = 2;
    /**
     * 达人资讯商品分享
     */
    public final static int SHARE_TYPE_MASTER_CONSULT = 0x08;
    /**
     * 积分商品分享
     */
    public final static int SHARE_TYPE_POINT_PRODUCT = 0x10;
    //个人信息分享二维码
    public final static int SHARE_TYPE_MINE_INFO = 0x12;

    private Fragment mShareFragment;
    private Fragment mInviteFragment;

    private Fragment mProductShareFragment;
    private Fragment mUserInfoFragment;
    //分享类型
    private int mType = SHARE_STEPS;
    private int mShareWay = -1;

    private MerchantItem mMerchantItem;
    private User mUserInfo;

    /**
     * 分享页面
     *
     * @param context
     */
    public static void gotoShareActivity(Context context,int type,int shareWay) {
        Intent intent = new Intent(context, ShareActivity.class);
        intent.putExtra(SPUtils.EXTRA_TYPE,type);
        intent.putExtra(SPUtils.EXTRA_SHARE_WAY,shareWay);
        context.startActivity(intent);
    }

    public static void gotoShareActivity(Context context, int type, int shareWay, MerchantItem merchantItem) {
        Intent intent = new Intent(context, ShareActivity.class);
        intent.putExtra(SPUtils.EXTRA_TYPE,type);
        intent.putExtra(SPUtils.EXTRA_SHARE_WAY,shareWay);
        intent.putExtra(SPUtils.EXTRA_DATA,merchantItem);
        context.startActivity(intent);
    }

    public static void gotoShareActivity(Context context, int type, int shareWay, User userInfo) {
        Intent intent = new Intent(context, ShareActivity.class);
        intent.putExtra(SPUtils.EXTRA_TYPE,type);
        intent.putExtra(SPUtils.EXTRA_SHARE_WAY,shareWay);
        intent.putExtra(SPUtils.EXTRA_USER_INFO, userInfo);
        context.startActivity(intent);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setTitleBarBackground(Color.TRANSPARENT);
        mType = getIntent().getIntExtra(SPUtils.EXTRA_TYPE,-1);
        mShareWay = getIntent().getIntExtra(SPUtils.EXTRA_SHARE_WAY,-1);
        mMerchantItem = (MerchantItem) getIntent().getSerializableExtra(SPUtils.EXTRA_DATA);
        mUserInfo = (User) getIntent().getSerializableExtra(SPUtils.EXTRA_USER_INFO);
        FragmentManager fm = getSupportFragmentManager();
        switch (mType){
            case SHARE_STEPS:
                mShareFragment = fm.findFragmentById(R.id.container);
                if (mShareFragment == null) {
                    mShareFragment = new ShareFragment();
                }
                fm.beginTransaction().add(R.id.container, mShareFragment).commit();
                break;
            case INVITE_FRIENDS:
                mInviteFragment = fm.findFragmentById(R.id.container);
                if (mInviteFragment == null) {
                    mInviteFragment = new InviteFriendsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt(SPUtils.EXTRA_SHARE_WAY,mShareWay);
                    mInviteFragment.setArguments(bundle);
                }
                fm.beginTransaction().add(R.id.container, mInviteFragment).commit();
                break;
            case SHARE_TYPE_MASTER_CONSULT:{
                // 咨询营养师
                mProductShareFragment = fm.findFragmentById(R.id.container);
                if(mProductShareFragment == null){
                    mProductShareFragment = new ProductShareMasterFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt(SPUtils.EXTRA_TYPE, SHARE_TYPE_MASTER_CONSULT);
                    bundle.putSerializable(SPUtils.EXTRA_DATA,mMerchantItem);
                    mProductShareFragment.setArguments(bundle);
                }
                fm.beginTransaction().add(R.id.container, mProductShareFragment).commit();
                break;
            }
            case SHARE_TYPE_POINT_PRODUCT: {
                mProductShareFragment = fm.findFragmentById(R.id.container);
                if (mProductShareFragment == null) {
                    mProductShareFragment = new ProductShareFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt(SPUtils.EXTRA_TYPE, SHARE_TYPE_POINT_PRODUCT);
                    bundle.putSerializable(SPUtils.EXTRA_DATA,mMerchantItem);
                    mProductShareFragment.setArguments(bundle);
                }
                fm.beginTransaction().add(R.id.container, mProductShareFragment).commit();
                break;
            }
            case SHARE_TYPE_MINE_INFO:
                mUserInfoFragment = fm.findFragmentById(R.id.container);
                if(mUserInfoFragment == null){
                    mUserInfoFragment = new UserInfoShareFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt(SPUtils.EXTRA_TYPE, SHARE_TYPE_MINE_INFO);
                    bundle.putSerializable(SPUtils.EXTRA_USER_INFO, mUserInfo);
                    mUserInfoFragment.setArguments(bundle);
                }
                fm.beginTransaction().add(R.id.container, mUserInfoFragment).commit();
                break;
        }
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.activity_pedo, null);
    }


    @Override
    public View onLoadNavView() {
        return null;
    }

    @Override
    public boolean isTopCover() {
        return true;
    }
}
