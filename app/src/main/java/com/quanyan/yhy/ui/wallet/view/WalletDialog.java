package com.quanyan.yhy.ui.wallet.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.quanyan.base.util.MD5Utils;
import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.R;
import com.quanyan.yhy.ui.base.utils.DialogUtil;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.wallet.Controller.WalletController;
import com.quanyan.yhy.ui.wallet.WalletUtils;
import com.quanyan.yhy.ui.wallet.view.gridpasswordview.GridPasswordView;
import com.yhy.common.beans.net.model.paycore.BankCard;
import com.yhy.common.beans.net.model.paycore.WithdrawParam;

/**
 * Created with Android Studio.
 * Title:WalletDialog
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-8-9
 * Time:16:03
 * Version 1.0
 * Description:
 */
public class WalletDialog {

    /**
     * 认证Dialog
     *
     * @param context
     * @return
     */
    public static Dialog showIdentificationDialog(final Context context, final String type) {
        Resources resources = context.getApplicationContext().getResources();
        Dialog dialog = DialogUtil.showMessageDialog2(context, resources.getString(R.string.dialog_auth_cancel_title), resources.getString(R.string.dialog_auth_cancel_content),
                resources.getString(R.string.wallet_btn_ok), resources.getString(R.string.wallet_btn_cancel), new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        //确定
                        if (context instanceof Activity) {
                            NavUtils.gotoRealNameAuthActivity(context, type,"");
                        }
                    }
                }, null);
        dialog.show();
        return dialog;
    }

    public static Dialog showUptateIdentityDialog(final Context context, String content, boolean isFocus, final IdentifyInterface mListener) {
        final Dialog dialog = new Dialog(context, R.style.kangzai_dialog);
        LayoutInflater mLayoutInflater = LayoutInflater.from(context);
        View view = mLayoutInflater.inflate(R.layout.dialog_show_updateidentify, null);
        TextView tvCancel = (TextView) view.findViewById(R.id.iv_identify_cancel);
        if (!TextUtils.isEmpty(content))
            ((TextView) view.findViewById(R.id.msg_dlg_content)).setText(content);
        if (isFocus) {
            tvCancel.setVisibility(View.GONE);
        }
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                mListener.OnIdentifyCancel();
            }
        });
        view.findViewById(R.id.iv_identify_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                mListener.OnIdentifyConfirm();
            }
        });

        view.findViewById(R.id.iv_identify_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                mListener.OnIdentifyCancel();
            }
        });
        dialog.setCancelable(false);
        dialog.setContentView(view);
        dialog.show();

        return dialog;

    }


    public static Dialog showAuthFailedDialog(final Context context, String content,final ToolsInterface mToolsInterface) {

        LayoutInflater mLayoutInflater = LayoutInflater.from(context);
        View view = mLayoutInflater.inflate(R.layout.dialog_show_authfaild, null);
        final Dialog dialog = new Dialog(context, R.style.kangzai_dialog);
        dialog.setCancelable(false);
        dialog.setContentView(view);
        dialog.show();
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        int screenW = getScreenWidth(context);
        lp.width = screenW;
        if (!TextUtils.isEmpty(content))
            ((TextView) view.findViewById(R.id.tv_authfaild_content)).setText(content);
        view.findViewById(R.id.btn_takephoto_agin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                mToolsInterface.OnShowDialog();
            }
        });
        view.findViewById(R.id.iv_identify_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });

        return dialog;

    }

    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * 显示提现输入金额dialog
     *
     * @param context
     * @return
     */
    public static Dialog showPayDialog(final Context context, final String sub, final WalletController mWalletController, final BankCard mBankCard) {
        LayoutInflater mLayoutInflater = LayoutInflater.from(context);
        View view = mLayoutInflater.inflate(R.layout.pay_pass_dialog, null);
        final Dialog dialog = new Dialog(context, R.style.kangzai_dialog);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);
        final GridPasswordView mCustomUi = (GridPasswordView) view.findViewById(R.id.gpv_customUi);
        TextView mForgetPass = (TextView) view.findViewById(R.id.tv_forget_pas);
        TextView mMoney = (TextView) view.findViewById(R.id.tv_money);
        mMoney.setText(sub);
        mCustomUi.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
            @Override
            public void onTextChanged(String psw) {
                if (!TextUtils.isEmpty(psw) && psw.length() == 6) {
                    WalletUtils.hideSoft(context, mCustomUi.getWindowToken());
                    WithdrawParam param = new WithdrawParam();
                    param.bindCardId = mBankCard.bindCardId;
                    param.payPwd = MD5Utils.toMD5(psw);
                    Log.i("Money", WalletUtils.formateMoney(Double.parseDouble(sub)) + "");
                    param.withdrawAmount = WalletUtils.formateMoney(Double.parseDouble(sub));
                    mWalletController.doWithdraw(param, context);
                }
            }

            @Override
            public void onInputFinish(String psw) {

            }
        });

        mForgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavUtils.gotoBindCardActivity(context, 2);
            }
        });
        dialog.show();
        return dialog;
    }


    /**
     * 钱包支付,提现 弹框
     *
     * @param context
     * @param sub
     * @return
     */
    public static Dialog showOrderPayDialog(View view, final Context context, final long sub, final int sourceType) {
        TextView tv_subtitle = (TextView) view.findViewById(R.id.tv_subtitle);
        if (sourceType == 1) {
            tv_subtitle.setText("");
        } else {
            tv_subtitle.setText("提现");
        }
        TextView mMoney = (TextView) view.findViewById(R.id.tv_money);
        mMoney.setText(StringUtil.formatWalletMoneyNoFlg(sub));
        Dialog dialog = new Dialog(context, R.style.kangzai_dialog);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        return dialog;
    }

    public interface IdentifyInterface {
        void OnIdentifyConfirm();

        void OnIdentifyCancel();
    }

    public interface ToolsInterface {
        void OnShowDialog();


    }
}
