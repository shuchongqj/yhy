package com.quanyan.yhy.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quanyan.yhy.R;
import com.yhy.common.beans.hotel.CircleInfo;
import com.yhy.common.beans.hotel.CutInfo;

import java.util.ArrayList;
import java.util.List;


public class HotelStarPopWin extends PopupWindow implements CompoundButton.OnCheckedChangeListener {


    public View view;
    public CheckBox mUnlimited;
    public CheckBox star5;
    public CheckBox star4;
    public CheckBox star3;
    public TextView mCancel, mDetermine;
    public CutSelectView mCutSelectView;
    public Context mContext;
    TextView mPicSmall;
    TextView mPicBig;
    LinearLayout l;
    RelativeLayout pop_layoutl;
    public HotelStarPopWin(final Context mContext, View.OnClickListener itemsOnClick) {
        this.mContext = mContext;
        this.view = LayoutInflater.from(mContext).inflate(R.layout.pop_hotel_stra, null);
        initData();
        mUnlimited = (CheckBox) view.findViewById(R.id.pop_cb_unlimited);
        star5 = (CheckBox) view.findViewById(R.id.pop_cb_star5);
        star4 = (CheckBox) view.findViewById(R.id.pop_cb_star4);
        star3 = (CheckBox) view.findViewById(R.id.pop_cb_star3);
        mCancel = (TextView) view.findViewById(R.id.pop_hotel_tv_cancel);
        mDetermine = (TextView) view.findViewById(R.id.pop_hotel_tv_determine);
        mCutSelectView = (CutSelectView) view.findViewById(R.id.pop_hotel_customView);
        mPicSmall = (TextView) view.findViewById(R.id.pop_hotel_tv_pic_small);
        mPicBig = (TextView) view.findViewById(R.id.pop_hotel_tv_pic_big);
        l=(LinearLayout) view.findViewById(R.id.pop_layout);
        pop_layoutl=(RelativeLayout) view.findViewById(R.id.pop_layoutl);
        mCutSelectView.setCircleInfo(mCircleInfo);



        mCutSelectView.setCutInfo(mCutInfos);
        mUnlimited.setOnCheckedChangeListener(this);
        star5.setOnCheckedChangeListener(this);
        star4.setOnCheckedChangeListener(this);
        star3.setOnCheckedChangeListener(this);
        mCancel.setOnClickListener(itemsOnClick);
        mDetermine.setOnClickListener(itemsOnClick);
//        layout_alignParentBottom

        mCutSelectView.setOnSelectChangeLinener(new CutSelectView.OnSelectChangeLinener() {

            @Override
            public void OnSelectChange(int selectL, int selectR) {
                mPicSmall.setText(mContext.getString(R.string.money_symbol) + selectL * 50);
                if (selectR == 20) {
                    mPicBig.setText("1000+");
                } else {
                    mPicBig.setText(mContext.getString(R.string.money_symbol) + selectR * 50);
                }
            }
        });


        // 设置外部可点击
        this.setOutsideTouchable(true);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        this.view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int height = view.findViewById(R.id.pop_layout).getTop();

                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });


        /* 设置弹出窗口特征 */
        // 设置视图
        this.setContentView(this.view);
        // 设置弹出窗体的宽和高
        this.setHeight(RelativeLayout.LayoutParams.MATCH_PARENT);
        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
        // 设置弹出窗体可点击
        this.setFocusable(true);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置弹出窗体的背景
        this.setBackgroundDrawable(dw);
        // 设置弹出窗体显示时的动画，从底部向上弹出
        this.setAnimationStyle(R.style.take_photo_anim);
    }

    private List<CutInfo> mCutInfos;
    private CircleInfo mCircleInfo;

    private void initData() {
        mCutInfos = new ArrayList<CutInfo>();
        mCutInfos.add(new CutInfo("￥0", mContext.getResources().getColor(R.color.dodgerblue), dp2px(mContext, 14)));
        mCutInfos.add(new CutInfo("", 0, 0));//50
        mCutInfos.add(new CutInfo("", 0, 0));//100
        mCutInfos.add(new CutInfo("", 0, 0));//150
        mCutInfos.add(new CutInfo("", 0, 0));//200
        mCutInfos.add(new CutInfo("", 0, 0));//250
        mCutInfos.add(new CutInfo("", 0, 0));//300
        mCutInfos.add(new CutInfo("", 0, 0));//350
        mCutInfos.add(new CutInfo("", 0, 0));//400
        mCutInfos.add(new CutInfo("", 0, 0));//450
        mCutInfos.add(new CutInfo("", 0, 0));//500
        mCutInfos.add(new CutInfo("", 0, 0));//550
        mCutInfos.add(new CutInfo("", 0, 0));//600
        mCutInfos.add(new CutInfo("", 0, 0));//650
        mCutInfos.add(new CutInfo("", 0, 0));//700
        mCutInfos.add(new CutInfo("", 0, 0));//750
        mCutInfos.add(new CutInfo("", 0, 0));//800
        mCutInfos.add(new CutInfo("", 0, 0));//850
        mCutInfos.add(new CutInfo("", 0, 0));//900
        mCutInfos.add(new CutInfo("", 0, 0));//950
        mCutInfos.add(new CutInfo("1000+", mContext.getResources().getColor(R.color.dodgerblue), dp2px(mContext, 14)));
        mCircleInfo = new CircleInfo(dp2px(mContext, 15), mContext.getResources().getColor(R.color.ac_title_bg_color), mContext.getResources().getColor(R.color.ac_title_bg_color));
    }

    public static int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


        int id = buttonView.getId();
        int red=mContext.getResources().getColor(R.color.tc_fa4619);
        int gray=mContext.getResources().getColor(R.color.color_norm_636363);
        if (id == R.id.pop_cb_unlimited) {

            mUnlimited.setTextColor(mUnlimited.isChecked() ? red:gray);
            star5.setChecked(false);
            star4.setChecked(false);
            star3.setChecked(false);
            star3.setTextColor(star3.isChecked() ? red:gray);
            star4.setTextColor(star4.isChecked() ? red:gray);
            star5.setTextColor(star5.isChecked() ? red:gray);
        } else if (id == R.id.pop_cb_star5) {
            if (isChecked) {
                mUnlimited.setChecked(false);
                mUnlimited.setTextColor(mContext.getResources().getColor(R.color.color_norm_636363));
                star5.setChecked(true);
            }else{
                star5.setChecked(false);
            }
            star3.setTextColor(star3.isChecked() ? red:gray);
            star4.setTextColor(star4.isChecked() ? red:gray);
            star5.setTextColor(star5.isChecked() ? red:gray);

        } else if (id == R.id.pop_cb_star4) {
            if (isChecked) {
                mUnlimited.setChecked(false);
                mUnlimited.setTextColor(mContext.getResources().getColor(R.color.color_norm_636363));
                star4.setChecked(true);
            }else{
                star4.setChecked(false);
            }
            star3.setTextColor(star3.isChecked() ? red:gray);
            star4.setTextColor(star4.isChecked() ? red:gray);
            star5.setTextColor(star5.isChecked() ? red:gray);
        } else if (id == R.id.pop_cb_star3) {
            if (isChecked) {
                mUnlimited.setChecked(false);
                mUnlimited.setTextColor(mContext.getResources().getColor(R.color.color_norm_636363));
                star3.setChecked(true);
            }else{
                star3.setChecked(false);
            }
            star3.setTextColor(star3.isChecked() ? red:gray);
            star4.setTextColor(star4.isChecked() ? red:gray);
            star5.setTextColor(star5.isChecked() ? red:gray);
        }
    }
}
