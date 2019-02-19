// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.comment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RateInfoList implements Serializable{

    private static final long serialVersionUID = 9109098145326366126L;
    /**
     * 当前页码
     */
    public int pageNo;
      
    /**
     * 是否有下一页
     */
    public boolean hasNext;
      
    /**
     * 评价数量
     */
    public int RateNum;
      
    /**
     * 评价信息列表
     */
    public List<RateInfo> rateInfoList;
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static RateInfoList deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static RateInfoList deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            RateInfoList result = new RateInfoList();
            
            // 当前页码
            result.pageNo = json.optInt("pageNo");
            // 是否有下一页
            result.hasNext = json.optBoolean("hasNext");
            // 评价数量
            result.RateNum = json.optInt("RateNum");
            // 评价信息列表
            JSONArray rateInfoListArray = json.optJSONArray("rateInfoList");
            if (rateInfoListArray != null) {
                int len = rateInfoListArray.length();
                result.rateInfoList = new ArrayList<RateInfo>(len);
                for (int i = 0; i < len; i++) {
                        JSONObject jo = rateInfoListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.rateInfoList.add(RateInfo.deserialize(jo));
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
        
        // 当前页码
        json.put("pageNo", this.pageNo);
          
        // 是否有下一页
        json.put("hasNext", this.hasNext);
          
        // 评价数量
        json.put("RateNum", this.RateNum);
          
        // 评价信息列表 
        if (this.rateInfoList != null) {
            JSONArray rateInfoListArray = new JSONArray();
            for (RateInfo value : this.rateInfoList)
            {
                if (value != null) {
                    rateInfoListArray.put(value.serialize());
                }
            }
            json.put("rateInfoList", rateInfoListArray);
        }
      
        return json;
    }
}
  