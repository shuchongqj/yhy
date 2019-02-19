// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.tm;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OrderRate implements Serializable {

    private static final long serialVersionUID = 4822671845630511214L;

    /**
     * 订单id 必填
     */
    public long bizOrderId;

    /**
     * 是否匿名 AVAILABLE:是  DELETED：否
     */
    public String anony;

    /**
     * 评价内容
     */
    public String content;

    /**
     * 图片列表
     */
    public List<String> picUrlList;
    /**
     * 评分维度列表
     */
    public List<RateDimension> rateDimensionList;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static OrderRate deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static OrderRate deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            OrderRate result = new OrderRate();

            // 订单id 必填
            result.bizOrderId = json.optLong("bizOrderId");
            // 是否匿名 AVAILABLE:是  DELETED：否

            if (!json.isNull("anony")) {
                result.anony = json.optString("anony", null);
            }
            // 评价内容

            if (!json.isNull("content")) {
                result.content = json.optString("content", null);
            }
            // 图片列表
            JSONArray picUrlListArray = json.optJSONArray("picUrlList");
            if (picUrlListArray != null) {
                int len = picUrlListArray.length();
                result.picUrlList = new
                        ArrayList<String>(len);
                for (int i = 0; i < len; i++) {

                    if (!picUrlListArray.isNull(i)) {
                        result.picUrlList.add(picUrlListArray.optString(i, null));
                    } else {
                        result.picUrlList.add(i, null);
                    }

                }
            }

            // 评分维度列表
            JSONArray rateDimensionListArray = json.optJSONArray("rateDimensionList");
            if (rateDimensionListArray != null) {
                int len = rateDimensionListArray.length();
                result.rateDimensionList = new ArrayList<RateDimension>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = rateDimensionListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.rateDimensionList.add(RateDimension.deserialize(jo));
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

        // 订单id 必填
        json.put("bizOrderId", this.bizOrderId);

        // 是否匿名 AVAILABLE:是  DELETED：否
        if (this.anony != null) {
            json.put("anony", this.anony);
        }

        // 评价内容
        if (this.content != null) {
            json.put("content", this.content);
        }

        // 图片列表 
        if (this.picUrlList != null) {
            JSONArray picUrlListArray = new JSONArray();
            for (String value : this.picUrlList) {
                picUrlListArray.put(value);
            }
            json.put("picUrlList", picUrlListArray);
        }

        // 评分维度列表 
        if (this.rateDimensionList != null) {
            JSONArray rateDimensionListArray = new JSONArray();
            for (RateDimension value : this.rateDimensionList) {
                if (value != null) {
                    rateDimensionListArray.put(value.serialize());
                }
            }
            json.put("rateDimensionList", rateDimensionListArray);
        }

        return json;
    }

}
  