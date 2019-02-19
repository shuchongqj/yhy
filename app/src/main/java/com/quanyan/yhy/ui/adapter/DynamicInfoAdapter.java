package com.quanyan.yhy.ui.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.nfc.FormatException;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.harwkin.nb.camera.ImageUtils;
import com.harwkin.nb.camera.MenuUtils;
import com.harwkin.nb.camera.TimeUtil;
import com.newyhy.config.CircleItemType;
import com.quanyan.base.util.DateUtil;
import com.quanyan.base.util.HarwkinLogUtil;
import com.quanyan.base.util.ScreenSize;
import com.quanyan.base.util.StringUtil;
import com.quanyan.base.view.customview.NoScrollGridView;

import com.quanyan.yhy.R;
import com.quanyan.yhy.eventbus.EvBusSubjectInfo;
import com.quanyan.yhy.libanalysis.AnEvent;
import com.quanyan.yhy.libanalysis.Analysis;
import com.quanyan.yhy.ui.adapter.base.QuickAdapter;
import com.quanyan.yhy.ui.base.utils.DialogUtil;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.club.clubcontroller.ClubController;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.discovery.LiveDetailActivity;
import com.quanyan.yhy.ui.discovery.TopicDetailsActivity;
import com.quanyan.yhy.ui.discovery.controller.DiscoverController;
import com.quanyan.yhy.ui.discovery.view.CopyDialog;
import com.quanyan.yhy.ui.discovery.viewhelper.GridViewUtils;
import com.quanyan.yhy.ui.discovery.viewhelper.LiveItemHelper;
import com.quanyan.yhy.ui.master.activity.MasterHomepageActivity;
import com.quanyan.yhy.util.HealthCircleTextUtil;
import com.smart.sdk.api.resp.Api_SNSCENTER_TagInfo;
import com.smart.sdk.api.resp.Api_SNSCENTER_UgcInfoResult;
import com.videolibrary.utils.IntentUtil;
import com.yhy.common.base.YHYBaseApplication;
import com.yhy.common.beans.net.model.VideoInfo;
import com.yhy.common.beans.net.model.club.POIInfo;
import com.yhy.common.beans.net.model.comment.ComSupportInfo;
import com.yhy.common.beans.net.model.comment.CommentInfo;
import com.yhy.common.beans.net.model.discover.UgcInfoResult;
import com.yhy.common.beans.net.model.trip.TagInfo;
import com.yhy.common.beans.net.model.user.UserInfo;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.CommonUtil;
import com.yhy.common.utils.SPUtils;
import com.yhy.imageloader.ImageLoadManager;
import com.yhy.router.YhyRouter;
import com.yhy.service.IUserService;

import org.akita.util.AndroidUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

import static com.quanyan.yhy.ui.discovery.viewhelper.LiveItemHelper.STATUS_LIVE;
import static com.quanyan.yhy.ui.discovery.viewhelper.LiveItemHelper.STATUS_LIVE_OVER;
import static com.quanyan.yhy.ui.discovery.viewhelper.LiveItemHelper.TYPE_LIVE_CONTENT;
import static com.quanyan.yhy.ui.discovery.viewhelper.LiveItemHelper.setPicPreviewAdapter;


/**
 * Created by Administrator on 2018/1/23.
 */

public class
DynamicInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
private Context mContext;
    private  List<Api_SNSCENTER_UgcInfoResult> ugcInfoList;
    private   ClubController mClubController;
    private String type;
    private boolean isClick = false;
    public static final int LIVE_CONTENT=1;
    public static final int VIDEO_CONTENT=2;
    public static final int OTHER_CONTENT=3;
    public static final int ITEM_FOOT=4;
    private String source;
    private UgcInfoResult mUgcInfoResult;
    private UgcInfoResult mChooseSubjectInfo;
    private DiscoverController mDiscoverController;
    private UgcInfoResult mSubjectInfo;
    private UgcInfoResult mAttentionClickTemp;
    private EditText mCommentText;
    private CommentInfo mCommentInfo;//回复评论
    private Dialog mCommDeleteDialog;//评论删除提示
    private LinearLayout mDetailTopParentView;
    private View mDetailTopView;
    private String mTypeComment;//类型定义，直播，动态
    private String mTypePraise;//类型定义，直播，动态
    private OnStatusChangedListener mOnStatusChangedListener;

    @Autowired
    IUserService userService;

    public DynamicInfoAdapter(Context context,OnStatusChangedListener listener){
        YhyRouter.getInstance().inject(this);
        mContext = context;
        mClubController = new ClubController(context, mHandler);
        mDiscoverController = new DiscoverController(mContext, mHandler);
        source= DynamicInfoAdapter.class.getSimpleName();
        mOnStatusChangedListener= listener;

    }

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            isClick = false;
            switch (msg.what) {
                case ValueConstants.MSG_COMMENT_OK:
                    //评论成功
                    AndroidUtil.hideIME((Activity) mContext, true);
                    mCommentInfo = null;
                    mCommentText.setText("");
                    mCommentText.setHint(R.string.label_post_comment);
                    if(mOnStatusChangedListener!=null){
                        mOnStatusChangedListener.onStatusChanged();
                    }
                    mSubjectInfo.commentNum += 1;
                    EventBus.getDefault().post(new EvBusSubjectInfo(mSubjectInfo, false));
                    break;
                case ValueConstants.MSG_COMMENT_ERROR:
                    AndroidUtil.hideIME((Activity) mContext, true);
                    ToastUtil.showToast(mContext, StringUtil.handlerErrorCode(mContext, msg.arg1));
                    break;
                case ValueConstants.MSG_LIVE_DETAIL_OK:
                    //直播详情数据
//                    UgcInfoResult subjectInfo = (UgcInfoResult) msg.obj;
//                    if (subjectInfo != null) {
//
//                        if (isInited) {
//                            LiveDetailCommentFragment liveDetailComment = (LiveDetailCommentFragment) mFragments.get(0);
//                            if (subjectInfo.userInfo != null) {
//                                liveDetailComment.updateData(true, subjectInfo.userInfo.userId);
//                            } else {
//                                liveDetailComment.updateData(true, 0);
//                            }
//                            LiveDetailApprasieFragment liveDetailApprasie = (LiveDetailApprasieFragment) mFragments.get(1);
//                            liveDetailApprasie.updateData(true);
//                        }
//
//                        //暂无评论，显示软键盘
//                        if (mSubjectInfo == null) {
//                            if (subjectInfo.contentType == LiveItemHelper.TYPE_LIVE_CONTENT) {
//                                mDetailTopView = View.inflate(mContext, R.layout.cell_master_circle_live, null);
//                                mDetailTopParentView.addView(mDetailTopView, 0);
//                            } else if (!TextUtils.isEmpty(subjectInfo.videoPicUrl)) {
//                                mDetailTopView = View.inflate(mContext, R.layout.cell_master_circle_video, null);
//                                mDetailTopParentView.addView(mDetailTopView, 0);
//                            } else {
//                                mDetailTopView = View.inflate(mContext, R.layout.cell_master_circle_ugc, null);
//                                mDetailTopParentView.addView(mDetailTopView, 0);
//                            }
//                            judgeIsSelf(subjectInfo);
//                            initTopData(subjectInfo);
//                            initial(subjectInfo);
//                        }
//                        mSubjectInfo = subjectInfo;
//                        updateTabTitles(mSubjectInfo);
//                    } else {
//                        ToastUtil.showToast(mContext, mContext.getString(R.string.label_error_live_detail_deleted));
//                        ((Activity)mContext).finish();
//                    }
                    break;
                case ValueConstants.MSG_LIVE_DEATIL_ERROR:
                    ToastUtil.showToast(mContext, StringUtil.handlerErrorCode(mContext, msg.arg1));
                    break;
                case ValueConstants.MSG_PRAISE_OK:
                    if(mOnStatusChangedListener!=null){
                        mOnStatusChangedListener.onStatusChanged();
                    }
//                    ComSupportInfo comSupportInfo = (ComSupportInfo) msg.obj;
//                    if (comSupportInfo != null && mSubjectInfo != null) {
//                        mSubjectInfo.isSupport = comSupportInfo.isSupport;
//                        if (ValueConstants.TYPE_DELETED.equals(mSubjectInfo.isSupport)) {
//                            mSubjectInfo.supportNum = mSubjectInfo.supportNum - 1 > 0 ? mSubjectInfo.supportNum - 1 : 0;
//                        } else {
//                            mSubjectInfo.supportNum = mSubjectInfo.supportNum + 1;
//                        }
//                        //点赞数据
//                        judgeThePraiseState(mSubjectInfo);
//                        EventBus.getDefault().post(new EvBusSubjectInfo(mSubjectInfo, false));
//                    }
//                    updataTabTitles(1);
                    break;
                case ValueConstants.MSG_PRAISE_ERROR:
                    ToastUtil.showToast(mContext, StringUtil.handlerErrorCode(mContext, msg.arg1));
                    break;
                //删除
                case ValueConstants.MSG_DELETE_DYNAMIC_OK: {
                    boolean isSuccess = (boolean) msg.obj;
                    if (isSuccess) {
                        EventBus.getDefault().post(new EvBusSubjectInfo(mSubjectInfo, true));
                        ((Activity)mContext).finish();
                    } else {
                        ToastUtil.showToast(mContext, mContext.getString(R.string.toast_delete_subject_ko));
                    }
                    break;
                }
                case ValueConstants.MSG_DELETE_DYNAMIC_ERROR:
                    ToastUtil.showToast(mContext, StringUtil.handlerErrorCode(mContext, msg.arg1));
                    break;
                //删除评论
                case ValueConstants.MSG_DELETE_COMMENT: {
                    boolean isSuccess = (boolean) msg.obj;
                    if (isSuccess) {
                        if(mOnStatusChangedListener!=null){
                            mOnStatusChangedListener.onStatusChanged();
                        }
                        mSubjectInfo.commentNum = mSubjectInfo.commentNum - 1 > 0 ?
                                mSubjectInfo.commentNum - 1 : 0;
                        EventBus.getDefault().post(new EvBusSubjectInfo(mSubjectInfo, false));
                    } else {
                        ToastUtil.showToast(mContext, "删除失败");
                    }
                    break;
                }
                case ValueConstants.MSG_DELETE_COMMENT_ERROR:
                    break;
                case ValueConstants.MSG_CANCEL_ATTENTION_OK:
                   if(mOnStatusChangedListener!=null){
                       mOnStatusChangedListener.onStatusChanged();
                   }
                    break;
                case ValueConstants.MSG_CANCEL_ATTENTION_KO:
                    ToastUtil.showToast(mContext, StringUtil.handlerErrorCode(mContext, msg.arg1));
                    break;
                case ValueConstants.MSG_ATTENTION_OK:
                    //关注成功
                    if(mOnStatusChangedListener!=null){
                        //mOnStatusChangedListener.onStatusChanged();
                    }
                    notifyDataSetChanged();
//                    if (mAttentionClickTemp != null && mAttentionClickTemp.userInfo != null) {
//                        mSubjectInfo.type = 1;
//                        initTopData(mSubjectInfo);
//                        EventBus.getDefault().post(new EvBusUGCInfoAttention(mAttentionClickTemp.userInfo.userId, true));
//                        mAttentionClickTemp = null;
//                    }
                    break;
                case ValueConstants.MSG_ATTENTION_KO:
                    ToastUtil.showToast(mContext, StringUtil.handlerErrorCode(mContext, msg.arg1));
                    break;
            }


            return false;
        }});

    /**
     * 设置直播数据视图
     *
     * @param subjectInfo
     */
    private void initTopData(UgcInfoResult subjectInfo) {
        int width = (ScreenSize.getScreenWidth(mContext) - 30 * 2 - 20 * 2) / 3;
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

                            if (userService.isLogin()) {
                                if (ugcInfoResult != null && ugcInfoResult.userInfo != null) {
                                    mClubController.doAddAttention(mContext, ugcInfoResult.userInfo.userId);
                                }
                            } else {
                                NavUtils.gotoLoginActivity(mContext);
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

                            if (userService.isLogin()) {
                                if (ugcInfoResult != null && ugcInfoResult.userInfo != null) {
                                    mClubController.doAddAttention(mContext, ugcInfoResult.userInfo.userId);
                                }
                            } else {
                                NavUtils.gotoLoginActivity(mContext);
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

                            if (userService.isLogin()) {
                                if (ugcInfoResult != null && ugcInfoResult.userInfo != null) {
                                    mClubController.doAddAttention(mContext, ugcInfoResult.userInfo.userId);
                                }
                            } else {
                                NavUtils.gotoLoginActivity(mContext);
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
            ToastUtil.showToast(mContext, mContext.getString(R.string.error_data_exception));
            ((Activity)mContext).finish();
            return;
        }
    }


    public void setUgcData(List<Api_SNSCENTER_UgcInfoResult> list){
        ugcInfoList = list;
    }
    public void addUgcData(List<Api_SNSCENTER_UgcInfoResult> list){
        if (ugcInfoList != null) {
            ugcInfoList.addAll(list);
        }else {
            ugcInfoList = new ArrayList<>();
            ugcInfoList.addAll(list);
        }
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.dynamic_info_item,parent,false);
        View view =null;
        if(viewType==LIVE_CONTENT){
            view =LayoutInflater.from(mContext)
                    .inflate(R.layout.cell_master_circle_live,null);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(params);
            return new DynamicInfoAdapter.LiveViewHolder(view);
        }else if(viewType==VIDEO_CONTENT){
            view =LayoutInflater.from(mContext)
                    .inflate(R.layout.cell_master_circle_video,null);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(params);
            return new DynamicInfoAdapter.VideoViewHolder(view);
        }else{
            view =LayoutInflater.from(mContext)
                    .inflate(R.layout.cell_master_circle_ugc,null);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(params);

            return new DynamicInfoAdapter.UgcViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position < ugcInfoList.size()) {//最后一个脚布局没有对应 ugcInfo
            Api_SNSCENTER_UgcInfoResult ugcInfo =  ugcInfoList.get(position);

            UgcInfoResult item = new UgcInfoResult();
            item.isSupport=ugcInfo.isSupport;
            item.commentNum=ugcInfo.commentNum;
            item.userInfo=new UserInfo();

            if (ugcInfo.userInfo != null) {
                item.userInfo.avatar = ugcInfo.userInfo.avatar;
                item.userInfo.nickname = ugcInfo.userInfo.nickname;
                item.userInfo.userId = ugcInfo.userInfo.userId;
            }
            item.commentNum=ugcInfo.commentNum;
            item.supportNum=ugcInfo.supportNum;
            if(ugcInfo.poiInfo!=null){
                item.poiInfo= new POIInfo();
                item.poiInfo.detail =ugcInfo.poiInfo.detail;
                item.poiInfo.latitude =ugcInfo.poiInfo.latitude;
                item.poiInfo.longitude =ugcInfo.poiInfo.longitude;
            }

            item.id=ugcInfo.id;
            item.videoPicUrl=ugcInfo.videoPicUrl;
            item.videoUrl=ugcInfo.videoUrl;
            item.gmtCreated = ugcInfo.gmtCreated;
            item.liveStatus = ugcInfo.liveStatus;
            item.liveId=ugcInfo.liveId;
            item.textContent=ugcInfo.textContent;
            item.type=ugcInfo.type;
            item.contentType=ugcInfo.contentType;
            item.picList=ugcInfo.picList;
            if(ugcInfo.tagInfoList!=null){
                item.tagInfoList=new ArrayList<>();
                for (int j = 0; j < ugcInfo.tagInfoList.size(); j++) {
                    Api_SNSCENTER_TagInfo tagInfo = ugcInfo.tagInfoList.get(j);
                    TagInfo info=new TagInfo();
                    info.id=tagInfo.id;
                    info.name=tagInfo.name;
                    item.tagInfoList.add(info);
                }
            }

            int type = getItemViewType(position);

            switch (type) {
                case LIVE_CONTENT:
                    convertCircleLive(holder.itemView, item, ValueConstants.TYPE_COMMENT_LIVESUP,
                            ValueConstants.TYPE_PRAISE_LIVESUP, true,
                            source, attentionClick);
                    break;
                case VIDEO_CONTENT:
                    convertCircleVideo(holder.itemView, item, ValueConstants.TYPE_COMMENT_LIVESUP,
                            ValueConstants.TYPE_PRAISE_LIVESUP, true,
                            source, attentionClick);
                    break;
                case OTHER_CONTENT:
                    convertCircleUGC(holder.itemView,position, item, ValueConstants.TYPE_COMMENT_LIVESUP,
                            ValueConstants.TYPE_PRAISE_LIVESUP, true,
                            source, attentionClick);
                    break;
                default:

                    break;
            }
        }
//        if(holder instanceof DynamicInfoAdapter.LiveViewHolder){
//            convertCircleLive(holder.itemView, item, ValueConstants.TYPE_COMMENT_LIVESUP,
//                        ValueConstants.TYPE_PRAISE_LIVESUP, true,
//                        source, attentionClick);
//        }else if(holder instanceof DynamicInfoAdapter.VideoViewHolder){
//            convertCircleVideo(holder.itemView, item, ValueConstants.TYPE_COMMENT_LIVESUP,
//                        ValueConstants.TYPE_PRAISE_LIVESUP, true,
//                        source, attentionClick);
//        }else{
//            convertCircleUGC(holder.itemView,position, item, ValueConstants.TYPE_COMMENT_LIVESUP,
//                        ValueConstants.TYPE_PRAISE_LIVESUP, true,
//                        source, attentionClick);
//        }
    }

    DynamicInfoAdapter.AttentionClickOuter attentionClick = new DynamicInfoAdapter.AttentionClickOuter() {
        @Override
        public void onAttentionClick(UgcInfoResult ugcInfoResult) {
            // TODO: 16/7/11 关注
            if (TimeUtil.isFastDoubleClick()) {
                return;
            }


            if (userService.isLogin()) {
                if (ugcInfoResult != null && ugcInfoResult.userInfo != null) {
                    Analysis.pushEvent(mContext,AnEvent.HOME_PAGE_INFORMATION_FLOW_ATTENTION);
                    mClubController.doAddAttention(mContext, ugcInfoResult.userInfo.userId);
                    for (Api_SNSCENTER_UgcInfoResult api_snscenter_ugcInfoResult : ugcInfoList) {
                        if (api_snscenter_ugcInfoResult.userInfo.userId == ugcInfoResult.userInfo.userId){
                            api_snscenter_ugcInfoResult.type = 1;
                        }
                    }
                    mUgcInfoResult=ugcInfoResult;
                }
            } else {
                NavUtils.gotoLoginActivity(mContext);
            }
        }
    };

    @Override
    public int getItemCount() {

        if(ugcInfoList == null){
            return 0;
        } else {
            return ugcInfoList.size();
        }
        //list.size() + Foot_Item
    }

    @Override
    public int getItemViewType(int position) {
        if (position < ugcInfoList.size()) {
            Api_SNSCENTER_UgcInfoResult ugcInfo =  ugcInfoList.get(position);
            if (ugcInfo.contentType == TYPE_LIVE_CONTENT) {
                return LIVE_CONTENT;
            } else if (!TextUtils.isEmpty(ugcInfo.videoPicUrl)) {
                return VIDEO_CONTENT;
            } else {
                return OTHER_CONTENT;
            }
        } else {
            return ITEM_FOOT;
        }
    }


    /**
     * ViewHolder
     */
    public static class VideoViewHolder extends RecyclerView.ViewHolder{

        public VideoViewHolder(View itemView) {
            super(itemView);
        }
    }
    public static class LiveViewHolder extends RecyclerView.ViewHolder{

        public LiveViewHolder(View itemView) {
            super(itemView);
        }
    }
    public static class UgcViewHolder extends RecyclerView.ViewHolder{

        public UgcViewHolder(View itemView) {
            super(itemView);
        }
    }

    public  void convertCircleUGC(View view, int position, final UgcInfoResult item,
                                        final String typeComment, final String typePraise,
                                        boolean isShowCommentPraise, final String source, final AttentionClickOuter attentionClick) {
        handleCommonTopView(view.findViewById(R.id.cell_circle_common_top_parent), item, typeComment, typePraise, source, attentionClick);
        handleCommonBottomView(view.findViewById(R.id.cell_circle_common_bottom_parent), item, typeComment, typePraise, isShowCommentPraise, source);

        final NoScrollGridView picGridView = (NoScrollGridView) view.findViewById(R.id.cell_circle_ugc_pic_grid);
        FrameLayout onePicLayout = (FrameLayout) view.findViewById(R.id.cell_circle_ugc_pic_one_img_layout);
        ImageView onePicImg = (ImageView) view.findViewById(R.id.cell_circle_ugc_pic_one_img);

        View lineDivide = view.findViewById(R.id.cell_circle_guc__line);

        //图片
        final List<String> picList = item.picList;
        if (picGridView.getAdapter() == null) {
            picGridView.setAdapter(setPicPreviewAdapter(onePicLayout.getContext()));
        }
        if (item.picList != null && item.picList.size() > 0) {
            picGridView.setVisibility(View.VISIBLE);
            onePicLayout.setVisibility(View.GONE);
            int width = (ScreenSize.getScreenWidth(onePicLayout.getContext().getApplicationContext()) - 30 * 2 - 30 * 2) / 3;
            if (item.picList.size() == 1) {
                picGridView.setNumColumns(1);
                picGridView.setVisibility(View.GONE);
                onePicLayout.setVisibility(View.VISIBLE);
//                BaseImgView.loadimg(onePicImg, ImageUtils.getImageFullUrl(item.picList.get(0)), R.mipmap.icon_default_310_180,
//                        R.mipmap.icon_default_310_180, R.mipmap.icon_default_310_180,
//                        ImageScaleType.EXACTLY, 600, 600, -1);
                ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(item.picList.get(0)), R.mipmap.icon_default_310_180, 600, 600, onePicImg);

                onePicLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ArrayList<String> pics = new ArrayList<String>();
                        pics.add(ImageUtils.getImageFullUrl(item.picList.get(0)));
                        NavUtils.gotoLookBigImage(v.getContext(), pics, 1);
                    }
                });
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        600
                );
                onePicLayout.setLayoutParams(layoutParams);
            } else if (item.picList.size() == 4) {
                ViewGroup.LayoutParams lp4 = picGridView.getLayoutParams();
                lp4.width = width * 2 + 30;
                picGridView.setLayoutParams(lp4);
                picGridView.setNumColumns(2);
            } else {
                ViewGroup.LayoutParams lp4 = picGridView.getLayoutParams();
                lp4.width = ViewGroup.LayoutParams.MATCH_PARENT;
                picGridView.setLayoutParams(lp4);
                picGridView.setNumColumns(3);
            }
            ((QuickAdapter) picGridView.getAdapter()).replaceAll(picList);
        } else {
            ((QuickAdapter) picGridView.getAdapter()).clear();
            picGridView.setVisibility(View.GONE);
            onePicLayout.setVisibility(View.GONE);
        }
        GridViewUtils.setGridViewHeightBasedOnChildren(position, picGridView);
        picGridView.setOnItemClickListener(new LiveItemHelper.PicGridItemClick((Activity) picGridView.getContext(), (ArrayList<String>) picList, item.id));
        picGridView.setOnTouchInvalidPositionListener(new NoScrollGridView.OnTouchInvalidPositionListener() {
            @Override
            public boolean onTouchInvalidPosition(int motionEvent) {
                if (item.userInfo != null) {
                    NavUtils.gotoLiveDetailActivity((Activity) picGridView.getContext(), item.id,
                            item, ValueConstants.TYPE_COMMENT_LIVESUP, ValueConstants.TYPE_PRAISE_LIVESUP, false);
                } else {
                    ToastUtil.showToast(picGridView.getContext(), mContext.getString(R.string.error_data_exception));
                }
                return false;
            }
        });

        //设置底部横线显示
        if (TopicDetailsActivity.class.getSimpleName().equals(source)) {
            lineDivide.setVisibility(View.VISIBLE);
        }
    }

    public  void convertCircleVideo(View view, final UgcInfoResult item,
                                          final String typeComment, final String typePraise,
                                          boolean isShowCommentPraise, final String source, final AttentionClickOuter attentionClick) {
        handleCommonTopView(view.findViewById(R.id.cell_circle_common_top_parent), item, typeComment, typePraise, source, attentionClick);
        handleCommonBottomView(view.findViewById(R.id.cell_circle_common_bottom_parent), item, typeComment, typePraise, isShowCommentPraise, source);

        FrameLayout videoLayout = (FrameLayout) view.findViewById(R.id.cell_circle_video_layout);
        ImageView videoBgImg = (ImageView) view.findViewById(R.id.cell_circle_video_img);
        ImageView videoPlayStateImg = (ImageView) view.findViewById(R.id.cell_circle_video_img_play);

        View lineDivide = view.findViewById(R.id.cell_circle_video_line);

//        BaseImgView.loadimg(videoBgImg,
//                ImageUtils.getImageFullUrl(item.videoPicUrl),
//                R.mipmap.icon_default_310_180,
//                R.mipmap.icon_default_310_180,
//                R.mipmap.icon_default_310_180,
//                ImageScaleType.EXACTLY,
//                310,
//                180,
//                0);
        ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(item.videoPicUrl), R.mipmap.icon_default_310_180, 310, 180, videoBgImg);

        videoPlayStateImg.setImageResource(R.mipmap.ic_video_play);
        videoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                VideoInfo videoInfo = new VideoInfo();
                videoInfo.videoUrl = item.videoUrl;
                NavUtils.gotoVideoPlayerctivty(v.getContext(), videoInfo);
            }
        });
        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ScreenSize.getScreenWidth(videoLayout.getContext().getApplicationContext()) * 3 / 5
        );
        videoLayout.setLayoutParams(layoutParams1);

        //设置底部横线显示
        if (TopicDetailsActivity.class.getSimpleName().equals(source)) {
            lineDivide.setVisibility(View.VISIBLE);
        }
    }

    public  void convertCircleLive(View view, final UgcInfoResult item,
                                         final String typeComment, final String typePraise,
                                         boolean isShowCommentPraise, final String source, final AttentionClickOuter attentionClick) {
        handleCommonTopView(view.findViewById(R.id.cell_circle_common_top_parent), item, typeComment, typePraise, source, attentionClick);
        handleCommonBottomView(view.findViewById(R.id.cell_circle_common_bottom_parent), item, typeComment, typePraise, isShowCommentPraise, source);

        FrameLayout liveBglayout = (FrameLayout) view.findViewById(R.id.cell_circle_live_layout);
        TextView liveType = (TextView) view.findViewById(R.id.cell_circle_live_type);
        TextView liveCategory = (TextView) view.findViewById(R.id.cell_circle_live_category_type);
        ImageView bgImage = (ImageView) view.findViewById(R.id.cell_circle_live_img);

        View lineDivide = view.findViewById(R.id.cell_circle_live_line);

        if (STATUS_LIVE == item.liveStatus || STATUS_LIVE_OVER == item.liveStatus) {
            liveType.setText("直播");
        } else {
            liveType.setText("回放");
        }

        StringBuilder tagList = new StringBuilder();
        if (item.tagInfoList != null && item.tagInfoList.size() > 0) {
            for (int i = 0; i < item.tagInfoList.size(); i++) {
                tagList.append("· ").append(item.tagInfoList.get(i).name);
            }
            liveCategory.setText(tagList.toString());//直播所属类别
        }

        if (item.picList != null && item.picList.size() > 0) {
//            BaseImgView.loadimg(bgImage, ImageUtils.getImageFullUrl(item.picList.get(0)), R.mipmap.icon_default_310_180,
//                    R.mipmap.icon_default_310_180, R.mipmap.icon_default_310_180,
//                    ImageScaleType.EXACTLY, 360, 280, -1);
            ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(item.picList.get(0)), R.mipmap.icon_default_310_180, 360, 280, bgImage);

        } else {
            bgImage.setImageResource(R.mipmap.icon_default_310_180);
        }
        liveBglayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.liveId > 0) {
                    if (item.userInfo != null) {

                        IntentUtil.startVideoClientActivity(item.liveId, item.userInfo.userId,
                                (STATUS_LIVE == item.liveStatus || STATUS_LIVE_OVER == item.liveStatus) ? true : false,item.liveScreenType);
                    } else {
                        IntentUtil.startVideoClientActivity(item.liveId, 0,
                                (STATUS_LIVE == item.liveStatus || STATUS_LIVE_OVER == item.liveStatus) ? true : false,item.liveScreenType);
                    }
                } else {
                    ToastUtil.showToast(v.getContext(), "liveId无效");
                }
            }
        });

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ScreenSize.getScreenWidth(liveBglayout.getContext().getApplicationContext()) * 3 / 5
        );
        liveBglayout.setLayoutParams(layoutParams);

        //设置底部横线显示
        if (TopicDetailsActivity.class.getSimpleName().equals(source)) {
            lineDivide.setVisibility(View.VISIBLE);
        }
    }

    private  void handleCommonTopView(View view, final UgcInfoResult item,
                                            final String typeComment, final String typePraise,
                                            final String source, final AttentionClickOuter attentionClick) {
        final UserInfo snsUserInfo = item.userInfo;
        ImageView userHead = (ImageView) view.findViewById(R.id.cell_circle_common_top_user_head);
        TextView userName = (TextView) view.findViewById(R.id.cell_circle_common_top_user_name);
        TextView followText = (TextView) view.findViewById(R.id.cell_circle_common_top_tv_attention);
        TextView postTimeTv = (TextView) view.findViewById(R.id.cell_circle_common_top_top_time);
        TextView postContent = (TextView) view.findViewById(R.id.cell_circle_common_top_brief);

        LinearLayout livePeopleLayout = (LinearLayout) view.findViewById(R.id.cell_circle_common_top_live_people_num_layout);
        TextView liveOnlinePoepleNum = (TextView) view.findViewById(R.id.cell_circle_common_top_live_people_num);
        TextView liveOnlinePoepleLabel = (TextView) view.findViewById(R.id.cell_circle_common_top_live_people_num_label);

        if (snsUserInfo != null) {
            //用户头像
            if (snsUserInfo.avatar != null && !TextUtils.isEmpty(snsUserInfo.avatar)) {
//                BaseImgView.loadimg(userHead, ImageUtils.getImageFullUrl(snsUserInfo.avatar), R.mipmap.icon_default_avatar, R.mipmap.icon_default_avatar, R.mipmap.icon_default_avatar,
//                        ImageScaleType.EXACTLY, 128, 128, 180);
                ImageLoadManager.loadCircleImage(CommonUtil.getImageFullUrl(snsUserInfo.avatar), R.mipmap.icon_default_avatar, 128, 128, userHead);

            }
//            else {
////                BaseImgView.loadimg(userHead, "res:///" + R.mipmap.icon_default_avatar , R.mipmap.icon_default_avatar, R.mipmap.icon_default_avatar, R.mipmap.icon_default_avatar,
////                        ImageScaleType.EXACTLY, 128, 128, 180);
//                ImageLoadManager.loadCircleImage("res:///" + R.mipmap.icon_default_avatar, R.mipmap.icon_default_310_180, 128, 128, userHead);
//
//            }

            userHead.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (!MasterHomepageActivity.class.getSimpleName().equals(source)) {
                        Analysis.pushEvent(mContext, AnEvent.HOME_PAGE_INFORMATION_FLOW);
//                    NavUtils.gotoPersonalPage(context, snsUserInfo.userId,snsUserInfo.nickname,snsUserInfo.options);
                        NavUtils.gotoMasterHomepage(v.getContext(), snsUserInfo.userId);
                    }
                }
            });
            userName.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (!MasterHomepageActivity.class.getSimpleName().equals(source)) {
                        Analysis.pushEvent(mContext, AnEvent.HOME_PAGE_INFORMATION_FLOW);
//                    NavUtils.gotoPersonalPage(context, snsUserInfo.userId,snsUserInfo.nickname,snsUserInfo.options);
                        NavUtils.gotoMasterHomepage(v.getContext(), snsUserInfo.userId);
                    }
                }
            });

            //用户名
            userName.setText(TextUtils.isEmpty(snsUserInfo.nickname) ? "" : snsUserInfo.nickname);

        } else {
            userHead.setImageResource(R.mipmap.icon_default_avatar);
            userName.setText("");
            userHead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            userName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }

        if (MasterHomepageActivity.class.getSimpleName().equals(source)) {
            followText.setVisibility(View.GONE);
        } else {
            if (snsUserInfo != null) {
                if (!userService.isLogin()){
                    followText.setText("+关注");
                    followText.setTextColor(mContext.getResources().getColor(R.color.red_ying));
                    followText.setEnabled(true);
                }else {
                    followText.setVisibility((userService.isLoginUser(snsUserInfo.userId) ? View.GONE : View.VISIBLE));
                }
            }
            switch (item.type) {
                case 0://未关注
                    followText.setText("+关注");
                    followText.setTextColor(mContext.getResources().getColor(R.color.red_ying));
                    followText.setEnabled(true);
                    break;
                case 1://已关注
                    /*followText.setText("已关注");
                    followText.setTextColor(mContext.getResources().getColor(R.color.gray));
                    followText.setEnabled(false);*/
                    followText.setVisibility(View.GONE);
                    break;
                case 2://双相关注
                    /*followText.setText("已关注");
                    followText.setTextColor(mContext.getResources().getColor(R.color.gray));
                    followText.setEnabled(false);*/
                    followText.setVisibility(View.GONE);
                    break;
            }
        }

        //关注操作
        followText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (attentionClick != null) {
                    attentionClick.onAttentionClick(item);
                }
            }
        });

        //发布时间
        try {
            postTimeTv.setText(DateUtil.getCreateAt(item.gmtCreated));
        } catch (FormatException e) {
            postTimeTv.setText(postTimeTv.getContext().getString(R.string.label_long_long_ago));
        }

        //动态内容
        if (TextUtils.isEmpty(item.textContent)) {
            postContent.setVisibility(View.GONE);
        } else {
            postContent.setVisibility(View.VISIBLE);
            postContent.setText(item.textContent);

            //复制逻辑
            postContent.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    v.setTag("longclick");
                    CopyDialog copyDialog = new CopyDialog(v.getContext(), R.style.logindialog, item.textContent);
                    copyDialog.show();

                    TextView txt = (TextView) v;
                    Spannable span = (Spannable) txt.getText();
                    Selection.removeSelection(span);
                    return true;
                }
            });
            postContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavUtils.gotoLiveDetailActivity((Activity) v.getContext(), item.id, item, typeComment, typePraise, false);
                }
            });
            //分散对齐
//			TextView contentView = (TextView)helper.getView(R.id.cell_live_brief);
//			TextJustification.justify(contentView, ScreenUtil.getScreenWidth(context));
        }

        if (item.contentType == TYPE_LIVE_CONTENT) {
            livePeopleLayout.setVisibility(View.VISIBLE);
            String num;
            if (item.viewNum >= 999000) {
                liveOnlinePoepleNum.setText("999+");//直播在线人数
                if ((STATUS_LIVE == item.liveStatus || STATUS_LIVE_OVER == item.liveStatus)) {
                    liveOnlinePoepleLabel.setText("万在线");//直播在线人数
                } else {
                    liveOnlinePoepleLabel.setText("万看过");//直播在线人数
                }
            } else if (item.viewNum >= 10000) {
                num = (new DecimalFormat("#.##").format(item.viewNum / 10000.0f));
                liveOnlinePoepleNum.setText(num);//直播在线人数
                if ((STATUS_LIVE == item.liveStatus || STATUS_LIVE_OVER == item.liveStatus)) {
                    liveOnlinePoepleLabel.setText("万在线");//直播在线人数
                } else {
                    liveOnlinePoepleLabel.setText("万看过");//直播在线人数
                }
            } else {
                liveOnlinePoepleNum.setText(item.viewNum + "");//直播在线人数
                if ((STATUS_LIVE == item.liveStatus || STATUS_LIVE_OVER == item.liveStatus)) {
                    liveOnlinePoepleLabel.setText("在线");//直播在线人数
                } else {
                    liveOnlinePoepleLabel.setText("看过");//直播在线人数
                }
            }
        } else {
            if (item.videoUrl != null && item.videoUrl.length() > 0) {
                if (item.viewNum >= 999000) {
                    liveOnlinePoepleNum.setText("999+");
                    liveOnlinePoepleLabel.setText("万看过");
                }else if (item.viewNum >= 10000) {
                    String num;
                    num = (new DecimalFormat("#.##").format(item.viewNum / 10000.0f));
                    liveOnlinePoepleNum.setText(num);
                    liveOnlinePoepleLabel.setText("万看过");
                }else {
                    liveOnlinePoepleNum.setText(item.viewNum + "");
                    liveOnlinePoepleLabel.setText("看过");
                }
            } else {
                livePeopleLayout.setVisibility(View.GONE);
            }
        }

        HealthCircleTextUtil.SetLinkClickIntercept(postContent.getContext(), postContent);
    }

    private  void handleCommonBottomView(View view, final UgcInfoResult item,
                                               final String typeComment, final String typePraise,
                                               boolean isShowCommentPraise, final String source) {
        final RelativeLayout likeLayout = (RelativeLayout) view.findViewById(R.id.cell_circle_common_bottom_like_layout);
        RelativeLayout commentLayout = (RelativeLayout) view.findViewById(R.id.cell_circle_common_bottom_comment_layout);
        LinearLayout moreLayout = (LinearLayout) view.findViewById(R.id.cell_circle_common_bottom_more_layout);
        ImageView likeSelectImg = (ImageView) view.findViewById(R.id.cell_circle_common_bottom_like_img);
        TextView commentNum = (TextView) view.findViewById(R.id.cell_circle_common_bottom_comment);
        TextView praiseNum = (TextView) view.findViewById(R.id.cell_circle_common_bottom_like);
        TextView gpsText = (TextView) view.findViewById(R.id.cell_circle_common_bottom_gps);

        //位置信息
        if (item.poiInfo != null && !StringUtil.isEmpty(item.poiInfo.detail)) {
            gpsText.setVisibility(View.VISIBLE);
            gpsText.setText(item.poiInfo.detail);
        } else {
            gpsText.setVisibility(View.GONE);
        }

        if (!isShowCommentPraise) {
//            likeLayout.setVisibility(View.GONE);
            commentLayout.setVisibility(View.GONE);
        } else {
//            likeLayout.setVisibility(View.VISIBLE);
            commentLayout.setVisibility(View.VISIBLE);
            commentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //评论进详情页
                    if (userService.isLogin()) {
                        NavUtils.gotoLiveDetailActivity((Activity) v.getContext(), item.id, item, typeComment, typePraise, true);
//                        NavUtils.gotoLiveDetailActivity((Activity) v.getContext(), item.id, typeComment, typePraise, CircleItemType.LIVE, true);
                    } else {
                        NavUtils.gotoLoginActivity(v.getContext());
                    }
                }
            });

            //点赞和评论数量显示
            commentNum.setText((item.commentNum > 0 ? (item.commentNum > 999 ? "999+" : item.commentNum) : 0) + "");
        }

        //点赞和评论
        likeSelectImg.setSelected(ValueConstants.TYPE_AVAILABLE.equals(item.isSupport));//是否点赞
        /**
         * 执行点赞操作
         */
        likeLayout.setOnClickListener(new LiveItemHelper.PraiseClick((Activity) likeLayout.getContext(), likeLayout, item, typePraise) {
            @Override
            public void onPraiseSucess(View itemView, ComSupportInfo comSupportInfo) {
                if (comSupportInfo != null) {
                    item.isSupport = comSupportInfo.isSupport;
                } else {
                    item.isSupport = ValueConstants.TYPE_AVAILABLE.equals(item.isSupport) ? ValueConstants.TYPE_DELETED : ValueConstants.TYPE_AVAILABLE;
                }
                itemView.findViewById(R.id.cell_circle_common_bottom_like_img).setSelected(ValueConstants.TYPE_AVAILABLE.equals(item.isSupport));
                if (ValueConstants.TYPE_AVAILABLE.equals(item.isSupport)) {
                    item.supportNum = item.supportNum + 1;
                    ((TextView) itemView.findViewById(R.id.cell_circle_common_bottom_like)).setText("" + (item.supportNum));
                } else {
                    item.supportNum = item.supportNum - 1;
                    ((TextView) itemView.findViewById(R.id.cell_circle_common_bottom_like)).setText("" + (item.supportNum < 0 ? 0 : item.supportNum));
                }
                if (LiveDetailActivity.class.getSimpleName().equals(source)) {
                    EventBus.getDefault().post(new EvBusSubjectInfo(item, false));
                }
            }

            @Override
            public void onPraiseError(int errorCode) {
                ToastUtil.showToast(likeLayout.getContext(), StringUtil.handlerErrorCode(likeLayout.getContext().getApplicationContext(), errorCode));
            }
        });
        praiseNum.setText((item.supportNum > 0 ? (item.supportNum > 999 ? "999+" : item.supportNum) : 0) + "");

        UserInfo snsUserInfo = item.userInfo;
        if (snsUserInfo != null && userService.isLoginUser(snsUserInfo.userId)) {
            moreLayout.setVisibility(View.GONE);
        } else {
            moreLayout.setVisibility(View.VISIBLE);
        }

        moreLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HarwkinLogUtil.info("item.id = " + item.id);
//                if (DynamicInfoAdapter.class.getSimpleName().equals(source)) {
//                    de.greenrobot.event.EventBus.getDefault().post(new EvBusComplaintDetailDynamic(item));
//                } else {
//                    de.greenrobot.event.EventBus.getDefault().post(new EvBusComplaintDynamic(item));
//                }

                mChooseSubjectInfo= item;
                showComplaintMenuDialog();
            }
        });

    }
    public interface AttentionClickOuter {
        void onAttentionClick(UgcInfoResult ugcInfoResult);
    }

    public void showComplaintMenuDialog() {
        List<String> menus = new ArrayList<>();
        if (mChooseSubjectInfo.type != 0) {
            menus.add(mContext.getString(R.string.label_menu_cancel_attention));
        }
        if (mChooseSubjectInfo == null ||
                mChooseSubjectInfo.userInfo == null ||
                !userService.isLoginUser(mChooseSubjectInfo.userInfo.userId)) {
            menus.add(mContext.getString(R.string.label_menu_accusation));
            menus.add(mContext.getString(R.string.label_menu_complaint));
        }
        final String[] mMenus = StringUtil.listToStrings(menus);
        Dialog mComplaintDlg = MenuUtils.showAlert(mContext,
                null,
                mMenus,
                null,
                new MenuUtils.OnAlertSelectId() {
                    @Override
                    public void onClick(int whichButton) {
                        if (whichButton != mMenus.length) {
                            if (mContext.getString(R.string.label_menu_accusation).equals(mMenus[whichButton])) {

                                gotoComplaintListUI();
                            } else if (mContext.getString(R.string.label_menu_complaint).equals(mMenus[whichButton])) {

                                gotoBlackSetting();
                            } else if (mContext.getString(R.string.label_menu_cancel_attention).equals(mMenus[whichButton])) {
//                                doCancelAttention();

                                showCancelDialog();
                            }
                        }
                    }
                },
                null);

        mComplaintDlg.show();
    }

    private void doCancelAttention() {
        if (mChooseSubjectInfo == null || mChooseSubjectInfo.userInfo == null) {
            return;
        }
        mDiscoverController.doCancelAttention(mContext, mChooseSubjectInfo.userInfo.userId);
    }

    /**
     * 屏蔽设置
     */
    private void gotoBlackSetting() {
        if (mChooseSubjectInfo == null) {
            return;
        }
//        NavUtils.gotoBlackSetting(mContext, mChooseSubjectInfo);
    }

    /**
     * 跳转到举报列表
     */
    private void gotoComplaintListUI() {
        if (mChooseSubjectInfo == null) {
            return;
        }
//        NavUtils.gotoComplaintList(mContext, mChooseSubjectInfo);
    }

    private Dialog mCancelDialog;

    private void showCancelDialog() {
        View view = View.inflate(mContext, R.layout.dialog_cancel_follow_confirm, null);
        if (mCancelDialog == null) {
//            mDialog = new DialogBuilder(this)
//                    .setCancelable(true)
//                    .setStyle(R.style.MenuDialogStyle)
//                    .setAnimation(R.anim.abc_fade_in)
//                    .setGravity(Gravity.CENTER)
//                    .build();
            mCancelDialog = DialogUtil.showMessageDialog(mContext, "是否不再关注此人？", "",
                    mContext.getString(R.string.label_btn_ok), mContext.getString(R.string.cancel), new View.OnClickListener() {
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

   public interface OnStatusChangedListener{
       public void onStatusChanged();
   }

}

