
package com.mogujie.tt.ui.widget.message;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.media.AudioManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.quanyan.yhy.R;
import com.quanyan.yhy.ui.base.utils.DialogUtil;
import com.yhy.common.constants.DBConstant;
import com.yhy.common.utils.SPUtils;

/**
 * A popup window that can be used to display an arbitrary view
 * OnItemClickListener
 */
public class MessageOperatePopup implements View.OnClickListener {

    private Dialog mDialog;
    private static MessageOperatePopup messageOperatePopup;
    private OnItemClickListener mListener;
    private Context ctx;

    private TextView copyBtn, speakerBtn;

    public void hidePopup() {
        if (mDialog != null) {
            dismiss();
        }
    }

    public MessageOperatePopup(Context ctx) {
        this.ctx = ctx;
        View view = LayoutInflater.from(ctx).inflate(R.layout.tt_popup_list,
                null);

        // popView = (LinearLayout) view.findViewById(R.id.popup_list);

        copyBtn = (TextView) view.findViewById(R.id.copy_btn);
        copyBtn.setOnClickListener(this);

//        resendBtn = (TextView) view.findViewById(R.id.resend_btn);
//        resendBtn.setOnClickListener(this);

        speakerBtn = (TextView) view.findViewById(R.id.speaker_btn);
        speakerBtn.setOnClickListener(this);
//        deleteBtn = (TextView) view.findViewById(R.id.delete_btn);
//        deleteBtn.setOnClickListener(this);

        mDialog = new Dialog(ctx, R.style.MenuDialogStyle);
        mDialog.setContentView(view);
        Window window = mDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        int screenW = DialogUtil.getScreenWidth((Activity) ctx);
        window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置
        lp.width = screenW / 2;
        window.setAttributes(lp);
        mDialog.setCanceledOnTouchOutside(true);
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        mListener = l;
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    public void show(View item, int type, boolean bResend, boolean bSelf) {
        if (mDialog == null || mDialog.isShowing()) {
            return;
        }

        //下面全用回调 todo

        // 语音类型
//        if (type == DBConstant.SHOW_AUDIO_TYPE) {
//            speakerBtn.setVisibility(View.VISIBLE);
//            if (AudioPlayerHandler.getInstance().getAudioMode(context) == AudioManager.MODE_NORMAL) {
//                speakerBtn.setText(R.string.call_mode);
//            } else {
//                speakerBtn.setText(R.string.speaker_mode);
//            }
//            bspeakerShow = true;
//        } else {
//            speakerBtn.setVisibility(View.GONE);
//            bspeakerShow = false;
//        }

        // 自己消息重发
        // 自己的消息
        // 非自己的消息
        // 图片语音
        // 文本
        if (type == DBConstant.SHOW_ORIGIN_TEXT_TYPE) {
            copyBtn.setVisibility(View.VISIBLE);
        } else {
            copyBtn.setVisibility(View.GONE);
        }
        if (type == DBConstant.SHOW_AUDIO_TYPE) {
            int streamType = SPUtils.getAudioStreamType(ctx);
            if (streamType == AudioManager.STREAM_MUSIC) {
                speakerBtn.setText(R.string.call_mode);
            } else {
                speakerBtn.setText(R.string.speaker_mode);
            }
            speakerBtn.setVisibility(View.VISIBLE);
        } else {
            speakerBtn.setVisibility(View.GONE);
        }
//        if (bResend && bSelf) {
//            resendBtn.setVisibility(View.VISIBLE);
//        } else {
//            resendBtn.setVisibility(View.GONE);
//        }
        mDialog.show();
    }

    public void dismiss() {
        if (mDialog == null || !mDialog.isShowing()) {
            return;
        }

        mDialog.dismiss();
    }

    public interface OnItemClickListener {
        void onCopyClick();

        void onResendClick();

        void onDeleteClick();

        void onSpeakerClick();
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();

        if (R.id.copy_btn == id) {
            dismiss();
            if (mListener != null) {
                mListener.onCopyClick();
            }
//        } else if (R.id.resend_btn == id) {
//            dismiss();
//            if (mListener != null) {
//                mListener.onResendClick();
//            }
  /*      } else if (R.id.delete_btn == id) {
            dismiss();
            if (mListener != null) {
                mListener.onDeleteClick();
            }*/
        } else if (R.id.speaker_btn == id) {
            dismiss();
            if (mListener != null) {
                mListener.onSpeakerClick();
            }
        }
    }

}
