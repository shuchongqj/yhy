package com.yhy.common.beans.net.model.msg;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:DeleteLiveListInfo
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-9-14
 * Time:16:01
 * Version 1.1.0
 */

public class DeleteLiveListInfo implements Serializable {
    private static final long serialVersionUID = 6156598152143669783L;

    /**
     * 回放ID数组
     */
    public long[] idArray;
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static DeleteLiveListInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static DeleteLiveListInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            DeleteLiveListInfo result = new DeleteLiveListInfo();

            // 回放ID数组
            JSONArray idArrayArray = json.optJSONArray("idArray");
            if (idArrayArray != null) {
                int len = idArrayArray.length();
                result.idArray = new long[len];
                for (int i = 0; i < len; i++) {
                    result.idArray[i] = idArrayArray.optLong(i);
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

        // 回放ID数组
        if (this.idArray != null) {
            JSONArray idArrayArray = new JSONArray();
            for (long value : this.idArray)
            {
                idArrayArray.put(value);
            }
            json.put("idArray", idArrayArray);
        }

        return json;
    }
}
