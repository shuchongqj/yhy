// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.club;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SnsActivePageInfoList implements Serializable {

    private static final long serialVersionUID = -740922987893499326L;
    /**
     * 当前页码
     */
    public int pageNo;

    /**
     * 是否有下一页
     */
    public boolean hasNext;

    /**
     * 活动列表
     */
    public List<SnsActivePageInfo> snsActivePageInfoList;
    /**
     * 活动数量
     */
    public int activeNum;

    /**
     * 成员数量
     */
    public int clubNum;

    /**
     * 动态数量
     */
    public int clubSujectNum;

    /**
     * 俱乐部信息
     */
    public ClubInfo clubinfo;

    /**
     * 是否加入俱乐部  AVAILABLE：是   DELETED:否
     */
    public String isJoin;


    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static SnsActivePageInfoList deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static SnsActivePageInfoList deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            SnsActivePageInfoList result = new SnsActivePageInfoList();

            // 当前页码
            result.pageNo = json.optInt("pageNo");
            // 是否有下一页
            result.hasNext = json.optBoolean("hasNext");
            // 活动列表
            JSONArray snsActivePageInfoListArray = json.optJSONArray("snsActivePageInfoList");
            if (snsActivePageInfoListArray != null) {
                int len = snsActivePageInfoListArray.length();
                result.snsActivePageInfoList = new ArrayList<SnsActivePageInfo>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = snsActivePageInfoListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.snsActivePageInfoList.add(SnsActivePageInfo.deserialize(jo));
                    }
                }
            }

            // 活动数量
            result.activeNum = json.optInt("activeNum");
            // 成员数量
            result.clubNum = json.optInt("clubNum");
            // 动态数量
            result.clubSujectNum = json.optInt("clubSujectNum");
            // 俱乐部信息
            result.clubinfo = ClubInfo.deserialize(json.optJSONObject("clubinfo"));
            // 是否加入俱乐部  AVAILABLE：是   DELETED:否

            if (!json.isNull("isJoin")) {
                result.isJoin = json.optString("isJoin", null);
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

        // 活动列表 
        if (this.snsActivePageInfoList != null) {
            JSONArray snsActivePageInfoListArray = new JSONArray();
            for (SnsActivePageInfo value : this.snsActivePageInfoList) {
                if (value != null) {
                    snsActivePageInfoListArray.put(value.serialize());
                }
            }
            json.put("snsActivePageInfoList", snsActivePageInfoListArray);
        }

        // 活动数量
        json.put("activeNum", this.activeNum);

        // 成员数量
        json.put("clubNum", this.clubNum);

        // 动态数量
        json.put("clubSujectNum", this.clubSujectNum);

        // 俱乐部信息
        if (this.clubinfo != null) {
            json.put("clubinfo", this.clubinfo.serialize());
        }

        // 是否加入俱乐部  AVAILABLE：是   DELETED:否
        if (this.isJoin != null) {
            json.put("isJoin", this.isJoin);
        }

        return json;
    }
}
  