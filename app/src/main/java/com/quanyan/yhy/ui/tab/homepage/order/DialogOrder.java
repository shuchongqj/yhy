package com.quanyan.yhy.ui.tab.homepage.order;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.view.View;

import com.quanyan.yhy.R;
import com.quanyan.yhy.ui.base.utils.DialogUtil;

/**
 * Created with Android Studio.
 * Title:Dialog
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:zhaoxp
 * Date:2015-12-12
 * Time:12:38
 * Version 1.0
 */
public class DialogOrder {

    public static Dialog cancelOrder(final Context context) {
        Resources resources = context.getApplicationContext().getResources();
        Dialog dialog = DialogUtil.showMessageDialog(context, resources.getString(R.string.dialog_order_cancel_title), resources.getString(R.string.dialog_order_cancel_content),
                resources.getString(R.string.label_btn_ok), resources.getString(R.string.cancel), new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        //确定
                        if (context instanceof Activity) {
                            ((Activity) context).finish();
                        }
                    }
                }, null);
        dialog.show();
        return dialog;
    }
}
