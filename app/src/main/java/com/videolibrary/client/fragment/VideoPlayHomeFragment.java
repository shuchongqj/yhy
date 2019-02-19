package com.videolibrary.client.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.harwkin.nb.camera.MenuUtils;
import com.quanyan.base.util.ScreenSize;
import com.quanyan.base.util.StringUtil;
import com.quanyan.base.view.customview.NoScrollGridView;
import com.quanyan.base.view.customview.pulltorefresh.PullToRefreshBase;
import com.quanyan.base.view.customview.pulltorefresh.PullToRefreshScrollView;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.AnalyDataValue;
import com.quanyan.yhy.libanalysis.AnEvent;
import com.quanyan.yhy.libanalysis.Analysis;
import com.quanyan.yhy.ui.adapter.base.BaseAdapterHelper;
import com.quanyan.yhy.ui.adapter.base.QuickAdapter;
import com.quanyan.yhy.ui.base.utils.DialogUtil;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.base.utils.TCEventHelper;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.videolibrary.client.activity.HorizontalVideoClientActivity;
import com.videolibrary.client.activity.VerticalVideoClientActivity;
import com.videolibrary.controller.LiveController;
import com.videolibrary.metadata.LiveTypeConstants;
import com.videolibrary.utils.IntentUtil;
import com.yhy.common.base.NoLeakHandler;
import com.yhy.common.beans.net.model.discover.UgcInfoResult;
import com.yhy.common.beans.net.model.master.TalentInfo;
import com.yhy.common.beans.net.model.msg.LiveRecordAPIPageResult;
import com.yhy.common.beans.net.model.msg.LiveRecordResult;
import com.yhy.common.beans.net.model.msg.LiveRoomResult;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.CommonUtil;
import com.yhy.common.utils.SPUtils;
import com.yhy.imageloader.ImageLoadManager;
import com.yhy.router.YhyRouter;
import com.yhy.service.IUserService;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with Android Studio.
 * Title:VideoPlayHomeFragment
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:赵晓坡
 * Date:16/8/8
 * Time:10:23
 * Version 1.1.0
 */
public class VideoPlayHomeFragment extends Fragment implements NoLeakHandler.HandlerCallback, /*PullToRefreshBase.OnRefreshListener2<HeaderGridView>*/
        PullToRefreshBase.OnRefreshListener2<ScrollView> {

    protected NoLeakHandler mHandler;
    /**
     * the master's userID
     */
    private long mAnchorId = -1;

    /**
     * set text for announcement
     */
    private TextView mAnnouncementText;
    private TextView mLiveTitle;

    //    private PullToRefreshHeaderGridView mPullToRefreshObserableGridView;
    private PullToRefreshScrollView mPullToRefreshObserableGridView;
    //    private HeaderGridView mNoScrollGridView;
    private NoScrollGridView mNoScrollGridView;
    private QuickAdapter<LiveRecordResult> mLiveRecordResultQuickAdapter;

    private List<String> mFetchTypes = new ArrayList<>();
    private List<String> mLiveRecordStatus = new ArrayList<>();

    private LiveController mLiveController;

    private boolean isRefresh = false;
    private boolean hasNext = true;
    private int mPageIndex = 1;

    private int mScreenWidth;

    @Autowired
    IUserService userService;

    public static VideoPlayHomeFragment getInstance(final long userId) {
        VideoPlayHomeFragment videoPlayHomeFragment = new VideoPlayHomeFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(IntentUtil.BUNDLE_ANCHORID, userId);
        videoPlayHomeFragment.setArguments(bundle);
        return videoPlayHomeFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        YhyRouter.getInstance().inject(this);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fg_video_play_home_view, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mHandler = new NoLeakHandler(this);
        mLiveController = LiveController.getInstance();
        Bundle bundle = getArguments();
        if (bundle != null) {
            mAnchorId = bundle.getLong(IntentUtil.BUNDLE_ANCHORID, -1);
        }

        mFetchTypes.add(LiveTypeConstants.LIVE_REPLAY);
        mLiveRecordStatus.add(LiveTypeConstants.NORMAL_LIVE);

        mScreenWidth = ScreenSize.getScreenWidth(getActivity().getApplicationContext());
        initView(getView(), savedInstanceState);
        fetchData();
        fetchMasterData();
        onPullDownToRefresh(mPullToRefreshObserableGridView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.setValid(true);
        }
    }

    //    private void addHeaderView(HeaderGridView observableGridView){
//        View headerView = View.inflate(getActivity(), R.layout.fg_video_home_grid_header_view, null);
//        ((RecommendView) headerView.findViewById(R.id.fg_live_play_home_announcement_view)).setRecommendTitle("直播公告");
//        ((RecommendView) headerView.findViewById(R.id.fg_live_play_home_replay_view)).setRecommendTitle("Ta的回放");
//
//        mLiveTitle = (TextView) headerView.findViewById(R.id.fg_video_home_top_title);
//        mAnnouncementText = (TextView) headerView.findViewById(R.id.fg_live_play_home_announcement_text);
//        headerView.findViewById(R.id.fg_video_complaint_img_layout).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // TODO: 8/31/16 举报，屏蔽
//                showComplaintMenuDialog();
//            }
//        });
//
//        observableGridView.addHeaderView(headerView);
//    }

    private void initHeaderView(View view) {
//        ((RecommendView) view.findViewById(R.id.fg_live_play_home_announcement_view)).setRecommendTitle("直播公告");
//        ((RecommendView) view.findViewById(R.id.fg_live_play_home_replay_view)).setRecommendTitle("Ta的回放");

        mLiveTitle = (TextView) view.findViewById(R.id.fg_video_home_top_title);
        mAnnouncementText = (TextView) view.findViewById(R.id.fg_live_play_home_announcement_text);
        view.findViewById(R.id.fg_video_complaint_img_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 8/31/16 举报，屏蔽
                showComplaintMenuDialog();
            }
        });

        if(!TextUtils.isEmpty(mLiveRoomTitle)){
            mLiveTitle.setText(mLiveRoomTitle);
        }

        judgeIsSelf();
    }

    /**
     * 当传进来的userid为空或者-1时，取得liverecord的时候需要重置
     *
     * @param userId
     */
    public void resetUserId(long userId) {
        if (isDetached() && getActivity() == null) {
            return;
        }
        mAnchorId = userId;
        fetchData();
        fetchMasterData();
        onPullDownToRefresh(mPullToRefreshObserableGridView);
        judgeIsSelf();
    }

    /**
     * 是否是自己
     */
    private void judgeIsSelf(){
        if (userService.getLoginUserId() == mAnchorId) {
            //主播自己不能关注自己
            getView().findViewById(R.id.fg_video_complaint_img_layout).setVisibility(View.INVISIBLE);
        }else {
            getView().findViewById(R.id.fg_video_complaint_img_layout).setVisibility(View.VISIBLE);
        }
    }

    private String mLiveRoomTitle;
    /**
     * 设置直播间的标题
     * @param liveTitle
     */
    public void setLiveRoomTitle(String liveTitle) {
        if (isDetached() && getActivity() == null) {
            return;
        }
        mLiveRoomTitle = liveTitle;
        if(mLiveTitle != null){
            mLiveTitle.setText(mLiveRoomTitle);
        }
    }

    public void fetchMasterData(){
        if (mAnchorId > 0 && getActivity() != null) {
            mLiveController.getMasterDetail(getActivity(), mHandler, mAnchorId);
        }
    }
    /**
     * network connection
     */
    private void fetchData() {
        if (mAnchorId > 0 && getActivity() != null) {
            mLiveController.getLiveRoomInfo(getActivity(), mAnchorId, mHandler);
        }
    }

    private void fetchGridData(int pageIndex) {
        if (getActivity() == null){
            return;
        }
        mLiveController.getLivelistByUserId(getActivity(), mHandler,
                mAnchorId,
                mFetchTypes,
                mLiveRecordStatus,
                pageIndex,
                4);
    }

    @Override
    public void handleMessage(Message msg) {
        if(isDetached() || getActivity() == null){
            return;
        }
        mPullToRefreshObserableGridView.onRefreshComplete();
        switch (msg.what) {
            case ValueConstants.MSG_GET_MASTER_DETAIL_OK:
                //the master detail info
                TalentInfo talentInfo = (TalentInfo) msg.obj;
                handleTheMasterDetailInfo(talentInfo);
                if (getActivity() != null && mAnchorId > 0 ) {
                    if (getActivity() instanceof HorizontalVideoClientActivity){
                        ((HorizontalVideoClientActivity) getActivity()).setFollowState(talentInfo);
                    }

                    if (getActivity() instanceof VerticalVideoClientActivity){
                        ((VerticalVideoClientActivity) getActivity()).setFollowState(talentInfo);
                    }
                }
                break;
            case ValueConstants.MSG_GET_MASTER_DETAIL_KO:
                //fail to fetch the master detail info
                ToastUtil.showToast(getActivity(), StringUtil.handlerErrorCode(getActivity().getApplicationContext(), msg.arg1));
                break;
            case LiveController.MSG_LIVE_ROOM_INFO_OK:
                LiveRoomResult liveRoomResult = (LiveRoomResult) msg.obj;
                if (liveRoomResult != null) {
//                    mLiveTitle.setText(TextUtils.isEmpty(liveRoomResult.liveTitle) ? "" : liveRoomResult.liveTitle);
                    mAnnouncementText.setText(TextUtils.isEmpty(liveRoomResult.roomNotice) ? "" : liveRoomResult.roomNotice);
                }
                break;
            case LiveController.MSG_LIVE_ROOM_INFO_ERROR:
                ToastUtil.showToast(getActivity(), StringUtil.handlerErrorCode(getActivity().getApplicationContext(), msg.arg1));
                break;
            case LiveController.MSG_LIVE_LIST_OK:
                LiveRecordAPIPageResult liveRecordAPIPageResult = (LiveRecordAPIPageResult) msg.obj;
                if (liveRecordAPIPageResult != null) {
                    hasNext = liveRecordAPIPageResult.hasNext;
                }
                handleListData(liveRecordAPIPageResult);
                break;
            case LiveController.MSG_LIVE_LIST_ERROR:
                ToastUtil.showToast(getActivity(), StringUtil.handlerErrorCode(getActivity().getApplicationContext(), msg.arg1));
                break;
            case ValueConstants.MSG_HAS_NO_DATA:
                ToastUtil.showToast(getActivity(), getString(R.string.no_more_data));
                break;
        }
    }

    /**
     * onPullDownToRefresh will be called only when the user has Pulled from
     * the start, and released.
     *
     * @param refreshView
     */
    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        if (mAnchorId > 0) {
            mPageIndex = 1;
            isRefresh = true;
            fetchGridData(mPageIndex);
        }else{
            mPullToRefreshObserableGridView.onRefreshComplete();
        }
    }

    /**
     * onPullUpToRefresh will be called only when the user has Pulled from
     * the end, and released.
     *
     * @param refreshView
     */
    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        if (mAnchorId > 0) {
            isRefresh = false;
            if (hasNext) {
                mPageIndex++;
                fetchGridData(mPageIndex);
            } else {
                Message.obtain(mHandler, ValueConstants.MSG_HAS_NO_DATA).sendToTarget();
            }
        }else{
            mPullToRefreshObserableGridView.onRefreshComplete();
        }
    }

    private void handleListData(LiveRecordAPIPageResult liveRecordAPIPageResult) {
        if (isRefresh) {
            if (liveRecordAPIPageResult != null && liveRecordAPIPageResult.list != null
                    && liveRecordAPIPageResult.list.size() > 0) {
                mLiveRecordResultQuickAdapter.replaceAll(liveRecordAPIPageResult.list);
            } else {

            }
        } else {
            if (liveRecordAPIPageResult != null && liveRecordAPIPageResult.list != null
                    && liveRecordAPIPageResult.list.size() > 0) {
                mLiveRecordResultQuickAdapter.addAll(liveRecordAPIPageResult.list);
            }
        }
    }

    /**
     * set the master properties
     *
     * @param talentInfo
     */
    public void handleTheMasterDetailInfo(TalentInfo talentInfo) {
        ((TextView) getView().findViewById(R.id.fg_video_home_top_nickname)).setText(TextUtils.isEmpty(talentInfo.nickName) ? "" : talentInfo.nickName);
        ImageView userHead = (ImageView) getView().findViewById(R.id.fg_video_home_top_avatar);
        if(!TextUtils.isEmpty(talentInfo.avatar)) {
//            BaseImgView.loadimg(userHead, talentInfo.avatar, R.mipmap.icon_default_avatar,
//                    R.mipmap.icon_default_avatar,
//                    R.mipmap.icon_default_avatar,
//                    ImageScaleType.EXACTLY,
//                    180, 180, 180);
            ImageLoadManager.loadCircleImage(CommonUtil.getImageFullUrl(talentInfo.avatar),
                    R.mipmap.icon_default_avatar, 180, 180, userHead);

        }else{
            userHead.setImageResource(R.mipmap.icon_default_avatar);
        }

        switch (talentInfo.gender){
            case ValueConstants.TYPE_MALE_OR_FEMAIL :
                break;
            case ValueConstants.TYPE_MALE :
                ((ImageView) getView().findViewById(R.id.fg_video_home_top_sex)).setImageResource(R.mipmap.master_men);
                break;
            case ValueConstants.TYPE_FEMAIL :
                ((ImageView) getView().findViewById(R.id.fg_video_home_top_sex)).setImageResource(R.mipmap.master_women);
                break;
        }

        TextView followNum = ((TextView) getView().findViewById(R.id.fg_video_home_top_attention_num));
        TextView fansNum = ((TextView) getView().findViewById(R.id.fg_video_home_top_fans_num));

        followNum.setText(getNum(talentInfo.followCount));
        fansNum.setText(getNum(talentInfo.fansCount));

        userHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavUtils.gotoMasterHomepage(v.getContext(), mAnchorId);
            }
        });
    }

    /**
     * while the Fragment resumed, initialize the View;
     *
     * @param view               root view, {@link Fragment#getView()}
     * @param savedInstanceState
     */
    private void initView(View view, Bundle savedInstanceState) {
//        mPullToRefreshObserableGridView = (PullToRefreshHeaderGridView) view.findViewById(R.id.fg_video_home_pull_gridview);
        mPullToRefreshObserableGridView = (PullToRefreshScrollView) view.findViewById(R.id.fg_video_home_pull_gridview);
        mPullToRefreshObserableGridView.setMode(PullToRefreshBase.Mode.BOTH);
//        mNoScrollGridView = mPullToRefreshObserableGridView.getRefreshableView();
        mNoScrollGridView = (NoScrollGridView) view.findViewById(R.id.fg_video_play_home_grid_view);
//        mNoScrollGridView.setNumColumns(2);
//        mNoScrollGridView.setHorizontalSpacing(20);
//        mNoScrollGridView.setPadding(ScreenSize.convertDIP2PX(getActivity().getApplicationContext(), 15), 0,
//                ScreenSize.convertDIP2PX(getActivity().getApplicationContext(), 15),0);
//        mNoScrollGridView.setVerticalSpacing(20);
//        addHeaderView(mNoScrollGridView);
        initHeaderView(view);
        mLiveRecordResultQuickAdapter = new QuickAdapter<LiveRecordResult>(getActivity(), R.layout.item_video_replay_grid) {
            @Override
            protected void convert(BaseAdapterHelper helper, LiveRecordResult item) {
                RelativeLayout relativeLayout = helper.getView(R.id.item_video_replay_bg_img_layout);
                relativeLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        (mScreenWidth - 150) / 2,
                        (((mScreenWidth - 150) / 2) * 194) / 344
                ));
                helper.setImageUrl(R.id.item_video_replay_bg_img, item.liveCover, 360, 280, R.mipmap.icon_default_310_180);
                String num;
                if (item.viewCount >= 999000) {
                    helper.setText(R.id.item_video_replay_look_num, "999万+");//直播在线人数
                } else if (item.viewCount >= 10000) {
                    num = (new DecimalFormat("#.##").format(item.viewCount / 10000.0f));
                    helper.setText(R.id.item_video_replay_look_num, num + "万");
                } else {
                    helper.setText(R.id.item_video_replay_look_num, item.viewCount + "");
                }
                helper.setText(R.id.item_video_replay_title, TextUtils.isEmpty(item.liveTitle) ? "" : item.liveTitle);
                if (item.userInfo != null) {
                    helper.setText(R.id.item_video_replay_nickname, TextUtils.isEmpty(item.userInfo.nickname) ? "" : item.userInfo.nickname);
                }
            }
        };
        mNoScrollGridView.setColumnWidth((mScreenWidth - 150) / 2);
        mNoScrollGridView.setAdapter(mLiveRecordResultQuickAdapter);
        mNoScrollGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View headerView, int position, long id) {
//                int headerCount = mNoScrollGridView.getHeaderViewCount();
//                if(position >= headerCount) {
//                    LiveRecordResult liveRecordResult = mLiveRecordResultQuickAdapter.getItem(position - headerCount);
                LiveRecordResult liveRecordResult = mLiveRecordResultQuickAdapter.getItem(position);
//                if(liveRecordResult.replayUrls != null && liveRecordResult.replayUrls.size() > 0) {
                tcEvent(liveRecordResult.liveId, mAnchorId);
                IntentUtil.startVideoClientActivity(
                        liveRecordResult.liveId, mAnchorId, false,liveRecordResult.liveScreenType);
                //事件统计
                Analysis.pushEvent(getActivity(), AnEvent.LIVE_VIDEO,liveRecordResult.liveTitle);
                getActivity().finish();
//              }
//          }
            }
        });
        mPullToRefreshObserableGridView.setOnRefreshListener(this);
    }

    private void tcEvent(long liveId, long mAnchorId) {
        Map<String, String> map = new HashMap<>();
        map.put(AnalyDataValue.KEY_LIVE_ID, liveId + "");
        map.put(AnalyDataValue.KEY_UID, mAnchorId + "");
        TCEventHelper.onEvent(getActivity(), AnalyDataValue.LIVE_DETAIL_VIDEO_ITEM_CLICK, map);
    }

    /**
     * format the follower and fans's number
     *
     * @param num
     * @return
     */
    private String getNum(long num) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        if (num > 9999) {
            return decimalFormat.format(num / 10000.0) + "万";
        } else {
            return num + "";
        }
    }

    public void showComplaintMenuDialog() {
//        View view = View.inflate(getActivity(), R.layout.view_live_home_complaint_dialog, null);
//        final PopView popView = new PopView(getActivity(), view, R.anim.slide_in_from_bottom, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        view.findViewById(R.id.view_live_home_complaint_layout).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                gotoComplaintListUI();
//            }
//        });
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                popView.dismiss();
//            }
//        });
//        popView.showPopViewBottom(mAnnouncementText);
        List<String> menus = new ArrayList<>();
//        if (mChooseSubjectInfo.type != 0) {
//        menus.add(getString(R.string.label_menu_cancel_attention));
//        }
        if (!userService.isLoginUser(mAnchorId)) {
            menus.add(getString(R.string.label_menu_accusation));
//            menus.add(getString(R.string.label_menu_complaint));
        }
        final String[] mMenus = StringUtil.listToStrings(menus);
        Dialog mComplaintDlg = MenuUtils.showAlert(getActivity(),
                null,
                mMenus,
                null,
                new MenuUtils.OnAlertSelectId() {
                    @Override
                    public void onClick(int whichButton) {
                        if (whichButton != mMenus.length) {
                            if (getString(R.string.label_menu_accusation).equals(mMenus[whichButton])) {
                                gotoComplaintListUI();
                            } else if (getString(R.string.label_menu_complaint).equals(mMenus[whichButton])) {
                                gotoBlackSetting();
                            } else if (getString(R.string.label_menu_cancel_attention).equals(mMenus[whichButton])) {
                                showCancelDialog();
                            }
                        }
                    }
                },
                null);
        mComplaintDlg.show();
    }

    /**
     * 取消关注
     */
    private void doCancelAttention() {
//        mDiscoverController.doCancelAttention(getActivity(), mAnchorId);
    }

    /**
     * 跳转到举报列表
     */
    private void gotoComplaintListUI() {
        UgcInfoResult mChooseSubjectInfo = new UgcInfoResult();
        mChooseSubjectInfo.id = mAnchorId;
        NavUtils.gotoComplaintList(getActivity(), mChooseSubjectInfo.textContent,
                mChooseSubjectInfo.picList != null && mChooseSubjectInfo.picList.size() > 0 ? new ArrayList<>(mChooseSubjectInfo.picList) : new ArrayList<>(), mChooseSubjectInfo.id);
    }

    /**
     * 屏蔽设置
     */
    private void gotoBlackSetting() {
//        NavUtils.gotoBlackSetting(getActivity(), mChooseSubjectInfo);
    }

    private Dialog mDialog;

    private void showCancelDialog() {
        if (mDialog == null) {
            mDialog = DialogUtil.showMessageDialog(getActivity(), "是否不再关注此人？", "",
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
}
