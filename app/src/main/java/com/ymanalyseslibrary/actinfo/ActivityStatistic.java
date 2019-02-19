package com.ymanalyseslibrary.actinfo;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.ymanalyseslibrary.AnalysesConstants;
import com.ymanalyseslibrary.data.ActAnalyData;
import com.ymanalyseslibrary.data.ActDuration;
import com.ymanalyseslibrary.log.LogUtil;
import com.ymanalyseslibrary.tool.DataEncapUtils;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Created with Android Studio.
 * Title:AboutAndFeedController
 * Description:
 * Copyright:Copyright (mActDurations) 2015
 * Company:quanyan
 * Author:zhaoxiaopo
 * Date:2/19/16
 * Time:13:22
 * Version 1.0
 */
public class ActivityStatistic {
    private static final String ACTIVITIES_DURATION = "activities";
    private Map<String, String> mActiitysDur = new HashMap<>();
    private ArrayList<ActDuration> mActDurations = new ArrayList<>();

    /**
     * 封装类名activity界面，开启时间
     * @param pageName 类名
     */
    public void onResume(String pageName) {
        if (!TextUtils.isEmpty(pageName)) {
            Map actiitysDur = this.mActiitysDur;
            synchronized (this.mActiitysDur) {
                this.mActiitysDur.put(pageName, DataEncapUtils.currentTimeString());
            }
        }
    }

    public void onPause(String pageName) {
        if (!TextUtils.isEmpty(pageName)) {
            Map var3 = this.mActiitysDur;
            String startTime;
            synchronized (this.mActiitysDur) {
                startTime = this.mActiitysDur.remove(pageName);
            }

            if (startTime == null) {
                LogUtil.e("please call \'onResume(%s)\' before onPause", pageName);
                return;
            }

            long duration = System.currentTimeMillis() - Long.parseLong(startTime);
            String durationStr = String.valueOf(duration);
            ArrayList var5 = this.mActDurations;
            synchronized (this.mActDurations) {
                this.mActDurations.add(new ActDuration(pageName, durationStr));
            }
        }
    }

    public void storeTheActititiesSession(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(AnalysesConstants.PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (this.mActDurations.size() > 0) {
            String activities = sharedPreferences.getString(ACTIVITIES_DURATION, "");
            StringBuilder stringBuilder = new StringBuilder();
            if (!TextUtils.isEmpty(activities)) {
                stringBuilder.append(activities);
                stringBuilder.append(";");
            }

            ArrayList arrayList = this.mActDurations;
            synchronized (this.mActDurations) {
                Iterator iterator = this.mActDurations.iterator();

                while (true) {
                    if (!iterator.hasNext()) {
                        this.mActDurations.clear();
                        break;
                    }

                    ActDuration actDuration = (ActDuration) iterator.next();
                    stringBuilder.append(String.format("[\"%s\",%d]", new Object[]{actDuration.mActName,
                            Long.valueOf(actDuration.mDuration)}));
                    stringBuilder.append(";");
                }
            }

            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            editor.remove(ACTIVITIES_DURATION);
            editor.putString(ACTIVITIES_DURATION, stringBuilder.toString());
        }

        editor.commit();
    }

    public static List<ActAnalyData> getSessionActivities(SharedPreferences sharedPreferences) {
        String activities = sharedPreferences.getString("activities", "");
        if ("".equals(activities)) {
            return null;
        } else {
            ArrayList arrayList = new ArrayList();

            try {
                String[] split = activities.split(";");

                for (int i = 0; i < split.length; ++i) {
                    String elem = split[i];
                    JSONArray jsonArray = new JSONArray(elem);
                    if (!TextUtils.isEmpty(elem)) {
                        arrayList.add(new ActAnalyData(jsonArray.getString(0), jsonArray.getLong(1)));
                    }
                }
            } catch (Exception var6) {
                var6.printStackTrace();
            }

            return arrayList.size() > 0 ? arrayList : null;
        }
    }
}
