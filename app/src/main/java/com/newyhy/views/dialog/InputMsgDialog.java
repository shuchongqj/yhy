package com.newyhy.views.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.quanyan.yhy.R;
import com.yixia.camera.util.Log;

import static android.view.inputmethod.InputMethodManager.HIDE_NOT_ALWAYS;
import static android.view.inputmethod.InputMethodManager.SHOW_FORCED;

public class InputMsgDialog extends AppCompatDialog {

    private EditText et_comment_content;
    private TextView tv_send;
    private SendMsgClickCallBack callBack;

    public InputMsgDialog(@NonNull Context context, int theme) {
        super(context, theme);

        Window window = getWindow();
        //设置dialog在屏幕底部
        if (window != null) {
            window.setGravity(Gravity.BOTTOM);
            //设置dialog弹出时的动画效果，从屏幕底部向上弹出
            window.setWindowAnimations(R.style.dialogStyle);
            window.getDecorView().setPadding(0, 0, 0, 0);
            //获得window窗口的属性
            android.view.WindowManager.LayoutParams lp = window.getAttributes();
            //设置窗口宽度为充满全屏
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            //设置窗口高度为包裹内容
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setBackgroundDrawableResource(android.R.color.transparent);
            window.setDimAmount(0f);
            //将设置好的属性set回去
            window.setAttributes(lp);
        }

        setContentView(R.layout.soft_edit_text);

    }

    public void setSendMsgClickCallBack(SendMsgClickCallBack callBack) {
        this.callBack = callBack;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        et_comment_content = findViewById(R.id.et_comment_content);
        tv_send = findViewById(R.id.tv_publish_comment);
        tv_send.setOnClickListener(v -> {
            if (callBack != null) {
                callBack.sendClick(et_comment_content.getText().toString());
                et_comment_content.setText("");
            }
            /*InputMethodManager imm = (InputMethodManager) getContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), HIDE_NOT_ALWAYS);
            }*/
            dismiss();
        });
        findViewById(R.id.input_root_layout).setOnTouchListener((v, event) -> {
            /*InputMethodManager imm = (InputMethodManager) getContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(et_comment_content.getWindowToken(), 0);
            }*/
            dismiss();
            return false;
        });

        et_comment_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tv_send.setEnabled(!TextUtils.isEmpty(s));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void showSoftInput() {
        showSoftInput("");
    }

    public void showSoftInput(String hint) {
        et_comment_content.setFocusable(true);
        et_comment_content.setFocusableInTouchMode(true);
        et_comment_content.requestFocus();
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(et_comment_content, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public void setHint(String hint) {
        et_comment_content.setHint(TextUtils.isEmpty(hint) ? "发表您的评论" : hint);

    }

    public interface SendMsgClickCallBack {
        void sendClick(String text);
    }

    @Override
    public void dismiss() {
        View view = getCurrentFocus();
            InputMethodManager mInputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            mInputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
        super.dismiss();
    }
}
