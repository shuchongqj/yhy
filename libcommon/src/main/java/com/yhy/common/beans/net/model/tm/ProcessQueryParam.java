package com.yhy.common.beans.net.model.tm;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:ProcessQueryParam
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-8-17
 * Time:18:51
 * Version 1.1.0
 */

public class ProcessQueryParam implements Serializable {
    private static final long serialVersionUID = 8055916042429684737L;

    /**
     * 订单类型   CONSULT:咨询订单
     */
    public List<String> processType;
    /**
     * 订单状态   NOT_AVAILABLE:未生效    WAITING_PAY:待付款    CANCEL:取消    FINISH:结束   RATED:已评价   CONSULT_IN_QUEUE:排队中  CONSULT_IN_CHAT:咨询中
     */
    public List<String> processStatus;
    /**
     * 查询起始页
     */
    public int pageNo;

    /**
     * 每页显示数目
     */
    public int pageSize;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static ProcessQueryParam deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static ProcessQueryParam deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            ProcessQueryParam result = new ProcessQueryParam();

            // 订单类型   CONSULT:咨询订单
            JSONArray processTypeArray = json.optJSONArray("processType");
            if (processTypeArray != null) {
                int len = processTypeArray.length();
                result.processType = new
                        ArrayList<String>(len);
                for (int i = 0; i < len; i++) {

                    if(!processTypeArray.isNull(i)){
                        result.processType.add(processTypeArray.optString(i, null));
                    }else{
                        result.processType.add(i, null);
                    }

                }
            }

            // 订单状态   NOT_AVAILABLE:未生效    WAITING_PAY:待付款    CANCEL:取消    FINISH:结束   RATED:已评价   CONSULT_IN_QUEUE:排队中  CONSULT_IN_CHAT:咨询中
            JSONArray processStatusArray = json.optJSONArray("processStatus");
            if (processStatusArray != null) {
                int len = processStatusArray.length();
                result.processStatus = new
                        ArrayList<String>(len);
                for (int i = 0; i < len; i++) {

                    if(!processStatusArray.isNull(i)){
                        result.processStatus.add(processStatusArray.optString(i, null));
                    }else{
                        result.processStatus.add(i, null);
                    }

                }
            }

            // 查询起始页
            result.pageNo = json.optInt("pageNo");
            // 每页显示数目
            result.pageSize = json.optInt("pageSize");
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 订单类型   CONSULT:咨询订单
        if (this.processType != null) {
            JSONArray processTypeArray = new JSONArray();
            for (String value : this.processType)
            {
                processTypeArray.put(value);
            }
            json.put("processType", processTypeArray);
        }

        // 订单状态   NOT_AVAILABLE:未生效    WAITING_PAY:待付款    CANCEL:取消    FINISH:结束   RATED:已评价   CONSULT_IN_QUEUE:排队中  CONSULT_IN_CHAT:咨询中
        if (this.processStatus != null) {
            JSONArray processStatusArray = new JSONArray();
            for (String value : this.processStatus)
            {
                processStatusArray.put(value);
            }
            json.put("processStatus", processStatusArray);
        }

        // 查询起始页
        json.put("pageNo", this.pageNo);

        // 每页显示数目
        json.put("pageSize", this.pageSize);

        return json;
    }
}
