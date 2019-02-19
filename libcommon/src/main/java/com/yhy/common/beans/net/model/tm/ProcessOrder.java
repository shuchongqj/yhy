package com.yhy.common.beans.net.model.tm;

import com.yhy.common.beans.net.model.user.UserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:ProcessOrder
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-7-19
 * Time:17:26
 * Version 1.0
 * Description:
 */
public class ProcessOrder implements Serializable{

    private static final long serialVersionUID = 1426082898392992726L;

    /**
     * 流程单id
     */
    public long processOrderId;

    /**
     * 流程单类型
     */
    public String processOrderType;

    /**
     * 流程单状态
     */
    public String processOrderStatus;

    /**
     * 买家信息
     */
    public UserInfo buyerInfo;

    /**
     * 卖家信息
     */
    public UserInfo sellerInfo;

    /**
     * 达人信息
     */
    public UserInfo consultUserInfo;

    /**
     * 商品id
     */
    public long itemId;

    /**
     * 商品名称
     */
    public String itemTitle;

    /**
     * 商品图片
     */
    public String itemPic;

    /**
     * 商品原价
     */
    public long totalFee;

    /**
     * 商品目的地列表
     */
    public List<String> itemDestination;
    /**
     * 总金额 分
     */
    public long actualTotalFee;

    /**
     * 使用积分数量 现价
     */
    public long usePointNum;

    /**
     * 服务总时长 秒
     */
    public long serveTime;

    /**
     * 下单时间
     */
    public long createTime;

    /**
     * 流程单详细内容
     */
    public ProcessOrderContent processOrderContent;
    /**
     * 订单完成类型    USER_APP:用户手动结束,TIME_OUT:超时系统关闭
     */
    public String finishOrderSource;
    /**
     * 流程项信息
     */
    public ProcessOrderItem processOrderItem;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static ProcessOrder deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static ProcessOrder deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            ProcessOrder result = new ProcessOrder();

            // 流程单id
            result.processOrderId = json.optLong("processOrderId");
            // 流程单类型

            if(!json.isNull("processOrderType")){
                result.processOrderType = json.optString("processOrderType", null);
            }
            // 流程单状态

            if(!json.isNull("processOrderStatus")){
                result.processOrderStatus = json.optString("processOrderStatus", null);
            }
            // 买家信息
            result.buyerInfo = UserInfo.deserialize(json.optJSONObject("buyerInfo"));
            // 卖家信息
            result.sellerInfo = UserInfo.deserialize(json.optJSONObject("sellerInfo"));
            // 达人信息
            result.consultUserInfo = UserInfo.deserialize(json.getJSONObject("consultUserInfo"));
            // 商品id
            result.itemId = json.optLong("itemId");
            // 商品名称

            if(!json.isNull("itemTitle")){
                result.itemTitle = json.optString("itemTitle", null);
            }
            // 商品图片

            if(!json.isNull("itemPic")){
                result.itemPic = json.optString("itemPic", null);
            }
            // 商品原价
            result.totalFee = json.optLong("totalFee");
            // 商品目的地列表
            JSONArray itemDestinationArray = json.optJSONArray("itemDestination");
            if (itemDestinationArray != null) {
                int len = itemDestinationArray.length();
                result.itemDestination = new
                        ArrayList<String>(len);
                for (int i = 0; i < len; i++) {

                    if(!itemDestinationArray.isNull(i)){
                        result.itemDestination.add(itemDestinationArray.optString(i, null));
                    }else{
                        result.itemDestination.add(i, null);
                    }

                }
            }
            if (!json.isNull("finishOrderSource"))
                result.finishOrderSource = json.optString("finishOrderSource", null);
            // 总金额 分
            result.actualTotalFee = json.optLong("actualTotalFee");
            // 使用积分数量 现价
            result.usePointNum = json.optLong("usePointNum");
            // 服务总时长 秒
            result.serveTime = json.optLong("serveTime");
            // 下单时间
            result.createTime = json.optLong("createTime");
            // 流程单详细内容
            result.processOrderContent = ProcessOrderContent.deserialize(json.optJSONObject("processOrderContent"));

            // 流程项信息
            result.processOrderItem = ProcessOrderItem.deserialize(json.optJSONObject("processOrderItem"));
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 流程单id
        json.put("processOrderId", this.processOrderId);

        // 流程单类型
        if(this.processOrderType != null) { json.put("processOrderType", this.processOrderType); }

        // 流程单状态
        if(this.processOrderStatus != null) { json.put("processOrderStatus", this.processOrderStatus); }

        // 买家信息
        if (this.buyerInfo != null) { json.put("buyerInfo", this.buyerInfo.serialize()); }

        // 卖家信息
        if (this.sellerInfo != null) { json.put("sellerInfo", this.sellerInfo.serialize()); }

        // 商品id
        json.put("itemId", this.itemId);

        // 商品名称
        if(this.itemTitle != null) { json.put("itemTitle", this.itemTitle); }

        // 商品图片
        if(this.itemPic != null) { json.put("itemPic", this.itemPic); }

        // 商品原价
        json.put("totalFee", this.totalFee);

        // 商品目的地列表
        if (this.itemDestination != null) {
            JSONArray itemDestinationArray = new JSONArray();
            for (String value : this.itemDestination)
            {
                itemDestinationArray.put(value);
            }
            json.put("itemDestination", itemDestinationArray);
        }

        // 总金额 分
        json.put("actualTotalFee", this.actualTotalFee);

        // 使用积分数量 现价
        json.put("usePointNum", this.usePointNum);

        // 服务总时长 秒
        json.put("serveTime", this.serveTime);

        // 下单时间
        json.put("createTime", this.createTime);
        json.put("finishOrderSource",this.finishOrderSource);
        // 流程单详细内容
        if (this.processOrderContent != null) { json.put("processOrderContent", this.processOrderContent.serialize()); }
        // 流程项信息
        if (this.processOrderItem != null) { json.put("processOrderItem", this.processOrderItem.serialize()); }
        return json;
    }
}
