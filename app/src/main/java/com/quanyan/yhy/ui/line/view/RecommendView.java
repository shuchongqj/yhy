package com.quanyan.yhy.ui.line.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
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
 * Date:16/3/23
 * Time:09:47
 * Version 1.0
 */
public class RecommendView extends RelativeLayout {

    public RecommendView(Context context) {
        super(context);
        init(context);
    }

    public RecommendView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RecommendView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RecommendView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context){
        View.inflate(context, R.layout.layout_recommend_view, this);
        int paddLR = ScreenSize.convertDIP2PX(context.getApplicationContext(), 15);
        int paddTB = ScreenSize.convertDIP2PX(context.getApplicationContext(), 10);
        setPadding(paddLR, paddTB, paddLR, paddTB);
        setBackgroundColor(getResources().getColor(R.color.white));
        setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
    }

    /**
     * 设置推荐标题
     * @param title
     */
    public void setRecommendTitle(String title){
        ((TextView)findViewById(R.id.recommend_view_title)).setText(title);
    }

    /**
     * 设置推荐标题
     * @param stringID
     */
    public void setRecommendTitle(int stringID) {
        ((TextView)findViewById(R.id.recommend_view_title)).setText(stringID);
    }

    /**
     * 是否显示更多的图标
     */
    public void showMoreImg(){
        findViewById(R.id.recommend_view2).setVisibility(View.VISIBLE);
    }
}
