package com.quanyan.yhy.ui.common.tourist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.util.RegexUtil;
import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.IdentityType;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.order.OrderTopView;
import com.yhy.common.beans.net.model.common.person.UserContact;
import com.yhy.common.beans.net.model.user.Certificate;
import com.yhy.common.beans.net.model.user.HkMacaoCertificate;
import com.yhy.common.beans.net.model.user.IDCertificate;
import com.yhy.common.beans.net.model.user.MilitaryCertificate;
import com.yhy.common.beans.net.model.user.PassportCertificate;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.AndroidUtils;
import com.yhy.common.utils.SPUtils;

/**
 * Created with Android Studio.
 * Title:AddOrUpdateCodeActivity
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-6-6
 * Time:16:36
 * Version 1.0
 * Description:新增或者修改证件号
 */
public class AddOrUpdateCodeActivity extends BaseActivity {

    private OrderTopView mOrderTopView;
    private Certificate mCertificate;
    @ViewInject(R.id.tv_code_title)
    private TextView mCodeTitle;//描述信息

    @ViewInject(R.id.et_code_value)
    private EditText mCodeValue;//证件值
    private String mCodeType;

    private int PASSPORT_MAX = 9;
    private int SOLDIERCARD_MAX = 20;
    private int IDCARD_MAX = 18;
    private UserContact mUserContact;
    private int mReqCode;
    private IDCertificate mIdCertificate;
    private PassportCertificate mPassportCertificate;
    private MilitaryCertificate mMilitaryCertificate;
    private HkMacaoCertificate mHkMacaoCertificate;
    private TouristController mController;

    public static void gotoAddOrUpdateCodeActivity(Activity context, int reqCode, Certificate certificate, UserContact userContact) {
        Intent intent = new Intent(context, AddOrUpdateCodeActivity.class);
        intent.putExtra(SPUtils.EXTRA_DATA, certificate);
        intent.putExtra(SPUtils.EXTRA_TAG_NAME, userContact);
        intent.putExtra(SPUtils.EXTRA_CODE, reqCode);
        context.startActivityForResult(intent, reqCode);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ViewUtils.inject(this);
        mController = new TouristController(this, mHandler);
        mCertificate = (Certificate) getIntent().getSerializableExtra(SPUtils.EXTRA_DATA);
        mUserContact = (UserContact) getIntent().getSerializableExtra(SPUtils.EXTRA_TAG_NAME);
        mReqCode = getIntent().getIntExtra(SPUtils.EXTRA_CODE, -1);
        mOrderTopView.setRightViewVisible(getString(R.string.save));
        if (mUserContact == null) {
            mOrderTopView.setOrderTopTitle(getString(R.string.label_btn_add_code));
        } else {
            mOrderTopView.setOrderTopTitle(getString(R.string.label_btn_edit_code));
        }
        initClick();
        //设置证件类型
        setHeaderTitle();

    }

    private void setEditHint() {
        mCodeValue.setHint(String.format(getString(R.string.hint_tour_code), IdentityType.showIdType(this, mCodeType)));
    }

    private void setHeaderTitle() {
        if (mCertificate != null) {
            mCodeType = mCertificate.type;
            if (!StringUtil.isEmpty(mCodeType)) {
                mCodeTitle.setText(IdentityType.showIdType(this, mCodeType));
                //设置hint字段
                setEditHint();
                //设置输入框大小
                limitSet(Integer.parseInt(mCodeType) - 1);

                //编辑时用到
                if (!StringUtil.isEmpty(mCertificate.cardNO)) {
                    mCodeValue.setText(mCertificate.cardNO);
                }
            }

        }
    }

    private void limitSet(int code) {
        switch (code) {
            case IdentityType.IDCARD_INT://身份证
                mCodeValue.setText("");
                mCodeValue.setFilters(new InputFilter[]{StringUtil.limitInputFilter(IDCARD_MAX), StringUtil.getSpaceStopInputFilter()});
                break;
            case IdentityType.PASSPORT_INT://护照
                mCodeValue.setText("");
                mCodeValue.setFilters(new InputFilter[]{StringUtil.limitInputFilter(PASSPORT_MAX), StringUtil.getSpaceStopInputFilter()});
                break;
            case IdentityType.ISSOLDIERCARD_INT://军人证
                mCodeValue.setText("");
                mCodeValue.setFilters(new InputFilter[]{StringUtil.limitInputFilter(SOLDIERCARD_MAX), StringUtil.getSpaceStopInputFilter()});
                break;
            case IdentityType.ISHKCARD_INT://港澳通行证
                mCodeValue.setText("");
                mCodeValue.setFilters(new InputFilter[]{StringUtil.limitInputFilter(PASSPORT_MAX), StringUtil.getSpaceStopInputFilter()});
                break;
        }
    }

    private void initClick() {
        mOrderTopView.setBackClickListener(new OrderTopView.BackClickListener() {
            @Override
            public void clickBack() {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        //保存
        mOrderTopView.setRightClickListener(new OrderTopView.RightClickListener() {
            @Override
            public void rightClick() {
                onFinish();
            }
        });
    }

    private void onFinish() {
        String idValue = mCodeValue.getText().toString();
        if (!StringUtil.isEmpty(mCodeType)) {
            //判断输入证件正确性
            if (TouristType.IDCertificateType.equals(mCodeType)) {
                if (!RegexUtil.isIdCard(idValue)) {
                    ToastUtil.showToast(this, getString(R.string.identity_error));
                    return;
                }
            } else if (TouristType.PassportCertificateType.equals(mCodeType)) {
                if (!RegexUtil.isPassport(idValue)) {
                    ToastUtil.showToast(this, getString(R.string.passport_error));
                    return;
                }
            } else if (TouristType.MilitaryCertificateType.equals(mCodeType)) {
                if (!RegexUtil.isSoldierCard(idValue)) {
                    ToastUtil.showToast(this, getString(R.string.soldierCard_error));
                    return;
                }
            } else {
                if (!RegexUtil.isHkcard(idValue)) {
                    ToastUtil.showToast(this, getString(R.string.hkcard_error));
                    return;
                }
            }

            //赋值
            mCertificate.cardNO = idValue;

            //新增游客时调用
            if (mUserContact == null) {
                //赋值并传递
                finishDown();
            } else {
                //编辑游客处理
                codeEdit();
            }

        }

    }

    private void finishDown() {
        Intent intent = new Intent();
        intent.putExtra(SPUtils.EXTRA_DATA, mCertificate);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void codeEdit() {
        if (mReqCode != -1) {
            //添加修改证件
            codeAddType();

            if (TouristType.ADD_CODE_REQ == mReqCode) {
                //访问网络
                mController.doAddCertificate(AddOrUpdateCodeActivity.this,mUserContact);
            } else if (TouristType.EDIT_CODE_REQ == mReqCode) {
                //访问网络,编辑证件信息
                mController.doUpdateCertificate(AddOrUpdateCodeActivity.this,mUserContact);
            }
        }
    }

    private void codeAddType() {

        if (TouristType.IDCertificateType.equals(mCodeType)) {
            if (mIdCertificate == null) {
                mIdCertificate = new IDCertificate();
            }
            mIdCertificate.cert = mCertificate;
            mUserContact.idCert = mIdCertificate;
        } else if (TouristType.PassportCertificateType.equals(mCodeType)) {
            if (mPassportCertificate == null) {
                mPassportCertificate = new PassportCertificate();
            }
            mPassportCertificate.cert = mCertificate;
            mUserContact.passportCert = mPassportCertificate;
        } else if (TouristType.MilitaryCertificateType.equals(mCodeType)) {
            if (mMilitaryCertificate == null) {
                mMilitaryCertificate = new MilitaryCertificate();
            }
            mMilitaryCertificate.cert = mCertificate;
            mUserContact.militaryCert = mMilitaryCertificate;
        } else {
            if (mHkMacaoCertificate == null) {
                mHkMacaoCertificate = new HkMacaoCertificate();
            }
            mHkMacaoCertificate.cert = mCertificate;
            mUserContact.hkMacaoCert = mHkMacaoCertificate;
        }

    }

    @Override
    public void handleMessage(Message msg) {
        hideLoadingView();
        hideErrorView(null);
        super.handleMessage(msg);
        switch (msg.what) {
            case ValueConstants.MSG_NEW_ADD_CODE_OK:
                Boolean isSuccess = (Boolean) msg.obj;
                if (isSuccess) {
                    ToastUtil.showToast(this, getString(R.string.add_finish));
                    finishDown();
                } else {
                    ToastUtil.showToast(this, getString(R.string.add_error));
                }
                break;
            case ValueConstants.MSG_NEW_ADD_CODE_KO:
                AndroidUtils.showToastCenter(AddOrUpdateCodeActivity.this, StringUtil.handlerErrorCode(getApplicationContext(), msg.arg1));
                break;
            case ValueConstants.MSG_GET_NEW_TOURISTCODE_UPDATE_OK://修改证件成功
                Boolean isValue = (Boolean) msg.obj;
                if(isValue){
                    ToastUtil.showToast(this, getString(R.string.edit_finish));
                    finishDown();
                }else {
                    ToastUtil.showToast(this, getString(R.string.edit_error));
                }
                break;
            case ValueConstants.MSG_GET_NEW_TOURISTCODE_UPDATE_KO://修改证件失败
                AndroidUtils.showToastCenter(AddOrUpdateCodeActivity.this, StringUtil.handlerErrorCode(getApplicationContext(), msg.arg1));
                break;
        }
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.activity_addorupdatecode, null);
    }

    @Override
    public View onLoadNavView() {
        mOrderTopView = new OrderTopView(this);
        return mOrderTopView;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }
}
