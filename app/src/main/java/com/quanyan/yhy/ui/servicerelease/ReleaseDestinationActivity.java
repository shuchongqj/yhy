package com.quanyan.yhy.ui.servicerelease;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.baseenum.IActionTitleBar;
import com.quanyan.base.util.StringUtil;
import com.quanyan.base.view.BaseNavView;
import com.quanyan.base.yminterface.ErrorViewClick;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.ErrorCode;
import com.quanyan.yhy.common.ItemType;
import com.quanyan.yhy.ui.adapter.base.BaseAdapterHelper;
import com.quanyan.yhy.ui.adapter.base.QuickAdapter;
import com.quanyan.yhy.ui.base.utils.DialogUtil;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.lineabroad.bean.AbroadAreaBean;
import com.quanyan.yhy.ui.lineabroad.bean.AbroadResultBean;
import com.quanyan.yhy.ui.lineabroad.bean.DestAbroadHelper;
import com.quanyan.yhy.ui.lineabroad.bean.SimpleNameComparator;
import com.quanyan.yhy.ui.lineabroad.controller.AbroadController;
import com.yhy.common.beans.net.model.user.Destination;
import com.yhy.common.beans.net.model.user.DestinationList;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.SPUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with Android Studio.
 * Title:ReleaseDestinationActivity
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-7-15
 * Time:10:11
 * Version 1.1.0
 */

public class ReleaseDestinationActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private BaseNavView mBaseNavView;
    @ViewInject(R.id.lv_area_name)
    private ListView mAreaListView;

    @ViewInject(R.id.rl_home_contain)
    private RelativeLayout mRLHomeContain;//境内布局

    @ViewInject(R.id.rl_abroad_contain)
    private RelativeLayout mRLAbroadContain;//境外布局

    @ViewInject(R.id.ll_word_container)
    private LinearLayout mWordContainer;//右侧字母显示境内

    @ViewInject(R.id.ll_word_container_abroad)
    private LinearLayout mWordContainerAbroad;////右侧字母显示境外

    @ViewInject(R.id.lv_city_list)
    private ListView mHomeCityList;//境内listview

    @ViewInject(R.id.gv_abroad)
    private ListView mAbroadCityList;//境外listview

    @ViewInject(R.id.btn_right)
    private TextView mBtnOk;//确定按钮

    @ViewInject(R.id.gv_select_contain)
    private GridView mSelectContain;//选中的城市显示

    private List<Destination> mSelectList = new ArrayList<>();//选中的列表



    private QuickAdapter<Destination> mAdapter;
    private QuickAdapter<AbroadResultBean> mHomeAdapter;//国内adapter
    //private QuickAdapter<Destination> mAbroadAdapter;//国外adapter
    private QuickAdapter<AbroadResultBean> mNewAbroadAdapter;//国外adapter
    private QuickAdapter<Destination> mSelectAdapter;//选中的adapter
    private List<Destination> mDestinations;//服务器返回的数据
    private AbroadController mController;

    //国内
    private List<AbroadResultBean> mHomeCityResultBeans = new ArrayList<>();
    private List<AbroadAreaBean> mHomeAreaBeans = new ArrayList<>();

    //国外
    private List<AbroadResultBean> mAbroadCityResultBeans = new ArrayList<>();
    private List<AbroadAreaBean> mAbroadAreaBeans = new ArrayList<>();

    private String mType;
    private Map<Integer, Boolean> mSelectedmap = new HashMap<Integer, Boolean>();
    private int mCurrentIndex = -1;
    private Destination mHomeDestination;
    private Destination mAbroadDestination;
    private List<Destination> mAbroadDestList;//境外目的地列表
    private List<Destination> mBackSelectList;//记录所选的目的地并显示
    private Dialog mDialogCancle;


    public static void gotoReleaseDestinationActivity(Activity context, List<Destination> list, int reqCode) {
        Intent intent = new Intent(context, ReleaseDestinationActivity.class);
        intent.putExtra(SPUtils.EXTRA_DATA, (Serializable) list);
        context.startActivityForResult(intent, reqCode);
    }


    @Override
    protected void initView(Bundle savedInstanceState) {
        mController = new AbroadController(this, mHandler);
        mBackSelectList = (List<Destination>) getIntent().getSerializableExtra(SPUtils.EXTRA_DATA);
        //第一次进入设置
//        mSelectedmap.put(0, true);

        mBaseNavView.setLeftClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doCancleBack();
            }
        });

        mAdapter = new QuickAdapter<Destination>(this, R.layout.item_abroad_dest_area, new ArrayList<Destination>()) {
            @Override
            protected void convert(BaseAdapterHelper helper, Destination item) {
                LinearLayout llTextView = helper.getView(R.id.ll_tv_area);
                if (mSelectedmap.get(helper.getPosition())) {
                    llTextView.setBackgroundColor(Color.WHITE);
                } else {
                    llTextView.setBackgroundResource(R.drawable.dest_area_menu_selected);
                }
                helper.setText(R.id.tv_area_name, item.name);
            }
        };
        mAreaListView.setAdapter(mAdapter);
        mAreaListView.setOnItemClickListener(this);
        //国内list处理
        initHomeCityList();
        //国外list处理
        initAbroadCityList();
        //选中的城市
        initSelectList();

        reloadDestionList();

        mBtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //确定
                onfinish();
            }
        });
    }

    private void onfinish() {

        if(mDialogCancle != null){
            mDialogCancle.dismiss();
        }

        if(mSelectList == null || mSelectList.size() <= 0){
            ToastUtil.showToast(this, getString(R.string.toast_no_releae_dest));
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(SPUtils.EXTRA_DATA, (Serializable) mSelectList);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(KeyEvent.KEYCODE_BACK == keyCode){
            doCancleBack();
        }
        return true;
    }

    //返回操作
    private void doCancleBack() {
        if(changeState()){
            mDialogCancle = DialogUtil.showMessageDialog(this, null, getString(R.string.release_detail_cancle_text),
                    getString(R.string.save), getString(R.string.release_detail_not_save), new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            onfinish();
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    });
            mDialogCancle.show();
        }else {
            finish();
        }
    }

    private boolean changeState() {
        if(mBackSelectList == null || mBackSelectList.size() <= 0){
            if(mSelectList == null || mSelectList.size() <= 0){
                return false;
            }else {
                return true;
            }
        }else {
            if(mSelectList == null || mSelectList.size() <= 0){
                return true;
            }else {
                if(mBackSelectList.size() != mSelectList.size()){
                    return true;
                }else {
                    List<String> list1 = new ArrayList<>();
                    List<String> list2 = new ArrayList<>();
                    for (int i = 0; i < mBackSelectList.size(); i++) {
                        list1.add(mBackSelectList.get(i).name);
                    }
                    for (int i = 0; i < mSelectList.size(); i++) {
                        list2.add(mSelectList.get(i).name);
                    }

                    for (int i = 0; i < list1.size(); i++) {
                        if(!list2.contains(list1.get(i))){
                            return true;
                        }
                    }

                    return false;

                }
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mDialogCancle != null){
            mDialogCancle.dismiss();
        }
    }

    private void initSelectList() {
        mSelectAdapter = new QuickAdapter<Destination>(this, R.layout.item_select_dest, new ArrayList<Destination>()) {
            @Override
            protected void convert(final BaseAdapterHelper helper, Destination item) {
                helper.setText(R.id.tv_dest_name, item.name);
                helper.setOnClickListener(R.id.iv_delete_button, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mSelectList.remove(helper.getPosition());
                        refreshSelectLayout();
                    }
                });
            }
        };

        mSelectContain.setAdapter(mSelectAdapter);
    }

    private void initAbroadCityList() {
        mNewAbroadAdapter = new QuickAdapter<AbroadResultBean>(this, R.layout.item_home_dest_list, new ArrayList<AbroadResultBean>()) {
            @Override
            protected void convert(BaseAdapterHelper helper, AbroadResultBean item) {
                DestAbroadHelper.handlerReleaseDest(ReleaseDestinationActivity.this, helper, item, mSelectList);
            }
        };

        mAbroadCityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        mAbroadCityList.setAdapter(mNewAbroadAdapter);

        //点击事件处理
        DestAbroadHelper.setOnCityClickListener(new DestAbroadHelper.OnCityClickListener() {
            @Override
            public void onCityClick(View view, Destination info) {
                refreshSelectLayout(view, info);
            }
        });


        /*mAbroadAdapter = new QuickAdapter<Destination>(this, R.layout.item_abroad_dest, new ArrayList<Destination>()) {
            @Override
            protected void convert(BaseAdapterHelper helper, Destination item) {
                helper.setText(R.id.tv_abroad_name, item.name);
                helper.setTextColor(R.id.tv_abroad_name, getResources().getColor(R.color.neu_666666));
                helper.setBackgroundRes(R.id.tv_abroad_name, R.drawable.dest_abroad_bg);
                if(mSelectList != null && mSelectList.size() > 0){
                    for (int i = 0; i < mSelectList.size(); i++) {
                        if(item.name.equals(mSelectList.get(i).name)){
                            helper.setTextColor(R.id.tv_abroad_name, getResources().getColor(R.color.main));
                            helper.setBackgroundRes(R.id.tv_abroad_name, R.drawable.shape_dest_select_city_bg);
                        }
                    }
                }

            }
        };

        mAbroadCityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                refreshSelectLayout(view, mAbroadDestList.get(position));
                //changeBackground(view);
            }
        });

        mAbroadCityList.setAdapter(mAbroadAdapter);*/

    }

    private void refreshSelectLayout(View view, Destination info) {

        for (int i = 0; i < mSelectList.size(); i++) {
            if(mSelectList.get(i).name.equals(info.name)){
                mSelectList.remove(i);

                mSelectAdapter.replaceAll(mSelectList);
                mNewAbroadAdapter.notifyDataSetChanged();
                mHomeAdapter.notifyDataSetChanged();
                return;
            }
        }

        if(mSelectList.size() >= ValueConstants.MAX_SELECT_DEST_CITYS){
            ToastUtil.showToast(this, getString(R.string.toast_releae_dest_limit));
            return;
        }

        mSelectList.add(info);

        mSelectAdapter.replaceAll(mSelectList);

        mNewAbroadAdapter.notifyDataSetChanged();
        mHomeAdapter.notifyDataSetChanged();
    }

    private void refreshSelectLayout() {
        mSelectAdapter.replaceAll(mSelectList);
        mNewAbroadAdapter.notifyDataSetChanged();
        mHomeAdapter.notifyDataSetChanged();
    }

    private void initHomeCityList() {
        mHomeAdapter = new QuickAdapter<AbroadResultBean>(this, R.layout.item_home_dest_list, new ArrayList<AbroadResultBean>()) {
            @Override
            protected void convert(BaseAdapterHelper helper, AbroadResultBean item) {
                DestAbroadHelper.handlerReleaseDest(ReleaseDestinationActivity.this, helper, item, mSelectList);
            }
        };

        mHomeCityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        mHomeCityList.setAdapter(mHomeAdapter);

        //点击事件处理
        DestAbroadHelper.setOnCityClickListener(new DestAbroadHelper.OnCityClickListener() {
            @Override
            public void onCityClick(View view, Destination info) {
                refreshSelectLayout(view, info);
            }
        });

    }

    private void dataEncap(Destination destination, List<AbroadAreaBean> abroadAreaBeens, List<AbroadResultBean> AbroadResultBeans, ListView listView, LinearLayout layout) {
            if (destination != null && destination.hasChild) {
            //目的地城市所在的集合
            List<Destination> oneDestinations = destination.childList;
            if (oneDestinations != null && oneDestinations.size() > 0) {
                for (int i = 0; i < oneDestinations.size(); i++) {
                    AbroadAreaBean abroadAreaBean = new AbroadAreaBean();
                    abroadAreaBean.setDestination(oneDestinations.get(i));
                    abroadAreaBean.setSimpleName(oneDestinations.get(i).simpleCode);
                    //城市中所有的景点的集合
                    if (oneDestinations.get(i).hasChild && oneDestinations.get(i).childList != null && oneDestinations.get(i).childList.size() > 0) {
                        for (int j = 0; j < oneDestinations.get(i).childList.size(); j++) {
                            abroadAreaBean.setChildDestinations(oneDestinations.get(i).childList);
                        }
                    }
                    abroadAreaBeens.add(abroadAreaBean);
                }
            }
        }

        //根据集合进行分组
        soutList(abroadAreaBeens, AbroadResultBeans);
        //快速索引添加
        quickSort(AbroadResultBeans, listView, layout);
        //数据选择select
        addBackgroundList();


        //填充数据
        /*if (mHomeCityResultBeans != null && mHomeCityResultBeans.size() > 0) {
            mHomeAdapter.addAll(mHomeCityResultBeans);
        }*/

    }

    private void addBackgroundList() {
        if(mBackSelectList != null && mBackSelectList.size() > 0){
            mSelectList.clear();
            mSelectList.addAll(mBackSelectList);
            mSelectAdapter.replaceAll(mSelectList);
            mNewAbroadAdapter.notifyDataSetChanged();
            mHomeAdapter.notifyDataSetChanged();
        }
    }

    private void quickSort(final List<AbroadResultBean> AbroadResultBeans, final ListView listView, LinearLayout layout) {
        //其他字母添加
        if (AbroadResultBeans != null && AbroadResultBeans.size() > 0) {
            for (int i = 0; i < AbroadResultBeans.size(); i++) {
                View indexView = View.inflate(this, R.layout.quick_text_index, null);
                TextView textIndex = (TextView) indexView.findViewById(R.id.tv_index_name);
                textIndex.setText(AbroadResultBeans.get(i).getIndex());
                final int finalI = i;
                textIndex.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listView.setSelection(finalI + listView.getHeaderViewsCount());
                        ToastUtil.showToast(ReleaseDestinationActivity.this, AbroadResultBeans.get(finalI).getIndex());
                    }
                });
                layout.addView(indexView);
            }
        }

    }

    private void soutList(List<AbroadAreaBean> abroadAreaBeens, List<AbroadResultBean> AbroadResultBeans) {
        SimpleNameComparator simpleNameComparator = new SimpleNameComparator();
        Collections.sort(abroadAreaBeens, simpleNameComparator);

        List<AbroadAreaBean> beanList = new ArrayList<>();
        for (int i = 0; i < abroadAreaBeens.size(); i++) {
            if(!StringUtil.isEmpty(abroadAreaBeens.get(i).getSimpleName())){
                String currentIndexStr = String.valueOf(abroadAreaBeens.get(i).getSimpleName().charAt(0));
                if (i != abroadAreaBeens.size() - 1) {
                    String lasterIndexStr = String.valueOf(abroadAreaBeens.get(i + 1).getSimpleName().charAt(0));
                    beanList.add(abroadAreaBeens.get(i));
                    if (!TextUtils.equals(lasterIndexStr, currentIndexStr)) {
                        AbroadResultBean abroadResultBean = new AbroadResultBean();
                        abroadResultBean.setIndex(currentIndexStr);
                        List<AbroadAreaBean> beans = new ArrayList<>();
                        beans.addAll(beanList);
                        abroadResultBean.setLists(beans);
                        AbroadResultBeans.add(abroadResultBean);
                        beanList.clear();
                        //System.out.println("baojie" + cityResultBean.getLists().get(0).getName());
                    }
                } else {
                    beanList.add(abroadAreaBeens.get(i));
                    AbroadResultBean abroadResultBean = new AbroadResultBean();
                    abroadResultBean.setIndex(currentIndexStr);
                    List<AbroadAreaBean> beans = new ArrayList<>();
                    beans.addAll(beanList);
                    abroadResultBean.setLists(beans);
                    AbroadResultBeans.add(abroadResultBean);
                }
            }

        }
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

    /**
     * 加载目的地列表
     */
    private void reloadDestionList() {
        String type = ItemType.PUBLISH_SERVICE;
        mController.doQueryDestinationTree(ReleaseDestinationActivity.this, type);
    }

    //处理区域数据
    private void handlerData(DestinationList result) {
        if (result.value != null && result.value.size() > 0) {
            if (mDestinations == null) {
                mDestinations = new ArrayList<>();
            }
            mDestinations = result.value;
            mAdapter.addAll(mDestinations);
            if (mDestinations != null && mDestinations.size() > 0) {
                for (int i = 0; i < mDestinations.size(); i++) {
                    mSelectedmap.put(i, false);
                    if (mDestinations.get(i).isInnerArea) {
                        mSelectedmap.put(i, true);
                        mCurrentIndex = i;
                        mHomeDestination = mDestinations.get(i);
                    }else {
                        mAbroadDestination = mDestinations.get(i);
                    }
                }
            }

        } else {
            showNoDataPageView();
        }
        mAdapter.notifyDataSetChanged();

        //整理所需进行的处理数据,国内的
        dataEncap(mHomeDestination, mHomeAreaBeans, mHomeCityResultBeans, mHomeCityList, mWordContainer);
        //整理所需进行的处理数据,国外的
        dataEncap(mAbroadDestination, mAbroadAreaBeans, mAbroadCityResultBeans, mAbroadCityList, mWordContainerAbroad);

        //填充数据国内
        if (mHomeCityResultBeans != null && mHomeCityResultBeans.size() > 0) {
            mHomeAdapter.addAll(mHomeCityResultBeans);
        }
        //填充数据国外
        if (mAbroadCityResultBeans != null && mAbroadCityResultBeans.size() > 0) {
            mNewAbroadAdapter.addAll(mAbroadCityResultBeans);
        }

    }


    /**
     * 展示无数据的提示
     */
    private void showNoDataPageView() {
        showErrorView(null, IActionTitleBar.ErrorType.EMPTYVIEW, getString(R.string.hotel_empty_text), " ", "", null);
    }


    @Override
    public View onLoadContentView() {
        View view = View.inflate(this, R.layout.ac_release_destination, null);
        ViewUtils.inject(this, view);
        return view;
    }

    @Override
    public View onLoadNavView() {
        mBaseNavView = new BaseNavView(this);
        mBaseNavView.setTitleText(R.string.title_release_destination);
        return mBaseNavView;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
            homeOrAbroadVisable(true);
        } else {
            homeOrAbroadVisable(false);
            //境外目的地,填充数据然后显示
            //handleAbroadDatas(mDestinations.get(position));
        }
    }

    //境内和境外list的显示,true境内显示，false境外显示
    private void homeOrAbroadVisable(Boolean isVisable){
        if(isVisable){
            mRLHomeContain.setVisibility(View.VISIBLE);
            mRLAbroadContain.setVisibility(View.GONE);

        }else {
            mRLHomeContain.setVisibility(View.GONE);
            mRLAbroadContain.setVisibility(View.VISIBLE);
        }
    }

    /*private void handleAbroadDatas(Destination datas) {
        if(datas != null && datas.hasChild){
            mAbroadDestList = datas.childList;
            if(mAbroadDestList != null && mAbroadDestList.size() > 0){
                mAbroadAdapter.replaceAll(mAbroadDestList);
            }
        }
    }*/

}
