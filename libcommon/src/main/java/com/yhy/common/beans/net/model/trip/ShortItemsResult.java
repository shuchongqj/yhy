// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.trip;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ShortItemsResult implements Serializable {

    private static final long serialVersionUID = 1322565609757783241L;
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
    public List<ShortItem> shortItemList;
    /**
     * 是否有下一页
     */
    public boolean hasNext;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static ShortItemsResult deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static ShortItemsResult deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            ShortItemsResult result = new ShortItemsResult();

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
            JSONArray shortItemListArray = json.optJSONArray("shortItemList");
            if (shortItemListArray != null) {
                int len = shortItemListArray.length();
                result.shortItemList = new ArrayList<ShortItem>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = shortItemListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.shortItemList.add(ShortItem.deserialize(jo));
                    }
                }
            }
            // 是否有下一页
            result.hasNext = json.optBoolean("hasNext");
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
        if (this.shortItemList != null) {
            JSONArray shortItemListArray = new JSONArray();
            for (ShortItem value : this.shortItemList) {
                if (value != null) {
                    shortItemListArray.put(value.serialize());
                }
            }
            json.put("shortItemList", shortItemListArray);
        }
        // 是否有下一页
        json.put("hasNext", this.hasNext);

        return json;
    }
}
  