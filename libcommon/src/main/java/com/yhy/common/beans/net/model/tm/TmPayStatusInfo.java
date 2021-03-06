// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.tm;
    
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class TmPayStatusInfo implements Serializable{

    private static final long serialVersionUID = 7310819981947941693L;
    /**
     * 支付状态,成功:10000
     */
    public int payStatus;
      
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static TmPayStatusInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static TmPayStatusInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            TmPayStatusInfo result = new TmPayStatusInfo();
            
            // 支付状态,成功:10000
            result.payStatus = json.optInt("payStatus");
            return result;
        }
        return null;
    }
    
    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();
        
        // 支付状态,成功:10000
        json.put("payStatus", this.payStatus);
          
        return json;
    }
}
  