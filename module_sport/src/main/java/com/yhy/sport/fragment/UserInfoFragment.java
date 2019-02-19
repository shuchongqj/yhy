package com.yhy.sport.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.yhy.module_sport.R;

/**
 * Created by Nandy on 2018/7/6
 */
public class UserInfoFragment extends SportBaseFragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void init(View view) {
        tabLayout = view.findViewById(R.id.tablayout);
        viewPager = view.findViewById(R.id.viewpager);
    }

    @Override
    protected void getData() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_main_layout;
    }
}
