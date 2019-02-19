package com.quanyan.yhy.ui.servicerelease;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.baseenum.IActionTitleBar;
import com.quanyan.base.view.BaseNavView;
import com.quanyan.base.yminterface.ErrorViewClick;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.ErrorCode;
import com.quanyan.yhy.ui.base.utils.DialogUtil;
import com.quanyan.yhy.ui.servicerelease.bean.DetailServiceBean;
import com.quanyan.yhy.ui.servicerelease.controller.ManageInfoController;
import com.yhy.common.beans.net.model.tm.ConsultCategoryInfoList;
import com.yhy.common.beans.net.model.tm.ItemProperty;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.SPUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:AddServiceDetailActivity
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-7-5
 * Time:13:43
 * Version 1.1.0
 */

public class AddServiceDetailActivity extends BaseActivity {

    private BaseNavView mBaseNavView;
    @ViewInject(R.id.tv_finish_button)
    private TextView mFinishButton;//完成按钮

    @ViewInject(R.id.rl_all_contain)
    private LinearLayout mRLAllContain;

    /*@ViewInject(R.id.et_cost_desc)
    private EditText mCostDesc;//费用描述

    @ViewInject(R.id.et_book_explain)
    private EditText mBookExplain;//预订说明

    @ViewInject(R.id.et_back_rule)
    private EditText mBackRule;//退改规则*/

    /*@ViewInject(R.id.tv_title_text1)
    private TextView mTVTitle1;//第1个描述

    @ViewInject(R.id.tv_title_text1)
    private TextView mTVTitle2;//第2个描述

    @ViewInject(R.id.tv_title_text1)
    private TextView mTVTitle3;//第3个描述*/

    @ViewInject(R.id.ll_detail_contain)
    private LinearLayout mLLDetailContain;//内容父布局

    private ManageInfoController mControler;

    //private DetailServiceBean mDetailServiceBean;
    private Dialog mDialogCancle;
    private ConsultCategoryInfoList mConsultCategoryInfoList;
    private List<ItemProperty> mListContain;
    private List<ItemProperty> mItemProperties;

    public static void gotoAddServiceDetailActivity(Activity context, DetailServiceBean detailServiceBean, int reqCode) {
        Intent intent = new Intent(context, AddServiceDetailActivity.class);
        intent.putExtra(SPUtils.EXTRA_DATA, detailServiceBean);
        context.startActivityForResult(intent, reqCode);
    }

    public static void gotoAddServiceDetailActivity(Activity context, List<ItemProperty> itemProperties, int reqCode) {
        Intent intent = new Intent(context, AddServiceDetailActivity.class);
        intent.putExtra(SPUtils.EXTRA_DATA, (Serializable) itemProperties);
        context.startActivityForResult(intent, reqCode);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ViewUtils.inject(this);
        mBaseNavView.setTitleText(R.string.label_goods_ser_detail);
        mControler = new ManageInfoController(this, mHandler);
        //mDetailServiceBean = (DetailServiceBean) getIntent().getSerializableExtra(SPUtils.EXTRA_DATA);
        mItemProperties = (List<ItemProperty>) getIntent().getSerializableExtra(SPUtils.EXTRA_DATA);
        initClick();
        if(mItemProperties == null || mItemProperties.size() <= 0){
            doNetService();
        }

        initData();
    }

    private void doNetService() {
        showLoadingView("");
        mControler.doGetConsultItemProperties(this);
    }

    private void initData() {
        if(mItemProperties != null && mItemProperties.size() > 0){
            addLinearLayout(false, mItemProperties);
        }
        /*mCostDesc.setText(getString(R.string.release_detail_cost_text));
        mBookExplain.setText(getString(R.string.release_detail_book_text));
        mBackRule.setText(getString(R.string.release_detail_back_text));

        if(mDetailServiceBean != null){
            if(!StringUtil.isEmpty(mDetailServiceBean.costDesc)){
                mCostDesc.setText(mDetailServiceBean.costDesc);
            }

            if(!StringUtil.isEmpty(mDetailServiceBean.bookExplain)){
                mBookExplain.setText(mDetailServiceBean.bookExplain);
            }

            if(!StringUtil.isEmpty(mDetailServiceBean.backRule)){
                mBackRule.setText(mDetailServiceBean.backRule);
            }
        }*/
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        hideLoadingView();
        hideErrorView(mRLAllContain);
        switch (msg.what){
            case ValueConstants.MSG_GETCONSULT_ITEMPROPERTIES_OK:
                mConsultCategoryInfoList = (ConsultCategoryInfoList) msg.obj;
                if(mConsultCategoryInfoList != null){
                    handleData(mConsultCategoryInfoList);
                }
                break;
            case ValueConstants.MSG_GETCONSULT_ITEMPROPERTIES_KO:
                showErrorView(mRLAllContain, ErrorCode.NETWORK_UNAVAILABLE == msg.arg1 ?
                        IActionTitleBar.ErrorType.NETUNAVAILABLE : IActionTitleBar.ErrorType.ERRORNET, "", getString(R.string.error_view_network_loaderror_content), "", new ErrorViewClick() {
                    @Override
                    public void onClick(View view) {
                        doNetService();
                    }
                });
                break;
        }

    }

    private void handleData(ConsultCategoryInfoList value) {
        if(value.itemProperties != null && value.itemProperties.size() > 0){
            addLinearLayout(true, value.itemProperties);
        }
    }

    //添加子view
    private void addLinearLayout(boolean isFirsh, List<ItemProperty> value) {
        mLLDetailContain.removeAllViews();
        for (int i = 0; i < value.size(); i++) {
            ItemProperty itemProperty = value.get(i);
            View view = View.inflate(this, R.layout.item_service_detail_label, null);
            TextView text = (TextView) view.findViewById(R.id.tv_title_text1);
            EditText editText = (EditText) view.findViewById(R.id.et_cost_desc);
            text.setText(itemProperty.text);
            if(isFirsh){
                editText.setText(itemProperty.defaultDesc);
            }else {
                editText.setText(itemProperty.value);
            }
            mLLDetailContain.addView(view);
        }
    }

    private void initClick() {
        mBaseNavView.setLeftClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doCancleBack();
            }
        });

        mFinishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //完成操作
                onFinish();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(KeyEvent.KEYCODE_BACK == keyCode){
            doCancleBack();
        }
        return true;
    }

    //返回操作
    private void doCancleBack() {
        if(changeState()){
            mDialogCancle = DialogUtil.showMessageDialog(this, null, getString(R.string.release_detail_cancle_text),
                    getString(R.string.save), getString(R.string.release_detail_not_save), new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            onFinish();
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    });
            mDialogCancle.show();
        }else {
            finish();
        }
    }

    private boolean changeState() {
        //编辑
        if(mItemProperties != null && mItemProperties.size() > 0){
            if(mLLDetailContain != null && mLLDetailContain.getChildCount() > 0){
                for (int i = 0; i < mLLDetailContain.getChildCount(); i++) {
                    View childAt = mLLDetailContain.getChildAt(i);
                    EditText editText = (EditText) childAt.findViewById(R.id.et_cost_desc);
                    String str = editText.getText().toString().trim();
                    if(!str.equals(mItemProperties.get(i).value)){
                        return true;
                    }
                }
            }
        }

        if(mConsultCategoryInfoList != null && mConsultCategoryInfoList.itemProperties != null && mConsultCategoryInfoList.itemProperties.size() > 0){
            if(mLLDetailContain != null && mLLDetailContain.getChildCount() > 0){
                for (int i = 0; i < mLLDetailContain.getChildCount(); i++) {
                    View childAt = mLLDetailContain.getChildAt(i);
                    EditText editText = (EditText) childAt.findViewById(R.id.et_cost_desc);
                    String str = editText.getText().toString().trim();
                    if(!str.equals(mConsultCategoryInfoList.itemProperties.get(i).defaultDesc)){
                        return true;
                    }
                }
            }
        }

        return false;

        /*if(mDetailServiceBean != null){
            if(!StringUtil.isEmpty(mDetailServiceBean.costDesc)){
                if(!mDetailServiceBean.costDesc.equals(mCostDesc.getText().toString().trim())){
                    return true;
                }
            }else {
                if(!getString(R.string.release_detail_cost_text).equals(mCostDesc.getText().toString().trim())){
                    return true;
                }
            }

            if(!StringUtil.isEmpty(mDetailServiceBean.bookExplain)){
                if(!mDetailServiceBean.bookExplain.equals(mBookExplain.getText().toString().trim())){
                    return true;
                }
            }else {
                if(!getString(R.string.release_detail_book_text).equals(mBookExplain.getText().toString().trim())){
                    return true;
                }
            }

            if(!StringUtil.isEmpty(mDetailServiceBean.backRule)){
                if(!mDetailServiceBean.backRule.equals(mBackRule.getText().toString().trim())){
                    return true;
                }
            }else {
                if(!getString(R.string.release_detail_back_text).equals(mBackRule.getText().toString().trim())){
                    return true;
                }
            }

        }*/
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mDialogCancle != null){
            mDialogCancle.dismiss();
        }
    }

    private void onFinish() {
        if(mLLDetailContain != null && mLLDetailContain.getChildCount() > 0  && mConsultCategoryInfoList != null && mConsultCategoryInfoList.itemProperties != null
                && mConsultCategoryInfoList.itemProperties.size() > 0){
            if(mListContain == null){
                mListContain = new ArrayList<>();
            }
            mListContain.clear();
            for (int i = 0; i < mLLDetailContain.getChildCount(); i++) {
                View childAt = mLLDetailContain.getChildAt(i);
                ItemProperty itemProperty = new ItemProperty();
                //TextView text = (TextView) childAt.findViewById(R.id.tv_title_text1);
                EditText editText = (EditText) childAt.findViewById(R.id.et_cost_desc);
                itemProperty.value = editText.getText().toString().trim();
                itemProperty.text = mConsultCategoryInfoList.itemProperties.get(i).text;
                itemProperty.id = mConsultCategoryInfoList.itemProperties.get(i).id;
                itemProperty.type = mConsultCategoryInfoList.itemProperties.get(i).type;
                mListContain.add(itemProperty);
            }
        }

        if(mLLDetailContain != null && mLLDetailContain.getChildCount() > 0  && mItemProperties != null && mItemProperties.size() > 0){
            if(mListContain == null){
                mListContain = new ArrayList<>();
            }
            mListContain.clear();
            for (int i = 0; i < mLLDetailContain.getChildCount(); i++) {
                View childAt = mLLDetailContain.getChildAt(i);
                ItemProperty itemProperty = new ItemProperty();
                //TextView text = (TextView) childAt.findViewById(R.id.tv_title_text1);
                EditText editText = (EditText) childAt.findViewById(R.id.et_cost_desc);
                itemProperty.value = editText.getText().toString().trim();
                itemProperty.text = mItemProperties.get(i).text;
                itemProperty.id = mItemProperties.get(i).id;
                itemProperty.type = mItemProperties.get(i).type;
                mListContain.add(itemProperty);
            }
        }

        if(mListContain != null && mListContain.size() > 0){
            Intent intent = new Intent();
            intent.putExtra(SPUtils.EXTRA_DATA, (Serializable) mListContain);
            setResult(RESULT_OK, intent);
        }

        finish();

       /* DetailServiceBean detailServiceBean = new DetailServiceBean();
        if(!StringUtil.isEmpty(mCostDesc.getText().toString().trim())){
            detailServiceBean.costDesc = mCostDesc.getText().toString().trim();
        }
        if(!StringUtil.isEmpty(mBookExplain.getText().toString().trim())){
            detailServiceBean.bookExplain = mBookExplain.getText().toString().trim();
        }
        if(!StringUtil.isEmpty(mBackRule.getText().toString().trim())){
            detailServiceBean.backRule = mBackRule.getText().toString().trim();
        }
        Intent intent = new Intent();
        intent.putExtra(SPUtils.EXTRA_DATA, detailServiceBean);
        setResult(RESULT_OK, intent);
        finish();*/
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.ac_service_detail, null);
    }

    @Override
    public View onLoadNavView() {
        mBaseNavView = new BaseNavView(this);
        return mBaseNavView;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }
}
