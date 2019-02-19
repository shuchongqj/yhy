package com.newyhy.beans;

import java.io.Serializable;
import java.util.List;

/**
 * 圈子直播，秀场，标准视频  Bean
 * Created by Jiervs on 2018/6/26.
 */

public class CircleLiveInfoResult implements Serializable {

    private static final long serialVersionUID = 2L;

    public long id;// id
    public String	nickname;	// 用户昵称
    public long	liveId;	// 直播ID
    public long	roomId;	// 直播间ID
    public String	liveCategoryName;	// 直播类别name
    public String	liveTitle;	// 直播标题
    public String	liveCover;	// 直播封面
    public String	liveStatus;	// 直播状态:创建直播(CREATE_LIVE)、直播(START_LIVE)、直播正在关闭(CLOSING_LIVE)、结束直播(END_LIVE)、回放(REPLAY_LIVE)、无效(INVALID_LIVE)
    public String	locationCityCode;	// 城市code
    public String	locationCityName;	// 城市名称
    public int	onlineCount;	// 在线人数
    public int	viewCount;	// 观看次数
    public String	liveRecordStatus;	// 直播记录状态:正常:NORMAL_LIVE、删除:DELETE_LIVE、下架:OFF_SHELVE_LIVE
    public String	liveScreenType;	// 直播横竖屏类型,横屏:HORIZONTAL, 竖屏:VERTICAL
    public int	type;	// 关注类型 0:未关注 1:单向关注 2:双向关注
    public int	commentNum;	// 评论数
    public int	supportNum;	// 点赞数
    public String	isSupport;	// 是否点赞 AVAILABLE：是 DELETED:否
    public UserInfo userInfo;
    public List<String> replayUrl;// 回放url
    public long	gmtCreated;	// 发布时间
}
