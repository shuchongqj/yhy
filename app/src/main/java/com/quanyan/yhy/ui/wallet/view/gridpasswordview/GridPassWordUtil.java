package com.quanyan.yhy.ui.wallet.view.gridpasswordview;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created with Android Studio.
 * Title:GridPassWordUtil
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-8-25
 * Time:16:52
 * Version 1.0
 * Description:
 */
public class GridPassWordUtil {

    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static int dp2px(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int) ((dp * displayMetrics.density) + 0.5);
    }

}
