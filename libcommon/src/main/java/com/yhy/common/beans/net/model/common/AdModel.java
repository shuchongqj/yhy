package com.yhy.common.beans.net.model.common;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:AdModel
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:徐学东
 * Date:16/5/7
 * Time:上午10:43
 * Version 1.0
 */
public class AdModel implements Serializable {

    private static final long serialVersionUID = -5120294881573013551L;
    //图片的下载地址
    private String imageUrl;
    //是否可以跳转
    private boolean canJump;
    //跳转的H5
    private String h5;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isCanJump() {
        return canJump;
    }

    public void setCanJump(boolean canJump) {
        this.canJump = canJump;
    }

    public String getH5() {
        return h5;
    }

    public void setH5(String h5) {
        this.h5 = h5;
    }

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static AdModel deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static AdModel deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            AdModel result = new AdModel();

            if (!json.isNull("imageUrl")) {
                result.imageUrl = json.optString("imageUrl", null);
            }

            if (!json.isNull("h5")) {
                result.h5 = json.optString("h5", null);
            }

            if (!json.isNull("canJump")) {
                result.canJump = json.optBoolean("canJump");
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

        if (this.imageUrl != null) {
            json.put("imageUrl", this.imageUrl);
        }

        if (this.h5 != null) {
            json.put("h5", this.h5);
        }

        json.put("canJump", this.canJump);

        return json;
    }
}
