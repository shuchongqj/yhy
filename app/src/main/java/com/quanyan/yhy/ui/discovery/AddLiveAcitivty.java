package com.quanyan.yhy.ui.discovery;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.harwkin.nb.camera.CameraManager;
import com.harwkin.nb.camera.PageBigImageActivity;
import com.harwkin.nb.camera.TimeUtil;
import com.harwkin.nb.camera.callback.SelectMoreListener;
import com.harwkin.nb.camera.options.CameraOptions;
import com.harwkin.nb.camera.pop.CameraPop;
import com.harwkin.nb.camera.pop.CameraPopListener;
import com.harwkin.nb.camera.type.OpenType;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.newyhy.activity.HomeMainTabActivity;
import com.newyhy.wsy.UgcInfo_Parcelable;
import com.newyhy.wsy.UpLoadUgcVideoService;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.util.DialogBuilder;
import com.quanyan.base.util.LocalUtils;
import com.quanyan.base.util.ScreenSize;
import com.quanyan.base.util.StringUtil;
import com.quanyan.base.view.BaseNavView;
import com.quanyan.base.view.LoadingDialog;
import com.quanyan.base.view.customview.NoScrollGridView;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.AnalyDataValue;
import com.quanyan.yhy.common.LiveType;
import com.quanyan.yhy.database.DBManager;
import com.quanyan.yhy.eventbus.EvBusShareSuccess;
import com.quanyan.yhy.libanalysis.AnEvent;
import com.quanyan.yhy.libanalysis.Analysis;
import com.quanyan.yhy.net.NetThreadManager;
import com.quanyan.yhy.ui.base.utils.DialogUtil;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.base.utils.TCEventHelper;
import com.quanyan.yhy.ui.base.views.MyPinchZoomImageView;
import com.quanyan.yhy.ui.club.clubcontroller.ClubController;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.discovery.adapter.SendSubjectImgGridAdapter;
import com.quanyan.yhy.ui.discovery.view.AddLiveEditText;
import com.quanyan.yhy.ui.shortvideo.MediaRecorderActivity;
import com.smart.sdk.api.resp.Api_SNSCENTER_POIInfo;
import com.smart.sdk.api.resp.Api_SNSCENTER_UgcInfo;
import com.yhy.common.beans.album.MediaItem;
import com.yhy.common.beans.net.model.PhotoData;
import com.yhy.common.beans.net.model.ShareBean;
import com.yhy.common.beans.net.model.VideoInfo;
import com.yhy.common.beans.net.model.club.POIInfo;
import com.yhy.common.beans.net.model.club.SubjectLive;
import com.yhy.common.beans.net.model.comment.ComTagInfo;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.CommonUtil;
import com.yhy.common.utils.SPUtils;
import com.yhy.imageloader.ImageLoadManager;
import com.yhy.location.LocationManager;
import com.yhy.service.IUserService;
import com.yixia.camera.model.MediaObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.greenrobot.event.EventBus;

import static com.newyhy.wsy.UpLoadUgcVideoService.MEDIA_PATH;
import static com.newyhy.wsy.UpLoadUgcVideoService.UGC_INFO;
import static com.quanyan.yhy.ui.base.utils.DialogUtil.showLoadingDialog;
import static com.umeng.commonsdk.stateless.UMSLEnvelopeBuild.mContext;

/**
 * “我要直播”发布
 * <p/>
 * Created by zhaoxp on 2015-10-29.
 */
public class AddLiveAcitivty extends BaseActivity implements AdapterView.OnItemClickListener,
        SendSubjectImgGridAdapter.PicNumChanged {

    private static final int ADD_LIVE_LOCATION = 1;//添加位置，请求码
    private static final int ADD_LIVE_LABEL = 2;//添加标签，请求码
    private static final int ADD_VIDEO_DFRAT = 3;//添加视频草稿箱
    public static final int ADD_VIDEO = 4;//添加视频
    @ViewInject(R.id.add_live_edit_content)
    private EditText mEditContent;
    @ViewInject(R.id.add_live_text_num_change)
    private TextView mEditTextNum;
    //    @ViewInject(R.id.add_live_addlocation_layout)
//    private LinearLayout mAddLocationLayout;
//    @ViewInject(R.id.add_live_addtopic_layout)
//    private TextView mAddTopicLayout;
//    @ViewInject(R.id.add_live_addlocation_text)
//    private TextView mAddLocationTv;
    //	@ViewInject(R.id.add_live_addtopic_text)
//	private TextView mAddTopicTv;
    @ViewInject(R.id.add_live_addtopic_content_layout)
    private LinearLayout mTopicTagLayout;//添加话题标签
    @ViewInject(R.id.add_live_addtopic_location_layout)
    private LinearLayout mTopicLocationLayout;//添加位置
    @ViewInject(R.id.add_live_pic_list_grid)
    private NoScrollGridView mNoScrollGridView;
//    @ViewInject(R.id.add_live_share_sina)
//    private ImageView mShareSina;
//    @ViewInject(R.id.add_live_share_wechat)
//    private ImageView mShareWechat;
//	private SelectImgGridAdapter mSelectImgGridAdapter;

    @ViewInject(R.id.ll_show_pic_array)
    private LinearLayout mShowPicturesParentView;
    @ViewInject(R.id.rl_show_video_thumb)
    private LinearLayout mShowVideoThumbParentView;
    @ViewInject(R.id.iv_video_thumbmail)
    private ImageView mVideoThumbView;
    @ViewInject(R.id.iv_video_thumbmail_delete)
    private ImageView mVideoDeleteView;
//    @ViewInject(R.id.tv_topic_tag)
//    private ImageView mTopicTagView;

    private SendSubjectImgGridAdapter mSelectImgGridAdapter;
    private CameraPop mCameraPop;
    private List<PhotoData> mData = new ArrayList<>();
    private ClubController mControler;
    //从外面传入
    private Intent mIntentData;
    private int mLiveType;
    private List<MediaItem> mPhotoList;
    private MediaItem mVideoFromGallery;//从相册选择过来的Video

    private ShareBean mShareData;
    protected com.yhy.common.base.LoadingDialog processDialog;
    private boolean isStep;

    protected com.yhy.common.base.LoadingDialog mLoadingDialog;

    @Autowired
    IUserService userService;

    @Override
    public void handleMessage(Message msg) {
        hideLoadingView();
        switch (msg.what) {
            case ValueConstants.MSG_PUBLISH_LIVE_OK:
                hideLoadingView();
                mBaseNavView.setRightEnable(true);
                if (isStep) {
                    EventBus.getDefault().post(new EvBusShareSuccess());
                }
                mEditContent.setText("");
                SPUtils.saveLiveDraft(getApplicationContext(), null);
                setResult(RESULT_OK);
                ToastUtil.showToast(this, getString(R.string.toast_publish_ok));
                if (!isFinishing()) {
                    finish();
                }
                break;
            case ValueConstants.MSG_UPLOADIMAGE_OK:
                hideLoadingView();
                mBaseNavView.setRightEnable(true);
                List<String> uploadFiles = (List<String>) msg.obj;
                if (msg.obj != null) {
                    try {
                        if (uploadFiles.size() > 0) {
                            int failcount = mData.size() - ((List<String>) msg.obj).size();
                            LogUtils.e("loza failcount = " + failcount + " mdata:" + mData.size() + " size():" + ((List<String>) msg.obj).size());
                            if (failcount == 0) {
                                postLive(uploadFiles, null);
                            } else {
                                ToastUtil.showToast(this, getString(R.string.toast_uploading_image_ko, failcount));
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    postLive(uploadFiles, null);
                }
                break;
            case ValueConstants.MSG_HIDELOADING_DIALOG:
                hideLoadingView();
                break;
            case ValueConstants.MSG_SHOW_DIALOG:
                showLoadingView(msg.obj instanceof String ? (String) msg.obj : "");
                break;
            case ValueConstants.MSG_UPLOADIMAGE_KO:
                hideLoadingView();
                mBaseNavView.setRightEnable(true);
                if (msg.obj != null) {
                    ToastUtil.showToast(this, (String) msg.obj);
                }
                break;
            case ValueConstants.MSG_UPLOAD_VIDEO_OK:
                hideLoadingView();
                mBaseNavView.setRightEnable(true);
                List<String> uploadVideos = (List<String>) msg.obj;
                if (msg.obj != null) {
                    if (uploadVideos.size() > 0) {
                        postLive(null, uploadVideos);
                    }
                } else {
                    ToastUtil.showToast(this, getString(R.string.label_video_upload_failed));
                }
                break;
            case ValueConstants.MSG_UPLOAD_VIDEO_KO:
                hideLoadingView();
                mBaseNavView.setRightEnable(true);
                if (msg.obj != null) {
                    ToastUtil.showToast(this, (String) msg.obj);
                }
                break;
        }
    }

    public static void gotoAddLiveActivity(Activity context, String topic) {
        Intent intent = new Intent(context, AddLiveAcitivty.class);
        if (!TextUtils.isEmpty(topic)) {
            intent.putExtra(SPUtils.EXTRA_TOPIC, topic);
        }
        context.startActivityForResult(intent, ValueConstants.POST_LIVE);
    }


    public static void gotoAddLiveActivity(Context context) {
        Intent intent = new Intent(context, AddLiveAcitivty.class);
        ((Activity) context).startActivityForResult(intent, ValueConstants.POST_LIVE);
    }

    public static void gotoAddLiveAcitivty(Activity context, int type, MediaObject mMediaObject) {
        Intent intent = new Intent(context, AddLiveAcitivty.class);
        intent.putExtra(SPUtils.EXTRA_SELECT_TYPE, type);
        if (mMediaObject != null) {
            intent.putExtra(SPUtils.EXTRA_MEDIAOBJECT, mMediaObject);

        }

        context.startActivityForResult(intent, ValueConstants.POST_LIVE);
    }

    /**
     * 从相册添加视频跳到发布页面
     *
     * @param context
     * @param video
     */
    public static void gotoAddLiveActivity(Activity context, MediaItem video) {
        Intent intent = new Intent(context, AddLiveAcitivty.class);
        intent.putExtra(SPUtils.EXTRA_MEDIA, video);
        context.startActivityForResult(intent, ValueConstants.POST_LIVE);
    }

    /**
     * 步步吸金跳转过来的
     *
     * @param context 上下文对象
     */
    public static void gotoAddLiveAcitivty(Activity context, ShareBean shareBean, boolean isStep) {
        Intent intent = new Intent(context, AddLiveAcitivty.class);
        intent.putExtra("isStep", isStep);
        if (shareBean != null) {
            intent.putExtra(SPUtils.EXTRA_SHARE_CONTENT, shareBean);
        }
        context.startActivityForResult(intent, ValueConstants.POST_LIVE);
    }

    /**
     * 跳转发布直播
     *
     * @param context 上下文对象
     */
    public static void gotoAddLiveAcitivty(Activity context, ShareBean shareBean) {
        Intent intent = new Intent(context, AddLiveAcitivty.class);
        if (shareBean != null) {
            intent.putExtra(SPUtils.EXTRA_SHARE_CONTENT, shareBean);
        }
        context.startActivityForResult(intent, ValueConstants.POST_LIVE);
    }

    /**
     * 跳转发布直播
     *
     * @param context
     * @param type
     * @param data
     */
    public static void gotoAddLiveAcitivty(Activity context, int type, Intent data, ArrayList<MediaItem> pathList) {
        Intent intent = new Intent(context, AddLiveAcitivty.class);
        intent.putExtra(SPUtils.EXTRA_SELECT_TYPE, type);
        if (data != null) {
            intent.putExtras(data);
        }
        if (pathList != null) {
            intent.putParcelableArrayListExtra(SPUtils.EXTRA_DATA, pathList);
        }
        context.startActivityForResult(intent, ValueConstants.POST_LIVE);
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.add_live_layout, null);
    }

    /**
     * 返回键处理
     */
    private void onBackKeyClick() {
       /* SPUtils.saveLiveDraft(getApplicationContext(), mEditContent.getText().toString());
        if (mVideoInfo != null && !DBManager.getInstance(AddLiveAcitivty.this).isCached(mVideoInfo)) {
            showCacheDialog();
        } else {
            finish();
        }*/
        deleteCacheVideo();
        finish();
    }

    private BaseNavView mBaseNavView;

    @Override
    public View onLoadNavView() {
        mBaseNavView = new BaseNavView(this);
        mBaseNavView.setLeftClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackKeyClick();
            }
        });
        mBaseNavView.setRightText(getString(R.string.label_btn_publish));
        mBaseNavView.setTitleText(getString(R.string.label_title_publish_live));
        mBaseNavView.setRightTextColor(R.color.neu_666666);
        mBaseNavView.setRightTextEnable(false);
        mBaseNavView.setRightTextClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBaseNavView.setRightEnable(false);
                //事件统计
                Analysis.pushEvent(mContext, isStep ? AnEvent.ZXSJ_SHARE_QUANZI_FABU : AnEvent.TOPIC_ISSUE);
                TCEventHelper.onEvent(AddLiveAcitivty.this, AnalyDataValue.Find_DirectRelease);
                String content = mEditContent.getText().toString();
                String postContent = StringUtil.sideTrim(content, "\n");
                if (TextUtils.isEmpty(postContent.trim()) && mSelectImgGridAdapter.getData().size() <= 0 && mVideoInfo == null && mVideoFromGallery == null) {
                    ToastUtil.showToast(AddLiveAcitivty.this, getString(R.string.label_add_dynamic_empty_notice));
                    mBaseNavView.setRightEnable(true);
                    return;
                }
                if (content.length() > 200) {
                    ToastUtil.showToast(AddLiveAcitivty.this, "不能超过200字");
                    mBaseNavView.setRightEnable(true);
                    return;
                }
                //showLoadingView(getString(R.string.label_dlg_msg_prepared));
                showLoadingView("发布中");
                String trendtype = "";
                if (mVideoInfo != null) {
                    uploadVideoAndImage();
                    trendtype = "小视频";
                } else if (mVideoFromGallery != null) {
                    trendtype = "小视频";

                    ToastUtil.showToast(AddLiveAcitivty.this, getString(R.string.start_upload_video));
                    Intent intentService = new Intent(AddLiveAcitivty.this, UpLoadUgcVideoService.class);
                    intentService.putExtra(MEDIA_PATH, mVideoFromGallery.getMediaPath());
                    intentService.putExtra(UGC_INFO, getUGCInfoForServiceUpLoad());
                    startService(intentService);
                    finish();
                } else if (mSelectImgGridAdapter.getData().size() > 0) {
                    Runnable requestThread = new Runnable() {
                        @Override
                        public void run() {
                            upLoadImage();
                        }
                    };
                    NetThreadManager.getInstance().execute(requestThread);
                    trendtype = "图文";

                } else {
                    trendtype = "文字";

                    postLive(null, null);
                }

                Analysis.pushEvent(AddLiveAcitivty.this, AnEvent.PublishConfirm,
                        new Analysis.Builder().
                                setLat(LocationManager.getInstance().getStorage().getLast_lat()).
                                setLng(LocationManager.getInstance().getStorage().getLast_lng()).
                                setTrendType(trendtype));

            }
        });
        return mBaseNavView;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }

    @Override
    public void finish() {
        super.finish();
        SPUtils.saveLiveDraft(getApplicationContext(), mEditContent.getText().toString());
        closeKeyboard();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            onBackKeyClick();
        }
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        SPUtils.saveLiveDraft(getApplicationContext(), mEditContent.getText().toString());
    }

    /**
     * 弹出是否缓存的确认对话框
     */
//	private Dialog mCacheDlg;
    private VideoCachePopView mCacheDlg;

    private void showCacheDialog() {
        if (mCacheDlg == null) {
            mCacheDlg = new VideoCachePopView(this, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.dialog_video_cache_quit:
                            deleteCacheVideo();
                            mCacheDlg.dismiss();
                            finish();
                            break;
                        case R.id.dialog_video_cache_while:
                            saveCache();
                            mCacheDlg.dismiss();
                            finish();
                            break;
                    }
                }
            });
        }
        //显示窗口
        mCacheDlg.showAtLocation(mNoScrollGridView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
    }

    /**
     * 初始化事件监听
     */
    private void initEvent() {
        mNoScrollGridView.setOnItemClickListener(this);
        mEditContent.addTextChangedListener(new TextWatcher() {
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
                    if (s.length() > 200) {
                        mEditTextNum.setTextColor(getResources().getColor(R.color.neu_fa4619));
                    } else {
                        mEditTextNum.setTextColor(getResources().getColor(R.color.tv_color_grayBD));
                    }
                    mEditTextNum.setText(s.length() + "/200字");
                    mBaseNavView.setRightTextEnable(true);
                    mBaseNavView.setRightTextColor(R.color.black);
                } else {
                    mEditTextNum.setText("0/200字");
                    if (mSelectImgGridAdapter.getDataSize() <= 0) {
                        mBaseNavView.setRightTextColor(R.color.neu_666666);
                        mBaseNavView.setRightTextEnable(false);
                    } else {
                        mBaseNavView.setRightTextEnable(true);
                        mBaseNavView.setRightTextColor(R.color.black);
                    }
                }
                Pattern pattern = Pattern.compile(AddLiveEditText.regex);
                Matcher matcher = pattern.matcher(content);
//                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(content);
                int find_index = 0;
                while (matcher.find()) {
                    String next = matcher.group();
                    int length = next.length();
                    int index = content.indexOf(next, find_index);
                    s.setSpan(new ForegroundColorSpan(Color.BLUE),
                            index,
                            index + length,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    find_index = index + length;
                }
            }
        });

        findViewById(R.id.add_live_photo_addimg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TCEventHelper.onEvent(AddLiveAcitivty.this, AnalyDataValue.DYNAMIC_ADD_PIC_BUTTON);
                if (mSelectImgGridAdapter.getData().size() > 0) {
                    mCameraPop.showMenuList(false, true);
                } else {
                    mCameraPop.showMenuList(true, true);
                }
                mCameraPop.showMenu(mNoScrollGridView);
            }
        });
    }

    /**
     * 初始化视图
     *
     * @param savedInstanceState
     */
    MediaObject mMediaObject;

    @Override
    protected void initView(Bundle savedInstanceState) {
        ViewUtils.inject(this);
        mControler = new ClubController(this, mHandler);

        mLiveType = getIntent().getIntExtra(SPUtils.EXTRA_SELECT_TYPE, -1);
        mMediaObject = (MediaObject) getIntent().getSerializableExtra(SPUtils.EXTRA_MEDIAOBJECT);
        isStep = getIntent().getBooleanExtra("isStep", false);

        mIntentData = getIntent();
        mPhotoList = getIntent().getParcelableArrayListExtra(SPUtils.EXTRA_DATA);
        mShareData = (ShareBean) getIntent().getSerializableExtra(SPUtils.EXTRA_SHARE_CONTENT);
        mVideoFromGallery = getIntent().getParcelableExtra(SPUtils.EXTRA_MEDIA);
        String draft = SPUtils.getLiveDraft(getApplicationContext());

        String EXTRA_TOPIC = getIntent().getStringExtra(SPUtils.EXTRA_TOPIC);
        //给话题前后加#
        if (!TextUtils.isEmpty(EXTRA_TOPIC)) {
            if (!(EXTRA_TOPIC.startsWith("#") && EXTRA_TOPIC.endsWith("#"))) {
                EXTRA_TOPIC = "#" + EXTRA_TOPIC + "#";
            }
        }
        if (!TextUtils.isEmpty(EXTRA_TOPIC)) {
            mEditContent.setText(EXTRA_TOPIC);
        } else {
            if (!TextUtils.isEmpty(draft)) {
                mEditContent.setText(draft);
                mEditContent.setSelection(draft.length());
                mEditContent.requestFocus();
//			mEditTextNum.setText(draft.length() + "/200字");
                mBaseNavView.setRightTextEnable(true);
                mBaseNavView.setRightTextColor(R.color.black);
            }
        }
        String content = mEditContent.getText().toString();
        Pattern pattern = Pattern.compile(AddLiveEditText.regex);
        Matcher matcher = pattern.matcher(content);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(content);
        int find_index = 0;
        while (matcher.find()) {
            String next = matcher.group();
            int length = next.length();
            int index = content.indexOf(next, find_index);
            spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.BLUE),
                    index,
                    index + length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            find_index = index + length;
        }
        mEditContent.setText(spannableStringBuilder);
        mEditContent.setSelection(content.length());

        mSelectImgGridAdapter = new SendSubjectImgGridAdapter(this);
        mSelectImgGridAdapter.setPicNumChanged(this);
        mNoScrollGridView.setAdapter(mSelectImgGridAdapter);

//		List<String> t = getIntent().getStringArrayListExtra(PageBigImageActivity.IMAGE_LIST_DATA);
        mCameraPop = new CameraPop(this, new CameraPopListener() {
            @Override
            public void onCamreaClick(CameraOptions options) {
                if (LocalUtils.isAlertMaxStorage()) {
                    ToastUtil.showToast(AddLiveAcitivty.this, getString(R.string.label_toast_sdcard_unavailable));
                    return;
                }
                options.setOpenType(OpenType.OPEN_CAMERA);
                mCameraPop.process();
            }

            @Override
            public void onPickClick(CameraOptions options) {
                options.setOpenType(OpenType.OPENN_USER_ALBUM).setMaxSelect(ValueConstants.MAX_SELECT_PICTURE - mSelectImgGridAdapter.getDataSize());
                mCameraPop.processWithMedia();
            }

            @Override
            public void onDelClick() {

            }

            @Override
            public void onVideoClick() {
//                startRecordActivity();
                Intent intent = new Intent(AddLiveAcitivty.this, MediaRecorderActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(SPUtils.EXTRA_TYPE, 2);
                intent.putExtras(bundle);
                startActivityForResult(intent, AddLiveAcitivty.ADD_VIDEO);
            }

            /*@Override
            public void onVideoDraftClick() {
                NavUtils.gotoVideoListActivity(AddLiveAcitivty.this, ADD_VIDEO_DFRAT);
            }*/
        }, new SelectMoreListener() {
            @Override
            public void onSelectedMoreListener(List<MediaItem> pathList) {
                selectPhotoOk(pathList);
                if (null != processDialog) processDialog.dismiss();
            }
        });
        mCameraPop.showMenuList(true, true);

        initEvent();

        handleTokenFromLastPage();

        handleShareData();

        //处理相册中选择的小视频
        handleVideoFromGallery();
    }

    //处理分享达人圈的内容
    private void handleShareData() {
        if (mShareData == null) {
            return;
        }
        mEditContent.setText("");
        if (!StringUtil.isEmpty(mShareData.shareContent)) {
            mEditContent.append(mShareData.shareContent);
        }
        if (!StringUtil.isEmpty(mShareData.shareWebPage)) {
            mEditContent.append(mShareData.shareWebPage);
        }
        if (!StringUtil.isEmpty(mShareData.shareImageLocal)) {
            if (mPhotoList != null) {
                mPhotoList.clear();
            } else {
                mPhotoList = new ArrayList<>();
            }
            MediaItem mediaItem = new MediaItem();
            mediaItem.setMediaPath(mShareData.shareImageLocal);
            mPhotoList.add(mediaItem);
            refreshPhotos(mPhotoList);
        }
    }

    /**
     * 处理相册中选择的小视频
     */
    private void handleVideoFromGallery() {
        if (null != mVideoFromGallery) {
            findViewById(R.id.ac_add_live_photo_add_layout).setVisibility(View.GONE);
            mShowVideoThumbParentView.setVisibility(View.VISIBLE);
            mShowPicturesParentView.setVisibility(View.GONE);
            //利用 ContentProvider 去查询视频文件缩略图，优点是速度快，缺点：有时候获取不到所有视频的缩略图
            //可以利用Glide直接加载视频源路径显示原视频的第一帧图
            if (null != mVideoFromGallery.thumbnailPath) {
                ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(mVideoFromGallery.thumbnailPath), mVideoThumbView);
            } else {
                ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(mVideoFromGallery.mediaPath), mVideoThumbView);
            }
            mVideoDeleteView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    findViewById(R.id.ac_add_live_photo_add_layout).setVisibility(View.VISIBLE);
                    mShowVideoThumbParentView.setVisibility(View.GONE);
                    mCameraPop.showMenuList(true, true);
                    mVideoFromGallery = null;
                    mBaseNavView.setRightText(getString(R.string.label_btn_publish));
                }
            });

            mVideoThumbView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VideoInfo videoInfo = new VideoInfo();
                    //从系统相册获取的视屏转成 VideoPlayerActivity 需要的VideoInfo
                    videoInfo.videoLocalPath = mVideoFromGallery.mediaPath;
                    videoInfo.createDate = mVideoFromGallery.mediaCreateTime;
                    videoInfo.duration = mVideoFromGallery.duration;
                    if (null != mVideoFromGallery.thumbnailPath)
                        videoInfo.videoThumbLocalPath = mVideoFromGallery.thumbnailPath;
                    NavUtils.gotoVideoPlayerctivty(AddLiveAcitivty.this, videoInfo);
                }
            });
        }
    }

    /**
     * 处理上一个页面带来的数据
     */
    private void handleTokenFromLastPage() {
        if (mLiveType == -1) {
            return;
        }
        switch (mLiveType) {
            case LiveType.ADD_CAMERA:
                if (mPhotoList != null) {
                    refreshPhotos(mPhotoList);
                }
                break;
            case LiveType.ADD_PICTURE:
                if (mPhotoList != null) {
                    refreshPhotos(mPhotoList);
                }
                break;
            case LiveType.ADD_VIDEO:
                if (mMediaObject != null) {
                    refreshVideo(mMediaObject);
                }
                break;
            /*case LiveType.ADD_VIDEO_DRAFT:
                if (mIntentData != null) {
                    refreshVideoDraft(mIntentData);
                }
                break;*/
        }
    }

    /**
     * 更新已选图片
     *
     * @param pathList
     */
    private void selectPhotoOk(List<MediaItem> pathList) {
        if (pathList != null) {
            findViewById(R.id.ac_add_live_photo_add_layout).setVisibility(View.GONE);
            showPictureView();
            mSelectImgGridAdapter.addAll(PhotoData.ImageItem2PhotoData(pathList));
        }
    }

    /**
     * 查看大图的返回
     *
     * @param data
     */
    private void refreshBigPicture(Intent data) {
        List<String> t = data.getStringArrayListExtra(PageBigImageActivity.IMAGE_LIST_DATA);
        if (PhotoData.String2PhotoData(t) != null) {
            //						label_label_post_0_9_pictures
//						String text = getString(R.string.label_label_post_0_9_pictures);
//						((TextView)findViewById(R.id.add_live_pic_label)).setText(text.substring(0, text.indexOf("或")));

            mSelectImgGridAdapter.replaceAll(PhotoData.String2PhotoData(t));
        }
    }

    /**
     * 刷新地址的返回
     *
     * @param data
     */
    private void refreshLocation(Intent data) {
        Bundle b = data.getExtras();
        mPoiLocation = (PoiLocation) b.get(SPUtils.EXTRA_SELECT_CITY);
        mTopicLocationLayout.removeAllViews();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final LinearLayout layout = new LinearLayout(getApplicationContext());
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setLayoutParams(layoutParams);
        layout.setGravity(Gravity.CENTER_VERTICAL);
        TextView textView = new TextView(getApplicationContext());
        if (mPoiLocation != null) {
            textView.setText(mPoiLocation.getTitle());
        } else {
            //不显示位置
            textView.setText(getString(R.string.add_topic_location_notshow));
        }
        textView.setTextSize(15);
        textView.setTextColor(getResources().getColor(R.color.main));
        ImageView imageView = new ImageView(getApplicationContext());
        imageView.setImageResource(R.mipmap.icon_delete);
        layout.addView(textView);
        layout.addView(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTopicLocationLayout.removeView(layout);
                if (mTopicLocationLayout.getChildCount() <= 0) {
                    TextView textView = new TextView(getApplicationContext());
                    textView.setText(getString(R.string.add_topic_location_default));
                    textView.setTextSize(12);
                    textView.setTextColor(getResources().getColor(R.color.tv_color_gray9));
                    mTopicLocationLayout.addView(textView);
                }
            }
        });
        mTopicLocationLayout.addView(layout);
    }

    /**
     * 刷新短视频拍摄返回
     */
    private void refreshVideo(MediaObject mMediaObject) {
        if (mMediaObject != null) {
            this.mMediaObject = mMediaObject;

//            mVideoResult = new EditorResult(mVideoData);
            mVideoInfo = convertEditResultToVideoInfo();
            if (mVideoInfo != null) {
                /*if (!DBManager.getInstance(this).isCached(mVideoInfo)) {
                    mBaseNavView.setRightImg(R.mipmap.ic_save);
                    mBaseNavView.showSaveImg();
                    mBaseNavView.setRightImgClick(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            saveCache();
                            mBaseNavView.setRightText(getString(R.string.label_btn_publish));
                        }
                    });
                } else {
                    mBaseNavView.setRightText(getString(R.string.label_btn_publish));
                }*/
                mBaseNavView.setRightText(getString(R.string.label_btn_publish));
                showVideoView();

                mBaseNavView.setRightTextEnable(true);
                mBaseNavView.setRightTextColor(R.color.black);
            }
        }
    }

    /**
     * 刷新短视频草稿箱的返回
     *
     * @param data
     */
    /*private void refreshVideoDraft(Intent data) {
        if (data != null) {
            mVideoInfo = (VideoInfo) data.getSerializableExtra(SPUtils.EXTRA_DATA);
            if (mVideoInfo != null) {
                if (!DBManager.getInstance(this).isCached(mVideoInfo)) {
                    mBaseNavView.setRightImg(R.mipmap.ic_save);
                    mBaseNavView.showSaveImg();
                    mBaseNavView.setRightImgClick(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            saveCache();
                            mBaseNavView.setRightText(getString(R.string.label_btn_publish));
                        }
                    });
                } else {
                    mBaseNavView.setRightText(getString(R.string.label_btn_publish));
                }
                showVideoView();

                mBaseNavView.setRightTextEnable(true);
                mBaseNavView.setRightTextColor(R.color.black);
            }
        }
    }*/

    /**
     * 刷新相册返回
     *
     * @param data
     */
    private void refreshPhotos(List<MediaItem> data) {
        selectPhotoOk(data);
    }

    /**
     * 刷新标签
     *
     * @param data
     */
    private void refreshLabels(Intent data) {
        mChooseTagInfo = (ComTagInfo) data.getSerializableExtra(SPUtils.EXTRA_ADD_LIVE_LABEL);
//					String text = data.getStringExtra(SPUtils.EXTRA_ADD_LIVE_LABEL);
        if (mChooseTagInfo != null) {
            mTopicTagLayout.removeAllViews();
//						mAddTopicTv.setText(text);
//						mAddTopicTv.setTextColor(getResources().getColor(R.color.black));
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            final LinearLayout layout = new LinearLayout(getApplicationContext());
            layout.setOrientation(LinearLayout.HORIZONTAL);
            layout.setLayoutParams(layoutParams);
            layout.setGravity(Gravity.CENTER_VERTICAL);
            TextView textView = new TextView(getApplicationContext());
            textView.setText(mChooseTagInfo.name);
            textView.setTextSize(15);
            textView.setTextColor(getResources().getColor(R.color.main));
            ImageView imageView = new ImageView(getApplicationContext());
            imageView.setImageResource(R.mipmap.icon_delete);
            layout.addView(textView);
            layout.addView(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mTopicTagLayout.removeView(layout);
                    if (mTopicTagLayout.getChildCount() <= 0) {
                        TextView textView = new TextView(getApplicationContext());
                        textView.setText(getString(R.string.add_topic_label_default));
                        textView.setTextSize(12);
                        textView.setTextColor(getResources().getColor(R.color.tv_color_gray9));
                        mTopicTagLayout.addView(textView);
                    }
                }
            });
            mTopicTagLayout.addView(layout);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PageBigImageActivity.REQ_CHOOSE_MAP: {
                mCameraPop.showMenuList(false, true);
                if (Activity.RESULT_OK == resultCode) {
                    refreshBigPicture(data);
                }
                break;
            }
            case ADD_LIVE_LOCATION: {
                if (Activity.RESULT_OK == resultCode) {
                    refreshLocation(data);
                }
                break;
            }
            case ADD_LIVE_LABEL: {
                if (Activity.RESULT_OK == resultCode) {
//                    refreshLabels(data);
                    String topicString = data.getStringExtra(SPUtils.EXTRA_ADD_LIVE_LABEL);
                    mEditContent.append(topicString);

                    String content = mEditContent.getText().toString();
                    Pattern pattern = Pattern.compile(AddLiveEditText.regex);
                    Matcher matcher = pattern.matcher(content);
                    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(content);
                    int find_index = 0;
                    while (matcher.find()) {
                        Log.i("regex", "find!!!!");
                        String next = matcher.group();
                        int length = next.length();
                        int index = content.indexOf(next, find_index);
                        Log.i("regex", "find!!!!" + length + ",   " + index);
                        spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.BLUE),
                                index,
                                index + length,
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        find_index = index + length;
                    }
                    mEditContent.setText(spannableStringBuilder);
                    mEditContent.setSelection(content.length());
                }

                break;
            }
            case ADD_VIDEO:
                mCameraPop.showMenuList(true, false);
                mSelectImgGridAdapter.clear();
                if (resultCode == RESULT_OK) {
                    refreshVideo((MediaObject) data.getSerializableExtra(SPUtils.EXTRA_MEDIAOBJECT));
                }
                break;
            /*case ADD_VIDEO_DFRAT:
                mCameraPop.showMenuList(true, false);
                mSelectImgGridAdapter.clear();
                if (resultCode == RESULT_OK) {
                    refreshVideoDraft(data);
                }
                break;*/
        }

        //相册相关
        if (requestCode == CameraManager.OPEN_CAMERA_CODE && resultCode == -1) {
            if (processDialog == null)
                processDialog = showLoadingDialog(this, getString(R.string.picture_process), true);
            processDialog.show();
            mCameraPop.forResult(requestCode, resultCode, data);
        } else if (requestCode == CameraManager.OPEN_USER_ALBUM && resultCode == -1) {
            mCameraPop.forResult(requestCode, resultCode, data);
        } else if (requestCode == CameraManager.OPEN_USER_ALBUM && resultCode == CameraManager.GET_VIDEO) {
            mVideoFromGallery = data.getParcelableExtra(SPUtils.EXTRA_VIDEO);
            handleVideoFromGallery();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 实体转换
     *
     * @return
     */
    private VideoInfo convertEditResultToVideoInfo() {
        VideoInfo videoInfo = new VideoInfo();

//        videoInfo.createDate = mVideoResult.getTimestamp();
        videoInfo.duration = mMediaObject.getDuration() / (1000 * 1000);
        videoInfo.videoLocalPath = mMediaObject.getOutputTempVideoPath();
        videoInfo.videoThumbLocalPath = mMediaObject.getOutputVideoThumbPath();
        videoInfo.id = System.currentTimeMillis();
        return videoInfo;
    }

    /**
     * 保存到草稿箱
     */
    private VideoInfo mVideoInfo;
    private Dialog mCacheSuccees;

    private void saveCache() {
        if (mVideoInfo != null) {
            DBManager.getInstance(this).addOrUpdateVideo(mVideoInfo);
            mCacheSuccees = new DialogBuilder(this)
                    .setCancelable(false)
                    .setCanceledOnTouchOutside(true)
                    .setContentViewId(R.layout.dialog_cache_video)
                    .setStyle(R.style.kangzai_dialog)
                    .setWidth(ScreenSize.getScreenWidth(getApplicationContext()) * 2 / 3)
                    .build();
            mCacheSuccees.show();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mCacheSuccees.dismiss();
                }
            }, 2000);
//			ToastUtil.showToast(this,getString(R.string.label_toast_video_cache_ok));
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int count = mSelectImgGridAdapter.getDataSize();
        if (ValueConstants.MAX_SELECT_PICTURE > count && position == mSelectImgGridAdapter.getCount() - 1) {
            TCEventHelper.onEvent(AddLiveAcitivty.this, AnalyDataValue.PUB_LIVE_ADD_PICTURES);
            if (mSelectImgGridAdapter.getData().size() > 0) {
                mCameraPop.showMenuList(false, true);
            } else {
                mCameraPop.showMenuList(true, true);
            }
            mCameraPop.showMenu(mNoScrollGridView);
            return;
        } else {
            if (mSelectImgGridAdapter.getItem(position) instanceof PhotoData) {
                ArrayList<String> data = new ArrayList<String>();
                for (PhotoData photoData : mSelectImgGridAdapter.getData()) {
                    data.add(photoData.mLocalUrl);
                }
                startActivityForResult(PageBigImageActivity.getIntent(AddLiveAcitivty.this, data,
                        position, true, true, MyPinchZoomImageView.Mode.NONE.ordinal()), PageBigImageActivity.REQ_CHOOSE_MAP);
            }
        }
    }

    /**
     * 发布直播
     *
     * @param uploadFiles
     */
    private void postLive(List<String> uploadFiles, List<String> video) {
        mControler.doPublishNewSubjectLive(AddLiveAcitivty.this, getUGCInfo(uploadFiles, video));
    }

    private PoiLocation mPoiLocation;//选择的位置 数据
    private ComTagInfo mChooseTagInfo;//选择的标签数据

    /**
     * 获取发帖数据
     *
     * @return
     */
    private SubjectLive getSnsData(List<String> uploadFiles, List<String> video) {
        SubjectLive mInfo = new SubjectLive();
        //标签ID
        mInfo.objId = mChooseTagInfo == null ? 0 : mChooseTagInfo.id;
        if (mChooseTagInfo != null && mChooseTagInfo.name != null) {
            mInfo.tagName = mChooseTagInfo.name;
        }
        //发布者ID
        mInfo.createId = userService.getLoginUserId();
        if (mInfo.poiInfo == null) {
            mInfo.poiInfo = new POIInfo();
            if (mPoiLocation != null) {
                mInfo.poiInfo.detail = mPoiLocation.getTitle();
                mInfo.poiInfo.latitude = mPoiLocation.getLatitude();
                mInfo.poiInfo.longitude = mPoiLocation.getLongitude();
            }
        }
        String postContent = StringUtil.sideTrim(mEditContent.getText().toString(), "\n");
        mInfo.textContent = postContent;
        if (uploadFiles != null) {
            mInfo.picList = uploadFiles;
        }
        if (video != null && video.size() > 0) {
            if (video.size() > 0) {
                mInfo.videoUrl = video.get(0);
            }
            if (video.size() > 1) {
                mInfo.videoPicUrl = video.get(1);
            }
        }
        return mInfo;
    }

    public Api_SNSCENTER_UgcInfo getUGCInfo(List<String> uploadFiles, List<String> video) {
        Api_SNSCENTER_UgcInfo mInfo = new Api_SNSCENTER_UgcInfo();
        //发布者ID
        mInfo.userId = userService.getLoginUserId();
        if (mInfo.poiInfo == null) {
            mInfo.poiInfo = new Api_SNSCENTER_POIInfo();
            if (mPoiLocation != null) {
                mInfo.poiInfo.detail = mPoiLocation.getTitle();
                mInfo.poiInfo.latitude = mPoiLocation.getLatitude();
                mInfo.poiInfo.longitude = mPoiLocation.getLongitude();
            }
        }
        String postContent = StringUtil.sideTrim(mEditContent.getText().toString(), "\n");
        mInfo.textContent = postContent;
        if (uploadFiles != null) {
            mInfo.picList = uploadFiles;
        }
        if (video != null && video.size() > 0) {
            if (video.size() > 0) {
                mInfo.videoUrl = video.get(0);
            }
            if (video.size() > 1) {
                mInfo.videoPicUrl = video.get(1);
            }
            mInfo.shortVideoType = "RECORD_VIDEO";
        }
        return mInfo;
    }

    /**
     * 该方法是提供给后台上传视频的Service 一个 Api_SNSCENTER_UgcInfo Ugc发布的信息实体
     *
     * @return
     */
    public UgcInfo_Parcelable getUGCInfoForServiceUpLoad() {
        UgcInfo_Parcelable mInfo = new UgcInfo_Parcelable();
        //发布者ID
        mInfo.userId = userService.getLoginUserId();
        if (mInfo.poiInfo == null) {
            mInfo.poiInfo = new UgcInfo_Parcelable.UgcInfo_POIInfo();
            if (mPoiLocation != null) {
                mInfo.poiInfo.detail = mPoiLocation.getTitle();
                mInfo.poiInfo.latitude = mPoiLocation.getLatitude();
                mInfo.poiInfo.longitude = mPoiLocation.getLongitude();
            }
        }
        String postContent = StringUtil.sideTrim(mEditContent.getText().toString(), "\n");
        mInfo.textContent = postContent;
        mInfo.shortVideoType = "UPLOAD_VIDEO";
        return mInfo;
    }

    /**
     * 上传视频和视频缩略图
     */
    private void uploadVideoAndImage() {
        if (mVideoInfo == null) {
            return;
        }
        mControler.uploadVideoAndImage(AddLiveAcitivty.this, mVideoInfo.videoLocalPath, mVideoInfo.videoThumbLocalPath);
    }

    /**
     * 上传多张图片
     */
    private void upLoadImage() {
        mData = mSelectImgGridAdapter.getData();
        String upurl[];
        if (mData != null && mData.size() > 0) {
            upurl = new String[mData.size()];
            for (int i = 0; i < mData.size(); i++) {
                upurl[i] = mData.get(i).mLocalUrl;
            }
            mControler.toUploadImage(AddLiveAcitivty.this, upurl);
        } else {
            mControler.sendUploadImage();
        }
    }

    @OnClick({R.id.add_live_addlocation_layout, R.id.add_live_addtopic_layout, R.id.tv_topic_tag})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.add_live_addlocation_layout:
                TCEventHelper.onEvent(AddLiveAcitivty.this, AnalyDataValue.DYNAMIC_LOCATION_CLICK);
                //添加位置
                if (!LocalUtils.isPermittedBySystem(this)) {
                    ToastUtil.showToast(this, getString(R.string.label_toast_disable_gps));
                    return;
                }
                NavUtils.gotoAddLocation(this, ADD_LIVE_LOCATION);
                break;
//            case R.id.add_live_addtopic_layout:
            case R.id.tv_topic_tag:
                //添加标签
                TCEventHelper.onEvent(AddLiveAcitivty.this, AnalyDataValue.DYNAMIC_TOPIC_CLICK);
                NavUtils.gotoAddTopic(this, ADD_LIVE_LABEL);
                break;
        }
    }

    @Override
    public void onPicNumChange(List<PhotoData> data) {
        if (TextUtils.isEmpty(mEditContent.getText().toString())) {
            if (data.size() > 0) {
                mBaseNavView.setRightTextEnable(true);
                mBaseNavView.setRightTextColor(R.color.black);
                mCameraPop.showMenuList(false, true);
            } else {
                mCameraPop.showMenuList(true, false);
//				String text = getString(R.string.label_label_post_0_9_pictures);
//				((TextView)findViewById(R.id.add_live_pic_label)).setText(text);
                findViewById(R.id.ac_add_live_photo_add_layout).setVisibility(View.VISIBLE);
                mShowPicturesParentView.setVisibility(View.GONE);
                mCameraPop.showMenuList(true, true);
                mBaseNavView.setRightTextEnable(false);
                mBaseNavView.setRightTextColor(R.color.neu_666666);
            }
        }
    }

    /**
     * 显示视频添加后页面
     */
    private void showVideoView() {
        findViewById(R.id.ac_add_live_photo_add_layout).setVisibility(View.GONE);
        mShowVideoThumbParentView.setVisibility(View.VISIBLE);
        mShowPicturesParentView.setVisibility(View.GONE);

//        ImageLoaderUtil.loadLocalImage(mVideoThumbView, mVideoInfo.videoThumbLocalPath);
        ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(mVideoInfo.videoThumbLocalPath), mVideoThumbView);
        mVideoDeleteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//				showPictureView();
                findViewById(R.id.ac_add_live_photo_add_layout).setVisibility(View.VISIBLE);
                mShowVideoThumbParentView.setVisibility(View.GONE);
                mCameraPop.showMenuList(true, true);
                mVideoInfo = null;
                mBaseNavView.setRightText(getString(R.string.label_btn_publish));
            }
        });

        mVideoThumbView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavUtils.gotoVideoPlayerctivty(AddLiveAcitivty.this, mVideoInfo);
            }
        });
    }

    /**
     * 删除缓存的视频文件和缩略图
     */
    private void deleteCacheVideo() {
        if (mVideoInfo == null) {
            return;
        }

        if (!DBManager.getInstance(this).isCached(mVideoInfo)) {
            com.harwkin.nb.camera.FileUtil.deleteFile(new File(mVideoInfo.videoLocalPath));
            com.harwkin.nb.camera.FileUtil.deleteFile(new File(mVideoInfo.videoThumbLocalPath));
        }

        mVideoInfo = null;
    }

    /**
     * 显示图片添加后页面
     */
    private void showPictureView() {
        mShowVideoThumbParentView.setVisibility(View.GONE);
        mShowPicturesParentView.setVisibility(View.VISIBLE);
    }

    /**
     * 关闭软键盘
     */
    private void closeKeyboard() {
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    public void showLoadingView(String msg) {
        if (mLoadingDialog == null) {
            mLoadingDialog = showLoadingDialog(this, msg, true);
        }

        mLoadingDialog.setDlgMessage(msg);

        if (this.isFinishing()) {
            return;
        }
        if (!mLoadingDialog.isShowing()) {
            mLoadingDialog.show();
        }
    }

    private static com.yhy.common.base.LoadingDialog showLoadingDialog(Context context, String message, boolean isCancelable) {
        com.yhy.common.base.LoadingDialog dialog = new com.yhy.common.base.LoadingDialog(context, com.yhy.common.R.style.loading_dialog);
        //沉浸式状态栏的设置
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = dialog.getWindow();
// Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
// Translucent navigation bar
//            window.setFlags(
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        dialog.setCancelable(isCancelable);
        dialog.setDlgMessage(message);
//		InterestMultiPageDialog.getWindow().setType((WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
        return dialog;
    }

    public void hideLoadingView() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }
}
