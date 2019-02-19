// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.club;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ClubMemberInfoList  implements Serializable {

    private static final long serialVersionUID = -6826640206209219668L;
    /**
     * 当前页码
     */
    public int pageNo;
      
    /**
     * 是否有下一页
     */
    public boolean hasNext;
      
    /**
     * 列表
     */
    public List<ClubMemberInfo> clubMemberInfoList;
    /**
     * 部长信息
     */
    public ClubMemberInfo clubMemberInfo;

    /**
     * 成员数量
     */
    public int clubMemberNum;
      
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static ClubMemberInfoList deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static ClubMemberInfoList deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            ClubMemberInfoList result = new ClubMemberInfoList();
            
            // 当前页码
            result.pageNo = json.optInt("pageNo");
            // 是否有下一页
            result.hasNext = json.optBoolean("hasNext");
            // 列表
            JSONArray clubMemberInfoListArray = json.optJSONArray("clubMemberInfoList");
            if (clubMemberInfoListArray != null) {
                int len = clubMemberInfoListArray.length();
                result.clubMemberInfoList = new ArrayList<ClubMemberInfo>(len);
                for (int i = 0; i < len; i++) {
                        JSONObject jo = clubMemberInfoListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.clubMemberInfoList.add(ClubMemberInfo.deserialize(jo));
                    }
                }
            }
      
            // 部长信息
            result.clubMemberInfo = ClubMemberInfo.deserialize(json.optJSONObject("clubMemberInfo"));

            // 成员数量
            result.clubMemberNum = json.optInt("clubMemberNum");
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
          
        // 列表 
        if (this.clubMemberInfoList != null) {
            JSONArray clubMemberInfoListArray = new JSONArray();
            for (ClubMemberInfo value : this.clubMemberInfoList)
            {
                if (value != null) {
                    clubMemberInfoListArray.put(value.serialize());
                }
            }
            json.put("clubMemberInfoList", clubMemberInfoListArray);
        }
      
        // 部长信息
        if (this.clubMemberInfo != null) { json.put("clubMemberInfo", this.clubMemberInfo.serialize()); }

        // 成员数量
        json.put("clubMemberNum", this.clubMemberNum);
          
        return json;
    }
}
  