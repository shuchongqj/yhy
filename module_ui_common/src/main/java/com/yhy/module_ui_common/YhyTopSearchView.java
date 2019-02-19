package com.yhy.module_ui_common;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * 顶部搜索控件
 * <p>
 * Created by yangboxue on 2018/7/9.
 */

public class YhyTopSearchView extends RelativeLayout {

    private ImageView ivBack;
    private EditText etSearch;

    public YhyTopSearchView(Context context) {
        super(context);
        initView(context, null);
    }

    public YhyTopSearchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public YhyTopSearchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(final Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.yhy_top_search_layout, this);

        ivBack = findViewById(R.id.iv_back);
        etSearch = findViewById(R.id.et_search);

        TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.YhyTopSearchView);
        String hitText = t.getString(R.styleable.YhyTopSearchView_hit_text);

        etSearch.setHint(hitText);
        ivBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof Activity) {
                    ((Activity) context).onBackPressed();
                }
            }
        });

    }

    public void setOnFocusChangeListener(OnFocusChangeListener onFocusChangeListener) {
        etSearch.setOnFocusChangeListener(onFocusChangeListener);
    }

    public void addTextChangedListener(TextWatcher textWatcher) {
        etSearch.addTextChangedListener(textWatcher);
    }

    public void clearFocus() {
        etSearch.clearFocus();
    }

    public String getText() {
        return etSearch.getText().toString();
    }
}
