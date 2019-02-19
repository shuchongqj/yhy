package com.newyhy.views;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * 标红
 * Created by Jiervs on 2018/7/11.
 */

public class KeyTextView extends AppCompatTextView {

    public KeyTextView(Context context) {
        super(context);
    }

    public KeyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public KeyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setKeyTextsColor(String text, String specifiedTexts, int color) {
        List<Integer> sTextsStartList = new ArrayList<>();

        int sTextLength = specifiedTexts.length();
        String temp = text;
        int lengthFront = 0;//记录被找出后前面的字段的长度
        int start = -1;
        do {
            start = temp.indexOf(specifiedTexts);

            if (start != -1) {
                start = start + lengthFront;
                sTextsStartList.add(start);
                lengthFront = start + sTextLength;
                temp = text.substring(lengthFront);
            }

        } while (start != -1);

        SpannableStringBuilder styledText = new SpannableStringBuilder(text);
        for (Integer i : sTextsStartList) {
            styledText.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(color)), i, i + sTextLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        setText(styledText);
    }
}
