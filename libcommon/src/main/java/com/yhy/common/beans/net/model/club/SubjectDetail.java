// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.club;


import com.yhy.common.beans.net.model.comment.CommentInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SubjectDetail implements Serializable {

    private static final long serialVersionUID = -120597198859124335L;
    /**
     * 话题内容
     */
    public SubjectInfo subjectInfo;
      
    /**
     * 点赞数
     */
    public int praiseNum;
      
    /**
     * 评论总数
     */
    public int commentNum;
      
    /**
     * 分享连接
     */
    public String shareUrl;
      
    /**
     * 最近3条评论
     */
    public List<CommentInfo> commentInfoList;
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static SubjectDetail deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static SubjectDetail deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            SubjectDetail result = new SubjectDetail();
            
            // 话题内容
            result.subjectInfo = SubjectInfo.deserialize(json.optJSONObject("subjectInfo"));
            // 点赞数
            result.praiseNum = json.optInt("praiseNum");
            // 评论总数
            result.commentNum = json.optInt("commentNum");
            // 分享连接
            
              if(!json.isNull("shareUrl")){
                  result.shareUrl = json.optString("shareUrl", null);
              }
            // 最近3条评论
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
      
            return result;
        }
        return null;
    }
    
    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();
        
        // 话题内容
        if (this.subjectInfo != null) { json.put("subjectInfo", this.subjectInfo.serialize()); }
          
        // 点赞数
        json.put("praiseNum", this.praiseNum);
          
        // 评论总数
        json.put("commentNum", this.commentNum);
          
        // 分享连接
        if(this.shareUrl != null) { json.put("shareUrl", this.shareUrl); }
          
        // 最近3条评论 
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
      
        return json;
    }
}
  