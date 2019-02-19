package com.yhy.common.beans.net.model.consult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:ProcessStateQuery
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-8-17
 * Time:19:03
 * Version 1.1.0
 */

public class ProcessStateQuery implements Serializable {
    private static final long serialVersionUID = -6626426325971980338L;
    /**
     * 消息发送者id
     */
    public long fromUserId;

    /**
     * 商品id
     */
    public long itemId;

    /**
     * 消息接受者id
     */
    public long toUserId;

    /**
     * 服务流程类型 达人咨询 CONSULT
     */
    public String processType;

    /**
     * 服务流程状态 NOT_AVAILABLE:未生效    WAITING_PAY:待付款    CANCEL:取消    FINISH:结束   RATED:已评价   CONSULT_IN_QUEUE:排队中  CONSULT_IN_CHAT:咨询中
     */
    public List<String> processStatus;
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static ProcessStateQuery deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static ProcessStateQuery deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            ProcessStateQuery result = new ProcessStateQuery();

            // 消息发送者id
            result.fromUserId = json.optLong("fromUserId");
            // 商品id
            result.itemId = json.optLong("itemId");
            // 消息接受者id
            result.toUserId = json.optLong("toUserId");
            // 服务流程类型 达人咨询 CONSULT

            if(!json.isNull("processType")){
                result.processType = json.optString("processType", null);
            }
            // 服务流程状态 NOT_AVAILABLE:未生效    WAITING_PAY:待付款    CANCEL:取消    FINISH:结束   RATED:已评价   CONSULT_IN_QUEUE:排队中  CONSULT_IN_CHAT:咨询中
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

            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 消息发送者id
        json.put("fromUserId", this.fromUserId);

        // 商品id
        json.put("itemId", this.itemId);

        // 消息接受者id
        json.put("toUserId", this.toUserId);

        // 服务流程类型 达人咨询 CONSULT
        if(this.processType != null) { json.put("processType", this.processType); }

        // 服务流程状态 NOT_AVAILABLE:未生效    WAITING_PAY:待付款    CANCEL:取消    FINISH:结束   RATED:已评价   CONSULT_IN_QUEUE:排队中  CONSULT_IN_CHAT:咨询中
        if (this.processStatus != null) {
            JSONArray processStatusArray = new JSONArray();
            for (String value : this.processStatus)
            {
                processStatusArray.put(value);
            }
            json.put("processStatus", processStatusArray);
        }

        return json;
    }
}
