package com.quanyan.yhy.ui.consult;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.quanyan.base.BaseActivity;
import com.quanyan.base.util.StringUtil;
import com.quanyan.base.view.BaseNavView;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.AnalyDataValue;
import com.quanyan.yhy.ui.base.utils.DialogUtil;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.base.utils.TCEventHelper;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.consult.controller.ConsultController;
import com.quanyan.yhy.ui.consult.view.ConsultSelectDialogView;
import com.quanyan.yhy.ui.views.AppSettingItem;
import com.quanyan.yhy.view.LabelLayout;
import com.yhy.common.beans.net.model.RCShowcase;
import com.yhy.common.beans.net.model.common.Booth;
import com.yhy.common.beans.net.model.tm.CreateProcessOrderResultTO;
import com.yhy.common.beans.net.model.tm.SellerAndConsultStateResult;
import com.yhy.common.constants.ConsultContants;
import com.yhy.common.constants.IntentConstants;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.SPUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with Android Studio.
 * Title:MasterConsultHomeActivity
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:邓明佳
 * Date:2016/7/7
 * Time:14:55
 * Version 1.0
 */
public class MasterConsultActivity extends BaseActivity {
    private long itemId;
    private long point;
    private long serviceTime;
    BaseNavView navView;
    private EditText mEditText;
    private LabelLayout mLabels;
    private TextView mWords;
    ScrollView mScrollView;
    private AppSettingItem mDays, mPersons;
    private int daysSelectionIndex = -1;
    private int personSelectionIndex = -1;
    private List<String> days;
    private List<String> persions;
    Dialog daysDialog;
    Dialog persionsDialog;
    ConsultController controller;
    private List<String> checkedList = new ArrayList<>();
    private TextView mCommit;

    @Override
    protected void initView(Bundle savedInstanceState) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.gray));
            //底部导航栏
            //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
        }

        itemId = getIntent().getLongExtra(IntentConstants.EXTRA_ITEM_ID, 0);
        point = getIntent().getLongExtra(IntentConstants.EXTRA_POINT, 0);
        serviceTime = getIntent().getLongExtra(IntentConstants.EXTRA_SERVICE_TIME, 0);
        controller = new ConsultController(this, mHandler);
        navView.setTitleText(R.string.label_consult_master_title);
        mEditText = (EditText) findViewById(R.id.edit_text);
        mLabels = (LabelLayout) findViewById(R.id.layout_label);
        mWords = (TextView) findViewById(R.id.number_words);
        mDays = (AppSettingItem) findViewById(R.id.days_count);
        mPersons = (AppSettingItem) findViewById(R.id.persons_count);
        mScrollView = (ScrollView) findViewById(R.id.scroll_view);
        mCommit = (TextView) findViewById(R.id.tv_commit);
        mPersons.setSummary("要去几个人");
        mDays.setTitle(R.string.travel_days_count);
        mDays.setSummary("计划玩几天");
        days = Arrays.asList(getResources().getStringArray(R.array.travel_days_count));
        persions = Arrays.asList(getResources().getStringArray(R.array.travel_persion_count));
        mPersons.setTitle(R.string.travel_persons_count);
//        setLabels(testDatas);
        setListener();
        showLoadingView(null);
        controller.getBooth(this, ConsultContants.COMMON_CONSULTING_KEYWORDS);
    }

    @Override
    public boolean isUseImmersiveStyle() {
        return false;
    }

    private void setListener() {
        mCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkedList.isEmpty()) {
//                    ToastUtil.showToast(MasterConsultActivity.this, "请填写必填项");
                    return;
                }
                showLoadingView(null);
                mCommit.setEnabled(false);
                tcEvent();
                controller.querySellerAndConsultStateWhenCommit(MasterConsultActivity.this, itemId);
            }
        });

        mDays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                daysDialog = DialogUtil.getMenuDialog(MasterConsultActivity.this, new ConsultSelectDialogView(MasterConsultActivity.this, new ConsultSelectDialogView.OnButtonClickListener() {
                    @Override
                    public void onCancel() {
                        daysDialog.dismiss();
                    }

                    @Override
                    public void onSubmit(String value) {
                        daysDialog.dismiss();
                        daysSelectionIndex = days.indexOf(value);
                        mDays.setSummary(value);
                    }
                }, days, daysSelectionIndex));
                daysDialog.setCanceledOnTouchOutside(true);
                daysDialog.show();
            }
        });
        mPersons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                persionsDialog = DialogUtil.getMenuDialog(MasterConsultActivity.this, new ConsultSelectDialogView(MasterConsultActivity.this, new ConsultSelectDialogView.OnButtonClickListener() {
                    @Override
                    public void onCancel() {
                        persionsDialog.dismiss();
                    }

                    @Override
                    public void onSubmit(String value) {
                        persionsDialog.dismiss();
                        personSelectionIndex = persions.indexOf(value);
                        mPersons.setSummary(value);
                    }
                }, persions, personSelectionIndex));
                persionsDialog.setCanceledOnTouchOutside(true);
                persionsDialog.show();
            }
        });

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mWords.setText(getString(R.string.words_count_limit_200, mEditText.getText().length()));
            }
        });
    }

    private void tcEvent() {
        Map<String, String> map = new HashMap<>();
        if (checkedList != null && checkedList.size() > 0) {
            StringBuilder str = new StringBuilder();
            for (int i = 0; i < checkedList.size(); i++) {
                if (i != checkedList.size() - 1) {
                    str.append(checkedList.get(i) + "、");
                } else {
                    str.append(checkedList.get(i));
                }

            }
            map.put(AnalyDataValue.KEY_TAGS, str.toString());
        }
        map.put(AnalyDataValue.KEY_PID, itemId + "");
        TCEventHelper.onEvent(this, AnalyDataValue.IM_CONSULTING_START_NOW, map);
    }

    private void addLable(String string, LabelLayout.LayoutParams params) {
        TextView textView = new TextView(this);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(14);
        textView.setPadding(25, 10, 25, 10);
        textView.setTextColor(getResources().getColor(R.color.neu_666666));
        textView.setBackgroundResource(R.drawable.shape_button_consult_content_unselected);
        textView.setText(string);
        textView.setTag(string);
        textView.setOnClickListener(labelOnclickListener);
        mLabels.addView(textView, params);
    }


    @Override
    public View onLoadContentView() {
        return LayoutInflater.from(this).inflate(R.layout.ac_master_consult, null);
    }

    @Override
    public View onLoadNavView() {
        return navView = new BaseNavView(this);
    }

    public void refreshCommitButton() {
        if (checkedList != null && checkedList.size() > 0) {
            mCommit.setBackgroundColor(getResources().getColor(R.color.blue_ying));
            mCommit.setTextColor(getResources().getColor(R.color.white));
        } else {
            mCommit.setBackgroundColor(getResources().getColor(R.color.neu_cccccc));
            mCommit.setTextColor(getResources().getColor(R.color.neu_666666));
        }
    }

    @Override
    public boolean isTopCover() {
        return false;
    }

    List<RCShowcase> labels;

    public void setLabels(List<RCShowcase> labels) {
        this.labels = labels;
        if (labels == null || labels.size() == 0) {
            ToastUtil.showToast(this, "labels is null");
            finish();
            return;
        }
        LabelLayout.LayoutParams marginLayoutParams = new LabelLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        marginLayoutParams.setMargins(10, 10, 10, 10);
        mLabels.removeAllViews();
        checkedList.clear();
        for (int i = 0; i < labels.size(); i++) {
            addLable(labels.get(i).title, marginLayoutParams);
        }
    }

    private View.OnClickListener labelOnclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String string = (String) v.getTag();
            if (checkedList.contains(string)) {
                checkedList.remove(string);
            } else {
                checkedList.add(string);
            }
            for (int i = 0; i < mLabels.getChildCount(); i++) {
                if (checkedList.contains(labels.get(i).title)) {
                    TextView textView = (TextView) mLabels.getChildAt(i);
                    textView.setBackgroundResource(R.drawable.shape_button_consult_content_selected);
                    textView.setTextColor(getResources().getColor(R.color.white));
                } else {
                    TextView textView = (TextView) mLabels.getChildAt(i);
                    textView.setBackgroundResource(R.drawable.shape_button_consult_content_unselected);
                    textView.setTextColor(getResources().getColor(R.color.neu_666666));
                }
            }
            refreshCommitButton();
        }
    };
    Dialog notFinishDialog;
    Dialog makePointConsultDialog;

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        int what = msg.what;
        if (what == ValueConstants.SELLER_AND_CONSULT_STATE_WHEN_COMMINT_OK) {
            final SellerAndConsultStateResult result = (SellerAndConsultStateResult) msg.obj;
            handleCheckStateWhenCommit(result);
        } else if (what == ValueConstants.SELLER_AND_CONSULT_STATE_WHEN_COMMINT_KO) {
            hideLoadingView();
            mCommit.setEnabled(true);
            ToastUtil.showToast(this, StringUtil.handlerErrorCode(this, msg.arg1));
        } else if (what == ValueConstants.CREATE_PROCESS_ORDER_OK) {
            CreateProcessOrderResultTO result = (CreateProcessOrderResultTO) msg.obj;
            final CreateProcessOrderResultTO processOrderResultTO = result;
            if (processOrderResultTO.success) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        hideLoadingView();
                        if (processOrderResultTO.processOrder == null || processOrderResultTO.processOrder.consultUserInfo == null) {
                            ToastUtil.showToast(MasterConsultActivity.this, "获取订单信息失败");
                            return;
                        }
                        NavUtils.gotoMessageActivity(MasterConsultActivity.this, processOrderResultTO.processOrder.consultUserInfo.userId, null, itemId);
                        finish();
                    }
                }, 1500);
            } else {
                mCommit.setEnabled(true);
                ToastUtil.showToast(this, "创建失败");
            }
        } else if (what == ValueConstants.CREATE_PROCESS_ORDER_KO) {
            hideLoadingView();
            mCommit.setEnabled(true);
            if (6000200 == msg.arg1) {
                makePointConsultDialog = DialogUtil.showMessageDialog(this, null, this.getString(R.string.make_point_consult_message), getString(R.string.make_point), getString(R.string.waiting), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        NavUtils.gotoIntegralActivity(MasterConsultActivity.this, 0);
                        String url = SPUtils.getPointDetail(MasterConsultActivity.this);
                        if (url != null && !url.isEmpty()) {
                            NavUtils.startWebview(MasterConsultActivity.this, "", url, 0);
                        }
                        makePointConsultDialog.dismiss();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        makePointConsultDialog.dismiss();
                    }
                });
                makePointConsultDialog.show();
                return;
            }
            ToastUtil.showToast(this, StringUtil.handlerErrorCode(this, msg.arg1));
        } else if (what == ValueConstants.GET_CONSULT_BOOTH_OK) {
            hideLoadingView();
            Booth booth = (Booth) msg.obj;
            setLabels(booth.showcases);
        } else if (what == ValueConstants.GET_CONSULT_BOOTH_KO) {
            hideLoadingView();
//            ToastUtil.showToast(this, StringUtil.handlerErrorCode(this, msg.arg1));
//            finish();
        } else if (what == ValueConstants.SELLER_AND_CONSULT_STATE_OK) {
            //TODO 检查咨询服务的状态
            hideLoadingView();
            final SellerAndConsultStateResult result = (SellerAndConsultStateResult) msg.obj;
            handleProcessState(result);
        } else if (what == ValueConstants.SELLER_AND_CONSULT_STATE_KO) {
            hideLoadingView();
            ToastUtil.showToast(this, StringUtil.handlerErrorCode(this, msg.arg1));
            finish();
        }
    }

    private Dialog payPointConsultDialog = null;

    /**
     * 处理提交后的处理流程
     */
    private void handleCheckStateWhenCommit(final SellerAndConsultStateResult result) {
        if (result.canCreateOrder) {
            if (point > 0) {
                hideLoadingView();
                payPointConsultDialog = DialogUtil.showMessageDialog(this, null, getString(R.string.pay_point_consult_message, point/10, serviceTime / 60), getString(R.string.label_btn_ok), getString(R.string.cancel), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showLoadingView(null);
                        createProcessOrder();
                        payPointConsultDialog.dismiss();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCommit.setEnabled(true);
                        payPointConsultDialog.dismiss();
                    }
                });
                payPointConsultDialog.show();
            } else {
                createProcessOrder();
            }
        } else {
            hideLoadingView();
            mCommit.setEnabled(true);
            if (result.reason.equals(ConsultContants.ORDER_NOT_FINISH)) {
                notFinishDialog = DialogUtil.showMessageDialog(MasterConsultActivity.this, null, getString(R.string.label_finish_current_cousult), getString(R.string.cancel), getString(R.string.fine), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        notFinishDialog.dismiss();
                        finish();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NavUtils.gotoMessageActivity(MasterConsultActivity.this, result.processOrder.consultUserInfo.userId, null, result.processOrder.itemId);
                        notFinishDialog.dismiss();
                        finish();
                    }
                });
                notFinishDialog.setCanceledOnTouchOutside(true);
                notFinishDialog.show();
            } else if (result.reason.equals(ConsultContants.TALENT_NOT_ONLINE)) {
                DialogUtil.showMessageDialog(this, null, getString(R.string.dialog_advice_no_fromsale), getString(R.string.dialog_advice_bt), null, null, null, null, null).show();
            } else if (result.reason.equals(ConsultContants.ITEM_NOT_AVAILABLE)) {
                DialogUtil.showMessageDialog(this, null, getString(R.string.dialog_item_not_avaliable), getString(R.string.dialog_advice_bt), null, null, null, null, null).show();
            }
        }
    }

    private void createProcessOrder() {
        String demandDetail = mEditText.getText().toString();
        int days = 0;
        if (daysSelectionIndex >= 0) {
            if (daysSelectionIndex == 20) {
                days = 100;
            } else {
                days = daysSelectionIndex + 1;
            }
        }
        int person = 0;
        if (personSelectionIndex >= 0) {
            if (personSelectionIndex == 20) {
                person = 100;
            } else {
                person = personSelectionIndex + 1;
            }
        }
        controller.createProcessOrder(MasterConsultActivity.this, itemId, checkedList, demandDetail, days, person, null, ConsultContants.GENERAL_CONSULT);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkProcessState();
    }

    /**
     * 页面进入前检查当前的咨询服务状态
     */
    private void checkProcessState() {
        showLoadingView("");
        controller.querySellerAndConsultState(this, itemId);
    }

    /**
     * 处理咨询服务的状态
     */
    private void handleProcessState(final SellerAndConsultStateResult result) {
        if (result != null) {
            if (result.canCreateOrder) {
                //TODO 可以咨询
            } else {
                if (result.reason.equals(ConsultContants.ORDER_NOT_FINISH)) {
                    notFinishDialog = DialogUtil.showMessageDialog(this, null, getString(R.string.label_finish_current_cousult), getString(R.string.cancel), getString(R.string.fine), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            notFinishDialog.dismiss();
                            finish();
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (result.processOrder != null && result.processOrder.buyerInfo != null) {
                                NavUtils.gotoMessageActivity(v.getContext(), result.processOrder.consultUserInfo.userId, null, result.processOrder.itemId);
                            }
                            notFinishDialog.dismiss();
                            finish();
                        }
                    });
                    notFinishDialog.setCanceledOnTouchOutside(true);
                    notFinishDialog.show();
                } else if (result.reason.equals(ConsultContants.TALENT_NOT_ONLINE)) {
                    ToastUtil.showToast(this, R.string.dialog_advice_no_fromsale);
                    finish();
                } else if (result.reason.equals(ConsultContants.ITEM_NOT_AVAILABLE)) {
                    ToastUtil.showToast(this, R.string.dialog_item_not_avaliable);
                    finish();
                }
            }
        }
    }
}
