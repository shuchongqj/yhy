package com.quanyan.yhy.ui.publish;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.harwkin.nb.camera.CameraHandler;
import com.harwkin.nb.camera.CameraManager;
import com.harwkin.nb.camera.ImageUtils;
import com.harwkin.nb.camera.callback.SelectMoreListener;
import com.harwkin.nb.camera.options.CameraOptions;
import com.harwkin.nb.camera.type.OpenType;
import com.newyhy.config.UmengEvent;
import com.quanyan.base.util.LocalUtils;
import com.quanyan.base.view.LoadingDialog;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.LiveType;
import com.quanyan.yhy.net.NetManager;
import com.quanyan.yhy.net.lsn.OnResponseListener;
import com.quanyan.yhy.ui.base.utils.DialogUtil;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.login.LoginActivity;
import com.quanyan.yhy.ui.shortvideo.MediaRecorderActivity;
import com.smart.sdk.api.resp.Api_LIVE_LiveRoomHasOrNot;
import com.smart.sdk.api.resp.Api_RESOURCECENTER_BannerDto;
import com.smart.sdk.api.resp.Api_RESOURCECENTER_PublishBootResult;
import com.smart.sdk.api.resp.Api_RESOURCECENTER_WeatherDto;
import com.umeng.analytics.MobclickAgent;
import com.videolibrary.controller.LiveController;
import com.videolibrary.utils.IntentUtil;
import com.yhy.common.base.NoLeakHandler;
import com.yhy.common.beans.album.MediaItem;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.CommonUtil;
import com.yhy.common.utils.SPUtils;
import com.yhy.imageloader.ImageLoadManager;
import com.yhy.location.LocationManager;
import com.yhy.router.YhyRouter;
import com.yhy.service.IUserService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by shenwenjie on 2018/1/22.
 * 发布页面
 */

public class PublishActivity extends Activity implements View.OnClickListener,NoLeakHandler.HandlerCallback {

    private Context context = this;
    private Button btnClose = null;
    private GridView gridView = null;

    private ImageView ivAdvertisement;
    private TextView tvCityName;
    private TextView tvDateTime;
    private TextView tvTemperature;
    private TextView tvDu;
    private TextView tvWeather;
    private TextView tvWeekName;
    private TextView tvAdvice;

    @Autowired
    IUserService userService;

    private PublishGridAdapter adapter = null;

    private NoLeakHandler mHandler;
    private LiveController mLiveController;
    private long userID;

    protected LoadingDialog processDialog;

    CameraHandler mCameraHandler = new CameraHandler(context, new SelectMoreListener() {
        @Override
        public void onSelectedMoreListener(List<MediaItem> pathList) {
            if (null != processDialog) processDialog.dismiss();
            NavUtils.gotoAddLiveAcitivty(PublishActivity.this, LiveType.ADD_PICTURE, null, (ArrayList<MediaItem>) pathList);
        }
    });

    private PublishHandler handler;//PublishHandler
    class PublishHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            final ArrayList<PublishInfo> list = (ArrayList<PublishInfo>) msg.obj;
            if (list != null && !list.isEmpty()) {
                adapter = new PublishGridAdapter(context);
                adapter.setList(list);
                gridView.setAdapter(adapter);
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (!userService.isLogin()) {
                            LoginActivity.gotoLoginActivity(context);
                            return;
                        }
                        PublishInfo info = list.get(position);
                        if (info != null) {
                            if (info.getTitle().equals("文字")) {
                                MobclickAgent.onEvent(PublishActivity.this, UmengEvent.SYMBOL_TEXT_RELEASED);
                                NavUtils.gotoAddLiveActivity(context);
                            } else if (info.getTitle().equals("拍摄")) {
                                MobclickAgent.onEvent(PublishActivity.this, UmengEvent.SYMBOL_FILM_RELEASED);
                                try {
                                    if (LocalUtils.isAlertMaxStorage()) {
                                        ToastUtil.showToast(context, getString(R.string.label_toast_sdcard_unavailable));
                                        return;
                                    }
                                    CameraOptions options = mCameraHandler.getOptions();
                                    options.setOpenType(OpenType.OPEN_CAMERA);
                                    mCameraHandler.process();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            } else if (info.getTitle().equals("相册")) {
                                MobclickAgent.onEvent(PublishActivity.this, UmengEvent.SYMBOL_PHOTO_RELEASED);
                                try {
                                    CameraOptions options = mCameraHandler.getOptions();
                                    options.setOpenType(OpenType.OPENN_USER_ALBUM);
                                    options.setMaxSelect(ValueConstants.MAX_SELECT_PICTURE);
                                    mCameraHandler.process();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else if (info.getTitle().equals("小视频")) {
                                MobclickAgent.onEvent(PublishActivity.this, UmengEvent.SYMBOL_VIDEO_RELEASED);
                                Intent intent = new Intent(PublishActivity.this, MediaRecorderActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putInt(SPUtils.EXTRA_TYPE, 1);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            } else if (info.getTitle().equals("直播")) {
                                MobclickAgent.onEvent(PublishActivity.this, UmengEvent.SYMBOL_LIVE_RELEASED);
                                //请求网络,判断是否有直播权限
                                mLiveController.doGetHasLiveRoomOrNot(PublishActivity.this, mHandler,userID);
                                gridView.setEnabled(false);
                            } else if (info.getTitle().equals("话题")) {
                                MobclickAgent.onEvent(PublishActivity.this, UmengEvent.SYMBOL_TOPIC_RELEASED);
                                NavUtils.gotoAddTopic(PublishActivity.this, 2);
                            } else if (info.getTitle().equals("俱乐部")) {
                                MobclickAgent.onEvent(PublishActivity.this, UmengEvent.SYMBOL_CLUB_RELEASED);
                                NavUtils.startWebview(PublishActivity.this, "俱乐部", SPUtils.addClub(context), 0);
                            } else if (info.getTitle().equals("活动")) {
                                MobclickAgent.onEvent(PublishActivity.this, UmengEvent.SYMBOL_ACTIVITY_RELEASED);
                                NavUtils.startWebview(PublishActivity.this, "活动", SPUtils.getAddClubAct(context).trim() + "?list=true", 0);
                            }

                        }
                    }
                });
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        YhyRouter.getInstance().inject(this);
        setContentView(R.layout.activity_publish);
        handler = new PublishHandler();
        gridView = findViewById(R.id.publish_gridView);
        btnClose = findViewById(R.id.publish_btnClose);
        btnClose.setOnClickListener(this);

        ivAdvertisement = findViewById(R.id.publish_ivAdvertisement);
        tvCityName = findViewById(R.id.publish_tvCityName);
        tvDateTime = findViewById(R.id.publish_tvDateTime);
        tvTemperature = findViewById(R.id.publish_tvTemperature);
        tvDu = findViewById(R.id.tv_du);
        tvWeather = findViewById(R.id.publish_tvWeather);
        tvWeekName = findViewById(R.id.publish_tvWeekName);
        tvAdvice = findViewById(R.id.publish_tvAdvice);
        ivAdvertisement.setOnClickListener(this);
        //根据uid获取权限
        //测试数据->PublishInfo
        ArrayList<PublishInfo> list = new ArrayList<>();
//        list.add(new PublishInfo(R.mipmap.court_icon_writing, "文字"));
//        list.add(new PublishInfo(R.mipmap.court_icon_shot, "拍摄"));
//        list.add(new PublishInfo(R.mipmap.court_icon_album, "相册"));
//        list.add(new PublishInfo(R.mipmap.court_icon_short_video, "小视频"));
//        list.add(new PublishInfo(R.mipmap.court_icon_direct_seeding, "直播"));
//        list.add(new PublishInfo(R.mipmap.court_icon_topic, "话题"));
//        list.add(new PublishInfo(R.mipmap.court_icon_club, "俱乐部"));
//        list.add(new PublishInfo(R.mipmap.court_icon_active, "活动"));
        Message msg = new Message();
        msg.obj = list;
        handler.sendMessage(msg);

        initNoLeakHandlerAndController();

        GetMainPublishBootInfo(LocationManager.getInstance().getStorage().getManual_cityName().replace("市",""),LocationManager.getInstance().getStorage().getManual_cityCode());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler = null;
    }

    private void initNoLeakHandlerAndController() {
        mHandler = new NoLeakHandler(this);
        mLiveController = LiveController.getInstance();
        userID = userService.getLoginUserId();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.publish_btnClose:
                finish();
                break;
            case R.id.publish_ivAdvertisement:
                if (v.getTag() != null) {
                    MobclickAgent.onEvent(PublishActivity.this, UmengEvent.SYMBOL_GET_5_YUAN);
                    String url = v.getTag().toString();
                    if (url != null && !url.isEmpty()) {
                        NavUtils.startWebview(PublishActivity.this, " ", url, 0);
                    }
                }
                break;
        }
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.publish_activity_close);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 290 && resultCode == -1) {
            if (processDialog == null) processDialog = DialogUtil.showLoadingDialog(PublishActivity.this,getString(R.string.picture_process), true);
            processDialog.show();
            mCameraHandler.forResult(requestCode, resultCode, data);
        } else if (requestCode == 293 && resultCode == -1) {
            mCameraHandler.forResult(requestCode, resultCode, data);
        } else if (requestCode == 2 && resultCode == -1) {
            String topic = data.getStringExtra(SPUtils.EXTRA_ADD_LIVE_LABEL);
            NavUtils.gotoAddLiveActivity(PublishActivity.this, topic);
        }
//        else if (requestCode == 293 && resultCode == CameraManager.GET_MEDIA) {
//            NavUtils.gotoAddLiveActivity(PublishActivity.this, (MediaItem) data.getParcelableExtra(SPUtils.EXTRA_MEDIA));
//        }
    }


    private void GetMainPublishBootInfo(String cityName,String cityCode) {

        NetManager.getInstance(context).doGetMainPublishBootInfo(cityName,cityCode,new OnResponseListener<Api_RESOURCECENTER_PublishBootResult>() {
            @Override
            public void onComplete(boolean isOK, Api_RESOURCECENTER_PublishBootResult result, int errorCode, String errorMsg) {
                if (!isFinishing() && result != null) {
                    Api_RESOURCECENTER_BannerDto bannerDto = result.banner;// boot展位信息

                    if (bannerDto == null) {
                        return;
                    }
                    long bannerId = bannerDto.bannerId;
                    String cover = bannerDto.cover;
                    String bannerType = bannerDto.bannerType;
                    String url = bannerDto.url;

                    if (cover != null && !cover.isEmpty() ) {
                        cover = ImageUtils.getImageFullUrl(cover);
//                        Glide.with(PublishActivity.this)
//                                .load(cover)
//                                .placeholder(getResources().getDrawable(R.mipmap.icon_default_215_150))
//                                .into(ivAdvertisement);
                        ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(cover), R.mipmap.icon_default_215_150, ivAdvertisement);
                    }
                    if (url != null && !url.isEmpty()) {
                        ivAdvertisement.setTag(url);
                    }

                    Api_RESOURCECENTER_WeatherDto weatherDto = result.weather;// 天气预报信息

                    if (weatherDto == null) {
                        return;
                    }
                    String cityCode = weatherDto.cityCode;
                    String areaCode = weatherDto.areaCode;
                    String cityName = weatherDto.cityName;
                    String areaName = weatherDto.areaName;
                    long dateTime = weatherDto.dateTime;
                    String weekName = weatherDto.weekName;
                    String weather = weatherDto.weather;
                    String advice = weatherDto.advice;
                    String temperature = weatherDto.temperature;

                    if (cityName != null && !cityName.isEmpty()) {
                        tvCityName.setText(cityName + "市 " + LocationManager.getInstance().getStorage().getManual_discName());
                    }
                    if (weekName != null && !weekName.isEmpty()) {
                        tvWeekName.setText(weekName);
                    }
                    if (weather != null && !weather.isEmpty()) {
                        tvWeather.setText(weather);
                    }
                    if (temperature != null && !temperature.isEmpty()) {
                        /*String[] temperatures = temperature.split("~");
                        if (temperatures != null) {
                            tvTemperature.setText(temperatures[0].replace("℃", ""));
                            tvDu.setVisibility(View.VISIBLE);
    //                        tvTemperature.setText(temperatures[0]);
                        }*/
                        tvTemperature.setText(temperature);
                        tvDu.setVisibility(View.VISIBLE);
                    }

                    if (dateTime > 0) {
                        Date date = new Date(dateTime);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                        tvDateTime.setText(sdf.format(date));
                    }

                    if(advice != null && !advice.isEmpty()){
                        tvAdvice.setText("温馨提示："+ advice);
                    }
//                    else {
//                        tvAdvice.setText("温馨提示 : PM2.5: 16,空气质量: 优");
//                    }
                }
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {

            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return super.onTouchEvent(event);
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case LiveController.MSG_LIVE_HAS_ROOM_OK:
                gridView.setEnabled(true);
                Api_LIVE_LiveRoomHasOrNot hasLiveRoom = (Api_LIVE_LiveRoomHasOrNot) msg.obj;
                if (hasLiveRoom.hasRoomOrNot){
                    IntentUtil.startPublishActivity(context);
                }else if (null != hasLiveRoom.msg && hasLiveRoom.msg.length() >0) {
                    NavUtils.startWebview(PublishActivity.this,"",hasLiveRoom.msg,0);
                } else {
                    ToastUtil.showToast(this,"您没有直播权限!");
                }
                break;
            case LiveController.MSG_LIVE_HAS_ROOM_ERROR:
                gridView.setEnabled(true);
                ToastUtil.showToast(this, "请求直播权限失败!");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        userID = userService.getLoginUserId();//登录更新uid
    }
}
