package com.quanyan.yhy.ui.discovery.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with Android Studio.
 * Title:AddLiveEditText
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:赵晓坡
 * Date:16/6/29
 * Time:20:08
 * Version 1.1.0
 */
public class AddLiveEditText extends EditText {

    public static final String regex = "([#])([^#]+)([#])";
    public AddLiveEditText(Context context) {
        super(context);
        initView();
    }

    public AddLiveEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public AddLiveEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AddLiveEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DEL) {
            int selection = getSelectionStart();
            String s = getText().toString();
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(s);
            int index = 0;
            while (matcher.find()) {
                String group = matcher.group();
                index = s.indexOf(group, index);
                if (selection > index && selection <= index + group.length()) {
                    setSelection(index, index + group.length());
//                    return true;
                }
                index += group.length();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        super.onSelectionChanged(selStart, selEnd);
        int selection = selStart;
        String s = getText().toString();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(s);
        int index = 0;
        while (matcher.find()) {
            String group = matcher.group();
            index = s.indexOf(group, index);
            if (selection > index && selection <= index + group.length()) {
                setSelection(index + group.length());
//                    return true;
            }
            index += group.length();
        }
    }

    private void initView() {
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.getParent().requestDisallowInterceptTouchEvent(false);
                } else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                    v.getParent().requestDisallowInterceptTouchEvent(false);
                }
                return false;
            }
        });
    }
}
