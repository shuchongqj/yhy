package com.quanyan.yhy.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quanyan.base.yminterface.ErrorViewClick;
import com.quanyan.yhy.R;

import pl.droidsonroids.gif.GifImageView;

/**
 * Created by dengmingjia on 2015/11/16.
 * 网络异常时或数据缺失时显示的视图
 */
public class NetWorkErrorView extends LinearLayout implements View.OnClickListener {

    private GifImageView mImage;
    private TextView tvTitle;
    private TextView tvMessage;
    public TextView reload;

    public NetWorkErrorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public NetWorkErrorView(Context context) {
        super(context);
        init(context);
    }


    //初始化
    private void init(Context context) {
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);
        setBackgroundColor(getResources().getColor(R.color.color_norm_f4f4f4));
        LayoutInflater.from(context).inflate(R.layout.error_view, this);

        mImage = (GifImageView) findViewById(R.id.error_view_image);
        tvTitle = (TextView) findViewById(R.id.error_view_text);
        tvMessage = (TextView) findViewById(R.id.error_view_notice);
        reload = (TextView) findViewById(R.id.error_view_load_again);
        reload.setOnClickListener(this);
    }

    private ErrorViewClick mErrorViewClick;

    public void show(String title, String message, String buttonText, final ErrorViewClick errorViewClick) {
        show(R.mipmap.error_empty_icon, title, message, buttonText, errorViewClick);
    }
    public void show(int iconResId, String title, String message, String buttonText, final ErrorViewClick errorViewClick) {
        mErrorViewClick = errorViewClick;
        if (errorViewClick == null) {
            hideButton();
        } else {
            showButton(buttonText);
        }
        if (iconResId <= 0) {
            mImage.setImageBitmap(null);
        } else {
            mImage.setImageResource(iconResId);
        }
        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(title.trim())) {
            if (tvTitle.getVisibility() == View.GONE) {
                tvTitle.setVisibility(View.VISIBLE);
            }
            tvTitle.setText(title);
        } else {
            tvTitle.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(message) && !TextUtils.isEmpty(message.trim())) {
            if (tvMessage.getVisibility() == View.GONE) {
                tvMessage.setVisibility(View.VISIBLE);
            }
            tvMessage.setText(message);
        } else {
            tvMessage.setVisibility(View.GONE);
        }
        setVisibility(View.VISIBLE);
    }

    public void hide() {
        setVisibility(View.GONE);
    }

    public void showButton(String buttonText) {
        if (TextUtils.isEmpty(buttonText)) {
            reload.setText(R.string.reload);
        } else {
            reload.setText(buttonText);
        }
        reload.setVisibility(View.VISIBLE);
    }

    private void hideButton() {
        reload.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        if (mErrorViewClick != null) {
            mErrorViewClick.onClick(v);
        }
    }
}
