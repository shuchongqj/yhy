package com.quanyan.yhy.ui.servicerelease;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.quanyan.base.BaseActivity;
import com.quanyan.yhy.R;
import com.quanyan.yhy.ui.order.NumberChooseView;
import com.quanyan.yhy.ui.order.OrderTopView;
import com.quanyan.yhy.ui.servicerelease.view.TextAndEditView;
import com.quanyan.yhy.ui.servicerelease.view.TextAndTextView;
import com.quanyan.yhy.ui.views.AppSettingItem;

/**
 * Created with Android Studio.
 * Title:ReleaseGuideServiceActivity
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-6-29
 * Time:11:06
 * Version 1.1.0
 */

public class ReleaseGuideServiceActivity extends BaseActivity implements View.OnClickListener {
    private OrderTopView mOrderTopView;

    @ViewInject(R.id.iv_release_header)
    private ImageView mImageViewHeader;//头图片

    @ViewInject(R.id.tat_title)
    private TextAndTextView mReleaseTitle;//类目

    @ViewInject(R.id.tae_name)
    private TextAndEditView mReleaseName;//名称

    @ViewInject(R.id.asi_destination)
    private AppSettingItem mDestination;//目的地

    @ViewInject(R.id.asi_time)
    private AppSettingItem mTime;//时间

    @ViewInject(R.id.order_item_title_tv)
    private TextView mPriceTitle;//参加人数

    @ViewInject(R.id.nc_num_select)
    private NumberChooseView mNumSelect;//人数选择

    @ViewInject(R.id.tv_price_config)
    private TextView mPriceConfig;//价格信息

    @ViewInject(R.id.asi_picture_text)
    private AppSettingItem mPictureAndText;//图文详情

    @ViewInject(R.id.asi_rule)
    private AppSettingItem mRule;//费用说明

    @ViewInject(R.id.tv_release)
    private TextView mReleaseButton;//发布按钮



    @Override
    protected void initView(Bundle savedInstanceState) {
        mOrderTopView.setRightViewVisible(getString(R.string.item_release_drafts));
        mOrderTopView.setOrderTopTitle(getString(R.string.label_release_service));

        initDesc();

        initClick();
    }

    private void initDesc() {
        mReleaseTitle.setTitle("商品类目");
        mReleaseName.setTitle("商品名称");

    }

    private void initClick() {

        mReleaseTitle.setOnClickListener(this);
        mDestination.setOnClickListener(this);
        mTime.setOnClickListener(this);
        mPictureAndText.setOnClickListener(this);
        mRule.setOnClickListener(this);

        mReleaseButton.setOnClickListener(this);

        mOrderTopView.setBackClickListener(new OrderTopView.BackClickListener() {
            @Override
            public void clickBack() {
                finish();
            }
        });

        //草稿箱
        mOrderTopView.setRightClickListener(new OrderTopView.RightClickListener() {
            @Override
            public void rightClick() {

            }
        });
    }

    @Override
    public View onLoadContentView() {
        View view = View.inflate(this, R.layout.ac_release_guide, null);
        ViewUtils.inject(this, view);
        return view;
    }

    @Override
    public View onLoadNavView() {
        mOrderTopView = new OrderTopView(this);
        return mOrderTopView;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tat_title:
                break;
            case R.id.asi_destination:
                break;
            case R.id.asi_time:
                break;
            case R.id.asi_picture_text:
                break;
            case R.id.asi_rule:
                break;
            case R.id.tv_release://发布按钮
                break;

        }
    }
}
