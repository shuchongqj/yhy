package com.mogujie.tt.imservice.service;

import android.app.IntentService;
import android.content.Intent;
import android.text.TextUtils;

import com.harwkin.nb.camera.ImageUtils;
import com.harwkin.nb.camera.PhotoUtil;
import com.mogujie.tt.imservice.event.MessageEvent;
import com.mogujie.tt.utils.Logger;
import com.quanyan.yhy.net.NetManager;
import com.quanyan.yhy.net.lsn.OnResponseListener;
import com.yhy.common.beans.im.entity.ImageMessage;
import com.yhy.common.constants.SysConstant;
import com.yhy.common.types.AppDebug;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * @author : yingmu on 15-1-12.
 * @email : yingmu@mogujie.com.
 */
public class LoadImageService extends IntentService {

    private static Logger logger = Logger.getLogger(LoadImageService.class);

    public LoadImageService() {
        super("LoadImageService");
    }

    public LoadImageService(String name) {
        super(name);
    }

    /**
     * This method is invoked on the worker thread with a request to process.
     * Only one Intent is processed at a time, but the processing happens on a
     * worker thread that runs independently from other application logic.
     * So, if this code takes a long time, it will hold up other requests to
     * the same IntentService, but it will not hold up anything else.
     * When all requests have been handled, the IntentService stops itself,
     * so you should not call {@link #stopSelf}.
     *
     * @param intent The value passed to {@link
     *               android.content.Context#startService(android.content.Intent)}.
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        final ImageMessage messageInfo = (ImageMessage) intent.getSerializableExtra(SysConstant.UPLOAD_IMAGE_INTENT_PARAMS);
        String str = messageInfo.getPath();
        List<String> files = new ArrayList<>();
        if (!AppDebug.ORG_BMP_UPLOAD) {
            try {
                int i = str.lastIndexOf(".");
                if (i > 0) {
                    String format = str.substring(i, str.length());
                    if (!TextUtils.isEmpty(format) && format.equals(".gif")) {
                        files.add(PhotoUtil.depositInDiskGif(this, str, format));
                    } else {
                        files.add(PhotoUtil.depositInDiskBitmap(this, PhotoUtil.getPhoto(str)));
                    }
                } else {
                    files.add(PhotoUtil.depositInDiskBitmap(this, PhotoUtil.getPhoto(str)));
                }
            } catch (Exception e) {
                EventBus.getDefault().post(new MessageEvent(MessageEvent.Event.IMAGE_UPLOAD_FAILD
                        , messageInfo));
                return;
            }
        } else {
            files.add(str);
        }

        NetManager.getInstance(this).doUploadImages(files, new OnResponseListener<List<String>>() {
            @Override
            public void onComplete(boolean isOK, List<String> uploadFileName, int errorCode, String errorMsg) {
                if (isOK && uploadFileName != null
                        && uploadFileName.size() > 0) {
                    messageInfo.setUrl(ImageUtils.getImageFullUrl(uploadFileName.get(0)));
                    EventBus.getDefault().post(new MessageEvent(
                            MessageEvent.Event.IMAGE_UPLOAD_SUCCESS
                            , messageInfo));
                } else {
                    EventBus.getDefault().post(new MessageEvent(MessageEvent.Event.IMAGE_UPLOAD_FAILD
                            , messageInfo));
                }
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                EventBus.getDefault().post(new MessageEvent(
                        MessageEvent.Event.IMAGE_UPLOAD_FAILD
                        , messageInfo));
            }
        });
    }
}
