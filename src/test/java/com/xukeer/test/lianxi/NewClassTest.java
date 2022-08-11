package com.xukeer.test.lianxi;

import com.xukeer.udp.plus.utils.ScheduleUtil;

import java.util.concurrent.*;

public class NewClassTest {
    public static void main(String[] args) {
       // ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

       new Thread(){
           @Override
           public void run(){
               while (true) {
                   try {
                       Thread.sleep(2000);
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
                   System.out.println(1);
               }
           }
       }.start();
    }
}
