package com.quanyan.yhy.ui.order;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.harwkin.nb.camera.TimeUtil;
import com.quanyan.yhy.R;

/**
 * Created with Android Studio.
 * Title:OrderBottomTabView
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-3-3
 * Time:10:36
 * Version 1.0
 * Description:订单最底部的UI
 */
public class OrderBottomTabView extends LinearLayout implements View.OnClickListener {

    private TextView tv_order_price;
    private Button tv_order_submit;
    private TextView tv_order_details;

    private SubmitClick submitClick;

    private LinearLayout mLeftView;
    private RelativeLayout mRightView;

    public OrderBottomTabView(Context context) {
        super(context);
        init(context);
    }

    public OrderBottomTabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public OrderBottomTabView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public OrderBottomTabView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }


    private void init(Context context) {
        LayoutInflater mLayoutInflater = LayoutInflater.from(context);
        View view = mLayoutInflater.inflate(R.layout.orderbottomtabview, null);

        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        rlp.bottomMargin = 0;
        rlp.topMargin = 0;
        rlp.leftMargin = 0;
        rlp.rightMargin = 0;

        mLeftView = (LinearLayout) view.findViewById(R.id.tv_order_left_layout);
        mRightView = (RelativeLayout) view.findViewById(R.id.rl_right_layout);

        tv_order_price = (TextView) view.findViewById(R.id.tv_order_price);
        tv_order_submit = (Button) view.findViewById(R.id.tv_order_submit);
        tv_order_details = (TextView)view.findViewById(R.id.tv_order_details);
        tv_order_submit.setOnClickListener(this);
        addView(view, rlp);
    }


    public void showleftImg(String str){
        findViewById(R.id.tv_order_img).setVisibility(View.VISIBLE);
        tv_order_price.setVisibility(View.GONE);
        tv_order_details.setVisibility(View.VISIBLE);
        if(!TextUtils.isEmpty(str)){
            tv_order_details.setText(str);
        }else{
            tv_order_details.setText("");
        }
    }
    public void setOnLeftClick(View.OnClickListener onLeftClick){
        findViewById(R.id.tv_order_left_layout).setOnClickListener(onLeftClick);
    }
    public void setonRighClick(View.OnClickListener onClickListener){
        findViewById(R.id.tv_order_submit).setOnClickListener(onClickListener);
    }

    public void setBottomPrice(String price) {
        if(!TextUtils.isEmpty(price)){
            tv_order_price.setText(price);
        }else{
            tv_order_price.setText("");
        }
    }

    public String getBottomPrice(){
        return tv_order_price.getText().toString();
    }

    public void setSubmitText(String title) {
        if (!TextUtils.isEmpty(title)) {
            tv_order_submit.setText(title);
        }
    }

    public void setSubmitClickListener(SubmitClick submitClick){
        this.submitClick = submitClick;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_order_submit:
                if (TimeUtil.isFastDoubleClick()) {
                    return;
                }
                if (submitClick != null) {
                    submitClick.submit();
                }
                break;
        }
    }

    public interface SubmitClick {
        void submit();
    }

    //设置权重
    public void setWeigth(int left, int right){
        LayoutParams leftParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, left);
        LayoutParams rightParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, right);
        mLeftView.setLayoutParams(leftParams);
        mRightView.setLayoutParams(rightParams);
        requestLayout();
    }

    public LinearLayout getLeftLayout(){
        return mLeftView;
    }

    public TextView getLeftView(){
        return tv_order_price;
    }

    public RelativeLayout getRightLayout(){
        return mRightView;
    }

    public Button getRightView(){
        return tv_order_submit;
    }

}
