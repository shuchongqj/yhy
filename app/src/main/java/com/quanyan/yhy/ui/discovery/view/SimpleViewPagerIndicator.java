package com.quanyan.yhy.ui.discovery.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quanyan.yhy.R;

import java.util.ArrayList;
import java.util.List;

public class SimpleViewPagerIndicator extends LinearLayout {

    private static final int COLOR_INDICATOR_COLOR = Color.GREEN;

    public List<TextView> mTextViews = new ArrayList<>();
    private List<String> mTitles = new ArrayList<>();
    private int mTabCount;
    private int mIndicatorColor = COLOR_INDICATOR_COLOR;
    private float mTranslationX;
    private Paint mPaint = new Paint();
    private int mTabWidth;
    private ViewPagerIndicatorClick mClickListener;

    public SimpleViewPagerIndicator(Context context) {
        this(context, null);
    }

    public SimpleViewPagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint.setColor(getResources().getColor(R.color.red_ying));
        mPaint.setStrokeWidth(9.0F);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mTabCount == 0)
            return;
        mTabWidth = w / mTabCount;
    }

    public void setTitles(List<String> titles) {
        mTitles.clear();
        mTitles.addAll(titles);
        mTabCount = titles.size();
        generateTitleView();

    }

    /**
     * 获取tabpage的标题
     *
     * @return list
     */
    public List<TextView> getTabTitleViews() {
        return mTextViews;
    }

    public void setIndicatorColor(int indicatorColor) {
        this.mIndicatorColor = indicatorColor;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        canvas.save();
        canvas.translate(mTranslationX, getHeight() - 2);
        canvas.drawLine(0, 0, mTabWidth, 0, mPaint);
        canvas.restore();
    }

    public void scroll(int position, float offset) {
        /**
         * <pre>
         *  0-1:position=0 ;1-0:postion=0;
         * </pre>
         */
        mTranslationX = getWidth() / mTabCount * (position + offset);
        invalidate();
    }

    public void setTabClickListener(ViewPagerIndicatorClick listener) {
        this.mClickListener = listener;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 更新tab页中的内容
     *
     * @param titles String数组
     */
    public void updateTheTabContent(List<String> titles) {
        mTitles.clear();
        mTitles.addAll(titles);
        mTabCount = titles.size();
        generateTitleView();
    }

    public int mUpdateTitleIndex = 0;

    private void generateTitleView() {
        if (getChildCount() > 0) {
            mTextViews.clear();
            this.removeAllViews();
        }
        int count = mTitles.size();

        setWeightSum(count);
        for (int i = 0; i < count; i++) {
            final int position = i;
            TextView tv = new TextView(getContext());
            LayoutParams lp = new LayoutParams(0,
                    LayoutParams.MATCH_PARENT);
            lp.weight = 1;
            tv.setGravity(Gravity.CENTER);
            tv.setTextColor(getResources().getColorStateList(R.color.tv_orange_black_selected));
            tv.setText(mTitles.get(i));
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            tv.setLayoutParams(lp);
            if (mUpdateTitleIndex == position) {
                tv.setSelected(true);
            }
            tv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.tabClick(position);
                }
            });
            addView(tv);
            mTextViews.add(tv);
        }
    }

    public interface ViewPagerIndicatorClick {
        void tabClick(int position);
    }

    public void showUnReadCount(int index, int count) {
        TextView textView = mTextViews.get(index);
    }

}
