// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.trip;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class PictureVO implements Serializable {

    private static final long serialVersionUID = -474713066478353870L;
    /**
     * 图片id
     */
    public long id;

    /**
     * 图片url
     */
    public String url;

    /**
     * 图片名
     */
    public String name;

    /**
     * 图片后缀名
     */
    public String suffix;

    /**
     * 图片类型 OUTLOOK：外观 INDOOR：内景 ROOM：房间 OTHER：其他
     */
    public String type;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static PictureVO deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static PictureVO deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            PictureVO result = new PictureVO();

            // 图片id
            result.id = json.optLong("id");
            // 图片url

            if (!json.isNull("url")) {
                result.url = json.optString("url", null);
            }
            // 图片名

            if (!json.isNull("name")) {
                result.name = json.optString("name", null);
            }
            // 图片后缀名

            if (!json.isNull("suffix")) {
                result.suffix = json.optString("suffix", null);
            }
            // 图片类型 OUTLOOK：外观 INDOOR：内景 ROOM：房间 OTHER：其他

            if (!json.isNull("type")) {
                result.type = json.optString("type", null);
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

        // 图片id
        json.put("id", this.id);

        // 图片url
        if (this.url != null) {
            json.put("url", this.url);
        }

        // 图片名
        if (this.name != null) {
            json.put("name", this.name);
        }

        // 图片后缀名
        if (this.suffix != null) {
            json.put("suffix", this.suffix);
        }

        // 图片类型 OUTLOOK：外观 INDOOR：内景 ROOM：房间 OTHER：其他
        if (this.type != null) {
            json.put("type", this.type);
        }

        return json;
    }

}
  