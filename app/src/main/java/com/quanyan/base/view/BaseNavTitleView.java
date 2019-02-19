package com.quanyan.base.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
 * Time:13:34
 * Version 1.0
 */
public class BaseNavTitleView extends RelativeLayout {

    public BaseNavTitleView(Context context) {
        super(context);
        initView(context);
    }

    public BaseNavTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public BaseNavTitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BaseNavTitleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ScreenSize.convertDIP2PX(context.getApplicationContext(), 48)));
        View.inflate(context, R.layout.base_nav_title_view, this);
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        findViewById(R.id.base_nav_view_left_layout).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 16/4/28 返回键
                if (getContext() instanceof Activity) {
                    ((Activity) getContext()).finish();
                }
            }
        });

        LinearLayout linearLayout = new LinearLayout(context);
        RelativeLayout.LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2);
        layoutParams.addRule(ALIGN_PARENT_BOTTOM);
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setBackgroundColor(getResources().getColor(R.color.neu_cccccc));
        addView(linearLayout);
    }

    /**
     * 显示左侧布局
     */
    public void showLeftLayout(){
        findViewById(R.id.base_nav_view_left_layout).setVisibility(VISIBLE);
    }

    /**
     * 显示右侧图片布局
     */
    public void showRightImgLayout(){
        findViewById(R.id.base_nav_view_right_layout).setVisibility(VISIBLE);
    }

    /**
     * 设置返回按钮的图标
     *
     * @param imgResID 图片资源ID
     */
    public void setLeftImg(int imgResID) {
        View view = findViewById(R.id.base_nav_view_left_layout);
        if(view.getVisibility() == GONE){
            view.setVisibility(View.VISIBLE);
        }
        ((ImageView) findViewById(R.id.base_nav_view_back_img)).setImageResource(imgResID);
    }

    /**
     * 设置标题栏左边图标
     *
     * @param imgResID 图片资源ID
     */
    public void setLeftImage(int imgResID) {
        View view = findViewById(R.id.base_nav_view_left_layout);
        if(view.getVisibility() == GONE){
            view.setVisibility(View.VISIBLE);
        }
        findViewById(R.id.base_nav_view_left_text).setVisibility(View.GONE);
        ImageView leftImg = ((ImageView) findViewById(R.id.base_nav_view_back_img));
        if (leftImg.getVisibility() == View.GONE) {
            leftImg.setVisibility(View.VISIBLE);
        }
        leftImg.setImageResource(imgResID);
    }

    /**
     * 设置左侧栏的点击事件
     *
     * @param leftImgClick {@link OnClickListener}
     */
    public void setLeftClick(OnClickListener leftImgClick) {
        findViewById(R.id.base_nav_view_left_layout).setOnClickListener(leftImgClick);
    }

    /**
     * 设置标题栏左边文本
     *
     * @param text {@link String}类型的字符串
     */
    public void setLeftText(String text) {
        View view = findViewById(R.id.base_nav_view_left_layout);
        if(view.getVisibility() == GONE){
            view.setVisibility(View.VISIBLE);
        }
        findViewById(R.id.base_nav_view_back_img).setVisibility(View.GONE);
        TextView leftText = (TextView) findViewById(R.id.base_nav_view_left_text);
        if (leftText.getVisibility() == View.GONE) {
            leftText.setVisibility(View.VISIBLE);
        }

        if (text != null) {
            leftText.setText(text);
        }
    }

    /**
     * 设置左边文本颜色
     * @param colorResId
     */
    public void setLeftTextColor(int colorResId) {
        findViewById(R.id.base_nav_view_back_img).setVisibility(View.GONE);
        TextView leftText = (TextView) findViewById(R.id.base_nav_view_left_text);
        if (leftText.getVisibility() == View.GONE) {
            leftText.setVisibility(View.VISIBLE);
        }

        if (colorResId != -1) {
            leftText.setTextColor(getContext().getResources().getColor(colorResId));
        }
    }


    /**
     * 设置界面标题
     *
     * @param titleText {@link String}类型的字符串
     */
    public void setTitleText(String titleText) {
        ((TextView) findViewById(R.id.base_nav_view_title)).setText(titleText);
    }

    /**
     * 设置界面标题
     *
     * @param stringResID {@link String}资源文件的ID
     */
    public void setTitleText(int stringResID) {
        ((TextView) findViewById(R.id.base_nav_view_title)).setText(stringResID);
    }


    /**
     * 设置右侧图片，会隐藏右侧文本布局
     *
     * @param imgResID 图片资源id
     */
    public void setRightImg(int imgResID) {
        ((ImageView) findViewById(R.id.base_nav_view_right_img)).setImageResource(imgResID);

    }

    /**
     * 设置右侧图片的点击事件
     *
     * @param rightImgClick
     */
    public void setRightImgClick(OnClickListener rightImgClick) {
        if (rightImgClick != null) {
            findViewById(R.id.base_nav_view_right_layout).setOnClickListener(rightImgClick);
        }
    }
}
