// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.user;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TravelKaPageInfoList implements Serializable {

    private static final long serialVersionUID = -7029325107485243797L;
    /**
     * 当前页码
     */
    public int pageNo;

    /**
     * 是否有下一页
     */
    public boolean hasNext;

    /**
     * 活动列表
     */
    public List<TravelKaItem> travelKaItemPageInfoList;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static TravelKaPageInfoList deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static TravelKaPageInfoList deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            TravelKaPageInfoList result = new TravelKaPageInfoList();

            // 当前页码
            result.pageNo = json.optInt("pageNo");
            // 是否有下一页
            result.hasNext = json.optBoolean("hasNext");
            // 活动列表
            JSONArray travelKaItemPageInfoListArray = json.optJSONArray("travelKaItemPageInfoList");
            if (travelKaItemPageInfoListArray != null) {
                int len = travelKaItemPageInfoListArray.length();
                result.travelKaItemPageInfoList = new ArrayList<TravelKaItem>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = travelKaItemPageInfoListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.travelKaItemPageInfoList.add(TravelKaItem.deserialize(jo));
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

        // 当前页码
        json.put("pageNo", this.pageNo);

        // 是否有下一页
        json.put("hasNext", this.hasNext);

        // 活动列表 
        if (this.travelKaItemPageInfoList != null) {
            JSONArray travelKaItemPageInfoListArray = new JSONArray();
            for (TravelKaItem value : this.travelKaItemPageInfoList) {
                if (value != null) {
                    travelKaItemPageInfoListArray.put(value.serialize());
                }
            }
            json.put("travelKaItemPageInfoList", travelKaItemPageInfoListArray);
        }

        return json;
    }
}
  