package com.quanyan.yhy.ui.master.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.quanyan.base.BaseFragment;
import com.quanyan.base.baseenum.IActionTitleBar;
import com.quanyan.base.util.StringUtil;
import com.quanyan.base.view.customview.NoScrollGridView;
import com.quanyan.base.view.customview.pulltorefresh.PullToRefreshBase;
import com.quanyan.base.view.customview.pulltorefresh.PullToRefreshListView;
import com.quanyan.base.yminterface.ErrorViewClick;

import com.quanyan.yhy.R;
import com.quanyan.yhy.common.AnalyDataValue;
import com.quanyan.yhy.common.ErrorCode;
import com.quanyan.yhy.common.ItemType;
import com.quanyan.yhy.common.MerchantType;
import com.quanyan.yhy.common.QueryType;
import com.quanyan.yhy.common.ResourceType;
import com.quanyan.yhy.manager.HistoryManager;
import com.quanyan.yhy.ui.adapter.base.BaseAdapterHelper;
import com.quanyan.yhy.ui.adapter.base.QuickAdapter;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.base.utils.TCEventHelper;
import com.quanyan.yhy.ui.common.city.DestinationSelectActivity;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.line.LineSearchResultActivity;
import com.quanyan.yhy.ui.master.controller.MasterController;
import com.quanyan.yhy.ui.master.helper.MasterViewHelper;
import com.yhy.common.base.YHYBaseApplication;
import com.yhy.common.beans.net.model.DefaultCityBean;
import com.yhy.common.beans.net.model.RCShowcase;
import com.yhy.common.beans.net.model.club.PageInfo;
import com.yhy.common.beans.net.model.common.Booth;
import com.yhy.common.beans.net.model.master.QueryTerm;
import com.yhy.common.beans.net.model.master.QueryTermsDTO;
import com.yhy.common.beans.net.model.master.TalentInfoList;
import com.yhy.common.beans.net.model.master.TalentQuery;
import com.yhy.common.beans.net.model.master.TalentUserInfo;
import com.yhy.common.beans.net.model.search.BaseHistorySearch;
import com.yhy.common.beans.net.model.search.MasterSearchHistoryList;
import com.yhy.common.beans.net.model.trip.ShortItem;
import com.yhy.common.beans.net.model.trip.ShortItemsResult;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.SPUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class SearchFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    //最大保存的历史记录条数
    private static final int MAX_HISTORY_SIZE = 10;
    private PullToRefreshListView plv_sample;
    private ListView mListView;

    private QuickAdapter mSearchResultAdapter;
    private QuickAdapter mHotSearchAdapter;
    private QuickAdapter mSearchHistoryAdapater;

    private int pageNo = 1;
    private int state = 0;
    private boolean hasNext = false;
    private MasterController mMasterController;
    private String mSearchType = ItemType.MASTER;
    private String mSource;

    private String mTitle;

    private PullToRefreshListView mSearchListView;
    private View mFooterView;
    private View mHeaderView;
    private String mSearchKeyWord;

    public static SearchFragment getInstance(String title, String type, String source){
        SearchFragment searchFragment = new SearchFragment();
        Bundle bundle = new Bundle();
        bundle.putString(SPUtils.EXTRA_TITLE, title);
        bundle.putString(SPUtils.EXTRA_TYPE, type);
        bundle.putString(SPUtils.EXTRA_SOURCE, source);
        searchFragment.setArguments(bundle);
        return searchFragment;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if(bundle != null){
            mSearchType = bundle.getString(SPUtils.EXTRA_TYPE);
            mSource = bundle.getString(SPUtils.EXTRA_SOURCE);
            mTitle = bundle.getString(SPUtils.EXTRA_TITLE);
        }
        plv_sample = (PullToRefreshListView) view.findViewById(R.id.pull_to_refresh_listview);
        mSearchListView = (PullToRefreshListView) view.findViewById(R.id.lv_master_search);

        initClick();
        initSearchView();

        plv_sample.setVisibility(View.GONE);
        if(ItemType.MASTER_PRODUCTS.equals(mSearchType) || ItemType.NORMAL.equals(mSearchType)){
            mSearchResultAdapter = new QuickAdapter<ShortItem>(getActivity(), R.layout.item_home_recommend, new ArrayList<ShortItem>()) {
                @Override
                protected void convert(BaseAdapterHelper helper, ShortItem item) {
                    MasterViewHelper.handleMasterSearchLineItem(getActivity(), helper, item);
                }
            };
        } else if(ItemType.MASTER.equals(mSearchType)){
            mSearchResultAdapter = new QuickAdapter<TalentUserInfo>(getActivity(), R.layout.master_list_item_view, new ArrayList<TalentUserInfo>()) {
                @Override
                protected void convert(BaseAdapterHelper helper, TalentUserInfo item) {
                    MasterViewHelper.handleMasterListItem(getActivity(), helper, item);
                }
            };
        }
        plv_sample.setMode(PullToRefreshBase.Mode.DISABLED);
        plv_sample.setScrollingWhileRefreshingEnabled(!plv_sample.isScrollingWhileRefreshingEnabled());
        plv_sample.setMode(PullToRefreshBase.Mode.BOTH);
        plv_sample.setOnRefreshListener(new MyOnRefreshListener());

        mListView = plv_sample.getRefreshableView();
        mListView.setBackgroundColor(Color.WHITE);
        mSearchListView.getRefreshableView().setBackgroundColor(Color.WHITE);
        mSearchListView.getRefreshableView().setDividerHeight(1);
        mListView.setAdapter(mSearchResultAdapter);
        mListView.setOnItemClickListener(this);
    }

    private void initClick() {
        if(getActivity() instanceof SearchInterface) {
            ((SearchInterface) getActivity()).getSearchEditText().setOnClearClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resetSearch();
                }
            });

            ((SearchInterface) getActivity()).getSearchEditText().addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (TextUtils.isEmpty(s.toString())) {
                        resetSearch();
                    }
                }
            });

            ((SearchInterface) getActivity()).getSearchEditText().setOnEditorActionListener(new TextView.OnEditorActionListener() {

                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        closeInput();
                        mSearchKeyWord=((SearchInterface) getActivity()).getSearchEditText().getText().toString().trim();
                        tcEventSting();
                        if (!StringUtil.isEmpty(mSearchKeyWord)) {
                            if (isNeedTransferWord()) {
                                transferSearchKeyWord();
                            } else {
                                startSearch();
                            }
                        } else {
                            ToastUtil.showToast(getActivity(), getString(R.string.label_toast_null_keyword));
                        }
                        return true;
                    }
                    return false;
                }
            });
        }
    }

    //自己输入的关键词打点
    public void tcEventSting() {
        Map<String, String> map = new HashMap<String, String>();
        String type = AnalyDataValue.getType(mSearchType);
        map.put(AnalyDataValue.KEY_TYPE, type);
        map.put(AnalyDataValue.KEY_VALUE, mSearchKeyWord);
        TCEventHelper.onEvent(getActivity(), AnalyDataValue.HOT_SEARCH_WORD_CLICK, map);
    }

    //热搜推荐点击打点
    private void tcHotEventSting() {
        Map<String, String> map = new HashMap<String, String>();
        String type = AnalyDataValue.getType(mSearchType);
        map.put(AnalyDataValue.KEY_TYPE, type);
        map.put(AnalyDataValue.KEY_VALUE, mSearchKeyWord);
        TCEventHelper.onEvent(getActivity(), AnalyDataValue.HOT_SEARCH_WORD_CLICK, map);
    }

    /**
     * 判断是否是线路
     * @return
     */
    public boolean isLine(){
        if(ItemType.TOUR_LINE_ABOARD.equals(mSearchType) ||
                ItemType.TOUR_LINE.equals(mSearchType) ||
                ItemType.FREE_LINE_ABOARD.equals(mSearchType) ||
                ItemType.FREE_LINE.equals(mSearchType) ||
                ItemType.CITY_ACTIVITY.equals(mSearchType) ||
                ItemType.ARROUND_FUN.equals(mSearchType) ||
                ItemType.HOTEL.equals(mSearchType) ||
                ItemType.SCENIC.equals(mSearchType)){
            return true;
        }
        return false;
    }

    /**
     * 判断是否是线路
     * @return
     */
    public boolean isNeedTransferWord(){
        if(ItemType.TOUR_LINE_ABOARD.equals(mSearchType) ||
                ItemType.TOUR_LINE.equals(mSearchType) ||
                ItemType.FREE_LINE_ABOARD.equals(mSearchType) ||
                ItemType.FREE_LINE.equals(mSearchType) ||
                ItemType.CITY_ACTIVITY.equals(mSearchType) ||
                ItemType.ARROUND_FUN.equals(mSearchType) ||
                ItemType.HOTEL.equals(mSearchType) ||
                ItemType.SCENIC.equals(mSearchType) ||
                ItemType.CONSULT.equals(mSearchType)){
            return true;
        }
        return false;
    }

    /**
     * 开始搜索
     */
    public void startSearch(){
        isRefresh = true;
        state = 0;
        pageNo = 1;
        hasNext = false;
        doSearchMasterList();
        //保存搜索纪录
        saveSearchHistory();
    }

    /***
     * 设置监听器
     */
    private boolean isRefresh = false;

    @Override
    public View onLoadContentView() {
        return View.inflate(getActivity(),R.layout.ac_master_search, null);
    }

    class MyOnRefreshListener implements PullToRefreshBase.OnRefreshListener2<ListView> {
        @Override
        public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
            isRefresh = true;
            pageNo = 1;
            state = 1;
            hasNext = false;
            doSearchMasterList();
        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            if (hasNext) {
                isRefresh = false;
                state = 2;
                hasNext = false;
                pageNo = (mSearchResultAdapter.getCount() / ValueConstants.PAGESIZE) + 1;
                doSearchMasterList();
            } else {
                mHandler.sendEmptyMessageDelayed(ValueConstants.MSG_HAS_NO_DATA, 1000);
            }
        }
    }

    /**
     * 获取搜索结果
     */
    private void doSearchMasterList() {
        if (TextUtils.isEmpty(mSearchKeyWord)) {
            ToastUtil.showToast(getActivity(),getString(R.string.label_toast_null_keyword));
            return;
        }
        showLoadingView(getString(R.string.dlg_msg_searching));
        if(ItemType.MASTER_PRODUCTS.equals(mSearchType) ){
            //TODO 达人线路搜索
            QueryTermsDTO params = new QueryTermsDTO();
            params.pageSize = ValueConstants.PAGESIZE;
            params.pageNo = pageNo;

            List<QueryTerm> ts = new ArrayList<>();
            QueryTerm t1 = new QueryTerm();
            t1.type = QueryType.TITLE;
            t1.value = mSearchKeyWord;
            ts.add(t1);

            QueryTerm t2 = new QueryTerm();
            t2.type = QueryType.SELLER_TYPE;
            t2.value = MerchantType.TALENT;
            ts.add(t2);
            //等同城活动支持搜索的时候再打开注释
//            QueryTerm t3 = new QueryTerm();
//            t3.type = QueryType.ITEM_TYPE;
//            t3.value = ItemType.FREE_LINE + "," + ItemType.FREE_LINE_ABOARD + "," + ItemType.TOUR_LINE + "," + ItemType.TOUR_LINE_ABOARD;
//            ts.add(t3);

            params.queryTerms = ts;
            params.pageNo = pageNo;
            params.pageSize = ValueConstants.PAGESIZE;
            mMasterController.doMasterLineListSearch(getActivity(),params);
        }else if(ItemType.MASTER.equals(mSearchType) ){
            //TODO 达人搜索
            TalentQuery query = new TalentQuery();
            query.searchWord = mSearchKeyWord;
            PageInfo pageInfo = new PageInfo();
            pageInfo.pageNo = pageNo;
            pageInfo.pageSize = ValueConstants.PAGESIZE;
            query.pageInfo = pageInfo;
            mMasterController.doMasterListSearch(getActivity(),query);
        }else if(ItemType.NORMAL.equals(mSearchType) ){
            //TODO 必买搜索
            QueryTermsDTO params = new QueryTermsDTO();
            params.pageSize = ValueConstants.PAGESIZE;
            params.pageNo = pageNo;

            List<QueryTerm> ts = new ArrayList<>();
            QueryTerm t1 = new QueryTerm();
            t1.type = QueryType.TITLE;
            t1.value = mSearchKeyWord;
            ts.add(t1);

            QueryTerm t2 = new QueryTerm();
            t2.type = QueryType.ITEM_TYPE;
            t2.value = ItemType.NORMAL;
            ts.add(t2);

            params.queryTerms = ts;
            params.pageNo = pageNo;
            params.pageSize = ValueConstants.PAGESIZE;
            mMasterController.doMasterLineListSearch(getActivity(),params);
        }else{
            hideLoadingView();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int headerCount = mListView.getHeaderViewsCount();
        if(position < headerCount){
            return ;
        }
        position = position - headerCount;
        if(ItemType.MASTER_PRODUCTS.equals(mSearchType)  || ItemType.NORMAL.equals(mSearchType)){
            ShortItem item  = ((ShortItem) mSearchResultAdapter.getItem(position));
            NavUtils.gotoProductDetail(getActivity(),
                    item.itemType,
                    item.id,
                    item.title);
        }else if(ItemType.MASTER.equals(mSearchType)) {
            TalentUserInfo item  = ((TalentUserInfo) mSearchResultAdapter.getItem(position));
            NavUtils.gotoMasterHomepage(getActivity(),
                    item.userId);
        }
    }

    @Override
    public void handleMessage(Message msg) {
        hideLoadingView();
        hideErrorView(null);
        plv_sample.onRefreshComplete();
        switch (msg.what) {
            case ValueConstants.MSG_MASTER_SEARCH_OK:
                if(ItemType.MASTER_PRODUCTS.equals(mSearchType)  || ItemType.NORMAL.equals(mSearchType)){
                    handleNetDataMasterLine((ShortItemsResult) msg.obj);
                }else if(ItemType.MASTER.equals(mSearchType)){
                    handleNetDataMaster((TalentInfoList) msg.obj);
                }
                break;
            case ValueConstants.MSG_MASTER_SEARCH_KO:
                if (isRefresh) {
                    if(mSearchResultAdapter.getCount() == 0){
                        mSearchResultAdapter.clear();
                        showErrorView(null, ErrorCode.NETWORK_UNAVAILABLE == msg.arg1 ?
                                IActionTitleBar.ErrorType.NETUNAVAILABLE : IActionTitleBar.ErrorType.ERRORNET, "", "", "", new ErrorViewClick() {
                            @Override
                            public void onClick(View view) {
                                isRefresh = true;
                                pageNo = 1;
                                hasNext = false;
                                state = 0;
                                doSearchMasterList();
                            }
                        });
                    }else{
                        ToastUtil.showToast(getActivity(),StringUtil.handlerErrorCode(getActivity(), msg.arg1));
                    }
                }
                break;
            case ValueConstants.MSG_HAS_NO_DATA:
                ToastUtil.showToast(getActivity(), getResources().getString(R.string.scenic_hasnodata));
                break;
            case ValueConstants.MSG_MASTER_SEARCH_HISTORY_OK:
                handleMasterSearchHistory((MasterSearchHistoryList)msg.obj);
                break;
            case ValueConstants.MSG_MASTER_HOT_SEARCH_OK:
                handleMasterHotSearch((Booth)msg.obj);
                break;
            case ValueConstants.MSG_MASTER_HOT_SEARCH_KO:
                showErrorView(null, ErrorCode.NETWORK_UNAVAILABLE == msg.arg1 ?
                        IActionTitleBar.ErrorType.NETUNAVAILABLE : IActionTitleBar.ErrorType.ERRORNET, "", "", "", new ErrorViewClick() {
                    @Override
                    public void onClick(View view) {
                        isRefresh = true;
                        pageNo = 1;
                        hasNext = false;
                        state = 0;
                        mMasterController.doGetHotSearchList(getActivity(),getBoothCodeByType());
                    }
                });
                break;
        }
    }

    /**
     * 关闭键盘事件
     */
    private void closeInput() {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null && getActivity().getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 重置搜索结果
     */
    public void resetSearch(){
        closeInput();
        hideLoadingView();
        hideErrorView(null);
        mSearchListView.setVisibility(View.VISIBLE);
        plv_sample.setVisibility(View.GONE);
        refreshFooterView();
    }

    /**
     * 处理达人列表搜索结果
     *
     * @param result
     */
    private void handleNetDataMaster(TalentInfoList result) {
        plv_sample.setVisibility(View.VISIBLE);
        mSearchListView.setVisibility(View.GONE);
        if (result == null || result.talentList == null || result.talentList.size() < ValueConstants.PAGESIZE) {
            plv_sample.setMode(PullToRefreshBase.Mode.DISABLED);
        } else {
            plv_sample.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        }
        hasNext = result.hasNext;
        if (result != null && result.talentList != null) {
            if (isRefresh) {
                if (state == 0) {
                    if (result.talentList.size() <= 0) {
                        mSearchResultAdapter.clear();
                        plv_sample.setMode(PullToRefreshBase.Mode.DISABLED);
                        showErrorView(null, IActionTitleBar.ErrorType.EMPTYVIEWSEARCH,
                                getString(R.string.label_master_search_empty_text),
                                " ",
                                "", null);
                    }
                }
                mSearchResultAdapter.replaceAll(result.talentList);
                mListView.setSelection(0);
            } else {
                mSearchResultAdapter.addAll(result.talentList);
            }
        } else {
            if (state == 0) {
                mSearchResultAdapter.clear();
                plv_sample.setMode(PullToRefreshBase.Mode.DISABLED);
                showErrorView(null, IActionTitleBar.ErrorType.EMPTYVIEWSEARCH,
                        getString(R.string.label_master_search_empty_text),
                        " ",
                        "",
                        null);
            }
        }
    }

    /**
     * 处理达人商品搜索结果
     * @param result
     */
    private void handleNetDataMasterLine(ShortItemsResult result) {
        plv_sample.setVisibility(View.VISIBLE);
        mSearchListView.setVisibility(View.GONE);
        if (result == null || result.shortItemList == null || result.shortItemList.size() < ValueConstants.PAGESIZE) {
            plv_sample.setMode(PullToRefreshBase.Mode.DISABLED);
        } else {
            plv_sample.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
            hasNext = result.shortItemList.size() == ValueConstants.PAGESIZE;
        }

        if (result != null && result.shortItemList != null) {
            if (isRefresh) {
                if (state == 0) {
                    if (result.shortItemList.size() <= 0) {
                        mSearchResultAdapter.clear();
                        plv_sample.setMode(PullToRefreshBase.Mode.DISABLED);
                        showErrorView(null, IActionTitleBar.ErrorType.EMPTYVIEWSEARCH,
                                getString(R.string.label_master_search_empty_text),
                                " ",
                                "",
                                null);
                    }
                }
                mSearchResultAdapter.replaceAll(result.shortItemList);
                mListView.setSelection(0);
            } else {
                mSearchResultAdapter.addAll(result.shortItemList);
            }
        } else {
            if (state == 0) {
                mSearchResultAdapter.clear();
                plv_sample.setMode(PullToRefreshBase.Mode.DISABLED);
                showErrorView(null, IActionTitleBar.ErrorType.EMPTYVIEWSEARCH,
                        getString(R.string.label_master_search_empty_text),
                        " ",
                        "",
                        null);
            }
        }
    }

    private void initSearchView(){
        mMasterController = new MasterController(getActivity(), mHandler);
        if(ItemType.MASTER_PRODUCTS.equals(mSearchType)) {
            mHistoryManager = new HistoryManager(getActivity(), HistoryManager.MASTER_SEARCH_LINES,mHandler);
        }else if(ItemType.MASTER.equals(mSearchType)){
            mHistoryManager = new HistoryManager(getActivity(), HistoryManager.MASTER_SEARCH,mHandler);
        }else if(isNeedTransferWord()){
            mHistoryManager = new HistoryManager(getActivity(), mSearchType,mHandler);
        }else if(ItemType.NORMAL.equals(mSearchType)){
            mHistoryManager = new HistoryManager(getActivity(), HistoryManager.MASTER_MUST_BUY,mHandler);
        }else if (ItemType.SERVICE.equals(mSearchType)){
            mHistoryManager = new HistoryManager(getActivity(), HistoryManager.SERVICE,mHandler);
        }
        mHeaderView = View.inflate(getActivity(), R.layout.header_master_hot_search,null);
        mFooterView = View.inflate(getActivity(), R.layout.footer_master_hot_search,null);
        mSearchListView.getRefreshableView().addHeaderView(mHeaderView);
        mSearchListView.getRefreshableView().addFooterView(mFooterView);
        NoScrollGridView hotSearchView = (NoScrollGridView)mHeaderView.findViewById(R.id.gv_hot_master_search);
        mHotSearchAdapter = new QuickAdapter<RCShowcase>(getActivity(), R.layout.item_base_hot_search_history, new ArrayList<RCShowcase>()) {
            @Override
            protected void convert(BaseAdapterHelper helper, RCShowcase item) {
                MasterViewHelper.handleMasterHotSearchItem(getActivity(), helper, item);
            }
        };
        hotSearchView.setAdapter(mHotSearchAdapter);
        hotSearchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSearchKeyWord = mHotSearchList.get(position).operationContent;
                tcHotEventSting();
                if(isNeedTransferWord()){
                    transferSearchKeyWord();
                    return ;
                }
                if(getActivity() instanceof SearchInterface){
                    ((SearchInterface)getActivity()).getSearchEditText().setText(mSearchKeyWord);
                }
                startSearch();
            }
        });

        mSearchHistoryAdapater = new QuickAdapter<BaseHistorySearch>(getActivity(), R.layout.item_base_search_history, new ArrayList<BaseHistorySearch>()) {
            @Override
            protected void convert(BaseAdapterHelper helper, BaseHistorySearch item) {
                MasterViewHelper.handleMasterHistorySearchItem(getActivity(), helper, item);
            }
        };
        mSearchListView.setAdapter(mSearchHistoryAdapater);
        mSearchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int headerCount = mSearchListView.getRefreshableView().getHeaderViewsCount();
                if(position < headerCount){
                    return ;
                }
                mSearchKeyWord = mHistorySearchList.get(position - headerCount).text;
                tcHotEventSting();
                if(isNeedTransferWord()){
                    transferSearchKeyWord();
                    return ;
                }
                if(getActivity() instanceof SearchInterface){
                    ((SearchInterface)getActivity()).getSearchEditText().setText(mSearchKeyWord);
                }
                startSearch();
            }
        });

        mFooterView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearSearchHistory();
            }
        });

        mMasterController.doGetHotSearchList(getActivity(),getBoothCodeByType());
        loadSearchHistory();
    }

    /**
     * 根据类型获取资源位代码
     * @return
     */
    private String getBoothCodeByType(){
        String code = "";
        if (ItemType.TOUR_LINE.equals(mSearchType) || ItemType.TOUR_LINE_ABOARD.equals(mSearchType)) {
            code = ResourceType.QUANYAN_GROUP_TRAVELL_SEARCH_KEYWORD;
        } else if (ItemType.FREE_LINE.equals(mSearchType) || ItemType.FREE_LINE_ABOARD.equals(mSearchType)) {
            code = ResourceType.QUANYAN_FREE_TRAVELL_SEARCH_KEYWORD;
        } else if (ItemType.CITY_ACTIVITY.equals(mSearchType)) {
            code = ResourceType.QUANYAN_ACTIVITY_SEARCH_KEYWORD;
        } else if (ItemType.ARROUND_FUN.equals(mSearchType)) {
            code = ResourceType.QUANYAN_NEARBY_ENJOY_SEARCH_KEYWORD;
        } else if (ItemType.MASTER.equals(mSearchType)) {
            code = ResourceType.QUANYAN_MASTER_SEARCH_KEYWORD;
        } else if (ItemType.MASTER_PRODUCTS.equals(mSearchType)) {
            code = ResourceType.QUANYAN_MASTER_LINE_SEARCH_KEYWORD;
        } else if (ItemType.NORMAL.equals(mSearchType)) {
            code = ResourceType.QUANYAN_MUST_BUY_SEARCH_KEYWORD;
        } else if (ItemType.HOTEL.equals(mSearchType)) {
            code = ResourceType.QUANYAN_HOTEL_SEARCH_KEYWORD;
        } else if (ItemType.SCENIC.equals(mSearchType)) {
            code = ResourceType.QUANYAN_SPOTS_BUY_SEARCH_KEYWORD;
        } else if(ItemType.CONSULT.equals(mSearchType)){
            code = ResourceType.QUANYAN_CONSULTING_SERVICE_SEARCH_KEYWORD;
        }
        return code;
    }

    /**
     * 刷新状态
     */
    private void refreshFooterView(){
        if(mHistorySearchList != null && mHistorySearchList.size() > 0){
            mHeaderView.findViewById(R.id.gv_hot_master_search_history_tv).setVisibility(View.VISIBLE);
            mFooterView.setVisibility(View.VISIBLE);
            mSearchHistoryAdapater.replaceAll(mHistorySearchList);
        }else{
            mHeaderView.findViewById(R.id.gv_hot_master_search_history_tv).setVisibility(View.GONE);
            mFooterView.setVisibility(View.GONE);
        }
    }

    /**
     * 把搜索关键词传递给线路搜索结果页
     */
    public void transferSearchKeyWord(){
        saveSearchHistory();

        if(LineSearchResultActivity.class.getSimpleName().equals(mSource)){
            Intent intent = new Intent();
            intent.putExtra(SPUtils.EXTRA_DATA, mSearchKeyWord);
            getActivity().setResult(Activity.RESULT_OK, intent);
            getActivity().finish();
        }else {
            if(ItemType.CITY_ACTIVITY.equals(mSearchType)){
                NavUtils.gotoLineSearchResultActivity(getActivity(),
                        mTitle,
                        mSearchType,
                        mSearchKeyWord,
                        SPUtils.getLocalCityCode(getActivity().getApplicationContext()),
                        SPUtils.getLocalCityName(getActivity().getApplicationContext()),
                        null,
                        -1);
            }else if(ItemType.ARROUND_FUN.equals(mSearchType)){
                //TODO 跳转同城玩乐结果页
                String cityCode = SPUtils.getArroundCityCode(getActivity().getApplicationContext());
                NavUtils.gotoLineSearchResultActivity(getActivity(),
                        mTitle,
                        mSearchType,
                        mSearchKeyWord,
                        TextUtils.isEmpty(cityCode)?
                                DefaultCityBean.cityCode : cityCode,
                        TextUtils.isEmpty(cityCode)?
                                DefaultCityBean.cityName :  SPUtils.getArroundCityName(getActivity().getApplicationContext()),
                        null,
                        -1);
            }else{
                NavUtils.gotoLineSearchResultActivity(getActivity(),
                        mTitle,
                        mSearchType,
                        mSearchKeyWord,
                        null,
                        null,
                        null,
                        -1);
            }
            YHYBaseApplication.getInstance().removeActivityClass(DestinationSelectActivity.class);
            getActivity().finish();
        }
    }

    /**
     * 清除历史搜索
     */
    private void clearSearchHistory(){
        mHistorySearchList.clear();
        MasterSearchHistoryList list = new MasterSearchHistoryList();
        list.history = mHistorySearchList;
        mHistoryManager.saveMasterSearchHistory(list);
        mSearchHistoryAdapater.clear();
        refreshFooterView();
    }

    /**
     * 加载搜索历史
     */
    private HistoryManager mHistoryManager;
    private List<BaseHistorySearch> mHistorySearchList = new ArrayList<>();
    private void loadSearchHistory(){
        mHistoryManager.loadMasterSearchHistory();
    }

    /**
     * 保存搜索纪录
     */
    private void saveSearchHistory(){
        if(mSearchKeyWord == null || mSearchKeyWord.trim().length() == 0){
            return ;
        }
        if(!isInHistoryList()) {
            if(mHistorySearchList.size() >= MAX_HISTORY_SIZE){
                mHistorySearchList.remove(MAX_HISTORY_SIZE - 1);
            }
        }else{
            for(BaseHistorySearch bhs : mHistorySearchList){
                if(bhs.text.equals(mSearchKeyWord)){
                    mHistorySearchList.remove(bhs);
                    break;
                }
            }
        }
        mHistorySearchList.add(0,new BaseHistorySearch(mSearchKeyWord));
        MasterSearchHistoryList list = new MasterSearchHistoryList();
        list.history = mHistorySearchList;
        mHistoryManager.saveMasterSearchHistory(list);
        refreshFooterView();
    }

    /**
     * 判断是否在搜索历史纪录列表中
     * @return
     */
    private boolean isInHistoryList(){
        if(mSearchKeyWord == null || mSearchKeyWord.trim().length() == 0){
            return true;
        }
        for(BaseHistorySearch txt:mHistorySearchList){
            if(mSearchKeyWord.equals(txt.text)){
               return true;
            }
        }
        return false;
    }

    /**
     * 处理历史搜索纪录
     * @param value
     */
    private void handleMasterSearchHistory(MasterSearchHistoryList value){
        if(value == null || value.history == null){
            mFooterView.setVisibility(View.GONE);
            ((TextView)mHeaderView.findViewById(R.id.gv_hot_master_search_history_tv)).setText("");
            return ;
        }
        ((TextView)mHeaderView.findViewById(R.id.gv_hot_master_search_history_tv)).setText(R.string.label_master_history_search);
        mHistorySearchList = value.history;
        mSearchHistoryAdapater.replaceAll(mHistorySearchList);

        refreshFooterView();
    }

    /**
     * 处理热门搜索
     * @param value
     */
    private List<RCShowcase> mHotSearchList = new ArrayList<>();
    private void handleMasterHotSearch(Booth value){
        if(value == null || value.showcases == null || value.showcases.size() == 0){
            return ;
        }
        mHotSearchList = value.showcases;
        mHotSearchAdapter.replaceAll(mHotSearchList);
    }
}
