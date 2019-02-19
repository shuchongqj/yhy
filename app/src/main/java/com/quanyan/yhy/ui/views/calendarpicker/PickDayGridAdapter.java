package com.quanyan.yhy.ui.views.calendarpicker;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.R;
import com.quanyan.yhy.util.CalendarUtil;
import com.yhy.common.beans.calender.PickSku;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by dengmingjia on 2015/11/12.
 * 选择日期的gridadapter
 */
public class PickDayGridAdapter extends BaseAdapter implements View.OnClickListener {
    private Context context;
    private LayoutInflater layoutInflater;
    private int pickType;
    private int daysNumber = 0;
    private int firstdayPosition = 0;
    private int firstdayPositionDays = 0;
    private int year;
    private int month;
    private Integer selectedIndex = null;
    private Integer selectedStartIndex;
    private Integer selectedEndIndex;
    //    private Integer signIndex = null;
    private OnDayChooseListener onDayChooseListener;
    private PickMonthBean monthBean;
    private HashMap<String, PickSku> pickSku;
    private Calendar firstDayCalendar;
    private Calendar lastDayCalendar;

    private Calendar initStartDate;
    private Calendar initEndDate;
    private HashMap<Integer, Calendar> calendarHashMap = new HashMap<>();
    private HashMap<Integer, String> skuCalendarStringHashMap = new HashMap<>();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    DecimalFormat decimalFormat = new DecimalFormat("#########");

    public PickDayGridAdapter(Context context, int pickType, Calendar initStartDate, Calendar initEndDate) {
        this.context = context;
        this.pickType = pickType;
        this.initStartDate = initStartDate;
        this.initEndDate = initEndDate;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public void replace(PickMonthBean monthBean) {
        this.year = monthBean.year;
        this.month = monthBean.month;
        this.monthBean = monthBean;

        firstDayCalendar = CalendarUtil.getCalendarWithMidNight();
        firstDayCalendar.set(year, month, 1);
        firstdayPositionDays = firstDayCalendar.get(Calendar.DAY_OF_MONTH);
        lastDayCalendar = CalendarUtil.getCalendarWithMidNight();
        lastDayCalendar.set(year, month, 1);
        lastDayCalendar.add(Calendar.MONTH, 1);
        lastDayCalendar.add(Calendar.DATE, -1);

        daysNumber = firstDayCalendar.getActualMaximum(Calendar.DAY_OF_MONTH) - firstdayPositionDays + 1;
        firstdayPosition = firstDayCalendar.get(Calendar.DAY_OF_WEEK) - 1;
        notifyDataSetChanged();
    }

    public void setRange(Calendar startCalendar, Calendar endCalendar) {
        if (startCalendar == null && endCalendar == null) {
            selectedStartIndex = -1;
            selectedEndIndex = -1;
            notifyDataSetChanged();
            return;
        }

        if (endCalendar == null) {
            selectedEndIndex = -1;
            if ((startCalendar.before(firstDayCalendar) || startCalendar.after(lastDayCalendar))) {
                selectedStartIndex = -1;
            } else {
                selectedStartIndex = startCalendar.get(Calendar.DATE) - firstdayPositionDays + firstdayPosition;
            }
        } else {
            if (startCalendar.after(lastDayCalendar) || endCalendar.before(firstDayCalendar)) {
                selectedEndIndex = -1;
                selectedStartIndex = -1;
            } else {
                int startTag = startCalendar.compareTo(firstDayCalendar);
                if (startTag < 0) {
                    selectedStartIndex = -1;
                } else if (startTag == 0) {
                    selectedStartIndex = firstdayPosition;
                } else {
                    selectedStartIndex = startCalendar.get(Calendar.DATE) - firstdayPositionDays + firstdayPosition;
                }

                int endTag = endCalendar.compareTo(lastDayCalendar);
                if (endTag < 0) {
                    selectedEndIndex = endCalendar.get(Calendar.DATE) - firstdayPositionDays + firstdayPosition;
                } else if (endTag == 0) {
                    selectedEndIndex = getCount() - 1;
                } else {
                    selectedEndIndex = getCount();
                }
            }
        }
        notifyDataSetChanged();
    }

    public void setSelect(Calendar calendar) {
        if (calendar == null) return;
        Integer temp = null;
        if (pickType == CalendarPickType.SINGLE_NORMAL || pickType == CalendarPickType.SINGLE_SKU) {
            if (year != calendar.get(Calendar.YEAR) || month != calendar.get(Calendar.MONTH)) {
                temp = null;
            } else if (pickType == CalendarPickType.SINGLE_SKU) {
                if (pickSku == null) {
                    temp = null;
                } else {
                    PickSku sku = PickSku.getSku(pickSku, calendar);
                    if (sku == null || sku.stockNum == 0) {
                        temp = null;
                    } else {
                        temp = calendar.get(Calendar.DAY_OF_MONTH) - firstdayPositionDays + firstdayPosition;
                    }
                }
            } else {
                temp = calendar.get(Calendar.DAY_OF_MONTH) - firstdayPositionDays + firstdayPosition;
            }
        } else {
            if (calendar.before(firstDayCalendar)) {
                temp = -1;
            } else if (calendar.after(lastDayCalendar)) {
                temp = getCount();
            } else {
                temp = calendar.get(Calendar.DAY_OF_MONTH) - firstdayPositionDays + firstdayPosition;
            }
        }
        if (temp != selectedIndex) {
            selectedIndex = temp;
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return daysNumber + firstdayPosition;
    }

    @Override
    public Object getItem(int position) {
        return position + firstdayPositionDays - firstdayPosition;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = createView();
        }
        ViewHolder holder = (ViewHolder) convertView.getTag(R.id.tag_holder);
        bindView(holder, position);
        return convertView;
    }

    private void bindView(ViewHolder holder, int position) {
        setVisiable(holder, position);
        if (position < firstdayPosition) return;
        setListener(holder, position);
        Calendar calendar = getCalendar(position);
        if (pickType == CalendarPickType.SINGLE_NORMAL) {
            if (selectedIndex != null && position == selectedIndex) {
                holder.clickView.setBackgroundColor(context.getResources().getColor(R.color.main));
                holder.top.setTextColor(Color.WHITE);
            } else if (calendar.before(initStartDate) || calendar.after(initEndDate)) {
                holder.clickView.setBackgroundColor(context.getResources().getColor(R.color.neu_fafafa));
                holder.top.setTextColor(context.getResources().getColor(R.color.neu_ccc));
            } else {
                holder.clickView.setBackgroundColor(Color.WHITE);
                holder.top.setTextColor(context.getResources().getColor(R.color.neu_666666));
            }
            holder.top.setText(String.valueOf(getItem(position)));
        } else if (pickType == CalendarPickType.SINGLE_SKU) {
            holder.top.setText(String.valueOf(getItem(position)));
            if (calendar.before(initStartDate) || calendar.after(initEndDate)) {
                holder.bottom.setText(null);
                holder.center.setText(null);
                holder.top.setTextColor(context.getResources().getColor(R.color.neu_ccc));
                holder.clickView.setBackgroundColor(context.getResources().getColor(R.color.neu_fafafa));
            } else {
                PickSku sku = pickSku == null ? null : PickSku.getSku(pickSku, getCalendarString(position));
                if (selectedIndex != null && position == selectedIndex) {
                    holder.clickView.setBackgroundColor(context.getResources().getColor(R.color.main));
                    holder.center.setTextColor(Color.WHITE);
                    holder.top.setTextColor(Color.WHITE);
                    holder.bottom.setTextColor(Color.WHITE);
                } else {
                    if (sku == null || sku.stockNum == 0) {
                        holder.top.setTextColor(context.getResources().getColor(R.color.neu_666666));
                        holder.center.setTextColor(context.getResources().getColor(R.color.neu_ccc));
                        holder.bottom.setTextColor(context.getResources().getColor(R.color.neu_ccc));
                        holder.clickView.setBackgroundColor(Color.WHITE);
                    } else {
                        holder.clickView.setBackgroundColor(Color.WHITE);
                        holder.top.setTextColor(context.getResources().getColor(R.color.neu_666666));
                        holder.center.setTextColor(context.getResources().getColor(R.color.neu_40c5d7));
                        holder.bottom.setTextColor(context.getResources().getColor(R.color.main));
                    }
                }

                if (sku == null) {
                    holder.bottom.setText(null);
                    holder.center.setText(null);
                } else {
                    if (sku.stockNum == 0) {
                        holder.center.setText(null);
                        holder.bottom.setText("已售罄");
                    } else {
                        holder.center.setText("余" + sku.stockNum);
                        holder.bottom.setText(converRMb2YunStrNoDot(sku.price));
                    }
                }
            }
        } else if (pickType == CalendarPickType.RANGE) {
            if (calendar.before(initStartDate) || calendar.after(initEndDate)) {
                holder.bottom.setText(null);
                holder.center.setText(null);
                holder.top.setTextColor(context.getResources().getColor(R.color.neu_ccc));
                holder.clickView.setBackgroundColor(context.getResources().getColor(R.color.neu_fafafa));
            } else if (selectedStartIndex == -1 && selectedEndIndex == -1) {
                holder.bottom.setText(null);
                holder.center.setText(null);
                holder.top.setTextColor(context.getResources().getColor(R.color.neu_666666));
                holder.clickView.setBackgroundColor(Color.WHITE);

            } else if (position == selectedStartIndex) {
                holder.top.setTextColor(Color.WHITE);
                holder.bottom.setTextColor(Color.WHITE);
                holder.clickView.setBackgroundColor(context.getResources().getColor(R.color.main));
                holder.bottom.setText("入住");
            } else if (position == selectedEndIndex) {
                holder.top.setTextColor(Color.WHITE);
                holder.bottom.setTextColor(Color.WHITE);
                holder.clickView.setBackgroundColor(context.getResources().getColor(R.color.main));
                holder.bottom.setText("离店");
            } else if (position > selectedStartIndex && position < selectedEndIndex) {
                holder.top.setTextColor(context.getResources().getColor(R.color.neu_666666));
                holder.center.setText(null);
                holder.bottom.setText(null);
                holder.clickView.setBackgroundColor(context.getResources().getColor(R.color.neu_ffdad2));
            } else {
                holder.top.setTextColor(context.getResources().getColor(R.color.neu_666666));
                holder.center.setText(null);
                holder.bottom.setText(null);
                holder.clickView.setBackgroundColor(Color.WHITE);
            }
            holder.top.setText(String.valueOf(getItem(position)));
        }
    }

    private String converRMb2YunStrNoDot(long rmbFen) {
        decimalFormat.setRoundingMode(RoundingMode.DOWN);
        return StringUtil.getFlagRmb(context) + decimalFormat.format(StringUtil.converRMb2Yun(rmbFen));
    }

    private String getCalendarString(int position) {
        String dateString = skuCalendarStringHashMap.get(position);
        if (TextUtils.isEmpty(dateString) && position >= firstdayPosition) {
            Calendar calendar = getCalendar(position);
            if (calendar == null) return null;
            dateString = dateFormat.format(calendar.getTimeInMillis());
            skuCalendarStringHashMap.put(position, dateString);
        }
        return dateString;
    }

    private Calendar getCalendar(int position) {
        Calendar calendar = calendarHashMap.get(position);
        if (calendar == null && position >= firstdayPosition) {
            int day = (int) getItem(position);
            calendar = CalendarUtil.getCalendarWithMidNight();
            calendar.set(year, month, day);
            calendarHashMap.put(position, calendar);
        }
        return calendar;
    }

    private void setListener(ViewHolder holder, int position) {
        holder.clickView.setTag(R.id.tag_position, position);
        holder.clickView.setOnClickListener(this);
    }


    private void setVisiable(ViewHolder holder, int position) {
        if (pickType == CalendarPickType.SINGLE_NORMAL) {
            if (position < firstdayPosition) {
                holder.top.setVisibility(View.INVISIBLE);
            } else {
                holder.top.setVisibility(View.VISIBLE);
            }
            holder.center.setVisibility(View.INVISIBLE);
            holder.bottom.setVisibility(View.INVISIBLE);
        } else {
            if (position < firstdayPosition) {
                holder.top.setVisibility(View.INVISIBLE);
                holder.center.setVisibility(View.INVISIBLE);
                holder.bottom.setVisibility(View.INVISIBLE);
            } else {
                holder.top.setVisibility(View.VISIBLE);
                holder.center.setVisibility(View.VISIBLE);
                holder.bottom.setVisibility(View.VISIBLE);
            }
        }
    }

    private View createView() {
        View v = layoutInflater.inflate(R.layout.calendar_picker_day_view, null);
        v.setTag(R.id.tag_holder, ViewHolder.getViewHolder(v, pickType));
        return v;
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag(R.id.tag_position);
        if (position < firstdayPosition)
            return;
        if (pickType == CalendarPickType.RANGE) {
            if (selectedEndIndex != -1) {
                onDayChoose(position);
            } else if (position != selectedStartIndex) {
                onDayChoose(position);
            }
        } else {
            onDayChoose(position);
        }
    }

    private void onDayChoose(int position) {

        if (onDayChooseListener != null) {
            Calendar calendar = getCalendar(position);
            if (calendar.before(initStartDate) || calendar.after(initEndDate)) {
                return;
            }
            onDayChooseListener.onDayChoose(calendar);
        }
    }

    public void setPickSku(HashMap<String, PickSku> pickSku) {
        this.pickSku = pickSku;
        notifyDataSetChanged();
    }

    static class ViewHolder {
        TextView center, bottom, top;
        View clickView;

        public static ViewHolder getViewHolder(View v, int pickType) {
            ViewHolder holder = new ViewHolder();
            holder.top = (TextView) v.findViewById(R.id.tv_day_top);
            holder.bottom = (TextView) v.findViewById(R.id.tv_day_bottom);
            holder.center = (TextView) v.findViewById(R.id.tv_day_center);
            holder.clickView = v.findViewById(R.id.ll_click);
            return holder;
        }
    }

    public void setOnDayChooseListener(OnDayChooseListener listener) {
        this.onDayChooseListener = listener;
    }

    public interface OnDayChooseListener {
        void onDayChoose(Calendar calendar);
    }
}
