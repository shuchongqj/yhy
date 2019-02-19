package com.quanyan.yhy.ui.line.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.R;

/**
 * Created with Android Studio.
 * Title:AboutAndFeedController
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:zhaoxiaopo
 * Date:16/3/11
 * Time:14:01
 * Version 1.0
 */
public class LineTopSearchView extends LinearLayout {

    private OnSearchClickListener mSearchViewClickListener;
    private String mPageTitle;

    public LineTopSearchView(Context context) {
        super(context);
        init(context);
    }

    public LineTopSearchView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        init(context);
    }

    public LineTopSearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LineTopSearchView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

//    public static final int LINE_HOME_PAGE = 1 ;
//    public static final int LINE_RESULT_HOME_PAGE = 2 ;
//    private String mLineType ;
//    private int mPageType = LINE_HOME_PAGE;
//    /**
//     * 设置页面类型和线路类型
//     * @param lineType
//     * @param pageType
//     */
//    public void setPageTypeAndLineType(String lineType,int pageType){
//        mPageType = pageType;
//        mLineType = lineType;
//    }

    private void init(final Context context) {
        View.inflate(context, R.layout.layout_line_top_search_view, this);
        findViewById(R.id.line_top_search_view_search_value).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 16/3/11 关键字搜索
                if (mSearchViewClickListener != null) {
                    mSearchViewClickListener.onSearchTextViewClick();
                }
            }
        });
        
        findViewById(R.id.line_top_search_view_location_layout).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 16/3/11 目的地搜索
                if (mSearchViewClickListener != null) {
                    mSearchViewClickListener.onDestSelectClick();
                }
            }
        });
        findViewById(R.id.line_top_search_view_free_pack).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 16/3/14 跟团游／自由行 顶部搜索（目的地／关键字）
                if(mSearchViewClickListener != null){
                    mSearchViewClickListener.onSearchTextViewClick();
                }
            }
        });
    }

    public void setSearchViewClickListener(OnSearchClickListener searchViewClickListener) {
        mSearchViewClickListener = searchViewClickListener;
    }

    /**
     * 显示跟团，自由行的样式
     */
    public void showFreePackStyle(){
        findViewById(R.id.line_top_search_view_common).setVisibility(View.GONE);
        findViewById(R.id.line_top_search_view_free_pack).setVisibility(View.VISIBLE);
    }

    /**
     * 周边，同城设置城市名称
     * @param cityName
     */
    public void setCityName(String cityName){
        if(TextUtils.isEmpty(cityName)){
            return;
        }
        if(cityName.length() >= 5){
            ((TextView)findViewById(R.id.line_top_search_view_location_tv)).setText(cityName.substring(0, 3) + "...");
        }else {
            ((TextView) findViewById(R.id.line_top_search_view_location_tv)).setText(cityName);
        }
    }

    public void setPageTitle(String pageTitle) {
        mPageTitle = pageTitle;
    }

    /**
     * 设置搜索关键字
     * @param keyWord
     */
    public void setSearchKeyWord(String keyWord){
        if(!StringUtil.isEmpty(keyWord)) {
            ((TextView)findViewById(R.id.line_top_search_view_search_value)).setText(keyWord);
        }
    }

    public interface OnSearchClickListener{
        void onDestSelectClick();

        void onSearchTextViewClick();
    }
}
