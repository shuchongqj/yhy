package com.yhy.router;

public interface RouterPath {

    /********************************************************************     activity     ***************************************************************/
    /**
     * 主activity
     **/
    String ACTIVITY_MAIN = "/main/main";

    /**
     * 广告 activity
     **/
    String ACTIVITY_AD = "/ad/ad";

    /**
     * login activity
     **/
    String ACTIVITY_LOGIN = "/login/login";

    /**
     * 个人主页 activity
     */
    String ACTIVITY_HOMEPAGE = "/homepage/homepage";

    /**
     * 圈子动态详情页 activity
     */
    String ACTIVITY_CIRCLE_DETAIL = "/circle/detail";

    /**
     * 圈子话题列表 activity
     */
    String ACTIVITY_TOPIC_LIST = "/circle/topicList";

    /**
     * 圈子搜索界面 activity
     */
    String ACTIVITY_CIRCLE_SEARCH = "/circle/search";

    /**
     * 选择城市 activity
     */
    String ACTIVITY_CITY_SELECT = "/city/select";

    /**
     * 精彩视频列表 activity
     */
    String ACTIVITY_VIDEO_LIST = "/video/list";

    /**
     * 直播视频列表 activity
     */
    String ACTIVITY_LIVE_LIST = "/live/list";

    /**
     * 横屏直播 activity
     */
    String ACTIVITY_HORIZONTAL_LIVE = "/live/HorizontalLiveActivity";
    /**
     * 竖屏直播 activity
     */
    String ACTIVITY_VERTICAL_LIVE = "/live/VerticalLiveActivity";
    /**
     * 横屏回放 activity
     */
    String ACTIVITY_HORIZONTAL_REPLAY = "/live/HorizontalReplayActivity";
    /**
     * 竖屏回放 activity
     */
    String ACTIVITY_VERTICAL_REPLAY = "/live/VerticalReplayActivity";
    /**
     * 扫描二维码界面 activity
     */
    String ACTIVITY_CAPTURE_QRCODE = "/qrcode/CaptureActivity";

    /********************************************************************    fragment     ***************************************************************/

    /**
     * 圈子主fragment
     */
    String FRAGMENT_CIRCLE = "/circle/main";

    /**
     * 圈子推荐功能(包括一些以此为基础的动态Tab页面) fragment
     */
    String FRAGMENT_CIRCLE_RECOMMEND = "/circle/recommend";

    /**
     * 圈子coffeevideo fragment
     */
    String FRAGMENT_CIRCLE_COFFEEVIDEO = "/circle/coffeevideo";

    /**
     * 圈子dynamic fragment
     */
    String FRAGMENT_CIRCLE_DYNAMIC = "/circle/dynamic";

    /**
     * 圈子follow fragment
     */
    String FRAGMENT_CIRCLE_FOLLOW = "/circle/follow";

    /**
     * 圈子live fragment
     */
    String FRAGMENT_CIRCLE_LIVE = "/circle/live";

    /**
     * 圈子standardvideo fragment
     */
    String FRAGMENT_CIRCLE_STANDARDVIDEO = "/circle/standardvideo";


    /********************************************************************    service     ***************************************************************/

    /**
     * IUserService
     */
    String SERVICE_USER = "/user/service";
    /**
     * ITopicService
     */
    String SERVICE_TOPIC = "/topic/service";
}
