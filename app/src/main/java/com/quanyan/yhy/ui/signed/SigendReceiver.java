package com.quanyan.yhy.ui.signed;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.BaseApplication;
import com.quanyan.yhy.R;
import com.yhy.common.beans.net.model.CreditNotification;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.SPUtils;


public class SigendReceiver extends BroadcastReceiver {
	Context context;

	@Override
	public void onReceive(Context context, Intent intent) {
		this.context = context;
		if(ValueConstants.BROADCASTRECEIVER_ALL_TAST_COMPLETE.equals(intent.getAction())){
			CreditNotification creditNotification= (CreditNotification)intent.getExtras().getSerializable(SPUtils.EXTRA_DATA);
			if(creditNotification!=null){
//				Intent i=new Intent(context,SigendDialogActivity.class);
//				i.putExtra(SPUtils.EXTRA_DATA,c);
//				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				context.startActivity(i);

//				ScorePopupWindow scorePopupWindow = ScorePopupWindow.makeText(BaseApplication.getActivityList().get(0), c);
//				scorePopupWindow.show();

				// 收到积分后弹出积分增加的弹框
				Activity activity = BaseApplication.getTopActivity();
				// activity已销毁
				if (activity == null || activity.isFinishing()) {
					return;
				}
				final Dialog sShareDlg = new Dialog(activity);
				sShareDlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
				sShareDlg.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
				View mContentView = LayoutInflater.from(activity).inflate(R.layout.score_popup_window, null);
//				centerView = mContentView.findViewById(R.id.rlyt_center_view);
				TextView mTitle = mContentView.findViewById(R.id.msg_dlg_title);
				TextView mContent = mContentView.findViewById(R.id.msg_dlg_content);

				if( creditNotification!=null){
					mTitle.setText("+" + creditNotification.credit);
					if(!StringUtil.isEmpty(creditNotification.description)){
						mContent.setVisibility(View.VISIBLE);
						mContent.setText( String.format(activity.getString(R.string.label_integralmall_reward), creditNotification.description));
					}else {
						mContent.setVisibility(View.INVISIBLE);
					}
				}else{
					mTitle.setVisibility(View.INVISIBLE);
					mContent.setVisibility(View.INVISIBLE);
				}

				mContentView.findViewById(R.id.ll_custom_dialog).setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						sShareDlg.dismiss();
					}
				});

				sShareDlg.setContentView(mContentView);

				Window dialogWindow = sShareDlg.getWindow();
				dialogWindow.setGravity(Gravity.CENTER);
				WindowManager.LayoutParams lp = dialogWindow.getAttributes();
				lp.width = WindowManager.LayoutParams.MATCH_PARENT;
				lp.height = WindowManager.LayoutParams.MATCH_PARENT;
				dialogWindow.setAttributes(lp);

				sShareDlg.show();

				mContentView.postDelayed(new Runnable() {
					@Override
					public void run() {
						try {
							sShareDlg.dismiss();
						}catch (Exception e){
							e.printStackTrace();
						}

					}
				}, 3000);

			}else{
			}

		}

	}



}