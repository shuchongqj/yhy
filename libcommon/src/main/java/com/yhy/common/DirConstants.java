package com.yhy.common;

import android.os.Environment;

public class DirConstants {
	//APP目录
	public static final String DIR_WORK = Environment.getExternalStorageDirectory() + "/min5/";
	//图片缓存
	public static final String DIR_PICTURE = DIR_WORK + "pic/";
	//短视频路径
	public static final String DIR_VIDEOS = DIR_WORK + "videos/";
	//网络下载的短视频缓存位置
	public static final String DIR_VIDEOS_CACHE = DIR_VIDEOS + "cache/";
	//app更新的目录
	public static final String DIR_UPDATE_APP = DIR_WORK + "app/";
	//缩略图的目录
	public static final String DIR_PIC_THUMB = DIR_PICTURE + "thumb/";
	//原图目录
	public static final String DIR_PIC_ORIGIN = DIR_PICTURE + "origin/";
	//分享图片的缓存
	public static final String DIR_PIC_SHARE = DIR_PICTURE + "share_images/";
	//证书缓存
	public static final String DIR_PFX = DIR_WORK + "/pfx/";
	//日志缓存
	public static final String DIR_LOGS = DIR_WORK + "logs/";
	//数据库缓存
	public static final String DIR_DB = DIR_WORK + "db/";
	//数据缓存
	public static final String DIR_CACHE = DIR_WORK + "cache/";
	//图片缓存路径
	public static final String DIR_CACHE_BIG_PIC = DIR_CACHE + "image_big";
	//小图片缓存路径
	public static final String DIR_CACHE_SMALL_PIC = DIR_CACHE + "image_small";
	//app更新的目录
	public static final String DIR_UPDATE_APATCH = DIR_WORK + "apatch/";
	//头像保存的名称
	public static final String USER_HEAD_ICON = "user_head_icon.png";
	//主题
	public static final String DIR_THEMES = DIR_WORK + "themes/";
}
