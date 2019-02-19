package com.newyhy.activity;

import android.content.Context;
import android.support.v4.app.FragmentTransaction;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.newyhy.contract.presenter.CircleSearchPresenter;
import com.newyhy.fragment.circle.CircleSearchFragment;
import com.quanyan.yhy.R;
import com.quanyan.yhy.libanalysis.AnEvent;
import com.quanyan.yhy.libanalysis.Analysis;
import com.yhy.common.base.BaseNewActivity;
import com.yhy.location.LocationManager;
import com.yhy.router.RouterPath;

/**
 * 圈子搜索界面
 * Created by Jiervs on 2018/6/29.
 */

@Route(path = RouterPath.ACTIVITY_CIRCLE_SEARCH)
public class CircleSearchActivity extends BaseNewActivity {

    private CircleSearchFragment fragment;
    private EditText et_search;
    private TextView tv_cancel;
    private CircleSearchPresenter mPresenter;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_circle_search;
    }

    @Override
    protected void initVars() {
        super.initVars();
        fragment = new CircleSearchFragment();
        mPresenter = new CircleSearchPresenter(this, fragment);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_content, fragment);
        transaction.commit();
    }

    @Override
    protected void initView() {
        super.initView();
        mImmersionBar.fitsSystemWindows(true).transparentStatusBar().statusBarDarkFont(true).init();

        et_search = findViewById(R.id.et_search);
        tv_cancel = findViewById(R.id.tv_cancel);
        et_search.postDelayed(() -> {
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputManager != null) {
                inputManager.showSoftInput(et_search, 0);
            }
        }, 500);
        et_search.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                Analysis.pushEvent(CircleSearchActivity.this, AnEvent.SearchConfirm,
                        new Analysis.Builder().
                                setLat(LocationManager.getInstance().getStorage().getLast_lat()).
                                setLng(LocationManager.getInstance().getStorage().getLast_lng()).
                                setContent(et_search.getText().toString().trim()));

                startSearch();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(et_search.getWindowToken(), 0);
                }
                return true;
            }
            return false;
        });

        tv_cancel.setOnClickListener(v -> finish());
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void startSearch() {
        fragment.search(et_search.getText().toString().trim());
    }

}
