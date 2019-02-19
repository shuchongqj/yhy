package com.quanyan.yhy.ui.common.tourist;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.harwkin.nb.camera.TimeUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.util.RegexUtil;
import com.quanyan.base.util.StringUtil;
import com.quanyan.base.view.customview.scrolldeletelistview.ScrollDeleteListView;
import com.quanyan.yhy.R;
import com.quanyan.yhy.ui.adapter.base.BaseAdapterHelper;
import com.quanyan.yhy.ui.adapter.base.QuickAdapter;
import com.quanyan.yhy.ui.base.utils.DialogUtil;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.common.tourist.adapter.DeleteCodeAdapter;
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
import com.yhy.service.IUserService;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created with Android Studio.
 * Title:AddOrUpdateMimeTouristActivity
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-6-6
 * Time:16:34
 * Version 1.0
 * Description: 增加或者修改我的常用游客界面
 */
public class AddOrUpdateMimeTouristActivity extends BaseActivity {

    private UserContact mUserContact;
    private OrderTopView mOrderTopView;
    @ViewInject(R.id.delete_listview)
    private ScrollDeleteListView mDeleteListView;
    private int whichSelect = 1;
    private Dialog mCodeDialog;

    private LayoutInflater mInflater;
    private View mHeadView;

    private DeleteCodeAdapter mAdapter;
    private EditText mChineseName;
    private EditText mFirstName;
    private EditText mLastName;
    private TextView mAddCode;
    private View mCodeDialogList;
    private ListView mCodeList;
    private String[] mCodeArray;

    private IDCertificate mIdCertificate;//身份证
    private MilitaryCertificate mMilitaryCertificate;//军人证
    private PassportCertificate mPassportCertificate;//护照
    private HkMacaoCertificate mHkMacaoCertificate;//港澳通行证
    private Certificate mCertificate;
    private Certificate mCertificate1;
    private Certificate mCertificate2;
    private Certificate mCertificate3;
    private Certificate mCertificate4;
    private ArrayList<Certificate> mCertificates = new ArrayList<>();
    private DeleteCodeAdapter mDeleteAdapter;
    private TouristController mController;
    private EditText mTelNumber;
    private String mDeleteType;

    @Autowired
    IUserService userService;


    public static void gotoAddOrUpdateMimeTouristActivity(Activity context, int reqCode, UserContact userContact) {
        Intent intent = new Intent(context, AddOrUpdateMimeTouristActivity.class);
        intent.putExtra(SPUtils.EXTRA_DATA, userContact);
        context.startActivityForResult(intent, reqCode);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ViewUtils.inject(this);
        mController = new TouristController(this, mHandler);
        mUserContact = (UserContact) getIntent().getSerializableExtra(SPUtils.EXTRA_DATA);
        mInflater = LayoutInflater.from(this);
        mHeadView = mInflater.inflate(R.layout.add_mime_tourist_headview, null);
        mCodeArray = getResources().getStringArray(R.array.id_type);
        findId();
        if (mUserContact == null) {
            mOrderTopView.setOrderTopTitle("新增游客");
        } else {
            mOrderTopView.setOrderTopTitle("编辑游客");
            //初始化数据
            editDataSet();
        }
        mOrderTopView.setRightViewVisible("保存");

        mDeleteListView.addHeaderView(mHeadView);

        mDeleteAdapter = new DeleteCodeAdapter(this, 0);
        mDeleteListView.setAdapter(mDeleteAdapter);
        //编辑时证件信息
        sourceCodeEncape();

        initClick();
    }

    private void sourceCodeEncape() {
        if(mUserContact != null){
            if(mUserContact.idCert != null && mUserContact.idCert.cert != null){
                mCertificates.add(mUserContact.idCert.cert);
            }
            if(mUserContact.passportCert != null && mUserContact.passportCert.cert != null){
                mCertificates.add(mUserContact.passportCert.cert);
            }
            if(mUserContact.militaryCert != null && mUserContact.militaryCert.cert != null){
                mCertificates.add(mUserContact.militaryCert.cert);
            }
            if(mUserContact.hkMacaoCert != null && mUserContact.hkMacaoCert.cert != null){
                mCertificates.add(mUserContact.hkMacaoCert.cert);
            }
            addCodeGoneOrVisable();
            mDeleteAdapter.setData(mCertificates);
        }
    }

    private void editDataSet() {
        if(!StringUtil.isEmpty(mUserContact.contactName)){
            mChineseName.setText(mUserContact.contactName);
        }

        if (!StringUtil.isEmpty(mUserContact.lastName)) {
            mFirstName.setText(mUserContact.lastName);//英文姓
        }

        if (!StringUtil.isEmpty(mUserContact.firstName)) {
            mLastName.setText(mUserContact.firstName);//英文名
        }

        if(!StringUtil.isEmpty(mUserContact.contactPhone)){
            mTelNumber.setText(mUserContact.contactPhone);
        }


    }

    private void findId() {
        mChineseName = (EditText) mHeadView.findViewById(R.id.et_name);
        mFirstName = (EditText) mHeadView.findViewById(R.id.et_head_name);
        mLastName = (EditText) mHeadView.findViewById(R.id.et_last_name);
        mAddCode = (TextView) mHeadView.findViewById(R.id.tv_add_code);
        mTelNumber = (EditText) mHeadView.findViewById(R.id.et_tel);
    }

    private void initClick() {
        mOrderTopView.setBackClickListener(new OrderTopView.BackClickListener() {
            @Override
            public void clickBack() {
                finish();
            }
        });

        //保存
        mOrderTopView.setRightClickListener(new OrderTopView.RightClickListener() {
            @Override
            public void rightClick() {
                //保存
                onFinish();
            }
        });

        //添加证件
        mAddCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TimeUtil.isFastDoubleClick()) {
                    return;
                }
                showSelectIdTypeDialog();
                mCodeDialog.show();
            }
        });

        //编辑删除证件号码点击事件
        mDeleteAdapter.setCodeAdapterlListener(new DeleteCodeAdapter.DeleteCodeClickListener() {
            @Override
            public void delete(Certificate personCode) {
                //删除操作
                deleteSet(personCode);
            }

            @Override
            public void edit(Certificate personCode) {
                NavUtils.gotoAddOrUpdateCodeActivity(AddOrUpdateMimeTouristActivity.this, TouristType.EDIT_CODE_REQ, personCode, mUserContact);
            }
        });

        //证件列表点击
        mDeleteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    private void deleteSet(Certificate personCode) {
        mDeleteType = personCode.type;
        //添加时删除
        if(mUserContact == null){
            //删除列表
            deleteListCode();
        }else {
            //修改时删除
            mController.doDeleteCertificate(AddOrUpdateMimeTouristActivity.this,mUserContact.id, mDeleteType);
        }
    }

    private void deleteListCode() {
        for (int i = 0; i < mCertificates.size(); i++) {
            if(mCertificates.get(i).type.equals(mDeleteType)){
                mCertificates.remove(i);
                mDeleteAdapter.setData(mCertificates);
            }
        }

        codeEncape();

        addCodeGoneOrVisable();
    }

    private void onFinish() {
        String chineseName = mChineseName.getText().toString().trim();
        String firstName = mFirstName.getText().toString().trim();
        String lastName = mLastName.getText().toString().trim();
        String phoneNumber = mTelNumber.getText().toString().trim();
        //姓名验证
        if (!RegexUtil.isName(chineseName) || RegexUtil.isBeforOrEnd(chineseName)) {
            ToastUtil.showToast(this, getString(R.string.name_error_limit));
            return;
        }

        //电话号码验证
        if (!RegexUtil.isMobile(phoneNumber)) {
            ToastUtil.showToast(this, getString(R.string.phone_error));
            return;
        }

        //英文姓和英文名
        if (mCertificates != null && mCertificates.size() > 0) {
            for (int i = 0; i < mCertificates.size(); i++) {
                if (TouristType.PassportCertificateType.equals(mCertificates.get(i).type) || TouristType.HkMacaoCertificateType.equals(mCertificates.get(i).type)) {
                    if (StringUtil.isEmpty(firstName)) {
                        ToastUtil.showToast(this, getString(R.string.englist_name_first));
                        return;
                    }

                    if (StringUtil.isEmpty(lastName)) {
                        ToastUtil.showToast(this, getString(R.string.englist_name_last));
                        return;
                    }
                }
            }
        }

        if (mUserContact == null) {
            mUserContact = new UserContact();
            mUserContact.userId = userService.getLoginUserId();
            mUserContact.contactName = chineseName;
            mUserContact.contactPhone = phoneNumber;
            mUserContact.lastName = firstName;
            mUserContact.firstName = lastName;
            //证件封装
            codeEncape();
            //添加联系人
            mController.doAddOrUpdateVisitorInfo(AddOrUpdateMimeTouristActivity.this,mUserContact);
        }else {
            mUserContact.userId = userService.getLoginUserId();
            mUserContact.contactName = chineseName;
            mUserContact.contactPhone = phoneNumber;
            mUserContact.lastName = firstName;
            mUserContact.firstName = lastName;
            //证件封装
            codeEncape();
            //添加联系人
            mController.doUpdateVisitorInfo(AddOrUpdateMimeTouristActivity.this,mUserContact);
        }

    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what){
            case ValueConstants.MSG_NEW_ADD_TOURIST_OK:
                ToastUtil.showToast(this, getString(R.string.add_finish));
                setResult(RESULT_OK);
                finish();
                break;
            case ValueConstants.MSG_NEW_ADD_TOURIST_KO:
                mUserContact = null;
                AndroidUtils.showToastCenter(AddOrUpdateMimeTouristActivity.this, StringUtil.handlerErrorCode(getApplicationContext(), msg.arg1));
                break;
            case ValueConstants.MSG_NEW_EDIT_TOURIST_OK:
                Boolean isSuccess = (Boolean) msg.obj;
                if(isSuccess){
                    ToastUtil.showToast(this, getString(R.string.edit_finish));
                    setResult(RESULT_OK);
                    finish();
                }else {
                    ToastUtil.showToast(this, getString(R.string.edit_error));
                }
                break;
            case ValueConstants.MSG_NEW_EDIT_TOURIST_KO:
                AndroidUtils.showToastCenter(AddOrUpdateMimeTouristActivity.this, StringUtil.handlerErrorCode(getApplicationContext(), msg.arg1));
                break;
            case ValueConstants.MSG_NEW_DELETE_CODE_OK:
                Boolean isDelete = (Boolean) msg.obj;
                if(isDelete){
                    ToastUtil.showToast(this, getString(R.string.toast_delete_subject_ok));
                    deleteListCode();
                }else {
                    ToastUtil.showToast(this, getString(R.string.toast_delete_subject_ko));
                }
                break;
            case ValueConstants.MSG_NEW_DELETE_CODE_KO:
                AndroidUtils.showToastCenter(AddOrUpdateMimeTouristActivity.this, StringUtil.handlerErrorCode(getApplicationContext(), msg.arg1));
                break;
        }
    }

    private void codeEncape() {
        if(mUserContact != null) {
            mUserContact.idCert = null;
            mUserContact.passportCert = null;
            mUserContact.militaryCert = null;
            mUserContact.hkMacaoCert = null;
        }

        if (mCertificates != null && mCertificates.size() > 0) {
            for (int i = 0; i < mCertificates.size(); i++) {
                if (TouristType.IDCertificateType.equals(mCertificates.get(i).type)) {//身份证
                    if(mIdCertificate == null){
                        mIdCertificate = new IDCertificate();
                    }
                    mIdCertificate.cert = mCertificates.get(i);
                    mUserContact.idCert = mIdCertificate;
                } else if (TouristType.PassportCertificateType.equals(mCertificates.get(i).type)) {//护照
                    if(mPassportCertificate == null){
                        mPassportCertificate = new PassportCertificate();
                    }
                    mPassportCertificate.cert = mCertificates.get(i);
                    mUserContact.passportCert = mPassportCertificate;

                } else if (TouristType.MilitaryCertificateType.equals(mCertificates.get(i).type)) {//军人证
                    if(mMilitaryCertificate == null){
                        mMilitaryCertificate = new MilitaryCertificate();
                    }
                    mMilitaryCertificate.cert = mCertificates.get(i);
                    mUserContact.militaryCert = mMilitaryCertificate;
                } else if (TouristType.HkMacaoCertificateType.equals(mCertificates.get(i).type)) {//港澳通行证
                    if(mHkMacaoCertificate == null){
                        mHkMacaoCertificate = new HkMacaoCertificate();
                    }
                    mHkMacaoCertificate.cert = mCertificates.get(i);
                    mUserContact.hkMacaoCert = mHkMacaoCertificate;
                }
            }
        }
    }

    private void showSelectIdTypeDialog() {
        mCodeDialogList = View.inflate(this, R.layout.code_dialog, null);
        mCodeList = (ListView) mCodeDialogList.findViewById(R.id.lv_code_name);
        QuickAdapter<String> mAdapter = new QuickAdapter<String>(this, R.layout.item_dialog_code, Arrays.asList(mCodeArray)) {
            @Override
            protected void convert(BaseAdapterHelper helper, String item) {
                helper.setText(R.id.popup_text, item);
            }
        };
        mCodeList.setAdapter(mAdapter);

        mCodeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //点击跳转相应的证件添加界面
                createDataEncape(position);
            }
        });
        mCodeDialog = DialogUtil.getMenuDialog(this, mCodeDialogList);
        mCodeDialog.setCanceledOnTouchOutside(true);
    }

    private void createDataEncape(int position) {
        if (mCertificate == null) {
            mCertificate = new Certificate();
        }
        switch (position) {
            case 0:
                mCertificate.type = TouristType.IDCertificateType;
                break;
            case 1:
                mCertificate.type = TouristType.PassportCertificateType;
                break;
            case 2:
                mCertificate.type = TouristType.MilitaryCertificateType;
                break;
            case 3:
                mCertificate.type = TouristType.HkMacaoCertificateType;
                break;
        }
        mCodeDialog.dismiss();
        NavUtils.gotoAddOrUpdateCodeActivity(this, TouristType.ADD_CODE_REQ, mCertificate, mUserContact);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == TouristType.ADD_CODE_REQ || requestCode == TouristType.EDIT_CODE_REQ) {
                Certificate certificate = (Certificate) data.getSerializableExtra(SPUtils.EXTRA_DATA);
                if (certificate != null) {
                    //添加证件信息，对应添加
                    setCertificateInfo(certificate);
                }else{
                    if(mDeleteAdapter != null) {
                        mDeleteAdapter.notifyDataSetChanged();
                    }
                }
            }
        }
    }

    private void setCertificateInfo(Certificate certificate) {
        if (mCertificates.size() == 0) {
            mCertificates.add(certificate);
        } else {
            for (int i = 0; i < mCertificates.size(); i++) {
                if (mCertificates.get(i).type.equals(certificate.type)) {
                    mCertificates.remove(i);
                }
            }
            mCertificates.add(certificate);
        }
        //显示隐藏添加证件按钮
        addCodeGoneOrVisable();

        mDeleteAdapter.setData(mCertificates);

    }

    private void addCodeGoneOrVisable() {
        if(mCertificates.size() < 4){
            mAddCode.setVisibility(View.VISIBLE);
        }else {
            mAddCode.setVisibility(View.GONE);
        }
    }


    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.activity_addorupdatemimetourist, null);
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
