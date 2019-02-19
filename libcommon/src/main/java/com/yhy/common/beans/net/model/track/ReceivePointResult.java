package com.yhy.common.beans.net.model.track;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:ReceivePointResult
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:赵晓坡
 * Date:16/7/20
 * Time:17:01
 * Version 1.1.0
 */
public class ReceivePointResult implements Serializable {

    private static final long serialVersionUID = -6398049037189764998L;

    public boolean isSuccess;

    public ReceivePointResult() {
    }

    public static ReceivePointResult deserialize(String json) throws JSONException {
        return json != null && !json.isEmpty()?deserialize(new JSONObject(json)):null;
    }

    public static ReceivePointResult deserialize(JSONObject json) throws JSONException {
        if(json != null && json != JSONObject.NULL && json.length() > 0) {
            ReceivePointResult result = new ReceivePointResult();
            result.isSuccess = json.optBoolean("isSuccess");
            return result;
        } else {
            return null;
        }
    }

    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("isSuccess", this.isSuccess);
        return json;
    }
}
