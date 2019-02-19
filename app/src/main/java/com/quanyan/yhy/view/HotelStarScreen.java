package com.quanyan.yhy.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quanyan.base.util.HarwkinLogUtil;
import com.quanyan.yhy.R;
import com.yhy.common.beans.hotel.CutInfo;

import java.util.ArrayList;
import java.util.List;


public class HotelStarScreen extends LinearLayout {

    public View view;
    public List<CutInfo> mCutInfos;
    public TextView mCancel, mDetermine, mDistanceTitle;
    public CutProgressBar mCutSelectView;
    public Context mContext;
    public TextView mPicSmall;
    public TextView mPicBig;
    public int distancePosition = 4;
    public LinearLayout mllHotelStraView;

    public HotelStarScreen(Context context) {
        super(context);

        init(context);
    }

    public HotelStarScreen(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HotelStarScreen(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public HotelStarScreen(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }


    public void init(Context mContext) {
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        this.mContext = getContext();

        this.view = View.inflate(mContext, R.layout.pop_hotel_stra_distance, this);

        initData();
        mCancel = (TextView) view.findViewById(R.id.pop_hotel_tv_distance_cancel);
        mDetermine = (TextView) view.findViewById(R.id.pop_hotel_tv_distance_determine);
        mCutSelectView = (CutProgressBar) view.findViewById(R.id.pop_hotel_customView);
        mPicSmall = (TextView) view.findViewById(R.id.pop_hotel_tv_pic_small);
        mPicBig = (TextView) view.findViewById(R.id.pop_hotel_tv_pic_big);
        mDistanceTitle = (TextView) view.findViewById(R.id.tv_distance_title);
        mllHotelStraView = (LinearLayout) view.findViewById(R.id.ll_hotel_stra_view);
        mCutSelectView.setCutInfo(mCutInfos);
        mCutSelectView.setCircleSize(dp2px(mContext, 15));
        mCutSelectView.setClickPosition(4);
        mCutSelectView.setOnSelectChangeLinener(new CutProgressBar.OnSelectChangeLinener() {

            @Override
            public void OnSelectChange(int position) {
                HarwkinLogUtil.info("position" + position);
                distancePosition = position;
            }
        });
    }

    public void changeCutSelectViewLineColor(String mTitle) {
        mDistanceTitle.setText(mTitle);
        mCutSelectView.LineColor = getResources().getColor(R.color.calendar_color_gray);
        mCutSelectView. LineColorGray = getResources().getColor(R.color.calendar_color_gray);
        mCutSelectView.TextColor = getResources().getColor(R.color.calendar_color_gray);
        mCutSelectView.isProhibitVew = false;
        mCutSelectView.invalidate();
    }

    private void initData() {
        mCutInfos = new ArrayList<CutInfo>();
        mCutInfos.add(new CutInfo("1km", mContext.getResources().getColor(R.color.Black), dp2px(mContext, 15)));
        mCutInfos.add(new CutInfo("3km", mContext.getResources().getColor(R.color.Black), dp2px(mContext, 15)));
        mCutInfos.add(new CutInfo("5km", mContext.getResources().getColor(R.color.Black), dp2px(mContext, 15)));
        mCutInfos.add(new CutInfo("10km", mContext.getResources().getColor(R.color.Black), dp2px(mContext, 15)));
        mCutInfos.add(new CutInfo("不限", mContext.getResources().getColor(R.color.Black), dp2px(mContext, 15)));

    }

    public static int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
