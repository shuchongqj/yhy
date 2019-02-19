package com.quanyan.yhy.ui.base.utils;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.quanyan.yhy.R;
import com.quanyan.yhy.ui.adapter.base.GridViewFilterAdapter;
import com.quanyan.yhy.ui.integralmall.integralmallinterface.OrderFilterInterface;
import com.quanyan.yhy.ui.order.entity.FilterCondition;

import java.util.List;
import java.util.Map;

/**
 * Created with Android Studio.
 * Title:***Acitivity
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:田海波
 * Date: 2016/9/23
 * Time: 14:10
 * Version 2.0
 */
public class PopupUtil {
    static PopupWindow mPopupWindow = null;

    public static void showOrderFilterPopup(final Context context, final List<FilterCondition> dataList, final OrderFilterInterface mOnOrderFilterListener, View view) {

        int mScreenWidth;
        int mScreenHeight;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mScreenHeight = wm.getDefaultDisplay().getHeight();
        mScreenWidth = wm.getDefaultDisplay().getWidth();
        if (mPopupWindow == null) {
            LinearLayout layout = new LinearLayout(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    (int) (mScreenHeight * 0.3));

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            RelativeLayout childView = (RelativeLayout) inflater.inflate(
                    R.layout.dialog_my_order_filter
                    , null);
            childView.setLayoutParams(params);
            layout.addView(childView);
            layout.setBackgroundColor(Color.argb(60, 0, 0, 0));
            mPopupWindow = new PopupWindow(layout, mScreenWidth, mScreenHeight);
            mPopupWindow.setFocusable(false);
            mPopupWindow.setOutsideTouchable(false);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismissOrderFilterPopup();
                    mOnOrderFilterListener.OnPopupDismiss();
                }
            });
            GridView gridView = (GridView) childView.findViewById(R.id.gridview);

            final GridViewFilterAdapter adapter = new GridViewFilterAdapter(context, dataList);
            gridView.setAdapter(adapter);

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                    Map<Integer, Boolean> isSelectedMap = adapter.isSelected;

                    for (Integer p : isSelectedMap.keySet()) {

                        if (position == p) {
                            isSelectedMap.put(p, true);
                        } else {
                            isSelectedMap.put(p, false);
                        }
                    }

                    adapter.notifyDataSetChanged();
                    mOnOrderFilterListener.OnFilterClick(position, dataList.get(position).getConId(), dataList.get(position).getConName());

                    dismissOrderFilterPopup();
                    mOnOrderFilterListener.OnPopupDismiss();
                }
            });
        }

        // mPopupWindow.setAnimationStyle(R.style.TranslationAnimation);
        mPopupWindow.showAsDropDown(view, 0, 0);


    }

    public static void destoryOrderFilterPopup() {
        if (mPopupWindow != null) {
            mPopupWindow = null;
        }
    }

    public static void dismissOrderFilterPopup() {
        if (mPopupWindow != null) {
            mPopupWindow.dismiss();
        }
    }
}
