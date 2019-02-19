package com.quanyan.base.view.customview.noticeview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.quanyan.yhy.R;
import com.yhy.common.beans.net.model.RCShowcase;

import java.util.List;

/**
 * Created with Android Studio.
 * Title:AboutAndFeedController
 * Description:类似头条的滚动文本布局，提供左侧图片和右侧文本内容
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:zhaoxiaopo
 * Date:16/3/3
 * Time:09:15
 * Version 1.0
 */
public class ScrollNoticeView extends LinearLayout {

    private static final String TAG = "ScrollNoticeView";
    /**
     * 滚动的父布局
     */
    private ViewFlipper mViewFlipper;
    /**
     * 滚动文本的点击事件回调接口
     */
    private ScrollNoticeInterface mScrollNoticeClick;
    /**
     * 滚动文本的图片标示
     */
    private ImageView mImageView;

    public ScrollNoticeView(Context context) {
        super(context);
        init(context);
    }

    public ScrollNoticeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * 设置滚动文本点击事件的回调接口
     * @param scrollNoticeClick 接口实例
     */
    public void setScrollNoticeClick(ScrollNoticeInterface scrollNoticeClick) {
        mScrollNoticeClick = scrollNoticeClick;
    }

    /**
     * 签到的点击事件
     * @param onSignClick
     */
    public void setOnSignClick(View.OnClickListener onSignClick){
        findViewById(R.id.scroll_notice_sign_on).setOnClickListener(onSignClick);
    }

    /**
     * 初始化自定义的布局
     */
    private void init(Context context) {
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        View.inflate(context, R.layout.scrollnoticebar, this);
        mViewFlipper = (ViewFlipper) findViewById(R.id.id_scrollNoticeTitle);
        mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_in_from_bottom));
        mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_out_to_top));
        mViewFlipper.startFlipping();

        mImageView = (ImageView) findViewById(R.id.notice_view_img);
    }

    /**
     * 网络请求内容后进行适配
     */
    public void bindNotices(final List<RCShowcase> data) {
        if(data != null) {
            int size = data.size();
            int childCount = mViewFlipper.getChildCount();
            if(childCount > size){
                int count = childCount - size;
                mViewFlipper.removeViews(size, count);
            }

            for(int i = 0; i < childCount && i < size; i ++){
                ((TextView)mViewFlipper.getChildAt(i)).setText(data.get(i).title);
            }

            for (int index = childCount; index < size; index++) {
                final int position = index;
                TextView textView = new TextView(getContext());
                textView.setText(data.get(position).title);
                textView.setGravity(Gravity.CENTER_VERTICAL);
                LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                layoutParams.gravity
                         = Gravity.CENTER_VERTICAL;
                mViewFlipper.addView(textView, layoutParams);
                textView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mScrollNoticeClick != null) {
                            mScrollNoticeClick.onNoticeClick(position);
                        }
                    }
                });
            }
        }
    }

    /**
     * 设置头条的左侧图片
     * @param resImg 图片资源id
     */
    public void setImage(int resImg){
        mImageView.setImageResource(resImg);
    }

    /**
     * 滑动文本的点击回调接口
     */
    public interface ScrollNoticeInterface{
        /**
         * 文本点击的回调事件
         * @param position 点击的文本位置
         */
        void onNoticeClick(int position);
    }

}