package com.quanyan.yhy.ui.common.calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.quanyan.base.BaseActivity;
import com.quanyan.base.util.DateUtil;
import com.quanyan.base.view.BaseNavView;
import com.quanyan.yhy.R;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.views.calendarpicker.CalendarPickType;
import com.quanyan.yhy.ui.views.calendarpicker.CalendarPickerView;
import com.quanyan.yhy.util.CalendarUtil;
import com.yhy.common.beans.calender.PickSku;
import com.yhy.common.utils.SPUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

/**
 * 选择时间页面
 */
public class CalendarSelectActivity extends BaseActivity implements CalendarPickerView.OnDateSelectedListener {

    private static final String SOURCETYPE = "sourceType";
    /**
     * 已选择的
     */
    Calendar mSelectedCalendar;
    /**
     * 范围内起始
     */
    Calendar mStartCalendar;
    /**
     * 范围内结束
     */
    Calendar mEndCalendar;
    /**
     * 选择类型 默认普通单选
     */
    private int mPickType = CalendarPickType.SINGLE_NORMAL;

    /**
     * 底部布局 范围选择时显示
     */
    private View bottomLayout;
    private TextView tvSelectDate;
    private TextView tvSelectPrice;

    private CalendarPickerView pickerView;
    private SimpleDateFormat dateFormat;
    private PickSku mSelectPickSku;

    public static void gotoSelectDate(Activity context, long initStartDate, long initEndDate, Long selectDate, int reqCode, int dateSelectModel, HashMap<String, PickSku> pickSkuHashMap,String sourceType) {
        Intent intent = new Intent(context, CalendarSelectActivity.class);
        intent.putExtra(SPUtils.EXTRA_INIT_START_DATE, initStartDate);
        intent.putExtra(SPUtils.EXTRA_INIT_END_DATE, initEndDate);
        intent.putExtra(SPUtils.EXTRA_TYPE, dateSelectModel);
        intent.putExtra(SPUtils.EXTRA_SELECT_DATE, selectDate);
        intent.putExtra(SPUtils.EXTRA_PICKSKU, pickSkuHashMap);
        intent.putExtra(SOURCETYPE,sourceType);
        context.startActivityForResult(intent, reqCode);
    }

    public static void gotoSelectRangeDate(Activity context, long initStartDate, long initEndDate, Long startDate, Long endDate, int reqCode) {
        Intent intent = new Intent(context, CalendarSelectActivity.class);
        intent.putExtra(SPUtils.EXTRA_TYPE, CalendarPickType.RANGE);
        intent.putExtra(SPUtils.EXTRA_INIT_START_DATE, initStartDate);
        intent.putExtra(SPUtils.EXTRA_INIT_END_DATE, initEndDate);
        intent.putExtra(SPUtils.EXTRA_START_DATE, startDate);
        intent.putExtra(SPUtils.EXTRA_END_DATE, endDate);
        context.startActivityForResult(intent, reqCode);
    }

    @Override
    public boolean isTopCover() {
        return false;
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.ac_cal_select, null);
    }

    private BaseNavView mBaseNavView;

    @Override
    public View onLoadNavView() {
        mBaseNavView = new BaseNavView(this);
        return mBaseNavView;
    }

    /**
     * 初始化数据
     */
    private void initData() {
        initIntentData();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        //默认显示当前本月以及后两个月的日历
        long initStartDate = getIntent().getLongExtra(SPUtils.EXTRA_INIT_START_DATE, 0);
        long initEndDate = getIntent().getLongExtra(SPUtils.EXTRA_INIT_END_DATE, 0);
        Calendar startCalendar;
        if (initStartDate > 0 || mPickType != CalendarPickType.RANGE) {
            startCalendar = CalendarUtil.getCalendarWithMidNight(initStartDate);
        } else {
            startCalendar = Calendar.getInstance();
            startCalendar.setTimeInMillis(DateUtil.getRangeStartDate());
            CalendarUtil.setMidNight(startCalendar);
        }


        Calendar endCalendar;
        if (initEndDate > 0) {
            endCalendar = CalendarUtil.getCalendarWithMidNight(initEndDate);
        } else {
            endCalendar = Calendar.getInstance();
            endCalendar.setTimeInMillis(startCalendar.getTimeInMillis());
            endCalendar.add(Calendar.MONTH, 3);
            endCalendar.set(Calendar.DAY_OF_MONTH, 1);
            endCalendar.add(Calendar.DATE, -1);
        }
        pickerView.setDate(startCalendar, endCalendar, mPickType);
        pickerView.setOnDateSelectedListener(this);

        if (mPickType == CalendarPickType.RANGE) {
            pickerView.withRangeDate(mStartCalendar, mEndCalendar);
            Toast.makeText(this, R.string.please_choose_checkin_date, Toast.LENGTH_LONG).show();
        } else if (mPickType == CalendarPickType.SINGLE_NORMAL) {
            pickerView.withSelectedDate(mSelectedCalendar);
        } else if (mPickType == CalendarPickType.SINGLE_SKU) {
            if (getIntent().getSerializableExtra(SPUtils.EXTRA_PICKSKU) != null) {
                HashMap<String, PickSku> pickSkus = (HashMap<String, PickSku>) getIntent().getSerializableExtra(SPUtils.EXTRA_PICKSKU);
                pickerView.setPickSku(pickSkus);
                setPickSkuDate(PickSku.getSku(pickSkus, mSelectedCalendar));
            }
            pickerView.withSelectedDate(mSelectedCalendar);
        }

    }

    /**
     * 初始化视图
     * @param savedInstanceState
     */
    @Override
    protected void initView(Bundle savedInstanceState) {
        initTitle();
        bottomLayout = findViewById(R.id.ll_bottom);
        tvSelectDate = (TextView) findViewById(R.id.tv_select_date);
        tvSelectPrice = (TextView) findViewById(R.id.tv_select_price);
        pickerView = (CalendarPickerView) findViewById(R.id.pickerView);

        initData();
    }

    /**
     * 初始化标题栏
     */
    private void initTitle() {
        mPickType = getIntent().getIntExtra(SPUtils.EXTRA_TYPE, CalendarPickType.SINGLE_NORMAL);
        String sourceType = getIntent().getStringExtra(SOURCETYPE);
        if(!TextUtils.isEmpty(sourceType)){
            if(sourceType.equals("Line")){
                mBaseNavView.setTitleText("选择出发日期");
            }else{
                if (mPickType == CalendarPickType.RANGE) {
                    mBaseNavView.setTitleText("选择入离店日期");
                } else {
                    mBaseNavView.setTitleText("选择日期");
                }
            }
        }else{
            if (mPickType == CalendarPickType.RANGE) {
                mBaseNavView.setTitleText("选择入离店日期");
            } else {
                mBaseNavView.setTitleText("选择日期");
            }

        }
    }

    /**
     * 选择日期返回上级页面
     */
    private void chooseDateforResult() {
        if (mPickType == CalendarPickType.RANGE) {
            mStartCalendar.add(Calendar.DATE, 21);
            if (!mStartCalendar.after(mEndCalendar)) {
                mStartCalendar.add(Calendar.DATE, -21);
                ToastUtil.showToast(this, R.string.select_range_error);
                return;
            }
            mStartCalendar.add(Calendar.DATE, -21);
        }
        Intent intent = new Intent();
        intent.putExtra(SPUtils.EXTRA_SELECT_DATE, mSelectedCalendar != null ? mSelectedCalendar.getTimeInMillis() : 0);
        intent.putExtra(SPUtils.EXTRA_START_DATE, mStartCalendar != null ? mStartCalendar.getTimeInMillis() : 0);
        intent.putExtra(SPUtils.EXTRA_END_DATE, mEndCalendar != null ? mEndCalendar.getTimeInMillis() : 0);
        if (mPickType == CalendarPickType.SINGLE_SKU && mSelectPickSku != null)
            intent.putExtra(SPUtils.EXTRA_SELECT_DATE, mSelectPickSku);
        setResult(RESULT_OK, intent);
        this.finish();

    }


    /**
     * 初始化上级传递数据
     */
    private void initIntentData() {
        mPickType = getIntent().getIntExtra(SPUtils.EXTRA_TYPE, CalendarPickType.SINGLE_NORMAL);
        long SingleSelectedDate = getIntent().getLongExtra(SPUtils.EXTRA_SELECT_DATE, 0);
        long StartDate = getIntent().getLongExtra(SPUtils.EXTRA_START_DATE, 0);
        long EndDate = getIntent().getLongExtra(SPUtils.EXTRA_END_DATE, 0);
        if (SingleSelectedDate > 0) {
            mSelectedCalendar = CalendarUtil.getCalendarWithMidNight(SingleSelectedDate);
        }
        mStartCalendar = CalendarUtil.getCalendarWithMidNight(StartDate);
        mEndCalendar = CalendarUtil.getCalendarWithMidNight(EndDate);
    }

    /**
     * 选中日期 刷新底部空间数据
     *
     * @param pickSku
     */
    private void setPickSkuDate(PickSku pickSku) {
        if (pickSku == null) return;
        mSelectPickSku = pickSku;
//        tvSelectDate.setText(dateFormat.format(Long.valueOf(pickSku.date)));
//        tvSelectPrice.setText(StringUtil.converRMb2YunWithFlag(pickSku.price) + "/成人");
//        bottomLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDateSelected(Calendar calendar) {
        mSelectedCalendar = calendar;
        chooseDateforResult();
    }

    @Override
    public void onDateRangeSelected(Calendar start, Calendar end) {
        mStartCalendar = start;
        mEndCalendar = end;
        if (mEndCalendar != null) {
            chooseDateforResult();
        } else {
            ToastUtil.showToast(this, "请选择离店时间");
        }
    }

    @Override
    public void onPickSkuSelected(PickSku sku) {
        setPickSkuDate(sku);
        chooseDateforResult();
    }

}
