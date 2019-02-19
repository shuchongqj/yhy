package com.quanyan.yhy.ui.views.calendarpicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by dengmingjia on 2015/11/12.
 */
public class PickMonthBean {
    public int year;
    public int month;

    public PickMonthBean(int year, int month) {
        this.year = year;
        this.month = month;
    }

    public static List<PickMonthBean> getMonthBeanList(Calendar startCalendar, Calendar endCalendar) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        List<PickMonthBean> monthBeans = new ArrayList<PickMonthBean>();
        Calendar start = Calendar.getInstance();
        start.setTimeInMillis(startCalendar.getTimeInMillis());
        Calendar end = Calendar.getInstance();
        end.setTimeInMillis(endCalendar.getTimeInMillis());
        do {
            PickMonthBean bean = new PickMonthBean(start.get(Calendar.YEAR), start.get(Calendar.MONTH));
            monthBeans.add(bean);
            start.add(Calendar.MONTH, 1);
        }
        while (start.before(end) || (!start.before(endCalendar) && sdf.format(start.getTimeInMillis()).equals(sdf.format(end.getTimeInMillis()))));
        return monthBeans;
    }

}
