// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.master;


import com.yhy.common.beans.net.model.club.PageInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class MerchantQuery implements Serializable{

    private static final long serialVersionUID = 4279509032945519682L;
    /**
     * 店铺类型
     */
    public String merchantType;

    /**
     * 分页查询
     */
    public PageInfo pageInfo;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static MerchantQuery deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static MerchantQuery deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            MerchantQuery result = new MerchantQuery();

            // 店铺类型

            if(!json.isNull("merchantType")){
                result.merchantType = json.optString("merchantType", null);
            }
            // 分页查询
            result.pageInfo = PageInfo.deserialize(json.optJSONObject("pageInfo"));
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 店铺类型
        if(this.merchantType != null) { json.put("merchantType", this.merchantType); }

        // 分页查询
        if (this.pageInfo != null) { json.put("pageInfo", this.pageInfo.serialize()); }

        return json;
    }

}
  