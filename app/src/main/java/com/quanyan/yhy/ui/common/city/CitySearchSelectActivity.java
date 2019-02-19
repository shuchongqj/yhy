package com.quanyan.yhy.ui.common.city;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.quanyan.base.BaseActivity;
import com.quanyan.base.util.StringUtil;
import com.quanyan.base.view.BaseNavView;
import com.quanyan.yhy.R;
import com.quanyan.yhy.ui.common.city.adapter.SearchCityAdapter;
import com.quanyan.yhy.view.SearchEditText;
import com.yhy.common.beans.city.bean.AddressBean;
import com.yhy.common.beans.city.bean.ListCityBean;
import com.yhy.common.utils.SPUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:CitySearchSelectActivity
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:baojie
 * Date:2016-3-2
 * Time:9:37
 * Version 1.0
 */

public class CitySearchSelectActivity extends BaseActivity implements TextWatcher {

    private SearchEditText mSearchBox;
    private ListView lv_city;
    private List<AddressBean> cityDatas;
    private SearchCityAdapter mSearchAdapter;
    private List<AddressBean> filterDateList;
    private TextView tv_cancle;
    private String mSelectType;
    private String mType;

    /**
     * 跳转城市选择搜索
     * @param activity
     * @param reqCode
     */
    public static void gotoSearchSelectCity(Activity activity, String type, ListCityBean cityData, int reqCode){
        Intent intent = new Intent(activity, CitySearchSelectActivity.class);
        intent.putExtra(SPUtils.EXTRA_TYPE, type);
        intent.putExtra(SPUtils.EXTRA_DATA, cityData);
        activity.startActivityForResult(intent, reqCode);
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.ac_searchselect_city, null);
    }

    private BaseNavView mBaseNavView;

    @Override
    public View onLoadNavView() {
        mBaseNavView = new BaseNavView(this);
        return mBaseNavView;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }

    private void initData() {
        //cityDatas = LocalUtils.loadCities(getApplicationContext());
    }

    private void initEvent() {
        mSearchBox.addTextChangedListener(this);
        lv_city.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra(SPUtils.EXTRA_DATA, (AddressBean) mSearchAdapter.getItem(position));
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mType = (String) getIntent().getSerializableExtra(SPUtils.EXTRA_TYPE);
        ListCityBean cityData = (ListCityBean) getIntent().getSerializableExtra(SPUtils.EXTRA_DATA);
        if(cityData != null){
            cityDatas = cityData.getDatas();
        }

        filterDateList = new ArrayList<>();
        tv_cancle = (TextView) findViewById(R.id.tv_cancle);
        mSearchBox = (SearchEditText) findViewById(R.id.et_search_box);
        mSearchBox.setVisibility(View.VISIBLE);
        lv_city = (ListView) findViewById(R.id.lv_city);
        mSearchBox.setFocusable(true);
        mSearchBox.requestFocus();

        mSelectType = getIntent().getStringExtra(SPUtils.EXTRA_TYPE);

        mSearchAdapter = new SearchCityAdapter(filterDateList);
        lv_city.setAdapter(mSearchAdapter);

        initData();
        initEvent();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
        filterData(s.toString());
    }


    @Override
    public void afterTextChanged(Editable s) {

    }

    private void filterData(String filterStr) {
        if (!StringUtil.isEmpty(filterStr)){
            filterDateList.clear();
            for (AddressBean addressBean : cityDatas) {
                if (addressBean.getPinyin().contains(filterStr.toString().toUpperCase())
                        || addressBean.getName().contains(filterStr.toString())
                        || addressBean.getShortPinyin().contains(filterStr.toString().toUpperCase())) {
                    filterDateList.add(addressBean);
                }
            }
            if(filterDateList.size() <= 0){
                //没有搜索到的结果处理
                //ToastUtil.showToast(this, "未搜索到结果");
            }
        }

        Collections.sort(filterDateList, new PinyinComparator());
        mSearchAdapter.updateListView(filterDateList);
    }

    public class PinyinComparator implements Comparator<AddressBean> {

        @Override
        public int compare(AddressBean lhs, AddressBean rhs) {
            return lhs.getPinyin().compareTo(rhs.getPinyin());
        }
    }
}
