// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.tm;

import com.yhy.common.beans.net.model.user.UserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TmMerchantInfo implements Serializable{

    private static final long serialVersionUID = 190593951411795002L;
    /**
     * 店铺名称
     */
    public String name;

    /**
     * 标题
     */
    public String title;

    /**
     * 地址
     */
    public String address;

    /**
     * 客服电话
     */
    public String serviceTel;

    /**
     * 商户负责人电话
     */
    public String merchantPrincipalTel;

    /**
     * 产品联系人电话
     */
    public String productContactTel;

    /**
     * 财务联系人电话
     */
    public String financialContactTel;

    /**
     * 城市编码
     */
    public int cityCode;

    /**
     * 城市名称
     */
    public String cityName;

    /**
     * 服务类型
     */
    public List<String> serviceType;
    /**
     * 人均消费
     */
    public long avgprice;

    /**
     * 销售数量
     */
    public long salesQuantity;

    /**
     * 店铺logo
     */
    public String logo;

    /**
     * 背景图
     */
    public String backgroudImage;

    /**
     * 轮播图
     */
    public List<String> loopImages;
    /**
     * 卖家信息
     */
    public UserInfo userInfo;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static TmMerchantInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static TmMerchantInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            TmMerchantInfo result = new TmMerchantInfo();

            // 店铺名称

            if(!json.isNull("name")){
                result.name = json.optString("name", null);
            }
            // 标题

            if(!json.isNull("title")){
                result.title = json.optString("title", null);
            }
            // 地址

            if(!json.isNull("address")){
                result.address = json.optString("address", null);
            }
            // 客服电话

            if(!json.isNull("serviceTel")){
                result.serviceTel = json.optString("serviceTel", null);
            }
            // 商户负责人电话

            if(!json.isNull("merchantPrincipalTel")){
                result.merchantPrincipalTel = json.optString("merchantPrincipalTel", null);
            }
            // 产品联系人电话

            if(!json.isNull("productContactTel")){
                result.productContactTel = json.optString("productContactTel", null);
            }
            // 财务联系人电话

            if(!json.isNull("financialContactTel")){
                result.financialContactTel = json.optString("financialContactTel", null);
            }
            // 城市编码
            result.cityCode = json.optInt("cityCode");
            // 城市名称

            if(!json.isNull("cityName")){
                result.cityName = json.optString("cityName", null);
            }
            // 服务类型
            JSONArray serviceTypeArray = json.optJSONArray("serviceType");
            if (serviceTypeArray != null) {
                int len = serviceTypeArray.length();
                result.serviceType = new
                        ArrayList<String>(len);
                for (int i = 0; i < len; i++) {

                    if(!serviceTypeArray.isNull(i)){
                        result.serviceType.add(serviceTypeArray.optString(i, null));
                    }else{
                        result.serviceType.add(i, null);
                    }

                }
            }

            // 人均消费
            result.avgprice = json.optLong("avgprice");
            // 销售数量
            result.salesQuantity = json.optLong("salesQuantity");
            // 店铺logo

            if(!json.isNull("logo")){
                result.logo = json.optString("logo", null);
            }
            // 背景图

            if(!json.isNull("backgroudImage")){
                result.backgroudImage = json.optString("backgroudImage", null);
            }
            // 轮播图
            JSONArray loopImagesArray = json.optJSONArray("loopImages");
            if (loopImagesArray != null) {
                int len = loopImagesArray.length();
                result.loopImages = new
                        ArrayList<String>(len);
                for (int i = 0; i < len; i++) {

                    if(!loopImagesArray.isNull(i)){
                        result.loopImages.add(loopImagesArray.optString(i, null));
                    }else{
                        result.loopImages.add(i, null);
                    }

                }
            }

            // 卖家信息
            result.userInfo = UserInfo.deserialize(json.optJSONObject("userInfo"));
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 店铺名称
        if(this.name != null) { json.put("name", this.name); }

        // 标题
        if(this.title != null) { json.put("title", this.title); }

        // 地址
        if(this.address != null) { json.put("address", this.address); }

        // 客服电话
        if(this.serviceTel != null) { json.put("serviceTel", this.serviceTel); }

        // 商户负责人电话
        if(this.merchantPrincipalTel != null) { json.put("merchantPrincipalTel", this.merchantPrincipalTel); }

        // 产品联系人电话
        if(this.productContactTel != null) { json.put("productContactTel", this.productContactTel); }

        // 财务联系人电话
        if(this.financialContactTel != null) { json.put("financialContactTel", this.financialContactTel); }

        // 城市编码
        json.put("cityCode", this.cityCode);

        // 城市名称
        if(this.cityName != null) { json.put("cityName", this.cityName); }

        // 服务类型 
        if (this.serviceType != null) {
            JSONArray serviceTypeArray = new JSONArray();
            for (String value : this.serviceType)
            {
                serviceTypeArray.put(value);
            }
            json.put("serviceType", serviceTypeArray);
        }

        // 人均消费
        json.put("avgprice", this.avgprice);

        // 销售数量
        json.put("salesQuantity", this.salesQuantity);

        // 店铺logo
        if(this.logo != null) { json.put("logo", this.logo); }

        // 背景图
        if(this.backgroudImage != null) { json.put("backgroudImage", this.backgroudImage); }

        // 轮播图 
        if (this.loopImages != null) {
            JSONArray loopImagesArray = new JSONArray();
            for (String value : this.loopImages)
            {
                loopImagesArray.put(value);
            }
            json.put("loopImages", loopImagesArray);
        }

        // 卖家信息
        if (this.userInfo != null) { json.put("userInfo", this.userInfo.serialize()); }

        return json;
    }


}
  