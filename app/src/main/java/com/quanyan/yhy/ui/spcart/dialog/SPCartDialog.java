package com.quanyan.yhy.ui.spcart.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quanyan.yhy.R;
import com.quanyan.yhy.ui.spcart.view.CartEditNumberChoose;

/**
 * Created with Android Studio.
 * Title:SPCartDialog
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-12-19
 * Time:9:42
 * Version 1.0
 * Description:
 */
public class SPCartDialog {

    /**
     * 显示加入购物成功失败的dialog
     *
     * @param context
     * @param content
     * @return
     */
    public static Dialog showSaveToSpcartDialog(final Context context, String content, int imageResource) {
        final Dialog dialog = new Dialog(context, R.style.translateDialogStyle);
        LayoutInflater mLayoutInflater = LayoutInflater.from(context);
        View view = mLayoutInflater.inflate(R.layout.dialog_savetocart, null);
        TextView mContent = (TextView) view.findViewById(R.id.tv_save_spcart_content);
        ImageView mSaveCartImage = (ImageView) view.findViewById(R.id.iv_save_spcart_image);
        mSaveCartImage.setImageResource(imageResource);
        mContent.setText(TextUtils.isEmpty(content) ? "" : content);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(view);
        dialog.show();
        return dialog;
    }

    /**
     * 删除
     *
     * @param context
     * @return
     */
    public static Dialog showDeleteDialog(Context context, final View.OnClickListener mClick) {
        Dialog dialog = new Dialog(context, R.style.kangzai_dialog);
        LayoutInflater mLayoutInflater = LayoutInflater.from(context);
        View view = mLayoutInflater.inflate(R.layout.dialog_spcart_delete, null);
        RelativeLayout mDeleteLayout = (RelativeLayout) view.findViewById(R.id.rl_delete);
        dialog.setCanceledOnTouchOutside(true);
        if (mClick != null) {
            mDeleteLayout.setOnClickListener(mClick);
        }
        dialog.setContentView(view);
        dialog.show();
        return dialog;
    }


    /**
     * @param context
     * @return
     */
    public static Dialog showUpdateNumberDialog(Context context, String title, String okText, String cancleText,
                                                final UpdateNumberDialogClick posLsn, int maxNum, int minNum, int currentNum) {
        final Dialog dialog = new Dialog(context, R.style.kangzai_dialog);
        LayoutInflater mLayoutInflater = LayoutInflater.from(context);
        View view = mLayoutInflater.inflate(R.layout.dialog_update_number, null);
        TextView mCancleTV = (TextView) view.findViewById(R.id.msg_dialog_btn_cancel);
        final CartEditNumberChoose mCartEditNumberChoose = (CartEditNumberChoose) view.findViewById(R.id.cart_edit_number);
        mCartEditNumberChoose.initCheckValue(maxNum, minNum, currentNum);
        TextView mOKTV = (TextView) view.findViewById(R.id.msg_dialog_btn_ok);
        TextView mTitle = (TextView) view.findViewById(R.id.msg_dlg_title);
        mTitle.setText(TextUtils.isEmpty(title) ? "" : title);
        mOKTV.setText(TextUtils.isEmpty(okText) ? "" : okText);
        mCancleTV.setText(TextUtils.isEmpty(cancleText) ? "" : cancleText);

        mOKTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCartEditNumberChoose.getNum() != -1) {
                    dialog.dismiss();
                    posLsn.config(mCartEditNumberChoose.getNum());
                }
            }
        });
        mCancleTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(view);
        dialog.show();
        return dialog;
    }


    public interface UpdateNumberDialogClick {
        void config(int num);
    }

    /**
     * 显示库存不足或者商品下架弹框提示
     *
     * @param context
     * @return
     */
    public static Dialog showSoldOutDialog(Context context, String mContent, String mBtnTitle, final View.OnClickListener mClick) {
        Dialog dialog = new Dialog(context, R.style.kangzai_dialog);
        LayoutInflater mLayoutInflater = LayoutInflater.from(context);
        View view = mLayoutInflater.inflate(R.layout.dialog_sold_out, null);
        TextView mConText = (TextView) view.findViewById(R.id.tv_content);
        TextView mBtnText = (TextView) view.findViewById(R.id.tv_btn);
        RelativeLayout mBtnLayout = (RelativeLayout) view.findViewById(R.id.rl_btn_layout);
        mConText.setText(TextUtils.isEmpty(mContent) ? "" : mContent);
        mBtnText.setText(TextUtils.isEmpty(mBtnTitle) ? "" : mBtnTitle);
        if (mClick != null)
            mBtnLayout.setOnClickListener(mClick);
        dialog.setCancelable(false);
        dialog.setContentView(view);
        return dialog;
    }
}
