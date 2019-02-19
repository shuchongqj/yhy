package com.yhy.network.utils;

import android.content.Context;

import com.yhy.common.base.YHYBaseApplication;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class NetManagerProxy {
    private static Method doDeviceRegist;
    static Class targetClass;

    static {
        try {
            targetClass = Class.forName("com.quanyan.yhy.net.NetManager");
            doDeviceRegist = targetClass.getDeclaredMethod("doDeviceRegist", String.class, String.class);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public synchronized static Object doDeviceRegist(String cert, String dtk) {

        try {
            return doDeviceRegist.invoke(targetClass.getDeclaredMethod("getInstance", Context.class).invoke(null, YHYBaseApplication.getInstance()), cert, dtk);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }
}
