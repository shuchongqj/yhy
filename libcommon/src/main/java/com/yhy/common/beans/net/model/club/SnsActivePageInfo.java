// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.club;

import com.yhy.common.beans.net.model.comment.ComTagInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SnsActivePageInfo implements Serializable {

    private static final long serialVersionUID = 6763856976153365650L;
    /**
     * 活动表id
     */
    public long id;

    /**
     * 俱乐部outId
     */
    public long outId;

    /**
     * 标题
     */
    public String title;

    /**
     * 俱乐部名称
     */
    public String clubName;

    /**
     * 原价
     */
    public double originalPrice;

    /**
     * 优惠价
     */
    public double preferentialPrice;

    /**
     * 背景图片
     */
    public String image;

    /**
     * 活动时间
     */
    public long acvitityDate;

    /**
     * 活动开始时间
     */
    public long startDate;

    /**
     * 活动结束时间
     */
    public long endDate;

    /**
     * 俱乐部logo
     */
    public String clubImg;

    /**
     * 总人数
     */
    public int memberCount;

    /**
     * 参加人数
     */
    public int joinMemberCount;

    /**
     * 活动状态 未开始、已满员、报名中、已结束
     */
    public String status;

    /**
     * 修改时间
     */
    public long modifyTime;

    /**
     * 创建时间
     */
    public long createrTime;

    /**
     * 地理位置信息
     */
    public POIInfo poiInfo;

    /**
     * 客服电话
     */
    public String serviceTel;

    /**
     * 报名须知
     */
    public String activeJson;

    /**
     * 活动介绍
     */
    public String content;

    /**
     * 商品id
     */
    public int productId;

    /**
     * 创建人id
     */
    public long createId;

    /**
     * 是否点赞 AVAILABLE 是  DELETED 否
     */
    public String isSupport;

    /**
     * 成员信息列表
     */
    public List<ActiveMemberInfo> activeMemberList;
    /**
     * 标签信息列表
     */
    public List<ComTagInfo> comTagList;
    /**
     * 点赞数量
     */
    public int supportNum;

    /**
     * 是否是会员
     */
    public String isMember;

    /**
     * 活动介绍
     */
    public List<ActivityJsonInfo> activityJsonArray;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static SnsActivePageInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static SnsActivePageInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            SnsActivePageInfo result = new SnsActivePageInfo();

            // 活动表id
            result.id = json.optLong("id");
            // 俱乐部outId
            result.outId = json.optLong("outId");
            // 标题

            if (!json.isNull("title")) {
                result.title = json.optString("title", null);
            }
            // 俱乐部名称

            if (!json.isNull("clubName")) {
                result.clubName = json.optString("clubName", null);
            }
            // 原价
            result.originalPrice = json.optDouble("originalPrice");
            // 优惠价
            result.preferentialPrice = json.optDouble("preferentialPrice");
            // 背景图片

            if (!json.isNull("image")) {
                result.image = json.optString("image", null);
            }
            // 活动时间
            result.acvitityDate = json.optLong("acvitityDate");
            // 活动开始时间
            result.startDate = json.optLong("startDate");
            // 活动结束时间
            result.endDate = json.optLong("endDate");
            // 俱乐部logo

            if (!json.isNull("clubImg")) {
                result.clubImg = json.optString("clubImg", null);
            }
            // 总人数
            result.memberCount = json.optInt("memberCount");
            // 参加人数
            result.joinMemberCount = json.optInt("joinMemberCount");
            // 活动状态 未开始、已满员、报名中、已结束

            if (!json.isNull("status")) {
                result.status = json.optString("status", null);
            }
            // 修改时间
            result.modifyTime = json.optLong("modifyTime");
            // 创建时间
            result.createrTime = json.optLong("createrTime");
            // 地理位置信息
            result.poiInfo = POIInfo.deserialize(json.optJSONObject("poiInfo"));
            // 客服电话

            if (!json.isNull("serviceTel")) {
                result.serviceTel = json.optString("serviceTel", null);
            }
            // 报名须知

            if (!json.isNull("activeJson")) {
                result.activeJson = json.optString("activeJson", null);
            }
            // 活动介绍

            if (!json.isNull("content")) {
                result.content = json.optString("content", null);
            }
            // 商品id
            result.productId = json.optInt("productId");
            // 创建人id
            result.createId = json.optLong("createId");
            // 是否点赞 AVAILABLE 是  DELETED 否 

            if (!json.isNull("isSupport")) {
                result.isSupport = json.optString("isSupport", null);
            }
            // 成员信息列表
            JSONArray activeMemberListArray = json.optJSONArray("activeMemberList");
            if (activeMemberListArray != null) {
                int len = activeMemberListArray.length();
                result.activeMemberList = new ArrayList<ActiveMemberInfo>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = activeMemberListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.activeMemberList.add(ActiveMemberInfo.deserialize(jo));
                    }
                }
            }

            // 标签信息列表
            JSONArray comTagListArray = json.optJSONArray("comTagList");
            if (comTagListArray != null) {
                int len = comTagListArray.length();
                result.comTagList = new ArrayList<ComTagInfo>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = comTagListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.comTagList.add(ComTagInfo.deserialize(jo));
                    }
                }
            }

            // 活动介绍
            JSONArray activityJsonArrayArray = json.optJSONArray("activityJsonArray");
            if (activityJsonArrayArray != null) {
                int len = activityJsonArrayArray.length();
                result.activityJsonArray = new ArrayList<ActivityJsonInfo>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = activityJsonArrayArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.activityJsonArray.add(ActivityJsonInfo.deserialize(jo));
                    }
                }
            }
            
            // 点赞数量
            result.supportNum = json.optInt("supportNum");
            // 是否是会员

            if (!json.isNull("isMember")) {
                result.isMember = json.optString("isMember", null);
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

        // 活动表id
        json.put("id", this.id);

        // 俱乐部outId
        json.put("outId", this.outId);

        // 标题
        if (this.title != null) {
            json.put("title", this.title);
        }

        // 俱乐部名称
        if (this.clubName != null) {
            json.put("clubName", this.clubName);
        }

        // 原价
        json.put("originalPrice", this.originalPrice);

        // 优惠价
        json.put("preferentialPrice", this.preferentialPrice);

        // 背景图片
        if (this.image != null) {
            json.put("image", this.image);
        }

        // 活动时间
        json.put("acvitityDate", this.acvitityDate);

        // 活动开始时间
        json.put("startDate", this.startDate);

        // 活动结束时间
        json.put("endDate", this.endDate);

        // 俱乐部logo
        if (this.clubImg != null) {
            json.put("clubImg", this.clubImg);
        }

        // 总人数
        json.put("memberCount", this.memberCount);

        // 参加人数
        json.put("joinMemberCount", this.joinMemberCount);

        // 活动状态 未开始、已满员、报名中、已结束
        if (this.status != null) {
            json.put("status", this.status);
        }

        // 修改时间
        json.put("modifyTime", this.modifyTime);

        // 创建时间
        json.put("createrTime", this.createrTime);

        // 地理位置信息
        if (this.poiInfo != null) {
            json.put("poiInfo", this.poiInfo.serialize());
        }

        // 客服电话
        if (this.serviceTel != null) {
            json.put("serviceTel", this.serviceTel);
        }

        // 报名须知
        if (this.activeJson != null) {
            json.put("activeJson", this.activeJson);
        }

        // 活动介绍
        if (this.content != null) {
            json.put("content", this.content);
        }

        // 商品id
        json.put("productId", this.productId);

        // 创建人id
        json.put("createId", this.createId);

        // 是否点赞 AVAILABLE 是  DELETED 否 
        if (this.isSupport != null) {
            json.put("isSupport", this.isSupport);
        }

        // 成员信息列表 
        if (this.activeMemberList != null) {
            JSONArray activeMemberListArray = new JSONArray();
            for (ActiveMemberInfo value : this.activeMemberList) {
                if (value != null) {
                    activeMemberListArray.put(value.serialize());
                }
            }
            json.put("activeMemberList", activeMemberListArray);
        }

        // 标签信息列表 
        if (this.comTagList != null) {
            JSONArray comTagListArray = new JSONArray();
            for (ComTagInfo value : this.comTagList) {
                if (value != null) {
                    comTagListArray.put(value.serialize());
                }
            }
            json.put("comTagList", comTagListArray);
        }

        // 活动介绍 
        if (this.activityJsonArray != null) {
            JSONArray activityJsonArrayArray = new JSONArray();
            for (ActivityJsonInfo value : this.activityJsonArray)
            {
                if (value != null) {
                    activityJsonArrayArray.put(value.serialize());
                }
            }
            json.put("activityJsonArray", activityJsonArrayArray);
        }

        // 点赞数量
        json.put("supportNum", this.supportNum);

        // 是否是会员
        if (this.isMember != null) {
            json.put("isMember", this.isMember);
        }

        return json;
    }
}
  