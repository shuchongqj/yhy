// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.trip;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class MasterRecommend implements Serializable {

    private static final long serialVersionUID = -2790363334383718770L;
    /**
     * 推荐id
     */
    public long id;

    /**
     * 用户id
     */
    public long userId;

    /**
     * 用户名
     */
    public String name;

    /**
     * 用户头像url
     */
    public String userPic;

    /**
     * 推荐内容标题
     */
    public String title;

    /**
     * 推荐内容
     */
    public String description;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static MasterRecommend deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static MasterRecommend deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            MasterRecommend result = new MasterRecommend();

            // 推荐id
            result.id = json.optLong("id");
            // 用户id
            result.userId = json.optLong("userId");
            // 用户名

            if(!json.isNull("name")){
                result.name = json.optString("name", null);
            }
            // 用户头像url

            if(!json.isNull("userPic")){
                result.userPic = json.optString("userPic", null);
            }
            // 推荐内容标题

            if(!json.isNull("title")){
                result.title = json.optString("title", null);
            }
            // 推荐内容

            if(!json.isNull("description")){
                result.description = json.optString("description", null);
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

        // 推荐id
        json.put("id", this.id);

        // 用户id
        json.put("userId", this.userId);

        // 用户名
        if(this.name != null) { json.put("name", this.name); }

        // 用户头像url
        if(this.userPic != null) { json.put("userPic", this.userPic); }

        // 推荐内容标题
        if(this.title != null) { json.put("title", this.title); }

        // 推荐内容
        if(this.description != null) { json.put("description", this.description); }

        return json;
    }

}
  