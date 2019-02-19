package com.yhy.common.beans.net.model.paycore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:BankNameList
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-9-22
 * Time:13:50
 * Version 1.1.0
 */

public class BankNameList implements Serializable {
    private static final long serialVersionUID = 6412133908166097622L;

    /**
     * 银行名列表
     */
    public List<String> bankNameList;
    /**
     * 总数
     */
    public int totalCount;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static BankNameList deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static BankNameList deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            BankNameList result = new BankNameList();

            // 银行名列表
            JSONArray bankNameListArray = json.optJSONArray("bankNameList");
            if (bankNameListArray != null) {
                int len = bankNameListArray.length();
                result.bankNameList = new
                        ArrayList<String>(len);
                for (int i = 0; i < len; i++) {

                    if(!bankNameListArray.isNull(i)){
                        result.bankNameList.add(bankNameListArray.optString(i, null));
                    }else{
                        result.bankNameList.add(i, null);
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

        // 银行名列表
        if (this.bankNameList != null) {
            JSONArray bankNameListArray = new JSONArray();
            for (String value : this.bankNameList)
            {
                bankNameListArray.put(value);
            }
            json.put("bankNameList", bankNameListArray);
        }

        // 总数
        json.put("totalCount", this.totalCount);

        return json;
    }

}
