package com.quanyan.yhy.ui.tab.homepage.order;

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
import com.yhy.common.beans.net.model.tm.TmCloseOrderReasonItem;

import java.util.List;

/**
 * Created with Android Studio.
 * Title:CancelOrderView
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:邓明佳
 * Date:2015/12/8
 * Time:16:12
 * Version 1.0
 */
public class CancelOrderView extends LinearLayout implements View.OnClickListener, AdapterView.OnItemClickListener {
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
    int checkIndex = 1;
    Context context;
    CancelOrderClickListener listener;
    /**
     * 关闭原因
     */
    List<TmCloseOrderReasonItem> reasonItems;

    public CancelOrderView(Context context) {
        super(context);
        init(context);
    }

    public CancelOrderView(Context context, CancelOrderClickListener cancelOrderClickListener, List<TmCloseOrderReasonItem> value) {
        super(context);
        setCancelOrderClickListener(cancelOrderClickListener);
        reasonItems = value;
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.cancel_order_dialog_view, this);
        ViewUtils.inject(this);
        cancel.setOnClickListener(this);
        submit.setOnClickListener(this);
        adapter = new QuickAdapter<TmCloseOrderReasonItem>(context, R.layout.item_cancel_order_reason, reasonItems) {
            @Override
            protected void convert(BaseAdapterHelper helper, TmCloseOrderReasonItem item) {
                TextView reason = helper.getView(R.id.tv_reason);
                int position = helper.getPosition();
                if (position == checkIndex) {
                    reason.setTextColor(context.getResources().getColor(R.color.red_ying));
                } else {
                    reason.setTextColor(context.getResources().getColor(R.color.neu_cccccc));
                }
                reason.setText(item.reasonText);
            }
        };
        listView.setOnItemClickListener(this);
        listView.setAdapter(adapter);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == cancel.getId()) {
            if (listener != null) listener.onCancel();
        } else if (id == submit.getId()) {
            if (listener != null) {
                TmCloseOrderReasonItem item = (TmCloseOrderReasonItem) adapter.getItem(checkIndex);
                listener.onSubmit(item.reasonId,item.reasonText);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        checkIndex = position;
        adapter.notifyDataSetInvalidated();
    }

    public interface CancelOrderClickListener {
        void onCancel();

        void onSubmit(long reasonId,String reasonText);
    }

    public void setCancelOrderClickListener(CancelOrderClickListener cancelOrderClickListener) {
        this.listener = cancelOrderClickListener;
    }
}
