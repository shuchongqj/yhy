// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.comment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CommentInfoList implements Serializable{

    private static final long serialVersionUID = 2750293235750171892L;
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
    public List<CommentInfo> commentInfoList;
    /**
     * 列表数量
     */
    public int commentNum;
      
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static CommentInfoList deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static CommentInfoList deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            CommentInfoList result = new CommentInfoList();
            
            // 当前页码
            result.pageNo = json.optInt("pageNo");
            // 是否有下一页
            result.hasNext = json.optBoolean("hasNext");
            // 列表
            JSONArray commentInfoListArray = json.optJSONArray("commentInfoList");
            if (commentInfoListArray != null) {
                int len = commentInfoListArray.length();
                result.commentInfoList = new ArrayList<CommentInfo>(len);
                for (int i = 0; i < len; i++) {
                        JSONObject jo = commentInfoListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.commentInfoList.add(CommentInfo.deserialize(jo));
                    }
                }
            }
      
            // 列表数量
            result.commentNum = json.optInt("commentNum");
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
        if (this.commentInfoList != null) {
            JSONArray commentInfoListArray = new JSONArray();
            for (CommentInfo value : this.commentInfoList)
            {
                if (value != null) {
                    commentInfoListArray.put(value.serialize());
                }
            }
            json.put("commentInfoList", commentInfoListArray);
        }
      
        // 列表数量
        json.put("commentNum", this.commentNum);
          
        return json;
    }
}
  