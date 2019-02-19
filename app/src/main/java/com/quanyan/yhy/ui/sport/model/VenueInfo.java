package com.quanyan.yhy.ui.sport.model;

/**
 * Created by shenwenjie on 2018/1/29.
 */

public class VenueInfo {

    private long activityId ;// 活动id
    private long placeId ;// 场地id
    private String startTime;
    private String venueName;
    private String venueDistance;
    private String sponsorName;
    private String sportContent;
    private String signedNum;
    private boolean isOffen;
    private boolean isSigned;
    private String sponsorHead;
    private String[] signedHeads;
    private long[] signedIds;


    public long[] getSignedIds() {
        return signedIds;
    }

    public void setSignedIds(long[] signedIds) {
        this.signedIds = signedIds;
    }

    public String getSponsorHead() {
        return sponsorHead;
    }

    public void setSponsorHead(String sponsorHead) {
        this.sponsorHead = sponsorHead;
    }

    public String[] getSignedHeads() {
        return signedHeads;
    }

    public void setSignedHeads(String[] signedHeads) {
        this.signedHeads = signedHeads;
    }

    public long getActivityId() {

        return activityId;
    }

    public void setActivityId(long activityId) {
        this.activityId = activityId;
    }

    public long getPlaceId() {
        return placeId;
    }

    public void setPlaceId(long placeId) {
        this.placeId = placeId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public String getVenueDistance() {
        return venueDistance;
    }

    public void setVenueDistance(String venueDistance) {
        this.venueDistance = venueDistance;
    }

    public String getSponsorName() {
        return sponsorName;
    }

    public void setSponsorName(String sponsorName) {
        this.sponsorName = sponsorName;
    }

    public String getSportContent() {
        return sportContent;
    }

    public void setSportContent(String sportContent) {
        this.sportContent = sportContent;
    }

    public String getSignedNum() {
        return signedNum;
    }

    public void setSignedNum(String signedNum) {
        this.signedNum = signedNum;
    }

    public boolean isOffen() {
        return isOffen;
    }

    public void setOffen(boolean offen) {
        isOffen = offen;
    }

    public boolean isSigned() {
        return isSigned;
    }

    public void setSigned(boolean signed) {
        isSigned = signed;
    }


}




