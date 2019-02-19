package com.quanyan.yhy.ui.consult;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.quanyan.base.BaseActivity;
import com.quanyan.base.util.StringUtil;
import com.quanyan.base.view.BaseNavView;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.AnalyDataValue;
import com.quanyan.yhy.common.ItemType;
import com.quanyan.yhy.libanalysis.AnEvent;
import com.quanyan.yhy.libanalysis.Analysis;
import com.quanyan.yhy.ui.base.utils.DialogUtil;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.base.utils.TCEventHelper;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.consult.controller.ConsultController;
import com.quanyan.yhy.ui.views.AppSettingItem;
import com.quanyan.yhy.view.LabelLayout;
import com.yhy.common.beans.city.bean.AddressBean;
import com.yhy.common.beans.net.model.RCShowcase;
import com.yhy.common.beans.net.model.common.Booth;
import com.yhy.common.beans.net.model.tm.CreateProcessOrderResultTO;
import com.yhy.common.beans.net.model.tm.SellerAndConsultStateResult;
import com.yhy.common.beans.net.model.tm.TmConsultInfo;
import com.yhy.common.constants.ConsultContants;
import com.yhy.common.constants.RequestCodeValues;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.SPUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with Android Studio.
 * Title:QuickConsultActivity
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:邓明佳
 * Date:2016/7/7
 * Time:14:55
 * Version 1.0
 */
public class QuickConsultActivity extends BaseActivity {
    BaseNavView navView;
    private EditText mEditText;
    private LabelLayout mLabels;
    private TextView mWords;
    ScrollView mScrollView;
    private AppSettingItem mDestination;
    ConsultController controller;

    private List<String> checkedList = new ArrayList<>();
    private TextView mCommit;
    private EditText mETOther;

    @Override
    protected void initView(Bundle savedInstanceState) {
        controller = new ConsultController(this, mHandler);
        navView.setTitleText(R.string.quick_consult);
        mEditText = (EditText) findViewById(R.id.edit_text);
        mLabels = (LabelLayout) findViewById(R.id.layout_label);
        mWords = (TextView) findViewById(R.id.number_words);
        mCommit = (TextView) findViewById(R.id.tv_commit);
        mScrollView = (ScrollView) findViewById(R.id.scroll_view);
        mETOther = (EditText) findViewById(R.id.et_other);
        mETOther.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                refreshCommitButton();
            }
        });
        mDestination = (AppSettingItem) findViewById(R.id.destination);
        mDestination.setTitle(R.string.destination);
        mDestination.setSummary(getString(R.string.places_of_interest));

        mDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavUtils.gotoDestinationSelectActivity(QuickConsultActivity.this, ItemType.SERVICE, ItemType.CONSULT, null, null, RequestCodeValues.REQUEST_LINE_DEST_CITY_SELECT);
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
        mCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadTips = getCheckedList();
                if (uploadTips == null || uploadTips.isEmpty()) {
//                    ToastUtil.showToast(QuickConsultActivity.this, "请填写必填项");
                    return;
                }
                showLoadingView(null);
                tcEvent2();
                mCommit.setEnabled(false);
                controller.getFastConsultItem(QuickConsultActivity.this, uploadTips, cityCode, mEditText.getText().toString());
                //事件统计
                Analysis.pushEvent(QuickConsultActivity.this, AnEvent.QUICK_CONSULT_BOTTOM);
            }
        });
        showLoadingView(null);

        controller.getBooth(this, ConsultContants.QUICK_CONSULTING_KEYWORDS);
    }

    public void refreshCommitButton() {
        if (checkedList.size() == 0) {
            mCommit.setBackgroundColor(getResources().getColor(R.color.neu_cccccc));
            mCommit.setTextColor(getResources().getColor(R.color.neu_666666));
        } else if (checkedList.size() == 1) {
            if (!checkedList.contains(getString(R.string.other_question))) {
                mCommit.setBackgroundColor(getResources().getColor(R.color.blue_ying));
                mCommit.setTextColor(getResources().getColor(R.color.white));
            } else if (!TextUtils.isEmpty(mETOther.getText().toString().trim())) {
                mCommit.setBackgroundColor(getResources().getColor(R.color.blue_ying));
                mCommit.setTextColor(getResources().getColor(R.color.white));
            } else {
                mCommit.setBackgroundColor(getResources().getColor(R.color.neu_cccccc));
                mCommit.setTextColor(getResources().getColor(R.color.neu_666666));
            }
        } else {
            mCommit.setBackgroundColor(getResources().getColor(R.color.blue_ying));
            mCommit.setTextColor(getResources().getColor(R.color.white));
        }
    }

    private void tcEvent2() {
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
        map.put(AnalyDataValue.KEY_CITY_NAME, city);
        map.put(AnalyDataValue.KEY_CITY_CODE, cityCode);
        map.put(AnalyDataValue.KEY_CONTENT, mEditText.getText().toString());
        TCEventHelper.onEvent(this, AnalyDataValue.IM_CONSULTING_QUICK_CLICK, map);
    }

    List<String> uploadTips = null;

    @Override
    public View onLoadContentView() {
        return LayoutInflater.from(this).inflate(R.layout.ac_quick_consult, null);
    }

    @Override
    public View onLoadNavView() {
        return navView = new BaseNavView(this);
    }

    @Override
    public boolean isTopCover() {
        return false;
    }

    String city;
    String cityCode;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RequestCodeValues.REQUEST_LINE_DEST_CITY_SELECT:
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        AddressBean addressBean = (AddressBean) data.getSerializableExtra(SPUtils.EXTRA_SELECT_CITY);
                        cityCode = addressBean.getCityCode();
                        city = addressBean.getName();
                        if(!"-1".equals(cityCode)) {
                            //用户选择不限后取消选择
                            mDestination.setSummary(addressBean.getName());
                        }else{
                            cityCode = null;
                            city = null;
                            mDestination.setSummary(getString(R.string.places_of_interest));
                        }
                    }
                }
                break;
        }
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
        addLable(getString(R.string.other_question), marginLayoutParams);
    }

    private View.OnClickListener labelOnclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //事件统计
            Analysis.pushEvent(QuickConsultActivity.this, AnEvent.CONTENT_LABEL);
            String string = (String) v.getTag();
            if (checkedList.contains(string)) {
                checkedList.remove(string);
            } else {
                checkedList.add(string);
            }
            for (int i = 0; i < labels.size(); i++) {
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
            boolean hasOther = checkedList.contains(getString(R.string.other_question));
            if (hasOther) {
                TextView textView = (TextView) mLabels.getChildAt(mLabels.getChildCount() - 1);
                textView.setBackgroundResource(R.drawable.shape_button_consult_content_selected);
                textView.setTextColor(getResources().getColor(R.color.white));
            } else {
                TextView textView = (TextView) mLabels.getChildAt(mLabels.getChildCount() - 1);
                textView.setBackgroundResource(R.drawable.shape_button_consult_content_unselected);
                textView.setTextColor(getResources().getColor(R.color.neu_666666));
            }
            mETOther.setVisibility(hasOther ? View.VISIBLE : View.GONE);
            refreshCommitButton();
        }
    };
    TmConsultInfo info;
    Dialog notFinishDialog;
    private Dialog payPointConsultDialog = null;
    Dialog makePointConsultDialog;

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        int what = msg.what;
        if (what == ValueConstants.GET_FAST_CONSULT_OK) {
            info = (TmConsultInfo) msg.obj;
            if (info.shortProcessOrder == null && info.itemId > 0) {
                if (info.totalFee > 0) {
                    hideLoadingView();
                    payPointConsultDialog = DialogUtil.showMessageDialog(this, null, getString(R.string.pay_point_consult_message, info.totalFee/10, info.serviceTime / 60), getString(R.string.label_btn_ok), getString(R.string.cancel), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showLoadingView(null);
                            createProcessOrder(info.itemId, uploadTips);
                            payPointConsultDialog.dismiss();
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            payPointConsultDialog.dismiss();
                        }
                    });
                    payPointConsultDialog.show();
                    mCommit.setEnabled(true);
                } else {
                    createProcessOrder(info.itemId, uploadTips);
                }
            } else {
                mCommit.setEnabled(true);
                tcEvent(4000005);
                hideLoadingView();
                notFinishDialog = DialogUtil.showMessageDialog(this, null, getString(R.string.label_finish_current_cousult), getString(R.string.cancel), getString(R.string.fine), null, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        notFinishDialog.dismiss();
                        QuickConsultActivity.this.finish();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        notFinishDialog.dismiss();
                        if (info.shortProcessOrder == null) {
                            ToastUtil.showToast(QuickConsultActivity.this, "未获取到正在咨询的订单信息");
                            return;
                        }
                        NavUtils.gotoMessageActivity(QuickConsultActivity.this, info.shortProcessOrder.sellerId, null, info.shortProcessOrder.itemId);
                        QuickConsultActivity.this.finish();
                    }
                }, null);
                notFinishDialog.show();
            }
        } else if (what == ValueConstants.GET_FAST_CONSULT_KO) {
            mCommit.setEnabled(true);
            hideLoadingView();
            tcEvent(msg.arg1);
            if (msg.arg1 == 4000006) {
                DialogUtil.showMessageDialog(this, null, getString(R.string.label_find_not_master_consult_change_tip), getString(R.string.dialog_advice_bt), null, null, null, null, null).show();
            } else if (msg.arg1 == 4000007) {
                DialogUtil.showMessageDialog(this, null, getString(R.string.label_find_not_master_consult_change_time), getString(R.string.dialog_advice_bt), null, null, null, null, null).show();
            } else {
                ToastUtil.showToast(this, StringUtil.handlerErrorCode(this, msg.arg1));
            }
        } else if (what == ValueConstants.CREATE_PROCESS_ORDER_OK) {
            CreateProcessOrderResultTO result = (CreateProcessOrderResultTO) msg.obj;
            final CreateProcessOrderResultTO processOrderResultTO = result;
            if (processOrderResultTO.success) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        hideLoadingView();
                        if (processOrderResultTO.processOrder == null || processOrderResultTO.processOrder.consultUserInfo == null) {
                            ToastUtil.showToast(
                                    QuickConsultActivity.this, "获取订单信息失败");
                            mCommit.setEnabled(true);
                            return;
                        }
                        NavUtils.gotoMessageActivity(QuickConsultActivity.this, processOrderResultTO.processOrder.consultUserInfo.userId, null, info.itemId);
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
//                        NavUtils.gotoIntegralActivity(QuickConsultActivity.this, 0);
                        // 改成跳h5的积分任务页面
                        String url = SPUtils.getPointDetail(QuickConsultActivity.this);
                        if (url != null && !url.isEmpty()) {
                            NavUtils.startWebview(QuickConsultActivity.this, "", url, 0);
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
            ToastUtil.showToast(this, StringUtil.handlerErrorCode(this, msg.arg1));
            finish();
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

    private void tcEvent(int arg1) {
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

        if (arg1 == 4000005) {
            map.put(AnalyDataValue.KEY_REASON, getString(R.string.label_finish_current_cousult));
        } else if (arg1 == 4000006) {
            map.put(AnalyDataValue.KEY_REASON, getString(R.string.label_find_not_master_consult_change_tip));
        } else if (arg1 == 4000007) {
            map.put(AnalyDataValue.KEY_REASON, getString(R.string.label_find_not_master_consult_change_time));
        }
        map.put(AnalyDataValue.KEY_CITY_NAME, city);
        TCEventHelper.onEvent(this, AnalyDataValue.IM_CONSULTING_NO_MATCH, map);

    }

    private void createProcessOrder(long itemId, List<String> uploadTips) {
        if (uploadTips == null || uploadTips.isEmpty()) {
            ToastUtil.showToast(this, "创建失败");
            hideLoadingView();
            mCommit.setEnabled(true);
            return;
        }
        String demandDetail = mEditText.getText().toString();
        int days = 0;
        int person = 0;
        controller.createProcessOrder(QuickConsultActivity.this, itemId, uploadTips, demandDetail, days, person, city, ConsultContants.QUICK_CONSULTING);
    }

    private List<String> getCheckedList() {
        if (checkedList.size() == 0) {
            return null;
        }
        if (checkedList.size() == 1 && checkedList.contains(getString(R.string.other_question)) && TextUtils.isEmpty(mETOther.getText().toString().trim())) {
            return null;
        }
        List<String> list = new ArrayList<>();
        for (int i = 0; i < checkedList.size(); i++) {
            String strings = checkedList.get(i);
            if (strings.equals(getString(R.string.other_question))) continue;
            list.add(strings);
        }
        if (checkedList.contains(getString(R.string.other_question))) {
            if (!TextUtils.isEmpty(mETOther.getText().toString().trim())) {
                list.add(mETOther.getText().toString());
            }
        }
        return list;
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
        controller.querySellerAndConsultState(this, -1);
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
