package com.quanyan.yhy.ui.personal;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.harwkin.nb.camera.ImageUtils;
import com.newyhy.activity.HomeMainTabActivity;

import com.quanyan.yhy.R;
import com.quanyan.yhy.common.DirConstants;
import com.quanyan.yhy.eventbus.EvBusMessageCount;
import com.quanyan.yhy.eventbus.EvBusWhenSelectFragment;
import com.quanyan.yhy.libanalysis.AnEvent;
import com.quanyan.yhy.libanalysis.Analysis;
import com.quanyan.yhy.net.NetManager;
import com.quanyan.yhy.net.lsn.OnResponseListener;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.common.AppSettingsActivity;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.personal.model.ClubInfo;
import com.quanyan.yhy.ui.personal.model.CommonToolInfo;
import com.quanyan.yhy.ui.personal.model.OperatingDataInfo;
import com.quanyan.yhy.ui.personal.model.OrderInfo;
import com.quanyan.yhy.ui.personal.model.RankingListInfo;
import com.quanyan.yhy.util.QRCodeUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.smart.sdk.api.resp.Api_RESOURCECENTER_ClubSimpleDto;
import com.smart.sdk.api.resp.Api_RESOURCECENTER_GameRankDto;
import com.smart.sdk.api.resp.Api_RESOURCECENTER_MemberLevelDto;
import com.smart.sdk.api.resp.Api_RESOURCECENTER_MyChareQrCodeDto;
import com.smart.sdk.api.resp.Api_RESOURCECENTER_PlaceSimpleDto;
import com.smart.sdk.api.resp.Api_RESOURCECENTER_TrainSimpleDto;
import com.smart.sdk.api.resp.Api_RESOURCECENTER_UserInfoResult;
import com.smart.sdk.api.resp.Api_RESOURCECENTER_UserSimpleDto;
import com.smart.sdk.api.resp.Api_RESOURCECENTER_UserToolDto;
import com.smart.sdk.api.resp.Api_RESOURCECENTER_UserToolsResult;
import com.smart.sdk.api.resp.Api_RESOURCECENTER_UserWalletResult;
import com.smart.sdk.api.resp.Api_TRADEMANAGER_DetailOrder;
import com.yhy.common.base.BaseNewActivity;
import com.yhy.common.base.YHYBaseApplication;
import com.yhy.common.beans.net.model.param.WebParams;
import com.yhy.common.beans.user.User;
import com.yhy.common.beans.user.UserLevelInfo;
import com.yhy.common.constants.IntentConstants;
import com.yhy.common.eventbus.event.EvBusGetUserExInfo;
import com.yhy.common.eventbus.event.EvBusGetUserInfo;
import com.yhy.common.utils.CommonUtil;
import com.yhy.common.utils.SPUtils;
import com.yhy.imageloader.ImageLoadManager;
import com.yhy.router.YhyRouter;
import com.yhy.service.IUserService;
import com.yixia.camera.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;

import static android.app.Activity.RESULT_OK;
import static com.umeng.commonsdk.stateless.UMSLEnvelopeBuild.mContext;

/**
 * Created by shenwenjie on 2018/1/22.
 * 我的页面
 */

public class PersonalFragment extends Fragment {

    //refresh
    private SmartRefreshLayout refreshLayout;
    private ScrollView scroll_parent;
    private ViewGroup container;
    private GridView grdCommonTools;
    private ToolAdapter toolAdapter;
    private LinearLayout llMyOrder;
    private LinearLayout llRankingList;
    private LinearLayout llClub;
    private LinearLayout llTrain;
    private LinearLayout llCoach;
    private LinearLayout llOperatingData;
    private LinearLayout llQRCode;
    private LinearLayout layout_login;
    private LinearLayout layout_not_login;
    private RelativeLayout rl_my_order;

    private TextView tvAllOrder;
    private TextView tvAllTool;

    private ImageView sdHead;
    private TextView tvNickname;
    private TextView tvFollowedCount;
    private TextView tvFansCount;
    private TextView tvPicCount;
    private TextView tvExp;
    private TextView tvGrade;
    private ImageView btnQrCode;
    private View level_icon;

    private ProgressBar progressBar;
    private TextView tvRechargeBalance;
    private TextView tvPointNum;
    private TextView tvCardCoupons;
    private TextView tvNewMsgCount;

    //各个模块的View布局，最近订单的布局
    private View myOrderView;
    //登录时候的View布局，

    private final int MSG_GET_COMMON_TOOL_SUCCESS = 1;
    private final int MSG_GET_COMMON_TOOL_FAIL = -1;
    private final int MSG_GET_RANKING_LIST_SUCCESS = 2;
    private final int MSG_GET_RANKING_LIST_FAIL = -2;
    private final int MSG_GET_MY_ORDER_SUCCESS = 3;
    private final int MSG_GET_MY_ORDER_FAIL = -3;
    private final int MSG_GET_CLUB_SUCCESS = 4;
    private final int MSG_GET_CLUB_FAIL = -4;
    private final int MSG_GET_OPERATING_DATA_SUCCESS = 5;
    private final int MSG_GET_OPERATING_DATA_FAIL = -5;
    private final int MSG_GET_TRAIN_SUCCESS = 6;
    private final int MSG_GET_TRAIN_FAIL = -6;

    private final int MSG_GET_COACH_SUCCESS = 7;
    private final int MSG_GET_COACH_FAIL = -7;

    @Autowired
    IUserService userService;

    private MyHandler handler;
    private ArrayList<CommonToolInfo> toolsList = new ArrayList<>();//ToolsList

    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

                case MSG_GET_COMMON_TOOL_FAIL:
                    break;
                case MSG_GET_MY_ORDER_FAIL:
                    break;
                case MSG_GET_RANKING_LIST_SUCCESS: {
                    ArrayList<RankingListInfo> list = (ArrayList<RankingListInfo>) msg.obj;
                    if (list != null && !list.isEmpty()) {
                        for (RankingListInfo i : list) {
                            initRankingListView(i);
                        }
                    }
                }
                break;
                case MSG_GET_RANKING_LIST_FAIL:
                    break;
                case MSG_GET_CLUB_SUCCESS: {
                    ArrayList<ClubInfo> list = (ArrayList<ClubInfo>) msg.obj;
                    if (list != null && !list.isEmpty()) {
                        for (ClubInfo i : list) {
                            initClubView(i);
                        }
                    }
                }
                break;
                case MSG_GET_CLUB_FAIL:
                    break;
                case MSG_GET_TRAIN_SUCCESS: {
                    Api_RESOURCECENTER_TrainSimpleDto trainSimpleDto = (Api_RESOURCECENTER_TrainSimpleDto) msg.obj;
                    if (trainSimpleDto != null) {
                        initTrainsView(trainSimpleDto);
                    }
                }
                break;
                case MSG_GET_COACH_SUCCESS: {
                    Api_RESOURCECENTER_TrainSimpleDto trainSimpleDto = (Api_RESOURCECENTER_TrainSimpleDto) msg.obj;
                    if (trainSimpleDto != null) {
                        initCoachView(trainSimpleDto);
                    }
                }
                break;
                case MSG_GET_OPERATING_DATA_SUCCESS: {
                    ArrayList<OperatingDataInfo> list = (ArrayList<OperatingDataInfo>) msg.obj;
                    if (list != null && !list.isEmpty()) {
                        for (OperatingDataInfo i : list) {
                            initOperatingDataView(i);
                        }
                    }
                }
                break;
                case MSG_GET_OPERATING_DATA_FAIL:
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new MyHandler();
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.container = container;
        View root = inflater.inflate(R.layout.fragment_personal, container, false);
        //initRefresh
        refreshLayout = root.findViewById(R.id.refreshLayout);
        initRefresh();
        scroll_parent = root.findViewById(R.id.scroll_parent);
        TextView btnSetting = root.findViewById(R.id.fragment_personal_btnSetting);
        btnSetting.setOnClickListener(v -> {
            //统计事件
            Analysis.pushEvent(mContext, AnEvent.SETTING);
//                if (isLoggedIn()) {//设置页面
            Intent intent = new Intent(getContext(), AppSettingsActivity.class);
            startActivityForResult(intent, AppSettingsActivity.SETTING_RESULT);
//                } else {//去登录
//                    Intent intent = new Intent(getContext(), LoginActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivityForResult(intent, LoginActivity.LOGIN_RESULT);
//                }
        });

        ImageView btnChat = root.findViewById(R.id.fragment_personal_btnChat);
        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLoggedIn()) {
                    NavUtils.gotoMsgCenter(getContext());
                }
            }
        });

        //二维码
        btnQrCode = root.findViewById(R.id.fragment_personal_btnQrCode);
        btnQrCode.setVisibility(View.VISIBLE);
        btnQrCode.setOnClickListener(v -> {
            //统计事件
            Analysis.pushEvent(mContext, AnEvent.QR_CODE);
            NavUtils.gotoMineQrActivity(getContext());
        });

        //添加卡片
        Button btnAddCard = root.findViewById(R.id.fragment_personal_btnAddCard);
        btnAddCard.setOnClickListener(v -> {
            //统计事件
            Analysis.pushEvent(mContext, AnEvent.ADD_CARD);
            if (isLoggedIn()) {
                //URL_ADD_MY_CARD
                String url = SPUtils.getAddMyCard(getContext());
                if (url != null && !url.isEmpty()) {
                    NavUtils.startWebview(getActivity(), "", url, 0);
                }
            }
        });

        //登录模块
        layout_login = root.findViewById(R.id.layout_login);
        layout_not_login = root.findViewById(R.id.layout_not_login);
        initLoginLayout(root);
        //订单标题栏
        tvAllOrder = root.findViewById(R.id.fragment_personal_tvAllOrder);
        tvAllOrder.setOnClickListener(v -> {
            //统计事件
            Analysis.pushEvent(mContext, AnEvent.MY_ORDER);
            if (isLoggedIn()) {
                //URL_MY_ORDER
                String url = SPUtils.getMyorder(getContext());
                if (url != null && !url.isEmpty()) {
                    NavUtils.startWebview(getActivity(), "", url, 0);
                }
            }
        });
        //工具标题栏
        tvAllTool = root.findViewById(R.id.fragment_personal_tvAllTool);
        tvAllTool.setOnClickListener(v -> {
            //统计事件
            Analysis.pushEvent(mContext, AnEvent.COMMON_TOOLS_MORE);
            if (isLoggedIn()) {
                //URL_SHOW_ALL_TOOL
                String url = SPUtils.getShowAllTool(getContext());
                if (url != null && !url.isEmpty()) {
                    NavUtils.startWebview(getActivity(), "", url, 0);
                }
            }
        });
        //账户余额
        tvRechargeBalance = root.findViewById(R.id.fragment_personal_tvRechargeBalance);
        root.findViewById(R.id.ll_personal_recharge_Balance).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Analysis.pushEvent(getActivity(), AnEvent.MY_WALLET);
                if (isLoggedIn()) {
                    //URL_RECHARGE_DETAIL
                    String url = SPUtils.getRechargeDetail(getContext());
                    if (url != null && !url.isEmpty()) {
                        NavUtils.startWebview(getActivity(), "", url, 0);
                    }
                }
            }
        });
        //当前积分
        tvPointNum = root.findViewById(R.id.fragment_personal_tvPointNum);
        root.findViewById(R.id.ll_personal_pointNum).setOnClickListener(v -> {
            if (isLoggedIn()) {
                //URL_POINT_DETAIL
                String url = SPUtils.getPointDetail(getContext());
                if (url != null && !url.isEmpty()) {
                    NavUtils.startWebview(getActivity(), "", url, 0);
                }
            }
        });
        //卡劵
        tvCardCoupons = root.findViewById(R.id.fragment_personal_tvCardCoupons);
        root.findViewById(R.id.ll_personal_card_coupons).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLoggedIn()) {
                    //URL_CARD_VOUCHER
                    String url = SPUtils.getCardVoucher(getContext());
                    if (url != null && !url.isEmpty()) {
                        NavUtils.startWebview(getActivity(), "", url, 0);
                    }
                }
            }
        });
        tvNewMsgCount = root.findViewById(R.id.personal_message_num);

        initCommonToolView(root);//常用工具
        llMyOrder = root.findViewById(R.id.fragment_personal_llMyOrder);//最近订单
        llRankingList = root.findViewById(R.id.fragment_personal_llRankingList);//排行榜
        llClub = root.findViewById(R.id.fragment_personal_llClub);//俱乐部管理
        llTrain = root.findViewById(R.id.fragment_personal_llTrain);//企业培训管理
        llCoach = root.findViewById(R.id.fragment_personal_llCoach);//私教管理
        llOperatingData = root.findViewById(R.id.fragment_personal_llOperatingData);//场馆管理
        llQRCode = root.findViewById(R.id.fragment_personal_llQRCode);//二维码管理
        return root;
    }

    /**
     * 初始化登录模块的布局
     */
    private void initLoginLayout(View root) {
        //已登录初始化
        sdHead = root.findViewById(R.id.fragment_personal_sdHead);
        sdHead.setOnClickListener(v -> NavUtils.gotoMasterHomepage(getContext(), userService.getLoginUserId()));
        tvNickname = root.findViewById(R.id.fragment_personal_tvNickname);
        tvNickname.setText("");
        tvFollowedCount = root.findViewById(R.id.fragment_personal_tvFollowedCount);
        tvFollowedCount.setText(String.valueOf(0));
        tvFansCount = root.findViewById(R.id.fragment_personal_tvFansCount);
        tvFansCount.setText(String.valueOf(0));
        tvPicCount = root.findViewById(R.id.fragment_personal_dynamic_count);
        tvPicCount.setText(String.valueOf(0));
        tvExp = root.findViewById(R.id.tv_level_point);
        tvGrade = root.findViewById(R.id.tv_level);
        level_icon = root.findViewById(R.id.iv_level);
        //progressBar = (ProgressBar) view.findViewById(R.id.fragment_personal_progresssBar);
        //关注
        root.findViewById(R.id.ll_to_follow).setOnClickListener(view -> NavUtils.gotoMyAttentionListActivity(getContext(), userService.getLoginUserId()));
        //粉丝
        root.findViewById(R.id.ll_to_fans).setOnClickListener(view -> NavUtils.gotoMyFansListActivity(getContext(), userService.getLoginUserId()));
        //我的动态
        root.findViewById(R.id.ll_to_my_dynamic).setOnClickListener(view -> NavUtils.gotoMyDaynamicActivity(getContext()));
        //会员等级跳转
        root.findViewById(R.id.rl_level).setOnClickListener(view -> {
            if (isLoggedIn()) {
                String url = SPUtils.getMemberLevel(getContext());
                if (url != null && !url.isEmpty()) {
                    NavUtils.startWebview(getActivity(), "", url, 0);
                }
            }
        });

        //未登录引导去登录界面
        root.findViewById(R.id.layout_not_login).setOnClickListener(v -> YhyRouter.getInstance().startLoginActivity(v.getContext(), null, Intent.FLAG_ACTIVITY_CLEAR_TOP, IntentConstants.LOGIN_RESULT));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateIMMessageCount(HomeMainTabActivity.imUnreadCount);
    }

    public void onEvent(EvBusMessageCount evBusMessageCount) {
        int count = evBusMessageCount.getCount();
        updateIMMessageCount(count);
    }

    /**
     * init Refresh View
     */
    private void initRefresh() {
        refreshLayout.setEnableLoadMore(false);//是否启用上拉加载功能
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            initLoginView();
            refreshLayout.finishRefresh(1000);
        });
    }

    public void initLoginView() {
        //重置排行榜
        llRankingList.removeAllViews();
        //重置俱乐部
        llClub.removeAllViews();
        //重置培训
        llTrain.removeAllViews();

        llCoach.removeAllViews();

        //重置经营数据
        llOperatingData.removeAllViews();
        //重置二维码
        llQRCode.removeAllViews();
        //重置工具栏
        toolsList.clear();

        if (userService.isLogin()) {
            btnQrCode.setVisibility(View.VISIBLE);
            layout_login.setVisibility(View.VISIBLE);
            layout_not_login.setVisibility(View.GONE);
            GetMineInfo(userService.getLoginUserId());
            getRecentOrder();
            GetUserWallet();
            GetUserToolsInfo();
        } else {
            btnQrCode.setVisibility(View.GONE);
            layout_login.setVisibility(View.GONE);
            layout_not_login.setVisibility(View.VISIBLE);
            //重置订单
            llMyOrder.removeAllViews();
            //注销账户，返回时重置UI
            tvCardCoupons.setText("- -");
            tvPointNum.setText("- -");
            tvRechargeBalance.setText("- -");
            //QrCode
            btnQrCode.setVisibility(View.GONE);
            GetToolsInfoWithOutLogin();
            ((BaseNewActivity) getActivity()).hideLoadingView();
            //initCommonToolData();
        }
    }

    private void initUserInfo(User user) {
        if (user == null || !userService.isLoginUser(user.getUserId())) {
            return;
        }
        String nickname = user.getNickname();
        if (nickname != null && !nickname.isEmpty()) {
            char[] chars = nickname.toCharArray();
            String name = "";
            boolean exceedWidth = false;
            for (char aChar : chars) {
                name = name + aChar;
                if (doJudgeTextWidth(tvNickname, name)) {
                    exceedWidth = true;
                    if (name.length() > 2)
                        tvNickname.setText(name.substring(0, name.length() - 2) + "...");
                    break;
                }
            }
            if (!exceedWidth) {
                tvNickname.setText(nickname);
            }
        }
        String avatar = user.getAvatar();
        if (null != getActivity() && null != sdHead) {
            ImageLoadManager.loadCircleImage(CommonUtil.getImageFullUrl(avatar), R.mipmap.defauthead, sdHead);
        }
        if (null != avatar) {//存储头像Icon
            Glide.with(YHYBaseApplication.getInstance()).asBitmap().load(CommonUtil.getImageFullUrl(avatar)).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull final Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    //放在子线程去读写
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                saveFile(resource);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                }
            });
        }
    }


    private void GetMineInfo(long userId) {
        User user = userService.getUserInfo(userId);
        initUserInfo(user);

        User extInfo = userService.getUserExtInfo(userId);

        initUserExtInfo(extInfo);
    }

    /**
     * 判断TextView的内容宽度是否超出其可用宽度
     *
     * @param tv
     * @param name
     * @return
     */
    public boolean doJudgeTextWidth(TextView tv, String name) {
        int availableWidth = tv.getWidth() - tv.getPaddingLeft() - tv.getPaddingRight();
        Paint textViewPaint = tv.getPaint();
        float textWidth = textViewPaint.measureText(name);
        if (textWidth > availableWidth) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取最近订单
     */
    private void getRecentOrder() {
        NetManager.getInstance(getContext()).doGetUserRecentSportsOrder(new OnResponseListener<Api_TRADEMANAGER_DetailOrder>() {
            @Override
            public void onComplete(boolean isOK, Api_TRADEMANAGER_DetailOrder result, int errorCode, String errorMsg) {
                if (result != null) {
                    //有订单就显示数据
                    if (null == myOrderView || llMyOrder.getChildCount() == 0) {
                        //myOrderView为空或者没有子控件
                        if (getActivity() != null) {
                            myOrderView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_personal_my_order, container, false);
                            llMyOrder.addView(myOrderView);
                        }
                    }
                    initMyOrderData(result, myOrderView);
                } else {
                    //没有订单就Remove掉订单
                    llMyOrder.removeAllViews();
                }
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                ToastUtil.showToast(YHYBaseApplication.getInstance(), errorMessage);
            }
        });
    }

    /**
     * 处理订单业务
     *
     * @param detailOrder
     * @param view
     */
    private void initMyOrderData(Api_TRADEMANAGER_DetailOrder detailOrder, View view) {
//        ArrayList<OrderInfo> list = new ArrayList<>();
        if (view == null) return;

        long orderId = detailOrder.parentId;// 订单id
        String orderDescription = detailOrder.itemTitle;// 订单描述
        long orderTime = detailOrder.consumeStartTime;// 订单消费时间
        String cover = detailOrder.itemPic;// 订单图片

        long countdownTime = orderTime - System.currentTimeMillis();
        long sum = countdownTime / 1000 / 60;
        long hour = Math.abs(sum / 60);
        long minute = Math.abs(sum % 60);

        final OrderInfo i = new OrderInfo();
        i.setOrderId(orderId);
        i.setOverdue(countdownTime >= 0 ? false : true);
        i.setCountdown(hour + "小时" + minute + "分");
        i.setOrderName(orderDescription);
        i.setCover(cover);
//      list.add(i);

        /*Message msg = new Message();
        msg.what = MSG_GET_MY_ORDER_SUCCESS;
        msg.obj = list;
        handler.sendMessage(msg);*/

        TextView tvOrderName = view.findViewById(R.id.personal_order_tvOrderName);
        TextView tvCountdownStatus = view.findViewById(R.id.personal_order_tvCountdownStatus);
        TextView tvCountdown = view.findViewById(R.id.personal_order_tvCountdown);
        ImageView ivOrderImage = view.findViewById(R.id.personal_order_ivOrderImage);
        String orderName = i.getOrderName();
        if (orderName != null && !orderName.isEmpty()) {
            tvOrderName.setText(orderName);
        }
        if (i.isOverdue()) {
            tvCountdownStatus.setText("活动已经开始");
        } else {
            tvCountdownStatus.setText("离活动开始还有");
        }
        String countdown = i.getCountdown();
        if (countdown != null && !countdown.isEmpty()) {
            tvCountdown.setText(countdown);
        }
        if (i.getCover() != null && !i.getCover().isEmpty()) {
            String url = CommonUtil.getImageFullUrl(i.getCover());
//            Glide.with(getContext()).load(url).placeholder(R.mipmap.icon_default_150_150).into(ivOrderImage);
            ImageLoadManager.loadImage(url, R.mipmap.icon_default_150_150, ivOrderImage);

        }
        //跳转到订单详情
        view.setOnClickListener(view1 -> {
            String url = SPUtils.getURL_VENUE_ORDER_DETAIL(getContext()).replace(":orderId", String.valueOf(i.getOrderId()));
            WebParams params = new WebParams();
            params.setUrl(url);
            NavUtils.openBrowser(getContext(), params);
        });
    }

    /**
     * 获取用户钱包信息
     */
    private void GetUserWallet() {
        NetManager.getInstance(getActivity()).doGetUserWallet(userService.getLoginUserId(), new OnResponseListener<Api_RESOURCECENTER_UserWalletResult>() {
            @Override
            public void onComplete(boolean isOK, Api_RESOURCECENTER_UserWalletResult result, int errorCode, String errorMsg) {
                String rechargeBalance = result.rechargeBalance;// 充值余额
                int pointNumId = result.pointNumId;// 当前积分ID
                int pointNum = result.pointNum;// 当前积分
                int cardCoupons = result.cardCoupons;// 卡券

                if (pointNum > 0) {
                    tvPointNum.setText(String.valueOf(pointNum));
                    SPUtils.setScore(getActivity(), pointNum);
                } else {
                    tvPointNum.setText(String.valueOf(0));
                }
                if (cardCoupons > 0) {
                    tvCardCoupons.setText(String.valueOf(cardCoupons));
                } else {
                    tvCardCoupons.setText(String.valueOf(0));
                }
                if (rechargeBalance != null && !rechargeBalance.isEmpty()) {
                    tvRechargeBalance.setText(rechargeBalance);
                } else {
                    tvRechargeBalance.setText(String.valueOf(0));
                }
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                if (getContext() != null) {
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * 获取用户钱包信息
     */
    private void GetUserToolsInfo() {
        NetManager.getInstance(getActivity()).doGetUserToolsInfo(new OnResponseListener<Api_RESOURCECENTER_UserToolsResult>() {
            @Override
            public void onComplete(boolean isOK, Api_RESOURCECENTER_UserToolsResult result, int errorCode, String errorMsg) {
                List<Api_RESOURCECENTER_UserToolDto> tools = result.tools;// 用户常用工具
                List<Api_RESOURCECENTER_GameRankDto> gameRanks = result.gameRanks;// 用户赛事排名
                List<Api_RESOURCECENTER_ClubSimpleDto> clubs = result.clubs;// 用户俱乐部信息
                List<Api_RESOURCECENTER_PlaceSimpleDto> places = result.places;// 用户场馆经营
                List<Api_RESOURCECENTER_MyChareQrCodeDto> chargeQrCodes = result.chargeQrCodes;//交易二维码
                List<Api_RESOURCECENTER_TrainSimpleDto> trains = result.trains;//用户培训管理
                List<Api_RESOURCECENTER_TrainSimpleDto> coach = result.personTrains;//用户私教管理
                initCommonToolData(tools);
                initRankingListData(gameRanks);
                initClubData(clubs);
                initTrainData(trains);
                initCoachData(coach);
                initOperatingData(places);
                initQRCodeView(chargeQrCodes);
                if (null != getActivity()) ((BaseNewActivity) getActivity()).hideLoadingView();
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
//                ToastUtil.showToast(getContext(), errorMessage);
                if (null != getActivity()) ((BaseNewActivity) getActivity()).hideLoadingView();
            }
        });
    }

    /**
     * 未登录情况下获取获取工具
     */
    private void GetToolsInfoWithOutLogin() {
        NetManager.getInstance(getActivity()).doGetToolsInfoWithoutLogin(new OnResponseListener<Api_RESOURCECENTER_UserToolsResult>() {
            @Override
            public void onComplete(boolean isOK, Api_RESOURCECENTER_UserToolsResult result, int errorCode, String errorMsg) {
                List<Api_RESOURCECENTER_UserToolDto> tools = result.tools;// 用户常用工具
                initCommonToolData(tools);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
//                ToastUtil.showToast(getActivity(), errorMessage);
            }
        });
    }

    private void initCommonToolView(View root) {
        grdCommonTools = root.findViewById(R.id.fragment_personal_grdCommonTools);
        toolAdapter = new ToolAdapter(getContext(), toolsList);
        grdCommonTools.setAdapter(toolAdapter);
        grdCommonTools.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isLoggedIn()) {
                    CommonToolInfo i = toolsList.get(position);
                    Analysis.pushEvent(getActivity(), AnEvent.COMMON_TOOLS, new HashMap<String, String>().put("name", i.getTitle()));
                    if (i != null) {
                        String url = i.getUrl();
                        String tirle = i.getTitle();
                        if (url != null && !url.isEmpty()) {
                            NavUtils.startWebview(getActivity(), "", url, 0);
                        } else {
                            ToastUtil.showToast(getActivity(), "url is null");
                        }
                    }
                }
            }
        });
    }


    private void initCommonToolData(List<Api_RESOURCECENTER_UserToolDto> tools) {
        if (tools != null && !tools.isEmpty()) {
            ArrayList<CommonToolInfo> list = new ArrayList<CommonToolInfo>();
            for (Api_RESOURCECENTER_UserToolDto tool : tools) {
                long toolId = tool.toolId;
                String cover = tool.cover;
                String url = tool.url;
                String name = tool.name;
                CommonToolInfo i = new CommonToolInfo();
                i.setTitle(name);
                i.setCover(cover);
                i.setUrl(url);
                list.add(i);
            }
            toolsList.clear();
            toolsList.addAll(list);
            //额外增加更多图标
            CommonToolInfo more = new CommonToolInfo();
            more.setResId(R.mipmap.mine_icon_tool_more);
            more.setTitle("更多");
            String url = SPUtils.getShowAllTool(YHYBaseApplication.getInstance());
            if (url != null && !url.isEmpty()) {
                more.setUrl(url);
            }
            toolsList.add(more);
            toolAdapter.notifyDataSetChanged();
        }
    }

    private void initRankingListData(List<Api_RESOURCECENTER_GameRankDto> gameRanks) {
        if (gameRanks != null && !gameRanks.isEmpty()) {
            ArrayList<RankingListInfo> list = new ArrayList<>();

            if (gameRanks.size() >= 2) {
                Api_RESOURCECENTER_GameRankDto r1 = gameRanks.get(0);
                String title1 = r1.mainTitle;
                String logoUrl1 = r1.logoUrl;
                String gameTitle1 = r1.gameTitle;
                int currentRank1 = r1.currentRank;
                boolean isUpOrNot1 = r1.isUpOrNot;
                int incrOrDecNum1 = r1.incrOrDecNum;
                String url1 = r1.url;
                Api_RESOURCECENTER_GameRankDto r2 = gameRanks.get(1);
                String title2 = r2.mainTitle;
                String logoUr2 = r2.logoUrl;
                String gameTitle2 = r2.gameTitle;
                int currentRank2 = r2.currentRank;
                boolean isUpOrNot2 = r2.isUpOrNot;
                int incrOrDecNum2 = r2.incrOrDecNum;
                String url2 = r2.url;

                RankingListInfo i = new RankingListInfo();
                i.setRankingUrl(logoUrl1);
                i.setRankingName(title1);
                i.setLeftMatchName(gameTitle1);
                i.setLeftMatchRanking(currentRank1);
                i.setLeftRankChange(isUpOrNot1 ? incrOrDecNum1 : -incrOrDecNum1);
                i.setRightMatchName(gameTitle2);
                i.setRightMatchRanking(currentRank2);
                i.setRightRankChange(isUpOrNot2 ? incrOrDecNum2 : -incrOrDecNum2);
                i.setAllRankingListURL(url1);
                list.add(i);

            }
            Message msg = new Message();
            msg.what = MSG_GET_RANKING_LIST_SUCCESS;
            msg.obj = list;
            handler.sendMessage(msg);
        }
    }

    private void initRankingListView(final RankingListInfo i) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_personal_ranking_list, container, false);
        ImageView simpleDraweeView = view.findViewById(R.id.personal_ranking_simpleDraweeView);
        TextView tvRankingName = view.findViewById(R.id.personal_ranking_tvRankingName);
        TextView tvLeftMatchName = view.findViewById(R.id.personal_ranking_tvLeftMatchName);
        TextView tvLeftMatchRanking = view.findViewById(R.id.personal_ranking_tvLeftMatchRanking);
        TextView tvLeftRankChange = view.findViewById(R.id.personal_ranking_tvLeftRankChange);
        TextView tvRightMatchName = view.findViewById(R.id.personal_ranking_tvRightMatchName);
        TextView tvRightMatchRanking = view.findViewById(R.id.personal_ranking_tvRightMatchRanking);
        TextView tvRightRankChange = view.findViewById(R.id.personal_ranking_tvRightRankChange);
        TextView btnAllRankingList = view.findViewById(R.id.personal_ranking_btnAllRankingList);

        btnAllRankingList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Analysis.pushEvent(getActivity(), AnEvent.CARD_TYPE, new HashMap<String, String>().put("name", i.getRankingName()));
                String url = i.getAllRankingListURL();
                if (url != null && !url.isEmpty()) {
                    NavUtils.startWebview(getActivity(), "", url, 0);
                }
            }
        });

        String rankingName = i.getRankingName();
        if (rankingName != null && !rankingName.isEmpty()) {
            tvRankingName.setText(rankingName);
        }
        String rankingUrl = i.getRankingUrl();
        if (rankingUrl != null && !rankingUrl.isEmpty()) {
//            Uri uri = Uri.parse(ImageUtils.getImageFullUrl(rankingUrl));
//            simpleDraweeView.setImageURI(uri);
            ImageLoadManager.loadCircleImage(CommonUtil.getImageFullUrl(rankingUrl), simpleDraweeView);
        }
        String leftMatchName = i.getLeftMatchName();
        if (leftMatchName != null && !leftMatchName.isEmpty()) {
            tvLeftMatchName.setText(leftMatchName);
        }
        int leftMatchRanking = i.getLeftMatchRanking();
        int leftRankChange = i.getLeftRankChange();
        tvLeftMatchRanking.setText(String.valueOf(leftMatchRanking));

        if (leftRankChange < 0) {
            tvLeftRankChange.setText("下降" + String.valueOf(Math.abs(leftRankChange)) + "名");
            tvLeftMatchRanking.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.icon_down), null);
        } else if (leftRankChange > 0) {
            tvLeftRankChange.setText("上升" + String.valueOf(leftRankChange) + "名");
            tvLeftMatchRanking.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.icon_up), null);
        } else {
            tvLeftRankChange.setText("排名不变");
            tvLeftMatchRanking.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }

        String rightMatchName = i.getRightMatchName();
        if (rightMatchName != null && !rightMatchName.isEmpty()) {
            tvRightMatchName.setText(rightMatchName);
        }
        int rightMatchRanking = i.getRightMatchRanking();
        int rightRankChange = i.getRightRankChange();
        tvRightMatchRanking.setText(String.valueOf(rightMatchRanking));
        if (rightRankChange < 0) {
            tvRightRankChange.setText("下降" + String.valueOf(Math.abs(rightRankChange)) + "名");
            tvRightMatchRanking.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.icon_down), null);
        } else if (rightRankChange > 0) {
            tvRightRankChange.setText("上升" + String.valueOf(rightRankChange) + "名");
            tvRightMatchRanking.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.icon_up), null);
        } else {
            tvRightRankChange.setText("排名不变");
            tvRightMatchRanking.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }
        llRankingList.removeAllViews();
        llRankingList.addView(view);
    }

    private void initClubView(final ClubInfo i) {
        if (getActivity() == null) {
            return;
        }
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_personal_club_item, container, false);
        ImageView simpleDraweeView = view.findViewById(R.id.personal_club_simpleDraweeView);
        TextView tvClubName = view.findViewById(R.id.personal_club_tvClubName);
        TextView tvExerciseTime = view.findViewById(R.id.personal_club_tvExerciseTime);
        TextView tvExerciseTimeMonth = view.findViewById(R.id.personal_club_tvExerciseTimeMonth);
        TextView tvClubFund = view.findViewById(R.id.personal_club_tvClubFund);
        TextView btnClubManagement = view.findViewById(R.id.personal_club_btnClubManagement);
        btnClubManagement.setOnClickListener(v -> {//俱乐部管理
            Analysis.pushEvent(getActivity(), AnEvent.CARD_TYPE, new HashMap<String, String>().put("name", i.getClubName()));
            String url = i.getMoreClubURL();
            if (url != null && !url.isEmpty()) {
                NavUtils.startWebview(getActivity(), "", url, 0);
            }
        });

        String clubUrl = i.getClubUrl();
        if (clubUrl != null && !clubUrl.isEmpty()) {
            ImageLoadManager.loadCircleImage(clubUrl, simpleDraweeView);
        }
        String clubName = i.getClubName();
        if (clubName != null && !clubName.isEmpty()) {
            tvClubName.setText(clubName);
        }
        int exerciseTime = i.getExerciseTime();
        tvExerciseTime.setText(String.valueOf(exerciseTime));

        int month = i.getExerciseTimeMonth();
        //tvExerciseTimeMonth.setText(String.valueOf(month) + "月活动总场次数");

        String clubFund = i.getClubFund();
        tvClubFund.setText(clubFund);
        llClub.removeAllViews();
        llClub.addView(view);
    }

    private void initClubData(List<Api_RESOURCECENTER_ClubSimpleDto> clubs) {
        if (clubs != null && !clubs.isEmpty()) {
            ArrayList<ClubInfo> list = new ArrayList<ClubInfo>();
            Api_RESOURCECENTER_ClubSimpleDto club = clubs.get(0);
            String clubName = club.clubName;
            long clubId = club.clubId;
            String logoUrl = club.logoUrl;
            int actNum = club.actNum;
            int actMonth = club.actMonth;
            String clubAmount = club.clubAmount;
            String url = club.url;
            ClubInfo i = new ClubInfo();
            i.setClubName(clubName);
            i.setClubFund(clubAmount);
            i.setExerciseTime(actNum);
            i.setExerciseTimeMonth(actMonth);
            i.setClubUrl(logoUrl);
            i.setMoreClubURL(url);
            list.add(i);
            Message msg = new Message();
            msg.what = MSG_GET_CLUB_SUCCESS;
            msg.obj = list;
            handler.sendMessage(msg);
        }
    }

    private void initTrainData(List<Api_RESOURCECENTER_TrainSimpleDto> trainSimpleDtos) {
        if (trainSimpleDtos != null && !trainSimpleDtos.isEmpty()) {
            Message msg = new Message();
            msg.what = MSG_GET_TRAIN_SUCCESS;
            msg.obj = trainSimpleDtos.get(0);
            handler.sendMessage(msg);
        }
    }

    private void initTrainsView(final Api_RESOURCECENTER_TrainSimpleDto trainSimpleDto) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_personal_train_item, container, false);
        ImageView simpleDraweeView = view.findViewById(R.id.personal_train_simpleDraweeView);//图标
        TextView tvTrainName = view.findViewById(R.id.personal_train_tvTrainName);//机构名称
        TextView tvStudentCount = view.findViewById(R.id.personal_train_student_count);//学员数
        TextView tvTrainIncome = view.findViewById(R.id.personal_train_tvIncome);//收入
        TextView btnTrainManagement = view.findViewById(R.id.personal_train_btnTrainManagement);//管理
        btnTrainManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//培训机构管理
                Analysis.pushEvent(getActivity(), AnEvent.CARD_TYPE, new HashMap<String, String>().put("name", trainSimpleDto.trainName));
                String url = trainSimpleDto.url;
                if (url != null && !url.isEmpty()) {
                    NavUtils.startWebview(getActivity(), "", url, 0);
                }
            }
        });

        String trainUrl = trainSimpleDto.logoUrl;
        if (trainUrl != null && !trainUrl.isEmpty()) {
            ImageLoadManager.loadCircleImage(CommonUtil.getImageFullUrl(trainUrl), simpleDraweeView);
        }
        String trainName = trainSimpleDto.trainName;
        if (trainName != null && !trainName.isEmpty()) {
            tvTrainName.setText(trainName);
        }
        long studentCount = trainSimpleDto.studentCount;
        tvStudentCount.setText(String.valueOf(studentCount));
        long amount = trainSimpleDto.amount;//分
        String income = amount / 100 + "." + amount % 100 / 10 + amount % 100 % 10;
        tvTrainIncome.setText(income);
        llTrain.removeAllViews();
        llTrain.addView(view);

    }

    private void initCoachData(List<Api_RESOURCECENTER_TrainSimpleDto> coachSimpleDtos) {
        if (coachSimpleDtos != null && !coachSimpleDtos.isEmpty()) {
            Message msg = new Message();
            msg.what = MSG_GET_COACH_SUCCESS;
            msg.obj = coachSimpleDtos.get(0);
            handler.sendMessage(msg);
        }
    }


    private void initCoachView(final Api_RESOURCECENTER_TrainSimpleDto trainSimpleDto) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_personal_coach_item, container, false);
        ImageView simpleDraweeView = view.findViewById(R.id.personal_train_simpleDraweeView);//图标
        TextView tvTrainName = view.findViewById(R.id.personal_train_tvTrainName);//机构名称
        TextView tvStudentCount = view.findViewById(R.id.personal_train_student_count);//学员数
        TextView tvTrainIncome = view.findViewById(R.id.personal_train_tvIncome);//收入
        TextView btnTrainManagement = view.findViewById(R.id.personal_train_btnTrainManagement);//管理
        btnTrainManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//私教管理
                Analysis.pushEvent(getActivity(), AnEvent.CARD_TYPE, new HashMap<String, String>().put("name", trainSimpleDto.trainName));
                String url = trainSimpleDto.url;
                if (url != null && !url.isEmpty()) {
                    NavUtils.startWebview(getActivity(), "", url, 0);
                }
            }
        });

        String trainUrl = trainSimpleDto.logoUrl;
        if (trainUrl != null && !trainUrl.isEmpty()) {
            ImageLoadManager.loadCircleImage(CommonUtil.getImageFullUrl(trainUrl), simpleDraweeView);
        }
        String trainName = trainSimpleDto.trainName;
        if (trainName != null && !trainName.isEmpty()) {
            tvTrainName.setText(trainName);
        }
        long studentCount = trainSimpleDto.studentCount;
        tvStudentCount.setText(String.valueOf(studentCount));
        long amount = trainSimpleDto.amount;//分
        String income = amount / 100 + "." + amount % 100 / 10 + amount % 100 % 10;
        tvTrainIncome.setText(income);
        llCoach.removeAllViews();
        llCoach.addView(view);

    }


    private void initOperatingData(List<Api_RESOURCECENTER_PlaceSimpleDto> places) {
        if (places != null && !places.isEmpty()) {
            ArrayList<OperatingDataInfo> list = new ArrayList<OperatingDataInfo>();
            Api_RESOURCECENTER_PlaceSimpleDto place = places.get(0);

            String placeName = place.placeName;
            long placeId = place.placeId;
            String logoUrl = place.logoUrl;
            boolean incomeUpOrNot = place.incomeUpOrNot;
            String incomePercent = place.incomePercent;
            double weekAmount = place.weekAmount;
            String weekDigest = place.weekDigest;
            boolean digestUpOrNot = place.digestUpOrNot;
            String digestPercent = place.digestPercent;
            String url = place.url;

            int digestibilityChange = Integer.parseInt(digestPercent.replace("%", ""));
            int incomeChange = 0;
            if (incomePercent.contains("%")) {
                try {
                    incomeChange = Integer.parseInt(incomePercent.replace("+", "")
                            .replace("-", "").replace("%", ""));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                incomeChange = 0;
            }

            OperatingDataInfo i = new OperatingDataInfo();
            i.setDigestibility(weekDigest);
            i.setDigestibilityChange(incomeUpOrNot ? incomeChange : -incomeChange);
            i.setIncome(weekAmount);
            i.setIncomeChange(digestUpOrNot ? digestibilityChange : -digestibilityChange);
            i.setOperatingName(placeName);
            i.setOperatingUrl(logoUrl);
            i.setUrl(url);
            list.add(i);

            Message msg = new Message();
            msg.what = MSG_GET_OPERATING_DATA_SUCCESS;
            msg.obj = list;
            handler.sendMessage(msg);
        }

    }

    private void initOperatingDataView(final OperatingDataInfo i) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_personal_operating_data, container, false);
        ImageView simpleDraweeView = (ImageView) view.findViewById(R.id.personal_operating_data_simpleDraweeView);
        TextView tvOperatingName = (TextView) view.findViewById(R.id.personal_personal_operating_data_tvOperatingName);
        TextView tvIncome = (TextView) view.findViewById(R.id.personal_personal_operating_data_tvIncome);
        TextView tvIncomeChange = (TextView) view.findViewById(R.id.personal_personal_operating_data_tvIncomeChange);
        TextView tvDigestibility = (TextView) view.findViewById(R.id.personal_personal_operating_data_tvDigestibility);
        TextView tvDigestibilityChange = (TextView) view.findViewById(R.id.personal_personal_operating_data_tvDigestibilityChange);
        TextView btnMoreOperatingData = (TextView) view.findViewById(R.id.personal_personal_operating_data_btnMoreOperatingData);
        btnMoreOperatingData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Analysis.pushEvent(getActivity(), AnEvent.CARD_TYPE, new HashMap<String, String>().put("name", i.getOperatingName()));
                String url = i.getUrl();
                if (url != null && !url.isEmpty()) {
                    NavUtils.startWebview(getActivity(), "", url, 0);
                }
            }
        });

        String operatingName = i.getOperatingName();
        if (operatingName != null && !operatingName.isEmpty()) {
            tvOperatingName.setText(operatingName);
        }
        String operatingUrl = i.getOperatingUrl();
        if (operatingUrl != null && !operatingUrl.isEmpty()) {
//            Uri uri = Uri.parse(CommonUtil.getImageFullUrl(operatingUrl));
//            simpleDraweeView.setImageURI(uri);
            ImageLoadManager.loadCircleImage(CommonUtil.getImageFullUrl(operatingUrl), simpleDraweeView);
        }
        double income = i.getIncome();
        tvIncome.setText(String.valueOf(income));
        int incomeChange = i.getIncomeChange();
        if (incomeChange > 0) {
            tvIncome.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.icon_up), null);
            tvIncomeChange.setText("本期比上期+" + incomeChange + "%");
        } else if (incomeChange < 0) {
            tvIncome.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.icon_down), null);
            tvIncomeChange.setText("本期比上期" + incomeChange + "%");
        } else {
            tvIncome.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            tvIncomeChange.setText("本期比上期没有变化");
        }

        String digestibility = i.getDigestibility();
        tvDigestibility.setText(digestibility);
        int digestibilityChange = i.getDigestibilityChange();
        if (digestibilityChange > 0) {
            tvDigestibility.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.icon_up), null);
            tvDigestibilityChange.setText("本期比上期+" + digestibilityChange + "%");
        } else if (digestibilityChange < 0) {
            tvDigestibility.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.icon_down), null);
            tvDigestibilityChange.setText("本期比上期" + digestibilityChange + "%");
        } else {
            tvDigestibility.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            tvDigestibilityChange.setText("本期比上期没有变化");
        }
        llOperatingData.removeAllViews();
        llOperatingData.addView(view);
    }

    private void initQRCodeView(List<Api_RESOURCECENTER_MyChareQrCodeDto> chargeQrCodes) {
        //string	name     名称
        //string	logoUrl	 logo地址
        //long	    userId	 用户id
        //string	url	     跳转url
        if (chargeQrCodes != null && chargeQrCodes.size() > 0) {
            final Api_RESOURCECENTER_MyChareQrCodeDto dao = chargeQrCodes.get(0);
            String s = SPUtils.getRecharge(getContext()) + File.separator + dao.userId;//生成的二维码
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_person_qrcode, container, false);
            ImageView simpleDraweeView = (ImageView) view.findViewById(R.id.personal_qrcode_simpleDraweeView);
            if (null != dao.logoUrl) {
//                Uri uri = Uri.parse(ImageUtils.getImageFullUrl(dao.logoUrl));
//                simpleDraweeView.setImageURI(uri);
                ImageLoadManager.loadCircleImage(CommonUtil.getImageFullUrl(dao.logoUrl), simpleDraweeView);
            }
            ImageView imageView = (ImageView) view.findViewById(R.id.iv_qr_code);//二维码
            imageView.setImageBitmap(QRCodeUtil.GenorateImage(s));
            TextView tv_detail = (TextView) view.findViewById(R.id.tv_detail);
            tv_detail.setOnClickListener(new View.OnClickListener() {//查看详情
                @Override
                public void onClick(View view) {
                    Analysis.pushEvent(getActivity(), AnEvent.CARD_TYPE, new HashMap<String, String>().put("name", "充值推荐二维码"));
                    WebParams params = new WebParams();
                    params.setUrl(dao.url);
                    NavUtils.openBrowser(getContext(), params);
                }
            });
            llQRCode.removeAllViews();
            llQRCode.addView(view);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        YhyRouter.getInstance().inject(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((resultCode == RESULT_OK && requestCode == IntentConstants.LOGIN_RESULT) || (resultCode == RESULT_OK && requestCode == AppSettingsActivity.SETTING_RESULT)) {
            initLoginView();
        }
        Log.i("shenwenjie", "resultCode=" + resultCode);
        Log.i("shenwenjie", "requestCode=" + requestCode);
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

    public void updateIMMessageCount(int messageNum) {
        if (messageNum == 0) {
            tvNewMsgCount.setVisibility(View.INVISIBLE);
        } else {
            tvNewMsgCount.setVisibility(View.VISIBLE);
            tvNewMsgCount.setText("" + messageNum);
        }
    }

    /**
     * 保存头像png
     *
     * @param bitmap
     * @throws IOException
     */
    private void saveFile(Bitmap bitmap) throws IOException {
        File file = new File(DirConstants.DIR_PIC_ORIGIN);
        if (!file.exists()) {
            file.mkdir();
        }
        File save = new File(DirConstants.DIR_PIC_ORIGIN + DirConstants.USER_HEAD_ICON);
        if (save.exists()) save.delete();
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(save));
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        bos.flush();
        bos.close();
    }

    public void onEvent(EvBusWhenSelectFragment event) {
        if (event.index == 3) {
            scroll_parent.scrollTo(0, 0);
            refreshLayout.autoRefresh();
        }
    }

    private void initUserExtInfo(User user) {
        if (user == null || !userService.isLoginUser(user.getUserId())) {
            return;
        }
        long followedCount = user.getUserFriendShipInfo().getFollowedCount();//关注数
        long fansCount = user.getUserFriendShipInfo().getFansCount();//粉丝数
        long ugcCount = user.getUserFriendShipInfo().getUgcCount();//相册数

        if (followedCount >= 0) {
            tvFollowedCount.setText(String.valueOf(followedCount));
        }
        if (fansCount >= 0) {
            tvFansCount.setText(String.valueOf(fansCount));
        }
        if (ugcCount >= 0) {
            tvPicCount.setText(String.valueOf(ugcCount));
        }

        UserLevelInfo memberLevel = user.getUserLevelInfo();
        if (memberLevel != null) {
            String levelName = memberLevel.getLevelName();//会员等级名称
            long memberLevelPoint = memberLevel.getMemberLevelPoint();//会员成长
            if (levelName != null && !levelName.isEmpty()) {
                tvGrade.setText(levelName);
            }

            if (memberLevelPoint >= 0) {
                tvExp.setText("成长值:" + String.valueOf(memberLevelPoint));
            }

            if (levelName.contains("钻石")) {//钻石会员
                level_icon.setBackgroundResource(R.mipmap.diamond_level);
            } else if (levelName.contains("黄金")) {//黄金会员
                level_icon.setBackgroundResource(R.mipmap.gold_level);
            } else if (levelName.contains("白银")) {//白银会员
                level_icon.setBackgroundResource(R.mipmap.silver_level);
            } else {//大众会员
                level_icon.setBackgroundResource(R.mipmap.normal_level);
            }
        }
    }

    public void onEvent(EvBusGetUserInfo event) {
        initUserInfo(event.getUser());
    }

    public void onEvent(EvBusGetUserExInfo event) {
        initUserExtInfo(event.getUser());
    }


    @Override
    public void onResume() {
        super.onResume();
        initLoginView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
