package com.quanyan.yhy.ui.personal;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.harwkin.nb.camera.ImageUtils;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.util.StringUtil;
import com.quanyan.base.view.BaseNavView;
import com.quanyan.pedometer.ShareActivity;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.AnalyDataValue;
import com.quanyan.yhy.common.DirConstants;
import com.quanyan.yhy.common.Gendar;
import com.quanyan.yhy.libanalysis.AnEvent;
import com.quanyan.yhy.libanalysis.Analysis;
import com.quanyan.yhy.ui.base.utils.NativeUtils;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.base.utils.TCEventHelper;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.zxing.view.FunctionPop;
import com.quanyan.yhy.util.QRCodeUtil;
import com.yhy.common.beans.net.model.user.NativeDataBean;
import com.yhy.common.beans.user.User;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.CommonUtil;
import com.yhy.common.utils.LogUtils;
import com.yhy.imageloader.ImageLoadManager;
import com.yhy.service.IUserService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static com.umeng.commonsdk.stateless.UMSLEnvelopeBuild.mContext;

public class MineQrActivity extends BaseActivity {
    private BaseNavView mBaseNavView;
    private ImageView mUserHead;
    private TextView mUserName;
    private ImageView mSex;
    private User mUserInfo;
    private ImageView mMineQr;
    private FunctionPop mFunctionPop;
    private Bitmap mQrAddImg;
    private Bitmap mNewQrImage;
    private boolean showShare;

    @Autowired
    IUserService userService;

    public static void gotoMineQrActivity(Context context, boolean showShare) {
        Intent intent = new Intent(context, MineQrActivity.class);
        intent.putExtra("showShare", showShare);
        context.startActivity(intent);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        findId();

        mFunctionPop = new FunctionPop(this);
        mUserInfo = userService.getUserInfo(userService.getLoginUserId());
        if (mUserInfo == null){
            return;
        }
        setData();

    }

    private void initClick() {
        mFunctionPop.setOnBtnClickListener(new FunctionPop.OnBtnClickListener() {
            @Override
            public void onSaveListener() {
                //统计事件
                Analysis.pushEvent(mContext, AnEvent.QR_CODE_MY_SAVE_THE_IMAGE);
                //保存图片
                TCEventHelper.onEvent(MineQrActivity.this, AnalyDataValue.MINEQRPAGE_SAVE_IMAGE_CLICK);
                savePic();
            }

            @Override
            public void onShareListener() {
                //统计事件
                Analysis.pushEvent(mContext, AnEvent.QR_CODE_MY_SHARE);
                //分享
                NavUtils.gotoShareActivity(MineQrActivity.this, ShareActivity.SHARE_TYPE_MINE_INFO, -1, mUserInfo);
            }
        });
    }

    private void savePic() {
        if (mNewQrImage != null) {
            String filename = "yhy_mine_qrcode_" + System.currentTimeMillis() + ".jpg";
            String qrCodePath = DirConstants.DIR_PIC_SHARE + filename;
            if (QRCodeUtil.saveImg(this, mNewQrImage, qrCodePath)) {
                // 其次把文件插入到系统图库
                try {
                    MediaStore.Images.Media.insertImage(getContentResolver(),
                            qrCodePath, filename, null);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                //放到相册中
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri uri = Uri.fromFile(new File(qrCodePath));
                intent.setData(uri);
                this.sendBroadcast(intent);
                //保存成功提示
//                ToastUtil.showToast(this, getString(R.string.mine_qr_save_desc) + qrCodePath);
                ToastUtil.showToast(this, "图片已保存至相册");
            }
        }
    }

    private void setData() {
        mBaseNavView.setRightTextAndImgClick(v -> mFunctionPop.showPopupWindow(mMineQr));
        initClick();
        mUserName.setText(mUserInfo.getNickname());
        if (!StringUtil.isEmpty(mUserInfo.getAvatar())) {
            ImageLoadManager.loadCircleImage(CommonUtil.getImageFullUrl(mUserInfo.getAvatar()),
                    R.mipmap.icon_default_avatar,
                    (int) getResources().getDimension(R.dimen.margin_80dp),
                    (int) getResources().getDimension(R.dimen.margin_80dp),
                    mUserHead);
        } else {
            mUserHead.setImageResource(R.mipmap.icon_default_avatar);
        }

        if (!StringUtil.isEmpty(mUserInfo.getGender())) {
            Gendar.setGendarIcon(mSex, mUserInfo.getGender());
        }

        //生成二维码
        genorateQr();

        showShare = getIntent().getBooleanExtra("showShare", false);
        if (showShare){
            NavUtils.gotoShareActivity(this, ShareActivity.SHARE_TYPE_MINE_INFO, -1, mUserInfo);

        }
    }

    private void genorateQr() {
        NativeDataBean nativeDataBean = new NativeDataBean();
        nativeDataBean.setId(String.valueOf(userService.getLoginUserId()));
        nativeDataBean.setOption(String.valueOf(mUserInfo.getOptions()));
        String userPageUrl = QRCodeUtil.getUserPageUrl(this, NativeUtils.MASTER_DETAIL, nativeDataBean);
        LogUtils.d("Harwkin", "url======" + userPageUrl);
        mQrAddImg = QRCodeUtil.createQrAddImg(this, userPageUrl);
        if (mQrAddImg != null) {

            File file = new File(DirConstants.DIR_PIC_ORIGIN + DirConstants.USER_HEAD_ICON);
            if (file.exists()) {
                try {
                    FileInputStream fis = new FileInputStream(file);
                    Bitmap logoBitmap = BitmapFactory.decodeStream(fis);
                    if (logoBitmap != null) {
                        Bitmap roundedCornerBitmap = ImageUtils.getRoundedCircleBitmap(logoBitmap, dip2Px(ValueConstants.MAX_SELECT_DEST_CITYS), R.color.white, this);
                        if (roundedCornerBitmap != null) {
                            mNewQrImage = QRCodeUtil.addLogo(mQrAddImg, roundedCornerBitmap);
                            if (mNewQrImage != null) {
                                mMineQr.setImageBitmap(mNewQrImage);
                            }
                        }
                    }

                } catch (FileNotFoundException e) {
                    ToastUtil.showToast(this, "read file fail");
                    e.printStackTrace();
                }
            } else {
                Bitmap logoBm = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_default_avatar);
                mNewQrImage = QRCodeUtil.addLogo(mQrAddImg, logoBm);
                if (mNewQrImage != null) {
                    mMineQr.setImageBitmap(mNewQrImage);
                } else {
                    ToastUtil.showToast(this, "mNewQrImage not exist");
                }
            }
        } else {
            ToastUtil.showToast(this, "mQrAddImg not exist");
        }
    }


    private void findId() {
        mUserHead = (ImageView) findViewById(R.id.iv_master_userhead);
        mUserName = (TextView) findViewById(R.id.tv_master_username);
        mSex = (ImageView) findViewById(R.id.iv_master_sex);
        mMineQr = (ImageView) findViewById(R.id.iv_mine_qr);
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.ac_mine_qr, null);
    }

    @Override
    public View onLoadNavView() {
        mBaseNavView = new BaseNavView(this);
        mBaseNavView.setTitleText(R.string.mine_capture_title);
        //mBaseNavView.setRightImg(R.mipmap.my_qr_more);
        mBaseNavView.setRightTextAndImg(R.string.contact_title_qr_right, R.mipmap.my_qr_more);
        return mBaseNavView;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }

    private int dip2Px(float dip) {
        return (int) (dip * getResources().getDisplayMetrics().density + 0.5f);
    }
}
