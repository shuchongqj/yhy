package com.yhy.common.beans.net.model.discover;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:TopicInfoResultList
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-6-29
 * Time:11:06
 * Version 1.0
 * Description:
 */
public class TopicInfoResultList implements Serializable{

    private static final long serialVersionUID = -483201675708579416L;
    /**
     * 当前页码
     */
    public int pageNo;

    /**
     * 是否有下一页
     */
    public boolean hasNext;

    /**
     * 话题信息
     */
    public List<TopicInfoResult> topicInfoResultList;
    /**
     * 1.推荐 2.全部
     */
    public int type;

    /**
     * 页码
     */
    public long startNum;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static TopicInfoResultList deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static TopicInfoResultList deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            TopicInfoResultList result = new TopicInfoResultList();

            // 当前页码
            result.pageNo = json.optInt("pageNo");
            // 是否有下一页
            result.hasNext = json.optBoolean("hasNext");
            // 话题信息
            JSONArray topicInfoResultListArray = json.optJSONArray("topicInfoResultList");
            if (topicInfoResultListArray != null) {
                int len = topicInfoResultListArray.length();
                result.topicInfoResultList = new ArrayList<TopicInfoResult>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = topicInfoResultListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.topicInfoResultList.add(TopicInfoResult.deserialize(jo));
                    }
                }
            }

            // 1.推荐 2.全部
            result.type = json.optInt("type");
            // 页码
            result.startNum = json.optLong("startNum");
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

        // 话题信息
        if (this.topicInfoResultList != null) {
            JSONArray topicInfoResultListArray = new JSONArray();
            for (TopicInfoResult value : this.topicInfoResultList)
            {
                if (value != null) {
                    topicInfoResultListArray.put(value.serialize());
                }
            }
            json.put("topicInfoResultList", topicInfoResultListArray);
        }

        // 1.推荐 2.全部
        json.put("type", this.type);

        // 页码
        json.put("startNum", this.startNum);

        return json;
    }

}
