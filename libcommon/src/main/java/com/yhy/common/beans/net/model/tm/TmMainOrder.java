// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.tm;


import com.smart.sdk.api.resp.Api_ITEMS_OrgInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TmMainOrder implements Serializable {
    private static final long serialVersionUID = 3183044322922864645L;
    /**
     * 交易订单基础信息
     */
    public TmBizOrder bizOrder;

    /**
     * 交易子订单列表
     */
    public List<TmDetailOrder> detailOrders;
    /**
     * 订单按钮状态
     */
    public TmButtonStatus buttonStatus;

    /**
     * 物流订单列表 暂时不用
     */
    public TmLgOrderList lgOrderList;

    /**
     * 订单物流信息 暂时不用
     */
    public TmLogisticsOrder logisticsOrder;

    /**
     * 实际支付订单金额
     */
    public long totalFee;

    /**
     * 原始订单金额
     */
    public long originalTotalFee;

    /**
     * 优惠券优惠金额
     */
    public long voucherDiscountFee;

    /**
     * 完成时间/交易关闭时间/收货时间
     */
    public long completionTime;

    /**
     * 发货时间/确认时间
     */
    public long deliveryTime;

    /**
     * 入园时间
     */
    public long scenicEnterTime;

    /**
     * 出行时间
     */
    public long departTime;

    /**
     * 入店时间
     */
    public long checkInTime;

    /**
     * 离店时间
     */
    public long checkOutTime;

    /**
     * 最晚到店时间
     */
    public String latestArriveTime;

    /**
     * 每天预订房间数
     */
    public int roomAmount;

    /**
     * 游客信息列表
     */
    public List<TmUserContactInfo> touristList;
    /**
     * 联系人信息
     */
    public TmUserContactInfo contactInfo;

    /**
     * 联系人邮箱
     */
    public String email;

    /**
     * 收货地址
     */
    public Address address;

    /**
     * 其他要求
     */
    public String otherInfo;

    /**
     * 客服电话
     */
    public String servicePhone;

    /**
     * 关闭原因
     */
    public String closeReason;

    /**
     * 店铺信息
     */
    public TmMerchantInfo merchantInfo;

    /**
     * 支付方式 ONLINE_PAY:在线付 OFFLINE_PAY:线下付
     */
    public String payType;

    /**
     * 酒店名称
     */
    public String hotelTitle;

    /**
     * 酒店id
     */
    public long hotelId;

    /**
     * 酒店电话列表 当前只取第一个
     */
    public List<String> hotelPhone;
    /**
     * 房间名称
     */
    public String roomTitle;

    /**
     * 景区id
     */
    public long scenicId;

    /**
     * 景区名称
     */
    public String scenicTitle;

    /**
     * 票型名称
     */
    public String ticketTitle;

    /**
     * 退款要求 FREE_REFUND:免费退 CAN_REFUND:可退 CANNOT_REFUND:不可退
     */
    public String refundRequirement;

    /**
     * 退订规则
     */
    public String refundRuleDesc;

    /**
     * 服务范围 过期字段
     */
    public List<String> itemDestinationList;
    /**
     * 使用的积分
     */
    public long usePoint;

    /**
     * 物流信息
     */
    public PackageResult packageInfo;

    /**
     * 机构信息
     */
    public Api_ITEMS_OrgInfo orgInfo;


    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static TmMainOrder deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static TmMainOrder deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            TmMainOrder result = new TmMainOrder();

            // 交易订单基础信息
            result.bizOrder = TmBizOrder.deserialize(json.optJSONObject("bizOrder"));
            // 交易子订单列表
            JSONArray detailOrdersArray = json.optJSONArray("detailOrders");
            if (detailOrdersArray != null) {
                int len = detailOrdersArray.length();
                result.detailOrders = new ArrayList<TmDetailOrder>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = detailOrdersArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.detailOrders.add(TmDetailOrder.deserialize(jo));
                    }
                }
            }

            // 订单按钮状态
            result.buttonStatus = TmButtonStatus.deserialize(json.optJSONObject("buttonStatus"));
            // 物流订单列表 暂时不用
            result.lgOrderList = TmLgOrderList.deserialize(json.optJSONObject("lgOrderList"));
            // 订单物流信息 暂时不用
            result.logisticsOrder = TmLogisticsOrder.deserialize(json.optJSONObject("logisticsOrder"));
            // 实际支付订单金额
            result.totalFee = json.optLong("totalFee");
            // 原始订单金额
            result.originalTotalFee = json.optLong("originalTotalFee");
            // 优惠券优惠金额
            result.voucherDiscountFee = json.optLong("voucherDiscountFee");
            // 完成时间/交易关闭时间/收货时间
            result.completionTime = json.optLong("completionTime");
            // 发货时间/确认时间
            result.deliveryTime = json.optLong("deliveryTime");
            // 入园时间
            result.scenicEnterTime = json.optLong("scenicEnterTime");
            // 出行时间
            result.departTime = json.optLong("departTime");
            // 入店时间
            result.checkInTime = json.optLong("checkInTime");
            // 离店时间
            result.checkOutTime = json.optLong("checkOutTime");
            // 最晚到店时间

            if(!json.isNull("latestArriveTime")){
                result.latestArriveTime = json.optString("latestArriveTime", null);
            }
            // 每天预订房间数
            result.roomAmount = json.optInt("roomAmount");
            // 游客信息列表
            JSONArray touristListArray = json.optJSONArray("touristList");
            if (touristListArray != null) {
                int len = touristListArray.length();
                result.touristList = new ArrayList<TmUserContactInfo>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = touristListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.touristList.add(TmUserContactInfo.deserialize(jo));
                    }
                }
            }

            // 联系人信息
            result.contactInfo = TmUserContactInfo.deserialize(json.optJSONObject("contactInfo"));
            // 联系人邮箱

            if(!json.isNull("email")){
                result.email = json.optString("email", null);
            }
            // 收货地址
            result.address = Address.deserialize(json.optJSONObject("address"));
            // 其他要求

            if(!json.isNull("otherInfo")){
                result.otherInfo = json.optString("otherInfo", null);
            }
            // 客服电话

            if(!json.isNull("servicePhone")){
                result.servicePhone = json.optString("servicePhone", null);
            }
            // 关闭原因

            if(!json.isNull("closeReason")){
                result.closeReason = json.optString("closeReason", null);
            }
            // 店铺信息
            result.merchantInfo = TmMerchantInfo.deserialize(json.optJSONObject("merchantInfo"));
            // 支付方式 ONLINE_PAY:在线付 OFFLINE_PAY:线下付

            if(!json.isNull("payType")){
                result.payType = json.optString("payType", null);
            }
            // 酒店名称

            if(!json.isNull("hotelTitle")){
                result.hotelTitle = json.optString("hotelTitle", null);
            }
            // 酒店id
            result.hotelId = json.optLong("hotelId");
            // 酒店电话列表 当前只取第一个
            JSONArray hotelPhoneArray = json.optJSONArray("hotelPhone");
            if (hotelPhoneArray != null) {
                int len = hotelPhoneArray.length();
                result.hotelPhone = new
                        ArrayList<String>(len);
                for (int i = 0; i < len; i++) {

                    if(!hotelPhoneArray.isNull(i)){
                        result.hotelPhone.add(hotelPhoneArray.optString(i, null));
                    }else{
                        result.hotelPhone.add(i, null);
                    }

                }
            }

            // 房间名称

            if(!json.isNull("roomTitle")){
                result.roomTitle = json.optString("roomTitle", null);
            }
            // 景区id
            result.scenicId = json.optLong("scenicId");
            // 景区名称

            if(!json.isNull("scenicTitle")){
                result.scenicTitle = json.optString("scenicTitle", null);
            }
            // 票型名称

            if(!json.isNull("ticketTitle")){
                result.ticketTitle = json.optString("ticketTitle", null);
            }
            // 退款要求 FREE_REFUND:免费退 CAN_REFUND:可退 CANNOT_REFUND:不可退

            if(!json.isNull("refundRequirement")){
                result.refundRequirement = json.optString("refundRequirement", null);
            }
            // 退订规则

            if(!json.isNull("refundRuleDesc")){
                result.refundRuleDesc = json.optString("refundRuleDesc", null);
            }
            // 服务范围 过期字段
            JSONArray itemDestinationListArray = json.optJSONArray("itemDestinationList");
            if (itemDestinationListArray != null) {
                int len = itemDestinationListArray.length();
                result.itemDestinationList = new
                        ArrayList<String>(len);
                for (int i = 0; i < len; i++) {

                    if(!itemDestinationListArray.isNull(i)){
                        result.itemDestinationList.add(itemDestinationListArray.optString(i, null));
                    }else{
                        result.itemDestinationList.add(i, null);
                    }

                }
            }

            // 使用的积分
            result.usePoint = json.optLong("usePoint");
            // 物流信息
            result.packageInfo = PackageResult.deserialize(json.optJSONObject("packageInfo"));
            // 机构信息
            result.orgInfo = Api_ITEMS_OrgInfo.deserialize(json.optJSONObject("orgInfo"));
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
        if (this.bizOrder != null) { json.put("bizOrder", this.bizOrder.serialize()); }

        // 交易子订单列表
        if (this.detailOrders != null) {
            JSONArray detailOrdersArray = new JSONArray();
            for (TmDetailOrder value : this.detailOrders)
            {
                if (value != null) {
                    detailOrdersArray.put(value.serialize());
                }
            }
            json.put("detailOrders", detailOrdersArray);
        }

        // 订单按钮状态
        if (this.buttonStatus != null) { json.put("buttonStatus", this.buttonStatus.serialize()); }

        // 物流订单列表 暂时不用
        if (this.lgOrderList != null) { json.put("lgOrderList", this.lgOrderList.serialize()); }

        // 订单物流信息 暂时不用
        if (this.logisticsOrder != null) { json.put("logisticsOrder", this.logisticsOrder.serialize()); }

        // 实际支付订单金额
        json.put("totalFee", this.totalFee);

        // 原始订单金额
        json.put("originalTotalFee", this.originalTotalFee);

        // 优惠券优惠金额
        json.put("voucherDiscountFee", this.voucherDiscountFee);

        // 完成时间/交易关闭时间/收货时间
        json.put("completionTime", this.completionTime);

        // 发货时间/确认时间
        json.put("deliveryTime", this.deliveryTime);

        // 入园时间
        json.put("scenicEnterTime", this.scenicEnterTime);

        // 出行时间
        json.put("departTime", this.departTime);

        // 入店时间
        json.put("checkInTime", this.checkInTime);

        // 离店时间
        json.put("checkOutTime", this.checkOutTime);

        // 最晚到店时间
        if(this.latestArriveTime != null) { json.put("latestArriveTime", this.latestArriveTime); }

        // 每天预订房间数
        json.put("roomAmount", this.roomAmount);

        // 游客信息列表
        if (this.touristList != null) {
            JSONArray touristListArray = new JSONArray();
            for (TmUserContactInfo value : this.touristList)
            {
                if (value != null) {
                    touristListArray.put(value.serialize());
                }
            }
            json.put("touristList", touristListArray);
        }

        // 联系人信息
        if (this.contactInfo != null) { json.put("contactInfo", this.contactInfo.serialize()); }

        // 联系人邮箱
        if(this.email != null) { json.put("email", this.email); }

        // 收货地址
        if (this.address != null) { json.put("address", this.address.serialize()); }

        // 其他要求
        if(this.otherInfo != null) { json.put("otherInfo", this.otherInfo); }

        // 客服电话
        if(this.servicePhone != null) { json.put("servicePhone", this.servicePhone); }

        // 关闭原因
        if(this.closeReason != null) { json.put("closeReason", this.closeReason); }

        // 店铺信息
        if (this.merchantInfo != null) { json.put("merchantInfo", this.merchantInfo.serialize()); }

        // 支付方式 ONLINE_PAY:在线付 OFFLINE_PAY:线下付
        if(this.payType != null) { json.put("payType", this.payType); }

        // 酒店名称
        if(this.hotelTitle != null) { json.put("hotelTitle", this.hotelTitle); }

        // 酒店id
        json.put("hotelId", this.hotelId);

        // 酒店电话列表 当前只取第一个
        if (this.hotelPhone != null) {
            JSONArray hotelPhoneArray = new JSONArray();
            for (String value : this.hotelPhone)
            {
                hotelPhoneArray.put(value);
            }
            json.put("hotelPhone", hotelPhoneArray);
        }

        // 房间名称
        if(this.roomTitle != null) { json.put("roomTitle", this.roomTitle); }

        // 景区id
        json.put("scenicId", this.scenicId);

        // 景区名称
        if(this.scenicTitle != null) { json.put("scenicTitle", this.scenicTitle); }

        // 票型名称
        if(this.ticketTitle != null) { json.put("ticketTitle", this.ticketTitle); }

        // 退款要求 FREE_REFUND:免费退 CAN_REFUND:可退 CANNOT_REFUND:不可退
        if(this.refundRequirement != null) { json.put("refundRequirement", this.refundRequirement); }

        // 退订规则
        if(this.refundRuleDesc != null) { json.put("refundRuleDesc", this.refundRuleDesc); }

        // 服务范围 过期字段
        if (this.itemDestinationList != null) {
            JSONArray itemDestinationListArray = new JSONArray();
            for (String value : this.itemDestinationList)
            {
                itemDestinationListArray.put(value);
            }
            json.put("itemDestinationList", itemDestinationListArray);
        }

        // 使用的积分
        json.put("usePoint", this.usePoint);

        // 物流信息
        if (this.packageInfo != null) { json.put("packageInfo", this.packageInfo.serialize()); }

        return json;
    }
}
  