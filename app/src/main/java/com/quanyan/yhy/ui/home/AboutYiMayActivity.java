package com.quanyan.yhy.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.harwkin.nb.camera.ImageUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.util.LocalUtils;
import com.quanyan.base.view.BaseNavView;
import com.quanyan.yhy.R;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.common.WeiXinPublicCodeActivity;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.yhy.common.beans.net.model.param.WebParams;
import com.yhy.common.utils.AndroidUtils;
import com.yhy.common.utils.SPUtils;

/**
 * 关于quanyan on 2015/11/12.
 */
public class AboutYiMayActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.tv_service)
    private TextView tv_service;
    @ViewInject(R.id.tv_weixin)
    private TextView tv_weixin;
    @ViewInject(R.id.tv_phone)
    private TextView tv_phone;
    @ViewInject(R.id.tv_version_info)
    private TextView tv_version_info;

    @ViewInject(R.id.tv_content1)
    private TextView tv_content1;

    @ViewInject(R.id.tv_content2)
    private TextView tv_content2;

    @ViewInject(R.id.tv_content3)
    private TextView tv_content3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ViewUtils.inject(this);
//        ((ImageView)findViewById(R.id.ac_about_us_bg)).setImageResource(R.mipmap.about_us_image);
        tv_content1.setText(R.string.about_us_content1);
        tv_content2.setText(R.string.about_us_content2);
        tv_content3.setText(R.string.about_us_content3);
        tv_version_info.setText(getString(R.string.label_copyright)+ AndroidUtils.getVersion(this));
        initEvent();
    }

    private void initEvent() {
        tv_service.setOnClickListener(this);
        tv_weixin.setOnClickListener(this);
        tv_phone.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_service:
                if(SPUtils.getServiceProtocol(this) != null) {
                    WebParams wp = new WebParams();
                    wp.setUrl(ImageUtils.getH5FullUrl(SPUtils.getServiceProtocol(this)));
                    NavUtils.openBrowser(this, wp);
                }else{
                    ToastUtil.showToast(this,getString(R.string.toast_server_sys_config_error));
                }
                break;
            case R.id.tv_weixin:
                Intent intent_1 = new Intent(AboutYiMayActivity.this, WeiXinPublicCodeActivity.class);
                startActivity(intent_1);
                overridePendingTransition(R.anim.push_up_in1, R.anim.push_up_out1);
                break;
            case R.id.tv_phone:
                LocalUtils.call(AboutYiMayActivity.this, SPUtils.getServicePhone(this));
                break;
            default:
                break;
        }
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.ac_about_up, null);
    }

    private BaseNavView mBaseNavView;

    @Override
    public View onLoadNavView() {
        mBaseNavView = new BaseNavView(this);
        mBaseNavView.setTitleText(getString(R.string.label_settings_about_us));
        return mBaseNavView;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }
}
