package com.quanyan.yhy.ui.views.calendarpicker;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.quanyan.yhy.R;
import com.yhy.common.beans.calender.PickSku;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by dengmingjia on 2015/11/12.
 * 日期选择layout界面 可根据时间间隔添加多个月份布局
 */
public class CalendarPickerView extends LinearLayout implements PickDayGridAdapter.OnDayChooseListener {
    private Context context;
    private LayoutInflater inflater;
    /**
     * 已选中的时间
     */
    private Calendar mSelectedCalendar;
    /**
     * 选择时间类型
     */
    private int pickeType;
    private ArrayList<CalendarPickMonthView> monthsViews = new ArrayList<>();
    private ArrayList<PickDayGridAdapter> gridAdapters = new ArrayList<>();
    private LinearLayout contentLayout;
    private HashMap<String, PickSku> pickSku;
    SimpleDateFormat format;
    /**
     * 范围选择开始
     */
    private Calendar mStartCalendar;
    /**
     * 范围选择结束
     */
    private Calendar mEndCalendar;
    //选择范围的模式，选择起始，选择结束
    private Range rangeMode;

    public enum Range {
        START, END;
    }


    public CalendarPickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CalendarPickerView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.calendar_picker_view, this);
        contentLayout = (LinearLayout) findViewById(R.id.ll_content_layout);
        setBackgroundResource(R.color.white);
        setBackgroundColor(Color.WHITE);
        setOrientation(VERTICAL);
        format = new SimpleDateFormat("yyyy-MM-dd");
    }

    public void setDate(Calendar start, Calendar end, int pickeType) {
        this.pickeType = pickeType;
        List<PickMonthBean> monthBeans = PickMonthBean.getMonthBeanList(start, end);
        //清空所有视图 从新创建
        contentLayout.removeAllViews();
        monthsViews.clear();
        gridAdapters.clear();

        for (int i = 0; i < monthBeans.size(); i++) {
            PickMonthBean bean = monthBeans.get(i);
            contentLayout.addView(createMouthView(bean, start, end));
        }
    }

    // 创建一个月的日历view
    private View createMouthView(PickMonthBean bean, Calendar start, Calendar end) {
        CalendarPickMonthView view = new CalendarPickMonthView(context);
        view.setMonth(bean.year + "-" + (bean.month + 1));
        final PickDayGridAdapter adapter = new PickDayGridAdapter(context, pickeType, start, end);
        adapter.replace(bean);
        view.setAdapter(adapter);
        adapter.setOnDayChooseListener(this);
        monthsViews.add(view);
        gridAdapters.add(adapter);
        return view;
    }


    /**
     * 设置选中日期
     */
    public void withSelectedDate(Calendar calendar) {
        mSelectedCalendar = calendar;
        for (int i = 0; i < gridAdapters.size(); i++) {
            gridAdapters.get(i).setSelect(calendar);
        }
    }

    /**
     * 设置选中范围
     *
     * @param start
     * @param end
     */
    public void withRangeDate(Calendar start, Calendar end) {
        mStartCalendar = start;
        mEndCalendar = end;
        for (int i = 0; i < gridAdapters.size(); i++) {
            gridAdapters.get(i).setRange(mStartCalendar, mEndCalendar);
        }
        refreshRangeMode();
    }

    /**
     * 刷新选择范围的模式
     */
    public void refreshRangeMode() {
        if (rangeMode == null || rangeMode == Range.END) {
            rangeMode = Range.START;
        } else {
            rangeMode = Range.END;
        }
    }

    @Override
    public void onDayChoose(Calendar calendar) {
        if (pickeType == CalendarPickType.SINGLE_NORMAL) {
            withSelectedDate(calendar);
            if (onDateSelectedListener != null) onDateSelectedListener.onDateSelected(calendar);
        } else if (pickeType == CalendarPickType.SINGLE_SKU) {
            if (pickSku != null) {
                PickSku sku = PickSku.getSku(pickSku, calendar);
                if (sku != null && sku.stockNum > 0) {
                    withSelectedDate(calendar);
                    if (onDateSelectedListener != null)
                        onDateSelectedListener.onPickSkuSelected(sku);
                }
            }
        } else if (pickeType == CalendarPickType.RANGE) {
            if (rangeMode == Range.START) {
                mStartCalendar = calendar;
                mEndCalendar = null;
            } else if (rangeMode == Range.END) {
                if (mStartCalendar.after(calendar)) {
                    mEndCalendar = mStartCalendar;
                    mStartCalendar = calendar;
                } else {
                    mEndCalendar = calendar;
                }
            }
            withRangeDate(mStartCalendar, mEndCalendar);
            if (onDateSelectedListener != null)
                onDateSelectedListener.onDateRangeSelected(mStartCalendar, mEndCalendar);
        }
    }

    public void setOnDateSelectedListener(OnDateSelectedListener onDateSelectedListener) {
        this.onDateSelectedListener = onDateSelectedListener;
    }

    public void setPickSku(HashMap<String, PickSku> pickSku) {
        this.pickSku = pickSku;
        for (int i = 0; i < gridAdapters.size(); i++) {
            gridAdapters.get(i).setPickSku(pickSku);
        }
    }

    public interface OnDateSelectedListener {
        void onDateSelected(Calendar calendar);

        void onDateRangeSelected(Calendar start, Calendar end);

        void onPickSkuSelected(PickSku sku);
    }

    private OnDateSelectedListener onDateSelectedListener;

}
