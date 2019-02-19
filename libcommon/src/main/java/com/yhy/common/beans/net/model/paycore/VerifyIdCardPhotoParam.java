package com.yhy.common.beans.net.model.paycore;

//import com.smart.sdk.api.resp.Api_PAYCORE_VerifyIdCardPhotoParam;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:VerifyIdCardPhotoParam
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-10-31
 * Time:14:23
 * Version 1.1.0
 */


public class VerifyIdCardPhotoParam implements Serializable {
    private static final long serialVersionUID = -3744580715186350061L;

    /**
     * 照片名称,照片不能超过1M
     */
    public String photoName;

    /**
     * 照片类型,正面:FRONT,反面:REVERSE
     */
    public String photoType;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static VerifyIdCardPhotoParam deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static VerifyIdCardPhotoParam deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            VerifyIdCardPhotoParam result = new VerifyIdCardPhotoParam();

            // 照片名称,照片不能超过1M

            if(!json.isNull("photoName")){
                result.photoName = json.optString("photoName", null);
            }
            // 照片类型,正面:FRONT,反面:REVERSE

            if(!json.isNull("photoType")){
                result.photoType = json.optString("photoType", null);
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

        // 照片名称,照片不能超过1M
        if(this.photoName != null) { json.put("photoName", this.photoName); }

        // 照片类型,正面:FRONT,反面:REVERSE
        if(this.photoType != null) { json.put("photoType", this.photoType); }

        return json;
    }
}
