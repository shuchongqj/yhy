// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.trip;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class QueryHotelDTO implements Serializable {

    private static final long serialVersionUID = 1497517370836588290L;
    /**
     * 关键字
     */
    public String keyword;

    /**
     * 区域code
     */
    public long regionCode;

    /**
     * 酒店星级 TWO_STAR:经济型/两星及以下 FIVE_STAR:豪华型/五星级 FOUR_STAR:高档型/四星级 THREE_STAR:舒适型/三星级
     */
    public String level;

    /**
     * 排序类型 DEFAULT:默认排序 PRICE_ASC:价格升序 PRICE_DESC:价格降序 GRADE_ASC:评分升序 GRADE_DESC评分降序 SALES_ASC销量升序 SALES_DESC销量降序
     */
    public String orderType;

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
    public static QueryHotelDTO deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static QueryHotelDTO deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            QueryHotelDTO result = new QueryHotelDTO();

            // 关键字

            if(!json.isNull("keyword")){
                result.keyword = json.optString("keyword", null);
            }
            // 区域code
            result.regionCode = json.optLong("regionCode");
            // 酒店星级 TWO_STAR:经济型/两星及以下 FIVE_STAR:豪华型/五星级 FOUR_STAR:高档型/四星级 THREE_STAR:舒适型/三星级

            if(!json.isNull("level")){
                result.level = json.optString("level", null);
            }
            // 排序类型 DEFAULT:默认排序 PRICE_ASC:价格升序 PRICE_DESC:价格降序 GRADE_ASC:评分升序 GRADE_DESC评分降序 SALES_ASC销量升序 SALES_DESC销量降序

            if(!json.isNull("orderType")){
                result.orderType = json.optString("orderType", null);
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

        // 关键字
        if(this.keyword != null) { json.put("keyword", this.keyword); }

        // 区域code
        json.put("regionCode", this.regionCode);

        // 酒店星级 TWO_STAR:经济型/两星及以下 FIVE_STAR:豪华型/五星级 FOUR_STAR:高档型/四星级 THREE_STAR:舒适型/三星级
        if(this.level != null) { json.put("level", this.level); }

        // 排序类型 DEFAULT:默认排序 PRICE_ASC:价格升序 PRICE_DESC:价格降序 GRADE_ASC:评分升序 GRADE_DESC评分降序 SALES_ASC销量升序 SALES_DESC销量降序
        if(this.orderType != null) { json.put("orderType", this.orderType); }

        // 页号
        json.put("pageNo", this.pageNo);

        // 页面大小
        json.put("pageSize", this.pageSize);

        return json;
    }

}
  