package com.quanyan.yhy.ui.consult.view;

import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.mogujie.tt.ui.activity.MessageActivity;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.R;
import com.quanyan.yhy.net.NetManager;
import com.quanyan.yhy.net.lsn.OnResponseListener;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.yhy.common.beans.net.model.comment.DimensionInfo;
import com.yhy.common.beans.net.model.comment.DimensionList;
import com.yhy.common.beans.net.model.tm.OrderRate;
import com.yhy.common.beans.net.model.tm.PostRateParam;
import com.yhy.common.beans.net.model.tm.RateDimension;
import com.yhy.common.constants.ConsultContants;
import com.yhy.common.utils.SPUtils;
import com.yhy.router.YhyRouter;
import com.yhy.service.IUserService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:MessageConsultComment
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:邓明佳
 * Date:2016/7/11
 * Time:17:13
 * Version 1.0
 */
public class MessageConsultComment extends LinearLayout implements View.OnClickListener {
    EditText mEditText;
    TextView mWordsCount, mCommit;
    LinearLayout mStarLayout;

    @Autowired
    IUserService userService;

    public MessageConsultComment(Context context, AttributeSet attrs) {
        super(context, attrs);
        YhyRouter.getInstance().inject(this);
    }

    List<DimensionInfo> dimensionList;
    Handler handler = new Handler();

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mEditText = (EditText) findViewById(R.id.edit_text);
        mCommit = (TextView) findViewById(R.id.tv_commit);
        mWordsCount = (TextView) findViewById(R.id.number_words);
        mStarLayout = (LinearLayout) findViewById(R.id.layout_star);
        mCommit.setOnClickListener(this);
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mWordsCount.setText(getContext().getString(R.string.words_count_limit_200, mEditText.getText().length()));
            }
        });
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mCommit.getId()) {
            if (mStarLayout.getChildCount() == 0) {
                return;
            }
            mCommit.setEnabled(false);
            PostRateParam param = new PostRateParam();
            param.userId = userService.getLoginUserId();
            param.mainOrderRate = new OrderRate();
            param.mainOrderRate.bizOrderId = orderId;
            param.mainOrderRate.anony = "DELETED";
            param.mainOrderRate.content = mEditText.getText().toString();
            param.mainOrderRate.rateDimensionList = new ArrayList<>();
            for (int i = 0; i < mStarLayout.getChildCount(); i++) {
                MessageConsultCommentStars stars = (MessageConsultCommentStars) mStarLayout.getChildAt(i);
                RateDimension dimension = new RateDimension();
                dimension.code = stars.getCode();
                dimension.value = stars.getStarCount();
                param.mainOrderRate.rateDimensionList.add(dimension);
            }
            NetManager.getInstance(getContext()).doPostRate(param, new OnResponseListener<Boolean>() {
                @Override
                public void onComplete(boolean isOK, Boolean result, int errorCode, String errorMsg) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showToast(getContext(), R.string.thanks_your_comment);
                            if (messageActivity != null) {
                                if (messageActivity instanceof MessageActivity) {
                                    if (isNeedFinishAc) {
                                        messageActivity.finish();
                                    } else {
                                        mCommit.setEnabled(true);
                                        ((MessageActivity) messageActivity).getConsultInfo();
                                    }
                                }
                            }
                        }
                    });
                }

                @Override
                public void onInternError(int errorCode, String errorMessage) {
                    final int code = errorCode;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            mCommit.setEnabled(true);
                            ToastUtil.showToast(getContext(), StringUtil.handlerErrorCode(getContext(), code));
                        }
                    });
                }
            });
        }
    }


    /**
     * 订单id 必填
     */
    public long bizOrderId;

    /**
     * 是否匿名 AVAILABLE:是  DELETED：否
     */
    public String anony;

    /**
     * 评价内容
     */
    public String content;

    /**
     * 图片列表
     */
    public List<String> picUrlList;
    /**
     * 评分维度列表
     */
    public List<RateDimension> rateDimensionList;

    BaseActivity messageActivity;
    long orderId;
    boolean isNeedFinishAc;

    public void showComment(BaseActivity messageActivity, long bizOrderId, boolean isNeedFinishAc) {
        setVisibility(View.VISIBLE);
        mCommit.setEnabled(true);
        this.orderId = bizOrderId;
        this.messageActivity = messageActivity;
        this.isNeedFinishAc = isNeedFinishAc;
        if (dimensionList == null || dimensionList.size() == 0) {
            requestDimension();
        } else {
            buildDimension();
        }

    }

    private void requestDimension() {
        NetManager.getInstance(getContext()).doGetRateDimensionList(ConsultContants.CONSULT, new OnResponseListener<DimensionList>() {
            @Override
            public void onInternError(int errorCode, String errorMessage) {
                final int code = errorCode;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showToast(getContext(), StringUtil.handlerErrorCode(getContext(), code));
                        if (messageActivity != null) messageActivity.finish();
                    }
                });
            }

            @Override
            public void onComplete(boolean isOK, DimensionList result, int errorCode, String errorMsg) {
                if (isOK && result != null) {
                    dimensionList = result.dimensionList;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            buildDimension();
                        }
                    });
                }
            }
        });
    }

    private void buildDimension() {
        mStarLayout.removeAllViews();
        if (dimensionList == null || dimensionList.size() == 0) return;
        for (int i = 0; i < dimensionList.size(); i++) {
            MessageConsultCommentStars stars = (MessageConsultCommentStars) LayoutInflater.from(getContext()).inflate(R.layout.view_message_consult_comment_stars, null);
            stars.setDimension(dimensionList.get(i));
            mStarLayout.addView(stars);
        }

    }
}
