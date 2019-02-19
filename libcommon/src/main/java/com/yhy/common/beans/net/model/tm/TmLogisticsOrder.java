// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.tm;
    
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class TmLogisticsOrder implements Serializable{

    private static final long serialVersionUID = -8603048788370074096L;
    /**
     * 发货时间
     */
    public long consignTime;
      
    /**
     * 交易结束时间
     */
    public long endTime;
      
    /**
     * 收货地址
     */
    public String address;
      
    /**
     * 收货人
     */
    public String fullName;
      
    /**
     * 邮政编码
     */
    public String post;
      
    /**
     * 电话
     */
    public String phone;
      
    /**
     * 移动电话
     */
    public String mobilePhone;
      
    /**
     * 省
     */
    public String prov;
      
    /**
     * 市
     */
    public String city;
      
    /**
     * 区
     */
    public String area;
      
    /**
     * 物流方式，包邮等
     */
    public String shipping;

    /**
     * 物流单号
     */
    public String expressNo;

    /**
     * 物流公司
     */
    public String expressCompany;
      
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static TmLogisticsOrder deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static TmLogisticsOrder deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            TmLogisticsOrder result = new TmLogisticsOrder();
            
            // 发货时间
            result.consignTime = json.optLong("consignTime");
            // 交易结束时间
            result.endTime = json.optLong("endTime");
            // 收货地址
            
              if(!json.isNull("address")){
                  result.address = json.optString("address", null);
              }
            // 收货人
            
              if(!json.isNull("fullName")){
                  result.fullName = json.optString("fullName", null);
              }
            // 邮政编码
            
              if(!json.isNull("post")){
                  result.post = json.optString("post", null);
              }
            // 电话
            
              if(!json.isNull("phone")){
                  result.phone = json.optString("phone", null);
              }
            // 移动电话
            
              if(!json.isNull("mobilePhone")){
                  result.mobilePhone = json.optString("mobilePhone", null);
              }
            // 省
            
              if(!json.isNull("prov")){
                  result.prov = json.optString("prov", null);
              }
            // 市
            
              if(!json.isNull("city")){
                  result.city = json.optString("city", null);
              }
            // 区
            
              if(!json.isNull("area")){
                  result.area = json.optString("area", null);
              }
            // 物流方式，包邮等
            
              if(!json.isNull("shipping")){
                  result.shipping = json.optString("shipping", null);
              }

            // 物流单号

            if(!json.isNull("expressNo")){
                result.expressNo = json.optString("expressNo", null);
            }
            // 物流公司

            if(!json.isNull("expressCompany")){
                result.expressCompany = json.optString("expressCompany", null);
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
        
        // 发货时间
        json.put("consignTime", this.consignTime);
          
        // 交易结束时间
        json.put("endTime", this.endTime);
          
        // 收货地址
        if(this.address != null) { json.put("address", this.address); }
          
        // 收货人
        if(this.fullName != null) { json.put("fullName", this.fullName); }
          
        // 邮政编码
        if(this.post != null) { json.put("post", this.post); }
          
        // 电话
        if(this.phone != null) { json.put("phone", this.phone); }
          
        // 移动电话
        if(this.mobilePhone != null) { json.put("mobilePhone", this.mobilePhone); }
          
        // 省
        if(this.prov != null) { json.put("prov", this.prov); }
          
        // 市
        if(this.city != null) { json.put("city", this.city); }
          
        // 区
        if(this.area != null) { json.put("area", this.area); }
          
        // 物流方式，包邮等
        if(this.shipping != null) { json.put("shipping", this.shipping); }

        // 物流单号
        if(this.expressNo != null) { json.put("expressNo", this.expressNo); }

        // 物流公司
        if(this.expressCompany != null) { json.put("expressCompany", this.expressCompany); }
          
        return json;
    }
}
  