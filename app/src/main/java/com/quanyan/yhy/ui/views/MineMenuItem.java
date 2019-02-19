package com.quanyan.yhy.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quanyan.yhy.R;

public class MineMenuItem extends RelativeLayout {
	private ImageView iconView;
	private ImageView newIconView;
	private TextView titleView;
	private TextView itemContentValue;
	private TextView summaryView;
	private TextView unreadView;
	private TextView itemValue;
	private ImageView mIvArrow;
	private ImageView mShopNewMsg;

	public MineMenuItem(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		init(context);
	}

	public MineMenuItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		init(context);
	}

	public MineMenuItem(Context context) {
		super(context);
		
		init(context);
	}

	private void init(Context context){
		LayoutInflater mLayoutInflater = LayoutInflater.from(context);
		View view = mLayoutInflater.inflate(R.layout.mine_menu_item, null);
		
		LayoutParams rlp = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		rlp.bottomMargin = 0;
		rlp.topMargin = 0;
		rlp.leftMargin = 0;
		rlp.rightMargin = 0;
		
		iconView = (ImageView)view.findViewById(R.id.icon);
		titleView = (TextView)view.findViewById(R.id.title);
		summaryView = (TextView)view.findViewById(R.id.summary);
		unreadView = (TextView)view.findViewById(R.id.unread_count);
		newIconView = (ImageView)view.findViewById(R.id.iv_new_icon);
		itemValue = (TextView)view.findViewById(R.id.tv_value);
		mIvArrow = (ImageView) view.findViewById(R.id.menu_array);
		mShopNewMsg = (ImageView) view.findViewById(R.id.iv_new_msg);
		itemContentValue = (TextView)view.findViewById(R.id.tv_cotent_value);
		addView(view, rlp);
		
		setBackgroundResource(R.drawable.find_item_bg_selector);
	}

	public void setArrowVisibility(int visibility){
		mIvArrow.setVisibility(visibility);
	}

	//积分商城msg红点显示
	public void setShopMsgVisibility(int visibility){
		mShopNewMsg.setVisibility(visibility);
	}
	
	public void initItem(int iconResId,int titleResId,int summaryResId){
		if(iconResId != -1){
			iconView.setImageResource(iconResId);
		}
		
		if(titleResId != -1){
			titleView.setText(titleResId);
		}
		
		if(summaryResId != -1){
			summaryView.setText(summaryResId);
		}
	}
	
	public void setUnread(int count){
		if(count > 0){
			unreadView.setVisibility(View.VISIBLE);
			unreadView.setText(String.valueOf(count));
		}else{
			unreadView.setVisibility(View.GONE);
		}
	}

	/**
	 * 设置Value的值
	 * @param value
	 */
	public void setValue(String value){
		if(value == null){
			itemValue.setVisibility(View.GONE);
			return ;
		}
		itemValue.setVisibility(View.VISIBLE);
		itemValue.setText(value);
	}

	//右边字体显示
	public void setContentValue(String value){
		if(value == null){
			itemContentValue.setVisibility(View.GONE);
			return ;
		}
		itemContentValue.setVisibility(View.VISIBLE);
		itemContentValue.setText(value);
	}
	
	public void setIcon(int iconResId){
		if(iconResId != -1){
			iconView.setImageResource(iconResId);
		}
	}
	
	public void setTitle(int titleResId){
		if(titleResId != -1){
			titleView.setText(titleResId);
		}
	}

	public void setSummary(int summaryResId){
		if(summaryResId != -1){
			summaryView.setText(summaryResId);
		}
	}
	
	public void setSummary(String summaryText){
		if(summaryText != null){
			summaryView.setText(summaryText);
		}
	}
	
	public void setNewIcon(int newIconResId){
		if(newIconResId != -1){
			newIconView.setVisibility(View.VISIBLE);
			newIconView.setImageResource(newIconResId);
		}else{
			newIconView.setVisibility(View.GONE);
		}
	}
}
