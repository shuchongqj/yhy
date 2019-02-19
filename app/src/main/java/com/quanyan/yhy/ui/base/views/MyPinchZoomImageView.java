package com.quanyan.yhy.ui.base.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class MyPinchZoomImageView extends PinchZoomImageView {
    private boolean mIsLongClick = false;
    private Runnable mLongClickRunnable;
    private float mActionDownX;
    private float mActionDownY;
    private View.OnClickListener onClickListener;
    private View.OnLongClickListener onLongClickListener;
    private Mode mode = Mode.NONE;
    private int mMode = Mode.NONE.ordinal();
    private final String TAG = "MyPinchZoomImageView";

    public static enum Mode {
        NONE, // 无效状态
        DRAGING, // 拖动中
        ZOOMING, // 缩放中
        CLICKING // 点击
    }

    /**
     * 设置模式
     * @param mode
     */
    public void setMode(int mode){
        mMode = mode;
    }

    public MyPinchZoomImageView(Context context, AttributeSet attributes) {
        super(context, attributes);
    }

    public MyPinchZoomImageView(Context context) {
        super(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(mMode == Mode.NONE.ordinal()){
            return super.onTouchEvent(event);
        }
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
        case MotionEvent.ACTION_DOWN:
            Log.i(TAG, "MotionEvent.ACTION_DOWN");
            mode = Mode.CLICKING;
            mIsLongClick = false;
            mActionDownX = event.getRawX();
            mActionDownY = event.getRawY();
            mLongClickRunnable = new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    mIsLongClick = true;
                }
            };
            postDelayed(mLongClickRunnable, 500);
            break;
        case MotionEvent.ACTION_POINTER_DOWN:
            Log.i(TAG, "MotionEvent.ACTION_POINTER_DOWN");
            mode = Mode.ZOOMING;
            break;
        case MotionEvent.ACTION_MOVE:
            break;
        case MotionEvent.ACTION_POINTER_UP:
            Log.i(TAG, "MotionEvent.ACTION_POINTER_UP");
            mode = Mode.NONE;
            break;
        case MotionEvent.ACTION_UP:
            Log.i(TAG, "MotionEvent.ACTION_UP");
            float deltX = event.getRawX() - mActionDownX;
            float deltY = event.getRawY() - mActionDownY;
            double distance = Math.sqrt(deltX * deltX + deltY * deltY);
            Log.i(TAG, "distance=" + distance);
            if (mLongClickRunnable != null)
                removeCallbacks(mLongClickRunnable);
            if (mode == Mode.CLICKING && distance == 0) {
                if (!mIsLongClick && onClickListener != null)
                    onClickListener.onClick(this);
                else if (mIsLongClick && onLongClickListener != null)
                    onLongClickListener.onLongClick(this);
            }
            mode = Mode.NONE;
            break;

        default:
            break;
        }
        return super.onTouchEvent(event);
    }

//    @Override
//    public void setOnClickListener(View.OnClickListener onClickListener) {
//        this.onClickListener = onClickListener;
//    }

    @Override
    public void setOnLongClickListener(View.OnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }
}
