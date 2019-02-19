// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.tm;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TmDetailOrder implements Serializable {

    private static final long serialVersionUID = -2208062574767109122L;
    /**
     * 交易订单基础信息
     */
    public TmBizOrder bizOrder;

    /**
     * 商品id
     */
    public long itemId;

    /**
     * 商品名称
     */
    public String itemTitle;

    /**
     * 商品子标题
     */
    public String itemSubTitle;

    /**
     * SKU标题
     */
    public String skuTitle;

    /**
     * 商品图片
     */
    public String itemPic;

    /**
     * 交易价格
     */
    public long itemPrice;

    /**
     * 父订单id
     */
    public long parentId;

    /**
     * 活动内容
     */
    public String activityContent;

    /**
     * 活动时间
     */
    public String activityTime;

    /**
     * 套餐类型
     */
    public String packageType;

    /**
     * 人员类型
     */
    public String personType;

    /**
     * 出行日期/入住日期/入园日期
     */
    public long startDate;

    /**
     * 早餐情况 NONE:不含早 SINGLE:含早 DOUBLE:含双早 MULTI:含多早
     */
    public String orderBreakfast;

    /**
     * 服务时长
     */
    public long serveTime;

    /**
     * 服务区域
     */
    public List<String> itemDestination;
    /**
     * 商品原价
     */
    public long itemOriginalPrice;


    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static TmDetailOrder deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static TmDetailOrder deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            TmDetailOrder result = new TmDetailOrder();

            // 交易订单基础信息
            result.bizOrder = TmBizOrder.deserialize(json.optJSONObject("bizOrder"));
            // 商品id
            result.itemId = json.optLong("itemId");
            // 商品名称

            if (!json.isNull("itemTitle")) {
                result.itemTitle = json.optString("itemTitle", null);
            }
            // 商品子标题

            if (!json.isNull("itemSubTitle")) {
                result.itemSubTitle = json.optString("itemSubTitle", null);
            }
            // SKU标题

            if (!json.isNull("skuTitle")) {
                result.skuTitle = json.optString("skuTitle", null);
            }
            // 商品图片

            if (!json.isNull("itemPic")) {
                result.itemPic = json.optString("itemPic", null);
            }
            // 交易价格
            result.itemPrice = json.optLong("itemPrice");
            // 父订单id
            result.parentId = json.optLong("parentId");
            // 活动内容

            if (!json.isNull("activityContent")) {
                result.activityContent = json.optString("activityContent", null);
            }
            // 活动时间

            if (!json.isNull("activityTime")) {
                result.activityTime = json.optString("activityTime", null);
            }
            // 套餐类型

            if (!json.isNull("packageType")) {
                result.packageType = json.optString("packageType", null);
            }
            // 人员类型

            if (!json.isNull("personType")) {
                result.personType = json.optString("personType", null);
            }

            // 出行日期/入住日期/入园日期
            result.startDate = json.optLong("startDate");
            // 早餐情况 NONE:不含早 SINGLE:含早 DOUBLE:含双早 MULTI:含多早

            if (!json.isNull("orderBreakfast")) {
                result.orderBreakfast = json.optString("orderBreakfast", null);
            }

            // 服务时长
            result.serveTime = json.optLong("serveTime");
            // 服务区域
            JSONArray itemDestinationArray = json.optJSONArray("itemDestination");
            if (itemDestinationArray != null) {
                int len = itemDestinationArray.length();
                result.itemDestination = new
                        ArrayList<String>(len);
                for (int i = 0; i < len; i++) {

                    if (!itemDestinationArray.isNull(i)) {
                        result.itemDestination.add(itemDestinationArray.optString(i, null));
                    } else {
                        result.itemDestination.add(i, null);
                    }

                }
            }

            // 商品原价
            result.itemOriginalPrice = json.optLong("itemOriginalPrice");
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 交易订单基础信息
        if (this.bizOrder != null) {
            json.put("bizOrder", this.bizOrder.serialize());
        }

        // 商品id
        json.put("itemId", this.itemId);

        // 商品名称
        if (this.itemTitle != null) {
            json.put("itemTitle", this.itemTitle);
        }

        // 商品子标题
        if (this.itemSubTitle != null) {
            json.put("itemSubTitle", this.itemSubTitle);
        }

        // SKU标题
        if (this.skuTitle != null) {
            json.put("skuTitle", this.skuTitle);
        }

        // 商品图片
        if (this.itemPic != null) {
            json.put("itemPic", this.itemPic);
        }

        // 交易价格
        json.put("itemPrice", this.itemPrice);

        // 父订单id
        json.put("parentId", this.parentId);

        // 活动内容
        if (this.activityContent != null) {
            json.put("activityContent", this.activityContent);
        }

        // 活动时间
        if (this.activityTime != null) {
            json.put("activityTime", this.activityTime);
        }

        // 套餐类型
        if (this.packageType != null) {
            json.put("packageType", this.packageType);
        }

        // 人员类型
        if (this.personType != null) {
            json.put("personType", this.personType);
        }

        // 出行日期/入住日期/入园日期
        json.put("startDate", this.startDate);

        // 早餐情况 NONE:不含早 SINGLE:含早 DOUBLE:含双早 MULTI:含多早
        if (this.orderBreakfast != null) {
            json.put("orderBreakfast", this.orderBreakfast);
        }

        // 服务时长
        json.put("serveTime", this.serveTime);

        // 服务区域
        if (this.itemDestination != null) {
            JSONArray itemDestinationArray = new JSONArray();
            for (String value : this.itemDestination) {
                itemDestinationArray.put(value);
            }
            json.put("itemDestination", itemDestinationArray);
        }

        // 商品原价
        json.put("itemOriginalPrice", this.itemOriginalPrice);
        return json;
    }
}
  