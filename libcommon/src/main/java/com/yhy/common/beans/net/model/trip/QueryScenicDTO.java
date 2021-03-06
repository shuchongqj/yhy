// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.trip;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class QueryScenicDTO implements Serializable {

    private static final long serialVersionUID = -4346867633304389094L;
    /**
     * 区域code
     */
    public long regionCode;

    /**
     * 主题id
     */
    public long subjectId;

    /**
     * 页号
     */
    public int pageNo;

    /**
     * 页面大小
     */
    public int pageSize;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static QueryScenicDTO deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static QueryScenicDTO deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            QueryScenicDTO result = new QueryScenicDTO();

            // 区域code
            result.regionCode = json.optLong("regionCode");
            // 主题id
            result.subjectId = json.optLong("subjectId");
            // 页号
            result.pageNo = json.optInt("pageNo");
            // 页面大小
            result.pageSize = json.optInt("pageSize");
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 区域code
        json.put("regionCode", this.regionCode);

        // 主题id
        json.put("subjectId", this.subjectId);

        // 页号
        json.put("pageNo", this.pageNo);

        // 页面大小
        json.put("pageSize", this.pageSize);

        return json;
    }

}
  