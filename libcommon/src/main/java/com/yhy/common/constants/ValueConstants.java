package com.yhy.common.constants;


import com.yhy.common.types.AppDebug;

/**
 * 常量
 * Created by zhaoxp on 2015-11-10.
 */
public class ValueConstants {
    public static final String MASTER_CONSULT_SHARE_QR_CODE_URL = "http://www.yingheying.com/d/daren_zx";
    //crash存储
    public static final String YM_CRASH_BUG_REPORT = "YM_crash_bug_report.log";
    /**
     * 默认头图的宽高比
     */
    public static final float SCALE_VALUE_BANNER = 750 / 300f;//图片的宽高比
    public static final float SCALE_PRODUCT_HEADER_IMG = 750 / 420.0f;
    public static final float SCALE_VALUE = 420 / 750.0f;//图片的宽高比
    public static final float SCALE_MASTER_OPERATION_IMG = 240 / 690.0f;//图片的宽高比
    public static final float SCALE_PRODUCT_LIST_IMG = 750 / 360.0f;//图片的宽高比
    //SPLASH宽高比
//    public static final float SCALE_SPLASH = 1242 / 1800.0f;
    public static final float SCALE_SPLASH = 0.8f;
    public static final int SDK_PAY_FLAG = 1;//支付宝支付
    public static final int SDK_WEIXIN_PAY_FLAG = 2;//微信支付
    //上传个人主页最大的照片数
    public static final int MAX_UPDATE_PERSON_PICTURES = 15;

    //无数据
    public static final int MSG_HAS_NO_DATA = 0x1111;
    //从缓存加载
    public static final int MSG_DATA_FROM_CACHE = 0x100;

    /**
     * ------------最大图片选择数量------------
     **/
    public static final int MAX_SELECT_PICTURE = 9;
    public static final int MAX_SELECT_PICTURE_COMMENT = 3;

    public static final int MAX_SELECT_DEST_CITYS = 5;

    public static final int HEAD_BORD_WIDTH = 15;

    /**
     * ------------分页大小------------
     **/
    public static final int PAGESIZE = 10;
    public static final int PAGESIZE_FIVE = 5;

    public static final int POST_LIVE = 3;//发表直播成功

    public static final int RESULT_LIVE_DATA = 110;//直播回传数据

    /**
     * 全局监听任务完成的广播action
     */
    public static final String BROADCASTRECEIVER_ALL_TAST_COMPLETE = "com.task.complete";

    /**
     * 性别类型
     **/
    public static final String TYPE_SEX_MALE = "MALE";
    public static final String TYPE_SEX_FEMAIL = "FEMALE";


    public static final String TYPE_MALE = "2";
    public static final String TYPE_MALE_OR_FEMAIL = "1";
    public static final String TYPE_FEMAIL = "3";

    /**
     * 是否加入俱乐部
     **/
    public static final String TYPE_AVAILABLE = "AVAILABLE";
    public static final String TYPE_DELETED = "DELETED";

    /**
     * 线路详情商家类型   行程项类型 RESTAURANT餐厅/HOTEL酒店/SCENIC景点
     **/
    public static final String TYPE_TRAVEL_BREAKFAST = "BREAKFAST";
    public static final String TYPE_TRAVEL_LUNCH = "LUNCH";
    public static final String TYPE_TRAVEL_DINNER = "DINNER";

    /**
     * 点赞类型
     **/
    public static final String TYPE_PRAISE_LIVESUP = "LIVESUP";
    public static final String TYPE_PRAISE_TRAVELNOTES = "TRAVELSPECIALSUP";
    public static final String TYPE_PRAISE_WONDERFULL_VIDEO = "WONDERFULL_VIDEO";


    /**
     * 订单类型
     **/

    public static final int KEY_ORDER_STATUS_ALL = 0;
    public static final int KEY_ORDER_STATUS_UNPAY = 1;
    public static final int KEY_ORDER_STATUS_VALID = 2;
    public static final int KEY_ORDER_STATUS_UNCOMMENT = 3;

    /**
     * 标签类型
     **/
    public static final String TYPE_TAG_LIVESUPTAG = "LIVESUPTAG";

    /**
     * 评论类型
     **/
    public static final String TYPE_COMMENT_LIVESUP = "LIVECOM";
    public static final String TYPE_COMMENT_DYNAMICSUP = "DYNAMICCOM";
    public static final String TYPE_COMMENT_WONDERFULL_VIDEO = "WONDERFULL_VIDEO";

    /**
     * -----------------点赞判断---------------
     **/
    public static final int PRAISE = 0;
    public static final int UNPRAISE = 1;

    //请求码
    public static final int MSG_LIST_OK = 0x0001;
    public static final int MSG_LIST_ERROR = 0x0002;

    public static final int MSG_DETAIL_OK = 0x0003;
    public static final int MSG_DETAIL_ERROR = 0x0004;

    //标签
    public static final int MSG_LABELS_OK = 0x0005;
    public static final int MSG_LABELS_ERROR = 0x0006;

    //点赞
    public static final int MSG_PRAISE_OK = 0x0007;
    public static final int MSG_PRAISE_ERROR = 0x0008;

    //评论
    public static final int MSG_COMMENT_OK = 0x0009;
    public static final int MSG_COMMENT_ERROR = 0x0010;

    /*---------------------------------首页-----------------------------------*/
    //首页
    public static final int MSG_GET_FIRST_PAGE_LIST_OK = 0x10004;
    public static final int MSG_GET_FIRST_PAGE_LIST_KO = 0x10005;
    //广告位
    public static final int MSG_GET_BANNER_LIST_OK = 0x10006;
    //达人首页
    public static final int MSG_GET_SECOND_PAGE_LIST_OK = 0x1000c;
    public static final int MSG_GET_SECOND_PAGE_LIST_KO = 0x1000d;
    //线路达人首页广告
    public static final int MSG_GET_MASTER_PRODUCTS_AD_OK = 0x10010;
    public static final int MSG_GET_MASTER_PRODUCTS_AD_KO = 0x10011;
    //线路达人列表
    public static final int MSG_GET_MASTER_PRODUCTS_OK = 0x10012;
    public static final int MSG_GET_MASTER_PRODUCTS_KO = 0x10013;

    //获取跟团游首页的资源位
    public static final int MSG_GET_PACKAGE_TOUR_HOME_PAGE_RESOURCE_LIST_OK = 0x1001c;
    public static final int MSG_GET_PACKAGE_TOUR_HOME_PAGE_RESOURCE_LIST_KO = 0x1002c;
    //获取自由行首页的资源位
    public static final int MSG_GET_FREE_WALK_HOME_PAGE_RESOURCE_LIST_OK = 0x1003c;
    public static final int MSG_GET_FREE_WALK_HOME_PAGE_RESOURCE_LIST_KO = 0x1004c;
    //获取同城活动首页资源位
    public static final int MSG_GET_CITY_ACTIVITY_HOME_PAGE_RESOURCE_LIST_OK = 0x1005c;
    public static final int MSG_GET_CITY_ACTIVITY_HOME_PAGE_RESOURCE_LIST_KO = 0x1006c;
    //周边玩乐首页资源位
    public static final int MSG_GET_ARROUND_FUN_HOME_PAGE_RESOURCE_LIST_OK = 0x1007c;
    public static final int MSG_GET_ARROUND_FUN_HOME_PAGE_RESOURCE_LIST_KO = 0x1008c;
    //获取线路首页推荐位
    public static final int MSG_LINE_HOME_PAGE_RECOMMAND_LIST_OK = 0x1009c;
    public static final int MSG_LINE_HOME_PAGE_RECOMMAND_LIST_KO = 0x1010c;
    /*---------------------------------首页-----------end------------------------*/

    /*---------------------------------周边-----------------------------------*/
    public static final int MSG_CLUB_LIST_OK = 0x20001;
    public static final int MSG_CLUB_LIST_ERROR = 0x20002;

    public static final int MSG_ACTIVE_LIST_OK = 0x20003;
    public static final int MSG_ACTIVE_LIST_ERROR = 0x20004;

    public static final int MSG_HIDELOADING_DIALOG = 0x20005;
    public static final int MSG_SHOW_DIALOG = 0x20006;
    public static final int MSG_UPLOADIMAGE_OK = 0x20007;
    public static final int MSG_UPLOADIMAGE_KO = 0x200071;
    //视频上传
    public static final int MSG_UPLOAD_VIDEO_OK = 0x200072;
    public static final int MSG_UPLOAD_VIDEO_KO = 0x200073;

    public static final int MSG_PUBLISH_LIVE_OK = 0x20009;
    //俱乐部详情信息
    public static final int MSG_CLUB_DETAIL_INFO_OK = 0x20015;
    public static final int MSG_CLUB_DETAIL_INFO_KO = 0x20016;

    //俱乐部详情动态列表
    public static final int MSG_CLUB_DETAIL_DYNAMIC_OK = 0x20017;
    public static final int MSG_CLUB_DETAIL_DYNAMIC_ERROR = 0x20018;

    //加入俱乐部
    public static final int MSG_CLUB_DETAIL_JOIN_OK = 0x20021;
    public static final int MSG_CLUB_DETAIL_JOIN_KO = 0x20022;
    //退出俱乐部
    public static final int MSG_CLUB_DETAIL_EXIT_KO = 0x20023;
    public static final int MSG_CLUB_DETAIL_EXIT_OK = 0x20024;

    //俱乐部搜索
    public static final int MSG_CLUB_SEARCH_LIST_OK = 0x20025;
    public static final int MSG_CLUB_SEARCH_LIST_ERROR = 0x20026;
    //活动搜索
    public static final int MSG_ACTIVE_SEARCH_LIST_OK = 0x20027;
    public static final int MSG_ACTIVE_SEARCH_LIST_ERROR = 0x20028;
    //活动详情更多成员
    public static final int MSG_ACTIVE_DETAIL_MORE_MEMBER_OK = 0x20027;
    public static final int MSG_ACTIVE_DETAIL_MORE_MEMBER_ERROR = 0x20028;
    //删除直播
    public static final int MSG_DELETE_LIVE_OK = 0x20029;
    public static final int MSG_DELETE_LIVE_ERROR = 0x20030;
    //删除动态
    public static final int MSG_DELETE_DYNAMIC_OK = 0x20029;
    public static final int MSG_DELETE_DYNAMIC_ERROR = 0x20030;

    public static final int MSG_GET_COMPLAINT_LIST_OK = 0x20035;
    public static final int MSG_GET_COMPLAINT_LIST_KO = 0x20036;

    public static final int MSG_SUBMIT_COMPLAINT_OK = 0x20037;
    public static final int MSG_SUBMIT_COMPLAINT_KO = 0x20038;
    //屏蔽成功
    public static final int MSG_BLACK_OK = 0x20039;
    public static final int MSG_BLACK_KO = 0x20040;
    /*---------------------------------周边----------end-------------------------*/

    /*---------------------------------发现-----------------------------------*/
    //游记列表
    public static final int MSG_TRAVELNOTES_LIST_OK = 0x30001;
    public static final int MSG_TRAVELNOTES_LIST_ERROR = 0x30002;
    //游记详情
    public static final int MSG_TRAVELNOTES_DETAIL_OK = 0x30003;
    public static final int MSG_TRAVELNOTES_DETAIL_ERROR = 0x30004;
    //直播详情
    public static final int MSG_LIVE_DETAIL_OK = 0x30005;
    public static final int MSG_LIVE_DEATIL_ERROR = 0x3006;
    //话题搜索
    public static final int MSG_TAG_SEARCH_OK = 0x30009;
    public static final int MSG_TAG_SEARCH_ERROR = 0x30010;
	/*---------------------------------发现--------end---------------------------*/

    /*---------------------------------行程-----------------------------------*/
    //线路详情
    public static final int MSG_LINE_DETAIL_OK = 0x40001;
    public static final int MSG_LINE_DETAIL_ERROR = 0x400002;
    //行程列表
    public static final int MSG_TRIP_DATA_OK = 0x40003;
    public static final int MSG_TRIP_DATA_ERROR = 0x40004;

    //线路出发日期
    public static final int MSG_GET_DEPARTURE_DATE_DATA_OK = 0x40005;
    public static final int MSG_GET_DEPARTURE_DATE_DATA_KO = 0x40006;

    //删除评论
    public static final int MSG_DELETE_COMMENT = 0x40008;
    public static final int MSG_DELETE_COMMENT_ERROR = 0x40009;

    /**
     * 获取线路筛选列表
     */
    public static final int MSG_GET_LINE_FILTER_OK = 0x40009;
    public static final int MSG_GET_LINE_FILTER_ERROR = 0x40010;

    /**
     * 获取sku和订单上下文
     */
    public static final int MSG_GET_SKU_CONTEXT_OK = 0x40011;
    public static final int MSG_GET_SKU_CONTEXT_ERROR = 0x40012;

	/*---------------------------------行程----------end-------------------------*/

    /*---------------------------------我的-----------------------------------*/
    public static final int MINE = 0x50001;

    //获取游客列表
    public static final int MSG_GET_VISITIOR_LIST_OK = 0x50002;
    public static final int MSG_GET_VISITIOR_LIST_KO = 0x50003;
    //添加游客信息
    public static final int MSG_ADD_UPDATE_VISITOR_INFO_OK = 0x50004;
    public static final int MSG_ADD_UPDATE_VISITOR_INFO_KO = 0x50005;
    //删除游客信息
    public static final int MSG_DELETE_VISITOR_OK = 0x50006;
    public static final int MSG_DELETE_VISITOR_KO = 0x50007;
    //修改游客信息
    public static final int MSG_UPDATE_VISITOR_OK = 0x50008;
    public static final int MSG_UPDATE_VISITOR_KO = 0x50009;

    //获取地址列表
    public static final int MSG_GET_ADDRESS_LIST_OK = 0x50010;
    public static final int MSG_GET_ADDRESS_LIST_KO = 0x50011;
    //新增地址信息
    public static final int MSG_ADD_UPDATE_ADDRESS_INFO_OK = 0x50012;
    public static final int MSG_ADD_UPDATE_ADDRESS_INFO_KO = 0x50013;
    //删除地址信息
    public static final int MSG_DELETE_ADDRESS_OK = 0x50014;
    public static final int MSG_DELETE_ADDRESS_KO = 0x50015;
    //修改地址信息
    public static final int MSG_UPDATE_ADDRESS_OK = 0x50016;
    public static final int MSG_UPDATE_ADDRESS_KO = 0x50017;

    //反馈信息
    public static final int MSG_FEEDBACK_OK = 0x50018;
    public static final int MSG_FEEDBACK_KO = 0x50019;

    //昵称
    public static final int SELECT_TYPE_NICK_NAME = 0x50020;
    //姓名
    public static final int SELECT_TYPE_USER_NAME = 0x50021;
    //旅行宣言
    public static final int SELECT_TYPE_SIGN = 0x50022;
    //QQ登录
    public static final int MSG_QQ_LOGIN_OK = 0x50023;
    public static final int MSG_QQ_LOGIN_KO = 0x50024;
    //微信登录
    public static final int MSG_WEIXIN_LOGIN_OK = 0x50025;
    public static final int MSG_WEIXIN_LOGIN_KO = 0x50026;
    //微博登录
    public static final int MSG_WEIBO_LOGIN_OK = 0x50027;
    public static final int MSG_WEIBO_LOGIN_KO = 0x50028;
    //绑定手机号
    public static final int MSG_BIND_MOBILE_OK = 0x50029;
    public static final int MSG_BIND_MOBILE_KO = 0x50030;
    //获取WTK
    public static final int MSG_GET_WTK_OK = 0x50031;
    public static final int MSG_GET_WTK_KO = 0x50032;
    //更新用户状态
    public static final int MSG_EDIT_USER_STATUS_OK = 0x50033;
    public static final int MSG_EDIT_USER_STATUS_KO = 0x50034;
    //获取用户状态
    public static final int MSG_BATCH_GET_USER_STATUS_OK = 0x50035;
    public static final int MSG_BATCH_GET_USER_STATUS_KO = 0x50036;
	/*---------------------------------我的----------end-------------------------*/

    /*---------------------------------订单相关-----------------------------------*/
    public static final int ORDER = 0x60001;
    //创建订单
    public static final int MSG_CREATE_ORDER_OK = 0x60002;
    public static final int MSG_CREATE_ORDER_KO = 0x60003;
    //获取订单上下文
    public static final int MSG_GET_ORDER_CONTEXT_OK = 0x60004;
    public static final int MSG_GET_ORDER_CONTEXT_KO = 0x60005;
	/*---------------------------------订单相关----------end-------------------------*/
    /**
     * 酒店详情  获取房型列表
     **/
    public static final int MSG_GET_ROOM_TYPE_LIST_OK = 0x70001;
    public static final int MSG_GET_ROOM_TYPE_LIST_ERROR = 0x70002;
    /**
     * 酒店景区详情  获取图片列表
     **/
    public static final int MSG_GET_DETIAL_PICS_LIST_OK = 0x70005;
    public static final int MSG_GET_DETIAL_PICS_LIST_ERROR = 0x70006;
    /**
     * 酒店景区详情评价数量
     **/
    public static final int MSG_GET_COMMENT_NUM_OK = 0x70007;
    public static final int MSG_GET_COMMENT_NUM_ERROR = 0x70008;
    /**
     * 景区详情几条评价的添加
     **/
    public static final int MSG_GET_SCENIC_DETAIL_COMMENT_OK = 0x70009;
    public static final int MSG_GET_SCENIC_DETAIL_COMMENT_ERROR = 0x70010;

    /*---------------------------------伴手礼-----------------------------------*/
    //伴手礼分类
    public static final int MSG_GET_HANDCEREMONY_TYPES_OK = 0x30001;
    public static final int MSG_GET_HANDCEREMONY_TYPES_KO = 0x30002;
    //伴手礼列表
    public static final int MSG_GET_HANDCEREMONY_LIST_OK = 0x30003;
    public static final int MSG_GET_HANDCEREMONY_LIST_KO = 0x30004;
	/*---------------------------------伴手礼--------end---------------------------*/

    /*---------------------------------酒店、景区-----------------------------------*/
    public static final int MSG_GET_ENTITY_SHOP_DETAIL_OK = 0x70001;
    public static final int MSG_GET_ENTITY_SHOP_DETAIL_KO = 0x70002;

    /*---------------------------------酒店、景区----------end-------------------------*/

    /*--------------------------------- 概况、必买推荐(省内、国内)----------------------*/
	/*--------------------------------- 概况、必买推荐(省内、国内)----------end----------*/

    /*---------------------------------酒店、景区、餐饮、图片-----------------------------------*/
    public static final int MSG_GET_ENTITY_SHOP_PICTURE_OK = 0x70005;
    public static final int MSG_GET_ENTITY_SHOP_PICTURE_KO = 0x70006;
	/*---------------------------------酒店、景区、餐饮、图片---------------end--------------------*/

    /*---------------------------------支付相关-----------------------------------*/
    public static final int MSG_GET_PAY_WEIXIN_OK = 0x80001;
    public static final int MSG_GET_PAY_WEIXIN_KO = 0x80002;
    public static final int MSG_GET_PAY_WEIXINSTATUS_OK = 0x80003;
    public static final int MSG_GET_PAY_WEIXINSTATUS_KO = 0x80004;


    /*---------------------------------美食必买城市9club-----------------------------------*/
    public static final int MSG_BUYMUST_LIST_OK = 0x90001;
    public static final int MSG_BUYMUST_LIST_KO = 0x90002;
    public static final int MSG_NINECLUB_LIST_OK = 0x90003;
    public static final int MSG_NINECLUB_LIST_KO = 0x90004;
    public static final int MSG_EATGREAT_LIST_OK = 0x90005;
    public static final int MSG_EATGREAT_LIST_KO = 0x90006;
    public static final int MSG_EATGREAT_DETAIL_OK = 0x90007;
    public static final int MSG_EATGREAT_DETAIL_KO = 0x90008;
    public static final int MSG_DESTCITY_NEW_OK = 0x90009;
    public static final int MSG_DESTCITY_NEW_KO = 0x90010;
    public static final int MSG_DESTCITY_TOP_NEW_OK = 0x90011;
    public static final int MSG_DESTCITY_TOP_NEW_KO = 0x90012;


    /*---------------------------------评价相关-----------------------------------*/
    public static final int MSG_COMMENT_WRITE_INIT_OK = 0x90021;
    public static final int MSG_COMMENT_WRITE_INIT_KO = 0x90022;
    public static final int MSG_COMMENT_WRITE_SUBMIT_OK = 0x90023;
    public static final int MSG_COMMENT_WRITE_SUBMIT_KO = 0x90024;
    public static final int MSG_COMMENT_LIST_HEADER_OK = 0x90025;
    public static final int MSG_COMMENT_LIST_HEADER_KO = 0x90026;
    public static final int MSG_COMMENT_LIST_OK = 0x90027;
    public static final int MSG_COMMENT_LIST_KO = 0x90028;
    public static final int MSG_GET_HOTEL_FACILITY_OK = 0x90029;
    public static final int MSG_GET_HOTEL_FACILITY_KO = 0x90030;


    /*---------------------------------境外游相关-----------------------------------*/
    public static final int MSG_GET_ABROAD_DESTINATION_OK = 0x91001;
    public static final int MSG_GET_ABROAD_DESTINATION_KO = 0x91002;

    /*---------------------------------我的界面相关-----------------------------------*/
    public static final int MSG_GET_MINE_OK = 0x91003;
    public static final int MSG_GET_MINE_KO = 0x91004;

    public static final int MSG_GET_CONSULT_LIST_OK = 0x91005;
    public static final int MSG_GET_CONSULT_LIST_KO = 0x91006;
    /*---------------------------------新游客(联系人)相关-----------------------------------*/
    public static final int MSG_NEW_ADD_CODE_OK = 0x91011;
    public static final int MSG_NEW_ADD_CODE_KO = 0x91012;
    public static final int MSG_NEW_DELETE_CODE_OK = 0x91013;
    public static final int MSG_NEW_DELETE_CODE_KO = 0x91014;
    public static final int MSG_NEW_ADD_TOURIST_OK = 0x91015;
    public static final int MSG_NEW_ADD_TOURIST_KO = 0x91016;
    public static final int MSG_NEW_DELETE_TOURIST_OK = 0x91017;
    public static final int MSG_NEW_DELETE_TOURIST_KO = 0x91018;
    public static final int MSG_NEW_EDIT_TOURIST_OK = 0x91019;
    public static final int MSG_NEW_EDIT_TOURIST_KO = 0x91020;
    public static final int MSG_GET_NEW_TOURIST_LIST_OK = 0x91021;
    public static final int MSG_GET_NEW_TOURIST_LIST_KO = 0x91022;
    public static final int MSG_GET_NEW_TOURISTCODE_UPDATE_OK = 0x91025;
    public static final int MSG_GET_NEW_TOURISTCODE_UPDATE_KO = 0x91026;

    /*---------------------------------店铺相关-----------------------------------*/
    //店铺所有商品列表
    public static final int MSG_GET_SHOP_PRODUCTS_LIST_OK = 0x31001;
    public static final int MSG_GET_SHOP_PRODUCTS_LIST_KO = 0x31002;
    //店铺详情
    public static final int MSG_GET_SHOP_DETAIL_OK = 0x31003;
    public static final int MSG_GET_SHOP_DETAIL_KO = 0x31004;
    //店铺优惠券列表
    public static final int MSG_GET_SHOP_COUPON_LIST_OK = 0x31005;
    public static final int MSG_GET_SHOP_COUPON_LIST_KO = 0x31006;
    //领取优惠券
    public static final int MSG_REC_SHOP_COUPON_OK = 0x31007;
    public static final int MSG_REC_SHOP_COUPON_KO = 0x31008;

    public static final int MSG_QUERY_SHOP_DESC_OK = 0x31016;
    public static final int MSG_QUERY_SHOP_DESC_KO = 0x31017;

    public static final int MSG_QUERY_SHOP_CARD_OK = 0x31018;
    public static final int MSG_QUERY_SHOP_CARD_KO = 0x31019;
    //物流详情
    public static final int MSG_GET_LOGISTICS_LIST_OK = 0x31020;
    public static final int MSG_GET_LOGISTICS_LIST_KO = 0x31021;
    // 包裹详情

    public static final int MSG_GET_GOOD_RATE_LIST_OK = 0x31022;
    public static final int MSG_GET_GOOD_RATE_LIST_KO = 0x31023;


    // 评价订单详情

    public static final int MSG_GET_ORDER_PACKET_LIST_OK = 0x31022;
    public static final int MSG_GET_ORDER_PACKET_LIST_KO = 0x31023;


    /*--------------------------------积分商城-----------------------------------*/
    public static final int MSG_INTEGRALMALL_LIST_OK = 0x30001;
    public static final int MSG_INTEGRALMALL_LIST_KO = 0x30002;
    public static final int MSG_INTEGRALMALL_TOTALPOINT_OK = 0x30003;
    public static final int MSG_INTEGRALMALL_TOTALPOINT_KO = 0x30004;
    public static final int MSG_INTEGRALMALL_DAILYTASK_OK = 0x30005;
    public static final int MSG_INTEGRALMALL_DAILYTASK_KO = 0x30006;


    public static final int MSG_INTEGRALMALL_POINTDETAIL_OK = 0x30007;
    public static final int MSG_INTEGRALMALL_POINTDETAIL_KO = 0x30008;
    public static final int MSG_INTEGRALMALL_COMPLETETASK_OK = 0x30009;
    public static final int MSG_INTEGRALMALL_COMPLETETASK_KO = 0x300010;
    /*---------------------------------达人相关-----------------------------------*/
    //达人首页推荐达人列表
    public static final int MSG_GET_MASTER_HOME_PAGE_RECOMMAND_LIST_1_OK = 0x41011;
    public static final int MSG_GET_MASTER_HOME_PAGE_RECOMMAND_LIST_1_KO = 0x41012;

    public static final int MSG_GET_MASTER_HOME_PAGE_RECOMMAND_LIST_2_OK = 0x41013;
    public static final int MSG_GET_MASTER_HOME_PAGE_RECOMMAND_LIST_2_KO = 0x41014;

    public static final int MSG_GET_MASTER_HOME_PAGE_RECOMMAND_LIST_3_OK = 0x41015;
    public static final int MSG_GET_MASTER_HOME_PAGE_RECOMMAND_LIST_3_KO = 0x41016;
    //达人列表
    public static final int MSG_GET_MASTER_LIST_OK = 0x41005;
    public static final int MSG_GET_MASTER_LIST_KO = 0x41006;
    //达人详情
    public static final int MSG_GET_MASTER_DETAIL_OK = 0x41007;
    public static final int MSG_GET_MASTER_DETAIL_KO = 0x41008;
    //达人搜索
    public static final int MSG_MASTER_SEARCH_OK = 0x41009;
    public static final int MSG_MASTER_SEARCH_KO = 0x4100a;
    //达人搜索历史纪录
    public static final int MSG_MASTER_SEARCH_HISTORY_OK = 0x4100b;
    //达人热门搜索
    public static final int MSG_MASTER_HOT_SEARCH_OK = 0x4100c;
    public static final int MSG_MASTER_HOT_SEARCH_KO = 0x4100d;
    /**
     * 获取关注列表
     */
    public static final int MSG_GET_ATTENTION_LIST_OK = 0x42001;
    public static final int MSG_GET_ATTENTION_LIST_KO = 0x42002;
    /**
     * 关注
     */
    public static final int MSG_ATTENTION_OK = 0x42003;
    public static final int MSG_ATTENTION_KO = 0x42004;
    /**
     * 取消关注
     */
    public static final int MSG_CANCEL_ATTENTION_OK = 0x42005;
    public static final int MSG_CANCEL_ATTENTION_KO = 0x42006;

    /*---------------------------------酒店相关-----------------------------------*/
    public static final int MSG_GET_HOTEL_RECOMMAND_LIST_OK = 0x500001;
    public static final int MSG_GET_HOTEL_RECOMMAND_LIST_KO = 0x500002;

    public static final int MSG_HOTEL_FILTER_OK = 0x500003;
    public static final int MSG_HOTEL_FILTER_KO = 0x500004;

    public static final int MSG_HOTEL_HOME_BANNER_OK = 0x500005;
    public static final int MSG_HOTEL_HOME_BANNER_KO = 0x500006;

    public static final int MSG_GET_HOTEL_LIST_OK = 0x500007;
    public static final int MSG_GET_HOTEL_LIST_KO = 0x500008;

    public static final int MSG_GET_INN_RECOMMAND_LIST_OK = 0x500009;
    public static final int MSG_GET_INN_RECOMMAND_LIST_KO = 0x500010;
    /*---------------------------------景区相关-----------------------------------*/
    //本地景点
    public static final int MSG_SCENIC_LOCAL_HOME_RECOMMAND_LIST_OK = 0x600001;
    public static final int MSG_SCENIC_LOCAL_HOME_RECOMMAND_LIST_KO = 0x600002;
    //周边景点
    public static final int MSG_SCENIC_ARROUND_HOME_RECOMMAND_LIST_OK = 0x600003;
    public static final int MSG_SCENIC_ARROUND_HOME_RECOMMAND_LIST_KO = 0x600004;
    //城市代码
    public static final int MSG_SCENIC_CITY_LIST_OK = 0x600005;
    public static final int MSG_SCENIC_CITY_LIST_KO = 0x600006;
    //景点列表
    public static final int MSG_SCENIC_LIST_OK = 0x600007;
    public static final int MSG_SCENIC_LIST_KO = 0x600008;
    //初始化周边景点页面
    public static final int MSG_INIT_ARROUND_SCENIC_LIST_OK = 0x600009;
    public static final int MSG_INIT_ARROUND_SCENIC_LIST_KO = 0x600010;
    //订单列表和详情相关 start
    //获取订单列表成功
    public static final int GET_ORDER_LIST_SUCCESS = 1;
    //获取订单列表失败
    public static final int GET_ORDER_LIST_FAIL = 2;
    //取消订单成功
    public static final int CANCLE_ORDER_SUCCESS = 3;
    //取消订单失败
    public static final int CANCLE_ORDER_FAIL = 4;
    //获取订单成功
    public static final int GET_ORDER_DETAILS_SUCCESS = 5;
    //获取订单详情失败
    public static final int GET_ORDER_DETAILS_FAIL = 6;
    //显示loadingview
    public static final int SHOW_LOADING_VIEW = 7;
    //确认收货成功
    public static final int CONFIRM_RECIVER_SUCCESS = 8;
    //确认收货失败
    public static final int CONFIRM_RECIVER_FAIL = 9;
    //获取取消订单原因列表失败
    public static final int GET_CLOSED_REASON_FAIL = 10;
    //达人接单成功
    public static final int GET_ORDER_SUCCESS = 11;
    //达人接单失败
    public static final int GET_ORDER_FAIL = 12;
    //达人接单成功
    public static final int GET_MANAGE_DETAIL_SUCCESS = 13;
    //达人接单失败
    public static final int GET_MANAGE_DETAIL_FAIL = 14;

    //获取优惠券列表
    public static final int COUPON_INFO_LIST_SUCCESS = 51;
    public static final int COUPON_INFO_LIST_FAIL = 52;
    public static final int COUPON_INFO_SELLER_SUCCESS = 53;
    public static final int COUPON_INFO_SELLER_FAIL = 54;
    public static final int COUPON_INFO_GET_SUCCESS = 55;
    public static final int COUPON_INFO_GET_FAIL = 56;
    /*--------------------------------钱包相关-----------------------------------*/
  //  * 交易类型,充值:TOP_UP,提现:WITHDRAW,结算:SETTLEMENT,余额支付:BALANCE_PAY,资金转入:TRANSFER_IN,资金转出:TRANSFER_OUT

    public static final String WALLET_INCOME_EXPENCE_TOP_UP = "TOP_UP";

    public static final String WALLET_INCOME_EXPENCE_WITHDRAW = "WITHDRAW";

    public static final String WALLET_INCOME_EXPENCE_SETTLEMENT = "SETTLEMENT";

    public static final String WALLET_INCOME_EXPENCE_BALANCE_PAY = "BALANCE_PAY";

    public static final String WALLET_INCOME_EXPENCE_TRANSFER_IN = "TRANSFER_IN";

    public static final String WALLET_INCOME_EXPENCE_TRANSFER_OUT = "TRANSFER_OUT";



    // 订单状态，待付款：WAITING_PAY，已付款：WAITING_DELIVERY，已发货：SHIPPING，已完成：FINISH
    public static final String ORDER_STATUS_WAITING_PAY = "WAITING_PAY";//代付款１
    public static final String ORDER_STATUS_WAITING_DELIVERY = "WAITING_DELIVERY";
    public static final String ORDER_STATUS_SHIPPING = "SHIPPING";
    public static final String ORDER_STATUS_FINISH = "FINISH";//已完成,有效订单
    public static final String ORDER_STATUS_UNCOMMENT = "NOT_RATE";//已完成
    public static final String ORDER_STATUS_VALID = "WAITING_DELIVERY,SHIPPING,FINISH";//有效订单,逗号隔开支持多个查询


    public static final String ORDER_STATUS_ALL = "ALL";
    public static final String ORDER_STATUS_CANCEL = "CANCEL";
    public static final String ORDER_STATUS_REFUNDED = "REFUNDED";
    public static final String ORDER_STATUS_CLOSED = "CLOSED";
    public static final String ORDER_STATUS_RATED = "RATED";//已评价５
    public static final String ORDER_STATUS_CONFIRMED_CLOSE = "CONFIRMED_CLOSE";
    public static final String ORDER_STATUS_CONFIRMED = "CONFIRMED";
    public static final String ORDER_STATUS_NOT_RATE = "NOT_RATE";


    public static final String ORDER_STATUS_CONSULT_IN_QUEUE = "CONSULT_IN_QUEUE";//排队中 带确认２
    public static final String ORDER_STATUS_CONSULT_IN_CHAT = "CONSULT_IN_CHAT";//咨询中 已确认３
    // 订单类型
    public static final String ORDER_TYPE_EXPERT_ACTIVITY = "PROCESS";
    public static final String ORDER_TYPE_NORMAL = "NORMAL";
    public static final String ORDER_TYPE_HOTEL = "HOTEL";
    public static final String ORDER_TYPE_HOTEL_OFFLINE = "HOTEL_OFFLINE";
    public static final String ORDER_TYPE_CITY_ACTIVITY = "CITY_ACTIVITY";
    public static final String ORDER_TYPE_SPOTS = "SPOTS";
    public static final String ORDER_TYPE_ACTIVITY = "ACTIVITY";
    public static final String ORDER_TYPE_TOUR_LINE = "TOUR_LINE";
    public static final String ORDER_TYPE_FREE_LINE = "FREE_LINE";
    public static final String ORDER_TYPE_TOUR_LINE_ABOARD = "TOUR_LINE_ABOARD";
    public static final String ORDER_TYPE_FREE_LINE_ABOARD = "FREE_LINE_ABOARD";
    public static final String ORDER_TYPE_LINE = "LINE";
    public static final String ORDER_TYPE_TRAVEL = "TRAVEL";
    public static final String ORDER_TYPE_SCENIC = "SCENIC";
    public static final String ORDER_TYPE_LOCAL = "LOCAL";
    public static final String ORDER_TYPE_POINT = "POINT";
    public static final String ORDER_TYPE_POINT_MALL = "POINT_MALL";

    //
    public static final String AUTH_SUCCESS = "SUCCESS";
    public static final String AUTH_FAIL = "FAIL";

    //订单列表和详情相关 end
    public static final String OFFLINE_PAY = "OFFLINE_PAY";
    //qq注册appid和key
    public static final String QQ_APPID = "1104742073";
    public static final String QQ_APPKEY = "I7LYsdz2cVZvizuh";

    public static String WX_APPID = "";
    public static String WX_APPSECRET = "";

    //新浪注册appid和key
//    public static final String Sina_APPID = "4188845889";
//    public static final String Sina_APPSECRET = "75aafcc1659685c2178ae769b046aa3c";
    public static final String Sina_APPID = "4188845889";
    public static final String Sina_APPSECRET = "75aafcc1659685c2178ae769b046aa3c";

    public static final String PAY_BIZORDERID = "bizOrderId";//订单号
    public static final String PAY_PRICE = "price";//订单价格

    //百度地图建淼测试key
    public static final String TEST_BAIDU_MAP_MCODE = "D5:43:A1:50:13:AF:E5:29:92:07:17:62:D1:04:81:AE:1A:08:2B:38;com.quanyan.yhy";
    public static final String TEST_BAIDU_MAP_AK = "3H3Tx79PLwN1XUBnRW9kkjFhQGGskTvO";
    //百度地图正式key
    public static final String BAIDU_MAP_MCODE = "ED:EF:8E:0C:43:38:69:99:E5:06:6E:5F:75:11:72:EE:00:1C:A7:96;com.quanyan.yhy";
    public static final String BAIDU_MAP_AK = "rY1AYchTfkv1S7qeUWBIvFrG2cp4Kfwe";
    //活动状态  已结束
    public static final String ACTIVITY_STATE_EXPIRED = "EXPIRED";
    //已下架
    public static final String ACTIVITY_STATE_INVALID = "INVALID";
    //出发地选择请求码
    public final static int REQ_CODE_SELECT_CITY = 0x1001;

    public static final String NONE = "NONE";
    public static final String FOLLOW = "FOLLOW";
    public static final String BIFOLLOW = "BIFOLLOW";

    //----------话题详情---------//
    public static final int TOPIC_DETAIL_OK = 0x92000;
    public static final int TOPIC_DETAIL_ERROR = 0x92001;
    public static final int TOPIC_DETAIL_LIST_OK = 0x92002;
    public static final int TOPIC_DETAIL_LIST_ERROR = 0x92003;

    public static final int POINT_TOTAL_OK = 0x92004;
    public static final int POINT_TOTAL_ERROR = 0x92005;

    public static final String FOLLOW_SUCCESS = "FOLLOW_SUCCESS";

    public static final int SELLER_AND_CONSULT_STATE_OK = 0x10000001;
    public static final int SELLER_AND_CONSULT_STATE_KO = 0x10000002;

    public static final int CREATE_PROCESS_ORDER_OK = 0x10000003;
    public static final int CREATE_PROCESS_ORDER_KO = 0x10000004;

    public static final int ACCEPT_PROCESS_ORDER_OK = 0x10000005;

    public static final int ACCEPT_PROCESS_ORDER_KO = 0x10000006;

    public static final int CANCEL_CONSULT_ORDER_OK = 0x10000007;

    public static final int CANCEL_CONSULT_ORDER_KO = 0x10000008;
    public static final int FINISH_CONSULT_ORDER_OK = 0x10000009;

    public static final int FINISH_CONSULT_ORDER_KO = 0x10000010;

    public static final int GET_FAST_CONSULT_OK = 0x10000010;
    public static final int GET_FAST_CONSULT_KO = 0x10000011;
    public static final int GET_CONSULT_BOOTH_OK = 0x10000012;
    public static final int GET_CONSULT_BOOTH_KO = 0x10000013;

    public static final int SELLER_AND_CONSULT_STATE_WHEN_COMMINT_OK = 0x10000014;
    public static final int SELLER_AND_CONSULT_STATE_WHEN_COMMINT_KO = 0x10000015;
    public static final int GET_ITEMS_OK = 0x10000016;
    public static final int GET_ITEMS_KO = 0x10000017;


    /*---------------------------------发布服务-----------------------------------*/
    public static final int MSG_RELEASE_SERVICE_OK = 0x11000001;
    public static final int MSG_RELEASE_SERVICE_KO = 0x11000002;

    public static final int MSG_SERVICE_UPDATE_SUCCESS = 101;
    public static final int MSG_SERVICE_UPDATE_FAIL = 102;
    public static final int MSG_SERVICE_MANAGE_SUCCESS = 103;
    public static final int MSG_SERVICE_MANAGE_FAIL = 104;
    public static final int MSG_RELEASE_SERVICE_WHITE_LIST_OK = 0x11000003;
    public static final int MSG_RELEASE_SERVICE_WHITE_LIST_KO = 0x11000004;

    public static final int MSG_SELECT_SERVICE_DETAIL_OK = 0x11000005;
    public static final int MSG_SELECT_SERVICE_DETAIL_KO = 0x11000006;

    public static final int MSG_GETCONSULT_ITEMPROPERTIES_OK = 0x11000021;
    public static final int MSG_GETCONSULT_ITEMPROPERTIES_KO = 0x11000022;

    /*---------------------------------达人主页图文介绍-----------------------------------*/
    public static final int MSG_MINE_HOME_PAGE_ADD_OK = 0x11000007;
    public static final int MSG_MINE_HOME_PAGE_ADD_KO = 0x11000008;

    public static final int MSG_MINE_HOME_PAGE_EDIT_OK = 0x11000009;
    public static final int MSG_MINE_HOME_PAGE_EDIT_KO = 0x11000010;

    public static final int MSG_MINE_HOME_PAGE_SELECT_OK = 0x11000011;
    public static final int MSG_MINE_HOME_PAGE_SELECT_KO = 0x11000012;

    /*--------------------------------支付-----------------------------------------------*/
    public static final int PAY_GETELEACCOUNTINFO_SUCCESS = 0x1200001;
    public static final int PAY_GETELEACCOUNTINFO_ERROR = 0x1200002;

    public static final int PAY_PAGEQUERYUBILL_SUCCESS = 0x1200003;
    public static final int PAY_PAGEQUERYUBILL_ERROR = 0x1200004;

    public static final int PAY_BankCardByCardNo_SUCCESS = 0x1200005;
    public static final int PAY_BankCardByCardNo_ERROR = 0x1200006;

    public static final int PAY_SendVerifyCode_SUCCESS = 0x1200007;
    public static final int PAY_SendVerifyCode_ERROR = 0x1200008;
    public static final int PAY_SendVerifyCode_TYPE_SUCCESS = 0x1200009;

    public static final int PAY_CheckVerifyCode_SUCCESS = 0x1200010;
    public static final int PAY_CheckVerifyCode_ERROR = 0x1200011;

    public static  final int PAY_SetupPayPwd_SUCCESS = 0x1200012;
    public static  final int PAY_SetupPayPwd_ERROR = 0x1200013;

    public static  final int PAY_GetCebCloudPayInfo_SUCCESS = 0x1200014;
    public static  final int PAY_GetCebCloudPayInfo_ERROR = 0x1200015;

    public static  final int PAY_GetPayStatusInfo_SUCCESS = 0x1200016;
    public static  final int PAY_GetPayStatusInfo_ERROR = 0x1200017;

    public static  final int PAY_PageQueryUserBindBankCard_SUCCESS = 0x1200018;
    public static  final int PAY_PageQueryUserBindBankCard_ERROR = 0x1200019;

    public static  final int PAY_Recharge_SUCCESS = 0x1200018;
    public static  final int PAY_Recharge_ERROR = 0x1200019;

    public static  final int PAY_UpdatePayPwd_SUCCESS = 0x1200020;
    public static  final int PAY_UpdatePayPwd_ERROR = 0x1200021;

    public static  final int PAY_VerifyIdentity_SUCCESS = 0x1200022;
    public static  final int PAY_VerifyIdentity_ERROR = 0x1200023;

    public static  final int PAY_VerifyPayPwd_SUCCESS = 0x1200024;
    public static  final int PAY_VerifyPayPwd_ERROR = 0x1200025;

    public static  final int PAY_Withdraw_SUCCESS = 0x1200026;
    public static  final int PAY_Withdraw_ERROR = 0x1200027;

    public static  final int PAY_QueryTransStatus_SUCCESS = 0x1200028;
    public static  final int PAY_QueryTransStatus_ERROR = 0x1200029;


    public static  final int PAY_GetBankNameList_SUCCESS = 0x1200030;
    public static  final int PAY_GetBankNameList_ERROR = 0x1200031;
    public static  final int PAY_ElePursePay_SUCCESS = 0x1200032;
    public static  final int PAY_ElePursePay_ERROR = 0x1200033;
    public static  final int PAY_VERIFY_IDCARDPHOTO_SUCCESS = 0x1200034;
    public static  final int PAY_VERIFY_IDCARDPHOTO_ERROR = 0x1200035;
    public static  final int PAY_SUBMIT_IDCARDPHOTO_SUCCESS = 0x1200036;
    public static  final int PAY_SUBMIT_IDCARDPHOTO_ERROR = 0x1200037;

    /*---------------------------------导览相关-----------------------------------*/
    public static final int MSG_GUODELIST_SEARCH_OK = 0x130001;
    public static final int MSG_GUODELIST_SEARCH_KO = 0x130002;


    public static final int MSG_GUODELIST_DETAIL_OK = 0x130003;
    public static final int MSG_GUODELIST_DETAIL_KO = 0x130004;
    public static final int MSG_GUODELIST_HOME_OK = 0x130005;
    public static final int MSG_GUODELIST_HOME_KO = 0x130006;

    //2.4新搜索
    public static final int MSG_NEW_LOADING_SEARCH_OK = 0x140001;
    public static final int MSG_NEW_LOADING_SEARCH_KO = 0x140002;

    /*---------------------------购物车----------------------------*/
    public static final int SPCART_SaveToCart_OK = 0x1200034;
    public static final int SPCART_SaveToCart_ERROR = 0x1200035;

    public static final int SPCART_SelectCartAmount_OK= 0x1200036;
    public static final int SPCART_SelectCartAmount_ERROR = 0x1200037;

    public static final int SPCART_DeleteCart_OK = 0x1200038;
    public static final int SPCART_DeleteCart_ERROR = 0x1200039;

    public static final int SPCART_GetCartInfoList_OK = 0x1200040;
    public static final int SPCART_GetCartInfoList_ERROR = 0x1200041;

    public static final int SPCART_SelectCart_OK= 0x1200042;
    public static final int SPCART_SelectCart_ERROR = 0x1200043;

    public static final int SPCART_UpdateCartAmount_OK = 0x1200044;
    public static final int SPCART_UpdateCartAmount_ERROR = 0x1200045;

    public static final int SPCART_CreateOrderContextForPointMall_OK = 0x1200046;
    public static final int SPCART_CreateOrderContextForPointMall_ERROR = 0x1200047;

    public static final int SPCART_CreateBatchOrder_OK = 0x1200048;
    public static final int SPCART_CreateBatchOrder_ERROR = 0x1200049;

    public static final int SPCART_GetAliBatchPayInfo_OK = 0x1200050;
    public static final int SPCART_GetAliBatchPayInfo_ERROR = 0x1200051;

    public static final int SPCART_ElePurseBatchPay_OK = 0x1200052;
    public static final int SPCART_ElePurseBatchPay_ERROR = 0x1200053;

    public static final int SPCART_GetCebCloudBatchPayInfo_OK = 0x1200054;
    public static final int SPCART_GetCebCloudBatchPayInfo_ERROR = 0x1200055;



    public static final int POINT_ORDER_DETAIL_LIKE_RECOMMEND_OK = 0x150001;
    public static final int POINT_ORDER_DETAIL_LIKE_RECOMMEND_KO = 0x150002;

    static {
        // 测试环境
        if (!AppDebug.IS_ONLINE) {
            WX_APPID = "wxa92a7851cbaacce9";
            WX_APPSECRET = "f5d164f8586a1fffd1c9ca9398270133";
        } else {
        // 正式环境
            WX_APPID = "wxee78efb4636dabb5";
            WX_APPSECRET = "2e863fbbfbf47402e9b9008699dabeff";
        }
    }
}
