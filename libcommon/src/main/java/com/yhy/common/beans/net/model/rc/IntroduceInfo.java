// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.rc;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class IntroduceInfo implements Serializable{

    private static final long serialVersionUID = -3405176574211213847L;

      
    /**
     * 介绍列表：获取概况 民俗 消费 获取贴士 亮点 必买推荐
     */
    public List<BaseInfo> introduceList;
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static IntroduceInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static IntroduceInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            IntroduceInfo result = new IntroduceInfo();

            // 介绍列表：获取概况 民俗 消费 获取贴士 亮点 必买推荐
            JSONArray introduceListArray = json.optJSONArray("introduceList");
            if (introduceListArray != null) {
                int len = introduceListArray.length();
                result.introduceList = new ArrayList<BaseInfo>(len);
                for (int i = 0; i < len; i++) {
                        JSONObject jo = introduceListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.introduceList.add(BaseInfo.deserialize(jo));
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

        // 介绍列表：获取概况 民俗 消费 获取贴士 亮点 必买推荐 
        if (this.introduceList != null) {
            JSONArray introduceListArray = new JSONArray();
            for (BaseInfo value : this.introduceList)
            {
                if (value != null) {
                    introduceListArray.put(value.serialize());
                }
            }
            json.put("introduceList", introduceListArray);
        }
      
        return json;
    }
}
  