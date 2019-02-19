package com.yhy.common.beans.net.model.tm;

import com.yhy.common.beans.net.model.base.BaseShrink;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:ItemSkuVO
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2015-11-27
 * Time:15:26
 * Version 1.0
 * Description:
 */
public class ItemSkuVO extends BaseShrink implements Serializable{

    private static final long serialVersionUID = 992149207661847224L;
    /**
     * id
     */
    public long id;

    /**
     * 商品id
     */
    public long itemId;

    /**
     * spu id
     */
    public long spuId;

    /**
     * 类目id
     */
    public long categoryId;

    /**
     * sku标题，所有显示销售属性值拼装而成
     */
    public String title;

    /**
     * 库存/购买数量
     */
    public int stockNum;

    /**
     * 价格
     */
    public long price;

    /**
     * 折扣
     */
    public long discount;

    /**
     * 属性与属性值列表
     */
    public List<ItemSkuPVPairVO> itemSkuPVPairList;
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static ItemSkuVO deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static ItemSkuVO deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            ItemSkuVO result = new ItemSkuVO();

            // id
            result.id = json.optLong("id");
            // 商品id
            result.itemId = json.optLong("itemId");
            // spu id
            result.spuId = json.optLong("spuId");
            // 类目id
            result.categoryId = json.optLong("categoryId");
            // sku标题，所有显示销售属性值拼装而成

            if(!json.isNull("title")){
                result.title = json.optString("title", null);
            }
            // 库存/购买数量
            result.stockNum = json.optInt("stockNum");
            // 价格
            result.price = json.optLong("price");
            // 折扣
            result.discount = json.optLong("discount");
            // 属性与属性值列表
            JSONArray itemSkuPVPairListArray = json.optJSONArray("itemSkuPVPairList");
            if (itemSkuPVPairListArray != null) {
                int len = itemSkuPVPairListArray.length();
                result.itemSkuPVPairList = new ArrayList<ItemSkuPVPairVO>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = itemSkuPVPairListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.itemSkuPVPairList.add(ItemSkuPVPairVO.deserialize(jo));
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

        // 商品id
        json.put("itemId", this.itemId);

        // spu id
        json.put("spuId", this.spuId);

        // 类目id
        json.put("categoryId", this.categoryId);

        // sku标题，所有显示销售属性值拼装而成
        if(this.title != null) { json.put("title", this.title); }

        // 库存/购买数量
        json.put("stockNum", this.stockNum);

        // 价格
        json.put("price", this.price);

        // 折扣
        json.put("discount", this.discount);

        // 属性与属性值列表
        if (this.itemSkuPVPairList != null) {
            JSONArray itemSkuPVPairListArray = new JSONArray();
            for (ItemSkuPVPairVO value : this.itemSkuPVPairList)
            {
                if (value != null) {
                    itemSkuPVPairListArray.put(value.serialize());
                }
            }
            json.put("itemSkuPVPairList", itemSkuPVPairListArray);
        }

        return json;
    }

}
