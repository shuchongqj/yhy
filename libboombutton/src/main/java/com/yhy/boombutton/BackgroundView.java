package com.yhy.boombutton;

import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.yhy.boombutton.Animation.AnimationManager;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by Weiping Huang at 11:47 on 2017/5/15
 * For Personal Open Source
 * Contact me at 2584541288@qq.com or nightonke@outlook.com
 * For more projects: https://github.com/Nightonke
 */

@SuppressLint("ViewConstructor")
public class BackgroundView extends FrameLayout {

    private int dimColor;

    protected BackgroundView(Context context, final BoomMenuButton bmb) {
        super(context);

//        if (view == null)
//            view = LayoutInflater.from(context).inflate(R.layout.new_publish, null);
//
//        imageView = view.findViewById(R.id.publish_close);
//        //广告图
//        ivAdvertisement = view.findViewById(R.id.publish_ivAdvertisement);
//        //城市名
//        tvCityName = view.findViewById(R.id.publish_tvCityName);
//        //日期
//        tvDateTime = view.findViewById(R.id.publish_tvDateTime);
//        //温度
//        tvTemperature = view.findViewById(R.id.publish_tvTemperature);
//        tvDu = view.findViewById(R.id.tv_du);
//        //天气
//        tvWeather = view.findViewById(R.id.publish_tvWeather);
//        //星期
//        tvWeekName = view.findViewById(R.id.publish_tvWeekName);
//        //提示
//        tvAdvice = view.findViewById(R.id.publish_tvAdvice);
//        //publish整体
//        publish_all = view.findViewById(R.id.publish_all);

//        dimColor = bmb.getDimColor();
//
//        ViewGroup rootView = bmb.getParentView();
//        rootView.setFitsSystemWindows(false);
//        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
//                rootView.getWidth(),
//                rootView.getHeight());
//        setLayoutParams(params);
//        setBackgroundColor(Color.TRANSPARENT);
//        setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                bmb.onBackgroundClicked();
//            }
//        });
//        setMotionEventSplittingEnabled(false);
//        rootView.addView(this);
    }

    public View getView() {
        return null;
    }

    protected void reLayout(final BoomMenuButton bmb) {
        ViewGroup rootView = bmb.getParentView();
        LayoutParams params = (LayoutParams) getLayoutParams();
        params.width = rootView.getWidth();
        params.height = rootView.getHeight();
        setLayoutParams(params);
        setBackgroundColor(Color.WHITE);
    }

    protected void dim(long duration, AnimatorListenerAdapter completeListener) {
        setVisibility(VISIBLE);
        AnimationManager.animate(this, "backgroundColor", 0, duration, new ArgbEvaluator(), completeListener, Color.TRANSPARENT, dimColor);
    }

    protected void light(long duration, AnimatorListenerAdapter completeListener) {
        AnimationManager.animate(this, "backgroundColor", 0, duration + 500, new ArgbEvaluator(), completeListener, dimColor, Color.TRANSPARENT);
    }

//    /**
//     * 展示Publish的信息
//     * @param result
//     */
//    public void setPublishInfo(final Api_RESOURCECENTER_PublishBootResult result){
//        if ( result != null) {
//            final Api_RESOURCECENTER_BannerDto bannerDto = result.banner;// boot展位信息
//
//            if (bannerDto == null) {
//                return;
//            }
//            long bannerId = bannerDto.bannerId;
//            String cover = bannerDto.cover;
//            String bannerType = bannerDto.bannerType;
//            String url = bannerDto.url;
//
//            if (cover != null && !cover.isEmpty() ) {
//                cover = ImageUtils.getImageFullUrl(cover);
//                ImageLoadManager.loadImage(cover, 0, ivAdvertisement);
//            }
//
//            Api_RESOURCECENTER_WeatherDto weatherDto = result.weather;// 天气预报信息
//
//            if (weatherDto == null) {
//                return;
//            }
//            String cityCode = weatherDto.cityCode;
//            String areaCode = weatherDto.areaCode;
//            String cityName = weatherDto.cityName;
//            String areaName = weatherDto.areaName;
//            long dateTime = weatherDto.dateTime;
//            String weekName = weatherDto.weekName;
//            String weather = weatherDto.weather;
//            String advice = weatherDto.advice;
//            String temperature = weatherDto.temperature;
//
//            if (cityName != null && !cityName.isEmpty()) {
//                if ("全部".equals(LocationManager.getInstance().getManual_discName())) {
//                    tvCityName.setText(cityName + "市 ");
//                } else {
//                    tvCityName.setText(cityName + "市 " + LocationManager.getInstance().getManual_discName());
//                }
//            }
//            if (weekName != null && !weekName.isEmpty()) {
//                tvWeekName.setText(weekName);
//            }
//            if (weather != null && !weather.isEmpty()) {
//                tvWeather.setText(weather);
//            }
//            if (temperature != null && !temperature.isEmpty()) {
//                tvTemperature.setText(temperature);
//                tvDu.setVisibility(View.VISIBLE);
//            }
//
//            if (dateTime > 0) {
//                Date date = new Date(dateTime);
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
//                tvDateTime.setText(sdf.format(date));
//            }
//
//            if(advice != null && !advice.isEmpty()){
//                tvAdvice.setText("温馨提示："+ advice);
//            }
//
//            ivAdvertisement.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //统计事件
//                    Analysis.pushEvent(mContext, AnEvent.SYMBOL_GET_5_YUAN);
//                    String url = bannerDto.url;
//                    if (url != null && !url.isEmpty()) {
//                        NavUtils.startWebview((Activity) getContext(), " ", url, 0);
//                    }
//                }
//            });
//        }
//    }
}
