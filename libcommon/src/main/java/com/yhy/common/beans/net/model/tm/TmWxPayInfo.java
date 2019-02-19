// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.tm;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class TmWxPayInfo implements Serializable {

    private static final long serialVersionUID = 6776089748755225298L;
    /**
     * 公众账号ID
     */
    public String appid;

    /**
     * 商户号
     */
    public String partnerid;

    /**
     * 预支付交易会话ID
     */
    public String prepayid;

    /**
     * 扩展字段
     */
    public String packageStr;

    /**
     * 随机字符串
     */
    public String noncestr;

    /**
     * 时间戳
     */
    public String timestamp;

    /**
     * 签名
     */
    public String sign;

    /**
     * wap支付直接获取次字段
     */
    public String dataJson;


    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static TmWxPayInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static TmWxPayInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            TmWxPayInfo result = new TmWxPayInfo();

            // 公众账号ID

            if (!json.isNull("appid")) {
                result.appid = json.optString("appid", null);
            }
            // 商户号

            if (!json.isNull("partnerid")) {
                result.partnerid = json.optString("partnerid", null);
            }
            // 预支付交易会话ID

            if (!json.isNull("prepayid")) {
                result.prepayid = json.optString("prepayid", null);
            }
            // 扩展字段

            if (!json.isNull("packageStr")) {
                result.packageStr = json.optString("packageStr", null);
            }
            // 随机字符串

            if (!json.isNull("noncestr")) {
                result.noncestr = json.optString("noncestr", null);
            }
            // 时间戳

            if (!json.isNull("timestamp")) {
                result.timestamp = json.optString("timestamp", null);
            }
            // 签名

            if (!json.isNull("sign")) {
                result.sign = json.optString("sign", null);
            }

            // wap支付直接获取次字段

            if (!json.isNull("dataJson")) {
                result.dataJson = json.optString("dataJson", null);
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

        // 公众账号ID
        if (this.appid != null) {
            json.put("appid", this.appid);
        }

        // 商户号
        if (this.partnerid != null) {
            json.put("partnerid", this.partnerid);
        }

        // 预支付交易会话ID
        if (this.prepayid != null) {
            json.put("prepayid", this.prepayid);
        }

        // 扩展字段
        if (this.packageStr != null) {
            json.put("packageStr", this.packageStr);
        }

        // 随机字符串
        if (this.noncestr != null) {
            json.put("noncestr", this.noncestr);
        }

        // 时间戳
        if (this.timestamp != null) {
            json.put("timestamp", this.timestamp);
        }

        // 签名
        if (this.sign != null) {
            json.put("sign", this.sign);
        }

        // wap支付直接获取次字段
        if (this.dataJson != null) {
            json.put("dataJson", this.dataJson);
        }

        return json;
    }
}
  