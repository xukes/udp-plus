package com.xukeer.udp.plus.utils;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * 带有优先级属性的任务
 * */
public class PrioritizedTask<V> extends FutureTask<V> implements Comparable<PrioritizedTask<V>> {

	private final int priority;
	
	public PrioritizedTask(Callable<V> callable, int priority) {
		super(callable);
		this.priority = priority;
	}
	
	@Override
	public int compareTo(PrioritizedTask<V> o) {
		if( this.priority == o.priority ) {
			return 0;
		}
		else {
			return priority < o.priority ? -1 : 1;
		}
	}
	
}
