package com.quanyan.yhy.ui.lineabroad.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.newyhy.activity.HomeMainTabActivity;
import com.quanyan.base.BaseFragment;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.AnalyDataValue;
import com.quanyan.yhy.common.ItemType;
import com.quanyan.yhy.eventbus.EvBusDestCityChoose;

import com.quanyan.yhy.ui.adapter.base.BaseAdapterHelper;
import com.quanyan.yhy.ui.adapter.base.QuickAdapter;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.base.utils.TCEventHelper;
import com.quanyan.yhy.ui.line.LineActivity;
import com.quanyan.yhy.util.PinyinUtil;
import com.yhy.common.beans.city.bean.AddressBean;
import com.yhy.common.beans.net.model.user.Destination;
import com.yhy.common.utils.SPUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Created with Android Studio.
 * Title:AbroadDestinationFragment
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-6-1
 * Time:20:11
 * Version 1.1.0
 */

public class AbroadDestinationFragment extends BaseFragment {

    private Destination mDatas;

    @ViewInject(R.id.gv_abroad)
    private GridView mGridViewAbroad;
    private QuickAdapter<Destination> mAdapter;

    private String mPageTitle;
    private String mSource;
    private String mLineType;
    public static AbroadDestinationFragment getInstance(Destination datas, String pageTitle, String itemType, String source) {
        AbroadDestinationFragment fragment = new AbroadDestinationFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(SPUtils.EXTRA_DATA, datas);
        bundle.putString(SPUtils.EXTRA_SOURCE, source);
        bundle.putString(SPUtils.EXTRA_ITEM_TYPE, itemType);
        bundle.putString(SPUtils.EXTRA_TITLE, pageTitle);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if(bundle != null){
            mDatas = (Destination) bundle.getSerializable(SPUtils.EXTRA_DATA);
            mSource = bundle.getString(SPUtils.EXTRA_SOURCE);
            mLineType = bundle.getString(SPUtils.EXTRA_ITEM_TYPE);
            mPageTitle = bundle.getString(SPUtils.EXTRA_TITLE);
        }
        mAdapter = new QuickAdapter<Destination>(getActivity(), R.layout.item_abroad_dest, new ArrayList<Destination>()) {
            @Override
            protected void convert(BaseAdapterHelper helper, Destination item) {
                helper.setText(R.id.tv_abroad_name, item.name);
            }
        };
        mGridViewAbroad.setAdapter(mAdapter);
        mGridViewAbroad.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(mLineType.equals(ItemType.SERVICE)){
                    EventBus.getDefault().post(new EvBusDestCityChoose(mAdapter.getItem(position).code+"", mAdapter.getItem(position).name));
                }else{
                    goFinish(String.valueOf(mAdapter.getItem(position).code),mAdapter.getItem(position).name);
                }


            }
        });
        //获取数据
        handleDatas(mDatas);
    }

    //点击事件处理
    private void goFinish(String cityCode,String cityName) {
        //打点
        Map<String, String> map = new HashMap<>();
        map.put(AnalyDataValue.KEY_PID, cityCode);
        map.put(AnalyDataValue.KEY_NAME, cityName);
        map.put(AnalyDataValue.KEY_TYPE, AnalyDataValue.getType(mLineType));
        TCEventHelper.onEvent(getActivity(), AnalyDataValue.DESTINATION_CHOICE, map);
        if ((HomeMainTabActivity.class.getSimpleName().equals(mSource) || LineActivity.class.getSimpleName().equals(mSource)) &&
                (ItemType.TOUR_LINE.equals(mLineType)
                || ItemType.FREE_LINE.equals(mLineType)
                || ItemType.TOUR_LINE_ABOARD.equals(mLineType)
                || ItemType.FREE_LINE_ABOARD.equals(mLineType))) {
            NavUtils.gotoLineSearchResultActivity(getActivity(),
                    mPageTitle,
                    mLineType,
                    null,
                    cityCode,
                    cityName,
                    null,
                    -1);
            getActivity().finish();
        } else {
            AddressBean ab =  new AddressBean();
            ab.setCityCode(cityCode);
            ab.setName(cityName);
            ab.setPinyin(PinyinUtil.getPinyin(cityName));
            Intent intent = new Intent();
            intent.putExtra(SPUtils.EXTRA_SELECT_CITY, ab);
            getActivity().setResult(Activity.RESULT_OK, intent);
            getActivity().finish();
        }
    }

    @Override
    public View onLoadContentView() {
        View view = View.inflate(getActivity(), R.layout.fr_abroad_destination, null);
        ViewUtils.inject(this, view);
        return view;
    }

    private void handleDatas(Destination datas){
        if(datas != null && datas.hasChild){
            List<Destination> childList = datas.childList;
            if(childList != null && childList.size() > 0){
                mAdapter.replaceAll(childList);
            }
        }
    }

    public void setDestinations(Destination destinations) {
        handleDatas(destinations);
    }
}
