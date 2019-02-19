package com.newyhy.views.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.newyhy.adapter.InterestGridAdapter;
import com.newyhy.utils.DisplayUtils;
import com.newyhy.utils.SharedPreferenceUtil;
import com.quanyan.yhy.BuildConfig;
import com.quanyan.yhy.R;
import com.quanyan.yhy.net.NetManager;
import com.quanyan.yhy.net.lsn.OnResponseListener;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.smart.sdk.api.resp.Api_SNSCENTER_GuideTagResult;
import com.smart.sdk.api.resp.Api_SNSCENTER_GuideTagResultList;
import com.yhy.network.YhyCallback;
import com.yhy.network.YhyCaller;
import com.yhy.network.YhyCode;
import com.yhy.network.api.snscenter.SnsCenterApi;
import com.yhy.network.req.snscenter.SaveUserCorrelationReq;
import com.yhy.network.resp.Response;
import com.yhy.network.resp.snscenter.QueryGuidanceRecordResp;
import com.yhy.network.resp.snscenter.SaveUserCorrelationResp;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by nandy on 2018/6/20
 */
public class InterestSinglePageDialog extends Dialog implements View.OnClickListener {
    private TextView tvSkip;
    private ImageView btnNext;

    private List<Api_SNSCENTER_GuideTagResult> list;

    private RecyclerView recyclerView;
    private InterestGridAdapter adapter;
    private int screenWidth;

    private YhyCaller saveDataCall;

    private ProgressBar progressBar;

    private boolean loading;

    public InterestSinglePageDialog(Context context) {
        super(context, R.style.InterestDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_single_interest_layout);
        screenWidth = getContext().getResources().getDisplayMetrics().widthPixels;
        initView();
        setCanceledOnTouchOutside(false);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity = Gravity.BOTTOM;
        layoutParams.width = getContext().getResources().getDisplayMetrics().widthPixels;
        layoutParams.height = (int) (screenWidth * 620f / 414);
        getWindow().setAttributes(layoutParams);
        getData();
    }

    private void initView() {
        recyclerView = findViewById(R.id.dialog_interest_grid);
        tvSkip = findViewById(R.id.dialog_interest_skip);
        tvSkip.setOnClickListener(this);
        btnNext = findViewById(R.id.dialog_interest_next);
        btnNext.setOnClickListener(this);
        progressBar = findViewById(R.id.dialog_progressBar);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 4);
        recyclerView.setLayoutManager(gridLayoutManager);
        MyDivider myDivider = new MyDivider();
        recyclerView.addItemDecoration(myDivider);
    }

    private boolean savingData;

    @Override
    public void onClick(View v) {
        if (v.equals(this.tvSkip)) {
            saveData(null);
        } else if (v.equals(this.btnNext)) {
            if (adapter == null || savingData) {
                return;
            }
            List<Api_SNSCENTER_GuideTagResult> selectedList = adapter.getSelectedList();
            if (selectedList.size() == 0) {
                cancel();
                return;
            }
            savingData = true;
            List<String> idList = new ArrayList<>();
            for (Api_SNSCENTER_GuideTagResult result : selectedList) {
                idList.add(String.valueOf(result.id));
            }
            saveData(idList);
        }

    }


    private void getData() {
        NetManager.getInstance(getContext()).doGetGuideTagInfo(new OnResponseListener<Api_SNSCENTER_GuideTagResultList>() {
            @Override
            public void onComplete(boolean isOK, Api_SNSCENTER_GuideTagResultList result, int errorCode, String errorMsg) {
                if (isOK) {
                    list = result.guideTagResultList;
                    if (list != null && list.size() != 0) {
                        adapter = new InterestGridAdapter(list);
                        recyclerView.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                ToastUtil.showToast(getContext(), errorMessage);
            }
        });

    }

    private void saveData(List<String> tagList) {
        if (progressBar.getVisibility() == View.VISIBLE) {
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        YhyCallback<Response<SaveUserCorrelationResp>> callback = new YhyCallback<Response<SaveUserCorrelationResp>>() {
            @Override
            public void onSuccess(Response<SaveUserCorrelationResp> data) {
                progressBar.setVisibility(View.GONE);
                if (data.getContent().getValue()) {
                    cancel();
                }
            }

            @Override
            public void onFail(YhyCode code, Exception exception) {
                if (BuildConfig.DEBUG) {
                    ToastUtil.showToast(getContext(), exception.getMessage());
                }
                progressBar.setVisibility(View.GONE);
                cancel();
            }
        };
        saveDataCall = new SnsCenterApi().saveUserCorrlation(new SaveUserCorrelationReq(tagList), callback).execAsync();
    }

    private class MyDivider extends RecyclerView.ItemDecoration {
        public MyDivider() {
            super();
        }

        @Override
        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
            super.onDraw(c, parent, state);
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.top = DisplayUtils.dp2px(getContext(), 22);
            outRect.left = DisplayUtils.dp2px(getContext(), 4);
            outRect.right = DisplayUtils.dp2px(getContext(), 4);

        }
    }
}
