package com.quanyan.yhy.ui.wallet.view.gridpasswordview;


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

interface PasswordView {

    //void setError(String error);

    String getPassWord();

    void clearPassword();

    void setPassword(String password);

    void setPasswordVisibility(boolean visible);

    void togglePasswordVisibility();

    void setOnPasswordChangedListener(GridPasswordView.OnPasswordChangedListener listener);

    void setPasswordType(PasswordType passwordType);
}
