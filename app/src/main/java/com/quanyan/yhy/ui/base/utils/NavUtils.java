package com.quanyan.yhy.ui.base.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;
import com.harwkin.nb.camera.PageBigImageActivity;
import com.mogujie.tt.protobuf.helper.EntityChangeEngine;
import com.mogujie.tt.ui.activity.ChatAcitivity;
import com.mogujie.tt.ui.activity.MessageActivity;
import com.mogujie.tt.ui.activity.MessageNotificationSettingActivity;
import com.mogujie.tt.ui.activity.NotificationListActivity;
import com.newyhy.activity.HomeMainTabActivity;
import com.newyhy.activity.NewTopicDetailActivity;
import com.quanyan.base.util.HarwkinLogUtil;
import com.quanyan.base.util.StringUtil;
import com.quanyan.pedometer.PedoActivity;
import com.quanyan.pedometer.ShareActivity;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.AnalyDataValue;
import com.quanyan.yhy.common.ItemType;
import com.quanyan.yhy.common.SysConfigType;
import com.quanyan.yhy.pay.PayActivity;
import com.quanyan.yhy.ui.BombBoxActivity;

import com.quanyan.yhy.ui.KickoutAcitivity;
import com.quanyan.yhy.ui.ShareTableActivity;
import com.quanyan.yhy.ui.base.views.MyPinchZoomImageView;
import com.quanyan.yhy.ui.comment.CommentFragmentActivity;
import com.quanyan.yhy.ui.comment.GoodsCommentListActivity;
import com.quanyan.yhy.ui.comment.WriteCommentActivity;
import com.quanyan.yhy.ui.common.ComplaintListActivity;
import com.quanyan.yhy.ui.common.FeedbackActivity;
import com.quanyan.yhy.ui.common.LogisticsActivity;
import com.quanyan.yhy.ui.common.WebViewActivity;
import com.quanyan.yhy.ui.common.address.activity.AddOrUpdateAddressActivity;
import com.quanyan.yhy.ui.common.address.activity.AddressListActivity;
import com.quanyan.yhy.ui.common.address.activity.AreaSelectActivity;
import com.quanyan.yhy.ui.common.calendar.CalendarSelectActivity;
import com.quanyan.yhy.ui.common.city.CitySearchSelectActivity;
import com.quanyan.yhy.ui.common.city.CitySelectActivity;
import com.quanyan.yhy.ui.common.city.DestinationSelectActivity;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.common.person.activity.AddOrUpdatePersonActivity;
import com.quanyan.yhy.ui.common.person.activity.AddOrUpdateVisitorActivity;
import com.quanyan.yhy.ui.common.tourist.AddOrUpdateCodeActivity;
import com.quanyan.yhy.ui.common.tourist.AddOrUpdateMimeTouristActivity;
import com.quanyan.yhy.ui.common.tourist.AddOrUpdateOrderTouristActivity;
import com.quanyan.yhy.ui.common.tourist.CommonUseTouristActivity;
import com.quanyan.yhy.ui.consult.MasterConsultActivity;
import com.quanyan.yhy.ui.consult.QuickConsultActivity;
import com.quanyan.yhy.ui.coupon.CouponActivity;
import com.quanyan.yhy.ui.discovery.AddLiveAcitivty;
import com.quanyan.yhy.ui.discovery.AddLocationSeach;
import com.quanyan.yhy.ui.discovery.AddTopicSearch;
import com.quanyan.yhy.ui.discovery.BlackSettingActivity;
import com.quanyan.yhy.ui.discovery.LiveDetailActivity;
import com.quanyan.yhy.ui.discovery.VideoPlayActivity;
import com.quanyan.yhy.ui.guide.GuideActivity;
import com.quanyan.yhy.ui.home.GuGeMapLocationActivity;
import com.quanyan.yhy.ui.integralmall.activity.MyOrderListActivity;
import com.quanyan.yhy.ui.line.CommodityDetailActivity;
import com.quanyan.yhy.ui.line.LineActivity;
import com.quanyan.yhy.ui.line.LineSearchResultActivity;
import com.quanyan.yhy.ui.lineabroad.DestinationAbroadActivity;
import com.quanyan.yhy.ui.login.ChangePasswordActivity;
import com.quanyan.yhy.ui.login.LoginActivity;
import com.quanyan.yhy.ui.login.RegisterFindPasswrodActivity;
import com.quanyan.yhy.ui.login.SettingFindPasswordActivity;
import com.quanyan.yhy.ui.master.activity.AttentionListActivity;
import com.quanyan.yhy.ui.master.activity.MasterAdviceListActivity;
import com.quanyan.yhy.ui.master.activity.MasterConsultHomeActivity;
import com.quanyan.yhy.ui.master.activity.MasterHomepageActivity;
import com.quanyan.yhy.ui.master.activity.MasterListActivity;
import com.quanyan.yhy.ui.master.activity.MyDynamicActivity;
import com.quanyan.yhy.ui.master.activity.SearchActivity;
import com.quanyan.yhy.ui.mine.activity.ServiceCenterActivity;
import com.quanyan.yhy.ui.mine.activity.UserInfoUpdateActivity;
import com.quanyan.yhy.ui.nineclub.BuyMustListActivity;
import com.quanyan.yhy.ui.nineclub.EatGreatActivity;
import com.quanyan.yhy.ui.nineclub.EatGreatDetailActivity;
import com.quanyan.yhy.ui.nineclub.NineClubActivity;
import com.quanyan.yhy.ui.nineclub.NineClubDetailListActivity;
import com.quanyan.yhy.ui.order.LineOrderActivity;
import com.quanyan.yhy.ui.order.OrderConfigActivity;
import com.quanyan.yhy.ui.order.OrderCouponActivity;
import com.quanyan.yhy.ui.order.PointOrderActivity;
import com.quanyan.yhy.ui.personal.MineQrActivity;
import com.quanyan.yhy.ui.servicerelease.AddServiceDetailActivity;
import com.quanyan.yhy.ui.servicerelease.ExpertOrderDetailActivity;
import com.quanyan.yhy.ui.servicerelease.ExpertOrderListActivity;
import com.quanyan.yhy.ui.servicerelease.ManageServiceInfoAcitvity;
import com.quanyan.yhy.ui.servicerelease.OrderCommentNewActivity;
import com.quanyan.yhy.ui.servicerelease.PictureAndTextActivity;
import com.quanyan.yhy.ui.servicerelease.ReleaseDestinationActivity;
import com.quanyan.yhy.ui.servicerelease.ReleaseServiceActivity;
import com.quanyan.yhy.ui.shop.ShopHomePageActivity;
import com.quanyan.yhy.ui.shop.ShopInformationActivity;
import com.quanyan.yhy.ui.shop.ShopLicenseActivity;
import com.quanyan.yhy.ui.shop.ShopSimpleIntroduceActivity;
import com.quanyan.yhy.ui.signed.activity.TaskDescriptionActivity;
import com.quanyan.yhy.ui.spcart.SPCartActivity;
import com.quanyan.yhy.ui.spcart.SPCartCouponActivity;
import com.quanyan.yhy.ui.spcart.SPCartOrderActivity;
import com.quanyan.yhy.ui.tab.homepage.order.MyBaseOrderDetailsActivity;
import com.quanyan.yhy.ui.views.calendarpicker.CalendarPickType;
import com.quanyan.yhy.ui.wallet.activity.BindCardActivity;
import com.quanyan.yhy.ui.wallet.activity.BindCardInforActivity;
import com.quanyan.yhy.ui.wallet.activity.BindCradVCodeActivity;
import com.quanyan.yhy.ui.wallet.activity.DetailAccountActivity;
import com.quanyan.yhy.ui.wallet.activity.ForgetPasBindCardActivity;
import com.quanyan.yhy.ui.wallet.activity.ForgetPasSelectCardActivity;
import com.quanyan.yhy.ui.wallet.activity.IDAuthenticActivity;
import com.quanyan.yhy.ui.wallet.activity.IDCardUploadActivity;
import com.quanyan.yhy.ui.wallet.activity.PayPassWordManagerActivity;
import com.quanyan.yhy.ui.wallet.activity.PrepaidOutAndInListActivity;
import com.quanyan.yhy.ui.wallet.activity.RealNameAuthActivity;
import com.quanyan.yhy.ui.wallet.activity.RechargeActivity;
import com.quanyan.yhy.ui.wallet.activity.SelectCardActivity;
import com.quanyan.yhy.ui.wallet.activity.SettingPayPassActivity;
import com.quanyan.yhy.ui.wallet.activity.UpdatePassWordActivity;
import com.quanyan.yhy.ui.wallet.activity.VerifyPassWordActivity;
import com.quanyan.yhy.ui.wallet.activity.WalletActivity;
import com.quanyan.yhy.ui.wallet.activity.WithDrawActivity;
import com.quanyan.yhy.ui.wallet.activity.WithDrawDetailsActivity;

import com.quanyan.yhy.util.MobileUtil;
import com.videolibrary.utils.IntentUtil;
import com.yhy.common.beans.album.MediaItem;
import com.yhy.common.beans.calender.PickSku;
import com.yhy.common.beans.city.bean.ListCityBean;
import com.yhy.common.beans.im.entity.ProductCardMessage;
import com.yhy.common.beans.net.model.NativeBean;
import com.yhy.common.beans.net.model.ProductCardModel;
import com.yhy.common.beans.net.model.RCShowcase;
import com.yhy.common.beans.net.model.ShareBean;
import com.yhy.common.beans.net.model.VideoInfo;
import com.yhy.common.beans.net.model.common.PictureTextItemInfo;
import com.yhy.common.beans.net.model.common.address.Address;
import com.yhy.common.beans.net.model.common.address.MyAddressContentInfo;
import com.yhy.common.beans.net.model.common.person.UserContact;
import com.yhy.common.beans.net.model.common.person.VisitorInfoList;
import com.yhy.common.beans.net.model.discover.UgcInfoResult;
import com.yhy.common.beans.net.model.master.Merchant;
import com.yhy.common.beans.net.model.param.WebParams;
import com.yhy.common.beans.net.model.paycore.BankCard;
import com.yhy.common.beans.net.model.paycore.Bill;
import com.yhy.common.beans.net.model.rc.RCExtendFieldInfo;
import com.yhy.common.beans.net.model.shop.MerchantItem;
import com.yhy.common.beans.net.model.tm.CreateOrderContextParamForPointMall;
import com.yhy.common.beans.net.model.tm.ItemProperty;
import com.yhy.common.beans.net.model.tm.TmCreateOrderResultTO;
import com.yhy.common.beans.net.model.user.Certificate;
import com.yhy.common.beans.net.model.user.Destination;
import com.yhy.common.beans.net.model.user.NativeDataBean;
import com.yhy.common.beans.user.User;
import com.yhy.common.constants.DBConstant;
import com.yhy.common.constants.IntentConstant;
import com.yhy.common.constants.IntentConstants;
import com.yhy.common.types.AppDebug;
import com.yhy.common.types.BannerType;
import com.yhy.common.utils.SPUtils;
import com.yhy.location.LocationManager;
import com.yhy.router.YhyRouter;
import com.yhy.router.types.MainActivityFromType;
import com.yhy.service.IUserService;
import com.yixia.camera.model.MediaObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yhy.router.RouterPath.ACTIVITY_CAPTURE_QRCODE;

/**
 * Created with Android Studio.
 * Title:NavUtils
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:和平必胜、正义必胜、人民必胜
 * Author:炎黄子孙
 * Date:15/10/23
 * Time:上午8:53
 * Version 1.0
 */
public class NavUtils {

    private static NavUtils instance = new NavUtils();

    @Autowired
    IUserService userService;

    private NavUtils(){
        YhyRouter.getInstance().inject(this);
    }
    /**
     * 跳转到引导页
     *
     * @param context
     */
    public static void gotoGuideActivty(Context context) {
        context.startActivity(new Intent(context, GuideActivity.class));
    }

    /**
     * 跳转视频播放页
     *
     * @param context
     */
    public static void gotoVideoPlayerctivty(Context context, VideoInfo videoInfo) {
        VideoPlayActivity.gotoVideoPlayerActivity(context, videoInfo);
    }


    /**
     * 跳转消息中心
     */
    public static void gotoMsgCenter(Context context) {
        if (!instance.userService.isLogin()) {
            gotoLoginActivity(context);
            return;
        }
        Intent intent = new Intent(context, ChatAcitivity.class);
        context.startActivity(intent);
    }


    /**
     * 跳转我的编辑
     */
    public static void gotoUserInfoEditActivity(Context context) {
        if (!instance.userService.isLogin()) {
            gotoLoginActivity(context);
            return;
        }
        UserInfoUpdateActivity.gotoUserInfoUpdatePage(context);
    }

    public static void gotoUserInfoEditActivity(Activity context, String source, int reqCode) {
        if (!instance.userService.isLogin()) {
            gotoLoginActivity(context);
            return;
        }
        UserInfoUpdateActivity.gotoUserInfoUpdatePage(context, source, reqCode);
    }


    /**
     * 单日选择 用于拥有skulist的时间页面
     *
     * @param activity
     * @param selectDate     已选择的时间
     * @param pickSkuHashMap sku数据
     * @param requestCode
     */
    public static void gotoSingleWithSkuSelectCalendar(Activity activity, Long startDate, Long endDate, Long selectDate, HashMap<String, PickSku> pickSkuHashMap, int requestCode, String sourceType) {
        CalendarSelectActivity.gotoSelectDate(activity, startDate, endDate, selectDate, requestCode, CalendarPickType.SINGLE_SKU, pickSkuHashMap, sourceType);
    }

    /**
     * @param activity
     * @param initStartDate 可选的起始时间 传0为当天
     * @param initEndDate   可选的结束时间 传0为当天后的九十天
     * @param startDate     选中的入店时间
     * @param endDate       选中的离店时间
     * @param requestCode
     */
    public static void gotoRangeSelectCalendar(Activity activity, long initStartDate, long initEndDate, Long startDate, Long endDate, int requestCode) {
        CalendarSelectActivity.gotoSelectRangeDate(activity, initStartDate, initEndDate, startDate, endDate, requestCode);
    }

    /**
     * 跳转到城市搜索框搜索
     *
     * @param activity
     * @param reqCode
     */
    public static void gotoSearchSelectCity(Activity activity, String type, ListCityBean listCityBean, int reqCode) {
        CitySearchSelectActivity.gotoSearchSelectCity(activity, type, listCityBean, reqCode);
    }

    /**
     * 跳转到出发地城市选择
     *
     * @param activity
     * @param reqCode
     */
    public static void gotoSelectCity(Activity activity, int reqCode) {
        CitySelectActivity.gotoSelectCity(activity, reqCode);
    }

    /**
     * 跳转到目的搜索城市
     *
     * @param activity
     * @param type
     * @param reqCode
     */
    public static void gotoDestinationSelectActivity(Activity activity, String type, String lineType, String title, String source, int reqCode) {
        DestinationAbroadActivity.gotoDestinationAbroadActivity(activity, type, lineType, title, source, reqCode);
    }

    /**
     * 跳转到老的目的地选择页
     *
     * @param activity
     * @param type
     * @param lineType
     * @param title
     * @param source
     * @param outType
     * @param reqCode
     */
    public static void gotoDestinationSelectActivity(Activity activity, String type, String lineType, String title, String source, String outType, int reqCode) {
        DestinationSelectActivity.gotoDestinationSelectActivity(activity, type, lineType, title, source, outType, reqCode);
    }

    /**
     * 跳转到写评价界面
     *
     * @param activity
     * @param orderId
     * @param userId
     * @param type     {@link com.quanyan.yhy.common.CommentType}
     * @param reqCode
     */
    public static void gotoWriteCommentAcitivty(Activity activity, long orderId, long userId, String type, int reqCode) {
        if (instance.userService.isLogin()) {
            WriteCommentActivity.gotoWriteCommentAcitivty(activity, orderId, userId, type, reqCode);
        } else {
            gotoLoginActivity(activity);
        }

    }

    /**
     * 跳转到评价列表界面
     *
     * @param context
     * @param id
     * @param type    {@link com.quanyan.yhy.common.CommentType}
     */
    public static void gotoCommentFragmentActivity(Context context, long id, String type) {
        CommentFragmentActivity.gotoCommentFragmentActivity(context, id, type);
    }

    /**
     * 跳转到添加游客
     *
     * @param context
     * @param userContact
     */
    public static void gotoAddOrUpdateVisitorActivity(Activity context, int reqCode, UserContact userContact) {
        AddOrUpdateVisitorActivity.gotoAddOrUpdateVisitorActivity(context, reqCode, userContact);
    }

    /**
     * 跳转到添加联系人
     *
     * @param context
     * @param userContact
     */
    public static void gotoAddOrUpdatePersonActivity(Activity context, int reqCode, UserContact userContact) {
        AddOrUpdatePersonActivity.gotoAddOrUpdatePersonActivity(context, reqCode, userContact);
    }

    /**
     * 跳转到地址列表
     *
     * @param context
     * @param reqCode
     */
    public static void gotoAddressListActivity(Activity context, int reqCode, String source) {
        if (instance.userService.isLogin()) {
            AddressListActivity.gotoAddressListActivity(context, reqCode, source);
        } else {
            gotoLoginActivity(context);
        }
    }

    /**
     * 跳转到添加地址
     *
     * @param context
     * @param reqCode
     * @param myAddress
     */
    public static void gotoAddOrUpdateAddressActivity(Activity context, int reqCode, MyAddressContentInfo myAddress) {
        AddOrUpdateAddressActivity.gotoAddOrUpdateAddressActivity(context, reqCode, myAddress);
    }

    /**
     * 跳转到登录页面
     *
     * @param context
     */
    public static void gotoLoginActivity(Context context) {
        if (context instanceof Activity) {
            gotoLoginActivity((Activity) context);
            return;
        }
        LoginActivity.gotoLoginActivity(context);
    }

    /**
     * @param context
     */
    public static void gotoLoginActivity(Context context, NativeBean bean) {
        if (context instanceof Activity) {
            gotoLoginActivity((Activity) context, bean);
            return;
        }
        LoginActivity.gotoLoginActivity(context, bean);
    }

    public static void gotoIDAuthenticActivity(Context context, boolean result, String userName, String idcard, String validDate) {
        Intent intent = new Intent(context, IDAuthenticActivity.class);
        intent.putExtra(SPUtils.EXTRA_AUTH_RESULT, result);
        intent.putExtra(SPUtils.EXTRA_USER_NAME, userName);
        intent.putExtra(SPUtils.EXTRA_IDCARD, idcard);
        intent.putExtra(SPUtils.EXTRA_VALIDDATE, validDate);
        context.startActivity(intent);
    }

    public static void gotoIDAuthenticActivity(Context context, boolean result) {
        Intent intent = new Intent(context, IDAuthenticActivity.class);
        intent.putExtra(SPUtils.EXTRA_AUTH_RESULT, result);
        context.startActivity(intent);
    }

    /**
     * 跳转到登录页面
     *
     * @param context
     */
    public static void gotoLoginActivity(Activity context, NativeBean bean) {
        YhyRouter.getInstance().startLoginActivity(context, bean, Intent.FLAG_ACTIVITY_CLEAR_TOP, IntentConstants.LOGIN_RESULT);
    }


    /**
     * 跳转到登录页面
     *
     * @param context
     */
    public static void gotoIdCardUploadActivity(Activity context, String userName) {
        Intent intent = new Intent(context, IDCardUploadActivity.class);
        intent.putExtra(SPUtils.KEY_USER_NAME, userName);
        context.startActivity(intent);
    }

    /**
     * 跳转到登录页面
     *
     * @param context
     */
    public static void gotoLoginActivity(Activity context) {
        YhyRouter.getInstance().startLoginActivity(context, null, -1, IntentConstants.LOGIN_RESULT);
    }

    /**
     * 跳转到注册页面
     *
     * @param context
     */
    public static void gotoFindPasswordActivity(Activity context) {
        RegisterFindPasswrodActivity.gotoFindPasswordActivity(context);
    }

    /**
     * 跳转到注册页面
     *
     * @param context
     */
    public static void gotoRegisterActivity(Activity context, int requestCode) {
        RegisterFindPasswrodActivity.gotoRegisterActivity(context, requestCode);
    }

    /**
     * 跳转到修改密码页面
     *
     * @param context
     */
    public static void goChangePasswordActivity(Context context, int requestCode) {
        ChangePasswordActivity.goChangePasswordActivity(context, requestCode);
    }

    /**
     * 跳转到个人设置中找回密码页面
     *
     * @param context
     */
    public static void goSettingFindPasswordActivity(Context context, int requestCode) {
        SettingFindPasswordActivity.goSettingFindPasswordActivity(context, requestCode);
    }

    /**
     * 跳转订单详情页
     *
     * @param context
     * @param orderType 订单类型 MyOrderListController。PARENT_TYPE
     * @param orderCode 订单号
     */
    public static void gotoOrderDetailsActivity(Context context, String orderType, long orderCode) {
        MyBaseOrderDetailsActivity.gotoOrderDetailsActivity(context, orderType, orderCode);
    }


    /**
     * 跳转到地区去选择
     *
     * @param context
     * @param address
     */
    public static void gotoAreaSelect(Activity context, Address address, int requestCode) {
        AreaSelectActivity.gotoAreaSelect(context, address, requestCode);
    }

    /**
     * 跳转发布直播
     *
     * @param context 上下文对象
     */
    public static void gotoAddLiveAcitivty(Activity context) {
        if (!instance.userService.isLogin()) {
            gotoLoginActivity(context);
            return;
        }
        AddLiveAcitivty.gotoAddLiveAcitivty(context, null);
    }

    public static void gotoAddLiveActivity(Activity context, String topic) {
        if (!instance.userService.isLogin()) {
            gotoLoginActivity(context);
            return;
        }
        AddLiveAcitivty.gotoAddLiveActivity(context, topic);
    }

    public static void gotoAddLiveActivity(Activity context, MediaItem video) {
        if (!instance.userService.isLogin()) {
            gotoLoginActivity(context);
            return;
        }
        AddLiveAcitivty.gotoAddLiveActivity(context, video);
    }

    public static void gotoAddLiveActivity(Context context) {
        if (!instance.userService.isLogin()) {
            gotoLoginActivity(context);
            return;
        }
        AddLiveAcitivty.gotoAddLiveActivity(context);
    }

    /**
     * 分享到达人圈(步步吸金过来)
     *
     * @param context 上下文对象
     */
    public static void gotoAddLiveAcitivty(Activity context, ShareBean shareBean, boolean isStep) {
        if (!instance.userService.isLogin()) {
            gotoLoginActivity(context);
            return;
        }
        AddLiveAcitivty.gotoAddLiveAcitivty(context, shareBean, isStep);
    }

    /**
     * 跳转发布直播
     *
     * @param context
     * @param type
     * @param data
     */
    public static void gotoAddLiveAcitivty(Activity context, int type, Intent data, ArrayList<MediaItem> pathList) {
        AddLiveAcitivty.gotoAddLiveAcitivty(context, type, data, pathList);
    }

    /**
     * 跳转发布直播
     *
     * @param context
     * @param type
     */
    public static void gotoAddLiveAcitivty(Activity context, int type, MediaObject mMediaObject) {
        AddLiveAcitivty.gotoAddLiveAcitivty(context, type, mMediaObject);
    }

    /**
     * 跳转到关注或粉丝列表
     *
     * @param context
     */
    private static void gotoAttentionListActivity(Context context, AttentionListActivity.AttenttionType attenttionType, long userId) {
        Intent intent = new Intent(context, AttentionListActivity.class);
        intent.putExtra(IntentConstant.EXTRA_ATTENTION_TYPE, attenttionType);
        intent.putExtra(IntentConstant.EXTRA_UID, userId);
        context.startActivity(intent);
    }

    /**
     * 我的粉丝
     */
    public static void gotoMyFansListActivity(Context context, long uid) {
        if (!instance.userService.isLogin()) {
            gotoLoginActivity(context);
            return;
        }
        gotoAttentionListActivity(context, AttentionListActivity.AttenttionType.MY_FANS, uid);
    }

    /**
     * 我的动态
     */
    public static void gotoMyDaynamicActivity(Context context) {
        if (!instance.userService.isLogin()) {
            gotoLoginActivity(context);
            return;
        }
        Intent intent = new Intent(context, MyDynamicActivity.class);
        context.startActivity(intent);
    }

    /**
     * 我的关注
     */
    public static void gotoMyAttentionListActivity(Context context, long uid) {
        if (!instance.userService.isLogin()) {
            gotoLoginActivity(context);
            return;
        }
        gotoAttentionListActivity(context, AttentionListActivity.AttenttionType.MY_ATTENTION, uid);
    }

    /**
     * TA的粉丝
     */
    public static void gotoOtherFansListActivity(Context context, long userId) {
        gotoAttentionListActivity(context, AttentionListActivity.AttenttionType.OTHER_FANS, userId);
    }

    /**
     * Ta的关注
     */
    public static void gotoOhterAttentionListActivity(Context context, long userId) {
        gotoAttentionListActivity(context, AttentionListActivity.AttenttionType.OTHER_ATTENTION, userId);
    }


    /**
     * 直播详情页面
     *
     * @param context
     * @param subjectInfo
     * @param typeComment 动态  or 直播
     * @param typePraise  动态  or 直播
     * @param b
     */
    public static void gotoLiveDetailActivity(Activity context, long id, @Nullable UgcInfoResult subjectInfo, String typeComment, String typePraise, boolean b) {
        LiveDetailActivity.gotoLiveDetailActivity(context, id, typeComment, typePraise, b);

//        YhyRouter.getInstance().startCircleDetailActivity(context, id, typeComment, typePraise, b);

    }

    /**
     * 套餐详情,商品详情
     *
     * @param context
     * @param type      只接受四种类型{@link ItemType#TOUR_LINE},{@link ItemType#FREE_LINE},
     *                  {@link ItemType#CITY_ACTIVITY},{@link ItemType#NORMAL}
     * @param productId
     */
    public static void gotoProductDetail(Context context, String type, long productId, String name) {
        if (!ItemType.MASTER_CONSULT_PRODUCTS.equals(type)) {
            Map<String, String> map = new HashMap<>();
            if (ItemType.FREE_LINE.equals(type) || ItemType.FREE_LINE_ABOARD.equals(type)) {
                map.put(AnalyDataValue.KEY_TYPE, AnalyDataValue.PRODUCT_DETAIL_FREE_WALK);
            } else if (ItemType.TOUR_LINE.equals(type) || ItemType.TOUR_LINE_ABOARD.equals(type)) {
                map.put(AnalyDataValue.KEY_TYPE, AnalyDataValue.PRODUCT_DETAIL_PACK_TOUR);
            } else if (ItemType.NORMAL.equals(type)) {
                map.put(AnalyDataValue.KEY_TYPE, AnalyDataValue.PRODUCT_DETAIL_MUST_BUY);
            } else if (ItemType.CITY_ACTIVITY.equals(type)) {
                map.put(AnalyDataValue.KEY_TYPE, AnalyDataValue.PRODUCT_DETAIL_CITY_ACTIVITY);
            } else if (ItemType.MASTER_PRODUCTS.equals(type)) {
                map.put(AnalyDataValue.KEY_TYPE, AnalyDataValue.PRODUCT_DETAIL_MASTER_PRODUCT);
            } else if (ItemType.POINT_MALL.equals(type)) {
                map.put(AnalyDataValue.KEY_TYPE, AnalyDataValue.PRODUCT_DETAIL_INTEGRAL);
            }
            map.put(AnalyDataValue.KEY_PID, productId + "");
            map.put(AnalyDataValue.KEY_PNAME, name);
            TCEventHelper.onEvent(context, AnalyDataValue.TC_ID_PRODUCT_SEE_DETAIL, map);
        }

        CommodityDetailActivity.gotoFreeWalkerAndPackageTourActivity(context, type, productId);
    }

    /**
     * 针对达人咨询服务的详情（传递服务是否发布的状态）
     *
     * @param context
     * @param type
     * @param productId
     * @param name
     * @param isPublish
     */
    public static void gotoProductDetail(Context context, String type, long productId, String name, boolean isPublish) {
        if (!ItemType.MASTER_CONSULT_PRODUCTS.equals(type)) {
            Map<String, String> map = new HashMap<>();
            if (ItemType.FREE_LINE.equals(type) || ItemType.FREE_LINE_ABOARD.equals(type)) {
                map.put(AnalyDataValue.KEY_TYPE, AnalyDataValue.PRODUCT_DETAIL_FREE_WALK);
            } else if (ItemType.TOUR_LINE.equals(type) || ItemType.TOUR_LINE_ABOARD.equals(type)) {
                map.put(AnalyDataValue.KEY_TYPE, AnalyDataValue.PRODUCT_DETAIL_PACK_TOUR);
            } else if (ItemType.NORMAL.equals(type)) {
                map.put(AnalyDataValue.KEY_TYPE, AnalyDataValue.PRODUCT_DETAIL_MUST_BUY);
            } else if (ItemType.CITY_ACTIVITY.equals(type)) {
                map.put(AnalyDataValue.KEY_TYPE, AnalyDataValue.PRODUCT_DETAIL_CITY_ACTIVITY);
            } else if (ItemType.MASTER_PRODUCTS.equals(type)) {
                map.put(AnalyDataValue.KEY_TYPE, AnalyDataValue.PRODUCT_DETAIL_MASTER_PRODUCT);
            } else if (ItemType.POINT_MALL.equals(type)) {
                map.put(AnalyDataValue.KEY_TYPE, AnalyDataValue.PRODUCT_DETAIL_INTEGRAL);
            }
            map.put(AnalyDataValue.KEY_PID, productId + "");
            map.put(AnalyDataValue.KEY_PNAME, name);
            TCEventHelper.onEvent(context, AnalyDataValue.TC_ID_PRODUCT_SEE_DETAIL, map);
        }

        CommodityDetailActivity.gotoProductionDetail(context, type, productId, isPublish);
    }

    /**
     * 查看图片大图
     */
    public static void gotoLookBigImage(Context context, ArrayList<String> pics, int position) {
        context.startActivity(PageBigImageActivity.getIntent(context, pics, position, false, false,
                MyPinchZoomImageView.Mode.NONE.ordinal(), false, true));
    }

    public static void gotoLookBigImage(Context context, ArrayList<String> pics, int position, boolean showGif) {
        context.startActivity(PageBigImageActivity.getIntent(context, pics, position, false, false,
                MyPinchZoomImageView.Mode.NONE.ordinal(), false, showGif));
    }

    /**
     * 查看图片大图
     */
    public static void gotoLookBigImage(Context context, ArrayList<String> pics, int position, boolean flag, boolean showGif) {
        context.startActivity(PageBigImageActivity.getIntent(context, pics, position, false, false,
                MyPinchZoomImageView.Mode.NONE.ordinal(), flag, showGif));
    }

    /**
     * @param context
     * @param bizOrderId id
     * @param price      商品金额
     *                   返回的SPUtils.EXTRA_DATA, payStatus为9000是支付成功
     */
    public static void gotoPayActivity(Context context, long[] bizOrderId, String price, int reqCode) {
        if (AppDebug.IS_MONKEY_TEST) {
            return;
        }
        ((Activity) context).startActivityForResult(PayActivity.getIntent(context, bizOrderId, price), reqCode);
        ((Activity) context).overridePendingTransition(R.anim.push_up_in1, R.anim.push_up_out1);
    }

    /**
     * 意见反馈
     *
     * @param context
     */
    public static void gotoFeedbackAcivity(Context context) {
        FeedbackActivity.gotoFeedbackActivity(context);
    }

    /**
     * 分发个人主页
     *
     * @param context
     */
    public static void gotoPersonalPage(Context context, long userId, String userName, long options) {
        if (userId < 0) {
            return;
        }
        if (SPUtils.isMerchantUser(options)) {
            ShopHomePageActivity.gotoShopHomePageActivity(context, userName == null ? "" : userName, userId);
        } else {
            MasterHomepageActivity.gotoMasterHomepage(context, userId);
        }
    }


    /**
     * 打开内部浏览器
     */
    public static void openBrowser(Context context, WebParams params) {
        WebViewActivity.openBrowser(context, params);
    }

    /**
     * 打开内部浏览器
     */
    public static void openBrowserByRequestCode(Activity context, WebParams params, int reqCode) {
        WebViewActivity.openBrowser(context, params, reqCode);
    }

    public static void startWebview(Activity activity, String title, String url, int requestCode) {
        WebParams params = new WebParams();
        params.setIsSetTitle(true);
        params.setUrl(url);
        params.setIsNeedSetResult(true);
        params.setTitle(title);
        NavUtils.openBrowserByRequestCode(activity, params, requestCode);
    }

    public static void startWebview(Activity activity, String url, long id) {
        WebParams params = new WebParams();
        params.setIsSetTitle(true);
        params.setUrl(url);
        params.setIsNeedSetResult(true);
        params.setArticle(true);
        params.setId(id);
        NavUtils.openBrowserByRequestCode(activity, params, -1);
    }
    /**
     * 查看大的地图
     *
     * @param context
     */
    public static void gotoViewBigMapView(Context context, String title, double lat, double lng, boolean type, String address) {
        GuGeMapLocationActivity.gotoViewBigMapView(context, title, lat, lng, type, address);
    }

    /**
     * 给程序员加油
     *
     * @param context
     */
    public static void gotoMarketSupport(Context context) {
        if (isSamsungPhone()) {
            Uri uri = Uri.parse("http://www.samsungapps.com/appquery/appDetail.as?appId=" + context.getPackageName());
            Intent goToMarket = new Intent();
            goToMarket.setClassName("com.sec.android.app.samsungapps", "com.sec.android.app.samsungapps.Main");
            goToMarket.setData(uri);
            try {
                context.startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }


    public static String getPhoneModel() {
        return Build.MANUFACTURER;
    }

    public static boolean isSamsungPhone() {
        if (getPhoneModel().contains("samsung")) {
            return true;
        }
        return false;
    }

    /**
     * 跳转到9club列表
     *
     * @param context
     */
    public static void gotoNineClubActivity(Context context) {
        NineClubActivity.gotoNineClubActivity(context);
    }

    /**
     * 跳转到9club单个专辑列表
     *
     * @param context
     * @param id
     * @param pageType
     * @param title
     */
    public static void gotoNineClubDetailListActivity(Context context, String id, String pageType, String title) {
        NineClubDetailListActivity.gotoNineClubDetailListActivity(context, id, pageType, title);
    }

    /**
     * 跳转到必吃列表
     *
     * @param context
     * @param type
     */
    public static void gotoEatGreatActivity(Context context, String type) {
        EatGreatActivity.gotoEatGreatActivity(context, type);
    }

    /**
     * 跳转到美食详情页
     *
     * @param context
     * @param eatId
     */
    public static void gotoEatGreatDetailActivity(Context context, long eatId) {
        EatGreatDetailActivity.gotoEatGreatDetailActivity(context, eatId);
    }

    /**
     * 跳转到必买列表
     *
     * @param context
     */
    public static void gotoBuyMustListActivity(Context context) {
        BuyMustListActivity.gotoBuyMustListActivity(context);
    }

    /**
     * 跳转到分享接口界面
     *
     * @param context
     * @param shareBean
     * @param type
     */
    public static void gotoShareTableActivity(Context context, ShareBean shareBean, String type) {
        ShareTableActivity.gotoShareTableActivity(context, shareBean, type);
    }

    public static void gotoShareTableActivity(Context context, ShareBean shareBean, boolean isOnlyImg) {
        ShareTableActivity.gotoShareTableActivity(context, shareBean, isOnlyImg);
    }

    /**
     * b步步吸金跳转过来
     *
     * @param context
     * @param shareBean
     * @param isOnlyImg
     * @param isStepShare
     */
    public static void gotoShareTableActivity(Context context, ShareBean shareBean, boolean isOnlyImg, boolean isStepShare) {
        ShareTableActivity.gotoShareTableActivity(context, shareBean, isOnlyImg, isStepShare);
    }

    /**
     * 跳转到商铺详情
     *
     * @param context
     * @param shopId
     */
    public static void gotoShopHomePageActivity(Context context, String title, long shopId) {
        ShopHomePageActivity.gotoShopHomePageActivity(context, title, shopId);
    }

    /**
     * 跳转到必买商铺详情
     *
     * @param context
     * @param title
     * @param shopId
     */
    public static void gotoMustBuyShopHomePageActivity(Context context, String title, long shopId) {
        ShopHomePageActivity.gotoMustBuyShopHomePageActivity(context, title, shopId);
    }

    /**
     * 达人主页跳转
     *
     * @param context          上下文对象
     * @param masterHomepageId 达人ID
     */
    public static void gotoMasterHomepage(Context context, long masterHomepageId) {
//        MasterHomepageActivity.gotoMasterHomepage(context, masterHomepageId);

//        Intent intent = new Intent(context, NewHomePagerActivity.class);
//        intent.putExtra(SPUtils.EXTRA_ID, masterHomepageId);
//        context.startActivity(intent);

        YhyRouter.getInstance().startHomePageActivity(context, masterHomepageId, false);
    }

    /**
     * 达人主页跳转,定位到服务
     *
     * @param context          上下文对象
     * @param masterHomepageId 达人ID
     */
    public static void gotoMasterHomepage(Context context, long masterHomepageId, boolean fromCousult) {
//        MasterHomepageActivity.gotoMasterHomepage(context, masterHomepageId, fromCousult);

//        Intent intent = new Intent(context, NewHomePagerActivity.class);
//        intent.putExtra(SPUtils.EXTRA_ID, masterHomepageId);
//        intent.putExtra("fromCousult", fromCousult);
//        context.startActivity(intent);
        YhyRouter.getInstance().startHomePageActivity(context, masterHomepageId, fromCousult);

    }

    /**
     * 跳转达人列表
     *
     * @param context
     */
    public static void gotoMasterListActivity(Context context, String title) {
        MasterListActivity.gotoMasterListActivity(context, title);
    }

    /**
     * 跳转达人列表
     *
     * @param context
     * @param tagId
     * @param title
     */
    public static void gotoMasterListActivity(Context context, String tagId, String title) {
        MasterListActivity.gotoMasterListActivity(context, tagId, title);
    }

    /**
     * 跳转达人搜索或者达人商品搜索
     *
     * @param context
     */
    public static void gotoSearchActivity(Activity context, String itemType, String source, String pageTitle, int reqCode) {
        SearchActivity.gotoSearchActivity(context, itemType, source, pageTitle, reqCode);
    }

    /**
     * 搜索
     *
     * @param context
     */
    public static void gotoSearchActivity(Context context, String itemType, String source, String title, String cityCode) {
        SearchActivity.gotoSearchActivity(context, itemType, source, title, cityCode);
    }

    /**
     * 资源位统一分发跳转页面
     *
     * @param context
     * @param showcase
     * @param position
     */
    public static void depatchAllJump(Context context, RCShowcase showcase, int position) {
        try {
            HarwkinLogUtil.info("context name : " + context.getClass().getName());
            NativeDataBean nativeDataBean = null;
            if (!StringUtil.isEmpty(showcase.operationContent)) {
                NativeBean nativeBean = NativeUtils.parseJson(showcase.operationContent);
                if (nativeBean != null && nativeBean.getData() != null) {
                    nativeDataBean = nativeBean.getData();
                }
            }

            if (BannerType.STR_H5.equals(showcase.operation)) {
                //TODO H5
                if (!StringUtil.isEmpty(showcase.operationContent.trim())) {
                    if (showcase.extendFieldInfos != null && showcase.extendFieldInfos.size() > 0) {
                        WebParams wp = new WebParams();
                        for (RCExtendFieldInfo item : showcase.extendFieldInfos) {
                            if ("isShowTitle".equals(item.key)) {
                                if (!StringUtil.isEmpty(item.value)) {
                                    wp.setShowTitle(!"true".equals(item.value.trim().toLowerCase()));
                                }
                                break;
                            }
                        }
                        if (showcase.operationContent.contains("recommendPlaceList")) {
                            wp.setUrl(showcase.operationContent.trim().replaceAll(":cityCode", LocationManager.getInstance().getStorage().getLast_cityCode()) + "/" + LocationManager.getInstance().getStorage().getLast_discCode());
                        } else {
                            wp.setUrl(showcase.operationContent.trim());
                        }
                        NavUtils.openBrowser(context, wp);
                    }
                }
            } else if (BannerType.STR_PACKAGE_TOUR_HOME_PAGE.equals(showcase.operation)) {
                //TODO 跟团游首页
                NavUtils.gotoLineActivity(context, -1, ItemType.TOUR_LINE, showcase.title);
            } else if (BannerType.STR_FREE_TOUR_HOME_PAGE.equals(showcase.operation)) {
                //TODO 自由行
                NavUtils.gotoLineActivity(context, -1, ItemType.FREE_LINE, showcase.title);
            } else if (BannerType.STR_CITY_ACTIVITY_HOME_PAGE.equals(showcase.operation)) {
                //TODO 同城活动
                NavUtils.gotoLineActivity(context, -1, ItemType.CITY_ACTIVITY, showcase.title);
            } else if (BannerType.STR_ARROUND_FUN_HOME_PAGE.equals(showcase.operation)) {
                //TODO 周边游
                NavUtils.gotoLineActivity(context, -1, ItemType.ARROUND_FUN, showcase.title);
            } else if (BannerType.STR_JIU_XIU_MASTER_HOME_PAGE.equals(showcase.operation)) {
                //TODO 达人所有列表
                gotoMasterListActivity(context, showcase.title);
            } else if (BannerType.STR_JIU_XIU_CLUB_HOME_PAGE.equals(showcase.operation)) {
                //TODO Club
                NavUtils.gotoNineClubActivity(context);
            } else if (BannerType.STR_AROUND_FUN_LIST.equals(showcase.operation)) {
                //TODO 周边玩乐专题列表
                if (!StringUtil.isEmpty(showcase.operationContent)) {
                    NavUtils.gotoNineClubDetailListActivity(context, showcase.operationContent, ItemType.ARROUND_FUN, showcase.title);
                } else {
                    ToastUtil.showToast(context, context.getString(R.string.toast_banner_params_error));
                }
            } else if (BannerType.STR_CITY_ACTIVITY_DETAIL.equals(showcase.operation)) {
                //TODO 同城活动详情
                if (!StringUtil.isEmpty(showcase.operationContent)) {
                    gotoProductDetail(context, ItemType.CITY_ACTIVITY, Long.parseLong(showcase.operationContent),
                            showcase.title);
                } else {
                    ToastUtil.showToast(context, context.getString(R.string.toast_banner_params_error));
                }
            } else if (BannerType.STR_CITY_ACTIVITY_LIST.equals(showcase.operation)) {
                //TODO 同城活动专题列表
                if (!StringUtil.isEmpty(showcase.operationContent)) {
                    NavUtils.gotoNineClubDetailListActivity(context, showcase.operationContent, ItemType.CITY_ACTIVITY, showcase.title);
                } else {
                    ToastUtil.showToast(context, context.getString(R.string.toast_banner_params_error));
                }
            } else if (BannerType.STR_FREE_TOUR_DETAIL.equals(showcase.operation)) {
                //TODO 自由行线路详情
                if (!StringUtil.isEmpty(showcase.operationContent)) {
                    gotoProductDetail(context, ItemType.FREE_LINE, Long.parseLong(showcase.operationContent),
                            showcase.title);
                } else {
                    ToastUtil.showToast(context, context.getString(R.string.toast_banner_params_error));
                }
            } else if (BannerType.STR_FREE_TOUR_ABOARD_DETAIL.equals(showcase.operation)) {
                //TODO 境外自由行线路详情
                if (!StringUtil.isEmpty(showcase.operationContent)) {
                    gotoProductDetail(context, ItemType.FREE_LINE_ABOARD, Long.parseLong(showcase.operationContent),
                            showcase.title);
                } else {
                    ToastUtil.showToast(context, context.getString(R.string.toast_banner_params_error));
                }
            } else if (BannerType.STR_FREE_TOUR_LIST.equals(showcase.operation)) {
                //TODO 自由行专辑列表
                if (!StringUtil.isEmpty(showcase.operationContent)) {
                    NavUtils.gotoNineClubDetailListActivity(context, showcase.operationContent, ItemType.FREE_LINE, showcase.title);
                } else {
                    ToastUtil.showToast(context, context.getString(R.string.toast_banner_params_error));
                }
            } else if (BannerType.STR_PACKAGE_TOUR_DETAIL.equals(showcase.operation)) {
                //TODO 跟团游详情
                if (!StringUtil.isEmpty(showcase.operationContent)) {
                    gotoProductDetail(context, ItemType.TOUR_LINE, Long.parseLong(showcase.operationContent),
                            showcase.title);
                } else {
                    ToastUtil.showToast(context, context.getString(R.string.toast_banner_params_error));
                }
            } else if (BannerType.STR_PACKAGE_TOUR_ABOARD_DETAIL.equals(showcase.operation)) {
                //TODO 境外跟团游详情
                if (!StringUtil.isEmpty(showcase.operationContent)) {
                    gotoProductDetail(context, ItemType.TOUR_LINE_ABOARD, Long.parseLong(showcase.operationContent),
                            showcase.title);
                } else {
                    ToastUtil.showToast(context, context.getString(R.string.toast_banner_params_error));
                }
            } else if (BannerType.STR_PACKAGE_TOUR_LIST.equals(showcase.operation)) {
                //TODO 跟团游专题列表
                if (!StringUtil.isEmpty(showcase.operationContent)) {
                    NavUtils.gotoNineClubDetailListActivity(context, showcase.operationContent, ItemType.TOUR_LINE, showcase.title);
                } else {
                    ToastUtil.showToast(context, context.getString(R.string.toast_banner_params_error));
                }
            } else if (BannerType.STR_SHOP_HOME_PAGE.equals(showcase.operation)) {
                //TODO 店铺首页
                if (!StringUtil.isEmpty(showcase.operationContent)) {
                    gotoShopHomePageActivity(context, "", Long.parseLong(showcase.operationContent));
                } else {
                    ToastUtil.showToast(context, context.getString(R.string.toast_banner_params_error));
                }
            } else if (BannerType.STR_MASTER_DETAIL.equals(showcase.operation)) {
                //TODO 达人主页
                if (!StringUtil.isEmpty(showcase.operationContent)) {
                    gotoMasterHomepage(context, Long.parseLong(showcase.operationContent));
                } else {
                    ToastUtil.showToast(context, context.getString(R.string.toast_banner_params_error));
                }
            } else if (BannerType.STR_MASTER_LIST.equals(showcase.operation)) {
                //TODO 达人专题列表
                if (!StringUtil.isEmpty(showcase.operationContent)) {
                    gotoMasterListActivity(context, showcase.operationContent, showcase.title);
                } else {
                    ToastUtil.showToast(context, context.getString(R.string.toast_banner_params_error));
                }
            } else if (BannerType.STR_QUANYAN_BUY_DETAIL.equals(showcase.operation)) {
                //TODO 必买详情
                if (!StringUtil.isEmpty(showcase.operationContent)) {
                    NavUtils.gotoProductDetail(context, ItemType.NORMAL, Long.parseLong(showcase.operationContent), showcase.title);
                } else {
                    ToastUtil.showToast(context, context.getString(R.string.toast_banner_params_error));
                }
            } else if (BannerType.STR_MUST_BUY_LIST.equals(showcase.operation)) {
                //TODO 必买专题列表
                NavUtils.gotoNineClubDetailListActivity(context, "0", ItemType.NORMAL, showcase.title);
            } else if (BannerType.STR_QUANYAN_BUY.equals(showcase.operation)) {
                //TODO 必买首页
                NavUtils.gotoBuyMustListActivity(context);
            } else if (BannerType.STR_QUANYAN_FOOD_DETAIL.equals(showcase.operation)) {
                //TODO 美食店铺详情
                if (!StringUtil.isEmpty(showcase.operationContent)) {
                    NavUtils.gotoEatGreatDetailActivity(context, Long.parseLong(showcase.operationContent));
                } else {
                    ToastUtil.showToast(context, context.getString(R.string.toast_banner_params_error));
                }
            } else if (BannerType.STR_QUANYAN_FOOD.equals(showcase.operation)) {
                //TODO 美食首页
                NavUtils.gotoEatGreatActivity(context, null);
            } else if (BannerType.STR_VIEW_TOPIC_DETAIL.equals(showcase.operation)) {
                //TODO 查看话题详情
                if (!StringUtil.isEmpty(showcase.operationContent)) {
                    //NavUtils.gotoTopicDetailsActivity(context, showcase.operationContent, showcase.id);
                    NavUtils.gotoNewTopicDetailsActivity(context, showcase.operationContent, showcase.id);
                } else {
                    ToastUtil.showToast(context, context.getString(R.string.toast_banner_params_error));
                }
            } else if (BannerType.STR_HOME_CHECKIN.equals(showcase.operation)) {
                //TODO 签到首页
                if (instance.userService.isLogin() && !StringUtil.isEmpty(showcase.operationContent)) {
                    WebParams wp = new WebParams();
                    wp.setIsShowShareButton(false);
                    wp.setTitle(showcase.title);
                    wp.setIsSetTitle(true);
                    wp.setUrl(showcase.operationContent);
                    NavUtils.openBrowser(context, wp);
                } else {
                    NavUtils.gotoLoginActivity(context);
                }
            } else if (BannerType.STR_HOME_PEDMOTER.equals(showcase.operation)) {
                //TODO 计步器首页
                NavUtils.gotoPedometerActivity(context);
            } else if (BannerType.STR_HOME_INTEGRAL_MALL.equals(showcase.operation)) {
                //TODO 积分商城首页
                NavUtils.gotoIntegralmallHomeActivity(context);
            } else if (BannerType.STR_HOME_MASTER_CIRCLE.equals(showcase.operation)) {
                //TODO 发现的达人圈
                NavUtils.gotoDiscoveryFragment(context, 0);
            } else if (BannerType.STR_VIEW_TOPIC_LIST.equals(showcase.operation)) {
                //TODO 发现的话题列表
                NavUtils.gotoDiscoveryFragment(context, 1);
            } else if (BannerType.STR_HOME_CONSULTING_LIST.equals(showcase.operation)) {
                //TODO 咨询达人首页
                NavUtils.gotoMasterConsultHomeActivity(context);
            } else if (BannerType.STR_MASTER_CONSULT_SERVICE_LIST.equals(showcase.operation)) {
                //TODO 咨询达人列表页
                NavUtils.gotoMasterAdviceListActivity(context);
            } else if (BannerType.STR_VIEW_TALENT_STORY.equals(showcase.operation)) {
                //TODO 达人故事
                WebParams wp = new WebParams();
                if (showcase.operationContent.startsWith("http://")) {
                    wp.setUrl(showcase.operationContent);
                } else {
                    String url = SPUtils.getString(SPUtils.TYPE_DEFAULT, context, SysConfigType.URL_TALENT_STORY);
                    wp.setUrl(url + showcase.operationContent);
                }
                wp.setShowTitle(false);
                NavUtils.openBrowser(context, wp);
            } else if (BannerType.STR_TRAVEL_INFORMATION.equals(showcase.operation)) {
                //TODO 旅游资讯
                WebParams wp = new WebParams();
                if (showcase.operationContent.startsWith("http://")) {
                    wp.setUrl(showcase.operationContent);
                } else {
                    String url = SPUtils.getString(SPUtils.TYPE_DEFAULT, context, SysConfigType.URL_TALENT_STORY);
                    wp.setUrl(url + showcase.operationContent);
                }
                wp.setShowTitle(false);
                NavUtils.openBrowser(context, wp);
            } else if (BannerType.STR_VIEW_CONSULTING_SERVICE_DETAIL.equals(showcase.operation)) {
                //TODO 咨询服务商品详情
                if (!StringUtil.isEmpty(showcase.operationContent)) {
                    NavUtils.gotoProductDetail(context, ItemType.CONSULT, Long.parseLong(showcase.operationContent), "");
                } else {
                    ToastUtil.showToast(context, context.getString(R.string.toast_banner_params_error));
                }
            } else if (BannerType.STR_FAST_CONSULTING.equals(showcase.operation)) {
                //TODO 快速咨询
                NavUtils.gotoQuickConsultActivity(context);
            }else if (BannerType.STR_LIVE_LIST_BY_CITY_CODE.equals(showcase.operation)) {
                //TODO 查看直播列表
                if (!StringUtil.isEmpty(showcase.operationContent)) {
                    long cityCode = -1;
                    try {
                        cityCode = Long.parseLong(showcase.operationContent);
                    } catch (NumberFormatException e) {
                    }
                    if (cityCode == -1) {
                        IntentUtil.startLiveListActivity(context, "直播", cityCode, true);
                    } else if (cityCode == 0) {
                        IntentUtil.startLiveListActivity(context, "火星", cityCode, false);
                    } else {
                        String cityName = LocationManager.getInstance().getStorage().getLast_cityName();
                        IntentUtil.startLiveListActivity(context, cityName, cityCode, false);
                    }
                } else {
                    IntentUtil.startLiveListActivity(context, "直播", -1, true);
//                    ToastUtil.showToast(context, context.getString(R.string.toast_banner_params_error));
                }
            } else if (BannerType.STR_LIVE_PLAYBACK_DETAIL.equals(showcase.operation)) {
                //TODO 查看直播回访详情
                if (!StringUtil.isEmpty(showcase.operationContent)) {
                    long liveId = -1;
                    try {
                        liveId = Long.parseLong(showcase.operationContent);
                    } catch (NumberFormatException e) {
                        ToastUtil.showToast(context, context.getString(R.string.toast_banner_params_error));
                        return;
                    }
                    IntentUtil.startVideoClientActivity(liveId, -1, false, 0);
                } else {
                    ToastUtil.showToast(context, context.getString(R.string.toast_banner_params_error));
                }
            } else if (BannerType.STR_LIVE_ROOM_DETAIL.equals(showcase.operation)) {
                //TODO 查看直播房间详情
                if (!StringUtil.isEmpty(showcase.operationContent)) {
                    long roomId = -1;
                    try {
                        roomId = Long.parseLong(showcase.operationContent);
                    } catch (NumberFormatException e) {
                        ToastUtil.showToast(context, context.getString(R.string.toast_banner_params_error));
                        return;
                    }
                    IntentUtil.startVideoClientActivity(-1, -1, true, 0);
                } else {
                    ToastUtil.showToast(context, context.getString(R.string.toast_banner_params_error));
                }
            } else if (BannerType.TALENT_PUBLISH_SERVICE.equals(showcase.operation)) {
                NavUtils.gotoReleaseServiceActivity(context, -1, -1);
            } else if (BannerType.TALENT_SERVICE_LIST.equals(showcase.operation)) {
                NavUtils.gotoManageServiceInfoActivity(context);
            } else if (BannerType.POINT_ORDER_DETAIL.equals(showcase.operation)) {
                long order_id = showcase.id;
            } else if (BannerType.CONSULT_ORDER_DETAIL.equals(showcase.operation)) {
                long order_id = showcase.id;
            } else {
                ToastUtil.showToast(context, context.getString(R.string.label_toast_version_low));
            }
        } catch (Exception e) {
            ToastUtil.showToast(context, context.getString(R.string.toast_banner_params_error));
            e.printStackTrace();
        }
    }

    /**
     * 同城，周边，跟团游/自由行
     *
     * @param context
     * @param id
     * @param type
     */
    public static void gotoLineActivity(Context context, long id, String type, String title) {
        LineActivity.gotoLineActivty(context, id, type, title);
    }

    /**
     * 线路搜索结果页
     *
     * @param context
     * @param title
     */
    public static void gotoLineSearchResultActivity(Context context,
                                                    String title,
                                                    String lineType,
                                                    String searchWord,
                                                    String cityCode,
                                                    String cityName,
                                                    String tagName,
                                                    long tagId) {
        LineSearchResultActivity.gotoLineSearchActivity(context, title, lineType, searchWord, cityCode, cityName, tagName, tagId);
    }

    /**
     * 跳转到线路确认订单界面
     *
     * @param context
     * @param startTimeMill
     * @param endTimeMill
     */
    public static void gotoOrderConfigActivity(Context context, String itemType, TmCreateOrderResultTO result, long startTimeMill, long endTimeMill, String scenicName) {
        // 还用webview跳转
        OrderConfigActivity.gotoActivitysOrderConfigActivity(context, itemType, result, startTimeMill, endTimeMill, scenicName);
//WebViewActivity.openBrowser(context, );
    }

    /**
     * 跳转到聊天界面
     *
     * @param context
     * @param peerId  联系人id
     */
    private static void gotoMessageActivity(Context context, long peerId, ProductCardModel productCardModel, long orderId, long serviceId) {
        if (!instance.userService.isLogin()) {
            gotoLoginActivity(context);
            return;
        }
        Intent intent = new Intent(context, MessageActivity.class);
        int sesssionType = DBConstant.SESSION_TYPE_SINGLE;
        if (serviceId > 0) {
            sesssionType = DBConstant.SESSION_TYPE_CONSULT;
        }
        intent.putExtra(IntentConstant.KEY_SESSION_KEY, EntityChangeEngine.getSessionKey(peerId, sesssionType, serviceId));
        if (productCardModel != null) {
            intent.putExtra(IntentConstant.EXTRA_GOODS_LINK, productCardModel);
        }
        if (orderId > 0) {
            intent.putExtra(IntentConstant.EXTRA_SEND_ORDER_ID, orderId);
        }
        context.startActivity(intent);

    }

    public static void gotoMessageActivity(Context context, long peerId, ProductCardModel productCardModel) {
        gotoMessageActivity(context, peerId, productCardModel, 0, 0);
    }

    public static void gotoMessageActivity(Context context, long peerId, ProductCardModel productCardModel, long serviceId) {
        gotoMessageActivity(context, peerId, productCardModel, 0, serviceId);
    }

    public static void gotoMessageActivity(Context context, long peerId, long orderId) {
        gotoMessageActivity(context, peerId, null, orderId, 0);
    }

    public static void gotoMessageActivity(Context context, long peerId) {
        gotoMessageActivity(context, peerId, null, 0, 0);
    }

    /**
     * 跳转到通知和互动界面
     *
     * @param context
     * @param bizType
     */
    public static void gotoNotificationListActivity(Context context, int bizType) {
        NotificationListActivity.gotoNotificationListActivity(context, bizType);
    }

    /**
     * 消息通知开关界面
     *
     * @param context
     */
    public static void gotoMessageNotificationSettingActivity(Context context) {
        if (!instance.userService.isLogin()) {
            gotoLoginActivity(context);
            return;
        }
        Intent intent = new Intent(context, MessageNotificationSettingActivity.class);
        context.startActivity(intent);
    }

    /* 线路订单
     *
     * @param context
     */
    public static void gotoLineOrderActivity(Context context, long id, String type) {
        if (TextUtils.isEmpty(type) || id == -1) {
            return;
        }
        if (ItemType.FREE_LINE.equals(type) || ItemType.TOUR_LINE.equals(type) ||
                ItemType.FREE_LINE_ABOARD.equals(type) || ItemType.TOUR_LINE_ABOARD.equals(type) || ItemType.POINT_MALL.equals(type)) {
            //线路订单or积分商城订单
            LineOrderActivity.gotoLineOrderActivity(context, id, type);
        } else {
            return;
        }
    }

    /**
     * 点击im产品卡跳转产品详情
     *
     * @param context
     * @param model
     */
    public static void gotoProductCardDetails(Context context, ProductCardMessage model) {
        String subType = model.getSubType();
        // 0启动旧版native,1启动h5
        if (SPUtils.getOPEN_NEW_H5_MALL(context).equals("1")) {
            String url = SPUtils.getURL_POINT_ITEM_DETAIL(context);
            if (TextUtils.isEmpty(url))
                return;
            NavUtils.startWebview((Activity) context, "", url.replace(":id", String.valueOf(model.getProductId())), 0);
        } else {
            NavUtils.gotoProductDetail(context, model.getSubType(), model.getProductId(), model.getTitle());
        }
    }

    public static void gotoAddLocation(Activity activity, int reqCode) {
        AddLocationSeach.gotoAddLocation(activity, reqCode);
    }

    /**
     * 添加话题标签
     *
     * @param activity
     * @param reqCode
     */
    public static void gotoAddTopic(Activity activity, int reqCode) {
        AddTopicSearch.gotoAddTopic(activity, reqCode);
    }

    /**
     * 启动被踢出页面
     */
    public static void gotoKickOut(Context context) {


        if (!MobileUtil.isActivityRunning(context, HomeMainTabActivity.class.getName())) {
            YhyRouter.getInstance().startMainActivity(context, MainActivityFromType.START_FROM_KICKOUT);
        } else {
            Intent intent = new Intent();
            intent.setClass(context, KickoutAcitivity.class);
            context.startActivity(intent);
            if (context instanceof Activity) {
                ((Activity) context).overridePendingTransition(R.anim.anim_pop_in, R.anim.anim_not_change);
            }
        }
    }

    /**
     * 从其他地方跳转到首页，切换到发现模块
     *
     * @param context
     * @param type    0:达人圈    1:话题列表     2:游记列表
     */
    public static void gotoDiscoveryFragment(Context context, int type) {
//        Intent intent = new Intent(context, GonaActivity.class);
        Intent intent = new Intent(context, HomeMainTabActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(SPUtils.EXTRA_NEWINTENT_TYPE, type);
        intent.putExtra(SPUtils.EXTRA_GONA_DISCOVER_TYPE, true);
        context.startActivity(intent);
    }



    /**
     * 举报
     *
     * @param context
     */
    public static void gotoComplaintList(Context context, String textContent, ArrayList<String> picList, long id) {
        if (!instance.userService.isLogin()) {
            gotoLoginActivity(context);
            return;
        }
        ComplaintListActivity.gotoComplaintList(context, textContent, picList, id);
    }

    /**
     * 屏蔽
     *
     * @param context
     */
    public static void gotoBlackSetting(Context context, long userId, long id, String nickname) {
        if (!instance.userService.isLogin()) {
            gotoLoginActivity(context);
            return;
        }
        BlackSettingActivity.gotoBlackSetting(context,  userId,  id,  nickname);
    }

    /**
     * 我的常用联系人和选择联系人时用这个跳转，其中channelType == TouristType.MIMETOURIST或者channelType == TouristType.ORDERCONTACTS
     *
     * @param context
     * @param reqCode
     * @param channelType
     * @param touristType
     */
    public static void gotoCommonUseTouristActivity(Activity context, int reqCode, int channelType, String touristType) {
        if (instance.userService.isLogin()) {
            CommonUseTouristActivity.gotoCommonUseTouristActivity(context, reqCode, channelType, touristType);
        } else {
            gotoLoginActivity(context);
        }
    }

    /**
     * 选择游客时用这个跳转 其中channelType == TouristType.ORDERTOURIST，可以选择的游客最大值
     *
     * @param context
     * @param reqCode
     * @param channelType
     * @param touristType
     * @param visitorInfoList
     * @param maxNum
     */
    public static void gotoCommonUseTouristActivity(Activity context, int reqCode, int channelType, String touristType, VisitorInfoList visitorInfoList, int maxNum) {
        if (instance.userService.isLogin()) {
            CommonUseTouristActivity.gotoCommonUseTouristActivity(context, reqCode, channelType, touristType, visitorInfoList, maxNum);
        } else {
            gotoLoginActivity(context);
        }
    }

    /**
     * 从下单界面跳转到添加或者修改联系人或游客界面
     *
     * @param context
     * @param reqCode
     * @param channelType
     * @param touristType
     */
    public static void gotoAddOrUpdateOrderTouristActivity(Activity context, int reqCode, int channelType, String touristType, UserContact userContact) {
        if (instance.userService.isLogin()) {
            AddOrUpdateOrderTouristActivity.gotoAddOrUpdateOrderTouristActivity(context, reqCode, channelType, touristType, userContact);
        } else {
            gotoLoginActivity(context);
        }
    }

    /**
     * 跳转到我的常用游客中的添加或修改界面
     *
     * @param context
     * @param reqCode
     * @param userContact
     */
    public static void gotoAddOrUpdateMimeTouristActivity(Activity context, int reqCode, UserContact userContact) {
        if (instance.userService.isLogin()) {
            AddOrUpdateMimeTouristActivity.gotoAddOrUpdateMimeTouristActivity(context, reqCode, userContact);
        } else {
            gotoLoginActivity(context);
        }
    }

    /**
     * 跳转到 我的常用游客中的 添加或者编辑证件号码界面
     *
     * @param context
     * @param reqCode
     * @param certificate
     */
    public static void gotoAddOrUpdateCodeActivity(Activity context, int reqCode, Certificate certificate, UserContact userContact) {
        if (instance.userService.isLogin()) {
            AddOrUpdateCodeActivity.gotoAddOrUpdateCodeActivity(context, reqCode, certificate, userContact);
        } else {
            gotoLoginActivity(context);
        }
    }

    /**
     * 积分商城
     *
     * @param context
     */
    public static void gotoIntegralmallHomeActivity(Context context) {
        String integralHomeUrl = SPUtils.getIntegralHomeUrl(context);
        if (!StringUtil.isEmpty(integralHomeUrl)) {
            WebParams wp = new WebParams();
            wp.setIsSetTitle(true);
            wp.setTitle("");
            wp.setUrl(integralHomeUrl);
//            wp.setUrlAndPlayer("http://h5test.yingheying.com/alauda/?#/formTest");
            NavUtils.openBrowser(context, wp);
        }
    }

    /**
     * 商品优惠券
     *
     * @param context
     */
    public static void gotoSellCouponAcitvity(Context context, long itemVOid, int type) {
        CouponActivity.gotoCouponAcitvity(context, itemVOid, type);
    }

    /**
     * 跳转到全部订单列表
     *
     * @param context
     */
    public static void gotoMyOrderAllListActivity(Context context) {
        if (!instance.userService.isLogin()) {
            gotoLoginActivity(context);
            return;
        }
        MyOrderListActivity.gotoMyOrderAllListActivity(context);
    }

    /**
     * 跳转到新的话题详情
     *
     * @param context
     */
    public static void gotoNewTopicDetailsActivity(Context context, String topicName, long topicId) {
        NewTopicDetailActivity.gotoNewTopicDetailsActivity(context, topicName, topicId);
    }

    /**
     * 跳转到积分上城下订单界面
     *
     * @param context
     */
    public static void gotoPointOrderActivity(Context context, long itemId, String itemType) {
        if (!instance.userService.isLogin()) {
            gotoLoginActivity(context);
            return;
        }
        PointOrderActivity.gotoPointOrderActivity(context, itemId, itemType);
    }

    /**
     * 跳转计步器首页
     *
     * @param context
     */
    public static void gotoPedometerActivity(Context context) {
        gotoPedometerActivity(context, false);
    }

    public static void gotoPedometerActivity(Context context, boolean showShare) {
        if (showShare) {
            if (!instance.userService.isLogin()) {
                gotoLoginActivity(context);
                return;
            } else {
                PedoActivity.gotoPedometerActivity(context, true);
            }
        } else {
            PedoActivity.gotoPedometerActivity(context);
        }
    }

    /**
     * 跳转分享页面
     *
     * @param context
     */
    public static void gotoShareActivity(Context context, int type, int shareWay) {
        ShareActivity.gotoShareActivity(context, type, shareWay);
    }

    /**
     * 跳转分享页面
     *
     * @param context
     */
    public static void gotoShareActivity(Context context, int type, int shareWay, MerchantItem merchantItem) {
        ShareActivity.gotoShareActivity(context, type, shareWay, merchantItem);
    }


    /**
     * 跳转分享页面
     *
     * @param context
     */
    public static void gotoShareActivity(Context context, int type, int shareWay, User userInfo) {
        ShareActivity.gotoShareActivity(context, type, shareWay, userInfo);
    }


    /**
     * 跳转到任务说明
     *
     * @param context
     */
    public static void gotoTaskDescriptionActivity(Activity context, String title, String content) {
        TaskDescriptionActivity.gotoTaskDescriptionActivity(context, title, content);
    }

    /**
     * 订单优惠券
     *
     * @param context
     * @param sellerId
     * @param totalPrice
     */
    public static void gotoOrderCouponActivity(Activity context, long sellerId, long totalPrice, int requestCode) {
        OrderCouponActivity.gotoOrderCouponActivity(context, sellerId, totalPrice, requestCode);
    }

    /**
     * 发布服务
     *
     * @param context
     * @param id
     * @param type
     */
    public static void gotoReleaseServiceActivity(Context context, long id, int type) {
        if (!instance.userService.isLogin()) {
            gotoLoginActivity(context);
            return;
        }
        ReleaseServiceActivity.gotoReleaseServiceActivity(context, id, type);
    }

    /**
     * 编辑服务
     *
     * @param context
     * @param id
     * @param type
     * @param reqCode
     */
    public static void gotoReleaseServiceActivity(Activity context, long id, int type, int reqCode) {
        if (!instance.userService.isLogin()) {
            gotoLoginActivity(context);
            return;
        }
        ReleaseServiceActivity.gotoReleaseServiceActivity(context, id, type, reqCode);
    }

    /**
     * 服务目的地
     *
     * @param context
     */
    public static void gotoReleaseDestinationActivity(Activity context, List<Destination> list, int reqCode) {
        ReleaseDestinationActivity.gotoReleaseDestinationActivity(context, list, reqCode);
    }

    public static void gotoAddServiceDetailActivity(Activity context, List<ItemProperty> itemProperties, int reqCode) {
        AddServiceDetailActivity.gotoAddServiceDetailActivity(context, itemProperties, reqCode);
    }

    /**
     * 跳转到提交咨询页面
     *
     * @param context
     */
    public static void gotoMasterConsultActivity(Context context, long itemId, long point, long time) {
        if (!instance.userService.isLogin()) {
            gotoLoginActivity(context);
            return;
        }
        Intent intent = new Intent(context, MasterConsultActivity.class);
        intent.putExtra(IntentConstants.EXTRA_ITEM_ID, itemId);
        intent.putExtra(IntentConstants.EXTRA_POINT, point);
        intent.putExtra(IntentConstants.EXTRA_SERVICE_TIME, time);
        context.startActivity(intent);
    }

    /**
     * 跳转到服务管理页面
     *
     * @param context
     */
    public static void gotoManageServiceInfoActivity(Context context) {
        ManageServiceInfoAcitvity.gotoManageServiceInfoActivity(context);
    }

    public static void gotoDetailAccountActivity(Context context, Bill bill) {
        DetailAccountActivity.gotoDetailAccountActivity(context, bill);
    }

    public static void gotoPrepaidOutAndInListActivity(Context context) {
        PrepaidOutAndInListActivity.gotoPrepaidOutAndInListActivity(context);
    }

    /**
     * 跳转到服务管理页面
     *
     * @param context
     * @param index
     */
    public static void gotoManageServiceInfoActivity(Context context, int index) {
        ManageServiceInfoAcitvity.gotoManageServiceInfoActivity(context, index);
    }

    /**
     * 跳转到图文详情
     *
     * @param context
     * @param type
     */
    public static void gotoPictureAndTextActivity(Activity context, String type, List<PictureTextItemInfo> list, List<MediaItem> imageList, int reqCode, long id) {
        PictureAndTextActivity.gotoPictureAndTextActivity(context, type, list, imageList, reqCode, id);
    }

    /**
     * 跳转达人订单列表
     *
     * @param context
     * @param type
     */
    public static void gotoExpertOrderListActivity(Context context, int type) {
        if (!instance.userService.isLogin()) {
            gotoLoginActivity(context);
            return;
        }
        ExpertOrderListActivity.gotoExpertOrderListActivity(context, type);
    }

    /**
     * 跳转达人订单详情
     *
     * @param context
     * @param type
     */
    public static void gotoExpertOrderDetailActivity(Context context, int type, long processOrderId) {
        ExpertOrderDetailActivity.startExpertOrderDetailActivity(context, type, processOrderId);
    }

    /**
     * 跳转达人订单详情ForResult
     *
     * @param context
     * @param type
     */
    public static void gotoExpertOrderDetailActivityForResult(Activity context, int type, long processOrderId) {
        ExpertOrderDetailActivity.startExpertOrderDetailActivityForResult(context, type, processOrderId);
    }

    /**
     * 跳转到达人咨询首页
     *
     * @param context
     */
    public static void gotoMasterConsultHomeActivity(Context context) {
        MasterConsultHomeActivity.gotoMasterConsultHomeActivity(context);
    }

    public static void gotoQuickConsultActivity(Context context) {
        if (!instance.userService.isLogin()) {
            gotoLoginActivity(context);
            return;
        }
        Intent intent = new Intent(context, QuickConsultActivity.class);
        context.startActivity(intent);
    }

    /**
     * 达人咨询服务评价
     *
     * @param context
     * @param orderId
     * @param userId
     * @param type
     */
    public static void gotoOrderCommentNewActivity(Activity context, long orderId, long userId, String type, int reqCode) {
        OrderCommentNewActivity.startOrderCommentNewActivity(context, orderId, userId, type, reqCode);
    }


    /**
     * 评论列表界面
     */
    public static void gotoGoodsCommentListActivity(Activity activity, long orderId, long userId, String type, int reqCode) {
        GoodsCommentListActivity.startGoodsCommentListActivity(activity, orderId, userId, type, reqCode);
    }


    /**
     * 跳转首页弹框
     *
     * @param context
     */
    public static void gotoBomboxActivity(Context context) {
        BombBoxActivity.gotoBomboxActivity(context);
    }

    /**
     * 钱包
     *
     * @param context
     */
    public static void gotoWalletActivity(Context context) {
        if (instance.userService.isLogin()) {
            WalletActivity.gotoWalletActivity(context);
        } else {
            gotoLoginActivity(context);
        }
    }

    /**
     * 实名认证
     *
     * @param context
     */
    public static void gotoRealNameAuthActivity(Context context, String type, String toWallet) {
        RealNameAuthActivity.gotoRealNameAuthActivity(context, type, toWallet);
    }

    /**
     * 设置支付密码
     *
     * @param context
     */

    public static void gotoSettingPayPassActivity(Context context, String verifyCode, String verifyIdentityCode, String type, String toWallet) {
        SettingPayPassActivity.gotoSettingPayPassActivity(context, verifyCode, verifyIdentityCode, type, toWallet);
    }

    /**
     * 跳转充值界面
     *
     * @param context
     */
    public static void gotoRechargeActivity(Activity context, int reqCode) {
        RechargeActivity.gotoRechargeActivity(context, reqCode);
    }

    /**
     * 提现
     *
     * @param context
     */
    public static void gotoWithDrawActivity(Context context, long balace) {
        WithDrawActivity.gotoWithDrawActivity(context, balace);
    }

    /**
     * 选择银行卡
     *
     * @param context
     */
    public static void gotoSelectCardActivity(Activity context, int requestCode, BankCard bankCard) {
        SelectCardActivity.gotoSelectCardActivity(context, requestCode, bankCard);
    }

    /**
     * 密码验证
     *
     * @param context
     */
    public static void gotoVerifyPassWordActivity(Context context) {
        VerifyPassWordActivity.gotoVerifyPassWordActivity(context);
    }

    /**
     * 提现详情
     *
     * @param context
     */
    public static void gotoWithDrawDetailsActivity(Context context, BankCard mBankCard, long money) {
        WithDrawDetailsActivity.gotoWithDrawDetailsActivity(context, mBankCard, money);
    }

    /**
     * 密码管理
     *
     * @param context
     */
    public static void gotoPassWordManagerActivity(Context context) {
        if (instance.userService.isLogin()) {
            PayPassWordManagerActivity.gotoPassWordManagerActivity(context);
        } else {
            gotoLoginActivity(context);
        }
    }

    /**
     * 修改支付密码
     *
     * @param context
     */
    public static void gotoUpdatePassWordActivity(Context context) {
        UpdatePassWordActivity.gotoUpdatePassWordActivity(context);
    }

    /**
     * 绑定银行卡
     *
     * @param context
     * @param type
     */
    public static void gotoBindCardActivity(Context context, int type) {
        BindCardActivity.gotoBindCardActivity(context, type);
    }

    /**
     * 店铺介绍
     *
     * @param context
     * @param sellerInfo
     */
    public static void gotoShopInformationActivity(Context context, Merchant sellerInfo) {
        ShopInformationActivity.gotoShopInformationActivity(context, sellerInfo);
    }

    /**
     * 店铺简介 文字
     *
     * @param context
     * @param sellerId
     */
    public static void gotoShopSimpleIntroduceActivity(Context context, long sellerId) {
        ShopSimpleIntroduceActivity.gotoShopSimpleIntroduceActivity(context, sellerId);
    }

    /**
     * 工商执照-店铺
     *
     * @param context
     * @param sellerId
     */
    public static void gotoShopLicenseActivity(Context context, long sellerId, String sellName) {
        ShopLicenseActivity.gotoShopLicenseActivity(context, sellerId, sellName);
    }

    /**
     * 二维码扫码
     *
     * @param context
     * @param reqCode
     */
    public static void gotoCaptureActivity(Activity context, int reqCode) {
        ARouter.getInstance().build(ACTIVITY_CAPTURE_QRCODE).navigation(context,2);
    }

    /**
     * 我的二维码界面
     *
     * @param context
     */
    public static void gotoMineQrActivity(Context context) {
        gotoMineQrActivity(context, false);
    }

    public static void gotoMineQrActivity(Context context, boolean showShare) {
        if (!instance.userService.isLogin()) {
            gotoLoginActivity(context);
            return;
        }
        MineQrActivity.gotoMineQrActivity(context, showShare);
    }

    /**
     * 跳转咨询达人服务列表页
     *
     * @param context
     */
    public static void gotoMasterAdviceListActivity(Context context) {
        MasterAdviceListActivity.gotoMasterAdviceListActivity(context);
    }


    /**
     * 物流
     *
     * @param context
     * @param orderId
     */
    public static void gotoLogisticsActivity(Context context, long orderId) {
        LogisticsActivity.gotoLogisticsActivity(context, orderId);
    }

    /**
     * 填写用户信息
     *
     * @param context
     * @param cardNo
     */
    public static void gotoBindCardInforActivity(Context context, BankCard cardNo, String cardNum) {
        BindCardInforActivity.gotoBindCardInforActivity(context, cardNo, cardNum);
    }

    /**
     * 填写验证码界面
     *
     * @param context
     */
    public static void gotoBindCradVCodeActivity(Context context, String phone, String verifyIdentityCode, String type) {
        BindCradVCodeActivity.gotoBindCradVCodeActivity(context, phone, verifyIdentityCode, type);
    }

    public static void gotoForgetPasBindCardActivity(Context context, BankCard mBankCard, int sourceType, String bankNo) {
        ForgetPasBindCardActivity.gotoForgetPasBindCardActivity(context, mBankCard, sourceType, bankNo);
    }

    public static void gotoForgetPasSelectCardActivity(Context context) {
        ForgetPasSelectCardActivity.gotoForgetPasSelectCardActivity(context);
    }

    /**
     * 购物车
     *
     * @param context
     */
    public static void gotoSPCartActivity(Context context) {
        SPCartActivity.gotoSPCartActivity(context);
    }

    /**
     * 购物车下单界面
     *
     * @param context
     */
    public static void gotoSPCartOrderActivity(Context context, CreateOrderContextParamForPointMall mCreateOrderContextParamForPointMall) {
        SPCartOrderActivity.gotoSPCartOrderActivity(context, mCreateOrderContextParamForPointMall);
    }

    /**
     * 客服中心界面
     *
     * @param context
     */
    public static void gotoServiceCenterActivity(Context context) {
        ServiceCenterActivity.gotoServiceCenterActivity(context);
    }

    /**
     * 跳转到购物车优惠券界面
     *
     * @param context
     * @param sellerId
     */
    public static void gotoSPCartCouponActivity(Context context, long sellerId) {
        if (AppDebug.IS_MONKEY_TEST) {
            return;
        }
        context.startActivity(SPCartCouponActivity.getIntent(context, sellerId));
        ((Activity) context).overridePendingTransition(R.anim.push_up_in1, R.anim.push_up_out1);
    }

}
