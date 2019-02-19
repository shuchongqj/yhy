package com.quanyan.yhy.ui.base.views.im;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created with Android Studio.
 * Title:ImageView_
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:和平必胜、正义必胜、人民必胜
 * Author:炎黄子孙
 * Date:15/10/20
 * Time:上午11:28
 * Version 1.0
 */
public class ImageView_ extends ImageView {
    public ImageView_(Context context) {
        super(context);
    }

    public ImageView_(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageView_(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected void onDraw(Canvas canvas) {
        Drawable drawable = this.getDrawable();
        if(drawable != null && drawable instanceof BitmapDrawable) {
            Bitmap bmp = ((BitmapDrawable)drawable).getBitmap();
            if(bmp != null && bmp.isRecycled()) {
                this.setImageBitmap((Bitmap)null);
            }
        }

        super.onDraw(canvas);
    }
}
