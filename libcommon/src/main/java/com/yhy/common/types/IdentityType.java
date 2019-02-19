package com.yhy.common.types;

import android.content.Context;
import android.widget.Button;
import android.widget.TextView;

import com.yhy.common.R;

/**
 * Created with Android Studio.
 * Title:IdentityType
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:baojie
 * Date:2015-12-16
 * Time:15:32
 * Version 1.0
 */

public class IdentityType {

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
