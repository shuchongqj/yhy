package com.yhy.push;

import android.content.Context;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

//TODO 使用路由替换
public class NavUtilsProxy {

    static Method gotoFirstLoginDialogActivity;

    static {
        try {
            Class targetClass = Class.forName("com.quanyan.yhy.ui.base.utils.NavUtils");
            gotoFirstLoginDialogActivity = targetClass.getDeclaredMethod("gotoFirstLoginDialogActivity", Context.class, String.class);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public static void gotoFirstLoginDialogActivity(Context context, String content) {

        try {
            gotoFirstLoginDialogActivity.invoke(null, context, content);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
