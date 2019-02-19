// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.trip;

import com.yhy.common.beans.net.model.user.UserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LineInfo implements Serializable {

    private static final long serialVersionUID = -305401471507920445L;
    /**
     * 线路id
     */
    public long id;

    /**
     * 线路名
     */
    public String name;

    /**
     * 创建者信息
     */
    public UserInfo userInfo;

    /**
     * 线路类型 REGULAR_LINE-线路 FLIGHT_HOTEL-机酒套餐 SCENIC_HOTEL-景酒套餐
     */
    public String type;

    /**
     * 线路首页图片url
     */
    public String logo_pic;

    /**
     * 现价
     */
    public long price;

    /**
     * 会员价
     */
    public long memberPrice;

    /**
     * 销量
     */
    public int sales;

    /**
     * 点赞数
     */
    public int likes;

    /**
     * 主题 保留字段
     */
    public String subject;

    /**
     * 推荐该产品的游咖名字列表
     */
    public List<String> recommendNames;
    /**
     * 标签列表
     */
    public List<String> tags;
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static LineInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static LineInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            LineInfo result = new LineInfo();

            // 线路id
            result.id = json.optLong("id");
            // 线路名

            if(!json.isNull("name")){
                result.name = json.optString("name", null);
            }
            // 创建者信息
            result.userInfo = UserInfo.deserialize(json.optJSONObject("userInfo"));
            // 线路类型 REGULAR_LINE-线路 FLIGHT_HOTEL-机酒套餐 SCENIC_HOTEL-景酒套餐

            if(!json.isNull("type")){
                result.type = json.optString("type", null);
            }
            // 线路首页图片url

            if(!json.isNull("logo_pic")){
                result.logo_pic = json.optString("logo_pic", null);
            }
            // 现价
            result.price = json.optLong("price");
            // 会员价
            result.memberPrice = json.optLong("memberPrice");
            // 销量
            result.sales = json.optInt("sales");
            // 点赞数
            result.likes = json.optInt("likes");
            // 主题 保留字段

            if(!json.isNull("subject")){
                result.subject = json.optString("subject", null);
            }
            // 推荐该产品的游咖名字列表
            JSONArray recommendNamesArray = json.optJSONArray("recommendNames");
            if (recommendNamesArray != null) {
                int len = recommendNamesArray.length();
                result.recommendNames = new
                        ArrayList<String>(len);
                for (int i = 0; i < len; i++) {

                    if(!recommendNamesArray.isNull(i)){
                        result.recommendNames.add(recommendNamesArray.optString(i, null));
                    }else{
                        result.recommendNames.add(i, null);
                    }

                }
            }

            // 标签列表
            JSONArray tagsArray = json.optJSONArray("tags");
            if (tagsArray != null) {
                int len = tagsArray.length();
                result.tags = new
                        ArrayList<String>(len);
                for (int i = 0; i < len; i++) {

                    if(!tagsArray.isNull(i)){
                        result.tags.add(tagsArray.optString(i, null));
                    }else{
                        result.tags.add(i, null);
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

        // 线路id
        json.put("id", this.id);

        // 线路名
        if(this.name != null) { json.put("name", this.name); }

        // 创建者信息
        if (this.userInfo != null) { json.put("userInfo", this.userInfo.serialize()); }

        // 线路类型 REGULAR_LINE-线路 FLIGHT_HOTEL-机酒套餐 SCENIC_HOTEL-景酒套餐
        if(this.type != null) { json.put("type", this.type); }

        // 线路首页图片url
        if(this.logo_pic != null) { json.put("logo_pic", this.logo_pic); }

        // 现价
        json.put("price", this.price);

        // 会员价
        json.put("memberPrice", this.memberPrice);

        // 销量
        json.put("sales", this.sales);

        // 点赞数
        json.put("likes", this.likes);

        // 主题 保留字段
        if(this.subject != null) { json.put("subject", this.subject); }

        // 推荐该产品的游咖名字列表 
        if (this.recommendNames != null) {
            JSONArray recommendNamesArray = new JSONArray();
            for (String value : this.recommendNames)
            {
                recommendNamesArray.put(value);
            }
            json.put("recommendNames", recommendNamesArray);
        }

        // 标签列表 
        if (this.tags != null) {
            JSONArray tagsArray = new JSONArray();
            for (String value : this.tags)
            {
                tagsArray.put(value);
            }
            json.put("tags", tagsArray);
        }

        return json;
    }

}
  