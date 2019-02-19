package com.quanyan.yhy.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.quanyan.yhy.R;

import java.util.ArrayList;

/**
 * Created with Android Studio.
 * Title:AboutAndFeedController
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:zhaoxiaopo
 * Date:16/3/31
 * Time:17:37
 * Version 1.0
 */
public class NumStarView extends LinearLayout{

    private Context mContext;
    private LinearLayout mViewParent;

    private int mStarNum = 5;//默认星星的数量

    //默认星星的高度和宽度
    private int mWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
    private int mHeight = ViewGroup.LayoutParams.WRAP_CONTENT;

    private int mCurrentSelectIndex = 0;//当前选中的索引

    private boolean isClickable = false;//设置星星是否可以被点击

    public NumStarView(Context context) {
        super(context);
        initView(context);
    }

    public NumStarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public NumStarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public NumStarView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    /**
     * 初始化
     * @param context
     */
    private void initView(Context context){
        mContext = context;
        View.inflate(context, R.layout.layout_num_star_view, this);
        mViewParent = (LinearLayout) findViewById(R.id.num_star_view_parent);
        addStarImg(mViewParent);
    }

    /**
     * 保存imageView的引用
     */
    private ArrayList<ImageView> mStarsImg = new ArrayList<>();

    /**
     * 根据设置的星星数量，添加图片
     * @param viewParent
     */
    private void addStarImg(LinearLayout viewParent){
        for(int index = 0; index < mStarNum; index++){
            ImageView imageView = new ImageView(mContext.getApplicationContext());
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.sl_rating_star));
            imageView.setLayoutParams(new LayoutParams(mWidth, mHeight));
            imageView.setOnClickListener(new StarViewClick(index));
            mStarsImg.add(imageView);
            viewParent.addView(imageView);
        }
    }

    /**
     * 更行星星的状态信息
     * @param starIndex
     * @param endIndex
     * @param selected
     * @param half
     */
    private void updateTheState(int starIndex, int endIndex, boolean selected, boolean half) {
        for(int updateIndex = starIndex; updateIndex <= endIndex; updateIndex ++){
            if(updateIndex == endIndex && half){
                mStarsImg.get(updateIndex).setImageResource(R.mipmap.ic_star_half);
            }else{
                mStarsImg.get(updateIndex).setImageDrawable(getResources().getDrawable(R.drawable.sl_rating_star));
            }
            mStarsImg.get(updateIndex).setSelected(selected);
        }
    }

    /**
     * 设置分数
     * @param rating
     */
    public void setRating(int rating) {
        int grade = (rating - 1) > 0 ? (rating - 1) : 0;
        if(mCurrentSelectIndex < grade) {
            updateTheState(mCurrentSelectIndex, grade, !mStarsImg.get(grade).isSelected(), false);
        }else{
            updateTheState(grade, mCurrentSelectIndex, !mStarsImg.get(grade).isSelected(), false);
        }
        mCurrentSelectIndex = grade;
    }

    /**
     * 设置分数
     * @param rating
     */
    public void setRating(float rating){
        int grade = ((int) rating - 1) > 0 ? ((int) rating - 1) : 0;
        if(mCurrentSelectIndex < grade) {
            updateTheState(mCurrentSelectIndex, grade, !mStarsImg.get(grade).isSelected(), true);
        }else{
            updateTheState(grade, mCurrentSelectIndex, !mStarsImg.get(grade).isSelected(), true);
        }
        mCurrentSelectIndex = grade;
    }

    /**
     * 星星的点击事件
     */
    private class StarViewClick implements View.OnClickListener {

        public int mIndex;
        public StarViewClick(int index){
            this.mIndex = index;
        }

        @Override
        public void onClick(View v) {
            if(isClickable) {
                if(mCurrentSelectIndex < mIndex) {
                    updateTheState(mCurrentSelectIndex, mIndex, !mStarsImg.get(mIndex).isSelected(), true);
                }else{
                    updateTheState(mIndex, mCurrentSelectIndex, !mStarsImg.get(mIndex).isSelected(), true);
                }
                mCurrentSelectIndex = mIndex;
            }
        }
    }

    /**
     * 设置星星是否可以点击
     * @param isClickable
     */
    public void isClickable(boolean isClickable){
        this.isClickable = isClickable;
    }

    /**
     * 设置星星的高度，宽度
     * @param width
     * @param height
     */
    public void setStarLayoutParams(int width, int height){
        this.mWidth = width;
        this.mHeight = height;
    }

    /**
     * 设置星星的数量
     * @param starNum
     */
    public void setStarNum(int starNum){
        this.mStarNum = starNum;
        mViewParent.removeAllViews();
        addStarImg(mViewParent);
    }
}
