package com.xukeer.test.client;

import com.xukeer.udp.plus.utils.CacheSet;
import com.xukeer.udp.plus.utils.ScheduleUtil;

import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

/*
 * @author xqw
 * @description
 * @date 14:54 2021/11/15
 **/
public class CommonTest {
    public static void main(String[] args) throws InterruptedException {
//        DecimalFormat df = new DecimalFormat();
//
//        Double s = 100000000.232323232d;
//        System.out.println(df.format(s));

        CacheSet<Integer> cacheSet = new CacheSet<>(2);
        cacheSet.add(2);
        Thread.sleep(2100);
        System.out.println(cacheSet.contains(2));


//        ScheduleUtil.timer().scheduleWithFixedDelay(() -> {
//            log.info("handle");
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//        }, 0, 1, TimeUnit.SECONDS);

    }
}
