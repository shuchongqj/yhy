package com.yhy.common.beans.net.model.guide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:GuideAttractionInfo
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-8-22
 * Time:11:15
 * Version 1.1.0
 */

public class GuideAttractionInfo implements Serializable {
    private static final long serialVersionUID = -6731372239405347423L;

    /**
     * 景点id
     */
    public long id;

    /**
     * 导览id
     */
    public long guideId;

    /**
     * 景点图片
     */
    public String img;

    /**
     * 景点名称
     */
    public String name;

    /**
     * 游览时间
     */
    public String tourTime;

    /**
     * 标题
     */
    public String title;

    /**
     * 副标题
     */
    public String subTitle;

    /**
     * 看点列表
     */
    public List<GuideFocusInfo> focusList;
    /**
     * 距离
     */
    public long distance;

    /**
     * 编号
     */
    public int no;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static GuideAttractionInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static GuideAttractionInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
        GuideAttractionInfo result = new GuideAttractionInfo();

            // 景点id
            result.id = json.optLong("id");
            // 导览id
            result.guideId = json.optLong("guideId");
            // 景点图片

            if(!json.isNull("img")){
                result.img = json.optString("img", null);
            }
            // 景点名称

            if(!json.isNull("name")){
                result.name = json.optString("name", null);
            }
            // 游览时间

            if(!json.isNull("tourTime")){
                result.tourTime = json.optString("tourTime", null);
            }
            // 标题

            if(!json.isNull("title")){
                result.title = json.optString("title", null);
            }
            // 副标题

            if(!json.isNull("subTitle")){
                result.subTitle = json.optString("subTitle", null);
            }
            // 看点列表
            JSONArray focusListArray = json.optJSONArray("focusList");
            if (focusListArray != null) {
                int len = focusListArray.length();
                result.focusList = new ArrayList<GuideFocusInfo>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = focusListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.focusList.add(GuideFocusInfo.deserialize(jo));
                    }
                }
            }

            // 距离
            result.distance = json.optLong("distance");
            // 编号
            result.no = json.optInt("no");
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 景点id
        json.put("id", this.id);

        // 导览id
        json.put("guideId", this.guideId);

        // 景点图片
        if(this.img != null) { json.put("img", this.img); }

        // 景点名称
        if(this.name != null) { json.put("name", this.name); }

        // 游览时间
        if(this.tourTime != null) { json.put("tourTime", this.tourTime); }

        // 标题
        if(this.title != null) { json.put("title", this.title); }

        // 副标题
        if(this.subTitle != null) { json.put("subTitle", this.subTitle); }

        // 看点列表
        if (this.focusList != null) {
            JSONArray focusListArray = new JSONArray();
            for (GuideFocusInfo value : this.focusList)
            {
                if (value != null) {
                    focusListArray.put(value.serialize());
                }
            }
            json.put("focusList", focusListArray);
        }

        // 距离
        json.put("distance", this.distance);

        // 编号
        json.put("no", this.no);

        return json;
    }
}
