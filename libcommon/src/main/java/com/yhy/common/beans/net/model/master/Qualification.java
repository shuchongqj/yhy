package com.yhy.common.beans.net.model.master;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:Qualification
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-8-23
 * Time:17:13
 * Version 1.1.0
 */

public class Qualification implements Serializable {
    private static final long serialVersionUID = 8110691584833858848L;

    /**
     * 营业执照照片
     */
    public String saleLicensePic;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static Qualification deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static Qualification deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            Qualification result = new Qualification();

            // 营业执照照片

            if(!json.isNull("saleLicensePic")){
                result.saleLicensePic = json.optString("saleLicensePic", null);
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

        // 营业执照照片
        if(this.saleLicensePic != null) { json.put("saleLicensePic", this.saleLicensePic); }

        return json;
    }
}
