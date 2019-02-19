// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.user;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class KaClub implements Serializable {

    private static final long serialVersionUID = 307040577969370352L;
    /**
     * 是否是部长 0-否 1-是
     */
    public int isMinister;

    /**
     * 俱乐部id
     */
    public long clubId;

    /**
     * 俱乐部名称
     */
    public String clubName;

    /**
     * 俱乐部图片
     */
    public String clubImg;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static KaClub deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static KaClub deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            KaClub result = new KaClub();

            // 是否是部长 0-否 1-是
            result.isMinister = json.optInt("isMinister");
            // 俱乐部id
            result.clubId = json.optLong("clubId");
            // 俱乐部名称

            if (!json.isNull("clubName")) {
                result.clubName = json.optString("clubName", null);
            }
            // 俱乐部图片

            if (!json.isNull("clubImg")) {
                result.clubImg = json.optString("clubImg", null);
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

        // 是否是部长 0-否 1-是
        json.put("isMinister", this.isMinister);

        // 俱乐部id
        json.put("clubId", this.clubId);

        // 俱乐部名称
        if (this.clubName != null) {
            json.put("clubName", this.clubName);
        }

        // 俱乐部图片
        if (this.clubImg != null) {
            json.put("clubImg", this.clubImg);
        }

        return json;
    }
}
  