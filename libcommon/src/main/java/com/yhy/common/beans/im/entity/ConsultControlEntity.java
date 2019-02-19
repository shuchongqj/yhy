package com.yhy.common.beans.im.entity;

import com.yhy.common.constants.ConsultContants;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created with Android Studio.
 * Title:ConsultControlEntity
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:邓明佳
 * Date:2016/7/25
 * Time:14:21
 * Version 1.0
 */
public class ConsultControlEntity {
    long buyerId;
    long itemId;
    long processOrderId;
    long sellerId;
    String tags;

    public static ConsultControlEntity parseFromJson(String jsonString) {
        ConsultControlEntity entity = new ConsultControlEntity();
        try {
            JSONObject object = new JSONObject(jsonString);
            JSONObject contentObject = object.getJSONObject(ConsultContants.CONTENT);
            entity.buyerId = contentObject.optLong(ConsultContants.BUYERID);
            entity.itemId = contentObject.optLong(ConsultContants.ITEMID);
            entity.processOrderId = contentObject.optLong(ConsultContants.PROCESSORDERID);
            entity.sellerId = contentObject.optLong(ConsultContants.SELLERID);
            entity.tags = object.optString(ConsultContants.TAGS);
        } catch (JSONException e) {
            return null;
        }
        return entity;
    }

    public long getBuyerId() {
        return buyerId;
    }

    public long getItemId() {
        return itemId;
    }

    public long getProcessOrderId() {
        return processOrderId;
    }

    public long getSellerId() {
        return sellerId;
    }

    public String getTags() {
        return tags;
    }
}
