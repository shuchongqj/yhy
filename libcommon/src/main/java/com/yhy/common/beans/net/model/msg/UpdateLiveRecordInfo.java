package com.yhy.common.beans.net.model.msg;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:UpdateLiveRecordInfo
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-8-16
 * Time:14:06
 * Version 1.0
 * Description:
 */
public class UpdateLiveRecordInfo implements Serializable{
    private static final long serialVersionUID = -2344766071726884441L;

    /**
     * 直播记录ID,必填
     */
    public long liveId;

    /**
     * 修改直播状态
     */
    public String liveStatus;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static UpdateLiveRecordInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static UpdateLiveRecordInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            UpdateLiveRecordInfo result = new UpdateLiveRecordInfo();

            // 直播记录ID,必填
            result.liveId = json.optLong("liveId");
            // 修改直播状态

            if(!json.isNull("liveStatus")){
                result.liveStatus = json.optString("liveStatus", null);
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

        // 直播记录ID,必填
        json.put("liveId", this.liveId);

        // 修改直播状态
        if(this.liveStatus != null) { json.put("liveStatus", this.liveStatus); }

        return json;
    }
}
