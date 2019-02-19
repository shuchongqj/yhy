package com.newyhy.activity;


import android.os.Message;
import android.support.v4.app.FragmentTransaction;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.newyhy.contract.presenter.CircleLivePresenter;
import com.newyhy.fragment.circle.CircleLiveFragment;
import com.quanyan.base.view.BaseNavView;
import com.quanyan.yhy.R;
import com.quanyan.yhy.libanalysis.AnArgs;
import com.quanyan.yhy.libanalysis.Analysis;
import com.yhy.common.base.BaseNewActivity;
import com.yhy.router.RouterPath;
import com.yhy.router.YhyRouter;

/**
 * 直播列表
 * Created by nandy on 2018/7/7
 */
@Route(path = RouterPath.ACTIVITY_LIVE_LIST)
public class LiveListActivity extends BaseNewActivity {
    // 标题
    private BaseNavView mBaseNavView;
    private CircleLiveFragment circleLiveFragment;//视频
    private CircleLivePresenter circleLivePresenter;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_live_list;
    }

    @Override
    protected void initVars() {
        super.initVars();
    }

    @Override
    protected void initView() {
        super.initView();
        mImmersionBar.fitsSystemWindows(true).transparentStatusBar().statusBarDarkFont(true).init();
        mBaseNavView = findViewById(R.id.title_view);
        mBaseNavView.setTitleText("直播视频");
//        circleLiveFragment = (CircleLiveFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_content);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        circleLiveFragment = (CircleLiveFragment) YhyRouter.getInstance().makeCircleLiveFragment(mContext,
                AnArgs.Instance()
                        .build(Analysis.TAB, "主场直播视频")
                        .getMap());
        transaction.add(R.id.content, circleLiveFragment);
        transaction.commit();

        circleLivePresenter = new CircleLivePresenter(this, circleLiveFragment, true);
    }

    @Override
    protected void setListener() {
        super.setListener();
        mBaseNavView.setLeftClick((v) -> finish());
    }

    @Override
    protected void initData() {
        super.initData();
    }


    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        circleLivePresenter.release();
        circleLivePresenter = null;
    }

}
