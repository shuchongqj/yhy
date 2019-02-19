package com.quanyan.yhy.ui.sport.model;

import java.util.List;

/**
 * Created by shenwenjie on 2018/1/29.
 */

public class LiveVideoInfo {


    private long liveId ;//直播ID
    private long roomId ;//直播间ID
    private long liveCategoryCode;//直播类别code
    private String liveCategoryName ;//直播类别name
    private String liveTitle ;//直播标题
    private String liveCover ;//直播封面
    private String liveStatus ;//直播状态:直播(START_LIVE)、结束(END_LIVE)、回放(REPLAY_LIVE)、无效(INVALID_LIVE)
    private String locationCityCode ;//城市code
    private String locationCityName ;//城市名称
    private long startDate ;//开始时间
    private long endDate ;//结束时间
    private int onlineCount ;//在线人数
    private int viewCount ;//观看次数
    private List<String> replayUrls ;//回放地址
    private String pushStreamUrl ;//推流地址
    private String pullStreamUrl ;//拉流地址
    private List<String> configContent ;//直播配置文本List
    private String status ;//直播记录状态:正常(NORMAL_LIVE) 删除（DELETE_LIVE）下架 (OFF_SHELVE_LIVE)
    private int liveScreenType ;//0:横向视频 1：竖向视频

    public long getLiveId() {
        return liveId;
    }

    public void setLiveId(long liveId) {
        this.liveId = liveId;
    }

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public long getLiveCategoryCode() {
        return liveCategoryCode;
    }

    public void setLiveCategoryCode(long liveCategoryCode) {
        this.liveCategoryCode = liveCategoryCode;
    }

    public String getLiveCategoryName() {
        return liveCategoryName;
    }

    public void setLiveCategoryName(String liveCategoryName) {
        this.liveCategoryName = liveCategoryName;
    }

    public String getLiveTitle() {
        return liveTitle;
    }

    public void setLiveTitle(String liveTitle) {
        this.liveTitle = liveTitle;
    }

    public String getLiveCover() {
        return liveCover;
    }

    public void setLiveCover(String liveCover) {
        this.liveCover = liveCover;
    }

    public String getLiveStatus() {
        return liveStatus;
    }

    public void setLiveStatus(String liveStatus) {
        this.liveStatus = liveStatus;
    }

    public String getLocationCityCode() {
        return locationCityCode;
    }

    public void setLocationCityCode(String locationCityCode) {
        this.locationCityCode = locationCityCode;
    }

    public String getLocationCityName() {
        return locationCityName;
    }

    public void setLocationCityName(String locationCityName) {
        this.locationCityName = locationCityName;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public int getOnlineCount() {
        return onlineCount;
    }

    public void setOnlineCount(int onlineCount) {
        this.onlineCount = onlineCount;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public List<String> getReplayUrls() {
        return replayUrls;
    }

    public void setReplayUrls(List<String> replayUrls) {
        this.replayUrls = replayUrls;
    }

    public String getPushStreamUrl() {
        return pushStreamUrl;
    }

    public void setPushStreamUrl(String pushStreamUrl) {
        this.pushStreamUrl = pushStreamUrl;
    }

    public String getPullStreamUrl() {
        return pullStreamUrl;
    }

    public void setPullStreamUrl(String pullStreamUrl) {
        this.pullStreamUrl = pullStreamUrl;
    }

    public List<String> getConfigContent() {
        return configContent;
    }

    public void setConfigContent(List<String> configContent) {
        this.configContent = configContent;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getLiveScreenType() {
        return liveScreenType;
    }

    public void setLiveScreenType(int liveScreenType) {
        this.liveScreenType = liveScreenType;
    }
}
