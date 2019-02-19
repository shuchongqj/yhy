package com.quanyan.yhy.ui.servicerelease;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.quanyan.yhy.common.AnonyStatus;
import com.quanyan.yhy.common.ErrorCode;
import com.quanyan.yhy.eventbus.EvBusComment;
import com.quanyan.yhy.ui.base.utils.TCEventHelper;
import com.quanyan.yhy.ui.comment.controller.CommentController;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.view.CommentRatingBar;
import com.yhy.common.beans.net.model.comment.DimensionInfo;
import com.yhy.common.beans.net.model.comment.DimensionList;
import com.yhy.common.beans.net.model.tm.OrderRate;
import com.yhy.common.beans.net.model.tm.PostRateParam;
import com.yhy.common.beans.net.model.tm.RateDimension;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.AndroidUtils;
import com.yhy.common.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

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
public class OrderCommentNewActivity extends BaseActivity {
    @ViewInject(R.id.ll_comment)
    private LinearLayout ll_comment;//评分头部
    @ViewInject(R.id.btn_content)
    private TextView btn_content;//提交按钮
    @ViewInject(R.id.tv_comment_content)
    private TextView tv_comment_content;//提交按钮

    @ViewInject(R.id.num_comment)
    private TextView num_comment;
    private ArrayList<RateDimension> mSendDimensionInfos;
    private CommentController mController;
    private String mCommentType;
    private OrderRate mOrderRate;
    private PostRateParam mPostRateParam;
    private long mOrderId;
    private long mUserId;
    private boolean isAnony = false;//是否匿名

    public static void startOrderCommentNewActivity(Activity activity, long orderId, long userId, String type, int reqCode) {
        Intent intent = new Intent(activity, OrderCommentNewActivity.class);
        intent.putExtra(SPUtils.EXTRA_TYPE, type);
        intent.putExtra(SPUtils.EXTRA_ID, orderId);
        intent.putExtra(SPUtils.EXTRA_TAG_ID, userId);
        activity.startActivityForResult(intent,reqCode);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ViewUtils.inject(this);
        mController = new CommentController(this, mHandler);
        mCommentType = getIntent().getStringExtra(SPUtils.EXTRA_TYPE);
        if (mCommentType != null && mCommentType.equals("PROCESS")) {
            mCommentType = "CONSULT";
        }
        mUserId = getIntent().getLongExtra(SPUtils.EXTRA_TAG_ID, -1);
        mOrderId = getIntent().getLongExtra(SPUtils.EXTRA_ID, -1);
        mPostRateParam = new PostRateParam();
        if (!StringUtil.isEmpty(mCommentType)) {
            doNetDatas();
        }
        setListener();
    }

    private void setListener() {
        btn_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              submitComment();

            }
        });
        tv_comment_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                num_comment.setText("" + s.length() + "/200");
            }
        });
    }

    /**
     * 提交评价内容
     */
    private void submitComment() {
        TCEventHelper.onEvent(this, AnalyDataValue.COMMENT_PUBLISH, mCommentType);
        if (TimeUtil.isFastDoubleClick()) {
            return;
        }
        //封装提交评价的数据
        dataPostEncap();
        //提交评价
        mController.doPostRate(OrderCommentNewActivity.this, mPostRateParam);

    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.activity_send_comment_new, null);
    }

    @Override
    public View onLoadNavView() {
        BaseNavView mBaseNavView = new BaseNavView(this);
        mBaseNavView.setTitleText(R.string.label_post_comment);
        return mBaseNavView;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }

    private void doNetDatas() {
        mController.doGetRateDimensionList(OrderCommentNewActivity.this, mCommentType);
    }

    /**
     * 处理数据
     *
     * @param value
     */
    private void handlerData(List<DimensionInfo> value) {
        mSendDimensionInfos = new ArrayList<>();
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 1);
        lp.setMargins(20,0,20,0);
        if (value != null && value.size() > 0) {
            //initOrderRate();
            ll_comment.removeAllViews();
            for (int i = 0; i < value.size(); i++) {
                final RateDimension dimensionInfo = new RateDimension();
                CommentRatingBar commentRatingBar = new CommentRatingBar(this);
                commentRatingBar.setDesc(value.get(i).dimensionName);
                dimensionInfo.value = 5;
                commentRatingBar.setOnCommentRatingListener(new CommentRatingBar.OnCommentRatingListener() {
                    @Override
                    public void onCommentRating(Object bindObject, int ratingScore) {
                        dimensionInfo.value = ratingScore;
                    }
                });
                dimensionInfo.code = value.get(i).dimensionCode;
                mSendDimensionInfos.add(dimensionInfo);

                  View  view =  new View(OrderCommentNewActivity.this);
                 view.setBackgroundResource(R.color.light_gray);


                ll_comment.addView(view,lp);
                ll_comment.addView(commentRatingBar);

            }
        }
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        hideLoadingView();
        hideErrorView(null);
        switch (msg.what) {
            case ValueConstants.MSG_COMMENT_WRITE_INIT_OK:
                DimensionList result = (DimensionList) msg.obj;
                if (result != null) {
                    handlerData(result.dimensionList);
                }
                break;
            case ValueConstants.MSG_COMMENT_WRITE_INIT_KO:
                showErrorView(null, ErrorCode.NETWORK_UNAVAILABLE == msg.arg1 ?
                        IActionTitleBar.ErrorType.NETUNAVAILABLE : IActionTitleBar.ErrorType.ERRORNET, "", getString(R.string.error_view_network_loaderror_content), "", new ErrorViewClick() {
                    @Override
                    public void onClick(View view) {
                        doNetDatas();
                    }
                });
                //ToastUtil.showToast(WriteCommentActivity.this, StringUtil.handlerErrorCode(getApplicationContext(), msg.arg1));
                break;

            case ValueConstants.MSG_COMMENT_WRITE_SUBMIT_OK:
                Boolean isSuccess = (Boolean) msg.obj;
                if (isSuccess) {
                    ToastUtil.showToast(this, getString(R.string.label_comment_submit_success));
                    EventBus.getDefault().post(new EvBusComment());
                    setResult(RESULT_OK);
                    finish();
                } else {
                    ToastUtil.showToast(this, getString(R.string.label_comment_submit_fail));
                }
                break;
            case ValueConstants.MSG_COMMENT_WRITE_SUBMIT_KO:
                AndroidUtils.showToastCenter(OrderCommentNewActivity.this, StringUtil.handlerErrorCode(getApplicationContext(), msg.arg1));
                break;
        }
    }

    //封装提交评价的请求数据
    private void dataPostEncap() {
        if (mUserId == -1 || mOrderId == -1) {
            return;
        }

        if (mPostRateParam == null) {
            mPostRateParam = new PostRateParam();
        }
        if (mOrderRate == null) {
            mOrderRate = new OrderRate();
        }
        mOrderRate.bizOrderId = mOrderId;
        mOrderRate.rateDimensionList = mSendDimensionInfos;
        mOrderRate.anony = isAnony ? AnonyStatus.AVAILABLE : AnonyStatus.DELETED;
        String commentContent = tv_comment_content.getText().toString();
        if (!StringUtil.isEmpty(commentContent)) {
            //commentContent = getString(R.string.comment_default_content);
            mOrderRate.content = commentContent;
        }
        mPostRateParam.userId = mUserId;
        mPostRateParam.mainOrderRate = mOrderRate;
    }


}
