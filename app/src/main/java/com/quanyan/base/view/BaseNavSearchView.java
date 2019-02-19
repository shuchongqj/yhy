package com.quanyan.base.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quanyan.base.util.ScreenSize;
import com.quanyan.yhy.R;

/**
 * Created with Android Studio.
 * Title:AboutAndFeedController
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:zhaoxiaopo
 * Date:16/4/28
 * Time:14:14
 * Version 1.0
 */
public class BaseNavSearchView extends LinearLayout {

    public BaseNavSearchView(Context context) {
        super(context);
        initView(context);
    }

    public BaseNavSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public BaseNavSearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BaseNavSearchView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ScreenSize.convertDIP2PX(context.getApplicationContext(),48)));
        View.inflate(context, R.layout.base_nav_search_view, this);
        findViewById(R.id.base_nav_search_left_img_layout).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 16/4/28 返回
                if(getContext() instanceof Activity){
                    ((Activity) getContext()).finish();
                }
            }
        });
        findViewById(R.id.base_nav_search_left_loc_layout).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 16/4/28 定位城市显示
                if(getContext() instanceof Activity){
                    ((Activity) getContext()).finish();
                }
            }
        });
        findViewById(R.id.base_nav_search_right_img_layout).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 16/4/28 跳转地图
                if(getContext() instanceof Activity){
                    ((Activity) getContext()).finish();
                }
            }
        });
        findViewById(R.id.base_nav_search_label_layout).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 16/4/28 搜索跳转
                if(getContext() instanceof Activity){
                    ((Activity) getContext()).finish();
                }
            }
        });
    }

    /**
     * 设置城市名称
     * @param cityName
     */
    public void setCityName(String cityName){
        if(TextUtils.isEmpty(cityName)){
            ((TextView)findViewById(R.id.base_nav_search_left_loc_name)).setText(cityName);
        }
    }
}
