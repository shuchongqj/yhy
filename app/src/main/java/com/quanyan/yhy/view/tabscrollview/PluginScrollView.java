package com.quanyan.yhy.view.tabscrollview;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.quanyan.yhy.R;

import java.util.List;


public class PluginScrollView extends RelativeLayout {

    private MyHorizontalScrollView scrollview;
    private LinearLayout layout;
    private ViewPager viewpager;
    private int currentSelectedButton = 0;
    private int singleButtonWidth = 90;
    private Context context;
    private List<String> testList;

    public PluginScrollView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public PluginScrollView(Context context, ViewPager viewPager) {
        this(context, viewPager, null);
    }

    public PluginScrollView(Context context, ViewPager viewPager, List<String> testList) {
        super(context);
        this.context = context;
        this.viewpager = viewPager;
        this.testList = testList;
        init();
    }

    public PluginScrollView(Context context, AttributeSet attr) {
        super(context, attr);
        this.context = context;
        init();
    }

    private void init() {

        View view = View.inflate(context, R.layout.activity_tab_scrollview_plugin, this);
        scrollview = (MyHorizontalScrollView) view
                .findViewById(R.id.my_horizontal_scrollview);
        scrollview.setHorizontalScrollBarEnabled(false);

        layout = (LinearLayout) view.findViewById(R.id.layout);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // TODO Auto-generated method stub
        super.onLayout(changed, l, t, r, b);
        if (layout != null) {
            View button = layout.getChildAt(0);
            if (button != null) {
                singleButtonWidth = button.getWidth();
            }
        }
    }

    public void setViewPager(ViewPager viewPager) {
        this.viewpager = viewPager;
    }

    public void setTestList(List<String> testList) {
        this.testList = testList;
        initButtons();
    }

    public List<String> getTestList() {
        return this.testList;
    }

    private void initButtons() {
        if (testList == null) return;
        layout.removeAllViews();
        for (int i = 0; i < testList.size(); i++) {
            final int j = i;
            Button mbutton = new Button(context);
            mbutton.setText(testList.get(i));
            mbutton.setTextColor(14);
            mbutton.setSingleLine(true);
            if (i == 0) {
                mbutton.setBackgroundResource(R.mipmap.btn_scrollview_plugin_unselected);
                mbutton.setTextColor(context.getResources().getColor(R.color.neu_ff9900));
            } else {
                mbutton.setBackgroundColor(Color.parseColor("#00000000"));
                mbutton.setTextColor(context.getResources().getColor(R.color.tv_color_gray6));
            }
            LayoutParams params = new LayoutParams((int)context.getResources().getDimension(R.dimen.dd_dimen_160px), LayoutParams.WRAP_CONTENT);
            mbutton.setLayoutParams(params);
            mbutton.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    viewpager.setCurrentItem(j, true);
                }
            });
            layout.addView(mbutton,params);
        }

    }

    public int size() {
        if (testList == null) return 0;
        return testList.size();
    }

    public void buttonSelected(int position) {
        if (position == currentSelectedButton) {
            return;
        }

        getButtonInLayout(currentSelectedButton).setSelected(false);
        getButtonInLayout(currentSelectedButton).setBackgroundColor(Color.parseColor("#00000000"));
        getButtonInLayout(currentSelectedButton).setTextColor(context.getResources().getColor(R.color.tv_color_gray6));
        getButtonInLayout(position).setSelected(true);
        getButtonInLayout(position).setBackgroundResource(R.mipmap.btn_scrollview_plugin_unselected);
        getButtonInLayout(position).setTextColor(context.getResources().getColor(R.color.neu_ff9900));
        currentSelectedButton = position;
        autoScrollView(position);
    }

    private void autoScrollView(int location) {

        int displayX = scrollview.getScrollX();
        int displayMaxX = getWidth() + displayX;

        int icoScrollX = 0;
        for (int i = 0; i < location; i++) {
            icoScrollX += layout.getChildAt(i).getWidth();
        }
        int icoScrollMaxX = layout.getChildAt(location).getWidth() + icoScrollX;
        if (icoScrollX < displayX) {
            scrollview.smoothScrollBy(icoScrollX - displayX - 10, 0);
        } else if (icoScrollMaxX > displayMaxX) {
            scrollview.smoothScrollBy(icoScrollMaxX - displayMaxX, 0);
        }
    }

    private Button getButtonInLayout(int i) {
        View view = layout.getChildAt(i);
        if (view instanceof Button) {
            return (Button) view;
        }
        return null;
    }


}
