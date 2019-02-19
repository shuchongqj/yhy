package com.quanyan.yhy.ui.base.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.quanyan.base.view.LoadingDialog;
import com.quanyan.yhy.R;
import com.quanyan.yhy.ui.integralmall.integralmallinterface.OrderFilterInterface;
import com.quanyan.yhy.ui.order.entity.FilterCondition;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class DialogUtil {

    /**
     * 获取加载等待对话框
     *
     * @param context
     * @param message
     * @param isCancelable
     * @return
     */
    public static LoadingDialog showLoadingDialog(Context context, String message, boolean isCancelable) {
        LoadingDialog dialog = new LoadingDialog(context, R.style.loading_dialog);
        //沉浸式状态栏的设置
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = dialog.getWindow();
// Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
// Translucent navigation bar
//            window.setFlags(
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        dialog.setCancelable(isCancelable);
        dialog.setDlgMessage(message);
//		InterestMultiPageDialog.getWindow().setType((WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
        return dialog;
    }

    /**
     * 弹出自定义的对话框
     *
     * @param context
     * @param title
     * @param content
     * @param positiveBtnLabel
     * @param negativeBtnLabel
     * @param posLsn
     * @param ngLsn
     * @return
     */
    public static Dialog showMessageDialog(Context context,
                                           String title,
                                           String content,
                                           String positiveBtnLabel,
                                           String negativeBtnLabel,
                                           View.OnClickListener posLsn,
                                           View.OnClickListener ngLsn) {

        return showMessageDialog(context, title, content, positiveBtnLabel, negativeBtnLabel, null, posLsn, ngLsn, null);
    }

    public static Dialog showMessageDialog(Context context,
                                           String title,
                                           String content,
                                           String positiveBtnLabel,
                                           String negativeBtnLabel,
                                           String btnLabel3,
                                           View.OnClickListener posLsn,
                                           View.OnClickListener ngLsn,
                                           View.OnClickListener btn3Lsn) {
        LayoutInflater mLayoutInflater = LayoutInflater.from(context);
        View view = mLayoutInflater.inflate(R.layout.custom_msg_dlg, null);
        final Dialog dialog = new Dialog(context, R.style.kangzai_dialog);
//		InterestMultiPageDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.setContentView(view);
        dialog.setCancelable(false);

        TextView msg_dlg_title;
        TextView msg_dlg_content;
        TextView msg_dialog_btn_cancel;
        TextView msg_dialog_btn_ok;
        TextView msg_dialog_btn_3;

        msg_dlg_title = (TextView) view.findViewById(R.id.msg_dlg_title);
        msg_dlg_content = (TextView) view.findViewById(R.id.msg_dlg_content);
        msg_dialog_btn_cancel = (TextView) view.findViewById(R.id.msg_dialog_btn_cancel);
        msg_dialog_btn_ok = (TextView) view.findViewById(R.id.msg_dialog_btn_ok);
        msg_dialog_btn_3 = (TextView) view.findViewById(R.id.msg_dialog_btn_1);
        if (!TextUtils.isEmpty(title)) {
            msg_dlg_title.setText(title);
        } else {
            msg_dlg_title.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(content)) {
            msg_dlg_content.setVisibility(View.VISIBLE);
            msg_dlg_content.setText(content);
        } else {
            msg_dlg_content.setVisibility(View.GONE);
        }
        if (negativeBtnLabel != null) {
            msg_dialog_btn_cancel.setVisibility(View.VISIBLE);
            msg_dialog_btn_cancel.setText(negativeBtnLabel);
        } else {
            msg_dialog_btn_cancel.setVisibility(View.GONE);
        }
        if (positiveBtnLabel != null) {
            msg_dialog_btn_ok.setVisibility(View.VISIBLE);
            msg_dialog_btn_ok.setText(positiveBtnLabel);
        } else {
            msg_dialog_btn_ok.setVisibility(View.GONE);
        }

        if (btnLabel3 != null) {
            msg_dialog_btn_3.setVisibility(View.VISIBLE);
            msg_dialog_btn_3.setText(btnLabel3);
        } else {
            msg_dialog_btn_3.setVisibility(View.GONE);
        }
        if (ngLsn == null) {
            msg_dialog_btn_cancel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        } else {
            msg_dialog_btn_cancel.setOnClickListener(ngLsn);
        }
        if (null == posLsn) {
            msg_dialog_btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != dialog) {
                        dialog.dismiss();
                    }
                }
            });
        } else {
            msg_dialog_btn_ok.setOnClickListener(posLsn);
        }

        if (null == btn3Lsn) {
            msg_dialog_btn_3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != dialog) {
                        dialog.dismiss();
                    }
                }
            });
        } else {
            msg_dialog_btn_3.setOnClickListener(btn3Lsn);
        }
        return dialog;
    }


    public static Dialog showMessageDialog2(Context context,
                                            String title,
                                            String content,
                                            String positiveBtnLabel,
                                            String negativeBtnLabel,
                                            View.OnClickListener posLsn,
                                            View.OnClickListener ngLsn) {

        return showMessageDialog2(context, title, content, positiveBtnLabel, negativeBtnLabel, null, posLsn, ngLsn, null);
    }

    public static Dialog showMessageDialog2(Context context,
                                            String title,
                                            String content,
                                            String positiveBtnLabel,
                                            String negativeBtnLabel,
                                            String btnLabel3,
                                            View.OnClickListener posLsn,
                                            View.OnClickListener ngLsn,
                                            View.OnClickListener btn3Lsn) {
        LayoutInflater mLayoutInflater = LayoutInflater.from(context);
        View view = mLayoutInflater.inflate(R.layout.custom_msg_dlg2, null);
        final Dialog dialog = new Dialog(context, R.style.kangzai_dialog);
//		InterestMultiPageDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.setContentView(view);
        dialog.setCancelable(false);

        TextView msg_dlg_title;
        TextView msg_dlg_content;
        TextView msg_dialog_btn_cancel;
        TextView msg_dialog_btn_ok;
        TextView msg_dialog_btn_3;

        msg_dlg_title = (TextView) view.findViewById(R.id.msg_dlg_title);
        msg_dlg_content = (TextView) view.findViewById(R.id.msg_dlg_content);
        msg_dialog_btn_cancel = (TextView) view.findViewById(R.id.msg_dialog_btn_cancel);
        msg_dialog_btn_ok = (TextView) view.findViewById(R.id.msg_dialog_btn_ok);
        msg_dialog_btn_3 = (TextView) view.findViewById(R.id.msg_dialog_btn_1);
        TextPaint tp = msg_dlg_title.getPaint();
        tp.setFakeBoldText(true);
        if (title != null) {
            msg_dlg_title.setText(title);
        } else {
            msg_dlg_title.setVisibility(View.GONE);
        }
        if (content != null) {
            msg_dlg_content.setVisibility(View.VISIBLE);
            msg_dlg_content.setText(content);
        } else {
            msg_dlg_content.setVisibility(View.GONE);
        }
        if (negativeBtnLabel != null) {
            msg_dialog_btn_cancel.setVisibility(View.VISIBLE);
            msg_dialog_btn_cancel.setText(negativeBtnLabel);
        } else {
            msg_dialog_btn_cancel.setVisibility(View.GONE);
        }
        if (positiveBtnLabel != null) {
            msg_dialog_btn_ok.setVisibility(View.VISIBLE);
            msg_dialog_btn_ok.setText(positiveBtnLabel);
        } else {
            msg_dialog_btn_ok.setVisibility(View.GONE);
        }

        if (btnLabel3 != null) {
            msg_dialog_btn_3.setVisibility(View.VISIBLE);
            msg_dialog_btn_3.setText(btnLabel3);
        } else {
            msg_dialog_btn_3.setVisibility(View.GONE);
        }
        if (ngLsn == null) {
            msg_dialog_btn_cancel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        } else {
            msg_dialog_btn_cancel.setOnClickListener(ngLsn);
        }
        if (null == posLsn) {
            msg_dialog_btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != dialog) {
                        dialog.dismiss();
                    }
                }
            });
        } else {
            msg_dialog_btn_ok.setOnClickListener(posLsn);
        }

        if (null == btn3Lsn) {
            msg_dialog_btn_3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != dialog) {
                        dialog.dismiss();
                    }
                }
            });
        } else {
            msg_dialog_btn_3.setOnClickListener(btn3Lsn);
        }
        return dialog;
    }

    /**
     * 提示消息对话框
     *
     * @param context
     * @param title
     * @param content
     * @param positiveBtnLabel
     * @param negativeBtnLabel
     * @param posLsn
     * @param ngLsn
     * @param leftBtnStyle
     * @param rightBtnStyle
     * @return
     */
    public static Dialog showMessageDialog(Context context,
                                           String title,
                                           String content,
                                           String positiveBtnLabel,
                                           String negativeBtnLabel,
                                           View.OnClickListener posLsn,
                                           View.OnClickListener ngLsn, int leftBtnStyle, int
                                                   rightBtnStyle) {
        Dialog dialog = showMessageDialog(context, title, content, positiveBtnLabel,
                negativeBtnLabel, posLsn, ngLsn);
        if (null != dialog) {
            TextView btnLeft = (TextView) dialog.findViewById(R.id.msg_dialog_btn_cancel);
            TextView btnRight = (TextView) dialog.findViewById(R.id.msg_dialog_btn_ok);
            btnLeft.setTextAppearance(context, leftBtnStyle);
            btnRight.setTextAppearance(context, rightBtnStyle);
        }
        return dialog;
    }

    public static Dialog getMenuDialog(Activity context, View view) {
        return getMenuDialog(context, view, Gravity.BOTTOM, 0, 0);
    }


    public static Dialog showLogisticalDialog(final Context context, final List<FilterCondition> dataList, int currentIndex, final OrderFilterInterface mOnOrderFilterListener) {

        /*LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RelativeLayout layout = (RelativeLayout) inflater.inflate(
                R.layout.alert_dialog_logistical_layout, null);
        GridView gridView = (GridView) layout.findViewById(R.id.gridview);

        final GridViewFilterAdapter adapter = new GridViewFilterAdapter(context, dataList, currentIndex);
        gridView.setAdapter(adapter);


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Map<Integer, Boolean> isSelectedMap = adapter.isSelected;

                for (Integer p : isSelectedMap.keySet()) {
                    isSelectedMap.put(p, false);
                }
                ViewHolder holder = (ViewHolder) view.getTag();
                isSelectedMap.put(position, true);
                adapter.notifyDataSetChanged();
                mOnOrderFilterListener.OnFilterClick(position, dataList.get(position).getConId(), dataList.get(position).getConName());


            }
        });
        final Dialog InterestMultiPageDialog = new Dialog(context, R.style.MenuDialogStyle);

        InterestMultiPageDialog.setContentView(layout);
        Window window = InterestMultiPageDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();

        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        int screenW = (int) (d.widthPixels);
        window.setGravity(Gravity.TOP);
        lp.width = screenW;

        InterestMultiPageDialog.setCanceledOnTouchOutside(true);
        window.setAttributes(lp);
        window.setWindowAnimations(R.style.FilterDialogAnimation); // 添加动画
        InterestMultiPageDialog.show();*/
        return null;
    }

    /**
     * 底部弹出Dialog
     *
     * @param context
     * @param view
     * @return
     */

    public static Dialog getMenuDialog(Activity context, View view, int gravity, int width, int height) {

        final Dialog dialog = new Dialog(context, R.style.MenuDialogStyle);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        int screenW = getScreenWidth(context);
        window.setGravity(gravity); // 此处可以设置dialog显示的位置
        // int screenH = getScreenHeightExcludeStatusBar(context);
         /*
         * lp.x与lp.y表示相对于原始位置的偏移.
         * 当参数值包含Gravity.LEFT时,对话框出现在左边,所以lp.x就表示相对左边的偏移,负值忽略.
         * 当参数值包含Gravity.RIGHT时,对话框出现在右边,所以lp.x就表示相对右边的偏移,负值忽略.
         * 当参数值包含Gravity.TOP时,对话框出现在上边,所以lp.y就表示相对上边的偏移,负值忽略.
         * 当参数值包含Gravity.BOTTOM时,对话框出现在下边,所以lp.y就表示相对下边的偏移,负值忽略.
         * 当参数值包含Gravity.CENTER_HORIZONTAL时
         * ,对话框水平居中,所以lp.x就表示在水平居中的位置移动lp.x像素,正值向右移动,负值向左移动.
         * 当参数值包含Gravity.CENTER_VERTICAL时
         * ,对话框垂直居中,所以lp.y就表示在垂直居中的位置移动lp.y像素,正值向右移动,负值向左移动.
         * gravity的默认值为Gravity.CENTER,即Gravity.CENTER_HORIZONTAL |
         * Gravity.CENTER_VERTICAL.
         *
         * 本来setGravity的参数值为Gravity.LEFT | Gravity.TOP时对话框应出现在程序的左上角,但在
         * 我手机上测试时发现距左边与上边都有一小段距离,而且垂直坐标把程序标题栏也计算在内了,
         * Gravity.LEFT, Gravity.TOP, Gravity.BOTTOM与Gravity.RIGHT都是如此,据边界有一小段距离
         */
        if (width > 0) {
            lp.width = width;
        } else {
            lp.width = screenW;
        }
        if (height > 0) {
            lp.height = height;
        }
//		lp.x = 0;
//		lp.y = 150;
        window.setAttributes(lp);
        window.setWindowAnimations(R.style.MenuDialogAnimation); // 添加动画
        return dialog;
    }

    public static int getScreenWidth(Activity context) {
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }


    /**
     * 弹出自定义的对话框（任务完成弹出框）
     *
     * @param context
     * @param title
     * @param content
     * @param time dialog显示时间 单位：秒
     */
    static Dialog dialog;

    public static Dialog showMessageDialogTask(Context context,
                                               String title,
                                               String content,
                                               int time) {
        LayoutInflater mLayoutInflater = LayoutInflater.from(context);
        View view = mLayoutInflater.inflate(R.layout.custom_job_finished_dlg, null);


        if (dialog == null) {
            dialog = new Dialog(context, R.style.kangzai_dialog);
        }

        dialog.setContentView(view);
        dialog.setCancelable(true);
        TextView mTitle;
        TextView mContent;
        mTitle = (TextView) view.findViewById(R.id.msg_dlg_title);
        mContent = (TextView) view.findViewById(R.id.msg_dlg_content);
        if (title != null) {
            mTitle.setText(title);
        } else {
            mTitle.setVisibility(View.INVISIBLE);
        }
        if (content != null) {
            mContent.setVisibility(View.VISIBLE);
            mContent.setText(String.format(context.getString(R.string.label_integralmall_reward), content));
        } else {
            mContent.setVisibility(View.INVISIBLE);
        }


        if (time != 0) {
            if (!dialog.isShowing()) {
                final Timer t = new Timer();
                t.schedule(new TimerTask() {
                    public void run() {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }

                        t.cancel();

                    }
                }, time);
            }

        }
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        if (!dialog.isShowing()) {
            dialog.show();
        }

        return dialog;
    }

    /**
     * 弹出券的对话框（任务完成弹出框）
     *
     * @param context
     * @param id
     * @param time    dialog显示时间 单位：秒
     */
    public static Dialog showCouponDialog(Context context,
                                          int id,
                                          int time) {
        try {
            LayoutInflater mLayoutInflater = LayoutInflater.from(context);
            View view = mLayoutInflater.inflate(R.layout.alert_coupon_bg, null);
            final Dialog dialog = new Dialog(context, R.style.kangzai_dialog);
            dialog.setContentView(view);
            dialog.setCancelable(true);
            ImageView imageView;
            imageView = (ImageView) view.findViewById(R.id.image_view);
            imageView.setImageResource(id);
            final Timer t = new Timer();
            t.schedule(new TimerTask() {
                public void run() {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                        t.cancel();
                    }
                }
            }, time);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                        t.cancel();
                    }
                    dialog.dismiss();
                }
            });
            dialog.show();
            return dialog;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 弹出计步器积分券的对话框（任务完成弹出框）
     *
     * @param context
     * @param id
     * @param time    dialog显示时间 单位：秒
     */
    public static Dialog showRevPointsDialog(Context context,
                                             int id,
                                             int time) {
        return showRevPointsDialog(context, id, time, true);
    }

    /**
     * 弹出计步器积分券的对话框（任务完成弹出框）
     *
     * @param context
     * @param id
     * @param time    dialog显示时间 单位：秒
     */
    public static Dialog showRevPointsDialog(Context context,
                                             int id,
                                             int time,
                                             boolean isAutoDismiss) {
        try {
            LayoutInflater mLayoutInflater = LayoutInflater.from(context);
            View view = mLayoutInflater.inflate(R.layout.alert_coupon_bg, null);
            final Dialog dialog = new Dialog(context, R.style.MMTheme_DataSheet);
            dialog.setContentView(view);
            dialog.setCancelable(true);
            ImageView imageView;
            imageView = (ImageView) view.findViewById(R.id.image_view);
            imageView.setImageResource(id);
            dialog.show();
            if (isAutoDismiss) {
                final Timer t = new Timer();
                t.schedule(new TimerTask() {
                    public void run() {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                            t.cancel();
                        }
                    }
                }, time);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                            t.cancel();
                        }
                        dialog.dismiss();
                    }
                });
            } else {
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        dialog.dismiss();
                    }
                });
            }
            return dialog;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
