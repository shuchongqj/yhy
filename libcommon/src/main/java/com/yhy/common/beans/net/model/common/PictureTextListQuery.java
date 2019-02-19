package com.yhy.common.beans.net.model.common;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:PictureTextListQuery
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-7-27
 * Time:18:19
 * Version 1.0
 * Description:
 */
public class PictureTextListQuery implements Serializable{
    private static final long serialVersionUID = -5066412095038126318L;

    /**
     * 图文信息集合
     */
    public List<PictureTextItemInfo> pictureTextItemInfoList;
    /**
     * id
     */
    public long id;

    /**
     * 外部id
     */
    public long outId;

    /**
     * 图文类型  ITEM:商品,HOTEL:酒店,SCENIC:景区,SHOP:店铺,FOOD:美食,EXPERTPUBLISH:达人发布,EXPERTHOME:达人主页
     */
    public String outType;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static PictureTextListQuery deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static PictureTextListQuery deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            PictureTextListQuery result = new PictureTextListQuery();

            // 图文信息集合
            JSONArray pictureTextItemInfoListArray = json.optJSONArray("pictureTextItemInfoList");
            if (pictureTextItemInfoListArray != null) {
                int len = pictureTextItemInfoListArray.length();
                result.pictureTextItemInfoList = new ArrayList<PictureTextItemInfo>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = pictureTextItemInfoListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.pictureTextItemInfoList.add(PictureTextItemInfo.deserialize(jo));
                    }
                }
            }

            // id
            result.id = json.optLong("id");
            // 外部id
            result.outId = json.optLong("outId");
            // 图文类型  ITEM:商品,HOTEL:酒店,SCENIC:景区,SHOP:店铺,FOOD:美食,EXPERTPUBLISH:达人发布,EXPERTHOME:达人主页

            if(!json.isNull("outType")){
                result.outType = json.optString("outType", null);
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

        // 图文信息集合
        if (this.pictureTextItemInfoList != null) {
            JSONArray pictureTextItemInfoListArray = new JSONArray();
            for (PictureTextItemInfo value : this.pictureTextItemInfoList)
            {
                if (value != null) {
                    pictureTextItemInfoListArray.put(value.serialize());
                }
            }
            json.put("pictureTextItemInfoList", pictureTextItemInfoListArray);
        }

        // id
        json.put("id", this.id);

        // 外部id
        json.put("outId", this.outId);

        // 图文类型  ITEM:商品,HOTEL:酒店,SCENIC:景区,SHOP:店铺,FOOD:美食,EXPERTPUBLISH:达人发布,EXPERTHOME:达人主页
        if(this.outType != null) { json.put("outType", this.outType); }

        return json;
    }
}
