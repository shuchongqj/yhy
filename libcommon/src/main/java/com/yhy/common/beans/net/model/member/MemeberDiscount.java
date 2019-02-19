// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.member;
    
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class MemeberDiscount implements Serializable{

    private static final long serialVersionUID = -8322353333934645853L;
    /**
     * 商品id
     */
    public long itemId;
      
    /**
     * 商品图片
     */
    public String itemPic;
      
    /**
     * 商品标题
     */
    public String itemTitle;
      
    /**
     * 购买时间
     */
    public long buyTime;
      
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static MemeberDiscount deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static MemeberDiscount deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            MemeberDiscount result = new MemeberDiscount();
            
            // 商品id
            result.itemId = json.optLong("itemId");
            // 商品图片
            
              if(!json.isNull("itemPic")){
                  result.itemPic = json.optString("itemPic", null);
              }
            // 商品标题
            
              if(!json.isNull("itemTitle")){
                  result.itemTitle = json.optString("itemTitle", null);
              }
            // 购买时间
            result.buyTime = json.optLong("buyTime");
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
        if(this.itemPic != null) { json.put("itemPic", this.itemPic); }
          
        // 商品标题
        if(this.itemTitle != null) { json.put("itemTitle", this.itemTitle); }
          
        // 购买时间
        json.put("buyTime", this.buyTime);
          
        return json;
    }
}
  