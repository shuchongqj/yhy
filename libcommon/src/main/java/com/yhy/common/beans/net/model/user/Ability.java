// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.user;
    
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Ability implements Serializable {

    private static final long serialVersionUID = 3606484987037401328L;
    /**
     * id
     */
    public long id;

    /**
     * 能力名称
     */
    public String name;

    /**
     * 能力图片
     */
    public String img;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static Ability deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static Ability deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            Ability result = new Ability();

            // id
            result.id = json.optLong("id");
            // 能力名称

            if(!json.isNull("name")){
                result.name = json.optString("name", null);
            }
            // 能力图片

            if(!json.isNull("img")){
                result.img = json.optString("img", null);
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

        // id
        json.put("id", this.id);

        // 能力名称
        if(this.name != null) { json.put("name", this.name); }

        // 能力图片
        if(this.img != null) { json.put("img", this.img); }

        return json;
    }

}
  