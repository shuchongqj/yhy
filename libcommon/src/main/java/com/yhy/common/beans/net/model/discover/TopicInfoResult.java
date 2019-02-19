package com.yhy.common.beans.net.model.discover;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:TopicInfoResult
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-6-28
 * Time:18:38
 * Version 1.0
 * Description:
 */
public class TopicInfoResult implements Serializable{

    private static final long serialVersionUID = -8901163145809627155L;
    /**
     * 话题名称
     */
    public String title;

    /**
     * 话题id
     */
    public long topicId;

    /**
     * 话题内容
     */
    public String content;

    /**
     * 话题图片
     */
    public String pics;

    /**
     * 阅读数量
     */
    public long redNum;

    /**
     * 参与数量
     */
    public long talkNum;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static TopicInfoResult deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static TopicInfoResult deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            TopicInfoResult result = new TopicInfoResult();

            // 话题名称

            if(!json.isNull("title")){
                result.title = json.optString("title", null);
            }
            // 话题id
            result.topicId = json.optLong("topicId");
            // 话题内容

            if(!json.isNull("content")){
                result.content = json.optString("content", null);
            }
            // 话题图片

            if(!json.isNull("pics")){
                result.pics = json.optString("pics", null);
            }
            // 阅读数量
            result.redNum = json.optLong("redNum");
            // 参与数量
            result.talkNum = json.optLong("talkNum");
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 话题名称
        if(this.title != null) { json.put("title", this.title); }

        // 话题id
        json.put("topicId", this.topicId);

        // 话题内容
        if(this.content != null) { json.put("content", this.content); }

        // 话题图片
        if(this.pics != null) { json.put("pics", this.pics); }

        // 阅读数量
        json.put("redNum", this.redNum);

        // 参与数量
        json.put("talkNum", this.talkNum);

        return json;
    }

}
