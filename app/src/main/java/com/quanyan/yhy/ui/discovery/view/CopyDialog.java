package com.quanyan.yhy.ui.discovery.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.quanyan.yhy.R;
import com.quanyan.yhy.ui.adapter.CopyAlertAdapter;

/**
 * Created with Android Studio.
 * Title:CopyDialog
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-1-14
 * Time:14:33
 * Version 1.0
 * Description:
 */
public class CopyDialog extends Dialog {

    private String textView;
    private ListView lv_list;
    private Context mContext;
    private CopyAlertAdapter adapter;

    public CopyDialog(Context context, int theme, String textView) {
        super(context, theme);
        this.mContext = context;
        this.textView = textView;
        this.setCanceledOnTouchOutside(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_alert_layout);
        initView();
    }

    private void initView() {
        lv_list = (ListView) this.findViewById(R.id.lv_list);
        adapter = new CopyAlertAdapter(mContext);
        lv_list.setAdapter(adapter);
        initClick();
    }

    private void initClick() {
        lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        copyContent(mContext, textView);
                        dismiss();
                        break;
                }
            }
        });
    }

    //复制逻辑
    private void copyContent(Context context, String textContent) {
        ClipboardManager cmb = (ClipboardManager) context
                .getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(textContent);
    }
}
