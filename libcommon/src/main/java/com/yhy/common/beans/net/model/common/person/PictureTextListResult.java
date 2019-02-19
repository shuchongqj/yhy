package com.yhy.common.beans.net.model.common.person;

import com.yhy.common.beans.net.model.common.PictureTextItemInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:PictureTextListResult
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-7-28
 * Time:11:47
 * Version 1.0
 * Description:
 */
public class PictureTextListResult implements Serializable{
    private static final long serialVersionUID = -2339135578969298092L;

    /**
     * 图文信息
     */
    public List<PictureTextItemInfo> pictureTextList;
    /**
     * 图文id
     */
    public long id;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static PictureTextListResult deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static PictureTextListResult deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            PictureTextListResult result = new PictureTextListResult();

            // 图文信息
            JSONArray pictureTextListArray = json.optJSONArray("pictureTextList");
            if (pictureTextListArray != null) {
                int len = pictureTextListArray.length();
                result.pictureTextList = new ArrayList<PictureTextItemInfo>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = pictureTextListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.pictureTextList.add(PictureTextItemInfo.deserialize(jo));
                    }
                }
            }

            // 图文id
            result.id = json.optLong("id");
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 图文信息
        if (this.pictureTextList != null) {
            JSONArray pictureTextListArray = new JSONArray();
            for (PictureTextItemInfo value : this.pictureTextList)
            {
                if (value != null) {
                    pictureTextListArray.put(value.serialize());
                }
            }
            json.put("pictureTextList", pictureTextListArray);
        }

        // 图文id
        json.put("id", this.id);

        return json;
    }
}
