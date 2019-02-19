package com.yhy.common.beans.net.model.pedometer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:InviteShareInfo
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-7-6
 * Time:16:39
 * Version 1.0
 * Description:
 */
public class InviteShareInfo implements Serializable{

    private static final long serialVersionUID = 9040204427852804706L;

    /**
     * 总积分
     */
    public long totalPoint;

    /**
     * 累计步数
     */
    public long totalStep;

    /**
     * 邀请url
     */
    public String inviteUrl;

    /**
     * 参与次数
     */
    public int collectCount;

    /**
     * 主文案信息，粗体那个
     */
    public String mainCopy;

    /**
     * 子文案信息
     */
    public String subCopy;

    /**
     * 邀请文案信息，邀请一个朋友+500积分
     */
    public String inviteCopy;

    /**
     * 分享的标题
     */
    public String shareTitle;

    /**
     * 分享的内容
     */
    public String shareContent;

    /**
     * 分享的图片链接
     */
    public String shareImageUrl;

    /**
     * 分享的网页地址
     */
    public String shareWebPage;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static InviteShareInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static InviteShareInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            InviteShareInfo result = new InviteShareInfo();

            // 总积分
            result.totalPoint = json.optLong("totalPoint");
            // 累计步数
            result.totalStep = json.optLong("totalStep");
            // 邀请url

            if(!json.isNull("inviteUrl")){
                result.inviteUrl = json.optString("inviteUrl", null);
            }
            // 参与次数
            result.collectCount = json.optInt("collectCount");
            // 主文案信息，粗体那个

            if(!json.isNull("mainCopy")){
                result.mainCopy = json.optString("mainCopy", null);
            }
            // 子文案信息

            if(!json.isNull("subCopy")){
                result.subCopy = json.optString("subCopy", null);
            }
            // 邀请文案信息，邀请一个朋友+500积分

            if(!json.isNull("inviteCopy")){
                result.inviteCopy = json.optString("inviteCopy", null);
            }
            // 分享的标题

            if(!json.isNull("shareTitle")){
                result.shareTitle = json.optString("shareTitle", null);
            }
            // 分享的内容

            if(!json.isNull("shareContent")){
                result.shareContent = json.optString("shareContent", null);
            }
            // 分享的图片链接

            if(!json.isNull("shareImageUrl")){
                result.shareImageUrl = json.optString("shareImageUrl", null);
            }
            // 分享的网页地址

            if(!json.isNull("shareWebPage")){
                result.shareWebPage = json.optString("shareWebPage", null);
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

        // 总积分
        json.put("totalPoint", this.totalPoint);

        // 累计步数
        json.put("totalStep", this.totalStep);

        // 邀请url
        if(this.inviteUrl != null) { json.put("inviteUrl", this.inviteUrl); }

        // 参与次数
        json.put("collectCount", this.collectCount);

        // 主文案信息，粗体那个
        if(this.mainCopy != null) { json.put("mainCopy", this.mainCopy); }

        // 子文案信息
        if(this.subCopy != null) { json.put("subCopy", this.subCopy); }

        // 邀请文案信息，邀请一个朋友+500积分
        if(this.inviteCopy != null) { json.put("inviteCopy", this.inviteCopy); }

        // 分享的标题
        if(this.shareTitle != null) { json.put("shareTitle", this.shareTitle); }

        // 分享的内容
        if(this.shareContent != null) { json.put("shareContent", this.shareContent); }

        // 分享的图片链接
        if(this.shareImageUrl != null) { json.put("shareImageUrl", this.shareImageUrl); }

        // 分享的网页地址
        if(this.shareWebPage != null) { json.put("shareWebPage", this.shareWebPage); }

        return json;
    }

}
