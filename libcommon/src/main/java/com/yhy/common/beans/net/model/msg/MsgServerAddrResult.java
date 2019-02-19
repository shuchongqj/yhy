package com.yhy.common.beans.net.model.msg;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:MsgServerAddrResult
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-8-17
 * Time:16:06
 * Version 1.0
 * Description:
 */
public class MsgServerAddrResult implements Serializable{
    private static final long serialVersionUID = 5198373102837512707L;

    /**
     * 直播ID
     */
    public long liveId;

    /**
     * 观看直播用户ID
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
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static MsgServerAddrResult deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static MsgServerAddrResult deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            MsgServerAddrResult result = new MsgServerAddrResult();

            // 直播ID
            result.liveId = json.optLong("liveId");
            // 观看直播用户ID
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
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 直播ID
        json.put("liveId", this.liveId);

        // 观看直播用户ID
        json.put("userId", this.userId);

        // 主IP
        if(this.priorIp != null) { json.put("priorIp", this.priorIp); }

        // 备IP
        if(this.backupIp != null) { json.put("backupIp", this.backupIp); }

        // 端口
        json.put("port", this.port);

        return json;
    }
}
