package com.newyhy.activity;

import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.harwkin.nb.camera.ImageUtils;
import com.newyhy.views.CirclePgBar;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.SysConfigType;
import com.quanyan.yhy.libanalysis.AnArgs;
import com.quanyan.yhy.libanalysis.AnEvent;
import com.quanyan.yhy.libanalysis.Analysis;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.yhy.common.base.BaseNewActivity;
import com.yhy.common.beans.net.model.RCShowcase;
import com.yhy.common.utils.SPUtils;
import com.yhy.imageloader.ImageLoadManager;

import org.json.JSONException;

import java.util.HashMap;

import static com.yhy.router.RouterPath.ACTIVITY_AD;

@Route(path = ACTIVITY_AD)
public class NewAdActivity extends BaseNewActivity implements View.OnClickListener {

    private CirclePgBar circlePgBar;
    private ImageView mAdImage;
    private RCShowcase mAdModel;

    @Override
    public void handleMessage(Message msg) {

    }

    @Override
    protected int setLayoutId() {
        return R.layout.ad_activity;
    }


    @Override
    protected void initView() {
        super.initView();
        circlePgBar = findViewById(R.id.circle_pg_bar);
        mAdImage = findViewById(R.id.iv_ad);
        circlePgBar.setTimesUpCallback(new TimesUpRunnable());
    }

    @Override
    protected void initData() {
        super.initData();
        circlePgBar.setOnClickListener(this);
        initAd();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        circlePgBar.setTimesUpCallback(null);
        circlePgBar.setOnClickListener(null);
    }

    /**
     * 初始化广告页
     */
    private void initAd() {
        String ad = SPUtils.getString(this, SysConfigType.AD);
        if (!TextUtils.isEmpty(ad)) {
            try {

                mAdModel = RCShowcase.deserialize(ad);
                if (mAdModel == null || TextUtils.isEmpty(mAdModel.imgUrl)) {
                    return;
                }
                mAdImage.setVisibility(View.VISIBLE);
                String img_url = ImageUtils.getImageFullUrl(mAdModel.imgUrl);
                ImageLoadManager.loadImage(img_url, R.mipmap.about_us_logo_pedometr_share, mAdImage);

                mAdImage.setOnClickListener(v -> {
                    //事件统计
                    HashMap<String, String> map = AnArgs.Instance().build(AnArgs.OPERATION_CONTENT, mAdModel.operationContent).getMap();
                    Analysis.pushEvent(this, AnEvent.QIDONGYE, String.valueOf(mAdModel.id), map);
                    NavUtils.depatchAllJump(this, mAdModel, -1);
                    finish();

                });
            } catch (JSONException e) {
            }
        }
    }


    @Override
    public void onClick(View v) {
        circlePgBar.setTimesUpCallback(null);
        circlePgBar.setOnClickListener(null);
        finish();
        overridePendingTransition(R.anim.anim_pop_in, R.anim.anim_pop_out);
    }

    private class TimesUpRunnable implements Runnable {

        @Override
        public void run() {
            finish();
            overridePendingTransition(R.anim.anim_pop_in, R.anim.anim_pop_out);
        }
    }
}
