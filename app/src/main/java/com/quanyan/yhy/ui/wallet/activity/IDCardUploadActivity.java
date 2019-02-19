package com.quanyan.yhy.ui.wallet.activity;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.harwkin.nb.camera.CropBuilder;
import com.harwkin.nb.camera.callback.CameraImageListener;
import com.harwkin.nb.camera.options.CameraOptions;
import com.harwkin.nb.camera.pop.CameraPopListener;
import com.harwkin.nb.camera.type.OpenType;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.baseenum.IActionTitleBar;
import com.quanyan.base.util.LocalUtils;
import com.quanyan.base.view.BaseNavView;
import com.quanyan.base.yminterface.ErrorViewClick;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.ErrorCode;
import com.quanyan.yhy.common.UpdateIDCardState;
import com.quanyan.yhy.net.NetManager;
import com.quanyan.yhy.net.lsn.OnResponseListener;
import com.quanyan.yhy.ui.base.utils.DialogUtil;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.wallet.Controller.WalletController;
import com.quanyan.yhy.ui.wallet.view.IDCardOptionDailog;
import com.quanyan.yhy.ui.wallet.view.WalletDialog;
import com.yhy.common.beans.net.model.paycore.BaseResult;
import com.yhy.common.beans.net.model.paycore.SubmitIdCardPhotoResult;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class IDCardUploadActivity extends BaseActivity implements View.OnClickListener, CameraPopListener,
        CameraImageListener {

    private BaseNavView mBaseNavView;

    @ViewInject(R.id.iv_idcard_position)
    private ImageView ivCardPosition;

    @ViewInject(R.id.tv_account_name)
    private TextView tvUserAccount;


    @ViewInject(R.id.iv_idcard_negative)
    private ImageView ivCardNegative;

    @ViewInject(R.id.tv_idcard_negative)
    private TextView tvCardNegative;

    @ViewInject(R.id.tv_idcard_position)
    private TextView tvCardPosition;

    @ViewInject(R.id.ll_idcard_upload_postive)
    private LinearLayout llvUploadPositive;

    @ViewInject(R.id.ll_idcard_upload_negative)
    private LinearLayout llUploadNegative;

    @ViewInject(R.id.btn_idcardupload_confirm_confirm)
    private Button btnUploadConfirm;

    IDCardOptionDailog mIDCardOptionDailog;
    String photoType = "FRONT";
    WalletController mController;
    boolean isUoloadIdCardPos;
    boolean isUoloadIdCardNeg;
    private Dialog mNoticeDialog;
    String frontPhotoName = "";
    String reversePhotoName = "";

    @Override
    protected void initView(Bundle savedInstanceState) {
        ViewUtils.inject(this);
        EventBus.getDefault().register(this);


        llvUploadPositive.setOnClickListener(this);
        llUploadNegative.setOnClickListener(this);
        btnUploadConfirm.setOnClickListener(this);
        mIDCardOptionDailog = new IDCardOptionDailog(this, this, this);
        mController = new WalletController(this, mHandler);
        btnUploadConfirm.setSelected(false);
        btnUploadConfirm.setClickable(false);


        String userName = this.getIntent().getStringExtra(SPUtils.KEY_USER_NAME);
        if (userName != null && !"".equals(userName)) {
            String pre = "**";
            if (userName.length() == 2) {
                pre = "*";
            } else {
                pre = "**";
            }
            userName = "请上传" + pre + userName.substring(userName.length() - 1, userName.length()) + "有效二代身份证";
            tvUserAccount.setText(userName);
        }


        mNoticeDialog = DialogUtil.showMessageDialog(this,
                "提示",
                getString(R.string.dlg_msg_idacard),
                getString(R.string.dlg_btn_label_confirm),
                getString(R.string.dlg_btn_label_idacard_cancel), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mNoticeDialog.dismiss();
                        IDCardUploadActivity.this.finish();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mNoticeDialog.dismiss();
                    }
                });
        mNoticeDialog.setCanceledOnTouchOutside(true);
    }

    public void onEventMainThread(UpdateIDCardState result) {
        llvUploadPositive.setClickable(true);
        ivCardPosition.setImageResource(R.mipmap.btn_idcard_upload_postive);
        llUploadNegative.setClickable(true);
        ivCardNegative.setImageResource(R.mipmap.btn_idcard_upload_negative);
        btnUploadConfirm.setSelected(false);
        btnUploadConfirm.setClickable(false);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            mNoticeDialog.show();
        }
        return false;
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.activity_idcard_upload, null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        int what = msg.what;
        hideLoadingView();
        if (what == ValueConstants.PAY_VERIFY_IDCARDPHOTO_ERROR) {
            ToastUtil.showToast(IDCardUploadActivity.this, (String) msg.obj);
        } else if (what == ValueConstants.PAY_VERIFY_IDCARDPHOTO_SUCCESS) {
            BaseResult mBaseResult = (BaseResult) msg.obj;
            if (!mBaseResult.success) {
                WalletDialog.showAuthFailedDialog(IDCardUploadActivity.this, "", new WalletDialog.ToolsInterface() {
                    @Override
                    public void OnShowDialog() {
                        getPicDialog();

                    }
                });
            } else {
                if (photoType.equals("FRONT")) {
                    isUoloadIdCardPos = true;
                    llvUploadPositive.setClickable(false);
                    tvCardPosition.setText("上传成功");
                    ivCardPosition.setImageResource(R.mipmap.icon_idcard_pos_success);
                } else {
                    isUoloadIdCardNeg = true;
                    llUploadNegative.setClickable(false);
                    tvCardNegative.setText("上传成功");
                    ivCardNegative.setImageResource(R.mipmap.icon_idcard_neg_success);
                }
                if (isUoloadIdCardPos && isUoloadIdCardNeg) {
                    btnUploadConfirm.setSelected(true);
                    btnUploadConfirm.setClickable(true);
                }
            }
        } else if (what == ValueConstants.PAY_SUBMIT_IDCARDPHOTO_SUCCESS) {
            SubmitIdCardPhotoResult mBaseResult = (SubmitIdCardPhotoResult) msg.obj;
            if (mBaseResult.success) {
                NavUtils.gotoIDAuthenticActivity(IDCardUploadActivity.this, mBaseResult.success, mBaseResult.userName, mBaseResult.idNo, mBaseResult.idCardValidDate);
                finish();
            } else {
                NavUtils.gotoIDAuthenticActivity(IDCardUploadActivity.this, mBaseResult.success);
            }

        } else if (what == ValueConstants.PAY_SUBMIT_IDCARDPHOTO_ERROR) {
            ToastUtil.showToast(IDCardUploadActivity.this, (String) msg.obj);
        }

    }

    private void showNetErrorView(int arg1) {
        showErrorView(null, ErrorCode.NETWORK_UNAVAILABLE == arg1 ?
                IActionTitleBar.ErrorType.NETUNAVAILABLE : IActionTitleBar.ErrorType.ERRORNET, "", "", "", new ErrorViewClick() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    @Override
    public View onLoadNavView() {
        mBaseNavView = new BaseNavView(this);
        mBaseNavView.setTitleText(getString(R.string.title_identity_auth));
        mBaseNavView.setLeftClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNoticeDialog.show();
            }
        });
        return mBaseNavView;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_idcard_upload_postive:
                photoType = "FRONT";
                getPicDialog();
                break;
            case R.id.ll_idcard_upload_negative:
                getPicDialog();
                photoType = "REVERSE";
                break;
            case R.id.btn_idcardupload_confirm_confirm:
              doSubmitIdCardPhoto();
          /*      NavUtils.gotoIDAuthenticActivity(IDCardUploadActivity.this,true);
              ;*/
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mIDCardOptionDailog != null) {
            mIDCardOptionDailog.forResult(requestCode, resultCode, data);
        }
    }

    public void doSubmitIdCardPhoto() {
        showLoadingView(getString(R.string.toast_authen_image));
        mController.doSubmitIdCardPhoto(frontPhotoName, reversePhotoName, IDCardUploadActivity.this);

    }

    public void getPicDialog() {
        mIDCardOptionDailog.showOptionDialog();

    }

    @Override
    public void onSelectedAsy(String imgPath) {


        UpImagefromCut(imgPath);
    }


    @Override
    public void onCamreaClick(CameraOptions options) {
        if (LocalUtils.isAlertMaxStorage()) {
            ToastUtil.showToast(this, getString(R.string.label_toast_sdcard_unavailable));
            return;
        }
        options.setOpenType(OpenType.OPEN_CAMERA).setCropBuilder(new CropBuilder(1, 1, 960, 480));

        managerProcess();
    }

    /**
     * 执行照片处理
     */
    private void managerProcess() {
        mIDCardOptionDailog.process();

    }

    @Override
    public void onPickClick(CameraOptions options) {

        options.setOpenType(OpenType.OPEN_GALLERY)
                .setCropBuilder(new CropBuilder(1, 1, 960, 480));

        managerProcess();
    }

    protected void UpImagefromCut(String data) {
        if (data == null) {
            return;
        }
        if (data != null && data.length() > 0) {
            UploadImage(data);
        }
    }

    /**
     * 上传头像
     *
     * @param file
     */
    protected void UploadImage(final String file) {
        List<String> files = new ArrayList<String>();
        files.add(file);
        showLoadingView(getString(R.string.toast_uploading_image));
        NetManager.getInstance(this).doUploadImages(files,
                new OnResponseListener<List<String>>() {

                    @Override
                    public void onInternError(int errorCode, String errorMessage) {
                        hideLoadingView();
                        ToastUtil.showToast(IDCardUploadActivity.this,  errorMessage);
                    }

                    @Override
                    public void onComplete(boolean isOK, List<String> result, int errorCode, String errorMsg) {
                        if (isOK) {
                            if (result != null && result.size() > 0) {
                                mController.doVerifyIdCardPhoto(result.get(0), photoType, IDCardUploadActivity.this);
                                if (photoType.equals("FRONT")) {
                                    frontPhotoName = result.get(0);
                                } else {
                                    reversePhotoName = result.get(0);
                                }
                            }
                        }else{
                            hideLoadingView();
                            ToastUtil.showToast(IDCardUploadActivity.this,   errorMsg);
                        }
                    }
                });
    }

    @Override
    public void onDelClick() {

    }

    @Override
    public void onVideoClick() {

    }

    /*@Override
    public void onVideoDraftClick() {

    }*/
}
