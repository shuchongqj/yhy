package com.quanyan.yhy.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.quanyan.yhy.R;

/**
 * Created with Android Studio.
 * Title:RatingBarView
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-5-11
 * Time:18:15
 * Version 1.1.0
 */


public class RatingBarView extends LinearLayout {

    public interface OnRatingListener {
        void onRating(Object bindObject, int RatingScore);
    }

    private boolean mClickable = true;
    private OnRatingListener onRatingListener;
    private Object bindObject;
    private float starImageSize;//星星大小
    private float starPaddingSize;//星星间距
    private int starCount;//星星总数
    private Drawable starEmptyDrawable;//未选中星星
    private Drawable starFillDrawable;//选中的星星
    private Drawable starHalfDrawable;//选中的半颗星星
    private int mStarCount;

    public void setClickable(boolean clickable) {
        this.mClickable = clickable;
    }

    public RatingBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.HORIZONTAL);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RatingBarView);
        starImageSize = ta.getDimension(R.styleable.RatingBarView_starImageSize, 20);
        starPaddingSize = ta.getDimension(R.styleable.RatingBarView_starPaddingSize, 0);
        starCount = ta.getInteger(R.styleable.RatingBarView_starCount, 5);
        starEmptyDrawable = ta.getDrawable(R.styleable.RatingBarView_starEmpty);
        starFillDrawable = ta.getDrawable(R.styleable.RatingBarView_starFill);
        starHalfDrawable = ta.getDrawable(R.styleable.RatingBarView_starHalf);
        ta.recycle();

        for (int i = 0; i < starCount; ++i) {
            ImageView imageView;
            if (i != starCount - 1) {
                imageView = getStarImageView(context, attrs, true);
            } else {
                imageView = getStarImageView(context, attrs, false);
            }

            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mClickable) {
                        mStarCount = indexOfChild(v) + 1;
                        setStar(mStarCount, false);
                        if (onRatingListener != null) {
                            onRatingListener.onRating(bindObject, mStarCount);
                        }
                    }
                }
            });
            addView(imageView);
        }
    }

    private ImageView getStarImageView(Context context, AttributeSet attrs, boolean isPadding) {
        ImageView imageView = new ImageView(context);
        LinearLayout.LayoutParams para = new LinearLayout.LayoutParams(Math.round(starImageSize), Math.round(starImageSize));
        //ViewGroup.LayoutParams para = new ViewGroup.LayoutParams((int)starImageSize, (int)starImageSize);
        // TODO:you can change gap between two stars use the padding
        if (isPadding) {
            para.setMargins(0, 0, Math.round(starPaddingSize), 0);
            //imageView.setPadding(0, 0, Math.round(starPaddingSize), 0);
        } else {
            para.setMargins(0, 0, 0, 0);
            //imageView.setPadding(0, 0, 0, 0);
        }
        imageView.setLayoutParams(para);
        imageView.setImageDrawable(starFillDrawable);
        //imageView.setImageDrawable(starEmptyDrawable);
        //imageView.setMaxWidth(10);
        //imageView.setMaxHeight(10);
        return imageView;
    }

    /**
     * 点击星星设置星星状态
     * @param starCount
     * @param animation
     */
    public void setStar(int starCount, boolean animation) {
        starCount = starCount > this.starCount ? this.starCount : starCount;
        starCount = starCount < 0 ? 0 : starCount;
        for (int i = 0; i < starCount; i++) {
            ((ImageView) getChildAt(i)).setImageDrawable(starFillDrawable);
            if (animation) {
                ScaleAnimation sa = new ScaleAnimation(0, 0, 1, 1);
                getChildAt(i).startAnimation(sa);
            }
        }
        for (int i = this.starCount - 1; i >= starCount; i--) {
            ((ImageView) getChildAt(i)).setImageDrawable(starEmptyDrawable);
        }
    }

    /**
     * 传值设置星星状态
     * @param grade
     */
    public void setHalfStar(float grade) {
        if (grade < 1 || grade > 5) {
            return;
        }
        int index = (int) grade;
        int point = (int) ((grade * 10) % 10);
        for (int i = 0; i < index; i++) {
            ((ImageView) getChildAt(i)).setImageDrawable(starFillDrawable);
        }
        for (int i = this.starCount - 1; i >= index; i--) {
            ((ImageView) getChildAt(i)).setImageDrawable(starEmptyDrawable);
        }
        if(point > 0){
            ((ImageView) getChildAt(index)).setImageDrawable(starHalfDrawable);
        }
    }

    public int getStarCount() {
        return starCount;
    }

    public void setStarFillDrawable(Drawable starFillDrawable) {
        this.starFillDrawable = starFillDrawable;
    }

    public void setStarEmptyDrawable(Drawable starEmptyDrawable) {
        this.starEmptyDrawable = starEmptyDrawable;
    }

    public void setStarCount(int startCount) {
        this.starCount = starCount;
    }

    public void setStarImageSize(float starImageSize) {
        this.starImageSize = starImageSize;
    }

    public void setBindObject(Object bindObject) {
        this.bindObject = bindObject;
    }

    public void setOnRatingListener(OnRatingListener onRatingListener) {
        this.onRatingListener = onRatingListener;
    }
}
