package com.yhy.common.beans.im.entity;

import android.text.TextUtils;
import com.yhy.common.beans.net.model.ProductCardModel;
import com.yhy.common.constants.DBConstant;
import com.yhy.common.constants.MessageConstant;
import com.yhy.common.utils.SequenceNumberMaker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created with Android Studio.
 * Title:ProductCardMessage
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:邓明佳
 * Date:2016/3/28
 * Time:10:30
 * Version 1.0
 */
public class ProductCardMessage extends MessageEntity {
    private static final long serialVersionUID = -6075720119954702617L;
    private int type;
    private String subType;
    private String title;
    private long productId;
    private String imgUrl;
    private String summary;
    private long price;
    private long childId;


    public ProductCardMessage() {
        msgId = SequenceNumberMaker.getInstance().makelocalUniqueMsgId();
    }

    private ProductCardMessage(MessageEntity entity) {
        // 父类主键
        id = entity.getId();
        msgId = entity.getMsgId();
        fromId = entity.getFromId();
        toId = entity.getToId();
        content = entity.getContent();
        msgType = entity.getMsgType();
        sessionKey = entity.getSessionKey();
        displayType = entity.getDisplayType();
        status = entity.getStatus();
        created = entity.getCreated();
        updated = entity.getUpdated();
        serviceId = entity.getServiceId();
    }


    public static ProductCardMessage parseFromDB(MessageEntity entity) {
        if (entity.getDisplayType() != DBConstant.SHOW_PRODUCT_CARD_TYPE) {
            throw new RuntimeException("#AudioMessage# parseFromDB,not SHOW_AUDIO_TYPE");
        }
        ProductCardMessage productCardMessage = new ProductCardMessage(entity);
        // 注意坑 啊
        String originContent = entity.getContent();
        try {
            JSONObject jsonObject = new JSONObject(originContent);
            productCardMessage.type = jsonObject.getInt("TYPE");
            JSONObject extraObject = jsonObject.getJSONObject("EXTRA");
            productCardMessage.subType = extraObject.getString("SUB_TYPE");
            JSONObject dataObject = extraObject.getJSONObject("DATA");
            productCardMessage.title = dataObject.getString("title");
            productCardMessage.productId = dataObject.getInt("id");
            if(dataObject.has("img_url")) {
                productCardMessage.imgUrl = dataObject.getString("img_url");
            }
            if(dataObject.has("summary")){
                productCardMessage.summary = dataObject.getString("summary");
            }
            if(dataObject.has("price")) {
                productCardMessage.price = dataObject.getLong("price");
            }
            productCardMessage.childId = dataObject.optLong("childId",0);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return productCardMessage;
    }

    public static ProductCardMessage buildForSend(ProductCardModel cardModel, UserEntity fromUser, PeerEntity peerEntity, int sessionType, long serviceId) {

        int nowTime = (int) (System.currentTimeMillis() / 1000);
        ProductCardMessage productCardMessage = new ProductCardMessage();
        productCardMessage.setFromId(fromUser.getPeerId());
        productCardMessage.setToId(peerEntity.getPeerId());
        productCardMessage.setCreated(nowTime);
        productCardMessage.setUpdated(nowTime);
        if (sessionType == DBConstant.SESSION_TYPE_SINGLE){
            productCardMessage.setMsgType(DBConstant.MSG_TYPE_SINGLE_PRODUCT_CARD);
        }
        productCardMessage.setServiceId(serviceId);
        productCardMessage.type = cardModel.getType();
        productCardMessage.subType = cardModel.getSubType();
        productCardMessage.title = cardModel.getTitle();
        productCardMessage.productId = (int)cardModel.getId();
        productCardMessage.imgUrl = cardModel.getImgUrl();
        productCardMessage.summary = cardModel.getSummary();
        productCardMessage.price = cardModel.getPrice();
        productCardMessage.childId = cardModel.getChildId();
        productCardMessage.setDisplayType(DBConstant.SHOW_PRODUCT_CARD_TYPE);
        productCardMessage.setStatus(MessageConstant.MSG_SENDING);
        productCardMessage.buildSessionKey(true);
        return productCardMessage;
    }


    /**
     * Not-null value.
     * DB 存数解析的时候需要
     */
    @Override
    public String getContent() {
        if (!TextUtils.isEmpty(content)) {
            return content;
        }
        JSONObject dataObject = new JSONObject();
        JSONObject extraObject = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        try {
            dataObject.put("title", title);
            dataObject.put("id", productId);
            dataObject.put("img_url", imgUrl);
            dataObject.put("summary", summary);
            dataObject.put("price", price);
            dataObject.put("childId",childId);
            //EXTRA
            extraObject.put("SUB_TYPE", subType);
            extraObject.put("DATA", dataObject);
            jsonObject.put("TYPE", type);
            jsonObject.put("EXTRA", extraObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }


    @Override
    public byte[] getSendContent() {
        try {
            return getContent().getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public long getChildId() {
        return childId;
    }
}
