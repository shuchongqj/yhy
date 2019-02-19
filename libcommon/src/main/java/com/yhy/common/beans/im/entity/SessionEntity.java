package com.yhy.common.beans.im.entity;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END

import com.yhy.common.utils.EntityChangeEngine;

/**
 * Entity mapped to table Session.
 */
public class SessionEntity {

    private Long id;
    /**
     * Not-null value.
     */
    private String sessionKey;
    private long peerId;
    private int peerType;
    private int latestMsgType;
    private int latestMsgId;
    /**
     * Not-null value.
     */
    private String latestMsgData;
    private long talkId;
    private int created;
    private int updated;
    private int status;
    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END
    private long serviceId;

    public SessionEntity() {
    }

    public SessionEntity(Long id) {
        this.id = id;
    }

    public SessionEntity(Long id, String sessionKey, long peerId, int peerType, int latestMsgType, int latestMsgId, String latestMsgData, int talkId, int created, int updated, int status,int serviceId) {
        this.id = id;
        this.sessionKey = sessionKey;
        this.peerId = peerId;
        this.peerType = peerType;
        this.latestMsgType = latestMsgType;
        this.latestMsgId = latestMsgId;
        this.latestMsgData = latestMsgData;
        this.talkId = talkId;
        this.created = created;
        this.updated = updated;
        this.status = status;
        this.serviceId = serviceId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Not-null value.
     */
    public String getSessionKey() {
        return sessionKey;
    }

    /**
     * Not-null value; ensure this value is available before it is saved to the database.
     */
    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public long getPeerId() {
        return peerId;
    }

    public void setPeerId(long peerId) {
        this.peerId = peerId;
    }

    public int getPeerType() {
        return peerType;
    }

    public void setPeerType(int peerType) {
        this.peerType = peerType;
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

    /**
     * Not-null value.
     */
    public String getLatestMsgData() {
        return latestMsgData;
    }

    /**
     * Not-null value; ensure this value is available before it is saved to the database.
     */
    public void setLatestMsgData(String latestMsgData) {
        this.latestMsgData = latestMsgData;
    }

    public long getTalkId() {
        return talkId;
    }

    public void setTalkId(long talkId) {
        this.talkId = talkId;
    }

    public int getCreated() {
        return created;
    }

    public void setCreated(int created) {
        this.created = created;
    }

    public int getUpdated() {
        return updated;
    }

    public void setUpdated(int updated) {
        this.updated = updated;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    // KEEP METHODS - put your custom methods here
    public String buildSessionKey() {
//        if (peerType <= 0 || peerId <= 0) {
//            throw new IllegalArgumentException(
//                    "SessionEntity buildSessionKey error,cause by some params <=0");
//        }
        sessionKey = EntityChangeEngine.getSessionKey(peerId, peerType,serviceId);
        return sessionKey;
    }

    public long getServiceId(){
        return serviceId;
    }

    public void setServiceId(long serviceId) {
        this.serviceId = serviceId;
    }
    // KEEP METHODS END

}