package com.xukeer.test.client;

import com.xukeer.udp.plus.utils.ScheduleUtil;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/*
 * @author xqw
 * @description
 * @date 9:38 2021/11/25
 **/
public class IteratorTest {
    static final List<String> list = new LinkedList<>();
    public static void main(String[] args) {
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        list.add("6");
        ScheduleUtil.timer().scheduleAtFixedRate(()->{
            synchronized (list) {
                Iterator<String> iterator = list.iterator();
                while (iterator.hasNext()) {
                    String str = iterator.next();
                    if (str.equals("5")) {
                        addList(str);
                    }
                }
            }
        },0,2, TimeUnit.SECONDS);


    }

    private static void addList(String str){
        synchronized (list) {
            System.out.println(str);
            list.add(str);
        }}
}
