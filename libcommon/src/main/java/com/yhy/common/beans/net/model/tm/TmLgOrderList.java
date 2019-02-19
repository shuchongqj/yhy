// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.tm;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TmLgOrderList implements Serializable{

    private static final long serialVersionUID = 3541239900792233917L;
    /**
     * 物流订单列表
     */
    public List<TmLgOrder> lgOrders;
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static TmLgOrderList deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static TmLgOrderList deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            TmLgOrderList result = new TmLgOrderList();
            
            // 物流订单列表
            JSONArray lgOrdersArray = json.optJSONArray("lgOrders");
            if (lgOrdersArray != null) {
                int len = lgOrdersArray.length();
                result.lgOrders = new ArrayList<TmLgOrder>(len);
                for (int i = 0; i < len; i++) {
                        JSONObject jo = lgOrdersArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.lgOrders.add(TmLgOrder.deserialize(jo));
                    }
                }
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
        
        // 物流订单列表 
        if (this.lgOrders != null) {
            JSONArray lgOrdersArray = new JSONArray();
            for (TmLgOrder value : this.lgOrders)
            {
                if (value != null) {
                    lgOrdersArray.put(value.serialize());
                }
            }
            json.put("lgOrders", lgOrdersArray);
        }
      
        return json;
    }
}
  