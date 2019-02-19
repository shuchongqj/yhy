package com.quanyan.pedometer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.Toast;

import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.yhy.common.beans.net.model.ShareBean;

import java.io.File;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:AndroidShareUtil
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:赵晓坡
 * Date:16/7/14
 * Time:15:50
 * Version 1.1.0
 */
public class AndroidShareUtil {

    public static final int TEXT = 1;
    public static final int IMAGE = 2;

    public static void shareQQFriend(Context context, String msgTitle, String msgText, int type, String imgPath){
//        shareMsg(context,"com.tencent.mobileqq",
//                "com.tencent.mobileqq.activity.JumpActivity", "QQ", msgTitle,
//                msgText, type, imgPath);

        ShareBean shareBean = new ShareBean();
        shareBean.shareImageLocal = imgPath;
        shareBean.shareWay = 3;
        shareBean.isNeedSyncToDynamic = false;
        if (!StringUtil.isEmpty(msgText)) {
//            shareBean.shareContent = (StringUtil.isEmpty(weiboTopicName) ? "" : weiboTopicName) + shareContent + qrCodeUrl;
        }
        NavUtils.gotoShareTableActivity(context, shareBean, true);
    }

    public static void shareWeChatFriend(Context context,String msgTitle, String msgText, int type, String imgPath) {

        shareMsg(context, "com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI", "微信",
                msgTitle, msgText, type, imgPath);
    }

    public static void shareWeChatFriendCircle(Context context,String msgTitle, String msgText,int type,String imgPath) {
        shareMsg(context, "com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI",
                "微信", msgTitle, msgText, type, imgPath);
    }

    private static void  shareMsg(Context context, String componentName, String componentUI, String where, String msgTitle, String msgText, int type, String imgPath) {
        if (!componentName.isEmpty() && !isAvilible(context, componentName)) {
            Toast.makeText(context, "您未安装" + where, Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        Uri imageUri = Uri.fromFile(new File(imgPath));
        intent.putExtra(Intent.EXTRA_STREAM, imageUri);

//        intent.putExtra(Intent.EXTRA_SUBJECT, msgTitle);
//        intent.putExtra(Intent.EXTRA_TEXT, msgText);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        if(!componentName.isEmpty()){
            intent.setComponent(new ComponentName(componentName, componentUI));
            context.startActivity(intent);
        }else{
            context.startActivity(Intent.createChooser(intent, msgTitle));
        }
    }

    public static boolean isAvilible(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();

        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        for (int i = 0; i < pinfo.size(); i++) {
            if (((PackageInfo) pinfo.get(i)).packageName
                    .equalsIgnoreCase(packageName))
                return true;
        }
        return false;
    }
}
