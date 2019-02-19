package com.quanyan.yhy.ui.guide;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import java.util.HashMap;
import java.util.LinkedHashMap;

@SuppressLint("NewApi")
public class GuideViewPager extends ViewPager {
	private State mState;
	private int oldPage;

	private View mLeft;
	private View mRight;
	private float mRot;
	private float mTrans;
	private float mScale;
	private HashMap<Integer, Object> mObjs = new LinkedHashMap<Integer, Object>();
	private enum State {
		IDLE,
		GOING_LEFT,
		GOING_RIGHT
	}
	
	private static final float ROT_MAX = 30.0f;
	
	private static final boolean API_11;
	static {
		API_11 = Build.VERSION.SDK_INT >= 11;
	}
	
	public GuideViewPager(Context context) {
		this(context, null);
	}

	public GuideViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		setClipChildren(false);
	}
	
	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		if (mState == State.IDLE && positionOffset > 0) {
			oldPage = getCurrentItem();
			mState = position == oldPage ? State.GOING_RIGHT : State.GOING_LEFT;
		}
		boolean goingRight = position == oldPage;				
		if (mState == State.GOING_RIGHT && !goingRight)
			mState = State.GOING_LEFT;
		else if (mState == State.GOING_LEFT && goingRight)
			mState = State.GOING_RIGHT;

		float effectOffset = isSmall(positionOffset) ? 0 : positionOffset;
		
//		mLeft = getChildAt(position);
//		mRight = getChildAt(position+1);
		mLeft = findViewFromObject(position);
		mRight = findViewFromObject(position+1);
		
		super.onPageScrolled(position, positionOffset, positionOffsetPixels);

		if (effectOffset == 0) {
			disableHardwareLayer();
			mState = State.IDLE;
		}
	}
	
	private boolean isSmall(float positionOffset) {
		return Math.abs(positionOffset) < 0.0001;
	}
	
	public View findViewFromObject(int position) {
		Object o = mObjs.get(Integer.valueOf(position));
		if (o == null) {
			return null;
		}
		PagerAdapter a = getAdapter();
		View v;
		for (int i = 0; i < getChildCount(); i++) {
			v = getChildAt(i);
			if (a.isViewFromObject(v, o))
				return v;
		}
		return null;
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void manageLayer(View v, boolean enableHardware) {
		if (!API_11) return;
		int layerType = enableHardware ? View.LAYER_TYPE_HARDWARE : View.LAYER_TYPE_NONE;
		if (layerType != v.getLayerType())
			v.setLayerType(layerType, null);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void disableHardwareLayer() {
		if (!API_11) return;
		View v;
		for (int i = 0; i < getChildCount(); i++) {
			v = getChildAt(i);
			if (v.getLayerType() != View.LAYER_TYPE_NONE)
				v.setLayerType(View.LAYER_TYPE_NONE, null);
		}
	}
	
	
	public void setObjectForPosition(Object obj, int position) {
		mObjs.put(Integer.valueOf(position), obj);
	}
}
