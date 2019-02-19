package com.newyhy.views.itemview;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.newyhy.config.CircleBizType;
import com.newyhy.utils.DateUtil;
import com.newyhy.utils.ShareUtils;
import com.quanyan.base.util.StringUtil;
import com.quanyan.base.view.customview.ClickPreventableTextView;
import com.quanyan.yhy.R;
import com.yhy.common.eventbus.event.EvBusCircleChangeFollow;
import com.yhy.common.eventbus.event.EvBusCircleChangePraise;
import com.yhy.common.eventbus.event.EvBusCircleDelete;
import com.quanyan.yhy.libanalysis.AnEvent;
import com.quanyan.yhy.libanalysis.Analysis;
import com.quanyan.yhy.net.NetManager;
import com.quanyan.yhy.net.lsn.OnResponseListener;
import com.quanyan.yhy.ui.base.utils.DialogUtil;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.util.HealthCircleTextUtil;
import com.videolibrary.utils.IntentUtil;
import com.yhy.common.beans.net.model.comment.ComSupportInfo;
import com.yhy.common.beans.net.model.discover.UgcInfoResult;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.CommonUtil;
import com.yhy.imageloader.ImageLoadManager;
import com.yhy.location.LocationManager;
import com.yhy.network.YhyCallback;
import com.yhy.network.YhyCode;
import com.yhy.network.api.activitycenter.ActivityCenterApi;
import com.yhy.network.req.activitycenter.PlayReq;
import com.yhy.network.resp.Response;
import com.yhy.network.resp.activitycenter.PlayResp;
import com.yhy.router.YhyRouter;
import com.yhy.service.IUserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * CircleLiveLayout 圈子中小视频 Item 的布局
 * Created by Jiervs on 2018/6/20.
 */

public class CircleUgcCoffeeVideoLayout extends LinearLayout {

    private ImageView iv_header;
    private TextView tv_name;
    private Button btn_follow;
    private ClickPreventableTextView tv_content;
    private ImageView iv_cover;
    private TextView tv_time;
    private TextView tv_date;
    private TextView tv_location;
    private LinearLayout ll_comment;
    private TextView tv_comment_num;
    private LinearLayout ll_support;
    private ImageView iv_support;
    private TextView tv_support_num;
    private LinearLayout ll_more;
    private LinearLayout ll_foot;

    //data
    private String avatar;
    private long id;
    private long userId;
    private String nickName;
    private long createdTime;
    private String textContent;
    private String location;
    private int commentNum;
    private int supportNum;
    private String isSupport;
    private int followType;
    private String videoPicUrl;
    private List<String> picList = new ArrayList<>();
    private int screenType;

    @Autowired
    IUserService userService;

    //recycler的位置
    public int position;
    public HashMap<String, String> extraMap = new HashMap<>();

    public CircleUgcCoffeeVideoLayout(Context context) {
        super(context);
        initViews(context);
    }

    public CircleUgcCoffeeVideoLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public CircleUgcCoffeeVideoLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    public void initViews(Context context) {
        YhyRouter.getInstance().inject(this);
        LayoutInflater.from(context).inflate(R.layout.circle_ugc_coffee_video_layout, this);
        iv_header = findViewById(R.id.iv_header);
        tv_name = findViewById(R.id.tv_name);
        btn_follow = findViewById(R.id.btn_follow);
        tv_content = findViewById(R.id.tv_content);
        iv_cover = findViewById(R.id.iv_cover);
        tv_time = findViewById(R.id.tv_time);
        tv_location = findViewById(R.id.tv_location);
        tv_date = findViewById(R.id.tv_date);
        ll_comment = findViewById(R.id.ll_comment);
        tv_comment_num = findViewById(R.id.tv_comment_num);
        ll_support = findViewById(R.id.ll_support);
        iv_support = findViewById(R.id.iv_support);
        tv_support_num = findViewById(R.id.tv_support_num);
        ll_more = findViewById(R.id.ll_more);
        ll_foot = findViewById(R.id.ll_foot);
    }


    //转换数据t
    public <T> void covertData(T t) {
        if (t instanceof UgcInfoResult) {//UgcInfoResult类型
            UgcInfoResult data = (UgcInfoResult) t;
            if (data.userInfo != null) avatar = data.userInfo.avatar;
            if (data.poiInfo.detail != null) location = data.poiInfo.detail;
            id = data.id;
            if (data.userInfo != null) userId = data.userInfo.userId;
            if (data.userInfo != null) nickName = data.userInfo.nickname;
            createdTime = data.gmtCreated;
            textContent = data.textContent;
            commentNum = data.commentNum;
            supportNum = data.supportNum;
            isSupport = data.isSupport;
            followType = data.type;
            videoPicUrl = data.videoPicUrl;
            picList = data.picList;
            screenType = data.liveScreenType;
        }
    }

    public <T> void setData(Context context, T data) {
        setData(context, data, false);
    }

    public <T> void setData(Context context, T data, boolean isDetail) {
        covertData(data);
        //头像
        ImageLoadManager.loadCircleImage(CommonUtil.getImageFullUrl(avatar), R.mipmap.icon_default_avatar, iv_header);

        iv_header.setOnClickListener(v -> {//跳转个人主页
            NavUtils.gotoMasterHomepage(v.getContext(), userId);
        });
        //昵称
        tv_name.setText(nickName);
        //发布时间
        tv_date.setVisibility(isDetail ? VISIBLE : GONE);
        try {
            if (createdTime > 0) {
                String time = DateUtil.getCreateAt(createdTime);
                tv_time.setText(time);
                tv_date.setText(time);
            } else {
                tv_time.setText("");
                tv_date.setText("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //关注关系
        if (followType == 1 || followType == 2 || userId == userService.getLoginUserId()) {// 关注类型 0:未关注 1:单向关注 2:双向关注
            btn_follow.setVisibility(View.GONE);
        } else {
            btn_follow.setVisibility(View.VISIBLE);
            btn_follow.setBackgroundResource(isDetail ? R.drawable.bg_follow_red : R.drawable.shape_gray_round_bg);
            btn_follow.setTextColor(isDetail ? context.getResources().getColor(R.color.white) : context.getResources().getColor(R.color.red_ying));
        }
        //ugc文本内容
        if (textContent.length() > 0) {
            tv_content.setVisibility(View.VISIBLE);
            tv_content.setText(textContent);
        } else {
            tv_content.setVisibility(View.GONE);
        }
        HealthCircleTextUtil.SetLinkClickIntercept(context, tv_content);
        //Cover
        ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(videoPicUrl), R.mipmap.icon_default_750_360, iv_cover);

        //location
        if (location != null && location.length() > 0) {
            tv_location.setText(location);
            tv_location.setVisibility(VISIBLE);
        } else {
            tv_location.setVisibility(GONE);
        }

        // 底部布局
        if (isDetail) {
            ll_foot.setVisibility(GONE);
        } else {
            ll_foot.setVisibility(VISIBLE);
            //评论数
            tv_comment_num.setText((commentNum > 0 ? (commentNum > 999 ? "999+" : commentNum) : 0) + "");
            //点赞数
            tv_support_num.setText((supportNum > 0 ? (supportNum > 999 ? "999+" : supportNum) : 0) + "");
            if (ValueConstants.TYPE_AVAILABLE.equals(isSupport)) {
                //已点赞
                iv_support.setSelected(true);
            } else {
                iv_support.setSelected(false);
            }
            //更多
            if (userService.isLoginUser(userId)) {
                ll_more.setVisibility(View.GONE);
            } else {
                ll_more.setVisibility(View.VISIBLE);
            }
        }
        /*******************************************************     Logic Method      *****************************************************************/

        btn_follow.setOnClickListener(v -> doFollow(context, userId));

        ll_support.setOnClickListener(v -> doPraise(context, id, isSupport));

        ll_comment.setOnClickListener(v -> {
            if (userService.isLogin()) {
                IntentUtil.startVideoClientActivity(0, 0, false,
                        screenType, "", id);
            } else {
                NavUtils.gotoLoginActivity(context);
            }
        });

        ll_more.setOnClickListener(v -> showMoreMenu(context));

        if (!isDetail)
            this.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 埋点
                    if (extraMap != null) {
                        Analysis.pushEvent(context, AnEvent.ListTrendClick,
                                new Analysis.Builder().
                                        setLat(LocationManager.getInstance().getStorage().getLast_lat()).
                                        setLng(LocationManager.getInstance().getStorage().getLast_lng()).
                                        setId(String.valueOf(id)).
                                        setTab(extraMap.get(Analysis.TAB)).
                                        setPosition(position));

                        Analysis.pushEvent(context, AnEvent.PageSvideoOpen,
                                new Analysis.Builder().
                                        setLat(LocationManager.getInstance().getStorage().getLast_lat()).
                                        setLng(LocationManager.getInstance().getStorage().getLast_lng()).
                                        setId(String.valueOf(id)).
                                        setLastPage(extraMap.get(Analysis.TAG)));
                    }
                    doPlay();
                    IntentUtil.startVideoClientActivity(0, 0, false,
                            screenType, "", id);
                }
            });
    }

/***********************************************************     Api      ************************************************************************/

    /**
     * 关注
     */
    public void doFollow(Context context, long userId) {
        Analysis.pushEvent(context, AnEvent.PageFollew,
                new Analysis.Builder().
                        setLat(LocationManager.getInstance().getStorage().getLast_lat()).
                        setLng(LocationManager.getInstance().getStorage().getLast_lng()).
                        setId(String.valueOf(id)).
                        setType("动态").
                        setList(true));

        if (userService.isLogin()) {

            NetManager.getInstance(context).doAddFollower(userId, new OnResponseListener<Boolean>() {
                @Override
                public void onComplete(boolean isOK, Boolean result, int errorCode, String errorMsg) {
                    if (isOK && result) {
                        btn_follow.setVisibility(GONE);
                        followType = 1;
                        EventBus.getDefault().post(new EvBusCircleChangeFollow(userId, followType));
                    } else {
                        ToastUtil.showToast(context, StringUtil.handlerErrorCode(context, errorCode));
                    }
                }

                @Override
                public void onInternError(int errorCode, String errorMessage) {
                    ToastUtil.showToast(context, StringUtil.handlerErrorCode(context, errorCode));

                }
            });
        } else {
            NavUtils.gotoLoginActivity(context);
        }
    }

    /**
     * 取消关注
     */
    public void doCancleFollow(Context context, long userId) {
        if (userService.isLogin()) {

            NetManager.getInstance(context).doRemoveFollower(userId, new OnResponseListener<Boolean>() {
                @Override
                public void onComplete(boolean isOK, Boolean result, int errorCode, String errorMsg) {
                    if (isOK && result) {
                        followType = 0;
                        btn_follow.setVisibility(VISIBLE);
                        EventBus.getDefault().post(new EvBusCircleChangeFollow(userId, followType));
                    } else {
                        ToastUtil.showToast(context, StringUtil.handlerErrorCode(context, errorCode));
                    }
                }

                @Override
                public void onInternError(int errorCode, String errorMessage) {
                    ToastUtil.showToast(context, StringUtil.handlerErrorCode(context, errorCode));
                }
            });
        } else {
            NavUtils.gotoLoginActivity(context);
        }
    }

    /**
     * 点赞或取消点赞
     */
    public void doPraise(Context context, long id, String isSupport) {
        // 埋点
        Analysis.pushEvent(context, AnEvent.PageLike,
                new Analysis.Builder().
                        setLat(LocationManager.getInstance().getStorage().getLast_lat()).
                        setLng(LocationManager.getInstance().getStorage().getLast_lng()).
                        setId(String.valueOf(id)).
                        setType("动态").
                        setList(true));

        if (userService.isLogin()) {
            doPraiseOrUnPraise(context, id, ValueConstants.TYPE_PRAISE_LIVESUP, ValueConstants.TYPE_AVAILABLE.equals(isSupport) ? ValueConstants.UNPRAISE : ValueConstants.PRAISE);
        } else {
            NavUtils.gotoLoginActivity(context);
        }

    }

    private void doPraiseOrUnPraise(Context context, long outId, String outType, int type) {
        if (userService.isLogin()) {
            NetManager.getInstance(context).doPrasizeForum(outId, outType, type, new OnResponseListener<ComSupportInfo>() {
                @Override
                public void onComplete(boolean isOK, ComSupportInfo result, int errorCode, String errorMsg) {
                    if (isOK) {
                        if (ValueConstants.TYPE_DELETED.equals(result.isSupport)) {
                            supportNum = supportNum - 1 > 0 ? supportNum - 1 : 0;
                            iv_support.setSelected(false);
                        } else {
                            supportNum = supportNum + 1;
                            iv_support.setSelected(true);
                        }
                        isSupport = result.isSupport;
                        tv_support_num.setText((supportNum > 0 ? (supportNum > 999 ? "999+" : supportNum) : 0) + "");
                        EventBus.getDefault().post(new EvBusCircleChangePraise(outId, result.isSupport));

                    } else {
                        ToastUtil.showToast(context, StringUtil.handlerErrorCode(context, errorCode));

                    }
                }

                @Override
                public void onInternError(int errorCode, String errorMessage) {
                    ToastUtil.showToast(context, StringUtil.handlerErrorCode(context, errorCode));

                }
            });
        } else {
            NavUtils.gotoLoginActivity(context);
        }
    }


    /**
     * 更多菜单
     *
     * @param context
     */
    public void showMoreMenu(Context context) {
        ShareUtils.showShareBoard((Activity) context, false, false,
                () -> NavUtils.gotoComplaintList(context, textContent,
                        picList != null && picList.size() > 0 ? new ArrayList<>(picList) : new ArrayList<>(), id),
                () -> NavUtils.gotoBlackSetting(context, userId, id, nickName),
                followType != 0 ? (ShareUtils.OnCancleFollowListener) () -> showCancelDialog(context) : null,
                userId == userService.getLoginUserId() ? (ShareUtils.OnDeleteListener) () -> doDelete(context) : null,
                null, -1, "")
        ;

    }

    private Dialog mCancelDialog;    //取消关注

    private void showCancelDialog(Context context) {
        if (mCancelDialog == null) {
            mCancelDialog = DialogUtil.showMessageDialog(context, "是否不再关注此人？", "",
                    context.getString(R.string.label_btn_ok), context.getString(R.string.cancel), v -> {
                        doCancleFollow(context, userId);
                        mCancelDialog.dismiss();
                    }, v -> mCancelDialog.dismiss());
        }
        mCancelDialog.show();
    }

    private Dialog mDialog;          //删除本条动态对话框

    private void doDelete(Context context) {
        if (mDialog == null) {
            mDialog = DialogUtil.showMessageDialog(context, context.getString(R.string.del_photo), context.getString(R.string.delete_notice),
                    context.getString(R.string.label_btn_ok), context.getString(R.string.label_btn_cancel), v1 -> {
                        deleteSubject(context, id);
                        mDialog.dismiss();
                    }, v1 -> {
                        mDialog.dismiss();
                    });
        }
        mDialog.show();
    }

    public void deleteSubject(Context context, long subjectId) {
        NetManager.getInstance(context).doDelUGC(subjectId, new OnResponseListener<Boolean>() {
            @Override
            public void onComplete(boolean isOK, Boolean result, int errorCode, String errorMsg) {
                if (isOK && result) {
                    EventBus.getDefault().post(new EvBusCircleDelete(id));
                } else {
                    ToastUtil.showToast(context, StringUtil.handlerErrorCode(context, errorCode));
                }
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                ToastUtil.showToast(context, StringUtil.handlerErrorCode(context, errorCode));
            }
        });
    }

    /**
     * 点击事件上传服务器（获取积分）
     */
    public void doPlay() {
        YhyCallback<Response<PlayResp>> callback = new YhyCallback<Response<PlayResp>>() {
            @Override
            public void onSuccess(Response<PlayResp> data) {
            }

            @Override
            public void onFail(YhyCode code, Exception exception) {
            }
        };
        new ActivityCenterApi().play(new PlayReq(new PlayReq.Companion.P(id, CircleBizType.SNS_VEDIO_SHORT)), callback).execAsync();
    }
}

