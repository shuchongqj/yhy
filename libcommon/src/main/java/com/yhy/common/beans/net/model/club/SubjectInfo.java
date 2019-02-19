// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.club;

import com.yhy.common.beans.net.model.comment.ComTagInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SubjectInfo implements Serializable {

    private static final long serialVersionUID = -6446956067470938849L;

    /**
     * id
     */
    public long id;

    /**
     * 发表动态时存俱乐部Id-发表直播时是标签Id
     */
    public long objId;

    /**
     * 用户id
     */
    public long createId;

    /**
     * 文字
     */
    public String textContent;

    /**
     * 图片信息
     */
    public String picContent;

    /**
     * 图片集合
     */
    public List<String> picList;
    /**
     * 地理位置信息
     */
    public POIInfo poiInfo;

    /**
     * 用户信息
     */
    public SnsUserInfo userInfo;

    /**
     * 创建时间
     */
    public long gmtCreated;

    /**
     * 是否点赞 AVAILABLE 是 DELETED 否
     */
    public String isSupport;

    /**
     * 点赞数量
     */
    public int supportNum;

    /**
     * 评论数量
     */
    public int commentNum;

    /**
     * 标签信息列表
     */
    public List<ComTagInfo> comTagList;

    /**
     * 视频URL
     */
    public String videoUrl;

    /**
     * 视频缩略图URL
     */
    public String videoPicUrl;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static SubjectInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static SubjectInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            SubjectInfo result = new SubjectInfo();

            // id
            result.id = json.optLong("id");
            // 发表动态时存俱乐部Id-发表直播时是标签Id
            result.objId = json.optLong("objId");
            // 用户id
            result.createId = json.optLong("createId");
            // 文字

            if (!json.isNull("textContent")) {
                result.textContent = json.optString("textContent", null);
            }
            // 图片信息

            if (!json.isNull("picContent")) {
                result.picContent = json.optString("picContent", null);
            }
            // 图片集合
            JSONArray picListArray = json.optJSONArray("picList");
            if (picListArray != null) {
                int len = picListArray.length();
                result.picList = new
                        ArrayList<String>(len);
                for (int i = 0; i < len; i++) {

                    if (!picListArray.isNull(i)) {
                        result.picList.add(picListArray.optString(i, null));
                    } else {
                        result.picList.add(i, null);
                    }

                }
            }

            // 地理位置信息
            result.poiInfo = POIInfo.deserialize(json.optJSONObject("poiInfo"));
            // 用户信息
            result.userInfo = SnsUserInfo.deserialize(json.optJSONObject("userInfo"));
            // 创建时间
            result.gmtCreated = json.optLong("gmtCreated");
            // 是否点赞 AVAILABLE 是 DELETED 否

            if (!json.isNull("isSupport")) {
                result.isSupport = json.optString("isSupport", null);
            }
            // 点赞数量
            result.supportNum = json.optInt("supportNum");
            // 评论数量
            result.commentNum = json.optInt("commentNum");
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
            // 视频URL

            if (!json.isNull("videoUrl")) {
                result.videoUrl = json.optString("videoUrl", null);
            }
            // 视频缩略图URL

            if (!json.isNull("videoPicUrl")) {
                result.videoPicUrl = json.optString("videoPicUrl", null);
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

        // id
        json.put("id", this.id);

        // 发表动态时存俱乐部Id-发表直播时是标签Id
        json.put("objId", this.objId);

        // 用户id
        json.put("createId", this.createId);

        // 文字
        if (this.textContent != null) {
            json.put("textContent", this.textContent);
        }

        // 图片信息
        if (this.picContent != null) {
            json.put("picContent", this.picContent);
        }

        // 图片集合 
        if (this.picList != null) {
            JSONArray picListArray = new JSONArray();
            for (String value : this.picList) {
                picListArray.put(value);
            }
            json.put("picList", picListArray);
        }

        // 地理位置信息
        if (this.poiInfo != null) {
            json.put("poiInfo", this.poiInfo.serialize());
        }

        // 用户信息
        if (this.userInfo != null) {
            json.put("userInfo", this.userInfo.serialize());
        }

        // 创建时间
        json.put("gmtCreated", this.gmtCreated);

        // 是否点赞 AVAILABLE 是 DELETED 否
        if (this.isSupport != null) {
            json.put("isSupport", this.isSupport);
        }

        // 点赞数量
        json.put("supportNum", this.supportNum);

        // 评论数量
        json.put("commentNum", this.commentNum);

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
        // 视频URL
        if (this.videoUrl != null) {
            json.put("videoUrl", this.videoUrl);
        }

        // 视频缩略图URL
        if (this.videoPicUrl != null) {
            json.put("videoPicUrl", this.videoPicUrl);
        }

        return json;
    }
}
  