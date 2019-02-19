package com.newyhy.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.util.MultiTypeDelegate;
import com.harwkin.nb.camera.MenuUtils;
import com.newyhy.activity.FullScreenVideoPlayerActivity;
import com.newyhy.activity.HomeMainTabActivity;
import com.newyhy.activity.NewTopicDetailActivity;
import com.newyhy.config.CircleItemType;
import com.newyhy.fragment.NewHomeCourtFragment;
import com.newyhy.network.CircleNetController;
import com.newyhy.utils.DateUtil;
import com.newyhy.views.ninelayout.FixPicUtils;
import com.newyhy.views.ninelayout.YHYNineGridLayout;
import com.quanyan.base.util.LocalUtils;
import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.R;
import com.quanyan.yhy.libanalysis.AnEvent;
import com.quanyan.yhy.libanalysis.Analysis;
import com.quanyan.yhy.ui.base.utils.DialogUtil;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.common.calendar.ScreenUtil;
import com.quanyan.yhy.ui.master.activity.MasterHomepageActivity;
import com.quanyan.yhy.util.HealthCircleTextUtil;
import com.videolibrary.utils.IntentUtil;
import com.yhy.common.beans.net.model.discover.UgcInfoResult;
import com.yhy.common.config.ContextHelper;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.AndroidUtils;
import com.yhy.common.utils.CommonUtil;
import com.yhy.common.utils.SPUtils;
import com.yhy.imageloader.ImageLoadManager;
import com.yhy.router.YhyRouter;
import com.yhy.service.IUserService;


import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static com.newyhy.adapter.UgcMultiAdapter.UGCItemType.LIVE_UGC;
import static com.newyhy.adapter.UgcMultiAdapter.UGCItemType.NORMAL_UGC;
import static com.newyhy.adapter.UgcMultiAdapter.UGCItemType.VIDEO_UGC;

/**
 * UgcMultiAdapter
 * Created by Jiervs on 2018/4/19.
 */

public class UgcMultiAdapter extends BaseQuickAdapter<UgcInfoResult, BaseViewHolder> {

    private Activity mActivity;
    private Fragment mFragment;
    private List<UgcInfoResult> mList;
    private CircleNetController mController;
    @Autowired
    IUserService userService;
    //private UgcItemManager manager;

    public UgcMultiAdapter(Activity mActivity, @Nullable Fragment mFragment, @Nullable List<UgcInfoResult> ugcList, CircleNetController mController) {
        super(ugcList);
        this.mActivity = mActivity;
        this.mFragment = mFragment;
        this.mList = ugcList;
        this.mController = mController;

        YhyRouter.getInstance().inject(this);

        setMultiTypeDelegate(new MultiTypeDelegate<UgcInfoResult>() {
            @Override
            protected int getItemType(UgcInfoResult data) {
                int viewType = 1;
                if (data.shortVideoType == 4 || data.shortVideoType == 5) {
                    viewType = VIDEO_UGC;
                } else if (data.contentType == 3) {//内容类型： 2.UGC 3.直播
                    viewType = LIVE_UGC;
                } else {
                    viewType = NORMAL_UGC;
                }
                return viewType;
            }
        });

        getMultiTypeDelegate()
                //一般Ugc
                .registerItemType(NORMAL_UGC, R.layout.ugc_recycle_normal_item)
                //Video
                .registerItemType(VIDEO_UGC, R.layout.ugc_recycle_video_item)
                //Live
                .registerItemType(LIVE_UGC, R.layout.ugc_recycle_live_item);
    }

    @Override
    protected void convert(BaseViewHolder holder, UgcInfoResult ugcInfoResult) {
        switch (holder.getItemViewType()) {
            case NORMAL_UGC:
                covertNormalUGC(holder, ugcInfoResult);
                break;
            case VIDEO_UGC:
                covertVideoUGC(holder, ugcInfoResult);
                break;
            case LIVE_UGC:
                covertLiveUGC(holder, ugcInfoResult);
                break;
        }
    }

    /**
     * Item 的公共部分
     *
     * @param holder
     * @param data
     */
    private void synCommon(final BaseViewHolder holder, final UgcInfoResult data) {
        //设置Tag防止Glide加载头像抖动
        if (null != data.userInfo.avatar && !data.userInfo.avatar.equals(holder.getView(R.id.iv_header_ugc).getTag(R.id.iv_header_ugc))) {
            holder.getView(R.id.iv_header_ugc).setTag(R.id.iv_header_ugc, data.userInfo.avatar);
            //头像
            ImageLoadManager.loadCircleImage(CommonUtil.getImageFullUrl(data.userInfo.avatar), R.mipmap.icon_default_avatar, (ImageView) holder.getView(R.id.iv_header_ugc));
        }

        holder.getView(R.id.iv_header_ugc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//跳转个人主页
                if (mFragment != null && mFragment instanceof NewHomeCourtFragment)  //事件统计
                    Analysis.pushEvent(mActivity, AnEvent.HOME_PAGE_INFORMATION_FLOW);
                NavUtils.gotoMasterHomepage(v.getContext(), data.userInfo.userId);
            }
        });
        //昵称
        ((TextView) holder.getView(R.id.tv_name_ugc)).setText(data.userInfo.nickname);
        //发布时间
        try {
            String time = DateUtil.getCreateAt(data.gmtCreated);
            ((TextView) holder.getView(R.id.tv_time_ugc)).setText(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //ugc文本内容
        if (data.textContent != null && data.textContent.length() > 0) {
            holder.getView(R.id.tv_ugc).setVisibility(View.VISIBLE);
            ((TextView) holder.getView(R.id.tv_ugc)).setText(data.textContent);
        } else {
            holder.getView(R.id.tv_ugc).setVisibility(View.GONE);
        }
        HealthCircleTextUtil.SetLinkClickIntercept(mContext, (TextView) holder.getView(R.id.tv_ugc));

        //位置信息
        if (data.poiInfo != null && !TextUtils.isEmpty(data.poiInfo.detail)) {
            holder.getView(R.id.ll_location_ugc).setVisibility(View.VISIBLE);
            ((TextView) holder.getView(R.id.tv_location_ugc)).setText(data.poiInfo.detail);
        } else {
            holder.getView(R.id.ll_location_ugc).setVisibility(View.GONE);
            ;
        }
        //评论数
        ((TextView) holder.getView(R.id.tv_comment_num_ugc)).setText((data.commentNum > 0 ? (data.commentNum > 999 ? "999+" : data.commentNum) : 0) + "");
        //点赞数
        ((TextView) holder.getView(R.id.tv_support_num_ugc)).setText((data.supportNum > 0 ? (data.supportNum > 999 ? "999+" : data.supportNum) : 0) + "");
        if (ValueConstants.TYPE_AVAILABLE.equals(data.isSupport)) {
            //已点赞
            holder.getView(R.id.iv_support_ugc).setSelected(true);
        } else {
            holder.getView(R.id.iv_support_ugc).setSelected(false);
        }
        //直播或视频观看人数
        if (data.contentType == 2 && data.liveStatus == 1) {//直播
            ((TextView) holder.getView(R.id.tv_type_live)).setText(mContext.getString(R.string.online));
        } else {//回放
            ((TextView) holder.getView(R.id.tv_type_live)).setText(mContext.getString(R.string.saw));
        }
        //观看或在线人数
        String num = null;
        if (data.viewNum >= 9990000) {
            ((TextView) holder.getView(R.id.tv_saw_number)).setText("999+万");
        } else if (data.viewNum >= 10000) {
            num = (new DecimalFormat("#.##").format(data.viewNum / 10000.0f));
            ((TextView) holder.getView(R.id.tv_saw_number)).setText(num + "万");
        } else {
            ((TextView) holder.getView(R.id.tv_saw_number)).setText(data.viewNum + "");
        }

        //关注关系
        if (data.type == 1 || data.type == 2 || data.userInfo.userId == userService.getLoginUserId()) {// 关注类型 0:未关注 1:单向关注 2:双向关注
            holder.getView(R.id.btn_follow_ugc).setVisibility(View.GONE);
        } else {
            holder.getView(R.id.btn_follow_ugc).setVisibility(View.VISIBLE);
        }

        //更多
        if (userService.isLoginUser(data.userInfo.userId)) {
            holder.getView(R.id.ll_more_ugc).setVisibility(View.GONE);
        } else {
            holder.getView(R.id.ll_more_ugc).setVisibility(View.VISIBLE);
        }

        //Item点击事件
        holder.getView(R.id.item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onItemOnclickListener)
                    onItemOnclickListener.onItemClick(holder.getAdapterPosition());
            }
        });
        //评论点击
        holder.getView(R.id.ll_comment_ugc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (null != mActivity) {
                    //事件统计
                    if (mFragment != null && mFragment instanceof NewHomeCourtFragment)
                        Analysis.pushEvent(mActivity, AnEvent.INFORMATION_FLOW_SEND_COMMENTS);
                    if (userService.isLogin()) {//动态详情
//                        NavUtils.gotoLiveDetailActivity(mActivity, data.id, data, ValueConstants.TYPE_COMMENT_LIVESUP, ValueConstants.TYPE_PRAISE_LIVESUP,true);
//                        NavUtils.gotoLiveDetailActivity((Activity) v.getContext(),   data.id,ValueConstants.TYPE_COMMENT_LIVESUP, ValueConstants.TYPE_PRAISE_LIVESUP, CircleItemType.LIVE, true);
                        YhyRouter.getInstance().startCircleDetailActivity(v.getContext(), data.id, true);

                    } else {
                        NavUtils.gotoLoginActivity(mContext);
                    }
                }
            }
        });

        //点赞
        holder.getView(R.id.ll_support_ugc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userService.isLogin()) {
                    String outType = ValueConstants.TYPE_PRAISE_LIVESUP;
                    if (ValueConstants.TYPE_AVAILABLE.equals(data.isSupport)) {
                        mController.doPraiseToComment(mContext, data.id, outType, ValueConstants.UNPRAISE);
                    } else if (ValueConstants.TYPE_DELETED.equals(data.isSupport)) {
                        mController.doPraiseToComment(mContext, data.id, outType, ValueConstants.PRAISE);
                    }
                } else {
                    NavUtils.gotoLoginActivity(mContext);
                }
            }
        });

        //关注
        holder.getView(R.id.btn_follow_ugc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFragment != null && mFragment instanceof NewHomeCourtFragment)
                    Analysis.pushEvent(mActivity, AnEvent.INFORMATION_FLOW_ATTENTION);
                if (mActivity != null && mActivity instanceof MasterHomepageActivity)
                    Analysis.pushEvent(mContext, AnEvent.HOME_PAGE_INFORMATION_FLOW_ATTENTION);
                if (null != onFollowListener) onFollowListener.onFollowClick(data.userInfo.userId);
            }
        });

        //点击更多
        holder.getView(R.id.ll_more_ugc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMoreDialog(data);
            }
        });
    }

    /**
     * Item中 一般Ugc的处理
     *
     * @param holder
     * @param data
     */
    @SuppressLint("CheckResult")
    private void covertNormalUGC(final BaseViewHolder holder, final UgcInfoResult data) {
        synCommon(holder, data);
        holder.getView(R.id.ll_saw_number).setVisibility(View.INVISIBLE);
        //图片合集
        if (data.picList != null && data.picList.size() > 1 && data.videoUrl == null) {
            holder.getView(R.id.iv_single_ugc).setVisibility(View.GONE);
            holder.getView(R.id.ic_sup_height).setVisibility(View.GONE);
            holder.getView(R.id.nine_layout).setVisibility(View.VISIBLE);
            ((YHYNineGridLayout) holder.getView(R.id.nine_layout)).setIsShowAll(false);//当传入的图片数超过9张时，是否全部显示
            ((YHYNineGridLayout) holder.getView(R.id.nine_layout)).setSpacing(ScreenUtil.dip2px(mContext, 6));
            ((YHYNineGridLayout) holder.getView(R.id.nine_layout)).setUrlList(data.picList); //最后再设置图片url

        } else if (data.picList != null && data.picList.size() == 1 && data.videoUrl == null) {
            //单张图，这里不用NineLayout中单张图的处理是因为，源码中对LayoutParam有修改适配，引起Item的复用时的撑开抖动
            holder.getView(R.id.iv_single_ugc).setVisibility(View.VISIBLE);
            holder.getView(R.id.nine_layout).setVisibility(View.GONE);

            Glide.with(mContext).asFile().load(CommonUtil.getImageFullUrl(data.picList.get(0))).into(new SimpleTarget<File>() {
                @Override
                public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    Bitmap bmp = BitmapFactory.decodeFile(resource.getAbsolutePath(), options);
                    int width = options.outWidth;
                    int height = options.outHeight;
                    FixPicUtils.PicSizeInfo size = FixPicUtils.getFixImage(mContext, width, height);
                    if (size.isSuperHeight) {
                        holder.getView(R.id.ic_sup_height).setVisibility(View.VISIBLE);
                        ((ImageView) holder.getView(R.id.iv_single_ugc)).setScaleType(ImageView.ScaleType.FIT_START);
                    } else {
                        holder.getView(R.id.ic_sup_height).setVisibility(View.GONE);
                        ((ImageView) holder.getView(R.id.iv_single_ugc)).setScaleType(ImageView.ScaleType.CENTER_CROP);
                    }
                    ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(data.picList.get(0)), R.mipmap.icon_default_750_360,
                            (int) size.width, (int) size.height, holder.getView(R.id.iv_single_ugc));
                }
            });

            holder.getView(R.id.iv_single_ugc).setOnClickListener(v -> {//单张图跳转
                ArrayList<String> list = new ArrayList<>();
                list.add(CommonUtil.getImageFullUrl(data.picList.get(0)));
                NavUtils.gotoLookBigImage(mContext, list, 1);
            });
        } else {
            holder.getView(R.id.iv_single_ugc).setVisibility(View.GONE);
            holder.getView(R.id.ic_sup_height).setVisibility(View.GONE);
            holder.getView(R.id.nine_layout).setVisibility(View.GONE);
        }
    }

    /**
     * Item中 Video Ugc的处理
     *
     * @param holder
     * @param data
     */
    public void covertVideoUGC(BaseViewHolder holder, final UgcInfoResult data) {
        synCommon(holder, data);
        holder.getView(R.id.ll_saw_number).setVisibility(View.VISIBLE);
        //视频
        if (data.videoUrl != null) {
           /* //使得播放器为正方形，根据要求适配正方形小视频
            ViewGroup.LayoutParams  param = holder.getView(R.id.jz_player).getLayoutParams();
            param.width = DisplayUtils.getScreenWidth(mContext) - DisplayUtils.dp2px(mContext,20);//frameLayout宽度;
            param.height = param.width;*/
            ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(data.videoPicUrl), R.mipmap.icon_default_750_360, ((ImageView) holder.getView(R.id.iv_cover)));
            //去全屏播放
            holder.getView(R.id.iv_cover).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (data.shortVideoType == 4) {//直录的小视频上传阿里云
                        FullScreenVideoPlayerActivity.gotoFullScreenVideoPlayerActivity(mContext,
                                CommonUtil.getImageFullUrl(data.videoUrl), data.id);
                    } else if (data.shortVideoType == 5) {//相册上传的小视频上传网宿云
                        FullScreenVideoPlayerActivity.gotoFullScreenVideoPlayerActivity(mContext,
                                ContextHelper.getVodUrl() + data.videoUrl, data.id);
                    }
                }
            });
        }
    }

    /**
     * Item中 Live Ugc的处理
     *
     * @param holder
     * @param data
     */
    public void covertLiveUGC(BaseViewHolder holder, final UgcInfoResult data) {
        synCommon(holder, data);
        holder.getView(R.id.ll_saw_number).setVisibility(View.VISIBLE);
        if (1 == data.liveStatus) {//直播状态 1.直播 2.回放
            ((TextView) holder.getView(R.id.tv_live_type)).setText("直播");
        } else {
            ((TextView) holder.getView(R.id.tv_live_type)).setText("回放");
        }
        //左下角标签
        StringBuilder tagList = new StringBuilder();
        if (data.tagInfoList != null && data.tagInfoList.size() > 0) {
            for (int i = 0; i < data.tagInfoList.size(); i++) {
                tagList.append("· ").append(data.tagInfoList.get(i).name);
            }
            ((TextView) holder.getView(R.id.tv_live_category)).setText(tagList.toString());//直播所属类别
        }

        if (data.picList != null && data.picList.size() > 0) {
            ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(data.picList.get(0)), R.mipmap.icon_default_750_360, (ImageView) holder.getView(R.id.iv_thumb));
        }
        holder.getView(R.id.iv_thumb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data.liveId > 0) {
                    if (data.userInfo != null) {
                        IntentUtil.startVideoClientActivity( data.liveId, data.userInfo.userId,
                                1 == data.liveStatus || 2 == data.liveStatus, data.liveScreenType);
                    } else {
                        IntentUtil.startVideoClientActivity( data.liveId, 0, 1 == data.liveStatus || 2 == data.liveStatus, data.liveScreenType);
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
            menus.add(mContext.getString(R.string.label_menu_cancel_attention));
        }
        if (data == null || data.userInfo == null || !userService.isLoginUser(data.userInfo.userId)) {
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
                                NavUtils.gotoComplaintList(mContext, data.textContent,
                                        data.picList != null && data.picList.size() > 0 ? new ArrayList<>(data.picList) : new ArrayList<>(), data.id);//举报
                            } else if (mContext.getString(R.string.label_menu_complaint).equals(mMenus[whichButton])) {
                                NavUtils.gotoBlackSetting(mContext, data.userInfo.userId, data.id, data.userInfo.nickname);//屏蔽
                            } else if (mContext.getString(R.string.label_menu_cancel_attention).equals(mMenus[whichButton])) {
                                showCancelDialog(data);
                            }
                        }
                    }
                }, null);
        mComplaintDlg.show();
    }

    private Dialog dialog;//确认是否取消关注的Dialog

    private void showCancelDialog(final UgcInfoResult data) {
        dialog = DialogUtil.showMessageDialog(mContext, "是否不再关注此人？", "",
                mContext.getString(R.string.label_btn_ok), mContext.getString(R.string.cancel), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mController.doUnFollow(mContext, data.userInfo.userId);
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

    /****************************************************        Interface         *************************************************************/
    //Item Click
    private OnItemOnclickListener onItemOnclickListener;

    public interface OnItemOnclickListener {
        void onItemClick(int position);
    }

    public void setOnItemOnclickListener(OnItemOnclickListener onItemOnclickListener) {
        this.onItemOnclickListener = onItemOnclickListener;
    }

    //Follow Click
    private OnFollowListener onFollowListener;

    public interface OnFollowListener {
        void onFollowClick(long userId);//被关注者Id
    }

    public void setOnFollowListener(OnFollowListener onFollowListener) {
        this.onFollowListener = onFollowListener;
    }

    public static final class UGCItemType {
        public static final int NORMAL_UGC = 1;
        public static final int VIDEO_UGC = 2;
        public static final int LIVE_UGC = 3;
    }
}
