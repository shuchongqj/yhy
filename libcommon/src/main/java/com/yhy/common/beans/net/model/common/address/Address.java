// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.common.address;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Address implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = -2983803043916460264L;

	/**
     * 记录id
     */
    public long addressId;
      
    /**
     * 省级行政区名称
     */
    public String province;
      
    /**
     * 省级行政区编码
     */
    public String provinceCode;
      
    /**
     * 市级行政区名称
     */
    public String city;
      
    /**
     * 市级行政区编码
     */
    public String cityCode;
      
    /**
     * 区县级行政区名称
     */
    public String area;
      
    /**
     * 区县级行政区编码
     */
    public String areaCode;
      
    /**
     * 详细街道地址
     */
    public String detailAddress;
      
    /**
     * 收件人姓名
     */
    public String recipientName;
      
    /**
     * 收件人号码
     */
    public String recipientPhone;
      
    /**
     * 收件人邮政编码
     */
    public String zipCode;
      
    /**
     * 是否默认收货地址
     */
    public boolean isDefault;
      
    /**
     * 用户id
     */
    public long userId;
      
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static Address deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static Address deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            Address result = new Address();
            
            // 记录id
            result.addressId = json.optLong("addressId");
            // 省级行政区名称
            
              if(!json.isNull("province")){
                  result.province = json.optString("province", null);
              }
            // 省级行政区编码
            
              if(!json.isNull("provinceCode")){
                  result.provinceCode = json.optString("provinceCode", null);
              }
            // 市级行政区名称
            
              if(!json.isNull("city")){
                  result.city = json.optString("city", null);
              }
            // 市级行政区编码
            
              if(!json.isNull("cityCode")){
                  result.cityCode = json.optString("cityCode", null);
              }
            // 区县级行政区名称
            
              if(!json.isNull("area")){
                  result.area = json.optString("area", null);
              }
            // 区县级行政区编码
            
              if(!json.isNull("areaCode")){
                  result.areaCode = json.optString("areaCode", null);
              }
            // 详细街道地址
            
              if(!json.isNull("detailAddress")){
                  result.detailAddress = json.optString("detailAddress", null);
              }
            // 收件人姓名
            
              if(!json.isNull("recipientName")){
                  result.recipientName = json.optString("recipientName", null);
              }
            // 收件人号码
            
              if(!json.isNull("recipientPhone")){
                  result.recipientPhone = json.optString("recipientPhone", null);
              }
            // 收件人邮政编码
            
              if(!json.isNull("zipCode")){
                  result.zipCode = json.optString("zipCode", null);
              }
            // 是否默认收货地址
            result.isDefault = json.optBoolean("isDefault");
            // 用户id
            result.userId = json.optLong("userId");
            return result;
        }
        return null;
    }
    
    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();
        
        // 记录id
        json.put("addressId", this.addressId);
          
        // 省级行政区名称
        if(this.province != null) { json.put("province", this.province); }
          
        // 省级行政区编码
        if(this.provinceCode != null) { json.put("provinceCode", this.provinceCode); }
          
        // 市级行政区名称
        if(this.city != null) { json.put("city", this.city); }
          
        // 市级行政区编码
        if(this.cityCode != null) { json.put("cityCode", this.cityCode); }
          
        // 区县级行政区名称
        if(this.area != null) { json.put("area", this.area); }
          
        // 区县级行政区编码
        if(this.areaCode != null) { json.put("areaCode", this.areaCode); }
          
        // 详细街道地址
        if(this.detailAddress != null) { json.put("detailAddress", this.detailAddress); }
          
        // 收件人姓名
        if(this.recipientName != null) { json.put("recipientName", this.recipientName); }
          
        // 收件人号码
        if(this.recipientPhone != null) { json.put("recipientPhone", this.recipientPhone); }
          
        // 收件人邮政编码
        if(this.zipCode != null) { json.put("zipCode", this.zipCode); }
          
        // 是否默认收货地址
        json.put("isDefault", this.isDefault);
          
        // 用户id
        json.put("userId", this.userId);
          
        return json;
    }
}
  