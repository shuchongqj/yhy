package com.quanyan.yhy.ui.common.address.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.util.LocalUtils;
import com.quanyan.base.util.RegexUtil;
import com.quanyan.base.util.StringUtil;
import com.quanyan.base.view.BaseNavView;
import com.quanyan.yhy.R;
import com.quanyan.yhy.ui.base.utils.DialogUtil;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.common.address.AddressController;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.yhy.common.beans.net.model.common.address.Address;
import com.yhy.common.beans.net.model.common.address.MyAddressContentInfo;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.SPUtils;
import com.yhy.service.IUserService;

/**
 * Created with Android Studio.
 * Title:PersonListActivity
 * Description:收货地址填写
 * Copyright:Copyright (c) 2015
 * Company:和平必胜、正义必胜、人民必胜
 * Author:wjm
 * Date:16/7/1
 * Time:
 * Version 1.3.0
 */
public class AddOrUpdateAddressActivity extends BaseActivity implements View.OnClickListener, TextWatcher {

    @ViewInject(R.id.et_name)
    private EditText et_name;

    @ViewInject(R.id.et_phone)
    private EditText et_phone;

    @ViewInject(R.id.et_post_code)
    private EditText et_post_code;

    @ViewInject(R.id.ll_area)
    private RelativeLayout ll_area;

    @ViewInject(R.id.et_area)
    private TextView et_area;

    @ViewInject(R.id.et_address)
    private EditText et_address;

    /*@ViewInject(R.id.btn_finish)
    private Button btn_finish;*/

    @ViewInject(R.id.cb_is_default)
    private CheckBox cb_is_default;

    private MyAddressContentInfo mAddressInfo;

    private AddressController mController;
    private String mProvince;
    private String mCity;
    private int TYPE_ADD = 0x001;
    private int TYPE_EDIT = 0x002;
    private int type;//区分点击事件

    private int NAME_MEX = 20;

    private String isDefAddress = "1";
    private String notDefAddress = "0";

    @Autowired
    IUserService userService;

    /**
     * 跳转到添加地址
     */
    public static void gotoAddOrUpdateAddressActivity(Activity context, int reqCode, MyAddressContentInfo myAddress) {
        Intent intent = new Intent(context, AddOrUpdateAddressActivity.class);
        intent.putExtra(SPUtils.EXTRA_DATA, myAddress);
        context.startActivityForResult(intent, reqCode);
    }

    @Override
    public void handleMessage(Message msg) {
        hideLoadingView();
        switch (msg.what) {
            case ValueConstants.MSG_ADD_UPDATE_ADDRESS_INFO_OK://新增
                ToastUtil.showToast(this, getString(R.string.add_finish));
                setResult(RESULT_OK);
                finish();
                break;
            case ValueConstants.MSG_ADD_UPDATE_ADDRESS_INFO_KO:
                ToastUtil.showToast(this, getString(R.string.add_error));
                break;
            case ValueConstants.MSG_UPDATE_ADDRESS_OK://修改
                ToastUtil.showToast(this, getString(R.string.edit_finish));
                setResult(RESULT_OK);
                finish();
                break;
            case ValueConstants.MSG_UPDATE_ADDRESS_KO:
                ToastUtil.showToast(this, getString(R.string.edit_error));
                break;
        }
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.ac_add_update_address, null);
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
        ViewUtils.inject(this);
        mController = new AddressController(this, mHandler);
        mAddressInfo = (MyAddressContentInfo) getIntent().getSerializableExtra(SPUtils.EXTRA_DATA);
        if (mAddressInfo == null) {
            mBaseNavView.setTitleText(getString(R.string.title_add_or_update_address));
            //sm_title_bar_content.setText(getString(R.string.title_add_or_update_address));
            type = TYPE_ADD;
        } else {
            mBaseNavView.setTitleText(getString(R.string.title_update_address));
            //sm_title_bar_content.setText(getString(R.string.title_update_address));
            type = TYPE_EDIT;
            initDatas();
        }
        initViews();
    }

    private void initDatas() {
        /*AddressInfo info = mAddressInfo.addressInfo;
        if(info != null){
            et_area.setText(info.province + " " + info.city + " " + info.area);
        }*/
        if (!StringUtil.isEmpty(mAddressInfo.recipientsName)) {
            et_name.setText(mAddressInfo.recipientsName);
        }

        if (!StringUtil.isEmpty(mAddressInfo.recipientsPhone)) {
            et_phone.setText(mAddressInfo.recipientsPhone);
        }

        if (!StringUtil.isEmpty(mAddressInfo.zipCode)) {
            et_post_code.setText(mAddressInfo.zipCode);
        }

        if (!StringUtil.isEmpty(mAddressInfo.province)) {
            if (mAddressInfo.province.equals(mAddressInfo.city)) {
                et_area.setText(mAddressInfo.province + " " + mAddressInfo.area);
            } else {
                et_area.setText(mAddressInfo.province + " " + mAddressInfo.city + " " + mAddressInfo.area);
            }
        }


        if (!StringUtil.isEmpty(mAddressInfo.detailAddress)) {
            et_address.setText(mAddressInfo.detailAddress);
        }

        if (!StringUtil.isEmpty(mAddressInfo.isDefault)) {
            cb_is_default.setChecked(mAddressInfo.isDefault.equals(isDefAddress));
        }
    }

    private void initViews() {
        ll_area.setOnClickListener(this);
//        et_address.addTextChangedListener(this);
//        et_area.addTextChangedListener(this);
//        et_name.addTextChangedListener(this);
//        et_phone.addTextChangedListener(this);
        et_address.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    return true;
                }
                return false;
            }


        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_area:
                NavUtils.gotoAreaSelect(this, null, 0);
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

   /* @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(KeyEvent.KEYCODE_BACK == keyCode){
            showDialog();
        }
        return true;
    }*/

    /**
     * 保存联系人
     */
    private void onFinish() {
        String name = et_name.getText().toString();
        String phone = et_phone.getText().toString().trim();
//        String postCode = et_post_code.getText().toString();
        String area = et_area.getText().toString();
        String address = et_address.getText().toString().trim();
        String isDefault = defaultAddress(cb_is_default.isChecked());

        /*//前后是否有空格
        if(RegexTool.isBeforOrEnd(name)){
            //System.out.println("ApiContext" + 111);
            ToastUtil.showToast(this, getString(R.string.name_error_limit));
            return;
        }*/

        if (!RegexUtil.isName(name)) {
            ToastUtil.showToast(this, getString(R.string.name_error_limit));
            return;
        }

        if (!LocalUtils.isMobileNO(phone)) {
            ToastUtil.showToast(this, getString(R.string.phone_error));
            return;
        }

//        if (StringUtil.isEmpty(postCode) || !RegexUtil.isCode(postCode)) {
//            ToastUtil.showToast(this, getString(R.string.postcode_error));
//            return;
//        }

        if (StringUtil.isEmpty(area)) {
            ToastUtil.showToast(this, getString(R.string.area_null));
            return;
        }

        if (address.length() < 2 || address.length() > 100) {
            ToastUtil.showToast(this, getString(R.string.address_error));
            return;
        }


        /*if(StringUtil.isEmpty(area)){
            return;
        }

        if(StringUtil.isEmpty(address)){
            return;
        }*/

        if (mAddressInfo == null) {
            mAddressInfo = new MyAddressContentInfo();
        }
        mAddressInfo.recipientsName = name;
        mAddressInfo.recipientsPhone = phone;
//        mAddressInfo.zipCode = postCode;
        if (mAddressBean != null) {
            mAddressInfo.city = mAddressBean.city;
            mAddressInfo.cityCode = mAddressBean.cityCode;
            mAddressInfo.area = mAddressBean.area.replaceAll("　", "");
            mAddressInfo.areaCode = mAddressBean.areaCode;
            mAddressInfo.province = mAddressBean.province;
            mAddressInfo.provinceCode = mAddressBean.provinceCode;
        }
        //mAddressInfo.addressInfo = mAddressBean;
        mAddressInfo.detailAddress = address;
        mAddressInfo.isDefault = isDefault;
        mAddressInfo.userId = userService.getLoginUserId();
        if (type == TYPE_ADD) {
            showLoadingView(getString(R.string.dialog_add_ing));
            mController.doAddOrUpdateAddressInfo(AddOrUpdateAddressActivity.this, mAddressInfo);
        } else {
            showLoadingView(getString(R.string.dialog_edit_ing));
            mController.doUpdateAddressInfo(AddOrUpdateAddressActivity.this, mAddressInfo);
        }

    }

    private String defaultAddress(boolean checked) {

        return checked ? isDefAddress : notDefAddress;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (0 == requestCode) {
            showAddress(resultCode, data);
        }
    }

    /**
     * 展示城市
     *
     * @param resultCode
     * @param data
     */
    Address mAddressBean;

    private void showAddress(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Address addressCodeName = (Address) data
                    .getSerializableExtra(SPUtils.EXTRA_DATA);
            if (null == mAddressBean) {
                mAddressBean = new Address();
            }
            mAddressBean.city = addressCodeName.city;
            mAddressBean.cityCode = addressCodeName.cityCode;
            mAddressBean.areaCode = addressCodeName.areaCode;
            mAddressBean.area = addressCodeName.area;
            mAddressBean.province = addressCodeName.province;
            mAddressBean.provinceCode = addressCodeName.provinceCode;
            StringBuffer buffer = new StringBuffer();
            showCity(addressCodeName, buffer);
            et_area.setText(buffer.toString());
        }
    }

    private void showCity(Address addressCodeName, StringBuffer buffer) {
        buffer.append(StringUtil.nullToEmpty(addressCodeName.province));
        if (!addressCodeName.city.equals(addressCodeName.province)) {
            buffer.append("  ");
            buffer.append(StringUtil.nullToEmpty(addressCodeName.city));
        }

        if (!StringUtil.isEmpty(addressCodeName.city)) {
            buffer.append("  ");
        }
        if (!StringUtil.isEmpty(addressCodeName.area)) {
            buffer.append(StringUtil.nullToEmpty(addressCodeName.area.replaceAll("　", "")));
        }
    }

    private void changeButton() {
        if (!StringUtil.isEmpty(et_name.getText().toString()) && !StringUtil.isEmpty(et_address.getText().toString()) &&
                !StringUtil.isEmpty(et_phone.getText().toString()) && !StringUtil.isEmpty(et_area.getText().toString())) {

            mBaseNavView.setRightTextEnable(true);
            mBaseNavView.setRightTextColor(R.color.neu_333333);
        } else {
            mBaseNavView.setRightTextEnable(false);
            mBaseNavView.setRightTextColor(R.color.neu_cccccc);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        changeButton();

        /*if(AddOrUpdateAddressActivity.this.getCurrentFocus() != null){
            if(AddOrUpdateAddressActivity.this.getCurrentFocus().getId() == R.id.et_name){
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
