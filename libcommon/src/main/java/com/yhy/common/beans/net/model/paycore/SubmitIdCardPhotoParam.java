package com.yhy.common.beans.net.model.paycore;

//import com.smart.sdk.api.resp.Api_PAYCORE_SubmitIdCardPhotoParam;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:SubmitIdCardPhotoParam
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-10-31
 * Time:14:30
 * Version 1.1.0
 */


public class SubmitIdCardPhotoParam implements Serializable {
    private static final long serialVersionUID = 389925796666503630L;

    /**
     * 照片正面名称，照片大小不能超过1M
     */
    public String frontPhotoName;

    /**
     * 照片反面名称，照片大小不能超过1M
     */
    public String reversePhotoName;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static SubmitIdCardPhotoParam deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static SubmitIdCardPhotoParam deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            SubmitIdCardPhotoParam result = new SubmitIdCardPhotoParam();

            // 照片正面名称，照片大小不能超过1M

            if(!json.isNull("frontPhotoName")){
                result.frontPhotoName = json.optString("frontPhotoName", null);
            }
            // 照片反面名称，照片大小不能超过1M

            if(!json.isNull("reversePhotoName")){
                result.reversePhotoName = json.optString("reversePhotoName", null);
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

        // 照片正面名称，照片大小不能超过1M
        if(this.frontPhotoName != null) { json.put("frontPhotoName", this.frontPhotoName); }

        // 照片反面名称，照片大小不能超过1M
        if(this.reversePhotoName != null) { json.put("reversePhotoName", this.reversePhotoName); }

        return json;
    }
}
