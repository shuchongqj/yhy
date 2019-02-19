package com.yhy.common.beans.net.model.tm;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:CreateBatchOrderParam
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-12-21
 * Time:19:15
 * Version 1.1.0
 */


public class CreateBatchOrderParam implements Serializable {
    private static final long serialVersionUID = 6670344097269466743L;

    /**
     * 购买商品信息
     */
    public List<ItemParamForCreateOrder> itemParamList;
    /**
     * 买家留言
     */
    public List<LeaveMassage> leaveMassageList;
    /**
     * 优惠券
     */
    public List<Voucher> voucherList;
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
    public static CreateBatchOrderParam deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static CreateBatchOrderParam deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            CreateBatchOrderParam result = new CreateBatchOrderParam();

            // 购买商品信息
            JSONArray itemParamListArray = json.optJSONArray("itemParamList");
            if (itemParamListArray != null) {
                int len = itemParamListArray.length();
                result.itemParamList = new ArrayList<ItemParamForCreateOrder>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = itemParamListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.itemParamList.add(ItemParamForCreateOrder.deserialize(jo));
                    }
                }
            }

            // 买家留言
            JSONArray leaveMassageListArray = json.optJSONArray("leaveMassageList");
            if (leaveMassageListArray != null) {
                int len = leaveMassageListArray.length();
                result.leaveMassageList = new ArrayList<LeaveMassage>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = leaveMassageListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.leaveMassageList.add(LeaveMassage.deserialize(jo));
                    }
                }
            }

            // 优惠券
            JSONArray voucherListArray = json.optJSONArray("voucherList");
            if (voucherListArray != null) {
                int len = voucherListArray.length();
                result.voucherList = new ArrayList<Voucher>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = voucherListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.voucherList.add(Voucher.deserialize(jo));
                    }
                }
            }

            // 使用积分数量
            result.usePoint = json.optLong("usePoint");
            // 发票信息
            result.invoice = TmInvoice.deserialize(json.optJSONObject("invoice"));
            // 是否是实体商品
            result.isEntity = json.optBoolean("isEntity");
            // 付款后的返回给客户端的url 保留字段

            if(!json.isNull("returnUrlAfterPay")){
                result.returnUrlAfterPay = json.optString("returnUrlAfterPay", null);
            }
            // 收货地址id
            result.addressId = json.optLong("addressId");
            // 联系人id
            result.contactId = json.optLong("contactId");
            // 联系人邮箱 线路订单填

            if(!json.isNull("email")){
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
            // 最晚到达时间

            if(!json.isNull("latestArriveTime")){
                result.latestArriveTime = json.optString("latestArriveTime", null);
            }
            // 其他要求

            if(!json.isNull("otherInfo")){
                result.otherInfo = json.optString("otherInfo", null);
            }
            // 入住人列表
            JSONArray checkInNamesArray = json.optJSONArray("checkInNames");
            if (checkInNamesArray != null) {
                int len = checkInNamesArray.length();
                result.checkInNames = new
                        ArrayList<String>(len);
                for (int i = 0; i < len; i++) {

                    if(!checkInNamesArray.isNull(i)){
                        result.checkInNames.add(checkInNamesArray.optString(i, null));
                    }else{
                        result.checkInNames.add(i, null);
                    }

                }
            }

            // 取票人姓名

            if(!json.isNull("contactName")){
                result.contactName = json.optString("contactName", null);
            }
            // 联系人电话

            if(!json.isNull("contactPhone")){
                result.contactPhone = json.optString("contactPhone", null);
            }
            // 活动id 活动订单必填
            result.activityId = json.optLong("activityId");
            // 商品类型 NORMAL:普通商品交易/LINE：线路商品/HOTEL：酒店商品/SPOTS：景区门票/FLIGHT_HOTEL：机酒套餐/SPOTS_HOTEL:景酒套餐/ACTIVITY:活动商品/MEMBER_RECHARGE：会员充值/TOUR_LINE:跟团游/FREE_LINE:自由行/CITY_ACTIVITY:同城活动

            if(!json.isNull("itemType")){
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

        // 购买商品信息
        if (this.itemParamList != null) {
            JSONArray itemParamListArray = new JSONArray();
            for (ItemParamForCreateOrder value : this.itemParamList)
            {
                if (value != null) {
                    itemParamListArray.put(value.serialize());
                }
            }
            json.put("itemParamList", itemParamListArray);
        }

        // 买家留言
        if (this.leaveMassageList != null) {
            JSONArray leaveMassageListArray = new JSONArray();
            for (LeaveMassage value : this.leaveMassageList)
            {
                if (value != null) {
                    leaveMassageListArray.put(value.serialize());
                }
            }
            json.put("leaveMassageList", leaveMassageListArray);
        }

        // 优惠券
        if (this.voucherList != null) {
            JSONArray voucherListArray = new JSONArray();
            for (Voucher value : this.voucherList)
            {
                if (value != null) {
                    voucherListArray.put(value.serialize());
                }
            }
            json.put("voucherList", voucherListArray);
        }

        // 使用积分数量
        json.put("usePoint", this.usePoint);

        // 发票信息
        if (this.invoice != null) { json.put("invoice", this.invoice.serialize()); }

        // 是否是实体商品
        json.put("isEntity", this.isEntity);

        // 付款后的返回给客户端的url 保留字段
        if(this.returnUrlAfterPay != null) { json.put("returnUrlAfterPay", this.returnUrlAfterPay); }

        // 收货地址id
        json.put("addressId", this.addressId);

        // 联系人id
        json.put("contactId", this.contactId);

        // 联系人邮箱 线路订单填
        if(this.email != null) { json.put("email", this.email); }

        // 游客id列表
        if (this.touristIds != null) {
            JSONArray touristIdsArray = new JSONArray();
            for (long value : this.touristIds)
            {
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

        // 最晚到达时间
        if(this.latestArriveTime != null) { json.put("latestArriveTime", this.latestArriveTime); }

        // 其他要求
        if(this.otherInfo != null) { json.put("otherInfo", this.otherInfo); }

        // 入住人列表
        if (this.checkInNames != null) {
            JSONArray checkInNamesArray = new JSONArray();
            for (String value : this.checkInNames)
            {
                checkInNamesArray.put(value);
            }
            json.put("checkInNames", checkInNamesArray);
        }

        // 取票人姓名
        if(this.contactName != null) { json.put("contactName", this.contactName); }

        // 联系人电话
        if(this.contactPhone != null) { json.put("contactPhone", this.contactPhone); }

        // 活动id 活动订单必填
        json.put("activityId", this.activityId);

        // 商品类型 NORMAL:普通商品交易/LINE：线路商品/HOTEL：酒店商品/SPOTS：景区门票/FLIGHT_HOTEL：机酒套餐/SPOTS_HOTEL:景酒套餐/ACTIVITY:活动商品/MEMBER_RECHARGE：会员充值/TOUR_LINE:跟团游/FREE_LINE:自由行/CITY_ACTIVITY:同城活动
        if(this.itemType != null) { json.put("itemType", this.itemType); }

        return json;
    }
}
