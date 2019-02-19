package com.quanyan.yhy.ui.wallet.view;

/**
 * Created by Administrator on 2016/10/31.
 */

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.harwkin.nb.camera.CameraHandler;
import com.harwkin.nb.camera.CropBuilder;
import com.harwkin.nb.camera.callback.CameraImageListener;
import com.harwkin.nb.camera.pop.CameraPopListener;
import com.quanyan.yhy.R;

/**
 * Custom PopWindow
 *
 * @author tianhaibo
 */
public class IDCardOptionDailog {
    private Context mContext;
    private CameraPopListener mPopListern;
    private CameraHandler mCameraHandler;
    private int clickId;


    public IDCardOptionDailog(Context context, CameraPopListener listener,
                              CameraImageListener imageListener) {

        initContext(context, listener);
        this.mContext = context;

        mCameraHandler = new CameraHandler(context, imageListener);
        mCameraHandler.getOptions().setNeedCameraCompress(true);

        mCameraHandler.getOptions().setImageHeightCompress(2200);
        mCameraHandler.getOptions().setImageWidthCompress(2200);
    }


    public void showOptionDialog() {
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.dialog_upload_idcard_layout, null);
        RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.rl);
        relativeLayout.getBackground().setAlpha(5);
        final Dialog dialog = new Dialog(mContext, R.style.MenuDialogStyle);
        dialog.setContentView(view);
        dialog.show();

        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        int screenW = getScreenWidth(mContext);
        lp.width = screenW;

        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.MenuDialogAnimation); // 添加动画

        view.findViewById(R.id.rl_takephoto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopListern.onCamreaClick(mCameraHandler.getOptions());
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.rl_ablum).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopListern.onPickClick(mCameraHandler.getOptions());
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.tv_cancel_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    private void initContext(Context context, CameraPopListener listener) {
        this.mContext = context;
        this.mPopListern = listener;
    }

    public void setCropBuilder(CropBuilder builder) {
        mCameraHandler.getOptions().setCropBuilder(builder);
    }

    public void process() {
        try {
            mCameraHandler.process();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void forResult(int requestCode, int resultCode, Intent data) {
        mCameraHandler.forResult(requestCode, resultCode, data);
    }
}
