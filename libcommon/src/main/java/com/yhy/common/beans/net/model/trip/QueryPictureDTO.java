// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.trip;
    
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class QueryPictureDTO implements Serializable{

    private static final long serialVersionUID = 3667700007502827777L;
    /**
     * 外部id
     */
    public long outId;
      
    /**
     * 外部类型 ITEM:商品/ITEM_SKU:商品SKU/HOTEL:酒店/SCENIC:景区/LINE：线路
     */
    public String outType;
      
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
    public static QueryPictureDTO deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static QueryPictureDTO deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            QueryPictureDTO result = new QueryPictureDTO();
            
            // 外部id
            result.outId = json.optLong("outId");
            // 外部类型 ITEM:商品/ITEM_SKU:商品SKU/HOTEL:酒店/SCENIC:景区/LINE：线路
            
              if(!json.isNull("outType")){
                  result.outType = json.optString("outType", null);
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
        
        // 外部id
        json.put("outId", this.outId);
          
        // 外部类型 ITEM:商品/ITEM_SKU:商品SKU/HOTEL:酒店/SCENIC:景区/LINE：线路
        if(this.outType != null) { json.put("outType", this.outType); }
          
        // 页号
        json.put("pageNo", this.pageNo);
          
        // 页面大小
        json.put("pageSize", this.pageSize);
          
        return json;
    }
}
  