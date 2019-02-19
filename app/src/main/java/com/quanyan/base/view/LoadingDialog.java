package com.quanyan.base.view;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.quanyan.yhy.R;

import pl.droidsonroids.gif.GifImageView;

public class LoadingDialog extends Dialog {
	private TextView msgView;
	public LoadingDialog(Context context, boolean cancelable,
						 OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		init(context);
	}

	public LoadingDialog(Context context, int theme) {
		super(context, theme);
		init(context);
	}

	public LoadingDialog(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context){
		LayoutInflater mLayoutInflater = LayoutInflater.from(context);
		View view = mLayoutInflater.inflate(R.layout.loading_dialog, null);
		
		setContentView(view);
		msgView = (TextView)view.findViewById(R.id.tv_msg);
		GifImageView gf1 = (GifImageView) view.findViewById(R.id.iv_waiting);
		// 设置Gif图片源
		gf1.setImageResource(R.drawable.loading);
	}
	
	public void setDlgMessage(String message){
		if(!TextUtils.isEmpty(message)){
			msgView.setVisibility(View.VISIBLE);
			msgView.setText(message);
		}
	}

	/**
	 * 设置对话框文本颜色
	 * @param colorResId
     */
	public void setDlgMsgColor(Context context, int colorResId){
		msgView.setTextColor(context.getResources().getColor(colorResId));
	}
}
