package com.yhy.common.beans.net.model.tm;



import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:ItemProperty
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-8-22
 * Time:19:53
 * Version 1.1.0
 */

public class ItemProperty implements Serializable {
    private static final long serialVersionUID = 6906650553589054818L;

    /**
     * id
     */
    public long id;

    /**
     * 属性名称
     */
    public String text;

    /**
     * 属性类型
     */
    public String type;

    /**
     * 属性值
     */
    public String value;

    /**
     * 属性默认文案
     */
    public String defaultDesc;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static ItemProperty deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static ItemProperty deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            ItemProperty result = new ItemProperty();

            // id
            result.id = json.optLong("id");
            // 属性名称

            if(!json.isNull("text")){
                result.text = json.optString("text", null);
            }
            // 属性类型

            if(!json.isNull("type")){
                result.type = json.optString("type", null);
            }
            // 属性值

            if(!json.isNull("value")){
                result.value = json.optString("value", null);
            }
            // 属性默认文案

            if(!json.isNull("defaultDesc")){
                result.defaultDesc = json.optString("defaultDesc", null);
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

        // 属性名称
        if(this.text != null) { json.put("text", this.text); }

        // 属性类型
        if(this.type != null) { json.put("type", this.type); }

        // 属性值
        if(this.value != null) { json.put("value", this.value); }

        // 属性默认文案
        if(this.defaultDesc != null) { json.put("defaultDesc", this.defaultDesc); }

        return json;
    }
}
