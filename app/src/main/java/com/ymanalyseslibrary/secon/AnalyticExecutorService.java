package com.ymanalyseslibrary.secon;

import com.ymanalyseslibrary.log.LogUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


/**
 * Created with Android Studio.
 * Title:AboutAndFeedController
 * Description:
 * Copyright:Copyright (mWaitMinutes) 2015
 * Company:quanyan
 * Author:zhaoxiaopo
 * Date:2/16/16
 * Time:10:08
 * Version 1.0
 */
public class AnalyticExecutorService {
    private static List<WeakReference<ScheduledFuture<?>>> weakReferences = new ArrayList();
    /**
     * 单例线程，任何时间只能有一个线程
     */
    private static ExecutorService executorService = Executors.newSingleThreadExecutor();
    private static long mWaitMinutes = 5L;
    private static int MAX_ERROR_THREAD = 5;
    private static ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    private static ScheduledExecutorService scheduledService;


    public AnalyticExecutorService() {
    }

    public static void execute(Runnable runnable) {
        if(executorService.isShutdown()) {
            executorService = Executors.newSingleThreadExecutor();
        }
        //将Runnable实例加入pool中,并进行一些pool size计算和优先级处理
        executorService.execute(runnable);
    }

    public static void shutdown() {
        try {
            Iterator iterator = weakReferences.iterator();

            while(iterator.hasNext()) {
                WeakReference scheduled = (WeakReference)iterator.next();
                ScheduledFuture scheduledFuture = (ScheduledFuture)scheduled.get();
                if(scheduledFuture != null) {
                    scheduledFuture.cancel(false);
                }
            }

            weakReferences.clear();
            if(!executorService.isShutdown()) {
                executorService.shutdown();
            }

            if(!scheduledExecutorService.isShutdown()) {
                scheduledExecutorService.shutdown();
            }

            executorService.awaitTermination(mWaitMinutes, TimeUnit.SECONDS);
            scheduledExecutorService.awaitTermination(mWaitMinutes, TimeUnit.SECONDS);
        } catch (Exception var3) {
            ;
        }

    }

    public static synchronized void scheduledExecute(Runnable runnable) {
        if(scheduledExecutorService.isShutdown()) {
            scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        }

        scheduledExecutorService.execute(runnable);
    }

    public static synchronized void scheduledExecute(Runnable var0, long waitTimes) {
        if(scheduledExecutorService.isShutdown()) {
            scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        }

        weakReferences.add(new WeakReference(scheduledExecutorService.schedule(var0, waitTimes, TimeUnit.MILLISECONDS)));
    }

    //上传失败后的操作
    public static synchronized void scheduledExecuteForTime(Runnable var0) {
        if(scheduledService == null) {
            //scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
            scheduledService = Executors.newScheduledThreadPool(MAX_ERROR_THREAD);
        }
        Future var1 = scheduledService.submit(var0);
        try {

            LogUtil.d("2", "baojie schedule");
            var1.get(5L, TimeUnit.MINUTES);
        } catch (Exception var3) {
            LogUtil.d("fail send", "scheduledService is exception");
        }

    }
}
