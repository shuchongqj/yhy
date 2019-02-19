package com.quanyan.yhy.ui.line.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.quanyan.yhy.R;
import com.quanyan.yhy.ui.adapter.base.BaseAdapterHelper;
import com.quanyan.yhy.ui.adapter.base.QuickAdapter;
import com.quanyan.yhy.ui.line.fragment.LineItemInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:AboutAndFeedController
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:zhaoxiaopo
 * Date:16/3/4
 * Time:13:51
 * Version 1.0
 */
public class RightPanelView extends LinearLayout {

//    private TextView mOverViewTitlte;
    private LinearLayout mOverViewContentLayout;
    private ListView mOverViewContent;
    private QuickAdapter<LineItemInfo> mLineDetailQuickAdapter;

    public RightPanelView(Context context) {
        super(context);
        init(context, null);
    }

    public RightPanelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RightPanelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RightPanelView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(final Context context, AttributeSet attributeSet){
        View.inflate(context, R.layout.line_overview_layout, this);

        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL|Gravity.RIGHT);
        setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOverViewItemClick != null) {
                    mOverViewItemClick.overViewClick(-1);
                    setVisibility(View.GONE);
                }
            }
        });
//        mOverViewTitlte = (TextView) findViewById(R.id.line_overview_title);
        mOverViewContentLayout = (LinearLayout) findViewById(R.id.line_overview_layout);
        mOverViewContent = (ListView) findViewById(R.id.base_listview);

        mLineDetailQuickAdapter = new QuickAdapter<LineItemInfo>(context, R.layout.item_line_overview,
                new ArrayList<LineItemInfo>()) {
            @Override
            protected void convert(BaseAdapterHelper helper, LineItemInfo item) {
                handleOverViewItem(helper, item);
            }
        };
        addOverViewHeader(context, mOverViewContent);
        mOverViewContent.setDividerHeight(0);
        mOverViewContent.setAdapter(mLineDetailQuickAdapter);
        mOverViewContent.setOnItemClickListener(mOnItemClickListener);
//        mOverViewTitlte.setOnClickListener(mOverViewClick);

        setVisibility(View.GONE);
    }

    /**
     * 添加行程概览的头部
     *
     * @param overViewContent
     */
    private void addOverViewHeader(Context context,ListView overViewContent) {
//        View headerView = View.inflate(context, R.layout.item_line_overview, null);
        findViewById(R.id.item_line_overview_indicate_layout).setVisibility(View.GONE);
        findViewById(R.id.item_line_overview_title).setVisibility(View.GONE);
        TextView textView = (TextView) findViewById(R.id.item_line_overview_content);
        textView.setText(R.string.tv_trip_overview);
        textView.setTextSize(15);
//        overViewContent.addHeaderView(headerView);
    }

    private void handleOverViewItem(BaseAdapterHelper helper, LineItemInfo item) {
        int position = helper.getPosition();
        if(position == 0){
            helper.setSelected(R.id.item_line_overview_title, true).setSelected(R.id.item_line_overview_indicate_img, true)
                    .setSelected(R.id.item_line_overview_content, true);
        }
        if(helper.getPosition() == mLineDetailQuickAdapter.getData().size() - 1){
            helper.setVisible(R.id.tem_line_overview_left_line, false);
        }else{
            helper.setVisible(R.id.tem_line_overview_left_line, true);
        }
        if(mCurrentSelectedPosition != -1 && mCurrentSelectedPosition == helper.getPosition()){
            helper.setSelected(R.id.item_line_overview_title, true).setSelected(R.id.item_line_overview_indicate_img, true)
                    .setSelected(R.id.item_line_overview_content, true);
        }else {
            helper.setSelected(R.id.item_line_overview_title, false).setSelected(R.id.item_line_overview_indicate_img, false)
                    .setSelected(R.id.item_line_overview_content, false);
        }
        helper.setText(R.id.item_line_overview_title, item.day)
                .setText(R.id.item_line_overview_content, TextUtils.isEmpty(item.title) ? "" :
                        item.title);
    }

//    private View.OnClickListener mOverViewClick = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            mOverViewTitlte.setVisibility(View.GONE);
//            mOverViewContentLayout.setVisibility(View.VISIBLE);
//        }
//    };

    private AbsListView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(mOverViewItemClick != null) {
//            mOverViewTitlte.setVisibility(View.VISIBLE);
                setVisibility(View.GONE);
//            mOverViewTitlte.setText("Day  " + position);
                mOverViewItemClick.overViewClick(position);
            }
        }
    };

    private int mCurrentSelectedPosition = -1;
    public void setSelection(int selected){
        mCurrentSelectedPosition = selected;
        mLineDetailQuickAdapter.notifyDataSetChanged();
    }

    /**
     * 传入数据
     * @param overViewData
     */
    public void bindViewData(List<LineItemInfo> overViewData){
        if(overViewData != null) {
            mLineDetailQuickAdapter.replaceAll(overViewData);
        }
    }

//    public void setOverViewListVisible(){
//        if(mOverViewContentLayout != null){
//            mOverViewContentLayout.setVisibility(View.VISIBLE);
//        }
//    }

//    public TextView getOverViewTitlte(){
//        return mOverViewTitlte;
//    }

    private OverViewItemClick mOverViewItemClick;

    public void setOverViewItemClick(OverViewItemClick overViewItemClick) {
        mOverViewItemClick = overViewItemClick;
    }

    public void setListClickable(boolean clickable){
        mOverViewContent.setEnabled(clickable);
    }

    public interface OverViewItemClick{
        void overViewClick(int pos);
    }
}
