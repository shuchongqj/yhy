package com.quanyan.yhy.common;

import android.view.View;
import android.widget.ImageView;

import com.quanyan.yhy.R;

/**
 * Created with Android Studio.
 * Title:Gendar
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:和平必胜、正义必胜、人民必胜
 * Author:炎黄子孙
 * Date:15/11/7
 * Time:下午2:17
 * Version 1.0
 */
public class Gendar {
    //未选择
    public static final int NONE = 1;
    //女
    public static final int MALE = 2;
    //男
    public static final int FEMALE = 3;
    //其他
    public static final int OTHER = 3;

    //未选择
    public static final String STR_NONE = "1";
    //女
    public static final String STR_MALE = "2";
    //男
    public static final String STR_FEMALE = "3";
    //其他
    public static final String STR_OTHER = "4";

    /**
     * 设置性别的图标
     * @param view
     * @param gendar
     */
    public static void setGendarIcon(ImageView view,int gendar){
        if(gendar == MALE){
            view.setImageResource(R.mipmap.ic_male);
        }else if(gendar == FEMALE){
            view.setImageResource(R.mipmap.ic_female);
        }
    }

    /**
     * 设置性别的图标,	// 性别 1-未确认 2-男 3-女
     * @param view
     * @param gendar
     *
     */
    public static void setGendarIcon(ImageView view,String gendar){
        if(String.valueOf(MALE).equals(gendar)){
            view.setVisibility(View.VISIBLE);
            view.setImageResource(R.mipmap.ic_male);
        }else if(String.valueOf(FEMALE).equals(gendar)){
            view.setVisibility(View.VISIBLE);
            view.setImageResource(R.mipmap.ic_female);
        }else{
            view.setVisibility(View.INVISIBLE);
        }
    }

}
