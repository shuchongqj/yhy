// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.trip;

import com.yhy.common.beans.net.model.base.BaseShrink;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TicketItemVO extends BaseShrink implements Serializable{

    private static final long serialVersionUID = -6211766970579273977L;
    /**
     * 票型实体
     */
    public TicketVO ticketVO;
      
    /**
     * 商品实体列表
     */
    public List<ItemVO> itemVOList;
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static TicketItemVO deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static TicketItemVO deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            TicketItemVO result = new TicketItemVO();
            
            // 票型实体
            result.ticketVO = TicketVO.deserialize(json.optJSONObject("ticketVO"));
            // 商品实体列表
            JSONArray itemVOListArray = json.optJSONArray("itemVOList");
            if (itemVOListArray != null) {
                int len = itemVOListArray.length();
                result.itemVOList = new ArrayList<ItemVO>(len);
                for (int i = 0; i < len; i++) {
                        JSONObject jo = itemVOListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.itemVOList.add(ItemVO.deserialize(jo));
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
        
        // 票型实体
        if (this.ticketVO != null) { json.put("ticketVO", this.ticketVO.serialize()); }
          
        // 商品实体列表 
        if (this.itemVOList != null) {
            JSONArray itemVOListArray = new JSONArray();
            for (ItemVO value : this.itemVOList)
            {
                if (value != null) {
                    itemVOListArray.put(value.serialize());
                }
            }
            json.put("itemVOList", itemVOListArray);
        }
      
        return json;
    }
}
  