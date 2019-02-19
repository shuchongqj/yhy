package com.videolibrary.puhser.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quanyan.yhy.R;

/**
 * Created with Android Studio.
 * Title:DefinitionDialogView
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:邓明佳
 * Date:2016/9/12
 * Time:14:35
 * Version 1.0
 */
public class DefinitionDialogView extends LinearLayout implements View.OnClickListener {
    TextView[] definitions;
    TextView cancel;

    public DefinitionDialogView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DefinitionDialogView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.dialog_definition, this);
        definitions = new TextView[3];
        definitions[0] = (TextView) findViewById(R.id.dialog_definition_1);
        definitions[0].setOnClickListener(this);
        definitions[1] = (TextView) findViewById(R.id.dialog_definition_2);
        definitions[1].setOnClickListener(this);
        definitions[2] = (TextView) findViewById(R.id.dialog_definition_3);
        definitions[2].setOnClickListener(this);
        cancel = (TextView) findViewById(R.id.dialog_definition_cancel);
        cancel.setOnClickListener(this);
    }

    public void setSelectedIndex(int position) {
        if (position < 0 || position > definitions.length - 1) {
            return;
        }
        for (int i = 0; i < definitions.length; i++) {
            if (i == position) {
                definitions[i].setTextColor(getResources().getColor(R.color.quan_red));
            } else {
                definitions[i].setTextColor(getResources().getColor(R.color.neu_666666));
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == cancel.getId()) {
            if (onDefinitionSelectedListener != null) {
                onDefinitionSelectedListener.onDefintionSelectCancel();
            }
        } else {
            if (onDefinitionSelectedListener != null) {
                for (int i = 0; i < definitions.length; i++) {
                    if (definitions[i] == v) {
                        onDefinitionSelectedListener.onDefinitionSelected(i, definitions[i].getText().toString());
                        break;
                    }
                }
            }
        }
    }

    OnDefinitionSelectedListener onDefinitionSelectedListener;

    public interface OnDefinitionSelectedListener {
        void onDefinitionSelected(int index, String defintion);

        void onDefintionSelectCancel();
    }

    public void setOnDefinitionSelectedListener(OnDefinitionSelectedListener listener) {
        onDefinitionSelectedListener = listener;
    }
}
