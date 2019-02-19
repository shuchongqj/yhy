package com.yhy.common.beans.net.model.point;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:PointsDetail
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-6-30
 * Time:13:46
 * Version 1.0
 * Description:
 */
public class PointsDetail implements Serializable{

    /**
     * 明细标题
     */
    public String title;

    /**
     * 积分数量
     */
    public int amount;

    /**
     * 创建日期 显示格式 yyyyMMddHHmmss
     */
    public long created;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static PointsDetail deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static PointsDetail deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            PointsDetail result = new PointsDetail();

            // 明细标题

            if(!json.isNull("title")){
                result.title = json.optString("title", null);
            }
            // 积分数量
            result.amount = json.optInt("amount");
            // 创建日期 显示格式 yyyyMMddHHmmss
            result.created = json.optLong("created");
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 明细标题
        if(this.title != null) { json.put("title", this.title); }

        // 积分数量
        json.put("amount", this.amount);

        // 创建日期 显示格式 yyyyMMddHHmmss
        json.put("created", this.created);

        return json;
    }

}
