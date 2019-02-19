// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.pedometer;
    
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class TrackConfig implements Serializable{

    private static final long serialVersionUID = -8280616135506072715L;
    /**
     * 客户端圆环最高步数
     */
    public int maxStepCircle;

    /**
     * 最高步数校验
     */
    public int maxStepValidate;

    /**
     * 同步时最小步数,客户端按这个值作为阀值提交同步
     */
    public int minSyncStep;

    /**
     * 同步最大步数，暂时没用
     */
    public int maxSyncStep;

    /**
     * 首页url
     */
    public String homeUrl;

    /**
     * app下载url
     */
    public String qrCodeUrl;

    /**
     * 分享文本
     */
    public String shareText;

    /**
     * 分享内容
     */
    public String shareTitle;

    /**
     * 微博话题名称
     */
    public String weiboTopicName;

    /**
     * 达人圈话题名称
     */
    public String topicName;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static TrackConfig deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static TrackConfig deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            TrackConfig result = new TrackConfig();

            // 客户端圆环最高步数
            result.maxStepCircle = json.optInt("maxStepCircle");
            // 最高步数校验
            result.maxStepValidate = json.optInt("maxStepValidate");
            // 同步时最小步数,客户端按这个值作为阀值提交同步
            result.minSyncStep = json.optInt("minSyncStep");
            // 同步最大步数，暂时没用
            result.maxSyncStep = json.optInt("maxSyncStep");
            // 首页url

            if(!json.isNull("homeUrl")){
                result.homeUrl = json.optString("homeUrl", null);
            }
            // app下载url

            if(!json.isNull("qrCodeUrl")){
                result.qrCodeUrl = json.optString("qrCodeUrl", null);
            }
            // 分享文本

            if(!json.isNull("shareText")){
                result.shareText = json.optString("shareText", null);
            }
            // 分享内容

            if(!json.isNull("shareTitle")){
                result.shareTitle = json.optString("shareTitle", null);
            }
            // 微博话题名称

            if(!json.isNull("weiboTopicName")){
                result.weiboTopicName = json.optString("weiboTopicName", null);
            }
            // 达人圈话题名称

            if(!json.isNull("topicName")){
                result.topicName = json.optString("topicName", null);
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

        // 客户端圆环最高步数
        json.put("maxStepCircle", this.maxStepCircle);

        // 最高步数校验
        json.put("maxStepValidate", this.maxStepValidate);

        // 同步时最小步数,客户端按这个值作为阀值提交同步
        json.put("minSyncStep", this.minSyncStep);

        // 同步最大步数，暂时没用
        json.put("maxSyncStep", this.maxSyncStep);

        // 首页url
        if(this.homeUrl != null) { json.put("homeUrl", this.homeUrl); }

        // app下载url
        if(this.qrCodeUrl != null) { json.put("qrCodeUrl", this.qrCodeUrl); }

        // 分享文本
        if(this.shareText != null) { json.put("shareText", this.shareText); }

        // 分享内容
        if(this.shareTitle != null) { json.put("shareTitle", this.shareTitle); }

        // 微博话题名称
        if(this.weiboTopicName != null) { json.put("weiboTopicName", this.weiboTopicName); }

        // 达人圈话题名称
        if(this.topicName != null) { json.put("topicName", this.topicName); }

        return json;
    }
}
  