package com.yhy.common.beans.im.entity;

import com.yhy.common.beans.net.model.trip.ShortItem;
import com.yhy.common.config.ContextHelper;
import com.yhy.common.constants.DBConstant;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : yingmu on 15-1-8.
 * @email : yingmu@mogujie.com.
 */
public class RecentInfo {
    /**sessionEntity*/
    private String sessionKey;
    private long peerId;
    private int sessionType;
    private int latestMsgType;
    private int latestMsgId;
    private String latestMsgData;
    private int updateTime;

    /**unreadEntity*/
    private int unReadCnt;

    /**group/userEntity*/
    private String name;
    private List<String> avatar;

    /**是否置顶*/
    private boolean isTop = false;
    /**是否屏蔽信息*/
    private boolean isForbidden = false;
    private int status;
    private long serviceId = 0;

    public RecentInfo(){}
    public RecentInfo(SessionEntity sessionEntity,UserEntity entity,UnreadEntity unreadEntity,ShortItem shortItem){
        sessionKey = sessionEntity.getSessionKey();
        peerId = sessionEntity.getPeerId();
        sessionType =  sessionEntity.getPeerType();
        latestMsgType = sessionEntity.getLatestMsgType();
        latestMsgId = sessionEntity.getLatestMsgId();
        latestMsgData = sessionEntity.getLatestMsgData();
        updateTime = sessionEntity.getUpdated();
        status = sessionEntity.getStatus();
        serviceId = sessionEntity.getServiceId();
        if(unreadEntity !=null)
        unReadCnt = unreadEntity.getUnReadCnt();

        if (sessionType == DBConstant.SESSION_TYPE_SINGLE) {
            if (entity != null) {
                name = entity.getMainName();
                ArrayList<String> avatarList = new ArrayList<>();
                avatarList.add(entity.getAvatar());
                avatar = avatarList;
            }
        } else if (sessionType == DBConstant.SESSION_TYPE_CONSULT) {
            if (shortItem != null) {
                name = shortItem.title;
                ArrayList<String> avatarList = new ArrayList<>();
                avatarList.add(ContextHelper.getImageFullUrl(shortItem.mainPicUrl));
                avatar = avatarList;
            }
        }

    }

    public RecentInfo(SessionEntity sessionEntity, UnreadEntity unreadEntity) {
        sessionKey = sessionEntity.getSessionKey();
        peerId = sessionEntity.getPeerId();
        sessionType = DBConstant.SESSION_TYPE_NOTIFICATION;
        latestMsgType = sessionEntity.getLatestMsgType();
        latestMsgId = sessionEntity.getLatestMsgId();
        latestMsgData = sessionEntity.getLatestMsgData();
        updateTime = sessionEntity.getUpdated();
        status = sessionEntity.getStatus();
        if (unreadEntity != null)
            unReadCnt = unreadEntity.getUnReadCnt();
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public long getPeerId() {
        return peerId;
    }

    public void setPeerId(int peerId) {
        this.peerId = peerId;
    }

    public int getSessionType() {
        return sessionType;
    }

    public void setSessionType(int sessionType) {
        this.sessionType = sessionType;
    }

    public int getLatestMsgType() {
        return latestMsgType;
    }

    public void setLatestMsgType(int latestMsgType) {
        this.latestMsgType = latestMsgType;
    }

    public int getLatestMsgId() {
        return latestMsgId;
    }

    public void setLatestMsgId(int latestMsgId) {
        this.latestMsgId = latestMsgId;
    }

    public String getLatestMsgData() {
        return latestMsgData;
    }

    public void setLatestMsgData(String latestMsgData) {
        this.latestMsgData = latestMsgData;
    }

    public int getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(int updateTime) {
        this.updateTime = updateTime;
    }

    public int getUnReadCnt() {
        return unReadCnt;
    }

    public void setUnReadCnt(int unReadCnt) {
        this.unReadCnt = unReadCnt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getAvatar() {
        return avatar;
    }

    public void setAvatar(List<String> avatar) {
        this.avatar = avatar;
    }

    public boolean isTop() {
        return isTop;
    }
    public boolean isForbidden()
    {
        return isForbidden;
    }

    public void setTop(boolean isTop) {
        this.isTop = isTop;
    }

    public void setForbidden(boolean isForbidden) {
        this.isForbidden = isForbidden;
    }

    public int getStatus() {
        return status;
    }

    public long getServiceId(){
        return serviceId;
    }
}
