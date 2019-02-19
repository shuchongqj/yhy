package com.ymanalyseslibrary.actinfo;

import android.content.Context;
import android.content.SharedPreferences;

import com.ymanalyseslibrary.AnalysesConstants;
import com.ymanalyseslibrary.AnalyticsConfig;
import com.ymanalyseslibrary.log.LogUtil;
import com.ymanalyseslibrary.tool.AnalyticUtils;

import java.util.List;


/**
 * Created with Android Studio.
 * Title:AboutAndFeedController
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:zhaoxiaopo
 * Date:2/19/16
 * Time:13:38
 * Version 1.0
 */
public class ActSesstionUtil {
    private static final String SESSION_START_TIME = "session_start_time";
    private static final String SESSION_END_TIME = "session_end_time";
    private static final String SESSION_ID = "session_id";
    private final String A_START_TIME = "a_start_time";
    private final String A_END_TIME = "a_end_time";
    private static final String TAG = "activities";
    private static String sessionID = null;

    private void clear(SharedPreferences sharedPreferences) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.remove(SESSION_START_TIME);
        edit.remove(SESSION_END_TIME);
        edit.remove(A_START_TIME);
        edit.remove(A_END_TIME);
        edit.putString(TAG, "");
        edit.commit();
    }

    public String getNewSessionID(Context context) {
        String deviceLabel = AnalyticUtils.getDeviceLabel(context);
        long currentTimeMillis = System.currentTimeMillis();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(currentTimeMillis).append(deviceLabel);
        sessionID = stringBuilder.toString();
        return sessionID;
    }

    public void onResume(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(AnalysesConstants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        if(sharedPreferences != null) {
            String sessionID = null;
            if(judgeOver(sharedPreferences)) {
                LogUtil.d("session", "start new session");
                sessionID = generateNewSession(context, sharedPreferences);
            } else {
                sessionID = sharedPreferences.getString(SESSION_ID, (String)null);
                SharedPreferences.Editor var4 = sharedPreferences.edit();
                var4.putLong(A_START_TIME, System.currentTimeMillis());
                var4.putLong(A_END_TIME, 0L);
                var4.commit();
            }
        }
    }

    public void onPause(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(AnalysesConstants.PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        if(sharedPreferences != null) {
            long start_time = sharedPreferences.getLong("a_start_time", 0L);
            if(start_time == 0L && AnalyticsConfig.ACTIVITY_DURATION_OPEN) {
                LogUtil.e(TAG, "onPause called before onResume");
            } else {
                long currentTimeMillis = System.currentTimeMillis();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putLong(A_START_TIME, 0L);
                editor.putLong(A_END_TIME, currentTimeMillis);
                editor.putLong(SESSION_END_TIME, currentTimeMillis);
                editor.commit();
            }
        }
    }

    private String generateNewSession(Context context, SharedPreferences sharedPreferences) {
        // TODO: 2/19/16  保存之前的数据，，或者直接上传
        String activitiesContext = wrapPreviousSession(context);

        // TODO: 2/19/16  生成新的session
        String newSessionID = getNewSessionID(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SESSION_ID, newSessionID);
        editor.putLong(SESSION_START_TIME, System.currentTimeMillis());
        editor.putLong(SESSION_END_TIME, 0L);
        editor.putLong(A_START_TIME, System.currentTimeMillis());
        editor.putLong(A_END_TIME, 0L);
        editor.commit();

        return newSessionID;
    }

    public String wrapPreviousSession(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(AnalysesConstants.PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        String var3 = sharedPreferences.getString(SESSION_ID, (String)null);
        if(var3 == null) {
            return null;
        } else {
            long session_start_time = sharedPreferences.getLong("session_start_time", 0L);
            long session_end_time = sharedPreferences.getLong("session_end_time", 0L);
            long duration = 0L;
            if(session_end_time != 0L) {
                duration = session_end_time - session_start_time;
                if(Math.abs(duration) > 86400000L) {
                    duration = 0L;
                }
            }
            // TODO: 2/19/16 按照规则拼装字符串

            List var13 = ActivityStatistic.getSessionActivities(sharedPreferences);
            if(var13 != null && var13.size() > 0) {
//                var10.a(var13);
            }

            clear(sharedPreferences);
        }
        return null;
    }

    private boolean judgeOver(SharedPreferences sharedPreferences) {
        long start_time = sharedPreferences.getLong(A_START_TIME, 0L);
        long end_time = sharedPreferences.getLong(A_END_TIME, 0L);
        long currentTimeMillis = System.currentTimeMillis();
        if(start_time != 0L && currentTimeMillis - start_time < AnalyticsConfig.kContinueSessionMillis) {
            LogUtil.e(TAG, "onResume called before onPause");
            return false;
        } else {
            return currentTimeMillis - end_time > AnalyticsConfig.kContinueSessionMillis;
        }
    }

}
