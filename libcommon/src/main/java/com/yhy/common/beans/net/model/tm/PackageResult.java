package com.yhy.common.beans.net.model.tm;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:PackageResult
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-12-22
 * Time:15:31
 * Version 1.1.0
 */


public class PackageResult implements Serializable {
    private static final long serialVersionUID = -8880660102331958546L;

    /**
     * 物流单id
     */
    public long id;

    /**
     * 订单id
     */
    public long mainOrderId;

    /**
     * 运单编号
     */
    public String expressNo;

    /**
     * 承运公司
     */
    public String expressCompany;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static PackageResult deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static PackageResult deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            PackageResult result = new PackageResult();

            // 物流单id
            result.id = json.optLong("id");
            // 订单id
            result.mainOrderId = json.optLong("mainOrderId");
            // 运单编号

            if(!json.isNull("expressNo")){
                result.expressNo = json.optString("expressNo", null);
            }
            // 承运公司

            if(!json.isNull("expressCompany")){
                result.expressCompany = json.optString("expressCompany", null);
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

        // 物流单id
        json.put("id", this.id);

        // 订单id
        json.put("mainOrderId", this.mainOrderId);

        // 运单编号
        if(this.expressNo != null) { json.put("expressNo", this.expressNo); }

        // 承运公司
        if(this.expressCompany != null) { json.put("expressCompany", this.expressCompany); }

        return json;
    }
}
