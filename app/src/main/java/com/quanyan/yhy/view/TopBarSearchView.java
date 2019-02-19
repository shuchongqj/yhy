package com.quanyan.yhy.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quanyan.yhy.R;

/**
 * Created with Android Studio.
 * Title:TopBarSearchView
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-3-15
 * Time:9:42
 * Version 1.0
 * Description:
 */
public class TopBarSearchView extends LinearLayout implements View.OnClickListener {

    private ClearEditText mSearch_clearedittext;
    private LinearLayout mSm_title_bar_right_panel;

    private SearchListener searchListener;


    public TopBarSearchView(Context context) {
        super(context);
        init(context);
    }

    public TopBarSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TopBarSearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TopBarSearchView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater mLayoutInflater = LayoutInflater.from(context);
        View view = mLayoutInflater.inflate(R.layout.topbarsearchview, null);

        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        rlp.bottomMargin = 0;
        rlp.topMargin = 0;
        rlp.leftMargin = 0;
        rlp.rightMargin = 0;

        mSearch_clearedittext = (ClearEditText) view.findViewById(R.id.search_clearedittext);
        mSm_title_bar_right_panel = (LinearLayout) view.findViewById(R.id.sm_title_bar_right_panel);

        mSm_title_bar_right_panel.setOnClickListener(this);

        mSearch_clearedittext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (searchListener != null) {
                        searchListener.editorAction(mSearch_clearedittext);
                    }
                    return true;
                }
                return false;
            }
        });

        addView(view, rlp);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sm_title_bar_right_panel:
                if (searchListener != null) {
                    searchListener.cancleClick();
                }
                break;
        }
    }

    public void setSearchListener(SearchListener listener) {
        this.searchListener = listener;
    }

    public interface SearchListener {
        public void cancleClick();

        public void editorAction(EditText editText);
    }


}
