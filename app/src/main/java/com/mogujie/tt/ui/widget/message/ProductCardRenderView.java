package com.mogujie.tt.ui.widget.message;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.R;
import com.yhy.common.beans.im.entity.MessageEntity;
import com.yhy.common.beans.im.entity.ProductCardMessage;
import com.yhy.common.beans.im.entity.UserEntity;
import com.yhy.common.utils.CommonUtil;
import com.yhy.imageloader.ImageLoadManager;

/**
 * @author : yingmu on 15-1-9.
 * @email : yingmu@mogujie.com.
 */
public class ProductCardRenderView extends BaseMsgRenderView {

    /**
     * 可点击的消息体
     */
    private View messageLayout;
    private ImageView productCardImage;
    private TextView productCardTitle;
    private TextView productCardPrice;

    public ProductCardRenderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static ProductCardRenderView inflater(Context ctx, ViewGroup viewGroup, boolean isMine) {

        int resoure = isMine ? R.layout.tt_mine_product_card_message_item : R.layout.tt_other_product_card_message_item;
        //tt_other_audio_message_item
        ProductCardRenderView audioRenderView = (ProductCardRenderView) LayoutInflater.from(ctx).inflate(resoure, viewGroup, false);
        audioRenderView.setMine(isMine);
        audioRenderView.setParentView(viewGroup);
        return audioRenderView;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        messageLayout = findViewById(R.id.message_layout);
        productCardImage = (ImageView) findViewById(R.id.product_image);
        productCardTitle = (TextView) findViewById(R.id.title);
        productCardPrice = (TextView) findViewById(R.id.price);
    }


    /**
     * 控件赋值
     * 是不是采用callback的形式
     *
     * @param messageEntity
     * @param userEntity
     */
    @Override
    public void render(final MessageEntity messageEntity, final UserEntity userEntity, final Context ctx) {
        super.render(messageEntity, userEntity, ctx);
        final ProductCardMessage message = (ProductCardMessage) messageEntity;

        messageLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        if (!StringUtil.isEmpty(message.getImgUrl())) {
//            BaseImgView.loadimg(productCardImage,
//                    message.getImgUrl(),
//                    R.mipmap.icon_default_215_215,
//                    R.mipmap.icon_default_215_215,
//                    R.mipmap.icon_default_215_215,
//                    ImageScaleType.EXACTLY,
//                    (int) getResources().getDimension(R.dimen.dd_dimen_300px),
//                    (int) getResources().getDimension(R.dimen.dd_dimen_300px),
//                    0);
            ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(message.getImgUrl()), R.mipmap.icon_default_215_215,
                    (int) getResources().getDimension(R.dimen.dd_dimen_300px),
                    (int) getResources().getDimension(R.dimen.dd_dimen_300px), productCardImage);
        } else {
            productCardImage.setImageResource(R.mipmap.icon_default_215_215);
        }

        productCardTitle.setText(message.getTitle());
        productCardPrice.setText(StringUtil.getFlagRmb(getContext()) + StringUtil.convertPriceNoSymbol(message.getPrice()));
    }

    public View getMessageLayout() {
        return messageLayout;
    }

    public void setMine(boolean isMine) {
        this.isMine = isMine;
    }

    public void setParentView(ViewGroup parentView) {
        this.parentView = parentView;
    }
}
