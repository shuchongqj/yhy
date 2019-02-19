package com.quanyan.base.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quanyan.yhy.R;

/**
 * Created with Android Studio.
 * Title:AboutAndFeedController
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:zhaoxiaopo
 * Date:16/5/7
 * Time:12:40
 * Version 1.0
 */
public class BaseTransparentNavView extends RelativeLayout {

    private TransNavInterface mTransNavInterface;
    private LinearLayout linearLayout;
    public BaseTransparentNavView(Context context) {
        super(context);
        init(context);
    }

    public BaseTransparentNavView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BaseTransparentNavView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BaseTransparentNavView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(final Context context) {
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        View.inflate(context, R.layout.transparent_topbar, this);
        findViewById(R.id.trasparent_topbar_left_layout).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 16/5/7 返回键
                if(context instanceof Activity){
                    ((Activity) context).finish();
                }
            }
        });
        if(context instanceof TransNavInterface){
            mTransNavInterface = (TransNavInterface) context;
            final ImageView imageView = (ImageView) findViewById(R.id.trasparent_topbar_right_like);
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: 16/5/7 点赞
                    mTransNavInterface.praiseClick(imageView);
                }
            });
            findViewById(R.id.trasparent_topbar_right_collect).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: 16/5/7 收藏
                    mTransNavInterface.collectClick();
                }
            });
            findViewById(R.id.trasparent_topbar_right_share).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: 16/5/7 分享
                    mTransNavInterface.shareClick();
                }
            });
        }

         linearLayout = new LinearLayout(context);
        RelativeLayout.LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2);
        layoutParams.addRule(ALIGN_PARENT_BOTTOM);
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setBackgroundColor(getResources().getColor(R.color.orderline_e1e1e1));
        addView(linearLayout);
    }

    public void showBottomDivid(boolean visible){
        linearLayout.setVisibility(visible?View.VISIBLE:View.INVISIBLE);
    }

    /**
     * 返回左侧返回键的ImageView
     * @return
     */
    public ImageView getLeftImg(){
        return (ImageView) findViewById(R.id.trasparent_topbar_left);
    }
    /**
     * 获取点赞的ImageView用于设置图标
     *
     * @return ImageView对象
     */
    public ImageView getPrasieView(){
        return (ImageView) findViewById(R.id.trasparent_topbar_right_like);
    }

    /**
     * 获取抽藏的ImageView用于设置图标
     * @return ImageView对象
     */
    public ImageView getCollectView(){
        return (ImageView) findViewById(R.id.trasparent_topbar_right_collect);
    }

    /**
     * 获得分享的ImageView
     * @return
     */
    public ImageView getShareView(){
        return (ImageView) findViewById(R.id.trasparent_topbar_right_share);
    }

    public void showTitleText(){
        findViewById(R.id.trasparent_topbar_title).setVisibility(View.VISIBLE);
    }

    public void hideTitleText(){
        findViewById(R.id.trasparent_topbar_title).setVisibility(View.INVISIBLE);
    }

    /**
     * 设置标题
     * @param titleText
     */
    public void setTitleText(String titleText){
        ((TextView)findViewById(R.id.trasparent_topbar_title)).setText(titleText);
    }

    /**
     * 设置标题
     * @param stringRes
     */
    public void setTitleText(int stringRes){
        ((TextView)findViewById(R.id.trasparent_topbar_title)).setText(stringRes);
    }

    /**
     * 设置标题的颜色
     * @param colorRes 具体颜色的一个资源ID
     */
    public void setTitleTextColor(int colorRes){
        ((TextView)findViewById(R.id.trasparent_topbar_title)).setTextColor(getResources().getColor(colorRes));
    }

    /**
     * 设置标题的颜色
     * @param titleTextColor {@link android.graphics.Color#argb(int, int, int, int)}}设置的颜色
     */
    public void setTitleTextColor2(int titleTextColor){
        ((TextView)findViewById(R.id.trasparent_topbar_title)).setTextColor(titleTextColor);
    }

    /**
     * 导航栏操作的的接口回调，（分享，收藏，点赞）
     */
    public interface TransNavInterface{
        void shareClick();
        void collectClick();
        void praiseClick(ImageView imageView);
    }
}
