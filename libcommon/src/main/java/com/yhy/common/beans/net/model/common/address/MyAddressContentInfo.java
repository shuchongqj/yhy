// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.common.address;
    
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class MyAddressContentInfo implements Serializable{

    private static final long serialVersionUID = -5141835793535308715L;
    /**
     * 收件人姓名
     */
    public String recipientsName;
      
    /**
     * 收件人手机号码
     */
    public String recipientsPhone;
      
    /**
     * 邮政编码
     */
    public String zipCode;
      
    /**
     * 省级
     */
    public String province;
      
    /**
     * provinceCode
     */
    public String provinceCode;
      
    /**
     * 市级
     */
    public String city;
      
    /**
     * cityCode
     */
    public String cityCode;
      
    /**
     * 区县级
     */
    public String area;
      
    /**
     * areaCode
     */
    public String areaCode;

    /**
     * 省市区
     */
    public AddressInfo addressInfo;
      
    /**
     * 详细地址
     */
    public String detailAddress;
      
    /**
     * 是否默认收货地址 0否 1是
     */
    public String isDefault;
      
    /**
     * 户用ID
     */
    public long userId;
      
    /**
     * ID
     */
    public long id;
      
    /**
     * 改修时间
     */
    public long gmtModified;
      
    /**
     * 创建时间
     */
    public long gmtCreated;
      
    /**
     * 是否删除
     */
    public String isDel;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static MyAddressContentInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static MyAddressContentInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            MyAddressContentInfo result = new MyAddressContentInfo();

            // 收件人姓名

            if(!json.isNull("recipientsName")){
                result.recipientsName = json.optString("recipientsName", null);
            }
            // 收件人手机号码

            if(!json.isNull("recipientsPhone")){
                result.recipientsPhone = json.optString("recipientsPhone", null);
            }
            // 邮政编码

            if(!json.isNull("zipCode")){
                result.zipCode = json.optString("zipCode", null);
            }
            // 省级

            if(!json.isNull("province")){
                result.province = json.optString("province", null);
            }
            // provinceCode

            if(!json.isNull("provinceCode")){
                result.provinceCode = json.optString("provinceCode", null);
            }
            // 市级

            if(!json.isNull("city")){
                result.city = json.optString("city", null);
            }
            // cityCode

            if(!json.isNull("cityCode")){
                result.cityCode = json.optString("cityCode", null);
            }
            // 区县级

            if(!json.isNull("area")){
                result.area = json.optString("area", null);
            }
            // areaCode

            if(!json.isNull("areaCode")){
                result.areaCode = json.optString("areaCode", null);
            }
            // 省市区
            result.addressInfo = AddressInfo.deserialize(json.optJSONObject("addressInfo"));
            // 详细地址

            if(!json.isNull("detailAddress")){
                result.detailAddress = json.optString("detailAddress", null);
            }
            // 是否默认收货地址 0否 1是

            if(!json.isNull("isDefault")){
                result.isDefault = json.optString("isDefault", null);
            }
            // 户用ID
            result.userId = json.optLong("userId");
            // ID
            result.id = json.optLong("id");
            // 改修时间
            result.gmtModified = json.optLong("gmtModified");
            // 创建时间
            result.gmtCreated = json.optLong("gmtCreated");
            // 是否删除

            if(!json.isNull("isDel")){
                result.isDel = json.optString("isDel", null);
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

        // 收件人姓名
        if(this.recipientsName != null) { json.put("recipientsName", this.recipientsName); }

        // 收件人手机号码
        if(this.recipientsPhone != null) { json.put("recipientsPhone", this.recipientsPhone); }

        // 邮政编码
        if(this.zipCode != null) { json.put("zipCode", this.zipCode); }

        // 省级
        if(this.province != null) { json.put("province", this.province); }

        // provinceCode
        if(this.provinceCode != null) { json.put("provinceCode", this.provinceCode); }

        // 市级
        if(this.city != null) { json.put("city", this.city); }

        // cityCode
        if(this.cityCode != null) { json.put("cityCode", this.cityCode); }

        // 区县级
        if(this.area != null) { json.put("area", this.area); }

        // areaCode
        if(this.areaCode != null) { json.put("areaCode", this.areaCode); }

        // 省市区
        if (this.addressInfo != null) { json.put("addressInfo", this.addressInfo.serialize()); }

        // 详细地址
        if(this.detailAddress != null) { json.put("detailAddress", this.detailAddress); }

        // 是否默认收货地址 0否 1是
        if(this.isDefault != null) { json.put("isDefault", this.isDefault); }

        // 户用ID
        json.put("userId", this.userId);

        // ID
        json.put("id", this.id);

        // 改修时间
        json.put("gmtModified", this.gmtModified);

        // 创建时间
        json.put("gmtCreated", this.gmtCreated);

        // 是否删除
        if(this.isDel != null) { json.put("isDel", this.isDel); }

        return json;
    }
}
  