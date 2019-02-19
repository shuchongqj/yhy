package com.quanyan.yhy.ui.consult.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quanyan.yhy.R;
import com.quanyan.yhy.common.AnalyDataValue;
import com.quanyan.yhy.common.ItemType;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.base.utils.TCEventHelper;
import com.yhy.common.beans.net.model.tm.ProcessState;
import com.yhy.common.beans.net.model.trip.ShortItem;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created with Android Studio.
 * Title:MessageConsultBottomView
 * Description:聊天页面咨询底部视图
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:邓明佳
 * Date:2016/7/11
 * Time:9:30
 * Version 1.0
 */
public class MessageConsultBottomView extends LinearLayout {
    TextView mCancel, mCommit, mTitle;
    Handler handler = new Handler();

    public MessageConsultBottomView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MessageConsultBottomView(Context context) {
        super(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mCancel = (TextView) findViewById(R.id.tv_cancel);
        mCommit = (TextView) findViewById(R.id.tv_commit);
        mTitle = (TextView) findViewById(R.id.content);
    }

//    public void changeModePay(boolean isMaster) {
//        long price = 0;
//        mCommit.setText(R.string.consult_now);
//        mCommit.setVisibility(View.VISIBLE);
//        mCommit.setOnClickListener(payListener);
//        if (price > 0) {
//            mTitle.setText(getContext().getString(R.string.consult_price_in_message, 1, 30));
//        } else {
//            mTitle.setText(getContext().getString(R.string.consult_price_in_message_free_limit, 0, 30));
//        }
//        mTitle.setVisibility(View.VISIBLE);
//        mCancel.setVisibility(View.GONE);
//        setVisibility(View.VISIBLE);
//    }

    private void changeModeConsultFinish() {
        if (isSeller) {
            setVisibility(View.GONE);
        } else {
            setVisibility(View.VISIBLE);
            boolean isServiceTime = processState.consultInfo.talentOnlineStatus;
            if (isServiceTime) {
                long point;
                long serviceTime;
                if (shortItem != null) {
                    point = shortItem.price;
                    serviceTime = shortItem.consultTime / 60;
                } else {
                    point = processState.processOrder.usePointNum;
                    serviceTime = processState.processOrder.serveTime / 60;
                }
                mCommit.setText(R.string.consult_now);
                mCommit.setVisibility(View.VISIBLE);
                mCommit.setOnClickListener(consultNowListener);
                if (point > 0) {
                    mTitle.setText(getContext().getString(R.string.consult_price_in_message, point/10, serviceTime));
                } else {
                    mTitle.setText(getContext().getString(R.string.consult_price_in_message_free_limit, point, serviceTime));
                }
                mTitle.setVisibility(View.VISIBLE);
                mCancel.setVisibility(View.GONE);
            } else {
                mTitle.setVisibility(View.VISIBLE);
                mTitle.setText(R.string.consult_no_service);
                mCancel.setVisibility(View.GONE);
                mCommit.setVisibility(View.VISIBLE);
                mCommit.setText(R.string.consult_other);
                mCommit.setOnClickListener(consultOhterListener);
            }
        }
    }

    public class ShowCommentTimerTask extends TimerTask {

        @Override
        public void run() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    setVisibility(View.GONE);
                    if (consultListener != null) consultListener.showComment();
                    timer = null;
                }
            });
        }
    }

    Timer timer;

    private void startShowCommentTimerTask() {
        if (timer != null) return;
        timer = new Timer();
        timer.schedule(new ShowCommentTimerTask(), 1000 * 10);
    }

    private void stopShowCommentTimerTask() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void changeModeNoService() {
        setVisibility(View.VISIBLE);
        mTitle.setVisibility(View.VISIBLE);
        mTitle.setText(R.string.consult_no_service);
        mCancel.setVisibility(View.GONE);
        mCommit.setVisibility(View.VISIBLE);
        mCommit.setText(R.string.consult_other);
        mCommit.setOnClickListener(consultOhterListener);
    }

    private void changeModeLineUp() {
        if (isSeller) {
            if (processState.consultInfo.ableToAcceptOrder) {
                setVisibility(VISIBLE);
                mTitle.setVisibility(View.GONE);
                mCommit.setText(getContext().getString(R.string.accpect_order));
                mCommit.setVisibility(View.VISIBLE);
                mCommit.setOnClickListener(accpetListener);
                mCancel.setVisibility(View.GONE);
            } else {
                setVisibility(VISIBLE);
                mTitle.setVisibility(View.VISIBLE);
                mTitle.setText(getContext().getString(R.string.label_user_in_queue_service_other));
                mCommit.setVisibility(View.GONE);
                mCancel.setVisibility(View.GONE);
            }
        } else {
            setVisibility(View.VISIBLE);
            mTitle.setVisibility(View.VISIBLE);
            if (processState.consultInfo.userQueuePosition <= 0) {
                mTitle.setText(getContext().getString(R.string.message_consult_line_up_0));
            } else {
                mTitle.setText(getContext().getString(R.string.message_consult_line_up, processState.consultInfo.userQueuePosition));
            }
            mCancel.setVisibility(View.GONE);
            mCommit.setVisibility(View.GONE);
        }
    }

    private OnClickListener consultNowListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (shortItem == null) {
                NavUtils.gotoMasterConsultActivity(getContext(), processState.processOrder.itemId, processState.processOrder.usePointNum, processState.processOrder.serveTime);
            } else {
                NavUtils.gotoMasterConsultActivity(getContext(), processState.processOrder.itemId, shortItem.price, shortItem.consultTime);
            }
        }
    };

    private OnClickListener consultAgainListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            stopShowCommentTimerTask();
            TCEventHelper.onEvent(v.getContext(), AnalyDataValue.IM_CONSULTING_START_AGAIN);
            NavUtils.gotoProductDetail(getContext(), ItemType.MASTER_CONSULT_PRODUCTS, processState.processOrderItem.itemId, null);
        }
    };


    private OnClickListener accpetListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (consultListener != null) consultListener.accpetOrder();
        }
    };

    private OnClickListener payListener = new OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
    private OnClickListener consultOhterListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            TCEventHelper.onEvent(v.getContext(), AnalyDataValue.IM_CONSULTING_CHANGE_OTHER);
            NavUtils.gotoMasterConsultHomeActivity(getContext());
        }
    };
    private OnClickListener cancelListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            stopShowCommentTimerTask();
            setVisibility(View.GONE);
            TCEventHelper.onEvent(v.getContext(), AnalyDataValue.IM_CONSULTING_IGNORE);
            if (consultListener != null) consultListener.showComment();
        }
    };
    ConsultListener consultListener;


    public void setConsultListener(ConsultListener listener) {
        consultListener = listener;
    }

    public interface ConsultListener {
        void showComment();

        void accpetOrder();
    }


    boolean isSeller = false;
    boolean needShowComment = false;
    ProcessState processState;
    ShortItem shortItem;

    public void setData(ProcessState processState, ShortItem shortItem, boolean needShowComment, boolean isSeller) {
        this.isSeller = isSeller;
        this.needShowComment = needShowComment;
        this.processState = processState;
        this.shortItem = shortItem;
        String processStatus = processState.processOrder.processOrderStatus;
        if (processStatus.equals("CONSULT_IN_CHAT")) {
            setVisibility(View.GONE);
        } else if (processStatus.equals("CANCEL") || processStatus.equals("RATED")) {
            changeModeConsultFinish();
        } else if (processStatus.equals("FINISH")) {
            changeModeComment();
        } else if (processStatus.equals("CONSULT_IN_QUEUE")) {
            changeModeLineUp();
        }

    }

    private void changeModeComment() {
        if (isSeller) {
            setVisibility(View.GONE);
        } else {
            if ("TIME_OUT".equals(processState.processOrder.finishOrderSource) && !needShowComment) {
                mTitle.setVisibility(View.VISIBLE);
                mTitle.setText(R.string.consult_finish);
                mCancel.setVisibility(View.VISIBLE);
                mCommit.setVisibility(View.VISIBLE);
                mCancel.setText(R.string.thats_ok);
                mCommit.setText(R.string.again_under_order);
                mCommit.setOnClickListener(consultAgainListener);
                mCancel.setOnClickListener(cancelListener);
                setVisibility(View.VISIBLE);
                startShowCommentTimerTask();
//        } else if ("USER_APP".equals(processState.processOrder.finishOrderSource)) {
//            setVisibility(View.GONE);
//            if (consultListener != null) consultListener.showComment();
            } else {
                setVisibility(View.GONE);
                if (consultListener != null) consultListener.showComment();
            }
        }
    }
}
