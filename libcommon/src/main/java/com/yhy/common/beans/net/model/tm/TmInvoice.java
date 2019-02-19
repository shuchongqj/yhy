// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.tm;
    
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class TmInvoice implements Serializable{

    private static final long serialVersionUID = -5661914118349714146L;
    /**
     * 发票类型
     */
    public int invoiceTypeId;
      
    /**
     * 发票内容
     */
    public int invoiceContentId;
      
    /**
     * 发票抬头
     */
    public String invoiceTitle;
      
    /**
     * 发票类型:2-个人 1-公司
     */
    public int invoiceSignalId;
      
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static TmInvoice deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static TmInvoice deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            TmInvoice result = new TmInvoice();
            
            // 发票类型
            result.invoiceTypeId = json.optInt("invoiceTypeId");
            // 发票内容
            result.invoiceContentId = json.optInt("invoiceContentId");
            // 发票抬头
            
              if(!json.isNull("invoiceTitle")){
                  result.invoiceTitle = json.optString("invoiceTitle", null);
              }
            // 发票类型:2-个人 1-公司
            result.invoiceSignalId = json.optInt("invoiceSignalId");
            return result;
        }
        return null;
    }
    
    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();
        
        // 发票类型
        json.put("invoiceTypeId", this.invoiceTypeId);
          
        // 发票内容
        json.put("invoiceContentId", this.invoiceContentId);
          
        // 发票抬头
        if(this.invoiceTitle != null) { json.put("invoiceTitle", this.invoiceTitle); }
          
        // 发票类型:2-个人 1-公司
        json.put("invoiceSignalId", this.invoiceSignalId);
          
        return json;
    }
}
  