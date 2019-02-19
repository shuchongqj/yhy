package com.mogujie.tt.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.mogujie.tt.imservice.event.LoginEvent;
import com.mogujie.tt.imservice.event.SocketEvent;
import com.mogujie.tt.ui.activity.MessageActivity;
import com.quanyan.yhy.R;
import com.yhy.common.beans.im.entity.SearchElement;
import com.yhy.common.beans.im.entity.UserEntity;
import com.yhy.common.constants.IntentConstant;
import com.yhy.common.constants.UrlConstant;
import com.yhy.common.utils.CommonUtil;
import com.yhy.common.utils.pinyin.PinYin;
import com.yhy.imageloader.ImageLoadManager;

public class IMUIHelper {

    // 根据event 展示提醒文案
    public static int getLoginErrorTip(LoginEvent event) {
        switch (event) {
            case LOGIN_AUTH_FAILED:
                return R.string.login_error_general_failed;
            case LOGIN_INNER_FAILED:
                return R.string.login_error_unexpected;
            default:
                return R.string.login_error_unexpected;
        }
    }

    public static int getSocketErrorTip(SocketEvent event) {
        switch (event) {
            case CONNECT_MSG_SERVER_FAILED:
                return R.string.connect_msg_server_failed;
            case REQ_MSG_SERVER_ADDRS_FAILED:
                return R.string.req_msg_server_addrs_failed;
            default:
                return R.string.login_error_unexpected;
        }
    }

    // 跳转到聊天页面
    public static void openChatActivity(Context ctx, String sessionKey) {
        Intent intent = new Intent(ctx, MessageActivity.class);
        intent.putExtra(IntentConstant.KEY_SESSION_KEY, sessionKey);
        ctx.startActivity(intent);
    }

    // 对话框回调函数
    public interface dialogCallback {
        void callback();
    }

    // 文字高亮显示
    public static void setTextHilighted(TextView textView, String text, SearchElement searchElement) {
        textView.setText(text);
        if (textView == null
                || TextUtils.isEmpty(text)
                || searchElement == null) {
            return;
        }

        int startIndex = searchElement.startIndex;
        int endIndex = searchElement.endIndex;
        if (startIndex < 0 || endIndex > text.length()) {
            return;
        }
        // 开始高亮处理
        int color = Color.rgb(69, 192, 26);
        textView.setText(text, BufferType.SPANNABLE);
        Spannable span = (Spannable) textView.getText();
        span.setSpan(new ForegroundColorSpan(color), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }


    /**
     * 如果图片路径是以  http开头,直接返回
     * 如果不是， 需要集合自己的图像路径生成规律
     *
     * @param avatarUrl
     * @return
     */
    public static String getRealAvatarUrl(String avatarUrl) {
        if (avatarUrl.toLowerCase().contains("http")) {
            return avatarUrl;
        } else if (avatarUrl.trim().isEmpty()) {
            return "";
        } else {
            return UrlConstant.AVATAR_URL_PREFIX + avatarUrl;
        }
    }


    public static boolean handleContactSearch(String key, UserEntity contact) {
        if (TextUtils.isEmpty(key) || contact == null) {
            return false;
        }

        contact.getSearchElement().reset();

        return handleTokenFirstCharsSearch(key, contact.getPinyinElement(), contact.getSearchElement())
                || handleTokenPinyinFullSearch(key, contact.getPinyinElement(), contact.getSearchElement())
                || handleNameSearch(contact.getMainName(), key, contact.getSearchElement());
        // 原先是 contact.name 代表花名的意思嘛??
    }

    public static boolean handleNameSearch(String name, String key,
                                           SearchElement searchElement) {
        int index = name.indexOf(key);
        if (index == -1) {
            return false;
        }

        searchElement.startIndex = index;
        searchElement.endIndex = index + key.length();

        return true;
    }

    public static boolean handleTokenFirstCharsSearch(String key, PinYin.PinYinElement pinYinElement, SearchElement searchElement) {
        return handleNameSearch(pinYinElement.tokenFirstChars, key.toUpperCase(), searchElement);
    }

    public static boolean handleTokenPinyinFullSearch(String key, PinYin.PinYinElement pinYinElement, SearchElement searchElement) {
        if (TextUtils.isEmpty(key)) {
            return false;
        }

        String searchKey = key.toUpperCase();

        //onLoginOut the old search result
        searchElement.reset();

        int tokenCnt = pinYinElement.tokenPinyinList.size();
        int startIndex = -1;
        int endIndex = -1;

        for (int i = 0; i < tokenCnt; ++i) {
            String tokenPinyin = pinYinElement.tokenPinyinList.get(i);

            int tokenPinyinSize = tokenPinyin.length();
            int searchKeySize = searchKey.length();

            int keyCnt = Math.min(searchKeySize, tokenPinyinSize);
            String keyPart = searchKey.substring(0, keyCnt);

            if (tokenPinyin.startsWith(keyPart)) {

                if (startIndex == -1) {
                    startIndex = i;
                }

                endIndex = i + 1;
            } else {
                continue;
            }

            if (searchKeySize <= tokenPinyinSize) {
                searchKey = "";
                break;
            }

            searchKey = searchKey.substring(keyCnt, searchKeySize);
        }

        if (!searchKey.isEmpty()) {
            return false;
        }

        if (startIndex >= 0 && endIndex > 0) {
            searchElement.startIndex = startIndex;
            searchElement.endIndex = endIndex;

            return true;
        }

        return false;
    }

    // search helper end


    public static void setViewTouchHightlighted(final View view) {
        if (view == null) {
            return;
        }

        view.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    view.setBackgroundColor(Color.rgb(1, 175, 244));
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    view.setBackgroundColor(Color.rgb(255, 255, 255));
                }
                return false;
            }
        });
    }


    // 这个还是蛮有用的,方便以后的替换
    public static int getDefaultAvatarResId(int sessionType) {
//		if (sessionType == DBConstant.SESSION_TYPE_SINGLE) {
//			return R.mipmap.tt_default_user_portrait_corner;
//		}
//		return R.mipmap.tt_default_user_portrait_corner;
        return R.mipmap.ic_app_launcher;
    }


    public static void setEntityImageViewAvatarNoDefaultPortrait(ImageView imageView,
                                                                 String avatarUrl, int sessionType, int roundPixel) {
        setEntityImageViewAvatarImpl(imageView, avatarUrl, sessionType, false, roundPixel);
    }

    public static void setEntityImageViewAvatarImpl(ImageView imageView,
                                                    String avatarUrl, int sessionType, boolean showDefaultPortrait, int roundPixel) {
        if (avatarUrl == null) {
            avatarUrl = "";
        }

        String fullAvatar = getRealAvatarUrl(avatarUrl);
        int defaultResId = -1;

        if (showDefaultPortrait) {
            defaultResId = getDefaultAvatarResId(sessionType);
        }

        displayImage(imageView, fullAvatar, defaultResId, roundPixel);
    }

    public static void displayImage(ImageView imageView,
                                    String resourceUri, int defaultResId, int roundPixel) {

        Logger logger = Logger.getLogger(IMUIHelper.class);

        logger.d("displayimage#displayImage resourceUri:%s, defeaultResourceId:%d", resourceUri, defaultResId);

        if (resourceUri == null) {
            resourceUri = "";
        }

        boolean showDefaultImage = !(defaultResId <= 0);

        if (TextUtils.isEmpty(resourceUri) && !showDefaultImage) {
            logger.e("displayimage#, unable to display image");
            return;
        }


//        DisplayImageOptions options;
//        if (showDefaultImage) {
//            options = new DisplayImageOptions.Builder().
//                    showImageOnLoading(defaultResId).
//                    showImageForEmptyUri(defaultResId).
//                    showImageOnFail(defaultResId).
//                    cacheInMemory(true).
//                    cacheOnDisk(true).
//                    considerExifParams(true).
//                    displayer(new RoundedBitmapDisplayer(roundPixel)).
//                    imageScaleType(ImageScaleType.EXACTLY).// 改善OOM
//                    bitmapConfig(Bitmap.Config.RGB_565).// 改善OOM
//                    build();
//        } else {
//            options = new DisplayImageOptions.Builder().
//                    cacheInMemory(true).
//                    cacheOnDisk(true).
////			considerExifParams(true).
////			displayer(new RoundedBitmapDisplayer(roundPixel)).
////			imageScaleType(ImageScaleType.EXACTLY).// 改善OOM
////			bitmapConfig(Bitmap.Config.RGB_565).// 改善OOM
//        build();
//        }
//
//        ImageLoader.getInstance().displayImage(resourceUri, imageView, options, null);

        ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(resourceUri), defaultResId, imageView, roundPixel);
    }


//    public static void displayImageNoOptions(ImageView imageView,
//                                             String resourceUri, int defaultResId, int roundPixel) {
//
//        Logger logger = Logger.getLogger(IMUIHelper.class);
//
//        logger.d("displayimage#displayImage resourceUri:%s, defeaultResourceId:%d", resourceUri, defaultResId);
//
//        if (resourceUri == null) {
//            resourceUri = "";
//        }
//
//        boolean showDefaultImage = !(defaultResId <= 0);
//
//        if (TextUtils.isEmpty(resourceUri) && !showDefaultImage) {
//            logger.e("displayimage#, unable to display image");
//            return;
//        }

//        DisplayImageOptions options;
//        if (showDefaultImage) {
//            options = new DisplayImageOptions.Builder().
//                    showImageOnLoading(defaultResId).
//                    showImageForEmptyUri(defaultResId).
//                    showImageOnFail(defaultResId).
//                    cacheInMemory(true).
//                    cacheOnDisk(true).
//                    considerExifParams(true).
//                    displayer(new RoundedBitmapDisplayer(roundPixel)).
//                    imageScaleType(ImageScaleType.EXACTLY).// 改善OOM
//                    bitmapConfig(Bitmap.Config.RGB_565).// 改善OOM
//                    build();
//        } else {
//            options = new DisplayImageOptions.Builder().
////                    cacheInMemory(true).
////                    cacheOnDisk(true).
//        imageScaleType(ImageScaleType.EXACTLY).// 改善OOM
//                    bitmapConfig(Bitmap.Config.RGB_565).// 改善OOM
//                    build();
//        }
//        ImageLoader.getInstance().displayImage(resourceUri, imageView, options, null);
//    }

}
