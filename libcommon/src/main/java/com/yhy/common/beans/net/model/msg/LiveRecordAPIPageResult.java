package com.yhy.common.beans.net.model.msg;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:LiveRecordAPIPageResult
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-8-16
 * Time:11:54
 * Version 1.0
 * Description:
 */
public class LiveRecordAPIPageResult implements Serializable{
    private static final long serialVersionUID = 7253720090609101914L;

    /**
     * 第几页
     */
    public int pageNo;

    /**
     * 一页多少
     */
    public int pageSize;

    /**
     * 共多少条
     */
    public int totalCount;

    /**
     * 是否有下一页
     */
    public boolean hasNext;

    /**
     * 选项
     */
    public List<LiveRecordResult> list;
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static LiveRecordAPIPageResult deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static LiveRecordAPIPageResult deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            LiveRecordAPIPageResult result = new LiveRecordAPIPageResult();

            // 第几页
            result.pageNo = json.optInt("pageNo");
            // 一页多少
            result.pageSize = json.optInt("pageSize");
            // 共多少条
            result.totalCount = json.optInt("totalCount");
            // 是否有下一页
            result.hasNext = json.optBoolean("hasNext");
            // 选项
            JSONArray listArray = json.optJSONArray("list");
            if (listArray != null) {
                int len = listArray.length();
                result.list = new ArrayList<LiveRecordResult>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = listArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.list.add(LiveRecordResult.deserialize(jo));
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

        // 第几页
        json.put("pageNo", this.pageNo);

        // 一页多少
        json.put("pageSize", this.pageSize);

        // 共多少条
        json.put("totalCount", this.totalCount);

        // 是否有下一页
        json.put("hasNext", this.hasNext);

        // 选项
        if (this.list != null) {
            JSONArray listArray = new JSONArray();
            for (LiveRecordResult value : this.list)
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
