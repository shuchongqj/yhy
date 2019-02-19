// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.shop;


import com.yhy.common.beans.net.model.trip.ItemVO;
import com.yhy.common.beans.net.model.user.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class MerchantItem implements Serializable{

    private static final long serialVersionUID = 7599084361567400699L;
    /**
     * 商品详情
     */
    public ItemVO itemVO;
      
    /**
     * 卖家信息
     */
    public UserInfo userInfo;
      
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static MerchantItem deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static MerchantItem deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            MerchantItem result = new MerchantItem();
            
            // 商品详情
            result.itemVO = ItemVO.deserialize(json.optJSONObject("itemVO"));
            // 卖家信息
            result.userInfo = UserInfo.deserialize(json.optJSONObject("userInfo"));
            return result;
        }
        return null;
    }
    
    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();
        
        // 商品详情
        if (this.itemVO != null) { json.put("itemVO", this.itemVO.serialize()); }
          
        // 卖家信息
        if (this.userInfo != null) { json.put("userInfo", this.userInfo.serialize()); }
          
        return json;
    }
}
  