package com.quanyan.yhy.ui.order;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quanyan.yhy.R;

/**
 * Created with Android Studio.
 * Title:LineOrderDialog
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-11-11
 * Time:11:05
 * Version 1.0
 * Description:
 */
public class LineOrderDialog {


    public static Dialog showSingleSupplementDialog(final Context context, String title, String content, boolean canceledOnTouchOutside) {
        LayoutInflater mLayoutInflater = LayoutInflater.from(context);
        View view = mLayoutInflater.inflate(R.layout.single_supplement_dialog, null);
        final Dialog dialog = new Dialog(context, R.style.kangzai_dialog);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
        TextView mTitle = (TextView) view.findViewById(R.id.tv_single_title);
        TextView mContent = (TextView) view.findViewById(R.id.tv_content);
        RelativeLayout mCancle = (RelativeLayout) view.findViewById(R.id.rl_single_cancle);

        if (!TextUtils.isEmpty(title)) {
            mTitle.setText(title);
        }

        if (!TextUtils.isEmpty(content)) {
            mContent.setText(content);
        }

        mCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
        return dialog;
    }

}
