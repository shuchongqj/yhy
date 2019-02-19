package com.newyhy.adapter.live;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;


public class LiveChatAndAnchorInfoAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> fragments;
    private String[] tab;

    public LiveChatAndAnchorInfoAdapter(FragmentManager fm, ArrayList<Fragment> fragments, String[] tab) {
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
        super.destroyItem(container, position, object);
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        try{
            super.finishUpdate(container);
        } catch (NullPointerException nullPointerException){

        }
    }
}
