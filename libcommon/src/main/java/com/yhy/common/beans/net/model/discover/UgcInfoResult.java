package com.yhy.common.beans.net.model.discover;


import com.yhy.common.beans.net.model.club.POIInfo;
import com.yhy.common.beans.net.model.trip.TagInfo;
import com.yhy.common.beans.net.model.user.UserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:UgcInfoResult
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-6-28
 * Time:18:36
 * Version 1.0
 * Description:
 */
public class UgcInfoResult implements Serializable{

    private static final long serialVersionUID = -9048291835659819052L;
    /**
     * 用户信息
     */
    public UserInfo userInfo;

    public String title;

    /**
     * 地理位置信息
     */
    public POIInfo poiInfo;

    /**
     * 话题信息
     */
    public List<TopicInfoResult> TopicInfoResultList;
    /**
     * 发布时间
     */
    public long gmtCreated;

    /**
     * ugc内容
     */
    public String textContent;

    /**
     * 图片集合
     */
    public List<String> picList;
    /**
     * 视频URL
     */
    public String videoUrl;

    /**
     * 视频缩略图URL
     */
    public String videoPicUrl;

    /**
     * 关注类型 0:未关注   1:单向关注 2:双向关注
     */
    public int type;

    /**
     * 评论数
     */
    public int commentNum;

    /**
     * 点赞数
     */
    public int supportNum;

    /**
     * 是否点赞  AVAILABLE：是   DELETED:否
     */
    public String isSupport;

    /**
     * id
     */
    public long id;

    /**
     * 直播id
     */
    public long liveId;

    /**
     * 直播状态 1.直播  2.回放
     */
    public int liveStatus;

    /**
     * 内容类型  2.UGC 3.直播
     */
    public int contentType;


    /**
     * 短视频类型  4为直录短视频 5为上传短视频
     */
    public int shortVideoType;

    /**
     * 观看人数
     */
    public long viewNum;
    /**
     * 标签信息列表
     */
    public List<TagInfo> tagInfoList;

    /**
     * 0 HORIZONTAL 1 VERTICAL
     */
    public int liveScreenType;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static UgcInfoResult deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static UgcInfoResult deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            UgcInfoResult result = new UgcInfoResult();

            // 用户信息
            result.userInfo = UserInfo.deserialize(json.optJSONObject("userInfo"));
            // 地理位置信息
            result.poiInfo = POIInfo.deserialize(json.optJSONObject("poiInfo"));
            // 话题信息
            JSONArray TopicInfoResultListArray = json.optJSONArray("TopicInfoResultList");
            if (TopicInfoResultListArray != null) {
                int len = TopicInfoResultListArray.length();
                result.TopicInfoResultList = new ArrayList<TopicInfoResult>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = TopicInfoResultListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.TopicInfoResultList.add(TopicInfoResult.deserialize(jo));
                    }
                }
            }

            // 发布时间
            result.gmtCreated = json.optLong("gmtCreated");
            // ugc内容

            result.liveScreenType = json.optInt("liveScreenType");

            if(!json.isNull("textContent")){
                result.textContent = json.optString("textContent", null);
            }

            if (!json.isNull("title")){
                result.title = json.optString("title","");
            }

            // 图片集合
            JSONArray picListArray = json.optJSONArray("picList");
            if (picListArray != null) {
                int len = picListArray.length();
                result.picList = new
                        ArrayList<String>(len);
                for (int i = 0; i < len; i++) {

                    if(!picListArray.isNull(i)){
                        result.picList.add(picListArray.optString(i, null));
                    }else{
                        result.picList.add(i, null);
                    }

                }
            }

            // 视频URL

            if(!json.isNull("videoUrl")){
                result.videoUrl = json.optString("videoUrl", null);
            }
            // 视频缩略图URL

            if(!json.isNull("videoPicUrl")){
                result.videoPicUrl = json.optString("videoPicUrl", null);
            }
            // 关注类型 0:未关注   1:单向关注 2:双向关注
            result.type = json.optInt("type");
            // 评论数
            result.commentNum = json.optInt("commentNum");
            // 点赞数
            result.supportNum = json.optInt("supportNum");
            // 是否点赞
            result.isSupport = json.optString("isSupport");
            // id
            result.id = json.optLong("id");
            // 直播id
            result.liveId = json.optLong("liveId");
            // 直播状态 1.直播  2.回复
            result.liveStatus = json.optInt("liveStatus");
            // 内容类型  2.UGC 3.直播
            result.contentType = json.optInt("contentType");
            // 短视频类型  4为直录短视频 5为上传短视频
            result.shortVideoType = json.optInt("shortVideoType");
            // 观看人数
             result.viewNum = json.optLong("viewNum");

            // 标签信息列表
            JSONArray tagInfoListArray = json.optJSONArray("tagInfoList");
            if (tagInfoListArray != null) {
                int len = tagInfoListArray.length();
                result.tagInfoList = new ArrayList<TagInfo>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = tagInfoListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.tagInfoList.add(TagInfo.deserialize(jo));
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

        // 用户信息
        if (this.userInfo != null) { json.put("userInfo", this.userInfo.serialize()); }

        // 地理位置信息
        if (this.poiInfo != null) { json.put("poiInfo", this.poiInfo.serialize()); }

        // 话题信息
        if (this.TopicInfoResultList != null) {
            JSONArray TopicInfoResultListArray = new JSONArray();
            for (TopicInfoResult value : this.TopicInfoResultList)
            {
                if (value != null) {
                    TopicInfoResultListArray.put(value.serialize());
                }
            }
            json.put("TopicInfoResultList", TopicInfoResultListArray);
        }

        // 发布时间
        json.put("gmtCreated", this.gmtCreated);

        if (this.title != null){
            json.put("title",this.title);
        }

        // ugc内容
        if(this.textContent != null) { json.put("textContent", this.textContent); }

        // 图片集合
        if (this.picList != null) {
            JSONArray picListArray = new JSONArray();
            for (String value : this.picList)
            {
                picListArray.put(value);
            }
            json.put("picList", picListArray);
        }

        // 视频URL
        if(this.videoUrl != null) { json.put("videoUrl", this.videoUrl); }

        // 视频缩略图URL
        if(this.videoPicUrl != null) { json.put("videoPicUrl", this.videoPicUrl); }

        // 关注类型 0:未关注   1:单向关注 2:双向关注
        json.put("type", this.type);

        // 评论数
        json.put("commentNum", this.commentNum);

        // 点赞数
        json.put("supportNum", this.supportNum);

        json.put("liveScreenType",this.liveScreenType);

        // 是否点赞  AVAILABLE：是   DELETED:否
        if(this.isSupport != null) { json.put("isSupport", this.isSupport); }

        // id
        json.put("id", this.id);

        // 直播id
        json.put("liveId", this.liveId);

        // 直播状态 1.直播  2.回复
        json.put("liveStatus", this.liveStatus);

        // 内容类型  2.UGC 3.直播
        json.put("contentType", this.contentType);
        // 短视频类型  4为直录短视频 5为上传短视频
        json.put("shortVideoType", this.shortVideoType);
        // 观看人数
        json.put("viewNum", this.viewNum);
        // 标签信息列表
        if (this.tagInfoList != null) {
            JSONArray tagInfoListArray = new JSONArray();
            for (TagInfo value : this.tagInfoList)
            {
                if (value != null) {
                    tagInfoListArray.put(value.serialize());
                }
            }
            json.put("tagInfoList", tagInfoListArray);
        }

        return json;
    }

}
