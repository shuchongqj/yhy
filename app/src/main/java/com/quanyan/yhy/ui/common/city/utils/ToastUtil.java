package com.quanyan.yhy.ui.common.city.utils;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.newyhy.utils.DisplayUtils;
import com.quanyan.yhy.R;

/**
 *  ToastUtil
 * Created by Jiervs on 2015/9/25.
 */
public class ToastUtil {
    private static Toast toast;
    //单例吐司
    public static void showToast(Context context,String msg) {
        if (toast == null && context != null) {
            toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
            toast.getView().setBackgroundResource(R.drawable.shape_toast_black);
            TextView v = toast.getView().findViewById(android.R.id.message);
            v.setTextColor(context.getResources().getColor(R.color.white));
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            params.setMargins(DisplayUtils.dp2px(context, 5),5,DisplayUtils.dp2px(context, 5),5);
            v.setLayoutParams(params);
        }
        if (toast != null) {
            toast.setText(msg);
            toast.show();
        }
    }

    public static void showToast(Context context, int resId) {
        showToast(context, context.getString(resId));
    }
}
