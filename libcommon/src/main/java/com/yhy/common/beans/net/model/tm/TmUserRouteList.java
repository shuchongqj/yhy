// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.tm;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TmUserRouteList implements Serializable{

    private static final long serialVersionUID = 8239067863051620726L;
    /**
     * 查询的结果
     */
    public List<TmUserRoute> list;
    /**
     * 结果数目
     */
    public long totalCount;

    /**
     * 起始页
     */
    public int pageIndex;

    /**
     * 每页数据量
     */
    public int pageSize;

    /**
     * 是否有下一页
     */
    public boolean hasNext;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static TmUserRouteList deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static TmUserRouteList deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            TmUserRouteList result = new TmUserRouteList();

            // 查询的结果
            JSONArray listArray = json.optJSONArray("list");
            if (listArray != null) {
                int len = listArray.length();
                result.list = new ArrayList<TmUserRoute>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = listArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.list.add(TmUserRoute.deserialize(jo));
                    }
                }
            }

            // 结果数目
            result.totalCount = json.optLong("totalCount");
            // 起始页
            result.pageIndex = json.optInt("pageIndex");
            // 每页数据量
            result.pageSize = json.optInt("pageSize");
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

        // 查询的结果 
        if (this.list != null) {
            JSONArray listArray = new JSONArray();
            for (TmUserRoute value : this.list)
            {
                if (value != null) {
                    listArray.put(value.serialize());
                }
            }
            json.put("list", listArray);
        }

        // 结果数目
        json.put("totalCount", this.totalCount);

        // 起始页
        json.put("pageIndex", this.pageIndex);

        // 每页数据量
        json.put("pageSize", this.pageSize);

        // 是否有下一页
        json.put("hasNext", this.hasNext);

        return json;
    }
}
  