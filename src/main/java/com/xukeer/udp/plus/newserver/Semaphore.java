package com.xukeer.udp.plus.newserver;

/**
 * 信号量类
 */
public class Semaphore<T> {
    private T t;

    public void setValue(T t) {
        synchronized (this) {
            this.t = t;
            notify();
        }
    }

    public T waitRsp() throws InterruptedException {
        synchronized (this) {
            wait(100);
            return t;
        }
    }
}
