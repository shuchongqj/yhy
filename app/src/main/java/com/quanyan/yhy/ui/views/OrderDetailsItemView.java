package com.quanyan.yhy.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quanyan.yhy.R;

/**
 * Created by dengmingjia on 2015/11/19.
 */
public class OrderDetailsItemView extends LinearLayout {
    public TextView mleft;
    public TextView mright;

    public OrderDetailsItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public OrderDetailsItemView(Context context) {
        super(context);
        init(context);
    }

    public OrderDetailsItemView(Context context, String key, String value) {
        super(context);
        init(context);
        setMessage(key, value);
    }

    private void init(Context context) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_order_details, this);
        mleft = (TextView) v.findViewById(R.id.left);
        mright = (TextView) v.findViewById(R.id.right);
    }

    public OrderDetailsItemView setMessage(String left, String right) {
        mleft.setText(left);
        mright.setText(right);
        return this;
    }

    public void setImage(String left, int imageResid, OnClickListener onClickListener) {
        setOnClickListener(onClickListener);
        mleft.setText(left);
        mright.setBackgroundResource(imageResid);
    }

    public void setValue(String value) {
        mright.setText(value);
    }
}
