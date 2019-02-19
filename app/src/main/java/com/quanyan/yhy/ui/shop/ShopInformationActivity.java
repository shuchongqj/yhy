package com.quanyan.yhy.ui.shop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.util.LocalUtils;
import com.quanyan.base.util.StringUtil;
import com.quanyan.base.view.BaseNavView;
import com.quanyan.yhy.R;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.yhy.common.beans.net.model.master.Merchant;
import com.yhy.common.utils.CommonUtil;
import com.yhy.common.utils.SPUtils;
import com.yhy.imageloader.ImageLoadManager;
import com.yhy.service.IUserService;

/**
 * Created with Android Studio.
 * Title:ShopInformationActivity
 * Description:店铺简介
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-8-20
 * Time:15:15
 * Version 1.1.0
 */

public class ShopInformationActivity extends BaseActivity implements View.OnClickListener {

    private BaseNavView mBaseNavView;
    private TextView mImButton;//联系客服
    private ImageView mIvShopHeadIcon;//店铺头像
    private TextView mTvShopName;//店铺名称
    private RelativeLayout mRLShopIdent;//店铺名片
    private RelativeLayout mRLShopSimpleIntroduce;//店铺简介
    private RelativeLayout mRLShopPhoneContain;//
    private RelativeLayout mRLShopPicture;//工商执照
    private TextView mTVPhoneNumber;//电话
    private Merchant mSellerInfo;
    private ImageView mShopBackImg;//店铺背景图片

    @Autowired
    IUserService userService;

    public IUserService getUserService() {
        return userService;
    }

    public static void gotoShopInformationActivity(Context context, Merchant sellerInfo) {
        Intent intent = new Intent(context, ShopInformationActivity.class);
        intent.putExtra(SPUtils.EXTRA_DATA, sellerInfo);
        context.startActivity(intent);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mSellerInfo = (Merchant) getIntent().getSerializableExtra(SPUtils.EXTRA_DATA);
        findId();
        initData();
        initClick();
    }

    private void initData() {
        if(mSellerInfo != null){
            if (!StringUtil.isEmpty(mSellerInfo.name)) {
                mTvShopName.setText(mSellerInfo.name);
            }
            if (!StringUtil.isEmpty(mSellerInfo.backPic)) {
//                BaseImgView.loadimg(mShopBackImg,
//                        mSellerInfo.backPic,
//                        R.mipmap.icon_default_750_360,
//                        R.mipmap.icon_default_750_360,
//                        R.mipmap.icon_default_750_360,
//                        ImageScaleType.EXACTLY,
//                        -1,
//                        -1,
//                        -1);

                ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(mSellerInfo.backPic), R.mipmap.icon_default_750_360, mShopBackImg);
            } else {
                mShopBackImg.setImageResource(R.mipmap.icon_default_750_360);
            }
            if (!StringUtil.isEmpty(mSellerInfo.icon)) {
//                BaseImgView.loadimg(mIvShopHeadIcon,
//                        mSellerInfo.icon,
//                        R.mipmap.ic_shop_default_logo,
//                        R.mipmap.ic_shop_default_logo,
//                        R.mipmap.ic_shop_default_logo,
//                        ImageScaleType.EXACTLY,
//                        -1,
//                        -1,
//                        180);

                ImageLoadManager.loadCircleImage(CommonUtil.getImageFullUrl(mSellerInfo.icon), R.mipmap.ic_shop_default_logo, mIvShopHeadIcon);

            } else {
                mIvShopHeadIcon.setImageResource(R.mipmap.ic_shop_default_logo);
            }

            if(!StringUtil.isEmpty(mSellerInfo.serviceTel)){
                mRLShopPhoneContain.setVisibility(View.VISIBLE);
                mTVPhoneNumber.setText(mSellerInfo.serviceTel);
            }else {
                mRLShopPhoneContain.setVisibility(View.GONE);
            }
        }
    }

    private void initClick() {
        mImButton.setOnClickListener(this);
        mRLShopIdent.setOnClickListener(this);
        mRLShopSimpleIntroduce.setOnClickListener(this);
        mRLShopPicture.setOnClickListener(this);
        mRLShopPhoneContain.setOnClickListener(this);
    }

    private void findId() {
        mImButton = (TextView) findViewById(R.id.tv_start_im);
        mIvShopHeadIcon = (ImageView) findViewById(R.id.iv_shop_head_icon);
        mShopBackImg = (ImageView) findViewById(R.id.iv_shop_backgroud);
        mTvShopName = (TextView) findViewById(R.id.tv_shop_name);
        mRLShopIdent = (RelativeLayout) findViewById(R.id.rl_shop_ident);
        mRLShopSimpleIntroduce = (RelativeLayout) findViewById(R.id.rl_shop_simple_introduce);
        mRLShopPhoneContain = (RelativeLayout) findViewById(R.id.rl_shop_phone_contain);
        mTVPhoneNumber = (TextView) findViewById(R.id.tv_phone_number);
        mRLShopPicture = (RelativeLayout) findViewById(R.id.rl_shop_picture);
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.ac_shop_introduce, null);
    }

    @Override
    public View onLoadNavView() {
        mBaseNavView = new BaseNavView(this);
        mBaseNavView.setTitleText(R.string.shop_introduce);
        return mBaseNavView;
    }

    @Override
    protected void onResume() {
        loadSellerInfo();
        super.onResume();
    }

    /**
     * 客服电话按钮显示与隐藏
     */
    private void loadSellerInfo(){
        if(mSellerInfo == null){
            mImButton.setVisibility(View.INVISIBLE);
            return ;
        }
        if(mSellerInfo.sellerId == userService.getLoginUserId()){
            mImButton.setVisibility(View.INVISIBLE);
        }else{
            mImButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean isTopCover() {
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_start_im://联系客服
                if (!getUserService().isLogin()) {
                    NavUtils.gotoLoginActivity(ShopInformationActivity.this);
                } else {
                    NavUtils.gotoMessageActivity(ShopInformationActivity.this, (int)mSellerInfo.sellerId);
                }
                break;
            case R.id.rl_shop_ident://店铺名片
                //TODO 艹
//                NavUtils.gotoShopSelfCardActivity(ShopInformationActivity.this, mSellerInfo);
                break;
            case R.id.rl_shop_simple_introduce://店铺简介
                NavUtils.gotoShopSimpleIntroduceActivity(ShopInformationActivity.this, mSellerInfo.sellerId);
                break;
            case R.id.rl_shop_picture://工商执照
                NavUtils.gotoShopLicenseActivity(ShopInformationActivity.this, mSellerInfo.sellerId, mSellerInfo.name);
                break;
            case R.id.rl_shop_phone_contain://客服电话
                if (!StringUtil.isEmpty(mSellerInfo.serviceTel)) {
                    LocalUtils.call(ShopInformationActivity.this, mSellerInfo.serviceTel);
                }
                break;
        }
    }
}
