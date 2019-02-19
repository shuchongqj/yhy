package com.mogujie.tt.imservice.event;

import com.google.protobuf.ByteString;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created with Android Studio.
 * Title:SendCarInfoEvent
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:邓明佳
 * Date:2016/11/7
 * Time:14:47
 * Version 1.0
 */
public class SendCarInfoEvent {
    /**
     * 经度
     */
    private Double lng;
    /**
     * 纬度
     */
    private Double lat;
    /**
     * 车头方向
     */
    private float direction;

    public SendCarInfoEvent(Double longitude, Double latitude, float direction) {
        this.lng = longitude;
        this.lat = latitude;
        this.direction = direction;
    }

    //获取发送内容
    public ByteString getSendContentForSocket() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("lng", lng);
            jsonObject.put("lat", lat);
            jsonObject.put("direction", direction);
            return ByteString.copyFrom(jsonObject.toString().getBytes("utf-8"));
        } catch (JSONException e) {
            return null;
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }
}
