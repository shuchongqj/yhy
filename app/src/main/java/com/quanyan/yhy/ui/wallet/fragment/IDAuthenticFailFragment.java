package com.quanyan.yhy.ui.wallet.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.quanyan.base.BaseFragment;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.UpdateIDCardState;

import de.greenrobot.event.EventBus;


/**********************************************
 * @desc 通用Dialog
 * @authoer enginekiss
 * @date 2016/1/11 12:12
 ***********************************************/
public class IDAuthenticFailFragment extends BaseFragment {
    @ViewInject(R.id.btn_takephoto_agin)
    private Button btnReload;

    public static IDAuthenticFailFragment getInstance() {
        IDAuthenticFailFragment fragment = new IDAuthenticFailFragment();
        return fragment;
    }


    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        btnReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new UpdateIDCardState());
                getActivity().finish();
            }
        });
    }

    @Override
    public View onLoadContentView() {
        View inflate = View.inflate(getActivity(), R.layout.fragment_authen_fail, null);
        ViewUtils.inject(this, inflate);
        return inflate;
    }
}
