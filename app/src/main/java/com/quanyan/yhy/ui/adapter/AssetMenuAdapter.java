package com.quanyan.yhy.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.quanyan.yhy.R;


public class AssetMenuAdapter extends BaseAdapter {
	private Context mContext;

	public String[] mAssetTitle;
	public String[] mAssetCount;
	public int[] mAssetImage = {R.mipmap.my_wallet_image, R.mipmap.ic_mine_point,R.mipmap.ic_mine_voucher};

	public AssetMenuAdapter(Context mContext) {
		super();
		this.mContext = mContext;
	}


	public AssetMenuAdapter(Context context, String[] assetTitle, String[] assetCount) {
		this(context);
		mAssetTitle = assetTitle;
		mAssetCount = assetCount;
	}

	//刷新积分和优惠券数值
	public void setData(String[] assetCount) {
		if (assetCount != null && assetCount.length > 0) {
			mAssetCount = assetCount;
		}
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mAssetTitle == null ? 0 : mAssetTitle.length;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.asset_grid_item, parent, false);
		}
		TextView tv_title = BaseViewHolder.get(convertView, R.id.tv_item_title);
		TextView tv_count = BaseViewHolder.get(convertView, R.id.tv_item_count);
		ImageView iv_icon = BaseViewHolder.get(convertView, R.id.iv_mine_icon);
		iv_icon.setBackgroundResource(mAssetImage[position]);
		tv_title.setText(mAssetTitle[position]);
		tv_count.setText(mAssetCount[position]);
		return convertView;
	}

}
