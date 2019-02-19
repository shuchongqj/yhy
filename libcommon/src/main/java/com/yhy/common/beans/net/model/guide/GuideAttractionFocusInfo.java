package com.yhy.common.beans.net.model.guide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:GuideAttractionFocusInfo
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-8-22
 * Time:11:12
 * Version 1.1.0
 */

public class GuideAttractionFocusInfo implements Serializable {
    private static final long serialVersionUID = -4012934751593212821L;

    /**
     * 导览景区信息
     */
    public GuideScenicInfo guideInfo;

    /**
     * 看点列表
     */
    public List<GuideAttractionInfo> attractionList;
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static GuideAttractionFocusInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static GuideAttractionFocusInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            GuideAttractionFocusInfo result = new GuideAttractionFocusInfo();

            // 导览景区信息
            result.guideInfo = GuideScenicInfo.deserialize(json.optJSONObject("guideInfo"));
            // 看点列表
            JSONArray attractionListArray = json.optJSONArray("attractionList");
            if (attractionListArray != null) {
                int len = attractionListArray.length();
                result.attractionList = new ArrayList<GuideAttractionInfo>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = attractionListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.attractionList.add(GuideAttractionInfo.deserialize(jo));
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

        // 导览景区信息
        if (this.guideInfo != null) { json.put("guideInfo", this.guideInfo.serialize()); }

        // 看点列表
        if (this.attractionList != null) {
            JSONArray attractionListArray = new JSONArray();
            for (GuideAttractionInfo value : this.attractionList)
            {
                if (value != null) {
                    attractionListArray.put(value.serialize());
                }
            }
            json.put("attractionList", attractionListArray);
        }

        return json;
    }
}
