package com.xukeer.udp.utils;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class PrioritizedTask<V> extends FutureTask<V> implements Comparable<PrioritizedTask<V>> {

	private int priority;
	
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
