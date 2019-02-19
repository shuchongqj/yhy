// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.trip;
    
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class QueryLineDTO implements Serializable{

    private static final long serialVersionUID = 8983017954452727806L;
    /**
     * 主题id
     */
    public long subjectId;

    /**
     * 出发地code
     */
    public long startCode;

    /**
     * 目的地code
     */
    public long destCode;

    /**
     * 线路是否大咖 DEFAULT:精彩线路 MASTER:大咖线路 必填
     */
    public String lineOwnerType;

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
    public static QueryLineDTO deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static QueryLineDTO deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            QueryLineDTO result = new QueryLineDTO();

            // 主题id
            result.subjectId = json.optLong("subjectId");
            // 出发地code
            result.startCode = json.optLong("startCode");
            // 目的地code
            result.destCode = json.optLong("destCode");
            // 线路是否大咖 DEFAULT:精彩线路 MASTER:大咖线路 必填

            if(!json.isNull("lineOwnerType")){
                result.lineOwnerType = json.optString("lineOwnerType", null);
            }
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

        // 主题id
        json.put("subjectId", this.subjectId);

        // 出发地code
        json.put("startCode", this.startCode);

        // 目的地code
        json.put("destCode", this.destCode);

        // 线路是否大咖 DEFAULT:精彩线路 MASTER:大咖线路 必填
        if(this.lineOwnerType != null) { json.put("lineOwnerType", this.lineOwnerType); }

        // 页号
        json.put("pageNo", this.pageNo);

        // 页面大小
        json.put("pageSize", this.pageSize);

        return json;
    }

}
  