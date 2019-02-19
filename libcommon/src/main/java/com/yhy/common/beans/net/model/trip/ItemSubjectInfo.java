package com.yhy.common.beans.net.model.trip;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:SubjectInfo
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2015-11-24
 * Time:13:44
 * Version 1.0
 * Description:
 */
public class ItemSubjectInfo implements Serializable {
    private static final long serialVersionUID = 7075780382974885892L;

    /**
     * 主题id
     */
    public long subjectId;

    /**
     * 主题ming
     */
    public String name;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static ItemSubjectInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static ItemSubjectInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            ItemSubjectInfo result = new ItemSubjectInfo();

            // 主题id
            result.subjectId = json.optLong("subjectId");
            // 主题ming

            if (!json.isNull("name")) {
                result.name = json.optString("name", null);
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

        // 主题id
        json.put("subjectId", this.subjectId);

        // 主题ming
        if (this.name != null) {
            json.put("name", this.name);
        }

        return json;
    }
}
