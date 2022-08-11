package com.xukeer.udp.plus.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

/**
 * 通用的线程池和定时器
 * @author mirror
 *
 */
public class ScheduleUtil {

	private volatile static ExecutorService threadPoolExecutor = null;
	
	private volatile static ScheduledExecutorService timerExecutor = null;
	
	public static ExecutorService threadPool() {
		if( threadPoolExecutor == null ) {
			synchronized (ScheduleUtil.class) {
				if( threadPoolExecutor == null ) {
					threadPoolExecutor = Executors.newCachedThreadPool();
				}
			}
		}
		return threadPoolExecutor;
	}

	public static ScheduledExecutorService timer() {
		if( timerExecutor == null ) {
			synchronized (ScheduleUtil.class) {
				if( timerExecutor == null ) {
					timerExecutor = Executors.newScheduledThreadPool(2);
				}
			}
		}
		return timerExecutor;
	}
	
	public static void shutdownTimer() {
		if( timerExecutor != null ) {
			timerExecutor.shutdownNow();
			timerExecutor = null;
		}
	}
}
