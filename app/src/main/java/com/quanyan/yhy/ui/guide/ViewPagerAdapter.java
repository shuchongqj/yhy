package com.quanyan.yhy.ui.guide;

import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {
	// 界面列表
	private List<View> views;
	private GuideViewPager mGuideViewPager;
	public ViewPagerAdapter(List<View> views, GuideViewPager guideViewPager) {
		this.views = views;
		this.mGuideViewPager = guideViewPager;
	}

	// 销毁arg1位置的界面
	@Override
	public void destroyItem(@NonNull View arg0, int arg1, @NonNull Object arg2) {
		((ViewPager) arg0).removeView(views.get(arg1));
	}

	@Override
	public void finishUpdate(@NonNull View arg0) {
	}

	// 获得当前界面数
	@Override
	public int getCount() {
		if (views != null) {
			return views.size();
		}
		return 0;
	}
	
	// 初始化arg1位置的界面
	@Override
	public Object instantiateItem(@NonNull ViewGroup container, final int position) {
		((ViewPager) container).addView(views.get(position), 0);
		if (position == views.size() - 1) {
			
		}
		
//		mGuideViewPager.setObjectForPosition(views.get(position), position);
		return views.get(position);
	}
	
	// 判断是否由对象生成界面
	@Override
	public boolean isViewFromObject(@NonNull View arg0, @NonNull Object arg1) {
		return (arg0 == arg1);
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void startUpdate(@NonNull View arg0) {
	}

	@Override
	public void finishUpdate(@NonNull ViewGroup container) {
		try{
			super.finishUpdate(container);
		} catch (NullPointerException nullPointerException){

		}
	}

}
