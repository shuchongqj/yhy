// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.user;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TravelKaClub implements Serializable{

    private static final long serialVersionUID = 5441369957867831247L;
    /**
     * 是否是部长 0-否 1-是
     */
    public int isMinister;
      
    /**
     * 直播数
     */
    public int liveCount;
      
    /**
     * 动态数
     */
    public int informationsCount;
      
    /**
     * 俱乐部集合
     */
    public List<KaClub> kaClubs;
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static TravelKaClub deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static TravelKaClub deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            TravelKaClub result = new TravelKaClub();
            
            // 是否是部长 0-否 1-是
            result.isMinister = json.optInt("isMinister");
            // 直播数
            result.liveCount = json.optInt("liveCount");
            // 动态数
            result.informationsCount = json.optInt("informationsCount");
            // 俱乐部集合
            JSONArray kaClubsArray = json.optJSONArray("kaClubs");
            if (kaClubsArray != null) {
                int len = kaClubsArray.length();
                result.kaClubs = new ArrayList<KaClub>(len);
                for (int i = 0; i < len; i++) {
                        JSONObject jo = kaClubsArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.kaClubs.add(KaClub.deserialize(jo));
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
        
        // 是否是部长 0-否 1-是
        json.put("isMinister", this.isMinister);
          
        // 直播数
        json.put("liveCount", this.liveCount);
          
        // 动态数
        json.put("informationsCount", this.informationsCount);
          
        // 俱乐部集合 
        if (this.kaClubs != null) {
            JSONArray kaClubsArray = new JSONArray();
            for (KaClub value : this.kaClubs)
            {
                if (value != null) {
                    kaClubsArray.put(value.serialize());
                }
            }
            json.put("kaClubs", kaClubsArray);
        }
      
        return json;
    }
}
  