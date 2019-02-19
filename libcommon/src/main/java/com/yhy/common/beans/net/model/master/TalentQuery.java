// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.master;

import com.yhy.common.beans.net.model.club.PageInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class TalentQuery implements Serializable{

    private static final long serialVersionUID = -6151560103299611058L;
    /**
     * 0-全部  1-全程伴游 2-包车服务 4-咨询规划
     */
    public String tagId;

    /**
     * 0-升序 1-降序  默认降序
     */
    public String sort;

    /**
     * 达人搜索查询    搜索字段
     */
    public String searchWord;

    /**
     * 分页查询
     */
    public PageInfo pageInfo;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static TalentQuery deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static TalentQuery deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            TalentQuery result = new TalentQuery();

            // 0-全部  1-全程伴游 2-包车服务 4-咨询规划

            if(!json.isNull("tagId")){
                result.tagId = json.optString("tagId", null);
            }
            // 0-升序 1-降序  默认降序

            if(!json.isNull("sort")){
                result.sort = json.optString("sort", null);
            }
            // 达人搜索查询    搜索字段

            if(!json.isNull("searchWord")){
                result.searchWord = json.optString("searchWord", null);
            }
            // 分页查询
            result.pageInfo = PageInfo.deserialize(json.optJSONObject("pageInfo"));
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 0-全部  1-全程伴游 2-包车服务 4-咨询规划
        if(this.tagId != null) { json.put("tagId", this.tagId); }

        // 0-升序 1-降序  默认降序
        if(this.sort != null) { json.put("sort", this.sort); }

        // 达人搜索查询    搜索字段
        if(this.searchWord != null) { json.put("searchWord", this.searchWord); }

        // 分页查询
        if (this.pageInfo != null) { json.put("pageInfo", this.pageInfo.serialize()); }

        return json;
    }

}
  