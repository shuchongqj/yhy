package com.videolibrary.puhser.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;

import com.quanyan.yhy.R;


/**
 * Created with Android Studio.
 * Title:PopView
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:赵晓坡
 * Date:16/8/5
 * Time:17:43
 * Version 1.1.0
 */
public class PopView extends PopupWindow {

    private View mContentView;

    public PopView(Context context, View contentView, int animStyle, int width, int height){
        mContentView = contentView;
        setContentView(contentView);
        setOutsideTouchable(true);
        setAnimationStyle(animStyle);
        setWidth(width);
        setHeight(height);
        setFocusable(true);
        ColorDrawable dw = new ColorDrawable(context.getResources().getColor(
                R.color.transparent_black_40));
        setBackgroundDrawable(dw);
    }

    public void showPopView(View anchor){
        if(!isShowing()){
            showAtLocation(anchor, Gravity.RIGHT | Gravity.CENTER_VERTICAL,
                    0, 0); // 设置layout在PopupWindow中显示的位置
        }
    }

    public void showPopViewBottom(View anchor){
        if(!isShowing()){
            showAtLocation(anchor, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL,
                    0, 0); // 设置layout在PopupWindow中显示的位置
        }
    }



}
