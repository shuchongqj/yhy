package com.mogujie.tt.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.R;
import com.yhy.common.beans.net.model.ProductCardModel;
import com.yhy.common.utils.CommonUtil;
import com.yhy.imageloader.ImageLoadManager;

/**
 * Created with Android Studio.
 * Title:IMGoodsLinkView
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:邓明佳
 * Date:2016/3/11
 * Time:15:20
 * Version 1.0
 */
public class IMGoodsLinkView extends RelativeLayout {
    private TextView mTitle;
    private TextView mPrice;
    private ImageView mImageIcon;
    private TextView mSendBtn;
    private Context mContext;

    public IMGoodsLinkView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTitle = (TextView) this.findViewById(R.id.tv_title);
        mPrice = (TextView) this.findViewById(R.id.tv_price);
        mImageIcon = (ImageView) this.findViewById(R.id.iv_icon);
        mSendBtn = (TextView) this.findViewById(R.id.tv_send);
    }

    public void setData(ProductCardModel productCardModel) {
        mTitle.setText(productCardModel.getTitle());
        String price;
        if (productCardModel.getPrice() == productCardModel.getMax_price()) {
            price = StringUtil.getFlagRmb(getContext()) + StringUtil.convertPriceNoSymbol(productCardModel.getPrice());
        } else {
            String min = StringUtil.getFlagRmb(getContext()) + StringUtil.convertPriceNoSymbol(productCardModel.getPrice());
            String max = StringUtil.getFlagRmb(getContext()) + StringUtil.convertPriceNoSymbol(productCardModel.getMax_price());
            price = min + "-" + max;
        }
        mPrice.setText(price);
        if (!StringUtil.isEmpty(productCardModel.getImgUrl())) {
//            BaseImgView.loadimg(mImageIcon,
//                    productCardModel.getImgUrl(),
//                    R.mipmap.icon_default_215_215,
//                    R.mipmap.icon_default_215_215,
//                    R.mipmap.icon_default_215_215,
//                    ImageScaleType.EXACTLY,
//                    300,
//                    300,
//                    0);
            ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(productCardModel.getImgUrl()), R.mipmap.icon_default_215_215,300, 300, mImageIcon);
        } else {
            mImageIcon.setImageResource(R.mipmap.icon_default_215_215);
        }
        mSendBtn.setText(R.string.send_goods_link);
    }

    public void setOnSendListener(OnClickListener clickListener) {
        mSendBtn.setOnClickListener(clickListener);
    }
}
