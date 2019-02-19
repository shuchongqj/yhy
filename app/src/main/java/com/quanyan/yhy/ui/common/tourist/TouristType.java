package com.quanyan.yhy.ui.common.tourist;

import android.content.Context;
import android.widget.Button;
import android.widget.TextView;

import com.quanyan.yhy.R;

/**
 * Created with Android Studio.
 * Title:TouristType
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-6-6
 * Time:14:58
 * Version 1.0
 * Description:
 */
public class TouristType {

    //我的-常用游客
    public static final int MIMETOURIST = 1;

    //订单选择游客
    public static final int ORDERTOURIST = 2;

    //订单选择联系人
    public static final int ORDERCONTACTS = 3;

    //境内游
    public static final String TRAVELIN = "TRAVELIN";
    //境外游
    public static final String TRAVELOUT = "TRAVELOUT";

    //身份证type
    public static final String IDCertificateType = "1";

    //护照type
    public static final String PassportCertificateType = "2";

    //军人证type
    public static final String MilitaryCertificateType = "3";

    //港澳通行证type
    public static final String HkMacaoCertificateType = "4";

    //添加证件req
    public static final int ADD_CODE_REQ = 0x001;

    //编辑证件req
    public static final int EDIT_CODE_REQ = 0x002;



    //身份证
    public static final String IDCARD = "1";
    public static final int IDCARD_INT = 0;
    //护照
    public static final String PASSPORT = "2";
    public static final int PASSPORT_INT = 1;
    //军人证
    public static final String ISSOLDIERCARD = "3";
    public static final int ISSOLDIERCARD_INT = 2;
    //港澳通行证
    public static final String ISHKCARD = "4";
    public static final int ISHKCARD_INT = 3;
    //国内
    public static final String HOME = "home";
    //国际
    public static final String ABROAD = "abroad";


    //根据传入的类型获取证件类型
    public static String showIdType(Context context, String certificatesType) {
        String[] idTypes = context.getResources().getStringArray(R.array.id_type);
        String idType = idTypes[0];
        if (PASSPORT.equals(certificatesType)) {
            idType = idTypes[1];
        } else if (ISSOLDIERCARD.equals(certificatesType)) {
            idType = idTypes[2];
        } else if (ISHKCARD.equals(certificatesType)) {
            idType = idTypes[3];
        }
        return idType;
    }

    //根据证件类型转换服务器类型
    public static String deShowIdType(Context context, String idType) {
        String deIdType;
        if (idType.equals(context.getResources().getStringArray(R.array.id_type)[0])) {
            deIdType = IDCARD;
        } else if (idType.equals(context.getResources().getStringArray(R.array.id_type)[1])) {
            deIdType = PASSPORT;
        } else if (idType.equals(context.getResources().getStringArray(R.array.id_type)[2])) {
            deIdType = ISSOLDIERCARD;
        } else {
            deIdType = ISHKCARD;
        }
        return deIdType;
    }
    //按钮可点状态
    public static void changeButtonClick(Context context, TextView button){
        button.setTextColor(context.getResources().getColor(R.color.neu_333333));
        button.setClickable(true);
    }
    //按钮不可点击状态
    public static void changeButtonUnClick(Context context, TextView button){
        button.setTextColor(context.getResources().getColor(R.color.neu_333333));
        button.setClickable(false);
    }

    //按钮可点状态
    public static void changeButtonClick(Context context, Button button){
        button.setTextColor(context.getResources().getColor(R.color.neu_333333));
        button.setClickable(true);
    }
    //按钮不可点击状态
    public static void changeButtonUnClick(Context context, Button button){
        button.setTextColor(context.getResources().getColor(R.color.neu_cccccc));
        button.setClickable(false);
    }


}
