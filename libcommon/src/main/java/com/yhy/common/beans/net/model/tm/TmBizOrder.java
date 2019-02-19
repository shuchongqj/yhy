// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.tm;
    
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class TmBizOrder implements Serializable{

    private static final long serialVersionUID = -3213945084445725357L;

    /**
    * 交易订单id，也就是订单号
    */
    public long bizOrderId;

    /**
     * 订单类型 NORMAL:普通商品交易 / HOTEL:酒店订单 / SPOTS:景区门票 / ACTIVITY:活动商品 / TOUR_LINE:国内跟团游订单 / FREE_LINE:国内自由行订单 / CITY_ACTIVITY:同城活动订单 / HOTEL_OFFLINE:到店付酒店订单 / TOUR_LINE_ABOARD:境外跟团游订单 / FREE_LINE_ABOARD:境外自由行订单
     */
    public String orderType;

    /**
     * 状态code 全部：ALL，待付款：WAITING_PAY，已付款：WAITING_DELIVERY，已发货/预订成功/待使用：SHIPPING，已完成：FINISH，已关闭：CLOSED，已取消：CANCEL，已退款：REFUNDED，待确认(过期)：WAITING_CONFIRM，已确认：CONFIRMED，已评价：RATED，未入住：CONFIRMED_CLOSE
     */
    public String orderStatus;

    /**
     * 购买数量
     */
    public long buyAmount;

    /**
     * 卖家id-保留
     */
    public long sellerId;

    /**
     * 卖家昵称
     */
    public String sellerNick;

    /**
     * 买家id
     */
    public long buyerId;

    /**
     * 买家昵称
     */
    public String buyerNick;

    /**
     * 外部ID，放置卡号-保留
     */
    public String outerId;

    /**
     * 付款时间
     */
    public long payTime;

    /**
     * 下单时间
     */
    public long createTime;

    /**
     * 交易实际支付现金
     */
    public long actualTotalFee;
    /**
     * 原交易实际支付现金
     */
    public long originalActualTotalFee;

    /**
     * 退款状态-保留
     */
    public String refundStatus;

    /**
     * url
     */
    public String url;

    /**
     * 优惠券
     */
    public VoucherResult voucherResult;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static TmBizOrder deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static TmBizOrder deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            TmBizOrder result = new TmBizOrder();

            // 交易订单id，也就是订单号
            result.bizOrderId = json.optLong("bizOrderId");
            // 订单类型 NORMAL:普通商品交易 / HOTEL:酒店订单 / SPOTS:景区门票 / ACTIVITY:活动商品 / TOUR_LINE:国内跟团游订单 / FREE_LINE:国内自由行订单 / CITY_ACTIVITY:同城活动订单 / HOTEL_OFFLINE:到店付酒店订单 / TOUR_LINE_ABOARD:境外跟团游订单 / FREE_LINE_ABOARD:境外自由行订单

            if(!json.isNull("orderType")){
                result.orderType = json.optString("orderType", null);
            }
            // 状态code 全部：ALL，待付款：WAITING_PAY，已付款：WAITING_DELIVERY，已发货/预订成功/待使用：SHIPPING，已完成：FINISH，已关闭：CLOSED，已取消：CANCEL，已退款：REFUNDED，待确认(过期)：WAITING_CONFIRM，已确认：CONFIRMED，已评价：RATED，未入住：CONFIRMED_CLOSE

            if(!json.isNull("orderStatus")){
                result.orderStatus = json.optString("orderStatus", null);
            }
            // 购买数量
            result.buyAmount = json.optLong("buyAmount");
            // 卖家id-保留
            result.sellerId = json.optLong("sellerId");
            // 卖家昵称

            if(!json.isNull("sellerNick")){
                result.sellerNick = json.optString("sellerNick", null);
            }
            // 买家id
            result.buyerId = json.optLong("buyerId");
            // 买家昵称

            if(!json.isNull("buyerNick")){
                result.buyerNick = json.optString("buyerNick", null);
            }
            // 外部ID，放置卡号-保留

            if(!json.isNull("outerId")){
                result.outerId = json.optString("outerId", null);
            }
            // 付款时间
            result.payTime = json.optLong("payTime");
            // 下单时间
            result.createTime = json.optLong("createTime");
            // 交易实际支付现金
            result.actualTotalFee = json.optLong("actualTotalFee");
            // 原交易实际支付现金
            result.originalActualTotalFee = json.optLong("originalActualTotalFee");
            // 退款状态-保留

            if(!json.isNull("refundStatus")){
                result.refundStatus = json.optString("refundStatus", null);
            }
            // url

            if(!json.isNull("url")){
                result.url = json.optString("url", null);
            }
            // 优惠券
            result.voucherResult = VoucherResult.deserialize(json.optJSONObject("voucherResult"));
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 交易订单id，也就是订单号
        json.put("bizOrderId", this.bizOrderId);

        // 订单类型 NORMAL:普通商品交易 / HOTEL:酒店订单 / SPOTS:景区门票 / ACTIVITY:活动商品 / TOUR_LINE:国内跟团游订单 / FREE_LINE:国内自由行订单 / CITY_ACTIVITY:同城活动订单 / HOTEL_OFFLINE:到店付酒店订单 / TOUR_LINE_ABOARD:境外跟团游订单 / FREE_LINE_ABOARD:境外自由行订单
        if(this.orderType != null) { json.put("orderType", this.orderType); }

        // 状态code 全部：ALL，待付款：WAITING_PAY，已付款：WAITING_DELIVERY，已发货/预订成功/待使用：SHIPPING，已完成：FINISH，已关闭：CLOSED，已取消：CANCEL，已退款：REFUNDED，待确认(过期)：WAITING_CONFIRM，已确认：CONFIRMED，已评价：RATED，未入住：CONFIRMED_CLOSE
        if(this.orderStatus != null) { json.put("orderStatus", this.orderStatus); }

        // 购买数量
        json.put("buyAmount", this.buyAmount);

        // 卖家id-保留
        json.put("sellerId", this.sellerId);

        // 卖家昵称
        if(this.sellerNick != null) { json.put("sellerNick", this.sellerNick); }

        // 买家id
        json.put("buyerId", this.buyerId);

        // 买家昵称
        if(this.buyerNick != null) { json.put("buyerNick", this.buyerNick); }

        // 外部ID，放置卡号-保留
        if(this.outerId != null) { json.put("outerId", this.outerId); }

        // 付款时间
        json.put("payTime", this.payTime);

        // 下单时间
        json.put("createTime", this.createTime);

        // 交易实际支付现金
        json.put("actualTotalFee", this.actualTotalFee);

        // 原交易实际支付现金
        json.put("originalActualTotalFee", this.originalActualTotalFee);

        // 退款状态-保留
        if(this.refundStatus != null) { json.put("refundStatus", this.refundStatus); }

        // url
        if(this.url != null) { json.put("url", this.url); }

        // 优惠券
        if (this.voucherResult != null) { json.put("voucherResult", this.voucherResult.serialize()); }

        return json;
    }
}
  