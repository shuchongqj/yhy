package com.quanyan.yhy.ui.master.view;

import android.util.SparseArray;
import android.view.ViewGroup;

import com.quanyan.yhy.ui.tab.view.hometab.BaseHomeView;
import com.quanyan.yhy.ui.tab.view.hometab.HomeViewInterface;

/**
 * Created with Android Studio.
 * Title:MasterConsultHomeDeliver
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-7-20
 * Time:15:48
 * Version 1.0
 * Description:
 */
public class MasterConsultHomeDeliver {

    private static SparseArray<HomeViewInterface> sMasterConsultHomeView;

    public MasterConsultHomeDeliver() {
        sMasterConsultHomeView = new SparseArray<>();
        sMasterConsultHomeView.put(0, new MasterConsultHomeBannerView());
        sMasterConsultHomeView.put(1, new MasterConsultHomeWKTView());
        sMasterConsultHomeView.put(2, new MasterConsultHomeSPView());
    }

    public static void deliverMasterConsultView(ViewGroup parentView) {
        for (int i = 0; i < sMasterConsultHomeView.size(); i++) {
            int key = sMasterConsultHomeView.keyAt(i);
            sMasterConsultHomeView.get(key).setView(parentView, key);
        }
    }

    public static void handleHeadData(Object o) {
        sMasterConsultHomeView.get(0).handleData(o);
    }

    public static void handleListData(Object o) {
        sMasterConsultHomeView.get(1).handleData(o);
    }

    public static void onResumeMasterConsult() {
        for (int i = 0; i < sMasterConsultHomeView.size(); i++) {
            int key = sMasterConsultHomeView.keyAt(i);
            if (sMasterConsultHomeView.get(key) instanceof BaseHomeView) {
                ((BaseHomeView) sMasterConsultHomeView.get(key)).onResume();
            }
        }
    }

    public static void onPauseMasterConsult() {
        for (int i = 0; i < sMasterConsultHomeView.size(); i++) {
            int key = sMasterConsultHomeView.keyAt(i);
            if (sMasterConsultHomeView.get(key) instanceof BaseHomeView) {
                ((BaseHomeView) sMasterConsultHomeView.get(key)).onPause();
            }
        }
    }

}
