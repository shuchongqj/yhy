package com.quanyan.yhy.net;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.mogujie.tt.DB.DBInterface;
import com.mogujie.tt.protobuf.helper.ProtoBuf2JavaBean;
import com.yhy.common.utils.NetworkUtil;
import com.quanyan.base.util.StringUtil;
import com.quanyan.pedometer.newpedometer.StepDBManager;
import com.quanyan.pedometer.newpedometer.WalkDataDaily;
import com.quanyan.utils.SHA1;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.ErrorCode;
import com.quanyan.yhy.database.DBManager;
import com.quanyan.yhy.eventbus.EvBusUserLoginState;
import com.quanyan.yhy.net.lsn.OnAbstractListener;
import com.quanyan.yhy.net.lsn.OnResponseListener;
import com.smart.sdk.api.request.ApiCode;
import com.smart.sdk.api.request.Device_SignDevice;
import com.smart.sdk.api.request.Items_GetItem;
import com.smart.sdk.api.request.Track_GetHistoryPedometerInfo;
import com.smart.sdk.api.request.Trademanager_GetCreateOrderContext;
import com.smart.sdk.api.request.Trademanager_GetUserAssets;
import com.smart.sdk.api.request.User_AppThirdPartyBind;
import com.smart.sdk.api.request.User_ChangePassword;
import com.smart.sdk.api.request.User_ChangeUserPhone;
import com.smart.sdk.api.request.User_DynamicPasswordRegisterOrLogin;
import com.smart.sdk.api.request.User_GetUserInfoByUserId;
import com.smart.sdk.api.request.User_GetUserInfoByUserIdList;
import com.smart.sdk.api.request.User_GetWapLoginToken;
import com.smart.sdk.api.request.User_ImLogin;
import com.smart.sdk.api.request.User_IsMobileExisted;
import com.smart.sdk.api.request.User_Login;
import com.smart.sdk.api.request.User_Logout;
import com.smart.sdk.api.request.User_RegisterNew;
import com.smart.sdk.api.request.User_RenewUserToken;
import com.smart.sdk.api.request.User_RequestSmsPassword;
import com.smart.sdk.api.request.User_RequestSmsPasswordByLogin;
import com.smart.sdk.api.request.User_ResetPassword;
import com.smart.sdk.api.resp.Api_BoolResp;
import com.smart.sdk.api.resp.Api_COMPETITION_CampaignsVoResult;
import com.smart.sdk.api.resp.Api_COMPETITION_QueryCampaignParam;
import com.smart.sdk.api.resp.Api_LIVE_LiveRoomHasOrNot;
import com.smart.sdk.api.resp.Api_PLACE_CityListResult;
import com.smart.sdk.api.resp.Api_RESOURCECENTER_HomeInfoResult;
import com.smart.sdk.api.resp.Api_RESOURCECENTER_PublishBootResult;
import com.smart.sdk.api.resp.Api_RESOURCECENTER_SportInfoResult;
import com.smart.sdk.api.resp.Api_RESOURCECENTER_UserInfoResult;
import com.smart.sdk.api.resp.Api_RESOURCECENTER_UserToolsResult;
import com.smart.sdk.api.resp.Api_RESOURCECENTER_UserWalletResult;
import com.smart.sdk.api.resp.Api_SNSCENTER_DisLikeArgs;
import com.smart.sdk.api.resp.Api_SNSCENTER_GuideTagResultList;
import com.smart.sdk.api.resp.Api_SNSCENTER_SnsCloseLiveResult;
import com.smart.sdk.api.resp.Api_SNSCENTER_SnsClosedViewTopNQuery;
import com.smart.sdk.api.resp.Api_SNSCENTER_SnsClosedViewTopNResult;
import com.smart.sdk.api.resp.Api_SNSCENTER_SnsCreateLiveResult;
import com.smart.sdk.api.resp.Api_SNSCENTER_SnsHasNoEndBatchResult;
import com.smart.sdk.api.resp.Api_SNSCENTER_SnsLiveRecordResult;
import com.smart.sdk.api.resp.Api_SNSCENTER_SnsReplayListQuery;
import com.smart.sdk.api.resp.Api_SNSCENTER_SnsReplayListResult;
import com.smart.sdk.api.resp.Api_SNSCENTER_UgcCountResultList;
import com.smart.sdk.api.resp.Api_SNSCENTER_UgcInfo;
import com.smart.sdk.api.resp.Api_TRACK_PedometerHistoryResult;
import com.smart.sdk.api.resp.Api_TRACK_PedometerHistoryResultList;
import com.smart.sdk.api.resp.Api_TRADEMANAGER_CreateProcessOrderParam;
import com.smart.sdk.api.resp.Api_TRADEMANAGER_DetailOrder;
import com.smart.sdk.api.resp.Api_TRADEMANAGER_ProcessQueryParam;
import com.smart.sdk.api.resp.Api_TRADEMANAGER_ProcessStateQuery;
import com.smart.sdk.api.resp.Api_TRADEMANAGER_QueryOrderVoucherDTO;
import com.smart.sdk.api.resp.Api_TRAIN_AsyncCallbackParam;
import com.smart.sdk.api.resp.Api_TRAIN_ResponseDTO;
import com.smart.sdk.api.resp.Api_USER_AppThirdPartyBind;
import com.smart.sdk.api.resp.Api_USER_ImLoginResp;
import com.smart.sdk.api.resp.Api_USER_LoginResp;
import com.smart.sdk.api.resp.Api_USER_RegisterResp;
import com.smart.sdk.api.resp.Api_USER_UserBatchQuery;
import com.smart.sdk.api.resp.Api_USER_UserInfo_ArrayResp;
import com.smart.sdk.client.ApiContext;
import com.smart.sdk.client.BaseRequest;
import com.yhy.common.base.YHYBaseApplication;
import com.yhy.common.beans.calender.PickSku;
import com.yhy.common.beans.net.model.AppDefaultConfig;
import com.yhy.common.beans.net.model.AppHomeData;
import com.yhy.common.beans.net.model.HomeTourGuide;
import com.yhy.common.beans.net.model.TalentHomeData;
import com.yhy.common.beans.net.model.club.ClubInfoList;
import com.yhy.common.beans.net.model.club.ClubMemberInfo;
import com.yhy.common.beans.net.model.club.ClubMemberInfoList;
import com.yhy.common.beans.net.model.club.PageInfo;
import com.yhy.common.beans.net.model.club.SnsActiveMemberPageInfo;
import com.yhy.common.beans.net.model.club.SnsActivePageInfo;
import com.yhy.common.beans.net.model.club.SnsActivePageInfoList;
import com.yhy.common.beans.net.model.club.SnsRimInfoList;
import com.yhy.common.beans.net.model.club.SubjectDetail;
import com.yhy.common.beans.net.model.club.SubjectDynamic;
import com.yhy.common.beans.net.model.club.SubjectInfo;
import com.yhy.common.beans.net.model.club.SubjectInfoList;
import com.yhy.common.beans.net.model.club.SubjectLive;
import com.yhy.common.beans.net.model.comment.ComSupportInfo;
import com.yhy.common.beans.net.model.comment.ComTagInfoList;
import com.yhy.common.beans.net.model.comment.CommentInfo;
import com.yhy.common.beans.net.model.comment.CommentInfoList;
import com.yhy.common.beans.net.model.comment.CommetDetailInfo;
import com.yhy.common.beans.net.model.comment.DimensionList;
import com.yhy.common.beans.net.model.comment.ProductRateInfoList;
import com.yhy.common.beans.net.model.comment.ProductRateInfoQuery;
import com.yhy.common.beans.net.model.comment.RateCaseList;
import com.yhy.common.beans.net.model.comment.RateCountInfo;
import com.yhy.common.beans.net.model.comment.RateCountQuery;
import com.yhy.common.beans.net.model.comment.RateInfoList;
import com.yhy.common.beans.net.model.comment.RateInfoQuery;
import com.yhy.common.beans.net.model.comment.SupportUserInfoList;
import com.yhy.common.beans.net.model.common.Booth;
import com.yhy.common.beans.net.model.common.BoothList;
import com.yhy.common.beans.net.model.common.ComIconList;
import com.yhy.common.beans.net.model.common.CrashInfoList;
import com.yhy.common.beans.net.model.common.PictureTextListQuery;
import com.yhy.common.beans.net.model.common.address.MyAddressContentInfo;
import com.yhy.common.beans.net.model.common.address.MyAddress_ArrayResp;
import com.yhy.common.beans.net.model.common.person.PictureTextListResult;
import com.yhy.common.beans.net.model.common.person.UserContact;
import com.yhy.common.beans.net.model.common.person.UserContact_ArrayResp;
import com.yhy.common.beans.net.model.discover.ComUserAndTagInfoList;
import com.yhy.common.beans.net.model.discover.TopicDetailResult;
import com.yhy.common.beans.net.model.discover.TopicInfoResult;
import com.yhy.common.beans.net.model.discover.TopicInfoResultList;
import com.yhy.common.beans.net.model.discover.TravelSpecialInfo;
import com.yhy.common.beans.net.model.discover.TravelSpecialInfoList;
import com.yhy.common.beans.net.model.discover.UgcInfoResult;
import com.yhy.common.beans.net.model.discover.UgcInfoResultList;
import com.yhy.common.beans.net.model.guide.GuideAttractionFocusInfo;
import com.yhy.common.beans.net.model.guide.GuideScenicInfoList;
import com.yhy.common.beans.net.model.guide.GuideSearchInfo;
import com.yhy.common.beans.net.model.guide.GuideSearchReq;
import com.yhy.common.beans.net.model.guide.GuideTipsInfo;
import com.yhy.common.beans.net.model.guide.NearGuideInfo;
import com.yhy.common.beans.net.model.item.BoothItemsResult;
import com.yhy.common.beans.net.model.item.CityActivityDetail;
import com.yhy.common.beans.net.model.item.CodeQueryDTO;
import com.yhy.common.beans.net.model.master.Merchant;
import com.yhy.common.beans.net.model.master.MerchantDesc;
import com.yhy.common.beans.net.model.master.MerchantList;
import com.yhy.common.beans.net.model.master.MerchantQuery;
import com.yhy.common.beans.net.model.master.Qualification;
import com.yhy.common.beans.net.model.master.QueryTerm;
import com.yhy.common.beans.net.model.master.QueryTermsDTO;
import com.yhy.common.beans.net.model.master.TalentInfo;
import com.yhy.common.beans.net.model.master.TalentInfoList;
import com.yhy.common.beans.net.model.master.TalentQuery;
import com.yhy.common.beans.net.model.member.MemberDetail;
import com.yhy.common.beans.net.model.member.MemberPurchauseDetail;
import com.yhy.common.beans.net.model.msg.CreateLiveResult;
import com.yhy.common.beans.net.model.msg.DeleteLiveListInfo;
import com.yhy.common.beans.net.model.msg.DisableParam;
import com.yhy.common.beans.net.model.msg.DisableResult;
import com.yhy.common.beans.net.model.msg.FollowAnchorParam;
import com.yhy.common.beans.net.model.msg.FollowAnchorResult;
import com.yhy.common.beans.net.model.msg.LiveCategoryResult;
import com.yhy.common.beans.net.model.msg.LiveMsgServerAddrParam;
import com.yhy.common.beans.net.model.msg.LiveMsgServerAddrResult;
import com.yhy.common.beans.net.model.msg.LiveRecordAPIPageQuery;
import com.yhy.common.beans.net.model.msg.LiveRecordAPIPageResult;
import com.yhy.common.beans.net.model.msg.LiveRecordInfo;
import com.yhy.common.beans.net.model.msg.LiveRecordListUserIdQuery;
import com.yhy.common.beans.net.model.msg.LiveRoomLivingRecordResult;
import com.yhy.common.beans.net.model.msg.LiveRoomResult;
import com.yhy.common.beans.net.model.msg.OtherMsgParam;
import com.yhy.common.beans.net.model.msg.OtherMsgResult;
import com.yhy.common.beans.net.model.paycore.AliBatchPayParam;
import com.yhy.common.beans.net.model.paycore.AliPayInfo;
import com.yhy.common.beans.net.model.paycore.BankCard;
import com.yhy.common.beans.net.model.paycore.BankCardList;
import com.yhy.common.beans.net.model.paycore.BankNameList;
import com.yhy.common.beans.net.model.paycore.BaseResult;
import com.yhy.common.beans.net.model.paycore.BillList;
import com.yhy.common.beans.net.model.paycore.CebCloudBatchPayParam;
import com.yhy.common.beans.net.model.paycore.CebCloudPayInfo;
import com.yhy.common.beans.net.model.paycore.CebCloudPayParam;
import com.yhy.common.beans.net.model.paycore.EleAccountInfo;
import com.yhy.common.beans.net.model.paycore.ElePurseBatchPayParam;
import com.yhy.common.beans.net.model.paycore.ElePursePayParam;
import com.yhy.common.beans.net.model.paycore.PayCoreBaseResult;
import com.yhy.common.beans.net.model.paycore.PcPayResult;
import com.yhy.common.beans.net.model.paycore.PcPayStatusInfo;
import com.yhy.common.beans.net.model.paycore.RechargeParam;
import com.yhy.common.beans.net.model.paycore.RechargeResult;
import com.yhy.common.beans.net.model.paycore.SettlementList;
import com.yhy.common.beans.net.model.paycore.SetupPayPwdParam;
import com.yhy.common.beans.net.model.paycore.SubmitIdCardPhotoParam;
import com.yhy.common.beans.net.model.paycore.SubmitIdCardPhotoResult;
import com.yhy.common.beans.net.model.paycore.UpdatePayPwdParam;
import com.yhy.common.beans.net.model.paycore.VerifyIdCardPhotoParam;
import com.yhy.common.beans.net.model.paycore.VerifyIdentityParam;
import com.yhy.common.beans.net.model.paycore.VerifyIdentityResult;
import com.yhy.common.beans.net.model.paycore.WithdrawParam;
import com.yhy.common.beans.net.model.paycore.WxBatchPayParam;
import com.yhy.common.beans.net.model.paycore.WxPayInfo;
import com.yhy.common.beans.net.model.pedometer.InviteShareInfo;
import com.yhy.common.beans.net.model.pedometer.PedometerHistoryResult;
import com.yhy.common.beans.net.model.pedometer.PedometerHistoryResultList;
import com.yhy.common.beans.net.model.pedometer.PedometerUserInfo;
import com.yhy.common.beans.net.model.pedometer.StepParam;
import com.yhy.common.beans.net.model.pedometer.StepResult;
import com.yhy.common.beans.net.model.pedometer.SyncParamList;
import com.yhy.common.beans.net.model.pedometer.SyncResult;
import com.yhy.common.beans.net.model.point.PointDetailQueryResult;
import com.yhy.common.beans.net.model.rc.ArticleRecommendInfo;
import com.yhy.common.beans.net.model.rc.CategoryTagList;
import com.yhy.common.beans.net.model.rc.CityList;
import com.yhy.common.beans.net.model.rc.ComplaintInfo;
import com.yhy.common.beans.net.model.rc.ComplaintOptionInfoList;
import com.yhy.common.beans.net.model.rc.DestCityPageContent;
import com.yhy.common.beans.net.model.rc.DestinationQuery;
import com.yhy.common.beans.net.model.rc.IntroduceInfo;
import com.yhy.common.beans.net.model.rc.MyTripContent;
import com.yhy.common.beans.net.model.rc.OnlineUpgrade;
import com.yhy.common.beans.net.model.rc.SystemConfig;
import com.yhy.common.beans.net.model.shop.MerchantItem;
import com.yhy.common.beans.net.model.tm.AcceptProcessOrderResult;
import com.yhy.common.beans.net.model.tm.CancelProcessResult;
import com.yhy.common.beans.net.model.tm.CartAmountResult;
import com.yhy.common.beans.net.model.tm.CartInfoListResult;
import com.yhy.common.beans.net.model.tm.ConsultCategoryInfoList;
import com.yhy.common.beans.net.model.tm.ConsultContent;
import com.yhy.common.beans.net.model.tm.ConsultState;
import com.yhy.common.beans.net.model.tm.CreateBatchOrderParam;
import com.yhy.common.beans.net.model.tm.CreateCartInfo;
import com.yhy.common.beans.net.model.tm.CreateOrderContextForPointMall;
import com.yhy.common.beans.net.model.tm.CreateOrderContextParamForPointMall;
import com.yhy.common.beans.net.model.tm.CreateOrderResultTOList;
import com.yhy.common.beans.net.model.tm.CreateProcessOrderResultTO;
import com.yhy.common.beans.net.model.tm.DailyTaskQueryResult;
import com.yhy.common.beans.net.model.tm.DeleteCartInfo;
import com.yhy.common.beans.net.model.tm.FinishProcessResult;
import com.yhy.common.beans.net.model.tm.ItemApiResult;
import com.yhy.common.beans.net.model.tm.ItemQueryParam;
import com.yhy.common.beans.net.model.tm.LgExpressInfo;
import com.yhy.common.beans.net.model.tm.OrderDetailResult;
import com.yhy.common.beans.net.model.tm.OrderPriceQueryDTO;
import com.yhy.common.beans.net.model.tm.OrderVoucherResult;
import com.yhy.common.beans.net.model.tm.PackageDetailResult;
import com.yhy.common.beans.net.model.tm.PostRateParam;
import com.yhy.common.beans.net.model.tm.ProcessOrder;
import com.yhy.common.beans.net.model.tm.ProcessOrderList;
import com.yhy.common.beans.net.model.tm.ProcessState;
import com.yhy.common.beans.net.model.tm.PublishServiceDO;
import com.yhy.common.beans.net.model.tm.SelectCartInfo;
import com.yhy.common.beans.net.model.tm.SellerAndConsultStateResult;
import com.yhy.common.beans.net.model.tm.SkuListWithStartDate;
import com.yhy.common.beans.net.model.tm.TmCloseOrderReasonItemList;
import com.yhy.common.beans.net.model.tm.TmConsultInfo;
import com.yhy.common.beans.net.model.tm.TmCreateOrderContext;
import com.yhy.common.beans.net.model.tm.TmCreateOrderParam;
import com.yhy.common.beans.net.model.tm.TmCreateOrderResultTO;
import com.yhy.common.beans.net.model.tm.TmOrderDetail;
import com.yhy.common.beans.net.model.tm.TmOrderList;
import com.yhy.common.beans.net.model.tm.TmPayInfo;
import com.yhy.common.beans.net.model.tm.TmPayStatusInfo;
import com.yhy.common.beans.net.model.tm.TmUserRoute;
import com.yhy.common.beans.net.model.tm.TmWxPayInfo;
import com.yhy.common.beans.net.model.tm.UpdateAmountCartInfo;
import com.yhy.common.beans.net.model.tm.VoucherResultList;
import com.yhy.common.beans.net.model.tm.VoucherTemplateResultList;
import com.yhy.common.beans.net.model.track.ReceivePointResult;
import com.yhy.common.beans.net.model.trip.ArroundScenicInitListResult;
import com.yhy.common.beans.net.model.trip.EntityShopDetail;
import com.yhy.common.beans.net.model.trip.HotelDetail;
import com.yhy.common.beans.net.model.trip.HotelFacilityResult;
import com.yhy.common.beans.net.model.trip.HotelFilter;
import com.yhy.common.beans.net.model.trip.HotelInfoResult;
import com.yhy.common.beans.net.model.trip.ItemVO;
import com.yhy.common.beans.net.model.trip.ItemVOResult;
import com.yhy.common.beans.net.model.trip.KeywordSearchDTO;
import com.yhy.common.beans.net.model.trip.LineDetail;
import com.yhy.common.beans.net.model.trip.LineFilter;
import com.yhy.common.beans.net.model.trip.LineInfoResult;
import com.yhy.common.beans.net.model.trip.LineItemDetail;
import com.yhy.common.beans.net.model.trip.PictureVOResult;
import com.yhy.common.beans.net.model.trip.QueryFacilitiesDTO;
import com.yhy.common.beans.net.model.trip.QueryItemDTO;
import com.yhy.common.beans.net.model.trip.QueryLineDTO;
import com.yhy.common.beans.net.model.trip.QueryPictureDTO;
import com.yhy.common.beans.net.model.trip.QueryRoomDTO;
import com.yhy.common.beans.net.model.trip.RoomsResult;
import com.yhy.common.beans.net.model.trip.ScenicDetail;
import com.yhy.common.beans.net.model.trip.ScenicFilter;
import com.yhy.common.beans.net.model.trip.ScenicInfoResult;
import com.yhy.common.beans.net.model.trip.SearchResultList;
import com.yhy.common.beans.net.model.trip.ShortItemsResult;
import com.yhy.common.beans.net.model.user.Destination;
import com.yhy.common.beans.net.model.user.DestinationList;
import com.yhy.common.beans.net.model.user.FollowerPageListResult;
import com.yhy.common.beans.net.model.user.SnsCountInfo;
import com.yhy.common.beans.net.model.user.TravelKa;
import com.yhy.common.beans.net.model.user.UserAssets;
import com.yhy.common.beans.net.model.user.UserInfo;
import com.yhy.common.beans.net.model.user.UserStatusInfoList;
import com.yhy.common.beans.user.User;
import com.yhy.common.utils.SPUtils;
import com.yhy.network.api.user.UserApi;
import com.yhy.network.manager.AccountManager;
import com.yhy.network.req.msgcenter.SaveMsgRelevanceReq;
import com.yhy.network.utils.RequestHandlerKt;
import com.yhy.network.utils.TempLog;
import com.yhy.push.Rom;
import com.yhy.router.YhyRouter;
import com.yhy.service.IUserService;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;

import static com.quanyan.yhy.net.BaseNetManager.getApiContext;
import static com.quanyan.yhy.net.BaseNetManager.sendRequest;

public class NetManager {
    public static final boolean DEBUG = true;
    private static NetManager mRssReaderInstance = null;
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    private String dtk = null;
    private String certStr = null;
    private String session = null;
    private long userId = -1;
    private String userToken = null;
    private Context mContext;
    private ApiContext mApiContext;
    private SnsNetManager mSnsNetManager;
    private CommonNetManager mCommonNetManager;
    private TravelNetManager mTravelNetManager;
    private TradeManagerNetManager mTradeManagerNetManager;

    @Autowired
    IUserService userService;

    private NetManager(Context context) {
        YhyRouter.getInstance().inject(this);
        mContext = context.getApplicationContext();
        initEnvInfo();
        TempLog.Companion.log("NetManager " + mApiContext.getDeviceId());
        if (mSnsNetManager == null) {
            mSnsNetManager = SnsNetManager.getInstance(mContext, mApiContext, mHandler);
        }

        if (mCommonNetManager == null) {
            mCommonNetManager = CommonNetManager.getInstance(mContext, mApiContext, mHandler);
        }

        if (mTravelNetManager == null) {
            mTravelNetManager = TravelNetManager.getInstance(mContext, mApiContext, mHandler);
        }

        if (mTradeManagerNetManager == null) {
            mTradeManagerNetManager = TradeManagerNetManager.getInstance(mContext, mApiContext, mHandler);
        }
    }

    /**
     * 释放资源
     */
    public void release() {
        if (mRssReaderInstance != null) {
            mRssReaderInstance = null;
        }

        if (mSnsNetManager != null) {
            mSnsNetManager.release();
            mSnsNetManager = null;
        }

        if (mCommonNetManager != null) {
            mCommonNetManager.release();
            mCommonNetManager = null;
        }

        if (mTravelNetManager != null) {
            mTravelNetManager.release();
            mTravelNetManager = null;
        }

        if (mTradeManagerNetManager != null) {
            mTradeManagerNetManager.release();
            mTradeManagerNetManager = null;
        }
    }

    private void initEnvInfo() {
        dtk = getDtk();
        certStr = getCertStr();
        // did = getDid();
        session = getSession();
        String tmpUserId = String
                .valueOf(userService.getLoginUserId());
        userId = Long.parseLong(tmpUserId);
        userToken = AccountManager.Companion.getAccountManager().getUserToken();
        try {
            mApiContext = getApiContext(mContext,
                    AccountManager.Companion.getAccountManager().getCertStr(), dtk);
        }catch (Exception e){
            e.printStackTrace();
            mApiContext = getApiContext(mContext);
        }

        ApiContext.setRsaPubKey(RequestHandlerKt.getRsaPublicKey());

        if (!TextUtils.isEmpty(userToken)) {
            mApiContext.setToken(userToken);
        }
        //初始化的时候设置好session
        mApiContext.setSession(session);

        if (userId > 0) {
            mApiContext.setUserId(userId);
        }

    }

    public synchronized static NetManager getInstance(Context context) {
        if (mRssReaderInstance == null) {
            mRssReaderInstance = new NetManager(context);
        }

        return mRssReaderInstance;
    }

    private String getDtk() {
        return AccountManager.Companion.getAccountManager().getDeviceToken();
    }

    private String getCertStr() {
        return AccountManager.Companion.getAccountManager().getCertStr();
    }

    private String getSession() {
        return AccountManager.Companion.getAccountManager().getSession();
    }

    public static String mPublicKey = "";

    public static void setRsaHelper(String publicKey) {
        if (publicKey == null) {
            return;
        }
        // mRsaHelper = new RsaHelper(publicKey);
        mPublicKey = publicKey;

    }

    /******************************************************* 帐号相关 begin**************************************/
    private boolean checkSubmitStatus(final OnAbstractListener lsn) {
        if (!NetworkUtil.isNetworkAvailable(mContext)) {
            mHandler.post(() -> {
                if (lsn != null) {
                    lsn.onInternError(
                            ErrorCode.NETWORK_UNAVAILABLE,
                            mContext.getString(R.string.network_unavailable));
                }
            });
            return false;
        }

        if (TextUtils.isEmpty(dtk)) {
            mHandler.post(() -> {
                if (lsn != null) {
                    lsn.onInternError(
                            ErrorCode.DEVICE_TOKEN_MISSING,
                            mContext.getString(R.string.device_token_missing));
                }
            });
            return false;
        }
        return true;
    }

    /**
     * NetManagerProxy在使用,误删
     */
    public synchronized void doDeviceRegist(String cert, String _dtk) {
        System.err.println("test========doDeviceRegist12 " + AccountManager.Companion.getAccountManager().getDeviceId());

        try {
            initEnvInfo();
            refreshOtherNetManager();
        } catch (Exception e) {
            e.printStackTrace();
        }

//        Runnable requestThread = () -> {
////            deviceRegistInner(channal, lsn);
//
//            //获取加签信息
//            Device_SignDevice signDeviceReq = new Device_SignDevice();
//            try {
//                sendRequest(mContext, mApiContext, signDeviceReq);
//                if (signDeviceReq.getReturnCode() != ApiCode.SUCCESS && signDeviceReq.getResponse() != null) {
//                    String sig = signDeviceReq.getResponse().deviceSignature;
//                    if (!StringUtil.isEmpty(sig)) {
//                        SPUtils.saveDSig(mContext, sig);
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        };
//        NetThreadManager.getInstance().execute(requestThread);
    }

    /**
     * 所有NM都需要刷新
     */
    private void refreshOtherNetManager() {
        if (mCommonNetManager != null) {
            mCommonNetManager.refreshEnvInfo();
            mCommonNetManager.updateApicontextAndHandler(mApiContext);
        }

        if (mSnsNetManager != null) {
            mSnsNetManager.refreshEnvInfo();
            mSnsNetManager.updateApicontextAndHandler(mApiContext);
        }

        if (mTradeManagerNetManager != null) {
            mTradeManagerNetManager.refreshEnvInfo();
            mTradeManagerNetManager.updateApicontextAndHandler(mApiContext);
        }

        if (mTravelNetManager != null) {
            mTravelNetManager.refreshEnvInfo();
            mTravelNetManager.updateApicontextAndHandler(mApiContext);
        }
    }

    /**
     * 更新过期的用户Token
     *
     * @param lsn
     */
    public void doRefreshUserToken(final OnResponseListener<Boolean> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        Runnable requestThread = () -> {
            try {
                final User_RenewUserToken req = new User_RenewUserToken();

                sendRequest(mContext, mApiContext, req);
                if (req.getReturnCode() == ApiCode.SUCCESS) {

                    Api_USER_LoginResp logInfo = req.getResponse();

                    SPUtils.setUid(mContext, logInfo.uid);

                    userId = logInfo.uid;
                    userToken = logInfo.token;

                    mApiContext.setToken(logInfo.token);
                    mApiContext.setUserId(logInfo.uid);

                    UserApi userApi = new UserApi();
                    userApi.saveLoginToken(logInfo.token);
                    userApi.savePhone(logInfo.mobile);
                    userApi.saveUserId(logInfo.uid);
                    userApi.saveDtk(dtk, certStr);


                    //刷新其他的NM
                    refreshOtherNetManager();

                    User_GetWapLoginToken reqWeb = new User_GetWapLoginToken();
                    sendRequest(mContext, mApiContext, new BaseRequest[]{reqWeb});
                    if (reqWeb.getReturnCode() == ApiCode.SUCCESS) {
                        SPUtils.setWebUserToken(mContext,
                                reqWeb.getResponse().value, logInfo.expire);
                        userApi.saveWebToken(reqWeb.getResponse().value);
                    }

                    mHandler.post(() -> {
                        if (lsn != null) {
                            lsn.onComplete(true, true, ErrorCode.STATUS_OK,
                                    null);
                        }
                    });

                } else {
                    mHandler.post(() -> {
                        if (lsn != null) {
                            lsn.onComplete(false, false,
                                    ErrorCode.STATUS_NETWORK_EXCEPTION,
                                    req.getReturnMessage());
                        }
                    });

                }
            } catch (final Exception e) {
                mHandler.post(() -> {
                    if (lsn != null) {
                        lsn.onComplete(false, false,
                                ErrorCode.STATUS_NETWORK_EXCEPTION,
                                e.getMessage());
                    }
                });

                e.printStackTrace();
            }
        };
        NetThreadManager.getInstance().execute(requestThread);
    }

    /**
     * 获取动态验证码
     *
     * @param phoneNumber 用户手机号，格式参考：13926583227
     * @param lsn
     */
    public void doRequestDynamic(final String phoneNumber,
                                 final String smsType,
                                 final OnResponseListener<Boolean> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        Runnable requestThread = () -> {
            cleanUserLogInfo();
            try {
                final User_RequestSmsPassword req = new User_RequestSmsPassword(phoneNumber);
//                    req.setTemplateId(1);// for 211
                if (!StringUtil.isEmpty(smsType)) {
                    req.setSmsType(smsType);
                }
                sendRequest(mContext, mApiContext, req);
                if (req.getReturnCode() == ApiCode.SUCCESS) {
                    mHandler.post(() -> {
                        if (lsn != null) {
                            lsn.onComplete(
                                    true,
                                    true,
                                    ErrorCode.STATUS_OK,
                                    null);
                        }
                    });
                } else {
                    mHandler.post(() -> {
                        if (lsn != null) {
                            lsn.onComplete(false, false,
                                    req.getReturnCode(),
                                    req.getReturnMessage());
                        }
                    });
                }
            } catch (final Exception e) {
                mHandler.post(() -> {
                    if (lsn != null) {
                        lsn.onComplete(false, false,
                                ErrorCode.STATUS_NETWORK_EXCEPTION,
                                e.getMessage());
                    }
                });
                e.printStackTrace();
            }
        };
        NetThreadManager.getInstance().execute(requestThread);
    }

    /**
     * 登录下获取动态验证码
     *
     * @param lsn
     */
    public void doRequestDynamicByLogin(final String smsType, final String phone,
                                        final OnResponseListener<Boolean> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        Runnable requestThread = () -> {
//                cleanUserLogInfo();
            try {
                final User_RequestSmsPasswordByLogin req = new User_RequestSmsPasswordByLogin(smsType);
//                    req.setSmsType(smsType);
//                    req.setTemplateId(1);// for 211
                if (!StringUtil.isEmpty(phone)) {
                    req.setChangeMobile(phone);
                }
                sendRequest(mContext, mApiContext, req);
                if (req.getReturnCode() == ApiCode.SUCCESS) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (lsn != null) {
                                lsn.onComplete(
                                        true,
                                        true,
                                        ErrorCode.STATUS_OK,
                                        null);
                            }
                        }
                    });
                } else {
                    mHandler.post(() -> {
                        if (lsn != null) {
                            lsn.onComplete(false, false,
                                    req.getReturnCode(),
                                    req.getReturnMessage());
                        }
                    });
                }
            } catch (final Exception e) {
                mHandler.post(() -> {
                    if (lsn != null) {
                        lsn.onComplete(false, false,
                                ErrorCode.STATUS_NETWORK_EXCEPTION,
                                e.getMessage());
                    }
                });
                e.printStackTrace();
            }
        };
        NetThreadManager.getInstance().execute(requestThread);
    }

    /**
     * 用户注册
     *
     * @param phoneNumber 用户手机号，格式参考：13926583227
     * @param dynamicCode 收到的活动验证码
     * @param password    密码
     * @param lsn
     */
    public void doUserReisterNew(final String phoneNumber,
                                 final String dynamicCode,
                                 final String password,
                                 final OnResponseListener<Boolean> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        Runnable requestThread = () -> {
            cleanUserLogInfo();
            try {
                mApiContext.setPhoneNumberAndDynamic(phoneNumber, dynamicCode);
                new UserApi().savePhone(phoneNumber);
                new UserApi().savePhoneCode(dynamicCode);
//                    String enNewPassword = MD5Utils.toMD5(password);
                String enNewPassword = SHA1.encode(password);
                final User_RegisterNew req = new User_RegisterNew(enNewPassword);
                sendRequest(mContext, mApiContext, req);
                if (req.getReturnCode() == ApiCode.SUCCESS) {
                    Api_USER_RegisterResp logInfo = req.getResponse();
                    handleLoginSuccess(logInfo, phoneNumber, lsn);
                } else {
                    mHandler.post(() -> {
                        if (lsn != null) {
                            lsn.onComplete(false, false,
                                    req.getReturnCode(),
                                    req.getReturnMessage());
                        }
                    });

                }
            } catch (final Exception e) {
                mHandler.post(() -> {
                    if (lsn != null) {
                        lsn.onComplete(false, false,
                                ErrorCode.STATUS_NETWORK_EXCEPTION,
                                e.getMessage());
                    }
                });
                e.printStackTrace();
            }
        };
        NetThreadManager.getInstance().execute(requestThread);
    }

    /**
     * 处理登录成功后的事件
     *
     * @param logInfo
     * @param lsn
     */
    private void handleLoginSuccess(Api_USER_RegisterResp logInfo,
                                    String phoneNumber,
                                    final OnResponseListener<Boolean> lsn) throws Exception {
        userService.getUserInfo(logInfo.uid);
        SPUtils.setUid(mContext, logInfo.uid);
        SPUtils.setMobilePhone(mContext, phoneNumber);

        userId = logInfo.uid;
        userToken = logInfo.token;

        mApiContext.setToken(logInfo.token);
        mApiContext.setUserId(logInfo.uid);
        DBInterface.instance().initDbHelp(mContext, (int) logInfo.uid);

        //TODO 引入新的api，暂时使用老的登陆，兼容老版本，后续自己维护
        UserApi userApi = new UserApi();
        userApi.saveLoginToken(logInfo.token);
        userApi.savePhone(phoneNumber);
        userApi.saveUserId(logInfo.uid);
        userApi.saveDtk(dtk, certStr);

        //刷新其他的NM
        refreshOtherNetManager();

        User_GetUserInfoByUserId getUserReq = new User_GetUserInfoByUserId(userId);

        String apns = SPUtils.getRegisterApnsToken(YHYBaseApplication.getInstance());
        if (!TextUtils.isEmpty(apns)){
            userApi.bindUserAndPush(new SaveMsgRelevanceReq(apns, phoneNumber, Rom.isEmui() ? 1 : 0), null).execAsync();
        }
        Track_GetHistoryPedometerInfo pedReq = new Track_GetHistoryPedometerInfo();

        if (getUserReq.getReturnCode() == ApiCode.SUCCESS && getUserReq.getResponse() != null) {
            UserInfo userInfo = UserInfo.deserialize(getUserReq.getResponse().serialize());
            DBManager.getInstance(mContext).release();
            DBManager.getInstance(mContext).saveUserInfo(userInfo);

            User user = new User();
            user.setUserId(userId);
            user.setAvatar(userInfo.avatar);
            user.setNickname(userInfo.nickname);
            user.setName(userInfo.name);
            user.setSignature(userInfo.signature);
            user.setProvinceCode(userInfo.provinceCode);
            user.setProvince(userInfo.province);
            user.setCityCode(userInfo.cityCode);
            user.setCity(userInfo.city);
            user.setAreaCode(userInfo.areaCode);
            user.setArea(userInfo.area);
            user.setBirthday(userInfo.birthday);
            user.setHasMainPage(userInfo.isHasMainPage);
            user.setFrontCover(userInfo.frontCover);
            user.setGender(userInfo.gender);
            user.setSportHobby(userInfo.sportHobby);
            user.setUserId(userInfo.userId);
            user.setAvatar(userInfo.avatar);
            user.setAge(userInfo.age);
            user.setLiveStation(userInfo.liveStation);
            user.setOptions(userInfo.options);
            user.setVip(userInfo.vip);
            userService.saveUserInfo(user);

            //获取登录用户信息后存储到im数据库
            DBInterface.instance().insertOrUpdateUser(ProtoBuf2JavaBean.getUserEntity(getUserReq.getResponse()));
            SPUtils.setUserHomePage(mContext, userInfo.isHasMainPage);
        }

        if (pedReq.getReturnCode() == ApiCode.SUCCESS && pedReq.getResponse() != null) {
            //成功处理计步器步数
            doPedReqSuccessHandle(pedReq.getResponse());
        }

        mHandler.post(() -> {
            EventBus.getDefault().post(new EvBusUserLoginState(EvBusUserLoginState.LOGIN_STATE));
            if (lsn != null) {
                lsn.onComplete(true, false, ErrorCode.STATUS_OK,
                        null);
            }
        });
    }

    private void doPedReqSuccessHandle(Api_TRACK_PedometerHistoryResultList response) {
        List<Api_TRACK_PedometerHistoryResult> pedometerHistoryResultList = response.pedometerHistoryResultList;
        if (pedometerHistoryResultList != null) {
            List<PedometerHistoryResult> resultList = new ArrayList<>();
            for (int i = 0; i < pedometerHistoryResultList.size(); i++) {
                PedometerHistoryResult result = new PedometerHistoryResult();
                result.stepDate = pedometerHistoryResultList.get(i).stepDate;
                result.stepCounts = pedometerHistoryResultList.get(i).stepCounts;
                result.targetSteps = pedometerHistoryResultList.get(i).targetSteps;
                result.fats = pedometerHistoryResultList.get(i).fats;
                result.calorie = pedometerHistoryResultList.get(i).calorie;
                result.distance = pedometerHistoryResultList.get(i).distance;
                resultList.add(result);
            }

            StepDBManager.SaveAndLoadWalkDataInfoListTask mSaveWalkDataTask = new StepDBManager.SaveAndLoadWalkDataInfoListTask(StepDBManager.DataChange2(resultList),
                    walkDataDailies -> {
//                            if (mHandler != null) {
//                                Message.obtain(mHandler, StepFragment2.MSG_GET_DB_DATA_OK, walkDataDailies)
//                                        .sendToTarget();
//                            }
                    });
            mSaveWalkDataTask.execute();
        }

    }

    private void cleanUserLogInfo() {
        if (mApiContext != null) {
            mApiContext.setToken(null);
            mApiContext.setUserId(-1);
            mApiContext.setTokenExpireTime(-1);
        }
    }

    /**
     * 账号密码登录
     *
     * @param phoneNumber
     * @param password
     * @param lsn
     */
    public void doLoginByPassword(final String phoneNumber,
                                  final String password,
                                  final OnResponseListener<Boolean> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        Runnable requestThread = () -> {
            try {
//                    String enNewPassword = MD5Utils.toMD5(password);
                String enNewPassword = SHA1.encode(password);
//                    String enNewPassword = password;
                Log.e("minrui", "enNewPassword=" + enNewPassword);
                final User_Login req = new User_Login(phoneNumber, enNewPassword);
                sendRequest(mContext, mApiContext, req);
                if (req.getReturnCode() == ApiCode.SUCCESS) {
                    Api_USER_LoginResp logInfo = req.getResponse();
                    handleLoginSuccess(logInfo, phoneNumber, lsn);
                } else {
                    mHandler.post(() -> {
                        if (lsn != null) {
                            lsn.onComplete(false, false,
                                    req.getReturnCode(),
                                    req.getReturnMessage());
                        }
                    });

                }
            } catch (final Exception e) {
                mHandler.post(() -> {
                    if (lsn != null) {
                        lsn.onComplete(false, false,
                                ErrorCode.STATUS_NETWORK_EXCEPTION,
                                e.getMessage());
                    }
                });

            }
        };
        NetThreadManager.getInstance().execute(requestThread);
    }

    /**
     * 账号动态验证码登录
     *
     * @param lsn
     */
    public void doLoginByDynamicCode(final String phoneNumber,
                                     final String dynamicCode,
                                     final OnResponseListener<Boolean> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        Runnable requestThread = () -> {
            try {
                mApiContext.setPhoneNumberAndDynamic(phoneNumber, dynamicCode);
                new UserApi().savePhone(phoneNumber);
                new UserApi().savePhoneCode(dynamicCode);
                final User_DynamicPasswordRegisterOrLogin req = new User_DynamicPasswordRegisterOrLogin();
                sendRequest(mContext, mApiContext, req);
                if (req.getReturnCode() == ApiCode.SUCCESS) {
                    Api_USER_LoginResp logInfo = req.getResponse();
                    handleLoginSuccess(logInfo, phoneNumber, lsn);
                } else {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (lsn != null) {
                                lsn.onComplete(false, false,
                                        req.getReturnCode(),
                                        req.getReturnMessage());
                            }
                        }
                    });

                }
            } catch (final Exception e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (lsn != null) {
                            lsn.onComplete(false, false,
                                    ErrorCode.STATUS_NETWORK_EXCEPTION,
                                    e.getMessage());
                        }
                    }
                });

            }
        };
        NetThreadManager.getInstance().execute(requestThread);
    }

    /**
     * 处理登录成功后的事件
     *
     * @param logInfo
     * @param lsn
     */
    private void handleLoginSuccess(Api_USER_LoginResp logInfo,
                                    String phoneNumber,
                                    final OnResponseListener<Boolean> lsn) throws Exception {
        userService.getUserInfo(logInfo.uid);
        SPUtils.setUid(mContext, logInfo.uid);
        if (!StringUtil.isEmpty(phoneNumber)) {
            SPUtils.setMobilePhone(mContext, phoneNumber);
        }

        userId = logInfo.uid;
        userToken = logInfo.token;

        mApiContext.setToken(logInfo.token);
        mApiContext.setUserId(logInfo.uid);

        //TODO 引入新的api，暂时使用老的登陆，兼容老版本，后续自己维护
        UserApi userApi = new UserApi();
        userApi.saveLoginToken(logInfo.token);
        userApi.savePhone(phoneNumber);
        userApi.saveUserId(logInfo.uid);
        userApi.saveDtk(dtk, certStr);

        DBInterface.instance().initDbHelp(mContext, (int) logInfo.uid);
        //刷新其他的NM
        refreshOtherNetManager();

        User_GetUserInfoByUserId getUserReq = new User_GetUserInfoByUserId(userId);
//        SellerAdmin_CheckWhiteList getCheckWhiteReq = new SellerAdmin_CheckWhiteList();

        String apns = SPUtils.getRegisterApnsToken(YHYBaseApplication.getInstance());
        if (!TextUtils.isEmpty(apns)){
            userApi.bindUserAndPush(new SaveMsgRelevanceReq(apns, phoneNumber, Rom.isEmui() ? 1 : 0), null).execAsync();
        }
        User_GetWapLoginToken reqWeb = new User_GetWapLoginToken();

        Trademanager_GetUserAssets uaReq = new Trademanager_GetUserAssets();
        Track_GetHistoryPedometerInfo pedReq = new Track_GetHistoryPedometerInfo();
        sendRequest(mContext, mApiContext, new BaseRequest[]{getUserReq, /*getCheckWhiteReq,*/ reqWeb, uaReq, pedReq});
        if (uaReq.getReturnCode() == ApiCode.SUCCESS && uaReq.getResponse() != null) {
            SPUtils.setMyFans(mContext, uaReq.getResponse().fansNumber);
            SPUtils.setMyAttentions(mContext, uaReq.getResponse().followNumber);
            SPUtils.setCouponCount(mContext, uaReq.getResponse().voucherCount);
            SPUtils.setScore(mContext, uaReq.getResponse().point);
        }

        if (getUserReq.getReturnCode() == ApiCode.SUCCESS && getUserReq.getResponse() != null) {
            UserInfo userInfo = UserInfo.deserialize(getUserReq.getResponse().serialize());

            //获取登录用户信息后存储到im数据库
            DBInterface.instance().insertOrUpdateUser(ProtoBuf2JavaBean.getUserEntity(getUserReq.getResponse()));
            DBManager.getInstance(mContext).release();
            DBManager.getInstance(mContext).saveUserInfo(userInfo);

            User user = new User();
            user.setUserId(userId);
            user.setAvatar(userInfo.avatar);
            user.setNickname(userInfo.nickname);
            user.setName(userInfo.name);
            user.setSignature(userInfo.signature);
            user.setProvinceCode(userInfo.provinceCode);
            user.setProvince(userInfo.province);
            user.setCityCode(userInfo.cityCode);
            user.setCity(userInfo.city);
            user.setAreaCode(userInfo.areaCode);
            user.setArea(userInfo.area);
            user.setBirthday(userInfo.birthday);
            user.setHasMainPage(userInfo.isHasMainPage);
            user.setFrontCover(userInfo.frontCover);
            user.setGender(userInfo.gender);
            user.setSportHobby(userInfo.sportHobby);
            user.setUserId(userInfo.userId);
            user.setAvatar(userInfo.avatar);
            user.setAge(userInfo.age);
            user.setLiveStation(userInfo.liveStation);
            user.setOptions(userInfo.options);
            user.setVip(userInfo.vip);
            userService.saveUserInfo(user);
            SPUtils.setUserHomePage(mContext, userInfo.isHasMainPage);
        }
        if (reqWeb.getReturnCode() == ApiCode.SUCCESS && reqWeb.getResponse() != null) {
            SPUtils.setWebUserToken(mContext,
                    reqWeb.getResponse().value, logInfo.expire);
            userApi.saveWebToken(reqWeb.getResponse().value);
        }
//        if (getCheckWhiteReq.getReturnCode() == ApiCode.SUCCESS && getCheckWhiteReq.getResponse() != null) {
//            SPUtils.setIsInWhiteList(mContext, getCheckWhiteReq.getResponse().value ? 1 : 0);
//        }

        if (pedReq.getReturnCode() == ApiCode.SUCCESS && pedReq.getResponse() != null) {
            //成功处理
            doPedReqSuccessHandle(pedReq.getResponse());
        }
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                EventBus.getDefault().post(new EvBusUserLoginState(EvBusUserLoginState.LOGIN_STATE));
                if (lsn != null) {
                    lsn.onComplete(true, true,
                            ErrorCode.STATUS_OK,
                            null);
                }
            }
        });
    }

    /**
     * 密码重置
     *
     * @param phoneNumber
     * @param newPassword
     * @param lsn
     */
    public void doResetPassword(final String phoneNumber,
                                final String dynamicCode,
                                final String newPassword,
                                final OnResponseListener<Boolean> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        Runnable requestThread = new Runnable() {
            @Override
            public void run() {
                try {
                    mApiContext.setPhoneNumberAndDynamic(phoneNumber, dynamicCode);
                    new UserApi().savePhone(phoneNumber);
                    new UserApi().savePhoneCode(dynamicCode);
//                    String enNewPassword = MD5Utils.toMD5(newPassword);
                    //change encode psw by shenwenjie
                    String enNewPassword = SHA1.encode(newPassword);

                    final User_ResetPassword req = new User_ResetPassword(enNewPassword);
                    sendRequest(mContext, mApiContext, req);
                    if (req.getReturnCode() == ApiCode.SUCCESS) {

                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (lsn != null) {
                                    lsn.onComplete(true, true,
                                            ErrorCode.STATUS_OK,
                                            null);
                                }
                            }
                        });
                    } else {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (lsn != null) {
                                    lsn.onComplete(false, false,
                                            req.getReturnCode(),
                                            req.getReturnMessage());
                                }
                            }
                        });

                    }
                } catch (final Exception e) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (lsn != null) {
                                lsn.onComplete(false, false,
                                        ErrorCode.STATUS_NETWORK_EXCEPTION,
                                        e.getMessage());
                            }
                        }
                    });

                }
            }
        };
        NetThreadManager.getInstance().execute(requestThread);
    }

    /**
     * 密码修改
     *
     * @param oldPassword
     * @param newPassword
     * @param lsn
     */
    public void doChangePassword(final String oldPassword,
                                 final String newPassword,
                                 final OnResponseListener<Boolean> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        Runnable requestThread = new Runnable() {
            @Override
            public void run() {
                try {
//                    String enOldPassword = MD5Utils.toMD5(oldPassword);
//                    String enNewPassword = MD5Utils.toMD5(newPassword);
                    //  change encode pwd by shenwenjie
                    String enOldPassword = SHA1.encode(oldPassword);
                    String enNewPassword = SHA1.encode(newPassword);

                    final User_ChangePassword req = new User_ChangePassword(enOldPassword, enNewPassword);
                    sendRequest(mContext, mApiContext, req);
                    if (req.getReturnCode() == ApiCode.SUCCESS) {

                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (lsn != null) {
                                    lsn.onComplete(true, true,
                                            ErrorCode.STATUS_OK,
                                            null);
                                }
                            }
                        });
                    } else {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (lsn != null) {
                                    lsn.onComplete(false, false,
                                            req.getReturnCode(),
                                            req.getReturnMessage());
                                }
                            }
                        });

                    }
                } catch (final Exception e) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (lsn != null) {
                                lsn.onComplete(false, false,
                                        ErrorCode.STATUS_NETWORK_EXCEPTION,
                                        e.getMessage());
                            }
                        }
                    });

                }
            }
        };
        NetThreadManager.getInstance().execute(requestThread);
    }

    /**
     * 注销
     *
     * @param lsn
     */
    public void doLogout(final OnResponseListener<Boolean> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        Runnable requestThread = new Runnable() {
            @Override
            public void run() {
                logoutInner(lsn);
            }
        };
        NetThreadManager.getInstance().execute(requestThread);
    }

    private void logoutInner(final OnResponseListener<Boolean> lsn) {
        try {
            final User_Logout req = new User_Logout();
            sendRequest(mContext, mApiContext, req);

            if (req.getReturnCode() == ApiCode.SUCCESS) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (lsn != null) {
                            lsn.onComplete(true, req.getResponse().value,
                                    ErrorCode.STATUS_OK, null);
                        }
                    }
                });
            } else {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (lsn != null) {
                            lsn.onComplete(false, false, req.getReturnCode(),
                                    req.getReturnMessage());
                        }
                    }
                });

            }
        } catch (final Exception e) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (lsn != null) {
                        lsn.onComplete(false, false,
                                ErrorCode.STATUS_IO_EXCEPTION, e.getMessage());
                    }
                }
            });

            e.printStackTrace();
        }
    }

    /**
     * 登出后改变登录状态
     */
    public void changeLogoutStatus() {
        initEnvInfo();

        if (mSnsNetManager != null) {
            mSnsNetManager.updateApicontextAndHandler(mApiContext);
        }

        if (mCommonNetManager != null) {
            mCommonNetManager.updateApicontextAndHandler(mApiContext);
        }

        if (mTravelNetManager != null) {
            mTravelNetManager.updateApicontextAndHandler(mApiContext);
        }

        if (mTradeManagerNetManager != null) {
            mTradeManagerNetManager.updateApicontextAndHandler(mApiContext);
        }
    }

    /**
     * 三方绑定
     *
     * @param mobile
     * @param outType
     * @param lsn
     * @throws JSONException
     */
    public void doAppThirdPartyBind(final String mobile,
                                    final String code,
                                    final String unionId,
                                    final String openId,
                                    final String outType,
                                    final String accessToken,
                                    final OnResponseListener<Boolean> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        Runnable requestThread = new Runnable() {
            @Override
            public void run() {
                try {
                    mApiContext.setPhoneNumberAndDynamic(mobile, code);
                    new UserApi().savePhone(mobile);
                    new UserApi().savePhoneCode(code);
                    Api_USER_AppThirdPartyBind params = new Api_USER_AppThirdPartyBind();
                    params.mobile = mobile;
                    if (!StringUtil.isEmpty(unionId)) {
                        params.outId = unionId;
                        params.openId = openId;
                    } else {
                        params.outId = openId;
                    }
                    params.openId = openId;
                    params.token = accessToken;
                    params.outType = outType;
                    final User_AppThirdPartyBind req = new User_AppThirdPartyBind(params);

                    sendRequest(mContext, mApiContext, req);
                    if (req.getReturnCode() == ApiCode.SUCCESS) {
                        Api_USER_RegisterResp logInfo = req.getResponse();
                        handleLoginSuccess(logInfo, mobile, lsn);
                    } else {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (lsn != null) {
                                    lsn.onComplete(false, false,
                                            req.getReturnCode(),
                                            req.getReturnMessage());
                                }
                            }
                        });

                    }
                } catch (final Exception e) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (lsn != null) {
                                lsn.onComplete(false, false,
                                        ErrorCode.STATUS_NETWORK_EXCEPTION,
                                        e.getMessage());
                            }
                        }
                    });

                }
            }
        };
        NetThreadManager.getInstance().execute(requestThread);
    }
    // TODO
    /***************************************************** 帐号相关 end **************************************/

    //****************************************联系人相关接口 BEGIN ********************************//

    /**
     * 获取游客列表
     *
     * @param lsn
     */
    public void doGetVisitorList(final long userId,
                                 final OnResponseListener<UserContact_ArrayResp> lsn) {
        if (mCommonNetManager == null) {
            return;
        }
        mCommonNetManager.doGetVisitorList(userId, lsn);
    }

    /**
     * 新增游客
     *
     * @param userContact
     */
    public void doAddOrUpdateVisitorInfo(UserContact userContact, OnResponseListener<UserContact> lsn) {
        if (mCommonNetManager == null) {
            return;
        }
        mCommonNetManager.doAddOrUpdateVisitorInfo(userContact, lsn);
    }

    /**
     * 增加证件
     *
     * @param userContact
     * @param lsn
     */
    public void doAddCertificate(final UserContact userContact, final OnResponseListener<Boolean> lsn) {
        if (mCommonNetManager == null) {
            return;
        }
        mCommonNetManager.doAddCertificate(userContact, lsn);
    }

    /**
     * 更新证件
     *
     * @param userContact
     * @param lsn
     */
    public void doUpdateCertificate(final UserContact userContact, final OnResponseListener<Boolean> lsn) {
        if (mCommonNetManager == null) {
            return;
        }
        mCommonNetManager.doUpdateCertificate(userContact, lsn);
    }

    /**
     * 删除证件
     *
     * @param contactId
     * @param type
     * @param lsn
     */
    public void doDeleteCertificate(final long contactId, final String type, final OnResponseListener<Boolean> lsn) {
        if (mCommonNetManager == null) {
            return;
        }
        mCommonNetManager.doDeleteCertificate(contactId, type, lsn);
    }

    /**
     * 更新游客
     *
     * @param userContact
     * @param lsn
     */
    public void doUpdateVisitorInfo(UserContact userContact, OnResponseListener<Boolean> lsn) {
        if (mCommonNetManager == null) {
            return;
        }
        mCommonNetManager.doUpdateVisitorInfo(userContact, lsn);
    }

    /**
     * 删除游客
     *
     * @param userContact
     */
    public void doDeleteVisitor(UserContact userContact, OnResponseListener<Boolean> lsn) {
        if (mCommonNetManager == null) {
            return;
        }
        mCommonNetManager.doDeleteVisitor(userContact, lsn);
    }

    /**
     * 获取地址列表
     *
     * @param userId
     * @param lsn
     */
    public void doGetAddressList(Long userId, OnResponseListener<MyAddress_ArrayResp> lsn) {

        if (mCommonNetManager == null) {
            return;
        }
        mCommonNetManager.doGetAddressList(userId, lsn);
    }

    /**
     * 新增地址信息
     *
     * @param addressContentInfo
     */
    public void doAddOrUpdateAddressInfo(MyAddressContentInfo addressContentInfo, OnResponseListener<MyAddressContentInfo> lsn) {
        if (mCommonNetManager == null) {
            return;
        }
        mCommonNetManager.doAddOrUpdateAddressInfo(addressContentInfo, lsn);
    }

    /**
     * 修改地址信息
     *
     * @param addressContentInfo
     */
    public void doUpdateAddressInfo(MyAddressContentInfo addressContentInfo, OnResponseListener<Boolean> lsn) {
        if (mCommonNetManager == null) {
            return;
        }
        mCommonNetManager.doUpdateAddressInfo(addressContentInfo, lsn);
    }

    /**
     * 删除地址
     *
     * @param addressId
     */
    public void doDeleteAddress(Long addressId, OnResponseListener<Boolean> lsn) {
        if (mCommonNetManager == null) {
            return;
        }
        mCommonNetManager.doDeleteAddress(addressId, lsn);
    }

    /**
     * 查询游记列表
     *
     * @param pageIndex
     * @param pageSize
     * @param lsn
     */
    public void doGetTravelSpecialListPage(final int pageIndex, final int pageSize, final OnResponseListener<TravelSpecialInfoList> lsn) {
        if (mSnsNetManager == null) {
            return;
        }
        mSnsNetManager.doGetTravelSpecialListPage(pageIndex, pageSize, lsn);
    }

    /**
     * 获取游记详情
     *
     * @param id
     * @param lsn
     */
    public void doGetTravelSpecialDetail(final long id, final OnResponseListener<TravelSpecialInfo> lsn) {
        if (mSnsNetManager == null) {
            return;
        }
        mSnsNetManager.doGetTravelSpecialDetail(id, lsn);
    }

    /**
     * 屏蔽人或者动态
     *
     * @param shieldType
     * @param id
     * @param lsn
     */
    public void doAddUserShield(final String shieldType, long id, final OnResponseListener<Boolean> lsn) {
        if (mSnsNetManager == null) {
            return;
        }
        mSnsNetManager.doAddUserShield(shieldType, id, lsn);
    }

    /**
     * 条件查询目的列表
     *
     * @param param
     * @param lsn
     * @throws JSONException
     */
    public void doQueryDestinationList(DestinationQuery param, final OnResponseListener<DestinationList> lsn) {
        if (mCommonNetManager == null) {
            return;
        }
        try {
            mCommonNetManager.doQueryDestinationList(param, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 条件查询目的列表
     *
     * @param type
     * @param lsn
     * @throws JSONException
     */
    public void doQueryDestinationTree(String type, final OnResponseListener<DestinationList> lsn) {
        if (mCommonNetManager == null) {
            return;
        }
        try {
            mCommonNetManager.doQueryDestinationTree(type, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * 查询评价信息列表
     *
     * @param param
     * @param lsn
     */
    public void doGetRateInfoPageList(RateInfoQuery param,
                                      final OnResponseListener<RateInfoList> lsn) {
        if (mSnsNetManager == null) {
            return;
        }
        try {
            mSnsNetManager.doGetRateInfoPageList(param, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询评价维度
     *
     * @param outType 外部类型： HOTEL: 酒店，VIEW：景区，LINE：线路，LOCAL：同城活动，PRODUCT：实物商品
     * @param lsn
     * @throws JSONException
     */
    public void doGetRateDimensionList(String outType,
                                       final OnResponseListener<DimensionList> lsn) {
        if (mSnsNetManager == null) {
            return;
        }
        try {
            mSnsNetManager.doGetRateDimensionList(outType, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询评价数量
     *
     * @param param
     * @param lsn
     * @throws JSONException
     */
    public void doGetRateCountByOutId(RateCountQuery param,
                                      final OnResponseListener<RateCountInfo> lsn) {
        if (mSnsNetManager == null) {
            return;
        }
        try {
            mSnsNetManager.doGetRateCountByOutId(param, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询评价条件
     *
     * @param id
     * @param outType 外部类型： HOTEL: 酒店，VIEW：景区，LINE：线路，LOCAL：同城活动，PRODUCT：实物商品
     * @param lsn
     * @throws JSONException
     */
    public void doGetRateCaseList(long id,
                                  String outType,
                                  final OnResponseListener<RateCaseList> lsn) {
        if (mSnsNetManager == null) {
            return;
        }
        try {
            mSnsNetManager.doGetRateCaseList(id, outType, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询实物商品评价信息列表
     *
     * @param param
     * @param lsn
     * @throws JSONException
     */
    public void doGetProductRateInfoPageList(ProductRateInfoQuery param,
                                             final OnResponseListener<ProductRateInfoList> lsn) {
        if (mSnsNetManager == null) {
            return;
        }
        try {
            mSnsNetManager.doGetProductRateInfoPageList(param, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //****************************************地址相关接口 END ********************************//

    //****************************************发现 BEGIN ********************************//

    /**
     * 获取直播详情页评论数据
     */
    public void doGetLiveDetailCommment(long subjectId, String outType, int pageIndex, int pageSize, final OnResponseListener<CommentInfoList> data) {
        if (mSnsNetManager == null) {
            return;
        }
        mSnsNetManager.doGetCommentList(subjectId, outType, pageIndex, pageSize, data);
    }

    /**
     * 获取直播详情页点赞人列表
     */
    public void doGetLiveDetailAppraisePeople(long outId, String outType, int pageIndex, int pageSize, final OnResponseListener<SupportUserInfoList> data) {
        if (mSnsNetManager == null) {
            return;
        }
        mSnsNetManager.doGetApraiseList(outId, outType, pageIndex, pageSize, data);
    }

    //****************************************发现 END ********************************//


    /**
     * 获取发布直播--搜索标签，默认数据列表
     *
     * @param lsn 数据回调
     */
    public void doGetLiveAddTopicLabels(final String outType, final int pageIndex, final int pageSize, final OnResponseListener<ComUserAndTagInfoList> lsn) {
        if (mSnsNetManager == null) {
            return;
        }
        mSnsNetManager.doSearchTag(outType, pageIndex, pageSize, lsn);
    }


    /**
     * 根据输入内容搜索标签
     *
     * @param searchValue
     * @param pageIndex
     * @param pageSize
     * @param lsn
     */
    public void doFindTagList(final String searchValue, final int pageIndex, final int pageSize, final OnResponseListener<ComTagInfoList> lsn) {
        if (mSnsNetManager == null) {
            return;
        }
        mSnsNetManager.doSearchTagByName(searchValue, pageIndex, pageSize, lsn);
    }
    //****************************************周边和发现 BEGIN ********************************//
    //TODO

    /**
     * 标签列表（直播，动态，俱乐部列表筛选）
     *
     * @param outType
     * @param pageIndex
     * @param pageSize
     * @param lsn
     */
    public void doGetTagInfoPageByOutType(final String outType, final int pageIndex, final int pageSize, final OnResponseListener<ComTagInfoList> lsn) {
        if (mSnsNetManager == null) {
            return;
        }
        mSnsNetManager.doGetTagInfoPageByOutType(outType, pageIndex, pageSize, lsn);
    }

    /**
     * 根据用户ID分页获取俱乐部的列表
     *
     * @param pageIndex
     * @param pageSize
     * @param lsn
     */
    public void doGetClubList(final long userId,
                              final int pageIndex,
                              final int pageSize,
                              final OnResponseListener<ClubInfoList> lsn) {
        if (mSnsNetManager == null) {
            return;
        }
        mSnsNetManager.doGetClubList(userId, pageIndex, pageSize, lsn);
    }

    /**
     * 根据类型分页获取俱乐部的列表
     *
     * @param type
     * @param pageIndex
     * @param pageSize
     * @param lsn
     */
    public void doGetClubListByType(final long type,
                                    final int pageIndex,
                                    final int pageSize,
                                    final OnResponseListener<ClubInfoList> lsn) {
        if (mSnsNetManager == null) {
            return;
        }
        mSnsNetManager.doGetClubListByType(type, pageIndex, pageSize, lsn);
    }

    /**
     * 俱乐部详情
     *
     * @param clubId
     * @param lsn
     */
    public void doGetClubDetail(final long clubId,
                                final OnResponseListener<SnsActivePageInfoList> lsn) {
        if (mSnsNetManager == null) {
            return;
        }
        mSnsNetManager.doGetClubDetail(clubId, lsn);
    }

    /**
     * 分页获取俱乐部的成员列表
     *
     * @param pageIndex
     * @param pageSize
     * @param clubId
     * @param lsn
     */
    public void doGetActiveByOutUserIdPage(final int pageIndex,
                                           final int pageSize,
                                           final long clubId,
                                           final OnResponseListener<ClubMemberInfoList> lsn) {
        if (mSnsNetManager == null) {
            return;
        }
        mSnsNetManager.doGetActiveByOutUserIdPage(pageIndex, pageSize, clubId, lsn);
    }

    /**
     * 分页获取俱乐部的活动列表
     *
     * @param pageIndex
     * @param pageSize
     * @param clubId
     * @param lsn
     */
    public void doGetActiveListByOutIdPage(final int pageIndex,
                                           final int pageSize,
                                           final long clubId,
                                           final OnResponseListener<SnsRimInfoList> lsn) {
        if (mSnsNetManager == null) {
            return;
        }
        mSnsNetManager.doGetActiveListByOutIdPage(pageIndex, pageSize, clubId, lsn);
    }

    /**
     * 分页获取俱乐部的动态列表
     *
     * @param pageIndex
     * @param pageSize
     * @param clubId
     * @param lsn
     */
    public void doGetSubjectListByClubId(final int pageIndex,
                                         final int pageSize,
                                         final long clubId,
                                         final OnResponseListener<SubjectInfoList> lsn) {
        if (mSnsNetManager == null) {
            return;
        }
        mSnsNetManager.doGetSubjectListByClubId(pageIndex, pageSize, clubId, lsn);
    }

    /**
     * 获取某人的动态列表
     *
     * @param pageIndex
     * @param pageSize
     * @param userId
     * @param lsn
     */
    public void doGetSubjectList(final long userId, final int pageIndex,
                                 final int pageSize,
                                 final OnResponseListener<SubjectInfoList> lsn) {
        if (mSnsNetManager == null) {
            return;
        }
        mSnsNetManager.doGetSubjectList(pageIndex, pageSize, userId, lsn);
    }

    /**
     * 根据活动id获取活动详情
     *
     * @param id
     * @param lsn
     */
    public void doGetActiveDetail(final long id, boolean isVip,
                                  final OnResponseListener<SnsActivePageInfo> lsn) {
        if (mSnsNetManager == null) {
            return;
        }
        mSnsNetManager.doGetActiveDetail(id, isVip, lsn);
    }

    /**
     * 分页获取活动参与的用户列表
     *
     * @param pageIndex
     * @param pageSize
     * @param id
     * @param lsn
     */
    public void doGetActiveJoinMemberList(final int pageIndex,
                                          final int pageSize,
                                          final long id,
                                          final OnResponseListener<SnsActiveMemberPageInfo> lsn) {
        if (mSnsNetManager == null) {
            return;
        }
        mSnsNetManager.doGetActiveJoinMemberList(pageIndex, pageSize, id, lsn);
    }

    /**
     * 加入俱乐部
     *
     * @param clubId
     * @param lsn
     */
    public void doJoinClubMember(final long clubId,
                                 final OnResponseListener<ClubMemberInfo> lsn) {
        if (mSnsNetManager == null) {
            return;
        }
        mSnsNetManager.doJoinClubMember(clubId, lsn);
    }

    /**
     * 退出俱乐部
     *
     * @param clubId
     * @param lsn
     */
    public void doExitClubMember(final long clubId,
                                 final OnResponseListener<ClubMemberInfo> lsn) {
        if (mSnsNetManager == null) {
            return;
        }
        mSnsNetManager.doExitClubMember(clubId, lsn);
    }

    /**
     * 发表新的动态
     *
     * @param subjectInfo
     * @param lsn
     */
    public void doPublishNewSubject(final SubjectDynamic subjectInfo,
                                    final OnResponseListener<SubjectDetail> lsn) {
        if (mSnsNetManager == null) {
            return;
        }
        mSnsNetManager.doPublishNewSubject(subjectInfo, lsn);
    }

    /**
     * 对动态、直播点赞或者取消点赞
     *
     * @param type 1点赞 0取消点赞
     * @param lsn
     */
    public void doPrasizeForum(final long outId,
                               final String outType,
                               final int type,
                               final OnResponseListener<ComSupportInfo> lsn) {
        if (mSnsNetManager == null) {
            return;
        }
        mSnsNetManager.doPrasizeForum(outId, outType, type, lsn);
    }

    /**
     * 对帖子、直播进行评论
     *
     * @param info
     * @param lsn
     */
    public void doPostComment(final CommetDetailInfo info,
                              final OnResponseListener<CommentInfo> lsn) {
        if (mSnsNetManager == null) {
            return;
        }
        mSnsNetManager.doPostComment(info, lsn);
    }

    /**
     * 删除自己发表的动态、直播
     *
     * @param forumId
     * @param lsn
     */
    public void doDeleteForum(final long forumId,
                              final OnResponseListener<Boolean> lsn) {
        if (mSnsNetManager == null) {
            return;
        }
        mSnsNetManager.doDeleteForum(forumId, lsn);
    }

    /**
     * 删除自己发表的直播
     *
     * @param forumId
     * @param lsn
     */
    public void doDeleteLive(final long forumId,
                             final OnResponseListener<Boolean> lsn) {
        if (mSnsNetManager == null) {
            return;
        }
        mSnsNetManager.doDeleteLive(forumId, lsn);
    }

    /**
     * 删除自己发表的评论和自己发表的帖子、直播下的评论
     *
     * @param commentId
     * @param type      类型 DYNAMICCOM动态 LIVECOM直播
     * @param lsn
     */
    public void doDeleteComment(long commentId,
                                String type,
                                OnResponseListener<Boolean> lsn) {
        if (mSnsNetManager == null) {
            return;
        }
        mSnsNetManager.doDeleteComment(commentId, type, lsn);
    }

    /**
     * 删除自己发表的直播下的评论
     *
     * @param reployId  评论id
     * @param subjectId 帖子的UID
     * @param lsn
     */
    public void doDeleteComment(long reployId,
                                long subjectId,
                                OnResponseListener<Boolean> lsn) {
        if (mSnsNetManager == null) {
            return;
        }
        mSnsNetManager.doDeleteComment(reployId, subjectId, lsn);
    }

    /**
     * 获取我的直播列表
     *
     * @param lsn
     */
    public void doGetLiveListByUserId(final LiveRecordListUserIdQuery api_live_liveRecordListUserIdQuery,
                                      final OnResponseListener<LiveRecordAPIPageResult> lsn) {
        if (mSnsNetManager == null) {
            return;
        }
        mSnsNetManager.doGetLiveListByUserID(api_live_liveRecordListUserIdQuery, lsn);
    }

    /**
     * 获取直播列表(根据标签)
     *
     * @param tagId
     * @param pageIndex
     * @param pageSize
     * @param lsn
     */
    public void doGetLiveListByTagId(final long tagId,
                                     final int pageIndex,
                                     final int pageSize,
                                     final OnResponseListener<SubjectInfoList> lsn) {
        if (mSnsNetManager == null) {
            return;
        }
        mSnsNetManager.doGetLiveListByTagId(tagId, pageIndex, pageSize, lsn);
    }

    /**
     * 获取直播详情
     *
     * @param liveId
     * @param lsn
     */
    public void doGetLiveDetail(final long liveId,
                                final OnResponseListener<SubjectInfo> lsn) {
        if (mSnsNetManager == null) {
            return;
        }

        mSnsNetManager.doGetLiveDetail(liveId, lsn);
    }

    /**
     * 获取动态辛勤
     *
     * @param dynamicId
     * @param lsn
     */
    public void deGetDynamicDetail(final long dynamicId, final OnResponseListener<SubjectInfo> lsn) {
        if (mSnsNetManager == null) {
            return;
        }

        mSnsNetManager.doGetDynamicDetail(dynamicId, lsn);
    }

    /**
     * 发表新的直播
     *
     * @param subjectInfo
     * @param lsn
     */
    public void doPublishNewSubjectLive(final SubjectLive subjectInfo,
                                        final OnResponseListener<SubjectDetail> lsn) {
        if (mSnsNetManager == null) {
            return;
        }
        mSnsNetManager.doPublishNewSubjectLive(subjectInfo, lsn);
    }

    /**
     * 首页活动列表以及首页搜索：按标题搜索
     *
     * @param pageIndex
     * @param pageSize
     * @param title
     * @param lsn
     */
    public void doGetActiveLstByTitlePage(final int pageIndex,
                                          final int pageSize,
                                          final String title,
                                          final OnResponseListener<SnsActivePageInfoList> lsn) {
        if (mSnsNetManager == null) {
            return;
        }
        mSnsNetManager.doGetActiveListByTitlePage(pageIndex, pageSize, title, lsn);
    }

    /**
     * 首页俱乐部列表以及首页搜索： 按名称
     *
     * @param pageIndex
     * @param pageSize
     * @param clubName
     * @param lsn
     */
    public void doGetClubInfoListPage(final int pageIndex,
                                      final int pageSize,
                                      final String clubName,
                                      final OnResponseListener<ClubInfoList> lsn) {
        if (mSnsNetManager == null) {
            return;
        }
        mSnsNetManager.doGetClubInfoListPage(pageIndex, pageSize, clubName, lsn);
    }
    //TODO
    //****************************************周边和发现 END ********************************//

    //****************************************消息中心 Begin ********************************//
    //TODO

    //TODO
    //****************************************消息中心 END ********************************//

    //****************************************旅游 Begin ********************************//
    //TODO

    /**
     * 根据类型、出发地、目的地分页筛选线路列表
     *
     * @param lsn
     */
    public void doGetTravelList(QueryLineDTO params,
                                final OnResponseListener<LineInfoResult> lsn) {
        if (mTravelNetManager == null) {
            return;
        }

        try {
            mTravelNetManager.doGetTravelList(params, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * 省目的地分页筛选线路列表
     *
     * @param lsn
     */
    public void GetDestProvincePageContent(QueryLineDTO params,
                                           final OnResponseListener<MyTripContent> lsn) {
        if (mTravelNetManager == null) {
            return;
        }

        try {
            mTravelNetManager.GetDestProvincePageContent(params, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取线路详情
     *
     * @param lineId
     * @param lsn
     */
    public void doGetLinesDetail(final long lineId,
                                 final OnResponseListener<LineDetail> lsn) {
        if (mTravelNetManager == null) {
            return;
        }
        mTravelNetManager.doGetLinesDetail(lineId, lsn);
    }

    /**
     * 获取商家详情
     *
     * @param merchantId
     * @param type       商家类型 HOTEL:酒店 RESTARUANT:饭店 SCENIC:景区
     * @param lsn
     */
    public void doGetMerchantDetail(final long merchantId,
                                    final String type,
                                    final OnResponseListener<EntityShopDetail> lsn) {
        if (mTravelNetManager == null) {
            return;
        }
        mTravelNetManager.doGetMerchantDetail(merchantId, type, lsn);
    }

    /**
     * 获取大咖个人主页信息
     *
     * @param bigshotId
     * @param lsn
     */
    public void doGetBigShotDetail(final long bigshotId,
                                   final OnResponseListener<TravelKa> lsn) {
        if (mTravelNetManager == null) {
            return;
        }
        mTravelNetManager.doGetBigShotDetail(bigshotId, lsn);
    }

    /**
     * 根据城市ID获取目的地市的推荐内容
     *
     * @param cityCode
     * @param lsn
     */
    public void doGetDestinationCityDetail(String cityCode,
                                           List<String> codes,
                                           final OnResponseListener<DestCityPageContent> lsn) {
        if (mTravelNetManager == null) {
            return;
        }
        mTravelNetManager.doGetDestinationCityDetail(cityCode, codes, lsn);
    }

    /**
     * 获取所有目的地列表
     *
     * @param lsn
     */
    public void doGetDestList(final OnResponseListener<CityList> lsn) {
        if (mCommonNetManager == null) {
            return;
        }
        mCommonNetManager.doGetDestList(lsn);
    }

    /**
     * 获取省市的介绍页 获取概况 民俗 消费 获取贴士 亮点 必买推荐
     *
     * @param destCode
     * @param code
     * @param lsn
     */
    public void doGetDestinationIntroductionInfo(String destCode,
                                                 String code,
                                                 final OnResponseListener<IntroduceInfo> lsn) {
        if (mTravelNetManager == null) {
            return;
        }
        mTravelNetManager.doGetDestinationIntroductionInfo(destCode, code, lsn);
    }

    /**
     * 获取景区主题和区域
     *
     * @param lsn
     */
    public void doGetScenicSpotThemes(final OnResponseListener<ScenicFilter> lsn) {
        if (mTravelNetManager == null) {
            return;
        }
        mTravelNetManager.doGetScenicSpotThemes(lsn);
    }

    /**
     * 获取酒店列表
     *
     * @param params
     * @param lsn
     * @throws JSONException
     */
    public void doGetHotelList(QueryTermsDTO params,
                               final OnResponseListener<HotelInfoResult> lsn) {
        if (mTravelNetManager == null) {
            return;
        }
        try {
            mTravelNetManager.doGetHotelList(params, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取酒店设施列表
     *
     * @param params
     * @param lsn
     * @throws JSONException
     */
    public void doGetHotelFacilities(QueryFacilitiesDTO params,
                                     final OnResponseListener<HotelFacilityResult> lsn) {
        if (mTravelNetManager == null) {
            return;
        }
        try {
            mTravelNetManager.doGetHotelFacilities(params, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取景区详情
     *
     * @param id
     * @param lsn
     */
    public void doGetScenicOldDetail(final long id,
                                     final OnResponseListener<ScenicDetail> lsn) {
        if (mTravelNetManager == null) {
            return;
        }
        mTravelNetManager.doScenicSpotDetail(id, lsn);
    }

    /**
     * 获取景区详情
     *
     * @param id
     * @param lsn
     */
    public void doGetScenicDetail(final long id,
                                  final OnResponseListener<ScenicDetail> lsn) {
        if (mTravelNetManager == null) {
            return;
        }
        mTravelNetManager.doScenicDetail(id, lsn);
    }

    /**
     * 通过资源ID，获取资源关联的商品列表
     *
     * @param resourceId
     * @param resourceType
     * @param lsn
     */
    public void doGetItemsByResourceId(final long resourceId,
                                       final String resourceType,
                                       final OnResponseListener<ItemVOResult> lsn) {
        if (mTravelNetManager == null) {
            return;
        }
        mTravelNetManager.doGetItemsByResourceId(resourceId, resourceType, lsn);
    }

    /**
     * 根据筛选条件筛选酒店
     *
     * @param params
     * @param lsn
     */
//    public void doGetHotelList(QueryHotelDTO params,
//                               final OnResponseListener<HotelInfoResult> lsn) {
//        if (mTravelNetManager == null) {
//            return;
//        }
//        try {
//            mTravelNetManager.doGetHotelList(params, lsn);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * 获取酒店详情
     *
     * @param id
     * @param lsn
     */
    public void doGetHotelDetail(final long id,
                                 final OnResponseListener<HotelDetail> lsn) {
        if (mTravelNetManager == null) {
            return;
        }
        mTravelNetManager.doGetHotelDetail(id, lsn);
    }

    /**
     * 获取线路筛选列表
     *
     * @param lsn
     */
    public void doGetLineFilter(final OnResponseListener<LineFilter> lsn) {
        if (mTravelNetManager == null) {
            return;
        }
        mTravelNetManager.doGetLineFilter(lsn);
    }

    /**
     * 获取酒店筛选列表
     *
     * @param lsn
     */
    public void doGetHotelFilter(final OnResponseListener<HotelFilter> lsn) {
        if (mTravelNetManager == null) {
            return;
        }
        mTravelNetManager.doGetHotelFilter(lsn);
    }

    /**
     * 获取图片列表
     *
     * @param queryPictureDTO
     * @param lsn
     */
    public void doGetQueryPicture(QueryPictureDTO queryPictureDTO,
                                  final OnResponseListener<PictureVOResult> lsn) {
        if (mTravelNetManager == null) {
            return;
        }
        try {
            mTravelNetManager.doGetQueryPicture(queryPictureDTO, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 达人页获取达人基本信息
     *
     * @param talentId
     * @param lsn
     * @throws JSONException
     */
    public void doGetTalentDetail(long talentId, final OnResponseListener<TalentInfo> lsn) {
        if (mTravelNetManager == null) {
            return;
        }
        try {
            mTravelNetManager.doGetTalentDetail(talentId, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取店铺信息
     *
     * @param talentId
     * @param lsn
     * @throws JSONException
     */
    public void doQueryMerchantInfo(long talentId, final OnResponseListener<Merchant> lsn) {
        if (mTravelNetManager == null) {
            return;
        }
        try {
            mTravelNetManager.doQueryMerchantInfo(talentId, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取店铺列表
     *
     * @param query
     * @param lsn
     * @throws JSONException
     */
    public void doQueryMerchantList(MerchantQuery query, final OnResponseListener<MerchantList> lsn) {
        if (mTravelNetManager == null) {
            return;
        }
        try {
            mTravelNetManager.doQueryMerchantList(query, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * 服务类型获取达人列表
     *
     * @param query
     * @param lsn
     * @throws JSONException
     */
    public void doQueryTalentList(TalentQuery query, final OnResponseListener<TalentInfoList> lsn) {
        if (mTravelNetManager == null) {
            return;
        }
        try {
            mTravelNetManager.doQueryTalentList(query, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取商品列表-新
     *
     * @param param
     * @param lsn
     * @throws JSONException
     */
    public void doGetItemList(QueryTermsDTO param, final OnResponseListener<ShortItemsResult> lsn) {
        if (mTravelNetManager == null) {
            return;
        }
        try {
            mTravelNetManager.doGetItemList(param, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取商品列表详情
     *
     * @param itemId
     * @param lsn
     */
    public void doGetNormItemDetail(long itemId, final OnResponseListener<MerchantItem> lsn) {
        if (mTravelNetManager == null) {
            return;
        }
        try {
            mTravelNetManager.doGetNormItemDetail(itemId, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据展位码获取商品列表
     *
     * @param code
     * @param lsn
     * @throws JSONException
     */

    public void doGetItemListByCode(CodeQueryDTO code, final OnResponseListener<ShortItemsResult> lsn) {
        if (mTravelNetManager == null) {
            return;
        }
        try {
            mTravelNetManager.doGetItemListByCode(code, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取同城活动详情
     *
     * @param itemId
     * @param lsn
     * @throws JSONException
     */
    public void doGetCityActivityDetail(long itemId, final OnResponseListener<CityActivityDetail> lsn) {
        if (mTravelNetManager == null) {
            return;
        }
        try {
            mTravelNetManager.doGetCityActivityDetail(itemId, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取线路商品详情
     *
     * @param itemId
     * @param lsn
     * @throws JSONException
     */
    public void doGetLineDetailByItemId(long itemId, final OnResponseListener<LineItemDetail> lsn) {
        if (mTravelNetManager == null) {
            return;
        }
        try {
            mTravelNetManager.doGetLineDetailByItemId(itemId, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //TODO
    //****************************************旅游 END ********************************//

    //****************************************我的 BEGIN ********************************//
    //TODO

    /**
     * 获取用户的信息
     *
     * @param lsn
     */
    public void doGetUserProfile(long userId, final OnResponseListener<UserInfo> lsn) {
        if (mCommonNetManager == null) {
            return;
        }

        mCommonNetManager.doGetUserProfile(userId, lsn);
    }

    /**
     * 更新用户信息
     *
     * @param userInfo
     * @param lsn
     */
    public void doUpdateUserProfile(final User userInfo,
                                    final OnResponseListener<Boolean> lsn) {
        if (mCommonNetManager == null) {
            return;
        }

        try {
            mCommonNetManager.doUpdateUserProfile(userInfo, lsn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 反馈
     *
     * @param content
     * @param contact
     * @param lsn
     */
    public void doFeedback(final String content,
                           final String contact,
                           final OnResponseListener<Boolean> lsn) {
        if (mCommonNetManager == null) {
            return;
        }

        mCommonNetManager.doFeedback(content, contact, lsn);
    }


    /**
     * 获取系统配置
     *
     * @param lsn
     */
    public void doGetSystemConfig(OnResponseListener<SystemConfig> lsn) {
        if (mCommonNetManager == null) {
            return;
        }
        mCommonNetManager.doGetSystemConfig(lsn);
    }

    /**
     * 获取广告位信息
     *
     * @param code
     * @param lsn
     */
    public void doGetBooth(final String code,
                           final OnResponseListener<Booth> lsn) {
        if (mCommonNetManager == null) {
            return;
        }

        mCommonNetManager.doGetBooth(code, lsn);
    }

    /**
     * 获取多个资源位信息
     *
     * @param codes
     * @param lsn
     */
    public void doGetMultiBooths(final List<String> codes,
                                 final OnResponseListener<BoothList> lsn) {
        if (mCommonNetManager == null) {
            return;
        }

        mCommonNetManager.doGetMultiBooths(codes, lsn);
    }

    /**
     * 查询图标
     *
     * @param lsn
     */
    public void doGetIcon(final OnResponseListener<ComIconList> lsn) {
        if (mCommonNetManager == null) {
            return;
        }

        mCommonNetManager.doGetIcon(lsn);
    }

    /**
     * 获取目录标签
     *
     * @param type
     * @param lsn
     */
    public void doGetCategoryTags(final String type,
                                  final OnResponseListener<CategoryTagList> lsn) {
        if (mCommonNetManager == null) {
            return;
        }

        mCommonNetManager.doGetCategoryTags(type, lsn);
    }

    /**
     * 专题列表
     *
     * @param code
     * @param lsn
     */
    public void doGetPageBooth(String code,
                               PageInfo pageInfo,
                               final OnResponseListener<Booth> lsn) {
        if (mCommonNetManager == null) {
            return;
        }

        try {
            mCommonNetManager.doGetPageBooth(code, pageInfo, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 目的地列表
     *
     * @param lsn
     */
    public void doGetDestListNew(final OnResponseListener<CityList> lsn) {
        if (mCommonNetManager == null) {
            return;
        }

        try {
            mCommonNetManager.doGetDestListNew(lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取筛选条件列表
     *
     * @param code
     * @param lsn
     * @throws JSONException
     */
    public void doGetQueryFilter(String code, final OnResponseListener<QueryTerm> lsn) {
        if (mCommonNetManager == null) {
            return;
        }

        try {
            mCommonNetManager.doGetQueryFilter(code, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化APP
     *
     * @param lsn
     * @throws JSONException
     */
    public void doInitApp(final OnResponseListener<AppDefaultConfig> lsn) {
        if (mCommonNetManager == null) {
            return;
        }

        try {
            mCommonNetManager.doInitApp(lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 导览首页加载新数据
     *
     * @param lsn
     * @throws JSONException
     */
    public void doLoadHomeTourGuideData(final OnResponseListener<HomeTourGuide> lsn) {
        if (mCommonNetManager == null) {
            return;
        }

        try {
            mCommonNetManager.doLoadHomeTourGuideData(lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //TODO
    //****************************************我的 END ********************************//

    //****************************************其他 BEGIN ********************************//
    //TODO

    /**
     * 上传日志
     *
     * @param files
     * @param lsn
     */
    public void doUploadLogs(List<String> files, OnResponseListener<List<String>> lsn) {
        if (mCommonNetManager == null) {
            return;
        }
        mCommonNetManager.doUploadLogs(files, lsn);
    }

    /**
     * 上传图片
     *
     * @param files
     * @param lsn
     */
    public void doUploadImages(List<String> files, OnResponseListener<List<String>> lsn) {
        if (mCommonNetManager == null) {
            return;
        }
        mCommonNetManager.doUpdateImages(files, lsn);
    }

    /**
     * 上传视频
     *
     * @param files
     * @param lsn
     */
    public void doUploadVideos(List<String> files, OnResponseListener<List<String>> lsn) {
        if (mCommonNetManager == null) {
            return;
        }
        mCommonNetManager.doUpdateVideos(files, lsn);
    }

    /**
     * 在线升级
     *
     * @param lsn
     */
    public void doGetOnlineUpgrade(OnResponseListener<OnlineUpgrade> lsn) {
        if (mCommonNetManager == null) {
            return;
        }
        mCommonNetManager.doGetOnlineUpgrade(lsn);
    }

    /**
     * 获取投诉选项
     *
     * @param lsn
     * @throws JSONException
     */
    public void doGetComplaintOptions(final OnResponseListener<ComplaintOptionInfoList> lsn) {
        if (mCommonNetManager == null) {
            return;
        }

        try {
            mCommonNetManager.doGetComplaintOptions(lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 投诉
     *
     * @param complaintInfo
     * @param lsn
     * @throws JSONException
     */
    public void doSubmitComplaint(ComplaintInfo complaintInfo, final OnResponseListener<Boolean> lsn) {
        if (mCommonNetManager == null) {
            return;
        }

        try {
            mCommonNetManager.doSubmitComplaint(complaintInfo, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据code获取目的地信息
     *
     * @param cityCode
     * @param type
     * @param lsn
     * @throws JSONException
     */
    public void doGetDestinationByCode(int cityCode,
                                       String type,
                                       final OnResponseListener<Destination> lsn) {
        if (mCommonNetManager == null) {
            return;
        }

        try {
            mCommonNetManager.doGetDestinationByCode(cityCode, type, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取WTK
     *
     * @param lsn
     * @throws JSONException
     */
    public void doGetWapLoginToken(final OnResponseListener<String> lsn) {
        if (mCommonNetManager == null) {
            return;
        }

        try {
            mCommonNetManager.doGetWapLoginToken(lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化周边景点页面数据
     *
     * @param cityCode
     * @param lsn
     */
    public void doInitArroundScenicList(String cityCode,
                                        final OnResponseListener<ArroundScenicInitListResult> lsn) {
        if (mCommonNetManager == null) {
            return;
        }

        try {
            mCommonNetManager.doInitArroundScenicList(cityCode, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取景区列表
     *
     * @param params
     * @param lsn
     */
    public void doGetScenicList(QueryTermsDTO params,
                                final OnResponseListener<ScenicInfoResult> lsn) {
        if (mTravelNetManager == null) {
            return;
        }

        try {
            mTravelNetManager.doGetScenicList(params, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取周边城市景区列表
     *
     * @param cityCode
     * @param lsn
     */
    public void doGetArroundScenicList(String cityCode,
                                       final OnResponseListener<ScenicInfoResult> lsn) {
        if (mTravelNetManager == null) {
            return;
        }

        try {
            mTravelNetManager.doGetArroundScenicList(cityCode, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取酒店推荐列表
     *
     * @param param
     * @param lsn
     * @throws JSONException
     */
    public void doGetHotelListByCode(CodeQueryDTO param, final OnResponseListener<HotelInfoResult> lsn) {
        if (mTravelNetManager == null) {
            return;
        }

        try {
            mTravelNetManager.doGetHotelListByCode(param, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //TODO
    //****************************************其他 END ********************************//

    /**
     * 获取商品详细信息，用于下单页面
     *
     * @param itemId 商品ID
     * @param lsn
     */
    public void doGetItem(long itemId, OnResponseListener<ItemVO> lsn) {
        if (mTradeManagerNetManager == null) {
            return;
        }
        mTravelNetManager.doGetItem(itemId, lsn);
    }

    /**
     * 通过商品类型，获取商品列表
     *
     * @param param
     * @param lsn
     */
    public void doGetItems(QueryItemDTO param, OnResponseListener<ItemVOResult> lsn) {
        if (mTravelNetManager == null) {
            return;
        }
        try {
            mTravelNetManager.doGetItems(param, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据关键字获取搜索结果(景区酒店)
     *
     * @param params
     * @param lsn
     */
    public void doGetItemskeywordSearch(final KeywordSearchDTO params,
                                        final OnResponseListener<SearchResultList> lsn) {
        if (mTravelNetManager == null) {
            return;
        }
        try {
            mTravelNetManager.doGetItemskeywordSearch(params, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 提交评价
     *
     * @param param
     * @param lsn
     */
    public void doPostRate(PostRateParam param,
                           final OnResponseListener<Boolean> lsn) {
        if (mTradeManagerNetManager == null) {
            return;
        }
        try {
            mTradeManagerNetManager.doPostRate(param, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取拆单结果及订单金额
     *
     * @param param
     * @param lsn
     * @throws JSONException
     */
    public void doGetOrderPrice(OrderPriceQueryDTO param,
                                final OnResponseListener<TmCreateOrderContext> lsn) {
        if (mTradeManagerNetManager == null) {
            return;
        }
        try {
            mTradeManagerNetManager.doGetOrderPrice(param, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //****************************************交易 BEGIN ********************************//
    //TODO

    /**
     * 关闭订单
     *
     * @param itemId
     * @param lsn
     */
    public void doBuyerCloseOrder(long itemId, String reason, final OnResponseListener<Boolean> lsn) {
        if (mTradeManagerNetManager == null) {
            return;
        }
        mTradeManagerNetManager.doBuyerCloseOrder(itemId, reason, lsn);
    }

    /**
     * 买家确认收货(普通商品)
     *
     * @param bizOrderId
     * @param lsn
     */
    public void doBuyerConfirmGoodsDeliveried(long bizOrderId, final OnResponseListener<Boolean> lsn) {
        if (mTradeManagerNetManager == null) {
            return;
        }
        mTradeManagerNetManager.doBuyerConfirmGoodsDeliveried(bizOrderId, lsn);
    }

    /**
     * 创建订单
     *
     * @param param
     * @param lsn
     * @throws JSONException
     */
    public void doCreateOrder(TmCreateOrderParam param, final OnResponseListener<TmCreateOrderResultTO> lsn) {
        if (mTradeManagerNetManager == null) {
            return;
        }
        try {
            //打点
            mTradeManagerNetManager.doCreateOrder(param, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 批量创建订单
     *
     * @param param
     * @param lsn
     */
    public void doCreateBatchOrder(CreateBatchOrderParam param, final OnResponseListener<CreateOrderResultTOList> lsn) {
        if (mTradeManagerNetManager == null) {
            return;
        }
        try {
            mTradeManagerNetManager.doCreateBatchOrder(param, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取下单页所需上下文
     *
     * @param id
     * @param lsn
     */
    public void doGetCreateOrderContext(long id, final OnResponseListener<TmCreateOrderContext> lsn) {
        if (mTradeManagerNetManager == null) {
            return;
        }
        mTradeManagerNetManager.doGetCreateOrderContext(id, lsn);
    }

    /**
     * 通过订单id查询包裹信息
     *
     * @param id
     * @param lsn
     */
    public void doQueryPackageBuyOrder(long id, final OnResponseListener<PackageDetailResult> lsn) {
        if (mTradeManagerNetManager == null) {
            return;
        }
        mTradeManagerNetManager.doQueryPackageBuyOrder(id, lsn);
    }

    /**
     * 通过买家id和订单id查询交易订单(包含子订单)
     *
     * @param bizOrderId
     * @param lsn
     */
    public void doQueryBizOrderInfoForBuyer(long bizOrderId, final OnResponseListener<OrderDetailResult> lsn) {
        if (mTradeManagerNetManager == null) {
            return;
        }
        try {
            mTradeManagerNetManager.doQueryBizOrderInfoForBuyer(bizOrderId, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过订单id查询评价列表
     *
     * @param id
     * @param lsn
     */
    public void doQueryRateBuyOrder(long id, final OnResponseListener<OrderDetailResult> lsn) {
        if (mTradeManagerNetManager == null) {
            return;
        }
        mTradeManagerNetManager.doQueryRateBuyOrder(id, lsn);
    }

    /**
     * 积分商城获取下单页所需上下文
     *
     * @param paramForPointMall
     * @param lsn
     */
    public void doGetCreateOrderContextForPointMall(CreateOrderContextParamForPointMall paramForPointMall, final OnResponseListener<CreateOrderContextForPointMall> lsn) {
        if (mTradeManagerNetManager == null) {
            return;
        }
        try {
            mTradeManagerNetManager.doGetCreateOrderContextForPointMall(paramForPointMall, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取行程详情
     *
     * @param id
     * @param lsn
     * @throws JSONException
     */
    public void doGetUserRouteDetail(long id, final OnResponseListener<TmUserRoute> lsn) {
        if (mTradeManagerNetManager == null) {
            return;
        }
        mTradeManagerNetManager.doGetUserRouteDetail(id, lsn);
    }

    /**
     * 分页查询交易订单，买家维度
     *
     * @param typeCode
     * @param statusCode 状态code 全部：ALL，代付款：WAITING_PAY，已完成：FINISH，已关闭：CLOSED，处理中：PENDING，待发货：WAITING_DELIVERY，待出行：WAITING_DEPART，待收货：SHIPPING，已取消：CANCEL，已退款：REFUNDED
     * @param pageIndex
     * @param pageSize
     * @param lsn
     * @throws JSONException
     */
    public void doGetOrderList(String typeCode,
                               String statusCode,
                               int pageIndex,
                               int pageSize,
                               final OnResponseListener<TmOrderList> lsn) {
        if (mTradeManagerNetManager == null) {
            return;
        }
        mTradeManagerNetManager.doGetOrderList(typeCode, statusCode, pageIndex, pageSize, lsn);
    }

    /**
     * 订单详情
     *
     * @param id
     * @param lsn
     * @throws JSONException
     */
    public void doGetOrderDetail(long id, final OnResponseListener<TmOrderDetail> lsn) {
        if (mTradeManagerNetManager == null) {
            return;
        }
        mTradeManagerNetManager.doGetOrderDetail(id, lsn);
    }

    /**
     * 订单详情
     *
     * @param id
     * @param lsn
     * @throws JSONException
     */
    public void doGetOrderDetailAndItemType(long id, final OnResponseListener<TmOrderDetail> lsn) {
        if (mTradeManagerNetManager == null) {
            return;
        }
        mTradeManagerNetManager.doGetOrderDetailAndItemType(id, lsn);
    }

    /**
     * 取消订单参数包装
     *
     * @param bizOrderId
     * @param closeReasonId
     * @param lsn
     */
    public void doBuyerCloseOrderWithReason(long bizOrderId, int closeReasonId, final OnResponseListener<Boolean> lsn) {
        if (mTradeManagerNetManager == null) {
            return;
        }
        mTradeManagerNetManager.doBuyerCloseOrderWithReason(bizOrderId, closeReasonId, lsn);
    }


    /**
     * 获取关闭订单理由列表
     *
     * @param orderBizType
     * @param lsn
     */
    public void doGetCloseOrderReasonList(String orderBizType, final OnResponseListener<TmCloseOrderReasonItemList> lsn) {
        if (mTradeManagerNetManager == null) {
            return;
        }
        mTradeManagerNetManager.doGetCloseOrderReasonList(orderBizType, lsn);
    }

    /**
     * 获取支付的信息
     *
     * @param bizOrderId 订单ID
     * @param payChannel 支付渠道:PAY_ALI_SDK：支付宝SDK支付
     */
    public void doGetPayInfoV2(long bizOrderId, String payChannel, OnResponseListener<TmPayInfo> lsn) {
        if (mTradeManagerNetManager == null) {
            return;
        }
        mTradeManagerNetManager.doGetPayInfoV2(bizOrderId, payChannel, lsn);
    }

    /**
     * 获取微信支付的信息
     *
     * @param bizOrderId     订单ID
     * @param payChannel     支付渠道:PAY_WX：微信SDK支付
     * @param spbillCreateIp 用户端ip
     * @param lsn
     */
    public void doGetWxPayInfo(long bizOrderId, String payChannel, String spbillCreateIp, OnResponseListener<TmWxPayInfo> lsn) {
        if (mTradeManagerNetManager == null) {
            return;
        }
        mTradeManagerNetManager.doGetWxPayInfo(bizOrderId, payChannel, spbillCreateIp, lsn);
    }

    /**
     * 获取支付状态信息
     *
     * @param bizOrderId 订单ID
     * @param lsn
     */
    public void doGetPayStatusInfo(long bizOrderId, OnResponseListener<TmPayStatusInfo> lsn) {
        if (mTradeManagerNetManager == null) {
            return;
        }
        mTradeManagerNetManager.doGetPayStatusInfo(bizOrderId, lsn);
    }
    //TODO
    //****************************************交易 END ********************************//

    /**
     * 获取会员详情
     *
     * @param lsn
     */
    public void doGetMemberDetail(final OnResponseListener<MemberDetail> lsn) {
        if (mCommonNetManager == null) {
            return;
        }
        mCommonNetManager.doGetMemberDetail(lsn);
    }

    /**
     * 获取会员购买详情页信息
     *
     * @param lsn
     */
    public void doGetMemberPurchuseDetail(final OnResponseListener<MemberPurchauseDetail> lsn) {
        if (mCommonNetManager == null) {
            return;
        }
        mCommonNetManager.doGetMemberPurchuseDetail(lsn);
    }

    /**
     * 获取线路的出发日期
     *
     * @param id
     * @param lsn
     */
    public void doGetLineSkuDateList(final long itemId, final long id, final OnResponseListener<SkuListWithStartDate> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        Runnable requestThread = new Runnable() {
            @Override
            public void run() {
                try {
                    final Items_GetItem reqDateList = new Items_GetItem(itemId);
                    final Trademanager_GetCreateOrderContext reqContext = new Trademanager_GetCreateOrderContext(id);

                    sendRequest(mContext, mApiContext, new BaseRequest[]{reqDateList, reqContext});
                    TmCreateOrderContext valueContext = null;
                    if (reqContext.getResponse() != null) {
                        try {
                            valueContext = TmCreateOrderContext.deserialize(reqContext.getResponse().serialize());
                        } catch (JSONException e) {
                            if (lsn != null) {
                                lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                            }
                            e.printStackTrace();
                        }
                    }

                    ItemVO valueItem = null;
                    if (reqDateList.getResponse() != null) {
                        try {
                            valueItem = ItemVO.deserialize(reqDateList.getResponse().serialize());
                        } catch (JSONException e) {
                            if (lsn != null) {
                                lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                            }
                            e.printStackTrace();
                        }
                    }
                    SkuListWithStartDate value = new SkuListWithStartDate();
                    if (valueContext != null) {
                        value.tmCreateOrderContext = valueContext;
                    }
                    if (valueItem != null) {
                        value.itemVO = valueItem;
                        Object[] objects = PickSku.getPickSkus(value.tmCreateOrderContext.startTime, value.itemVO.skuList);
                        value.endDate = (long) objects[0];
                        value.skuMap = (HashMap<String, PickSku>) objects[1];
                    }
                    if (lsn != null) {
                        lsn.onComplete(true, value, ErrorCode.STATUS_OK, reqContext.getReturnMessage());
                    }
                } catch (final Exception e) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (lsn != null) {
                                lsn.onComplete(false, null,
                                        ErrorCode.STATUS_NETWORK_EXCEPTION,
                                        e.getMessage());
                            }
                        }
                    });
                    e.printStackTrace();
                }
            }
        };
        NetThreadManager.getInstance().execute(requestThread);
    }


    /**
     * 获取Im服务器地址和信息
     */
    public void doGetImServerAddrs(final OnResponseListener<Api_USER_ImLoginResp> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        Runnable requestThread = new Runnable() {
            @Override
            public void run() {
                try {
                    Api_USER_ImLoginResp loginResp = null;
                    User_ImLogin imLogin = new User_ImLogin();
                    sendRequest(mContext, mApiContext, imLogin);
                    if (imLogin.getResponse() != null) {
                        try {
                            loginResp = Api_USER_ImLoginResp.deserialize(imLogin.getResponse().serialize());
                            if (lsn != null) {
                                lsn.onComplete(true, loginResp, ErrorCode.STATUS_OK, imLogin.getReturnMessage());
                            }
                        } catch (JSONException e) {
                            if (lsn != null) {
                                lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                            }
                        }
                    } else {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, null);
                        }
                    }
                } catch (final Exception e) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (lsn != null) {
                                lsn.onComplete(false, null,
                                        ErrorCode.STATUS_NETWORK_EXCEPTION,
                                        e.getMessage());
                            }
                        }
                    });
                }
            }
        };
        NetThreadManager.getInstance().execute(requestThread);
    }

    /**
     * 批量获取userinfo
     */
    public void doGetUserInfoById(final List<Long> ids, final OnResponseListener<Api_USER_UserInfo_ArrayResp> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        Runnable requestThread = new Runnable() {
            @Override
            public void run() {
                try {
                    Api_USER_UserInfo_ArrayResp resp = null;
                    Api_USER_UserBatchQuery query = new Api_USER_UserBatchQuery();
                    long[] temp = new long[ids.size()];
                    for (int i = 0; i < ids.size(); i++) {
                        temp[i] = ids.get(i);
                    }
                    query.userIds = temp;
                    User_GetUserInfoByUserIdList request = new User_GetUserInfoByUserIdList(query);
                    sendRequest(mContext, mApiContext, request);
                    resp = request.getResponse();
                    if (resp != null) {
                        if (lsn != null) {
                            lsn.onComplete(true, resp, ErrorCode.STATUS_OK, request.getReturnMessage());
                        }
                    }
                } catch (final Exception e) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (lsn != null) {
                                lsn.onComplete(false, null,
                                        ErrorCode.STATUS_NETWORK_EXCEPTION,
                                        e.getMessage());
                            }
                        }
                    });
                }
            }
        };
        NetThreadManager.getInstance().execute(requestThread);
    }

    /**
     * 获取景区详情
     *
     * @param id
     * @param lsn
     */
    public void getScenicDetail(final long id, final OnResponseListener<ScenicDetail> lsn) {
        if (mTravelNetManager == null) {
            return;
        }
        mTravelNetManager.doScenicSpotDetail(id, lsn);
    }

    /**
     * 获取酒店详情
     *
     * @param id
     * @param lsn
     */
    public void getHotelDetail(final long id, final OnResponseListener<HotelDetail> lsn) {
        if (mTravelNetManager == null) {
            return;
        }
        mTravelNetManager.doGetHotelDetail(id, lsn);
    }

    /**
     * 根据入离时间获取酒店详情的房型列表
     *
     * @param startTimeMill
     * @param endTimeMill
     * @param lsn
     */
    public void getHotelRoomTypeList(long id, long startTimeMill, long endTimeMill, OnResponseListener<RoomsResult> lsn) {
        QueryRoomDTO params = new QueryRoomDTO();
        params.hotelId = id;
        params.startTime = startTimeMill;
        params.endTime = endTimeMill;
        if (mTravelNetManager == null) {
            return;
        }
        try {
            mTravelNetManager.doGetRoomList(params, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取详情的图片列表（酒店，景区）
     *
     * @param id
     * @param onResponseListener
     */
    public void getDetailPicList(long id, String type, int pageIndex, int pageNo,
                                 OnResponseListener<PictureVOResult> onResponseListener) {
        QueryPictureDTO queryPictureDTO = new QueryPictureDTO();
        queryPictureDTO.outId = id;
        queryPictureDTO.outType = type;
        queryPictureDTO.pageNo = pageIndex;
        queryPictureDTO.pageSize = pageNo;
        if (mTravelNetManager == null) {
            return;
        }
        try {
            mTravelNetManager.doGetQueryPicture(queryPictureDTO, onResponseListener);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取详情图片列表的类型（酒店，景区）
     *
     * @param id
     * @param onResponseListener
     */
    public void getDetailPicTypesList(long id, OnResponseListener<SubjectInfo> onResponseListener) {

    }

    /**
     * 取消关注
     *
     * @param followedUserId 被关注人的userId
     * @param lsn
     */
    public void doRemoveFollower(long followedUserId, final OnResponseListener<Boolean> lsn) {
        if (mSnsNetManager == null) {
            return;
        }
        try {
            mSnsNetManager.doRemoveFollower(followedUserId, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加关注
     *
     * @param followedUserId 被关注人的userId
     * @param lsn
     */
    public void doAddFollower(long followedUserId, final OnResponseListener<Boolean> lsn) {
        if (mSnsNetManager == null) {
            return;
        }
        try {
            mSnsNetManager.doAddFollower(followedUserId, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取UGC详情
     *
     * @param ugcId
     * @param lsn
     */
    public void doGetUGCDetail(long ugcId, final OnResponseListener<UgcInfoResult> lsn) {
        if (mSnsNetManager == null) {
            return;
        }
        try {
            mSnsNetManager.doGetUGCDetail(ugcId, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取UGC列表
     *
     * @param pageIndex
     * @param pageSize
     * @param type      查询类型 1,全部,2,关注
     * @param lsn
     */
    public void doGetUGCPageList(final int pageIndex, final int pageSize, int type, final OnResponseListener<UgcInfoResultList> lsn) {
        if (mSnsNetManager == null) {
            return;
        }
        try {
            mSnsNetManager.doGetUGCPageList(pageIndex, pageSize, type, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询用户的UGC列表
     *
     * @param pageIndex
     * @param pageSize
     * @param createId
     * @param lsn
     */
    public void doGetUserUGCPageList(final int pageIndex, final int pageSize, long createId, final OnResponseListener<UgcInfoResultList> lsn) {
        if (mSnsNetManager == null) {
            return;
        }
        try {
            mSnsNetManager.doGetUserUGCPageList(pageIndex, pageSize, createId, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询我的粉丝、关注、ugc数量
     *
     * @param theUserId
     * @param lsn
     */
    public void doQueryUserSnsCountInfo(long theUserId, final OnResponseListener<SnsCountInfo> lsn) {
        if (mSnsNetManager == null) {
            return;
        }
        try {
            mSnsNetManager.doQueryUserSnsCountInfo(theUserId, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 模糊查询话题标题
     *
     * @param pageIndex
     * @param pageSize
     * @param titleLike
     * @param lsn
     */
    public void doSearchTopicTitlePageList(final int pageIndex, final int pageSize, String titleLike, final OnResponseListener<TopicInfoResultList> lsn) {
        if (mSnsNetManager == null) {
            return;
        }
        try {
            mSnsNetManager.doSearchTopicTitlePageList(pageIndex, pageSize, titleLike, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 领取优惠券
     *
     * @param voucherTemplateId
     * @param lsn
     */
    public void doGenerateVoucher(long voucherTemplateId, final OnResponseListener<Boolean> lsn) {
        if (mTradeManagerNetManager == null) {
            return;
        }
        try {
            mTradeManagerNetManager.doGenerateVoucher(voucherTemplateId, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询商品优惠券
     *
     * @param itemId
     * @param lsn
     */
    public void doQueryItemVoucherList(int pageSize, int pageNo, long itemId, final OnResponseListener<VoucherTemplateResultList> lsn) {
        if (mTradeManagerNetManager == null) {
            return;
        }
        try {
            mTradeManagerNetManager.doQueryItemVoucherList(pageSize, pageNo, itemId, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param status   券状态，2未使用，4已使用，5已过期
     * @param pageSize
     * @param pageNo
     * @param lsn
     */
    public void doQueryMyVoucherList(String status, int pageSize, int pageNo, final OnResponseListener<VoucherResultList> lsn) {
        if (mTradeManagerNetManager == null) {
            return;
        }
        try {
            mTradeManagerNetManager.doQueryMyVoucherList(status, pageSize, pageNo, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询订单优惠券
     *
     * @param queryOrderVoucherDTO
     * @param lsn
     */
    public void doQueryOrderVoucherList(Api_TRADEMANAGER_QueryOrderVoucherDTO queryOrderVoucherDTO, final OnResponseListener<OrderVoucherResult> lsn) {
        if (mTradeManagerNetManager == null) {
            return;
        }
        try {
            mTradeManagerNetManager.doQueryOrderVoucherList(queryOrderVoucherDTO, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询店铺优惠券
     *
     * @param sellerId
     * @param lsn
     */
    public void doQuerySellerVoucherList(long sellerId, final OnResponseListener<VoucherTemplateResultList> lsn) {
        if (mTradeManagerNetManager == null) {
            return;
        }
        try {
            mTradeManagerNetManager.doQuerySellerVoucherList(sellerId, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查看话题详情
     *
     * @param lsn
     */
    public void doGetTopicInfo(String topicTitle, final OnResponseListener<TopicInfoResult> lsn) {
        if (mSnsNetManager == null) {
            return;
        }
        try {
            mSnsNetManager.doGetTopicInfo(topicTitle, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查看话题列表
     *
     * @param pageNo
     * @param pageSize
     * @param type     1.推荐 2.全部
     * @param lsn
     */
    public void doGetTopicPageList(int pageNo, int pageSize, int type, long startNum, final OnResponseListener<TopicInfoResultList> lsn) {
        if (mSnsNetManager == null) {
            return;
        }
        try {
            mSnsNetManager.doGetTopicPageList(pageNo, pageSize, type, startNum, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发布UGC
     *
     * @param ugcInfo
     * @param lsn
     */
    public void doAddUGC(Api_SNSCENTER_UgcInfo ugcInfo, final OnResponseListener<Boolean> lsn) {
        if (mSnsNetManager == null) {
            return;
        }
        try {
            mSnsNetManager.doAddUGC(ugcInfo, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除UGC
     *
     * @param ugcId
     * @param lsn
     */
    public void doDelUGC(long ugcId, final OnResponseListener<Boolean> lsn) {
        if (mSnsNetManager == null) {
            return;
        }
        try {
            mSnsNetManager.doDelUGC(ugcId, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询指定用户的粉丝列表
     *
     * @param userId
     * @param pageNo
     * @param pageSize
     * @param lsn
     */
    public void doGetFansList(long userId, int pageNo, int pageSize, final OnResponseListener<FollowerPageListResult> lsn) {
        if (mSnsNetManager == null) {
            return;
        }
        try {
            mSnsNetManager.doGetFansList(userId, pageNo, pageSize, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询指定用户的关注列表
     *
     * @param userId
     * @param pageNo
     * @param pageSize
     * @param lsn
     */
    public void doGetFollowerList(long userId, int pageNo, int pageSize, final OnResponseListener<FollowerPageListResult> lsn) {
        if (mSnsNetManager == null) {
            return;
        }
        try {
            mSnsNetManager.doGetFollowerList(userId, pageNo, pageSize, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取用户各种数量
     *
     * @param lsn
     */
    public void doGetUserAssets(final OnResponseListener<UserAssets> lsn) {
        if (mTradeManagerNetManager == null) {
            return;
        }
        try {
            mTradeManagerNetManager.doGetUserAssets(lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 积分明细查询
     *
     * @param pageNo
     * @param pageSize
     * @param lsn
     */
    public void doPointDetailQuery(int pageNo, int pageSize, final OnResponseListener<PointDetailQueryResult> lsn) {
        if (mTradeManagerNetManager == null) {
            return;
        }
        try {
            mTradeManagerNetManager.doPointDetailQuery(pageNo, pageSize, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 总积分查询
     *
     * @param lsn
     */
    public void doTotalPointQuery(final OnResponseListener<Long> lsn) {
        if (mTradeManagerNetManager == null) {
            return;
        }
        try {
            mTradeManagerNetManager.doTotalPointQuery(lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 每日任务清单查询
     *
     * @param pageNo
     * @param pageSize
     * @param lsn
     */
    public void doDailyTaskQuery(int pageNo, int pageSize, final OnResponseListener<DailyTaskQueryResult> lsn) {
        if (mTradeManagerNetManager == null) {
            return;
        }
        try {
            mTradeManagerNetManager.doDailyTaskQuery(pageNo, pageSize, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取历史步数
     *
     * @param lsn
     * @throws JSONException
     */
    public void doGetHistoryPedometerInfo(final OnResponseListener<PedometerHistoryResultList> lsn) {
        if (mCommonNetManager == null) {
            return;
        }
        try {
            mCommonNetManager.doGetHistoryPedometerInfo(lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取邀请配置信息
     *
     * @param lsn
     */
    public void doGetInviteShareInfo(final OnResponseListener<InviteShareInfo> lsn) {
        if (mCommonNetManager == null) {
            return;
        }
        try {
            mCommonNetManager.doInviteShareInfo(lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取计步器用户信息
     *
     * @param lsn
     * @throws JSONException
     */
    public void doGetPedometerUserInfo(final OnResponseListener<PedometerUserInfo> lsn) throws JSONException {
        if (mCommonNetManager == null) {
            return;
        }
        try {
            mCommonNetManager.doGetPedometerUserInfo(lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 历史步数同步
     *
     * @param syncParamList
     * @param lsn
     * @throws JSONException
     */
    public void doSyncHistoryData(SyncParamList syncParamList, final OnResponseListener<SyncResult> lsn) throws JSONException {
        if (mCommonNetManager == null) {
            return;
        }
        try {
            mCommonNetManager.doSyncHistoryData(syncParamList, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过当前步数获得积分
     *
     * @param stepParam
     * @param lsn
     * @throws JSONException
     */
    public void doGetCurrentPointByStep(StepParam stepParam, final OnResponseListener<StepResult> lsn) {
        if (mCommonNetManager == null) {
            return;
        }
        try {
            mCommonNetManager.doGetCurrentPointByStep(stepParam, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取起步基金
     *
     * @param lsn
     */
    public void doGetStepStartPoint(final OnResponseListener<ReceivePointResult> lsn) {
        if (mCommonNetManager == null) {
            return;
        }
        mCommonNetManager.doGetStepStartPoint(lsn);
    }

    /**
     * 计步器——获取昨日积分
     *
     * @param lsn
     */
    public void doGetStepYesterdayPoint(final OnResponseListener<ReceivePointResult> lsn) {
        if (mCommonNetManager == null) {
            return;
        }
        mCommonNetManager.doGetYesterdayPoint(lsn);
    }

    /**
     * 获取商品详情+店铺信息
     *
     * @param itemId
     * @param lsn
     */
    public void doGetItemAndSellerInfo(long itemId,
                                       final OnResponseListener<MerchantItem> lsn) {
        if (mTradeManagerNetManager == null) {
            return;
        }
        try {
            mTradeManagerNetManager.doGetItemAndSellerInfo(itemId, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询话题下的UGC列表
     *
     * @param pageNo
     * @param pageSize
     * @param lsn
     */
    public void doGetTopicUGCPageList(String topicTitle, int pageNo, int pageSize, final OnResponseListener<TopicDetailResult> lsn) {
        if (mSnsNetManager == null) {
            return;
        }
        try {
            mSnsNetManager.doGetTopicUGCPageList(topicTitle, pageNo, pageSize, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 分享每日步数
     *
     * @param lsn
     */
    public void doShareDailySteps(final OnResponseListener<Long> lsn) {
        if (mTradeManagerNetManager == null) {
            return;
        }
        try {
            mTradeManagerNetManager.doShareDailySteps(lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 验证今天是否已签到
     *
     * @param lsn
     */
    public void doIsSignIn(final OnResponseListener<Boolean> lsn) {
        if (mTradeManagerNetManager == null) {
            return;
        }
        try {
            mTradeManagerNetManager.doIsSignIn(lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询热门话题
     *
     * @param pageIndex
     * @param pageSize
     * @param titleLike
     * @param lsn
     */
    public void doSearchHotTopicPageList(final int pageIndex, final int pageSize, String titleLike, final OnResponseListener<TopicInfoResultList> lsn) {
        if (mSnsNetManager == null) {
            return;
        }
        try {
            mSnsNetManager.doSearchHotTopicPageList(pageIndex, pageSize, titleLike, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询达人广场
     *
     * @param pageIndex
     * @param pageSize
     * @param lsn
     */
    public void doGetAreUgcPageList(final int pageIndex, final int pageSize, final OnResponseListener<UgcInfoResultList> lsn) {
        if (mSnsNetManager == null) {
            return;
        }
        try {
            mSnsNetManager.doGetAreUgcPageList(pageIndex, pageSize, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取邀请配置信息
     *
     * @param lsn
     */
//    public void doInviteShareInfo(final OnResponseListener<InviteShareInfo> lsn) {
//        if (mCommonNetManager == null) {
//            return;
//        }
//        try {
//            mCommonNetManager.doInviteShareInfo(lsn);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * 发布商品
     *
     * @param doPublishService
     * @param lsn
     */
    public void doPublishService(PublishServiceDO doPublishService, final OnResponseListener<Boolean> lsn) {
        if (mTradeManagerNetManager == null) {
            return;
        }
        try {
            mTradeManagerNetManager.doPublishService(doPublishService, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取咨询商品属性
     *
     * @param lsn
     */
    public void doGetConsultItemProperties(final OnResponseListener<ConsultCategoryInfoList> lsn) {
        if (mTradeManagerNetManager == null) {
            return;
        }
        try {
            mTradeManagerNetManager.doGetConsultItemProperties(lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取商品管理页面信息
     *
     * @param params
     * @param lsn
     */
    public void doGetGoodsManagementInfo(ItemQueryParam params, final OnResponseListener<ItemApiResult> lsn) {
        if (mTradeManagerNetManager == null) {
            return;
        }
        try {
            mTradeManagerNetManager.doGetGoodsManagementInfo(params, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取商品详情页面信息
     *
     * @param lsn
     */
    public void doGetGoodsDetailInfo(ItemQueryParam params, long id, final OnResponseListener<ItemApiResult> lsn) {
        if (mTradeManagerNetManager == null) {
            return;
        }
        try {
            mTradeManagerNetManager.doGetGoodsDetailInfo(params, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发布服务白名单
     *
     * @param lsn
     */
    public void doCheckWhiteList(final OnResponseListener<Boolean> lsn) {
        if (mTradeManagerNetManager == null) {
            return;
        }
        try {
            mTradeManagerNetManager.doCheckWhiteList(lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新商品状态
     *
     * @param params
     * @param lsn
     * @throws JSONException
     */
    public void doGetUpdateState(ItemQueryParam params, final OnResponseListener<Boolean> lsn) {
        if (mTradeManagerNetManager == null) {
            return;
        }
        try {
            mTradeManagerNetManager.doGetUpdateState(params, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 分页查询流程订单列表
     *
     * @param params
     * @param lsn
     */
    public void doGetProcessOrderList(Api_TRADEMANAGER_ProcessQueryParam params, final OnResponseListener<ProcessOrderList> lsn) {
        if (mTradeManagerNetManager == null) {
            return;
        }
        try {
            mTradeManagerNetManager.doGetProcessOrderList(params, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取流程订单详情
     *
     * @param processOrderId
     * @param lsn
     */
    public void doGetProcessOrderDetail(long processOrderId, final OnResponseListener<ProcessOrder> lsn) {
        if (mTradeManagerNetManager == null) {
            return;
        }
        try {
            mTradeManagerNetManager.doGetProcessOrderDetail(processOrderId, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 达人接单
     *
     * @param buyerId
     * @param processType
     * @param lsn
     */
    public void doAcceptProcessOrder(long buyerId, String processType, final OnResponseListener<AcceptProcessOrderResult> lsn) {
        if (mTradeManagerNetManager == null) {
            return;
        }
        try {
            mTradeManagerNetManager.doAcceptProcessOrder(buyerId, processType, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询用户能否下咨询单
     *
     * @param itemId
     * @param lsn
     */
    public void doQuerySellerAndConsultState(long itemId, final OnResponseListener<SellerAndConsultStateResult> lsn) {
        if (mTradeManagerNetManager == null) {
            return;
        }
        try {
            mTradeManagerNetManager.doQuerySellerAndConsultState(itemId, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 达人查询咨询状态
     *
     * @param params
     * @param lsn
     */
    public void doGetConsultInfoForSeller(List<String> params, final OnResponseListener<ConsultState> lsn) {
        if (mTradeManagerNetManager == null) {
            return;
        }
        try {
            mTradeManagerNetManager.doGetConsultInfoForSeller(params, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建流程订单
     *
     * @param params
     * @param lsn
     */
    public void doCreateProcessOrder(Api_TRADEMANAGER_CreateProcessOrderParam params, final OnResponseListener<CreateProcessOrderResultTO> lsn) {
        if (mTradeManagerNetManager == null) {
            return;
        }
        try {
            mTradeManagerNetManager.doCreateProcessOrder(params, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 用户结束流程
     *
     * @param itemId
     * @param processOrderId
     * @param lsn
     */
    public void doFinishConsult(long itemId, long processOrderId, final OnResponseListener<FinishProcessResult> lsn) {
        if (mTradeManagerNetManager == null) {
            return;
        }
        try {
            mTradeManagerNetManager.doFinishConsult(itemId, processOrderId, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 用户取消流程
     *
     * @param processOrderId
     * @param lsn
     */
    public void doCancelProcessOrder(long processOrderId, final OnResponseListener<CancelProcessResult> lsn) {
        if (mTradeManagerNetManager == null) {
            return;
        }
        try {
            mTradeManagerNetManager.doCancelProcessOrder(processOrderId, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据商品ids批量查询商品
     *
     * @param itemIds
     * @param lsn
     */
    public void doGetItemListByItemIds(long[] itemIds, final OnResponseListener<ShortItemsResult> lsn) {
        if (mTradeManagerNetManager == null) {
            return;
        }
        try {
            mTradeManagerNetManager.doGetItemListByItemIds(itemIds, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取流程状态（恢复现场）
     *
     * @param params
     * @param lsn
     */
    public void doGetProcessState(Api_TRADEMANAGER_ProcessStateQuery params, final OnResponseListener<ProcessState> lsn) {
        if (mTradeManagerNetManager == null) {
            return;
        }
        try {
            mTradeManagerNetManager.doGetProcessState(params, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取达人咨询商品列表
     *
     * @param params
     * @param lsn
     */
    public void doGetConsultItemList(QueryTermsDTO params, final OnResponseListener<ShortItemsResult> lsn) {
        if (mTradeManagerNetManager == null) {
            return;
        }
        try {
            mTradeManagerNetManager.doGetConsultItemList(params, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * 查询商品
     *
     * @param lsn
     */
    public void doGetPublishItemInfo(ItemQueryParam params, final OnResponseListener<PublishServiceDO> lsn) {
        if (mTradeManagerNetManager == null) {
            return;
        }
        try {
            mTradeManagerNetManager.doGetPublishItemInfo(params, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新商品状态
     *
     * @param params
     * @param lsn
     */
    public void doUpdateState(ItemQueryParam params, final OnResponseListener<Boolean> lsn) {
        if (mTradeManagerNetManager == null) {
            return;
        }
        try {
            mTradeManagerNetManager.doUpdateState(params, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取快速咨询商品
     *
     * @param params
     * @param lsn
     */
    public void doGetFastConsultItem(ConsultContent params, final OnResponseListener<TmConsultInfo> lsn) {
        if (mTradeManagerNetManager == null) {
            return;
        }
        try {
            mTradeManagerNetManager.doGetFastConsultItem(params, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改用户在线和隐身状态
     *
     * @param userId
     * @param status
     * @param lsn
     */
    public void doEditUserStatus(final long userId, final int status, final OnResponseListener<Boolean> lsn) {
        if (mSnsNetManager == null) {
            return;
        }
        try {
            mSnsNetManager.doEditUserStatus(userId, status, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询用户在线和隐身状态
     *
     * @param userIds
     * @param lsn
     */
    public void doGetBatchUserStatus(final long[] userIds, final OnResponseListener<UserStatusInfoList> lsn) {
        if (mSnsNetManager == null) {
            return;
        }
        try {
            mSnsNetManager.doGetBatchUserStatus(userIds, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * 增加图文
     *
     * @param params
     * @param lsn
     */
    public void doAddPictureText(PictureTextListQuery params, final OnResponseListener<Boolean> lsn) {
        if (mCommonNetManager == null) {
            return;
        }
        try {
            mCommonNetManager.doAddPictureText(params, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询图文详情(APP统一返回属性)
     *
     * @param outId
     * @param outType
     * @param lsn
     */
    public void doGetPictureTextInfo(long outId, String outType, final OnResponseListener<PictureTextListResult> lsn) {
        if (mCommonNetManager == null) {
            return;
        }
        try {
            mCommonNetManager.doGetPictureTextInfo(outId, outType, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改图文
     *
     * @param params
     * @param lsn
     */
    public void doUpdatePictureText(PictureTextListQuery params, final OnResponseListener<Boolean> lsn) {
        if (mCommonNetManager == null) {
            return;
        }
        try {
            mCommonNetManager.doUpdatePictureText(params, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据文章id获取文章信息
     *
     * @param boothCode
     * @param lsn
     */
    public void doGetArticleListByBoothCode(final String boothCode, final OnResponseListener<ArticleRecommendInfo> lsn) {
        if (mSnsNetManager == null) {
            return;
        }
        try {
            mSnsNetManager.doGetArticleListByBoothCode(boothCode, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取APP首页数据
     *
     * @param lsn
     * @throws JSONException
     */
    public void doInitHomeData(final OnResponseListener<AppHomeData> lsn) {
        if (mCommonNetManager == null) {
            return;
        }

        try {
            mCommonNetManager.doInitHomeData(lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取达人首页数据
     *
     * @param lsn
     * @throws JSONException
     */
    public void doInitTalentHomeData(final OnResponseListener<TalentHomeData> lsn) {
        if (mCommonNetManager == null) {
            return;
        }

        try {
            mCommonNetManager.doInitTalentHomeData(lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据展位码获取达人咨询商品列表
     *
     * @param params
     * @param lsn
     */
    public void doGetThemeItemListByBoothCode(CodeQueryDTO params, final OnResponseListener<BoothItemsResult> lsn) {
        if (mSnsNetManager == null) {
            return;
        }

        try {
            mSnsNetManager.doGetThemeItemListByBoothCode(params, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 钱包检验验证码
     *
     * @param verifyCodeType
     * @param verifyCode
     * @param lsn
     */
    public void doCheckVerifyCode(String verifyCodeType, String verifyCode, String verifyIdentityCode, final OnResponseListener<PayCoreBaseResult> lsn) {
        if (mCommonNetManager == null) {
            return;
        }
        try {
            mCommonNetManager.doCheckVerifyCode(verifyCodeType, verifyCode, verifyIdentityCode, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据卡号得到银行卡信息
     *
     * @param bankCardNo
     * @param lsn
     */
    public void doGetBankCardByCardNo(String bankCardNo, final OnResponseListener<BankCard> lsn) {
        if (mCommonNetManager == null) {
            return;
        }
        try {
            mCommonNetManager.doGetBankCardByCardNo(bankCardNo, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取光大快捷支付的信息
     *
     * @param cebCloudPayParam
     * @param lsn
     */
    public void doGetCebCloudPayInfo(CebCloudPayParam cebCloudPayParam, final OnResponseListener<CebCloudPayInfo> lsn) {
        if (mCommonNetManager == null) {
            return;
        }
        try {
            mCommonNetManager.doGetCebCloudPayInfo(cebCloudPayParam, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 得到用户电子钱包的信息
     *
     * @param lsn
     */
    public void doGetEleAccountInfo(final OnResponseListener<EleAccountInfo> lsn) {
        if (mCommonNetManager == null) {
            return;
        }
        try {
            mCommonNetManager.doGetEleAccountInfo(lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取钱包订单支付状态
     *
     * @param bizOrderId
     * @param lsn
     */
    public void doGetPcPayStatusInfo(long bizOrderId, final OnResponseListener<PcPayStatusInfo> lsn) {
        if (mCommonNetManager == null) {
            return;
        }
        try {
            mCommonNetManager.doGetPcPayStatusInfo(bizOrderId, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询用户收支明细
     *
     * @param lsn
     */
    public void doPageQueryUserBill(int pageNo, int pageSize, final OnResponseListener<BillList> lsn) {
        if (mCommonNetManager == null) {
            return;
        }
        try {
            mCommonNetManager.doPageQueryUserBill(pageNo, pageSize, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询用户绑定的银行卡列表
     *
     * @param lsn
     */
    public void doPageQueryUserBindBankCard(int pageNo, int pageSize, final OnResponseListener<BankCardList> lsn) {
        if (mCommonNetManager == null) {
            return;
        }
        try {
            mCommonNetManager.doPageQueryUserBindBankCard(pageNo, pageSize, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询已结算信息详情
     *
     * @param settlementId
     * @param pageNo
     * @param pageSize
     * @param lsn
     */
    public void doQuerySettlementDetails(long settlementId, int pageNo, int pageSize, final OnResponseListener<SettlementList> lsn) {
        if (mCommonNetManager == null) {
            return;
        }
        try {
            mCommonNetManager.doQuerySettlementDetails(settlementId, pageNo, pageSize, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询已结算信息
     *
     * @param pageNo
     * @param pageSize
     * @param lsn
     */
    public void doQuerySettlements(int pageNo, int pageSize, final OnResponseListener<SettlementList> lsn) {
        if (mCommonNetManager == null) {
            return;
        }
        try {
            mCommonNetManager.doQuerySettlements(pageNo, pageSize, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 得到交易状态
     *
     * @param payOrderId
     * @param lsn
     */
    public void doQueryTransStatus(long payOrderId, final OnResponseListener<PcPayResult> lsn) {
        if (mCommonNetManager == null) {
            return;
        }
        try {
            mCommonNetManager.doQueryTransStatus(payOrderId, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询未结算信息详情
     *
     * @param pageNo
     * @param pageSize
     * @param lsn
     */
    public void doQueryUnsettlements(int pageNo, int pageSize, final OnResponseListener<SettlementList> lsn) {
        if (mCommonNetManager == null) {
            return;
        }
        try {
            mCommonNetManager.doQueryUnsettlements(pageNo, pageSize, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 钱包充值
     *
     * @param rechargeParam
     * @param lsn
     */
    public void doRecharge(RechargeParam rechargeParam, final OnResponseListener<RechargeResult> lsn) {
        if (mCommonNetManager == null) {
            return;
        }
        try {
            mCommonNetManager.doRecharge(rechargeParam, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 钱包发送验证码
     *
     * @param verifyCodeType
     * @param mobilePhone
     * @param lsn
     */
    public void doSendVerifyCode(String verifyCodeType, String mobilePhone, final OnResponseListener<PayCoreBaseResult> lsn) {
        if (mCommonNetManager == null) {
            return;
        }
        try {
            mCommonNetManager.doSendVerifyCode(verifyCodeType, mobilePhone, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置支付密码
     *
     * @param setupPayPwdParam
     * @param lsn
     */
    public void doSetupPayPwd(SetupPayPwdParam setupPayPwdParam, final OnResponseListener<PayCoreBaseResult> lsn) {
        if (mCommonNetManager == null) {
            return;
        }
        try {
            mCommonNetManager.doSetupPayPwd(setupPayPwdParam, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新支付密码
     *
     * @param updatePayPwdParam
     * @param lsn
     */
    public void doUpdatePayPwd(UpdatePayPwdParam updatePayPwdParam, final OnResponseListener<PayCoreBaseResult> lsn) {
        if (mCommonNetManager == null) {
            return;
        }
        try {
            mCommonNetManager.doUpdatePayPwd(updatePayPwdParam, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 验证身份
     *
     * @param verifyIdentityParam
     * @param lsn
     */
    public void doVerifyIdentity(VerifyIdentityParam verifyIdentityParam, final OnResponseListener<VerifyIdentityResult> lsn) {
        if (mCommonNetManager == null) {
            return;
        }
        try {
            mCommonNetManager.doVerifyIdentity(verifyIdentityParam, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 验证支付密码
     *
     * @param payPwd
     * @param lsn
     */
    public void doVerifyPayPwd(String payPwd, final OnResponseListener<PayCoreBaseResult> lsn) {
        if (mCommonNetManager == null) {
            return;
        }
        try {
            mCommonNetManager.doVerifyPayPwd(payPwd, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 提现
     *
     * @param withdrawParam
     * @param lsn
     */
    public void doWithdraw(WithdrawParam withdrawParam, final OnResponseListener<PayCoreBaseResult> lsn) {
        if (mCommonNetManager == null) {
            return;
        }
        try {
            mCommonNetManager.doWithdraw(withdrawParam, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 钱包批量支付
     *
     * @param elePurseBatchPayParam
     * @param lsn
     */
    public void doElePurseBatchPay(ElePurseBatchPayParam elePurseBatchPayParam, final OnResponseListener<PayCoreBaseResult> lsn) {
        if (mCommonNetManager == null) {
            return;
        }
        try {
            mCommonNetManager.doElePurseBatchPay(elePurseBatchPayParam, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取支付宝批量支付的信息
     *
     * @param aliBatchPayParam
     * @param lsn
     */
    public void doGetAliBatchPayInfo(AliBatchPayParam aliBatchPayParam, final OnResponseListener<AliPayInfo> lsn) {
        if (mCommonNetManager == null) {
            return;
        }
        try {
            mCommonNetManager.doGetAliBatchPayInfo(aliBatchPayParam, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取光大快捷批量支付的信息
     *
     * @param cebCloudBatchPayParam
     * @param lsn
     */
    public void doGetCebCloudBatchPayInfo(CebCloudBatchPayParam cebCloudBatchPayParam, final OnResponseListener<CebCloudPayInfo> lsn) {
        if (mCommonNetManager == null) {
            return;
        }
        try {
            mCommonNetManager.doGetCebCloudBatchPayInfo(cebCloudBatchPayParam, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 钱包支付
     *
     * @param elePursePayParam
     * @param lsn
     */
    public void doElePursePay(ElePursePayParam elePursePayParam, final OnResponseListener<PayCoreBaseResult> lsn) {
        if (mCommonNetManager == null) {
            return;
        }
        try {
            mCommonNetManager.doElePursePay(elePursePayParam, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 得到银行名列表
     *
     * @param lsn
     */
    public void doGetBankNameList(final OnResponseListener<BankNameList> lsn) {
        if (mCommonNetManager == null) {
            return;
        }
        try {
            mCommonNetManager.doGetBankNameList(lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 验证照片是否为身份证
     *
     * @param verifyIdCardPhotoParam
     * @param lsn
     */
    public void doVerifyIdCardPhoto(VerifyIdCardPhotoParam verifyIdCardPhotoParam, final OnResponseListener<BaseResult> lsn) {
        if (mCommonNetManager == null) {
            return;
        }
        try {
            mCommonNetManager.doVerifyIdCardPhoto(verifyIdCardPhotoParam, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 提交身份证照片
     *
     * @param submitIdCardPhotoParam
     * @param lsn
     */
    public void doSubmitIdCardPhoto(SubmitIdCardPhotoParam submitIdCardPhotoParam, final OnResponseListener<SubmitIdCardPhotoResult> lsn) {
        if (mCommonNetManager == null) {
            return;
        }
        try {
            mCommonNetManager.doSubmitIdCardPhoto(submitIdCardPhotoParam, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取微信批量支付的信息
     *
     * @param wxBatchPayParam
     * @param lsn
     */
    public void doGetWxBatchPayInfo(WxBatchPayParam wxBatchPayParam, final OnResponseListener<WxPayInfo> lsn) {
        if (mCommonNetManager == null) {
            return;
        }
        try {
            mCommonNetManager.doGetWxBatchPayInfo(wxBatchPayParam, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 增加收听人数
     *
     * @param guideId
     * @param attractionId
     * @param focusId
     * @param delta
     * @param lsn
     */
    public void doAddListenCount(long guideId, long attractionId, long focusId, long delta, final OnResponseListener<Boolean> lsn) {
        if (mTravelNetManager == null) {
            return;
        }
        try {
            mTravelNetManager.doAddListenCount(guideId, attractionId, focusId, delta, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取导览详情
     *
     * @param lsn
     */
    public void doGetGuideDetail(long scenicId, final OnResponseListener<GuideAttractionFocusInfo> lsn) {
        if (mTravelNetManager == null) {
            return;
        }
        try {
            mTravelNetManager.doGetGuideDetail(scenicId, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取实用锦囊
     *
     * @param guideId
     * @param lsn
     */
    public void doGetGuideTips(long guideId, final OnResponseListener<GuideTipsInfo> lsn) {
        if (mTravelNetManager == null) {
            return;
        }
        try {
            mTravelNetManager.doGetGuideTips(guideId, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取附近的导览列表
     *
     * @param nearGuideInfo
     * @param lsn
     */
    public void doGetNearbyGuideList(NearGuideInfo nearGuideInfo, final OnResponseListener<GuideScenicInfoList> lsn) {
        if (mTravelNetManager == null) {
            return;
        }
        try {
            mTravelNetManager.doGetNearbyGuideList(nearGuideInfo, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取推荐的导览列表
     *
     * @param lsn
     */
    public void doGetSugGuideList(final OnResponseListener<GuideScenicInfoList> lsn) {
        if (mTravelNetManager == null) {
            return;
        }
        try {
            mTravelNetManager.doGetSugGuideList(lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 搜索导览列表
     *
     * @param searchReq
     * @param lsn
     */
    public void doSearchGuideList(GuideSearchReq searchReq, final OnResponseListener<GuideSearchInfo> lsn) {
        if (mTravelNetManager == null) {
            return;
        }
        try {
            mTravelNetManager.doSearchGuideList(searchReq, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询店铺简介信息
     *
     * @param sellerId
     * @param lsn
     */
    public void doQueryMerchantDesc(long sellerId, final OnResponseListener<MerchantDesc> lsn) {
        if (mTravelNetManager == null) {
            return;
        }
        try {
            mTravelNetManager.doQueryMerchantDesc(sellerId, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询店铺资质信息
     *
     * @param sellerId
     * @param lsn
     */
    public void doQueryMerchantQualification(long sellerId, final OnResponseListener<Qualification> lsn) {
        if (mTravelNetManager == null) {
            return;
        }
        try {
            mTravelNetManager.doQueryMerchantQualification(sellerId, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取物流详情
     *
     * @param logisticsId
     * @param lsn
     */
    public void doGetLogisticsList(long logisticsId, final OnResponseListener<LgExpressInfo> lsn) {
        if (mTradeManagerNetManager == null) {
            return;
        }
        try {
            mTradeManagerNetManager.doGetLogisticsList(logisticsId, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发起直播
     *
     * @param params
     * @param lsn
     */
    public void doCreateLive(LiveRecordInfo params, final OnResponseListener<Api_SNSCENTER_SnsCreateLiveResult> lsn) {
        if (mSnsNetManager == null) {
            return;
        }
        try {
            mSnsNetManager.doCreateLive(params, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取直播类型列表
     *
     * @param lsn
     */
    public void doGetLiveCategoryList(final OnResponseListener<LiveCategoryResult> lsn) {
        if (mSnsNetManager == null) {
            return;
        }
        try {
            mSnsNetManager.doGetLiveCategoryList(lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取直播列表
     *
     * @param params
     * @param lsn
     */
    public void doGetLiveList(LiveRecordAPIPageQuery params, final OnResponseListener<LiveRecordAPIPageResult> lsn) {
        if (mSnsNetManager == null) {
            return;
        }
        try {
            mSnsNetManager.doGetLiveList(params, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取直播项
     *
     * @param livecenterId
     * @param lsn
     */
    public void doGetLiveRecord(long livecenterId, final OnResponseListener<Api_SNSCENTER_SnsLiveRecordResult> lsn) {
        if (mSnsNetManager == null) {
            return;
        }
        try {
            mSnsNetManager.doGetLiveRecord(livecenterId, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取直播间信息
     *
     * @param lsn
     */
    public void doGetLiveRoomInfo(long userId, final OnResponseListener<LiveRoomResult> lsn) {
        if (mSnsNetManager == null) {
            return;
        }
        try {
            mSnsNetManager.doGetLiveRoomInfo(userId, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取msg_server地址
     *
     * @param liveMsgServerAddrParam
     * @param lsn
     */
    public void doGetMsgServerAddressByUser(LiveMsgServerAddrParam liveMsgServerAddrParam, final OnResponseListener<LiveMsgServerAddrResult> lsn) {
        if (mSnsNetManager == null) {
            return;
        }
        try {
            mSnsNetManager.doGetMsgServerAddress(liveMsgServerAddrParam, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void doGetMsgServerAddressByVisitor(LiveMsgServerAddrParam liveMsgServerAddrParam, final OnResponseListener<LiveMsgServerAddrResult> lsn) {
        if (mSnsNetManager == null) {
            return;
        }
        try {
            mSnsNetManager.doGetMsgServerAddressByVisitor(liveMsgServerAddrParam, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 直播禁言
     *
     * @param params
     * @param lsn
     */
    public void doDisableSendMsg(DisableParam params, final OnResponseListener<DisableResult> lsn) {
        if (mSnsNetManager == null) {
            return;
        }
        try {
            mSnsNetManager.doDisableSendMsg(params, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关注主播
     *
     * @param params
     * @param lsn
     */
    public void doFollowAnchor(FollowAnchorParam params, final OnResponseListener<FollowAnchorResult> lsn) {
        if (mSnsNetManager == null) {
            return;
        }
        try {
            mSnsNetManager.doFollowAnchor(params, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送通知消息，如主播离开/主播回来/分享直播等
     *
     * @param params
     * @param lsn
     */
    public void doSendOtherMsg(OtherMsgParam params, final OnResponseListener<OtherMsgResult> lsn) {
        if (mSnsNetManager == null) {
            return;
        }
        try {
            mSnsNetManager.doSendOtherMsg(params, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭直播
     *
     * @param liveId
     * @param lsn
     */
    public void doCloseLive(long liveId, final OnResponseListener<Api_SNSCENTER_SnsCloseLiveResult> lsn) {
        if (mSnsNetManager == null) {
            return;
        }
        try {
            mSnsNetManager.doCloseLive(liveId, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 是否含有未结束直播
     *
     * @param lsn
     */
    public void hasNoEndLive(final OnResponseListener<CreateLiveResult> lsn) {
        if (mSnsNetManager == null) {
            return;
        }
        try {
            mSnsNetManager.hasNoEndLive(lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

//    /**
//     * 修改直播间
//     *
//     * @param updateLiveRoom
//     * @param lsn
//     */
//    public void doUpdateLiveRoom(UpdateLiveRoom updateLiveRoom, final OnResponseListener<Boolean> lsn) {
//        if (mSnsNetManager == null) {
//            return;
//        }
//        try {
//            mSnsNetManager.doUpdateLiveRoom(updateLiveRoom, lsn);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * 批量删除回放
     *
     * @param liveIdList
     * @param lsn
     */
    public void doDeleteLiveReplay(DeleteLiveListInfo liveIdList, final OnResponseListener<Boolean> lsn) {
        if (mSnsNetManager == null) {
            return;
        }
        try {
            mSnsNetManager.doDeleteLiveReplay(liveIdList, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取直播间正在直播记录
     *
     * @param roomId
     * @param lsn
     */
    public void doGetLiveRoomLivingRecord(final long roomId, final OnResponseListener<LiveRoomLivingRecordResult> lsn) {
        if (mSnsNetManager == null) {
            return;
        }
        try {
            mSnsNetManager.doGetLiveRoomLivingRecord(roomId, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * 批量删除购物车
     *
     * @param deleteCartInfo
     * @param lsn
     */
    public void doDeleteCart(DeleteCartInfo deleteCartInfo, final OnResponseListener<Boolean> lsn) {
        if (mTradeManagerNetManager == null) {
            return;
        }
        try {
            mTradeManagerNetManager.doDeleteCart(deleteCartInfo, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取购物车列表
     *
     * @param lsn
     */
    public void doGetCartInfoList(final OnResponseListener<CartInfoListResult> lsn) {
        if (mTradeManagerNetManager == null) {
            return;
        }
        try {
            mTradeManagerNetManager.doGetCartInfoList(lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 加入购物车
     *
     * @param createCartInfo
     * @param lsn
     */
    public void doSaveToCart(CreateCartInfo createCartInfo, final OnResponseListener<Boolean> lsn) {
        if (mTradeManagerNetManager == null) {
            return;
        }
        try {
            mTradeManagerNetManager.doSaveToCart(createCartInfo, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 勾选购物车
     *
     * @param selectCartInfo
     * @param lsn
     */
    public void doSelectCart(SelectCartInfo selectCartInfo, final OnResponseListener<Boolean> lsn) {
        if (mTradeManagerNetManager == null) {
            return;
        }
        try {
            mTradeManagerNetManager.doSelectCart(selectCartInfo, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 购物车数量
     *
     * @param lsn
     */
    public void doSelectCartAmount(final OnResponseListener<CartAmountResult> lsn) {
        if (mTradeManagerNetManager == null) {
            return;
        }
        try {
            mTradeManagerNetManager.doSelectCartAmount(lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改商品数量
     *
     * @param updateAmountCartInfo
     * @param lsn
     */
    public void doUpdateCartAmount(UpdateAmountCartInfo updateAmountCartInfo, final OnResponseListener<Boolean> lsn) {
        if (mTradeManagerNetManager == null) {
            return;
        }
        try {
            mTradeManagerNetManager.doUpdateCartAmount(updateAmountCartInfo, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void doAddCrash(CrashInfoList crashInfoList, final OnResponseListener<Boolean> lsn) {
        if (mCommonNetManager == null) {
            return;
        }
        try {
            mCommonNetManager.doAddCrash(crashInfoList, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void doGetHomeCourtData(String detail, double latitude, double longtitude,
                                   String citycode, int interestType, final OnResponseListener<Api_RESOURCECENTER_HomeInfoResult> lsn) {
        if (mCommonNetManager == null) {
            return;
        }
        try {
            mCommonNetManager.doGetHomeCourtData(detail, latitude, longtitude,
                    citycode, interestType, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void doGetHomeCourtUGCData(int pageNo, int pageSize, int type, String cityCode, double latitude, double longitude,
                                      final OnResponseListener<UgcInfoResultList> lsn) {
        if (mCommonNetManager == null) {
            return;
        }
        try {
            mCommonNetManager.doGetHomeCourtUGCData(pageNo, pageSize, type, cityCode, latitude, longitude,
                    lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void doGetMineInfo(long userId, final OnResponseListener<Api_RESOURCECENTER_UserInfoResult> lsn) {
        if (mCommonNetManager == null) {
            return;
        }
        try {
            mCommonNetManager.doGetMineInfo(userId,
                    lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void doGetUserWallet(long userId, final OnResponseListener<Api_RESOURCECENTER_UserWalletResult> lsn) {
        if (mCommonNetManager == null) {
            return;
        }
        try {
            mCommonNetManager.doGetUserWallet(userId, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void doGetUserToolsInfo(final OnResponseListener<Api_RESOURCECENTER_UserToolsResult> lsn) {
        if (mCommonNetManager == null) {
            return;
        }
        try {
            mCommonNetManager.doGetUserToolsInfo(lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void doGetToolsInfoWithoutLogin(final OnResponseListener<Api_RESOURCECENTER_UserToolsResult> lsn) {
        if (mCommonNetManager == null) return;
        try {
            mCommonNetManager.doGetToolsInfoWithoutLogin(lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void doGetSportInfo(long latitude, long longtitude,
                               String cityCode,
                               final OnResponseListener<Api_RESOURCECENTER_SportInfoResult> lsn) {
        if (mCommonNetManager == null) {
            return;
        }
        try {
            mCommonNetManager.doGetSportInfo(latitude, longtitude,
                    cityCode, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void doGetMainPublishBootInfo(String cityName, String cityCode, final OnResponseListener<Api_RESOURCECENTER_PublishBootResult> lsn) {
        if (mCommonNetManager == null) {
            return;
        }
        try {
            mCommonNetManager.doGetMainPublishBootInfo(cityName, cityCode, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void doGetCityInfo(boolean containDistrictFlg,
                              final OnResponseListener<Api_PLACE_CityListResult> lsn) {
        if (mCommonNetManager == null) {
            return;
        }
        try {
            mCommonNetManager.doGetCityInfo(containDistrictFlg, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void doGetHasLiveRoomOrNot(long userID, final OnResponseListener<Api_LIVE_LiveRoomHasOrNot> value) {
        if (mSnsNetManager == null) {
            return;
        }
        try {
            mSnsNetManager.doGetHasLiveRoomOrNot(userID, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void doGetUgcNewAddCountByTime(double mLat, double mLng, String mCityCode, int mType, long lastRequestTime, OnResponseListener<Api_SNSCENTER_UgcCountResultList> lsn) {

        if (mCommonNetManager == null) {
            return;
        }
        try {
            mCommonNetManager.doGetUgcNewAddCountByTime(mLat, mLng, mCityCode, mType, lastRequestTime, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void doGetUserRecentSportsOrder(final OnResponseListener<Api_TRADEMANAGER_DetailOrder> lsn) {
        if (mCommonNetManager == null) {
            return;
        }
        try {
            mCommonNetManager.doGetUserRecentSportsOrder(lsn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 运动页面分页获取商品列表
     *
     * @param code
     * @param lsn
     * @throws JSONException
     */

    public void doGetItemListForPointMall(CodeQueryDTO code, final OnResponseListener<ShortItemsResult> lsn) {
        if (mTravelNetManager == null) {
            return;
        }
        try {
            mTravelNetManager.doGetItemListForPointMall(code, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 直播结束后获得技术页面数据
     *
     * @param api_snscenter_snsClosedViewTopNQuery
     * @param onResponseListener
     */
    public void doGetLiveOverResult(Api_SNSCENTER_SnsClosedViewTopNQuery api_snscenter_snsClosedViewTopNQuery, OnResponseListener<Api_SNSCENTER_SnsClosedViewTopNResult> onResponseListener) {
        if (mSnsNetManager == null) {
            return;
        }
        try {
            mSnsNetManager.doGetLiveOverResult(api_snscenter_snsClosedViewTopNQuery, onResponseListener);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 手机号是否已经存在
     */
    public void isMobileExisted(final String phone, final OnResponseListener<Api_BoolResp> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        Runnable requestThread = new Runnable() {
            @Override
            public void run() {
                try {
                    final User_IsMobileExisted req = new User_IsMobileExisted(phone);

                    sendRequest(mContext, mApiContext, req);
                    if (req.getReturnCode() == ApiCode.SUCCESS) {

                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (lsn != null) {
                                    lsn.onComplete(true, req.getResponse(),
                                            ErrorCode.STATUS_OK,
                                            null);
                                }
                            }
                        });
                    } else {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (lsn != null) {
                                    lsn.onComplete(false, req.getResponse(),
                                            req.getReturnCode(),
                                            req.getReturnMessage());
                                }
                            }
                        });

                    }

                } catch (final Exception e) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (lsn != null) {
                                lsn.onComplete(false, null,
                                        ErrorCode.STATUS_NETWORK_EXCEPTION,
                                        e.getMessage());
                            }
                        }
                    });

                }
            }
        };
        NetThreadManager.getInstance().execute(requestThread);

    }

    /**
     * 更换手机号
     */
    public void userChangePhone(final String phone, final String dynamicCode, final OnResponseListener<Api_BoolResp> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        Runnable requestThread = new Runnable() {
            @Override
            public void run() {
                try {
                    mApiContext.setPhoneNumberAndDynamic(phone, dynamicCode);
                    new UserApi().savePhone(phone);
                    new UserApi().savePhoneCode(dynamicCode);
                    final User_ChangeUserPhone req = new User_ChangeUserPhone();
                    req.setChangeMobile(phone);
                    sendRequest(mContext, mApiContext, req);
                    if (req.getReturnCode() == ApiCode.SUCCESS) {

                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (lsn != null) {
                                    lsn.onComplete(true, req.getResponse(),
                                            ErrorCode.STATUS_OK,
                                            null);
                                }
                            }
                        });
                    } else {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (lsn != null) {
                                    lsn.onComplete(false, req.getResponse(),
                                            req.getReturnCode(),
                                            req.getReturnMessage());
                                }
                            }
                        });

                    }

                } catch (final Exception e) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (lsn != null) {
                                lsn.onComplete(false, null,
                                        ErrorCode.STATUS_NETWORK_EXCEPTION,
                                        e.getMessage());
                            }
                        }
                    });

                }
            }
        };
        NetThreadManager.getInstance().execute(requestThread);

    }

    /**
     * 播放小视频次数加 1
     */
    public void getShortVideoDetail(final OnResponseListener<Long> lsn, long ugcId) {
        if (mCommonNetManager == null) {
            return;
        }
        try {
            mCommonNetManager.getShortVideoDetail(lsn, ugcId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化APP
     *
     * @param lsn
     * @throws JSONException
     */
    public void getHomePopup(final OnResponseListener<BoothList> lsn) {
        if (mCommonNetManager == null) {
            return;
        }

        try {
            mCommonNetManager.getHomePopup(lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void doGetHasNoEndBatch(long uid, OnResponseListener<Api_SNSCENTER_SnsHasNoEndBatchResult> onResponseListener) {
        if (mSnsNetManager == null) {
            return;
        }
        try {
            mSnsNetManager.doGetHasNoEndBatch(uid, onResponseListener);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * 上传培训视频回调
     *
     * @param lsn
     */
    public void trainAsyncCallback(Api_TRAIN_AsyncCallbackParam param, final OnResponseListener<Api_TRAIN_ResponseDTO> lsn) {
        if (mSnsNetManager == null) {
            return;
        }
        try {
            mSnsNetManager.trainAsyncCallback(param, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取圈子中的标准视频视频列表或者直播列表
     *
     * @param lsn
     */
    public void queryLiveOrVideoList(long uuid, int sportHobby,int pageIndex, int pageSize, String recordType, String liveScreenType, final OnResponseListener<String> lsn) {
        if (mSnsNetManager == null) {
            return;
        }
        try {
            mSnsNetManager.doGetHomeLivePageList(uuid, sportHobby,pageIndex, pageSize, recordType, liveScreenType, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取圈子中的用户上传视频列表
     *
     * @param lsn
     */
    public void doGetUserVideoList(long uuid, int pageIndex, int pageSize, final OnResponseListener<String> lsn) {
        if (mSnsNetManager == null) {
            return;
        }
        try {
            mSnsNetManager.doGetUserVideoList(uuid, pageIndex, pageSize, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取圈子中的爬取到的推荐列表
     *
     * @param lsn
     */
    public void getRecommendPageList(long uuid, int pageIndex, int pageSize, List<String> tagList, int queryType, final OnResponseListener<String> lsn) {
        if (mSnsNetManager == null) {
            return;
        }
        try {
            mSnsNetManager.getRecommendPageList(uuid, pageIndex, pageSize, tagList, queryType, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 圈子 推荐或动态Tab页 不喜欢 删除
     * @param lsn
     */
    public void doDislike(Api_SNSCENTER_DisLikeArgs args, final OnResponseListener<Boolean> lsn) {
        if (mSnsNetManager == null) {
            return;
        }
        try {
            mSnsNetManager.doDislike(args, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 圈子搜索接口
     *
     * @param lsn
     */
    public void getSearchPageList(long uuid, String title, int pageIndex, int pageSize, final OnResponseListener<String> lsn) {
        if (mSnsNetManager == null) {
            return;
        }
        try {
            mSnsNetManager.getSearchPageList(uuid, title, pageIndex, pageSize, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取运动兴趣标签
     *
     * @param lsn
     */
    public void doGetGuideTagInfo(final OnResponseListener<Api_SNSCENTER_GuideTagResultList> lsn) {
        if (mSnsNetManager == null) {
            return;
        }
        try {
            mSnsNetManager.doGetGuideTagInfo(lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * 约战广场
     *
     * @param lsn
     */
    public void getCampaignData(Api_COMPETITION_QueryCampaignParam param, OnResponseListener<Api_COMPETITION_CampaignsVoResult> lsn) {
        if (mSnsNetManager == null) {
            return;
        }
        try {
            mSnsNetManager.getCampaignData(param, lsn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取用户回放列表
     *
     * @param api_snscenter_snsReplayListQuery
     * @param lsn
     */
    public void doGetLiveListByUserID(Api_SNSCENTER_SnsReplayListQuery api_snscenter_snsReplayListQuery, OnResponseListener<Api_SNSCENTER_SnsReplayListResult> lsn) {
        if (mSnsNetManager == null) {
            return;
        }
        mSnsNetManager.doGetLiveListByUserId(api_snscenter_snsReplayListQuery, lsn);
    }
}