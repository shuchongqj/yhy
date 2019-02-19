package com.quanyan.pedometer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.harwkin.nb.camera.ImageUtils;
import com.quanyan.base.BaseFragment;
import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.AnalyDataValue;
import com.quanyan.yhy.common.DirConstants;
import com.quanyan.yhy.common.Gendar;
import com.quanyan.yhy.ui.ShareTableActivity;
import com.quanyan.yhy.ui.base.utils.NativeUtils;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.base.utils.TCEventHelper;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.util.QRCodeUtil;
import com.yhy.common.beans.net.model.ShareBean;
import com.yhy.common.beans.net.model.user.NativeDataBean;
import com.yhy.common.beans.net.model.user.UserInfo;
import com.yhy.common.beans.user.User;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.CommonUtil;
import com.yhy.common.utils.LogUtils;
import com.yhy.common.utils.SPUtils;
import com.yhy.imageloader.ImageLoadManager;
import com.yhy.service.IUserService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created with Android Studio.
 * Title:ProductShareFragment
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:赵晓坡
 * Date:16/8/29
 * Time:09:46
 * Version 1.1.0
 */
public class UserInfoShareFragment extends BaseFragment {

    public final static int MSG_CROP_VIEW = 0x2222;

    private User mUserInfo;
    private int mShareType;
    private Bitmap mQrAddImg;
    private Bitmap mNewQrImage;

    private ImageView mUserHead;
    private TextView mUserName;
    private ImageView mSex;

    private LinearLayout mQuanYanCircleView;
    private LinearLayout mWeiXinView;
    private LinearLayout mWeiXinCircleView;
    private LinearLayout mQQCiew;
    private LinearLayout mWeiBoView;

    private View mParentView;
    private LinearLayout mSubParentView;

    @Autowired
    IUserService userService;

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        setTitleBarBackground(Color.TRANSPARENT);
        mParentView = view;

        Bundle bundle = getArguments();
        if (bundle != null) {
            mShareType = bundle.getInt(SPUtils.EXTRA_TYPE, -1);
            mUserInfo = (User) bundle.getSerializable(SPUtils.EXTRA_USER_INFO);
        }


        mUserHead = (ImageView) view.findViewById(R.id.iv_master_userhead);
        mUserName = (TextView) view.findViewById(R.id.tv_master_username);
        mSex = (ImageView) view.findViewById(R.id.iv_master_sex);

        initData();


        mQuanYanCircleView = (LinearLayout) view.findViewById(R.id.view_share_pedometer_cicles_layout);
        mWeiXinView = (LinearLayout) view.findViewById(R.id.view_share_pedometer_wechat_layout);
        mWeiXinCircleView = (LinearLayout) view.findViewById(R.id.view_share_pedometer_wechat_circles_layut);
        mQQCiew = (LinearLayout) view.findViewById(R.id.view_share_pedometer_qq_layout);
        mWeiBoView = (LinearLayout) view.findViewById(R.id.view_share_pedometer_weibo_layout);

        mSubParentView = (LinearLayout)  view.findViewById(R.id.view_pedometer_share_container);

//        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams)mSubParentView.getLayoutParams();
//        rlp.leftMargin = ScreenUtil.getScreenWidth(getActivity())/10;
//        rlp.rightMargin = ScreenUtil.getScreenWidth(getActivity())/10;
//        rlp.topMargin = ScreenUtil.getScreenHeight(getActivity())/8;
//        rlp.bottomMargin = ScreenUtil.getScreenHeight(getActivity())/8;
//        mSubParentView.setLayoutParams(rlp);

        initOthers();

        view.findViewById(R.id.view_pedometer_share_parent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

    private void initData() {
        mUserName.setText(SPUtils.getNickName(getActivity()));
        if (!StringUtil.isEmpty(SPUtils.getUserIcon(getActivity()))) {
//            BaseImgView.loadimg(mUserHead,
//                    SPUtils.getUserIcon(getActivity()),
//                    R.mipmap.icon_default_avatar,
//                    R.mipmap.icon_default_avatar,
//                    R.mipmap.icon_default_avatar,
//                    ImageScaleType.EXACTLY,
//                    (int) getResources().getDimension(R.dimen.margin_80dp),
//                    (int) getResources().getDimension(R.dimen.margin_80dp),
//                    180);
            ImageLoadManager.loadCircleImage(CommonUtil.getImageFullUrl(SPUtils.getUserIcon(getActivity())), R.mipmap.icon_default_avatar, (int) getResources().getDimension(R.dimen.margin_80dp), (int) getResources().getDimension(R.dimen.margin_80dp), mUserHead);

        } else {
            mUserHead.setImageResource(R.mipmap.icon_default_avatar);
        }

        if(!StringUtil.isEmpty(mUserInfo.getGender())){
            Gendar.setGendarIcon(mSex, mUserInfo.getGender());
        }
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(getActivity(), R.layout.fr_share_mine_info, null);
    }

    /**
     * 初始化
     */
    private void initOthers() {
        //二维码
        genorateQr();

        mQuanYanCircleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 16/6/29 分享健康圈
                TCEventHelper.onEvent(getActivity(), AnalyDataValue.MINEQRPAGE_SHARE_TYPE, "QUANYAN");
                cropView(ShareTableActivity.QUANYAN);

            }
        });
        mWeiXinView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 16/6/29 分享微信
                TCEventHelper.onEvent(getActivity(), AnalyDataValue.MINEQRPAGE_SHARE_TYPE, "WEIXIN");
                cropView(ShareTableActivity.WEIXIN);
            }
        });
        mWeiXinCircleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 16/6/29 分享微信朋友圈
                TCEventHelper.onEvent(getActivity(), AnalyDataValue.MINEQRPAGE_SHARE_TYPE, "WEIXIN_CICRLE");
                cropView(ShareTableActivity.WEIXIN_CIRCLE);
            }
        });
        mQQCiew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 16/6/29 分享QQ
                TCEventHelper.onEvent(getActivity(), AnalyDataValue.MINEQRPAGE_SHARE_TYPE, "QQ");
                cropView(ShareTableActivity.QQ);
            }
        });
        mWeiBoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 16/6/29 分享微博
                TCEventHelper.onEvent(getActivity(), AnalyDataValue.MINEQRPAGE_SHARE_TYPE, "WEIBO");
                cropView(ShareTableActivity.WEIBO);
            }
        });
    }

    /**
     * 开始分享
     * @param index
     */
    private void handleShare(int index){
        //SPUtils.setShareOK(getActivity(),true);

        String bitmap = getScreenShot();
        if(StringUtil.isEmpty(bitmap)){
            ToastUtil.showToast(getActivity(),getString(R.string.label_toast_share_steps_error));
            return ;
        }
        String shareContent = "";
        String weiboTopicName = "";
        String topicName = "";
        String shareText = "";
        switch (index){
            case ShareTableActivity.WEIBO:
            {
                ShareBean shareBean = new ShareBean();
                shareBean.shareImageLocal = bitmap;
                shareBean.shareWay = index;
                shareBean.isNeedSyncToDynamic = false;
                NavUtils.gotoShareTableActivity(getActivity(), shareBean, "");
            }
            break;
            case ShareTableActivity.QQ:
                AndroidShareUtil.shareQQFriend(getActivity(), "", StringUtil.isEmpty(shareContent) ? "" : (shareContent), AndroidShareUtil.IMAGE, bitmap);
                break;
            case ShareTableActivity.WEIXIN:
                AndroidShareUtil.shareWeChatFriend(getActivity(), "", StringUtil.isEmpty(shareContent) ? "" : (shareContent), AndroidShareUtil.IMAGE, bitmap);
                break;
            case ShareTableActivity.WEIXIN_CIRCLE:
                AndroidShareUtil.shareWeChatFriendCircle(getActivity(), "", StringUtil.isEmpty(shareContent) ? "" : (shareContent), AndroidShareUtil.IMAGE, bitmap);
                break;
            case ShareTableActivity.QUANYAN:
            {
                ShareBean shareBean = new ShareBean();
                shareBean.shareImageLocal = bitmap;
                shareBean.shareWay = index;
                if(!StringUtil.isEmpty(shareContent)) {
                    shareBean.shareContent = (StringUtil.isEmpty(topicName)?"" : topicName)+ shareContent;
                }
                NavUtils.gotoShareTableActivity(getActivity(), shareBean, "");
            }
            break;
        }
        getActivity().finish();
    }

    /**
     * 开始截图
     * @return
     */
    private void cropView(int index) {
        mParentView.findViewById(R.id.rl_share_text).setVisibility(View.GONE);
        mParentView.findViewById(R.id.view_share_pedometer_share_layout).setVisibility(View.GONE);
        mParentView.findViewById(R.id.iv_bottom).setVisibility(View.VISIBLE);
        mParentView.setVisibility(View.INVISIBLE);

        Message msg = new Message();
        msg.what = MSG_CROP_VIEW;
        msg.arg1 = index;
        mHandler.sendMessageDelayed(msg, 10);
    }

    /**
     * 获取截图
     * @return
     */
    private String getScreenShot(){
        mSubParentView.setDrawingCacheEnabled(true);
        mSubParentView.buildDrawingCache();
        Bitmap bitmap = mSubParentView.getDrawingCache();
        if(bitmap == null){
            return null;
        }
        Bitmap temp = Bitmap.createBitmap(bitmap);
        mSubParentView.destroyDrawingCache();
        File file = new File(DirConstants.DIR_CACHE + "cache_QuanYan_mine_share_" + System.currentTimeMillis() + ".jpg");
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
//            Bitmap result = Bitmap.createBitmap(temp, 0, 0, width, height, matrix,true);
//            result.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            temp.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        hideLoadingView();
        hideErrorView(null);
        switch (msg.what) {
            case MSG_CROP_VIEW:
                handleShare(msg.arg1);
                break;
        }
    }

    private void genorateQr() {
        NativeDataBean nativeDataBean = new NativeDataBean();
        nativeDataBean.setId(String.valueOf(userService.getLoginUserId()));
        nativeDataBean.setOption(String.valueOf(mUserInfo.getOptions()));
        String userPageUrl = QRCodeUtil.getUserPageUrl(getActivity(), NativeUtils.MASTER_DETAIL, nativeDataBean);
        LogUtils.d("Harwkin", "url======" + userPageUrl);
        mQrAddImg = QRCodeUtil.createQrAddImg(getActivity(), userPageUrl);
        if (mQrAddImg != null) {
            File file = new File(DirConstants.DIR_PIC_ORIGIN + DirConstants.USER_HEAD_ICON);
            if(file.exists()){
                try {
                    FileInputStream fis = new FileInputStream(file);
                    Bitmap logoBitmap  = BitmapFactory.decodeStream(fis);
                    if(logoBitmap != null){
                        Bitmap roundedCornerBitmap = ImageUtils.getRoundedCircleBitmap(logoBitmap, ValueConstants.MAX_SELECT_DEST_CITYS, R.color.white, getActivity());
                        if(roundedCornerBitmap != null){
                            mNewQrImage = QRCodeUtil.addLogo(mQrAddImg, roundedCornerBitmap);
                            if(mNewQrImage != null){
                                ((ImageView) mParentView.findViewById(R.id.iv_mine_qr)).setImageBitmap(mNewQrImage);
                            }
                        }
                    }

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }else {
                Bitmap logoBm = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_default_avatar);
                mNewQrImage = QRCodeUtil.addLogo(mQrAddImg, logoBm);
                if(mNewQrImage != null){
                    ((ImageView) mParentView.findViewById(R.id.iv_mine_qr)).setImageBitmap(mNewQrImage);
                }
            }


        }
    }
}
