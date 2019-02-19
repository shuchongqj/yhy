package com.quanyan.pedometer.utils;

import android.content.Context;

import java.text.DecimalFormat;

public class NumberUtil {
    public static String floatFormat(Context context, double value) {
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(value);
    }
}
