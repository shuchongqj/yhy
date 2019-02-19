package com.quanyan.yhy.ui.consult.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quanyan.yhy.R;
import com.yhy.common.beans.net.model.comment.DimensionInfo;

/**
 * Created with Android Studio.
 * Title:MessageConsultCommentStars
 * Description:咨询评论星星
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:邓明佳
 * Date:2016/7/11
 * Time:16:16
 * Version 1.0
 */
public class MessageConsultCommentStars extends LinearLayout {
    TextView mTitle, mScore;
    ImageView mStar[] = new ImageView[5];
    private int mStarCount = 5;

    public MessageConsultCommentStars(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTitle = (TextView) findViewById(R.id.tv_title);
        initImageStar(0, R.id.iv_star1);
        initImageStar(1, R.id.iv_star2);
        initImageStar(2, R.id.iv_star3);
        initImageStar(3, R.id.iv_star4);
        initImageStar(4, R.id.iv_star5);
        mScore = (TextView) findViewById(R.id.tv_score);
    }

    /**
     * 初始化星星图标
     *
     * @param position
     * @param resId
     */
    private void initImageStar(int position, int resId) {
        mStar[position] = (ImageView) findViewById(resId);
        mStar[position].setTag(position);
        mStar[position].setOnClickListener(onStarClickListener);
    }

    private OnClickListener onStarClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            mStarCount = position + 1;
            mScore.setText(getContext().getString(R.string.comment_score, mStarCount));
            setStarsImg(position);
        }
    };

    /**
     * 设置星星图标
     *
     * @param position
     */
    private void setStarsImg(int position) {
        for (int i = 0; i < mStar.length; i++) {
            mStar[i].setImageResource(i > position ? R.mipmap.ic_star_normal : R.mipmap.ic_star_selected);
        }
    }

    public int getStarCount() {
        return mStarCount;
    }

    public void setContent(String text) {
        mTitle.setText(text);
    }

    DimensionInfo dimension;

    public void setDimension(DimensionInfo dimension) {
        this.dimension = dimension;
        setContent(dimension.dimensionName);
    }

    public String getCode() {
        return dimension == null ? null : dimension.dimensionCode;
    }
}
