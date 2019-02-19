package com.quanyan.yhy.ui.servicerelease;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.harwkin.nb.camera.CropBuilder;
import com.harwkin.nb.camera.callback.SelectMoreListener;
import com.harwkin.nb.camera.options.CameraOptions;
import com.harwkin.nb.camera.pop.CameraPop;
import com.harwkin.nb.camera.pop.CameraPopListener;
import com.harwkin.nb.camera.type.OpenType;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.util.LocalUtils;
import com.quanyan.base.util.ScreenSize;
import com.quanyan.base.util.StringUtil;
import com.quanyan.base.view.BaseNavView;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.SysConfigType;
import com.quanyan.yhy.net.NetThreadManager;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.club.clubcontroller.ClubController;
import com.quanyan.yhy.ui.comment.ValueCommentType;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.order.OrderBottomTabView;
import com.quanyan.yhy.ui.servicerelease.controller.ManageInfoController;
import com.quanyan.yhy.ui.servicerelease.view.TextAndTextView;
import com.quanyan.yhy.ui.views.ReleaseSettingItem;
import com.yhy.common.beans.album.MediaItem;
import com.yhy.common.beans.net.model.common.PictureTextItemInfo;
import com.yhy.common.beans.net.model.param.WebParams;
import com.yhy.common.beans.net.model.tm.ItemProperty;
import com.yhy.common.beans.net.model.tm.ItemQueryParam;
import com.yhy.common.beans.net.model.tm.PublishServiceDO;
import com.yhy.common.beans.net.model.tm.ServiceArea;
import com.yhy.common.beans.net.model.user.Destination;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.CommonUtil;
import com.yhy.common.utils.JSONUtils;
import com.yhy.common.utils.SPUtils;
import com.yhy.imageloader.ImageLoadManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:ReleaseServiceActivity
 * Description:  　发布服务
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-6-30
 * Time:21:20
 * Version 1.1.0
 */

public class ReleaseServiceActivity extends BaseActivity implements View.OnClickListener, CameraPopListener, SelectMoreListener {
    public static final String ACTION_OPERATE_SERVICE = "com.quanyan.yhy.ui.servicerelease";
    private BaseNavView mBaseNavView;

    @ViewInject(R.id.obt_bottom)
    private OrderBottomTabView mBottomView;

    @ViewInject(R.id.iv_release_header)
    private ImageView mIVReleaseHeader;//上传的图片

    @ViewInject(R.id.icon_photo_click)
    private ImageView mPhotoClick;//照片点击

    @ViewInject(R.id.add_release_goods_title)
    private EditText mReleaseTitle;//商品标题

    @ViewInject(R.id.add_release_goods_title_count)
    private TextView mReleaseTitleCount;//商品标题字数

    @ViewInject(R.id.tat_goods_type)
    private TextAndTextView mGoodsType;//L类目

    @ViewInject(R.id.asi_goods_dest)
    private ReleaseSettingItem mGoodsDest;//目的地

    /*@ViewInject(R.id.tat_goods_price)
    private TextAndTextView mGoodsPrice;//价格*/

    @ViewInject(R.id.asi_pic_text)
    private ReleaseSettingItem mPicText;//图文详情

    @ViewInject(R.id.asi_ser_detail)
    private ReleaseSettingItem mSerDetail;//服务详情

    @ViewInject(R.id.ll_check_contain)
    private LinearLayout mLLCheckContain;//阅读协议布局

    @ViewInject(R.id.cb_is_default)
    private CheckBox mCheckBox;//选择框

    @ViewInject(R.id.tv_check_name)
    private TextView mCheckName;//阅读协议

    @ViewInject(R.id.tv_source_integral_value)
    private EditText mSourceIntegralValue;//原价积分

    @ViewInject(R.id.tv_current_integral_value)
    private EditText mCurrentIntegralValue;//现价积分

    @ViewInject(R.id.et_minute_value)
    private EditText mMinuteValue;//时长

    private int REQCODE_SERVICE_DETAIL = 0x0001;
    private int REQCODE_SERVICE_PIC_AND_TEXT = 0x0002;
    private int REQCODE_SERVICE_DESTINATION = 0x0003;

    private CameraPop mCameraPop;
    //    private DetailServiceBean mServiceBean;//服务详情
    private List<PictureTextItemInfo> mSourcePicTextList;//上传前的图文详情
    private List<PictureTextItemInfo> mPicTextList;//图文详情
    private List<Destination> mDestList;//目的地
    private List<ServiceArea> mServiceDestList;//服务器需传的目的地
    private List<MediaItem> mPicPathList;
    private MediaItem mTopPicItem;//头部的图片
    private ClubController mClubControler;//上传图片controller
    private ManageInfoController mControler;
    private PublishServiceDO mServiceDO;
    private Boolean isFirstClickButton = true;
    private int mClickType = 0;
    private long mAdviceId;
    private int mType;
    private List<MediaItem> mNetImageList;//不是网络图片的集合
    private PublishServiceDO mNewPublishService;
    private int mReqCode;
    private List<ItemProperty> mServiceDetailList;

    public static void gotoReleaseServiceActivity(Context context, long id, int type) {
        Intent intent = new Intent(context, ReleaseServiceActivity.class);
        intent.putExtra(SPUtils.EXTRA_ID, id);
        intent.putExtra(SPUtils.EXTRA_TYPE, type);
        context.startActivity(intent);
    }

    public static void gotoReleaseServiceActivity(Activity context, long id, int type, int reqCode) {
        Intent intent = new Intent(context, ReleaseServiceActivity.class);
        intent.putExtra(SPUtils.EXTRA_ID, id);
        intent.putExtra(SPUtils.EXTRA_TYPE, type);
        intent.putExtra(SPUtils.REQUEST_CODE, reqCode);
        context.startActivityForResult(intent, reqCode);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ViewUtils.inject(this);
        mClubControler = new ClubController(this, mHandler);
        mControler = new ManageInfoController(this, mHandler);
        mBaseNavView.setTitleText(R.string.label_release_service);
        mAdviceId = getIntent().getLongExtra(SPUtils.EXTRA_ID, -1);
        mType = getIntent().getIntExtra(SPUtils.EXTRA_TYPE, -1);
        mReqCode = getIntent().getIntExtra(SPUtils.REQUEST_CODE, -1);

        mIVReleaseHeader.setLayoutParams(new RelativeLayout.LayoutParams(ScreenSize.getScreenWidth(getApplicationContext()),
                ScreenSize.getScreenWidth(getApplicationContext())));
        initData();
        initCamera();
        initClick();
    }

    private void initClick() {
        mPhotoClick.setOnClickListener(this);
        mGoodsDest.setOnClickListener(this);
        mSerDetail.setOnClickListener(this);
        mPicText.setOnClickListener(this);
        mIVReleaseHeader.setOnClickListener(this);
        //返回按钮点击
        mBaseNavView.setLeftClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAdviceId == -1 && mType == -1) {
                    doCancleBack();
                } else {
                    finish();
                }
            }
        });

        mBottomView.setOnLeftClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //稍后发布
                mClickType = 3;
                doReleaseService();
            }
        });

        mBottomView.setonRighClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发布服务的操作
                mClickType = 2;
                doReleaseService();
            }
        });

        mReleaseTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String content = s.toString();
                boolean flag = content.contentEquals("\r\n");
                if (!flag && !TextUtils.isEmpty(s.toString().trim())) {
                    mReleaseTitleCount.setText(s.length() + "/38");
                } else {
                    mReleaseTitleCount.setText("0/38");
                }
            }
        });

        mReleaseTitle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            if (mAdviceId == -1 && mType == -1) {
                doCancleBack();
            } else {
                finish();
            }
        }
        return true;
    }

    //返回按钮做保存操作
    private void doCancleBack() {

        PublishServiceDO publishServiceDO = new PublishServiceDO();
        String releaseName = mReleaseTitle.getText().toString().trim();
        String goodType = mGoodsType.getContent();
        String sourceIntegralValue = mSourceIntegralValue.getText().toString().trim();
        String currentIntegralValue = mCurrentIntegralValue.getText().toString().trim();
        String minuteValue = mMinuteValue.getText().toString().trim();

        //头图
        if (mTopPicItem != null) {
            publishServiceDO.avater = mTopPicItem.getMediaPath();
        }

        if (!StringUtil.isEmpty(releaseName)) {
            publishServiceDO.title = releaseName;
        }

        if (!StringUtil.isEmpty(goodType)) {
            publishServiceDO.categoryType = ValueCommentType.MASTER_ADVICE;
        }

        if (!StringUtil.isEmpty(sourceIntegralValue)) {
            publishServiceDO.oldPrice = Long.parseLong(sourceIntegralValue);
        } else {
            publishServiceDO.oldPrice = 0;
        }

        if (!StringUtil.isEmpty(currentIntegralValue)) {
            publishServiceDO.discountPrice = Long.parseLong(currentIntegralValue);
        } else {
            publishServiceDO.discountPrice = 0;
        }

        if (!StringUtil.isEmpty(minuteValue)) {
            publishServiceDO.discountTime = Long.parseLong(minuteValue);
            publishServiceDO.oldTime = Long.parseLong(minuteValue);
        } else {
            publishServiceDO.discountTime = 5;
            publishServiceDO.oldTime = 5;
        }

        if (mServiceDestList != null && mServiceDestList.size() > 0) {
            publishServiceDO.serviceAreas = mServiceDestList;
        }

        /*if (mServiceBean != null) {
            if (!StringUtil.isEmpty(mServiceBean.costDesc)) {
                publishServiceDO.feeDesc = mServiceBean.costDesc;
            }
            if (!StringUtil.isEmpty(mServiceBean.bookExplain)) {
                publishServiceDO.bookingTip = mServiceBean.bookExplain;
            }
            if (!StringUtil.isEmpty(mServiceBean.backRule)) {
                publishServiceDO.refundRule = mServiceBean.backRule;
            }
        }*/

        if (mServiceDetailList != null && mServiceDetailList.size() > 0) {
            publishServiceDO.itemProperties = mServiceDetailList;
        }

        if (mSourcePicTextList != null && mSourcePicTextList.size() > 0) {
            publishServiceDO.pictureTextItems = mSourcePicTextList;
        }

        //保存数据存储到sp中
        saveSP(publishServiceDO);

    }

    private void saveSP(PublishServiceDO publishServiceDO) {
        String publishServiceDOString = JSONUtils.toJson(publishServiceDO);
        SPUtils.setReleaseConsultText(this, publishServiceDOString);
        finish();
    }

    private void doReleaseService() {

        String releaseName = mReleaseTitle.getText().toString().trim();
        String goodType = mGoodsType.getContent();
        String sourceIntegralValue = mSourceIntegralValue.getText().toString().trim();
        String currentIntegralValue = mCurrentIntegralValue.getText().toString().trim();
        String minuteValue = mMinuteValue.getText().toString().trim();

        if (StringUtil.isEmpty(sourceIntegralValue)) {
            sourceIntegralValue = "0";
        }

        if (StringUtil.isEmpty(currentIntegralValue)) {
            currentIntegralValue = "0";
        }

        if (StringUtil.isEmpty(minuteValue)) {
            minuteValue = "5";
        }

        if (mTopPicItem == null) {//头图
            ToastUtil.showToast(this, getString(R.string.toast_no_title_pic));
            return;
        }

        if (StringUtil.isEmpty(releaseName)) {//标题
            ToastUtil.showToast(this, getString(R.string.toast_no_title_name));
            return;
        }

        if (StringUtil.isEmpty(goodType)) {//类目
            ToastUtil.showToast(this, getString(R.string.toast_no_releae_type));
            return;
        }

//        if (mDestList == null || mDestList.size() <= 0) {//目的地
//            ToastUtil.showToast(this, getString(R.string.toast_no_releae_dest));
//            return;
//        }

        if (StringUtil.isEmpty(sourceIntegralValue)) {//原价
            ToastUtil.showToast(this, getString(R.string.toast_no_releae_source_price));
            return;
        }

        if (Long.parseLong(sourceIntegralValue) <= 0) {//原价不能小于0
            ToastUtil.showToast(this, getString(R.string.toast_source_price_zero));
            return;
        }

        if (Long.parseLong(sourceIntegralValue) > 50000) {//原价不能大于5万
            ToastUtil.showToast(this, getString(R.string.toast_source_price_five));
            return;
        }

        if (StringUtil.isEmpty(currentIntegralValue)) {//优惠价
            ToastUtil.showToast(this, getString(R.string.toast_no_releae_current_price));
            return;
        }

        if (Long.parseLong(currentIntegralValue) >= Long.parseLong(sourceIntegralValue)) {//优惠价大于原价提示
            ToastUtil.showToast(this, getString(R.string.toast_current_price_error));
            return;
        }

        if (StringUtil.isEmpty(minuteValue)) {//时长
            ToastUtil.showToast(this, getString(R.string.toast_no_releae_minute_value));
            return;
        }

        if (Long.parseLong(minuteValue) < 5) {//时长校验
            ToastUtil.showToast(this, getString(R.string.toast_no_minute_limit));
            return;
        }

        if (Long.parseLong(minuteValue) > 300) {//时长校验
            ToastUtil.showToast(this, getString(R.string.toast_no_max_minute_limit));
            return;
        }

        if (StringUtil.isEmpty(mPicText.getSummary())) {//图文详情
            ToastUtil.showToast(this, getString(R.string.toast_no_releae_pic_and_text));
            return;
        }

        if (StringUtil.isEmpty(mSerDetail.getSummary())) {//服务详情
            ToastUtil.showToast(this, getString(R.string.toast_no_releae_service_detail));
            return;
        }

        if (!mCheckBox.isChecked()) {//久休协议
            ToastUtil.showToast(this, getString(R.string.toast_releae_agreement));
            return;
        }

        if (StringUtil.isEmpty(sourceIntegralValue)) {
            sourceIntegralValue = "0";
        } else {
            int value = Integer.valueOf(sourceIntegralValue) * 10;
            sourceIntegralValue = String.valueOf(value);

        }

        if (StringUtil.isEmpty(currentIntegralValue)) {
            currentIntegralValue = "0";
        } else {
            int value = Integer.valueOf(currentIntegralValue) * 10;
            currentIntegralValue = String.valueOf(value);

        }
        
        if (mServiceDO == null) {
            mServiceDO = new PublishServiceDO();
        }

        //编辑封装id
        if (mAdviceId != -1 && mType != -1) {
            mServiceDO.id = mAdviceId;
            mServiceDO.categoryType = mType;
        }

        mServiceDO.title = releaseName;
        mServiceDO.categoryType = ValueCommentType.MASTER_ADVICE;
        mServiceDO.serviceAreas = mServiceDestList;
        mServiceDO.oldPrice = Long.parseLong(sourceIntegralValue);
        mServiceDO.oldTime = Long.parseLong(minuteValue);
        mServiceDO.discountPrice = Long.parseLong(currentIntegralValue);
        mServiceDO.discountTime = Long.parseLong(minuteValue);
        //服务详情
        /*if (mServiceBean != null) {
            if (!StringUtil.isEmpty(mServiceBean.costDesc)) {
                mServiceDO.feeDesc = mServiceBean.costDesc;
            }
            if (!StringUtil.isEmpty(mServiceBean.bookExplain)) {
                mServiceDO.bookingTip = mServiceBean.bookExplain;
            }
            if (!StringUtil.isEmpty(mServiceBean.backRule)) {
                mServiceDO.refundRule = mServiceBean.backRule;
            }
        }*/
        //服务详情
        if (mServiceDetailList != null && mServiceDetailList.size() > 0) {
            mServiceDO.itemProperties = mServiceDetailList;
        }

        showLoadingView("");
        mBottomView.getLeftLayout().setClickable(false);
        mBottomView.getRightView().setClickable(false);
        /*if (mClickType == 3) {
            mBottomView.getLeftLayout().setClickable(false);
        } else if (mClickType == 2) {
            mBottomView.getRightView().setClickable(false);
        }*/

        //上传图片
        doUpLoadPic();

    }

    private void doUpLoadPic() {
        if (mPicPathList != null) {
            //封装集合,添加头图
            if (isFirstClickButton && mTopPicItem != null) {
                mPicPathList.add(mTopPicItem);
                isFirstClickButton = false;
            }
            //开启线程上传图片
            Runnable requestThread = new Runnable() {
                @Override
                public void run() {
                    //上传
                    upLoadimage();
                }
            };
            NetThreadManager.getInstance().execute(requestThread);
        }
    }

    @Override
    public void finishAndRemoveTask() {
        super.finishAndRemoveTask();
    }

    private void upLoadimage() {
        String upurl[];
        if (mNetImageList == null) {
            mNetImageList = new ArrayList<>();
        }
        mNetImageList.clear();
        //收集非网络的图片
        if (mPicPathList != null && mPicPathList.size() > 0) {
            for (int i = 0; i < mPicPathList.size(); i++) {
                if (!mPicPathList.get(i).isNetImage) {
                    mNetImageList.add(mPicPathList.get(i));
                }
            }
        }

        //上传非网络状态的图片
        if (mNetImageList != null && mNetImageList.size() > 0) {
            upurl = new String[mNetImageList.size()];
            for (int i = 0; i < mNetImageList.size(); i++) {
                upurl[i] = mNetImageList.get(i).mediaPath;
            }
            mClubControler.compressionImage(ReleaseServiceActivity.this, upurl);
        } else {
            mClubControler.sendUploadImage();
        }
    }

    private void initData() {
        mBottomView.setBottomPrice(getString(R.string.label_goods_bottom_warehouse));
        mBottomView.setSubmitText(getString(R.string.label_goods_bottom_release));
        mBottomView.getLeftView().setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17);
        mBottomView.getRightView().setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17);
        //mBottomView.setWeigth(1, 1);

        mReleaseTitle.setHint(getString(R.string.hint_release_goods_title));
        mGoodsType.setTitle(getString(R.string.label_goods_type));
        mGoodsType.setContent(getString(R.string.value_goods_type));
        mGoodsDest.initItem(R.string.label_goods_dest);
        mGoodsDest.setSummary(getString(R.string.label_release_dest_desc));
        mPicText.initItem(R.string.label_goods_pic_text);
        mSerDetail.initItem(R.string.label_goods_ser_detail);
        mSourceIntegralValue.setHint("0");
        mCurrentIntegralValue.setHint("0");
        mMinuteValue.setHint("5");
        mIVReleaseHeader.setClickable(false);

        //协议服务
        initAgree();

        displayData();
    }

    //回显数据
    private void displayData() {

        if (mAdviceId != -1 && mType != -1) {
            //编辑的逻辑
            doEditFromNet();
            return;
        } else {
            //新发布的逻辑
            String releaseConsultText = SPUtils.getReleaseConsultText(this);
            if (StringUtil.isEmpty(releaseConsultText)) {
                return;
            }

            PublishServiceDO publishServiceDO = JSONUtils.convertToObject(releaseConsultText, PublishServiceDO.class);
            //数据展示
            dataShow(publishServiceDO, ValueCommentType.RELEASE_ADD);
        }

    }

    //查询咨询详情
    private void doEditFromNet() {
        showLoadingView("");
        ItemQueryParam param = new ItemQueryParam();
        param.id = mAdviceId;
        param.categoryId = mType;
        mControler.doGetPublishItemInfo(param);
    }

    @Override
    public void onPanelClosed(int featureId, Menu menu) {
        super.onPanelClosed(featureId, menu);
    }

    private void dataShow(PublishServiceDO publishServiceDO, String type) {
        //头图
        if (!StringUtil.isEmpty(publishServiceDO.avater)) {
            mIVReleaseHeader.setClickable(true);
            mPhotoClick.setVisibility(View.GONE);
            if (mTopPicItem == null) {
                mTopPicItem = new MediaItem();
            }
            mTopPicItem.mediaPath = publishServiceDO.avater;

            if (type.equals(ValueCommentType.RELEASE_ADD)) {
                mTopPicItem.isNetImage = false;
//                ImageLoaderUtil.loadLocalImage(mIVReleaseHeader, publishServiceDO.avater);
                ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(publishServiceDO.avater), mIVReleaseHeader);
            } else {
//                BaseImgView.loadimg(mIVReleaseHeader,
//                        ImageUtils.getImageFullUrl(publishServiceDO.avater),
//                        R.mipmap.icon_default_release_750_420,
//                        R.mipmap.icon_default_release_750_420,
//                        R.mipmap.icon_default_release_750_420,
//                        ImageScaleType.EXACTLY,
//                        750,
//                        420,
//                        0);

                ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(publishServiceDO.avater), R.mipmap.icon_default_750_360, 750, 420, mIVReleaseHeader);

                mTopPicItem.isNetImage = true;
            }

        }

        //标题
        if (!StringUtil.isEmpty(publishServiceDO.title)) {
            mReleaseTitle.setText(publishServiceDO.title);
            mReleaseTitleCount.setText(publishServiceDO.title.length() + "/38");
        }

        //服务目的地
        if (publishServiceDO.serviceAreas != null && publishServiceDO.serviceAreas.size() > 0) {
            if (mDestList == null) {
                mDestList = new ArrayList<>();
            }
            if (mServiceDestList == null) {
                mServiceDestList = new ArrayList<>();
            }
            mServiceDestList.clear();
            mDestList.clear();
            mServiceDestList.addAll(publishServiceDO.serviceAreas);

            StringBuilder destString = new StringBuilder();
            for (int i = 0; i < publishServiceDO.serviceAreas.size(); i++) {
                Destination destination = new Destination();
                destination.name = publishServiceDO.serviceAreas.get(i).areaName;
                destination.code = (int) publishServiceDO.serviceAreas.get(i).areaCode;
                mDestList.add(destination);
                if (i != publishServiceDO.serviceAreas.size() - 1) {
                    destString.append(publishServiceDO.serviceAreas.get(i).areaName + "、");
                } else {
                    destString.append(publishServiceDO.serviceAreas.get(i).areaName);
                }
            }
            mGoodsDest.setSummary(destString.toString());
        } else {
            mGoodsDest.setSummary(getString(R.string.label_release_dest_desc));
        }

        //原价
        if (publishServiceDO.oldPrice == 0) {
            mSourceIntegralValue.setHint("0");
        } else {
            mSourceIntegralValue.setText(String.valueOf(publishServiceDO.oldPrice/10));
        }

        //优惠价
        if (publishServiceDO.discountPrice == 0) {
            mCurrentIntegralValue.setHint("0");
        } else {
            mCurrentIntegralValue.setText(String.valueOf(publishServiceDO.discountPrice/10));
        }

        //时长
        if (publishServiceDO.discountTime == 5) {
            mMinuteValue.setHint("5");
        } else {
            mMinuteValue.setText(String.valueOf(publishServiceDO.discountTime));
        }

        //服务详情
        if (publishServiceDO.itemProperties != null && publishServiceDO.itemProperties.size() > 0) {
            if (mServiceDetailList == null) {
                mServiceDetailList = new ArrayList<>();
            }
            mServiceDetailList = publishServiceDO.itemProperties;
            mSerDetail.setSummary("");
            for (int i = 0; i < publishServiceDO.itemProperties.size(); i++) {
                if (!StringUtil.isEmpty(publishServiceDO.itemProperties.get(i).value)) {
                    mSerDetail.setSummary(getString(R.string.labe_alread_edit));
                    break;
                }
            }
        } else {
            mSerDetail.setSummary("");
        }


        //服务详情
        /*if (!StringUtil.isEmpty(publishServiceDO.feeDesc) || !StringUtil.isEmpty(publishServiceDO.bookingTip) || !StringUtil.isEmpty(publishServiceDO.refundRule)) {
            mSerDetail.setSummary(getString(R.string.labe_alread_edit));
        } else {
            mSerDetail.setSummary("");
        }*/
        /*if (mServiceBean == null) {
            mServiceBean = new DetailServiceBean();
        }
        if (!StringUtil.isEmpty(publishServiceDO.feeDesc)) {
            mServiceBean.costDesc = publishServiceDO.feeDesc;
        }
        if (!StringUtil.isEmpty(publishServiceDO.bookingTip)) {
            mServiceBean.bookExplain = publishServiceDO.bookingTip;
        }
        if (!StringUtil.isEmpty(publishServiceDO.refundRule)) {
            mServiceBean.backRule = publishServiceDO.refundRule;
        }*/

        //图文详情
        if (publishServiceDO.pictureTextItems != null && publishServiceDO.pictureTextItems.size() == 1) {
            if (StringUtil.isEmpty(publishServiceDO.pictureTextItems.get(0).value)) {
                mPicText.setSummary("");
            }
        }
        if (publishServiceDO.pictureTextItems != null && publishServiceDO.pictureTextItems.size() > 0) {
            if (mPicTextList == null) {
                mPicTextList = new ArrayList<>();
            }

            if (mPicPathList == null) {
                mPicPathList = new ArrayList<>();
            }

            if (mSourcePicTextList == null) {
                mSourcePicTextList = new ArrayList<>();
            }

            mPicPathList.clear();
            mPicTextList.clear();
            mSourcePicTextList.clear();

            mPicTextList.addAll(publishServiceDO.pictureTextItems);
            mSourcePicTextList.addAll(publishServiceDO.pictureTextItems);


            for (int i = 0; i < publishServiceDO.pictureTextItems.size(); i++) {
                if (!StringUtil.isEmpty(publishServiceDO.pictureTextItems.get(i).value)) {
                    mPicText.setSummary(getString(R.string.labe_alread_edit));
                    break;
                }
            }

            for (int i = 0; i < publishServiceDO.pictureTextItems.size(); i++) {
                if (!StringUtil.isEmpty(publishServiceDO.pictureTextItems.get(i).type)) {
                    if (publishServiceDO.pictureTextItems.get(i).type.equals(ValueCommentType.PIC_TYPE)) {
                        MediaItem mediaItem = new MediaItem();
                        mediaItem.mediaPath = publishServiceDO.pictureTextItems.get(i).value;
                        //是否是网络图片进行不同的赋值
                        if (type.equals(ValueCommentType.RELEASE_ADD)) {
                            mediaItem.isNetImage = false;
                        } else {
                            mediaItem.isNetImage = true;
                        }
                        mPicPathList.add(mediaItem);
                    }
                }

            }
        }
    }

    private void initAgree() {
        mCheckName.append(getString(R.string.label_release_read));
        String str = getString(R.string.label_release_agreement);
        SpannableString spannableString = new SpannableString(str);
        //UnderlineSpan span = new UnderlineSpan();
        //spannableString.setSpan(span, 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ClickableSpan() {

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
//                ds.setColor(0xFFFFAF00);//设置文字颜色
                ds.setColor(Color.parseColor("#E50011"));//设置文字颜色
                ds.setUnderlineText(false); //取消下划线
            }

            @Override
            public void onClick(View widget) {
                if (SPUtils.getServiceProtocol(ReleaseServiceActivity.this) != null) {
                    WebParams wp = new WebParams();
                    wp.setUrl(SPUtils.getString(SPUtils.TYPE_DEFAULT,
                            ReleaseServiceActivity.this,
                            SysConfigType.URL_MASTER_CONSULTING_SERVICE_PROTOCOL));
                    NavUtils.openBrowser(ReleaseServiceActivity.this, wp);
                } else {
                    ToastUtil.showToast(ReleaseServiceActivity.this, getString(R.string.toast_server_sys_config_error));
                }
            }
        }, 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        mCheckName.setHighlightColor(Color.TRANSPARENT); //设置点击后的颜色为透明，否则会一直出现高亮
        mCheckName.append(spannableString);
        mCheckName.setMovementMethod(LinkMovementMethod.getInstance());//响应点击事件

    }


    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        hideLoadingView();
        switch (msg.what) {
            case ValueConstants.MSG_UPLOADIMAGE_OK:
                buttonClickEnable();
                List<String> uploadFiles = (List<String>) msg.obj;
                if (msg.obj != null) {
                    try {
                        if (uploadFiles.size() > 0) {
                            int failcount;
                            if (mAdviceId != -1 && mType != -1) {//编辑时调用
                                failcount = mNetImageList.size() - ((List<String>) msg.obj).size();
                            } else {//添加时候调用
                                failcount = mPicPathList.size() - ((List<String>) msg.obj).size();
                            }

                            if (failcount == 0) {
                                //获取图片的上传地址
                                encapImageStr(uploadFiles);
                            } else {
                                ToastUtil.showToast(this, getString(R.string.toast_uploading_image_ko, failcount));
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    encapImageStr(uploadFiles);
                }
                break;
            case ValueConstants.MSG_UPLOADIMAGE_KO:
                buttonClickEnable();
                if (msg.obj != null) {
                    ToastUtil.showToast(this, (String) msg.obj);
                }
                break;

            case ValueConstants.MSG_RELEASE_SERVICE_OK:
                buttonClickEnable();
                Boolean isFinisth = (Boolean) msg.obj;
                if (isFinisth) {
                    mNewPublishService = new PublishServiceDO();
                    mNewPublishService.oldTime = 5;
                    mNewPublishService.discountTime = 5;
                    String serviceDOString = JSONUtils.toJson(mNewPublishService);
                    SPUtils.setReleaseConsultText(this, serviceDOString);
                    if (mReqCode != -1) {
                        if (mClickType == 3) {
                            //跳到稍后发布的服务管理
                            ToastUtil.showToast(this, getString(R.string.toast_publish_ok_wait));
                        } else {
                            //跳到立即发布的服务管理
                            ToastUtil.showToast(this, getString(R.string.toast_publish_ok_array));
                        }
                        Intent intent = new Intent();
                        intent.putExtra(ACTION_OPERATE_SERVICE, mClickType);
                        setResult(RESULT_OK, intent);
                    } else {
                        toastShow();
                    }
                    finish();
                } else {
                    mPicTextList.clear();
                    mPicTextList.addAll(mSourcePicTextList);
                    ToastUtil.showToast(this, getString(R.string.toast_publish_ko));
                }
                break;

            case ValueConstants.MSG_RELEASE_SERVICE_KO:
                buttonClickEnable();
                mPicTextList.clear();
                mPicTextList.addAll(mSourcePicTextList);
                ToastUtil.showToast(this, StringUtil.handlerErrorCode(getApplicationContext(), msg.arg1));
                break;

            case ValueConstants.MSG_SELECT_SERVICE_DETAIL_OK:
                PublishServiceDO value = (PublishServiceDO) msg.obj;
                if (value != null) {
                    dataShow(value, "");
                }
                break;

            case ValueConstants.MSG_SELECT_SERVICE_DETAIL_KO:
                ToastUtil.showToast(this, StringUtil.handlerErrorCode(getApplicationContext(), msg.arg1));
                break;

        }
    }

    private void buttonClickEnable() {
        mBottomView.getRightView().setClickable(true);
        mBottomView.getLeftLayout().setClickable(true);
        /*if (mClickType == 3) {
            mBottomView.getLeftLayout().setClickable(true);
        } else if (mClickType == 2) {
            mBottomView.getRightView().setClickable(true);
        }*/
    }

    private void toastShow() {
        /*CustomToastDialog myToast = new CustomToastDialog(this);
        myToast.setMessage(getString(R.string.toast_clear_cache_ok));
        myToast.showTime(3000);
        myToast.show();*/

        if (mClickType == 3) {
            //跳到待发布的服务管理
            ToastUtil.showToast(this, getString(R.string.toast_publish_ok_wait));
            NavUtils.gotoManageServiceInfoActivity(this, 1);
        } else {
            //跳到发布中的服务管理
            ToastUtil.showToast(this, getString(R.string.toast_publish_ok_array));
            NavUtils.gotoManageServiceInfoActivity(this, 0);
        }


    }


    //图片地址封装
    private void encapImageStr(List<String> uploadFiles) {
        //编辑的图片地址封装操作
        if (mAdviceId != -1 && mType != -1) {
            if (mServiceDO != null) {
                if (uploadFiles != null && uploadFiles.size() > 0) {
                    if (mNetImageList != null && mNetImageList.size() > 0) {
                        //判断头图是否是本地图
                        if (!mTopPicItem.isNetImage()) {
                            mServiceDO.avater = uploadFiles.get(uploadFiles.size() - 1);
                        } else {
                            mServiceDO.avater = mTopPicItem.mediaPath;
                        }
                    }

                    if (mPicTextList != null && mPicTextList.size() > 0) {
                        int j = 0;
                        int k = 0;
                        for (int i = 0; i < mPicTextList.size(); i++) {
                            if (mPicTextList.get(i).type.equals(ValueCommentType.PIC_TYPE)) {
                                if (mPicPathList.get(j).isNetImage()) {
                                    j++;
                                } else {
                                    mPicTextList.get(i).value = uploadFiles.get(k);
                                    k++;
                                    j++;
                                }
                            }
                        }
                        mServiceDO.pictureTextItems = mPicTextList;
                        mServiceDO.serviceState = mClickType;
                    }

                } else {
                    mServiceDO.avater = mTopPicItem.mediaPath;
                    mServiceDO.pictureTextItems = mPicTextList;
                    mServiceDO.serviceState = mClickType;
                }

                doReleaseNet();
            }

            return;
        }

        //非编辑的时候用到
        if (uploadFiles != null && uploadFiles.size() > 0 && mServiceDO != null) {
            //头图封装
            mServiceDO.avater = uploadFiles.get(uploadFiles.size() - 1);

            if (mPicTextList != null && mPicTextList.size() > 0) {
                //图片地址变成网络的地址
                int j = 0;
                for (int i = 0; i < mPicTextList.size(); i++) {
                    if (mPicTextList.get(i).type.equals(ValueCommentType.PIC_TYPE)) {
                        mPicTextList.get(i).value = uploadFiles.get(j);
                        j++;
                    }
                }

                mServiceDO.pictureTextItems = mPicTextList;
                mServiceDO.serviceState = mClickType;
            }

            doReleaseNet();
        }

    }

    //发布服务
    private void doReleaseNet() {
        showLoadingView("");
        mControler.doPublishService(this, mServiceDO);
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.ac_release_service, null);
    }

    @Override
    public View onLoadNavView() {
        mBaseNavView = new BaseNavView(this);
        return mBaseNavView;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.icon_photo_click://点击照片
                selectPicture();
                break;
            case R.id.iv_release_header://更改图片
                selectPicture();
                break;
            case R.id.asi_goods_dest://目的地选择
                NavUtils.gotoReleaseDestinationActivity(this, mDestList, REQCODE_SERVICE_DESTINATION);
                break;
            case R.id.asi_pic_text://图文介绍
                if (isValue(mPicTextList)) {
                    NavUtils.gotoPictureAndTextActivity(this, "", mPicTextList, mPicPathList, REQCODE_SERVICE_PIC_AND_TEXT, -1);
                } else {
                    NavUtils.gotoPictureAndTextActivity(this, "", null, null, REQCODE_SERVICE_PIC_AND_TEXT, -1);
                }
                break;
            case R.id.asi_ser_detail://服务详情
                NavUtils.gotoAddServiceDetailActivity(this, mServiceDetailList, REQCODE_SERVICE_DETAIL);
                break;
        }
    }

    private void selectPicture() {
        mCameraPop.showMenu(mIVReleaseHeader);
    }

    private void initCamera() {
        if (mCameraPop == null) {
            mCameraPop = new CameraPop(this, this, this);
        }
    }

    @Override
    public void onCamreaClick(CameraOptions options) {
        if (LocalUtils.isAlertMaxStorage()) {
            ToastUtil.showToast(ReleaseServiceActivity.this, getString(R.string.label_toast_sdcard_unavailable));
            return;
        }
//        options.setOpenType(OpenType.OPEN_CAMERA);
        options.setOpenType(OpenType.OPEN_CAMERA_CROP)
                .setCropBuilder(new CropBuilder(25, 14, 750, 420));
        managerProcess();
    }

    @Override
    public void onPickClick(CameraOptions options) {
        options.setOpenType(OpenType.OPEN_GALLERY_CROP)
                .setCropBuilder(new CropBuilder(25, 14, 750, 420));

//        options.setOpenType(OpenType.OPENN_USER_ALBUM).setMaxSelect(1);
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

    @Override
    public void onSelectedMoreListener(List<MediaItem> pathList) {
        if (pathList != null && pathList.size() > 0) {
            mTopPicItem = pathList.get(0);
            mPhotoClick.setVisibility(View.GONE);
            mIVReleaseHeader.setClickable(true);
            pathList.get(0).isNetImage = false;
//            ImageLoaderUtil.loadLocalImage(mIVReleaseHeader,
//                    !TextUtils.isEmpty(pathList.get(0).thumbnailPath) ? pathList.get(0).thumbnailPath : pathList.get(0).mediaPath);
ImageLoadManager.loadImage(!TextUtils.isEmpty(pathList.get(0).thumbnailPath) ? CommonUtil.getImageFullUrl(pathList.get(0).thumbnailPath) : CommonUtil.getImageFullUrl(pathList.get(0).mediaPath), mIVReleaseHeader);
        }
    }


    private void managerProcess() {
        mCameraPop.process();
        if (null != mCameraPop) {
            mCameraPop.dismiss();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQCODE_SERVICE_DETAIL) {//服务详情返回值
                mServiceDetailList = (List<ItemProperty>) data.getSerializableExtra(SPUtils.EXTRA_DATA);
                if (mServiceDetailList != null && mServiceDetailList.size() > 0) {
                    for (int i = 0; i < mServiceDetailList.size(); i++) {
                        if (!StringUtil.isEmpty(mServiceDetailList.get(i).value)) {
                            mSerDetail.setSummary(getString(R.string.labe_alread_edit));
                            return;
                        }
                    }
                    mSerDetail.setSummary("");
                }
                /*mServiceBean = (DetailServiceBean) data.getSerializableExtra(SPUtils.EXTRA_DATA);
                if (mServiceBean != null) {
                    if (!StringUtil.isEmpty(mServiceBean.costDesc) || !StringUtil.isEmpty(mServiceBean.bookExplain) || !StringUtil.isEmpty(mServiceBean.backRule)) {
                        mSerDetail.setSummary(getString(R.string.labe_alread_edit));
                    } else {
                        mSerDetail.setSummary("");
                    }
                }*/
            } else if (requestCode == REQCODE_SERVICE_PIC_AND_TEXT) {//图文详情返回值
                mPicTextList = (List<PictureTextItemInfo>) data.getSerializableExtra(SPUtils.EXTRA_DATA);
                mPicPathList = (List<MediaItem>) data.getSerializableExtra(SPUtils.EXTRA_NAME);

                if (!isValue(mPicTextList)) {
                    mPicText.setSummary("");
                } else {
                    mPicText.setSummary(getString(R.string.labe_alread_edit));

                    //保存原来的list集合
                    if (mSourcePicTextList == null) {
                        mSourcePicTextList = new ArrayList<>();
                    }
                    mSourcePicTextList.clear();

                    for (int i = 0; i < mPicTextList.size(); i++) {
                        PictureTextItemInfo pictureTextItemVo = new PictureTextItemInfo();
                        pictureTextItemVo.type = mPicTextList.get(i).type;
                        pictureTextItemVo.value = mPicTextList.get(i).value;
                        mSourcePicTextList.add(pictureTextItemVo);
                    }

                }

            } else if (requestCode == REQCODE_SERVICE_DESTINATION) {//目的地返回值
                mDestList = (List<Destination>) data.getSerializableExtra(SPUtils.EXTRA_DATA);
                if (mServiceDestList == null) {
                    mServiceDestList = new ArrayList<>();
                }
                mServiceDestList.clear();
                if (mDestList != null && mDestList.size() > 0) {
                    StringBuilder destString = new StringBuilder();
                    for (int i = 0; i < mDestList.size(); i++) {
                        ServiceArea infoVO = new ServiceArea();
                        infoVO.areaCode = mDestList.get(i).code;
                        infoVO.areaName = mDestList.get(i).name;
                        mServiceDestList.add(infoVO);
                        if (i != mDestList.size() - 1) {
                            destString.append(mDestList.get(i).name + "、");
                        } else {
                            destString.append(mDestList.get(i).name);
                        }
                    }
                    mGoodsDest.setSummary(destString.toString());
                } else {
                    mGoodsDest.setSummary(getString(R.string.label_release_dest_desc));
                }
            }
        }
        mCameraPop.forResult(requestCode, resultCode, data);
    }

    //判断集合是否有值，有--true， 无 ==false
    private Boolean isValue(List<PictureTextItemInfo> list) {
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (!StringUtil.isEmpty(list.get(i).value)) {
                    return true;
                }
            }
        }
        return false;
    }

}
