package com.quanyan.pedometer;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quanyan.base.BaseFragment;
import com.quanyan.base.util.StringUtil;
import com.quanyan.pedometer.newpedometer.StepService;
import com.quanyan.pedometer.utils.PreferencesUtils;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.DirConstants;
import com.quanyan.yhy.ui.ShareTableActivity;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.integralmall.integralmallcontroller.IntegralmallController;
import com.quanyan.yhy.util.QRCodeUtil;
import com.yhy.common.beans.net.model.ShareBean;
import com.yhy.common.beans.net.model.pedometer.InviteShareInfo;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.CommonUtil;
import com.yhy.common.utils.SPUtils;
import com.yhy.imageloader.ImageLoadManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created with Android Studio.
 * Title:InviteFriendsFragment
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:徐学东
 * Date:16/7/7
 * Time:下午8:25
 * Version 1.1.0
 */
public class InviteFriendsFragment extends BaseFragment{
    private IntegralmallController mController;

    private View mParentView;
    private LinearLayout mSubParentView;
    // logo
    private ImageView ivLogo;
    //头像
    private ImageView mAvatarView;
    //昵称
    private TextView mNickNameView;
    //标题
    private TextView mTitleView;
    //副标题
    private TextView mSummaryView;
    //累计赚钱数
    private TextView mMoneyView;
    //步数
    private TextView mStepsView;
    //天数
    private TextView mDaysView;
    //邀请按钮
    private TextView mInviteView;
    //关闭页面
    private ImageView mDeleteView;
    //分享方式
    private int mShareWay;
    //显示邀请好友的按钮
    private LinearLayout mLLButtonShowView;
    //显示二维码的按钮
    private LinearLayout mLLQrCodeShowView;
    //二维码显示
    private ImageView mQRCodeView;
    /**
     * 分享的内容
     */
    private InviteShareInfo mInviteShareInfo;
    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        setTitleBarBackground(Color.TRANSPARENT);
        mParentView = view;
        mController = new IntegralmallController(getActivity(), mHandler);
        getShareInfo();//获取分享的信息
        mShareWay = getArguments().getInt(SPUtils.EXTRA_SHARE_WAY);

        mSubParentView = (LinearLayout)  view.findViewById(R.id.ll_content);

        ivLogo = (ImageView) view.findViewById(R.id.iv_logo);
        mAvatarView = (ImageView)view.findViewById(R.id.iv_avatar);
        mNickNameView = (TextView) view.findViewById(R.id.tv_nick_name);
        mTitleView = (TextView)view.findViewById(R.id.tv_title);
        mSummaryView = (TextView)view.findViewById(R.id.tv_summary);
        mInviteView = (TextView)view.findViewById(R.id.tv_invite);
        mDeleteView = (ImageView)view.findViewById(R.id.iv_delete);

        mMoneyView = (TextView)view.findViewById(R.id.view_share_pedometer_money);
        mStepsView = (TextView)view.findViewById(R.id.view_share_pedometer_steps);
        mDaysView = (TextView)view.findViewById(R.id.view_share_pedometer_days);

        mLLButtonShowView = (LinearLayout) view.findViewById(R.id.ll_btn_show);
        mLLQrCodeShowView = (LinearLayout)view.findViewById(R.id.ll_share_pedometer_qr_code_layout);
        mQRCodeView = (ImageView) view.findViewById(R.id.iv_share_pedometer_qr_code);

        mInviteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doInvite();
            }
        });

        mDeleteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

    /**
     * 开始邀请
     */
    private void doInvite(){
        mLLButtonShowView.setVisibility(View.GONE);
        mLLQrCodeShowView.setVisibility(View.VISIBLE);
        mParentView.setVisibility(View.INVISIBLE);

        Message msg = new Message();
        msg.what = ShareFragment.MSG_CROP_VIEW;
        msg.arg1 = mShareWay;
        mHandler.sendMessageDelayed(msg, 10);
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(getActivity(), R.layout.view_pedometer_invite, null);
    }

    /**
     * 开始分享
     * @param index
     */
    private void handleShare(int index){
        String bitmap = getScreenShot();
        String shareContent = "";
        if(!StringUtil.isEmpty(mInviteShareInfo.shareContent)) {
            shareContent = mInviteShareInfo.shareContent;
        }
        String qrCodeUrl = mInviteShareInfo.inviteUrl;
        String weiboTopicName = PreferencesUtils.getString(getActivity(), StepService.PEDOMETER_SHARE_WEIBO_TOPIC_NAME, "");
        switch (index){
            case ShareTableActivity.WEIBO:
                ShareBean shareBean = new ShareBean();
                shareBean.shareImageLocal = bitmap;
                shareBean.shareWay = index;
                if(!StringUtil.isEmpty(shareContent)) {
                    shareBean.shareContent = weiboTopicName + shareContent + qrCodeUrl;
                }
                NavUtils.gotoShareTableActivity(getActivity(), shareBean, "");
                break;
            case ShareTableActivity.QQ:
                AndroidShareUtil.shareQQFriend(getActivity(), "", shareContent, AndroidShareUtil.IMAGE, bitmap);
                break;
            case ShareTableActivity.WEIXIN:
                AndroidShareUtil.shareWeChatFriend(getActivity(), "", shareContent, AndroidShareUtil.IMAGE, bitmap);
                break;
            case ShareTableActivity.WEIXIN_CIRCLE:
                AndroidShareUtil.shareWeChatFriendCircle(getActivity(), "", shareContent, AndroidShareUtil.IMAGE, bitmap);
                break;
        }
        getActivity().finish();
    }

    /**
     * 获取截图
     * @return
     */
    private String getScreenShot(){
        ivLogo.setVisibility(View.VISIBLE);

        mSubParentView.setDrawingCacheEnabled(true);
        mSubParentView.buildDrawingCache();
        Bitmap bitmap = mSubParentView.getDrawingCache();

        Bitmap temp = Bitmap.createBitmap(bitmap);
        mSubParentView.destroyDrawingCache();
        File file = new File(DirConstants.DIR_CACHE + "cache_QuanYan_invite_share_qr"+ ".jpg");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            Log.i("file", file.getAbsolutePath());
            temp.setHasAlpha(true);
            temp.prepareToDraw();
//            int width = temp.getWidth();
//            int height = temp.getHeight();
//            // 设置想要的大小
//            int newWidth = 480;
//            int newHeight = 800;
//            // 计算缩放比例
//            float scaleWidth = ((float) newWidth) / width;
//            float scaleHeight = ((float) newHeight) / height;
//            // 取得想要缩放的matrix参数
//            Matrix matrix = new Matrix();
//            matrix.postScale(scaleWidth, scaleHeight);
//            // 得到新的图片
//            Bitmap result = Bitmap.createBitmap(temp, 0, 0, width, height, matrix, true);
////            Bitmap result = Bitmap.createBitmap(temp, 0, 0, width, height);
//            result.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            temp.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }

    private void getShareInfo(){
        mController.doGetInviteShareInfo(getActivity());
    }

    @Override
    public void handleMessage(Message msg) {
        hideLoadingView();
        hideErrorView(null);
        switch (msg.what) {
            case ValueConstants.MSG_HOTEL_HOME_BANNER_OK:
                mInviteShareInfo = (InviteShareInfo) msg.obj;
                if(mInviteShareInfo != null) {
                    mMoneyView.setText(String.valueOf(mInviteShareInfo.totalPoint));
                    mStepsView.setText(String.valueOf(mInviteShareInfo.totalStep));
                    mDaysView.setText(String.valueOf(mInviteShareInfo.collectCount));

                    mTitleView.setText(mInviteShareInfo.mainCopy);
                    mSummaryView.setText(mInviteShareInfo.subCopy);

                    mInviteView.setText(String.format(getActivity().getString(R.string.label_btn_invite_friends) ,mInviteShareInfo.inviteCopy));
                }

                if (!StringUtil.isEmpty(SPUtils.getUserIcon(getActivity()))) {
                    ImageLoadManager.loadCircleImage(CommonUtil.getImageFullUrl(SPUtils.getUserIcon(getActivity())), R.mipmap.icon_default_avatar, (int) getResources().getDimension(R.dimen.margin_80dp), (int) getResources().getDimension(R.dimen.margin_80dp), mAvatarView);
                } else {
                    mAvatarView.setImageResource(R.mipmap.icon_default_avatar);
                }

                mNickNameView.setText(SPUtils.getNickName(getActivity()));

                if(mInviteShareInfo != null) {
                    createQRCode(mInviteShareInfo.inviteUrl);
                }
                break;
            case ShareFragment.MSG_CROP_VIEW:
                handleShare(msg.arg1);
                break;
            case ValueConstants.MSG_HOTEL_HOME_BANNER_KO:
                ToastUtil.showToast(getActivity(),StringUtil.handlerErrorCode(getActivity(),msg.arg1));
                getActivity().finish();
                break;
        }
    }

    /**
     * 生成二维码
     * @param url
     */
    private void createQRCode(String url){
        Bitmap bitmap = QRCodeUtil.GenorateImage(url);
        if(bitmap != null) {
           mQRCodeView.setImageBitmap(bitmap);
        }
    }
}
