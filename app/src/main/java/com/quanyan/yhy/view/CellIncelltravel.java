package com.quanyan.yhy.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quanyan.base.util.ScreenSize;
import com.quanyan.yhy.R;

/**
 * 游记列表布局
 *
 * Created by zhaoxp on 2015-11-3.
 */
public class CellIncelltravel extends LinearLayout {

	private ImageView img_clc_travel;
	private TextView tv_clc_travel;
//	private LabelLayout mLayout;
	private View mBottomView;
	private MarginLayoutParams mMarginLayoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
	public CellIncelltravel(Context context) {
		super(context);
		init(context);
	}

	public CellIncelltravel(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public CellIncelltravel(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	@SuppressLint("NewApi")
	public CellIncelltravel(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init(context);
	}

	/**
	 * 初始化
	 * @param c context对象
	 */
	public void init(Context c) {
		LayoutInflater.from(c).inflate(R.layout.cell_in_travel_item, this);
		img_clc_travel = (ImageView) findViewById(R.id.img_clc_travel);
		tv_clc_travel = (TextView) findViewById(R.id.tv_clc_travel);
		mBottomView = findViewById(R.id.cell_in_travel_bottom);
//		mLayout = (LabelLayout) findViewById(R.id.tv_clc_travel_layout);

		mMarginLayoutParams.leftMargin = ScreenSize.convertDIP2PX(c.getApplicationContext(), 5);
		mMarginLayoutParams.rightMargin = ScreenSize.convertDIP2PX(c.getApplicationContext(), 5);
		mMarginLayoutParams.topMargin = ScreenSize.convertDIP2PX(c.getApplicationContext(), 3);
	}

	/**
	 * 设置底部View隐藏
	 */
	public void setBottomViewinVisible(){
		mBottomView.setVisibility(View.GONE);
	}

	/**
	 * 设置内容
	 * @param title 内容，标题
	 */
	public void setTitle(String title) {
		tv_clc_travel.setText(TextUtils.isEmpty(title) ? "" : title);
	}

	/**
	 * set the {@link SpannableStringBuilder} to the {@link TextView}
	 * @param stringBuilder
	 */
	public void setTextSpnnable(SpannableStringBuilder stringBuilder){
		tv_clc_travel.setVisibility(View.VISIBLE);
		tv_clc_travel.setMovementMethod(LinkMovementMethod.getInstance());
		tv_clc_travel.setText(stringBuilder);
	}

	/**
	 * 设置文本颜色
	 * @param color 色值
	 */
	public void setTitleTextColor(int color) {
		tv_clc_travel.setTextColor(color);
	}

	/**
	 * 设置左侧图标内容
	 * @param icon 图片资源ID
	 */
	public void setIcon(int icon) {
		img_clc_travel.setBackgroundResource(icon);
	}

	/**
	 * 移除所有的子View
	 */
//	public void removeAllViews(){
//		mLayout.removeAllViews();
//	}

	/**
	 * 添加子布局
	 *
	 * @param view 视图
	 * @param listener 点击事件
	 */
	public void addText(TextView view, OnClickListener listener){
//		view.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
//		mLayout.addView(view, mMarginLayoutParams);
//		if(listener != null) {
//			view.setOnClickListener(listener);
//		}
	}
}
