package com.quanyan.yhy.ui.comment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.baseenum.IActionTitleBar;
import com.quanyan.base.view.BaseNavView;
import com.quanyan.base.yminterface.ErrorViewClick;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.CommentType;
import com.quanyan.yhy.common.ErrorCode;
import com.quanyan.yhy.service.controller.OrderController;
import com.quanyan.yhy.ui.adapter.base.BaseAdapterHelper;
import com.quanyan.yhy.ui.adapter.base.QuickAdapter;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.yhy.common.beans.net.model.tm.OrderDetailResult;
import com.yhy.common.beans.net.model.tm.TmBizOrder;
import com.yhy.common.beans.net.model.tm.TmDetailOrder;
import com.yhy.common.beans.net.model.tm.TmMainOrder;
import com.yhy.common.constants.IntentConstants;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.SPUtils;
import com.yhy.service.IUserService;

import java.util.List;

public class GoodsCommentListActivity extends BaseActivity {

    BaseNavView mBaseNavView;
    @ViewInject(R.id.goods_comment_listview)
    private ListView mListView;
    private QuickAdapter<TmDetailOrder> goodsAdapter;
    private OrderController controller;
    private long mOrderId;
    private long mUserId;
    private String mCommentType;
    private boolean isComment = false;

    @Autowired
    IUserService userService;


    public static void startGoodsCommentListActivity(Activity activity, long orderId, long userId, String type, int reqCode) {
        Intent intent = new Intent(activity, GoodsCommentListActivity.class);
        intent.putExtra(SPUtils.EXTRA_TYPE, type);
        intent.putExtra(SPUtils.EXTRA_ID, orderId);
        intent.putExtra(SPUtils.EXTRA_TAG_ID, userId);
        activity.startActivityForResult(intent, reqCode);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        hideLoadingView();
        hideErrorView(null);
        switch (msg.what) {
            case ValueConstants.MSG_GET_GOOD_RATE_LIST_OK:
                if (msg.obj != null) {
                    OrderDetailResult mOrderDetailResult = (OrderDetailResult) msg.obj;
                    updateUi(mOrderDetailResult);
                }
                break;
            case ValueConstants.MSG_GET_GOOD_RATE_LIST_KO:
                showErrorView(null, ErrorCode.NETWORK_UNAVAILABLE == msg.arg1 ?
                        IActionTitleBar.ErrorType.NETUNAVAILABLE : IActionTitleBar.ErrorType.ERRORNET, "", "", "", new ErrorViewClick() {
                    @Override
                    public void onClick(View view) {
                        GetGoodRateList(mOrderId);
                    }
                });

                break;
        }
    }

    public void updateUi(OrderDetailResult mOrderDetailResult) {
        final TmMainOrder mTmMainOrder = mOrderDetailResult.mainOrder;
        if (mTmMainOrder != null) {
            List<TmDetailOrder> tmDetailOrders = mTmMainOrder.detailOrders;
            if (tmDetailOrders != null && tmDetailOrders.size() > 0) {
                mListView.setAdapter(goodsAdapter = new QuickAdapter<TmDetailOrder>(this, R.layout.item_goods_comment) {
                    @Override
                    protected void convert(BaseAdapterHelper helper, final TmDetailOrder item) {
                        helper.setImageUrl(R.id.image, item.itemPic, 300, 300, R.mipmap.icon_default_150_150);
                        helper.setText(R.id.tv_commentgoods_title, item.itemTitle);
                        TmBizOrder mTmBizOrder = item.bizOrder;
                        if (mTmBizOrder != null) {
                            String orderStatus = item.bizOrder.orderStatus;
                            if (ValueConstants.ORDER_STATUS_RATED.equals(orderStatus)) {
                                helper.setText(R.id.tv_goto_comments,getString(R.string.label_btn_comment_list));
                                helper.setOnClickListener(R.id.tv_goto_comments, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        NavUtils.gotoCommentFragmentActivity(GoodsCommentListActivity.this, item.itemId, CommentType.POINT);
                                    }
                                });

                            } else if (ValueConstants.ORDER_STATUS_NOT_RATE.equals(orderStatus)) {
                                helper.setText(R.id.tv_goto_comments, getString(R.string.label_btn_go_comment));
                                helper.setOnClickListener(R.id.tv_goto_comments, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        NavUtils.gotoWriteCommentAcitivty(GoodsCommentListActivity.this, item.bizOrder.bizOrderId, userService.getLoginUserId(), OrderController.getOrderTypeStringForComment(item.bizOrder.orderType), IntentConstants.REQUEST_CODE_COMMENT);
                                    }
                                });
                            }

                        }


                    }
                });
                goodsAdapter.replaceAll(tmDetailOrders);
            } else {
                showNoDataPage();
                //没有数据的显示
                goodsAdapter.clear();
            }

        }


    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ViewUtils.inject(this);

        if (controller == null) {
            controller = new OrderController(this, mHandler);
        }


        mBaseNavView.setLeftClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isComment) {
                    setResult(RESULT_OK);
                }
                finish();
            }
        });
        mCommentType = getIntent().getStringExtra(SPUtils.EXTRA_TYPE);
        mOrderId = getIntent().getLongExtra(SPUtils.EXTRA_ID, -1);
        mUserId = getIntent().getLongExtra(SPUtils.EXTRA_TAG_ID, -1);
        GetGoodRateList(mOrderId);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            if (isComment) {
                setResult(RESULT_OK);
            }
            finish();
        }
        return true;
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.activity_goods_comment_list, null);
    }

    @Override
    public View onLoadNavView() {
        mBaseNavView = new BaseNavView(this);
        mBaseNavView.setTitleText("评价晒单");
        return mBaseNavView;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }

    public void GetGoodRateList(long orderId) {
        showLoadingView(getString(R.string.loading_text));
        controller.doQueryRateBuyOrder(GoodsCommentListActivity.this, orderId);
    }

    private void showNoDataPage() {
        showErrorView(mListView, IActionTitleBar.ErrorType.EMPTYVIEW, getString(R.string.label_nodata_wonderful_play_list), getString(R.string.label_nodata_wonderfulplay_list_message), "", null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == IntentConstants.REQUEST_CODE_COMMENT) {
                isComment = true;
                GetGoodRateList(mOrderId);
            }
        }
    }
}
