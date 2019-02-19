package com.yhy.common.beans.im.entity;

import com.yhy.common.utils.EntityChangeEngine;

/**
 * @author : yingmu on 15-1-6.
 * @email : yingmu@mogujie.com.
 * <p/>
 * 未读session实体，并未保存在DB中
 */
public class UnreadEntity {
    private String sessionKey;
    private long peerId;
    private int sessionType;
    private int unReadCnt;
    private int laststMsgId;
    private String latestMsgData;
    private long serviceId;
    private String title;
    private String ntfCode;
    private String ntfVaule;
    private boolean needNotify = true;

    public boolean isNeedNotify() {
        return needNotify;
    }

    public void setNeedNotify(boolean needNotify) {
        this.needNotify = needNotify;
    }

    public String getNtfCode() {
        return ntfCode;
    }

    public void setNtfCode(String ntfCode) {
        this.ntfCode = ntfCode;
    }

    public String getNtfVaule() {
        return ntfVaule;
    }

    public void setNtfVaule(String ntfVaule) {
        this.ntfVaule = ntfVaule;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private boolean isForbidden = false;

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public long getPeerId() {
        return peerId;
    }

    public void setPeerId(long peerId) {
        this.peerId = peerId;
    }

    public int getSessionType() {
        return sessionType;
    }

    public void setSessionType(int sessionType) {
        this.sessionType = sessionType;
    }

    public int getUnReadCnt() {
        return unReadCnt;
    }

    public void setUnReadCnt(int unReadCnt) {
        this.unReadCnt = unReadCnt;
    }

    public int getLaststMsgId() {
        return laststMsgId;
    }

    public void setLaststMsgId(int laststMsgId) {
        this.laststMsgId = laststMsgId;
    }

    public String getLatestMsgData() {
        return latestMsgData;
    }

    public void setLatestMsgData(String latestMsgData) {
        this.latestMsgData = latestMsgData;
    }

    public boolean isForbidden() {
        return isForbidden;
    }

    public void setForbidden(boolean isForbidden) {
        this.isForbidden = isForbidden;
    }

    public long getServiceId() {
        return serviceId;
    }

    public void setServiceId(long serviceId) {
        this.serviceId = serviceId;
    }

    @Override
    public String toString() {
        return "UnreadEntity{" +
                "sessionKey='" + sessionKey + '\'' +
                ", peerId=" + peerId +
                ", sessionType=" + sessionType +
                ", unReadCnt=" + unReadCnt +
                ", laststMsgId=" + laststMsgId +
                ", latestMsgData='" + latestMsgData + '\'' +
                ", isForbidden=" + isForbidden +
                '}';
    }

    public String buildSessionKey() {
        if (sessionType <= 0 || peerId <= 0) {
            throw new IllegalArgumentException(
                    "SessionEntity buildSessionKey error,cause by some params <=0");
        }
        sessionKey = EntityChangeEngine.getSessionKey(peerId, sessionType, serviceId);
        return sessionKey;
    }
}
