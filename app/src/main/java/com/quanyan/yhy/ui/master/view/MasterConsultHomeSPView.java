package com.quanyan.yhy.ui.master.view;

import android.view.View;
import android.view.ViewGroup;

import com.quanyan.yhy.R;
import com.quanyan.yhy.ui.tab.view.hometab.HomeViewInterface;

/**
 * Created with Android Studio.
 * Title:MasterConsultHomeSPView
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-7-20
 * Time:15:59
 * Version 1.0
 * Description:
 */
public class MasterConsultHomeSPView implements HomeViewInterface {

    private View mConsultSPView;

    @Override
    public void setView(ViewGroup parentView, int index) {
        mConsultSPView = View.inflate(parentView.getContext(), R.layout.view_masterhome_consult_sp, null);
        if (index <= parentView.getChildCount()) {
            parentView.addView(mConsultSPView, index);
        } else {
            parentView.addView(mConsultSPView);
        }
    }

    @Override
    public void handleData(Object data) {

    }
}
