// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.rc;



import com.yhy.common.beans.net.model.club.ClubInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GreatClubContent implements Serializable{

    private static final long serialVersionUID = -313202661926566344L;
    /**
     * 当前页码
     */
    public int pageNo;
      
    /**
     * 是否有下一页
     */
    public boolean hasNext;
      
    /**
     * 俱乐部列表
     */
    public List<ClubInfo> clubInfoList;
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static GreatClubContent deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static GreatClubContent deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            GreatClubContent result = new GreatClubContent();
            
            // 当前页码
            result.pageNo = json.optInt("pageNo");
            // 是否有下一页
            result.hasNext = json.optBoolean("hasNext");
            // 俱乐部列表
            JSONArray clubInfoListArray = json.optJSONArray("clubInfoList");
            if (clubInfoListArray != null) {
                int len = clubInfoListArray.length();
                result.clubInfoList = new ArrayList<ClubInfo>(len);
                for (int i = 0; i < len; i++) {
                        JSONObject jo = clubInfoListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.clubInfoList.add(ClubInfo.deserialize(jo));
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
          
        // 俱乐部列表 
        if (this.clubInfoList != null) {
            JSONArray clubInfoListArray = new JSONArray();
            for (ClubInfo value : this.clubInfoList)
            {
                if (value != null) {
                    clubInfoListArray.put(value.serialize());
                }
            }
            json.put("clubInfoList", clubInfoListArray);
        }
      
        return json;
    }
}
  