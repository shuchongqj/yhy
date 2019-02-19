package com.newyhy.views;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.androidkun.xtablayout.XTabLayout;
import com.quanyan.yhy.R;

public class CircleXTabLayout extends XTabLayout {
    public CircleXTabLayout(Context context) {
        super(context);
        init();
    }

    public CircleXTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleXTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
//    app:xTabMode="scrollable"
//    app:xTabTextSelectedBold="true"
//    app:xTabIndicatorColor="@color/red_win"
//    app:xTabIndicatorHeight="@dimen/yhy_size_3px"
//    app:xTabIndicatorWidth="25dp"
//    app:xTabSelectedTextColor="@color/tab_selected"
//    app:xTabTextColor="@color/tab_normal"
//    app:xTabTextSize="@dimen/yhy_size_16px"
//    app:xTabSelectedTextSize="@dimen/yhy_size_16px"
//    app:xTabDisplayNum="7"/>
    private void init(){
        addOnTabSelectedListener(new OnTabSelectedListener() {
            @Override
            public void onTabSelected(Tab tab) {
                if (tab == null){
                    return;
                }
                View view = tab.getCustomView();
                if (view == null){
                    return;
                }
                TextView textView = view.findViewById(android.R.id.text1);
                textView.setTypeface(Typeface.DEFAULT_BOLD);
                textView.setTextColor(ContextCompat.getColor(getContext(), R.color.tab_selected));
            }

            @Override
            public void onTabUnselected(Tab tab) {
                if (tab == null){
                    return;
                }
                View view = tab.getCustomView();
                if (view == null){
                    return;
                }
                TextView textView = view.findViewById(android.R.id.text1);
                textView.setTypeface(Typeface.DEFAULT);
                textView.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
            }

            @Override
            public void onTabReselected(Tab tab) {

            }
        });
    }

    @NonNull
    @Override
    public Tab newTab() {
        Tab tab = super.newTab();
        tab.setCustomView(R.layout.circle_tab_item);
        TextView textView = tab.getCustomView().findViewById(android.R.id.text1);
        textView.setTypeface(Typeface.DEFAULT);
        textView.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
        return tab;
    }
}
