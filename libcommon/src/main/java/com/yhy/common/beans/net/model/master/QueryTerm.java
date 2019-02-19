// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.master;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class QueryTerm implements Serializable {

    private static final long serialVersionUID = -1796133853353121921L;
    /**
     * 条件类型 SUBJECT:主题/START_CITY:出发地/DEST_CITY:目的地/ITEM_TYPE:商品类型/DAYS:天数/TITLE:标题名称/SELLER_TYPE:卖家类型/SELLER_ID:卖家ID/SORT_TYPE:排序方式/START_DATE:开始时间/END_DATE:结束时间/MIN_PRICE:最低价格/MAX_PRICE:最高价格/HOTEL_TYPE:酒店类型/STAR_LEVEL:星级/DISTANCE:距离/TRADEAREA:商圈/CANTON:行政区/INTELLIGENT_SORT:智能排序
     */
    public String type;

    /**
     * 条件级别
     */
    public String level;

    /**
     * 条件类型
     */
    public boolean hasChild;

    /**
     * 条件文本
     */
    public String text;

    /**
     * 条件值
     */
    public String value;

    public List<QueryTerm> queryTermList;

    public boolean isSelected = false;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static QueryTerm deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static QueryTerm deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            QueryTerm result = new QueryTerm();

            // 条件类型 SUBJECT:主题/START_CITY:出发地/DEST_CITY:目的地/ITEM_TYPE:商品类型/DAYS:天数/TITLE:标题名称/SELLER_TYPE:卖家类型/SELLER_ID:卖家ID/SORT_TYPE:排序方式/

            if (!json.isNull("type")) {
                result.type = json.optString("type", null);
            }
            // 条件级别

            if (!json.isNull("level")) {
                result.level = json.optString("level", null);
            }
            // 条件类型
            result.hasChild = json.optBoolean("hasChild");
            // 条件文本

            if (!json.isNull("text")) {
                result.text = json.optString("text", null);
            }
            // 条件值

            if (!json.isNull("value")) {
                result.value = json.optString("value", null);
            }

            JSONArray queryTermListArray = json.optJSONArray("queryTermList");
            if(queryTermListArray != null) {
                int len = queryTermListArray.length();
                result.queryTermList = new ArrayList(len);

                for(int i = 0; i < len; ++i) {
                    JSONObject jo = queryTermListArray.optJSONObject(i);
                    if(jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.queryTermList.add(deserialize(jo));
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

        // 条件类型 SUBJECT:主题/START_CITY:出发地/DEST_CITY:目的地/ITEM_TYPE:商品类型/DAYS:天数/TITLE:标题名称/SELLER_TYPE:卖家类型/SELLER_ID:卖家ID/SORT_TYPE:排序方式/
        if (this.type != null) {
            json.put("type", this.type);
        }

        // 条件级别
        if (this.level != null) {
            json.put("level", this.level);
        }

        // 条件类型
        json.put("hasChild", this.hasChild);

        // 条件文本
        if (this.text != null) {
            json.put("text", this.text);
        }

        // 条件值
        if (this.value != null) {
            json.put("value", this.value);
        }
        // 查询条件
        if (this.queryTermList != null) {
            JSONArray valueArray = new JSONArray();
            for (QueryTerm value : this.queryTermList)
            {
                if (value != null) {
                    valueArray.put(value.serialize());
                }
            }
            json.put("queryTermList", valueArray);
        }
        return json;
    }
}
  