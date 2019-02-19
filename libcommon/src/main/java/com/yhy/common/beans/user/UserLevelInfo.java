package com.yhy.common.beans.user;

public class UserLevelInfo {

    private long	pointNumId;	// 会员积分id
    private int	levelId;	// 会员等级id
    private String	levelUrl;	// 会员等级url
    private int	levelMinPoint;	// 会员当前等级最小值
    private int	levelMaxPoint;	// 会员当前等级最大值
    private String	levelName;	// 会员等级名称
    private long	startTime;	// 会员生效日期
    private long	expireTime;	// 会员过期日期
    private long	memberLevelPoint;	// 会员成长值

    public long getPointNumId() {
        return pointNumId;
    }

    public void setPointNumId(long pointNumId) {
        this.pointNumId = pointNumId;
    }

    public int getLevelId() {
        return levelId;
    }

    public void setLevelId(int levelId) {
        this.levelId = levelId;
    }

    public String getLevelUrl() {
        return levelUrl;
    }

    public void setLevelUrl(String levelUrl) {
        this.levelUrl = levelUrl;
    }

    public int getLevelMinPoint() {
        return levelMinPoint;
    }

    public void setLevelMinPoint(int levelMinPoint) {
        this.levelMinPoint = levelMinPoint;
    }

    public int getLevelMaxPoint() {
        return levelMaxPoint;
    }

    public void setLevelMaxPoint(int levelMaxPoint) {
        this.levelMaxPoint = levelMaxPoint;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

    public long getMemberLevelPoint() {
        return memberLevelPoint;
    }

    public void setMemberLevelPoint(long memberLevelPoint) {
        this.memberLevelPoint = memberLevelPoint;
    }
}
