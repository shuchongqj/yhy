// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.rc;
    
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class CategoryTag implements Serializable{

    private static final long serialVersionUID = 6827315820353609886L;
    /**
     * 目录标签名称
     */
    public String name;

    /**
     * 目录Ids
     */
    public long[] categoryIds;
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static CategoryTag deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static CategoryTag deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            CategoryTag result = new CategoryTag();

            // 目录标签名称

            if(!json.isNull("name")){
                result.name = json.optString("name", null);
            }
            // 目录Ids
            JSONArray categoryIdsArray = json.optJSONArray("categoryIds");
            if (categoryIdsArray != null) {
                int len = categoryIdsArray.length();
                result.categoryIds = new long[len];
                for (int i = 0; i < len; i++) {
                    result.categoryIds[i] = categoryIdsArray.optLong(i);
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

        // 目录标签名称
        if(this.name != null) { json.put("name", this.name); }

        // 目录Ids 
        if (this.categoryIds != null) {
            JSONArray categoryIdsArray = new JSONArray();
            for (long value : this.categoryIds)
            {
                categoryIdsArray.put(value);
            }
            json.put("categoryIds", categoryIdsArray);
        }

        return json;
    }
}
  