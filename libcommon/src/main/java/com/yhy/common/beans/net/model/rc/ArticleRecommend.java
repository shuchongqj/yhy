package com.yhy.common.beans.net.model.rc;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:ArticleRecommend
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-7-28
 * Time:14:33
 * Version 1.0
 * Description:
 */
public class ArticleRecommend implements Serializable{
    private static final long serialVersionUID = -6652838781205846461L;

    /**
     * 文章id
     */
    public long id;

    /**
     * 文章标题
     */
    public String title;

    /**
     * 文章url
     */
    public String articleUrl;

    /**
     * 文章评论数
     */
    public long supportNum;

    /**
     * 文章头像
     */
    public String articlePic;

    /**
     * 文章副标题
     */
    public String subTitle;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static ArticleRecommend deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static ArticleRecommend deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            ArticleRecommend result = new ArticleRecommend();

            // 文章id
            result.id = json.optLong("id");
            // 文章标题

            if(!json.isNull("title")){
                result.title = json.optString("title", null);
            }

            if(!json.isNull("articlePic")){
                result.articlePic = json.optString("articlePic", null);
            }
            // 文章副标题

            if(!json.isNull("subTitle")){
                result.subTitle = json.optString("subTitle", null);
            }

            // 文章url

            if(!json.isNull("articleUrl")){
                result.articleUrl = json.optString("articleUrl", null);
            }
            // 文章评论数
            result.supportNum = json.optLong("supportNum");
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 文章id
        json.put("id", this.id);

        // 文章标题
        if(this.title != null) { json.put("title", this.title); }

        // 文章头像
        if(this.articlePic != null) { json.put("articlePic", this.articlePic); }

        // 文章副标题
        if(this.subTitle != null) { json.put("subTitle", this.subTitle); }

        // 文章url
        if(this.articleUrl != null) { json.put("articleUrl", this.articleUrl); }

        // 文章评论数
        json.put("supportNum", this.supportNum);

        return json;
    }
}
