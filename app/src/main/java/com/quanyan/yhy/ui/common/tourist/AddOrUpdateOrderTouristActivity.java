package com.quanyan.yhy.ui.common.tourist;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.harwkin.nb.camera.TimeUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.util.RegexUtil;
import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.IdentityType;
import com.quanyan.yhy.ui.adapter.base.BaseAdapterHelper;
import com.quanyan.yhy.ui.adapter.base.QuickAdapter;
import com.quanyan.yhy.ui.base.utils.DialogUtil;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.order.OrderTopView;
import com.yhy.common.beans.net.model.common.person.UserContact;
import com.yhy.common.beans.net.model.user.Certificate;
import com.yhy.common.beans.net.model.user.HkMacaoCertificate;
import com.yhy.common.beans.net.model.user.IDCertificate;
import com.yhy.common.beans.net.model.user.MilitaryCertificate;
import com.yhy.common.beans.net.model.user.PassportCertificate;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.SPUtils;
import com.yhy.service.IUserService;

import java.util.Arrays;

/**
 * Created with Android Studio.
 * Title:AddOrUpdateOrderTouristActivity
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-6-6
 * Time:16:35
 * Version 1.0
 * Description: 增加或者修改订单里面游客（或者联系人）信息
 */
public class AddOrUpdateOrderTouristActivity extends BaseActivity {

    private static final String TYPE = "type";
    private static final String TOURISTTYPE = "touristType";
    private static final String USERS = "UserContact";

    private OrderTopView mOrderTopView;
    private View mTravelIn;
    private View mTravelOut;
    private UserContact mUserContact;
    private int channelType;
    private String touristType;

    //国内
    @ViewInject(R.id.et_travelin_name)
    private EditText mTravelInName;
    @ViewInject(R.id.ll_travelin_cardtype)
    private LinearLayout mTravelInCardType;
    @ViewInject(R.id.tv_travelin_cardtype)
    private TextView mTravelInCard;
    @ViewInject(R.id.et_travelin_cardtnum)
    private EditText mTravelInCardNum;
    @ViewInject(R.id.et_travelin_tel)
    private EditText mTravelInTel;

    //国外游
    @ViewInject(R.id.et_travelout_name)
    private EditText mTravelOutName;
    @ViewInject(R.id.et_travelout_lasttname)
    private EditText mTravelOutLastName;
    @ViewInject(R.id.et_travelout_firstname)
    private EditText mTravelOutFirstName;
    @ViewInject(R.id.et_travelout_cardnum)
    private EditText mTravelOutCardNum;
    @ViewInject(R.id.et_travelout_tel)
    private EditText mTravelOutTel;

    private Dialog mCodeDialog;
    private View mCodeDialogList;
    private ListView mCodeList;
    private String[] mCodeArray;

    @Autowired
    IUserService userService;

    private TouristController mController;

    private int dialogClickPosition = 0;

    private int PASSPORT_MAX = 9;
    private int SOLDIERCARD_MAX = 20;
    private int IDCARD_MAX = 18;
    private int type;//区分点击事件
    private int TYPE_ADD = 0x001;
    private int TYPE_EDIT = 0x002;

    public static void gotoAddOrUpdateOrderTouristActivity(Activity context, int reqCode, int channelType, String touristType, UserContact userContact) {
        Intent intent = new Intent(context, AddOrUpdateOrderTouristActivity.class);
        intent.putExtra(TYPE, channelType);
        intent.putExtra(TOURISTTYPE, touristType);
        intent.putExtra(USERS, userContact);
        context.startActivityForResult(intent, reqCode);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ViewUtils.inject(this);
        mUserContact = (UserContact) getIntent().getSerializableExtra(USERS);
        channelType = getIntent().getIntExtra(TYPE, 0);
        touristType = getIntent().getStringExtra(TOURISTTYPE);
        mController = new TouristController(this, mHandler);

        mTravelIn = (View) this.findViewById(R.id.travelin_include);
        mTravelOut = (View) this.findViewById(R.id.travelout_include);
        mOrderTopView.setRightViewVisible("保存");
        setBottomDialog();
        if (mUserContact == null) {
            type = TYPE_ADD;
            if (channelType == TouristType.ORDERCONTACTS) {
                mOrderTopView.setOrderTopTitle("新增联系人");
                mTravelIn.setVisibility(View.VISIBLE);
                mTravelOut.setVisibility(View.GONE);
            } else {
                mOrderTopView.setOrderTopTitle("新增游客");
                if (touristType.equals(TouristType.TRAVELIN)) {
                    mTravelIn.setVisibility(View.VISIBLE);
                    mTravelOut.setVisibility(View.GONE);
                } else {
                    limitSet(1);
                    mTravelIn.setVisibility(View.GONE);
                    mTravelOut.setVisibility(View.VISIBLE);
                }
            }
        } else {
            type = TYPE_EDIT;
            if (channelType == TouristType.ORDERCONTACTS) {
                mOrderTopView.setOrderTopTitle("修改联系人");
                mTravelIn.setVisibility(View.VISIBLE);
                mTravelOut.setVisibility(View.GONE);
            } else {
                mOrderTopView.setOrderTopTitle("修改游客");
                if (touristType.equals(TouristType.TRAVELIN)) {
                    mTravelIn.setVisibility(View.VISIBLE);
                    mTravelOut.setVisibility(View.GONE);
                } else {
                    limitSet(1);
                    mTravelIn.setVisibility(View.GONE);
                    mTravelOut.setVisibility(View.VISIBLE);
                }
            }

            setUpdateView(mUserContact);
        }

        mTravelOutCardNum.setFilters(new InputFilter[]{StringUtil.limitInputFilter(PASSPORT_MAX), StringUtil.getSpaceStopInputFilter()});

        initClick();
    }

    /**
     * 设置底部证件弹框
     */
    private void setBottomDialog() {
        mCodeArray = getResources().getStringArray(R.array.id_type);
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
                if (mCodeDialog != null && mCodeDialog.isShowing()) {
                    mCodeDialog.dismiss();
                }
                mTravelInCard.setText(mCodeArray[position]);
                limitSet(position);
                dialogClickPosition = position;
            }
        });

        mCodeDialog = DialogUtil.getMenuDialog(this, mCodeDialogList);
        mCodeDialog.setCanceledOnTouchOutside(true);
    }


    private void initClick() {
        mOrderTopView.setBackClickListener(new OrderTopView.BackClickListener() {
            @Override
            public void clickBack() {
                AddOrUpdateOrderTouristActivity.this.finish();
            }
        });

        //保存
        mOrderTopView.setRightClickListener(new OrderTopView.RightClickListener() {
            @Override
            public void rightClick() {
                if (TimeUtil.isFastDoubleClick()) {
                    return;
                }
                configParams();
            }
        });

        //弹出证件选择框
        mTravelInCardType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCodeDialog != null) {
                    mCodeDialog.show();
                }
            }
        });
    }

    /**
     * 修改游客或者联系人
     *
     * @param userContact
     */
    private void setUpdateView(UserContact userContact) {
        if (userContact == null) {
            return;
        }
        if (channelType == TouristType.ORDERCONTACTS || touristType.equals(TouristType.TRAVELIN)) {
            if (!TextUtils.isEmpty(userContact.contactName)) {
                mTravelInName.setText(userContact.contactName);
            } else {
                mTravelInName.setText("");
            }

            if (userContact.idCert != null) {
                dialogClickPosition = 0;
                mTravelInCardNum.setText(userContact.idCert.cert.cardNO);
            } else {
                if (userContact.passportCert != null) {
                    dialogClickPosition = 1;
                    mTravelInCardNum.setText(userContact.passportCert.cert.cardNO);
                } else {
                    if (userContact.militaryCert != null) {
                        dialogClickPosition = 2;
                        mTravelInCardNum.setText(userContact.militaryCert.cert.cardNO);
                    } else {
                        if (userContact.hkMacaoCert != null) {
                            dialogClickPosition = 3;
                            mTravelInCardNum.setText(userContact.hkMacaoCert.cert.cardNO);
                        } else {
                            dialogClickPosition = 0;
                            mTravelInCardNum.setText("");
                        }
                    }
                }
            }

            if (!TextUtils.isEmpty(userContact.contactPhone)) {
                mTravelInTel.setText(userContact.contactPhone);
            } else {
                mTravelInTel.setText("");
            }
            limitSet(dialogClickPosition);
            mTravelInCard.setText(mCodeArray[dialogClickPosition]);
        } else {
            if (!TextUtils.isEmpty(userContact.contactName)) {
                mTravelOutName.setText(userContact.contactName);
            } else {
                mTravelOutName.setText("");
            }

            if (!TextUtils.isEmpty(userContact.lastName)) {
                mTravelOutLastName.setText(userContact.lastName);
            } else {
                mTravelOutLastName.setText("");
            }

            if (!TextUtils.isEmpty(userContact.firstName)) {
                mTravelOutFirstName.setText(userContact.firstName);
            } else {
                mTravelOutFirstName.setText("");
            }

            if (userContact.passportCert != null && userContact.passportCert.cert != null && !TextUtils.isEmpty(userContact.passportCert.cert.cardNO)) {
                mTravelOutCardNum.setText(userContact.passportCert.cert.cardNO);
            } else {
                mTravelOutCardNum.setText("");
            }

            if (!TextUtils.isEmpty(userContact.contactPhone)) {
                mTravelOutTel.setText(userContact.contactPhone);
            } else {
                mTravelOutTel.setText("");
            }
        }

    }

    /**
     * 确认填写的是否正确
     */
    private void configParams() {
        if (channelType == TouristType.ORDERCONTACTS || touristType.equals(TouristType.TRAVELIN)) {
            String userName = mTravelInName.getText().toString();
            //姓名验证
            if (!RegexUtil.isName(userName) || RegexUtil.isBeforOrEnd(userName)) {
                ToastUtil.showToast(this, getString(R.string.name_error_limit));
                return;
            }

            String idType = mTravelInCard.getText().toString();
            String userCardNum = mTravelInCardNum.getText().toString();
            //判断输入证件正确性
            if (idType.equals(getResources().getStringArray(R.array.id_type)[0])) {
                if (!RegexUtil.isIdCard(userCardNum)) {
                    ToastUtil.showToast(this, getString(R.string.identity_error));
                    return;
                }
            } else if (idType.equals(getResources().getStringArray(R.array.id_type)[1])) {
                if (!RegexUtil.isPassport(userCardNum)) {
                    ToastUtil.showToast(this, getString(R.string.passport_error));
                    return;
                }
            } else if (idType.equals(getResources().getStringArray(R.array.id_type)[2])) {
                if (!RegexUtil.isSoldierCard(userCardNum)) {
                    ToastUtil.showToast(this, getString(R.string.soldierCard_error));
                    return;
                }
            } else {
                if (!RegexUtil.isHkcard(userCardNum)) {
                    ToastUtil.showToast(this, getString(R.string.hkcard_error));
                    return;
                }
            }

            String userTel = mTravelInTel.getText().toString();
            if (!RegexUtil.isMobile(userTel)) {
                ToastUtil.showToast(this, getString(R.string.phone_error));
                return;
            }

            if (mUserContact == null) {
                mUserContact = new UserContact();
            }

            mUserContact.contactName = userName;
            mUserContact.contactPhone = userTel;
            mUserContact.certificatesType = (dialogClickPosition + 1) + "";
            mUserContact.certificatesNum = userCardNum;
            setUserCard(mUserContact, dialogClickPosition, userCardNum);
            mUserContact.userId = userService.getLoginUserId();
        } else {
            String userName = mTravelOutName.getText().toString();
            //姓名验证
            if (!RegexUtil.isName(userName) || RegexUtil.isBeforOrEnd(userName)) {
                ToastUtil.showToast(this, getString(R.string.name_error_limit));
                return;
            }

            String lastName = mTravelOutLastName.getText().toString();
            if (TextUtils.isEmpty(lastName.trim())) {
                ToastUtil.showToast(this, "请输入英文姓");
                return;
            }

            String firstName = mTravelOutFirstName.getText().toString();
            if (TextUtils.isEmpty(firstName.trim())) {
                ToastUtil.showToast(this, "请输入英文名");
                return;
            }

            String userCardNum = mTravelOutCardNum.getText().toString();
            //判断输入证件正确性
            if (!TextUtils.isEmpty(userCardNum)) {
                if (!RegexUtil.isPassport(userCardNum)) {
                    ToastUtil.showToast(this, getString(R.string.passport_error));
                    return;
                }
            }

            String userTel = mTravelOutTel.getText().toString();
            if (!RegexUtil.isMobile(userTel)) {
                ToastUtil.showToast(this, getString(R.string.phone_error));
                return;
            }

            if (mUserContact == null) {
                mUserContact = new UserContact();
            }

            mUserContact.contactName = userName;
            mUserContact.contactPhone = userTel;
            mUserContact.certificatesType = TouristType.PassportCertificateType;
            mUserContact.certificatesNum = userCardNum;
            mUserContact.lastName = lastName;
            mUserContact.firstName = firstName;
            setUserCard(mUserContact, 1, userCardNum);
            mUserContact.userId = userService.getLoginUserId();
        }
        setUpdateOrAdd(mUserContact);
    }

    /**
     * 访问网络添加或者修改游客信息
     *
     * @param userContact
     */
    private void setUpdateOrAdd(UserContact userContact) {
        if (userContact == null) {
            return;
        }
        if (type == TYPE_ADD) {//添加
            showLoadingView(getString(R.string.dialog_add_ing));
            mController.doAddOrUpdateVisitorInfo(AddOrUpdateOrderTouristActivity.this,mUserContact);
        } else {//修改
            showLoadingView(getString(R.string.dialog_edit_ing));
            mController.doUpdateVisitorInfo(AddOrUpdateOrderTouristActivity.this,mUserContact);
        }
    }


    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        hideLoadingView();
        switch (msg.what) {
            case ValueConstants.MSG_NEW_ADD_TOURIST_OK://添加联系人或游客成功
                ToastUtil.showToast(this, getString(R.string.add_finish));
                setResult(RESULT_OK);
                finish();
                break;
            case ValueConstants.MSG_NEW_ADD_TOURIST_KO://添加联系人或游客失败
                ToastUtil.showToast(this, getString(R.string.add_error));
                break;
            case ValueConstants.MSG_NEW_EDIT_TOURIST_OK://修改联系人或游客成功
                ToastUtil.showToast(this, getString(R.string.edit_finish));
                setResult(RESULT_OK);
                finish();
                break;
            case ValueConstants.MSG_NEW_EDIT_TOURIST_KO://修改联系人或游客失败
                ToastUtil.showToast(this, getString(R.string.edit_error));
                break;
        }
    }


    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.activity_addorupdate_ordertourist, null);
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


    //控制字数
    private void limitSet(int whichButton) {
        switch (whichButton) {
            case IdentityType.IDCARD_INT://身份证
                mTravelInCardNum.setFilters(new InputFilter[]{StringUtil.limitInputFilter(IDCARD_MAX), StringUtil.getSpaceStopInputFilter()});
                break;
            case IdentityType.PASSPORT_INT://护照
                mTravelInCardNum.setFilters(new InputFilter[]{StringUtil.limitInputFilter(PASSPORT_MAX), StringUtil.getSpaceStopInputFilter()});
                break;
            case IdentityType.ISSOLDIERCARD_INT://军人证
                mTravelInCardNum.setFilters(new InputFilter[]{StringUtil.limitInputFilter(SOLDIERCARD_MAX), StringUtil.getSpaceStopInputFilter()});
                break;
            case IdentityType.ISHKCARD_INT://港澳通行证
                mTravelInCardNum.setFilters(new InputFilter[]{StringUtil.limitInputFilter(PASSPORT_MAX), StringUtil.getSpaceStopInputFilter()});
                break;
        }
        setCardText(whichButton);
    }

    private void setCardText(int postion) {
        if (mUserContact == null) {
            return;
        }
        switch (postion) {
            case IdentityType.IDCARD_INT:
                if (mUserContact.idCert != null && mUserContact.idCert.cert != null) {
                    if (!TextUtils.isEmpty(mUserContact.idCert.cert.cardNO)) {
                        mTravelInCardNum.setText(mUserContact.idCert.cert.cardNO);
                    } else {
                        mTravelInCardNum.setText("");
                    }
                } else {
                    mTravelInCardNum.setText("");
                }
                break;
            case IdentityType.PASSPORT_INT://护照
                if (mUserContact.passportCert != null && mUserContact.passportCert.cert != null) {
                    if (!TextUtils.isEmpty(mUserContact.passportCert.cert.cardNO)) {
                        mTravelInCardNum.setText(mUserContact.passportCert.cert.cardNO);
                    } else {
                        mTravelInCardNum.setText("");
                    }
                } else {
                    mTravelInCardNum.setText("");
                }
                break;
            case IdentityType.ISSOLDIERCARD_INT://军人证
                if (mUserContact.militaryCert != null && mUserContact.militaryCert.cert != null) {
                    if (!TextUtils.isEmpty(mUserContact.militaryCert.cert.cardNO)) {
                        mTravelInCardNum.setText(mUserContact.militaryCert.cert.cardNO);
                    } else {
                        mTravelInCardNum.setText("");
                    }
                } else {
                    mTravelInCardNum.setText("");
                }
                break;
            case IdentityType.ISHKCARD_INT://港澳通行证
                if (mUserContact.hkMacaoCert != null && mUserContact.hkMacaoCert.cert != null) {
                    if (!TextUtils.isEmpty(mUserContact.hkMacaoCert.cert.cardNO)) {
                        mTravelInCardNum.setText(mUserContact.hkMacaoCert.cert.cardNO);
                    } else {
                        mTravelInCardNum.setText("");
                    }
                } else {
                    mTravelInCardNum.setText("");
                }
                break;
        }
    }

    private void setUserCard(UserContact userContact, int postion, String userCardNum) {
        if (userContact == null) {
            return;
        }
        if (TextUtils.isEmpty(userCardNum)) {
            userContact.passportCert = null;
            return;
        }
        switch (postion) {
            case 0:
                IDCertificate mID = new IDCertificate();
                Certificate cert1 = new Certificate();
                cert1.type = (postion + 1) + "";
                cert1.cardNO = userCardNum;
                mID.cert = cert1;
                userContact.idCert = mID;
                break;
            case 1:
                PassportCertificate passportCert = new PassportCertificate();
                Certificate cert2 = new Certificate();
                cert2.type = (postion + 1) + "";
                cert2.cardNO = userCardNum;
                passportCert.cert = cert2;
                userContact.passportCert = passportCert;
                break;
            case 2:
                MilitaryCertificate militaryCert = new MilitaryCertificate();
                Certificate cert3 = new Certificate();
                cert3.type = (postion + 1) + "";
                cert3.cardNO = userCardNum;
                militaryCert.cert = cert3;
                userContact.militaryCert = militaryCert;
                break;
            case 3:
                HkMacaoCertificate hkMacaoCert = new HkMacaoCertificate();
                Certificate cert4 = new Certificate();
                cert4.type = (postion + 1) + "";
                cert4.cardNO = userCardNum;
                hkMacaoCert.cert = cert4;
                userContact.hkMacaoCert = hkMacaoCert;
                break;
        }
    }

}
