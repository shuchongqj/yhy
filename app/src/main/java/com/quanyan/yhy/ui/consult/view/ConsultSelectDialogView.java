package com.quanyan.yhy.ui.consult.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.quanyan.yhy.R;
import com.quanyan.yhy.ui.adapter.base.BaseAdapterHelper;
import com.quanyan.yhy.ui.adapter.base.QuickAdapter;

import java.util.List;

/**
 * Created with Android Studio.
 * Title:CancelOrderView
 * Description:咨询选择日期，人数
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:邓明佳
 * Date:2015/12/8
 * Time:16:12
 * Version 1.0
 */
public class ConsultSelectDialogView extends LinearLayout implements View.OnClickListener, AdapterView.OnItemClickListener {
    //取消
    @ViewInject(R.id.tv_cancle)
    TextView cancel;
    //确定
    @ViewInject(R.id.tv_affirm)
    TextView submit;
    @ViewInject(R.id.base_listview)
    ListView listView;
    QuickAdapter adapter;
    //选中的index
    int checkIndex = 0;
    Context context;
    OnButtonClickListener listener;

    List<String>  values;

    public ConsultSelectDialogView(Context context) {
        super(context);
        init(context);
    }

    public ConsultSelectDialogView(Context context, OnButtonClickListener onButtonClickListener, List<String> value,int checkIndex) {
        super(context);
        setOnButtonClickListener(onButtonClickListener);
        if (checkIndex == -1) checkIndex = 0;
        values = value;
        this.checkIndex = checkIndex;
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.view_consult_select_dialog, this);
        ViewUtils.inject(this);
        cancel.setOnClickListener(this);
        submit.setOnClickListener(this);
        adapter = new QuickAdapter<String>(context, R.layout.item_cancel_order_reason, values) {
            @Override
            protected void convert(BaseAdapterHelper helper, String item) {
                TextView reason = helper.getView(R.id.tv_reason);
                int position = helper.getPosition();
                if (position == checkIndex) {
                    reason.setTextColor(context.getResources().getColor(R.color.neu_ff9900));
                } else {
                    reason.setTextColor(context.getResources().getColor(R.color.neu_cccccc));
                }
                reason.setText(item);
            }
        };
        listView.setOnItemClickListener(this);
        listView.setAdapter(adapter);
        listView.setSelection(checkIndex);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == cancel.getId()) {
            if (listener != null) listener.onCancel();
        } else if (id == submit.getId()) {
            if (listener != null) {
                String value = (String) adapter.getItem(checkIndex);
                listener.onSubmit(value);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        checkIndex = position;
        adapter.notifyDataSetInvalidated();
    }

    public interface OnButtonClickListener {
        void onCancel();

        void onSubmit(String value);
    }

    public void setOnButtonClickListener(OnButtonClickListener onButtonClickListener) {
        this.listener = onButtonClickListener;
    }
}
