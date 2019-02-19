// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.rc;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

@Table(name = "sys_configs")
public class SysConfig implements Serializable {

    private static final long serialVersionUID = 1659686412170217182L;
    @Id
    @Column(column = "id")
    public long id;
    /**
     * 标题
     */
    @Column(column = "title")
    public String title;

    /**
     * 内容
     */
    @Column(column = "content")
    public String content;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static SysConfig deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static SysConfig deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            SysConfig result = new SysConfig();

            // 标题

            if (!json.isNull("title")) {
                result.title = json.optString("title", null);
            }
            // 内容

            if (!json.isNull("content")) {
                result.content = json.optString("content", null);
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

        // 标题
        if (this.title != null) {
            json.put("title", this.title);
        }

        // 内容
        if (this.content != null) {
            json.put("content", this.content);
        }

        return json;
    }
}
  