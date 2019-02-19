package com.yhy.common.beans.net.model.guide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:NearGuideInfo
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-8-22
 * Time:19:27
 * Version 1.1.0
 */

public class NearGuideInfo implements Serializable {
    private static final long serialVersionUID = 2123977561604446755L;

    /**
     * 城市code
     */
    public String cityCode;

    /**
     * 经度
     */
    public long latitude;

    /**
     * 维度
     */
    public long longitude;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static NearGuideInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static NearGuideInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            NearGuideInfo result = new NearGuideInfo();

            // 城市code

            if(!json.isNull("cityCode")){
                result.cityCode = json.optString("cityCode", null);
            }
            // 经度
            result.latitude = json.optLong("latitude");
            // 维度
            result.longitude = json.optLong("longitude");
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 城市code
        if(this.cityCode != null) { json.put("cityCode", this.cityCode); }

        // 经度
        json.put("latitude", this.latitude);

        // 维度
        json.put("longitude", this.longitude);

        return json;
    }
}
