package com.mogujie.tt.ui.widget.message;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.quanyan.yhy.R;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.yhy.common.beans.im.entity.ImageMessage;
import com.yhy.common.beans.im.entity.MessageEntity;
import com.yhy.common.beans.im.entity.UserEntity;
import com.yhy.common.utils.CommonUtil;
import com.yhy.common.utils.FileUtil;
import com.yhy.imageloader.ImageLoadManager;

import java.util.ArrayList;

/**
 * Created by zhujian on 15/3/26.
 */
public class GifImageRenderView extends BaseMsgRenderView {
    private ImageView messageContent;
//    ImageViewAware imageAware;
//    ImageView imageAware;

    public ImageView getMessageContent() {
        return messageContent;
    }

    public static GifImageRenderView inflater(Context context, ViewGroup viewGroup, boolean isMine) {
        int resource = isMine ? R.layout.tt_mine_gifimage_message_item : R.layout.tt_other_gifimage_message_item;
        GifImageRenderView gifRenderView = (GifImageRenderView) LayoutInflater.from(context).inflate(resource, viewGroup, false);
        gifRenderView.setMine(isMine);
        return gifRenderView;
    }

    public GifImageRenderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        messageContent = (ImageView) findViewById(R.id.message_image);
//        imageAware = new ImageView(messageContent.getContext());
    }

    ImageMessage message;

    /**
     * 控件赋值
     *
     * @param messageEntity
     * @param userEntity
     */
    @Override
    public void render(MessageEntity messageEntity, UserEntity userEntity, Context context) {
        super.render(messageEntity, userEntity, context);
        message = (ImageMessage) messageEntity;
        loadImage(message);
    }

    private void loadImage(final ImageMessage message) {
        messageContent.setOnClickListener(GotoBigImageListener);
        String imageUrl = null;
        String url = message.getUrl();
        if (FileUtil.isFileExist(message.getPath())) {
            imageUrl = "file://" + message.getPath();
        } else if (!TextUtils.isEmpty(url)) {
            imageUrl = url;
        }

//        ImageLoaderUtil.getImageLoaderInstance().displayImage(imageUrl, imageAware, new DisplayImageOptions.Builder()
//                .cacheInMemory(true)
//                .cacheOnDisk(true)
//                .showImageOnLoading(R.mipmap.tt_img_default_bg)
//                .showImageOnFail(R.mipmap.tt_img_error_bg)
//                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
//                .bitmapConfig(Bitmap.Config.RGB_565)
//                .delayBeforeLoading(100)
//                .build());

        ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(imageUrl), R.mipmap.tt_img_default_bg, 0, 0, messageContent
        , null, false, 0, true, false);
//        GifDecoder

//        Glide.with(messageContent.getContext()).load(imageUrl).asBitmap().placeholder(R.mipmap.tt_img_default_bg).listener(new RequestListener<String, Bitmap>() {
//            @Override
//            public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
//                messageContent.setOnClickListener(loadImageListener);
//                return false;
//            }
//
//            @Override
//            public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                return false;
//            }
//        }).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(messageContent);
    }

    private OnClickListener GotoBigImageListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            String imagePath = message.getPath();
            String url = message.getUrl();
            String pic;
            if (FileUtil.isFileExist(imagePath)) {
                pic = imagePath;
            } else if (!TextUtils.isEmpty(url)) {
                pic = url;
            } else {
                Toast.makeText(getContext(), getContext().getString(R.string.image_path_unavaluable), Toast.LENGTH_LONG).show();
                return;
            }
            ArrayList<String> pics = new ArrayList<String>();
            pics.add(pic);
            NavUtils.gotoLookBigImage(getContext(), pics, 0, false);
        }
    };

    private OnClickListener loadImageListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            loadImage(message);
        }
    };


    @Override
    public void msgFailure(MessageEntity messageEntity) {
        super.msgFailure(messageEntity);
    }

    /**
     * ----------------set/get---------------------------------
     */

    public void setMine(boolean isMine) {
        this.isMine = isMine;
    }


    public void setParentView(ViewGroup parentView) {
        this.parentView = parentView;
    }


}
