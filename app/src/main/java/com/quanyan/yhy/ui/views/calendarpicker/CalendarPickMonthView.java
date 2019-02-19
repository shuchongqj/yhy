package com.quanyan.yhy.ui.views.calendarpicker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.quanyan.yhy.R;
import com.quanyan.yhy.ui.base.views.LineGridView;

/**
 * Created by dengmingjia on 2015/11/12.
 * 日历每月的view
 */
public class CalendarPickMonthView extends LinearLayout {
    public TextView tvMonth;
    public LineGridView girdView;

    public CalendarPickMonthView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.calendar_picker_mouth_view, this);
        tvMonth = (TextView) findViewById(R.id.tv_month);
        girdView = (LineGridView) findViewById(R.id.grid_view);
        girdView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE)
                    return true;
                return false;
            }
        });
    }

    public void setMonth(String month) {
        tvMonth.setText(month);
    }

    public void setAdapter(PickDayGridAdapter adapter) {
        girdView.setAdapter(adapter);
    }

    public ListAdapter getAdapter() {
        return girdView.getAdapter();
    }
}
