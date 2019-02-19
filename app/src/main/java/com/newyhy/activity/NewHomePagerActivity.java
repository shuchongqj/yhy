package com.newyhy.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.harwkin.nb.camera.ImageUtils;
import com.newyhy.fragment.UgcFragment;
import com.newyhy.fragment.circle.CircleArticleFragment;
import com.quanyan.base.util.LocalUtils;
import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.R;
import com.quanyan.yhy.eventbus.EvBusUGCInfoAttention;
import com.quanyan.yhy.libanalysis.AnEvent;
import com.quanyan.yhy.libanalysis.Analysis;
import com.quanyan.yhy.net.NetManager;
import com.quanyan.yhy.net.lsn.OnResponseListener;
import com.quanyan.yhy.ui.adapter.DetailViewPagerAdapter;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.comment.ValueCommentType;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.master.fragment.TaEvaluateFragment;
import com.quanyan.yhy.ui.master.fragment.TaMasterfragment;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yhy.common.base.BaseNewActivity;
import com.yhy.common.beans.net.model.master.TalentInfo;
import com.yhy.common.constants.IntentConstants;
import com.yhy.common.utils.CommonUtil;
import com.yhy.common.utils.SPUtils;
import com.yhy.imageloader.ImageLoadManager;
import com.yhy.router.RouterPath;
import com.yhy.service.IUserService;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * 圈子新版本个人主页
 * <p>
 * Created by yangboxue on 2018/6/22.
 */
@Route(path = RouterPath.ACTIVITY_HOMEPAGE)
public class NewHomePagerActivity extends BaseNewActivity implements OnRefreshListener, View.OnClickListener {

    private SmartRefreshLayout refreshLayout;
    private AppBarLayout appbar;
    private ImageView ivBack;
    private TextView tvTitle;

    private ImageView topbgImgPagerView;
    private TextView topbgTitle;
    private ImageView mHeadView;
    private TextView nameTv;
    private TextView mTvfollow;
    private TextView mTvFunsNum;
    private TextView mTvAttentionNum;
    private LinearLayout mLLAttentionNum;
    private LinearLayout mLLFunsNum;
//    private LinearLayout base_sticky_bottom_layout2;
//    private LinearLayout butMasterTel;
//    private LinearLayout butMasterChat;

    private ViewPager mContentViewPager;
    private TabLayout tabLayout;
    private DetailViewPagerAdapter mAdapter;

    private TalentInfo TravelKaBeab = null;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private TaMasterfragment mTaMasterfragment;
    private UgcFragment mUgcFragment;
    private CircleArticleFragment mArticleFragment;
    private TaEvaluateFragment mTaEvaluateFragment;
    private long mTalentId;
    private boolean isMeUser;
    private boolean fromCousult;      //  咨询师跳转过来展示服务fragment
    private int oldOffset = 0;

    @Autowired
    IUserService userService;

    private static int USERINFO_REQCODE = 101;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_new_home_pager;
    }

    @Override
    protected void initVars() {
        super.initVars();
        mTalentId = getIntent().getLongExtra(SPUtils.EXTRA_ID, -1);
        fromCousult = getIntent().getBooleanExtra("fromCousult", false);
        isMeUser = userService.getLoginUserId() == mTalentId;
    }

    @Override
    protected void initView() {
        super.initView();

        refreshLayout = findViewById(R.id.refreshLayout);
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setEnableRefresh(false);

        appbar = findViewById(R.id.appbar);
//        toolbar = findViewById(R.id.toolbar);
        ivBack = findViewById(R.id.iv_back);
        tvTitle = findViewById(R.id.tv_title);
        mContentViewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.main_tablayout);

        topbgImgPagerView = findViewById(R.id.master_personal_topbg);
        topbgTitle = findViewById(R.id.master_bannerview_title);
        mHeadView = findViewById(R.id.master_personal_head);
        nameTv = findViewById(R.id.master_top_name);
        mTvfollow = findViewById(R.id.tv_master_follow);
        mTvFunsNum = findViewById(R.id.tv_fans_count);
        mTvAttentionNum = findViewById(R.id.tv_attention_count);
        mLLAttentionNum = findViewById(R.id.ll_attention_count);
        mLLFunsNum = findViewById(R.id.ll_funs_count);

        mImmersionBar.fitsSystemWindows(false).statusBarColor(R.color.white).statusBarDarkFont(true).init();

//        base_sticky_bottom_layout2 = findViewById(R.id.base_sticky_bottom_layout2);
//        base_sticky_bottom_layout2.setVisibility(isMeUser ? View.GONE : View.VISIBLE);
//        butMasterTel = findViewById(R.id.but_master_tel);
//        butMasterChat = findViewById(R.id.but_master_chat);
    }

    @Override
    protected void initData() {
        super.initData();
        getHomePageDetail();

    }

    /**
     * 获取个人主页数据
     */
    private void getHomePageDetail() {
        NetManager.getInstance(this).doGetTalentDetail(mTalentId, new OnResponseListener<TalentInfo>() {
            @Override
            public void onComplete(boolean isOK, TalentInfo value, int errorCode, String errorMsg) {
                if (isOK) {
                    handleHomePageData(value);
                } else {

                }
//                sendMessage(ValueConstants.MSG_GET_MASTER_DETAIL_KO, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
//                sendMessage(ValueConstants.MSG_GET_MASTER_DETAIL_KO, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 处理个人主页数据
     */
    private void handleHomePageData(TalentInfo value) {
        if (value == null) {
            return;
        }
        TravelKaBeab = value;

        // 背景
        ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(TravelKaBeab.frontCover), R.mipmap.icon_masterhomepage_bg, topbgImgPagerView);
        // 签名
        topbgTitle.setText(TravelKaBeab.signature);
        // 关注
        mTvAttentionNum.setText(TextUtils.isEmpty(String.valueOf(TravelKaBeab.followCount)) ? "0" : String.valueOf(TravelKaBeab.followCount));
        // 粉丝
        mTvFunsNum.setText(TextUtils.isEmpty(String.valueOf(TravelKaBeab.fansCount)) ? "0" : StringUtil.TransformationFansData((int) TravelKaBeab.fansCount));
        // 是否已关注
        showAttentionStatus(!"NONE".equals(TravelKaBeab.attentionType));
        // 头像
        ImageLoadManager.loadCircleImage(CommonUtil.getImageFullUrl(TravelKaBeab.avatar), R.mipmap.icon_default_avatar, mHeadView);
        // 昵称
        nameTv.setText(TravelKaBeab.nickName);
        tvTitle.setText(TravelKaBeab.nickName);
        // 性别 1-未确认 2-男 3-女
        if (!TextUtils.isEmpty(TravelKaBeab.gender)) {
            if (TravelKaBeab.gender.equals("2")) {
                Drawable nav_man = getResources().getDrawable(R.mipmap.ic_male);
                nav_man.setBounds(0, 0, nav_man.getMinimumWidth(), nav_man.getMinimumHeight());
                nameTv.setCompoundDrawables(null, null, nav_man, null);
            } else if (TravelKaBeab.gender.equals("3")) {
                Drawable nav_women = getResources().getDrawable(R.mipmap.ic_female);
                nav_women.setBounds(0, 0, nav_women.getMinimumWidth(), nav_women.getMinimumHeight());
                nameTv.setCompoundDrawables(null, null, nav_women, null);
            } else {
            }
        } else {
            nameTv.setVisibility(View.INVISIBLE);
        }
        // fragments
        mTaMasterfragment = TaMasterfragment.createTaMasrerfragment(mTalentId, value);
        mUgcFragment = UgcFragment.newInstance(0, value.userId);
        if (mUgcFragment != null){
            mUgcFragment.setFollowStatus(TravelKaBeab.attentionType);
        }
        mTaEvaluateFragment = TaEvaluateFragment.createTaEvaluateFragment(value.userId, value.orgId);
//        mArticleFragment = CircleArticleFragment.createCircleArticleFragment(value.userId, value.orgId);
        mFragments.add(mUgcFragment);
//        mFragments.add(mArticleFragment);
        mFragments.add(mTaMasterfragment);
        mFragments.add(mTaEvaluateFragment);
        ArrayList<String> titles = new ArrayList<>();
        String[] strings = getResources().getStringArray(R.array.master_tabs);
        for (int i = 0; i < strings.length; i++) {
            titles.add(strings[i]);
        }
        mAdapter = new DetailViewPagerAdapter(getSupportFragmentManager(), mFragments, titles);
        mContentViewPager.setAdapter(mAdapter);
        mContentViewPager.setCurrentItem(fromCousult ? mFragments.size() - 1 : 0);
        mContentViewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(mContentViewPager);

    }

    private void showAttentionStatus(boolean isAttention) {
        mTvfollow.setVisibility(View.VISIBLE);
        if (isMeUser) {
            mTvfollow.setClickable(true);
            mTvfollow.setText(getString(R.string.label_master_personaldata));
        } else {
            mTvfollow.setClickable(!isAttention);
            mTvfollow.setText(isAttention ? getString(R.string.icon_label_club_list_joined) : getString(R.string.add_attention));
        }
    }

    @Override
    protected void setListener() {
        super.setListener();

        refreshLayout.setOnRefreshListener(this);
        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset < oldOffset) {
                    if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                        appbar.setExpanded(false, true);
                    }
                }
                oldOffset = verticalOffset;

                if (Math.abs(verticalOffset) > appbar.getTotalScrollRange() / 1.5) {
                    ivBack.setImageResource(R.mipmap.arrow_back_gray);
                    tvTitle.setVisibility(View.VISIBLE);
                } else {
                    ivBack.setImageResource(R.mipmap.arrow_back_white);
                    tvTitle.setVisibility(View.GONE);
                }

            }
        });

        ivBack.setOnClickListener(this);
        mHeadView.setOnClickListener(this);
        mLLAttentionNum.setOnClickListener(this);
        mLLFunsNum.setOnClickListener(this);
        mTvfollow.setOnClickListener(this);
//        butMasterTel.setOnClickListener(this);
//        butMasterChat.setOnClickListener(this);

    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        switch (mContentViewPager.getCurrentItem()) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
        }

        refreshLayout.finishRefresh(1000);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.but_master_tel:
                if (TravelKaBeab != null && !StringUtil.isEmpty(TravelKaBeab.telNum)) {
                    LocalUtils.call(NewHomePagerActivity.this, TravelKaBeab.telNum);
                } else {
                    ToastUtil.showToast(NewHomePagerActivity.this, getString(R.string.no_telephone_number));
                }
                break;
            case R.id.but_master_chat:
                NavUtils.gotoMessageActivity(NewHomePagerActivity.this, Integer.parseInt(String.valueOf(TravelKaBeab.userId)));
                break;
            case R.id.tv_master_follow:
                if (userService.isLogin()) {
                    if (isMeUser) {
                        NavUtils.gotoUserInfoEditActivity(NewHomePagerActivity.this, ValueCommentType.PIC_AND_TEXT_EXPERTHOME, USERINFO_REQCODE);
                    } else {
                        // 事件统计
                        if (fromCousult) {
                            Analysis.pushEvent(this, AnEvent.CONSULTANT_HOMEPAGE_CONCERN);
                        } else {
                            Analysis.pushEvent(this, AnEvent.INFORMATION_FLOW_ATTENTION);
                        }
                        doAttention();
                    }
                } else {
                    NavUtils.gotoLoginActivity(NewHomePagerActivity.this);
                }
                break;
            case R.id.ll_funs_count:            // 粉丝列表
                if (isMeUser) {
                    NavUtils.gotoMyFansListActivity(NewHomePagerActivity.this, userService.getLoginUserId());
                } else {
                    NavUtils.gotoOtherFansListActivity(NewHomePagerActivity.this, (TravelKaBeab.userId));
                }
                break;
            case R.id.ll_attention_count:       // 关注列表
                if (isMeUser) {
                    NavUtils.gotoMyAttentionListActivity(this, userService.getLoginUserId());
                } else {
                    NavUtils.gotoOhterAttentionListActivity(NewHomePagerActivity.this, TravelKaBeab.userId);
                }
                break;
            case R.id.master_personal_head:     // 查看头像大图
                if (TravelKaBeab != null && !TextUtils.isEmpty(TravelKaBeab.avatar)) {
                    ArrayList<String> tmpList = new ArrayList<>();
                    tmpList.add(ImageUtils.getImageFullUrl(TravelKaBeab.avatar));
                    NavUtils.gotoLookBigImage(this, tmpList, 0);
                }
                break;
        }

    }

    /**
     * 添加关注
     */
    private void doAttention() {
        NetManager.getInstance(this).doAddFollower(mTalentId, new OnResponseListener<Boolean>() {
            @Override
            public void onComplete(boolean isOK, Boolean result, int errorCode, String errorMsg) {
                if (isOK && result) {
                    mTvfollow.setClickable(false);
                    mTvfollow.setText(getString(R.string.icon_label_club_list_joined));
                    TravelKaBeab.fansCount = TravelKaBeab.fansCount + 1;
                    mTvFunsNum.setText(StringUtil.TransformationFansData(TravelKaBeab.fansCount));
                    ToastUtil.showToast(NewHomePagerActivity.this, getString(R.string.label_master_toast_follow_success));
                    EventBus.getDefault().post(new EvBusUGCInfoAttention(mTalentId, true));
                } else {
                    ToastUtil.showToast(NewHomePagerActivity.this, StringUtil.handlerErrorCode(NewHomePagerActivity.this, errorCode));
                }
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                ToastUtil.showToast(NewHomePagerActivity.this, StringUtil.handlerErrorCode(NewHomePagerActivity.this, errorCode));

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case IntentConstants.LOGIN_RESULT://成功登录
                if (RESULT_OK == resultCode) {
                    getHomePageDetail();
                }
                break;
            case TaMasterfragment.PEFECT_RESULT://完善资料
                if (RESULT_OK == resultCode) {
                    boolean booleanExtra = data.getBooleanExtra(SPUtils.EXTRA_DATA, true);
                    SPUtils.setUserHomePage(NewHomePagerActivity.this, booleanExtra);
                    mTaMasterfragment.againLoadUrl(SPUtils.getUserHomePage(NewHomePagerActivity.this));
                }
                break;
            case 101://编辑个人资料按钮
                if (RESULT_OK == resultCode) {
                    getHomePageDetail();
                    mTaMasterfragment.againLoadUrl(SPUtils.getUserHomePage(NewHomePagerActivity.this));
                }
                break;

        }
    }
}
