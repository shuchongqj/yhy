package com.quanyan.yhy.ui.zxing.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.quanyan.yhy.R;

/**
 * Created with Android Studio.
 * Title:FunctionPop
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-9-23
 * Time:14:19
 * Version 1.1.0
 */

public class FunctionPop extends PopupWindow implements View.OnTouchListener {

    private final View mView;
    private TextView mCancel;
    private TextView mSaveBtn;
    private TextView mShareBtn;
    private Context context;

    public FunctionPop (Context context){
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.alert_dialog, null);
        setContentView(mView);
        initPop();
        findId();
        initClick();
    }

    private void initPop() {
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setAnimationStyle(R.style.PopupAnimation);
        ColorDrawable dw = new ColorDrawable(context.getResources().getColor(
                R.color.transparent_50));
        this.setBackgroundDrawable(dw);
    }

    private void initClick() {
        mView.setOnTouchListener(this);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnBtnClickListener != null){
                    mOnBtnClickListener.onSaveListener();
                }
                dismiss();

            }
        });

        mShareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnBtnClickListener != null){
                    mOnBtnClickListener.onShareListener();
                }
                dismiss();
            }
        });
    }

    private void findId() {
        mCancel = (TextView) mView.findViewById(R.id.btn_cancel);
        mSaveBtn = (TextView) mView.findViewById(R.id.btn_pick_photo);
        mShareBtn = (TextView) mView.findViewById(R.id.btn_take_photo);
        mCancel.setText(context.getString(R.string.label_title_bar_cancel));
        mSaveBtn.setText(context.getString(R.string.mine_qr_save_pic));
        mShareBtn.setText(context.getString(R.string.mine_qr_share_pic));
    }

    //显示popwindow
    public void showPopupWindow(View view) {
        if (!this.isShowing()) {
            showLocation(view);
        } else {
            this.dismiss();
        }
    }

    private void showLocation(View view) {
        // 设置layout在PopupWindow中显示的位置
        this.showAtLocation(view, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        int height = mView.findViewById(R.id.pop_layout).getTop();
        int y = (int) event.getY();
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (y < height) {
                dismiss();
            }
        }
        return true;

    }

    private OnBtnClickListener mOnBtnClickListener;

    public interface OnBtnClickListener{
        void onSaveListener();
        void onShareListener();
    }

    public void setOnBtnClickListener(OnBtnClickListener mOnBtnClickListener){
        this.mOnBtnClickListener = mOnBtnClickListener;
    }
}
