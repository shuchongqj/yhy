package com.quanyan.yhy.ui.master.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.quanyan.base.BaseListViewActivity;
import com.quanyan.base.baseenum.IActionTitleBar;
import com.quanyan.base.util.StringUtil;
import com.quanyan.base.view.BaseNavView;
import com.quanyan.base.view.customview.pulltorefresh.PullToRefreshBase;
import com.quanyan.base.yminterface.ErrorViewClick;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.AnalyDataValue;
import com.quanyan.yhy.common.ErrorCode;
import com.quanyan.yhy.ui.adapter.base.BaseAdapterHelper;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.base.utils.TCEventHelper;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.master.controller.MasterController;
import com.quanyan.yhy.ui.views.CancelFollowPopView;
import com.yhy.common.beans.net.model.user.FollowerInfo;
import com.yhy.common.beans.net.model.user.FollowerPageListResult;
import com.yhy.common.constants.IntentConstant;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.SPUtils;
import com.yhy.service.IUserService;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with Android Studio.
 * Title:AttentionListActivity
 * Description:关注粉丝列表
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:邓明佳
 * Date:2016/6/22
 * Time:11:10
 * Version 1.0
 */
public class AttentionListActivity extends BaseListViewActivity<FollowerInfo> {
    private long userId;
    private AttenttionType attenttionType;
    BaseNavView baseNavView;
    private MasterController controller;
    private String emptyMessage;
    private boolean isAction;

    @Autowired
    IUserService userService;

    public IUserService getUserService() {
        return userService;
    }

    @Override
    public void fetchData(int pageIndex) {
        setPageIndex(pageIndex);
        if (attenttionType == AttenttionType.MY_FANS || attenttionType == AttenttionType.OTHER_FANS) {
            controller.doGetFansList(this, userId, pageIndex);
        } else if (attenttionType == AttenttionType.MY_ATTENTION || attenttionType == AttenttionType.OTHER_ATTENTION) {
            controller.doGetAttentionList(this, userId, pageIndex);
        }
    }

    @Override
    public void dispatchMessage(Message msg) {
        if (msg.what == ValueConstants.MSG_GET_ATTENTION_LIST_OK) {
            //获取关注列表成功
            getBaseDropListView().getPullRefreshView().onRefreshComplete();
            if (msg.obj != null) {
                FollowerPageListResult result = (FollowerPageListResult) msg.obj;
                List<FollowerInfo> list = result.followList;
                if (list != null && list.size() > 0) {
                    if (getPageIndex() == 1) {
                        getAdapter().replaceAll(list);
                    } else {
                        //其他页 添加数据
                        getAdapter().addAll(list);
                    }
                } else {
                    if (getAdapter().getCount() != 0) {
                        if (getPageIndex() != 1) {
                            ToastUtil.showToast(this, R.string.scenic_hasnodata);
                        } else {
                            getAdapter().clear();
                        }
                    }
                }
            }
            if (getAdapter().getCount() == 0) {
                getBaseDropListView().getPullRefreshView().setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                showErrorView(null, IActionTitleBar.ErrorType.EMPTYVIEW, emptyMessage, " ", null, null);
            } else {
                getBaseDropListView().getPullRefreshView().setMode(PullToRefreshBase.Mode.BOTH);
            }
        } else if (msg.what == ValueConstants.MSG_GET_ATTENTION_LIST_KO) {
            getBaseDropListView().getPullRefreshView().onRefreshComplete();
            if (getAdapter().getCount() == 0) {
                showErrorView(null, ErrorCode.NETWORK_UNAVAILABLE == msg.arg1 ?
                        IActionTitleBar.ErrorType.NETUNAVAILABLE : IActionTitleBar.ErrorType.ERRORNET, "", "", "", new ErrorViewClick() {
                    @Override
                    public void onClick(View view) {
                        fetchData(1);
                    }
                });
            } else {
                ToastUtil.showToast(this, StringUtil.handlerErrorCode(this, msg.arg2));
            }
            //我的粉丝 我的关注 他的粉丝 他的关注
        } else if (msg.what == ValueConstants.MSG_ATTENTION_OK) {
            long userId = (long) msg.obj;
            for (int i = 0; i < getAdapter().getData().size(); i++) {
                if (getAdapter().getData().get(i).userId == userId) {
                    getAdapter().getData().get(i).type = ValueConstants.FOLLOW;
                    break;
                }
            }
            getAdapter().notifyDataSetChanged();
            isAction = false;
            //关注成功
        } else if (msg.what == ValueConstants.MSG_ATTENTION_KO) {
            ToastUtil.showToast(this, StringUtil.handlerErrorCode(this, msg.arg1));
            isAction = false;
            //关注失败
        } else if (msg.what == ValueConstants.MSG_CANCEL_ATTENTION_OK) {
            ToastUtil.showToast(this, R.string.label_master_toast_cancelfollow_success);
            long userId = (long) msg.obj;
            //取关成功
            for (int i = 0; i < getAdapter().getData().size(); i++) {
                if (getAdapter().getData().get(i).userId == userId) {
                    if (attenttionType == AttenttionType.MY_ATTENTION) {
                        getAdapter().getData().remove(i);
                    } else {
                        getAdapter().getData().get(i).type = ValueConstants.NONE;
                    }
                    break;
                }
            }
            getAdapter().notifyDataSetChanged();
            isAction = false;
        } else if (msg.what == ValueConstants.MSG_CANCEL_ATTENTION_KO) {
            ToastUtil.showToast(this, StringUtil.handlerErrorCode(this, msg.arg1));
            //取关失败
            isAction = false;
        }
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        controller = new MasterController(this, mHandler);
        userId = getIntent().getLongExtra(IntentConstant.EXTRA_UID, 0);
        attenttionType = (AttenttionType) getIntent().getSerializableExtra(IntentConstant.EXTRA_ATTENTION_TYPE);
        refreshAttentionInfo();
        fetchData(1);
    }

    private boolean refreshAttentionInfo() {
        boolean isChange = false;
        if (userId == userService.getLoginUserId()) {
            if (attenttionType == AttenttionType.OTHER_FANS) {
                attenttionType = AttenttionType.MY_FANS;
                isChange = true;
            } else if (attenttionType == AttenttionType.OTHER_ATTENTION) {
                attenttionType = AttenttionType.MY_ATTENTION;
                isChange = true;
            }
        }
        int resId = getTitle(attenttionType);
        if (resId != -1){
            baseNavView.setTitleText(resId);
        }
        if (attenttionType == AttenttionType.OTHER_FANS || attenttionType == AttenttionType.MY_FANS) {
            emptyMessage = "暂无粉丝";
        } else {
            emptyMessage = "暂无关注";
        }
        return isChange;
    }

    private int getTitle(AttenttionType attenttionType) {
        int resId = -1;
        if (attenttionType == AttenttionType.MY_FANS) {
            resId = R.string.my_fans;
        } else if (attenttionType == AttenttionType.MY_ATTENTION) {
            resId = R.string.my_attention;
        } else if (attenttionType == AttenttionType.OTHER_FANS) {
            resId = R.string.other_fans;
        } else if (attenttionType == AttenttionType.OTHER_ATTENTION) {
            resId = R.string.other_attention;
        }
        return resId;
    }

    @Override
    public void convertItem(BaseAdapterHelper helper, FollowerInfo item) {
        helper.setText(R.id.tv_username, item.nickname).setText(R.id.tv_content, item.signature).setImageUrlRound(R.id.iv_userhead, item.avatar, 128, 128, R.mipmap.icon_default_avatar);
        ImageView submit = helper.getView(R.id.iv_submit);
        submit.setTag(item);
        View clickView = helper.getView(R.id.click_view);
        clickView.setTag(item);
        clickView.setOnClickListener(onGotoMasterHomeActivityListener);
        if (loginUid == item.userId) {
            submit.setVisibility(View.GONE);
        } else {
            int resId = -1;
            if (ValueConstants.NONE.equals(item.type)) {
                resId = R.mipmap.ic_add_attention;
                submit.setOnClickListener(onAddAttentionListener);
            } else if (ValueConstants.FOLLOW.equals(item.type)) {
                resId = R.mipmap.ic_attentioned;
                submit.setOnClickListener(onCancelAttentionListener);
            } else if (ValueConstants.BIFOLLOW.equals(item.type)) {
                if (attenttionType == AttenttionType.MY_ATTENTION) {
                    resId = R.mipmap.ic_both_attentioned;
                } else {
                    resId = R.mipmap.ic_attentioned;
                }
                submit.setOnClickListener(onCancelAttentionListener);
            }
            submit.setImageResource(resId);
            submit.setVisibility(View.VISIBLE);
        }
    }

    View.OnClickListener onAddAttentionListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!getUserService().isLogin()) {
                NavUtils.gotoLoginActivity(AttentionListActivity.this);
                return;
            }
            if (isAction) return;
            isAction = true;
            FollowerInfo info = (FollowerInfo) v.getTag();
            tcEvent(info);
            controller.doAttention(AttentionListActivity.this, info.userId);
        }
    };

    private void tcEvent(FollowerInfo info) {
        Map<String, String> map = new HashMap();
        map.put(AnalyDataValue.KEY_ID, info.userId + "");
        map.put(AnalyDataValue.KEY_NAME, info.nickname);
        map.put(AnalyDataValue.KEY_TYPE, info.type);
        TCEventHelper.onEvent(this, AnalyDataValue.ATTENTION_AND_FANS_BUTTON_CLICK, map);
    }

    public AttenttionType getAttenttionType() {
        return attenttionType;
    }

    CancelFollowPopView mCacheDl;

    /***
     * 取消关注
     */
    private void showCacheDialog(long userId) {
        if (mCacheDl == null) {
            mCacheDl = new CancelFollowPopView(this, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.dialog_canel_follow://取消
                            isAction = true;
                            controller.cancelAttention(AttentionListActivity.this, (Long) v.getTag(), new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    isAction = false;
                                }
                            });

                            mCacheDl.dismiss();
                            break;
                    }
                }
            });
        }
        mCacheDl.getContentView().findViewById(R.id.dialog_canel_follow).setTag(userId);
        mCacheDl.showAtLocation(getContentParentView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loginUid = userService.getLoginUserId();
        if (refreshAttentionInfo()) {
            fetchData(1);
        }
    }

    long loginUid = 0;

    View.OnClickListener onCancelAttentionListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (isAction) return;
            FollowerInfo info = (FollowerInfo) v.getTag();
            tcEvent(info);
            showCacheDialog(info.userId);
        }
    };

    View.OnClickListener onGotoMasterHomeActivityListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FollowerInfo info = (FollowerInfo) v.getTag();
            Map<String, String> map = new HashMap();
            map.put(AnalyDataValue.KEY_ID, info.userId + "");
            map.put(AnalyDataValue.KEY_NAME, info.nickname);
            map.put(AnalyDataValue.KEY_TYPE, attenttionType.toString());
            TCEventHelper.onEvent(AttentionListActivity.this, AnalyDataValue.ATTETION_AND_FANS_LIST_CLICK, map);
//            NavUtils.gotoPersonalPage(AttentionListActivity.this,info.userId,info.nickname,info.option);
            NavUtils.gotoMasterHomepage(AttentionListActivity.this, info.userId);
        }
    };

    @Override
    public int setItemLayout() {
        return R.layout.attention_list_item_view;
    }

    @Nullable
    @Override
    public List<String> setTabStrings() {
        return null;
    }

    @Nullable
    @Override
    public List<View> setPopViews() {
        return null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public View onLoadNavView() {
        baseNavView = new BaseNavView(this);
        return baseNavView;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }

    public enum AttenttionType implements Serializable {
        MY_FANS, OTHER_FANS, MY_ATTENTION, OTHER_ATTENTION;
    }

    @Override
    public int getPageSize() {
        return 20;
    }
}
