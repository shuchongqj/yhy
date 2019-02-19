package com.newyhy.views.dialog;

import android.content.Context;

import com.yhy.common.utils.AndroidUtils;

/**
 *
 * Created by Jiervs on 2018/7/7.
 */

public class DialogLocationUtils {

    public static final int UP = 0;
    public static final int DOWN = 1;

    /**
     * 根据锚点显示适应位置 Dialog
     * @param context
     * @param y
     * @return
     */
    public static  int showLocation(Context context ,int y) {
        int screenHeight = AndroidUtils.getScreenHeight(context);
        if (y > screenHeight/2) {
            return UP;
        } else {
            return DOWN;
        }
    }
}
