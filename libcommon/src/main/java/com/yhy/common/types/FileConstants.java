
package com.yhy.common.types;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;


import com.yhy.common.utils.AndroidUtils;

import java.io.File;


public class FileConstants {

    /** app 图片文件目录 */
    public static final String IMG_DIR = "imgs";
    /** app 音频消息缓存目录 */
    public static final String AUDIO_DIR = "audios";

    public static final String MY_IMGS = "myimgs";

    public static final String CACHE_DIR = "caches";
    /** im相关的日志文件记录路径 */
    public static final String LOG_DIR = "logs";

    /**
     * app图片缓存目录路径
     * 
     * @return
     */
    public static final String getImageDirPath(Context ctx) {
        String path = AndroidUtils.getDefaultCachePath(ctx);
        if (TextUtils.isEmpty(path)) {
            return "";
        }
        return path + File.separator + IMG_DIR + File.separator;
    }

    /***
     * app音频消息缓存目录路径
     * 
     * @param ctx
     * @return
     */
    public static final String getAudioMsgDirPath(Context ctx) {
        String path = AndroidUtils.getDefaultCachePath(ctx);
        if (TextUtils.isEmpty(path)) {
            return "";
        }
        return path + File.separator + AUDIO_DIR + File.separator;
    }

    /**
     * 
     * 文件缓存地址
     * 
     * @param ctx
     * @return
     */
    public static final String getCacheLocalPath(Context ctx) {
        String path = "";
        if (AndroidUtils.checkSDCard()) {
            path = AndroidUtils.getDefaultCachePath(ctx);
        } else {
            path = ctx.getFilesDir().getAbsolutePath();
            Log.d("zc", "path"+path);

        }
        if (TextUtils.isEmpty(path)) {
            return "";
        }

        path += File.separator + CACHE_DIR + File.separator;

        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    /**
     * 日志文件存储目录
     * 
     * @param ctx
     * @return
     */
    public static final String getLogDirPath(Context ctx) {
        String path = AndroidUtils.getDefaultCachePath(ctx);
        if (TextUtils.isEmpty(path)) {
            return "";
        }
        return path + File.separator + LOG_DIR + File.separator;
    }
}
