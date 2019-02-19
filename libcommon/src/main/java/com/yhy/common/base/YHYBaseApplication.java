package com.yhy.common.base;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import java.util.ArrayList;
import java.util.List;

public abstract class YHYBaseApplication extends Application{

    private static YHYBaseApplication application;

    private static List<ModuleApplication> modules = new ArrayList<>();

    protected static ArrayList<Activity> activityList = new ArrayList<Activity>();

    public static ArrayList<Activity> getActivityList() {
        return activityList;
    }

    public static Activity getTopActivity(){
        return activityList == null ? null : activityList.get(activityList.size() - 1);
    }

    public void finishActivity(Activity item) {
        if (item == null)
            return;
        if (activityList.contains(item)) {
            if (item.isFinishing()) {
                activityList.remove(item);
            } else {
                item.finish();
                activityList.remove(item);
            }
        } else {
            item.finish();
        }
    }

    public void destroyActivity(Activity activity) {
        if (activity != null) {
            activityList.remove(activity);
        }
    }

    public void addActivity(Activity activity) {
        if (activity != null) {
            activityList.add(activity);
        }
    }

    public void removeActivity(Activity item) {
        if (item == null)
            return;
        activityList.remove(item);
        if (!item.isFinishing()){
            item.finish();
        }

    }

    public void removeActivityClass(Class<? extends Activity> activityClass) {
        if (activityClass == null)
            return;
        for (Activity activity : activityList) {
            if (activity.getClass().equals(activityClass)) {
                removeActivity(activity);
                return;
            }
        }
    }

    public boolean isActivityOpened(Class<? extends Activity> activityClass) {
        for (Activity activity : activityList) {
            if (activity.getClass().equals(activityClass)) {
                return true;
            }
        }
        return false;
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        application = this;

        if (isMainProcess(this)){
            for (ModuleApplication moduleApplication : modules){
                moduleApplication.onCreate(this);
            }
        }

    }


    public static YHYBaseApplication getInstance(){
        return application;
    }

    public static void registModule(ModuleApplication moduleApplication){
        if (!modules.contains(moduleApplication)){
            modules.add(moduleApplication);
        }
    }

    /**
     * 退出全部activity
     */
    public void exitAllActivity() {
        for (int i = (activityList.size() - 1); i >= 0; i--) {
            activityList.get(i).finish();
            break;
        }
        activityList.clear();
    }

    /**
     * 获取APP的环境类型
     * @return
     */
    public abstract YhyEnvironment getYhyEnvironment();

    public abstract void exitNeedLoginActivity();

    /**
     * 包名判断是否为主进程
     *
     * @param context
     * @return
     */
    public static boolean isMainProcess(Context context) {
        return context.getPackageName().equals(getProcessName(context));
    }

    /**
     * 获取进程名称
     *
     * @param context
     * @return
     */
    public static String getProcessName(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo proInfo : runningApps) {
            if (proInfo.pid == android.os.Process.myPid()) {
                if (proInfo.processName != null) {
                    return proInfo.processName;
                }
            }
        }
        return null;
    }
}
