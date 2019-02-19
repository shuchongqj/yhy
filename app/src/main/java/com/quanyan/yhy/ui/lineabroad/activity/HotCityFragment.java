package com.quanyan.yhy.ui.lineabroad.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.quanyan.base.BaseFragment;
import com.quanyan.base.baseenum.IActionTitleBar;
import com.quanyan.base.yminterface.ErrorViewClick;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.ErrorCode;
import com.quanyan.yhy.ui.adapter.base.BaseAdapterHelper;
import com.quanyan.yhy.ui.adapter.base.QuickAdapter;
import com.quanyan.yhy.ui.lineabroad.controller.AbroadController;
import com.yhy.common.beans.net.model.user.Destination;
import com.yhy.common.beans.net.model.user.DestinationList;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.SPUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with Android Studio.
 * Title:HomeDestinationFragment
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:wjm
 * Date:2016-7-21
 * Time:20:10
 * Version 1.1.0
 */

public class HotCityFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    //左侧listview
    @ViewInject(R.id.lv_area_name)
    private ListView mAreaListView;


    private HomeDestinationFragment mHomeDestinationFragment;
    private AbroadDestinationFragment mAbroadDestinationFragment;

    private QuickAdapter<Destination> mAdapter;
    private List<Destination> mDestinations;//服务器返回的数据
    private FragmentManager mFragmentManager;
    private AbroadController mController;
    private String mType;
    private Map<Integer, Boolean> mSelectedmap = new HashMap<Integer, Boolean>();
    private int mCurrentIndex = -1;



    public static HotCityFragment getInstance(String type, String pageTitle, String itemType, String source) {
        HotCityFragment fragment = new HotCityFragment();
        Bundle bundle = new Bundle();
//        bundle.putSerializable(SPUtils.EXTRA_DATA, datas);
        bundle.putString(SPUtils.EXTRA_TYPE, type);//
        bundle.putString(SPUtils.EXTRA_SOURCE, source);//
        bundle.putString(SPUtils.EXTRA_ITEM_TYPE, itemType);//
        bundle.putString(SPUtils.EXTRA_TITLE, pageTitle);//
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mController = new AbroadController(getActivity(), mHandler);
        mType = getActivity().getIntent().getStringExtra(SPUtils.EXTRA_TYPE);
        mAdapter = new QuickAdapter<Destination>(getActivity(), R.layout.item_abroad_dest_area, new ArrayList<Destination>()) {
            @Override
            protected void convert(BaseAdapterHelper helper, Destination item) {
                LinearLayout llTextView = helper.getView(R.id.ll_tv_area);
                TextView textView = helper.getView(R.id.tv_area_name);
                if (mSelectedmap.get(helper.getPosition())) {
                    llTextView.setBackgroundColor(Color.WHITE);
                } else {
                    llTextView.setBackgroundResource(R.drawable.dest_area_menu_selected);
                }
                helper.setText(R.id.tv_area_name, item.name);
            }
        };
        mAreaListView.setAdapter(mAdapter);
        mFragmentManager = getActivity().getSupportFragmentManager();
        mAreaListView.setOnItemClickListener(this);
        reloadDestionList();
    }

    /**
     * 加载目的地列表
     */
    private void reloadDestionList() {
        mController.doQueryDestinationTree(getActivity(), mType);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        hideLoadingView();
        hideErrorView(null);
        switch (msg.what) {
            case ValueConstants.MSG_GET_ABROAD_DESTINATION_OK:
                DestinationList result = (DestinationList) msg.obj;
                if (result != null) {
                    handlerData(result);
                }
                break;
            case ValueConstants.MSG_GET_ABROAD_DESTINATION_KO:
                showErrorView(null, ErrorCode.NETWORK_UNAVAILABLE == msg.arg1 ?
                        IActionTitleBar.ErrorType.NETUNAVAILABLE : IActionTitleBar.ErrorType.ERRORNET, "", "", "", new ErrorViewClick() {
                    @Override
                    public void onClick(View view) {
                        reloadDestionList();
                        showLoadingView("");
                    }
                });
                break;
        }
    }

    //处理区域数据
    private void handlerData(DestinationList result) {
        if (result.value != null && result.value.size() > 0) {
            if (mDestinations == null) {
                mDestinations = new ArrayList<>();
            }
            mDestinations = result.value;
            mAdapter.addAll(mDestinations);
            mAdapter.notifyDataSetChanged();
            if (mDestinations != null && mDestinations.size() > 0) {
                for (int i = 0; i < mDestinations.size(); i++) {
                    mSelectedmap.put(i, false);
                    if (mDestinations.get(i).isInnerArea) {
                        mSelectedmap.put(i, true);
                        mCurrentIndex = i;
                        switchFragment(0, mDestinations.get(i));
                    }
                }
                //switchFragment(0, mDestinations.get(mDestinations.size() - 1));
            }

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

    //切换fragment
    private synchronized void switchFragment(int pos, Destination destination) {
        switch (pos) {
            case 0:
                if (mHomeDestinationFragment == null) {
                    mHomeDestinationFragment = HomeDestinationFragment.getInstance(destination,
                            mType,
                            getActivity().getIntent().getStringExtra(SPUtils.EXTRA_TITLE),
                            getActivity().getIntent().getStringExtra(SPUtils.EXTRA_ITEM_TYPE),
                            getActivity().getIntent().getStringExtra(SPUtils.EXTRA_SOURCE));
                }
                if (!mHomeDestinationFragment.isAdded() && !mHomeDestinationFragment.isResumed()) {
//                    Bundle bundle_1 = new Bundle();
//                    bundle_1.putSerializable(SPUtils.EXTRA_DATA, destination);
//                    bundle_1.putString(SPUtils.EXTRA_TYPE, mType);
//                    mHomeDestinationFragment.setArguments(bundle_1);
                    if (mAbroadDestinationFragment != null) {
                        mFragmentManager.beginTransaction().hide(mAbroadDestinationFragment).commit();
                    }
                    mFragmentManager.beginTransaction().add(R.id.fl_dest_abroad_fragment2, mHomeDestinationFragment).commit();
                } else {
                    if (mAbroadDestinationFragment != null) {
                        mFragmentManager.beginTransaction().hide(mAbroadDestinationFragment).show(mHomeDestinationFragment).commit();
                    } else {
                        mFragmentManager.beginTransaction().show(mHomeDestinationFragment).commit();
                    }
                }

                break;
            case 1:
                if (mAbroadDestinationFragment == null) {
                    mAbroadDestinationFragment = AbroadDestinationFragment.getInstance(destination,
                            getActivity().getIntent().getStringExtra(SPUtils.EXTRA_TITLE),
                            getActivity().getIntent().getStringExtra(SPUtils.EXTRA_ITEM_TYPE),
                            getActivity().getIntent().getStringExtra(SPUtils.EXTRA_SOURCE));
                }
                if (!mAbroadDestinationFragment.isAdded() && !mAbroadDestinationFragment.isResumed()) {
                    if (mHomeDestinationFragment != null) {
                        mFragmentManager.beginTransaction().hide(mHomeDestinationFragment).commit();
                    }
//                    Bundle bundle_2 = new Bundle();
//                    bundle_2.putSerializable(SPUtils.EXTRA_DATA, destination);
//                    mAbroadDestinationFragment.setArguments(bundle_2);
                    if (mHomeDestinationFragment != null) {
                        mFragmentManager.beginTransaction().hide(mHomeDestinationFragment).commit();
                    }
                    mFragmentManager.beginTransaction().add(R.id.fl_dest_abroad_fragment2, mAbroadDestinationFragment).commit();
                } else {
                    if (mHomeDestinationFragment != null) {
                        mFragmentManager.beginTransaction().hide(mHomeDestinationFragment).show(mAbroadDestinationFragment).commit();
                    } else {
                        mFragmentManager.beginTransaction().show(mAbroadDestinationFragment).commit();
                    }
                    mAbroadDestinationFragment.setDestinations(destination);
                }
                break;
        }
    }

    @Override
    public View onLoadContentView() {
        View view = View.inflate(getActivity(), R.layout.hotcity_fragment, null);
        ViewUtils.inject(this, view);
        return view;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //点击状态
        if (-1 != mCurrentIndex) {
            mSelectedmap.put(mCurrentIndex, false);
        }
        mSelectedmap.put(position, true);
        mCurrentIndex = position;

        mAdapter.notifyDataSetChanged();

        if (mDestinations.get(position).isInnerArea) {
            //境内目的地
            switchFragment(0, mDestinations.get(position));
        } else {
            //境外目的地
            switchFragment(1, mDestinations.get(position));
        }
    }

}
