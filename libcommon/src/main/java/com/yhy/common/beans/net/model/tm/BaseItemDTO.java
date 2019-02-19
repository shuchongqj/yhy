// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.tm;
    
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class BaseItemDTO implements Serializable{

    private static final long serialVersionUID = -750076301615481239L;
    /**
     * 商品id
     */
    public long itemId;
      
    /**
     * skuId
     */
    public long skuId;
      
    /**
     * 购买数量
     */
    public int buyAmount;
      
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static BaseItemDTO deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static BaseItemDTO deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            BaseItemDTO result = new BaseItemDTO();
            
            // 商品id
            result.itemId = json.optLong("itemId");
            // skuId
            result.skuId = json.optLong("skuId");
            // 购买数量
            result.buyAmount = json.optInt("buyAmount");
            return result;
        }
        return null;
    }
    
    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();
        
        // 商品id
        json.put("itemId", this.itemId);
          
        // skuId
        json.put("skuId", this.skuId);
          
        // 购买数量
        json.put("buyAmount", this.buyAmount);
          
        return json;
    }
}
  