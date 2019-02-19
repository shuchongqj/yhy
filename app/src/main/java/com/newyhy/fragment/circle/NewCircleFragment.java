package com.newyhy.fragment.circle;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.androidkun.xtablayout.XTabLayout;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.newyhy.activity.CircleSearchActivity;
import com.newyhy.activity.HomeMainTabActivity;
import com.newyhy.activity.VideoPlayer;
import com.newyhy.adapter.CirclePagerAdapter;
import com.newyhy.cache.circle.SPCache;
import com.newyhy.contract.presenter.CircleCoffeeVideoPresenter;
import com.newyhy.contract.presenter.CircleDynamicPresenter;
import com.newyhy.contract.presenter.CircleFollowPresenter;
import com.newyhy.contract.presenter.CircleLivePresenter;
import com.newyhy.contract.presenter.CircleRecommendPresenter;
import com.newyhy.contract.presenter.CircleStandardVideoPresenter;
import com.quanyan.yhy.R;
import com.quanyan.yhy.eventbus.EvBusMessageCount;
import com.quanyan.yhy.libanalysis.AnArgs;
import com.quanyan.yhy.libanalysis.AnEvent;
import com.quanyan.yhy.libanalysis.Analysis;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.discovery.AddLiveAcitivty;
import com.yhy.common.base.BaseNewFragment;
import com.yhy.common.base.BasePresenter;
import com.yhy.common.constants.IntentConstants;
import com.yhy.common.eventbus.event.EvBusCircleTabRefresh;
import com.yhy.common.utils.JSONUtils;
import com.yhy.imageloader.ImageLoadManager;
import com.yhy.location.LocationManager;
import com.yhy.network.resp.snscenter.GetTagInfoListByTypeResp;
import com.yhy.router.RouterPath;
import com.yhy.router.YhyRouter;
import com.yhy.service.IUserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 大改版版圈子
 * Created by Jiervs on 2018/6/13.
 */
@Route(path = RouterPath.FRAGMENT_CIRCLE)
public class NewCircleFragment extends BaseNewFragment {
    private String TAG = this.getClass().getSimpleName();
    private TextView search;
    private TextView message_num;
    private XTabLayout tab_circle;
    private ViewPager viewpager_circle;
    private RelativeLayout rl_message;
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private String[] tab;
    //各种Fragment
    private CircleFollowFragment circle_follow;//关注
    private Fragment circle_recommend;//推荐
    private CircleLiveFragment circle_live;//直播
    private CircleCoffeeVideoFragment circle_coffee_video;//小视频
    private CircleStandardVideoFragment circle_standard_video;//视频
    private CircleDynamicFragment circle_dynamic;//动态
    //Fragment对应的Presenter
    private CircleFollowPresenter followPresenter;
    private CircleRecommendPresenter recommendPresenter;
    private CircleLivePresenter livePresenter;
    private CircleCoffeeVideoPresenter coffeePresenter;
    private CircleStandardVideoPresenter standardPresenter;
    private CircleDynamicPresenter dynamicPresenter;
    //存放动态标签Presenter的引用
    private ArrayList<CircleRecommendPresenter> pList;

    @Autowired
    IUserService userService;
    private List<GetTagInfoListByTypeResp.TagResult> list;

    @Override
    protected int setLayoutId() {
        return R.layout.new_circle_fragment;
    }

    @Override
    protected void initVars() {
        super.initVars();
        YhyRouter.getInstance().inject(this);
        pList = new ArrayList<>();
        //获取 Circle 动态Tab页面
        String json = SPCache.getCircleTabCache(getActivity());
        list = JSONUtils.convertToArrayList(json, GetTagInfoListByTypeResp.TagResult.class);
        if (list != null && list.size() > 0) {
            initNetTabs(list);
        } else {
            initLocalTabs();
        }
    }


    @Override
    protected void initView() {
        super.initView();
        search = mRootView.findViewById(R.id.search);
        message_num = mRootView.findViewById(R.id.message_num);
        tab_circle = mRootView.findViewById(R.id.tab_circle);
        viewpager_circle = mRootView.findViewById(R.id.viewpager_circle);
        rl_message = mRootView.findViewById(R.id.message);
        message_num = mRootView.findViewById(R.id.message_num);
        //viewpager
        CirclePagerAdapter circleAdapter = new CirclePagerAdapter(getFragmentManager(), fragments, tab);
        viewpager_circle.setAdapter(circleAdapter);
        viewpager_circle.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //tab bind viewpager
        tab_circle.setupWithViewPager(viewpager_circle);
//        TabLayoutUtils.reflex(tab_circle);

        //search
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Analysis.pushEvent(mActivity, AnEvent.SearchClick,
                        new Analysis.Builder().
                                setLat(LocationManager.getInstance().getStorage().getLast_lat()).
                                setLng(LocationManager.getInstance().getStorage().getLast_lng()));

                YhyRouter.getInstance().startCircleSearchActivity(NewCircleFragment.this.getActivity());
            }
        });
        //message
        rl_message.setOnClickListener(v -> {
            if (isLoggedIn()) {
                NavUtils.gotoMsgCenter(getContext());
            }
        });

//        initNetTabIcon(list);
        viewpager_circle.setCurrentItem(1);
        tab_circle.addOnTabSelectedListener(new XTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(XTabLayout.Tab tab) {

                Analysis.pushEvent(mActivity, AnEvent.TabClick,
                        new Analysis.Builder().
                                setLat(LocationManager.getInstance().getStorage().getLast_lat()).
                                setLng(LocationManager.getInstance().getStorage().getLast_lng()).
                                setTab(tab.getText().toString()));

            }

            @Override
            public void onTabUnselected(XTabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(XTabLayout.Tab tab) {
                EventBus.getDefault().post(new EvBusCircleTabRefresh());
            }
        });
        updateIMMessageCount(HomeMainTabActivity.imUnreadCount);
    }

    /***************************************************************     LogicMethod      ********************************************************************/


    /**
     * 动态Tab init
     */
    public void initNetTabs(List<GetTagInfoListByTypeResp.TagResult> tagResultList) {
        if (pList == null) return;
        tab = new String[tagResultList.size()];
        for (int i = 0; i < tagResultList.size(); i++) {
            GetTagInfoListByTypeResp.TagResult tag = tagResultList.get(i);
            switch (tag.getCode()) {
                case "ATTENTION":
                    circle_follow = (CircleFollowFragment) YhyRouter.getInstance().makeCircleFollowFragment(getActivity(),
                            AnArgs.Instance()
                                    .build(Analysis.TAB, tag.getDescription())
                                    .getMap());
                    fragments.add(circle_follow);
                    followPresenter = new CircleFollowPresenter(getActivity(), circle_follow);
                    tab[i] = tag.getDescription();
                    break;
                case "RECOMMEND":
                    circle_recommend = YhyRouter.getInstance().makeCircleRecommendFragment(getActivity(), -1,
                            AnArgs.Instance()
                                    .build(Analysis.TAB, tag.getDescription())
                                    .getMap());
                    fragments.add(circle_recommend);
                    recommendPresenter = new CircleRecommendPresenter(getActivity(), (CircleRecommendFragment) circle_recommend);
                    tab[i] = tag.getDescription();
                    break;
                case "LIVE":
                    circle_live = (CircleLiveFragment) YhyRouter.getInstance().makeCircleLiveFragment(getActivity(),
                            AnArgs.Instance()
                                    .build(Analysis.TAB, tag.getDescription())
                                    .getMap());
                    fragments.add(circle_live);
                    livePresenter = new CircleLivePresenter(getActivity(), circle_live, false);
                    tab[i] = tag.getDescription();
                    break;
                case "SHORTVIDEO":
                    circle_coffee_video = (CircleCoffeeVideoFragment) YhyRouter.getInstance().makeCircleCoffeeVideoFragment(getActivity(),
                            AnArgs.Instance()
                                    .build(Analysis.TAB, tag.getDescription())
                                    .getMap());
                    fragments.add(circle_coffee_video);
                    coffeePresenter = new CircleCoffeeVideoPresenter(getActivity(), circle_coffee_video);
                    tab[i] = tag.getDescription();
                    break;
                case "VIDEO":
                    circle_standard_video = (CircleStandardVideoFragment) YhyRouter.getInstance().makeCircleStandardVideoFragment(getActivity(),
                            AnArgs.Instance()
                                    .build(Analysis.TAB, tag.getDescription())
                                    .getMap());
                    fragments.add(circle_standard_video);
                    standardPresenter = new CircleStandardVideoPresenter(getActivity(), circle_standard_video, false);
                    tab[i] = tag.getDescription();
                    break;
                case "DYNAMIC":
                    circle_dynamic = (CircleDynamicFragment) YhyRouter.getInstance().makeCircleDynamicFragment(getActivity(),
                            AnArgs.Instance()
                                    .build(Analysis.TAB, tag.getDescription())
                                    .getMap());
                    fragments.add(circle_dynamic);
                    dynamicPresenter = new CircleDynamicPresenter(getActivity(), circle_dynamic);
                    tab[i] = tag.getDescription();
                    break;

                case "RECOMMEND2":

                    Fragment var = YhyRouter.getInstance().makeCircleRecommendFragment(getActivity(), tag.getId(), AnArgs.Instance()
                            .build(Analysis.TAB, tag.getDescription())
                            .getMap());
                    fragments.add(var);
                    CircleRecommendPresenter presenter = new CircleRecommendPresenter(getActivity(), (CircleRecommendFragment) var);
                    pList.add(presenter);
                    tab[i] = tag.getDescription();
                    break;

                default:
                    break;
            }
        }
    }

    /**
     * 动态Tab init
     */
    public void initNetTabIcon(List<GetTagInfoListByTypeResp.TagResult> tagResultList) {
        if (tagResultList == null || tagResultList.size() == 0) {
            return;
        }
        tab = new String[tagResultList.size()];
        for (int i = 0; i < tagResultList.size(); i++) {
            GetTagInfoListByTypeResp.TagResult tag = tagResultList.get(i);
            tab[i] = tag.getDescription();
            switch (tag.getCode()) {
                case "ATTENTION":
                case "RECOMMEND":
                case "LIVE":
                case "SHORTVIDEO":
                case "VIDEO":
                case "DYNAMIC":
                case "RECOMMEND2":
                    break;
                default:
                    final XTabLayout.Tab t = tab_circle.getTabAt(i);
                    t.getCustomView().setTag(tag.getIconUrl());
                    ImageLoadManager.loadImage(getActivity(), tag.getIconUrl(), new SimpleTarget<Drawable>() {

                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            if (t.getCustomView().getTag().equals(tag.getIconUrl())) {
                                t.setIcon(resource);
                            }
                        }
                    });
                    break;
            }
        }
    }

    /**
     * 默认Tab init
     */
    public void initLocalTabs() {
        tab = getResources().getStringArray(R.array.tab_circle);
        //init fragments
        circle_follow = (CircleFollowFragment) YhyRouter.getInstance().makeCircleFollowFragment(getActivity(),
                AnArgs.Instance()
                        .build(Analysis.TAB, "关注")
                        .getMap());
        ArrayList<String> tabList = new ArrayList<>();
        circle_recommend = YhyRouter.getInstance().makeCircleRecommendFragment(getActivity(), -1, AnArgs.Instance()
                .build(Analysis.TAB, "推荐")
                .getMap());
        circle_live = (CircleLiveFragment) YhyRouter.getInstance().makeCircleLiveFragment(getActivity(),
                AnArgs.Instance()
                        .build(Analysis.TAB, "直播")
                        .getMap());
        circle_coffee_video = (CircleCoffeeVideoFragment) YhyRouter.getInstance().makeCircleCoffeeVideoFragment(getActivity(),
                AnArgs.Instance()
                        .build(Analysis.TAB, "小视频")
                        .getMap());
        circle_standard_video = (CircleStandardVideoFragment) YhyRouter.getInstance().makeCircleStandardVideoFragment(getActivity(),
                AnArgs.Instance()
                        .build(Analysis.TAB, "视频")
                        .getMap());
        circle_dynamic = (CircleDynamicFragment) YhyRouter.getInstance().makeCircleDynamicFragment(getActivity(),
                AnArgs.Instance()
                        .build(Analysis.TAB, "动态")
                        .getMap());
        //fill collections
        fragments.add(circle_follow);
        fragments.add(circle_recommend);
        fragments.add(circle_live);
        fragments.add(circle_coffee_video);
        fragments.add(circle_standard_video);
        fragments.add(circle_dynamic);
        //init presenter
        followPresenter = new CircleFollowPresenter(getActivity(), circle_follow);
        recommendPresenter = new CircleRecommendPresenter(getActivity(), (CircleRecommendFragment) circle_recommend);
        livePresenter = new CircleLivePresenter(getActivity(), circle_live, false);
        coffeePresenter = new CircleCoffeeVideoPresenter(getActivity(), circle_coffee_video);
        standardPresenter = new CircleStandardVideoPresenter(getActivity(), circle_standard_video, false);
        dynamicPresenter = new CircleDynamicPresenter(getActivity(), circle_dynamic);
    }


    private boolean isLoggedIn() {
        long uid = userService.getLoginUserId();
        if (uid > 0) {
            return true;
        } else {
            YhyRouter.getInstance().startLoginActivity(getContext(), null, Intent.FLAG_ACTIVITY_CLEAR_TOP, IntentConstants.LOGIN_RESULT);
            return false;
        }
    }

    /**
     * 刷新消息数
     *
     * @param messageNum
     */
    public void updateIMMessageCount(int messageNum) {
        if (messageNum == 0) {
            message_num.setVisibility(View.INVISIBLE);
        } else {
            message_num.setVisibility(View.VISIBLE);
            message_num.setText("" + messageNum);
        }
    }

    /***************************************************************     onEvent      ********************************************************************/

    public void onEvent(EvBusMessageCount evBusMessageCount) {
        int count = evBusMessageCount.getCount();
        updateIMMessageCount(count);
    }


    @Override
    public void handleMessage(Message msg) {

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden && null != VideoPlayer.getInstance()) {
            VideoPlayer.getInstance().pause();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (null != VideoPlayer.getInstance())
            VideoPlayer.getInstance().pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (followPresenter != null) {
            followPresenter.release();
            followPresenter = null;
        }

        if (recommendPresenter != null) {
            recommendPresenter.release();
            recommendPresenter = null;
        }

        if (livePresenter != null) {
            livePresenter.release();
            livePresenter = null;
        }

        if (coffeePresenter != null) {
            coffeePresenter.release();
            coffeePresenter = null;
        }

        if (standardPresenter != null) {
            standardPresenter.release();
            standardPresenter = null;
        }

        if (dynamicPresenter != null) {
            dynamicPresenter.release();
            dynamicPresenter = null;
        }

        if (pList != null && pList.size() > 0) {
            for (CircleRecommendPresenter presenter : pList) {
                if (presenter != null) {
                    presenter.release();
                }
            }
            pList = null;
        }
    }
}
