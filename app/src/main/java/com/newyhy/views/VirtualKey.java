package com.newyhy.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class VirtualKey extends LinearLayout {

    private onLayoutKeyChange mLayoutKeyChange;

    public VirtualKey(Context context) {
        super(context);
    }

    public VirtualKey(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VirtualKey(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    /**
     * @param changed 布局发生改变 ture 没有改变False
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed)
            mLayoutKeyChange.onLayoutKeyChange(b);
    }


    public void setonLayoutKeyChange (onLayoutKeyChange layoutKeyChange) {
        mLayoutKeyChange = layoutKeyChange;
    }


    public interface onLayoutKeyChange {
        /**
         * 虚拟键盘状态监听
         *
         * @param b 布局距离底部的布局
         */
        void onLayoutKeyChange(int b);
    }

}