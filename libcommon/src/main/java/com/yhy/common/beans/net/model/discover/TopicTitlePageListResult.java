package com.yhy.common.beans.net.model.discover;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:TopicTitlePageListResult
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-6-28
 * Time:19:08
 * Version 1.0
 * Description:
 */
public class TopicTitlePageListResult implements Serializable{

    private static final long serialVersionUID = -5128714181394310974L;
    /**
     * 当前页码
     */
    public int pageNo;

    /**
     * 是否有下一页
     */
    public boolean hasNext;

    /**
     * 话题标题列表
     */
    public List<String> titleList;
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static TopicTitlePageListResult deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static TopicTitlePageListResult deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            TopicTitlePageListResult result = new TopicTitlePageListResult();

            // 当前页码
            result.pageNo = json.optInt("pageNo");
            // 是否有下一页
            result.hasNext = json.optBoolean("hasNext");
            // 话题标题列表
            JSONArray titleListArray = json.optJSONArray("titleList");
            if (titleListArray != null) {
                int len = titleListArray.length();
                result.titleList = new
                        ArrayList<String>(len);
                for (int i = 0; i < len; i++) {

                    if(!titleListArray.isNull(i)){
                        result.titleList.add(titleListArray.optString(i, null));
                    }else{
                        result.titleList.add(i, null);
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

        // 话题标题列表
        if (this.titleList != null) {
            JSONArray titleListArray = new JSONArray();
            for (String value : this.titleList)
            {
                titleListArray.put(value);
            }
            json.put("titleList", titleListArray);
        }

        return json;
    }

}
