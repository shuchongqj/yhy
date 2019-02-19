package com.quanyan.yhy.ui.signed.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.R;
import com.yhy.common.beans.net.model.CreditNotification;
import com.yhy.common.utils.SPUtils;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Created with Android Studio.
 * Title:EatGreatActivity
 * Description:积分
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:wjm
 * Date:2016-6-24
 * Time:12:05
 * Version 1.3.0
 */
public class SigendDialogActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_job_finished_dlg);
        CreditNotification c= (CreditNotification)getIntent().getExtras().getSerializable(SPUtils.EXTRA_DATA);
        TextView mTitle;
        TextView mContent;
        mTitle = (TextView) findViewById(R.id.msg_dlg_title);
        mContent = (TextView) findViewById(R.id.msg_dlg_content);

       if( c!=null){
           mTitle.setText("+" + c.credit);
           if(!StringUtil.isEmpty(c.description)){
               mContent.setVisibility(View.VISIBLE);
               mContent.setText( String.format(getString(R.string.label_integralmall_reward), c.description));
           }else {
               mContent.setVisibility(View.INVISIBLE);
           }
       }else{
           mTitle.setVisibility(View.INVISIBLE);
           mContent.setVisibility(View.INVISIBLE);
       }



        final Timer t = new Timer();
        t.schedule(new TimerTask() {
            public void run() {
                finish();
                t.cancel();

            }
        }, 3000);


    }

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                finish();
                break;
        }
        return super.onTouchEvent(event);
    }
}
