package com.yhy.common.beans.net.model.paycore;

//import com.smart.sdk.api.resp.Api_PAYCORE_Bill;
//import com.smart.sdk.api.resp.Api_PAYCORE_BillList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:BillList
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-8-13
 * Time:12:25
 * Version 1.1.0
 */

public class BillList implements Serializable {
    private static final long serialVersionUID = 4871341628585473782L;

    /**
     * 收支列表
     */
    public List<Bill> billList;
    /**
     * 总数
     */
    public int totalCount;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static BillList deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static BillList deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            BillList result = new BillList();

            // 收支列表
            JSONArray billListArray = json.optJSONArray("billList");
            if (billListArray != null) {
                int len = billListArray.length();
                result.billList = new ArrayList<Bill>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = billListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.billList.add(Bill.deserialize(jo));
                    }
                }
            }

            // 总数
            result.totalCount = json.optInt("totalCount");
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 收支列表
        if (this.billList != null) {
            JSONArray billListArray = new JSONArray();
            for (Bill value : this.billList)
            {
                if (value != null) {
                    billListArray.put(value.serialize());
                }
            }
            json.put("billList", billListArray);
        }

        // 总数
        json.put("totalCount", this.totalCount);

        return json;
    }

}
