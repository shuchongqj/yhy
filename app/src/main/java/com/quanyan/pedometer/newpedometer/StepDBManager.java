package com.quanyan.pedometer.newpedometer;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Environment;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.util.LogUtils;
import com.quanyan.pedometer.core.Constants;
import com.quanyan.pedometer.utils.TimeUtil;
import com.yhy.common.beans.net.model.pedometer.PedometerHistoryResult;
import com.yhy.common.beans.net.model.pedometer.SyncParam;
import com.yhy.common.beans.net.model.pedometer.WalkDataInfo;
import com.yhy.common.beans.net.model.pedometer.WalkDataInfoPerHour;
import com.yhy.common.utils.SPUtils;
import com.yhy.router.YhyRouter;
import com.yhy.service.IUserService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created with Android Studio.
 * Title:StepDBManager
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:赵晓坡
 * Date:16/7/2
 * Time:11:12
 * Version 1.1.0
 */
public class StepDBManager {
    public static DbUtils mDbUtils;

    @Autowired
    IUserService userService;

    private static StepDBManager instance = new StepDBManager();

    public StepDBManager() {
        YhyRouter.getInstance().inject(this);
    }

    public static DbUtils getDefaultDbUtils(Context context) {
        if (mDbUtils == null) {
            String stepDbName = "quanyan_steps.db";
            mDbUtils = DbUtils.create(context, stepDbName, 1, new DbUtils.DbUpgradeListener() {
                @Override
                public void onUpgrade(DbUtils dbUtils, int oldVersion, int newVersion) {
                }
            });
        }
        return mDbUtils;
    }

    public interface SavedSuccessTreshold {
        void success(List<WalkDataThresholdGag> walkDataThresholdGags);
    }

    public interface LoadSuccessDailyData {
        void success(List<WalkDataDaily> walkDataDailies);
    }

    /**
     * 保存服务器中的30天数据
     */
    public static class SaveAndLoadWalkDataInfoListTask extends
            AsyncTask<Void, Void, List<WalkDataDaily>> {
        private List<WalkDataDaily> mlist;
        private LoadSuccessDailyData mLoadSuccessDailyData;

        public SaveAndLoadWalkDataInfoListTask(List<WalkDataDaily> alist, LoadSuccessDailyData loadSuccessDailyData) {
            mlist = alist;
            mLoadSuccessDailyData = loadSuccessDailyData;
        }

        @Override
        protected List<WalkDataDaily> doInBackground(Void... params) {
            if (null == mDbUtils) {
                return new ArrayList<>();
            }
            try {
//                cleanWalkDataInfoList(0, 0);
                mDbUtils.saveOrUpdateAll(mlist);
            } catch (DbException e) {
                LogUtils.e("保存数据报错");
            }
            List<WalkDataDaily> result = new ArrayList<>();
            result.addAll(loadWalkDataInfoList());
            return result;
        }

        @Override
        protected void onPostExecute(List<WalkDataDaily> result) {
            LogUtils.d("saveAndLoadWalkDataInfoListTask onPostExecute");
            if(mLoadSuccessDailyData != null) {
                mLoadSuccessDailyData.success(result);
            }
        }
    }

    /**
     * @param lastSynTime
     * @return
     * @Description 查询所有历史每日数据
     */

    public static List<WalkDataDaily> loadWalkDataInfoList(long lastStepDayTime,
                                                           long lastSynTime) {
        if (null == mDbUtils) {
            return null;
        }
        try {
            if (mDbUtils.tableIsExist(WalkDataDaily.class)) {
                List<WalkDataDaily> list = mDbUtils.findAll(Selector.from(WalkDataDaily.class)
                        .where("synTime", "<", lastStepDayTime)
                        .and("synTime", ">", lastSynTime));
                return list;
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @author xiezhidong@pajk.cn
     * @Description 查询历史每日数据--------前台使用
     * @date 2014-12-5 下午3:43:42
     */

    public static class LoadWalkDataInfoListTask extends
            AsyncTask<Void, Void, List<WalkDataDaily>> {
        long mlastStepDayTime = 0;
        long mlastSynTime = 0;
        LoadSuccessDailyData mlsn;

        public LoadWalkDataInfoListTask(long lastStepDayTime, long lastSynTime,
                                        LoadSuccessDailyData lsn) {
            mlastStepDayTime = lastStepDayTime > 0 ? lastStepDayTime : 0;
            mlastSynTime = lastSynTime > 0 ? lastSynTime : 0;
            mlsn = lsn;
        }

        public LoadWalkDataInfoListTask(LoadSuccessDailyData lsn) {
            mlsn = lsn;
        }

        @Override
        protected List<WalkDataDaily> doInBackground(Void... params) {
            List<WalkDataDaily> result = new ArrayList<>();
            if (mlastStepDayTime != 0 || mlastSynTime != 0) {
                result.addAll(loadWalkDataInfoList(mlastStepDayTime, mlastSynTime));
            } else {
                result.addAll(loadWalkDataInfoList());
            }
            return result;
        }

        @Override
        protected void onPostExecute(List<WalkDataDaily> result) {
            LogUtils.e("LoadWalkDataInfoListTask onPostExecute");
            if (mlsn != null) {
                mlsn.success(result);
            }
        }
    }

    public static List<PedometerHistoryResult> DataChange(List<WalkDataDaily> walkDataDailies) {
        List<PedometerHistoryResult> pedometerHistoryResultLists = new ArrayList<>();
        for (int i = 0; i < walkDataDailies.size(); i++) {
            PedometerHistoryResult pedometerHistoryResult = new PedometerHistoryResult();
            pedometerHistoryResult.stepCounts = walkDataDailies.get(i).stepCount;
            pedometerHistoryResult.distance = (float) walkDataDailies.get(i).distance;
            pedometerHistoryResult.calorie = (float) walkDataDailies.get(i).calories;
            pedometerHistoryResult.targetSteps = walkDataDailies.get(i).targetStepCount;
            pedometerHistoryResult.stepDate = walkDataDailies.get(i).synTime;
            pedometerHistoryResult.fats = (float) (walkDataDailies.get(i).calories / 9);
            pedometerHistoryResultLists.add(pedometerHistoryResult);
        }
        return pedometerHistoryResultLists;
    }

    public static List<WalkDataDaily> DataChange2(List<PedometerHistoryResult> walkDataDailies) {
        List<WalkDataDaily> pedometerHistoryResultLists = new ArrayList<>();
        for (int i = 0; i < walkDataDailies.size(); i++) {
            WalkDataDaily pedometerHistoryResult = new WalkDataDaily();
            pedometerHistoryResult.stepCount = (int) walkDataDailies.get(i).stepCounts;
            pedometerHistoryResult.distance = walkDataDailies.get(i).distance;
            pedometerHistoryResult.calories = walkDataDailies.get(i).calorie;
            pedometerHistoryResult.targetStepCount = walkDataDailies.get(i).targetSteps;
            pedometerHistoryResult.synTime = walkDataDailies.get(i).stepDate;
            pedometerHistoryResultLists.add(pedometerHistoryResult);
        }
        return pedometerHistoryResultLists;
    }

    /**
     * @return List<WalkDataDaily>
     * @Description 查询30天历史记录
     */

    public static List<WalkDataDaily> loadWalkDataInfoList() {
        if (null == mDbUtils) {
            return new ArrayList<>();
        }
        try {
            List<WalkDataDaily> list = new ArrayList<>();
            if (mDbUtils.tableIsExist(WalkDataDaily.class)) {
                list = mDbUtils.findAll(Selector.from(WalkDataDaily.class)
                        .where("synTime", ">=", TimeUtil.getThirtyDayTime())
                        .orderBy("synTime"));
                LogUtils.i(list.toString());
                // 最后的数据补全，数据为0时补0size()
            }
            return list;

        } catch (DbException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * @param timefromStr  --- 目前未使用，考虑是否需要按照某些时间条件进行删除
     * @param timefromStr2
     * @Description 按照日期清空已有的DB数据 -- WalkDataInfo
     * @author xiezhidong@pajk.cn
     */

    private static void cleanWalkDataInfoList(long timefromStr, long timefromStr2) {
        if (null == mDbUtils) {
            return;
        }
        try {
            if (mDbUtils.tableIsExist(WalkDataDaily.class)) {
                // mDbUtils.deleteAll(mDbUtils.findAll(Selector.from(WalkDataInfo.class)));
                mDbUtils.dropTable(WalkDataDaily.class);
            }
            return;
        } catch (DbException e) {
            e.printStackTrace();
        }
    }


    public static class SaveWalkDataDaily extends AsyncTask<Void, Void, List<WalkDataDaily>> {
        private Context mContext;
        private long mTotalSteps = 0;
        private double mTotalDistance = 0;
        private double mTotalCalories = 0;
        private long mSynTime = 0;
        private long mLastServerSynTime = 0;
        private LoadSuccessDailyData mSavedSuccessTreshold;
        public SaveWalkDataDaily(Context context, long tempSteps, double tempDistance, double tempCalories, long dayTime, long lastServerSynTime,
                                 LoadSuccessDailyData savedSuccessTreshold) {
            mTotalSteps = tempSteps;
            mTotalDistance = tempDistance;
            mTotalCalories = tempCalories;
            mSynTime = dayTime;
            mLastServerSynTime = lastServerSynTime;
            mContext = context;

            mSavedSuccessTreshold = savedSuccessTreshold;
        }

        @Override
        protected List<WalkDataDaily> doInBackground(Void... params) {
            if (null == mDbUtils) {
                return null;
            }

            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            try {
                WalkDataDaily info = new WalkDataDaily();
                info.stepCount = mTotalSteps;
                info.distance = Double.parseDouble(decimalFormat.format(mTotalDistance));
                info.calories = Double.parseDouble(decimalFormat.format(mTotalCalories));
                info.synTime = mSynTime;
                info.targetStepCount = PedometerUtil.getTargetStep(mContext);
                mDbUtils.saveOrUpdate(info);

                List<WalkDataDaily> walkDataDailies = mDbUtils.findAll(Selector.from(WalkDataDaily.class)
                        .where("synTime", ">", mLastServerSynTime).orderBy("synTime", true).limit(30));
                LogUtils.d("数据库中的数据  ： " + walkDataDailies);
                if(walkDataDailies.size() > 0) {
                    long time = walkDataDailies.get(0).synTime;
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
                    long now = (calendar.getTimeInMillis() / 1000) * 1000;
                    int size = (int) ((now - time) / Constants.MIL_SECONDS_DAY);
                    if (size > 1) {
                        calendar.setTimeInMillis(time);
                        for (int i = 0; i < (size - 1); i++) {
                            calendar.add(Calendar.DAY_OF_MONTH, 1);
                            WalkDataDaily walkDataDaily = new WalkDataDaily();
                            walkDataDaily.synTime = (calendar.getTimeInMillis() / 1000) * 1000;
                            walkDataDailies.add(0, walkDataDaily);
                        }
                        if (walkDataDailies.size() > 30) {
                            return walkDataDailies.subList(0, 30);
                        }
                    }
                }
                return walkDataDailies;
            } catch (DbException e) {
                LogUtils.e("保存数据报错");
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<WalkDataDaily> walkDataDailies) {
            super.onPostExecute(walkDataDailies);
            if(mSavedSuccessTreshold != null && walkDataDailies != null){
                LogUtils.d("" + walkDataDailies);
                mSavedSuccessTreshold.success(walkDataDailies);
            }
        }
    }

    public static class UploadWalkDataDaily extends AsyncTask<Void, Void, List<WalkDataDaily>> {

        private long mLastServerSynTime = 0;
        private LoadSuccessDailyData mSavedSuccessTreshold;

        public UploadWalkDataDaily(long lastServerSynTime, LoadSuccessDailyData savedSuccessTreshold) {
            mLastServerSynTime = lastServerSynTime;
            mSavedSuccessTreshold = savedSuccessTreshold;
        }

        @Override
        protected List<WalkDataDaily> doInBackground(Void... params) {
            if (null == mDbUtils) {
                return null;
            }

            try {
                List<WalkDataDaily> walkDataDailies = mDbUtils.findAll(Selector.from(WalkDataDaily.class)
                        .where("synTime", ">", mLastServerSynTime).orderBy("synTime", true).limit(30));
                LogUtils.d("数据库中的数据  ： " + walkDataDailies);
                if(walkDataDailies != null && walkDataDailies.size() > 0) {
                    long time = walkDataDailies.get(0).synTime;
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
                    long now = (calendar.getTimeInMillis() / 1000) * 1000;
                    int size = (int) ((now - time) / Constants.MIL_SECONDS_DAY);
                    if (size > 1) {
                        calendar.setTimeInMillis(time);
                        for (int i = 0; i < (size - 1); i++) {
                            calendar.add(Calendar.DAY_OF_MONTH, 1);
                            WalkDataDaily walkDataDaily = new WalkDataDaily();
                            walkDataDaily.synTime = (calendar.getTimeInMillis() / 1000) * 1000;
                            walkDataDailies.add(0, walkDataDaily);
                        }
                        if (walkDataDailies.size() > 30) {
                            return walkDataDailies.subList(0, 30);
                        }
                    }
                }
                return walkDataDailies;
            }catch (DbException e) {
                LogUtils.e("保存数据报错");
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<WalkDataDaily> walkDataDailies) {
            super.onPostExecute(walkDataDailies);
            if(mSavedSuccessTreshold != null && walkDataDailies != null){
                LogUtils.d("" + walkDataDailies);
                mSavedSuccessTreshold.success(walkDataDailies);
            }
        }
    }

    public interface ResetNewPersonValue{
        void loadSuccess(long stepCount, double distance, double calories);
    }

    /**
     * 上传成功后删除对应数据
     */
    public static class DeleteUploadData extends AsyncTask<Void, Void, Boolean>{

        private List<WalkDataThresholdGag> mWalkDataThresholdGags;
        public DeleteUploadData(List<WalkDataThresholdGag> walkDataThresholdGags){
            this.mWalkDataThresholdGags = walkDataThresholdGags;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            if(mDbUtils == null){
                    return false;
            }
            if(mWalkDataThresholdGags != null){
                try {
                    mDbUtils.deleteAll(mWalkDataThresholdGags);
                } catch (DbException e) {
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            LogUtils.i("数据删除成功");
        }
    }
    /**
     * 用户登陆后同步信息
     */
    public static class UpdateLoginUserInfo extends AsyncTask<Void, Void, WalkDataThresholdGag>{
        private Context mContext;
        private long mTotalSteps = 0;
        private double mTotalDistance = 0;
        private double mTotalCalories = 0;

        private ResetNewPersonValue mResetNewPersonValue;
        public UpdateLoginUserInfo(Context context, long stepCount, double distance, double calories, ResetNewPersonValue resetNewPersonValue){
            mContext = context;
            mTotalSteps = stepCount;
            mTotalDistance = distance;
            mTotalCalories = calories;
            mResetNewPersonValue = resetNewPersonValue;
        }

        @Override
        protected WalkDataThresholdGag doInBackground(Void... params) {
            WalkDataThresholdGag result = new WalkDataThresholdGag();
            try {
                List<WalkDataThresholdGag> walkDataThresholdGags = mDbUtils.findAll(
                        Selector.from(WalkDataThresholdGag.class).where("userId", "=", 0));
                if(walkDataThresholdGags != null) {
                    for (WalkDataThresholdGag walkDataThresholdGag : walkDataThresholdGags) {
                        walkDataThresholdGag.userId = instance.userService.getLoginUserId();
                    }
                    mDbUtils.saveOrUpdateAll(walkDataThresholdGags);
                }

                int stepCounts = 0;
                double distance = 0;
                double calories = 0;
                if(mDbUtils.tableIsExist(WalkDataThresholdGag.class)){
                    Cursor cursor = mDbUtils.execQuery("select SUM(stepCount) stepCount,SUM(distance) distance," +
                            "SUM(calories) calories from walkdata_threshold_gaps where userId="
                            + instance.userService.getLoginUserId() + " and synTime>=" + TimeUtil.getDayTime(System.currentTimeMillis()));

                    while (cursor.moveToNext()){
                        stepCounts = cursor.getInt(cursor.getColumnIndex("stepCount"));
                        distance = cursor.getDouble(cursor.getColumnIndex("distance"));
                        calories = cursor.getDouble(cursor.getColumnIndex("calories"));
                        LogUtils.d("logint reset values : -------->>  stepCounts : " + stepCounts + ",   distance : " + distance + ",   calories : " + calories);
                    }
                }
                result.stepCount = stepCounts;
                result.distance = distance;
                result.calories = calories;
            } catch (DbException e) {
                LogUtils.e("保存数据报错");
            }
            return result;
        }

        @Override
        protected void onPostExecute(WalkDataThresholdGag aBoolean) {
            super.onPostExecute(aBoolean);
            if(mResetNewPersonValue != null){
                LogUtils.d("logint reset values : -------->>  stepCounts : " + (aBoolean.stepCount + mTotalSteps)
                        + ",   distance : " + (aBoolean.distance + mTotalDistance) + ",   calories : " + (aBoolean.calories + mTotalCalories));
                mResetNewPersonValue.loadSuccess(aBoolean.stepCount + mTotalSteps, aBoolean.distance + mTotalDistance, aBoolean.calories + mTotalCalories);
            }
        }
    }

    /**
     * 步数到达一定上限后保存数据，然后上传
     */
    public static class SaveWalkDataPerThreshold extends AsyncTask<Void, Void, Boolean> {

        private Context mContext;
        private long mTotalSteps = 0;
        private double mTotalDistance = 0;
        private double mTotalCalories = 0;
        private long mSynTime = 0;
        private long mLastSynTime = 0;

        private SavedSuccessTreshold mSavedSuccessTreshold;

        public SaveWalkDataPerThreshold(Context context, long tempSteps, double tempDistance,
                                        double tempCalories, long synTime, long lastSynTime,
                                        SavedSuccessTreshold savedSuccessTreshold) {
            DecimalFormat decimalFormat = new DecimalFormat("#.###");
            mTotalSteps = tempSteps;
            mTotalDistance = Double.parseDouble(decimalFormat.format(tempDistance));
            mTotalCalories = Double.parseDouble(decimalFormat.format(tempCalories));
            mSynTime = synTime;
            mLastSynTime = lastSynTime;

            mContext = context;
            mSavedSuccessTreshold = savedSuccessTreshold;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            if (null == mDbUtils) {
                return false;
            }
            try {
                WalkDataThresholdGag info = new WalkDataThresholdGag();
                info.userId = instance.userService.getLoginUserId();
                info.stepCount = mTotalSteps;
                info.distance = mTotalDistance;
                info.calories = mTotalCalories;
                info.synTime = mSynTime;
                info.targetStepCount = PedometerUtil.getTargetStep(mContext);
                mDbUtils.saveOrUpdate(info);
                return true;
            } catch (DbException e) {
                LogUtils.e("保存数据报错");
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            List<WalkDataThresholdGag> walkDataThresholdGags = loadTresholdWalkDataByTime(mSynTime, mLastSynTime);
            LogUtils.d("result : " + walkDataThresholdGags);
            mSavedSuccessTreshold.success(walkDataThresholdGags);
        }


    }

    /**
     * 每小时保存数据
     */
    public static class SaveWalkDataInfoPerHourTask extends AsyncTask<Void, Void, Boolean> {

        private Context mContext;
        private int mTotalSteps = 0;
        private double mTotalDistance = 0;
        private double mTotalCalories = 0;
        private long mSynTime = 0;

        private boolean isDaily = false;

        /**
         * @param context
         * @param tempSteps    截止保存时刻的总步数
         * @param tempDistance 截止保存时刻的总距离
         * @param tempCalories 截止保存时刻的总卡路里
         * @param synTime      保存时间
         */
        public SaveWalkDataInfoPerHourTask(Context context, int tempSteps, double tempDistance, double tempCalories, long synTime) {
            mTotalSteps = tempSteps;
            mTotalDistance = tempDistance;
            mTotalCalories = tempCalories;
            mSynTime = synTime;

            mContext = context;
        }

        public void setDailySave() {
            isDaily = true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            LogUtils.v("SaveWalkDataInfoPerHourTask onPostExecute");
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Boolean result;

            if (isDaily) {
                saveWalkDataInfoDaily(mContext, mTotalSteps, mTotalDistance,
                        mTotalCalories, mSynTime);
            }
            result = saveWalkDataInfoPerHour(mContext, mTotalSteps, mTotalDistance,
                    mTotalCalories, mSynTime);

            return result;
        }
    }

    /**
     * 保存saveWalkDataInfoPerHour 保存小时时间到前一毫秒
     * 0:00-0:59 作为一个时间段。
     * 23:00-23:59作为一个时间段。
     *
     * @param context
     * @param steps
     * @param distance
     * @param calories
     * @param synTime
     * @return
     */
    private static Boolean saveWalkDataInfoPerHour(Context context, int steps, double distance, double calories, long synTime) {
        if (null == mDbUtils) {
            return false;
        }
        try {
            WalkDataInfoPerHour info = new WalkDataInfoPerHour();
            info.stepCount = steps;
            info.distance = distance;
            info.walkTime = synTime;
            info.calories = calories;
            info.targetStepCount = (int) PedometerUtil.getTargetStep(context);
            mDbUtils.saveOrUpdate(info);
            return true;
        } catch (DbException e) {
            LogUtils.e("保存数据报错");
        }
        return false;
    }

    /**
     * 当日数据保存
     *
     * @param context
     * @param steps
     * @param distance
     * @param calories
     * @param synTime
     * @return
     */
    public static boolean saveWalkDataInfoDaily(Context context, int steps, double distance, double calories, long synTime) {
        if (null == mDbUtils) {
            return false;
        }
        try {
            WalkDataInfo info = new WalkDataInfo();
            info.stepCount = steps;
            info.distance = distance;
            //整点小时时间  == 当日时间    即（23点或者0点 == 0点）
            info.walkTime = TimeUtil.getHourTime(synTime) == TimeUtil
                    .getDayTime(synTime) ? TimeUtil.getDayTime(synTime) - 1
                    : synTime;
            //隔天判断后造成，小于5分钟，即 23:55，作为前一日数据进行存储
            if (synTime - TimeUtil.getDayTime(synTime) > 5 * 60 * 1000
                    && synTime - TimeUtil.getDayTime(synTime) < 24 * 60 * 60
                    * 1000 - 5 * 60 * 1000) {
                info.walkTime = TimeUtil.getDayTime(synTime) - 1;
            }
            info.calories = calories;
            info.targetStepCount = (int) PedometerUtil.getTargetStep(context);
            mDbUtils.saveOrUpdate(info);
            return true;
        } catch (DbException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<WalkDataThresholdGag> loadTresholdWalkDataByTime(long now, long lastSynTime) {
        if (null == mDbUtils) {
            return null;
        }
        try {
            if (mDbUtils.tableIsExist(WalkDataThresholdGag.class)) {
                List<WalkDataThresholdGag> list = mDbUtils.findAll(Selector
                        .from(WalkDataThresholdGag.class)
                        .where("synTime", "<=", now)
                        .and("synTime", ">", lastSynTime).orderBy("synTime"));
                return list;
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static List<SyncParam> dataChange(long lastSynTime, List<WalkDataDaily> walkDataThresholdGags) {
        List<SyncParam> syncParamLists = new ArrayList<>();
        for (WalkDataDaily walkDataThresholdGag : walkDataThresholdGags) {
            SyncParam syncParam = new SyncParam();
            syncParam.lastRecordTime = lastSynTime;
            syncParam.stepDate = walkDataThresholdGag.synTime;
            syncParam.stepCounts = walkDataThresholdGag.stepCount;
            syncParam.targetSteps = walkDataThresholdGag.targetStepCount;
            syncParam.fats = Double.isInfinite(walkDataThresholdGag.calories) ? 0 : (float) (walkDataThresholdGag.calories / 9f);
            syncParam.calorie = Double.isInfinite(walkDataThresholdGag.calories) ? 0 : (float) walkDataThresholdGag.calories;
            syncParam.distance = Double.isInfinite(walkDataThresholdGag.distance) ? 0 : (float) walkDataThresholdGag.distance;
            syncParamLists.add(syncParam);
        }
        return syncParamLists;
    }

    public static long getLocalTotleDistance(Context ctx, List<PedometerHistoryResult> alist) {
        if (alist == null || alist.size() <= 1) {
            return 0;
        }

        SharedPreferences editor = ctx.getSharedPreferences("state", 0);
        long lastSynTime = editor.getLong(StepService.LASTSYNTIME, -1);

        if (lastSynTime == -1) return 0;

        int pos = alist.size() - 2;
        long distance = 0;
        for (pos = alist.size() - 2; pos > 0; pos--) {
            if (alist.get(pos).stepDate >= TimeUtil.getDayTime(lastSynTime)) {
                distance += alist.get(pos).distance;
            } else {
                break;
            }
        }
        return distance;
    }

    public static class DebugData extends AsyncTask<Void, Void, Boolean>{
        private long mTotalSteps = 0;
        private double mTotalDistance = 0;
        private double mTotalCalories = 0;
        private double mSpeed;
        private double mPace;
        private long mSynTime = 0;
        public DebugData(long tempSteps, double tempDistance, double tempCalories, double tempSpeed, double tempPace, long synTime){
            DecimalFormat decimalFormat = new DecimalFormat("#.000");
            mTotalSteps = tempSteps;
            mTotalDistance = Double.parseDouble(decimalFormat.format(tempDistance));
            mTotalCalories = Double.parseDouble(decimalFormat.format(tempCalories));
            mSpeed = Double.parseDouble(decimalFormat.format(tempSpeed));
            mPace = Double.parseDouble(decimalFormat.format(tempPace));
            mSynTime = synTime;
        }
        @Override
        protected Boolean doInBackground(Void... params) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS", Locale.SIMPLIFIED_CHINESE);
            String text = "synchronize time : " + simpleDateFormat.format(new Date(mSynTime)) + "    temp steps : " + mTotalSteps
                    + "   temp distance : " + mTotalDistance + "   temp calories : " + mTotalCalories
                    + "   temp pace : " + mPace + "   temp speed : " + mSpeed + "\n";
            File file = new File(Environment.getExternalStorageDirectory() + File.separator + "step_debug_data.txt");
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(file,true);
                fileOutputStream.write(text.getBytes());
            } catch (FileNotFoundException e) {
                LogUtils.e("保存数据报错");
            } catch (IOException e) {
                LogUtils.e("保存数据报错");
            }finally {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    LogUtils.e("保存数据报错");
                }
            }
            return null;
        }
    }

    public static class DebugSpeed extends AsyncTask<Void, Void, Boolean>{
        private double mPace;
        private double mSpeed;
        private double mCalories;
        private double mDistance;

        public DebugSpeed(double pace, double speed, double calories, double distance) {
            DecimalFormat decimalFormat = new DecimalFormat("#.000");
            mPace = Double.parseDouble(decimalFormat.format(pace));
            mSpeed = Double.parseDouble(decimalFormat.format(speed));
            mCalories = Double.parseDouble(decimalFormat.format(calories));
            mDistance = Double.parseDouble(decimalFormat.format(distance));
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS", Locale.SIMPLIFIED_CHINESE);
            String text = "time : " + simpleDateFormat.format(new Date(System.currentTimeMillis())) + "    mPace : " + mPace
                    + "   mSpeed: " + mSpeed + "   mCalories : " + mCalories
                    + "   mDistance : " + mDistance + "\n";
            File file = new File(Environment.getExternalStorageDirectory() + File.separator + "step_debug_speed_data.txt");
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(file,true);
                fileOutputStream.write(text.getBytes());
            } catch (FileNotFoundException e) {
                LogUtils.e("保存数据报错");
            } catch (IOException e) {
                LogUtils.e("保存数据报错");
            }finally {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    LogUtils.e("保存数据报错");
                }
            }
            return null;
        }
    }

    public static class DebugSpeedDelta extends AsyncTask<Void, Void, Boolean>{
        private long mCurrentTime;
        private long mTimeDelta;

        public DebugSpeedDelta(long thisStepTime, long pace) {
            mCurrentTime = thisStepTime;
            mTimeDelta = pace;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS", Locale.SIMPLIFIED_CHINESE);
            String text = "time : " + simpleDateFormat.format(new Date(System.currentTimeMillis())) + "current time : " + mCurrentTime + "    mTimeDelta : " + mTimeDelta + "\n";
            File file = new File(Environment.getExternalStorageDirectory() + File.separator + "step_debug_speed_delta_data.txt");
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(file,true);
                fileOutputStream.write(text.getBytes());
            } catch (FileNotFoundException e) {
                LogUtils.e("保存数据报错");
            } catch (IOException e) {
                LogUtils.e("保存数据报错");
            }finally {
                try {
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                } catch (IOException e) {
                    LogUtils.e("保存数据报错");
                }
            }
            return null;
        }
    }
}
