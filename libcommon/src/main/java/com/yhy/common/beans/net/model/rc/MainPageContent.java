// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.rc;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainPageContent implements Serializable {

    private static final long serialVersionUID = -6873054031221339709L;
    /**
     * 金牌旅游咖
     */
    public TravelMasterColumn travelMasterColumn;

    /**
     * 精选推荐
     */
    public List<BaseInfo> recommendList;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static MainPageContent deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static MainPageContent deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            MainPageContent result = new MainPageContent();

            // 金牌旅游咖
            result.travelMasterColumn = TravelMasterColumn.deserialize(json.optJSONObject("travelMasterColumn"));
            // 精选推荐
            JSONArray recommendListArray = json.optJSONArray("recommendList");
            if (recommendListArray != null) {
                int len = recommendListArray.length();
                result.recommendList = new ArrayList<BaseInfo>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = recommendListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.recommendList.add(BaseInfo.deserialize(jo));
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

        // 金牌旅游咖
        if (this.travelMasterColumn != null) {
            json.put("travelMasterColumn", this.travelMasterColumn.serialize());
        }

        // 精选推荐 
        if (this.recommendList != null) {
            JSONArray recommendListArray = new JSONArray();
            for (BaseInfo value : this.recommendList) {
                if (value != null) {
                    recommendListArray.put(value.serialize());
                }
            }
            json.put("recommendList", recommendListArray);
        }

        return json;
    }
}
  