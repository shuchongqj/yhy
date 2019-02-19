package com.yhy.network.resp.snscenter;

import java.util.List;

public class GetUserSuperbListResp {

    public boolean hasNext;
    public List<LiveRecordResult> list;

    public static class LiveRecordResult{
        public long liveId; //直播id
        public long batchId; //批次id
        public long liveCategoryCode; //直播类别code
        public String liveCategoryName;//直播类别name
        public String liveTitle;//直播标题
        public String liveCover;//直播封面
        public String liveStatus;// 直播状态:创建(CREATE_LIVE)、直播(START_LIVE)、结束(END_LIVE)、回放(REPLAY_LIVE)、无效(INVALID_LIVE)
        public String status;// 直播记录状态:正常(NORMAL_LIVE) 删除（DELETE_LIVE）下架 (OFF_SHELVE_LIVE)
        public String locationCityCode;// 城市code
        public String locationCityName;// 城市名称
        public int onlineCount;// 在线人数
        public int viewCount;// 观看次数
        public String liveScreenType;// 视频横竖屏类型,横屏:HORIZONTAL, 竖屏:VERTICAL
        public String nickname;// 用户昵称

    }
}
