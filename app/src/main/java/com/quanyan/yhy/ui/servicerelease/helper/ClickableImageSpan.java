package com.quanyan.yhy.ui.servicerelease.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.style.ImageSpan;
import android.view.View;

/**
 * Created with Android Studio.
 * Title:ClickableImageSpan
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-7-12
 * Time:17:11
 * Version 1.1.0
 */

public abstract class ClickableImageSpan extends ImageSpan {

    public ClickableImageSpan(Context context, Bitmap b) {
        super(context, b);
    }

    public abstract void onClick(View view);
}
