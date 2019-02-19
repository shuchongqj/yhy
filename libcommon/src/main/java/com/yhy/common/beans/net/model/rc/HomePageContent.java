// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.rc;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HomePageContent implements Serializable{

    private static final long serialVersionUID = 254382884503617622L;
    /**
     * 当前页码
     */
    public int pageNo;
      
    /**
     * 是否有下一页
     */
    public boolean hasNext;
      
    /**
     * 精选推荐列表
     */
    public List<GreatRecommend> greatRecommendList;
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static HomePageContent deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static HomePageContent deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            HomePageContent result = new HomePageContent();
            
            // 当前页码
            result.pageNo = json.optInt("pageNo");
            // 是否有下一页
            result.hasNext = json.optBoolean("hasNext");
            // 精选推荐列表
            JSONArray greatRecommendListArray = json.optJSONArray("greatRecommendList");
            if (greatRecommendListArray != null) {
                int len = greatRecommendListArray.length();
                result.greatRecommendList = new ArrayList<GreatRecommend>(len);
                for (int i = 0; i < len; i++) {
                        JSONObject jo = greatRecommendListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.greatRecommendList.add(GreatRecommend.deserialize(jo));
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
          
        // 精选推荐列表 
        if (this.greatRecommendList != null) {
            JSONArray greatRecommendListArray = new JSONArray();
            for (GreatRecommend value : this.greatRecommendList)
            {
                if (value != null) {
                    greatRecommendListArray.put(value.serialize());
                }
            }
            json.put("greatRecommendList", greatRecommendListArray);
        }
      
        return json;
    }
}
  