// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.tm;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OrderPriceQueryDTO implements Serializable{

    private static final long serialVersionUID = 7430019520418122524L;
    /**
     * 所选商品sku列表
     */
    public List<BaseItemDTO> baseItemDTOList;
    /**
     * 所选的店铺优惠id 所选优惠id null:使用默认优惠 0:不使用优惠 其他：使用该Id优惠
     */
    public long selectedPromotionId;
      
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static OrderPriceQueryDTO deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static OrderPriceQueryDTO deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            OrderPriceQueryDTO result = new OrderPriceQueryDTO();
            
            // 所选商品sku列表
            JSONArray baseItemDTOListArray = json.optJSONArray("baseItemDTOList");
            if (baseItemDTOListArray != null) {
                int len = baseItemDTOListArray.length();
                result.baseItemDTOList = new ArrayList<BaseItemDTO>(len);
                for (int i = 0; i < len; i++) {
                        JSONObject jo = baseItemDTOListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.baseItemDTOList.add(BaseItemDTO.deserialize(jo));
                    }
                }
            }
      
            // 所选的店铺优惠id 所选优惠id null:使用默认优惠 0:不使用优惠 其他：使用该Id优惠
            result.selectedPromotionId = json.optLong("selectedPromotionId");
            return result;
        }
        return null;
    }
    
    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();
        
        // 所选商品sku列表 
        if (this.baseItemDTOList != null) {
            JSONArray baseItemDTOListArray = new JSONArray();
            for (BaseItemDTO value : this.baseItemDTOList)
            {
                if (value != null) {
                    baseItemDTOListArray.put(value.serialize());
                }
            }
            json.put("baseItemDTOList", baseItemDTOListArray);
        }
      
        // 所选的店铺优惠id 所选优惠id null:使用默认优惠 0:不使用优惠 其他：使用该Id优惠
        json.put("selectedPromotionId", this.selectedPromotionId);
          
        return json;
    }
}
  