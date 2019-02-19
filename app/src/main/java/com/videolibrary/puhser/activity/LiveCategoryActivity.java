package com.videolibrary.puhser.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.quanyan.base.BaseActivity;
import com.quanyan.base.view.BaseNavView;
import com.quanyan.yhy.R;
import com.quanyan.yhy.ui.adapter.base.BaseAdapterHelper;
import com.quanyan.yhy.ui.adapter.base.QuickAdapter;
import com.videolibrary.utils.IntentUtil;
import com.yhy.common.beans.net.model.msg.LiveCategoryResult;

import java.util.ArrayList;

/**
 * Created with Android Studio.
 * Title:LiveCategoryActivity
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:邓明佳
 * Date:2016/9/13
 * Time:15:07
 * Version 1.0
 */
public class LiveCategoryActivity extends BaseActivity {
    BaseNavView navView;
    ListView mListView;
    private long categoryCode;
    ArrayList<LiveCategoryResult> categoryResults;
    QuickAdapter<LiveCategoryResult> adapter;

    @Override
    protected void initView(Bundle savedInstanceState) {
        categoryCode = getIntent().getLongExtra(IntentUtil.BUNDLE_CATEGORY_CODE, 0);
        categoryResults = (ArrayList<LiveCategoryResult>) getIntent().getSerializableExtra(IntentUtil.BUNDLE_CATEGORY_RESULT);
        mListView = (ListView) findViewById(R.id.base_listview);

        navView.setTitleText("直播分类");
        if (categoryResults != null && categoryResults.size() > 0) {
            adapter = new QuickAdapter<LiveCategoryResult>(this, R.layout.item_live_category) {
                @Override
                protected void convert(BaseAdapterHelper helper, LiveCategoryResult item) {
                    helper.setText(R.id.item_live_category_name, item.name);
                    helper.setVisible(R.id.item_live_category_check, item.code == categoryCode);
                }
            };
            mListView.setAdapter(adapter);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    LiveCategoryResult result = adapter.getItem(position);
                    Intent intent = new Intent();
                    intent.putExtra(IntentUtil.BUNDLE_CATEGORY_NAME, result.name);
                    intent.putExtra(IntentUtil.BUNDLE_CATEGORY_CODE, result.code);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });

            adapter.replaceAll(categoryResults);
        } else {
            finish();
        }
    }

    @Override
    public View onLoadContentView() {
        return LayoutInflater.from(this).inflate(R.layout.ac_live_category, null);
    }

    @Override
    public View onLoadNavView() {
        return navView = new BaseNavView(this);
    }

    @Override
    public boolean isTopCover() {
        return false;
    }
}
