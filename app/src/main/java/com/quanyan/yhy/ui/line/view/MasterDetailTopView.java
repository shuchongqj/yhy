package com.quanyan.yhy.ui.line.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quanyan.base.view.customview.imgpager.ImgPagerView;
import com.quanyan.yhy.R;
import com.quanyan.yhy.view.CommodityBottomView;
import com.yhy.common.beans.net.model.item.CityActivityDetail;

/**
 * Created with Android Studio.
 * Title:AboutAndFeedController
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:zhaoxiaopo
 * Date:16/3/21
 * Time:14:52
 * Version 1.0
 */
public class MasterDetailTopView extends LinearLayout {

    private CommodityBottomView.ExchangeData mExchangeData;

    private ImgPagerView mImgPagerView;//轮播图
    private TextView mTitle;//标题

    private ImageView mMasterImg;//达人头像
    private TextView mMasterName;//达人姓名
    private ImageView mMasterSex;//达人性别
    private TextView mMasterAddress;//达人地址
    private TextView mMasterMark;//达人评分
    private TextView mMasterCommentNum;//达人评论数
    private TextView mMasterServiceNum;//达人服务数

    public MasterDetailTopView(Context context) {
        super(context);
        init(context, null);
    }

    public MasterDetailTopView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MasterDetailTopView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MasterDetailTopView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attributeSet){
        View.inflate(context, R.layout.layout_master_detail_top_view, this);
        mImgPagerView = (ImgPagerView) findViewById(R.id.master_detail_top_pager_view);
        mTitle = (TextView) findViewById(R.id.master_detail_top_title);
        mMasterImg = (ImageView) findViewById(R.id.master_detail_top_user_img);
        mMasterName = (TextView) findViewById(R.id.master_detail_top_user_name);
        mMasterSex = (ImageView) findViewById(R.id.master_detail_top_use_sex);
        mMasterAddress = (TextView) findViewById(R.id.master_detail_top_user_address);
        mMasterMark = (TextView) findViewById(R.id.master_detail_top_mark);
        mMasterCommentNum = (TextView) findViewById(R.id.master_detail_top_comment);
        mMasterServiceNum = (TextView) findViewById(R.id.master_detail_top_service);

        findViewById(R.id.master_detail_top_user_layout).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 16/3/21 点击达人  跳转达人主页 
            }
        });
    }

    /**
     * 设置View数据源
     * @param cityActivityDetail
     */
    public void bindViewData(CityActivityDetail cityActivityDetail){

    }

    public void setExchangeData(CommodityBottomView.ExchangeData exchangeData) {
        mExchangeData = exchangeData;
    }
}
