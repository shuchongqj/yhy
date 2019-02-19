package com.yhy.common.beans.net.model.tm;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:Task
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-6-30
 * Time:15:37
 * Version 1.0
 * Description:
 */
public class Task implements Serializable{

    private static final long serialVersionUID = 6941122202306770032L;
    /**
     * 任务标题
     */
    public String name;

    /**
     * 可加积分数量
     */
    public int amount;

    /**
     * 没有积分数量显示标题
     */
    public String tiltle;

    /**
     * 任务状态 UNFINISHED:未完成  FINISHED:已完成 NO_SHOW:不显示
     */
    public String status;

    /**
     * 100 签到,101 发布动态,102 评论动态, 103 点赞, 104 分享每日步数,105 新用户注册,106 完善个人资料, 107 粉丝人数达到10人,108 粉丝人数达到30人,109 粉丝人数达到50人
     */
    public String code;

    /**
     * 跳转类型 NO_JUMP:不跳转,PAGE_FOUND:发现页,URL:jumpContent,PEDOMETER_HOME:计步器首页,PERSONAL_INFO:个人资料,ORDER_INFO:订单信息
     */
    public String jumpType;

    /**
     * 跳转内容 比如跳转类型是2的时候，这里就是url
     */
    public String jumpContent;

    /**
     * 图片地址
     */
    public String photosAddress;

    /**
     * 任务说明
     */
    public String explanation;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static Task deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static Task deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            Task result = new Task();

            // 任务标题

            if(!json.isNull("name")){
                result.name = json.optString("name", null);
            }
            // 可加积分数量
            result.amount = json.optInt("amount");
            // 没有积分数量显示标题

            if(!json.isNull("tiltle")){
                result.tiltle = json.optString("tiltle", null);
            }
            // 任务状态 UNFINISHED:未完成  FINISHED:已完成 NO_SHOW:不显示

            if(!json.isNull("status")){
                result.status = json.optString("status", null);
            }
            // 100 签到,101 发布动态,102 评论动态, 103 点赞, 104 分享每日步数,105 新用户注册,106 完善个人资料, 107 粉丝人数达到10人,108 粉丝人数达到30人,109 粉丝人数达到50人

            if(!json.isNull("code")){
                result.code = json.optString("code", null);
            }
            // 跳转类型 NO_JUMP:不跳转,PAGE_FOUND:发现页,URL:jumpContent,PEDOMETER_HOME:计步器首页,PERSONAL_INFO:个人资料,ORDER_INFO:订单信息

            if(!json.isNull("jumpType")){
                result.jumpType = json.optString("jumpType", null);
            }
            // 跳转内容 比如跳转类型是2的时候，这里就是url

            if(!json.isNull("jumpContent")){
                result.jumpContent = json.optString("jumpContent", null);
            }
            // 图片地址

            if(!json.isNull("photosAddress")){
                result.photosAddress = json.optString("photosAddress", null);
            }
            // 任务说明

            if(!json.isNull("explanation")){
                result.explanation = json.optString("explanation", null);
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

        // 任务标题
        if(this.name != null) { json.put("name", this.name); }

        // 可加积分数量
        json.put("amount", this.amount);

        // 没有积分数量显示标题
        if(this.tiltle != null) { json.put("tiltle", this.tiltle); }

        // 任务状态 UNFINISHED:未完成  FINISHED:已完成 NO_SHOW:不显示
        if(this.status != null) { json.put("status", this.status); }

        // 100 签到,101 发布动态,102 评论动态, 103 点赞, 104 分享每日步数,105 新用户注册,106 完善个人资料, 107 粉丝人数达到10人,108 粉丝人数达到30人,109 粉丝人数达到50人
        if(this.code != null) { json.put("code", this.code); }

        // 跳转类型 NO_JUMP:不跳转,PAGE_FOUND:发现页,URL:jumpContent,PEDOMETER_HOME:计步器首页,PERSONAL_INFO:个人资料,ORDER_INFO:订单信息
        if(this.jumpType != null) { json.put("jumpType", this.jumpType); }

        // 跳转内容 比如跳转类型是2的时候，这里就是url
        if(this.jumpContent != null) { json.put("jumpContent", this.jumpContent); }

        // 图片地址
        if(this.photosAddress != null) { json.put("photosAddress", this.photosAddress); }

        // 任务说明
        if(this.explanation != null) { json.put("explanation", this.explanation); }

        return json;
    }
}

