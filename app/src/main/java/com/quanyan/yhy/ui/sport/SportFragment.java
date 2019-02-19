package com.quanyan.yhy.ui.sport;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.harwkin.nb.camera.ImageUtils;
import com.newyhy.activity.HomeMainTabActivity;
import com.newyhy.utils.DisplayUtils;
import com.newyhy.views.CenterBoothView;
import com.newyhy.views.ObservableScrollView;
import com.newyhy.views.PopupButton;
import com.newyhy.views.SpacesItemDecoration;
import com.quanyan.base.BaseFragment;
import com.quanyan.pedometer.newpedometer.PedometerUtil;
import com.quanyan.pedometer.newpedometer.StepService;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.ItemType;
import com.quanyan.yhy.eventbus.EvBusMessageCount;
import com.quanyan.yhy.eventbus.EvBusWhenSelectFragment;
import com.quanyan.yhy.libanalysis.AnArgs;
import com.quanyan.yhy.libanalysis.AnEvent;
import com.quanyan.yhy.libanalysis.Analysis;
import com.quanyan.yhy.net.NetManager;
import com.quanyan.yhy.net.lsn.OnResponseListener;
import com.quanyan.yhy.ui.adapter.MallAdapter;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.common.calendar.ScreenUtil;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.integralmall.integralmallcontroller.IntegralmallController;
import com.quanyan.yhy.ui.master.activity.MasterAdviceListActivity;
import com.quanyan.yhy.ui.sport.model.HealthInfo;
import com.quanyan.yhy.ui.sport.model.LiveVideoInfo;
import com.quanyan.yhy.ui.sport.model.PedometerInfo;
import com.quanyan.yhy.ui.sport.model.VenueInfo;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.smart.sdk.api.resp.Api_RESOURCECENTER_BannerDto;
import com.smart.sdk.api.resp.Api_RESOURCECENTER_Booth;
import com.smart.sdk.api.resp.Api_RESOURCECENTER_ImUserDto;
import com.smart.sdk.api.resp.Api_RESOURCECENTER_IntegralItemDto;
import com.smart.sdk.api.resp.Api_RESOURCECENTER_JoinActivityUserDto;
import com.smart.sdk.api.resp.Api_RESOURCECENTER_LiveRecordDto;
import com.smart.sdk.api.resp.Api_RESOURCECENTER_PlaceActivityDto;
import com.smart.sdk.api.resp.Api_RESOURCECENTER_SportInfoResult;
import com.smart.sdk.api.resp.Api_RESOURCECENTER_UserSimpleDto;
import com.yhy.cityselect.eventbus.EvBusLocationChange;
import com.yhy.common.beans.net.model.RCShowcase;
import com.yhy.common.beans.net.model.common.Booth;
import com.yhy.common.beans.net.model.item.CodeQueryDTO;
import com.yhy.common.beans.net.model.trip.ShortItem;
import com.yhy.common.beans.net.model.trip.ShortItemsResult;
import com.yhy.common.constants.IntentConstants;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.CommonUtil;
import com.yhy.common.utils.SPUtils;
import com.yhy.imageloader.ImageLoadManager;
import com.yhy.location.EvBusLocation;
import com.yhy.location.LocationManager;
import com.yhy.router.YhyRouter;
import com.yhy.service.IUserService;
import com.yixia.camera.util.Log;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 运动页面
 * <p>
 * Created by shenwenjie on 2018/1/27.
 */

public class SportFragment extends BaseFragment implements View.OnClickListener {
    //refresh
    private SmartRefreshLayout refreshLayout;
    private ObservableScrollView scroll_parent;
    // 顶部城市地区
    private PopupButton mPopupBtnCity;
    private PopupButton mFloatPopup;
//    private ListView lv_city, lv_district;
//    private ArrayList<Api_PLACE_City> mCityList = new ArrayList<>();
//    private ArrayList<Api_PLACE_District> mDistrictList = new ArrayList<>();
//    private PopNormalAdapter_OrderVenue mSwitchCityAdapter;
//    private PopNormalAdapter_OrderVenue mSwitchDistrictAdapter;
//    private int mCityPosition = 0;
//    private int mDistrictPosition = 0;

    // 消息
    private TextView tvNewMsgCount;
    private TextView tvFloatMsgCount;
    // 可配置展位
    private LinearLayout llytCenterBoothOne;
    private LinearLayout llytCenterBoothTwo;
    // 步步吸金  邀请好友
    private RelativeLayout llPedometer;
    private LinearLayout llStepCount;
    private ImageView ivBanner;
    private ImageView ivLeftBanner;
    private RelativeLayout rlRightBanner;
    private TextView tvPedometer;
    // 直播视屏
    private LinearLayout llytLive;
    private RecyclerView mLiveRecyclerView;
    private LiveVideoAdapter mLiveVideoAdapter;
    // 健康咨询
    private LinearLayout llytCousult;
    private RecyclerView mHealthRecyclerView;
    private HealthAdapter mHealthAdapter;
    // 场馆活动
    private LinearLayout llytVenue;
    private LinearLayout llVenue;
    // 积分商城
    private LinearLayout llytMall;
    private RecyclerView grdMall;
    private MallAdapter materialAdapter;
    IntegralmallController integralmallController;
    private int page = 1;
    boolean hasNext;

    private View mView;
    //    private View view_city;
    private View mFloatToolView;
    private View mScrollBgView;
    private View mScrollActView;
    ImageView btnChat, float_btnChat;
    private ImageView ivItem1, ivItem2, ivItem3, ivItem4;
    private TextView tvItem1, tvItem2, tvItem3, tvItem4;
    private View vItem1, vItem2, vItem3, vItem4;
    private ImageView ivSportAct1, ivSportAct2;
    private RCShowcase rcShowcaseAct1, rcShowcaseAct2;

    private final int PAGE_SIZE = 20;
    private final int MSG_GET_LIVE_LIST_SUCCESS = 1;
    private final int MSG_GET_LIVE_LIST_FAIL = -1;
    private final int MSG_GET_HEALTH_LIST_SUCCESS = 2;
    private final int MSG_GET_HEALTH_LIST_FAIL = -2;
    private final int MSG_GET_VENUE_LIST_SUCCESS = 3;
    private final int MSG_GET_VENUE_LIST_FAIL = -3;
    private final int MSG_GET_MALL_LIST_SUCCESS = 4;
    private final int MSG_GET_MALL_LIST_FAIL = -4;
    private final int MSG_GET_PEDOMETER_SUCCESS = 5;
    private final int MSG_GET_PEDOMETER_FAIL = -5;

    private static final String SPORT_FOOTBALL = "足球";
    private static final String SPORT_BASKETBALL = "篮球";
    private static final String SPORT_BADMINTON = "羽毛球";
    private static final String SPORT_TENNIS = "网球";
    private SportHandler handler = new SportHandler();
    private ImageView ivBackground;

    @Autowired
    IUserService userService;

    @SuppressLint("HandlerLeak")
    public class SportHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_GET_LIVE_LIST_SUCCESS: {
                    // 直播列表
                    ArrayList<LiveVideoInfo> list = (ArrayList<LiveVideoInfo>) msg.obj;
                    if (list != null && !list.isEmpty()) {
                        llytLive.setVisibility(View.VISIBLE);
                        mLiveVideoAdapter.setList(list);
                        mLiveVideoAdapter.notifyDataSetChanged();
                    } else {
                        llytLive.setVisibility(View.GONE);
                    }
                }
                break;
                case MSG_GET_LIVE_LIST_FAIL:
                    break;
                case MSG_GET_HEALTH_LIST_SUCCESS: {
                    // 健康咨询
                    ArrayList<HealthInfo> list = (ArrayList<HealthInfo>) msg.obj;
                    if (list != null && !list.isEmpty()) {
                        llytCousult.setVisibility(View.VISIBLE);
                        mHealthAdapter.setList(list);
                        mHealthAdapter.notifyDataSetChanged();
                    } else {
                        llytCousult.setVisibility(View.GONE);
                    }
                }
                break;
                case MSG_GET_HEALTH_LIST_FAIL:
                    break;
                case MSG_GET_VENUE_LIST_SUCCESS: {
                    // 场馆活动
                    ArrayList<VenueInfo> list = (ArrayList<VenueInfo>) msg.obj;
                    if (list != null && !list.isEmpty()) {
                        llytVenue.setVisibility(View.VISIBLE);
                        llVenue.removeAllViews();
                        for (int i = 0; i < list.size(); i++) {
                            initVenueView(list.get(i), !(i == 0));
                        }
                    } else {
                        llytVenue.setVisibility(View.GONE);
                    }

                }
                break;
                case MSG_GET_VENUE_LIST_FAIL:
                    break;
                case MSG_GET_MALL_LIST_SUCCESS: {
//                    ArrayList<MallInfo> list = (ArrayList<MallInfo>) msg.obj;
//                    if (list != null && !list.isEmpty()) {
//                        mMallAdapter.setList(list);
//                        setGridViewHeight(grdMall);
//                        mMallAdapter.notifyDataSetChanged();
//                    }
                }
                break;
                case MSG_GET_MALL_LIST_FAIL:
                    break;
                case MSG_GET_PEDOMETER_SUCCESS: {
                    // 步步吸金
                    PedometerInfo pedometerInfo = (PedometerInfo) msg.obj;
                    if (pedometerInfo != null) {
                        int stepNum = pedometerInfo.getStepNum();// 运动页运动步数
                        long bannerId = pedometerInfo.getBannerId();// banner id
                        String bannerCover = pedometerInfo.getBannerCover();// banner封面
                        String bannerType = pedometerInfo.getBannerType();// 展位类别
                        String url = pedometerInfo.getUrl();// banner URL
                        String bannerLeftCover = pedometerInfo.getBannerLeftCover();
                        if (bannerCover != null && !bannerCover.isEmpty()) {
                            bannerCover = CommonUtil.getImageFullUrl(bannerCover);
                            ImageLoadManager.loadImage(bannerCover, ivBanner, 3);
                        }

                        if (bannerLeftCover != null && !bannerLeftCover.isEmpty()) {
                            bannerLeftCover = CommonUtil.getImageFullUrl(bannerLeftCover);
                            ImageLoadManager.loadImage(bannerLeftCover, ivLeftBanner, 3);
                        }

                        if (url != null && !url.isEmpty()) {
                            rlRightBanner.setTag(url);
                        }

                    }
                }
                break;
                case ValueConstants.MSG_INTEGRALMALL_LIST_OK:
                    //商品列表
                    if (msg.obj != null) {
                        ShortItemsResult shortItemsResult = (ShortItemsResult) msg.obj;
                        if (shortItemsResult.shortItemList != null && shortItemsResult.shortItemList.size() > 0) {

                            hasNext = shortItemsResult.hasNext;
                            if (page == 1) {
                                llytMall.setVisibility(View.VISIBLE);
//                                shortItemsResult.shortItemList.get(0).maxPrice = 20000;
//                                shortItemsResult.shortItemList.get(0).payInfo.minPoint =  1;

                                materialAdapter.setNewData(shortItemsResult.shortItemList);
                            } else {
                                materialAdapter.addData(shortItemsResult.shortItemList);
                            }

                        } else {
                            if (page == 1) {
                                llytMall.setVisibility(View.GONE);
                            }
                        }
                    } else {
                        hasNext = false;
                        if (page == 1) {
                            llytMall.setVisibility(View.GONE);
                        }
                    }
                    hideLoadingView();
                    break;
                case ValueConstants.MSG_INTEGRALMALL_LIST_KO:
                    llytMall.setVisibility(View.GONE);
                    hideLoadingView();

                    break;
                case MSG_GET_PEDOMETER_FAIL:
                    break;
                default:
                    break;

            }
        }
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        integralmallController = new IntegralmallController(getActivity(), handler);
        mView = view;
        //initRefresh
        refreshLayout = view.findViewById(R.id.refreshLayout);
        initRefresh();
        scroll_parent = view.findViewById(R.id.scroll_parent);

        ImageView ivLiveList = (ImageView) mView.findViewById(R.id.fragment_sport_ivLiveList);
        ivLiveList.setOnClickListener(this);

        ImageView ivConsultList = (ImageView) mView.findViewById(R.id.fragment_sport_ivConsultList);
        ivConsultList.setOnClickListener(this);
        //计步器
        llPedometer = (RelativeLayout) mView.findViewById(R.id.fragment_sport_llPedometer);
        llStepCount = (LinearLayout) mView.findViewById(R.id.ll_step_count);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) llPedometer.getLayoutParams();
        lp.height = (ScreenUtil.getScreenWidth(getActivity()) - ScreenUtil.dip2px(getActivity(), 30)) / 2 * 60 / 111;
        llPedometer.setLayoutParams(lp);

        RelativeLayout.LayoutParams lpstep = (RelativeLayout.LayoutParams) llStepCount.getLayoutParams();
        lpstep.bottomMargin = lp.height * 26 / 90;
        llStepCount.setLayoutParams(lpstep);

        rlRightBanner = (RelativeLayout) mView.findViewById(R.id.rl_rightbanner);
        ivBanner = (ImageView) mView.findViewById(R.id.fragment_sport_ivBanner);
        ivLeftBanner = (ImageView) mView.findViewById(R.id.fragment_sport_ivLeftBanner);
        tvPedometer = (TextView) mView.findViewById(R.id.fragment_sport_tvPedometer);
        mView.findViewById(R.id.rl_leftbanner).setOnClickListener(this);
        rlRightBanner.setOnClickListener(this);

        if (null != StepService.getInstance()) {
            long steps = StepService.getInstance().getSteps();
            tvPedometer.setText(String.valueOf(steps));
        }

        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "Impact.ttf");
        tvPedometer.setTypeface(typeface);

//        mPopupBtnCity = mView.findViewById(R.id.location);
//        mFloatPopup = mView.findViewById(R.id.float_location);
//        mPopupBtnCity.getPaint().setFakeBoldText(true);
//        mFloatPopup.getPaint().setFakeBoldText(true);
//        View view_city = getActivity().getLayoutInflater().inflate(R.layout.popup_venue_two, null);
//        lv_city = (ListView) view_city.findViewById(R.id.two_parent_lv);
//        lv_district = (ListView) view_city.findViewById(R.id.two_child_lv);

        //检查位置权限
//        if (!PermissionUtils.checkLocationPermission(getContext())) {
//            view_city.findViewById(R.id.rl_no_location).setVisibility(View.VISIBLE);
//            view_city.findViewById(R.id.rl_get_location).setVisibility(View.GONE);
//            view_city.findViewById(R.id.tv_go_setting).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mPopupBtnCity.hidePopup();
//                    mFloatPopup.hidePopup();
//                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                    startActivity(intent);
//                }
//            });
//        } else {
//            view_city.findViewById(R.id.rl_get_location).setVisibility(View.VISIBLE);
//            view_city.findViewById(R.id.rl_no_location).setVisibility(View.GONE);
//            TextView iv_location = view_city.findViewById(R.id.iv_location);
//            iv_location.setText(LocationManager.getInstance().getStorage().getGprs_cityName());
//        }

        initCityList(view);
//        getCityList(true);
        if (!TextUtils.isEmpty(LocationManager.getInstance().getStorage().getManual_cityName())) {
            mPopupBtnCity.setText(LocationManager.getInstance().getStorage().getManual_cityName());
        }
        // 消息数量和按钮
        tvNewMsgCount = (TextView) mView.findViewById(R.id.personal_message_num);
        tvFloatMsgCount = mView.findViewById(R.id.float_court_message_num);
        updateIMMessageCount(HomeMainTabActivity.imUnreadCount);
        btnChat = (ImageView) mView.findViewById(R.id.fragment_personal_btnChat);
        float_btnChat = (ImageView) mView.findViewById(R.id.float_message_layout);
        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLoggedIn()) {
                    NavUtils.gotoMsgCenter(getContext());
                }
            }
        });
        float_btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLoggedIn()) {
                    NavUtils.gotoMsgCenter(getContext());
                }
            }
        });

        vItem1 = view.findViewById(R.id.fragment_sport_item1);
        vItem1.setOnClickListener(this);
        vItem2 = view.findViewById(R.id.fragment_sport_item2);
        vItem2.setOnClickListener(this);
        vItem3 = view.findViewById(R.id.fragment_sport_item3);
        vItem3.setOnClickListener(this);
        vItem4 = view.findViewById(R.id.fragment_sport_item4);
        vItem4.setOnClickListener(this);

        mView.findViewById(R.id.sport_act_1).setOnClickListener(this);
        mView.findViewById(R.id.sport_act_2).setOnClickListener(this);

        // 可配置展位板块
        llytCenterBoothOne = mView.findViewById(R.id.llyt_center_booth_one);
        llytCenterBoothTwo = mView.findViewById(R.id.llyt_center_booth_two);

        //积分商城
        ImageView ivIntegralMallMore = (ImageView) mView.findViewById(R.id.fragment_sport_ivIntegralMallMore);
        ivIntegralMallMore.setOnClickListener(this);

        //场馆
        ImageView ivVenueMore = (ImageView) mView.findViewById(R.id.fragment_sport_ivVenueMore);
        ivVenueMore.setOnClickListener(this);

        //快速咨询，俱乐部，积分商城点击
//        mView.findViewById(R.id.fragment_sport_ivQuickConsultation).setOnClickListener(this);
//        mView.findViewById(R.id.fragment_sport_ivClub).setOnClickListener(this);
//        mView.findViewById(R.id.fragment_sport_ivIntegralMall).setOnClickListener(this);

        mFloatToolView = view.findViewById(R.id.float_tool_bar);
        mScrollBgView = view.findViewById(R.id.scroll_layout);
        mScrollActView = view.findViewById(R.id.scroll_act_layout);
        ivItem1 = view.findViewById(R.id.sport_football);
        ivItem2 = view.findViewById(R.id.sport_basketball);
        ivItem3 = view.findViewById(R.id.sport_badminton);
        ivItem4 = view.findViewById(R.id.sport_tennis);
        tvItem1 = view.findViewById(R.id.sport_item1);
        tvItem2 = view.findViewById(R.id.sport_item2);
        tvItem3 = view.findViewById(R.id.sport_item3);
        tvItem4 = view.findViewById(R.id.sport_item4);
        ivSportAct1 = view.findViewById(R.id.sport_act_1);
        ivSportAct2 = view.findViewById(R.id.sport_act_2);
        ivBackground = view.findViewById(R.id.scroll_image_bg);

        initLiveView(mView);
        initHealthView(mView);
        initVenueView(mView);
        initMallView(mView);

        getSportInfo();

        scroll_parent.setOnScrollChangedCallback(new ObservableScrollView.OnScrollChangedCallback() {
            @Override
            public void onScroll(int dx, int dy) {
                float alpha = (float) Math.abs(dy) / 360;
                mFloatToolView.setVisibility(View.VISIBLE);
                if (alpha <= 1) {
                    mFloatToolView.setAlpha(alpha <= 0.1 ? 0 : alpha);
                }
            }
        });
    }

    private void initCityList(View view) {
        mPopupBtnCity = mView.findViewById(R.id.location);
        mFloatPopup = mView.findViewById(R.id.float_location);
        mPopupBtnCity.getPaint().setFakeBoldText(true);
        mFloatPopup.getPaint().setFakeBoldText(true);

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

                YhyRouter.getInstance().startCitySelectActivity(getActivity(), false, AnArgs.Instance().build(Analysis.UID, userService.getLoginUserId() > 0 ? userService.getLoginUserId() + "" : "0")
                        .build(Analysis.PAGENAME, "运动")
                        .getMap());

            }

            @Override
            public void onHide() {
                mPopupBtnCity.setSelected(false);
            }
        });

        mFloatPopup.setPopupView(null, 360);
        mFloatPopup.setListener(new PopupButton.PopupButtonListener() {
            @Override
            public void onShow() {
//                if (view_city != null) {
//                    initLocationDialogTittle(view_city);
//                }
//                mFloatPopup.setSelected(true);
                YhyRouter.getInstance().startCitySelectActivity(getActivity(), false, AnArgs.Instance().build(Analysis.UID, userService.getLoginUserId() > 0 ? userService.getLoginUserId() + "" : "0")
                        .build(Analysis.PAGENAME, "运动")
                        .getMap());

            }

            @Override
            public void onHide() {
                mFloatPopup.setSelected(false);
            }
        });

        mPopupBtnCity.setText(LocationManager.getInstance().getStorage().getLast_cityName());
        mFloatPopup.setText(LocationManager.getInstance().getStorage().getLast_cityName());
//        view_city.findViewById(R.id.rl_get_location).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                matchGpsCityName();
//                mPopupBtnCity.hidePopup();
//                mFloatPopup.hidePopup();
//            }
//        });
    }

//    private void matchGpsCityName() {
//        int cityPosition = 0;
//        int districtPosition = 0;
//        boolean cityMatch = false;
//
//        for (int i = 0; i < mCityList.size(); i++) {
//            if (LocationManager.getInstance().getStorage().getGprs_cityName().equals(mCityList.get(i).name)) {
//                cityPosition = i;
//                cityMatch = true;
//            }
//        }
//        if (cityMatch) {
//            LocationManager.getInstance().getStorage().setManualLocation(mCityList.get(cityPosition).name, mCityList.get(cityPosition).cityCode,
//                    mCityList.get(cityPosition).districtList.get(districtPosition).name,
//                    mCityList.get(cityPosition).districtList.get(districtPosition).districtCode,
//                    mCityList.get(cityPosition).lat, mCityList.get(cityPosition).lng);
//
//            EventBus.getDefault().post(new EvBusLocationChange(cityPosition, districtPosition));
//            mSwitchCityAdapter.setPressPostion(cityPosition);
//            mSwitchDistrictAdapter.setPressPostion(districtPosition);
//            //刷新一遍主场的数据
//            getSportInfo();
//            mPopupBtnCity.setText(LocationManager.getInstance().getStorage().getManual_cityName());
//            mFloatPopup.setText(LocationManager.getInstance().getStorage().getManual_cityName());
//            Analysis.cityAnalysis(getActivity(), userService.getLoginUserId(),mCityList.get(cityPosition).name + " " + mCityList.get(cityPosition).districtList.get(districtPosition).name,"运动");
//        }
//    }

    /**
     * 获取位置信息,更新dialog的tittle
     */
//    private void initLocationDialogTittle(View view_city) {
//        if (!PermissionUtils.checkLocationPermission(getActivity())) {
//            view_city.findViewById(R.id.rl_no_location).setVisibility(View.VISIBLE);
//            view_city.findViewById(R.id.rl_get_location).setVisibility(View.GONE);
//            view_city.findViewById(R.id.tv_go_setting).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mPopupBtnCity.hidePopup();
//                    mFloatPopup.hidePopup();
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
    private void getMallList() {
        showLoadingView(null);

        CodeQueryDTO codeQueryDTO = new CodeQueryDTO();
        codeQueryDTO.pageNo = page;
        codeQueryDTO.pageSize = PAGE_SIZE;
        integralmallController.doGetIntegralmallListByPage(getActivity(), codeQueryDTO);
    }

    /**
     * init Refresh View
     */
    private void initRefresh() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getSportInfo();
                refreshLayout.finishRefresh(1000);
            }
        });

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                getMallList();
                refreshLayout.finishLoadMore(1000);
            }
        });
    }

    private void getCityList(boolean containDistrictFlg) {

//        NetManager.getInstance(getActivity()).doGetCityInfo(containDistrictFlg, new OnResponseListener<Api_PLACE_CityListResult>() {
//            @Override
//            public void onComplete(boolean isOK, Api_PLACE_CityListResult result, int errorCode, String errorMsg) {
//                if (result != null && result.cityList != null && result.cityList.size() > 0) {
//                    mCityList = new ArrayList<>(result.cityList);
//                    cityListReLoad();
//                }
//
////                List<Api_PLACE_City> list = result.cityList;
////                for (int i = 0; i < list.size(); i++) {
////                    RespCityInfo cityInfo = new RespCityInfo();
////                    Api_PLACE_City city = list.get(i);
////                    RespCity respCity = new RespCity();
////                    List<RespDistrict> respDistrict = new ArrayList<>();
////                    List<Api_PLACE_District> districtList = city.districtList;
////                    for (int j = 0; j < districtList.size(); j++) {
////                        RespDistrict district = new RespDistrict();
////                        Api_PLACE_District dist = districtList.get(j);
////                        district.setId(Integer.parseInt(dist.districtCode));
////                        district.setCityId(Integer.parseInt(dist.cityCode));
////                        district.setName(dist.name);
////                        district.setLat(dist.lat);
////                        district.setLng(dist.lng);
////
////                        respDistrict.add(district);
////                    }
////                    respCity.setName(city.name);
////                    respCity.setId(Integer.parseInt(city.cityCode));
////                    respCity.setLat(city.lat);
////                    respCity.setLng(city.lng);
////                    cityInfo.setRespCity(respCity);
////                    cityInfo.setRespDistrict(respDistrict);
////
////                    mCityList.add(cityInfo);
////                }
//
//            }
//
//            @Override
//            public void onInternError(int errorCode, String errorMessage) {
//
//            }
//        });
    }

//    private void cityListReLoad() {
//        mDistrictList.addAll(mCityList.get(0).districtList);
//        mSwitchCityAdapter = new PopNormalAdapter_OrderVenue<>(getActivity(), R.layout.popup_left_item, mCityList);
//        mSwitchDistrictAdapter = new PopNormalAdapter_OrderVenue<>(getActivity(), R.layout.popup_right_item, mDistrictList);
//
//        for (int i = 0; i < mCityList.size(); i++) {
//            if (LocationManager.getInstance().getStorage().getManual_cityName().equals(mCityList.get(i).name)) {
//                mSwitchCityAdapter.setPressPostion(i);
//                mCityPosition = i;
//                mDistrictList.clear();
//                mDistrictList.addAll(mCityList.get(i).districtList);
//                for (int j = 0; j < mDistrictList.size(); j++) {
//                    if (LocationManager.getInstance().getStorage().getManual_discName().equals(mDistrictList.get(j).name)) {
//                        mSwitchDistrictAdapter.setPressPostion(j);
//                        mDistrictPosition = j;
//                        if ("全部".equals(mDistrictList.get(j).name)) {
//                            mPopupBtnCity.setText(mCityList.get(i).name);
//                            mFloatPopup.setText(mCityList.get(i).name);
//                        } else {
//                            mPopupBtnCity.setText(mCityList.get(i).name + " " + mDistrictList.get(j).name);
//                            mFloatPopup.setText(mCityList.get(i).name + " " + mDistrictList.get(j).name);
//                        }
//                        break;
//                    }
//                }
//                break;
//            }
//        }
//
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
//                mFloatPopup.hidePopup();
//
//                Api_PLACE_City respCity = mCityList.get(mSwitchCityAdapter.getPressPostion());
//
//                if (position == 0) {
//                    mPopupBtnCity.setText(respCity.name);
//                    mFloatPopup.setText(respCity.name);
//                } else {
//                    mPopupBtnCity.setText(respCity.name + " " + mDistrictList.get(position).name);
//                    mFloatPopup.setText(respCity.name + " " + mDistrictList.get(position).name);
//                }
//                Analysis.cityAnalysis(getActivity(), userService.getLoginUserId(),"changeCity" + respCity.name + " " + mDistrictList.get(position).name,"运动");
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
//                getSportInfo();
//            }
//        });
//    }

//    private void cityPoint(String cityName, String pageName) {
//
//    }

    //初始化直播列表
    private void initLiveView(View root) {
        llytLive = (LinearLayout) root.findViewById(R.id.llyt_live_video);
        mLiveRecyclerView = (RecyclerView) root.findViewById(R.id.fragment_sport_live_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mLiveRecyclerView.setLayoutManager(layoutManager);
        mLiveRecyclerView.addItemDecoration(new SpacesItemDecoration(DisplayUtils.dp2px(getContext(), 8)));
        mLiveVideoAdapter = new LiveVideoAdapter(getContext(), 2);
        mLiveRecyclerView.setAdapter(mLiveVideoAdapter);
    }

    //联网获取直播数据
    private void initLiveData(List<Api_RESOURCECENTER_LiveRecordDto> lives) {
        ArrayList<LiveVideoInfo> list = new ArrayList<LiveVideoInfo>();

        if (lives == null) {
            return;
        }
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
            list.add(i);

        }

        Message msg = new Message();
        msg.what = MSG_GET_LIVE_LIST_SUCCESS;
        msg.obj = list;
        handler.sendMessage(msg);
    }

    private void initHealthView(View root) {
        llytCousult = (LinearLayout) root.findViewById(R.id.llyt_consult);

        mHealthRecyclerView = (RecyclerView) root.findViewById(R.id.fragment_sport_health_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mHealthRecyclerView.setLayoutManager(layoutManager);
        mHealthRecyclerView.addItemDecoration(new SpacesItemDecoration(DisplayUtils.dp2px(getContext(), 8)));
        mHealthAdapter = new HealthAdapter(getContext());
        mHealthRecyclerView.setAdapter(mHealthAdapter);
    }

    //联网获取健康咨询数据
    private void initHealthData(List<Api_RESOURCECENTER_ImUserDto> imUsers) {
        ArrayList<HealthInfo> mHealthInfoList = new ArrayList<HealthInfo>();
        for (Api_RESOURCECENTER_ImUserDto imUserDto : imUsers) {
            long id = imUserDto.id;// 用户id
            String avatar = imUserDto.avatar;// 用户头像
            String nickname = imUserDto.nickname;// 用户昵称
            String imLevelDesc = imUserDto.imLevelDesc;// 用户咨询等级描述

            HealthInfo healthInfo = new HealthInfo();
            healthInfo.setId(id);
            healthInfo.setName(nickname);
            healthInfo.setAvatar(avatar);
            healthInfo.setPositional(imLevelDesc);
            mHealthInfoList.add(healthInfo);

        }
        Message message = new Message();
        message.what = MSG_GET_HEALTH_LIST_SUCCESS;
        message.obj = mHealthInfoList;
        handler.sendMessage(message);
    }

    private void initVenueView(View root) {
        llytVenue = (LinearLayout) root.findViewById(R.id.llyt_venue);
        llVenue = (LinearLayout) root.findViewById(R.id.fragment_sport_llVenue);
    }

    private void initVenueView(VenueInfo i, boolean isShowDeliver) {
        if (null == getActivity()) return;
        View itemView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_venue_list_item, null);
        itemView.findViewById(R.id.line).setVisibility(isShowDeliver ? View.VISIBLE : View.GONE);
        LinearLayout llItem = (LinearLayout) itemView.findViewById(R.id.ll_item);
        ImageView ivOffen = (ImageView) itemView.findViewById(R.id.venue_list_item_ivOffen);
        ImageView sdSponsorHead = (ImageView) itemView.findViewById(R.id.venue_list_item_sdSponsorHead);
        TextView tvStartTime = (TextView) itemView.findViewById(R.id.venue_list_item_tvStartTime);
        TextView tvVenueName = (TextView) itemView.findViewById(R.id.venue_list_item_tvVenueName);
        TextView tvVenueDistance = (TextView) itemView.findViewById(R.id.venue_list_item_tvVenueDistance);
        TextView tvSponsorName = (TextView) itemView.findViewById(R.id.venue_list_item_tvSponsorName);
        TextView tvSportContent = (TextView) itemView.findViewById(R.id.venue_list_item_tvSportContent);
        TextView tvSignedNum = (TextView) itemView.findViewById(R.id.venue_list_item_tvSignedNum);
//        GridLayout glSignedHead = (GridLayout) itemView.findViewById(R.id.venue_list_item_glSignedHead);
        LinearLayout glSignedHead = (LinearLayout) itemView.findViewById(R.id.sport_members_layout);
        ImageView ivMore = (ImageView) itemView.findViewById(R.id.iv_more);
        TextView tvSignUp = (TextView) itemView.findViewById(R.id.venue_list_item_tvSignUp);
        TextView tvSignedUp = (TextView) itemView.findViewById(R.id.venue_list_item_tvSignedUp);
        tvSignUp.setTag(i);
        llItem.setTag(i);
        tvSignUp.setOnClickListener(this);
        llItem.setOnClickListener(this);

        String venueName = i.getVenueName();
        if (venueName != null && !venueName.isEmpty()) {
            tvVenueName.setText(venueName);
        }
        String venueDistance = i.getVenueDistance();
        if (venueDistance != null && !venueDistance.isEmpty()) {
            tvVenueDistance.setText(venueDistance);
        }
        String signedNum = i.getSignedNum();
        if (signedNum != null && !signedNum.isEmpty()) {
            tvSignedNum.setText(signedNum);
        }
        String sponsorName = i.getSponsorName();
        if (sponsorName != null && !sponsorName.isEmpty()) {
            tvSponsorName.setText(sponsorName);
        }
        String sponsorHead = i.getSponsorHead();
//        if (sponsorHead != null && !sponsorHead.isEmpty()) {
        sponsorHead = ImageUtils.getImageFullUrl(sponsorHead);
//            sdSponsorHead.setImageURI(sponsorHead);
        ImageLoadManager.loadCircleImage(CommonUtil.getImageFullUrl(sponsorHead), R.mipmap.icon_default_avatar, sdSponsorHead);
//        }

        String sportContent = i.getSportContent();
        if (sportContent != null && !sportContent.isEmpty()) {
            tvSportContent.setText(sportContent);
        }
        String startTime = i.getStartTime();
        if (startTime != null && !startTime.isEmpty()) {
            tvStartTime.setText(startTime);
        }
        if (i.isOffen()) {
            ivOffen.setVisibility(View.VISIBLE);
        } else {
            ivOffen.setVisibility(View.GONE);
        }
        if (i.isSigned()) {
            tvSignedUp.setVisibility(View.VISIBLE);
            tvSignUp.setVisibility(View.GONE);
        } else {
            tvSignedUp.setVisibility(View.GONE);
            tvSignUp.setVisibility(View.VISIBLE);
        }

        String[] signedHeads = i.getSignedHeads();
//        String[] signedHeads ={
//                "https://www.baidu.com/img/bd_logo1.png",
//                "https://www.baidu.com/img/bd_logo1.png",
//                "https://www.baidu.com/img/bd_logo1.png",
//                "https://www.baidu.com/img/bd_logo1.png",
//                "https://www.baidu.com/img/bd_logo1.png",
//                "https://www.baidu.com/img/bd_logo1.png",
//                "https://www.baidu.com/img/bd_logo1.png",
//                "https://www.baidu.com/img/bd_logo1.png",
//                "https://www.baidu.com/img/bd_logo1.png",
//                "https://www.baidu.com/img/bd_logo1.png"
//        };

        if (signedHeads != null && signedHeads.length > 0) {
            glSignedHead.removeAllViews();
            if (signedHeads.length > 6) {   //  显示不超过6个
                ivMore.setVisibility(View.VISIBLE);
                for (int j = 0; j < 6; j++) {
                    String head = signedHeads[j];
//                    if (head != null) {
                    ImageView simpleDraweeView = new ImageView(getContext());
                    int width = getResources().getDimensionPixelSize(R.dimen.value_20dp);
                    int height = getResources().getDimensionPixelSize(R.dimen.value_20dp);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
                    params.rightMargin = DisplayUtils.dp2px(getContext(), 5);
                    simpleDraweeView.setLayoutParams(params);
                    glSignedHead.addView(simpleDraweeView);
//                        if (TextUtils.isEmpty(head)) {
//                            simpleDraweeView.setImageURI(Uri.parse("res:///" + R.mipmap.icon_default_215_215));
//                        } else {
//                            head = ImageUtils.getImageFullUrl(head);
//                            simpleDraweeView.setImageURI(head);
//                        }
//                        GenericDraweeHierarchy hierarchy = GenericDraweeHierarchyBuilder.newInstance(getResources()).setRoundingParams(RoundingParams.asCircle()).build();
//                        simpleDraweeView.setHierarchy(hierarchy);
                    ImageLoadManager.loadCircleImage(CommonUtil.getImageFullUrl(head), R.mipmap.icon_default_avatar, simpleDraweeView);

//                    }
                }
            } else {
                ivMore.setVisibility(View.GONE);
                for (String head : signedHeads) {
//                    if (head != null) {
                    ImageView simpleDraweeView = new ImageView(getContext());
                    int width = getResources().getDimensionPixelSize(R.dimen.value_20dp);
                    int height = getResources().getDimensionPixelSize(R.dimen.value_20dp);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
                    params.rightMargin = DisplayUtils.dp2px(getContext(), 5);
                    simpleDraweeView.setLayoutParams(params);
                    glSignedHead.addView(simpleDraweeView);
//                        if (TextUtils.isEmpty(head)) {
//                            simpleDraweeView.setImageURI(Uri.parse("res:///" + R.mipmap.icon_default_215_215));
//                        } else {
//                            head = ImageUtils.getImageFullUrl(head);
//                            simpleDraweeView.setImageURI(head);
//                        }
//                        GenericDraweeHierarchy hierarchy = GenericDraweeHierarchyBuilder.newInstance(getResources()).setRoundingParams(RoundingParams.asCircle()).build();
//                        simpleDraweeView.setHierarchy(hierarchy);
                    ImageLoadManager.loadCircleImage(CommonUtil.getImageFullUrl(head), R.mipmap.icon_default_avatar, simpleDraweeView);


//                    }
                }
            }
        } else {
            glSignedHead.removeAllViews();
            ivMore.setVisibility(View.GONE);
        }

        llVenue.addView(itemView);

    }

    private void initVenueData(List<Api_RESOURCECENTER_PlaceActivityDto> activitys) {

        ArrayList<VenueInfo> list = new ArrayList<VenueInfo>();

        for (Api_RESOURCECENTER_PlaceActivityDto activityDto : activitys) {
            long activityId = activityDto.activityId;// 活动id
            long placeId = activityDto.placeId;// 活动id
            String placeName = activityDto.placeName;// 所属场馆名字
            long startTime = activityDto.startTime;// 活动开始时间
            String description = activityDto.description;// 活动描述
            long maleFee = activityDto.maleFee;// 男价格
            long femaleFee = activityDto.femaleFee;// 女价格
            int alreadySignTotalNum = activityDto.alreadySignTotalNum;// 总报名人数
            int starLevel = activityDto.starLevel;// 星级标识 默认0为普通 1为常去
            int totalUserCount = activityDto.totalUserCount;// 活动计划总工人数
            double distance = activityDto.distance;// 场馆距离当前位置距离
            Api_RESOURCECENTER_UserSimpleDto organiser = activityDto.organiser;// 组织者
            List<Api_RESOURCECENTER_JoinActivityUserDto> joinUsers = activityDto.joinUsers;// 报名人数

            long organiserId = 0;
            String organiserAvatar = null;
            String organiserNickname = null;
            if (organiser != null) {
                organiserId = organiser.id;
                organiserAvatar = organiser.avatar;
                organiserNickname = organiser.nickname;
            }

            String[] signedHeads = null;
            long[] signedIds = null;
            if (joinUsers != null && !joinUsers.isEmpty()) {
                signedHeads = new String[joinUsers.size()];
                signedIds = new long[joinUsers.size()];


                for (int i = 0; i < joinUsers.size(); i++) {
                    Api_RESOURCECENTER_JoinActivityUserDto joinUser = joinUsers.get(i);
                    long id = joinUser.id;
                    String avatar = joinUser.avatar;
                    String nickname = joinUser.nickname;
                    int insteadCount = joinUser.insteadCount;
                    signedHeads[i] = avatar;
                    signedIds[i] = id;
                }
            }
            boolean isSigned = false;
            if (signedIds != null && signedIds.length != 0) {
                for (long id : signedIds) {
                    if (id == userService.getLoginUserId()) {
                        isSigned = true;
                        break;
                    }
                }
            }

            Date date = new Date(startTime);
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

            VenueInfo i = new VenueInfo();
            i.setActivityId(activityId);
            i.setPlaceId(placeId);
            i.setOffen(starLevel == 0 ? false : true);
            i.setSigned(isSigned);
            i.setSignedNum(alreadySignTotalNum + "/" + totalUserCount);
            i.setSponsorName(organiserNickname);
            i.setSponsorHead(organiserAvatar);
            i.setSportContent(description);
            i.setStartTime(sdf.format(date));
            if (distance < 1) {
                i.setVenueDistance(Math.round(distance * 1000) + "m");
            } else {
                i.setVenueDistance(new DecimalFormat("#########0.0")
                        .format(distance) + "km");
            }
            i.setVenueName(placeName);
            i.setSignedHeads(signedHeads);
            i.setSignedIds(signedIds);
            list.add(i);

        }


        Message msg = new Message();
        msg.what = MSG_GET_VENUE_LIST_SUCCESS;
        msg.obj = list;
        handler.sendMessage(msg);
    }

    private void initMallView(View root) {
        llytMall = (LinearLayout) root.findViewById(R.id.llyt_mall);
        grdMall = root.findViewById(R.id.fragment_sport_grdMall);

//        materialAdapter = new QuickAdapter<ShortItem>(getActivity(), R.layout.integralmall_home_fictitious_item_view, new ArrayList<ShortItem>()) {
//            @Override
//            protected void convert(BaseAdapterHelper helper, ShortItem item) {
//                IntegralmallViewHelper.handleIntegralmallListItem(getActivity(), helper, item);
//            }
//        };
        materialAdapter = new MallAdapter(getActivity(), R.layout.integralmall_home_fictitious_item_view, new ArrayList<ShortItem>());
        grdMall.setAdapter(materialAdapter);
        grdMall.setLayoutManager(new GridLayoutManager(getContext(), 2));
        materialAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                //事件统计
                Analysis.pushEvent(getActivity(), AnEvent.ITEM, String.valueOf(materialAdapter.getData().get(i).id));
                ShortItem shortItem = (ShortItem) baseQuickAdapter.getItem(i);
                // 判断跳转本地还是h5
                if ("native".equals(shortItem.skipType)) {
                    NavUtils.gotoProductDetail(getContext(),
                            ItemType.POINT_MALL,
                            shortItem.id,
                            shortItem.title);
                } else {
                    // 跳h5
//                    NavUtils.startWebview(getActivity(), "", SPUtils.getURL_POINT_ITEM_DETAIL(getActivity()) + shortItem.id, 0);
                    String url = SPUtils.getURL_POINT_ITEM_DETAIL(getActivity());
                    if (TextUtils.isEmpty(url))
                        return;
                    NavUtils.startWebview(getActivity(), "", url.replace(":id", String.valueOf(shortItem.id)), 0);

                }
            }
        });
        grdMall.setNestedScrollingEnabled(false);

//        grdMall.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                ShortItem shortItem = (ShortItem) grdMall.getAdapter().getItem(position);
//                NavUtils.gotoProductDetail(getContext(),
//                        ItemType.POINT_MALL,
//                        shortItem.id,
//                        shortItem.title);
//            }
//        });
    }

    private void initMallData(List<Api_RESOURCECENTER_IntegralItemDto> items) {
//        ArrayList<MallInfo> list = new ArrayList<MallInfo>();
//        for (Api_RESOURCECENTER_IntegralItemDto item : items) {
//            long itemId = item.itemId;//item id
//            String cover = item.cover;//item封面
//            String itemTitle = item.itemTitle;//积分商品标题
//            int scorePrice = item.scorePrice;// 积分价格
//
//            MallInfo i = new MallInfo();
//            i.setItemId(itemId);
//            i.setCover(cover);
//            i.setItemTitle(itemTitle);
//            i.setScorePrice(scorePrice);
//            list.add(i);
//        }
//
//        Message msg = new Message();
//        msg.what = MSG_GET_MALL_LIST_SUCCESS;
//        msg.obj = list;
//        handler.sendMessage(msg);
        page = 1;

//        QueryTermsDTO params = new QueryTermsDTO();
//        params.pageNo = 1;
//        params.pageSize = 6;
//        params.boothCode = "PAGE_FEATURE_RECOMMEND";
//        params.latitude = YHYApplication.mLat;
//        params.longitude = YHYApplication.mLng;

//        List<QueryTerm> ts = new ArrayList<>();
//        QueryTerm t1 = new QueryTerm();
//        t1.type = QueryType.ITEM_TYPE;
//        t1.value = ItemType.POINT_MALL;
////NORMAL  POINT_MALL
//        ts.add(t1);
//        params.queryTerms = ts;
//        integralmallController.doGetIntegralmallList(getActivity(), params);
//        items.getItemsForPointMall
        CodeQueryDTO codeQueryDTO = new CodeQueryDTO();
//        codeQueryDTO.boothCode = ResourceType.YHY_YUNDONG_JFSC_RECOMMEND;
        codeQueryDTO.pageNo = page;
        codeQueryDTO.pageSize = PAGE_SIZE;
//        if (!StringUtil.isEmpty(String.valueOf(YHYApplication.mLat))) {
//            codeQueryDTO.latitude = YHYApplication.mLat;
//        }
//        if (!StringUtil.isEmpty(String.valueOf(YHYApplication.mLng))) {
//            codeQueryDTO.longitude = YHYApplication.mLng;
//        }
//        integralmallController.doGetIntegralmallListByCode(getActivity(), codeQueryDTO);
        integralmallController.doGetIntegralmallListByPage(getActivity(), codeQueryDTO);

    }

    private void initPedometer(int stepNum, Api_RESOURCECENTER_BannerDto banner, Api_RESOURCECENTER_BannerDto bannerLeft) {
        if (banner == null || bannerLeft == null)
            return;

        int num = stepNum;// 运动页运动步数
        long bannerId = banner.bannerId;// banner id
        String bannerCover = banner.cover;// banner封面
        String bannerType = banner.bannerType;// 展位类别
        String url = banner.url;// banner URL

        long bannerLeftId = bannerLeft.bannerId;
        String bannerLeftType = bannerLeft.bannerType;
        String bannerLeftCover = bannerLeft.cover;
        String bannerLeftUrl = bannerLeft.url;


        PedometerInfo i = new PedometerInfo();
        i.setStepNum(num);
        i.setBannerId(bannerId);
        i.setBannerCover(bannerCover);
        i.setBannerType(bannerType);
        i.setUrl(url);
        i.setBannerLeftCover(bannerLeftCover);

        Message message = new Message();
        message.what = MSG_GET_PEDOMETER_SUCCESS;
        message.obj = i;
        handler.sendMessage(message);
    }


    private void setGridViewHeight(GridView gridView) {
        ListAdapter adapter = gridView.getAdapter();
        if (adapter == null) {
            return;
        }
        int mNumColumns = gridView.getNumColumns();
        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i += mNumColumns) {
            View listItem = adapter.getView(i, null, gridView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight;
        gridView.setLayoutParams(params);
    }

    private void getSportInfo() {
        BigDecimal lat = new BigDecimal(Double.valueOf(LocationManager.getInstance().getStorage().getLast_lat()));
        BigDecimal lng = new BigDecimal(Double.valueOf(LocationManager.getInstance().getStorage().getLast_lng()));

        long latitude = lat.longValue();
        long longtitude = lng.longValue();

        String cityCode = LocationManager.getInstance().getStorage().getLast_cityCode();

        showLoadingView(null);
        NetManager.getInstance(getActivity()).doGetSportInfo(latitude, longtitude, cityCode, new OnResponseListener<Api_RESOURCECENTER_SportInfoResult>() {

            @Override
            public void onComplete(boolean isOK, Api_RESOURCECENTER_SportInfoResult result, int errorCode, String errorMsg) {
                if (result != null) {
                    int stepNum = result.stepNum;// 运动页运动步数
                    Api_RESOURCECENTER_BannerDto banner = result.banner;
                    Api_RESOURCECENTER_BannerDto bannerLeft = result.bannerLeft;
                    List<Api_RESOURCECENTER_PlaceActivityDto> activitys = result.activitys;
                    List<Api_RESOURCECENTER_LiveRecordDto> lives = result.lives;
                    List<Api_RESOURCECENTER_ImUserDto> imUsers = result.imUsers;
                    List<Api_RESOURCECENTER_IntegralItemDto> items = result.items;
                    Api_RESOURCECENTER_Booth centerBooth = result.centerBooth;
                    Api_RESOURCECENTER_Booth topBooth = result.topBooth;
                    Api_RESOURCECENTER_Booth twoEntranceBooth = result.twoEntranceBooth;
                    String background_url = result.smallBackground;
                    initPedometer(stepNum, banner, bannerLeft);
                    initLiveData(lives);
                    initHealthData(imUsers);
                    initMallData(items);
                    initCenterBooth(centerBooth);
                    if (TextUtils.isEmpty(background_url)) {
                        ivBackground.setImageResource(R.mipmap.sports_bg);
                        return;
                    }
                    setSportBackground(background_url);
                    initTopBooth(topBooth);
                    initTwoBooth(twoEntranceBooth);
                }
                hideLoadingView();
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                hideLoadingView();
            }
        });
    }

    private void initTwoBooth(Api_RESOURCECENTER_Booth twoEntranceBooth) {
        try {
            Booth value = Booth.deserialize(twoEntranceBooth.serialize());
            if (value == null || value.showcases == null || value.showcases.size() == 0) {
                return;
            }

            for (int i = 0; i < twoEntranceBooth.showcases.size(); i++) {
                RCShowcase rcShowcase = value.showcases.get(i);
                if (rcShowcase.title.equals("1")) {
                    setIvSportAct1(rcShowcase);
                    continue;
                }

                if (rcShowcase.title.equals("2")) {
                    setIvSportAct2(rcShowcase);
                    continue;
                }
            }
        } catch (Exception e) {

        }
    }

    private void initTopBooth(Api_RESOURCECENTER_Booth topBooth) {
        try {
            Booth value = Booth.deserialize(topBooth.serialize());
            if (value == null || value.showcases == null || value.showcases.size() == 0) {
                return;
            }

            if (value.showcases.size() != 4) {
                return;
            }

            setItem(value.showcases.get(0), ivItem1, tvItem1, vItem1);
            setItem(value.showcases.get(1), ivItem2, tvItem2, vItem2);
            setItem(value.showcases.get(2), ivItem3, tvItem3, vItem3);
            setItem(value.showcases.get(3), ivItem4, tvItem4, vItem4);
        } catch (Exception e) {

        }
    }

    /**
     * 快速咨询，积分商城，俱乐部等后台可配置入口
     *
     * @param centerBooth
     */
    private void initCenterBooth(Api_RESOURCECENTER_Booth centerBooth) {
        try {
            Booth value = Booth.deserialize(centerBooth.serialize());
            if (value == null || value.showcases == null || value.showcases.size() == 0) {
                llytCenterBoothOne.setVisibility(View.GONE);
                llytCenterBoothTwo.setVisibility(View.GONE);
                return;
            }

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            lp.weight = 1;
            if (value.showcases.size() > 8) {
                llytCenterBoothOne.removeAllViews();
                llytCenterBoothTwo.removeAllViews();
                for (int i = 0; i < 8; i++) {
                    if (i < 4) {  // 第一行
                        CenterBoothView centerBoothView = new CenterBoothView(getActivity(), value.showcases.get(i));
                        centerBoothView.setLayoutParams(lp);
                        llytCenterBoothOne.addView(centerBoothView);
                    } else {  // 第二行    有更多
                        if (i == 7) {
                            RCShowcase rcShowcase = new RCShowcase();
                            rcShowcase.title = "更多";
                            rcShowcase.isMore = true;
                            CenterBoothView centerBoothView = new CenterBoothView(getActivity(), rcShowcase);
                            centerBoothView.setLayoutParams(lp);
                            llytCenterBoothTwo.addView(centerBoothView);
                        } else {
                            CenterBoothView centerBoothView = new CenterBoothView(getActivity(), value.showcases.get(i));
                            centerBoothView.setLayoutParams(lp);
                            llytCenterBoothTwo.addView(centerBoothView);
                        }
                    }
                }
                llytCenterBoothOne.setVisibility(View.VISIBLE);
                llytCenterBoothTwo.setVisibility(View.VISIBLE);
            } else if (value.showcases.size() > 4) {
                llytCenterBoothOne.removeAllViews();
                llytCenterBoothTwo.removeAllViews();
                for (int i = 0; i < 8; i++) {
                    if (i < 4) {  // 第一行
                        CenterBoothView centerBoothView = new CenterBoothView(getActivity(), value.showcases.get(i));
                        centerBoothView.setLayoutParams(lp);
                        llytCenterBoothOne.addView(centerBoothView);
                    } else {  // 第二行   没有更多，显示完
                        if (i < value.showcases.size()) {
                            CenterBoothView centerBoothView = new CenterBoothView(getActivity(), value.showcases.get(i));
                            centerBoothView.setLayoutParams(lp);
                            llytCenterBoothTwo.addView(centerBoothView);
                        } else {  // 填充满一行
                            CenterBoothView centerBoothView = new CenterBoothView(getActivity(), null);
                            centerBoothView.setLayoutParams(lp);
                            llytCenterBoothTwo.addView(centerBoothView);
                        }
                    }
                }
                llytCenterBoothOne.setVisibility(View.VISIBLE);
                llytCenterBoothTwo.setVisibility(View.VISIBLE);
            } else {
                llytCenterBoothOne.removeAllViews();
                for (RCShowcase rcShowcase : value.showcases) {
                    CenterBoothView centerBoothView = new CenterBoothView(getActivity(), rcShowcase);
                    centerBoothView.setLayoutParams(lp);
                    llytCenterBoothOne.addView(centerBoothView);
                }
                llytCenterBoothOne.setVisibility(View.VISIBLE);
                llytCenterBoothTwo.setVisibility(View.GONE);
            }

        } catch (Exception e) {

        }

    }

    private void setSportBackground(String imgUrl) {
//        ImageLoadManager.loadImage(getActivity(), imgUrl, R.mipmap.sports_bg, new ImageLoadManager.onBitmapListener() {
//            @Override
//            public void onResult(Bitmap bitmap) {
//                mScrollBgView.setBackground(new BitmapDrawable(getActivity().getResources(), bitmap));
//            }
//        });

        ImageLoadManager.loadImage(imgUrl, ivBackground);
        setSportToolStyle();
    }

    // 顶部足球，篮球，羽毛球，网球点击
    private void setItem(RCShowcase rcShowcase, ImageView imageView, TextView textView, View vItem) {
        int res = 0;
        if (rcShowcase.title.equals(SPORT_FOOTBALL)) {
            res = R.mipmap.sports_icon_football;
        } else if (rcShowcase.title.equals(SPORT_BASKETBALL)) {
            res = R.mipmap.sports_icon_basketball;
        } else if (rcShowcase.title.equals(SPORT_TENNIS)) {
            res = R.mipmap.sports_icon_tennis;
        } else if (rcShowcase.title.equals(SPORT_BADMINTON)) {
            res = R.mipmap.sports_icon_badminton;
        }

        ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(rcShowcase.imgUrl), res, imageView);
        textView.setText(rcShowcase.title);
        vItem.setTag(rcShowcase);
    }

    private void setIvSportAct1(RCShowcase rcShowcase) {
        rcShowcaseAct1 = rcShowcase;
        ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(rcShowcase.imgUrl), ivSportAct1);
    }

    private void setIvSportAct2(RCShowcase rcShowcase) {
        rcShowcaseAct2 = rcShowcase;
        ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(rcShowcase.imgUrl), ivSportAct2);
    }

    private void setSportToolStyle() {
        mScrollActView.setVisibility(View.VISIBLE);
        mFloatToolView.setBackgroundColor(Color.WHITE);
        Drawable drawable = getResources().getDrawable(R.drawable.popup_btn_arrow_black);
        mFloatPopup.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
        mFloatPopup.setTextColor(Color.parseColor("#333333"));
        float_btnChat.setImageResource(R.mipmap.icon_message);
        tvFloatMsgCount.setTextColor(Color.WHITE);
        tvFloatMsgCount.setBackgroundResource(R.drawable.background_ying_red_circle_fill);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.fragment_sport_ivVenueMore:           //更多场馆
//                NavUtils.startWebview(getActivity(), "更多场馆", SPUtils.getClubActivityList(getContext()), 0);
                String venuurl = getWebViewUrl(SPUtils.getVenueActivityList(getContext()));
                if (TextUtils.isEmpty(venuurl))
                    return;
                if (!TextUtils.isEmpty(LocationManager.getInstance().getStorage().getManual_discCode())) {
                    venuurl = venuurl + "&districtCode=" + LocationManager.getInstance().getStorage().getLast_cityCode();
                }
                NavUtils.startWebview(getActivity(), "", venuurl, 0);
                break;

            case R.id.ll_item:
            case R.id.venue_list_item_tvSignUp:             //场馆详情
                VenueInfo i = (VenueInfo) v.getTag();
                long activityId = i.getActivityId();
                long placeId = i.getPlaceId();
                String url = SPUtils.getClubActivityDetail(getContext());
                url = url.replace(":id", String.valueOf(activityId));
                Log.i("shenwenjie", url);
                NavUtils.startWebview(getActivity(), "", url, 0);
                break;
            case R.id.fragment_sport_ivIntegralMallMore:    //积分商城
                //事件统计
                Analysis.pushEvent(getActivity(), AnEvent.SHOPPING_MALL_HOMEPAGE);
                NavUtils.gotoIntegralmallHomeActivity(getContext());
                break;

            case R.id.fragment_sport_ivLiveList:            //视频列表
                //事件统计
                Analysis.pushEvent(getActivity(), AnEvent.WONDERFUL_VIDEO);
                YhyRouter.getInstance().startWonderfulVideoListActivity(getContext());
//                NavUtils.startWebview(getActivity(), "", SPUtils.getVideoList(getContext()), 0);
                break;

            case R.id.fragment_sport_ivConsultList:         //全部咨询
                //事件统计
                Analysis.pushEvent(getActivity(), AnEvent.HEALTH_CONSULTATION);
                Intent intent = new Intent(getContext(), MasterAdviceListActivity.class);
                startActivity(intent);
                break;

            case R.id.rl_leftbanner:           //计步器
                //事件统计
                Analysis.pushEvent(getActivity(), AnEvent.ZXSJ);
                NavUtils.gotoPedometerActivity(getContext());
                break;

            case R.id.rl_rightbanner:              //计步器广告
                //事件统计
                Analysis.pushEvent(getActivity(), AnEvent.YQHY);
                if (v.getTag() != null) {
                    String urL = v.getTag().toString();
                    NavUtils.startWebview(getActivity(), "", urL, 0);
                } else {
                    ToastUtil.showToast(getActivity(), "网络错误，链接获取失败");
                }
                break;

            case R.id.fragment_sport_item1:           //足球
            case R.id.fragment_sport_item2:        // 篮球
            case R.id.fragment_sport_item3:             //网球
            case R.id.fragment_sport_item4:          //羽毛球
                //事件统计
                String event = "";
                RCShowcase rcShowcase = (RCShowcase) v.getTag();
                if (rcShowcase == null) {
                    return;
                }

                if (rcShowcase.title.equals(SPORT_FOOTBALL)) {
                    event = AnEvent.FOOTBALL;
                } else if (rcShowcase.title.equals(SPORT_BASKETBALL)) {
                    event = AnEvent.BASKETBALL;
                } else if (rcShowcase.title.equals(SPORT_TENNIS)) {
                    event = AnEvent.TENNIS;
                } else if (rcShowcase.title.equals(SPORT_BADMINTON)) {
                    event = AnEvent.BADMINTON;
                }

                Analysis.pushEvent(getActivity(), event);
                NavUtils.depatchAllJump(getActivity(), (RCShowcase) v.getTag(), -1);
                break;
            case R.id.sport_act_1:          //
                //事件统计
//                NavUtils.startWebview(getActivity(), "", getWebViewUrl(SPUtils.getBadmintonBooking(getContext())) + "/" + LocationManager.getInstance().getLast_discCode(), 0);
                NavUtils.depatchAllJump(getActivity(), rcShowcaseAct1, -1);
                break;
            case R.id.sport_act_2:          //
                //事件统计
//                NavUtils.startWebview(getActivity(), "", getWebViewUrl(SPUtils.getBadmintonBooking(getContext())) + "/" + LocationManager.getInstance().getLast_discCode(), 0);
                NavUtils.depatchAllJump(getActivity(), rcShowcaseAct2, -1);
                break;

            default:
                break;
        }
    }

    private String getWebViewUrl(String booking) {
        try {
            if (TextUtils.isEmpty(booking))
                return null;
            String cityCode = "";
            cityCode = LocationManager.getInstance().getStorage().getLast_cityCode();
            String urlBtnBadminton = booking.replaceAll(":cityCode", cityCode);
            return urlBtnBadminton;
        } catch (Exception e) {
            return "";
        }

    }

    @Override
    public View onLoadContentView() {
        return View.inflate(getActivity(), R.layout.fragment_sport, null);
    }

    @Override
    public void onStart() {
        super.onStart();
        PedometerUtil.getInstance().bindService(getActivity());
        PedometerUtil.getInstance().addListener(mOnStepListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        PedometerUtil.getInstance().removeListener(mOnStepListener);
        PedometerUtil.getInstance().unBindService(getActivity());
    }

    private PedometerUtil.OnStepListener mOnStepListener = new PedometerUtil.OnStepListener() {
        @Override
        public void onStep(long step, double speed, double calories, long time, double distance) {
            tvPedometer.setText(String.valueOf(step));
        }
    };

    /**
     * 城市选择改变后同步
     *
     * @param event
     */
    public void onEvent(EvBusLocationChange event) {
        if (event.isFromWeb) return;
        if (!TextUtils.isEmpty(LocationManager.getInstance().getStorage().getManual_cityName())) {
//            if (LocationManager.getInstance().getStorage().getManual_discName().contains("全")) {
            mPopupBtnCity.setText(LocationManager.getInstance().getStorage().getManual_cityName());
            mFloatPopup.setText(LocationManager.getInstance().getStorage().getManual_cityName());
//            } else {
//                mPopupBtnCity.setText(LocationManager.getInstance().getStorage().getManual_cityName() + " " + LocationManager.getInstance().getStorage().getManual_discName());
//                mFloatPopup.setText(LocationManager.getInstance().getStorage().getManual_cityName() + " " + LocationManager.getInstance().getStorage().getManual_discName());
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
            getSportInfo();
        }
    }

    /**
     * 同步下拉选项中位置列表的选择
     */
//    private void synLocationSelectedState(String city, String district) {
//        if (mCityList == null || mCityList.size() == 0) return;
//
//        int cityPosition = 0;
//        int districtPosition = 0;
//
//        for (int i = 0; i < mCityList.size(); i++) {
//            if (city.equals(mCityList.get(i).name)) {
//                cityPosition = i;
//                break;
//            }
//        }
//
//        mDistrictList.clear();
//        mDistrictList.addAll(mCityList.get(cityPosition).districtList);
//
//        for (int j = 0; j < mDistrictList.size(); j++) {
//            if (district.equals(mDistrictList.get(j).name)) {
//                districtPosition = j;
//                break;
//            }
//        }
//        mSwitchCityAdapter.setPressPostion(cityPosition);
//        mSwitchDistrictAdapter.setPressPostion(districtPosition);
//    }
    public void onEvent(EvBusMessageCount evBusMessageCount) {
        int count = evBusMessageCount.getCount();
        updateIMMessageCount(count);
    }

    /**
     * 刷新消息数
     *
     * @param messageNum
     */
    public void updateIMMessageCount(int messageNum) {
        if (messageNum == 0) {
            tvNewMsgCount.setVisibility(View.INVISIBLE);
            tvFloatMsgCount.setVisibility(View.INVISIBLE);
        } else {
            tvNewMsgCount.setVisibility(View.VISIBLE);
            tvNewMsgCount.setText("" + messageNum);
            tvFloatMsgCount.setVisibility(View.VISIBLE);
            tvFloatMsgCount.setText("" + messageNum);
        }
    }

    private boolean isLoggedIn() {
        long uid = userService.getLoginUserId();
        Log.i("shenwenjie", "uid=" + uid);
        if (uid > 0) {
            return true;
        } else {
            YhyRouter.getInstance().startLoginActivity(getContext(), null, Intent.FLAG_ACTIVITY_CLEAR_TOP, IntentConstants.LOGIN_RESULT);
            return false;
        }
    }

    public void onEvent(EvBusWhenSelectFragment event) {
        if (event.index == 1) {
            scroll_parent.scrollTo(0, 0);
            refreshLayout.autoRefresh();
        }
    }

    public void onEvent(EvBusLocation evBusLocation) {
//        if (LocationManager.getInstance().getStorage().getManual_discName().contains("全")) {
        mPopupBtnCity.setText(LocationManager.getInstance().getStorage().getManual_cityName());
        mFloatPopup.setText(LocationManager.getInstance().getStorage().getManual_cityName());
//        } else {
//            mPopupBtnCity.setText(LocationManager.getInstance().getStorage().getManual_cityName() + " " + LocationManager.getInstance().getStorage().getManual_discName());
//            mFloatPopup.setText(LocationManager.getInstance().getStorage().getManual_cityName() + " " + LocationManager.getInstance().getStorage().getManual_discName());
//        }
//        synLocationSelectedState(LocationManager.getInstance().getStorage().getManual_cityName(), LocationManager.getInstance().getStorage().getManual_discName());
    }
}
