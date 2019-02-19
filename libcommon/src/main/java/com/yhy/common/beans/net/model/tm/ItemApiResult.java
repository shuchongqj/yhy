// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.tm;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ItemApiResult implements Serializable{

    private static final long serialVersionUID = -3451378510662872538L;
    /**
     * 商品管理列表
     */
    public List<ItemManagement> itemManagements;
    /**
     * 商品详情
     */
    public SDItemDetail itemDetail;

    /**
     * 达人信息
     */
    public TalentInfo talentInfo;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static ItemApiResult deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static ItemApiResult deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            ItemApiResult result = new ItemApiResult();

            // 商品管理列表
            JSONArray itemManagementsArray = json.optJSONArray("itemManagements");
            if (itemManagementsArray != null) {
                int len = itemManagementsArray.length();
                result.itemManagements = new ArrayList<ItemManagement>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = itemManagementsArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.itemManagements.add(ItemManagement.deserialize(jo));
                    }
                }
            }

            // 商品详情
            result.itemDetail = SDItemDetail.deserialize(json.optJSONObject("itemDetail"));
            // 达人信息
            result.talentInfo = TalentInfo.deserialize(json.optJSONObject("talentInfo"));
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 商品管理列表
        if (this.itemManagements != null) {
            JSONArray itemManagementsArray = new JSONArray();
            for (ItemManagement value : this.itemManagements)
            {
                if (value != null) {
                    itemManagementsArray.put(value.serialize());
                }
            }
            json.put("itemManagements", itemManagementsArray);
        }

        // 商品详情
        if (this.itemDetail != null) { json.put("itemDetail", this.itemDetail.serialize()); }

        // 达人信息
        if (this.talentInfo != null) { json.put("talentInfo", this.talentInfo.serialize()); }

        return json;
    }
}
  