package com.yhy.common.beans.net.model.tm;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:VoucherTemplateResult
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-6-28
 * Time:20:07
 * Version 1.0
 * Description:
 */
public class VoucherTemplateResult implements Serializable{

    private static final long serialVersionUID = -6678058794019302560L;
    /**
     * id
     */
    public long id;

    /**
     * 标题
     */
    public String title;

    /**
     * 券类型
     */
    public int voucherType;

    /**
     * 条件
     */
    public long requirement;

    /**
     * 值
     */
    public long value;

    /**
     * 状态
     */
    public String status;

    /**
     * 券码
     */
    public String voucherCode;

    /**
     * 使用有效期开始时间
     */
    public long startTime;

    /**
     * 使用有效期结束时间
     */
    public long endTime;

    /**
     * 店铺信息
     */
    public SellerResult sellerResult;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static VoucherTemplateResult deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static VoucherTemplateResult deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            VoucherTemplateResult result = new VoucherTemplateResult();

            // id
            result.id = json.optLong("id");
            // 标题

            if(!json.isNull("title")){
                result.title = json.optString("title", null);
            }
            // 券类型
            result.voucherType = json.optInt("voucherType");
            // 条件
            result.requirement = json.optLong("requirement");
            // 值
            result.value = json.optLong("value");
            // 状态
            if(!json.isNull("status")){
                result.status = json.optString("status", null);
            }
            // 券码

            if(!json.isNull("voucherCode")){
                result.voucherCode = json.optString("voucherCode", null);
            }
            // 使用有效期开始时间
            result.startTime = json.optLong("startTime");
            // 使用有效期结束时间
            result.endTime = json.optLong("endTime");
            // 店铺信息
            result.sellerResult = SellerResult.deserialize(json.optJSONObject("sellerResult"));
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

        // 标题
        if(this.title != null) { json.put("title", this.title); }

        // 券类型
        json.put("voucherType", this.voucherType);

        // 条件
        json.put("requirement", this.requirement);

        // 值
        json.put("value", this.value);

        // 状态
        json.put("status", this.status);

        // 券码
        if(this.voucherCode != null) { json.put("voucherCode", this.voucherCode); }

        // 使用有效期开始时间
        json.put("startTime", this.startTime);

        // 使用有效期结束时间
        json.put("endTime", this.endTime);

        // 店铺信息
        if (this.sellerResult != null) { json.put("sellerResult", this.sellerResult.serialize()); }

        return json;
    }

}
