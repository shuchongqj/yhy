package com.quanyan.yhy.ui.master.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.baseenum.IActionTitleBar;
import com.quanyan.base.util.LocalUtils;
import com.quanyan.base.util.ScreenSize;
import com.quanyan.base.util.StringUtil;
import com.quanyan.base.view.BaseTransparentNavView;
import com.quanyan.base.view.customview.dropdownview.DropDownMenu;
import com.quanyan.base.view.customview.pulltorefresh.PullToRefreshBase;
import com.quanyan.base.view.customview.pulltorefresh.PullToRefreshGridView;
import com.quanyan.base.yminterface.ErrorViewClick;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.AnalyDataValue;
import com.quanyan.yhy.common.ErrorCode;
import com.quanyan.yhy.common.ItemType;
import com.quanyan.yhy.common.QueryType;
import com.quanyan.yhy.eventbus.EvBusDestCityChoose;
import com.quanyan.yhy.ui.adapter.base.BaseAdapterHelper;
import com.quanyan.yhy.ui.adapter.base.QuickAdapter;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.base.utils.TCEventHelper;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.integralmall.integralmallviewhelper.IntegralmallViewHelper;
import com.quanyan.yhy.ui.line.lineinterface.DropMenuInterface;
import com.quanyan.yhy.ui.line.view.TabPopView;
import com.quanyan.yhy.ui.login.LoginActivity;
import com.quanyan.yhy.ui.master.controller.MasterController;
import com.quanyan.yhy.view.MasterAdviceRegionView;
import com.yhy.common.beans.net.model.master.QueryTerm;
import com.yhy.common.beans.net.model.master.QueryTermsDTO;
import com.yhy.common.beans.net.model.trip.ShortItem;
import com.yhy.common.beans.net.model.trip.ShortItemsResult;
import com.yhy.common.beans.net.model.user.DestinationList;
import com.yhy.common.constants.IntentConstants;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.AndroidUtils;
import com.yhy.common.utils.SPUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;


/**
 * Created with Android Studio.
 * Title:AboutAndFeedController
 * Description:全部咨询服务
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:wjm
 * Date:16/7/22
 * Version 2.0.0
 */
public class MasterAdviceListActivity extends BaseActivity implements DropDownMenu.DropMenuClose,
        View.OnClickListener,
        AdapterView.OnItemClickListener,
        PullToRefreshBase.OnRefreshListener<GridView> {
    MasterController mController;
    //    @ViewInject(R.id.layout_error_view)
//    private LinearLayout layout_error_view;
    @ViewInject(R.id.trasparent_topbar_title)
    private TextView mTopTitle;
    @ViewInject(R.id.trasparent_topbar_left)
    private ImageView mTopLeft;
    @ViewInject(R.id.trasparent_topbar_right)
    private ImageView mTopRightImg;

    private PullToRefreshGridView mBasePullrefreshScrollview;
    private QuickAdapter materialAdapter;
    private GridView mGridview;

    DropDownMenu mDropDownMenu;
    private LinearLayout mRefreshViewParent;
    private int pageIndex = 1;

    private static int PAGESIZE = 10;

    /**
     * 达人咨询列表跳转
     *
     * @param context 上下文对象
     */
    public static void gotoMasterAdviceListActivity(Context context) {
        Intent intent = new Intent(context, MasterAdviceListActivity.class);
        context.startActivity(intent);
    }


    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.base_drop_down_layout, null);
    }

    private BaseTransparentNavView mBaseTransparentNavView;

    @Override
    public View onLoadNavView() {
        mBaseTransparentNavView = new BaseTransparentNavView(this);
        return mBaseTransparentNavView;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ViewUtils.inject(this);
        EventBus.getDefault().register(this);

        mLineSearchQuery = new QueryTermsDTO();
        if (!StringUtil.isEmpty(SPUtils.getExtraCurrentLat(this))) {
            mLineSearchQuery.latitude = Double.parseDouble(SPUtils.getExtraCurrentLat(this));
        }
        if (!StringUtil.isEmpty(SPUtils.getExtraCurrentLon(this))) {
            mLineSearchQuery.longitude = Double.parseDouble(SPUtils.getExtraCurrentLon(this));
        }
        mLineSearchQuery.pageNo = pageIndex;
        mLineSearchQuery.pageSize = ValueConstants.PAGESIZE;

        mDropDownMenu = (DropDownMenu) findViewById(R.id.dropDownMenu);

        View contentView = View.inflate(this, R.layout.base_pullrefresh_gridview, null);

        mController = new MasterController(this, mHandler);
        //顶部栏
        mTopTitle.setVisibility(View.VISIBLE);
        mTopTitle.setText(R.string.master_advice_title);
        mTopTitle.setTextColor(getResources().getColor(R.color.color_norm_000000));
        mTopLeft.setImageResource(R.mipmap.arrow_back_gray);
        mTopRightImg.setVisibility(View.GONE);
        ((ImageView) findViewById(R.id.trasparent_topbar_right_share)).setVisibility(View.GONE);
//        View v = View.inflate(this, R.layout.activity_master_adcice, null);
        mRefreshViewParent = (LinearLayout) contentView.findViewById(R.id.base_pullrefresh_gridview_parent);
        mBasePullrefreshScrollview = (PullToRefreshGridView) contentView.findViewById(R.id.base_pullrefresh_gridview);
        mGridview = mBasePullrefreshScrollview.getRefreshableView();
        mGridview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case SCROLL_STATE_IDLE:
//                        ImageLoader.getInstance().resume();
                        break;
                    case SCROLL_STATE_TOUCH_SCROLL:
                    case SCROLL_STATE_FLING:
//                        ImageLoader.getInstance().pause();
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        mGridview.setNumColumns(2);
        mGridview.setPadding(ScreenSize.convertDIP2PX(getApplicationContext(), 10),
                0,
                ScreenSize.convertDIP2PX(getApplicationContext(), 10),
                0);
        mDropDownMenu.setDropDownMenu(null, null, contentView);
        //gridview
        mBasePullrefreshScrollview.setMode(PullToRefreshBase.Mode.BOTH);
        mBasePullrefreshScrollview.setOnRefreshListener(this);
        materialAdapter = new QuickAdapter<ShortItem>(MasterAdviceListActivity.this, R.layout.view_item_masterhome_consult, new ArrayList<ShortItem>()) {
            @Override
            protected void convert(BaseAdapterHelper helper, ShortItem item) {
                IntegralmallViewHelper.handleMasterAdviceListItem(MasterAdviceListActivity.this, helper, item);
            }
        };
        mGridview.setAdapter(materialAdapter);
        mGridview.setOnItemClickListener(this);

        showLoadingView(getString(R.string.loading_text));
        //顶部筛选列表 QueryType.ALL_CONSULTING_SERVICES

//        mController.doGetHotelFilter(MasterAdviceListActivity.this,QueryType.ALL_CONSULTING_SERVICES );
        startLoadData(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case IntentConstants.LOGIN_RESULT://成功登录
                if (RESULT_OK == resultCode) {
                }
                break;
        }
    }


    private QueryTermsDTO mLineSearchQuery;

    /**
     * 开始搜索列表
     */
    private void startLoadData(boolean isLoading) {

        if (isLoading) {
            showLoadingView(getString(R.string.loading_text));
            pageIndex = 1;
        } else {
            pageIndex++;
        }
        mSearchParams.put(QueryType.ITEM_TYPE,ItemType. CONSULT);
        mLineSearchQuery.pageNo = pageIndex;
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
        mController.getConsultItemList(MasterAdviceListActivity.this, mLineSearchQuery);
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (mDropDownMenu != null) {
            mDropDownMenu.closeMenu();
        }
    }

    @Override
    public void handleMessage(Message msg) {
        hideLoadingView();

        hideErrorView(mRefreshViewParent);
        mBasePullrefreshScrollview.onRefreshComplete();
        switch (msg.what) {
            case ValueConstants.MSG_INTEGRALMALL_LIST_OK://商品列表
                if (msg.obj != null) {
                    ShortItemsResult shortItemsResult = (ShortItemsResult) msg.obj;
                    if (shortItemsResult.shortItemList != null && shortItemsResult.shortItemList.size() > 0) {
                        if (pageIndex == 1) {
                            materialAdapter.replaceAll(shortItemsResult.shortItemList);
                            /*
                             *  http://andydyer.org/blog/2012/09/25/setting-item-selected-state-in-android-gridview
                             */
                            mGridview.post(new Runnable() {
                                @Override
                                public void run() {
                                    mGridview.setSelection(0);
                                }
                            });
                        } else {
                            //其他页 添加数据
                            materialAdapter.addAll(shortItemsResult.shortItemList);
                        }
                    } else {
                        if (materialAdapter.getCount() != 0) {
                            if (pageIndex != 1) {
                                ToastUtil.showToast(MasterAdviceListActivity.this, R.string.scenic_hasnodata);
//                                pageIndex--;
                            } else {
                                materialAdapter.clear();
                            }
                        }
                    }
                }
                if (materialAdapter.getCount() == 0) {
                    showNoDataPageView();
                    pageIndex = 1;
                } else {
                }
                break;
            case ValueConstants.MSG_INTEGRALMALL_LIST_KO:

                if (materialAdapter.getCount() == 0) {

                    showNetErrorView(null, msg.arg1);
                } else {
                    AndroidUtils.showToastCenter(MasterAdviceListActivity.this, StringUtil.handlerErrorCode(MasterAdviceListActivity.this, msg.arg1));
                }
                break;
            case ValueConstants.MSG_HOTEL_FILTER_OK://顶部筛选
                hideErrorView(null);
                bindFilterView((QueryTerm) msg.obj);
                break;
            case ValueConstants.MSG_HOTEL_FILTER_KO:
                showNetErrorView(null, msg.arg1);
                break;
            case ValueConstants.MSG_GET_ABROAD_DESTINATION_OK: //城市
                DestinationList result = (DestinationList) msg.obj;
                if (result != null) {
                    mMasterAdviceRegionView.handlerData(result);
                }
                startLoadData(true);
                break;
            case ValueConstants.MSG_GET_ABROAD_DESTINATION_KO:
                break;
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
                hideErrorView(null);
                if (!isHasFilter) {
                    mController.doGetHotelFilter(MasterAdviceListActivity.this, QueryType.ALL_CONSULTING_SERVICES );
                } else {
                    startLoadData(true);
                }
                showLoadingView(getResources().getString(R.string.scenic_loading_notice));
            }
        });
    }

    /**
     * 顶部绑定筛选条件
     *
     * @param list
     */

    private List<QueryTerm> mLineFilter;
    private List<View> mTabViews;
    private boolean isHasFilter = false;//标识是否已经获取到筛选条件
    private Map<String, String> mSearchParams = new HashMap<String, String>();
    private long mTagId = -1;
    private MasterAdviceRegionView mMasterAdviceRegionView;

    private void bindFilterView(QueryTerm list) {
        isHasFilter = true;
        if (list == null || list.queryTermList == null || list.queryTermList.size() == 0) {
            showNoDataPageView();
            return;
        }

        mLineFilter = list.queryTermList;
        List<String> tabStrings = new ArrayList<>(mLineFilter.size());
        mTabViews = new ArrayList<>(mLineFilter.size());
        tabStrings.add("地区");
        mMasterAdviceRegionView = new MasterAdviceRegionView(this, ItemType.SERVICE, this);
        mTabViews.add(mMasterAdviceRegionView);
        for (int num = 0; num < mLineFilter.size(); num++) {
            QueryTerm t = mLineFilter.get(num);
            String tabContent = t.text;
            if (tabContent.equals(getString(R.string.label_hotel_intelligent_ordering))) {
                List<QueryTerm> queryTerms = new ArrayList<>();
                tabStrings.add(tabContent);
                if (t.queryTermList != null) {
                    for(int i=0;i< t.queryTermList.size();i++){
                        if( t.queryTermList.get(i).text.equals(getString(R.string.label_hotel_intelligent_ordering))){

                        }else{
                            queryTerms.add(t.queryTermList.get(i));
                        }
                    }

                    if(t.queryTermList.get(0).type!=null){
                        QueryTerm ts=new QueryTerm();
                        ts.text=getString(R.string.label_hotel_intelligent_ordering);
                        ts.type=t.queryTermList.get(0).type;
                        queryTerms.add(0,ts);
                    }

                }
                TabPopView tabPopView = new TabPopView(getApplicationContext());
                tabPopView.bindViewData(queryTerms, mTagId);
                tabPopView.setOnItemClickInterface(new DropViewItemClickListener(num));
                mTabViews.add(tabPopView);
            }
        }

        //城市名称
        mController.doQueryDestinationTree(MasterAdviceListActivity.this, ItemType.SERVICE);
        mDropDownMenu.setDropMenuClose(this);
        mDropDownMenu.setDropDownMenu(tabStrings, mTabViews);
    }

    @Override
    public void menuClose(int position, LinearLayout linearLayout) {

    }

    @Override
    public void menuOpen(int position, LinearLayout linearLayout) {
////if(position==0){
//    mMasterAdviceRegionView
////}
    }

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
            if(!StringUtil.isEmpty(data.value)){
                mSearchParams.put(data.type, data.value);
                Map<String, String> map = new HashMap<>();
                map.put(AnalyDataValue.KEY_TYPE, data.type);
                map.put(AnalyDataValue.KEY_VALUE, data.text);
                TCEventHelper.onEvent(MasterAdviceListActivity.this, AnalyDataValue.CONSULTING_SERVICE_LIST_FILTER_SORT, map);
            }else{
                mSearchParams.remove(data.type);
            }


            if (!data.hasChild) {
                mDropDownMenu.setTabText(data.text);
                startLoadData(true);
                mDropDownMenu.closeMenu();

            }
        }

        @Override
        public void onSecondItemSelect(QueryTerm data) {

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.trasparent_topbar_left_layout:

                onBackPressed();
                break;
//            case R.id.tv_look_score://请登录
//                NavUtils.gotoLoginActivity(MasterAdviceListActivity.this);
//                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ShortItem shortItem = (ShortItem) mGridview.getAdapter().getItem(position);
        NavUtils.gotoProductDetail(this,
                ItemType.MASTER_CONSULT_PRODUCTS,
                shortItem.id,
                "");
    }


    @Override
    public void onRefresh(PullToRefreshBase<GridView> refreshView) {

        if (refreshView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_START) {
            startLoadData(true);
        } else if (refreshView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_END) {
            startLoadData(false);
        }

    }

    /**
     * 展示无数据的提示
     */
    private void showNoDataPageView() {
        showErrorView(mRefreshViewParent,
                IActionTitleBar.ErrorType.EMPTYVIEW, getString(R.string.label_no_consulting_service_data), " ", "", null);
    }

    /**
     * 目的地选中
     *
     * @param evBus
     */
    public void onEvent(EvBusDestCityChoose evBus) {
        goFinish(evBus.getCityCode(), evBus.getCityName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    //点击事件处理
    private void goFinish(String cityCode, String cityName) {
        //打点
        Map<String, String> map = new HashMap<>();
        map.put(AnalyDataValue.KEY_CITY_NAME, cityName);
        map.put(AnalyDataValue.KEY_CITY_CODE, cityCode);
        TCEventHelper.onEvent(this, AnalyDataValue.CONSULTING_SERVICE_LIST_FILTER_AREA, map);

        if(!"-1".equals(cityCode)){
            mSearchParams.put(QueryType.DEST_CITY, cityCode);
        }else{
            mSearchParams.put(QueryType.DEST_CITY, null);
        }
        mDropDownMenu.setTabText(cityName);
        mDropDownMenu.closeMenu();
        startLoadData(true);
    }

}
