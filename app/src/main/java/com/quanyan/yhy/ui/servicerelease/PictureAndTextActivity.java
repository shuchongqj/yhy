package com.quanyan.yhy.ui.servicerelease;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.text.InputFilter;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.bumptech.glide.Glide;
import com.harwkin.nb.camera.ImageUtils;
import com.harwkin.nb.camera.PhotoUtil;
import com.harwkin.nb.camera.callback.SelectMoreListener;
import com.harwkin.nb.camera.options.CameraOptions;
import com.harwkin.nb.camera.pop.CameraPop;
import com.harwkin.nb.camera.pop.CameraPopListener;
import com.harwkin.nb.camera.type.OpenType;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.util.HarwkinLogUtil;
import com.quanyan.base.util.LocalUtils;
import com.quanyan.base.util.StringUtil;
import com.quanyan.base.view.BaseNavView;
import com.quanyan.yhy.R;
import com.quanyan.yhy.net.NetThreadManager;
import com.quanyan.yhy.ui.base.utils.DialogUtil;
import com.quanyan.yhy.ui.club.clubcontroller.ClubController;
import com.quanyan.yhy.ui.comment.ValueCommentType;
import com.quanyan.yhy.ui.comment.controller.CommentController;
import com.quanyan.yhy.ui.common.calendar.ScreenUtil;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.order.OrderBottomTabView;
import com.yhy.common.beans.album.MediaItem;
import com.yhy.common.beans.net.model.common.PictureTextItemInfo;
import com.yhy.common.beans.net.model.common.PictureTextListQuery;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.CommonUtil;
import com.yhy.common.utils.SPUtils;
import com.yhy.imageloader.ImageLoadManager;
import com.yhy.service.IUserService;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Created with Android Studio.
 * Title:PictureAndTextActivity
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-7-7
 * Time:17:16
 * Version 1.1.0
 */

public class PictureAndTextActivity extends BaseActivity implements CameraPopListener, SelectMoreListener {

    private BaseNavView mBaseNavView;
    @ViewInject(R.id.obt_bottom)
    private OrderBottomTabView mObtView;

    @ViewInject(R.id.ll_father_layout)
    private LinearLayout mFatherLayout;//图文具体的内容
    private int mIndex;

    private CameraPop mCameraPop;
    private LinearLayout.LayoutParams mParams;
    private LinearLayout.LayoutParams mPicParams;

    private boolean isPictureClick = false;
    private View mFocuseView;

    private List<MediaItem> mListImage;//图片的地址集合
    private List<PictureTextItemInfo> mPicListBeans;//图片的对象集合
    private List<View> mImageViewList;//自动存储的图片view，替换对应的关系
    private int mPicChild;
    private String mType;
    private List<PictureTextItemInfo> mListDatas;//所有对象的集合,传过来的参数
    private List<MediaItem> mImageListDatas;//图片的地址集合，传过来的参数
    private Boolean isFirstAddEdit = false;
    private Dialog mDialogCancle;
    private CommentController mController;
    private ClubController mClubControler;
    private List<PictureTextItemInfo> mContentList;//所有对象的集合,本地的参数
    private long mHomePageId;
    private List<String> mLocalPicList;
    private int mScreenWidth;

    @Autowired
    IUserService userService;

    public static void gotoPictureAndTextActivity(Activity context, String type, List<PictureTextItemInfo> list, List<MediaItem> imageList, int reqCode, long id) {
        Intent intent = new Intent(context, PictureAndTextActivity.class);
        intent.putExtra(SPUtils.EXTRA_TYPE, type);
        intent.putExtra(SPUtils.EXTRA_DATA, (Serializable) list);
        intent.putExtra(SPUtils.EXTRA_NAME, (Serializable) imageList);
        intent.putExtra(SPUtils.EXTRA_ID, id);
        context.startActivityForResult(intent, reqCode);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ViewUtils.inject(this);
        mController = new CommentController(this, mHandler);
        mClubControler = new ClubController(this, mHandler);
        mType = getIntent().getStringExtra(SPUtils.EXTRA_TYPE);
        mListDatas = (List<PictureTextItemInfo>) getIntent().getSerializableExtra(SPUtils.EXTRA_DATA);
        mImageListDatas = (List<MediaItem>) getIntent().getSerializableExtra(SPUtils.EXTRA_NAME);
        mHomePageId = getIntent().getLongExtra(SPUtils.EXTRA_ID, -1);
        initTitle();
        initData();
        //图片按钮点击
        initCamera();
        //点击事件处理
        initClick();

        initLayout();
    }

    //标题显示
    private void initTitle() {
        if (mType.equals(ValueCommentType.PIC_AND_TEXT_EXPERTHOME)) {
            mBaseNavView.setTitleText(R.string.mine_homepage_title);
        } else {
            mBaseNavView.setTitleText(R.string.label_goods_pic_text);
        }

    }

    private void initLayout() {
        if (mListDatas != null && mListDatas.size() > 0) {
            dataEncape();
        } else {
            isFirstAddEdit = true;
            mFatherLayout.addView(addEditView());
        }

    }

    private EditText addEditView() {

        EditText editText = new EditText(this);
        editText.setBackgroundColor(getResources().getColor(R.color.ac_bg_color));
        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        editText.setTextColor(getResources().getColor(R.color.neu_333333));
        editText.setLayoutParams(mParams);
        editText.setPadding(0, 0, 0, 0);
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(500)});
        if (isFirstAddEdit) {
            isFirstAddEdit = false;
            if (mType.equals(ValueCommentType.PIC_AND_TEXT_EXPERTHOME)) {
                editText.setHint(getString(R.string.mine_homepage_hint));
            } else {
                editText.setHint(getString(R.string.releae_pic_and_edit_hint));
            }

            editText.setHintTextColor(getResources().getColor(R.color.neu_999999));

        }
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mFocuseView = v;
                }
            }
        });
        return editText;
    }

    private ImageView addImageView() {
        final ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(mPicParams);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPictureClick = true;
                mPicChild = mFatherLayout.indexOfChild(v);
                mCameraPop.showMenu(v, getString(R.string.replace_photo), getString(R.string.delete_photo));
            }
        });
        return imageView;
    }

    //高度自适应的imageview
    private ImageView addImageView(String path) {
        final ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        if(!StringUtil.isEmpty(path)){
            File file = new File(path);
            if(file.exists()){
                //Bitmap bitmap = BitmapFactory.decodeFile(path);
                Bitmap bitmap = PhotoUtil.getThenumBitmap(path, 480, 800);
                int bWidth = bitmap.getWidth();
                int bHeight = bitmap.getHeight();
                int height = bHeight * mScreenWidth / bWidth;
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.width = mScreenWidth;
                layoutParams.height = height;
                layoutParams.gravity = Gravity.CENTER;
                imageView.setLayoutParams(layoutParams);
                //iv.invalidate();
                bitmap.recycle();
            }
        }else {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.width = mScreenWidth;
            layoutParams.gravity = Gravity.CENTER;
            imageView.setLayoutParams(layoutParams);
        }


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPictureClick = true;
                mPicChild = mFatherLayout.indexOfChild(v);
                mCameraPop.showMenu(v, getString(R.string.replace_photo), getString(R.string.delete_photo));
            }
        });
        return imageView;
    }


    private void initClick() {
        //添加图片
        mObtView.setOnLeftClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListImage != null && mListImage.size() >= ValueConstants.MAX_UPDATE_PERSON_PICTURES) {
                    ToastUtil.showToast(PictureAndTextActivity.this, getString(R.string.releae_pic_and_edit_limit));
                    return;
                }
                //照片选择
                isPictureClick = false;
                final ImageView imageView = addImageView("");
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isPictureClick = true;
                        mPicChild = mFatherLayout.indexOfChild(v);
                        mCameraPop.showMenu(v, getString(R.string.replace_photo), getString(R.string.delete_photo));
                    }
                });
                selectPicture(imageView);
            }
        });
        //完成
        mObtView.setonRighClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //完成操作
                onfinish();
            }
        });

        mBaseNavView.setLeftClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doCancleBack();
            }
        });

    }

    private void onfinish() {
        mContentList = getContentList();

        //判断是否有修改
        if (!isEdit(mContentList, mListDatas)) {
            finish();
        } else {
            if (mDialogCancle != null) {
                mDialogCancle.dismiss();
            }

            //主页点完成
            if (mType.equals(ValueCommentType.PIC_AND_TEXT_EXPERTHOME)) {
                if (mHomePageId != -1) {
                    //修改达人主页操作
                    doEditHomePageNet();
                } else {
                    //保存添加达人主页操作
                    doAddHomePageNet();
                }

            } else {
                Intent intent = new Intent();
                intent.putExtra(SPUtils.EXTRA_DATA, (Serializable) mContentList);
                intent.putExtra(SPUtils.EXTRA_NAME, (Serializable) mListImage);
                setResult(RESULT_OK, intent);
                finish();
            }
        }


    }

    //判断是否有修改操作，true--有修改，false--没有修改
    private Boolean isEdit(List<PictureTextItemInfo> list1, List<PictureTextItemInfo> list2) {
        if (list2 == null && !isValue()) {
            return false;
        }
        if (list2 != null && list2.size() > 0) {
            if (list1.size() != list2.size()) {
                return true;
            } else {
                for (int i = 0; i < list1.size(); i++) {
                    if (!(list1.get(i).type.equals(list2.get(i).type))) {
                        return true;
                    } else {
                        if (StringUtil.isEmpty(list1.get(i).value)) {
                            if (!StringUtil.isEmpty(list2.get(i).value)) {
                                return true;
                            }
                        } else {
                            if (StringUtil.isEmpty(list2.get(i).value)) {
                                return true;
                            } else {
                                if (!(list1.get(i).value.equals(list2.get(i).value))) {
                                    return true;
                                }
                            }
                        }
                    }
                }
                return false;
            }

        } else {
            return true;
        }
    }

    //修改达人主页
    private void doEditHomePageNet() {
        //判断是否需要上传图片
        if (mLocalPicList == null) {
            mLocalPicList = new ArrayList<>();
        }
        mLocalPicList.clear();
        if (mListImage != null && mListImage.size() > 0) {
            for (int i = 0; i < mListImage.size(); i++) {
                if (!mListImage.get(i).isNetImage) {
                    mLocalPicList.add(mListImage.get(i).mediaPath);
                }
            }

            if (mLocalPicList != null && mLocalPicList.size() > 0) {
                doUpLoadPic();
            } else {
                netEditPicAndText();
            }
        } else {
            netEditPicAndText();
        }
    }

    //添加达人主页
    private void doAddHomePageNet() {
        //上传图片
        if (mListImage != null && mListImage.size() > 0) {
            doUpLoadPic();
        } else {
            netAddPicAndText();
        }

    }

    //添加主页
    private void netAddPicAndText() {
        showLoadingView("");
        PictureTextListQuery params = new PictureTextListQuery();
        params.pictureTextItemInfoList = mContentList;
        params.outType = ValueCommentType.PIC_AND_TEXT_EXPERTHOME;
        params.outId = userService.getLoginUserId();
        mController.doAddPictureText(this, params);
    }

    //修改主页
    private void netEditPicAndText() {
        showLoadingView("");
        PictureTextListQuery params = new PictureTextListQuery();
        params.pictureTextItemInfoList = mContentList;
        params.outType = ValueCommentType.PIC_AND_TEXT_EXPERTHOME;
        params.id = mHomePageId;
        params.outId = userService.getLoginUserId();
        mController.doUpdatePictureText(this, params);
    }

    //上传图片
    private void doUpLoadPic() {
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

    //本地图片地址封装
    private void upLoadimage() {
        String upurl[];
        if (mHomePageId != -1) {//编辑
            if (mLocalPicList != null && mLocalPicList.size() > 0) {
                upurl = new String[mLocalPicList.size()];
                for (int i = 0; i < mLocalPicList.size(); i++) {
                    upurl[i] = mLocalPicList.get(i);
                }
                mClubControler.compressionImage(PictureAndTextActivity.this, upurl);
            } else {
                mClubControler.sendUploadImage();
            }
        } else {//添加
            if (mListImage != null && mListImage.size() > 0) {
                upurl = new String[mListImage.size()];
                for (int i = 0; i < mListImage.size(); i++) {
                    upurl[i] = mListImage.get(i).mediaPath;
                }
                mClubControler.compressionImage(PictureAndTextActivity.this, upurl);
            } else {
                mClubControler.sendUploadImage();
            }
        }

    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        hideLoadingView();
        switch (msg.what) {
            case ValueConstants.MSG_UPLOADIMAGE_OK:
                List<String> uploadFiles = (List<String>) msg.obj;
                if (msg.obj != null) {
                    try {
                        if (uploadFiles.size() > 0) {
                            int failcount;
                            if (mHomePageId != -1) {
                                failcount = mLocalPicList.size() - ((List<String>) msg.obj).size();
                            } else {
                                failcount = mListImage.size() - ((List<String>) msg.obj).size();
                            }
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
                if (msg.obj != null) {
                    ToastUtil.showToast(this, (String) msg.obj);
                }
                break;

            case ValueConstants.MSG_MINE_HOME_PAGE_ADD_OK:
                Boolean isTrue = (Boolean) msg.obj;
                if (isTrue) {
                    ToastUtil.showToast(this, getString(R.string.add_finish));
                    Intent intent = new Intent();
                    intent.putExtra(SPUtils.EXTRA_DATA, isValue());
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    ToastUtil.showToast(this, getString(R.string.add_error));
                }
                break;

            case ValueConstants.MSG_MINE_HOME_PAGE_ADD_KO:
                ToastUtil.showToast(this, StringUtil.handlerErrorCode(getApplicationContext(), msg.arg1));
                break;

            case ValueConstants.MSG_MINE_HOME_PAGE_EDIT_OK:
                Boolean isEditTrue = (Boolean) msg.obj;
                if (isEditTrue) {
                    ToastUtil.showToast(this, getString(R.string.edit_finish));
                    Intent intent = new Intent();
                    intent.putExtra(SPUtils.EXTRA_DATA, isValue());
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    ToastUtil.showToast(this, getString(R.string.edit_error));
                }
                break;

            case ValueConstants.MSG_MINE_HOME_PAGE_EDIT_KO:
                ToastUtil.showToast(this, StringUtil.handlerErrorCode(getApplicationContext(), msg.arg1));
                break;
        }

    }

    //图片的网络地址
    private void encapImageStr(List<String> uploadFiles) {
        //编辑的图片地址封装操作
        if (mHomePageId != -1) {
            if (uploadFiles != null && uploadFiles.size() > 0) {
                int j = 0;
                int k = 0;
                for (int i = 0; i < mContentList.size(); i++) {
                    if (mContentList.get(i).type.equals(ValueCommentType.PIC_TYPE)) {
                        if (!mListImage.get(j).isNetImage) {
                            mContentList.get(i).value = uploadFiles.get(k);
                            k++;
                        }
                        j++;
                    }
                }
            }
            netEditPicAndText();
            return;
        }

        //非编辑的时候用到
        if (uploadFiles != null && uploadFiles.size() > 0) {

            if (mContentList != null && mContentList.size() > 0) {
                //图片地址变成网络的地址
                int j = 0;
                for (int i = 0; i < mContentList.size(); i++) {
                    if (mContentList.get(i).type.equals(ValueCommentType.PIC_TYPE)) {
                        mContentList.get(i).value = uploadFiles.get(j);
                        j++;
                    }
                }
            }

            netAddPicAndText();
        }
    }

    private void selectPicture(View mPatContent) {
        mCameraPop.rebuildUI();
        mCameraPop.showMenu(mPatContent);
    }

    private void initCamera() {
        if (mCameraPop == null) {
            mCameraPop = new CameraPop(this, this, this);
        }

    }


    private void initData() {
        mObtView.setBottomPrice(getString(R.string.label_add_picture));
        mObtView.setSubmitText(getString(R.string.label_btn_finish));
        mObtView.getLeftView().setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        mObtView.getRightView().setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        //mObtView.setWeigth(1, 1);
        mParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mParams.setMargins(30, 30, 30, 30);
        mScreenWidth = ScreenUtil.getScreenWidth(this) - 60;
        mPicParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPicParams.setMargins(30, 0, 30, 0);
        if (mListImage == null) {
            mListImage = new ArrayList<>();
        }

        if (mPicListBeans == null) {
            mPicListBeans = new ArrayList<>();
        }

        if (mImageViewList == null) {
            mImageViewList = new ArrayList<>();
        }
    }

    private void dataEncape() {
        if (mImageListDatas != null && mImageListDatas.size() > 0) {
            mListImage.clear();
            mListImage.addAll(mImageListDatas);
        }

        int j = 0;
        for (int i = 0; i < mListDatas.size(); i++) {
            if (mListDatas.get(i).type.equals(ValueCommentType.EDIT_TYPE)) {
                EditText editText = addEditView();
                mFatherLayout.addView(editText);
                editText.setText(mListDatas.get(i).value);
            } else if (mListDatas.get(i).type.equals(ValueCommentType.PIC_TYPE)) {
                //添加图片操作
                ImageView imageView;
                if(!mImageListDatas.get(j).isNetImage){
                    imageView = addImageView(mListDatas.get(i).value);
                }else {
                    imageView = addImageView("");
                }

                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isPictureClick = true;
                        mPicChild = mFatherLayout.indexOfChild(v);
                        mCameraPop.showMenu(v, getString(R.string.replace_photo), getString(R.string.delete_photo));
                    }
                });
                mFatherLayout.addView(imageView);
                mImageViewList.add(imageView);
                mPicListBeans.add(mListDatas.get(i));
                if (!mImageListDatas.get(j).isNetImage) {
                    //imageHeighChange(imageView, mListDatas.get(i).value);
//                    ImageLoaderUtil.loadLocalImage(imageView, mListDatas.get(i).value);
                    ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(mListDatas.get(i).value), imageView);

                } else {
                    if (ImageUtils.getImageFullUrl(mListDatas.get(i).value).endsWith("gif")) {//gif
//                        Glide.with(imageView.getContext())
//                                .asGif()
//                                .load(ImageUtils.getImageFullUrl(mListDatas.get(i).value))
//                                .fitCenter()
//                                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
//                                .placeholder(R.mipmap.icon_default_750_420)
//                                .into(imageView);
                        ImageLoadManager.loadGifImage(CommonUtil.getImageFullUrl(mListDatas.get(i).value), R.mipmap.icon_default_750_420, 0, 0, imageView);

                    } else {//bitmap
                        Glide.with(imageView.getContext())
//                                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                .asBitmap()
                                .load(ImageUtils.getImageFullUrl(mListDatas.get(i).value))
//                                .placeholder(R.mipmap.icon_default_750_420)
                                .into(imageView);

//                        ImageLoadManager.loadImage(mListDatas.get(i).value, R.mipmap.icon_default_750_420, imageView);

                    }
                }
                j++;
            }
        }
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.ac_pic_and_text, null);
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
    public void onCamreaClick(CameraOptions options) {
        if (LocalUtils.isAlertMaxStorage()) {
            ToastUtil.showToast(PictureAndTextActivity.this, getString(R.string.label_toast_sdcard_unavailable));
            return;
        }
        options.setOpenType(OpenType.OPEN_CAMERA);
        managerProcess();
    }

    @Override
    public void onPickClick(CameraOptions options) {
        if (isPictureClick) {//替换图片只能选择一张
            options.setOpenType(OpenType.OPENN_USER_ALBUM).setMaxSelect(1);
        } else {
            options.setOpenType(OpenType.OPENN_USER_ALBUM).setMaxSelect(ValueConstants.MAX_UPDATE_PERSON_PICTURES - mListImage.size());
        }

        managerProcess();
    }

    @Override
    public void onDelClick() {
        //移除相关操作
        removeListAndUI();
    }

    private void removeListAndUI() {

        if (mImageViewList.size() != mPicListBeans.size() || mImageViewList.size() != mListImage.size()) {
            return;
        }

        if (mImageViewList != null && mImageViewList.size() > 0) {
            //删除集合
            for (int i = 0; i < mImageViewList.size(); i++) {
                if (mImageViewList.get(i) == mFatherLayout.getChildAt(mPicChild)) {
                    //mImageViewList.remove(i);
                    mPicListBeans.remove(i);
                    mListImage.remove(i);
                    break;
                }
            }
        }
        mImageViewList.remove(mFatherLayout.getChildAt(mPicChild));

        //删除UI
        mFatherLayout.removeViews(mPicChild, 1);


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
            //点击的是edittext
            if (!isPictureClick) {
                //添加view
                imageVisable(pathList);
                //生成list
                listAddCode(pathList);
            } else {
                //替换图片
                replaceListCode(pathList);
            }
//            ImageLoaderUtil.loadLocalImage((ImageView) mCameraPop.getClickView(), pathList.get(0).mediaPath);

        }
    }

    //替换图片
    private void replaceListCode(List<MediaItem> pathList) {
        if (mImageViewList != null && mImageViewList.size() > 0) {
            for (int i = 0; i < mImageViewList.size(); i++) {
                if (mCameraPop.getClickView() == mImageViewList.get(i)) {
                    PictureTextItemInfo bean = new PictureTextItemInfo();
                    bean.type = ValueCommentType.PIC_TYPE;
                    bean.value = pathList.get(0).getMediaPath();
                    mPicListBeans.set(i, bean);
                    MediaItem mediaItem = pathList.get(0);
                    mListImage.set(i, mediaItem);
                    break;
                }
            }
        }
        if(!pathList.get(0).isNetImage){
            imageHeighChange((ImageView) mCameraPop.getClickView(), pathList.get(0).mediaPath);
        }
        //imageHeighChange((ImageView) mCameraPop.getClickView(), pathList.get(0).mediaPath);
//        ImageLoaderUtil.loadLocalImage((ImageView) mCameraPop.getClickView(), pathList.get(0).mediaPath);
        ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(pathList.get(0).mediaPath), (ImageView) mCameraPop.getClickView());

    }

    //高度改变
    private void imageHeighChange(ImageView clickView, String imagePath) {
        if(!StringUtil.isEmpty(imagePath)){
            File file = new File(imagePath);
            if(file.exists()){
                //Bitmap bitmap = BitmapFactory.decodeFile(mediaPath);
                Bitmap bitmap = PhotoUtil.getThenumBitmap(imagePath, 480, 800);
                int bWidth = bitmap.getWidth();
                int bHeight = bitmap.getHeight();
                int height = bHeight * mScreenWidth / bWidth;
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) clickView.getLayoutParams();
                layoutParams.width = mScreenWidth;
                layoutParams.height = height;
                layoutParams.gravity =Gravity.CENTER;
                clickView.setLayoutParams(layoutParams);
                //iv.invalidate();
                bitmap.recycle();
            }
        }

    }

    //生成list
    private void listAddCode(List<MediaItem> pathList) {
        //存储每个imageView
        if (mFatherLayout.getChildCount() > 0) {
            mImageViewList.clear();
            for (int i = 0; i < mFatherLayout.getChildCount(); i++) {
                if (mFatherLayout.getChildAt(i) instanceof ImageView) {
                    mImageViewList.add(mFatherLayout.getChildAt(i));
                }
            }
        }
        //在插入的地方改变集合
        if (mFocuseView != null) {
            int indexFocus = mFatherLayout.indexOfChild(mFocuseView);
            for (int i = 0; i < mImageViewList.size(); i++) {
                if (mImageViewList.get(i) == mFatherLayout.getChildAt(indexFocus + 1)) {
                    for (int j = 0; j < pathList.size(); j++) {
                        HarwkinLogUtil.info("i = " + i + ",j = " + j + ",size = " + pathList.size());
                        mListImage.add((i + j/* - pathList.size()*/), pathList.get(j));
                        PictureTextItemInfo bean = new PictureTextItemInfo();
                        bean.type = ValueCommentType.PIC_TYPE;
                        bean.value = ((MediaItem) mImageViewList.get(i).getTag()).getMediaPath();
                        mPicListBeans.add((i + j/* - pathList.size()*/), bean);
                    }
                    break;
                }
            }
        } else {
            for (MediaItem item : pathList) {
                mListImage.add(item);
                PictureTextItemInfo bean = new PictureTextItemInfo();
                bean.type = ValueCommentType.PIC_TYPE;
                bean.value = item.getMediaPath();
                mPicListBeans.add(bean);
            }
        }

        //设置焦点
        if (mFatherLayout.getChildAt(mFatherLayout.getChildCount() - 1) instanceof EditText) {
            mFatherLayout.getChildAt(mFatherLayout.getChildCount() - 1).setFocusable(true);
            mFatherLayout.getChildAt(mFatherLayout.getChildCount() - 1).setFocusableInTouchMode(true);
            mFatherLayout.getChildAt(mFatherLayout.getChildCount() - 1).requestFocus();
            /*mFatherLayout.getChildAt(mFatherLayout.getChildCount() - 1).postDelayed(new Runnable() {
                @Override
                public void run() {
                    mFatherLayout.getChildAt(mFatherLayout.getChildCount() - 1).requestFocus();
                }
            }, 2000);*/
        }

    }

    //显示改变
    private void imageVisable(List<MediaItem> pathList) {
        if (mFatherLayout.getChildCount() > 0) {
            if (mFocuseView != null) {
                int indexFocus = mFatherLayout.indexOfChild(mFocuseView);
//                int child = mFatherLayout.indexOfChild(mCameraPop.getClickView());
                for (int i = 0; i < pathList.size(); i++) {
                    //ImageView iv = addImageView();
                    ImageView iv = addImageView(pathList.get(i).mediaPath);
                    iv.setTag(pathList.get(i));
                    mFatherLayout.addView(iv, indexFocus + (2 * i + 1));
                    mFatherLayout.addView(addEditView(), indexFocus + (2 * i + 2));
                    //imageHeightDeal(iv, pathList.get(i).mediaPath);
//                    ImageLoaderUtil.loadLocalImage(iv, pathList.get(i).mediaPath);
                    ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(pathList.get(i).mediaPath), iv);

                }
            } else {
                for (int i = 0; i < pathList.size(); i++) {
                    /*if (i == 0) {
                        mCameraPop.getClickView().setTag(pathList.get(0));
                        mFatherLayout.addView(mCameraPop.getClickView());
                        ImageLoaderUtil.loadLocalImage((ImageView) mCameraPop.getClickView(), pathList.get(0).mediaPath);
                    } else {
                        ImageView iv = addImageView();
                        iv.setTag(pathList.get(0));
                        mFatherLayout.addView(iv);
                        ImageLoaderUtil.loadLocalImage(iv, pathList.get(i).mediaPath);
                    }*/
                    ImageView iv = addImageView(pathList.get(i).mediaPath);
                    iv.setTag(pathList.get(i));
                    mFatherLayout.addView(iv);
//                    ImageLoaderUtil.loadLocalImage(iv, pathList.get(i).mediaPath);
                    ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(pathList.get(i).mediaPath), iv);

                    mFatherLayout.addView(addEditView());
                }
            }

        }
    }

    //宽高比设置
    private void imageHeightDeal(ImageView iv, String imagePath) {
        if(!StringUtil.isEmpty(imagePath)){
            File file = new File(imagePath);
            if(file.exists()){
                //Bitmap bitmap = BitmapFactory.decodeFile(mediaPath);
                //Bitmap bitmap = BitmapFactory.decodeFile(mediaPath);
                Bitmap bitmap = PhotoUtil.getThenumBitmap(imagePath, 480, 800);
                int bWidth = bitmap.getWidth();
                int bHeight = bitmap.getHeight();
                int height = bHeight * mScreenWidth / bWidth;
                ViewGroup.LayoutParams layoutParams = iv.getLayoutParams();
                layoutParams.width = mScreenWidth;
                layoutParams.height = height;
                iv.setLayoutParams(layoutParams);
                //iv.invalidate();
                bitmap.recycle();
            }
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
        mCameraPop.forResult(requestCode, resultCode, data);
    }

    //获取要上传图片的地址list
    private List<MediaItem> getImagePathList() {
        return mListImage;
    }

    //获取图文介绍总的list
    private List<PictureTextItemInfo> getContentList() {
        ArrayList<PictureTextItemInfo> list = new ArrayList<>();
        if (mFatherLayout.getChildCount() > 0) {
            for (int i = 0; i < mFatherLayout.getChildCount(); i++) {
                PictureTextItemInfo bean = new PictureTextItemInfo();
                if (mFatherLayout.getChildAt(i) instanceof EditText) {
                    bean.type = ValueCommentType.EDIT_TYPE;
                    bean.value = ((EditText) mFatherLayout.getChildAt(i)).getText().toString();
                    list.add(bean);
                } else if (mFatherLayout.getChildAt(i) instanceof ImageView) {
                    for (int j = 0; j < mImageViewList.size(); j++) {
                        if (mFatherLayout.getChildAt(i) == mImageViewList.get(j)) {
                            bean.type = ValueCommentType.PIC_TYPE;
                            bean.value = mListImage.get(j).getMediaPath();
                            list.add(bean);
                            break;
                        }
                    }

                }
            }
        }
        return list;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mDialogCancle != null) {
            mDialogCancle.dismiss();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            //判断是否有修改
            doCancleBack();

        }
        return true;
    }

    private void doCancleBack() {
        mContentList = getContentList();
        if (!isEdit(mContentList, mListDatas)) {
            finish();
        } else {
            mDialogCancle = DialogUtil.showMessageDialog(this, null, getString(R.string.release_detail_cancle_text),
                    getString(R.string.save), getString(R.string.release_detail_not_save), new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            onfinish();
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    });
            mDialogCancle.show();
            /*if (mContentList != null) {
                if (mContentList.size() == 1) {
                    if (StringUtil.isEmpty(mContentList.get(0).value)) {
                        finish();
                    } else {
                        mDialogCancle.show();
                    }
                } else {
                    mDialogCancle.show();
                }
            }*/
        }

    }

    //所有的值都为空的话，视为没有主页
    private Boolean isValue() {
        mContentList = getContentList();
        if (mContentList != null) {
            for (int i = 0; i < mContentList.size(); i++) {
                if (!StringUtil.isEmpty(mContentList.get(i).value)) {
                    return true;
                }
            }

        }
        return false;
    }

}
