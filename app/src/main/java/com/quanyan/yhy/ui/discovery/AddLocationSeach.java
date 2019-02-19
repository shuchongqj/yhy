package com.quanyan.yhy.ui.discovery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.view.BaseNavView;
import com.quanyan.base.view.customview.pulltorefresh.PullToRefreshBase;
import com.quanyan.base.view.customview.pulltorefresh.PullToRefreshListView;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.AnalyDataValue;
import com.quanyan.yhy.ui.adapter.base.BaseAdapterHelper;
import com.quanyan.yhy.ui.adapter.base.QuickAdapter;
import com.quanyan.yhy.ui.base.utils.TCEventHelper;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.view.SearchEditText;
import com.yhy.common.beans.net.model.AddLiveLocationBean;
import com.yhy.common.utils.SPUtils;
import com.yhy.location.LocationManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 发布直播--添加位置
 * <p/>
 * Created by zhaoxp on 2015-11-2.
 */
public class AddLocationSeach extends BaseActivity implements AdapterView.OnItemClickListener, PoiSearch.OnPoiSearchListener, View.OnClickListener {

    @ViewInject(R.id.base_nav_view_edit)
    private SearchEditText mSearchEditText;
    @ViewInject(R.id.pull_to_refresh_listview)
    private PullToRefreshListView mPullToRefreshListView;

    private ListView mListView;
    private RelativeLayout nosearch;
    private QuickAdapter<PoiLocation> mAdapter;
    private List<AddLiveLocationBean> mBeans;

    /**
     * 添加位置
     * @param activity
     * @param reqCode
     */
    public static void gotoAddLocation(Activity activity, int reqCode){
        Intent intent = new Intent(activity, AddLocationSeach.class);
        activity.startActivityForResult(intent, reqCode);
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.base_pull_refresh_layout_listview, null);
    }

    private BaseNavView mBaseNavView;
    @Override
    public View onLoadNavView() {
        mBaseNavView = new BaseNavView(this);
        mBaseNavView.showSeachView(true, "添加位置");
        mBaseNavView.setRightText(R.string.label_btn_search);
        mBaseNavView.setRightTextClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getString(R.string.label_btn_search).equals(mBaseNavView.getRightText().toString())) {
                    doSearchQuery(mSearchEditText.getText().toString().trim());
                } else {
                    mBaseNavView.setRightText(getString(R.string.label_btn_search));
                    mAdapter.clear();
                }
            }
        });
        return mBaseNavView;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ViewUtils.inject(this);

        mPullToRefreshListView.setMode(PullToRefreshBase.Mode.DISABLED);
        mListView = mPullToRefreshListView.getRefreshableView();

        addHeaderView();
        mAdapter = new QuickAdapter<PoiLocation>(this, R.layout.cell_add_live_location, prompList) {
            @Override
            protected void convert(BaseAdapterHelper helper, PoiLocation item) {
                handleItem(helper, item);
            }
        };

        mListView.setAdapter(mAdapter);
        doSearchQuery();
        iniEvent();
    }

    private void handleItem(BaseAdapterHelper helper, PoiLocation item) {
        helper.setText(R.id.cell_live_add_location_title, item.getTitle())
                .setText(R.id.cell_live_add_location_info, item.getAddress());
    }

    private void addHeaderView() {
        View view = getLayoutInflater().inflate(R.layout.add_live_location_headview_view, null);
        nosearch = (RelativeLayout) view.findViewById(R.id.add_live_loation_nosearch);
        nosearch.setOnClickListener(this);
        mListView.addHeaderView(view);
    }

    private void iniEvent() {
        mSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mBaseNavView.setRightText(getString(R.string.label_btn_search));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mSearchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (getString(R.string.label_btn_search).equals(mBaseNavView.getRightText().toString())) {
                        doSearchQuery(mSearchEditText.getText().toString().trim());
                    } else {
                        mBaseNavView.setRightText(getString(R.string.label_btn_search));
                        mAdapter.clear();
                    }
                    return true;
                }
                return false;
            }
        });
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TCEventHelper.onEvent(AddLocationSeach.this, AnalyDataValue.PUB_LIVE_ADD_LOCATION);
        Intent intent = getIntent();
        intent.putExtra(SPUtils.EXTRA_ADD_LIVE_LOCATION, (Serializable) mAdapter.getItem(position));
        setResult(RESULT_OK, intent);
    }

    /**
     * 判断字符串是否为空
     *
     * @author lvliuyan
     */
    public static boolean isEmpty(String str) {
        if (str != null && !str.equals("") && !str.equals("null")) {
            return false;
        } else {
            return true;
        }
    }

    String latitude = "", longitude = "", address = "";
    private List<PoiLocation> prompList = new ArrayList<PoiLocation>();
    private PoiSearch poiSearch;// POI搜索
    private PoiSearch.Query query;// Poi查询条件类

    /**
     * 开始进行poi搜索
     */
    protected void doSearchQuery(String searchText) {
        if (!isEmpty(searchText)) {
            if(!isEmpty(LocationManager.getInstance().getStorage().getGprs_cityName())){
                mBaseNavView.setRightText(getString(R.string.label_btn_cancel));
                query = new PoiSearch.Query(searchText, "", LocationManager.getInstance().getStorage().getGprs_cityName());
                query.setPageSize(10);
                query.setPageNum(0);
                poiSearch = new PoiSearch(this, query);
                poiSearch.setOnPoiSearchListener(this);
                poiSearch.searchPOIAsyn();
            }else{
                ToastUtil.showToast(this,getString(R.string.toast_null_current_location));
            }

        } else {
            ToastUtil.showToast(this,getString(R.string.label_toast_null_keyword));
        }

    }




    /**
     * 根据当前位置搜索周边信息
     */
    protected void doSearchQuery() {
        try {
            LatLonPoint current = new LatLonPoint(LocationManager.getInstance().getStorage().getGprs_lat(), LocationManager.getInstance().getStorage().getGprs_lng());
            if(!isEmpty(LocationManager.getInstance().getStorage().getGprs_cityName())&&current!=null){
                query = new PoiSearch.Query("", null, LocationManager.getInstance().getStorage().getGprs_cityName());
                query.setPageSize(20);
                query.setPageNum(0);
                poiSearch = new PoiSearch(this, query);
                poiSearch.setOnPoiSearchListener(this);
                poiSearch.setBound(new PoiSearch.SearchBound(current, 500, true));//
                poiSearch.searchPOIAsyn();
            }else{
                ToastUtil.showToast(this,getString(R.string.toast_null_current_location));
            }

        } catch (Exception e) {
        }
    }


    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }
    @Override
    public void onPoiSearched(PoiResult result, int rCode) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mSearchEditText.getWindowToken(),0);
        if (rCode == 1000) {
            if (result != null && result.getQuery() != null) {
                if (result.getQuery().equals(query)) {
                    List<PoiItem> poiItems = result.getPois();
                    if (poiItems != null && poiItems.size() > 0) {
                        prompList.clear();
                        for (int i = 0; i < poiItems.size(); i++) {
                            PoiLocation ai = new PoiLocation();
                            ai.setLatitude(poiItems.get(i).getLatLonPoint().getLatitude());
                            ai.setLongitude(poiItems.get(i).getLatLonPoint().getLongitude());
                            ai.setTitle(poiItems.get(i).getTitle());
                            ai.setAddress(poiItems.get(i).getSnippet());
                            prompList.add(ai);
                        }
                        // 刷新列表
                        mAdapter.replaceAll(prompList);
                        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                int headerCount =mListView.getHeaderViewsCount();
                                if (position >= headerCount) {
                                    PoiLocation poh2 = prompList.get(position - headerCount);
                                    longitude = poh2.getLongitude() + "";
                                    latitude = poh2.getLatitude() + "";
                                    address = poh2.getTitle() + "";
                                    saveLocation(poh2);
                                }
                            }
                        });

                    }else{
                        ToastUtil.showToast(this,getString(R.string.toast_null_search_result));
                    }
                }
            }else{
                ToastUtil.showToast(this,getString(R.string.toast_null_search_result));
            }
        }else{
            ToastUtil.showToast(this,getString(R.string.toast_null_search_result));
        }
    }

    /**
     * 保存位置选择
     *
     * @param bean
     */
    private void saveLocation(PoiLocation bean) {
        Intent intent = new Intent();
        intent.putExtra(SPUtils.EXTRA_SELECT_CITY, bean);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_live_loation_nosearch:
                saveLocation(null);
                break;


        }
    }


}
