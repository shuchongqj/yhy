package com.yhy.common.beans.im.entity;

import android.text.TextUtils;

import com.yhy.common.constants.DBConstant;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:KnowLedgeMessage
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:邓明佳
 * Date:2016/11/22
 * Time:18:43
 * Version 1.0
 */
public class KnowLedgeMessage extends MessageEntity {
    private String title;
    private List<KnowLedgeItemBean> beans;
    private boolean buildJson = false;

    public void buildDisplayMessage() {
        if (TextUtils.isEmpty(content)) {
            return;
        }
        try {
            JSONObject object = new JSONObject(content);
            JSONObject jsonObject = object.getJSONObject("content");
            JSONArray jsonArray = jsonObject.getJSONArray("questionRecommendList");
            title = jsonObject.getString("title");
            beans = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                KnowLedgeItemBean bean = new KnowLedgeItemBean();
                final JSONObject item = jsonArray.getJSONObject(i);
                final long id = item.getLong("id");
                String title = item.getString("title");
                String h5Url = item.getString("h5Url");
                bean.setMsg(title);
                bean.setH5Url(h5Url);
                beans.add(bean);
            }
            buildJson = true;
        } catch (Exception e) {
            buildJson = true;
            return;
        }
    }

    public KnowLedgeMessage() {
    }

    public KnowLedgeMessage(MessageEntity entity) {
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

    public static MessageEntity parseFromDB(MessageEntity entity) {
        if (entity.getDisplayType() != DBConstant.SHOW_KNOWLEDGE_TYPE) {
            throw new RuntimeException("#KnowledgeMessage# parseFromDB,not SHOW_KNOWLEDGE_TYPE");
        }
        KnowLedgeMessage knowLedgeMessage = new KnowLedgeMessage(entity);

        return knowLedgeMessage;
    }


    public boolean isBuildJson() {
        return buildJson;
    }

    public String getTitle() {
        return title;
    }

    public List<KnowLedgeItemBean> getBeans() {
        return beans;
    }
}
