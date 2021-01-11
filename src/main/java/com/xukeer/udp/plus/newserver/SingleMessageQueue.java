package com.xukeer.udp.plus.newserver;

import java.util.concurrent.ExecutionException;

public class SingleMessageQueue {
    private SimpleMsgBody simpleMsgBody;
    private Semaphore<Integer> semaphore;

    public SingleMessageQueue(Semaphore semaphore) {
        this.semaphore = semaphore;
    }

    public Integer addMessage(SimpleMsgBody simpleMsgBody) throws InterruptedException, ExecutionException {

        this.simpleMsgBody = simpleMsgBody;
        System.out.println(2);
        Integer i;
        synchronized (this) {
            notify();
        }
        i = semaphore.waitRsp();
        return i;
    }

    public SimpleMsgBody read() throws InterruptedException {
        synchronized (this) {
            wait();
            System.out.println(1);
            return simpleMsgBody;
        }
    }

    public Semaphore getSemphore() {
        return this.semaphore;
    }
}
