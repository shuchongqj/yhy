package com.newyhy.wsy;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.chinanetcenter.wcs.android.ClientConfig;
import com.chinanetcenter.wcs.android.api.FileUploader;
import com.chinanetcenter.wcs.android.api.ParamsConf;
import com.chinanetcenter.wcs.android.listener.SliceUploaderBase64Listener;
import com.quanyan.yhy.R;

import com.quanyan.yhy.eventbus.EvBusUpCourseVideo;
import com.quanyan.yhy.net.NetManager;
import com.quanyan.yhy.net.lsn.OnResponseListener;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.smart.sdk.api.resp.Api_TRAIN_AsyncCallbackParam;
import com.smart.sdk.api.resp.Api_TRAIN_ResponseDTO;
import com.yhy.common.base.YHYBaseApplication;
import com.yhy.common.config.ContextHelper;
import com.yhy.common.utils.SPUtils;
import com.yhy.router.YhyRouter;
import com.yhy.service.IUserService;

import java.io.File;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import de.greenrobot.event.EventBus;

/**
 * Created by yangboxue on 2018/6/13.
 */

public class UpLoadCourseVideoService extends Service {

    public static final String MEDIA_PATH = "mediaPath";
    public static final String VIDEO_ID = "videoId";
    public static final String TYPE = "type";
    private String mediaPath;
    private String videoId;
    private ParamsConf conf;
    private int type;       // 0是传视频1是传图片

    @Autowired
    IUserService userService;

    @Override
    public void onCreate() {
        super.onCreate();
        YhyRouter.getInstance().inject(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //执行文件的上传操作
        mediaPath = intent.getStringExtra(MEDIA_PATH);
        videoId = intent.getStringExtra(VIDEO_ID);
        type = intent.getIntExtra(TYPE, -1);
        if (mediaPath != null) {
            if (type == 0) {
                doUpLoad(WsyUtils.generateUpLoadToken(), new File(mediaPath).getName(), mediaPath);
            } else {
                thumbnailUpload(mediaPath);
            }
        } else {
            stopSelf();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    /**
     * 上传视频
     *
     * @param token
     * @param filename
     * @param filePath
     */
    public void doUpLoad(String token, String filename, String filePath) {

        FileUploader.setUploadUrl(WsyUtils.wsy_upload_url);
        ClientConfig config = new ClientConfig();
        config.setMaxConcurrentRequest(10);
        FileUploader.setClientConfig(config);

        conf = new ParamsConf();
        // 原始文件名称
        conf.fileName = filename;
        // 通过表单参数设置文件保存到云存储的名称
        String mediaType = filename.substring(filename.lastIndexOf(".") + 1);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Long time = System.currentTimeMillis();
        String date = format.format(time);
        conf.keyName = "train/" + userService.getLoginUserId() + "-" + date + "." + mediaType;
        // 通过表单参数设置文件的mimeType
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String type = fileNameMap.getContentTypeFor(filename);
        conf.mimeType = type;
        FileUploader.setParams(conf);
        FileUploader.setBlockConfigs(8, 4 * 1024);
        ToastUtil.showToast(YHYBaseApplication.getInstance(), getString(R.string.tips_upload_video));
        FileUploader.sliceUpload(filePath, YHYBaseApplication.getInstance(), token, new File(filePath), null, new SliceUploaderBase64Listener() {

            @Override
            public void onSliceUploadSucceed(String string) {
                Log.e("WYS-------------------", "slice upload succeeded.");
                String url = ContextHelper.getVodUrl() + conf.keyName;
                asyncCallback(true, url);
            }

            @Override
            public void onProgress(long uploaded, long total) {
                Log.e("WYS-------------------", String.format(Locale.CHINA, "uploaded : %s, total : %s", uploaded, total));
            }

            @Override
            public void onSliceUploadFailured(HashSet<String> errorMessages) {
                Log.e("WYS-------------------", "slice upload failure.");
                asyncCallback(false, "");
                stopSelf();
            }
        });
    }

    /**
     * 上传视频的封面图（该图是上传到阿里云）
     */
    public void thumbnailUpload(String tempThumbnail) {
        List<String> files = new ArrayList<>();
        files.add(tempThumbnail);
        ToastUtil.showToast(YHYBaseApplication.getInstance(), getString(R.string.tips_upload_pic));
        NetManager.getInstance(getApplicationContext()).doUploadImages(files, new OnResponseListener<List<String>>() {
            @Override
            public void onComplete(boolean isOK, List<String> result, int errorCode, String errorMsg) {
                if (isOK) {
                    if (result != null && result.size() > 0) {
                        String thumbnailUrl = result.get(0);
                        asyncCallback(true, thumbnailUrl);
                    }
                } else {
                    asyncCallback(false, "");
                    stopSelf();
                }
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                ToastUtil.showToast(YHYBaseApplication.getInstance(), errorMessage);
                asyncCallback(false, "");
                stopSelf();
            }
        });
    }

    /**
     * 上传成功后回调通知
     */
    private void asyncCallback(boolean success, String url) {
        Api_TRAIN_AsyncCallbackParam param = new Api_TRAIN_AsyncCallbackParam();
        param.callbackId = videoId;
        param.callbackStatus = success ? 0 : 1;
        param.callbackContent = url;
        NetManager.getInstance(getApplicationContext()).trainAsyncCallback(param, new OnResponseListener<Api_TRAIN_ResponseDTO>() {
            @Override
            public void onComplete(boolean isOK, Api_TRAIN_ResponseDTO result, int errorCode, String errorMsg) {
                if (result.success) {
                    ToastUtil.showToast(YHYBaseApplication.getInstance(), "上传成功");
                    EventBus.getDefault().post(new EvBusUpCourseVideo(true, url));

                }
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                ToastUtil.showToast(YHYBaseApplication.getInstance(), errorMessage);
            }
        });

    }

}
