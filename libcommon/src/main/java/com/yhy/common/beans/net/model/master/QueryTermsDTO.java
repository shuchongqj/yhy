// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.master;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class QueryTermsDTO implements Serializable{

    private static final long serialVersionUID = -2135915129192057555L;
    /**
     * 查询条件集合
     */
    public List<QueryTerm> queryTerms;
    /**
     * 所在地经度
     */
    public double longitude;
      
    /**
     * 所在地纬度
     */
    public double latitude;
      
    /**
     * 页号
     */
    public int pageNo;
      
    /**
     * 页面大小
     */
    public int pageSize;

    /**
     * // 展位的码 首页商品推荐：PAGE_FEATURE_RECOMMEND
     */
    public String boothCode;
      
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static QueryTermsDTO deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static QueryTermsDTO deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            QueryTermsDTO result = new QueryTermsDTO();
            
            // 查询条件集合
            JSONArray queryTermsArray = json.optJSONArray("queryTerms");
            if (queryTermsArray != null) {
                int len = queryTermsArray.length();
                result.queryTerms = new ArrayList<QueryTerm>(len);
                for (int i = 0; i < len; i++) {
                        JSONObject jo = queryTermsArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.queryTerms.add(QueryTerm.deserialize(jo));
                    }
                }
            }
      
            // 所在地经度
            result.longitude = json.optDouble("longitude");
            // 所在地纬度
            result.latitude = json.optDouble("latitude");
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
        
        // 查询条件集合 
        if (this.queryTerms != null) {
            JSONArray queryTermsArray = new JSONArray();
            for (QueryTerm value : this.queryTerms)
            {
                if (value != null) {
                    queryTermsArray.put(value.serialize());
                }
            }
            json.put("queryTerms", queryTermsArray);
        }
      
        // 所在地经度
        json.put("longitude", this.longitude);
          
        // 所在地纬度
        json.put("latitude", this.latitude);
          
        // 页号
        json.put("pageNo", this.pageNo);
          
        // 页面大小
        json.put("pageSize", this.pageSize);
          
        return json;
    }
}
  