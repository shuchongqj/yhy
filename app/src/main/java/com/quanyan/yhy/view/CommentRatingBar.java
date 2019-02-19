package com.quanyan.yhy.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.quanyan.yhy.R;


/**
 * Created with Android Studio.
 * Title:CommentRatingBar
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:baojie
 * Date:2016-3-3
 * Time:9:32
 * Version 1.0
 */

public class CommentRatingBar extends FrameLayout {

    private TextView tv_desc;
    private RatingBarView ratingbar;
    private TextView tv_grade;
    private OnCommentRatingListener onCommentRatingListener;
    private static float RATING_INIT = 5;
    private static int RATING_NUMBER = 5;

    public CommentRatingBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public CommentRatingBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CommentRatingBar(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        View view = View.inflate(context, R.layout.view_comment_ratingbar, null);
        tv_desc = (TextView) view.findViewById(R.id.tv_desc);
        ratingbar = (RatingBarView) view.findViewById(R.id.ratingbar);
        tv_grade = (TextView) view.findViewById(R.id.tv_grade);

        initRatingBar();

        addView(view);
    }

    private void initRatingBar() {
        ratingbar.setClickable(true);
        tv_grade.setText(ratingbar.getStarCount() + "");
        ratingbar.setOnRatingListener(new RatingBarView.OnRatingListener() {
            @Override
            public void onRating(Object bindObject, int RatingScore) {
                tv_grade.setText(RatingScore + "");
                //doEnscapGrade(bindObject, RatingScore);
                if(onCommentRatingListener != null){
                    onCommentRatingListener.onCommentRating(bindObject, RatingScore);
                }
            }
        });
    }


    //设置描述信息
    public void setDesc(int desc){
        tv_desc.setText(desc);
    }

    public void setDesc(String desc){
        tv_desc.setText(desc);
    }

    //获取分数
    public int getGrade(){
       return Integer.parseInt(tv_grade.getText().toString());
    }

    public interface OnCommentRatingListener {
        void onCommentRating(Object bindObject, int RatingScore);
    }

    public void setOnCommentRatingListener(OnCommentRatingListener onCommentRatingListener) {
        this.onCommentRatingListener = onCommentRatingListener;
    }

}
