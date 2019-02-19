package com.newyhy.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.newyhy.adapter.CourseAdapter;
import com.newyhy.adapter.InviteFightAdapter;
import com.newyhy.adapter.TrainAdapter;
import com.newyhy.adapter.circle.DynamicAdapter;
import com.newyhy.network.CircleNetController;
import com.newyhy.network.NetHandler;
import com.newyhy.utils.DisplayUtils;
import com.newyhy.views.PopupButton;
import com.newyhy.views.RoundImageView;
import com.newyhy.views.SpacesItemDecoration;
import com.newyhy.views.UgcScrollView;
import com.newyhy.views.YhyRecyclerDivider;
import com.quanyan.base.util.DialogBuilder;
import com.quanyan.base.util.ScreenSize;
import com.quanyan.base.view.customview.imgpager.ImgPagerView;
import com.quanyan.utils.TimeUtils;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.AnalyDataValue;
import com.quanyan.yhy.common.ShieldType;
import com.yhy.cityselect.eventbus.EvBusCityGet;
import com.yhy.common.eventbus.event.EvBusBlack;
import com.yhy.common.eventbus.event.EvBusCircleChangeFollow;
import com.yhy.common.eventbus.event.EvBusCircleChangePraise;
import com.yhy.common.eventbus.event.EvBusCircleDelete;
import com.quanyan.yhy.eventbus.EvBusMessageCount;
import com.quanyan.yhy.eventbus.EvBusWhenSelectFragment;
import com.quanyan.yhy.libanalysis.AnArgs;
import com.quanyan.yhy.libanalysis.AnEvent;
import com.quanyan.yhy.libanalysis.Analysis;
import com.quanyan.yhy.net.NetManager;
import com.quanyan.yhy.net.lsn.OnResponseListener;
import com.quanyan.yhy.ui.adapter.LocalClubAdapter;
import com.quanyan.yhy.ui.base.utils.DialogUtil;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.base.utils.TCEventHelper;
import com.quanyan.yhy.ui.sport.LiveVideoAdapter;
import com.quanyan.yhy.ui.sport.model.LiveVideoInfo;
import com.quanyan.yhy.util.TimeUtil;
import com.quanyan.yhy.view.OnWheelChangedListener;
import com.quanyan.yhy.view.Wheel3DView;
import com.quanyan.yhy.view.WheelView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.smart.sdk.api.resp.Api_COMPETITION_CampaignsVoResult;
import com.smart.sdk.api.resp.Api_COMPETITION_QueryCampaignParam;
import com.smart.sdk.api.resp.Api_RESOURCECENTER_BannerDto;
import com.smart.sdk.api.resp.Api_RESOURCECENTER_FieldClubDto;
import com.smart.sdk.api.resp.Api_RESOURCECENTER_HomeInfoResult;
import com.smart.sdk.api.resp.Api_RESOURCECENTER_JoinActivityUserDto;
import com.smart.sdk.api.resp.Api_RESOURCECENTER_LiveRecordDto;
import com.smart.sdk.api.resp.Api_RESOURCECENTER_PlaceActivityDto;
import com.smart.sdk.api.resp.Api_RESOURCECENTER_RecommendPlaceDto;
import com.smart.sdk.api.resp.Api_SNSCENTER_UgcCountResultList;
import com.yhy.cityselect.eventbus.EvBusLocationChange;
import com.yhy.cityselect.cache.CityCache;
import com.yhy.common.base.BaseNewFragment;
import com.yhy.common.beans.net.model.discover.UgcInfoResult;
import com.yhy.common.beans.net.model.discover.UgcInfoResultList;
import com.yhy.common.beans.user.User;
import com.yhy.common.config.ContextHelper;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.eventbus.event.EvBusSportHabit;
import com.yhy.common.utils.CommonUtil;
import com.yhy.common.utils.JSONUtils;
import com.yhy.common.utils.PermissionUtils;
import com.yhy.common.utils.SPUtils;
import com.yhy.imageloader.ImageLoadManager;
import com.yhy.location.EvBusLocation;
import com.yhy.location.LocationManager;
import com.yhy.network.resp.outplace.GetOutPlaceCityListResp;
import com.yhy.router.YhyRouter;
import com.yhy.service.IUserService;
import com.yixia.camera.util.Log;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import de.greenrobot.event.EventBus;

public class NewHomeCourtFragment extends BaseNewFragment implements View.OnClickListener, NetHandler.NetHandlerCallback {
    //refresh
    private SmartRefreshLayout refreshLayout;
    private UgcScrollView scroll_parent;
    private RecyclerView mLiveRecyclerView;
    private RecyclerView mRecyclerLocalClub;
    private LiveVideoAdapter mLiveVideoAdapter;
    private LocalClubAdapter mLocalClubAdapter;
    //new Ugc_list
    private DynamicAdapter ugc_adapter;
    private ArrayList<UgcInfoResult> ugcList = new ArrayList<>();
    private LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false) {
        @Override
        public boolean canScrollVertically() {
            return false;
        }
    };
    //handler
    private NetHandler handler = new NetHandler(this);
    private CircleNetController controller;
    private RecyclerView rv_ugc;
    boolean isSlidingToLast = false;
    public static final int NOTSELECT = -1;
    public static final int OTHER = 0;
    public static final int YUMAOQIU = 2;
    public static final int BASKETBALL = 1;
    public static final int FOOTBALL = 3;
    private int mType = OTHER;
    private Wheel3DView wvMonth, wvDay, wvTime_start, wvTime_end;
    private TextView mDateText, mTimeText;
    private int mMonth, mDay, mTime;
    private String mMonthString, mDayString;
    private String mStartTimeString, mEndTimeString;
    private Dialog mTimeDialog;
    private Dialog mDateDialog;
    private PopupButton mPopupBtnCity;
    private ArrayList<GetOutPlaceCityListResp.OutPlaceCity> mCityList = new ArrayList<>();
    //    private ArrayList<Api_PLACE_City> mCityList = new ArrayList<>();
//    private ArrayList<Api_PLACE_District> mDistrictList = new ArrayList<>();
//    private PopNormalAdapter_OrderVenue mSwitchCityAdapter;
//    private PopNormalAdapter_OrderVenue mSwitchDistrictAdapter;
//    private ListView lv_city, lv_district;
//    private int mCityId = 0;
//    private int mCityPosition = 0;
//    private int mDistrictPosition = 0;
    private Dialog mDialog;
    private BroadcastReceiver broadcastReceiver;
    private TextView mUnReadNum;
    private RelativeLayout mMessageLayout;

    public static final int ALL = 1;
    public static final int CONCERN = 2;
    private int mUgcType = ALL;
    private ImgPagerView bannerView;
    //    private SimpleDraweeView mBasketBallBanner;
    private View mSportItem1;
    private View mSportItem2;
    private View mInterestGroup;
    private TextView mJustSee;
    //    private View mView;
    private View mInterestLayout;
    private String mCurCity = "深圳";
    private View mRecommentEmptyLayout;
    private View mRecommentVeneLayout;
    private View mDynamicEmpty;
    private TextView mDynamicRefresh;
    private View mBasketBallRecomment;
    private int mCurPageIndex = 1;
    private String mCityCode;
    private List<Api_RESOURCECENTER_PlaceActivityDto> mActivitys;
    private long mfastBookingPlaceId;

    private long getDynamicInfosTime;
    private Handler mHandler = new Handler();
    private Runnable runnable;

    private LinearLayout ll_common_venue;
    private LinearLayout ll_venue_activities;

    private static final String SPORT = "sport";
//    private User mUserInfo;

    private int mOldMonthIndex;
    private int mOldDayIndex;
    private int mOldStartHourIndex;
    private int mOldEndHourIndex;

    private RelativeLayout rl_club_list;
    private int dayIndex;
    private int hourIndex;
    private int monthIndex;

    private RelativeLayout badminton_list;

    private LinearLayout ll_home_live_video;

    // 课程推荐
    private LinearLayout ll_home_course;
    private RecyclerView rycvCourse;
    private CourseAdapter mCourseAdapter;

    // 驻场培训机构
    private LinearLayout ll_home_train;
    private RecyclerView rycvTrain;
    private TrainAdapter mTrainAdapter;

//    private View view_city;

    // 约战广场板块
    private LinearLayout llytInviteFight;
    private RelativeLayout rlytInviteTitle;
    private RecyclerView rycvInviteList;
    private InviteFightAdapter mInviteAdapter;

    private boolean hasCitys = false;

    @Autowired
    IUserService userService;

    HashMap<String, String> extraMap = new HashMap<>();

    @Override
    protected int setLayoutId() {
        return R.layout.home_court_fragment;
    }

    @Override
    protected void initVars() {
        super.initVars();
        EventBus.getDefault().register(this);
        controller = new CircleNetController(mActivity, handler);
        extraMap.put(Analysis.TAG, tag);

    }

    @Override
    protected void initView() {
        super.initView();

        //延时2分钟,继续加载数据的操作F
        runnable = new Runnable() {
            @Override
            public void run() {
                //请求是否有新的动态
                getUgcNewAddCountByTime(getDynamicInfosTime);
                mHandler.postDelayed(this, 120000);
            }
        };

        //initRefresh
        refreshLayout = mRootView.findViewById(R.id.refreshLayout);
        initRefresh();
        scroll_parent = mRootView.findViewById(R.id.scroll_parent);

        mRootView.findViewById(R.id.badminton_radio).setOnClickListener(this);
        mRootView.findViewById(R.id.football_radio).setOnClickListener(this);
        mRootView.findViewById(R.id.basketball_radio).setOnClickListener(this);
        mRootView.findViewById(R.id.just_see).setOnClickListener(this);
        mInterestLayout = mRootView.findViewById(R.id.interest_layout);
        mDynamicEmpty = mRootView.findViewById(R.id.dynamic_empty);
        mDynamicRefresh = (TextView) mRootView.findViewById(R.id.dynamic_refresh);
        mDynamicRefresh.setOnClickListener(this);
        mRootView.findViewById(R.id.create_dynamic).setOnClickListener(this);
        initCityList(mRootView);

        mUnReadNum = (TextView) mRootView.findViewById(R.id.court_message_num);
        mMessageLayout = (RelativeLayout) mRootView.findViewById(R.id.message_layout);

        mRecyclerLocalClub = (RecyclerView) mRootView.findViewById(R.id.local_club_list);
        mRecyclerLocalClub.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerLocalClub.addItemDecoration(new SpacesItemDecoration(DisplayUtils.dp2px(mActivity, 8)));
        mLocalClubAdapter = new LocalClubAdapter(mActivity);
        mRecyclerLocalClub.setAdapter(mLocalClubAdapter);

//        rv_ugc= (NoScrollRecyclerView) view.findViewById(R.id.dymaic_info_list);
        rv_ugc = mRootView.findViewById(R.id.dynamic_info_list);
        //init RecyclerView
        initRecyclerView_ugc();

        mRootView.findViewById(R.id.home_court_scan).setOnClickListener(this);
        mRootView.findViewById(R.id.home_court_card_package).setOnClickListener(this);

        mRootView.findViewById(R.id.home_court_charge).setOnClickListener(this);
        mRootView.findViewById(R.id.home_court_member_sqcode).setOnClickListener(this);
        mInterestGroup = mRootView.findViewById(R.id.interest_group);
        mJustSee = (TextView) mRootView.findViewById(R.id.just_see);
        mJustSee.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        mDateText = (TextView) mRootView.findViewById(R.id.choose_date);
        mDateText.getPaint().setFakeBoldText(true);
        mDateText.setOnClickListener(this);
        mTimeText = (TextView) mRootView.findViewById(R.id.choose_time);
        mTimeText.getPaint().setFakeBoldText(true);
        mTimeText.setOnClickListener(this);

        mRootView.findViewById(R.id.notification_layout).setOnClickListener(this);
        mRootView.findViewById(R.id.im_img).setOnClickListener(this);
        mRootView.findViewById(R.id.court_message_num).setOnClickListener(this);

        initDateSelectWindow();
        initTimeSelectWindow(hourIndex);

        rl_club_list = (RelativeLayout) mRootView.findViewById(R.id.club_list);
        rl_club_list.setOnClickListener(this);

        mRootView.findViewById(R.id.find_venue).setOnClickListener(this);

        mRootView.findViewById(R.id.close).setOnClickListener(this);
        bannerView = (ImgPagerView) mRootView.findViewById(R.id.home_court_banner);

        mRecommentEmptyLayout = mRootView.findViewById(R.id.recomment_empty_layout);
        mRecommentVeneLayout = mRootView.findViewById(R.id.recommend_vene_layout);
        mRootView.findViewById(R.id.create_sport).setOnClickListener(this);
        badminton_list = (RelativeLayout) mRootView.findViewById(R.id.badminton_list);
        badminton_list.setOnClickListener(this);
        mRootView.findViewById(R.id.venue_sport_list).setOnClickListener(this);

        ll_home_live_video = (LinearLayout) mRootView.findViewById(R.id.home_live_video);

        // 课程推荐
        ll_home_course = mRootView.findViewById(R.id.home_course);
        rycvCourse = mRootView.findViewById(R.id.fragment_home_course_recyclerView);
        rycvCourse.setNestedScrollingEnabled(false);
        rycvCourse.setLayoutManager(new LinearLayoutManager(mActivity));
        mCourseAdapter = new CourseAdapter(mActivity, new ArrayList<>());
        rycvCourse.setAdapter(mCourseAdapter);

        // 驻场培训机构
        ll_home_train = mRootView.findViewById(R.id.home_train);
        rycvTrain = mRootView.findViewById(R.id.fragment_home_train_recyclerView);
        rycvTrain.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false));
        rycvTrain.addItemDecoration(new SpacesItemDecoration(DisplayUtils.dp2px(mActivity, 8)));
        mTrainAdapter = new TrainAdapter(mActivity, new ArrayList<>());
        rycvTrain.setAdapter(mTrainAdapter);

        // 约战广场
        llytInviteFight = mRootView.findViewById(R.id.llyt_invite_fight);
        rlytInviteTitle = mRootView.findViewById(R.id.rlyt_invite_fight);
        rycvInviteList = mRootView.findViewById(R.id.rycv_invite_fight);
        rycvInviteList.setNestedScrollingEnabled(false);
        rycvInviteList.setLayoutManager(new LinearLayoutManager(mActivity));
        mInviteAdapter = new InviteFightAdapter(mActivity, new ArrayList<>());
        rycvInviteList.setAdapter(mInviteAdapter);

        initSelectSport();

        initLiveView(mRootView);
        initCommonItem(mRootView);
        initVenueActivitiesItem(mRootView);
    }

    public void initRecyclerView_ugc() {
        rv_ugc.setLayoutManager(mLayoutManager);
        ugc_adapter = new DynamicAdapter(getActivity(), ugcList);
        ugc_adapter.extraMap = extraMap;

        rv_ugc.setAdapter(ugc_adapter);
        rv_ugc.addItemDecoration(new YhyRecyclerDivider(getResources(), R.color.gray_E, R.dimen.yhy_size_6px, R.dimen.yhy_size_1px, LinearLayoutManager.VERTICAL));
        ((DefaultItemAnimator) rv_ugc.getItemAnimator()).setSupportsChangeAnimations(false);
    }

    @Override
    protected void setListener() {
        super.setListener();

        rlytInviteTitle.setOnClickListener(this);
        mInviteAdapter.setOnItemClickListener((baseQuickAdapter, view, i) -> {
//              ToastUtil.showToast(mActivity, "去约战详情页");   https://h5alpha.yingheying.com/alauda/?#/aboutWar/detail/:arrange_campaign_id
            // 埋点
            HashMap<String, String> params = new HashMap<>();
            params.put("type", "详情");
            params.put("id", String.valueOf(mInviteAdapter.getData().get(i).arrange_campaign_id));
            Analysis.pushEvent(mActivity, AnEvent.BATTLESQUARE, userService.getLoginUserId() > 0 ? String.valueOf(userService.getLoginUserId()) : "0", params);

            String url = SPUtils.getURL_WAR_ID(mActivity);
            if (TextUtils.isEmpty(url))
                return;
            url = url.replace(":arrange_campaign_id", String.valueOf(mInviteAdapter.getData().get(i).arrange_campaign_id));
            NavUtils.startWebview(mActivity, "", url, -1);

        });
        mInviteAdapter.setOnItemChildClickListener((baseQuickAdapter, view, i) -> {
            switch (view.getId()) {
                case R.id.llyt_aa_right_inviting:
//                        ToastUtil.showToast(mActivity, "接受aa约战后刷新页面");
                case R.id.llyt_half_right_inviting:
//                        ToastUtil.showToast(mActivity, "接受半场约战后刷新页面");
                case R.id.llyt_ball_right_inviting:
//                        ToastUtil.showToast(mActivity, "接受aa约球后刷新页面");    https://h5alpha.yingheying.com/alauda/?#/aboutWar/confirmAboutWar/:arrange_campaign_id
                    String url = SPUtils.getURL_CONFIRM_WAR(mActivity);
                    if (TextUtils.isEmpty(url))
                        return;
                    url = url.replace(":arrange_campaign_id", String.valueOf(mInviteAdapter.getData().get(i).arrange_campaign_id));
                    NavUtils.startWebview(mActivity, "", url, -1);

                    break;
            }
        });

        mRootView.findViewById(R.id.fragment_home_ivCourseList).setOnClickListener(this);
        mCourseAdapter.setOnItemClickListener((baseQuickAdapter, view, i) -> {
            // 埋点
            HashMap<String, String> params = new HashMap<>();
            params.put("type", "详情");
            params.put("title", mCourseAdapter.getData().get(i).name);
            params.put("id", String.valueOf(mCourseAdapter.getData().get(i).id));
            Analysis.pushEvent(mActivity, AnEvent.RECOMMENDEDCOURSE, userService.getLoginUserId() > 0 ? String.valueOf(userService.getLoginUserId()) : "0", params);

            // 去课程详情页
            String url = SPUtils.getURL_TRAIN_COURSE_DETAIL(mActivity);
            if (TextUtils.isEmpty(url))
                return;
            url = url.replace(":courseId", String.valueOf(mCourseAdapter.getData().get(i).id));
            NavUtils.startWebview(mActivity, "", url, -1);

        });
        mRootView.findViewById(R.id.fragment_home_train).setOnClickListener(this);
        mTrainAdapter.setOnItemClickListener((baseQuickAdapter, view, i) -> {
            // 埋点
            HashMap<String, String> params = new HashMap<>();
            params.put("type", "详情");
            params.put("Name", mTrainAdapter.getData().get(i).name);
            params.put("id", String.valueOf(mTrainAdapter.getData().get(i).id));
            Analysis.pushEvent(mActivity, AnEvent.TRAININGINSTITUTIONS, userService.getLoginUserId() > 0 ? String.valueOf(userService.getLoginUserId()) : "0", params);

            // 去机构详情页
            String url = SPUtils.getURL_ORG_DETAIL(mActivity);
            if (TextUtils.isEmpty(url))
                return;
            url = url.replace(":orgId", String.valueOf(mTrainAdapter.getData().get(i).id));
            NavUtils.startWebview(mActivity, "", url, -1);
        });


    }

    @Override
    protected void initData() {
        super.initData();
        getCityList(true);

        // 获取约战广场信息
//        getInviteFight();

    }

    private void initSelectSport() {
        //获取用户兴趣类型
        if (userService.isLogin()) {
            User mUserInfo = userService.getUserInfo(userService.getLoginUserId());
            if (mUserInfo == null) {
                return;
            }
            if (mUserInfo.getSportHobby() == -1) {
                mType = 0;
            } else {
                mType = mUserInfo.getSportHobby();
            }
            switch (mUserInfo.getSportHobby()) {
                case NOTSELECT:
                    mInterestLayout.setVisibility(View.VISIBLE);
                    break;
                case OTHER:
                    mInterestLayout.setVisibility(View.GONE);
                    break;
                case YUMAOQIU:
                    mInterestLayout.setVisibility(View.GONE);
                    break;
                case BASKETBALL:
                    mInterestLayout.setVisibility(View.GONE);
                    break;
                case FOOTBALL:
                    mInterestLayout.setVisibility(View.GONE);
                    break;
            }
        } else {
            /*//TODO 判断用户有没有在未登录的情况下 选择兴趣
            int sport = SPUtils.getInt(mActivity, SPORT, 0);
            if (sport == 0) {
                mInterestLayout.setVisibility(View.VISIBLE);
            }else {
                mInterestLayout.setVisibility(View.GONE);
            }*/
            mType = 0;
        }
    }

    private void initVenueActivitiesItem(View view) {
        ll_venue_activities = (LinearLayout) view.findViewById(R.id.recommend_vene_layout);
    }

    private void initCommonItem(View root) {
        ll_common_venue = (LinearLayout) root.findViewById(R.id.fragment_home_llVenue);//推荐场馆
    }

    //初始化精彩视频list
    private void initLiveView(View root) {
        mLiveRecyclerView = (RecyclerView) root.findViewById(R.id.fragment_home_live_recyclerView);
        root.findViewById(R.id.fragment_home_ivLiveList).setOnClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mLiveRecyclerView.setLayoutManager(layoutManager);
        mLiveRecyclerView.addItemDecoration(new SpacesItemDecoration(DisplayUtils.dp2px(getContext(), 8)));
        mLiveVideoAdapter = new LiveVideoAdapter(getContext(), 1);
        mLiveRecyclerView.setAdapter(mLiveVideoAdapter);
    }

    /**
     * init Refresh
     */
    private void initRefresh() {
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            getHomeCourtData();
            getCampaignData();
            getDynamicInfos(true);
            refreshLayout.finishRefresh(1000);
        });

        refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            getCampaignData();
            getDynamicInfos(false);
            refreshLayout.finishLoadMore(1000);
        });
    }

    private void refreshData(View view) {
        View home_activity_recommend_layout = view.findViewById(R.id.home_activity_recommend_layout);

        if (mType == YUMAOQIU || mType == OTHER) {
            home_activity_recommend_layout.setVisibility(View.VISIBLE);
        } else {
            home_activity_recommend_layout.setVisibility(View.GONE);
        }

        getCampaignData();
        getDynamicInfos(true);
        getHomeCourtData();

    }

    /**
     * 获取约战广场信息
     */
    private void getCampaignData() {
        Api_COMPETITION_QueryCampaignParam param = new Api_COMPETITION_QueryCampaignParam();
        param.status = "1";
        param.projecttype = SPUtils.getUserSportHabit(mActivity) < 0 ? 0 : SPUtils.getUserSportHabit(mActivity);
        param.projectstart = System.currentTimeMillis();
        param.dateafter = 1;
        param.pageNo = 1;
        param.pageSize = 2;
        try {
            param.addresscode = Integer.valueOf(LocationManager.getInstance().getStorage().getLast_cityCode());
        } catch (Exception e) {
            e.printStackTrace();
        }

        String lat;
        String lng;

        if (LocationManager.getInstance().getStorage().isGPRSok()) {
            if (PermissionUtils.checkLocationPermission(mActivity)) {
                //如果定位成功则使用定位得到地理位置信息拉取这个接口
                lat = String.valueOf(LocationManager.getInstance().getStorage().getGprs_lat());
                lng = String.valueOf(LocationManager.getInstance().getStorage().getGprs_lng());
            } else {
                //否则使用LastLocation
                lat = LocationManager.getInstance().getStorage().getLast_lat();
                lng = LocationManager.getInstance().getStorage().getLast_lng();
            }
        } else {
            //否则使用LastLocation
            lat = LocationManager.getInstance().getStorage().getLast_lat();
            lng = LocationManager.getInstance().getStorage().getLast_lng();
        }
        param.latitude = lat;
        param.longitude = lng;

        NetManager.getInstance(mActivity).getCampaignData(param, new OnResponseListener<Api_COMPETITION_CampaignsVoResult>() {
            @Override
            public void onComplete(boolean isOK, Api_COMPETITION_CampaignsVoResult result, int errorCode, String errorMsg) {

                if (isOK && result.success) {
                    if (result != null && result.campaignList != null && result.campaignList.size() > 0) {
                        llytInviteFight.setVisibility(View.VISIBLE);
                        mInviteAdapter.setNewData(result.campaignList);
                    } else {
                        llytInviteFight.setVisibility(View.GONE);
                    }
                } else {
                    llytInviteFight.setVisibility(View.GONE);
                }
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                llytInviteFight.setVisibility(View.GONE);

            }
        });
    }


    private String formatDate(int monthIndex, int dayIndex) {

        String curMonth = TimeUtils.formatHour(monthIndex + 1);
        String curDay = TimeUtils.formatHour(dayIndex + 1);
        String str = curMonth + "/" + curDay;
        if (monthIndex == Calendar.getInstance().get(Calendar.MONTH) && dayIndex == Calendar.getInstance().get(Calendar.DAY_OF_MONTH) - 1) {
            str += "（今天）";
        }

        if (monthIndex == Calendar.getInstance().get(Calendar.MONTH) && dayIndex == Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) {
            str += "（明天）";
        }

        return str;
    }

    private void initCityList(View view) {
        mPopupBtnCity = view.findViewById(R.id.location);
        TextPaint tp = mPopupBtnCity.getPaint();
        tp.setFakeBoldText(true);
//        view_city = getActivity().getLayoutInflater().inflate(R.layout.popup_venue_two, null);
//        lv_city = view_city.findViewById(R.id.two_parent_lv);
//        lv_district = view_city.findViewById(R.id.two_child_lv);
        mPopupBtnCity.setPopupView(null, 360);
        mPopupBtnCity.setListener(new PopupButton.PopupButtonListener() {
            @Override
            public void onShow() {
//                if (view_city != null) {
//                    initLocationDialogTittle(view_city);
//                }
//                mPopupBtnCity.setSelected(true);
                YhyRouter.getInstance().startCitySelectActivity(mActivity, false, AnArgs.Instance().build(Analysis.UID, userService.getLoginUserId() > 0 ? userService.getLoginUserId() + "" : "0")
                        .build(Analysis.PAGENAME, "主场")
                        .getMap());
            }

            @Override
            public void onHide() {
                mPopupBtnCity.setSelected(false);
            }
        });

//        view_city.findViewById(R.id.rl_get_location).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                matchGpsCityName();
//                mPopupBtnCity.hidePopup();
//            }
//        });
    }

    private void matchGpsCityName() {
//        int cityPosition = 0;
//        int districtPosition = 0;
//        boolean cityMatch = false;

        for (int i = 0; i < mCityList.size(); i++) {
            if (LocationManager.getInstance().getStorage().getGprs_cityName().equals(mCityList.get(i).name)) {
//                cityPosition = i;
//                cityMatch = true;
                LocationManager.getInstance().getStorage().setManualLocation(mCityList.get(i).name, mCityList.get(i).cityCode,
                        "全部",
                        "-1",
                        mCityList.get(i).lat, mCityList.get(i).lng, mCityList.get(i).isPublic);

                EventBus.getDefault().post(new EvBusLocationChange(mCityList.get(i), false));
//            mSwitchCityAdapter.setPressPostion(cityPosition);
//            mSwitchDistrictAdapter.setPressPostion(districtPosition);
                //刷新一遍主场的数据
//                getCampaignData();
//                getDynamicInfos(true);
//                getHomeCourtData();
//                if (LocationManager.getInstance().getStorage().getManual_discName().contains("全")) {
//                    mPopupBtnCity.setText(LocationManager.getInstance().getStorage().getManual_cityName());
//                } else {
//                    mPopupBtnCity.setText(LocationManager.getInstance().getStorage().getManual_cityName() + " " + LocationManager.getInstance().getStorage().getManual_discName());
//                }
                Analysis.cityAnalysis(getActivity(), userService.getLoginUserId(), mCityList.get(i).name + " " + "全部", "主场");
                return;
            }
        }
//        if (cityMatch) {
//            LocationManager.getInstance().getStorage().setManualLocation(mCityList.get(cityPosition).name, mCityList.get(cityPosition).cityCode,
//                    "全部",
//                    "-1",
//                    mCityList.get(cityPosition).lat, mCityList.get(cityPosition).lng, mCityList.get(cityPosition).isPublic);
//
//            EventBus.getDefault().post(new EvBusLocationChange(cityPosition, districtPosition));
////            mSwitchCityAdapter.setPressPostion(cityPosition);
////            mSwitchDistrictAdapter.setPressPostion(districtPosition);
//            //刷新一遍主场的数据
//            getCampaignData();
//            getDynamicInfos(true);
//            getHomeCourtData();
//            if (LocationManager.getInstance().getStorage().getManual_discName().contains("全")) {
//                mPopupBtnCity.setText(LocationManager.getInstance().getStorage().getManual_cityName());
//            } else {
//                mPopupBtnCity.setText(LocationManager.getInstance().getStorage().getManual_cityName() + " " + LocationManager.getInstance().getStorage().getManual_discName());
//            }
//            Analysis.cityAnalysis(getActivity(), userService.getLoginUserId(), mCityList.get(cityPosition).name + " " + "全部", "主场");
//        }
    }

    /**
     * 获取位置信息,更新dialog的tittle
     *
     * @param view_city
     */
//    private void initLocationDialogTittle(View view_city) {
//        if (!PermissionUtils.checkLocationPermission(getActivity())) {
//            view_city.findViewById(R.id.rl_no_location).setVisibility(View.VISIBLE);
//            view_city.findViewById(R.id.rl_get_location).setVisibility(View.GONE);
//            view_city.findViewById(R.id.tv_go_setting).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mPopupBtnCity.hidePopup();
//                    SystemUtils.getAppDetailSettingIntent(getActivity());
//                }
//            });
//        } else {
//            view_city.findViewById(R.id.rl_get_location).setVisibility(View.VISIBLE);
//            view_city.findViewById(R.id.rl_no_location).setVisibility(View.GONE);
//            TextView iv_location = view_city.findViewById(R.id.iv_location);
//            iv_location.setText(LocationManager.getInstance().getStorage().getGprs_cityName());
//        }
//        synLocationSelectedState(LocationManager.getInstance().getStorage().getManual_cityName(), LocationManager.getInstance().getStorage().getManual_discName());
//    }

    /**
     * 同步下拉选项中位置列表的选择
     */
//    private void synLocationSelectedState(String city, String district) {
//    int cityPosition = 0;
//    int districtPosition = 0;

//        for (int i = 0; i < mCityList.size(); i++) {
//            if (city.equals(mCityList.get(i).name)) {
//                cityPosition = i;
//                break;
//            }
//        }
//
//        mDistrictList.clear();
//        if (mCityList.size() == 0) {
//            return;
//        }
//        mDistrictList.addAll(mCityList.get(cityPosition).districtList);
//
//        for (int j = 0; j < mDistrictList.size(); j++) {
//            if (district.equals(mDistrictList.get(j).name)) {
//                districtPosition = j;
//                break;
//            }
//        }
//
//        mSwitchCityAdapter.setPressPostion(cityPosition);
//        mSwitchDistrictAdapter.setPressPostion(districtPosition);

//    }

//    private void cityListReLoad() {
//        if (getActivity() == null) {
//            return;
//        }
//        mDistrictList.addAll(mCityList.get(0).districtList);
//        mSwitchCityAdapter = new PopNormalAdapter_OrderVenue<>(getActivity(), R.layout.popup_left_item, mCityList);
//        mSwitchDistrictAdapter = new PopNormalAdapter_OrderVenue<>(getActivity(), R.layout.popup_right_item, mDistrictList);

//        for (int i = 0; i < mCityList.size(); i++) {
//            if (LocationManager.getInstance().getStorage().getManual_cityName().equals(mCityList.get(i).name)) {
//                mSwitchCityAdapter.setPressPostion(i);
//                mCityPosition = i;
//                mDistrictList.clear();
//                mDistrictList.addAll(mCityList.get(i).districtList);
//                for (int j = 0; j < mDistrictList.size(); j++) {
//                    if (LocationManager.getInstance().getStorage().getManual_discName().equals(mDistrictList.get(j).name)) {
//                        mSwitchDistrictAdapter.setPressPostion(j);
//                        mPopupBtnCity.setText(mCityList.get(i).name);
//                        mDistrictPosition = j;
//                        break;
//                    }
//                }
//                break;
//            }
//        }

//        lv_city.setAdapter(mSwitchCityAdapter);
//        lv_district.setAdapter(mSwitchDistrictAdapter);
//        lv_city.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Api_PLACE_City respCity = mCityList.get(position);
//                mSwitchCityAdapter.setPressPostion(position);
//                mSwitchCityAdapter.notifyDataSetChanged();
//                mDistrictList.clear();
//                mDistrictList.addAll(respCity.districtList);
//                mSwitchDistrictAdapter.notifyDataSetChanged();
//                mSwitchDistrictAdapter.setPressPostion(0);
//                PreferencesUtils.putInt(YHYBaseApplication.getInstance(), "searchCityId", mCityId);
//                //104_场馆_列表_城市
//            }
//        });
//
//        lv_district.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                mSwitchDistrictAdapter.setPressPostion(position);
//                mSwitchDistrictAdapter.notifyDataSetChanged();
//                mPopupBtnCity.hidePopup();
//
//                Api_PLACE_City respCity = mCityList.get(mSwitchCityAdapter.getPressPostion());
//                if (position == 0) {
//                    mPopupBtnCity.setText(respCity.name);
//                } else {
//                    mPopupBtnCity.setText(respCity.name + " " + mDistrictList.get(position).name);
//                }
//
//
//                Analysis.cityAnalysis(getActivity(), userService.getLoginUserId(), "changeCity" + respCity.name + " " + mDistrictList.get(position).name, "主场");
//
//                //按下Item更新 ManualLocation
//                LocationManager.getInstance().getStorage().setManualLocation(respCity.name, respCity.cityCode,
//                        mDistrictList.get(position).name, mDistrictList.get(position).districtCode,
//                        mDistrictList.get(position).lat, mDistrictList.get(position).lng);
//
//                mCityPosition = mSwitchCityAdapter.getPressPostion();
//                mDistrictPosition = position;
//
//                EventBus.getDefault().post(new EvBusLocationChange(mCityPosition, mDistrictPosition));
//                //刷新一遍主场的数据
//                getCampaignData();
//                getDynamicInfos(true);
//                getHomeCourtData();
//            }
//        });
//    }
    private void initSportData(LinearLayout glSignedHead, List<Api_RESOURCECENTER_JoinActivityUserDto> joins, ImageView ivMore) {
        if (joins != null && joins.size() > 0) {
            glSignedHead.removeAllViews();
            if (joins.size() > 6) {   //  显示不超过6个
                ivMore.setVisibility(View.VISIBLE);
                for (int j = 0; j < 6; j++) {
                    String head = joins.get(j).avatar;
//                    if (head != null) {
                    ImageView simpleDraweeView = new ImageView(getContext());
                    int width = getResources().getDimensionPixelSize(R.dimen.value_20dp);
                    int height = getResources().getDimensionPixelSize(R.dimen.value_20dp);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
                    params.rightMargin = DisplayUtils.dp2px(getContext(), 5);
                    simpleDraweeView.setLayoutParams(params);
                    glSignedHead.addView(simpleDraweeView);
                    ImageLoadManager.loadCircleImage(CommonUtil.getImageFullUrl(head), R.mipmap.icon_default_avatar, simpleDraweeView);

//                    }
                }
            } else {
                ivMore.setVisibility(View.GONE);
                for (Api_RESOURCECENTER_JoinActivityUserDto head : joins) {
//                    if (head != null) {
                    ImageView simpleDraweeView = new ImageView(getContext());
                    int width = getResources().getDimensionPixelSize(R.dimen.value_20dp);
                    int height = getResources().getDimensionPixelSize(R.dimen.value_20dp);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
                    params.rightMargin = DisplayUtils.dp2px(getContext(), 5);
                    simpleDraweeView.setLayoutParams(params);
                    glSignedHead.addView(simpleDraweeView);

                    ImageLoadManager.loadCircleImage(CommonUtil.getImageFullUrl(head.avatar), R.mipmap.icon_default_avatar, simpleDraweeView);

//                    }
                }
            }
        } else {
            glSignedHead.removeAllViews();
            ivMore.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.home_court_scan:
                //事件统计
                Analysis.pushEvent(mActivity, AnEvent.SCAVENGING);
                NavUtils.gotoCaptureActivity(mActivity, 2);
                break;
            case R.id.home_court_charge:
                if (userService.isLogin()) {
                    //事件统计
                    Analysis.pushEvent(mActivity, AnEvent.RECHARGE);
                    NavUtils.startWebview(mActivity, getResources().getString(R.string.charge), SPUtils.getRecharge(mActivity), 10002);
                } else {
                    NavUtils.gotoLoginActivity(mActivity);
                }
                break;
            case R.id.home_court_member_sqcode:
                if (userService.isLogin()) {
                    //事件统计
                    Analysis.pushEvent(getActivity(), AnEvent.THE_MEMBER_CODE);
                    NavUtils.startWebview(mActivity, getResources().getString(R.string.member_sqcode), SPUtils.getMemberCode(mActivity), 10001);
                } else {
                    NavUtils.gotoLoginActivity(mActivity);
                }
                break;
            case R.id.home_court_card_package:
                if (userService.isLogin()) {
                    //事件统计
                    Analysis.pushEvent(getActivity(), AnEvent.CARD_BAG);
                    NavUtils.startWebview(mActivity, getResources().getString(R.string.card_package), SPUtils.getCardBag(mActivity), 10003);
                } else {
                    NavUtils.gotoLoginActivity(mActivity);
                }
                break;

            case R.id.choose_date:
                //事件统计
                Analysis.pushEvent(getActivity(), AnEvent.FAST_LOOKUP_TIME_SELECTION);
                mDateDialog.show();
                break;
            case R.id.choose_time:
                //事件统计
                Analysis.pushEvent(getActivity(), AnEvent.FAST_LOOKUP_TIME_SELECTION);
                mTimeDialog.show();
                break;
            case R.id.notification_layout:
                NavUtils.gotoNotificationListActivity(mActivity, 0);
                break;
            case R.id.im_img:
            case R.id.court_message_num:
                //事件统计
                Analysis.pushEvent(getActivity(), AnEvent.MYMESSAGE);
                NavUtils.gotoMsgCenter(mActivity);
                break;
            case R.id.club_list:
                NavUtils.startWebview(getActivity(), getResources().getString(R.string.local_clubs),
                        SPUtils.getClubList(mActivity) + "?cityCode=" + LocationManager.getInstance().getStorage().getLast_cityCode() +
                                "&districtCode=" + LocationManager.getInstance().getStorage().getLast_discCode(), 10004);
                break;
            case R.id.fragment_home_ivLiveList: //直播列表
                //事件统计
                Analysis.pushEvent(getActivity(), AnEvent.LIVE_VIDEO);
                YhyRouter.getInstance().startLiveListActivity(getContext());
//                NavUtils.startWebview(getActivity(), "", SPUtils.getURLLIVELIST(getContext()), 0);
                break;
            case R.id.badminton_list:     // 推荐场馆
//                http://192.168.17.41:8080/venue/#/linkRout/:citycode/:type/:ispublic
//
//                ispublic=1的是第三方场馆
//                type:类别 ，足球，篮球
//                citycode:城市 code
                //事件统计
                Analysis.pushEvent(getActivity(), AnEvent.RECOMMEND_THE_GYM);
//                if (LocationManager.getInstance().getStorage().getLast_isPublic() == 1) {
//                    String url = SPUtils.getRecommend(mActivity);
//                    if (TextUtils.isEmpty(url))
//                        return;
//                    NavUtils.startWebview(getActivity(), "",
//                            url.replace(":cityCode", LocationManager.getInstance().getStorage().getLast_cityCode()) +
//                                    (mType == 0 ? "/-1" : ("/" + mType)) + ("/" + LocationManager.getInstance().getStorage().getLast_isPublic()), 10006);
//
//                } else {
                String url = SPUtils.getNewRecommend(mActivity);
                if (TextUtils.isEmpty(url))
                    return;
                //:categoryId/:ispublic
                url = url.replace(":cityCode", LocationManager.getInstance().getStorage().getLast_cityCode());
                url = url.replace(":categoryId", mType == 0 ? "0" : mType + "");
                url = url.replace(":ispublic", LocationManager.getInstance().getStorage().getLast_isPublic() + "");
                NavUtils.startWebview(getActivity(), "", url, 10006);

//                }
                break;
            case R.id.basketball_list: {
//                if (LocationManager.getInstance().getStorage().getLast_isPublic() == 1) {
//                    String url = SPUtils.getRecommend(mActivity);
//                    if (TextUtils.isEmpty(url))
//                        return;
//                    NavUtils.startWebview(getActivity(), "",
//                            url.replace(":cityCode", LocationManager.getInstance().getStorage().getLast_cityCode()) +
//                                    (mType == 0 ? "/-1" : ("/" + mType)) + ("/" + LocationManager.getInstance().getStorage().getLast_isPublic()), 10006);
//
//                } else {
                String url1 = SPUtils.getNewRecommend(mActivity);
                if (TextUtils.isEmpty(url1))
                    return;
                //:categoryId/:ispublic
                url1 = url1.replace(":cityCode", LocationManager.getInstance().getStorage().getLast_cityCode());
                url1 = url1.replace(":categoryId", mType == 0 ? "0" : mType + "");
                url1 = url1.replace(":ispublic", LocationManager.getInstance().getStorage().getLast_isPublic() + "");
                NavUtils.startWebview(getActivity(), "", url1, 10006);

//                }
            }
            break;
            case R.id.venue_sport_list:
                //事件统计
                Analysis.pushEvent(getActivity(), AnEvent.GYM_ACTIVITY);
                String venuurl = getWebViewUrl(SPUtils.getVenueActivityList(getContext()));
                if (TextUtils.isEmpty(venuurl))
                    return;
                if (!TextUtils.isEmpty(LocationManager.getInstance().getStorage().getLast_discCode())) {
                    venuurl = venuurl + "&districtCode=" + LocationManager.getInstance().getStorage().getLast_discCode();
                }
                NavUtils.startWebview(getActivity(), "场馆活动", venuurl, 0);
                break;
            case R.id.basket_ticket_item1:
                NavUtils.startWebview(mActivity, getResources().getString(R.string.recommend_venue),
                        SPUtils.getVendeDetail(mActivity), 10012);
                break;
            case R.id.basket_ticekt_item2:
                NavUtils.startWebview(mActivity, getResources().getString(R.string.recommend_venue),
                        SPUtils.getVendeDetail(mActivity), 10013);
                break;
            case R.id.more_ticket:
                //事件统计
                Analysis.pushEvent(getActivity(), AnEvent.GYMNASIUM_LIST);
                NavUtils.startWebview(getActivity(), getResources().getString(R.string.recommend_venue),
                        SPUtils.getBasketballBooking(mActivity), 10014);
                break;
            case R.id.find_venue:   //   快速找场
                //事件统计
            {
                Analysis.pushEvent(getActivity(), AnEvent.FAST_LOOKUP);
//                if (LocationManager.getInstance().getStorage().getLast_isPublic() == 1) {
//                    String url = SPUtils.getRecommend(mActivity);
//                    if (TextUtils.isEmpty(url))
//                        return;
//                    NavUtils.startWebview(getActivity(), "",
//                            url.replace(":cityCode", LocationManager.getInstance().getStorage().getLast_cityCode()) +
//                                    (mType == 0 ? "/-1" : ("/" + mType)) + ("/" + LocationManager.getInstance().getStorage().getLast_isPublic()), 10006);
//
//                } else {
                String url2 = SPUtils.getNewRecommend(mActivity);
                if (TextUtils.isEmpty(url2))
                    return;
                //:categoryId/:ispublic
                url2 = url2.replace(":cityCode", LocationManager.getInstance().getStorage().getLast_cityCode());
                url2 = url2.replace(":categoryId", mType == 0 ? "0" : mType + "");
                url2 = url2.replace(":ispublic", LocationManager.getInstance().getStorage().getLast_isPublic() + "");
                NavUtils.startWebview(getActivity(), "", url2, 10006);

//                }
            }
            break;
            case R.id.close:
                mType = OTHER;
                doSelectSport(OTHER);
                mInterestLayout.setVisibility(View.GONE);
                break;
            case R.id.badminton_radio:
                mType = YUMAOQIU;
                doSelectSport(YUMAOQIU);
                mInterestLayout.setVisibility(View.GONE);
                break;
            case R.id.football_radio:
                mType = FOOTBALL;
                doSelectSport(FOOTBALL);
                mInterestLayout.setVisibility(View.GONE);
                break;
            case R.id.basketball_radio:
                mType = BASKETBALL;
                doSelectSport(BASKETBALL);
                mInterestLayout.setVisibility(View.GONE);
                break;
            case R.id.just_see:
                mType = OTHER;
                doSelectSport(OTHER);
                mInterestLayout.setVisibility(View.GONE);
                break;
            case R.id.dynamic_refresh:
                Analysis.pushEvent(mActivity, AnEvent.CLUB_CHANNEL_RECENT_NEWS);
                mDynamicRefresh.setText("点击查看最新动态");
                getDynamicInfos(true);
                break;
            case R.id.create_sport:
                if (!userService.isLogin()) {
                    NavUtils.gotoLoginActivity(mActivity);
                } else {
                    NavUtils.startWebview(mActivity, getResources().getString(R.string.create_sport),
                            SPUtils.getAddClubAct(mActivity), 10020);
                }
                break;
            case R.id.create_dynamic:
                NavUtils.gotoAddLiveActivity(mActivity);

                break;
            case R.id.rlyt_invite_fight:            // 约战广场
                // 埋点
                HashMap<String, String> paramsFight = new HashMap<>();
                paramsFight.put("type", "列表");
                Analysis.pushEvent(mActivity, AnEvent.BATTLESQUARE, userService.getLoginUserId() > 0 ? String.valueOf(userService.getLoginUserId()) : "0", paramsFight);

                NavUtils.startWebview(mActivity, "", SPUtils.getURL_WAR_SQUARE(mActivity), -1);
                break;
            case R.id.fragment_home_ivCourseList:   // 课程推荐列表
                // 埋点
                HashMap<String, String> paramsCourse = new HashMap<>();
                paramsCourse.put("type", "列表");
                Analysis.pushEvent(mActivity, AnEvent.RECOMMENDEDCOURSE, userService.getLoginUserId() > 0 ? String.valueOf(userService.getLoginUserId()) : "0", paramsCourse);

                NavUtils.startWebview(mActivity, "", SPUtils.getURL_TRAIN_COURSE_LIST(mActivity), -1);
                break;
            case R.id.fragment_home_train:          // 驻场培训机构列表
                // 埋点
                HashMap<String, String> paramsTrain = new HashMap<>();
                paramsTrain.put("type", "列表");
                Analysis.pushEvent(mActivity, AnEvent.RECOMMENDEDCOURSE, userService.getLoginUserId() > 0 ? String.valueOf(userService.getLoginUserId()) : "0", paramsTrain);

                NavUtils.startWebview(mActivity, "", SPUtils.getURL_ORG_LIST(mActivity), -1);
                break;

        }
    }

    private void doSelectSport(int sport) {
        if (userService.isLogin()) {
            //TODO 保存信息,上传个人信息

            User loginUser = userService.getUserInfo(userService.getLoginUserId());
            if (loginUser == null) {
                loginUser = new User();
            }
            loginUser.setSportHobby(sport);
            NetManager.getInstance(mActivity).doUpdateUserProfile(loginUser, new OnResponseListener<Boolean>() {
                @Override
                public void onComplete(boolean isOK, Boolean result, int errorCode, String errorMsg) {
                    if (isOK && result) {
                        refreshData(mRootView);
                    }
                }

                @Override
                public void onInternError(int errorCode, String errorMessage) {
                    refreshData(mRootView);
                }
            });
        }
    }


    private void initDateSelectWindow() {

        monthIndex = Calendar.getInstance().get(Calendar.MONTH);
        dayIndex = Calendar.getInstance().get(Calendar.DAY_OF_MONTH) - 1;
        hourIndex = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

        if (hourIndex >= 0 && hourIndex < 18) {
            hourIndex = 18;
            mTimeText.setText(18 + ":00-" + 20 + ":00");
            mDateText.setText(formatDate(monthIndex, dayIndex));
        } else if (hourIndex >= 18 && hourIndex < 22) {
            hourIndex++;
            mTimeText.setText(hourIndex + ":00-" + (hourIndex + 2) + ":00");
            mDateText.setText(formatDate(monthIndex, dayIndex));
            hourIndex++;
        } else if (hourIndex >= 22 && hourIndex < 24) {
            hourIndex = 18;
            dayIndex++;
            mTimeText.setText(18 + ":00-" + 20 + ":00");
            mDateText.setText(formatDate(monthIndex, dayIndex));
        }

        mOldDayIndex = dayIndex;
        mOldMonthIndex = monthIndex;

        if (mDateDialog == null) {
            View dialogView = View.inflate(mActivity, R.layout.dialog_date_view, null);
            mDateDialog = new DialogBuilder(mActivity)
                    .setStyle(R.style.kangzai_dialog)
                    .setContentView(dialogView)
                    .setCanceledOnTouchOutside(true)
                    .setWidth(ScreenSize.getScreenWidth(mActivity) - 100)
                    .setGravity(Gravity.CENTER)
                    .setAnimation(R.anim.pop_enter_anim)
                    .build();

            wvMonth = dialogView.findViewById(R.id.wv_month);
            wvDay = dialogView.findViewById(R.id.wv_day);
            wvMonth.setEntries(new String[]{"1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"});
            wvMonth.setOnWheelChangedListener(new OnWheelChangedListener() {
                @Override
                public void onChanged(WheelView wheel, int oldIndex, int newIndex) {
                    mMonthString = (String) wvMonth.getItem(newIndex);
                    mMonth = Integer.parseInt(mMonthString.substring(0, mMonthString.length() - 1));

                    updateDayEntries(mMonth);

                }
            });
            wvDay.setOnWheelChangedListener(new OnWheelChangedListener() {
                @Override
                public void onChanged(WheelView wheel, int oldIndex, int newIndex) {
                    mDayString = (String) wvDay.getItem(newIndex);
                    mDay = Integer.parseInt(mDayString.substring(0, mDayString.length() - 1));

                }
            });
            mMonth = monthIndex + 1;
            updateDayEntries(monthIndex + 1);
            dialogView.findViewById(R.id.dialog_ok).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOldMonthIndex = wvMonth.getCurrentIndex();
                    mOldDayIndex = wvDay.getCurrentIndex();
                    mDateDialog.dismiss();
                    mDateText.setText(formatDate(mMonth - 1, mDay - 1));
                }
            });
            dialogView.findViewById(R.id.dialog_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDateDialog.dismiss();
                }
            });
            mDateDialog.setCanceledOnTouchOutside(true);
            mDateDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    wvMonth.setCurrentIndex(mOldMonthIndex);
                    wvDay.setCurrentIndex(mOldDayIndex);
                }
            });
        }
    }

    private void initTimeSelectWindow(int hourStartIndex) {

        mOldStartHourIndex = hourStartIndex;
        mOldEndHourIndex = mOldStartHourIndex + 2;

        if (mTimeDialog == null) {
            View dialogView = View.inflate(mActivity, R.layout.dialog_time_view, null);
            mTimeDialog = new DialogBuilder(mActivity)
                    .setStyle(R.style.kangzai_dialog)
                    .setContentView(dialogView)
                    .setCanceledOnTouchOutside(true)
                    .setWidth(ScreenSize.getScreenWidth(mActivity) - 100)
                    .setGravity(Gravity.CENTER)
                    .setAnimation(R.anim.pop_enter_anim)
                    .build();

            mTimeDialog.setCanceledOnTouchOutside(true);

            wvTime_start = dialogView.findViewById(R.id.wv_time_start);
            wvTime_end = dialogView.findViewById(R.id.wv_time_end);

            wvTime_start.setEntries(TimeUtils.generateTimes(0, 24));
            wvTime_start.setOnWheelChangedListener(new OnWheelChangedListener() {
                @Override
                public void onChanged(WheelView wheel, int oldIndex, int newIndex) {
                    mStartTimeString = (String) wvTime_start.getItem(newIndex);
                }
            });
            wvTime_end.setEntries(TimeUtils.generateTimes(0, 24));
            wvTime_end.setOnWheelChangedListener(new OnWheelChangedListener() {
                @Override
                public void onChanged(WheelView wheel, int oldIndex, int newIndex) {
                    mEndTimeString = (String) wvTime_end.getItem(newIndex);

                }
            });
            dialogView.findViewById(R.id.dialog_ok).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOldStartHourIndex = wvTime_start.getCurrentIndex();
                    mOldEndHourIndex = wvTime_end.getCurrentIndex();
                    mTimeDialog.dismiss();
                    mTimeText.setText(mStartTimeString + "-" + mEndTimeString);
                }
            });
            dialogView.findViewById(R.id.dialog_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mTimeDialog.dismiss();
                }
            });

            mTimeDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    wvTime_start.setCurrentIndex(mOldStartHourIndex);
                    wvTime_end.setCurrentIndex(mOldEndHourIndex);
                }
            });
        }

    }

    public static int calculateDaysInMonth(int year, int month) {
        // 添加大小月月份并将其转换为list,方便之后的判断
        String[] bigMonths = {"1", "3", "5", "7", "8", "10", "12"};
        String[] littleMonths = {"4", "6", "9", "11"};
        List<String> bigList = Arrays.asList(bigMonths);
        List<String> littleList = Arrays.asList(littleMonths);
        // 判断大小月及是否闰年,用来确定"日"的数据
        if (bigList.contains(String.valueOf(month))) {
            return 31;
        } else if (littleList.contains(String.valueOf(month))) {
            return 30;
        } else {
            if (year <= 0) {
                return 29;
            }
            // 是否闰年
            if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
                return 29;
            } else {
                return 28;
            }
        }
    }

    private void updateDayEntries(int month) {
        Calendar calendar = Calendar.getInstance();
//
//        calendar.set(Calendar.MONTH,mMonth-1);
//
//        int days = calendar.getActualMaximum(Calendar.DATE);

        int days = calculateDaysInMonth(calendar.get(Calendar.YEAR), month);

        switch (days) {
            case 28:
                wvDay.setEntries(new String[]{
                        "1日", "2日", "3日", "4日", "5日", "6日", "7日", "8日", "9日", "10日",
                        "11日", "12日", "13日", "14日", "15日", "16日", "17日", "18日", "19日", "20日",
                        "21日", "22日", "23日", "24日", "25日", "26日", "27日", "28日"});
                break;
            case 29:
                wvDay.setEntries(new String[]{
                        "1日", "2日", "3日", "4日", "5日", "6日", "7日", "8日", "9日", "10日",
                        "11日", "12日", "13日", "14日", "15日", "16日", "17日", "18日", "19日", "20日",
                        "21日", "22日", "23日", "24日", "25日", "26日", "27日", "28日", "29日"});
                break;
            case 30:
                wvDay.setEntries(new String[]{
                        "1日", "2日", "3日", "4日", "5日", "6日", "7日", "8日", "9日", "10日",
                        "11日", "12日", "13日", "14日", "15日", "16日", "17日", "18日", "19日", "20日",
                        "21日", "22日", "23日", "24日", "25日", "26日", "27日", "28日", "29日", "30日"});
                break;
            case 31:
            default:
                wvDay.setEntries(new String[]{
                        "1日", "2日", "3日", "4日", "5日", "6日", "7日", "8日", "9日", "10日",
                        "11日", "12日", "13日", "14日", "15日", "16日", "17日", "18日", "19日", "20日",
                        "21日", "22日", "23日", "24日", "25日", "26日", "27日", "28日", "29日", "30日",
                        "31日"});
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (bannerView != null) {
            bannerView.startAutoScroll();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (bannerView != null) {
            bannerView.stopAutoScroll();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
//        mActivity.unregisterReceiver(broadcastReceiver);
        EventBus.getDefault().unregister(this);
        if (mHandler != null && runnable != null) {
            mHandler.removeCallbacks(runnable);// 关闭定时器处理
        }
    }

    public void updateIMMessageCount(int messageNum) {
        if (messageNum == 0) {
            mUnReadNum.setVisibility(View.INVISIBLE);
        } else {
            mUnReadNum.setVisibility(View.VISIBLE);
            mUnReadNum.setText("" + messageNum);
        }
    }

    public void getUgcNewAddCountByTime(long lastRequestTime) {
        NetManager.getInstance(mActivity).doGetUgcNewAddCountByTime(
                Double.valueOf(LocationManager.getInstance().getStorage().getLast_lat()), Double.valueOf(LocationManager.getInstance().getStorage().getLast_lng()),
                LocationManager.getInstance().getStorage().getLast_cityCode(), mType, lastRequestTime, new OnResponseListener<Api_SNSCENTER_UgcCountResultList>() {
                    @Override
                    public void onComplete(boolean isOK, Api_SNSCENTER_UgcCountResultList result, int errorCode, String errorMsg) {
                        getDynamicInfosTime = System.currentTimeMillis();
                        if (mHandler != null && runnable != null) {
                            mHandler.removeCallbacks(runnable);
                            mHandler.postDelayed(runnable, 120000);
                        }
                        if (result != null && result.newCount > 0) {
                            mDynamicRefresh.setText("主场有" + result.newCount + "条球友动态更新,点击刷新");
                        }
                    }

                    @Override
                    public void onInternError(int errorCode, String errorMessage) {
                        if (mHandler != null && runnable != null) {
                            mHandler.removeCallbacks(runnable);
                            mHandler.postDelayed(runnable, 120000);
                        }
                    }
                });
    }

    public void getDynamicInfos(final boolean isRefresh) {

        if (isRefresh) {
            mCurPageIndex = 1;
        } else {
            mCurPageIndex++;
        }
//        showLoadingView(null);
        NetManager.getInstance(mActivity).doGetHomeCourtUGCData(mCurPageIndex, 10, mUgcType, LocationManager.getInstance().getStorage().getLast_cityCode(),
                Double.valueOf(LocationManager.getInstance().getStorage().getLast_lat()), Double.valueOf(LocationManager.getInstance().getStorage().getLast_lng()), new OnResponseListener<UgcInfoResultList>() {

                    @Override
                    public void onComplete(boolean isOK, UgcInfoResultList result, int errorCode, String errorMsg) {
                        List<UgcInfoResult> ugcInfoList = result.ugcInfoList;


                        Log.e("minrui", "ugcInfoList=" + ugcInfoList);
                        if (ugcInfoList == null || ugcInfoList.size() == 0) {
                            if (mCurPageIndex == 1) {//刷新未获得数据
                                mDynamicEmpty.setVisibility(View.VISIBLE);
                                rv_ugc.setVisibility(View.GONE);
                                mDynamicRefresh.setVisibility(View.GONE);
                            }
                        } else {
                            mDynamicRefresh.setVisibility(View.VISIBLE);
                            rv_ugc.setVisibility(View.VISIBLE);
                            mDynamicEmpty.setVisibility(View.GONE);
                            if (isRefresh) {
                                ugcList.clear();
                                ugcList.addAll(ugcInfoList);
                                //mDynamicInfoAdapter.setUgcData(ugcInfoList);
                                getDynamicInfosTime = System.currentTimeMillis();
                                if (mHandler != null && runnable != null) {
                                    mHandler.removeCallbacks(runnable);
                                    mHandler.postDelayed(runnable, 120000);
                                }
                            } else {
                                ugcList.addAll(ugcInfoList);
                                //mDynamicInfoAdapter.addUgcData(ugcInfoList);
                            }
                            ugc_adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onInternError(int errorCode, String errorMessage) {
                        if (isRefresh && mHandler != null && runnable != null) {
                            mHandler.removeCallbacks(runnable);
                            mHandler.postDelayed(runnable, 120000);
                        }
                    }
                });
    }

    public void getHomeCourtData() {

        double lat;
        double lng;

        if (LocationManager.getInstance().getStorage().isGPRSok()) {
            if (PermissionUtils.checkLocationPermission(mActivity)) {
                //如果定位成功则使用定位得到地理位置信息拉取这个接口
                lat = LocationManager.getInstance().getStorage().getGprs_lat();
                lng = LocationManager.getInstance().getStorage().getGprs_lng();
            } else {
                //否则使用LastLocation
                lat = Double.valueOf(LocationManager.getInstance().getStorage().getLast_lat());
                lng = Double.valueOf(LocationManager.getInstance().getStorage().getLast_lng());
            }
        } else {
            //否则使用LastLocation
            lat = Double.valueOf(LocationManager.getInstance().getStorage().getLast_lat());
            lng = Double.valueOf(LocationManager.getInstance().getStorage().getLast_lng());
        }

        NetManager.getInstance(mActivity).doGetHomeCourtData(mPopupBtnCity.getText().toString(), lat, lng, LocationManager.getInstance().getStorage().getLast_cityCode(), mType, new OnResponseListener<Api_RESOURCECENTER_HomeInfoResult>() {
            @Override
            public void onComplete(boolean isOK, Api_RESOURCECENTER_HomeInfoResult result, int errorCode, String errorMsg) {

                mActivitys = result.activitys;

                //加载轮播图
                List<String> imagePaths = new ArrayList<>();
                if (result.banners != null && result.banners.size() > 0) {
                    for (Api_RESOURCECENTER_BannerDto banner : result.banners) {
                        imagePaths.add(ContextHelper.getImageUrl() + banner.cover);
                    }
                }
                bannerView.setImgs(imagePaths);
                bannerView.startAutoScroll();

                final List<Api_RESOURCECENTER_BannerDto> banners = result.banners;
                List<Api_RESOURCECENTER_FieldClubDto> clubs = result.clubs;
                List<Api_RESOURCECENTER_RecommendPlaceDto> places = result.places;

                if (result.places != null && result.places.size() > 0) {
                    mfastBookingPlaceId = result.places.get(0).placeId;
                }

                bannerView.setOnImageClickListner(new ImgPagerView.OnImageClickListner() {

                    @Override
                    public void onImageClick(int currentImagePosition) {
                        if (TextUtils.isEmpty(banners.get(currentImagePosition).url)) {
                            return;
                        }
                        //事件统计
                        HashMap<String, String> map = AnArgs.Instance().build(AnArgs.URL, banners.get(currentImagePosition).url).getMap();
                        Analysis.pushEvent(mActivity, AnEvent.HOMEBANNER, String.valueOf(banners.get(currentImagePosition).bannerId), map);

                        NavUtils.startWebview(mActivity, "", banners.get(currentImagePosition).url, -1);
                    }
                });

                initLiveData(result.lives);
                if (clubs != null && clubs.size() > 0) {
                    mRecyclerLocalClub.setVisibility(View.VISIBLE);
                    rl_club_list.setVisibility(View.VISIBLE);
                    mLocalClubAdapter.setData(clubs);
                    mLocalClubAdapter.notifyDataSetChanged();
                } else {
                    mRecyclerLocalClub.setVisibility(View.GONE);
                    rl_club_list.setVisibility(View.GONE);
                }


                if (places != null && places.size() > 0) {
                    ll_common_venue.removeAllViews();
                    badminton_list.setVisibility(View.VISIBLE);
                    for (int i = 0; i < places.size(); i++) {
                        initRecommendVenue(places.get(i), !(i == (places.size() - 1)));
                    }
                } else {
                    ll_common_venue.removeAllViews();
                    badminton_list.setVisibility(View.GONE);
                }

                if (mActivitys != null) {
                    if (mActivitys.size() > 0) {
                        ll_venue_activities.removeAllViews();
                        for (int i = 0; i < mActivitys.size(); i++) {
                            initVenueActivities(mActivitys.get(i), i);
                        }
                        mRecommentEmptyLayout.setVisibility(View.GONE);
                        ll_venue_activities.setVisibility(View.VISIBLE);
                    } else {
                        ll_venue_activities.removeAllViews();
                        mRecommentEmptyLayout.setVisibility(View.VISIBLE);
                        ll_venue_activities.setVisibility(View.GONE);
                    }
                } else {
                    ll_venue_activities.removeAllViews();
                    mRecommentEmptyLayout.setVisibility(View.VISIBLE);
                    ll_venue_activities.setVisibility(View.GONE);
                }

                // 课程推荐
                if (result.courses != null && result.courses.size() > 0) {
                    ll_home_course.setVisibility(View.VISIBLE);
                    mCourseAdapter.setNewData(result.courses);
                } else {
                    ll_home_course.setVisibility(View.GONE);
                }

                // 驻场培训机构
                if (result.trains != null && result.trains.size() > 0) {
                    ll_home_train.setVisibility(View.VISIBLE);
                    mTrainAdapter.setNewData(result.trains);
                } else {
                    ll_home_train.setVisibility(View.GONE);
                }

                scroll_parent.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        scroll_parent.scrollTo(0, 0);
                    }
                }, 500);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {

            }
        });
    }

    /**
     * 填充场馆活动list
     *
     * @param activity
     */
    private void initVenueActivities(final Api_RESOURCECENTER_PlaceActivityDto activity, final int position) {
        // View itemView = LayoutInflater.from(getContext()).inflate(R.layout.yumaoqiu_sport_item, null);
        if (null == getActivity()) return;
        View itemView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_venue_list_item, null);
        ImageView go_offen = itemView.findViewById(R.id.venue_list_item_ivOffen);
        TextView start_time = itemView.findViewById(R.id.venue_list_item_tvStartTime);
        TextView yumaoqiu_venue_name = itemView.findViewById(R.id.venue_list_item_tvVenueName);
        TextView yumaoqiu_venue_distance = itemView.findViewById(R.id.venue_list_item_tvVenueDistance);
        ImageView sport_sponsor = itemView.findViewById(R.id.venue_list_item_sdSponsorHead);
        TextView sponsor_name = itemView.findViewById(R.id.venue_list_item_tvSponsorName);
        TextView yumaoqiu_sport_info = itemView.findViewById(R.id.venue_list_item_tvSportContent);
        TextView people = itemView.findViewById(R.id.venue_list_item_tvSignedNum);
        TextView has_signed = itemView.findViewById(R.id.venue_list_item_tvSignedUp);
        TextView baoming = itemView.findViewById(R.id.venue_list_item_tvSignUp);
        LinearLayout court_gridView = itemView.findViewById(R.id.sport_members_layout);
        ImageView ivMore = itemView.findViewById(R.id.iv_more);

        if (activity.starLevel == 1) {
            go_offen.setVisibility(View.VISIBLE);
        } else {
            go_offen.setVisibility(View.GONE);
        }

        if (!hasSigned(activity.joinUsers)) {
            baoming.setVisibility(View.VISIBLE);
            has_signed.setVisibility(View.GONE);
        } else {
            baoming.setVisibility(View.GONE);
            has_signed.setVisibility(View.VISIBLE);
        }

        baoming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mActivitys.size() > 0) {
                    String url = SPUtils.getClubActivityDetail(getContext());
                    url = url.replace(":id", String.valueOf(activity.activityId));
                    //事件统计
                    Analysis.pushEvent(getActivity(), AnEvent.GYM_ACTIVITY, String.valueOf(activity.activityId));
                    NavUtils.startWebview(getActivity(), "场馆详情", url, 11000);
                }
            }
        });

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*NavUtils.startWebview((Activity) mActivity, getResources().getString(R.string.venue_sport),
                        SPUtils.getClubActivityDetail(mActivity), 10010);*/
                if (mActivitys.size() > 0) {
                    String url = SPUtils.getClubActivityDetail(getContext());
                    url = url.replace(":id", String.valueOf(activity.activityId));
                    //事件统计
                    Analysis.pushEvent(getActivity(), AnEvent.GYM_ACTIVITY, String.valueOf(activity.activityId));
                    NavUtils.startWebview(getActivity(), "场馆详情", url, 11000);
                }
            }
        });

        start_time.setText(TimeUtil.getStartTime(activity.startTime, "MM:HH"));
        people.setText(activity.joinUsers.size() + "/" + activity.totalUserCount);
        yumaoqiu_venue_name.setText(activity.placeName);
        double distance = activity.distance;
        if (distance < 1) {
            yumaoqiu_venue_distance.setText(Math.round(distance * 1000) + "m");
        } else {
            yumaoqiu_venue_distance.setText(new DecimalFormat("#########0.0")
                    .format(distance) + "km");
        }
//        yumaoqiu_venue_distance.setText(activity.distance + "km");
//        sport_sponsor.setImageURI(ContextHelper.getImageUrl() + activity.organiser.avatar);
        ImageLoadManager.loadCircleImage(ContextHelper.getImageUrl() + activity.organiser.avatar, R.mipmap.icon_default_avatar, sport_sponsor);
        sponsor_name.setText(activity.organiser.nickname);
        yumaoqiu_sport_info.setText(activity.description);
        initSportData(court_gridView, activity.joinUsers, ivMore);
        LinearLayout parent = (LinearLayout) ll_venue_activities.getParent();
        parent.setVisibility(View.VISIBLE);
        ll_venue_activities.addView(itemView);

    }

    /**
     * 填充推荐场馆list
     *
     * @param place
     * @param isLast
     */
    private void initRecommendVenue(final Api_RESOURCECENTER_RecommendPlaceDto place, boolean isLast) {
        try {

            if (null == getActivity()) return;
            View itemView = LayoutInflater.from(getActivity()).inflate(R.layout.recommend_venue_item, null);

            RoundImageView place_cover = (RoundImageView) itemView.findViewById(R.id.img_venue_propaganda_pic);

            ImageView goOften = (ImageView) itemView.findViewById(R.id.go_offen);
            TextView book_venue = (TextView) itemView.findViewById(R.id.book_venue);
            TextView tv_venue_name = (TextView) itemView.findViewById(R.id.tv_venue_name);
            TextView tv_venue_self = (TextView) itemView.findViewById(R.id.tv_venue_self);
            TextView tv_location = (TextView) itemView.findViewById(R.id.tv_location);
            LinearLayout llytPrice = itemView.findViewById(R.id.llyt_price);
            TextView tv_price = (TextView) itemView.findViewById(R.id.tv_price);


            ImageLoadManager.loadImage(place.cover, R.mipmap.icon_default_215_215, place_cover);
            if (place.starLevel == 1) {
                goOften.setVisibility(View.VISIBLE);
            } else {
                goOften.setVisibility(View.INVISIBLE);
            }
            if (place.businessMode == 1) {
                tv_venue_self.setVisibility(View.VISIBLE);
            } else {
                tv_venue_self.setVisibility(View.GONE);
            }

            tv_venue_name.setText(place.name);
            if (!TextUtils.isEmpty(place.price) && Float.valueOf(place.price) > 0) {
                llytPrice.setVisibility(View.VISIBLE);
                tv_price.setText("¥" + place.price);
            } else {
                llytPrice.setVisibility(View.GONE);
            }

            //距离处理
            double distance = Double.valueOf(place.distance.replaceAll("km", ""));
            boolean flag = false;
            String distanceText = "";
            if (distance > 100) {
                distanceText = ">100公里";
                flag = true;
            } else {
                distanceText = distance + "公里";
            }
            //距区描述还是距定位描述
            String locationDetail = "";
            String ju = " ";
            if (!flag) ju = ju + "距您";
            if (PermissionUtils.checkLocationPermission(getActivity())) {
                //是否定位定位服务和权限
                if (LocationManager.getInstance().getStorage().isSameWithGPRS()) {
                    //是否定位和选择地区一致（这里把未定位也看作是该情况）
                    locationDetail = place.poiInfo.detail + ju;
                } else {
                    if (!TextUtils.isEmpty(place.address)) {
                        locationDetail = place.address + ju;
                    } else {
                        locationDetail = place.poiInfo.detail + ju;
                    }
                }
                tv_location.setText(locationDetail + " " + distanceText);
            } else {
                locationDetail = place.poiInfo.detail;
                tv_location.setText(locationDetail);
            }

            ll_common_venue.addView(itemView);
            book_venue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gotoVenueDetail(place);
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gotoVenueDetail(place);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void gotoVenueDetail(Api_RESOURCECENTER_RecommendPlaceDto place) {
        if (LocationManager.getInstance().getStorage().getLast_isPublic() == 1) {
            // https://h5test.yingheying.com/venue/?#/detail/:id
            NavUtils.startWebview((Activity) mActivity, "",
                    SPUtils.getFaskBooking(mActivity).replace(":placeId", String.valueOf(place.orgId)), 10021);
        } else {
            NavUtils.startWebview((Activity) mActivity, "",
                    SPUtils.getOutVenueDetail(mActivity).replace(":id", String.valueOf(place.placeId)), 10021);
        }
    }

    /**
     * 拿到精彩视频的数据设置给精彩视频list
     *
     * @param lives
     */
    private void initLiveData(List<Api_RESOURCECENTER_LiveRecordDto> lives) {
        ArrayList<LiveVideoInfo> list = new ArrayList<>();

        if (lives == null) {
            ll_home_live_video.setVisibility(View.GONE);
            return;
        }

        if (lives.size() == 0) {
            ll_home_live_video.setVisibility(View.GONE);
            return;
        }

        ll_home_live_video.setVisibility(View.VISIBLE);

        for (Api_RESOURCECENTER_LiveRecordDto live : lives) {
            long liveId = live.liveId;//直播ID
            long roomId = live.roomId;//直播间ID
            long liveCategoryCode = live.liveCategoryCode;//直播类别code
            String liveCategoryName = live.liveCategoryName;//直播类别name
            String liveTitle = live.liveTitle;//直播标题
            String liveCover = live.liveCover;//直播封面
            String liveStatus = live.liveStatus;//直播状态:直播(START_LIVE)、结束(END_LIVE)、回放(REPLAY_LIVE)、无效(INVALID_LIVE)
            String locationCityCode = live.locationCityCode;//城市code
            String locationCityName = live.liveCategoryName;//城市名称
            long startDate = live.startDate;//开始时间
            long endDate = live.endDate;//结束时间
            int onlineCount = live.onlineCount;//在线人数
            int viewCount = live.viewCount;//观看次数
            List<String> replayUrls = live.replayUrls;//回放地址
            String pushStreamUrl = live.pushStreamUrl;//推流地址
            String pullStreamUrl = live.pullStreamUrl;//拉流地址
            List<String> configContent = live.configContent;//直播配置文本List
            String status = live.status;//直播记录状态:正常(NORMAL_LIVE) 删除（DELETE_LIVE）下架 (OFF_SHELVE_LIVE)
            int liveScreenType = live.liveScreenType;//0:横向视频 1：竖向视频

            LiveVideoInfo i = new LiveVideoInfo();
            i.setLiveId(liveId);
            i.setRoomId(roomId);
            i.setLiveCategoryCode(liveCategoryCode);
            i.setLiveCategoryName(liveCategoryName);
            i.setStartDate(startDate);
            i.setEndDate(endDate);
            i.setLiveStatus(liveStatus);
            i.setLiveTitle(liveTitle);
            i.setLiveCover(liveCover);
            i.setOnlineCount(onlineCount);
            i.setViewCount(viewCount);
            i.setPullStreamUrl(pullStreamUrl);
            i.setPushStreamUrl(pushStreamUrl);
            i.setReplayUrls(replayUrls);
            i.setStatus(status);
            i.setLocationCityCode(locationCityCode);
            i.setLocationCityName(locationCityName);
            i.setConfigContent(configContent);
            i.setLiveScreenType(liveScreenType);
            list.add(i);


            mLiveVideoAdapter.setList(list);
            mLiveVideoAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 获取城市列表
     *
     * @param containDistrictFlg
     */
    private void getCityList(boolean containDistrictFlg) {
        String jsonCityList = CityCache.getOutPlaceCityListOrigin(mActivity);
        mCityList = JSONUtils.convertToArrayList(jsonCityList, GetOutPlaceCityListResp.OutPlaceCity.class);
        if (mCityList != null && mCityList.size() > 0) {
            hasCitys = true;
//            mCityList = new ArrayList<>(result.cityList);
//            cityListReLoad();

            // GPRS的位置信息与服务器列表的数据进行匹配 获取当前定位到的城市信息
            boolean match = false;
            for (int i = 0; i < mCityList.size(); i++) {
                GetOutPlaceCityListResp.OutPlaceCity city = mCityList.get(i);
//                List<Api_PLACE_District> districtList = city.districtList;
                if ((city.name).equals(LocationManager.getInstance().getStorage().getLast_cityName())) {
                    // 默认只显示到市，区域是全部
                    match = true;
                    mPopupBtnCity.setText(city.name);
//                    Api_PLACE_District dist = districtList.get(0);
                    //  匹配成功后更新一下这个数据，因为sdk拿到定位的时候是没有这个数值的ispublick
                    LocationManager.getInstance().getStorage().setGPRSLocation(city.name, city.cityCode, "全部", "-1",
                            Double.valueOf(LocationManager.getInstance().getStorage().getLast_lat()),
                            Double.valueOf(LocationManager.getInstance().getStorage().getLast_lng()), city.isPublic);
//                    LocationManager.getInstance().getStorage().setGprs_isPublic(city.isPublic);
//                    mSwitchCityAdapter.setPressPostion(i);
//                    mSwitchDistrictAdapter.setPressPostion(0);
                }
            }
            if (!match) EventBus.getDefault().post(new EvBusLocation(4001));
            refreshData(mRootView);
        }
//        else {
//            mPopupBtnCity.setText(LocationManager.getInstance().getStorage().getLast_cityName());
//            refreshData(mRootView);
//        }

//        NetManager.getInstance(mActivity).doGetCityInfo(containDistrictFlg, new OnResponseListener<Api_PLACE_CityListResult>() {
//            @Override
//            public void onComplete(boolean isOK, Api_PLACE_CityListResult result, int errorCode, String errorMsg) {
//                if (result != null && result.cityList != null && result.cityList.size() > 0) {
//                    mCityList = new ArrayList<>(result.cityList);
//                    cityListReLoad();
//                    boolean match = false;
//                    for (int i = 0; i < mCityList.size(); i++) {
//                        Api_PLACE_City city = mCityList.get(i);
//                        List<Api_PLACE_District> districtList = city.districtList;
//                        if ((city.name).equals(LocationManager.getInstance().getStorage().getLast_cityName())) {
//                            // 默认只显示到市，区域是全部
//                            match = true;
//                            mPopupBtnCity.setText(city.name);
//                            Api_PLACE_District dist = districtList.get(0);
//                            LocationManager.getInstance().getStorage().setManualLocation(city.name, city.cityCode, dist.name, dist.districtCode, dist.lat, dist.lng);
//                            mSwitchCityAdapter.setPressPostion(i);
//                            mSwitchDistrictAdapter.setPressPostion(0);
//                        }
//                    }
//                    if (!match) EventBus.getDefault().post(new EvBusLocation(4001));
//                    refreshData(mView);
//                }
//
//            }
//
//            @Override
//            public void onInternError(int errorCode, String errorMessage) {
//                mPopupBtnCity.setText(LocationManager.getInstance().getStorage().getLast_cityName());
//                refreshData(mView);
//            }
//        });
    }

    public boolean hasSigned(List<Api_RESOURCECENTER_JoinActivityUserDto> signedIds) {
        boolean isSigned = false;
        if (signedIds != null && signedIds.size() != 0) {
            for (Api_RESOURCECENTER_JoinActivityUserDto dao : signedIds) {
                if (dao.id == userService.getLoginUserId()) {
                    isSigned = true;
                    break;
                }
            }
        }
        return isSigned;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ValueConstants.RESULT_LIVE_DATA) {
            getDynamicInfos(true);
        }
    }

    private String getWebViewUrl(String booking) {
        if (TextUtils.isEmpty(booking))
            return "";
        String cityCode = "";
        cityCode = LocationManager.getInstance().getStorage().getLast_cityCode();
        String urlBtnBadminton = booking.replaceAll(":cityCode", cityCode);
        return urlBtnBadminton;
    }

    @Override
    public void handleMessage(Message msg) {

    }

    /*****************************************************************        OnEvent         *************************************************************/

    //praise State
    public void onEvent(EvBusCircleChangePraise state) {
        for (UgcInfoResult result : ugcList) {
            if (result.id == state.id) {
                result.isSupport = state.isSupport;
                if (ValueConstants.TYPE_AVAILABLE.equals(state.isSupport)) {//点赞数加1
                    result.supportNum += 1;
                }
                if (ValueConstants.TYPE_DELETED.equals(state.isSupport)) {//点赞数减1
                    result.supportNum -= 1;
                }
            }
        }
        ugc_adapter.notifyDataSetChanged();

    }

    //follow State
    public void onEvent(EvBusCircleChangeFollow state) {
        for (UgcInfoResult result : ugcList) {
            if (result.userInfo.userId == state.userId) {
                result.type = state.type;
            }
        }
        ugc_adapter.notifyDataSetChanged();

    }

    //删除动态
    public void onEvent(EvBusCircleDelete state) {
        for (int i = 0; i < ugcList.size(); i++) {
            if (ugcList.get(i).id == state.id) {
                ugcList.remove(i);
                ugc_adapter.notifyItemRemoved(i);
                break;
            }
        }
    }

    //屏蔽
    public void onEvent(EvBusBlack black) {
        if (black.type.equals(ShieldType.USER_SUBJECT)) {//屏蔽用户
            Iterator iterator = ugcList.iterator();
            while (iterator.hasNext()) {
                UgcInfoResult result = (UgcInfoResult) iterator.next();
                if (result.userInfo.userId == black.id) {
                    iterator.remove();
                }
            }
            ugc_adapter.notifyDataSetChanged();
        }

        if (black.type.equals(ShieldType.SUBJECT)) {//屏蔽动态
            for (int i = 0; i < ugcList.size(); i++) {
                if (ugcList.get(i).id == black.id) {
                    ugcList.remove(i);
                    ugc_adapter.notifyItemRemoved(i);
                    break;
                }
            }
        }
    }

    /**
     * 运动城市选择改变后同步
     *
     * @param event
     */
    public void onEvent(EvBusLocationChange event) {
        if (event.isFromWeb) return;
        if (!TextUtils.isEmpty(LocationManager.getInstance().getStorage().getManual_cityName())) {
            mPopupBtnCity.setText(LocationManager.getInstance().getStorage().getManual_cityName());

//            if (LocationManager.getInstance().getStorage().getManual_discName().contains("全")) {
//                mPopupBtnCity.setText(LocationManager.getInstance().getStorage().getManual_cityName());
//            } else {
//                mPopupBtnCity.setText(LocationManager.getInstance().getStorage().getManual_cityName() + " " + LocationManager.getInstance().getStorage().getManual_discName());
//            }
//            //更新下拉列表Ui
//            mCityPosition = event.cityPosotion;
//            mDistrictPosition = event.districPosotion;
//            mSwitchCityAdapter.setPressPostion(event.cityPosotion);
//            Api_PLACE_City respCity = mCityList.get(mCityPosition);
//            mDistrictList.clear();
//            mDistrictList.addAll(respCity.districtList);
//            mSwitchDistrictAdapter.notifyDataSetChanged();
//            mSwitchDistrictAdapter.setPressPostion(event.districPosotion);
            getCampaignData();
            getDynamicInfos(true);
            getHomeCourtData();
        }
    }


    public void onEvent(EvBusWhenSelectFragment event) {
        if (event.index == 0) {
            scroll_parent.scrollTo(0, 0);
            refreshLayout.autoRefresh();
            initSelectSport();
            initDateSelectWindow();
        }
    }

    public void onEvent(EvBusSportHabit event) {
        getCampaignData();
    }

    public void onEvent(EvBusMessageCount evBusMessageCount) {
        int count = evBusMessageCount.getCount();
        updateIMMessageCount(count);
    }

    public void onEvent(EvBusCityGet evBusCityGet) {
        if (!hasCitys){
            getCityList(true);
        }
    }

    public void onEvent(EvBusLocation evBusLocation) {
        //type==1001代表定位成功    type==4001 代表定位失败（包含KEY错误或网络问题导致定位的失败）
        TCEventHelper.onEvent(getActivity(), AnalyDataValue.TC_ID_LOCATION_RECORD_HOME);
        switch (evBusLocation.getLocationType()) {
            case 4001: {
                mPopupBtnCity.setText(LocationManager.getInstance().getStorage().getLast_cityName());
//                if (LocationManager.getInstance().getStorage().getLast_discName().contains("全")) {
//                } else {
//                    mPopupBtnCity.setText(LocationManager.getInstance().getStorage().getLast_cityName() + " " + LocationManager.getInstance().getStorage().getLast_discName());
//                }
//                synLocationSelectedState(LocationManager.getInstance().getStorage().getLast_cityName(), LocationManager.getInstance().getStorage().getLast_discName());

                break;
            }
            case 1001: {
                if (LocationManager.getInstance().getStorage().isGPRSChange()) {
                    if (mDialog == null) {
                        mDialog = DialogUtil.showMessageDialog(getActivity(), getString(R.string.notice_title_change_city),
                                String.format(getString(R.string.notice_conent_change_city), LocationManager.getInstance().getStorage().getGprs_cityName()),
                                getString(R.string.btn_text_change),
                                getString(R.string.btn_text_not_change), v -> {
                                    matchGpsCityName();
                                    mDialog.dismiss();
                                }, v -> mDialog.dismiss());
                    }
                    mDialog.show();
                    return;
                }
                break;
            }
        }
    }

}
