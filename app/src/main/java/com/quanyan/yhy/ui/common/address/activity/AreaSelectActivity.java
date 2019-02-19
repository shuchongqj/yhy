package com.quanyan.yhy.ui.common.address.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.quanyan.base.BaseActivity;
import com.quanyan.base.util.StringUtil;
import com.quanyan.base.view.BaseNavView;
import com.quanyan.yhy.R;
import com.quanyan.yhy.ui.common.address.adapter.AddressAdapter;
import com.yhy.common.beans.net.model.common.address.Address;
import com.yhy.common.beans.net.model.common.address.AddressItem;
import com.yhy.common.beans.net.model.common.address.Area;
import com.yhy.common.beans.net.model.common.address.City;
import com.yhy.common.beans.net.model.common.address.CityEntity;
import com.yhy.common.beans.net.model.common.address.Province;
import com.yhy.common.utils.SPUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with Android Studio.
 * Title:HomeTownSelectActivity
 * Description:
 * Copyright:Copyright (c) 2014
 * Author:Alice
 * Date:15/9/10
 * Time:上午11:02
 * Version 1.0
 */
public class AreaSelectActivity extends BaseActivity {
    public final static int CHECKADDRESS_CODE = 0;
    public final static int NO_CHECK = -2;
    private final String ALLPRC = "allprc"; // 保存所有省份KEY
    private AddressAdapter adapter;
    // --------view
    private ListView lv_address;
    private List<AddressItem> data = null;
    private Address mAddress;
    private TextView tv_province, tv_city;
    private LinearLayout ll_commom_loading;
    private ImageView iv_loading_icon;
    // --------Listener
    private AdapterView.OnItemClickListener itemClickListener;
    private View.OnClickListener cityListeren, provinceListeren;

    private CityEntity mCityEntity;
    // --------data
    private int temp = 0; // 表示3个状态 0 选择省份 1 选择城市 2选择区 默认选择省份
    Map<String, List<AddressItem>> allCache = new HashMap<String, List<AddressItem>>(); // 缓存选择过的

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.ac_area_select, null);
    }

    @Override
    public boolean isTopCover() {
        return false;
    }

    private BaseNavView mBaseNavView;
    @Override
    public View onLoadNavView() {
        mBaseNavView = new BaseNavView(this);
        mBaseNavView.setTitleText("选择城市");
        mBaseNavView.setLeftClick(v -> {
            if (tv_province.getVisibility() == View.VISIBLE) {
                if (tv_city.getVisibility() == View.VISIBLE) {
                    checkCity();
                    return;
                } else {

                    checkPro();
                    return;
                }

            }
            setResult(NO_CHECK);
            finish();
        });
        return mBaseNavView;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mAddress = (Address) getIntent().getSerializableExtra(
                SPUtils.EXTRA_DATA);
        mCityEntity = new CityEntity(this);
        initViews();
        initAdapter();
        if (null != mAddress) {
            checkBeanAddress();
        } else {
            initprovince_data();
        }

        initListeren();
    }

    /**
     * 跳转到地区去选择
     * @param context
     * @param address
     */
    public static void gotoAreaSelect(Activity context, Address address, int requestCode){
        Intent intent = new Intent(context,AreaSelectActivity.class);
        if(null != address){
            intent.putExtra(SPUtils.EXTRA_DATA, address);
        }

        context.startActivityForResult(intent, requestCode);
    }

    /**
     * 当为修改的时候 默认选择之前选中的省份 和城市
     */
    public void checkBeanAddress() {
        if (null == mAddress) {
            initprovince_data();
            return;
        }

        if (StringUtil.isEmpty(mAddress.provinceCode)
                || StringUtil.isEmpty(mAddress.province)) {
            initprovince_data();
            return;
        }

        showPro(mAddress.province);

        if (StringUtil.isEmpty(mAddress.cityCode)
                || StringUtil.isEmpty(mAddress.city)) {
            setCityData(mAddress.provinceCode);
            return;
        }

        showCity(mAddress.city);

        if (StringUtil.isEmpty(mAddress.areaCode)
                || StringUtil.isEmpty(mAddress.area)) {
            initAreaData(mAddress.cityCode);
            return;
        }
    }

    /**
     * 显示头部选中的省份
     */
    private void showPro(String name) {
        tv_province.setText(name);
        tv_province.setVisibility(View.VISIBLE);
    }

    /**
     * 显示头部选中的城市
     */
    private void showCity(String name) {
        tv_city.setText(name);
        tv_city.setVisibility(View.VISIBLE);
        initAreaData(mAddress.cityCode);
    }

    public void initAreaData(String cityCode) {
        if (TextUtils.isEmpty(cityCode)) {
            // TODO
            return;
        }
        if (null == mCityEntity) {
            mCityEntity = new CityEntity(this);
        }

        if (null != mCityEntity.mAreaHm.get(cityCode) && mCityEntity.mAreaHm.get(cityCode).size() > 0) {
            if (null == data) {
                data = new ArrayList<AddressItem>();
            }
            if (data.size() > 0) {
                data.clear();
            }
            for (Area area : mCityEntity.mAreaHm.get(cityCode)) {
                if (null != area) {
                    AddressItem addressItem = new AddressItem(area.areaCode,
                            area.areaName, cityCode);
                    data.add(addressItem);
                }
            }
            adapter.notifyDataSetChanged();
            temp = 2;
            allCache.put(cityCode, data);
        } else {

        }

    }

    private void checkCity() {
        tv_city.setVisibility(View.GONE);
        setCityData(mAddress.provinceCode);
    }

    private void checkPro() {
        temp = 0;
        tv_province.setVisibility(View.GONE);
        tv_city.setVisibility(View.GONE);
        initprovince_data(); // 初始化所有省份
    }

    private void initListeren() {
        // 点击头部城市
        cityListeren = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCity();
            }
        };

        // 点击头部省份
        provinceListeren = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPro();
            }

        };
        // 点击ListViewItem时候
        itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if (temp == 0) { // 省份Listview的item事件
                    ChageDataOnProvince(arg2);
                } else {
                    if (temp == 1) { // 城市ListView的item事件
                        ChageDataOnCity(arg2);
                    } else {
                        if (temp == 2) { // 区域Listviewitem事件
                            mAddress.area = data.get(arg2).name;
                            mAddress.areaCode = data.get(arg2).code;
                            Intent intent = new Intent();
                            intent.putExtra(SPUtils.EXTRA_DATA,
                                    mAddress);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }
                }
            }

        };

        tv_city.setOnClickListener(cityListeren);
        tv_province.setOnClickListener(provinceListeren);
        lv_address.setOnItemClickListener(itemClickListener);
    }

    /**
     * 点击省份项时 数据改变操作
     *
     * @param arg2
     */
    private void ChageDataOnProvince(int arg2) {
        if(null == mAddress){
            mAddress = new Address();
        }
        if (mAddress.city != null) {
            mAddress.city = null;
            mAddress.cityCode = null;
        }
        tv_province.setText(data.get(arg2).name);
        // 将点击的项 存到对象中
        mAddress.provinceCode = data.get(arg2).code;
        mAddress.province = data.get(arg2).name;
        tv_province.setVisibility(View.VISIBLE);
        setCityData(data.get(arg2).code);
    }

    public void cacheData(final String code) {
        data.clear();
        data.addAll(allCache.get(code));
        adapter.notifyDataSetChanged();
        lv_address.setSelection(0);
    }

    public boolean IsMapCache(final String code) {
        for (String item : allCache.keySet()) {
            if (item.equals(code)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 点击城市时 数据改变操作
     *
     * @param arg2
     */
    private void ChageDataOnCity(int arg2) {
        tv_city.setText(data.get(arg2).name);
        mAddress.city = data.get(arg2).name;
        mAddress.cityCode = data.get(arg2).code;
        tv_city.setVisibility(View.VISIBLE);
        setAreaData(data.get(arg2).code);
    }

    /**
     * 通过省份Code 查询所有城市 绑定Data
     *
     * @param provinceCode
     *            省份Code
     */
    public void setCityData(String provinceCode) {
        if (TextUtils.isEmpty(provinceCode)) {
            // TODO
            return;
        }

        if (null == mCityEntity) {
            mCityEntity = new CityEntity(this);
        }

        for (int i = 0; i < mCityEntity.mProvinceList.size(); i++) {
            if (mCityEntity.mProvinceList.get(i).provinceCode
                    .equals(provinceCode)) {
                if (null == data) {
                    data = new ArrayList<AddressItem>();
                }
                if (data.size() > 0) {
                    data.clear();
                }
                if (null == mCityEntity.mProvinceList.get(i).mallCityList
                        || mCityEntity.mProvinceList.get(i).mallCityList.size() == 0) {
                    // TODO
                    return;
                }
                for (City city : mCityEntity.mProvinceList.get(i).mallCityList) {
                    if (null != city) {
                        AddressItem addressItem = new AddressItem(
                                city.cityCode, city.cityName, provinceCode);
                        data.add(addressItem);
                    }
                }
                adapter.notifyDataSetChanged();
                temp = 1;
                allCache.put(provinceCode, data);
            }
        }
    }

    /**
     * 通过城市的Code 查询所有区域 绑定Data
     *
     * @param cityCode
     *            城市Code
     */
    public void setAreaData(String cityCode) {
        if (TextUtils.isEmpty(cityCode)) {
            // TODO
            return;
        }
        if (null == mCityEntity) {
            mCityEntity = new CityEntity(this);
        }

        if (null != mCityEntity.mAreaHm.get(cityCode) && mCityEntity.mAreaHm.get(cityCode).size() > 0) {
            if (null == data) {
                data = new ArrayList<AddressItem>();
            }
            if (data.size() > 0) {
                data.clear();
            }
            for (Area area : mCityEntity.mAreaHm.get(cityCode)) {
                if (null != area) {
                    AddressItem addressItem = new AddressItem(area.areaCode,
                            area.areaName, cityCode);
                    data.add(addressItem);
                }
            }
            adapter.notifyDataSetChanged();
            temp = 2;
            allCache.put(cityCode, data);
        } else {
            mAddress.areaCode = "";
            mAddress.area = "";
            Intent intent = new Intent();
            intent.putExtra(SPUtils.EXTRA_DATA,
                    mAddress);
            //setResult(CHECKADDRESS_CODE, intent);
            setResult(RESULT_OK, intent);
            finish();
        }

    }

    public void showAddressLoading() {
        ll_commom_loading.setVisibility(View.VISIBLE);
        AnimationDrawable animationDrawable = (AnimationDrawable) iv_loading_icon
                .getDrawable();
        animationDrawable.start();

    }

    public void hideAddressLoading() {
        ll_commom_loading.setVisibility(View.GONE);
    }

    private void initViews() {
        ll_commom_loading = (LinearLayout) findViewById(R.id.ll_commom_loading_address);
        iv_loading_icon = (ImageView) findViewById(R.id.iv_loading_icon_address);
        lv_address = (ListView) findViewById(R.id.lv_common_address);
        tv_city = (TextView) findViewById(R.id.tv_common_address_city);
        tv_province = (TextView) findViewById(R.id.tv_common_address_province);
    }

    private void initAdapter() {
        data = new ArrayList<AddressItem>();
        adapter = new AddressAdapter(data, this); // 初始化显示省份
        lv_address.setAdapter(adapter);
    }

    /**
     * 初始化 所有省份
     */
    public void initprovince_data() {
        if (null == mCityEntity) {
            mCityEntity = new CityEntity(this);
        }

        if (null != mCityEntity.mProvinceList) {
            if (null == data) {
                data = new ArrayList<AddressItem>();
            }
            if (data.size() > 0) {
                data.clear();
            }
            for (Province province : mCityEntity.mProvinceList) {
                if (null != province) {
                    AddressItem addressItem = new AddressItem(
                            province.provinceCode, province.provinceName, "");
                    data.add(addressItem);
                }
            }
            adapter.notifyDataSetChanged();
            temp = 0;
        } else {
            // TODO
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (tv_province.getVisibility() == View.VISIBLE) {
                if (tv_city.getVisibility() == View.VISIBLE) {
                    checkCity();
                    return false;
                } else {
                    checkPro();
                    return false;
                }
            }
        }
        setResult(NO_CHECK);
        this.finish();
        return true;
    }

    @Override
    protected void onDestroy() {
        if (allCache != null) {
            allCache.clear();
            allCache = null;
        }
        if (null != mCityEntity) {
            mCityEntity = null;
        }
        super.onDestroy();
    }
}
