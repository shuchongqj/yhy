package com.quanyan.yhy.ui.wallet.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.quanyan.base.BaseFragment;
import com.quanyan.yhy.R;
import com.yhy.common.utils.SPUtils;

/**
 * Created by Administrator on 2016/10/24.
 */
public class IDAuthenticSuccessFragment extends BaseFragment {

    @ViewInject(R.id.btn_confirm)
    private Button btnReload;

    @ViewInject(R.id.tv_user_name)
    private TextView tvUserName;

    @ViewInject(R.id.tv_id_card)
    private TextView tvIdCard;

    @ViewInject(R.id.tv_vaild)
    private TextView tvValid;


    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        btnReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getActivity().finish();

            }
        });

        Bundle bundle = this.getArguments();

        tvUserName.setText(bundle.getString(SPUtils.EXTRA_USER_NAME));
        tvIdCard.setText(bundle.getString(SPUtils.EXTRA_IDCARD));
        tvValid.setText(bundle.getString(SPUtils.EXTRA_VALIDDATE));
    }

    public static IDAuthenticSuccessFragment getInstance(String userName, String idCard, String ValidDate) {
        IDAuthenticSuccessFragment fragment = new IDAuthenticSuccessFragment();
        Bundle bundle = new Bundle();

        bundle.putString(SPUtils.EXTRA_USER_NAME, userName);
        bundle.putString(SPUtils.EXTRA_IDCARD, idCard);
        bundle.putString(SPUtils.EXTRA_VALIDDATE, ValidDate);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static IDAuthenticSuccessFragment getInstance() {
        IDAuthenticSuccessFragment fragment = new IDAuthenticSuccessFragment();

        return fragment;
    }

    @Override
    public View onLoadContentView() {
        View inflate = View.inflate(getActivity(), R.layout.fragment_authen_success, null);
        ViewUtils.inject(this, inflate);
        return inflate;
    }
}
