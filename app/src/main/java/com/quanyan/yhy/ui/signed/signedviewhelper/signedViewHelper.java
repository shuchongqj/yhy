package com.quanyan.yhy.ui.signed.signedviewhelper;

import android.content.Context;
import android.view.View;

import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.AnalyDataValue;
import com.quanyan.yhy.common.TaskStatus;
import com.quanyan.yhy.ui.adapter.base.BaseAdapterHelper;
import com.quanyan.yhy.ui.adapter.base.QuickAdapter;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.base.utils.TCEventHelper;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.yhy.common.beans.net.model.param.WebParams;
import com.yhy.common.beans.net.model.point.PointsDetail;
import com.yhy.common.beans.net.model.tm.Task;
import com.yhy.common.utils.SPUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with Android Studio.
 * Title:AboutAndFeedController
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:wjm
 * Date:16/621
 * Version 1.3.0
 */
public class signedViewHelper {

    /***
     * 兑奖记录
     *
     * @param context
     * @return
     */
    public static QuickAdapter<Task> setSignedDAdapter(Context context) {


        QuickAdapter<Task> adapter = new QuickAdapter<Task>(context, R.layout.item_sigend_list, new ArrayList<Task>()) {
            @Override
            protected void convert(BaseAdapterHelper helper, final Task item) {
                helper.setImageUrl(R.id.iv_eat_product_bg, item.photosAddress, 0, 0, R.mipmap.icon_default_215_150);
                helper.setText(R.id.tv_signed_title, item.name.trim());

                if (item.amount > 0) {
                    helper.setText(R.id.tv_signed_fraction, "+" + item.amount);
                } else if (item.amount < 0) {
                    helper.setText(R.id.tv_signed_fraction, "-" + item.amount);
                } else {
                    if(!StringUtil.isEmpty(item.tiltle)) {
                        helper.setText(R.id.tv_signed_fraction, item.tiltle);
                    }
                }
                if (TaskStatus.UNFINISHED.equals(item.status)) {//0:未完成
                    helper.setVisibleOrInVisible(R.id.tv_signed_complete, false);
                    helper.setVisibleOrInVisible(R.id.tv_signed_go_complete, true);
                    helper.setTextColor(R.id.tv_signed_fraction, context.getResources().getColor(R.color.ac_title_bg_color));
                } else if (TaskStatus.FINISHED.equals(item.status)) {//1:已完成
                    helper.setVisibleOrInVisible(R.id.tv_signed_complete, true);
                    helper.setVisibleOrInVisible(R.id.tv_signed_go_complete, false);
                    helper.setTextColor(R.id.tv_signed_fraction, context.getResources().getColor(R.color.tv_color_gray3));
                } else if (TaskStatus.NO_SHOW.equals(item.status)) {//2:不显示
                    helper.setVisibleOrInVisible(R.id.tv_signed_complete, false);
                    helper.setVisibleOrInVisible(R.id.tv_signed_go_complete, false);
                    helper.setTextColor(R.id.tv_signed_fraction, context.getResources().getColor(R.color.ac_title_bg_color));
                }
                helper.setOnClickListener(R.id.tv_signed_go_complete, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tcEvent(context, item);
                        String jumpType = item.jumpType;
                        if (TaskStatus.NO_JUMP.equals(jumpType)) {
                        } else if (TaskStatus.PAGE_FOUND.equals(jumpType)) {
                            String code = item.code;
                            if (code.equals("101")) {
                                NavUtils.gotoAddLiveActivity(context);
                            } else {
                                NavUtils.gotoDiscoveryFragment(context, 0);
                            }

                        } else if (TaskStatus.URL.equals(jumpType)) {
//                            WebParams wp = new WebParams();
//                            wp.setUrlAndPlayer(item.jumpContent);
//                            NavUtils.openBrowser(context, wp);
                            String checkInUrl = SPUtils.getCheckInUrl(context);
                            if (!StringUtil.isEmpty(checkInUrl)) {
                                WebParams wp = new WebParams();
                                wp.setIsSetTitle(false);
                                wp.setUrl(checkInUrl);
                                NavUtils.openBrowser(context, wp);
                            }
                        } else if (TaskStatus.PEDOMETER_HOME.equals(jumpType)) {
                            NavUtils.gotoPedometerActivity(context);
                        } else if (TaskStatus.PERSONAL_INFO.equals(jumpType)) {
                            NavUtils.gotoUserInfoEditActivity(context);
                        } else if (TaskStatus.ORDER_LIST.equals(jumpType)) {
                            NavUtils.gotoMyOrderAllListActivity(context);
                        } else{
                            ToastUtil.showToast(context,context.getString(R.string.label_toast_version_low));
                        }
                    }
                });
            }
        };
        return adapter;
    }

    private static void tcEvent(Context context, Task item) {
        Map<String, String> map = new HashMap();
        map.put(AnalyDataValue.KEY_ID, item.code);
        map.put(AnalyDataValue.KEY_TITLE, item.name);
        TCEventHelper.onEvent(context, AnalyDataValue.INTEGRAL_TASK_GO_FINISH, map);
    }


    /***
     * 兑奖记录
     *
     * @param context
     * @return
     */
    public static QuickAdapter<PointsDetail> setSignedDetailsAdapter(Context context) {
        long curTime = System.currentTimeMillis();
        final Date date = new Date(curTime);
        QuickAdapter<PointsDetail> adapter = new QuickAdapter<PointsDetail>(context, R.layout.item_sigend_details_list, new ArrayList<PointsDetail>()) {
            @Override
            protected void convert(BaseAdapterHelper helper, PointsDetail item) {
                helper.setText(R.id.tv_eat_product_name, item.title);
                helper.setText(R.id.tv_eat_product_address, getDateTimeToString(item.created));
                helper.setText(R.id.tv_eat_product_time, getDateoString(item.created));
                if (item.amount > 0) {
                    helper.setTextColor(R.id.tv_eat_product_num, context.getResources().getColor(R.color.ac_title_bg_color));
                    helper.setText(R.id.tv_eat_product_num, "+" + item.amount);
                } else if (item.amount < 0) {
                    helper.setTextColor(R.id.tv_eat_product_num, context.getResources().getColor(R.color.black));
                    helper.setText(R.id.tv_eat_product_num, "" + item.amount);
                } else {
                    helper.setText(R.id.tv_eat_product_num, "0");
                }
                helper.setText(R.id.tv_eat_product_name11, daysBetween(new Date(item.created), date));

            }
        };
        return adapter;
    }

    /***
     * 以下函数测试没问题后放到公共类中去
     *
     * @param time
     * @return
     */
    public static String getDateTimeToString(long time) {
        Date d = new Date(time);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy.MM.dd");
        return sf.format(d);
    }

    public static String getDateoString(long time) {
        Date d = new Date(time);
        SimpleDateFormat sf = new SimpleDateFormat("HH:mm");
        return sf.format(d);
    }


    /**
     * 计算两个日期之间相差的天数
     *
     * @param smdate
     * @param bdate  当前时间
     * @return 相差天数
     */
    public static String daysBetween(Date smdate, Date bdate) {
        String days = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            smdate = sdf.parse(sdf.format(smdate));
            bdate = sdf.parse(sdf.format(bdate));
            Calendar cal = Calendar.getInstance();
            cal.setTime(smdate);
            long time1 = cal.getTimeInMillis();
            cal.setTime(bdate);
            long time2 = cal.getTimeInMillis();
            long between_days = (time2 - time1) / (1000 * 3600 * 24);
            int dayDispatch = Integer.parseInt(String.valueOf(between_days));
            if (dayDispatch == 0) {
                days = "今天";
            } else if (dayDispatch == 1) {
                days = "昨天";
            } else if (dayDispatch == 2) {
                days = "前天";
            } else if (dayDispatch >= 3) {
                days = getWeek(smdate);
            }
            return days;
        } catch (Exception e) {
            return "";
        }

    }

    // 根据日期取得星期几
    public static String getWeek(Date date) {
        String[] weeks = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (week_index < 0) {
            week_index = 0;
        }
        return weeks[week_index];
    }

}
