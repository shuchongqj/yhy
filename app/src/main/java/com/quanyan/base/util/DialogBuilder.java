package com.quanyan.base.util;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created with Android Studio.
 * Title:DialogUtil
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:徐学东
 * Date:16/5/20
 * Time:11:17
 * Version 1.1.0
 */
public class DialogBuilder {

    private Context mContext;
    private int mWidth, mHeight;
    private int mStyle;
    private int mContentViewId;
    private View mContentView;
    private boolean cancelable;
    private boolean canceledOnTouchOutside;
    private int mAnimation;
    private int mGravity;

    public DialogBuilder(Context context){
        this.mContext = context;
    }

    public DialogBuilder setWidth(int width){
        this.mWidth = width;
        return this;
    }

    public DialogBuilder setStyle(int style) {
        mStyle = style;
        return this;
    }

    public DialogBuilder setContentViewId(int contentViewId) {
        mContentViewId = contentViewId;
        return this;
    }

    public DialogBuilder setContentView(View view){
        this.mContentView = view;
        return this;
    }
    public DialogBuilder setHeight(int height){
        this.mHeight = height;
        return this;
    }

    public DialogBuilder setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
        return this;
    }

    public DialogBuilder setCanceledOnTouchOutside(boolean canceledOnTouchOutside) {
        this.canceledOnTouchOutside = canceledOnTouchOutside;
        return this;
    }

    public DialogBuilder setAnimation(int animation) {
        mAnimation = animation;
        return this;
    }

    public DialogBuilder setGravity(int gravity) {
        mGravity = gravity;
        return this;
    }

    public Dialog build(){
        if(mContext == null){
            return null;
        }

        Dialog dialog;
        if(mStyle > 0){
            dialog = new Dialog(mContext, mStyle);
        }else{
            dialog = new Dialog(mContext);
        }

        if(mContentViewId > 0){
            dialog.setContentView(mContentViewId);
        }

        if(mContentView != null){
            dialog.setContentView(mContentView);
        }

        dialog.setCancelable(cancelable);
        dialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
        Window window = dialog.getWindow();
        if(mGravity > 0){
            window.setGravity(mGravity);
        }
        if(mWidth > 0 || mHeight > 0){
            WindowManager.LayoutParams lp = window.getAttributes();
            if(mWidth > 0){
                lp.width = mWidth;
            }
            if(mHeight > 0){
                lp.height = mHeight;
            }
//            window.setAttributes(lp);
            dialog.onWindowAttributesChanged(lp);
        }


        if(mAnimation > 0) {
            window.setWindowAnimations(mAnimation); // 添加动画
        }
        return dialog;
    }
}
