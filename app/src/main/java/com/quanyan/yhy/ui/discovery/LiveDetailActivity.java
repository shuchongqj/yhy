package com.quanyan.yhy.ui.discovery;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.harwkin.nb.camera.MenuUtils;
import com.harwkin.nb.camera.TimeUtil;
import com.newyhy.activity.FullScreenVideoPlayerActivity;
import com.newyhy.utils.DateUtil;
import com.newyhy.views.ninelayout.FixPicUtils;
import com.newyhy.views.ninelayout.YHYNineGridLayout;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.util.HarwkinLogUtil;
import com.quanyan.base.util.ScreenSize;
import com.quanyan.base.util.StringUtil;
import com.quanyan.base.view.BaseNavView;
import com.quanyan.base.view.customview.pulltorefresh.PullToRefreshBase;
import com.quanyan.base.view.customview.pulltorefresh.PullToRefreshSticyView;
import com.quanyan.base.view.customview.stickyview.StickyNavLayout;
import com.quanyan.base.view.customview.tabscrollindicator.SlidingTabLayout;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.AnalyDataValue;
import com.yhy.common.eventbus.event.EvBusBlack;
import com.quanyan.yhy.eventbus.EvBusComplaintDetailDynamic;
import com.quanyan.yhy.eventbus.EvBusLiveComPraiNum;
import com.quanyan.yhy.eventbus.EvBusSubjectInfo;
import com.quanyan.yhy.eventbus.EvBusUGCInfoAttention;
import com.quanyan.yhy.eventbus.EvBusUserLoginState;
import com.quanyan.yhy.libanalysis.AnEvent;
import com.quanyan.yhy.libanalysis.Analysis;
import com.quanyan.yhy.ui.adapter.DetailViewPagerAdapter;
import com.quanyan.yhy.ui.base.utils.DialogUtil;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.base.utils.TCEventHelper;
import com.quanyan.yhy.ui.club.clubcontroller.ClubController;
import com.quanyan.yhy.ui.common.calendar.ScreenUtil;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.discovery.controller.DiscoverController;
import com.quanyan.yhy.ui.discovery.fragment.LiveDetailApprasieFragment;
import com.quanyan.yhy.ui.discovery.fragment.LiveDetailCommentFragment;
import com.quanyan.yhy.util.HealthCircleTextUtil;
import com.videolibrary.utils.IntentUtil;
import com.yhy.common.beans.net.model.comment.ComSupportInfo;
import com.yhy.common.beans.net.model.comment.CommentInfo;
import com.yhy.common.beans.net.model.comment.CommetDetailInfo;
import com.yhy.common.beans.net.model.discover.UgcInfoResult;
import com.yhy.common.beans.net.model.user.UserInfo;
import com.yhy.common.config.ContextHelper;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.AndroidUtils;
import com.yhy.common.utils.CommonUtil;
import com.yhy.common.utils.SPUtils;
import com.yhy.imageloader.ImageLoadManager;
import com.yhy.service.IUserService;

import org.akita.util.AndroidUtil;


import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

import static com.newyhy.adapter.UgcMultiAdapter.UGCItemType.LIVE_UGC;
import static com.newyhy.adapter.UgcMultiAdapter.UGCItemType.NORMAL_UGC;
import static com.newyhy.adapter.UgcMultiAdapter.UGCItemType.VIDEO_UGC;

/**
 * 发现---直播列表详情页
 * Created by zhaoxp on 2015-10-29.
 */
public class LiveDetailActivity extends BaseActivity implements LiveDetailCommentFragment.CommentItemClick,
        ViewPager.OnPageChangeListener, SlidingTabLayout.TabClick, PullToRefreshBase.OnRefreshListener<StickyNavLayout> {

    private EditText mCommentText;
    private TextView mPostComment;//发送按钮
    protected ClubController mClubController;
    private DiscoverController mDiscoverController;

    /**
     * 下拉刷新的ListView
     */
    protected PullToRefreshSticyView mPullToRefreshSticyView;

    /**
     * 粘性布局的父容器
     */
    protected StickyNavLayout mStickyNavLayout;

    /**
     * ViewPager标题的TAB布局
     */
    private SlidingTabLayout mSlidingTabLayout;
    /**
     * ViewPager的引用
     */
    protected ViewPager mContentViewPager;

    /**
     * ViewPager标题数组
     */
    private DetailViewPagerAdapter mAdapter;
    /**
     * Fragment的引用
     */
    protected ArrayList<Fragment> mFragments = new ArrayList<>();

    /**
     * 当前页数，默认为1
     */
    private int mPageIndex = 1;
    /**
     * 刷新状态
     */
    private boolean isRefresh = true;

    /**
     * 高度信息，用于设置ViewPager的高度
     */
    private int mTopViewHeight, mSlidingViewHeight, mBottomViewHeight;

    //private UgcInfoResult mAttentionClickTemp;

    private LinearLayout mDetailTopParentView;
    private View mDetailTopView;

    @Autowired
    IUserService userService;

    public IUserService getUserService() {
        return userService;
    }
    /**
     * @param context
     * @param id
     * @param commentType
     * @param praiseType
     */
    public static void gotoLiveDetailActivity(Fragment context, long id, String commentType, String praiseType, boolean isCommentClick) {
        Intent intent = new Intent(context.getActivity(), LiveDetailActivity.class);
        intent.putExtra(SPUtils.EXTRA_ID, id);
        intent.putExtra(SPUtils.EXTRA_TYPE_COMMENT, commentType);
        intent.putExtra(SPUtils.EXTRA_TYPE_PRAISE, praiseType);
        intent.putExtra("isCommentClick", isCommentClick);
        context.startActivityForResult(intent, ValueConstants.RESULT_LIVE_DATA);
    }

    /**
     * @param context
     * @param id
     * @param commentType
     * @param praiseType
     * @param b
     */
    public static void gotoLiveDetailActivity(Activity context, long id, String commentType, String praiseType, boolean b) {
        Intent intent = new Intent(context, LiveDetailActivity.class);
        intent.putExtra(SPUtils.EXTRA_ID, id);
        intent.putExtra(SPUtils.EXTRA_TYPE_COMMENT, commentType);
        intent.putExtra(SPUtils.EXTRA_TYPE_PRAISE, praiseType);
        intent.putExtra("isCommentClick", b);
        context.startActivityForResult(intent, ValueConstants.RESULT_LIVE_DATA);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.live_detail, null);
    }

    private BaseNavView mBaseNavView;

    @Override
    public View onLoadNavView() {
        mBaseNavView = new BaseNavView(this);
        mBaseNavView.setTitleText(R.string.label_title_live_detail);
        mBaseNavView.setRightTextClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TimeUtil.isFastDoubleClick()) {
                    return;
                }
                if (mDialog == null) {
                    mDialog = DialogUtil.showMessageDialog(LiveDetailActivity.this, getString(R.string.del_photo), getString(R.string.delete_notice),
                            getString(R.string.label_btn_ok), getString(R.string.label_btn_cancel), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //删除
                                    TCEventHelper.onEvent(LiveDetailActivity.this, AnalyDataValue.DISCOVERY_DELETE_CLICK, mSubjectInfo.id + "");
                                    mClubController.delSubjectInfo(LiveDetailActivity.this, mSubjectInfo.id);
                                    mDialog.dismiss();
                                }
                            }, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //取消
                                    mDialog.dismiss();
                                }
                            });
                }
                mDialog.show();
            }
        });
        return mBaseNavView;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }

    private UgcInfoResult mSubjectInfo;
    private String mTypeComment;//类型定义，直播，动态
    private String mTypePraise;//类型定义，直播，动态
    private long mSubjectId = -1;
    private boolean isCommentClick = false;
    private LiveDetailCommentFragment mLiveDetailComment;

    @Override
    protected void initView(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        mClubController = new ClubController(LiveDetailActivity.this, mHandler);
        mDiscoverController = new DiscoverController(LiveDetailActivity.this, mHandler);
        /*获取传递的数据*/
        mSubjectId = getIntent().getLongExtra(SPUtils.EXTRA_ID, -1);
        isCommentClick = getIntent().getBooleanExtra("isCommentClick", false);
        mTypeComment = getIntent().getStringExtra(SPUtils.EXTRA_TYPE_COMMENT);
        mTypePraise = getIntent().getStringExtra(SPUtils.EXTRA_TYPE_PRAISE);

        mDetailTopParentView = (LinearLayout) findViewById(R.id.id_stickynavlayout_topview);

        mPullToRefreshSticyView = (PullToRefreshSticyView) findViewById(R.id.live_detail_sticklayout);
        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.id_stickynavlayout_indicator);
        mContentViewPager = (ViewPager) findViewById(R.id.id_stickynavlayout_viewpager);
        mStickyNavLayout = mPullToRefreshSticyView.getRefreshableView();

        mCommentText = (EditText) findViewById(R.id.live_detail_bottom_comment);
        mPostComment = (TextView) findViewById(R.id.live_detail_bottom_comment_button);
        mPostComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doPostComment();
            }
        });
//        findViewById(R.id.cell_live_comment_layout).setVisibility(View.GONE);

        mPullToRefreshSticyView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mPullToRefreshSticyView.setScrollingWhileRefreshingEnabled(!mPullToRefreshSticyView.isScrollingWhileRefreshingEnabled());
        mPullToRefreshSticyView.setOnRefreshListener(this);


        mPullToRefreshSticyView.setOnRefreshListener(this);

        mLiveDetailComment = LiveDetailCommentFragment.getInstance(mSubjectId, mTypeComment);
        mLiveDetailComment.setCommentItemClickListener(this);
        mFragments.add(mLiveDetailComment);
        mFragments.add(LiveDetailApprasieFragment.getInstance(mSubjectId, mTypePraise));
        mAdapter = new DetailViewPagerAdapter(getSupportFragmentManager(), mFragments, null);

        mAdapter.setTitles(setPagerTitles());
        mContentViewPager.setAdapter(mAdapter);
        mContentViewPager.setCurrentItem(0);
        mContentViewPager.addOnPageChangeListener(this);

        //tab属性设置
        mSlidingTabLayout.setDistributeEvenly(true);
        mSlidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.ac_title_bg_color));
        mSlidingTabLayout.setTabViewTextSizeSp(15);
        mSlidingTabLayout.setOnTabClickListener(this);
        mSlidingTabLayout.setViewPager(mContentViewPager);

        mBottomHeight = ScreenSize.convertDIP2PX(getApplicationContext(), 48);
        resetViewHeight();

        //先填充一个占位布局
        mDetailTopView = View.inflate(this, R.layout.ugc_recycle_normal_item, null);
        mDetailTopParentView.addView(mDetailTopView, 0);

        //获取Ugc详情数据
        getDetailData();

//        findViewById(R.id.tv_attention).setVisibility(View.INVISIBLE);
    }

    private void resetViewHeight() {
        mBaseNavView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mBaseNavView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                if (isTopCover()) {
                    mStickyNavLayout.setTopHeight(mBaseNavView.getMeasuredHeight());
                    mStickyNavLayout.requestLayout();
                }
                mTopViewHeight = mBaseNavView.getMeasuredHeight();
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
    }

    private ArrayList<String> setPagerTitles() {
        ArrayList<String> titles = new ArrayList<>();
        String[] strings = getResources().getStringArray(R.array.live_detail_tabs);
        titles.add(String.format(strings[0], 0));
        titles.add(String.format(strings[1], 0));
        return titles;
    }

    private void doPostComment() {
        //事件统计
        Analysis.pushEvent(this, AnEvent.INFORMATION_FLOW_SEND_COMMENTS);
        if (getUserService().isLogin()) {
            TCEventHelper.onEvent(LiveDetailActivity.this, AnalyDataValue.Find_DirectComment, mSubjectId + "");
            String content = mCommentText.getText().toString();
            String postContent = StringUtil.sideTrim(content, "\n");
            if (!TextUtils.isEmpty(postContent)) {
                mPostComment.setClickable(false);
                doPostComment(postContent);
            } else {
                ToastUtil.showToast(LiveDetailActivity.this, "评论不能为空哦");
            }
        } else {
            NavUtils.gotoLoginActivity(LiveDetailActivity.this);
        }
    }

    /**
     * 获取详情数据
     */
    public void getDetailData() {
        if (mSubjectId > 0) {
            mDiscoverController.doGetLiveDetail(LiveDetailActivity.this, mSubjectId);
        } else {
            ToastUtil.showToast(LiveDetailActivity.this, getString(R.string.error_params));
        }
    }

    /**
     * 使用EventBus发送通知更新title数量
     *
     * @param evBusLiveComPraiNum
     */
    public void onEvent(EvBusLiveComPraiNum evBusLiveComPraiNum) {
        if (null != evBusLiveComPraiNum) {
            switch (evBusLiveComPraiNum.getType()) {
                case 1:
                    //评论数量
                    if (null != mSubjectInfo) {
                        mSubjectInfo.commentNum = evBusLiveComPraiNum.getNum();
                    }
                    updateTabTitles(mSubjectInfo);
                    break;
                case 2:
                    //点赞数量
                    if (null != mSubjectInfo) {
                        mSubjectInfo.supportNum = evBusLiveComPraiNum.getNum();
                    }
                    updateTabTitles(mSubjectInfo);
                    break;
            }
        }
    }

    /**
     * 登录登出事件响应
     *
     * @param evBusUserLoginState
     */
    public void onEvent(EvBusUserLoginState evBusUserLoginState) {
        if (null != evBusUserLoginState) {
            if (evBusUserLoginState.getUserLoginState() == 0) {
                if (mSubjectInfo != null && mSubjectInfo.userInfo != null) {
                    if (getUserService().isLoginUser(mSubjectInfo.userInfo.userId)) {
                    } else {
                        mClubController.doAddAttention(LiveDetailActivity.this, mSubjectInfo.userInfo.userId);
                    }
                }
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //initTopData(mSubjectInfo);
                    covertUgcDetailUi(mSubjectInfo);
                    judgeIsSelf(mSubjectInfo);
                }
            });
        }
    }

    /**
     * 用户被屏蔽通知
     *
     * @param evBusBlackUser
     */
    public void onEvent(EvBusBlack evBusBlackUser) {
        if (null != evBusBlackUser) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            });
        }
    }

    /**
     * 更新tab内容
     *
     * @param subjectInfo
     */
    private void updateTabTitles(UgcInfoResult subjectInfo) {
        //直播tab
        ArrayList<String> titles = new ArrayList<>();
        String[] strings = getResources().getStringArray(R.array.live_detail_tabs);
        titles.add(String.format(strings[0], subjectInfo == null ? 0 : (mSubjectInfo.commentNum > 0 ? mSubjectInfo.commentNum : 0)));
        titles.add(String.format(strings[1], subjectInfo == null ? 0 : (mSubjectInfo.supportNum > 0 ? mSubjectInfo.supportNum : 0)));
        mAdapter.setTitles(titles);
        mSlidingTabLayout.setViewPager(mContentViewPager);
    }

    private Dialog mDialog;//删除对话框
    private boolean isInited = false;

    /**
     *  更新点赞状态
     */
    private void judgeThePraiseState(final UgcInfoResult subjectInfo) {
        if (subjectInfo != null && subjectInfo.userInfo != null
                && (getUserService().isLogin()
                && !getUserService().isLoginUser(subjectInfo.userInfo.userId))) {
            if (ValueConstants.TYPE_AVAILABLE.equals(subjectInfo.isSupport)) {
                if(mDetailTopView != null) {
                    mDetailTopView.findViewById(R.id.iv_support_ugc).setSelected(true);//是否点赞
                }
            } else {
                if(mDetailTopView != null) {
                    mDetailTopView.findViewById(R.id.iv_support_ugc).setSelected(false);//是否点赞
                }
            }
        }
    }

    /**
     * 设置直播数据视图
     */
    /*private void initTopData(UgcInfoResult subjectInfo) {
        int width = (ScreenSize.getScreenWidth(getApplicationContext()) - 30 * 2 - 20 * 2) / 3;
        final LinearLayout.LayoutParams layoutParamsPic = new LinearLayout.LayoutParams(width, width);
        if(subjectInfo.contentType == LiveItemHelper.TYPE_LIVE_CONTENT){
            LiveItemHelper.convertCircleLive(mDetailTopView, subjectInfo, mTypeComment, mTypePraise, false,
                    LiveDetailActivity.class.getSimpleName(), new LiveItemHelper.AttentionClickOuter() {
                        @Override
                        public void onAttentionClick(UgcInfoResult ugcInfoResult) {
                            // TODO: 16/7/11 关注
                            if (TimeUtil.isFastDoubleClick()) {
                                return;
                            }
                            mAttentionClickTemp = ugcInfoResult;
                            if (ugcInfoResult != null && ugcInfoResult.userInfo != null) {
                                analyDataTwo(LiveDetailActivity.this, ugcInfoResult.userInfo);
                            }
                            if (SPUtils.isLogin(getApplicationContext())) {
                                if (ugcInfoResult != null && ugcInfoResult.userInfo != null) {
                                    mClubController.doAddAttention(LiveDetailActivity.this, ugcInfoResult.userInfo.userId);
                                }
                            } else {
                                NavUtils.gotoLoginActivity(LiveDetailActivity.this);
                            }
                        }
                    });
        }else if(!TextUtils.isEmpty(subjectInfo.videoPicUrl)){
            LiveItemHelper.convertCircleVideo(mDetailTopView, subjectInfo, mTypeComment, mTypePraise, false,
                    LiveDetailActivity.class.getSimpleName(), new LiveItemHelper.AttentionClickOuter() {
                        @Override
                        public void onAttentionClick(UgcInfoResult ugcInfoResult) {
                            // TODO: 16/7/11 关注
                            if (TimeUtil.isFastDoubleClick()) {
                                return;
                            }
                            mAttentionClickTemp = ugcInfoResult;
                            if (ugcInfoResult != null && ugcInfoResult.userInfo != null) {
                                analyDataTwo(LiveDetailActivity.this, ugcInfoResult.userInfo);
                            }
                            if (SPUtils.isLogin(getApplicationContext())) {
                                if (ugcInfoResult != null && ugcInfoResult.userInfo != null) {
                                    mClubController.doAddAttention(LiveDetailActivity.this, ugcInfoResult.userInfo.userId);
                                }
                            } else {
                                NavUtils.gotoLoginActivity(LiveDetailActivity.this);
                            }
                        }
                    });
        }else{
           LiveItemHelper.convertCircleUGC(mDetailTopView, 0, subjectInfo, mTypeComment, mTypePraise, false,
                   LiveDetailActivity.class.getSimpleName(), new LiveItemHelper.AttentionClickOuter() {
                       @Override
                       public void onAttentionClick(UgcInfoResult ugcInfoResult) {
                           // TODO: 16/7/11 关注
                           if (TimeUtil.isFastDoubleClick()) {
                               return;
                           }
                           mAttentionClickTemp = ugcInfoResult;
                           if (ugcInfoResult != null && ugcInfoResult.userInfo != null) {
                               analyDataTwo(LiveDetailActivity.this, ugcInfoResult.userInfo);
                           }
                           if (SPUtils.isLogin(getApplicationContext())) {
                               if (ugcInfoResult != null && ugcInfoResult.userInfo != null) {
                                   mClubController.doAddAttention(LiveDetailActivity.this, ugcInfoResult.userInfo.userId);
                               }
                           } else {
                               NavUtils.gotoLoginActivity(LiveDetailActivity.this);
                           }
                       }
                   });
        }
//        LiveItemHelper.handleItemDetail(LiveDetailActivity.this, mTypeComment, mTypePraise,
//                findViewById(R.id.id_stickynavlayout_topview), subjectInfo, "", layoutParamsPic,
//
//                new LiveItemHelper.AttentionClickOuter() {
//                    @Override
//                    public void onAttentionClick(UgcInfoResult ugcInfoResult) {
//                        // TODO: 16/7/11 关注
//                        if (TimeUtil.isFastDoubleClick()) {
//                            return;
//                        }
//                        mAttentionClickTemp = ugcInfoResult;
//                        if (ugcInfoResult != null && ugcInfoResult.userInfo != null) {
//                            analyDataTwo(LiveDetailActivity.this, ugcInfoResult.userInfo);
//                        }
//                        if (SPUtils.isLogin(getApplicationContext())) {
//                            if (ugcInfoResult != null && ugcInfoResult.userInfo != null) {
//                                mClubController.doAddAttention(LiveDetailActivity.this, ugcInfoResult.userInfo.userId);
//                            }
//                        } else {
//                            NavUtils.gotoLoginActivity(LiveDetailActivity.this);
//                        }
//                    }
//                });

        if (subjectInfo.userInfo == null) {
            ToastUtil.showToast(LiveDetailActivity.this, getString(R.string.error_data_exception));
            finish();
            return;
        }
    }*/

    private void analyDataTwo(Activity mContext, UserInfo userInfo) {
        Map<String, String> map = new HashMap();
        map.put(AnalyDataValue.KEY_MID, userInfo.userId + "");
        map.put(AnalyDataValue.KEY_MNAME, userInfo.nickname);
        TCEventHelper.onEvent(mContext, AnalyDataValue.DISCOVERY_FOLLOW_CLICK, map);
    }

    /**
     * 屏蔽功能
     *
     * @param evBusComplaintDynamic
     */
    private UgcInfoResult mChooseSubjectInfo;

    public void onEvent(EvBusComplaintDetailDynamic evBusComplaintDynamic) {
        if (evBusComplaintDynamic == null || evBusComplaintDynamic.getSubjectInfo() == null) {
            mChooseSubjectInfo = null;
            return;
        }
        mChooseSubjectInfo = evBusComplaintDynamic.getSubjectInfo();
        showComplaintMenuDialog();
    }

    /**
     * 展示投诉菜单
     */
    public void showComplaintMenuDialog() {
        List<String> menus = new ArrayList<>();
        if (mChooseSubjectInfo.type != 0) {
            menus.add(getString(R.string.label_menu_cancel_attention));
        }
        if (mChooseSubjectInfo == null ||
                mChooseSubjectInfo.userInfo == null ||
                !getUserService().isLoginUser(mChooseSubjectInfo.userInfo.userId)) {
            menus.add(getString(R.string.label_menu_accusation));
            menus.add(getString(R.string.label_menu_complaint));
        }
        final String[] mMenus = StringUtil.listToStrings(menus);
        Dialog mComplaintDlg = MenuUtils.showAlert(this,
                null,
                mMenus,
                null,
                new MenuUtils.OnAlertSelectId() {
                    @Override
                    public void onClick(int whichButton) {
                        if (whichButton != mMenus.length) {
                            if (getString(R.string.label_menu_accusation).equals(mMenus[whichButton])) {
                                tcEvent(1);
                                gotoComplaintListUI();
                            } else if (getString(R.string.label_menu_complaint).equals(mMenus[whichButton])) {
                                tcEvent(2);
                                gotoBlackSetting();
                            } else if (getString(R.string.label_menu_cancel_attention).equals(mMenus[whichButton])) {
//                                doCancelAttention();
                                tcEvent(3);
                                showCancelDialog();
                            }
                        }
                    }
                },
                null);

        mComplaintDlg.show();
    }

    private Dialog mCancelDialog;

    private void showCancelDialog() {
        View view = View.inflate(this, R.layout.dialog_cancel_follow_confirm, null);
        if (mCancelDialog == null) {
//            mDialog = new DialogBuilder(this)
//                    .setCancelable(true)
//                    .setStyle(R.style.MenuDialogStyle)
//                    .setAnimation(R.anim.abc_fade_in)
//                    .setGravity(Gravity.CENTER)
//                    .build();
            mCancelDialog = DialogUtil.showMessageDialog(this, "是否不再关注此人？", "",
                    getString(R.string.label_btn_ok), getString(R.string.cancel), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            doCancelAttention();
                            mCancelDialog.dismiss();
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mCancelDialog.dismiss();
                        }
                    });
        }
        mCancelDialog.show();
    }

    private void tcEvent(int i) {
        Map<String, String> map = new HashMap();
        map.put(AnalyDataValue.KEY_ID, mChooseSubjectInfo.id + "");
        switch (i) {
            case 1:
                map.put(AnalyDataValue.KEY_TYPE, "1");
                TCEventHelper.onEvent(this, AnalyDataValue.DISCOVERY_OTHER_CLICK, map);
                break;
            case 2:
                map.put(AnalyDataValue.KEY_TYPE, "2");
                TCEventHelper.onEvent(this, AnalyDataValue.DISCOVERY_OTHER_CLICK, map);
                break;
            case 3:
                map.put(AnalyDataValue.KEY_TYPE, "3");
                TCEventHelper.onEvent(this, AnalyDataValue.DISCOVERY_OTHER_CLICK, map);
                break;
        }
    }

    /**
     * 取消关注
     */
    private void doCancelAttention() {
        if (mChooseSubjectInfo == null || mChooseSubjectInfo.userInfo == null) {
            return;
        }
        mDiscoverController.doCancelAttention(this, mChooseSubjectInfo.userInfo.userId);
    }

    /**
     * 屏蔽设置
     */
    private void gotoBlackSetting() {
        if (mChooseSubjectInfo == null) {
            return;
        }
//        NavUtils.gotoBlackSetting(this, mChooseSubjectInfo);
    }

    /**
     * 跳转到举报列表
     */
    private void gotoComplaintListUI() {
        if (mChooseSubjectInfo == null) {
            return;
        }
//        NavUtils.gotoComplaintList(this, mChooseSubjectInfo);
    }

    private void initial(UgcInfoResult subjectInfo) {
        // TODO: 2016/11/28  默认弹起对话框
        if (isCommentClick /*&& subjectInfo.commentNum <= 0*/ && !isInited) {
            isInited = true;
            mCommentText.requestFocus();
            mStickyNavLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        mStickyNavLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                    mStickyNavLayout.scrollToTop();
                    AndroidUtil.hideIME(LiveDetailActivity.this, false);
                }
            });
        }
    }

    private void judgeIsSelf(final UgcInfoResult subjectInfo) {
        if (getUserService().isLogin()
                && subjectInfo.userInfo != null
                && getUserService().isLoginUser(subjectInfo.userInfo.userId)) {
            //自己的
            mBaseNavView.setRightText(R.string.del_photo);
        } else {
            judgeThePraiseState(subjectInfo);
        }
    }

    /**
     * 发表评论
     */
    private void doPostComment(String content) {
        if (mSubjectInfo == null) {
            HarwkinLogUtil.error("直播详情的数据对象mSubjectInfo == null");
            return;
        }
        boolean flag = content.contentEquals("\r\n");
        if (!flag && TextUtils.isEmpty(content.trim())) {
            ToastUtil.showToast(LiveDetailActivity.this, getString(R.string.notice_is_all_space));
            return;
        }
        CommetDetailInfo commetDetailInfo = new CommetDetailInfo();
        commetDetailInfo.outType = mTypeComment;
        commetDetailInfo.userId = userService.getLoginUserId();
        commetDetailInfo.textContent = content;
        commetDetailInfo.outId = mSubjectInfo.id;
        if (mCommentInfo != null && mCommentInfo.createUserInfo != null) {
            commetDetailInfo.replyToUserId = mCommentInfo.createUserInfo.userId;
        } else {
            HarwkinLogUtil.error("直播详情    评论的用户对象为---》null");
        }
        mClubController.doPostComment(LiveDetailActivity.this, commetDetailInfo);
    }

    /**
     * 点赞，评论成功后刷新列表
     *
     * @param pos
     */
    private void updataTabTitles(int pos) {
        switch (pos) {
            case 0:
                LiveDetailCommentFragment liveDetailComment = (LiveDetailCommentFragment) mFragments.get(0);
                if (0 != mContentViewPager.getCurrentItem()) {
                    mContentViewPager.setCurrentItem(0);
                }
                if (mSubjectInfo != null && mSubjectInfo.userInfo != null) {
                    liveDetailComment.updateData(true, mSubjectInfo.userInfo.userId);
                }
                break;
            case 1:
                LiveDetailApprasieFragment liveDetailApprasie = (LiveDetailApprasieFragment) mFragments.get(1);
                if (1 != mContentViewPager.getCurrentItem()) {
                    mContentViewPager.setCurrentItem(1);
                }
                liveDetailApprasie.updateData(true);
                break;
        }
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        mPullToRefreshSticyView.onRefreshComplete();
        switch (msg.what) {
            case ValueConstants.MSG_COMMENT_OK:
                mPostComment.setClickable(true);
                //评论成功
                AndroidUtil.hideIME(LiveDetailActivity.this, true);
                mCommentInfo = null;
                mCommentText.setText("");
                mCommentText.setHint(R.string.label_post_comment);
                updataTabTitles(0);
                mSubjectInfo.commentNum += 1;
                EventBus.getDefault().post(new EvBusSubjectInfo(mSubjectInfo, false));
                break;
            case ValueConstants.MSG_COMMENT_ERROR:
                AndroidUtil.hideIME(LiveDetailActivity.this, true);
                ToastUtil.showToast(this, StringUtil.handlerErrorCode(this, msg.arg1));
                break;
            case ValueConstants.MSG_LIVE_DETAIL_OK:
                //直播详情数据
                UgcInfoResult subjectInfo = (UgcInfoResult) msg.obj;
                if (subjectInfo != null) {
                    mSubjectInfo = subjectInfo;
                    covertUgcDetailUi(mSubjectInfo);
                    if (isInited) {
                        LiveDetailCommentFragment liveDetailComment = (LiveDetailCommentFragment) mFragments.get(0);
                        if (subjectInfo.userInfo != null) {
                            liveDetailComment.updateData(true, subjectInfo.userInfo.userId);
                        } else {
                            liveDetailComment.updateData(true, 0);
                        }
                        LiveDetailApprasieFragment liveDetailApprasie = (LiveDetailApprasieFragment) mFragments.get(1);
                        liveDetailApprasie.updateData(true);
                    }
                    judgeIsSelf(subjectInfo);
                    //initTopData(subjectInfo);
                    initial(subjectInfo);

                    /*//暂无评论，显示软键盘
                    if (mSubjectInfo == null) {
                        if (subjectInfo.contentType == LiveItemHelper.TYPE_LIVE_CONTENT) {
                            mDetailTopView = View.inflate(this, R.layout.cell_master_circle_live, null);
                            mDetailTopParentView.addView(mDetailTopView, 0);
                        } else if (!TextUtils.isEmpty(subjectInfo.videoPicUrl)) {
                            mDetailTopView = View.inflate(this, R.layout.cell_master_circle_video, null);
                            mDetailTopParentView.addView(mDetailTopView, 0);
                        } else {
                            mDetailTopView = View.inflate(this, R.layout.cell_master_circle_ugc, null);
                            mDetailTopParentView.addView(mDetailTopView, 0);
                        }
                    }*/
                    updateTabTitles(mSubjectInfo);
                    //((TextView)mDetailTopView.findViewById(R.id.cell_circle_common_bottom_like)).setText(Integer.toString(subjectInfo.supportNum));
                } else {
                    ToastUtil.showToast(LiveDetailActivity.this, getString(R.string.label_error_live_detail_deleted));
                    finish();
                }
                break;
            case ValueConstants.MSG_LIVE_DEATIL_ERROR:
                ToastUtil.showToast(this, StringUtil.handlerErrorCode(this, msg.arg1));
                break;
            case ValueConstants.MSG_PRAISE_OK:
                ComSupportInfo comSupportInfo = (ComSupportInfo) msg.obj;
                if (comSupportInfo != null && mSubjectInfo != null) {
                    mSubjectInfo.isSupport = comSupportInfo.isSupport;
                    if (ValueConstants.TYPE_DELETED.equals(mSubjectInfo.isSupport)) {
                        mSubjectInfo.supportNum = mSubjectInfo.supportNum - 1 > 0 ? mSubjectInfo.supportNum - 1 : 0;
                    } else {
                        mSubjectInfo.supportNum = mSubjectInfo.supportNum + 1;
                    }
                    ((TextView)mDetailTopView.findViewById(R.id.tv_support_num_ugc)).setText("" + mSubjectInfo.supportNum);
                    //点赞数据
                    judgeThePraiseState(mSubjectInfo);
                    EventBus.getDefault().post(new EvBusSubjectInfo(mSubjectInfo, false));
                }
                updataTabTitles(1);
                break;
            case ValueConstants.MSG_PRAISE_ERROR:
                ToastUtil.showToast(LiveDetailActivity.this, StringUtil.handlerErrorCode(this, msg.arg1));
                break;
            //删除
            case ValueConstants.MSG_DELETE_DYNAMIC_OK: {
                boolean isSuccess = (boolean) msg.obj;
                if (isSuccess) {
                    EventBus.getDefault().post(new EvBusSubjectInfo(mSubjectInfo, true));
                    finish();
                } else {
                    ToastUtil.showToast(LiveDetailActivity.this, getString(R.string.toast_delete_subject_ko));
                }
                break;
            }
            case ValueConstants.MSG_DELETE_DYNAMIC_ERROR:
                ToastUtil.showToast(LiveDetailActivity.this, StringUtil.handlerErrorCode(this, msg.arg1));
                break;
            //删除评论
            case ValueConstants.MSG_DELETE_COMMENT: {
                boolean isSuccess = (boolean) msg.obj;
                if (isSuccess) {
                    updataTabTitles(0);
                    mSubjectInfo.commentNum = mSubjectInfo.commentNum - 1 > 0 ?
                            mSubjectInfo.commentNum - 1 : 0;
                    EventBus.getDefault().post(new EvBusSubjectInfo(mSubjectInfo, false));
                } else {
                    ToastUtil.showToast(LiveDetailActivity.this, "删除失败");
                }
                break;
            }
            case ValueConstants.MSG_DELETE_COMMENT_ERROR:
                break;
            case ValueConstants.MSG_CANCEL_ATTENTION_OK:
                /*if (mChooseSubjectInfo != null && mChooseSubjectInfo.userInfo != null) {
                    mSubjectInfo.type = 0;
                    initTopData(mSubjectInfo);
                    ToastUtil.showToast(this, getString(R.string.toast_cancel_attention));
                    EventBus.getDefault().post(new EvBusUGCInfoAttention(mChooseSubjectInfo.userInfo.userId, false));
                }*/
                mSubjectInfo.type = 0;
                mDetailTopView.findViewById(R.id.btn_follow_ugc).setVisibility(View.GONE);
                ToastUtil.showToast(this, getString(R.string.toast_cancel_attention));
                EventBus.getDefault().post(new EvBusUGCInfoAttention(mSubjectInfo.userInfo.userId, false));
                break;
            case ValueConstants.MSG_CANCEL_ATTENTION_KO:
                ToastUtil.showToast(getApplicationContext(), StringUtil.handlerErrorCode(getApplicationContext(), msg.arg1));
                break;
            case ValueConstants.MSG_ATTENTION_OK:
                //关注成功
                mSubjectInfo.type = 1;
                //initTopData(mSubjectInfo);
                mDetailTopView.findViewById(R.id.btn_follow_ugc).setVisibility(View.GONE);
                EventBus.getDefault().post(new EvBusUGCInfoAttention(mSubjectInfo.userInfo.userId, true));
                /*if (mAttentionClickTemp != null && mAttentionClickTemp.userInfo != null) {
                    mAttentionClickTemp = null;
                }*/
                break;
            case ValueConstants.MSG_ATTENTION_KO:
                ToastUtil.showToast(LiveDetailActivity.this, StringUtil.handlerErrorCode(LiveDetailActivity.this, msg.arg1));
                break;
        }
    }

    private CommentInfo mCommentInfo;//回复评论
    private Dialog mCommDeleteDialog;//评论删除提示

    /**
     * 回复某人的评论
     *
     * @param item
     */
    @Override
    public void commentItemClick(final CommentInfo item) {
        if (item != null && item.createUserInfo != null) {
            mCommentInfo = null;
            if (userService.getLoginUserId() == item.createUserInfo.userId) {
//                if (mCommDeleteDialog == null) {
                    mCommDeleteDialog = DialogUtil.showMessageDialog(LiveDetailActivity.this, getString(R.string.del_photo), getString(R.string.delete_notice),
                            getString(R.string.label_btn_ok), getString(R.string.label_btn_cancel), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //TODO 删除我的评论
                                    mDiscoverController.doDelComment(LiveDetailActivity.this, item.id, mTypeComment);
                                    System.out.println(item.id);
                                    mCommDeleteDialog.dismiss();
                                }
                            }, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //取消
                                    mCommDeleteDialog.dismiss();
                                }
                            });
//                }
                mCommDeleteDialog.show();
            } else {
                mCommentInfo = item;
                mCommentText.requestFocus();
                AndroidUtil.hideIME(LiveDetailActivity.this, false);
                mCommentText.setHint("回复" + (TextUtils.isEmpty(item.createUserInfo.nickname) ? "" : item.createUserInfo.nickname) + "：");
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        if (!mPullToRefreshSticyView.getRefreshableView().isTopHidden) {
            ViewGroup view = (ViewGroup) mFragments.get(position).getView().findViewById(R.id.id_stickynavlayout_innerscrollview);
            mPullToRefreshSticyView.getRefreshableView().resetScroll(view);
        }
        switch (position) {
            case 0:
                findViewById(R.id.live_detail_bottom_comment_layout).setVisibility(View.VISIBLE);
                mBottomHeight = ScreenSize.convertDIP2PX(getApplicationContext(), 48);
                setViewPagerHeight();
                break;
            case 1:
                findViewById(R.id.live_detail_bottom_comment_layout).setVisibility(View.GONE);
                mBottomHeight = 0;
                setViewPagerHeight();
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onTabClick(View view, int position) {
        //更新数据
        switch (position) {
            case 0:
                findViewById(R.id.live_detail_bottom_comment_layout).setVisibility(View.VISIBLE);
                mBottomHeight = ScreenSize.convertDIP2PX(getApplicationContext(), 48);
                setViewPagerHeight();

                LiveDetailCommentFragment liveDetailComment = (LiveDetailCommentFragment) mAdapter.getItem(position);
                if (0 == mContentViewPager.getCurrentItem()) {
                    if (mSubjectInfo != null && mSubjectInfo.userInfo != null) {
                        liveDetailComment.updateData(true, mSubjectInfo.userInfo.userId);
                    }
                } else {
                    mContentViewPager.setCurrentItem(position);
                }
                break;
            case 1:
                findViewById(R.id.live_detail_bottom_comment_layout).setVisibility(View.GONE);
                mBottomHeight = 0;
                setViewPagerHeight();

                LiveDetailApprasieFragment liveDetailApprasie = (LiveDetailApprasieFragment) mAdapter.getItem(position);
                if (1 == mContentViewPager.getCurrentItem()) {
                    liveDetailApprasie.updateData(true);
                } else {
                    mContentViewPager.setCurrentItem(position);
                }
                break;
        }
    }

    @Override
    public void onRefresh(PullToRefreshBase<StickyNavLayout> refreshView) {
        // TODO: 2/4/16 刷新列表
        getDetailData();
        LiveDetailCommentFragment liveDetailComment = (LiveDetailCommentFragment) mFragments.get(0);
        if (mSubjectInfo != null && mSubjectInfo.userInfo != null) {
            liveDetailComment.updateData(true, mSubjectInfo.userInfo.userId);
        } else {
            liveDetailComment.updateData(true, 0);
        }
        LiveDetailApprasieFragment liveDetailApprasie = (LiveDetailApprasieFragment) mFragments.get(1);
        liveDetailApprasie.updateData(true);
    }

    private int mBottomHeight;

    private void setViewPagerHeight() {
        mContentViewPager.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ScreenSize.getScreenHeightExcludeStatusBar(getApplicationContext())
                        - ScreenSize.convertDIP2PX(getApplicationContext(), 48)
                        - mSlidingViewHeight
                        - mBottomHeight));
    }


    /**************************************************************    新 UgcItem Ui 上的处理     *****************************************************************/
    private int viewType;//ugc类型
    private boolean isLoadView = false;//是否加载过视图
    public void covertUgcDetailUi(UgcInfoResult data){

        viewType = 1;
        if (data.shortVideoType == 4 || data.shortVideoType == 5) {
            viewType = VIDEO_UGC;
        } else if (data.contentType == 3){//内容类型： 2.UGC 3.直播
            viewType = LIVE_UGC;
        } else {
            viewType = NORMAL_UGC;
        }

        switch (viewType) {
            case NORMAL_UGC:
                if (!isLoadView) {
                    mDetailTopView = View.inflate(this, R.layout.ugc_recycle_normal_item, null);
                    mDetailTopParentView.removeViewAt(0);
                    mDetailTopParentView.addView(mDetailTopView, 0);
                }
                covertNormalUGC(mDetailTopView,data);
                isLoadView = true;
                break;
            case VIDEO_UGC:
                if (!isLoadView) {
                    mDetailTopView = View.inflate(this, R.layout.ugc_recycle_video_item, null);
                    mDetailTopParentView.removeViewAt(0);
                    mDetailTopParentView.addView(mDetailTopView, 0);
                }
                covertVideoUGC(mDetailTopView,data);
                isLoadView = true;
                break;
            case LIVE_UGC:
                if (!isLoadView) {
                    mDetailTopView = View.inflate(this, R.layout.ugc_recycle_live_item, null);
                    mDetailTopParentView.removeViewAt(0);
                    mDetailTopParentView.addView(mDetailTopView, 0);
                }
                covertLiveUGC(mDetailTopView,data);
                isLoadView = true;
                break;
        }
    }

    /**
     * Item 的公共部分
     * @param view
     * @param data
     */
    private void synCommon(View view, final UgcInfoResult data) {
        //设置Tag防止Glide加载头像抖动
        if (null != data.userInfo.avatar && !data.userInfo.avatar.equals(view.findViewById(R.id.iv_header_ugc).getTag(R.id.iv_header_ugc))) {
            view.findViewById(R.id.iv_header_ugc).setTag(R.id.iv_header_ugc,data.userInfo.avatar);
            //头像
            ImageLoadManager.loadCircleImage(CommonUtil.getImageFullUrl(data.userInfo.avatar),R.mipmap.icon_default_avatar,(ImageView) view.findViewById(R.id.iv_header_ugc));
        }

        view.findViewById(R.id.iv_header_ugc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//跳转个人主页
                NavUtils.gotoMasterHomepage(v.getContext(), data.userInfo.userId);
            }
        });
        //昵称
        ((TextView)view.findViewById(R.id.tv_name_ugc)).setText(data.userInfo.nickname);
        //发布时间
        try {
            String time = DateUtil.getCreateAt(data.gmtCreated);
            ((TextView)view.findViewById(R.id.tv_time_ugc)).setText(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //ugc文本内容
        if (data.textContent != null && data.textContent.length() > 0) {
            view.findViewById(R.id.tv_ugc).setVisibility(View.VISIBLE);
            ((TextView)view.findViewById(R.id.tv_ugc)).setText(data.textContent);
        }else {
            view.findViewById(R.id.tv_ugc).setVisibility(View.GONE);
        }
        HealthCircleTextUtil.SetLinkClickIntercept(LiveDetailActivity.this, (TextView)view.findViewById(R.id.tv_ugc));

        //位置信息
        if (data.poiInfo != null && !TextUtils.isEmpty(data.poiInfo.detail)) {
            view.findViewById(R.id.ll_location_ugc).setVisibility(View.VISIBLE);
            ((TextView)view.findViewById(R.id.tv_location_ugc)).setText(data.poiInfo.detail);
        } else {
            view.findViewById(R.id.ll_location_ugc).setVisibility(View.GONE);
        }
        //隐藏评论
        (view.findViewById(R.id.ll_comment_ugc)).setVisibility(View.GONE);
        //点赞数
        ((TextView)view.findViewById(R.id.tv_support_num_ugc)).setText((data.supportNum > 0 ? (data.supportNum > 999 ? "999+" : data.supportNum) : 0) + "");
        if (ValueConstants.TYPE_AVAILABLE.equals(data.isSupport)) {
            //已点赞
            view.findViewById(R.id.iv_support_ugc).setSelected(true);
        }else {
            view.findViewById(R.id.iv_support_ugc).setSelected(false);
        }
        //直播或视频观看人数
        if (data.contentType == 2 && data.liveStatus == 1) {//直播
            ((TextView)view.findViewById(R.id.tv_type_live)).setText(getString(R.string.online));
        } else {//回放
            ((TextView)view.findViewById(R.id.tv_type_live)).setText(getString(R.string.saw));
        }
        //观看或在线人数
        String num = null;
        if (data.viewNum >= 9990000) {
            ((TextView)view.findViewById(R.id.tv_saw_number)).setText("999+万");
        } else if (data.viewNum >= 10000){
            num = (new DecimalFormat("#.##").format(data.viewNum / 10000.0f));
            ((TextView)view.findViewById(R.id.tv_saw_number)).setText(num + "万");
        } else {
            ((TextView)view.findViewById(R.id.tv_saw_number)).setText(data.viewNum + "");
        }

        //关注关系
        if (data.type == 1 || data.type == 2 || data.userInfo.userId == userService.getLoginUserId()) {// 关注类型 0:未关注 1:单向关注 2:双向关注
            view.findViewById(R.id.btn_follow_ugc).setVisibility(View.GONE);
        } else {
            view.findViewById(R.id.btn_follow_ugc).setVisibility(View.VISIBLE);
        }

        //更多
        if (getUserService().isLoginUser(data.userInfo.userId)) {
            view.findViewById(R.id.ll_more_ugc).setVisibility(View.GONE);
        } else {
            view.findViewById(R.id.ll_more_ugc).setVisibility(View.VISIBLE);
        }

        //点赞
        view.findViewById(R.id.ll_support_ugc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getUserService().isLogin()) {
                    String outType = ValueConstants.TYPE_PRAISE_LIVESUP;
                    if (ValueConstants.TYPE_AVAILABLE.equals(data.isSupport)) {
                        mClubController.doAddNewPraiseToComment(LiveDetailActivity.this, data.id, outType, ValueConstants.UNPRAISE);
                    } else if (ValueConstants.TYPE_DELETED.equals(data.isSupport)) {
                        mClubController.doAddNewPraiseToComment(LiveDetailActivity.this, data.id, outType, ValueConstants.PRAISE);
                    }
                } else {
                    NavUtils.gotoLoginActivity(LiveDetailActivity.this);
                }
            }
        });

        //关注
        view.findViewById(R.id.btn_follow_ugc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TimeUtil.isFastDoubleClick()) return;
                if (data != null && data.userInfo != null) analyDataTwo(LiveDetailActivity.this, data.userInfo);

                if (getUserService().isLogin()) {
                    if (data != null && data.userInfo != null) mClubController.doAddAttention(LiveDetailActivity.this, data.userInfo.userId);
                } else {
                    NavUtils.gotoLoginActivity(LiveDetailActivity.this);
                }
            }
        });

        //点击更多
        view.findViewById(R.id.ll_more_ugc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMoreDialog(data);
            }
        });
    }

    /**
     * Item中 一般Ugc的处理
     * @param view
     * @param data
     */
    public void covertNormalUGC(final View view, final UgcInfoResult data){
        synCommon(view, data);
        view.findViewById(R.id.ll_saw_number).setVisibility(View.INVISIBLE);
        //图片合集
        if (data.picList != null && data.picList.size() > 1 && data.videoUrl == null) {
            view.findViewById(R.id.iv_single_ugc).setVisibility(View.GONE);
            findViewById(R.id.ic_sup_height).setVisibility(View.GONE);
            view.findViewById(R.id.nine_layout).setVisibility(View.VISIBLE);
            ((YHYNineGridLayout)view.findViewById(R.id.nine_layout)).setIsShowAll(false);//当传入的图片数超过9张时，是否全部显示
            ((YHYNineGridLayout)view.findViewById(R.id.nine_layout)).setSpacing(ScreenUtil.dip2px(this,6));
            ((YHYNineGridLayout)view.findViewById(R.id.nine_layout)).setUrlList(data.picList); //最后再设置图片url

        } else if (data.picList != null && data.picList.size() == 1 && data.videoUrl == null) {
            //单张图，这里不用NineLayout中单张图的处理是因为，源码中对LayoutParam有修改适配，引起Item的复用时的撑开抖动
            view.findViewById(R.id.iv_single_ugc).setVisibility(View.VISIBLE);
            view.findViewById(R.id.nine_layout).setVisibility(View.GONE);

            Glide.with(this).asFile().load(CommonUtil.getImageFullUrl(data.picList.get(0))).into(new SimpleTarget<File>() {
                @Override
                public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    Bitmap bmp = BitmapFactory.decodeFile(resource.getAbsolutePath(), options);
                    int width = options.outWidth;
                    int height = options.outHeight;
                    FixPicUtils.PicSizeInfo size = FixPicUtils.getFixImage(LiveDetailActivity.this,width,height);
                    if (size.isSuperHeight) {
                        findViewById(R.id.ic_sup_height).setVisibility(View.VISIBLE);
                        ((ImageView)findViewById(R.id.iv_single_ugc)).setScaleType(ImageView.ScaleType.FIT_START);
                    } else {
                        findViewById(R.id.ic_sup_height).setVisibility(View.GONE);
                        ((ImageView)findViewById(R.id.iv_single_ugc)).setScaleType(ImageView.ScaleType.CENTER_CROP);
                    }
                    ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(data.picList.get(0)),R.mipmap.icon_default_750_360,
                            (int)size.width,(int)size.width,findViewById(R.id.iv_single_ugc));
                }
            });

            view.findViewById(R.id.iv_single_ugc).setOnClickListener(v -> {//单张图跳转
                ArrayList<String> list = new ArrayList<>();
                list.add(CommonUtil.getImageFullUrl(data.picList.get(0)));
                NavUtils.gotoLookBigImage(LiveDetailActivity.this, list, 1);
            });
        }else {
            findViewById(R.id.ic_sup_height).setVisibility(View.GONE);
            view.findViewById(R.id.iv_single_ugc).setVisibility(View.GONE);
            view.findViewById(R.id.nine_layout).setVisibility(View.GONE);
        }
    }

    /**
     * Item中 Video Ugc的处理
     * @param view
     * @param data
     */
    public void covertVideoUGC(View view, final UgcInfoResult data) {
        synCommon(view, data);
        view.findViewById(R.id.ll_saw_number).setVisibility(View.VISIBLE);
        //视频
        if (data.videoUrl != null ) {
            /* //使得播放器为正方形，根据要求适配正方形小视频
            ViewGroup.LayoutParams  param = holder.getView(R.id.jz_player).getLayoutParams();
            param.width = DisplayUtils.getScreenWidth(mContext) - DisplayUtils.dp2px(mContext,20);//frameLayout宽度;
            param.height = param.width;*/
            ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(data.videoPicUrl),R.mipmap.icon_default_750_360, (ImageView)findViewById(R.id.iv_cover));
            //去全屏播放
            findViewById(R.id.iv_cover).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (data.shortVideoType == 4) {//直录的小视频上传阿里云
                        FullScreenVideoPlayerActivity.gotoFullScreenVideoPlayerActivity(LiveDetailActivity.this,
                                CommonUtil.getImageFullUrl(data.videoUrl),data.id);
                    } else if (data.shortVideoType == 5) {//相册上传的小视频上传网宿云
                        FullScreenVideoPlayerActivity.gotoFullScreenVideoPlayerActivity(LiveDetailActivity.this,
                                ContextHelper.getVodUrl() + data.videoUrl,data.id);
                    }
                }
            });
        }
    }

    /**
     * Item中 Live Ugc的处理
     * @param view
     * @param data
     */
    public void covertLiveUGC(View view, final UgcInfoResult data) {
        synCommon(view, data);
        view.findViewById(R.id.ll_saw_number).setVisibility(View.VISIBLE);
        if (1 == data.liveStatus) {//直播状态 1.直播 2.回放
            ((TextView)view.findViewById(R.id.tv_live_type)).setText("直播");
        } else {
            ((TextView)view.findViewById(R.id.tv_live_type)).setText("回放");
        }
        //左下角标签
        StringBuilder tagList = new StringBuilder();
        if (data.tagInfoList != null && data.tagInfoList.size() > 0) {
            for (int i = 0; i < data.tagInfoList.size(); i++) {
                tagList.append("· ").append(data.tagInfoList.get(i).name);
            }
            ((TextView)view.findViewById(R.id.tv_live_category)).setText(tagList.toString());//直播所属类别
        }

        if (data.picList != null && data.picList.size() > 0) {
            ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(data.picList.get(0)), R.mipmap.icon_default_750_360 , (ImageView) view.findViewById(R.id.iv_thumb));
        }
        view.findViewById(R.id.iv_thumb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data.liveId > 0) {
                    if (data.userInfo != null) {
                        IntentUtil.startVideoClientActivity(data.liveId, data.userInfo.userId, 1 == data.liveStatus || 2 == data.liveStatus,data.liveScreenType);
                    } else {
                        IntentUtil.startVideoClientActivity(data.liveId, 0, 1 == data.liveStatus || 2 == data.liveStatus,data.liveScreenType);
                    }
                } else {
                    AndroidUtils.showToast(v.getContext(), "liveId无效");
                }
            }
        });
    }
    /****************************************************        logicMethod         *************************************************************/
    /**
     * 展示举报，屏蔽Menu
     */
    public void showMoreDialog(final UgcInfoResult data) {
        List<String> menus = new ArrayList<>();
        if (data.type != 0) {
            menus.add(this.getString(R.string.label_menu_cancel_attention));
        }
        if (data == null || data.userInfo == null || !getUserService().isLoginUser(data.userInfo.userId)) {
            menus.add(this.getString(R.string.label_menu_accusation));
            menus.add(this.getString(R.string.label_menu_complaint));
        }
        final String[] mMenus = StringUtil.listToStrings(menus);
        Dialog mComplaintDlg = MenuUtils.showAlert(this,
                null,
                mMenus,
                null,
                new MenuUtils.OnAlertSelectId() {
                    @Override
                    public void onClick(int whichButton) {
                        if (whichButton != mMenus.length) {
                            if (getString(R.string.label_menu_accusation).equals(mMenus[whichButton])) {
                                NavUtils.gotoComplaintList(LiveDetailActivity.this, data.textContent,
                                        data.picList != null && data.picList.size() > 0 ? new ArrayList<>(data.picList) : new ArrayList<>(), data.id);//举报
                            } else if (getString(R.string.label_menu_complaint).equals(mMenus[whichButton])) {
//                                NavUtils.gotoBlackSetting(LiveDetailActivity.this, data);//屏蔽
                            } else if (getString(R.string.label_menu_cancel_attention).equals(mMenus[whichButton])) {
                                showCancelDialog(data);
                            }
                        }
                    }
                }, null);
        mComplaintDlg.show();
    }

    private Dialog dialog;//确认是否取消关注的Dialog
    private void showCancelDialog(final UgcInfoResult data) {
        dialog = DialogUtil.showMessageDialog(this, "是否不再关注此人？", "",
                getString(R.string.label_btn_ok), getString(R.string.cancel), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDiscoverController.doCancelAttention(LiveDetailActivity.this, data.userInfo.userId);
                        dialog.dismiss();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
        dialog.show();
    }
}
