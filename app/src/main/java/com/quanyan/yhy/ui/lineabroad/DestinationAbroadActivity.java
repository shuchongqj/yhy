package com.quanyan.yhy.ui.lineabroad;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;

import com.lidroid.xutils.ViewUtils;
import com.newyhy.activity.HomeMainTabActivity;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.util.StringUtil;
import com.quanyan.base.view.BaseNavView;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.AnalyDataValue;
import com.quanyan.yhy.common.ItemType;
import com.quanyan.yhy.eventbus.EvBusDestCityChoose;

import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.base.utils.TCEventHelper;
import com.quanyan.yhy.ui.line.LineActivity;
import com.quanyan.yhy.ui.lineabroad.activity.HotCityFragment;
import com.quanyan.yhy.ui.master.activity.SearchFragment;
import com.quanyan.yhy.ui.master.activity.SearchInterface;
import com.quanyan.yhy.util.PinyinUtil;
import com.quanyan.yhy.view.SearchEditText;
import com.yhy.common.beans.city.bean.AddressBean;
import com.yhy.common.beans.net.model.user.Destination;
import com.yhy.common.utils.CommonUtil;
import com.yhy.common.utils.SPUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Created with Android Studio.
 * Title:DestinationAbroadActivity
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-6-1
 * Time:9:46
 * Version 1.1.0
 */

public class DestinationAbroadActivity extends BaseActivity implements AdapterView.OnItemClickListener, SearchInterface {

    private BaseNavView mBaseNavView;
//    @ViewInject(R.id.lv_area_name)
//    private ListView mAreaListView;

    //    @ViewInject(R.id.fl_dest_abroad_fragment)
//    private FrameLayout mFrameLayout;
//    private QuickAdapter<Destination> mAdapter;
    private List<Destination> mDestinations;//服务器返回的数据
    //    private FragmentManager mFragmentManager;
//    private AbroadController mController;
//    private String mType;
    private Map<Integer, Boolean> mSelectedmap = new HashMap<Integer, Boolean>();
    private int mCurrentIndex = -1;

    private SearchFragment mSearchFragment;

    /**
     * 跳转到目的地城市选择
     *
     * @param activity
     * @param reqCode
     */
    public static void gotoDestinationAbroadActivity(Activity activity,
                                                     String type,
                                                     String itemType,
                                                     String title,
                                                     String source,
                                                     int reqCode) {
        Intent intent = new Intent(activity, DestinationAbroadActivity.class);
        if (!StringUtil.isEmpty(type)) {
            intent.putExtra(SPUtils.EXTRA_TYPE, type);
        }
        if (!StringUtil.isEmpty(title)) {
            intent.putExtra(SPUtils.EXTRA_TITLE, title);
        }

        if (!StringUtil.isEmpty(source)) {
            intent.putExtra(SPUtils.EXTRA_SOURCE, source);
        }

        if (!StringUtil.isEmpty(itemType)) {
            intent.putExtra(SPUtils.EXTRA_ITEM_TYPE, itemType);
        }
        if (reqCode == -1) {
            activity.startActivity(intent);
        } else {
            activity.startActivityForResult(intent, reqCode);
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mBaseNavView.getSearchBox().hasFocus()) {
                mBaseNavView.getSearchBox().clearFocus();
                return true;
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        CommonUtil.hideInput(this);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);

        mSearchFragment = SearchFragment.getInstance(getIntent().getStringExtra(SPUtils.EXTRA_TITLE), getIntent().getStringExtra(SPUtils.EXTRA_ITEM_TYPE), DestinationAbroadActivity.class.getSimpleName());
        //监听导航栏搜索的焦点事件
        mBaseNavView.getSearchBox().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mBaseNavView.showSeachView(false, true, true, getString(R.string.hint_destination_abroad_search));
                    mBaseNavView.setRightText(getString(R.string.cancel));
                    getSupportFragmentManager().beginTransaction().replace(R.id.fl_dest_abroad_search_layout, mSearchFragment).commit();
                } else {
                    mBaseNavView.showSeachView(true, false, true, getString(R.string.hint_destination_abroad_search));
                    mBaseNavView.showLeftLayout();
                    getSupportFragmentManager().beginTransaction().remove(mSearchFragment).commit();
                    //隐藏输入法
                    CommonUtil.hideInput(DestinationAbroadActivity.this);
                }
            }
        });
        HotCityFragment mHotCityFragment = HotCityFragment.getInstance(getIntent().getStringExtra(SPUtils.EXTRA_TYPE), getIntent().getStringExtra(SPUtils.EXTRA_TITLE), getIntent().getStringExtra(SPUtils.EXTRA_ITEM_TYPE), getIntent().getStringExtra(SPUtils.EXTRA_SOURCE));
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_dest_abroad_fragment, mHotCityFragment).commit();
    }

    @Override
    public View onLoadContentView() {
        View view = View.inflate(this, R.layout.ac_abroad_destination, null);
        ViewUtils.inject(this, view);
        return view;
    }

    @Override
    public View onLoadNavView() {
        mBaseNavView = new BaseNavView(this);
        if (!ItemType.CONSULT.equals(getIntent().getStringExtra(SPUtils.EXTRA_ITEM_TYPE))) {
            mBaseNavView.showSeachView(true, false, true, getString(R.string.hint_destination_abroad_search));
            mBaseNavView.setLeftImage(R.mipmap.arrow_back_gray);
            mBaseNavView.setRightTextClick(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mBaseNavView.getSearchBox().setText("");
                    mBaseNavView.getSearchBox().clearFocus();
                }
            });
        }
        return mBaseNavView;
    }

    @Override
    public boolean isTopCover() {
        return false;
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
        map.put(AnalyDataValue.KEY_PID, cityCode);
        map.put(AnalyDataValue.KEY_NAME, cityName);
        map.put(AnalyDataValue.KEY_TYPE, AnalyDataValue.getType(getIntent().getStringExtra(SPUtils.EXTRA_ITEM_TYPE)));
        TCEventHelper.onEvent(this, AnalyDataValue.DESTINATION_CHOICE, map);
        if ((HomeMainTabActivity.class.getSimpleName().equals(getIntent().getStringExtra(SPUtils.EXTRA_SOURCE)) || LineActivity.class.getSimpleName().equals(getIntent().getStringExtra(SPUtils.EXTRA_SOURCE)))
                && (ItemType.TOUR_LINE.equals(getIntent().getStringExtra(SPUtils.EXTRA_ITEM_TYPE)) || ItemType.FREE_LINE.equals(getIntent().getStringExtra(SPUtils.EXTRA_ITEM_TYPE)) ||
                ItemType.TOUR_LINE_ABOARD.equals(getIntent().getStringExtra(SPUtils.EXTRA_ITEM_TYPE)) || ItemType.FREE_LINE_ABOARD.equals(getIntent().getStringExtra(SPUtils.EXTRA_ITEM_TYPE)))) {
            NavUtils.gotoLineSearchResultActivity(this,
                    getIntent().getStringExtra(SPUtils.EXTRA_TITLE),
                    getIntent().getStringExtra(SPUtils.EXTRA_ITEM_TYPE),
                    null,
                    cityCode,
                    cityName,
                    null,
                    -1);
            finish();
        } else {
            AddressBean ab = new AddressBean();
            ab.setCityCode(cityCode);
            ab.setName(cityName);
            ab.setPinyin(PinyinUtil.getPinyin(cityName));
            Intent intent = new Intent();
            intent.putExtra(SPUtils.EXTRA_SELECT_CITY, ab);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public SearchEditText getSearchEditText() {
        return mBaseNavView.getSearchBox();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
