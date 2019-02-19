package com.yhy.common.beans.user;

public class UserFriendShipInfo {
    private long	followedCount;	// 用户关注数
    private long	fansCount;	// 用户粉丝数
    private long	ugcCount;

    public long getFollowedCount() {
        return followedCount;
    }

    public void setFollowedCount(long followedCount) {
        this.followedCount = followedCount;
    }

    public long getFansCount() {
        return fansCount;
    }

    public void setFansCount(long fansCount) {
        this.fansCount = fansCount;
    }

    public long getUgcCount() {
        return ugcCount;
    }

    public void setUgcCount(long ugcCount) {
        this.ugcCount = ugcCount;
    }
}
