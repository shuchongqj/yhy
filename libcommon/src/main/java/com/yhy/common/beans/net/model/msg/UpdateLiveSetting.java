package com.yhy.common.beans.net.model.msg;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:UpdateLiveSetting
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-8-30
 * Time:11:17
 * Version 1.1.0
 */

public class UpdateLiveSetting implements Serializable {
    private static final long serialVersionUID = 2631955117799689312L;

    /**
     * 直播间ID
     */
    public long roomId;

    /**
     * 修改公告
     */
    public String liveNotice;

    /**
     * 修改类别
     */
    public String liveCategory;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static UpdateLiveSetting deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static UpdateLiveSetting deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            UpdateLiveSetting result = new UpdateLiveSetting();

            // 直播间ID
            result.roomId = json.optLong("roomId");
            // 修改公告

            if(!json.isNull("liveNotice")){
                result.liveNotice = json.optString("liveNotice", null);
            }
            // 修改类别

            if(!json.isNull("liveCategory")){
                result.liveCategory = json.optString("liveCategory", null);
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

        // 直播间ID
        json.put("roomId", this.roomId);

        // 修改公告
        if(this.liveNotice != null) { json.put("liveNotice", this.liveNotice); }

        // 修改类别
        if(this.liveCategory != null) { json.put("liveCategory", this.liveCategory); }

        return json;
    }
}
