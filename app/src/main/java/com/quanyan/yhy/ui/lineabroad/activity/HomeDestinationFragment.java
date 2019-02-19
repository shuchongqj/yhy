package com.quanyan.yhy.ui.lineabroad.activity;

import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.quanyan.base.BaseFragment;
import com.quanyan.base.baseenum.IActionTitleBar;
import com.quanyan.base.util.ScreenSize;
import com.quanyan.base.util.StringUtil;
import com.quanyan.base.view.customview.NoScrollGridView;
import com.quanyan.base.yminterface.ErrorViewClick;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.ErrorCode;
import com.quanyan.yhy.common.ItemType;
import com.quanyan.yhy.common.ResourceType;
import com.quanyan.yhy.eventbus.EvBusDestCityChoose;
import com.quanyan.yhy.ui.adapter.base.BaseAdapterHelper;
import com.quanyan.yhy.ui.adapter.base.QuickAdapter;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.lineabroad.bean.AbroadAreaBean;
import com.quanyan.yhy.ui.lineabroad.bean.AbroadResultBean;
import com.quanyan.yhy.ui.lineabroad.bean.DestAbroadHelper;
import com.quanyan.yhy.ui.lineabroad.bean.SimpleNameComparator;
import com.quanyan.yhy.ui.nineclub.controller.BuyMustController;
import com.yhy.common.beans.city.bean.AddressBean;
import com.yhy.common.beans.net.model.RCShowcase;
import com.yhy.common.beans.net.model.common.Booth;
import com.yhy.common.beans.net.model.user.Destination;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.SPUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created with Android Studio.
 * Title:HomeDestinationFragment
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-6-1
 * Time:20:10
 * Version 1.1.0
 */

public class HomeDestinationFragment extends BaseFragment {

    private Destination mDatas;
    private List<AbroadAreaBean> mAbroadAreaBeans = new ArrayList<>();

    @ViewInject(R.id.ll_word_container)
    private LinearLayout mLinearWordContainer;
    private List<AddressBean>  mCityTopBeans = new ArrayList<>();

    @ViewInject(R.id.lv_city_list)
    private ListView mListView;
    private QuickAdapter<AbroadResultBean> mAdapter;
    private List<AbroadResultBean> mCityResultBeans = new ArrayList<>();
    private BuyMustController mHotController;
    private String mType;
    private QuickAdapter<AddressBean> mHeaderAdapter;
    private String mPageTitle;
    private String mSource;
    private String mLineType;
    public static HomeDestinationFragment getInstance(Destination datas, String type, String pageTitle, String itemType, String source) {
        HomeDestinationFragment fragment = new HomeDestinationFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(SPUtils.EXTRA_DATA, datas);
        bundle.putString(SPUtils.EXTRA_TYPE, type);
        bundle.putString(SPUtils.EXTRA_SOURCE, source);
        bundle.putString(SPUtils.EXTRA_ITEM_TYPE, itemType);
        bundle.putString(SPUtils.EXTRA_TITLE, pageTitle);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mHotController = new BuyMustController(getActivity(), mHandler);
        Bundle bundle = getArguments();
        if(bundle != null){
            mDatas = (Destination) bundle.getSerializable(SPUtils.EXTRA_DATA);
            mType = bundle.getString(SPUtils.EXTRA_TYPE);
            mSource = bundle.getString(SPUtils.EXTRA_SOURCE);
            mLineType = bundle.getString(SPUtils.EXTRA_ITEM_TYPE);
            mPageTitle = bundle.getString(SPUtils.EXTRA_TITLE);
        }

        mAdapter = new QuickAdapter<AbroadResultBean>(getActivity(),R.layout.item_home_dest_list, new ArrayList<AbroadResultBean>()) {
            @Override
            protected void convert(BaseAdapterHelper helper, AbroadResultBean item) {
                DestAbroadHelper.handler(getActivity(), helper, item);
            }
        };

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int headViewCount = mListView.getHeaderViewsCount();
                if(position < headViewCount){
                    return ;
                }
                AbroadResultBean aab = mCityResultBeans.get(position - headViewCount);

            }
        });
        //头部热门信息
        initHeaderView();
        mListView.setAdapter(mAdapter);
        //整理所需进行的处理数据
        dataEncap();
        //热门城市访问
        doNetTopDatas();
    }

    private void doNetTopDatas() {
        if(!StringUtil.isEmpty(mType)){
            String boothCode = "";
            if(ItemType.LINE.equals(mType) || ItemType.URBAN_LINE.equals(mType)){//线路热门城市
                boothCode = ResourceType.QUANYAN_FEATURE_DESTINATION;
            }else if(ItemType.SCENIC.equals(mType)){//景区热门城市
                boothCode = ResourceType.JX_SCENIC_HOT_DEST;
            }else if(ItemType.HOTEL.equals(mType)){//酒店热门城市
                boothCode = ResourceType.JX_HOTEL_HOT_CITY;
            }else if(ItemType.SERVICE.equals(mType)){//咨询服务
                boothCode = ResourceType.JX_SERVICE_HOT_CITY;
            }
            mHotController.doGetBoothDestTop(getActivity(), boothCode);
        }
    }

    private void initHeaderView() {
        View view = View.inflate(getActivity(), R.layout.header_abroad_hotcity, null);
        NoScrollGridView gridView = (NoScrollGridView) view.findViewById(R.id.gv_hot_city);
        mHeaderAdapter = new QuickAdapter<AddressBean>(getActivity(), R.layout.item_abroad_dest, new ArrayList<AddressBean>()) {
            @Override
            protected void convert(BaseAdapterHelper helper, AddressBean item) {
                helper.setText(R.id.tv_abroad_name, item.getName());
            }
        };
        gridView.setColumnWidth((ScreenSize.getScreenWidth(getActivity().getApplicationContext()) - 250) / 3);
//        gridView.setVerticalSpacing(20);
//        gridView.setHorizontalSpacing(20);
        gridView.setAdapter(mHeaderAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EventBus.getDefault().post(new EvBusDestCityChoose(mHeaderAdapter.getItem(position).getCityCode(),mHeaderAdapter.getItem(position).getName()));
            }
        });
        mListView.addHeaderView(view);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        hideLoadingView();
        hideErrorView(null);
        switch (msg.what){
            case ValueConstants.MSG_DESTCITY_TOP_NEW_OK:
                Booth value = (Booth) msg.obj;
                if (value != null) {
                    handlerTopDatas(value.showcases);
                }
                break;
            case ValueConstants.MSG_DESTCITY_TOP_NEW_KO:
                showErrorView(null, ErrorCode.NETWORK_UNAVAILABLE == msg.arg1 ?
                        IActionTitleBar.ErrorType.NETUNAVAILABLE : IActionTitleBar.ErrorType.ERRORNET, "", getString(R.string.error_view_network_loaderror_content), "", new ErrorViewClick() {
                    @Override
                    public void onClick(View view) {
                        doNetTopDatas();
                    }
                });
                //ToastUtil.showToast(DestinationSelectActivity.this, StringUtil.handlerErrorCode(getApplicationContext(), msg.arg1));
                break;
        }
    }

    //热门城市
    private void handlerTopDatas(List<RCShowcase> datas) {
        if (datas != null && datas.size() > 0) {
            //咨询服务添加不限
            if(ItemType.SERVICE.equals(mType)){
                RCShowcase rc = addNewNoLimitedCity();
                datas.add(0,rc);
            }
            for (int i = 0; i < datas.size(); i++) {
                AddressBean addressBean = new AddressBean();
                String name = datas.get(i).title;
                String cityCode = datas.get(i).operationContent;
                if (!StringUtil.isEmpty(name) && !StringUtil.isEmpty(cityCode)) {
                    addressBean.setName(name);
                    addressBean.setCityCode(cityCode);
                    mCityTopBeans.add(addressBean);
                }
            }
            if (mCityTopBeans.size() > 0) {
                mHeaderAdapter.addAll(mCityTopBeans);
            }
        }
        //快速索引添加
        quickSort();
    }

    /**
     * 新增不限的选择
     * @return
     */
    private RCShowcase addNewNoLimitedCity(){
        RCShowcase rc = new RCShowcase();
        rc.title = "不限";
        rc.operationContent = "-1";
        return rc;
    }

    private void dataEncap() {
        if(mDatas != null && mDatas.hasChild){
            //目的地城市所在的集合
            List<Destination> oneDestinations = mDatas.childList;
            if(oneDestinations != null && oneDestinations.size() > 0 ){
                for (int i = 0; i < oneDestinations.size(); i++) {
                    AbroadAreaBean abroadAreaBean = new AbroadAreaBean();
                    abroadAreaBean.setDestination(oneDestinations.get(i));
                    abroadAreaBean.setSimpleName(oneDestinations.get(i).simpleCode);
                    //城市中所有的景点的集合
                    if(oneDestinations.get(i).hasChild && oneDestinations.get(i).childList != null && oneDestinations.get(i).childList.size() > 0){
                        for (int j = 0; j < oneDestinations.get(i).childList.size(); j++){
                            abroadAreaBean.setChildDestinations(oneDestinations.get(i).childList);
                        }
                    }
                    mAbroadAreaBeans.add(abroadAreaBean);
                }
            }
        }
        //根据集合进行分组
        soutList();
        //填充数据
        if(mCityResultBeans != null && mCityResultBeans.size() > 0){
            mAdapter.addAll(mCityResultBeans);
        }

    }

    private void quickSort() {
        if(mCityTopBeans != null && mCityTopBeans.size() > 0){
            //热门点击
            View hotView = View.inflate(getActivity(), R.layout.quick_text_index, null);
            TextView textHot = (TextView) hotView.findViewById(R.id.tv_index_name);
            textHot.setText("热");
            textHot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListView.setSelection(0);
                    ToastUtil.showToast(getActivity(), "热");
                }
            });
            mLinearWordContainer.addView(hotView);
            //其他字母添加
            if(mCityResultBeans != null && mCityResultBeans.size() > 0){
                for (int i = 0; i < mCityResultBeans.size(); i++) {
                    View indexView = View.inflate(getActivity(), R.layout.quick_text_index, null);
                    TextView textIndex = (TextView) indexView.findViewById(R.id.tv_index_name);
                    textIndex.setText(mCityResultBeans.get(i).getIndex());
                    final int finalI = i;
                    textIndex.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mListView.setSelection(finalI + mListView.getHeaderViewsCount());
                            ToastUtil.showToast(getActivity(), mCityResultBeans.get(finalI).getIndex());
                        }
                    });
                    mLinearWordContainer.addView(indexView);
                }
            }
        }
    }

    private void soutList() {
        SimpleNameComparator simpleNameComparator = new SimpleNameComparator();
        Collections.sort(mAbroadAreaBeans, simpleNameComparator);

        List<AbroadAreaBean> beanList = new ArrayList<>();
        for (int i = 0; i < mAbroadAreaBeans.size(); i++) {
            String currentIndexStr = String.valueOf(mAbroadAreaBeans.get(i).getSimpleName().charAt(0));
            if (i != mAbroadAreaBeans.size() - 1) {
                String lasterIndexStr = String.valueOf(mAbroadAreaBeans.get(i + 1).getSimpleName().charAt(0));
                beanList.add(mAbroadAreaBeans.get(i));
                if (!TextUtils.equals(lasterIndexStr, currentIndexStr)) {
                    AbroadResultBean abroadResultBean = new AbroadResultBean();
                    abroadResultBean.setIndex(currentIndexStr);
                    List<AbroadAreaBean> beans = new ArrayList<>();
                    beans.addAll(beanList);
                    abroadResultBean.setLists(beans);
                    mCityResultBeans.add(abroadResultBean);
                    beanList.clear();
                    //System.out.println("baojie" + cityResultBean.getLists().get(0).getName());
                }
            } else {
                beanList.add(mAbroadAreaBeans.get(i));
                AbroadResultBean abroadResultBean = new AbroadResultBean();
                abroadResultBean.setIndex(currentIndexStr);
                List<AbroadAreaBean> beans = new ArrayList<>();
                beans.addAll(beanList);
                abroadResultBean.setLists(beans);
                mCityResultBeans.add(abroadResultBean);
            }

        }

    }

    @Override
    public View onLoadContentView() {
        View view = View.inflate(getActivity(), R.layout.fr_home_destination, null);
        ViewUtils.inject(this, view);
        return view;
    }
}
