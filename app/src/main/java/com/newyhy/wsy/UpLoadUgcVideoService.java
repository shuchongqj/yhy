package com.newyhy.wsy;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.IBinder;
import android.util.Log;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.chinanetcenter.wcs.android.ClientConfig;
import com.chinanetcenter.wcs.android.api.FileUploader;
import com.chinanetcenter.wcs.android.api.ParamsConf;
import com.chinanetcenter.wcs.android.listener.SliceUploaderBase64Listener;

import com.quanyan.yhy.R;
import com.quanyan.yhy.net.NetManager;
import com.quanyan.yhy.net.lsn.OnResponseListener;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.smart.sdk.api.resp.Api_SNSCENTER_POIInfo;
import com.smart.sdk.api.resp.Api_SNSCENTER_UgcInfo;
import com.yhy.common.base.YHYBaseApplication;
import com.yhy.common.utils.SPUtils;
import com.yhy.router.YhyRouter;
import com.yhy.service.IUserService;
import com.yixia.camera.VCamera;

import java.io.File;
import java.io.FileOutputStream;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

/**
 * Wys视频上传服务
 * Created by Jiervs on 2018/5/25.
 */

public class UpLoadUgcVideoService extends Service {

    public static final String MEDIA_PATH = "mediaPath";
    public static final String UGC_INFO = "ugcInfo";
    private String mediaPath;
    private UgcInfo_Parcelable ugcInfo;
    private ParamsConf conf;
    private String tempThumbnail;//暂时存储在本地的封面路径
    private String ThumbnailUrl;//上传到阿里云的封面路径

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
        ugcInfo = intent.getParcelableExtra(UGC_INFO);
        if (mediaPath != null && ugcInfo != null) {
            getThumbnail(mediaPath);
            doUpLoad(WsyUtils.generateUpLoadToken(),new File(mediaPath).getName(),mediaPath);
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
     * 给视频生成预览帧
     */
    public String getThumbnail(String str) {
        //设置视频路径
        String path = str;
        //将路径实例化为一个文件对象
        File file = new File(path);
        tempThumbnail = "";
        //判断对象是否存在，不存在的话给出Toast提示
        if (file.exists()) {
            //提供统一的接口用于从一个输入媒体中取得帧和元数据
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(path);
            Bitmap bitmap = retriever.getFrameAtTime(1 * 1000 * 1000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
            tempThumbnail = VCamera.getVideoCachePath() + "tempThumbnail.png";
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(tempThumbnail);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ToastUtil.showToast(YHYBaseApplication.getInstance(),getString(R.string.record_video_file_not_exist));
        }
        return tempThumbnail;
    }

    /**
     * 上传视频
     * @param token
     * @param filename
     * @param filePath
     */
    public void doUpLoad(String token , String filename, String filePath) {

        FileUploader.setUploadUrl(WsyUtils.wsy_upload_url);
        ClientConfig config = new ClientConfig();
        config.setMaxConcurrentRequest(10);
        FileUploader.setClientConfig(config);

        conf = new ParamsConf();
        // 原始文件名称
        conf.fileName = filename;
        // 通过表单参数设置文件保存到云存储的名称
        String mediaType = filename.substring(filename.lastIndexOf(".") + 1);
        SimpleDateFormat format =  new SimpleDateFormat("yyyyMMddHHmmss");
        Long time= System.currentTimeMillis();
        String date = format.format(time);
        conf.keyName = "user/"+ userService.getLoginUserId()+"-"+ date +"."+ mediaType;
        // 通过表单参数设置文件的mimeType
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String type = fileNameMap.getContentTypeFor(filename);
        conf.mimeType = type;
        FileUploader.setParams(conf);
        FileUploader.setBlockConfigs(8, 4 * 1024);
        ToastUtil.showToast(YHYBaseApplication.getInstance(),getString(R.string.tips_upload_video));
        FileUploader.sliceUpload(filePath, YHYBaseApplication.getInstance(), token, new File(filePath), null, new SliceUploaderBase64Listener() {

            @Override
            public void onSliceUploadSucceed(String string) {
                Log.e("WYS-------------------", "slice upload succeeded.");
                ThumbnailUpload(tempThumbnail);
            }

            @Override
            public void onProgress(long uploaded, long total) {
                Log.e("WYS-------------------", String.format(Locale.CHINA, "uploaded : %s, total : %s", uploaded, total));
            }

            @Override
            public void onSliceUploadFailured(HashSet<String> errorMessages) {
                Log.e("WYS-------------------", "slice upload failure.");
                stopSelf();
            }
        });
    }

    /**
     * 上传视频的封面图（该图是上传到阿里云）
     */
    public void ThumbnailUpload(String tempThumbnail){
        List<String> files = new ArrayList<>();
        files.add(tempThumbnail);
        NetManager.getInstance(getApplicationContext()).doUploadImages(files, new OnResponseListener<List<String>>() {
            @Override
            public void onComplete(boolean isOK, List<String> result, int errorCode, String errorMsg) {
                if (isOK) {
                    if (result != null && result.size()>0) {
                        ThumbnailUrl = result.get(0);
                    }
                    publishUploadVideoUgc(ugcInfo);
                }
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                ToastUtil.showToast(YHYBaseApplication.getInstance(),errorMessage);
            }
        });
    }

    /**
     * 发布 从本地上传视频的 Ugc
     */
    public void publishUploadVideoUgc(UgcInfo_Parcelable ugcInfo_parcelable) {

        Api_SNSCENTER_UgcInfo mInfo = new Api_SNSCENTER_UgcInfo();
        //发布者ID
        mInfo.userId = ugcInfo_parcelable.userId;
        if (mInfo.poiInfo == null) {
            mInfo.poiInfo = new Api_SNSCENTER_POIInfo();
            if (ugcInfo_parcelable.poiInfo != null) {
                mInfo.poiInfo.detail = ugcInfo_parcelable.poiInfo.detail;
                mInfo.poiInfo.latitude =  ugcInfo_parcelable.poiInfo.latitude;
                mInfo.poiInfo.longitude = ugcInfo_parcelable.poiInfo.longitude;
            }
        }
        mInfo.textContent = ugcInfo_parcelable.textContent;
        mInfo.videoUrl = conf.keyName;
        mInfo.videoPicUrl = ThumbnailUrl;
        mInfo.shortVideoType = "UPLOAD_VIDEO";

        NetManager.getInstance(getApplicationContext()).doAddUGC(mInfo, new OnResponseListener<Boolean>() {
            @Override
            public void onComplete(boolean isOK, Boolean result, int errorCode, String errorMsg) {
                if (result) {
                    ToastUtil.showToast(YHYBaseApplication.getInstance(),"上传视频成功");
                }
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                ToastUtil.showToast(YHYBaseApplication.getInstance(),errorMessage);
            }
        });
    }
}
