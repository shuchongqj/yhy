package com.mogujie.tt.ui.widget.message;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.mogujie.tt.ui.widget.BubbleImageView;
import com.mogujie.tt.ui.widget.MGProgressbar;
import com.quanyan.yhy.R;
import com.quanyan.yhy.eventbus.EvBusImage;
import com.yhy.common.beans.im.entity.ImageMessage;
import com.yhy.common.beans.im.entity.MessageEntity;
import com.yhy.common.beans.im.entity.UserEntity;
import com.yhy.common.constants.MessageConstant;
import com.yhy.common.utils.CommonUtil;
import com.yhy.common.utils.FileUtil;
import com.yhy.imageloader.ImageLoadManager;

import de.greenrobot.event.EventBus;

/**
 * @author : yingmu on 15-1-9.
 * @email : yingmu@mogujie.com.
 */
public class ImageRenderView extends BaseMsgRenderView {
    private BtnImageListener btnImageListener;

    /**
     * 可点击的view
     */
    private View messageLayout;
    /**
     * 图片消息体
     */
    private BubbleImageView messageImage;
    /**
     * 图片状态指示
     */
    private MGProgressbar imageProgress;
    //    private ImageAware imageAware;
//    private ImageView imageAware;

    public ImageRenderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static ImageRenderView inflater(Context context, ViewGroup viewGroup, boolean isMine) {
        int resource = isMine ? R.layout.tt_mine_image_message_item : R.layout.tt_other_image_message_item;
        ImageRenderView imageRenderView = (ImageRenderView) LayoutInflater.from(context).inflate(resource, viewGroup, false);
        imageRenderView.setMine(isMine);
        imageRenderView.getMessageImage().initForeBitmap(isMine);
        imageRenderView.setParentView(viewGroup);
        return imageRenderView;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        messageLayout = findViewById(R.id.message_layout);
        messageImage = (BubbleImageView) findViewById(R.id.message_image);
        imageProgress = (MGProgressbar) findViewById(R.id.tt_image_progress);
        imageProgress.setShowText(false);
//        imageAware = new ImageViewAware(messageImage, false);
//        imageAware = new ImageView(messageImage.getContext());
    }

    /**
     *
     * */

    /**
     * 控件赋值
     *
     * @param messageEntity
     * @param userEntity    对于mine。 图片send_success 就是成功了直接取地址
     *                      对于sending  就是正在上传
     *                      <p/>
     *                      对于other，消息一定是success，接受成功额
     *                      2. 然后分析loadStatus 判断消息的展示状态
     */
    @Override
    public void render(final MessageEntity messageEntity, final UserEntity userEntity, Context ctx) {
        super.render(messageEntity, userEntity, ctx);
        loadImage((ImageMessage) messageEntity);
    }


    /**
     * 多端同步也不会拉到本地失败的数据
     * 只有isMine才有的状态，消息发送失败
     * <p/>
     * <p/>
     * 1. 图片上传失败。点击图片重新上传??[也是重新发送]
     * 2. 图片上传成功，但是发送失败。 点击重新发送??
     * 3. 比较悲剧的是 图片上传失败和消息发送失败都是这个状态 不过可以通过另外一个状态来区别 图片load状态
     *
     * @param entity
     */
    @Override
    public void msgFailure(final MessageEntity entity) {
        super.msgFailure(entity);
        messageImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                /**判断状态，重新发送resend*/
                btnImageListener.onMsgFailure();
            }
        });
        imageProgress.hideProgress();
    }

    private void loadImage(final ImageMessage message) {
        String imageUrl = null;
        String url = message.getUrl();
        String thumbnailUrl = message.getThumbnailUrl();
        if (FileUtil.isFileExist(message.getPath())) {
            imageUrl = "file://" + message.getPath();
        } else if (!TextUtils.isEmpty(url) || !TextUtils.isEmpty(thumbnailUrl)) {
            imageUrl = TextUtils.isEmpty(thumbnailUrl) ? url : thumbnailUrl;
        }
        final int status = message.getStatus();
        if (!TextUtils.isEmpty(imageUrl) && !(status == MessageConstant.MSG_SUCCESS && message.getLoadStatus() == MessageConstant.IMAGE_LOADED_FAILURE)) {
            final String finalImageUrl = imageUrl;
            if (status == MessageConstant.MSG_SUCCESS) {
                message.setLoadStatus(MessageConstant.IMAGE_LOADING);
            }
//            ImageLoadManager.loadImage(imageUrl, R.mipmap.im_img_default, imageAware, new RequestListener() {
            ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(imageUrl), R.mipmap.im_img_default, messageImage, new RequestListener() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object o, Target target, boolean b) {
                    if (status == MessageConstant.MSG_SUCCESS) {
                        message.setLoadStatus(MessageConstant.IMAGE_LOADED_FAILURE);
                    }
                    return false;
                }

                @Override
                public boolean onResourceReady(Object o, Object o2, Target target, DataSource dataSource, boolean b) {

                    // TODO: 2018/4/3  Tom delete
                    if (message.getLoadStatus() != MessageConstant.IMAGE_LOADED_SUCCESS) {
                        EvBusImage image = new EvBusImage();
                        image.path = finalImageUrl;
                        EventBus.getDefault().post(image);
                    }

                    message.setLoadStatus(MessageConstant.IMAGE_LOADED_SUCCESS);
                    return false;
                }
            });
//            ImageLoaderUtil.getImageLoaderInstance().displayImage(imageUrl, imageAware, new DisplayImageOptions.Builder()
//                    .cacheInMemory(true)
//                    .cacheOnDisk(true)
//                    .showImageOnLoading(R.mipmap.im_img_default)
//                    .showImageOnFail(R.mipmap.tt_img_error_bg)
//                    .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
//                    .bitmapConfig(Bitmap.Config.RGB_565)
//                    .delayBeforeLoading(100)
//                    .build(), new SimpleImageLoadingListener() {
//                @Override
//                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                    super.onLoadingComplete(imageUri, view, loadedImage);
//                    if (status == MessageConstant.MSG_SUCCESS) {
//                        // TODO: 2018/4/3  Tom delete
////                        if (message.getLoadStatus() != MessageConstant.IMAGE_LOADED_SUCCESS) {
////                            EvBusImage image = new EvBusImage();
////                            image.path = imageUri;
////                            EventBus.getDefault().post(image);
////                        }
//
//                        message.setLoadStatus(MessageConstant.IMAGE_LOADED_SUCCESS);
//                    }
//                }
//
//                @Override
//                public void onLoadingStarted(String imageUri, View view) {
//                    super.onLoadingStarted(imageUri, view);
//                    if (status == MessageConstant.MSG_SUCCESS) {
//                        message.setLoadStatus(MessageConstant.IMAGE_LOADING);
//                    }
//                }
//
//                @Override
//                public void onLoadingCancelled(String imageUri, View view) {
//                    super.onLoadingCancelled(imageUri, view);
//                    if (status == MessageConstant.MSG_SUCCESS) {
//                        message.setLoadStatus(MessageConstant.IMAGE_UNLOAD);
//                    }
//                }
//
//                @Override
//                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//                    super.onLoadingFailed(imageUri, view, failReason);
//                    if (status == MessageConstant.MSG_SUCCESS) {
//                        message.setLoadStatus(MessageConstant.IMAGE_LOADED_FAILURE);
//                    }
//                }
//            });
        } else {
            if (status == MessageConstant.MSG_SUCCESS) {
                message.setLoadStatus(MessageConstant.IMAGE_LOADED_FAILURE);
            }
            messageImage.setImageResource(R.mipmap.tt_img_error_bg);
        }
    }


    @Override
    public void msgStatusError(final MessageEntity entity) {
        super.msgStatusError(entity);
        messageImage.setOnClickListener(null);
        imageProgress.hideProgress();
    }


    /**
     * 图片信息正在发送的过程中
     * 1. 上传图片
     * 2. 发送信息
     */
    @Override
    public void msgSendinging(final MessageEntity entity) {
        messageFailed.setVisibility(View.GONE);
        loadingProgressBar.setVisibility(View.GONE);
        imageProgress.showProgress();
        messageImage.setOnClickListener(null);
    }


    /**
     * 消息成功
     * 1. 对方图片消息
     * 2. 自己多端同步的消息
     * 说明imageUrl不会为空的
     */
    @Override
    public void msgSuccess(final MessageEntity entity) {

        super.msgSuccess(entity);
        ImageMessage imageMessage = (ImageMessage) entity;
        String thumbnailUrl = ((ImageMessage) entity).getThumbnailUrl();
        final String url = TextUtils.isEmpty(thumbnailUrl) ? ((ImageMessage) entity).getUrl() : thumbnailUrl;

        if (TextUtils.isEmpty(url)) {
            /**消息状态异常*/
            msgStatusError(entity);
            return;
        }
        getImageProgress().hideProgress();
        messageImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int loadStatus = ((ImageMessage) entity).getLoadStatus();
                if (loadStatus == MessageConstant.IMAGE_LOADED_FAILURE) {
                    ((ImageMessage) entity).setLoadStatus(MessageConstant.IMAGE_UNLOAD);
                    loadImage((ImageMessage) entity);
                } else {
                    if (btnImageListener != null) {
                        btnImageListener.onMsgSuccess();
                    }
                }
            }
        });
    }

    /**
     * ---------------------图片下载相关、点击、以及事件回调start-----------------------------------
     */
    public interface BtnImageListener {
        void onMsgSuccess();

        void onMsgFailure();

    }

    public void setBtnImageListener(BtnImageListener btnImageListener) {
        this.btnImageListener = btnImageListener;
    }


    public interface ImageLoadListener {
        void onLoadComplete(String path);

        // 应该把exception 返回结构放进去
        void onLoadFailed();

    }

    /**---------------------图片下载相关、以及事件回调 end-----------------------------------*/


    /**
     * ----------------------set/get------------------------------------
     */
    public View getMessageLayout() {
        return messageLayout;
    }

    public BubbleImageView getMessageImage() {
        return messageImage;
    }

    public MGProgressbar getImageProgress() {
        return imageProgress;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setMine(boolean isMine) {
        this.isMine = isMine;
    }

    public ViewGroup getParentView() {
        return parentView;
    }

    public void setParentView(ViewGroup parentView) {
        this.parentView = parentView;
    }

//    public enum IMAGE_STATUS {
//        FINISH, LOADING, FAIL;
//    }
}
