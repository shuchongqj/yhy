package com.yhy.cityselect;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.module_city_select.R;
import com.quanyan.yhy.libanalysis.AnEvent;
import com.quanyan.yhy.libanalysis.Analysis;
import com.yhy.cityselect.adapter.CityListAdapter;
import com.yhy.cityselect.adapter.SearchResultAdapter;
import com.yhy.cityselect.cache.CityCache;
import com.yhy.cityselect.entity.CityIndexBean;
import com.yhy.cityselect.entity.CityListBean;
import com.yhy.cityselect.eventbus.EvBusLocationChange;
import com.yhy.common.base.BaseNewActivity;
import com.yhy.common.utils.JSONUtils;
import com.yhy.common.utils.PermissionUtils;
import com.yhy.location.EvBusLocation;
import com.yhy.location.LocationManager;
import com.yhy.module_ui_common.YhyTopSearchView;
import com.yhy.network.resp.outplace.GetOutPlaceCityListResp;
import com.yhy.router.RouterPath;

import java.util.ArrayList;
import java.util.HashMap;

import de.greenrobot.event.EventBus;

/**
 * 选择城市页面
 * <p>
 * Created by yangboxue on 2018/7/9.
 */
@Route(path = RouterPath.ACTIVITY_CITY_SELECT)
public class CitySelectActivity extends BaseNewActivity {
    // 搜索框
    private YhyTopSearchView searchView;
    // 城市列表
    private RelativeLayout rlytContent;
    private View headView;
    private RelativeLayout rlytLocation;
    private LinearLayout llytLocation;
    private TextView tvLocation;
    private RelativeLayout rlytNoLocation;
    private TextView tvGoSetting;
    private LinearLayout llytHistory;
    private RecyclerView rvHistory;
    private BaseQuickAdapter<GetOutPlaceCityListResp.OutPlaceCity, BaseViewHolder> historyAdapter;
    private LinearLayout llytHot;
    private RecyclerView rvHot;
    private BaseQuickAdapter<GetOutPlaceCityListResp.OutPlaceCity, BaseViewHolder> hotAdapter;
    private CitySelectSideBar sideBar;
    private PinnedHeaderListView lvCity;
    private CityListAdapter cityListAdapter;
    // 搜索结果
    private RelativeLayout rlytSearchResult;
    private RecyclerView rvSearchResult;
    private SearchResultAdapter searchResultAdapter;
    private LinearLayout error_view;

    // webview过来的话不刷新位置选择，只作为webview当前的筛选控件
    private boolean isFromWeb;
    private boolean locationPermissionWhenOnCreate;

    @Autowired(name = "extraMap")
    HashMap<String, String> extraMap;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_city_select;
    }

    @Override
    protected void initVars() {
        super.initVars();
        isFromWeb = getIntent().getBooleanExtra("isFromWeb", false);
        locationPermissionWhenOnCreate = PermissionUtils.checkLocationPermission(this);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initView() {
        super.initView();
        mImmersionBar.fitsSystemWindows(false).transparentStatusBar().statusBarDarkFont(true).init();

        searchView = (YhyTopSearchView) findViewById(R.id.llyt_top);

        rlytContent = (RelativeLayout) findViewById(R.id.rlyt_content);
        headView = LayoutInflater.from(this).inflate(R.layout.city_list_head_layout, null);
        rlytLocation = headView.findViewById(R.id.rlyt_location);
        llytLocation = headView.findViewById(R.id.llyt_location);
        tvLocation = headView.findViewById(R.id.tv_gps_location);
        rlytNoLocation = headView.findViewById(R.id.rl_no_location);
        tvGoSetting = headView.findViewById(R.id.tv_go_setting);
        llytHistory = headView.findViewById(R.id.llyt_history);
        rvHistory = headView.findViewById(R.id.rv_history);
        llytHot = headView.findViewById(R.id.llyt_hot);
        rvHot = headView.findViewById(R.id.rv_hot);

        lvCity = (PinnedHeaderListView) findViewById(R.id.lv_cities);
        sideBar = (CitySelectSideBar) findViewById(R.id.side_bar);

        rlytSearchResult = (RelativeLayout) findViewById(R.id.rlyt_search_result);
        rvSearchResult = (RecyclerView) findViewById(R.id.rv_search_result);
        error_view = (LinearLayout) findViewById(R.id.error_view);
        ((ImageView) error_view.findViewById(R.id.iv_error)).setImageResource(R.mipmap.default_page_search);
        ((TextView) error_view.findViewById(R.id.tv_tips)).setText("暂无搜索结果");
    }

    @Override
    protected void setListener() {
        super.setListener();

        searchView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                rlytContent.setVisibility(hasFocus ? View.GONE : View.VISIBLE);
                rlytSearchResult.setVisibility(hasFocus ? View.VISIBLE : View.GONE);

                if (!hasFocus) {
                    View view = getCurrentFocus();
                    InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    mInputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);

                }
            }
        });

        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (searchResultAdapter != null) {
                    if (TextUtils.isEmpty(s)) {
                        searchResultAdapter.getFilter().filter(null);
                    } else {
                        searchResultAdapter.getFilter().filter(s);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        sideBar.setOnTouchSectionListener(new CitySelectSideBar.OnTouchSectionListener() {
            @Override
            public void onTouchLetterSection(final int sectionIndex, CityIndexBean letterSection) {
                if (sectionIndex == 0) {
                    lvCity.setSelection(0);
                    lvCity.smoothScrollToPosition(0);
                    return;
                }
                //计算正确的位置
                int itemId = (int) cityListAdapter.getItemId(sectionIndex, 0);
                lvCity.setSelection(itemId);
                sideBar.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        sideBar.setSelectIndex(sectionIndex);
                    }
                }, 100);

            }
        });

        lvCity.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
//                lvCity.get

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (sideBar.isOnTuch()) return;
                sideBar.setSelectIndex(lvCity.getmCurrentSection() + 1);
            }
        });

        rlytLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (llytLocation.getVisibility() == View.VISIBLE) {
                    GetOutPlaceCityListResp.OutPlaceCity outPlaceCity = new GetOutPlaceCityListResp.OutPlaceCity();
                    outPlaceCity.name = LocationManager.getInstance().getStorage().getGprs_cityName();
                    outPlaceCity.lat = LocationManager.getInstance().getStorage().getGprs_lat();
                    outPlaceCity.lng = LocationManager.getInstance().getStorage().getGprs_lng();
                    outPlaceCity.isPublic = LocationManager.getInstance().getStorage().getGprs_isPublic();
                    outPlaceCity.cityCode = LocationManager.getInstance().getStorage().getGprs_cityCode();
                    citySelected(outPlaceCity);
                }
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();

        // 获取缓存数据
        String jsonCity = CityCache.getOutPlaceCityList(this);
        String jsonOriginCity = CityCache.getOutPlaceCityListOrigin(this);
        String jsonHotCity = CityCache.getHotCityList(this);
        String jsonIndex = CityCache.getCityListIndex(this);
        String jsonHistory = CityCache.getCityHistoryt(this);
        ArrayList<GetOutPlaceCityListResp.OutPlaceCity> originCityList = JSONUtils.convertToArrayList(jsonOriginCity, GetOutPlaceCityListResp.OutPlaceCity.class);
        ArrayList<CityListBean> cityList = JSONUtils.convertToArrayList(jsonCity, CityListBean.class);
        ArrayList<GetOutPlaceCityListResp.OutPlaceCity> hotList = JSONUtils.convertToArrayList(jsonHotCity, GetOutPlaceCityListResp.OutPlaceCity.class);
        ArrayList<CityIndexBean> indexList = JSONUtils.convertToArrayList(jsonIndex, CityIndexBean.class);
        ArrayList<GetOutPlaceCityListResp.OutPlaceCity> historyList = JSONUtils.convertToArrayList(jsonHistory, GetOutPlaceCityListResp.OutPlaceCity.class);

        if (originCityList == null || originCityList.size() == 0) return;

        // 索引
//        indexList.get(2).isSelect = true;
        sideBar.setSections(indexList);

        // gprs，热门和历史城市
        initGprs();

        if (hotList != null && hotList.size() > 0) {
            llytHot.setVisibility(View.VISIBLE);
            rvHot.setLayoutManager(new GridLayoutManager(this, 3));
            hotAdapter = new BaseQuickAdapter<GetOutPlaceCityListResp.OutPlaceCity, BaseViewHolder>(R.layout.city_tag_layout, hotList != null && hotList.size() > 6 ? new ArrayList<>(hotList.subList(0, 6)) : hotList) {
                @Override
                protected void convert(BaseViewHolder holder, final GetOutPlaceCityListResp.OutPlaceCity outPlaceCity) {
                    holder.setText(R.id.tv_city_name, outPlaceCity.name);
                    holder.getView(R.id.tv_city_name).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            citySelected(outPlaceCity);
                        }
                    });
                }
            };
            rvHot.setAdapter(hotAdapter);
        } else {
            llytHot.setVisibility(View.GONE);
        }

        if (historyList != null && historyList.size() > 0) {
            llytHistory.setVisibility(View.VISIBLE);
            rvHistory.setLayoutManager(new GridLayoutManager(this, 3));
            historyAdapter = new BaseQuickAdapter<GetOutPlaceCityListResp.OutPlaceCity, BaseViewHolder>(R.layout.city_tag_layout, historyList) {
                @Override
                protected void convert(BaseViewHolder holder, final GetOutPlaceCityListResp.OutPlaceCity outPlaceCity) {
                    holder.setText(R.id.tv_city_name, outPlaceCity.name);
                    holder.getView(R.id.tv_city_name).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            citySelected(outPlaceCity);
                        }
                    });
                }
            };
            rvHistory.setAdapter(historyAdapter);
        } else {
            llytHistory.setVisibility(View.GONE);
        }

        // 城市列表
        lvCity.addHeaderView(headView);
        cityListAdapter = new CityListAdapter(this, cityList);
        cityListAdapter.setOnItemClickListener(new CityListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(GetOutPlaceCityListResp.OutPlaceCity outPlaceCity) {
                citySelected(outPlaceCity);
            }
        });
        lvCity.setAdapter(cityListAdapter);

        // 搜素结果列表
        rvSearchResult.setLayoutManager(new LinearLayoutManager(this));
        searchResultAdapter = new SearchResultAdapter(this, originCityList, new ArrayList<GetOutPlaceCityListResp.OutPlaceCity>());
        rvSearchResult.setAdapter(searchResultAdapter);
        searchResultAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                if (!isFromWeb) {
                    // 埋点
                    HashMap<String, String> params = new HashMap<>();
                    params.put("name", searchView.getText());
                    params.put("type", searchResultAdapter.getItem(i).name);
                    params.put("pageName", extraMap.get(Analysis.PAGENAME));
                    Analysis.pushEvent(CitySelectActivity.this, AnEvent.PageSearchCity, extraMap.get(Analysis.UID), params);
                }
                citySelected(searchResultAdapter.getItem(i));
            }
        });
    }

    /**
     * 获取GPS位置信息
     */
    private void initGprs() {
        if (!PermissionUtils.checkLocationPermission(this)) {
            rlytNoLocation.setVisibility(View.VISIBLE);
            llytLocation.setVisibility(View.GONE);
            tvGoSetting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent localIntent = new Intent();
                    localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    if (Build.VERSION.SDK_INT >= 9) {
                        localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                        localIntent.setData(Uri.fromParts("package", getPackageName(), null));
                    } else if (Build.VERSION.SDK_INT <= 8) {
                        localIntent.setAction(Intent.ACTION_VIEW);
                        localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
                        localIntent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
                    }
                    startActivity(localIntent);
                }
            });

        } else {
            rlytNoLocation.setVisibility(View.GONE);
            llytLocation.setVisibility(View.VISIBLE);
            tvLocation.setText(LocationManager.getInstance().getStorage().getGprs_cityName());

        }
    }


    /**
     * 点选城市后逻辑处理
     *
     * @param outPlaceCity
     */
    private void citySelected(GetOutPlaceCityListResp.OutPlaceCity outPlaceCity) {
        // 按下Item更新 ManualLocation
        LocationManager.getInstance().getStorage().setManualLocation(outPlaceCity.name, outPlaceCity.cityCode,
                "全部", "-1",
                outPlaceCity.lat, outPlaceCity.lng, outPlaceCity.isPublic);
        // 加入历史搜索
        String jsonHistory = CityCache.getCityHistoryt(this);
        ArrayList<GetOutPlaceCityListResp.OutPlaceCity> historyList = JSONUtils.convertToArrayList(jsonHistory, GetOutPlaceCityListResp.OutPlaceCity.class);
        if (historyList == null || historyList.size() == 0) {
            ArrayList<GetOutPlaceCityListResp.OutPlaceCity> newHistoryList = new ArrayList<>();
            newHistoryList.add(outPlaceCity);
            CityCache.saveCityHistory(this, JSONUtils.toJson(newHistoryList));
            updataHistoryView(newHistoryList);
        } else {
            boolean include = false;
            for (GetOutPlaceCityListResp.OutPlaceCity city : historyList) {
                if (city.name.equals(outPlaceCity.name)) {
                    include = true;
                    break;
                }
            }
            if (!include) {
                historyList.add(0, outPlaceCity);
                if (historyList.size() > 3) {
                    historyList = new ArrayList<>(historyList.subList(0, 3));
                }
                CityCache.saveCityHistory(this, JSONUtils.toJson(historyList));
                updataHistoryView(historyList);
            }

        }
        // 发eventBus同步状态
        EventBus.getDefault().post(new EvBusLocationChange(outPlaceCity, isFromWeb));
        //关闭页面
        finish();

    }

    /**
     * 更新历史选择城市
     *
     * @param newHistoryList
     */
    private void updataHistoryView(ArrayList<GetOutPlaceCityListResp.OutPlaceCity> newHistoryList) {
        if (newHistoryList != null && newHistoryList.size() > 0) {
            llytHistory.setVisibility(View.VISIBLE);
            if (historyAdapter == null) {
                rvHistory.setLayoutManager(new GridLayoutManager(this, 3));
                historyAdapter = new BaseQuickAdapter<GetOutPlaceCityListResp.OutPlaceCity, BaseViewHolder>(R.layout.city_tag_layout, newHistoryList) {
                    @Override
                    protected void convert(BaseViewHolder holder, GetOutPlaceCityListResp.OutPlaceCity outPlaceCity) {
                        holder.setText(R.id.tv_city_name, outPlaceCity.name);
                    }
                };
                rvHistory.setAdapter(historyAdapter);
            } else {
                historyAdapter.setNewData(newHistoryList);
            }
        } else {
            llytHistory.setVisibility(View.GONE);
        }
    }


    public void setSearchResultView(ArrayList<GetOutPlaceCityListResp.OutPlaceCity> values) {
        if (values != null && values.size() > 0) {
            error_view.setVisibility(View.GONE);
            rvSearchResult.setVisibility(View.VISIBLE);
        } else {
            error_view.setVisibility(View.VISIBLE);
            rvSearchResult.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onNewBackPressed() {
        if (searchView.hasFocus()) {
            searchView.clearFocus();
        } else {
            super.onNewBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //每次都要重新获得一下位置
        boolean locationPermissionWhenOnResume = PermissionUtils.checkLocationPermission(this);
        if (locationPermissionWhenOnResume) {
            if (!locationPermissionWhenOnCreate) {
                locationPermissionWhenOnCreate = true;
                LocationManager.getInstance().startLocation(this);
            }
        } else {
            locationPermissionWhenOnCreate = false;
        }
    }

    public void onEvent(EvBusLocation evBusLocation) {
        //type==1001代表定位成功    type==4001 代表定位失败（包含KEY错误或网络问题导致定位的失败）
        switch (evBusLocation.getLocationType()) {
            case 1001:
                rlytNoLocation.setVisibility(View.GONE);
                llytLocation.setVisibility(View.VISIBLE);
                tvLocation.setText(LocationManager.getInstance().getStorage().getGprs_cityName());
                break;
        }
    }
}
