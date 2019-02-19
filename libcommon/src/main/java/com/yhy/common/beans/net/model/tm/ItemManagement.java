package com.yhy.common.beans.net.model.tm;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:GoodsManagement
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-7-19
 * Time:14:28
 * Version 1.0
 * Description:
 */
public class ItemManagement implements Serializable{

    private static final long serialVersionUID = -5494370104335208570L;

    /**
     * 商品状态
     */
    public int state;

    /**
     * 商品id
     */
    public long itemId;

    /**
     * 已售数量
     */
    public int saleVolume;

    /**
     * 商品信息
     */
    public PublishServiceDO publishServiceDO;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static ItemManagement deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static ItemManagement deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            ItemManagement result = new ItemManagement();

            // 商品状态
            result.state = json.optInt("state");
            // 商品id
            result.itemId = json.optLong("itemId");
            // 已售数量
            result.saleVolume = json.optInt("saleVolume");
            // 商品信息
            result.publishServiceDO = PublishServiceDO.deserialize(json.optJSONObject("publishServiceDO"));
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 商品状态
        json.put("state", this.state);

        // 商品id
        json.put("itemId", this.itemId);

        // 已售数量
        json.put("saleVolume", this.saleVolume);

        // 商品信息
        if (this.publishServiceDO != null) { json.put("publishServiceDO", this.publishServiceDO.serialize()); }

        return json;
    }
}
