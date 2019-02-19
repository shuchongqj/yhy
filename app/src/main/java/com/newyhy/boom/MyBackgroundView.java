package com.newyhy.boom;

import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.harwkin.nb.camera.ImageUtils;
import com.quanyan.yhy.R;
import com.quanyan.yhy.libanalysis.AnEvent;
import com.quanyan.yhy.libanalysis.Analysis;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.smart.sdk.api.resp.Api_RESOURCECENTER_BannerDto;
import com.smart.sdk.api.resp.Api_RESOURCECENTER_PublishBootResult;
import com.smart.sdk.api.resp.Api_RESOURCECENTER_WeatherDto;
import com.yhy.boombutton.Animation.AnimationManager;
import com.yhy.boombutton.BackgroundView;
import com.yhy.boombutton.BoomMenuButton;
import com.yhy.common.utils.CommonUtil;
import com.yhy.imageloader.ImageLoadManager;
import com.yhy.location.LocationManager;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyBackgroundView extends BackgroundView{
    private int dimColor;
    private View view;
    private ImageView imageView;
    private ImageView ivAdvertisement;
    private TextView tvCityName;
    private TextView tvDateTime;
    private TextView tvTemperature;
    private TextView tvDu;
    private TextView tvWeather;
    private TextView tvWeekName;
    private TextView tvAdvice;
    private RelativeLayout publish_all;

    protected MyBackgroundView(Context context, BoomMenuButton bmb) {
        super(context, bmb);

        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.new_publish, null);

        imageView = view.findViewById(R.id.publish_close);
        //广告图
        ivAdvertisement = view.findViewById(R.id.publish_ivAdvertisement);
        //城市名
        tvCityName = view.findViewById(R.id.publish_tvCityName);
        //日期
        tvDateTime = view.findViewById(R.id.publish_tvDateTime);
        //温度
        tvTemperature = view.findViewById(R.id.publish_tvTemperature);
        tvDu = view.findViewById(R.id.tv_du);
        //天气
        tvWeather = view.findViewById(R.id.publish_tvWeather);
        //星期
        tvWeekName = view.findViewById(R.id.publish_tvWeekName);
        //提示
        tvAdvice = view.findViewById(R.id.publish_tvAdvice);
        //publish整体
        publish_all = view.findViewById(R.id.publish_all);

        dimColor = bmb.getDimColor();

        ViewGroup rootView = bmb.getParentView();
        rootView.setFitsSystemWindows(false);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                rootView.getWidth(),
                rootView.getHeight());
        setLayoutParams(params);
        setBackgroundColor(Color.WHITE);
        setOnClickListener(v -> bmb.onBackgroundClicked());
        setMotionEventSplittingEnabled(false);
        rootView.addView(this);
        rootView.addView(view);
    }

    protected void dim(long duration, AnimatorListenerAdapter completeListener) {
        setVisibility(VISIBLE);
        AnimationManager.animate(this, "backgroundColor", 0, duration, new ArgbEvaluator(), completeListener, Color.TRANSPARENT, dimColor);
        AnimationManager.animate(publish_all, "alpha", 0, duration + 500, new DecelerateInterpolator(), completeListener,0, 1);
        AnimationManager.animate(imageView, "alpha", 0, duration + 500, new DecelerateInterpolator(), completeListener,0, 1);
    }

    protected void light(long duration, AnimatorListenerAdapter completeListener) {
        AnimationManager.animate(imageView, "alpha", 0, duration, new DecelerateInterpolator(), completeListener,1, 0);
        AnimationManager.animate(publish_all, "alpha", 0, duration , new DecelerateInterpolator(), completeListener,1, 0);
        AnimationManager.animate(this, "backgroundColor", 0, duration + 500, new ArgbEvaluator(), completeListener, dimColor, Color.TRANSPARENT);
    }


    /**
     * 展示Publish的信息
     * @param result
     */
    public void setPublishInfo(final Api_RESOURCECENTER_PublishBootResult result){
        if ( result != null) {
            final Api_RESOURCECENTER_BannerDto bannerDto = result.banner;// boot展位信息

            if (bannerDto == null) {
                return;
            }
            long bannerId = bannerDto.bannerId;
            String cover = bannerDto.cover;
            String bannerType = bannerDto.bannerType;
            String url = bannerDto.url;

            if (cover != null && !cover.isEmpty() ) {
                cover = CommonUtil.getImageFullUrl(cover);
                ImageLoadManager.loadImage(cover, 0, ivAdvertisement);
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
                if ("全部".equals(LocationManager.getInstance().getStorage().getManual_discName())) {
                    tvCityName.setText(cityName + "市 ");
                } else {
                    tvCityName.setText(cityName + "市 " + LocationManager.getInstance().getStorage().getManual_discName());
                }
            }
            if (weekName != null && !weekName.isEmpty()) {
                tvWeekName.setText(weekName);
            }
            if (weather != null && !weather.isEmpty()) {
                tvWeather.setText(weather);
            }
            if (temperature != null && !temperature.isEmpty()) {
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

            ivAdvertisement.setOnClickListener(v -> {
                //统计事件
                Analysis.pushEvent(v.getContext(), AnEvent.SYMBOL_GET_5_YUAN);
                String url1 = bannerDto.url;
                if (url1 != null && !url1.isEmpty()) {
                    NavUtils.startWebview((Activity) getContext(), " ", url1, 0);
                }
            });
        }
    }

    @Override
    public View getView() {
        return view;
    }
}
