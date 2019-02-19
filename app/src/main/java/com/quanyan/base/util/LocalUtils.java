package com.quanyan.base.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;

import com.harwkin.nb.camera.FileUtil;
import com.meituan.android.walle.WalleChannelReader;

import com.quanyan.yhy.common.DirConstants;
import com.quanyan.yhy.common.ErrorCode;
import com.quanyan.yhy.database.DBManager;
import com.quanyan.yhy.eventbus.EvBusUserLoginState;
import com.quanyan.yhy.net.NetManager;
import com.quanyan.yhy.net.NetThreadManager;
import com.quanyan.yhy.net.cache.LocalJsonCache;
import com.quanyan.yhy.net.lsn.OnResponseListener;
import com.quanyan.yhy.ui.common.city.PinyinComparator;
import com.quanyan.yhy.util.CookieUtil;
import com.yhy.common.base.YHYBaseApplication;
import com.yhy.common.beans.city.bean.AddressBean;
import com.yhy.common.utils.JSONUtils;
import com.yhy.common.utils.SPUtils;
import com.yhy.network.api.user.UserApi;
import com.yhy.network.manager.AccountManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import de.greenrobot.event.EventBus;

public class LocalUtils {
	private static final boolean D = true;
	private static final String DEFAULT_TAG = "Harwkin";
	private List<AddressBean> addressBeans;


	/**
	 * 获取渠道号
	 * @param context
	 * @return
	 */
	private static String getChannelFromFile(Context context) {
		ApplicationInfo appinfo = context.getApplicationInfo();
		String sourceDir = appinfo.sourceDir;
		String ret = "";
		ZipFile zipfile = null;
		try {
			zipfile = new ZipFile(sourceDir);
			Enumeration<?> entries = zipfile.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = ((ZipEntry) entries.nextElement());
				String entryName = entry.getName();
				if (entryName.startsWith("META-INF/channel")) {
					ret = entryName;
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (zipfile != null) {
				try {
					zipfile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		String[] split = ret.split("_");
		if (split != null && split.length >= 2) {
			return ret.substring(split[0].length() + 1);
		} else {
			return "";
		}
	}

	/**
	 * 获取渠道号
	 * @param context
	 * @return
     */
	public static String getChannel(Context context) {
		String channal = getChannelFromFile(context);
		if(StringUtil.isEmpty(channal)){
			channal = getMetaDataValue(context,"UMENG_CHANNEL","SJGW");
		}
		return channal.trim();
	}

	/**
	 * 获取渠道名称
	 * @param context
	 * @return
     */
	public static String getChannelName(Context context) {
		if (context == null) {
			return null;
		}
		String channelName = null;
		try {
			PackageManager packageManager = context.getPackageManager();
			if (packageManager != null) {
				//注意此处为ApplicationInfo 而不是 ActivityInfo,因为友盟设置的meta-data是在application标签中，而不是某activity标签中，所以用ApplicationInfo
				ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
				if (applicationInfo != null) {
					if (applicationInfo.metaData != null) {
						channelName = applicationInfo.metaData.getString("");
					}
				}
				/*ActivityInfo activityInfo = packageManager.getActivityInfo(ComponentName.unflattenFromString(context.getPackageName()), packageManager.GET_META_DATA);
				if(activityInfo != null){
					if(activityInfo.metaData != null){
						   channelName = activityInfo.metaData.getString("");
					}
				}*/
			}
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return channelName;
	}

	/**
	 * 获取AndroidManifest.xml某个metadata的值
	 * @param name
	 * @param def
     * @return
     */
	public static String getMetaDataValue(Context context,String name, String def) {
		String value = getMetaDataValue(context,name);
		return (value == null) ? def : value;
	}

	/**
	 * 获取AndroidManifest.xml某个metadata的值
	 * @param context
	 * @param name
     * @return
     */
	public static String getMetaDataValue(Context context,String name) {
		return WalleChannelReader.getChannel(context.getApplicationContext());
	}

	/**
	 * 获取App Version Code
	 * @param context
	 * @return
     */
	public static int getAppVersionCode(Context context) {
		int version = 0;
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			version = info.versionCode;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
			version = 1;
		}
		return version;
	}
	//是否为合法的手机号码
	public static boolean isPhoneNumber(String text) {
		if (text == null || text.length() == 0 || !text.matches("\\d{11}")) {
			return false;
		}
		return true;
	}
	//是否为合法的手机号码
	public static boolean isMobileNO(String mobiles) {
		if(!isPhoneNumber(mobiles)){
			return false;
		}
//		Pattern p = Pattern
//				.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
//		Matcher m = p.matcher(mobiles);
		Pattern p = Pattern
				.compile("^((1[0-9]))\\d{9}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	public static boolean isMainProcess(Context ctx) {
		boolean isMainProcess = false;

		final int pid = android.os.Process.myPid();
		ActivityManager am = (ActivityManager) (ctx
				.getSystemService(Context.ACTIVITY_SERVICE));
		List<ActivityManager.RunningAppProcessInfo> list = am.getRunningAppProcesses();
		if(list == null || list.size() == 0){
			return true;
		}
		for (ActivityManager.RunningAppProcessInfo appProcess : list) {
			if (appProcess.pid == pid) {
				isMainProcess = appProcess.processName.equalsIgnoreCase(ctx.getPackageName());
				break;
			}
		}

		return isMainProcess;
	}



	/**
	 * 跳转到登录页面
	 */
	public static void JumpToLogin(Context context){
		clearLocalCache(context);
		//TODO 新版API 退出登陆
		AccountManager.Companion.getAccountManager().logout();
		NetManager.getInstance(context).release();
		DBManager.getInstance(context.getApplicationContext()).release();
		YHYBaseApplication.getInstance().exitNeedLoginActivity();
		//删除头像
		FileUtil.deleteFile(new File(DirConstants.DIR_PIC_ORIGIN, DirConstants.USER_HEAD_ICON));

		EventBus.getDefault().post(new EvBusUserLoginState(EvBusUserLoginState.LOGOUT_STATE));
	}

	public static void clearLocalCache(Context context){
		SPUtils.clearLogStatus(context);

		//清除json的缓存
		new LocalJsonCache(context).removeAll();
		CookieUtil.cleanCookie(context);
	}

	/**
	 * 发送广播
	 * @param context
	 * @param action
	 */
	public synchronized static void sendBroadcast(Context context,String action){
		context.sendBroadcast(new Intent(action));
	}


	//打电话
	public static void call(Context context,String phoneNum){
		try {
			Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNum));
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		}catch (Exception e){

		}
	}

	/**
	 * 获取缓存大小
	 * @return
	 */
	public static String getCacheSize(Context context){
		long lCacheSize = 0;
		try {
			lCacheSize = FileUtil.getFolderSize(new File(DirConstants.DIR_WORK));
			lCacheSize = lCacheSize + FileUtil.getFolderSize(context.getExternalCacheDir());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return FileUtil.getFormatSize(lCacheSize);
	}

	/**
	 * 清除缓存
	 * @return
	 */
	public static void clearCache(final Context context,final OnResponseListener<Boolean> lsn){
		Runnable requestThread = new Runnable() {
			@Override
			public void run() {
				FileUtil.deleteDirectory(DirConstants.DIR_CACHE);
				FileUtil.deleteDirectory(DirConstants.DIR_LOGS);
				FileUtil.deleteDirectory(DirConstants.DIR_DB);
				FileUtil.deleteDirectory(DirConstants.DIR_PIC_ORIGIN);
				FileUtil.deleteDirectory(DirConstants.DIR_PIC_SHARE);
				FileUtil.deleteDirectory(DirConstants.DIR_PIC_THUMB);
				FileUtil.deleteDirectory(DirConstants.DIR_PICTURE);
				FileUtil.deleteDirectory(DirConstants.DIR_UPDATE_APP);
				FileUtil.deleteDirectory(DirConstants.DIR_VIDEOS);
				FileUtil.deleteDirectory(DirConstants.DIR_VIDEOS_CACHE);
				if(context != null) {
					FileUtil.deleteDirectory(context.getExternalCacheDir().getAbsolutePath());
				}
				DBManager.getInstance(context).deleteVideoTable();
				if(lsn != null){
					lsn.onComplete(true,true,ErrorCode.STATUS_OK,"");
				}
			}
		};
		NetThreadManager.getInstance().execute(requestThread);
	}

	/**
	 * Determine whether the calling process of an IPC or yourself have been
	 * granted a particular permission.
	 *
	 * @param permission The name of the permission being checked.
	 * @return {@link PackageManager#PERMISSION_GRANTED} if the calling
	 */
	public static boolean checkDynamicPermission(Context context, String permission) {
		try {
			PackageManager pm = context.getPackageManager();
			boolean dynamicPermission = PackageManager.PERMISSION_GRANTED == context.getApplicationContext().checkCallingOrSelfPermission(permission);
			return dynamicPermission;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 *
	 * @param context
	 * @return
     */
	public static List<AddressBean> loadCities(Context context){
		InputStreamReader inputReader = null;
		BufferedReader bufReader = null;
		StringBuilder sb = new StringBuilder(2000);
		try {
			inputReader = new InputStreamReader(context.getAssets().open("city.json"));
			bufReader = new BufferedReader(inputReader);
			String line = "";
			while ((line = bufReader.readLine()) != null){
				sb.append(line);
			}
			List<AddressBean> cities = new ArrayList<AddressBean>(JSONUtils.convertToArrayList(sb.toString(), AddressBean.class));
			PinyinComparator pinyinComparator = new PinyinComparator();
			Collections.sort(cities, pinyinComparator);
			return cities;
		}catch (Exception e){
			e.printStackTrace();
		}
		return new ArrayList<>();
	}

	/**
	 * 定位到的城市与本地用的AddressBean对应
	 * @param context
	 * @return
	 */
	public static AddressBean citycodeToLocal(Context context){

		AddressBean bean = null;
		List<AddressBean> addressBeans = new ArrayList<>();
		String cityName = SPUtils.getExtraCurrentCityName(context);
		if(!StringUtil.isEmpty(cityName) && addressBeans != null && addressBeans.size() > 0){
			for (int i = 0; i < addressBeans.size(); i++) {
				if(cityName.contains(addressBeans.get(i).getName())){
					bean = addressBeans.get(i);
				}
			}
		}

		return bean;
	}

	/**
	 * 检查是否有定位的权限
	 * @param context
	 * @return
     */
	public static boolean isPermittedBySystem(Context context){
		try {
			int net = context.checkCallingOrSelfPermission("android.permission.ACCESS_COARSE_LOCATION");
			int gps = context.checkCallingOrSelfPermission("android.permission.ACCESS_FINE_LOCATION");
			return net == PackageManager.PERMISSION_GRANTED || gps == PackageManager.PERMISSION_GRANTED;
		}catch (Exception e){
			e.printStackTrace();
		}
		return false;
	}

	/**
	 *
	 * 是否达到最大缓存
	 * @return
     */
	private static final long MIN_STORAGE_KEEP = 50;
	public static boolean isAlertMaxStorage(){
		if(!isExistSDCard()){
			return true;
		}
		if(getSDFreeSize() < MIN_STORAGE_KEEP){
			return true;
		}

		return false;
	}

	/**
	 * 判断SD卡是否存在
	 * @return
     */
	private static boolean isExistSDCard() {
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			return true;
		} else
			return false;
	}

	/**
	 * 获取SDK卡剩余空间
	 * @return
     */
	private static long getSDFreeSize(){
		//取得SD卡文件路径
		File path = Environment.getExternalStorageDirectory();
		StatFs sf = new StatFs(path.getPath());
		//获取单个数据块的大小(Byte)
		long blockSize = sf.getBlockSize();
		//空闲的数据块的数量
		long freeBlocks = sf.getAvailableBlocks();
		//返回SD卡空闲大小
		//return freeBlocks * blockSize;  //单位Byte
		//return (freeBlocks * blockSize)/1024;   //单位KB
		return (freeBlocks * blockSize)/1024 /1024; //单位MB
	}

	/**
	 * 判断微信是否安装
	 * @param context
	 * @return
     */
	public static boolean isWeixinAvilible(Context context) {
		final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
		List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
		if (pinfo != null) {
			for (int i = 0; i < pinfo.size(); i++) {
				String pn = pinfo.get(i).packageName;
				if (pn.equals("com.tencent.mm")) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * 判断H5是否需要全屏显示
	 * @param url
	 * @return
     */
	public static boolean isNeedFullScreen(String url){
		try {
			Uri uri = Uri.parse(url);
			List<String> isShowTitles = uri.getQueryParameters("isShowTitle");
			if (isShowTitles != null && isShowTitles.size() > 0) {
				if ("true".equals(isShowTitles.get(0))) {
					return false;
				} else {
					return true;
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return false;
	}
}
