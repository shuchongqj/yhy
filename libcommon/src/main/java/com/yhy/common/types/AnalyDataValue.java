package com.yhy.common.types;

/**
 * Created with Android Studio.
 * Title:AnalyDataValue
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:和平必胜、正义必胜、人民必胜
 * Author:炎黄子孙
 * Date:15/12/14
 * Time:下午2:48
 * Version 1.0
 */
public class AnalyDataValue {
    /**
     * 登录相关　start
     */
    //登录
    public static final String USER_LOGIN = "UserLogin";
    /** 登录相关　end */

    /**
     * app启动
     */
    public static final String APP_START = "App_start";
    /**
     * app退出
     */
    public static final String APP_OUT = "App_out";
    /**
     * 我的相关 start
     */
    //消息点击
    //public static final String TAB_MINE_MSG_CLICK = "TAB_MINE_MSG_CLICK";
    /** 我的相关 end */
    /**
     * 会员页面 start
     */

    /**************************************我的个人资料开始******************************************/

    /**************************************我的个人资料结束*****************************************/
    //确认支付
    public static final String CONFIRM_PAYMENT = "Confirm_Payment";
    //取消支付
    public static final String CANCEL_ORDER = "Cancel_order";
    //目的地选择 id目的地Code name目的地名称
    public static final String DESTINATION_CHOICE = "Destination_Choice";
    //定位点击记录
    public static final String MASTER_LOCATION = "Master_Location";
    //头条
    public static final String HOME_HEADLINES = "Home_Headlines";
    //直播点击
    public static final String TAB_DIS_LIVE_CLICK = "TAB_DIS_LIVE_CLICK";
    //游记的点击
    public static final String TAB_DIS_TRAVEL_NOTES_CLICK = "TAB_DIS_TRAVEL_NOTES_CLICK";
    //推荐行程项点击
    public static final String TAB_TRIP_RECOMM_ITEM_CLICK = "TAB_TRIP_RECOMM_ITEM_CLICK";
    //我的行程项点击
    public static final String TAB_TRIP_MINE_ITEM_CLICK = "TAB_TRIP_MINE_ITEM_CLICK";
    //添加位置点击
    public static final String PUB_LIVE_ADD_LOCATION = "PUB_LIVE_ADD_LOCATION";
    //添加话题标签点击
    public static final String PUB_LIVE_ADD_TAG = "PUB_LIVE_ADD_TAG";
    //添加图片点击
    public static final String PUB_LIVE_ADD_PICTURES = "PUB_LIVE_ADD_PICTURES";

    /***************************线路购买**********************************/
    //线路选择联系人
    public static final String LINE_SUBMIT_SEL_CONTACTS = "LINE_SUBMIT_SEL_CONTACTS";
    //线路选择游客
    public static final String LINE_SUBMIT_SEL_VISITORS = "LINE_SUBMIT_SEL_VISITORS";
    /**
     * －－－－－－－－－发现－－－－－－－－－－－
     **/
    //发现-直播点赞
    public static final String Find_DirectLike = "Find_DirectLike";
    //发现-直播取消点赞
    public static final String Find_DirectCancel = "Find_DirectCancel";
    //发现-直播发布
    public static final String Find_DirectRelease = "Find_DirectRelease";
    //发现-直播查看
    public static final String Find_DirectSee = "Find_DirectSee";
    //发现-直播评论
    public static final String Find_DirectComment = "Find_DirectComment";
    //发现-游记查看
    public static final String Find_TravelsSee = "Find_TravelsSee";
    //发现-游记点赞
    public static final String Find_TravelsLike = "Find_TravelsLike";
    public static final String Find_TravelsCancel = "Find_TravelsCancel";
    /**－－－－－－－－－发现－－－－－end－－－－－－**/

    /**
     * －－－－－－－－－行程－－－－－－－－－－－
     **/
    //行程-推荐行程访问记录
    public static final String Trip_Recommend = "Trip_Recommend";
    //行程-购买行程访问记录
    public static final String Trip_Purchase = "Trip_Purchase";
    /**－－－－－－－－－行程－－－－－end－－－－－－**/


    /**
     * key start
     */
    public static final String KEY_SHOPID = "shopid";//店铺id
    public static final String KEY_MID = "mid";//达人id
    public static final String KEY_LIVE_ID = "liveId";//动态ID
    public static final String KEY_MNAME = "mname";//达人昵称
    public static final String KEY_PTYPE = "ptype";//商品类型
    public static final String KEY_PID = "pid";//商品id
    public static final String KEY_PNAME = "pname";//商品名称
    public static final String KEY_TYPE = "type";
    public static final String KEY_WORD = "keyword";
    public static final String KEY_ID = "id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_NAME = "name";
    public static final String KEY_UID = "uid";
    public static final String KEY_PAY_WAY = "way";
    public static final String KEY_ORDER_ID = "orderId";
    public static final String KEY_REASON = "reason";
    public static final String KEY_SUBNAME = "subname";
    public static final String KEY_INDEX = "index";
    public static final String KEY_CITY_CODE = "cityCode";
    public static final String KEY_CITY_NAME = "cityName";
    public static final String KEY_SHARE_TYPE = "shareType";
    public static final String KEY_TAGID = "tagId";
    public static final String KEY_TAGNAME = "tagName";
    public static final String KEY_VALUE = "value";
    public static final String KEY_COMMENTID = "commentId";
    public static final String KEY_FILTER_ID = "filterId";
    public static final String KEY_FILTER_NAME = "filterName";
    public static final String KEY_CID = "cid";
    public static final String KEY_CNAME = "cname";
    public static final String KEY_SELLER_NAME = "sellername";
    public static final String KEY_FULL_PRICE = "fullprice";
    public static final String KEY_REDUCE_PRICE = "reduceprice";
    public static final String KEY_BID = "bid";
    public static final String KEY_FROM = "from";
    public static final String KEY_TAGS = "tags";
    public static final String KEY_CONTENT = "content";
    public static final String KEY_SID = "sid";
    public static final String KEY_GNAME = "gname";
    public static final String KEY_WNAME = "wname";
    public static final String KEY_GID = "gid";
    public static final String KEY_WID = "wid";

    public static final String KEY_DESTCITY = "destCity";//目的地
    public static final String KEY_STARTDATE = "startDate";//入店时间
    public static final String KEY_ENDDATE = "endDate";//离店时间
    public static final String KEY_KEYWORD = "keyWord";//关键字
    public static final String KEY_STAR = "star";//星级
    public static final String KEY_PRICE = "price";//价格

    public static final String WEIXIN = "WEIXIN";
    public static final String WEIXIN_CICRLE = "WEIXIN_CICRLE";
    public static final String WEIBO = "WEIBO";
    public static final String QQ = "QQ";
    /**
     * key end
     */
    public static final String VALUE_TYPE_CLUB_ACT = "俱乐部活动";
    public static final String VALUE_TYPE_HOME = "首页";
    public static final String VALUE_TYPE_MINE = "我的";
    public static final String VALUE_PRODUCT_LINE = "产品线路";

    public static final String VALUE_TYPE_LINE = "线路";
    public static final String VALUE_TYPE_PHYSICAL_GOODS = "实物商品";
    public static final String VALUE_TYPE_SPOT = "景区";
    public static final String VALUE_TYPE_HOTEL = "酒店";
    public static final String VALUE_TYPE_INN = "客栈";
    public static final String VALUE_TYPE_SCENIC_LOCAL = "1";//本地景点
    public static final String VALUE_TYPE_SCENIC_AROUND = "2";//周边景点
    public static final String VALUE_TYPE_FLIGHT_HOTEL = "机+酒";
    public static final String VALUE_TYPE_SPOT_HOTEL = "景+酒";

    public static final String MASTER_CHAT_TYPE_TEL = "电话";
    public static final String MASTER_CHAT_TYPE_CHAT = "私聊";

    /**
     * 打点获取商品类型
     *
     * @param itemType {@link com.quanyan.yhy.net.model.trip.ItemVO}
     * @return 打点数据
     */
    public static String getValueType(String itemType) {
        if ("LINE".equals(itemType)) {
            return VALUE_TYPE_LINE;
        } else if ("HOTEL".equals(itemType)) {
            return VALUE_TYPE_HOTEL;
        } else if ("SPOTS".equals(itemType)) {
            return VALUE_TYPE_SPOT;
        } else if ("FLIGHT_HOTEL".equals(itemType)) {
            return VALUE_TYPE_FLIGHT_HOTEL;
        } else if ("SPOTS_HOTEL".equals(itemType)) {
            return VALUE_TYPE_SPOT_HOTEL;
        } else if ("ACTIVITY".equals(itemType)) {
            return VALUE_TYPE_CLUB_ACT;
        }
        return null;
    }

    /**
     * －－－－－－－－－主页－－－－－－－－－－－
     **/
    public static final String CHANGE_MENU = "Change_menu";
    /**－－－－－－－－－主页－－－－－end－－－－－－**/

    /**
     * －－－－－－－－－首页－－－－－－－－－－－
     **/
    //首页分类位访问记录
    public static final String HOME_CLASSIFICATION = "Home_Classification";
    //首页方阵位访问记录
    public static final String HOME_MATRIX = "Home_Matrix";
    //首页三个方块位
    public static final String Home_Three_Matrix = "Home_Three_Matrix";
    //首页精选推荐位访问记录
    public static final String HOME_SELECTION = "Home_Selection";
    //首页金牌导游位访问记
    public static final String HOME_GUIDE = "Home_Guide";


    /**－－－－－－－－－首页－－－－－end－－－－－－**/
    /**
     * ----------------搜索-----------------
     **/
    public static final String TC_ID_SEARCH = "Search_Click";
    public static final String SEARCH_CLICK_HOME = "HOME";//首页
    public static final String SEARCH_CLICK_MASTER = "MASTER";//达人
    public static final String SEARCH_CLICK_PACKAGE_TOUR = "PACKAGE_TOUR";//跟团游
    public static final String SEARCH_CLICK_FREE_WALK = "FREE_WALK";//自由行
    public static final String SEARCH_CLICK_CITY_ACTIVITY = "CITY_ACTIVITY";//同城活动
    public static final String SEARCH_CLICK_CITY_ARROUND_FUN = "ARROUND_FUN";//周边活动
    public static final String SEARCH_CLICK_MASTER_PRODUCT = "MASTER_PRODUCT";//达人商品
    public static final String SEARCH_CLICK_MUST_BUY = "MUST_BUY";//必买推荐
    /**
     * ----------------搜索------------end-----
     **/
    public static final String TC_ID_LOCATION_RECORD_HOME = "Home_Location";//首页定位记录
    public static final String TC_ID_BANNER_CLICK = "Banner_Click";
    public static final String BANNER_CLICK_HOME = "HOME";//资源位点击首页
    public static final String BANNER_CLICK_PACKAGE_TOUR = "PACKAGE_TOUR";//资源为点击跟团游
    public static final String BANNER_CLICK_FREE_WALK = "FREE_WALK";//资源位点击自由行
    public static final String BANNER_CLICK_CITY_ACTIVITY = "CITY_ACTIVITY";//资源为点击同城活动
    public static final String BANNER_CLICK_ARROUND_FUN = "ARROUND_FUN";//资源位点击周边
    public static final String BANNER_CLICK_HOTEL = "HOTEL";//资源为点击酒店
    public static final String BANNER_CLICK_SCENIC = "SCENIC";//资源位点击景区
    public static final String BANNER_CLICK_MASTER = "MASTER_HOME";//资源位点击达人
    public static final String BANNER_CLICK_MASTER_CONSULT = "MASTER_CONSULT";//资源位咨询达人首页banner
    public static final String BANNER_CLICK_GUIDE_HOME = "GUIDE_HOME";//资源位导览首页banner
    public static final String BANNER_CLICK_LIVE_HOME = "LIVE_HOME";//直播banner位

    public static final String TC_ID_GROUP_HOT_CITY = "Group_HotCity";//跟团游热门城市
    public static final String TC_ID_PRODUCT_SEE_DETAIL = "See_Detail";
    public static final String PRODUCT_DETAIL_PACK_TOUR = "PACKAGE_TOUR";//商品详情 跟团游
    public static final String PRODUCT_DETAIL_FREE_WALK = "FREE_WALK";//商品详情 自由行
    public static final String PRODUCT_DETAIL_CITY_ACTIVITY = "CITY_ACTIVITY";//商品详情 同城活动
    public static final String PRODUCT_DETAIL_MASTER_PRODUCT = "MASTER_PRODUCT";//商品详情 达人详情
    public static final String PRODUCT_DETAIL_MUST_BUY = "MUST_BUY";//商品详情 必买推荐
    public static final String PRODUCT_DETAIL_INTEGRAL = "INTEGRAL";//商品详情 积分商品

    public static final String TC_ID_PERIPHERALPLAY_THEME = "PeripheralPlay_Theme";//周边玩乐主题
    public static final String TC_ID_PERIPHERALPLAY_PERIPHERY = "PeripheralPlay_Periphery";//周边游-当季周边好去处
    public static final String TC_ID_DELICIOUS_FOOR_DETAIL = "DeliciousFood_SeeDetails";//美食-美食商家列表详情查看

    public static final String TC_ID_BUY = "Sign_Up";//立即购买
    public static final String TC_ID_SUBMIT_ORDER = "Submit_Order";//提交订单
    public static final String TC_ID_SHOPP_CALL = "Shop_Call";//电话咨询
    public static final String TC_ID_SHOPP_IM = "Shop_Im";//客服咨询
    /**
     * －－－－－－－－－达人－－－－－start－－－－－－
     **/
    //达人-热门城市点击
    public static final String MASTER_RECOMMEND = "Master_Recommend";
    //达人精彩主题点击
    public static final String MASTER_WONDERFULTHEME = "Master_WonderfulTheme";
    //达人榜点击全部按钮
    public static final String MASTER_CLICKALL = "Master_ClickAll";
    //达人-达人店铺详情查看
    public static final String MASTER_SEEDETAILS = "Master_SeeDetails";
    //达人店铺-直播查看
    public static final String MASTERSHOP_SEELIVEDETAIL = "MasterShop_SeeLiveDetail";
    //达人店铺-商品查看
    public static final String MASTERSHOP_SEEGOODSDETAIL = "MasterShop_SeeGoodsDetail";
    //达人店铺-咨询沟通
    public static final String MASTERSHOP_COMMUNICATE = "MasterShop_Communicate";

    /**－－－－－－－－－达人－－－－－end－－－－－－**/


    /**－－－－－－－－－酒店－－－－－start－－－－－－**/

    //酒店首页_精选列表
    public static final String HOTEL_HOME_LIST_CLICK = "Hotel_Home_List_Click";
    //酒店首页_搜索按钮
    public static final String HOTEL_HOME_SEARCH = "Hotel_Home_Search";
    //酒店首页TAB切换
    public static final String HOTEL_HOME_TAB_CHANGE = "Hotel_Home_Tab_Change";
    //酒店列表筛选条件点击
    public static final String HOTEL_LIST_FILTER = "Hotel_List_Filter";
    //酒店列表点击
    public static final String HOTEL_LIST_CLICK = "Hotel_List_Click";
    //酒店详情图片点击
    public static final String HOTEL_DETAIL_VIEW_PICTURES = "Hotel_Detail_View_Pictures";
    //酒店详情评论点击
    public static final String HOTEL_DETAIL_VIEW_COMMENTS = "Hotel_Detail_View_Comments";
    //酒店详情地址点击
    public static final String HOTEL_DETAIL_VIEW_ADDRESS = "Hotel_Detail_View_Address";
    //酒店详情设施点击
    public static final String HOTEL_DETAIL_VIEW_FACILITY = "Hotel_Detail_View_Facility";
    //酒店详情房间预订
    public static final String HOTEL_DETAIL_ORDER_ROOM = "Hotel_Detail_Order_Room";

    /**－－－－－－－－－酒店－－－－－end－－－－－－**/


    /**－－－－－－－－－景区－－－－－start－－－－－－**/

    //景区首页主题点击
    public static final String SCENIC_HOME_THEME_CLICK = "Scenic_Home_Theme_Click";
    //景区首页运营位点击
    public static final String SCENIC_HOME_RESOURCE_CLICK = "Scenic_Home_Resource_Click";
    //景区首页列表点击
    public static final String SCENIC_HOME_LIST_CLICK = "Scenic_Home_List_Click";
    //景区列表筛选条件点击
    public static final String SCENIC_LIST_FILTER = "Scenic_List_Filter";
    //景区列表点击
    public static final String SCENIC_LIST_CLICK = "Scenic_List_Click";
    //景区详情图片点击
    public static final String SCENIC_DETAIL_VIEW_PICTURES = "Scenic_Detail_View_Pictures";
    //景区详情地址点击
    public static final String SCENIC_DETAIL_VIEW_ADDRESS = "Scenic_Detail_View_Address";
    //景区详情介绍点击
    public static final String SCENIC_DETAIL_INTRODUCTION = "Scenic_Detail_Introduction";
    //景区详情门票预订
    public static final String SCENIC_DETAIL_ORDER = "Scenic_Detail_Order";
    //周边景区列表城市点击
    public static final String SCENIC_ARROUND_LIST_CITY_CLICK = "Scenic_Arround_List_City_Click";

    /**－－－－－－－－－景区－－－－－end－－－－－－**/

    //发布评价
    public static final String COMMENT_PUBLISH = "Comment_Publish";
    //评价列表筛选点击
    public static final String COMMENTS_FILTER = "Comments_Filter";
    //评价列表图片点击
    public static final String COMMENTS_VIEW_PICTURES = "Comments_View_Pictures";
    //热搜关键词点击
    public static final String HOT_SEARCH_WORD_CLICK = "Hot_Search_Word_Click";
    //分享
    public static final String DETAIL_SHARE = "Detail_Share";




    /**－－－－－－－－－1.3 打点需求－－－－－start－－－－－－**/


    /**－－－－－－－－－达人圈话题－－－－－start－－－－－－**/
    //界面头部切换
    public static final String DISCOVERY_TAB_CHANGE = "Discovery_Tab_Change";
    //照相机按钮
    public static final String DISCOVERY_RELEASE = "Discovery_Release";
    //达人圈头像点击
    public static final String DISCOVERY_USER_HEADER = "Discovery_User_Header";
    //达人圈关注按钮
    public static final String DISCOVERY_FOLLOW_CLICK = "Discovery_Follow_Click";
    //达人圈图像视频点击
    public static final String DISCOVERY_PIC_AND_VIDEO_CLICK = "Discovery_Pic_And_Video_Click";
    //达人圈其他按钮点击
    public static final String DISCOVERY_OTHER_CLICK = "Discovery_Other_Click";
    //达人圈详情删除按钮
    public static final String DISCOVERY_DELETE_CLICK = "Discovery_Delete_Click";
    //话题列表点击
    public static final String TOPIC_LIST_CLICK = "Topic_List_Click";
    //话题详情参与话题
    public static final String TOPIC_PARTAKE = "Topic_Partake";

    /**－－－－－－－－－达人圈话题－－－－－end－－－－－－**/

    //关注粉丝列表点击
    public static final String ATTETION_AND_FANS_LIST_CLICK = "Attetion_And_Fans_List_Click";
    //关注粉丝按钮点击
    public static final String  ATTENTION_AND_FANS_BUTTON_CLICK = "Attention_And_Fans_Button_Click";


    /**－－－－－－－－－达人主页－－－－－start－－－－－－**/
    //粉丝点击
    public static final String MASTER_HOMEPAGE_FANS_CLICK = "Master_HomePage_Fans_Click";
    //关注点击
    public static final String  MASTER_HOMEPAGE_FOLLOW_CLICK = "Master_HomePage_Follow_Click";
    //关注按钮点击
    public static final String MASTER_HOMEPAGE_ATTETION_BUTTON_CLICK = "Master_HomePage_Attetion_Button_Click";

    /**－－－－－－－－－达人主页－－－－－end－－－－－－**/



    //添加图片按钮点击
    public static final String  DYNAMIC_ADD_PIC_BUTTON = "Dynamic_Add_Pic_Button";
    //添加位置点击
    public static final String DYNAMIC_LOCATION_CLICK = "Dynamic_Location_Click";
    //#点击
    public static final String  DYNAMIC_TOPIC_CLICK = "Dynamic_Topic_Click";
    //我的界面优惠券点击
    public static final String MINE_COUPON_CLICK = "Mine_Coupon_Click";
    //优惠券列表
    public static final String  MINE_COUPON_LIST_ITEM = "Mine_Coupon_List_Item";


    //我的  签到
    public static final String  MINE_SIGN_CLICK = "Mine_Sign_Click";
    //我的  积分
    public static final String MINE_INTEGRAL_CLICK = "Mine_Integral_Click";
    //我的  零钱
    public static final String MINE_INCHARGE_CLICK = "Mine_Incharge_Click";
    //我的  积分商城
    public static final String  MINE_INTEGRAL_SHOP_CLICK = "Mine_Integral_Shop_Click";
    //积分任务界面去兑换
    public static final String INTEGRAL_EXCHANGE = "Integral_Exchange";
    //积分任务界面tab点击
    public static final String  INTEGRAL_TASK_TAB_CHANGE = "Integral_Task_Tab_Change";
    //积分任务界面去完成
    public static final String  INTEGRAL_TASK_GO_FINISH = "Integral_Task_Go_Finish";
    //积分商城界面积分明细
    public static final String INTEGRAL_SHOP_DETAIL = "Integral_Shop_Detail";
    //积分商城界面积分任务
    public static final String  INTEGRAL_SHOP_TASK = "Integral_Shop_Task";



    //联系商家按钮
    public static final String ORDER_DETAIL_CONNECTION_SELLER = "Order_Detail_Connection_Seller";
    //久休客服和热线
    public static final String  ORDER_DETAIL_SERVICE = "Order_Detail_Service";

    //点击右上角分享按钮
    public static final String  STEP_SHARE = "Step_Share";
    //三方登录
    public static final String THREE_PARTY_LOGIN = "Three_Party_Login";


    /**－－－－－－－－－1.3 打点需求－－－－－end－－－－－－**/

    /**
     * 按钮点击次数
     */
    public static final String CLICK_EVENT_ID = "Activity_Click_Time";

    /**
     * 点击大类型
     */
    public static final String KEY_CLICK_EVENT_NAME = "activity_name";

    /**
     * 按钮名称
     */
    public static final String KEY_CLICK_EVENT_BUTTON = "activity_click_button";


    /**－－－－－－－－－2.0 打点需求－－－－－stat－－－－－－**/

    /*--------------首页打点--------------*/
    //计步器入口
    public static final String HOME_PEDOMETER = "Home_Pedometer";
    //签到
    public static final String HOME_CHECKIN = "Home_Checkin";
    //查看咨询服务列表
    public static final String HOME_CONSULTING_SERVICE_LIST = "Home_Consulting_Service_List";
    //咨询达人item点击
    public static final String HOME_VIEW_CONSULT_MASTER_ITEM = "Home_View_Consult_Master_Item";
    //抢先体验
    public static final String HOME_VIEW_LINE_DETAIL = "Home_View_Line_Detail";
    //旅行资讯
    public static final String HOME_VIEW_TRAVEL_INFO = "Home_View_Travel_Info";
    //本周推荐
    public static final String HOME_VIEW_WEEK_RECOMMAND = "Home_View_Week_Recommand";
    //久休推荐
    public static final String HOME_VIEW_QUANYAN_RECOMMAND = "Home_View_QuanYan_Recommand";
    //消息点击
    public static final String TAB_MINE_MSG_CLICK = "TAB_MINE_MSG_CLICK";


    /*--------------达人咨询服务--------------*/
    //咨询服务详情
    public static final String VIEW_CONSULTING_SERVICE_DETAIL = "View_Consulting_Service_Detail";
    //咨询达人首页banner
    public static final String HOME_CONSULTING_SERVICE_BANNER = "Home_Consulting_Service_Banner";
    //查看咨询服务全部
    public static final String CONSULTING_HOME_CONSULTING_SERVICE_LIST = "Consulting_Home_Consulting_Service_List";
    //咨询服务列表条件地区
    public static final String CONSULTING_SERVICE_LIST_FILTER_AREA = "Consulting_Service_List_Filter_Area";
    //咨询服务列表条件排序
    public static final String CONSULTING_SERVICE_LIST_FILTER_SORT = "Consulting_Service_List_Filter_Sort";
    //咨询达人首页（咨询商品点击）
    public static final String CONSULTING_HOME_CONSULT_ITEM_CLICK = "Consulting_Home_Consult_Item_Click";



    /*--------------达人首页--------------*/
    //达人故事
    public static final String VIEW_TALENT_STORY = "View_Talent_Story";
    //咨询达人查看全部
    public static final String TALENT_HOME_CONSULTING_SERIVCE_LIST = "Talent_Home_Consulting_Serivce_List";
    //线路达人查看全部
    public static final String TALENT_HOME_MASTER_LINE_LIST = "Talent_Home_Master_Line_List";
    //查看达人线路详情
    public static final String TALENT_HOME_VIEW_MASTER_LINE_DETAIL = "Talent_Home_View_Master_Line_Detail";
    //查看达人咨询服务详情
    public static final String TALENT_HOME_VIEW_MASTER_CONSULTING_SERVICE_DETAIL = "Talent_Home_View_Master_Consulting_Service_Detail";
    //包装页1
    public static final String TALENT_HOME_VIEW_H5_1 = "Talent_Home_View_H5_1";
    //包装页2
    public static final String TALENT_HOME_VIEW_H5_2 = "Talent_Home_View_H5_2";


    /*--------------咨询消息--------------*/
    //换其他达人按钮点击
    public static final String IM_CONSULTING_CHANGE_OTHER = "Im_Consulting_Change_Other";
    //立即咨询提交按钮点击
    public static final String IM_CONSULTING_START_NOW = "Im_Consulting_Start_Now";
    //快速咨询按钮点击
    public static final String IM_CONSULTING_QUICK_CLICK = "Im_Consulting_Quick_Click";
    //稍后再说按钮点击
    public static final String IM_CONSULTING_USER_REMIND_LATER = "Im_Consulting_User_Remind_Later";
    //接单按钮点击
    public static final String IM_CONSULTING_ACCEPT = "Im_Consulting_Accept";
    //稍后再说点击
    public static final String IM_CONSULTING_TALENT_REMIND_LATER = "Im_Consulting_Talent_Remind_Later";
    //应答按钮点击
    public static final String IM_CONSULTING_ACK = "Im_Consulting_Ack";
    //不用了按钮点击
    public static final String IM_CONSULTING_IGNORE = "Im_Consulting_Ignore";
    //再次下单按钮点击
    public static final String IM_CONSULTING_START_AGAIN = "Im_Consulting_Start_Again";
    //快速匹配没对应上
    public static final String IM_CONSULTING_NO_MATCH = "Im_Consulting_No_Match";


    /**－－－－－－－－－2.0 打点需求－－－－－end－－－－－－**/

    /**－－－－－－－－－2.1 打点需求－－－－－start－－－－－－**/
    //景区详情页点击随身导
    public static final String SCENIC_DETAIL_GUIDE_CLICK = "Scenic_Detail_Guide_click";
    //进入随身导首页
    public static final String GUIDE_HOMEPAGE_INTO = "Guide_Homepage_Into";
    //随身导首页点击随身导入口
    public static final String GUIDE_HOMEPAGE_GUIDE_CLICK = "Guide_Homepage_Guide_Click";
    //热门推荐
    public static final String GUIDE_HOMEPAGE_HOT_RECOMMEND = "Guide_Homepage_Hot_Recommend";
    //离我最近
    public static final String GUIDE_HOMEPAGE_NEARBY = "Guide_Homepage_Nearby";
    //右上角快速入口
    public static final String GUIDE_HOMEPAGE_FAST_INTO = "Guide_Homepage_Fast_Into";
    //导览详情页点击
    public static final String GUIDE_DETAIL_INTO = "Guide_Detail_Into";
    //导览详情页预订门票
    public static final String GUIDE_DETAIL_BOOK_CLICK = "Guide_Detail_Book_Click";
    //导览详情开场语
    public static final String GUIDE_DETAIL_OPEN_LISTEN = "Guide_Detail_Open_Listen";
    //导览详情看点音频点击
    public static final String GUIDE_DETAIL_WATCH_LISTEN = "Guide_Detail_Watch_Listen";
    //我的界面入口
    public static final String INTEGRAL_INVITATION_INTO = "Integral_Invitation_Into";
    //登录稍后领取
    public static final String LOGIN_LATER_RECEIVE = "Login_Later_Receive";
    //登录领起步基金
    public static final String LOGIN_RECEIVE_INTEGRAL = "Login_Receive_Integral";

    /**－－－－－－－－－2.1 打点需求－－－－－end－－－－－－**/



    /**－－－－－－－－－2.3 打点需求－－－－－start－－－－－－**/

    //首页的直播入口
    public static final String HOME_VIEW_LIVE_INTO = "Home_View_Live_Into";
    //地点索引
    public static final String LIVE_LOCATION_CLICK = "Live_Location_Click";
    //达人首页直播&回放入口
    public static final String MASTER_LIVE_ITEM_CLICK = "Master_Live_Item_Click";
    //达人首页全部入口
    public static final String MASTER_LIVE_MORE_CLICK = "Master_Live_More_Click";
    //达人圈直播&回放动态
    public static final String DISCOVER_LIVE_ITEM_CLICK = "Discover_Live_Item_Click";
    //播放直播+回放-主播tab
    public static final String LIVE_DETAIL_ANCHOR_TAB = "Live_Detail_Anchor_Tab";
    //播放直播+回放-主播的回放
    public static final String LIVE_DETAIL_VIDEO_ITEM_CLICK = "Live_Detail_Video_Item_Click";
    //播放直播+回放-关注按钮
    public static final String LIVE_DETAIL_ATTENTION_CLICK = "Live_Detail_Attention_Click";
    //播放直播+回放-全屏按钮
    public static final String LIVE_DETAIL_FULL_SCREAN_CLICK = "Live_Detail_Full_Screan_Click";
    //播放直播+回放-分享按钮
    //public static final String LIVE_DETAIL_SHARE_CLICK = "Live_Detail_Share_Click";
    //播放直播（横屏）- 收起弹幕
    public static final String LIVE_DETAIL_STOP_BARRAGE = "Live_Detail_Stop_Barrage";
    //播放直播（横屏）- 展开弹幕
    public static final String LIVE_DETAIL_OPEN_BARRAGE = "Live_Detail_Open_Barrage";
    //直播分享页
    //public static final String LIVE_DETAIL_SHARE_BUTTON_CLICK = "Live_Detail_Share_Button_Click";
    //播放直播+回放 分享类型点击
    public static final String LIVE_DETAIL_SHARE_TYPE_CLICK = "Live_Detail_Share_Type_Click";
    //我的直播-删除
    public static final String MINE_LIVE_DELETE_CLICK = "Mine_Live_Delete_Click";
    //我的-发起直播
    public static final String MINE_OPEN_LIVE = "Mine_Open_Live";
    //我的-继续直播
    public static final String MINE_LIVE_CONTINUE = "Mine_Live_Continue";
    //直播准备-添加话题
    public static final String LIVE_READY_ADD_TOPIC = "Live_Ready_Add_Topic";
    //直播准备-高清直播
    public static final String LIVE_READY_HIGH_DEFINITION = "Live_Ready_High_Definition";
    //直播准备-关闭定位
    public static final String LIVE_READY_CLOSE_LOCATION = "Live_Ready_Close_Location";
    //直播设置-直播公告
    public static final String LIVE_SETTING_NOTIC_CLICK = "Live_Setting_Notic_Click";
    //直播设置-直播分类
    public static final String LIVE_SETTING_TYPE_CLICK = "Live_Setting_Type_Click";
    //直播前分享+直播-前后摄像头切换
    public static final String LIVE_DETAIL_CAMERA_CLICK = "Live_Detail_Camera_Click";
    //直播前分享+直播-闪光灯
    public static final String LIVE_DETAIL_LAMP_CLICK = "Live_Detail_Lamp_Click";
    //直播前分享-分享
    public static final String LIVE_READY_SHARE_CLICK = "Live_Ready_Share_Click";
    //直播页-用户名称
    public static final String LIVE_DETAIL_UNAME_CLICK = "Live_Detail_Uname_Click";
    //直播页-禁言
    public static final String LIVE_DETAIL_GAG_CLICK = "Live_Detail_Gag_Click";
    //直播结束-分享
    public static final String LIVE_FINISH_SHARE_TYPE_CLICK = "Live_Finish_Share_Type_Click";




    //我的界面—扫一扫
    public static final String MINEPAGE_SCAN_CLICK = "MinePage_Scan_Click";
    //我的界面—我的二维码
    public static final String MINEPAGE_MINE_QR_CLICK = "MinePage_Mine_Qr_Click";
    //扫一扫页—相册
    public static final String SCANPAGE_IMAGE_CLICK = "ScanPage_Image_Click";
    //扫一扫页—我的二维码
    public static final String SCANPAGE_MINE_QR_CLICK = "ScanPage_Mine_Qr_Click";
    //我的二维码页—保存图片
    public static final String MINEQRPAGE_SAVE_IMAGE_CLICK = "MineQrPage_Save_Image_Click";
    //我的二维码页—分享
    public static final String MINEQRPAGE_SHARE_TYPE = "MineQrPage_Share_Type";
    //个人资料页—我的二维码
    public static final String MINEINFOPAGE_MINE_QR_CLICK = "MineInfoPage_Mine_Qr_Click";


    /**－－－－－－－－－2.3 打点需求－－－－－end－－－－－－**/
    //guide向导页点击进入按钮
    public static final String GUIDE_BUTTON_CLICK = "Guide_Button_Click";
    //logo图片点击
    public static final String LOGO_IMAGE_CLICK = "Logo_Image_Click";

    //积分订单猜你喜欢推荐
    public static final String POINT_ORDER_DETAIL_RECOMMEND = "Point_Order_Detail_Recommend";

    //购物车付款成功
    public static final String INTEGRAL_PAY_SUCCESS = "Integral_Pay_Success";



    public static String getType(String type){
        String anaType = "";
        if(ItemType.NORMAL.equals(type)){
            anaType = AnalyDataValue.SEARCH_CLICK_MUST_BUY;//必买
        }else if(ItemType.MASTER.equals(type)){
            anaType = AnalyDataValue.SEARCH_CLICK_MASTER;//达人
        }else if(ItemType.MASTER_PRODUCTS.equals(type)){
            anaType = AnalyDataValue.SEARCH_CLICK_MASTER_PRODUCT;//达人商品
        }else if(ItemType.TOUR_LINE.equals(type) || ItemType.TOUR_LINE_ABOARD.equals(type)){
            anaType = AnalyDataValue.SEARCH_CLICK_PACKAGE_TOUR;//跟团游
        }else if(ItemType.FREE_LINE.equals(type) || ItemType.FREE_LINE_ABOARD.equals(type)){
            anaType = AnalyDataValue.SEARCH_CLICK_FREE_WALK;//自由行
        }else if(ItemType.CITY_ACTIVITY.equals(type)){
            anaType = AnalyDataValue.SEARCH_CLICK_CITY_ACTIVITY;//同城活动
        }else if(ItemType.ARROUND_FUN.equals(type)){
            anaType = AnalyDataValue.SEARCH_CLICK_CITY_ARROUND_FUN;//周边
        }else if(ItemType.POINT_MALL.equals(type)){
            anaType = AnalyDataValue.PRODUCT_DETAIL_INTEGRAL;//积分
        }else {
            anaType = type;
        }
        return anaType;
    }

}
