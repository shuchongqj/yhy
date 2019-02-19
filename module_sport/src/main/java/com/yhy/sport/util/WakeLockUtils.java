package com.yhy.sport.util;

import android.content.Context;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.Calendar;

/**
 * @Description: PowerManager 用来控制设备的电源状态.
 * 而PowerManager.WakeLock 也称作唤醒锁, 是一种保持 CPU 运转防止设备休眠的方式.
 * @Created by zhaolei.yang 2018-07-10 19:29
 */
public class WakeLockUtils {

    private static PowerManager.WakeLock mWakeLock;

    /**
     * 当息屏以后保持指定wakeName后台cpu运转
     *
     * @param context
     * @param wakeName
     * @return
     */
    @Nullable
    public synchronized static PowerManager.WakeLock getLock(Context context, String wakeName) {
        if (mWakeLock != null) {
            if (mWakeLock.isHeld())
                mWakeLock.release();
            mWakeLock = null;
        }

        if (TextUtils.isEmpty(wakeName))
            return null;

        if (mWakeLock == null) {
            PowerManager mgr = (PowerManager) context
                    .getSystemService(Context.POWER_SERVICE);
            mWakeLock = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    wakeName);
            mWakeLock.setReferenceCounted(true);
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(System.currentTimeMillis());
            mWakeLock.acquire();
        }
        return (mWakeLock);
    }
}
