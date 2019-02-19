package com.quanyan.yhy.ui.discovery;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.quanyan.yhy.R;

/**
 * Created with Android Studio.
 * Title:VideoCachePopView
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:徐学东
 * Date:16/5/23
 * Time:13:02
 * Version 1.1.0
 */
public class VideoCachePopView extends PopupWindow {

    private Context mContext;
    private View.OnClickListener mOnClickListener;

    public VideoCachePopView(Context context, View.OnClickListener onClickListener){
        mContext = context;
        mOnClickListener = onClickListener;
        final View view = View.inflate(context, R.layout.dialog_video_cache, null);
        view.findViewById(R.id.dialog_video_cache_quit).setOnClickListener(onClickListener);
        view.findViewById(R.id.dialog_video_cache_while).setOnClickListener(onClickListener);
        view.findViewById(R.id.dialog_video_cache_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        //设置SelectPicPopupWindow的View
        this.setContentView(view);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.MenuDialogAnimation);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        view.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = view.findViewById(R.id.dialog_video_cache_layout).getTop();
                int y=(int) event.getY();
                if(event.getAction()==MotionEvent.ACTION_UP){
                    if(y<height){
                        dismiss();
                    }
                }
                return true;
            }
        });
    }
}
