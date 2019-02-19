// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.member;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MemberPurchauseDetail implements Serializable{

    private static final long serialVersionUID = -6142628646700822633L;
    /**
     * 会员商品列表
     */
    public List<MemeberItem> memeberItems;
    /**
     * 特权列表
     */
    public List<PrivilegeInfo> privilegeInfos;
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static MemberPurchauseDetail deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static MemberPurchauseDetail deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            MemberPurchauseDetail result = new MemberPurchauseDetail();
            
            // 会员商品列表
            JSONArray memeberItemsArray = json.optJSONArray("memeberItems");
            if (memeberItemsArray != null) {
                int len = memeberItemsArray.length();
                result.memeberItems = new ArrayList<MemeberItem>(len);
                for (int i = 0; i < len; i++) {
                        JSONObject jo = memeberItemsArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.memeberItems.add(MemeberItem.deserialize(jo));
                    }
                }
            }
      
            // 特权列表
            JSONArray privilegeInfosArray = json.optJSONArray("privilegeInfos");
            if (privilegeInfosArray != null) {
                int len = privilegeInfosArray.length();
                result.privilegeInfos = new ArrayList<PrivilegeInfo>(len);
                for (int i = 0; i < len; i++) {
                        JSONObject jo = privilegeInfosArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.privilegeInfos.add(PrivilegeInfo.deserialize(jo));
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
        
        // 会员商品列表 
        if (this.memeberItems != null) {
            JSONArray memeberItemsArray = new JSONArray();
            for (MemeberItem value : this.memeberItems)
            {
                if (value != null) {
                    memeberItemsArray.put(value.serialize());
                }
            }
            json.put("memeberItems", memeberItemsArray);
        }
      
        // 特权列表 
        if (this.privilegeInfos != null) {
            JSONArray privilegeInfosArray = new JSONArray();
            for (PrivilegeInfo value : this.privilegeInfos)
            {
                if (value != null) {
                    privilegeInfosArray.put(value.serialize());
                }
            }
            json.put("privilegeInfos", privilegeInfosArray);
        }
      
        return json;
    }
}
  