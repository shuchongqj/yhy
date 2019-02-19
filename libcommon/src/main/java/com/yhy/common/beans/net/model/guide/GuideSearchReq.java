package com.yhy.common.beans.net.model.guide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:GuideSearchReq
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-8-22
 * Time:14:07
 * Version 1.1.0
 */

public class GuideSearchReq implements Serializable {
    private static final long serialVersionUID = -8120833195065787402L;

    /**
     * 当前页号
     */
    public int pageNo;

    /**
     * 页面大小
     */
    public int pageSize;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static GuideSearchReq deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static GuideSearchReq deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            GuideSearchReq result = new GuideSearchReq();

            // 当前页号
            result.pageNo = json.optInt("pageNo");
            // 页面大小
            result.pageSize = json.optInt("pageSize");
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 当前页号
        json.put("pageNo", this.pageNo);

        // 页面大小
        json.put("pageSize", this.pageSize);

        return json;
    }
}
