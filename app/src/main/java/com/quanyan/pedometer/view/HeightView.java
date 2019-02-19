package com.quanyan.pedometer.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.quanyan.pedometer.utils.Tools;
import com.quanyan.yhy.R;


public class HeightView extends View implements OnTouchListener {

	private int MAX_DISPLAY_LENGTH;

	private static final int END_DISPLAY_POS = 190;//(225-35)范围
    private int MAX_HEIGHT;
    private int MIN_HEIGHT;
    private boolean isFirst = true;

    private int viewWidth;
    private int viewHeight;

    private int INTERVAL_RULER = 10;//一厘米的高度
    private int SHORT_LINE = 20;//短刻度
    private int LONG_LINE = 30;//长刻度
	//刻度margin
    private int MARGIN_RIGHT = 10;//字体margin left
    private int MARGIN_LEFT = 0;

    //线的粗细
	private int STROKE_LINE_WIDTH = 3;
	
	private Paint paint = null;
	private Paint gra_paint = null;
	private Paint cur_paint = null;
	private float maxHeightPX;
	private float minHeightPX;

	private float slider_pxt = 0;
	private int slider_px = 190;
	private float slider_len = 0;
	private float slider_l = 0;
	private float slider_start_y = 0;
	private float slider_move_y = 0;

    private int defaultHeight = 175;

	private static final int TEXT_SIZE = 15;
	private OnHeightPickListener mOnHeightPickListener;
	private Context mContext;

	public HeightView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	public HeightView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public HeightView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		mContext = context;
		
		initPaint();

		INTERVAL_RULER = Tools.dip2px(mContext, INTERVAL_RULER);
		SHORT_LINE = Tools.dip2px(mContext, SHORT_LINE);
		LONG_LINE = Tools.dip2px(mContext, LONG_LINE);
        MARGIN_RIGHT = Tools.dip2px(mContext, MARGIN_RIGHT);
				
		setOnTouchListener(this);
	}
	
	private void initPaint(){
		paint = new Paint();
		gra_paint = new Paint();
		cur_paint = new Paint();
		
		paint.setAntiAlias(true);
		paint.setColor(mContext.getResources().getColor(R.color.line_color));
		paint.setStrokeWidth(STROKE_LINE_WIDTH);

		gra_paint.setColor(mContext.getResources().getColor(R.color.line_color));
		gra_paint.setAntiAlias(true);
		gra_paint.setTextSize(Tools.dip2px(mContext, TEXT_SIZE));
        gra_paint.setStrokeWidth(STROKE_LINE_WIDTH);

		cur_paint.setAntiAlias(true);
		cur_paint.setColor(Color.RED);
		cur_paint.setStrokeWidth(STROKE_LINE_WIDTH);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

        viewWidth = getWidth();
        viewHeight = getHeight();
        MARGIN_LEFT = viewWidth -LONG_LINE - MARGIN_RIGHT;
        MAX_DISPLAY_LENGTH = (int)viewHeight / INTERVAL_RULER;
        MAX_HEIGHT = 225 + (int)MAX_DISPLAY_LENGTH / 2;
        MIN_HEIGHT = 35 - (int)MAX_DISPLAY_LENGTH / 2;

        maxHeightPX = MAX_HEIGHT * INTERVAL_RULER;
        minHeightPX = MIN_HEIGHT * INTERVAL_RULER;


		// 初始化绘制
        drawRuler(canvas);

		drawRedMark(canvas);
	}

	public void drawRuler(Canvas canvas) {

        if (isFirst){
            //default
//            slider_len = maxHeightPX - END_DISPLAY_POS * INTERVAL_RULER;
            //default height from user
            slider_len = (225-defaultHeight)*INTERVAL_RULER;
            slider_start_y = END_DISPLAY_POS * INTERVAL_RULER;
            slider_l = slider_len;
            isFirst = false;
        }

		if (slider_len > 0) {
			slider_px = (int) (slider_len / INTERVAL_RULER);
		} else {
			slider_px = 0;
		}
		slider_pxt = slider_len - slider_px * INTERVAL_RULER;
		for (int i = MAX_DISPLAY_LENGTH; i > 0; i--) {
			int displayRuler = MAX_HEIGHT - (i + slider_px); 
			if(displayRuler > 225 || displayRuler < 35){
				continue;	
			}
			if (displayRuler % 5 == 0) {
				canvas.drawLine(MARGIN_LEFT, slider_pxt + INTERVAL_RULER * i, MARGIN_LEFT + LONG_LINE, slider_pxt
						+ INTERVAL_RULER * i, paint);
				
				String rulerText = String.valueOf(displayRuler);
				FontMetrics fm = gra_paint.getFontMetrics();
				float textHeight = (float)Math.ceil(fm.descent - fm.ascent); 
				
				canvas.drawText(rulerText,MARGIN_RIGHT,
						slider_pxt + INTERVAL_RULER * i + textHeight / 3, gra_paint);
			} else {
				canvas.drawLine(MARGIN_LEFT + (LONG_LINE - SHORT_LINE), slider_pxt + INTERVAL_RULER * i, MARGIN_LEFT + LONG_LINE, slider_pxt
						+ INTERVAL_RULER * i, paint);
			}
		}
	}

    public void setDefaultHeight(int defaultHeight) {
        this.defaultHeight = defaultHeight;
    }

	private void drawRedMark(Canvas canvas) {
		float currentheight = (MAX_HEIGHT - (slider_len/INTERVAL_RULER) - MAX_DISPLAY_LENGTH / 2);
		canvas.drawLine(MARGIN_LEFT, MAX_DISPLAY_LENGTH * INTERVAL_RULER / 2 , MARGIN_LEFT + LONG_LINE, MAX_DISPLAY_LENGTH * INTERVAL_RULER / 2 , cur_paint);
		if(mOnHeightPickListener != null){
			mOnHeightPickListener.onPick((int) currentheight);
		}
	}

	public void setOnHeightPickListener(OnHeightPickListener lsn){
		mOnHeightPickListener = lsn;
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			slider_start_y = event.getY(0);
			invalidate();
		}else if (event.getAction() == MotionEvent.ACTION_MOVE) {
			slider_move_y = event.getY(0);
			slider_len = slider_l + slider_start_y - slider_move_y;
			if (slider_len < 0) {
				slider_len = 0;
			}else if(slider_len > END_DISPLAY_POS * INTERVAL_RULER){
				slider_len = END_DISPLAY_POS * INTERVAL_RULER;
			}
			slider_len = (int) (slider_len / INTERVAL_RULER) * INTERVAL_RULER;
			invalidate();
		}else if (event.getAction() == MotionEvent.ACTION_UP) {
			slider_l = slider_len;
		}

		return true;
	}

	public interface OnHeightPickListener {
	    void onPick(int height);
	}
}
