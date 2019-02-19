package com.quanyan.yhy.ui.master.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.quanyan.base.BaseActivity;
import com.quanyan.base.util.StringUtil;
import com.quanyan.base.view.BaseNavView;
import com.quanyan.yhy.R;
import com.quanyan.yhy.view.SearchEditText;
import com.yhy.common.utils.SPUtils;

/**
 * Created with Android Studio.
 * Title:ShopHomePageActivity
 * Description:达人搜索
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:徐学东
 * Date:16/3/8
 * Time:下午1:18
 * Version 1.0
 */
public class SearchActivity extends BaseActivity implements SearchInterface{

    private SearchFragment mSearchFragment;
    private BaseNavView mBaseNavView;

    private SearchEditText mSearchEditText;

    /**
     * 搜索
     *
     * @param context
     */
    public static void gotoSearchActivity(Activity context, String itemType, String source , String title, int reqCode) {
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtra(SPUtils.EXTRA_TYPE,itemType);
        if(!StringUtil.isEmpty(source)){
            intent.putExtra(SPUtils.EXTRA_SOURCE,source);
        }

        if(!StringUtil.isEmpty(title)){
            intent.putExtra(SPUtils.EXTRA_TITLE,title);
        }

        if(reqCode != -1) {
            context.startActivityForResult(intent,reqCode);
        }else {
            context.startActivity(intent);
        }
    }

    /**
     * 搜索
     *
     * @param context
     */
    public static void gotoSearchActivity(Context context, String itemType, String source , String title) {
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtra(SPUtils.EXTRA_TYPE,itemType);
        if(!StringUtil.isEmpty(source)){
            intent.putExtra(SPUtils.EXTRA_SOURCE,source);
        }

        if(!StringUtil.isEmpty(title)){
            intent.putExtra(SPUtils.EXTRA_TITLE,title);
        }

        context.startActivity(intent);
    }

    /**
     * 搜索
     *
     * @param context
     */
    public static void gotoSearchActivity(Context context, String itemType, String source ,String title,String cityCode) {
        Intent intent = new Intent(context, SearchActivity.class);
        if(!StringUtil.isEmpty(itemType)) {
            intent.putExtra(SPUtils.EXTRA_TYPE, itemType);
        }
        if(!StringUtil.isEmpty(source)){
            intent.putExtra(SPUtils.EXTRA_SOURCE,source);
        }

        if(!StringUtil.isEmpty(title)){
            intent.putExtra(SPUtils.EXTRA_TITLE,title);
        }

        if(!StringUtil.isEmpty(cityCode)){
            intent.putExtra(SPUtils.EXTRA_CURRENT_AMAP_CITY_CODE,cityCode);
        }
        context.startActivity(intent);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        Intent intent = getIntent();
        String title = intent.getStringExtra(SPUtils.EXTRA_TITLE);
        String type = intent.getStringExtra(SPUtils.EXTRA_TYPE);
        String source = intent.getStringExtra(SPUtils.EXTRA_SOURCE);
        mSearchEditText = mBaseNavView.getSearchBox();
        mSearchEditText.requestFocus();
        mSearchFragment = SearchFragment.getInstance(title, type, source);
        getSupportFragmentManager().beginTransaction().replace(R.id.ac_base_content_view, mSearchFragment).commit();
    }

    @Override
    public View onLoadContentView() {
        return null;
    }

    @Override
    public View onLoadNavView() {
        mBaseNavView = new BaseNavView(this);
        mBaseNavView.showSeachView(false, true, true, getString(R.string.hint_desthint_search));
//        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT);
//        layoutParams.rightMargin = 30;
//        layoutParams.addRule(RelativeLayout.LEFT_OF, R.id.base_nav_view_right_text_layout);
//        mBaseNavView.resetLayoutParams(R.id.base_nav_view_edit_layout, layoutParams);
        mBaseNavView.setRightText(getString(R.string.cancel));
        mBaseNavView.setRightTextClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        return mBaseNavView;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }

    @Override
    public SearchEditText getSearchEditText() {
        return mSearchEditText;
    }
}
