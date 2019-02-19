// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.tm;
    
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class TmLgOrder implements Serializable{

    private static final long serialVersionUID = 2813703031027718391L;
    /**
     * 发货时间
     */
    public long consignTime;
      
    /**
     * 快递公司
     */
    public String logisticsCompany;
      
    /**
     * 快递单号
     */
    public String logisticsExternalNo;
      
    /**
     * 快递公司icon
     */
    public String logisticsCompanyImgUrl;
      
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
     * 地址
     */
    public String toAddress;
      
    /**
     * 收货人
     */
    public String toContact;
      
    /**
     * 邮政编码
     */
    public String toPost;
      
    /**
     * 移动电话
     */
    public String toMobile;
      
    /**
     * 物流状态 取值范围 UNCONSIGNED, CONSIGNED, DELIVERED
     */
    public String lgOrderStatus;
      
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static TmLgOrder deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static TmLgOrder deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            TmLgOrder result = new TmLgOrder();
            
            // 发货时间
            result.consignTime = json.optLong("consignTime");
            // 快递公司
            
              if(!json.isNull("logisticsCompany")){
                  result.logisticsCompany = json.optString("logisticsCompany", null);
              }
            // 快递单号
            
              if(!json.isNull("logisticsExternalNo")){
                  result.logisticsExternalNo = json.optString("logisticsExternalNo", null);
              }
            // 快递公司icon
            
              if(!json.isNull("logisticsCompanyImgUrl")){
                  result.logisticsCompanyImgUrl = json.optString("logisticsCompanyImgUrl", null);
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
            // 地址
            
              if(!json.isNull("toAddress")){
                  result.toAddress = json.optString("toAddress", null);
              }
            // 收货人
            
              if(!json.isNull("toContact")){
                  result.toContact = json.optString("toContact", null);
              }
            // 邮政编码
            
              if(!json.isNull("toPost")){
                  result.toPost = json.optString("toPost", null);
              }
            // 移动电话
            
              if(!json.isNull("toMobile")){
                  result.toMobile = json.optString("toMobile", null);
              }
            // 物流状态 取值范围 UNCONSIGNED, CONSIGNED, DELIVERED
            
              if(!json.isNull("lgOrderStatus")){
                  result.lgOrderStatus = json.optString("lgOrderStatus", null);
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
          
        // 快递公司
        if(this.logisticsCompany != null) { json.put("logisticsCompany", this.logisticsCompany); }
          
        // 快递单号
        if(this.logisticsExternalNo != null) { json.put("logisticsExternalNo", this.logisticsExternalNo); }
          
        // 快递公司icon
        if(this.logisticsCompanyImgUrl != null) { json.put("logisticsCompanyImgUrl", this.logisticsCompanyImgUrl); }
          
        // 省
        if(this.prov != null) { json.put("prov", this.prov); }
          
        // 市
        if(this.city != null) { json.put("city", this.city); }
          
        // 区
        if(this.area != null) { json.put("area", this.area); }
          
        // 地址
        if(this.toAddress != null) { json.put("toAddress", this.toAddress); }
          
        // 收货人
        if(this.toContact != null) { json.put("toContact", this.toContact); }
          
        // 邮政编码
        if(this.toPost != null) { json.put("toPost", this.toPost); }
          
        // 移动电话
        if(this.toMobile != null) { json.put("toMobile", this.toMobile); }
          
        // 物流状态 取值范围 UNCONSIGNED, CONSIGNED, DELIVERED
        if(this.lgOrderStatus != null) { json.put("lgOrderStatus", this.lgOrderStatus); }
          
        return json;
    }
}
  