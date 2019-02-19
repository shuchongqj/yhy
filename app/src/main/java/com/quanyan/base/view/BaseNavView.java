package com.quanyan.base.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quanyan.base.util.ScreenSize;
import com.quanyan.yhy.R;
import com.quanyan.yhy.view.SearchEditText;
import com.yhy.common.utils.AndroidUtils;
import com.yhy.common.utils.CommonUtil;
import com.yhy.imageloader.ImageLoadManager;

/**
 * Created with Android Studio.
 * Title:AboutAndFeedController
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:zhaoxiaopo
 * Date:16/4/28
 * Time:13:34
 * Version 1.0
 */
public class BaseNavView extends RelativeLayout {
    LinearLayout bottomLayout;

    public BaseNavView(Context context) {
        super(context);
        initView(context);
    }

    public BaseNavView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public BaseNavView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BaseNavView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(final Context context) {
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ScreenSize.convertDIP2PX(context.getApplicationContext(), 48)));
        View.inflate(context, R.layout.base_nav_view, this);
    /*    setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });*/
        findViewById(R.id.base_nav_view_left_layout).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 16/4/28 返回键
                if (getContext() instanceof Activity) {
                    ((Activity) getContext()).onBackPressed();
                }
            }
        });

        bottomLayout = new LinearLayout(context);
        RelativeLayout.LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2);
        layoutParams.addRule(ALIGN_PARENT_BOTTOM);
        bottomLayout.setLayoutParams(layoutParams);
        bottomLayout.setBackgroundColor(getResources().getColor(R.color.orderline_e1e1e1));
        addView(bottomLayout);

    }

    public void setBottomLayoutVisiable(boolean isShow) {
        bottomLayout.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    /**
     * 显示左侧布局
     */
    public void showLeftLayout() {
        findViewById(R.id.base_nav_view_left_layout).setVisibility(VISIBLE);
    }

    /**
     * 显示右侧图片布局
     */
    public void showRightImgLayout() {
        findViewById(R.id.base_nav_view_right_layout).setVisibility(VISIBLE);
    }

    /**
     * 显示右侧文本布局
     */
    public void showRightTextLayout() {
        findViewById(R.id.base_nav_view_right_text_layout).setVisibility(VISIBLE);
    }

    /**
     * 显示右侧文本布局（内容为wrap_content）
     */
    public void showRightTextWrapLayout() {
        findViewById(R.id.base_nav_view_right_text_layout_wrap).setVisibility(VISIBLE);
    }

    /**
     * 显示搜索框
     *
     * @param textOrImg 右侧是图片还是文本，true：文本，false：图片
     */
    public void showSeachView(boolean textOrImg, String hint) {
        findViewById(R.id.base_nav_view_left_layout).setVisibility(View.GONE);
        findViewById(R.id.base_nav_view_title).setVisibility(View.GONE);
        ((SearchEditText) findViewById(R.id.base_nav_view_edit)).setHint(hint);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.base_nav_view_edit_layout);
        linearLayout.setVisibility(View.VISIBLE);
        linearLayout.setSelected(true);
        RelativeLayout.LayoutParams layoutParams = (LayoutParams) linearLayout.getLayoutParams();
        layoutParams.rightMargin = 10;
        layoutParams.leftMargin = 30;
        if (textOrImg) {
            layoutParams.addRule(RelativeLayout.LEFT_OF, R.id.base_nav_view_right_text_layout);
        } else {
            layoutParams.addRule(RelativeLayout.LEFT_OF, R.id.base_nav_search_right_img_layout);
        }
        linearLayout.setLayoutParams(layoutParams);
    }


    /**
     * 显示搜索框
     *
     * @param showLeft
     * @param showRight
     * @param rightTextOrImg 右侧是图片还是文本，true：文本，false：图片
     * @param hint
     */
    public void showSeachView(boolean showLeft, boolean showRight, boolean rightTextOrImg, String hint) {
        findViewById(R.id.base_nav_view_title).setVisibility(View.GONE);

        ((SearchEditText) findViewById(R.id.base_nav_view_edit)).setHint(hint);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.base_nav_view_edit_layout);
        linearLayout.setVisibility(View.VISIBLE);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setSelected(true);

        RelativeLayout.LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        if (!showLeft) {
            findViewById(R.id.base_nav_view_left_layout).setVisibility(View.GONE);
            layoutParams.leftMargin = 30;
        } else {
            layoutParams.addRule(RelativeLayout.RIGHT_OF, R.id.base_nav_view_left_layout);
        }

        if (!showRight) {
            findViewById(R.id.base_nav_view_right_layout).setVisibility(View.GONE);
            findViewById(R.id.base_nav_view_right_text_layout).setVisibility(View.GONE);
            findViewById(R.id.base_nav_view_right_text_layout_wrap).setVisibility(View.GONE);
            layoutParams.rightMargin = 30;
        } else {
            if (rightTextOrImg) {
                layoutParams.addRule(RelativeLayout.LEFT_OF, R.id.base_nav_view_right_text_layout);
            } else {
                layoutParams.addRule(RelativeLayout.LEFT_OF, R.id.base_nav_search_right_img_layout);
            }
        }
        linearLayout.setLayoutParams(layoutParams);
    }

    /**
     * 获取搜索框对象
     *
     * @return {@link SearchEditText}
     */
    public SearchEditText getSearchBox() {
        return (SearchEditText) findViewById(R.id.base_nav_view_edit);
    }

    /**
     * 设置返回按钮的图标
     *
     * @param imgResID 图片资源ID
     */
    public void setLeftImg(int imgResID) {
        View view = findViewById(R.id.base_nav_view_left_layout);
        if (view.getVisibility() == GONE) {
            view.setVisibility(View.VISIBLE);
        }
        ((ImageView) findViewById(R.id.base_nav_view_back_img)).setImageResource(imgResID);
    }

    /**
     * 设置标题栏左边图标
     *
     * @param imgResID 图片资源ID
     */
    public void setLeftImage(int imgResID) {
        View view = findViewById(R.id.base_nav_view_left_layout);
        if (view.getVisibility() == GONE) {
            view.setVisibility(View.VISIBLE);
        }
        findViewById(R.id.base_nav_view_left_text).setVisibility(View.GONE);
        ImageView leftImg = ((ImageView) findViewById(R.id.base_nav_view_back_img));
        if (leftImg.getVisibility() == View.GONE) {
            leftImg.setVisibility(View.VISIBLE);
        }
        leftImg.setImageResource(imgResID);
    }

    /**
     * 设置左侧栏的点击事件
     *
     * @param leftImgClick {@link android.view.View.OnClickListener}
     */
    public void setLeftClick(View.OnClickListener leftImgClick) {
        findViewById(R.id.base_nav_view_left_layout).setOnClickListener(leftImgClick);
    }

    /**
     * 设置标题栏左边文本
     *
     * @param text {@link String}类型的字符串
     */
    public void setLeftText(String text) {
        View view = findViewById(R.id.base_nav_view_left_layout);
        if (view.getVisibility() == GONE) {
            view.setVisibility(View.VISIBLE);
        }
        findViewById(R.id.base_nav_view_back_img).setVisibility(View.GONE);
        TextView leftText = (TextView) findViewById(R.id.base_nav_view_left_text);
        if (leftText.getVisibility() == View.GONE) {
            leftText.setVisibility(View.VISIBLE);
        }

        if (text != null) {
            leftText.setText(text);
        }
    }

    /**
     * 设置左边文本颜色
     *
     * @param colorResId
     */
    public void setLeftTextColor(int colorResId) {
        findViewById(R.id.base_nav_view_back_img).setVisibility(View.GONE);
        TextView leftText = (TextView) findViewById(R.id.base_nav_view_left_text);
        if (leftText.getVisibility() == View.GONE) {
            leftText.setVisibility(View.VISIBLE);
        }

        if (colorResId != -1) {
            leftText.setTextColor(getContext().getResources().getColor(colorResId));
        }
    }


    /**
     * 设置界面
     *
     * @param titleText {@link String}类型的字符串
     */
    public void setTitleText(String titleText) {
        ((TextView) findViewById(R.id.base_nav_view_title)).setText(titleText);
    }

    public void setTitleColor(String color) {
        ((TextView) findViewById(R.id.base_nav_view_title)).setTextColor(Color.parseColor(color));
    }

    public void setImageFilter(int stringResID) {
        ((ImageView) findViewById(R.id.iv_order_filter)).setVisibility(View.VISIBLE);
        ((ImageView) findViewById(R.id.iv_order_filter)).setImageResource(stringResID);
    }


    /**
     * 设置界面标题
     *
     * @param stringResID {@link String}资源文件的ID
     */
    public void setTitleText(int stringResID) {
        ((TextView) findViewById(R.id.base_nav_view_title)).setText(stringResID);
    }

    public void setTitleVisible(boolean show) {
        ((TextView) findViewById(R.id.base_nav_view_title)).setVisibility(show ? VISIBLE : GONE);
    }

    /**
     * 拓展View的布局格式，LayoutParams根据需要自行设置, 提供布局ID
     *
     * @param viewGroupID  {@link BaseNavView}布局中使用的所有布局ID如下：
     *                     <li>R.id.base_nav_view_left_layout, 左侧布局（图片或者文字）</li>
     *                     <li>R.id.base_nav_view_edit_layout, 搜索框布局</li>
     *                     <li>R.id.base_nav_view_right_layout, 右侧图片布局</li>
     *                     <li>R.id.base_nav_view_right_text_layout, 右侧文本布局</li>
     *                     <li>R.id.base_nav_view_right_text_layout_wrap, 右侧文本布局（文本宽度为wrap_content）</li>
     * @param layoutParams
     */
    public void resetLayoutParams(int viewGroupID, RelativeLayout.LayoutParams layoutParams) {
        LinearLayout linearLayout = (LinearLayout) findViewById(viewGroupID);
        linearLayout.setLayoutParams(layoutParams);
    }

    /**
     * 设置右侧文本，默认会显示文本，将右侧图片吧布局隐藏
     *
     * @param titleText 文本内容
     */
    public void setRightText(String titleText) {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.base_nav_view_right_text_layout);
        if (View.GONE == linearLayout.getVisibility()) {
            linearLayout.setVisibility(View.VISIBLE);
        }
        View view = findViewById(R.id.base_nav_view_right_layout);
        if (View.VISIBLE == view.getVisibility()) {
            view.setVisibility(View.GONE);
        }
        ((TextView) findViewById(R.id.base_nav_view_right_text)).setText(titleText);
    }

    /**
     * 设置右侧文本，默认会显示文本，将右侧图片吧布局隐藏
     *
     * @param stringResID 文本资源id
     */
    public void setRightText(int stringResID) {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.base_nav_view_right_text_layout);
        if (View.GONE == linearLayout.getVisibility()) {
            linearLayout.setVisibility(View.VISIBLE);
        }
        View view = findViewById(R.id.base_nav_view_right_layout);
        if (View.VISIBLE == view.getVisibility()) {
            view.setVisibility(View.GONE);
        }
        ((TextView) findViewById(R.id.base_nav_view_right_text)).setText(stringResID);
    }

    /**
     * 设置右侧带图片的textview
     *
     * @param stringResID
     * @param imageId
     */
    public void setRightTextAndImg(int stringResID, int imageId) {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.base_nav_view_right_text_layout_wrap);
        if (View.GONE == linearLayout.getVisibility()) {
            linearLayout.setVisibility(View.VISIBLE);
        }
        View view = findViewById(R.id.base_nav_view_right_layout);
        if (View.VISIBLE == view.getVisibility()) {
            view.setVisibility(View.GONE);
        }
        ((TextView) findViewById(R.id.base_nav_view_right_text_wrap)).setText(stringResID);
        Drawable leftDrawable = getResources().getDrawable(imageId);
        //((TextView) findViewById(R.id.base_nav_view_right_text)).setCompoundDrawablePadding(20);
        leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(), leftDrawable.getMinimumHeight());
        ((TextView) findViewById(R.id.base_nav_view_right_text_wrap)).setCompoundDrawables(leftDrawable, null, null, null);
        ((TextView) findViewById(R.id.base_nav_view_right_text_wrap)).setCompoundDrawablePadding(20);
    }

    /**
     * 设置右侧文本是否可以点击
     *
     * @param flag
     */
    public void setRightTextEnable(boolean flag) {
        TextView textView = ((TextView) findViewById(R.id.base_nav_view_right_text));
        textView.setEnabled(flag);
        textView.invalidate();
    }

    /**
     * 设置右侧文本的颜色
     *
     * @param color
     */
    public void setRightTextColor(int color) {
        ((TextView) findViewById(R.id.base_nav_view_right_text)).setTextColor(getResources().getColor(color));
    }

    public String getRightText() {
        return ((TextView) findViewById(R.id.base_nav_view_right_text)).getText().toString();
    }

    /**
     * 设置右侧图片，会隐藏右侧文本布局
     *
     * @param imgResID 图片资源id
     */
    public void setRightImg(int imgResID) {
        ((ImageView) findViewById(R.id.base_nav_view_right_img)).setImageResource(imgResID);

    }

    public void removeRightView() {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.base_nav_view_right_text_layout_wrap);
        linearLayout.removeAllViews();
    }

    public void removeLineView() {
        if (bottomLayout.getParent() != null) {
            removeView(bottomLayout);
        }
    }

    public void addLineView() {
        if (bottomLayout.getParent() == null) {
            addView(bottomLayout);
        }
    }

    public void addRightView(String title, String url, String color, int font, boolean bold, View.OnClickListener listener) {
        if (TextUtils.isEmpty(title)) {
            return;
        }
        showRightTextWrapLayout();
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.base_nav_view_right_text_layout_wrap);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        lp.gravity = Gravity.CENTER_VERTICAL;
        if (!TextUtils.isEmpty(url)) {
            final ImageView imageView = new ImageView(getContext());
            imageView.setLayoutParams(lp);
            imageView.setPadding(0, 0, 30, 0);
            int width = AndroidUtils.dip2px(getContext(), 20);
//            Glide.with(getContext()).load(url).override(width, width).into(imageView);
            ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(url), 0, width, width, imageView);
            if (listener != null) {
                imageView.setOnClickListener(listener);
            }
            linearLayout.addView(imageView);
        } else {
            TextView textView = new TextView(getContext());
            textView.setGravity(Gravity.CENTER);
            textView.setLayoutParams(lp);
            textView.setPadding(AndroidUtils.dip2px(getContext(), 16),
                    0, AndroidUtils.dip2px(getContext(), 16), 0);
            textView.setText(title);
            if (!TextUtils.isEmpty(color))
                textView.setTextColor(Color.parseColor(color));
            if (font > 0) {
                textView.setTextSize(font);
            }
            if (bold) {
                textView.setTextAppearance(getContext(), R.style.TextBold);
            }
            if (listener != null) {
                textView.setOnClickListener(listener);
            }
            linearLayout.addView(textView);
        }
    }

    /**
     * 设置右侧文本的点击事件
     *
     * @param rightImgClick
     */
    public void setRightTextClick(View.OnClickListener rightImgClick) {
        if (rightImgClick != null) {
            findViewById(R.id.base_nav_view_right_text_layout).setOnClickListener(rightImgClick);
        }
    }

    public void setRightEnable(boolean enable) {
        findViewById(R.id.base_nav_view_right_text_layout).setEnabled(enable);
    }

    /**
     * 右侧文本加图片点击事件
     *
     * @param rightImgClick
     */
    public void setRightTextAndImgClick(View.OnClickListener rightImgClick) {
        if (rightImgClick != null) {
            findViewById(R.id.base_nav_view_right_text_layout_wrap).setOnClickListener(rightImgClick);
        }
    }

    /**
     * 设置右侧图片的点击事件
     *
     * @param rightImgClick
     */
    public void setRightImgClick(View.OnClickListener rightImgClick) {
        if (rightImgClick != null) {
            findViewById(R.id.base_nav_view_right_layout).setOnClickListener(rightImgClick);
        }
    }

    /**
     * 针对发直播设置“暂存”状态按钮
     */
    public void showSaveImg() {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.base_nav_view_right_layout);
        linearLayout.setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams layoutParams = new LayoutParams(
                ScreenSize.convertDIP2PX(getContext().getApplicationContext(), 48),
                ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.addRule(RelativeLayout.LEFT_OF, R.id.base_nav_view_right_text_layout);
        linearLayout.setLayoutParams(layoutParams);
    }

}
