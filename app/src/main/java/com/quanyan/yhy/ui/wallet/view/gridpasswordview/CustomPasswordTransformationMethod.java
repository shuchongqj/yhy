package com.quanyan.yhy.ui.wallet.view.gridpasswordview;

import android.text.method.PasswordTransformationMethod;
import android.view.View;

/**
 * Created with Android Studio.
 * Title:WalletDialog
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-8-9
 * Time:16:03
 * Version 1.0
 * Description:  默认'●'
 */

public class CustomPasswordTransformationMethod extends PasswordTransformationMethod {
    String transformation;

    public CustomPasswordTransformationMethod(String transformation) {
        this.transformation = transformation;
    }

    @Override
    public CharSequence getTransformation(CharSequence source, View view) {
        return new PasswordCharSequence(source);
    }

    private class PasswordCharSequence implements CharSequence {
        private CharSequence mSource;

        public PasswordCharSequence(CharSequence source) {
            mSource = source;
        }

        @Override
        public int length() {
            return mSource.length();
        }

        @Override
        public char charAt(int index) {
            return transformation.charAt(0);
        }

        @Override
        public CharSequence subSequence(int start, int end) {
            return mSource.subSequence(start, end);
        }
    }

}