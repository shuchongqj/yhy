// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.comment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProductRateInfoList implements Serializable{

    private static final long serialVersionUID = 5016925574480471679L;
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
     * 实物商品评价信息列表
     */
    public List<ProductRateInfo> productRateInfoList;
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static ProductRateInfoList deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static ProductRateInfoList deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            ProductRateInfoList result = new ProductRateInfoList();
            
            // 当前页码
            result.pageNo = json.optInt("pageNo");
            // 是否有下一页
            result.hasNext = json.optBoolean("hasNext");
            // 评价数量
            result.RateNum = json.optInt("RateNum");
            // 实物商品评价信息列表
            JSONArray productRateInfoListArray = json.optJSONArray("productRateInfoList");
            if (productRateInfoListArray != null) {
                int len = productRateInfoListArray.length();
                result.productRateInfoList = new ArrayList<ProductRateInfo>(len);
                for (int i = 0; i < len; i++) {
                        JSONObject jo = productRateInfoListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.productRateInfoList.add(ProductRateInfo.deserialize(jo));
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
          
        // 实物商品评价信息列表 
        if (this.productRateInfoList != null) {
            JSONArray productRateInfoListArray = new JSONArray();
            for (ProductRateInfo value : this.productRateInfoList)
            {
                if (value != null) {
                    productRateInfoListArray.put(value.serialize());
                }
            }
            json.put("productRateInfoList", productRateInfoListArray);
        }
      
        return json;
    }
}
  