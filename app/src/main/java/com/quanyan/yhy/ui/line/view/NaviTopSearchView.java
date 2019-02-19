package com.quanyan.yhy.ui.line.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quanyan.yhy.R;

/**
 * Created with Android Studio.
 * Title:AboutAndFeedController
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:zhaoxiaopo
 * Date:16/3/8
 * Time:11:14
 * Version 1.0
 */
public class NaviTopSearchView extends LinearLayout {

    private ImageView mLocateImg;
    private TextView mLocateText;
    private TextView mTitleText;

    public NaviTopSearchView(Context context) {
        super(context);
        init(context, null);
    }

    public NaviTopSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public NaviTopSearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public NaviTopSearchView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(final Context context, AttributeSet attributeSet){
        View.inflate(context, R.layout.layout_navi_search_location, this);
        setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        mTitleText = (TextView) findViewById(R.id.navi_search_title);
        findViewById(R.id.navi_search_location_back_layout).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 16/3/8 返回键
                if (context instanceof Activity) {
                    ((Activity) context).finish();

                }
            }
        });
        findViewById(R.id.navi_search_location_search_layout).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 16/3/8 顶部搜索栏点击
                if(mOnClickListener != null) {
                    mOnClickListener.onStartSearch();
                }
            }
        });

        findViewById(R.id.navi_search_location_locate_layout).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 16/3/11 选择出发地
                if(mOnClickListener != null) {
                    mOnClickListener.onStartCitySelect();
                }
            }
        });
        mLocateImg = (ImageView) findViewById(R.id.navi_search_location_locate_img);
        mLocateText = (TextView) findViewById(R.id.navi_search_location_locate_text);
    }

    private OnNavTopClickListener mOnClickListener;
    public void setOnNavTopListener(OnNavTopClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
    }

    /**
     * 设置标题内容
     * @param titleText
     */
    public void setTitleText(String titleText){
        if(mTitleText != null) {
            mTitleText.setText(titleText);
        }
    }

    /**
     * 是否显示右侧出发地搜索按钮
     * @param isShow
     */
    public void showDepartureSearch(boolean isShow){
        findViewById(R.id.navi_search_location_locate_layout).setVisibility(isShow ? View.VISIBLE : View.INVISIBLE);
    }

    /**
     * 设置出发地名称
     * @param cityName
     */
    public void setStartCtiyName(String cityName){
        if(cityName.length() >= 5){
            ((TextView)findViewById(R.id.navi_search_location_locate_text)).setText(cityName.substring(0, 3) + "...");
        }else {
            ((TextView) findViewById(R.id.navi_search_location_locate_text)).setText(cityName);
        }
    }

    public interface OnNavTopClickListener{
        void onStartCitySelect();

        void onStartSearch();
    }
}
