package com.yhy.common.beans.net.model.user;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:FollowerPageListResult
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-6-28
 * Time:18:13
 * Version 1.0
 * Description:
 */

public class FollowerPageListResult implements Serializable {

    private static final long serialVersionUID = 999449915682917113L;
    /**
     * 当前页码
     */
    public int pageNo;

    /**
     * 是否有下一页
     */
    public boolean hasNext;

    /**
     * 关注列表信息
     */
    public List<FollowerInfo> followList;
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static FollowerPageListResult deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static FollowerPageListResult deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            FollowerPageListResult result = new FollowerPageListResult();

            // 当前页码
            result.pageNo = json.optInt("pageNo");
            // 是否有下一页
            result.hasNext = json.optBoolean("hasNext");
            // 关注列表信息
            JSONArray followListArray = json.optJSONArray("followUserList");
            if (followListArray != null) {
                int len = followListArray.length();
                result.followList = new ArrayList<FollowerInfo>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = followListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.followList.add(FollowerInfo.deserialize(jo));
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

        // 关注列表信息
        if (this.followList != null) {
            JSONArray followListArray = new JSONArray();
            for (FollowerInfo value : this.followList)
            {
                if (value != null) {
                    followListArray.put(value.serialize());
                }
            }
            json.put("followList", followListArray);
        }

        return json;
    }
}
