package com.newyhy.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by Jiervs on 2018/6/2.
 */

public class UgcScrollView extends ScrollView {

    private OnUgcScrollChanged listener;
    public interface OnUgcScrollChanged{
        void onScrollChanged(int l, int t, int oldl, int oldt);
    }

    public void addOnScrollChanged(OnUgcScrollChanged listener) {
        this.listener = listener;
    }

    public UgcScrollView(Context context) {
        super(context);
    }

    public UgcScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UgcScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (null != listener) listener.onScrollChanged(l, t, oldl, oldt);
    }
}
