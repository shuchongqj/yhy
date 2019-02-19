// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.tm;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LgExpressInfo implements Serializable{

    private static final long serialVersionUID = -8795433840261340681L;
    /**
     * 快递单号
     */
    public String numbers;
      
    /**
     * 快递公司编码
     */
    public String companyCode;
      
    /**
     * 快递公司名称
     */
    public String companyName;
      
    /**
     * 发货时间
     */
    public long sendTime;
      
    /**
     * 详细信息
     */
    public List<LgExpressLineInfo> expressLineInfoList;
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static LgExpressInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static LgExpressInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            LgExpressInfo result = new LgExpressInfo();
            
            // 快递单号
            
              if(!json.isNull("numbers")){
                  result.numbers = json.optString("numbers", null);
              }
            // 快递公司编码
            
              if(!json.isNull("companyCode")){
                  result.companyCode = json.optString("companyCode", null);
              }
            // 快递公司名称
            
              if(!json.isNull("companyName")){
                  result.companyName = json.optString("companyName", null);
              }
            // 发货时间
            result.sendTime = json.optLong("sendTime");
            // 详细信息
            JSONArray expressLineInfoListArray = json.optJSONArray("expressLineInfoList");
            if (expressLineInfoListArray != null) {
                int len = expressLineInfoListArray.length();
                result.expressLineInfoList = new ArrayList<LgExpressLineInfo>(len);
                for (int i = 0; i < len; i++) {
                        JSONObject jo = expressLineInfoListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.expressLineInfoList.add(LgExpressLineInfo.deserialize(jo));
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
        
        // 快递单号
        if(this.numbers != null) { json.put("numbers", this.numbers); }
          
        // 快递公司编码
        if(this.companyCode != null) { json.put("companyCode", this.companyCode); }
          
        // 快递公司名称
        if(this.companyName != null) { json.put("companyName", this.companyName); }
          
        // 发货时间
        json.put("sendTime", this.sendTime);
          
        // 详细信息 
        if (this.expressLineInfoList != null) {
            JSONArray expressLineInfoListArray = new JSONArray();
            for (LgExpressLineInfo value : this.expressLineInfoList)
            {
                if (value != null) {
                    expressLineInfoListArray.put(value.serialize());
                }
            }
            json.put("expressLineInfoList", expressLineInfoListArray);
        }
      
        return json;
    }
}
  