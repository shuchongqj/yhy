package com.newyhy.views.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.newyhy.adapter.InterestGridAdapter;
import com.quanyan.yhy.R;
import com.quanyan.yhy.net.NetManager;
import com.quanyan.yhy.net.lsn.OnResponseListener;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.smart.sdk.api.resp.Api_SNSCENTER_TagResult;
import com.smart.sdk.api.resp.Api_SNSCENTER_TagResultList;
import com.yhy.common.base.NoLeakHandler;

import java.util.List;

/**
 * Create by nandy on 2018/6/20
 */
public class InterestMultiPageDialog extends Dialog implements View.OnClickListener, NoLeakHandler.HandlerCallback {
    private TextView tvSkip;
    private View panel_parent, panel1, panel2, panel3;
    private View sexMale, sexFemale;
    private Button btnNext;
    private int screenWidth;
    private NoLeakHandler handler = new NoLeakHandler(this);
    private int currentScrollX;
    private int currentPage = 0;//0性别页 1兴趣页  2用途页
    private boolean animationRunning;

    private List<Api_SNSCENTER_TagResult> list1, list2;

    private RecyclerView recyclerView1, recyclerView2;
    private InterestGridAdapter interestGridAdapter1, interestGridAdapter2;
    private SEX sex;

    private enum SEX {BOY, GIRL}

    public InterestMultiPageDialog(@NonNull Context context) {
        super(context, R.style.InterestDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_interest_layout);
        screenWidth = getContext().getResources().getDisplayMetrics().widthPixels;
        initView();
        setCanceledOnTouchOutside(false);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity = Gravity.BOTTOM;
        layoutParams.width = getContext().getResources().getDisplayMetrics().widthPixels;
        layoutParams.height = (int) (screenWidth * 650f / 414);
        getWindow().setAttributes(layoutParams);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getPage2Data();
    }

    private void initView() {
        tvSkip = findViewById(R.id.dialog_interest_skip);
        tvSkip.setOnClickListener(this);
        panel_parent = findViewById(R.id.dialog_interest_panel_parent);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) panel_parent.getLayoutParams();
        layoutParams.width = screenWidth * 3;
        panel_parent.setLayoutParams(layoutParams);

        panel1 = findViewById(R.id.dialog_interest_panel1);

        RelativeLayout.LayoutParams layoutParams1 = (RelativeLayout.LayoutParams) panel1.getLayoutParams();
        layoutParams1.width = screenWidth;
        panel1.setLayoutParams(layoutParams1);

        panel2 = findViewById(R.id.dialog_interest_panel2);

        RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) panel2.getLayoutParams();
        layoutParams2.width = screenWidth;
        panel2.setLayoutParams(layoutParams2);

        panel3 = findViewById(R.id.dialog_interest_panel3);

        RelativeLayout.LayoutParams layoutParams3 = (RelativeLayout.LayoutParams) panel3.getLayoutParams();
        layoutParams3.width = screenWidth;
        panel3.setLayoutParams(layoutParams3);

        btnNext = findViewById(R.id.dialog_interest_next);
        btnNext.setOnClickListener(this);

        sexMale = findViewById(R.id.dialog_interest_sex_male);
        sexMale.setOnClickListener(this);
        ImageView imgMale = sexMale.findViewById(R.id.dialog_interest_item_image);
        imgMale.setImageResource(R.mipmap.ic_interest_male);
        sexMale.findViewById(R.id.dialog_interest_item_selected).setVisibility(View.GONE);
        sexMale.findViewById(R.id.dialog_interest_item_background).setSelected(false);
        TextView tvMale = sexMale.findViewById(R.id.dialog_interest_item_text);
        tvMale.setText("男");


        sexFemale = findViewById(R.id.dialog_interest_sex_female);
        sexFemale.setOnClickListener(this);
        sexFemale.findViewById(R.id.dialog_interest_item_background).setSelected(false);
        TextView tvFemale = sexFemale.findViewById(R.id.dialog_interest_item_text);
        tvFemale.setText("女");
        sexFemale.findViewById(R.id.dialog_interest_item_selected).setVisibility(View.GONE);
        ImageView imgFemale = sexFemale.findViewById(R.id.dialog_interest_item_image);
        imgFemale.setImageResource(R.mipmap.ic_interest_female);

        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(getContext(), 4);
        GridLayoutManager gridLayoutManager2 = new GridLayoutManager(getContext(), 4);

        recyclerView1 = findViewById(R.id.dialog_interest_grid1);
        recyclerView1.setLayoutManager(gridLayoutManager1);

        recyclerView2 = findViewById(R.id.dialog_interest_grid2);
        recyclerView2.setLayoutManager(gridLayoutManager2);

    }

    private void setSex(SEX sex) {
        if (sex == SEX.GIRL) {
            this.sex = sex;
            sexFemale.findViewById(R.id.dialog_interest_item_background).setSelected(true);
            sexMale.findViewById(R.id.dialog_interest_item_background).setSelected(false);
            sexFemale.findViewById(R.id.dialog_interest_item_selected).setVisibility(View.VISIBLE);
            sexMale.findViewById(R.id.dialog_interest_item_selected).setVisibility(View.GONE);

        } else if (sex == SEX.BOY) {
            this.sex = sex;
            sexFemale.findViewById(R.id.dialog_interest_item_background).setSelected(false);
            sexMale.findViewById(R.id.dialog_interest_item_background).setSelected(true);
            sexFemale.findViewById(R.id.dialog_interest_item_selected).setVisibility(View.GONE);
            sexMale.findViewById(R.id.dialog_interest_item_selected).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.equals(this.tvSkip)) {
            this.dismiss();
        } else if (v.equals(this.btnNext)) {
            if (animationRunning) {
                return;
            }
            currentScrollX = 0;
            if (currentPage == 2) {
                dismiss();
            } else {
                v.setSelected(true);
                nextPage();
            }

        } else if (v.equals(this.sexFemale)) {
            setSex(SEX.GIRL);
        } else if (v.equals(this.sexMale)) {
            setSex(SEX.BOY);
        }
    }

    private void nextPage() {
        if (currentPage == 0 && (list1 == null || list1.size() == 0)) {
            getPage2Data();//获取失败,再次获取数据
            return;
        }
        if (currentPage == 1 && (list2 == null || list2.size() == 0)) {
            getPage3Data();//获取失败,再次获取数据
            return;
        }
        animationRunning = true;
        handler.sendEmptyMessageDelayed(100, delay);
    }

    private final int step = 80;
    private final int delay = 22;

    @Override
    public void handleMessage(Message msg) {
        currentScrollX += step;
        panel_parent.scrollBy(step, 0);
        if (screenWidth - currentScrollX > step) {
            handler.sendEmptyMessageDelayed(100, delay);
        } else {
            animationRunning = false;
            panel_parent.scrollBy(screenWidth - currentScrollX, 0);
            currentPage++;
            if (currentPage == 2) {
                btnNext.setText("开启运动之旅");
            }
        }

    }


    private void getPage2Data() {

    }

    private void getPage3Data() {


    }
}
