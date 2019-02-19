package com.quanyan.yhy.ui.spcart.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.quanyan.yhy.R;

/**
 * Created with Android Studio.
 * Title:SpCartEditBottomView
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-10-21
 * Time:11:45
 * Version 1.0
 * Description:
 */
public class SpCartEditBottomView extends LinearLayout {

    private ImageView mAllSelectImage;
    private Button mAllDeleteBtn;

    private SpCartEditBottomViewClick mSpCartEditBottomViewClick;

    public SpCartEditBottomView(Context context) {
        super(context);
        initView(context);
    }

    public SpCartEditBottomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public SpCartEditBottomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SpCartEditBottomView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }


    private void initView(Context context) {
        View.inflate(context, R.layout.spcart_edit_bottom_view, this);
        mAllSelectImage = (ImageView) this.findViewById(R.id.iv_all_checked);
        mAllDeleteBtn = (Button) this.findViewById(R.id.btn_delete_all);

        initClick();
    }

    private void initClick() {
        mAllSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSpCartEditBottomViewClick != null) {
                    mSpCartEditBottomViewClick.selectAll();
                }
            }
        });

        mAllDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSpCartEditBottomViewClick != null) {
                    mSpCartEditBottomViewClick.deleteAll();
                }
            }
        });
    }

    public void setAllSelectImage(boolean isSelect) {
        if (isSelect) {
            mAllSelectImage.setImageResource(R.mipmap.spcart_checked);
        } else {
            mAllSelectImage.setImageResource(R.mipmap.spcart_unchecked);
        }
    }

    public void setSpCartEditBottomViewClickListener(SpCartEditBottomViewClick mSpCartEditBottomViewClick) {
        this.mSpCartEditBottomViewClick = mSpCartEditBottomViewClick;
    }

    public interface SpCartEditBottomViewClick {
        void selectAll();

        void deleteAll();
    }
}
