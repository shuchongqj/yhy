package com.yhy.common.beans.net.model.tm;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:ExpressInfo
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-12-22
 * Time:15:31
 * Version 1.1.0
 */


public class ExpressInfo implements Serializable {
    private static final long serialVersionUID = 6199688584166023820L;

    /**
     * 物流id
     */
    public long id;

    /**
     * 物流单号
     */
    public String numbers;

    /**
     * 物流公司名称
     */
    public String name;

    /**
     * 物流状态
     */
    public String state;

    /**
     * 物流详细信息
     */
    public List<ExpressDetailInfo> expressDetailList;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static ExpressInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static ExpressInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            ExpressInfo result = new ExpressInfo();

            // 物流id
            result.id = json.optLong("id");
            // 物流单号

            if(!json.isNull("numbers")){
                result.numbers = json.optString("numbers", null);
            }
            // 物流公司名称

            if(!json.isNull("name")){
                result.name = json.optString("name", null);
            }
            // 物流状态

            if(!json.isNull("state")){
                result.state = json.optString("state", null);
            }
            // 物流详细信息
            JSONArray expressDetailListArray = json.optJSONArray("expressDetailList");
            if (expressDetailListArray != null) {
                int len = expressDetailListArray.length();
                result.expressDetailList = new ArrayList<ExpressDetailInfo>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = expressDetailListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.expressDetailList.add(ExpressDetailInfo.deserialize(jo));
                    }
                }
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

        // 物流id
        json.put("id", this.id);

        // 物流单号
        if(this.numbers != null) { json.put("numbers", this.numbers); }

        // 物流公司名称
        if(this.name != null) { json.put("name", this.name); }

        // 物流状态
        if(this.state != null) { json.put("state", this.state); }

        // 物流详细信息
        if (this.expressDetailList != null) {
            JSONArray expressDetailListArray = new JSONArray();
            for (ExpressDetailInfo value : this.expressDetailList)
            {
                if (value != null) {
                    expressDetailListArray.put(value.serialize());
                }
            }
            json.put("expressDetailList", expressDetailListArray);
        }

        return json;
    }
}
