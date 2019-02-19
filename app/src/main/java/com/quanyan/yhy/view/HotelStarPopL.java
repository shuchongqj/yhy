package com.quanyan.yhy.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quanyan.yhy.R;
import com.yhy.common.beans.hotel.AddHotelSearchBean;
import com.yhy.common.beans.hotel.CircleInfo;
import com.yhy.common.beans.hotel.CutInfo;

import java.util.ArrayList;
import java.util.List;


public class HotelStarPopL extends LinearLayout implements CompoundButton.OnCheckedChangeListener {

    public View view;
    public CheckBox mUnlimited;
    public CheckBox star5;
    public CheckBox star4;
    public CheckBox star3;
    public CheckBox star2;

    public TextView mCancel, mDetermine;
    public CutSelectView mCutSelectView;
    public Context mContext;
    public TextView mPicSmall;
    public TextView mPicBig;
    int position;

    public HotelStarPopL(Context context, int position) {
        super(context);
        this.position = position;
        init(context);
    }

    public HotelStarPopL(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HotelStarPopL(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public HotelStarPopL(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    TextView mPicBigPlus;
    public int selectLs = 0;
    public int selectRs = 20;
    public LinearLayout mLayoutBelow;

    public void init(Context mContext) {
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        this.mContext = getContext();
        if (position == 0) {
            this.view = View.inflate(mContext, R.layout.pop_hotel_stra_below, this);
        } else {
            this.view = View.inflate(mContext, R.layout.pop_hotel_stra, this);
        }

        initData();
        mUnlimited = (CheckBox) view.findViewById(R.id.pop_cb_unlimited);
        star5 = (CheckBox) view.findViewById(R.id.pop_cb_star5);
        star4 = (CheckBox) view.findViewById(R.id.pop_cb_star4);
        star3 = (CheckBox) view.findViewById(R.id.pop_cb_star3);
        star2 = (CheckBox) view.findViewById(R.id.pop_cb_star2);
        mLayoutBelow = (LinearLayout) view.findViewById(R.id.layout_below);
        mCancel = (TextView) view.findViewById(R.id.pop_hotel_tv_cancel);
        mDetermine = (TextView) view.findViewById(R.id.pop_hotel_tv_determine);
        mCutSelectView = (CutSelectView) view.findViewById(R.id.pop_hotel_customView);
        mPicSmall = (TextView) view.findViewById(R.id.pop_hotel_tv_pic_small);
        mPicBig = (TextView) view.findViewById(R.id.pop_hotel_tv_pic_big);
        mPicBigPlus = (TextView) view.findViewById(R.id.pop_hotel_tv_pic_big_plus);

        mCutSelectView.setCircleInfo(mCircleInfo);
        mCutSelectView.setCutInfo(mCutInfos);
        mUnlimited.setOnCheckedChangeListener(this);
        star5.setOnCheckedChangeListener(this);
        star4.setOnCheckedChangeListener(this);
        star3.setOnCheckedChangeListener(this);
        star2.setOnCheckedChangeListener(this);
        mCutSelectView.setOnSelectChangeLinener(new CutSelectView.OnSelectChangeLinener() {

            @Override
            public void OnSelectChange(int selectL, int selectR) {
                selectLs = selectL;
                selectRs = selectR;
                mPicSmall.setText(selectL * 50 + "");
                if (selectR == 20) {
                    mPicBig.setText("1000");
                    mPicBigPlus.setVisibility(VISIBLE);
                } else {
                    mPicBig.setText(selectR * 50 + "");
                    mPicBigPlus.setVisibility(GONE);
                }
            }
        });
        mUnlimited.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mUnlimited.isChecked()) {
                    mUnlimited.setChecked(true);
                }
            }
        });

        // 设置外部可点击
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
//        this.view.setOnTouchListener(new View.OnTouchListener() {
//            public boolean onTouch(View v, MotionEvent event) {
//                int height = view.findViewById(R.id.pop_layout).getTop();
//
//                int y = (int) event.getY();
//                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    if (y < height) {
////                        dismiss();
//                    }
//                }
//                return true;
//            }
//        });

//
//        /* 设置弹出窗口特征 */
//        // 设置视图
//        this.setContentView(this.view);
//        // 设置弹出窗体的宽和高
//        this.setHeight(RelativeLayout.LayoutParams.MATCH_PARENT);
//        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
//        // 设置弹出窗体可点击
//        this.setFocusable(true);
//        // 实例化一个ColorDrawable颜色为半透明
//        ColorDrawable dw = new ColorDrawable(0xb0000000);
//        // 设置弹出窗体的背景
//        this.setBackgroundDrawable(dw);
//        // 设置弹出窗体显示时的动画，从底部向上弹出
//        this.setAnimationStyle(R.style.take_photo_anim);
    }

    /**
     * 还原view到最初
     */
    public void initializationHotelStarPopL(AddHotelSearchBean mAddHotelSearchBean) {
        mCutSelectView.initializationView();
        mUnlimited.setChecked(true);
        star5.setChecked(false);
        star4.setChecked(false);
        star3.setChecked(false);
        star2.setChecked(false);
        mPicSmall.setText("0");
        mPicBig.setText("1000");
        mPicBigPlus.setVisibility(VISIBLE);
        selectLs = 0;
        selectRs = 20;
        mAddHotelSearchBean.setmPriceOrStar("");
        mAddHotelSearchBean.setIsCbCheckedUnlimited(true);
        mAddHotelSearchBean.setIsCbChecked2(false);
        mAddHotelSearchBean.setIsCbChecked3(false);
        mAddHotelSearchBean.setIsCbChecked4(false);
        mAddHotelSearchBean.setIsCbChecked5(false);
        mAddHotelSearchBean.setmPriceSmall(0);
        mAddHotelSearchBean.setmPriceBig(20);


    }

    /**
     * 赋值
     */
    public void assignmentHotelStarPopL(AddHotelSearchBean mAddHotelSearchBean, boolean isFirstDraw) {
        mCutSelectView.isFirstDraw = isFirstDraw;
        if (mAddHotelSearchBean != null) {
            star2.setChecked(mAddHotelSearchBean.isCbChecked2());
            star3.setChecked(mAddHotelSearchBean.isCbChecked3());
            star4.setChecked(mAddHotelSearchBean.isCbChecked4());
            star5.setChecked(mAddHotelSearchBean.isCbChecked5());
            mUnlimited.setChecked(mAddHotelSearchBean.isCbCheckedUnlimited());
            selectLs = mAddHotelSearchBean.getmPriceSmall();
            selectRs = mAddHotelSearchBean.getmPriceBig();
            mPicSmall.setText(selectLs * 50 + "");
            if (selectRs == 20) {
                mPicBig.setText("1000");
                mPicBigPlus.setVisibility(VISIBLE);
            } else {
                mPicBig.setText(selectRs * 50 + "");
                mPicBigPlus.setVisibility(GONE);
            }
            mCutSelectView.setClickPosition(selectLs, selectRs);
        }
    }

    public List<CutInfo> mCutInfos;
    private CircleInfo mCircleInfo;

    private void initData() {
        mCutInfos = new ArrayList<CutInfo>();
        mCutInfos.add(new CutInfo("", mContext.getResources().getColor(R.color.dodgerblue), dp2px(mContext, 14)));
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
        mCutInfos.add(new CutInfo("", mContext.getResources().getColor(R.color.dodgerblue), dp2px(mContext, 14)));
        mCircleInfo = new CircleInfo(dp2px(mContext, 15), mContext.getResources().getColor(R.color.order_666666), mContext.getResources().getColor(R.color.white));
    }

    public static int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int id = buttonView.getId();
        int red = mContext.getResources().getColor(R.color.tc_fa4619);
        int gray = mContext.getResources().getColor(R.color.color_norm_636363);
        if (id == R.id.pop_cb_unlimited) {
            mUnlimited.setTextColor(mUnlimited.isChecked() ? red : gray);
            star5.setChecked(false);
            star4.setChecked(false);
            star3.setChecked(false);
            star2.setChecked(false);
            star2.setTextColor(star2.isChecked() ? red : gray);
            star3.setTextColor(star3.isChecked() ? red : gray);
            star4.setTextColor(star4.isChecked() ? red : gray);
            star5.setTextColor(star5.isChecked() ? red : gray);
        } else if (id == R.id.pop_cb_star5) {
            if (isChecked) {
                mUnlimited.setChecked(false);
                mUnlimited.setTextColor(mContext.getResources().getColor(R.color.color_norm_636363));
                star5.setChecked(true);
            } else {
                star5.setChecked(false);
            }
            star2.setTextColor(star2.isChecked() ? red : gray);
            star3.setTextColor(star3.isChecked() ? red : gray);
            star4.setTextColor(star4.isChecked() ? red : gray);
            star5.setTextColor(star5.isChecked() ? red : gray);
            mUnlimited.setChecked(CbIsAllChecked());

        } else if (id == R.id.pop_cb_star4) {
            if (isChecked) {
                mUnlimited.setChecked(false);
                mUnlimited.setTextColor(mContext.getResources().getColor(R.color.color_norm_636363));
                star4.setChecked(true);
            } else {
                star4.setChecked(false);
            }
            star2.setTextColor(star2.isChecked() ? red : gray);
            star3.setTextColor(star3.isChecked() ? red : gray);
            star4.setTextColor(star4.isChecked() ? red : gray);
            star5.setTextColor(star5.isChecked() ? red : gray);
            mUnlimited.setChecked(CbIsAllChecked());
        } else if (id == R.id.pop_cb_star3) {
            if (isChecked) {
                mUnlimited.setChecked(false);
                mUnlimited.setTextColor(mContext.getResources().getColor(R.color.color_norm_636363));
                star3.setChecked(true);
            } else {
                star3.setChecked(false);
            }
            star2.setTextColor(star2.isChecked() ? red : gray);
            star3.setTextColor(star3.isChecked() ? red : gray);
            star4.setTextColor(star4.isChecked() ? red : gray);
            star5.setTextColor(star5.isChecked() ? red : gray);
            mUnlimited.setChecked(CbIsAllChecked());
        } else if (id == R.id.pop_cb_star3) {
            if (isChecked) {
                mUnlimited.setChecked(false);
                mUnlimited.setTextColor(mContext.getResources().getColor(R.color.color_norm_636363));
                star3.setChecked(true);
            } else {
                star3.setChecked(false);
            }
            star2.setTextColor(star2.isChecked() ? red : gray);
            star3.setTextColor(star3.isChecked() ? red : gray);
            star4.setTextColor(star4.isChecked() ? red : gray);
            star5.setTextColor(star5.isChecked() ? red : gray);
            mUnlimited.setChecked(CbIsAllChecked());
        } else if (id == R.id.pop_cb_star2) {
            if (isChecked) {
                mUnlimited.setChecked(false);
                mUnlimited.setTextColor(mContext.getResources().getColor(R.color.color_norm_636363));
                star2.setChecked(true);
            } else {
                star2.setChecked(false);
            }
            star2.setTextColor(star2.isChecked() ? red : gray);
            star3.setTextColor(star3.isChecked() ? red : gray);
            star4.setTextColor(star4.isChecked() ? red : gray);
            star5.setTextColor(star5.isChecked() ? red : gray);
            mUnlimited.setChecked(CbIsAllChecked());
        }
    }

    public boolean CbIsAllChecked() {
        if (star2.isChecked() && star3.isChecked() && star4.isChecked() && star5.isChecked()) {
            return true;
        }
        if (!star2.isChecked() && !star3.isChecked() && !star4.isChecked() && !star5.isChecked()) {
            return true;
        }
        return false;

    }


}
