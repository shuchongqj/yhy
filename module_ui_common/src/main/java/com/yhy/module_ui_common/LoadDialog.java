package com.yhy.module_ui_common;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.CircularProgressDrawable;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Nandy on 2018/7/17
 */
public class LoadDialog extends Dialog {
    private TextView tvMessage;

    public LoadDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setContentView(R.layout.load_dialog);
        tvMessage =findViewById(R.id.tv);
        tvMessage.setText("请稍候...");
    }
}
