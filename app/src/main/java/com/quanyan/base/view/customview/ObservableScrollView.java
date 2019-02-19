package com.quanyan.base.view.customview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

/**
 * 监听ScrollView的滑动事件
 *
 * Created by zhaoxp on 2015-11-19.
 */
public class ObservableScrollView extends ScrollView {

	private ScrollViewListener scrollViewListener;
	public ObservableScrollView(Context context) {
		super(context);
		init(context);
	}

	public ObservableScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);

	}

	public ObservableScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);

	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public ObservableScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init(context);
	}

	private void init(Context context) {
		setOverScrollMode(View.OVER_SCROLL_NEVER);
	}

	@Override
	protected void onScrollChanged(int x, int y, int oldx, int oldy) {
		super.onScrollChanged(x, y, oldx, oldy);
		if (scrollViewListener != null) {
			scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
		}
		setOverScrollMode(View.OVER_SCROLL_NEVER);
	}

	public void setScrollViewListener(ScrollViewListener scrollViewListener) {
		this.scrollViewListener = scrollViewListener;
	}

	public interface ScrollViewListener {

		void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy);

	}
}
