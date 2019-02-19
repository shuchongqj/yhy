package com.newyhy.utils;

import android.text.TextUtils;

/**
 * Created by yangboxue on 2018/5/14.
 */

public class StringUtils {


    /**
     * 手机号中间用*代替
     *
     * @param phonenum
     */
    public static String transformPhone(String phonenum) {
        if (!TextUtils.isEmpty(phonenum) && phonenum.length() > 6) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < phonenum.length(); i++) {
                char c = phonenum.charAt(i);
                if (i >= 3 && i <= 6) {
                    sb.append('*');
                } else {
                    sb.append(c);
                }
            }
            return sb.toString();
        }
        return phonenum;
    }

}