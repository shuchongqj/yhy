package com.quanyan.yhy.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.quanyan.yhy.R;
import com.yhy.common.beans.hotel.CircleInfo;
import com.yhy.common.beans.hotel.CutInfo;

import java.util.List;


@SuppressLint("ClickableViewAccessibility")
public class CutSelectView extends View {

    private String TAG = CutSelectView.class.getSimpleName();
    /**
     * 每节直接的间隙宽度
     */
    private float GAP_SIZE = 0;

    private int SELECT_COLOR = Color.parseColor("#990000FF");

    private int DEFAULT_COLOR = Color.parseColor("#88000000");

    private float PROGRESS_HEIGHT = 12;
    private Paint mPaint;
    private int viewWidth;
    private int viewHeight;
    private List<CutInfo> mCutInfos;
    private CircleInfo mCircleInfo;
    private float mPadding;
    private float progress_width;

    public int circle1Position = 0;
    public int circle2Position = 0;
    private boolean circle1IsMoving = false;
    private boolean circle2IsMoving = false;
    public float circle1X = 0;
    public float circle2X = 0;

    public boolean isFirstDraw = true;
    public boolean isTag = false;
    /* ==========================================================================*/
    private OnSelectChangeLinener mOnSelectChangeLinener;

    public CutSelectView(Context context) {
        this(context, null);
    }

    public CutSelectView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CutSelectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        viewWidth = getWidth();
        viewHeight = getHeight();

        mPaint.setAntiAlias(true);// 消除锯齿
        initData();
        mDifference=calculateTextX0(1)-calculateTextX0(0);
//		for (int i = 0; i < mCutInfos.size(); i++) {//画文字
//			if(i==0||i==mCutInfos.size()-1){
//				drawText(canvas, i);
//			}
//
//		}

        mPaint.setColor(DEFAULT_COLOR);
        float y = viewHeight - mCircleInfo.getCircleSize();
        mPaint.setStrokeWidth(PROGRESS_HEIGHT);
        canvas.drawLine(calculateTextX0(0), y, calculateTextX0(mCutInfos.size() - 1), y, mPaint);//画底线

        mPaint.setColor(this.getResources().getColor(R.color.ac_title_bg_color));
        //初始化
        if (isFirstDraw || circle1X == 0.0 && circle2X == 0.0) {//画选中线条
            if(isTag){
                canvas.drawLine(calculateTextX0(circle1Position), y, calculateTextX0(circle2Position), y, mPaint);
                isTag=false;
            }else{
                canvas.drawLine(calculateTextX0(0), y, calculateTextX0(mCutInfos.size() - 1), y, mPaint);
            }

        } else {
//            if(isTag){
//                canvas.drawLine(circle1X, y, circle2X, y, mPaint);
//                isTag=false;
//            }else{
//                canvas.drawLine(calculateTextX0(0), y, calculateTextX0(mCutInfos.size() - 1), y, mPaint);
//            }
            canvas.drawLine(circle1X, y, circle2X, y, mPaint);
        }
//        canvas.drawLine(circle1X, y, circle2X, y, mPaint);
        if (circle1IsMoving) {//画左边圆
            drawCircle(canvas, circle1X);
        } else {
            drawCircle(canvas, circle1Position);
        }


        if (circle2IsMoving) {//画右边圆
            drawCircle(canvas, circle2X);
        } else {
            drawCircle(canvas, circle2Position);
        }
        if (isFirstDraw) isFirstDraw = false;
    }
    /**
     * 设置选中位置
     *
     */
    public void setClickPosition(int clickPositionX0,int clickPositionX1) {
        circle1Position=clickPositionX0;
        circle2Position=clickPositionX1;
        circle1X=calculateTextX0(circle1Position);
        circle2X=calculateTextX0(circle2Position-1);
        invalidate();

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                unifyXandPosition();
                circle1IsMoving = false;
                float r1 = (float) (Math.pow(
                        (event.getX() - getCirclePosition(circle1Position).x), 2) + Math
                        .pow((event.getY() - getCirclePosition(circle1Position).y),
                                2));
                if (r1 < Math.pow(mCircleInfo.getCircleSize(), 2)) {
                    circle1IsMoving = true;
                    return true;
                }

                circle2IsMoving = false;
                float r2 = (float) (Math.pow(
                        (event.getX() - getCirclePosition(circle2Position).x), 2) + Math
                        .pow((event.getY() - getCirclePosition(circle2Position).y),
                                2));
                if (r2 < Math.pow(mCircleInfo.getCircleSize(), 2)) {
                    circle2IsMoving = true;
                    return true;
                }
                break;

            case MotionEvent.ACTION_MOVE:

                if (circle1IsMoving) {

                    circle1X = event.getX();
                    if (circle1X < calculateTextX0(0)) {
                        circle1X = calculateTextX0(0);
                    }
                    if (circle1X >= calculateTextX0(circle2Position)-mDifference) {
                        circle1X = calculateTextX0(circle2Position) -mDifference ;
                    }
                    invalidate();
                }
                if (circle2IsMoving) {
                    circle2X = event.getX();
                    if (circle2X <= calculateTextX0(circle1Position)+mDifference ) {
                        circle2X = calculateTextX0(circle1Position) +mDifference ;
                    }
                    if (circle2X > calculateTextX0(mCutInfos.size() - 1)) {
                        circle2X = calculateTextX0(mCutInfos.size() - 1);
                    }
                    invalidate();
                }

                break;
            case MotionEvent.ACTION_UP:
                circle1IsMoving = false;
                circle2IsMoving = false;

                circle1Position = getRecentlyPosition(circle1X);
                circle2Position = getRecentlyPosition(circle2X);

                unifyXandPosition();

                invalidate();

                mOnSelectChangeLinener.OnSelectChange(circle1Position, circle2Position);
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 获取当前最近的position
     *
     * @return
     */
    private int getRecentlyPosition(float clickX) {
        float r = viewWidth;
        int position = 0;
        for (int i = 0; i < mCutInfos.size(); i++) {
            Log.i(TAG, "r:" + r + "-----Math.abs(clickX-calculateTextX0(i)):"
                    + Math.abs(clickX - calculateTextX0(i)));
            if (r > Math.abs(clickX - calculateTextX0(i))) {
                r = Math.abs(clickX - calculateTextX0(i));
                position = i;
            }
        }
        return position;
    }

    /**
     * 获取圆心位置
     *
     * @param position
     * @return
     */
    private PointF getCirclePosition(int position) {
        return new PointF(calculateTextX0(position), viewHeight
                - mCircleInfo.getCircleSize());
    }

    /**
     * 画圆
     *
     * @param canvas
     */
    private void drawCircle(Canvas canvas, int position) {
        mPaint.setColor(mCircleInfo.getAnnulusColor());
        canvas.drawCircle(calculateTextX0(position),
                viewHeight - mCircleInfo.getCircleSize(),
                mCircleInfo.getCircleSize(), mPaint);

        mPaint.setColor(mCircleInfo.getCircleColor());
        canvas.drawCircle(calculateTextX0(position),
                viewHeight - mCircleInfo.getCircleSize(),
                mCircleInfo.getCircleSize() - 1, mPaint);
    }

//	private void drawLine(Canvas canvas, int i,int color) {
//		mPaint.setColor(color);
//		float y = viewHeight - mCircleInfo.getCircleSize();
//		mPaint.setStrokeWidth(PROGRESS_HEIGHT);
//		canvas.drawLine(calculateTextX0(i) + GAP_SIZE, y, calculateTextX0(i)
//				+ progress_width, y, mPaint);
//	}

    /**
     * 画圆
     *
     * @param canvas
     */
    private void drawCircle(Canvas canvas, float x) {
        mPaint.setColor(mCircleInfo.getAnnulusColor());
        canvas.drawCircle(x, viewHeight - mCircleInfo.getCircleSize(),
                mCircleInfo.getCircleSize(), mPaint);
        mPaint.setColor(mCircleInfo.getCircleColor());
        canvas.drawCircle(x, viewHeight - mCircleInfo.getCircleSize(),
                mCircleInfo.getCircleSize() - 1, mPaint);
    }

    /**
     * 画文字
     */
    private void drawText(Canvas canvas, int position) {
        mPaint.setTextSize(mCutInfos.get(position).getMsgSize());
        mPaint.setColor(mCutInfos.get(position).getMsgColor());
        canvas.drawText(mCutInfos.get(position).getCutMsg(),
                calculateTextX0(position) - measure(position) / 2, mCutInfos
                        .get(position).getMsgSize(), mPaint);
    }

    /**
     * 初始化大小数据
     */
    public void initData() {
        if (mCutInfos.size() < 1) {
            return;
        }

        // 比较 第一段字宽度/2 最后一段字宽度/2 和圆半径的大小 那最大值作为mPadding
        if (mCircleInfo.getCircleSize() > measure(0) / 2) {
            mPadding = mCircleInfo.getCircleSize();
        } else {
            mPadding = measure(0) / 2;
        }
        if (mPadding < measure(mCutInfos.size() - 1) / 2)
            mPadding = measure(mCutInfos.size() - 1) / 2;
        // 控件宽度 -圆的直径（左右一边一半）-所有间隙 /段数
        progress_width = (viewWidth - 2 * mPadding - GAP_SIZE
                * mCutInfos.size())
                / (mCutInfos.size() - 1);
        if (progress_width < 0) {
            progress_width = 0;
        }
    }

    /**
     * 设置节点信息
     *
     * @param catInfos
     */
    float  mDifference;
    public void setCutInfo(List<CutInfo> catInfos) {
        this.mCutInfos = catInfos;
        circle2Position = catInfos.size() - 1;
    }

    /**
     * 设置圆球信息
     */
    public void setCircleInfo(CircleInfo circleInfo) {
        this.mCircleInfo = circleInfo;
    }

    /**
     * 测量字体宽度
     *
     * @param position
     * @return
     */
    private float measure(int position) {
        mPaint.setTextSize(mCutInfos.get(position).getMsgSize());
        return mPaint.measureText(mCutInfos.get(position).getCutMsg());
    }

    /**
     * 每段起点的x坐标
     *
     * @param position
     * @return
     */
    private float calculateTextX0(int position) {
        return progress_width * position + mPadding + GAP_SIZE * position;
    }

    /**
     * 校准x位置 校准的目的是为了让位置position 和位置x统一起来
     */
    private void unifyXandPosition() {
        circle1X = getCirclePosition(circle1Position).x;
        circle2X = getCirclePosition(circle2Position).x;
    }
    /**
     * 还原view到最初
     */
    public void initializationView() {
        isFirstDraw = true;
        circle1X = 0;
        circle2X = 0;
        circle1Position = 0;
        circle2Position = 0;
        setCutInfo(mCutInfos);
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height;
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            int desired = (int) (getPaddingLeft() + getWidth() + getPaddingRight());
            width = desired;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            int desired = (int) (getPaddingTop() + getWidth() + getPaddingBottom());
            height = desired;
        }

        setMeasuredDimension(width, height);
    }

    public void setOnSelectChangeLinener(OnSelectChangeLinener onSelectChangeLinener) {
        this.mOnSelectChangeLinener = onSelectChangeLinener;
    }

    public interface OnSelectChangeLinener {
        public void OnSelectChange(int selectL, int selectR);
    }

}
