package com.quanyan.yhy.ui.master.view;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quanyan.base.view.customview.NoScrollGridView;
import com.quanyan.yhy.R;
import com.quanyan.yhy.ui.adapter.base.BaseAdapterHelper;
import com.quanyan.yhy.ui.adapter.base.QuickAdapter;
import com.quanyan.yhy.ui.master.activity.MasterAdviceListActivity;
import com.quanyan.yhy.ui.master.helper.MasterViewHelper;
import com.quanyan.yhy.ui.tab.view.hometab.HomeViewInterface;
import com.yhy.common.beans.net.model.item.BoothItemsResult;
import com.yhy.common.beans.net.model.trip.ShortItem;

import java.util.ArrayList;

/**
 * Created with Android Studio.
 * Title:MasterConsultHomeWKTView
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-7-20
 * Time:15:55
 * Version 1.0
 * Description:
 */
public class MasterConsultHomeWKTView implements HomeViewInterface {

    private View mConsultWKTView;
    private Context mContext;
    private LinearLayout mMasterConsultContent;
    private RelativeLayout mMoreConsult;

    @Override
    public void setView(ViewGroup parentView, int index) {
        mContext = parentView.getContext();
        mConsultWKTView = View.inflate(mContext, R.layout.view_masterconsult_wkt, null);
        mMasterConsultContent = (LinearLayout) mConsultWKTView.findViewById(R.id.ll_master_consult);
        mMoreConsult = (RelativeLayout) mConsultWKTView.findViewById(R.id.rl_more_consult);
        initClick();
        if (index <= parentView.getChildCount()) {
            parentView.addView(mConsultWKTView, index);
        } else {
            parentView.addView(mConsultWKTView);
        }
    }


    private void initClick() {
        mMoreConsult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MasterAdviceListActivity.gotoMasterAdviceListActivity(mContext);
            }
        });
    }

    @Override
    public void handleData(Object data) {
        if (data != null && data instanceof BoothItemsResult) {
            BoothItemsResult result = (BoothItemsResult) data;
            if (result != null && result.boothItemList != null && result.boothItemList.size() != 0) {
                if (mMasterConsultContent != null) {
                    mMasterConsultContent.removeAllViews();
                }
                for (int i = 0; i < result.boothItemList.size(); i++) {
                    View v = LayoutInflater.from(mContext).inflate(R.layout.view_masterconsult_wkt_item, null);
                    TextView title = (TextView) v.findViewById(R.id.tv_master_consult);
                    TextView consultNum = (TextView) v.findViewById(R.id.view_master_consult_people_num);
                    final NoScrollGridView mNoScrollGridView = (NoScrollGridView) v.findViewById(R.id.nsg_dest_list1);
                    if (TextUtils.isEmpty(result.boothItemList.get(i).themeTitle)) {
                        title.setText("暂时没有标题");
                    } else {
                        title.setText(result.boothItemList.get(i).themeTitle);
                    }
                    if (result.boothItemList.get(i).consultCount >= 0) {
                        consultNum.setText("" + result.boothItemList.get(i).consultCount);
                    } else {
                        consultNum.setText("0");
                    }
                    QuickAdapter mMasterConsultAdapter = new QuickAdapter<ShortItem>(mContext, R.layout.view_item_masterhome_consult, new ArrayList<ShortItem>()) {
                        @Override
                        protected void convert(BaseAdapterHelper helper, ShortItem item) {
                            MasterViewHelper.handleMasterAdviceListItem((Activity) mContext, helper, item);
                        }
                    };
                    mNoScrollGridView.setAdapter(mMasterConsultAdapter);
                    mMasterConsultAdapter.replaceAll(result.boothItemList.get(i).shortItemList);
                    mMasterConsultContent.addView(v);
                }
            }
        }
    }
}
