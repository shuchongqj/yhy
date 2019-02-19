package com.yhy.common.beans.net.model.tm;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:PackageDetailResult
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-12-22
 * Time:15:26
 * Version 1.1.0
 */


public class PackageDetailResult implements Serializable {
    private static final long serialVersionUID = 3469820955386238038L;

    /**
     * 包裹信息
     */
    public List<PackageDetail> packageDetail;
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static PackageDetailResult deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static PackageDetailResult deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            PackageDetailResult result = new PackageDetailResult();

            // 包裹信息
            JSONArray packageDetailArray = json.optJSONArray("packageDetail");
            if (packageDetailArray != null) {
                int len = packageDetailArray.length();
                result.packageDetail = new ArrayList<PackageDetail>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = packageDetailArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.packageDetail.add(PackageDetail.deserialize(jo));
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

        // 包裹信息
        if (this.packageDetail != null) {
            JSONArray packageDetailArray = new JSONArray();
            for (PackageDetail value : this.packageDetail)
            {
                if (value != null) {
                    packageDetailArray.put(value.serialize());
                }
            }
            json.put("packageDetail", packageDetailArray);
        }

        return json;
    }
}
