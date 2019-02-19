package com.quanyan.yhy.ui.discovery.viewhelper;

import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;

import com.quanyan.base.view.customview.NoScrollGridView;

import java.lang.reflect.Field;

/**
 * Created with Android Studio.
 * Title:GridViewUtils
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:zhaoxp
 * Date:2015-12-18
 * Time:17:41
 * Version 1.0
 */
public class GridViewUtils {

	static SparseIntArray mGvWidth = new SparseIntArray();

	public static void setGridViewHeightBasedOnChildren(int position, NoScrollGridView gridView) {
		// 获取GridView对应的Adapter
		ListAdapter listAdapter = gridView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int rows;
		int columns = 0;
		int horizontalBorderHeight = 0;
		Class<?> clazz = gridView.getClass();
		Class<?> gridView1 = clazz.getSuperclass();
		try {
			//利用反射，取得每行显示的个数
			Field column = gridView1.getDeclaredField("mRequestedNumColumns");
			gridView1.asSubclass(GridView.class);
			column.setAccessible(true);
			columns = (Integer) column.get(gridView);
			//利用反射，取得横向分割线高度
			Field horizontalSpacing = gridView1.getDeclaredField("mRequestedHorizontalSpacing");
			horizontalSpacing.setAccessible(true);
			horizontalBorderHeight = (Integer) horizontalSpacing.get(gridView);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		int height = mGvWidth.get(position);
		int totalHeight = 0;
		if (columns > 0) {
			//判断数据总数除以每行个数是否整除。不能整除代表有多余，需要加一行
			if (listAdapter.getCount() % columns > 0) {
				rows = listAdapter.getCount() / columns + 1;
			} else {
				rows = listAdapter.getCount() / columns;
			}
			for (int i = 0; i < rows; i++) { //只计算每项高度*行数
				View listItem = listAdapter.getView(i, null, gridView);
				listItem.measure(0, 0); // 计算子项View 的宽高
				totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
			}
			mGvWidth.put(position, totalHeight);
			ViewGroup.LayoutParams params = gridView.getLayoutParams();
			params.height = totalHeight + horizontalBorderHeight * (rows - 1);//最后加上分割线总高度
			gridView.setLayoutParams(params);
		}else{
			mGvWidth.put(position, 0);
			ViewGroup.LayoutParams params = gridView.getLayoutParams();
			params.height = 0;//最后加上分割线总高度
			gridView.setLayoutParams(params);
		}
	}
}
