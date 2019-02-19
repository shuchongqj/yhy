package com.quanyan.yhy.ui.line;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.quanyan.base.BaseListViewActivity;
import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.AnalyDataValue;
import com.quanyan.yhy.common.ItemType;
import com.quanyan.yhy.ui.adapter.base.BaseAdapterHelper;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.base.utils.TCEventHelper;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.line.view.LineHeaderView;
import com.quanyan.yhy.ui.line.view.LineTopSearchView;
import com.quanyan.yhy.ui.line.view.NaviTopSearchView;
import com.quanyan.yhy.ui.master.helper.MasterViewHelper;
import com.yhy.common.beans.city.bean.AddressBean;
import com.yhy.common.beans.net.model.DefaultCityBean;
import com.yhy.common.beans.net.model.common.BoothList;
import com.yhy.common.beans.net.model.trip.ShortItem;
import com.yhy.common.beans.net.model.trip.ShortItemsResult;
import com.yhy.common.constants.RequestCodeValues;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.SPUtils;
import com.yhy.location.LocationManager;

import java.util.ArrayList;
import java.util.List;


/**
 * Created with Android Studio.
 * Title:AboutAndFeedController
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:zhaoxiaopo
 * Date:16/3/2
 * Time:09:00
 * Version 1.0
 */
public class LineActivity extends BaseListViewActivity<ShortItem> {

    private NaviTopSearchView mNaviTopSearchView;
    //线路类型
    private String mPageType;
    private long netDataId;

    private String mPageTitle = "";
    private LineController mLineController;
    private LineTopSearchView mLineTopSearchView;
    private LineHeaderView mLineHeaderView;
    private String mCityCode = "";
    private boolean mHasNodata = false;

//    private View mHeaderView;

    /**
     * 跳转到线路首页
     *
     * @param context
     * @param id
     * @param pageType
     * @param title
     */
    public static void gotoLineActivty(Context context, long id, String pageType, String title) {
        Intent intent = new Intent(context, LineActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(SPUtils.EXTRA_TYPE, pageType);
        bundle.putLong(SPUtils.EXTRA_ID, id);
        bundle.putString(SPUtils.EXTRA_TITLE, title);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public String getType() {
        return mPageType;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLineHeaderView.startBannerHandle(true);

        setCityName();
    }

    private void setCityName() {
        if (ItemType.CITY_ACTIVITY.equals(mPageType)) {
            mLineTopSearchView.setCityName(SPUtils.getLocalCityName(getApplicationContext()));
        } else if (ItemType.ARROUND_FUN.equals(mPageType)) {
            mLineTopSearchView.setCityName(SPUtils.getArroundCityName(getApplicationContext()));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mLineHeaderView.startBannerHandle(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            switch (requestCode) {
                case RequestCodeValues.REQUEST_LINE_STARTCITY_CHOOSE:
                    // TODO: 16/3/21 跟团 自由行 出发地选择
                    if (data != null) {
                        AddressBean addressBean = (AddressBean) data.getSerializableExtra(SPUtils.EXTRA_SELECT_CITY);
                        if (addressBean != null) {
                            mNaviTopSearchView.setStartCtiyName(addressBean.getName());
                            mCityCode = addressBean.getCityCode();
                            if (ItemType.FREE_LINE.equals(mPageType)) {
                                SPUtils.setExtraFreeTripCityName(getApplicationContext(), addressBean.getName());
                                SPUtils.setExtraFreeTripCityCode(getApplicationContext(), addressBean.getCityCode());
                                SPUtils.setFreeTripCityChange(getApplicationContext(), false);
                            } else {
                                SPUtils.setExtraPackTripCityName(getApplicationContext(), addressBean.getName());
                                SPUtils.setExtraPackTripCityCode(getApplicationContext(), addressBean.getCityCode());
                                SPUtils.setPackTripCityChange(getApplicationContext(), false);
                            }
                        }
                    }
                    break;
                case RequestCodeValues.REQUEST_LINE_DEST_CITY_SELECT:
                    // TODO: 16/3/21 同城周边 城市选择（目的地）
                    if (data != null) {
                        AddressBean addressBean = (AddressBean) data.getSerializableExtra(SPUtils.EXTRA_SELECT_CITY);
                        if (addressBean != null) {
                            mLineTopSearchView.setCityName(addressBean.getName());
                            mCityCode = addressBean.getCityCode();
                            if (ItemType.TOUR_LINE.equals(mPageType)
                                    || ItemType.FREE_LINE.equals(mPageType)
                                    || ItemType.TOUR_LINE_ABOARD.equals(mPageType)
                                    || ItemType.FREE_LINE_ABOARD.equals(mPageType)) {
                                //跳转搜索结果页
                                NavUtils.gotoLineSearchResultActivity(this,
                                        mPageTitle,
                                        mPageType,
                                        null,
                                        null,
                                        null,
                                        null,
                                        0);
                            } else if (ItemType.ARROUND_FUN.equals(mPageType)
                                    || ItemType.CITY_ACTIVITY.equals(mPageType)) {
                                //更新数据列表
                                if (ItemType.ARROUND_FUN.equals(mPageType)) {
                                    SPUtils.setArroundCityName(getApplicationContext(), addressBean.getName());
                                    SPUtils.setArroundCityCode(getApplicationContext(), addressBean.getCityCode());
                                    SPUtils.setArroundCityChange(getApplicationContext(), false);
                                } else {
                                    SPUtils.setLocalCityName(getApplicationContext(), addressBean.getName());
                                    SPUtils.setLocalCityCode(getApplicationContext(), addressBean.getCityCode());
                                    SPUtils.setLocalCityChange(getApplicationContext(), false);
                                }
                            }
                        }
                        break;
                    }
            }
            manualRefresh();
        }
    }

    @Override
    public boolean isTopCover() {
        return false;
    }

    @Override
    public View onLoadNavView() {
        mNaviTopSearchView = new NaviTopSearchView(this);
        mNaviTopSearchView.setOnNavTopListener(new NaviTopSearchView.OnNavTopClickListener() {
            @Override
            public void onStartCitySelect() {
                NavUtils.gotoSelectCity(LineActivity.this, RequestCodeValues.REQUEST_LINE_STARTCITY_CHOOSE);
            }

            @Override
            public void onStartSearch() {

            }
        });
        return mNaviTopSearchView;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mPageType = bundle.getString(SPUtils.EXTRA_TYPE);
            netDataId = bundle.getLong(SPUtils.EXTRA_ID);
            mPageTitle = bundle.getString(SPUtils.EXTRA_TITLE);
        }
        mNaviTopSearchView.setTitleText(mPageTitle);
        mLineTopSearchView.setPageTitle(mPageTitle);

        mLineController = new LineController(this, mHandler);

//        mHeaderView = View.inflate(mContext, R.layout.footer_line_list_empty_view, null);
        addHeaderView(getBaseDropListView().getPullRefreshView().getRefreshableView());

        if (ItemType.FREE_LINE.equals(mPageType) || ItemType.TOUR_LINE.equals(mPageType) ||
                ItemType.FREE_LINE_ABOARD.equals(mPageType) || ItemType.TOUR_LINE_ABOARD.equals(mPageType)) {
            // TODO: 16/3/22  跟团游自由行
            judgeTheFreePackCityName();
        } else if (ItemType.CITY_ACTIVITY.equals(mPageType) || ItemType.ARROUND_FUN.equals(mPageType)) {
            // TODO: 16/3/22 同城  周边
            judgeTheLocalArroundCityName();
        }

        getBaseDropListView().getPullRefreshView().setRefreshing(false);//fetch the data from network
    }

    @Override
    public void addViewAboveDrop(LinearLayout dropViewParent) {
        super.addViewAboveDrop(dropViewParent);
        mLineTopSearchView = new LineTopSearchView(this);
        mLineTopSearchView.setSearchViewClickListener(mOnClickListener);
        dropViewParent.addView(mLineTopSearchView, 0);
    }

    @Override
    public List<String> setTabStrings() {
        return null;
    }

    @Override
    public List<View> setPopViews() {
        return null;
    }

    /**
     * 加载资源位列表
     */
    private void loadResourcesList() {
        if (ItemType.TOUR_LINE.equals(mPageType) || ItemType.TOUR_LINE_ABOARD.equals(mPageType)) {
            mLineController.doGetPackageTourResourceList(LineActivity.this);
        } else if (ItemType.FREE_LINE.equals(mPageType) || ItemType.FREE_LINE_ABOARD.equals(mPageType)) {
            mLineController.doGetFreeWalkResourceList(LineActivity.this);
        } else if (ItemType.ARROUND_FUN.equals(mPageType)) {
            mLineController.doGetArroundFunResourceList(LineActivity.this);
        } else if (ItemType.CITY_ACTIVITY.equals(mPageType)) {
            mLineController.doGetCityActivityResourceList(LineActivity.this);
        }
    }

    private void addHeaderView(ListView listView) {
        mLineHeaderView = new LineHeaderView(this);
        listView.addHeaderView(mLineHeaderView);
        mLineHeaderView.setPageTitle(mPageTitle);
        mLineHeaderView.setPageType(mPageType);
//        listView.addHeaderView(mHeaderView);
    }

    @Override
    public int setItemLayout() {
        return R.layout.item_home_recommend;
    }

    @Override
    public void convertItem(BaseAdapterHelper helper, ShortItem item) {
        if(mHasNodata){
            helper.setVisible(R.id.item_recommend_nodata_layout, true)
            .setVisible(R.id.item_recommend_layout, false)
            .setText(R.id.item_recommend_nodata_text, getString(R.string.label_nodata_wonderfulplay_list_message));
        }else {
            helper.setVisible(R.id.item_recommend_nodata_layout, false)
                    .setVisible(R.id.item_recommend_layout, true);
            MasterViewHelper.handleLineItem(this, helper, item, mPageType);
        }
    }

    @Override
    public void fetchData(int pageIndex) {
        // TODO: 16/3/2 从网络获取数据
        if (1 == pageIndex) {
            loadResourcesList();
        }
//        String type;
//        if (ItemType.TOUR_LINE.equals(mPageType)) {
//            type = mPageType + "," + ItemType.TOUR_LINE_ABOARD;
//        } else if (ItemType.FREE_LINE.equals(mPageType)) {
//            type = mPageType + "," + ItemType.FREE_LINE_ABOARD;
//        }else{
//            type = mPageType;
//        }
        String latitude = SPUtils.getExtraCurrentLat(getApplicationContext());
        String longtitude = SPUtils.getExtraCurrentLon(getApplicationContext());
        mLineController.doGetLineRecomandList(LineActivity.this,
                TextUtils.isEmpty(latitude) ? 0 : Double.parseDouble(latitude),
                TextUtils.isEmpty(longtitude) ? 0 : Double.parseDouble(longtitude),
                mPageType,
                mCityCode,
                pageIndex,
                ValueConstants.PAGESIZE);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int headerCount = getBaseDropListView().getPullRefreshView().getRefreshableView().getHeaderViewsCount();
        if (position >= headerCount) {
            ShortItem greatRecommend = (ShortItem) getBaseDropListView().getQuickAdapter().getItem(position - headerCount);
            NavUtils.gotoProductDetail(LineActivity.this, greatRecommend.itemType, greatRecommend.id,
                    greatRecommend.title);
        }
    }

    @Override
    public void dispatchMessage(Message msg) {
        hideLoadingView();
        hideErrorView(getBaseDropListView().getListViewViewParent());
        getBaseDropListView().getPullRefreshView().onRefreshComplete();
        switch (msg.what) {
            case ValueConstants.MSG_GET_PACKAGE_TOUR_HOME_PAGE_RESOURCE_LIST_OK:
                mLineHeaderView.handlePackageTourResouceList((BoothList) msg.obj);
                break;
            case ValueConstants.MSG_LINE_HOME_PAGE_RECOMMAND_LIST_KO:
                if(getBaseDropListView().getQuickAdapter().getCount() == 0) {
                    showNetErrorView(getBaseDropListView().getListViewViewParent(), msg.arg1);
                }else{
                    ToastUtil.showToast(this, StringUtil.handlerErrorCode(getApplicationContext(), msg.arg1));
                }
                break;
            case ValueConstants.MSG_GET_FREE_WALK_HOME_PAGE_RESOURCE_LIST_OK:
                mLineHeaderView.handleFreeWalkResouceList((BoothList) msg.obj);
                break;
            case ValueConstants.MSG_GET_CITY_ACTIVITY_HOME_PAGE_RESOURCE_LIST_OK:
                mLineHeaderView.handleCityActivityResouceList((BoothList) msg.obj);
                break;
            case ValueConstants.MSG_GET_ARROUND_FUN_HOME_PAGE_RESOURCE_LIST_OK:
                mLineHeaderView.handleArroundFunResouceList((BoothList) msg.obj);
                break;
            case ValueConstants.MSG_LINE_HOME_PAGE_RECOMMAND_LIST_OK:
                ShortItemsResult shortItemsResult = (ShortItemsResult) msg.obj;
                if(shortItemsResult != null){
//                    setHaxNext(shortItemsResult.hasNext);
                    handleLineRecommandList(shortItemsResult);
                }else{

                }
                break;
            case ValueConstants.MSG_HAS_NO_DATA:
                ToastUtil.showToast(this, getResources().getString(R.string.scenic_hasnodata));
                break;
        }
    }

    /**
     * 处理线路首页推荐数据
     *
     * @param list
     */
    private void handleLineRecommandList(ShortItemsResult list) {
        if (isRefresh()) {
            if(list.shortItemList != null) {
                getBaseDropListView().getQuickAdapter().replaceAll(list.shortItemList);
            }else{
                getBaseDropListView().getQuickAdapter().clear();
            }
            if(getBaseDropListView().getQuickAdapter().getCount() == 0){
                mHasNodata = true;
                getBaseDropListView().getQuickAdapter().add(new ShortItem());
            }else{
                mHasNodata = false;
            }

        } else {
            mHasNodata = false;
            if(list.shortItemList != null) {
                getBaseDropListView().getQuickAdapter().addAll(list.shortItemList);
            }else{
                ToastUtil.showToast(this, getString(R.string.no_more));
            }
        }
    }

    private LineTopSearchView.OnSearchClickListener mOnClickListener = new LineTopSearchView.OnSearchClickListener() {
        @Override
        public void onDestSelectClick() {

            if(ItemType.TOUR_LINE.equals(mPageType) || ItemType.FREE_LINE.equals(mPageType)){
                NavUtils.gotoDestinationSelectActivity(LineActivity.this,
                        ItemType.LINE,
                        mPageType,
                        mPageTitle,
                        LineActivity.class.getSimpleName(),
                        RequestCodeValues.REQUEST_LINE_DEST_CITY_SELECT);
            }else {
                if(ItemType.CITY_ACTIVITY.equals(mPageType) || ItemType.ARROUND_FUN.equals(mPageType)){
                    NavUtils.gotoDestinationSelectActivity(LineActivity.this,
                            ItemType.URBAN_LINE,
                            mPageType,
                            mPageTitle,
                            LineActivity.class.getSimpleName(),
                            RequestCodeValues.REQUEST_LINE_DEST_CITY_SELECT);
                }
            }

        }

        @Override
        public void onSearchTextViewClick() {
            if (ItemType.TOUR_LINE.equals(mPageType) || ItemType.FREE_LINE.equals(mPageType) ||
                    ItemType.TOUR_LINE_ABOARD.equals(mPageType) || ItemType.FREE_LINE_ABOARD.equals(mPageType)) {
                NavUtils.gotoDestinationSelectActivity(LineActivity.this,
                        ItemType.LINE,
                        mPageType,
                        mPageTitle,
                        LineActivity.class.getSimpleName(),
                        RequestCodeValues.REQUEST_LINE_DEST_CITY_SELECT);
                if (ItemType.TOUR_LINE.equals(mPageType) || ItemType.TOUR_LINE_ABOARD.equals(mPageType)) {
                    SPUtils.setIsJumpFromHomeSearch(getApplicationContext(), false);
                    TCEventHelper.onEvent(LineActivity.this, AnalyDataValue.TC_ID_SEARCH, AnalyDataValue.SEARCH_CLICK_PACKAGE_TOUR);
                } else {
                    TCEventHelper.onEvent(LineActivity.this, AnalyDataValue.TC_ID_SEARCH, AnalyDataValue.SEARCH_CLICK_FREE_WALK);
                }
            } else if (ItemType.CITY_ACTIVITY.equals(mPageType) || ItemType.ARROUND_FUN.equals(mPageType)) {
                NavUtils.gotoSearchActivity(LineActivity.this,
                        mPageType,
                        null,
                        mPageTitle,
                        -1);
                if (ItemType.CITY_ACTIVITY.equals(mPageType)) {
                    TCEventHelper.onEvent(LineActivity.this, AnalyDataValue.TC_ID_SEARCH, AnalyDataValue.SEARCH_CLICK_CITY_ACTIVITY);
                } else {
                    TCEventHelper.onEvent(LineActivity.this, AnalyDataValue.TC_ID_SEARCH, AnalyDataValue.SEARCH_CLICK_CITY_ARROUND_FUN);
                }
            }
        }
    };

    /**
     * 跟团游，自由行城市判断
     */
    private void judgeTheFreePackCityName() {
        String packFreeTtipCityName;
        String packFreeCityCode;
        if (ItemType.FREE_LINE.equals(mPageType)) {
            packFreeTtipCityName = SPUtils.getFreeTripCityName(getApplicationContext());
            packFreeCityCode = SPUtils.getFreeTripCityCode(getApplicationContext());
        } else {
            packFreeTtipCityName = SPUtils.getPackTripCityName(getApplicationContext());
            packFreeCityCode = SPUtils.getPackTripCityCode(getApplicationContext());
        }
        mNaviTopSearchView.showDepartureSearch(true);
        mLineTopSearchView.showFreePackStyle();

        if (!TextUtils.isEmpty(packFreeTtipCityName)) {
            // TODO: 16/3/21 之前选择过城市
            mNaviTopSearchView.setStartCtiyName(packFreeTtipCityName);
            mCityCode = packFreeCityCode;
        } else {
            // TODO: 16/3/21 默认昆明活着第一次进入
//            String locateCity = SPUtils.getExtraCurrentCityName(getApplicationContext());
//            if (!TextUtils.isEmpty(locateCity)) {
//                // TODO: 16/3/22 判断是否定位成功，有没有定位到的地址，未定位成功 重新定位
//                mNaviTopSearchView.setStartCtiyName(locateCity);
//                mCityCode = ((YHYApplication) getApplication()).getCityCode(locateCity);
//            } else {
//                if (SPUtils.getHomeCityIsChange(getApplicationContext())) {
//                    mNaviTopSearchView.setStartCtiyName(SPUtils.getHomeChangeCityName(getApplicationContext()));
//                    mCityCode = ((YHYApplication) getApplication()).getCityCode(
//                            SPUtils.getHomeChangeCityName(getApplicationContext())
//                    );
//                } else {
                    mNaviTopSearchView.setStartCtiyName(DefaultCityBean.cityName);
                    mCityCode = LocationManager.getInstance().getStorage().getLast_cityCode();
//                }
//            }

        }
    }

    /**
     * 同城周边城市判断
     */
    private void judgeTheLocalArroundCityName() {
        String localArroundCityName;
        String localArroundCityCode;
        if (ItemType.CITY_ACTIVITY.equals(mPageType)) {
            localArroundCityName = SPUtils.getLocalCityName(getApplicationContext());
            localArroundCityCode = SPUtils.getLocalCityCode(getApplicationContext());
        } else {
            localArroundCityName = SPUtils.getArroundCityName(getApplicationContext());
            localArroundCityCode = SPUtils.getArroundCityCode(getApplicationContext());
        }
        List<AddressBean> defaultCityArray = new ArrayList<>();

        if (!TextUtils.isEmpty(localArroundCityName)) {
            // TODO: 16/3/21 之前选择过城市
            mLineTopSearchView.setCityName(localArroundCityName);
            mCityCode = localArroundCityCode;
            return;
        } else {
            // TODO: 16/3/21 默认昆明活着第一次进入
            String locationName = SPUtils.getExtraCurrentCityName(getApplicationContext());
            if (!TextUtils.isEmpty(locationName)) {
                if (defaultCityArray != null) {
                    // TODO: 16/3/17 是否在16个默认城市列表中，（true ：显示传入的目的地；false ： 显示上次选择的目的地活着默认的目的地）
                    for (AddressBean addressBean : defaultCityArray) {
                        if (locationName.equals(addressBean.getName())) {
                            mLineTopSearchView.setCityName(locationName);
                            mCityCode = LocationManager.getInstance().getStorage().getLast_cityCode();
                            if (ItemType.ARROUND_FUN.equals(mPageType)) {
                                SPUtils.setArroundCityName(getApplicationContext(), addressBean.getName());
                                SPUtils.setArroundCityCode(getApplicationContext(), addressBean.getCityCode());
                                SPUtils.setArroundCityChange(getApplicationContext(), false);
                            } else {
                                SPUtils.setLocalCityName(getApplicationContext(), addressBean.getName());
                                SPUtils.setLocalCityCode(getApplicationContext(), addressBean.getCityCode());
                                SPUtils.setLocalCityChange(getApplicationContext(), false);
                            }
                            return;
                        }
                    }
                }
            } else if (SPUtils.getHomeCityIsChange(getApplicationContext())) {
                String homeChangecity = SPUtils.getHomeChangeCityName(getApplicationContext());
                if (!TextUtils.isEmpty(homeChangecity) && defaultCityArray != null) {
                    // TODO: 16/3/17 是否在16个默认城市列表中，（true ：显示传入的目的地；false ： 显示上次选择的目的地活着默认的目的地）
                    for (AddressBean addressBean : defaultCityArray) {
                        if (homeChangecity.equals(addressBean.getName())) {
                            mLineTopSearchView.setCityName(homeChangecity);
                            mCityCode = LocationManager.getInstance().getStorage().getLast_cityCode();
                            if (ItemType.ARROUND_FUN.equals(mPageType)) {
                                SPUtils.setArroundCityName(getApplicationContext(), addressBean.getName());
                                SPUtils.setArroundCityCode(getApplicationContext(), addressBean.getCityCode());
                                SPUtils.setArroundCityChange(getApplicationContext(), false);
                            } else {
                                SPUtils.setLocalCityName(getApplicationContext(), addressBean.getName());
                                SPUtils.setLocalCityCode(getApplicationContext(), addressBean.getCityCode());
                                SPUtils.setLocalCityChange(getApplicationContext(), false);
                            }
                            return;
                        }
                    }
                }
            }
        }
        mLineTopSearchView.setCityName(DefaultCityBean.cityName);
        mCityCode = LocationManager.getInstance().getStorage().getLast_cityCode();
        if (ItemType.ARROUND_FUN.equals(mPageType)) {
            SPUtils.setArroundCityName(getApplicationContext(), DefaultCityBean.cityName);
            SPUtils.setArroundCityCode(getApplicationContext(), mCityCode);
            SPUtils.setArroundCityChange(getApplicationContext(), false);
        } else {
            SPUtils.setLocalCityName(getApplicationContext(), DefaultCityBean.cityName);
            SPUtils.setLocalCityCode(getApplicationContext(), mCityCode);
            SPUtils.setLocalCityChange(getApplicationContext(), false);
        }
    }

}
