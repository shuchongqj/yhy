package com.yhy.common.beans.net.model.tm;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:CreateProcessOrderParam
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-8-17
 * Time:18:55
 * Version 1.1.0
 */

public class CreateProcessOrderParam implements Serializable {
    private static final long serialVersionUID = 8051123442543646003L;

    /**
     * 服务商品id 必填
     */
    public long itemId;

    /**
     * 服务流程类型 CONSULT:达人咨询
     */
    public String processType;

    /**
     * 流程单来源  GENERAL_CONSULT:普通咨询 ,QUICK_CONSULTING:快速咨询
     */
    public String processOrderSource;

    /**
     * 流程单咨询内容
     */
    public ProcessOrderContent processOrderContent;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static CreateProcessOrderParam deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static CreateProcessOrderParam deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            CreateProcessOrderParam result = new CreateProcessOrderParam();

            // 服务商品id 必填
            result.itemId = json.optLong("itemId");
            // 服务流程类型 CONSULT:达人咨询

            if(!json.isNull("processType")){
                result.processType = json.optString("processType", null);
            }
            // 流程单来源  GENERAL_CONSULT:普通咨询 ,QUICK_CONSULTING:快速咨询

            if(!json.isNull("processOrderSource")){
                result.processOrderSource = json.optString("processOrderSource", null);
            }
            // 流程单咨询内容
            result.processOrderContent = ProcessOrderContent.deserialize(json.optJSONObject("processOrderContent"));
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 服务商品id 必填
        json.put("itemId", this.itemId);

        // 服务流程类型 CONSULT:达人咨询
        if(this.processType != null) { json.put("processType", this.processType); }

        // 流程单来源  GENERAL_CONSULT:普通咨询 ,QUICK_CONSULTING:快速咨询
        if(this.processOrderSource != null) { json.put("processOrderSource", this.processOrderSource); }

        // 流程单咨询内容
        if (this.processOrderContent != null) { json.put("processOrderContent", this.processOrderContent.serialize()); }

        return json;
    }
}
