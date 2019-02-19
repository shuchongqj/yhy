package com.newyhy.views;


import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * RecycleView 自定义分割线
 * Created by Jiervs on 2018/6/20.
 */

public class YhyRecyclerDivider extends RecyclerView.ItemDecoration {
    private final Drawable mDivider;
    private final int mSize;
    private final int mPadding;
    private final int mOrientation;

    public YhyRecyclerDivider(Resources resources, @ColorRes int color, @DimenRes int size, int padding , int orientation) {
        mDivider = new ColorDrawable(resources.getColor(color));
        mSize = resources.getDimensionPixelSize(size);
        mPadding = resources.getDimensionPixelSize(padding);
        mOrientation = orientation;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left;
        int right;
        int top;
        int bottom;
        if (mOrientation == LinearLayoutManager.HORIZONTAL) {
            top = parent.getPaddingTop() + mPadding;
            bottom = parent.getHeight() - parent.getPaddingBottom() - mPadding;
            final int childCount = parent.getChildCount();
            for (int i = 0; i < childCount - 1; i++) {
                final View child = parent.getChildAt(i);
                final RecyclerView.LayoutParams params =
                        (RecyclerView.LayoutParams) child.getLayoutParams();
                left = child.getRight() + params.rightMargin;
                right = left + mSize;
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        } else {
            left = parent.getPaddingLeft() + mPadding;
            right = parent.getWidth() - parent.getPaddingRight() - mPadding;
            final int childCount = parent.getChildCount();
            for (int i = 0; i < childCount - 1; i++) {
                final View child = parent.getChildAt(i);
                final RecyclerView.LayoutParams params =
                        (RecyclerView.LayoutParams) child.getLayoutParams();
                top = child.getBottom() + params.bottomMargin;
                bottom = top + mSize;
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (mOrientation == LinearLayoutManager.HORIZONTAL) {
            outRect.set(0, 0, mSize, 0);
        } else {
            outRect.set(0, 0, 0, mSize);
        }
    }
}