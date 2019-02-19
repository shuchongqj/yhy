// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.item;
    
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class CodeQueryDTO implements Serializable{

    private static final long serialVersionUID = 7217544660432191319L;
    /**
     * 展位的码 首页商品推荐：QUANYAN_PAGE_FEATURE_RECOMMEND
     */
    public String boothCode;

    /**
     * 页号
     */
    public int pageNo;

    /**
     * 页面大小
     */
    public int pageSize;

    /**
     * 所在地经度
     */
    public double longitude;

    /**
     * 所在地纬度
     */
    public double latitude;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static CodeQueryDTO deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static CodeQueryDTO deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            CodeQueryDTO result = new CodeQueryDTO();

            // 展位的码 首页商品推荐：QUANYAN_PAGE_FEATURE_RECOMMEND

            if(!json.isNull("boothCode")){
                result.boothCode = json.optString("boothCode", null);
            }
            // 页号
            result.pageNo = json.optInt("pageNo");
            // 页面大小
            result.pageSize = json.optInt("pageSize");
            // 所在地经度
            result.longitude = json.optDouble("longitude");
            // 所在地纬度
            result.latitude = json.optDouble("latitude");
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 展位的码 首页商品推荐：QUANYAN_PAGE_FEATURE_RECOMMEND
        if(this.boothCode != null) { json.put("boothCode", this.boothCode); }

        // 页号
        json.put("pageNo", this.pageNo);

        // 页面大小
        json.put("pageSize", this.pageSize);

        // 所在地经度
        json.put("longitude", this.longitude);

        // 所在地纬度
        json.put("latitude", this.latitude);

        return json;
    }

}
  