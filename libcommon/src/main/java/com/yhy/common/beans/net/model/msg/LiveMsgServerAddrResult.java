package com.yhy.common.beans.net.model.msg;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:LiveMsgServerAddrResult
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-8-26
 * Time:10:48
 * Version 1.1.0
 */

public class LiveMsgServerAddrResult implements Serializable {
    private static final long serialVersionUID = -7162016452408958860L;

    /**
     * 登录用户ID
     */
    public long userId;

    /**
     * 主IP
     */
    public String priorIp;

    /**
     * 备IP
     */
    public String backupIp;

    /**
     * 端口
     */
    public int port;

    /**
     * 返回结果描述
     */
    public String resultDesc;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static LiveMsgServerAddrResult deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static LiveMsgServerAddrResult deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            LiveMsgServerAddrResult result = new LiveMsgServerAddrResult();

            // 登录用户ID
            result.userId = json.optLong("userId");
            // 主IP

            if(!json.isNull("priorIp")){
                result.priorIp = json.optString("priorIp", null);
            }
            // 备IP

            if(!json.isNull("backupIp")){
                result.backupIp = json.optString("backupIp", null);
            }
            // 端口
            result.port = json.optInt("port");
            // 返回结果描述

            if(!json.isNull("resultDesc")){
                result.resultDesc = json.optString("resultDesc", null);
            }
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 登录用户ID
        json.put("userId", this.userId);

        // 主IP
        if(this.priorIp != null) { json.put("priorIp", this.priorIp); }

        // 备IP
        if(this.backupIp != null) { json.put("backupIp", this.backupIp); }

        // 端口
        json.put("port", this.port);

        // 返回结果描述
        if(this.resultDesc != null) { json.put("resultDesc", this.resultDesc); }

        return json;
    }
}
