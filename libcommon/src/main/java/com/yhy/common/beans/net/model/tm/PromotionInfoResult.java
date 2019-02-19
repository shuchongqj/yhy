package com.yhy.common.beans.net.model.tm;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:PromotionInfoResult
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-12-14
 * Time:10:20
 * Version 1.1.0
 */


public class PromotionInfoResult implements Serializable {
    private static final long serialVersionUID = 1261951991502012536L;
    /**
     * id
     */
    public long id;

    /**
     * 优惠标题
     */
    public String title;

    /**
     * 优惠描述
     */
    public String description;

    /**
     * 优惠实体类型 1-sku 2-item 3-shop
     */
    public int entityType;

    /**
     * 优惠实体id
     */
    public long entityId;

    /**
     * 优惠开始生效时间
     */
    public long startTime;

    /**
     * 优惠截止时间
     */
    public long endTime;

    /**
     * 优惠条件，如满100减20的100
     */
    public long requirement;

    /**
     * 优惠/折扣额度，金额单位分，折扣为1-99的数字，代表0.1折至99折
     */
    public long value;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static PromotionInfoResult deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static PromotionInfoResult deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            PromotionInfoResult result = new PromotionInfoResult();

            // id
            result.id = json.optLong("id");
            // 优惠标题

            if(!json.isNull("title")){
                result.title = json.optString("title", null);
            }
            // 优惠描述

            if(!json.isNull("description")){
                result.description = json.optString("description", null);
            }
            // 优惠实体类型 1-sku 2-item 3-shop
            result.entityType = json.optInt("entityType");
            // 优惠实体id
            result.entityId = json.optLong("entityId");
            // 优惠开始生效时间
            result.startTime = json.optLong("startTime");
            // 优惠截止时间
            result.endTime = json.optLong("endTime");
            // 优惠条件，如满100减20的100
            result.requirement = json.optLong("requirement");
            // 优惠/折扣额度，金额单位分，折扣为1-99的数字，代表0.1折至99折
            result.value = json.optLong("value");
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // id
        json.put("id", this.id);

        // 优惠标题
        if(this.title != null) { json.put("title", this.title); }

        // 优惠描述
        if(this.description != null) { json.put("description", this.description); }

        // 优惠实体类型 1-sku 2-item 3-shop
        json.put("entityType", this.entityType);

        // 优惠实体id
        json.put("entityId", this.entityId);

        // 优惠开始生效时间
        json.put("startTime", this.startTime);

        // 优惠截止时间
        json.put("endTime", this.endTime);

        // 优惠条件，如满100减20的100
        json.put("requirement", this.requirement);

        // 优惠/折扣额度，金额单位分，折扣为1-99的数字，代表0.1折至99折
        json.put("value", this.value);

        return json;
    }
}
