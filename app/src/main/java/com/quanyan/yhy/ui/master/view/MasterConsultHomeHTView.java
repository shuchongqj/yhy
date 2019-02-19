package com.quanyan.yhy.ui.master.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.quanyan.base.view.customview.NoScrollGridView;
import com.quanyan.yhy.R;
import com.quanyan.yhy.ui.adapter.base.BaseAdapterHelper;
import com.quanyan.yhy.ui.adapter.base.QuickAdapter;
import com.quanyan.yhy.ui.tab.view.hometab.HomeViewInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:MasterConsultHomeHTView
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-7-20
 * Time:15:57
 * Version 1.0
 * Description:
 */
public class MasterConsultHomeHTView implements HomeViewInterface {

    private View mConsultHTView;
    private NoScrollGridView mNoScrollGridView;
    private TextView mTitle;
    private TextView mConsultNum;
    private QuickAdapter mMasterConsultAdapter;
    private Context mContext;

    private List<String> mList;

    @Override
    public void setView(ViewGroup parentView, int index) {
        mContext = parentView.getContext();
        mConsultHTView = View.inflate(mContext, R.layout.view_masterhome_consult_ht, null);
        mNoScrollGridView = (NoScrollGridView) mConsultHTView.findViewById(R.id.nsg_dest_list);
        mTitle = (TextView) mConsultHTView.findViewById(R.id.tv_master_consult);
        mConsultNum = (TextView) mConsultHTView.findViewById(R.id.view_master_consult_people_num);
        testList();
        mMasterConsultAdapter = new QuickAdapter<String>(mContext, R.layout.view_item_masterhome_consult, new ArrayList<String>()) {
            @Override
            protected void convert(BaseAdapterHelper helper, String item) {

            }
        };
        mNoScrollGridView.setAdapter(mMasterConsultAdapter);
        mMasterConsultAdapter.replaceAll(mList);

        if (index <= parentView.getChildCount()) {
            parentView.addView(mConsultHTView, index);
        } else {
            parentView.addView(mConsultHTView);
        }
    }

    private void testList() {
        mList = new ArrayList<String>();
        mList.add("1");
        mList.add("2");
        mList.add("3");
        mList.add("4");
    }

    @Override
    public void handleData(Object data) {
//        mMasterConsultAdapter = new QuickAdapter<RCShowcase>(mContext, R.layout.view_item_masterhome_consult, new ArrayList<RCShowcase>()) {
//            @Override
//            protected void convert(BaseAdapterHelper helper, RCShowcase item) {
//
//            }
//        };

        if (data instanceof HashMap) {

        }
    }
}
