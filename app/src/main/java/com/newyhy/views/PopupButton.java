package com.newyhy.views;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.quanyan.yhy.R;

/**
 * 自定义的带弹出框的按钮,类似于美团和大众点评的筛选框
 *
 * Created by yangboxue on 2018/4/12.
 */

public class PopupButton extends AppCompatTextView implements PopupWindow.OnDismissListener {
    private int normalBg;//正常状态下的背景
    private int pressBg;//按下状态下的背景
    private int normalIcon;//正常状态下的图标
    private int pressIcon;//按下状态下的图标
    private int normalColor;
    private int pressColor;
    private PopupWindow popupWindow;
    private Context context;
    private int screenWidth;
    private int screenHeight;
    private int paddingTop;
    private int paddingLeft;
    private int paddingRight;
    private int paddingBottom;
    private PopupButtonListener listener;

    public PopupButton(Context context) {
        super(context);
        this.context = context;
    }

    public PopupButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initAttrs(context, attrs);
        initBtn(context);
    }

    public PopupButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    //初始化各种自定义参数
    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.popupbtn);

        normalBg = typedArray.getResourceId(R.styleable.popupbtn_normalBg, -1);
        pressBg = typedArray.getResourceId(R.styleable.popupbtn_pressBg, -1);
        normalIcon = typedArray.getResourceId(R.styleable.popupbtn_normalIcon, -1);
        pressIcon = typedArray.getResourceId(R.styleable.popupbtn_pressIcon, -1);
        normalColor = typedArray.getColor(R.styleable.popupbtn_normalColor, Color.parseColor("#2d2d37"));
        pressColor = typedArray.getColor(R.styleable.popupbtn_pressColor, Color.parseColor("#ed4d4d"));
    }

    /**
     * 初始话各种按钮样式
     */
    private void initBtn(final Context context) {
        paddingTop = getResources().getDimensionPixelSize(R.dimen.value_6dp);
        paddingLeft = getResources().getDimensionPixelSize(R.dimen.value_6dp);
        paddingRight = getResources().getDimensionPixelSize(R.dimen.value_6dp);
        paddingBottom = getResources().getDimensionPixelSize(R.dimen.value_6dp);
        setNormal();

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        screenWidth = wm.getDefaultDisplay().getWidth();
        screenHeight = wm.getDefaultDisplay().getHeight();

    }

    /**
     * 隐藏弹出框
     */
    public void hidePopup() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }

    /**
     * 设置自定义接口
     *
     * @param listener
     */
    public void setListener(PopupButtonListener listener) {
        this.listener = listener;
    }

    public void setPopupView(final View view, final int h) {
        setPopupView(view, h, -1);
    }

    /**
     * 运动场首页弹框，底部可展示区域过小，所以动态设置popupWindow的高度
     *
     * @param view 内容View
     * @param h    内容的高度
     */
//    public void setPopupViewForSportHomePage(final View view, final int h, final int filterViewHeight, final String type) {
//        this.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!TextUtils.isEmpty(type)) {
//                    StatisticsUtils.onEvent(type);
//                }
//                int[] location = new int[2];
//                getLocationOnScreen(location);
//
//                if (popupWindow == null) {
//                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (getResources().getDisplayMetrics().density * h));
//                    view.setLayoutParams(params);
//
//                    LinearLayout layout = new LinearLayout(context);
//                    layout.addView(view);
//                    layout.setBackgroundColor(Color.argb(60, 256, 0, 0));
//
//                    if (h == 0) {
//                        popupWindow = new PopupWindow(layout, screenWidth, (int) (getResources().getDisplayMetrics().density * 100));
//                    } else {
//                        popupWindow = new PopupWindow(layout, screenWidth, screenHeight - location[1] - filterViewHeight);
//                    }
//
//                    popupWindow.setFocusable(true);
//                    popupWindow.setBackgroundDrawable(new ColorDrawable(0xb0000000));
//                    popupWindow.setOutsideTouchable(true);
//                    popupWindow.setOnDismissListener(PopupButton.this);
//                    layout.setOnClickListener(new OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            popupWindow.dismiss();
//                        }
//                    });
//                } else {
//                    if (h == 0) {
//                        popupWindow.setHeight((int) (getResources().getDisplayMetrics().density * 100));
//                    } else {
//                        popupWindow.setHeight(screenHeight - location[1] - filterViewHeight);
//                    }
//                }
//
//                if (listener != null) {
//                    listener.onShow();
//                }
//
//                popupWindow.showAtLocation(PopupButton.this, Gravity.TOP, 0, location[1] + filterViewHeight);
//
//                setPress();
//            }
//        });
//    }

    /**
     * 设置popupwindow的view
     *
     * @param view
     */
    public void setPopupView(final View view, final int h, final int up) {
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (popupWindow == null) {
//                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
//                    view.setLayoutParams(params);
//
//                    LinearLayout layout = new LinearLayout(context);
//                    layout.addView(view);
//                    layout.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
////                    layout.setBackgroundColor(Color.argb(60, 255, 0, 0));
//
//                    if (h == 0) {
//                        popupWindow = new PopupWindow(layout, screenWidth, (int) (getResources().getDisplayMetrics().density * 100));
//                    } else {
//                        popupWindow = new PopupWindow(layout, screenWidth, (int) (getResources().getDisplayMetrics().density * h));
//                    }
//
//                    popupWindow.setFocusable(true);
//                    popupWindow.setBackgroundDrawable(new ColorDrawable(0xb0000000));
//                    popupWindow.setOutsideTouchable(true);
//                    popupWindow.setOnDismissListener(PopupButton.this);
//                    layout.setOnClickListener(new OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            popupWindow.dismiss();
//                        }
//                    });
//                } else {
//                    if (h == 0) {
//                        popupWindow.setHeight((int) (getResources().getDisplayMetrics().density * 100));
//                    } else {
//                        popupWindow.setHeight((int) (getResources().getDisplayMetrics().density * h));
//                    }
//                }

                if (listener != null) {
                    listener.onShow();
                }

                setPress();
                // TODO: 2018/7/12   不弹出来了
//                if (up == -1) {
//                    popupWindow.showAsDropDown(PopupButton.this);
//                } else {
//                    popupWindow.showAtLocation(PopupButton.this, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, up);
//                }
            }
        });
    }


    public void setPopupView(final int h, final int up, final int bg, final View view) {
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow == null) {
                    LinearLayout layout = new LinearLayout(context);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (getResources()
                            .getDisplayMetrics().density * h));
                    view.setLayoutParams(params);
                    layout.addView(view);
                    layout.setBackgroundColor(Color.argb(bg, 0, 0, 0));
                    popupWindow = new PopupWindow(layout, screenWidth, (int) (getResources()
                            .getDisplayMetrics().density * h));
                    popupWindow.setFocusable(true);
//                    popupWindow.setBackgroundDrawable(new BitmapDrawable());
                    ColorDrawable dw = new ColorDrawable(0xb0000000);
                    popupWindow.setBackgroundDrawable(dw);
                    popupWindow.setOutsideTouchable(true);
                    popupWindow.setOnDismissListener(PopupButton.this);
                    layout.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popupWindow.dismiss();
                        }
                    });
                } else {
                    popupWindow.setHeight((int) (getResources().getDisplayMetrics().density * h));
                }
                if (listener != null) {
                    listener.onShow();
                }
                setPress();
                if (up == -1) {
                    popupWindow.showAsDropDown(PopupButton.this);
                } else {
                    popupWindow.showAtLocation(PopupButton.this, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, up);
                }
            }
        });
    }

    public void setPopupView(final View view, final int w, final int h, final int y) {
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(((Activity) context).getWindow().peekDecorView().getWindowToken(), 0);
                if (popupWindow == null) {
                    LinearLayout layout = new LinearLayout(context);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(w, (int) (getResources()
                            .getDisplayMetrics().density * h));
                    layout.setGravity(Gravity.RIGHT | Gravity.BOTTOM);
                    view.setLayoutParams(params);
                    layout.addView(view);
                    layout.setBackgroundColor(Color.argb(60, 0, 0, 0));
                    popupWindow = new PopupWindow(layout, screenWidth, screenHeight - y);
                    popupWindow.setFocusable(true);
                    popupWindow.setBackgroundDrawable(new BitmapDrawable());
                    popupWindow.setOutsideTouchable(true);
                    popupWindow.setOnDismissListener(PopupButton.this);
                    layout.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popupWindow.dismiss();
                        }
                    });
                }
                if (listener != null) {
                    listener.onShow();
                }
                setPress();
                if (y == -1) {
                    popupWindow.showAsDropDown(PopupButton.this);
                } else {
                    popupWindow.showAtLocation(PopupButton.this, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, y);
                }
            }
        });
    }

    public void setPopupView(int x, final View view, final int w, final int h) {
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow == null) {
                    LinearLayout layout = new LinearLayout(context);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(w, (int) (getResources()
                            .getDisplayMetrics().density * h));
                    layout.setGravity(Gravity.RIGHT | Gravity.BOTTOM);
                    view.setLayoutParams(params);
                    layout.addView(view);
                    layout.setBackgroundColor(Color.argb(60, 0, 0, 0));
                    popupWindow = new PopupWindow(layout, w, (int) (getResources()
                            .getDisplayMetrics().density * h));
                    popupWindow.setFocusable(true);
                    popupWindow.setBackgroundDrawable(new BitmapDrawable());
                    popupWindow.setOutsideTouchable(true);
                    popupWindow.setOnDismissListener(PopupButton.this);
                    layout.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popupWindow.dismiss();
                        }
                    });
                }
                if (listener != null) {
                    listener.onShow();
                }
                setPress();
                popupWindow.showAsDropDown(PopupButton.this);

            }
        });
    }

    /**
     * 设置选中时候的按钮状态
     */
    private void setPress() {
        if (pressBg != -1) {
            this.setBackgroundResource(pressBg);
            this.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
            this.setTextColor(pressColor);
        }
        if (pressIcon != -1) {
            Drawable drawable = getResources().getDrawable(pressIcon);
            /// 这一步必须要做,否则不会显示.
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            this.setCompoundDrawables(null, null, drawable, null);
        }
    }

    /**
     * 设置正常模式下的按钮状态
     */
    private void setNormal() {
        if (normalBg != -1) {
            this.setBackgroundResource(normalBg);
            this.setTextColor(normalColor);
            this.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
        }
        if (normalIcon != -1) {
            Drawable drawable = getResources().getDrawable(normalIcon);
            /// 这一步必须要做,否则不会显示.
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            this.setCompoundDrawables(null, null, drawable, null);
        }
    }

    @Override
    public void onDismiss() {
        setNormal();
        if (listener != null) {
            listener.onHide();
        }
    }

    /**
     * 显隐用到的接口，用于在显示和隐藏的时候处理一些自定义操作
     * Created by Chris on 2014/12/11.
     */
    public interface PopupButtonListener {
        public void onShow();
        public void onHide();
    }
}
