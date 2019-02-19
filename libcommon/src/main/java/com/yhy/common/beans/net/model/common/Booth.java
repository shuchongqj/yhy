// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.common;


import com.yhy.common.beans.net.model.RCShowcase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Booth implements Serializable {

    private static final long serialVersionUID = -6232872047288252411L;
    /**
     * 展位code
     */
    public String code;

    /**
     * 展位名称
     */
    public String name;

    /**
     * 介绍信息
     */
    public String desc;

    /**
     * 展位内容
     */
    public List<RCShowcase> showcases;
    /**
     * 当前页码
     */
    public int pageNo;

    /**
     * 是否有下一页
     */
    public boolean hasNext;

    /**
     * 展位版本号
     */
    public int appVersionCode;

    /**
     * 主标题
     */
    public String title;

    /**
     * 副标题
     */
    public String subTitle;


    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static Booth deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static Booth deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            Booth result = new Booth();

            // 展位code

            if (!json.isNull("code")) {
                result.code = json.optString("code", null);
            }
            // 展位名称

            if (!json.isNull("name")) {
                result.name = json.optString("name", null);
            }
            // 介绍信息

            if (!json.isNull("desc")) {
                result.desc = json.optString("desc", null);
            }
            // 展位内容
            JSONArray showcasesArray = json.optJSONArray("showcases");
            if (showcasesArray != null) {
                int len = showcasesArray.length();
                result.showcases = new ArrayList<RCShowcase>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = showcasesArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.showcases.add(RCShowcase.deserialize(jo));
                    }
                }
            }

            // 当前页码
            result.pageNo = json.optInt("pageNo");
            // 是否有下一页
            result.hasNext = json.optBoolean("hasNext");
            // 展位版本号
            result.appVersionCode = json.optInt("appVersionCode");
            // 主标题

            if (!json.isNull("title")) {
                result.title = json.optString("title", null);
            }
            // 副标题

            if (!json.isNull("subTitle")) {
                result.subTitle = json.optString("subTitle", null);
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

        // 展位code
        if (this.code != null) {
            json.put("code", this.code);
        }

        // 展位名称
        if (this.name != null) {
            json.put("name", this.name);
        }

        // 介绍信息
        if (this.desc != null) {
            json.put("desc", this.desc);
        }

        // 展位内容 
        if (this.showcases != null) {
            JSONArray showcasesArray = new JSONArray();
            for (RCShowcase value : this.showcases) {
                if (value != null) {
                    showcasesArray.put(value.serialize());
                }
            }
            json.put("showcases", showcasesArray);
        }

        // 当前页码
        json.put("pageNo", this.pageNo);

        // 是否有下一页
        json.put("hasNext", this.hasNext);
// 展位版本号
        json.put("appVersionCode", this.appVersionCode);

        // 主标题
        if (this.title != null) {
            json.put("title", this.title);
        }

        // 副标题
        if (this.subTitle != null) {
            json.put("subTitle", this.subTitle);
        }
        return json;
    }

}
  