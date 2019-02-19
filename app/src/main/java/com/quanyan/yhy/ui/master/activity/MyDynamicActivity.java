package com.quanyan.yhy.ui.master.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.newyhy.fragment.UgcFragment;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.view.BaseNavView;
import com.quanyan.yhy.R;
import com.yhy.common.utils.SPUtils;
import com.yhy.service.IUserService;

/**
 * 我的动态列表
 * Created by user on 2018/3/7.
 */

public class MyDynamicActivity extends BaseActivity{

    private BaseNavView baseNavView;
    private UgcFragment fragment;

    @Autowired
    IUserService userService;

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.ac_my_dynamic, null);
    }

    @Override
    public View onLoadNavView() {
        baseNavView = new BaseNavView(this);
        baseNavView.setTitleText(R.string.my_dynamic);
        return baseNavView;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }




    @Override
    protected void initView(Bundle savedInstanceState) {
        baseNavView = new BaseNavView(this);
        if (fragment == null) {
            fragment = UgcFragment.newInstance(4, userService.getLoginUserId());
            FragmentTransaction transaction =  getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fl_content, fragment);
            transaction.commit();
        }
    }
}
