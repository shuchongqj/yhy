package com.quanyan.yhy.ui.common.tourist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.harwkin.nb.camera.TimeUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.baseenum.IActionTitleBar;
import com.quanyan.base.util.StringUtil;
import com.quanyan.base.view.customview.pulltorefresh.PullToRefreshBase;
import com.quanyan.base.view.customview.pulltorefresh.PullToRefreshScrollDeleteListView;
import com.quanyan.base.view.customview.scrolldeletelistview.ScrollDeleteListView;
import com.quanyan.base.yminterface.ErrorViewClick;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.ErrorCode;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.common.tourist.adapter.CommonUseTouristAdapter;
import com.quanyan.yhy.ui.common.tourist.model.DeleteUserContact;
import com.quanyan.yhy.ui.order.OrderTopView;
import com.yhy.common.beans.net.model.common.person.UserContact;
import com.yhy.common.beans.net.model.common.person.UserContact_ArrayResp;
import com.yhy.common.beans.net.model.common.person.VisitorInfoList;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.AndroidUtils;
import com.yhy.common.utils.SPUtils;
import com.yhy.service.IUserService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:CommonUseTouristActivity
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-6-6
 * Time:14:53
 * Version 1.0
 * Description:常用游客列表
 */
public class CommonUseTouristActivity extends BaseActivity {

    private static final String TYPE = "type";
    private static final String TOURISTTYPE = "touristType";
    private static final String MAXNUM = "MAXNUM";
    private static final String VISTLIST = "VISTLIST";

    private static final int ADDORUPDATE = 0x100;

    private OrderTopView mOrderTopView;
    @ViewInject(R.id.lv_content)
    private PullToRefreshScrollDeleteListView mTourstContent;
    private ListView mListView;
    @ViewInject(R.id.tv_visitor_add_hint)
    private TextView mBottomTV;
    @ViewInject(R.id.rl_bottom_view)
    private RelativeLayout mBottonView;
    @ViewInject(R.id.rl_tips)
    private RelativeLayout mTips;
    @ViewInject(R.id.tv_select_num)
    private TextView mSelectNumTv;
    @ViewInject(R.id.tv_total_num)
    private TextView mTotalNumTv;
    @ViewInject(R.id.emptyView)
    private LinearLayout emptyView;
    @ViewInject(R.id.error_view_text)
    private TextView mErrorTv;

    private int channelType = 0;
    private String touristType;
    private int maxSelectNum = 0;

    private TouristController mController;
    private long uid;

    private CommonUseTouristAdapter mAdapter;

    private VisitorInfoList visitorInfoList;

    private DeleteUserContact deleteUsers;
    private List<UserContact> mUsers;
    private UserContact deleteUser;

    @Autowired
    IUserService userService;

    /**
     * @param context
     * @param reqCode
     * @param channelType 区分是我的，还是下订单时进入的(下订单进入时是选择游客还是选择联系人)
     * @param touristType 区分境外游境内游
     */
    public static void gotoCommonUseTouristActivity(Activity context, int reqCode, int channelType, String touristType) {
        Intent intent = new Intent(context, CommonUseTouristActivity.class);
        intent.putExtra(TYPE, channelType);
        intent.putExtra(TOURISTTYPE, touristType);
        context.startActivityForResult(intent, reqCode);
    }

    /**
     * @param context
     * @param reqCode
     * @param channelType     区分是我的，还是下订单时进入的(下订单进入时是选择游客还是选择联系人)
     * @param touristType     区分境外游境内游
     * @param visitorInfoList 选中的游客列表
     * @param maxNum          最大选择多少游客
     */
    public static void gotoCommonUseTouristActivity(Activity context, int reqCode, int channelType, String touristType, VisitorInfoList visitorInfoList, int maxNum) {
        Intent intent = new Intent(context, CommonUseTouristActivity.class);
        intent.putExtra(TYPE, channelType);
        intent.putExtra(TOURISTTYPE, touristType);
        intent.putExtra(VISTLIST, visitorInfoList);
        intent.putExtra(MAXNUM, maxNum);
        context.startActivityForResult(intent, reqCode);
    }

    /**
     * 保存选择的联系人
     */
    private void saveContactSelect(UserContact userContact) {
        Intent intent = new Intent();
        intent.putExtra(SPUtils.EXTRA_DATA, userContact);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    /**
     * 保存选择的游客
     */
    private void saveVisitorsSelect() {
        Intent intent = new Intent();
        if (visitorInfoList == null) {
            visitorInfoList = new VisitorInfoList();
        }
        visitorInfoList.value = mAdapter.getSelectedVisitors();
        intent.putExtra(SPUtils.EXTRA_DATA, visitorInfoList);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    /**
     * 删除游客
     */
    private void deleteBack() {
        Intent intent = new Intent();
        if (deleteUsers == null) {
            deleteUsers = new DeleteUserContact();
        }
        deleteUsers.mUsers = mUsers;
        intent.putExtra(SPUtils.EXTRA_DATA, deleteUsers);
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ViewUtils.inject(this);
        uid = userService.getLoginUserId();
        mController = new TouristController(this, mHandler);
        channelType = getIntent().getIntExtra(TYPE, -1);
        touristType = getIntent().getStringExtra(TOURISTTYPE);
        if (channelType == TouristType.ORDERTOURIST) {
            maxSelectNum = getIntent().getIntExtra(MAXNUM, -1);
            visitorInfoList = (VisitorInfoList) getIntent().getSerializableExtra(VISTLIST);
        }
        mUsers = new ArrayList<UserContact>();
        setTitleBarTv();
        mTourstContent.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mTourstContent.setScrollingWhileRefreshingEnabled(!mTourstContent.isScrollingWhileRefreshingEnabled());
        mListView = mTourstContent.getRefreshableView();
        mAdapter = new CommonUseTouristAdapter(this, channelType, touristType);
        mListView.setAdapter(mAdapter);
        //网络访问
        showLoadingView(getString(R.string.loading_text));
        mController.doUpdateVisitorInfo(CommonUseTouristActivity.this, uid);

        initClick();
    }

    private void setTitleBarTv() {
        if (channelType == TouristType.MIMETOURIST) {
            mOrderTopView.setOrderTopTitle(getResources().getString(R.string.title_common_visitors));
            mBottomTV.setText("新增常用游客");
            mTips.setVisibility(View.GONE);
        } else if (channelType == TouristType.ORDERTOURIST) {
            mOrderTopView.setOrderTopTitle(getResources().getString(R.string.title_visitors_select));
            mBottomTV.setText("新增游客");
            mTips.setVisibility(View.VISIBLE);
            mSelectNumTv.setText("0");
            mTotalNumTv.setText("/" + maxSelectNum + "位游客");
            mOrderTopView.setRightTvColor(getResources().getColor(R.color.neu_999999));
//            mOrderTopView.setRightLayoutEnable(false);
            mOrderTopView.setRightViewVisible("确定");
        } else if (channelType == TouristType.ORDERCONTACTS) {
            mOrderTopView.setOrderTopTitle(getResources().getString(R.string.title_people_list));
            mBottomTV.setText("新增联系人");
            mTips.setVisibility(View.GONE);
        }
    }

    private void initClick() {
        //底部添加
        mBottonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TimeUtil.isFastDoubleClick()) {
                    return;
                }
                if (channelType == TouristType.MIMETOURIST) {//新增常用游客
                    NavUtils.gotoAddOrUpdateMimeTouristActivity(CommonUseTouristActivity.this, ADDORUPDATE, null);
                } else if (channelType == TouristType.ORDERCONTACTS) {//新增联系人
                    NavUtils.gotoAddOrUpdateOrderTouristActivity(CommonUseTouristActivity.this, ADDORUPDATE, channelType, null, null);
                } else if (channelType == TouristType.ORDERTOURIST) {//新增游客
                    if (touristType.equals(TouristType.TRAVELIN)) {//境内游
                        NavUtils.gotoAddOrUpdateOrderTouristActivity(CommonUseTouristActivity.this, ADDORUPDATE, channelType, touristType, null);
                    } else if (touristType.equals(TouristType.TRAVELOUT)) {//境外游
                        NavUtils.gotoAddOrUpdateOrderTouristActivity(CommonUseTouristActivity.this, ADDORUPDATE, channelType, touristType, null);
                    }
                }
            }
        });

        mOrderTopView.setBackClickListener(new OrderTopView.BackClickListener() {
            @Override
            public void clickBack() {
                deleteBack();
            }
        });

        mOrderTopView.setRightClickListener(new OrderTopView.RightClickListener() {
            @Override
            public void rightClick() {
                if (TimeUtil.isFastDoubleClick()) {
                    return;
                }
                if (mAdapter.getSelectedVisitors() == null || mAdapter.getSelectedVisitors().size() < maxSelectNum) {
                    ToastUtil.showToast(CommonUseTouristActivity.this, "需选择" + maxSelectNum + "名游客");
                    return;
                } else if (mAdapter.getSelectedVisitors().size() > maxSelectNum) {
                    ToastUtil.showToast(CommonUseTouristActivity.this, "最多可选择" + maxSelectNum + "名游客");
                    return;
                } else {
                    saveVisitorsSelect();
                }
            }
        });

        mAdapter.setOnVisitorItemClickLsn(new CommonUseTouristAdapter.OnVisitorItemClickLsn() {
            @Override
            public void onEditClick(UserContact info) {
                if (channelType == TouristType.MIMETOURIST) {//修改常用游客
                    NavUtils.gotoAddOrUpdateMimeTouristActivity(CommonUseTouristActivity.this, ADDORUPDATE, info);
                } else if (channelType == TouristType.ORDERCONTACTS) {//修改联系人
                    NavUtils.gotoAddOrUpdateOrderTouristActivity(CommonUseTouristActivity.this, ADDORUPDATE, channelType, null, info);
                } else if (channelType == TouristType.ORDERTOURIST) {//修改游客

                    if (visitorInfoList == null) {
                        visitorInfoList = new VisitorInfoList();
                    }
                    visitorInfoList.value = mAdapter.getSelectedVisitors();

                    if (touristType.equals(TouristType.TRAVELIN)) {//境内游
                        NavUtils.gotoAddOrUpdateOrderTouristActivity(CommonUseTouristActivity.this, ADDORUPDATE, channelType, touristType, info);
                    } else if (touristType.equals(TouristType.TRAVELOUT)) {//境外游
                        NavUtils.gotoAddOrUpdateOrderTouristActivity(CommonUseTouristActivity.this, ADDORUPDATE, channelType, touristType, info);
                    }
                }
            }

            @Override
            public void onDeleteClick(UserContact info) {
                deleteUser = info;
                if (visitorInfoList == null) {
                    visitorInfoList = new VisitorInfoList();
                }
                visitorInfoList.value = mAdapter.getSelectedVisitors();
                mController.doDeleteVisitor(CommonUseTouristActivity.this, info);
            }
        });

        mTourstContent.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollDeleteListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollDeleteListView> refreshView) {
                if (visitorInfoList == null) {
                    visitorInfoList = new VisitorInfoList();
                }
                visitorInfoList.value = mAdapter.getSelectedVisitors();
                mController.doUpdateVisitorInfo(CommonUseTouristActivity.this, uid);//下拉
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollDeleteListView> refreshView) {

            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int headerCount = mListView.getHeaderViewsCount();
                int newPosition = position - headerCount;
                if (channelType == TouristType.MIMETOURIST) {

                } else if (channelType == TouristType.ORDERCONTACTS) {
                    if (newPosition >= 0) {
                        UserContact userContact = (UserContact) mAdapter.getItem(newPosition);
                        if (userContact.isCanChoose) {
                            saveContactSelect(userContact);
                        } else {
                            ToastUtil.showToast(CommonUseTouristActivity.this, "请先补全信息");
                        }
                    }
                } else if (channelType == TouristType.ORDERTOURIST) {
                    if (newPosition >= 0) {
                        UserContact userContact = (UserContact) mAdapter.getItem(newPosition);
                        if (userContact.isCanChoose) {
                            //至多选择多少
                            userContact.isChoosed = userContact.isChoosed ? false : true;
                            if (maxSelectNum > 0) {
                                List<UserContact> selectedVisitors = mAdapter.getSelectedVisitors();
                                if (selectedVisitors.size() > maxSelectNum) {
                                    ToastUtil.showToast(CommonUseTouristActivity.this, "最多选择" + maxSelectNum + "名游客");
                                    userContact.isChoosed = false;
                                } else {
                                    if (selectedVisitors.size() == maxSelectNum) {
//                                        mOrderTopView.setRightLayoutEnable(true);
                                        mOrderTopView.setRightTvColor(getResources().getColor(R.color.neu_333333));
                                    } else {
//                                        mOrderTopView.setRightLayoutEnable(false);
                                        mOrderTopView.setRightTvColor(getResources().getColor(R.color.neu_999999));
                                    }
                                    mSelectNumTv.setText(selectedVisitors.size() + "");
                                }

                                ((ImageView) view.findViewById(R.id.iv_check)).setImageResource(userContact.isChoosed ? R.mipmap.ic_checked : R.mipmap.ic_uncheck);
                            } else {
                                ToastUtil.showToast(CommonUseTouristActivity.this, "最多选择" + maxSelectNum + "名游客");
                            }
                        } else {
                            ToastUtil.showToast(CommonUseTouristActivity.this, "请先补全信息");
                        }
                    }
                }

            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            deleteBack();
        }
        return true;
    }


    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.activity_commonusetourist, null);
    }

    @Override
    public View onLoadNavView() {
        mOrderTopView = new OrderTopView(this);
        return mOrderTopView;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        hideLoadingView();
        mTourstContent.onRefreshComplete();
        switch (msg.what) {
            case ValueConstants.MSG_GET_NEW_TOURIST_LIST_OK://获取游客列表成功
                hideNoDataPageView();
                handleVisitorList((UserContact_ArrayResp) msg.obj);
                break;
            case ValueConstants.MSG_GET_NEW_TOURIST_LIST_KO:
                hideNoDataPageView();
                if (mAdapter.getCount() == 0) {
                    mAdapter.clearData();
                    showNetErrorView(null, msg.arg1);
                } else {
                    AndroidUtils.showToastCenter(this, StringUtil.handlerErrorCode(this, msg.arg1));
                }
                break;
            case ValueConstants.MSG_NEW_DELETE_TOURIST_OK://删除游客列表成功
                mUsers.add(deleteUser);
                mController.doUpdateVisitorInfo(CommonUseTouristActivity.this, uid);
                break;
            case ValueConstants.MSG_NEW_DELETE_TOURIST_KO:
                ToastUtil.showToast(CommonUseTouristActivity.this, getString(R.string.toast_delete_visitor_failed));
                break;
        }
    }

    /**
     * 处理联系人列表
     *
     * @param response
     */
    private void handleVisitorList(UserContact_ArrayResp response) {
        if (response == null) {
            if (mAdapter != null) {
                mAdapter.clearData();
            }
            showNoDataPageView();
            return;
        }

        if (response.value != null && response.value.size() != 0) {
            mAdapter.setData(response.value);
            mAdapter.setCanChoose();
            if (channelType == TouristType.ORDERTOURIST) {
                if (visitorInfoList != null && visitorInfoList.value.size() != 0) {
                    if (deleteUser != null) {
                        for (int i = 0; i < visitorInfoList.value.size(); i++) {
                            if (visitorInfoList.value.get(i).id == deleteUser.id) {
                                visitorInfoList.value.remove(i);
                            }
                        }
                        deleteUser = null;
                    }
                }
                //选择过的联系人状态
                if (visitorInfoList != null && visitorInfoList.value.size() != 0) {
                    List<UserContact> userContacts = visitorInfoList.value;
                    mAdapter.checkStatusChange(userContacts);
                }
                List<UserContact> selectedVisitors = mAdapter.getSelectedVisitors();
                mSelectNumTv.setText(selectedVisitors.size() + "");
                if (selectedVisitors.size() == maxSelectNum) {
                    mOrderTopView.setRightTvColor(getResources().getColor(R.color.neu_333333));
                }

//            mOrderTopView.setRightLayoutEnable(false);
            }
        } else {
            mAdapter.clearData();
            showNoDataPageView();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ADDORUPDATE:
                    showLoadingView(getString(R.string.loading_text));
                    mController.doUpdateVisitorInfo(CommonUseTouristActivity.this, uid);
                    break;
            }
        }
    }

    /**
     * 展示无数据的提示
     */
    private void showNoDataPageView() {
        String msg = "";
        if (channelType == TouristType.MIMETOURIST) {
            msg = "暂无游客信息，请添加";
        } else if (channelType == TouristType.ORDERCONTACTS) {
            msg = "暂无联系人信息，请添加";
        } else if (channelType == TouristType.ORDERTOURIST) {
            msg = "暂无游客信息，请添加";
        }
        emptyView.setVisibility(View.VISIBLE);
        mErrorTv.setText(msg);
    }

    /**
     * 隐藏没有数据的界面
     */
    private void hideNoDataPageView() {
        if (emptyView != null && emptyView.getVisibility() == View.VISIBLE) {
            emptyView.setVisibility(View.GONE);
        }
    }

    /**
     * 展示错误页面
     *
     * @param arg1
     */
    protected void showNetErrorView(ViewGroup parent, int arg1) {
        final ViewGroup parents = parent;
        showErrorView(parent, ErrorCode.NETWORK_UNAVAILABLE == arg1 ?
                IActionTitleBar.ErrorType.NETUNAVAILABLE : IActionTitleBar.ErrorType.ERRORNET, "", "", "", new ErrorViewClick() {
            @Override
            public void onClick(View view) {
                hideErrorView(parents);
                mController.doUpdateVisitorInfo(CommonUseTouristActivity.this, uid);
                showLoadingView(getResources().getString(R.string.scenic_loading_notice));
            }
        });
    }

}
