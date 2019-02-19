package com.quanyan.yhy.ui.discovery;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.harwkin.nb.camera.MenuUtils;
import com.harwkin.nb.camera.TimeUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.baseenum.IActionTitleBar;
import com.quanyan.base.util.HarwkinLogUtil;
import com.quanyan.base.util.StringUtil;
import com.quanyan.base.view.BaseTransparentNavView;
import com.quanyan.base.view.customview.pulltorefresh.PullToRefreshBase;
import com.quanyan.base.view.customview.pulltorefresh.PullToRefreshObservableListView;
import com.quanyan.base.yminterface.ErrorViewClick;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.AnalyDataValue;
import com.quanyan.yhy.common.ErrorCode;
import com.yhy.common.eventbus.event.EvBusBlack;
import com.quanyan.yhy.eventbus.EvBusComplaintDynamic;
import com.quanyan.yhy.eventbus.EvBusSubjectInfo;
import com.quanyan.yhy.eventbus.EvBusUGCInfoAttention;
import com.quanyan.yhy.eventbus.EvBusUserLoginState;
import com.quanyan.yhy.libanalysis.AnEvent;
import com.quanyan.yhy.libanalysis.Analysis;
import com.quanyan.yhy.ui.adapter.base.BaseAdapterHelper;
import com.quanyan.yhy.ui.adapter.base.QuickAdapter;
import com.quanyan.yhy.ui.base.utils.DialogUtil;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.base.utils.TCEventHelper;
import com.quanyan.yhy.ui.club.clubcontroller.ClubController;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.discovery.controller.DiscoverController;
import com.quanyan.yhy.ui.discovery.view.TopicDetailsHeadView;
import com.quanyan.yhy.ui.discovery.viewhelper.LiveItemHelper;
import com.yhy.common.beans.net.model.discover.TopicDetailResult;
import com.yhy.common.beans.net.model.discover.TopicInfoResult;
import com.yhy.common.beans.net.model.discover.UgcInfoResult;
import com.yhy.common.beans.net.model.user.UserInfo;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.SPUtils;
import com.yhy.service.IUserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

import static com.umeng.commonsdk.stateless.UMSLEnvelopeBuild.mContext;

/**
 * Created with Android Studio.
 * Title:TopicDetailsActivity
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-6-30
 * Time:16:15
 * Version 1.0
 * Description:
 */
public class TopicDetailsActivity extends BaseActivity {

    public final static String TOPICID = "topicId";
    private final static int PAGESIZE = 10;

    @ViewInject(R.id.tv_order_submit)
    private Button mJoinTopic;

    private BaseTransparentNavView mBaseTransparentNavView;//导航栏布局
    private TopicDetailsHeadView mTopicDetailsHeadView;
    private DiscoverController mController;
    //话题名称
    private String mTopicName;
    private long topicId;
    @ViewInject(R.id.id_stickynavlayout_innerscrollview)
    private PullToRefreshObservableListView mPullListView;
    private ListView mListView;
    private QuickAdapter<UgcInfoResult> mThemeAdapter;
    private int pageNo = 1;

    private UgcInfoResult mAttentionClickTemp;
    private ClubController mClubController;

    @Autowired
    IUserService userService;

    public IUserService getUserService() {
        return userService;
    }

    private boolean mHasNodata = false;
    //TODO 判断是否为onResume状态
    private boolean isResuming = true;
    public static void gotoTopicDetailsActivity(Context context, String topicName, long topicId) {
        Intent intent = new Intent(context, TopicDetailsActivity.class);
        if (!StringUtil.isEmpty(topicName)) {
            intent.putExtra(SPUtils.EXTRA_DATA, topicName);
        }
        intent.putExtra(TOPICID, topicId);
        context.startActivity(intent);
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.activity_topicdetails, null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isResuming = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isResuming = true;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        mPullListView.onRefreshComplete();
        hideErrorView(null);
        switch (msg.what) {
            case ValueConstants.TOPIC_DETAIL_OK://获取话题详情成功
                TopicInfoResult topicInfoResult = (TopicInfoResult) msg.obj;
                if (topicInfoResult != null) {
                    mTopicDetailsHeadView.setTopicContent(topicInfoResult);
                    mBaseTransparentNavView.setTitleText(topicInfoResult.title);
                } else {
                    ToastUtil.showToast(TopicDetailsActivity.this, getResources().getString(R.string.topic_details_forbid));
                    TopicDetailsActivity.this.finish();
                }
                break;
            case ValueConstants.TOPIC_DETAIL_ERROR:
                ToastUtil.showToast(TopicDetailsActivity.this, StringUtil.handlerErrorCode(TopicDetailsActivity.this, msg.arg1));
                showNetErrorView(msg.arg1);
                break;
            case ValueConstants.TOPIC_DETAIL_LIST_OK://获取话题详情列表成功
                TopicDetailResult topicDetailResult = (TopicDetailResult) msg.obj;
                handleFirstPageData(topicDetailResult);
                break;
            case ValueConstants.TOPIC_DETAIL_LIST_ERROR:

                break;
            case ValueConstants.MSG_ATTENTION_OK:
                //关注成功
                if (mAttentionClickTemp != null && mAttentionClickTemp.userInfo != null) {
                    for (UgcInfoResult subjectInfo : mThemeAdapter.getData()) {
                        if (subjectInfo.userInfo != null && mAttentionClickTemp.userInfo.userId == subjectInfo.userInfo.userId) {
                            subjectInfo.type = 1;
                            mThemeAdapter.notifyDataSetChanged();
                        }
                    }
                    EventBus.getDefault().post(new EvBusUGCInfoAttention(mAttentionClickTemp.userInfo.userId, true));
                    mAttentionClickTemp = null;
                }
                break;
            case ValueConstants.MSG_ATTENTION_KO:
                ToastUtil.showToast(TopicDetailsActivity.this, StringUtil.handlerErrorCode(TopicDetailsActivity.this, msg.arg1));
                break;
            case ValueConstants.MSG_CANCEL_ATTENTION_OK:
                //取消关注
                if (mChooseSubjectInfo != null && mChooseSubjectInfo.userInfo != null) {
                    ToastUtil.showToast(this, getString(R.string.toast_cancel_attention));
                    EventBus.getDefault().post(new EvBusUGCInfoAttention(mChooseSubjectInfo.userInfo.userId, false));
                }
                break;
            case ValueConstants.MSG_CANCEL_ATTENTION_KO:
                ToastUtil.showToast(getApplicationContext(), StringUtil.handlerErrorCode(getApplicationContext(), msg.arg1));
                break;
        }
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ViewUtils.inject(this);
        EventBus.getDefault().register(this);
        mTopicName = getIntent().getStringExtra(SPUtils.EXTRA_DATA);
        topicId = getIntent().getLongExtra(TOPICID, -1);
        //TODO服务器增加##
//        if (!TextUtils.isEmpty(mTopicName)) {
//            if (!(mTopicName.startsWith("#") && mTopicName.endsWith("#"))) {
//                mTopicName = "#" + mTopicName + "#";
//            }
//        }
        mListView = mPullListView.getRefreshableView();
        mController = new DiscoverController(this, mHandler);
        mClubController = new ClubController(this, mHandler);

        mBaseTransparentNavView.showBottomDivid(false);
        initHeaderView(mListView);
        setTitleBarBackGround(0, 0xff, 0xaf, 0x00);
        mPullListView.getRefreshableView().setScrollViewCallbacks(new ObservableScrollViewCallbacks() {
            @Override
            public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
                if (scrollY <= 360) {
                    mBaseTransparentNavView.showBottomDivid(false);
                    int alpha = (int) (255 * (scrollY / 360.0));
                    mBaseTransparentNavView.getLeftImg().setImageResource(R.mipmap.scenic_arrow_back_white);
                    mBaseTransparentNavView.getShareView().setImageResource(R.mipmap.icon_top_share_nobg);
                    setTitleBarBackground(Color.argb(alpha, 0xff, 0xff, 0xff));
                    mBaseTransparentNavView.showTitleText();
                    mBaseTransparentNavView.setTitleTextColor2(Color.argb(alpha, 0x00, 0x00, 0x00));
                } else {
                    mBaseTransparentNavView.showBottomDivid(true);
                    setTitleBarBackground(Color.WHITE);
                    mBaseTransparentNavView.getLeftImg().setImageResource(R.mipmap.arrow_back_gray);
                    mBaseTransparentNavView.getShareView().setImageResource(R.mipmap.icon_top_share_nobg);
                }
            }

            @Override
            public void onDownMotionEvent() {

            }

            @Override
            public void onUpOrCancelMotionEvent(ScrollState scrollState) {

            }
        });

        mThemeAdapter = new QuickAdapter<UgcInfoResult>(this, R.layout.cell_live, new ArrayList<UgcInfoResult>()) {
            @Override
            protected void convert(BaseAdapterHelper helper, UgcInfoResult item) {
                if (mHasNodata) {
                    helper.setVisible(R.id.item_live_nodata_layout, true)
                            .setVisible(R.id.cell_live_root_view, false)
                            .setText(R.id.item_live_nodata_text, getString(R.string.topic_details_nodata));
                } else {
                    helper.setVisible(R.id.item_live_nodata_layout, false)
                            .setVisible(R.id.cell_live_root_view, true);
                    LiveItemHelper.handleItem(TopicDetailsActivity.this, helper, item, ValueConstants.TYPE_COMMENT_LIVESUP,
                            ValueConstants.TYPE_PRAISE_LIVESUP, true, TopicDetailsActivity.class.getSimpleName(), new LiveItemHelper.AttentionClickOuter() {
                                @Override
                                public void onAttentionClick(UgcInfoResult ugcInfoResult) {
                                    // TODO: 16/7/11 关注
                                    if (TimeUtil.isFastDoubleClick()) {
                                        return;
                                    }
                                    mAttentionClickTemp = ugcInfoResult;
                                    if (ugcInfoResult != null && ugcInfoResult.userInfo != null) {
                                        analyDataTwo(TopicDetailsActivity.this, ugcInfoResult.userInfo);
                                    }
                                    if (getUserService().isLogin()) {
                                        if (ugcInfoResult != null && ugcInfoResult.userInfo != null) {
                                            mClubController.doAddAttention(TopicDetailsActivity.this, ugcInfoResult.userInfo.userId);
                                        }
                                    } else {
                                        NavUtils.gotoLoginActivity(TopicDetailsActivity.this);
                                    }
                                }
                            });
                }
            }
        };

        mListView.setAdapter(mThemeAdapter);

        initClick();

        getMsg();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void analyDataTwo(Activity mContext, UserInfo userInfo) {
        Map<String, String> map = new HashMap();
        map.put(AnalyDataValue.KEY_MID, userInfo.userId + "");
        map.put(AnalyDataValue.KEY_MNAME, userInfo.nickname);
        TCEventHelper.onEvent(mContext, AnalyDataValue.DISCOVERY_FOLLOW_CLICK, map);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ValueConstants.POST_LIVE:
                if (Activity.RESULT_OK == resultCode) {
                    if (mListView != null) {
                        mListView.setSelection(0);
                        pageNo = 1;
                        getMsg();
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 用户登录退出通知
     *
     * @param evBusUserLoginState
     */
    public void onEvent(EvBusUserLoginState evBusUserLoginState) {
        if (evBusUserLoginState.getUserLoginState() == 0) {
            if (mAttentionClickTemp != null && mAttentionClickTemp.userInfo != null) {
                if (getUserService().isLoginUser(mAttentionClickTemp.userInfo.userId)) {
                    for (UgcInfoResult subjectInfo : mThemeAdapter.getData()) {
                        if (subjectInfo.userInfo != null && mAttentionClickTemp.userInfo.userId == subjectInfo.userInfo.userId) {
                            subjectInfo.type = 1;
                            mThemeAdapter.notifyDataSetChanged();
                        }
                    }
                    mThemeAdapter.notifyDataSetChanged();
                    EventBus.getDefault().post(new EvBusUGCInfoAttention(mAttentionClickTemp.userInfo.userId, true));
                }
//                else {
//                    mClubController.doAddAttention(TopicDetailsActivity.this, mAttentionClickTemp.userInfo.userId);
//                }
            } else {
                pageNo = 1;
                getMsg();
            }
        }
    }

    /**
     * 更新数据列表
     *
     * @param evBusSubjectInfo
     */
    public void onEvent(EvBusSubjectInfo evBusSubjectInfo) {
        if (evBusSubjectInfo != null) {
            boolean delete = evBusSubjectInfo.isDelete();
            if (!delete) {
                UgcInfoResult result = evBusSubjectInfo.getSubjectInfo();
                for (UgcInfoResult subjectInfo : mThemeAdapter.getData()) {
                    if (result.id == subjectInfo.id) {
                        subjectInfo.isSupport = result.isSupport;
                        subjectInfo.commentNum = result.commentNum;
                        subjectInfo.supportNum = result.supportNum;
                        mThemeAdapter.notifyDataSetChanged();
                        break;
                    }
                }
            } else {
                pageNo = 1;
                getMsg();
            }
        }
    }

    /**
     * 点击关注后更新列表数据
     *
     * @param evBusUGCInfoAttention
     */
    public void onEvent(EvBusUGCInfoAttention evBusUGCInfoAttention) {
        if (evBusUGCInfoAttention != null) {
            List<UgcInfoResult> ugcInfoResults = mThemeAdapter.getData();
            for (UgcInfoResult subjectInfo : ugcInfoResults) {
                if (subjectInfo.userInfo != null && evBusUGCInfoAttention.getUserId() == subjectInfo.userInfo.userId) {
                    if (evBusUGCInfoAttention.isFollow()) {
                        subjectInfo.type = 1;
                    } else {
                        subjectInfo.type = 0;
                    }
                }
            }
            mThemeAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 屏蔽功能
     *
     * @param evBusComplaintDynamic
     */
    public void onEvent(EvBusComplaintDynamic evBusComplaintDynamic) {
        if(!isResuming){
            return ;
        }
        if (evBusComplaintDynamic == null || evBusComplaintDynamic.getSubjectInfo() == null) {
            mChooseSubjectInfo = null;
            return;
        }
        mChooseSubjectInfo = evBusComplaintDynamic.getSubjectInfo();
        showComplaintMenuDialog();
    }

    /**
     * 展示投诉菜单
     *
     * @param subjectId
     */
    private UgcInfoResult mChooseSubjectInfo;

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
                                tcEvent(3);
//                            doCancelAttention();
                                showCancelDialog();
                            }
                        }
                    }
                },
                null);
        mComplaintDlg.show();
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

    private Dialog mDialog;

    private void showCancelDialog() {
        View view = View.inflate(this, R.layout.dialog_cancel_follow_confirm, null);
        if (mDialog == null) {
//            mDialog = new DialogBuilder(this)
//                    .setCancelable(true)
//                    .setStyle(R.style.MenuDialogStyle)
//                    .setAnimation(R.anim.abc_fade_in)
//                    .setGravity(Gravity.CENTER)
//                    .build();
            mDialog = DialogUtil.showMessageDialog(this, "是否不再关注此人？", "",
                    getString(R.string.label_btn_ok), getString(R.string.cancel), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            doCancelAttention();
                            mDialog.dismiss();
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mDialog.dismiss();
                        }
                    });
        }
        mDialog.show();
    }

    /**
     * 取消点赞
     */
    private void doCancelAttention() {
        if (mChooseSubjectInfo == null || mChooseSubjectInfo.userInfo == null) {
            return;
        }
        mController.doCancelAttention(this, mChooseSubjectInfo.userInfo.userId);
    }

    /**
     * 跳转到举报列表
     */
    private void gotoComplaintListUI() {
        if (mChooseSubjectInfo == null) {
            return;
        }
        NavUtils.gotoComplaintList(this, mChooseSubjectInfo.textContent,
                mChooseSubjectInfo.picList != null && mChooseSubjectInfo.picList.size() > 0 ? new ArrayList<>(mChooseSubjectInfo.picList) : new ArrayList<>(), mChooseSubjectInfo.id);
    }

    /**
     * 屏蔽设置
     */
    private void gotoBlackSetting() {
        if (mChooseSubjectInfo == null) {
            return;
        }
        HarwkinLogUtil.info("subjectInfo.id = " + mChooseSubjectInfo.id);
        NavUtils.gotoBlackSetting(this, mChooseSubjectInfo.userInfo.userId, mChooseSubjectInfo.id, mChooseSubjectInfo.userInfo.nickname);
    }

    private void initClick() {
        //参与话题
        mJoinTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //统计事件
                Analysis.pushEvent(mContext, AnEvent.JOIN_TOPIC);
                Map<String, String> map = new HashMap();
                map.put(AnalyDataValue.KEY_ID, topicId + "");
                map.put(AnalyDataValue.KEY_NAME, mTopicName);
                TCEventHelper.onEvent(TopicDetailsActivity.this, AnalyDataValue.TOPIC_PARTAKE, map);
                NavUtils.gotoAddLiveActivity(TopicDetailsActivity.this, mTopicName);
            }
        });

        mPullListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ObservableListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ObservableListView> refreshView) {
                pageNo = 1;
                getMsg();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ObservableListView> refreshView) {
                pageNo++;
                getMsg();
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int headerCount = mListView.getHeaderViewsCount();
                if (position >= headerCount) {
                    UgcInfoResult ugcInfoResult = mThemeAdapter.getItem(position - headerCount);
                    NavUtils.gotoLiveDetailActivity(TopicDetailsActivity.this, ugcInfoResult.id, ugcInfoResult,
                            ValueConstants.TYPE_COMMENT_LIVESUP, ValueConstants.TYPE_PRAISE_LIVESUP, false);
                }
            }
        });

    }

    private void getMsg() {
        if (pageNo == 1) {
            mController.doGetTopicInfo(this, mTopicName);
        }
        mController.doGetTopicUGCPageList(this, mTopicName, pageNo, PAGESIZE);
    }

    @Override
    public View onLoadNavView() {
        mBaseTransparentNavView = new BaseTransparentNavView(this);
        return mBaseTransparentNavView;
    }

    @Override
    public boolean isTopCover() {
        return true;
    }

    private void initHeaderView(ListView listView) {
        mTopicDetailsHeadView = new TopicDetailsHeadView(this);
        listView.addHeaderView(mTopicDetailsHeadView);
    }

    /**
     * 处理首页配置数据
     */
    private UgcInfoResult mEmptyResult;

    private void handleFirstPageData(TopicDetailResult topicDetailResult) {
        if (pageNo == 1) {
            if (topicDetailResult.ugcInfoList != null && topicDetailResult.ugcInfoList.size() != 0) {
                mThemeAdapter.replaceAll(topicDetailResult.ugcInfoList);
            } else {
                mThemeAdapter.clear();
            }
            if (mThemeAdapter.getCount() == 0) {
                mHasNodata = true;
                mEmptyResult = new UgcInfoResult();
                mThemeAdapter.add(mEmptyResult);
            } else {
                mHasNodata = false;
            }
        } else {
            mHasNodata = false;
            if (topicDetailResult.ugcInfoList != null && topicDetailResult.ugcInfoList.size() != 0) {
                if (mEmptyResult != null) {
                    mEmptyResult = null;
                    mThemeAdapter.clear();
                }
                mThemeAdapter.addAll(topicDetailResult.ugcInfoList);
            } else {
                ToastUtil.showToast(this, getString(R.string.no_more));
            }
        }
    }

    private void showNetErrorView(int arg1) {
        showErrorView(null, ErrorCode.NETWORK_UNAVAILABLE == arg1 ?
                IActionTitleBar.ErrorType.NETUNAVAILABLE : IActionTitleBar.ErrorType.ERRORNET, "", "", "", new ErrorViewClick() {
            @Override
            public void onClick(View view) {
                getMsg();
            }
        });
    }

    /**
     * 屏蔽成功后接受事件刷新列表
     * @param event
     */
    public void onEvent(EvBusBlack event){
          pageNo = 1;
          getMsg();
    }
}
