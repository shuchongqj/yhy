// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model;

import com.yhy.common.beans.net.model.rc.RCExtendFieldInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RCShowcase implements Serializable {

    private static final long serialVersionUID = -8996830189775812354L;
    /**
     * id
     */
    public long id;

    /**
     * 标题
     */
    public String title;

    /**
     * 状态，取值ONLINE，OFFLINE
     */
    public String status;

    /**
     * 简介
     */
    public String summary;

    /**
     * 业务code，当资源位与特殊业务操作捆绑在一起的时候才会用到
     */
    public String bizCode;

    /**
     * 展位资源对应的业务数据，例如：帖子中配置资源信息的时候，此值就表示帖子id
     */
    public String boothContent;

    /**
     * 操作
     */
    public String operation;

    /**
     * 操作内容，例如：跳转到帖子，则此值为帖子id，如果跳转到h5，则此值为url
     */
    public String operationContent;

    /**
     * 显示类型：DEFAULT默认显示，DISABLE不可用的时候显示  MAN-男性用户显示  WOMAN-女性用户显示 MOTHERANDCHILD-母婴用户显示
     */
    public String showType;

    /**
     * 图片
     */
    public String imgUrl;

    /**
     * 序号
     */
    public int serialNo;

    /**
     * 上架时间
     */
    public long timingOnDate;

    /**
     * 下架时间
     */
    public long timingOffDate;

    /**
     * 广告位类型
     */
    public int bannerType;

    /**
     * 名称(运营备注)
     */
    public String info;

    /**
     * 扩展字段数据信息
     */
    public List<RCExtendFieldInfo> extendFieldInfos;

    /**
     * json格式的内容
     */
    public String content;

    /**
     * 更多按钮
     */
    public boolean isMore;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static RCShowcase deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static RCShowcase deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            RCShowcase result = new RCShowcase();

            // id
            result.id = json.optLong("id");
            // 标题

            if (!json.isNull("title")) {
                result.title = json.optString("title", null);
            }
            // 状态，取值ONLINE，OFFLINE

            if (!json.isNull("status")) {
                result.status = json.optString("status", null);
            }
            // 简介

            if (!json.isNull("summary")) {
                result.summary = json.optString("summary", null);
            }
            // 业务code，当资源位与特殊业务操作捆绑在一起的时候才会用到

            if (!json.isNull("bizCode")) {
                result.bizCode = json.optString("bizCode", null);
            }
            // 展位资源对应的业务数据，例如：帖子中配置资源信息的时候，此值就表示帖子id

            if (!json.isNull("boothContent")) {
                result.boothContent = json.optString("boothContent", null);
            }
            // 操作

            if (!json.isNull("operation")) {
                result.operation = json.optString("operation", null);
            }
            // 操作内容，例如：跳转到帖子，则此值为帖子id，如果跳转到h5，则此值为url

            if (!json.isNull("operationContent")) {
                result.operationContent = json.optString("operationContent", null);
            }
            // 显示类型：DEFAULT默认显示，DISABLE不可用的时候显示  MAN-男性用户显示  WOMAN-女性用户显示 MOTHERANDCHILD-母婴用户显示

            if (!json.isNull("showType")) {
                result.showType = json.optString("showType", null);
            }
            // 图片

            if (!json.isNull("imgUrl")) {
                result.imgUrl = json.optString("imgUrl", null);
            }
            // 序号
            result.serialNo = json.optInt("serialNo");
            // 上架时间
            result.timingOnDate = json.optLong("timingOnDate");
            // 下架时间
            result.timingOffDate = json.optLong("timingOffDate");
            // 名称(运营备注)

            if (!json.isNull("info")) {
                result.info = json.optString("info", null);
            }
            // json格式的内容

            if (!json.isNull("content")) {
                result.content = json.optString("content", null);
            }

            // 扩展字段数据信息
            JSONArray extendFieldInfosArray = json.optJSONArray("extendFieldInfos");
            if (extendFieldInfosArray != null) {
                int len = extendFieldInfosArray.length();
                result.extendFieldInfos = new ArrayList<RCExtendFieldInfo>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = extendFieldInfosArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.extendFieldInfos.add(RCExtendFieldInfo.deserialize(jo));
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

        // id
        json.put("id", this.id);

        // 标题
        if (this.title != null) {
            json.put("title", this.title);
        }

        // 状态，取值ONLINE，OFFLINE
        if (this.status != null) {
            json.put("status", this.status);
        }

        // 简介
        if (this.summary != null) {
            json.put("summary", this.summary);
        }

        // 业务code，当资源位与特殊业务操作捆绑在一起的时候才会用到
        if (this.bizCode != null) {
            json.put("bizCode", this.bizCode);
        }

        // 展位资源对应的业务数据，例如：帖子中配置资源信息的时候，此值就表示帖子id
        if (this.boothContent != null) {
            json.put("boothContent", this.boothContent);
        }

        // 操作
        if (this.operation != null) {
            json.put("operation", this.operation);
        }

        // 操作内容，例如：跳转到帖子，则此值为帖子id，如果跳转到h5，则此值为url
        if (this.operationContent != null) {
            json.put("operationContent", this.operationContent);
        }

        // 显示类型：DEFAULT默认显示，DISABLE不可用的时候显示  MAN-男性用户显示  WOMAN-女性用户显示 MOTHERANDCHILD-母婴用户显示
        if (this.showType != null) {
            json.put("showType", this.showType);
        }

        // 图片
        if (this.imgUrl != null) {
            json.put("imgUrl", this.imgUrl);
        }

        // 序号
        json.put("serialNo", this.serialNo);

        // 上架时间
        json.put("timingOnDate", this.timingOnDate);

        // 下架时间
        json.put("timingOffDate", this.timingOffDate);

        // 名称(运营备注)
        if (this.info != null) {
            json.put("info", this.info);
        }

        // json格式的内容
        if (this.content != null) {
            json.put("content", this.content);
        }
        // 扩展字段数据信息
        if (this.extendFieldInfos != null) {
            JSONArray extendFieldInfosArray = new JSONArray();
            for (RCExtendFieldInfo value : this.extendFieldInfos) {
                if (value != null) {
                    extendFieldInfosArray.put(value.serialize());
                }
            }
            json.put("extendFieldInfos", extendFieldInfosArray);
        }
        return json;
    }
}
  