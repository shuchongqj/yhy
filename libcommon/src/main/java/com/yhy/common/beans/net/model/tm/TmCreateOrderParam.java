// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.tm;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TmCreateOrderParam implements Serializable {
    private static final long serialVersionUID = 4401725566428055827L;
    /**
     * 商品id 必填
     */
    public long itemId;

    /**
     * 是否含有sku
     */
    public boolean hasSku;

    /**
     * sku列表 有sku时必填
     */
    public List<TmItemSku> skuList;
    /**
     * 购买商品的数量 无sku时必填
     */
    public long buyAmount;

    /**
     * 优惠券id
     */
    public long voucherId;

    /**
     * 使用积分数量
     */
    public long usePoint;

    /**
     * 发票信息
     */
    public TmInvoice invoice;

    /**
     * 是否是实体商品
     */
    public boolean isEntity;

    /**
     * 付款后的返回给客户端的url 保留字段
     */
    public String returnUrlAfterPay;

    /**
     * 收货地址id
     */
    public long addressId;

    /**
     * 联系人id
     */
    public long contactId;

    /**
     * 联系人邮箱 线路订单填
     */
    public String email;

    /**
     * 游客id列表
     */
    public long[] touristIds;
    /**
     * 入住时间/入园时间
     */
    public long enterTime;

    /**
     * 离店时间
     */
    public long leaveTime;

    /**
     * 每天预订房间数
     */
    public int roomAmount;

    /**
     * 最晚到达时间
     */
    public String latestArriveTime;

    /**
     * 其他要求
     */
    public String otherInfo;

    /**
     * 入住人列表
     */
    public List<String> checkInNames;
    /**
     * 取票人姓名
     */
    public String contactName;

    /**
     * 联系人电话
     */
    public String contactPhone;

    /**
     * 活动id 活动订单必填
     */
    public long activityId;

    /**
     * 商品类型 NORMAL:普通商品交易/LINE：线路商品/HOTEL：酒店商品/SPOTS：景区门票/FLIGHT_HOTEL：机酒套餐/SPOTS_HOTEL:景酒套餐/ACTIVITY:活动商品/MEMBER_RECHARGE：会员充值/TOUR_LINE:跟团游/FREE_LINE:自由行/CITY_ACTIVITY:同城活动
     */
    public String itemType;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static TmCreateOrderParam deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static TmCreateOrderParam deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            TmCreateOrderParam result = new TmCreateOrderParam();

            // 商品id 必填
            result.itemId = json.optLong("itemId");
            // 是否含有sku
            result.hasSku = json.optBoolean("hasSku");
            // sku列表 有sku时必填
            JSONArray skuListArray = json.optJSONArray("skuList");
            if (skuListArray != null) {
                int len = skuListArray.length();
                result.skuList = new ArrayList<TmItemSku>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = skuListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.skuList.add(TmItemSku.deserialize(jo));
                    }
                }
            }

            // 购买商品的数量 无sku时必填
            result.buyAmount = json.optLong("buyAmount");
            // 优惠券id
            result.voucherId = json.optLong("voucherId");
            // 使用积分数量
            result.usePoint = json.optLong("usePoint");
            // 发票信息
            result.invoice = TmInvoice.deserialize(json.optJSONObject("invoice"));
            // 是否是实体商品
            result.isEntity = json.optBoolean("isEntity");
            // 付款后的返回给客户端的url 保留字段

            if (!json.isNull("returnUrlAfterPay")) {
                result.returnUrlAfterPay = json.optString("returnUrlAfterPay", null);
            }
            // 收货地址id
            result.addressId = json.optLong("addressId");
            // 联系人id
            result.contactId = json.optLong("contactId");
            // 联系人邮箱 线路订单填

            if (!json.isNull("email")) {
                result.email = json.optString("email", null);
            }
            // 游客id列表
            JSONArray touristIdsArray = json.optJSONArray("touristIds");
            if (touristIdsArray != null) {
                int len = touristIdsArray.length();
                result.touristIds = new long[len];
                for (int i = 0; i < len; i++) {
                    result.touristIds[i] = touristIdsArray.optLong(i);
                }
            }

            // 入住时间/入园时间
            result.enterTime = json.optLong("enterTime");
            // 离店时间
            result.leaveTime = json.optLong("leaveTime");
            // 每天预订房间数
            result.roomAmount = json.optInt("roomAmount");
            // 最晚到达时间-过期字段

            if (!json.isNull("latestArriveTime")) {
                result.latestArriveTime = json.optString("latestArriveTime", null);
            }
            // 其他要求

            if (!json.isNull("otherInfo")) {
                result.otherInfo = json.optString("otherInfo", null);
            }
            // 入住人列表
            JSONArray checkInNamesArray = json.optJSONArray("checkInNames");
            if (checkInNamesArray != null) {
                int len = checkInNamesArray.length();
                result.checkInNames = new
                        ArrayList<String>(len);
                for (int i = 0; i < len; i++) {

                    if (!checkInNamesArray.isNull(i)) {
                        result.checkInNames.add(checkInNamesArray.optString(i, null));
                    } else {
                        result.checkInNames.add(i, null);
                    }

                }
            }

            // 取票人姓名

            if (!json.isNull("contactName")) {
                result.contactName = json.optString("contactName", null);
            }
            // 联系人电话

            if (!json.isNull("contactPhone")) {
                result.contactPhone = json.optString("contactPhone", null);
            }
            // 活动id 活动订单必填
            result.activityId = json.optLong("activityId");
            // 商品类型 NORMAL:普通商品交易/LINE：线路商品/HOTEL：酒店商品/SPOTS：景区门票/FLIGHT_HOTEL：机酒套餐/SPOTS_HOTEL:景酒套餐/ACTIVITY:活动商品/MEMBER_RECHARGE：会员充值/TOUR_LINE:跟团游/FREE_LINE:自由行/CITY_ACTIVITY:同城活动

            if (!json.isNull("itemType")) {
                result.itemType = json.optString("itemType", null);
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

        // 商品id 必填
        json.put("itemId", this.itemId);

        // 是否含有sku
        json.put("hasSku", this.hasSku);

        // sku列表 有sku时必填 
        if (this.skuList != null) {
            JSONArray skuListArray = new JSONArray();
            for (TmItemSku value : this.skuList) {
                if (value != null) {
                    skuListArray.put(value.serialize());
                }
            }
            json.put("skuList", skuListArray);
        }

        // 购买商品的数量 无sku时必填
        json.put("buyAmount", this.buyAmount);

        // 优惠券id
        json.put("voucherId", this.voucherId);

        // 使用积分数量
        json.put("usePoint", this.usePoint);

        // 发票信息
        if (this.invoice != null) {
            json.put("invoice", this.invoice.serialize());
        }

        // 是否是实体商品
        json.put("isEntity", this.isEntity);

        // 付款后的返回给客户端的url 保留字段
        if (this.returnUrlAfterPay != null) {
            json.put("returnUrlAfterPay", this.returnUrlAfterPay);
        }

        // 收货地址id
        json.put("addressId", this.addressId);

        // 联系人id
        json.put("contactId", this.contactId);

        // 联系人邮箱 线路订单填
        if (this.email != null) {
            json.put("email", this.email);
        }

        // 游客id列表 
        if (this.touristIds != null) {
            JSONArray touristIdsArray = new JSONArray();
            for (long value : this.touristIds) {
                touristIdsArray.put(value);
            }
            json.put("touristIds", touristIdsArray);
        }

        // 入住时间/入园时间
        json.put("enterTime", this.enterTime);

        // 离店时间
        json.put("leaveTime", this.leaveTime);

        // 每天预订房间数
        json.put("roomAmount", this.roomAmount);

        // 最晚到达时间-过期字段
        if (this.latestArriveTime != null) {
            json.put("latestArriveTime", this.latestArriveTime);
        }
        // 其他要求
        if (this.otherInfo != null) {
            json.put("otherInfo", this.otherInfo);
        }

        // 入住人列表 
        if (this.checkInNames != null) {
            JSONArray checkInNamesArray = new JSONArray();
            for (String value : this.checkInNames) {
                checkInNamesArray.put(value);
            }
            json.put("checkInNames", checkInNamesArray);
        }

        // 取票人姓名
        if (this.contactName != null) {
            json.put("contactName", this.contactName);
        }

        // 联系人电话
        if (this.contactPhone != null) {
            json.put("contactPhone", this.contactPhone);
        }

        // 活动id 活动订单必填
        json.put("activityId", this.activityId);

        // 商品类型 NORMAL:普通商品交易/LINE：线路商品/HOTEL：酒店商品/SPOTS：景区门票/FLIGHT_HOTEL：机酒套餐/SPOTS_HOTEL:景酒套餐/ACTIVITY:活动商品/MEMBER_RECHARGE：会员充值/TOUR_LINE:跟团游/FREE_LINE:自由行/CITY_ACTIVITY:同城活动
        if (this.itemType != null) {
            json.put("itemType", this.itemType);
        }
        return json;
    }
}
  