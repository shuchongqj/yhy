package com.yhy.common.beans.net.model.guide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:GuideSearchInfo
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-8-22
 * Time:14:09
 * Version 1.1.0
 */

public class GuideSearchInfo implements Serializable {
    private static final long serialVersionUID = 6921422770465916811L;

    /**
     * 是否有下一页
     */
    public boolean hasNext;

    /**
     * 记录总数
     */
    public int recordCount;

    /**
     * 返回记录数
     */
    public int recordSize;

    /**
     * 当前页号
     */
    public int pageNo;

    /**
     * 页面大小
     */
    public int pageSize;

    /**
     * 导览景区列表
     */
    public List<GuideScenicInfo> guideList;
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static GuideSearchInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static GuideSearchInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            GuideSearchInfo result = new GuideSearchInfo();

            // 是否有下一页
            result.hasNext = json.optBoolean("hasNext");
            // 记录总数
            result.recordCount = json.optInt("recordCount");
            // 返回记录数
            result.recordSize = json.optInt("recordSize");
            // 当前页号
            result.pageNo = json.optInt("pageNo");
            // 页面大小
            result.pageSize = json.optInt("pageSize");
            // 导览景区列表
            JSONArray guideListArray = json.optJSONArray("guideList");
            if (guideListArray != null) {
                int len = guideListArray.length();
                result.guideList = new ArrayList<GuideScenicInfo>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = guideListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.guideList.add(GuideScenicInfo.deserialize(jo));
                    }
                }
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

        // 是否有下一页
        json.put("hasNext", this.hasNext);

        // 记录总数
        json.put("recordCount", this.recordCount);

        // 返回记录数
        json.put("recordSize", this.recordSize);

        // 当前页号
        json.put("pageNo", this.pageNo);

        // 页面大小
        json.put("pageSize", this.pageSize);

        // 导览景区列表
        if (this.guideList != null) {
            JSONArray guideListArray = new JSONArray();
            for (GuideScenicInfo value : this.guideList)
            {
                if (value != null) {
                    guideListArray.put(value.serialize());
                }
            }
            json.put("guideList", guideListArray);
        }

        return json;
    }
}
