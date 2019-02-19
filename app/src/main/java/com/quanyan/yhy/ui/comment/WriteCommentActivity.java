package com.quanyan.yhy.ui.comment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.harwkin.nb.camera.PageBigImageActivity;
import com.harwkin.nb.camera.TimeUtil;
import com.harwkin.nb.camera.callback.SelectMoreListener;
import com.harwkin.nb.camera.options.CameraOptions;
import com.harwkin.nb.camera.pop.CameraPop;
import com.harwkin.nb.camera.pop.CameraPopListener;
import com.harwkin.nb.camera.type.OpenType;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.baseenum.IActionTitleBar;
import com.quanyan.base.util.LocalUtils;
import com.quanyan.base.util.StringUtil;
import com.quanyan.base.view.BaseNavView;
import com.quanyan.base.view.customview.NoScrollGridView;
import com.quanyan.base.yminterface.ErrorViewClick;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.AnalyDataValue;
import com.quanyan.yhy.common.AnonyStatus;
import com.quanyan.yhy.common.ErrorCode;
import com.quanyan.yhy.eventbus.EvBusComment;
import com.quanyan.yhy.net.NetThreadManager;
import com.quanyan.yhy.ui.base.utils.DialogUtil;
import com.quanyan.yhy.ui.base.utils.TCEventHelper;
import com.quanyan.yhy.ui.base.views.MyPinchZoomImageView;
import com.quanyan.yhy.ui.club.clubcontroller.ClubController;
import com.quanyan.yhy.ui.comment.controller.CommentController;
import com.quanyan.yhy.ui.common.calendar.ScreenUtil;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.discovery.adapter.SendSubjectImgGridAdapter;
import com.quanyan.yhy.view.CommentRatingBar;
import com.yhy.common.beans.album.MediaItem;
import com.yhy.common.beans.net.model.PhotoData;
import com.yhy.common.beans.net.model.comment.DimensionInfo;
import com.yhy.common.beans.net.model.comment.DimensionList;
import com.yhy.common.beans.net.model.tm.OrderRate;
import com.yhy.common.beans.net.model.tm.PostRateParam;
import com.yhy.common.beans.net.model.tm.RateDimension;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.AndroidUtils;
import com.yhy.common.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created with Android Studio.
 * Title:WriteCommentActivity
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:baojie
 * Date:2016-3-3
 * Time:9:16
 * Version 1.0
 */
public class WriteCommentActivity extends BaseActivity implements SendSubjectImgGridAdapter.PicNumChanged, AdapterView.OnItemClickListener, View.OnClickListener {

    @ViewInject(R.id.ll_comment)
    private LinearLayout ll_comment;//评分头部

    @ViewInject(R.id.tv_photo_suggest)
    private TextView mTvPhotoSuggest;//图片提示

    @ViewInject(R.id.ll_comment_content)
    private LinearLayout mLlCommentContent;//内容的父级

    @ViewInject(R.id.add_comment_content)
    private EditText mCommentContent;//点评内容

    @ViewInject(R.id.add_comment_text_num_change)
    private TextView mCommentTextNum;//点评字数

    @ViewInject(R.id.add_comment_pic_list_grid)
    private NoScrollGridView mNoScrollGridView;//点评图片

    @ViewInject(R.id.cb_switch)
    private CheckBox mCbSwitch;

    @ViewInject(R.id.tv_commit)
    private TextView tv_commit;//提交按钮

    private String typeSource = "";//来源
    private String[] commentArray;
    private CameraPop mCameraPop;
    private SendSubjectImgGridAdapter mSelectImgGridAdapter;
    private CommentController mController;
    private Dialog mDialogCancle;
    private BaseNavView mBaseNavView;

    private PostRateParam mPostRateParam;
    private boolean isAnony = true;//是否匿名
    private List<PhotoData> mData = new ArrayList<PhotoData>();
    private ClubController mClubControler;
    private OrderRate mOrderRate;
    private long mOrderId;
    private long mUserId;private String mCommentType;
    private ArrayList<RateDimension> mSendDimensionInfos;


    /**
     * 进入写点评界面
     * @param activity
     * @param type
     * @param reqCode
     */
    public static void gotoWriteCommentAcitivty(Activity activity, long orderId, long userId, String type, int reqCode) {
        Intent intent = new Intent(activity, WriteCommentActivity.class);
        intent.putExtra(SPUtils.EXTRA_TYPE, type);
        intent.putExtra(SPUtils.EXTRA_ID, orderId);
        intent.putExtra(SPUtils.EXTRA_TAG_ID, userId);
        activity.startActivityForResult(intent, reqCode);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ViewUtils.inject(this);
        mController = new CommentController(this, mHandler);
        mClubControler = new ClubController(this, mHandler);
        mCommentType = getIntent().getStringExtra(SPUtils.EXTRA_TYPE);
        mOrderId = getIntent().getLongExtra(SPUtils.EXTRA_ID, -1);
        mUserId = getIntent().getLongExtra(SPUtils.EXTRA_TAG_ID, -1);

        mPostRateParam = new PostRateParam();
        mOrderRate = new OrderRate();
        //点评头部信息
        //initCommentHead();
        //图片处理
        initPicSelect();
        //事件处理
        initEvent();

        //图片提示
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mTvPhotoSuggest.getLayoutParams();
        int width = (ScreenUtil.getScreenWidth(this) - 30 * 2 - 40 * 2) / 3 + 40;
        layoutParams.setMargins(width, 0, 0, 0);
        mTvPhotoSuggest.setLayoutParams(layoutParams);

        if(!StringUtil.isEmpty(mCommentType)){
            doNetDatas();
        }
    }

    private void doNetDatas() {
        mController.doGetRateDimensionList(WriteCommentActivity.this,mCommentType);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        hideLoadingView();
        hideErrorView(null);
        switch (msg.what){
            case ValueConstants.MSG_COMMENT_WRITE_INIT_OK:
                DimensionList result = (DimensionList) msg.obj;
                if(result != null){
                    handlerData(result.dimensionList);
                }
                break;
            case ValueConstants.MSG_COMMENT_WRITE_INIT_KO:
                showErrorView(null, ErrorCode.NETWORK_UNAVAILABLE == msg.arg1 ?
                        IActionTitleBar.ErrorType.NETUNAVAILABLE : IActionTitleBar.ErrorType.ERRORNET, "", getString(R.string.error_view_network_loaderror_content), "", new ErrorViewClick() {
                    @Override
                    public void onClick(View view) {
                        doNetDatas();
                    }
                });
                //ToastUtil.showToast(WriteCommentActivity.this, StringUtil.handlerErrorCode(getApplicationContext(), msg.arg1));
                break;
            case ValueConstants.MSG_UPLOADIMAGE_OK:
                List<String> uploadFiles = (List<String>) msg.obj;
                if (msg.obj != null) {
                    try {
                        if (uploadFiles.size() > 0) {
                            int failcount = mData.size() - ((List<String>) msg.obj).size();
                            if (failcount == 0) {
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
                if(msg.obj != null) {
                    ToastUtil.showToast(this, (String) msg.obj);
                }
                break;
            case ValueConstants.MSG_COMMENT_WRITE_SUBMIT_OK:
                Boolean isSuccess = (Boolean) msg.obj;
                if(isSuccess){
                    ToastUtil.showToast(this, getString(R.string.label_comment_submit_success));
                    EventBus.getDefault().post(new EvBusComment());
                    setResult(RESULT_OK);
                    finish();
                }else {
                    ToastUtil.showToast(this, getString(R.string.label_comment_submit_fail));
                }
                break;
            case ValueConstants.MSG_COMMENT_WRITE_SUBMIT_KO:
                AndroidUtils.showToastCenter(WriteCommentActivity.this, StringUtil.handlerErrorCode(getApplicationContext(), msg.arg1));
                break;
        }
    }


    //图片上传地址封装
    private void encapImageStr(List<String> uploadFiles) {

        if(mOrderRate == null){
            mOrderRate = new OrderRate();
        }
        mOrderRate.picUrlList = uploadFiles;
        //封装提交评价的数据
        dataPostEncap();
        //提交评价
        mController.doPostRate(WriteCommentActivity.this,mPostRateParam);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(KeyEvent.KEYCODE_BACK == keyCode){
            doCancleBack();
        }
        return true;
    }

    //返回操作
    private void doCancleBack() {
        mDialogCancle = DialogUtil.showMessageDialog(this, getString(R.string.dialog_order_cancel_title), getString(R.string.dialog_comment_cancel_content),
                getString(R.string.label_btn_ok), getString(R.string.cancel), new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }, null);
        mDialogCancle.show();
    }

    /**
     * 处理数据
     * @param value
     */
    private void handlerData(List<DimensionInfo> value) {
        mSendDimensionInfos = new ArrayList<>();
        if(value != null && value.size() > 0){
            //initOrderRate();
            ll_comment.removeAllViews();
            for (int i = 0; i < value.size(); i++) {
                final RateDimension dimensionInfo = new RateDimension();
                CommentRatingBar commentRatingBar = new CommentRatingBar(this);
                commentRatingBar.setDesc(value.get(i).dimensionName);
                dimensionInfo.value = 5;
                commentRatingBar.setOnCommentRatingListener(new CommentRatingBar.OnCommentRatingListener() {
                    @Override
                    public void onCommentRating(Object bindObject, int ratingScore) {
                        dimensionInfo.value = ratingScore;
                    }
                });
                dimensionInfo.code = value.get(i).dimensionCode;
                mSendDimensionInfos.add(dimensionInfo);
                ll_comment.addView(commentRatingBar);

            }
        }
    }

    //封装提交评价的请求数据
    private void dataPostEncap() {
        if(mUserId == -1 || mOrderId == -1){
            return;
        }

        if(mPostRateParam == null){
            mPostRateParam = new PostRateParam();
        }
        if(mOrderRate == null){
            mOrderRate = new OrderRate();
        }
        mOrderRate.bizOrderId = mOrderId;
        mOrderRate.rateDimensionList = mSendDimensionInfos;
        mOrderRate.anony = isAnony? AnonyStatus.AVAILABLE:AnonyStatus.DELETED;
        String commentContent = mCommentContent.getText().toString();
        if(!StringUtil.isEmpty(commentContent)){
            //commentContent = getString(R.string.comment_default_content);
            mOrderRate.content = commentContent;
        }
        mPostRateParam.userId = mUserId;
        mPostRateParam.mainOrderRate = mOrderRate;
    }

    private void initEvent() {
        mNoScrollGridView.setOnItemClickListener(this);
        tv_commit.setOnClickListener(this);
        mCbSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //是否匿名评价
                if(isChecked){
                    isAnony = true;
                }else {
                    isAnony = false;
                }
            }
        });
        //内容焦点设置
        mCommentContent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mCommentContent.setFocusable(true);
                mCommentContent.setFocusableInTouchMode(true);
                mCommentContent.requestFocus();
                return false;
            }
        });

        mCommentContent.addTextChangedListener(new TextWatcher() {
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
                    mCommentTextNum.setText(s.length() + "/200字");
                } else {
                    mCommentTextNum.setText("0/200字");
                }
            }
        });
    }

    private void initPicSelect() {
        mSelectImgGridAdapter = new SendSubjectImgGridAdapter(this, ValueConstants.MAX_SELECT_PICTURE_COMMENT);
        mSelectImgGridAdapter.setPicNumChanged(this);
        mNoScrollGridView.setAdapter(mSelectImgGridAdapter);
        mCameraPop = new CameraPop(this, new CameraPopListener() {
            @Override
            public void onCamreaClick(CameraOptions options) {
                if (LocalUtils.isAlertMaxStorage()) {
                    ToastUtil.showToast(WriteCommentActivity.this, getString(R.string.label_toast_sdcard_unavailable));
                    return;
                }
                options.setOpenType(OpenType.OPEN_CAMERA);
                mCameraPop.process();
            }

            @Override
            public void onPickClick(CameraOptions options) {
                options.setOpenType(OpenType.OPENN_USER_ALBUM).setMaxSelect(ValueConstants.MAX_SELECT_PICTURE_COMMENT - mSelectImgGridAdapter.getDataSize());
                mCameraPop.process();
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
        }, new SelectMoreListener() {
            @Override
            public void onSelectedMoreListener(List<MediaItem> pathList) {
                selectPhotoOk(pathList);
            }
        });
    }

    @Override
    public void onPicNumChange(List<PhotoData> data) {
        if(data.size() >= 1){
            mTvPhotoSuggest.setVisibility(View.GONE);
        }else {
            mTvPhotoSuggest.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 更新已选图片
     *
     * @param pathList
     */
    private void selectPhotoOk(List<MediaItem> pathList) {
        if (pathList != null) {
            mSelectImgGridAdapter.addAll(PhotoData.ImageItem2PhotoData(pathList));
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int count = mSelectImgGridAdapter.getDataSize();
        if (ValueConstants.MAX_SELECT_PICTURE_COMMENT > count && position == mSelectImgGridAdapter.getCount() - 1) {
            mCameraPop.showMenu(mNoScrollGridView);
            return;
        } else {
            if (mSelectImgGridAdapter.getItem(position) instanceof PhotoData) {
                ArrayList<String> data = new ArrayList<String>();
                for (PhotoData photoData : mSelectImgGridAdapter.getData()) {
                    data.add(photoData.mLocalUrl);
                }
                startActivityForResult(PageBigImageActivity.getIntent(WriteCommentActivity.this, data,
                        position, true, true, MyPinchZoomImageView.Mode.NONE.ordinal()), PageBigImageActivity.REQ_CHOOSE_MAP);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //提交
            case R.id.tv_commit:
                submitComment();
                break;
        }
    }

    /**
     * 提交评价内容
     */
    private void submitComment() {
        TCEventHelper.onEvent(this, AnalyDataValue.COMMENT_PUBLISH, mCommentType);
        if(TimeUtil.isFastDoubleClick()){
            return;
        }
        //上传图片
        if(mSelectImgGridAdapter.getData().size() > 0){
            Runnable requestThread = new Runnable() {
                @Override
                public void run() {
                    upLoadimage();
                }
            };
            NetThreadManager.getInstance().execute(requestThread);
        }else {
            //封装提交评价的数据
            dataPostEncap();
            //提交评价
            mController.doPostRate(WriteCommentActivity.this,mPostRateParam);
        }
    }
    //上传图片
    private void upLoadimage() {
        mData = mSelectImgGridAdapter.getData();
        String upurl[];
        if (mData != null && mData.size() > 0) {
            upurl = new String[mData.size()];
            for (int i = 0; i < mData.size(); i++) {
                upurl[i] = mData.get(i).mLocalUrl;
            }
            mClubControler.compressionImage(WriteCommentActivity.this,upurl);
        } else {
            mClubControler.sendUploadImage();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mDialogCancle != null){
            mDialogCancle.dismiss();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case PageBigImageActivity.REQ_CHOOSE_MAP:
                if (Activity.RESULT_OK == resultCode) {
                    List<String> t = data.getStringArrayListExtra(PageBigImageActivity.IMAGE_LIST_DATA);
                    if (PhotoData.String2PhotoData(t) != null) {
                        mSelectImgGridAdapter.replaceAll(PhotoData.String2PhotoData(t));
                    }
                }
                break;
        }
        mCameraPop.forResult(requestCode, resultCode, data);
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.ac_writecomment, null);
    }

    /**
     * 头部title
     * @return
     */
    @Override
    public View onLoadNavView() {
        mBaseNavView = new BaseNavView(this);
        mBaseNavView.setTitleText(R.string.write_comment_title);
        mBaseNavView.setLeftClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doCancleBack();
            }
        });
        return mBaseNavView;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }
}
