package com.xukeer.test.lianxi;

import com.xukeer.udp.plus.utils.ScheduleUtil;

/*
 * @author xqw
 * @description
 * @date 9:50 2021/12/6
 **/
public class ProTest {
    public void hello(String str) {
        ScheduleUtil.threadPool().execute(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(str);
                }
        );

    }
}
