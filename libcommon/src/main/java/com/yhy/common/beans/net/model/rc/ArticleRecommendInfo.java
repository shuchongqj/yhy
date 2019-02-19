package com.yhy.common.beans.net.model.rc;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:ArticleRecommendInfo
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-7-28
 * Time:14:35
 * Version 1.0
 * Description:
 */
public class ArticleRecommendInfo implements Serializable{
    private static final long serialVersionUID = -691741799654276744L;

    /**
     * 推荐文章列表
     */
    public List<ArticleRecommend> articleRecommendList;
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static ArticleRecommendInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static ArticleRecommendInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            ArticleRecommendInfo result = new ArticleRecommendInfo();

            // 推荐文章列表
            JSONArray articleRecommendListArray = json.optJSONArray("articleRecommendList");
            if (articleRecommendListArray != null) {
                int len = articleRecommendListArray.length();
                result.articleRecommendList = new ArrayList<ArticleRecommend>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = articleRecommendListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.articleRecommendList.add(ArticleRecommend.deserialize(jo));
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

        // 推荐文章列表
        if (this.articleRecommendList != null) {
            JSONArray articleRecommendListArray = new JSONArray();
            for (ArticleRecommend value : this.articleRecommendList)
            {
                if (value != null) {
                    articleRecommendListArray.put(value.serialize());
                }
            }
            json.put("articleRecommendList", articleRecommendListArray);
        }

        return json;
    }
}
