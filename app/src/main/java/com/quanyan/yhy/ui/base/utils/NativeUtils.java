package com.quanyan.yhy.ui.base.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.newyhy.activity.HomeMainTabActivity;
import com.newyhy.activity.HorizontalReplayActivity;
import com.quanyan.base.util.LocalUtils;
import com.quanyan.base.util.StringUtil;
import com.quanyan.pedometer.PedoActivity;
import com.quanyan.pedometer.ShareActivity;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.ItemType;
import com.quanyan.yhy.common.SysConfigType;
import com.quanyan.yhy.eventbus.EvBusRecPointsStatus;
import com.quanyan.yhy.eventbus.EvBusStartFund;
import com.quanyan.yhy.eventbus.EvBusWebUserLoginState;
import com.quanyan.yhy.eventbus.GuangDaPaySuccessEvBus;
import com.quanyan.yhy.service.controller.OrderController;

import com.quanyan.yhy.ui.common.WebViewActivity;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.discovery.LiveDetailActivity;
import com.quanyan.yhy.ui.discovery.TopicDetailsActivity;
import com.quanyan.yhy.ui.guide.GuideActivity;
import com.quanyan.yhy.ui.line.CommodityDetailActivity;
import com.quanyan.yhy.ui.line.LineActivity;
import com.quanyan.yhy.ui.master.activity.MasterAdviceListActivity;
import com.quanyan.yhy.ui.master.activity.MasterConsultHomeActivity;
import com.quanyan.yhy.ui.nineclub.EatGreatActivity;
import com.quanyan.yhy.ui.nineclub.EatGreatDetailActivity;
import com.quanyan.yhy.ui.signed.activity.IntegralActivity;
import com.videolibrary.client.activity.HorizontalVideoClientActivity;
import com.videolibrary.client.activity.LiveListActivity;
import com.videolibrary.utils.IntentUtil;
import com.yhy.common.beans.net.model.NativeBean;
import com.yhy.common.beans.net.model.ShareBean;
import com.yhy.common.beans.net.model.param.WebParams;
import com.yhy.common.beans.net.model.user.NativeDataBean;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.types.BannerType;
import com.yhy.common.utils.SPUtils;
import com.yhy.router.YhyRouter;
import com.yhy.service.IUserService;

import org.json.JSONObject;

import de.greenrobot.event.EventBus;

/**
 * Created with Android Studio.
 * Title:NativeUtils
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-3-9
 * Time:15:54
 * Version 1.0
 * Description:处理html穿过来的值进行界面跳转
 */
public class NativeUtils {

    public static String AROUND_FUN = "AROUND_FUN";//周边玩乐首页
    public static String AROUND_FUN_DETAIL = "AROUND_FUN_DETAIL";//周边玩乐线路详情
    public static String AROUND_FUN_LIST = "AROUND_FUN_LIST";//周边玩乐专题列表
    public static String CITY_ACTIVITY = "CITY_ACTIVITY";//同城活动首页
    public static String CITY_ACTIVITY_DETAIL = "CITY_ACTIVITY_DETAIL";//同城活动详情
    public static String CITY_ACTIVITY_LIST = "CITY_ACTIVITY_LIST";//同城活动专题列表
    public static String FREE_TOUR = "FREE_TOUR";//自由行首页
    public static String FREE_TOUR_DETAIL = "FREE_TOUR_DETAIL";//自由行线路详情
    public static String FREE_TOUR_ABOARD_DETAIL = "FREE_TOUR_ABOARD_DETAIL";//境外自由行详情
    public static String FREE_TOUR_LIST = "FREE_TOUR_LIST";//自由行列表
    public static String PACKAGE_TOUR = "PACKAGE_TOUR";//跟团游首页
    public static String PACKAGE_TOUR_ABOARD_DETAIL = "PACKAGE_TOUR_ABOARD_DETAIL";//境外跟团游详情
    public static String PACKAGE_TOUR_DETAIL = "PACKAGE_TOUR_DETAIL";//跟团游详情
    public static String PACKAGE_TOUR_LIST = "PACKAGE_TOUR_LIST";//跟团游专题列表
    public static String QUANYAN_FOOD = "QUANYAN_FOOD";//美食首页
    public static String QUANYAN_FOOD_DETAIL = "QUANYAN_FOOD_DETAIL";//美食店铺首页
    public static String QUANYAN_BUY = "QUANYAN_BUY";//必买首页
    public static String QUANYAN_BUY_DETAIL = "QUANYAN_BUY_DETAIL";//必买详情
    public static String SHOP_HOME_PAGE = "SHOP_HOME_PAGE";//店铺首页
    public static String SCENIC_HOME_PAGE = "SCENIC_HOME_PAGE";//景区首页
    public static String SCENIC = "SCENIC";//景区详情
    public static String SCENIC_LIST = "SCENIC_LIST";//景区专题列表
    public static String HOTEL_HOME_PAGE = "HOTEL_HOME_PAGE";//酒店首页
    public static String HOTEL = "HOTEL";//酒店详情
    //    public static String HOTEL_LIST = "HOTEL_LIST";//酒店专题列表
    public static String QUANYAN_CLUB = "QUANYAN_CLUB";//优club首页
    public static String QUANYAN_MASTER = "QUANYAN_MASTER";//达人首页
    public static String MASTER_DETAIL = "MASTER_DETAIL";//达人主页
    public static String MASTER_LIST = "MASTER_LIST";//达人专题列表
    public static String LOGIN = "LOGIN";//跳转登录
    public static String REGISTER = "REGISTER";//跳转注册
    public static String CHAT_DETAIL = "CHAT_DETAIL";//会话页面
    public static String MSG_CENTER = "MSG_CENTER";//消息列表
    public static String CALL = "CALL";//拨打电话
    //分享
    public static String WEB_SHARE = "WEB_SHARE";
    //打开内部浏览器
    public static String OPEN_INNER_WEB_BROWSER = "OPEN_WEB";
    //查看达人广场
    public static String VIEW_HOME_MASTER_DYNAMIC = "VIEW_HOME_MASTER_DYNAMIC";
    //查看达人圈
    public static String HOME_MASTER_CIRCLE = "HOME_MASTER_CIRCLE";
    //查看积分商城首页
    public static String VIEW_INTEGRAL_MALL = "VIEW_INTEGRAL_MALL";
    //查看积分商城首页h5
    public static String WEB_INTEGRAL_MALL = "WEB_INTEGRAL_MALL";
    //查看积分明细
    public static String VIEW_INTEGRAL_DETAIL = "VIEW_INTEGRAL_DETAIL";
    //查看积分任务列表
    public static String VIEW_INTEGRAL_TASK_LIST = "VIEW_INTEGRAL_TASK_LIST";
    //查看话题详情
    public static String VIEW_TOPIC_DETAIL = "VIEW_TOPIC_DETAIL";
    //领取积分的状态
    public static final String RECEIVE_POINTS = "RECEIVE_POINTS";
    //邀请好友分享
    public static final String INVITE_FRENIDS = "INVITE_FRENIDS";
    //关闭当前页面
    public static final String CLOSE_WINDOW = "CLOSE_WINDOW";
    //领取跑步基金
    public static final String START_FUND = "START_FUND";
    //达人服务商品详情
    public static final String VIEW_CONSULTING_SERVICE_DETAIL = "VIEW_CONSULTING_SERVICE_DETAIL";
    //提交普通咨询
    public static final String SUBMIT_COMMON_CONSULTING_INFORMATION = "SUBMIT_COMMON_CONSULTING_INFORMATION";
    //提交快速咨询
    public static final String SUBMIT_QUICK_CONSULTING_INFORMATION = "SUBMIT_QUICK_CONSULTING_INFORMATION";
    //达人故事
    public static final String VIEW_TALENT_STORY = "VIEW_TALENT_STORY";
    //咨询达人首页
    public static final String HOME_CONSULTING_LIST = "HOME_CONSULTING_LIST";
    //话题列表
    public static final String VIEW_TOPIC_LIST = "VIEW_TOPIC_LIST";
    //旅游资讯
    public static final String TRAVEL_INFORMATION = "TRAVEL_INFORMATION";
    //积分商城商品详情
    public static final String VIEW_INTEGRAL_MALL_DETAIL = "VIEW_INTEGRAL_MALL_DETAIL";
    //邀请有礼首页
    public static final String HOME_INTEGRAL_FISSION = "HOME_INTEGRAL_FISSION";
    //光大支付成功的通知
    public static final String NOTIFY_GUANGDA_PAY_SUCCESS = "NOTIFY_GUANGDA_PAY_SUCCESS";

    //消息通知新加类型
    //酒店首页
    public static final String HOTEL_HOME = "HOTEL_HOME";
    //景区首页
    public static final String SCENIC_HOME = "SCENIC_HOME";
    //随身导
    public static final String HOME_TOUR_GUIDE = "HOME_TOUR_GUIDE";
    //积分商城首页
    public static final String HOME_INTEGRAL_MALL = "HOME_INTEGRAL_MALL";
    //签到首页
    public static final String HOME_CHECKIN = "HOME_CHECKIN";
    //步步前进首页
    public static final String HOME_PEDOMETER = "HOME_PEDOMETER";
    //达人广场首页
    public static final String VIEW_MASTER_CIRCLE_LIST = "VIEW_MASTER_CIRCLE_LIST";
    //咨询达人全部列表页
    public static final String MASTER_CONSULT_SERVICE_LIST = "MASTER_CONSULT_SERVICE_LIST";
    //酒店资源详情
    public static final String HOTEL_DETAIL = "HOTEL_DETAIL";
    //景区详情
    public static final String SCENIC_DETAIL = "SCENIC_DETAIL";
    //随身导景区详情
    public static final String VIEW_TOUR_GUIDE_DETAIL = "VIEW_TOUR_GUIDE_DETAIL";
    //积分商城商品详情
    public static final String INTEGRAL_MALL_DETAIL = "INTEGRAL_MALL_DETAIL";
    //达人圈帖子详情
    public static final String MASTER_CIRCLE_DETAIL = "MASTER_CIRCLE_DETAIL";
    //随身导列表页
    public static final String HOME_TOUR_LIST = "HOME_TOUR_LIST";
    //购物车列表
    public static final String SHOPCART_LIST = "SHOPCART_LIST";
    //去商店升级
    public static final String VERSION_UPGRADE = "VERSION_UPGRADE";
    //文章详情
    public static final String OPEN_ARTICLE_DETAIL = "OPEN_ARTICLE_DETAIL";
    //短视频详情
    public static final String OPEN_SHORT_MOVIE = "OPEN_SHORT_MOVIE";

    private static NativeUtils instance = new NativeUtils();

    @Autowired
    IUserService userService;

    private NativeUtils(){
        YhyRouter.getInstance().inject(this);
    }

    public static void parseNativeData(Intent intent, Context context) {
        String action = intent.getAction();
//        if (Intent.ACTION_VIEW.equals(action)) {
        if (Intent.ACTION_VIEW.equals(action)) {
            parseNativeData(intent.getData(), context);
        }
    }

    public static void parseNativeData(Uri uri, Context context) {
        if (uri != null) {
            String content = uri.getQueryParameter("content");
            if (!TextUtils.isEmpty(content)) {
                htmlSkipNative(context, parseJson(content));
            }
        }
    }

    public static NativeBean parseJson(String content) {
        if (TextUtils.isEmpty(content)) {
            return null;
        }
        NativeBean bean = new NativeBean();
        try {
            JSONObject json = new JSONObject(content);
            if (json.has("TYPE")) {
                bean.setTYPE(json.getString("TYPE"));
            }
            if (json.has("OPERATION")) {
                bean.setOPERATION(json.getString("OPERATION"));
            }
            if (json.has("DATA")) {
                String DATA = json.getString("DATA");
                NativeDataBean dataBean = new NativeDataBean();
                if (!TextUtils.isEmpty(DATA)) {
                    JSONObject js = new JSONObject(DATA);
                    if (js.has("id")) {
                        dataBean.setId(js.getString("id"));
                    }
                    if (js.has("title")) {
                        dataBean.setTitle(js.getString("title"));
                    }
                    if (js.has("tagId")) {
                        dataBean.setTagId(js.getString("tagId"));
                    }
                    if (js.has("tagName")) {
                        dataBean.setTagName(js.getString("tagName"));
                    }
                    if (js.has("cityName")) {
                        dataBean.setCityName(js.getString("cityName"));
                    }
                    if (js.has("cityCode")) {
                        dataBean.setCityCode(js.getString("cityCode"));
                    }
                    if (js.has("uid")) {
                        dataBean.setUid(js.getString("uid"));
                    }
                    if (js.has("name")) {
                        dataBean.setName(js.getString("name"));
                    }
                    if (js.has("phone")) {
                        dataBean.setPhone(js.getString("phone"));
                    }
                    if (js.has("shareTitle")) {
                        dataBean.setShareTitle(js.getString("shareTitle"));
                    }
                    if (js.has("shareContent")) {
                        dataBean.setShareContent(js.getString("shareContent"));
                    }
                    if (js.has("shareImageUrl")) {
                        dataBean.setShareImageUrl(js.getString("shareImageUrl"));
                    }
                    if (js.has("shareWebPage")) {
                        dataBean.setShareWebPage(js.getString("shareWebPage"));
                    }
                    if (js.has("shareWay")) {
                        dataBean.setShareWay(js.getInt("shareWay"));
                    }
                    if (js.has("link")) {
                        dataBean.setLink(js.getString("link"));
                    }
                    if (js.has("topicName")) {
                        dataBean.setTopicName(js.getString("topicName"));
                    }
                    if (js.has("status")) {
                        dataBean.setStatus(js.getString("status"));
                    }
                    if (js.has("option")) {
                        dataBean.setOption(js.getString("option"));
                    }
                    if (js.has("isCleanCookie")) {
                        dataBean.setCleanCookie(js.getBoolean("isCleanCookie"));
                    }

                    if (js.has("point")) {
                        dataBean.setPoint(js.getLong("point"));
                    }

                    if (js.has("time")) {
                        dataBean.setTime(js.getLong("time"));
                    }

                    if (js.has("isShowTitle")) {
                        dataBean.setShowTitle(js.getBoolean("isShowTitle"));
                    }

                    if (js.has("isShowTitle")) {
                        dataBean.setShowTitle(js.getBoolean("isShowTitle"));
                    }

                    if (js.has("orderId")) {
                        dataBean.setOrderId(js.getLong("orderId"));
                    }

                    if (js.has("orderType")) {
                        dataBean.setOrderType(js.getString("orderType"));
                    }

                    if (js.has("liveScreenType")) {
                        dataBean.setLiveScreenType(js.getInt("liveScreenType"));
                    }

                    if (js.has("ugcid")) {
                        dataBean.setUgcId(js.getLong("ugcid"));
                    }

//                    if (js.has("isSetTitle")) {
//                        dataBean.setSetTitle(js.getBoolean("isSetTitle"));
//                    }
                    bean.setData(dataBean);
                }
            }
        } catch (Exception e) {
            bean = null;
            e.printStackTrace();
        }

        return bean;
    }

    public static void htmlSkipNative(Context context, NativeBean bean) {
        if (bean == null) {
            return;
        }
        //界面跳转
        if (!TextUtils.isEmpty(bean.getOPERATION())) {
            if (bean.getOPERATION().equals(AROUND_FUN)) {
                //TODO 1.周边玩乐首页
                NavUtils.gotoLineActivity(context,
                        -1,
                        ItemType.ARROUND_FUN,
                        context.getString(R.string.label_title_arround_fun));
            } else if (bean.getOPERATION().equals(AROUND_FUN_DETAIL)) {
                //TODO 2.周边玩乐线路详情
                if (bean.getData() != null && bean.getData().getId() != null) {
                    NavUtils.gotoProductDetail(context,
                            ItemType.TOUR_LINE,
                            Long.parseLong(bean.getData().getId()),
                            bean.getData().getName());
                } else {
                    ToastUtil.showToast(context, context.getString(R.string.toast_banner_params_error));
                }
            } else if (bean.getOPERATION().equals(AROUND_FUN_LIST)) {
                //TODO 3.周边玩乐专题列表
                if (bean.getData() != null && bean.getData().getTagId() != null) {
                    NavUtils.gotoNineClubDetailListActivity(context,
                            bean.getData().getTagId(),
                            ItemType.ARROUND_FUN,
                            bean.getData().getTagName());
                } else {
                    ToastUtil.showToast(context, context.getString(R.string.toast_banner_params_error));
                }
            } else if (bean.getOPERATION().equals(CITY_ACTIVITY)) {
                //TODO 4.同城活动首页
                NavUtils.gotoLineActivity(context,
                        -1,
                        ItemType.CITY_ACTIVITY,
                        context.getString(R.string.label_title_city_activity));
            } else if (bean.getOPERATION().equals(CITY_ACTIVITY_DETAIL)) {
                //TODO 5.同城活动详情
                if (bean.getData() != null && bean.getData().getId() != null) {
                    NavUtils.gotoProductDetail(context,
                            ItemType.CITY_ACTIVITY,
                            Long.parseLong(bean.getData().getId()),
                            bean.getData().getName());
                } else {
                    ToastUtil.showToast(context, context.getString(R.string.toast_banner_params_error));
                }
            } else if (bean.getOPERATION().equals(CITY_ACTIVITY_LIST)) {
                //TODO 6.同城活动专题列表
                if (bean.getData() != null && bean.getData().getTagId() != null) {
                    NavUtils.gotoNineClubDetailListActivity(context,
                            bean.getData().getTagId(),
                            ItemType.CITY_ACTIVITY,
                            bean.getData().getTagName());
                } else {
                    ToastUtil.showToast(context, context.getString(R.string.toast_banner_params_error));
                }
            } else if (bean.getOPERATION().equals(FREE_TOUR)) {
                //TODO 7.自由行首页
                NavUtils.gotoLineActivity(context,
                        -1,
                        ItemType.FREE_LINE,
                        context.getString(R.string.label_title_free_walk));
            } else if (bean.getOPERATION().equals(FREE_TOUR_DETAIL)) {
                //TODO 8.自由行线路详情
                if (bean.getData() != null && bean.getData().getId() != null) {
                    NavUtils.gotoProductDetail(context,
                            ItemType.FREE_LINE,
                            Long.parseLong(bean.getData().getId()),
                            bean.getData().getName());
                } else {
                    ToastUtil.showToast(context, context.getString(R.string.toast_banner_params_error));
                }
            } else if (bean.getOPERATION().equals(FREE_TOUR_ABOARD_DETAIL)) {
                //TODO 8.境外自由行线路详情
                if (bean.getData() != null && bean.getData().getId() != null) {
                    NavUtils.gotoProductDetail(context,
                            ItemType.FREE_LINE_ABOARD,
                            Long.parseLong(bean.getData().getId()),
                            bean.getData().getName());
                } else {
                    ToastUtil.showToast(context, context.getString(R.string.toast_banner_params_error));
                }
            } else if (bean.getOPERATION().equals(FREE_TOUR_LIST)) {
                //TODO 9.自由行列表
                if (bean.getData() != null && bean.getData().getTagId() != null) {
                    NavUtils.gotoNineClubDetailListActivity(context,
                            bean.getData().getTagId(),
                            ItemType.FREE_LINE,
                            bean.getData().getTagName());
                } else {
                    ToastUtil.showToast(context, context.getString(R.string.toast_banner_params_error));
                }
            } else if (bean.getOPERATION().equals(PACKAGE_TOUR)) {
                //TODO 10.跟团游首页
                NavUtils.gotoLineActivity(context,
                        -1,
                        ItemType.TOUR_LINE,
                        context.getString(R.string.label_title_package_tour));
            } else if (bean.getOPERATION().equals(PACKAGE_TOUR_DETAIL)) {
                //TODO 11.跟团游详情
                if (bean.getData() != null && bean.getData().getId() != null) {
                    NavUtils.gotoProductDetail(context,
                            ItemType.TOUR_LINE,
                            Long.parseLong(bean.getData().getId()),
                            bean.getData().getName());
                } else {
                    ToastUtil.showToast(context, context.getString(R.string.toast_banner_params_error));
                }
            } else if (bean.getOPERATION().equals(PACKAGE_TOUR_ABOARD_DETAIL)) {
                //TODO 11.境外跟团游详情
                if (bean.getData() != null && bean.getData().getId() != null) {
                    NavUtils.gotoProductDetail(context,
                            ItemType.TOUR_LINE_ABOARD,
                            Long.parseLong(bean.getData().getId()),
                            bean.getData().getName());
                } else {
                    ToastUtil.showToast(context, context.getString(R.string.toast_banner_params_error));
                }
            } else if (bean.getOPERATION().equals(PACKAGE_TOUR_LIST)) {
                //TODO 12.跟团游专题列表
                if (bean.getData() != null && bean.getData().getTagId() != null) {
                    NavUtils.gotoNineClubDetailListActivity(context,
                            bean.getData().getTagId(),
                            ItemType.TOUR_LINE,
                            bean.getData().getTagName());
                } else {
                    ToastUtil.showToast(context, context.getString(R.string.toast_banner_params_error));
                }
            } else if (bean.getOPERATION().equals(QUANYAN_FOOD)) {
                //TODO 13.美食首页
                NavUtils.gotoEatGreatActivity(context, null);
            } else if (bean.getOPERATION().equals(QUANYAN_FOOD_DETAIL)) {
                //TODO 13.美食店铺首页
                if (bean.getData() != null && bean.getData().getId() != null) {
                    NavUtils.gotoEatGreatDetailActivity(context,
                            Long.parseLong(bean.getData().getId()));
                } else {
                    ToastUtil.showToast(context, context.getString(R.string.toast_banner_params_error));
                }
            } else if (bean.getOPERATION().equals(QUANYAN_BUY)) {
                //TODO 14.必买首页
                NavUtils.gotoBuyMustListActivity(context);
            } else if (bean.getOPERATION().equals(QUANYAN_BUY_DETAIL)) {
                //TODO 14.必买商品详情
                if (bean.getData() != null && bean.getData().getId() != null) {
                    NavUtils.gotoProductDetail(context,
                            ItemType.NORMAL,
                            Long.parseLong(bean.getData().getId()),
                            (bean.getData().getName()));
                } else {
                    ToastUtil.showToast(context, context.getString(R.string.toast_banner_params_error));
                }
            } else if (bean.getOPERATION().equals(SHOP_HOME_PAGE)) {
                //TODO 15.店铺首页
                if (bean.getData() != null && bean.getData().getId() != null) {
                    NavUtils.gotoShopHomePageActivity(context,
                            bean.getData().getName(),
                            Long.parseLong(bean.getData().getId()));
                } else {
                    ToastUtil.showToast(context, context.getString(R.string.toast_banner_params_error));
                }
            } else if (bean.getOPERATION().equals(QUANYAN_CLUB)) {
                //TODO 22.优club首页
                NavUtils.gotoNineClubActivity(context);
            } else if (bean.getOPERATION().equals(QUANYAN_MASTER)) {
                //TODO 23.达人首页
                NavUtils.gotoMasterListActivity(context, context.getString(R.string.label_title_master_list));
            } else if (bean.getOPERATION().equals(MASTER_DETAIL)) {
                //TODO 24.达人主页
                if (bean.getData() != null && !StringUtil.isEmpty(bean.getData().getId()) && !StringUtil.isEmpty(bean.getData().getOption())) {
                    NavUtils.gotoPersonalPage(context, Long.parseLong(bean.getData().getId()), null,
                            Long.parseLong(bean.getData().getOption()));
                } else {
                    ToastUtil.showToast(context, context.getString(R.string.toast_banner_params_error));
                }
            } else if (bean.getOPERATION().equals(MASTER_LIST)) {
                //TODO 25.达人专题列表
                ToastUtil.showToast(context, context.getString(R.string.label_function_expired));
            } else if (bean.getOPERATION().equals(LOGIN)) {
                //TODO 26.跳转登录
                if (instance.userService.isLogin()) {
                    EventBus.getDefault().post(new EvBusWebUserLoginState(EvBusWebUserLoginState.LOGIN_STATE));
                } else {
                    NavUtils.gotoLoginActivity(context);
                }
            } else if (bean.getOPERATION().equals(REGISTER)) {
                //TODO 27.跳转注册
                if (instance.userService.isLogin()) {
                    EventBus.getDefault().post(new EvBusWebUserLoginState(EvBusWebUserLoginState.LOGIN_STATE));
                } else {
                    NavUtils.gotoLoginActivity(context);
                }
            } else if (bean.getOPERATION().equals(CHAT_DETAIL)) {
                //TODO 28.会话页面
                NavUtils.gotoMessageActivity(context, Integer.parseInt(bean.getData().getUid()));
            } else if (bean.getOPERATION().equals(MSG_CENTER)) {
                //TODO 29.消息列表
                NavUtils.gotoMsgCenter(context);
            } else if (bean.getOPERATION().equals(CALL)) {
                //TODO 30.拨打电话
                LocalUtils.call(context, bean.getData().getPhone());
            } else if (bean.getOPERATION().equals(WEB_SHARE)) {
                //TODO 31.分享
                ShareBean shareBean = new ShareBean();
                if (!StringUtil.isEmpty(bean.getData().getShareTitle())) {
                    shareBean.shareTitle = bean.getData().getShareTitle();
                }
                if (!StringUtil.isEmpty(bean.getData().getShareContent())) {
                    shareBean.shareContent = bean.getData().getShareContent();
                }
                if (!StringUtil.isEmpty(bean.getData().getShareImageUrl())) {
                    shareBean.shareImageURL = bean.getData().getShareImageUrl();
                }
                if (!StringUtil.isEmpty(bean.getData().getShareWebPage())) {
                    shareBean.shareWebPage = bean.getData().getShareWebPage();
                }
                if (!StringUtil.isEmpty(bean.getData().getShareWebPage())) {
                    shareBean.shareWay = bean.getData().getShareWay();
                }
                NavUtils.gotoShareTableActivity(context, shareBean, null);
            } else if (bean.getOPERATION().equals(OPEN_INNER_WEB_BROWSER)) {
                //TODO 32.打开内部浏览器
                if (bean != null && bean.getData() != null && bean.getData().getLink() != null) {
                    WebParams wp = new WebParams();
                    wp.setIsCleanCookie(bean.getData().isCleanCookie());
                    wp.setUrl(bean.getData().getLink());
                    wp.setShowTitle(bean.getData().isShowTitle());
                    NavUtils.openBrowser(context, wp);
                }
            } else if (bean.getOPERATION().equals(HOME_MASTER_CIRCLE)) {
                //TODO 33.查看达人圈
                NavUtils.gotoDiscoveryFragment(context, 0);
            } else if (bean.getOPERATION().equals(VIEW_INTEGRAL_MALL)) {
                //TODO 34.查看积分商城首页native
                ToastUtil.showToast(context, "页面已被删掉");
//                IntegralmallHomeActivity.gotoIntegralmallHomeActivity(context);
            } else if (bean.getOPERATION().equals(WEB_INTEGRAL_MALL)) {
                //TODO 34.查看积分商城首页H5
                NavUtils.gotoIntegralmallHomeActivity(context);
            } else if (bean.getOPERATION().equals(VIEW_INTEGRAL_MALL_DETAIL)) {
                //TODO 34.查看积分商城商品详情
                if (bean.getData() != null && bean.getData().getId() != null) {
//                    NavUtils.gotoProductDetail(context, ItemType.POINT_MALL, Long.parseLong(bean.getData().getId()), "");
                    String url = SPUtils.getURL_POINT_ITEM_DETAIL(context);
                    if (TextUtils.isEmpty(url))
                        return;
                    NavUtils.startWebview((Activity) context, "", url.replace(":id", bean.getData().getId()), 0);
                } else {
                    ToastUtil.showToast(context, context.getString(R.string.toast_banner_params_error));
                }
            } else if (bean.getOPERATION().equals(VIEW_INTEGRAL_DETAIL)) {
                //TODO 35.查看积分明细
                if (instance.userService.isLogin()) {
                    String url = SPUtils.getVIEW_INTEGRAL_DETAIL(context);
                    NavUtils.startWebview((Activity) context, "", url, 0);
                } else {
                    NavUtils.gotoLoginActivity(context, bean);
                }
            } else if (VIEW_INTEGRAL_TASK_LIST.equals(bean.getOPERATION())) {
                //TODO 351 查看任务列表
                if (instance.userService.isLogin()) {
                    //                    NavUtils.gotoIntegralActivity(context, 0);
                    String url = SPUtils.getVIEW_INTEGRAL_TASK_LIST(context);
                    NavUtils.startWebview((Activity) context, "", url, 0);
                } else {
                    NavUtils.gotoLoginActivity(context, bean);
                }
            } else if (bean.getOPERATION().equals(VIEW_TOPIC_DETAIL)) {
                //TODO 35.查看话题详情
                if (bean != null && bean.getData() != null && bean.getData().getTopicName() != null) {
                    //NavUtils.gotoTopicDetailsActivity(context, bean.getData().getTopicName(), -1);
                    NavUtils.gotoNewTopicDetailsActivity(context, bean.getData().getTopicName(), -1);
                }
            } else if (BannerType.STR_HOME_PEDMOTER.equals(bean.getOPERATION())) {
                //TODO 计步器首页
                NavUtils.gotoPedometerActivity(context);
            } else if (INVITE_FRENIDS.equals(bean.getOPERATION())) {
                //TODO 邀请好友
                if (bean.getData() != null) {
                    NavUtils.gotoShareActivity(context, ShareActivity.INVITE_FRIENDS, bean.getData().getShareWay());
                }
            } else if (RECEIVE_POINTS.equals(bean.getOPERATION())) {
                //TODO 领取积分状态
                if (bean != null && bean.getData() != null && bean.getData().getStatus() != null) {
                    EventBus.getDefault().post(new EvBusRecPointsStatus(bean.getData().getStatus()));
                }
            } else if (CLOSE_WINDOW.equals(bean.getOPERATION())) {
                //TODO 关闭当前页面
                if (context instanceof Activity) {
                    ((Activity) context).finish();
                } else if (context instanceof FragmentActivity) {
                    ((FragmentActivity) context).finish();
                }
            } else if (START_FUND.equals(bean.getOPERATION())) {
                //TODO 领取起步基金
                if (bean != null && bean.getData() != null) {
                    EventBus.getDefault().post(new EvBusStartFund());
                }
            } else if (VIEW_CONSULTING_SERVICE_DETAIL.equals(bean.getOPERATION())) {
                //TODO 达人服务商品详情
                if (bean.getData() != null && bean.getData().getId() != null) {
                    NavUtils.gotoProductDetail(context, ItemType.CONSULT, Long.parseLong(bean.getData().getId()), "");
                } else {
                    ToastUtil.showToast(context, context.getString(R.string.toast_banner_params_error));
                }
            } else if (SUBMIT_COMMON_CONSULTING_INFORMATION.equals(bean.getOPERATION())) {
                //TODO 提交普通咨询
                if (bean.getData() != null && bean.getData().getId() != null) {
                    NavUtils.gotoMasterConsultActivity(context,
                            Long.parseLong(bean.getData().getId()),
                            bean.getData().getPoint(),
                            bean.getData().getTime());
                } else {
                    ToastUtil.showToast(context, context.getString(R.string.toast_banner_params_error));
                }
            } else if (SUBMIT_QUICK_CONSULTING_INFORMATION.equals(bean.getOPERATION())) {
                //TODO 提交快速咨询
                NavUtils.gotoQuickConsultActivity(context);
            } else if (VIEW_TALENT_STORY.equals(bean.getOPERATION())) {
                //TODO 达人故事
                if (bean.getData() != null && bean.getData().getId() != null) {
                    WebParams wp = new WebParams();
                    String url = SPUtils.getString(SPUtils.TYPE_DEFAULT, context, SysConfigType.URL_TALENT_STORY);
                    wp.setUrl(url + bean.getData().getId());
                    wp.setShowTitle(false);
                    NavUtils.openBrowser(context, wp);
                }
            } else if (TRAVEL_INFORMATION.equals(bean.getOPERATION())) {
                //TODO 旅游资讯
                if (bean.getData() != null && !StringUtil.isEmpty(bean.getData().getLink())) {
                    WebParams wp = new WebParams();
                    wp.setUrl(bean.getData().getLink());
                    wp.setShowTitle(false);
                    NavUtils.openBrowser(context, wp);
                }
            } else if (HOME_CONSULTING_LIST.equals(bean.getOPERATION())) {
                //TODO 咨询达人首页
                NavUtils.gotoMasterConsultHomeActivity(context);
            } else if (BannerType.STR_MASTER_CONSULT_SERVICE_LIST.equals(bean.getOPERATION())) {
                //TODO 咨询达人列表页
                NavUtils.gotoMasterAdviceListActivity(context);
            } else if (VIEW_TOPIC_LIST.equals(bean.getOPERATION())) {
                //TODO 话题列表
                NavUtils.gotoDiscoveryFragment(context, 1);
            } else if (HOME_INTEGRAL_FISSION.equals(bean.getOPERATION())) {
                // TODO: 2016-10-12 邀请有礼首页
                String inviteGiftUrl = SPUtils.getInviteGiftUrl(context);
                if (!StringUtil.isEmpty(inviteGiftUrl)) {
                    WebParams wp = new WebParams();
                    //wp.setIsSetTitle(false);
                    wp.setShowTitle(false);
                    wp.setUrl(inviteGiftUrl);
                    NavUtils.openBrowser(context, wp);
                }
            } else if (NOTIFY_GUANGDA_PAY_SUCCESS.equals(bean.getOPERATION())) {
                //TODO 光大支付成功的通知
                EventBus.getDefault().post(new GuangDaPaySuccessEvBus());
            } else if (BannerType.STR_LIVE_PLAYBACK_DETAIL.equals(bean.getOPERATION())) {
                //TODO 查看直播回访详情
                if (bean.getData() != null && !StringUtil.isEmpty(bean.getData().getId())) {
                    long liveId = -1;
                    try {
                        liveId = Long.parseLong(bean.getData().getId());
                    } catch (NumberFormatException e) {
                        ToastUtil.showToast(context, context.getString(R.string.toast_banner_params_error));
                        return;
                    }
                    IntentUtil.startVideoClientActivity(liveId, -1,
                            false, bean.getData().getLiveScreenType());
                } else {
                    ToastUtil.showToast(context, context.getString(R.string.toast_banner_params_error));
                }
            } else if (BannerType.STR_LIVE_ROOM_DETAIL.equals(bean.getOPERATION())) {
                //TODO 查看直播房间详情
                if (bean.getData() != null && !StringUtil.isEmpty(bean.getData().getId())) {
                    long roomId = -1;
                    try {
                        roomId = Long.parseLong(bean.getData().getId());
                    } catch (NumberFormatException e) {
                        ToastUtil.showToast(context, context.getString(R.string.toast_banner_params_error));
                        return;
                    }
                    IntentUtil.startVideoClientActivity(-1, -1,
                            true, bean.getData().getLiveScreenType());
                } else {
                    ToastUtil.showToast(context, context.getString(R.string.toast_banner_params_error));
                }
            } else if (SHOPCART_LIST.equals(bean.getOPERATION())) {
                //TODO 购物车列表
                if (instance.userService.isLogin()) {
//                    NavUtils.gotoIntegralActivity(context, 0);
                    String url = SPUtils.getSHOPCART_LIST(context);
                    NavUtils.startWebview((Activity) context, "", url, 0);
                } else {
                    NavUtils.gotoLoginActivity(context, bean);
                }
            } else if (BannerType.TALENT_PUBLISH_SERVICE.equals(bean.getOPERATION())) {
                // 咨询发布服务
                NavUtils.gotoReleaseServiceActivity(context, -1, -1);
            } else if (BannerType.TALENT_SERVICE_LIST.equals(bean.getOPERATION())) {
                NavUtils.gotoManageServiceInfoActivity(context);
            } else if (BannerType.OPEN_LIVE.equals(bean.getOPERATION())) {
                //TODO 发布动态
                IntentUtil.startPublishActivity(context);
            } else if (BannerType.POINT_ORDER_DETAIL.equals(bean.getOPERATION())) {
                long order_id = bean.getData().getOrderId();
//                String order_type = bean.getData().getOrderType();
//                NavUtils.gotoOrderDetailsActivity(context, "POINT", order_id);
                if (instance.userService.isLogin()) {
//                    NavUtils.gotoIntegralActivity(context, 0);
                    String url = SPUtils.getPOINT_ORDER_DETAIL(context);
                    NavUtils.startWebview((Activity) context, "", url + order_id, 0);
                } else {
                    NavUtils.gotoLoginActivity(context, bean);
                }
            } else if (BannerType.CONSULT_ORDER_DETAIL.equals(bean.getOPERATION())) {
                long order_id = bean.getData().getOrderId();
                String order_type = bean.getData().getOrderType();
                NavUtils.gotoExpertOrderDetailActivity(context, -1, order_id);
            } else if (BannerType.TALENT_SALE_LIST.equals(bean.getOPERATION())) {
                NavUtils.gotoExpertOrderListActivity(context, -1);
            } else if (BannerType.MINE_PROFILE.equals(bean.getOPERATION())) {
                // TODO: 2018/6/1 个人资料页
                NavUtils.gotoUserInfoEditActivity(context);
            } else if (BannerType.PUBLISH_DYNAMIC.equals(bean.getOPERATION())) {
                // todo 动态发布页
                NavUtils.gotoAddLiveAcitivty((Activity) context);
            } else if (BannerType.SHARE_PEDOMETER.equals(bean.getOPERATION())) {
                // TODO 点击直接唤起分享足下生金
                NavUtils.gotoPedometerActivity(context, true);
            } else if (BannerType.SHARE_USER_QRCODE_TO_WX.equals(bean.getOPERATION())) {
                // TODO 点击直接唤起转发个人二维码的分享到朋友圈的页面
                NavUtils.gotoMineQrActivity(context, true);
            } else if (BannerType.JPEDOMETER_HOME.equals(bean.getOPERATION())) {
                // TODO: 2018/6/1 步步吸金首页
                NavUtils.gotoPedometerActivity(context);
            } else if (VERSION_UPGRADE.equals(bean.getOPERATION())) {
                // TODO: 2018/6/1 跳转应用市场升级app
                NavUtils.gotoMarketSupport(context);
            } else if (MASTER_CIRCLE_DETAIL.equals(bean.getOPERATION())) {
                if (bean.getData() != null && !StringUtil.isEmpty(bean.getData().getId())) {
                    long id = -1;
                    try {
                        id = Long.parseLong(bean.getData().getId());
                    } catch (NumberFormatException e) {
                        ToastUtil.showToast(context, context.getString(R.string.toast_banner_params_error));
                        return;
                    }
                    YhyRouter.getInstance().startCircleDetailActivity(context, id, false);
                } else {
                    ToastUtil.showToast(context, context.getString(R.string.toast_banner_params_error));
                }
            } else if (OPEN_ARTICLE_DETAIL.equals(bean.getOPERATION())) {
                // TODO: 2018/6/1 文章详情
                if (bean.getData() != null && bean.getData().getUgcId() > 0) {
                    String url = SPUtils.getURL_QUANZI_ARTICLE(context);
                    if (TextUtils.isEmpty(url))
                        return;
                    NavUtils.startWebview((Activity) context, "", url + bean.getData().getUgcId(), 0);
                } else {
                    ToastUtil.showToast(context, context.getString(R.string.toast_banner_params_error));
                }
            } else if (OPEN_SHORT_MOVIE.equals(bean.getOPERATION())) {
                // TODO: 2018/6/1 短视频详情
                if (bean.getData() != null && bean.getData().getUgcId() > 0) {
                    String url = SPUtils.getURL_SHORT_VIDEO(context);
                    if (TextUtils.isEmpty(url))
                        return;
                    NavUtils.startWebview((Activity) context, "", url + bean.getData().getUgcId(), 0);
                } else {
                    ToastUtil.showToast(context, context.getString(R.string.toast_banner_params_error));
                }
            } else {
                ToastUtil.showToast(context, context.getString(R.string.label_toast_version_low));
            }
        }
    }


    /**
     * @Description 是否为客户端可接收的scheme
     * @param url
     * @return
     */
    public static final String QUANYAN_SCHEME = "yhyapp://";
    public static final String QUANYAN_SCHEME_INTENT_FILTER = "quanyan";
    public static final int MSG_SCHEME_URL = 0x9001;

    public static boolean parseUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        return url.startsWith(QUANYAN_SCHEME);
    }

    /**
     * 获取提示框通知点击后的跳转intent
     *
     * @param ntfOperationCode
     * @param ntfOperationVaule
     */
    public static Intent getIntent(String ntfOperationCode, String ntfOperationVaule, Context context, boolean isLogin) {
        try {

            if (PACKAGE_TOUR.equals(ntfOperationCode)) {
                //跟团游
                Intent intent = new Intent(context, LineActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(SPUtils.EXTRA_TYPE, ItemType.TOUR_LINE);
                bundle.putLong(SPUtils.EXTRA_ID, -1);
                bundle.putString(SPUtils.EXTRA_TITLE, context.getString(R.string.label_title_package_tour));
                intent.putExtras(bundle);
                return intent;
            } else if (CITY_ACTIVITY_DETAIL.equals(ntfOperationCode)) {
                Intent intent = new Intent(context, CommodityDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong(SPUtils.EXTRA_ID, Long.parseLong(ntfOperationVaule));
                bundle.putString(SPUtils.EXTRA_TYPE, ItemType.CITY_ACTIVITY);
                intent.putExtras(bundle);
                return intent;
            } else if (AROUND_FUN.equals(ntfOperationCode)) {
                //周边游
                Intent intent = new Intent(context, LineActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(SPUtils.EXTRA_TYPE, ItemType.ARROUND_FUN);
                bundle.putLong(SPUtils.EXTRA_ID, -1);
                bundle.putString(SPUtils.EXTRA_TITLE, context.getString(R.string.label_title_arround_fun));
                intent.putExtras(bundle);
                return intent;
            } else if (FREE_TOUR.equals(ntfOperationCode)) {
                //自由行
                Intent intent = new Intent(context, LineActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(SPUtils.EXTRA_TYPE, ItemType.FREE_LINE);
                bundle.putLong(SPUtils.EXTRA_ID, -1);
                bundle.putString(SPUtils.EXTRA_TITLE, context.getString(R.string.label_title_free_walk));
                intent.putExtras(bundle);
                return intent;
            } else if (CITY_ACTIVITY.equals(ntfOperationCode)) {
                //同城首页
                Intent intent = new Intent(context, LineActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(SPUtils.EXTRA_TYPE, ItemType.CITY_ACTIVITY);
                bundle.putLong(SPUtils.EXTRA_ID, -1);
                bundle.putString(SPUtils.EXTRA_TITLE, context.getString(R.string.label_title_city_activity));
                intent.putExtras(bundle);
                return intent;
            } else if (QUANYAN_FOOD.equals(ntfOperationCode)) {
                //美食首页
                return new Intent(context, EatGreatActivity.class);
            } else if (HOME_INTEGRAL_MALL.equals(ntfOperationCode)) {
                //积分商城首页
//                return new Intent(context, IntegralmallHomeActivity.class);旧版积分商城
                String integralHomeUrl = SPUtils.getIntegralHomeUrl(context);
                if (!StringUtil.isEmpty(integralHomeUrl)) {
                    Intent intent = new Intent(context, WebViewActivity.class);
                    WebParams wp = new WebParams();
                    wp.setIsSetTitle(false);
                    wp.setUrl(integralHomeUrl);
                    intent.putExtra(SPUtils.EXTRA_DATA, wp);
                    return intent;
                } else {
                    return null;
                }
            } else if (HOME_CHECKIN.equals(ntfOperationCode)) {
                //签到首页
                if (!isLogin) {
                    //未登录返回空不进行跳转
                    return null;
                } else {
                    String checkInUrl = SPUtils.getCheckInUrl(context);
                    if (StringUtil.isEmpty(checkInUrl)) {
                        return null;
                    } else {
                        Intent intent = new Intent(context, WebViewActivity.class);
                        WebParams wp = new WebParams();
                        wp.setIsSetTitle(false);
                        wp.setUrl(checkInUrl);
                        intent.putExtra(SPUtils.EXTRA_DATA, wp);
                        return intent;
                    }
                }
            } else if (HOME_PEDOMETER.equals(ntfOperationCode)) {
                //步步前进首页
                return new Intent(context, PedoActivity.class);
            } else if (VIEW_HOME_MASTER_DYNAMIC.equals(ntfOperationCode)) {
                //达人圈热门
                Intent intent = new Intent(context, HomeMainTabActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(SPUtils.EXTRA_NEWINTENT_TYPE, 1);
                intent.putExtra(SPUtils.EXTRA_GONA_DISCOVER_TYPE, true);
                return intent;
            } else if (MASTER_CONSULT_SERVICE_LIST.equals(ntfOperationCode)) {
                //咨询全部列表页
                return new Intent(context, MasterAdviceListActivity.class);
            } else if (HOME_CONSULTING_LIST.equals(ntfOperationCode)) {
                //咨询达人首页
                return new Intent(context, MasterConsultHomeActivity.class);
            } else if (PACKAGE_TOUR_DETAIL.equals(ntfOperationCode)) {
                //线路商品详情（跟团）
                Intent intent = new Intent(context, CommodityDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong(SPUtils.EXTRA_ID, Long.parseLong(ntfOperationVaule));
                bundle.putString(SPUtils.EXTRA_TYPE, ItemType.TOUR_LINE);
                intent.putExtras(bundle);
                return intent;
            } else if (FREE_TOUR_DETAIL.equals(ntfOperationCode)) {
                //线路商品详情(自由行)
                Intent intent = new Intent(context, CommodityDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong(SPUtils.EXTRA_ID, Long.parseLong(ntfOperationVaule));
                bundle.putString(SPUtils.EXTRA_TYPE, ItemType.FREE_LINE);
                intent.putExtras(bundle);
                return intent;
            } else if (AROUND_FUN_DETAIL.equals(ntfOperationCode)) {
                //周边玩乐详情页
                Intent intent = new Intent(context, CommodityDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong(SPUtils.EXTRA_ID, Long.parseLong(ntfOperationVaule));
                bundle.putString(SPUtils.EXTRA_TYPE, ItemType.TOUR_LINE);
                intent.putExtras(bundle);
            } else if (INTEGRAL_MALL_DETAIL.equals(ntfOperationCode)) {
                //积分商城商品详情
                Intent intent = new Intent(context, CommodityDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong(SPUtils.EXTRA_ID, Long.parseLong(ntfOperationVaule));
                bundle.putString(SPUtils.EXTRA_TYPE, ItemType.POINT_MALL);
                intent.putExtras(bundle);
                return intent;
            } else if (MASTER_CIRCLE_DETAIL.equals(ntfOperationCode)) {
                //达人圈帖子详情
                Intent intent = new Intent(context, LiveDetailActivity.class);
                intent.putExtra(SPUtils.EXTRA_ID, Long.parseLong(ntfOperationVaule));
                intent.putExtra(SPUtils.EXTRA_TYPE_COMMENT, ValueConstants.TYPE_COMMENT_LIVESUP);
                intent.putExtra(SPUtils.EXTRA_TYPE_PRAISE, ValueConstants.TYPE_COMMENT_LIVESUP);
                return intent;
            } else if (VIEW_TOPIC_DETAIL.equals(ntfOperationCode)) {
                Intent intent = new Intent(context, TopicDetailsActivity.class);
                intent.putExtra(SPUtils.EXTRA_DATA, ntfOperationVaule);
                return intent;
            } else if (VIEW_TALENT_STORY.equals(ntfOperationCode)) {
                //达人故事
                Intent intent = new Intent(context, WebViewActivity.class);
                WebParams wp = new WebParams();
                String url = SPUtils.getString(SPUtils.TYPE_DEFAULT, context, SysConfigType.URL_TALENT_STORY);
                wp.setUrl(url + ntfOperationVaule);
                wp.setShowTitle(false);
                intent.putExtra(SPUtils.EXTRA_DATA, wp);
                return intent;
            } else if (H5.equals(ntfOperationCode)) {
                Intent intent = new Intent(context, WebViewActivity.class);
                WebParams wp = new WebParams();
                wp.setUrl(ntfOperationVaule);
                wp.setShowTitle(false);
                intent.putExtra(SPUtils.EXTRA_DATA, wp);
                return intent;
            } else if (TRAVEL_INFORMATION.equals(ntfOperationCode)) {
                //旅游那些事
                Intent intent = new Intent(context, WebViewActivity.class);
                WebParams wp = new WebParams();
                String url = SPUtils.getString(SPUtils.TYPE_DEFAULT, context, SysConfigType.URL_TALENT_STORY);
                wp.setUrl(url + ntfOperationVaule);
                wp.setShowTitle(false);
                intent.putExtra(SPUtils.EXTRA_DATA, wp);
                return intent;
            } else if (VIEW_CONSULTING_SERVICE_DETAIL.equals(ntfOperationCode)) {
                //达人咨询商品详情
                Intent intent = new Intent(context, CommodityDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong(SPUtils.EXTRA_ID, Long.parseLong(ntfOperationVaule));
                bundle.putString(SPUtils.EXTRA_TYPE, ItemType.CONSULT);
                intent.putExtras(bundle);
                return intent;
            } else if (QUANYAN_FOOD_DETAIL.equals(ntfOperationCode)) {
                //美食店铺详情
                Intent intent = new Intent(context, EatGreatDetailActivity.class);
                intent.putExtra(SPUtils.EXTRA_ID, Long.parseLong(ntfOperationVaule));
                return intent;
            } else if (APP_INDEX.equals(ntfOperationCode)) {
                //app首页
                return new Intent(context, HomeMainTabActivity.class);
            } else if (INTEGRAL_MISSION.equals(ntfOperationCode)) {
                //积分任务
                if (isLogin) {
                    Intent intent = new Intent(context, IntegralActivity.class);
                    intent.putExtra(OrderController.PARAMS_ORDER_CHECK_TYPE, 0);
                    return intent;
                } else {
                    return null;
                }
            } else if (INTEGRAL_DETAIL.equals(ntfOperationCode)) {
                //积分明细
                if (isLogin) {
                    Intent intent = new Intent(context, IntegralActivity.class);
                    intent.putExtra(OrderController.PARAMS_ORDER_CHECK_TYPE, 1);
                    return intent;
                } else {
                    return null;
                }
            } else if (TOPIC_HOME.equals(ntfOperationCode)) {
                //话题首页
                Intent intent = new Intent(context, HomeMainTabActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(SPUtils.EXTRA_NEWINTENT_TYPE, 2);
                intent.putExtra(SPUtils.EXTRA_GONA_DISCOVER_TYPE, true);
                return intent;
            } else if (TRAVEL_HOME.equals(ntfOperationCode)) {
                //游记首页
                Intent intent = new Intent(context, HomeMainTabActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(SPUtils.EXTRA_NEWINTENT_TYPE, 2);
                intent.putExtra(SPUtils.EXTRA_GONA_DISCOVER_TYPE, true);
                return intent;
            } else if (LIVE_LIST_BY_CITY_CODE.equals(ntfOperationCode)) {
                //视频直播首页
                Intent intent = new Intent(context, LiveListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(IntentUtil.BUNDLE_TITLE, "全部");
                bundle.putLong(IntentUtil.BUNDLE_CITYCODE, -1);
                bundle.putBoolean(IntentUtil.BUNDLE_IS_NEED_BANNER, false);
                intent.putExtras(bundle);
                //return intent;
                return null;
            } else if (LIVE_ROOM_DETAIL.equals(ntfOperationCode)) {
                //直播详情
                Intent intent = new Intent(context, HorizontalVideoClientActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong(IntentUtil.BUNDLE_LIVEID, Long.parseLong(ntfOperationVaule));
                bundle.putBoolean(IntentUtil.BUNDLE_IS_LIVE, true);
                intent.putExtras(bundle);
                return intent;
            } else if (LIVE_PLAYBACK_DETAIL.equals(ntfOperationCode)) {
                //回放详情
                Intent intent = new Intent(context, HorizontalReplayActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong(IntentUtil.BUNDLE_LIVEID, Long.parseLong(ntfOperationVaule));
                bundle.putBoolean(IntentUtil.BUNDLE_IS_LIVE, false);
                intent.putExtras(bundle);
                return intent;
            } else if (TOUR_GUIDE_PACKAGE.equals(ntfOperationCode)) {
                //导览包装业
                return new Intent(context, GuideActivity.class);
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    //APP首页
    public static final String APP_INDEX = "APP_INDEX";
    //积分任务
    public static final String INTEGRAL_MISSION = "INTEGRAL_MISSION";
    //积分明细
    public static final String INTEGRAL_DETAIL = "INTEGRAL_DETAIL";
    //话题首页
    public static final String TOPIC_HOME = "TOPIC_HOME";
    //游记首页
    public static final String TRAVEL_HOME = "TRAVEL_HOME";
    //视频直播首页
    public static final String LIVE_LIST_BY_CITY_CODE = "LIVE_LIST_BY_CITY_CODE";
    //游记详情页
    public static final String TRAVEL_DETAIL = "TRAVEL_DETAIL";
    //直播详情
    public static final String LIVE_ROOM_DETAIL = "LIVE_ROOM_DETAIL";
    //回放详情
    public static final String LIVE_PLAYBACK_DETAIL = "LIVE_PLAYBACK_DETAIL";
    //导览包装业
    public static final String TOUR_GUIDE_PACKAGE = "TOUR_GUIDE_PACKAGE";
    //H5
    public static final String H5 = "H5";

}

