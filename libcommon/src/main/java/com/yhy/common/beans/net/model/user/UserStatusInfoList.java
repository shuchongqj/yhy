package com.yhy.common.beans.net.model.user;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:UserStatusInfo_ArrayResp
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-7-27
 * Time:15:49
 * Version 1.0
 * Description:
 */
public class UserStatusInfoList implements Serializable{
    private static final long serialVersionUID = -3103372833920525860L;
    /**
     * 用户状态
     */
    public List<UserStatusInfo> value;
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static UserStatusInfoList deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static UserStatusInfoList deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            UserStatusInfoList result = new UserStatusInfoList();

            // 用户状态
            JSONArray valueArray = json.optJSONArray("value");
            if (valueArray != null) {
                int len = valueArray.length();
                result.value = new ArrayList<UserStatusInfo>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = valueArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.value.add(UserStatusInfo.deserialize(jo));
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

        // 用户状态
        if (this.value != null) {
            JSONArray valueArray = new JSONArray();
            for (UserStatusInfo value : this.value)
            {
                if (value != null) {
                    valueArray.put(value.serialize());
                }
            }
            json.put("value", valueArray);
        }

        return json;
    }
}
