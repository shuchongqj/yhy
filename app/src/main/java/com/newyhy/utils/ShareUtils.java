package com.newyhy.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.harwkin.nb.camera.ImageUtils;
import com.newyhy.config.CircleBizType;
import com.newyhy.config.CircleShareChannel;
import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.R;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.yhy.common.beans.net.model.ShareBean;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.AndroidUtils;
import com.yhy.common.utils.CommonUtil;
import com.yhy.network.YhyCallback;
import com.yhy.network.YhyCode;
import com.yhy.network.api.activitycenter.ActivityCenterApi;
import com.yhy.network.req.activitycenter.ShareReq;
import com.yhy.network.resp.Response;
import com.yhy.network.resp.activitycenter.ShareResp;

/**
 * 分享弹窗
 * Created by yangboxue on 2018/5/23.
 */

public class ShareUtils {

    private static Dialog sShareDlg;

    public static final int WEIXIN = 1;
    public static final int WEIXIN_CIRCLE = 2;
    public static final int QQ = 3;
    public static final int WEIBO = 4;
    public static final int QUANYAN = 5;

    /**
     * 一般分享
     *
     * @param activity activity
     * @param title    标题, n
     * @param text     文本
     * @param url      small_icon
     * @param imageUrl 图片url
     */
    public static void showShareBoard(Activity activity, String title, String text, String url, String imageUrl) {
        showShareBoard(activity, title, text, url, imageUrl, true, false, false, false, null, null, null, null, null, -1, "", null);
    }

    public static void showShareBoard(Activity activity, String title, String text, String url, String imageUrl, ShareExtraInfo shareExtraInfo) {
        showShareBoard(activity, title, text, url, imageUrl, true, false, false, false, null, null, null, null, null, -1, "", shareExtraInfo);
    }

    /**
     * 带扩展的分享
     *
     * @param activity       activity
     * @param title          标题, n
     * @param text           文本
     * @param url            small_icon
     * @param imageUrl       图片url
     * @param showSms        发送短信分享
     * @param canCopy        可以复制链接
     * @param reportListener 屏蔽接口
     * @param shieldListener 举报接口
     * @param praiseListener 点赞接口
     */
    public static void showShareBoard(Activity activity, String title, String text, String url, String imageUrl, boolean showSms, boolean canCopy, OnReportListener reportListener, OnShieldListener shieldListener, OnCancleFollowListener cancleFollowListener, OnDeleteListener deleteListener, OnPraiseListener praiseListener, int praiseNum, String praiseState, ShareExtraInfo shareExtraInfo) {
        showShareBoard(activity, title, text, url, imageUrl, true, showSms, canCopy, false, reportListener, shieldListener, cancleFollowListener, deleteListener, praiseListener, praiseNum, praiseState, shareExtraInfo);
    }

    /**
     * 分享单张网络图片
     *
     * @param activity
     */
    public static void showShareBoard(Activity activity, String bitmap) {
        showShareBoard(activity, "", "", "", bitmap, true, false, false, true, null, null, null, null, null, -1, "", null);
    }

    /**
     * 不带分享,只有扩展
     *
     * @param activity
     */
    public static void showShareBoard(Activity activity, boolean showSms, boolean canCopy, OnReportListener reportListener, OnShieldListener shieldListener, OnCancleFollowListener cancleFollowListener, OnDeleteListener deleteListener, OnPraiseListener praiseListener, int praiseNum, String praiseState) {
        showShareBoard(activity, "", "", "", "", false, showSms, canCopy, false, reportListener, shieldListener, cancleFollowListener, deleteListener, praiseListener, praiseNum, praiseState, null);
    }

    /**
     * 分享
     *
     * @param activity       activity
     * @param title          标题, n
     * @param text           文本
     * @param url            small_icon
     * @param imageUrl       图片url
     * @param showSms        发送短信分享
     * @param canCopy        可以复制链接
     * @param onlyImg        只分享单张图片
     * @param reportListener 屏蔽接口
     * @param shieldListener 举报接口
     */
    public static void showShareBoard(final Activity activity, final String title, final String text, final String url, final String imageUrl, boolean base,
                                      boolean showSms, boolean canCopy, final boolean onlyImg,
                                      OnReportListener reportListener, OnShieldListener shieldListener, OnCancleFollowListener cancleFollowListener, OnDeleteListener deleteListener,
                                      OnPraiseListener praiseListener, int praiseNum, String praiseState, ShareExtraInfo shareExtraInfo) {
        sShareDlg = new Dialog(activity);
        sShareDlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        sShareDlg.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        View view = LayoutInflater.from(activity).inflate(R.layout.dlg_share_board, null);

        // 分享监听
        View.OnClickListener listener = v -> {
            // 选择平台
            SHARE_MEDIA platform = null;
            ShareBean shareBean = new ShareBean();
            switch (v.getId()) {
                case R.id.tv_share_cancel:
                    dismissShareBoard();
                    return;
                case R.id.llyt_copy:
                    copyToClipboard(activity, url);
                    dismissShareBoard();
                    return;
                case R.id.llyt_shield:
                    if (shieldListener != null)
                        shieldListener.onShield();
                    dismissShareBoard();
                    return;
                case R.id.llyt_report:
                    if (reportListener != null)
                        reportListener.onReport();
                    dismissShareBoard();
                    return;
                case R.id.llyt_cancle_follow:
                    if (cancleFollowListener != null)
                        cancleFollowListener.onCancleFollow();
                    dismissShareBoard();
                    return;
                case R.id.llyt_delete:
                    if (deleteListener != null)
                        deleteListener.onDelete();
                    dismissShareBoard();
                    return;
                case R.id.llyt_praise:
                    if (praiseListener != null)
                        praiseListener.onPraise();
                    dismissShareBoard();
                    return;
                case R.id.llyt_duanxin:
                    String shareText = "";
                    if (!TextUtils.isEmpty(title)) {
                        shareText += title;
                    }

                    if (!TextUtils.isEmpty(text)) {
                        shareText += text;
                    }

                    if (!TextUtils.isEmpty(url)) {
                        shareText += url;
                    }
                    sendSms(activity, shareText);
                    dismissShareBoard();
                    return;
                case R.id.llyt_weixin:
                    platform = SHARE_MEDIA.WEIXIN;
                    shareBean.shareWay = ShareUtils.WEIXIN;
                    if (shareExtraInfo != null) {
                        doShareUpLoad(shareExtraInfo.bizId, shareExtraInfo.bizType, CircleShareChannel.Wechat);
                    }
                    break;
                case R.id.llyt_weixin_circle:
                    platform = SHARE_MEDIA.WEIXIN_CIRCLE;
                    shareBean.shareWay = ShareUtils.WEIXIN_CIRCLE;
                    if (shareExtraInfo != null) {
                        doShareUpLoad(shareExtraInfo.bizId, shareExtraInfo.bizType, CircleShareChannel.WeMoment);
                    }
                    break;
                case R.id.llyt_weibo:
                    platform = SHARE_MEDIA.SINA;
                    shareBean.shareWay = ShareUtils.WEIBO;
                    if (shareExtraInfo != null) {
                        doShareUpLoad(shareExtraInfo.bizId, shareExtraInfo.bizType, CircleShareChannel.WeiBo);
                    }
                    break;
                case R.id.llyt_qq:
                    platform = SHARE_MEDIA.QQ;
                    shareBean.shareWay = ShareUtils.QQ;
                    if (shareExtraInfo != null) {
                        doShareUpLoad(shareExtraInfo.bizId, shareExtraInfo.bizType, CircleShareChannel.QQ);
                    }
                    break;
                default:
                    break;
            }

            //无网
            if (!AndroidUtils.isNetWorkAvalible(activity)) {
                ToastUtil.showToast(activity, activity.getString(R.string.error_label_popup_wlyc));
                return;
            }

            if ((platform == SHARE_MEDIA.WEIXIN || platform == SHARE_MEDIA.WEIXIN_CIRCLE) && !UMShareAPI.get(activity).isInstall(activity, SHARE_MEDIA.WEIXIN)) {
                ToastUtil.showToast(activity, "请安装微信客户端");
                return;
            }

            if (platform == SHARE_MEDIA.QQ && !UMShareAPI.get(activity).isInstall(activity, SHARE_MEDIA.QQ)) {
                ToastUtil.showToast(activity, "请安装QQ客户端");
                return;
            }

            if (platform == SHARE_MEDIA.SINA && !UMShareAPI.get(activity).isInstall(activity, SHARE_MEDIA.SINA)) {
                ToastUtil.showToast(activity, "请安装微博客户端");
                return;
            }

            if (onlyImg) {
                if (StringUtil.isEmpty(imageUrl)) {
                    ToastUtil.showToast(activity, activity.getString(R.string.label_toast_share_steps_error));
                    return;
                }
                doShare(platform, activity, title, text, url, imageUrl, true);
            } else {
                doShare(platform, activity, title, text, url, imageUrl, false);
            }

        };

        // 设置监听
        // 基本分享
        view.findViewById(R.id.llyt_weixin).setOnClickListener(listener);
        view.findViewById(R.id.llyt_weixin_circle).setOnClickListener(listener);
        view.findViewById(R.id.llyt_weibo).setOnClickListener(listener);
        view.findViewById(R.id.llyt_qq).setOnClickListener(listener);
        if (base) {
            view.findViewById(R.id.llyt_base).setVisibility(View.VISIBLE);
        } else {
            view.findViewById(R.id.llyt_base).setVisibility(View.GONE);
        }
        // 短信  复制链接
        if (showSms || canCopy) {
            view.findViewById(R.id.llyt_extra).setVisibility(View.VISIBLE);
            if (showSms) {
                view.findViewById(R.id.llyt_duanxin).setVisibility(View.VISIBLE);
                view.findViewById(R.id.llyt_duanxin).setOnClickListener(listener);
            } else {
                view.findViewById(R.id.llyt_duanxin).setVisibility(View.GONE);
            }
            if (canCopy) {
                view.findViewById(R.id.llyt_copy).setVisibility(View.VISIBLE);
                view.findViewById(R.id.llyt_copy).setOnClickListener(listener);
            } else {
                view.findViewById(R.id.llyt_copy).setVisibility(View.GONE);
            }
            LinearLayout llytSpaceOne = view.findViewById(R.id.llyt_space_one);
            LinearLayout llytSpaceTwo = view.findViewById(R.id.llyt_space_two);
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) llytSpaceOne.getLayoutParams();

            if (!showSms || !canCopy) {
                lp.weight = 1.5f;
                llytSpaceOne.setLayoutParams(lp);
                llytSpaceTwo.setLayoutParams(lp);
            } else {
                lp.weight = 1;
                llytSpaceOne.setLayoutParams(lp);
                llytSpaceTwo.setLayoutParams(lp);
            }

        } else {
            view.findViewById(R.id.llyt_extra).setVisibility(View.GONE);
        }
        // 举报 屏蔽 取消关注  删除 赞
        int extraCircleNum = 0;
        if (reportListener != null) {
            view.findViewById(R.id.llyt_report).setVisibility(View.VISIBLE);
            view.findViewById(R.id.llyt_report).setOnClickListener(listener);
            extraCircleNum++;
        } else {
            view.findViewById(R.id.llyt_report).setVisibility(View.GONE);
        }
        if (shieldListener != null) {
            view.findViewById(R.id.llyt_shield).setVisibility(View.VISIBLE);
            view.findViewById(R.id.llyt_shield).setOnClickListener(listener);
            extraCircleNum++;
        } else {
            view.findViewById(R.id.llyt_shield).setVisibility(View.GONE);
        }
        if (cancleFollowListener != null) {
            view.findViewById(R.id.llyt_cancle_follow).setVisibility(View.VISIBLE);
            view.findViewById(R.id.llyt_cancle_follow).setOnClickListener(listener);
            extraCircleNum++;
        } else {
            view.findViewById(R.id.llyt_cancle_follow).setVisibility(View.GONE);
        }
        if (deleteListener != null) {
            view.findViewById(R.id.llyt_delete).setVisibility(View.VISIBLE);
            view.findViewById(R.id.llyt_delete).setOnClickListener(listener);
            extraCircleNum++;
        } else {
            view.findViewById(R.id.llyt_delete).setVisibility(View.GONE);
        }
        if (praiseListener != null) {
            view.findViewById(R.id.llyt_praise).setVisibility(View.VISIBLE);
            view.findViewById(R.id.llyt_praise).setOnClickListener(listener);
            ImageView ivPraise = view.findViewById(R.id.iv_share_praise);
            TextView tvPraise = view.findViewById(R.id.tv_share_praise);
            ivPraise.setSelected(ValueConstants.TYPE_AVAILABLE.equals(praiseState));
            tvPraise.setText(praiseNum > 0 ? "赞(" + praiseNum + ")" : "赞");
            extraCircleNum++;
        } else {
            view.findViewById(R.id.llyt_praise).setVisibility(View.GONE);
        }
        if (extraCircleNum > 0) {
            view.findViewById(R.id.llyt_extra_circle).setVisibility(View.VISIBLE);
            LinearLayout llytSpace = view.findViewById(R.id.llyt_extra_circle_placeholder);
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) llytSpace.getLayoutParams();
            lp.weight = 4 - extraCircleNum;
            llytSpace.setLayoutParams(lp);
        } else {
            view.findViewById(R.id.llyt_extra_circle).setVisibility(View.GONE);
        }

        view.findViewById(R.id.tv_share_cancel).setOnClickListener(listener);

        sShareDlg.setContentView(view);

        Window dialogWindow = sShareDlg.getWindow();
        dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialogWindow.setAttributes(lp);

        sShareDlg.show();
    }


    public static void doShare(SHARE_MEDIA platform, final Activity activity, final String title, String text, String url, String imageUrl) {
        doShare(platform, activity, title, text, url, imageUrl, false);
    }

    /**
     * @param platform 平台
     * @param activity Activity
     * @param title    标题
     * @param text     文本
     * @param url      small_icon
     * @param imageUrl imageUrl
     */
    public static void doShare(SHARE_MEDIA platform, final Activity activity, final String title, String text, String url, String imageUrl, boolean onlyImg) {

        // 分享
        ShareAction shareAction = new ShareAction(activity)
                .setPlatform(platform)
                .setCallback(new UMShareListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {
                        dismissShareBoard();
                    }

                    @Override
                    public void onResult(SHARE_MEDIA platform) {
                        ToastUtil.showToast(activity, activity.getString(R.string.ym_share_success));

                    }

                    @Override
                    public void onError(SHARE_MEDIA platform, Throwable t) {
                        ToastUtil.showToast(activity, activity.getString(R.string.ym_share_failed));

                    }

                    @Override
                    public void onCancel(SHARE_MEDIA platform) {
                        ToastUtil.showToast(activity, activity.getString(R.string.ym_share_cancel));
                    }
                });

        if (onlyImg) {
            shareAction.withMedia(new UMImage(activity, ImageUtils.getImageFullUrl(imageUrl)));

        } else if (platform == SHARE_MEDIA.SINA) { // 微博使用图片分享，拼url到text里
            String shareText = "";
            if (!TextUtils.isEmpty(title)) {
                shareText += title;
            }

            if (!TextUtils.isEmpty(text)) {
                shareText += text;
            }

            if (!TextUtils.isEmpty(url)) {
                shareText += url;
            }

            shareAction.withMedia(new UMImage(activity, CommonUtil.getImageFullUrl(imageUrl)))
                    .withText(shareText);
        } else {
            UMWeb web = new UMWeb(url);
            web.setTitle(title);//标题
            web.setThumb(new UMImage(activity, CommonUtil.getImageFullUrl(imageUrl)));  //缩略图
            web.setDescription(text);//描述
            shareAction.withMedia(web);
        }

        shareAction.share();
    }

    /**
     * 隐藏分享面板
     */
    public static void dismissShareBoard() {
        if (sShareDlg != null && sShareDlg.isShowing()) {
            sShareDlg.dismiss();
        }
    }

    /**
     * 短信分享
     *
     * @param mContext
     * @param smstext  短信分享内容
     * @return
     */
    private static void sendSms(Activity mContext, String smstext) {
        Uri smsToUri = Uri.parse("smsto:");
        Intent mIntent = new Intent(Intent.ACTION_SENDTO, smsToUri);
        mIntent.putExtra("sms_body", smstext);
        mContext.startActivity(mIntent);
    }

    /**
     * 文本复制功能
     *
     * @param mContext
     * @param content
     */
    public static void copyToClipboard(Context mContext, String content) {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content.trim());
        ToastUtil.showToast(mContext, R.string.copy_to_clipboard);
    }

    /**
     * 举报接口
     */
    public interface OnReportListener {
        void onReport();
    }

    /**
     * 屏蔽接口
     */
    public interface OnShieldListener {
        void onShield();
    }

    /**
     * 取消关注接口
     */
    public interface OnCancleFollowListener {
        void onCancleFollow();
    }

    /**
     * 删除接口
     */
    public interface OnDeleteListener {
        void onDelete();
    }

    /**
     * 点赞接口
     */
    public interface OnPraiseListener {
        void onPraise();
    }

    /**
     * 点击事件上传服务器（获取积分）
     */
    public static void doShareUpLoad(long bizId, String bizType, String channel) {
        YhyCallback<Response<ShareResp>> callback = new YhyCallback<Response<ShareResp>>() {
            @Override
            public void onSuccess(Response<ShareResp> data) {
            }

            @Override
            public void onFail(YhyCode code, Exception exception) {
            }
        };
        new ActivityCenterApi().share(new ShareReq(new ShareReq.Companion.P(bizId, bizType, channel)), callback).execAsync();
    }

    public static class ShareExtraInfo {
//        String title;
//        String content;
//        String url;
//        String img;

        public String bizType;
        public long bizId;
        public boolean needDoShare;
    }
}
