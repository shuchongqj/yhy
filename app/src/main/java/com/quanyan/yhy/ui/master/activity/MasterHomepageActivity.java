package com.quanyan.yhy.ui.master.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.harwkin.nb.camera.ImageUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.newyhy.fragment.UgcFragment;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.baseenum.IActionTitleBar;
import com.quanyan.base.util.LocalUtils;
import com.quanyan.base.util.ScreenSize;
import com.quanyan.base.util.StringUtil;
import com.quanyan.base.view.BaseTransparentNavView;
import com.quanyan.base.view.customview.pulltorefresh.PullToRefreshBase;
import com.quanyan.base.view.customview.pulltorefresh.PullToRefreshSticyView;
import com.quanyan.base.view.customview.stickyview.StickyNavLayout;
import com.quanyan.base.view.customview.tabscrollindicator.SlidingTabLayout;
import com.quanyan.base.yminterface.ErrorViewClick;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.AnalyDataValue;
import com.quanyan.yhy.common.ErrorCode;
import com.quanyan.yhy.eventbus.EvBusUGCInfoAttention;
import com.quanyan.yhy.libanalysis.AnEvent;
import com.quanyan.yhy.libanalysis.Analysis;

import com.quanyan.yhy.ui.adapter.DetailViewPagerAdapter;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.base.utils.TCEventHelper;
import com.quanyan.yhy.ui.comment.ValueCommentType;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.login.LoginActivity;
import com.quanyan.yhy.ui.master.controller.MasterController;
import com.quanyan.yhy.ui.master.fragment.TaEvaluateFragment;
import com.quanyan.yhy.ui.master.fragment.TaMasterfragment;
import com.quanyan.yhy.ui.views.CancelFollowPopView;
import com.yhy.common.beans.net.model.master.TalentInfo;
import com.yhy.common.constants.IntentConstants;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.CommonUtil;
import com.yhy.common.utils.SPUtils;
import com.yhy.imageloader.ImageLoadManager;
import com.yhy.service.IUserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * 达人店铺
 * Created by wujm on 2016-3-3.
 */
public class
MasterHomepageActivity extends BaseActivity implements View.OnClickListener, StickyNavLayout.StickyLayoutScrollY, SlidingTabLayout.TabClick, PullToRefreshBase.OnRefreshListener<StickyNavLayout> {
    public static final int RESULT_CLUBDETAIL = 2;
    TalentInfo TravelKaBeab = null;
    @ViewInject(R.id.id_stickynavlayout_indicator)
    private SlidingTabLayout mSlidingTabLayout;
    @ViewInject(R.id.id_stickynavlayout_viewpager)
    private ViewPager mContentViewPager;
    private DetailViewPagerAdapter mAdapter;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private long mTalentId = -1;
    @ViewInject(R.id.base_sticky_refresh_layout)
    private PullToRefreshSticyView mStickyNavLayout;
    @ViewInject(R.id.trasparent_topbar_layout)
    private RelativeLayout mTopBarlayout;
    @ViewInject(R.id.trasparent_topbar_left)
    private ImageView mTopLeft;
    @ViewInject(R.id.trasparent_topbar_right)
    private ImageView mTopRightImg;
    @ViewInject(R.id.trasparent_topbar_title)
    private TextView mTopTitle;

    @ViewInject(R.id.master_personal_topbg)
    private ImageView topbgImgPagerView;
    @ViewInject(R.id.master_bannerview_title)
    private TextView topbgTitle;
    @ViewInject(R.id.master_personal_head)
    private ImageView mHeadView;
    //    @ViewInject(R.id.master_personal_head2)
//    private ImageView largeVImg;
    @ViewInject(R.id.master_top_name)
    private TextView nameTv;
//    @ViewInject(R.id.master_top_starsnum)
//    private TextView positionTv;
//    @ViewInject(R.id.master_top_server)
//    private TextView serverTv;

    @ViewInject(R.id.but_master_tel)
    private LinearLayout butMasterTel;

    @ViewInject(R.id.but_master_chat)
    private LinearLayout butMasterChat;

    @ViewInject(R.id.base_sticky_bottom_layout2)
    private LinearLayout base_sticky_bottom_layout2;
    @ViewInject(R.id.tv_master_follow)
    private TextView mTvfollow;

    //粉丝数
    @ViewInject(R.id.tv_fans_count)
    private TextView mTvFunsNum;
    //关注数
    @ViewInject(R.id.tv_attention_count)
    private TextView mTvAttentionNum;
    //关注数的父布局
    @ViewInject(R.id.ll_attention_count)
    private LinearLayout mLLAttentionNum;
    //粉丝数的父布局
    @ViewInject(R.id.ll_funs_count)
    private LinearLayout mLLFunsNum;

    private static int USERINFO_REQCODE = 101;

    private boolean fromCousult;          // 是否是咨询跳转过来的 

    @Autowired
    IUserService userService;

    public IUserService getUserService() {
        return userService;
    }
    /**
     * 达人主页跳转
     *
     * @param context 上下文对象
     * @param userId  达人ID
     */
    public static void gotoMasterHomepage(Activity context, long userId) {
        Intent intent = new Intent(context, MasterHomepageActivity.class);
        intent.putExtra(SPUtils.EXTRA_ID, userId);
        context.startActivityForResult(intent, RESULT_CLUBDETAIL);
    }


    public static void gotoMasterHomepage(Context context, long userId) {
        Intent intent = new Intent(context, MasterHomepageActivity.class);
        intent.putExtra(SPUtils.EXTRA_ID, userId);
        context.startActivity(intent);
    }

    public static void gotoMasterHomepage(Fragment context, long userId) {
        Intent intent = new Intent(context.getActivity(), MasterHomepageActivity.class);
        intent.putExtra(SPUtils.EXTRA_ID, userId);
        context.startActivityForResult(intent, RESULT_CLUBDETAIL);
    }

    public static void gotoMasterHomepage(Context context, long userId, boolean fromCousult) {
        Intent intent = new Intent(context, MasterHomepageActivity.class);
        intent.putExtra(SPUtils.EXTRA_ID, userId);
        intent.putExtra("fromCousult", fromCousult);
        context.startActivity(intent);
    }

    MasterController mController;
    private LinearLayout mBottomLayout;
    private boolean isMeUser = false;

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.activity_masterhomepage, null);
    }

    private BaseTransparentNavView mBaseTransparentNavView;

    @Override
    public View onLoadNavView() {
        mBaseTransparentNavView = new BaseTransparentNavView(this);
        mBaseTransparentNavView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        return mBaseTransparentNavView;
    }

    @Override
    public boolean isTopCover() {
        return true;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        Analysis.pushEvent(this, AnEvent.INFORMATION_FLOW1_DYNAMIC_DETAILS);
        EventBus.getDefault().register(this);
        setTitleBarBackground(getResources().getColor(R.color.transparent));
        ViewUtils.inject(this);
        mBaseTransparentNavView.showBottomDivid(false);
        mStickyNavLayout.getRefreshableView().setFixedHeight(false);
        mStickyNavLayout.setMode(PullToRefreshBase.Mode.DISABLED);
//        mStickyNavLayout.setTopHeight(ScreenSize.dp2px(getApplicationContext(), 25));
        mStickyNavLayout.requestLayout();

        mStickyNavLayout.setOnRefreshListener(this);
        mStickyNavLayout.getRefreshableView().setScrollListener(this);
        mStickyNavLayout.getRefreshableView().setTopHeight(ScreenSize.getStatusBarHeight(MasterHomepageActivity.this));
        mTalentId = getIntent().getLongExtra(SPUtils.EXTRA_ID, -1);
        fromCousult = getIntent().getBooleanExtra("fromCousult", false);

        mController = new MasterController(this, mHandler);
        isLoading = true;
        resumeFresh();
    }

    private void resumeFresh() {
        if (userService.getLoginUserId() == mTalentId) {
            isMeUser = true;
//            mTvfollow.setClickable(true);
            mTvfollow.setVisibility(View.VISIBLE);
            mTvfollow.setText(getString(R.string.label_master_personaldata));
        }
        startLoadData(isLoading);
        refreshBottomStatus();
    }

    @Override
    protected void onResume() {

        super.onResume();

    }

    /**
     * 刷新电话、客服UI
     */
    private void refreshBottomStatus() {
        if (userService.getLoginUserId() == mTalentId) {
            base_sticky_bottom_layout2.setVisibility(View.GONE);
        } else {
            base_sticky_bottom_layout2.setVisibility(View.VISIBLE);
        }
    }

    boolean isLoading = true;

    private void startLoadData(boolean isLoading) {
        this.isLoading = isLoading;
        if (isLoading) {
            showLoadingView(getString(R.string.loading_text));
        }

        if (-1 != mTalentId) {
            refreshBottomStatus();
            mController.getMasterDetail(MasterHomepageActivity.this, mTalentId);
        } else {
            ToastUtil.showToast(this, getString(R.string.error_params));
        }
    }

    TaMasterfragment mTaMasterfragment;

    private void initView(TalentInfo value) {
        mTaMasterfragment = TaMasterfragment.createTaMasrerfragment(mTalentId, value);

        //mFragments.add(DiscChildLiveFragment.getInstance(value.userId, -1, MasterHomepageActivity.class.getSimpleName()));
        mFragments.add(UgcFragment.newInstance(0,value.userId));
        mFragments.add(mTaMasterfragment);
        mFragments.add(TaEvaluateFragment.createTaEvaluateFragment(value.userId, value.orgId));
        //达人主页tab
        ArrayList<String> titles = new ArrayList<>();
        String[] strings = getResources().getStringArray(R.array.master_tabs);
        for (int i = 0; i < strings.length; i++) {
            titles.add(strings[i]);
        }
        //mContentViewPager.removeAllViews();
        mAdapter = new DetailViewPagerAdapter(getSupportFragmentManager(), mFragments, titles);
        mContentViewPager.setAdapter(mAdapter);
        mContentViewPager.setCurrentItem(fromCousult ? mFragments.size() - 1 : 0);
        mContentViewPager.setOffscreenPageLimit(3);

        //tab属性设置
        mSlidingTabLayout.setDistributeEvenly(true);
        mSlidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.ac_title_bg_color));
        mSlidingTabLayout.setOnTabClickListener(this, true);
        mSlidingTabLayout.setViewPager(mContentViewPager);

        mHeadView.setOnClickListener(this);
        //底部按钮事件
        mLLAttentionNum.setOnClickListener(this);
        mLLFunsNum.setOnClickListener(this);
        butMasterTel.setOnClickListener(this);
        butMasterChat.setOnClickListener(this);

        mTopTitle.setVisibility(View.GONE);
        mTopLeft.setImageResource(R.mipmap.scenic_arrow_back_white);

        mTopRightImg.setOnClickListener(this);
        mTopRightImg.setVisibility(View.GONE);
        mTopBarlayout.setBackgroundColor(getResources().getColor(R.color.transparent_all));
        setCalculationHeight();

        mContentViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (!mStickyNavLayout.getRefreshableView().isTopHidden) {
                    ViewGroup view = (ViewGroup) mFragments.get(position).getView();
                    if (view != null){
                        view = view.findViewById(R.id.id_stickynavlayout_innerscrollview);
                        mStickyNavLayout.getRefreshableView().resetScroll(view);
                    }

                }


                if (fromCousult) {
                    // 友盟埋点
                    switch (position) {
                        case 0:
                            //事件统计
                            Analysis.pushEvent(MasterHomepageActivity.this, AnEvent.CONSULTANT_HOMEPAGE_STATUS);
                            break;
                        case 1:
                            //事件统计
                            Analysis.pushEvent(MasterHomepageActivity.this, AnEvent.CONSULTANT_HOMEPAGE1);
                            break;
                        case 2:
                            //事件统计
                            Analysis.pushEvent(MasterHomepageActivity.this, AnEvent.CONSULTANT_HOMEPAGE_SERVICE);
                            break;
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick({R.id.trasparent_topbar_left_layout, R.id.trasparent_topbar_right, R.id.club_detail_post})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.trasparent_topbar_left_layout:
                onBackPressed();
                break;
            case R.id.but_master_tel:
                Map<String, String> map1 = new HashMap<>();
                map1.put(AnalyDataValue.KEY_TYPE, AnalyDataValue.MASTER_CHAT_TYPE_TEL);
                map1.put(AnalyDataValue.KEY_MID, String.valueOf(mTalentId));
                TCEventHelper.onEvent(MasterHomepageActivity.this, AnalyDataValue.MASTERSHOP_COMMUNICATE, map1);

                if (TravelKaBeab != null && !StringUtil.isEmpty(TravelKaBeab.telNum)) {
                    LocalUtils.call(MasterHomepageActivity.this, TravelKaBeab.telNum);
                } else {
                    ToastUtil.showToast(MasterHomepageActivity.this, getString(R.string.no_telephone_number));
                }
                break;
            case R.id.but_master_chat:
                Map<String, String> map2 = new HashMap<>();
                map2.put(AnalyDataValue.KEY_TYPE, AnalyDataValue.MASTER_CHAT_TYPE_CHAT);
                map2.put(AnalyDataValue.KEY_MID, String.valueOf(mTalentId));
                TCEventHelper.onEvent(MasterHomepageActivity.this, AnalyDataValue.MASTERSHOP_COMMUNICATE, map2);

                NavUtils.gotoMessageActivity(MasterHomepageActivity.this, Integer.parseInt(String.valueOf(TravelKaBeab.userId)));
                break;
            case R.id.tv_master_follow:
                if (!getUserService().isLogin()) {
                    NavUtils.gotoLoginActivity(MasterHomepageActivity.this);
                    return;
                }
                tcEvent(3);

                if (userService.getLoginUserId() == mTalentId) {
                    //NavUtils.gotoUserInfoEditActivity(MasterHomepageActivity.this);
                    NavUtils.gotoUserInfoEditActivity(MasterHomepageActivity.this, ValueCommentType.PIC_AND_TEXT_EXPERTHOME, USERINFO_REQCODE);
                } else {
                    long id = TravelKaBeab.userId;
                    if (isFollow) {
//                        showCacheDialog();
                    } else {
                        // 事件统计
                        if (fromCousult) {
                            Analysis.pushEvent(this, AnEvent.CONSULTANT_HOMEPAGE_CONCERN);
                        }else {
                            Analysis.pushEvent(this, AnEvent.INFORMATION_FLOW_ATTENTION);
                        }
                        mController.doAttention(MasterHomepageActivity.this, TravelKaBeab.userId);
                    }
                }
                break;
            case R.id.trasparent_topbar_right:

                break;
            case R.id.ll_funs_count://粉丝
                tcEvent(1);
                if (userService.getLoginUserId() == TravelKaBeab.userId) {
                    NavUtils.gotoMyFansListActivity(MasterHomepageActivity.this, userService.getLoginUserId());
                } else {
                    NavUtils.gotoOtherFansListActivity(MasterHomepageActivity.this, (TravelKaBeab.userId));
                }
                break;

            case R.id.ll_attention_count://关注

                tcEvent(2);

                if (userService.getLoginUserId() == TravelKaBeab.userId) {
                    NavUtils.gotoMyAttentionListActivity(this, userService.getLoginUserId());
                } else {
                    NavUtils.gotoOhterAttentionListActivity(MasterHomepageActivity.this, TravelKaBeab.userId);
                }
                break;
            case R.id.master_personal_head://查看头像大图
                if (TravelKaBeab != null && !StringUtil.isEmpty(TravelKaBeab.avatar)) {
                    ArrayList<String> tmpList = new ArrayList<>();
                    tmpList.add(ImageUtils.getImageFullUrl(TravelKaBeab.avatar));
                    NavUtils.gotoLookBigImage(this, tmpList, 0);
                }
                break;
        }
    }

    private void tcEvent(int i) {
        Map<String, String> map = new HashMap();
        map.put(AnalyDataValue.KEY_MID, TravelKaBeab.userId + "");
        map.put(AnalyDataValue.KEY_MNAME, TravelKaBeab.nickName);
        switch (i) {
            case 1:
                TCEventHelper.onEvent(this, AnalyDataValue.MASTER_HOMEPAGE_FANS_CLICK, map);
                break;
            case 2:
                TCEventHelper.onEvent(this, AnalyDataValue.MASTER_HOMEPAGE_FOLLOW_CLICK, map);
                break;
            case 3:
                TCEventHelper.onEvent(this, AnalyDataValue.MASTER_HOMEPAGE_ATTETION_BUTTON_CLICK, map);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case IntentConstants.LOGIN_RESULT://成功登录
                if (RESULT_OK == resultCode) {
                    startLoadData(false);
                }

                break;
            case TaMasterfragment.PEFECT_RESULT://完善资料
                if (RESULT_OK == resultCode) {
                    boolean booleanExtra = data.getBooleanExtra(SPUtils.EXTRA_DATA, true);
                    SPUtils.setUserHomePage(MasterHomepageActivity.this, booleanExtra);
                    mTaMasterfragment.againLoadUrl(SPUtils.getUserHomePage(MasterHomepageActivity.this));
                }

                break;

            case 101://编辑个人资料按钮
                if (RESULT_OK == resultCode) {
                    startLoadData(isLoading);
                    mTaMasterfragment.againLoadUrl(SPUtils.getUserHomePage(MasterHomepageActivity.this));
                }
                break;

        }
    }

    boolean isFollow = false;

    private void showNetErrorView(int arg1) {
        showErrorView(null, ErrorCode.NETWORK_UNAVAILABLE == arg1 ?
                IActionTitleBar.ErrorType.NETUNAVAILABLE : IActionTitleBar.ErrorType.ERRORNET, "", "", "", new ErrorViewClick() {
            @Override
            public void onClick(View view) {
                mController.getMasterDetail(MasterHomepageActivity.this, mTalentId);
            }
        });
    }

    @Override
    public void handleMessage(Message msg) {
        hideLoadingView();
        hideErrorView(null);
        mStickyNavLayout.onRefreshComplete();
//        mTvfollow.setClickable(true);
        switch (msg.what) {
            case ValueConstants.MSG_GET_MASTER_DETAIL_OK:
                handleFirstPageData((TalentInfo) msg.obj);
                break;
            case ValueConstants.MSG_GET_MASTER_DETAIL_KO:
//                showErrorPage(msg.arg1, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        mController.getMasterDetail(MasterHomepageActivity.this, mTalentId);
//                    }
//                });
                showNetErrorView(msg.arg1);
                break;
            case ValueConstants.MSG_ATTENTION_OK://成功关注
                isFollow = true;
//                mTopRightImg.setVisibility(View.VISIBLE);
                mTvfollow.setClickable(false);
                mTvfollow.setText(getString(R.string.icon_label_club_list_joined));
                TravelKaBeab.fansCount = TravelKaBeab.fansCount + 1;
                mTvFunsNum.setText(StringUtil.TransformationFansData(TravelKaBeab.fansCount));
                ToastUtil.showToast(this, getString(R.string.label_master_toast_follow_success));
                EventBus.getDefault().post(new EvBusUGCInfoAttention(mTalentId, true));
                break;
            case ValueConstants.MSG_ATTENTION_KO://关注失败
                ToastUtil.showToast(this, StringUtil.handlerErrorCode(this, msg.arg1));
                break;
            case ValueConstants.MSG_CANCEL_ATTENTION_OK://取消关注成功
                isFollow = false;
                mTvfollow.setClickable(true);
//                mTopRightImg.setVisibility(View.GONE);
                mTvfollow.setText(getString(R.string.add_attention));
                TravelKaBeab.fansCount = TravelKaBeab.fansCount - 1;
                mTvFunsNum.setText(StringUtil.TransformationFansData(TravelKaBeab.fansCount));
                ToastUtil.showToast(this, getString(R.string.label_master_toast_cancelfollow_success));
                EventBus.getDefault().post(new EvBusUGCInfoAttention(mTalentId, false));
                break;
            case ValueConstants.MSG_CANCEL_ATTENTION_KO://取消关注失败
                ToastUtil.showToast(this, StringUtil.handlerErrorCode(this, msg.arg1));
                break;
        }
    }


    /**
     * 处理首页配置数据
     *
     * @param value
     */
    private void handleFirstPageData(TalentInfo value) {
        if (value == null) {
            return;
        }
        TravelKaBeab = value;
//
        HashMap<String, String> hValue = new HashMap<>();
        hValue.put(AnalyDataValue.KEY_MID, String.valueOf(TravelKaBeab.userId));
        if (!StringUtil.isEmpty(TravelKaBeab.nickName)) {
            hValue.put(AnalyDataValue.KEY_MNAME, TravelKaBeab.nickName);
        }
        TCEventHelper.onEvent(this, AnalyDataValue.MASTER_SEEDETAILS, hValue);

        if (TravelKaBeab != null) {
            if (TravelKaBeab.frontCover != null) {
//                BaseImgView.loadimg(topbgImgPagerView,
//                        TravelKaBeab.frontCover,
//                        R.mipmap.icon_masterhomepage_bg,
//                        R.mipmap.icon_masterhomepage_bg,
//                        R.mipmap.icon_masterhomepage_bg,
//                        ImageScaleType.EXACTLY,
//                        -1,
//                        -1,
//                        -1);
                ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(TravelKaBeab.frontCover), R.mipmap.icon_masterhomepage_bg, topbgImgPagerView);

            } else {
                topbgImgPagerView.setImageResource(R.mipmap.icon_masterhomepage_bg);
            }

            if (!StringUtil.isEmpty(TravelKaBeab.signature)) {
                topbgTitle.setText(TravelKaBeab.signature);
            }

            //关注
            if (!StringUtil.isEmpty(TravelKaBeab.followCount + "")) {
                mTvAttentionNum.setText(TravelKaBeab.followCount + "");
            } else {
                mTvAttentionNum.setText("0");
            }

            //粉丝
            if (!StringUtil.isEmpty(TravelKaBeab.fansCount + "")) {
                mTvFunsNum.setText(StringUtil.TransformationFansData((int) TravelKaBeab.fansCount));
            } else {
                mTvFunsNum.setText("0");
            }
            mTvfollow.setOnClickListener(this);
            //是否已关注
            showAttentionStatus(!"NONE".equals(TravelKaBeab.attentionType));

            if (!StringUtil.isEmpty(TravelKaBeab.avatar)) {
//                BaseImgView.loadimg(mHeadView,
//                        TravelKaBeab.avatar,
//                        R.mipmap.icon_default_avatar,
//                        R.mipmap.icon_default_avatar,
//                        R.mipmap.icon_default_avatar,
//                        null,
//                        -1,
//                        -1,
//                        180);
                ImageLoadManager.loadCircleImage(CommonUtil.getImageFullUrl(TravelKaBeab.avatar), R.mipmap.icon_default_avatar, mHeadView);

            }
            if (!StringUtil.isEmpty(TravelKaBeab.nickName)) {
                nameTv.setText(TravelKaBeab.nickName);
            }

            if (!StringUtil.isEmpty(TravelKaBeab.gender)) {
                if (TravelKaBeab.gender.equals("2")) {  // 性别 1-未确认 2-男 3-女
                    Drawable nav_man = getResources().getDrawable(R.mipmap.ic_male);
                    nav_man.setBounds(0, 0, nav_man.getMinimumWidth(), nav_man.getMinimumHeight());
                    nameTv.setCompoundDrawables(null, null, nav_man, null);
                } else if (TravelKaBeab.gender.equals("3")) {
                    Drawable nav_women = getResources().getDrawable(R.mipmap.ic_female);
                    nav_women.setBounds(0, 0, nav_women.getMinimumWidth(), nav_women.getMinimumHeight());
                    nameTv.setCompoundDrawables(null, null, nav_women, null);
                } else {
                }
            } else {
                nameTv.setVisibility(View.INVISIBLE);
            }
            if (isLoading) {
                initView(value);
                isLoading = false;
            }
        }

    }

    @Override
    public void scrollY(boolean isHidden, int scrollY) {
        if (scrollY >= 360) {
//            mBaseTransparentNavView.showBottomDivid(true);
            ((ImageView) findViewById(R.id.trasparent_topbar_left)).setImageResource(R.mipmap.arrow_back_gray);
        } else {
//            mBaseTransparentNavView.showBottomDivid(false);
            ((ImageView) findViewById(R.id.trasparent_topbar_left)).setImageResource(R.mipmap.scenic_arrow_back_white);
        }
    }

    @Override
    public void onTabClick(View view, int position) {
        switch (position) {
            case 0:
                if (0 == mContentViewPager.getCurrentItem()) {
                } else {
                    mContentViewPager.setCurrentItem(position);
                }
                break;
            case 1://TA的评价监听事件
                if (1 == mContentViewPager.getCurrentItem()) {
                } else {
                    mContentViewPager.setCurrentItem(position);
                }
                break;
            case 2:
                if (2 == mContentViewPager.getCurrentItem()) {
                } else {
                    mContentViewPager.setCurrentItem(position);
                }
                break;
            default:
                break;
        }
    }

    /***
     * 计算 ViewPager高度
     */
    private int mTopViewHeight, mSlidingViewHeight, mBottomViewHeight;

    private void setCalculationHeight() {
        mBottomLayout = (LinearLayout) findViewById(R.id.base_sticky_bottom_layout);
        mBottomLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mBottomLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                mBottomViewHeight = mBottomLayout.getMeasuredHeight();
                setViewPagerHeight();
            }
        });

        mSlidingTabLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mSlidingTabLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                mSlidingViewHeight = mSlidingTabLayout.getMeasuredHeight();
                setViewPagerHeight();
            }
        });
//        mStickyNavLayout.requestLayout();
    }


    /**
     * 设置ViewPager的高度
     */
    private void setViewPagerHeight() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mContentViewPager.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ScreenSize.getScreenHeightIncludeStatusBar(getApplicationContext())
                            - mSlidingViewHeight
                            - mBottomViewHeight - ScreenSize.getStatusBarHeight(MasterHomepageActivity.this)));
        } else {
            mContentViewPager.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ScreenSize.getScreenHeightExcludeStatusBar(getApplicationContext())
                            - mSlidingViewHeight
                            - mBottomViewHeight - ScreenSize.getStatusBarHeight(MasterHomepageActivity.this)));
        }
    }

    @Override
    public void onRefresh(PullToRefreshBase<StickyNavLayout> refreshView) {
        if (mFragments != null && mFragments.size() == 3) {
            startLoadData(false);
            if (((TaEvaluateFragment) mFragments.get(1)) != null) {
                ((TaEvaluateFragment) mFragments.get(1)).updateData();//商品
            }
        } else {
            hideLoadingView();
            hideErrorView(null);
            mStickyNavLayout.onRefreshComplete();
        }
    }

    private CancelFollowPopView mCacheDlg;

    /***
     * 取消关注
     */
    private void showCacheDialog() {
        if (mCacheDlg == null) {
            mCacheDlg = new CancelFollowPopView(this, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.dialog_canel_follow://取消
//                            showCancelDialog();
                            mController.cancelAttention(MasterHomepageActivity.this, Integer.parseInt(String.valueOf(TravelKaBeab.userId)));
                            mCacheDlg.dismiss();
                            break;

                    }
                }
            });
        }
        mCacheDlg.showAtLocation(base_sticky_bottom_layout2, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    /**
     * 显示或者隐藏关注状态
     *
     * @param isAttention
     */
    public void showAttentionStatus(boolean isAttention) {
        mTvfollow.setVisibility(View.VISIBLE);
        //是否已关注
        if (!isAttention) {
            isFollow = false;
            //未关注
            mTvfollow.setClickable(true);
            mTvfollow.setText(getString(R.string.add_attention));
//            mTvfollow.setClickable(true);
//            mTopRightImg.setVisibility(View.GONE);
        } else {
            mTvfollow.setClickable(false);
            isFollow = true;
            //已经关注
            mTvfollow.setText(getString(R.string.icon_label_club_list_joined));
//            mTvfollow.setVisibility(View.GONE);
//            mTopRightImg.setVisibility(View.VISIBLE);
        }
        if (isMeUser) {
            if (userService.getLoginUserId() == mTalentId) {
                isMeUser = true;
                mTvfollow.setClickable(true);
                mTvfollow.setText(getString(R.string.label_master_personaldata));
            }
//            mTvfollow.setVisibility(View.GONE);
//            mTopRightImg.setVisibility(View.GONE);
        }
    }

    /**
     * 点击关注后更新列表数据
     *
     * @param evBusUGCInfoAttention
     */
    public void onEvent(EvBusUGCInfoAttention evBusUGCInfoAttention) {
        if (evBusUGCInfoAttention != null) {
            if (evBusUGCInfoAttention.isFollow()) {
//                TravelKaBeab.fansCount = TravelKaBeab.fansCount + 1;
//                mTvFunsNum.setText(StringUtil.TransformationFansData(TravelKaBeab.fansCount));
            } else {

                TravelKaBeab.fansCount = TravelKaBeab.fansCount - 1;
                mTvFunsNum.setText(StringUtil.TransformationFansData(TravelKaBeab.fansCount));
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


}
