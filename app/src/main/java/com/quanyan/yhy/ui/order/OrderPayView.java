package com.quanyan.yhy.ui.order;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quanyan.yhy.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:OrderPayView
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-3-10
 * Time:10:12
 * Version 1.0
 * Description:
 */
public class OrderPayView extends LinearLayout {

    private LinearLayout mLl_paycontent;

    private LayoutInflater mLayoutInflater;

    private List<PayModle> payList;

    private Context mContext;

    private int clickPostion = 0;

    private ChoosePostion chooserPostionListener;

    public OrderPayView(Context context) {
        super(context);
        this.mContext = context;
        init(context);
    }

    public OrderPayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init(context);
    }

    public OrderPayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public OrderPayView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mContext = context;
        init(context);
    }

    private void init(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        View view = mLayoutInflater.inflate(R.layout.orderpayview, null);

        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        rlp.bottomMargin = 0;
        rlp.topMargin = 0;
        rlp.leftMargin = 0;
        rlp.rightMargin = 0;

        mLl_paycontent = (LinearLayout) view.findViewById(R.id.ll_paycontent);
        setList();
        setLayoutView();
        addView(view, rlp);
    }

    private void setLayoutView() {
        if (mLl_paycontent != null) {
            mLl_paycontent.removeAllViews();
        }
        for (int position = 0; position < payList.size(); position++) {
            View convertView = mLayoutInflater.inflate(R.layout.scenicorderconfig_paywaypop_item_layout, null);
            TextView payway_texttitle = (TextView) convertView.findViewById(R.id.payway_texttitle);
            ImageView payway_image = (ImageView) convertView.findViewById(R.id.payway_image);
            ImageView pay_select_im = (ImageView) convertView.findViewById(R.id.pay_select_im);
            final RelativeLayout pay_item_layout = (RelativeLayout) convertView.findViewById(R.id.pay_item_layout);
            payway_texttitle.setText(payList.get(position).getTitle().toString());
            if (payList.get(position).getTitle().toString().equals(getResources().getString(R.string.pay_byzfb))) {
                payway_image.setImageResource(R.mipmap.pay_zhifubao_image);
            } else if (payList.get(position).getTitle().toString().equals(getResources().getString(R.string.pay_quanyan))) {
                payway_image.setImageResource(R.mipmap.jiuxiu_pay);
            } else if (payList.get(position).getTitle().toString().equals(getResources().getString(R.string.pay_kuaijie))) {
                payway_image.setImageResource(R.mipmap.kuaijie_pay);
            }else if (payList.get(position).getTitle().toString().equals(getResources().getString(R.string.pay_weichat))) {
                payway_image.setImageResource(R.mipmap.pay_wexin_image);
            }

            if (payList.get(position).isSelect()) {
                pay_select_im.setImageResource(R.mipmap.ic_checked);
            } else {
                pay_select_im.setImageResource(R.mipmap.ic_uncheck);
            }

            pay_item_layout.setTag(position);

            pay_item_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resetPayList((Integer) pay_item_layout.getTag());
                    clickPostion = (Integer) pay_item_layout.getTag();
                    if (chooserPostionListener != null) {
                        chooserPostionListener.choosePostion(clickPostion);
                    }
                }
            });
            mLl_paycontent.addView(convertView);
        }
    }

    /**
     * 重置数据刷新界面
     *
     * @param postion
     */
    private void resetPayList(int postion) {
        for (int i = 0; i < payList.size(); i++) {
            if (postion == i) {
                payList.get(i).setIsSelect(true);
            } else {
                payList.get(i).setIsSelect(false);
            }
        }
        setLayoutView();
    }

    private void setList() {
        payList = new ArrayList<PayModle>();
        PayModle modle1 = new PayModle();
        modle1.setTitle(getResources().getString(R.string.pay_byzfb));
        modle1.setIsSelect(false);
        PayModle modle2 = new PayModle();
        modle2.setTitle(getResources().getString(R.string.pay_quanyan));
        modle2.setIsSelect(true);
        PayModle modle3 = new PayModle();
        modle3.setTitle(getResources().getString(R.string.pay_kuaijie));
        modle3.setIsSelect(false);

        PayModle modle4 = new PayModle();
        modle4.setTitle(getResources().getString(R.string.pay_weichat));
        modle4.setIsSelect(false);
        //payList.add(modle4);
        payList.add(modle2);
        payList.add(modle1);
        payList.add(modle3);
//        if (PayConfig.WEIPAYCONFIG) {
//            if (MobileUtil.isWeixinAvilible(mContext)) {
//                payList.add(modle2);
//            }
//        }
    }

    class PayModle {
        private String title;
        private boolean isSelect;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public boolean isSelect() {
            return isSelect;
        }

        public void setIsSelect(boolean isSelect) {
            this.isSelect = isSelect;
        }
    }

    public void setChooserPostionListener(ChoosePostion chooserPostionListener) {
        this.chooserPostionListener = chooserPostionListener;
    }

    public int getSelectedIndex() {
        return clickPostion;
    }

    public interface ChoosePostion {
        void choosePostion(int postion);
    }

}
