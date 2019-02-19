// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.tm;
    
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class TmPayInfo implements Serializable{

    private static final long serialVersionUID = -7377622372052356939L;
    /**
     * 支付宝支付请求信息
     */
    public String payInfo;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static TmPayInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static TmPayInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            TmPayInfo result = new TmPayInfo();

            // 支付宝支付请求信息

            if(!json.isNull("payInfo")){
                result.payInfo = json.optString("payInfo", null);
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

        // 支付宝支付请求信息
        if(this.payInfo != null) { json.put("payInfo", this.payInfo); }

        return json;
    }
}
  