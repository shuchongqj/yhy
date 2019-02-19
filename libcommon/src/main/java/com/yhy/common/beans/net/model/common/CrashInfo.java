package com.yhy.common.beans.net.model.common;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:CrashInfo
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-11-16
 * Time:16:04
 * Version 1.1.0
 */


public class CrashInfo implements Serializable {
    private static final long serialVersionUID = -6781282494560682673L;

    /**
     * 渠道
     */
    public String channel;

    /**
     * app版本
     */
    public String appVersion;

    /**
     * 平台
     */
    public String platform;

    /**
     * 系统版本
     */
    public String systemVersion;

    /**
     * 手机型号
     */
    public String phoneModel;

    /**
     * 错误内容
     */
    public String errorMsg;

    /**
     * 发生crash时间戳
     */
    public long crashTime;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static CrashInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static CrashInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            CrashInfo result = new CrashInfo();

            // 渠道

            if(!json.isNull("channel")){
                result.channel = json.optString("channel", null);
            }
            // app版本

            if(!json.isNull("appVersion")){
                result.appVersion = json.optString("appVersion", null);
            }
            // 平台

            if(!json.isNull("platform")){
                result.platform = json.optString("platform", null);
            }
            // 系统版本

            if(!json.isNull("systemVersion")){
                result.systemVersion = json.optString("systemVersion", null);
            }
            // 手机型号

            if(!json.isNull("phoneModel")){
                result.phoneModel = json.optString("phoneModel", null);
            }
            // 错误内容

            if(!json.isNull("errorMsg")){
                result.errorMsg = json.optString("errorMsg", null);
            }
            // 发生crash时间戳
            result.crashTime = json.optLong("crashTime");
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 渠道
        if(this.channel != null) { json.put("channel", this.channel); }

        // app版本
        if(this.appVersion != null) { json.put("appVersion", this.appVersion); }

        // 平台
        if(this.platform != null) { json.put("platform", this.platform); }

        // 系统版本
        if(this.systemVersion != null) { json.put("systemVersion", this.systemVersion); }

        // 手机型号
        if(this.phoneModel != null) { json.put("phoneModel", this.phoneModel); }

        // 错误内容
        if(this.errorMsg != null) { json.put("errorMsg", this.errorMsg); }

        // 发生crash时间戳
        json.put("crashTime", this.crashTime);

        return json;
    }
}
