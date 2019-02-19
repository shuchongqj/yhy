package com.yhy.common.beans.net.model.common;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:CrashInfoList
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-11-17
 * Time:14:30
 * Version 1.1.0
 */


public class CrashInfoList implements Serializable {
    private static final long serialVersionUID = -634455277184140716L;

    /**
     * crash列表
     */
    public List<CrashInfo> crashList;
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static CrashInfoList deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static CrashInfoList deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            CrashInfoList result = new CrashInfoList();

            // crash列表
            JSONArray crashListArray = json.optJSONArray("crashList");
            if (crashListArray != null) {
                int len = crashListArray.length();
                result.crashList = new ArrayList<CrashInfo>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = crashListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.crashList.add(CrashInfo.deserialize(jo));
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

        // crash列表
        if (this.crashList != null) {
            JSONArray crashListArray = new JSONArray();
            for (CrashInfo value : this.crashList)
            {
                if (value != null) {
                    crashListArray.put(value.serialize());
                }
            }
            json.put("crashList", crashListArray);
        }

        return json;
    }
}
