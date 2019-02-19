package com.yhy.common.beans.net.model.discover;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:TopicDetailResult
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-6-29
 * Time:11:02
 * Version 1.0
 * Description:
 */
public class TopicDetailResult implements Serializable{

    private static final long serialVersionUID = -2412025546264234050L;
    /**
     * 当前页码
     */
    public int pageNo;

    /**
     * 是否有下一页
     */
    public boolean hasNext;

    /**
     * UGC信息列表
     */
    public List<UgcInfoResult> ugcInfoList;
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static TopicDetailResult deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static TopicDetailResult deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            TopicDetailResult result = new TopicDetailResult();

            // 当前页码
            result.pageNo = json.optInt("pageNo");
            // 是否有下一页
            result.hasNext = json.optBoolean("hasNext");
            // UGC信息列表
            JSONArray ugcInfoListArray = json.optJSONArray("ugcInfoList");
            if (ugcInfoListArray != null) {
                int len = ugcInfoListArray.length();
                result.ugcInfoList = new ArrayList<UgcInfoResult>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = ugcInfoListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.ugcInfoList.add(UgcInfoResult.deserialize(jo));
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

        // UGC信息列表
        if (this.ugcInfoList != null) {
            JSONArray ugcInfoListArray = new JSONArray();
            for (UgcInfoResult value : this.ugcInfoList)
            {
                if (value != null) {
                    ugcInfoListArray.put(value.serialize());
                }
            }
            json.put("ugcInfoList", ugcInfoListArray);
        }

        return json;
    }

}
