package com.quanyan.yhy.ui.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created with Android Studio.
 * Title:LoginInputView
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:邓明佳
 * Date:2015/11/26
 * Time:18:53
 * Version 1.0
 */

/**
 * 注册登录输入框
 */
public class LoginInputView extends LinearLayout implements View.OnClickListener, TextWatcher {
    @ViewInject(R.id.edit_text)
    EditText editText;
    @ViewInject(R.id.divider)
    View divider;
    @ViewInject(R.id.tv_86)
    TextView tv_86;
    @ViewInject(R.id.tv_xian)
    TextView tv_xian;
    @ViewInject(R.id.iv_delete)
    ImageView delete;
    @ViewInject(R.id.iv_change_mode)
    ImageView eye;
    /**
     * 输入模式
     */
    private Mode mMode;
    private boolean showDelete = false;
    private boolean showEye = false;
    private Context mContext;
    private OnTextChangeListener listener;
    private String nicknameRegEx = "[^_a-zA-Z0-9\u4E00-\u9FA5]";
    private String passwordRegEx = "[^a-zA-Z0-9]";
    private String loginPasswordRegEx = "^(?=.*\\d)(?=.*[a-zA-Z])";
    private String regEx = passwordRegEx;

    public String stringFilter(String str) throws PatternSyntaxException {
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence ss, int start, int before, int count) {
        String editable = editText.getText().toString();
        String str = stringFilter(editable.toString());
        if (!editable.equals(str)) {
            editText.setText(str);
            //设置新的光标所在位置
            editText.setSelection(str.length());
        }

    }

    @Override
    public void afterTextChanged(Editable s) {
        if(!editText.isFocused()){
            return ;
        }
        resetUI();
        if (listener != null) listener.onTextChange(this, s.toString());
    }

    public void setOnTextChangeListener(OnTextChangeListener listener) {
        this.listener = listener;
    }

    public interface OnTextChangeListener {
        public void onTextChange(LoginInputView loginInputView, String text);
    }

    public LoginInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LoginInputView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.login_input_layout, this);
        ViewUtils.inject(this);
        delete.setOnClickListener(this);
        eye.setOnClickListener(this);
        editText.addTextChangedListener(this);
        //获取焦点处理显示情况
        editText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    resetUI();
                } else {
                    delete.setVisibility(GONE);
                }
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void setMode(int mode) {
        if (mode == Mode.LOGIN_PHONE) {
            editText.setHint(R.string.hint_your_phone);
            showDelete = true;
            showEye = false;
            tv_86.setVisibility(VISIBLE);
            tv_xian.setVisibility(VISIBLE);
            editText.setInputType(InputType.TYPE_CLASS_PHONE);
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
        } else if (mode == Mode.LOGIN_PASSWORD) {
            editText.setHint(R.string.hint_your_password);
            showDelete = true;
            showEye = true;
            tv_86.setVisibility(GONE);
            tv_xian.setVisibility(GONE);
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
            regEx = loginPasswordRegEx;
        } else if (mode == Mode.REGISTER_PHONE) {
            editText.setHint(R.string.hint_input_phone_number);
            showDelete = true;
            showEye = false;
            tv_86.setVisibility(VISIBLE);
            tv_xian.setVisibility(VISIBLE);
            //editText.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.ic_login_phone), null, null, null);
            editText.setInputType(InputType.TYPE_CLASS_PHONE);
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
            setDividerVisibility(View.GONE);
        } else if (mode == Mode.FIND_PASSWORD_PHONE) {
            editText.setHint(R.string.hint_your_phone);
            showDelete = true;
            showEye = false;
            tv_86.setVisibility(VISIBLE);
            tv_xian.setVisibility(VISIBLE);
            editText.setInputType(InputType.TYPE_CLASS_PHONE);
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
            setDividerVisibility(View.GONE);
        } else if (mode == Mode.VALIDATE_CODE) {
            editText.setHint(R.string.hint_input_dynamic_code);
            showDelete = true;
            showEye = false;
            tv_86.setVisibility(GONE);
            tv_xian.setVisibility(GONE);
            editText.setInputType(InputType.TYPE_CLASS_PHONE);
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
        } else if (mode == Mode.REGISTER_PWD || mode == Mode.FIND_PASSWORD_PWD) {
            editText.setHint(R.string.please_hint_input_pwd);
            showDelete = true;
            showEye = true;
            tv_86.setVisibility(GONE);
            tv_xian.setVisibility(GONE);
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
        } else if (mode == Mode.NICK_NAME) {
            editText.setHint(R.string.hint_nickname);
            showDelete = true;
            showEye = false;
            tv_86.setVisibility(GONE);
            tv_xian.setVisibility(GONE);
            editText.setInputType(InputType.TYPE_CLASS_TEXT);
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)});
            regEx = nicknameRegEx;
        }
        resetUI();
    }

    private void resetUI() {
        eye.setVisibility(showEye ? View.VISIBLE : View.GONE);
        delete.setVisibility(showDelete && !TextUtils.isEmpty(getText()) ? VISIBLE : GONE);
    }

    /**
     * 更新删除的图标
     * @param resId
     */
    public void updateDeleteIcon(int resId){
        if(delete == null || resId == -1){
            return ;
        }
        delete.setImageResource(resId);
    }

    /**
     * 获取输入框
     * @return
     */
    public EditText getInputBox(){
        return editText;
    }

    public TextView get86TextView(){
        return tv_86;
    }

    public TextView getLineTextView(){
        return tv_xian;
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == delete.getId()) {
            editText.setText(null);
        } else if (id == eye.getId()) {
            if(!StringUtil.isEmpty(editText.getText().toString())){
                int inputType = editText.getInputType();
                if (inputType == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    eye.setImageResource(R.mipmap.ic_normal_password_eye);
                } else {
                    editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    eye.setImageResource(R.mipmap.ic_change_password_eye);
                }
                //fix bug 3758
                Selection.setSelection(editText.getText(), editText.getText().length());
            }
        }
    }

    public static class Mode {
        public static final int LOGIN_PHONE = 1;
        public static final int LOGIN_PASSWORD = 2;
        public static final int REGISTER_PHONE = 3;
        public static final int FIND_PASSWORD_PHONE = 4;
        public static final int VALIDATE_CODE = 5;
        public static final int REGISTER_PWD = 6;
        public static final int FIND_PASSWORD_PWD = 7;
        public static final int NICK_NAME = 8;
    }

    public String getText() {
        return editText.getText().toString();
    }

    public void setText(CharSequence c) {
        editText.setText(c);
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
    }

    public void setDividerVisibility(int visibility) {
        //divider.setVisibility(visibility);
    }
}
