package com.quanyan.yhy.ui.comment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;

import com.quanyan.base.BaseActivity;
import com.quanyan.base.util.StringUtil;
import com.quanyan.base.view.BaseNavView;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.CommentType;
import com.yhy.common.utils.SPUtils;

/**
 * Created with Android Studio.
 * Title:CommentFragmentActivity
 * Description:全部评价
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-5-13
 * Time:13:58
 * Version 1.1.0
 */


public class CommentFragmentActivity extends BaseActivity {

    private FrameLayout mFlContent;
    private long mSearchId;
    private String mOrderType;
    private BaseNavView mBaseNavView;

    public static void gotoCommentFragmentActivity(Context context, long id, String type) {
        Intent intent = new Intent(context, CommentFragmentActivity.class);
        intent.putExtra(SPUtils.EXTRA_ID, id);
        intent.putExtra(SPUtils.EXTRA_TYPE, type);
        context.startActivity(intent);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mFlContent = (FrameLayout) findViewById(R.id.fl_comment_content);
        mSearchId = getIntent().getLongExtra(SPUtils.EXTRA_ID, -1);
        mOrderType = getIntent().getStringExtra(SPUtils.EXTRA_TYPE);
        initTitle();
        setFragment();
    }

    //添加fragment
    private void setFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FullCommentFragment fullCommentFragment = new FullCommentFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(SPUtils.EXTRA_ID, mSearchId);
        bundle.putString(SPUtils.EXTRA_TYPE, mOrderType);
        bundle.putString(SPUtils.EXTRA_SOURCE, mOrderType);
        fullCommentFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.fl_comment_content, fullCommentFragment);
        fragmentTransaction.commit();
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.ac_comment_fa, null);
    }

    @Override
    public View onLoadNavView() {
        mBaseNavView = new BaseNavView(this);
        return mBaseNavView;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }

    private void initTitle() {
        if (!StringUtil.isEmpty(mOrderType)) {
            if (CommentType.HOTEL.equals(mOrderType)) {
                mBaseNavView.setTitleText(R.string.label_hotel_comment);
            } else if (CommentType.SCENIC.equals(mOrderType)) {
                mBaseNavView.setTitleText(R.string.label_scenic_comment);
            } else if (CommentType.LINE.equals(mOrderType)) {
                mBaseNavView.setTitleText(R.string.label_line_comment);
            } else if (CommentType.LOCAL.equals(mOrderType)) {
                mBaseNavView.setTitleText(R.string.label_activity_comment);
            } else if (CommentType.PRODUCT.equals(mOrderType)) {
                mBaseNavView.setTitleText(R.string.label_buy_comment);
            }else if (CommentType.CONSULT.equals(mOrderType)) {
                mBaseNavView.setTitleText(R.string.label_ask_comment);
            }
        }
    }

}
