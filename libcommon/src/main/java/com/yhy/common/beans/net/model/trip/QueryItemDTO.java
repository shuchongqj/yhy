// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.trip;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class QueryItemDTO implements Serializable {

    private static final long serialVersionUID = -5376666125565684706L;
    /**
     * 类目id 当categoryIds不为空时有效
     */
    public long categoryId;

    /**
     * 类目id集合
     */
    public long[] categoryIds;
    /**
     * 商品类型
     */
    public List<String> itemTypes;
    /**
     * 卖家id
     */
    public long sellerId;

    /**
     * 标题
     */
    public String title;

    /**
     * 距离范围，仅大于0时根据经纬度筛选
     */
    public int distance;

    /**
     * 所在地经度
     */
    public double longitude;

    /**
     * 所在地纬度
     */
    public double latitude;

    /**
     * 出行天数下限
     */
    public int minDays;

    /**
     * 出行天数上限
     */
    public int maxDays;

    /**
     * 页号
     */
    public int pageNo;

    /**
     * 页面大小
     */
    public int pageSize;

    /**
     * 排序方式 PRICE_ASC:价格升序/PRICE_DESC:价格降序/SALES_ASC:销量升序/SALES_DESC:销量降序
     */
    public String sortType;

    /**
     * 标签id集合
     */
    public long[] tagIds;
    /**
     * 出发地code集合
     */
    public int[] departCityCodes;
    /**
     * 目的地code集合
     */
    public int[] destCityCodes;
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static QueryItemDTO deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static QueryItemDTO deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            QueryItemDTO result = new QueryItemDTO();

            // 类目id 当categoryIds不为空时有效
            result.categoryId = json.optLong("categoryId");
            // 类目id集合
            JSONArray categoryIdsArray = json.optJSONArray("categoryIds");
            if (categoryIdsArray != null) {
                int len = categoryIdsArray.length();
                result.categoryIds = new long[len];
                for (int i = 0; i < len; i++) {
                    result.categoryIds[i] = categoryIdsArray.optLong(i);
                }
            }

            // 商品类型
            JSONArray itemTypesArray = json.optJSONArray("itemTypes");
            if (itemTypesArray != null) {
                int len = itemTypesArray.length();
                result.itemTypes = new
                        ArrayList<String>(len);
                for (int i = 0; i < len; i++) {

                    if(!itemTypesArray.isNull(i)){
                        result.itemTypes.add(itemTypesArray.optString(i, null));
                    }else{
                        result.itemTypes.add(i, null);
                    }

                }
            }

            // 卖家id
            result.sellerId = json.optLong("sellerId");
            // 标题

            if(!json.isNull("title")){
                result.title = json.optString("title", null);
            }
            // 距离范围，仅大于0时根据经纬度筛选
            result.distance = json.optInt("distance");
            // 所在地经度
            result.longitude = json.optDouble("longitude");
            // 所在地纬度
            result.latitude = json.optDouble("latitude");
            // 出行天数下限
            result.minDays = json.optInt("minDays");
            // 出行天数上限
            result.maxDays = json.optInt("maxDays");
            // 页号
            result.pageNo = json.optInt("pageNo");
            // 页面大小
            result.pageSize = json.optInt("pageSize");
            // 排序方式 PRICE_ASC:价格升序/PRICE_DESC:价格降序/SALES_ASC:销量升序/SALES_DESC:销量降序

            if(!json.isNull("sortType")){
                result.sortType = json.optString("sortType", null);
            }
            // 标签id集合
            JSONArray tagIdsArray = json.optJSONArray("tagIds");
            if (tagIdsArray != null) {
                int len = tagIdsArray.length();
                result.tagIds = new long[len];
                for (int i = 0; i < len; i++) {
                    result.tagIds[i] = tagIdsArray.optLong(i);
                }
            }

            // 出发地code集合
            JSONArray departCityCodesArray = json.optJSONArray("departCityCodes");
            if (departCityCodesArray != null) {
                int len = departCityCodesArray.length();
                result.departCityCodes = new int[len];
                for (int i = 0; i < len; i++) {
                    result.departCityCodes[i] = departCityCodesArray.optInt(i);
                }
            }

            // 目的地code集合
            JSONArray destCityCodesArray = json.optJSONArray("destCityCodes");
            if (destCityCodesArray != null) {
                int len = destCityCodesArray.length();
                result.destCityCodes = new int[len];
                for (int i = 0; i < len; i++) {
                    result.destCityCodes[i] = destCityCodesArray.optInt(i);
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

        // 类目id 当categoryIds不为空时有效
        json.put("categoryId", this.categoryId);

        // 类目id集合 
        if (this.categoryIds != null) {
            JSONArray categoryIdsArray = new JSONArray();
            for (long value : this.categoryIds)
            {
                categoryIdsArray.put(value);
            }
            json.put("categoryIds", categoryIdsArray);
        }

        // 商品类型 
        if (this.itemTypes != null) {
            JSONArray itemTypesArray = new JSONArray();
            for (String value : this.itemTypes)
            {
                itemTypesArray.put(value);
            }
            json.put("itemTypes", itemTypesArray);
        }

        // 卖家id
        json.put("sellerId", this.sellerId);

        // 标题
        if(this.title != null) { json.put("title", this.title); }

        // 距离范围，仅大于0时根据经纬度筛选
        json.put("distance", this.distance);

        // 所在地经度
        json.put("longitude", this.longitude);

        // 所在地纬度
        json.put("latitude", this.latitude);

        // 出行天数下限
        json.put("minDays", this.minDays);

        // 出行天数上限
        json.put("maxDays", this.maxDays);

        // 页号
        json.put("pageNo", this.pageNo);

        // 页面大小
        json.put("pageSize", this.pageSize);

        // 排序方式 PRICE_ASC:价格升序/PRICE_DESC:价格降序/SALES_ASC:销量升序/SALES_DESC:销量降序
        if(this.sortType != null) { json.put("sortType", this.sortType); }

        // 标签id集合 
        if (this.tagIds != null) {
            JSONArray tagIdsArray = new JSONArray();
            for (long value : this.tagIds)
            {
                tagIdsArray.put(value);
            }
            json.put("tagIds", tagIdsArray);
        }

        // 出发地code集合 
        if (this.departCityCodes != null) {
            JSONArray departCityCodesArray = new JSONArray();
            for (int value : this.departCityCodes)
            {
                departCityCodesArray.put(value);
            }
            json.put("departCityCodes", departCityCodesArray);
        }

        // 目的地code集合 
        if (this.destCityCodes != null) {
            JSONArray destCityCodesArray = new JSONArray();
            for (int value : this.destCityCodes)
            {
                destCityCodesArray.put(value);
            }
            json.put("destCityCodes", destCityCodesArray);
        }

        return json;
    }


}
  