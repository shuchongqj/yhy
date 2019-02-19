package com.quanyan.yhy.ui.wallet.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quanyan.yhy.R;

import java.util.Map;

/**
 * Created with Android Studio.
 * Title:***Acitivity
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:田海波
 * Date:2016/8/12
 * Time:10:25
 * Version 2.0.1
 */
public class ViewHelper {
    Map<String, String> tempMap = null;
    LinearLayout linearLayout = null;

    public static ViewHelper get() {
        return new ViewHelper();
    }

    public ViewHelper data(Map<String, String> tempMap) {
        this.tempMap = tempMap;
        return this;
    }

    public ViewHelper parent(LinearLayout linearLayout) {
        this.linearLayout = linearLayout;
        return this;
    }

    public void build(Context context) {

        if (tempMap != null && tempMap.size() > 0 && linearLayout != null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 36, 0, 0);
            for (Map.Entry entry : tempMap.entrySet()) {
                View view = inflater.inflate(R.layout.item_detial_account, null);
                if (entry.getKey().toString().equals("备注")) {
                    if (entry.getValue().toString().equals("")) {
                        return;
                    }
                }
                ((TextView) view.findViewById(R.id.tv_detial_account_front)).setText(entry.getKey().toString());
                ((TextView) view.findViewById(R.id.tv_detial_account_behind)).setText(entry.getValue().toString());
                linearLayout.addView(view, lp);
            }
        }
    }


}