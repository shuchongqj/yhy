package com.yhy.push;

import android.content.Context;
import android.content.Intent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
//TODO 使用路由替换
public class NativeUtilsProxy {
    static Method getIntent;

    static {
        try {
            Class targetClass = Class.forName("com.quanyan.yhy.ui.base.utils.NativeUtils");
            getIntent = targetClass.getDeclaredMethod("getIntent", String.class, String.class, Context.class, boolean.class);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public static Intent getIntent(String ntfOperationCode, String ntfOperationVaule, Context context, boolean isLogin) {

        try {
            return (Intent) getIntent.invoke(null, ntfOperationCode, ntfOperationVaule, context, isLogin);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
