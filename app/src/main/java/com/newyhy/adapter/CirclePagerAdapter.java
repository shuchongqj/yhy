package com.newyhy.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * 圈子 Viewpager 适配器
 * Created by Jiervs on 2018/6/15.
 */

public class CirclePagerAdapter extends FragmentPagerAdapter{

    private ArrayList<Fragment> fragments;

    private String[] tab;

    public CirclePagerAdapter(FragmentManager fm,ArrayList<Fragment> fragments,String[] tab) {
        super(fm);
        this.fragments = fragments;
        this.tab = tab;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tab[position];
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //super.destroyItem(container, position, object);
    }

    /*@Override
    public void finishUpdate(ViewGroup container) {
        try{
            super.finishUpdate(container);
        } catch (NullPointerException nullPointerException){

        }
    }*/

}
