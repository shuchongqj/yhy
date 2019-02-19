package com.yhy.common.beans.net.model.trip;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:SearchResultList
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2015-11-24
 * Time:18:04
 * Version 1.0
 * Description:
 */
public class SearchResultList implements Serializable{

    private static final long serialVersionUID = -1683844217372835693L;
    /**
     * 是否分页
     */
    public boolean isPage;

    /**
     * 是否有下一页
     */
    public boolean hasNext;

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
     * 结果列表
     */
    public List<SearchResultVO> list;
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static SearchResultList deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static SearchResultList deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            SearchResultList result = new SearchResultList();

            // 是否分页
            result.isPage = json.optBoolean("isPage");
            // 是否有下一页
            result.hasNext = json.optBoolean("hasNext");
            // 记录总数
            result.recordCount = json.optInt("recordCount");
            // 返回记录数
            result.recordSize = json.optInt("recordSize");
            // 当前页号
            result.pageNo = json.optInt("pageNo");
            // 页面大小
            result.pageSize = json.optInt("pageSize");
            // 结果列表
            JSONArray listArray = json.optJSONArray("list");
            if (listArray != null) {
                int len = listArray.length();
                result.list = new ArrayList<SearchResultVO>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = listArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.list.add(SearchResultVO.deserialize(jo));
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

        // 是否有下一页
        json.put("hasNext", this.hasNext);

        // 记录总数
        json.put("recordCount", this.recordCount);

        // 返回记录数
        json.put("recordSize", this.recordSize);

        // 当前页号
        json.put("pageNo", this.pageNo);

        // 页面大小
        json.put("pageSize", this.pageSize);

        // 结果列表
        if (this.list != null) {
            JSONArray listArray = new JSONArray();
            for (SearchResultVO value : this.list)
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
