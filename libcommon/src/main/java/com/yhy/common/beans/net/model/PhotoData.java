package com.yhy.common.beans.net.model;

import android.graphics.Bitmap;
import android.text.TextUtils;


import com.yhy.common.beans.album.MediaItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class PhotoData implements Serializable {

    private static final long serialVersionUID = 454334906434482297L;
    public String mTfsUrl;//图片所在服务器位置
    public String mLocalUrl;//图片所在本地位置
    public Bitmap mBmp;//图片
    public String mThumbnail;//缩略图

    public static PhotoData ImageItem2PhotoData(MediaItem im){
        PhotoData mp = new PhotoData();
        if (!TextUtils.isEmpty(im.mediaPath)){
            mp.mLocalUrl = im.mediaPath;
        }

        if(!TextUtils.isEmpty(im.thumbnailPath)){
            mp.mThumbnail = im.thumbnailPath;
        }
        return mp;
    }

    public static List<PhotoData> ImageItem2PhotoData(List<MediaItem> imlist){
        if(imlist == null ){
            return null;
        }
        List<PhotoData> mplist = new ArrayList<PhotoData>();
        for(MediaItem tmp : imlist){
            mplist.add(ImageItem2PhotoData(tmp));
        }
        return mplist;
    }

    public static PhotoData ImageItem2PhotoData(String im){
        PhotoData mp = new PhotoData();
        if (!TextUtils.isEmpty(im)){
            mp.mLocalUrl = im;
        }
        return mp;
    }

    public static List<PhotoData> String2PhotoData(List<String> imlist){
        if(imlist == null ){
            return null;
        }
        List<PhotoData> mplist = new ArrayList<PhotoData>();
        for(String tmp : imlist){
            mplist.add(ImageItem2PhotoData(tmp));
        }
        return mplist;
    }
}
