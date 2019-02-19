/**   
 * @Title: AKCirclePageAdapter.java 
 * @Package: com.pingan.papd.utils 
 * @Description: TODO
 * @author xiezhidong@pajk.cn  
 * @date 2014-8-28 下午5:36:41 
 */

package com.quanyan.yhy.ui.base.views.banner;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public abstract class AKCirclePageAdapter<T> extends PagerAdapter {
	protected ArrayList<T> mData = new ArrayList();
	protected LayoutInflater mInflater;
	protected Context mContext;

	private boolean isInfiniteLoop;

	public AKCirclePageAdapter(Context c) {
		mContext = c;
		mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		isInfiniteLoop = false;
	}

	@Override
	public abstract Object instantiateItem(ViewGroup container, int position);

	public void addItem(final T item) {
		mData.add(item);
		notifyDataSetChanged();
	}

	public void addItem(int idx, final T item) {
		mData.add(idx, item);
		notifyDataSetChanged();
	}

	public void clearItems() {
		mData.clear();
		notifyDataSetChanged();
	}

	public T getItem(int position) {
		return mData.get(getPosition(position));
	}

	public ArrayList<T> getData() {
		return mData;
	}

	@Override
	public int getCount() {
		// Infinite loop
		if (mData == null || mData.size() < 1) {
			return 0;
		} else if (mData.size() == 1) {
			return 1;
		}
		return isInfiniteLoop ? Integer.MAX_VALUE : mData.size();
	}

	/**
	 * get really position
	 * 
	 * @param position
	 * @return
	 */
	public int getPosition(int position) {
		if (mData == null || mData.size() < 1) {
			return 0;
		} else if (mData.size() == 1) {
			return 0;
		}
		return isInfiniteLoop ? position % mData.size() : position;
	}

	public AKCirclePageAdapter<T> setInfiniteLoop(boolean isInfiniteLoop) {
		this.isInfiniteLoop = isInfiniteLoop;
		return this;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		if (object instanceof View) {
			container.removeView((View) object);
		}
	}

}
