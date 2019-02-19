package com.quanyan.yhy.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.harwkin.nb.camera.ImageUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.AnalyDataValue;
import com.quanyan.yhy.eventbus.EvBusShareSuccess;
import com.quanyan.yhy.net.NetThreadManager;
import com.quanyan.yhy.ui.adapter.ShareAdapter;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.base.utils.TCEventHelper;
import com.quanyan.yhy.ui.club.clubcontroller.ClubController;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.util.MobileUtil;
import com.smart.sdk.api.resp.Api_SNSCENTER_UgcInfo;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.yhy.common.base.NoLeakHandler;
import com.yhy.common.beans.net.model.ShareBean;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.AndroidUtils;
import com.yhy.common.utils.SPUtils;
import com.yhy.router.YhyRouter;
import com.yhy.service.IUserService;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;


/**
 * Created with Android Studio.
 * <p>
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:baojie
 * Date:2015-12-30
 * Time:17:23
 * Version 1.0
 */
public class ShareTableActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener, NoLeakHandler.HandlerCallback {

    @ViewInject(R.id.hg_gridview)
    private GridView gridView;

    @ViewInject(R.id.ll_other)
    private LinearLayout llOther;

    @ViewInject(R.id.ll_share_layout)
    private LinearLayout mLLShareParentView;

    @ViewInject(R.id.tv_cancle)
    private TextView mTVCancle;

    @ViewInject(R.id.cb_sync_dynamic)
    private CheckBox mSyncDynamicView;

    @Autowired
    IUserService userService;

    private String dlgTilte = null;
    private String dlgMessage = null;
    private String shareTitle = null;
    private String shareContent = null;
    private String shareImageUrl = null;
    private String shareImageLocal = null;
    private String shareWebPage = null;
    private Bitmap thumb = null;
    UMImage urlImage = null;
    private ShareBean shareBean;

    public static final int WEIXIN = 1;
    public static final int WEIXIN_CIRCLE = 2;
    public static final int QQ = 3;
    public static final int WEIBO = 4;
    public static final int QUANYAN = 5;
    private String mShareType;
    //分享方式
    private int mShareWay;

    private UMShareAPI umShareAPI;

    private ClubController mControler;

    private boolean isOnlyImg;
    private boolean isStep;

    public static void gotoShareTableActivity(Context context, ShareBean shareBean, String type) {
        Intent intent = new Intent(context, ShareTableActivity.class);
        intent.putExtra(SPUtils.EXTRA_TYPE, type);
        intent.putExtra(SPUtils.EXTRA_DATA, shareBean);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.silde_out_null_ani, R.anim.silde_out_null_ani);
//        ((Activity) context).overridePendingTransition(R.anim.push_up_in1, R.anim.push_up_out1);
    }

    public static void gotoShareTableActivity(Context context, ShareBean shareBean, boolean isOnlyImg) {
        Intent intent = new Intent(context, ShareTableActivity.class);
        intent.putExtra("isOnlyImg", isOnlyImg);
        intent.putExtra(SPUtils.EXTRA_DATA, shareBean);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.silde_out_null_ani, R.anim.silde_out_null_ani);
//        ((Activity) context).overridePendingTransition(R.anim.push_up_in1, R.anim.push_up_out1);
    }


    public static void gotoShareTableActivity(Context context, ShareBean shareBean, boolean isOnlyImg, boolean isStep) {
        Intent intent = new Intent(context, ShareTableActivity.class);
        intent.putExtra("isOnlyImg", isOnlyImg);
        intent.putExtra("isStep", isStep);
        intent.putExtra(SPUtils.EXTRA_DATA, shareBean);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.silde_out_null_ani, R.anim.silde_out_null_ani);
//        ((Activity) context).overridePendingTransition(R.anim.push_up_in1, R.anim.push_up_out1);
    }

    @Override
    public void finish() {
        super.finish();
//        overridePendingTransition(R.anim.silde_out_null_ani, R.anim.silde_out_null_ani);
//        overridePendingTransition(R.anim.anim_not_change, R.anim.anim_pop_out);
        overridePendingTransition(R.anim.anim_not_change, R.anim.anim_not_change);

    }

    NoLeakHandler mHandler = new NoLeakHandler(this);

    @Override
    public void handleMessage(Message msg) {
//        hideLoadingView();
        switch (msg.what) {
            case ValueConstants.MSG_PUBLISH_LIVE_OK:
//                setResult(RESULT_OK);
//                ToastUtil.showToast(this, getString(R.string.toast_publish_ok));
//                finish();
                break;
            case ValueConstants.MSG_UPLOADIMAGE_OK:
                List<String> uploadFiles = (List<String>) msg.obj;
                if (msg.obj != null) {
                    try {
                        if (uploadFiles.size() > 0) {
                            postLive(uploadFiles.get(0));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        finish();
                    }
                } else {
                    postLive(null);
                }
                break;
            case ValueConstants.MSG_UPLOADIMAGE_KO:
                if (msg.obj != null) {
                    ToastUtil.showToast(this, (String) msg.obj);
                }
                break;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_share_normal);
        YhyRouter.getInstance().inject(this);
        ViewUtils.inject(this);
//        setTitleBarBackground(Color.TRANSPARENT);
        mControler = new ClubController(this, mHandler);

        mShareType = getIntent().getStringExtra(SPUtils.EXTRA_TYPE);
        shareBean = (ShareBean) getIntent().getSerializableExtra(SPUtils.EXTRA_DATA);
        isOnlyImg = getIntent().getBooleanExtra("isOnlyImg", false);
        isStep = getIntent().getBooleanExtra("isStep", false);
        if (shareBean != null) {
            mShareWay = shareBean.shareWay;
        }
        initView();
    }

    private void initView() {
        configPlatforms();
        ShareAdapter adapter = new ShareAdapter(this);
        gridView.setAdapter(adapter);
        initShareData();
        initEvent();

        if (mShareWay <= 0) {
            mSyncDynamicView.setChecked(shareBean.isNeedSyncToDynamic);
            if (shareBean.isNeedSyncToDynamic) {
                mSyncDynamicView.setVisibility(View.VISIBLE);
            } else {
                mSyncDynamicView.setVisibility(View.GONE);
            }
            mLLShareParentView.setVisibility(View.VISIBLE);
        } else {
            mLLShareParentView.setVisibility(View.GONE);
            if (mShareWay == WEIXIN) {
                if (isOnlyImg) {
                    shareWeixinonlyImg();

                } else {
                    shareWeixin();
                }
            } else if (mShareWay == WEIXIN_CIRCLE) {
                if (isOnlyImg) {
                    shareWeicirleonlyImg();

                } else {
                    shareFriendly();
                }
            } else if (mShareWay == QQ) {
                if (isOnlyImg) {
                    shareQQonlyImg();

                } else {

                    shareQQ();
                }
            } else if (mShareWay == WEIBO) {
                shareWb();
            } else if (mShareWay == QUANYAN) {
                shareQuanYan();
            }
//            finish();
        }
    }

    private void shareWeicirleonlyImg() {
        //无网
        if (!AndroidUtils.isNetWorkAvalible(this)) {
            ToastUtil.showToast(this, getString(R.string.error_label_popup_wlyc));
            finishAC();
            return;
        }

        if (!MobileUtil.isWeixinAvilible(this)) {
            ToastUtil.showToast(this, getString(R.string.wx_isinstall));
            finishAC();
            return;
        }
        new ShareAction(ShareTableActivity.this)
                .setCallback(umShareListener)
                .setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
                .withMedia(urlImage)
                .share();

    }

    private void shareWeixinonlyImg() {
//无网
        if (!AndroidUtils.isNetWorkAvalible(this)) {
            ToastUtil.showToast(this, getString(R.string.error_label_popup_wlyc));
            finishAC();
            return;
        }

        if (!MobileUtil.isWeixinAvilible(this)) {
            ToastUtil.showToast(this, getString(R.string.wx_isinstall));
            finishAC();
            return;
        }
        new ShareAction(ShareTableActivity.this)
                .setCallback(umShareListener)
                .setPlatform(SHARE_MEDIA.WEIXIN)
                .withMedia(urlImage)
                .share();
    }

    private void shareQQonlyImg() {
        //无网
        if (!AndroidUtils.isNetWorkAvalible(this)) {
            ToastUtil.showToast(this, getString(R.string.error_label_popup_wlyc));
            finishAC();
            return;
        }

        if (!umShareAPI.isInstall(this, SHARE_MEDIA.QQ)) {
            ToastUtil.showToast(this, getString(R.string.qq_isinstall));
            finishAC();
            return;
        }


//        UMWeb web = new UMWeb(shareWebPage);
//        web.setTitle(shareTitle);//标题
//        web.setThumb(new UMImage(ShareTableActivity.this, ImageUtils.getImageFullUrl(shareImageUrl)));  //缩略图
//        web.setDescription(!StringUtil.isEmpty(shareContent) ? shareContent : "来云南,选");//描述

        new ShareAction(ShareTableActivity.this)
                .setCallback(umShareListener)
                .setPlatform(SHARE_MEDIA.QQ)
                .withMedia(urlImage)
                .share();
    }

    private void initEvent() {
        gridView.setOnItemClickListener(this);
        llOther.setOnClickListener(this);
        mTVCancle.setOnClickListener(this);
    }

    private void initShareData() {
        //对象确定分享
        if (shareBean != null) {
            dlgTilte = shareBean.dlgTitle;
            dlgMessage = shareBean.dlgMessage;
            shareTitle = shareBean.shareTitle;
            shareContent = shareBean.shareContent;
            shareImageUrl = shareBean.shareImageURL;
            shareImageLocal = shareBean.shareImageLocal;
            shareWebPage = shareBean.shareWebPage;

        }

        // TODO: 2018/3/6   去掉这个逻辑，分享内容由h5传过来
        //Type类型确定分享内容
//        if (!StringUtil.isEmpty(mShareType)) {
//            if (ItemType.NORMAL.equals(mShareType)) {
//                shareContent = getString(R.string.share_eatfood);
//            } else if(ItemType.FREE_LINE.equals(mShareType) ){
//                shareContent = getString(R.string.share_travel);
//            }else if(ItemType.CITY_ACTIVITY.equals(mShareType)){
//                shareContent = getString(R.string.share_travel);//产品还没定
//            }
//        }

        if (!StringUtil.isEmpty(shareImageLocal)) {
            urlImage = new UMImage(this, BitmapFactory.decodeFile(shareImageLocal));
        }
        if (!StringUtil.isEmpty(shareImageUrl)) {
//            urlImage = new UMImage(this, /*getLocalOrNetBitmap(shareImageUrl)*/shareImageUrl);
            urlImage = new UMImage(this, /*getLocalOrNetBitmap(shareImageUrl)*/ImageUtils.getImageFullUrl(shareImageUrl));
        }

    }

    /**
     * 获取Bitmap对象
     *
     * @param url
     * @return
     */
    private Bitmap getLocalOrNetBitmap(String url) {
        Bitmap bitmap = null;
        InputStream in = null;
        BufferedOutputStream out = null;
        try {
            in = new BufferedInputStream(new URL(url).openStream(), 1024);
            final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
            out = new BufferedOutputStream(dataStream, 1024);
            copy(in, out);
            out.flush();
            byte[] data = dataStream.toByteArray();
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            data = null;
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void copy(InputStream in, OutputStream out)
            throws IOException {
        byte[] b = new byte[1024];
        int read;
        while ((read = in.read(b)) != -1) {
            out.write(b, 0, read);
        }
    }

    /**
     * 配置分享平台参数</br>
     */
    private void configPlatforms() {
        umShareAPI = UMShareAPI.get(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                analyDataEncap(0);
                shareWeixin();//微信分享
                finishAC();
                break;
            case 1:
                analyDataEncap(1);
                shareFriendly();//朋友圈分享
                finishAC();
                break;
            case 3:
                analyDataEncap(2);
                shareQQ();//qq分享
                //finishAC();
                break;
            case 2:
                analyDataEncap(3);
                shareWb();//新浪微博分享
//                finishAC();
                break;
//            case 4:
//                analyDataEncap(4);
//                shareQQZone();//qq空间分享
//                break;
//            case 5:
//                analyDataEncap(5);
//                shareQuanYan();//分享到达人圈
//                break;
        }
    }

    private void analyDataEncap(int bid) {
        Map<String, String> map = new HashMap<>();
        map.put(AnalyDataValue.KEY_PID, shareBean.pid);
        map.put(AnalyDataValue.KEY_PTYPE, AnalyDataValue.getType(shareBean.ptype));
        if (!StringUtil.isEmpty(shareBean.pname)) {
            map.put(AnalyDataValue.KEY_PNAME, shareBean.pname);
        } else {
            if (!StringUtil.isEmpty(shareTitle)) {
                map.put(AnalyDataValue.KEY_PNAME, shareTitle);
            }
        }

        switch (bid) {
            case 0:
                map.put(AnalyDataValue.KEY_SHARE_TYPE, AnalyDataValue.WEIXIN);
                break;
            case 1:
                map.put(AnalyDataValue.KEY_SHARE_TYPE, AnalyDataValue.WEIXIN_CICRLE);
                break;
            case 2:
                map.put(AnalyDataValue.KEY_SHARE_TYPE, AnalyDataValue.QQ);
                break;
            case 3:
                map.put(AnalyDataValue.KEY_SHARE_TYPE, AnalyDataValue.WEIBO);
                break;
        }
        TCEventHelper.onEvent(this, AnalyDataValue.DETAIL_SHARE, map);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finishAC();
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_other:
                finishAC();
                break;
            case R.id.tv_cancle:
                finishAC();
                break;
        }
    }

    //关闭页面
    private void finishAC() {
        finish();
//        overridePendingTransition(R.anim.push_up_in2, R.anim.push_up_out2);
    }

    UMShareListener umShareListener = new UMShareListener() {

        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {
            if (isStep) {
                EventBus.getDefault().post(new EvBusShareSuccess());
            }

            //分享成功
            ToastUtil.showToast(ShareTableActivity.this, getString(R.string.ym_share_success));
            finishAC();
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            //分享失败
            ToastUtil.showToast(ShareTableActivity.this, getString(R.string.ym_share_failed));
            finishAC();
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            //分享取消
            //ToastUtil.showToast(ShareTableActivity.this, getString(R.string.ym_share_cancel));
            finishAC();
        }
    };


    private void shareWb() {
        /*if(!umShareAPI.isInstall(this, SHARE_MEDIA.SINA)){
            ToastUtil.showToast(this, getString(R.string.sina_isinstall));
            return;
        }*/


        //无网
        if (!AndroidUtils.isNetWorkAvalible(this)) {
            ToastUtil.showToast(this, getString(R.string.error_label_popup_wlyc));
            return;
        }


//        UMWeb web = new UMWeb(shareWebPage);
//        web.setTitle(shareTitle);//标题
//        web.setThumb(ImageUtils.getImageFullUrl(shareImageUrl));  //缩略图
//        web.setDescription(!StringUtil.isEmpty(shareContent) ? shareContent : "来云南,选");//描述


//        new ShareAction(ShareTableActivity.this)
//                .setCallback(umShareListener)
//                .setPlatform(SHARE_MEDIA.SINA)
//                .withMedia(web)
//                .share();


//        if (!StringUtil.isEmpty(shareImageUrl)) {
////            urlImage = new UMImage(this, /*getLocalOrNetBitmap(shareImageUrl)*/shareImageUrl);
//            urlImage = new UMImage(this, /*getLocalOrNetBitmap(shareImageUrl)*/ImageUtils.getImageFullUrl(shareImageUrl));
//        }


        ShareAction shareAction = new ShareAction(ShareTableActivity.this);
        shareAction
                .setCallback(umShareListener)
                .setPlatform(SHARE_MEDIA.SINA);
        if (!StringUtil.isEmpty(shareImageLocal)) {
            shareAction.withMedia(new UMImage(ShareTableActivity.this, BitmapFactory.decodeFile(shareImageLocal)));
        } else {
            shareAction.withMedia(new UMImage(ShareTableActivity.this, ImageUtils.getImageFullUrl(shareImageUrl)));
        }
        String text = "";
        if (!TextUtils.isEmpty(shareTitle)) {
            text += shareTitle;
        }

        if (!TextUtils.isEmpty(shareContent)) {
            text += shareContent;
        }

        if (!TextUtils.isEmpty(shareWebPage)) {
            text += shareWebPage;
        }
        shareAction.withText(text);
        shareAction.share();
//        WbShareHandler shareHandler = new WbShareHandler(this);
//        shareHandler.registerApp();
//        shareHandler.setProgressColor(0xff33b5e5);
//        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
////        if (hasText) {
//            weiboMessage.textObject = getTextObj();
////        }
////        if (hasImage) {
//            weiboMessage.imageObject = getImageObj();
////        }
//
//
//        shareHandler.shareMessage(weiboMessage, false);
        //新浪微博
//        UMImage urlImage1 = new UMImage(ShareTableActivity.this, R.mipmap.ic_app_launcher);
//        shareTitle="abd";
//        shareWebPage="http://mtest.yingheying.com/wxlive/#!/live/91";
//        ShareSinaUtil.shareSina(ShareTableActivity.this,umShareListener,shareTitle,shareContent,shareWebPage,urlImage1);
//////////////////////
//        ShareAction sa = new ShareAction(this);
//        sa.setPlatform(SHARE_MEDIA.SINA);
//        if(umShareListener != null) {
//            sa.setCallback(umShareListener);
//        }
//        if(!StringUtil.isEmpty(shareTitle)){
//            sa.withTitle(shareTitle);
//        }
//        if(!StringUtil.isEmpty(shareContent)){
//            sa.withText(shareContent);
//        }else{
//            sa.withText("来云南,选");
//        }
//        if(!StringUtil.isEmpty(shareWebPage)){
//            sa.withTargetUrl(shareWebPage);
//        }
//        if(urlImage != null){
//            sa.withMedia(urlImage);
//        }
//
//        sa.share();
/////////////////////////////
//        com.quncao.baselib.bean.ShareBean shareBean = new com.quncao.baselib.bean.ShareBean();
//        if(!StringUtil.isEmpty(shareTitle)){
//            shareBean.shareTitle=shareTitle;
//        }
//        if(!StringUtil.isEmpty(shareContent)){
//            shareBean.shareContent=shareContent;
//        }else{
//            shareBean.shareContent="来云南,选";
//        }
//        if(!StringUtil.isEmpty(shareWebPage)){
//            shareBean.shareWebUrl= shareWebPage;
//
//        }
//        if(urlImage != null){
//            shareBean.shareImageUrl=urlImage.toUrl();
//
//        }
//        shareBean.setShareSet(com.quncao.baselib.bean.ShareBean.SHARE_WEIBO);
//        LarkShareUtils.share(this, shareBean, new OnShareItemClickListener() {
//            @Override
//            public void onShareItemClick(int i) {
//            }
//        });
        //判断是否同步到达人圈
        if (mSyncDynamicView.isChecked() && userService.isLogin()) {
            doAddLive();
        }
    }

    private void shareQQ() {

        //无网
        if (!AndroidUtils.isNetWorkAvalible(this)) {
            ToastUtil.showToast(this, getString(R.string.error_label_popup_wlyc));
            return;
        }

        if (!umShareAPI.isInstall(this, SHARE_MEDIA.QQ)) {
            ToastUtil.showToast(this, getString(R.string.qq_isinstall));
            return;
        }
        //QQ分享
//        new ShareAction(this).setPlatform(SHARE_MEDIA.QQ)
//                .setCallback(umShareListener)
////                .withTitle(shareTitle)
//                .withText(shareContent)
////                .withTargetUrl(shareWebPage)
//                .withMedia(urlImage)
//                .share();


        UMWeb web = new UMWeb(shareWebPage);
        web.setTitle(shareTitle);//标题
        web.setThumb(new UMImage(ShareTableActivity.this, ImageUtils.getImageFullUrl(shareImageUrl)));  //缩略图
        web.setDescription(!StringUtil.isEmpty(shareContent) ? shareContent : "来云南,选");//描述

        new ShareAction(ShareTableActivity.this)
                .setCallback(umShareListener)
                .setPlatform(SHARE_MEDIA.QQ)
                .withMedia(web)
                .share();

        //判断是否同步到达人圈
        if (mSyncDynamicView.isChecked() && userService.isLogin()) {
            doAddLive();
        }
    }

    private void shareFriendly() {
        /*if(!umShareAPI.isInstall(this, SHARE_MEDIA.WEIXIN_CIRCLE)){
            ToastUtil.showToast(this, getString(R.string.wx_isinstall));
            return;
        }*/

        //无网
        if (!AndroidUtils.isNetWorkAvalible(this)) {
            ToastUtil.showToast(this, getString(R.string.error_label_popup_wlyc));
            return;
        }

        if (!MobileUtil.isWeixinAvilible(this)) {
            ToastUtil.showToast(this, getString(R.string.wx_isinstall));
            return;
        }
        // 设置朋友圈分享的内容
//        ShareAction sa = new ShareAction(this);
//        sa.setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE);
//        if (umShareListener != null) {
//            sa.setCallback(umShareListener);
//        }
//        if (!StringUtil.isEmpty(shareTitle)) {
////            sa.withTitle(shareTitle + "\n" + shareContent);
//        }
//        if (!StringUtil.isEmpty(shareContent)) {
//            sa.withText(shareContent);
//        }
//        if (!StringUtil.isEmpty(shareWebPage)) {
////            sa.withTargetUrl(shareWebPage);
//        }
//        if (urlImage != null) {
//            sa.withMedia(urlImage);
//        }
//        sa.share();
        UMWeb web = new UMWeb(shareWebPage);
        web.setTitle(shareTitle);//标题
        web.setThumb(new UMImage(ShareTableActivity.this, ImageUtils.getImageFullUrl(shareImageUrl)));  //缩略图
        web.setDescription(!StringUtil.isEmpty(shareContent) ? shareContent : "来云南,选");//描述

        new ShareAction(ShareTableActivity.this)
                .setCallback(umShareListener)
                .setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
                .withMedia(web)
                .share();
        //判断是否同步到达人圈
        if (mSyncDynamicView.isChecked() && userService.isLogin()) {
            doAddLive();
        }
    }

    private void shareQQZone() {
        //qq空间分享
    }

    private void shareWeixin() {
        /*if(!umShareAPI.isInstall(this, SHARE_MEDIA.WEIXIN)){
            ToastUtil.showToast(this, getString(R.string.wx_isinstall));
            return;
        }*/
        //无网
        if (!AndroidUtils.isNetWorkAvalible(this)) {
            ToastUtil.showToast(this, getString(R.string.error_label_popup_wlyc));
            return;
        }

        if (!MobileUtil.isWeixinAvilible(this)) {
            ToastUtil.showToast(this, getString(R.string.wx_isinstall));
            return;
        }
        //微信分享
//        new ShareAction(this).setPlatform(SHARE_MEDIA.WEIXIN)
//                .setCallback(umShareListener)
////                .withTitle(shareTitle)
//                .withText(shareContent)
////                .withTargetUrl(shareWebPage)
//                .withMedia(urlImage)
//                .share();

        UMWeb web = new UMWeb(shareWebPage);
        web.setTitle(shareTitle);//标题
        web.setThumb(new UMImage(ShareTableActivity.this, ImageUtils.getImageFullUrl(shareImageUrl)));  //缩略图
        web.setDescription(!StringUtil.isEmpty(shareContent) ? shareContent : "来云南,选");//描述

        new ShareAction(ShareTableActivity.this)
                .setCallback(umShareListener)
                .setPlatform(SHARE_MEDIA.WEIXIN)
                .withMedia(web)
                .share();
        //判断是否同步到达人圈
        if (mSyncDynamicView.isChecked() && userService.isLogin()) {
            doAddLive();
        }
    }

    /**
     * 发布动态
     */
    private void doAddLive() {
        if (!StringUtil.isEmpty(shareBean.shareImageLocal)) {
            Runnable requestThread = new Runnable() {
                @Override
                public void run() {
                    upLoadimage();
                }
            };
            NetThreadManager.getInstance().execute(requestThread);
        } else {
            postLive(null);
        }
    }

    /**
     * 上传多张图片
     */
    private void upLoadimage() {
        String upurl[] = new String[1];
        upurl[0] = shareBean.shareImageLocal;
        mControler.compressionImage(this, upurl);

    }

    /**
     * 发布动态
     */
    private void postLive(String image) {
        mControler.doPublishNewSubjectLive(this, getUGCInfo(image));
    }

    /**
     * 组装UGC
     *
     * @return
     */
    public Api_SNSCENTER_UgcInfo getUGCInfo(String image) {
        Api_SNSCENTER_UgcInfo mInfo = new Api_SNSCENTER_UgcInfo();
        //发布者ID
        mInfo.userId = userService.getLoginUserId();
        StringBuffer sb = new StringBuffer();
        if (!StringUtil.isEmpty(shareBean.shareContent)) {
            sb.append(StringUtil.sideTrim(shareBean.shareContent, "\n"));
        }
        if (!StringUtil.isEmpty(shareBean.shareWebPage)) {
            sb.append(shareBean.shareWebPage);
        }
        mInfo.textContent = sb.toString();
        if (!StringUtil.isEmpty(image)) {
            List<String> uploadFiles = new ArrayList<>();
            uploadFiles.add(image);
            mInfo.picList = uploadFiles;
        }
        return mInfo;
    }

    //分享达人圈
    private void shareQuanYan() {
        //无网
        if (!AndroidUtils.isNetWorkAvalible(this)) {
            ToastUtil.showToast(this, getString(R.string.error_label_popup_wlyc));
            return;
        }
        NavUtils.gotoAddLiveAcitivty(this, shareBean, isStep);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);

//        if (requestCode == ValueConstants.POST_LIVE)
        finish();
    }
}
