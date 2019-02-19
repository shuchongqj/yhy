package com.quanyan.yhy.ui.mine.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.google.gson.Gson;
import com.harwkin.nb.camera.CropBuilder;
import com.harwkin.nb.camera.callback.CameraImageListener;
import com.harwkin.nb.camera.options.CameraOptions;
import com.harwkin.nb.camera.pop.CameraPop;
import com.harwkin.nb.camera.pop.CameraPopListener;
import com.harwkin.nb.camera.type.OpenType;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.mogujie.tt.DB.DBInterface;
import com.mogujie.tt.imservice.event.UserInfoEvent;
import com.mogujie.tt.protobuf.helper.ProtoBuf2JavaBean;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.util.DateUtil;
import com.quanyan.base.util.LocalUtils;
import com.quanyan.base.util.StringUtil;
import com.quanyan.base.view.BaseNavView;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.AnalyDataValue;
import com.quanyan.yhy.common.Gendar;
import com.quanyan.yhy.database.DBManager;
import com.quanyan.yhy.net.NetManager;
import com.quanyan.yhy.net.lsn.OnResponseListener;
import com.quanyan.yhy.ui.base.utils.DialogUtil;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.base.utils.TCEventHelper;
import com.quanyan.yhy.ui.comment.ValueCommentType;
import com.quanyan.yhy.ui.comment.controller.CommentController;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.views.birthdaychoose.JudgeDate;
import com.quanyan.yhy.ui.views.birthdaychoose.ScreenInfo;
import com.quanyan.yhy.ui.views.birthdaychoose.WheelMain;
import com.quanyan.yhy.util.InformationDialogUtils;
import com.yhy.common.beans.album.MediaItem;
import com.yhy.common.beans.im.entity.UserEntity;
import com.yhy.common.beans.net.model.common.PictureTextItemInfo;
import com.yhy.common.beans.net.model.common.address.Address;
import com.yhy.common.beans.net.model.common.person.PictureTextListResult;
import com.yhy.common.beans.net.model.user.UserInfo;
import com.yhy.common.beans.user.User;
import com.yhy.common.constants.IntentConstants;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.eventbus.event.EvBusSportHabit;
import com.yhy.common.utils.CommonUtil;
import com.yhy.common.utils.SPUtils;
import com.yhy.imageloader.ImageLoadManager;
import com.yhy.service.IUserService;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.greenrobot.event.EventBus;


/**
 * Created with Android Studio.
 * Title:UserInfoUpdateActivity
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:和平必胜、正义必胜、人民必胜
 * Author:炎黄子孙
 * Date:15/9/15
 * Time:下午2:07
 * Version 1.0
 */
public class UserInfoUpdateActivity extends BaseActivity implements View.OnClickListener, CameraPopListener,
        CameraImageListener {

    public static final int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime = 0;
    //常用地址
    public static final int REQ_COMMON_ADDRESS_CODE = 102;
    //居住地
    public static final int REQ_LOCATION_ADDRESS_CODE = 103;

    public static final int REQ_TEXT_INPUT = 0x2001;

    public static final int REQ_HOME_PAGE = 104;

    private static final int FOOTBALL = 3;
    private static final int BASKETBALL = 1;
    private static final int BADMINTON = 2;
    private static final int JUST_LOOKING = 0;

    @ViewInject(R.id.rl_user_icon)
    private RelativeLayout rl_user_icon;
    @ViewInject(R.id.rl_user_name)
    private RelativeLayout rl_user_name;
    @ViewInject(R.id.rl_nick_name)
    private RelativeLayout rl_nick_name;
    @ViewInject(R.id.rl_user_gendar)
    private RelativeLayout rl_user_gendar;
    @ViewInject(R.id.rl_user_birthday)
    private RelativeLayout rl_user_birthday;
    @ViewInject(R.id.rl_user_sign)
    private RelativeLayout rl_user_sign;
    @ViewInject(R.id.rl_location)
    private RelativeLayout rl_location;
    @ViewInject(R.id.rl_change_pwd)
    private RelativeLayout rl_change_pwd;
    @ViewInject(R.id.rl_cover)
    private RelativeLayout mRLCover;
    @ViewInject(R.id.rl_user_intro)
    private RelativeLayout mRLIntro;
    @ViewInject(R.id.rl_sports_hobbies)
    private RelativeLayout rl_sports_hobbies;

    @ViewInject(R.id.iv_user_icon)
    private ImageView iv_user_icon;
    @ViewInject(R.id.tv_user_name)
    private TextView tv_user_name;
    @ViewInject(R.id.tv_nick_name)
    private TextView tv_nick_name;
    @ViewInject(R.id.tv_phone)
    private TextView tv_phone;
    @ViewInject(R.id.tv_user_gendar)
    private TextView tv_user_gendar;
    @ViewInject(R.id.tv_user_birthday)
    private TextView tv_user_birthday;
    @ViewInject(R.id.tv_user_sign)
    private TextView tv_user_sign;
    @ViewInject(R.id.iv_cover)
    private ImageView mIVCover;
    @ViewInject(R.id.tv_user_intro)
    private TextView mTVIntro;

    @ViewInject(R.id.tv_cover_default)
    private TextView mTVCoverDefault;

    @ViewInject(R.id.rl_user_qr)
    private RelativeLayout mUserQr;//二维码

    @ViewInject(R.id.tv_hobbies)
    private TextView tv_hobbies;

    @ViewInject(R.id.tv_location)
    private TextView tv_location;
    private User mUserInfo;
    private String mHeadIconPath = null;
    private CameraPop mCameraPop;

    private View birthdayView;
    private Dialog birthDayDialog;
    private LinearLayout timePicker;
    private TextView tv_cancle;
    private TextView tv_confirm;
    private int whichSelect = 0;
    private CommentController mController;
    private List<MediaItem> mPicPathList;
    private User mUpLoadUserInfo;
    private String mSource;

    private boolean isChange = false;
    private int whichSportHobby = 0;

    @Autowired
    IUserService userService;

    public IUserService getUserService() {
        return userService;
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.ac_user_info_modify, null);
    }

    private BaseNavView mBaseNavView;

    @Override
    public View onLoadNavView() {
        mBaseNavView = new BaseNavView(this);
        mBaseNavView.setTitleText(getString(R.string.label_title_update_user_profile));
        return mBaseNavView;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ViewUtils.inject(this);
        mUserInfo = getUserService().getUserInfo(getUserService().getLoginUserId());
        if (mUserInfo == null){
            return;
        }
        mController = new CommentController(this, mHandler);
        //达人主页进入的
        mSource = getIntent().getStringExtra(SPUtils.EXTRA_TYPE);

        updateProfile();
        initViews();
        initCamera();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        initUpLoadUserInfo();
        if (requestCode == IntentConstants.REQUEST_CODE_CHANGE_PWD && resultCode == RESULT_OK) {
            finish();
        } else if (REQ_COMMON_ADDRESS_CODE == requestCode) {
            showAddress(resultCode, data, requestCode);
        } else if (REQ_LOCATION_ADDRESS_CODE == requestCode) {
            showAddressInfo(resultCode, data, requestCode);
            //showAddress(resultCode, data, requestCode);
        } else if (REQ_HOME_PAGE == requestCode) {
            if (resultCode == RESULT_OK) {
                /*SPUtils.setUserHomePage(UserInfoUpdateActivity.this, true);
                mTVIntro.setText(R.string.label_has_filled);*/
                if (data != null) {
                    boolean booleanExtra = data.getBooleanExtra(SPUtils.EXTRA_DATA, true);
                    mUserInfo.setHasMainPage(booleanExtra);
                    mUpLoadUserInfo.setHasMainPage(booleanExtra);
                    doUpdateProfile(mUpLoadUserInfo, null);
                }

            }
        } else if (REQ_TEXT_INPUT == requestCode) {
            if (resultCode == RESULT_OK) {
                final String value = data.getStringExtra(SPUtils.EXTRA_SELECT_CURRENT_VALUE);
                int type = data.getIntExtra(SPUtils.EXTRA_SELECT_TYPE, -1);
                if (type == ValueConstants.SELECT_TYPE_USER_NAME) {
//                    tv_user_name.setText(value);

                    mUpLoadUserInfo.setName(value);
                    doUpdateProfile(mUpLoadUserInfo, new OnResponseListener<Boolean>() {
                        @Override
                        public void onComplete(boolean isOK, Boolean result, int errorCode, String errorMsg) {
                            if (isOK && result) {
                                mUserInfo.setName(value);
                            } else {
                                mUpLoadUserInfo.setName(mUserInfo.getName());
                            }

                        }

                        @Override
                        public void onInternError(int errorCode, String errorMessage) {
                            mUpLoadUserInfo.setName(mUserInfo.getName());
                        }
                    });
                } else if (type == ValueConstants.SELECT_TYPE_NICK_NAME) {
//                    tv_nick_name.setText(value);
//                    mUserInfo.nickname = value;
                    mUpLoadUserInfo.setNickname(value);

                    doUpdateProfile(mUpLoadUserInfo, new OnResponseListener<Boolean>() {
                        @Override
                        public void onComplete(boolean isOK, Boolean result, int errorCode, String errorMsg) {
                            if (isOK && result) {
                                mUserInfo.setNickname(value);
                            } else {
                                mUpLoadUserInfo.setNickname(mUserInfo.getNickname());
                            }

                        }

                        @Override
                        public void onInternError(int errorCode, String errorMessage) {
                            mUpLoadUserInfo.setNickname(mUserInfo.getNickname());
                        }
                    });
                } else if (type == ValueConstants.SELECT_TYPE_SIGN) {
//                    tv_user_sign.setText(value);
//                    mUserInfo.signature = value;
                    mUpLoadUserInfo.setSignature(value);
                    doUpdateProfile(mUpLoadUserInfo, new OnResponseListener<Boolean>() {
                        @Override
                        public void onComplete(boolean isOK, Boolean result, int errorCode, String errorMsg) {
                            if (isOK && result) {
                                mUserInfo.setSignature(value);
                            } else {
                                mUpLoadUserInfo.setSignature(mUserInfo.getSignature());
                            }

                        }

                        @Override
                        public void onInternError(int errorCode, String errorMessage) {
                            mUpLoadUserInfo.setSignature(mUserInfo.getSignature());
                        }
                    });
                }
            }
        } else {
            if (mCameraPop != null) {
                mCameraPop.forResult(requestCode, resultCode, data);
            }
        }

    }

    private void showAddressInfo(int resultCode, Intent data, int reqCode) {
        initUpLoadUserInfo();
        if (resultCode == Activity.RESULT_OK) {
            Address addressCodeName = (Address) data
                    .getSerializableExtra(SPUtils.EXTRA_DATA);
            if (null == mAddressBean) {
                mAddressBean = new Address();
            }
            if (!StringUtil.isEmpty(addressCodeName.provinceCode)) {
                mUserInfo.setProvinceCode(Integer.parseInt(addressCodeName.provinceCode));
                mUpLoadUserInfo.setProvinceCode(Integer.parseInt(addressCodeName.provinceCode));
            }
            if (!StringUtil.isEmpty(addressCodeName.cityCode)) {
                mUserInfo.setCityCode(Integer.parseInt(addressCodeName.cityCode));
                mUpLoadUserInfo.setCityCode(Integer.parseInt(addressCodeName.cityCode));
            }
            if (!StringUtil.isEmpty(addressCodeName.areaCode)) {
                mUserInfo.setAreaCode(Integer.parseInt(addressCodeName.areaCode));
                mUpLoadUserInfo.setAreaCode(Integer.parseInt(addressCodeName.areaCode));
            }
            if (!StringUtil.isEmpty(addressCodeName.province)) {
                mUserInfo.setProvince(addressCodeName.province);
                mUpLoadUserInfo.setProvince(addressCodeName.province);
            }
            if (!StringUtil.isEmpty(addressCodeName.city)) {
                mUserInfo.setCity(addressCodeName.city);
                mUpLoadUserInfo.setCity(addressCodeName.city);
            }

            if (!StringUtil.isEmpty(addressCodeName.area)) {
                mUserInfo.setArea(addressCodeName.area);
                mUpLoadUserInfo.setArea(addressCodeName.area);
            }

            StringBuffer buffer = new StringBuffer();
            showCity(addressCodeName, buffer);

            doUpdateProfile(mUpLoadUserInfo, null);
            tv_location.setText(buffer.toString());
        }
    }

    private void initCamera() {
        if (mCameraPop == null) {
            mCameraPop = new CameraPop(this, this, this);
        }
    }

    Address mAddressBean;

    /**
     * 展示城市
     *
     * @param resultCode
     * @param data
     */
    private void showAddress(int resultCode, Intent data, int reqCode) {
        if (resultCode == Activity.RESULT_OK) {
            initUpLoadUserInfo();
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
            if (reqCode == REQ_COMMON_ADDRESS_CODE) {
                if (!StringUtil.isEmpty(mAddressBean.provinceCode)) {
                    mUserInfo.setProvinceCode(Integer.parseInt(mAddressBean.provinceCode));
                    mUpLoadUserInfo.setProvinceCode(Integer.parseInt(mAddressBean.provinceCode));
                }
                if (!StringUtil.isEmpty(mAddressBean.cityCode)) {
                    mUserInfo.setCityCode(Integer.parseInt(mAddressBean.cityCode));
                    mUpLoadUserInfo.setCityCode(Integer.parseInt(mAddressBean.cityCode));
                }
                if (!StringUtil.isEmpty(mAddressBean.areaCode)) {
                    mUserInfo.setAreaCode(Integer.parseInt(mAddressBean.areaCode));
                    mUpLoadUserInfo.setAreaCode(Integer.parseInt(mAddressBean.areaCode));
                }
                doUpdateProfile(mUpLoadUserInfo, null);

                tv_location.setText(buffer.toString());
            }
        }
    }

    private void showCity(Address addressCodeName, StringBuffer buffer) {
        if (addressCodeName.province.equals(addressCodeName.city)) {
            buffer.append(StringUtil.nullToEmpty(addressCodeName.province));
            return;
        }
        buffer.append(StringUtil.nullToEmpty(addressCodeName.province));
        buffer.append("  ");
        buffer.append(StringUtil.nullToEmpty(addressCodeName.city));
        /*buffer.append("  ");
        buffer.append(StringUtil.nullToEmpty(addressCodeName.area));*/
        /*if (!StringUtil.isEmpty(addressCodeName.city)) {
            buffer.append("  ");
        }
        if (!StringUtil.isEmpty(addressCodeName.area)) {
            buffer.append(StringUtil.nullToEmpty(addressCodeName.area));
        }*/
    }

    private void initViews() {
        //生日对话框
        birthdayView = LayoutInflater.from(this).inflate(R.layout.timepicker, null);
        tv_cancle = (TextView) birthdayView.findViewById(R.id.tv_cancle);
        tv_confirm = (TextView) birthdayView.findViewById(R.id.tv_confirm);
        birthDayDialog = DialogUtil.getMenuDialog(this, birthdayView);
        birthDayDialog.setCanceledOnTouchOutside(true);
        timePicker = (LinearLayout) birthdayView.findViewById(R.id.timePicker1);

        final WheelMain wheelMain = new WheelMain(timePicker);
        final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        ScreenInfo screenInfo = new ScreenInfo(UserInfoUpdateActivity.this);
        wheelMain.screenheight = screenInfo.getHeight();
        final String time = tv_user_birthday.getText().toString();
        final Calendar calendar = Calendar.getInstance();
        if (JudgeDate.isDate(time, "yyyy-MM-dd")) {
            try {
                calendar.setTime(dateFormat.parse(time));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        wheelMain.initDateTimePicker(year, month, day);
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                birthDayDialog.dismiss();
            }
        });

        tv_confirm.setOnClickListener(new View.OnClickListener() {
            // Date curDate = new Date(System.currentTimeMillis());//获取当前时间
            @Override
            public void onClick(View v) {
                //mUserInfo.birthday = wheelMain.getSelectTime();
                initUpLoadUserInfo();
                try {
                    mUserInfo.setBirthday(dateFormat.parse(wheelMain.getTime()).getTime());
                    mUpLoadUserInfo.setBirthday(dateFormat.parse(wheelMain.getTime()).getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (DateUtil.convert2long(DateUtil.getNextDayDate("yyyy-MM-dd"), "yyyy-MM-dd") >= wheelMain.getSelectTime()) {
                    tv_user_birthday.setText(wheelMain.getTime());
                    birthDayDialog.dismiss();
                    doUpdateProfile(mUpLoadUserInfo, null);
                } else {
                    ToastUtil.showToast(UserInfoUpdateActivity.this, getResources().getString(R.string.label_birthdaytoast));
                }

            }
        });


        rl_user_icon.setOnClickListener(this);
        rl_user_name.setOnClickListener(this);
        rl_nick_name.setOnClickListener(this);
        rl_user_birthday.setOnClickListener(this);
        rl_user_gendar.setOnClickListener(this);
//        rl_constellation.setOnClickListener(this);
        rl_user_sign.setOnClickListener(this);
        rl_location.setOnClickListener(this);
        rl_change_pwd.setOnClickListener(this);
        mRLCover.setOnClickListener(this);
        mRLIntro.setOnClickListener(this);
        mUserQr.setOnClickListener(this);
        rl_sports_hobbies.setOnClickListener(this);

        mBaseNavView.setLeftClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!StringUtil.isEmpty(mSource) && ValueCommentType.PIC_AND_TEXT_EXPERTHOME.equals(mSource)) {
                    doCancleBack();
                }
                finish();
            }
        });
    }

    private void doCancleBack() {
        if (isChange) {
            setResult(RESULT_OK);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            if (!StringUtil.isEmpty(mSource) && ValueCommentType.PIC_AND_TEXT_EXPERTHOME.equals(mSource)) {
                doCancleBack();
            }
            finish();
        }
        return true;
    }

    private void updateProfile() {
//        if(mUpLoadUserInfo != null){
//            mUpLoadUserInfo = null;
//        }
        //TODO ?
//        mUserInfo.setHasMainPage(SPUtils.getUserHomePage(this));
//        mUserInfo.setFrontCover(SPUtils.getUserCover(this));
        if (mUserInfo == null) {
            return;
        }
        if (!StringUtil.isEmpty(mUserInfo.getNickname())) {
            tv_nick_name.setText(mUserInfo.getNickname());
        } else {
            tv_nick_name.setText(R.string.label_unknown_select);
        }

        if (!StringUtil.isEmpty(mUserInfo.getName())) {
            tv_user_name.setText(mUserInfo.getName());
        } else {
            tv_user_name.setText(R.string.label_no_select);
        }

        if (!StringUtil.isEmpty(SPUtils.getMobilePhone(this))) {
            tv_phone.setText(SPUtils.getMobilePhone(this));
        } else {
            tv_phone.setText(R.string.label_unknown_select);
        }

        if (Gendar.STR_MALE.equals(mUserInfo.getGender())) {
            tv_user_gendar.setText(R.string.label_man);
        } else if (Gendar.STR_FEMALE.equals(mUserInfo.getGender())) {
            tv_user_gendar.setText(R.string.label_women);
        }

        if (mUserInfo.getSportHobby() == BASKETBALL) {
            tv_hobbies.setText(getResources().getString(R.string.label_sport_basketball));
        } else if (mUserInfo.getSportHobby() == BADMINTON) {
            tv_hobbies.setText(getResources().getString(R.string.label_sport_badminton));
        } else if (mUserInfo.getSportHobby() == FOOTBALL) {
            tv_hobbies.setText(getResources().getString(R.string.label_football));
        } else if (mUserInfo.getSportHobby() == JUST_LOOKING) {
            tv_hobbies.setText(getResources().getString(R.string.label_just_looking));
        }

        if (!StringUtil.isEmpty(mUserInfo.getAvatar())) {
//            BaseImgView.loadimg(iv_user_icon,
//                    mUserInfo.avatar,
//                    R.mipmap.icon_default_avatar,
//                    R.mipmap.icon_default_avatar,
//                    R.mipmap.icon_default_avatar,
//                    ImageScaleType.EXACTLY,
//                    (int) getResources().getDimension(R.dimen.healthcircle_padding30),
//                    (int) getResources().getDimension(R.dimen.healthcircle_padding30),
//                    180);
            ImageLoadManager.loadCircleImage(CommonUtil.getImageFullUrl(mUserInfo.getAvatar()), R.mipmap.icon_default_avatar, (int) getResources().getDimension(R.dimen.healthcircle_padding30), (int) getResources().getDimension(R.dimen.healthcircle_padding30), iv_user_icon);

        } else {
            iv_user_icon.setImageDrawable(getResources().getDrawable(R.mipmap.icon_default_avatar));
        }
        //用户封面
        if (!StringUtil.isEmpty(SPUtils.getUserCover(this))) {
            mIVCover.setVisibility(View.VISIBLE);
            mTVCoverDefault.setVisibility(View.GONE);
//            BaseImgView.loadimg(mIVCover,
//                    SPUtils.getUserCover(this),
//                    R.mipmap.icon_default_215_215,
//                    R.mipmap.icon_default_215_215,
//                    R.mipmap.icon_default_215_215,
//                    ImageScaleType.EXACTLY,
//                    (int) getResources().getDimension(R.dimen.dd_dimen_75px),
//                    (int) getResources().getDimension(R.dimen.dd_dimen_75px),
//                    0);

            ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(SPUtils.getUserCover(this)), R.mipmap.icon_default_215_215, (int) getResources().getDimension(R.dimen.dd_dimen_75px), (int) getResources().getDimension(R.dimen.dd_dimen_75px), mIVCover);

        } else {
            //mIVCover.setImageDrawable(getResources().getDrawable(R.mipmap.icon_default_215_215));
            mIVCover.setBackgroundResource(R.color.transparent);
            mIVCover.setVisibility(View.GONE);
            mTVCoverDefault.setVisibility(View.VISIBLE);
        }

        if (SPUtils.getUserHomePage(this)) {
            mTVIntro.setText(R.string.label_has_filled);
        } else {
            mTVIntro.setText(R.string.label_no_select);
        }

        if (mUserInfo.getBirthday() != 0) {
            tv_user_birthday.setText(DateUtil.getDisplayDate(mUserInfo.getBirthday(), "yyyy-MM-dd"));
        } else {
            tv_user_birthday.setText(R.string.label_select_birthday);
        }

        if (mUserInfo.getSignature() != null) {
            tv_user_sign.setText(mUserInfo.getSignature());
        } else {
            tv_user_sign.setText(R.string.label_no_select);
        }

        if (mUserInfo.getProvince() != null) {
            if (mUserInfo.getProvince().equals(mUserInfo.getCity())) {
                tv_location.setText(mUserInfo.getCity());
                return;
            }
            tv_location.setText(mUserInfo.getProvince() + " " + mUserInfo.getCity());
        } else {
            tv_location.setText(R.string.label_pleace_select);
        }

//        if(mUserInfo.provinceCode != null){
//            tv_location.setText(mUserInfo.provinceCode);
//        }else{
//            tv_location.setText(R.string.label_unknown_select);
//        }
    }

    public static void gotoUserInfoUpdatePage(Context context) {
        context.startActivity(new Intent(context, UserInfoUpdateActivity.class));
    }

    public static void gotoUserInfoUpdatePage(Activity context, String source, int reqCode) {
        Intent intent = new Intent(context, UserInfoUpdateActivity.class);
        intent.putExtra(SPUtils.EXTRA_TYPE, source);
        context.startActivityForResult(intent, reqCode);
    }

    @Override
    public void onClick(View v) {
        //防止过快点击
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            switch (v.getId()) {
                case R.id.rl_user_icon:
                    doPickHeadIcon();
                    break;
                case R.id.rl_user_name:
                    doPickUserName();
                    break;
                case R.id.rl_nick_name:
                    doPickNickName();
                    break;
                case R.id.rl_user_birthday:
                    doPickBirthday();
                    break;
                case R.id.rl_user_gendar:
                    doPickUserGendar();
                    break;
                case R.id.rl_user_sign:
                    doPickUserSign();
                    break;
                case R.id.rl_location:
                    doPickUserLocation();
                    break;
                case R.id.rl_change_pwd:
                    NavUtils.goChangePasswordActivity(this, IntentConstants.REQUEST_CODE_CHANGE_PWD);
                    break;
                case R.id.rl_cover:
                    //设置封面
                    doPickCover();
                    break;
                case R.id.rl_user_intro:
                    //设置主页
                    doPickUserIntro();
                    break;
                case R.id.rl_sports_hobbies:
                    //设置运动爱好
                    doPickUserSportHobby();
                    break;
                case R.id.rl_user_qr:
                    //我的二维码
                    TCEventHelper.onEvent(UserInfoUpdateActivity.this, AnalyDataValue.MINEINFOPAGE_MINE_QR_CLICK);
                    NavUtils.gotoMineQrActivity(UserInfoUpdateActivity.this);
                    break;
            }
        }
    }

    //封面
    private final static int COVER = 1;
    //头像
    private final static int HEAD_ICON = 2;
    private int mPictureOp = HEAD_ICON;

    /**
     * 设置封面
     */
    private void doPickCover() {
        mPictureOp = COVER;
        mCameraPop.showMenu(mIVCover);
    }

    /**
     * 设置主页
     */
    private void doPickUserIntro() {
        String value = mTVIntro.getText().toString().trim();
        if (value.equals(getString(R.string.label_has_filled))) {
            //查看主页
            doSelectHomePageNet();
        } else {
            //新增主页,调到全新的图文介绍界面
            NavUtils.gotoPictureAndTextActivity(this, ValueCommentType.PIC_AND_TEXT_EXPERTHOME, null, null, REQ_HOME_PAGE, -1);
        }
    }

    //查询主页网络访问
    private void doSelectHomePageNet() {
        mController.doGetPictureTextInfo(this, userService.getLoginUserId(), ValueCommentType.PIC_AND_TEXT_EXPERTHOME);
    }

    /**
     * 选择生日
     */
    private void doPickBirthday() {
        birthDayDialog.show();
    }

    /**
     * 选择头像
     */
    private void doPickHeadIcon() {
        mPictureOp = HEAD_ICON;
        mCameraPop.showMenu(iv_user_icon);
    }

    /**
     * 选择姓名
     */
    private void doPickUserName() {
        MyTextActivity.gotoSeclectText(this, getString(R.string.label_user_name),
                ValueConstants.SELECT_TYPE_USER_NAME,
                mUserInfo.getName());
    }

    /**
     * 选择昵称
     */
    private void doPickNickName() {
        MyTextActivity.gotoSeclectText(this, getString(R.string.label_nick_name), ValueConstants.SELECT_TYPE_NICK_NAME, tv_nick_name.getText().toString());
    }

    /**
     * 选择性别
     */
    private void doPickUserGendar() {
        initUpLoadUserInfo();
        whichSelect = getSelectPostion(mUserInfo.getGender());
        InformationDialogUtils.showAlert(UserInfoUpdateActivity.this,
                getResources().getString(R.string.label_gendar),
                getResources().getStringArray(R.array.gendar),
                null,
                new InformationDialogUtils.OnAlertSelectId() {
                    @Override
                    public void onClick(int whichButton) {
                        tv_user_gendar.setText(getResources().getStringArray(R.array.gendar)[whichButton]);
                        whichSelect = whichButton;
                        if (whichButton == 0) {
                            mUserInfo.setGender(Gendar.STR_MALE);
                            mUpLoadUserInfo.setGender(Gendar.STR_MALE);
                        } else if (whichButton == 1) {
                            mUserInfo.setGender(Gendar.STR_FEMALE);
                            mUpLoadUserInfo.setGender(Gendar.STR_FEMALE);
                        } else {
                            mUserInfo.setGender(Gendar.STR_NONE);
                            mUpLoadUserInfo.setGender(Gendar.STR_NONE);
                        }
                        doUpdateProfile(mUpLoadUserInfo, null);
                    }
                }, whichSelect);


    }

    /**
     * 选择运动爱好
     */
    private void doPickUserSportHobby() {
        initUpLoadUserInfo();
        if (mUserInfo != null) {
            whichSportHobby = getSelectHabitPosition(mUserInfo.getSportHobby());
        }
        InformationDialogUtils.showAlert(UserInfoUpdateActivity.this,
                getResources().getString(R.string.label_sport_hobby),
                getResources().getStringArray(R.array.sport_habit),
                null,
                new InformationDialogUtils.OnAlertSelectId() {
                    @Override
                    public void onClick(int whichButton) {
                        tv_hobbies.setText(getResources().getStringArray(R.array.sport_habit)[whichSportHobby]);
                        whichSportHobby = whichButton;
                        switch (whichButton) {
                            case 0:
                                mUserInfo.setSportHobby(FOOTBALL);
                                mUpLoadUserInfo.setSportHobby(FOOTBALL);
                                break;
                            case 1:
                                mUserInfo.setSportHobby(BASKETBALL);
                                mUpLoadUserInfo.setSportHobby(BASKETBALL);
                                break;
                            case 2:
                                mUserInfo.setSportHobby(BADMINTON);
                                mUpLoadUserInfo.setSportHobby(BADMINTON);
                                break;
                            case 3:
                                mUserInfo.setSportHobby(JUST_LOOKING);
                                mUpLoadUserInfo.setSportHobby(JUST_LOOKING);
                                break;
                        }
                        doUpdateProfile(mUpLoadUserInfo, null, true);
                    }
                }, whichSportHobby + 1);

    }

    /**
     * 得到dialog默认选择的位置
     *
     * @param sportHobby
     * @return
     */
    private int getSelectHabitPosition(int sportHobby) {
        int selectPosition = 0;
        switch (sportHobby) {
            case FOOTBALL:
                selectPosition = 0;
                break;
            case BADMINTON:
                selectPosition = 2;
                break;
            case BASKETBALL:
                selectPosition = 1;
                break;
            case JUST_LOOKING:
                selectPosition = 3;
                break;
        }
        return selectPosition;
    }

    //初始化更新的对象
    private void initUpLoadUserInfo() {
        try {
            if (mUpLoadUserInfo == null) {
                mUpLoadUserInfo = new Gson().fromJson(new Gson().toJson(mUserInfo), User.class);
                //mUpLoadUserInfo.userId = mUserInfo.userId;
            }
        } catch (Exception e) {


        }

    }


    /**
     * 选择签名
     */
    private void doPickUserSign() {
        MyTextActivity.gotoSeclectText(this, getString(R.string.label_travel_declaration), ValueConstants.SELECT_TYPE_SIGN, mUserInfo.getSignature());
    }

    /**
     * 选择居住地
     */
    private void doPickUserLocation() {
        Address address = new Address();
//        address.cityCode = mUserInfo.locationCity;
        NavUtils.gotoAreaSelect(this, address, REQ_LOCATION_ADDRESS_CODE);
    }


    @Override
    public void onCamreaClick(CameraOptions options) {
        if (LocalUtils.isAlertMaxStorage()) {
            ToastUtil.showToast(this, getString(R.string.label_toast_sdcard_unavailable));
            return;
        }
        if (mPictureOp == HEAD_ICON) {
            options.setOpenType(OpenType.OPEN_CAMERA_CROP)
                    .setCropBuilder(new CropBuilder(1, 1, 600, 600));
        } else if (mPictureOp == COVER) {
//            options.setOpenType(OpenType.OPEN_CAMERA);
            options.setOpenType(OpenType.OPEN_CAMERA_CROP)
                    .setCropBuilder(new CropBuilder(1, 1, 750, 420));
        }
        managerProcess();
    }

    @Override
    public void onPickClick(CameraOptions options) {
        if (mPictureOp == HEAD_ICON) {
            options.setOpenType(OpenType.OPEN_GALLERY_CROP)
                    .setCropBuilder(new CropBuilder(1, 1, 600, 600));
        } else if (mPictureOp == COVER) {
//            options.setOpenType(OpenType.OPEN_GALLERY);
            options.setOpenType(OpenType.OPEN_GALLERY_CROP)
                    .setCropBuilder(new CropBuilder(1, 1, 750, 420));
        }

        managerProcess();
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

    /**
     * 执行照片处理
     */
    private void managerProcess() {
        mCameraPop.process();
        if (null != mCameraPop) {
            mCameraPop.dismiss();
        }
    }

    @Override
    public void onSelectedAsy(String imgPath) {
        UpImagefromCut(imgPath);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        hideLoadingView();
        switch (msg.what) {
            case ValueConstants.MSG_MINE_HOME_PAGE_SELECT_OK:
                PictureTextListResult value = (PictureTextListResult) msg.obj;
                if (value != null) {
                    handleData(value);
                }
                break;
            case ValueConstants.MSG_MINE_HOME_PAGE_SELECT_KO:
                ToastUtil.showToast(this, StringUtil.handlerErrorCode(getApplicationContext(), msg.arg1));
                break;
        }
    }

    private void handleData(PictureTextListResult value) {
        List<PictureTextItemInfo> pictureTextList = value.pictureTextList;
        if (pictureTextList != null && pictureTextList.size() > 0) {
            if (mPicPathList == null) {
                mPicPathList = new ArrayList<>();
            }
            mPicPathList.clear();
            for (int i = 0; i < pictureTextList.size(); i++) {
                if (pictureTextList.get(i).type.equals(ValueCommentType.PIC_TYPE)) {
                    MediaItem mediaItem = new MediaItem();
                    mediaItem.mediaPath = pictureTextList.get(i).value;
                    mediaItem.isNetImage = true;
                    mPicPathList.add(mediaItem);
                }
            }

            NavUtils.gotoPictureAndTextActivity(this, ValueCommentType.PIC_AND_TEXT_EXPERTHOME, pictureTextList, mPicPathList, REQ_HOME_PAGE, value.id);
        }

    }

    /**
     * 更新个人资料
     */
    private void doUpdateProfile(final User userInfo, final OnResponseListener<Boolean> onResponseListener) {
        doUpdateProfile(userInfo, onResponseListener, false);
    }

    private void doUpdateProfile(final User userInfo, final OnResponseListener<Boolean> onResponseListener, boolean refreshHome) {
        showLoadingView(getString(R.string.dlg_msg_saving));
        NetManager.getInstance(this).doUpdateUserProfile(userInfo,
                new OnResponseListener<Boolean>() {
                    @Override
                    public void onComplete(boolean isOK, Boolean result, int errorCode, String errorMsg) {
                        hideLoadingView();
                        if (isOK && result) {
                            //mUserInfo = userInfo;
                            isChange = true;
                            getUserService().saveUserInfo(mUpLoadUserInfo);
                            //获取登录用户信息后存储到im数据库

                            ArrayList<UserEntity> entitys = new ArrayList<UserEntity>();
                            UserEntity userEntity = ProtoBuf2JavaBean.getUserEntity(mUserInfo);
                            entitys.add(userEntity);
                            DBInterface.instance().batchInsertOrUpdateUser(entitys);
                            EventBus.getDefault().post(UserInfoEvent.USER_LOGIN_INFO_UPDATE);
                            updateProfile();

                            // 运动兴趣改变后 刷新首页数据
                            EventBus.getDefault().post(new EvBusSportHabit());
                        } else {
                            isChange = false;
                            ToastUtil.showToast(UserInfoUpdateActivity.this,
                                    StringUtil.handlerErrorCode(UserInfoUpdateActivity.this, errorCode));
                        }
                        if (onResponseListener != null) {
                            onResponseListener.onComplete(isOK, result, errorCode, errorMsg);
                        }

                    }

                    @Override
                    public void onInternError(int errorCode, String errorMessage) {

                        if (onResponseListener != null) {
                            onResponseListener.onInternError(errorCode, errorMessage);
                        }

                        isChange = false;
                        hideLoadingView();
                        ToastUtil.showToast(UserInfoUpdateActivity.this, errorMessage);
                    }
                });
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
        if (mPictureOp == HEAD_ICON) {
            showLoadingView(getString(R.string.dlg_msg_head_icon_uploading));
        } else {
            showLoadingView(getString(R.string.dlg_msg_cover_uploading));
        }
        NetManager.getInstance(this).doUploadImages(files,
                new OnResponseListener<List<String>>() {

                    @Override
                    public void onInternError(int errorCode, String errorMessage) {
                        hideLoadingView();
                        ToastUtil.showToast(UserInfoUpdateActivity.this, errorMessage);
                    }

                    @Override
                    public void onComplete(boolean isOK, List<String> result, int errorCode, String errorMsg) {
                        hideLoadingView();
                        if (isOK) {
                            if (result != null && result.size() > 0) {
                                initUpLoadUserInfo();
                                if (mPictureOp == HEAD_ICON) {
                                    mHeadIconPath = String.valueOf(result.get(0));
                                    mUserInfo.setAvatar(mHeadIconPath);
                                    mUpLoadUserInfo.setAvatar(mHeadIconPath);
                                } else if (mPictureOp == COVER) {
                                    mUserInfo.setFrontCover(String.valueOf(result.get(0)));
                                    mUpLoadUserInfo.setFrontCover(String.valueOf(result.get(0)));
                                }
                                doUpdateProfile(mUpLoadUserInfo, null);
                            } else {
                                if (mPictureOp == HEAD_ICON) {
                                    ToastUtil.showToast(UserInfoUpdateActivity.this, getString(R.string.toast_upload_head_icon_failed));
                                } else if (mPictureOp == COVER) {
                                    ToastUtil.showToast(UserInfoUpdateActivity.this, getString(R.string.toast_upload_cover_failed));
                                }
                            }
                        } else {
                            ToastUtil.showToast(UserInfoUpdateActivity.this, errorMsg);
                        }
                    }
                });
    }

    private int getSelectPostion(String gender) {
        if (TextUtils.isEmpty(gender)) {
            return -1;
        } else if (gender.equals(Gendar.STR_MALE)) {//男
            return 1;
        } else if (gender.equals(Gendar.STR_FEMALE)) {//女
            return 2;
        }
        return -1;
    }

}
