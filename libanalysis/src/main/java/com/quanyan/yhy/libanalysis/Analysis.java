package com.quanyan.yhy.libanalysis;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.tendcloud.tenddata.TCAgent;
import com.tendcloud.tenddata.TDAccount;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.yhy.common.utils.JSONUtils;
import com.yhy.network.YhyCallback;
import com.yhy.network.YhyCode;
import com.yhy.network.api.log.LogApi;
import com.yhy.network.manager.AccountManager;
import com.yhy.network.req.log.LogUploadReq;
import com.yhy.network.resp.Response;
import com.yhy.network.resp.log.LogUploadResp;
import com.yhy.network.utils.DeviceInfoUtils;
import com.yhy.network.utils.RequestHandlerKt;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 统计事件 发射器
 * Created by Jiervs on 2018/6/6.
 */

public class Analysis {

    public static final String TD_ACCOUNT_TYPE = "TDAccount_Type";
    public static final String TC_APP_ID = "TC_APP_ID";
    public static final String UMENG_KEY = "UMENG_KEY";

    public static final String TAG = "TAG";   //   类名（即当前页面）
    public static final String TAB = "TAB";   //   顶部tablayout的当前tab

    public static final String UID = "UID";   //   类名（即当前页面）
    public static final String PAGENAME = "PAGENAME";   //   顶部tablayout的当前tab

    //初始化统计平台的配置,map可以存放一些额外信息
    public static void initAnalysis(Context context, String channel, Map<String, String> map) {
        String Tc_AppId = map.get(TC_APP_ID);
        String umengKey = map.get(UMENG_KEY);

        //TalkingData统计
        TCAgent.init(context, Tc_AppId, channel);
        TCAgent.setReportUncaughtExceptions(true);

        //友盟统计
        UMConfigure.init(context, umengKey, channel, UMConfigure.DEVICE_TYPE_PHONE, "1fe6a20054bcef865eeb0991ee84525b");
        UMConfigure.setEncryptEnabled(true);
    }

    //注册账号
    public static void onRegister(String accountId, String name, @Nullable Map<String, Object> map) {
        TDAccount.AccountType type = (TDAccount.AccountType) map.get(TD_ACCOUNT_TYPE);
        TCAgent.onRegister(accountId, type, name);
    }

    //登录账号
    public static void onLogin(String accountId, String name, @Nullable Map<String, Object> map) {
        TDAccount.AccountType type = (TDAccount.AccountType) map.get(TD_ACCOUNT_TYPE);
        TCAgent.onLogin(accountId, type, name);
    }

    //各种发射函数(根据需求扩展)
    public static void pushEvent(Context context, String eventId) {
        MobclickAgent.onEvent(context, eventId);
        TCAgent.onEvent(context, eventId);
    }

    public static void pushEvent(Context context, String eventId, String eventLabel) {
        TCAgent.onEvent(context, eventId, eventLabel);
        MobclickAgent.onEvent(context, eventId, eventLabel);
    }

    public static void pushEvent(Context context, String eventId, Map<String, String> map) {
        TCAgent.onEvent(context, eventId, null, map);
        MobclickAgent.onEvent(context, eventId, map);
    }

    public static void pushEvent(Context context, String eventId, String eventLabel, Map<String, String> map) {
        TCAgent.onEvent(context, eventId, eventLabel, map);
        //map.put("eventLabel",eventLabel);
        MobclickAgent.onEvent(context, eventId, map);
    }

    public static void pushEvent(Context context, String eventId, Builder builder) {

        LogUploadReq.Log log = new LogUploadReq.Log();
        log.setEventCode(eventId);
        log.setParam(builder.build());

        pushCircleEvent(context, log);

        // 这里是把原来的友盟的埋点加上
        pushEvent(context, eventId, builder.build());
    }

    /**
     * 选择城市打点
     *
     * @param context
     * @param uid      用户id
     * @param cityName 城市和区域名称
     * @param pageName 页面名称中文
     */
    public static void cityAnalysis(Context context, long uid, String cityName, String pageName) {
        HashMap<String, String> params = new HashMap<>();
        params.put("name", cityName);
        params.put("pageName", pageName);
        Analysis.pushEvent(context, AnEvent.SELECTED_CITY, uid > 0 ? String.valueOf(uid) : "0", params);
    }

    public static void notificationReceive(Context context, String msgId){
        HashMap<String, String> params = new HashMap<>();
        Analysis.pushEvent(context, AnEvent.NotificationReceive, msgId, params);
    }

    public static void notificationClick(Context context, String msgId){
        HashMap<String, String> params = new HashMap<>();
        Analysis.pushEvent(context, AnEvent.NotificationClick, msgId, params);
    }

    /**
     * 圈子打点
     */
    private static void pushCircleEvent(Context context, LogUploadReq.Log log) {
        saveData(context, log);

    }

    /**
     * 存储上传失败的圈子打点，再次上传的时候使用
     *
     * @param context
     * @param logs
     */
    private static void saveFailData(Context context, List<LogUploadReq.Log> logs) {
        // 已经有存储，add后添加
        String logsJson = AnCache.getAnCacheCircleFailedLogs(context);
        if (!TextUtils.isEmpty(logsJson)) {
            List<LogUploadReq.Log> cachedlogs = JSONUtils.convertToArrayList(logsJson, LogUploadReq.Log.class);
            if (cachedlogs != null && cachedlogs.size() > 0) {
                cachedlogs.addAll(logs);
                if (cachedlogs.size() >= 9) {
                    upLoadLogs(context, cachedlogs, true);
                } else {
                    AnCache.saveAnCacheCircleFailedLogs(context, JSONUtils.toJson(cachedlogs));
                }
                return;
            }
        }

        // 没有就直接添加
        AnCache.saveAnCacheCircleFailedLogs(context, JSONUtils.toJson(logs));

    }

    private static void saveData(Context context, LogUploadReq.Log log) {
        // 已经有存储，add后添加
        String logsJson = AnCache.getAnCacheCircleLogs(context);
        if (!TextUtils.isEmpty(logsJson)) {
            List<LogUploadReq.Log> cachedlogs = JSONUtils.convertToArrayList(logsJson, LogUploadReq.Log.class);
            if (cachedlogs != null && cachedlogs.size() > 0) {
                if (cachedlogs.size() >= 9) {
                    cachedlogs.add(log);
                    upLoadLogs(context, cachedlogs, false);
                } else {
                    cachedlogs.add(log);
                    AnCache.saveAnCacheCircleLogs(context, JSONUtils.toJson(cachedlogs));

                }
                return;
            }
        }

        // 没有就直接添加
        List<LogUploadReq.Log> logs = new ArrayList<>();
        logs.add(log);
        AnCache.saveAnCacheCircleLogs(context, JSONUtils.toJson(logs));

    }

    public static void upLoadLogs(Context context, List<LogUploadReq.Log> cachedlogs, boolean isFailedLogs) {
        YhyCallback<Response<LogUploadResp>> callback = new YhyCallback<Response<LogUploadResp>>() {
            @Override
            public void onSuccess(Response<LogUploadResp> data) {
                if (isFailedLogs) {
                    AnCache.saveAnCacheCircleFailedLogs(context, "");
                } else {
                    AnCache.saveAnCacheCircleLogs(context, "");
                }
            }

            @Override
            public void onFail(YhyCode code, Exception exception) {
                if (!isFailedLogs) {
                    saveFailData(context, cachedlogs);
                }

            }
        };
        new LogApi().uploadLog(new LogUploadReq(cachedlogs), callback).execLogAsync();

    }


    public static class Builder {
        public Map<String, String> map;

        public Builder() {
            map = new HashMap<>();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = formatter.format(System.currentTimeMillis());
            map.put("_time", time);
            map.put("_uid", String.valueOf(AccountManager.Companion.getAccountManager().getUserId()));
            map.put("_tk", String.valueOf(AccountManager.Companion.getAccountManager().getUserToken()));
            map.put("_dtk", String.valueOf(AccountManager.Companion.getAccountManager().getDeviceToken()));
            map.put("_pn", String.valueOf(AccountManager.Companion.getAccountManager().getPhoneNumber()));
            map.put("_did", String.valueOf(AccountManager.Companion.getAccountManager().getDeviceId()));
            map.put("_cip", DeviceInfoUtils.getLocalIpAddress());

        }

        public Builder setLng(String _lng) {
            map.put("_lng", _lng);
            return this;
        }

        public Builder setLat(String _lat) {
            map.put("_lat", _lat);
            return this;
        }

        public Builder setUid(String _uid) {
            map.put("_uid", _uid);
            return this;
        }

        public Builder setTab(String tab) {
            map.put("tab", tab);
            return this;
        }

        public Builder setDragtime(int dragtime) {
            map.put("dragtime", String.valueOf(dragtime));
            return this;
        }

        public Builder setId(String id) {
            map.put("id", id);
            return this;
        }

        /**
         * @param position 外面传进来是从0开始。后台要求从1开始于是加1
         * @return
         */
        public Builder setPosition(int position) {
            map.put("position", String.valueOf(position + 1));
            return this;
        }

        public Builder setMode(int mode) {
            map.put("mode", String.valueOf(mode));
            return this;
        }

        public Builder setQuantity(int quantity) {
            map.put("quantity", String.valueOf(quantity));
            return this;
        }

        public Builder setPlayMode(String playMode) {
            map.put("playmode", playMode);
            return this;
        }

        public Builder setLastPage(String lastpage) {
            map.put("lastpage", lastpage);
            return this;
        }

        public Builder setLiveState(boolean livestate) {
            map.put("livestate", String.valueOf(livestate));
            return this;
        }

        public Builder setFinished(boolean finished) {
            map.put("finished", String.valueOf(finished));
            return this;
        }

        public Builder setPlayPause(String playpause) {
            map.put("playpause", playpause);
            return this;
        }

        public Builder setFullResize(String fullresize) {
            map.put("fullresize", fullresize);
            return this;
        }

        public Builder setList(boolean list) {
            map.put("list", String.valueOf(list));
            return this;
        }

        public Builder setPage(String page) {
            map.put("page", page);
            return this;
        }

        public Builder setType(String type) {
            map.put("type", type);
            return this;
        }

        public Builder setTrendType(String trendtype) {
            map.put("trendtype", trendtype);
            return this;
        }

        public Builder setContent(String content) {
            map.put("content", content);
            return this;
        }

        public Builder setMap(HashMap<String, String> params) {
            for (String key : params.keySet()) {
                map.put(key, params.get(key));
            }
            return this;
        }

        public Map<String, String> build() {
            return map;
        }
    }
}
