package com.quanyan.yhy.ui.common.city.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.quanyan.yhy.R;

public class QuickIndexBar extends View {

    private Paint paint;
    private int cellWidth;
    private int mHeight;
    private float cellHeigh;
    private String[] LETTERS;
    private OnLetterUpdateListener mOnLetterUpdateListener;

    public OnLetterUpdateListener getmOnLetterUpdateListener() {
        return mOnLetterUpdateListener;
    }

    public void setmOnLetterUpdateListener(OnLetterUpdateListener mOnLetterUpdateListener) {
        this.mOnLetterUpdateListener = mOnLetterUpdateListener;
    }

    public QuickIndexBar(Context context) {
        this(context, null);
    }

    public QuickIndexBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QuickIndexBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //抗锯齿属性
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(getResources().getColor(R.color.main));
        //文本加粗
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        paint.setTextSize(getResources().getDimensionPixelSize(R.dimen.dd_dimen_22px));
        doLetters(1);
    }

    public void doLetters(int i) {
        if(i == 1){
            LETTERS = LETTERS_1;
        }else {
            LETTERS = LETTERS_2;
        }
    }


    //数组准备
    private static final String[] LETTERS_2 = new String[]{
//            "当前", "历史", "热点",
            "定位", "历史",
            "A", "B", "C", "D",
            "E", "F", "G", "H",
            "I", "J", "K", "L",
            "M", "N", "O", "P",
            "Q", "R", "S", "T",
            "U", "V", "W", "X",
            "Y", "Z"};

    //数组准备
    private static final String[] LETTERS_1 = new String[]{
            "定位",
            "A", "B", "C", "D",
            "E", "F", "G", "H",
            "I", "J", "K", "L",
            "M", "N", "O", "P",
            "Q", "R", "S", "T",
            "U", "V", "W", "X",
            "Y", "Z"};

    @Override
    protected void onDraw(Canvas canvas) {
        //绘制字母
        for (int i = 0; i < LETTERS.length; i++) {
            String letter = LETTERS[i];
            float x = cellWidth / 2.0f - paint.measureText(letter) / 2.0f;

            Rect bounds = new Rect();//矩形
            paint.getTextBounds(letter, 0, letter.length(), bounds);//获取文本占用的矩形区域
            float y = cellHeigh / 2.0f + bounds.height() / 2.0f + i * cellHeigh;
            //画画
            canvas.drawText(letter, x, y, paint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //单元格宽，控件宽度
        cellWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        //单元格高度
        cellHeigh = (mHeight * 1.0f / LETTERS.length * 1.0f);
    }

    int current = -1;


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int index;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //根据按下的高度，判断按下的单元格
                index = (int) (event.getY() / cellHeigh);

                if (current != index) {
                    if (index >= 0 && index < LETTERS.length) {
                        //得到按下的字段
                        String letter = LETTERS[index];
                        //ToastUtil.showToast(getContext(), letter);//自定义单例吐司
                        if (mOnLetterUpdateListener != null) {
                            mOnLetterUpdateListener.onLetterUpdate(letter);
                        }
                        current = index;
                    }
                }

                break;
            case MotionEvent.ACTION_MOVE:
                //根据按下的高度，判断按下的单元格
                index = (int) (event.getY() / cellHeigh);

                if (current != index) {
                    if (index >= 0 && index < LETTERS.length) {
                        //得到按下的字段
                        String letter = LETTERS[index];
                        //ToastUtil.showToast(getContext(),letter);
                        if (mOnLetterUpdateListener != null) {
                            mOnLetterUpdateListener.onLetterUpdate(letter);
                        }
                        current = index;

                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                current = -1;
                break;
        }
        return true;
    }

    public interface OnLetterUpdateListener {
        //当新的字母被选中的时候调用
        void onLetterUpdate(String letter);
    }
}

