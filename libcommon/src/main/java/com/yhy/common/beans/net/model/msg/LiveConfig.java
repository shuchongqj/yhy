package com.yhy.common.beans.net.model.msg;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created with Android Studio.
 * Title:LiveConfig
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-9-14
 * Time:16:09
 * Version 1.1.0
 */

public class LiveConfig implements Serializable {
    private static final long serialVersionUID = -2821548948748861214L;

    /**
     * 直播配置文本List
     */
    public ArrayList<String> configContent;
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static LiveConfig deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static LiveConfig deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            LiveConfig result = new LiveConfig();

            // 直播配置文本List
            JSONArray configContentArray = json.optJSONArray("configContent");
            if (configContentArray != null) {
                int len = configContentArray.length();
                result.configContent = new
                        ArrayList<String>(len);
                for (int i = 0; i < len; i++) {

                    if(!configContentArray.isNull(i)){
                        result.configContent.add(configContentArray.optString(i, null));
                    }else{
                        result.configContent.add(i, null);
                    }

                }
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

        // 直播配置文本List
        if (this.configContent != null) {
            JSONArray configContentArray = new JSONArray();
            for (String value : this.configContent)
            {
                configContentArray.put(value);
            }
            json.put("configContent", configContentArray);
        }

        return json;
    }
}
