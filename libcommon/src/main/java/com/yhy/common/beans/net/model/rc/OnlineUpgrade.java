// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.rc;
    
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class OnlineUpgrade implements Serializable{

    private static final long serialVersionUID = -2869478607395392159L;
    /**
     * 升级描述
     */
    public String desc;

    /**
     * 下载URL
     */
    public String downloadUrl;

    /**
     * 版本号
     */
    public String version;

    /**
     * 版本名
     */
    public String versionName;

    /**
     * 是否强制升级
     */
    public boolean forceUpgrade;

    /**
     * 强制升级描述
     */
    public String forceDesc;

    /**
     * 升级版本号
     */
    public String patchVersion;

    /**
     * 升级版本地址
     */
    public String patchUrl;
      
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static OnlineUpgrade deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static OnlineUpgrade deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            OnlineUpgrade result = new OnlineUpgrade();

            // 升级描述

            if(!json.isNull("desc")){
                result.desc = json.optString("desc", null);
            }
            // 下载URL

            if(!json.isNull("downloadUrl")){
                result.downloadUrl = json.optString("downloadUrl", null);
            }
            // 版本号

            if(!json.isNull("version")){
                result.version = json.optString("version", null);
            }
            // 版本名

            if(!json.isNull("versionName")){
                result.versionName = json.optString("versionName", null);
            }
            // 是否强制升级
            result.forceUpgrade = json.optBoolean("forceUpgrade");
            // 强制升级描述

            if(!json.isNull("forceDesc")){
                result.forceDesc = json.optString("forceDesc", null);
            }
            // 升级版本号

            if(!json.isNull("patchVersion")){
                result.patchVersion = json.optString("patchVersion", null);
            }
            // 升级版本地址

            if(!json.isNull("patchUrl")){
                result.patchUrl = json.optString("patchUrl", null);
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

        // 升级描述
        if(this.desc != null) { json.put("desc", this.desc); }

        // 下载URL
        if(this.downloadUrl != null) { json.put("downloadUrl", this.downloadUrl); }

        // 版本号
        if(this.version != null) { json.put("version", this.version); }

        // 版本名
        if(this.versionName != null) { json.put("versionName", this.versionName); }

        // 是否强制升级
        json.put("forceUpgrade", this.forceUpgrade);

        // 强制升级描述
        if(this.forceDesc != null) { json.put("forceDesc", this.forceDesc); }

        // 升级版本号
        if(this.patchVersion != null) { json.put("patchVersion", this.patchVersion); }

        // 升级版本地址
        if(this.patchUrl != null) { json.put("patchUrl", this.patchUrl); }
          
        return json;
    }
}
  