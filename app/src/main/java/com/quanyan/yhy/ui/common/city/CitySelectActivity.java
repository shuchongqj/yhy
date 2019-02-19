package com.quanyan.yhy.ui.common.city;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quanyan.base.BaseActivity;
import com.quanyan.base.util.LocalUtils;
import com.quanyan.base.view.BaseNavView;
import com.quanyan.yhy.R;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.common.city.adapter.CityAdapter;
import com.quanyan.yhy.ui.common.city.adapter.GridCurrentAdapter;
import com.quanyan.yhy.ui.common.city.adapter.GridHistoryAdapter;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.common.city.view.QuickIndexBar;
import com.yhy.common.beans.city.bean.AddressBean;
import com.yhy.common.beans.city.bean.ListCityBean;
import com.yhy.common.utils.JSONUtils;
import com.yhy.common.utils.SPUtils;
import com.yhy.location.LocationManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;

import de.greenrobot.event.EventBus;

public class CitySelectActivity extends BaseActivity implements TextWatcher, View.OnClickListener {
    private ListView mCityListView;
    private List<AddressBean> cityDatas;
    private ArrayList<AddressBean> hotCitys;
    private ArrayList<AddressBean> historyCitys;
    private CityAdapter mCityAdapter;
    private LinkedHashMap linkedHashMap;

    private TextView mSearchBox;
    private QuickIndexBar mQuickIndexBar;
    private TextView tv_cancle;
    private static int GRIDVIEW_COLUMN = 4;
    private AddressBean currentAddressBean;
    private static int REQCODE = 101;
    private static int MAX_HISTORY = 4;
    private String mSelectType;
    private GridCurrentAdapter mGridCurrentAdapter;
    private TextView tv_location_error;
    private TextView tv_current_location;

    /**
     * 跳转到城市选择
     *
     * @param activity
     * @param reqCode
     */
    public static void gotoSelectCity(Activity activity, int reqCode) {
        Intent intent = new Intent(activity, CitySelectActivity.class);
        activity.startActivityForResult(intent, reqCode);
    }

    /**
     * 跳转到城市选择
     * @param activity
     * @param type
     * @param reqCode
     */
    public static void gotoSelectCity(Activity activity, String type, int reqCode) {
        Intent intent = new Intent(activity, CitySelectActivity.class);
        intent.putExtra(SPUtils.EXTRA_TYPE, type);
        activity.startActivityForResult(intent, reqCode);
    }


    /*public static void gotoSelectCity(Fragment activity, String type, int reqCode) {
        Intent intent = new Intent(activity.getActivity(), CitySelectActivity.class);
        intent.putExtra(SPUtils.EXTRA_TYPE, type);
        activity.startActivityForResult(intent, reqCode);
    }*/

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.ac_city_select, null);
    }

    private BaseNavView mBaseNavView;

    @Override
    public View onLoadNavView() {
        mBaseNavView = new BaseNavView(this);
        mBaseNavView.setTitleText(getString(R.string.city_select_sourcehome_title));
        return mBaseNavView;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        currentAddressBean = LocalUtils.citycodeToLocal(this);
        //获取历史城市
        historyCitys = SPUtils.getCityHistoryAddress(this);
        //获取类型
        mSelectType = getIntent().getStringExtra(SPUtils.EXTRA_TYPE);
        if(mSelectType == null){
            mSelectType = "";
        }
        initTitle();
        initDataList();
        init();
    }

    private void initTitle() {
        RelativeLayout rl_title_line = (RelativeLayout) findViewById(R.id.rl_title_line);
        LinearLayout ll = (LinearLayout) findViewById(R.id.top_bar_search_layout);
        ll.setVisibility(View.VISIBLE);
        tv_cancle = (TextView) findViewById(R.id.tv_cancle);
        mSearchBox = (TextView) findViewById(R.id.tv_search_box);
        mSearchBox.setText(getString(R.string.city_select_top_tvtext));
        rl_title_line.setVisibility(View.GONE);
        /*if(mSelectType.equals(ValueCommentType.CITY_SOURCE_HOME)){
            setTitleText(getString(R.string.city_select_sourcehome_title));
        }else {
            setTitleText(getString(R.string.city_select_other_title));
        }*/
    }

    private void init() {
        mQuickIndexBar = (QuickIndexBar) findViewById(R.id.quickIndexBar);
        mCityListView = (ListView) findViewById(R.id.lv_city);
        creatLocationCityHeadView();//添加定位头
        //有历史记录才添加头
        if(historyCitys != null && historyCitys.size() > 0){
            mQuickIndexBar.doLetters(0);
            creatHistoryCityHeadView();//添加历史头
        }else {
            mQuickIndexBar.doLetters(1);
        }

        mCityAdapter = new CityAdapter(cityDatas);
        mCityListView.setAdapter(mCityAdapter);
        mCityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int headerViewsCount = mCityListView.getHeaderViewsCount();
                if(position >= headerViewsCount){
                    saveCitySelect((AddressBean) mCityAdapter.getItem(position - headerViewsCount));
                }

            }
        });

        tv_cancle.setOnClickListener(this);

        mSearchBox.setOnClickListener(this);

        //mSearchBox.addTextChangedListener(this);

        mQuickIndexBar.setmOnLetterUpdateListener(new QuickIndexBar.OnLetterUpdateListener() {
            @Override
            public void onLetterUpdate(String letter) {
                ToastUtil.showToast(CitySelectActivity.this, letter);

                if ((getString(R.string.city_index_location)).equals(letter)) {
                    mCityListView.setSelection(0);
                }

                if (getString(R.string.city_index_history).equals(letter)) {
                    mCityListView.setSelection(1);
                }

                for (int i = 0; i < cityDatas.size(); i++) {

                    AddressBean addressBean = cityDatas.get(i);
                    String str = String.valueOf(addressBean.getPinyin().charAt(0));

                    if (TextUtils.equals(letter, str)) {
                        mCityListView.setSelection(i + mCityListView.getHeaderViewsCount());
                        break;
                    }
                }
            }
        });
    }

    private void initDataList() {
        cityDatas = new ArrayList<>();/*LocalUtils.loadCities(getApplicationContext());*/
    }

    private void creatLocationCityHeadView() {
        View view = View.inflate(this, R.layout.item_list_indexaddress, null);
        TextView tv_before = (TextView) view.findViewById(R.id.tv_before);
        tv_location_error = (TextView) view.findViewById(R.id.tv_location_error);
        tv_current_location = (TextView) view.findViewById(R.id.tv_current_location);
        tv_before.setText(getString(R.string.label_current_city));
        //定位失败
        if(currentAddressBean == null){
            tv_location_error.setVisibility(View.VISIBLE);
            tv_current_location.setVisibility(View.GONE);
            tv_location_error.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().register(CitySelectActivity.this);
                    showLoadingView(getString(R.string.label_locationing));
                    LocationManager.getInstance().startLocation(CitySelectActivity.this);
                }
            });
        }else {
            //定位成功
            tv_location_error.setVisibility(View.GONE);
            tv_current_location.setVisibility(View.VISIBLE);
            tv_current_location.setText(currentAddressBean.getName());
            tv_current_location.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveCitySelect(currentAddressBean);
                }
            });
        }

        mCityListView.addHeaderView(view);
    }

    //重新定位
//    public void onEventMainThread(EvBusLocation evBusLocation){
//        hideLoadingView();
//        if(evBusLocation.getLocationType() == 1001){
//            currentAddressBean = LocalUtils.citycodeToLocal(this);
//            tv_location_error.setVisibility(View.GONE);
//            tv_current_location.setVisibility(View.VISIBLE);
//            tv_current_location.setText(currentAddressBean.getName());
//        }else {
//            ToastUtil.showToast(this, getString(R.string.label_locationing_error));
//        }
//        EventBus.getDefault().unregister(this);
//    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void creatHistoryCityHeadView() {
        View view = View.inflate(this, R.layout.item_list_header_history, null);
        TextView tv_before = (TextView) view.findViewById(R.id.tv_before);
        GridView gridView = (GridView) view.findViewById(R.id.gridView);
        tv_before.setText(getString(R.string.label_history_city));
        gridView.setNumColumns(GRIDVIEW_COLUMN);
        GridHistoryAdapter gridHistoryAdapter = new GridHistoryAdapter(historyCitys);
        gridView.setAdapter(gridHistoryAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                saveCitySelect(historyCitys.get(position));
            }
        });

        mCityListView.addHeaderView(view);
    }

    /**
     * 保存城市选择
     *
     * @param bean
     */
    private void saveCitySelect(AddressBean bean) {

        saveSP(bean);

        Intent intent = new Intent();
        intent.putExtra(SPUtils.EXTRA_SELECT_CITY, bean);
        setResult(RESULT_OK, intent);
        finish();
    }

    //保存到sp中
    private void saveSP(AddressBean bean) {

        if (historyCitys != null && historyCitys.size() > 0) {
            for (int i = 0; i < historyCitys.size(); i++) {
                if (bean.getCityCode().equals(historyCitys.get(i).getCityCode())) {
                    historyCitys.remove(i);
                    //historyCitys.add(0, bean);
                }
            }
        }

        historyCitys.add(0, bean);

        if (historyCitys.size() > MAX_HISTORY) {
            historyCitys.remove(historyCitys.size() - 1);
        }

        String historyCitys = JSONUtils.toJson(this.historyCitys);
        //存储历史城市信息
        SPUtils.setHistoryCity(this, historyCitys);

    }

    private void soutHotCity(ArrayList<AddressBean> hotCitys) {
        Collections.sort(hotCitys, new MyComparator());
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }


    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
        filterData(s.toString());
    }


    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_cancle:
                //ToastUtil.showToast(CitySelectActivity.this, "点击取消");
                finish();
                break;
            case R.id.tv_search_box:
                ListCityBean listCityBean = new ListCityBean();
                listCityBean.setDatas(cityDatas);
                NavUtils.gotoSearchSelectCity(CitySelectActivity.this, mSelectType, listCityBean, REQCODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQCODE && resultCode == RESULT_OK){
            AddressBean bean = (AddressBean) data.getSerializableExtra(SPUtils.EXTRA_DATA);
            saveCitySelect(bean);
        }
    }

    public class MyComparator implements Comparator {

        @Override
        public int compare(Object lhs, Object rhs) {
            return ((AddressBean) lhs).getSeq() - ((AddressBean) rhs).getSeq();
        }
    }

    private void filterData(String filterStr) {
        List<AddressBean> filterDateList = new ArrayList<>();
        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = cityDatas;
        } else {
            filterDateList.clear();
            for (AddressBean addressBean : cityDatas) {
                if (addressBean.getPinyin().contains(filterStr.toString().toUpperCase())
                        || addressBean.getName().contains(filterStr.toString())
                        || addressBean.getShortPinyin().contains(filterStr.toString())) {
                    filterDateList.add(addressBean);
                }
            }
        }

        Collections.sort(filterDateList, new PinyinComparator());
        mCityAdapter.updateListView(filterDateList);

    }

    public class PinyinComparator implements Comparator<AddressBean> {

        @Override
        public int compare(AddressBean lhs, AddressBean rhs) {
            return lhs.getPinyin().compareTo(rhs.getPinyin());
        }
    }

    public void back(View view){
        finish();
    }
}
