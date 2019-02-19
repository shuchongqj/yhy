// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.pedometer;
    
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class PointConfig implements Serializable{

    private static final long serialVersionUID = -7801211170619249769L;
    /**
     * 前N步
     */
    public int levelOneStep;

    /**
     * 前N步积分
     */
    public int levelOneStepPoint;

    /**
     * 之后每N步
     */
    public int levelTwoStep;

    /**
     * 之后每N步积分
     */
    public int levelTwoStepPoint;

    /**
     * 文案信息
     */
    public String copy;

    /**
     * 文案完整信息
     */
    public String toString;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static PointConfig deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static PointConfig deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            PointConfig result = new PointConfig();

            // 前N步
            result.levelOneStep = json.optInt("levelOneStep");
            // 前N步积分
            result.levelOneStepPoint = json.optInt("levelOneStepPoint");
            // 之后每N步
            result.levelTwoStep = json.optInt("levelTwoStep");
            // 之后每N步积分
            result.levelTwoStepPoint = json.optInt("levelTwoStepPoint");
            // 文案信息

            if(!json.isNull("copy")){
                result.copy = json.optString("copy", null);
            }
            // 文案完整信息

            if(!json.isNull("toString")){
                result.toString = json.optString("toString", null);
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

        // 前N步
        json.put("levelOneStep", this.levelOneStep);

        // 前N步积分
        json.put("levelOneStepPoint", this.levelOneStepPoint);

        // 之后每N步
        json.put("levelTwoStep", this.levelTwoStep);

        // 之后每N步积分
        json.put("levelTwoStepPoint", this.levelTwoStepPoint);

        // 文案信息
        if(this.copy != null) { json.put("copy", this.copy); }

        // 文案完整信息
        if(this.toString != null) { json.put("toString", this.toString); }

        return json;
    }
}
  