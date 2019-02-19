package com.quanyan.yhy.ui.common.person.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.util.RegexUtil;
import com.quanyan.base.util.StringUtil;
import com.quanyan.base.view.BaseNavView;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.IdentityType;
import com.quanyan.yhy.ui.base.utils.DialogUtil;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.common.person.PersonController;
import com.quanyan.yhy.util.InformationDialogUtils;
import com.yhy.common.beans.net.model.common.person.UserContact;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.SPUtils;
import com.yhy.service.IUserService;

/**
 * Created with Android Studio.
 * Title:PersonListActivity
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:和平必胜、正义必胜、人民必胜
 * Author:炎黄子孙
 * Date:15/10/30
 * Time:下午5:31
 * Version 1.0
 */
public class AddOrUpdateVisitorActivity extends BaseActivity implements View.OnClickListener, TextWatcher {

    @ViewInject(R.id.et_name)
    private EditText et_name;

    @ViewInject(R.id.ll_id_type_value)
    private LinearLayout ll_id_type_value;

    @ViewInject(R.id.tv_id_type)
    private TextView tv_id_type;

    @ViewInject(R.id.et_id_value)
    private EditText et_id_value;

    @ViewInject(R.id.et_phone_value)
    private EditText et_phone_value;

    @ViewInject(R.id.rl_select_id_type)
    private RelativeLayout rl_select_id_type;


    private UserContact userContact;

    private PersonController mController;

    private int TYPE_ADD = 0x001;
    private int TYPE_EDIT = 0x002;
    private int type;//区分点击事件

    private int whichSelect = 1;
    private int PASSPORT_MAX = 9;
    private int SOLDIERCARD_MAX = 20;
    private int IDCARD_MAX = 18;

    private int NAME_MEX = 20;

    @Autowired
    IUserService userService;

    /**
     * 跳转到添加游客
     */
    public static void gotoAddOrUpdateVisitorActivity(Activity context, int reqCode, UserContact userContact) {
        Intent intent = new Intent(context, AddOrUpdateVisitorActivity.class);
        intent.putExtra(SPUtils.EXTRA_DATA, userContact);
        context.startActivityForResult(intent, reqCode);
    }

    @Override
    public void handleMessage(Message msg) {
        hideLoadingView();
        switch (msg.what) {
            case ValueConstants.MSG_ADD_UPDATE_VISITOR_INFO_OK://添加成功
                ToastUtil.showToast(this, getString(R.string.add_finish));
                setResult(RESULT_OK);
                finish();
                break;
            case ValueConstants.MSG_ADD_UPDATE_VISITOR_INFO_KO:
                ToastUtil.showToast(this, getString(R.string.add_error));
                break;
            case ValueConstants.MSG_UPDATE_VISITOR_OK://修改成功
                ToastUtil.showToast(this, getString(R.string.edit_finish));
                setResult(RESULT_OK);
                finish();
                break;
            case ValueConstants.MSG_UPDATE_VISITOR_KO:
                ToastUtil.showToast(this, getString(R.string.edit_error));
                break;
        }
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.ac_add_update_visitor, null);
    }
    private BaseNavView mBaseNavView;

    @Override
    public View onLoadNavView() {
        mBaseNavView = new BaseNavView(this);
        mBaseNavView.setRightText(getString(R.string.label_btn_finish));
        mBaseNavView.setRightTextClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFinish();
            }
        });
        return mBaseNavView;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mController = new PersonController(this, mHandler);
        userContact = (UserContact) getIntent().getSerializableExtra(SPUtils.EXTRA_DATA);
        initViews();

        if (userContact == null) {
            mBaseNavView.setTitleText(getString(R.string.label_add_visitor));
            //sm_title_bar_content.setText(getString(R.string.label_add_visitor));
            type = TYPE_ADD;
            limitSet(0);
        } else {
            mBaseNavView.setTitleText(getString(R.string.title_add_or_update_contact));
            //sm_title_bar_content.setText(getString(R.string.title_add_or_update_contact));
            type = TYPE_EDIT;
            initDatas();
        }
        changeButton();
    }

    private void initDatas() {
        if (!StringUtil.isEmpty(userContact.contactName)) {
            et_name.setText(userContact.contactName);
        }

        if (!StringUtil.isEmpty(userContact.certificatesType)) {
            String idType = IdentityType.showIdType(this, userContact.certificatesType);
            tv_id_type.setText(idType);
            int i = Integer.parseInt(userContact.certificatesType);
            limitSet(i - 1);
        }

        if (!StringUtil.isEmpty(userContact.certificatesNum)) {
            et_id_value.setText(userContact.certificatesNum);
        }

        if (!StringUtil.isEmpty(userContact.contactPhone)) {
            et_phone_value.setText(userContact.contactPhone);
        }

    }


    private void initViews() {

        tv_id_type.setText(getResources().getStringArray(R.array.id_type)[0]);
        rl_select_id_type.setOnClickListener(this);
        et_name.addTextChangedListener(this);
        et_id_value.addTextChangedListener(this);
        et_phone_value.addTextChangedListener(this);

    }

    //完成按钮点击监听事件
    private void changeButton() {
        if (!StringUtil.isEmpty(et_name.getText().toString()) && !StringUtil.isEmpty(et_phone_value.getText().toString()) &&
                !StringUtil.isEmpty(et_id_value.getText().toString())) {

            mBaseNavView.setRightTextColor(R.color.neu_333333);
            mBaseNavView.setRightTextEnable(true);
        } else {
            mBaseNavView.setRightTextColor(R.color.neu_cccccc);
            mBaseNavView.setRightTextEnable(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_select_id_type:
                showSelectIdTypeDialog();
                break;
        }
    }

    //取消提示
    private void showDialog() {
        DialogUtil.showMessageDialog(this, null, getString(R.string.leave_message), getString(R.string.label_btn_ok),
                getString(R.string.label_btn_cancel), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }, null).show();
    }

    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(KeyEvent.KEYCODE_BACK == keyCode){
            showDialog();
        }
        return true;
    }*/

    /**
     * 选择身份证件
     */
    private void showSelectIdTypeDialog() {
        InformationDialogUtils.showAlert(AddOrUpdateVisitorActivity.this,
                getString(R.string.dialog_select_idtype),
                getResources().getStringArray(R.array.id_type),
                null,
                new InformationDialogUtils.OnAlertSelectId() {
                    @Override
                    public void onClick(int whichButton) {
                        whichSelect = whichButton + 1;
                        tv_id_type.setText(getResources().getStringArray(R.array.id_type)[whichButton]);
                        limitSet(whichButton);
                    }
                }, whichSelect);

    }

    //控制字数
    private void limitSet(int whichButton) {
        switch (whichButton){
            case IdentityType.IDCARD_INT://身份证
                et_id_value.setText("");
                et_id_value.setFilters(new InputFilter[]{ StringUtil.limitInputFilter(IDCARD_MAX), StringUtil.getSpaceStopInputFilter()});
                break;
            case IdentityType.PASSPORT_INT://护照
                et_id_value.setText("");
                et_id_value.setFilters(new InputFilter[]{ StringUtil.limitInputFilter(PASSPORT_MAX), StringUtil.getSpaceStopInputFilter()});
                break;
            case IdentityType.ISSOLDIERCARD_INT://军人证
                et_id_value.setText("");
                et_id_value.setFilters(new InputFilter[]{ StringUtil.limitInputFilter(SOLDIERCARD_MAX), StringUtil.getSpaceStopInputFilter()});
                break;
            case IdentityType.ISHKCARD_INT://港澳通行证
                et_id_value.setText("");
                et_id_value.setFilters(new InputFilter[]{ StringUtil.limitInputFilter(PASSPORT_MAX), StringUtil.getSpaceStopInputFilter()});
                break;
        }
    }


    /**
     * 保存联系人
     */
    private void onFinish() {
        String name = et_name.getText().toString();
        String idType = tv_id_type.getText().toString();
        String idValue = et_id_value.getText().toString();
        String phoneValue = et_phone_value.getText().toString().trim();
        //String deIdType = deShowIdType(idType);
        String deIdType = IdentityType.deShowIdType(this, idType);

        //姓名验证
        if (!RegexUtil.isName(name) || RegexUtil.isBeforOrEnd(name)) {
            ToastUtil.showToast(this, getString(R.string.name_error_limit));
            return;
        }

        //判断输入证件正确性
        if (idType.equals(getResources().getStringArray(R.array.id_type)[0])) {
            if(!RegexUtil.isIdCard(idValue)){
                ToastUtil.showToast(this, getString(R.string.identity_error));
                return;
            }
        } else if (idType.equals(getResources().getStringArray(R.array.id_type)[1])) {
            if(!RegexUtil.isPassport(idValue)){
                ToastUtil.showToast(this, getString(R.string.passport_error));
                return;
            }
        } else if (idType.equals(getResources().getStringArray(R.array.id_type)[2])) {
            if(!RegexUtil.isSoldierCard(idValue)){
                ToastUtil.showToast(this, getString(R.string.soldierCard_error));
                return;
            }
        } else {
            if(!RegexUtil.isHkcard(idValue)){
                ToastUtil.showToast(this, getString(R.string.hkcard_error));
                return;
            }
        }

        if (!RegexUtil.isMobile(phoneValue)) {
            ToastUtil.showToast(this, getString(R.string.phone_error));
            return;
        }

        if (userContact == null) {
            userContact = new UserContact();
        }
        userContact.contactName = name;
        userContact.certificatesNum = idValue;
        userContact.contactPhone = phoneValue;
        userContact.certificatesType = deIdType;
        userContact.userId = userService.getLoginUserId();

        if (type == TYPE_ADD) {
            showLoadingView(getString(R.string.dialog_add_ing));
            mController.doAddOrUpdateVisitorInfo(AddOrUpdateVisitorActivity.this,userContact);
        } else {
            showLoadingView(getString(R.string.dialog_edit_ing));
            mController.doUpdateVisitorInfo(AddOrUpdateVisitorActivity.this,userContact);
        }

    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String location_name = s.toString();
        if(location_name.equals(" ")){

        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        changeButton();

        /*if(AddOrUpdateVisitorActivity.this.getCurrentFocus() != null){
            if(AddOrUpdateVisitorActivity.this.getCurrentFocus().getId() == R.id.et_name){
                int editStart = et_name.getSelectionStart();
                int editEnd = et_name.getSelectionEnd();
                // 先去掉监听器，否则会出现栈溢出
                et_name.removeTextChangedListener(this);
                if (!TextUtils.isEmpty(et_name.getText())) {
                    //String etstring = et_name.getText().toString();
                    while (StringUtil.Length(s.toString()) > NAME_MEX) {
                        s.delete(editStart - 1, editEnd);
                        editStart--;
                        editEnd--;
                    }
                }

                et_name.setText(s);
                et_name.setSelection(editStart);

                // 恢复监听器
                et_name.addTextChangedListener(this);
            }
        }*/

    }
}
