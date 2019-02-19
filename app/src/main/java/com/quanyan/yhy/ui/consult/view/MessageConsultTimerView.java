package com.quanyan.yhy.ui.consult.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.quanyan.yhy.R;
import com.yhy.common.beans.net.model.tm.ConsultInfo;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created with Android Studio.
 * Title:MessageConsultTimerView
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:邓明佳
 * Date:2016/7/7
 * Time:14:30
 * Version 1.0
 */
public class MessageConsultTimerView extends LinearLayout {
    private Context context;
    private TextView mTime1, mTime2, mTime3, mTime4;
    private ProgressBar mProgressBar;

    public MessageConsultTimerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.view_message_consult_timer, this);
        mTime1 = (TextView) findViewById(R.id.tv_time_1);
        mTime2 = (TextView) findViewById(R.id.tv_time_2);
        mTime3 = (TextView) findViewById(R.id.tv_time_3);
        mTime4 = (TextView) findViewById(R.id.tv_time_4);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
    }

    public MessageConsultTimerView(Context context) {
        super(context);
        init(context);
    }

    Timer timer;

    public synchronized void setData(ConsultInfo data) {
        if (timer != null) {
            timer.cancel();
            timer = null;

        }
        mProgressBar.setMax((int) data.serviceTotalTime);
        mProgressBar.setProgress((int) data.serviceCurrentTime);

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                int max = mProgressBar.getMax();
                int progress = mProgressBar.getProgress();
                if (max == progress) {
                if (listner != null) {
                    listner.onFinish();
                }
                    timer.cancel();
                    return;
                }
                mProgressBar.setProgress(progress + 1);
            }
        }, 1000, 1000);

        long timeAll = Math.round(div(data.serviceTotalTime, 60));
        long time13 = Math.round(div(data.serviceTotalTime, 60 * 3));
        long time23 = Math.round(mul(div(data.serviceTotalTime, 60 * 3), 2));
        mTime1.setText("0");
        mTime4.setText(timeAll + "分钟");
        mTime2.setText(time13 + "");
        mTime3.setText(time23 + "");
    }

    public void setFinishTime(long totalTime) {
        mProgressBar.setMax((int) totalTime);
        mProgressBar.setProgress((int) totalTime);
        long timeAll = Math.round(div(totalTime, 60));
        long time13 = Math.round(div(totalTime, 60 * 3));
        long time23 = Math.round(mul(div(totalTime, 60 * 3), 2));
        mTime1.setText("0");
        mTime4.setText(timeAll + "分钟");
        mTime2.setText(time13 + "");
        mTime3.setText(time23 + "");
    }

    public interface OnTimeOverListner {
        void onFinish();
    }

    private OnTimeOverListner listner;

    public void setOnTimeOverListner(OnTimeOverListner listner) {
        this.listner = listner;
    }

    private double mul(double a, double b) {
        return a * b;
    }

    private double div(double a, double b) {
        if (b == 0) {
            return 0;
        }
        return a / b;
    }
}
