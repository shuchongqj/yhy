// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.member;
    
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class MemeberItem implements Serializable{

    private static final long serialVersionUID = -2854206232558527419L;
    /**
     * 商品id
     */
    public long itemId;
      
    /**
     * 商品图片
     */
    public String itemPics;
      
    /**
     * 商品标题
     */
    public String itemTitle;
      
    /**
     * 价格，单位是分
     */
    public long price;
      
    /**
     * 原价，单位是分
     */
    public long originalPrice;
      
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static MemeberItem deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static MemeberItem deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            MemeberItem result = new MemeberItem();
            
            // 商品id
            result.itemId = json.optLong("itemId");
            // 商品图片
            
              if(!json.isNull("itemPics")){
                  result.itemPics = json.optString("itemPics", null);
              }
            // 商品标题
            
              if(!json.isNull("itemTitle")){
                  result.itemTitle = json.optString("itemTitle", null);
              }
            // 价格，单位是分
            result.price = json.optLong("price");
            // 原价，单位是分
            result.originalPrice = json.optLong("originalPrice");
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
          
        // 商品图片
        if(this.itemPics != null) { json.put("itemPics", this.itemPics); }
          
        // 商品标题
        if(this.itemTitle != null) { json.put("itemTitle", this.itemTitle); }
          
        // 价格，单位是分
        json.put("price", this.price);
          
        // 原价，单位是分
        json.put("originalPrice", this.originalPrice);
          
        return json;
    }
}
  