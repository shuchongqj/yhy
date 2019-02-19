package com.quanyan.yhy.ui.consult.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quanyan.yhy.R;

/**
 * Created with Android Studio.
 * Title:ConsultHomeItemView
 * Description:咨询首页itemView
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:邓明佳
 * Date:2016/6/29
 * Time:11:41
 * Version 1.0
 */
public class ConsultHomeItemView extends RelativeLayout {
    ImageView ivPic;
    TextView tvName, tvCity, tvPrice, tvSubmit;

    public ConsultHomeItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ivPic = (ImageView) findViewById(R.id.iv_consult);
        tvName = (TextView) findViewById(R.id.tv_consult_name);
        tvCity = (TextView) findViewById(R.id.tv_service_city);
        tvPrice = (TextView) findViewById(R.id.tv_price);
        tvSubmit = (TextView) findViewById(R.id.tv_submit);
    }

    public void setData() {

    }
}
