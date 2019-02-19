// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.trip;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ItemVOResult implements Serializable{

    private static final long serialVersionUID = 5006805523523613864L;
    /**
     * 是否分页
     */
    public boolean isPage;
      
    /**
     * 记录总数
     */
    public int recordCount;
      
    /**
     * 返回记录数
     */
    public int recordSize;
      
    /**
     * 当前页号
     */
    public int pageNo;
      
    /**
     * 页面大小
     */
    public int pageSize;
      
    /**
     * 商品列表
     */
    public List<ItemVO> list;
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static ItemVOResult deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static ItemVOResult deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            ItemVOResult result = new ItemVOResult();
            
            // 是否分页
            result.isPage = json.optBoolean("isPage");
            // 记录总数
            result.recordCount = json.optInt("recordCount");
            // 返回记录数
            result.recordSize = json.optInt("recordSize");
            // 当前页号
            result.pageNo = json.optInt("pageNo");
            // 页面大小
            result.pageSize = json.optInt("pageSize");
            // 商品列表
            JSONArray listArray = json.optJSONArray("list");
            if (listArray != null) {
                int len = listArray.length();
                result.list = new ArrayList<ItemVO>(len);
                for (int i = 0; i < len; i++) {
                        JSONObject jo = listArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.list.add(ItemVO.deserialize(jo));
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
        
        // 是否分页
        json.put("isPage", this.isPage);
          
        // 记录总数
        json.put("recordCount", this.recordCount);
          
        // 返回记录数
        json.put("recordSize", this.recordSize);
          
        // 当前页号
        json.put("pageNo", this.pageNo);
          
        // 页面大小
        json.put("pageSize", this.pageSize);
          
        // 商品列表 
        if (this.list != null) {
            JSONArray listArray = new JSONArray();
            for (ItemVO value : this.list)
            {
                if (value != null) {
                    listArray.put(value.serialize());
                }
            }
            json.put("list", listArray);
        }
      
        return json;
    }
}
  