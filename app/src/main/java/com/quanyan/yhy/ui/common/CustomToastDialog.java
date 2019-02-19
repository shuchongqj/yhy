package com.quanyan.yhy.ui.common;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.TextView;

import com.quanyan.yhy.R;


/**
 * Created with Android Studio.
 * Title:CustomToastDialog
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:baojie
 * Date:2015-12-11
 * Time:10:47
 * Version 1.0
 */

public class CustomToastDialog extends Dialog implements Runnable {

    private Context mContext;
    private Dialog mDialog;
    private TextView mTipsText;
    private int mShowTime;

    public CustomToastDialog(Context context) {
        super(context);
        this.mContext = context;
        mDialog = new Dialog(mContext, R.style.customDialog);//自定义Dialog样式
        initView();
        initData();
    }

    private void initData() {

        Window dialogWindow = mDialog.getWindow();//得到要显示的窗口
        dialogWindow.setGravity(Gravity.CENTER);
        mDialog.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_BACK:
                            dismiss();
                            break;
                    }
                }
                return false;
            }
        });
    }

    private void initView() {
        mDialog.setContentView(R.layout.custom_toast);
        mTipsText = (TextView) mDialog.findViewById(R.id.toastText);
    }

    @Override
    public void run() {
        try {
            Thread.sleep(mShowTime);//睡眠时间
            sendMessage(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(int msg) {
        Message message = new Message();
        message.what = msg;
        mHandler.sendMessage(message);
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    dismiss();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public void setMessage(int msg) {
        mTipsText.setText(msg);
    }

    public void setMessage(CharSequence msg) {
        mTipsText.setText(msg);
    }

    public void showTime(int time) {
        mShowTime = time;
    }

    public void show() {
        mDialog.show();
        new Thread(this).start();
    }

    public void dismiss() {
        if (mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    public void setLayout(int value){
        mDialog.setContentView(value);
    }
}
