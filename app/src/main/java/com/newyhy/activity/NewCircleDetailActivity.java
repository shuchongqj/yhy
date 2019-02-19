package com.newyhy.activity;

import android.app.Dialog;
import android.os.Build;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.harwkin.nb.camera.TimeUtil;
import com.newyhy.fragment.circle.CircleDetailCommentFragment;
import com.newyhy.fragment.circle.CircleDetailPrasieFragment;
import com.newyhy.utils.ShareUtils;
import com.newyhy.views.dialog.InputMsgDialog;
import com.newyhy.views.itemview.CircleUgcPicLayout;
import com.quanyan.base.util.HarwkinLogUtil;
import com.quanyan.base.util.StringUtil;
import com.quanyan.base.view.BaseNavView;
import com.quanyan.yhy.R;
import com.yhy.common.eventbus.event.EvBusBlack;
import com.yhy.common.eventbus.event.EvBusCircleChangeFollow;
import com.yhy.common.eventbus.event.EvBusCircleChangePraise;
import com.quanyan.yhy.eventbus.EvBusSubjectInfo;
import com.quanyan.yhy.eventbus.EvBusUGCInfoAttention;
import com.quanyan.yhy.eventbus.EvBusUserLoginState;
import com.quanyan.yhy.libanalysis.AnEvent;
import com.quanyan.yhy.libanalysis.Analysis;
import com.quanyan.yhy.ui.adapter.DetailViewPagerAdapter;
import com.quanyan.yhy.ui.base.utils.DialogUtil;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.club.clubcontroller.ClubController;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.discovery.controller.DiscoverController;
import com.yhy.common.base.BaseNewActivity;
import com.yhy.common.beans.net.model.comment.ComSupportInfo;
import com.yhy.common.beans.net.model.comment.CommentInfo;
import com.yhy.common.beans.net.model.comment.CommetDetailInfo;
import com.yhy.common.beans.net.model.discover.UgcInfoResult;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.SPUtils;
import com.yhy.location.LocationManager;
import com.yhy.router.RouterPath;
import com.yhy.service.IUserService;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * 圈子详情页
 * <p>
 * Created by yangboxue on 2018/6/25.
 */
@Route(path = RouterPath.ACTIVITY_CIRCLE_DETAIL)
public class NewCircleDetailActivity extends BaseNewActivity implements View.OnClickListener {
    // 标题
    private BaseNavView mBaseNavView;
    // 头部详情
    private AppBarLayout appBarLayout;
    private LinearLayout mDetailTopParentView;
    private CircleUgcPicLayout mDetailTopView;
    // fragment
    private TabLayout mTabLayout;
    private TextView tvCommentTab;
    private TextView tvPraiseTab;
    private TextView tvBrowseNum;
    private ViewPager mContentViewPager;
    private DetailViewPagerAdapter mAdapter;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private CircleDetailCommentFragment mCircleDetailComment;
    private CircleDetailPrasieFragment mCircleDetailPrasie;
    // 底部
    private InputMsgDialog inputMsgDialog;
    private LinearLayout llytBottom;
    private LinearLayout llytGoComment;
    private LinearLayout llytComment;
    private LinearLayout llytPraise;
    private ImageView ivPraise;

    private ClubController mClubController;
    private DiscoverController mDiscoverController;

    private UgcInfoResult mSubjectInfo;
    //    private int circleDetailType;
    private long mSubjectId = -1;
    private boolean isCommentClick = false;
    private String[] titles;
    private boolean isMeUser;
    private boolean isExExpanded;
    private int oldOffset = 0;

    private CommentInfo mCommentInfo;//回复评论
    private Dialog mCommDeleteDialog;//评论删除提示
    private Dialog mDialog;          //删除本条动态对话框
    private Dialog mCancelDialog;    //取消关注

    @Autowired
    IUserService userService;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_new_circle_detail;
    }

    @Override
    protected void initVars() {
        super.initVars();
        EventBus.getDefault().register(this);
        mClubController = new ClubController(this, mHandler);
        mDiscoverController = new DiscoverController(this, mHandler);
        titles = getResources().getStringArray(R.array.live_detail_tabs);
        isExExpanded = true;
        /*获取传递的数据*/
        mSubjectId = getIntent().getLongExtra(SPUtils.EXTRA_ID, -1);
        isCommentClick = getIntent().getBooleanExtra("isCommentClick", false);
//        circleDetailType = getIntent().getIntExtra("circleDetailType", -1);

    }

    @Override
    protected void initView() {
        super.initView();
        mImmersionBar.fitsSystemWindows(false).transparentStatusBar().statusBarDarkFont(true).init();
        // 标题
        mBaseNavView = findViewById(R.id.title_view);
        mBaseNavView.setTitleText(R.string.label_title_live_detail);
        mBaseNavView.setRightImg(R.drawable.ic_new_more2);
        // 头部详情
        appBarLayout = findViewById(R.id.appbar);
        mDetailTopParentView = findViewById(R.id.llyt_detail);
        mDetailTopView = new CircleUgcPicLayout(this);
//        switch (circleDetailType) {
//            case CircleItemType.COFFEE_VIDEO:
//                mDetailTopView = new CircleUgcCoffeeVideoLayout(this);
//                break;
//            case CircleItemType.LIVE:
//                mDetailTopView = new CircleUgcLiveLayout(this);
//                break;
//            case CircleItemType.UGC_PIC:
//                mDetailTopView = new CircleUgcPicLayout(this);
//                break;
//        }
        mDetailTopParentView.addView(mDetailTopView, 0);
        // fragments
        mTabLayout = findViewById(R.id.tablayout);
        tvBrowseNum = findViewById(R.id.tv_browse_num);
        mContentViewPager = findViewById(R.id.viewPager);
        mCircleDetailComment = CircleDetailCommentFragment.getInstance(mSubjectId);
        mCircleDetailPrasie = CircleDetailPrasieFragment.getInstance(mSubjectId);
        mFragments.add(mCircleDetailComment);
        mFragments.add(mCircleDetailPrasie);
        mAdapter = new DetailViewPagerAdapter(getSupportFragmentManager(), mFragments, setPagerTitles());
        mContentViewPager.setAdapter(mAdapter);
        mContentViewPager.setCurrentItem(0);
        mTabLayout.setupWithViewPager(mContentViewPager);
        // tab样式
        View viewComment = LayoutInflater.from(this).inflate(R.layout.tab_view_item, null);
        viewComment.setSelected(true);
        tvCommentTab = viewComment.findViewById(R.id.tab_title);
        View viewPraise = LayoutInflater.from(this).inflate(R.layout.tab_view_item, null);
        tvPraiseTab = viewPraise.findViewById(R.id.tab_title);
        mTabLayout.getTabAt(0).setCustomView(viewComment);
        mTabLayout.getTabAt(1).setCustomView(viewPraise);
        tvCommentTab.setTextAppearance(NewCircleDetailActivity.this, R.style.TextBold);
        // 底部
        llytBottom = findViewById(R.id.live_detail_bottom_comment_layout);
        llytGoComment = findViewById(R.id.llyt_go_comment);
        llytComment = findViewById(R.id.llyt_comment);
        llytPraise = findViewById(R.id.llyt_praise);
        ivPraise = findViewById(R.id.iv_praise);
        // 从外面直接点评论则头部缩起来,弹出评论框
        if (isCommentClick) {
            appBarLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        appBarLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                    appBarLayout.postDelayed(() -> {
                        appBarLayout.setExpanded(false, true);
                        showInPutMsgDialog("");
                    }, 300);

                }
            });
        }

    }

    @Override
    protected void setListener() {
        super.setListener();

        llytGoComment.setOnClickListener(this);
        llytComment.setOnClickListener(this);
        llytPraise.setOnClickListener(this);

        mBaseNavView.setRightImgClick(v -> {
            if (TimeUtil.isFastDoubleClick()) {
                return;
            }
            ShareUtils.showShareBoard(NewCircleDetailActivity.this, false, false,
                    () -> gotoComplaintListUI(),
                    () -> gotoBlackSetting(),
                    mSubjectInfo != null && mSubjectInfo.type != 0 ? (ShareUtils.OnCancleFollowListener) () -> showCancelDialog() : null,
                    mSubjectInfo != null && mSubjectInfo.userInfo.userId == userService.getLoginUserId() ? (ShareUtils.OnDeleteListener) () -> doDelete() : null,
                    null, -1, "");

        });
        appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if (verticalOffset < oldOffset) {
                if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    appBarLayout.setExpanded(false, true);
                }
            }
            oldOffset = verticalOffset;
        });
        mCircleDetailComment.setCommentItemClickListener(item -> {
            if (item != null && item.createUserInfo != null) {
                mCommentInfo = null;
                if (userService.getLoginUserId() == item.createUserInfo.userId) {
                    mCommDeleteDialog = DialogUtil.showMessageDialog(NewCircleDetailActivity.this, getString(R.string.del_photo), getString(R.string.delete_notice),
                            getString(R.string.label_btn_ok), getString(R.string.label_btn_cancel), v -> {
                                //TODO 删除我的评论
                                mDiscoverController.doDelComment(NewCircleDetailActivity.this, item.id, ValueConstants.TYPE_COMMENT_LIVESUP);
                                System.out.println(item.id);
                                mCommDeleteDialog.dismiss();
                            }, v -> mCommDeleteDialog.dismiss());
                    mCommDeleteDialog.show();
                } else {  // 回复某人
                    mCommentInfo = item;
                    showInPutMsgDialog("回复" + (TextUtils.isEmpty(item.createUserInfo.nickname) ? "" : item.createUserInfo.nickname) + "：");
                }
            }
        });

        mContentViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                llytBottom.setVisibility(position == 0 ? View.VISIBLE : View.GONE);
                tvCommentTab.setTextAppearance(NewCircleDetailActivity.this, position == 0 ? R.style.TextBold : R.style.TextCommon);
                tvPraiseTab.setTextAppearance(NewCircleDetailActivity.this, position == 1 ? R.style.TextBold : R.style.TextCommon);

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    @Override
    protected void initData() {
        super.initData();
        getDetailData();
    }

    /**
     * 获取详情数据
     */
    public void getDetailData() {
        if (mSubjectId > 0) {
            mDiscoverController.doGetLiveDetail(this, mSubjectId);
        } else {
            ToastUtil.showToast(this, getString(R.string.error_params));
        }
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case ValueConstants.MSG_COMMENT_OK:
                //评论成功
                mCommentInfo = null;
                mSubjectInfo.commentNum += 1;
                updataTabTitles(0);
                break;
            case ValueConstants.MSG_COMMENT_ERROR:
                ToastUtil.showToast(this, StringUtil.handlerErrorCode(this, msg.arg1));
                break;
            case ValueConstants.MSG_LIVE_DETAIL_OK:
                //详情数据
                UgcInfoResult subjectInfo = (UgcInfoResult) msg.obj;
                if (subjectInfo != null) {
                    mSubjectInfo = subjectInfo;
                    setTopDetailData();
                    tvCommentTab.setText(String.format(titles[0], subjectInfo == null ? 0 : (mSubjectInfo.commentNum > 0 ? mSubjectInfo.commentNum : 0)));
                    tvPraiseTab.setText(String.format(titles[1], subjectInfo == null ? 0 : (mSubjectInfo.supportNum > 0 ? mSubjectInfo.supportNum : 0)));
                    ivPraise.setSelected(ValueConstants.TYPE_AVAILABLE.equals(subjectInfo.isSupport));
//                    tvBrowseNum.setText(mSubjectInfo.viewNum + "人浏览");
                } else {
                    ToastUtil.showToast(NewCircleDetailActivity.this, getString(R.string.label_error_live_detail_deleted));
                    finish();
                }
                break;
            case ValueConstants.MSG_LIVE_DEATIL_ERROR:
                ToastUtil.showToast(this, StringUtil.handlerErrorCode(this, msg.arg1));
                break;
            case ValueConstants.MSG_PRAISE_OK:
                llytPraise.setEnabled(true);
                ComSupportInfo comSupportInfo = (ComSupportInfo) msg.obj;
                if (comSupportInfo != null && mSubjectInfo != null) {
                    mSubjectInfo.isSupport = comSupportInfo.isSupport;
                    if (ValueConstants.TYPE_DELETED.equals(mSubjectInfo.isSupport)) {
                        mSubjectInfo.supportNum = mSubjectInfo.supportNum - 1 > 0 ? mSubjectInfo.supportNum - 1 : 0;
                        ivPraise.setSelected(false);
                    } else {
                        mSubjectInfo.supportNum = mSubjectInfo.supportNum + 1;
                        ivPraise.setSelected(true);
                    }
                    EventBus.getDefault().post(new EvBusCircleChangePraise(mSubjectInfo.id, comSupportInfo.isSupport));
                }
                updataTabTitles(1);
                break;
            case ValueConstants.MSG_PRAISE_ERROR:
                llytPraise.setEnabled(true);
                ToastUtil.showToast(NewCircleDetailActivity.this, StringUtil.handlerErrorCode(this, msg.arg1));
                break;
            //删除
            case ValueConstants.MSG_DELETE_DYNAMIC_OK: {
                boolean isSuccess = (boolean) msg.obj;
                if (isSuccess) {
                    EventBus.getDefault().post(new EvBusSubjectInfo(mSubjectInfo, true));
                    finish();
                } else {
                    ToastUtil.showToast(NewCircleDetailActivity.this, getString(R.string.toast_delete_subject_ko));
                }
                break;
            }
            case ValueConstants.MSG_DELETE_DYNAMIC_ERROR:
                ToastUtil.showToast(NewCircleDetailActivity.this, StringUtil.handlerErrorCode(this, msg.arg1));
                break;
            //删除评论
            case ValueConstants.MSG_DELETE_COMMENT: {
                boolean isSuccess = (boolean) msg.obj;
                if (isSuccess) {
                    mSubjectInfo.commentNum = mSubjectInfo.commentNum - 1 > 0 ?
                            mSubjectInfo.commentNum - 1 : 0;
                    updataTabTitles(0);
                    EventBus.getDefault().post(new EvBusSubjectInfo(mSubjectInfo, false));
                } else {
                    ToastUtil.showToast(NewCircleDetailActivity.this, "删除失败");
                }
                break;
            }
            case ValueConstants.MSG_DELETE_COMMENT_ERROR:
                break;
            case ValueConstants.MSG_CANCEL_ATTENTION_OK:
                mSubjectInfo.type = 0;
                setTopDetailData();
                ToastUtil.showToast(this, getString(R.string.toast_cancel_attention));
                EventBus.getDefault().post(new EvBusCircleChangeFollow(mSubjectInfo.userInfo.userId, mSubjectInfo.type));
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
                EventBus.getDefault().post(new EvBusCircleChangeFollow(mSubjectInfo.userInfo.userId, mSubjectInfo.type));
                EventBus.getDefault().post(new EvBusUGCInfoAttention(mSubjectInfo.userInfo.userId, true));
                break;
            case ValueConstants.MSG_ATTENTION_KO:
                ToastUtil.showToast(NewCircleDetailActivity.this, StringUtil.handlerErrorCode(NewCircleDetailActivity.this, msg.arg1));
                break;
        }
    }

    private void setTopDetailData() {
        mDetailTopView.setData(NewCircleDetailActivity.this, mSubjectInfo, true);
//        switch (circleDetailType) {
//            case CircleItemType.COFFEE_VIDEO:
//                ((CircleUgcCoffeeVideoLayout) mDetailTopView).setData(NewCircleDetailActivity.this, mSubjectInfo, true);
//                break;
//            case CircleItemType.LIVE:
//                ((CircleUgcLiveLayout) mDetailTopView).setData(NewCircleDetailActivity.this, mSubjectInfo, true);
//                break;
//            case CircleItemType.UGC_PIC:
//                ((CircleUgcPicLayout) mDetailTopView).setData(NewCircleDetailActivity.this, mSubjectInfo, true);
//                break;
//        }
    }

    private ArrayList<String> setPagerTitles() {
        ArrayList<String> title = new ArrayList<>();
        title.add(String.format(titles[0], 0));
        title.add(String.format(titles[1], 0));
        return title;
    }

    /**
     * 点赞，评论成功后刷新列表及tab
     *
     * @param pos
     */
    private void updataTabTitles(int pos) {
        switch (pos) {
            case 0:
                if (0 != mContentViewPager.getCurrentItem()) {
                    mContentViewPager.setCurrentItem(0);
                }
                if (mSubjectInfo != null && mSubjectInfo.userInfo != null) {
                    mCircleDetailComment.updateData();
                }
                tvCommentTab.setText(String.format(titles[0], mSubjectInfo == null ? 0 : (mSubjectInfo.commentNum > 0 ? mSubjectInfo.commentNum : 0)));
                break;
            case 1:
//                if (1 != mContentViewPager.getCurrentItem()) {
//                    mContentViewPager.setCurrentItem(1);
//                }
                if (mSubjectInfo != null && mSubjectInfo.userInfo != null) {
                    mCircleDetailPrasie.updateData();
                }
                tvPraiseTab.setText(String.format(titles[1], mSubjectInfo == null ? 0 : (mSubjectInfo.supportNum > 0 ? mSubjectInfo.supportNum : 0)));
                break;
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
                getDetailData();
            }
        }
    }

    /**
     * 关注改变
     */
    public void onEvent(EvBusCircleChangeFollow changeFollow) {
        mSubjectInfo.type = changeFollow.type;
    }

    /**
     * 用户被屏蔽通知
     *
     * @param evBusBlackUser
     */
    public void onEvent(EvBusBlack evBusBlackUser) {
        if (null != evBusBlackUser) {
            runOnUiThread(() -> finish());
        }
    }

    /**
     * 屏蔽设置
     */
    private void gotoBlackSetting() {
        if (mSubjectInfo == null) {
            return;
        }
        NavUtils.gotoBlackSetting(this, mSubjectInfo.userInfo.userId, mSubjectInfo.id, mSubjectInfo.userInfo.nickname);
    }

    /**
     * 跳转到举报列表
     */
    private void gotoComplaintListUI() {
        if (mSubjectInfo == null) {
            return;
        }
        NavUtils.gotoComplaintList(this, mSubjectInfo.textContent,
                mSubjectInfo.picList != null && mSubjectInfo.picList.size() > 0 ? new ArrayList<>(mSubjectInfo.picList) : new ArrayList<>(), mSubjectInfo.id);
    }

    private void showCancelDialog() {
        if (mCancelDialog == null) {
            mCancelDialog = DialogUtil.showMessageDialog(this, "是否不再关注此人？", "",
                    getString(R.string.label_btn_ok), getString(R.string.cancel), v -> {
                        doCancelAttention();
                        mCancelDialog.dismiss();
                    }, v -> mCancelDialog.dismiss());
        }
        mCancelDialog.show();
    }

    /**
     * 取消关注
     */
    private void doCancelAttention() {
        if (mSubjectInfo == null || mSubjectInfo.userInfo == null) {
            return;
        }
        mDiscoverController.doCancelAttention(this, mSubjectInfo.userInfo.userId);
    }

    /**
     * 删除本条动态
     */
    private void doDelete() {
        if (mDialog == null) {
            mDialog = DialogUtil.showMessageDialog(NewCircleDetailActivity.this, getString(R.string.del_photo), getString(R.string.delete_notice),
                    getString(R.string.label_btn_ok), getString(R.string.label_btn_cancel), v1 -> {
                        mClubController.delSubjectInfo(NewCircleDetailActivity.this, mSubjectInfo.id);
                        mDialog.dismiss();
                    }, v1 -> {
                        mDialog.dismiss();
                    });
        }
        mDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llyt_go_comment:
                showInPutMsgDialog("");
                break;
            case R.id.llyt_comment:
                if (Math.abs(appBarLayout.getY()) == appBarLayout.getTotalScrollRange()) {
                    mCircleDetailComment.smoothScrollToTop();
                    mCircleDetailPrasie.smoothScrollToTop();
                    appBarLayout.setExpanded(true, true);
                } else {
                    appBarLayout.setExpanded(false, true);
                }
                mContentViewPager.setCurrentItem(0);
                break;
            case R.id.llyt_praise:
                if (userService.isLogin()) {
                    llytPraise.setEnabled(false);
                    String outType = ValueConstants.TYPE_PRAISE_LIVESUP;
                    if (ValueConstants.TYPE_AVAILABLE.equals(mSubjectInfo.isSupport)) {
                        mClubController.doAddNewPraiseToComment(this, mSubjectInfo.id, outType, ValueConstants.UNPRAISE);
                    } else if (ValueConstants.TYPE_DELETED.equals(mSubjectInfo.isSupport)) {
                        mClubController.doAddNewPraiseToComment(this, mSubjectInfo.id, outType, ValueConstants.PRAISE);
                    }
                } else {
                    NavUtils.gotoLoginActivity(this);
                }
                break;
        }
    }

    /**
     * 显示评论输入框
     */
    private void showInPutMsgDialog(String hint) {
        if (inputMsgDialog == null) {
            inputMsgDialog = new InputMsgDialog(this, R.style.Theme_Light_Dialog);
            inputMsgDialog.setOnShowListener(dialog ->
                    mHandler.postDelayed(() -> inputMsgDialog.showSoftInput(hint), 100));
            inputMsgDialog.setSendMsgClickCallBack(text -> {
                doPostComment(text);
            });
            inputMsgDialog.show();
        } else {
            if (!inputMsgDialog.isShowing()) {
                inputMsgDialog.setHint(hint);
                inputMsgDialog.show();
            }
        }
    }

    /**
     * 发表评论
     */
    private void doPostComment(String content) {
        Analysis.pushEvent(this, AnEvent.PageComment,
                new Analysis.Builder().
                        setLat(LocationManager.getInstance().getStorage().getLast_lat()).
                        setLng(LocationManager.getInstance().getStorage().getLast_lng()).
                        setId(String.valueOf(mSubjectId)).
                        setType("动态"));

        if (userService.isLogin()) {
            if (!TextUtils.isEmpty(content)) {
                if (mSubjectInfo == null) return;
                boolean flag = content.contentEquals("\r\n");
                if (!flag && TextUtils.isEmpty(content.trim())) {
                    ToastUtil.showToast(NewCircleDetailActivity.this, getString(R.string.notice_is_all_space));
                    return;
                }
                CommetDetailInfo commetDetailInfo = new CommetDetailInfo();
                commetDetailInfo.outType = ValueConstants.TYPE_COMMENT_LIVESUP;
                commetDetailInfo.userId = userService.getLoginUserId();
                commetDetailInfo.textContent = content;
                commetDetailInfo.outId = mSubjectInfo.id;
                if (mCommentInfo != null && mCommentInfo.createUserInfo != null) {
                    commetDetailInfo.replyToUserId = mCommentInfo.createUserInfo.userId;
                } else {
                    HarwkinLogUtil.error("直播详情    评论的用户对象为---》null");
                }
                mClubController.doPostComment(NewCircleDetailActivity.this, commetDetailInfo);
            } else {
                ToastUtil.showToast(NewCircleDetailActivity.this, "评论不能为空哦");
            }
        } else {
            NavUtils.gotoLoginActivity(NewCircleDetailActivity.this);
        }

    }


    @Override
    public void finish() {
        // 埋点
        Analysis.pushEvent(this, AnEvent.PageTrendClose,
                new Analysis.Builder().
                        setLat(LocationManager.getInstance().getStorage().getLast_lat()).
                        setLng(LocationManager.getInstance().getStorage().getLast_lng()).
                        setId(String.valueOf(mSubjectId)));
        super.finish();
    }
}
