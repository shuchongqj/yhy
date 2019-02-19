package com.videolibrary.puhser.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.util.DialogBuilder;
import com.quanyan.base.util.ScreenSize;
import com.quanyan.base.util.StringUtil;
import com.quanyan.base.view.BaseNavView;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.AnalyDataValue;
import com.quanyan.yhy.ui.base.utils.DialogUtil;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.base.utils.TCEventHelper;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.view.Wheel3DView;
import com.smart.sdk.api.resp.Api_SNSCENTER_SnsHasNoEndBatchResult;
import com.videolibrary.controller.LiveController;
import com.videolibrary.puhser.view.DefinitionDialogView;
import com.videolibrary.utils.IntentUtil;
import com.yhy.common.beans.net.model.msg.LiveCategoryResult;
import com.yhy.common.beans.net.model.msg.LiveRoomResult;
import com.yhy.common.utils.CommonUtil;
import com.yhy.common.utils.SPUtils;
import com.yhy.imageloader.ImageLoadManager;
import com.yhy.location.LocationManager;
import com.yhy.service.IUserService;
import com.yixia.camera.util.Log;

import java.util.ArrayList;

import static com.videolibrary.controller.LiveController.MSG_GET_HASH_NO_END_LIVE_RESULT_ERROR;
import static com.videolibrary.controller.LiveController.MSG_GET_HASH_NO_END_LIVE_RESULT_OK;
import static com.videolibrary.controller.LiveController.MSG_LIVE_ROOM_INFO_ERROR;
import static com.videolibrary.controller.LiveController.MSG_LIVE_ROOM_INFO_OK;


/**
 * Created with Android Studio.
 * Title:PublishLiveActivity
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:邓明佳
 * Date:2016/8/25
 * Time:10:23
 * Version 1.0
 */
public class PublishLiveActivity extends BaseActivity implements View.OnClickListener, DefinitionDialogView.OnDefinitionSelectedListener {

    private static final int ADD_LIVE_LABEL = 2;//添加标签，请求码
    private static final int HORIZONTAL_PUBLISH = 0;//横屏直播
    private static final int VERTICAL_PUBLISH = 1;//竖屏直播
    private static final int REQUEST_CATEGORY_CODE = 3;
    private static final int REQUEST_CATEGORY_LIVE_HOR = 4;
    private static final int REQUEST_CATEGORY_LIVE_VER = 5;

    BaseNavView mNavView;
    private ImageView mHeadView;
    private TextView mFans;
    private TextView mHome;
    private ImageView mDeleteLocation;
    private EditText mTitle;
    private TextView mLocation;
    private LinearLayout mHDParent;
    private TextView mHDTextView;

    private String cityName = LocationManager.getInstance().getStorage().getGprs_cityName();
    private String cityCode = LocationManager.getInstance().getStorage().getGprs_cityCode();
    private TextView mTextViewCategory;
    private RelativeLayout publish_activity;
    String notice;
    String categoryName;
    long categoryCode;
    DefinitionDialogView definitionView;
    int defintionIndex = 1;
    public static final int REQUEST_SETTING_CODE = 0x1002;
    ArrayList<LiveCategoryResult> categoryResults;

    private Dialog mDateDialog;

    private boolean isVerticalClickLive  = false;
    private boolean isHorizontalClickLive  = false;

    @Autowired
    IUserService userService;

    public IUserService getUserService() {
        return userService;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

        mHeadView = (ImageView) findViewById(R.id.ac_publish_head_view);
        mFans = (TextView) findViewById(R.id.ac_publish_fans);
        mHome = (TextView) findViewById(R.id.ac_publish_home_number);
        mDeleteLocation = (ImageView) findViewById(R.id.ac_publish_delete_location);
        mLocation = (TextView) findViewById(R.id.ac_publish_location);
        mHDParent = (LinearLayout) findViewById(R.id.ac_publish_hd_parent);
        mHDTextView = (TextView) findViewById(R.id.ac_publish_hd_text);
        mTitle = (EditText) findViewById(R.id.ac_publish_title_edit);
        publish_activity = (RelativeLayout) findViewById(R.id.activity_publish);
        mTextViewCategory = findViewById(R.id.ac_publish_category);
        //mNavView.setRightImg(R.mipmap.ic_live_settings);
        mNavView.setRightText("设置");
        mNavView.setRightTextColor(R.color.white);
        mNavView.setLeftText(getString(R.string.cancel));
        mNavView.setLeftTextColor(R.color.white);
        mNavView.setBottomLayoutVisiable(false);
        definitionView = new DefinitionDialogView(this);
        definitionView.setOnDefinitionSelectedListener(this);
        changePublishButton();
        initListener();
        initData();
        getRecentLive();
    }

    /**
     * 查询是否最近有未结束的直播
     */
    private void getRecentLive() {
        LiveController liveController = LiveController.getInstance();
        liveController.getHasNoEndBatch(this,mHandler, userService.getLoginUserId());
    }

    /**
     * 动态适配两个发起直播button的布局
     */
    private void changePublishButton() {

        WindowManager manager = this.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int screenWidth = outMetrics.widthPixels;

        int layoutWidth = (int) (screenWidth * (1 - 16f * 3f / 416f) / 2);
        int margin = (int) (screenWidth * (16f / 416f));

        //动态创建一个相对布局
        RelativeLayout relaLayoutLeft = new RelativeLayout(this);
        relaLayoutLeft.setBackgroundColor(getResources().getColor(R.color.publish_bg));
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(layoutWidth, layoutWidth);
        layoutParams.setMargins(margin, 0, 0, margin);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        relaLayoutLeft.setLayoutParams(layoutParams);
        relaLayoutLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isHorizontalClickLive = true;
                isVerticalClickLive = false;
                if ("直播分类".equals(mTextViewCategory.getText())) {
                    if (result == null || result.list == null || result.list.size() == 0) return;
                    initSelectLiveCategorization();
                    mDateDialog.show();
                } else {
                    String title = mTitle.getText().toString();
                    IntentUtil.startPushStreamHorizontalActivity(PublishLiveActivity.this, title, cityName,
                            cityCode, (int) categoryCode, result.roomId, HORIZONTAL_PUBLISH, notice, 0);
                    finish();
                }
            }
        });
        publish_activity.addView(relaLayoutLeft);

        ImageView leftButton = new ImageView(this);
        relaLayoutLeft.addView(leftButton);
        leftButton.setImageResource(R.mipmap.hengping);
        RelativeLayout.LayoutParams layoutParams1 = (RelativeLayout.LayoutParams) leftButton.getLayoutParams();
        layoutParams1.width = (int) (layoutWidth * (7f / 19f));
        layoutParams1.height = (int) (layoutWidth * (7f / 19f));
        layoutParams1.addRule(RelativeLayout.CENTER_HORIZONTAL);
        layoutParams1.setMargins(0, (int) (42f / 190f * layoutWidth), 0, 0);
        leftButton.setLayoutParams(layoutParams1);

        TextView leftText = new TextView(this);
        relaLayoutLeft.addView(leftText);
        leftText.setText("横屏直播");
        leftText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15); //22SP
        leftText.setTextColor(Color.WHITE);
        RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) leftText.getLayoutParams();
        layoutParams2.addRule(RelativeLayout.CENTER_HORIZONTAL);
        layoutParams2.setMargins(0, (int) (layoutWidth * (127f / 190f)), 0, 0);
        leftText.setLayoutParams(layoutParams2);

        //动态创建一个相对布局
        RelativeLayout relaLayoutRight = new RelativeLayout(this);
        relaLayoutRight.setBackgroundColor(getResources().getColor(R.color.publish_bg));
        RelativeLayout.LayoutParams layoutParamsRight = new RelativeLayout.LayoutParams(layoutWidth, layoutWidth);
        layoutParamsRight.setMargins(0, 0, margin, margin);
        layoutParamsRight.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParamsRight.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        relaLayoutRight.setLayoutParams(layoutParamsRight);
        relaLayoutRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isVerticalClickLive = true;
                isHorizontalClickLive = false;
                if ("直播分类".equals(mTextViewCategory.getText())) {
                    if (result == null || result.list == null || result.list.size() == 0) return;
                    initSelectLiveCategorization();
                    mDateDialog.show();
                } else {
                    String title = mTitle.getText().toString();
                    IntentUtil.startPushStreamActivity(PublishLiveActivity.this, title,LocationManager.getInstance().getStorage().getGprs_cityName(),
                            LocationManager.getInstance().getStorage().getGprs_cityCode(), (int) categoryCode, result.roomId, VERTICAL_PUBLISH, notice, 0);
                    finish();
                }
            }
        });
        publish_activity.addView(relaLayoutRight);

        ImageView rightButton = new ImageView(this);
        relaLayoutRight.addView(rightButton);
        rightButton.setImageResource(R.mipmap.shuping);
        RelativeLayout.LayoutParams layoutParams3 = (RelativeLayout.LayoutParams) rightButton.getLayoutParams();
        layoutParams3.width = (int) (layoutWidth * (7f / 19f));
        layoutParams3.height = (int) (layoutWidth * (7f / 19f));
        layoutParams3.addRule(RelativeLayout.CENTER_HORIZONTAL);
        layoutParams3.setMargins(0, (int) (42f / 190f * layoutWidth), 0, 0);
        rightButton.setLayoutParams(layoutParams3);

        TextView rightText = new TextView(this);
        relaLayoutRight.addView(rightText);
        rightText.setText("竖屏直播");
        rightText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15); //22SP
        rightText.setTextColor(Color.WHITE);
        RelativeLayout.LayoutParams layoutParams4 = (RelativeLayout.LayoutParams) rightText.getLayoutParams();
        layoutParams4.addRule(RelativeLayout.CENTER_HORIZONTAL);
        layoutParams4.setMargins(0, (int) (layoutWidth * (127f / 190f)), 0, 0);
        rightText.setLayoutParams(layoutParams4);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initData() {
//        notice = getString(R.string.user_live_notice,SPUtils.getNickName(this));
        setTitleBarBackground(getResources().getColor(R.color.black));
        defintionIndex = SPUtils.getInt(this, SPUtils.EXTRA_LIVE_DEFINTION_INDEX, 1);
        mHDTextView.setText(getDefintionString(defintionIndex));
        if (!StringUtil.isEmpty(SPUtils.getUserIcon(this))) {
            ImageLoadManager.loadCircleImage(CommonUtil.getImageFullUrl(SPUtils.getUserIcon(this)), R.mipmap.icon_default_avatar, 136, 136, mHeadView);
        } else {
            mHeadView.setImageResource(R.mipmap.icon_default_avatar);
        }
        mFans.setText(getString(R.string.publish_live_fans_count, SPUtils.getMyFans(this)));
        showLoadingView(null);

        // test by shenwenjie
        LiveController.getInstance().getLiveRoomInfo(this, userService.getLoginUserId(), mHandler);
        Log.i("shenwenjie", "uid:" + userService.getLoginUserId());//1153

        mLocation.setText(LocationManager.getInstance().getStorage().getGprs_cityName());
        cityCode = LocationManager.getInstance().getStorage().getGprs_cityCode();

//        LiveController.getInstance().getLiveRoomInfo(this, 1151, mHandler);

    }


    private String getDefintionString(int index) {
        if (index == 0) {
            return "标清直播";
        } else if (index == 1) {
            return "高清直播";
        } else if (index == 2) {
            return "超清直播";
        }
        return null;
    }

    View.OnClickListener mSettinsOnclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (result == null || result.list == null || result.list.size() == 0) return;

            ArrayList<LiveCategoryResult> arrayList = new ArrayList<>(result.list);
            IntentUtil.startLiveSettingsActivity(PublishLiveActivity.this, notice, categoryName, categoryCode, result.roomId, arrayList, REQUEST_SETTING_CODE);
        }
    };

    private void initListener() {
        mLocation.setOnClickListener(this);
        mDeleteLocation.setOnClickListener(this);
        findViewById(R.id.ac_publish_add_topic).setOnClickListener(this);
        findViewById(R.id.ac_publish_location_layout).setOnClickListener(this);
        findViewById(R.id.ac_publish_category).setOnClickListener(this);
        /*findViewById(R.id.ac_publish_add_topic).setOnClickListener(this);
        findViewById(R.id.ac_publish_location_layout).setOnClickListener(this);
        findViewById(R.id.iv_vertical_publish).setOnClickListener(this);
        findViewById(R.id.iv_horizontal_publish).setOnClickListener(this);*/
        mHDParent.setOnClickListener(this);
        //mNavView.setRightImgClick(mSettinsOnclickListener);
        mNavView.setRightTextClick(mSettinsOnclickListener);
        mTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    LiveRoomResult result;

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        int what = msg.what;
        hideLoadingView();
        switch (what){
            case MSG_GET_HASH_NO_END_LIVE_RESULT_OK:
                Api_SNSCENTER_SnsHasNoEndBatchResult obj = (Api_SNSCENTER_SnsHasNoEndBatchResult) msg.obj;
                if (obj == null){
                    return;
                }
                if (obj.hasNoEndBatch){
                    showRecentLiveDialog(obj);
                }
                break;
            case MSG_GET_HASH_NO_END_LIVE_RESULT_ERROR:
                //获取最近是否有未完成的直播失败
                break;
            case MSG_LIVE_ROOM_INFO_OK:
                if (msg.obj == null) {
                    ToastUtil.showToast(this, "获取直播间信息失败");
                    finish();
                    return;
                } else {
                    result = (LiveRoomResult) msg.obj;
                    if (result.liveRoomStatus == 3) {
                        ToastUtil.showToast(this, "直播间已关闭");
                        finish();
                        return;
                    }
                    notice = result.roomNotice;
                    categoryName = result.liveCategoryName;
                    categoryResults = new ArrayList<>(result.list);
//                categoryCode = result.liveCategoryCode;
                    mHome.setText(getString(R.string.publish_live_room_id, result.roomId));
                    mTitle.setText(result.liveTitle);
                }
                break;
            case MSG_LIVE_ROOM_INFO_ERROR:
                ToastUtil.showToast(this, StringUtil.handlerErrorCode(this, msg.arg1));
                finish();
                break;

        }
    }

    private Dialog mDialog;
    /**
     * 是否继续未结束的直播
     */
    private void showRecentLiveDialog(final Api_SNSCENTER_SnsHasNoEndBatchResult result) {
        if (mDialog == null) {
            mDialog = DialogUtil.showMessageDialog(this, "您有未结束的直播", "是否继续直播?",
                    getString(R.string.continue_live), getString(R.string.cancel), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            mDialog.dismiss();

                            if (!TextUtils.isEmpty(result.liveScreenType)){
                                if (("VERTICAL".equals(result.liveScreenType))){
                                    IntentUtil.startPushStreamActivity(PublishLiveActivity.this, null,null,
                                            null, 0, 0, 0, notice,result.batchId);
                                }else {
                                    IntentUtil.startPushStreamHorizontalActivity(PublishLiveActivity.this, null,null,
                                            null, 0, 0, 0, notice,result.batchId);
                                }
                            }
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mDialog.dismiss();
                        }
                    });
        }
        mDialog.show();
    }

    @Override
    public View onLoadContentView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_publish_live, null);
    }

    @Override
    public View onLoadNavView() {
        return mNavView = new BaseNavView(this);
    }

    @Override
    public boolean isTopCover() {
        return false;
    }

    private boolean isDeleteLocation = false;
    private int mIndex;

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.ac_publish_location) {
            //选择地址
//            NavUtils.gotoSelectCity(this, ValueCommentType.CITY_SOURCE_HOME,
//                    ValueConstants.REQ_CODE_SELECT_CITY);
        } else if (id == R.id.ac_publish_location_layout) {
            isDeleteLocation = !isDeleteLocation;

            TCEventHelper.onEvent(PublishLiveActivity.this, AnalyDataValue.LIVE_READY_CLOSE_LOCATION);
            if (isDeleteLocation) {
                //删除地址
                cityCode = "0";
                cityName = "火星";
                ((ImageView) findViewById(R.id.ac_publish_location_img)).setImageResource(R.mipmap.ic_live_location_gray);
                findViewById(R.id.ac_publish_delete_location).setVisibility(View.INVISIBLE);
                findViewById(R.id.ac_publish_location).setVisibility(View.INVISIBLE);
                mLocation.setText("");
            } else {
                cityCode = LocationManager.getInstance().getStorage().getManual_cityCode();
                cityName = LocationManager.getInstance().getStorage().getManual_cityName();
                ((ImageView) findViewById(R.id.ac_publish_location_img)).setImageResource(R.mipmap.ic_live_location);
                findViewById(R.id.ac_publish_delete_location).setVisibility(View.VISIBLE);
                findViewById(R.id.ac_publish_location).setVisibility(View.VISIBLE);
                mLocation.setText(cityName);
            }
        }/*else if (id == R.id.iv_horizontal_publish){
            String title = mTitle.getText().toString();
            IntentUtil.startPushStreamHorizontalActivity(PublishLiveActivity.this, title, cityName,
                    cityCode, (int) categoryCode, result.roomId,HORIZONTAL_PUBLISH, notice);
            finish();
        }else if (id == R.id.iv_vertical_publish){
            String title = mTitle.getText().toString();
            IntentUtil.startPushStreamActivity(PublishLiveActivity.this, title, YHYApplication.sCityName,
                    YHYApplication.sCityCode, (int) categoryCode, result.roomId, VERTICAL_PUBLISH,notice);
            finish();
        }*/
        // test by shenwenjie

//            cityName:火星
//            cityCode:0
//            categoryCode:2
//            roomId:18
//            notice:dfasfsddfa

//             test1153
//             cityName:火星
//             cityCode:0
//             categoryCode:2
//             roomId:19
//             notice:testessteste
        else if (id == R.id.ac_publish_add_topic) {
            // TODO: 2016/8/25  添加话题
            TCEventHelper.onEvent(PublishLiveActivity.this, AnalyDataValue.LIVE_READY_ADD_TOPIC);
            NavUtils.gotoAddTopic((Activity) v.getContext(), ADD_LIVE_LABEL);
        } else if (id == R.id.ac_publish_category) {
            if (result == null || result.list == null || result.list.size() == 0) return;
            // 直播分类
            TCEventHelper.onEvent(this, AnalyDataValue.LIVE_SETTING_TYPE_CLICK);
            initSelectLiveCategorization();
            mDateDialog.show();
        } else if (id == R.id.ac_publish_hd_parent) {
            TCEventHelper.onEvent(PublishLiveActivity.this, AnalyDataValue.LIVE_READY_HIGH_DEFINITION);
            showHdDialog();
        }
    }

    private void initSelectLiveCategorization() {
        if (mDateDialog == null) {
            View dialogView = View.inflate(this, R.layout.dialog_live_categorization_view, null);
            mDateDialog = new DialogBuilder(this)
                    .setStyle(R.style.kangzai_dialog)
                    .setContentView(dialogView)
                    .setCanceledOnTouchOutside(true)
                    .setWidth(ScreenSize.getScreenWidth(this))
                    .setGravity(Gravity.BOTTOM)
                    .setAnimation(R.anim.pop_enter_anim)
                    .build();
            final Wheel3DView mCate = (Wheel3DView) dialogView.findViewById(R.id.wv_cate);
            mCate.setCyclic(false);
            final String[] categoryResult = new String[categoryResults.size()];
            for (int i = 0; i < categoryResults.size(); i++) {
                categoryResult[i] = categoryResults.get(i).name;
            }
            mCate.setEntries(categoryResult);
            dialogView.findViewById(R.id.dialog_ok).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mIndex = mCate.getCurrentIndex();
                    categoryName = categoryResults.get(mIndex).name;
                    categoryCode = categoryResults.get(mIndex).code;
                    mTextViewCategory.setText(categoryName);
                    mDateDialog.dismiss();
                    if (isHorizontalClickLive){
                        String title = mTitle.getText().toString();
                        IntentUtil.startPushStreamHorizontalActivity(PublishLiveActivity.this, title, cityName,
                                cityCode, (int) categoryCode, result.roomId, HORIZONTAL_PUBLISH, notice, 0);
                        finish();
                    }else if (isVerticalClickLive){
                        String title = mTitle.getText().toString();
                        IntentUtil.startPushStreamActivity(PublishLiveActivity.this, title,LocationManager.getInstance().getStorage().getGprs_cityName(),
                                LocationManager.getInstance().getStorage().getGprs_cityCode(), (int) categoryCode, result.roomId, VERTICAL_PUBLISH, notice, 0);
                        finish();
                    }
                }
            });
            dialogView.findViewById(R.id.dialog_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDateDialog.dismiss();
                }
            });
            mDateDialog.setCanceledOnTouchOutside(true);
            mDateDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    mCate.setCurrentIndex(mIndex);
                }
            });
        }
    }

    Dialog hdDialog;

    private void showHdDialog() {
        definitionView.setSelectedIndex(defintionIndex);
        if (hdDialog == null) {
            hdDialog = DialogUtil.getMenuDialog(this, definitionView);
            hdDialog.setCanceledOnTouchOutside(true);
        }
        if (getWindow().getDecorView().getWindowToken() != null) {
            hdDialog.show();
        }
    }

    private void hideHdDialog() {
        if (hdDialog != null && hdDialog.isShowing()) {
            hdDialog.hide();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
//            case ValueConstants.REQ_CODE_SELECT_CITY:
//                if (resultCode == Activity.RESULT_OK) {
//                    if (data != null) {
//                        AddressBean addressBean = (AddressBean) data.getSerializableExtra(SPUtils.EXTRA_SELECT_CITY);
//                        cityName = addressBean.getName();
//                        cityCode = addressBean.getCityCode();
//                        mLocation.setText(cityName);
//                    }
//                }
//                break;
            case REQUEST_SETTING_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    notice = data.getStringExtra(IntentUtil.BUNDLE_NOTICE);
                    categoryName = data.getStringExtra(IntentUtil.BUNDLE_CATEGORY_NAME);
                    categoryCode = data.getLongExtra(IntentUtil.BUNDLE_CATEGORY_CODE, 0);
                }
                break;
            case ADD_LIVE_LABEL: {
                if (Activity.RESULT_OK == resultCode) {
                    String mTopicContent = data.getStringExtra(SPUtils.EXTRA_ADD_LIVE_LABEL);
                    if (TextUtils.isEmpty(mTopicContent) || mTopicContent.length() + mTitle.getText().length() > 15) {
                        return;
                    }
                    int index = mTitle.getSelectionStart();
                    Editable editable = mTitle.getText();
                    editable.insert(index, mTopicContent);
//                    mTitle.setText(mTopicContent + mTitle.getText().toString());
                }
                break;
            }

            case REQUEST_CATEGORY_CODE: {        // 选择直播分类
                if (resultCode == RESULT_OK) {
                    categoryCode = data.getLongExtra(IntentUtil.BUNDLE_CATEGORY_CODE, 0);
                    for (int i = 0; i < categoryResults.size(); i++) {
                        if (categoryResults.get(i).code == categoryCode) {
                            categoryName = categoryResults.get(i).name;
                            mTextViewCategory.setText(categoryName);
                            break;
                        }
                    }
                }
                break;

            }
            case REQUEST_CATEGORY_LIVE_HOR: {    //  横屏直播时去选择直播分类
                if (resultCode == RESULT_OK) {
                    categoryCode = data.getLongExtra(IntentUtil.BUNDLE_CATEGORY_CODE, 0);
                    for (int i = 0; i < categoryResults.size(); i++) {
                        if (categoryResults.get(i).code == categoryCode) {
                            categoryName = categoryResults.get(i).name;
                            mTextViewCategory.setText(categoryName);
                            String title = mTitle.getText().toString();
                            IntentUtil.startPushStreamHorizontalActivity(PublishLiveActivity.this, title, cityName,
                                    cityCode, (int) categoryCode, result.roomId, HORIZONTAL_PUBLISH, notice, 0);
                            finish();
                            break;
                        }
                    }
                }
                break;

            }
            case REQUEST_CATEGORY_LIVE_VER: {    // 竖屏直播时去选择直播分类
                if (resultCode == RESULT_OK) {
                    categoryCode = data.getLongExtra(IntentUtil.BUNDLE_CATEGORY_CODE, 0);
                    for (int i = 0; i < categoryResults.size(); i++) {
                        if (categoryResults.get(i).code == categoryCode) {
                            categoryName = categoryResults.get(i).name;
                            mTextViewCategory.setText(categoryName);
                            String title = mTitle.getText().toString();
                            IntentUtil.startPushStreamActivity(PublishLiveActivity.this, title,LocationManager.getInstance().getStorage().getGprs_cityName(),
                                    LocationManager.getInstance().getStorage().getGprs_cityCode(), (int) categoryCode, result.roomId, VERTICAL_PUBLISH, notice, 0);
                            finish();
                            break;
                        }
                    }
                }
                break;

            }
        }
    }

    @Override
    public void onDefinitionSelected(int index, String defintion) {
        defintionIndex = index;
        mHDTextView.setText(defintion);
        SPUtils.save(this, SPUtils.EXTRA_LIVE_DEFINTION_INDEX, defintionIndex);
        hideHdDialog();
    }

    @Override
    public void onDefintionSelectCancel() {
        hideHdDialog();
    }
}
