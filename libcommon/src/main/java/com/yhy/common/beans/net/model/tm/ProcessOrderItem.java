package com.yhy.common.beans.net.model.tm;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:ProcessOrderItem
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-7-20
 * Time:17:44
 * Version 1.0
 * Description:
 */
public class ProcessOrderItem implements Serializable{
    private static final long serialVersionUID = -6197093916941069210L;

    /**
     * 流程单信息
     */
    public TmBizOrder bizOrder;

    /**
     * 商品id
     */
    public long processOrderItemId;

    /**
     * 流程单id
     */
    public long processOrderId;

    /**
     * 流程单项标题
     */
    public String title;

    /**
     * 创建时间
     */
    public long createTime;

    /**
     * 完成时间
     */
    public long finishTime;

    /**
     * 状态
     */
    public String processStatus;

    /**
     * 流程单类型
     */
    public String processOrderItemType;

    /**
     * 商品id
     */
    public long itemId;

    /**
     * 商品子标题
     */
    public String itemSubTitle;

    /**
     * 商品名称
     */
    public String itemTitle;

    /**
     * 商品图片
     */
    public String itemPic;

    /**
     * 交易价格
     */
    public long itemPrice;


    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static ProcessOrderItem deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static ProcessOrderItem deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            ProcessOrderItem result = new ProcessOrderItem();

            // 流程单信息
            result.bizOrder = TmBizOrder.deserialize(json.optJSONObject("bizOrder"));
            // 商品id
            result.processOrderItemId = json.optLong("processOrderItemId");
            // 流程单id
            result.processOrderId = json.optLong("processOrderId");
            // 流程单项标题

            if(!json.isNull("title")){
                result.title = json.optString("title", null);
            }
            // 创建时间
            result.createTime = json.optLong("createTime");
            // 完成时间
            result.finishTime = json.optLong("finishTime");
            // 状态

            if(!json.isNull("processStatus")){
                result.processStatus = json.optString("processStatus", null);
            }
            // 流程单类型

            if(!json.isNull("processOrderItemType")){
                result.processOrderItemType = json.optString("processOrderItemType", null);
            }
            // 商品id
            result.itemId = json.optLong("itemId");
            // 商品名称

            if(!json.isNull("itemTitle")){
                result.itemTitle = json.optString("itemTitle", null);
            }
            // 商品子标题

            if(!json.isNull("itemSubTitle")){
                result.itemSubTitle = json.optString("itemSubTitle", null);
            }
            // 商品图片

            if(!json.isNull("itemPic")){
                result.itemPic = json.optString("itemPic", null);
            }

            // 交易价格
            result.itemPrice = json.optLong("itemPrice");
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 流程单信息
        if (this.bizOrder != null) { json.put("bizOrder", this.bizOrder.serialize()); }

        // 商品id
        json.put("processOrderItemId", this.processOrderItemId);

        // 流程单id
        json.put("processOrderId", this.processOrderId);

        // 流程单项标题
        if(this.title != null) { json.put("title", this.title); }

        // 创建时间
        json.put("createTime", this.createTime);

        // 完成时间
        json.put("finishTime", this.finishTime);

        // 状态
        if(this.processStatus != null) { json.put("processStatus", this.processStatus); }

        // 流程单类型
        if(this.processOrderItemType != null) { json.put("processOrderItemType", this.processOrderItemType); }

        // 商品id
        json.put("itemId", this.itemId);

        // 商品名称
        if(this.itemTitle != null) { json.put("itemTitle", this.itemTitle); }

        // 商品子标题
        if(this.itemSubTitle != null) { json.put("itemSubTitle", this.itemSubTitle); }

        // 商品图片
        if(this.itemPic != null) { json.put("itemPic", this.itemPic); }

        // 交易价格
        json.put("itemPrice", this.itemPrice);

        return json;
    }
}
