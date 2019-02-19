package com.yhy.common.beans.net.model.guide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:GuideTipsInfo
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-8-22
 * Time:11:33
 * Version 1.1.0
 */

public class GuideTipsInfo implements Serializable {
    private static final long serialVersionUID = 9158675778102990673L;

    /**
     * 实用锦囊数据条目
     */
    public List<GuideTipsEntry> tipList;
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static GuideTipsInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static GuideTipsInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            GuideTipsInfo result = new GuideTipsInfo();

            // 实用锦囊数据条目
            JSONArray tipListArray = json.optJSONArray("tipList");
            if (tipListArray != null) {
                int len = tipListArray.length();
                result.tipList = new ArrayList<GuideTipsEntry>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = tipListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.tipList.add(GuideTipsEntry.deserialize(jo));
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

        // 实用锦囊数据条目
        if (this.tipList != null) {
            JSONArray tipListArray = new JSONArray();
            for (GuideTipsEntry value : this.tipList)
            {
                if (value != null) {
                    tipListArray.put(value.serialize());
                }
            }
            json.put("tipList", tipListArray);
        }

        return json;
    }
}
