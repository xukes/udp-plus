package com.xukeer.test.server;

import com.xukeer.udp.plus.utils.ScheduleUtil;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/*
 * @author xqw
 * @description
 * @date 9:41 2021/11/11
 **/
public class FutureTest {
    private static final A a = new A();
    private static int count = 0;

    private static boolean isFinish=false;

    public static void main(String[] args) {
        Runnable r = () -> {
            try {
                synchronized (a) {
                    a.wait();
                    isFinish=true;
                }
            } catch (InterruptedException ignored) {
            }
        };
        ScheduleUtil.threadPool().execute(() -> {
            Future<?> future = ScheduleUtil.threadPool().submit(r);
            try {
                Integer a = (Integer) future.get();
                System.out.println(count);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
//
        ScheduleUtil.threadPool().execute(() -> {
            try {
                Thread.sleep(1000);
                synchronized (a) {
                    a.notify();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        });

        ScheduleUtil.threadPool().execute(() -> {
            try {
                while (!isFinish) {
                    Thread.sleep(100);
                    System.out.println(String.format("count=%d", count));
                    count = count + 2;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    private static Integer get() {
        System.out.println("get count");
        return count;
    }

    private static class A {
    }
}
