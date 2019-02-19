package com.quanyan.yhy.ui.common.city;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.quanyan.base.BaseActivity;
import com.quanyan.base.baseenum.IActionTitleBar;
import com.quanyan.base.util.StringUtil;
import com.quanyan.base.view.BaseNavView;
import com.quanyan.base.view.customview.pulltorefresh.PullToRefreshBase;
import com.quanyan.base.view.customview.pulltorefresh.PullToRefreshListView;
import com.quanyan.base.yminterface.ErrorViewClick;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.AnalyDataValue;
import com.quanyan.yhy.common.ErrorCode;
import com.quanyan.yhy.common.ItemType;
import com.quanyan.yhy.common.ResourceType;
import com.quanyan.yhy.ui.adapter.base.QuickAdapter;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.base.utils.TCEventHelper;
import com.quanyan.yhy.ui.comment.ValueCommentType;
import com.quanyan.yhy.ui.common.city.adapter.DestSelectAdapter;
import com.quanyan.yhy.ui.line.LineActivity;
import com.quanyan.yhy.ui.nineclub.controller.BuyMustController;
import com.quanyan.yhy.view.HomeMenu_GridView;
import com.quanyan.yhy.view.SearchEditText;
import com.yhy.common.beans.city.bean.AddressBean;
import com.yhy.common.beans.city.bean.CityResultBean;
import com.yhy.common.beans.net.model.RCShowcase;
import com.yhy.common.beans.net.model.common.Booth;
import com.yhy.common.beans.net.model.trip.CityInfo;
import com.yhy.common.beans.net.model.user.Destination;
import com.yhy.common.beans.net.model.user.DestinationList;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.SPUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with Android Studio.
 * Title:DestinationSelectActivity
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:baojie
 * Date:2016-3-4
 * Time:15:22
 * Version 1.0
 */

public class DestinationSelectActivity extends BaseActivity {
    private PullToRefreshListView mPList;
    private ListView mListView;

    private HomeMenu_GridView mGTop;
    private QuickAdapter<AddressBean> mCityAdapter;

    private List<AddressBean> mAddressBeans;
    private List<AddressBean> mCityTopBeans;
    private List<CityResultBean> mCityResultBeans;

    private DestSelectAdapter mDestBottomAdapter;
    private String mType;
    private static int REQCODE = 101;
    private String mLineType;
    private String mOutType;//热门城市用到的type
    private String mPageTitle;
    private String mSource;
    private BuyMustController mController;
    //目的地类型
    private String mRequestType = ItemType.LINE;

    /**
     * 跳转到目的地城市选择
     *
     * @param activity
     * @param reqCode
     */
    public static void gotoDestinationSelectActivity(Activity activity, String type, String lineType, String title, String source, String outType, int reqCode) {
        Intent intent = new Intent(activity, DestinationSelectActivity.class);
        intent.putExtra(SPUtils.EXTRA_TYPE, type);
        intent.putExtra(SPUtils.EXTRA_CODE, outType);
        if (!StringUtil.isEmpty(lineType)) {
            intent.putExtra(SPUtils.EXTRA_DATA, lineType);
        }

        if (!StringUtil.isEmpty(title)) {
            intent.putExtra(SPUtils.EXTRA_TITLE, title);
        }

        if (!StringUtil.isEmpty(source)) {
            intent.putExtra(SPUtils.EXTRA_SOURCE, source);
        }
        if (reqCode == -1) {
            activity.startActivity(intent);
        } else {
            activity.startActivityForResult(intent, reqCode);
        }
    }

    /**
     * 跳转到目的地城市选择
     *
     * @param activity
     * @param type
     * @param outType
     * @param reqCode
     */
    public static void gotoDestinationSelectActivity(Activity activity, String type, String outType, int reqCode) {
        Intent intent = new Intent(activity, DestinationSelectActivity.class);
        intent.putExtra(SPUtils.EXTRA_TYPE, type);
        intent.putExtra(SPUtils.EXTRA_CODE, outType);
        activity.startActivityForResult(intent, reqCode);
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.ac_destselectcity, null);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SPUtils.setIsJumpFromHomeSearch(getApplicationContext(), false);
    }

    //访问头部数据4个球
    private void doNetTopDatas() {
        if (!StringUtil.isEmpty(mOutType)) {
            String boothCode = "";
            if (ItemType.LINE.equals(mOutType)) {//线路热门城市
                boothCode = ResourceType.QUANYAN_FEATURE_DESTINATION;
            } else if (ItemType.SCENIC.equals(mOutType)) {//景区热门城市
                boothCode = ResourceType.JX_SCENIC_HOT_DEST;
            } else if (ItemType.HOTEL.equals(mOutType)) {//酒店热门城市
                boothCode = ResourceType.JX_HOTEL_HOT_CITY;
            }
            mController.doGetBoothDestTop(DestinationSelectActivity.this,boothCode);
        }
    }

    private void initEvent() {
        mGTop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                goFinish(mCityTopBeans.get(position));
            }
        });

        mDestBottomAdapter.setOnSubscribeClickListener(new DestSelectAdapter.OnSubscribeClickListener() {
            @Override
            public void onSubscribeClick(AddressBean addressBean) {
                goFinish(addressBean);
            }
        });
    }

    //点击事件处理
    private void goFinish(AddressBean bean) {
        //打点
        Map<String, String> map = new HashMap<>();
        map.put(AnalyDataValue.KEY_PID, bean.getCityCode());
        map.put(AnalyDataValue.KEY_NAME, bean.getName());
        map.put(AnalyDataValue.KEY_TYPE, AnalyDataValue.getType(mLineType));
        TCEventHelper.onEvent(this, AnalyDataValue.DESTINATION_CHOICE, map);
        if (LineActivity.class.getSimpleName().equals(mSource) &&
                (ItemType.TOUR_LINE.equals(mLineType)
                        || ItemType.FREE_LINE.equals(mLineType)
                        || ItemType.TOUR_LINE_ABOARD.equals(mLineType)
                        || ItemType.FREE_LINE_ABOARD.equals(mLineType))) {
            NavUtils.gotoLineSearchResultActivity(this,
                    mPageTitle,
                    mLineType,
                    null,
                    bean.getCityCode(),
                    bean.getName(),
                    null,
                    -1);
            finish();
        } else {
            Intent intent = new Intent();
            intent.putExtra(SPUtils.EXTRA_SELECT_CITY, bean);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    //头部显示
    private void initTopHg() {
        mPList.setScrollingWhileRefreshingEnabled(!mPList.isScrollingWhileRefreshingEnabled());
        mPList.setMode(PullToRefreshBase.Mode.DISABLED);
        mListView = mPList.getRefreshableView();
        mListView.setDivider(getResources().getDrawable(R.color.transparent));
        mListView.setDividerHeight(0);

        View topView = View.inflate(this, R.layout.head_destselect_city, null);
        mGTop = (HomeMenu_GridView) topView.findViewById(R.id.hg_dest_top);

        //头部adapter
        mCityAdapter = CityAdapterHelper.setAdapter(this, new ArrayList<AddressBean>());

        mGTop.setAdapter(mCityAdapter);

        mListView.addHeaderView(topView);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mController = new BuyMustController(this, mHandler);
        mType = (String) getIntent().getSerializableExtra(SPUtils.EXTRA_TYPE);
        mLineType = getIntent().getStringExtra(SPUtils.EXTRA_DATA);
        mOutType = getIntent().getStringExtra(SPUtils.EXTRA_CODE);
        mSource = getIntent().getStringExtra(SPUtils.EXTRA_SOURCE);
        mPageTitle = getIntent().getStringExtra(SPUtils.EXTRA_TITLE);
        mPList = (PullToRefreshListView) findViewById(R.id.pull_to_refresh_listview);
        initTitle();
        initTopHg();

        mDestBottomAdapter = new DestSelectAdapter(this);
        mListView.setAdapter(mDestBottomAdapter);
//        mAddressBeans = ((YHYApplication) getApplicationContext()).getDestCities(mRequestType);
        //16个城市加载
        destList();

        initEvent();
        showLoadingView(getString(R.string.loading_text));
        doNetTopDatas();
    }

    private void destList() {
        //如果为空就向服务器去取数据
        if (mAddressBeans == null || mAddressBeans.size() == 0) {
            //访问网络获取目的地列表城市
            doNetDatas();
        } else {
            evalueDataList();
        }
    }

    //目的地列表信息
    private void doNetDatas() {
        showLoadingView(getString(R.string.loading_text));
        mController.doQueryDestinationTree(this,mOutType);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        hideLoadingView();
        hideErrorView(null);
        switch (msg.what) {
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
                        destList();
                    }
                });
                break;
            case ValueConstants.MSG_GET_ABROAD_DESTINATION_OK:
//                CityList result = (CityList) msg.obj;
//                if (result != null) {
//                    handlerDatas(result.cityInfoList);
//                }
                DestinationList result = (DestinationList) msg.obj;
                if (result != null) {
                    handlerData(result);
                }
                break;
            case ValueConstants.MSG_GET_ABROAD_DESTINATION_KO:
                showErrorView(null, ErrorCode.NETWORK_UNAVAILABLE == msg.arg1 ?
                        IActionTitleBar.ErrorType.NETUNAVAILABLE : IActionTitleBar.ErrorType.ERRORNET, "", getString(R.string.error_view_network_loaderror_content), "", new ErrorViewClick() {
                    @Override
                    public void onClick(View view) {
                        doNetTopDatas();
                        destList();
                    }
                });
                break;
        }
    }

    private List<Destination> mDestinations;//服务器返回的数据
    //处理区域数据
    private void handlerData(DestinationList result) {
        if (result.value != null && result.value.size() > 0) {
            if (mDestinations == null) {
                mDestinations = new ArrayList<>();
            }
            if (result.value != null && result.value.size() > 0) {
                for (int i = 0; i < result.value.size(); i++) {
                    if (result.value.get(i).isInnerArea) {
                        mDestinations = result.value.get(i).childList;
                    }
                }
            }
            mAddressBeans = AddressBean.transferNewCityInfoToAddressBeanNotChild(mDestinations);
            evalueDataList();
        } else {
            showNoDataPageView();
        }
    }

    /**
     * 展示无数据的提示
     */
    private void showNoDataPageView() {
        showErrorView(null, IActionTitleBar.ErrorType.EMPTYVIEW, getString(R.string.hotel_empty_text), " ", "", null);
    }

    //处理头部数据
    private void handlerTopDatas(List<RCShowcase> datas) {
        if (datas != null && datas.size() > 0) {
            if (mCityTopBeans == null) {
                mCityTopBeans = new ArrayList<>();
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
                mCityAdapter.addAll(mCityTopBeans);
            }
        }
    }

    //处理列表信息
    private void handlerDatas(List<CityInfo> value) {
        mAddressBeans = AddressBean.transferCityInfoToAddressBean(value);
        //mAddressBeans = LocalUtils.destCityToSourceCity(this, value);
        evalueDataList();
    }

    //填充数据
    private void evalueDataList() {
        if (mAddressBeans != null && mAddressBeans.size() > 0) {
            soutList();
        }
        if (mCityResultBeans != null && mCityResultBeans.size() > 0) {
            mDestBottomAdapter.addAll(mCityResultBeans);
        }
    }

    private void soutList() {
        PinyinComparator pinyinComparator = new PinyinComparator();
        Collections.sort(mAddressBeans, pinyinComparator);
        mCityResultBeans = new ArrayList<>();
        List<AddressBean> beanList = new ArrayList<>();
        for (int i = 0; i < mAddressBeans.size(); i++) {
            String currentIndexStr = String.valueOf(mAddressBeans.get(i).getPinyin().charAt(0));
            if (i != mAddressBeans.size() - 1) {
                String lasterIndexStr = String.valueOf(mAddressBeans.get(i + 1).getPinyin().charAt(0));
                beanList.add(mAddressBeans.get(i));
                if (!TextUtils.equals(lasterIndexStr, currentIndexStr)) {
                    CityResultBean cityResultBean = new CityResultBean();
                    cityResultBean.setIndex(currentIndexStr);
                    List<AddressBean> beans = new ArrayList<>();
                    beans.addAll(beanList);
                    cityResultBean.setLists(beans);
                    mCityResultBeans.add(cityResultBean);
                    beanList.clear();
                }
            } else {
                beanList.add(mAddressBeans.get(i));
                CityResultBean cityResultBean = new CityResultBean();
                cityResultBean.setIndex(currentIndexStr);
                List<AddressBean> beans = new ArrayList<>();
                beans.addAll(beanList);
                cityResultBean.setLists(beans);
                mCityResultBeans.add(cityResultBean);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQCODE && resultCode == RESULT_OK) {
            AddressBean bean = (AddressBean) data.getSerializableExtra(SPUtils.EXTRA_DATA);
            goFinish(bean);
        }
    }

    private void initTitle() {
        if (ValueCommentType.COMMENT_SOURCE_SUPPERMEN.equals(mType)) {
            mBaseNavView.setTitleText(getString(R.string.city_select_dest_title));
        } else if (ValueCommentType.COMMENT_SOURCE_TRAVEL.equals(mType)) {
            mBaseNavView.showSeachView(true, false,false,getString(R.string.hint_desthint_search));
            final SearchEditText searchBox = mBaseNavView.getSearchBox();
            final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            searchBox.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    imm.hideSoftInputFromWindow(searchBox.getWindowToken(), 0);
                    searchBox.setInputType(InputType.TYPE_NULL);
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (!StringUtil.isEmpty(mLineType)) {
                            NavUtils.gotoSearchActivity(DestinationSelectActivity.this,
                                    mLineType,
                                    null,
                                    mPageTitle,
                                    -1);
                        }
                    }
                    return true;
                }
            });
        }
    }
}
