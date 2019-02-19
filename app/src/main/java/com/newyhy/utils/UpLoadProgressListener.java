package com.newyhy.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.TrafficStats;

import java.util.Timer;
import java.util.TimerTask;


/**
 * 当前app流量监听类(需要传入Context,与上传文件总大小,并调用start(),和setRXListener(),停止时调用stop()比较好，有时timer停不下来
 * ，你们自行优化，解决了可以和我说一声)
 * 如果需要监听下载流量，可利用rx的值，具体逻辑差不多，自行实现
 * @author TJbaobao
 *
 */
public class UpLoadProgressListener {
	private Context context ;
	private Timer timer;
	private Timer_Task timerTask ;
	private int speed ;//当前速度 kb/s
	private RXListener rxListener ;//自定义监听器
	/**
	 * 构造方法
	 * @param context
	 */
	public UpLoadProgressListener(Context context)
	{
		this.context = context ;
	}
	private class Timer_Task extends TimerTask
	{
		long lastRX = -1;
		@Override
		public void run() {
			try {
				PackageManager manager = context.getPackageManager();
				PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
				int uId = info.applicationInfo.uid;
				long rx = TrafficStats.getUidRxBytes(uId); // 获取接收的流量
				long tx = TrafficStats.getUidTxBytes(uId); // 获取发送的流量
				if(lastRX==-1)
				{
					lastRX = tx;
				}
				long nowTX = tx - lastRX ;
				//计算当前的值
				speed = (int) (nowTX/1024f);

				lastRX = tx;

				//Tools.printLog("当前时间截里的总流量:"+nowTX+"当前速度:"+speed+",当前进度:"+progress+"当前上传数据:"+allTX+",总数据大小:"+fileSize);
				rxListener.getSpeed(speed);
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 启动流量监听器线程
	 */
	public void start()
	{
		timer = new Timer();
		timerTask = new Timer_Task();
		timer.schedule(timerTask, 0,1000);
	}
	/**
	 * 停止流量监听器线程
	 */
	public void stop()
	{
		if(timer!=null)
		{
			timer.cancel();
			timer = null;
		}
		if(timerTask!=null)
		{
			timerTask.cancel();
			timerTask = null;
		}
	}
	/**
	 * 流量监听器
	 * @param rxListener
	 */
	public void setRXListener(RXListener rxListener)
	{
		this.rxListener = rxListener;
	}
	public interface RXListener
	{
		/**
		 * 获取当前速度kb/s
		 * @param speed
		 */
		public void getSpeed(int speed);

	}
}
