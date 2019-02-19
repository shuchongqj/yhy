package com.quanyan.yhy.ui.servicerelease;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.harwkin.nb.camera.TimeUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.baseenum.IActionTitleBar;
import com.quanyan.base.util.LocalUtils;
import com.quanyan.base.util.StringUtil;
import com.quanyan.base.view.BaseNavView;
import com.quanyan.base.yminterface.ErrorViewClick;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.AnalyDataValue;
import com.quanyan.yhy.common.CommentType;
import com.quanyan.yhy.common.ConsultingState;
import com.quanyan.yhy.common.ErrorCode;
import com.quanyan.yhy.common.ItemType;
import com.quanyan.yhy.eventbus.EvBusNativeGoBack;
import com.quanyan.yhy.ui.base.utils.DialogUtil;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.base.utils.TCEventHelper;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.servicerelease.controller.ManageInfoController;
import com.quanyan.yhy.ui.servicerelease.view.ExpertOrderDetailTopView;
import com.yhy.common.beans.net.model.tm.ProcessOrder;
import com.yhy.common.beans.net.model.user.UserInfo;
import com.yhy.common.constants.IntentConstants;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.CommonUtil;
import com.yhy.common.utils.SPUtils;
import com.yhy.imageloader.ImageLoadManager;
import com.yhy.service.IUserService;

import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Created with Android Studio.
 * Title:***Acitivity
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:王东旭
 * Date:2016/6/1
 * Time:16:46
 * Version 2.0
 */
public class ExpertOrderDetailActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.detail_top_view)
    private ExpertOrderDetailTopView mDetailTopView;
    @ViewInject(R.id.tv_consulting_service_status)
    private TextView mTvConsultingServiceStatusDesc;
    @ViewInject(R.id.rl_consulting_service_status_bar)
    private RelativeLayout mRLStatusBar;

    @ViewInject(R.id.lin_expert_name)
    private LinearLayout mLinExpertName;

    //商品详情介绍
    @ViewInject(R.id.lin_user_name)
    private LinearLayout mLinUserName;


    @ViewInject(R.id.iv_user_pic)
    private ImageView mIvUserPic;
    @ViewInject(R.id.tv_user_name)
    private TextView mTvUserName;
    @ViewInject(R.id.user_right_direct)
    private ImageView mUserRightDirect;

    @ViewInject(R.id.iv_detail_goods_img)
    private ImageView mIvDetailGoodsImg;

    @ViewInject(R.id.tv_goods_describe)
    private TextView mTvGoodsDescribe;
    @ViewInject(R.id.tv_detail_service_time)
    private TextView mTvDetailServiceTime;
    @ViewInject(R.id.tv_detail_service_area)
    private TextView mTvDetailServiceArea;
    //限免
    @ViewInject(R.id.tv_detail_goods_price)
    private TextView mTvDetailGoodsPrice;
    @ViewInject(R.id.tv_service_free_price)
    private TextView mTvFreePrice;

    @ViewInject(R.id.iv_expert_pic)
    private ImageView mIvExpertPic;
    @ViewInject(R.id.tv_goods_name)
    private TextView mTvGoodsName;
    @ViewInject(R.id.right_direct)
    private ImageView mRightDirect;

    @ViewInject(R.id.tv_detail_order_price)
    private TextView mTvDetailOrderPrice;
    @ViewInject(R.id.tv_detail_real_price)
    private TextView mTvDetailRealPrice;
    @ViewInject(R.id.tv_detail_travel_day)
    private TextView mTvDetailTravelDay;
    @ViewInject(R.id.tv_detail_travel_people)
    private TextView mTvDetailTravelPeople;
    @ViewInject(R.id.tv_detail_travel_area)
    private TextView mTvDetailTravelArea;

    @ViewInject(R.id.tv_detail_ask_lebal)
    private TextView mTvDetailAskLebal;
    @ViewInject(R.id.tv_detail_ask_content)
    private TextView mTvDetailAskContent;
    @ViewInject(R.id.tv_detail_ask_content_describe)
    private TextView mTvDetailAskContentDescribe;

    @ViewInject(R.id.tv_order_time)
    private TextView mTvOrderTime;

    @ViewInject(R.id.tv_order_id)
    private TextView mTvOrderId;

    @ViewInject(R.id.lin_to_goods_detail)
    private LinearLayout lin_to_goods_detail;
    @ViewInject(R.id.ll_merhchant_im)
    private LinearLayout mllCustomService;
    @ViewInject(R.id.ll_merhchant_hotline)
    private LinearLayout mllHotLine;
    @ViewInject(R.id.line)
    private View line;

    @ViewInject(R.id.tv_merhchant_im)
    private TextView mTVMerhchantIm;

    @ViewInject(R.id.tv_merhchant_hotline)
    private TextView mTVMerhchantHotline;

    @ViewInject(R.id.btn_receive_order)
    private Button mBtnReceiveOrder;

    @ViewInject(R.id.lin_manage_detail_to)
    private LinearLayout lin_manage_detail_to;
    private int state;
    long processOrderId;
    private ProcessOrder mProcessOrder;

    @Autowired
    IUserService userService;

    ManageInfoController mManageInfoController;
    private boolean isPost;

    public static void startExpertOrderDetailActivity(Context context, int type, long processOrderId) {//1是卖家  达人
        Intent intent = new Intent();
        intent.setClass(context, ExpertOrderDetailActivity.class);
        intent.putExtra(SPUtils.EXTRA_TYPE, type);
        intent.putExtra(SPUtils.EXTRA_ID, processOrderId);
        context.startActivity(intent);
    }

    public static void startExpertOrderDetailActivityForResult(Activity context, int type, long processOrderId) {//1是卖家  达人
        Intent intent = new Intent();
        intent.setClass(context, ExpertOrderDetailActivity.class);
        intent.putExtra(SPUtils.EXTRA_TYPE, type);
        intent.putExtra(SPUtils.EXTRA_ID, processOrderId);
        context.startActivityForResult(intent, ExpertOrderListActivity.REQ_CODE_REFRESH_ORDER_LIST);
    }

    boolean isCancle;
    public void setTopView() {
        isCancle = false;
        String stateName = getString(R.string.label_service_status_waiting_pay);
        switch (mProcessOrder.processOrderStatus) {
            case ValueConstants.ORDER_STATUS_WAITING_PAY:
                if (checkPageType()) {
                    stateName = getString(R.string.label_service_status_waiting_pay_sell);
                } else {
                    stateName = getString(R.string.label_service_status_waiting_pay);
                }
                state = ConsultingState.WAITING_PAY;
                break;
            case ValueConstants.ORDER_STATUS_CONSULT_IN_QUEUE:
                if (checkPageType()) {
                    stateName = getString(R.string.label_service_status_consult_in_queue_sell);
                } else {
                    stateName = getString(R.string.label_service_status_consult_in_queue);
                }
                state = ConsultingState.CONSULT_IN_QUEUE;
                break;
            case ValueConstants.ORDER_STATUS_CONSULT_IN_CHAT:
                if (checkPageType()) {
                    stateName = getString(R.string.label_service_status_consult_in_chat_sell);
                } else {
                    stateName = getString(R.string.label_service_status_consult_in_chat);
                }

                state = ConsultingState.CONSULT_IN_CHAT;
                break;
            case ValueConstants.ORDER_STATUS_FINISH:
                if (checkPageType()) {
                    stateName = getString(R.string.label_service_status_finish_sell);
                } else {
                    stateName = getString(R.string.label_service_status_finish);
                }
                isCancle = true;
                state = ConsultingState.FINISH;
                break;
            case ValueConstants.ORDER_STATUS_RATED:
                if (checkPageType()) {
                    stateName = getString(R.string.label_service_status_rated_sell);
                } else {
                    stateName = getString(R.string.label_service_status_rated);
                }

                state = ConsultingState.RATED;
                break;
            case ValueConstants.ORDER_STATUS_CANCEL:
                if (checkPageType()) {
                    stateName = getString(R.string.label_service_status_cancel_sell);
                } else {
                    stateName = getString(R.string.label_service_status_cancel);
                }
                state = ConsultingState.CANCEL;

        }
        //如果是取消状态显示白条的取消UI,否则显示状态
        if (state == ConsultingState.CANCEL) {
            mRLStatusBar.setVisibility(View.GONE);
            mTvConsultingServiceStatusDesc.setVisibility(View.VISIBLE);
            return;
        } else {
            mRLStatusBar.setVisibility(View.VISIBLE);
            mTvConsultingServiceStatusDesc.setVisibility(View.GONE);
        }

        mDetailTopView.setDescribeInfo("" + stateName);
        mDetailTopView.setCurrentConsultState(state);
//        mDetailTopView.setOrderState(state, this, isCancle);

        if ((state == ConsultingState.WAITING_PAY || state == ConsultingState.CONSULT_IN_QUEUE) && !checkPageType()) {
            mBtnReceiveOrder.setVisibility(View.VISIBLE);
            mBtnReceiveOrder.setText(R.string.cancel);
        } else if (mProcessOrder.processOrderStatus.equals(ValueConstants.ORDER_STATUS_FINISH) && !checkPageType()) {
            mBtnReceiveOrder.setVisibility(View.VISIBLE);
            mBtnReceiveOrder.setText(R.string.label_btn_go_comment);
        } else if (mProcessOrder.processOrderStatus.equals(ValueConstants.ORDER_STATUS_RATED)) {
            mBtnReceiveOrder.setVisibility(View.VISIBLE);
            mBtnReceiveOrder.setText(R.string.label_btn_comment_list);
        }
        if ((state == ConsultingState.RATED || state == ConsultingState.FINISH) /*&& checkPageType()*/) {
            lin_manage_detail_to.setVisibility(View.VISIBLE);
        } else {
            lin_manage_detail_to.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.activity_expert_order_detail, null);
    }

    BaseNavView mBaseNavView;

    @Override
    public View onLoadNavView() {
        mBaseNavView = new BaseNavView(this);
        mBaseNavView.setTitleText(R.string.order_details);
        mBaseNavView.setLeftClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNeedChangeStatus) {
                    setResult(RESULT_OK);
                }
                finish();
            }
        });
        return mBaseNavView;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }


    @Override
    protected void initView(Bundle savedInstanceState) {
        ViewUtils.inject(this);

        mBtnReceiveOrder.setOnClickListener(this);
        init();
        initData();
        setListener();
    }

    private void setListener() {
        mLinUserName.setOnClickListener(this);
        // 咨询订单没有联系商家
        mllCustomService.setVisibility(View.GONE);
        line.setVisibility(View.GONE);
        mllCustomService.setOnClickListener(this);
        mllHotLine.setOnClickListener(this);
        lin_manage_detail_to.setOnClickListener(this);
        lin_to_goods_detail.setOnClickListener(this);
        mLinExpertName.setOnClickListener(this);
    }


    private void init() {
        if (!checkPageType()) {
            mBtnReceiveOrder.setVisibility(View.GONE);
        }
        processOrderId = getIntent().getExtras().getLong(SPUtils.EXTRA_ID);


        if (checkPageType()) {
            mLinExpertName.setVisibility(View.VISIBLE);

        } else {
            mLinUserName.setVisibility(View.VISIBLE);

        }
        mBtnReceiveOrder.setVisibility(View.GONE);
    }

    private void initData() {
        mTVMerhchantIm.setText(getString(R.string.custom_service));
//        mTVMerhchantHotline.setText(getString(R.string.custom_hotline));
        showLoadingView(null);
        if (mManageInfoController == null) {
            mManageInfoController = new ManageInfoController(ExpertOrderDetailActivity.this, mHandler);
        }
        if (checkPageType()) {
            mLinExpertName.setVisibility(View.VISIBLE);
        } else {
            mLinUserName.setVisibility(View.VISIBLE);
        }
        mManageInfoController.doGetExpertOrderDetail(processOrderId);
    }

    /**
     * 是否为卖家订单详情
     *
     * @return
     */
    public boolean checkPageType() {//达人返回true
        int type = getIntent().getExtras().getInt(SPUtils.EXTRA_TYPE);
        if (type == 1) {
            return true;
        } else {
            return false;
        }
    }

    //咨询服务订单取消对话框
    private Dialog mCancelServiceDlg;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_receive_order:
                if (getString(R.string.cancel).equals(mBtnReceiveOrder.getText())) {
                    if (mCancelServiceDlg == null) {
                        mCancelServiceDlg = DialogUtil.showMessageDialog(this,
                                null,
                                getString(R.string.label_dlg_msg_cancel_consult_service),
                                getString(R.string.cancel),
                                getString(R.string.label_btn_ok),
                                null,
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        showLoadingView("");
                                        mCancelServiceDlg.dismiss();
                                        mManageInfoController.cancelOrderNewInfo(mProcessOrder.processOrderId);
                                    }
                                });
                    }
                    mCancelServiceDlg.show();
                } else if (getString(R.string.label_btn_go_comment).equals(mBtnReceiveOrder.getText())) {
                    if (mProcessOrder.processOrderItem != null && mProcessOrder.processOrderItem.bizOrder != null) {
                        NavUtils.gotoOrderCommentNewActivity(this, mProcessOrder.processOrderItem.bizOrder.bizOrderId,
                                userService.getLoginUserId(),
                                mProcessOrder.processOrderType,
                                IntentConstants.REQUEST_CODE_COMMENT);
                    }
                } else if (getString(R.string.label_btn_comment_list).equals(mBtnReceiveOrder.getText())) {
                    if (mProcessOrder.processOrderItem != null && mProcessOrder.processOrderItem.bizOrder != null) {
                        NavUtils.gotoCommentFragmentActivity(this, mProcessOrder.itemId, CommentType.CONSULT);

                    }
                }
                break;
            case R.id.lin_user_name:
                if (checkPageType()) {
                    if (mProcessOrder.buyerInfo != null) {
                        NavUtils.gotoMasterHomepage(this, mProcessOrder.buyerInfo.userId);
                    }
                } else {
                    if (mProcessOrder.consultUserInfo != null) {
                        NavUtils.gotoMasterHomepage(this, mProcessOrder.consultUserInfo.userId);
                    } else if (mProcessOrder.sellerInfo != null) {
                        NavUtils.gotoMasterHomepage(this, mProcessOrder.sellerInfo.userId);
                    }
                }
                break;

            case R.id.ll_merhchant_im:
                if (SPUtils.getServiceUID(this) > 0) {
                    tcEvent(1);
                    NavUtils.gotoMessageActivity(ExpertOrderDetailActivity.this, (int) SPUtils.getServiceUID(this));
                } else {
                    ToastUtil.showToast(this, getString(R.string.label_toast_no_config_service_uid));
                }
                break;
            case R.id.ll_merhchant_hotline:
                if (mProcessOrder == null)
                    return;
                tcEvent(2);
                LocalUtils.call(ExpertOrderDetailActivity.this, SPUtils.getServicePhone(this));
//                LocalUtils.call(ServiceCenterActivity.this, SPUtils.getServicePhone(this));

                break;
            case R.id.lin_manage_detail_to:
                if (checkPageType()) {
                    if (mProcessOrder.buyerInfo != null) {
                        NavUtils.gotoMessageActivity(this, mProcessOrder.buyerInfo.userId, null, mProcessOrder.itemId);
                    }
                } else {
                    if (mProcessOrder.consultUserInfo != null) {
                        NavUtils.gotoMessageActivity(this, mProcessOrder.consultUserInfo.userId, null, mProcessOrder.itemId);
                    } else if (mProcessOrder.sellerInfo != null) {
                        NavUtils.gotoMessageActivity(this, mProcessOrder.sellerInfo.userId, null, mProcessOrder.itemId);
                    }
                }
                break;
            case R.id.lin_to_goods_detail:
                NavUtils.gotoProductDetail(this, ItemType.MASTER_CONSULT_PRODUCTS, mProcessOrder.itemId, "服务管理详情");
                break;
            case R.id.lin_expert_name:
                if (checkPageType()) {
                    if (mProcessOrder.buyerInfo != null) {
                        NavUtils.gotoMasterHomepage(this, mProcessOrder.buyerInfo.userId);
                    }
                } else {
                    if (mProcessOrder.sellerInfo != null) {
                        NavUtils.gotoMasterHomepage(this, mProcessOrder.sellerInfo.userId);
                    }
                }
                break;
        }
    }

    private void tcEvent(int i) {
        Map map = new HashMap();
        map.put(AnalyDataValue.KEY_BID, mProcessOrder.processOrderId);
        switch (i) {
            case 1:
                map.put(AnalyDataValue.KEY_TYPE, "1");
                TCEventHelper.onEvent(this, AnalyDataValue.ORDER_DETAIL_SERVICE, map);
                break;
            case 2:
                map.put(AnalyDataValue.KEY_TYPE, "2");
                TCEventHelper.onEvent(this, AnalyDataValue.ORDER_DETAIL_SERVICE, map);
                break;
        }

    }

    private boolean isNeedChangeStatus = false;

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        hideLoadingView();
        hideErrorView(null);
        switch (msg.what) {
            case ValueConstants.GET_ORDER_DETAILS_SUCCESS:
                mDetailTopView.setVisibility(View.VISIBLE);
                mProcessOrder = (ProcessOrder) msg.obj;
                if (mProcessOrder != null) {
                    updateView();
                } else {
                    showErrorPage(msg.arg2, null);
                }
                break;
            case ValueConstants.GET_ORDER_DETAILS_FAIL:
                showErrorView(null, ErrorCode.NETWORK_UNAVAILABLE == msg.arg1 ?
                        IActionTitleBar.ErrorType.NETUNAVAILABLE : IActionTitleBar.ErrorType.ERRORNET, "", "", "", new ErrorViewClick() {
                    @Override
                    public void onClick(View view) {
                        getOrderDetail();
                    }
                });
                break;
            case ValueConstants.CANCLE_ORDER_SUCCESS:
                isNeedChangeStatus = true;
                mBtnReceiveOrder.setVisibility(View.GONE);
                ToastUtil.showToast(ExpertOrderDetailActivity.this, getString(R.string.label_toast_cancel_order_success));
                //TODO 修改头部的状态
                mRLStatusBar.setVisibility(View.GONE);
                mTvConsultingServiceStatusDesc.setVisibility(View.VISIBLE);
                mDetailTopView.resetCancelDraw(this);
                break;
            case ValueConstants.CANCLE_ORDER_FAIL:
                ToastUtil.showToast(ExpertOrderDetailActivity.this, StringUtil.handlerErrorCode(this, msg.arg1));
                break;
            case MSG_GET_ORDER_DETAIL:
                getOrderDetail();
                break;
        }
    }

    /**
     * 获取订单详情
     */
    private void getOrderDetail() {

        mManageInfoController.doGetExpertOrderDetail(processOrderId);

    }

    public void updateString() {
        if (mProcessOrder.sellerInfo == null) {
            mProcessOrder.sellerInfo = new UserInfo();
        }
        if (TextUtils.isEmpty(mProcessOrder.sellerInfo.nickname)) {
            mProcessOrder.sellerInfo.nickname = "";
        }
        if (TextUtils.isEmpty(mProcessOrder.processOrderContent.demandDetail)) {
            mProcessOrder.processOrderContent.demandDetail = "";
        }
        if (TextUtils.isEmpty(mProcessOrder.processOrderContent.destination)) {
            mProcessOrder.processOrderContent.destination = "";
        }
    }

    public void updateView() {
        setTopView();
        updateString();
        if (checkPageType()) {
            if (mProcessOrder.buyerInfo != null) {
                loadImage(mIvUserPic, mProcessOrder.buyerInfo.imgUrl, R.mipmap.icon_default_avatar, 128,128,true);
                mTvUserName.setText("" + mProcessOrder.buyerInfo.nick);
                mTvGoodsName.setText("" + mProcessOrder.buyerInfo.nick);
            }
        } else {
            if (mProcessOrder.consultUserInfo != null) {
                loadImage(mIvUserPic, mProcessOrder.consultUserInfo.imgUrl, R.mipmap.icon_default_avatar,  128,128,true);
                mTvUserName.setText("" + mProcessOrder.consultUserInfo.nick);
                mTvGoodsName.setText("" + mProcessOrder.consultUserInfo.nick);
            } else if (mProcessOrder.sellerInfo != null) {
                loadImage(mIvUserPic, mProcessOrder.sellerInfo.imgUrl, R.mipmap.icon_default_avatar,  128,128,true);
                mTvUserName.setText("" + mProcessOrder.sellerInfo.nick);
                mTvGoodsName.setText("" + mProcessOrder.sellerInfo.nick);
            }
        }

        mTvGoodsDescribe.setText("" + mProcessOrder.itemTitle);

        loadImage(mIvDetailGoodsImg, mProcessOrder.itemPic, R.mipmap.icon_default_150_150, 300,300, false);
        mTvDetailServiceTime.setText("" + mProcessOrder.serveTime / 60 + "分钟");
//        mTvDetailServiceArea.setText(String.format(this.getString(R.string.label_consulting_service_area),StringUtil.getServiceArea(mProcessOrder.itemDestination)));
        if (mProcessOrder.usePointNum == 0) {
            mTvFreePrice.setText(R.string.label_free_now);
        } else {
            mTvFreePrice.setText(mProcessOrder.usePointNum/10 + "积分");
        }
        mTvDetailGoodsPrice.setText("" + mProcessOrder.totalFee/10 + "积分");
        mTvDetailGoodsPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        if (checkPageType()) {
            loadImage(mIvExpertPic, mProcessOrder.buyerInfo.imgUrl, R.mipmap.icon_default_avatar, 128,128,true);
        } else {
            loadImage(mIvExpertPic, mProcessOrder.sellerInfo.imgUrl, R.mipmap.icon_default_avatar, 128,128, true);
        }

        mTvDetailOrderPrice.setText(mProcessOrder.totalFee/10 + "积分");
        mTvDetailRealPrice.setText(mProcessOrder.actualTotalFee/10 + "积分");

        if (mProcessOrder.processOrderContent.travelDaysStr == null) {
            mTvDetailTravelDay.setText("无");
        } else {

            mTvDetailTravelDay.setText("" + mProcessOrder.processOrderContent.travelDaysStr);
        }

        if (mProcessOrder.processOrderContent.personNumStr == null) {
            mTvDetailTravelPeople.setText("无");
        } else {
            mTvDetailTravelPeople.setText("" + mProcessOrder.processOrderContent.personNumStr);
        }

        if (mProcessOrder.processOrderContent != null && !StringUtil.isEmpty(mProcessOrder.processOrderContent.destination)) {
            mTvDetailTravelArea.setText(mProcessOrder.processOrderContent.destination);
        } else {
            mTvDetailTravelArea.setText("无");
        }

        if (mProcessOrder.processOrderContent.demandDescription != null) {
            mTvDetailAskContent.setText(StringUtil.getServiceArea(mProcessOrder.processOrderContent.demandDescription));
        } else {
            mTvDetailAskContent.setText("无");
        }

        if (!TextUtils.isEmpty(mProcessOrder.processOrderContent.demandDetail)) {
            mTvDetailAskContentDescribe.setText("" + mProcessOrder.processOrderContent.demandDetail);
        } else {
            mTvDetailAskContentDescribe.setText("无");
        }
        mTvOrderTime.setText("下单时间: " + TimeUtil.getPreciseDString(mProcessOrder.createTime));
        if(mProcessOrder.processOrderItem != null && mProcessOrder.processOrderItem.bizOrder != null) {
            mTvOrderId.setText("订单编号: " + mProcessOrder.processOrderItem.bizOrder.bizOrderId);
        }

    }

    public void loadImage(ImageView imgView, String imgSource, int defaultImg, int width,int height,boolean isCircle) {
        int RadiusPixels = 0;
        if (isCircle) {
            RadiusPixels = 180;
            ImageLoadManager.loadCircleImage(CommonUtil.getImageFullUrl(imgSource), defaultImg, width, height, imgView);
        }else {
            ImageLoadManager.loadImage((imgSource), defaultImg, width, height, imgView);
        }
//        BaseImgView.loadimg(imgView,
//                imgSource,
//                defaultImg,
//                defaultImg,
//                defaultImg,
//                null,
//                width,
//                height,
//                RadiusPixels);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (isNeedChangeStatus) {
            setResult(RESULT_OK);
        }
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        if (!isPost) {
            isPost = true;
            EventBus.getDefault().post(new EvBusNativeGoBack());
        }
    }

    private final static int MSG_GET_ORDER_DETAIL = 0x1001;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == IntentConstants.REQUEST_CODE_COMMENT) {
                //TODO Because the server modify the comment status is async,we should request order detail after 2 seconds
                isNeedChangeStatus = true;
                // showLoadingView("");
                // mHandler.sendEmptyMessageDelayed(MSG_GET_ORDER_DETAIL, 2000);
                getOrderDetail();
            }
        }
    }
}
