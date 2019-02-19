package com.quanyan.yhy.ui.base.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import com.quanyan.base.util.ScreenSize;


/**
 * 可以自适应高度的 ViewPager
 * 
 */
public class WrapContentHeightViewPager extends ViewPager {

    /**
     * 5:2
     */
    public static final float SCALE_VP = 750 / 300f;

    float mScale = 0;
    
    public WrapContentHeightViewPager(Context context) {
        super(context);
    }

    public WrapContentHeightViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = ScreenSize.getScreenWidth(getContext());
        heightMeasureSpec = MeasureSpec.makeMeasureSpec((int)(width/(mScale != 0 ? mScale : SCALE_VP)),
                MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setScale(float sc){
        mScale = sc;
    }

}
