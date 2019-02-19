package com.quanyan.yhy.ui.line;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.quanyan.base.BaseListViewActivity;
import com.quanyan.base.baseenum.IActionTitleBar;
import com.quanyan.base.util.StringUtil;
import com.quanyan.base.view.customview.dropdownview.DropDownMenu;
import com.quanyan.base.yminterface.ErrorViewClick;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.ErrorCode;
import com.quanyan.yhy.common.ItemType;
import com.quanyan.yhy.common.MerchantType;
import com.quanyan.yhy.common.QueryType;
import com.quanyan.yhy.ui.adapter.base.BaseAdapterHelper;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.line.lineinterface.DropMenuInterface;
import com.quanyan.yhy.ui.line.view.LineTopSearchView;
import com.quanyan.yhy.ui.line.view.NaviTopSearchView;
import com.quanyan.yhy.ui.line.view.TabPopView;
import com.quanyan.yhy.ui.master.helper.MasterViewHelper;
import com.yhy.common.beans.city.bean.AddressBean;
import com.yhy.common.beans.net.model.master.QueryTerm;
import com.yhy.common.beans.net.model.master.QueryTermsDTO;
import com.yhy.common.beans.net.model.trip.ShortItem;
import com.yhy.common.beans.net.model.trip.ShortItemsResult;
import com.yhy.common.constants.RequestCodeValues;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.SPUtils;
import com.yhy.location.LocationManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created with Android Studio.
 * Title:AboutAndFeedController
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:zhaoxiaopo
 * Date:16/3/4
 * Time:15:06
 * Version 1.0
 */
public class LineSearchResultActivity extends BaseListViewActivity<ShortItem> {
    private List<View> mTabViews;

    private LineController mController;

    private NaviTopSearchView mNaviTopSearchView;
    private String mPageTitle = "";
    //线路搜索条件
    private QueryTermsDTO mLineSearchQuery;
    //关键字
    private String mSearchWord;
    //城市代码
    private String mCityCode;
    //城市名称
    private String mCityName;
    //标签名字
    private String mTagName;
    //标签id
    private long mTagId = -1;
    //线路类型
    private String mLineType;

    private Map<String, String> mSearchParams = new HashMap<String, String>();
    private boolean isHasFilter = false;//标识是否已经获取到筛选条件

    /**
     * 跳转关键字搜索结果页
     *
     * @param context
     * @param title
     * @param searchWord
     * @param cityCode
     * @param tagName
     * @param tagId
     */
    public static void gotoLineSearchActivity(Context context,
                                              String title,
                                              String lineType,
                                              String searchWord,
                                              String cityCode,
                                              String cityName,
                                              String tagName,
                                              long tagId) {
        Intent intent = new Intent(context, LineSearchResultActivity.class);
        Bundle bundle = new Bundle();
        if (!StringUtil.isEmpty(searchWord)) {
            bundle.putString(SPUtils.EXTRA_DATA, searchWord);
        }
        if (!StringUtil.isEmpty(lineType)) {
            bundle.putString(SPUtils.EXTRA_TYPE, lineType);
        }
        if (!StringUtil.isEmpty(cityCode)) {
            bundle.putString(SPUtils.EXTRA_CURRENT_CITY_CODE, cityCode);
        }
        if (!StringUtil.isEmpty(cityName)) {
            bundle.putString(SPUtils.EXTRA_CURRENT_CITY_NAME, cityName);
        }
        if (tagId > 0) {
            bundle.putLong(SPUtils.EXTRA_TAG_ID, tagId);
        }
        if (!StringUtil.isEmpty(tagName)) {
            bundle.putString(SPUtils.EXTRA_TAG_NAME, tagName);
        }
        if (!StringUtil.isEmpty(title)) {
            bundle.putString(SPUtils.EXTRA_TITLE, title);
        }
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (getBaseDropListView().getDropDownMenu() != null) {
            getBaseDropListView().getDropDownMenu().closeMenu();
        }
    }

    @Override
    public boolean isTopCover() {
        return false;
    }

    @Override
    public List<String> setTabStrings() {
        return null;
    }

    @Override
    public List<View> setPopViews() {
        return null;
    }

    private LineTopSearchView mLineTopSearchView;

    @Override
    public View onLoadNavView() {
        mNaviTopSearchView = new NaviTopSearchView(this);
        mNaviTopSearchView.setOnNavTopListener(new NaviTopSearchView.OnNavTopClickListener() {
            @Override
            public void onStartCitySelect() {
                NavUtils.gotoSelectCity(LineSearchResultActivity.this, RequestCodeValues.REQUEST_LINE_STARTCITY_CHOOSE);
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
            mPageTitle = bundle.getString(SPUtils.EXTRA_TITLE);
            mSearchWord = bundle.getString(SPUtils.EXTRA_DATA);
            mCityCode = bundle.getString(SPUtils.EXTRA_CURRENT_CITY_CODE);
            mCityName = bundle.getString(SPUtils.EXTRA_CURRENT_CITY_NAME);
            mTagId = bundle.getLong(SPUtils.EXTRA_TAG_ID);
            mTagName = bundle.getString(SPUtils.EXTRA_TAG_NAME);
            mLineType = bundle.getString(SPUtils.EXTRA_TYPE);
        }
        mNaviTopSearchView.setTitleText(mPageTitle);

        if (!StringUtil.isEmpty(mSearchWord)) {
            mLineTopSearchView.setSearchKeyWord(mSearchWord);
        }
        if (!StringUtil.isEmpty(mPageTitle)) {
            mNaviTopSearchView.setTitleText(mPageTitle);
        }
        if (!StringUtil.isEmpty(mCityName)) {
            mLineTopSearchView.setCityName(mCityName);
        } else {
            mLineTopSearchView.setCityName(getString(R.string.label_default_china_go));
        }

        mLineSearchQuery = new QueryTermsDTO();
        mController = new LineController(this, mHandler);

        if (!StringUtil.isEmpty(SPUtils.getExtraCurrentLat(this))) {
            mLineSearchQuery.latitude = Double.parseDouble(SPUtils.getExtraCurrentLat(this));
        }
        if (!StringUtil.isEmpty(SPUtils.getExtraCurrentLon(this))) {
            mLineSearchQuery.longitude = Double.parseDouble(SPUtils.getExtraCurrentLon(this));
        }

        mLineSearchQuery.pageNo = getPageIndex();
        mLineSearchQuery.pageSize = ValueConstants.PAGESIZE;

        if (!StringUtil.isEmpty(mSearchWord)) {
            mSearchParams.put(QueryType.TITLE, mSearchWord);
        }
        if (!StringUtil.isEmpty(mCityCode)) {
            mSearchParams.put(QueryType.DEST_CITY, mCityCode);
        }


        if (ItemType.FREE_LINE.equals(mLineType) || ItemType.FREE_LINE_ABOARD.equals(mLineType)) {
            String freeCityName = SPUtils.getFreeTripCityName(getApplicationContext());
            mSearchParams.put(QueryType.START_CITY, LocationManager.getInstance().getStorage().getLast_cityCode());
            mSearchParams.put(QueryType.ITEM_TYPE, ItemType.FREE_LINE + "," + ItemType.FREE_LINE_ABOARD);
            mSearchParams.put(QueryType.SELLER_TYPE, MerchantType.MERCHANT);
        } else if (ItemType.TOUR_LINE.equals(mLineType) || ItemType.TOUR_LINE_ABOARD.equals(mLineType)) {
            String packCityName = SPUtils.getPackTripCityName(getApplicationContext());
            String homeCityName = SPUtils.getExtraCurrentCityName(getApplicationContext());
            if (SPUtils.isJumpFromHomeSearch(getApplicationContext())) {
                if (SPUtils.getHomeCityIsChange(getApplicationContext())) {
                    mSearchParams.put(QueryType.START_CITY, LocationManager.getInstance().getStorage().getLast_cityCode());
                } else {
                    mSearchParams.put(QueryType.START_CITY, LocationManager.getInstance().getStorage().getLast_cityCode());
                }
            } else {
                mSearchParams.put(QueryType.START_CITY, LocationManager.getInstance().getStorage().getLast_cityCode());
            }
            mSearchParams.put(QueryType.ITEM_TYPE, ItemType.TOUR_LINE + "," + ItemType.TOUR_LINE_ABOARD);
            mSearchParams.put(QueryType.SELLER_TYPE, MerchantType.MERCHANT);
        } else if (ItemType.CITY_ACTIVITY.equals(mLineType)) {
            mSearchParams.put(QueryType.ITEM_TYPE, ItemType.CITY_ACTIVITY);
            mSearchParams.put(QueryType.SELLER_TYPE, MerchantType.MERCHANT);
        } else if (ItemType.ARROUND_FUN.equals(mLineType)) {
            mSearchParams.put(QueryType.ITEM_TYPE, ItemType.FREE_LINE + "," + ItemType.TOUR_LINE + "," + ItemType.FREE_LINE_ABOARD + "," + ItemType.TOUR_LINE_ABOARD);
            mSearchParams.put(QueryType.SELLER_TYPE, MerchantType.MERCHANT);
        } else if (ItemType.MASTER_PRODUCTS.equals(mLineType)) {
            mSearchParams.put(QueryType.ITEM_TYPE, ItemType.TOUR_LINE);
            mSearchParams.put(QueryType.SELLER_TYPE, MerchantType.TALENT);
            if (SPUtils.getHomeCityIsChange(getApplicationContext())) {
                String cityName = SPUtils.getHomeChangeCityName(getApplicationContext());
                mSearchParams.put(QueryType.START_CITY, LocationManager.getInstance().getStorage().getLast_cityCode());
            } else {
                String homeCityName = SPUtils.getExtraCurrentCityName(getApplicationContext());
                mSearchParams.put(QueryType.START_CITY, LocationManager.getInstance().getStorage().getLast_cityCode());
            }
        }

        if (mTagId > 0) {
            mSearchParams.put(QueryType.SUBJECT, String.valueOf(mTagId));
        }

        mController.doGetLineFilter(LineSearchResultActivity.this,mLineType);
    }

    @Override
    public void addViewAboveDrop(LinearLayout dropViewParent) {
        super.addViewAboveDrop(dropViewParent);
        mLineTopSearchView = new LineTopSearchView(this);
        mLineTopSearchView.setSearchViewClickListener(mOnClickListener);
        dropViewParent.addView(mLineTopSearchView, 0);
    }

    @Override
    public int setItemLayout() {
        return R.layout.item_home_recommend;
    }

    @Override
    public void convertItem(BaseAdapterHelper helper, ShortItem item) {
        MasterViewHelper.handleLineItem(this, helper, item, mLineType);
    }

    /***
     *
     * @param pageIndex
     */
    @Override
    public void fetchData(int pageIndex) {
        // TODO: 16/3/8 从网络获取数据
        startSearchLineList();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int headerCount = getBaseDropListView().getPullRefreshView().getRefreshableView().getHeaderViewsCount();
        if (position >= headerCount) {
            ShortItem shortItem = (ShortItem) getBaseDropListView().getQuickAdapter().getItem(position - headerCount);
            NavUtils.gotoProductDetail(this, shortItem.itemType, shortItem.id,
                    shortItem.title);
        }
    }

    private List<QueryTerm> mLineFilter;

    @Override
    public void dispatchMessage(Message msg) {
        hideLoadingView();
        hideErrorView(getBaseDropListView().getListViewViewParent());
        getBaseDropListView().getPullRefreshView().onRefreshComplete();
        switch (msg.what) {
            case ValueConstants.MSG_GET_LINE_FILTER_OK:
                bindFilterView((QueryTerm) msg.obj);
                break;
            case ValueConstants.MSG_GET_LINE_FILTER_ERROR:
            case ValueConstants.MSG_MASTER_SEARCH_KO:
                setPageIndex(getPageIndex() - 1);
                if (getBaseDropListView().getQuickAdapter().getCount() == 0) {
                    showNetErrorView(getBaseDropListView().getListViewViewParent(), msg.arg1);
                } else {
                    ToastUtil.showToast(this, StringUtil.handlerErrorCode(getApplicationContext(), msg.arg1));
                }
                break;
            case ValueConstants.MSG_MASTER_SEARCH_OK:
                handleLineSearchResult((ShortItemsResult) msg.obj);
                break;
            case ValueConstants.MSG_HAS_NO_DATA:
                ToastUtil.showToast(this, getResources().getString(R.string.scenic_hasnodata));
                break;
            default:
                break;
        }
    }

    /**
     * 绑定筛选条件
     *
     * @param list
     */
    private void bindFilterView(QueryTerm list) {
        isHasFilter = true;
        if (list == null || list.queryTermList == null || list.queryTermList.size() == 0) {
            return;
        }
        mLineFilter = list.queryTermList;
        List<String> tabStrings = new ArrayList<>(list.queryTermList.size());
        mTabViews = new ArrayList<>(list.queryTermList.size());
        for (int num = 0; num < list.queryTermList.size(); num++) {
            QueryTerm t = list.queryTermList.get(num);
            tabStrings.add(t.text);
            if (t.queryTermList != null && t.queryTermList.size() > 0) {
                if (!StringUtil.isEmpty(t.queryTermList.get(0).type)
                        && !StringUtil.isEmpty(t.queryTermList.get(0).value)
                        && !mSearchParams.containsKey(t.queryTermList.get(0).type)) {
                    mSearchParams.put(t.queryTermList.get(0).type, t.queryTermList.get(0).value);
                }
            }
            TabPopView tabPopView = new TabPopView(getApplicationContext());
            tabPopView.bindViewData(t.queryTermList, mTagId);
            tabPopView.setOnItemClickInterface(new DropViewItemClickListener(num));
            mTabViews.add(tabPopView);
        }
        getBaseDropListView().getDropDownMenu().setDropDownMenu(tabStrings, mTabViews);
        if (!StringUtil.isEmpty(mTagName)) {
            getBaseDropListView().getDropDownMenu().setTabText(0, mTagName);
        }
        getBaseDropListView().getDropDownMenu().setDropMenuClose(new DropDownMenu.DropMenuClose() {
            @Override
            public void menuClose(int position, LinearLayout linearLayout) {
                if(isSencondSelected){
                    ((TabPopView)mTabViews.get(position)).performSencondItemClick(0);
                }
            }

            @Override
            public void menuOpen(int position, LinearLayout linearLayout) {

            }
        });
        manualRefresh();
    }

    /**
     * 处理线路搜索结果
     *
     * @param result
     */
    private void handleLineSearchResult(ShortItemsResult result) {
        if (result != null) {
            setHaxNext(result.hasNext);
            if (isRefresh()) {
                if (result.shortItemList != null) {
                    getBaseDropListView().getQuickAdapter().replaceAll(result.shortItemList);
                } else {
                    getBaseDropListView().getQuickAdapter().clear();
                }
                if (getBaseDropListView().getQuickAdapter().getCount() == 0) {
                    showNoDataPageView();
                }
            } else {
                if (result.shortItemList != null) {
                    getBaseDropListView().getQuickAdapter().addAll(result.shortItemList);
                }else{
                    ToastUtil.showToast(this, getString(R.string.no_more));
                }
            }
        }
    }

    /**
     * 展示无数据的提示
     */
    private void showNoDataPageView() {
        showErrorView(getBaseDropListView().getListViewViewParent(), IActionTitleBar.ErrorType.EMPTYVIEW, getString(R.string.label_no_products_searched), " ", "", null);
    }

    /**
     * 开始搜索
     */
    private void startSearchLineList() {
        if(1 == getPageIndex()){
            getBaseDropListView().getListView().setSelection(0);
        }
        mLineSearchQuery.pageNo = getPageIndex();
        List<QueryTerm> terms = new ArrayList<>();
        Iterator iter = mSearchParams.entrySet().iterator();
        while (iter.hasNext()) {
            QueryTerm term = new QueryTerm();
            Map.Entry entry = (Map.Entry) iter.next();
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            term.type = key;
            term.value = value;
            terms.add(term);
        }
        mLineSearchQuery.queryTerms = terms;
        mController.doSearchLineListSearch(LineSearchResultActivity.this,mLineSearchQuery);
    }

    private boolean isSencondSelected = false;//有二级列表时，若只选择了一级菜单，则自动选中二级菜单的第一天数据，在closeMenu的时候判断

    /**
     * 筛选列表点击事件
     */
    private class DropViewItemClickListener implements DropMenuInterface<QueryTerm> {

        private int mTabPosition;

        public DropViewItemClickListener(int pos) {
            this.mTabPosition = pos;
        }

        @Override
        public void onFirstItemSelect(QueryTerm data) {
            mSearchParams.put(data.type, data.value);
            if (ItemType.MASTER_PRODUCTS.equals(mLineType)) {
                if(mSearchParams.containsValue(ItemType.CITY_ACTIVITY)){
                    mSearchParams.remove(QueryType.START_CITY);
                }else{
                    if (SPUtils.getHomeCityIsChange(getApplicationContext())) {
                        String cityName = SPUtils.getHomeChangeCityName(getApplicationContext());
                        mSearchParams.put(QueryType.START_CITY, LocationManager.getInstance().getStorage().getLast_cityCode());
                    } else {
                        String homeCityName = SPUtils.getExtraCurrentCityName(getApplicationContext());
                        mSearchParams.put(QueryType.START_CITY, LocationManager.getInstance().getStorage().getLast_cityCode());
                    }
                }
            }
            if (!data.hasChild) {
                getBaseDropListView().getDropDownMenu().setTabText(data.text);
                manualRefresh();
                getBaseDropListView().getDropDownMenu().closeMenu();
            }else{
                isSencondSelected = true;
            }
        }

        @Override
        public void onSecondItemSelect(QueryTerm data) {
            //选择后设置TAB内容，setTabText必须在closeMenu之前调用，否则会失效
            isSencondSelected = false;
            mSearchParams.put(data.type, data.value);
            if (!data.hasChild) {
                getBaseDropListView().getDropDownMenu().setTabText(data.text);
//            startSearchLineList();
                manualRefresh();
                getBaseDropListView().getDropDownMenu().closeMenu();
            }
        }
    }

    private LineTopSearchView.OnSearchClickListener mOnClickListener = new LineTopSearchView.OnSearchClickListener() {
        @Override
        public void onDestSelectClick() {
            if(ItemType.TOUR_LINE.equals(mLineType) || ItemType.FREE_LINE.equals(mLineType)){
                NavUtils.gotoDestinationSelectActivity(LineSearchResultActivity.this,
                        ItemType.LINE,
                        mLineType,
                        mPageTitle,
                        null,
                        RequestCodeValues.REQUEST_LINE_DEST_CITY_SELECT);
            }else {
//                if(ItemType.CITY_ACTIVITY.equals(mLineType) || ItemType.ARROUND_FUN.equals(mLineType)){
                    NavUtils.gotoDestinationSelectActivity(LineSearchResultActivity.this,
                            ItemType.URBAN_LINE,
                            mLineType,
                            mPageTitle,
                            null,
                            RequestCodeValues.REQUEST_LINE_DEST_CITY_SELECT);
//                }
            }
        }

        @Override
        public void onSearchTextViewClick() {
            NavUtils.gotoSearchActivity(LineSearchResultActivity.this,
                    mLineType,
                    LineSearchResultActivity.class.getSimpleName(),
                    mPageTitle,
                    RequestCodeValues.REQUEST_LINE_SEARCH_KEY_WORD);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            switch (requestCode) {
                case RequestCodeValues.REQUEST_LINE_STARTCITY_CHOOSE: {
                    //选择出发地
                    if (data != null) {
                        AddressBean addressBean = (AddressBean) data.getSerializableExtra(SPUtils.EXTRA_SELECT_CITY);
                        if (addressBean != null) {
                            mSearchParams.put(QueryType.START_CITY, LocationManager.getInstance().getStorage().getLast_cityCode());
                            showLoadingView(getString(R.string.dlg_msg_searching));
//                            startSearchLineList();
                            manualRefresh();
                        }
                    }
                    break;
                }
                case RequestCodeValues.REQUEST_LINE_SEARCH_KEY_WORD:
                    //TODO 关键字刷新
                    String keyWord = data.getStringExtra(SPUtils.EXTRA_DATA);
                    if (!StringUtil.isEmpty(keyWord)) {
                        mLineTopSearchView.setSearchKeyWord(keyWord);
                        mSearchParams.put(QueryType.TITLE, keyWord);
                        showLoadingView(getString(R.string.dlg_msg_searching));
//                        startSearchLineList();
                        manualRefresh();
                    }
                    break;
                case RequestCodeValues.REQUEST_LINE_DEST_CITY_SELECT:
                    // TODO: 16/3/21 目的地更新
                    if (data != null) {
                        AddressBean addressBean = (AddressBean) data.getSerializableExtra(SPUtils.EXTRA_SELECT_CITY);
                        if (addressBean != null) {
//                            if(ItemType.ARROUND_FUN.equals(mLineType)){
//                                SPUtils.setArroundCityName(getApplicationContext(), addressBean.getName());
//                                SPUtils.setArroundCityCode(getApplicationContext(), addressBean.getCityCode());
//                            }else if(ItemType.CITY_ACTIVITY.equals(mLineType)){
//                                SPUtils.setLocalCityName(getApplicationContext(), addressBean.getName());
//                                SPUtils.setLocalCityCode(getApplicationContext(), addressBean.getCityCode());
//                            }
                            mLineTopSearchView.setCityName(addressBean.getName());
                            mSearchParams.put(QueryType.DEST_CITY, addressBean.getCityCode());
                            showLoadingView(getString(R.string.dlg_msg_searching));
//                            startSearchLineList();
                            manualRefresh();
                        }
                    }
                    break;
            }
        }
    }

    /**
     * 展示错误页面
     *
     * @param arg1
     */
    protected void showNetErrorView(ViewGroup parent, int arg1) {
        showErrorView(parent, ErrorCode.NETWORK_UNAVAILABLE == arg1 ?
                IActionTitleBar.ErrorType.NETUNAVAILABLE : IActionTitleBar.ErrorType.ERRORNET, "", "", "", new ErrorViewClick() {
            @Override
            public void onClick(View view) {
                if (!isHasFilter) {
                    mController.doGetLineFilter(LineSearchResultActivity.this,mLineType);
                } else {
                    manualRefresh();
                }
                showLoadingView(getResources().getString(R.string.scenic_loading_notice));
            }
        });
    }
}
