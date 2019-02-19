// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.msg;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class PushRecordResult implements Serializable {

    private static final long serialVersionUID = -6279829298932219343L;
    /**
     * 用户Id
     */
    public long userId;

    /**
     * 标题
     */
    public String push_title;

    /**
     * 内容
     */
    public String push_content;

    /**
     * 发送时间
     */
    public long send_date;

    /**
     * 业务类型
     */
    public int biz_type;

    /**
     * 业务子类型
     */
    public int biz_subtype;

    /**
     * 实体id(手机端跳转使用)
     */
    public long out_id;

    /**
     * 扩展信息
     */
    public String extra_data;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static PushRecordResult deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static PushRecordResult deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            PushRecordResult result = new PushRecordResult();

            // 用户Id
            result.userId = json.optLong("userId");
            // 标题

            if (!json.isNull("push_title")) {
                result.push_title = json.optString("push_title", null);
            }
            // 内容

            if (!json.isNull("push_content")) {
                result.push_content = json.optString("push_content", null);
            }
            // 发送时间
            result.send_date = json.optLong("send_date");
            // 业务类型
            result.biz_type = json.optInt("biz_type");
            // 业务子类型
            result.biz_subtype = json.optInt("biz_subtype");
            // 实体id(手机端跳转使用)
            result.out_id = json.optLong("out_id");
            // 扩展信息

            if (!json.isNull("extra_data")) {
                result.extra_data = json.optString("extra_data", null);
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

        // 用户Id
        json.put("userId", this.userId);

        // 标题
        if (this.push_title != null) {
            json.put("push_title", this.push_title);
        }

        // 内容
        if (this.push_content != null) {
            json.put("push_content", this.push_content);
        }

        // 发送时间
        json.put("send_date", this.send_date);

        // 业务类型
        json.put("biz_type", this.biz_type);

        // 业务子类型
        json.put("biz_subtype", this.biz_subtype);

        // 实体id(手机端跳转使用)
        json.put("out_id", this.out_id);

        // 扩展信息
        if (this.extra_data != null) {
            json.put("extra_data", this.extra_data);
        }

        return json;
    }
}
  