package com.quanyan.yhy.ui.wallet;

import android.content.Context;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.yhy.common.beans.net.model.paycore.Bill;
import com.yhy.common.constants.ValueConstants;

import java.text.SimpleDateFormat;

/**
 * Created with Android Studio.
 * Title:WalletUtils
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-8-10
 * Time:15:26
 * Version 1.0
 * Description:
 */
public class WalletUtils {

    //储蓄卡
    private static final String DEPOSIT_CARD = "DEPOSIT_CARD";
    //信用卡
    private static final String CREDIT_CARD = "CREDIT_CARD";

    //验证码 类型
    public static final String OPEN_ELE_ACCOUNT = "OPEN_ELE_ACCOUNT";//开户
    public static final String BIND_BANK_CARD = "BIND_BANK_CARD";//绑定银行卡
    public static final String SETUP_PAY_PWD = "SETUP_PAY_PWD";//设置支付密码
    public static final String FORGET_PAY_PWD = "FORGET_PAY_PWD";//忘记支付密码
    public static final String UPDATE_PAY_PWD = "UPDATE_PAY_PWD";//更新支付密码

    public static final String IS_MERCHANT = "IS_MERCHANT";
    public static final String NOT_MERCHANT = "NOT_MERCHANT";

    public static final String TACK_EFFECT = "TACK_EFFECT";
    public static final String FREEZE = "FREEZE";
    public static final String LOGOUT = "LOGOUT";
    public static final String INVALID = "INVALID";
    public static final String VERIFIED = "VERIFIED";

    public static final String PERSON = "PERSON";
    public static final String COMPANY = "COMPANY";

    public static final String SYSTEM_ERROR = "SYSTEM_ERROR";

    public static final String APP = "APP";

    public static final String PAY_PWD_ERROR = "PAY_PWD_ERROR";
    public static final String PAY_PWD_MORE_THAN_MAXIMUM_RETRIES = "PAY_PWD_MORE_THAN_MAXIMUM_RETRIES";


    /**
     * 设置输入框只能输入两位小数
     *
     * @param editText
     */
    public static void setPricePoint(final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        editText.setText(s);
                        editText.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    editText.setText(s);
                    editText.setSelection(2);
                }

                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        editText.setText(s.subSequence(0, 1));
                        editText.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });
    }

    public static void hideSoft(Context context, IBinder windowToken) {
        //1.得到InputMethodManager对象
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        //2.调用hideSoftInputFromWindow方法隐藏软键盘
        imm.hideSoftInputFromWindow(windowToken, 0); //强制隐藏键盘
    }

    public static void showSoft(Context context) {
        //1.得到InputMethodManager对象
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        //2.调用toggleSoftInput方法，实现切换显示软键盘的功能。
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }


    /*
     * 校验银行卡卡号
	 *
	 * @param cardId
	 *
	 * @return
	 */
    public static boolean checkBankCard(String cardId) {
        if (!TextUtils.isEmpty(cardId) && cardId.length() >= 16) {
            if (cardId.length() >= 16 && cardId.length() <= 19) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     *
     * @param nonCheckCodeCardId
     * @return
     */
    public static char getBankCardCheckCode(String nonCheckCodeCardId) {
        if (nonCheckCodeCardId == null
                || nonCheckCodeCardId.trim().length() == 0
                || !nonCheckCodeCardId.matches("\\d+")) {
            // 如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }

    /**
     * @param bankCardType
     * @return
     */
    public static String getBankByCode(String bankCardType) {
        if (TextUtils.isEmpty(bankCardType)) {
            return "";
        }
        if (bankCardType.equals(DEPOSIT_CARD)) {
            return "储蓄卡";
        } else if (bankCardType.equals(CREDIT_CARD)) {
            return "信用卡";
        } else {
            return "";
        }
    }

    /**
     * 转换电话
     *
     * @param phone
     * @return
     */
    public static String formatePhone(String phone) {
        if (TextUtils.isEmpty(phone)) {
            return "";
        }

        if (phone.length() != 11) {
            return "";
        }

        String a = phone.substring(0, 3);
        String b = phone.substring(7, phone.length());

        return a + "****" + b;
    }

    /**
     * 将 元换成分
     *
     * @param money
     * @return
     */
    public static long formateMoney(double money) {
        return (long) (money * 100);
    }


    public static String getIncomeExpenceType(Bill bill) {

        if (bill != null) {
            if (ValueConstants.WALLET_INCOME_EXPENCE_TOP_UP.equals(bill.transType)) {
                return "充值";
            } else if (ValueConstants.WALLET_INCOME_EXPENCE_WITHDRAW.equals(bill.transType)) {
                return "提现";
            } else if (ValueConstants.WALLET_INCOME_EXPENCE_SETTLEMENT.equals(bill.transType)) {
                return "结算";
            } else if (ValueConstants.WALLET_INCOME_EXPENCE_BALANCE_PAY.equals(bill.transType)) {
                return "消费";
            } else if (ValueConstants.WALLET_INCOME_EXPENCE_TRANSFER_IN.equals(bill.transType)) {
                return "资金转入";
            } else if (ValueConstants.WALLET_INCOME_EXPENCE_TRANSFER_OUT.equals(bill.transType)) {
                return "资金转出";
            }
        }
        return "";
    }

    public static long getIncomeExpenceAccount(Bill bill) {

        if (bill != null) {
            if (ValueConstants.WALLET_INCOME_EXPENCE_TOP_UP.equals(bill.transType)) {
                return bill.transAmount;
            } else if (ValueConstants.WALLET_INCOME_EXPENCE_WITHDRAW.equals(bill.transType)) {
                return -bill.transAmount;
            } else if (ValueConstants.WALLET_INCOME_EXPENCE_SETTLEMENT.equals(bill.transType)) {
                return bill.transAmount;
            } else if (ValueConstants.WALLET_INCOME_EXPENCE_BALANCE_PAY.equals(bill.transType)) {
                return -bill.transAmount;
            } else if (ValueConstants.WALLET_INCOME_EXPENCE_TRANSFER_IN.equals(bill.transType)) {
                return bill.transAmount;
            } else if (ValueConstants.WALLET_INCOME_EXPENCE_TRANSFER_OUT.equals(bill.transType)) {
                return -bill.transAmount;
            }
        }
        return 0;
    }

    public static String getFormatData(long mills) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return dateFormat.format(mills);
    }


    /**
     * 转换姓名
     *
     * @param userName
     * @return
     */
    public static String formatUserName(String userName) {
        if (TextUtils.isEmpty(userName)) {
            return "";
        }
        String b = userName.substring(userName.length() - 1, userName.length());
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < userName.length() - 1; i++) {
            buffer.append("*");
        }
        return buffer + b;
    }

}
