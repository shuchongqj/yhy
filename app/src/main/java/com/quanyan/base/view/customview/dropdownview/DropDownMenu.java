package com.quanyan.base.view.customview.dropdownview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quanyan.yhy.R;

import java.util.List;

/**
 * Created with Android Studio.
 * Title:AboutAndFeedController
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:zhaoxiaopo
 * Date:16/2/26
 * Time:10:52
 * Version 1.0
 */
public class DropDownMenu extends LinearLayout {

    //顶部菜单布局
    private LinearLayout tabMenuView;
    //底部容器，包含popupMenuViews，maskView
    private FrameLayout containerView;
    //弹出菜单父布局
    private FrameLayout popupMenuViews;
    //遮罩半透明View，点击可关闭DropDownMenu
    private View maskView;
    //tabMenuView里面选中的tab位置，-1表示未选中
    private int current_tab_position = -1;

    //分割线颜色
    private int dividerColor = 0xe1e1e1;
    //tab选中颜色
    private int textSelectedColor = 0xff890c85;
    //tab未选中颜色
    private int textUnselectedColor = 0xff111111;
    //遮罩颜色
    private int maskColor = 0x88888888;
    //tab字体大小
    private int menuTextSize = 14;

    //tab选中图标
    private int menuSelectedIcon;
    //tab未选中图标
    private int menuUnselectedIcon;
    private View underLine;

    public DropDownMenu(Context context) {
        super(context, null);
        initView(context, null);
    }

    public DropDownMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        initView(context, attrs);
    }

    public DropDownMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs){
        setOrientation(VERTICAL);

        //为DropDownMenu添加自定义属性
        int menuBackgroundColor = 0xffffffff;
        int underlineColor = 0xffcccccc;
        if(attrs != null){
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DropDownMenu);
            underlineColor = a.getColor(R.styleable.DropDownMenu_ddunderlineColor, underlineColor);
            dividerColor = a.getColor(R.styleable.DropDownMenu_dddividerColor, dividerColor);
            textSelectedColor = a.getColor(R.styleable.DropDownMenu_ddtextSelectedColor, textSelectedColor);
            textUnselectedColor = a.getColor(R.styleable.DropDownMenu_ddtextUnselectedColor, textUnselectedColor);
            menuBackgroundColor = a.getColor(R.styleable.DropDownMenu_ddmenuBackgroundColor, menuBackgroundColor);
            maskColor = a.getColor(R.styleable.DropDownMenu_ddmaskColor, maskColor);
            menuTextSize = a.getDimensionPixelSize(R.styleable.DropDownMenu_ddmenuTextSize, menuTextSize);
            menuSelectedIcon = a.getResourceId(R.styleable.DropDownMenu_ddmenuSelectedIcon, menuSelectedIcon);
            menuUnselectedIcon = a.getResourceId(R.styleable.DropDownMenu_ddmenuUnselectedIcon, menuUnselectedIcon);
            a.recycle();
        }

        //初始化tabMenuView并添加到tabMenuView
        tabMenuView = new LinearLayout(context);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tabMenuView.setOrientation(HORIZONTAL);
        tabMenuView.setGravity(Gravity.CENTER_VERTICAL);
        tabMenuView.setBackgroundColor(menuBackgroundColor);
        tabMenuView.setLayoutParams(params);
        addView(tabMenuView, 0);

        //为tabMenuView添加下划线
        underLine = new View(getContext());
        underLine.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpTpPx(0.5f)));
        underLine.setBackgroundColor(underlineColor);
        addView(underLine, 1);

        //初始化containerView并将其添加到DropDownMenu
        containerView = new FrameLayout(context);
        containerView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        addView(containerView, 2);
    }

    /**
     * 初始化DropDownMenu
     *
     * @param tabTexts
     * @param popupViews
     * @param contentView
     */
    public void setDropDownMenu(@Nullable List<String> tabTexts, @Nullable List<View> popupViews, @NonNull View contentView) {
        if(tabTexts != null && popupViews != null) {
            if (tabTexts.size() != popupViews.size()) {
                throw new IllegalArgumentException("params not match, tabTexts.size() should be equal popupViews.size()");
            }

            for (int i = 0; i < tabTexts.size(); i++) {
                addTab(tabTexts, i);
            }
        }else{
            underLine.setVisibility(View.GONE);
        }
        containerView.addView(contentView, 0);

        maskView = new View(getContext());
        maskView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        maskView.setBackgroundColor(maskColor);
        maskView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                closeMenu();
            }
        });
        containerView.addView(maskView, 1);
        maskView.setVisibility(GONE);

        popupMenuViews = new FrameLayout(getContext());
        popupMenuViews.setVisibility(GONE);
        containerView.addView(popupMenuViews, 2);

        if(popupViews != null) {
            for (int i = 0; i < popupViews.size(); i++) {
//                popupViews.get(i).setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                        ScreenSize.getScreenHeightExcludeStatusBar(getContext().getApplicationContext()) / 3));
                popupMenuViews.addView(popupViews.get(i), i);
            }
        }
    }

    /**
     * 初始化完成时，需要改变menu数量内容时调用
     * @param tabTexts
     * @param popupViews
     */
    public void setDropDownMenu(@NonNull List<String> tabTexts, @NonNull List<View> popupViews) {
        if (tabTexts.size() != popupViews.size()) {
            throw new IllegalArgumentException("params not match, tabTexts.size() should be equal popupViews.size()");
        }

        if(tabMenuView != null && popupMenuViews != null){
            underLine.setVisibility(View.VISIBLE);
            tabMenuView.removeAllViews();
            popupMenuViews.removeAllViews();
            if(tabTexts != null) {
                for (int i = 0; i < tabTexts.size(); i++) {
                    addTab(tabTexts, i);
                }
            }

            if(popupViews != null) {
                for (int i = 0; i < popupViews.size(); i++) {
//                    popupViews.get(i).setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                            ScreenSize.getScreenHeightExcludeStatusBar(getContext().getApplicationContext()) / 3));
                    popupMenuViews.addView(popupViews.get(i), i);
                }
            }
        }else{
            throw new IllegalStateException("DropDownMenu没有初始化完成");
        }
    }

    private void addTab(@NonNull List<String> tabTexts, int i) {
        final LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(HORIZONTAL);
        linearLayout.setLayoutParams(new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
        linearLayout.setGravity(Gravity.CENTER);

        final TextView tab = new TextView(getContext());
        tab.setSingleLine();
        tab.setEllipsize(TextUtils.TruncateAt.END);
        tab.setGravity(Gravity.CENTER);
        tab.setTextSize(TypedValue.COMPLEX_UNIT_PX, menuTextSize);
        tab.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tab.setTextColor(textUnselectedColor);
        tab.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(menuUnselectedIcon), null);
        tab.setText(tabTexts.get(i));
        tab.setPadding(dpTpPx(5), dpTpPx(12), dpTpPx(5), dpTpPx(12));
        tab.setCompoundDrawablePadding(8);
        linearLayout.addView(tab);
//        final ImageView imageview = new ImageView(getContext());
//        imageview.setImageResource(menuUnselectedIcon);
//        imageview.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//        linearLayout.addView(imageview);
        //添加点击事件
        linearLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                switchMenu(linearLayout);
            }
        });
        tabMenuView.addView(linearLayout);
        //添加分割线
        if (i < tabTexts.size() - 1) {
            View view = new View(getContext());
            view.setLayoutParams(new LayoutParams(dpTpPx(0.5f), dpTpPx(20)));
//            view.setBackgroundColor(dividerColor);
            view.setBackgroundColor(getResources().getColor(R.color.divider));
//            view.setBackgroundResource(R.drawable.shape_comment_like_line);
            tabMenuView.addView(view);
        }
    }

    /**
     * 改变tab文字
     *
     * @param text
     */
    public void setTabText(String text) {
        if (current_tab_position != -1) {
            ((TextView)((LinearLayout) tabMenuView.getChildAt(current_tab_position)).getChildAt(0))
                    .setText(text);
        }
    }

    /**
     * 根据索引值设置菜单项
     * @param index
     * @param text
     */
    public void setTabText(int index, String text){
        if(index >= 0 && (index * 2) < tabMenuView.getChildCount()){
            ((TextView)((LinearLayout) tabMenuView.getChildAt(index * 2)).getChildAt(0))
                    .setText(text);
        }
    }

    /**
     * 设置菜单不可点击, 初始化完成之后调用
     * @param clickable
     */
    public void setTabClickable(int index, boolean clickable) {
        for (int i = 0; i < tabMenuView.getChildCount(); i = i + 2) {
            if(index == (i / 2)){
                tabMenuView.getChildAt(i).setClickable(clickable);
                ((TextView)((LinearLayout) tabMenuView.getChildAt(i)).getChildAt(0))
                        .setTextColor(getResources().getColor(R.color.neu_999999));
                break;
            }
        }
    }

    /**
     * 关闭菜单
     */
    public void closeMenu() {
        if (current_tab_position != -1) {
            LinearLayout linearLayout = (LinearLayout) tabMenuView.getChildAt(current_tab_position);
            if(mDropMenuClose != null){
                mDropMenuClose.menuClose(current_tab_position / 2, linearLayout);
            }
//            ((TextView) tabMenuView.getChildAt(current_tab_position)).setTextColor(textUnselectedColor);
            ((TextView) linearLayout.getChildAt(0)).setCompoundDrawablesWithIntrinsicBounds(null, null,
                    getResources().getDrawable(menuUnselectedIcon), null);
            ((TextView)linearLayout.getChildAt(0)).setTextColor(textUnselectedColor);
//            ((ImageView)linearLayout.getChildAt(1))
//                    .setImageResource(menuUnselectedIcon);
            popupMenuViews.setVisibility(View.GONE);
            popupMenuViews.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_menu_out));
            maskView.setVisibility(GONE);
            maskView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_mask_out));

            current_tab_position = -1;
        }

    }

    /**
     * DropDownMenu是否处于可见状态
     *
     * @return
     */
    public boolean isShowing() {
        return current_tab_position != -1;
    }

    /**
     * 切换菜单
     *
     * @param target
     */
    private void switchMenu(View target) {
        for (int i = 0; i < tabMenuView.getChildCount(); i = i + 2) {
            if (target == tabMenuView.getChildAt(i)) {
                if (current_tab_position == i) {
                    closeMenu();
                } else {
                    if (current_tab_position == -1) {
                        popupMenuViews.setVisibility(View.VISIBLE);
                        popupMenuViews.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_menu_in));
                        maskView.setVisibility(VISIBLE);
                        maskView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_mask_in));
                        popupMenuViews.getChildAt(i / 2).setVisibility(View.VISIBLE);

                    } else {
                        popupMenuViews.getChildAt(i / 2).setVisibility(View.VISIBLE);
                    }
                    current_tab_position = i;
                    ((TextView)((LinearLayout) tabMenuView.getChildAt(i)).getChildAt(0)).setTextColor(textSelectedColor);
//                    ((ImageView)((LinearLayout) tabMenuView.getChildAt(i)).getChildAt(1)).setImageResource(menuSelectedIcon);
                    ((TextView) ((LinearLayout) tabMenuView.getChildAt(i)).getChildAt(0)).setCompoundDrawablesWithIntrinsicBounds(null, null,
                            getResources().getDrawable(menuSelectedIcon), null);
                    if(mDropMenuClose != null){
                        mDropMenuClose.menuOpen(current_tab_position / 2, (LinearLayout) tabMenuView.getChildAt(i));
                    }
                }
            } else {
                ((TextView)((LinearLayout) tabMenuView.getChildAt(i)).getChildAt(0)).setTextColor(textUnselectedColor);
//                ((ImageView)((LinearLayout) tabMenuView.getChildAt(i)).getChildAt(1)).setImageResource(menuUnselectedIcon);
//                ((TextView) tabMenuView.getChildAt(i)).setTextColor(textUnselectedColor);
                ((TextView)((LinearLayout) tabMenuView.getChildAt(i)).getChildAt(0)).setCompoundDrawablesWithIntrinsicBounds(null, null,
                        getResources().getDrawable(menuUnselectedIcon), null);
                popupMenuViews.getChildAt(i / 2).setVisibility(View.GONE);
            }
        }
    }

    /**
     * 获取当前菜单索引
     * @return
     */
    public int getCurrentMenuIndex(){
        return current_tab_position;
    }

    private  int dpTpPx(float value) {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, dm) + 0.5);
    }

    private DropMenuClose mDropMenuClose;

    public void setDropMenuClose(DropMenuClose dropMenuClose) {
        mDropMenuClose = dropMenuClose;
    }

    public interface DropMenuClose{
        void menuClose(int position, LinearLayout linearLayout);
        void menuOpen(int position, LinearLayout linearLayout);
    }
}
