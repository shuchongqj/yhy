package com.quanyan.yhy.net;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class NetThreadManager {
	private static NetThreadManager pool;
	private int corePoolSize;
	private int maximumPoolSize;
	private long keepAliveTime;
	private ThreadPoolExecutor threadPoolExecutor;
	private TimeUnit unit;
	
	private BlockingQueue<Runnable> workQueue;
	private RejectedExecutionHandler handler;
	private NetThreadManager() {
		if (threadPoolExecutor == null) {
			init();
			threadPoolExecutor = new ThreadPoolExecutor(corePoolSize,
					maximumPoolSize, keepAliveTime, unit, workQueue,handler);
		}
	}

	public static synchronized NetThreadManager initNetThreadManager(){
		if(pool == null){
			pool = new NetThreadManager();

		}
		return pool;
	}
	/**
	 * 获取单例线程池对象,第一次调用将触发初始化线程池
	 */
	public static NetThreadManager getInstance() {
		if (pool == null) {
			initNetThreadManager();
		}
		return pool;
	}

	/**
	 * 初始化线程池
	 */
	private void init() {
		corePoolSize = 5;
		maximumPoolSize = 60;
		keepAliveTime = 20000;
		unit = TimeUnit.MILLISECONDS;
		workQueue = new ArrayBlockingQueue<>(100);
		handler = new ThreadPoolExecutor.DiscardOldestPolicy();
	}

	/**
	 * 到线程池执行一个任务
	 */
	public void execute(Runnable runnable) {
		threadPoolExecutor.execute(runnable);
	}

	/**
	 * 关闭线程池
	 */
	public void shutdown() {
		threadPoolExecutor.shutdown();
	}

}
