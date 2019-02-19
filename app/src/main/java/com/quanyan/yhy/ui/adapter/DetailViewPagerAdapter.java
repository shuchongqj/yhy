package com.quanyan.yhy.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * <p>https://developer.android.com/reference/android/support/v4/app/FragmentStatePagerAdapter.html</p>
 * <p>中有句话：When using FragmentPagerAdapter the host ViewPager must have a valid ID set.</p>
 * <p>否则使用new关键字创建的{@link android.support.v4.view.ViewPager}会报错android.content.res.Resources$NotFoundException: Unable to find resource ID #0xffffffff</p>
 * Created by zhaoxp on 2015-11-21.
 */
public class DetailViewPagerAdapter extends FragmentStatePagerAdapter {

	private FragmentManager mFragmentManager;
	private ArrayList<Fragment> mFragments = new ArrayList<>();
	private ArrayList<String> mTitles;

	public DetailViewPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
		super(fm);
		this.mFragmentManager = fm;
		this.mFragments = fragments;
	}

	public DetailViewPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments, ArrayList<String> titles) {
		super(fm);
		this.mFragmentManager = fm;
		this.mFragments = fragments;
		this.mTitles = titles;
	}

	@Override
	public Fragment getItem(int position) {
		return mFragments.get(position);
	}

	@Override
	public int getCount() {
		return mFragments.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		if(mTitles != null && mTitles.size() > 0 && mTitles.size() == mFragments.size()){
			return mTitles.get(position);
		}
		return super.getPageTitle(position);
	}

	public void setTitles(ArrayList<String> titles) {
		if(mTitles != null){
			mTitles.clear();
			mTitles.addAll(titles);
		}else {
			mTitles = titles;
		}
		notifyDataSetChanged();
	}
}