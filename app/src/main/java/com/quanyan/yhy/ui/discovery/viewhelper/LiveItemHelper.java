package com.quanyan.yhy.ui.discovery.viewhelper;

import android.app.Activity;
import android.content.Context;
import android.nfc.FormatException;
import android.os.Handler;
import android.os.Message;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.harwkin.nb.camera.ImageUtils;
import com.harwkin.nb.camera.TimeUtil;
import com.quanyan.base.util.DateUtil;
import com.quanyan.base.util.HarwkinLogUtil;
import com.quanyan.base.util.ScreenSize;
import com.quanyan.base.util.StringUtil;
import com.quanyan.base.view.customview.NoScrollGridView;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.AnalyDataValue;
import com.quanyan.yhy.eventbus.EvBusComplaintDetailDynamic;
import com.quanyan.yhy.eventbus.EvBusComplaintDynamic;
import com.quanyan.yhy.eventbus.EvBusSubjectInfo;
import com.quanyan.yhy.ui.adapter.base.BaseAdapterHelper;
import com.quanyan.yhy.ui.adapter.base.MultiItemTypeSupport;
import com.quanyan.yhy.ui.adapter.base.QuickAdapter;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.base.utils.TCEventHelper;
import com.quanyan.yhy.ui.club.clubcontroller.ClubController;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.discovery.LiveDetailActivity;
import com.quanyan.yhy.ui.discovery.TopicDetailsActivity;
import com.quanyan.yhy.ui.discovery.view.CopyDialog;
import com.quanyan.yhy.ui.master.activity.MasterHomepageActivity;
import com.quanyan.yhy.util.HealthCircleTextUtil;
import com.videolibrary.utils.IntentUtil;
import com.yhy.common.beans.net.model.VideoInfo;
import com.yhy.common.beans.net.model.club.SubjectInfo;
import com.yhy.common.beans.net.model.comment.ComSupportInfo;
import com.yhy.common.beans.net.model.comment.ComTagInfo;
import com.yhy.common.beans.net.model.discover.UgcInfoResult;
import com.yhy.common.beans.net.model.rc.ComplaintOptionInfo;
import com.yhy.common.beans.net.model.user.UserInfo;
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

import de.greenrobot.event.EventBus;

import static com.umeng.socialize.utils.DeviceConfig.context;

/**
 * 直播列表视图数据处理
 * <p/>
 * Created by zhaoxp on 2015-11-8.
 */
public class LiveItemHelper {

    public static final int STATUS_LIVE = 1;
    public static final int STATUS_LIVE_OVER = 2;
    public static final int STATUS_REPLAY = 3;

    public static final int TYPE_UGC_CONTENT = 2;
    public static final int TYPE_LIVE_CONTENT = 3;

    private static LiveItemHelper instance = new LiveItemHelper();
    @Autowired
    IUserService userService;

    private LiveItemHelper() {
        YhyRouter.getInstance().inject(this);
    }

    public static LiveItemHelper getInstance(){
        return instance;
    }

    /**
     * 设置adapter
     *
     * @param activity
     * @param beans
     * @param typeComment
     * @param typePraise
     * @return
     */
    public static QuickAdapter<UgcInfoResult> setLiveAdapter(final Activity activity, List<UgcInfoResult> beans, final String typeComment, final String typePraise, final AttentionClickOuter attentionClick) {
        return setLiveAdapter(activity, beans, typeComment, typePraise, null, attentionClick);
    }

    /**
     * 设置adapter
     *
     * @param activity
     * @param beans
     * @param typeComment
     * @param typePraise
     * @return
     */
    public static QuickAdapter<UgcInfoResult> setLiveAdapter(final Activity activity, List<UgcInfoResult> beans, final String typeComment, final String typePraise, final String source,
                                                             final AttentionClickOuter attentionClick) {
        QuickAdapter<UgcInfoResult> adapter = new QuickAdapter<UgcInfoResult>(activity, R.layout.cell_live, beans) {
            @Override
            protected void convert(BaseAdapterHelper helper, UgcInfoResult item) {
                handleItem(activity, helper, item, typeComment, typePraise, true, source, attentionClick);
            }
        };
        return adapter;
    }

    /**
     * 设置adapter
     *
     * @param activity
     * @param beans
     * @param typeComment
     * @param typePraise
     * @return
     */
    public static QuickAdapter<UgcInfoResult> setLiveAdapterMultiItem(final Activity activity, List<UgcInfoResult> beans, final String typeComment, final String typePraise, final String source,
                                                                      final AttentionClickOuter attentionClick) {
        QuickAdapter<UgcInfoResult> adapter = new QuickAdapter<UgcInfoResult>(activity, (ArrayList<UgcInfoResult>) beans, new MultiItemTypeSupport<UgcInfoResult>() {
            @Override
            public int getLayoutId(int position, UgcInfoResult ugcInfoResult) {
                if (ugcInfoResult.contentType == TYPE_LIVE_CONTENT) {
                    return R.layout.cell_master_circle_live;
                } else if (!TextUtils.isEmpty(ugcInfoResult.videoPicUrl)) {
                    return R.layout.cell_master_circle_video;
                } else {
                    return R.layout.cell_master_circle_ugc;
                }
//                return 0;
            }

            @Override
            public int getViewTypeCount() {
                return 3;
            }

            @Override
            public int getItemViewType(int postion, UgcInfoResult ugcInfoResult) {
                if (ugcInfoResult.contentType == TYPE_LIVE_CONTENT) {
                    return 0;
                } else if (!TextUtils.isEmpty(ugcInfoResult.videoPicUrl)) {
                    return 1;
                } else {
                    return 2;
                }
//                return 0;
            }
        }) {
            @Override
            protected void convert(BaseAdapterHelper helper, UgcInfoResult item) {
                switch (getItemViewType(helper.getPosition())) {
                    case 0:
                        convertCircleLive(helper.getView(), item, typeComment, typePraise, true,
                                source, attentionClick);
                        break;
                    case 1:
                        convertCircleVideo(helper.getView(), item, typeComment, typePraise, true,
                                source, attentionClick);
                        break;
                    case 2:
                        convertCircleUGC(helper.getView(), helper.getPosition(), item, typeComment, typePraise, true,
                                source, attentionClick);
                        break;
                    default:
                        break;
                }
            }
        };
        return adapter;
    }

    /**
     * 达人圈UGC布局的数据处理
     *
     * @param view
     * @param item
     * @param typeComment
     * @param typePraise
     */
    public static void convertCircleUGC(View view, int position, final UgcInfoResult item,
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
                        tcEvent((Activity) v.getContext(), item.id);
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
        }
        GridViewUtils.setGridViewHeightBasedOnChildren(position, picGridView);
        picGridView.setOnItemClickListener(new PicGridItemClick((Activity) picGridView.getContext(), (ArrayList<String>) picList, item.id));
        picGridView.setOnTouchInvalidPositionListener(new NoScrollGridView.OnTouchInvalidPositionListener() {
            @Override
            public boolean onTouchInvalidPosition(int motionEvent) {
                if (item.userInfo != null) {
                    NavUtils.gotoLiveDetailActivity((Activity) picGridView.getContext(), item.id,
                            item, ValueConstants.TYPE_COMMENT_LIVESUP, ValueConstants.TYPE_PRAISE_LIVESUP, false);
                } else {
                    ToastUtil.showToast(picGridView.getContext(), context.getString(R.string.error_data_exception));
                }
                return false;
            }
        });

        //设置底部横线显示
        if (TopicDetailsActivity.class.getSimpleName().equals(source)) {
            lineDivide.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 达人圈小视频布局的数据处理
     *
     * @param view
     * @param item
     */
    public static void convertCircleVideo(View view, final UgcInfoResult item,
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
                tcEvent((Activity) v.getContext(), item.id);
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

    /**
     * 达人圈直播布局的数据处理
     *
     * @param view
     * @param item
     */
    public static void convertCircleLive(View view, final UgcInfoResult item,
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
                        tcEvent(v.getContext(), item.liveId, item.userInfo.userId);
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

    /**
     * 达人圈列表的common  top  view 数据处理
     *
     * @param view
     * @param item
     * @param typeComment
     * @param typePraise
     * @param source
     * @param attentionClick
     */
    private static void handleCommonTopView(View view, final UgcInfoResult item,
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
//            BaseImgView.loadimg(userHead, ImageUtils.getImageFullUrl(snsUserInfo.avatar), R.mipmap.icon_default_avatar, R.mipmap.icon_default_avatar, R.mipmap.icon_default_avatar,
//                    ImageScaleType.EXACTLY, 128, 128, 180);
            ImageLoadManager.loadCircleImage(CommonUtil.getImageFullUrl(snsUserInfo.avatar), R.mipmap.icon_default_avatar, 128, 128, userHead);

            userHead.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (!MasterHomepageActivity.class.getSimpleName().equals(source)) {
                        analyData((Activity) v.getContext(), snsUserInfo);
//                    NavUtils.gotoPersonalPage(context, snsUserInfo.userId,snsUserInfo.nickname,snsUserInfo.options);
                        NavUtils.gotoMasterHomepage(v.getContext(), snsUserInfo.userId);
                    }
                }
            });
            userName.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (!MasterHomepageActivity.class.getSimpleName().equals(source)) {
                        analyData((Activity) v.getContext(), snsUserInfo);
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
                followText.setVisibility((instance.userService.isLoginUser(snsUserInfo.userId) ? View.GONE : (item.type == 0 ? View.VISIBLE : View.GONE)));
            } else {
                followText.setVisibility((item.type == 0 ? View.VISIBLE : View.GONE));
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

    /**
     * 达人圈列表的common bottom view 数据处理
     *
     * @param view
     * @param item
     * @param typeComment
     * @param typePraise
     * @param isShowCommentPraise
     * @param source
     */
    private static void handleCommonBottomView(View view, final UgcInfoResult item,
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
                    if (getInstance().userService.isLogin()) {
                        NavUtils.gotoLiveDetailActivity((Activity) v.getContext(), item.id, item, typeComment, typePraise, true);
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
        likeLayout.setOnClickListener(new PraiseClick((Activity) likeLayout.getContext(), likeLayout, item, typePraise) {
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
        if (snsUserInfo != null && instance.userService.isLoginUser(snsUserInfo.userId)) {
            moreLayout.setVisibility(View.GONE);
        } else {
            moreLayout.setVisibility(View.VISIBLE);
        }

        moreLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HarwkinLogUtil.info("item.id = " + item.id);
                if (LiveDetailActivity.class.getSimpleName().equals(source)) {
                    de.greenrobot.event.EventBus.getDefault().post(new EvBusComplaintDetailDynamic(item));
                } else {
                    de.greenrobot.event.EventBus.getDefault().post(new EvBusComplaintDynamic(item));
                }
            }
        });

    }

    /**
     * 直播详情页
     * 设置顶部信息
     *
     * @param context context对象
     * @param view    根视图
     * @param item    数据
     */
    public static void handleItemDetail(final Activity context,
                                        String typeComment,
                                        String typePraise,
                                        View view,
                                        final UgcInfoResult item,
                                        final String clubName,
                                        final LinearLayout.LayoutParams layoutParams,
                                        final AttentionClickOuter attentionClick) {
        //标签
        LinearLayout labelsLayout = (LinearLayout) view.findViewById(R.id.cell_live_labels);
        if (item == null) {
            return;
        }
        final UserInfo snsUserInfo = item.userInfo;
        if (snsUserInfo != null) {
            view.findViewById(R.id.cell_live_user_head).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    analyData(context, snsUserInfo);
//                    NavUtils.gotoPersonalPage(context, snsUserInfo.userId,snsUserInfo.nickname,snsUserInfo.options);
                    NavUtils.gotoMasterHomepage(context, snsUserInfo.userId);
                }
            });

            view.findViewById(R.id.cell_live_user_name).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    analyData(context, snsUserInfo);
//                    NavUtils.gotoPersonalPage(context, snsUserInfo.userId,snsUserInfo.nickname,snsUserInfo.options);
                    NavUtils.gotoMasterHomepage(context, snsUserInfo.userId);
                }
            });
            if (!StringUtil.isEmpty(snsUserInfo.avatar)) {
//                BaseImgView.loadimg(((ImageView) view.findViewById(R.id.cell_live_user_head)),
//                        ImageUtils.getImageFullUrl(snsUserInfo.avatar),
//                        R.mipmap.icon_default_avatar,
//                        R.mipmap.icon_default_avatar,
//                        R.mipmap.icon_default_avatar,
//                        ImageScaleType.EXACTLY, -1, -1, 180);
                ImageLoadManager.loadCircleImage(CommonUtil.getImageFullUrl(snsUserInfo.avatar), R.mipmap.icon_default_avatar, (ImageView) view.findViewById(R.id.cell_live_user_head));

            } else {
                ((ImageView) view.findViewById(R.id.cell_live_user_head)).setImageResource(R.mipmap.icon_default_avatar);
            }
            //发布时间
            try {
                ((TextView) view.findViewById(R.id.cell_live_top_time)).setText(DateUtil.getCreateAt(item.gmtCreated));
            } catch (FormatException e) {
                ((TextView) view.findViewById(R.id.cell_live_top_time)).setText("很久以前");
            }
            //用户名
            ((TextView) view.findViewById(R.id.cell_live_user_name)).setText(TextUtils.isEmpty(snsUserInfo.nickname) ? "" : snsUserInfo.nickname);
        }

        if (item.poiInfo != null && !StringUtil.isEmpty(item.poiInfo.detail)) {
            view.findViewById(R.id.cell_live_gps).setVisibility(View.VISIBLE);
            ((TextView) view.findViewById(R.id.cell_live_gps)).setText(item.poiInfo.detail);
        } else {
            view.findViewById(R.id.cell_live_gps).setVisibility(View.GONE);
        }
        //动态内容
        if (TextUtils.isEmpty(item.textContent)) {
            view.findViewById(R.id.cell_live_brief).setVisibility(View.GONE);
        } else {
            view.findViewById(R.id.cell_live_brief).setVisibility(View.VISIBLE);
            ((TextView) view.findViewById(R.id.cell_live_brief)).setText(item.textContent);

            //复制逻辑
            view.findViewById(R.id.cell_live_brief).setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    v.setTag("longclick");
                    CopyDialog copyDialog = new CopyDialog(context, R.style.logindialog, item.textContent);
                    copyDialog.show();
                    TextView txt = (TextView) v;
                    Spannable span = (Spannable) txt.getText();
                    Selection.removeSelection(span);
                    return true;
                }
            });
//            view.findViewById(R.id.cell_live_brief).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    NavUtils.gotoLiveDetailActivity(context, item.id, item, ValueConstants.TYPE_COMMENT_LIVESUP, ValueConstants.TYPE_PRAISE_LIVESUP, false);
//                }
//            });
        }
        List<String> picList = item.picList;
        NoScrollGridView noScrollGridView = (NoScrollGridView) view.findViewById(R.id.cell_live_pic_grid);
        FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.cell_live_detail_video_layout);

        //the online people
        if (item.contentType == TYPE_LIVE_CONTENT) {
            view.findViewById(R.id.cell_live_live_people_num_layout).setVisibility(View.VISIBLE);
            view.findViewById(R.id.cell_live_video_type).setVisibility(View.VISIBLE);
            view.findViewById(R.id.cell_live_video_category_type).setVisibility(View.VISIBLE);
            if ((STATUS_LIVE == item.liveStatus || STATUS_LIVE_OVER == item.liveStatus)) {
                ((TextView) view.findViewById(R.id.cell_live_video_type)).setText("直播");
            } else {
                ((TextView) view.findViewById(R.id.cell_live_video_type)).setText("回放");
            }

            String num;
            if (item.viewNum >= 9990000) {
                ((TextView) view.findViewById(R.id.cell_live_live_people_num)).setText("999");//直播在线人数
                if ((STATUS_LIVE == item.liveStatus || STATUS_LIVE_OVER == item.liveStatus)) {
                    ((TextView) view.findViewById(R.id.cell_live_live_people_num_label)).setText("万+在线");//直播在线人数
                } else {
                    ((TextView) view.findViewById(R.id.cell_live_live_people_num_label)).setText("万+看过");//直播在线人数
                }
            } else if (item.viewNum >= 10000) {
                num = (new DecimalFormat("#.##").format(item.viewNum / 10000.0f));
                ((TextView) view.findViewById(R.id.cell_live_live_people_num)).setText(num);//直播在线人数
                ((TextView) view.findViewById(R.id.cell_live_live_people_num_label)).setText("万在线");//直播在线人数
                if ((STATUS_LIVE == item.liveStatus || STATUS_LIVE_OVER == item.liveStatus)) {
                    ((TextView) view.findViewById(R.id.cell_live_live_people_num_label)).setText("万在线");//直播在线人数
                } else {
                    ((TextView) view.findViewById(R.id.cell_live_live_people_num_label)).setText("万看过");//直播在线人数
                }
            } else {
                ((TextView) view.findViewById(R.id.cell_live_live_people_num)).setText(item.viewNum + "");//直播在线人数
                if ((STATUS_LIVE == item.liveStatus || STATUS_LIVE_OVER == item.liveStatus)) {
                    ((TextView) view.findViewById(R.id.cell_live_live_people_num_label)).setText("在线");//直播在线人数
                } else {
                    ((TextView) view.findViewById(R.id.cell_live_live_people_num_label)).setText("看过");//直播在线人数
                }
            }
            StringBuilder tagList = new StringBuilder();
            if (item.tagInfoList != null && item.tagInfoList.size() > 0) {
                for (int i = 0; i < item.tagInfoList.size(); i++) {
                    tagList.append("·").append(item.tagInfoList.get(i).name);
                }
                ((TextView) view.findViewById(R.id.cell_live_video_category_type)).setText(tagList.toString());//直播所属类别
            }
        } else {
            view.findViewById(R.id.cell_live_live_people_num_layout).setVisibility(View.GONE);
            view.findViewById(R.id.cell_live_video_type).setVisibility(View.GONE);
            view.findViewById(R.id.cell_live_video_category_type).setVisibility(View.GONE);
        }
        if (item.contentType == TYPE_LIVE_CONTENT || !TextUtils.isEmpty(item.videoPicUrl)) {
            ImageView videoTypeImg = ((ImageView) view.findViewById(R.id.cell_live_detail_video_img_play));
            noScrollGridView.setVisibility(View.GONE);
            frameLayout.setVisibility(View.VISIBLE);
            videoTypeImg.setVisibility(View.VISIBLE);
            ((ImageView) view.findViewById(R.id.cell_live_detail_video_img)).setVisibility(View.VISIBLE);
            if (item.contentType == TYPE_LIVE_CONTENT) {
                videoTypeImg.setImageResource(R.mipmap.ic_live_circle_camera);
                if (item.picList != null && item.picList.size() > 0) {
//                    BaseImgView.loadimg((ImageView) view.findViewById(R.id.cell_live_detail_video_img),
//                            ImageUtils.getImageFullUrl(item.picList.get(0)),
//                            R.mipmap.icon_default_310_180,
//                            R.mipmap.icon_default_310_180,
//                            R.mipmap.icon_default_310_180,
//                            ImageScaleType.EXACTLY,
//                            310,
//                            180,
//                            0);
                    ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(item.picList.get(0)), R.mipmap.icon_default_310_180, 310, 180, (ImageView) view.findViewById(R.id.cell_live_detail_video_img));

                } else {
                    ((ImageView) view.findViewById(R.id.cell_live_detail_video_img)).setImageResource(R.mipmap.icon_default_310_180);
                }
            } else {
//                BaseImgView.loadimg((ImageView) view.findViewById(R.id.cell_live_detail_video_img),
//                        ImageUtils.getImageFullUrl(item.videoPicUrl),
//                        R.mipmap.icon_default_310_180,
//                        R.mipmap.icon_default_310_180,
//                        R.mipmap.icon_default_310_180,
//                        ImageScaleType.EXACTLY,
//                        310,
//                        180,
//                        0);
                ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(item.videoPicUrl), R.mipmap.icon_default_310_180, 310, 180, (ImageView) view.findViewById(R.id.cell_live_detail_video_img));

                videoTypeImg.setImageResource(R.mipmap.ic_video_play);
            }
            frameLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.contentType == TYPE_LIVE_CONTENT) {
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
                    } else {
                        tcEvent(context, item.id);
                        VideoInfo videoInfo = new VideoInfo();
                        videoInfo.videoUrl = item.videoUrl;
                        NavUtils.gotoVideoPlayerctivty(context, videoInfo);
                    }
                }
            });
            LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ScreenSize.getScreenWidth(context.getApplicationContext()) * 3 / 5
            );
            frameLayout.setLayoutParams(layoutParams1);
        } else {
            noScrollGridView.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);
        }

        if (item.picList != null && item.picList.size() > 0) {
            QuickAdapter<String> adapter = new QuickAdapter<String>(context, R.layout.imageview, picList) {
                @Override
                protected void convert(BaseAdapterHelper helper, String picPath) {
                    ImageView imageView = helper.getView(R.id.imageview);
                    imageView.setLayoutParams(layoutParams);
                    helper.setImageUrl(R.id.imageview, picPath, 250, 250, R.mipmap.icon_default_150_150);
                }
            };
            noScrollGridView.setVisibility(View.VISIBLE);
            int width = (ScreenSize.getScreenWidth(context.getApplicationContext()) - 30 * 2 - 30 * 2) / 3;
            if (item.picList.size() == 1) {
                noScrollGridView.setVisibility(View.GONE);
                frameLayout.setVisibility(View.VISIBLE);
                ((ImageView) view.findViewById(R.id.cell_live_detail_video_img_play)).setVisibility(View.GONE);
//                BaseImgView.loadimg((ImageView) view.findViewById(R.id.cell_live_detail_video_img),
//                        ImageUtils.getImageFullUrl(item.picList.get(0)),
//                        R.mipmap.icon_default_310_180,
//                        R.mipmap.icon_default_310_180,
//                        R.mipmap.icon_default_310_180,
//                        ImageScaleType.EXACTLY,
//                        600,
//                        600,
//                        0);
                ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(item.picList.get(0)), R.mipmap.icon_default_310_180, 600, 600, (ImageView) view.findViewById(R.id.cell_live_detail_video_img));

                frameLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tcEvent(context, item.id);
                        ArrayList<String> pics = new ArrayList<String>();
                        pics.add(ImageUtils.getImageFullUrl(item.picList.get(0)));
                        NavUtils.gotoLookBigImage(context, pics, 1);
                    }
                });
                LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        600
                );
                frameLayout.setLayoutParams(layoutParams1);
            } else if (item.picList.size() == 4) {
                ViewGroup.LayoutParams lp4 = noScrollGridView.getLayoutParams();
                lp4.width = width * 2 + 30;
                noScrollGridView.setLayoutParams(lp4);
                noScrollGridView.setNumColumns(2);
            } else {
                ViewGroup.LayoutParams lp4 = noScrollGridView.getLayoutParams();
                lp4.width = ViewGroup.LayoutParams.MATCH_PARENT;
                noScrollGridView.setLayoutParams(lp4);
                noScrollGridView.setNumColumns(3);
            }
            noScrollGridView.setAdapter(adapter);
        } else {
            noScrollGridView.setVisibility(View.GONE);
        }

        //点赞和评论
        view.findViewById(R.id.cell_live_like_img).setSelected(ValueConstants.TYPE_AVAILABLE.equals(item.isSupport));//是否点赞
        /**
         * 执行点赞操作
         */
        view.findViewById(R.id.cell_live_like_layout).setOnClickListener(new PraiseClick(context, view.findViewById(R.id.cell_live_like_layout), item, typePraise) {
            @Override
            public void onPraiseSucess(View itemView, ComSupportInfo comSupportInfo) {
                if (comSupportInfo != null) {
                    item.isSupport = comSupportInfo.isSupport;
                } else {
                    item.isSupport = ValueConstants.TYPE_AVAILABLE.equals(item.isSupport) ? ValueConstants.TYPE_DELETED : ValueConstants.TYPE_AVAILABLE;
                }
                itemView.findViewById(R.id.cell_live_like_img).setSelected(ValueConstants.TYPE_AVAILABLE.equals(item.isSupport));
                if (ValueConstants.TYPE_AVAILABLE.equals(item.isSupport)) {
                    item.supportNum = item.supportNum + 1;
                    ((TextView) itemView.findViewById(R.id.cell_live_like)).setText("" + (item.supportNum));
                } else {
                    item.supportNum = item.supportNum - 1;
                    ((TextView) itemView.findViewById(R.id.cell_live_like)).setText("" + (item.supportNum < 0 ? 0 : item.supportNum));
                }

                EventBus.getDefault().post(new EvBusSubjectInfo(item, false));
            }

            @Override
            public void onPraiseError(int errorCode) {
                ToastUtil.showToast(context, StringUtil.handlerErrorCode(context, errorCode));
            }
        });
        //关注操作
        view.findViewById(R.id.tv_attention).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (attentionClick != null) {
                    //analyData(context, item.userInfo);
                    attentionClick.onAttentionClick(item);
                }
            }
        });

        //点赞和评论数量显示
        ((TextView) view.findViewById(R.id.cell_live_like)).setText((item.supportNum > 0 ? (item.supportNum > 999 ? "999+" : item.supportNum) : 0) + "");

        noScrollGridView.setOnItemClickListener(new PicGridItemClick(context, (ArrayList<String>) picList, item.id));
        if (item.userInfo != null && instance.userService.isLoginUser(item.userInfo.userId)) {
            view.findViewById(R.id.cell_live_more_layout).setVisibility(View.GONE);
        } else {
            view.findViewById(R.id.cell_live_more_layout).setVisibility(View.VISIBLE);
        }
        view.findViewById(R.id.cell_live_more_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                de.greenrobot.event.EventBus.getDefault().post(new EvBusComplaintDetailDynamic(item));
            }
        });
        if (snsUserInfo != null) {
            view.findViewById(R.id.tv_attention).setVisibility(
                    (instance.userService.isLoginUser(snsUserInfo.userId) ? View.GONE : (item.type == 0 ? View.VISIBLE : View.GONE)));
        } else {
            view.findViewById(R.id.tv_attention).setVisibility(item.type == 0 ? View.VISIBLE : View.GONE);
        }
        HealthCircleTextUtil.SetLinkClickIntercept(context, (TextView) view.findViewById(R.id.cell_live_brief));
    }

    /**
     * 列表数据显示
     * NOTE：此方法已被废弃，
     * 具体请到{@link #setLiveAdapterMultiItem(Activity, List, String, String, String,
     * AttentionClickOuter)}
     *
     * @param context context
     * @param helper  视图控制类
     * @param item    数据
     */
    public static void handleItem(final Activity context,
                                  final BaseAdapterHelper helper,
                                  final UgcInfoResult item,
                                  final String typeComment,
                                  final String typePraise,
                                  boolean isShowCommentPraise,
                                  final String source, final AttentionClickOuter attentionClick) {
        final UserInfo snsUserInfo = item.userInfo;
        if (snsUserInfo != null) {
            //用户头像
            helper.setImageUrlRound(R.id.cell_live_user_head,
                    ImageUtils.getImageFullUrl(snsUserInfo.avatar), 128, 128,
                    R.mipmap.icon_default_avatar);
            helper.setOnClickListener(R.id.cell_live_user_head, new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    analyData(context, snsUserInfo);
//                    NavUtils.gotoPersonalPage(context, snsUserInfo.userId,snsUserInfo.nickname,snsUserInfo.options);
                    NavUtils.gotoMasterHomepage(context, snsUserInfo.userId);
                }
            });
            //用户名
            helper.setText(R.id.cell_live_user_name, TextUtils.isEmpty(snsUserInfo.nickname) ? "" : snsUserInfo.nickname);
            helper.setOnClickListener(R.id.cell_live_user_name, new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    analyData(context, snsUserInfo);
//                    NavUtils.gotoPersonalPage(context, snsUserInfo.userId,snsUserInfo.nickname,snsUserInfo.options);
                    NavUtils.gotoMasterHomepage(context, snsUserInfo.userId);
                }
            });
        } else {
            helper.setBackgroundRes(R.id.cell_live_user_head, R.mipmap.icon_default_avatar);
            helper.setText(R.id.cell_live_user_name, "");
            helper.setOnClickListener(R.id.cell_live_user_head, new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                }
            });
            helper.setOnClickListener(R.id.cell_live_user_name, new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
        //位置信息
        if (item.poiInfo != null && !StringUtil.isEmpty(item.poiInfo.detail)) {
            helper.setVisible(R.id.cell_live_gps, true);
            helper.setText(R.id.cell_live_gps, item.poiInfo.detail);
        } else {
            helper.setVisible(R.id.cell_live_gps, false);
        }
        //发布时间
        try {
            helper.setText(R.id.cell_live_top_time, DateUtil.getCreateAt(item.gmtCreated));
        } catch (FormatException e) {
            helper.setText(R.id.cell_live_top_time, context.getString(R.string.label_long_long_ago));
        }
        //动态内容
        if (TextUtils.isEmpty(item.textContent)) {
            helper.setVisible(R.id.cell_live_brief, false);
        } else {
            helper.setVisible(R.id.cell_live_brief, true);
            helper.setText(R.id.cell_live_brief, item.textContent);

            //复制逻辑
            helper.setOnLongClickListener(R.id.cell_live_brief, new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    v.setTag("longclick");
                    CopyDialog copyDialog = new CopyDialog(context, R.style.logindialog, item.textContent);
                    copyDialog.show();

                    TextView txt = (TextView) v;
                    Spannable span = (Spannable) txt.getText();
                    Selection.removeSelection(span);
                    return true;
                }
            });
            helper.setOnClickListener(R.id.cell_live_brief, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavUtils.gotoLiveDetailActivity(context, item.id, item, typeComment, typePraise, false);
                }
            });
            //分散对齐
//			TextView contentView = (TextView)helper.getView(R.id.cell_live_brief);
//			TextJustification.justify(contentView, ScreenUtil.getScreenWidth(context));
        }
        if (!isShowCommentPraise) {
            helper.setVisible(R.id.cell_live_like_layout, false);
            helper.setVisible(R.id.cell_live_comment_layout, false);
        } else {
            //点赞和评论
            helper.setSelected(R.id.cell_live_like_img, ValueConstants.TYPE_AVAILABLE.equals(item.isSupport));//是否点赞
            helper.setOnClickListener(R.id.cell_live_comment_layout, new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //评论进详情页
                    if (getInstance().userService.isLogin()) {
                        NavUtils.gotoLiveDetailActivity(context, item.id, item, typeComment, typePraise, true);
                    } else {
                        NavUtils.gotoLoginActivity(context);
                    }
                }
            });
            /**
             * 执行点赞操作
             */
            helper.setOnClickListener(R.id.cell_live_like_layout, new PraiseClick(context, helper.getView(R.id.cell_live_like_layout), item, typePraise) {
                @Override
                public void onPraiseSucess(View itemView, ComSupportInfo comSupportInfo) {
                    if (comSupportInfo != null) {
                        item.isSupport = comSupportInfo.isSupport;
                    } else {
                        item.isSupport = ValueConstants.TYPE_AVAILABLE.equals(item.isSupport) ? ValueConstants.TYPE_DELETED : ValueConstants.TYPE_AVAILABLE;
                    }
                    itemView.findViewById(R.id.cell_live_like_img).setSelected(ValueConstants.TYPE_AVAILABLE.equals(item.isSupport));
                    if (ValueConstants.TYPE_AVAILABLE.equals(item.isSupport)) {
                        item.supportNum = item.supportNum + 1;
                        ((TextView) itemView.findViewById(R.id.cell_live_like)).setText("" + (item.supportNum));
                    } else {
                        item.supportNum = item.supportNum - 1;
                        ((TextView) itemView.findViewById(R.id.cell_live_like)).setText("" + (item.supportNum < 0 ? 0 : item.supportNum));
                    }
                }

                @Override
                public void onPraiseError(int errorCode) {
                    ToastUtil.showToast(context, StringUtil.handlerErrorCode(context, errorCode));
                }
            });

            if (MasterHomepageActivity.class.getSimpleName().equals(source)) {
                helper.setVisible(R.id.tv_attention, false);
            }

            //设置底部横线显示
            if (TopicDetailsActivity.class.getSimpleName().equals(source)) {
                helper.setVisible(R.id.v_line, true);
            }

            //关注操作
            helper.setOnClickListener(R.id.tv_attention, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (attentionClick != null) {
                        attentionClick.onAttentionClick(item);
                    }
                }
            });

            //点赞和评论数量显示
            helper.setText(R.id.cell_live_comment, (item.commentNum > 0 ? (item.commentNum > 999 ? "999+" : item.commentNum) : 0) + "");
            helper.setText(R.id.cell_live_like, (item.supportNum > 0 ? (item.supportNum > 999 ? "999+" : item.supportNum) : 0) + "");
        }
        NoScrollGridView noScrollGridView = helper.getView(R.id.cell_live_pic_grid);
        //the online people
        if (item.contentType == TYPE_LIVE_CONTENT) {
            helper.setVisible(R.id.cell_live_live_people_num_layout, true);
            helper.setVisible(R.id.cell_live_video_type, true);
            if ((STATUS_LIVE == item.liveStatus || STATUS_LIVE_OVER == item.liveStatus)) {
                helper.setText(R.id.cell_live_video_type, "直播");
            } else {
                helper.setText(R.id.cell_live_video_type, "回放");
            }

            String num;
            if (item.viewNum >= 999000) {
                helper.setText(R.id.cell_live_live_people_num, "999+");//直播在线人数
                if ((STATUS_LIVE == item.liveStatus || STATUS_LIVE_OVER == item.liveStatus)) {
                    helper.setText(R.id.cell_live_live_people_num_label, "万在线");//直播在线人数
                } else {
                    helper.setText(R.id.cell_live_live_people_num_label, "万看过");//直播在线人数
                }
            } else if (item.viewNum >= 10000) {
                num = (new DecimalFormat("#.##").format(item.viewNum / 10000.0f));
                helper.setText(R.id.cell_live_live_people_num, num);//直播在线人数
                helper.setText(R.id.cell_live_live_people_num_label, "万在线");//直播在线人数
                if ((STATUS_LIVE == item.liveStatus || STATUS_LIVE_OVER == item.liveStatus)) {
                    helper.setText(R.id.cell_live_live_people_num_label, "万在线");//直播在线人数
                } else {
                    helper.setText(R.id.cell_live_live_people_num_label, "万看过");//直播在线人数
                }
            } else {
                helper.setText(R.id.cell_live_live_people_num, item.viewNum + "");//直播在线人数
                if ((STATUS_LIVE == item.liveStatus || STATUS_LIVE_OVER == item.liveStatus)) {
                    helper.setText(R.id.cell_live_live_people_num_label, "在线");//直播在线人数
                } else {
                    helper.setText(R.id.cell_live_live_people_num_label, "看过");//直播在线人数
                }
            }
            StringBuilder tagList = new StringBuilder();
            if (item.tagInfoList != null && item.tagInfoList.size() > 0) {
                for (int i = 0; i < item.tagInfoList.size(); i++) {
                    tagList.append("·").append(item.tagInfoList.get(i).name);
                }
                helper.setVisible(R.id.cell_live_video_category_type, true);
                helper.setText(R.id.cell_live_video_category_type, tagList.toString());//直播所属类别
            }
        } else {
            helper.setVisible(R.id.cell_live_live_people_num_layout, false);
            helper.setVisible(R.id.cell_live_video_type, false);
            helper.setVisible(R.id.cell_live_video_category_type, false);
        }
        if (item.contentType == TYPE_LIVE_CONTENT || !TextUtils.isEmpty(item.videoPicUrl)) {
            noScrollGridView.setVisibility(View.GONE);
            FrameLayout frameLayout = helper.getView(R.id.cell_live_detail_video_layout);
            helper.setVisible(R.id.cell_live_detail_video_layout, true);
            helper.setVisible(R.id.cell_live_detail_video_img_play, true);
            if (item.contentType == TYPE_LIVE_CONTENT) {
                helper.setImageResource(R.id.cell_live_detail_video_img_play, R.mipmap.ic_live_circle_camera);
                if (item.picList != null && item.picList.size() > 0) {
                    helper.setImageUrl(R.id.cell_live_detail_video_img,
                            ImageUtils.getImageFullUrl(item.picList.get(0)),
                            -1,
                            -1,
                            R.mipmap.icon_default_310_180);
                } else {
                    helper.setImageResource(R.id.cell_live_detail_video_img, R.mipmap.icon_default_310_180);
                }
            } else {
                helper.setImageResource(R.id.cell_live_detail_video_img_play, R.mipmap.ic_video_play);
                helper.setImageUrl(R.id.cell_live_detail_video_img,
                        ImageUtils.getImageFullUrl(item.videoPicUrl),
                        -1,
                        -1,
                        R.mipmap.icon_default_310_180);
            }
            helper.setOnClickListener(R.id.cell_live_detail_video_layout, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.contentType == TYPE_UGC_CONTENT) {
                        tcEvent((Activity) v.getContext(), item.id);
                        VideoInfo videoInfo = new VideoInfo();
                        videoInfo.videoUrl = item.videoUrl;
                        NavUtils.gotoVideoPlayerctivty(v.getContext(), videoInfo);
                    } else {
                        if (item.liveId > 0) {
                            if (item.userInfo != null) {
                                tcEvent(v.getContext(), item.liveId, item.userInfo.userId);
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
                }
            });
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ScreenSize.getScreenWidth(context.getApplicationContext()) * 3 / 5
            );
            frameLayout.setLayoutParams(layoutParams);
        } else {
            helper.setVisible(R.id.cell_live_detail_video_layout, false);
            //图片
            final List<String> picList = item.picList;
            if (noScrollGridView.getAdapter() == null) {
                noScrollGridView.setAdapter(setPicPreviewAdapter(context));
            }
            if (item.picList != null && item.picList.size() > 0) {
                noScrollGridView.setVisibility(View.VISIBLE);
                helper.setVisible(R.id.cell_live_detail_video_layout, false);
                int width = (ScreenSize.getScreenWidth(context.getApplicationContext()) - 30 * 2 - 30 * 2) / 3;
                if (item.picList.size() == 1) {
                    noScrollGridView.setNumColumns(1);
                    noScrollGridView.setVisibility(View.GONE);
                    FrameLayout frameLayout = helper.getView(R.id.cell_live_detail_video_layout);
                    helper.setVisible(R.id.cell_live_detail_video_layout, true);
                    helper.setVisible(R.id.cell_live_detail_video_img_play, false);
                    helper.setImageUrl(R.id.cell_live_detail_video_img,
                            ImageUtils.getImageFullUrl(item.picList.get(0)),
                            600,
                            600,
                            R.mipmap.icon_default_310_180);
                    helper.setOnClickListener(R.id.cell_live_detail_video_layout, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            tcEvent(context, item.id);
                            ArrayList<String> pics = new ArrayList<String>();
                            pics.add(ImageUtils.getImageFullUrl(item.picList.get(0)));
                            NavUtils.gotoLookBigImage(context, pics, 1);
                        }
                    });
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            600
                    );
                    frameLayout.setLayoutParams(layoutParams);
                } else if (item.picList.size() == 4) {
                    ViewGroup.LayoutParams lp4 = noScrollGridView.getLayoutParams();
                    lp4.width = width * 2 + 30;
                    noScrollGridView.setLayoutParams(lp4);
                    noScrollGridView.setNumColumns(2);
                } else {
                    ViewGroup.LayoutParams lp4 = noScrollGridView.getLayoutParams();
                    lp4.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    noScrollGridView.setLayoutParams(lp4);
                    noScrollGridView.setNumColumns(3);
                }
                ((QuickAdapter) noScrollGridView.getAdapter()).replaceAll(picList);
            } else {
                ((QuickAdapter) noScrollGridView.getAdapter()).clear();
                noScrollGridView.setVisibility(View.GONE);
            }
//		else {
//			noScrollGridView.setVisibility(View.GONE);
//		}
            GridViewUtils.setGridViewHeightBasedOnChildren(helper.getPosition(), noScrollGridView);
            noScrollGridView.setOnItemClickListener(new PicGridItemClick(context, (ArrayList<String>) picList, item.id));
            noScrollGridView.setOnTouchInvalidPositionListener(new NoScrollGridView.OnTouchInvalidPositionListener() {
                @Override
                public boolean onTouchInvalidPosition(int motionEvent) {
                    if (item.userInfo != null) {
                        NavUtils.gotoLiveDetailActivity(context, item.id, item, ValueConstants.TYPE_COMMENT_LIVESUP, ValueConstants.TYPE_PRAISE_LIVESUP, false);
                    } else {
                        ToastUtil.showToast(context, context.getString(R.string.error_data_exception));
                    }
                    return false;
                }
            });
        }

        if (snsUserInfo != null && instance.userService.isLoginUser(snsUserInfo.userId)) {
            helper.setVisible(R.id.cell_live_more_layout, false);
        } else {
            helper.setVisible(R.id.cell_live_more_layout, true);
        }

        helper.setOnClickListener(R.id.cell_live_more_layout, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HarwkinLogUtil.info("item.id = " + item.id);
                de.greenrobot.event.EventBus.getDefault().post(new EvBusComplaintDynamic(item));
            }
        });
        if (MasterHomepageActivity.class.getSimpleName().equals(source)) {
            helper.setVisible(R.id.tv_attention, false);
        } else {
            if (snsUserInfo != null) {
                helper.setVisible(R.id.tv_attention, (instance.userService.isLoginUser(snsUserInfo.userId) ? false : (item.type == 0 ? true : false)));
            } else {
                helper.setVisible(R.id.tv_attention, (item.type == 0 ? true : false));
            }
        }
        HealthCircleTextUtil.SetLinkClickIntercept(context, (TextView) helper.getView(R.id.cell_live_brief));
    }

    private static void tcEvent(Context context, long liveId, long userId) {
        Map<String, String> map = new HashMap<>();
        map.put(AnalyDataValue.KEY_LIVE_ID, liveId + "");
        map.put(AnalyDataValue.KEY_UID, userId + "");
        TCEventHelper.onEvent(context, AnalyDataValue.DISCOVER_LIVE_ITEM_CLICK, map);
    }

    //图片打点
    private static void tcEvent(Activity context, long id) {
        TCEventHelper.onEvent(context, AnalyDataValue.DISCOVERY_PIC_AND_VIDEO_CLICK, id + "");
    }

    //打点
    private static void analyData(Activity context, UserInfo snsUserInfo) {
        Map<String, String> map = new HashMap();
        map.put(AnalyDataValue.KEY_MID, snsUserInfo.userId + "");
        map.put(AnalyDataValue.KEY_MNAME, snsUserInfo.nickname);
        TCEventHelper.onEvent(context, AnalyDataValue.DISCOVERY_USER_HEADER, map);
    }


    private static int getScreenWidth(Activity context) {
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * 添加标签
     */
    private static void addLabels(Activity context, LinearLayout labelsLayout, final SubjectInfo item) {
        int labelViewPaddingLR = ScreenSize.convertDIP2PX(context.getApplicationContext(), 8);
        int labelViewPaddingTB = ScreenSize.convertDIP2PX(context.getApplicationContext(), 3);
        labelsLayout.removeAllViews();
        if (item.comTagList != null) {
            int tagSize = item.comTagList.size();
            List<ComTagInfo> comTagInfos = item.comTagList;
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            for (int i = 0; i < tagSize; i++) {
                TextView textView = new TextView(context.getApplicationContext());
                textView.setTextSize(12);
                textView.setBackgroundResource(R.drawable.bg_live_label_selector);
                textView.setPadding(labelViewPaddingLR, labelViewPaddingTB, labelViewPaddingLR, labelViewPaddingTB);
                textView.setTextColor(context.getResources().getColor(R.color.neu_fa4619));
                textView.setLayoutParams(layoutParams);
//				if (i != tagSize - 1) {
//					textView.setText(comTagInfos.get(i).name + "·");
//				} else {
                textView.setText(comTagInfos.get(i).name);
//				}
                textView.setOnClickListener(new TagClick(context, comTagInfos.get(i).name, comTagInfos.get(i).id));
                labelsLayout.addView(textView);
            }
        }
    }

    /**
     * 投诉图标
     */
//    public interface OnComplaintClickListener {
//        void onComplaintClick(SubjectInfo subjectInfo);
//    }

    /**
     * 点赞
     */
    public static abstract class PraiseClick implements View.OnClickListener {

        private Activity mContext;
        private View mItemView;
        private UgcInfoResult mItemSubject;
        private ClubController mClubController;
        private String type;
        private boolean isClick = false;
        private Handler mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                isClick = false;
                switch (msg.what) {
                    case ValueConstants.MSG_PRAISE_OK:
                        //点赞成功
                        ComSupportInfo comSupportInfo = (ComSupportInfo) msg.obj;
                        onPraiseSucess(mItemView, comSupportInfo);
                        break;
                    case ValueConstants.MSG_PRAISE_ERROR:
                        onPraiseError(msg.arg1);
                        break;
                }
                return false;
            }
        });

        /**
         * 点赞
         *
         * @param context
         * @param itemView
         * @param type
         */
        public PraiseClick(Activity context, View itemView, UgcInfoResult itemSubject, String type) {
            this.mContext = context;
            mClubController = new ClubController(context, mHandler);
            this.mItemView = itemView;
            this.mItemSubject = itemSubject;
            this.type = type;
        }

        @Override
        public void onClick(View v) {
            //点赞
            if (TimeUtil.isFastDoubleClick() || isClick) {
                return;
            }
            if (getInstance().userService.isLogin()) {
                if (ValueConstants.TYPE_AVAILABLE.equals(mItemSubject.isSupport)) {
                    TCEventHelper.onEvent(mContext, AnalyDataValue.Find_DirectCancel, mItemSubject.id + "");
                    mClubController.doAddNewPraiseToComment(mContext, mItemSubject.id, type, ValueConstants.UNPRAISE);
                } else if (ValueConstants.TYPE_DELETED.equals(mItemSubject.isSupport)) {
                    TCEventHelper.onEvent(mContext, AnalyDataValue.Find_DirectLike, mItemSubject.id + "");
                    mClubController.doAddNewPraiseToComment(mContext, mItemSubject.id, type, ValueConstants.PRAISE);
                }
                isClick = true;
            } else {
                NavUtils.gotoLoginActivity(mContext);
            }
        }

        /**
         * 抽象方法，点赞成功后调用
         *
         * @param itemView
         * @param comSupportInfo
         */
        public abstract void onPraiseSucess(View itemView, ComSupportInfo comSupportInfo);

        /**
         * 抽象方法，点赞失败后调用
         */
        public abstract void onPraiseError(int errorCode);
    }

    /**
     * 标签筛选
     */
    private static class TagClick implements View.OnClickListener {

        private Activity mActivity;
        private long mTagID;
        private String mTagTitle;

        public TagClick(Activity context, String tagTitle, long tagID) {
            mActivity = context;
            mTagID = tagID;
            mTagTitle = tagTitle;
        }

        @Override
        public void onClick(View v) {
//            TCEventHelper.onEvent(mActivity, AnalyDataValue.LIVE_LIST_TAG_CLICK, mTagID + "");
//            NavUtils.gotoLiveListActivity(mActivity, mTagTitle, mTagID);
        }
    }

    /**
     * 查看大图
     */
    public static class PicGridItemClick implements AdapterView.OnItemClickListener {

        private ArrayList<String> mStrings;
        private Activity mActivity;
        private long mLiveId;

        public PicGridItemClick(Activity context, ArrayList<String> picStrings, long id) {
            this.mStrings = picStrings;
            this.mActivity = context;
            this.mLiveId = id;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            tcEvent(mActivity, mLiveId);
            ArrayList<String> tmpList = new ArrayList<>();
            for (String str : mStrings) {
                tmpList.add(ImageUtils.getImageFullUrl(str));
            }
            NavUtils.gotoLookBigImage(mActivity, tmpList, position);
        }
    }

    public static QuickAdapter<String> setPicPreviewAdapter(Context context) {
        int width = (ScreenSize.getScreenWidth(context.getApplicationContext()) - 30 * 2 - 30 * 2) / 3;
        final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, width);
        QuickAdapter<String> adapter = new QuickAdapter<String>(context, R.layout.imageview, new ArrayList<String>()) {
            @Override
            protected void convert(BaseAdapterHelper helper, String picPath) {
                ImageView imageView = helper.getView(R.id.imageview);
//					int width = ScreenSize.dp2px(context.getApplicationContext(), 100);
//					AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(width, width);
                imageView.setLayoutParams(layoutParams);
                helper.setImageUrl(R.id.imageview, picPath, 250, 250, R.mipmap.icon_default_150_150);
            }
        };
        return adapter;
    }

    /**
     * 处理投诉列表
     */
    public static void handleComplaintItem(BaseAdapterHelper helper, ComplaintOptionInfo item) {
        helper.setText(R.id.tv_title, item.title);
        helper.setVisible(R.id.iv_check, item.isChecked);
    }

    public interface AttentionClickOuter {
        void onAttentionClick(UgcInfoResult ugcInfoResult);
    }

    /**
     * 关注点击
     */
    public static abstract class AttentionClick implements View.OnClickListener {

        private Activity mContext;
        private View mItemView;
        private UgcInfoResult mItemSubject;
        private ClubController mClubController;
        private String type;
        private Handler mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case ValueConstants.MSG_ATTENTION_OK:
                        //关注成功
                        onAttentionSuccess(mItemView, true);
                        break;
                    case ValueConstants.MSG_ATTENTION_KO:
                        onAttentionError(msg.arg1);
                        break;
                }
                return false;
            }
        });

        /**
         * 点赞
         *
         * @param context
         * @param itemView
         * @param type
         */
        public AttentionClick(Activity context, View itemView, UgcInfoResult itemSubject, String type) {
            this.mContext = context;
            mClubController = new ClubController(context, mHandler);
            this.mItemView = itemView;
            this.mItemSubject = itemSubject;
            this.type = type;
        }

        @Override
        public void onClick(View v) {
            //点赞
            if (TimeUtil.isFastDoubleClick()) {
                return;
            }
            if (getInstance().userService.isLogin()) {
//                TCEventHelper.onEvent(mContext, AnalyDataValue.Find_DirectCancel, mItemSubject.id + "");
                if (mItemSubject != null && mItemSubject.userInfo != null) {
                    mClubController.doAddAttention(mContext, mItemSubject.userInfo.userId);
                }
            } else {
                NavUtils.gotoLoginActivity(mContext);
            }
        }

        private void analyDataTwo(Activity mContext, UserInfo userInfo) {
            Map<String, String> map = new HashMap();
            map.put(AnalyDataValue.KEY_MID, userInfo.userId + "");
            map.put(AnalyDataValue.KEY_MNAME, userInfo.nickname);
            TCEventHelper.onEvent(mContext, AnalyDataValue.DISCOVERY_FOLLOW_CLICK, map);
        }

        /**
         * 抽象方法，关注成功后调用
         *
         * @param itemView
         * @param isAttentioned
         */
        public abstract void onAttentionSuccess(View itemView, boolean isAttentioned);

        /**
         * 抽象方法，关注失败后调用
         */
        public abstract void onAttentionError(int errorCode);
    }
}

